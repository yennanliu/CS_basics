---
name: lc-again
description: Move a problem's status cell in PROBLEMS.md after a re-solve — promote AGAIN to OK when it was genuinely re-derived unaided, add a review pass when it was not, or demote OK back to AGAIN when a solid problem comes back. Keeps the trailing `*` run, which is the recorded pass count that the cost curve and the review planner both read, and refreshes data/again_problems.txt. Use when asked to mark a problem solid, to graduate an AGAIN marker, to record another failed pass, or to update the status column after drilling something. Triggers - "I finally got 128, mark it OK", "/lc-again 139", "promote 1143 to OK", "560 came back again", "refresh the again list".
allowed-tools: Read, Glob, Grep, Bash, Write, Edit
---

# Move a problem's status

Edit the **status column** of a problem's row in `PROBLEMS.md` (the problem index — it
was `README.md` until Sep 2026) after a re-solve, and refresh
`data/again_problems.txt`.

**Invocation**: `/lc-again <LC number> [verdict]` — e.g. `/lc-again 128 ok`,
`/lc-again 560`. With no verdict, ask how the re-solve actually went before deciding
(directive 1).

## Why this exists

The status column is currently **325 `AGAIN` against 124 `OK`**, and
`doc/lc-readiness-guide.md` says why that number cannot be read as a mastery figure:

> the `AGAIN` marker behaves as a permanent review tag rather than a mastery verdict —
> 125 of 136 hard rows carry it, some after 20+ passes. So the Mastery percentage is a
> floor, not a measurement.

106 problems are `AGAIN` after 12 or more passes. Nothing in the repo promotes a row, so the
marker only accretes — and `script/suggest_review.py` reads a high pass count on an `AGAIN`
row as *difficulty*, not progress, which means a problem you have actually mastered keeps
scoring as a gap and keeps being handed back to you.

This skill is the missing half of that loop. It is deliberately hard to get a promotion out
of, because a promotion that is given away turns the column into noise in the other
direction.

## What the cell actually means

The column is free text — **403 distinct spellings** across the index — but three pieces
are read by tooling and must survive any edit:

| Piece | Read by | Meaning |
|---|---|---|
| the word `OK` / `AGAIN` | `suggest_review.py`'s `STATUS_WORD`, `get_again_problems.sh` | the verdict |
| the trailing `*` run | `suggest_review.py`'s `passes` (`status.count("*")`), and the readiness guide's cost curve | **how many review passes the problem has cost** |
| `MUST` in any casing | `extract_must_lc.py`, `suggest_review.py` | the must-know marker |

`(n)`, `(not start)`, and the occasional typo (`(not srart)`) are the user's own notes. They
are preserved verbatim.

```text
AGAIN*** (3)                      three recorded passes, still not solid
OK* (2)                           solid, and it cost two passes to get there
AGAIN (not start)                 never attempted; not a promotion candidate at all
AGAIN*************** (7) (MUST)   LC 128, as it really reads: 15 stars, 7 in the paren,
                                  and a MUST that has to survive the edit
```

The star run and the `(n)` frequently disagree — LC 128 above is 15 against 7. Neither is
recomputed from the other; both are kept as written.

## Prime directives

1. **A promotion is earned, not asked for.** The bar is all four of: the user re-derived it
   **unaided**, they can state the invariant the solution turns on, they can name the line
   that sets the complexity, and they handled the edge cases. If the user just says "mark it
   OK", ask which of those held before doing it. Taking their word once they answer is
   correct — interrogating them twice is not.
2. **Never erase the pass count.** The `*` run is the cost record, and promoting is not
   forgetting what it cost. `AGAIN*** (3)` becomes `OK*** (3)`, never `OK`. The cost curve in
   the readiness guide is computed from exactly those stars.
3. **Match the row's own idiom.** 403 spellings exist; this is not the commit that
   standardises them. Change the verdict word, add a star when a pass was spent, and leave
   the rest of the cell exactly as written — spacing, parens, notes and all.
4. **One row, one cell.** Not the tags, not the complexity, not the solution links, and not
   any other row.
5. **The log is a separate record.** A re-solve is also a practice attempt, and that belongs
   in `data/progress.txt` via
   [`/lc-log`](https://github.com/yennanliu/CS_basics/blob/master/.claude/skills/lc-log/SKILL.md).
   This skill does not write it, and `/lc-log` does not write the status cell. Say which one
   the user still needs.

## The three moves

| The re-solve went | Move | Example |
|---|---|---|
| clean, unaided, all four criteria | **promote** — swap `AGAIN` → `OK`, keep the stars and notes | `AGAIN*** (3)` → `OK*** (3)` |
| it came back / needed a hint / the idea was there but the code was not | **another pass** — keep `AGAIN`, add one `*` | `AGAIN**` → `AGAIN***` |
| a previously-`OK` problem failed | **demote** — `OK` → `AGAIN`, keep the stars, add one | `OK* (2)` → `AGAIN** (2)` |

"Another pass" is the **default** and the most common outcome. A problem that needed one
nudge did not graduate.

A row marked `(not start)` has never been attempted; a first attempt makes it an ordinary
`AGAIN`/`OK` row, not a promotion.

## The steps

### 1. Find the row and read the cell

```bash
grep -n "^| *0*<number> |" PROBLEMS.md
```

A problem can legitimately have **two rows** — LC 547 is filed under both DFS and Graph.
Check, and edit both, or say which one you edited and why:

```bash
grep -cn "^| *0*<number> |" PROBLEMS.md
```

Read out of the cell, before changing anything: the verdict word, the star count, and
whatever notes it carries.

### 2. Establish what actually happened

Ask, unless the user already said it:

- did you re-derive it yourself, or did you look at the old solution?
- what is the invariant it turns on? (not the steps — the reason the steps are correct)
- which line sets the complexity?
- what breaks it — empty, single element, duplicates, overflow?

The purpose is not a quiz. It is that a problem you cannot answer these about is a problem
you will re-solve from memory today and re-fail in three weeks, which is exactly the shape
the 106 chronic rows have.

If the user declines to answer, the move is **another pass**, not a promotion. Say so
plainly and move on.

### 3. Make the edit

Change only the verdict word and, where a pass was spent, the star run:

```text
| 0128 | [Longest Consecutive Sequence](…) | … | Medium | … | AGAIN*************** (7) (MUST) |
                                                              ^^^^^                ^^^^^^^^^
                                                              only this word       kept exactly
                                                              changes

                                                          -> OK*************** (7) (MUST)
```

Preserve the cell's spacing exactly — several rows have a leading or trailing space inside
the pipes, and the tables are read by column position in places.

### 4. Refresh the derived list

```bash
bash script/get_again_problems.sh
```

`data/again_problems.txt` is generated from the index and was last regenerated in **Nov 2025**,
so the first run after this will produce a large diff. That is the point — say so in the
report rather than hiding it, and keep it as a separate concern from the one-row edit.

### 5. Check the tooling still reads the row

```bash
python3 script/suggest_review.py --only again | head -20
python3 script/eval_lc_readiness.py 2>/dev/null | head -30   # optional: the OK/AGAIN ratio
```

The promoted problem should **leave** the `again` pool, and its pass count should still be
visible in the readiness cost curve. If the row vanished from both, the star run was eaten
(directive 2).

### 6. Report

Close with: the row(s) edited and the line number(s), the before and after cell verbatim,
which of the four criteria the user confirmed, the `again_problems.txt` diff size, and the
reminder that the attempt itself still needs `/lc-log`.

## Do not

- ❌ promote on request alone, without establishing that it was re-derived unaided
  (directive 1)
- ❌ drop the `*` run when promoting — it is the cost record, not the verdict (directive 2)
- ❌ normalise a cell's spelling, spacing or notes while you are in there (directive 3)
- ❌ touch the tags, complexity or solution cells, or any other row (directive 4)
- ❌ write `data/progress.txt` — that is `/lc-log` (directive 5)
- ❌ promote a `(not start)` row — it has never been attempted
- ❌ bulk-promote. One problem, one re-solve, one decision
- ❌ commit or push unless asked

## Worked example

`/lc-again 128` after the user said they finally got Longest Consecutive Sequence:

| Step | What it produced |
|---|---|
| 1 | one row, PROBLEMS.md:836 — `AGAIN*************** (7) (MUST)`: 15 stars, 7 passes recorded in the paren, and a MUST marker |
| 2 | asked the four; the user re-derived it unaided, named the invariant (*only start a run at a number whose `n-1` is absent*), named the `while (s.contains(n+1))` walk as the one amortising the scan to O(n), and covered the empty and all-duplicates cases |
| 3 | cell changed to `OK*************** (7) (MUST)` — every star, the `(7)` and the `(MUST)` kept verbatim; only the word moved |
| 4 | `get_again_problems.sh` rerun; `again_problems.txt` went from 879 lines to 876, having been stale since Nov 2025 |
| 5 | `suggest_review.py --only again` no longer offers 128; the readiness cost curve still counts its passes, and `--only must` still finds it |
| 6 | flagged: the attempt itself is not logged — `/lc-log 128 ok` is still owed |
