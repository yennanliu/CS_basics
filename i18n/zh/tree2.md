<!-- 6471894571f3 -->
# 樹的模式模板 — 完整指南

> **範圍** — 每個樹的模式配一份編號好、可直接複製貼上的**模板**，Python *與* Java 都有 — 樹模板的唯一集散地。模板優先，不談理論：某一題該用哪種走訪，是 [tree.md](./tree.md) 要回答的問題。
> **另見**：[tree.md](./tree.md) — 概念、樹的種類、什麼時候用哪種走訪；[tree_lca_distance.md](./tree_lca_distance.md) — LCA、節點距離與根到葉的路徑，這份表單完全交給它；[tree_construction.md](./tree_construction.md) 與 [tree_codec.md](./tree_codec.md) — 從編碼建樹，以及把樹序列化回去；[binary_tree.md](./binary_tree.md) — DFS 的狀態怎麼在二元樹裡流動；[bst.md](./bst.md) — 有序的樹。

> **注意：** 這份檔案收錄的是詳細的走訪模板與實作程式碼。樹的概念、種類與演算法模式，請看 [tree.md](./tree.md)。

<!-- cbeb2667caea -->
## LeetCode 題目清單

- [Tree](https://leetcode.com/problem-list/tree/)
- [Binary Tree](https://leetcode.com/problem-list/binary-tree/)
- [Depth-First Search](https://leetcode.com/problem-list/depth-first-search/)

<!-- fd8c96db94a4 -->
## 概觀

這份文件提供所有樹題型模式的詳細模板，依分類整理，附上範例程式碼、說明，以及對應的 LeetCode 題目。

---

<!-- 6514b1c7f825 -->
## 1) 樹走訪模板

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

<!-- 503f3a91fe11 -->
### 1.2) 中序模板 — LC 94 ⭐⭐⭐⭐⭐

**模式**：左 → 根 → 右
**適用情境**：BST 的排序順序、樹的驗證
**時間複雜度**：O(n)
**空間複雜度**：O(h)

<!-- fea6cd485751 -->
#### LeetCode 題目
- LC 94: Binary Tree Inorder Traversal (Easy)
- LC 98: Validate Binary Search Tree (Medium)
- LC 230: Kth Smallest Element in a BST (Medium)

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

<!-- ae4f784be4ee -->
### 1.4) BFS 模板（層序） — LC 102 ⭐⭐⭐⭐⭐

**模式**：一層一層處理節點
**適用情境**：最短路徑、以層為單位的問題
**時間複雜度**：O(n)
**空間複雜度**：O(w)，w 是最大寬度

<!-- b7d822bcb193 -->
#### LeetCode 題目
- LC 102: Binary Tree Level Order Traversal (Medium)
- LC 107: Binary Tree Level Order Traversal II (Medium)
- LC 103: Binary Tree Zigzag Level Order Traversal (Medium)
- LC 199: Binary Tree Right Side View (Medium)
- LC 637: Average of Levels in Binary Tree (Easy) — 同一個迴圈，只是把每層收集起來改成做彙總

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

<!-- b55e2b7838b7 -->
## 2) 樹的性質模板

<!-- 24ed054d96f2 -->
### 2.1) 後序求高度模板 — LC 104

**模式**：由下而上算高度
**適用情境**：計算樹的高度／深度
**時間複雜度**：O(n)
**空間複雜度**：O(h)

<!-- fbdf7e606255 -->
#### LeetCode 題目
- LC 104: Maximum Depth of Binary Tree (Easy)

---

<!-- d84bea8b029f -->
### 2.2) BFS 提早結束模板 — LC 111

**模式**：條件成立就停
**適用情境**：到葉節點的最小深度
**時間複雜度**：最壞 O(n)，實際上通常更好
**空間複雜度**：O(w)

<!-- 1f16d985fa2c -->
#### LeetCode 題目
- LC 111: Minimum Depth of Binary Tree (Easy)

---

<!-- f5d93f4937e9 -->
### 2.3) 高度驗證模板 — LC 110 ⭐⭐⭐

**模式**：算高度的同時順便驗證樹的性質
**適用情境**：判斷樹是否平衡
**時間複雜度**：O(n)
**空間複雜度**：O(h)

<!-- 431e22a95d3f -->
#### LeetCode 題目
- LC 110: Balanced Binary Tree (Easy)

---

<!-- 5c9ef8ed0fcf -->
### 2.4) 鏡像驗證模板 — LC 101

**模式**：比較對稱的兩棵子樹
**適用情境**：判斷樹是否對稱
**時間複雜度**：O(n)
**空間複雜度**：O(h)

<!-- e0ac33b3c1b7 -->
#### LeetCode 題目
- LC 101: Symmetric Tree (Easy)

---

<!-- 9dc435684347 -->
### 2.5) 樹比較模板 — LC 100

**模式**：逐節點比較兩棵樹
**適用情境**：判斷兩棵樹是否完全相同
**時間複雜度**：O(n)
**空間複雜度**：O(h)

<!-- 23a2f7eb26d1 -->
#### LeetCode 題目
- LC 100: Same Tree (Easy)
- LC 572: Subtree of Another Tree (Easy)
- LC 951: Flip Equivalent Binary Trees (Medium) — **變形**：子節點可以互換，所以兩種配對都接受：`(l,l && r,r) || (l,r && r,l)`

---

<!-- e2192e7d53b5 -->
### 2.6) 最小深度 — 遞迴寫法與它的獨子陷阱

> 上面的 `2.2)` 用 BFS 解 LC 111，那才是比較好的答案，因為它碰到第一個葉節點就停。
> 但遞迴寫法還是值得知道，因為經典錯誤就藏在這裡：對只有**一個**子節點的節點來說，
> `1 + min(left, right)` 是錯的 — 缺的那一側回傳 0，於是這個節點被當成葉節點。
> 下面那兩道守衛就是修正。

**模式**：找到葉節點的最小深度
**適用情境**：到葉節點的最短路徑
**時間複雜度**：O(n)
**空間複雜度**：O(h)

<!-- 1c95c3005969 -->
### 2.7) 最大深度上最左邊的值 — LC 513

**模式**：找出最大深度那一層最左邊的節點
**適用情境**：樹的左下角值
**時間複雜度**：O(n)
**空間複雜度**：O(w)

<!-- 29699cf8e213 -->
#### LeetCode 題目
- LC 513: Find Bottom Left Tree Value (Medium)

---

<!-- 5a4b1a99e53e -->
## 3) 路徑類模板

<!-- 9fd895e3ca3c -->
### 3.1) 全域最大值更新模板 — LC 124 ⭐⭐⭐⭐

**模式**：走訪過程中追蹤全域最大值
**適用情境**：最大路徑和問題
**時間複雜度**：O(n)
**空間複雜度**：O(h)

<!-- dc3c82423766 -->
#### LeetCode 題目
- LC 124: Binary Tree Maximum Path Sum (Hard)

---

<!-- 6c424efb5fad -->
### 3.2) 路徑累加模板 — LC 112

**模式**：沿路徑累計總和
**適用情境**：判斷有沒有和為某值的路徑
**時間複雜度**：O(n)
**空間複雜度**：O(h)

<!-- 165f999f5d42 -->
#### LeetCode 題目
- LC 112: Path Sum (Easy)

---

<!-- 7233d27876cb -->
### 3.3) 路徑 + 回溯模板 — LC 113 ⭐⭐⭐⭐

**模式**：用回溯把所有路徑收集起來
**適用情境**：找出所有符合條件的路徑
**時間複雜度**：O(n)
**空間複雜度**：O(h)

<!-- 2b4f985aec08 -->
#### LeetCode 題目
- LC 113: Path Sum II (Medium)
- LC 257: Binary Tree Paths (Easy)

---

<!-- 908b6ea7d2eb -->
### 3.4) 路徑計數模板 — LC 437

**模式**：用前綴和數路徑
**適用情境**：和為目標值的路徑（起訖點任意）
**時間複雜度**：O(n)
**空間複雜度**：O(n)

<!-- 7adb02c20941 -->
#### LeetCode 題目
- LC 437: Path Sum III (Medium)

---

<!-- e3e80a0da27b -->
### 3.5) 路徑數值組建模板 — LC 129

**模式**：從根到葉一路組出一個數值
**適用情境**：計算根到葉路徑所代表的數字
**時間複雜度**：O(n)
**空間複雜度**：O(h)

<!-- 602c9d711494 -->
#### LeetCode 題目
- LC 129: Sum Root to Leaf Numbers (Medium)

---

<!-- 22998e462685 -->
### 3.6) 路徑狀態追蹤模板 — LC 1448

**模式**：沿路徑追蹤最大值
**適用情境**：數出好節點（值 >= 路徑上最大值的節點）
**時間複雜度**：O(n)
**空間複雜度**：O(h)

<!-- b4cf060f77f1 -->
#### LeetCode 題目
- LC 1448: Count Good Nodes in Binary Tree (Medium)

---

<!-- 7fd246b2e802 -->
### 3.7) 最長路徑模板 — LC 543

**模式**：找出任意兩節點之間的最長路徑
**適用情境**：樹的直徑
**時間複雜度**：O(n)
**空間複雜度**：O(h)

<!-- 90451fb2e2a2 -->
#### LeetCode 題目
- LC 543: Diameter of Binary Tree (Easy)

---

<!-- 387e71280244 -->
### 3.8) 同值路徑模板 — LC 687

**模式**：找出值都相同的最長路徑
**適用情境**：最長同值路徑
**時間複雜度**：O(n)
**空間複雜度**：O(h)

<!-- 9ea124a9da6f -->
#### LeetCode 題目
- LC 687: Longest Univalue Path (Medium)

---

<!-- 5916e9eac1cd -->
## 4) 距離與 LCA 模板

這四份模板已經搬出這份表單。現在由 [tree_lca_distance.md](./tree_lca_distance.md)
負責，而且每一份都講得比這裡能容納的篇幅長上好幾倍：

| 這一節原本放什麼 | 現在在哪裡 |
|---|---|
| 4.1) LCA 標準模板 — LC 236 | [LCA — LC 236](./tree_lca_distance.md#1-lowest-common-ancestor-lca--lc-236)，還多了 LC 865 / 1123 的最深節點變形 |
| 4.2) 值比較模板 — LC 235 | 同一節 — LC 235 是同一份模板在 BST 上的捷徑 |
| 4.3) 路徑距離模板 — LC 1740 | [Distance Between Nodes — LC 1740](./tree_lca_distance.md#3-distance-between-nodes--lc-1740) |
| 4.4) 樹轉圖模板 — LC 863 | [Move Parent Pattern](./tree_lca_distance.md#2-move-parent-pattern---bidirectional-tree-traversal)，那是一般化的形式 — LC 863 和 LC 742 都是它的實例 |

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

<!-- 9308329b914c -->
## 7) 樹的修改模板

<!-- 5a974e7785d2 -->
### 7.1) 樹翻轉模板 — LC 226

**模式**：交換左右子樹
**適用情境**：把樹鏡像／翻轉
**時間複雜度**：O(n)
**空間複雜度**：O(h)

<!-- fee2a90a9483 -->
#### LeetCode 題目
- LC 226: Invert Binary Tree (Easy)

---

<!-- afd14a6c171a -->
#### LeetCode 題目
- LC 114: Flatten Binary Tree to Linked List (Medium)

---

<!-- 99374d243497 -->
### 7.3) 樹合併模板 — LC 617

**模式**：逐節點合併兩棵樹
**適用情境**：把兩棵樹疊在一起
**時間複雜度**：O(min(n, m))
**空間複雜度**：O(min(h1, h2))

<!-- eadad15a83c8 -->
#### LeetCode 題目
- LC 617: Merge Two Binary Trees (Easy)

---

<!-- ca5abd8afd92 -->
## 8) 進階樹模板

<!-- 535c8e801000 -->
### 8.1) O(1) 空間的層串接模板 — LC 117 ⭐⭐⭐⭐⭐

**模式**：把已經串好的那一層當成**鏈結串列**，然後用 **dummy head + tail** 指標建出下一層
**適用情境**：不用 BFS 佇列就把每一層的 `next` 指標接起來
**核心想法**：每一層自己已經知道順序時，你根本不需要佇列 — 沿著 `next` 走過去，把子節點接到一個有哨兵頭的串列上，然後往下降到 `dummy.next`
**時間複雜度**：O(n)
**空間複雜度**：O(1) — 沒有佇列，也沒有遞迴

<!-- 11070d4c4f35 -->
#### 模板程式碼

<!--CODE-->

<!--CODE-->

**變形 — LC 116（完美二元樹）**：每個節點不是 0 個就是 2 個子節點，所以 dummy head 沒有必要 — 直接接 `node.left → node.right` 和 `node.right → node.next.left`，然後直接掉到 `leftmost.left`。

<!--CODE-->

<!--CODE-->

<!-- 9bc430a26c1d -->
#### LeetCode 題目
- LC 117: Populating Next Right Pointers in Each Node II (Medium)
- LC 116: Populating Next Right Pointers in Each Node (Medium)


**為什麼要 dummy head**：子節點可能缺席（LC 117 是*一般*二元樹，不是完美二元樹），所以你沒辦法
靠位置算出「下一個節點是誰」。dummy 加上 `tail` 指標會自動跳過那些洞 — 這正是為什麼*同一份*
程式碼可以同時解掉 LC 116 和 LC 117。

**追蹤**（`root = [1,2,3,4,5,null,7]`）：

<!--CODE-->

**什麼時候可以重用這招**：任何「串接／比較同一層節點」的題目，只要節點本身帶著一個備用指標
（LC 116、LC 117）。如果節點**沒有** `next` 欄位，就退回 [1.4)](#14-bfs-template-level-order--lc-102-) 的佇列 BFS。

<!-- d8051ff21307 -->
### 8.2) 後序樹 DP（回傳一對值）模板 — LC 337 ⭐⭐⭐⭐

**模式**：每個節點回傳**兩個（或 k 個）答案** — 每種狀態各一個 — 而不是單一個數字
**適用情境**：相鄰節點的限制（「不能同時取一個節點和它的子節點」），以及任何「父節點的選擇取決於子節點有沒有被取」的樹 DP
**核心想法**：回傳 `{take, skip}` 就不需要記憶化了 — 一趟單純的後序走訪本來就是 O(n)
**時間複雜度**：O(n)
**空間複雜度**：O(h)

<!-- a1912a4219e1 -->
#### LeetCode 題目
- LC 337: House Robber III (Medium)

---

<!-- 3f4bccd5aecb -->
### 8.3) 座標對應走訪模板 — LC 987

**模式**：用 DFS 幫每個節點標上 `(col, row)`，然後**依 (col, row, val) 排序**
**適用情境**：垂直／依欄輸出，而且平手時必須有確定的排序規則
**核心想法**：`left → col - 1`、`right → col + 1`、深度 → `row`。光靠 BFS 對 LC 987 是*不夠*的：兩個節點可能有相同的 `(col, row)`，而平手時要用**值**來決勝，所以要先收集再排序
**時間複雜度**：O(n log n)
**空間複雜度**：O(n)

<!-- 6e1892c5e6f5 -->
#### LeetCode 題目
- LC 987: Vertical Order Traversal of a Binary Tree (Hard)

---

<!-- 80483e23e5dd -->
### 8.4) 完全樹節點計數模板 — LC 222

**模式**：利用**完全樹**的形狀跳過整棵子樹，而不是走訪全部 `n` 個節點
**適用情境**：樹是完全樹時，用比 O(n) 更快的方式數節點
**核心想法**：如果最左深度 == 最右深度，這棵子樹就是**完美的** → 直接 `2^d - 1`，不用遞迴。否則就遞迴下去；每一層只有一個子樹不完美，所以遞迴深度是 O(log n)，而每一步要做一次 O(log n) 的深度探測
**時間複雜度**：O(log² n)
**空間複雜度**：O(log n)

<!-- e50a3826f857 -->
#### LeetCode 題目
- LC 222: Count Complete Tree Nodes (Medium)

---

<!-- dc680b6201e4 -->
## 總表：所有模板

| 模板名稱 | 模式 | 時間 | 空間 | LeetCode 題目 |
|--------------|---------|------|-------|-------------------|
| **前序模板** | 根 → 左 → 右 | O(n) | O(h) | LC 144 |
| **中序模板** | 左 → 根 → 右 | O(n) | O(h) | LC 94, 98, 230 |
| **後序模板** | 左 → 右 → 根 | O(n) | O(h) | LC 145 |
| **BFS 模板** | 一層一層 | O(n) | O(w) | LC 102, 103, 107, 199 |
| **BFS + 方向** | 各層交替方向 | O(n) | O(w) | LC 103 |
| **後序求高度** | 由下而上算高度 | O(n) | O(h) | LC 104 |
| **BFS 提早結束** | 條件成立就停 | O(n) | O(w) | LC 111 |
| **高度驗證** | 平衡檢查 | O(n) | O(h) | LC 110 |
| **鏡像驗證** | 對稱檢查 | O(n) | O(h) | LC 101 |
| **樹比較** | 比較兩棵樹 | O(n) | O(h) | LC 100, 572 |
| **全域最大值更新** | 追蹤全域最大值 | O(n) | O(h) | LC 124 |
| **路徑累加** | 沿路徑累計總和 | O(n) | O(h) | LC 112 |
| **路徑 + 回溯** | 收集所有路徑 | O(n) | O(h) | LC 113, 257 |
| **路徑計數** | 用前綴和數路徑 | O(n) | O(n) | LC 437 |
| **路徑數值組建** | 組出路徑的數值 | O(n) | O(h) | LC 129 |
| **路徑狀態追蹤** | 追蹤路徑上的最大值 | O(n) | O(h) | LC 1448 |
| **最長路徑** | 計算直徑 | O(n) | O(h) | LC 543 |
| **同值路徑** | 同值路徑 | O(n) | O(h) | LC 687 |
| **LCA 標準** → [tree_lca_distance.md](./tree_lca_distance.md#1-lowest-common-ancestor-lca--lc-236) | 找 LCA | O(n) | O(h) | LC 236 |
| **值比較** → [tree_lca_distance.md](./tree_lca_distance.md#1-lowest-common-ancestor-lca--lc-236) | BST 上的 LCA | O(h) | O(1) | LC 235 |
| **路徑距離** → [tree_lca_distance.md](./tree_lca_distance.md#3-distance-between-nodes--lc-1740) | 透過 LCA 算距離 | O(n) | O(h) | LC 1740 |
| **樹轉圖** → [tree_lca_distance.md](./tree_lca_distance.md#2-move-parent-pattern---bidirectional-tree-traversal) | 為了查詢而轉換 | O(n) | O(n) | LC 863, 742 |
| **最小深度（遞迴）** | 獨子守衛 | O(n) | O(h) | LC 111 |
| **深度最深的最左值** | 左下角的值 | O(n) | O(w) | LC 513 |
| **建樹** → [tree_construction.md](./tree_construction.md#2-construct-binary-tree-from-preorder-and-inorder-traversal--lc-105) | 從陣列建樹 | O(n) | O(n) | LC 105, 106 |
| **字串轉換** → [tree_codec.md](./tree_codec.md) | 序列化／反序列化 | O(n) | O(n) | LC 297, 449 |
| **字串建構** → [tree_codec.md](./tree_codec.md) | 樹轉字串 | O(n) | O(h) | LC 606 |
| **樹翻轉** | 鏡像樹 | O(n) | O(h) | LC 226 |
| **樹攤平** | 攤平成串列 | O(n) | O(h) | LC 114 |
| **樹合併** | 合併兩棵樹 | O(n) | O(h) | LC 617 |
| **O(1) 層串接** | dummy head + `next` 鏈 | O(n) | O(1) | LC 117, 116 |
| **後序樹 DP** | 回傳 {take, skip} 這一對 | O(n) | O(h) | LC 337 |
| **座標對應走訪** | 依 (col, row, val) 排序 | O(n log n) | O(n) | LC 987 |
| **完全樹節點計數** | 完美子樹 ⇒ 2^d − 1 | O(log² n) | O(log n) | LC 222 |

---

<!-- c1839cbb2e0a -->
## 速查指南

<!-- 8cf287966838 -->
### 各模板的使用時機

1. **需要在處理子節點前先處理根？** → 用前序模板
2. **需要排序好的順序（BST）？** → 用中序模板
3. **父節點需要子節點的資料？** → 用後序模板
4. **需要一層一層處理？** → 用 BFS 模板
5. **需要追蹤路徑上的總和／數值？** → 用路徑追蹤類模板
6. **需要找 LCA？** → [tree_lca_distance.md](./tree_lca_distance.md)
7. **需要從陣列建樹？** → [tree_construction.md](./tree_construction.md)
8. **需要修改樹的結構？** → 用樹的修改模板
9. **需要驗證樹的性質？** → 用驗證類模板
10. **需要算兩節點之間的距離？** → [tree_lca_distance.md](./tree_lca_distance.md)

---

<!-- 4bce95ffad8d -->
## 練習建議

<!-- f6e666928b96 -->
### Easy 題（從這裡開始）
- LC 144, 94, 145：基本走訪
- LC 100, 101：樹的比較
- LC 104, 111：深度計算
- LC 226：樹翻轉
- LC 617：樹合併

<!-- ac93e386d8b2 -->
### Medium 題（把功力堆起來）
- LC 102, 103, 107：層序的各種變形
- LC 105, 106：樹的建構
- LC 113, 129, 437：路徑問題
- LC 236：LCA
- LC 114：樹攤平

<!-- 083adec3c9d5 -->
### Hard 題（進入精通）
- LC 124：最大路徑和
- LC 297：序列化
- LC 1740：距離計算

---

**注意**：所有模板都假設以下的 TreeNode 定義：
<!--CODE-->

<!--CODE-->

<!-- bc0db47aaf56 -->
## LC 範例 — 沒有專屬模板的題目

> 這份檔案裡其他每一題，都在上面自己編號的模板中解掉了。下面這兩題沒有專屬的模板小節，所以放在這裡。

<!-- b7d4ab8038b5 -->
### 2-1) Validate Binary Search Tree (LC 98) — 帶上下界的 DFS
> 遞迴往下傳合法範圍 (lo, hi)；每個節點的值都必須嚴格落在界線之內。

<!--CODE-->

<!-- dba790aeacdb -->
### 2-2) Binary Tree Right Side View (LC 199) — BFS 層序
> 一層一層跑 BFS；把每層的最後一個節點記下來，那就是從右邊看得到的。

<!--CODE-->

<!-- stale: fcd3b70bf1a2 -->
### 7.2) 樹攤平模板 — LC 114

**模式**：把樹攤平成鏈結串列
**適用情境**：轉成全部往右倒的樹
**時間複雜度**：O(n)
**空間複雜度**：O(h)
