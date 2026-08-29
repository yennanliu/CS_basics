<!-- 8d231431c4e4 -->
# 進階回溯

> **範圍** — 需要在遞迴中額外攜帶狀態的困難級回溯 — 一個 Trie 節點、前一個運算元、一份刪除額度 — 再加上約束傳播、記憶化搜尋，以及第一輪學習應該直接跳過的通用切分模板；必背的基本形狀不會在這裡重複。
> **另見**：[backtrack.md](./backtrack.md) — 必背模板與決策表；[backtrack_examples.md](./backtrack_examples.md) — 那些模板的例題解法；[trie.md](./trie.md) — 字典樹（Trie）本身；[dp.md](./dp.md)／[knapsack.md](./knapsack.md) — 記憶化切分最後會走到的地方。

<!-- 3a98de53945b -->
## LeetCode 題目清單

- [Backtracking](https://leetcode.com/problem-list/backtracking/)
- [Trie](https://leetcode.com/problem-list/trie/)

<!-- 7be76fac7476 -->
## 總覽

三個**無法**化約成「在索引上選／不選」的模板。每一個都在遞迴中多帶一份狀態 — 一個 **Trie 節點**、**前一個運算元**，或一份**刪除額度** — 而那份多出來的狀態就是整題的關鍵。

這頁的所有內容都假設[選擇 → 探索 → 撤銷的骨架](./backtrack.md#template-1-choose--explore--un-choose-)對你已經是反射動作。當基本形狀不夠用時再來看這裡：對每個輸入重跑一次簡單模板太慢、部分解需要被**評分**而不只是被收集，或搜尋要先傳播約束才跑得動的時候。

<!-- a8a188510919 -->
### 關鍵性質

- **複雜度**：最壞情況是指數級，跟任何回溯一樣 — 見 [Time Complexity by Problem Type](./backtrack.md#time-complexity-by-problem-type) 表格
- **核心想法**：在遞迴中額外攜帶的那份狀態**就是**關鍵
- **什麼時候用**：困難級面試；第一輪學這個主題請直接跳過這頁

<!-- 2096a7cbec84 -->
## 題型分類

| 模板 | 額外攜帶的狀態 | 例題 |
|----------|--------------------|----------------|
| Trie 剪枝的網格搜尋 | 當前的 `TrieNode` | LC 212 Word Search II |
| 建構運算式 | `prev` 運算元（處理 `*` 優先序） | LC 282 Expression Add Operators |
| 刪除額度回溯 | 還能刪的 `(l, r)` 字元數 | LC 301 Remove Invalid Parentheses |

再加上切分家族 — 它的通用模板與記憶化形式放在這裡，而不放在主篇：

**定義**：依某種條件把輸入切成若干群組或區段。

**常見的切分類型**：

**1. 等和切分**
- 把陣列分成總和相等的群組
- 例題：LC 416（Partition Equal Subset Sum）、LC 698（K Equal Sum Subsets）

**2. 迴文切分**
- 把字串切成若干迴文子字串
- 例題：LC 131（Palindrome Partitioning）、LC 132（Palindrome Partitioning II）

**3. 子集切分**
- 依約束條件把元素分組
- 例題：LC 90（Subsets II）、LC 47（Permutations II）

<!-- ddc7f484e4af -->
## 模板與演算法

<!-- 937e210bdd91 -->
### 模板 1：Trie + 網格回溯 — LC 212 Word Search II ⭐⭐⭐⭐


**核心想法**：LC 79 只要找**一個**字，對每個字重跑一次是 `O(W · M · N · 4^L)`。改成把**所有字塞進一棵 Trie**，然後只走網格**一次**，在 `(r, c)` 旁邊多帶著當前的 Trie 節點。只要 Trie 沒有對應那個字母的子節點，這條分支立刻死掉。

**三個關鍵動作**
1. **用 Trie 節點當「索引」** — 取代指向單一字串的 `idx`；一次 DFS 就涵蓋所有字。
2. **原地標記**（`board[r][c] = '#'`，遞迴後還原）— 不需要 `visited` 矩陣。
3. **葉節點剪枝** — 遞迴回來後，如果某節點已經沒有子節點，就把它從父節點上斷開。這讓 Trie 持續縮小，也是最壞情況還能忍受的原因。

**去重的小轉折**：收集到字之後立刻把 `node.word = null`，而不是額外用一個 `Set`。

<!--CODE-->

<!--CODE-->

> **和 LC 79 的對比**：LC 79 一配對成功就沿著堆疊回傳 `True`（提前結束）。LC 212 命中之後還得繼續探索，因為可能有更長的字延續同一條路徑。

<!-- 316a4e094eea -->
### 模板 2：建構運算式（插入運算子）— LC 282 Expression Add Operators ⭐⭐⭐⭐


**核心想法**：在每個數字之間的縫隙，對 `+ | - | *` 分支（同時也對當前運算元吃掉幾位數字分支）。唯一難的地方是 `*` 的**優先序**：你不能直接把它乘進累計總和，因為 `2 + 3 * 2` 必須是 `8`，不是 `10`。

**`prev` 這個技巧** — 把**上一個運算元套用時的樣子**帶著走：

<!--CODE-->

**兩個絕對不能忘的防線**
- **前導零**：`if j > idx and num[idx] == '0': break` → `"05"` 永遠不是合法運算元。
- **溢位**：Java 要用 `long` — 中間的乘積會衝破 `int`。

<!--CODE-->

<!--CODE-->

> **同樣的形狀，不同的題目**：LC 679（24 Game）是另一種「建構運算式」的回溯 — 那裡是從清單裡**挑兩個運算元**、套一個運算子，再對縮小後的清單遞迴（而且要用浮點數 epsilon 比較，不能用 `==`）。

<!-- c9262e0a8bf3 -->
### 模板 3：刪除額度回溯 — LC 301 Remove Invalid Parentheses ⭐⭐⭐⭐


**核心想法**：「刪掉**最少**數量的字元」→ 不要窮舉所有刪法。先一趟掃描**數出**多餘的 `(` 和 `)` 各有幾個，然後把這個數字當成**額度**去回溯。任何走到結尾且 `budget == 0` 的字串，自動就是最少刪法。

**數多餘的量**（一趟掃描）：
<!--CODE-->

**每個字元剛好兩條分支**：*刪掉它*（只在該額度 > 0 時）或*留下它*（`)` 只在 `open > 0` 時才留，否則前綴已經非法 → 剪枝）。

<!--CODE-->

<!--CODE-->

> **不用 Set 的版本**：不要用 `HashSet`，改成刪字元時一次跳過**所有相同的連續字元**（`while i+1 < n and s[i+1] == s[i]: i++`）— 同樣的

<!-- cdcda62db8ed -->
### 模板 4：約束傳播（提前終止）

比單純的界線檢查更進一步：在遞迴之前先把約束往前傳播。這正是把 O(n!) 暴力法和實用回溯區分開來的關鍵洞見。

<!--CODE-->

例子：數獨中放下一個數字後，立刻把它從同列／同行／同宮的候選中刪掉。只要有任何格子的候選數歸零，馬上回溯，不必再往下鑽。

> 只用列／行／宮集合的樸素數獨版本在
> [backtrack.md 模板 11](./backtrack.md#template-11-sudoku-solver--lc-37)；這裡講的是疊在它上面的最佳化。

<!-- 95b67a9d8b0e -->
### 模板 5：通用切分模板

兩個只有骨架的模板 — 合法性判斷由外部傳進來。當題目切的東西既不是迴文字串、也不是 `k` 個等和桶時很好用。

**1. 字串切分模板**：
<!--CODE-->

**2. 陣列切分模板**：
<!--CODE-->

<!-- 3555c82cdadf -->
### 模板 6：等和子集切分 — LC 416

純粹的選／不選遞迴，不需要收集路徑。這是 0/1 背包的回溯**祖先** — 同一棵呼叫樹，只是還沒記憶化。DP 形式見 [knapsack.md](./knapsack.md)。

<!--CODE-->

<!-- e8b33f9868ef -->
### 模板 7：記憶化回溯

一旦遞迴是由**狀態**而非路徑決定，搜尋就塌縮成 DP。這是機械式的中繼站：保留回溯的形狀，在狀態 tuple 上加一層 `memo`。這裡只有骨架 — `check_valid_partition`、`get_choices` 和 `update_state` 要自己填。

<!--CODE-->

> 當每條分支都只由狀態決定，就把遞迴整個丟掉 — 見
> [recursion_to_dp.md](./recursion_to_dp.md)。

<!-- 6aaf9baa07a2 -->
### 模板 8：桶切分 + 提前終止

`if len(groups[i]) == 0: break` 這一行是撐起整段的關鍵：試第二個**空**桶，只是把你剛剛否決過的切分重新貼個標籤而已。

<!--CODE-->

<!-- 7c884de95c6b -->
### 框架變體（虛擬碼）

骨架的「先驗證再放置」寫法 — `is_valid` 放在 `place` 之前，而不是在遞迴呼叫的開頭檢查。只是骨架，不能直接跑。

<!--CODE-->

<!-- ea4fa3974f42 -->
## 總結與速查

<!-- 76910f1a9985 -->
### 剪枝與切分比較

| 技巧 | 目的 | 什麼時候用 | 對複雜度的影響 |
|-----------|---------|-------------|-------------------|
| **約束剪枝** | 提前終止 | 非法狀態 | 大幅減少分支數 |
| **界線剪枝** | 砍掉不可能更好的路徑 | 最佳化問題 | 有機會 O(2^n) → O(n!) |
| **對稱剪枝** | 避免重複 | 排列問題 | 消掉階乘級的重複 |
| **等和切分** | 分成總和相等的群組 | 子集和問題 | 指數級降到多項式 |
| **字串切分** | 依條件切開 | 字串分段 | 最壞 O(2^n) |

<!-- 803823b0008b -->
### 該用哪個進階模板？

| 題目裡的訊號 | 模板 |
|---|---|
| 一堆字／樣式要比對同一個網格或串流 | [模板 1](#template-1-trie--grid-backtracking--lc-212-word-search-ii-) |
| 建構運算式／插入運算子 | [模板 2](#template-2-expression-building-operator-insertion--lc-282-expression-add-operators-) |
| 「刪掉**最少**數量的字元」 | [模板 3](#template-3-deletion-budget-backtracking--lc-301-remove-invalid-parentheses-) |
| 寫法沒錯但還是超時 | [模板 4](#template-4-constraint-propagation-early-termination) |
| 在某個判斷條件下切成 `k` 組 | [模板 5](#template-5-generic-partitioning-templates)／[模板 8](#template-8-bucket-partitioning-with-early-termination) |
| 同一個**狀態**一直重複出現 | [模板 7](#template-7-memoised-backtracking) → [dp.md](./dp.md) |

<!-- 19b9653e9c4a -->
### 相關主題

- [backtrack.md](./backtrack.md) — 必背模板
- [backtrack_examples.md](./backtrack_examples.md) — 那些模板的例題解法
- [trie.md](./trie.md) — Trie 的建構與搜尋
- [dfs_advanced.md](./dfs_advanced.md) — Trie + DFS 萬用字元搜尋（LC 211）、Tarjan、Hierholzer
- [knapsack.md](./knapsack.md)／[recursion_to_dp.md](./recursion_to_dp.md) — 記憶化回溯最後落腳的地方
