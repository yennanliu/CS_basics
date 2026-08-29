<!-- 799ebdfe6b29 -->
# Shortest Path Algorithms — 什麼時候用哪一個

> **範圍** — **只做選型判斷** — 哪種題型該用哪個最短路徑演算法，以及直覺選法在哪裡會錯。不放完整實作。
> **另見**：[Dijkstra.md](./Dijkstra.md)；[Bellman-Ford.md](./Bellman-Ford.md)；[Floyd-Warshall.md](./Floyd-Warshall.md)；[bfs.md](./bfs.md) — 無權重／0-1 權重；[graph.md](./graph.md) — 圖的其他所有主題。

<!-- ecb3a1050010 -->
## LeetCode 題目清單

- [Shortest Path](https://leetcode.com/problem-list/shortest-path/)
- [Graph Theory](https://leetcode.com/problem-list/graph/)

<!-- 6f465a57b3e7 -->
## 快速決策表

| 問題 | 答案 → 演算法 |
|----------|-------------------|
| 權重非負、單一起點？ | **Dijkstra** O((V+E) log V) |
| 允許負權重、單一起點？ | **Bellman-Ford** O(V·E) |
| 需要偵測負環？ | **Bellman-Ford** O(V·E) |
| 最多 K 條邊／K 次轉乘？ | **Bellman-Ford**（跑 K 輪） |
| 全點對最短路徑？ | **Floyd-Warshall** O(V³) |
| 遞移閉包（可達性）？ | **Floyd-Warshall**（布林版） |
| 無權重圖？ | **BFS** O(V+E) |
| 權重只有 0/1 的格子圖？ | **0-1 BFS**（雙端佇列）O(V+E) |
| DAG？ | **拓撲排序 + 鬆弛** O(V+E) |
| 稠密圖、單一起點？ | **Dijkstra** 搭配陣列 O(V²) |
| 稀疏圖、單一起點？ | **Dijkstra** 搭配堆積 O((V+E) log V) |

<!-- 546d4426413c -->
## 並排比較

| 性質 | BFS | Dijkstra | Bellman-Ford | Floyd-Warshall |
|----------|-----|----------|--------------|----------------|
| **類型** | 單一起點 | 單一起點 | 單一起點 | 全點對 |
| **時間** | O(V+E) | O((V+E) log V) | O(V·E) | O(V³) |
| **空間** | O(V) | O(V) | O(V) | O(V²) |
| **負權重** | 不行 | 不行 | 可以 | 可以 |
| **偵測負環** | 不行 | 不行 | 可以 | 可以 |
| **圖的型態** | 無權重 | 有權重（≥0） | 任意 | 任意 |
| **做法** | 佇列 | 貪婪 + 堆積 | 鬆弛 ×(V-1) | DP |
| **實作難度** | 簡單 | 中等 | 簡單 | 簡單 |

<!-- aa98ebda5e2d -->
## 決策流程圖

<!--CODE-->

<!-- 7a2bb41c233c -->
## 常見錯誤與陷阱

<!-- b44c8862d4f1 -->
### 1. 對負權重用 Dijkstra
<!--CODE-->

<!-- 232c411c684f -->
### 2. LC 787（Cheapest Flights K Stops）該用 Bellman-Ford 還是 Dijkstra
<!--CODE-->

<!-- c6095b3bbc08 -->
### 3. Floyd-Warshall 的迴圈順序很要命
<!--CODE-->

<!-- 2e33cc91eb87 -->
### 4. 格子圖上該用 Dijkstra 還是 DP
<!--CODE-->

<!-- c709f92de2c1 -->
## 變形：0-1 BFS

當邊的權重只有 0 或 1：

<!--CODE-->

**經典題：** LC 1368（Min Cost to Make at Least One Valid Path）— 在格子圖上跑 0-1 BFS

<!-- 43e5f707872c -->
## LC 範例

| # | 題目 | 演算法 | 為什麼選它？ |
|---|---------|-----------|---------------|
| 743 | Network Delay Time | Dijkstra | 非負權重、單一起點 |
| 787 | Cheapest Flights K Stops | Bellman-Ford（跑 K 輪） | 有 K 條邊的限制 |
| 1334 | Find City Smallest Neighbors | Floyd-Warshall | 全點對 + 門檻值 |
| 1631 | Path with Min Effort | Dijkstra | 格子圖、四方向、非負 |
| 778 | Swim in Rising Water | Dijkstra / BS+BFS | 格子圖、最小化路徑上的最大值 |
| 1368 | Min Cost Valid Path | 0-1 BFS | 權重只有 0/1 |
| 1462 | Course Schedule IV | Floyd-Warshall | 遞移閉包 |
| 862 | Shortest Subarray Sum ≥ K | 根本不是最短路徑！ | 前綴和 + 單調雙端佇列 |
| 64 | Minimum Path Sum | DP（不是 Dijkstra） | DAG — 只能往右／往下 |
| 505 | The Maze II | Dijkstra | 有權重（滾動距離）、非負 |

<!-- cbed860bc677 -->
## 題目 → 演算法決策表（延伸版） ⭐⭐⭐⭐⭐

先看 **關鍵訊號** 那一欄 — 那是題目敘述裡逼你做出選擇的那句話。

| # | 題目 | 敘述裡的關鍵訊號 | 演算法 | 為什麼只能這樣做 |
|---|---------|-------------------------|-----------|-----------------|
| 847 | Shortest Path Visiting All Nodes | 「走訪**每一個**節點」、`n ≤ 12` | **對 `(node, mask)` 做 BFS** | 無權重 → BFS，但單純的 `seen[node]` 是錯的：同一個節點會需要帶著不同的已訪集合再進來一次 |
| 1129 | Shortest Path with Alternating Colors | 邊的顏色必須**交替** | **對 `(node, lastColor)` 做 BFS** | 無權重 → BFS；這個限制是多出來的**狀態**，不是多出來的權重 |
| 1514 | Path with Maximum Probability | 權重落在 `[0,1]`，要**最大化**乘積 | **Dijkstra 搭配最大堆積** | 乘積沿著路徑只會越來越小（權重 ≤ 1）→ 貪婪依然成立；嚴格來說取 `-log(p)` 就變回一般的最小成本 Dijkstra |
| 1976 | Number of Ways to Arrive at Destination | **計算**最短路徑有幾條 | **Dijkstra + `ways[]`** | 一趟搞定：嚴格變短時 `ways[v] = ways[u]`，打平時 `ways[v] += ways[u]`（對 1e9+7 取模） |
| 1928 | Minimum Cost to Reach Destination in Time | 在**時間預算**內最小化**費用** | **對 `(node, timeUsed)` 做 Dijkstra**，或 `dp[t][node]` | 兩個彼此獨立的資源 — 只對費用做 Dijkstra 是**錯的**（見下） |
| 399 | Evaluate Division | 有權重的邊，查詢是「**任一條**路徑的值」 | **DFS/BFS 邊走邊乘**（或帶權併查集） | 權重本身自洽 ⇒ 每條路徑答案都一樣，根本沒有東西要最小化 — 不需要鬆弛 |
| 1971 | Find if Path Exists in Graph | **只問可達性**，沒問距離 | **BFS / DFS / 併查集** | 敘述裡沒有成本 → 搬出最短路徑那套完全是做白工 |
| 1697 | Checking Existence of Edge Length Limited Paths | 「路徑上**每一條邊**都 < limit」 | **查詢排序 + 併查集（離線）** | 這是瓶頸（最大邊）限制，不是可加總的成本 → 鬆弛式 `dist[u]+w` 根本套不上 |
| 1584 | Min Cost to Connect All Points | 「把**所有**點連起來，總花費最小」 | **MST（Prim / Kruskal）** | 要的是最便宜的*樹*，不是最便宜的*路徑* — Prim 長得像 Dijkstra，但鬆弛方式不同（見下） |
| 329 | Longest Increasing Path in a Matrix | **最長**路徑 | **在隱式 DAG 上做記憶化 DFS** | 最長路徑沒有對應的貪婪／鬆弛做法；是「嚴格遞增」這條規則讓圖變成無環的 |
| 1857 | Largest Color Value in a Directed Graph | 路徑上某個顏色出現最多次 | **拓撲排序 + DP**（有環 ⇒ `-1`） | 同上，但這是一般的有向圖 — 做最長路徑 DP 之前必須先偵測環 |

<!-- eca20b28550b -->
## 直覺選法錯在哪 ⭐⭐⭐⭐⭐

<!-- bd241a740e60 -->
### A. Dijkstra 拿錯了純量 — LC 1928

<!--CODE-->

跟 **LC 787**（費用 vs 轉乘次數）是同一種形狀 — 只要題目同時給了**兩個預算**，其中一個就該放進狀態裡。

<!-- 7e17287e6314 -->
### B. `seen[node]` 太粗 — LC 847 / 1129 / 787

<!--CODE-->

<!-- 7cd780743484 -->
### C. Prim vs Dijkstra — 只差一項 — LC 1584

<!--CODE-->

堆積骨架一模一樣，鬆弛方式不同。「把**所有**節點連起來」→ MST；「從 **A 走到 B**」→ 最短路徑。

<!-- 936d491cf438 -->
### D. 要最大化而不是最小化 — LC 1514

<!--CODE-->

<!-- 24d47d92fef5 -->
## 模板：狀態擴增的搜尋 ⭐⭐⭐⭐⭐

**關鍵想法**：演算法不變（無權重就 BFS，有權重就 Dijkstra），把*狀態*加寬。
其餘的東西 — `seen`、`dist`、佇列裡塞的資料 — 全部改用加寬後的狀態當鍵值。

<!-- 310064b36fad -->
### 變形 1 — 對 `(node, bitmask)` 做 BFS — LC 847

<!--CODE-->

<!--CODE-->

<!-- c82c63f95488 -->
### 變形 2 — 對 `(node, resourceUsed)` 做 Dijkstra — LC 1928

<!--CODE-->

<!--CODE-->

> **LC 1928 的另一種寫法**：分層 DP `dp[t][node] = 恰好在時間 t 抵達 node 的最小費用`，
> 沿著 `t = 1..maxTime` 一路鬆弛 — 這就是 Bellman-Ford 的框架，`O(maxTime · E)`，不用堆積。
> 跟 LC 787 的「跑 K 輪」是同一招：有上界的那個資源直接變成 DP 的一個維度。

<!-- 0e62fda9bf03 -->
## 0-1 BFS — Java 版（LC 1368）

<!--CODE-->

<!-- 40d8130111a3 -->
## 延伸參考

- **LC 1311** Get Watched Videos by Your Friends — BFS 的**層**（剛好 `k` 步），再依出現次數排序；這是層數查詢，不是距離查詢。
- **LC 1466** Reorder Routes to Make All Paths Lead to the City Zero — 從 `0` 出發、無視方向走訪，數有幾條邊指錯方向（等於 0/1 邊權，但因為是樹 ⇒ 單純 DFS 就夠）。
- **LC 1489** Find Critical and Pseudo-Critical Edges in MST — 屬於 MST 家族，不是最短路徑；參考上面 LC 1584 那一列。

<!-- 543aeff6eb4c -->
## 另見
- [Dijkstra Cheatsheet](./Dijkstra.md)
- [Bellman-Ford Cheatsheet](./Bellman-Ford.md)
- [Floyd-Warshall Cheatsheet](./Floyd-Warshall.md)
- [BFS Cheatsheet](./bfs.md)
