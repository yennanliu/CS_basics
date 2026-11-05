<!-- 2bd26ad04a83 -->
# 樹的模式模板 — 索引與走訪變形

> **範圍** — 從每個樹的模式連到教它的那一張卡片的索引，再加上核心表單沒寫出來的幾種走訪：迭代的前序與後序、之字形層序、左下角的值，以及垂直順序。
> **另見**：[binary_tree.md](./binary_tree.md) — 二元樹的模板，一個一張卡片；[tree.md](./tree.md) — 概念、樹的種類、什麼時候用哪種走訪；[tree_lca_distance.md](./tree_lca_distance.md) — LCA、節點距離與根到葉的路徑；[tree_examples.md](./tree_examples.md) — 實作過的題目；[tree_construction.md](./tree_construction.md) 與 [tree_codec.md](./tree_codec.md) — 從編碼建樹，以及把樹序列化回去；[bst.md](./bst.md) — 有序的樹。

<!-- cbeb2667caea -->
## LeetCode 題目清單

- [Tree](https://leetcode.com/problem-list/tree/)
- [Binary Tree](https://leetcode.com/problem-list/binary-tree/)
- [Depth-First Search](https://leetcode.com/problem-list/depth-first-search/)

<!-- 0be92cf1310b -->
## 概觀

這份表單以前對每個樹的模板都有自己的一份，和
[binary_tree.md](./binary_tree.md)、[tree_lca_distance.md](./tree_lca_distance.md)、
[tree_examples.md](./tree_examples.md) 裡的版本並存，結果各份漸行漸遠。現在每個模式只有一個家，下面每一節都寫明它在哪裡。留在這裡的只有幾種沒有別處可放的寫法：用顯式堆疊的前序與後序、之字形 BFS、左下角的值，以及依座標排序的垂直順序。

---

<!-- 9ff2e01bb291 -->
## 辨識

這裡只列這份表單還負責的幾種寫法；其他所有樹的訊號都在 [binary_tree.md](./binary_tree.md#recognition) 的表裡。

| 題目敘述裡的訊號 | 限制條件線索 | 該用 | 不適用時 |
|---|---|---|---|
| 「前序走訪」，再追問「用迭代」 | n ≤ 100 | [1.1)](#11-preorder-template--lc-144) 一個堆疊，先推右子節點 | 不用遞迴的中序 → [binary_tree § Template 2](./binary_tree.md#template-2-iterative-traversal--stack-in-order-and-queue-level-order--lc-102) |
| 「後序走訪」、「用迭代」 | n ≤ 100 | [1.3)](#13-postorder-template--lc-145) 兩個堆疊，或反轉一次「根-右-左」的走訪 | 順序不重要、只要拜訪到 → 任何走訪都行 |
| 「之字形層序」、「左到右與右到左交替」 | n ≤ 2000 | [1.5)](#15-bfs--direction-template--lc-103) 層序 BFS，每隔一層反轉 | 每層同一個方向 → [binary_tree § Template 2](./binary_tree.md#template-2-iterative-traversal--stack-in-order-and-queue-level-order--lc-102) |
| 「最後一列最左邊的值」 | n ≤ 10⁴ | [2.7)](#27-leftmost-value-at-maximum-depth--lc-513) 每層索引 0 的節點 | **每一層**最右邊的值（LC 199）→ [tree_examples.md](./tree_examples.md#binary-tree-right-side-view--lc-199) |
| 「垂直順序」，平手依值排 | n ≤ 1000 | [8.3)](#83-coordinate-map-traversal-template--lc-987) 收集 `(col, row, val)` 後排序 | 平手保留 BFS 順序（LC 314）→ 依欄做 BFS 分組，不排序 |

<!-- dd0ccfefd9fc -->
## 1) 樹走訪模板

| 這一節以前放的 | 現在在哪裡 |
|---|---|
| 1.2) 中序模板 — LC 94 | [binary_tree § Template 1](./binary_tree.md#template-1-recursive-pre--in--post-order-traversal--lc-94)（遞迴）與 [§ Template 2](./binary_tree.md#template-2-iterative-traversal--stack-in-order-and-queue-level-order--lc-102)（堆疊版）；O(1) 空間的 Morris 走訪在 [tree.md § Template 1](./tree.md#template-1-morris-in-order-traversal--lc-94) |
| 1.4) BFS 模板（層序）— LC 102 | [binary_tree § Template 2](./binary_tree.md#template-2-iterative-traversal--stack-in-order-and-queue-level-order--lc-102) — 佇列長度快照 |

<!-- c648949c05a7 -->
### 1.1) 前序模板 — LC 144

**模式**：根 → 左 → 右
**適用情境**：處理子節點之前需要先拿到父節點的資料
**時間複雜度**：O(n)
**空間複雜度**：O(h)，遞迴堆疊

<!-- 3cc2833efc02 -->
#### 模板程式碼

<!--CODE-->

<!--CODE-->

<!-- dcb58d306ee4 -->
#### LeetCode 題目
- LC 144: Binary Tree Preorder Traversal (Easy)
- LC 589: N-ary Tree Preorder Traversal (Easy)

---

<!-- 95ce11d55dc4 -->
### 1.3) 後序模板 — LC 145

**模式**：左 → 右 → 根
**適用情境**：處理父節點之前需要先拿到子節點的資料
**時間複雜度**：O(n)
**空間複雜度**：O(h)

<!-- 627687a5ad61 -->
#### LeetCode 題目
- LC 145: Binary Tree Postorder Traversal (Easy)
- LC 590: N-ary Tree Postorder Traversal (Easy)

---

<!-- 414944661e56 -->
### 1.5) BFS + 方向模板 — LC 103

**模式**：每一層交替方向
**適用情境**：鋸齒狀走訪
**時間複雜度**：O(n)
**空間複雜度**：O(w)

<!-- fa9e8fea8245 -->
#### LeetCode 題目
- LC 103: Binary Tree Zigzag Level Order Traversal (Medium)

---

<!-- fe79e2f68054 -->
## 2) 樹的性質模板

| 這一節以前放的 | 現在在哪裡 |
|---|---|
| 2.1) 後序高度模板 — LC 104 | [Maximum Depth of Binary Tree — LC 104](./tree_examples.md#maximum-depth-of-binary-tree--lc-104) |
| 2.2) BFS 提早停止模板 — LC 111 | [Minimum Depth of Binary Tree — LC 111](./tree_examples.md#minimum-depth-of-binary-tree--lc-111) — BFS 彈出的第一個葉子 |
| 2.3) 高度驗證模板 — LC 110 | [Balanced Binary Tree — LC 110](./tree_examples.md#balanced-binary-tree--lc-110) — 回傳 `-1` 當作不平衡的訊號 |
| 2.4) 鏡像驗證模板 — LC 101 | [Symmetric Tree — LC 101](./tree_examples.md#symmetric-tree--lc-101) |
| 2.5) 比較兩棵樹的模板 — LC 100 | [binary_tree § Template 12](./binary_tree.md#template-12-two-trees-in-lockstep--lc-100) — 兩棵樹同步走 |
| 2.6) 最小深度 — 遞迴寫法與單一子節點的陷阱 | 同一張 LC 111 卡片，它的陷阱有講只有一個子節點的情況 |

<!-- a8f937042ad6 -->
### 2.7) 最大深度上最左邊的值 — LC 513

> **關鍵** — 層序 BFS 由上往下走過每一層、每層由左往右，所以每層第一個彈出的節點就是該層最左邊的；照這樣最後記下的值就是左下角那個。

**歸約到**：[binary_tree § Template 2](./binary_tree.md#template-2-iterative-traversal--stack-in-order-and-queue-level-order--lc-102) — 佇列長度快照，只記每層索引 0 的節點，而不是整層。

<!--CODE-->

<!--CODE-->

**陷阱**
- 「最左」是對每一層而言，不是「一直往左走」：在 `[1,2,3,4,null,5,6,null,null,7]` 裡，答案 7 在**右**子節點底下。
- 先放右再放左，最**後**彈出的節點就是左下角那個：只要一個變數的版本，但整個技巧就在順序上，所以要說出來。

---

<!-- ecc3568a13c5 -->
## 3) 路徑類模板

每個路徑模板都搬到負責路徑的那份表單，或搬到轉彎路徑題所歸約到的高度卡片：

| 這一節以前放的 | 現在在哪裡 |
|---|---|
| 3.1) 全域最大值更新模板 — LC 124 | [Binary Tree Maximum Path Sum — LC 124](./tree_lca_distance.md#binary-tree-maximum-path-sum--lc-124) |
| 3.2) 路徑累加模板 — LC 112 | [Path Sum — LC 112](./tree_lca_distance.md#path-sum--lc-112) |
| 3.3) 路徑＋回溯模板 — LC 113 | [tree_lca_distance § Template 4](./tree_lca_distance.md#template-4-root-to-leaf-path-with-backtracking--lc-113) |
| 3.4) 路徑計數模板 — LC 437 | [tree_lca_distance § Template 5](./tree_lca_distance.md#template-5-downward-path-count-with-a-prefix-sum-map--lc-437) |
| 3.5) 路徑數值建構模板 — LC 129 | [Sum Root to Leaf Numbers — LC 129](./tree_lca_distance.md#sum-root-to-leaf-numbers--lc-129) |
| 3.6) 路徑狀態追蹤模板 — LC 1448 | [Count Good Nodes in Binary Tree — LC 1448](./tree_examples.md#count-good-nodes-in-binary-tree--lc-1448) |
| 3.7) 最長路徑模板 — LC 543 | [binary_tree § Template 5](./binary_tree.md#template-5-height-up-answer-in-a-global--lc-543) |
| 3.8) 同值路徑模板 — LC 687 | [Longest Univalue Path — LC 687](./tree_lca_distance.md#longest-univalue-path--lc-687) |

---

<!-- eb8ec6f94a87 -->
## 4) 距離與 LCA 模板

這四份模板已經搬出這份表單。現在由 [tree_lca_distance.md](./tree_lca_distance.md)
負責，而且每一份都講得比這裡能容納的篇幅長上好幾倍：

| 這一節原本放什麼 | 現在在哪裡 |
|---|---|
| 4.1) LCA 標準模板 — LC 236 | [LCA — LC 236](./tree_lca_distance.md#template-1-lca-by-returned-presence--lc-236)，還多了 LC 865 / 1123 的最深節點變形 |
| 4.2) 值比較模板 — LC 235 | 同一節 — LC 235 是同一份模板在 BST 上的捷徑 |
| 4.3) 路徑距離模板 — LC 1740 | [Distance Between Nodes — LC 1740](./tree_lca_distance.md#template-3-distance-between-nodes-via-lca--lc-1740) |
| 4.4) 樹轉圖模板 — LC 863 | [Move Parent Pattern](./tree_lca_distance.md#template-2-parent-map--bfs--lc-863)，那是一般化的形式 — LC 863 和 LC 742 都是它的實例 |

**從這裡該帶走的想法**：樹上每一個「距離」問題，骨子裡都是 LCA 問題，因為兩個節點之間
唯一的路徑必定經過它們的最低共同祖先 — `dist(p, q) = depth(p) + depth(q) - 2·depth(lca)`。
如果還需要*往上*走，就加上父指標，把樹當成無向圖來處理。

<!-- f67b438f4e9d -->
## 6) 樹的建構模板

建構也搬出這份表單了；由兩份 Tier 1 表單分別接手：

| 這一節原本放什麼 | 現在在哪裡 |
|---|---|
| 6.1) 建樹模板 — LC 105 / 106 | [tree_construction.md](./tree_construction.md#2-construct-binary-tree-from-preorder-and-inorder-traversal--lc-105) — 這份表單原本的 Java 模板和它的 LC 106 後序變形都併過去了 |
| 6.2) 字串轉換模板 — LC 297 | [tree_codec.md](./tree_codec.md) — 整個 codec 家族，LC 297 / 449 / 331 |
| 6.3) 字串建構模板 — LC 606 | [tree_codec.md](./tree_codec.md) — 括號格式與省略成對括號的規則 |

**從這裡該帶走的想法**：每一個建構問題都是同一套遞迴 — 從編碼中認出根節點、算出輸入中
有多少屬於各棵子樹，然後遞迴下去。只有第一步不一樣：前序的開頭、後序的結尾、某個區間的
最大值，或是第一個 `(` 之前的 token。

<!-- 53e02e6bf55e -->
## 7) 改樹模板

| 這一節以前放的 | 現在在哪裡 |
|---|---|
| 7.1) 翻轉樹模板 — LC 226 | [Invert Binary Tree — LC 226](./tree_examples.md#invert-binary-tree--lc-226) |
| 7.2) 把樹攤平的模板 — LC 114 | [Flatten Binary Tree to Linked List — LC 114](./tree_examples.md#flatten-binary-tree-to-linked-list--lc-114) |
| 7.3) 合併樹的模板 — LC 617 | [Merge Two Binary Trees — LC 617](./tree_examples.md#merge-two-binary-trees--lc-617) |
| 刪除節點並回傳森林 — LC 1110 | [binary_tree § Template 10](./binary_tree.md#template-10-post-order-structural-modification--lc-1110-) |

---

<!-- 735a42e09c65 -->
## 8) 進階樹模板

| 這一節以前放的 | 現在在哪裡 |
|---|---|
| 8.1) O(1) 空間的層連結模板 — LC 117 | [binary_tree § Template 8](./binary_tree.md#template-8-level-linking-with-o1-space--lc-117-) |
| 8.2) 後序樹 DP（回傳一對值）模板 — LC 337 | [binary_tree § Template 9](./binary_tree.md#template-9-tree-dp--return-multiple-states--lc-337-) |
| 8.4) 完全樹節點計數模板 — LC 222 | [binary_tree § Template 7](./binary_tree.md#template-7-count-nodes-of-a-complete-tree--lc-222) |

<!-- 303310d89498 -->
### 8.3) 座標對應走訪模板 — LC 987

> **關鍵** — 給每個節點一個座標（`left → col - 1`、`right → col + 1`、深度 → `row`），輸出順序就是單純依 `(col, row, val)` 排序。只用 BFS 不夠：兩個節點可能同一個 `(col, row)`，平手時要依**值**排，所以先收集、再排序。

**歸約到**：[binary_tree § Template 1](./binary_tree.md#template-1-recursive-pre--in--post-order-traversal--lc-94) — 任何把 `(row, col)` 往下帶的走訪，最後排序一次。

<!--CODE-->

<!--CODE-->

**陷阱**
- 用 BFS 依欄分組卻不排序：LC 987 範例 3 的 5 和 6 在同一個 `(col, row)`，必須輸出成 `[1,5,6]`，依值排序，而不是依拜訪順序。
- LC 314（垂直順序，沒有依值的平手規則）要的是欄內依 BFS 順序，所以那裡的排序鍵是 `(col, BFS index)`，不是 `(col, row, val)`。

---

<!-- 0ed6b9f31533 -->
## 練習路徑

| 階段 | 題目（易 → 難） | 多學到什麼 |
|---|---|---|
| **核心** | LC 144 · LC 145 · LC 103 | 兩種顯式堆疊的走訪，再來是每層方向互換的層序 BFS |
| **練習** | LC 513 · LC 589 · LC 590 | 最後一層的第一個節點；同樣的堆疊走訪用在 N 元樹上 |
| **延伸** | LC 987 · LC 314 | 座標，平手先依值、再依 BFS 順序 |
