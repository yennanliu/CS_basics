# LeetCode Readiness Evaluation — Running It, and Reading It

`script/eval_lc_readiness.py` scores a LeetCode profile against a Google SWE coding
bar and prints a terminal report. This guide covers how to run it, what every
section of the output means, and — the part that actually matters — which numbers to
act on and which to ignore.

For the one-paragraph version, see [`doc/utility-scripts.md`](./utility-scripts.md).

---

## Quick Start

```bash
# Full report against the L3 bar (the default)
python3 script/eval_lc_readiness.py
```

No install, no dependencies, no LeetCode login, no Premium. It reads the public
GraphQL API and this repo's `README.md`.

### All the flags

| Flag | Default | Purpose |
|------|---------|---------|
| `--level {L3,L4,L5}` | `L3` | Which bar to score against. Moves the grade a lot — see [Levels](#levels). |
| `--user <name>` | `yennanliu` | Any public LeetCode username. |
| `--offline` | off | Replay the cache, make no network calls. Fails if any cache file for the requested user, year, or the shared tag universe is missing. |
| `--json <path>` | — | Write the evaluated report as JSON, for tracking over time. It is everything the terminal shows minus the raw tag map, not the untouched evaluator result. |
| `--cache-dir <dir>` | `.lc_cache/` | Where fetched JSON lands. Gitignored. |
| `--readme <path>` | `README.md` | The status-column source. |
| `--year <yyyy>` | current year | Which year's submission calendar to summarise. |

```bash
python3 script/eval_lc_readiness.py --level L4          # harder bar, same data
python3 script/eval_lc_readiness.py --offline           # no network
python3 script/eval_lc_readiness.py --json out.json     # evaluated report as JSON
python3 script/eval_lc_readiness.py --user someone_else # scout a friend's profile
```

### Caching

Every fetch is written to `--cache-dir`, one JSON file per query, namespaced so that
two profiles can never be mixed up:

```text
.lc_cache/
├── universe.json          # per-tag problem totals — shared, describes the problem set
└── <username>/
    ├── profile.json
    ├── tags.json
    ├── contest.json
    ├── recent.json
    └── calendar-2026.json # per year
```

Profile data is keyed by user, and the calendar additionally by year, so
`--user b --offline` cannot replay user a's cache and print it under b's name. As a
second guard, the run aborts if the profile payload's own username disagrees with
`--user` — which catches a hand-edited or hand-copied cache file.

A second run without `--offline` re-fetches the five per-user queries but **reuses
`universe.json`**, which costs one request per tag and essentially never changes. It is
shared across users because it describes the problem set, not a profile.

`--offline` requires `universe.json` too. Without it the topic targets are no longer
capped at each tag's own problem count, so the breadth score would shift by a point or
two with no visible cause — a silent difference is worse than a hard failure.

So the cheap loop while iterating on the report itself is:

```bash
python3 script/eval_lc_readiness.py            # once, online
python3 script/eval_lc_readiness.py --offline  # as many times as you like
```

---

## Where the numbers come from

Two sources, and it matters which is which, because they disagree in a useful way.

**1. LeetCode's public GraphQL API** (`https://leetcode.com/graphql`) — ground truth
for what has actually been accepted on the platform:

| Query | Gives |
|-------|-------|
| `matchedUser.submitStats` | solved counts and submission counts per difficulty |
| `matchedUser.tagProblemCounts` | distinct problems solved per topic tag |
| `userContestRanking` | rating, contests attended, percentile |
| `matchedUser.userCalendar` | streak, active days, per-day submission counts |
| `questionList(filters:{tags})` | how many problems exist per tag — the denominator |

**2. This repo's `README.md`** — ground truth for *your own assessment* of each
problem: the `OK` / `AGAIN` status, the trailing `*` run that counts review passes,
and the note column's `google` / `MUST` tags.

The gap between them is informative. A problem can have a README row, a Java file
and a Python file, and still have never been accepted on LeetCode — which is exactly
what the graph topics turned out to look like.

---

## Levels

The level changes what "enough" means, not what gets asked. Passing `--level` is the
single biggest lever on the output.

| | `L3` (default) | `L4` | `L5` |
|---|---|---|---|
| Solved | 300 | 500 | 600 |
| Medium | 200 | 300 | 350 |
| Hard | 60 | 150 | 200 |
| Hard share | 12% | 20% | 25% |
| Contest rating | 1650 | 1800 | 1900 |
| Contests attended | 8 | 10 | 12 |
| Topic target scale | 0.70× | 1.00× | 1.15× |

Why they differ:

- **L3** is entry level. There is no system design round, so the coding rounds carry
  almost the whole packet, and what they ask for is a *clean medium* — clarified,
  reasoned aloud, written correctly, complexity stated — not a clever hard. Hard
  problems are upside, not a requirement.
- **L4** asks the same problems with less hand-holding, and hard-flavoured mediums
  appear often enough that the hard tier has to be familiar.
- **L5** adds a system design round that this data cannot see at all, so an L5 score
  from this tool is necessarily incomplete.

> **These are modelled numbers, not official ones.** Google publishes no LeetCode
> threshold. They encode the widely-reported "medium fluency plus hard exposure"
> consensus. Treat the grade as calibration, and re-tune `LEVELS` in the script if
> you disagree — it is a dict at the top of the file for exactly that reason.

---

## Reading the report

### The header: four axes and one grade

```text
OVERALL vs Google L3 SWE coding bar: B+  (83%)

  Volume    ██████████████████████ 100%  A
  Breadth   █████████████████████░  94%  A
  Mastery   ███████████░░░░░░░░░░░  51%  C
  Signal    ████████████████░░░░░░  74%  B
```

| Axis | Answers | Weight |
|------|---------|--------|
| **Volume** | Have you solved enough, with the right difficulty mix? | 30% |
| **Breadth** | Is every topic the loop draws from covered? | 30% |
| **Mastery** | Do solved problems stay solved? | 20% |
| **Signal** | Any evidence of performance under time pressure? | 20% |

Grade scale: `A` ≥ 90%, `B+` ≥ 78%, `B` ≥ 66%, `C+` ≥ 54%, `C` ≥ 42%, `D` ≥ 30%,
`E` below.

**Read the axes, not the overall grade.** The single number averages four things that
call for completely different responses. Two profiles can both score B: one needs
more problems, the other needs to stop solving problems and start timing itself.

### Volume

```text
  OK  all      809 / 300  target
  OK  medium   487 / 200  target
  OK  hard     113 / 60   target
      easy     209
      hard share 14.0%  (target 12%)
      7,813 accepted / 12,660 submissions = 62% ; 9.7 AC per problem
```

`OK` / `GAP` is just target comparison. The line worth staring at is the last one:
**AC per problem**. A value near 1.0 means each problem was solved once and never
revisited. A high value means deliberate re-solving. Neither is wrong, but it tells
you which failure mode to look for — low means fragile recall, high means the review
loop may be spinning without retiring anything (see [Mastery](#mastery-the-cost-curve)).

Scoring: the mean of the three target ratios (each capped at 1.0), then
`0.75 × that + 0.25 × hard-share ratio`.

### Precision and drill depth

```text
           solved    subs  rejected  AC/problem
  easy        209    2859       44%         7.6
  medium      487    8764       37%        11.3
  hard        113    1037       31%         6.4
```

This table is not scored — it is diagnostic, and it is the fastest way to spot a
verification problem.

LeetCode publishes no first-attempt statistic, so `rejected` is
`(total submissions − accepted submissions) / total submissions` per difficulty: a
proxy for how often a first cut is wrong.

**How to read it:** rejection rate should *rise* with difficulty. If it is flat, or
inverted so that easy problems fail more often than hard ones, the cause is not
difficulty — it is speed without a self-check habit. Free retries hide that on
LeetCode; an interview has no submit button.

`AC/problem` shows which tier actually gets drilled. A tier that is both thin and
lightly drilled is the one that will surprise you.

### Breadth

```text
  You have solved 809/4033 = 20% of all LeetCode. A topic's 'x base' is its own
  coverage over that baseline: <1.0x means under-practised for you.
  topic                      solved    of   cov  x base  w
  GAP Shortest Path (Dijkstra)      7    42   17%   0.83x  3  █████░░░░░
      DFS                       125   349   36%   1.79x  3  ██████████
      Heap / PQ                 ~66     -     -       -  3  ██████████
  n/a BST                         -     -     -       -  2  (not reported by the API)
```

Six columns:

- **solved** — distinct problems accepted with that tag. A leading `~` means the
  number came from a README regex, not the API (see below).
- **of** — how many problems carry that tag in total, fetched from LeetCode.
- **cov** — solved ÷ of.
- **x base** — coverage divided by your overall coverage of all of LeetCode.
  **This is the column to trust.** `<1.00×` means under-practised *by your own
  standard*, which is a far stronger claim than missing a target someone made up.
- **w** — weight, 1–3, for how often the topic comes up in a Google loop. Drives the
  weighted breadth score and the `GAP` flag.
- **bar** — progress toward the level-scaled target.

Row prefixes: `GAP` = under 60% of target on a weight-2-or-3 topic, i.e. worth acting
on. `thin` = under 60% but low weight. `n/a` = not measurable.

Three quirks, all deliberate:

- **`~` rows.** LeetCode's skill-stats endpoint returns only a curated tag set and
  silently omits Heap/PQ and prefix sum. Those fall back to a regex over the README
  rows, which is a *lower bound* — it counts only what this repo tracks — so they are
  measured but not compared against a denominator.
- **`n/a` rows.** BST is omitted by the API and its README notes are full of
  "check with BST" cross-references, so any regex over-counts. Rather than report a
  bad number, it is excluded from the score entirely. An unmeasured topic is not a
  proven gap. Tree coverage is the honest proxy.
- **Targets are capped at the tag's own problem count.** Sweep line has only 8
  problems on all of LeetCode, so a target of 20 would be unreachable by definition.

### Mastery: the cost curve

This is the most useful part of the report and none of it is visible from LeetCode.

```text
  tracked rows 1191   OK 337   AGAIN 854   -> 28% marked solid

  Cost curve - mean review passes per problem, by README section.
  High mean = the topic keeps costing you re-learns. Ranked worst first:
    section                      n   OK  AGAIN  mean passes
    Recursion                   30    3     27       7.3  █████████░
    Math                        81   19     62       1.1  █░░░░░░░░░
```

`parse_readme()` also recognises `TODO` and `NOT_OK`. Neither appears in the README
today, but they are counted in their own bucket rather than folded into `AGAIN`, so the
per-section totals always reconcile with the header line. They stay in the mastery
denominator — a `TODO` row is tracked but not solid.

**Mean passes** is the average of the trailing `*` run across a section's rows: how
many times a topic has to be re-learned before it sticks. Ranking sections by it
separates two very different situations that a plain solved-count conflates —
*never seen* versus *never sticks*.

The comparison is what carries meaning, not the absolute value. If cheap topics
(array, string, math) sit at 1–3 passes and expensive ones (recursion, tree, BST,
BFS, backtracking, stack, heap) sit at 5–7, then volume is not your constraint —
retention on recursive structure is. And since the expensive group is most of what a
coding round is made of, that is where the remaining work is.

> **The `OK` / `AGAIN` ratio is a floor, not a measurement.** In this repo the
> `AGAIN` marker behaves as a permanent review tag rather than a mastery verdict —
> 125 of 136 hard rows carry it, some after 20+ passes. So the Mastery percentage is
> understated by a bookkeeping convention. Trust the *relative* cost curve; discount
> the absolute percentage. Fixing this needs a graduation rule, not a code change:
> decide what "solid" means (solved clean, first attempt, under 20 minutes,
> complexity stated) and promote rows that clear it.

### Chronic blind spots

```text
  Chronic blind spots - AGAIN after 12+ passes (106 problems):
    LC 91     23 passes  Medium Dynamic Programming     MUST
    LC 658    22 passes  Medium Binary Search           google MUST
```

Rows still queued after 12 or more passes. This is the most directly actionable list
the tool produces, and the point is a change of tactics rather than a change of
effort: **more repetition has already been proven not to work on these.**

What is missing is a written *invariant* — one sentence naming the state, the
transition, and the specific thing you get wrong — filed in the matching
[`doc/cheatsheet/`](./cheatsheet/) page. Check the difficulty column too. If the
chronic list is all mediums, it is describing the exact tier an L3 round is built
from, which makes it urgent rather than academic.

### Signal

```text
  contest rating 1528  (target 1650) from 1 contest(s), top 37.4%
  2026: 210 active days, current streak 92
  submissions by month:
    2026-03   433 subs / 31 days  ██████████████░░
```

Contests are the only speed-under-pressure evidence LeetCode exposes, which is why
they carry 55% of this axis (contests attended 25%, active days 20%).

**A rating from very few contests is not a low rating — it is no rating.** Ratings
start near 1500 and converge slowly, so one or two samples leave the number
essentially at its initial value. That is why `contests` is scored separately: the
report is asking whether the measurement exists at all, not just whether it is high.

The monthly histogram is for spotting a collapse or a taper, which the streak number
hides — a live 90-day streak is compatible with volume having quietly dropped by 80%.

---

## Acting on it

A rough priority order, because the axes are not equally expensive to fix:

1. **Signal at C or below with few contests** — fix first. It is cheap (two virtual
   contests a week) and nothing else substitutes for it.
2. **An inverted rejection curve** — easy problems failing more than hard ones.
   Fix with timed, no-run practice, not more problems.
3. **A steep cost curve on interview-core topics** — write invariants for the
   chronic list; stop re-solving.
4. **`GAP` topics with weight 3** — targeted, and usually small once the targets are
   level-scaled.
5. **Volume** — last, and often not at all. If Volume already scores A, more solved
   problems cannot move the overall grade.

And the thing the report cannot tell you: **it measures one of the three or four
things a loop scores.** Communication during coding, Googleyness, and (from L5)
system design are all invisible here.

---

## Tracking over time

```bash
python3 script/eval_lc_readiness.py --json "data/readiness-$(date +%Y-%m).json"
```

The four axis percentages in `scores` are the tracking metric. Re-run monthly rather
than weekly — solved counts and cost curves move on a scale of months, and a weekly
cadence mostly measures noise.

## Changing the bar

Everything tunable is at the top of the script:

- `LEVELS` — per-level volume targets, signal targets, and the topic target scale.
- `GOOGLE_TOPICS` — the topic list, as
  `(tag slug, display name, target, weight, README fallback regex)`.
- `grade()` — the letter cutoffs.
- The axis weights are in `evaluate()`, on the line that computes `overall`.

Adding a topic needs a real LeetCode tag slug. An unknown slug is not an error —
LeetCode ignores the filter and returns the entire problem set, which would read as
"1% coverage" on a topic you had actually covered. The script detects that case by
comparing against the unfiltered total and drops the denominator instead, but the
solved count will still be missing, so check the row shows `n/a` rather than a
plausible-looking number.
