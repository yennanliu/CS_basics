<!-- 77235418730a -->
# 圖論實戰題解

> **範圍** — [graph.md](./graph.md) 的題目集：每題一張 problem card，依照它歸約到的 graph.md 模板分組，本身不放任何模板或理論。
> **另見**：[graph.md](./graph.md) — 圖的表示法、走訪、連通性與環偵測，以及下面每張卡歸約到的模板；[graph_advanced.md](./graph_advanced.md) — Tarjan、Euler、最大流與二分圖的進階題；[dfs_examples.md](./dfs_examples.md) 和 [bfs_examples.md](./bfs_examples.md) — 這份指過去的網格題與樹題；[union_find_examples.md](./union_find_examples.md) — DSU 那份自己的題目集。

<!-- b784189b9ec9 -->
## LeetCode 題目清單

- [Graph Theory](https://leetcode.com/problem-list/graph/)
- [Depth-First Search](https://leetcode.com/problem-list/depth-first-search/)
- [Breadth-First Search](https://leetcode.com/problem-list/breadth-first-search/)
- [Union Find](https://leetcode.com/problem-list/union-find/)

<!-- c06ae2bc1fdd -->
## 總覽

不屬於網格、樹或依賴順序的經典圖論題，每題一張 problem card，放在它歸約到的
[graph.md](./graph.md) 模板底下。[graph.md](./graph.md) 放模板；這份放題目。
已經在別處完整解過的題目，在 Index 裡只留一列指標，不再放第二份。

<!-- 37d621e67457 -->
## 索引

| LC | 題目 | 歸約到 | 難度 |
|---|---|---|---|
| 323 | [Number of Connected Components in an Undirected Graph](#number-of-connected-components-in-an-undirected-graph--lc-323) | [graph § Template 1](./graph.md#template-1-connected-components-by-traversal--lc-547-) — 走訪計算連通分量 | Medium |
| 133 | [Clone Graph](#clone-graph--lc-133-) | [graph § Template 2](./graph.md#template-2-dfs-reachability--lc-841-) — DFS 可達性 | Medium |
| 947 | [Most Stones Removed with Same Row or Column](#most-stones-removed-with-same-row-or-column--lc-947) | [graph § Template 3](./graph.md#template-3-union-find-with-path-compression--lc-684-) — union-find | Medium |
| 886 | [Possible Bipartition](#possible-bipartition--lc-886) | [graph § Template 6](./graph.md#template-6-bipartite-check-2-coloring--lc-785-) — 二著色 | Medium |

**在別份解過** — 一題一個家，所以這些只是指標：

| LC | 題目 | 解在 |
|---|---|---|
| 200 | Number of Islands | [dfs.md § Template 2](./dfs.md#template-2-graph--grid-dfs-flood-fill--lc-200-) |
| 695 | Max Area of Island | [dfs_examples.md](./dfs_examples.md) |
| 742 | Closest Leaf in a Binary Tree | [bfs_examples.md](./bfs_examples.md) |
| 1319 | Number of Operations to Make Network Connected | [union_find_examples.md](./union_find_examples.md) |
| 207 | Course Schedule | [graph.md § Template 4](./graph.md#template-4-topological-sort-kahns-bfs--lc-207-) |
| 802 | Find Eventual Safe States | [graph.md § Template 5](./graph.md#template-5-three-state-dfs-cycle-detection--lc-802-) |
| 399 | Evaluate Division | [dfs.md § Template 10](./dfs.md#template-10-weighted-graph-dfs-divisionratio-queries--lc-399) |
| 329 | Longest Increasing Path in a Matrix | [topology_sorting.md § Template 9](./topology_sorting.md#template-9-topological-sort--dp-on-an-implicit-dag-) |
| 815 | Bus Routes | [bfs_advanced.md](./bfs_advanced.md) |

<!-- 7745728500f9 -->
## 連通分量範例

<!-- 11165f7cd516 -->
### Number of Connected Components in an Undirected Graph — LC 323

> **關鍵** — 從還沒走訪過的節點發射的每一次走訪，恰好涵蓋一個連通分量，所以答案就是發射的次數。
> 圖是以邊列表給的，所以要先把鄰接表的**兩個**方向都建好。

**歸約到**：[graph § Template 1](./graph.md#template-1-connected-components-by-traversal--lc-547-) — 同一個「發射並計數」的迴圈，只是輸入從鄰接矩陣換成邊列表。

<!--CODE-->

**陷阱**
- 孤立節點沒有邊，所以永遠不會出現在 `graph` 裡：要對 `range(n)` 迴圈，不要對鄰接表的 key 迴圈，
  否則 `n = 3, edges = []` 會回傳 0 而不是 3。
- union-find 的版本（每次成功合併就把計數器減一）一樣好，解在
  [union_find_examples.md](./union_find_examples.md)。

<!-- 2682caa59ee3 -->
## DFS 可達性範例

<!-- b343e568bf4f -->
### Clone Graph — LC 133 ⭐⭐⭐⭐

> **關鍵** — `{original: copy}` 這個 map **就是** visited 集合。在遞迴進鄰居**之前**先登記節點的複本；
> 這樣繞回它的環會直接找到已經存在的複本，而不是無限遞迴。

**歸約到**：[graph § Template 2](./graph.md#template-2-dfs-reachability--lc-841-) — 從單一節點出發的 DFS 可達性，只是「走訪」的意思變成「建立複本」。

<!--CODE-->

**陷阱**
- 空圖：`node` 是 `None`，答案也是 `None`，不是一個新節點。
- map 的 key 用**節點物件**，不要用 `node.val`：LC 133 的值剛好唯一，但拿掉這個保證的追問會讓以值為 key 的 map
  無聲地出錯。

<!-- aabc502f4e94 -->
## Union-Find 範例

<!-- 69a6d53cf2ad -->
### Most Stones Removed with Same Row or Column — LC 947

> **關鍵** — 一個有 `k` 顆石頭的連通分量總能縮到只剩一顆，所以答案是 `n − 分量數`。邊沒有直接給：
> 兩顆石頭**同列或同行**就相連。把列和行本身當成 DSU 節點，每顆石頭就是一條邊 `第 r 列 — 第 c 行`。
> 這樣就不必比較全部 O(n²) 對。

**歸約到**：[graph § Template 3](./graph.md#template-3-union-find-with-path-compression--lc-684-) — union-find，只是節點是屬性值而不是石頭本身。

<!--CODE-->

<!--CODE-->

**陷阱**
- 列和行都是小整數，所以 `union(r, c)` 會把第 1 列和第 1 行併在一起。要把兩個命名空間分開：
  加標籤的 tuple、`~c`，或 `c + 10001`。
- 數**根**，不要數成功合併的次數。不論大小，一個分量只留下一顆。
- 兩兩比較的版本（同列或同行的石頭兩兩合併）正確但是 O(n²)；那是
  [union_find_examples.md](./union_find_examples.md) 裡的版本。

**相似題** — LC 721（共享屬性是 email）· LC 990（共享屬性是變數）

<!-- dbda0b995682 -->
## 二分圖檢查範例

<!-- fbdbc1d50252 -->
### Possible Bipartition — LC 886

> **關鍵** — 「分成兩組，讓互相討厭的人不在同一組」恰好就是「討厭關係圖是不是二分圖？」。
> 先用配對建圖，再對每個分量二著色：一條邊兩端同色就是奇數環，不可能分組。

**歸約到**：[graph § Template 6](./graph.md#template-6-bipartite-check-2-coloring--lc-785-) — 和 LC 785 一樣的 BFS 二著色，只是先從邊列表建圖，而且人是從 1 開始編號。程式碼就是 Template 6 的，跑在討厭關係圖上；奇偶併查集的寫法在 [graph_advanced § Template 3](./graph_advanced.md#template-3-parity-union-find-bipartite-check--lc-886)。

**陷阱**
- 討厭關係圖通常不連通：要從**每一個**還沒上色的人發射，否則第二個分量裡的衝突永遠看不到。
- 人編號是 `1..n`，所以顏色陣列要開 `n + 1`。

**相似題** — LC 785（圖以鄰接表給出）· LC 1042（四種顏色：用 greedy，不是二著色）
