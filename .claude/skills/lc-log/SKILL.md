---
name: lc-log
description: Append today's practice session to data/progress.txt — the daily log that is the only source for the review plan. Writes the day's line in the log's own shape (the YYYYMMDD date, the bucket labels, the `|` session separators), carries forward the buckets still open from yesterday, and gets the annotations right, because `again` / `again!!!` / `ok` / `ok*` / `todo` are what the whole spaced-repetition schedule keys on. Use when asked to log practice, record what was solved today, update the progress file, or mark a problem as done or still shaky. Triggers - "log today's practice", "/lc-log 23 ok, 24 again!!", "record that I did 141 and 142 clean", "update progress".
allowed-tools: Read, Glob, Grep, Bash, Write, Edit
---

# Log a practice session

Append one day to `data/progress.txt` — the hand-written practice log that
`site/build-review-plan.js` compiles into the review plan's data on every build. **It is the only copy.** Nothing else in the repo records that a problem was
practised, so a line this skill writes badly is a day that quietly leaves the schedule.

**Invocation**: `/lc-log <what you practised>` — e.g. `/lc-log 23 ok, 24 again!!, 25`.
With no argument, ask what was practised today; do not invent a session.

## Prime directives

1. **Never invent an attempt.** Only what the user actually says they practised goes in.
   Not what they were "supposed to" do, not the carried-over `todo` bucket's contents as
   though they were attempted, not a problem filed by `/lc-python` or `/lc-java` in the same
   session. Filing a solution is not practising it.
2. **A number must start its own comma-chunk, or it is silently lost.** This is the format's
   sharpest edge and it has already cost this log 93 attempts — see
   [The two shapes that lose data](#the-two-shapes-that-lose-data).
3. **The annotation is the whole signal.** `again` beats `ok` when a note says both, and the
   bang count sorts. Copy the user's own wording; do not normalise `again!!` down to `again`.
4. **Never downgrade or rewrite a previous day.** The log is an append-only record of what
   happened. A problem that went badly on Tuesday still went badly on Tuesday.
5. **Run the gate before reporting done.** A line the parser misreads produces no error —
   it produces a smaller schedule. Step 5 is not optional.

## The format, as the parser actually reads it

One physical line per day, **newest first at the top of the file**, separated by two blank
lines:

```text
20260921: top 100 like(dp):, 32(ok), 300 | LCA:, 236(again!!) | others:, 354(2D LIS) |
```

| Piece | Rule |
|---|---|
| `YYYYMMDD:` | must be a real calendar date — an impossible one is warned about and **the whole day is dropped** |
| `\|` | groups the day into sessions. Cosmetic to the parser (it becomes a comma), structural to the reader |
| `,` | the real separator. A `.` also splits, but that is typo-tolerance, not a convention — write commas |
| `123(note)` | the annotation. Commas and nested parens inside it are safe: `70(ok*, o(1) space!!)` is one entry |
| `topo_sort`, `weekly_331` | named drills. Parsed and ignored — they have no LC number to schedule, which is fine and intended |
| a line starting `---` | a separator that ends the current day. Do not write one mid-session |

The annotation vocabulary, in the order the classifier tries it — **`again`, then `todo`,
then `ok`**:

| Written | Classified | Why it matters |
|---|---|---|
| `141(ok)` | ok | graduated this pass |
| `994(ok*)` | ok | the `*` is the user's own shading, carried through as the note |
| `128(again!!!)` | again, emphasis 3 | bangs are counted anywhere in the note and sort it above a bare `again` |
| `44(again !!)` | again, emphasis 2 | spaces inside the note are fine |
| `139(ok, but again)` | **again** | `again` is checked first, on purpose |
| `79(todo)` | todo | intended, not attempted — still records the intent |
| `300` | none | practised, no verdict given. Legitimate; do not invent one |

## The two shapes that lose data

Both are already in the log, both parse without a single warning, and both are the reason
this skill exists rather than a habit.

### A number sharing a chunk with its bucket label

Each comma-chunk must **start** with the digits. A chunk that starts with a label looks
exactly like a named drill, so it is dropped whole — taking the number with it:

```text
20260920: top 100 like(linked list):  23, 24(again!!!), 25 | ...
                                      ^^ LC 23 is dropped. 24 and 25 survive.
```

**93 attempts across the log sit in a chunk shaped like that.** The two readers of the log
used to disagree about them — `script/suggest_review.py` strips the label first
(`_strip_label`), and `site/build-review-plan.js` did not until Sep 2026, when it gained the
same `stripLabel` because the roadmap stamps its done state from what that parser reads. Both
read the chunk now; a reader that does not exist yet will not, so the shape is still avoided.

The fix is one character — end the label with a comma so the number starts its own chunk,
which no reader has to be taught:

```text
20260920: top 100 like(linked list):, 23, 24(again!!!), 25 | ...
```

That is the shape this skill writes. It reads identically to a human and parses losslessly.

### A number written after its description

`^(\d+)` matches the *first* digits in the chunk, so a description that itself starts with a
digit hands the parser the wrong number. **Neither** reader recovers this one:

```text
2D LIS (354)      ->  logged as LC 2, note "D LIS" discarded.   16 entries in the log do this.
354(2D LIS)       ->  logged as LC 354, note "2D LIS".          correct.
```

Rule: **the LC number first, the description inside the parens.**

Fixing the 16 number-after-description entries is a separate job from writing today's line
— this skill does not rewrite past days (directive 4).

## The steps

### 1. Read the last few days

```bash
head -20 data/progress.txt
```

Two things to take from them:

- **the date.** New entries go at the **top** of the file. Use today's real date — check it
  rather than assuming, and never post-date or back-date without being asked.
- **the open buckets.** A day's line carries forward the buckets still in play
  (`top 100 like(dp):`, `LCA:`, `DP:`, `LC weekly:`, `others:`). They are the user's own
  running agenda, not decoration. Carry forward the ones still open, keep their wording, and
  drop a bucket only when the user says it is finished.

### 2. Sort what the user said into buckets

Put each problem in the bucket it belongs to, matching the labels already in use. A problem
with no obvious bucket goes in `others:`. A new bucket is fine when the user names a new
theme — keep the label short and reuse it the next day.

Keep the user's own verdicts verbatim. If they said "24 fought me again", that is
`24(again)`; if they said "again, three times now", that is `24(again!!!)` only if they gave
three bangs or said so — do not inflate the emphasis.

### 3. Write the line

```text
YYYYMMDD: <label>:, <n>(<note>), <n>, ... | <label>:, <n>(<note>) | others:, <n> |
```

- every label ends `:,` (directive 2)
- every problem is `<number>` or `<number>(<note>)`, number first (directive 2)
- sessions separated by ` | `, and the line ends with a trailing ` |` like its neighbours
- one physical line, however long — the parser handles wrapping, but the log writes one line
- two blank lines before the entry below it

Insert at the **top** of the file, above the current newest day.

### 4. Do not touch anything else

This skill writes exactly one new line in one file. Not README's status column — that is
[`/lc-again`](https://github.com/yennanliu/CS_basics/blob/master/.claude/skills/lc-again/SKILL.md)'s
job and it has its own rules about what earns a promotion. Not a solution file. Not
`data/again_problems.txt`.

### 5. Run the gate

The parser never errors on a badly shaped line — it just reads fewer problems. So check the
numbers, not the exit code:

```bash
node site/build-review-plan.js
npm test --prefix site
```

`build-review-plan.js` prints the day count, the problem count and the `again` count, and
**`site/test/build-review-plan.test.js` asserts the real log parses with zero warnings.**
Then confirm today's line round-trips — every number the user gave, and nothing extra:

```bash
node -e "
const p = require('./site/build-review-plan.js');
const { payload } = p.buildPayload(require('fs').readFileSync('data/progress.txt','utf8'));
const d = payload.days[payload.days.length - 1];
console.log(d.date, d.items.map(i => i.id + (i.note ? '(' + i.note + ')' : '')).join(' '));"
```

If a number the user gave is missing from that output, the line is wrong — fix the line,
not the expectation.

### 6. Report

Close with the date written, the problems in it grouped by bucket, the round-trip output,
and anything inferred: a bucket chosen for a problem the user did not place, a carried-over
bucket dropped because it looked finished, a verdict left blank because none was given.

## Do not

- ❌ invent an attempt, or promote a `todo` into an attempt (directive 1)
- ❌ write a label and a number in one chunk — `others: 678(todo)` loses LC 678
- ❌ write `2D LIS (354)` — it logs LC 2
- ❌ edit or "tidy" a previous day's line
- ❌ inflate or flatten the user's emphasis — `again!!` is not `again`
- ❌ touch README's status column, `data/again_problems.txt`, or any solution file
- ❌ report done without the round-trip in step 5
- ❌ commit or push unless asked — the practice log gets its own commit

## Worked example

`/lc-log 23 ok, 24 again!!!, 25, and 138 came back again` on 2026-09-21:

| Step | What it produced |
|---|---|
| 1 | read the top of the log: yesterday's open buckets were `top 100 like(linked list)`, `top 100 like(heap)`, `top 100 like(dp)`, `others`, `DP` |
| 2 | 23/24/25/138 all sort into `top 100 like(linked list)`; the rest of yesterday's buckets carry forward untouched |
| 3 | `20260921: top 100 like(linked list):, 23(ok), 24(again!!!), 25, 138(again) \| top 100 like(heap):, 295 \| ...` written at the top |
| 4 | README, `again_problems.txt` and the solution files left alone |
| 5 | `build-review-plan.js` → 822 days, 799 problems; round-trip prints `20260921 23(ok) 24(again!!!) 25 138(again)` — all four present |
| 6 | flagged: 25 recorded with no verdict because none was given; `LC weekly:` dropped, as the user said 2289 was finally done |
