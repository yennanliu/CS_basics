<!-- 942223f9ea59 -->
# 圖論實作範例

> **範圍** — [graph.md](./graph.md) 的解法歸檔：針對格子圖、複製圖、連通性、比值圖、隱式 DAG 與「依屬性做併查集」這幾類題目，每題各給一份標準解，本身不包含任何模板或理論內容。
> **另見**：[graph.md](./graph.md) — 表示法、走訪、連通性與環偵測，以及下面每份解法所實例化的那些模板；[graph_advanced.md](./graph_advanced.md) — Tarjan、尤拉路徑、最大流與二分圖的進階題材；[dfs_examples.md](./dfs_examples.md) 與 [bfs_examples.md](./bfs_examples.md) — 其中幾題從走訪速查表的角度再解一次；[union_find.md](./union_find.md) — 併查集那份文件自己對 LC 323 / 947 / 1319 的處理。

<!-- b784189b9ec9 -->
## LeetCode 題目清單

- [Graph Theory](https://leetcode.com/problem-list/graph/)
- [Depth-First Search](https://leetcode.com/problem-list/depth-first-search/)
- [Breadth-First Search](https://leetcode.com/problem-list/breadth-first-search/)
- [Union Find](https://leetcode.com/problem-list/union-find/)

<!-- 5690c44312a9 -->
## 總覽

每個條目都會標明它實例化的是 [graph.md](./graph.md) 裡的哪一份模板，
好讓這份文件維持在「歸檔」的定位，而不是變成第二份教學文件。

| # | 題目 | LC | 實例化的模板 |
|---|---|---|---|
| 2-1 | Number of Islands | 200 | 模板 2 — 在格子上做 DFS 淹沒填色 |
| 2-2 | Max Area of Island | 695 | 模板 2 — 回傳計數的 DFS |
| 2-3 | Closest Leaf in a Binary Tree | 742 | 樹 → 無向圖，再做 BFS（模板 1） |
| 2-4 | Number of Connected Components | 323 | 模板 5 — 反覆走訪求連通分量 |
| 2-5 | Clone Graph | 133 | 模板 1 / 2 搭配一個 `{original: copy}` map |
| 2-6 | Bus Routes | 815 | 模板 1 — 在抽象（路線層級）圖上做 BFS |
| 2-7 | Course Schedule | 207 | 模板 5 — 有向圖環偵測 |
| 2-8 | Find Eventual Safe States | 802 | 模板 5 — 帶記憶化狀態的環偵測 |
| 2-9 | Evaluate Division | 399 | 隱式加權圖 + DFS 連乘 |
| 2-10 | Longest Increasing Path in a Matrix | 329 | 隱式 DAG + 記憶化 DFS |
| 2-11 | Most Stones Removed | 947 | 模板 3 — 對共用屬性做併查集 |
| 2-12 | Possible Bipartition | 886 | 模板 6 — 對衝突圖做二著色 |

<!-- 598a9bfd2a3c -->
## LC 範例

<!-- a3d1fc84c88c -->
### 2-9) Evaluate Division — LC 399

**關鍵想法**：當輸入是一串*關係式*（`a / b = 2.0`）時，這個圖是**隱式的** — 節點就是你從輸入裡逐步發現的那些字串。權重要存**雙向**（`w` 和 `1/w`），然後沿著 DFS 路徑把權重連乘起來；一次查詢就只是在問「有沒有路徑，以及它的乘積是多少？」。

<!--CODE-->

<!--CODE-->

**地雷**：
- 只有當 `a` 曾出現在等式裡，`a / a` 才是 `1.0`；沒見過的變數一律是 `-1.0`。
- 這個乘法權重也讓本題成為一道**加權併查集**題（存 `weight[x] = x 的值 / parent[x] 的值`），那是每次查詢 O(1) 的版本。

**面試訊號**：「給你比值／換算／匯率，回答一堆查詢」→ 加權圖 DFS（或加權併查集）。

---

<!-- 7bcd0303b661 -->
### 2-10) Longest Increasing Path in a Matrix — LC 329

**關鍵想法**：一個只允許走向**嚴格更大**數值的格子圖就是一張 **DAG**（不可能有環，因為數值嚴格遞增）。在 DAG 上就可以做記憶化：`dp[cell] = 從這一格出發的最長遞增路徑`。少了「嚴格遞增」這個保證，就得處理環 — 這正是面試官會探的 DFS 與 DP 的分界線。

**為什麼不需要 `visited` 集合**：嚴格不等式本身已經擋掉了在當前路徑上重訪同一格，所以記憶化陣列同時兼任快取與已訪標記。

<!--CODE-->

<!--CODE-->

**另一種做法（拓撲／剝層）**：把出度為 0 的格子當成匯點，在反向 DAG 上跑 Kahn 演算法；BFS 的層數就是答案。同樣是 O(m·n)，而且沒有遞迴深度的風險。

**面試訊號**：「最長路徑」在一般圖上是 NP-hard，但在 **DAG 上是線性的** — 在宣稱 O(V+E) 之前，一定要先講清楚這張圖為什麼無環。

---

<!-- b2ac12bab4fb -->
### 2-11) Most Stones Removed with Same Row or Column — LC 947

**關鍵想法**：有時候邊並沒有直接給你 — 兩個項目之所以相連，是因為它們**共用某個屬性**（同一列、同一行、同一個 email、同一個等式變數）。暴力比較所有配對是 O(n²)。改成**把屬性本身也當成一個併查集節點**，然後 union `item ↔ attribute`。共用同一個屬性的項目會透過遞移關係落在同一個連通分量裡，而且接近線性時間。

**命名空間技巧**：列和行都是整數，所以不能撞在一起。行的部分用 `~c`（或 `c + OFFSET`，或一個 tuple／字串 key）。

**LC 947 的洞見**：在一個有 `k` 顆石頭的連通分量裡，你一定可以移除其中 `k - 1` 顆（照反向 DFS 順序一顆顆剝掉，留下最後一顆），所以答案是 `n - (連通分量數)`。

<!--CODE-->

<!--CODE-->

<!-- 417021b4ab9c -->
#### 變化題：計算連通分量數 + 多餘的邊 — LC 1319

*轉折*：問的不是「我能移除幾個」，而是「我手上有幾條**多餘**的邊，夠不夠把這些連通分量接起來」。

<!--CODE-->

<!--CODE-->

**面試訊號**：「因為共用 X 而相連」（列／行、email、帳號、變數）→ 把 X 變成一個併查集節點，而不是去建 O(n²) 條邊。同一招也能解 LC 721 Accounts Merge 和 LC 990 Satisfiability of Equality Equations。

---

<!-- e189dd02613e -->
### 2-12) Possible Bipartition — LC 886

*轉折*：這張圖不是以「既有節點集上的一堆邊」交到你手上 — 你要先從 `dislikes` 的配對建出衝突圖，然後跑跟 LC 785 一樣的二著色 DFS（[graph.md](./graph.md) 裡的模板 6）。

<!--CODE-->

<!-- c08d129fa26b -->
## 總結

- **格子題（LC 200、695）** 是同一套 DFS，只差在回傳型別：只數連通分量時用 `void`，
  要在分量上做彙總時用 `int`。
- **樹 → 圖（LC 742）** — 為每條子節點的邊補上一條指向父節點的邊，找葉節點就變成
  從目標節點向外的普通 BFS。
- **複製圖（LC 133）** — 那個 `{original: copy}` map *就是*已訪集合。第一次看到節點時
  建出複本；把它取出來處理時再接上它的鄰居。
- **抽象圖（LC 815）** — 選對節點的型別。節點是*路線*而不是站牌，這一步就把一道難題
  變成兩層 BFS。
- **環偵測（LC 207、802）** — 一個布林的 `visited` 是不夠的；你必須區分
  「在當前路徑上」和「已處理完，而且確定安全」。
- **隱式圖（LC 399、329、947）** — 邊從來不會直接給你。要從輸入（字串、格子、
  列／行編號）裡發現節點，邊讀邊建。
- **依屬性做併查集（LC 947、1319）** — 把共用屬性變成節點，就免掉了 O(n²) 的
  兩兩建邊。
