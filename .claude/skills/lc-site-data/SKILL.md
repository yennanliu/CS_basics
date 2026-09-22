---
name: lc-site-data
description: Add a topic to the Study Roadmap (data/roadmap.json) or a question to the Complexity Quiz (data/complexity_quiz.json), satisfying the build-time validators in one pass — a row strictly below every prereq, no cycle and no edge the graph already implies, sheet slugs and LC numbers that exist, no duplicate quiz id, accept always an array, and every answer parseable by site/complexity.js's single-letter identifiers. Use when asked to add a roadmap topic, wire a topic's prerequisites, add a complexity question or drill, or fix a build that a roadmap or quiz entry failed. Triggers - "add monotonic stack to the roadmap", "/lc-site-data quiz for LC 239", "add a complexity question", "the roadmap build is failing".
allowed-tools: Read, Glob, Grep, Bash, Write, Edit
---

# Add a roadmap topic or a quiz question

Edit `data/roadmap.json` or `data/complexity_quiz.json` so the build accepts it the first
time.

**Invocation**: `/lc-site-data <what>` — e.g. `/lc-site-data roadmap monotonic-stack`,
`/lc-site-data quiz for LC 239`.

## Why this one is a skill

Neither file is long, but both are validated hard **at build time** and both span three or
four other files. `site/build-roadmap.js` and `site/build-quiz.js` fail the build rather
than warn — deliberately, so nothing lands in an unsorted bucket — which turns a five-line
edit into a build-break-and-retry loop unless every constraint is satisfied at once.

**Never repeat a title, a difficulty, or a link to this repo's solutions in either file.**
They are read from `PROBLEMS.md` at build time. A typed copy is a copy that goes stale.

## Part A — a roadmap topic

`lc-roadmap.html` is driven entirely by `data/roadmap.json` — one entry per topic in
its `nodes` array.

```json
{
  "id": "monotonic-stack",
  "title": "Monotonic Stack",
  "row": 2,
  "prereqs": ["stack"],
  "sheets": ["monotonic_stack", "monotonic_queue"],
  "blurb": "One sentence on what the topic buys you.",
  "problems": [496, 503, 85, 901, 907]
}
```

### What fails the build

| Rule | Why it exists |
|---|---|
| every `problems` id is in a `PROBLEMS.md` table | a number the index does not know has no title, difficulty or solution link to attach |
| every `sheets` slug is a file in `doc/cheatsheet/` | a dead sheet link on a teaching page is worse than no link |
| `row` is **strictly greater** than every prereq's `row` | edges must point downward |
| no cycle in `prereqs` | — |
| **no edge the graph already implies** | the roadmap has to stay a *transitive reduction*, or the drawing turns into spaghetti |

That last one is the one that is easy to get wrong: if `monotonic-stack` needs `stack`, and
`stack` already needs `array`, then listing `array` as a prereq of `monotonic-stack` is an
implied edge and fails. List only the *immediate* prerequisites.

**Placement**: within a row, topics are drawn in the order they appear in the file. Put a
new topic near the column its prerequisite sits in, to keep the edges short.

### The list picker

The page shows one problem set at a time, and all of them are declared in the same file:

- **`lists`** — the picker's entries. `from` says where membership comes from: `curated`
  (the ids on the nodes), `list:<flag>` (a flag in `data/problem_lists.json`), or
  `readme:<field>` (`google` / `must`, read out of the index's tag and status columns — the
  source is still spelled `readme:` in the data file).
- **`topicSources`** — each source files problems under its own taxonomy (NeetCode's
  `Arrays & Hashing`, LeetCode's plan group `Hashing`, the index's `## Array` heading). These
  maps put them on roadmap topics; **`null` means *deliberately* off the roadmap** (SQL,
  shell, JavaScript-only exercises). A list's `topicFrom` names which taxonomies to try, in
  order, so a coarse group falls through to a finer one.

The build fails on a taxonomy key that is **missing, pointing at an unknown topic, or mapped
but unused** — so a renamed upstream category cannot silently drop a whole group of
problems. Watch the per-list "shown of" tally the build prints.

Only the curated list has a teaching order, so only it renders locks and prerequisites.

`data/problem_lists.json` is **vendored, not built** — refresh it by hand, never from the
site build:

```bash
python3 script/fetch_problem_lists.py           # rewrite the file
python3 script/fetch_problem_lists.py --check   # exit 1 if it is stale
```

## Part B — a quiz question

`lc-complexity-quiz.html` draws from `data/complexity_quiz.json`.

```json
{
  "id": "two-sum-hash",
  "lc": 1,
  "topic": "Arrays & Hashing",
  "vars": "n = len(nums)",
  "code": ["def twoSum(nums, target):", "    ..."],
  "time": "O(n)",
  "space": "O(n)",
  "why": "One sentence on where each bound comes from.",
  "trap": "The wrong answer people actually give, and why it is wrong."
}
```

### What fails the build

| Rule | Note |
|---|---|
| no duplicate `id` | — |
| an `lc` number is in a `PROBLEMS.md` table | — |
| an entry **with** an `lc` sets **no** `title`/`difficulty` | they come from the index |
| an entry **without** an `lc` sets **both** | a pure algorithm or Python drill |
| `accept` is an **array**, never a bare string | a bare string survives validation and then breaks the page's feedback |
| every answer parses as a complexity expression | `time`, `space`, and each `accept` |

**`site/complexity.js`'s identifiers are single letters.** Write `O(n * a)` with a `vars`
line saying what `a` is — never `O(n * amount)`. This is the single most common failure.

`accept` is for answers that are **genuinely defensible** — `O(h)` versus `O(n)` for a
tree's recursion stack — not for spelling variants. `O(n log n)`, `nlogn` and `N·logN`
already grade the same.

`why` says where each bound comes from. `trap` is **the wrong answer people actually give**,
and why it is wrong — not a restatement of the right one.

## The steps

### 1. Check what already exists

```bash
python3 -c "import json;d=json.load(open('data/roadmap.json'));print([n['id'] for n in d['nodes']])"
grep -n '"id":' data/complexity_quiz.json | wc -l
grep -n "^| *0*<lc> " PROBLEMS.md        # the number must be there
ls doc/cheatsheet/<slug>.md               # the sheet must be there
```

A quiz `id` that repeats and a roadmap topic that duplicates an existing one are both build
failures, so check before writing.

### 2. Write the entry

Roadmap: place it in the file next to the column its prereq sits in, list only immediate
prereqs, and give `row` a value strictly greater than all of them.

Quiz: keep `code` to the few lines the question turns on. The reader is answering about the
complexity, not reading a solution.

### 3. Build — the validator is the test

```bash
SKIP_FONTS=1 bash site/build.sh
```

Read the output rather than just the exit code:

- the roadmap line prints topics, rows and distinct problems;
- the **per-list "shown of" tally** is where a dropped taxonomy group shows up
  (`NeetCode (all) 943 shown of 972, 29 unplaceable`) — a number that moved when you did not
  intend it to is a mapping you broke;
- the quiz line prints the question count and the per-topic spread.

### 4. Check the page, then gate

```bash
node site/e2e-check.js _site
npm test --prefix site
python3 -m http.server -d _site 8000   # /lc-roadmap.html or /lc-complexity-quiz.html
```

For a roadmap topic, look at the drawing: an edge that crosses the whole diagram means the
topic is in the wrong place within its row (step 2), even though it validated.

For a quiz question, answer it — including with the `accept` alternatives — and read the
feedback. A question whose `trap` does not match what the grader actually rejects is a
question that teaches the wrong thing.

### 5. Report

Close with the entry, the build tallies **before and after** (especially the "shown of"
lines), and anything inferred: the row chosen, a prereq deliberately left off as implied, an
`accept` alternative added and why it is defensible rather than a spelling.

## Do not

- ❌ repeat a title, difficulty or solution link in either file — they come from the index
- ❌ list a prereq the graph already implies (transitive reduction)
- ❌ give a topic a `row` equal to or below a prereq's
- ❌ write `O(n * amount)` — identifiers are single letters, with a `vars` line
- ❌ write `accept` as a bare string
- ❌ set `title`/`difficulty` on an entry that has an `lc`, or omit them on one that does not
- ❌ use `accept` for spelling variants that already grade the same
- ❌ edit `data/problem_lists.json` by hand — it is vendored, via `fetch_problem_lists.py`
- ❌ ignore a "shown of" tally that moved
- ❌ commit or push unless asked

## Worked example

`/lc-site-data roadmap monotonic-stack`:

| Step | What it produced |
|---|---|
| 1 | `monotonic-stack` not present; `stack` is row 1; `doc/cheatsheet/monotonic_stack.md` and `monotonic_queue.md` both exist; 496/503/85/901/907 all in `PROBLEMS.md` |
| 2 | entry written at row 2, `prereqs: ["stack"]` only — `array` left off because `stack` already requires it, which would have failed the transitive-reduction check |
| 3 | build printed `30 topics over 7 rows`; every "shown of" tally unchanged |
| 4 | the drawing put it one column from `stack`, so the edge is short |
| 5 | flagged: row 2 chosen over row 3 because nothing on row 2 depends on it |
