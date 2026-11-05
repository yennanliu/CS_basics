<!-- ef3f7856ee7f -->
# 樹的回溯模板

> **範圍** — 樹的路徑問題背後那套「選擇 / 探索 / 復原」骨架，以及每題各自帶著的路徑狀態，外加根到葉卡片沒涵蓋的變形：通往單一節點的路徑（LC 236）、隱式的樹（LC 386、LC 388）；根到葉的卡片本身放在 tree_lca_distance.md。
> **另見**：[tree_lca_distance.md](./tree_lca_distance.md) — 根到葉的路徑卡片（LC 112、113、129、257、437）；[tree.md](./tree.md) — 一般的樹走訪；[binary_tree.md](./binary_tree.md) — DFS 狀態流動的方向；[backtrack.md](./backtrack.md) — 樹以外的回溯。

很多樹的路徑問題共用同一套 DFS 骨架：選擇（延伸路徑狀態）、檢查目標（通常在葉子）、探索兩個子節點、復原。這份表單講的是骨架，以及每題帶著什麼狀態；標準的根到葉卡片（LC 112、113、129、257、437）放在 [tree_lca_distance.md](./tree_lca_distance.md)，下面各節會連過去。

<!-- 98c3c7f8dbc2 -->
## LeetCode 題目清單

- [Tree](https://leetcode.com/problem-list/tree/)
- [Backtracking](https://leetcode.com/problem-list/backtracking/)
- [Depth-First Search](https://leetcode.com/problem-list/depth-first-search/)

<!-- 36a88681b3ac -->
## 1️⃣ 通用樹回溯模板

<!--CODE-->

處理**路徑問題**時，通常要維護：

<!--CODE-->

---

<!-- 403c96c671fa -->
## 2️⃣ 完整的通用模板

<!--CODE-->

關鍵步驟：

| 步驟    | 用途             |
| ------- | ------------------- |
| choose  | 把節點加進路徑    |
| check   | 看看是否達成目標 |
| explore | 對子節點做 DFS       |
| undo    | 還原狀態       |

---

<!-- 25294c46a6e7 -->
## 3️⃣ 套用到 LC 113（Path Sum II）

狀態 = **剩下的總和**，再加上一個進入時 append、離開時 pop 的路徑 list。完整的卡片（Python 與 Java、試跑和陷阱）在 [tree_lca_distance § Template 4](./tree_lca_distance.md#template-4-root-to-leaf-path-with-backtracking--lc-113)，那份表單負責整個根到葉路徑家族。

---

<!-- c99cfaf2c53e -->
## 4️⃣ 套用到 LC 257（Binary Tree Paths）

狀態 = **路徑字串**。字串不可變，每次呼叫各自建出自己的一份，沒有東西要復原。完整解法在 [tree_lca_distance § Template 4](./tree_lca_distance.md#template-4-root-to-leaf-path-with-backtracking--lc-113) 的 *Path Update Strategies*，旁邊就是拿來對照的可變 list 版本。

---

<!-- fa9d26535a99 -->
## 5️⃣ 套用到 LC 129（根 → 葉的數字）

狀態 = **數字**：`num * 10 + node.val`，以值傳遞，所以同樣沒有東西要復原。完整解法是 [Sum Root to Leaf Numbers — LC 129](./tree_lca_distance.md#sum-root-to-leaf-numbers--lc-129)。

---

<!-- 527d20a6faea -->
## 6️⃣ 特殊情況：LC 437（Path Sum III）

LC 437 的路徑往下走時**可以從任何地方開始**，不一定是根。從每個節點各跑一次模板行得通，但在歪斜的樹上是 O(N²)。O(N) 的版本回溯的是一張前綴和 map 而不是 list — 見下面的 §1️⃣1️⃣。

---

<!-- 73c8e2e52f6e -->
## 7️⃣ 心智模型（非常重要）

只要看到：

<!--CODE-->

馬上就該想到：

<!--CODE-->

模板：

<!--CODE-->

---

<!-- 9890b7cf05c4 -->
## 8️⃣ 面試速記密技 ⭐

把這個**五行骨架**背起來就好：

<!--CODE-->

**大多數根到葉的題目**用這個就能解掉。

---

✅ **面試小訣竅**

如果題目裡出現這些字眼：

<!--CODE-->

那幾乎**保證是 DFS＋回溯**。

---

<!-- 011092a7b264 -->
## 9️⃣ 快速決策表（該用哪個路徑模板？）

| 路徑上要做的事 | 模板 | 復原步驟 | 例題 |
|------------------|----------|-----------|---------|
| 收集所有根到葉的路徑 | push 節點／pop 節點 | `path.remove(size-1)` | LC 113、LC 257 |
| 把路徑摺疊成一個純量（和／數字） | 用傳值的方式帶狀態 | *不用復原* — 值是複製的 | LC 129 |
| 找到通往**某一個**節點的路徑 | push，**只在失敗時** pop | 子樹回傳 false 時才 pop | **LC 236** |
| 統計和為目標值的子路徑數 | 路徑上的前綴和計數器 | `count[cur] -= 1` | **LC 437** |
| 走訪**隱含**的樹（數字／文字） | 從父節點狀態重建子節點 | 除回去／彈堆疊 | **LC 386**、**LC 388** |

⭐⭐⭐⭐⭐ 這幾種之間唯一的差別，就是**「路徑狀態」是什麼**，以及**要怎麼把它復原**。

---

<!-- dd9ede452618 -->
## 🔟 模板：根→節點的路徑，只在失敗時復原（LC 236）

**核心想法**：標準模板在探索完子節點後會**無條件** pop。
但當你在找**某一個特定節點**時，改成**只有子樹失敗才** pop——
這樣搜尋成功時，`path` 裡剛好留著完整的根→目標路徑。

**模式**：分別建出 `path(root→p)` 和 `path(root→q)`，兩者**最後一個共同前綴元素就是 LCA**。

<!--CODE-->

<!--CODE-->

> **為什麼要學這個，而不是那個有名的三行後序 LCA？**
> 面試官追問時，活下來的是路徑版：*「回傳 p 和 q 之間的距離」*、
> *「印出 p 到 q 的路徑」*、*「不保證兩個節點都存在」*（看那個布林回傳值）。
> 三行版在只有 p 或 q 其中一個存在時，還是會默默回傳一個節點。

---

<!-- 49244c828335 -->
## 1️⃣1️⃣ 模板：**在路徑上**做前綴和＋復原（LC 437）

§6️⃣ 的「從每個節點做 DFS」是 O(N²)。O(N) 的做法把目前根到節點的路徑當成陣列，套用子陣列和的技巧：一張記錄**目前路徑上**前綴和的 map，回程時復原 — 在這裡 map 就是路徑狀態，就像 LC 113 的 list 一樣。完整的卡片（Python、Java、試跑，以及陷阱：`prefix[0] = 1`、復原、Java 的 `long`）在 [tree_lca_distance § Template 5](./tree_lca_distance.md#template-5-downward-path-count-with-a-prefix-sum-map--lc-437)。

---

<!-- fdf73ce12047 -->
## 1️⃣2️⃣ 模板：在**隱含**的樹上回溯（LC 386）

不是每棵樹都由 `TreeNode` 組成。`1..n` 依字典序排列，本身就是一棵 **10 元的 trie**：
`1 → 10 → 100 …`，而字典序恰好就是它的**前序走訪**。

**模式**：子節點 = `parent * 10 + d`。`child > n` 時剪枝。

<!--CODE-->

<!--CODE-->

**追問（`O(1)` 額外空間）** — 面試官最愛的變化。同樣的走法，改成**迭代**；
「往上回到父節點」變成*除以 10*：

<!--CODE-->

<!--CODE-->

---

<!-- f774ef92cb9c -->
## 1️⃣3️⃣ 模板：用深度當鍵的路徑狀態**堆疊**（LC 388）

一段有縮排的文字就是一棵序列化的樹，tab 的數量**就是**深度。
你完全不用把樹建出來——只要保留**當前根→節點的路徑長度**，
一旦跳到比較淺的深度，較深的那些項目就自動被丟掉（也就是回溯）。

**核心想法**：`pathLen[d]` = 深度 `d` 處的目錄前綴長度。
由上往下掃描時寫入 `pathLen[depth + 1]`，會**覆蓋掉**任何殘留的兄弟分支，
這正好就是經典模板裡的「pop」。

<!--CODE-->

<!--CODE-->

**容易踩到的坑**
- 完全**沒有檔案**時要回傳 `0`（`"a"` → `0`），而不是最長的目錄路徑。
- 那個 `+1` 是 `/` 分隔符；深度 `0` 的檔案剛好不會多一個分隔符。
- 用 `Deque<Integer>` 一路彈到 `size == depth`，是同一件事的顯式堆疊寫法。

---

<!-- 31ba12d9990b -->
## 1️⃣4️⃣ 變形：狀態往**下**流，結構往**上**改（LC 1110）

骨架相同，但不是往路徑上 push，而是把一個**布林值往下傳**（「我的父節點被刪了，所以我是一棵新樹的根」），並把**重接好的子樹往上回傳**（被刪的節點回傳 `None`，父節點就把那條連結拿掉）。這是改樹的結構而不是路徑問題，所以它的卡片和其他結構修改放在一起：[binary_tree § Template 10](./binary_tree.md#template-10-post-order-structural-modification--lc-1110-)。

---

<!-- b86b127e65e1 -->
## 1️⃣5️⃣ 路徑模板總表

| LC | 題目 | 路徑狀態 | 復原方式 |
|----|---------|-----------|------|
| 113 | Path Sum II | `List<Integer>` ＋剩餘的和 | `path.remove(size-1)` |
| 257 | Binary Tree Paths | 字串（傳值） | 隱含 |
| 129 | Sum Root to Leaf Numbers | `int num`（傳值） | 隱含 |
| **236** | Lowest Common Ancestor | 節點 list，失敗才 pop | 有條件的 pop |
| **437** | Path Sum III | `Map<prefixSum, count>` | `count[cur] -= 1` |
| **386** | Lexicographical Numbers | 目前的數字 | `cur /= 10` |
| **388** | Longest Absolute File Path | `pathLen[depth]` | 覆寫／彈到該深度 |
| **1110** | Delete Nodes And Return Forest | `parentWasDeleted` 旗標 | 往上回傳 `null` |
