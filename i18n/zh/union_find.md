<!-- 185bb52bba52 -->
# Union Find（併查集）

> **範圍** — 併查集（disjoint set union）——**無向圖**的連通性、連通元件計數、環偵測，搭配路徑壓縮與按秩合併。
> **另見**：[union_find_examples.md](./union_find_examples.md) — 支撐這些模式的十七道實戰題；[diff_toposort_quickunion.md](./diff_toposort_quickunion.md) — 併查集 vs 拓撲排序，什麼題該用哪個工具；[topology_sorting.md](./topology_sorting.md) — 有向圖的排序；[graph.md](./graph.md) — 一般的圖論素材。

- 在動態變化的圖上，有效率地判斷節點之間是否連通
- 什麼時候用：動態連通性查詢、環偵測、MST 演算法、把元素分群
- 代表性 LeetCode 題目：Graph Valid Tree、Number of Islands、Accounts Merge、Friend Circles
- 資料結構：parent 陣列，加上做最佳化用的 size／rank 陣列
- 狀態：連通元件、父子關係

**時間複雜度：** 加上最佳化後，每次操作近似 O(1)

<!-- 7ad5d79ad436 -->
## LeetCode 題目清單

- [Union-Find](https://leetcode.com/problem-list/union-find/)
- [Graph Theory](https://leetcode.com/problem-list/graph/)

<!-- 442b0cdc3b57 -->
## 0) 概念

<!-- ef47623f9b64 -->
### 0-0) 併查集的兩種變體 ⭐⭐⭐⭐

<!-- d7eea5a1f396 -->
#### Quick Find vs Quick Union

**Quick Find：**
- **Find**：O(1) — 直接查陣列
- **Union**：O(n) — 要更新整個元件裡的所有元素
- **適用情境**：find 次數遠多於 union 次數時
- **實作方式**：每個元素直接存自己的元件 ID

<!--CODE-->

**Quick Union（加上最佳化）：**
- **Find**：**同時**加上路徑壓縮與按秩／按大小合併，才是攤銷 O(α(n)) ≈ O(1)
- **Union**：攤銷 O(α(n)) ≈ O(1)，條件相同 — 只用其中一個最佳化只能到 O(log n)（見 [0-1 的表](#complexity-by-which-optimizations-you-use-)）
- **適用情境**：通用；find 與 union 次數差不多時
- **實作方式**：存父節點指標，長成一棵樹

**兩者比較：**

| 操作 | Quick Find | Quick Union | Quick Union + 最佳化 |
|-----------|------------|-------------|---------------------------|
| 初始化 | O(n) | O(n) | O(n) |
| Find | O(1) | 最差 O(n) | O(α(n)) ≈ O(1) |
| Union | O(n) | 最差 O(n) | O(α(n)) ≈ O(1) |
| 空間 | O(n) | O(n) | O(n) |
| 最適合 | find 很多 | 兩者均衡 | 通用 |

**什麼時候用 Quick Find：**
- union 操作極少
- 需要即時的 find 查詢
- 資料量小，O(n) 的 union 還能接受

**什麼時候用 Quick Union（最佳化版）：**
- find 與 union 混著來、次數相當
- 資料量大（幾百萬個元素）
- 大多數實務情境（推薦）

<!-- 70b590a8574b -->
### 0-1) 兩個關鍵最佳化 ⭐⭐⭐⭐⭐
併查集能做到近似 O(1)，靠的是兩個關鍵最佳化：

**路徑壓縮**：用在 `find()` 裡
- 讓走過的每個節點直接指向根
- 走訪的同時把樹壓扁
- 遞迴寫法：`parent[x] = find(parent[x])`
- 把之後的查詢攤還成 O(1)

**按秩／按大小合併**：用在 `union()` 裡
- 永遠把小樹掛到大樹的根上
- 讓樹高保持平衡（對數級）
- 避免退化成一條線
- 可以追樹高（rank），也可以追節點數（size）

沒有這兩個最佳化，操作會退化成 O(n)。只用其中一個是 O(log n)；**兩個一起用**才是 O(α(n))，α 是反 Ackermann 函數（任何放得進記憶體的輸入都 ≤ 4，實務上等同常數）。

<!-- 69b2db3d95e6 -->
#### 依使用的最佳化區分複雜度 ⭐⭐⭐⭐

`N` = 元素個數，`M` = `find` / `union` 的呼叫次數。每一列的空間都是 O(N)（`parent`，有用到時再加上 `rank` 或 `size`）。

| 最佳化 | 單次操作最差 | 每次操作攤銷 | `M` 次操作總計（含初始化） |
|---|---|---|---|
| 都沒有 | O(N) | O(N) | O(N + M·N) |
| 只有按秩／按大小合併 | O(log N) | O(log N) | O(N + M log N) |
| 只有路徑壓縮 | O(N) | O(log N) | O(N + M log N) |
| **兩者都有** | O(log N) | **O(α(N))** | **O(N + M·α(N))** |

這張表講出了兩件「近似 O(1)」藏起來的事：

- **攤銷不等於每一次呼叫。** 就算兩個最佳化都用上，單次 `find` 仍可能走 O(log N) 條邊；α(N) 是一整串操作的平均。
- **只有路徑壓縮時，單次呼叫仍是 O(N)。** 對長度 N 的鏈第一次 `find` 會整條走完 — 而*遞迴版* `find` 會遞迴 N 層，在 Python 超過約 1000 個節點就是 `RecursionError`。按秩／按大小合併把樹高壓在 log N，遞迴自然很淺。

大家第一次寫出來的「半最佳化」版本 — 有路徑壓縮，但 `union` 盲目地把一個根掛到另一個根下面：

<!--CODE-->

它幾乎能過所有 LeetCode 題 — 攤銷 O(log N) 已經夠快 — 但面試時它的正確答案是 O(log N)，不是 O(α(N))。要拿到 α(N)，就加上 [0-3 的 Python 模板](#0-3-algorithm-pattern--template-)裡的 `rank`（或 `size`）陣列。

**面試時這樣講：**

> 使用路徑壓縮加上按秩（或按大小）合併時，每次操作是攤銷 O(α(N)) — 反 Ackermann 函數，實務上等同常數。初始化 O(N)，空間 O(N)。只有路徑壓縮時是攤銷 O(log N)，而且單次呼叫仍可能是 O(N)。

<!-- e56a26ad678e -->
### 0-2) 題型分類
- **基本連通性**：判斷兩點是否連通、數連通元件
- **環偵測**：判斷加上這條邊會不會產生環
- **動態 MST**：Kruskal 最小生成樹演算法
- **加權併查集**：處理節點之間的比值／權重（LC 399）
- **格子題**：二維格子的連通性（Number of Islands 系列變形）

<!-- 530ea42b67bb -->
### 0-3) 演算法模式／模板 ⭐⭐⭐⭐⭐

**核心操作：**
- `find(x)`：取得 x 的根，順便做路徑壓縮
- `union(x, y)`：把兩個節點接起來；若本來就連通則回傳 false
- `connected(x, y)`：判斷兩個節點是否在同一個元件

**模板（按大小合併）：**
<!--CODE-->

**另一種模板（按秩合併）：**
<!--CODE-->

**Python 模板（按大小合併——乾淨的 class 寫法）：**
<!--CODE-->

**Python 模板（按秩合併——替代版）：**
<!--CODE-->

> **Python ASCII 追蹤——`find(3)` 過程中的路徑壓縮**，鏈為 `3 → 2 → 1 → 0 (root)`：
>
> ```text
> parent = [0, 0, 1, 2]        # index:  0  1  2  3
>
>   Before find(3):            Recursion unwinds, each frame rewires parent[x] = root:
>
>     0 (root)                   find(3) → find(2) → find(1) → find(0) returns 0
>     |                          ↑ on the way back:
>     1                            parent[1] = 0
>     |                            parent[2] = 0
>     2                            parent[3] = 0
>     |
>     3
>
>   After find(3):             parent = [0, 0, 0, 0]
>
>          0 (root)            # tree flattened: every node now points straight to root 0
>        / | \                 # any later find() on 1/2/3 is O(1)
>       1  2  3
> ```

**Size 與 Rank 的關鍵差異**
- **按大小合併**：追每棵樹實際的節點數
  - 需要知道元件大小時很好用
  - 每次 union 之後都要更新 size
- **按秩合併**：追樹高的近似值（上界）
  - 更省空間（rank 增長很慢）
  - 只有在合併兩棵秩相同的樹時，秩才會加一
  - 有路徑壓縮之後，rank 就不等於真正的樹高了

**邊界情況：**
- 只有單一節點的圖
- 本來就已經連通的節點
- 不合法的索引

<!-- 1a53266406b4 -->
### 0-4) 併查集的五大模式 ⭐⭐⭐⭐

底下每一題都是同一個模板，差別只在對這個問題的答案不同：*「什麼算一個節點，什麼時候把兩個節點合併？」* 難的全在這個問題上；DSU 本身從頭到尾都沒變。

| # | 模式 | 節點是什麼 | 什麼時候合併 | 題解在 |
|---|---|---|---|---|
| 1 | **環偵測** | 一個頂點 | 每條邊都做——`union` 回傳 `false` 的那一刻，*就是*環 | [1) LC 684](./union_find_examples.md#1-redundant-connection--lc-684)、[5) LC 261](./union_find_examples.md#5-graph-valid-tree--lc-261) |
| 2 | **元件計數** | 一個頂點 | 每條邊都做；union 成功就把計數器減一 | [6) LC 323](./union_find_examples.md#6-number-of-connected-components-in-an-undirected-graph--lc-323)、[4) LC 547](./union_find_examples.md#4-number-of-provinces--lc-547) |
| 3 | **多餘的邊** | 一個頂點 | 同上，但要*回傳*第一條 union 失敗的邊 | [1) LC 684](./union_find_examples.md#1-redundant-connection--lc-684)、[2) LC 685](./union_find_examples.md#2-redundant-connection-ii--lc-685--dsu-on-a-directed-graph-) |
| 4 | **二維格子連通性** | 一個格子，壓平成 `row * cols + col` | 只跟右邊與下面的鄰居合併，這樣每一對只會被考慮一次 | [9) LC 200](./union_find_examples.md#9-number-of-islands--lc-200--grid--1d-via-row--cols--col)、[10) LC 130](./union_find_examples.md#10-surrounded-regions--lc-130--a-virtual-border-node) |
| 5 | **加權併查集** | 一個變數 | 邊上帶著到父節點的*比值*，路徑壓縮時一路相乘 | [12) LC 399](./union_find_examples.md#12-evaluate-division--lc-399--weighted-union-find-with-ratios-) |

另外兩種結構性變形，改的是*什麼時候跑*演算法，而不是合併什麼：

- **排序邊（Kruskal 風格）** — 由便宜到貴依序處理邊，一旦你在意的那兩個端點連上就停。[13) LC 1631](./union_find_examples.md#13-path-with-minimum-effort--lc-1631--sorted-edge-kruskal-style)。
- **離線倒放** — 併查集不能*拆開*，所以當題目在移除東西時，就把過程倒過來重播、改成一個個加回去。[14) LC 803](./union_find_examples.md#14-bricks-falling-when-hit--lc-803--offline-reverse-union-find-)。

<!-- 9d7579d7fac6 -->
## 1) 範例題目與程式碼索引

<!-- 6e46acf4aa1a -->
### 基本連通性與元件計數
- **LC 200** – Number of Islands：數二維格子上的連通元件
  - Java：`leetcode_java/src/main/java/LeetCodeJava/DFS/NumberOfIslands.java:493`
  - 模式：格子壓成一維（`row * cols + col`），併查集配四方向檢查

- **LC 261** – Graph Valid Tree：檢查 n-1 條邊是否剛好形成一個元件
  - Java：`leetcode_java/src/main/java/LeetCodeJava/BFS/GraphValidTree.java:36`
  - 模式：環偵測，加上「邊數必須剛好是 n-1」的驗證

- **LC 323** – Number of Connected Components：基本的元件計數
  - Java：`leetcode_java/src/main/java/LeetCodeJava/Graph/NumberOfConnectedComponentsUndirectedGraph.java:49`
  - 模式：追蹤元件數，union 成功就減一

<!-- 9c81299992f9 -->
### 環偵測與多餘邊
- **LC 684** – Redundant Connection：找出在樹上造成環的那條邊
  - Java：`leetcode_java/src/main/java/LeetCodeJava/Tree/RedundantConnection.java:50`
  - 模式：回傳第一條讓 `union()` 失敗的邊（偵測到環）— 這條邊同時也是環上在輸入中最後出現的邊，正是題目要求的平手規則（[原因](./union_find_examples.md#1-redundant-connection--lc-684)）

<!-- e3ad86acfdb8 -->
### 加權併查集
- **LC 399** – Evaluate Division：用帶比值的加權併查集解方程式
  - Java：`leetcode_java/src/main/java/LeetCodeJava/DFS/EvaluateDivision.java:421`
  - 模式：存比值，路徑壓縮時把比值一路相乘

<!-- e8f9a91289c6 -->
### 進階應用
- **LC 130** – Surrounded Regions：用一個虛擬節點串起所有邊界區域
- **LC 547** – Friend Circles：在朋友關係矩陣裡找分群
- **LC 721** – Accounts Merge：依共用的 email 把帳號分群
- **LC 886** – Possible Bipartition：偵測二分圖的衝突
- **LC 1135** – Connecting Cities：用 Kruskal 演算法求 MST
- **LC 1319** – Network Connections：把所有節點連起來所需的最少操作數
- **LC 2316** – Count Unreachable Pairs of Nodes：元件大小 + 邊掃邊算剩餘量的跨元件配對數（見 §2-13）

<!-- 0951b419d419 -->
### 排序邊／離線併查集
- **LC 1631** – Path With Minimum Effort：Kruskal 掃描，最小化最大邊（見 §2-14）
- **LC 778** – Swim in Rising Water：同一種掃描，只是權重掛在格子上（見 §2-14）
- **LC 1697** – Checking Existence of Edge Length Limited Paths：查詢先依 limit 排序的離線做法（見 §2-14）
- **LC 803** – Bricks Falling When Hit：離線**倒放** DSU + 虛擬屋頂節點（見 §2-15）

<!-- 7cc38308e99d -->
### 帶大小資訊與有向圖的變形
- **LC 827** – Making A Large Island：維護大小 + 翻轉時只算相異的鄰居根（見 §2-16）
- **LC 685** – Redundant Connection II：有向圖，兩個候選逐一排除（見 §2-17）
- **LC 1971** – Find if Path Exists in Graph：最基本的「把所有邊 union 起來，再做一次連通性查詢」

<!-- 3ea6ae9e3d9c -->
## 2) 圖解

<!-- 7e9c31ea9c40 -->
### 基本 union 操作
<!--CODE-->

<!-- 6bd8898633e8 -->
### 路徑壓縮圖解
<!--CODE-->

<!-- 7ff69138400e -->
### 按秩合併範例
<!--CODE-->

<!-- 47ee26f0f5a5 -->
### 路徑壓縮實際運作
<!--CODE-->

<!-- 63e4641c8cfb -->
## 3) 技巧與陷阱 ⭐⭐⭐⭐

**常見錯誤：**
1. **忘記做路徑壓縮**：時間會變成 O(n)，而不是近似 O(1)
<!--CODE-->

2. **沒有維護元件數**：union 裡漏掉減一
<!--CODE-->

3. **索引搞混**：0-based 跟 1-based 混用
4. **環偵測的時機**：在 union 之後才檢查，而不是之前
5. **更新錯父節點**：union 時更新了節點本身，而不是它的根
<!--CODE-->

6. **把 rank 跟 size 搞混**：
   - Rank = 樹高的近似值（只有在合併兩棵秩相同的樹時才增加）
   - Size = 實際節點數（每次都加上被併進來的那一份）

**怎麼最佳化：**
- **find 裡一定要做路徑壓縮**
- **按大小／按秩合併**，讓樹保持平衡
- **維護元件數**，查詢時可以直接回答
- **用迭代版 find**，省掉遞迴開銷

**空間與時間的取捨：**
- 陽春版 UF：O(n) 空間，每次操作 O(n) 時間
- 加上最佳化：O(n) 空間，每次操作 O(α(n)) ≈ O(1) 時間
- α(n) 是反 Ackermann 函數，在實務輸入規模下等同常數

**關鍵模式：**
1. **環偵測**：`if (find(x) == find(y)) return false; // cycle`
2. **元件計數**：union 成功時把計數減一
3. **二維格子壓成一維**：座標轉換用 `row * cols + col`
4. **虛擬節點**：把邊界元素接到一個虛擬節點上，處理起來更簡單
5. **加權關係**：對方程式類的題目，在邊上存比值／距離

**什麼時候「不要」用併查集：**
- 靜態的圖，DFS/BFS 就夠了
- **本來就已經是一棵樹的 `parent[]` 陣列**（例如 `parent[0] = -1`，LC 4015 Weighted Sum of a Tree）。它*看起來*像 DSU 陣列，但沒有 `union()` 就沒有東西要合併——直接記憶化 `depth[x] = depth[parent[x]] + 1` 然後往上爬就好。走法跟帶路徑壓縮的 `find()` 一樣，但完全不用那些簿記。見 [dfs_advanced.md → Template 11](./dfs_advanced.md#template-11-parent-array-tree--memoized-upward-depth--lc-4015)。
- 要求最短路徑（改用 Dijkstra／Floyd-Warshall）
- 有向圖的強連通元件（改用 Tarjan）
- 圖很小，直接查鄰接關係就夠了

<!-- 3f7ed95e50fd -->
## 實戰題解

十七道題收在 **[union_find_examples.md](./union_find_examples.md)**，是依「節點代表什麼」分組，而不是依題號：

| 分組 | 題目 |
|---|---|
| [環偵測與多餘邊](./union_find_examples.md#cycle-detection--redundancy) | LC 684, 685, 990 |
| [元件計數與連通性](./union_find_examples.md#component-counting--connectivity) | LC 547, 261, 323, 1319, 2316 |
| [格子題](./union_find_examples.md#grids) | LC 200, 130, 827 |
| [加權、排序邊與離線變形](./union_find_examples.md#weighted-sorted-edge--offline-variants) | LC 399, 1631, 803 |
| [併查集用在其他結構上](./union_find_examples.md#union-find-on-other-structures) | LC 721, 1202, 947 |
