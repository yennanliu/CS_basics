# LC Coverage Gap vs. kamyu104/LeetCode-Solutions

Problems solved in [kamyu104/LeetCode-Solutions](https://github.com/kamyu104/LeetCode-Solutions)
that have **no solution file in this repository**. Generated 2026-08-13.

No source code was changed by this audit — these files are a to-add list only.

## Summary

| Source list | In list | Already here | **To add** |
|---|---|---|---|
| [0001 – 1000](./missing_0001-1000.md) | 999 | 852 | **147** |
| [1001 – 2000](./missing_1001-2000.md) | 997 | 315 | **682** |
| [2001 – 3000](./missing_2001-3000.md) | 1000 | 78 | **922** |
| [3001 – Latest](./missing_3001-latest.md) | 1012 | 29 | **983** |
| **Total** | **4008** | **1274** | **2734** |

### To-add breakdown by difficulty

| Source list | Easy | Medium | Hard | 🔒 Premium |
|---|---|---|---|---|
| 0001 – 1000 | 23 | 21 | 103 | 34 |
| 1001 – 2000 | 178 | 332 | 170 | 94 |
| 2001 – 3000 | 236 | 466 | 220 | 208 |
| 3001 – Latest | 189 | 499 | 293 | 177 |

Coverage is strongest on the classic range (85% of 0001–1000) and thins out sharply
after LC 2000 — the gap is dominated by recent contest problems.

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
