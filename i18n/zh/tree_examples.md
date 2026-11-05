<!-- f390446e13ad -->
# 樹 — LeetCode 實戰範例

> **範圍** — binary_tree.md 各模板的二元樹題目集：每題歸在它化約到的模板底下，只寫它多出來的那一步，模板本身不在這裡重講。
> **另見**：[binary_tree.md](./binary_tree.md) — 這些題目化約到的模板；[tree.md](./tree.md) — 樹的觀念、走訪策略與 Morris threading；[tree_lca_distance.md](./tree_lca_distance.md) — LCA、距離與路徑題；[bst_examples.md](./bst_examples.md) — 用到 BST 順序的題目；[tree_codec.md](./tree_codec.md) 與 [tree_construction.md](./tree_construction.md) — 樹的序列化與建構。

<!-- 189f22eff331 -->
## LeetCode 題目清單

- [Tree](https://leetcode.com/problem-list/tree/)
- [Binary Tree](https://leetcode.com/problem-list/binary-tree/)
- [Depth-First Search](https://leetcode.com/problem-list/depth-first-search/)
- [Breadth-First Search](https://leetcode.com/problem-list/breadth-first-search/)

<!-- 02231984c76d -->
## 概覽

經典的二元樹題目，依各題化約到的 [binary_tree.md](./binary_tree.md) 模板分組，順序與該檔的模板順序一致。模板在 binary_tree.md，這一份只放題目，每張卡只寫這題比模板多了什麼。凡是某個模板的錨點題，或已在別份檔案完整解過的題目，Index 只放一列指過去，不在這裡再抄一份。

<!-- d06edd636cd9 -->
## Index

| LC | 題目 | 歸約到 | 難度 |
|---|---|---|---|
| 545 | [Boundary of Binary Tree](#boundary-of-binary-tree--lc-545) | [binary_tree § Template 1](./binary_tree.md#template-1-recursive-pre--in--post-order-traversal--lc-94) | Medium |
| 111 | [Minimum Depth of Binary Tree](#minimum-depth-of-binary-tree--lc-111) | [binary_tree § Template 2](./binary_tree.md#template-2-iterative-traversal--stack-in-order-and-queue-level-order--lc-102) | Easy |
| 199 | [Binary Tree Right Side View](#binary-tree-right-side-view--lc-199) | [binary_tree § Template 2](./binary_tree.md#template-2-iterative-traversal--stack-in-order-and-queue-level-order--lc-102) | Medium |
| 106 | [Construct Binary Tree from Inorder and Postorder Traversal](#construct-binary-tree-from-inorder-and-postorder-traversal--lc-106) | [binary_tree § Template 3](./binary_tree.md#template-3-build-from-preorder--inorder--lc-105) | Medium |
| 1448 | [Count Good Nodes in Binary Tree](#count-good-nodes-in-binary-tree--lc-1448) | [tree_lca_distance § Template 4](./tree_lca_distance.md#template-4-root-to-leaf-path-with-backtracking--lc-113) | Medium |
| 1026 | [Maximum Difference Between Node and Ancestor](#maximum-difference-between-node-and-ancestor--lc-1026) | [tree_lca_distance § Template 4](./tree_lca_distance.md#template-4-root-to-leaf-path-with-backtracking--lc-113) | Medium |
| 104 | [Maximum Depth of Binary Tree](#maximum-depth-of-binary-tree--lc-104) | [binary_tree § Template 5](./binary_tree.md#template-5-height-up-answer-in-a-global--lc-543) | Easy |
| 110 | [Balanced Binary Tree](#balanced-binary-tree--lc-110) | [binary_tree § Template 5](./binary_tree.md#template-5-height-up-answer-in-a-global--lc-543) | Easy |
| 124 | Binary Tree Maximum Path Sum — 完整解法在 [tree_lca_distance.md](./tree_lca_distance.md#binary-tree-maximum-path-sum--lc-124) | [binary_tree § Template 5](./binary_tree.md#template-5-height-up-answer-in-a-global--lc-543) | Hard |
| 222 | Count Complete Tree Nodes — 該模板的錨點題 | [binary_tree § Template 7](./binary_tree.md#template-7-count-nodes-of-a-complete-tree--lc-222) | Medium |
| 968 | [Binary Tree Cameras](#binary-tree-cameras--lc-968) | [binary_tree § Template 9](./binary_tree.md#template-9-tree-dp--return-multiple-states--lc-337-) | Hard |
| 226 | [Invert Binary Tree](#invert-binary-tree--lc-226) | [binary_tree § Template 10](./binary_tree.md#template-10-post-order-structural-modification--lc-1110-) | Easy |
| 114 | [Flatten Binary Tree to Linked List](#flatten-binary-tree-to-linked-list--lc-114) | [binary_tree § Template 10](./binary_tree.md#template-10-post-order-structural-modification--lc-1110-) | Medium |
| 1110 | Delete Nodes And Return Forest — 該模板的錨點題 | [binary_tree § Template 10](./binary_tree.md#template-10-post-order-structural-modification--lc-1110-) | Medium |
| 662 | Maximum Width of Binary Tree — 該模板的錨點題 | [binary_tree § Template 11](./binary_tree.md#template-11-bfs-with-positional-index--lc-662-) | Medium |
| 101 | [Symmetric Tree](#symmetric-tree--lc-101) | [binary_tree § Template 12](./binary_tree.md#template-12-two-trees-in-lockstep--lc-100) | Easy |
| 617 | [Merge Two Binary Trees](#merge-two-binary-trees--lc-617) | [binary_tree § Template 12](./binary_tree.md#template-12-two-trees-in-lockstep--lc-100) | Easy |
| 951 | [Flip Equivalent Binary Trees](#flip-equivalent-binary-trees--lc-951) | [binary_tree § Template 12](./binary_tree.md#template-12-two-trees-in-lockstep--lc-100) | Medium |
| 2415 | [Reverse Odd Levels of Binary Tree](#reverse-odd-levels-of-binary-tree--lc-2415) | [binary_tree § Template 12](./binary_tree.md#template-12-two-trees-in-lockstep--lc-100) | Medium |
| 98 | Validate Binary Search Tree — 該模板的錨點題 | [bst § Template 4](./bst.md#template-4-validate-with-bounds--lc-98) | Medium |

高度 vs 深度，以及深度搜尋用 `-1` 當「沒找到」的哨兵值，統一在 [binary_tree.md § 0-5](./binary_tree.md#0-5-depth-vs-height--the-two-numbers-a-tree-dfs-computes-) 講一次。

<!-- 3b3708f1a0eb -->
## 遞迴走訪範例

<!-- d0f66cb70a96 -->
### Boundary of Binary Tree — LC 545

> **關鍵** — 邊界是三段走訪接起來的：左邊界用**前序**（往下走時記錄），所有葉子由左到右，再接右邊界用**後序**（回程時記錄，所以是由下往上）。兩段邊界走訪都不記葉子，角落的節點才不會被列兩次。

**歸約到**：[binary_tree § Template 1](./binary_tree.md#template-1-recursive-pre--in--post-order-traversal--lc-94) — 一次走訪變成三次，每段都依它在邊界上被讀出的順序記錄。

<!--CODE-->

**陷阱**
- 邊界上的節點只有右子節點：`[1,2,null,null,3,…]` — 沒有左子節點時，左邊界要接著往**右**子節點走。只跟著 `.left` 走會提早停下。
- `root = [1]`：root 本身是葉子，再收一次葉子會印出 `[1, 1]`。要先擋掉 root。
- `root = [1,null,2,3,4]`：root 沒有左子樹，所以左邊界只有 `[1]`；葉子是 `3, 4`，右邊界是 `2`，答案 `[1,3,4,2]`。

<!-- 91c4f81bd928 -->
## 層序範例

<!-- 47570f8cda22 -->
### Minimum Depth of Binary Tree — LC 111

> **關鍵** — BFS 依深度順序抵達節點，所以第一個出佇列的**葉子**就是最淺的，搜尋可以就此停止；遞迴版的 `min` 則必須特別處理只有一個子節點的節點，因為空的那一側不是葉子。

**歸約到**：[binary_tree § Template 2](./binary_tree.md#template-2-iterative-traversal--stack-in-order-and-queue-level-order--lc-102) — 層序佇列，加上遇到第一個葉子就提早結束。

<!--CODE-->

**陷阱**
- 遞迴寫成 `1 + min(minDepth(left), minDepth(right))`，在 `[2,null,3,null,4,null,5,null,6]` 上會回傳 1：空的左子節點得 0，但它不是葉子。正確答案是 5。缺一個子節點時，只往另一邊遞迴。
- LC 104（最大深度）沒有這個陷阱：`max` 永遠不會挑空的那一側。

<!-- 9b546bb8160f -->
### Binary Tree Right Side View — LC 199

> **關鍵** — 從右邊看到的節點，就是每一層由左到右的**最後一個**節點；先記下該層的大小，才知道哪一次出佇列是最後一個。

**歸約到**：[binary_tree § Template 2](./binary_tree.md#template-2-iterative-traversal--stack-in-order-and-queue-level-order--lc-102) — 每層的歸納只保留最後一個節點。

<!--CODE-->

**陷阱**
- 只沿著右子節點走：`[1,2,3,4]` 第 2 層看得到 `4`，雖然它掛在**左**子樹下。答案是 `[1,3,4]`，不是 `[1,3]`。
- 先走右再走左、在每個新深度記下第一個節點的 DFS 也行，空間 O(h)；`len(result) == depth` 就是「第一次到這個深度」的判斷。

<!-- 105a5a7e5506 -->
## 由走訪序建樹範例

<!-- 9a3c8fb49e34 -->
### Construct Binary Tree from Inorder and Postorder Traversal — LC 106

> **關鍵** — postorder 的**最後一個**值是 root，所以從尾端往回讀；倒著讀是 root、右、左，因此必須先建**右**子樹再建左子樹。

**歸約到**：[binary_tree § Template 3](./binary_tree.md#template-3-build-from-preorder--inorder--lc-105) — 同樣用 inorder 切分，只是 root 從另一端取，兩個遞迴呼叫對調。

<!--CODE-->

**陷阱**
- 先建左子樹：從 postorder 尾端讀到的下一個值屬於右子樹，所以 `inorder = [9,3,15,20,7]`、`postorder = [9,15,7,20,3]` 建出來 20 會跑到左邊。
- 每次呼叫都切片（`inorder[:idx]`、`postorder[:idx]`）並呼叫 `inorder.index(...)`，各是 O(n)，在歪斜的樹上就是 O(n²)；用索引 map 加一個移動的指標才是 O(n)。

<!-- 0779c45f30c1 -->
## 根到節點狀態範例

<!-- 7e1e073c0a38 -->
### Count Good Nodes in Binary Tree — LC 1448

> **關鍵** — 一個節點是不是「好節點」只取決於它**上方**的最大值，所以這個最大值當參數往下傳；它是 `int`，每次呼叫各有一份，回程時沒有東西要復原。

**歸約到**：[tree_lca_distance § Template 4](./tree_lca_distance.md#template-4-root-to-leaf-path-with-backtracking--lc-113) — 根到節點的狀態是一個數字而不是 list，所以回溯那一步就不見了。

<!--CODE-->

**陷阱**
- 用 `>` 取代 `>=` 會漏掉等於目前最大值的節點：`[3,3,null,4,2]` 中第二個 3 是好節點，答案是 3，用 `>` 會回傳 2。
- 起始值設成 0 而不是 `root.val`，遇到負數就錯；root 一定是好節點，所以用它自己的值（或 `-inf`）當起點。

<!-- 17171bd2c22e -->
### Maximum Difference Between Node and Ancestor — LC 1026

> **關鍵** — 一條路徑上最大的 `|祖先 − 子孫|` 就是這條路徑的最大值減最小值，所以把這兩個數往下帶，
> 路徑結束時讀出 `high − low` 即可。不用比較任何一對，也用不到 BST 的順序。

**歸約到**：[tree_lca_distance § Template 4](./tree_lca_distance.md#template-4-root-to-leaf-path-with-backtracking--lc-113) —— 沿根到葉路徑往下帶狀態；
和 [bst § Template 4](./bst.md#template-4-validate-with-bounds--lc-98) 的上下界是同樣兩個值，只是拿來擴張而不是檢查。

<!--CODE-->

**陷阱**
- 每個節點只跟父節點比：LC 1026 的範例 1，
  `[8,3,10,1,6,null,14,null,null,4,7,13]`，會回傳 5（8 − 3）而不是 7（8 − 1，相隔兩條邊）。

| | 驗證（LC 98） | 祖先最大差（LC 1026） |
|---|---|---|
| 往下帶的 | 合法區間 | 路徑上的最小值與最大值 |
| 節點落在區間外時 | 回傳 false | 擴張區間 |
| 子節點結果用什麼合併 | `and` | `max` |

<!-- 6a07b8f28645 -->
## 高度往上傳範例

<!-- 52bac94ad11f -->
### Maximum Depth of Binary Tree — LC 104

> **關鍵** — 一棵樹的深度是兩棵子樹中較深者再加一；這就是模板回傳的高度，而且 root 自己的回傳值就是答案，不需要全域變數。

**歸約到**：[binary_tree § Template 5](./binary_tree.md#template-5-height-up-answer-in-a-global--lc-543) — 只用回傳值，不用全域變數。

<!--CODE-->

**陷阱**
- 數邊而不是數節點：LC 104 數的是**節點**，所以 `maxDepth(None) = 0`，單一節點是 1。`None` 回傳 `-1`（數邊的慣例）會讓 `[3,9,20,null,null,15,7]` 得到 2。
- 10⁴ 個節點的鏈，遞迴就有 10⁴ 層；Python 需要 `sys.setrecursionlimit`，或改用逐層計數的層序 BFS。

<!-- 4bca76729182 -->
### Balanced Binary Tree — LC 110

> **關鍵** — 高度和判定結果在**同一次**後序走訪裡一起回傳：`-1` 代表「下面某處已經不平衡」，每個祖先直接往上傳，所以每個節點只走一次。

**歸約到**：[binary_tree § Template 5](./binary_tree.md#template-5-height-up-answer-in-a-global--lc-543) — 高度同時兼任失敗訊號，而不是拿去更新全域變數。

<!--CODE-->

**第二種解法 — 由上而下，以及為什麼比較貴。** 多數人第一次寫的版本是先在 root 比較兩邊高度，再遞迴進兩個子節點，所以 `height()` 會對每個祖先把整棵子樹重走一次。它是對的；放在這裡只是為了面試官接下來會問的複雜度問題。

<!--CODE-->

用「遞迴每一層的工作量 × 層數」來算。每種形狀各自給出一個每層成本和一個層數，絕不能兩種形狀各取一個：

| | 平衡的樹 | 歪斜的樹（一條鏈） |
|---|---|---|
| 層數 | log n | n |
| 第 `d` 層的工作量 | 2^d 次呼叫 × 子樹大小 n/2^d ≈ n | 1 次呼叫 × 子樹大小 n − d |
| 總和 | n · log n = **O(n log n)** — T(n) = 2T(n/2) + O(n) | n + (n−1) + … + 1 = **O(n²)** — T(n) = T(n−1) + O(n) |

換成以節點來數：`height()` 會因為每個祖先各走訪一個節點一次，所以總和就是所有深度的和，即 n ×（平均深度）。只有平衡的形狀能把那個深度壓在 log n。

**陷阱**
- O(n² log n) 是把歪斜情況的級數（本身已經是所有層的總和）乘上平衡情況的高度得到的。它把層數算了兩次。
- 在鏈上，這段由上而下的程式其實在 root 就結束了（高度 n−1 和 0 差超過 1），所以 O(n²) 是這種遞迴模式的成本，由沒有提早結束的程式全額支付，例如在每個節點都呼叫 `height` 的天真直徑寫法。
- 由下而上版本只在兩次呼叫之後才檢查一次 `-1`：仍是 O(n)，但會白白走完一個已經失敗節點的右子樹。在遞迴右邊之前先測 `left == -1`。

<!-- 0ad7eeccc532 -->
## 樹形 DP 範例

<!-- 8fd866820fdc -->
### Binary Tree Cameras — LC 968

> **關鍵** — 每次呼叫回傳一個**狀態** — 未覆蓋、有攝影機、已覆蓋 — 由父節點做決定。空子節點算已覆蓋，所以葉子會回報「未覆蓋」，逼父節點裝攝影機，一台可以蓋三層而不是一層。未覆蓋的節點自己什麼都不放：交給父節點決定。

**歸約到**：[binary_tree § Template 9](./binary_tree.md#template-9-tree-dp--return-multiple-states--lc-337-) — 三個狀態取代 (take, skip)，每個節點用貪心規則取代取 max。

<!--CODE-->

<!--CODE-->

**試跑** — `root = [1,2,3,4,5,null,null,6,7]`

<!--CODE-->

**陷阱**
- 先測「有子節點裝了攝影機」再測「有子節點未覆蓋」：上例的節點 2 會回傳 COVERED，讓 5 附近完全沒有攝影機。
- 忘了 root：`[0]` 是一個沒有父節點、未覆蓋的葉子，答案是 1，不是 0。
- 空節點回傳 UNCOVERED 會讓每個葉子都裝攝影機；空節點必須是「已覆蓋但沒有攝影機」的狀態。

<!-- 4f01c6aa2aad -->
## 結構修改範例

<!-- 74a1a5bcbd63 -->
### Invert Binary Tree — LC 226

> **關鍵** — 翻轉一棵樹就是在每個節點交換兩個子節點**指標**；遞迴回傳翻好的子樹、父節點再接回去，所以兩邊的結果都要在任一指標被覆寫之前算好。

**歸約到**：[binary_tree § Template 10](./binary_tree.md#template-10-post-order-structural-modification--lc-1110-) — 回傳重建好的子樹再重新指派；沒有刪除任何東西。

<!--CODE-->

<!--CODE-->

**陷阱**
- `root.left = invertTree(root.right); root.right = invertTree(root.left);` — 第二行讀到的是第一行剛覆寫的左指標，所以 `[2,1,3]` 會變成 `[2,3,3]` 這種亂掉的形狀。Python 的 `root.left, root.right = f(root.right), f(root.left)` 之所以安全，只是因為右邊會先整個算完。

<!-- a7db4dca2490 -->
### Flatten Binary Tree to Linked List — LC 114

> **關鍵** — 每次呼叫把自己的子樹攤平，並回傳子樹的**尾巴**（前序中的最後一個節點）；父節點就能用 O(1) 接成 `node → 左鏈 → 右鏈`：把原本的右子樹掛在左鏈的尾巴上，不必一路走下去找。

**歸約到**：[binary_tree § Template 10](./binary_tree.md#template-10-post-order-structural-modification--lc-1110-) — 後序重新接線，回傳的是尾巴而不是新的 root。

<!--CODE-->

**試跑** — `root = [1,2,5,3,4,null,6]`

<!--CODE-->

**陷阱**
- 在 `left_tail.right = node.right` **之前**寫 `node.right = node.left`，原本的右子樹就丟了：先讀出來，或先暫存。
- 找尾巴的順序是右 → 左 → 自己，和走訪順序相反：有右半段時，鏈就結束在右半段。有右子節點但沒有左子節點的節點，仍要回傳 `right_tail`。
- 不回傳尾巴、改在每個節點用 `while cur.right` 走下去找，是 O(n·h)，在歪斜的樹上是 O(n²)。

**不適用時** — 追問要求 O(1) 額外空間：那是 [tree.md § Template 1](./tree.md#template-1-morris-in-order-traversal--lc-94) 的 Morris 重接，不用遞迴，把每棵左子樹最右邊的節點接到右子節點。

<!-- d64aa7d1db0b -->
## 兩棵樹同步走範例

<!-- efd4e7ea5b19 -->
### Symmetric Tree — LC 101

> **關鍵** — 一棵樹對稱，就是它的左右子樹互為**鏡像**，也就是把 LC 100 用在兩棵子樹上、配對交叉：外側配外側（`a.left`、`b.right`），內側配內側（`a.right`、`b.left`）。

**歸約到**：[binary_tree § Template 12](./binary_tree.md#template-12-two-trees-in-lockstep--lc-100) — 一棵樹和自己比較，子節點交叉配對。它只在一個地方解，遞迴與迭代兩種寫法、試跑和直接配對的 bug 都在：[dfs.md § LC 101](./dfs.md#symmetric-tree--lc-101)。

**陷阱**
- 逐層比較數值、檢查每層是不是回文，會漏掉形狀：`[1,2,2,null,3,null,3]` 去掉 null 後每層數值都是回文，但它不對稱。
- `a.left` 配 `b.left` 檢查的是相等，不是對稱。

<!-- bc18ce0b527a -->
### Merge Two Binary Trees — LC 617

> **關鍵** — 兩棵樹都有節點的地方，合併後的節點是兩者之和；只有一棵有的地方，整棵子樹原封不動拿來用，遞迴就停在那裡，不必複製。

**歸約到**：[binary_tree § Template 12](./binary_tree.md#template-12-two-trees-in-lockstep--lc-100) — 同步走訪回傳新節點，而不是布林值。

<!--CODE-->

**陷阱**
- 遞迴進單獨的那棵子樹去複製它，成本是 O(n1 + n2)，而不是只算重疊部分；原樣回傳才是重點。
- 把和寫進 `root1` 再回傳（很多解法這樣做）也會被接受，但它改動了輸入；要說清楚你用的是哪一種。

<!-- ee50ead6d105 -->
### Flip Equivalent Binary Trees — LC 951

> **關鍵** — 每個節點的子節點都可能被交換過，所以兩種配對都接受：同步檢查 `left-left / right-right`，**或** `left-right / right-left`。

**歸約到**：[binary_tree § Template 12](./binary_tree.md#template-12-two-trees-in-lockstep--lc-100) — 每個節點試兩種配對，用 `or` 接起來。

<!--CODE-->

**陷阱**
- O(n) 的界依賴數值唯一：有重複值時，兩種配對都可能遞迴很深，最差情況比線性還差。
- 先把兩棵樹正規化（每個節點的子節點依值排序）在這題也行，但只因為值是唯一的；兩路檢查才是通用寫法。

<!-- 56180fb6b9bc -->
### Reverse Odd Levels of Binary Tree — LC 2415

> **關鍵** — 在**完美**二元樹裡，一層中第 `i` 個節點和它鏡像位置的節點，可以用同步走兩半的方式找到：外側配外側、內側配內側，和 LC 101 的配對完全一樣；反轉一層就是交換每一對的值。

**歸約到**：[binary_tree § Template 12](./binary_tree.md#template-12-two-trees-in-lockstep--lc-100) — LC 101 的鏡像配對，在奇數層交換值而不是比較值。

<!--CODE-->

**陷阱**
- 交換節點而不是交換值，就得在兩邊重接父節點；這題只需要交換 `val`。
- `a.left` 配 `b.left` 會把位置 `i` 對到 `i + half`，不是它的鏡像：一層是 `1 … 8` 時會變成 `5 6 7 8 1 2 3 4`，而不是 `8 7 6 5 4 3 2 1`。

**不適用時** — 樹不是完美二元樹，或你偏好直觀的做法：用層序 BFS 收集每個奇數層再反轉它的值（[binary_tree § Template 2](./binary_tree.md#template-2-iterative-traversal--stack-in-order-and-queue-level-order--lc-102)），空間 O(w)。
