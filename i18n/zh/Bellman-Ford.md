<!-- b97098dd2127 -->
# Bellman-Ford Algorithm

> **範圍** — 容得下**負權重**、而且能偵測負環的單源最短路徑演算法，另外還有限制跳數（k hop）的變形。
> **另見**：[shortest_path_comparison.md](./shortest_path_comparison.md) — 該挑哪個演算法；[Dijkstra.md](./Dijkstra.md) — 權重全非負時更快；[Floyd-Warshall.md](./Floyd-Warshall.md) — 全點對最短路徑。

<!-- 2fe2a7427baf -->
## LeetCode 題目清單

- [Shortest Path](https://leetcode.com/problem-list/shortest-path/)
- [Graph Theory](https://leetcode.com/problem-list/graph/)
- [Dynamic Programming](https://leetcode.com/problem-list/dynamic-programming/)

<!-- 0ed129077154 -->
## 總覽
**Bellman-Ford 演算法**是一個能處理負邊權的單源最短路徑演算法。跟 Dijkstra（戴克斯特拉）不同，它可以偵測負環；只要圖中沒有負環，它保證找得到最短路徑。

<!-- 7b0eaa5d721e -->
### 關鍵性質
- **時間複雜度**：O(V·E)，V 是頂點數、E 是邊數
- **空間複雜度**：O(V)，用於距離陣列
- **核心想法**：把所有邊鬆弛 V-1 次
- **什麼時候用**：有負權重的單源最短路徑
- **特色**：`可以偵測`負環

<!-- e0fa482a5345 -->
### 核心特徵
- **實作簡單**：就是兩層巢狀迴圈掃邊
- **吃得下負權重**：跟 Dijkstra 不同，負邊也能處理
- **能偵測環**：可以找出圖中的負環
- **以鬆弛為基礎**：反覆鬆弛邊，直到收斂
- **保證最佳**：只要沒有負環，找到的就是最短路徑

<!-- 57de7310e449 -->
### 參考資料
- [Bellman-Ford Visualization](https://www.cs.usfca.edu/~galles/visualization/BellmanFord.html)
- [CP Algorithms - Bellman-Ford](https://cp-algorithms.com/graph/bellman_ford.html)
- [Dijkstra Cheatsheet](./Dijkstra.md) - 非負權重時的對照
- [Floyd-Warshall Cheatsheet](./Floyd-Warshall.md) - 全點對的對照

<!-- 5ab0cfa81ec9 -->
## 題型分類

<!-- df546056f396 -->
### **分類 1：帶負權重的經典最短路徑**
- **說明**：有負邊的標準單源最短路徑
- **例子**：LC 787（Cheapest Flights K Stops）、貨幣換算
- **模式**：直接套 Bellman-Ford

<!-- d5136af805a5 -->
### **分類 2：偵測負環**
- **說明**：判斷圖中是否含負環
- **例子**：LC 1334（套利偵測）、貨幣套利
- **模式**：多跑第 V 輪，看還有沒有更新

<!-- 7d71eed3c116 -->
### **分類 3：帶限制的最短路徑**
- **說明**：最多只能用 K 條邊／K 次跳躍的最短路徑
- **例子**：LC 787（K 站中轉）、LC 1928（連線次數受限）
- **模式**：把迭代次數設上限的 Bellman-Ford

<!-- 8c61ae8af90b -->
### **分類 4：貨幣兌換與套利**
- **說明**：把權重取對數，藉此偵測套利機會
- **例子**：外匯交易、價格套利
- **模式**：取對數轉換 + 偵測負環

<!-- 4ad18446d0e5 -->
### **分類 5：帶成本的網路路由**
- **說明**：在可能有負成本（折扣）的情況下找最便宜的路徑
- **例子**：有折扣的配送、有回饋的路由
- **模式**：標準 Bellman-Ford 加上成本追蹤

<!-- ddc7f484e4af -->
## 模板與演算法

<!-- 02550448ead4 -->
### 模板比較表
| 模板類型 | 適用情境 | 迭代次數 | 什麼時候用 |
|---------------|----------|------------|-------------|
| **基本 Bellman-Ford** | 標準最短路徑 | V-1 | 有負權重 |
| **加上負環偵測** | 偵測負環 | V | 套利、驗證 |
| **限制 K 條邊** | 邊數受限的路徑 | K | 跳數／中轉次數受限 |
| **SPFA（佇列最佳化）** | 平均情況更快 | 不固定 | 稀疏圖 |
| **路徑重建** | 記錄實際路徑 | V-1 | 需要路徑細節 |

<!-- 9182c5861212 -->
### 模板 1：基本 Bellman-Ford
<!--CODE-->

<!-- 33253d82ac05 -->
### 模板 2：帶負環偵測的 Bellman-Ford
<!--CODE-->

<!-- c2d8628fac80 -->
### 模板 3：限制 K 條邊的 Bellman-Ford
<!--CODE-->

<!-- b04553272fdb -->
### 模板 4：SPFA（Shortest Path Faster Algorithm）
<!--CODE-->

<!-- eeac829c0014 -->
### 模板 5：帶路徑重建的 Bellman-Ford
<!--CODE-->

<!-- fb6e8f9d9d42 -->
### 模板 6：貨幣套利偵測
<!--CODE-->

<!-- 8d677454b9e9 -->
## 演算法比較

<!-- f6628cf9622b -->
### Bellman-Ford vs Dijkstra vs Floyd-Warshall

| 特性 | Bellman-Ford | Dijkstra | Floyd-Warshall |
|---------|--------------|----------|----------------|
| **問題類型** | 單源 | 單源 | 全點對 |
| **時間複雜度** | O(V·E) | O((V+E) log V) | O(V³) |
| **空間複雜度** | O(V) | O(V) | O(V²) |
| **負權重** | ✅ 可以 | ❌ 不行 | ✅ 可以 |
| **負環** | ✅ 可偵測 | N/A | ✅ 可偵測 |
| **實作難度** | 簡單（2 層迴圈） | 中等（要優先佇列） | 非常簡單（3 層迴圈） |
| **提早結束** | ✅ 可以 | ✅ 抵達目標即可停 | ❌ 必須跑完 |
| **平均效能** | 稠密圖上很慢 | 稀疏圖上很快 | 小圖上不錯 |
| **最適合的圖** | 任何圖（尤其有負邊時） | 稀疏、非負 | 稠密、小型 |
| **最佳化手段** | SPFA 變形 | 加啟發式的 A* | 沒有實用的 |

<!-- d3e0e4889e02 -->
### 各演算法的適用時機

<!--CODE-->

<!-- dbb1e7a57324 -->
### 效能比較

**例子：V=1000 個頂點的圖**

| 圖的稠密度 | 邊數 | Bellman-Ford | Dijkstra | SPFA（平均） |
|---------------|-------|--------------|----------|------------|
| 稀疏 | 2,000 | 2,000,000 次操作 | ~20,000 次操作 ⚡ | ~40,000 次操作 |
| 中等 | 10,000 | 10,000,000 次操作 | ~100,000 次操作 ⚡ | ~200,000 次操作 |
| 稠密 | 100,000 | 100,000,000 次操作 | ~1,000,000 次操作 ⚡ | ~2,000,000 次操作 |

**註**：在非負權重的情況下，Dijkstra 通常比 Bellman-Ford 快上 50-100 倍。

<!-- 32675455dece -->
### 演算法挑選範例

| 情境 | 最佳演算法 | 為什麼 |
|----------|----------------|-----|
| GPS 導航（道路網） | **Dijkstra** | 非負權重、稀疏圖 |
| 帶手續費的貨幣兌換 | **Bellman-Ford** | 可能出現負權重 |
| 套利偵測 | **Bellman-Ford** | 需要偵測負環 |
| 網路延遲時間 | **Dijkstra** | 非負、單源 |
| 課程先修關係（全點對） | **Floyd-Warshall** | 小圖、遞移閉包 |
| 最多 K 次中轉的航班 | **Bellman-Ford（跑 K 輪）** | 有邊數限制 |
| 網際網路路由（OSPF） | **Dijkstra** | 成本非負 |
| 外匯交易機會 | **Bellman-Ford** | 偵測套利環 |

<!-- 598a9bfd2a3c -->
## LC 範例

<!-- 48e280e435da -->
### 2-1) Cheapest Flights Within K Stops (LC 787) — 鬆弛 K 輪的 Bellman-Ford
> 最多鬆弛 K+1 輪；每輪都複製一份 dist 陣列，避免用到同一輪剛更新的邊。

<!--CODE-->

<!--CODE-->

<!-- cc527643a511 -->
#### **變形：分層 DP 觀點 `dp[t][v]` — 「最多 K 條邊」vs「剛好 K 條邊」** ⭐⭐⭐⭐⭐

> 鬆弛完全一樣，只是把輪次索引攤開成一個 DP 維度。**關鍵差異**：保留前一列的繼承（`dp[t] = dp[t-1]`）代表**最多** K 條邊；不保留就代表**剛好** K 條邊。

**核心想法**：Bellman-Ford *本來就是*一個以「用了幾條邊」為維度的 DP。這也是為什麼限制跳數的那題（LC 787）同時出現在本文開頭連的 `shortest-path` 和 `dynamic-programming` 兩個標籤頁上。

**遞迴式**：
<!--CODE-->

把 `t` 明確寫出來，剛好回答了面試官幾乎一定會追問的兩個問題：
- *「滾動版為什麼要複製陣列？」* → 第 `t` 輪只能讀第 `t-1` 輪；沒有複製的話，一輪之內就可能串起 2 條以上的邊，悄悄超出跳數上限。
- *「如果路徑必須**剛好**用 K 條邊呢？」* → 每一列從 `INF` 開始，而不是複製前一列，這樣比較短的解就活不下來。

<!--CODE-->

<!--CODE-->

| 你要的 | 每列的初始化 | 答案 |
|------|--------------------|------|
| **最多 K 條邊** | `dp[t] = dp[t-1].clone()` / `dp[t-1][:]` | `dp[K][dst]` |
| **剛好 K 條邊** | `dp[t] = [INF] * n` | `dp[K][dst]` |
| **邊數不限** | 跑 `V-1` 輪，原地更新即可 | `dp[V-1][dst]` |

**🚫 陷阱**：在 Java 裡 `INF` 要用 `Integer.MAX_VALUE / 2`（不要用 `Integer.MAX_VALUE`），否則 `dp[u] + w` 會溢位成負數，導致每次鬆弛都「成功」。

<!-- bb5628007a72 -->
### 2-2) Network Delay Time (LC 743) — 鬆弛 N-1 輪的 Bellman-Ford
> 把所有邊鬆弛 N-1 輪；訊號傳到所有節點的最短時間 = dist 陣列的最大值。

<!--CODE-->

<!--CODE-->

<!-- ba8fc0b98f28 -->
### 2-3) 貨幣套利偵測 — 用對數轉換的自訂 Bellman-Ford

<!--CODE-->

<!-- 920685ec7684 -->
### 2-4) Minimum Cost to Reach Destination — 自訂的 Bellman-Ford 變形 — LC 1928

<!--CODE-->

<!-- 419589a0d8c9 -->
### 2-5) 時光旅行問題 — 理論上的負環偵測

<!--CODE-->

<!-- c8b75ab397b3 -->
## 依模式分類的題目

<!-- 4f95fda9485a -->
### **帶負權重的經典最短路徑**
| 題目 | LC # | 關鍵技巧 | 難度 |
|---------|------|---------------|------------|
| Cheapest Flights Within K Stops | 787 | K 條邊的 Bellman-Ford | Medium |
| Network Delay Time | 743 | 基本 Bellman-Ford（Dijkstra 更好） | Medium |
| Minimum Cost to Reach Destination | 1928 | 帶限制的路徑 | Hard |
| Path with Maximum Probability | 1514 | 改寫權重 | Medium |

<!-- 3c3b6e06ba75 -->
### **負環偵測**
| 題目 | LC # | 關鍵技巧 | 難度 |
|---------|------|---------------|------------|
| Currency Arbitrage | N/A | 對數轉換 | Hard |
| Detect Cycle in Graph | N/A | 檢查第 V 輪 | Medium |
| Find Negative Weight Cycle | N/A | 記錄父節點指標 | Hard |

<!-- 62113ef31832 -->
### **帶限制的路徑問題**
| 題目 | LC # | 關鍵技巧 | 難度 |
|---------|------|---------------|------------|
| Cheapest Flights K Stops | 787 | 限制迭代輪數 | Medium |
| Maximum Probability Path | 1514 | 改寫的 Bellman-Ford | Medium |
| Minimum Cost K Edges | N/A | 跑 K 輪 | Medium |

<!-- 292102d8426c -->
### **含負權重的圖上度量**
| 題目 | LC # | 關鍵技巧 | 難度 |
|---------|------|---------------|------------|
| Evaluate Division | 399 | 帶權圖 | Medium |
| Accounts Merge | 721 | 併查集更好 | Medium |

<!-- 14cad3b3f9bb -->
## 決策框架

<!-- baf50893c842 -->
### 什麼時候該用 Bellman-Ford

✅ **這些情況用 Bellman-Ford：**
- 圖裡有負邊權
- 需要偵測負環
- 路徑最多只能用 K 條邊
- 貨幣兌換或套利問題
- 實作簡單比速度重要
- 在分散式系統上跑（可以平行化）
- 圖的結構常常變動（比較好更新）

❌ **這些情況別用 Bellman-Ford：**
- 權重全都非負（改用 Dijkstra）
- 小圖上要全點對最短路徑（改用 Floyd-Warshall）
- 圖非常大又稠密（太慢）
- 對即時效能要求高、又沒有負權重
- 圖沒有權重（改用 BFS）

<!-- a7b2bf450a4a -->
### 這題真的是 Bellman-Ford 嗎？（標籤分流）

本文開頭連的 `graph` / `dynamic-programming` / `shortest-path` 清單裡有好幾百題，但**真正非用 Bellman-Ford 不可的 LC 題目非常少**。下面三個徵兆全部成立，你才該動用它：

1. **邊上有權重。** 沒有權重 → 單純的 BFS（[bfs.md](./bfs.md)）。
2. **權重可能是負的，或者路徑的邊數／跳數有上限。** 非負*而且*沒上限 → Dijkstra 嚴格來說更好（[Dijkstra.md](./Dijkstra.md)、[shortest_path_comparison.md](./shortest_path_comparison.md)）。
3. **要求的答案是最短／最便宜路徑，或是環的可行性判斷** — 不是計數、不是子序列、不是排序、也不是可達性。

如果只有第 3 點成立，你八成看的是另一個模式。以下是那幾個標籤頁上常見的高頻「長得很像」的題目：

| LC | 題目 | 為什麼看起來像 Bellman-Ford | 其實是什麼 |
|----|-------|--------------------------------|---------------------|
| 45 | Jump Game II | 「最少跳幾次」讀起來像限制邊數的最短路徑 | 所有權重都是 1 → 貪婪／BFS 分層只要 **O(n)**，而 BF 是 O(V·E) — [greedy.md](./greedy.md) |
| 279 | Perfect Squares | 隱式圖上的最少步數 | 無權重 BFS 或零錢兌換型 DP — [dp.md](./dp.md) |
| 207 | Course Schedule | 圖 +「偵測環」 | 拓撲排序（Kahn / DFS 著色）— [topology_sorting.md](./topology_sorting.md) |
| 1192 | Critical Connections in a Network | 以邊為主的圖掃描 | Tarjan 找橋（DFS low-link）— [graph.md](./graph.md) |
| 785 | Is Graph Bipartite? | 對所有邊做全域一致性檢查 | BFS/DFS 二著色 — [bfs.md](./bfs.md) |
| 133 | Clone Graph | 完整走訪整張圖 | DFS/BFS 加一個 visited map |
| 947 | Most Stones Removed with Same Row or Column | 隱式圖上的連通性 | 併查集 — [union_find.md](./union_find.md) |
| 332 | Reconstruct Itinerary | 「找出穿過整張圖的路徑」 | Hierholzer 尤拉路徑 — [graph.md](./graph.md) |
| 753 | Cracking the Safe | 覆蓋整張圖的最短字串 | de Bruijn 序列／尤拉迴路 |
| 53 | Maximum Subarray | 一路「鬆弛目前最佳解」的 DP | Kadane — [kadane_algorithm.md](./kadane_algorithm.md) |

**面試金句** — LC 45 是最值得準備的對比：它*確實*是一個最少邊數的最短路徑問題，所以 Bellman-Ford 是對的，但因為每條邊權重都是 1，BFS 分層／貪婪掃描只要 O(n) 就解掉。把兩個都講出來、然後挑便宜的那個，展現的是你「做了選擇」而不是「用預設值」。

<!-- 18a29cf21d59 -->
### 實作檢查清單

<!--CODE-->

<!-- e116981d9250 -->
### 常見最佳化

1. **提早結束**
<!--CODE-->

2. **SPFA（用佇列）**
<!--CODE-->

3. **雙向搜尋**
<!--CODE-->

4. **限制迭代輪數**
<!--CODE-->

<!-- ea4fa3974f42 -->
## 總結與速查

<!-- 065bf746ed61 -->
### 時間／空間複雜度

| 面向 | 複雜度 | 備註 |
|--------|------------|-------|
| 時間（標準） | O(V·E) | 對 E 條邊跑 V-1 輪 |
| 時間（SPFA 平均） | O(E) | 佇列最佳化後的平均情況 |
| 時間（SPFA 最壞） | O(V·E) | 退化回標準版 |
| 空間 | O(V) | 距離陣列 + 父節點陣列 |
| 環偵測 | +O(E) | 多跑的第 V 輪 |

<!-- cfaaf96e3ce6 -->
### 演算法核心結構

<!--CODE-->

<!-- cfdd68547b70 -->
### 跟其他演算法的關鍵差異

| 面向 | Bellman-Ford | Dijkstra | Floyd-Warshall |
|--------|--------------|----------|----------------|
| **邊的鬆弛** | 所有邊，跑 V-1 次 | 只處理距離最短的節點 | 透過中介點處理所有點對 |
| **資料結構** | 單純的陣列 | 優先佇列 | 二維矩陣 |
| **順序重要嗎** | 不重要（所有邊都鬆弛） | 重要（貪婪挑選） | 重要（k 迴圈要放最外層） |
| **可平行化** | ✅ 可以（同一輪之內） | ❌ 不行（本質是序列的） | ✅ 可以（要改寫） |

<!-- c3d0316b51ed -->
### 常見模式與技巧

<!-- f49471778ec3 -->
#### **模式 1：負環偵測**
<!--CODE-->

<!-- 347a7014f2be -->
#### **模式 2：K 條邊的限制**
<!--CODE-->

<!-- f75d0ce04ddc -->
#### **模式 3：路徑重建**
<!--CODE-->

<!-- 5454d9b40da1 -->
#### **模式 4：套利偵測**
<!--CODE-->

<!-- 672ca6bf6bab -->
#### **模式 5：提早結束**
<!--CODE-->

<!-- cac9bbd22a4d -->
### 常見錯誤與提示

**🚫 常見錯誤：**
- 明明 Dijkstra 就能解卻用 Bellman-Ford（白白浪費時間）
- 鬆弛前忘記檢查 `dist[u] != inf`
- 做 K 條邊限制的題目時沒有用暫存陣列
- 迭代輪數搞錯（標準版應該是 V-1）
- 沒處理不連通的分量
- 忘了加上提早結束的最佳化

**✅ 最佳實務：**
- 先確認負權重是不是真的存在
- 稀疏圖上用 SPFA 換取更好的平均效能
- 實作提早結束來提升效率
- K 條邊的題目用暫存陣列，避免錯誤的更新
- 比較時小心處理無限大的值
- 沒有負權重就先考慮 Dijkstra
- 需要全點對時，先跟 Floyd-Warshall 的成本比一比

<!-- a7f2638e3cc4 -->
### 面試提示

1. **什麼時候該把 Bellman-Ford 講出來**：
   - 「有負邊權嗎？」→ 有的話就提 Bellman-Ford
   - 「需要偵測負環嗎？」→ 那答案就是 Bellman-Ford
   - 「路徑最多 K 條邊？」→ 改寫過的 Bellman-Ford

2. **複雜度的討論**：
   - 一開始就把 O(V·E) 講清楚
   - 主動說明非負權重下它比 Dijkstra 慢
   - 稀疏圖上把 SPFA 當成最佳化手段提出來

3. **實作上的說明**：
   - 比 Dijkstra 好寫（不用優先佇列）
   - 要加限制（K 條邊）很容易改
   - 多跑一輪就能偵測負環

4. **其他解法**：
   - 沒有負權重 → 「Dijkstra 會更快」
   - 需要全點對 → 「Floyd-Warshall 可能更單純」
   - 圖非常大 → 「建議用 SPFA 最佳化」

5. **值得討論的邊界情況**：
   - 不連通的分量
   - 負環（怎麼處理）
   - 某些頂點從起點根本到不了
   - 同一對頂點之間有多條邊

<!-- 512dcdc1ccbd -->
### 相關演算法

- **[Dijkstra](./Dijkstra.md)**：更快的單源解法，但不吃負權重
- **[Floyd-Warshall](./Floyd-Warshall.md)**：全點對，可處理負權重
- **SPFA**：用佇列最佳化的 Bellman-Ford 變形
- **Johnson's Algorithm**：重新配權 + Dijkstra，用於全點對
- **Yen's Algorithm**：前 K 短路徑
- **Eppstein's Algorithm**：前 K 短路徑（更快）

<!-- 4d6ca31aac58 -->
### 快速決策矩陣

| 你的情況 | 選擇 |
|----------------|--------|
| 單源、沒有負權重 | **Dijkstra** ⚡ |
| 單源、有負權重 | **Bellman-Ford** ✅ |
| 需要偵測負環 | **Bellman-Ford** ✅ |
| 最多 K 條邊／K 次跳躍 | **Bellman-Ford（跑 K 輪）** ✅ |
| 全點對、小圖 | **Floyd-Warshall** |
| 全點對、大圖 | **跑 V 次 Dijkstra** 或 **Johnson's** |
| 無權重的圖 | **BFS** ⚡⚡ |
| 貨幣套利 | **Bellman-Ford** ✅ |
| 即時導航 | **Dijkstra** 或 **A*** ⚡ |
