<!-- 5c7716820e6e -->
# DFS（深度優先搜尋）

> **範圍** — DFS 的主文件：十個核心的深度優先模板 — 樹走訪、網格填色（flood fill）、路徑搜尋、回溯、樹結構修改、後序彙總、邊界消除、形狀簽名與帶權邊走訪 — 並附上用來挑選模板的辨識表。
> **另見** — *從本檔案拆出去的深入主題*：[dfs_advanced.md](./dfs_advanced.md) — 尤拉路徑（Hierholzer）、Tarjan 找橋、字典樹 + 萬用字元 DFS、以深度為索引的堆疊 DFS、距離桶配對葉節點、N 元樹與 `parent[]` 的彙總；[dfs_examples.md](./dfs_examples.md) — 題解存放處，以及依模式與難度整理的完整題目索引。
> *鄰近文件*：[bfs.md](./bfs.md) — 廣度優先的對應版本以及如何取捨；[backtrack.md](./backtrack.md) — 回程時會還原狀態的 DFS；[graph.md](./graph.md) — 圖的表示法；[tree.md](./tree.md) — 樹上的 DFS。

<!-- d9aab21dcee5 -->
## LeetCode 題目清單

- [Depth-First Search](https://leetcode.com/problem-list/depth-first-search/)
- [Graph Theory](https://leetcode.com/problem-list/graph/)

<!-- bc11a4794920 -->
## 總覽
**深度優先搜尋（DFS）** 是一種圖／樹的走訪演算法，沿著每個分支盡可能往深處走，走不下去才回溯。它用遞迴或堆疊來維護走訪路徑。

<!-- 051851fbe209 -->
### 關鍵性質
- **時間複雜度**：圖是 O(V + E)，樹是 O(n)
- **空間複雜度**：遞迴堆疊 O(h)，其中 h = 高度
- **核心想法**：先往深走，再往廣走
- **資料結構**：堆疊（遞迴的隱含堆疊或顯式堆疊）
- **什麼時候用**：路徑搜尋、環偵測、拓撲排序、樹走訪、回溯類題目

<!-- ec4ec64a59de -->
### 參考資料
- [DFS Visualization](https://www.cs.usfca.edu/~galles/visualization/DFS.html)
- [DFS vs BFS Comparison](https://github.com/yennanliu/CS_basics/blob/master/doc/pic/dfs_vs_bfs.png)
- [Tree Traversal Animations](https://github.com/yennanliu/CS_basics/blob/master/doc/pic/dfs_2.png)

<!-- 3311584cf729 -->
## 題型分類

下面每個模式都**只出現一次** — 以下一節的模板形式呈現。這張表是它們的索引：
先對上辨識關鍵字，再跳到對應的模板。

| # | 模式 | 辨識關鍵字 | 模板 | 代表題 | 其他 |
|---|---------|----------------------|----------|--------------|------|
| 1 | 樹走訪 | "traverse"、"visit all"、"print tree"、"serialize" | [T1](#template-1-tree-traversal--lc-94-) | LC 94 | 144, 145, 297, 449, 100 |
| 2 | 圖／網格走訪、連通分量 | "connected components"、"islands"、"cycle detection" | [T2](#template-2-graph--grid-dfs-flood-fill--lc-200-) | LC 200 | 695, 133, 207, 210, 419 |
| 3 | 路徑類問題 | "path sum"、"root to leaf"、"all paths"、"does a path exist" | [T3](#template-3-path-finding--lc-112-) | LC 112 | 113, 257, 129, 1971 |
| 4 | 回溯 | "all combinations"、"permutations"、"subsets" | [T4](#template-4-backtracking--lc-46) | LC 46 | 78, 39, 17, 22, 51, 79 |
| 5 | 樹結構修改 | "delete"、"insert"、"trim"、"convert" | [T5](#template-5-tree-modification--lc-450) | LC 450 | 701, 669, 538, 226, 114 |
| 6 | 子樹彙總與 LCA | "subtree sum"、"duplicate subtrees"、"LCA"、"deepest leaves" | [T6](#template-6-bottom-up-post-order-dfs--lc-543-) | LC 543 | 124, 236, 508, 652, 663, 2049 |
| 7 | 邊界消除（兩趟） | "closed islands"、"surrounded regions"、"captured" | [T7](#template-7-2-pass-dfs-boundary-elimination--lc-1254) | LC 1254 | 130, 417, 1020 |
| 8 | 路徑簽名（形狀編碼） | "distinct islands"、"unique shapes"、"same shape after translation" | [T8](#template-8-path-signature-shape-encoding--lc-694) | LC 694 | 711, 652 |
| 9 | 網格 DFS + 回溯 | "one path"、"collect the most"、"cannot revisit a cell" | [T9](#template-9-grid-dfs--backtracking--3-styles-compared-lc-1219-path-with-maximum-gold) | LC 1219 | 79, 329, 980 |
| 10 | 帶權邊 DFS（比值查詢） | "evaluate division"、"exchange rates"、"transitive ratios" | [T10](#template-10-weighted-graph-dfs-divisionratio-queries--lc-399) | LC 399 | 721, 1101, 737 |

**不在這份文件裡** — 以下內容住在 [dfs_advanced.md](./dfs_advanced.md)：雙網格驗證（LC 1905）、
邊方向追蹤（LC 1466）、連通分量配對計數（LC 2316）、尤拉路徑（LC 332, 753）、Tarjan
找橋（LC 1192）、字典樹 + 萬用字元 DFS（LC 211, 676）、以深度為索引的堆疊 DFS（LC 388, 1233）、
距離桶配對葉節點（LC 1530）、N 元樹後序彙總（LC 3965）、樹 ⟷ 字串編解碼
（LC 606, 536），以及 `parent[]` 陣列往上爬深度（LC 4015）。依模式與難度整理的完整題目清單
在 [dfs_examples.md → Problems by Pattern](./dfs_examples.md#problems-by-pattern)。

<!-- ddc7f484e4af -->
## 模板與演算法

<!-- 44d80ebb9360 -->
### 模板比較表
| 模板 | 使用情境 | 關鍵操作 | 時間 | 空間 | 什麼時候用 |
|----------|----------|---------------|------|-------|-------------|
| **1. 樹走訪** | 拜訪所有節點 | 遞迴／堆疊 | O(n) | O(h) | 樹的題目 |
| **2. 圖／網格 DFS** | 探索圖、填色 | visited 集合／就地標記 | O(V+E) | O(V) | 圖與網格的探索 |
| **3. 路徑搜尋** | 找出特定路徑 | 追蹤路徑 | O(n) | O(h) | 路徑類題目 |
| **4. 回溯** | 嘗試所有路徑 | 撤銷選擇 | O(b^d) | O(d) | 組合類題目 |
| **5. 結構修改** | 改變結構 | 更新節點 | O(n) | O(h) | 樹的編輯 |
| **6. 由下而上** | 彙總資訊 | 後序回傳 | O(n) | O(h) | 子樹類題目 |
| **7. 兩趟 DFS** | 邊界消除 | 兩階段填色 | O(m×n) | O(m×n) | 封閉／被包圍的區域 |
| **8. 路徑簽名** | 編碼形狀 | 記錄方向 | O(m×n) | O(m×n) | 計算相異形狀數 |
| **9. 網格 DFS + 回溯** | 網格中的單一最佳路徑 | 標記、遞迴、**還原** | O(4^k) | O(k) | 多個起點且路徑會重疊 |
| **10. 帶權圖 DFS** | 比值／除法查詢 | 累乘 | O(Q·(V+E)) | O(V+E) | 傳遞性比值計算 |

<!-- 70c2abc8da15 -->
### 通用 DFS 模板
<!--CODE-->

<!-- 35bebdd7b248 -->
### 模板 1：樹走訪 — LC 94 ⭐⭐⭐⭐⭐
- **說明**：以特定順序拜訪所有節點（前序、中序、後序）
- **辨識**："Traverse"、"visit all"、"print tree"、"serialize"
- **例題**：LC 94、LC 144、LC 145、LC 297、LC 449

<!--CODE-->

<!-- 481b1282f1c5 -->
### 模板 2：圖／網格 DFS（Flood Fill） — LC 200 ⭐⭐⭐⭐⭐
- **說明**：探索圖、找出連通分量、偵測環
- **辨識**："Connected components"、"islands"、"cycle detection"
- **例題**：LC 200、LC 695、LC 133、LC 207、LC 210

<!--CODE-->

<!-- c007da359200 -->
#### 變體：**不用** flood fill 也能數連通分量 — LC 419 Battleships in a Board

**巧思**：當每個連通分量都保證是筆直的 1×k / k×1 直線時，你根本不需要 DFS —
只要數出屬於某艘船**左上端**的格子即可，這讓額外空間降到 `O(1)`
（不用 `visited` 集合，也不用就地改動）。在 LC 200 的 flood fill 基準之上，
這正是那個經典追問 *「能不能一趟掃完、O(1) 空間、且不修改盤面？」* 的好答案。

<!--CODE-->

<!--CODE-->

> 如果拿掉「船一定是直線」這個保證，就退回上面那套 LC 200 的網格 DFS。

<!-- bbefdf079602 -->
### 模板 3：路徑搜尋 — LC 112 ⭐⭐⭐⭐⭐
- **說明**：在樹／圖中找出具備特定性質的路徑
- **辨識**："Path sum"、"root to leaf"、"all paths"、"longest path"
- **例題**：LC 112、LC 113、LC 257、LC 124、LC 543

**📚 相關模式**：想看更完整、變化更多的路徑題型（路徑和、最大路徑、連續序列、前綴和技巧），請看 **bst.md 模板 7（Path Problems）**，那裡有 7 種路徑模式的詳細實作。

<!--CODE-->

<!-- c6c8974fd759 -->
### DFS 提早回傳模式 — TRUE 要積極回傳，FALSE 要拖到最後
**問題**：在 DFS 裡找路徑時，下面這兩種寫法差在哪？

<!-- e4771f7509a5 -->
#### ❌ 錯誤做法：沒有檢查回傳值
<!--CODE-->

<!-- 6bf8be358983 -->
#### ✅ 正確做法：成功時立刻回傳
<!--CODE-->

---

<!-- 5b9ac8efb770 -->
#### 📊 具體範例：為什麼提早回傳很重要

**測試案例：**
<!--CODE-->

---

<!-- b60d628bf7c2 -->
##### 情境 1：❌ 錯誤（沒有提早回傳）

**呼叫堆疊追蹤：**
<!--CODE-->

**為什麼會失敗：**
- 在第 4 步找到了終點（回傳 TRUE）
- 但第 3 步的上層呼叫**忽略了**這個 TRUE
- 於是繼續多餘地探索其他鄰居
- 最後因為其他路徑到不了終點而回傳 FALSE

---

<!-- d2b8d3aa3966 -->
##### 情境 2：✅ 正確（有提早回傳）

**呼叫堆疊追蹤：**
<!--CODE-->

**為什麼可行：**
- 在第 4 步找到了終點（回傳 TRUE）
- 第 3 步的上層呼叫**檢查了**回傳值
- 立刻回傳 TRUE，不再探索其他路徑
- 把 TRUE 一路傳回根節點

---

<!-- 521f3ed672fb -->
#### 🎯 關鍵洞察

| 面向 | ❌ 沒有提早回傳 | ✅ 有提早回傳 |
|--------|------------------------|---------------------|
| **正確性** | ❌ 路徑存在卻回傳 FALSE | ✅ 找到路徑就回傳 TRUE |
| **效率** | 多餘地探索所有路徑 | 一找到路徑就停 |
| **時間複雜度** | 永遠 O(V + E)（走完全部） | 最壞 O(V + E)，但通常好很多 |
| **使用情境** | 蒐集**所有**路徑／結果 | 判斷是否存在**任一**路徑 |

---

<!-- 2b884968c7c7 -->
#### 📝 各自適用的時機

<!-- 62d9f77654a2 -->
##### 模式 1：提早回傳（檢查路徑是否存在）
<!--CODE-->
**例題：** LC 1971（Path Exists）、LC 797（All Paths）、LC 79（Word Search）

<!-- c23f2d2cd61d -->
##### 模式 2：不回傳、繼續走（蒐集所有結果）
<!--CODE-->
**例題：** LC 257（All Root-to-Leaf Paths）、LC 113（Path Sum II）、LC 22（Generate Parentheses）

---

<!-- 2cfb4402e2a7 -->
### 模板 4：回溯 — LC 46
- **說明**：嘗試所有可能，並撤銷選擇
- **辨識**："All combinations"、"permutations"、"subsets"
- **例題**：LC 46、LC 78、LC 39、LC 17

<!--CODE-->

<!-- bd596ffd4fde -->
### 模板 5：樹結構修改 — LC 450
- **說明**：在走訪過程中修改樹的結構或數值
- **辨識**："Delete"、"insert"、"trim"、"convert"
- **例題**：LC 450、LC 701、LC 669、LC 538

<!--CODE-->

<!-- 7c7244008ee8 -->
#### 慣用寫法：先把子樹指回去，再回傳該節點
- 把子樹指派給節點，最後再回傳更新後的節點（非常重要！！！！）

<!--CODE-->

<!-- 5370aa7cd1c2 -->
### 模板 6：由下而上（後序）DFS — LC 543 ⭐⭐⭐⭐⭐
- **說明**：先處理子樹並由下往上彙總結果；也用來找目標節點的最低共同祖先
- **辨識**："Subtree sum"、"duplicate subtrees"、"LCA"、"smallest subtree containing"、"lowest common ancestor"、"deepest leaves"
- **例題**：LC 508、LC 652、LC 236、LC 663、LC 865、LC 1123
- **什麼時候用 LCA 解法**：
  - 兩個（或更多）目標節點分別落在不同子樹，而你要找第一個「同時看得到兩邊」的節點
  - 「包含〔條件 X〕的最小子樹」— 這其實就是換皮的 LCA
  - 目標可能是**題目給定**的（LC 236：找 p、q 的 LCA），也可能是**隱含**的（LC 865/1123：所有最深層的節點）
- **核心想法（後序／由下而上）**：
  1. 先遞迴左右子樹（後序）
  2. 每個子樹往上回傳一組 `(node, depth/info)`
  3. 在每個節點比較左右結果：
     - **左邊較深** → 答案在左子樹，把左邊的結果往上傳
     - **右邊較深** → 答案在右子樹，把右邊的結果往上傳
     - **深度相同** → 目前節點就是 LCA（最深的路徑在這裡交會），回傳目前節點
  4. 遞迴的根節點持有最終答案
- **主要變體**：
  - **標準 LCA（LC 236）**：目標 p、q 已給定；回傳第一個在不同子樹看到兩者的節點
  - **以深度為基準的 LCA（LC 865/1123）**：目標是找出來的（最深的節點）；用深度比較找出最深路徑收斂處
  - **先標記再作答（LC 865 官解 V1）**：兩趟 — 第一趟 DFS 算出所有深度，第二趟 DFS 找出包含所有最深節點的子樹
  - **BFS + parent 對照表（LC 865 V0-4）**：先 BFS 找出最深的一層，再沿著 parent 往上走，直到全部收斂到同一個節點
- **相似的經典 LC 題目**：
  - LC 236 - Lowest Common Ancestor of a Binary Tree（標準 LCA）
  - LC 235 - Lowest Common Ancestor of a Binary Search Tree（用 BST 性質最佳化）
  - LC 865 - Smallest Subtree with all the Deepest Nodes（以深度為基準的 LCA）
  - LC 1123 - Lowest Common Ancestor of Deepest Leaves（同 LC 865）
  - LC 1644 - Lowest Common Ancestor of a Binary Tree II（節點可能不存在）
  - LC 1650 - Lowest Common Ancestor of a Binary Tree III（有 parent 指標）
  - LC 1676 - Lowest Common Ancestor of a Binary Tree IV（多個目標節點）

<!--CODE-->

<!-- a1033b2ed8fa -->
#### 全域累加器寫法 — LC 124 Binary Tree Maximum Path Sum
<!--CODE-->

<!-- 44959c0e78bf -->
#### 變體：子樹大小彙總（移除節點後計分） — LC 2049
- **說明**：後序 DFS 回傳每個節點的**子樹大小**，同時算出由「移除該節點後形成的各連通分量大小」推導出的每個節點的值（分數）
- **辨識**："remove node and edges → tree splits into subtrees"、"product/sum of component sizes"、"score of a node"、"tree given as `parents[]` array"
- **關鍵技巧**：一趟 DFS 回傳 `subtree_size = 1 + Σ child_subtree_size`。移除節點後，連通分量是 (a) 每個子節點的子樹，以及 (b) **上方那一側** = `n - subtree_size`。邊走邊彙總即可。
- **例題**：LC 2049（Count Nodes With the Highest Score）
- **核心想法**：
  - 移除節點 `x` 會把樹切成 `len(children[x])` 個子節點分量，**再加上**「上方」那個分量（x 子樹以外的所有東西）。
  - `child component size` = 每個子節點的子樹大小（由 DFS 回傳）。
  - `parent / above component size` = `n - subtree_size(x)`（只有在 `> 0`，也就是 x 不是根節點時才算數）。
  - `score(x) = Π(child subtree sizes) × max(1, n - subtree_size(x))` — 每個子樹大小都只算**一次**，因此是 O(n) 時間／O(n) 空間（n ≤ 10^5 時必須如此）。
- **從 `parents[]` 建樹**：對 `i != root` 做 `children[parents[i]].append(i)`；根節點是 `parents[i] == -1` 的那個索引（通常是節點 0）。
- **模式變體**：
  - **一趟 DFS**（回傳大小的同時就地累乘／更新最大值）— 最精簡
  - **兩趟**（第一趟：預先算出 `subtree_size[]` 陣列；第二趟：逐一走過節點算分數）— 把算大小和算分數拆開，比較好推理
- **重要提醒**：
  - 用 `max(1, ...)` 或 `if remaining > 0` 保護上方分量 — 根節點沒有「上方」分量。
  - 用以分數為鍵的 `Counter`／dict 來數有多少節點達到最大值，或是邊走邊追蹤 `(max_score, count)`。
  - 這個做法不限於二元樹 — 同一套 DFS 適用於任何以 `parents[]`／鄰接表給定的樹。
- **相似的經典 LC 題目**：
  - LC 2049 - Count Nodes With the Highest Score（移除節點計分的代表題）
  - LC 1519 - Number of Nodes in the Sub-Tree With the Same Label（用 DFS 做子樹彙總）
  - LC 508 - Most Frequent Subtree Sum（每個子樹的值 + 次數統計）
  - LC 543 - Diameter of Binary Tree（由下而上的子樹指標）
  - LC 124 - Binary Tree Maximum Path Sum（回傳子樹值，彙總全域最大）
  - LC 834 - Sum of Distances in Tree（子樹大小 + 換根 DP，進階追問）

<!-- bd9ca9dfa15e -->
### 模板 7：兩趟 DFS（邊界消除） — LC 1254
- **說明**：先消掉和邊界相連的格子，再處理內部
- **辨識**："Closed islands"、"surrounded regions"、"captured pieces"
- **例題**：LC 1254、LC 130、LC 417

<!--CODE-->

<!--CODE-->

<!-- 1a5ceed34f4a -->
### 模板 8：路徑簽名（形狀編碼） — LC 694
- **說明**：用唯一的路徑簽名來編碼島嶼或子樹的形狀／結構
- **辨識**："Distinct islands"、"unique shapes"、"count different structures"、"same shape after translation"
- **關鍵技巧**：在 DFS 走訪過程中記錄移動方向，組出一個標準化的簽名
- **例題**：LC 694、LC 711、LC 652

<!--CODE-->

> **兩種可互換的編碼方式。** 上面的 Java 版記錄的是進入每個格子時所**走的方向**
> （`D/U/R/L`，回程時再加一個 `O` 分隔符）；下面的 Python 版則改為記錄每個格子的
> **相對座標** `(r-r0, c-c0)`。兩者都對平移不變、但對旋轉敏感 — 挑一種用就好，
> 千萬別在同一個簽名裡混用。

<!--CODE-->

**路徑簽名的關鍵觀念：**

1. **固定的走訪順序**
   - 永遠以同一個固定順序檢查鄰居（例如 D、U、R、L）
   - 這才能保證相同的形狀產生相同的簽名

2. **起點正規化**
   - 以固定順序掃描網格（由上而下、由左而右）
   - 遇到的第一個陸地格子就是原點
   - 所有座標都相對於這個原點

3. **為什麼分隔符很重要**
<!--CODE-->

4. **一致性保證**
   - 相同形狀 → 相同簽名（永遠成立）
   - 不同形狀 → 不同簽名
   - 對平移不變（位置不影響結果）
   - 對旋轉／鏡射敏感（這正是題目要求的）

<!-- 3086d629ddc4 -->
### 模板 9：網格 DFS + 回溯 — 三種寫法比較（LC 1219 Path with Maximum Gold）

> **題目**：在 `m x n` 的網格中，沿單一條路徑蒐集最多的金子。你可以從任一個有金子的格子
> 出發／結束，上下左右移動，不能重複走同一格，也不能踩到值為 `0` 的格子。
> 因為路徑可以從任何地方開始，我們對**每一個**有金子的格子都啟動一次 DFS。
> 又因為不同起點的路徑會互相重疊，每次 DFS 結束後我們都要**回溯**（還原該格），
> 讓下一次啟動時網格是乾淨的。
>
> 出處：[`path-with-maximum-gold.py`](../../leetcode_python/Backtracking/path-with-maximum-gold.py)

三個版本都是正確的。它們的差別在於三個決策**放在哪裡**做：
1. **檢查（Guard）** — 這個鄰居合法嗎（在界內 + 有金子 + 沒走過）？
2. **累加（Accumulate）** — `cur_gold` 在哪裡加上目前這格的值？
3. **更新最大值** — 我們在哪裡記錄 `self.max_gold`？

<!-- 10760ec79010 -->
#### 快速比較

| | **V0-1** — 在子呼叫裡檢查 | **V0-2** — 呼叫前先檢查 | **V0-3** — 在迴圈裡更新最大值 |
|---|---|---|---|
| **鄰居迴圈** | 4 個明寫的遞迴呼叫 | `for m in moves:` | `for m in moves:` |
| **檢查的位置** | **子呼叫開頭**（base case） | 遞迴呼叫**之前** | 遞迴呼叫**之前** |
| **累加 `cur_gold`** | 在子呼叫裡（`+= grid[r][c]`） | 在呼叫點（`cur_gold + grid[..]`） | 在呼叫點（`cur_gold + grid[..]`） |
| **傳入的起始值** | `0` | `grid[start]` | `grid[start]` |
| **更新 `max_gold`** | 子呼叫開頭（每格一次） | 子呼叫開頭（每格一次） | 迴圈內（每個鄰居一次）— **需要 seed** |
| **會有多餘的遞迴呼叫嗎？** | 會 — 不合法的鄰居仍會呼叫後才返回 | 不會 — 只有合法鄰居才遞迴 | 不會 — 只有合法鄰居才遞迴 |
| **能處理孤立的起始格嗎？** | ✅ 自動處理 | ✅ 自動處理 | ⚠️ 只能靠呼叫端的 seed |
| **結論** | ✅ 最乾淨的預設寫法 | ✅ 有效率、慣用 | ⚠️ 能動，但脆弱 — 避免 |

**差異的心智模型：** V0-1 把合法性檢查*往下*推給被呼叫者（「由子節點自己決定該不該存在」）—
所以 base case 同時兼任檢查。V0-2 / V0-3 則把它*往上*拉到呼叫端（「父節點只呼叫合法的子節點」）—
因此不會浪費堆疊框架，但起始格必須另外驗證（由啟動迴圈裡的 `if grid[y][x] > 0` 完成）。

<!-- bf3253e0b0c8 -->
#### V0-1 — 在子呼叫裡檢查（建議的預設寫法）

<!--CODE-->

**什麼時候用：** 網格 DFS 的**預設選擇**。最不容易寫錯 — 起始格和鄰居走的是*同一套*檢查，
不需要任何特例處理。當清晰度優先、或起始格本身可能不合法時，就選它。

<!-- 33084eb9ca3d -->
#### V0-2 — 呼叫前先檢查，在呼叫點累加

<!--CODE-->

**什麼時候用：** 當你想要**有效率／競賽慣用**的寫法時 — `moves` 陣列很容易擴充到八方向或
斜向題目，而且能省掉往牆裡撞的無效呼叫。因為 `max_gold` 仍是在**進入節點時**（迴圈之前）更新，
孤立的起始格不用額外程式碼就能正確計分。等你上手之後，這就是首選版本。

<!-- 86fb74664cea -->
#### V0-3 — 在迴圈裡更新最大值（能動，但脆弱 — 避免）

<!--CODE-->

**什麼時候用：** 基本上**永遠不要**當第一選擇。列在這裡是為了展示陷阱：把最大值的更新搬進迴圈後，
起始格對 DFS 來說就變成隱形的，只好由呼叫端補一個 seed。少寫那一行，單格（或完全孤立）的
輸入就會悄悄回傳 `0`。請優先選 V0-1 / V0-2。

<!-- 9d45a24a128a -->
#### 注意事項（三個版本通用）

- **這題的回溯是必要的**，不是可選的。一條路徑可能從很多格子出發，而且路徑會重疊；
  遞迴後還原 `grid[r][c] = cache`，後續的啟動才能重複使用該格。
  和單純的「數島嶼」（LC 200）對比 — 那裡是標記後永不還原。
- **就地標記已訪**（`-1` / `0`）省掉了額外的 `visited` 集合 — 因為我們會還原，所以沒問題。
  檢查條件把*空格*和*已訪*一視同仁（`<= 0`），所以不需要另外的 visited 檢查。
- **每個遞迴框架恰好標記／取消標記一次**，把鄰居探索包在中間 — 絕不是每個鄰居各做一次。
- **在哪裡更新最大值，決定了你需不需要 seed**：在*進入格子時*更新（V0-1/V0-2），
  每一格（包含孤立格）都自動被計入；改成*每個鄰居*更新（V0-3），你就欠呼叫端一個起始格的 seed。
- **複雜度**（三者相同）：`time = O(4^k)` 最壞情況，其中 `k ≤ 25` 是金子格數
  （第一格之後，每格最多分岔到 3 個未訪鄰居）；`space = O(k)` 為遞迴深度。

<!-- bffb9c0cf1e9 -->
### 模板 10：帶權圖 DFS（除法／比值查詢） — LC 399
- **說明**：建一張帶權有向圖，邊的權重代表比值／除法結果，再用 DFS 算出任兩個連通節點之間的傳遞性比值
- **辨識**："Evaluate division"、"exchange rates"、"currency conversion"、"ratio queries"、"transitive relationships with weights"
- **關鍵技巧**：把等式建模成雙向帶權圖（`Map<String, Map<String, Double>>`），DFS 時沿路累乘邊權
- **例題**：LC 399（Evaluate Division）、LC 1101（The Earliest Moment When Everyone Become Friends — 變體）、LC 721（Accounts Merge — 圖分群變體）
- **核心演算法想法**：
  1. **建圖**：對每個等式 `a / b = val`，加上權重為 `val` 的邊 `a → b`，以及權重為 `1/val` 的邊 `b → a`
  2. **處理查詢**：對查詢 `c / d`，從 `c` DFS 到 `d`，沿路把邊權相乘
  3. **累乘**：在 DFS 中傳遞一個累乘值；抵達目標時，該乘積就是答案
  4. **替代做法**：帶比值的併查集（存 `node → root` 的比值，查詢只要 O(α(n))）
- **重要提醒**：
  - **雙向邊**：務必同時存 `a→b` 與 `b→a`，權重互為倒數
  - **visited 集合**：每次查詢都要重置，讓每條路徑的探索互相獨立
  - **提早結束**：若任一節點不在圖中，立刻回傳 -1.0
  - **自己除自己**：若 `start == end` 且該節點存在於圖中，回傳 1.0
  - **乘法 vs 加法**：和最短路徑類題目不同，這裡是乘法累積
- **相似的經典 LC 題目**：
  - LC 399 - Evaluate Division（帶權圖 DFS 的代表題）
  - LC 1976 - Number of Ways to Arrive at Destination（帶權圖走訪）
  - LC 787 - Cheapest Flights Within K Stops（有限制的帶權圖）
  - LC 743 - Network Delay Time（帶權圖探索）
  - LC 1334 - Find the City With the Smallest Number of Neighbors at a Threshold Distance

<!--CODE-->

<!--CODE-->

<!-- ea4fa3974f42 -->
## 總結與速查

<!-- aa6be377a3e5 -->
### 決策流程圖
<!--CODE-->

<!-- 0ff288f87986 -->
### 解題步驟
1. **辨識模式**：樹、圖、回溯，還是路徑？
2. **選擇模板**：挑出合適的 DFS 模板
3. **追蹤狀態**：visited 集合、路徑串列，或全域變數
4. **處理 base case**：空節點、邊界、找到目標
5. **測試邊界情況**：空輸入、單一節點、環

<!-- b63aae4415e0 -->
### 常見錯誤與技巧

**🚫 常見錯誤：**
- **忘記 visited 集合**：在圖上會無窮迴圈
- **沒有回溯**：組合類題目會產生錯誤的路徑
- **走訪順序錯誤**：該用後序時卻用了前序
- **邊走訪邊修改**：會破壞迭代
- **沒處理 null**：NullPointerException
- **⚠️ 關鍵：找到路徑時沒有立刻回傳**：在 DFS 裡找路徑時，一找到就必須立刻回傳 true（詳見前面的說明）

**✅ 最佳實務：**
- **在圖上使用 visited 集合**：避免成環
- **複製路徑**：存結果時用 `path[:]`
- **先檢查邊界**：網格類題目
- **取有意義的名字**：用 `visited` 而不是 `v`
- **考慮改成迭代**：遞迴很深的時候

<!-- 6381e2422ab4 -->
### 面試提示
1. **釐清題型**：是樹還是圖？可能有環嗎？
2. **說明做法**：「我要用 DFS，因為……」
3. **討論複雜度**：時間與空間分析
4. **處理邊界情況**：空的、單一元素、環
5. **必要時最佳化**：記憶化、剪枝

<!-- a9a41a63c8ac -->
### 選模板的實用心法

- **兩趟型問題**：如果得先消掉某些東西（邊界、邊），用模板 7
- **形狀比較**：如果要比較結構／形狀，用模板 8（路徑簽名）
- **由下而上彙總**：如果答案取決於先處理完子節點，用模板 6
- **嘗試所有可能**：如果題目要「所有」解／組合，用模板 4（回溯）
- **多起點且路徑重疊**：標記、遞迴，然後**還原** — 模板 9
- **完全對不上的題目**：在自創模式之前，先翻 [dfs_advanced.md](./dfs_advanced.md)

<!-- 06d7bae4f862 -->
### 相關主題
- **[bfs.md](./bfs.md)**：需要最短路徑時
- **[dp.md](./dp.md)**：子問題重疊 — 把 DFS 記憶化
- **[backtrack.md](./backtrack.md)**：用於組合、且會撤銷的 DFS
- **[union_find.md](./union_find.md)**：處理連通性的替代方案
- **[topology_sorting.md](./topology_sorting.md)**：DFS 在相依關係上的應用
- **[dfs_advanced.md](./dfs_advanced.md)**：從本文件拆出去的冷門模板
- **[dfs_examples.md](./dfs_examples.md)**：題解與完整題目索引

---
**面試必會題目**：LC 94, 104, 112, 113, 124, 200, 236, 297, 399, 694
**進階題目**：LC 124, 297, 329, 472, 652, 694, 711
**路徑簽名模式**：LC 694（Distinct Islands）、LC 711（Distinct Islands II）、LC 652（Find Duplicate Subtrees）
