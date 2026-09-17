<!-- 14907d38568d -->
# 前綴和 — 進階模板

> **範圍** — 六個需要借用其他結構或其他恆等式的前綴和模板：補集技巧、應付含負數陣列的單調雙端佇列、二維的列對壓縮、前綴 XOR、用雜湊表實作的稀疏差分陣列，以及樹上前綴和的計數表。
> **另見**：[prefix_sum.md](./prefix_sum.md) — 母篇：模板 1–8、概念與模板選擇策略；[prefix_sum_examples.md](./prefix_sum_examples.md) — 實作過的例題；[monotonic_queue.md](./monotonic_queue.md) — 模板 10 背後的雙端佇列；[difference_array.md](./difference_array.md) — 模板 13 的稠密版本；[bit_manipulation.md](./bit_manipulation.md) — 為什麼 XOR 跟加法一樣支援同一個相減恆等式；[matrix.md](./matrix.md) — 模板 11 壓掉的那層二維幾何；[tree_backtrack.md](./tree_backtrack.md) — 模板 14 所一般化的 root→leaf 路徑模板，以及它需要的「回溯時復原」習慣。

<!-- ea216edf7cf4 -->
## LeetCode 題目清單

- [Prefix Sum](https://leetcode.com/problem-list/prefix-sum/)
- [Matrix](https://leetcode.com/problem-list/matrix/)

<!-- d449ca0a1119 -->
## 總覽

[prefix_sum.md](./prefix_sum.md) 裡的模板 1–8 講的都是同一招：把陣列建出來，然後相減兩項。
下面這六個，就是那一招不夠用的時候。

<!-- f686f4cc9c99 -->
### 關鍵性質
- **複雜度**：各模板分別標示；每一個的重點都是把 O(n²) 或 O(n·m²) 的掃描壓成 O(n) 或 O(n·m)
- **核心想法**：前綴和恆等式 `sum(l, r) = P[r+1] - P[l]` 對任何**可逆**的合併運算都成立 — 這就是 XOR 可行、而 min/max 不行的原因
- **什麼時候用**：當你一眼就想到前綴和，但題目有東西把它弄壞了 — 負數、二維、環狀繞回、座標大到開不出陣列，或者那個「陣列」其實是樹上一條 root→node 的鏈

<!-- d4930c79488e -->
### 模板 9：補集技巧 —「總和 − 中間視窗」⭐⭐⭐⭐⭐ — LC 1423

**核心想法：** 當元素是**從兩端**拿走時，**剩下沒拿的**永遠是一段連續的中間子陣列。所以不要去枚舉 `(leftTake, rightTake)` 的組合，把問題反過來看：

<!--CODE-->

這樣一來，「從兩端拿」的題目就變成單純的**前綴和上的固定長度視窗**問題 — 這也是它歸在這裡、而不是歸在雙指標那份文件的原因。

<!--CODE-->

<!--CODE-->

<!-- 519672d71f3f -->
#### 變形 — **視窗長度不固定**（LC 1658）

一樣是補集，但這裡是從兩端一直拿到總和等於 `x`，所以中間視窗的**和是固定的**（`total - x`），而**長度可變**、且我們要**最大化**它。因為 `nums[i] >= 1`，前綴和嚴格遞增 → 用收縮視窗就可以。

<!--CODE-->

<!--CODE-->

> 如果值可能是**負的**，收縮迴圈就會壞掉 — 退回模板 2（`{prefix_sum: first_index}`）去找和為 `target` 的最長子陣列。

<!-- 3bfa7636ed6d -->
### 模板 10：前綴和 + 單調雙端佇列（最短子陣列，允許**負數**）⭐⭐⭐⭐⭐ — LC 862

**核心想法：**「和 ≥ K 的最短子陣列」只有在**所有值都非負**時才能直接用滑動視窗（LC 209）。一旦有負數，前綴和不再單調，視窗也就不能安全地收縮。解法：維護一個**遞增的前綴和索引單調雙端佇列**。

<!--CODE-->

**在 `nums = [2, -1, 2], K = 3`（`p = [0, 2, 1, 3]`）上的追蹤：**

<!--CODE-->

<!--CODE-->

<!--CODE-->

| 題目 | LC # | 值域 | 該用的工具 |
|---------|------|--------|------------|
| Minimum Size Subarray Sum | 209 | 全正 | 單純滑動視窗（見 `sliding_window.md`） |
| Shortest Subarray with Sum at Least K | 862 | 可能有負 | **前綴和 + 單調雙端佇列** |
| Subarray Sum Equals K | 560 | 任意 | 模板 2（雜湊表，精確和） |

<!-- c51dfc40e185 -->
### 模板 11：列對壓縮 — 把二維壓成一維前綴和 ⭐⭐⭐⭐ — LC 363

**核心想法：** 每個子矩陣都由一組**列對** `(top, bottom)` 加上一段行區間決定。固定列對，把這兩列之間每一行的和壓成一維陣列 `colSum`，二維問題就變成對應的**一維子陣列問題** — 那個你早就會解了。

<!--CODE-->

以 **LC 363（最大矩形和 ≤ k）** 來說，一維子問題是「和 ≤ k 的最大子陣列」：
`run - prefix_j <= k`  ⟹  `prefix_j >= run - k`  ⟹  在有序集合中查**大於等於 run − k 的最小前綴和**（`ceiling` / `bisect_left`）。

<!--CODE-->

<!--CODE-->

> **換掉一維解法，就變成另一題。** 外層雙迴圈完全相同，只換內層那段：
> - **LC 1074**（統計和為 target 的子矩陣個數）→ 內層解法 = 模板 2 的雜湊表。
> - **LC 363**（最大和 ≤ k）→ 內層解法 = 有序集合 + `ceiling`，如上。
> - 如果 `n < m`，先轉置，讓平方那一項落在比較小的維度上。

<!-- adb3c5130b03 -->
### 模板 12：前綴 XOR ⭐⭐⭐⭐ — LC 1310

**核心想法：** XOR 是自己的反運算（`a ^ a = 0`），這正是減法對加法所做的事。所以整套前綴和工具只要把 `+`／`-` 換成 `^` 就能直接搬過來：

| | 和 | XOR |
|---|-----|-----|
| 建表 | `p[i+1] = p[i] + a[i]` | `p[i+1] = p[i] ^ a[i]` |
| 區間 `[l, r]` | `p[r+1] - p[l]` | `p[r+1] ^ p[l]` |
| 哨兵 | `p[0] = 0` | `p[0] = 0` |

<!--CODE-->

<!--CODE-->

<!-- 3c81f5b78c30 -->
#### 變形 — **把 XOR 位元遮罩當成奇偶指紋**（LC 1915）

轉折在於：不是對**數值**做 XOR，而是對一個**每個字母佔一個 bit 的遮罩**做 XOR，讓遮罩的第 `c` 個 bit 代表「到目前為止字母 `c` 出現了奇數次」。於是子字串 `(j, i]` 中每個字母都出現偶數次，等價於 `mask[i] == mask[j]` — 就是模板 2 的雜湊表查詢，只是查的是遮罩而不是和。

<!--CODE-->

<!--CODE-->

> **同一副骨架的其他口味：** LC 1738（Find Kth Largest XOR Coordinate Value）是模板 5 的排容原理，把 `+`／`-` 換成 `^`；LC 1829（Maximum XOR for Each Query）則是一路把元素剝掉的後綴 XOR。

<!-- fea18e21c074 -->
### 模板 13：用雜湊表做稀疏差分陣列（掃描線）⭐⭐⭐⭐⭐ — LC 2021

**核心想法**：就是模板 4 的差分陣列，只是座標空間**大到（或負到）開不成陣列**。把陣列換成雜湊表，然後走訪 `sorted(keys)` 而不是 `range(n)`。

**什麼時候該用**：
- 座標很大（`-10^8 <= pos <= 10^8`）→ 開一個 `2 * 10^8` 大小的陣列會爆記憶體
- 座標可能是**負的** → 陣列索引得先做偏移
- 真正有意義的位置只有 `O(n)` 個 — 兩個相鄰事件之間的值不會變，所以**只有事件點可能是答案**

| | 陣列差分（模板 4） | 雜湊表差分（模板 13） |
|---|---|---|
| 儲存 | `[0] * (maxCoord + 2)` | `defaultdict(int)`，只有 2n 個 key |
| 空間 | O(座標範圍) | **O(n)** |
| 走訪 | `for i in range(n)` | `for k in sorted(d)` |
| 時間 | O(range + n) | **O(n log n)**（排序） |
| 負座標 | 需要偏移 | **直接可用** |

**`+1` 這個小技巧**：區間 `[p-r, p+r]` 是**閉區間**，所以「結束」標記要放在 `p + r + 1`，不是 `p + r`。這裡的差一錯誤是這個模式的頭號 bug。

<!--CODE-->

<!--CODE-->

<!-- 5e6d0662dde3 -->
#### 兩個值得背起來的細節

1. **為什麼 `sorted()` 會給出最小的答案** — 前綴和只有在事件由左往右套用時才正確。再配上**嚴格**的 `>`（不是 `>=`），第一個達到新最大值的位置會被記下來，之後同分的也不會覆蓋它。LC 2021 明確要求「最小的那一個」。
2. **為什麼雜湊表比排序過的事件清單好用** — 用 key 分桶，代表**同一個**座標上的所有 `+1`／`-1` 在掃描看到它們之前就已經合併好了。改用 `List<int[]>` 的話，你還得煩惱同一座標內部的排序先後；用 map 這問題根本不存在。

<!-- 8d134658c77f -->
#### 同一個模式的其他題目

| 題目 | LC # | 事件是什麼 | 備註 |
|---------|------|---------------------|------|
| Brightest Position on Street | 2021 | 路燈 `[p-r, p+r]` | 閉區間 → `+r+1` |
| Meeting Rooms II | 253 | 開始 `+1`、結束 `-1` | 結束是**開區間** → 不用 `+1` |
| Car Pooling | 1094 | 上車 `+num`、下車 `-num` | 範圍固定且小 → 用陣列就好 |
| Corporate Flight Bookings | 1109 | 在 `[first, last]` 上 `+seats` | 閉區間 → `last+1` |
| Maximum Population Year | 1854 | 出生 `+1`、死亡 `-1` | 死亡年份是開區間 |
| My Calendar III | 732 | 預約區間 | 需要即時的 TreeMap（線上處理） |
| Describe the Painting | 1943 | 有顏色的線段 | map 的值 = 顏色總和 |

> **經驗法則**：座標範圍 ≤ 約 10^6 且非負 → 用一般陣列（模板 4）。否則，或座標為負 → 用 HashMap／TreeMap（模板 13）。

<!-- 032cc8f5bd22 -->
### 模板 14：樹上的前綴和（DFS + HashMap + 回溯）⭐⭐⭐⭐⭐ — LC 437

**關鍵想法**：樹上一條**往下走的路徑**，其實就是*那條 root→node 鏈的一段子陣列*。所以
模板 2（在 HashMap 裡找 `cur - k`）原封不動就能用 —— 「那個陣列」只是換成了 DFS 的呼叫堆疊，
而不是 `nums`。唯一多出來的動作是：這條鏈是**一根分支，不是某個全域陣列的前綴**，
所以遞迴離開節點時，必須把 map 裡的那筆**復原**。

| | 陣列（模板 2） | 樹（模板 14） |
|---|---|---|
| 「那個陣列」 | `nums[0..i]` | root→node 的鏈 ＝ 目前的 DFS 堆疊 |
| 累加值 | `cur += nums[i]` | `cur += node.val` |
| 數以此結尾的路徑 | `cnt += map[cur - k]` | 一樣 |
| 記下這個前綴 | `map[cur] += 1` | 一樣 |
| **把它取消記錄** | 永遠不用 —— 陣列只會變長 | **兩個子節點都走完後 `map[cur] -= 1`** |
| 結果 | 和為 `k` 的子陣列個數 | 和為 `k` 的往下路徑個數 |

**什麼時候該拿它出來** —— 三個條件都要成立：

- 路徑必須**只能往下**（父 → 子），這樣每個候選路徑才是 `chain[i..j]`；
- 路徑**起點與終點都可以是任意節點** —— 不綁在 root 或 leaf 上（這正是
  [tree_backtrack.md](./tree_backtrack.md) 那種單純 root→leaf DFS 解不了的原因）；
- 你要做的是**計數**（或判斷存在性）某個目標和的路徑，而不是在一條可能*轉彎*的路徑上取最大值 ——
  見下面的 [什麼時候不要用它](#when-not-to-use-it--the-path-bends-or-the-answer-is-per-subtree)。

<!-- f203d66bbb35 -->
#### 為什麼那個復原是必須的

少了它，**兄弟**子樹留下來的前綴還在 map 裡，於是一條橫著跳過樹的「路徑」就會被算進去。
最小的失敗案例，`targetSum = 1`：

<!--CODE-->

在離開節點時做 `map[cur] -= 1`，就會在進入右分支之前把前綴 `5` 丟掉，計數維持在 1。
這樣 map 裡**非零**的項目，剛好就是當前節點所有祖先的前綴 —— 最多 `h` 筆，
而這就是 `O(h)` 空間的由來。

> **`-= 1` 不等於 `del`。**被減過的 key 會以值 `0` 留在 dict 裡，所以跑久了 map 可能累積到
> `O(n)` 個死 key。這對正確性無害 —— 計數 `0` 對 `cnt` 沒有貢獻 —— 但如果你要那個 `O(h)`
> 是字面上的成立，就在計數歸零時 `del prefix[cur]`（Java：`prefix.remove(cur)`）。

> **Python 特有的細節**：那個 map 是每一層 frame 共用的可變物件，所以那個復原是唯一在
> 限制它作用範圍的東西。單純的 `cur`（一個 `int`）不需要復原 —— 它是以值的形式一路傳下去的。

<!-- 9c20384e49ad -->
#### Python 模板

<!--CODE-->

<!-- ef980063755f -->
#### Java 模板

<!--CODE-->

> **Java 的累加值請用 `long`。**LC 437 允許 `-10^9 <= node.val <= 10^9`，而節點可以到 1000 個，
> 所以用 `int` 累加鏈上的和會溢位。那麼 *key* 也必須是 `Long`，否則查表會靜悄悄地找不到。

<!-- 46d3d8b3c7d4 -->
#### 基本情況的兩種寫法 —— 選一種，絕對不要兩種都寫

| | 哨兵（推薦） | 明確判斷 |
|---|---|---|
| 初始化 | `prefix = {0: 1}` | `prefix = {}` |
| 計數 | `cnt += prefix[cur - k]` | `if cur == k: cnt += 1`<br>`cnt += prefix.get(cur - k, 0)` |
| 怎麼處理「路徑從 root 起算」 | 那筆 `0` 就處理掉了 | 靠那個 `if` |
| 風險 | 沒有 | **兩種都寫**，每條從 root 起算的路徑都會被重複計算 |

這個哨兵和陣列模板裡的 `prefix[0] = 0` 是同一個想法：它代表*空*前綴，
也正是它讓「從 root 開始的路徑」不需要任何特例。

<!-- c51dc2987669 -->
#### 步驟追蹤 — LC 437，最左邊那條鏈

<!--CODE-->

<!-- 0fcbef71df6f -->
#### 複雜度，以及它取代了什麼

| 做法 | 時間 | 空間 | 備註 |
|---|---|---|---|
| 從**每個**節點都做一次 DFS（`pathSum(root) = dfs(root) + pathSum(left) + pathSum(right)`） | 最壞 O(n²)，平衡時 O(n log n) | O(h) | 最直覺的第一個答案；先講出來，再改進 |
| BFS 走每個節點 + 各自做 DFS | O(n²) | O(n) | 同樣的工作量，但多花空間 |
| **模板 14** | **O(n)** —— 每個節點只走一次 | **O(h)** —— map 只裝祖先 | 把那個 `O(h)` 講出來；面試官在這題期待的是 `O(n)` |

<!-- a23d81eb51f9 -->
#### 同一套骨架，其他題目

| 題目 | LC # | 變的是什麼 | 模板 |
|---------|------|--------------|----------|
| Path Sum III | 437 | — 最標準的形式 | 模板 14 |
| Subarray Sum Equals K | 560 | 陣列版；樹只多了那個復原 | 模板 2 |
| Number of Submatrices That Sum to Target | 1074 | 同一個計數表，「鏈」換成固定的一對列 | 模板 11 + 2 |
| Path Sum | 112 | 路徑釘在 root→leaf → **不需要 map**，只要帶著 `cur` | 模板 14 退化版 |
| Path Sum II | 113 | root→leaf **並且**要收集路徑 → 帶一個 list，並且要復原它 | 模板 14 退化版 + 回溯 |
| Binary Tree Paths | 257 | 一樣，只是收字串而不是和 | — |
| Sum Root to Leaf Numbers | 129 | `cur = cur * 10 + val` —— 累加的是一個*數字*，但一樣是往下的前綴 | — |
| Sum of Root To Leaf Binary Numbers | 1022 | `cur = cur * 2 + val` | — |
| Path Sum IV | 666 | 樹是用 `depth-position-value` 三元組給的；先重建父子關係，再套同樣的鏈上求和 | — |
| Sum of Nodes with Even-Valued Grandparent | 1315 | 往下帶的是最近**兩個**祖先，而不是一個累加和 | — |

**把合併運算換掉**，只要它是*可逆*的，恆等式一樣成立
（見上面的 [關鍵性質](#key-properties)）：

- **XOR** —— 數往下路徑中 XOR 等於 `k` 的個數：`cur ^= node.val`，查 `cur ^ k`
  （[模板 12](#template-12-prefix-xor---lc-1310)）；
- **取模** —— 數往下路徑中和可被 `k` 整除的個數：以 `cur % k` 當 key
  （[模板 3](./prefix_sum.md#template-3-modulo-prefix-sum-divisibility-problems--lc-974)）；
- **min / max** —— **不行**。`min` 沒有反運算，所以「前綴 min」沒辦法再被減回去；
  那類題目要改用後序遍歷。

<!-- d9461bdddf8d -->
#### 什麼時候不要用它 —— 路徑會轉彎，或答案是以子樹為單位

如果路徑可以在某個節點轉彎（`left → node → right`），或者要求的量是某棵*子樹*的性質，
那麼「鏈」這個框架就是錯的，答案是**後序 DFS 把值往上回傳**：

| 題目 | LC # | 為什麼不能用前綴和 |
|---------|------|--------------------|
| Binary Tree Maximum Path Sum | 124 | 路徑會轉彎；要在每個節點用 `left + val + right` 取最大 |
| Diameter of Binary Tree | 543 | 會轉彎；`leftDepth + rightDepth` |
| Longest Univalue Path | 687 | 會轉彎 |
| Most Frequent Subtree Sum | 508 | 是對**子樹**和做的 HashMap，由下往上算 —— 不是鏈上的前綴 |
| Count Nodes Equal to Average of Subtree | 2265 | 需要每棵子樹往上回傳 `(sum, count)` |

> **面試中的提示語**：「不必從 root 或 leaf 開始或結束，但必須**往下走**」
> → 模板 14。「路徑可以經過某個節點」／「任意兩個節點之間」→ 後序遍歷
> （或 LCA，見 [tree_lca_distance.md](./tree_lca_distance.md)）。

<!-- 73248657484d -->
### 模板 9-14 — 題目索引

| 題目 | LC # | 關鍵技巧 | 難度 | 模板 |
|---------|------|---------------|------------|----------|
| Maximum Points You Can Obtain from Cards | 1423 | 總和 − 最小固定視窗 | Medium | 模板 9 |
| Minimum Operations to Reduce X to Zero | 1658 | 總和 − 和為 total−x 的最長視窗 | Medium | 模板 9 變形 |
| Shortest Subarray with Sum at Least K | 862 | 前綴和 + 單調雙端佇列 | Hard | 模板 10 |
| Minimum Size Subarray Sum | 209 | 全正 → 單純滑動視窗 | Medium | 模板 10（對照組） |
| Max Sum of Rectangle No Larger Than K | 363 | 列對壓縮 + 有序集合 | Hard | 模板 11 |
| Number of Submatrices That Sum to Target | 1074 | 列對壓縮 + 雜湊表 | Hard | 模板 11 + 2 |
| XOR Queries of a Subarray | 1310 | 前綴 XOR 區間查詢 | Medium | 模板 12 |
| Number of Wonderful Substrings | 1915 | 前綴 XOR 位元遮罩奇偶 + 計數 | Medium | 模板 12 變形 |
| Find Kth Largest XOR Coordinate Value | 1738 | 二維前綴 XOR（排容原理） | Medium | 模板 12 + 5 |
| Number of Sub-arrays With Odd Sum | 1524 | 前綴奇偶計數（模板 3 取 k = 2） | Medium | 模板 3 變形 |
| Max Consecutive Ones III | 1004 | 0/1 轉換，最多 k 個 0 的最長視窗 | Medium | 模板 6／滑動視窗 |
| Number of Good Ways to Split a String | 1525 | 前綴相異數 vs 後綴相異數 | Medium | 模板 1（前綴 + 後綴） |
| Minimum Number of Operations to Move All Balls to Each Box | 1769 | 左→右與右→左的累計（數量、成本）掃描 | Medium | 模板 7 變形 |
| Plates Between Candles | 2055 | 前綴盤子數 + 最近蠟燭索引陣列 | Medium | 模板 1（離線查詢） |
| Find Good Days to Rob the Bank | 2100 | 前綴非遞增／後綴非遞減的連續長度 | Medium | 模板 1 變形 |
| Product of the Last K Numbers | 1352 | 前綴**乘積**（遇到 0 就重設清單） | Medium | 模板 1 變形 |
| Brightest Position on Street | 2021 | 雜湊表差分陣列 + 依 key 排序掃描 | Medium | 模板 13 |
| Describe the Painting | 1943 | 雜湊表差分陣列，值 = 顏色總和 | Medium | 模板 13 |
| My Calendar III | 732 | TreeMap 差分陣列，線上求最大重疊 | Hard | 模板 13 |
| Path Sum III | 437 | 在 root→node 的鏈上做前綴和 + 回溯 | Medium | 模板 14 |
| Path Sum | 112 | root→leaf 的鏈上求和，不需要 map | Easy | 模板 14（退化版） |
| Path Sum II | 113 | root→leaf 的鏈 + 路徑回溯 | Medium | 模板 14（退化版） |
| Sum Root to Leaf Numbers | 129 | 沿著鏈往下累一個*數字*（`cur*10 + val`） | Medium | 模板 14（退化版） |
| Path Sum IV | 666 | 從 `depth-pos-val` 重建樹，再做鏈上求和 | Medium | 模板 14（退化版） |

> **交叉參考：** 精確和的雜湊表補集寫法（`prefix_sum - k`）在 [`n_sum.md`](./n_sum.md) 裡也被寫成「在前綴和上做 2-sum」的模板 — 本文一律使用上面模板 2 的版本。模板 14 就是同一個補集技巧沿著 DFS 堆疊跑；它在樹那一側的視角寫在 [`tree.md`](./tree.md) 與 [`binary_tree.md`](./binary_tree.md)。
