<!-- af2359899961 -->
# 二元樹

> **範圍** — 二元樹特有的思路：**DFS 的狀態往哪個方向流**（往下還是往上），以及建立在這個基礎上的 11 個結構化模板。
> **另見**：[tree.md](./tree.md) — 一般性的樹概念與走訪策略；[tree2.md](./tree2.md) — 各模式現成的模板；[bst.md](./bst.md) — 樹有序的情況。

<!-- 64e557fa8aa6 -->
## LeetCode 題目清單

- [Binary Tree](https://leetcode.com/problem-list/binary-tree/)
- [Tree](https://leetcode.com/problem-list/tree/)

<!-- fd6b7725cbe7 -->
## 時間複雜度

| 資料結構 | 搜尋   | 插入   | 刪除   | 最小／最大  |
| -------------- | -------- | -------- | -------- | -------- |
| Binary Tree    | O(n)     | O(n)     | O(n)     | O(n)     |

> 一般（未排序）的二元樹 — 沒有順序性，所以每個操作都可能走遍所有節點。*平衡*樹會把搜尋／插入／刪除降到 **O(log n)**。空間是儲存的 **O(n)** 加上遞迴堆疊的 **O(h)**。有序的版本見 [bst.md](./bst.md)。

<!-- 9d7aa218e2fb -->
## 總覽
**二元樹**是一種階層式資料結構，每個節點最多有兩個子節點（左與右）。它是 BST、堆積等許多進階資料結構的基礎，也是理解樹相關演算法的關鍵。

<!-- 7bfbe4adac90 -->
### 關鍵性質
- **複雜度**：見上面的[時間複雜度](#time-complexity)表
- **核心想法**：具有遞迴性質的階層結構
- **什麼時候用**：階層式資料、搜尋、排序、決策、運算式解析

<!-- 363aad3d69fc -->
### 參考資料
- [Binary Tree Visualizer](https://www.cs.usfca.edu/~galles/visualization/BST.html)
- [Wikipedia - Binary Tree](https://en.wikipedia.org/wiki/Binary_tree)
- [Binary Tree - 演算法筆記](https://web.ntnu.edu.tw/~algo/BinaryTree.html)

<!-- 3fd2776dfc4b -->
## 辨識

限制條件的線索出自 [complexity_cheatsheet.md](./complexity_cheatsheet.md) 裡的那把尺。

| 題目敘述裡的訊號 | 限制條件線索 | 該用 | 不是它，如果 |
|---|---|---|---|
| 「回傳中序／前序／後序走訪」 | n ≤ 100 | [模板 1](#template-1-recursive-pre--in--post-order-traversal--lc-94) 遞迴，`append` 的位置決定順序 | 「不用遞迴」、樹很深 → [模板 2](#template-2-iterative-traversal--stack-in-order-and-queue-level-order--lc-102) 的堆疊 |
| 「一層一層」、「右視圖」、「每個深度」 | n ≤ 2000 | [模板 2](#template-2-iterative-traversal--stack-in-order-and-queue-level-order--lc-102) 佇列長度快照 | 節點之間的空隙也要算 → [模板 11](#template-11-bfs-with-positional-index--lc-662-) |
| 「由前序與中序建出這棵樹」 | n ≤ 3000，值不重複 | [模板 3](#template-3-build-from-preorder--inorder--lc-105) 索引 map | 給的是一條帶 null 標記的字串 → [tree_codec.md](./tree_codec.md) |
| 「所有總和 = target 的根到葉路徑」 | n ≤ 5000 | [模板 4](#template-4-root-to-leaf-path-with-backtracking--lc-113) append / pop | 路徑可以從任何地方開始 → 樹上的前綴和，[tree_lca_distance.md](./tree_lca_distance.md) |
| 「直徑」、「任兩節點間的最長路徑」、「是否平衡」 | n ≤ 10⁴ | [模板 5](#template-5-height-up-answer-in-a-global--lc-543) 高度往上回傳，轉彎的路徑記在全域變數 | 需要的值來自上方（深度、目前為止的最大值）→ 往下傳，[§0-5](#0-5-depth-vs-height--the-two-numbers-a-tree-dfs-computes-) |
| 「最低共同祖先」，有 root、兩個節點都存在 | n ≤ 10⁵ | [模板 6](#template-6-lca-by-returned-presence--lc-236) 用回傳值表示找到了沒 | 是 BST → 依值往下走（[bst.md](./bst.md)）；沒有 root、只有 `parent` 指標 → [tree_lca_distance.md](./tree_lca_distance.md) |
| 「用少於 O(n) 的時間數出**完全**二元樹的節點數」 | n ≤ 5 · 10⁴ | [模板 7](#template-7-count-nodes-of-a-complete-tree--lc-222) 比較左側脊 | 不是完全樹 → O(n) 走訪 |
| 「填好每個 `next` 指標」、「常數額外空間」 | n ≤ 6000 | [模板 8](#template-8-level-linking-with-o1-space--lc-117-) 虛擬頭 + 尾指標 | 允許 O(n) 空間 → [模板 2](#template-2-iterative-traversal--stack-in-order-and-queue-level-order--lc-102) |
| 「不能同時選父節點和它的子節點」、「覆蓋每個節點」 | n ≤ 10⁴ | [模板 9](#template-9-tree-dp--return-multiple-states--lc-337-) 回傳一組狀態 | 每棵子樹一個數字就夠 → [模板 5](#template-5-height-up-answer-in-a-global--lc-543) |
| 「刪除節點並回傳森林」、「剪枝」 | n ≤ 1000 | [模板 10](#template-10-post-order-structural-modification--lc-1110-) 回傳新的子樹 | 有序的樹依範圍修剪 → [bst.md](./bst.md) |
| 「某層的寬度要算進中間的 null」、「是否為完全樹」 | n ≤ 3000 | [模板 11](#template-11-bfs-with-positional-index--lc-662-) 堆積式索引 | 依欄輸出、同格還要排序 → [tree.md](./tree.md) |
| 「相同的樹」、「對稱」、「合併兩棵樹」、函式簽名裡有兩個根 | n ≤ 2000 | [模板 12](#template-12-two-trees-in-lockstep--lc-100) 對節點對遞迴 | 要在一棵樹裡找另一棵 → 把子樹雜湊起來（[tree_codec.md](./tree_codec.md)） |

<!-- fc06fdf12b7d -->
## 0) 概念：DFS 的狀態往哪個方向流？ ⭐⭐⭐⭐⭐

> 選模板之前先回答一個問題：**一個節點需要的資訊，是從它上面來的，還是從它下面來的？** 光是這一個答案，就能把幾乎所有樹 DFS 題目分成三種形狀。

<!-- 6818c1675470 -->
### 0-1) 三種 DFS 形狀

| 形狀 | 狀態流向 | 函式簽名 | 答案從哪裡讀 | 經典 LC |
|-------|-------------|-----------|----------------|------------|
| **A — 由上而下、回看** | 往下，透過 **parent** 參數 | `dfs(node, parent, state)` | 從**全域變數** | 112、129、1448、298 |
| **B — 由上而下、前看** | 往下，**由父節點**決定 | `dfs(node, state)` | 從**全域變數** | 298、687 |
| **C — 由下而上（後序）** | **往上**，透過**回傳值** | `dfs(node) -> state` | 從**回傳值**（+ 全域變數） | 104、543、124、337 |

**A 和 B 只是同一種由上而下走訪的兩種寫法。** C 才是真正不同的演算法。在 A/B 之間選是風格問題；在由上而下與由下而上之間選是*正確性*問題。

<!-- b24dd6d1527b -->
### 0-2) A vs B — 回看 vs 前看

兩者都能解 LC 298，時間都是 O(n)、空間都是 O(h)。差別在於**父子比較這件事由誰負責**。

<!--CODE-->

<!--CODE-->

<!-- 8bb775d85861 -->
#### 並排比較

| | **A — 回看** | **B — 前看** |
|---|---|---|
| 誰做比較 | **子節點**，比自己 | **父節點**，對每個子節點比 |
| null 怎麼處理 | 基底情況 `if not node: return` | 呼叫端擋掉 `if node.left:` |
| `n` 個節點的呼叫次數 | **2n + 1**（null 也會被呼叫） | **n**（null 永遠不會被呼叫） |
| 額外參數 | 有 — `parent`（或 `parent_val`） | 沒有，直接讀 `node.val` |
| 起始呼叫 | `dfs(root, None, 0)` | `dfs(root, 1)` |
| 比較邏輯要寫幾次 | **一次** | **兩次**（左 + 右） |
| N 元樹（LC 589/1522） | `for c in node.children: dfs(c, node, s)` — 不用改 | 必須把比較邏輯再包進迴圈裡 |
| 圖／被改建成圖（LC 863） | 很自然 — `parent` = 「我從哪來」 | 很彆扭 — 沒有固定的子節點集合 |

> 這是**實測**出來的，不是嘴上說說：在一棵 15 個節點的完美樹上，寫法 A 執行了 **31** 次呼叫，寫法 B 執行了 **15** 次。多出來的那些是 `None` 子節點。都是 O(n) — B 只是常數比較小。

<!-- 960c3491f992 -->
#### 該選哪一個

- **預設選 A。** 轉移邏輯只有一份，擴展到 N 元樹和圖都不用改，而且 `if not node` 這個基底情況是其他每個樹模板早就在訓練的習慣。
- **這些情況選 B**：比較需要邊的*兩端*，而你想避開一個 null 分支 — 或者你**根本不能**走進某個子節點（剪枝），因為 B 是在遞迴之前就先做決定。
- **A 洩漏的狀態比較少。** 在 B 裡，`self.max_len` 必須在節點本身更新（不是在子節點），否則根節點自己的長度永遠不會被算進去。
- **不要把子樹序列化來找連續串。** 像 `"{val}-{left}-{right}"` 這樣的字串是整棵*樹*的前序傾印：兄弟節點緊挨在一起，真正的父子邊反而被 `#` 標記隔開，所以掃描 `+1` 步會把 `3-9-#-#-4-#-5-#-#` 讀成 9 和 4 相連。而且建出來要 O(n²)。序列化回答的是「兩棵子樹是否相同？」（LC 652），不是「這條鏈有多長？」。

<!-- f92bb104be96 -->
#### 哨兵值捷徑（以及它什麼時候會爆）

如果傳的不是節點而是一個**假的父節點值**，寫法 A 的 `if parent and ...` 就消失了：

<!--CODE-->

⚠️ **這只在狀態取決於父節點的*值*時才成立。** 如果你需要的是父節點的**身分** — 例如 **LC 993（Cousins in Binary Tree）**，兩個節點必須同深度但*父節點不同* — 就一定要傳實際的節點。在那裡傳 `parent_val`，只要兩個父節點的值剛好相同，答案就會默默地錯掉。

<!-- ee04520a37b7 -->
### 0-3) A/B 的選擇適用於*每一道*樹 DFS 題嗎？ — **不**

A vs B 這個問題只對**由上而下**的題目有意義：也就是一個節點的答案，完全由**從根走到它**的那條路徑決定。問自己：

<!--CODE-->

**C 的辨認特徵**：某個節點的答案要**把兩個子節點的結果合起來**（`left + right + node.val`），或者節點回傳的東西跟全域變數追蹤的東西不一樣。

| LC | 題目 | 形狀 | 為什麼 |
|----|---------|-------|-----|
| 112 / 113 | Path Sum I / II | **A 或 B** | 累計和從上面來 |
| 129 | Sum Root to Leaf Numbers | **A 或 B** | 往下累積 `num*10 + val` |
| 1448 | Count Good Nodes | **A 或 B** | 把 `maxSoFar` 帶下去 |
| 1026 | Max Diff Node vs Ancestor | **A 或 B** | 把 `(min, max)` 帶下去 |
| 298 | Longest Consecutive Sequence | **A 或 B** | 連續長度從上面來 |
| 993 | Cousins in Binary Tree | **只能 A** | 需要父**節點**，不是它的值 |
| 863 | All Nodes Distance K | **只能 A** | 樹被當成圖來走；`parent` = 從哪來 |
| 104 / 111 | Max / Min Depth | **C** | 需要子節點的高度 |
| 543 | Diameter | **C** | 在節點上算 `left + right` |
| 110 | Balanced Binary Tree | **C** | 比較子樹高度 |
| 124 | Max Path Sum | **C** | 回傳單邊，全域記錄兩邊相加 |
| 687 | Longest Univalue Path | **B *和* C** | 用 B 算往下的單邊，用 C 把兩邊接起來 → 見模板 9 |
| 337 | House Robber III | **C** | 回傳 `(take, skip)` 二元組 → 見模板 9 |
| 236 | LCA | **C** | 需要知道「p/q 有沒有在我下面被找到」 |

> **LC 298 是少見的三種寫法都能解的題目** — 它的路徑嚴格往下（所以由上而下可行），*而且*一棵子樹往下最長的連續段也有明確定義（所以由下而上也可行）。跟下面**模板 9（樹 DP — 由下而上回傳多個狀態）**的 C 版本對照一下 — 它是把 `cur_len` 往上回傳，而不是往下串。大部分題目只吃一種形狀。

<!-- 9776e029e0a7 -->
#### 卡住時把 A 改成 C

如果由上而下的嘗試需要子樹的資訊，機械式的修法是：**別再把累計器往下傳，改成把它往上回傳**，答案仍然放全域變數。

<!--CODE-->

<!-- ce5f0c058ace -->
### 0-4) 三種形狀共通的坑 — **是重置，不是停止**

在 LC 298（以及每一道「最長的 X 連續段」樹題）裡，連續段斷掉時必須**從 1 重新開始**，絕不是終止遞迴：

<!--CODE-->

在 4000 棵隨機樹上驗證過：寫法 A、B、C 每一個案例都跟暴力解一致，包括下面這棵之字形的樹 — 路徑 `1→2→3→4` 左右交錯，仍然是合法的，因為**唯一的規則就是父 → 子**。

<!--CODE-->

<!-- a8190b33534c -->
### 0-5) 深度 vs 高度 — 樹的 DFS 算的兩個數字 ⭐⭐⭐⭐

這是 §0-1 的同一條規則，套用在幾乎每道樹題都會問的兩個量上。要問的是：父節點在呼叫子節點**之前**，就已經知道這個值了嗎？

| 量 | 量的是 | 誰先知道 | 流向 | 形狀 | 經典 LC |
|----------|----------|--------------------|-------|-------|------------|
| **深度** | 從**根**往下到這個節點的邊數 | 父節點 | **往下**，當**參數**：`dfs(child, depth + 1)` | A / B | 102, 111, 199, 1302 |
| **高度** | 從這個節點往下到葉節點、最長路徑上的**節點數**（葉節點 = 1，空樹 = 0） | 子節點 | **往上**，當**回傳值**：`return 1 + max(l, r)` | C | 104, 110, 543, 366 |
| **距離** | 兩個節點之間的邊數 | 單靠任一方都不知道 | 兩者都要：從 LCA 量深度，或建 parent map | — | 1740, 863 → [tree_lca_distance.md](./tree_lca_distance.md#template-3-distance-between-nodes-via-lca--lc-1740) |

> **注意單位：這裡的深度算邊數（根 = 0），高度算節點數（葉節點 = 1）。** 下面的程式碼回傳的就是這兩種值，而且這樣 LC 543 的 `left_height + right_height` 直接就是邊數。
>
> **LC 104 是兩種讀法都成立的那一題。** 它的答案算的是節點數，所以等於根的高度，也等於最深葉節點的深度 + 1。因此既可以由上往下（把 `depth` 帶到每個葉節點），也可以由下往上（回傳高度）。這就是 §0-3 把深度同時列在兩個分支下的原因。

<!-- d8148012382d -->
#### 深度 — 往下傳

<!--CODE-->

三種走訪唯一的差別，就是 `append` 放在前序 / 中序 / 後序的哪個位置。走訪本身見 [模板 1](#template-1-recursive-pre--in--post-order-traversal--lc-94) 和 [模板 2](#template-2-iterative-traversal--stack-in-order-and-queue-level-order--lc-102)。

<!-- 9762f06cbbe2 -->
#### 高度 — 往上回傳

高度是後序算的：先算兩個子節點，再 `1 + max(left, right)`。它最經典的兩個用法各只講一次，不在這裡重複 — LC 543 用全域變數記錄兩條臂、回傳值只帶一條，是 [模板 5](#template-5-height-up-answer-in-a-global--lc-543) 的錨點題；LC 110 回傳 `-1` 當作「已經不平衡」的訊號一路往上短路，是 [tree_examples.md](./tree_examples.md) 裡的一張問題卡。

**全域變數不等於回傳值。** LC 543 回傳一條臂（`1 + max`），記錄的卻是兩條臂（`left + right`）。回傳 `left + right` 是最常見的 bug：父節點會去延伸一條已經轉過彎的路徑。LC 124 和 LC 687 也遵守同一條規則，只是臂換成總和、或加上同值的檢查（它們的卡片：[LC 124](./tree_lca_distance.md#binary-tree-maximum-path-sum--lc-124)、[LC 687](./tree_lca_distance.md#longest-univalue-path--lc-687)）。

<!-- 69491d5c6963 -->
#### 距離 — 兩者同時用上

距離同時需要來自上方**和**下方的資訊，所以放在 [tree_lca_distance.md](./tree_lca_distance.md)：

- **p 和 q 兩個節點之間**（LC 1740）：`d(p, q) = depth(p) + depth(q) − 2·depth(lca)`。先找 LCA，再從 LCA 跑兩次那一節的 `get_dist`。LC 1740 給的是**值**，所以 `get_dist` 比的是 `root.val == target`；上面的 `depth_of` 比的是節點本身，會回傳 `-1`。見 [Template 3：節點間距離](./tree_lca_distance.md#template-3-distance-between-nodes-via-lca--lc-1740)。
- **離 target 距離為 k 的所有節點**（LC 863）：用一次 DFS 記下每個節點的父節點，樹就變成一張無向圖，再帶著 `visited` 集合往外走。見 [Template 2：parent map + BFS](./tree_lca_distance.md#template-2-parent-map--bfs--lc-863)。

---

<!-- 5ab0cfa81ec9 -->
## 題型分類

<!-- 0457643aafa4 -->
### **模式 1：樹的走訪**
- **說明**：以特定順序走訪所有節點（前序、中序、後序、層序）
- **辨認關鍵字**：「走訪所有節點」、「印出這棵樹」、「序列化這棵樹」
- **例題**：LC 94、LC 144、LC 145、LC 102
- **模板**：用走訪模板

<!-- ce2d95e5f858 -->
### **模式 2：樹的建構**
- **說明**：從走訪序列或其他表示法建出樹
- **辨認關鍵字**：「從……建構」、「build tree」、「deserialize」
- **例題**：LC 105、LC 106、LC 108、LC 297
- **模板**：用建構模板

<!-- f7fc3d76022f -->
### **模式 3：路徑問題**
- **說明**：找出具有特定性質的路徑（和、長度、樣態）
- **辨認關鍵字**：「路徑和」、「從根到葉」、「最長路徑」
- **例題**：LC 112、LC 113、LC 257、LC 124
- **模板**：用路徑模板搭配回溯

<!-- c521f908c7e8 -->
### **模式 4：樹的性質**
- **說明**：檢查或計算樹的性質（高度、平衡、對稱）
- **辨認關鍵字**：「高度」、「平衡」、「對稱」、「直徑」
- **例題**：LC 104、LC 110、LC 101、LC 543
- **模板**：用性質檢查模板

<!-- 4b8d14f5e7b4 -->
### **模式 5：LCA 與距離**
- **說明**：找共同祖先，或計算節點之間的距離
- **辨認關鍵字**：「最近共同祖先」、「節點間的距離」
- **例題**：LC 236、LC 235、LC 863
- **模板**：用 LCA 模板

<!-- 5a547aee852d -->
### **模式 6：在樹上做二分搜尋**
- **說明**：把二分搜尋技巧套用在樹的性質上（高度、節點數、結構）
- **辨認關鍵字**：「O(log n) 時間」、「完全二元樹」、「數節點」、「找第 k 個元素」
- **例題**：LC 222（Count Complete Tree Nodes）、LC 230（Kth Smallest in BST）
- **模板**：用二分搜尋 + 樹性質模板
- **關鍵洞見**：
  - 對完全二元樹，可以在樹的結構上做二分搜尋
  - 檢查左／右子樹的性質來決定往哪邊搜
  - 時間複雜度可以從 O(n) 降到 O(log²n)

<!-- a4ca5d1819a7 -->
### 完全樹的陣列表示法

-  注意，如果我們用一個 `array` 來表示 `complete binary tree`，並且 `store the root node at index 1`
    - 那麼，任何節點的`父`節點索引就是 `[index of the node / 2]`
    - 那麼，`左子`節點的索引就是 `[index of the node * 2]`
    - 那麼，`右子`節點的索引就是 `[index of the node * 2 + 1]`
    - 參考實作：https://github.com/yennanliu/CS_basics/blob/master/data_structure/python/MinHeap.py#L36-L40
    - [video](https://leetcode.com/explore/learn/card/heap/643/heap/4017/)：講得非常好！！
    - 性質
        - 怎麼存？
            - 用陣列加索引
        - 怎麼找父節點？
            - n / 2
            - 注意：`n 是陣列裡的「索引」`
        - 怎麼找左右子節點？
            - 左子節點：n * 2
            - 右子節點：n * 2 + 1
        - 怎麼判斷一個節點是不是葉節點？
            - 檢查 i > (節點總數) / 2
        - <p align="center"><img src="../pic/complete_tree_to_array.png"></p>

<!-- 0ab24e67f7ca -->
#### 範例：

假設你有一棵像這樣的完全二元樹：

<!--CODE-->

這棵樹用**陣列（1 起始）**表示會是：

<!--CODE-->

關係：

* 索引 2 的節點（15）

  * 父節點：2 / 2 = 1 → 10
  * 左子節點：2 * 2 = 4 → 30
  * 右子節點：2 * 2 + 1 = 5 → 40

---


- 陣列轉完全樹
    - dev

- `完全二元樹（Complete binary tree）`
    - 完全二元樹是一種二元樹：`除了最後一層之外`每一層都被填滿，而且最後一層的節點全部盡量靠左。
    - [wiki](https://en.wikipedia.org/wiki/Binary_tree#:~:text=A%20complete%20binary%20tree%20is,tree%20is%20not%20necessarily%20perfect.)
    - 例子：
        - 是完全二元樹
        <p align="center"><img src="../pic/complete_binary_tree1.png"></p>
        - 不是完全二元樹
        <p align="center"><img src="../pic/not_complete_binary_tree.png"></p>

<!-- ddc7f484e4af -->
## 模板與演算法

<!-- 732480141dbe -->
### 模板比較表
| 模板類型 | 適用情境 | 做法 | 時間 | 空間 | 什麼時候用 |
|---------------|----------|----------|------|-------|--------------|
| **遞迴走訪** | 單純走訪 | 遞迴 | O(n) | O(h) | 預設選擇，程式碼乾淨 |
| **迭代走訪** | 記憶體吃緊 | 堆疊／佇列 | O(n) | O(h) | 避開遞迴的額外開銷 |
| **Morris 走訪** | 空間吃緊 | 執行緒化（threading） | O(n) | O(1) | 要求常數空間時 |
| **層序** | BFS 題目 | 佇列 | O(n) | O(w) | 一層一層處理 |
| **在樹上二分搜尋** | 完全／平衡樹 | 二分搜尋 | O(log²n) | O(log n) | 利用樹結構做最佳化 |

<!-- e26d62486ca2 -->
### 模板 1：遞迴的前序 / 中序 / 後序走訪 — LC 94

**辨認** — 「回傳前序／中序／後序走訪」、「拜訪每個節點」，或任何答案是「依某種順序排出的節點值清單」的題目。這題 n ≤ 100，但同一段程式碼就是之後每道大題的 O(n) 骨架。

**關鍵點** — 遞迴剛好拜訪每個節點一次；**那唯一一行 `append` 放在哪裡**（兩次遞迴呼叫之前、之間或之後），是決定順序的唯一因素。

<!--CODE-->

<!--CODE-->

**複雜度**
- **時間 O(n)**：每個節點呼叫一次 `dfs`，每個 null 子節點再多一次，而每次呼叫除了兩次遞迴之外只做 O(1) 的事。
- **空間 O(h)**：遞迴堆疊在目前這條根到節點的路徑上每層放一個 frame；歪斜樹 `h = n`，平衡樹是 `log n`。輸出清單不算在內。

**陷阱**
- `return [root.val] + preorder(root.left) + preorder(root.right)` 看起來漂亮，但每一層都在複製清單：n 個節點的歪斜樹（LC 94 `[1,null,2,null,3,…]`）要花 O(n²)。請 append 到同一個共用清單。
- 10⁵ 個節點的歪斜樹會超過 Python 預設的遞迴上限（1000）— 改用 [模板 2](#template-2-iterative-traversal--stack-in-order-and-queue-level-order--lc-102) 的顯式堆疊。

**不是它，如果** — 樹是 BST，而你要的是「第 k 小」或「是否合法」：走訪一樣，但提早停止和排序不變式放在 [bst.md](./bst.md)。

**練習** — LC 94 · LC 144 · LC 145 · LC 589 *（n 元樹：改成迴圈走 `children`）*

<!-- b6746e58ede3 -->
### 模板 2：迭代走訪 — 用堆疊做中序、用佇列做層序 — LC 102

**辨認** — 「一層一層」、「每個深度」、「右視圖」、「之字形」、「每層的平均」→ 佇列。「不用遞迴」、「樹可能深達 10⁵」，或是一次吐出一個節點的迭代器（LC 173）→ 堆疊。

**關鍵點** — 讀 `level_size = len(queue)` 的那一刻，佇列裡剛好就是一整層，所以內層的 `for` 迴圈只會把這一層清空，不多不少。（堆疊這邊：它存的是還欠一次拜訪的左側脊，所以頂端永遠是排序順序裡的下一個節點。）

<!--CODE-->

<!--CODE-->

**複雜度**
- **時間 O(n)**：每個節點進佇列一次、出佇列一次；內層 `for` 迴圈的次數加總起來就是 n。
- **空間 O(w)**：佇列裡最多只有相鄰的兩層；完全樹最寬的一層大約 n / 2。中序的堆疊只放一條根到節點的脊，O(h)。

**陷阱**
- Java 的 `for (int i = 0; i < queue.size(); i++)` 會在迴圈 poll 和 offer 的同時重新讀取大小，於是某些層被截短或被合併：LC 102 `[3,9,20,null,null,15,7]` 就不再回傳 `[[3],[9,20],[15,7]]`。請在迴圈開始前只讀一次大小。
- 先寫 `if not root: return []`：空樹否則會把 `None` 放進佇列，然後在 `.val` 上當掉。
- 用 `list.pop(0)` 取代 `deque.popleft()` 會讓每次出佇列都是 O(n) — 寬的樹上就變 O(n²)。

**不是它，如果** — 答案需要每個節點的**欄位**或水平空隙（LC 662、LC 987）：讓節點帶著索引，見 [模板 11](#template-11-bfs-with-positional-index--lc-662-)。

**練習** — LC 102 ⭐ · LC 199 *（每層的最後一個節點）* · LC 103 *（每隔一層反轉）* · LC 173 *（把中序堆疊做成迭代器）* · LC 314

<!-- 4fe82f005ecd -->
### 模板 3：由前序 + 中序建樹 — LC 105

**辨認** — 「由前序和中序（或中序和後序）走訪建出這棵二元樹」，值**不重複**。n ≤ 3000 → O(n²) 的 `index()` 版也會過，但面試期待的是 O(n)。

**關鍵點** — `preorder[pre_index]` 永遠是正在建的這棵子樹的根，而它在 `inorder` 裡的位置，會把剩下的範圍剛好切成左子樹的值和右子樹的值。先建左再建右，`pre_index` 就會依序走完 preorder。

<!--CODE-->

<!--CODE-->

**複雜度**
- **時間 O(n)**：每次呼叫消耗一個 preorder 的值、建立一個節點；`index_of[root_val]` 是 O(1) 的 map 查詢，用 `inorder.index()` 的話整個建樹會變成 O(n²)。
- **空間 O(n)**：`index_of` map 有 n 筆；遞迴再加 O(h)。

**手動追蹤**

<!--CODE-->

**陷阱**
- 先建 `root.right` 再建 `root.left`，preorder 就會以錯的順序被消耗：LC 105 範例 1 會回傳一棵 `[3,20,9,…]` 形狀的亂樹。（**後序 + 中序**，LC 106，要從**尾端**取根，並且**先建右**。）
- 每次呼叫都切片 `preorder[1:mid+1]`，每次就是 O(n) → 歪斜樹上變 O(n²)。
- 值重複會讓 `index_of` 壞掉；題目保證不重複，所以要把這個假設講出來。

**不是它，如果** — 你拿到的是**一條**帶 null 標記的序列（`"1,2,#,#,3"`）：那是序列化的編解碼，一個前序解析器，在 [tree_codec.md](./tree_codec.md)。

**練習** — LC 105 ⭐ · LC 106 *（後序：從尾端取根、先建右）* · LC 889 *（前序 + 後序，左子樹的根是 `preorder[1]`）* · LC 1008 *（只靠前序建 BST：用上下界取代 map）*

<!-- 397ce247e2d1 -->
### 模板 4：帶回溯的根到葉路徑 — LC 113

這張卡片的家在 [tree_lca_distance.md § Template 4](./tree_lca_distance.md#template-4-root-to-leaf-path-with-backtracking--lc-113)，那份表單負責整個根到葉路徑家族（LC 112 / 113 / 129 / 257 / 437）：同一份前序 append / pop 的 Python 與 Java 程式碼，連同試跑與陷阱。編號留在這裡，後面的模板才不用重新編號。

<!-- d9eb97e0caef -->
### 模板 5：高度往上回傳、答案記在全域變數 — LC 543

**辨認** — 「直徑」、「**任兩**節點間的最長路徑」、「最大路徑和」、「是否平衡」、「坡度」— 某個節點的答案需要兩個子節點的結果，而且最佳答案可能在一個不是根的節點轉彎。

**關鍵點** — 函式**回傳**一條臂（高度，父節點可以接著延伸），並把轉彎的路徑 `left + right` **記錄**到全域變數；全域變數絕不是回傳值，因為父節點沒辦法延伸一條已經轉過彎的路徑。

<!--CODE-->

<!--CODE-->

**複雜度**
- **時間 O(n)**：`height` 以後序對每個節點跑一次，兩次呼叫之後只做 O(1) 的事。
- **空間 O(h)**：遞迴堆疊；全域變數是 O(1)。

**陷阱**
- 回傳 `left + right` 而不是 `1 + max(left, right)`：父節點會去延伸一條轉過彎的路徑。`[1,2,3,4,5]` 在根會得到 4 而不是 3。
- 單位：`height(None) = 0` 數的是**節點**，所以 `left + right` 已經是轉彎路徑的**邊**數。再加 1 會讓 LC 543 範例 2（`[1,2]`）的答案變成 2 而不是 1。
- LC 124（最大路徑和）有負值：每條臂要用 `max(0, arm)` 截掉負數，全域變數從 `-inf` 開始，否則 `[-3]` 會回傳 0。
- 在迴圈裡再呼叫一次 `height`（由上而下寫的 LC 110）在歪斜樹上是 O(n²)；高度只算一次，用 `-1` 當「已經不平衡」的旗標回傳。

**不是它，如果** — 節點需要的值來自它的**上方**（深度、路徑上目前為止的最大值，LC 1448）：改成當參數往下傳 — 見 [§0-5](#0-5-depth-vs-height--the-two-numbers-a-tree-dfs-computes-)。

**練習** — LC 543 ⭐ · LC 104 ⭐ · LC 110 ⭐ · LC 687 · LC 124 ⭐

<!-- 7f5abcf6def5 -->
### 模板 6：用回傳值表示找到了沒的 LCA — LC 236

這張卡片的家在 [tree_lca_distance.md § Template 1](./tree_lca_distance.md#template-1-lca-by-returned-presence--lc-236)，那份表單負責整個 LCA 家族：後序「回傳我找到的東西」的 Python 與 Java 程式碼、一次試跑，以及各改一個前提的變形（BST 上的 LC 235、目標可能不存在的 LC 1644、有父指標的 LC 1650、目標是一個集合的 LC 1676）。編號留在這裡，後面的模板才不用重新編號。

<!-- 7eebc80428cf -->
### 模板 7：數完全樹的節點 — LC 222

**辨認** — 「**完全**二元樹」、「設計一個少於 O(n) 的演算法」。n 最多 5 · 10⁴ — O(n) 也會過，所以題目真正問的是追問。

**關鍵點** — 在完全樹裡，如果左子樹最左邊的深度等於右子樹的，那**左**子樹就是完美樹（`2^h − 1` 個節點）；否則**右**子樹是完美樹，少一層。不管哪種，一半用公式算，只需要遞迴進另一半。

<!--CODE-->

<!--CODE-->

**第二種解法 — 比較同一個節點的兩條脊**（同樣的界；值得會它的理由：最左與最右深度相等就**證明**這棵子樹是完美樹，這正是面試官想聽到的那句話，代價是兩邊不等時兩個子節點都要遞迴）：

<!--CODE-->

**複雜度**
- **時間 O(log² n)**：每次呼叫往下一層（共 h = log n 次呼叫），而每次呼叫在 `left_depth` 裡走兩條長度 ≤ log n 的左側脊。
- **空間 O(log n)**：每層一個堆疊 frame。

**陷阱**
- `1 << h` 算的是子樹**加上**目前的根；寫成 `(1 << h) - 1` 再替根加 1 也一樣，但忘了根，LC 222 範例 1（`[1,2,3,4,5,6]`）就會回傳 5。
- 量右子樹的**右**側脊而不是左側脊會讓判斷失效：最後一層是從左邊填起，只有左側脊才告訴你它真正的深度。

**不是它，如果** — 不保證是完全樹：只有 O(n) 的走訪是對的。

**練習** — LC 222 · LC 958 *（是否完全？BFS，[模板 11](#template-11-bfs-with-positional-index--lc-662-)）* · LC 1261 · LC 1104 *（在之字形標號的樹上做堆積索引運算）*

<!-- 95bb3eceda91 -->
### 模板 8：O(1) 空間串接同層節點 — LC 117 ⭐⭐⭐⭐⭐

**辨認** — 「把每個 `next` 指標指向它右邊的下一個節點」、「只能用常數額外空間」，而且樹**不是**完美樹（LC 117；LC 116 是完美樹的情況）。

**關鍵點** — 你站在第 `k` 層時，它的 `next` 鏈已經串好了，所以它**就是**第 `k` 層的佇列；一邊走它，一邊用**虛擬頭節點 + 移動的尾指標**把第 `k + 1` 層縫起來。

<!--CODE-->

<!--CODE-->

**複雜度**
- **時間 O(n)**：每個節點經由 `cur = cur.next` 抵達一次，經由 `tail.next` 被串一次。
- **空間 O(1)**：只有 `cur`、`dummy` 和 `tail`；模板 2 的 BFS 佇列不見了。

**陷阱**
- 完美樹的捷徑 `cur.left.next = cur.right; cur.right.next = cur.next.left` 在 LC 117 範例 1（`[1,2,3,4,5,null,7]`）會出錯：節點 3 沒有左子節點，所以 `cur.next.left` 是 `None`，5 永遠接不到 7。
- 每層都要一個新的 `dummy`；重複使用同一個，下一層是空的時候 `dummy.next` 會指著舊的那層，外層迴圈永遠不會結束。

**不是它，如果** — 題目允許 O(n) 空間，或要每層一個清單：就用一般的 [模板 2](#template-2-iterative-traversal--stack-in-order-and-queue-level-order--lc-102)。

**練習** — LC 117 · LC 116 *（完美樹：同一段程式碼原封不動）* · LC 199

<!-- 98ae277d6735 -->
### 模板 9：樹 DP — 回傳多個狀態 — LC 337 ⭐⭐⭐⭐⭐

**辨認** — 「不能選兩個**相鄰**（父子）的節點」、「覆蓋每一個節點」、「每個節點有 k 種模式」— 任何把父節點的選擇和子節點**選了什麼**綁在一起的限制。

**關鍵點** — 每次呼叫對自己這個根的**每個狀態**各回傳一個最佳值（這裡是 `(take, skip)`），父節點就能挑它自己的選擇所允許的子節點狀態。遞迴式（LC 337）：`take(n) = n.val + skip(L) + skip(R)`，`skip(n) = max(take(L), skip(L)) + max(take(R), skip(R))`。

<!--CODE-->

<!--CODE-->

**複雜度**
- **時間 O(n)**：每個節點一次後序呼叫，合併兩組值是 O(1)。
- **空間 O(h)**：遞迴堆疊；每個 frame 只放兩個小 tuple。

**手動追蹤**

<!--CODE-->

**陷阱**
- 只回傳一個「這棵子樹的最佳值」會丟掉父節點需要的資訊：貪婪的「每隔一層搶一次」在 `[2,1,3,null,4]` 上得到 6，答案是 7（搶 3 和 4）。
- 跳過一個節點**不代表**一定要搶它的子節點：`skip = take(L) + take(R)` 在鏈 `[4,1,null,2,null,3]` 上回傳 6，答案是 7（搶 4 和 3）。每個子節點要各自取最大值。
- 孫節點遞迴 `max(val + rob(grandchildren), rob(children))` 沒有 memo 的話，每棵子樹會被兩個祖先各呼叫一次 — 深的樹上是指數時間。回傳 tuple 本身就是 memo。

**變形** — 一樣是後序的「回傳我這棵子樹的資訊」骨架，只是承載的東西不同：

| LC | 題目 | 每次呼叫回傳什麼 |
|----|---------|------------------------|
| 337 | House Robber III | `(take, skip)` — 上面那個模板 |
| 968 | Binary Tree Cameras | 節點狀態：`needsCover / hasCamera / covered`（在三個狀態上做貪婪） |
| 508 | Most Frequent Subtree Sum | 子樹**和**，往上的路上記進一個 `HashMap` |
| 652 | Find Duplicate Subtrees | 一個**正規化字串** `val,left,right`，記進一個 `HashMap`；計數剛好到 2 時把節點加入答案 |
| 563 | Binary Tree Tilt | 子樹和，同時把 `abs(leftSum - rightSum)` 累加到全域變數 |
| 687 | Longest Univalue Path | 往下同值的最長單邊；全域最大值 = 左邊 + 右邊 |
| 549 | Binary Tree Longest Consecutive Sequence II | `(inc, dec)` — 從我這裡**往下**的最長遞增／遞減連續串；全域最大值 = `inc + dec - 1`（下面有完整寫法） |

<!--CODE-->

<!--CODE-->

**練習** — LC 337 · LC 563 · LC 652 · LC 549 *（下面有完整寫法）* · LC 968

<!-- 039a11a6291c -->
#### 實作變形：一個節點兩條連續串 — LC 549 Binary Tree Longest Consecutive Sequence II ⭐⭐⭐⭐

> LC 298（§0-2）的續集，改了**兩件事**：連續串可以遞增**也可以**遞減，而且可以在節點處**轉彎**
> （`子 → 父 → 子`），不再只能一路往下。
> 出處：[`binary-tree-longest-consecutive-sequence-ii.py`](../../leetcode_python/Recursion/binary-tree-longest-consecutive-sequence-ii.py)

**為什麼要回傳一組值，而不是一個數字** — 從值為 `v` 的節點看出去，值是 `v + 1` 的子節點只能延長**遞增**串，
值是 `v - 1` 的子節點只能延長**遞減**串。父節點兩件事都要問，所以每次呼叫回傳 `(inc, dec)`：
**從這個節點出發、一路往下**的最長遞增／遞減連續串。

<!--CODE-->

<!--CODE-->

**轉彎的過程**

<!--CODE-->

**三個讓它正確的關鍵**

1. **`- 1` 是因為節點被算了兩次** — 它既是遞減那一臂的最後一格，也是遞增那一臂的第一格。
   葉節點一樣成立：`1 + 1 - 1 = 1`。
2. **兩臂永遠不可能是同一個子節點** — `inc` 只會從值為 `val + 1` 的子節點長出來，`dec` 只會從 `val - 1`
   長出來，一個子節點不可能兩者皆是。所以 `inc + dec - 1` 一定是一條真實存在的路徑，不會把同一條分支
   重複算兩次。（LC 124 / 543 是靠分別寫 `left`、`right` 白拿這個保證；這裡則來自值的判斷。）
3. **全域記答案，往上只回傳單邊** — 轉彎後的長度寫進 `res`；往上只給 `(inc, dec)`。
   和 LC 124（最大路徑和）、LC 543（直徑）、LC 687（同值路徑）是同一種拆法。

**LC 298 vs LC 549**

| | LC 298 | LC 549 |
|---|--------|--------|
| 方向 | 嚴格遞增，父 → 子 | 遞增**或**遞減 |
| 形狀 | 只能一路往下 | 可以轉彎：子 → 節點 → 子 |
| 狀態 | 一個純量 `cur_len` **往下**帶（§0-2 的 A／B 型） | 一組 `(inc, dec)` **往上**回傳（C 型／本模板） |
| 答案 | `cur_len` 的全域最大值 | `inc + dec - 1` 的全域最大值 |
| 中斷規則 | 連續中斷 → `cur_len = 1` | 連續中斷 → 那一臂就停在 `1` |

> **陷阱**：把 `res` 初始化成 `0` 是安全的（空樹必須回傳 `0`，而任何真實節點至少算得到 `1 + 1 - 1 = 1`），
> 但把兩臂的初始值設成 `0` 就不安全 — 葉節點會回報 `-1`。

---

<!-- f3e958e00411 -->
### 模板 10：後序的結構性修改 — LC 1110 ⭐⭐⭐⭐

**辨認** — 「刪除節點……並回傳森林」、「剪掉所有……的子樹」、「移除值為 x 的葉子」— 樹的**形狀**會改變，結果是一個節點（或節點清單），不是一個數字。

**關鍵點** — 遞迴**回傳新的子樹**（可能是 `None`），父節點再把它**重新指派**回去：`node.left = walk(node.left, …)`。就靠這一行，不需要父指標就能把被刪的子節點拆下來。父節點被刪掉、自己卻留下來的節點是新的森林根，所以這件事以 `is_root` 往下傳。

<!--CODE-->

<!--CODE-->

**第二種解法 — BFS，由父節點那一側拆開。** 不用遞迴，所以深的樹也沒有 O(h) 的堆疊：每個節點看自己的子節點，把被刪的子節點拆掉；如果自己被刪了，就把留下來的子節點收進森林。值得會它的理由：面試官要求迭代版本，或 n 大到遞迴深度會出問題。

<!--CODE-->

**複雜度**
- **時間 O(n)**：`walk` 每個節點拜訪一次；`node.val in to_del` 是 O(1) 的 set 查詢。
- **空間 O(n)**：`to_del` 最多放 n 個值；遞迴堆疊再加 O(h)。

**陷阱**
- 呼叫 `walk(node.left, …)` 卻沒把結果指派回去，被刪的節點就還掛在樹上：LC 1110 範例 1（`[1,2,3,4,5,6,7]`，刪 `[3,5]`）回傳的樹裡還留著 3 和 5。
- 被刪節點的子節點就算自己也被刪了，仍然是「根」— 靠 `is_root and not deleted` 這個判斷，才不會把被刪的根放進森林（`[1,2]`，刪 `[1,2]` → `[]`）。
- 用**清單** `to_delete` 而不是 set 做成員判斷，每次就是 O(k)：整體 O(n · k)。

**變形**
- **LC 814（Binary Tree Pruning）** — 沒有森林，只有一棵樹，而且刪除的判斷取決於*已經剪過*的子節點，所以檢查必須放在兩次遞迴呼叫**之後**。

<!--CODE-->

<!--CODE-->

**不是它，如果** — 樹是 BST，要依**值的範圍**（LC 669）或單一鍵值（LC 450）移除節點：有序性讓你能一次丟掉整邊，見 [bst.md](./bst.md)。

**練習** — LC 1110 · LC 226 ⭐ *（回傳交換後的子樹）* · LC 814 · LC 1325 · LC 114

<!-- 61a89247aaa3 -->
### 模板 11：帶位置索引的 BFS — LC 662 ⭐⭐⭐⭐

**辨認** — 「某一層的**寬度**要算進兩端節點之間的 null 位置」、「這棵樹是否**完全**」、「垂直順序／欄」— 一種單純數每層節點數表達不出來的水平位置。

**關鍵點** — 每個節點帶著它在完全樹陣列佈局裡的索引（`left = 2i`、`right = 2i + 1`），所以索引編碼的是**含空隙在內**的位置；某層的寬度 = `last − first + 1`。

<!--CODE-->

<!--CODE-->

**第二種解法 — DFS 加上「每層第一個索引」的 map。** 一樣的堆積索引，不用佇列：先走左邊的前序走訪，會最先碰到每個深度最左邊的節點，所以那個索引只記一次，之後同一深度的節點都拿來跟它比。當解法其餘部分本來就是 DFS 時值得知道；預設仍是上面的 BFS，因為它每層重新定基準，索引保持很小。

<!--CODE-->

**複雜度**
- **時間 O(n)**：每個節點進出佇列各一次，和模板 2 一樣。
- **空間 O(w)**：佇列最多放一層（加上正在加入的子節點）。

**手動追蹤**

<!--CODE-->

**陷阱**
- 不重新定基準的話，索引每層翻倍：3000 層深的歪斜樹需要 `2^3000`，Java 的 `int` 會溢位（Python 的大整數也會變慢）。每層減掉該層第一個索引。
- `first` 必須在迴圈 pop 任何東西之前讀；在迴圈裡 `i == 0` 時讀也行，但在第一次 pop 之後讀 `queue[0]` 拿到的是第二個節點。

**變形**
- **LC 958（Check Completeness of a Binary Tree）** — 同樣是「空隙很重要」的想法，但更簡單的做法是把 `null` 子節點也推進佇列，並要求**一旦 pop 出一個 `null`，後面就不能再出現真正的節點**。

<!--CODE-->

<!--CODE-->

**不是它，如果** — 同一格內還要依列與值**排序**（LC 987）：帶著 `(row, col)`，最後再排序，見 [tree.md](./tree.md)。

**練習** — LC 662 · LC 958 · LC 314 · LC 987

<!-- 9100df92f8b5 -->
### 模板 12：兩棵樹同步走 — LC 100

**辨識** — 「兩棵樹是否相同」、「這棵樹是不是自己的鏡像」、「合併兩棵樹」、「一棵樹是否為另一棵的子樹」：輸入是**兩個**根（或一個根和它自己比），而一對節點的答案取決於它們子節點配成的那幾對的同一個答案。n ≤ 100 到 2000 → 同時走兩棵樹一次，O(n)。

**關鍵** — 一次呼叫拿著**每棵樹各一個節點**，對配好的子節點對遞迴。兩個 `None` 檢查放最前面，而且順序固定：兩個都是 `None` 算相同，只有一個是 `None` 算不同，所以到了比值那一步兩個節點一定都存在。

<!--CODE-->

<!--CODE-->

**複雜度**
- **時間 O(min(n, m))**：每次呼叫比一對，而且遇到第一對不同就停，所以不會走超過較小的那棵樹。
- **空間 O(min(h1, h2))**：遞迴堆疊，兩棵樹還共有的每一層一個 frame。

**陷阱**
- 在 `None` 檢查之前先比 `p.val != q.val`：`p = [1,2]`、`q = [1,null,2]` 在其中一邊走完的那一刻就會出錯。
- 只有一邊是 `None` 時回傳 `True`：`[1,2]` 和 `[1]` 會被判成相同。
- 怎麼配對**就是**題目本身：LC 101 配的是 `(left.left, right.right)` 和 `(left.right, right.left)`；LC 951 交換後的配對也接受；LC 617 是合併而不是比較。這些題的卡片在 [tree_examples.md](./tree_examples.md)。

**不適用時** — 要在大樹裡找另一棵樹（LC 572）：這個模板會在大樹的每個節點各跑一次，O(n · m)。用 `(value, left ID, right ID)` 指派固定大小的 ID（[tree_codec.md](./tree_codec.md)）期望是 O(n + m)；替每棵子樹建一整個字串則可能是 O(n² + m²)。

**練習** — LC 100 · LC 101 *（鏡像配對）* · LC 617 *（合併，每一對建一個新節點）* · LC 951 *（兩種配對都行）* · LC 572 *（在大樹的每個節點上跑）*

<!-- c8b75ab397b3 -->
## 依模式分類的題目

<!-- cae2934e6af7 -->
### 依模式的題目分類

<!-- cd373d7689ae -->
#### **模式 1：樹走訪題目**
| 題目 | LC # | 難度 | 關鍵技巧 | 模板 |
|---------|------|------------|---------------|----------|
| Binary Tree Inorder Traversal | 94 | Easy | 堆疊／遞迴 | 模板 1/2 |
| Binary Tree Preorder Traversal | 144 | Easy | 堆疊／遞迴 | 模板 1/2 |
| Binary Tree Postorder Traversal | 145 | Easy | 堆疊／遞迴 | 模板 1/2 |
| Binary Tree Level Order Traversal | 102 | Medium | 佇列 BFS | 模板 2 |
| Binary Tree Zigzag Level Order | 103 | Medium | BFS + 方向 | 模板 2 |
| Binary Tree Right Side View | 199 | Medium | 層序／DFS | 模板 2 |
| Binary Tree Vertical Order | 314 | Medium | BFS + 雜湊表 | 模板 2 |
| Find Bottom Left Tree Value | 513 | Medium | 層序 | 模板 2 |

<!-- 5f8d7a3be220 -->
#### **模式 1b：層序的各種變形（BFS 骨架完全相同，只有每層的彙整方式不同）**

> 這些全都是模板 2 那個 `while queue: for _ in range(level_size)` 迴圈，只改一行。骨架學一次就好。

| 題目 | LC # | 難度 | 改動的那一行 |
|---------|------|------------|---------------------------|
| Level Order Traversal II | 107 | Medium | 最後把結果串列反轉（或用 `insert(0, level)`） |
| Average of Levels | 637 | Easy | `res.append(sum(level) / len(level))` |
| Find Largest Value in Each Row | 515 | Medium | `res.append(max(level))`（取該層最大值） |
| Maximum Level Sum | 1161 | Medium | 追蹤 `sum(level)`，並回傳最大者**從 1 起算**的層號 |
| Cousins in Binary Tree | 993 | Easy | 同深度（同一層）但父節點不同 → 入佇列時順便記住父節點 |
| Maximum Width of Binary Tree | 662 | Medium | 每個節點帶著堆積索引 → **模板 11** |
| Check Completeness | 958 | Medium | `null` 子節點也入佇列 → **模板 11** 的變形 |
| Vertical Order Traversal | 987 | Hard | 像 LC 314 那樣依欄做 BFS，但同位置時要用 `(row, value)` 決勝負 → 每欄必須**排序** |

<!-- 37c0ec3b703c -->
#### **模式 2：樹建構題目**
| 題目 | LC # | 難度 | 關鍵技巧 | 模板 |
|---------|------|------------|---------------|----------|
| Construct from Preorder & Inorder | 105 | Medium | 索引對照表 | 模板 3 |
| Construct from Inorder & Postorder | 106 | Medium | 索引對照表 | 模板 3 |
| Construct from Preorder & Postorder | 889 | Medium | 遞迴 | 模板 3 |
| Convert Sorted Array to BST | 108 | Easy | 二分搜尋 | 模板 3 |
| Serialize and Deserialize Tree | 297 | Hard | BFS／DFS | 模板 3 |
| Construct from String | 536 | Medium | 堆疊／遞迴 | 模板 3 |

<!-- 3cf5429475f2 -->
#### **模式 3：路徑題目**
| 題目 | LC # | 難度 | 關鍵技巧 | 模板 |
|---------|------|------------|---------------|----------|
| Path Sum | 112 | Easy | DFS | 模板 4 |
| Path Sum II | 113 | Medium | DFS + 回溯 | 模板 4 |
| Binary Tree Paths | 257 | Easy | DFS + 記錄路徑 | 模板 4 |
| Sum Root to Leaf Numbers | 129 | Medium | DFS | 模板 4 |
| Binary Tree Maximum Path Sum | 124 | Hard | DFS + 全域最大值 | 模板 4 |
| Longest Consecutive Sequence | 298 | Medium | DFS + 計數器 | 模板 4（見 §0-2：由上而下**和**由下而上都能解） |
| Longest Consecutive Sequence II | 549 | Medium | DFS + 狀態組 | 模板 9（路徑可轉彎，`inc + dec - 1`） |
| Path Sum III | 437 | Medium | 前綴和 | 模板 4 + [樹上前綴和模板](./prefix_sum_advanced.md#template-14-prefix-sum-on-a-tree-dfs--hashmap--backtrack---lc-437) |

<!-- e9741f38c8d9 -->
#### **模式 4：樹性質題目**
| 題目 | LC # | 難度 | 關鍵技巧 | 模板 |
|---------|------|------------|---------------|----------|
| Maximum Depth | 104 | Easy | DFS／BFS | 模板 5 |
| Minimum Depth | 111 | Easy | DFS／BFS | 模板 5 |
| Balanced Binary Tree | 110 | Easy | 檢查高度 | 模板 5 |
| Diameter of Binary Tree | 543 | Easy | DFS + 取最大 | 模板 5 |
| Symmetric Tree | 101 | Easy | 鏡像檢查 | 模板 5 |
| Same Tree | 100 | Easy | 兩棵樹同步 DFS | 模板 5 |

<!-- 97286e484647 -->
#### **模式 4b：性質／雙樹 DFS 骨架的各種轉折**

| 題目 | LC # | 難度 | 轉折在哪 |
|---------|------|------------|-----------|
| Flip Equivalent Binary Trees | 951 | Medium | LC 100 的雙樹 DFS，但**兩種**配對都接受：`(L,L)&(R,R)` **或** `(L,R)&(R,L)` |
| Merge Two Binary Trees | 617 | Easy | 雙樹 DFS，但缺一個節點不算不符 — 直接回傳另一邊 |
| Max Difference Between Node and Ancestor | 1026 | Medium | 改成**由上而下**而非由下而上：把 `(minSoFar, maxSoFar)` 往下推；每片葉子上的答案是 `max - min` |
| Most Frequent Subtree Sum | 508 | Medium | 由下而上的子樹和 + 頻率表 → **模板 9** |
| Binary Tree Tilt / Longest Univalue Path | 563 / 687 | Easy / Medium | 往上回傳一個值，同時把另一個值累加進全域變數 → **模板 9** |

<!-- 8021335fc46b -->
#### **模式 5：LCA 與距離題目**
| 題目 | LC # | 難度 | 關鍵技巧 | 模板 |
|---------|------|------------|---------------|----------|
| Lowest Common Ancestor | 236 | Medium | DFS | 模板 6 |
| LCA of BST | 235 | Easy | BST 性質 | 模板 6 |
| LCA II（目標可能不存在） | 1644 | Medium | DFS + 數找到幾個 | 模板 6 的變形 |
| LCA III（有父指標、沒有 root） | 1650 | Medium | 往**上**走兩條父鏈（LC 160） | 模板 6 的變形 |
| LCA IV（N 個目標） | 1676 | Medium | 對著目標 `set` 做 DFS | 模板 6 的變形 |
| Distance K from Target | 863 | Medium | 轉成圖 | 模板 6 |
| LCA of Deepest Leaves | 1123 | Medium | DFS + 深度 | 模板 6 |

<!-- bc2103d8003c -->
#### **模式 6：在樹上二分搜尋的題目**
| 題目 | LC # | 難度 | 關鍵技巧 | 模板 |
|---------|------|------------|---------------|----------|
| Count Complete Tree Nodes | 222 | Medium | 對高度做二分搜尋 | 模板 7 |
| Kth Smallest in BST | 230 | Medium | 中序 + 二分搜尋 | 模板 7 |
| Closest BST Value | 270 | Easy | 在 BST 上二分搜尋 | 模板 7 |
| Closest BST Value II | 272 | Hard | 中序 + 雙指標 | 模板 7 |

<!-- 69f00fdec4e9 -->
### 依難度分類的完整題目清單

<!-- f3a6f4047be1 -->
#### Easy 題（基礎）
- LC 94: Binary Tree Inorder Traversal - 基本走訪
- LC 100: Same Tree - 樹的比對
- LC 101: Symmetric Tree - 鏡像性質檢查
- LC 104: Maximum Depth - 基本遞迴
- LC 108: Convert Sorted Array to BST - 陣列轉樹
- LC 110: Balanced Binary Tree - 高度計算
- LC 111: Minimum Depth - 用 BFS 找最短路徑
- LC 112: Path Sum - 簡單的路徑追蹤
- LC 144: Binary Tree Preorder Traversal - 堆疊的使用
- LC 145: Binary Tree Postorder Traversal - 堆疊操作
- LC 226: Invert Binary Tree - 樹的修改
- LC 235: LCA of BST - BST 性質
- LC 257: Binary Tree Paths - 蒐集路徑
- LC 543: Diameter of Binary Tree - 全域最大值模式
- LC 572: Subtree of Another Tree - 樹的比對

<!-- 5af40b4e75e1 -->
#### Medium 題（核心）
- LC 102: Binary Tree Level Order Traversal - BFS 基礎
- LC 103: Binary Tree Zigzag Level Order - 帶方向的層序
- LC 105: Construct from Preorder & Inorder - 索引對照表
- LC 106: Construct from Inorder & Postorder - 陣列切片
- LC 113: Path Sum II - 回溯路徑
- LC 114: Flatten Binary Tree - 原地修改
- LC 116: Populating Next Right Pointers - 串接同層節點
- LC 129: Sum Root to Leaf Numbers - 組出數字
- LC 173: Binary Search Tree Iterator - iterator 設計
- LC 199: Binary Tree Right Side View - 每層最後一個元素
- LC 222: Count Complete Tree Nodes - 在樹上二分搜尋
- LC 230: Kth Smallest in BST - 中序性質
- LC 236: Lowest Common Ancestor - 經典 LCA
- LC 298: Binary Tree Longest Consecutive - 路徑追蹤
- LC 314: Binary Tree Vertical Order - 欄位索引
- LC 437: Path Sum III - 樹上的前綴和（[模板](./prefix_sum_advanced.md#template-14-prefix-sum-on-a-tree-dfs--hashmap--backtrack---lc-437)）
- LC 513: Find Bottom Left Tree Value - 層序的變形
- LC 536: Construct from String - 解析成樹
- LC 549: Binary Tree Longest Consecutive II - 可轉彎的路徑，(inc, dec) 狀態
- LC 654: Maximum Binary Tree - 單調堆疊
- LC 863: All Nodes Distance K - 轉成圖

<!-- c16e609153d1 -->
#### Hard 題（進階）
- LC 124: Binary Tree Maximum Path Sum - 全域最佳化
- LC 297: Serialize and Deserialize - 字串轉樹
- LC 834: Sum of Distances in Tree - 換根技巧
- LC 968: Binary Tree Cameras - 樹上的貪婪

<!-- 45235d06cb2a -->
## 模式選擇策略

<!--CODE-->

<!-- 58f6a24e82c8 -->
### 決策框架
1. **辨認模式**：找關鍵字（走訪、路徑、建構、性質、祖先）
2. **選模板**：把題目需求對上模板的能力
3. **調整解法**：依特定限制改寫模板
4. **最佳化**：考慮迭代 vs 遞迴、空間 vs 時間的取捨

<!-- 2482af730730 -->
## 練習路徑

| 階段 | 題目（易 → 難） | 增加了什麼 |
|---|---|---|
| **核心** ⭐ | LC 226 ⭐ · LC 104 ⭐ · LC 100 ⭐ · LC 572 ⭐ · LC 543 ⭐ · LC 110 ⭐ · LC 102 ⭐ · LC 1448 ⭐ · LC 105 ⭐ | 交換後回傳、高度往上、兩棵樹一起遞迴、轉彎的路徑、BFS 分層、狀態往下傳、由走訪建樹 |
| **練習** | LC 94 · LC 113 · LC 199 · LC 222 · LC 117 · LC 337 · LC 1110 | 堆疊、回溯、數完全樹、O(1) 串接同層、tuple DP、結構性修改 |
| **延伸** | LC 662 · LC 652 · LC 549 · LC 968 | 位置索引、子樹 id、每個節點兩條連續串、三狀態貪婪 |

<!-- ea4fa3974f42 -->
## 總結與速查

<!-- 8dd6da7a019a -->
### 複雜度速查
| 操作 | 時間 | 空間 | 備註 |
|-----------|------|-------|-------|
| 走訪（任何順序） | O(n) | O(h) | h = 高度，平衡時為 O(log n) |
| 層序 | O(n) | O(w) | w = 最大寬度 |
| 建構 | O(n) | O(n) | 建出整棵樹 |
| 找路徑 | O(n) | O(h) | 要列出所有路徑時可能需要 O(n) |
| 檢查性質 | O(n) | O(h) | 通常掃一趟就夠 |
| LCA | O(n) | O(h) | BST 可最佳化到 O(log n) |
| 在樹上二分搜尋 | O(log²n) | O(log n) | 用於完全／平衡樹 |
| 序列化／反序列化 | O(n) | O(n) | 字串表示法 |

<!-- 5d3b63033680 -->
### 模板速查
| 模板 | 最適合 | 什麼時候別用 | 關鍵程式碼樣態 |
|----------|----------|------------|------------------|
| 遞迴走訪 | 程式碼乾淨 | 有堆疊爆掉的風險時 | 順序決定處理位置 |
| 迭代走訪 | 大樹 | 單純遞迴就夠時 | 操作堆疊／佇列 |
| 建構 | 建樹 | 修改既有的樹時 | 索引對照表是關鍵 |
| 路徑 | 根到葉 | 樹中任意路徑 | 回溯模式 |
| 性質 | 樹的度量 | 路徑題 | 由下而上計算 |
| LCA | 共同祖先 | 單純走訪 | 提早回傳模式 |
| 在樹上二分搜尋 | 完全／平衡樹 | 一般的樹 | 高度比較 + 遞迴 |

<!-- c3d0316b51ed -->
### 常見模式與技巧

<!-- 675f59b76b28 -->
#### **模式：用全域變數做最佳化**
<!--CODE-->

<!-- 60f75870cd52 -->
#### **模式：用分隔符處理層**
<!--CODE-->

<!-- 01836ffcfa95 -->
### 解題步驟
1. **分析**：釐清樹的結構與要求的輸出
2. **選擇**：依模式挑合適的模板
3. **實作**：把模板調整成符合需求
4. **最佳化**：考慮改用迭代、剪枝
5. **測試**：檢查空根、單一節點、歪斜樹

<!-- 9573365c7f69 -->
### 常見錯誤與提示

**🚫 常見錯誤：**
- **忘了基底情況**：一定要檢查 `if not root`
- **走訪途中修改樹**：可能把樹的結構搞壞
- **沒處理 null 子節點**：存取 `.left/.right` 前先檢查
- **走訪順序搞錯**：前序 ≠ 中序 ≠ 後序
- **參照 vs 值**：Python 傳的是物件參照

**✅ 最佳實務：**
- **變數名要有意義**：寫 `left_height` 而不是 `l`
- **先處理邊界情況**：空樹、單一節點
- **遞迴與迭代都想一遍**：知道它們的取捨
- **小心追蹤狀態**：用輔助函式讓邏輯更清楚
- **用歪斜樹測**：那是遞迴深度的最壞情況

<!-- 8f4467a9b550 -->
### 面試提示
1. **釐清**：問清楚樹的性質（平衡嗎？是 BST 嗎？完全嗎？）
2. **畫圖**：把小例子（3-5 個節點）畫出來
3. **切入方式**：先講遞迴解，再提一下迭代的替代做法
4. **複雜度**：一定要講出時間與空間複雜度
5. **邊界情況**：null、單一節點、全左／全右歪斜

<!-- 140a11f3d800 -->
### 相關主題
- **二元搜尋樹（BST）**：節點滿足 left < root < right 時
- **堆積**：滿足堆積性質的完全二元樹
- **圖**：樹是圖的特例（無環、連通）
- **字典樹（Trie）**：做前綴比對的樹
- **B-Tree**：資料庫用的自平衡樹

<!-- 55fad750e393 -->
### Java 實作注意事項
<!--CODE-->

<!-- 93768d96471c -->
### Python 實作注意事項
<!--CODE-->

---
**面試必會題**：LC 94、102、104、105、110、124、222、226、236、297、543
**進階題**：LC 124、222（最佳化版）、297、437、863、968
**關鍵字**：二元樹、走訪、DFS、BFS、遞迴、路徑、LCA、建構、在樹上二分搜尋、完全樹
