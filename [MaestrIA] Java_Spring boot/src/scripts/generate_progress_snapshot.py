#!/usr/bin/env python3
"""Generate a progress snapshot page ("Reflection Point") from Roadmap.md.

What it reads
-------------
- Roadmap.md's master index table (day / status / date / Java track /
  Spring track / roadmap.sh mapping / note) to get overall progress and
  the "up next" queue.
- Roadmap.md's per-day detail sections, specifically the
  ``**Planned date:** YYYY-MM-DD`` line, optionally annotated as
  ``**Planned date:** YYYY-MM-DD (completed YYYY-MM-DD)`` when a day slipped.
- ``git log --all`` in this repo, matching ``Day<N>`` / ``day-<N>`` in commit
  subjects, as a fallback source of "actual completion date" for days that
  don't carry an explicit ``(completed ...)`` annotation.

What it writes
---------------
An HTML snapshot (self-contained, no build step) with:
  - an overall progress bar with week ticks,
  - a to-scale bar chart comparing "on-plan pace" vs "currently observed
    pace" finish dates,
  - a caveat about how many real data points that observed pace rests on,
  - a recent-log table (planned vs. actual date per day) and an up-next list,
  - an activity calendar,
  - a "Random review" button: the notes of completed days ("[Day NN] ....md"
    in this folder) are embedded in the page, and the button shows one of
    them, favouring days not reviewed recently. It shows the note's interview
    questions with hidden answers (or the full note when it has none).
    Review dates are kept in the browser's localStorage. Markdown is rendered
    with src/scripts/vendor/marked.min.js (MIT), inlined into the page; if
    that file is missing, notes show as plain text. Regenerate the page after
    adding a note.

Usage
-----
    python3 generate_progress_snapshot.py
    python3 generate_progress_snapshot.py --today 2026-10-01   # backdate for testing
    python3 generate_progress_snapshot.py --roadmap PATH --out PATH --repo PATH

Only the standard library is used, so no pip install is needed.
Opening the page needs no network: everything is inlined.
"""

from __future__ import annotations

import argparse
import json
import calendar
import html
import re
import subprocess
import sys
from dataclasses import dataclass, field
from datetime import date, datetime, timedelta
from pathlib import Path
from string import Template

# ---------------------------------------------------------------------------
# Paths
# ---------------------------------------------------------------------------

SCRIPT_DIR = Path(__file__).resolve().parent
STUDY_DIR = SCRIPT_DIR.parent.parent  # .../src/scripts -> "[MaestrIA] Java_Spring boot"
DEFAULT_ROADMAP = STUDY_DIR / "Roadmap.md"
DEFAULT_OUT = STUDY_DIR / "progress-snapshot.html"

# Study cadence: Monday-Saturday, Sunday off (see Roadmap.md "Calendar rule").
STUDY_DAYS_PER_WEEK = 6
SUNDAY = 6  # datetime.weekday() value


# ---------------------------------------------------------------------------
# Data model
# ---------------------------------------------------------------------------

@dataclass
class RoadmapDay:
    number: int
    completed: bool
    table_date_raw: str
    java_track: str
    spring_track: str
    planned_date: date | None = None
    actual_date: date | None = None
    actual_date_source: str | None = None  # "annotation" | "git" | None


@dataclass
class Roadmap:
    days: dict[int, RoadmapDay] = field(default_factory=dict)

    @property
    def total(self) -> int:
        return max(self.days) if self.days else 0

    @property
    def completed_numbers(self) -> list[int]:
        return sorted(n for n, d in self.days.items() if d.completed)

    @property
    def upcoming_numbers(self) -> list[int]:
        return sorted(n for n, d in self.days.items() if not d.completed)


# ---------------------------------------------------------------------------
# Parsing
# ---------------------------------------------------------------------------

MASTER_ROW_RE = re.compile(
    r"^\|\s*(\d+)\s*\|\s*([^|]+?)\s*\|\s*([^|]+?)\s*\|\s*([^|]+?)\s*\|\s*([^|]+?)\s*\|\s*([^|]+?)\s*\|\s*([^|]+?)\s*\|\s*$"
)
DAY_HEADING_RE = re.compile(r"^##\s+Day\s+(\d+)\s+—")
PLANNED_DATE_RE = re.compile(
    r"^\*\*Planned date:\*\*\s*(\d{4}-\d{2}-\d{2})(?:\s*\(completed\s*(\d{4}-\d{2}-\d{2})\))?"
)
DATE_RE = re.compile(r"^\d{4}-\d{2}-\d{2}$")


def parse_date(s: str) -> date:
    return datetime.strptime(s, "%Y-%m-%d").date()


def parse_master_table(text: str) -> dict[int, RoadmapDay]:
    days: dict[int, RoadmapDay] = {}
    for line in text.splitlines():
        m = MASTER_ROW_RE.match(line)
        if not m:
            continue
        num_s, status, table_date, java_track, spring_track, _mapping, _note = m.groups()
        num = int(num_s)
        completed = "✅" in status
        days[num] = RoadmapDay(
            number=num,
            completed=completed,
            table_date_raw=table_date.strip(),
            java_track=java_track.strip(),
            spring_track=spring_track.strip(),
        )
    return days


def parse_detail_planned_dates(text: str, days: dict[int, RoadmapDay]) -> None:
    current_day: int | None = None
    for line in text.splitlines():
        heading = DAY_HEADING_RE.match(line)
        if heading:
            current_day = int(heading.group(1))
            continue
        m = PLANNED_DATE_RE.match(line.strip())
        if m and current_day is not None and current_day in days:
            planned_s, completed_s = m.groups()
            days[current_day].planned_date = parse_date(planned_s)
            if completed_s:
                days[current_day].actual_date = parse_date(completed_s)
                days[current_day].actual_date_source = "annotation"


def fill_planned_dates_from_table(days: dict[int, RoadmapDay]) -> None:
    """For upcoming days, the master table's Date column *is* the planned date."""
    for d in days.values():
        if d.planned_date is None and DATE_RE.match(d.table_date_raw):
            d.planned_date = parse_date(d.table_date_raw)


# ---------------------------------------------------------------------------
# Git-derived actual completion dates
# ---------------------------------------------------------------------------

GIT_DAY_RE = re.compile(r"day[\s-]*0*?(\d{1,3})\b", re.IGNORECASE)


def git_day_dates(repo_root: Path) -> dict[int, date]:
    """Earliest commit date (any branch) whose subject mentions "Day N"."""
    try:
        proc = subprocess.run(
            ["git", "log", "--all", "--date=short", "--pretty=format:%ad|%s"],
            cwd=repo_root,
            capture_output=True,
            text=True,
            check=True,
        )
    except (subprocess.CalledProcessError, FileNotFoundError) as exc:
        print(f"warning: could not read git log ({exc}); actual dates limited to "
              f"explicit (completed ...) annotations in Roadmap.md", file=sys.stderr)
        return {}

    result: dict[int, date] = {}
    for line in proc.stdout.splitlines():
        if "|" not in line:
            continue
        date_s, subject = line.split("|", 1)
        try:
            d = parse_date(date_s)
        except ValueError:
            continue
        m = GIT_DAY_RE.search(subject)
        if not m:
            continue
        day_num = int(m.group(1))
        if day_num == 0 or day_num > 999:
            continue
        if day_num not in result or d < result[day_num]:
            result[day_num] = d
    return result


def git_activity_dates(repo_root: Path) -> set[date]:
    """Every date with at least one commit whose subject mentions "Day N"."""
    try:
        proc = subprocess.run(
            ["git", "log", "--all", "--date=short", "--pretty=format:%ad|%s"],
            cwd=repo_root,
            capture_output=True,
            text=True,
            check=True,
        )
    except (subprocess.CalledProcessError, FileNotFoundError):
        return set()

    result: set[date] = set()
    for line in proc.stdout.splitlines():
        if "|" not in line:
            continue
        date_s, subject = line.split("|", 1)
        if not GIT_DAY_RE.search(subject):
            continue
        try:
            result.add(parse_date(date_s))
        except ValueError:
            continue
    return result


def find_repo_root(start: Path) -> Path | None:
    try:
        proc = subprocess.run(
            ["git", "rev-parse", "--show-toplevel"],
            cwd=start,
            capture_output=True,
            text=True,
            check=True,
        )
        return Path(proc.stdout.strip())
    except (subprocess.CalledProcessError, FileNotFoundError):
        return None


# ---------------------------------------------------------------------------
# Pace math
# ---------------------------------------------------------------------------

def next_study_date(d: date) -> date:
    """The next study day after ``d`` (Mon-Sat, skipping Sunday)."""
    nxt = d + timedelta(days=1)
    if nxt.weekday() == SUNDAY:
        nxt += timedelta(days=1)
    return nxt


def walk_study_days(anchor: date, steps: int) -> date:
    d = anchor
    for _ in range(steps):
        d = next_study_date(d)
    return d


@dataclass
class PaceProjection:
    anchor_date: date
    remaining: int
    on_plan_finish: date
    on_plan_days: int
    current_pace_finish: date | None
    current_pace_days: int | None
    interval_count: int  # how many calendar-day intervals the current pace is based on
    avg_interval_days: float | None


def compute_pace(roadmap: Roadmap, today: date) -> PaceProjection:
    completed = roadmap.completed_numbers
    remaining = roadmap.total - len(completed)

    dated = [
        (n, roadmap.days[n].actual_date)
        for n in completed
        if roadmap.days[n].actual_date is not None
    ]
    dated.sort()

    anchor_date = dated[-1][1] if dated else today
    on_plan_finish = walk_study_days(anchor_date, remaining)
    on_plan_days = (on_plan_finish - today).days

    intervals = [
        (dated[i][1] - dated[i - 1][1]).days
        for i in range(1, len(dated))
        if (dated[i][1] - dated[i - 1][1]).days > 0
    ]

    current_pace_finish = None
    current_pace_days = None
    avg_interval = None
    if intervals:
        avg_interval = sum(intervals) / len(intervals)
        current_pace_finish = anchor_date + timedelta(days=round(avg_interval * remaining))
        current_pace_days = (current_pace_finish - today).days

    return PaceProjection(
        anchor_date=anchor_date,
        remaining=remaining,
        on_plan_finish=on_plan_finish,
        on_plan_days=on_plan_days,
        current_pace_finish=current_pace_finish,
        current_pace_days=current_pace_days,
        interval_count=len(intervals),
        avg_interval_days=avg_interval,
    )


# ---------------------------------------------------------------------------
# HTML rendering
# ---------------------------------------------------------------------------

def esc(s: str) -> str:
    return html.escape(s, quote=True)


def shorten(s: str, limit: int = 92) -> str:
    s = s.strip()
    if len(s) <= limit:
        return s
    cut = s[:limit].rsplit(" ", 1)[0]
    return cut + "…"


def render_tick_marks(total: int) -> str:
    weeks = -(-total // STUDY_DAYS_PER_WEEK)  # ceil
    ticks = []
    for i in range(1, weeks):
        pct = i * STUDY_DAYS_PER_WEEK / total * 100
        ticks.append(f'<i style="left:{pct:.2f}%"></i>')
    return "".join(ticks)


def render_stat_row(roadmap: Roadmap, today: date) -> str:
    completed = roadmap.completed_numbers
    completed_count = len(completed)
    remaining = roadmap.total - completed_count

    upcoming = roadmap.upcoming_numbers
    if upcoming:
        next_day = roadmap.days[upcoming[0]]
        next_label = f"Day {next_day.number}"
        next_sub = (
            f"Up next · due {next_day.planned_date.strftime('%a %m-%d')}"
            if next_day.planned_date
            else "Up next"
        )
    else:
        next_label, next_sub = "Done", "No days left in the plan"

    # Schedule status: compare the plan with today, not "did I log something today".
    # Behind = the next planned day's date has already passed.
    dated_completed = [
        (n, roadmap.days[n].actual_date) for n in completed if roadmap.days[n].actual_date
    ]
    last_logged = (
        max(dated_completed, key=lambda t: t[0]) if dated_completed else None
    )
    last_part = (
        f"last: Day {last_logged[0]} on {last_logged[1].strftime('%m-%d')}"
        if last_logged
        else "no dated completions yet"
    )

    if not upcoming:
        status_value, status_color = "Done", "var(--good)"
        status_label = "Plan complete"
    elif roadmap.days[upcoming[0]].planned_date is None:
        status_value, status_color = "No plan date", "var(--ink-primary)"
        status_label = last_part
    else:
        next_due = roadmap.days[upcoming[0]].planned_date
        behind_days = (today - next_due).days
        overdue = [
            n for n in upcoming
            if roadmap.days[n].planned_date and roadmap.days[n].planned_date < today
        ]
        if behind_days <= 0:
            status_value, status_color = "On track", "var(--good)"
            status_label = f"Day {upcoming[0]} due {next_due.strftime('%a %m-%d')} · {last_part}"
        else:
            status_value, status_color = f"{behind_days}d behind", "var(--warning)"
            status_label = (
                f"{len(overdue)} planned day{'s' if len(overdue) != 1 else ''} overdue · {last_part}"
            )

    return f"""
    <div class="stat">
      <div class="stat-value tnum">{completed_count}</div>
      <div class="stat-label">Days completed</div>
    </div>
    <div class="stat">
      <div class="stat-value tnum">{remaining}</div>
      <div class="stat-label">Days remaining</div>
    </div>
    <div class="stat">
      <div class="stat-value mono">{esc(next_label)}</div>
      <div class="stat-label">{esc(next_sub)}</div>
    </div>
    <div class="stat">
      <div class="stat-value" style="color:{status_color}">{esc(status_value)}</div>
      <div class="stat-label">{esc(status_label)}</div>
    </div>
    """


def render_pace_chart(pace: PaceProjection) -> str:
    on_plan_days = max(pace.on_plan_days, 0)
    current_days = pace.current_pace_days

    scale_top = max(on_plan_days, current_days or 0, 10)
    chart_max = ((scale_top // 50) + 1) * 50
    left_margin = 20
    drawable = 600
    px_per_day = drawable / chart_max

    def bar_width(days: int) -> float:
        return max(round(days * px_per_day), 3)

    on_plan_w = bar_width(on_plan_days)
    ref_x = left_margin + on_plan_w

    svg_parts = [
        f'<line class="ref-line" x1="{ref_x}" y1="34" x2="{ref_x}" y2="178"></line>',
        f'<text class="axis-label" x="{ref_x + 4}" y="26">{on_plan_days}-day mark</text>',
        f'<text class="row-label" x="20" y="52">If you hold the plan’s cadence</text>',
        f'<rect x="{left_margin}" y="60" width="{on_plan_w}" height="26" rx="6" fill="var(--accent)"></rect>',
        f'<text class="value-label" x="{left_margin + on_plan_w + 10}" y="70" fill="var(--accent-strong)">{on_plan_days} days</text>',
        f'<text class="date-label" x="{left_margin + on_plan_w + 10}" y="86">&rarr; {pace.on_plan_finish.strftime("%a, %b %-d %Y")}</text>',
    ]

    if current_days is not None:
        current_w = bar_width(current_days)
        svg_parts += [
            f'<text class="row-label" x="20" y="122">At your current logged pace</text>',
            f'<rect x="{left_margin}" y="130" width="{current_w}" height="26" rx="6" fill="var(--warning)"></rect>',
            f'<text class="value-label" x="20" y="178" fill="var(--warning)">{current_days} days</text>',
            f'<text class="date-label" x="94" y="178">&rarr; {pace.current_pace_finish.strftime("%a, %b %-d %Y")}</text>',
        ]
    else:
        svg_parts += [
            f'<text class="row-label" x="20" y="122">At your current logged pace</text>',
            f'<rect x="{left_margin}" y="130" width="{drawable}" height="26" rx="6" '
            f'fill="none" stroke="var(--border)" stroke-dasharray="4 4"></rect>',
            f'<text class="date-label" x="{left_margin + 10}" y="147">'
            f'not enough dated completions yet (need at least 2)</text>',
        ]

    svg_parts += [
        f'<line class="axis" x1="{left_margin}" y1="196" x2="{left_margin + drawable}" y2="196"></line>',
        f'<text class="axis-label" x="{left_margin}" y="207">Today</text>',
        f'<text class="axis-label" x="{left_margin + drawable}" y="207" text-anchor="end">+{chart_max} days</text>',
    ]
    return "\n      ".join(svg_parts)


def render_caveat(pace: PaceProjection) -> str:
    if pace.interval_count == 0:
        return (
            "<strong>No pace projection yet:</strong> at least two dated completions "
            "are needed to measure a calendar-day interval. Right now there's only one "
            "(or zero). The \"current pace\" bar will appear once a second day lands "
            "with a real date attached."
        )
    if pace.interval_count == 1:
        return (
            "<strong>Read this one with caution:</strong> there's only a single measured "
            f"interval behind the \"current pace\" line so far "
            f"({pace.avg_interval_days:.0f} calendar days for one topic). That's a "
            f"{pace.remaining}&times; extrapolation off one sample, not a trend. It'll "
            "sharpen every time another day lands with a real date attached."
        )
    return (
        f"<strong>Pace based on {pace.interval_count} intervals</strong> averaging "
        f"{pace.avg_interval_days:.1f} calendar days per completed topic. Still worth "
        "revisiting as more days land &mdash; a handful of samples can swing a lot."
    )


def render_recent_log(roadmap: Roadmap, limit: int = 3) -> str:
    rows = []
    for n in sorted(roadmap.completed_numbers, reverse=True):
        d = roadmap.days[n]
        if d.actual_date and d.planned_date:
            rows.append(d)
        if len(rows) >= limit:
            break
    rows.reverse()

    if not rows:
        return '<tr><td colspan="4" style="color:var(--ink-muted)">No dated completions logged yet.</td></tr>'

    out = []
    for d in rows:
        late = (d.actual_date - d.planned_date).days
        if late <= 0:
            chip = '<span class="chip good">on time</span>'
        else:
            chip = f'<span class="chip warning">+{late} days</span>'
        out.append(
            f"""<tr>
            <td class="day-cell">{d.number}</td>
            <td class="date-cell tnum">{d.planned_date.strftime('%m-%d %a')}</td>
            <td class="date-cell tnum">{d.actual_date.strftime('%m-%d %a')}</td>
            <td>{chip}</td>
          </tr>"""
        )
    return "\n          ".join(out)


def render_up_next(roadmap: Roadmap, limit: int = 3) -> str:
    upcoming = roadmap.upcoming_numbers[:limit]
    if not upcoming:
        return '<li class="upnext-item"><span class="upnext-body">Plan complete.</span></li>'

    out = []
    for n in upcoming:
        d = roadmap.days[n]
        title = shorten(f"{esc(d.java_track)} · {esc(d.spring_track)}")
        date_label = d.planned_date.strftime("%a, %b %-d %Y") if d.planned_date else "date TBD"
        out.append(
            f"""<li class="upnext-item">
          <span class="upnext-badge">{d.number}</span>
          <span class="upnext-body">
            <span class="upnext-title">{title}</span>
            <span class="upnext-date">{date_label}</span>
          </span>
        </li>"""
        )
    return "\n        ".join(out)


def render_activity_calendar(roadmap: Roadmap, activity: set[date], today: date) -> str:
    """Month grids from the first tracked date to today.

    green  = something was done that day (a roadmap day completed, or a "Day N" commit)
    red    = weekday (Mon-Fri) with nothing done
    black  = weekend (Sat/Sun) with nothing done
    Days before tracking started, today without activity, and future days stay neutral.
    """
    tracked = sorted(d for d in activity if d <= today)
    if not tracked:
        return '<p class="cal-empty">No dated activity yet.</p>', 0, 0
    start = tracked[0]

    active_count = 0
    missed_count = 0
    months = []
    y, m = start.year, start.month
    while (y, m) <= (today.year, today.month):
        cells = []
        first_weekday, days_in_month = calendar.monthrange(y, m)  # Monday = 0
        cells.extend('<span class="cal-day blank"></span>' for _ in range(first_weekday))
        for day in range(1, days_in_month + 1):
            d = date(y, m, day)
            weekend = d.weekday() >= 5
            if d < start or d > today:
                cls, label = "untracked", "not tracked" if d < start else "upcoming"
            elif d in activity:
                cls, label = "active", "done"
                active_count += 1
            elif d == today:
                cls, label = "today", "today"
            elif weekend:
                cls, label = "weekend", "weekend"
            else:
                cls, label = "missed", "nothing done"
                missed_count += 1
            if d == today:
                cls += " is-today"
            cells.append(
                f'<span class="cal-day {cls}" title="{d.strftime("%a %Y-%m-%d")} · {label}">{day}</span>'
            )
        head = "".join(f'<span class="cal-dow">{n}</span>' for n in ("M", "T", "W", "T", "F", "S", "S"))
        months.append(
            f"""<div class="cal-month">
          <div class="cal-title">{date(y, m, 1).strftime("%B %Y")}</div>
          <div class="cal-grid">{head}{"".join(cells)}</div>
        </div>"""
        )
        y, m = (y + 1, 1) if m == 12 else (y, m + 1)

    return "\n        ".join(months), active_count, missed_count


# ---------------------------------------------------------------------------
# Random review: embed completed-day notes so the page can show one for review
# ---------------------------------------------------------------------------
#
# The page is opened straight from disk (file://), where browsers refuse to
# read other local files. So the notes are read here, at generation time, and
# embedded in the page as JSON. Regenerate the page after adding a note.

NOTE_FILE_RE = re.compile(r"^\[Day\s*(\d+)\]\s*(.*?)\.md$", re.IGNORECASE)
HEADING_RE = re.compile(r"^(#{1,6})\s+(.*?)\s*#*\s*$")
FENCE_RE = re.compile(r"^\s*(```|~~~)")
QUESTION_SECTION_RE = re.compile(
    r"interview\s+(questions|concepts|cheat\s*sheet|traps)|for\s+interviews", re.IGNORECASE
)
FRONT_MATTER_RE = re.compile(r"\A---\s*\n(.*?)\n---\s*\n", re.DOTALL)
DAY_PREFIX_RE = re.compile(r"^(\[Day\s*\d+\]|(Java\s+)?Day\s*\d+)\s*([—–:]|-{1,3})?\s*", re.IGNORECASE)
ANSWER_SECTION_RE = re.compile(r"senior\s+interview\s+answer", re.IGNORECASE)
NUMBERED_ITEM_RE = re.compile(r"^\s*(\d+)\.\s+(.*)$")
VENDOR_MARKED = SCRIPT_DIR / "vendor" / "marked.min.js"


def find_note_files(study_dir: Path) -> dict[int, Path]:
    """Map day number -> note file ("[Day 07] ...md" or "[Day07] ...md").

    When two files claim the same day, the most recently modified one wins.
    """
    found: dict[int, Path] = {}
    for p in study_dir.glob("*.md"):
        m = NOTE_FILE_RE.match(p.name)
        if not m:
            continue
        n = int(m.group(1))
        if n not in found or p.stat().st_mtime > found[n].stat().st_mtime:
            found[n] = p
    return found


def _headings(lines: list[str]) -> list[tuple[int, int, str]]:
    """(line index, level, text) for real headings, skipping fenced code blocks."""
    out = []
    in_fence = False
    for i, line in enumerate(lines):
        if FENCE_RE.match(line):
            in_fence = not in_fence
            continue
        if in_fence:
            continue
        m = HEADING_RE.match(line)
        if m:
            out.append((i, len(m.group(1)), m.group(2).strip()))
    return out


def _section_end(heads: list[tuple[int, int, str]], idx: int, total_lines: int) -> int:
    """End line (exclusive) of the section that starts at heads[idx]."""
    _, level, _ = heads[idx]
    for line_no, lvl, _ in heads[idx + 1:]:
        if lvl <= level:
            return line_no
    return total_lines


def _clean(md: str) -> str:
    """Trim blank lines and trailing horizontal rules from a markdown chunk."""
    lines = md.strip("\n").split("\n")
    while lines and lines[-1].strip() in ("", "---", "***", "___", "-" * 72):
        lines.pop()
    while lines and not lines[0].strip():
        lines.pop(0)
    return "\n".join(lines)


def _numbered_questions(body: list[str]) -> list[str]:
    """Questions written as a numbered list ("1. What is ...?"), with wrapped lines joined."""
    questions: list[str] = []
    in_fence = False
    for line in body:
        if FENCE_RE.match(line):
            in_fence = not in_fence
            continue
        if in_fence:
            continue
        m = NUMBERED_ITEM_RE.match(line)
        if m:
            questions.append(m.group(2).strip())
        elif questions and line.startswith(("   ", "\t")) and line.strip():
            questions[-1] += " " + line.strip()
    return questions


def extract_review(md: str) -> dict:
    """Pull the interview Q&A out of a note.

    Two formats occur in the notes:
      * Q&A: each question is a sub-heading and its answer follows it.
      * Question list: a numbered list of questions, with the answers in a
        separate "Senior Interview Answer(s)" section (shown on demand).
    """
    lines = md.split("\n")
    heads = _headings(lines)
    qa: list[dict] = []
    questions_only: list[str] = []
    answer_blocks: list[str] = []

    for idx, (line_no, level, text) in enumerate(heads):
        if ANSWER_SECTION_RE.search(text):
            end = _section_end(heads, idx, len(lines))
            block = _clean("\n".join(lines[line_no + 1:end]))
            if block:
                answer_blocks.append(block)
            continue
        if not QUESTION_SECTION_RE.search(text):
            continue

        end = _section_end(heads, idx, len(lines))
        subs = [(j, h) for j, h in enumerate(heads) if line_no < h[0] < end and h[1] > level]
        if subs:
            # A numbered list before the first sub-heading is a question list.
            questions_only.extend(_numbered_questions(lines[line_no + 1:subs[0][1][0]]))
            # Q&A format; a sub-heading followed only by a numbered list is a
            # grouping ("## Java", "## Spring") and contributes list questions.
            for k, (j, (s_line, s_level, s_text)) in enumerate(subs):
                s_end = subs[k + 1][1][0] if k + 1 < len(subs) else end
                body = lines[s_line + 1:s_end]
                listed = _numbered_questions(body)
                answer = _clean("\n".join(body))
                if listed and len(listed) >= 2 and not s_text.rstrip().endswith("?"):
                    questions_only.extend(listed)
                elif answer:
                    qa.append({"q": s_text, "a": answer})
                else:
                    questions_only.append(s_text)
        else:
            questions_only.extend(_numbered_questions(lines[line_no + 1:end]))

    return {
        "qa": qa,
        "questions": questions_only,
        "answers": "\n\n---\n\n".join(answer_blocks),
    }


def collect_review_notes(roadmap: Roadmap, study_dir: Path) -> list[dict]:
    """One entry per completed day that has a note file."""
    files = find_note_files(study_dir)
    notes = []
    for n in roadmap.completed_numbers:
        path = files.get(n)
        if not path:
            continue
        md = path.read_text(encoding="utf-8", errors="replace").replace("\r\n", "\n")
        title_m = NOTE_FILE_RE.match(path.name)
        title = title_m.group(2).strip() if title_m else path.stem

        # Joplin exports start with a YAML front-matter block: use its title, then drop it.
        fm = FRONT_MATTER_RE.match(md)
        if fm:
            t = re.search(r"^title:\s*(.+)$", fm.group(1), re.MULTILINE)
            if t:
                title = t.group(1).strip().strip("'\"").replace("\\_", " ")
            md = md[fm.end():]
        else:
            # Otherwise use a level-1 heading near the top, if there is one.
            for line in [l for l in md.split("\n") if l.strip()][:3]:
                hm = HEADING_RE.match(line)
                if hm and len(hm.group(1)) == 1:
                    title = hm.group(2).strip()
                    break
        title = DAY_PREFIX_RE.sub("", title).strip() or title
        review = extract_review(md)
        notes.append({
            "day": n,
            "title": title,
            "file": path.name,
            "qa": review["qa"],
            "questions": review["questions"],
            "answers": review["answers"],
            "full": md,
        })
    return notes


def render_review_data(notes: list[dict]) -> str:
    """JSON for a <script type="application/json"> block (safe against </script>)."""
    data = json.dumps(notes, ensure_ascii=False)
    return data.replace("</", "<\\/").replace("<!--", "<\\!--")


def render_marked_lib() -> str:
    """Inline the vendored markdown renderer, if present (otherwise notes show as plain text)."""
    try:
        js = VENDOR_MARKED.read_text(encoding="utf-8")
    except OSError:
        return "/* marked.min.js not found next to the script: notes render as plain text */"
    return js.replace("</script", "<\\/script")


REVIEW_CSS = r"""
  .head-row { display: flex; align-items: flex-end; justify-content: space-between; gap: 16px; flex-wrap: wrap; }
  .review-btn {
    font: inherit; font-size: 14px; font-weight: 700; cursor: pointer;
    color: #1B1406; background: var(--accent-strong); border: 0; border-radius: 10px;
    padding: 10px 16px; box-shadow: var(--shadow); display: inline-flex; align-items: center; gap: 8px;
  }
  .review-btn:hover { filter: brightness(1.06); }
  .review-btn:focus-visible, .rv-btn:focus-visible { outline: 2px solid var(--ink-primary); outline-offset: 2px; }
  .review-btn kbd, .rv-hint kbd {
    font-family: "JetBrains Mono", monospace; font-size: 11px; border-radius: 4px; padding: 0 5px;
    border: 1px solid color-mix(in srgb, currentColor 40%, transparent);
  }
  .rv-overlay {
    position: fixed; inset: 0; z-index: 50; display: none; align-items: flex-start; justify-content: center;
    background: color-mix(in srgb, #05080C 62%, transparent); padding: 32px 16px; overflow-y: auto;
  }
  .rv-overlay.open { display: flex; }
  .rv-panel {
    width: min(860px, 100%); background: var(--surface); border: 1px solid var(--border);
    border-radius: 16px; box-shadow: var(--shadow); padding: 24px 26px 28px;
  }
  .rv-top { display: flex; justify-content: space-between; align-items: flex-start; gap: 12px; }
  .rv-day { font-family: "JetBrains Mono", monospace; font-size: 12px; font-weight: 600; color: var(--accent-strong); letter-spacing: .04em; }
  .rv-title { margin: 4px 0 0; font-size: 22px; font-weight: 800; letter-spacing: -.01em; }
  .rv-meta { margin-top: 6px; font-size: 12.5px; color: var(--ink-muted); }
  .rv-close { flex: none; background: none; border: 1px solid var(--border); color: var(--ink-secondary); border-radius: 8px; width: 34px; height: 34px; cursor: pointer; font-size: 18px; line-height: 1; }
  .rv-actions { margin-top: 18px; display: flex; flex-wrap: wrap; gap: 8px; }
  .rv-btn {
    font: inherit; font-size: 13px; font-weight: 600; cursor: pointer; border-radius: 8px; padding: 8px 12px;
    background: var(--surface-2); color: var(--ink-primary); border: 1px solid var(--border);
  }
  .rv-btn.primary { background: var(--good); border-color: var(--good); color: #fff; }
  .rv-btn[aria-pressed="true"] { border-color: var(--accent-strong); color: var(--accent-strong); }
  .rv-hint { margin-top: 10px; font-size: 12px; color: var(--ink-muted); }
  .rv-section-title { margin: 22px 0 8px; font-size: 12px; font-weight: 700; letter-spacing: .05em; text-transform: uppercase; color: var(--ink-muted); }
  .rv-qa details { border: 1px solid var(--border); border-radius: 10px; margin-top: 8px; background: var(--surface); }
  .rv-qa summary { cursor: pointer; padding: 11px 14px; font-weight: 600; font-size: 14.5px; line-height: 1.45; list-style: none; display: flex; gap: 10px; }
  .rv-qa summary::-webkit-details-marker { display: none; }
  .rv-qa summary::before { content: "+"; flex: none; width: 18px; color: var(--accent-strong); font-family: "JetBrains Mono", monospace; }
  .rv-qa details[open] summary::before { content: "\2212"; }
  .rv-qa details[open] summary { border-bottom: 1px solid var(--border); }
  .rv-qa .rv-answer { padding: 4px 16px 12px 42px; }
  .rv-qlist { margin: 0; padding-left: 30px; }
  .rv-qlist li { margin: 6px 0; font-size: 14.5px; line-height: 1.5; }
  .rv-empty { color: var(--ink-secondary); font-size: 14px; }
  .md { font-size: 14px; line-height: 1.6; color: var(--ink-secondary); overflow-wrap: anywhere; }
  .md h1, .md h2, .md h3, .md h4 { color: var(--ink-primary); line-height: 1.3; margin: 1.2em 0 .4em; }
  .md h1 { font-size: 20px; } .md h2 { font-size: 17px; } .md h3, .md h4 { font-size: 15px; }
  .md p, .md ul, .md ol, .md blockquote, .md pre, .md table { margin: .55em 0; }
  .md pre { background: var(--surface-2); border: 1px solid var(--border); border-radius: 8px; padding: 10px 12px; overflow-x: auto; }
  .md pre code { background: none; border: 0; padding: 0; font-size: 12.5px; }
  .md blockquote { border-left: 3px solid var(--accent); margin-left: 0; padding: 2px 12px; color: var(--ink-secondary); }
  .md table { border-collapse: collapse; display: block; overflow-x: auto; }
  .md th, .md td { border: 1px solid var(--border); padding: 5px 8px; text-transform: none; letter-spacing: 0; font-size: 13px; }
  .md hr { border: 0; border-top: 1px solid var(--border); margin: 1em 0; }
  .md .plain { white-space: pre-wrap; font-family: "JetBrains Mono", monospace; font-size: 12.5px; }
  @media (max-width: 640px) { .rv-panel { padding: 18px; } .rv-qa .rv-answer { padding-left: 16px; } }
"""


REVIEW_HTML = r"""
<div class="rv-overlay" id="rv-overlay" aria-hidden="true">
  <div class="rv-panel" role="dialog" aria-modal="true" aria-labelledby="rv-title">
    <div class="rv-top">
      <div>
        <div class="rv-day" id="rv-day"></div>
        <h2 class="rv-title" id="rv-title"></h2>
        <div class="rv-meta" id="rv-meta"></div>
      </div>
      <button class="rv-close" id="rv-close" aria-label="Close review">&times;</button>
    </div>
    <div class="rv-actions">
      <button class="rv-btn primary" id="rv-done">Mark as reviewed</button>
      <button class="rv-btn" id="rv-next">Next note</button>
      <button class="rv-btn" id="rv-reveal">Show all answers</button>
      <button class="rv-btn" id="rv-full" aria-pressed="false">Show full note</button>
    </div>
    <div class="rv-hint">Try to answer each question out loud before opening it. <kbd>R</kbd> next &middot; <kbd>Esc</kbd> close</div>
    <div id="rv-body"></div>
  </div>
</div>
"""


REVIEW_JS = r"""
(function () {
  var notes = [];
  try { notes = JSON.parse(document.getElementById('review-data').textContent || '[]'); } catch (e) { notes = []; }
  var btn = document.getElementById('review-btn');
  if (!btn) return;
  if (!notes.length) { btn.disabled = true; btn.title = 'No completed notes found'; return; }

  var KEY_LAST = 'jfl-review-last';      // { day: 'YYYY-MM-DD' }
  var KEY_RECENT = 'jfl-review-recent';  // [day, day, day] most recent first
  function load(key, fallback) { try { var v = localStorage.getItem(key); return v ? JSON.parse(v) : fallback; } catch (e) { return fallback; } }
  function save(key, value) { try { localStorage.setItem(key, JSON.stringify(value)); } catch (e) {} }

  function todayIso() { var d = new Date(); return d.getFullYear() + '-' + String(d.getMonth() + 1).padStart(2, '0') + '-' + String(d.getDate()).padStart(2, '0'); }
  function daysSince(iso) { if (!iso) return null; var a = new Date(iso + 'T00:00:00'), b = new Date(todayIso() + 'T00:00:00'); return Math.round((b - a) / 86400000); }

  // Least recently reviewed notes are more likely; never-reviewed ones most of all.
  function pick() {
    var last = load(KEY_LAST, {}), recent = load(KEY_RECENT, []);
    var pool = notes.filter(function (n) { return recent.indexOf(n.day) === -1; });
    if (!pool.length) pool = notes.slice();
    var weights = pool.map(function (n) { var s = daysSince(last[n.day]); return s === null ? 90 : Math.min(s, 60) + 1; });
    var total = weights.reduce(function (a, b) { return a + b; }, 0), r = Math.random() * total;
    for (var i = 0; i < pool.length; i++) { r -= weights[i]; if (r <= 0) return pool[i]; }
    return pool[pool.length - 1];
  }

  var hasMarked = typeof window.marked !== 'undefined' && window.marked && typeof window.marked.parse === 'function';
  function md(text) {
    var div = document.createElement('div'); div.className = 'md';
    if (hasMarked) { div.innerHTML = window.marked.parse(text || '', { gfm: true, breaks: false }); }
    else { var pre = document.createElement('div'); pre.className = 'plain'; pre.textContent = text || ''; div.appendChild(pre); }
    return div;
  }
  function inline(text) {
    if (!hasMarked) { var s = document.createElement('span'); s.textContent = text; return s; }
    var span = document.createElement('span'); span.innerHTML = window.marked.parseInline(text); return span;
  }
  function el(tag, cls, text) { var e = document.createElement(tag); if (cls) e.className = cls; if (text != null) e.textContent = text; return e; }

  var overlay = document.getElementById('rv-overlay'), body = document.getElementById('rv-body');
  var fullBtn = document.getElementById('rv-full'), revealBtn = document.getElementById('rv-reveal'), doneBtn = document.getElementById('rv-done');
  var current = null, lastFocus = null;

  function renderMeta(n) {
    var s = daysSince(load(KEY_LAST, {})[n.day]);
    document.getElementById('rv-meta').textContent =
      (s === null ? 'Never reviewed' : s === 0 ? 'Reviewed today' : 'Last reviewed ' + s + (s === 1 ? ' day ago' : ' days ago')) + ' · ' + n.file;
  }

  function renderReview(n) {
    body.innerHTML = '';
    var any = false;
    if (n.qa.length) {
      any = true;
      body.appendChild(el('div', 'rv-section-title', 'Interview questions — click to reveal the answer'));
      var box = el('div', 'rv-qa');
      n.qa.forEach(function (item) {
        var d = document.createElement('details'), sum = document.createElement('summary');
        sum.appendChild(inline(item.q)); d.appendChild(sum);
        var ans = md(item.a); ans.classList.add('rv-answer'); d.appendChild(ans);
        box.appendChild(d);
      });
      body.appendChild(box);
    }
    if (n.questions.length) {
      any = true;
      body.appendChild(el('div', 'rv-section-title', 'Answer these without notes'));
      var ol = el('ol', 'rv-qlist');
      n.questions.forEach(function (q) { var li = document.createElement('li'); li.appendChild(inline(q)); ol.appendChild(li); });
      body.appendChild(ol);
      if (n.answers) {
        var d2 = document.createElement('details');
        var wrap = el('div', 'rv-qa'); var sum2 = document.createElement('summary'); sum2.textContent = 'Senior interview answers';
        d2.appendChild(sum2); var a2 = md(n.answers); a2.classList.add('rv-answer'); d2.appendChild(a2);
        wrap.appendChild(d2); body.appendChild(wrap);
      }
    }
    if (!any) {
      body.appendChild(el('p', 'rv-empty', 'This note has no interview-questions section, so here is the full note.'));
      body.appendChild(md(n.full));
    }
    revealBtn.hidden = !any;
  }

  function show(n) {
    current = n;
    var recent = load(KEY_RECENT, []).filter(function (d) { return d !== n.day; });
    recent.unshift(n.day); save(KEY_RECENT, recent.slice(0, Math.min(3, Math.max(0, notes.length - 1))));
    document.getElementById('rv-day').textContent = 'DAY ' + String(n.day).padStart(2, '0');
    document.getElementById('rv-title').textContent = n.title;
    renderMeta(n);
    fullBtn.setAttribute('aria-pressed', 'false'); fullBtn.textContent = 'Show full note';
    doneBtn.textContent = 'Mark as reviewed'; doneBtn.disabled = false;
    renderReview(n);
    overlay.scrollTop = 0;
  }

  function open() {
    lastFocus = document.activeElement;
    overlay.classList.add('open'); overlay.setAttribute('aria-hidden', 'false');
    document.body.style.overflow = 'hidden';
    show(pick());
    doneBtn.focus();
  }
  function close() {
    overlay.classList.remove('open'); overlay.setAttribute('aria-hidden', 'true');
    document.body.style.overflow = '';
    if (lastFocus && lastFocus.focus) lastFocus.focus();
  }

  btn.addEventListener('click', open);
  document.getElementById('rv-close').addEventListener('click', close);
  document.getElementById('rv-next').addEventListener('click', function () { show(pick()); });
  overlay.addEventListener('click', function (e) { if (e.target === overlay) close(); });
  doneBtn.addEventListener('click', function () {
    if (!current) return;
    var last = load(KEY_LAST, {}); last[current.day] = todayIso(); save(KEY_LAST, last);
    renderMeta(current); doneBtn.textContent = 'Reviewed ✓'; doneBtn.disabled = true;
  });
  revealBtn.addEventListener('click', function () {
    var all = body.querySelectorAll('details'), openAll = Array.prototype.some.call(all, function (d) { return !d.open; });
    all.forEach(function (d) { d.open = openAll; });
    revealBtn.textContent = openAll ? 'Hide all answers' : 'Show all answers';
  });
  fullBtn.addEventListener('click', function () {
    if (!current) return;
    var on = fullBtn.getAttribute('aria-pressed') !== 'true';
    fullBtn.setAttribute('aria-pressed', String(on));
    fullBtn.textContent = on ? 'Back to questions' : 'Show full note';
    revealBtn.textContent = 'Show all answers';
    if (on) { body.innerHTML = ''; body.appendChild(md(current.full)); revealBtn.hidden = true; }
    else { renderReview(current); }
    overlay.scrollTop = 0;
  });
  document.addEventListener('keydown', function (e) {
    var t = e.target && e.target.tagName;
    if (t === 'INPUT' || t === 'TEXTAREA') return;
    if (e.key === 'Escape' && overlay.classList.contains('open')) { close(); }
    else if ((e.key === 'r' || e.key === 'R') && !e.ctrlKey && !e.metaKey && !e.altKey) {
      if (overlay.classList.contains('open')) show(pick()); else open();
    }
  });
})();
"""


TEMPLATE = Template(r"""<!doctype html>
<meta charset="utf-8">
<title>Reflection Point</title>
<meta name="viewport" content="width=device-width, initial-scale=1, viewport-fit=cover">
<link rel="preconnect" href="https://fonts.googleapis.com">
<link rel="preconnect" href="https://fonts.gstatic.com" crossorigin>
<link rel="stylesheet" href="https://fonts.googleapis.com/css2?family=Manrope:wght@500;600;700;800&family=JetBrains+Mono:wght@400;500;600&display=swap">
<style>
  :root {
    --bg: #F4F6F8;
    --surface: #FFFFFF;
    --surface-2: #EEF1F4;
    --border: #DCE2E8;
    --ink-primary: #131A22;
    --ink-secondary: #4B5A68;
    --ink-muted: #66727E;
    --accent: #93670F;
    --accent-strong: #7A5609;
    --good: #00806B;
    --warning: #C1531F;
    --cal-active: #1E9C5A; --cal-missed: #D23F3F; --cal-weekend: #111418; --cal-weekend-ink: #8B96A3;
    --shadow: 0 1px 2px rgba(19, 26, 34, 0.04), 0 8px 24px -12px rgba(19, 26, 34, 0.12);
    padding-top: env(safe-area-inset-top, 0px);
    padding-bottom: env(safe-area-inset-bottom, 0px);
  }
  @media (prefers-color-scheme: dark) {
    :root:not([data-theme="light"]) {
      --bg: #10141B; --surface: #171C25; --surface-2: #1E2430; --border: #2B3341;
      --ink-primary: #EDF1F5; --ink-secondary: #B7C0CB; --ink-muted: #8B96A3;
      --accent: #C99B3E; --accent-strong: #E0B15A; --good: #1E9C82; --warning: #CC5F26;
      --shadow: 0 1px 2px rgba(0,0,0,.3), 0 12px 28px -14px rgba(0,0,0,.55);
    }
  }
  :root[data-theme="dark"] {
    --bg: #10141B; --surface: #171C25; --surface-2: #1E2430; --border: #2B3341;
    --ink-primary: #EDF1F5; --ink-secondary: #B7C0CB; --ink-muted: #8B96A3;
    --accent: #C99B3E; --accent-strong: #E0B15A; --good: #1E9C82; --warning: #CC5F26;
    --shadow: 0 1px 2px rgba(0,0,0,.3), 0 12px 28px -14px rgba(0,0,0,.55);
  }
  * { box-sizing: border-box; }
  body {
    margin: 0; background: var(--bg); color: var(--ink-primary);
    font-family: "Manrope", -apple-system, "Segoe UI", sans-serif;
    padding-inline: 20px; padding-block: 40px 64px;
  }
  [hidden] { display: none !important; }
  .layout {
    max-width: 1148px; margin: 0 auto; display: grid; gap: 28px;
    grid-template-columns: minmax(0, 760px) 360px; justify-content: center;
    grid-template-areas: "head head" "main side" "foot foot";
  }
  .layout > header { grid-area: head; }
  .page { grid-area: main; min-width: 0; display: flex; flex-direction: column; gap: 28px; }
  .side { grid-area: side; align-self: start; position: sticky; top: 24px; }
  .layout > footer { grid-area: foot; }
  @media (max-width: 1200px) {
    .layout { max-width: 760px; grid-template-columns: minmax(0, 1fr); grid-template-areas: "head" "main" "side" "foot"; }
    .side { position: static; }
  }
  .mono { font-family: "JetBrains Mono", ui-monospace, "SF Mono", monospace; }
  .tnum { font-variant-numeric: tabular-nums; }
  .eyebrow {
    font-family: "JetBrains Mono", monospace; font-size: 12px; font-weight: 500;
    letter-spacing: .11em; text-transform: uppercase; color: var(--ink-muted);
  }
  h1 { margin: 6px 0 0; font-size: clamp(28px, 5vw, 36px); font-weight: 800; letter-spacing: -.01em; text-wrap: balance; }
  .subhead { margin: 8px 0 0; color: var(--ink-secondary); font-size: 15px; line-height: 1.55; max-width: 60ch; }
  .subhead code {
    font-family: "JetBrains Mono", monospace; font-size: .92em; background: var(--surface-2);
    border: 1px solid var(--border); border-radius: 4px; padding: .05em .4em; color: var(--ink-primary);
  }
  .card { background: var(--surface); border: 1px solid var(--border); border-radius: 16px; box-shadow: var(--shadow); padding: 26px; }
  .card-title { margin: 0; font-size: 13px; font-weight: 700; letter-spacing: .04em; text-transform: uppercase; color: var(--ink-muted); }
  .hero-top { display: flex; align-items: baseline; justify-content: space-between; gap: 12px; flex-wrap: wrap; }
  .hero-pct { font-size: 44px; font-weight: 800; letter-spacing: -.02em; color: var(--accent-strong); }
  .hero-frac { font-family: "JetBrains Mono", monospace; font-size: 15px; color: var(--ink-secondary); }
  .bar-wrap { margin-top: 20px; position: relative; }
  .bar-track { position: relative; height: 16px; border-radius: 999px; background: var(--surface-2); border: 1px solid var(--border); overflow: hidden; }
  .bar-fill { position: absolute; inset: 0; width: $BAR_PCT%; border-radius: 999px; background: linear-gradient(90deg, var(--accent) 0%, var(--accent-strong) 100%); }
  .bar-ticks { position: absolute; inset: 0; pointer-events: none; }
  .bar-ticks i { position: absolute; top: -4px; bottom: -4px; width: 1px; background: color-mix(in srgb, var(--ink-muted) 35%, transparent); }
  .bar-caption { margin-top: 10px; display: flex; justify-content: space-between; font-family: "JetBrains Mono", monospace; font-size: 11.5px; color: var(--ink-muted); letter-spacing: .02em; }
  .stat-row { margin-top: 24px; display: grid; grid-template-columns: repeat(4, 1fr); gap: 1px; background: var(--border); border: 1px solid var(--border); border-radius: 12px; overflow: hidden; }
  .stat { background: var(--surface); padding: 14px 16px; display: flex; flex-direction: column; gap: 4px; }
  .stat-value { font-size: 21px; font-weight: 700; letter-spacing: -.01em; }
  .stat-label { font-size: 11.5px; color: var(--ink-muted); letter-spacing: .02em; }
  @media (max-width: 620px) { .stat-row { grid-template-columns: repeat(2, 1fr); } }
  .pace-head { display: flex; align-items: baseline; justify-content: space-between; gap: 12px; flex-wrap: wrap; }
  .pace-head p { margin: 4px 0 0; font-size: 13.5px; color: var(--ink-secondary); max-width: 46ch; }
  .pace-chart { margin-top: 18px; width: 100%; height: auto; display: block; }
  .pace-chart text { font-family: "JetBrains Mono", monospace; }
  .pace-chart .row-label { font-family: "Manrope", sans-serif; font-weight: 700; fill: var(--ink-primary); font-size: 13.5px; }
  .pace-chart .value-label { font-weight: 600; font-size: 13px; }
  .pace-chart .date-label { font-size: 11.5px; fill: var(--ink-muted); }
  .pace-chart .axis { stroke: var(--border); stroke-width: 1; }
  .pace-chart .axis-label { font-size: 10.5px; fill: var(--ink-muted); letter-spacing: .03em; }
  .pace-chart .ref-line { stroke: var(--ink-muted); stroke-width: 1; stroke-dasharray: 3 3; opacity: .6; }
  .caveat { margin-top: 18px; border: 1px solid var(--border); border-left: 3px solid var(--warning); background: var(--surface-2); border-radius: 0 10px 10px 0; padding: 12px 16px; font-size: 13px; line-height: 1.6; color: var(--ink-secondary); }
  .caveat strong { color: var(--ink-primary); }
  .twin { display: grid; grid-template-columns: 1fr 1fr; gap: 18px; }
  @media (max-width: 620px) { .twin { grid-template-columns: 1fr; } }
  table { width: 100%; border-collapse: collapse; margin-top: 16px; font-size: 13px; }
  th { text-align: left; font-size: 10.5px; letter-spacing: .04em; text-transform: uppercase; color: var(--ink-muted); font-weight: 600; padding-bottom: 8px; border-bottom: 1px solid var(--border); }
  td { padding: 10px 0; border-bottom: 1px solid var(--border); vertical-align: top; color: var(--ink-secondary); }
  tr:last-child td { border-bottom: none; }
  td.day-cell { font-family: "JetBrains Mono", monospace; font-weight: 600; color: var(--ink-primary); white-space: nowrap; }
  td.date-cell { font-family: "JetBrains Mono", monospace; white-space: nowrap; }
  .chip { display: inline-flex; align-items: center; gap: 5px; font-family: "JetBrains Mono", monospace; font-size: 11px; font-weight: 600; padding: 3px 8px; border-radius: 999px; white-space: nowrap; }
  .chip.good { color: var(--good); background: color-mix(in srgb, var(--good) 14%, transparent); }
  .chip.warning { color: var(--warning); background: color-mix(in srgb, var(--warning) 14%, transparent); }
  .chip::before { content: ""; width: 5px; height: 5px; border-radius: 50%; background: currentColor; }
  .upnext-list { list-style: none; margin: 16px 0 0; padding: 0; display: flex; flex-direction: column; gap: 12px; }
  .upnext-item { display: flex; gap: 12px; align-items: flex-start; }
  .upnext-badge { flex: none; font-family: "JetBrains Mono", monospace; font-size: 11px; font-weight: 600; color: var(--accent-strong); background: color-mix(in srgb, var(--accent) 14%, transparent); border-radius: 6px; padding: 3px 7px; margin-top: 1px; }
  .upnext-body { display: flex; flex-direction: column; gap: 2px; }
  .upnext-title { font-size: 13.5px; color: var(--ink-primary); line-height: 1.4; }
  .upnext-date { font-family: "JetBrains Mono", monospace; font-size: 11.5px; color: var(--ink-muted); }
  .cal-head { display: flex; align-items: baseline; justify-content: space-between; gap: 12px; flex-wrap: wrap; }
  .cal-summary { font-family: "JetBrains Mono", monospace; font-size: 12px; color: var(--ink-secondary); }
  .cal-legend { margin-top: 12px; display: flex; gap: 8px 16px; flex-wrap: wrap; font-size: 12px; color: var(--ink-secondary); }
  .cal-legend span { display: inline-flex; align-items: center; gap: 6px; }
  .cal-legend i { width: 12px; height: 12px; border-radius: 3px; display: inline-block; border: 1px solid var(--border); }
  .cal-months { margin-top: 18px; display: grid; grid-template-columns: repeat(auto-fill, minmax(220px, 1fr)); gap: 22px; }
  .cal-title { font-size: 13px; font-weight: 700; margin-bottom: 8px; }
  .cal-grid { display: grid; grid-template-columns: repeat(7, 1fr); gap: 4px; }
  .cal-dow { font-family: "JetBrains Mono", monospace; font-size: 10px; color: var(--ink-muted); text-align: center; padding-bottom: 2px; }
  .cal-day { aspect-ratio: 1; border-radius: 5px; display: flex; align-items: center; justify-content: center; font-family: "JetBrains Mono", monospace; font-size: 10.5px; font-weight: 600; border: 1px solid transparent; }
  .cal-day.blank { visibility: hidden; }
  .cal-day.active { background: var(--cal-active); color: #fff; }
  .cal-day.missed { background: var(--cal-missed); color: #fff; }
  .cal-day.weekend { background: var(--cal-weekend); color: var(--cal-weekend-ink); border-color: var(--border); }
  .cal-day.untracked { background: var(--surface-2); color: var(--ink-muted); opacity: .55; }
  .cal-day.today { background: var(--surface-2); color: var(--ink-primary); }
  .cal-day.is-today { outline: 2px solid var(--accent-strong); outline-offset: 1px; }
  .cal-legend i.active { background: var(--cal-active); }
  .cal-legend i.missed { background: var(--cal-missed); }
  .cal-legend i.weekend { background: var(--cal-weekend); }
  .cal-empty { margin: 12px 0 0; color: var(--ink-muted); font-size: 13px; }
  footer { text-align: center; font-size: 12px; color: var(--ink-muted); padding-top: 4px; }
  footer .mono { font-size: 11.5px; }
$REVIEW_CSS
</style>

<div class="layout">
  <header>
    <div class="head-row">
    <div>
    <div class="eyebrow">Java + Spring Boot &middot; $TOTAL_DAYS-Day Roadmap</div>
    <h1>Reflection Point</h1>
    <p class="subhead">
      A pace check on the roadmap, generated on <code>$TODAY</code>.
      $INTRO_NOTE
    </p>
    </div>
    <button class="review-btn" id="review-btn" type="button" title="Review a random completed day ($REVIEW_COUNT notes)">Random review <kbd>R</kbd></button>
    </div>
  </header>

  <div class="page">
  <section class="card">
    <div class="hero-top">
      <div>
        <div class="card-title">Overall progress</div>
        <div class="hero-pct tnum">$PCT%</div>
      </div>
      <div class="hero-frac tnum">$COMPLETED / $TOTAL_DAYS days</div>
    </div>

    <div class="bar-wrap">
      <div class="bar-track" role="img" aria-label="$COMPLETED of $TOTAL_DAYS days complete, $PCT percent">
        <div class="bar-fill"></div>
        <div class="bar-ticks">$TICK_MARKS</div>
      </div>
      <div class="bar-caption">
        <span>Day 01</span>
        <span>Week $CUR_WEEK of $TOTAL_WEEKS &middot; ticks mark each $STUDY_DAYS_PER_WEEK-day study week</span>
        <span>Day $TOTAL_DAYS</span>
      </div>
    </div>

    <div class="stat-row">$STAT_ROW</div>
  </section>


  <section class="card">
    <div class="pace-head">
      <div>
        <div class="card-title">Expected day to completion</div>
        <p>Days remaining from today to Day $TOTAL_DAYS, at two different cadences &mdash; drawn to the same scale so the gap is honest.</p>
      </div>
    </div>

    <svg class="pace-chart" viewBox="0 0 700 210" role="img" aria-label="Bar chart comparing completion projections">
      $PACE_SVG
    </svg>

    <div class="caveat">$CAVEAT</div>
  </section>

  <div class="twin">
    <section class="card">
      <div class="card-title">Recent log</div>
      <table>
        <thead><tr><th>Day</th><th>Planned</th><th>Actual</th><th>Status</th></tr></thead>
        <tbody>
          $RECENT_LOG_ROWS
        </tbody>
      </table>
    </section>

    <section class="card">
      <div class="card-title">Up next</div>
      <ul class="upnext-list">
        $UPNEXT_ITEMS
      </ul>
    </section>
  </div>

  </div>

  <aside class="side">
  <section class="card">
      <div class="cal-head">
        <div class="card-title">Activity calendar</div>
        <div class="cal-summary tnum">$CAL_ACTIVE active &middot; $CAL_MISSED missed weekdays</div>
      </div>
      <div class="cal-legend">
        <span><i class="active"></i>Did something</span>
        <span><i class="missed"></i>Weekday, nothing done</span>
        <span><i class="weekend"></i>Weekend, nothing done</span>
      </div>
      <div class="cal-months">
          $CAL_MONTHS
      </div>
    </section>
  </aside>

  <footer>
    <div>Source: <span class="mono">Roadmap.md</span> master index &amp; per-day <span class="mono">Planned date</span> lines, cross-checked against <span class="mono">git log</span>.</div>
    <div class="mono">Snapshot generated $TODAY by src/scripts/generate_progress_snapshot.py</div>
  </footer>
</div>
$REVIEW_HTML
<script id="review-data" type="application/json">$REVIEW_DATA</script>
<script>$REVIEW_LIB</script>
<script>$REVIEW_JS</script>
""")


def build_html(roadmap: Roadmap, pace: PaceProjection, today: date, activity: set[date] | None = None,
               review_notes: list[dict] | None = None) -> str:
    total = roadmap.total
    completed_count = len(roadmap.completed_numbers)
    pct = completed_count / total * 100 if total else 0
    total_weeks = -(-total // STUDY_DAYS_PER_WEEK)
    cur_week = -(-completed_count // STUDY_DAYS_PER_WEEK) or 1

    if completed_count <= 9:
        intro_note = "Not enough dated history yet to measure pace."
    else:
        intro_note = (
            "Days completed before this file started tracking real dates aren't "
            "counted as pace samples &mdash; only days with a known actual date are."
        )

    if activity is None:
        activity = {d.actual_date for d in roadmap.days.values() if d.actual_date}
    cal_months, cal_active, cal_missed = render_activity_calendar(roadmap, activity, today)

    review_notes = review_notes or []

    return TEMPLATE.substitute(
        REVIEW_CSS=REVIEW_CSS,
        REVIEW_HTML=REVIEW_HTML,
        REVIEW_DATA=render_review_data(review_notes),
        REVIEW_LIB=render_marked_lib(),
        REVIEW_JS=REVIEW_JS,
        REVIEW_COUNT=len(review_notes),
        CAL_MONTHS=cal_months,
        CAL_ACTIVE=cal_active,
        CAL_MISSED=cal_missed,
        TODAY=today.isoformat(),
        TOTAL_DAYS=total,
        TOTAL_WEEKS=total_weeks,
        CUR_WEEK=cur_week,
        STUDY_DAYS_PER_WEEK=STUDY_DAYS_PER_WEEK,
        PCT=f"{pct:.1f}",
        COMPLETED=completed_count,
        BAR_PCT=f"{pct:.2f}",
        TICK_MARKS=render_tick_marks(total),
        STAT_ROW=render_stat_row(roadmap, today),
        PACE_SVG=render_pace_chart(pace),
        CAVEAT=render_caveat(pace),
        RECENT_LOG_ROWS=render_recent_log(roadmap),
        UPNEXT_ITEMS=render_up_next(roadmap),
        INTRO_NOTE=intro_note,
    )


# ---------------------------------------------------------------------------
# Main
# ---------------------------------------------------------------------------

def main() -> None:
    parser = argparse.ArgumentParser(description=__doc__, formatter_class=argparse.RawDescriptionHelpFormatter)
    parser.add_argument("--roadmap", type=Path, default=DEFAULT_ROADMAP, help="Path to Roadmap.md")
    parser.add_argument("--out", type=Path, default=DEFAULT_OUT, help="Output HTML path")
    parser.add_argument("--repo", type=Path, default=None, help="Git repo root (default: auto-detected)")
    parser.add_argument("--today", type=str, default=None, help="Override today's date, YYYY-MM-DD (mainly for testing)")
    args = parser.parse_args()

    if not args.roadmap.exists():
        sys.exit(f"error: roadmap not found at {args.roadmap}")

    today = parse_date(args.today) if args.today else date.today()
    repo_root = args.repo or find_repo_root(args.roadmap.parent) or args.roadmap.parent

    text = args.roadmap.read_text(encoding="utf-8")
    days = parse_master_table(text)
    if not days:
        sys.exit("error: no rows matched the master index table format in Roadmap.md")

    parse_detail_planned_dates(text, days)
    fill_planned_dates_from_table(days)

    git_dates = git_day_dates(repo_root)
    for n, d in days.items():
        if d.completed and d.actual_date is None and n in git_dates:
            d.actual_date = git_dates[n]
            d.actual_date_source = "git"

    roadmap = Roadmap(days=days)
    pace = compute_pace(roadmap, today)

    # "Did something" = a roadmap day completed that date, or a commit mentioning "Day N".
    activity = {d.actual_date for d in days.values() if d.actual_date}
    activity |= git_activity_dates(repo_root)

    review_notes = collect_review_notes(roadmap, args.roadmap.parent)

    out_html = build_html(roadmap, pace, today, activity, review_notes)
    args.out.write_text(out_html, encoding="utf-8")

    completed = len(roadmap.completed_numbers)
    print(f"Random review: {len(review_notes)} completed-day notes embedded")
    print(f"Progress: {completed}/{roadmap.total} days ({completed / roadmap.total * 100:.1f}%)")
    print(f"On-plan finish:      {pace.on_plan_finish.isoformat()} ({pace.on_plan_days} days from today)")
    if pace.current_pace_finish:
        print(f"Current-pace finish: {pace.current_pace_finish.isoformat()} "
              f"({pace.current_pace_days} days from today, based on {pace.interval_count} interval(s))")
    else:
        print("Current-pace finish: not enough dated completions yet")
    print(f"Wrote {args.out}")


if __name__ == "__main__":
    main()
