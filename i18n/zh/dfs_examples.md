<!-- e02afd9caae0 -->
# DFS — 實戰題解

> **範圍** — [dfs.md](./dfs.md) 的題庫：它的模板能解的 DFS 題目，每題一張題目卡，歸在它所歸約到的模板底下；題目若在別份文件裡完整解過，這裡只留一列指標。
> **另見** — *母表*：[dfs.md](./dfs.md) — 十個核心模板和模式選擇流程圖，*技巧*本身在那裡講；[dfs_advanced.md](./dfs_advanced.md) — 冷門／困難的 DFS 模板與範例。
> *相鄰文件*：[bst.md](./bst.md)、[tree_codec.md](./tree_codec.md) 和 [tree_lca_distance.md](./tree_lca_distance.md) 擁有這裡指過去的樹題；[bfs_examples.md](./bfs_examples.md) — 廣度優先的題庫；[union_find.md](./union_find.md) — 連通性題目的另一種引擎。

<!-- bae58b0ae7cf -->
## LeetCode 題目清單

- [Depth-First Search](https://leetcode.com/problem-list/depth-first-search/)
- [Graph Theory](https://leetcode.com/problem-list/graph/)
- [Binary Tree](https://leetcode.com/problem-list/binary-tree/)

<!-- 44b1cf08f25c -->
## 總覽

這裡收的是那些不是模板錨定題的 DFS 題目，依它們所歸約到的 [dfs.md](./dfs.md) 模板分組，順序和那份文件一致。
[dfs.md](./dfs.md) 放模板；這份放題目。每張卡片只講這題在模板之外多加的那一步。在別份文件裡完整解過的題目，這裡只留一列指標，所以每題都只有一個家。
在這裡沒有自己題目的模板（模板 9、模板 10）就沒有對應的章節。

<!-- b0de021b1d51 -->
## 索引

| LC | 題目 | 歸約到 | 難度 |
|---|---|---|---|
| 655 | [Print Binary Tree](#print-binary-tree--lc-655) | [模板 1：樹的走訪](./dfs.md#template-1-tree-traversal--lc-94-) | Medium |
| 695 | [Max Area of Island](#max-area-of-island--lc-695) | [模板 2：圖／網格 DFS](./dfs.md#template-2-graph--grid-dfs-flood-fill--lc-200-) | Medium |
| 529 | [Minesweeper](#minesweeper--lc-529) | [模板 2：圖／網格 DFS](./dfs.md#template-2-graph--grid-dfs-flood-fill--lc-200-) | Medium |
| 737 | [Sentence Similarity II](#sentence-similarity-ii--lc-737) | [模板 2：圖／網格 DFS](./dfs.md#template-2-graph--grid-dfs-flood-fill--lc-200-) | Medium |
| 990 | [Satisfiability of Equality Equations](#satisfiability-of-equality-equations--lc-990) | [模板 2：圖／網格 DFS](./dfs.md#template-2-graph--grid-dfs-flood-fill--lc-200-) | Medium |
| 472 | [Concatenated Words](#concatenated-words--lc-472) | [模板 4：回溯](./dfs.md#template-4-backtracking--lc-46) | Hard |
| 663 | [Equal Tree Partition](#equal-tree-partition--lc-663) | [模板 6：由下而上的 DFS](./dfs.md#template-6-bottom-up-post-order-dfs--lc-543-) | Medium |
| 1339 | [Maximum Product of Splitted Binary Tree](#maximum-product-of-splitted-binary-tree--lc-1339) | [模板 6：由下而上的 DFS](./dfs.md#template-6-bottom-up-post-order-dfs--lc-543-) | Medium |
| 3319 | [K-th Largest Perfect Subtree Size in Binary Tree](#k-th-largest-perfect-subtree-size-in-binary-tree--lc-3319) | [模板 6：由下而上的 DFS](./dfs.md#template-6-bottom-up-post-order-dfs--lc-543-) | Medium |
| 417 | [Pacific Atlantic Water Flow](#pacific-atlantic-water-flow--lc-417) | [模板 7：兩趟 DFS（邊界）](./dfs.md#template-7-2-pass-dfs-boundary-elimination--lc-1254) | Medium |

<!-- 38fdce00d090 -->
## 樹的走訪範例

<!-- aa0d50684e91 -->
### Print Binary Tree — LC 655

> **關鍵** —— 網格的大小只取決於樹高，所以先算樹高，DFS 就只需要把 `(row, col)` 往下傳。每往下一層，到子節點的水平位移減半：
> `2^(height - row - 1)`，子樹因此不會重疊。

**歸約到**：[dfs § 模板 1](./dfs.md#template-1-tree-traversal--lc-94-) — 前序走訪，往下傳的是節點的位置，不往上回傳任何東西。

<!--CODE-->

**陷阱**
- 單一節點的高度必須是 0（null 回傳 `-1`）。null 回傳 `0` 的話網格會多一列：
  `[1]` 會得到兩列而不是一列。

**在別處解過**

| LC | 題目 | 家 |
|---|---|---|
| 94 | Binary Tree Inorder Traversal | [dfs § 模板 1](./dfs.md#template-1-tree-traversal--lc-94-)（錨定題） |
| 100 | Same Tree | [binary_tree § Template 12](./binary_tree.md#template-12-two-trees-in-lockstep--lc-100)（錨定題） |
| 98 | Validate Binary Search Tree | [bst.md](./bst.md) — 用上下界驗證 |
| 700 | Search in a Binary Search Tree | [bst.md](./bst.md) |
| 297 | Serialize and Deserialize Binary Tree | [tree_codec.md](./tree_codec.md) |
| 449 | Serialize and Deserialize BST | [bst_examples.md](./bst_examples.md) |
| 662 | Maximum Width of Binary Tree | [bfs_examples.md](./bfs_examples.md) — 每層用堆積索引 |

<!-- 717998bcec40 -->
## 圖／網格 DFS 範例

<!-- 0c43e00ab136 -->
### Max Area of Island — LC 695

> **關鍵** —— LC 200 裡數島嶼的那個 flood fill 也能*量*島嶼的大小：讓淹沒函式回傳 `1 + 四個鄰居的大小`，
> 啟動迴圈保留最大值，而不是計數。

**歸約到**：[dfs § 模板 2](./dfs.md#template-2-graph--grid-dfs-flood-fill--lc-200-) — 同一個 flood fill，只是回傳大小而不是什麼都不回傳。

<!--CODE-->

<!--CODE-->

**陷阱**
- 遞迴*之前*就要標記格子。四個呼叫之後才標記的話，兩個鄰居會互相計數，
  遇到 2×2 的陸地區塊遞迴永遠不會結束。

<!-- 02a2ba08c978 -->
### Minesweeper — LC 529

> **關鍵** —— flood fill 遇到數字就停。翻開的格子若旁邊有地雷，就變成地雷數、不再擴散；
> 只有旁邊**沒有**地雷的格子會變成 `'B'`，並遞迴到全部八個鄰居。只遞迴進 `'E'` 格子，這本身就是 visited 檢查。

**歸約到**：[dfs § 模板 2](./dfs.md#template-2-graph--grid-dfs-flood-fill--lc-200-) — 八個方向，由格子依它的鄰居決定 flood 要不要繼續。

<!--CODE-->

**陷阱**
- 守衛條件是 `board[r][c] != "E"`，不是「是不是地雷」。`'B'` 和數字格都已經翻開了；
  少了這個守衛，兩個空白格會互相遞迴到天荒地老。

<!-- 908cd3e910b3 -->
### Sentence Similarity II — LC 737

> **關鍵** —— 相似具有**遞移性**，所以兩個字相似，正好等於它們在「以相似字對為邊」的圖裡屬於同一個連通分量。
> 每一組對齊的字對就是一次可達性查詢。（LC 734 沒有遞移性，查一下集合就好。）

**歸約到**：[dfs § 模板 2](./dfs.md#template-2-graph--grid-dfs-flood-fill--lc-200-) — 在一張明確的字詞圖上，每個查詢跑一次。

<!--CODE-->

**陷阱**
- 一個字就算沒出現在任何字對裡，也跟自己相似：先檢查 `w1 != w2` 再搜尋。
- 查詢很多時，先一次把連通分量標好（像下面的 LC 990），或改用 union-find；
  每次查詢都跑一次 DFS 會重複走同一段路。

<!-- 192d7a85b3d2 -->
### Satisfiability of Equality Equations — LC 990

> **關鍵** —— `==` 是等價關係：`==` 的邊把變數分成幾個連通分量，除非某個 `!=` 連到**同一個**分量裡的兩個變數，
> 否則方程式都能滿足。所以先一次把每個分量標好，之後每個 `!=` 都是 O(1) 檢查。所有 `==` 都要在任何 `!=` 之前讀完。

**歸約到**：[dfs § 模板 2](./dfs.md#template-2-graph--grid-dfs-flood-fill--lc-200-) — 寫入分量編號、而不是把格子淹掉的 flood fill。

<!--CODE-->

**陷阱**
- `a!=a` 本身就無法滿足，而且 `a` 可能沒出現在任何 `==` 裡。先測 `a == b`。
- 單向的圖會讓遞移性檢查出錯：從 `a==b, b==c` 來看，從 `c` 出發的搜尋找不到任何邊。
  兩個方向都要加。
- 併查集是另一個常見答案，同樣 O(n)：先 union 每個 `==`，再拒絕兩個字母同根的 `!=`。這張卡是它的家，`union_find_examples.md` 指向這裡。

**在別處解過**

| LC | 題目 | 家 |
|---|---|---|
| 200 | Number of Islands | [dfs § 模板 2](./dfs.md#template-2-graph--grid-dfs-flood-fill--lc-200-)（錨定題） |
| 133 | Clone Graph | [graph_examples.md](./graph_examples.md) |

<!-- b0f4165cdd5b -->
## 路徑搜尋範例

這組題目裡每一題根到葉的路徑題，都在別份文件裡解過。

| LC | 題目 | 家 |
|---|---|---|
| 112 | Path Sum | [dfs § 模板 3](./dfs.md#template-3-path-finding--lc-112-)（錨定題） |
| 113 | Path Sum II | [dfs § 模板 3](./dfs.md#template-3-path-finding--lc-112-) — 收集每一條路徑 |
| 129 | Sum Root to Leaf Numbers | [tree_lca_distance.md](./tree_lca_distance.md) — 根到葉的路徑 |
| 257 | Binary Tree Paths | [tree_lca_distance.md](./tree_lca_distance.md) — 根到葉的路徑 |
| 1448 | Count Good Nodes in Binary Tree | [tree_examples.md](./tree_examples.md) |

<!-- 28b0964eb405 -->
## 回溯範例

<!-- 63879dbe19fb -->
### Concatenated Words — LC 472

> **關鍵** —— 一個字能拆成**兩個以上**的字典字才算數。試每一個字典前綴、對剩下的部分遞迴；
> 把「這個後綴能不能拆」記憶化，指數級的搜尋就變成 word-break DP，因為同一個後綴會被很多種切法走到。

**歸約到**：[dfs § 模板 4](./dfs.md#template-4-backtracking--lc-46) — 選一個前綴、對剩下的遞迴；再加上記憶化，因為選擇會重疊。

<!--CODE-->

**陷阱**
- 一個字也在它自己的字典裡。要求第一段是*真*前綴（`i < len(word)`），才能擋住每個字都跟自己配對。
- 沒有記憶化的話，`"aaaa…ab"` 配上 `["a", "aa", "aaa"]` 會走遍每一種組合：指數級。

<!-- cb6b665eafc3 -->
## 樹的修改範例

這組題目裡每一題樹的修改題，都在別份文件裡解過。

| LC | 題目 | 家 |
|---|---|---|
| 450 | Delete Node in a BST | [dfs § 模板 5](./dfs.md#template-5-tree-modification--lc-450)（錨定題） |
| 701 | Insert into a Binary Search Tree | [bst.md](./bst.md) |
| 669 | Trim a Binary Search Tree | [bst.md](./bst.md) |
| 538 | Convert BST to Greater Tree | [bst_examples.md](./bst_examples.md) — 反向中序 |
| 776 | Split BST | [bst_examples.md](./bst_examples.md) |
| 623 | Add One Row to Tree | [bfs_examples.md](./bfs_examples.md) — 逐層 BFS，旁邊附 DFS 倒數版本 |

<!-- 7d3ea63534d7 -->
## 由下而上（後序）DFS 範例

<!-- 2108cec76304 -->
### Equal Tree Partition — LC 663

> **關鍵** —— 拿掉一條邊，會留下它底下的子樹和其餘部分。一趟後序走訪收集每個子樹和；
> 只要有某個**不是整棵樹**的子樹和等於 `total / 2`，這棵樹就能平分。

**歸約到**：[dfs § 模板 6](./dfs.md#template-6-bottom-up-post-order-dfs--lc-543-) — 每個節點把子樹和往上回傳，同時記下來。

<!--CODE-->

**陷阱**
- 搜尋前先拿掉根自己的和。`[0, -1, 1]` 的總和是 0，整棵樹的和也是 0，
  但沒有任何一條邊拿掉後能得到兩半各為 0。
- 總和是奇數就不可能平分；在 `//` 之前先檢查。

<!-- 4777205180e1 -->
### Maximum Product of Splitted Binary Tree — LC 1339

> **關鍵** —— 跟 LC 663 同一趟子樹和：在和為 `s` 的子樹上方切開，得到
> `s * (total - s)`。一趟拿到每個 `s`，再取最好的乘積。

**歸約到**：[dfs § 模板 6](./dfs.md#template-6-bottom-up-post-order-dfs--lc-543-) — 子樹和往上回傳，再拿來跟總和算分。

<!--CODE-->

**陷阱**
- 取模只做一次，而且是對最大值做。拿已經對 10⁹+7 取模的乘積互相比較，
  會挑到錯的切法。

<!-- 33e66d058a6f -->
### K-th Largest Perfect Subtree Size in Binary Tree — LC 3319

> **關鍵** —— 子樹是完美的，正好等於兩個子節點都完美**而且**高度相同。往上回傳 `(height, is_perfect)`；
> 高度為 `h` 的完美子樹有 `2^h − 1` 個節點，所以大小不必另外計數。

**歸約到**：[dfs § 模板 6](./dfs.md#template-6-bottom-up-post-order-dfs--lc-543-) — 節點依子節點回傳的那一對值做決定。

<!--CODE-->

**陷阱**
- 「兩個子節點都完美」還不夠：左子是一片葉子、右子是高度 2 的完美子樹的節點，
  並不完美。高度必須相同。

**在別處解過**

| LC | 題目 | 家 |
|---|---|---|
| 543 | Diameter of Binary Tree | [dfs § 模板 6](./dfs.md#template-6-bottom-up-post-order-dfs--lc-543-)（錨定題） |
| 236 | Lowest Common Ancestor of a Binary Tree | [tree_lca_distance.md](./tree_lca_distance.md) |
| 508 | Most Frequent Subtree Sum | [hashing.md](./hashing.md) — 用雜湊表計數子樹和 |

<!-- 2abb00b53a10 -->
## 兩趟 DFS（邊界消去）範例

<!-- 405d50fa9f0f -->
### Pacific Atlantic Water Flow — LC 417

> **關鍵** —— 對每個格子問「這裡的水流得到海洋嗎？」是 O((mn)²)。
> 反過來做：從每個海洋的邊界出發往**上坡**爬（`next >= current`）。一次 flood 走得到的格子，
> 水都能流進那個海洋；答案就是兩次 flood 都走得到的格子。

**歸約到**：[dfs § 模板 7](./dfs.md#template-7-2-pass-dfs-boundary-elimination--lc-1254) — flood 從邊界出發，只是跑兩次（每個海洋一次），結果取交集而不是相減。

<!--CODE-->

**陷阱**
- 比較方向是反的：flood 走向**較高或等高**的鄰居，因為水會從那裡流下來到目前的格子。
  用 `<` 找到的是水流*往*哪裡去。
- 已到達的集合同時也是 visited 集合；少了它，兩個等高的格子會互相遞迴到天荒地老。

**在別處解過**

| LC | 題目 | 家 |
|---|---|---|
| 1254 | Number of Closed Islands | [dfs § 模板 7](./dfs.md#template-7-2-pass-dfs-boundary-elimination--lc-1254)（錨定題） |

<!-- 8a76bfa9ac98 -->
## 路徑簽名範例

| LC | 題目 | 家 |
|---|---|---|
| 694 | Number of Distinct Islands | [dfs § 模板 8](./dfs.md#template-8-path-signature-shape-encoding--lc-694)（錨定題） |
| 652 | Find Duplicate Subtrees | [tree_codec.md](./tree_codec.md) — 後序子樹簽名 + 雜湊表計數 |
