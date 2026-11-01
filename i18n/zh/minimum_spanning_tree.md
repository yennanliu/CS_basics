<!-- b6587628afc8 -->
# Minimum Spanning Tree（最小生成樹，MST）

> **範圍** — 無向加權圖上的最小生成樹：切割性質、Kruskal（排序好的邊 + 併查集）、Prim（heap 或稠密陣列），以及建立在它們之上的題型 — 完全幾何圖、虛擬源點、關鍵邊與瓶頸樹。
> **另見**：[union_find.md](./union_find.md) — Kruskal 底下跑的併查集；[heap.md](./heap.md) — Prim 底下的優先佇列；[shortest_path_comparison.md](./shortest_path_comparison.md#c-prim-vs-dijkstra--one-term-apart--lc-1584) — Prim 和 Dijkstra 只差一項；[graph.md](./graph.md) — 圖的表示法、走訪，以及把題目分流到這裡的圖論題目目錄。

- **核心想法**：用剛好 `n - 1` 條邊、最小的總權重，把**全部** `n` 個節點連起來 — 貪心地拿「不會形成環的最便宜那條邊」
- **什麼時候用**：「把每個點／城市／房子都連起來」、「讓所有點連通的最小成本」、「任兩點之間恰好只有一條簡單路徑」 — 要的是最便宜的*樹*，不是最便宜的*路徑*
- **代表性 LeetCode 題目**：LC 1584、LC 1135、LC 1168、LC 1489、LC 3600
- **資料結構**：併查集 + 排序好的邊清單（Kruskal），或 min-heap + visited 集合（Prim）
- **典型狀態**：Kruskal — `parent[]` 與已選的邊數；Prim — `in_tree`，以及每個樹外節點目前已知最便宜的那條連入邊

**時間複雜度：** Kruskal `O(E log E)`（排序）；Prim 用 heap 是 `O(E log V)`；Prim 用一般陣列是 `O(V^2)`
**空間複雜度：** 圖本身 `O(V + E)`，併查集或 visited 集合 `O(V)`

<!-- 8b92803fa382 -->
## LeetCode 題目清單

- [Minimum Spanning Tree](https://leetcode.com/problem-list/minimum-spanning-tree/)
- [Union Find](https://leetcode.com/problem-list/union-find/)
- [Graph](https://leetcode.com/problem-list/graph/)

<!-- 442b0cdc3b57 -->
## 0) 概念

<!-- e73908f37b7f -->
### 0-0) 核心原理

一張有 `n` 個節點的連通無向圖，它的**生成樹**是邊的一個子集：能走到每個節點、而且**沒有環**
— 所以它永遠有**剛好 `n - 1` 條邊**，任兩個節點之間也恰好只有一條簡單路徑。**最小**生成樹
就是所有生成樹中，邊權總和最小的那一棵。

**為什麼貪心是對的 — 兩個性質：**

- **切割性質（cut property）**：把節點任意分成兩群，橫跨這個切割最便宜的那條邊，一定屬於*某一棵*
  MST。Prim 就是反覆套用這個性質（切割是「已在樹裡」vs「還沒進來」）。
- **環性質（cycle property）**：任何環上最貴的那條邊都用不到。Kruskal 就是反覆套用這個性質
  （一條兩端早已連通的邊會形成環，而且它是那個環上最貴的一條，因為之前選的每條邊都比它便宜）。

**關鍵洞見：**
- MST 最小化的是樹上邊的**總和**。它**不會**最小化任兩個特定節點之間的距離 — 那是最短路徑
  （Dijkstra），完全是另一個問題。
- MST 同時也會最小化樹上**最大**的那條邊（它是一棵*最小瓶頸*生成樹），所以「最小化最大邊」
  這類題目，其實都是喬裝過的 Kruskal 掃描。
- 圖不連通就沒有生成樹：Kruskal 結束時選不到 `n - 1` 條邊，Prim 結束時拜訪不到 `n` 個節點。
  題目有要求時就回傳 `-1`。

<!-- 65ca83f536cf -->
### 0-1) 題型分類

1. **經典的加權邊清單** — 邊直接給你；原封不動跑 Kruskal（LC 1135）
2. **完全幾何圖** — 每一對點都是一條邊，權重是某種距離（LC 1584）
3. **虛擬源點** — 「在某個節點蓋一座站，*或*把它接到某座站」：加一個節點 `0`，它連到節點 `i` 的邊權就是蓋站的價錢（LC 1168）
4. **邊的分類** — 哪些邊在每一棵 MST 裡／在某些 MST 裡：排除或強制選某一條邊後重建 MST（LC 1489）
5. **瓶頸樹** — 最大化生成樹裡最弱的邊／最小化最強的邊：排序後逐條合併，或二分搜尋 + 併查集檢查可行性（LC 3600，參見 LC 1631）

<!-- 93a331b3a12a -->
### 0-2) Kruskal 還是 Prim？ ⭐⭐⭐⭐

| 你拿到的圖 | 該用 | 理由 |
|---|---|---|
| 一份邊清單，`E` 接近 `V`（稀疏） | **Kruskal** | 邊本來就有了；排序一次，`O(E log E)` |
| 一份鄰接表，或「從任一點開始往外長」 | **Prim + heap** | 骨架跟 Dijkstra 一樣，`O(E log V)` |
| 平面上的點 — **每一對**都是一條邊（稠密，`E ≈ V²/2`） | **Prim + 陣列** | `O(V²)`、不用邊清單、`O(V)` 記憶體 — 已是最佳，因為每一對都非看過一次不可 |
| 需要排除／強制選某條特定邊 | **Kruskal** | 很容易跳過某條邊或先把它合併，再重跑一次 |
| 邊是線上陸續進來的，或兩個節點一連通就要停 | **Kruskal** | 每加一條邊，它的併查集都能回答「連通了沒？」 |

面試時，Kruskal 通常是比較快能寫對的那個；圖很稠密時要點名的是 Prim，而在那種情況下真正
勝過其他做法的，是稠密陣列版的 Prim。

---

<!-- 0de0d3d03b4b -->
## 1) 通用形式

<!-- 612c6bcdef45 -->
### 1-1) Kruskal — 排序好的邊 + 併查集 ⭐⭐⭐⭐⭐

**步驟：**
1. 把所有邊依權重排序，最便宜的在前。
2. 對每條邊 `(w, a, b)`：若 `a` 與 `b` 已在同一個連通分量，就跳過（會形成環）；否則把它們合併並加上 `w`。
3. 選滿 `n - 1` 條邊就停。最後不到 `n - 1` 條 → 圖不連通。

<!--CODE-->

<!--CODE-->

<!-- 535cb286c6b3 -->
### 1-2) Prim — 用 min-heap 長出一棵樹 ⭐⭐⭐⭐⭐

**步驟：**
1. 推入 `(0, start)` — 第一個節點加入不用花錢。
2. 彈出最便宜的 `(cost, node)`。若 `node` 已經在樹裡就跳過 — 它稍早已用更便宜的價錢加入了。
3. 否則把它加入樹、加上 `cost`，並對每條連到樹外鄰居的邊推入 `(w, neighbour)`。
4. 全部 `n` 個節點都進來就停。在那之前 heap 就空了 → 圖不連通。

<!--CODE-->

<!--CODE-->

<!-- ebaf0dfac3a7 -->
### 1-3) Prim vs Dijkstra — 只差一項

兩個迴圈長得一模一樣 — `(key, node)` 的 heap、節點被彈出第二次就跳過、把它的鄰居推進去。
差別只在 key：

<!--CODE-->

在 Prim 裡寫成 `cost + weight`，它就變成了 Dijkstra，LC 1584 會得到錯的答案
（最短路徑樹不是最小生成樹）。見
[shortest_path_comparison.md](./shortest_path_comparison.md#c-prim-vs-dijkstra--one-term-apart--lc-1584)。

<!-- 9f71201cd19a -->
### 1-4) 常見陷阱

- **在過期的彈出上算了成本。** lazy Prim 裡一個節點每有一個鄰居就會被推一次；只有它**第一次**
  被彈出時才是最便宜的。加成本*之前*先檢查 `in_tree`。
- **把有向邊當無向邊（或反過來）。** MST 是給**無向**圖用的 — Prim 的鄰接表要兩個方向都加。
  有向的「最小樹形圖」是另一套演算法（Chu–Liu/Edmonds），面試不會考。
- **忘了不連通的情況。** 回傳成本之前，先檢查 `edges_used == n - 1`（Kruskal）或
  `nodes_joined == n`（Prim）。
- **節點從 1 開始編號。** LC 1135 與 LC 1168 的節點是 `1..n`；`parent` 的大小要開 `n + 1`。
- **替完全圖建一份邊清單。** `n = 1000` 個點就有約 500,000 條邊外加一次排序；
  [2-1](#2-1-min-cost-to-connect-all-points--lc-1584-) 的稠密 Prim 完全不需要邊清單。

---

<!-- 5fa13fe640ce -->
## 2) LC 範例

<!-- 5849680257e3 -->
### 2-1) Min Cost to Connect All Points — LC 1584 ⭐⭐⭐⭐⭐

**題目：** 把平面上所有 `points` 連起來；連接兩點的成本是它們的曼哈頓距離
`|xi - xj| + |yi - yj|`。回傳最小總成本。

**核心想法：** 「任兩點之間恰好只有一條簡單路徑」代表要的是一棵生成**樹**，最便宜的那棵就是
**完全**圖的 MST — 每個點是一個節點，每一對點是一條邊。邊從來不會直接給你；需要時再算出來。

**執行追蹤** — `points = [[0,0],[2,2],[3,10],[5,2],[7,0]]`：

<!--CODE-->

<!--CODE-->

<!--CODE-->

<!-- f212d95ff14e -->
#### 稠密 Prim — `O(n²)`，不用 heap

**需要第二種解法的理由：更好的上界。** 圖是完全圖，heap 只會多帶來一個 `log n` 和 `O(n²)`
的記憶體。改成維護 `min_dist[i]` = 從樹連到點 `i` 最便宜的邊，每一輪掃描找出最小值：
`n` 輪 × `O(n)` 掃描 = `O(n²)` 時間、`O(n)` 空間 — 已是最佳，因為每一對都得看過一次。
面試時在 heap 版之後點出它。

<!--CODE-->

Kruskal 也會過 — 產生全部 `n(n-1)/2` 對，再丟給
[1-1](#1-1-kruskal--sorted-edges--union-find-) — 但在這題它是三種裡最慢的，
因為它要把每一對都拿來排序。

<!-- 8d433ca67d97 -->
### 2-2) Connecting Cities With Minimum Cost — LC 1135

**題目：** `n` 座城市 `1..n`，`connections[i] = [a, b, cost]`。回傳把每座城市都連起來的
最小成本，做不到就回傳 `-1`。

**核心想法：** 這就是毫無偽裝的 [1-1](#1-1-kruskal--sorted-edges--union-find-) — 一份邊清單、
一張稀疏圖，不連通時答案就是 `-1`。唯一的改動是城市從 1 開始編號：把它們平移到 `0..n-1`
（或把併查集開成 `n + 1`）。

<!--CODE-->

<!-- 2c0d20b20732 -->
### 2-3) Optimize Water Distribution in a Village — LC 1168 ⭐⭐⭐⭐

**題目：** 每間房子 `i` 要嘛花 `wells[i-1]` 自己蓋一口井，要嘛用水管接到另一間房子。
回傳讓每間房子都有水的最小總成本。

**核心想法 — 虛擬節點。** 「在房子 `i` 蓋一口井」等同於「從水源 `0` 拉一條水管到房子 `i`」，
成本是 `wells[i-1]`。加上這個節點和這 `n` 條邊，答案就是這張 `n + 1` 個節點的圖的 MST。
每一題「每個節點付一筆固定價錢，*或*把它接到鄰居」都能這樣化簡。

<!--CODE-->

<!--CODE-->

<!-- e7c685c63520 -->
### 2-4) Find Critical and Pseudo-Critical Edges in MST — LC 1489

**題目：** 把每條邊分類：每一棵 MST 都含有它就是**關鍵邊（critical）**；某些 MST 含有它、
但不是全部，就是**偽關鍵邊（pseudo-critical）**。

**核心想法 — 圍繞一條邊重建 MST。** 先算一次 MST 的權重 `best`。接著對每條邊 `e`：

- **排除** `e` 再重建：若圖變得不連通、或權重升到比 `best` 高，`e` 就是**關鍵邊**；
- 否則**強制**先選 `e`（合併它、算進它的權重）再重建：若權重仍等於 `best`，`e` 就是
  **偽關鍵邊**。

在只排序一次的邊上跑 `E + 1` 次 Kruskal：`O(E² · α(V))`，`E ≤ 200` 綽綽有餘。

<!--CODE-->

<!-- 7be698a9e921 -->
### 2-5) 相似題目

| LC | 題目 | 題型 | 備註 |
|---|---|---|---|
| 1584 | Min Cost to Connect All Points | 完全幾何圖 | Prim；稠密陣列版 Prim 是 `O(n²)` — [2-1](#2-1-min-cost-to-connect-all-points--lc-1584-) |
| 1135 | Connecting Cities With Minimum Cost | 邊清單 | 原封不動的 Kruskal，不連通回傳 `-1` — [2-2](#2-2-connecting-cities-with-minimum-cost--lc-1135) |
| 1168 | Optimize Water Distribution in a Village | 虛擬源點 | 井 = 從節點 `0` 出發的邊 — [2-3](#2-3-optimize-water-distribution-in-a-village--lc-1168-) |
| 1489 | Find Critical and Pseudo-Critical Edges in MST | 邊的分類 | 排除／強制選一條邊 — [2-4](#2-4-find-critical-and-pseudo-critical-edges-in-mst--lc-1489) |
| 3600 | Maximize Spanning Tree Stability with Upgrades | 瓶頸樹 | 二分搜尋穩定度，用併查集檢查「只用 `≥` 它的邊」是否還能組成生成樹 |
| 1631 | Path With Minimum Effort | 瓶頸路徑 | 起點與終點一連通就停下的 Kruskal 掃描 — [union_find_examples.md](./union_find_examples.md#13-path-with-minimum-effort--lc-1631--sorted-edge-kruskal-style) |
| 1167 | Minimum Cost to Connect Sticks | **不是** MST | 合併成本會重複計入已合併的長度 — 是 Huffman，一種 heap 貪心（[heap.md](./heap.md)） |

**決策樹：**

<!--CODE-->
