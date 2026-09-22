# Joplin Notes

Personal Joplin notebook, exported to Markdown and versioned in git. Currently
holds one active notebook: a 90-day Java + Spring Boot study plan, with its
notes, its runnable code, and a script that reports progress against the plan.

## Repository layout

```
.
├── README.md
├── _resources/                          # Joplin attachment store (images etc.)
└── [MaestrIA] Java_Spring boot/
    ├── Roadmap.md                       # source of truth: the 90-day plan
    ├── progress-snapshot.html           # generated — see "Tracking progress"
    ├── [Day 01] ... [Day 11] *.md       # one detailed note per finished day
    ├── _old/                            # superseded plans, kept for history
    ├── anki/                            # Anki TSV exports (Front/Back/Tags)
    └── src/
        ├── java_core/                   # one Maven project, plain-Java days
        │   └── src/main/java/org/jfl/DayXX_Topic/...
        ├── spring/
        │   ├── day10_configuration/     # one Maven/Boot module per Spring day
        │   └── day11_reflection/
        └── scripts/
            └── generate_progress_snapshot.py
```

Each study day pairs a Java topic with a Spring topic wherever the two
reinforce each other (annotations ↔ configuration, exceptions ↔
transactions, generics ↔ repositories, …). Plain-Java days live under
`src/java_core` as a package per day; days with a Spring Boot component get
their own Maven module under `src/spring/dayNN_topic`.

## Roadmap.md

`Roadmap.md` is the single source of truth for the plan: a 90-row master
index table (day, status, date, Java track, Spring track, roadmap.sh
mapping, note link), followed by a detailed section per day (tutorial,
runnable mini-lab, verify step, and a four-box "done when" checklist).

Two conventions matter for progress tracking, both enforced by hand when a
day is closed out:

- **Master table row:** status flips from `⬜ Upcoming` to `✅ Done`, and the
  `Date` column changes from a planned date to the literal word `completed`.
- **Detail section's `Planned date` line:** stays as-is when a day lands on
  schedule (`**Planned date:** 2026-09-16`). When it slips, the actual date
  gets appended so the slip is visible without digging through git history:
  `**Planned date:** 2026-09-17 (completed 2026-09-22)`.

When a day slips, every day after it gets rescheduled by the same offset
(master table + detail sections), following the plan's own calendar rule:
study Monday–Saturday, Sunday off, and a slip moves the whole tail forward
rather than compressing two sessions into one day.

## Tracking progress

`src/scripts/generate_progress_snapshot.py` reads `Roadmap.md` (the master
table and the per-day `Planned date` lines) and cross-checks actual
completion dates against `git log --all` — matching `Day N` in commit
subjects — for any completed day that doesn't already carry an explicit
`(completed ...)` annotation. From that it renders `progress-snapshot.html`:
an overall progress bar with week ticks, a to-scale comparison of "finish
date if the plan's cadence holds" vs. "finish date at the currently observed
pace," a recent-log table (planned vs. actual per day), and an up-next
preview. It's stdlib-only Python 3 — no `pip install` required.

Run it from anywhere inside the repo:

```bash
python3 "[MaestrIA] Java_Spring boot/src/scripts/generate_progress_snapshot.py"
```

It writes `progress-snapshot.html` next to `Roadmap.md` and prints a summary
to stdout. Useful flags:

| Flag | Purpose |
|---|---|
| `--roadmap PATH` | Point at a different `Roadmap.md` |
| `--out PATH` | Write the snapshot somewhere else |
| `--repo PATH` | Git repo root, if it can't be auto-detected |
| `--today YYYY-MM-DD` | Backdate "today" (mainly for testing) |

**Workflow:** finish a day → update `Roadmap.md` (status, date/annotation,
checklist) → commit → re-run the script → commit the refreshed
`progress-snapshot.html`. The "currently observed pace" projection is only
as good as the dated history behind it, so the `(completed ...)` annotation
on a slipped day is what keeps it honest — without it, the script falls back
to whatever `git log` can infer, which is coarser.
