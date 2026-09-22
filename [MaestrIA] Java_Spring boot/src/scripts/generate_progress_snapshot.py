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
  - a recent-log table (planned vs. actual date per day) and an up-next list.

Usage
-----
    python3 generate_progress_snapshot.py
    python3 generate_progress_snapshot.py --today 2026-10-01   # backdate for testing
    python3 generate_progress_snapshot.py --roadmap PATH --out PATH --repo PATH

Only the standard library is used, so no pip install is needed.
"""

from __future__ import annotations

import argparse
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

    dated_completed = [
        (n, roadmap.days[n].actual_date) for n in completed if roadmap.days[n].actual_date
    ]
    if dated_completed:
        last_day, last_actual = max(dated_completed, key=lambda t: t[0])
        if last_actual == today:
            status_value, status_color = "On time", "var(--good)"
            status_label = f"Logged today, {today.isoformat()}"
        else:
            days_since = (today - last_actual).days
            status_value, status_color = f"{days_since}d ago", "var(--ink-primary)"
            status_label = f"Day {last_day} logged {last_actual.isoformat()}"
    else:
        status_value, status_color = "Unlogged", "var(--ink-primary)"
        status_label = "No dated completions yet"

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
  .page { max-width: 760px; margin: 0 auto; display: flex; flex-direction: column; gap: 28px; }
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
  footer { text-align: center; font-size: 12px; color: var(--ink-muted); padding-top: 4px; }
  footer .mono { font-size: 11.5px; }
</style>

<div class="page">
  <header>
    <div class="eyebrow">Java + Spring Boot &middot; $TOTAL_DAYS-Day Roadmap</div>
    <h1>Reflection Point</h1>
    <p class="subhead">
      A pace check on the roadmap, generated on <code>$TODAY</code>.
      $INTRO_NOTE
    </p>
  </header>

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

  <footer>
    <div>Source: <span class="mono">Roadmap.md</span> master index &amp; per-day <span class="mono">Planned date</span> lines, cross-checked against <span class="mono">git log</span>.</div>
    <div class="mono">Snapshot generated $TODAY by src/scripts/generate_progress_snapshot.py</div>
  </footer>
</div>
""")


def build_html(roadmap: Roadmap, pace: PaceProjection, today: date) -> str:
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

    return TEMPLATE.substitute(
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

    out_html = build_html(roadmap, pace, today)
    args.out.write_text(out_html, encoding="utf-8")

    completed = len(roadmap.completed_numbers)
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
