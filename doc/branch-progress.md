# Branch progress log

Running log of the automated clean-up of `leetcode_python/`, one section per branch.
Generated from real repo state by `script/py_multi_version/gen_progress.py` — do not hand-edit.

_Snapshot: 2026-08-22 08:54_

## Where this work came from

Audit of `leetcode_python/`: **2,868 files**, 7,847 `# V<n>` markers, **6,967** of them
holding a real implementation. Three tasks fell out of it:

| # | Task | Size found by audit | Branch |
|---|------|---------------------|--------|
| 1 | Add missing `# time =` / `# space =` | 373 blocks in 142 files | not started |
| 2 | Fix code format | 90 files with py2/tab defects; 366 duplicate blocks in 264 files; 375 files with no `V0`; ~9,300 whitespace fixes | not started |
| 3 | Every LC needs >=3 solution versions | **2,176** files short (1,913 have 1, 258 have 2, 5 have none) | `feat/py-multi-version` |

Task 2's dedup pass must run **after** task 3, or the two passes fight over the
same version markers.

A note on the count: a first pass put task 3 at 2,168 files. That counter treated a
`pass`-only block as an implementation. `audit.py` does not, which is the stricter and
correct reading, and it moves the number to 2,176. Re-run `audit.py --report` for the
live figure — it always measures the tree rather than trusting this table.

## `feat/py-multi-version`

**Task**: Task 3 — every LC python file gets >=3 distinct, tested solution versions

- Base: `master`  |  Worktree: `/Users/jliu/CS_basics-wt-py3ver`
- Tooling: `script/py_multi_version/` (`SPEC.md` agent contract, `audit.py`, `verify.py`, `gen_progress.py`, `batches/`)
- HEAD: `7608bf0a2 feat(leetcode_python): add missing solution versions, wave 1 (60 files)`
- Commits ahead of `master`: 1

### Batches dispatched

| Batch | Files | Verified OK | Needs rework | Committed | Status |
|-------|-------|-------------|--------------|-----------|--------|
| 001 | 6 | 6 | 0 | 6 | committed |
| 002 | 6 | 6 | 0 | 6 | committed |
| 003 | 6 | 6 | 0 | 6 | committed |
| 004 | 6 | 6 | 0 | 6 | committed |
| 005 | 6 | 6 | 0 | 6 | committed |
| 006 | 6 | 6 | 0 | 6 | committed |
| 007 | 6 | 6 | 0 | 6 | committed |
| 008 | 6 | 6 | 0 | 6 | committed |
| 009 | 6 | 6 | 0 | 6 | committed |
| 010 | 6 | 6 | 0 | 6 | committed |

Batches still running are marked *in flight*; their file counts are partial.

The batch work-list was cut from the original 2,168-file audit, so it holds 345
batches. It gets recut from `audit.py` once the in-flight wave lands, which both
picks up the 8 files the stricter count added and drops the files already finished.

### Overall

- In scope: **2176** files across **345** batches
- Dispatched: **60**  |  Verified complete: **60** (2.8% of scope)  |  Remaining: **2116**
- Files changed in commits so far: **60**
- Changed but uncommitted: **0**

### Definition of done

Enforced mechanically by `script/py_multi_version/verify.py`; a file passes only if:

- at least **3 blocks with real code** — a `pass`/bare-`return` placeholder block
  does not count toward the 3
- every real block carries both `# time =` and `# space =` in its header
- no two blocks are identical after comment/whitespace normalisation (this is what
  stops the pass from re-creating the 366 duplicates task 2 has to clean up)
- the file parses under `ast.parse`
- **added** lines carry no tabs, no trailing whitespace, no py2 constructs, and the
  file ends in exactly one newline (pre-existing debt is deliberately out of scope
  here — it belongs to task 2)

Each new block also gets the house 4-line header, and every solution is run against
the problem's docstring examples before it counts. Existing blocks are left
byte-for-byte unchanged — the pass is purely additive.

