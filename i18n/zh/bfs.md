<!-- d1596b4372de -->
# BFS（廣度優先搜尋）

> **範圍** — BFS 的主要參考文件：佇列模板、逐層展開、格子與多源 BFS，以及為什麼在無權圖上「第一次拜訪」就等於最短路徑；較重的變形與大量解題範例則各自放在自己的文件裡。
> **另見** — *從本文拆分出去的深入探討*：[bfs_advanced.md](./bfs_advanced.md) — 雙向 BFS、搭配 deque 的 0-1 BFS、狀態空間／隱式圖 BFS、DAG 上所有最短路徑的列舉，以及多源與各自獨立執行的差別；[bfs_examples.md](./bfs_examples.md) — 經典題目（LC 116-117 / 130 / 279 / 286 / 623 / 662 / 742 / 752 / 773 / 909 / 958），依本文的模板分組。
> *相鄰文件*：[dfs.md](./dfs.md) — 深度優先的對照組，以及該怎麼選；[graph.md](./graph.md) — 圖的表示法與題型總覽；[Dijkstra.md](./Dijkstra.md) — 一旦邊有了權重就看這裡；[topology_sorting.md](./topology_sorting.md) — Kahn 演算法本質上就是 BFS。

<!-- c78b67f28ffa -->
## LeetCode 題目清單

- [Breadth-First Search](https://leetcode.com/problem-list/breadth-first-search/)
- [Graph Theory](https://leetcode.com/problem-list/graph/)

<!-- 926dd8cf0897 -->
## 總覽
廣度優先搜尋是一種逐層探索節點的圖走訪演算法：先拜訪完當前深度的所有節點，才前進到下一個深度。

<!-- ce426816332b -->
### 關鍵性質
- **完備**：只要解存在就一定找得到
- **最佳**：在 `unweighted`（無權）圖上能找到最短路徑
- **複雜度**：見下方 [Time & Space Complexity](#time--space-complexity) 表格

<!-- 45edc5271433 -->
### 核心特徵
- 使用**佇列**資料結構（FIFO，先進先出）
- 在無權圖上保證**最短路徑**
- **逐層**探索節點（先廣後深）
- 相較於 DFS 較耗記憶體

<!-- 5374dfc821da -->
### 節點狀態（用於偵測環）
- **狀態 0**：尚未拜訪（白）
- **狀態 1**：處理中（灰） 
- **狀態 2**：已處理完畢（黑）

<!-- dfc9d402c519 -->
### BFS vs DFS

<!-- 2f3656fbcc61 -->
#### BFS（廣度優先搜尋）
- 使用**佇列**
- 順序：**FIFO**（先進先出）
- 運作方式：拜訪一個節點 → 把所有鄰居加入佇列 → 依加入順序處理
- 想成：**逐層走訪**

<!-- a6dcad7fecff -->
#### DFS（深度優先搜尋）
- 使用**堆疊**（顯式，或透過遞迴）
- 順序：**FILO / LIFO**（後進先出）
- 運作方式：沿著一條路徑盡量往深處走 → 需要時再回溯
- 想成：**先走深，再回溯**

| 面向 | BFS | DFS |
|--------|-----|-----|
| 資料結構 | 佇列（FIFO） | 堆疊／遞迴（LIFO） |
| 走訪順序 | 逐層 | 先走深路徑，再回溯 |
| 記憶體 | O(w) — 樹的寬度 | O(h) — 樹的高度 |
| 最短路徑 | ✅ 是（無權） | ❌ 否 |
| 完備性 | ✅ 是 | ❌ 否（無限空間時） |
| 使用時機 | 最短路徑、逐層走訪 | 探索所有路徑、拓撲排序、偵測環 |

<!-- d2e7264bf49d -->
## 辨識

BFS 的訊號是**成本相同的移動，求最少次數**，或任何「一層一層」的說法。
拿限制對照[限制尺規](./complexity_cheatsheet.md#6-interview-decision-guide)：
BFS 是 O(V + E)，所以大約 10⁶ 格以內的圖或格子都裝得下。

| 敘述裡的訊號 | 限制提示 | 該用 | 不是它的情況 |
|---|---|---|---|
| 「層序」、「每一層」、「右側視角」、「最小深度」 | 節點 ≤ 10⁵ | [Template 1](#template-1-level-order-bfs--lc-102-) — 層序 BFS | 由葉子往上彙總的值 → [dfs.md § Template 6](./dfs.md#template-6-bottom-up-post-order-dfs--lc-543-) |
| 「最少步數／邊數」、「是否存在路徑」，每一步成本都是 1 | V + E ≤ 10⁵ | [Template 2](#template-2-graph-bfs-mark-on-enqueue--lc-1971-) — 圖 BFS，入列時標記 | 每一步成本不同 → [Dijkstra.md](./Dijkstra.md)；成本只有 0 或 1 → 0-1 BFS（[bfs_advanced.md](./bfs_advanced.md)） |
| 「二元矩陣／迷宮中的最短路徑」 | 格子 ≤ 10⁶ 格 | [Template 3](#template-3-grid-bfs-with-a-direction-array--lc-1091-) — 搭配 `dirs` 的格子 BFS | 每格有各自成本（體力、高度）→ Dijkstra（LC 1631） |
| 「腐爛」、「從每個來源同時擴散」、「到最近的 0／門的距離」 | 格子 ≤ 10⁵ 格 | [Template 4](#template-4-multi-source-bfs--lc-994-) — 多源 BFS | 每個來源的距離要**加總** → 各自獨立的 BFS（LC 317，[bfs_advanced.md](./bfs_advanced.md)） |
| 「把一個單字／鎖的狀態轉成另一個，每步只改一處」 | L ≤ 10、N ≤ 5,000 | [Template 5](#template-5-word-ladder-bfs-over-generated-neighbours--lc-127-) — 對即時產生的鄰居做 BFS | 要找出所有最短序列 → BFS + DFS（LC 126） |
| 「距離 `target` 為 k 的節點」、「從 `start` 感染整棵樹要多久」 | 節點 ≤ 10⁵ | [Template 6](#template-6-parent-map--bfs-radiating-from-a-target--lc-863-) — 父節點表，再 BFS | 距離是從**根**算 → Template 1 |
| 「分成兩組」、「是否為二分圖」 | n ≤ 100 | [graph.md § Template 6](./graph.md#template-6-bipartite-check-2-coloring--lc-785-) — BFS 二著色 | 三組以上 → 回溯著色 |
| 「最多移除 k 個障礙物的最短路徑」、「收集所有鑰匙」 | 狀態 ≤ 10⁶ | 在 `(cell, resource)` 狀態上跑 Template 2（[bfs_advanced.md](./bfs_advanced.md) Pattern 16） | 資源無上限 → 一般格子 BFS |

<!-- d748cff22ef8 -->
## 時間與空間複雜度

<!-- 32f1e07eac38 -->
### BFS 時間複雜度分析

BFS 的時間複雜度取決於圖的表示法：

<!-- 7acf855d33d6 -->
#### 圖的表示法

**鄰接串列（實務上最常見）：**
- 每個頂點入列／出列各一次 → O(V)
- 每條邊最多被檢視一次 → O(E)
- ✅ **總計 = O(V + E)**

**鄰接矩陣：**
- 檢查一個頂點的所有鄰居要 O(V)
- 對所有頂點都做一次要 O(V²)
- ✅ **總計 = O(V²)**

<!-- d01d67a411a9 -->
#### 依資料結構的細部拆解

**樹的 BFS**
- **時間**：O(n) - 每個節點拜訪一次
- **空間**：O(w) - 樹的最大寬度
- **說明**：每個節點恰好被拜訪一次，佇列最多存放一層

**圖的 BFS（鄰接串列）**
- **時間**：O(V + E) - 每個頂點與每條邊各處理一次
- **空間**：O(V) - 佇列與 visited 集合
- **說明**：
  - 頂點處理：每個頂點入列／出列各一次 = O(V)
  - 邊的處理：每條邊檢視一次 = O(E)
  - 佇列空間：最多 O(V) 個頂點
  - visited 集合：O(V) 個頂點

**圖的 BFS（鄰接矩陣）**
- **時間**：O(V²) - 檢查所有可能的邊
- **空間**：O(V) - 佇列與 visited 集合
- **說明**：
  - 對每個頂點，檢查全部 V 個可能鄰居
  - 頂點總數 × 每個頂點的鄰居數 = V × V = O(V²)

**格子的 BFS**
- **時間**：O(m × n) - 每一格拜訪一次
- **空間**：O(m × n) - 最差情況的佇列大小
- **說明**：
  - 每一格最多被拜訪一次
  - 最差情況下佇列可能裝下所有格子
  - 格子本質上就是一張有 m×n 個頂點、四方向邊的圖

<!-- ae844445b44a -->
#### 效能比較表

| 圖的類型 | 表示法 | 時間複雜度 | 空間複雜度 | 適用情境 |
|------------|----------------|-----------------|------------------|----------|
| **稀疏圖** | 鄰接串列 | O(V + E) | O(V) | E << V² |
| **稠密圖** | 鄰接矩陣 | O(V²) | O(V²) | E ≈ V² |
| **樹** | 父子連結 | O(n) | O(w) | 階層式資料 |
| **格子** | 二維陣列 | O(m × n) | O(m × n) | 空間類問題 |

<!-- e594caf370f7 -->
#### 鄰接串列為什麼是 O(V + E)？

<!--CODE-->

<!-- bea51ad8cdb5 -->
## 實作模式

> 兩份衛星文件各自保留自己的 `Pattern N` 編號：**Pattern 4.5、4.6、6、8、8.5、9、12、15 與 16** 是深入探討，放在 [bfs_advanced.md](./bfs_advanced.md)；解題範例放在 [bfs_examples.md](./bfs_examples.md)，依下面的模板分組。當這兩份文件引用住在這裡的模式時，用的是本文件的舊編號：
>
> | 舊名稱 | 本頁對應的卡片 |
> |---|---|
> | Pattern 1（基本的樹 BFS）、Pattern 2（逐層） | [Template 1](#template-1-level-order-bfs--lc-102-) |
> | Pattern 3（圖 BFS）、Pattern 3.1（入列前標記）、Pattern 5（路徑追蹤） | [Template 2](#template-2-graph-bfs-mark-on-enqueue--lc-1971-) |
> | Pattern 3.2（方向陣列） | [Template 3](#template-3-grid-bfs-with-a-direction-array--lc-1091-) |
> | Pattern 4（多源） | [Template 4](#template-4-multi-source-bfs--lc-994-) |
> | Pattern 7（word ladder） | [Template 5](#template-5-word-ladder-bfs-over-generated-neighbours--lc-127-) |
> | Pattern 11（父節點表，從目標向外輻射） | [Template 6](#template-6-parent-map--bfs-radiating-from-a-target--lc-863-) |
> | Pattern 13（二著色） | [graph.md § Template 6](./graph.md#template-6-bipartite-check-2-coloring--lc-785-) |

<!-- b0b1253b8bed -->
### Template 1：層序 BFS — LC 102 ⭐⭐⭐⭐⭐

**辨識** — 「一層一層」、「層序」、「右側視角」、「之字形」、「每層的平均」、「最小深度」。節點 ≤ 2,000（LC 102）到 10⁵ → 預期 O(n)。

**關鍵** — 每一輪 `while` 開頭，佇列裡恰好是完整的一層，所以在內層迴圈**之前**讀到的 `len(queue)` 就是這一層的大小；內層迴圈中加入的子節點屬於下一層，排在它後面等。

<!--CODE-->

<!--CODE-->

**複雜度**
- **時間 O(n)**：每個節點加入一次、彈出一次；內層 `for` 只是把同樣的 n 次彈出切成一層一層。
- **空間 O(w)**：佇列最多只放一整層，加上下一層已建好的部分。完全二元樹的最後一層約 n/2，所以最壞是 O(n)。

**試跑**

<!--CODE-->

**陷阱**
- Java 的 `for (int i = 0; i < queue.size(); i++)` 每次重讀的大小會隨著子節點加入而變大：`[1,2,3]` 會回傳 `[[1,2],[3]]`。先把大小存進變數。
- 拿 `list.pop(0)` 當佇列，每次彈出 O(n)，10⁵ 個節點的樹就變 O(n²)。用 `deque.popleft()`。
- 空樹：`[]` 必須回傳 `[]` 而不是 `[[]]` — 靠 `if not root` 這道防線。
- 不需要分層時，拿掉內層 `for`：單純的 BFS 順序就是這個模板去掉 `level_size`。

**不適用時** — 答案是由葉子往上彙總（高度、直徑、是否平衡）→ 後序 DFS（[dfs.md § Template 6](./dfs.md#template-6-bottom-up-post-order-dfs--lc-543-)）。LC 104 Maximum Depth 用數層數也能解，但遞迴更短。

**練習** — LC 102 ⭐ · LC 111 *（在第一個葉節點就回傳：見下方變形）* · LC 199 *（保留每層最後一個節點）* · LC 103 *（每隔一層反轉）* · LC 662 *（攜帶堆積索引）*

<!-- 4ec4f3debe77 -->
#### Minimum Depth of Binary Tree — LC 111

> **關鍵** — 佇列是依深度順序吐出每一層的，所以**第一個出隊的葉節點就是最淺的葉節點**：
> 當場回傳它的深度。DFS 必須走完每個節點才知道最小值；BFS 在第一個葉節點就停下來。

**歸約到**：[Template 1](#template-1-level-order-bfs--lc-102-) — 在第一個沒有子節點的節點處提前回傳。

只在一個地方解，連同陷阱：[tree_examples.md § LC 111](./tree_examples.md#minimum-depth-of-binary-tree--lc-111)。

**陷阱**
- 只有一個子節點的節點**不是**葉節點：`[1, 2]` 的最小深度是 2，不是 1。判斷條件是
  「沒有左子**而且**沒有右子」。
- LC 104 Maximum Depth 正好相反：每一層都必須看過，所以用遞迴比較短。

> 另外兩個層序 BFS 的變形 —— 帶著**堆積索引**（LC 662），以及**把 `null` 子節點也入隊**（LC 958）—— 在 [bfs_examples.md](./bfs_examples.md)。

<!-- 2b2495e4336b -->
### Template 2：圖 BFS，入列時標記 — LC 1971 ⭐⭐⭐⭐⭐

**辨識** — 「是否存在路徑」、「最少步數／邊數／移動次數」，每一步成本相同，圖以邊列表或鄰接串列給出。V + E ≤ 4·10⁵（LC 1971）→ 一趟 O(V + E)。

**關鍵** — 節點在**入列的那一刻**就被標記，所以最多進佇列一次，而且它第一次拿到的 `dist` 就是最終值：BFS 按距離不遞減的順序彈出節點，所以第一次抵達走的就是最短路徑。你決定某個鄰居該進佇列的那一刻，就是你做出承諾的那一刻 — 標記它、更新計數器、變更格子。這是正確性規則，不是優化。

<!--CODE-->

<!--CODE-->

**複雜度**
- **時間 O(V + E)**：建串列時每條邊讀兩次；`nxt not in dist` 這道防線讓每個節點只進佇列一次，所以 `for nxt in graph[node]` 整趟下來每個串列只讀一次。
- **空間 O(V + E)**：鄰接串列；`dist` 與佇列是 O(V)。

**試跑** — 為什麼標記要在入列時做，而不是出列時

<!--CODE-->

**陷阱**
- 在出列時才標記，同一層的兩個鄰居會把同一個節點都入列：拜訪時更新的計數器會動兩次（LC 994 的 `fresh` 變負數），距離可能被較長的值覆寫（LC 286），稠密圖上佇列會長到 O(E)。
- 無向邊列表只加了一個方向：`n = 2, edges = [[1,0]], source = 0, destination = 1` 會回傳 False。
- 沒有任何邊時 `source == destination` 也必須是 True：檢查是在彈出時做的，而起點第一個被彈出。

入列時標記決定對錯的地方：

| 題目 | 出列時才標記會壞掉什麼 |
|---|---|
| LC 994 Rotting Oranges | 每個重複項都跑一次 `fresh -= 1`，`fresh` 最後低於零 |
| LC 542 01 Matrix、LC 1162 As Far from Land | 距離必須在第一次、也就是最短的那次抵達時設定 |
| LC 286 Walls and Gates | 房間距離被較長的路徑覆寫 |
| LC 200 Number of Islands（BFS 寫法） | 同一塊陸地被再次展開 |
| LC 934 Shortest Bridge | 前緣的水格被算兩次 |
| LC 127 Word Ladder | 每個能抵達它的鄰居都會把同一個字入列一次 |

**不適用時** — 邊權重不同 → Dijkstra（[Dijkstra.md](./Dijkstra.md)）；權重只有 0 和 1 → 搭配 deque 的 0-1 BFS（[bfs_advanced.md](./bfs_advanced.md) Pattern 15）。只問可達性且遞迴深度不大 → DFS 也行（[graph.md § Template 2](./graph.md#template-2-dfs-reachability--lc-841-)）。

**練習** — LC 1971 · LC 841 *（從 0 號房間出發的可達性）* · LC 1091 *（格子，8 個方向：Template 3）* · LC 433 *（鄰居即時產生：Template 5）* · LC 1293 *（狀態 = 格子 + 剩餘可消除數）*

<!-- c37e252e4d9e -->
#### 變形：回傳路徑，而不只是長度（父節點表）

記下每個節點是誰把它入列的；目標被彈出後，順著這些父節點走回起點。改在佇列裡攜帶 `path + [neighbor]` 也行，但每次入列都要複製一條路徑，長度 L 的路徑要 O(V · L) 記憶體。

<!--CODE-->

<!-- 24a0cf76eb4e -->
### Template 3：搭配方向陣列的格子 BFS — LC 1091 ⭐⭐⭐⭐

**辨識** — 「二元矩陣／迷宮／格子中的最短路徑」、「從左上到右下的最少步數」，往 4 或 8 個鄰格移動，每步成本 1。格子 ≤ 100 × 100（LC 1091）→ O(m·n)。

**關鍵** — 格子就是圖：一格是一個節點，`dirs` 陣列列出它的邊。入列時就把該格寫成 `1`，格子本身就成了 visited 集合，所以每一格只入列一次，目標第一次被彈出時的 `dist` 就是最短。

<!--CODE-->

<!--CODE-->

**複雜度**
- **時間 O(n²)**：n² 格中每一格最多入列一次，`for dr, dc in dirs` 迴圈每格固定 8 次。
- **空間 O(n²)**：佇列可能裝下一整條前緣的格子；原地標記不花額外空間。

**陷阱**
- 答案算的是**格數**而不是步數：`[[0]]` 回傳 1，所以起點以 `dist = 1` 入列。
- 起點或終點被擋住：`[[1,0],[0,0]]` 回傳 -1。放入佇列前兩個都要先檢查。
- 先檢查邊界再讀值：Python 裡 `grid[-1][c]` 會默默讀到最後一列，所以要像 `and` 串那樣先測 `0 <= nr < n`。

**不適用時** — 每格移動成本不同（LC 1631 體力、LC 1368 箭頭成本 0 或 1）→ Dijkstra 或 0-1 BFS。問的是「**每一格**到最近的 X」→ 多源 BFS（[Template 4](#template-4-multi-source-bfs--lc-994-)）。

**練習** — LC 1091 · LC 1926 *（最近的出口，4 個方向）* · LC 1730 *（到食物的最短路徑）* · LC 490 *（球一路滾到牆：鄰居是停下來的格子）* · LC 1293 *（狀態多了剩餘可消除數）*

<!-- 03336acbf621 -->
### Template 4：多源 BFS — LC 994 ⭐⭐⭐⭐⭐

**辨識** — 「橘子腐爛」、「到最近的 0／門／陸地的距離」、「火或感染從每個來源同時擴散」、「最少要幾分鐘」。格子 ≤ 10⁵ 格 → 一次 O(m·n) 的 BFS，絕不是每個來源各跑一次。

**關鍵** — 把每個來源都放在第 0 層，等於從一個連到所有來源的虛擬超級來源做 BFS，所以每一格最先被離它最近的來源抵達，而一層就是一分鐘。

<!--CODE-->

<!--CODE-->

**複雜度**
- **時間 O(m·n)**：初始化的迴圈把每一格讀一次，入列時標記這道防線讓每顆新鮮橘子只進佇列一次。
- **空間 O(m·n)**：佇列可能裝下所有格子。

**試跑**

<!--CODE-->

**陷阱**
- 在一層開頭做 `minutes += 1`，`while` 條件就必須有 `fresh > 0`：少了它，最後那層（什麼也沒腐爛）會多加一分鐘，上面的例子會回傳 5。另一種正確寫法是在一層**結尾**遞增，而且只在這一層真的有東西腐爛時才加（一個 `rotted_this_minute` 旗標）。兩種混用，或兩種都沒做，就會把最後那個空層也算進去。
- 完全沒有新鮮橘子：`[[0,2]]` 回傳 0 而不是 -1 — `while` 根本不會執行。
- 每顆腐爛橘子各跑一次 BFS 再取最小值，數字是對的但慢 k 倍，來源一多就逾時。
- 在彈出時才讓它腐爛：同一層的兩個腐爛鄰居都會把它入列，`fresh` 變負數（[Template 2](#template-2-graph-bfs-mark-on-enqueue--lc-1971-)）。

**不適用時** — 每個來源的距離要各自加總（LC 317 Shortest Distance from All Buildings）→ 各自獨立的 BFS，每個來源用一個全新的 `visited`（[bfs_advanced.md](./bfs_advanced.md) Pattern 4.6）。邊有權重 → 以所有來源為起點的 Dijkstra。

**練習** — LC 994 ⭐ · LC 542 *（下方的距離變形）* · LC 286 · LC 1162 · LC 417 ⭐ *（每個海洋各一次多源 BFS，再取交集）* · LC 934 *（先 DFS 一座島，再從整座島 BFS）*

<!-- f4ae23ea1309 -->
#### 01 Matrix — LC 542

> **關鍵** — 「每個 1 到最近的 0 有多遠」等同於「每個 0 能擴散多遠」：把**所有** 0 格以距離 0
> 一起放進佇列，跑一次 BFS。一個格子第一次被走到時的值就是它的距離，所以每格只寫一次 ——
> O(m·n)，而不是每個 1 格各跑一次 BFS。

**歸約到**：[Template 4](#template-4-multi-source-bfs--lc-994-) — 用 `dist` 格子取代分鐘計數器：
距離是逐格寫入，而不是逐層。

<!--CODE-->

<!--CODE-->

**陷阱**
- 每個 1 格各跑一次 BFS 是 O((m·n)²)：m·n = 10⁴ 時就會超時。把方向反過來，從 0 出發。
- 格子要在**入隊時**標記（`dist[nr][nc] == -1` → 寫入），而不是出隊時，否則同一格會被好幾個
  鄰居重複放進佇列。

<!-- 24eedc89b09a -->
### Template 5：對即時產生的鄰居做 Word Ladder BFS — LC 127 ⭐⭐⭐⭐⭐

**辨識** — 「一次改一個字母，把一個單字轉成另一個」、「打開鎖的最少步數」、「每一步只改一個位置」，並給一份合法狀態的字典或 `deadends` 清單。字長 L ≤ 10、單字數 N ≤ 5,000 → 產生一個字的 26·L 個鄰居，而不是比較全部 N² 對。

**關鍵** — 圖從來不必建出來：一個字的鄰居是一次改一個位置產生的，只留下在字典裡的。每個字入列時就被標記，所以只展開一次，而 BFS 的層數就是轉換次數。

<!--CODE-->

<!--CODE-->

**複雜度**
- **時間 O(N · 26 · L²)**：`visited` 讓每個字只展開一次；展開時在 L 個位置各試 26 個字母，而建立並雜湊每個候選字串要 O(L)。
- **空間 O(N · L)**：單字集合、`visited` 與佇列各自最多放 N 個長度 L 的字。

**陷阱**
- `endWord` 不在 `wordList` 裡 → 立刻回傳 0。不然 LC 127 的第二個範例（少了 `"cog"`）會白白搜完整本字典。
- 答案算的是**單字數**而不是邊數：`hit → hot → dot → dog → cog` 是 5，所以 `steps` 從 1 開始。
- Java 的字元陣列寫法必須在試完 26 個字母後還原 `arr[j] = original`，否則試位置 `j + 1` 時，位置 `j` 還留著錯的字母。
- 兩兩比較所有單字來建圖是 O(N² · L)，N = 5,000 時約 2.5·10⁸；即時產生鄰居才能讓它對 N 維持線性。

**不適用時** — 要找出每一條最短序列（LC 126）→ 先用 BFS 建出分層 DAG，再在上面 DFS（[bfs_advanced.md](./bfs_advanced.md) Pattern 8.5）。兩端都已知且分支很大 → 雙向 BFS（[bfs_advanced.md](./bfs_advanced.md)）。

**練習** — LC 127 ⭐ · LC 433 *（4 種字母、8 個位置）* · LC 752 *（一把鎖：每個轉輪 ±1，deadends 是牆）* · LC 126 *（所有最短序列）* · LC 854 *（鄰居由交換兩個字母產生）*

<!-- c4e1ecd47f02 -->
### Template 6：父節點表 + 從目標向外輻射的 BFS — LC 863 ⭐⭐⭐⭐⭐

**辨識** — 「距離 `target` 為 k 的所有節點」、「感染從節點 `start` 擴散所需時間」、「離節點 k 最近的葉節點」：距離是從一個**不是根**的節點量起，答案可能包含祖先或兄弟子樹裡的節點。節點 ≤ 500（LC 863）到 10⁵（LC 2385）。

**關鍵** — 二元樹只存向下的指標。做一次 DFS 記錄 `{node: parent}`，補上缺少的邊方向，於是每個節點最多三個鄰居 — `(left, right, parent)` — 接著從 `target` 向外 BFS，因為每條邊成本都是 1，就得到每個節點精確的樹上距離。

樹只儲存**向下**的指標（`left`、`right`），但距離 `target` 為 `k` 的節點可能出現在**三個**地方：在它下面、在它**上面**，或在**兄弟子樹**裡（先上後下）。只會往下走的 DFS 永遠碰不到它們。有兩件事讓這個 BFS 正確：

| 元素 | 為什麼不可省 |
|---|---|
| `visited` 集合 | 加上父邊之後圖變成**無向**的 → BFS 會在 `child → parent → child` 之間永遠來回。單純由上而下的樹走訪從不需要 `visited`；這裡則一定要。 |
| `dist == k` 時 `continue` | 超過 `k` 的節點無關緊要，而且它們唯一的回頭路徑得經過一個已被收集的節點。停止展開能限制工作量，也避免多收。 |

<!--CODE-->

<!--CODE-->

**複雜度**
- **時間 O(n)**：`add_parents` 每個節點拜訪一次，`visited` 防線讓每個節點只進佇列一次；每個節點最多試 3 個鄰居。
- **空間 O(n)**：父節點表、`visited` 與佇列；DFS 另外佔 O(h) 的遞迴。

**試跑** — `root = [3,5,1,6,2,0,8,null,null,7,4]`、`target = 5`、`k = 2`

<!--CODE-->

**陷阱**
- 沒有 `visited` 集合：迴圈仍會停（`dist == k` 的 `continue` 讓每條路最多走 `k` 條邊），但答案會錯。`k = 2` 時它走 5 → 6 → 5，`target` 自己在距離 2 又出現，同一個節點也可能被收進來好幾次。有了 `visited`，`dist` 才是最短距離。
- `dist == k` 時沒有 `continue`：答案仍然正確，因為只收集 `dist == k` 的節點，但每個 `k` 都會走遍整棵樹。
- LC 863 給你的 `target` 是**節點**；LC 2385 給你的 `start` 是**值**。在 BFS 開始前，先在建父節點表的 DFS 中找到那個節點。
- `k = 0` 回傳 `[target.val]`：目標以 `dist == 0` 被彈出，任何展開之前就收集了。

**不適用時** — 距離是從**根**算 → 一般的層序 BFS（[Template 1](#template-1-level-order-bfs--lc-102-)）。DFS 替代解（「距離滲透」）是用後序 DFS 回傳 `target` 在各子樹中的深度；在目標上方 `d` 條邊的節點，往*另一個*子節點收集往下 `k - d` 層的節點。同樣是 O(n)，但寫錯的機率高得多 — BFS + 父節點表才是面試上穩妥的答案。節點是值而不是 `TreeNode` 物件時，改建完整的鄰接表（LC 1530 就需要；它的標準解是 [dfs_advanced.md](./dfs_advanced.md) 的後序分桶 DFS）：

| | 父節點表 `{node: parent}` | 鄰接表 `{node: [neighbors]}` |
|---|---|---|
| 建立方式 | 一次 DFS，每個節點 1 筆 | 一次 DFS，每條邊 2 筆 |
| 取得鄰居 | `(node.left, node.right, parents[node])` | `graph[node]` |
| 空間 | 約 n 個指標 | 約 2(n-1) 個串列項目 |
| 使用時機 | 節點是可直接解參照的**真實 `TreeNode` 物件** | 你操作的是**值**，或結構本身不是二元樹 |

**練習** — LC 863 ⭐ · LC 2385 *（答案是最後一層 BFS）* · LC 742 *（在彈出第一個葉節點時停）* · LC 1740 *（從一個節點 BFS 直到另一個被彈出）* · LC 2096 *（同樣先上後下的路徑，改成重建路徑）*

| 題目 | LC # | 與本模式的關聯 |
|---------|------|----------------------|
| All Nodes Distance K in Binary Tree | 863 | **標準題** — 父節點表 + 向外 BFS `k` 步 |
| Amount of Time for Binary Tree to Be Infected | 2385 | 同一套父節點表 BFS；答案 = **最大**距離（最後一層 BFS） |
| Closest Leaf in a Binary Tree | 742 | 從目標向外 BFS，在彈出**第一個葉節點**時停止（[bfs_examples.md](./bfs_examples.md)） |
| Find Distance in a Binary Tree | 1740 | 兩節點間的距離 = 從其中一個 BFS 直到另一個被彈出（或用 LCA） |
| Number of Good Leaf Nodes Pairs | 1530 | 每個葉節點各做一次有界 BFS 可行，但標準解是 [dfs_advanced.md](./dfs_advanced.md) 的後序距離分桶 |
| Step-By-Step Directions From a Binary Tree Node | 2096 | 同樣的先上後下洞見，但重建的是路徑而非距離 |
| Cousins in Binary Tree | 993 | 每個節點需要 `parent` + `depth` — 只要父節點表，不需向外輻射 BFS |
| Lowest Common Ancestor of a Binary Tree | 236 | LCA 就是先上後下路徑的「轉折點」 |
| Minimum Height Trees | 310 | 無向樹的 BFS，是往內修剪而非往外輻射（[topology_sorting_examples.md](./topology_sorting_examples.md)） |

<!-- 14a209cad3a7 -->
#### 第二種解法：剛好展開 `k` 層，佇列就是答案

佇列項目裡不放 `dist`：做 `k` 次完整的逐層展開（先記下 `len(q)`），佇列裡剩下的就恰好是距離 `k`
的集合。同樣 O(n)；`k = 0` 回傳 `[target]`，`k` 比樹還大時佇列會清空成 `[]`，都不需要特判。若同時
也需要各個距離，就用上面 `(node, dist)` 的寫法。

<!--CODE-->


> **模式重點**：一旦某題樹的問題是**從根以外的節點**去量測某件事，就別再想「樹的遞迴」，
> 改想**「無向圖」**——補上父連結（`{node: parent}` 對照表，或 `node.par` 標註），
> 接著它就是一場普通的 BFS，其中每個節點有 3 個鄰居，而 `visited` 是不可省略的。

---

<!-- 5343958f1000 -->
### 二分圖檢查（二著色）— 放在 graph.md

BFS 不一定要帶著距離；它也可以帶著一個**標籤**。最經典的例子就是二著色：把每個鄰居塗成相反的
顏色，一條兩端同色的邊就是奇數環。這張卡片 —— BFS 與 DFS 兩種寫法、Python 與 Java、不連通圖與
自環的陷阱 —— 在
[graph.md § Template 6](./graph.md#template-6-bipartite-check-2-coloring--lc-785-)，那裡是 LC 785
唯一的家；LC 886 的解題在 [graph_examples.md](./graph_examples.md)。

<!-- 772b64cd5d29 -->
## 練習路徑

| 階段 | 題目（易 → 難） | 新增的能力 |
|---|---|---|
| **核心** ⭐ | LC 102 · LC 994 · LC 127 | 逐層 BFS、多源 BFS、即時產生而非儲存的鄰居 |
| **練習** | LC 111 · LC 1971 · LC 1091 · LC 542 · LC 863 · LC 785 · LC 752 | 在第一個葉節點提早結束、邊列表、8 個方向的格子、從每個來源出發的距離、把樹當圖、二著色、把鎖當狀態圖 |
| **延伸** | LC 126 · LC 815 · LC 1293 | 所有最短路徑、以路線而非站點做 BFS、帶著預算的狀態 |

---

<!-- a3a2093b7a82 -->
## 總結與速查

> 下方的 `Pattern 4.5 / 4.6 / 6 / 8 / 8.5 / 9 / 10 / 12 / 14 / 15 / 16` 放在 [bfs_advanced.md](./bfs_advanced.md)；`§2-N` 的參照放在 [bfs_examples.md](./bfs_examples.md)。

<!-- 782a3ebc43e7 -->
### 什麼時候該用 BFS
- 在無權圖上找最短路徑
- 樹的層序走訪
- 找連通元件
- 檢查圖是否為二分圖
- 網頁爬蟲（廣度優先探索）
- **同步的多源距離計算**（Template 4）- 到最近來源的距離
- **從多個來源各自獨立執行 BFS**（Pattern 4.6）- 到所有來源的距離總和

<!-- 5049c1eeee98 -->
### 什麼時候不該用 BFS
- 很深的樹／圖，而記憶體有限
- 只需要找出「任一」條路徑（不必最短）
- 成本不一的加權圖（改用 Dijkstra）
- 需要探索所有路徑（改用 DFS）

<!-- b7bbbbb09835 -->
### BFS vs Dijkstra — 該用哪一個

| 判準 | BFS | Dijkstra |
|----------|-----|----------|
| **邊的權重** | 全部相同（無權）或 0/1 | 非負、且大小不一 |
| **資料結構** | 佇列（`LinkedList`） | 優先佇列（最小堆積） |
| **時間複雜度** | O(V + E) | O((V + E) log V) |
| **第一次拜訪 = 最短？** | ✅ 是（層數 = 距離） | ❌ 否（必須透過 PQ 鬆弛） |
| **「最少步數／移動次數」** | ✅ 用 BFS | ❌ 殺雞用牛刀 |
| **「最小成本／權重」** | ❌ 會得到錯誤答案 | ✅ 用 Dijkstra |
| **成本一致的格子** | ✅ BFS | ❌ 不必要的額外開銷 |
| **成本不一的格子** | ❌ | ✅ 在隱式圖上跑 Dijkstra |

**決策規則**：若每條邊成本相同（或成本為 1），就用 BFS。一旦邊有了不同的權重，就換成 Dijkstra。

**常見陷阱**：對 LC 279 Perfect Squares 或 LC 752 Open the Lock 這類所有邊成本都是 1 的題目使用 Dijkstra（PQ）——單純的 BFS 更簡單也更快。

**0-1 BFS 特例**：如果邊的權重只有 0 或 1，就用 **deque** ——權重 0 的邊推到前端，權重 1 的邊推到後端。時間跟 BFS 一樣是 O(V+E)，又能正確處理兩種權重。

<!-- 3feae5fa7921 -->
### 常見錯誤與最佳實務

<!-- 42df941b154a -->
#### 常見錯誤
1. 對 list 用 `queue.pop()` 而不是 `queue.popleft()`
2. 在圖上沒有處理 visited 集合（造成無限迴圈）
3. 需要逐層處理時卻忘了分層
4. 格子題的邊界檢查寫錯

<!-- ad3857526c2c -->
#### 最佳實務
1. 用 `collections.deque` 以獲得更好的效能
2. 圖的題目一律使用 visited 集合
3. 格子題在加入佇列前先檢查邊界
4. 考慮用多源 BFS 來優化
5. 需要最短路徑時記得追蹤層數／距離
6. **在入列前而非出列後標記狀態** — 決定要把鄰居入列的那一刻就更新 grid／visited／計數器；延到出列才做會讓多個鄰居重複把同一格入列（見上方 **Template 2**）

<!-- 4f71db84c5cf -->
### 依題型分類的題目

<!-- 37f7bc8a5c2d -->
#### 1. 樹的走訪問題
- **層序走訪**：LC 102, 107, 103
- **二元樹路徑**：LC 257, 1022
- **右視圖**：LC 199
- **垂直順序**：LC 314
- **逐層改動樹**：LC 623 (Add One Row)、LC 116/117 (Next Right Pointers)
- **距離任意節點多遠（Template 6 — 父節點表 + 向外輻射）**：LC 863 (Distance K)、LC 2385 (Tree Infection)、LC 742 (Closest Leaf)、LC 1740 (Find Distance)

<!-- 705ce0624ca1 -->
#### 2. 最短路徑問題
- **無權圖**：LC 127 (Word Ladder)
- **格子導航**：LC 1730 (Shortest Path to Food)、LC 1091 (Shortest Path in Binary Matrix)
- **同步多源距離（Template 4）**：
  - **LC 542 (01 Matrix)** - 每一格到最近的 0 的距離
  - LC 1162 (As Far from Land) - 每個水格到最近陸地的距離
  - LC 286 (Walls and Gates) - 從門到房間的距離
  - LC 994 (Rotting Oranges) - 感染擴散所需時間
- **各自獨立的 BFS（Pattern 4.6）**：
  - **LC 317 (Shortest Distance from All Buildings)** - 到所有建築物的距離總和（每次都用全新的 visited）
- **DFS + 多源 BFS（Pattern 4.5）**：LC 934 (Shortest Bridge - 標記其中一個元件，再擴張去找另一個)
- **依序處理多個目標（Pattern 6）**：LC 675 (Cut Off Trees for Golf Event - 排序 + 重複 BFS)
- **以路線為層的 BFS（Pattern 8）**：LC 815 (Bus Routes - 抵達目標所需的最少公車數／轉乘數)
- **帶狀態的 BFS**：LC 864 (Shortest Path to Get All Keys)、LC 1293 (Shortest Path with Obstacles Elimination)

<!-- 31182113c26e -->
#### 3. 圖結構問題
- **偵測環**：LC 207 (Course Schedule)
- **連通元件**：LC 200 (Number of Islands)
- **圖的驗證**：LC 261 (Graph Valid Tree)
- **複製圖**：LC 133

<!-- b52fb1747e12 -->
#### 4. 矩陣／格子問題
- **被包圍的區域**：LC 130
- **Walls and Gates**：LC 286
- **迷宮問題**：LC 490

<!-- f26e2a202713 -->
#### 5. 組合列舉問題（Pattern 9 — BFS 風格的笛卡兒積）
- **Brace Expansion (LC 1087)** — 解析成群組，再逐層 BFS
- **Letter Combinations of a Phone Number (LC 17)** — 數字 → 字母群組，笛卡兒積式 BFS
- **Letter Case Permutation (LC 784)** — 每個字元 1 或 2 個選項的群組
- **Generalized Abbreviation (LC 320)** — 每個字元「保留或跳過」的群組

<!-- 05f421f1748c -->
### 關鍵 LeetCode 題目
| 難度 | 題目 | 關鍵概念 | 核心模式 |
|------------|---------|-------------|--------------|
| Easy | LC 102 | 層序走訪 | Template 1（逐層） |
| **Medium** | **LC 127** | **最短路徑轉換 - Word Ladder** | **Template 5（無權最短路徑）** |
| Medium | LC 200 | 連通元件 | Template 2（圖 BFS） |
| Medium | LC 742 | 最近的葉節點（樹 → 無向圖） | [bfs_examples.md](./bfs_examples.md#closest-leaf-in-a-binary-tree--lc-742)（樹 → 圖 + BFS） |
| Medium | LC 863 | 距離**目標節點** `k`（父節點表、3 個鄰居） | Template 6（向外輻射），兩種寫法 |
| Medium | LC 623 | 逐層 BFS 到 `depth - 1`，再重接子節點指標 | [bfs_examples.md](./bfs_examples.md#add-one-row-to-tree--lc-623) |
| **Medium** | **LC 542** | **同步多源 - 01 Matrix** | **Template 4（同步多源）** |
| Medium | LC 934 | DFS + 多源 BFS（島嶼擴張） | Pattern 4.5（DFS + 多源） |
| Medium | LC 1162 | As Far from Land as Possible | Template 4（同步多源） |
| **Hard** | **LC 126** | **找出所有最短路徑 - Word Ladder II** | **Pattern 8.5（BFS + DFS 的 DAG 列舉）** |
| Hard | LC 286 | Walls and Gates | Template 4（同步多源） |
| **Hard** | **LC 317** | **各自獨立的 BFS（距離總和）** | **Pattern 4.6（獨立 BFS）** |
| Hard | LC 675 | 排序 + 重複 BFS（依序處理目標） | Pattern 6（排序 + 重複 BFS） |
| **Hard** | **LC 752** | **在狀態空間上做 BFS - Open the Lock** | **Template 5（無權最短路徑）；解題見 [bfs_examples.md](./bfs_examples.md#open-the-lock--lc-752)** |
| **Hard** | **LC 815** | **以路線為層的 BFS（最少公車數）** | **Pattern 8（路線層 BFS）** |
| Hard | LC 864 | 帶狀態的 BFS（收集鑰匙） | Template 2 + 狀態（[bfs_advanced.md](./bfs_advanced.md) Pattern 16） |
| Hard | LC 1293 | 帶狀態的 BFS（消除障礙物） | Template 2 + 狀態（[bfs_advanced.md](./bfs_advanced.md) Pattern 16） |

<!-- 1b108a513cba -->
### 也常被問到（沒有新模板——它們重用上面的模板）

| LC | 題目 | 重用了哪個模板 |
|----|---------|--------------------------|
| 297 / 449 | Serialize and Deserialize Binary Tree / BST | Template 1 逐層 BFS 並寫入 `null` 標記；反序列化 = 同一個佇列讀回來（見 [bfs_examples.md 的 LC 958](./bfs_examples.md#check-completeness-of-a-binary-tree--lc-958)） |
| 104 | Maximum Depth of Binary Tree | Template 1 — 數層數；這題 DFS 遞迴更短，BFS 只在 LC 111 才佔優勢 |
| 101 | Symmetric Tree | Template 1 搭配**成對佇列** — 鏡像地入列 `(left, right)`，彈出時比較 |
| 637 / 515 | Average of Levels / Largest Value in Each Tree Row | Template 1 — 把「蒐集這一層」換成「彙總這一層」（平均／最大） |
| 433 | Minimum Genetic Mutation | 與 LC 127 / 752 同一套模板 — 8 個字元的基因字串、4 種字母、bank = 合法狀態集合 |
| 529 | Minesweeper | Template 3 格子 BFS — 只有在相鄰地雷數為 `0` 時才展開該格，否則寫下數字就停 |
| 547 | Number of Provinces | Template 2 — 數要跑幾次 BFS 才能覆蓋所有節點（或用併查集） |
| 1376 | Time Needed to Inform All Employees | 在主管樹上跑 Template 1，佇列存放 `(employee, timeSoFar)` — 答案是最大值 |
| 787 | Cheapest Flights Within K Stops | BFS 的**有界層數鬆弛**（帶有 Bellman-Ford 味道）：恰好跑 `k+1` 層，而且**不要用全域 visited** — 同一個節點可能以更低成本再次進入。見 `Dijkstra.md`。 |
| 329 | Longest Increasing Path in a Matrix | 這不是 BFS 題 — 用 DFS + memo，或在 DAG 上跑 Kahn 的 BFS（見 `topology_sorting.md`） |
| 721 / 947 / 684 / 839 | Accounts Merge / Stones Removed / Redundant Connection / Similar String Groups | 這是連通性問題而非最短路徑 — 併查集才是預期解（BFS 洪水填充也行） |
