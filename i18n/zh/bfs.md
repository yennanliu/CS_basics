<!-- 9072714a3b2f -->
# BFS（廣度優先搜尋）

> **範圍** — BFS 的主要參考文件：佇列模板、逐層展開、格子與多源 BFS，以及為什麼在無權圖上「第一次拜訪」就等於最短路徑；較重的變形與大量解題範例則各自放在自己的文件裡。
> **另見** — *從本文拆分出去的深入探討*：[bfs_advanced.md](./bfs_advanced.md) — 雙向 BFS、搭配 deque 的 0-1 BFS、狀態空間／隱式圖 BFS、DAG 上所有最短路徑的列舉，以及多源與各自獨立執行的差別；[bfs_examples.md](./bfs_examples.md) — 完整解題彙整（LC 130 / 207 / 279 / 286 / 310 / 417 / 623 / 742 / 752 / 909 / 116-117 …）以及 LC 994 的計時逐步說明。
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

<!-- 1f72b660d9b1 -->
### 節點狀態（用於偵測環）
- **狀態 0**：尚未拜訪（白）
- **狀態 1**：處理中（灰） 
- **狀態 2**：已處理完畢（黑）

<!-- 2decf65a444a -->
#### 🔹 BFS（廣度優先搜尋）
- 使用**佇列**
- 順序：**FIFO**（先進先出）
- 運作方式：拜訪一個節點 → 把所有鄰居加入佇列 → 依加入順序處理
- 👉 想成：**逐層走訪**

<!-- 479ef9782e62 -->
#### 🔹 DFS（深度優先搜尋）
- 使用**堆疊**（顯式，或透過遞迴）
- 順序：**FILO / LIFO**（後進先出）
- 運作方式：沿著一條路徑盡量往深處走 → 需要時再回溯
- 👉 想成：**先走深，再回溯**

| 面向 | BFS | DFS |
|--------|-----|-----|
| 資料結構 | 佇列（FIFO） | 堆疊／遞迴（LIFO） |
| 走訪順序 | 逐層 | 先走深路徑，再回溯 |
| 記憶體 | O(w) — 樹的寬度 | O(h) — 樹的高度 |
| 最短路徑 | ✅ 是（無權） | ❌ 否 |
| 完備性 | ✅ 是 | ❌ 否（無限空間時） |
| 使用時機 | 最短路徑、逐層走訪 | 探索所有路徑、拓撲排序、偵測環 |

<!-- d748cff22ef8 -->
## 時間與空間複雜度

<!-- 32f1e07eac38 -->
### BFS 時間複雜度分析

BFS 的時間複雜度取決於圖的表示法：

<!-- d1aaa30546de -->
#### 🔹 圖的表示法

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

<!-- 1e2e7cf3236d -->
## 實作模式

> Pattern 編號在三份 BFS 文件之間保持一致。**Pattern 4.5、4.6、6、8、8.5、9、10、12、14 與 15** 是深入探討，放在 [bfs_advanced.md](./bfs_advanced.md)；`§2-N` 的解題範例放在 [bfs_examples.md](./bfs_examples.md)。

<!-- 166ba18c578b -->
### Pattern 1：基本的樹 BFS
<!--CODE-->

<!-- 3aaba9e07fc8 -->
### Pattern 2：逐層 BFS — LC 102 ⭐⭐⭐⭐⭐
<!--CODE-->

<!-- 2a3152587d09 -->
#### 變形：**彈出第一個葉節點時就回傳** — LC 111 (Minimum Depth of Binary Tree)

> **轉折**：不必走完整棵樹——BFS 會最先抵達最淺的葉節點，所以一旦出列的節點沒有子節點就立刻回傳。在一條長長的左歪斜脊上，DFS 會拜訪每個節點；BFS 在第一個葉節點就收工。（對照 LC 104 Maximum Depth：那題你*必須*看過每一層，所以 DFS 遞迴才是更俐落的工具。）

<!--CODE-->

> 另外兩個逐層 BFS 的變形——攜帶 **heap index**（LC 662）以及**把 `null` 子節點也入列**（LC 958）——放在 [bfs_examples.md](./bfs_examples.md)。

<!-- 2ca914d4e1c0 -->
### Pattern 3：搭配 visited 集合的圖 BFS — LC 200
<!--CODE-->

<!-- 5c96263bedd6 -->
### Pattern 3.1：visited 集合的擺放規則——入列前就標記 ⭐⭐⭐⭐⭐

一個關鍵的 BFS 實作細節：**永遠在把某一格加入佇列「之前」就標記為已拜訪（更新格子狀態與計數器）**，而不是等到出列時才做。

<!-- 4c62fa856e35 -->
#### 規則

<!--CODE-->

**通用 BFS 模板（標準形式）：**
<!--CODE-->

**當狀態更新不單純時（格子變更、計數器）的三步驟模式：**
<!--CODE-->

<!--CODE-->

<!-- 85ea383f7bca -->
#### 為什麼這件事重要

如果你把標記延後到出列才做，**多個鄰居可能在任何一個處理它之前就重複把同一格入列**：

<!--CODE-->

**結果**：重複計數、答案錯誤，或白白浪費運算。

<!-- 46276202fd59 -->
#### 入列前標記所保證的：

| 保證 | 說明 |
|-----------|-------------|
| **佇列中沒有重複** | 在任何其他鄰居看到它之前，該格已被標記為已拜訪 |
| **計數正確** | 每一格恰好被計算一次 |
| **O(m x n) 時間** | 每一格最多入列一次 |
| **BFS 層次正確** | 層的邊界維持準確，計時／距離才不會錯 |

<!-- c2de6a80fb1b -->
#### 適用的情況

| 情境 | 為什麼入列前標記很重要 |
|----------|-------------------------------|
| **計數**（新鮮橘子、感染數） | 避免計數器被重複遞減 |
| **計時／距離**（經過的分鐘數） | 確保該格被歸到正確的 BFS 層 |
| **格子變更**（腐爛擴散、洪水填充） | 避免同一格被處理多次 |
| **用格子值來追蹤已拜訪** | 格子本身就是 visited 集合；必須在入列前標記 |

<!-- b6f8f7066c35 -->
#### 使用獨立 `visited` 集合時

同樣的原則成立——在**入列時**加入 `visited`，而不是出列時：

<!--CODE-->

<!-- 0b8f5f34a418 -->
#### 相關 LeetCode 題目

| 題目 | 為什麼入列前標記至關重要 |
|---------|-------------------------------------|
| **LC 994** - Rotting Oranges | 計數器 `freshOrange--` 每一格必須恰好執行一次 |
| **LC 542** - 01 Matrix | 距離必須在第一次（也就是最短）拜訪時就指派 |
| **LC 286** - Walls and Gates | 房間距離不能被較長的路徑覆寫 |
| **LC 1162** - As Far from Land as Possible | 同樣是多源 BFS，距離必須在第一次抵達時設定 |
| **LC 200** - Number of Islands | 入列時就標記可避免重複拜訪同一塊陸地 |
| **LC 934** - Shortest Bridge | 擴張島嶼邊界時不能重複計算水格 |
| **LC 127** - Word Ladder | 單字必須在入列時標記已拜訪，才不會產生重複路徑 |

<!-- e7dec2a21aa9 -->
#### 小結——入列前標記 vs 入列後標記

> 在 BFS 中，**你決定某個鄰居該進佇列的那一刻，就是你做出承諾的那一刻**——標記它已拜訪、更新計數器、變更格子。永遠不要把狀態變更延到出列時才做。這不是優化，這是**正確性要求**。

<!-- b2b4134ec64f -->
### Pattern 3.2：搭配方向陣列的格子 BFS — LC 1091
> 從左上角穿過 0 的格子 BFS 到右下角（八方向）。
>
> **慣用寫法**：把移動方向提取成 `dirs` 陣列再迴圈跑過 — 一般四鄰格用 `int[][] dirs = {{1,0},{-1,0},{0,1},{0,-1}}`
> （見 **Pattern 4**），要算對角線時就用下方那八組。先做邊界檢查，
> 再入列前標記（**Pattern 3.1**）——這裡格子本身就是 visited 集合。

<!--CODE-->

<!-- 622c326fb64c -->
### Pattern 4：多源 BFS（距離計算） — LC 542 / LC 994 ⭐⭐⭐⭐⭐
<!--CODE-->

<!-- 4c0db54ec110 -->
#### 標準題：Rotting Oranges — 多源，一層 = 一分鐘
> 讓所有初始的腐爛橘子同時逐層向外擴散腐爛。

<!--CODE-->

**Python 實作 — LC 994：**
<!--CODE-->

如果我們把 `grid[nr][nc] = 2`（Java）／`grid[y_][x_] = 2`（Python）延後到出列才做，同一層裡的兩個腐爛鄰居就可能把同一顆新鮮橘子都入列，導致 `fresh` 變成負數而回傳錯誤答案。

<!-- 7ed71eca1f50 -->
#### 距離變形：01 Matrix — 到最近來源的距離
> 從所有 0 的格子同時開始 BFS；距離會向外傳播。

<!--CODE-->

**這個模式為什麼有效：**
1. **同步擴張**：所有來源以相同速率擴張 → 一層一層推進
2. **第一次拜訪 = 最短**：在無權 BFS 中，第一次抵達即保證是最短路徑
3. **不需回溯**：一格被拜訪過，就代表已找到它的最短距離
4. **線性時間**：每一格恰好拜訪一次 → 總計 O(m×n)

**關鍵洞見 — 為什麼從 0 開始而不是從 1 開始？**
- ❌ 從每個 1 開始 → O(m×n) 次 BFS 呼叫 → 總時間 O(m²×n²)
- ✅ 從所有 0 一起開始 → 單趟 BFS → 總時間 O(m×n)
- **原則**：把問題翻轉過來——與其問「這個 1 離任一個 0 有多遠？」，改問「所有 0 能擴散到多遠？」

> **時間該在哪裡遞增——經驗法則：** 如果你在一層的**開頭**做 `time++`，那 while 迴圈裡就**必須**有提早結束的條件（`&& fresh > 0`）。否則就在一層的**結尾**搭配旗標做 `time++`。完整的 A vs B 逐步說明：[bfs_examples.md](./bfs_examples.md) → *When to Increment Time/Distance*。

<!-- 8a824635fb79 -->
### Pattern 5：帶路徑追蹤的 BFS（攜帶路徑，而不是距離）
<!--CODE-->

<!-- 65989e8ea02e -->
### Pattern 7：無權圖上的最短路徑 — BFS + 原地狀態變更 — LC 127 ⭐⭐⭐⭐⭐
<!--CODE-->

> 這個模板的逐行解說——執行追蹤、為什麼還原那一步是必要的，以及它與其他 BFS 形狀的比較——在 [bfs_advanced.md](./bfs_advanced.md)。

<!-- 8b050f4d9711 -->
### Pattern 11：父節點對照表 + 從目標向外輻射的 BFS — LC 863 ⭐⭐⭐⭐⭐

**a. 核心想法**

> **「距離某個節點多遠」（不是距離根多遠）⇒ 把樹變成無向圖，再從那個節點向外 BFS。**

二元樹只儲存**向下**的指標（`left`、`right`），但距離 `target` 為 `k` 的節點可能出現在**三個**地方：在它下面、在它**上面**，或在**兄弟子樹**裡（先上後下）。只會往下走的單一次 DFS 永遠碰不到它們。

分兩步修正：

1. **先做一次 DFS 記錄 `{node: parent}`** — 這是*唯一*缺少的邊方向。你不需要完整的鄰接表（像 Pattern 10 那樣）：`left`、`right` 本來就在節點上，所以**每個節點最多有 3 個鄰居 = `(left, right, parent)`**。
2. **從 `target` 開始以 `(node, dist)` 做 BFS**，往這 3 個方向展開。因為每條邊成本都是 1，**`dist` 就是精確的樹上距離**——當 `dist == k` 時收集 `node.val` 並**停止展開那一支**（`continue`）。

**兩個讓它正確的關鍵：**

| 元素 | 為什麼不可省 |
|---|---|
| `visited` 集合 | 加上父邊之後圖變成**無向**的 → BFS 會在 `child → parent → child` 之間永遠來回。單純由上而下的樹走訪從不需要 `visited`；這裡則一定要。 |
| `dist == k` 時 `continue` | 超過 `k` 的節點無關緊要，而且它們唯一的回頭路徑得經過一個已被收集的節點。停止展開能限制工作量，也避免多收。 |

**b. 模式**

<!--CODE-->

<!--CODE-->

**圖解追蹤** — `root = [3,5,1,6,2,0,8,null,null,7,4]`、`target = 5`、`k = 2`

<!--CODE-->

**辨識訊號**
- 距離／鄰居是**從任意一個節點**去量測，而不是從根。
- 答案集合可能包含**祖先**或**兄弟子樹**裡的節點。
- 出現「距離 `target` 為 `k`」、「從節點 `start` 擴散／感染」、「離節點 `k` 最近的 X」這類措辭。

**變形：做 `k` 次逐層 BFS，然後把佇列倒出來** — tuple 裡不放 `dist`；展開 `k` 次之後，佇列*本身*就是答案集合。完整程式碼、A vs B 比較與陷阱表：[bfs_examples.md](./bfs_examples.md) §2-18。

> **DFS 替代解（「距離滲透」）**：用後序 DFS 回傳 `target` 在各子樹中的深度；在目標上方 `d` 條邊處的節點，往*另一個*子節點收集 `k - d` 層下方的節點。同樣是 O(n)，遞迴之外只要 O(1) 額外空間——但寫錯的機率高得多。**BFS + 父節點對照表才是面試上穩妥的答案。**

**父節點對照表（本模式） vs 完整鄰接表（Pattern 10）**

| | 父節點表 `{node: parent}` | 鄰接表 `{node: [neighbors]}` |
|---|---|---|
| 建立方式 | 一次 DFS，每個節點 1 筆 | 一次 DFS，每條邊 2 筆 |
| 取得鄰居 | `(node.left, node.right, parents[node])` | `graph[node]` |
| 空間 | 約 n 個指標 | 約 2(n-1) 個串列項目 |
| 使用時機 | 節點是可直接解參照的**真實 `TreeNode` 物件** | 你操作的是**值**，或結構本身不是二元樹 |

**c. 相似 LC**

| 題目 | LC # | 與本模式的關聯 |
|---------|------|----------------------|
| All Nodes Distance K in Binary Tree | 863 | **標準題** — 父節點表 + 向外 BFS `k` 步 |
| Amount of Time for Binary Tree to Be Infected | 2385 | 同一套父節點表 BFS；答案 = **最大**距離（最後一層 BFS） |
| Closest Leaf in a Binary Tree | 742 | 從目標向外 BFS，在彈出**第一個葉節點**時停止（§2-15） |
| Find Distance in a Binary Tree | 1740 | 兩節點間的距離 = 從其中一個 BFS 直到另一個被彈出（或用 LCA） |
| Number of Good Leaf Nodes Pairs | 1530 | Pattern 10 — 從*每個*葉節點向外做有界 BFS |
| Step-By-Step Directions From a Binary Tree Node | 2096 | 同樣的先上後下洞見，但重建的是路徑而非距離 |
| Cousins in Binary Tree | 993 | 每個節點需要 `parent` + `depth` — 只要父節點表，不需向外輻射 BFS |
| All Possible Full Binary Trees / LCA 236 | 236 | LCA 就是先上後下路徑的「轉折點」 |
| Minimum Height Trees | 310 | 無向樹的 BFS，是往內修剪而非往外輻射（§2-10） |

> **模式重點**：一旦某題樹的問題是**從根以外的節點**去量測某件事，就別再想「樹的遞迴」，
> 改想**「無向圖」**——補上父連結（`{node: parent}` 對照表，或 `node.par` 標註），
> 接著它就是一場普通的 BFS，其中每個節點有 3 個鄰居，而 `visited` 是不可省略的。

---

<!-- e55100799816 -->
### Pattern 13：BFS 二著色（二分圖檢查） — LC 785 ⭐⭐⭐⭐

**關鍵想法**：BFS 不一定要攜帶**距離**——它也可以攜帶**標籤**。把起點塗成 `0`，每個鄰居塗成相反的顏色（`color ^ 1`）。如果 BFS 遇到某個已上色的鄰居顏色**相同**，就代表存在奇數長度的環 → 不是二分圖。

**兩個陷阱**：
- **不連通的圖** — 你必須用 `for (s = 0..n-1)` 迴圈，對每個尚未上色的節點都重新開一次 BFS；一次 BFS 只涵蓋一個連通元件。
- **不要用單純的 `visited` 布林值** — 以 `-1 = 未拜訪` 的 `color[]` 同時扮演已拜訪標記*與*答案。

<!--CODE-->

<!--CODE-->

**注意**：同樣這招「BFS 攜帶的是標籤而非距離」也能解決「把互相討厭的人分成 2 組」這類題目——先從配對建出鄰接串列，然後這段程式碼原封不動就能跑。

---

<!-- de48d33dc263 -->
## 總結與速查

> 下方的 `Pattern 4.5 / 4.6 / 6 / 8 / 8.5 / 9 / 10 / 12 / 14 / 15` 放在 [bfs_advanced.md](./bfs_advanced.md)；`§2-N` 的參照放在 [bfs_examples.md](./bfs_examples.md)。

<!-- 58e76ae91f42 -->
### 什麼時候該用 BFS
- 在無權圖上找最短路徑
- 樹的層序走訪
- 找連通元件
- 檢查圖是否為二分圖
- 網頁爬蟲（廣度優先探索）
- **同步的多源距離計算**（Pattern 4）- 到最近來源的距離
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

<!-- 3664a617513c -->
#### ❌ 常見錯誤
1. 對 list 用 `queue.pop()` 而不是 `queue.popleft()`
2. 在圖上沒有處理 visited 集合（造成無限迴圈）
3. 需要逐層處理時卻忘了分層
4. 格子題的邊界檢查寫錯

<!-- e5ef843840b3 -->
#### ✅ 最佳實務
1. 用 `collections.deque` 以獲得更好的效能
2. 圖的題目一律使用 visited 集合
3. 格子題在加入佇列前先檢查邊界
4. 考慮用多源 BFS 來優化
5. 需要最短路徑時記得追蹤層數／距離
6. **在入列前而非出列後標記狀態** — 決定要把鄰居入列的那一刻就更新 grid／visited／計數器；延到出列才做會讓多個鄰居重複把同一格入列（見上方 **Pattern 3.1**）

<!-- 4f71db84c5cf -->
### 依題型分類的題目

<!-- 56b4ad0e80b6 -->
#### 1. 樹的走訪問題
- **層序走訪**：LC 102, 107, 103
- **二元樹路徑**：LC 257, 1022
- **右視圖**：LC 199
- **垂直順序**：LC 314
- **逐層改動樹**：LC 623 (Add One Row)、LC 116/117 (Next Right Pointers)
- **距離任意節點多遠（Pattern 11 — 父節點表 + 向外輻射）**：LC 863 (Distance K)、LC 2385 (Tree Infection)、LC 742 (Closest Leaf)、LC 1740 (Find Distance)

<!-- b64d1fcf1d61 -->
#### 2. 最短路徑問題
- **無權圖**：LC 127 (Word Ladder)
- **格子導航**：LC 1730 (Shortest Path to Food)、LC 1091 (Shortest Path in Binary Matrix)
- **同步多源距離（Pattern 4）**：
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

<!-- 3b6c3ab1766a -->
### 關鍵 LeetCode 題目
| 難度 | 題目 | 關鍵概念 | 核心模式 |
|------------|---------|-------------|--------------|
| Easy | LC 102 | 層序走訪 | Pattern 2（逐層） |
| **Medium** | **LC 127** | **最短路徑轉換 - Word Ladder** | **Pattern 7（無權最短路徑）** |
| Medium | LC 200 | 連通元件 | Pattern 3（圖 BFS） |
| Medium | LC 742 | 最近的葉節點（樹 → 無向圖） | `bfs_examples.md` §2-15（樹 → 圖 + BFS） |
| Medium | LC 863 | 距離**目標節點** `k`（父節點表、3 個鄰居） | Pattern 11（向外輻射）；`bfs_examples.md` §2-18 中的形狀 B |
| Medium | LC 623 | 逐層 BFS 到 `depth - 1`，再重接子節點指標 | `bfs_examples.md` §2-17（Add One Row to Tree） |
| **Medium** | **LC 542** | **同步多源 - 01 Matrix** | **Pattern 4（同步多源）** |
| Medium | LC 934 | DFS + 多源 BFS（島嶼擴張） | Pattern 4.5（DFS + 多源） |
| Medium | LC 1162 | As Far from Land as Possible | Pattern 4（同步多源） |
| **Hard** | **LC 126** | **找出所有最短路徑 - Word Ladder II** | **Pattern 8.5（BFS + DFS 的 DAG 列舉）** |
| Hard | LC 286 | Walls and Gates | Pattern 4（同步多源） |
| **Hard** | **LC 317** | **各自獨立的 BFS（距離總和）** | **Pattern 4.6（獨立 BFS）** |
| Hard | LC 675 | 排序 + 重複 BFS（依序處理目標） | Pattern 6（排序 + 重複 BFS） |
| **Hard** | **LC 752** | **在狀態空間上做 BFS - Open the Lock** | **Pattern 7（無權最短路徑）；解題見 `bfs_examples.md` §2-6** |
| **Hard** | **LC 815** | **以路線為層的 BFS（最少公車數）** | **Pattern 8（路線層 BFS）** |
| Hard | LC 864 | 帶狀態的 BFS（收集鑰匙） | Pattern 3 + 狀態 |
| Hard | LC 1293 | 帶狀態的 BFS（消除障礙物） | Pattern 3 + 狀態 |

<!-- 7c6937ca87aa -->
### 也常被問到（沒有新模板——它們重用上面的模板）

| LC | 題目 | 重用了哪個模板 |
|----|---------|--------------------------|
| 297 / 449 | Serialize and Deserialize Binary Tree / BST | Pattern 2 逐層 BFS 並寫入 `null` 標記；反序列化 = 同一個佇列讀回來（見 [bfs_examples.md](./bfs_examples.md) 中的 Variation C） |
| 104 | Maximum Depth of Binary Tree | Pattern 2 — 數層數；這題 DFS 遞迴更短，BFS 只在 LC 111 才佔優勢 |
| 101 | Symmetric Tree | Pattern 2 搭配**成對佇列** — 鏡像地入列 `(left, right)`，彈出時比較 |
| 637 / 515 | Average of Levels / Largest Value in Each Tree Row | Pattern 2 — 把「蒐集這一層」換成「彙總這一層」（平均／最大） |
| 433 | Minimum Genetic Mutation | 與 LC 127 / 752 同一套模板 — 8 個字元的基因字串、4 種字母、bank = 合法狀態集合 |
| 529 | Minesweeper | Pattern 3 格子 BFS — 只有在相鄰地雷數為 `0` 時才展開該格，否則寫下數字就停 |
| 547 | Number of Provinces | Pattern 3 — 數要跑幾次 BFS 才能覆蓋所有節點（或用併查集） |
| 1376 | Time Needed to Inform All Employees | 在主管樹上跑 Pattern 2，佇列存放 `(employee, timeSoFar)` — 答案是最大值 |
| 787 | Cheapest Flights Within K Stops | BFS 的**有界層數鬆弛**（帶有 Bellman-Ford 味道）：恰好跑 `k+1` 層，而且**不要用全域 visited** — 同一個節點可能以更低成本再次進入。見 `Dijkstra.md`。 |
| 329 | Longest Increasing Path in a Matrix | 這不是 BFS 題 — 用 DFS + memo，或在 DAG 上跑 Kahn 的 BFS（見 `topology_sorting.md`） |
| 721 / 947 / 684 / 839 | Accounts Merge / Stones Removed / Redundant Connection / Similar String Groups | 這是連通性問題而非最短路徑 — 併查集才是預期解（BFS 洪水填充也行） |
