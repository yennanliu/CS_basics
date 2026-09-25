---
name: l3-core
description: Report where the L3 core set stands and pick the next problems to drill from it. The set is Blind 75 plus the NeetCode 150 problems README marks MUST — 99 problems, held fixed in data/l3_core.json so its ok share means something month to month — and the verdict for each is the latest annotation in data/progress.txt, not README's status column. Use when asked what to practise today, how ready the core set is, how many of Blind 75 are solid, which problems keep coming back, or to refresh the set after a MUST marker or a list changed. Triggers - "/l3-core", "/l3-core next 5", "what should I drill today?", "how is the core set looking?", "which Blind 75 are still again?", "refresh the l3 core list".
allowed-tools: Read, Glob, Grep, Bash
---

# The L3 core set

Where each of the ~100 problems this repo measures itself on stands, and which five
to do next.

**Invocation**: `/l3-core` — the status table; `/l3-core next 5` — a session;
`/l3-core status --only again` — the ones still coming back; `/l3-core refresh` —
rebuild `data/l3_core.json` after the rule's inputs changed.

## Why this exists

The Aug 2026 readiness run scored Volume and Breadth **A** for the Google L3 bar and
said more solved problems cannot move the grade. The September 2026 project review
(PR #172) found the
loop that is not closing is *solved → solid*: 3,270 problems indexed, Blind 75 and
NeetCode 150 fully solved, and the practice log's latest verdict on Blind 75 was `ok`
for **14 of 75**, with 25 never given a verdict at all.

A percentage only means something on a set that does not move. `suggest_review.py`
and the review plan pick from the whole 3,270; this skill pins a set and reports
against it, so the same question asked next month is the same question.

## What the set is

```text
blind75 | (neetcode150 & readme:must)          -> 99 problems (Sep 2026)
```

- **Blind 75** because every interviewer has seen it.
- **NeetCode 150 ∩ MUST** because the `MUST` marker in README's status and Note
  columns is the owner's own must-know verdict — so the second half adds the NeetCode
  problems *this* preparation has already decided matter, and nothing else.
- README's `google` tag was tried and rejected: it marks 37% of the index, so
  NeetCode 150 ∩ google is 147 — NeetCode 150 with three problems missing.

The rule lives in `script/l3_core.py` (`RULE`, `core_ids`); the result lives in
[`data/l3_core.json`](https://github.com/yennanliu/CS_basics/blob/master/data/l3_core.json),
which is generated, committed, and
read by the roadmap page as the **L3 core** list. Titles and difficulty are never
stored there — they come from README at build time, like every other list.

## Where the verdict comes from

`data/progress.txt` is the primary record — it is written every day; README's status
column is updated only when needed and lags it. So the verdict this skill reports is
**the latest annotation the log gives a problem**:

| The log's last word on it | Reported as |
|---|---|
| `139(ok)`, `70(ok*, o(1) space!!)` | `ok` |
| `139(again)`, `907(again!!)`, `(ok, but again)` | `again` — `again` beats `ok` when a note says both |
| `139(todo)`, a bare `139`, `139(mono stack)` | `none` — attempted, **no verdict** |
| not in the log | `never` |

README's `OK`/`AGAIN` and its star run are shown beside it as the slower-moving
view, never instead of it. The log is parsed with the same helpers
`script/suggest_review.py` uses, so the two planners cannot disagree about a line.

## Prime directives

1. **The set does not move inside a session.** A `refresh` that changes membership is
   a commit with a reason (a MUST marker moved, a list was re-fetched), reported as
   such — never a quiet side effect of asking what to drill.
2. **The log is the record.** Report the log's verdict first; quote README's cell as
   corroboration. Never describe a problem as solid because README says `OK`.
3. **Never write the log or README.** A session picked here is logged with
   [`/lc-log`](https://github.com/yennanliu/CS_basics/blob/master/.claude/skills/lc-log/SKILL.md)
   and a README cell is moved with
   [`/lc-again`](https://github.com/yennanliu/CS_basics/blob/master/.claude/skills/lc-again/SKILL.md).
   This skill reads; it says what is owed.
4. **Every attempt gets a verdict.** 35 of the 98 have been attempted and never given
   one. The report ends with the exact `/lc-log` shape — `371(ok|again)` — for each
   problem picked, so the gap does not grow.
5. **Breadth in a session.** `next` round-robins across README sections in priority
   order; five problems should not be five DP rows.

## The steps

### 1. Check the set is current

```bash
python3 script/l3_core.py refresh --check
```

Exit 0 means `data/l3_core.json` still matches the rule. Exit 1 means a MUST marker or
a list membership changed since it was written; regenerate it and say so in the report
(directive 1) — the diff is the news:

```text
python3 script/l3_core.py refresh        # rewrites data/l3_core.json; commit it
```

### 2. Read the state

```bash
python3 script/l3_core.py status
```

One row per problem — the log's latest verdict, how many attempts, how long ago, and
README's cell — then the summary: size, `ok` share, `again`, no verdict, never logged,
the chronic list (`again` after 8+ attempts), and a per-section breakdown. Narrow it:

```bash
python3 script/l3_core.py status --only again
python3 script/l3_core.py status --only none
python3 script/l3_core.py status --section backtrack
python3 script/l3_core.py status --markdown
```

### 3. Pick the session

```bash
python3 script/l3_core.py next 5
```

Priority order, then round-robin across sections: never logged → attempted without a
verdict → `again` (oldest first) → an `ok` more than 30 days old. A fresh `ok` is not
offered — the set is for closing gaps, not re-proving what is solid — and nothing
logged in the last 3 days is offered either (`--exclude-recent DAYS` to change it).

### 4. Drill, then hand off

The user works the problems, ideally timed (a clean medium in 35 minutes, complexity
stated, edges named). Afterwards:

- every problem gets a verdict in the log via `/lc-log` — `371(ok), 217(again!!), …`;
- a problem the user re-derived unaided **and** wants README to reflect goes through
  `/lc-again`, which asks its own four questions before moving the cell.

This skill writes neither file (directive 3).

### 5. Track it monthly

```text
python3 script/l3_core.py status --json data/readiness-l3core-<YYYY-MM>.json
```

The JSON carries `size`, `ok`, `again`, `none`, `never`, `ok_share`, `chronic` and
`by_section`. The readiness guide already says to re-run its own script monthly; this
is the companion number, on the fixed set.

### 6. Report

Close with: the set's size and `ok` share, the `again` and no-verdict counts, the
problems picked with the reason each was picked, and the `/lc-log` line the session is
owed. If step 1 regenerated the file, say what moved and that it needs a commit.

## Do not

- ❌ change `RULE` or `core_ids` to make a number look better — the rule is a commit
  with a reason, and the review names the one it replaced (directive 1)
- ❌ report README's `OK`/`AGAIN` as the verdict; the log is the record and README is
  quoted beside it (directive 2)
- ❌ write `data/progress.txt` or a README status cell — `/lc-log` and `/lc-again` own
  them (directive 3)
- ❌ offer a fresh `ok` or a problem logged in the last three days as a drill
- ❌ pick five problems from one section when the priority order allows a spread
  (directive 5)
- ❌ store titles, difficulty or links in `data/l3_core.json` — ids only; README is the
  source at build time
- ❌ commit or push unless asked

## Worked example

`/l3-core next 5` on 2026-09-23:

| Step | What it produced |
|---|---|
| 1 | `refresh --check` → `data/l3_core.json is up to date (98 problems)` |
| 2 | `status` → 98 problems: latest log verdict `ok` 39 (40%), `again` 24, no verdict 35, never logged 0; chronic (`again`, 8+ attempts): 139, 91, 5, 128, 39, 206, 1143, 48, 124, 143, 211, 19, 271, 72, 131, 543, 621, 208; Backtracking 0 `ok` of 11, String 0 of 7 |
| 3 | `next 5` → LC 371 Sum of Two Integers (attempted 3×, no verdict), 217 Contains Duplicate (4×, no verdict), 242 Valid Anagram (3×, no verdict), 78 Subsets (4×, no verdict), 56 Merge Intervals (10×, no verdict) — five sections, all from the no-verdict bucket because nothing on the set is unlogged |
| 4 | the user drills them; the report hands over `371(ok\|again), 217(ok\|again), 242(ok\|again), 78(ok\|again), 56(ok\|again)` for `/lc-log` |
| 5 | not run — it is the monthly step |
| 6 | reported: 40% `ok` on 98; 35 attempts still owe a verdict, which is the cheapest number on the page to move |
