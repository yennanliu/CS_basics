<!-- 3a8ec5a4d0c9 -->
# 回溯（Backtracking）

> **範圍** — 帶「復原」動作的系統性搜尋：choose/explore/un-choose 骨架、`start_idx` 的控制、跳過重複、剪枝，以及每個必會題型剛好一份的標準模板 — 大量的實作解答與 hard 級的帶狀態模板放在兩個衛星檔裡。
> **另見**：[backtrack_examples.md](./backtrack_examples.md) — 這些模板對應的 LC 實作解；[backtrack_advanced.md](./backtrack_advanced.md) — 用 Trie 剪枝的格子搜尋、運算式建構、刪除次數預算、記憶化切分；[dfs.md](./dfs.md) — 沒有復原步驟的走訪；[recursion.md](./recursion.md) — 遞迴的機制；[tree_backtrack.md](./tree_backtrack.md) — 根到葉的路徑問題；[dp.md](./dp.md) — 什麼時候把搜尋記憶化比硬搜划算。

<!-- 2de3e520ab98 -->
## LeetCode 題目清單

- [Backtracking](https://leetcode.com/problem-list/backtracking/)
- [Recursion](https://leetcode.com/problem-list/recursion/)

<!-- 987ce5d0c12d -->
## 總覽

回溯就是**在決策樹上做暴力搜尋**：每一步做一個選擇、往下遞迴，然後把選擇**復原**（「回溯」）再試下一個。
要產生**所有**子集／排列／組合，或是在限制條件下找出**任一組**可行解（N 皇后、數獨、單字搜尋），
第一個該想到的就是它。

<!-- 05ae75028da1 -->
### 關鍵性質

- **核心想法**：`選擇 → 探索（遞迴）→ 取消選擇（復原）`
- **時間複雜度**：指數級 — `O(b^d)`，`b` = 分支數，`d` = 決策樹深度
- **空間複雜度**：`O(d)` 的遞迴深度（不算輸出的清單）
- **什麼時候用**：題目問*所有*／*每一種*／*有幾種*配置，或是要在限制下*擺放／填滿／切分*
- **最佳化路線**：回溯（暴力）→ 加上**剪枝** → 常常再進一步 → **DP**（把重疊子問題記憶化）
- **演算法**：DFS + 遞迴
- **常用資料結構**：`dict`（去重用的 counter）、`set`（visited／限制條件）、`array`／`list`（路徑）

<!-- 68a1fcb84fec -->
### 每個回溯都要追蹤的 3 件事

| 元素 | 意義 |
| ------- | ------- |
| **路徑**（Route） | 到目前為止做過的選擇（也就是當前路徑） |
| **選擇清單**（Choice list） | 此刻還能做的選擇 |
| **結束條件** | 決策樹的葉節點 — 記下路徑然後返回 |

<p align="center"><img src="../pic/backtrack1.png"></p>

> 上面那三列直接對應到 [Template 1](#template-1-choose--explore--un-choose-) —
> `path` 是路徑，`for` 迴圈是選擇清單，最上面那個 `if` 是結束條件。

<!-- 70ca8b3d8efc -->
### 各題型的時間複雜度

| 題型      | 典型時間      | 空間（不含輸出） | 範例      |
| ----------------- | ----------------- | -------------------- | ------------ |
| 子集           | O(2^n · n)        | O(n)                 | LC 78, 90    |
| 排列      | O(n! · n)         | O(n)                 | LC 46, 47    |
| 組合      | O(C(n,k) · k)     | O(k)                 | LC 77        |
| 組合總和   | 指數級       | O(target / min)      | LC 39, 40    |
| 切分      | O(2^n · n)        | O(n)                 | LC 131       |
| N 皇后          | O(n!)             | O(n)                 | LC 51        |

> 後面那個 `· n` / `· k` 是把每條合法路徑複製進結果的成本。
> **剪枝**砍掉分支、也砍掉常數項，但**不會**改變最壞情況的量級。

<!-- 43156ff3bd75 -->
### 參考資料

- [labuladong — Backtracking framework](https://labuladong.online/algo/essential-technique/backtrack-framework/#%E4%B8%80%E3%80%81%E5%85%A8%E6%8E%92%E5%88%97%E9%97%AE%E9%A2%98)
- [labuladong — Two views of backtracking](https://labuladong.online/algo/practice-in-action/two-views-of-backtrack/)
    - [Sudoku](https://labuladong.online/algo/practice-in-action/sudoku/)
    - [Generate parentheses](https://labuladong.online/algo/practice-in-action/generate-parentheses/)
    - [Partition to k equal sum subsets](https://labuladong.online/algo/practice-in-action/partition-to-k-equal-sum-subsets/)
- [LeetCode — A general approach to backtracking (Java)](https://leetcode.com/problems/subsets/solutions/27281/a-general-approach-to-backtracking-questions-in-java-subsets-permutations-combination-sum-palindrome-partitioning/)

<!-- 9b476c11819d -->
## 題型分類

四種形狀幾乎涵蓋所有回溯題。下面這張分類表決定你**該拿哪份模板**；
程式碼在 [Templates & Algorithms](#templates--algorithms)，
完整解答在 [backtrack_examples.md](./backtrack_examples.md)。

| # | 形狀 | 要 `start_idx` 嗎？ | 標準模板 | LC |
|---|-------|--------------|--------------------|----|
| 1 | 子集（Subsets） | ✅ `i + 1` | [Template 3](#template-3-subsets--lc-78-) / [Template 4](#template-4-subsets-ii-skip-same-level-duplicates--lc-90-) | 78, 90 |
| 2 | 排列（Permutations） | ❌ `visited[]` | [Template 5](#template-5-permutations--lc-46-) | 46, 47 |
| 3 | 組合（Combinations） | ✅ `i + 1` | [Template 6](#template-6-combinations--lc-77) | 77, 216 |
| 4 | 組合總和 | ✅ `i`（可重複用）或 `i + 1` | [Template 7](#template-7-combination-sum--lc-39--lc-40-) | 39, 40 |
| 5 | 切分 | ✅ 在子字串／桶上用 `i + 1` | [Template 8](#template-8-palindrome-partitioning--lc-131-) / [Template 13](#template-13-k-bucket-partitioning--lc-698--lc-473) | 131, 698, 473 |
| 6 | 格子／單字搜尋 | ❌ 標記格子再還原 | [Template 9](#template-9-grid--word-search--lc-79-) | 79, 980, 1219 |
| 7 | 限制條件滿足 | ❌ 每層處理一列／一格 | [Template 10](#template-10-n-queens--lc-51) / [Template 11](#template-11-sudoku-solver--lc-37) | 51, 37 |
| 8 | 括號／字串建構 | ❌ 用計數器當限制 | [backtrack_examples.md](./backtrack_examples.md#8-generate-parentheses--lc-22) | 20, 22, 93 |

<!-- 15b6031a1c33 -->
### 各型態筆記

每種形狀的隨手筆記 — 迴圈主體的口訣，以及會踩到的坑。

- 型態 1) : `Subsets`（子集）
    - 題目 : LC 78, 90, 17
    - [代碼隨想錄 - 0078.子集](https://github.com/youngyangyang04/leetcode-master/blob/master/problems/0078.%E5%AD%90%E9%9B%86.md)
    - （用 for 迴圈呼叫輔助函式）+ start_idx + for 迴圈 + pop(-1)
    - 回溯。找最小情況，把問題轉成`樹的問題`。用 `start` 把已經用過的數字排掉，回傳所有情況
    - 需要 `!cur.contains(nums[i])` -> 才不會加進重複的元素
- `Subsets II`
    - LC 90
    - start idx + 回溯 + 去重（seen）
    - 去重 : 用 dict counter 或用索引都行

- 型態 2) : `Permutations（排列組合）`（全排列）
    - 題目 : LC 46, 47
    - （用 for 迴圈呼叫輔助函式）+ contains + pop(-1)
    - 回溯。用 `contains` 把已經用過的數字排掉，回傳所有情況
    - **不需要**用 start_idx

- 型態 3) : `Combinations（組成）`
    - LC 77
    - （用 for 迴圈呼叫輔助函式）+ start_idx + for 迴圈 + 檢查 len == k + pop(-1)

- 型態 4) : `Others`

- 括號（Parentheses，括弧）
    - LC 20, LC 22

<!-- ddc7f484e4af -->
## 模板與演算法

<!-- cfbdb38c5722 -->
### 模板對照表

| 模板 | 形狀 | 迴圈／下一個索引 | 復原 | LC |
|---|---|---|---|---|
| 1 | 選擇 → 探索 → 取消選擇 | `for i in start..n` | `path.pop()` | — |
| 2 | `start_idx` 控制 | `i`（可重用）vs `i + 1`（只用一次） | — | 39 vs 40 |
| 3 | 子集 | **每個**節點都記錄，用 `i + 1` | `remove(last)` | 78 |
| 4 | 子集 II | `i > start && a[i] == a[i-1]` 就跳過 | `remove(last)` | 90 |
| 5 | 排列 | 掃**全部**，跳過 `visited[i]` | `pop()` + `visited[i] = False` | 46, 47 |
| 6 | 組合 | `len(path) == k` 就停 | `pop()` | 77 |
| 7 | 組合總和 | `i` 可重用／`i + 1` 只用一次，加 `break` 剪枝 | `pop()` | 39, 40 |
| 8 | 回文切分 | `for end in start+1..n`，過回文這關 | `remove(last)` | 131 |
| 9 | 格子／單字搜尋 | 每格往四個方向走 | 還原該格 | 79 |
| 10 | N 皇后 | 每層放一列，三個限制集合 | 三個集合都要移除 | 51 |
| 11 | 數獨 | 每層填一個空格，列／行／宮格集合 | 重設格子與集合 | 37 |
| 12 | 剪枝 | 遞迴前先 `break` / `continue` | — | 39, 40 |
| 13 | k 桶切分 | 對每個桶試一次，先由大到小排序 | `buckets[i] -= v` | 698, 473 |
| 14 | 可變 vs 不可變狀態 | — | **只有**可變狀態要復原 | 113, 1740 |

<!-- 71af99a660eb -->
### Template 1: choose → explore → un-choose ⭐⭐⭐⭐⭐

標準的「選擇 → 探索 → 取消選擇」骨架，拿去改就能用：

<!--CODE-->

<!--CODE-->

只要轉兩個旋鈕，這份模板就能變成所有變體：
- **`start_idx`** — 控制搜尋空間（組合／子集 vs 排列）
- **提早結束／剪枝** — 砍掉不可能得到合法答案的分支

<!-- b70e438b4f7c -->
#### 跳過重複 — 同一層才跳的規則 ⭐⭐⭐⭐⭐

我們**不是**每個重複值都跳 — 只跳出現在*同一層遞迴*的重複值。
同一條規則的三種寫法（片段，不是可直接執行的類別）：

<!--CODE-->

<!--CODE-->

<!--CODE-->

<!-- e6e8f92d20ff -->
### Template 2: `start_idx` — `i` vs `i + 1` ⭐⭐⭐⭐⭐

`start_idx`（或叫 `index` 之類的）是用來**控制搜尋空間**的 — 用來**避免重複**，並讓產生的結果保持順序。

-> 什麼時候要用 `start_idx`：

- 你在產生**組合／子集**
- 你想**避免重複**
- 你想**保留選擇的順序**

一旦確定需要 `start_idx`，下一個問題就是**下一層要傳什麼當起始索引** —
`i`（可以重複用當前元素）還是 `i + 1`（跳過它）。

| 傳什麼 | 意義 | 類比 | 例子 |
| ---- | ------- | ------- | -------- |
| `i`     | **同一個元素可以再用一次** | **完全背包**（數量無限） | LC 39 (Combination Sum), LC 518 (Coin Change II), LC 377 |
| `i + 1` | **每個元素最多用一次** | **0/1 背包**、子集 | LC 40 (Combination Sum II), LC 78/90 (Subsets), LC 131, LC 494 |

> 排列兩個都不用 — 它會回頭用前面的元素，所以改成維護一個 `visited[]`
> 陣列／`contains` 檢查，而不是 `start_idx`（見 [Problem Categories](#problem-categories)）。

<!--CODE-->

> **重點**：可以重複用 → `i`；只能用一次 → `i + 1`。

<!-- e00686992f8b -->
### Template 3: Subsets — LC 78 ⭐⭐⭐⭐⭐

在**每個**節點都把路徑記下來（前序），並且傳 `i + 1`，讓每個元素只用一次。
那些 `// ...` 是省略掉的樣板碼，不是真的程式碼。

<!--CODE-->

<!--CODE-->

<!-- 0387a8800b4f -->
#### 有重複元素的排列 — LC 47

用 `Counter` 取代 `visited[]`：選的時候減一，取消選擇時加回去。
另一種寫法（排序 + `i > 0 and a[i] == a[i-1] and not used[i-1]`）就是
[跳過重複](#duplicate-skipping--the-same-level-skip-rule-)裡的那段 Java。

<!--CODE-->

<!-- 2b14f9f7b748 -->
### Template 6: Combinations — LC 77

`start_idx` 加上一個長度關卡。一旦 `len(path) == k` 就停。

<!--CODE-->

<!-- 3cc6345cef39 -->
### Template 7: Combination Sum — LC 39 / LC 40 ⭐⭐⭐⭐

遞迴呼叫傳 `i` 代表*可以重複用*（LC 39）；傳 `i + 1` 代表*只能用一次*（LC 40）。
LC 40 還額外需要同一層的重複跳過。

<!--CODE-->

<!--CODE-->

**Python — LC 40，兩種剪枝都上**（先 `sort()`，`break` 才合法）：

<!--CODE-->

<!-- 77cce9435ce9 -->
### Template 8: Palindrome Partitioning — LC 131 ⭐⭐⭐⭐

切分題的形狀：`end` 從 `start + 1` 跑到 `n`，用一個合法性判斷當關卡
（`isPalindrome`），再從 `end` 往下遞迴。把判斷式換掉，就變成還原 IP 位址
（LC 93）或斷詞（LC 140）。

<!--CODE-->

<!--CODE-->

<!-- daad7c059aef -->
### Template 9: Grid / Word Search — LC 79 ⭐⭐⭐⭐

標記格子、往四個方向遞迴、再把格子還原。用 `visited[][]` 矩陣的版本，以及
計數／取最大值的變體（LC 980、LC 1219），放在
[backtrack_examples.md §3](./backtrack_examples.md#3-word-search--lc-79-)。

<!--CODE-->

<!-- 6da5f131a07a -->
### Template 10: N-Queens — LC 51

經典回溯，搜尋空間 O(n!)，靠追蹤行與對角線來剪枝。

<!--CODE-->

**關鍵剪枝**：三個 O(1) 的集合取代了 O(n) 的行／對角線掃描。時間：O(n!)，空間：O(n)。

<!-- aab73f0bdc4a -->
### Template 11: Sudoku Solver — LC 37

一格一格回溯；用列／行／宮格集合剪枝。

<!--CODE-->

> 再往前推一步 — 一放下數字就把它從同伴格的候選中消掉，某格候選歸零就立刻宣告失敗 —
> 那就是**限制傳播（constraint propagation）**，在
> [backtrack_advanced.md](./backtrack_advanced.md#template-4-constraint-propagation-early-termination)。

<!-- 1de13297915e -->
### Template 12: Pruning Techniques ⭐⭐⭐⭐

**定義**：透過砍掉不可能通往合法解的分支，來縮小搜尋空間的最佳化手法。

**剪枝的種類**：

**1. 限制條件剪枝**
- 一違反限制就提早結束
- 遞迴呼叫前先檢查合法性

**2. 界限剪枝**
- 用上界／下界排除掉不夠好的路徑
- 分支界限法（branch and bound）

**3. 對稱性剪枝**
- 跳過等價的狀態以避免重複
- 排序輸入來處理排列

**4. 記憶化剪枝**
- 把子問題的結果快取起來
- 避免重算同樣的狀態

**常見剪枝樣式** — 這是大綱不是可執行的程式碼：`current_sum`、`target_length`、
`is_valid` 與 `result` 都是佔位，實際由題目決定。

<!--CODE-->

**實作剪枝 — LC 39 Combination Sum**（先 `sort()` 才能用 `break`）：

<!--CODE-->

<!-- 9cda879f11e0 -->
### Template 13: k-Bucket Partitioning — LC 698 / LC 473

把每個數字分配到 `k` 個桶的其中一個。三個剪枝讓它變得可行：`sum % k != 0` 直接失敗、
由**大到小**排序讓大數字提早爆掉，以及用 `break` 而不是再去試第二個*空*桶
（那只是把同一種分法換個標籤而已）。

<!--CODE-->

> **變體 — LC 473 (Matchsticks to Square)**：*就是同一份 k 桶模板，只是把 `k`
> 寫死成 4*，且 `target = 周長 // 4`。同樣的三個剪枝照用：`sum % 4 != 0` 或
> `max(nums) > target` 就直接失敗、先由**大到小**排序，以及復原之後
> `if buckets[i] == 0: break`（再去試第二個*空*桶只是把同一種分法換個標籤）。

<!-- bbd8ff969510 -->
#### 一成功就立刻把 `true` 往上回傳

判定型問題（「到底*能不能*切分？」）只要有一條分支成功就馬上回傳 —
不要拿到 `true` 之後還繼續跑迴圈：

<!--CODE-->

<!-- 401b0130cabc -->
### Template 14: When to Undo — Mutable vs Immutable State ⭐⭐⭐⭐

<!--CODE-->

**重要提醒：什麼時候「不需要」回溯**

<!--CODE-->

**什麼時候要回溯（復原）**：

| 資料型別 | 需要回溯？ | 原因 |
|-----------|-----------------|--------|
| 基本型別（`int`、`char`、`boolean` 等） | ❌ 不用 | 傳值；每次遞迴呼叫都有自己的副本 |
| 可變物件（`List`、`Set`、`Map`、`StringBuilder` 等） | ✅ 要 | 傳參考；改動會影響到所有遞迴呼叫 |
| 不可變物件（`String`、`Integer` 等） | ❌ 不用 | 改動會產生新的實例 |

**Python 版的對照 — `int` 累加值 vs `list` 路徑（LC 113 Path Sum II）** ⭐

同一條規則在 Python 也成立。在一個**同時**帶著累計總和（`cur_sum`，一個 `int`）
**和**路徑（`cache`，一個 `list`）的 DFS 裡，我們會 `cache.pop()`，但從來不去
「減回」`cur_sum`：

- **`cur_sum`（`int`）— 不用回溯。** 整數是**不可變**的。`cur_sum += root.val`
  並不會就地改掉父層的整數；它是**把本地的 `cur_sum` 重新綁定**到一個全新的
  int 物件。子層的 frame 結束後，父層的 `cur_sum` 完全沒被動到。
- **`cache`（`list`）— 需要回溯。** 整棵遞迴樹從頭到尾只有**一個** list 實例。
  子層的 `append` 父層看得到，所以我們**必須** `pop()` 才能把狀態還原給兄弟分支。

<!--CODE-->

**記憶體走一遍** — 父層在 `cur_sum = 5`、`cache = [5]`，往下走進一個值為 `3` 的子節點：

| | 往下走進子節點 | 回到父層 |
|---|---|---|
| **`cache`（list）** | `cache.append(3)` → `[5, 3]`（同一個物件） | 沒有 `pop()` 就會停在 `[5, 3]` → **父層被汙染 → 一定要回溯** |
| **`cur_sum`（int）** | `cur_sum + 3` → `8`（新的 int，是區域變數） | 子層 frame 被銷毀 → 父層的 `cur_sum` 還是 `5` → **不需要回溯** |

> 另見 [python_trick.md §1-54](https://github.com/yennanliu/CS_basics/blob/master/doc/cheatsheet/python_trick.md) — `str`／`tuple`／`int`（不可變，不用回溯）vs `list.append`（可變，需要 `pop`）。

**為什麼一定要 `cache.pop()`** — 拿一棵三個節點的樹走一遍：

假設樹長這樣：

<!--CODE-->

沒有 `cache.pop()`：

<!--CODE-->

有 `cache.pop()`：

<!--CODE-->

> **另一種寫法（不用明確 pop）：** 每次呼叫都傳一份*新的* list（`path + [node.val]`），這樣每條分支都有自己的副本 — 就不需要 `pop()` 了；[tree_backtrack.md](./tree_backtrack.md) 用的就是這種形式。代價是多了複製成本，換掉的是「共用一個 list 加回溯」。

<!-- ea4fa3974f42 -->
## 總結與速查

<!-- d9536bc99c34 -->
### 決策表 — 該用哪種回溯形狀？ ⭐⭐⭐⭐⭐

| 題型      | 要用 `start_idx` 嗎？ | 範例題 |
|-------------------|------------------|-----------------|
| 子集           | ✅ 要           | Leetcode 78     |
| 組合      | ✅ 要           | Leetcode 77     |
| 組合總和   | ✅ 要           | Leetcode 39     |
| 排列      | ❌ 不用            | Leetcode 46     |
| N 皇后          | ❌ 不用            | Leetcode 51     |
| 切分      | ✅ 要           | Leetcode 131    |

<!-- ec06aad64dbd -->
#### 排序、去重、剪枝 — 什麼時候做

| 問題 | 答案 |
|---|---|
| 要先排序輸入嗎？ | 只要你必須**跳過重複**（LC 40, 47, 90, 996），或想在排序過的候選集上用 `break` 剪枝（LC 39, 216），就要 |
| 怎麼跳過重複？ | 只跳同一層：`i > start && a[i] == a[i-1]`（用索引，LC 40/90）或 `i > 0 && a[i] == a[i-1] && !used[i-1]`（用 visited，LC 47/996）；`Counter` 是第三種寫法 |
| 用 `break` 還是 `continue`？ | 候選集**已排序**且一超出就代表後面整串都沒救時用 `break`（LC 39, 216）；只有這一個候選不合法時用 `continue`（LC 698 桶滿了） |
| 剪枝要放哪？ | 遞迴**之前** — 在做選擇的當下檢查限制，而不是到葉節點才檢查（LC 526, 996） |
| 什麼時候該停止剪枝、改成記憶化？ | 當重複出現的是同一個*狀態*（而不是同一條路徑）時 — 那就是 [DP](./dp.md) 的分界線 |

<!-- 695d0329744a -->
#### 需要 `start_idx` 的題目

組合、子集，以及元素可重複使用的題型：順序不重要，而且前面的選擇不該再被走一次。

| 題目 | 是否用 `start_idx` | 為什麼？ |
|--------|------------------|------|
| `Subsets` (Leetcode 78) | ✅ 要 | 避免產生重複的子集 |
| `Combination Sum` (Leetcode 39) | ✅ 要 | 可以重複用，但要照順序 |
| `Combination Sum II` (Leetcode 40) | ✅ 要 | 不能重複用，還要跳過重複值 |
| `Combinations` (Leetcode 77) | ✅ 要 | 從 n 個裡照順序選 k 個 |
| `Palindrome Partitioning` | ✅ 要 | 從 `start` 開始往後試子字串 |

<!-- 4789ca180590 -->
#### 不用 `start_idx` 的題目

排列型的題目：順序有意義，每一種排法都是不同的答案，而且前面的選擇還會被再次使用。

| 題目 | 是否用 `start_idx` | 為什麼不用？ |
|--------|------------------|---------|
| `Permutations` (Leetcode 46) | ❌ 不用 | 所有排法都合法 |
| `Permutations II` (Leetcode 47) | ❌ 不用 | 只要聰明地跳過重複值就好 |
| `N-Queens` | ❌ 不用 | 每層遞迴處理一列 |
| `Word Break II` | ❌ 不用 | 選擇取決於子字串能不能配到字 |

<!-- 4a0ff1edd6f9 -->
### 終止條件的樣式

三種基底條件的形狀（片段 — 挑一個跟題目的「做完了」條件對得上的）：

<!--CODE-->

<!-- 2f2ea291855c -->
### 分支數與剪枝速查

| 題目 | 分支數 | 深度 | 剪枝 | 最壞情況 |
|---------|----------------|-------|---------|------------|
| 子集 | 2 | n | 無 | O(2^n) |
| 排列 | n, n-1, ... | n | used 集合 | O(n!) |
| 組合 | n-k+1 | k | 起始索引 | O(C(n,k)) |
| N 皇后 | n | n | 3 個集合 | O(n!) → 實務上好很多 |
| 數獨 | 9 | 81 | 列／行／宮格 | O(9^81) → 實務上每盤約 O(1) |

> 最壞情況的量級在上面的 [Time Complexity by Problem Type](#time-complexity-by-problem-type)
> 表格裡；這張表講的是**靠什麼把它剪掉**。

<!-- ee19773d92ef -->
### 面試訊號 → 模式

| 聽到這種話… | 就拿出… |
|--------|---------|
| 「所有可能的組合／排列」 | 標準回溯 + result.append(copy) |
| 「擺 N 個互不攻擊的皇后」 | N 皇后加三個剪枝集合 |
| 「在有限制的格子裡填東西」 | 數獨式解法 + 列／行／宮格集合 |
| 「把字串切成合法的片段」 | 用索引的回溯加上 is_valid 檢查 |
| 「產生合法的括號」 | 把左右括號的計數當作限制 |
| 「太慢了？剪得更狠一點」 | 遞迴前先做限制傳播 |

<!-- 8486fab09056 -->
### 相關主題

- [backtrack_examples.md](./backtrack_examples.md) — 這裡每份模板對應的 LC 實作解
- [backtrack_advanced.md](./backtrack_advanced.md) — 用 Trie 剪枝的格子搜尋（LC 212）、運算式建構（LC 282）、刪除次數預算（LC 301）、記憶化／通用切分
- [tree_backtrack.md](./tree_backtrack.md) — 在明確的樹上做根到葉的路徑回溯
- [dfs.md](./dfs.md) — 沒有復原步驟的 DFS 走訪
- [dp.md](./dp.md) / [knapsack.md](./knapsack.md) — 同一套搜尋的記憶化版本
