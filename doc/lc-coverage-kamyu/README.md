# LC Coverage Gap vs. kamyu104/LeetCode-Solutions

Problems solved in [kamyu104/LeetCode-Solutions](https://github.com/kamyu104/LeetCode-Solutions)
that have **no solution file in this repository**. Generated 2026-08-13.

No source code was changed by this audit — these files are a to-add list only.

## Summary

Original audit (2026-08-13): 4008 problems across the four lists, 1274 already
covered, 2734 to add.

Current state of the work lists:

| Source list | To add | Done | Skipped | **Left** | |
|---|---|---|---|---|---|
| 0001 – 1000 | 128 | 128 | 19 | **0** | 100.0% |
| 1001 – 2000 | 672 | 672 | 10 | **0** | 100.0% |
| 2001 – 3000 | 783 | 653 | 139 | **130** | 83.4% |
| 3001 – Latest | 910 | 0 | 73 | **910** | 0.0% |
| **Total** | **2493** | **1453** | **241** | **1040** | **58.3%** |

`Skipped` are problems in kamyu's lists that have no meaningful Python solution:
SQL problems, and LeetCode's JavaScript track (`Sleep`, `Debounce`, `Promise Pool`,
`Curry`, …). They are removed from the work list, not just recorded beside it.

The remaining gap is dominated by recent contest problems: everything below LC 2000
is now covered, and 3001–Latest has not been started.

## Data integrity

The work lists were repaired after the source audit was found to mis-key some
records. Each `work_<range>.json` record is now checked so a to-add entry cannot
produce a duplicate solution file:

- **Coverage is detected by LC number, not filename.** LeetCode renames problems
  (LC 1529 `Bulb Switcher IV` → `minimum-suffix-flips`, LC 2086 → `minimum-number-
  of-food-buckets-to-feed-the-hamsters`). Matching on slug alone reported these as
  missing when the repo already had them under the old name.
- **Slug and difficulty come from the canonical `leetcode.com/problems/...` URL**
  and front matter of each problem statement, not from the parsed index.
- **No two records share a `target_file`.** The LC 2499 / LC 2522 pair collided on
  one path in the original data and produced a real conflict during a batch run.
- **Titles** are corrected only on substantive mismatch; punctuation-only
  differences from the statement source are not applied.

Verified invariants (all four files): no duplicate LC numbers, no duplicate
`target_file`, no record in both the work and skipped list, no title/slug
disagreement, and no to-add entry already covered anywhere in the repo.

## Files

Each batch is checkpointed as both markdown (readable table) and JSON (machine-readable):

- `missing_<range>.md` — `#`, title, difficulty, slug
- `missing_<range>.json` — same records for scripting
- `_summary.json` — per-batch totals

## Method

Source of truth for the problem lists is kamyu104's four index files
(`0001-1000.md`, `1001-2000.md`, `2001-3000.md`, `README.md`), parsed for
`number | [Title](leetcode.com/problems/<slug>) | ... | Difficulty`.
Rows repeat across tag sections, so each number is counted once.

A problem counts as **already here** if either matches:

1. **Slug** — a solution filename in `leetcode_python/`, `leetcode_java/`,
   `leetcode_SQL/`, `leetcode_scala/`, `algorithm/`, or `data_structure/`.
   Java `CamelCase` names are converted to kebab-case, and both sides are
   normalized to alphanumerics so `LRUCache_.java` matches `lru-cache`.
2. **LC number** — an `LC <number>` marker or a `leetcode.com/problems/...`
   URL inside a solution file.

The generator script is `script/compare_kamyu_coverage.py`.
