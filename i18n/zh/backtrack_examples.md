<!-- 359f530a086b -->
# 回溯 — LC 題目實作

> **範圍** — 回溯題解的長尾（LC 17、39、79、78、90、77、46、22、93、139、140、207），附遞迴軌跡與差一點就一樣的變形，每題每種語言只留一份正典解 — 它本身不教任何模板，每一節都指回它所實例化的那一個。
> **另見**：[backtrack.md](./backtrack.md) — 這裡每個範例所實例化的模板，以及挑模板用的決策表；[backtrack_advanced.md](./backtrack_advanced.md) — Hard 等級的模板（LC 212、282、301）；[tree_backtrack.md](./tree_backtrack.md) — 樹上 root→leaf 的路徑題。

<!-- 2de3e520ab98 -->
## LeetCode 題目清單

- [Backtracking](https://leetcode.com/problem-list/backtracking/)
- [Recursion](https://leetcode.com/problem-list/recursion/)

<!-- bc03b4bb961e -->
## 總覽

這是 [backtrack.md](./backtrack.md) 的範例倉庫。每一節都在實例化那張表裡的某個模板 — 先讀模板，
再回來看完整解、遞迴軌跡，以及那些只改一行就沿用同一個迴圈的鄰居題。

<!-- c190cecb21af -->
### 關鍵性質

- **複雜度**：見母表的 [Time Complexity by Problem Type](./backtrack.md#time-complexity-by-problem-type) 表格
- **核心想法**：每題每種語言只放一份正典解；只有當上方註解說明第二種寫法教到了第一種沒教的東西時，才會出現第二個版本
- **什麼時候用**：當你已經知道該套哪個模板，想看它從頭到尾跑一遍的時候

<!-- dd15e10f194e -->
## 題型分類

| § | 題目 | 實例化的模板 |
|---|---------|--------------|
| 1 | LC 17 Letter Combinations | [Template 1](./backtrack.md#template-1-choose--explore--un-choose-) — 由索引驅動的選擇清單 |
| 2 | LC 39 Combination Sum（＋ LC 216） | [Template 7](./backtrack.md#template-7-combination-sum--lc-39--lc-40-) |
| 3 | LC 79 Word Search（＋ LC 980、1219） | [Template 9](./backtrack.md#template-9-grid--word-search--lc-79-) |
| 4 | LC 78 Subsets | [Template 3](./backtrack.md#template-3-subsets--lc-78-) |
| 5 | LC 90 Subsets II | [Template 4](./backtrack.md#template-4-subsets-ii-skip-same-level-duplicates--lc-90-) |
| 6 | LC 77 Combinations | [Template 6](./backtrack.md#template-6-combinations--lc-77) |
| 7 | LC 46 Permutations（＋ LC 526、996、784） | [Template 5](./backtrack.md#template-5-permutations--lc-46-) |
| 8 | LC 22 Generate Parentheses | 用計數器當限制條件 |
| 9 | LC 93 Restore IP Addresses | [Template 8](./backtrack.md#template-8-palindrome-partitioning--lc-131-)，換成數值判斷式 |
| 10 | LC 139 Word Break | 子字串之間的可達性（BFS，不是回溯） |
| 11 | LC 140 Word Break II | [Template 8](./backtrack.md#template-8-palindrome-partitioning--lc-131-)，換成字典判斷式 |
| 12 | LC 207 Course Schedule | DFS 搭配*在 visiting 集合上做 undo* — 環偵測 |

<!-- 598a9bfd2a3c -->
## LC 範例

<!-- 281d96d0a663 -->
### 2) Combination Sum — LC 39 ⭐⭐⭐⭐

> 下面的 **V0** 是對的，但**很浪費**：沒有 `start_idx`，所以它會把每一種*排列順序*都跑一遍
> （例如 `[2,3]` 和 `[3,2]`），最後再靠 `sort()` + `tmp not in res` 去重。
> 請優先用 **V1**（有 start_idx，傳 `i` 讓元素可以重複使用）— 它根本不會產生重複解。

<!--CODE-->

**視覺化軌跡（遞迴樹）** — `candidates = [2, 3, 6, 7]`、`target = 7` → 答案 `[[2,2,3],[7]]`

> 每個節點是一次 `dfs(start, path, total)` 呼叫。我們傳的是 **`i`**（不是 `i+1`），
> 所以同一個候選值可以**重複使用**。只要 `total > target` 就剪掉這條分支（`✗`）；`total == target` 時記錄（`✅`）。

<!--CODE-->

> **怎麼看這棵樹**：深度 = `path` 裡有幾個數字；`start` 索引（0/1/2/3）往下走時會縮小選擇清單，
> 所以我們永遠不會回頭去拿較早的候選值 → 不會有重複組合。把遞迴呼叫改成 `i + 1`（每個只能用一次）
> 就變成 LC 40。

<!-- 81ba7c760117 -->
#### 2') 變形 — Combination Sum III — LC 216


**轉折**：候選池是*隱含*的排序清單 `1..9`，而且現在有**兩個**停止條件 — `len(path) == k`
**以及** `total == n`。因為候選池已排序，超過目標時用 `break`（不是 `continue`）可以把迴圈剩下的尾巴全部剪掉。

<!--CODE-->

<!-- 8f6a4c861ce8 -->
### 3) Word Search — LC 79 ⭐⭐⭐⭐

> 原地改成 `board[r][c] = '#'` 的版本是正典的
> [Template 9](./backtrack.md#template-9-grid--word-search--lc-79-)。下面是
> **`visited[][]` 矩陣**版 — 演算法完全一樣，但它不會改動輸入，而面試官有時候就是要你這樣寫。

<!--CODE-->

<!--CODE-->

<!-- 0bc7f58056c7 -->
#### 3') 變形 — 同一個格子模板，不同回傳值

LC 79 回傳 **boolean** 而且會短路（`if dfs(...): return True`）。下面兩題沿用一模一樣的
*標記 → 四方向遞迴 → 取消標記*骨架，但它們必須**把每條路走到底**，所以沒有提前退出 —
它們累積的是一個計數或一個最大值。

| LC | 回傳 | 標記技巧 | 為什麼不能提前退出 |
|----|---------|-----------|----------------------|
| 79 Word Search | `bool` | `visited[][]` 或 `board[r][c]='#'` | 第一個找到的就算贏 |
| 980 Unique Paths III | `int` 計數 | 把格子設成 `-1`（障礙值） | 必須數出**所有**合法路徑 |
| 1219 Path with Maximum Gold | `int` 最大值 | 把格子設成 `0`（空值） | 必須比較**所有**路徑 |

**轉折（LC 980）** — 「走過所有格子」這個條件變成一個額外的 `remain` 計數器貫穿整個遞迴；
只有 `remain == 0` 時走到終點格才算數。

<!--CODE-->

**轉折（LC 1219）** — 沒有固定起點，所以 DFS 要從**每一格**啟動；遞迴是*回傳*最佳子路徑的值，
而不是寫進一個共用的 list。

<!--CODE-->

> 另見 [backtrack_advanced.md Template 1](./backtrack_advanced.md#template-1-trie--grid-backtracking--lc-212-word-search-ii-) — 這個格子模板的多字版本，
> 用一個 **Trie 節點**取代指向單一字串的 `idx` 游標。

<!-- 44a293ae2e2b -->
### 4) Subsets — LC 78 ⭐⭐⭐⭐⭐

> 在每個節點都記錄一次的正典解是
> [Template 3](./backtrack.md#template-3-subsets--lc-78-)。這裡的兩個版本是
> *不同的演算法*：Python 那個是**依大小**建子集（`k = 0..n`），而 Java 的
> `helper` 是**選／不選的二元決策樹**。

<!--CODE-->

**視覺化軌跡（遞迴樹）** — `nums = [1, 2, 3]` → `2^3 = 8` 個子集

> 節點 = 一次 `backtrack(start, path)` 呼叫。和組合／排列題不同，子集是在
> **每一個節點都記錄 `path`**（前序），不是只在葉節點記錄。`start` 只會往前走（`i + 1`），
> 所以每個元素最多用一次，也就不會出現重複子集。

<!--CODE-->

> **關鍵對照**：記錄前沒有 `end_condition` 這道關卡 — 子集在任何深度都是合法的。
> 把它看成「選／不選」的二元視角（見下面的 Java `helper`），畫出來就是一棵高度 `n`、
> 有 `2^n` 個葉節點的完滿二元樹。

<!--CODE-->

<!--CODE-->

<!-- d808b5a6780f -->
### 6) Combinations — LC 77

> `len(path) == k` 那個版本是 [Template 6](./backtrack.md#template-6-combinations--lc-77)。
> 下面是**選／跳的二元**寫法 — 完全沒有 `for` 迴圈，只有兩個遞迴呼叫。

<!--CODE-->

<!--CODE-->

<!-- 6feae13604b0 -->
### 7) Permutations — LC 46 ⭐⭐⭐⭐⭐

> `visited[]` 那個版本是 [Template 5](./backtrack.md#template-5-permutations--lc-46-)。
> 下面是 `if i not in cur` 的版本 — 形狀一樣，但成員檢查是 O(n) 而不是 O(1)，
> 這正是面試時該寫 `visited[]` 的理由。

<!--CODE-->

**視覺化軌跡（遞迴樹）** — `nums = [1, 2, 3]` → `3! = 6` 種排列

> 節點 = 一次帶著 `visited` 集合的 `dfs(path)` 呼叫。排列題**沒有 `start_idx`** —
> 每一層都掃過**全部** `nums`，只跳過已經在 `visited` 裡的元素。只有在**葉節點**、
> 也就是 `len(path) == len(nums)` 時才記錄 `path`（`✅`）。

<!--CODE-->

> **和子集的關鍵對照**：分支數每往下一層就**變少**（3 → 2 → 1），因為 `visited` 一直在長；
> 而且結果**只出現在葉節點** — 所以是 `n!` 個葉節點，而不是 `2^n` 個節點。

<!-- f68fb84eb6dc -->
#### 7') 變形 — 排列迴圈只多加一個 `if`

下面每一題都是 LC 46 的骨架（`對每個還沒用過的值 → 選 → 遞迴 → 取消選`）。
唯一改變的是迴圈裡多加的那道**守門條件**：

| LC | 迴圈裡多加的守門條件 | 換到了什麼 |
|----|-----------------------------|--------------|
| 46 Permutations | *（無）* | 全部 `n!` 種順序 |
| 47 Permutations II | `i > 0 and a[i] == a[i-1] and not used[i-1]` | 跳過同一層的重複值 |
| 526 Beautiful Arrangement | `v % pos == 0 or pos % v == 0` | n=15 時把樹剪到只剩幾千個節點 |
| 996 Number of Squareful Arrays | 上面兩個都要，再加 `is_square(path[-1] + a[i])` | 去重**加上**相鄰限制 |

**轉折（LC 526）** — 改成對**位置**遞迴（`pos = 1..n`）、對*值*做迴圈，這樣值一放下去就能
馬上檢查整除條件。因為只需要**個數**，所以根本不用建 `path` 這個 list。

<!--CODE-->

**轉折（LC 996）** — 把 LC 47 的去重規則*疊在*相鄰限制*之上*。注意去重規則需要陣列先**排序**，
而且讀的是 `not used[i-1]`（相等的前一個值不在目前路徑上 → 代表我們在同一層 → 跳過）。

<!--CODE-->

**轉折（LC 784, Letter Case Permutation）** — 它*根本不是*排列：順序是固定的，我們是**逐索引**分支，
字母兩條路、數字一條路。它其實是 LC 78 子集的形狀（每個位置做二元選擇），只是頂著「permutation」這個名字。

<!--CODE-->

<!-- e9d34d0fc49e -->
### 8) Generate Parentheses — LC 22

> 三個版本、三種不同的演算法：**V0** 產生所有長度 `2n` 的字串再去*驗證*
> （指數級的浪費 — 放在這裡是因為它是最直覺的第一個想法）；
> **V0'** 帶著剩餘的 `(` / `)` 數量，所以只會蓋出合法的前綴；
> **Java** 版就是 V0'，但改用 `StringBuilder` 和明確的 undo，而不是字串串接。

<!--CODE-->
<!--CODE-->

<!--CODE-->

<!-- bb8eea158bad -->
### 10) Word Break — LC 139

> 放在這裡是因為它是下面 LC 140 的*判定版*雙胞胎 — 但注意它的解法是
> **對起始索引做 BFS**，不是回溯：沒有東西需要 undo。

<!--CODE-->

<!-- 54c57cc0ab11 -->
### 11) Word Break II — LC 140

> **V0** 是列舉*字典*（把每個單字接上去，再測試接出來的前綴）；**V1** 是列舉*字串*
> （切出每一個前綴，再測試它在不在字典裡）— 該寫的是第二種，它遇到第一個不是單字的前綴就會剪枝。
> **V1'** 兩者都不是：它用迭代方式建一張 parent-pointer DAG，再倒著走一遍，所以完全不遞迴。

<!--CODE-->

<!-- 557c2bf25c6b -->
### 12) Course Schedule — LC 207

> 從回溯的角度看：`visiting.remove(crs)` **就是**取消選擇那一步 — 這個集合裝的是目前的 DFS 路徑，
> 所以在裡面重複出現就代表有環。Kahn／入度的做法見
> [topology_sorting.md](./topology_sorting.md)。

<!--CODE-->

<!-- 1a5d4813b258 -->
## 總結與速查

| 如果上面的範例讓你覺得陌生 | 回去看 |
|---|---|
| 為什麼要 `path.pop()` ／ 什麼時候不用 | [Template 14](./backtrack.md#template-14-when-to-undo--mutable-vs-immutable-state-) |
| 遞迴呼叫要傳 `i` 還是 `i + 1` | [Template 2](./backtrack.md#template-2-start_idx--i-vs-i--1-) |
| 怎麼跳過重複值 | [Duplicate skipping](./backtrack.md#duplicate-skipping--the-same-level-skip-rule-) |
| 剪枝時該用 `break` 還是 `continue` | [Sort, dedup, prune — when](./backtrack.md#sort-dedup-prune--when) |
| 怎麼從題目敘述挑出對的形狀 | [Decision Table](./backtrack.md#decision-table--which-backtrack-shape-) |

<!-- 962b16149816 -->
### 相關主題

- [backtrack.md](./backtrack.md) — 模板本身
- [backtrack_advanced.md](./backtrack_advanced.md) — LC 212 / 282 / 301
- [tree_backtrack.md](./tree_backtrack.md) — root→leaf 路徑題（LC 113、257、129、437）
- [dfs.md](./dfs.md) — 不需要 undo 的走訪
