# 繁體中文 Cheatsheet Translation Audit — Sep 2026

A one-off scan of every sheet in [`doc/cheatsheet/`](./cheatsheet/) for a document
that is **not** fully translated. The live figure is always
`node script/zh.js status cheatsheet` (see
[cheatsheet-zh-progress.md](./cheatsheet-zh-progress.md)); this file records what
was checked on 2026-09-30 and what was found.

## Result

**No cheatsheet is missing a translation.**

| Check | Result |
|---|---|
| `node script/zh.js status cheatsheet` | 5340/5340 sections, 134/134 documents (100%) |
| `node script/zh.js check cheatsheet` | pass — every section has an entry, composes, and matches its English shape |
| English sheets with no overlay file | only `00_template.md`, which is not built by design |
| `status --write` against the committed progress doc | no drift |

## Entries with no Chinese in their prose

The gate counts a section as translated when the store **has an entry** for it, so
a second pass looked for live entries whose prose — after dropping headings,
`<!--CODE-->` markers, inline code, link targets and URLs — holds eight or more
English words and no CJK character at all. It found **34 sections across 31
sheets**, and every one is correct as it stands: each is a heading already
translated into Chinese over a list of LC problem titles or reference-link
titles, which house rule keeps in English.

| Heading (zh) | Sheets |
|---|---|
| `LeetCode 題目清單` — the LeetCode problem-list link | Collection, bst_examples, dfs_advanced, dp_advanced, graph_advanced, graph_examples, sliding_window_advanced, sort, tree_examples, tree_lca_distance |
| `參考資料` / `補充資源` — external reference links | backtrack, binary_search, bst, dfs, difference_array, heap, heap_language_apis, intervals, kadane_algorithm, monotonic_stack, palindrome, prefix_sum, python_gotchas, queue, scanning_line, sliding_window, tree |
| `常見題目` / `相關題目` / `LeetCode 題目` — LC-title lists | dp_pattern (2), stock_trading, tree2 (3) |
| `另見` — links to sibling sheets | shortest_path_comparison |

## Parked entries

23 `<!-- stale: … -->` entries are still parked across 11 overlay files.
`compose` ignores them, so none reaches a page, and none stands in for a missing
section — each sheet's live entries already cover it. They are left in place:
`sync --prune` is the only command that discards parked work.

| Overlay | Parked |
|---|---|
| `i18n/zh/binary_search.md` | 5 |
| `i18n/zh/tree_examples.md` | 4 |
| `i18n/zh/binary_tree.md` | 2 |
| `i18n/zh/bit_manipulation_examples.md` | 2 |
| `i18n/zh/dfs.md` | 2 |
| `i18n/zh/memory_constrained_algorithms.md` | 2 |
| `i18n/zh/recursion.md` | 2 |
| `i18n/zh/bit_manipulation.md` | 1 |
| `i18n/zh/concurrency_patterns.md` | 1 |
| `i18n/zh/dp_string.md` | 1 |
| `i18n/zh/sort.md` | 1 |
