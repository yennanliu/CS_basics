<!-- 01357a6132b6 -->
# BST (Binary Search Tree)（二元搜尋樹）

> **範圍** — 只談有序的樹 —— `left < root < right` 這條不變式換來了什麼（O(log n) 搜尋、中序即排序、依範圍剪枝、順序統計）。

> **另見**：[bst_examples.md](./bst_examples.md) — 在這些模板上多加一步的 BST 題；[bst_advanced.md](./bst_advanced.md) — O(h) 的延遲式迭代器、修復壞掉的 BST、順序統計（rank）查詢，以及各種建構變形；[tree_lca_distance.md](./tree_lca_distance.md) — 路徑和題型，用不到大小順序；[binary_tree.md](./binary_tree.md) — 無序的二元樹；[tree.md](./tree.md) — 樹的通用概念；[segment_tree.md](./segment_tree.md) — 在陣列（而非樹）上做區間查詢。

<!-- 2ecddae21148 -->
## LeetCode 題目清單

- [Binary Search Tree](https://leetcode.com/problem-list/binary-search-tree/)
- [Binary Tree](https://leetcode.com/problem-list/binary-tree/)

<!-- 95ceafe390a1 -->
## 時間複雜度

| 資料結構 | 搜尋   | 插入   | 刪除   | 最小／最大  |
| -------------- | -------- | -------- | -------- | -------- |
| BST（平均）  | O(log n) | O(log n) | O(log n) | O(log n) |

> 表中是平均情況（樹大致平衡）。**最差情況（不平衡／退化成鏈）：所有操作都是 O(n)。** 最小／最大 = 最左／最右節點，也就是 O(h)。完整走訪永遠是 **O(n)**。空間是儲存的 **O(n)** 加上遞迴的 **O(h)**。

<!-- c7509238d8b9 -->
## 總覽
**二元搜尋樹（BST）** 是一種二元樹，每個節點都遵守排序性質：左子 < 父 < 右子。有了這條性質，搜尋、插入、刪除都能做得很有效率。

<!-- 02efaf70cdce -->
### 關鍵性質
- **複雜度**：見上方的[時間複雜度](#time-complexity)表
- **核心性質**：所有節點都滿足 `left < root < right`
- **中序走訪**：輸出遞增排序的序列
- **什麼時候用**：有序資料的操作、區間查詢、順序統計

<!-- c08c87b8bedf -->
### 參考資料
- [BST Visualizer](https://www.cs.usfca.edu/~galles/visualization/BST.html)
- [fucking-algorithm - BST pt.1](https://labuladong.github.io/algo/2/21/43/)
- [fucking-algorithm - BST pt.2](https://labuladong.github.io/algo/2/21/44/)
- [fucking-algorithm - BST pt.3](https://labuladong.github.io/algo/2/21/42/)

<!-- 23e5a37c4293 -->
### 樹節點的基本操作

所有 BST 模板都建立在這種樸素遞迴之上。下面兩段都沒有用到排序性質 ——
當 `left < root < right` 幫不上忙時，你就會退回這個形狀。

<!-- 74964655b13f -->
#### **改動每個節點 —— 樸素的前序遞迴**
<!--CODE-->

<!--CODE-->

逐節點比較兩棵樹（LC 100）是同一套遞迴、同時走兩棵樹；它在
[binary_tree.md § Template 12](./binary_tree.md#template-12-two-trees-in-lockstep--lc-100) 裡完整寫出。

<!-- 7ea6c35e7e37 -->
## 辨識

限制條件的提示來自 [complexity_cheatsheet.md](./complexity_cheatsheet.md) 裡的那把尺。
每一列都假設題目寫明了**二元搜尋樹**；少了這個字，這些卡片都不適用。

| 題目裡的訊號 | 限制條件提示 | 該用 | 不是這個，如果 |
|---|---|---|---|
| 「在 BST 裡搜尋／找／最接近的值」 | 預期 O(h) | [模板 1](#template-1-descend-by-comparison--lc-700) 依比較往下走 | 樹沒有順序 → 完整 DFS（[binary_tree.md](./binary_tree.md)） |
| 「插入一個值並回傳 root」 | | [模板 2](#template-2-insert-at-the-null-leaf--lc-701) 插在 null 的葉子位置 | |
| 「刪除值為 k 的節點」 | | [模板 3](#template-3-delete-with-successor-swap--lc-450) 後繼交換 | 要刪的是整個範圍 → 模板 3b |
| 「只保留 `[low, high]` 內的值」、「BST 的區間和」 | | [模板 3b](#template-3b-trim-to-a-range--lc-669-) 修剪到範圍內 | |
| 「這是不是合法 BST」 | 值會碰到 `Integer.MIN_VALUE` / `MAX_VALUE` | [模板 4](#template-4-validate-with-bounds--lc-98) 往下帶上下界 | 要找出被交換的兩個節點（LC 99）→ 中序 + `prev`（[bst_advanced.md](./bst_advanced.md)） |
| 「第 k 小」、「iterator」、「最小差值」、「眾數」 | | [模板 5](#template-5-in-order-walk-is-sorted--lc-230) 中序即排序 | 樹會更新、又要反覆問第 k 小 → 附帶大小的 BST（[bst_advanced.md](./bst_advanced.md)） |
| 「有序陣列 → 高度平衡的 BST」、「把這棵 BST 平衡」 | | [模板 6](#template-6-sorted-array-to-balanced-bst--lc-108) 取中間當根 | 輸入是鏈結串列（LC 109）→ [bst_advanced.md](./bst_advanced.md) |
| BST 上的「最近共同祖先」 | 節點 ≤ 10⁵ | [模板 10](#template-10-lca-via-bst-ordering--lc-235-) 往下走到分岔點 | 一般二元樹，或只有 parent 指標 → [tree_lca_distance.md](./tree_lca_distance.md) |
| 「路徑和」、「從根到葉」 | | 不是 BST 題 → [tree_lca_distance.md](./tree_lca_distance.md) | |

<!-- 5ab0cfa81ec9 -->
## 題型分類

<!-- 16cfbcae0e1d -->
### **模式 1：BST 搜尋與驗證**
- **說明**：找元素，或驗證 BST 性質
- **辨識關鍵字**：「search」、「find」、「validate」、「is valid BST」
- **例題**：LC 98、LC 700、LC 270、LC 285
- **模板**：用搜尋模板搭配 BST 性質

<!-- fa7b5e79ec65 -->
### **模式 2：BST 插入與刪除**
- **說明**：改動 BST 結構，同時維持性質
- **辨識關鍵字**：「insert」、「delete」、「remove」、「add node」
- **例題**：LC 450、LC 701、LC 669
- **模板**：用修改模板

<!-- 08be09bb9488 -->
### **模式 3：BST 走訪與轉換**
- **說明**：利用中序性質做排序／轉換
- **辨識關鍵字**：「Kth smallest」、「convert」、「flatten」、「sorted order」
- **例題**：LC 230、LC 173、LC 426、LC 538
- **模板**：用中序模板

<!-- 22155eee8ad3 -->
### **模式 4：BST 建構**
- **說明**：從各種輸入建出 BST，或重建／平衡既有的 BST
- **辨識關鍵字**：「construct」、「build」、「generate」、「serialize」、「balance」
- **例題**：LC 108、LC 109、LC 95、LC 96、LC 449、LC 1008、LC 1382
- **模板**：用建構模板

<!-- a0a7275eeef8 -->
### **模式 5：BST 性質與最佳化**
- **說明**：在 BST 中找最佳值或某種性質
- **辨識關鍵字**：「closest」、「LCA」、「range」、「distance」
- **例題**：LC 235、LC 530、LC 783、LC 776
- **模板**：用性質模板

<!-- bb74678e05a1 -->
### **模式 6：路徑問題**
- **說明**：牽涉根到葉、或節點到節點路徑的題目
- **辨識關鍵字**：「path sum」、「root to leaf」、「maximum path」、「consecutive sequence」
- **例題**：LC 112、LC 113、LC 257、LC 124、LC 129、LC 298、LC 437
- **模板**：本頁沒有 —— 用不到順序；見 [tree_lca_distance.md](./tree_lca_distance.md)

<!-- ddc7f484e4af -->
## 模板與演算法

<!-- 0dcdb0abf273 -->
### 模板比較表
| 模板類型 | 適用情境 | 關鍵操作 | 時間 | 空間 | 什麼時候用 |
|---------------|----------|---------------|------|-------|-------------|
| **搜尋模板** | 找值 | 二分搜尋 | O(log n) | O(1)/O(h) | 查詢某個值 |
| **插入模板** | 加節點 | 找位置 + 插入 | O(log n) | O(1)/O(h) | 加入新值 |
| **刪除模板** | 移除節點 | 找到 + 重接 | O(log n) | O(h) | 刪掉某個值 |
| **中序模板** | 有序操作 | 左-根-右 | O(n) | O(h) | 第 k 個元素、區間 |
| **建構模板** | 建 BST | 分治法 | O(n) | O(n) | 從陣列建樹 |

> **其他模板在哪裡。** 模板 7–9 已經移出，所以本頁的編號從 6 直接跳到 10。延遲式迭代器、
> 修復壞掉的 BST、rank 查詢與各種建構變形是 [bst_advanced.md](./bst_advanced.md) 的模板 1–5；
> 路徑問題在 [tree_lca_distance.md](./tree_lca_distance.md)。

<!-- d3533180953c -->
### 模板 1：依比較往下走 — LC 700

**辨識** —— 「在 **BST** 裡搜尋／找／最接近的值」、「`target` 存不存在」、「x 的 floor／ceiling」。
節點 ≤ 5,000 而且是 BST → 預期 O(h)，不是 O(n)。

**關鍵** —— 每個節點都把剩下的值切成兩半：`< node.val` 的只可能在左邊，`>` 的只可能在右邊。
所以一次比較就排除一整棵子樹，整個走法就是一條從根到葉的路徑。

<!--CODE-->

<!--CODE-->

**複雜度**
- **時間 O(h)**：`while` 每次往下走一步、從不回頭，所以最多跑 h 次：平衡時 O(log n)，
  歪斜（依序插入建出來）的樹是 O(n)。
- **空間 O(1)**：迴圈只留一個指標。遞迴寫法要付 O(h) 的呼叫堆疊。

**陷阱**
- 兩邊子樹都搜（`search(left) or search(right)`）在 `[4,2,7,1,3]` 上答案還是對的，
  但是 O(n)：那是一般二元樹的掃描，等於把 BST 丟掉了。
- 沒說「如果平衡」就保證 O(log n)：依序插入 `1, 2, 3, …, n` 建出的樹是一條鏈結串列，
  這裡每個模板在它上面都是 O(n)。

**不適用時** —— 樹沒有順序（一般二元樹）→ 只能完整 DFS
（[binary_tree.md](./binary_tree.md)）。

**練習** —— LC 700 · LC 270 *（最接近的值：往下走時記住最佳解）* · LC 285 *（後繼：
記住最後一個往左轉的節點）*

<!-- a3c603a05770 -->
### 模板 2：插在 null 的葉子位置 — LC 701

**辨識** —— 「插入 BST」、「加入一個值並回傳 root」、「依序插入這些值建出一棵 BST」。

**關鍵** —— 新值的位置，正是模板 1 搜尋它時掉出樹的那個地方。
回傳（可能是新的）子節點再接回去 —— `root.left = insert(root.left, val)` ——
就能把路徑重新接好，完全不需要 parent 指標。

<!--CODE-->

<!--CODE-->

**複雜度**
- **時間 O(h)**：每一層一次遞迴呼叫，而且只走一邊。
- **空間 O(h)**：呼叫堆疊裝著從根到葉的路徑。改成帶 `parent` 指標的 `while` 迴圈就是 O(1)。

**陷阱**
- 結尾忘了 `return root`：每個祖先都回傳 `None`，呼叫端拿回一棵空樹。
  `[4,2,7,1,3]`、val = 5 會回傳 `[]`。
- 呼叫 `insertIntoBST(root.left, val)` 卻沒把結果接回去：新葉子建出來就被丟掉，樹完全沒變。

**練習** —— LC 701 · LC 108 *（插入很多個、還要平衡）* · LC 1008 *（插入一段前序，
或用上下界做到 O(n)）*

<!-- 6c7e5d472863 -->
### 模板 3：用後繼交換來刪除 — LC 450

**辨識** —— 「刪除值為 k 的節點並回傳 root」、「從 BST 移除一個值」。

**關鍵** —— 有 **0 或 1** 個子節點的節點，直接用那個子節點取代。有 **2** 個子節點的節點
留在原位，但換成它中序後繼（右子樹的最小值）的值；那個後繼沒有左子節點，接著再從右子樹
把它刪掉，就落進簡單的情況。

<!--CODE-->

<!--CODE-->

**複雜度**
- **時間 O(h)**：找 `key` 走一條往下的路徑；找後繼和第二次 `deleteNode` 繼續沿著同一棵右子樹
  往下，所以總長還是一條路徑。
- **空間 O(h)**：沿著那條路徑的遞迴堆疊。

**試跑**

<!--CODE-->

**陷阱**
- 被覆寫的是 `root.val`，不是節點：如果呼叫端握著原本存後繼值的那個節點，它現在看到的是
  被搬走的值。值必須跟著節點走時，就改成把後繼節點本身接進來。
- 找不到 key 時樹必須保持不變：在 `[5,3,6,2,4,null,7]` 上 `key = 0` 會走到 `if not root`
  的 base case，每一層都回傳自己。
- 改用**前驅**（左子樹的最大值）一樣正確；在同一份解法裡兩種混用就不對了。
- 把 `key` 換到後繼的位置、再往**兩邊**子樹都遞迴（常見的初稿寫法）結果正確，但變成 O(n)：
  它放掉了讓這題只走一條路徑、做到 O(h) 的那個性質。

**不適用時** —— 要移除的是一整個**範圍**的值 → 模板 3b 一趟就修剪完，
不必每個值刪一次。

**練習** —— LC 450 · LC 669 *（刪除一個範圍）* · LC 776 *（拆開而不是刪除）*

<!-- 9e8ebb38072a -->
### 模板 3b：修剪到範圍內 — LC 669 ⭐

**辨識** —— 「只保留 `[low, high]` 內的值」、「刪掉範圍外的所有節點並回傳新的 root」、
「求範圍內的值的總和」（LC 938）。

**關鍵** —— 如果 `root.val < low`，root **連同整棵左子樹**都比 `low` 小，所以答案是修剪後的
右子樹；`> high` 則對稱。範圍內的 root 保留，兩個子節點都修剪。回傳修剪後的子樹，
正是把倖存的子孫提升到被刪節點位置上的那一步。

<!--CODE-->

<!--CODE-->

**複雜度**
- **時間 O(n)**：每個保留的節點只拜訪一次，每棵被丟掉的子樹一步就跳過，所以沒有節點被拜訪
  兩次。是 O(n) 而不是 O(h)，因為每個範圍內的節點都得走到，才能把它的子節點接回去。
- **空間 O(h)**：遞迴堆疊。

**試跑**

<!--CODE-->

**陷阱**
- 對範圍外的節點回傳 `None`，會連它範圍內的子孫一起丟掉。在上面那棵樹，節點 `0` 若寫成
  `if root.val < low: return None`，`2` 和 `1` 就不見了。要回傳修剪後的那一邊。
- 範圍內的 root 只修剪子節點、卻忘了把結果**接回去**（單獨寫 `trimBST(root.left, …)`），
  範圍外的節點就還掛在樹上。

**不適用時** —— 樹不是 BST：「整棵子樹都在範圍外」這一步不再成立，每個節點都得自己
檢查（後序刪除，像 LC 1110）。

**練習** —— LC 669 · LC 938 *（求和，而不是重建）* · LC 450 · LC 776 *（拆開，每個節點都留著）*

<!-- 4cec6f70c607 -->
#### **變形：求和而非重組結構 —— LC 938**

> **關鍵** —— 跟 `trimBST` 一樣的三路剪枝：範圍外的 root 會排除整整一邊，
> 所以只有合併那一步改變，從把子節點接回去，變成把值加起來。

**歸約到**：[模板 3b](#template-3b-trim-to-a-range--lc-669-) —— 求和，而不是重組結構。

<!--CODE-->

<!-- 0d710e1f131c -->
#### 比較：Trim vs Split vs Delete

| 操作 | LC # | 會刪節點嗎？ | 回傳值 | 關鍵差異 |
|-----------|------|----------------|--------------|----------------|
| **Trim** | 669 | 會（範圍外的） | 單一 `TreeNode` | 保留 [L,R] 內的節點，其餘丟掉 |
| **Split** | 776 | 不會 | `TreeNode[2]` | 拆成 2 棵樹，**所有**節點都留著 |
| **Delete** | 450 | 會（1 個） | 單一 `TreeNode` | 剛好移除 1 個指定節點 |

<!-- 0d7fda9ed147 -->
### 模板 4：用上下界驗證 — LC 98

**辨識** —— 「這是不是合法 BST」、「最大的、本身是 BST 的子樹」（LC 333）、
「每個值是否都落在祖先允許的區間內」。值可能是 `Integer.MIN_VALUE` / `MAX_VALUE`
→ 哨兵值要小心。

**關鍵** —— 節點要比的不是父節點，而是所有祖先共同允許的**開區間**：往左走會把上界收緊成
父節點的值，往右走會把下界收緊。每個節點都落在自己的區間內，樹才合法。

<!--CODE-->

<!--CODE-->

第二種解法，保留它是因為它回答了「能不能**不用遞迴**？」：同一個區間跟著每個節點放進佇列，
所以就算是 10⁴ 個節點的歪斜樹，也不會讓呼叫堆疊溢位。

<!--CODE-->

**複雜度**
- **時間 O(n)**：每個節點對自己的區間檢查一次；`and` 在第一個違規處就提早停下。
- **空間 O(h)**：遞迴堆疊。

**陷阱**
- 只拿節點跟**父節點**比，會接受 `[5,4,6,null,null,3,7]`：局部看 `3 < 6` 沒問題，
  但 `3` 在 5 的右子樹裡。要把區間帶下去。
- Java 用 `Integer.MIN_VALUE` / `MAX_VALUE` 當哨兵，會把只有一個值為 `Integer.MIN_VALUE`
  的節點的樹判成不合法。「沒有界」要用 `null`（或 `long`）。
- 用 `<=` 和 `>=`，不是 `<` 和 `>`：LC 98 裡重複值不合法，所以 `[2,2,2]` 必須回傳 false。

**不適用時** —— 面試官偏好中序：依中序走（模板 5），檢查每個值都嚴格大於前一個。
一樣 O(n)，而且能推廣到 LC 99（找出被交換的兩個節點）。

**練習** —— LC 98 ⭐ · LC 530 *（中序 + prev）* · LC 333 *（改成把上下界往上回傳）* · LC 99

<!-- 40c19c4b624e -->
### 模板 5：中序走訪即排序 — LC 230

**辨識** —— 「第 k 小」、「BST iterator」、「任兩值的最小差」、「眾數」、「轉成有序串列」——
任何關於 BST 的值**依排序順序**的問題。

**關鍵** —— 中序（左、節點、右）會依遞增順序走訪 BST 的值。迭代寫法把還沒拜訪的左邊脊線
放在堆疊上，所以可以在第 k 次 pop 就停，不必走完整棵樹。

<!--CODE-->

<!--CODE-->

**複雜度**
- **時間 O(h + k)**：內層 `while` 先往下走 h 個節點到最小值；之後每次 pop 都是排序中的下一個值，
  走到第 k 個就停。
- **空間 O(h)**：堆疊最多裝一條從根到葉的脊線。

**陷阱**
- 先建出整個排序串列（`inorder(left) + [val] + inorder(right)`），每次查詢都要 O(n) 時間和
  O(n) 空間，串列串接還會再多出 O(n·h)。
- 差一：要在 pop **之後**遞減，再檢查 `k == 0`。遞減前就檢查會回傳第 k+1 個：
  `[3,1,4,null,2]`、k = 1 會回傳 2 而不是 1。

**不適用時** —— 樹會變動、又要反覆問第 k 小（LC 230 的 follow-up）→ 在每個節點存子樹大小，
也就是
[bst_advanced.md § Template 3](./bst_advanced.md#template-3-size-augmented-order-statistic-bst--lc-703-) 的順序統計樹。

**練習** —— LC 230 ⭐ · LC 530 · LC 501 · LC 173 *（同一個堆疊，在兩次呼叫之間暫停）* · LC 538

<!-- 3bd673610f67 -->
#### **模式：反向中序做遞減**

右 → 節點 → 左的順序會依遞減走訪各個值，所以維護一個累加和，就能把每個節點換成
「所有大於等於它的值的總和」（LC 538 / LC 1038）。

<!--CODE-->

<!-- d2b5dbbe6923 -->
### 模板 6：有序陣列轉平衡 BST — LC 108

**辨識** —— 「把有序陣列／串列轉成**高度平衡**的 BST」、「把這棵 BST 重建成平衡的」
（LC 1382 = 中序 + 這個）。

**關鍵** —— 有序區間的中間元素當根，兩邊的大小最多差一個。對 `[left, mid − 1]` 和
`[mid + 1, right]` 遞迴，每一層都重複這件事，所以兄弟子樹的高度最多差 1。

<!--CODE-->

<!--CODE-->

**複雜度**
- **時間 O(n)**：每個索引恰好變成一個節點，而 `build` 每次呼叫只做 O(1) 的事。
- **空間 O(log n)**：遞迴深度就是平衡樹的高度（輸出的 O(n) 個節點不算）。

**陷阱**
- 切片（`nums[:mid]`、`nums[mid+1:]`）每一層都在複製陣列：時間是 O(n log n) 而不是 O(n)，峰值額外記憶體是 O(n)（還活著的切片每層減半）。
  傳索引。
- Python 3 裡 `mid = (left + right) / 2` 是浮點數，拿來當索引會失敗；要用 `//`。

**不適用時** —— 輸入是有序**鏈結串列**（LC 109）：每一層找中間要 O(n)，所以要嘛用快慢指標，
要嘛模擬中序建構 —— 見
[bst_advanced.md § LC 109](./bst_advanced.md#convert-sorted-list-to-binary-search-tree--lc-109)。

**練習** —— LC 108 · LC 109 · LC 1382 *（先中序，再用這個）* · LC 1008

<!-- 54ddd0e97e62 -->
#### **其他建構輸入**

有序鏈結串列（LC 109）、前序（LC 1008）、要重新平衡的既有 BST（LC 1382），以及所有相異 BST
（LC 95，LC 96 只算數量），都在 [bst_advanced.md](./bst_advanced.md#construction-variants) 的建構一節完整寫出。

值得記在腦子裡的一句話：**LC 1382 = LC 94 + LC 108** —— 用中序走訪把 BST 攤平
（模板 5），再用上面那套「取中間當根」的遞迴重建。

<!-- c2709632f6e4 -->
#### **建構模式總表**
| 輸入型態 | 做法 | 關鍵技巧 | 時間 | 空間 | LC # |
|------------|----------|---------------|------|-------|------|
| **有序陣列** | 二分搜尋 | 取中間當根 | O(n) | O(n) | 108 |
| **有序串列** | 雙指標 | 找中間節點 | O(n log n) | O(log n) | 109 |
| **前序** | 邊界檢查 | 用 min/max | O(n) | O(h) | 1008 |
| **平衡 BST** | 中序 + 重建 | 收集有序節點 | O(n) | O(n) | 1382 |
| **生成全部** | 組合列舉 | 每個都當一次根 | O(4^n/n^1.5) | O(4^n/n^1.5) | 95 |
| **計算相異數量** | 動態規劃 | 卡塔蘭數 | O(n²) | O(n) | 96 |
| **序列化** | 前序編碼 | BST 性質 | O(n) | O(n) | 449 |

<!-- 64ce4f7058b7 -->
### 模板 10：用 BST 排序性質求 LCA —— LC 235 ⭐⭐⭐⭐

**辨識** —— 「最近共同祖先」，**而且**樹是 BST。節點 ≤ 10⁵，兩個目標保證存在。

**關鍵** —— 只要 `p` 和 `q` 都比 `node.val` 小，它們就都在左子樹；都比較大就都在右子樹，
所以 LCA 在那一邊。第一個沒有把它們送往同一邊的節點（一邊一個，或節點*本身*就是其中之一）
就是分岔點，而分岔點就是 LCA。

<!--CODE-->

<!--CODE-->

**複雜度**
- **時間 O(h)**：每次迭代往下一步，只沿著一條路徑。
- **空間 O(1)**：一個指標。遞迴寫法要付 O(h) 的堆疊。

**試跑**

<!--CODE-->

**陷阱**
- 用嚴格的 `<` / `>`：當 `node` **就是** `p`（上面 p = 2、q = 4），「都比較小」和「都比較大」
  都不成立，正是這一點讓它被回傳。寫成 `<=` 會走過答案。
- 比較節點而不是值（`p < node`）沒有任何順序可言；要比 `.val`。

**不適用時** —— 樹不是 BST → 對兩邊子樹做後序搜尋，O(n)
（[tree_lca_distance.md](./tree_lca_distance.md)）；沒有 root、只有 `parent` 指標（LC 1650）→
沿著兩條 parent 鏈往上走。

**練習** —— LC 235 ⭐ · LC 236 *（沒有順序）* · LC 1650 *（parent 指標）* · LC 1644

<!-- 594e77bf1481 -->
## 依模式分類的題目

> 只有索引，沒有程式碼。`Template N` 是本頁的卡片；`advanced T N` 是 [bst_advanced.md](./bst_advanced.md) 的 Template N；只寫 `advanced` 則是那裡的一張題目卡。

<!-- cae2934e6af7 -->
### 依模式分類的題目對照

<!-- ecb9ca08b153 -->
#### **模式 1：BST 搜尋與驗證**
| 題目 | LC # | 難度 | 關鍵技巧 | 模板 |
|---------|------|------------|---------------|----------|
| Validate Binary Search Tree | 98 | Medium | 上下界 | 模板 4 |
| Search in a BST | 700 | Easy | 二分搜尋 | 模板 1 |
| Closest Binary Search Tree Value | 270 | Easy | 二分搜尋 | 模板 1 |
| Inorder Successor in BST | 285 | Medium | 中序性質 | 模板 1 |
| Two Sum IV - Input is BST | 653 | Easy | 雜湊 + 走訪 | 模板 5 |
| Find Mode in BST | 501 | Easy | 中序走訪 | 模板 5 |

<!-- 38eec28c563b -->
#### **模式 2：BST 插入與刪除**
| 題目 | LC # | 難度 | 關鍵技巧 | 模板 |
|---------|------|------------|---------------|----------|
| Insert into a BST | 701 | Medium | 遞迴插入 | 模板 2 |
| Delete Node in a BST | 450 | Medium | 三種情況 | 模板 3 |
| Trim a Binary Search Tree | 669 | Medium | 遞迴修剪 | 模板 3b |

<!-- fc14411769c2 -->
#### **模式 3：BST 走訪與轉換**
| 題目 | LC # | 難度 | 關鍵技巧 | 模板 |
|---------|------|------------|---------------|----------|
| Kth Smallest Element in BST | 230 | Medium | 中序走訪 | 模板 5 |
| BST Iterator | 173 | Medium | 堆疊 + 中序 | advanced T1 |
| Convert BST to Greater Tree | 538 | Medium | 反向中序 | 模板 5 |
| Binary Search Tree to Greater Sum Tree | 1038 | Medium | 反向中序 | 模板 5 |
| Convert Sorted List to BST | 109 | Medium | 雙指標 | advanced |
| Flatten BST to Sorted List | 426 | Medium | 中序 + 串接 | 模板 5 |
| Increasing Order Search Tree | 897 | Easy | 中序重建 | 模板 5 |
| All Elements in Two BSTs | 1305 | Medium | 兩個延遲式迭代器 + 合併 | advanced |

<!-- 6ec91e65fa1f -->
#### **模式 4：BST 建構**
| 題目 | LC # | 難度 | 關鍵技巧 | 模板 |
|---------|------|------------|---------------|----------|
| Convert Sorted Array to BST | 108 | Easy | 二分搜尋 | 模板 6 |
| Unique Binary Search Trees | 96 | Medium | DP／卡塔蘭數 | advanced |
| Unique Binary Search Trees II | 95 | Medium | 生成全部 | advanced T5 |
| Serialize and Deserialize BST | 449 | Medium | 前序編碼 | 模板 4 |
| Construct BST from Preorder | 1008 | Medium | 前序加上下界 | advanced T4 |
| Balance a Binary Search Tree | 1382 | Medium | 中序 + 重建 | advanced |

<!-- 38cf929d734c -->
#### **模式 5：BST 性質與範圍問題**
| 題目 | LC # | 難度 | 關鍵技巧 | 模板 |
|---------|------|------------|---------------|----------|
| Lowest Common Ancestor of BST | 235 | Easy | BST 性質 | 模板 10 |
| Minimum Distance Between BST Nodes | 783 | Easy | 中序相鄰差 | 模板 5 |
| Minimum Absolute Difference in BST | 530 | Easy | 中序相鄰差 | 模板 5 |
| Range Sum of BST | 938 | Easy | DFS + 剪枝 | 模板 3b |
| Split BST | 776 | Medium | 遞迴拆分 | 模板 3b |
| Largest BST Subtree | 333 | Medium | 由下而上驗證 | 模板 4 |
| Kth Largest Element in a Stream | 703 | Easy | 附帶大小資訊的 BST（rank 查詢） | advanced T3 |
| Maximum Difference Between Node and Ancestor | 1026 | Medium | 邊界往下傳遞（min/max） | advanced |

<!-- 614b94c22714 -->
#### **模式 6：路徑問題**
不是 BST 題：LC 112、113、124、129、257、437 在 [tree_lca_distance.md](./tree_lca_distance.md)，LC 298 在 [binary_tree.md](./binary_tree.md)。

<!-- 69f00fdec4e9 -->
### 依難度的完整題目清單

<!-- 23a098285dec -->
#### Easy（打底）
- LC 700: Search in a Binary Search Tree - 基本 BST 搜尋
- LC 270: Closest Binary Search Tree Value - 改寫過的搜尋
- LC 108: Convert Sorted Array to BST - 基本建構
- LC 235: Lowest Common Ancestor of a BST - 用 BST 性質
- LC 653: Two Sum IV - Input is a BST - 樹上的雙指標
- LC 530: Minimum Absolute Difference in BST - 中序性質
- LC 783: Minimum Distance Between BST Nodes - 中序走訪
- LC 897: Increasing Order Search Tree - 中序重建
- LC 938: Range Sum of BST - DFS + 剪枝
- LC 501: Find Mode in Binary Search Tree - 中序 + 計數

<!-- 92abeb66e1ac -->
#### Medium（核心）
- LC 98: Validate Binary Search Tree - 經典驗證題
- LC 173: Binary Search Tree Iterator - 設計題
- LC 230: Kth Smallest Element in a BST - 中序的應用
- LC 450: Delete Node in a BST - 複雜的結構重組
- LC 701: Insert into a BST - 基本修改
- LC 285: Inorder Successor in BST - BST 上的移動
- LC 96: Unique Binary Search Trees - 卡塔蘭數
- LC 95: Unique Binary Search Trees II - 生成所有樹
- LC 109: Convert Sorted List to BST - 串列轉樹
- LC 449: Serialize and Deserialize BST - 編碼／解碼
- LC 538: Convert BST to Greater Tree - 反向中序
- LC 669: Trim a Binary Search Tree - 遞迴修剪
- LC 776: Split BST - 進階操作
- LC 333: Largest BST Subtree - 子樹驗證
- LC 1008: Construct BST from Preorder - 堆疊解法
- LC 1038: Binary Search Tree to Greater Sum Tree - 累加
- LC 1382: Balance a Binary Search Tree - 中序 + 重建成平衡 BST
- LC 426: Convert BST to Sorted Doubly Linked List - 原地轉換

<!-- d565bade4b85 -->
#### Hard（進階）
- LC 99: Recover Binary Search Tree - 修復被交換的節點（見 [bst_advanced.md § Template 2](./bst_advanced.md#template-2-in-order-drop-detection--lc-99-)）
- LC 1373: Maximum Sum BST in Binary Tree - 複雜驗證

<!-- 91b92ac26603 -->
## 練習路線

| 階段 | 題目（易 → 難） | 增加了什麼 |
|---|---|---|
| **核心** ⭐ | LC 235 ⭐ · LC 98 ⭐ · LC 230 ⭐ | 分岔點、往下帶的區間、中序即排序 |
| **練習** | LC 700 · LC 108 · LC 701 · LC 450 · LC 669 · LC 173 | 搜尋、建構、插入、刪除、範圍修剪、可暫停的中序 |
| **延伸** | LC 1382 · LC 776 · LC 99 | 先中序再重建；拆成兩棵樹；找出被交換的兩個節點 |

<!-- d075b3f93b22 -->
## 總結與速查

**複雜度** —— 全篇只有一張表，就在本文件開頭：見
[時間複雜度](#time-complexity)。以下全部都在講*該挑哪一個*模板。

<!-- aa6be377a3e5 -->
### 決策流程圖
<!--CODE-->

<!-- b34a15b882ad -->
### 該用哪個模板？—— 快速挑選

| 題目問的是… | 用哪個模板 | 關鍵技巧 | 典型 LC 題 |
|-------------------|--------------|---------------|---------------------|
| 「找／搜尋某個值」 | 模板 1 | 二分搜尋性質 | 700, 270, 938 |
| 「插入 BST」 | 模板 2 | 遞迴插入 | 701 |
| 「從 BST 刪除」 | 模板 3 | 三種情況處理 | 450, 669 |
| 「是不是合法 BST？」 | 模板 4 | 上下界 | 98, 333 |
| 「第 k 小／大」 | 模板 5 | 中序走訪 | 230, 173 |
| 「兩節點的 LCA」 | 模板 10 | 只往同時容納兩者的那側下降 | 235 |
| 「把有序陣列轉成樹」 | 模板 6 | 二分取中間 | 108 |
| 「平衡 BST」 | [advanced § LC 1382](./bst_advanced.md#balance-a-binary-search-tree--lc-1382-) | 中序 + 重建 | 1382 |
| 「路徑和」、「最大路徑和」 | [tree_lca_distance.md](./tree_lca_distance.md) | 不是 BST 技巧 | 112, 113, 124, 437 |
| 「修復壞掉的 BST」 | [advanced § Template 2](./bst_advanced.md#template-2-in-order-drop-detection--lc-99-) | 中序 + 用 `prev` 偵測下降 | 99, 501, 530 |
| 「串流中的第 k 大／rank 查詢」 | [advanced § Template 3](./bst_advanced.md#template-3-size-augmented-order-statistic-bst--lc-703-) | 附帶大小資訊的 BST | 703 |

<!-- 16af5382c6d6 -->
### 辨識模式

**關鍵字 → 模板對照：**
- **「search」、「find」、「closest」** → 模板 1（搜尋）
- **「insert」、「add node」** → 模板 2（插入）
- **「delete」、「remove」、「trim」** → 模板 3（刪除）
- **「valid」、「validate」、「is BST」** → 模板 4（驗證）
- **「Kth」、「sorted」、「inorder」、「iterator」** → 模板 5（中序）
- **「lowest common ancestor」、「split point」** → 模板 10（用排序性質求 LCA）
- **「construct」、「build」、「convert」、「balance」、「generate」** → 模板 6（建構）
- **「path」、「sum」、「maximum path」、「consecutive」** → 不是 BST：[tree_lca_distance.md](./tree_lca_distance.md)
- **「recover」、「swapped」、「fix」、「adjacent values」、「successor」、「mode」** → [bst_advanced.md](./bst_advanced.md) 模板 2（中序 + `prev`）
- **「stream」、「after each insert」、「rank」、「how many are less than」** → [bst_advanced.md](./bst_advanced.md) 模板 3（順序統計 BST）

<!-- f1ac367e38fe -->
### 解題步驟
1. **確認能不能用 BST 性質**：`left < root < right` 派得上用場嗎？
2. **選對模板**：依操作類型決定
3. **想清楚邊界情況**：空樹、單一節點、重複值
4. **用剪枝最佳化**：跳過不必要的子樹
5. **拿退化的樹測**：最差情況

<!-- ee1fddd4635b -->
### 常見錯誤與提示

每張卡片的**陷阱**都寫出會觸發它的輸入；下面是代價最高的五個。

- **只跟父節點比的驗證** —— `[5,4,6,null,null,3,7]` 通過父子檢查，但 `3` 位在 `5` 的右邊。
  要把區間帶下去（[模板 4](#template-4-validate-with-bounds--lc-98)）。
- **丟掉回傳的子節點** —— 寫了 `insertIntoBST(root.left, val)` 卻沒有 `root.left = …`，
  葉子建出來就掉了（[模板 2](#template-2-insert-at-the-null-leaf--lc-701)）。
- **超出範圍的節點直接回傳 `None`** —— 在 LC 669 會把它底下仍在範圍內的節點一起丟掉
  （[模板 3b](#template-3b-trim-to-a-range--lc-669-)）。
- **往 LCA 走時用 `<=`** —— 在 `[6,2,8,0,4,…]` 上 `p = 2, q = 4` 會走過頭，錯過答案 `2`
  （[模板 10](#template-10-lca-via-bst-ordering--lc-235-)）。
- **直接宣稱 O(log n)** —— 依序插入 `1, 2, …, n` 會長成一條鏈，這裡每個模板在它上面都是 O(n)。
  要說「O(h)，平衡時是 O(log n)」。

<!-- be865a8c5ef2 -->
### 面試提示
1. **釐清 BST 的性質**：可以有重複值嗎？樹是平衡的嗎？
2. **講出複雜度**：平均 O(log n)、最差 O(n) 都要說
3. **提一下自平衡結構**：情境合適時提 AVL／紅黑樹
4. **善用 BST 性質**：讓對方看到你懂這個最佳化
5. **處理所有情況**：刪除尤其要涵蓋 0、1、2 個子節點

<!-- ea0beeea5cf9 -->
### 相關主題
- **自平衡 BST**：AVL 樹、紅黑樹（保證 O(log n)）
- **B 樹**：用於資料庫索引（每個節點多個鍵）
- **二元堆積**：性質不同（父 > 子）
- **字典樹（Trie）**：字串用的前綴樹
- **線段樹**：區間查詢與更新

<!-- 55fad750e393 -->
### Java 實作備註
<!--CODE-->

<!-- 4dac85b9c36b -->
### Python 實作備註
<!--CODE-->

<!-- d0d53adb4333 -->
### 挑模板的實戰心法

1. **善用 BST 性質**：題目能用到 `left < root < right`，就用模板 1-6
2. **路徑問題**：在任何二元樹上都成立，所以放在 [tree_lca_distance.md](./tree_lca_distance.md)
3. **建構**：先看輸入型態（陣列／串列／走訪序列）→ 對應不同的模板變體
4. **修改**：改完之後一律回傳 root（模板 2、3、6）
5. **不確定時**：先想想中序走訪有沒有幫助（模板 5）

---
**面試必會題**：LC 98, 108, 173, 230, 235, 450, 700, 701, 1382
**進階題**：LC 99, 333, 776, 1373
**關鍵字**：BST、二元搜尋樹、中序、有序、驗證、搜尋樹、路徑和、DFS、回溯、平衡、建構
