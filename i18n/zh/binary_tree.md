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

<!-- 8140f9709ca1 -->
#### 該選哪一個

- **預設選 A。** 轉移邏輯只有一份，擴展到 N 元樹和圖都不用改，而且 `if not node` 這個基底情況是其他每個樹模板早就在訓練的習慣。
- **這些情況選 B**：比較需要邊的*兩端*，而你想避開一個 null 分支 — 或者你**根本不能**走進某個子節點（剪枝），因為 B 是在遞迴之前就先做決定。
- **A 洩漏的狀態比較少。** 在 B 裡，`self.max_len` 必須在節點本身更新（不是在子節點），否則根節點自己的長度永遠不會被算進去。

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

<!-- 4af049757325 -->
### 0-4) 三種形狀共通的坑 — **是重置，不是停止**

在 LC 298（以及每一道「最長的 X 連續段」樹題）裡，連續段斷掉時必須**從 1 重新開始**，絕不是終止遞迴：

<!--CODE-->

在 4000 棵隨機樹上驗證過：寫法 A、B、C 每一個案例都跟暴力解一致，包括下面這棵之字形的樹 — 路徑 `1→2→3→4` 左右交錯，仍然是合法的，因為**唯一的規則就是父 → 子**。

<!--CODE-->

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
    - https://github.com/yennanliu/CS_basics/blob/master/data_structure/python/MinHeap.py#L36-L40
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

<!-- 7c6d73f85039 -->
### 通用樹模板
<!--CODE-->

<!-- a37128053191 -->
### 模板 1：樹走訪（遞迴）
<!--CODE-->

<!-- a8bea9328fb9 -->
### 模板 2：樹走訪（迭代）
<!--CODE-->

<!-- f52e019df6a0 -->
### 模板 3：樹的建構
<!--CODE-->

<!-- 30b30c902bcb -->
### 模板 4：路徑問題
<!--CODE-->

<!-- dc41c474f9f7 -->
### 模板 5：樹的性質
<!--CODE-->

<!-- e74041ec85db -->
### 模板 6：LCA（最近共同祖先）
<!--CODE-->

<!-- 1f09a6578af2 -->
### 模板 7：在樹上做二分搜尋
<!--CODE-->

<!-- a94c4b11265d -->
### 模板 8：O(1) 空間串接同層節點（`next` 指標）⭐⭐⭐⭐⭐

> **模式**：你已經站在一層完全串好的節點上了，所以可以用 `next` 走這一層，不需要佇列 — 而在走的同時，用一個**虛擬頭節點 + 移動的尾指標**把下面那一層縫起來。
> **關鍵想法**：第 `k` 層的 `next` 鏈*就是*第 `k` 層的佇列。這樣就省掉了 O(w) 的佇列，達到**額外 O(1) 空間**。
> 用在樹**不是完美樹**（有缺子節點）的時候，這也正是天真的 `root.left.next = root.right` 招數會失敗的原因。

<!--CODE-->

<!--CODE-->

**變形**
- **LC 116（完美樹）** — 轉折：每個節點不是 0 個就是 2 個子節點，所以虛擬頭／尾的那套記帳可以塌縮成 `cur.left.next = cur.right; cur.right.next = cur.next.left`。上面模板 8 的程式碼原封不動也能解 116 — 背 117，116 免費附送。

---

<!-- cb8000677092 -->
### 模板 9：樹 DP — 由下而上回傳多個狀態 ⭐⭐⭐⭐⭐

> **模式**：模板 5 每棵子樹回傳*一個*數字。當父節點的選擇取決於子節點**選了什麼做法**時，改成回傳一個**狀態組**。
> **遞迴式**（LC 337）：`take(n) = n.val + skip(L) + skip(R)`，`skip(n) = max(take(L), skip(L)) + max(take(R), skip(R))`。
> **辨認關鍵字**：「不能選相鄰的兩個節點」、「覆蓋每一個節點」、「每個節點有 k 種模式」— 任何把父子決策綁在一起的限制。

<!--CODE-->

<!--CODE-->

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

<!-- 1241abe1d818 -->
#### 實作變形：一個節點兩條連續串 — LC 549 Binary Tree Longest Consecutive Sequence II ⭐⭐⭐⭐

> LC 298（§2-4）的續集，改了**兩件事**：連續串可以遞增**也可以**遞減，而且可以在節點處**轉彎**
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

<!-- 07215a6552fb -->
### 模板 10：後序的結構性修改（回傳新的子樹）⭐⭐⭐⭐

> **模式**：遞迴回傳一個**節點**（可能是 `null`），父節點再把它**重新指派**回去：`node.left = helper(node.left)`。就靠這一行，你可以刪掉／剪掉節點，完全不用碰父指標。
> **關鍵想法**：先修好子節點（後序），再決定當前節點的命運。父節點被刪掉的節點會變成**新的森林根**，所以要把這件事往下傳。
> **辨認關鍵字**：「刪除節點並回傳……」、「剪枝」、「移除滿足……的子樹」。

<!--CODE-->

<!--CODE-->

**變形**
- **LC 814（Binary Tree Pruning）** — 轉折：沒有森林，只有一棵樹，而且刪除的判斷取決於*已經剪過*的子節點，所以檢查必須放在兩次遞迴呼叫**之後**。

<!--CODE-->

<!--CODE-->

---

<!-- 99479a1c7d07 -->
### 模板 11：帶位置索引的 BFS（在一般樹上做堆積式索引）⭐⭐⭐⭐

> **模式**：在 BFS 佇列裡讓每個節點帶著一個**虛擬陣列索引** — `left = 2*i`、`right = 2*i + 1` — 也就是把任何二元樹都當成嵌在上面講的完全樹陣列佈局裡。
> **關鍵想法**：索引編碼的是*含空隙在內的水平位置*，這是單純數每層節點數辦不到的。某一層的寬度 = `lastIndex - firstIndex + 1`。
> **容易踩到的坑**：索引每層翻倍，在一棵 3000 層深的歪斜樹上會**溢位** — 每一輪都減掉該層第一個索引來正規化。

<!--CODE-->

<!--CODE-->

**變形**
- **LC 958（Check Completeness of a Binary Tree）** — 轉折：同樣是「空隙很重要」的想法，但更簡單的做法是把 `null` 子節點也推進佇列，然後主張**一旦 pop 出一個 `null`，後面就不能再出現非 null**。

<!--CODE-->

<!--CODE-->

---

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
| Find Largest Value in Each Row | 515 | Medium | `res.append(max(level))` |
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

<!-- f5d4753f0da6 -->
#### **模式 5：LCA 與距離題目**
| 題目 | LC # | 難度 | 關鍵技巧 | 模板 |
|---------|------|------------|---------------|----------|
| Lowest Common Ancestor | 236 | Medium | DFS | 模板 6 |
| LCA of BST | 235 | Easy | BST 性質 | 模板 6 |
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

<!-- c16e609153d1 -->
#### Hard 題（進階）
- LC 124: Binary Tree Maximum Path Sum - 全域最佳化
- LC 297: Serialize and Deserialize - 字串轉樹
- LC 834: Sum of Distances in Tree - 換根技巧
- LC 968: Binary Tree Cameras - 樹上的貪婪

<!-- 5fa13fe640ce -->
## 2) LC 範例

<!-- 221eb3801cc4 -->
### 2-1) Construct Binary Tree from Preorder and Inorder Traversal — LC 105
<!--CODE-->

<!-- 160cfc612d34 -->
### 2-2) Construct Binary Tree from Inorder and Postorder Traversal — LC 106
<!--CODE-->

<!-- ac879be55b70 -->
### 2-3) Binary Tree Paths — LC 257
<!--CODE-->

<!-- 4d2fc00c3c28 -->
### 2-4) Binary Tree Longest Consecutive Sequence — LC 298

> 回看 vs 前看 vs 由下而上的比較見 **§0-2 / §0-3** — LC 298 是少見的三種形狀都能解的題目。
> 續集 **LC 549**（路徑可以轉彎，也可以遞減）見 **模板 9 — LC 549**，在那裡純量 `cur_len` 必須變成一組 `(inc, dec)`。

<!--CODE-->

<!-- 8b0b8723a877 -->
### 2-5) Binary Search Tree Iterator — LC 173
<!--CODE-->

<!-- 8c59cd2add5f -->
### 2-6) Count Complete Tree Nodes（在樹上二分搜尋）— LC 222
<!--CODE-->

<!-- 45235d06cb2a -->
## 模式選擇策略

<!--CODE-->

<!-- 58f6a24e82c8 -->
### 決策框架
1. **辨認模式**：找關鍵字（走訪、路徑、建構、性質、祖先）
2. **選模板**：把題目需求對上模板的能力
3. **調整解法**：依特定限制改寫模板
4. **最佳化**：考慮迭代 vs 遞迴、空間 vs 時間的取捨

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

<!-- 006692657189 -->
### 模板速查
| 模板 | 最適合 | 什麼時候別用 | 關鍵程式碼樣態 |
|----------|----------|------------|------------------|
| 通用 | 一般遞迴 | 需要迭代版時 | `if not root: return` |
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

<!-- stale: 03979f7da904 -->
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
| Path Sum III | 437 | Medium | 前綴和 | 模板 4 |

<!-- stale: a839fb67f861 -->
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
- LC 437: Path Sum III - 樹上的前綴和
- LC 513: Find Bottom Left Tree Value - 層序的變形
- LC 536: Construct from String - 解析成樹
- LC 549: Binary Tree Longest Consecutive II - 可轉彎的路徑，(inc, dec) 狀態
- LC 654: Maximum Binary Tree - 單調堆疊
- LC 863: All Nodes Distance K - 轉成圖
