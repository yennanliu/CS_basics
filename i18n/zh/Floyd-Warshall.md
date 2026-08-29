<!-- 42fd59c51797 -->
# Floyd-Warshall Algorithm

> **範圍** — 用「中繼頂點」做 DP 求**全點對**最短路徑 — O(V³)、稠密圖、遞移閉包。
> **另見**：[shortest_path_comparison.md](./shortest_path_comparison.md) — 該挑哪個演算法；[Dijkstra.md](./Dijkstra.md) — 單源、稀疏圖；[Bellman-Ford.md](./Bellman-Ford.md) — 單源、負權重。

<!-- 2fe2a7427baf -->
## LeetCode 題目清單

- [Shortest Path](https://leetcode.com/problem-list/shortest-path/)
- [Graph Theory](https://leetcode.com/problem-list/graph/)
- [Dynamic Programming](https://leetcode.com/problem-list/dynamic-programming/)

<!-- 7afb28caac61 -->
## 總覽
**Floyd-Warshall 演算法**是一個用動態規劃解全點對最短路徑的方法。它會算出加權圖裡所有頂點兩兩之間的最短路徑，就算有負權重的邊也行（但不能有負環）。

<!-- b52c14737a59 -->
### 關鍵性質
- **時間複雜度**：O(V³)，V 是頂點數
- **空間複雜度**：O(V²)，用來存距離矩陣
- **核心想法**：以中繼頂點為維度的動態規劃
- **什麼時候用**：全點對最短路徑，而且可以處理負權重
- **限制**：`Cannot` 處理 `negative cycles`（偵測得到，但算不出正確答案）

<!-- 44109f50ffb8 -->
### 核心特徵
- **動態規劃**：一個一個放入中繼頂點，逐步把解建起來
- **矩陣為基礎**：使用相鄰矩陣表示法
- **實作簡單**：三層巢狀迴圈
- **用途廣**：支援負權重、可偵測負環
- **路徑重建**：搭配前驅矩陣就能還原路徑

<!-- c1c0a63c1a81 -->
### 參考資料
- [Floyd-Warshall Visualization](https://www.cs.usfca.edu/~galles/visualization/Floyd.html)
- [CP Algorithms - Floyd-Warshall](https://cp-algorithms.com/graph/all-pair-shortest-path-floyd-warshall.html)
- [Dijkstra Cheatsheet](./Dijkstra.md) - 拿來跟單源版本比較
- [Bellman-Ford Cheatsheet](./Bellman-Ford.md) - 單源且允許負權重的版本

<!-- 5ab0cfa81ec9 -->
## 題型分類

<!-- d7c977c02188 -->
### **類型 1：經典全點對最短路徑**
- **說明**：求出所有頂點兩兩之間的最短路徑
- **例題**：LC 1334（Find City with Smallest Number）、LC 1462（Course Schedule IV）
- **模式**：Floyd-Warshall 直接套用

<!-- 3583760c1434 -->
### **類型 2：遞移閉包**
- **說明**：判斷任兩個頂點之間能不能到達
- **例題**：LC 1462（Course Schedule IV）、各種圖連通性問題
- **模式**：Floyd-Warshall 的布林版本

<!-- 252d3767be76 -->
### **類型 3：負環偵測**
- **說明**：判斷圖裡有沒有負環
- **例題**：套利偵測、負權重環
- **模式**：跑完 Floyd-Warshall 之後檢查對角線

<!-- c58eaced5dc2 -->
### **類型 4：Minimax／Maximin 路徑**
- **說明**：找出「最大邊最小」或「最小邊最大」的路徑
- **例題**：LC 1334（門檻類問題）、瓶頸最短路徑
- **模式**：把 Floyd-Warshall 的運算換掉

<!-- 844c0f18838c -->
### **類型 5：圖的直徑與各種度量**
- **說明**：求最長的最短路徑、圖心、半徑
- **例題**：網路直徑、圖的離心率
- **模式**：對 Floyd-Warshall 的結果做後處理

<!-- ddc7f484e4af -->
## 模板與演算法

<!-- e66e679800b5 -->
### 模板比較表
| 模板類型 | 使用情境 | 運算 | 什麼時候用 |
|---------------|----------|-----------|-------------|
| **基本 Floyd-Warshall** | 全點對最短路徑 | min(dist[i][j], dist[i][k]+dist[k][j]) | 一般最短路徑 |
| **遞移閉包** | 可達性 | dist[i][j] OR (dist[i][k] AND dist[k][j]) | 布林連通性 |
| **Minimax 路徑** | 瓶頸路徑 | min(dist[i][j], max(dist[i][k], dist[k][j])) | 容量／頻寬 |
| **路徑重建** | 追出實際路徑 | 前驅矩陣 | 需要真正的路徑 |
| **負環** | 偵測環 | 檢查 dist[i][i] < 0 | 套利、環偵測 |

<!-- a8983bbf313e -->
### 模板 1：基本 Floyd-Warshall
<!--CODE-->

<!-- f572f2dc600d -->
### 模板 2：Floyd-Warshall + 路徑重建
<!--CODE-->

<!-- 330ff196ac80 -->
### 模板 3：遞移閉包（可達性）
<!--CODE-->

<!-- dbb43a541d5d -->
### 模板 4：負環偵測
<!--CODE-->

<!-- cdf064b2737e -->
### 模板 5：Minimax 路徑（瓶頸）
<!--CODE-->

<!-- dce167eacc13 -->
### 模板 6：省空間版本
<!--CODE-->

<!-- 8d677454b9e9 -->
## 演算法比較

<!-- ca050f503c6c -->
### Floyd-Warshall vs Dijkstra vs Bellman-Ford

| 特性 | Floyd-Warshall | Dijkstra | Bellman-Ford |
|---------|----------------|----------|--------------|
| **問題類型** | 全點對最短路徑 | 單源最短路徑 | 單源最短路徑 |
| **時間複雜度** | O(V³) | O((V+E) log V) | O(V·E) |
| **空間複雜度** | O(V²) | O(V) | O(V) |
| **負權重** | ✅ 可以 | ❌ 不行 | ✅ 可以 |
| **負環** | 偵測得到 | N/A | 偵測得到 |
| **實作難度** | 非常簡單（3 層迴圈） | 中等（要用優先佇列） | 簡單（2 層迴圈） |
| **適合的圖** | 偏好稠密圖 | 偏好稀疏圖 | 都可以 |
| **輸出** | 全點對距離 | 單源距離 | 單源距離 |
| **最佳使用情境** | 小圖、要全點對 | 大型稀疏圖 | 負權重、環偵測 |

<!-- 2728300eaacb -->
### 什麼情況該用哪個演算法

<!--CODE-->

<!-- 0c10445976fd -->
### 實務比較表

| 情境 | 最佳演算法 | 理由 |
|----------|----------------|--------|
| 小型完全圖、要全點對 | Floyd-Warshall | O(V³) 可以接受，程式碼又短 |
| 大型稀疏圖、單源 | Dijkstra | O((V+E) log V) 快非常多 |
| 有負權重、單源 | Bellman-Ford | 只有它處理得了 |
| 遞移閉包 | Floyd-Warshall | DP 寫法最自然 |
| 格子上的最短路徑 | Dijkstra | 圖是隱式的，而且稀疏 |
| 網路直徑 | Floyd-Warshall | 反正本來就需要全點對 |
| 帶限制條件的路徑 | Dijkstra（改造版） | 狀態要怎麼帶都很彈性 |
| 套利偵測 | Floyd-Warshall | 需要環偵測，而且要全點對 |

<!-- 0a48bc68d0b7 -->
### 複雜度實例比較

以 V=1000 個頂點、E=5000 條邊的圖為例：

| 演算法 | 運算次數 | 相對速度 |
|-----------|------------|----------------|
| Floyd-Warshall | 1,000,000,000 | 基準（最慢） |
| Dijkstra（跑 V 次） | ~50,000 × log(1000) × 1000 | 快約 20 倍 |
| Dijkstra（單次） | ~5,000 × log(1000) | 快約 20,000 倍 |
| Bellman-Ford | 1000 × 5000 = 5,000,000 | 快約 200 倍 |

<!-- 598a9bfd2a3c -->
## LC 範例

<!-- f5869865a7f3 -->
### 2-1) Find the City With the Smallest Number of Neighbors (LC 1334) — Floyd-Warshall 全點對
> 先跑 Floyd-Warshall；再對每座城市算出門檻內可達的城市數；回傳最少的那座（平手取索引較大者）。

<!--CODE-->

<!--CODE-->

<!-- cfc5e9278846 -->
### 2-2) Course Schedule IV (LC 1462) — Floyd-Warshall 遞移閉包
> 用布林可達性矩陣；如果 i 是 j 的先修（直接或間接），reachable[i][j] = true。

<!--CODE-->

<!--CODE-->

<!-- 1d9e8dd87c81 -->
### 2-3) Network Delay Time Alternative Solution (LC 743) — Floyd-Warshall 全點對
> 算出全點對距離；答案就是從來源 k 出發的最大距離（比起 Dijkstra 是殺雞用牛刀，但答案正確）。

<!--CODE-->

<!--CODE-->

<!-- 2f922dc15a1e -->
### 2-4) Graph Connectivity With Threshold (LC 1627) — Floyd-Warshall 連通性
> 對所有 GCD > threshold 的組合建邊；再用 Floyd-Warshall 遞移閉包回答詢問。

<!--CODE-->

<!--CODE-->

<!-- f8ce3fda877b -->
### 2-5) Shortest Path Visiting All Nodes (LC 847) — BFS + 位元遮罩（Floyd-Warshall 預處理）
> 以 (node, visitedMask) 為狀態做 BFS；有需要的話先用 Floyd-Warshall 預先算好兩兩距離。

<!--CODE-->

<!--CODE-->

<!-- 16d3a3ec78d1 -->
### 2-6) Cheapest Flights Within K Stops (LC 787) — Min-Plus 矩陣次方 ⭐⭐⭐⭐⭐
> `k-i-j` 迴圈順序的規則，在這題被講得最具體。**這題直接套 Floyd-Warshall 是錯的**：它最外層的 `k` 是*中繼頂點*，不是*跳數*，所以三層迴圈跑完之後，`dist[src][dst]` 是完全不受限的最短路徑，根本沒記錄用了幾條邊。Floyd-Warshall *家族*的解法是改變第三層迴圈的意義 — 把 `k` 移到內層，就變成 **min-plus 矩陣乘法**，而它的次方數剛好就在數邊。

| | 迴圈順序 | `k` 代表什麼 | 結果 |
|---|---|---|---|
| **Floyd-Warshall** | `k` 在**最外層**，接著 `i`、`j` | 目前允許使用的中繼頂點集合 | 不受限的全點對最短路徑（邊數沒有上限） |
| **Min-plus 乘積** | `i`，接著 `k`，最後 `j`（`k` 在內層） | 把兩半接起來的那一個銜接頂點 | `C = A ⊗ B`：A 的跳數預算**加上** B 的跳數預算 |

**核心想法**：定義 `(A ⊗ B)[i][j] = min over k of (A[i][k] + B[k][j])` — 就是普通的矩陣乘法，把 `(+, ×)` 換成 `(min, +)`。這個乘積具**結合律**，所以可以用反覆平方法做次方。

令 `M[i][j]` = `i → j` 最便宜的單一航班，並且設定 **`M[i][i] = 0`** — 這個「原地不動」的自環，正是把*剛好 t 條邊*變成*最多 t 條邊*的關鍵。於是 `M^t[i][j]` = `i → j` 最多用 **`t` 個航班**的最低票價。LC 787 允許 `K` 次轉機 = `K + 1` 個航班，所以答案是 `(M^(K+1))[src][dst]`。

<!--CODE-->

<!--CODE-->

**⚠️ 溢位防護**：在 Java 裡 `INF` 要用 `Integer.MAX_VALUE / 3`（不要用 `MAX_VALUE`）— `minPlus` 會把兩個都可能是 `INF` 的項加起來，而 `MAX_VALUE + MAX_VALUE` 會繞回負數，變成一條假的「最短」路徑。

**面試現實**：以 LC 787 實際的限制（`n ≤ 100`、`k ≤ 100`）來說，分層的 O(K·E) 鬆弛更簡單也更快 — 你真正會寫的那兩種解法，請看 [Bellman-Ford](./Bellman-Ford.md) §2-1 和 [Dijkstra](./Dijkstra.md) §2-2。只有在**跳數預算 `K` 大到誇張（10⁹）而 `V` 又很小**的時候，才輪到 min-plus 次方上場，因為這時 `log K` 打得贏任何逐跳的迴圈。面試的加分點，是把它當成追問（「那如果 K 是十億呢？」）的答案講出來。

<!-- 56ebe8f7c52a -->
### 尺寸檢查：`n` 夠小到能跑 O(n³) 嗎？

決定用 Floyd-Warshall 之前先看限制 — 三次方是不留情面的。

| `n` | `n³` | 判定 |
|-----|------|---------|
| ≤ 100 | 10⁶ | 完全沒問題（LC 787、LC 1462 都在這一格） |
| ≤ 200 | 8 × 10⁶ | 沒問題（LC 1334 上限是 100） |
| ≤ 500 | 1.25 × 10⁸ | 邊緣 — Java/C++ 可以，Python 很危險 |
| ≤ 1000 | 10⁹ | 太慢了 — 改成從每個來源各跑一次 Dijkstra |
| > 1000 | ≥ 10⁹ | 這根本不是全點對問題，回去重讀題目 |

**經驗法則**：如果頂點數的限制寫的是 `n ≤ 幾百`，**而且**題目問的是很多組不同的 `(u, v)`，那出題者幾乎可以確定就是在請你用 Floyd-Warshall。

<!-- c8b75ab397b3 -->
## 依模式分類的題目

<!-- eb096c10a9f6 -->
### **全點對最短路徑類**
| 題目 | LC # | 關鍵技巧 | 難度 |
|---------|------|---------------|------------|
| Find the City With Smallest Number | 1334 | 直接套 Floyd-Warshall | Medium |
| Network Delay Time | 743 | 殺雞用牛刀，但可行 | Medium |
| Minimum Weighted Subgraph | 2203 | 三個來源 | Hard |
| Shortest Path in Undirected Graph | 1976 | 全點對距離 | Medium |

<!-- adc3058c7656 -->
### **遞移閉包類**
| 題目 | LC # | 關鍵技巧 | 難度 |
|---------|------|---------------|------------|
| Course Schedule IV | 1462 | 布林版 Floyd-Warshall | Medium |
| Graph Connectivity | 1627 | 可達性矩陣 | Hard |
| Evaluate Division | 399 | 帶權重的遞移閉包 | Medium |

<!-- df204e3cf50c -->
### **Minimax／Maximin 類**
| 題目 | LC # | 關鍵技巧 | 難度 |
|---------|------|---------------|------------|
| Path With Minimum Effort | 1631 | 改造版 Floyd-Warshall | Medium |
| Swim in Rising Water | 778 | Minimax 路徑 | Hard |
| Minimum Score of a Path | 2492 | 換掉運算子 | Medium |

<!-- d57429e01d44 -->
### **圖度量類**
| 題目 | LC # | 關鍵技巧 | 難度 |
|---------|------|---------------|------------|
| Graph Diameter | N/A | 全點對取最大 | Medium |
| Center of Star Graph | 1791 | 對距離做後處理 | Easy |
| Tree Diameter | 1522 | 樹上的全點對 | Medium |

<!-- 14cad3b3f9bb -->
## 決策框架

<!-- 9a8067de888d -->
### 什麼時候該用 Floyd-Warshall

✅ **這些情況用 Floyd-Warshall：**
- 需要全點對最短路徑
- 圖很小（V ≤ 400-500）
- 需要遞移閉包
- 需要偵測負環
- 圖很稠密（E ≈ V²）
- 實作簡單度優先
- 需要回答很多組不同點對的詢問

❌ **這些情況別用 Floyd-Warshall：**
- 只需要單源最短路徑（用 Dijkstra／Bellman-Ford）
- 圖非常大（V > 1000）
- 圖很稀疏（改成跑 V 次 Dijkstra）
- 記憶體吃緊（要 O(V²) 空間）
- 追求最快的尋路（單源的話 Dijkstra 更快）

<!-- 18a29cf21d59 -->
### 實作檢查清單

<!--CODE-->

<!-- ea4fa3974f42 -->
## 總結與速查

<!-- d5f2c210bb20 -->
### 時間／空間複雜度

| 面向 | 複雜度 | 說明 |
|--------|------------|-------|
| 時間 | O(V³) | 三層巢狀迴圈 |
| 空間 | O(V²) | 距離矩陣 |
| 前處理 | O(E) | 建相鄰矩陣 |
| 查詢時間 | O(1) | 前處理做完之後 |

<!-- 9a7472cf8045 -->
### 關鍵程式碼模式

<!--CODE-->

<!-- ab1177ab6a51 -->
### 常見變形

| 變形 | 改法 | 使用情境 |
|-----------|--------------|----------|
| **標準版** | min(dist[i][j], dist[i][k]+dist[k][j]) | 最短路徑 |
| **最長路徑** | max(dist[i][j], dist[i][k]+dist[k][j]) | 關鍵路徑 |
| **Minimax** | min(dist[i][j], max(dist[i][k], dist[k][j])) | 瓶頸路徑 |
| **Maximin** | max(dist[i][j], min(dist[i][k], dist[k][j])) | 最寬路徑 |
| **布林版** | OR/AND 運算 | 可達性 |

<!-- 249d4c848d08 -->
### 常見錯誤與提醒

**🚫 常見錯誤：**
- 迴圈順序寫錯（k 必須在最外層）
- 忘記把對角線初始化成 0
- 沒處理無向圖（兩個方向都要建）
- 負環的檢查方式不對
- 在大圖上拿 Floyd-Warshall 解單源問題

**✅ 最佳實務：**
- k 永遠放最外層（它是中繼頂點）
- 加邊之前先把 dist[i][i] 設成 0
- 無向圖記得兩個方向都加
- 檢查對角線有沒有負值來偵測環
- 如果只需要最後的距離，可以考慮省空間版本
- 只要單源的話就用 Dijkstra

<!-- 12b9918b9865 -->
### 面試提醒

1. **先辨認題型**：問清楚是單源還是全點對
2. **主動講複雜度**：一開始就講 O(V³) 時間、O(V²) 空間
3. **跟其他選項比較**：說明什麼時候 Dijkstra／Bellman-Ford 更好
4. **邊界情況**：不連通的分量、負環、自環
5. **可以優化的地方**：改成跑 V 次 Dijkstra 會不會更好？

<!-- 0ae1b1cf388c -->
### 面試中什麼時候該提 Floyd-Warshall

- 「我們需要全點對最短路徑」→ Floyd-Warshall
- 「圖很小（< 500 個頂點）」→ Floyd-Warshall 可行
- 「需要遞移閉包」→ Floyd-Warshall 最自然
- 「可以處理負權重嗎？」→ 可以，這點跟 Dijkstra 不同
- 「那更大的圖呢？」→ 跑 V 次 Dijkstra，或改用 Johnson 演算法

<!-- e55549b0d0e7 -->
### 相關演算法

- **[Dijkstra](./Dijkstra.md)**：單源，稀疏圖上更快，不支援負權重
- **[Bellman-Ford](./Bellman-Ford.md)**：單源，支援負權重，比較慢
- **Johnson's Algorithm**：用重新加權 + Dijkstra 做全點對，O(V²logV + VE)
- **Warshall's Algorithm**：遞移閉包的布林版本
- **Path Matrix Multiplication**：另一種 O(V³logV) 的做法
