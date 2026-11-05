# Cheatsheet 審查 2026-11 — 執行進度

追蹤 [審查](cheatsheet-review-2026-11.zh-tw.md)（[English](cheatsheet-review-2026-11.md)）的〈建議順序〉一項一項落地的情況。**一個項目一個 PR，照順序做**；每個 PR 合併前把自己那一列改成 ✅ 並填上 PR 編號。

狀態：✅ 已合併 · 🔄 PR 開著 · ⬜ 還沒開始

## 總表

| 步驟 | 項目 | 範圍 | PR | 狀態 |
|---|---|---|---|---|
| 1 | 模板、風格指南、審查本身 | `00_template.md`、`CLAUDE.md`、`/lc-cheatsheet`、card gate | [#290](https://github.com/yennanliu/CS_basics/pull/290) | ✅ |
| — | 這份進度文件 | `doc/cheatsheet-review-2026-11-progress.md` | [#301](https://github.com/yennanliu/CS_basics/pull/301) | 🔄 |
| 2 | **P0** 修 bug | §9 的 9 個 verified + 11 個 reported、Java 減法 comparator | [#302](https://github.com/yennanliu/CS_basics/pull/302) | 🔄 |
| 3 | **P7** lint + baseline | `script/check_cheatsheets.py` + baseline + workflow | [#303](https://github.com/yennanliu/CS_basics/pull/303) | 🔄 |
| 4 | **P1** 入口 | `lc_pattern.md` 改成 router；`startHere` 第一階 | [#305](https://github.com/yennanliu/CS_basics/pull/305) | 🔄 |
| 5a | **P2** tree 家族 | `tree`、`binary_tree`、`bst`、`tree_lca_distance` | [#306](https://github.com/yennanliu/CS_basics/pull/306) | 🔄 |
| 5b | **P2** graph 家族 | `bfs`、`dfs`、`graph` | [#307](https://github.com/yennanliu/CS_basics/pull/307) | 🔄 |
| 5c | **P2** arrays / strings 家族 | `array`、`string`、`2_pointers`、`sliding_window`、`binary_search`、`binary_search_on_answer` | | ⬜ |
| 5d | **P2** dp 家族 | `dp`、`backtrack`、`knapsack` | | ⬜ |
| 5e | **P2** hashing / stacks 家族 | `hash_map`、`stack`、`linked_list`、`heap` | | ⬜ |
| 6a | **P3 + P4** tree 家族 | tree path 去重；`tree_examples`、`bst_examples` 改 Skeleton C，`bst_advanced` 改 D | | ⬜ |
| 6b | **P3 + P4** graph 家族 | 最短路徑比較去重；`bfs_examples`、`dfs_examples`、`graph_examples`、`topology_sorting_examples` 改 C，`bfs_advanced`、`dfs_advanced`、`graph_advanced` 改 D | | ⬜ |
| 6c | **P3 + P4** arrays / strings 家族 | quickselect、KMP / rolling hash 去重；`array_examples`、`string_examples`、`2_pointers_examples`、`sliding_window_examples`、`binary_search_examples` 改 C，`sliding_window_advanced` 改 D | | ⬜ |
| 6d | **P3 + P4** dp 家族 | 0/1 knapsack 去重；`dp_examples`、`backtrack_examples` 改 C，`dp_advanced`、`backtrack_advanced` 改 D | | ⬜ |
| 6e | **P3 + P4** hashing / stacks 家族 | `monotonic_stack` vs `stack_examples`；`hash_map_examples`、`stack_examples`、`linked_list_examples`、`heap_examples`、`design_examples` 改 C，`heap_advanced` 改 D | | ⬜ |
| 7a | **P5** 第二階段 | `build-site.js` 在 Drill Path 旁標出 `progress.txt` 的 verdict | | ⬜ |
| 7b | **P6** 雜訊與語言 | Python 優先、IPython transcript、行內中文、檔名修正 | | ⬜ |

P2 的 24 份 tier 5 cheatsheet 裡有 4 份不在 5a–5e：`lc_pattern` 由 P1 改寫，`knapsack_01_zh` 是 P3 #2 的 0/1 knapsack 中文 home，`python_complexity` 與 `complexity_cheatsheet` 是查表型的參考文件（沒有 pattern card 可補），在 P1 合併 constraint ruler 時一起處理。

6a–6e 只涵蓋 24 份 tier 5 主檔的 satellite。其餘 9 份 `_examples`（`greedy`、`matrix`、`prefix_sum`、`set`、`scanning_line`、`trie`、`union_find`、`bit_manipulation`、`Dijkstra`）和 `prefix_sum_advanced` 在 6e 之後補一列，再決定怎麼分。

## 指標

每個 PR 合併時更新「現在」一欄。P7 之後讀 `python3 script/check_cheatsheets.py --report`；它的算法和審查的量測略有不同（見括號），以 lint 為準。

| 指標 | 審查時（`6ad1564dd`） | 目標 | 現在 |
|---|---:|---|---:|
| §9 verified bug | 9 | 0 | 0 |
| Java 減法 comparator `(a, b) -> a - b` | 26 | 0 | 0（實際掃到 75 處：61 個 lambda + 14 個 `return a - b`；`java_trick_strings_sorting` 示範反例的 3 行保留） |
| 有 recognition 標題的檔案 | 37 / 135 | 所有 tier ≥ 4 主檔 | 缺的 tier ≥ 4 主檔：37（lint `no-recognition`） |
| 沒有 `## Drill Path` 的 tier ≥ 4 主檔 | — | 0 | 59（lint `no-drill-path`） |
| 沒有 `## Index` 的 `_examples` | 27 | 0 | 27 |
| 沒有 `## When Not to Use` 的 `_advanced` | 9 | 0 | 9 |
| `### … LC n` 標題後直接接 code | 401 | 0 | 401（lint `code-first-example`） |
| 沒有 insight 行的範例 | 570 | < 100 | 570 |
| `NOTE !!!` | 583 行 / 69 檔 | < 50 | 567（lint `note-wall`，只算 `NOTE !!!` 本身） |
| IPython `In [n]:` | 224 行 / 10 檔 | 0 | 347（lint `ipython`，`Out[n]:` 也算） |

## 每個 PR 的檢查清單

- [ ] 只做總表裡那一列的範圍；順手發現的問題記到下方〈待辦〉，不夾帶
- [ ] 每段改過的 snippet 都拿題目自己的 example 跑過
- [ ] `python3 script/check_cheatsheet_cards.py`（新寫或改寫的段落是 card）
- [ ] `python3 script/check_cheatsheets.py`（沒有數字變多；變少的話 `--update-baseline`）
- [ ] `node script/zh.js check cheatsheet`（改到 prose 的段落，中文 overlay 同步）
- [ ] `bash site/build.sh && node site/e2e-check.js _site && npm test --prefix site`
- [ ] 這份文件：總表那一列、〈指標〉的「現在」、〈紀錄〉加一行

## 待辦

執行中發現、但不屬於當下那個 PR 的事。

- [x] `lc_pattern.md` 的錯誤歸類（§9 最後一列 reported）：P1 整份改寫成 router 時一起解決
- [ ] （6a，P4）`tree_lca_distance` 的 LC 742 worked example 有 bug：建完圖又重設、BFS 從 root 而不是 k 出發
- [ ] （6a，P3）`binary_tree` Template 4、6 與 `tree_lca_distance` 的 path / LCA card 重複；`tree2` 重複 `binary_tree` 大部分模板；LC 437 同時在 `prefix_sum_advanced` Template 14；LC 543/124/687 在兩處
- [ ] （6a，P4）`bst.md` 的「Problems by Pattern」把 LC 669、938 指到錯的模板；Basic Operations 仍是 Java 先、Summary 的 Common Mistakes 太空泛
- [ ] （6b，P3）LC 785 二分圖同時是 `bfs` T7 與 `graph` T6 的 card；LC 1971 在兩份都有
- [ ] （6b，P4）`bfs` / `dfs` 裡早於 card 的 variation（LC 101、111、419、797、124、979、2049、542）還不是 problem card
- [ ] （跨 PR）中文 card 標籤：graph 家族用了 辨識 / 關鍵 / 複雜度 / 試跑 / 陷阱 / 不適用時 / 練習，其他家族不一定一致；P6 統一

## 紀錄

| 日期 | 步驟 | 備註 |
|---|---|---|
| 2026-11-05 | 1 | #290 合併：pattern card / problem card、Skeleton A–D、card gate 與 Claude Code hook |
| 2026-11-05 | 2 | P0：9 個 verified + 10 個 reported 修好並用題目 example 跑過（`lc_pattern` 移到 P1）；comparator 改成 `Integer.compare`；改到 prose 的 8 個段落中文 overlay 同步 |
| 2026-11-05 | 3 | P7：`check_cheatsheets.py` 14 條規則、每檔每規則計數的 baseline（1,672 筆）、CI job；第一次跑就抓到 P0 漏掉的一個 comparator（已補進 #302） |
| 2026-11-05 | 4 | P1：`lc_pattern.md` 改寫成 router（10 個家族表、69 列，每列都連到實際存在的 card anchor）；constraint ruler 合併到 `complexity_cheatsheet` §6，`time_space_complexity` 留指標；中文 overlay 全部重寫 |
| 2026-11-05 | 5a | P2 tree：27 張 pattern card（tree 3、binary_tree 11、bst 8、tree_lca_distance 5），四份都加 Recognition 與 Drill Path；smoke test 修掉 O(n²) 串接 / 切片、無法編譯的 Java、不能跑的 pseudocode |
| 2026-11-05 | 5b | P2 graph：23 張 pattern card（graph 6、bfs 7、dfs 10）；修掉沒解到錨定題的模板（dfs T2–T6、graph T1、T5 未定義的名稱）；Recognition 與 Drill Path |
