<!-- a7ebc0ddcb9e -->
# DFS — 實戰題解

> **範圍** — [dfs.md](./dfs.md) 的題解檔案庫：核心模板涵蓋的每道 DFS 題目各一份標準解法，外加整個 DFS 題庫依模式與難度的索引。
> **另見** — *母表*：[dfs.md](./dfs.md) — 十個核心模板和模式選擇流程圖，*技巧*本身在那裡講；[dfs_advanced.md](./dfs_advanced.md) — 冷門／困難的 DFS 模板與範例。
> *鄰近的表*：[tree.md](./tree.md)、[tree2.md](./tree2.md) 和 [bst.md](./bst.md) 才是這裡重複出現的多數樹題的正主；[bfs.md](./bfs.md) — 同樣幾道網格題的廣度優先解法；[backtrack.md](./backtrack.md)、[union_find.md](./union_find.md) — 註解裡提到的替代引擎。

<!-- bae58b0ae7cf -->
## LeetCode 題目清單

- [Depth-First Search](https://leetcode.com/problem-list/depth-first-search/)
- [Graph Theory](https://leetcode.com/problem-list/graph/)
- [Binary Tree](https://leetcode.com/problem-list/binary-tree/)

<!-- 35f0e6d2ec48 -->
## 總覽
這份檔案裝的是 DFS 解法的長尾。它本身不教任何東西 —— 每一條都是 [dfs.md](./dfs.md) 某個模板的實例，*為什麼*在那邊。用它來對照解法、比較同一段遞迴的兩種寫法，或是從最下面的[依模式分類的題目](#problems-by-pattern)索引挑下一題。

<!-- edc928ea33ae -->
### 關鍵性質
- **每題每種語言一份標準解法。** 只有在上方的註解說明「第二份跟第一份到底教了什麼不一樣的東西」時，才會出現第二段程式碼。
- **複雜度**：依模板而定 —— 見
  [dfs.md 的模板比較表](./dfs.md#template-comparison-table)。
- **模板已經解掉的題目**（LC 200 flood fill、LC 694 簽章、LC 1254 兩趟掃描、
  LC 1219 回溯、LC 399 比值查詢）這裡**不會**重複 —— 它們直接寫在
  [dfs.md](./dfs.md) 裡。

<!-- 598a9bfd2a3c -->
## LC 範例

<!-- 6a3471619229 -->
### 0) 基本操作

幾段小而完整的遞迴，值得練到能默寫。

<!-- 59caf555b9ba -->
#### 0-1) DFS 走訪寫法（先動作，再靠比較往下遞迴）
<!--CODE-->

<!-- 308aefbb935a -->
#### 0-2) 把二元樹每個 node.value 都加 1？
<!--CODE-->

<!-- e299f4b8af24 -->
#### 0-3) 檢查兩棵二元樹是否相同
<!--CODE-->

<!-- 729b09f6e7d0 -->
#### 0-4) 檢查某個值是否存在於 BST
<!--CODE-->

<!-- 374fc0ada8e3 -->
#### 0-5) 取得子樹的總和

<!--CODE-->

<!-- 5b2ce69be046 -->
#### 0-6) 取得樹中每個節點的 `累加總和`
<!--CODE-->

<!-- 9e7c35305a19 -->
#### 0-7) 把 BST 轉成 Greater Tree
<!--CODE-->

<!-- 07090472130c -->
#### 0-8) Serialize and Deserialize Binary Tree

> Python 版本：見下方 [2-20) LC 297](#2-20-serialize-and-deserialize-binary-tree--lc-297)。

<!--CODE-->

<!-- bbc68c153398 -->
#### 0-10) 找節點之間的最長距離
<!--CODE-->

<!-- c234af1485fe -->
#### 0-11) 把節點的值和路徑比較
<!--CODE-->

<!-- e15934d4613b -->
#### 0-12) 用 `visited` 集合做網格 DFS
<!--CODE-->

<!-- cc580af5505c -->
#### 0-13) 閉包：在巢狀的 `dfs` 裡讀取外層作用域的變數
<!--CODE-->

<!-- 8ab9ec835b25 -->
#### 深入探討 —— 子樹簽章 + 雜湊表，這不是路徑題 ⭐⭐⭐⭐

> 「我覺得這是一題樹的*路徑*問題？」—— **不是。**路徑題（LC 112 / 113 / 257）
> 追蹤的是一條 *root → leaf* 的節點線。LC 652 問的則是兩棵**完整子樹**
> 結構上是否一模一樣。訣竅是給每棵子樹一個**標準簽章**，
> 再用雜湊表數每個簽章出現幾次。它屬於
> **[dfs.md 模板 8 —— 路徑簽章／形狀編碼](./dfs.md#template-8-path-signature-shape-encoding--lc-694)**
> —— 也就是「相異島嶼」在樹上的對應版本。

**1) 核心想法**

- **後序序列化**：一棵子樹可以被 `val + signature(left) + signature(right)` 完整描述。
  子節點必須*先*編碼，父節點才編碼 → **後序 DFS**（由下而上）。
- **雜湊表計數**：相同的子樹會產生相同的簽章字串。
  每個簽章各記一個計數器；第一次數到 **2** 的時候，那棵子樹就是重複的。
- **加進 `root`，而且只加一次**：在簽章**第二次**出現時才收集節點
  （在遞增*前*用 `if count == 1`，或在遞增*後*用 `if count == 2`），這樣每種重複子樹
  都只回報一次 —— 就算它出現 3 次以上也一樣。

**2) 模式／辨識**

| 訊號 | 它在告訴你什麼 |
|--------|-------------------|
| 「重複／完全相同的**子樹**」、「結構和值都一樣」 | 序列化 + 雜湊表 |
| 要比較的是*整棵子樹*，不是單一條 root→leaf 線 | 這**不是**路徑題 |
| 答案是由子節點往上組出來的 | **後序** DFS |
| 需要分隔符（`,`）+ null 標記（`#`） | 避免簽章有歧義 |

<!--CODE-->

**3) 相似 LC**

| LC | 題目 | 關聯 |
|----|---------|----------|
| 652 | Find Duplicate Subtrees | 本題 —— 子樹簽章 + 計數 |
| 694 | Number of Distinct Islands | 網格版本 —— 把形狀編碼，用 `set` 去重 |
| 449 | Serialize / Deserialize BST | 同樣的序列化想法，編碼→解碼 |
| 297 | Serialize / Deserialize Binary Tree | 標準的（前序／後序 + `#`）編碼 |
| 572 | Subtree of Another Tree | 比對單一子樹（也可以用簽章比較） |
| 508 | Most Frequent Subtree Sum | 由下而上的子樹彙總 + 雜湊表計數 |
| 1948 | Delete Duplicate Folders in System | 652 的一般化 —— 把子樹序列化、標出重複的 |

<!-- 47ba4d9695f8 -->
#### 四方向鄰居呼叫的兩種寫法
- 圖的走訪（DFS）：往 4 個方向走（上、下、左、右）
<!--CODE-->

<!-- 15ed63d943a3 -->
### 2-17) Sum Root to Leaf Numbers — LC 129

**模式：**
每一條 root 到 leaf 的路徑，都代表一個由上往下把數字接起來的數（例如 `1 -> 2 -> 3` = `123`）。要看出這是一題**路徑編碼 DFS**：不要像 LC 113 那樣把路徑收進 list／字串、到葉子才合併（用 `sum`／`+`），而是把一個**累積值**沿著遞迴往下帶，每個節點用 O(1) 更新它 —— 到葉子時不需要任何後處理。

**核心想法：**
把一個數字 `d` 接到 `curr` 後面，就只是 `curr * 10 + d`（跟從數字字串組出整數是同一個想法）。把這個累加器當成函式參數傳下去，每個遞迴呼叫自然就有自己的作用域 —— 不需要明確回溯（`path.pop()`），因為每個 stack frame 都持有自己那份 `curr`（傳值），而不是共用一個可變的 list：

<!--CODE-->

到葉子時（`not root.left and not root.right`），`curr` 已經是那條路徑的完整數字了 —— 直接回傳即可。再把左右子樹回傳的葉子值加總。

<!--CODE-->

**路徑 list 版本（等價，但需要明確回溯）：**
<!--CODE-->

**為什麼偏好累加器寫法：**把 `curr` 當成不可變參數傳下去（`curr * 10 + node.val`），代表每條遞迴分支都免費拿到自己獨立的一份 —— 沒有共用的可變狀態，也就不需要回溯的記帳。這和 LC 113 的 `path + [val]`（每次呼叫都新建 list，不用 pop）對上 `path.append/pop`（共用 list，需要明確復原）是同一組取捨。

**相似 LC 題目（用累加器做 root-to-leaf 路徑編碼）：**
| 題目 | 模式 |
|---------|---------|
| LC 129 - Sum Root to Leaf Numbers | `curr = curr * 10 + val` —— 十進位數字接龍 |
| LC 257 - Binary Tree Paths | 把路徑累積成用 `"->"` 串起來的字串，到葉子收集 |
| LC 112 - Path Sum | 用相減累積剩下的目標值（`sum - root.val`），而不是往上組 |
| LC 113 - Path Sum II | 和 112 一樣，但在每個合法葉子收集實際的路徑 list |
| LC 988 - Smallest String Starting From Leaf | 由下而上（葉到根）把路徑累積成字串，再比字典序 |

<!-- 03ff1a0327ba -->
#### 深入探討 —— 傳遞性的相似其實就是圖的連通性 ⭐⭐⭐⭐

> 雖然題目包著「句子／單字」的外皮，這其實是一題**圖連通性**問題，
> **不是**字串問題。每個 `similarPair` 都是一條**無向邊**；相似關係具有
> **傳遞性**（`a~b, b~c ⇒ a~c`），這正好就是「這兩個節點在不在同一個
> 連通分量裡？」。（對照 LC 734 *Sentence Similarity I* —— 沒有傳遞性，
> 所以查一個 set 就夠了，不需要圖。）

**1) 核心想法**

- 從 `similarPairs` **建一張無向圖**：`graph[a].add(b)`、`graph[b].add(a)`。
- 對每組對齊的單字 `(w1, w2)`：
  - `w1 == w2` → 依定義相似（一個字和自己相似）→ 跳過。
  - 否則從 `w1` 做 **DFS/BFS** 試著走到 `w2`；走不到就 return `False`。
- 長度不一樣 → 直接 `False`。

<!--CODE-->

**2) 模式／辨識**

| 訊號 | 它在告訴你什麼 |
|--------|-------------------|
| 關係具有**傳遞性**（`a~b, b~c ⇒ a~c`） | 連通分量問題 |
| 「X 和 Y 有沒有關聯／連通／在同一組」 | DFS / BFS / **併查集** |
| 邊以成對形式給定，而且要查很多組 (x,y) 可達性 | 優先用**併查集**（每次查詢接近 O(1)） |
| 一開始就要把起點放進 `visited` | 避免在有環的圖上無限迴圈 |

<!--CODE-->

**3) 相似 LC**

| LC | 題目 | 關聯 |
|----|---------|----------|
| 737 | Sentence Similarity II | 本題 —— 有傳遞性 → 檢查連通分量 |
| 734 | Sentence Similarity I | **沒有**傳遞性 → 查 set 就好（不用建圖） |
| 547 | Number of Provinces | 數連通分量（DFS／併查集） |
| 200 | Number of Islands | 網格上的連通分量 |
| 990 | Satisfiability of Equality Equations | `==`／`!=` 限制 → 併查集 |
| 684 | Redundant Connection | 找出造成環的那條邊（併查集） |
| 399 | Evaluate Division | 連通性 + 帶權（比值）邊 |

**4) 觀念 —— 為什麼提早 `return False` 不會毀掉整個 DFS**

> 這個模板最常見的困惑：
> ```python
> def helper(graph, node, target, visited):
>     if node == target:    return True
>     if node in visited:   return False     # <-- does this kill the whole search??
>     visited.add(node)
>     for nei in graph[node]:
>         if helper(graph, nei, target, visited):
>             return True                    # bubble success UP
>     return False                           # <-- and does this??
> ```
> **不會。**一個 `return` 只會往遞迴堆疊上跳**一層** —— 回到*呼叫它的人*，
> **不是**回到最外層的呼叫。`False` 只是結束*那一條分支*，讓父層的
> `for` 迴圈繼續走下一個鄰居。只有 `True` 會一路往上傳
> （因為每個呼叫端都寫著 `if helper(...): return True`）。

**走查** —— 圖 `A→[B,C]`、`B→[D]`、`C→[E]`；呼叫 `helper(A, target=E)`：

<!--CODE-->

<!--CODE-->

第一個 `False`（來自 `B→D` 那條分支）**沒有**中止搜尋 —— 它只結束了那條分支，
`helper(A)` 裡的迴圈接著繼續走 `C`。

**`if node in visited: return False` 也是同樣的道理** —— 在有環的圖上
（`A↔B`、`A↔C`）：`helper(A)→helper(B)→helper(A)` 撞到 `A in visited` 就回傳 `False`
*而且只回傳給 `helper(B)`*。它的意思是「別再從 A 重搜一次」，不是「放棄」。控制權
回到 `helper(A)` 的迴圈，接著正常地去探索 `C`。什麼都沒被切掉。

> **核心想法**：最下面那行 `return False` **只有在每個鄰居都試過之後**才會執行。
> 某個子節點回傳 `False` 只是讓 `for` 迴圈往前走；整個 DFS 只有在*所有*分支都走完
> 卻沒碰到目標時，才會回報 `False`。

<!-- a8a774fb9ad4 -->
### 2-27) Satisfiability of Equality Equations — LC 990

**模式 —— 連通性／矛盾檢查（等式分群）**

- **描述**：給一堆等式（`==`）和不等式（`!=`）限制，判斷它們能否同時成立。用 `==` 的邊建圖，再驗證沒有任何 `!=` 的兩端其實是連通的。
- **辨識**：「等式方程」、「變數相等／不相等」、「可滿足性」、「先按等價關係分群再找矛盾」，以及具有**傳遞性**的關係（`a==b`、`b==c` ⟹ `a==c`）
- **關鍵技巧**：**兩階段**處理 —— (1) 用所有 `==` 關係建一張**無向**圖；(2) 對每個 `!=` 關係做 DFS 檢查可達性。如果兩個「必須不同」的變數是連通的 → 矛盾 → 回傳 False。
- **例題**：LC 990（Satisfiability of Equality Equations）
- **核心演算法想法**：
  1. **建圖**：對每個 `x==y`，**兩個方向都要加** `x→y` 和 `y→x`（無向）。`==` 這個關係同時是對稱且傳遞的，所以連通分量 = 等價類。
  2. **掃矛盾**：對每個 `x!=y`，從 `x` 跑 DFS；如果走得到 `y`，代表圖逼著它們相等、但題目要求不等 → **無法滿足**。
  3. 一定要**先處理完所有 `==`**，再處理**所有 `!=`** —— 在群組還沒建完就看 `!=`，會得到錯的答案。
- **重要提醒**：
  - ⚠️ **圖一定要是雙向的。**在*單向*圖上呼叫 `dfs(a,b)` 和 `dfs(b,a)` **並不等價** —— 對於 `a==b, b==c`，單向的 `dfs(c, a)` 找不到任何出邊，就會錯誤地回傳 False。兩個方向都要存。
  - DFS 之前**不需要**先檢查 `if y in graph[x]` —— DFS 自然涵蓋了直接相鄰的情況（第一跳的遞迴就會命中 `cur == target`）。
  - `a!=a` 這種自我不等式本質上無法滿足；DFS 會因為 `cur == target` 立刻回傳 True（gemini 那個版本有明確擋掉）。
  - `visited` 集合要**每個 `!=` 查詢重設一次**，讓每次可達性檢查各自獨立探索。
- **另一種（更乾淨的）做法：併查集** —— 每個 `==` 做一次 `union(x,y)`；接著對每個 `!=`，如果 `find(x)==find(y)` 就回傳 False。時間 `O(N·α)`，通常也是面試比較想聽到的答案。見 [union_find.md](./union_find.md)。
- **DFS vs 併查集的取捨**：DFS 每次 `!=` 檢查是 `O(V+E)`（整體可能到 `O(N²)`）；併查集每次查詢接近 `O(1)` —— 但 DFS 能強化「圖連通性」的心智模型。
- **相似的經典 LC 題目**：
  - LC 990 - Satisfiability of Equality Equations（等式分群 + 找矛盾的標準題）
  - LC 547 - Number of Provinces（用 DFS／併查集數連通分量）
  - LC 200 - Number of Islands（網格上的連通分群）
  - LC 721 - Accounts Merge（依共用 email 合併 → 分量）
  - LC 684 - Redundant Connection（找出造成環的那條邊 —— 併查集）
  - LC 399 - Evaluate Division（傳遞關係的帶權版本 —— [dfs.md 模板 10](./dfs.md#template-10-weighted-graph-dfs-divisionratio-queries--lc-399)）
  - LC 785 - Is Graph Bipartite?（二著色 = 一種「必須不同組」的限制檢查）

<!--CODE-->

**併查集版本**（更乾淨，接近 `O(N·α)`）：

<!--CODE-->

**容易踩到的坑**：`==` 的圖一定要雙向。對於 `a==b, b==c`，單向圖會讓 `dfs(c, a)` 失敗（`c` 沒有出邊），錯誤地回報可滿足 —— `x→y` 和 `y→x` 都要存。

---

<!-- e5b2ce6dd2c2 -->
### 2-28) Print Binary Tree — LC 655

> **DFS + 固定大小的矩陣**。先算出樹高，據此開一個 `(height+1) × (2^(height+1)-1)` 的字串網格，把 root 放在正中間那一欄，然後用 DFS 把每個子節點放在**每層減半的水平位移** `2^(height-row-1)` 處。

**核心想法**：網格的尺寸在走訪*之前*就定好了（純粹由樹高推出來），所以 DFS 只需要 `(row, col)` —— 不用動態調整大小。每往下一層，水平展開就減半，剛好對應二元樹分岔的方式。

<!--CODE-->

**為什麼 `get_tree_height` 對 null 回傳 `-1`**：這樣單節點樹的高度就是 `0`，於是 `rows = 1`，節點正好落在唯一那一列。如果 null 回傳 `0`，所有高度都會多算一，網格就會多出一列。

**位移的直覺**：最上面那列，子節點必須跳整個寬度的四分之一；再往下一層，就是它的一半；以此類推。`2^(height-row-1)` 精準地編碼了這個等比減半，子節點才不會撞在一起，版面也才會對稱。

| 步驟 | 公式 | 為什麼 |
|------|---------|-----|
| 列數 | `height + 1` | 每層一列 |
| 欄數 | `2^(height+1) - 1` | 最底層可能的最大寬度，也讓版面對稱 |
| root 的欄 | `(cols - 1) // 2` | 最上面那列的正中央 |
| 子節點位移 | `2^(height - row - 1)` | 每層減半，子樹才不會重疊 |

---

<!-- 308eba3a0643 -->
### 2-29) Add One Row to Tree — LC 623 ⭐⭐⭐⭐

> **帶倒數深度的 DFS**。在 `depth` 這一層插入一排值為 `val` 的節點。不要去追絕對層數，
> 而是**每次遞迴呼叫都把 `d` 減 1**，等 `d == 2` 時觸發 base case —— 這時*當下*這個
> 節點就是那個要重接子指標的父節點。原本的左子樹掛到新左節點的 `.left`，
> 原本的右子樹掛到新右節點的 `.right`。

**1) 核心想法**

- **往下數，不要往上數。** BFS 需要 `cur_depth == depth - 1`；DFS 只要把 `d - 1`
  往下傳，在 `d == 2` 停下來就好，遞迴裡不用穿一個深度變數。
  `d == 2` 的意思是「我的子節點就是目標那一列」—— 也就是**我是 `depth - 1` 那個父節點**。
- **兩個 base case，順序如下**：
  - `d == 1` → 沒有父層可言；建一個**新的 root**，把整棵原樹掛在它的
    **左邊**。這只可能在*最外層*的呼叫發生（見下面的說明）。
  - `d == 2` → 重接*這個*節點的子指標：建兩個 `val` 節點，再把舊的子樹接回去。
- **覆蓋前先存起來。** `root.left = TreeNode(v)` 會毀掉原本的指標。Python 的
  tuple 賦值*在順序正確時*能安全處理這件事：
<!--CODE-->
  整個右手邊會在任何賦值發生前先算完（所以那裡的 `root.left` 還是*舊的*子節點），
  接著目標由**左到右**依序賦值：`root.left` 先變成新節點，然後
  `root.left.left`（也就是那個新節點）接到舊子樹。兩個目標調換順序就壞了。
- **接在外側**：舊左 → `new_left.left`，舊右 → `new_right.right`。接在內側會把子樹鏡射掉。
- **子節點是 `None` 沒關係** —— 位於 `depth - 1` 但沒有子節點的節點，一樣會長出兩個新
  子節點，而 `new.left = None` 正是我們要的。只有 `root` 自己需要防 null。
- **DFS 自然會剪枝**：遞迴在 `d == 2` 就停了，所以永遠不會走到插入那列的下面 ——
  它沒去走訪的節點，剛好就是它不該碰的那些。不像 BFS 版本還要 `break`／`return` 來擋。

**2) 模式**

<!--CODE-->

**變形 —— 原地修改、忽略回傳值**（也是對的，以及為什麼）：

<!--CODE-->

這之所以可行，是因為唯一會*替換*節點（而不是就地修改）的分支是 `d == 1`，而遞迴裡
`d` 永遠到不了 `1` —— 它一路 `d → d-1` 往下降，在 `2` 就停住了。所以每次遞迴呼叫都是
就地修改自己的參數，父節點的指標始終有效。
但還是建議用**重新賦值**的寫法：不管觸發哪個 base case 它都正確，而且改寫 base case
之後也還撐得住。

<!--CODE-->

**這題的 DFS vs BFS**

| | DFS（本節） | BFS（見 [bfs.md §2-17](./bfs.md)） |
|---|---|---|
| 深度追蹤 | 隱含 —— 倒數 `d - 1`，在 `d == 2` 停 | 明確的 `cur_depth`，在 `depth - 1` 停 |
| 空間 | `O(h)` 遞迴堆疊 | `O(W)` 佇列（最大層寬） |
| 停止方式 | 自動（遞迴自己結束） | 需要明確的 `break`／`return` |
| 程式長度 | 最短 | 比較囉唆，但沒有爆堆疊的風險 |
| 風險 | ⚠️ 限制裡 `depth` 可到 `10^4` → 極度傾斜的樹可能超過 Python 預設的遞迴上限（1000） | 沒有 |

> 因為限制允許樹深達 `10^4`，DFS 版本在退化成鏈結串列形狀的樹上可能需要
> `sys.setrecursionlimit(...)`；BFS 版本沒有這個限制。DFS 是面試時比較漂亮的答案，
> BFS 則是在最大輸入下比較安全的那個。

**容易踩到的坑**

| 坑 | 為什麼會壞 |
|---|---|
| 遞迴停在 `d == 1` | 太深了 —— 要重接的指標在父節點身上，而 `d == 1` 是*新 root* 的情況 |
| `root.left.left, root.left = root.left, TreeNode(v)` | 目標順序錯了 —— `root.left.left` 寫到了**舊**子節點上，接著整個被蓋掉 |
| `new_left.right = old_left`（接在內側） | 會把子樹鏡射掉；必須分別是 `.left` / `.right` |
| 省略 `if not root: return None` | `d == 2` 時會在 null 節點上取 `root.left` |
| 沒有寫 `root.left = self.addOneRow(...)` 重新賦值 | 只是碰巧安全（見上面的變形）；一旦某個 base case 開始回傳*新*節點就會壞 |

**3) 相似 LC**

| LC | 題目 | 關聯 |
|----|---------|----------|
| 623 | Add One Row to Tree | 本題 —— DFS 倒數到 `d == 2`，重接子指標 |
| 226 | Invert Binary Tree | 同樣有「先存再賦值」的子指標陷阱 |
| 617 | Merge Two Binary Trees | DFS 回傳（可能是新的）子樹 root —— 重新賦值的寫法 |
| 654 | Maximum Binary Tree | 在 DFS 過程中建節點並往上回傳 |
| 971 | Flip Binary Tree To Match Preorder | 在走訪途中修改左右連結 |
| 116 / 117 | Populating Next Right Pointers | 也是重接指標，但是逐層做（適合 BFS） |
| 655 | Print Binary Tree | [2-28)](#2-28-print-binary-tree--lc-655) —— DFS 把推導出來的深度／位移往下帶 |
| 111 / 104 | Min / Max Depth of Binary Tree | 這題所仰賴的數深度遞迴 |

> **模式帶走的重點**：「在深度 `d` 做 X」⇒ 用 `d - 1` 遞迴，並在 **`d == 2`** 動手，因為
> 你真正能修改的節點是目標那列的*父節點*。先把舊的子指標算完再賦值新的、接在外側，
> 並回傳子樹 root，呼叫端的連結才會正確。

---

<!-- c8b75ab397b3 -->
## 依模式分類的題目

<!-- 905f287b6297 -->
### 依模式的題目分類

`Template N` 指的是 [dfs.md → Templates & Algorithms](./dfs.md#templates--algorithms)；
`*adv* TN` 指的是 [dfs_advanced.md](./dfs_advanced.md)。

<!-- f65242877c32 -->
#### **模式 1：樹的走訪**
| 題目 | LC # | 難度 | 關鍵技巧 | 模板 |
|---------|------|------------|---------------|----------|
| Binary Tree Inorder Traversal | 94 | Easy | 堆疊／遞迴 | Template 1 |
| Binary Tree Preorder Traversal | 144 | Easy | 堆疊／遞迴 | Template 1 |
| Binary Tree Postorder Traversal | 145 | Easy | 堆疊／遞迴 | Template 1 |
| Serialize and Deserialize Binary Tree | 297 | Hard | DFS 編碼 | Template 1 |
| Serialize and Deserialize BST | 449 | Medium | BST 性質 | Template 1 |
| Binary Tree Paths | 257 | Easy | 追蹤路徑 | Template 3 |
| Same Tree | 100 | Easy | 同步 DFS | Template 1 |

<!-- e640ff24762e -->
#### **模式 2：路徑問題**
| 題目 | LC # | 難度 | 關鍵技巧 | 模板 |
|---------|------|------------|---------------|----------|
| Path Sum | 112 | Easy | DFS 走訪 | Template 3 |
| Path Sum II | 113 | Medium | 回溯 | Template 3 |
| Binary Tree Maximum Path Sum | 124 | Hard | 全域最大值 | Template 6 |
| Diameter of Binary Tree | 543 | Easy | 由下而上 | Template 6 |
| Longest Univalue Path | 687 | Medium | 由下而上 | Template 6 |
| Sum Root to Leaf Numbers | 129 | Medium | 追蹤路徑 | Template 3 |

<!-- 909ad9e4b4c6 -->
#### **模式 3：圖的走訪**
| 題目 | LC # | 難度 | 關鍵技巧 | 模板 |
|---------|------|------------|---------------|----------|
| Number of Islands | 200 | Medium | 網格 DFS | Template 2 |
| Max Area of Island | 695 | Medium | 網格 DFS | Template 2 |
| Clone Graph | 133 | Medium | HashMap | Template 2 |
| Course Schedule | 207 | Medium | 偵測環 | Template 2 |
| Course Schedule II | 210 | Medium | 拓撲排序 | Template 2 |
| Pacific Atlantic Water Flow | 417 | Medium | 多源 | Template 2 |
| Evaluate Division | 399 | Medium | 圖的走訪 | Template 2 |
| Minesweeper | 529 | Medium | 網格探索 | Template 2 |

<!-- 5a9da6773935 -->
#### **模式 4：回溯**
| 題目 | LC # | 難度 | 關鍵技巧 | 模板 |
|---------|------|------------|---------------|----------|
| Permutations | 46 | Medium | 回溯 | Template 4 |
| Subsets | 78 | Medium | 回溯 | Template 4 |
| Combination Sum | 39 | Medium | 回溯 | Template 4 |
| Letter Combinations | 17 | Medium | 回溯 | Template 4 |
| Generate Parentheses | 22 | Medium | 回溯 | Template 4 |
| Word Search | 79 | Medium | 網格回溯 | Template 4 |
| N-Queens | 51 | Hard | 回溯 | Template 4 |

<!-- 4aec1562f2a1 -->
#### **模式 5：修改樹結構**
| 題目 | LC # | 難度 | 關鍵技巧 | 模板 |
|---------|------|------------|---------------|----------|
| Delete Node in BST | 450 | Medium | BST 刪除 | Template 5 |
| Insert into BST | 701 | Medium | BST 插入 | Template 5 |
| Trim a Binary Search Tree | 669 | Medium | 條件式修剪 | Template 5 |
| Convert BST to Greater Tree | 538 | Medium | 反向中序 | Template 5 |
| Invert Binary Tree | 226 | Easy | 交換左右子樹 | Template 5 |
| Flatten Binary Tree | 114 | Medium | 原地修改 | Template 5 |

<!-- bfd3ed435fde -->
#### **模式 6：子樹與彙總**
| 題目 | LC # | 難度 | 關鍵技巧 | 模板 |
|---------|------|------------|---------------|----------|
| Most Frequent Subtree Sum | 508 | Medium | HashMap | Template 6 |
| Find Duplicate Subtrees | 652 | Medium | 序列化 | Template 6 |
| Lowest Common Ancestor | 236 | Medium | 由下而上 | Template 6 |
| Equal Tree Partition | 663 | Medium | 子樹總和 | Template 6 |
| Maximum Product of Splitted Tree | 1339 | Medium | 所有子樹總和 | Template 6 |
| Validate Binary Search Tree | 98 | Medium | 上下界 | Template 1 |
| Split BST | 776 | Medium | 遞迴切分 | Template 5 |

<!-- f51364f50368 -->
#### **模式 7：邊界消去（兩趟 DFS）**
| 題目 | LC # | 難度 | 關鍵技巧 | 模板 |
|---------|------|------------|---------------|----------|
| Number of Closed Islands | 1254 | Medium | 從邊界淹水 | Template 7 |
| Surrounded Regions | 130 | Medium | 消去邊界相連者 | Template 7 |
| Pacific Atlantic Water Flow | 417 | Medium | 兩個海洋 | Template 7 |
| Number of Enclaves | 1020 | Medium | 與邊界相連 | Template 7 |

<!-- 7d010c88ed73 -->
#### **模式 8：路徑簽章（形狀編碼）**
| 題目 | LC # | 難度 | 關鍵技巧 | 模板 |
|---------|------|------------|---------------|----------|
| Number of Distinct Islands | 694 | Medium | 方向編碼 | Template 8 |
| Number of Distinct Islands II | 711 | Hard | 處理旋轉／鏡射 | Template 8 |
| Find Duplicate Subtrees | 652 | Medium | 樹的序列化 | Template 8 |
| Most Frequent Subtree Sum | 508 | Medium | 子樹簽章 | Template 8 |

<!-- 9821d9342678 -->
#### **模式 9：帶驗證的 DFS（偵測子分量）**
| 題目 | LC # | 難度 | 關鍵技巧 | 模板 |
|---------|------|------------|---------------|----------|
| Count Sub Islands | 1905 | Medium | 布林旗標往上傳 | *adv* T1 |
| Number of Islands | 200 | Medium | 基本的分量計數 | Template 2 |
| Max Area of Island | 695 | Medium | 追蹤分量大小 | Template 2 |
| Island Perimeter | 463 | Easy | 數邊 | Template 2 |
| Making A Large Island | 827 | Hard | 合併分量 | Template 2 |

<!-- 51577e9b8f75 -->
#### **模式 10：帶方向追蹤的雙向圖**
| 題目 | LC # | 難度 | 關鍵技巧 | 模板 |
|---------|------|------------|---------------|----------|
| Reorder Routes to Make All Paths Lead to the City Zero | 1466 | Medium | 雙向圖 + 方向旗標 | *adv* T2 |
| Minimum Number of Days to Disconnect Island | 1568 | Hard | 修改圖結構（相關） | - |
| Remove Max Number of Edges to Keep Graph Fully Traversable | 1579 | Hard | 邊的定向（相關） | - |

<!-- 101580ca55d5 -->
#### **模式 11：分量配對計數（不可達的配對）**
| 題目 | LC # | 難度 | 關鍵技巧 | 模板 |
|---------|------|------------|---------------|----------|
| Count Unreachable Pairs of Nodes in an Undirected Graph | 2316 | Medium | 分量計數 + 累乘 | *adv* T3 |
| Number of Connected Components in an Undirected Graph | 323 | Medium | 基本的分量計數 | Template 2 |
| Number of Provinces | 547 | Medium | 偵測分量 | Template 2 |

<!-- 69f00fdec4e9 -->
### 依難度的完整題目清單

<!-- 7301907e354f -->
#### Easy 題（基礎）
- LC 94: Binary Tree Inorder Traversal - 基本 DFS
- LC 100: Same Tree - 平行 DFS
- LC 101: Symmetric Tree - 鏡像 DFS
- LC 104: Maximum Depth - 簡單遞迴
- LC 112: Path Sum - 追蹤路徑
- LC 144: Binary Tree Preorder Traversal - 使用堆疊
- LC 145: Binary Tree Postorder Traversal - 操作堆疊
- LC 226: Invert Binary Tree - 修改樹結構
- LC 257: Binary Tree Paths - 收集路徑
- LC 543: Diameter of Binary Tree - 全域最大值模式
- LC 572: Subtree of Another Tree - 子樹比對

<!-- 540876cea341 -->
#### Medium 題（核心）
- LC 98: Validate BST - 上下界檢查
- LC 113: Path Sum II - 回溯路徑
- LC 130: Surrounded Regions - 邊界消去
- LC 133: Clone Graph - HashMap + DFS
- LC 200: Number of Islands - 網格 DFS
- LC 207: Course Schedule - 偵測環
- LC 210: Course Schedule II - 拓撲排序
- LC 236: Lowest Common Ancestor - 由下而上的 DFS
- LC 297: Serialize/Deserialize Tree - DFS 編碼
- LC 399: Evaluate Division - 圖的 DFS
- LC 417: Pacific Atlantic Water Flow - 多源 DFS
- LC 450: Delete Node in BST - 重構樹結構
- LC 449: Serialize/Deserialize BST - BST 性質
- LC 472: Concatenated Words - Word break DFS
- LC 508: Most Frequent Subtree Sum - 彙總
- LC 529: Minesweeper - 網格探索
- LC 538: Convert BST to Greater Tree - 反向中序
- LC 652: Find Duplicate Subtrees - 序列化
- LC 663: Equal Tree Partition - 子樹總和
- LC 669: Trim BST - 條件式修改
- LC 695: Max Area of Island - 連通分量
- LC 701: Insert into BST - BST 插入
- LC 1466: Reorder Routes to Make All Paths Lead to the City Zero - 帶方向追蹤的雙向圖
- LC 1905: Count Sub Islands - 帶驗證的 DFS
- LC 2316: Count Unreachable Pairs of Nodes in an Undirected Graph - 分量配對計數
- LC 737: Sentence Similarity II - 圖的連通性
- LC 776: Split BST - 進階操作
- LC 1020: Number of Enclaves - 邊界消去
- LC 1254: Number of Closed Islands - 兩趟 DFS
- LC 1339: Maximum Product of Splitted Tree - 所有子樹總和

<!-- 31dfaa428d72 -->
#### Hard 題（進階）
- LC 124: Binary Tree Maximum Path Sum - 全域最佳化
- LC 297: Serialize and Deserialize Binary Tree - 複雜編碼
- LC 51: N-Queens - 複雜回溯
- LC 329: Longest Increasing Path in Matrix - 記憶化 DFS
- LC 3319: K-th Largest Perfect Subtree - 複雜彙總
- LC 332: Reconstruct Itinerary - 尤拉路徑（Hierholzer），見 *adv* Template 4
- LC 753: Cracking the Safe - de Bruijn 圖上的尤拉迴路，見 *adv* Template 4
- LC 1192: Critical Connections in a Network - Tarjan 橋（low-link），見 *adv* Template 5

<!-- a61f5e952fd8 -->
#### 其他高頻 DFS 題（參考）

這些是經典的 FAANG DFS 題，用的都是上面已經涵蓋的模板 —— 列出來只是求完整，沒有新技巧。

- LC 388: Longest Absolute File Path - 以深度為索引的堆疊 DFS（*adv* Template 7）
- LC 419: Battleships in a Board - 不用 flood fill 的分量計數（Template 2 變形）
- LC 211: Design Add and Search Words Data Structure - 字典樹（Trie）+ 萬用字元 DFS（*adv* Template 6）
- LC 676: Implement Magic Dictionary - 帶「錯配額度」的 Trie DFS（*adv* Template 6 變形）
- LC 1233: Remove Sub-Folders from the Filesystem - 路徑 Trie DFS + 提早剪枝（*adv* Template 7 變形）
- LC 863: All Nodes Distance K in Binary Tree - 用 DFS 補上 parent 連結，再把樹當成圖處理
- LC 337: House Robber III - 後序 DFS，每個節點回傳一組 `(rob, skip)` 狀態
- LC 947: Most Stones Removed with Same Row or Column - 在 row/column key 上的連通分量
- LC 690: Employee Importance - 在 `id -> employee` 的 map 上做 DFS，而不是鄰接串列
- LC 341: Flatten Nested List Iterator - 用明確的堆疊對巢狀結構做 DFS 展平
- LC 430: Flatten a Multilevel Doubly Linked List - 在鏈結串列上做 DFS；就地把 child 串列接進去
- LC 934: Shortest Bridge - 先用 DFS 標記一座島，再從它往外 BFS 到另一座

<!-- a9380e260a0f -->
## 重點整理與速查

| 你想找 | 去哪裡 |
|---|---|
| 這裡任一解法背後的技巧 | [dfs.md → Templates & Algorithms](./dfs.md#templates--algorithms) |
| 某題屬於哪個模板 | 上面的[依模式分類的題目](#problems-by-pattern)索引 |
| 冷門模式（尤拉路徑、Tarjan、trie DFS、`parent[]` 樹） | [dfs_advanced.md](./dfs_advanced.md) |
| 同一道網格／樹題目的 BFS 解法 | [bfs.md](./bfs.md) |
| 某道樹題以樹為主軸的完整說明 | [tree.md](./tree.md)、[tree2.md](./tree2.md)、[bst.md](./bst.md) |

**怎麼讀有多段程式碼的條目**：同一個標題下有兩段程式碼時，中間的註解會說明第二段教了什麼 —— 不同的複雜度、不同語言的慣用寫法，或是一個獨立的技巧。其他情況都只是單一份標準解法。
