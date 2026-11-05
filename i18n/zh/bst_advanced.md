<!-- 932ca7a29452 -->
# BST — 進階模式與深入探討

> **範圍** — 第一輪可以先跳過的 BST 內容：O(h) 空間的延遲式迭代器、用中序下降點找出被破壞的 BST、順序統計（rank）查詢，以及有序陣列以外的各種建構變形 —— 每一項都附上什麼時候用 `bst.md` 那張較簡單的卡就夠了。
> **另見**：[bst.md](./bst.md) — 這裡每一節延伸的標準模板；[bst_examples.md](./bst_examples.md) — BST 實戰題；[binary_tree.md](./binary_tree.md#template-10-post-order-structural-modification--lc-1110-) — 刪掉多個節點、回傳森林（LC 1110）；[segment_tree.md](./segment_tree.md) — 在陣列（而非樹）上做區間查詢；[binary_indexed_tree.md](./binary_indexed_tree.md) — 不用節點樹的前綴和 rank。

<!-- e8a84c963cdb -->
## LeetCode 題目清單

- [Binary Search Tree](https://leetcode.com/problem-list/binary-search-tree/)
- [Binary Tree](https://leetcode.com/problem-list/binary-tree/)
- [Design](https://leetcode.com/problem-list/design/)

<!-- edbb45d7a9f0 -->
## 總覽

五種技巧，每一種都是 [bst.md](./bst.md) 的某個標準模板再加一樣東西：一個讓中序走訪可以暫停的顯式堆疊、
一個找出逆序的 `prev` 指標、一個回答 rank 的子樹大小欄位、一組從前序重建的區間上下界，以及一個嘗試
每一種根的迴圈。它們在面試裡多半以 **follow-up** 的形式出現（「如果資料一直在變呢？」、「能不能 O(1)
空間？」、「如果輸入是鏈結串列呢？」），很少是第一題。

<!-- 8bf8f1e23397 -->
### 先備知識
- [bst § Template 5](./bst.md#template-5-in-order-walk-is-sorted--lc-230)：迭代版中序走訪。
  下面的模板 1 和 2 是讓它暫停、以及盯著它看。
- [bst § Template 2](./bst.md#template-2-insert-at-the-null-leaf--lc-701)：靠回傳子節點來插入。
  模板 3 在上面多加一個計數器。
- [bst § Template 4](./bst.md#template-4-validate-with-bounds--lc-98)：往下帶的 `(low, high)`
  區間。模板 4 用它來建樹。
- [bst § Template 6](./bst.md#template-6-sorted-array-to-balanced-bst--lc-108)：有序區間的中間當根。
  LC 109 和 LC 1382 歸約到它；模板 5 則改成每一個值都當一次根。

<!-- 52388bd75abd -->
## 辨識

| 題目裡的訊號 | 限制條件提示 | 該用 | 不適用時 |
|---|---|---|---|
| 「iterator」、「`next()` / `hasNext()` 只用 O(h) 記憶體」 | 呼叫 ≤ 10⁵ 次，平均 O(1) | [模板 1](#template-1-lazy-in-order-iterator--lc-173-) 延遲式中序 | 只需要走一遍 → [bst § Template 5](./bst.md#template-5-in-order-walk-is-sorted--lc-230) |
| 「有兩個節點被誤換了，把樹復原」 | follow-up：O(1) 空間 | [模板 2](#template-2-in-order-drop-detection--lc-99-) 中序下降點偵測 | 只問「合不合法？」→ [bst § Template 4](./bst.md#template-4-validate-with-bounds--lc-98) |
| 「串流中的第 k 大」、「x 的 rank」、「頻繁插入下的第 k 小」 | ≤ 10⁴ 次 add，每次 O(log n) | [模板 3](#template-3-size-augmented-order-statistic-bst--lc-703-) 附帶子樹大小的 BST | k 固定 → 大小為 k 的 min-heap 更短（[heap.md](./heap.md)） |
| 「用前序建出 BST」 | n ≤ 100，兩種都行 | [模板 4](#template-4-preorder-build-with-bounds--lc-1008) 在前序串流上用上下界 | 輸入是有序的 → [bst § Template 6](./bst.md#template-6-sorted-array-to-balanced-bst--lc-108) |
| 「所有結構相異的 BST」、「有幾棵相異 BST」 | 列出 n ≤ 8，計數 n ≤ 19 | [模板 5](#template-5-every-value-as-the-root--lc-95) 每個值都當一次根 | 只要一棵平衡樹 → [bst § Template 6](./bst.md#template-6-sorted-array-to-balanced-bst--lc-108) |
| 「刪掉這些節點、回傳森林」 | | 不是 BST 技巧 → [binary_tree § Template 10](./binary_tree.md#template-10-post-order-structural-modification--lc-1110-) | |

<!-- 0819fa94bc7f -->
## 延遲式中序走訪

<!-- b00e63059eca -->
### 模板 1：延遲式中序迭代器 — LC 173 ⭐⭐⭐⭐

**辨識** —— 「實作一個 BST 的 iterator」、「`next()` 回傳下一個最小值」、
「`next()` 與 `hasNext()` 平均 O(1) 時間、O(h) 記憶體」、「合併兩棵 BST」（LC 1305）。

**關鍵** —— 堆疊裡放的恰好是「值還沒回傳、但左子樹已經處理完」的節點：也就是目前位置底下的左側脊線。
pop 出來的就是中序的下一個值；再把那個節點右子節點的左側脊線推進去，不變式就恢復了。

<!--CODE-->

<!--CODE-->

**複雜度**
- **時間：每次 `next()` 攤銷 O(1)**：每個節點被 `_push_left` 推入一次、被 `next` 彈出一次，
  所以 n 次呼叫總共 O(n)，雖然單次呼叫可能推入 h 個節點。
- **空間 O(h)**：堆疊裡永遠不超過一條從根到葉的脊線。

**試跑** — `[7,3,15,null,null,9,20]`

<!--CODE-->

**陷阱**
- 在建構子裡把整棵樹攤平也會過測資，但那是 O(n) 記憶體，正是 follow-up 不允許的。
- 推入 `node.right` 本身而不是它的左側脊線，試跑裡會先回傳 15 再回傳 9。

**不適用時** —— 一次就要完整的排序結果，或只要第 k 個值：
[bst § Template 5](./bst.md#template-5-in-order-walk-is-sorted--lc-230) 的一般中序走訪更短。

**練習** —— LC 173 · LC 230 *（pop k 次就停）* · LC 1305 *（兩個迭代器，合併）* · LC 653
*（一個正向、一個反向的迭代器在中間相遇）*

<!-- 1bac6d0aeb35 -->
### All Elements in Two Binary Search Trees — LC 1305

> **關鍵** — 兩個延遲式迭代器就是兩條有序串流，所以答案就是 merge sort 的合併步驟：每次都從下一個值
> 比較小的那條串流取。額外記憶體是 O(h1 + h2)，而不是兩個攤平串列的 O(m + n)。

**歸約到**：[模板 1](#template-1-lazy-in-order-iterator--lc-173-) —— 兩個並排。

<!--CODE-->

**陷阱**
- 沒先確認兩個堆疊都非空就比較 `stack1[-1].val <= stack2[-1].val`：總有一棵樹先用完，
  `[1,8]` 和 `[8,1]` 跑到一半就會丟出 `IndexError`。

<!-- 0bcb90b0102a -->
## 修復壞掉的 BST

<!-- b208917342c6 -->
### 模板 2：中序下降點偵測 — LC 99 ⭐⭐⭐⭐⭐

**辨識** —— 「BST 中恰好有兩個節點被誤換了；在不改變結構的前提下復原這棵樹」、
「follow-up：O(1) 空間」。同一套走訪也能回答「任兩節點的最小差」（LC 530）和「眾數」（LC 501）。

**關鍵** —— 合法 BST 的中序序列是嚴格遞增的，所以一次交換會以**下降點**的形式出現：
`prev.val > node.val`。相鄰的兩個值被交換會產生一個下降點；相隔較遠則產生兩個。
`first` 是第一個下降點的 `prev`，之後不再改變；`second` 在每個下降點都設成 `node`，所以最後停在
最後一個下降點。這一條規則同時涵蓋兩種情況。

<!--CODE-->

<!--CODE-->

第二種解法，保留它是因為它是 **O(1) 空間**：同樣的下降點偵測，改用 Morris 走訪，
把每個節點的中序前驅串回它自己，而不用堆疊。

<!--CODE-->

**複雜度**
- **時間 O(n)**：中序走訪每個節點一次。Morris 每條邊最多走三次，仍是 O(n)。
- **空間 O(h)**：`inorder` 的遞迴堆疊。Morris 只保留 `cur`、`pred` 和三個標記，O(1)；
  串線用的是本來就存在的 null 指標。

**試跑** — 壞掉的樹的中序是 `[1, 6, 3, 4, 5, 2, 7]`（2 和 6 被交換）

<!--CODE-->

**陷阱**
- 每個下降點都設 `first = prev`：試跑最後會變成 5，把 5 和 2 交換後樹還是壞的。
- 在第一個下降點就停：`[1, 6, 3, 4, 5, 2, 7]` 會把 6 和 3 交換。
- 用 `TreeNode(-2**31)` 當 `prev` 的哨兵：LC 99 允許真實節點有這個值。從 `None` 開始，比較前先擋掉。
- 交換兩個節點參考而不是它們的 `.val`，樹完全沒變。

**不適用時** —— 只問這棵樹合不合法：把上下界往下帶，
[bst § Template 4](./bst.md#template-4-validate-with-bounds--lc-98)。

**練習** —— LC 99 · LC 530 *（相鄰值的最小差）* · LC 501 *（相等相鄰值的連續長度）* ·
LC 99 · LC 426 *（把 `prev` 和 `node` 串成串列）*

<!-- 1ad29982e6a0 -->
## 順序統計

<!-- 1c13fb30b00e -->
### 模板 3：附帶子樹大小的順序統計 BST — LC 703 ⭐⭐⭐⭐

**辨識** —— 「串流中的第 k 大元素」、「每次 `add` 後回傳第 k 大」、
「BST 經常被修改時的第 k 小」（LC 230 的 follow-up）、「有幾個值小於 x」——
任何你會用 `TreeMap` 或 `SortedList` 加上 rank 來回答的查詢。

**關鍵** —— 每個節點存 `count`，也就是它子樹的大小，插入時沿路每個節點都加一。這樣 `node.right`
的 `count` 就告訴你有幾個值比 `node.val` 大，所以 rank 查詢像搜尋一樣只走一條路徑：k 落在右邊就往右，
輪到 `node` 就回傳它，否則往左，並把跳過的數量從 k 扣掉。

<!--CODE-->

<!--CODE-->

**複雜度**
- **時間：每次 `add` O(h)**：`_insert` 和 rank 迴圈各走一條從根到葉的路徑。平衡時 O(log n)，
  串流依序到達時 O(n)。
- **空間 O(n)**：每個加入過的值一個節點。

**試跑** — `k = 3`、`nums = [4,5,8,2]`，接著 `add(3)`

<!--CODE-->

**陷阱**
- 只在新葉子上加 `count`：所有祖先的大小都會過時，LC 703 範例的第二次 `add` 回傳錯誤的 rank。
- 歪斜的最差情況是每次 O(n)；要說出來，並點名平衡樹（`TreeMap`、紅黑樹）是正式環境的解法。

**不適用時** —— k 固定、而且永遠只問第 k 大：大小為 k 的 min-heap 每次 add 只要 O(log k)，
五行寫完（[heap.md](./heap.md)）。先給那個，再把這個當成「如果 k 會變，或你還需要 rank(x)」的答案。

**練習** —— LC 703 · LC 230 *（follow-up）* · LC 315 *（計算右側較小的元素數）* · LC 295
*（中位數：k = n / 2 的查詢）*

<!-- a2b2da322d3f -->
## 建構變形

<!-- ca048b82a5fb -->
### 模板 4：用上下界從前序建樹 — LC 1008

**辨識** —— 「用前序走訪建出 BST」、「反序列化一棵以前序寫出的 BST」（LC 449）、
「驗證這個序列是不是某棵 BST 的前序」（LC 255）。

**關鍵** —— 前序會先列出根，再列出它子樹裡的所有東西，所以把它當成只有一個游標的串流來讀。
每次呼叫擁有一個開區間 `(low, high)`；下一個值落在區間內，它才是這棵子樹的根，否則它屬於某個祖先，
這次呼叫就回傳 `None`、不消耗它。每個值只被消耗一次。

<!--CODE-->

<!--CODE-->

**複雜度**
- **時間 O(n)**：每個值恰好被一次 `build` 呼叫消耗；其餘呼叫都在上下界檢查處以 O(1) 返回，
  而每個節點最多只有兩次這種呼叫。
- **空間 O(h)**：遞迴堆疊。

**試跑** — `[8,5,1,7,10,12]`

<!--CODE-->

**陷阱**
- 用 [bst § Template 2](./bst.md#template-2-insert-at-the-null-leaf--lc-701) 一個一個插入也行，
  但遇到 `[1,2,3,…]` 這種有序的前序就是 O(n²)。
- 在檢查上下界之前就推進游標，會吃掉一個屬於祖先的值。

**不適用時** —— 輸入是有序的（中序）：哪個值當根都行，取中間就得到平衡樹，
[bst § Template 6](./bst.md#template-6-sorted-array-to-balanced-bst--lc-108)。

**練習** —— LC 1008 · LC 449 *（用這個當解碼器做序列化）* · LC 255 *（只驗證：同樣的上下界，
不建樹）*

<!-- 60d11346e522 -->
### Convert Sorted List to Binary Search Tree — LC 109

> **關鍵** — 中間仍然是根，但串列沒有索引。快慢指標在 O(長度) 內找到 `[left, right)` 的中間，
> 所以整個建構是 O(n log n)。

**歸約到**：[bst § Template 6](./bst.md#template-6-sorted-array-to-balanced-bst--lc-108) ——
只是中間是用走的找到，而不是用索引。

<!--CODE-->

第二種解法，保留它是因為它是 **O(n)**：依中序的順序建樹。先數節點數，再對索引區間遞迴；
左子樹先建，所以串列的開頭永遠是下一個要放的值。

<!--CODE-->

**陷阱**
- 先把串列複製成陣列也是 O(n) 時間，可以接受；它花 O(n) 記憶體，這通常就是 follow-up 要問的。

<!-- 5f7e073e70c2 -->
### Balance a Binary Search Tree — LC 1382 ⭐⭐⭐⭐

> **關鍵** — 不要旋轉。任何 BST 都能用中序走訪攤平成有序值，而任何有序區間都能用「中間當根」
> 重建成平衡 BST。LC 1382 就是 LC 94 接上 LC 108。

**歸約到**：[bst § Template 5](./bst.md#template-5-in-order-walk-is-sorted--lc-230)，接著
[bst § Template 6](./bst.md#template-6-sorted-array-to-balanced-bst--lc-108)。

<!--CODE-->

**陷阱**
- 前序或後序都不是有序的，所以重建出來的樹不是 BST。
- LC 1382 允許 10⁴ 個節點排成一條歪斜鏈：遞迴的中序走訪會超過 Python 預設的 1000 層遞迴上限。
  改用迭代走訪（[bst § Template 5](./bst.md#template-5-in-order-walk-is-sorted--lc-230)）。
- 重用舊節點而不新配置也可以，前提是 `build` 對每個節點的**兩個**子節點都賦值；否則舊樹的過時指標會留下來。
- 原地、O(1) 空間的做法是 Day–Stout–Warren 演算法（先旋轉成一條藤，再壓縮）。面試很少見，說出名字就夠了。

<!-- c44a2a0cb9c7 -->
### 模板 5：每個值都當一次根 — LC 95

**辨識** —— 「生成所有存放 `1..n` 的結構相異 BST」、「有幾棵結構相異的 BST」（LC 96）、
「所有可能的滿二元樹」（LC 894）。列出時 n ≤ 8：答案本身就是指數級。

**關鍵** —— 選 `i` 當根，就強制 `lo..i-1` 進左子樹、`i+1..hi` 進右子樹，所以以 `i` 為根的樹就是
每棵左子樹配上每棵右子樹。空區間必須回傳 `[None]`（一棵空樹），不是 `[]`，否則配對迴圈什麼都產生不出來。

<!--CODE-->

<!--CODE-->

**複雜度**
- **時間 O(n · Catalan(n))**：會產出 Catalan(n) 棵樹（Catalan(8) = 1430），每棵要建 O(n) 個節點。
  不同結果之間會共用子樹，這在 LC 95 是允許的。
- **空間 O(n · Catalan(n))**：由輸出主導。

**陷阱**
- 空區間回傳 `[]`：每個葉子的配對迴圈都跑零次，`n = 1` 一棵樹都回傳不了。
- `n = 0` 必須回傳 `[]`，不是 `[None]`。

**不適用時** —— 只要一棵平衡樹：取中間，
[bst § Template 6](./bst.md#template-6-sorted-array-to-balanced-bst--lc-108)。只要數量：
見下面的 LC 96；把樹建出來毫無意義。

**練習** —— LC 95 · LC 96 · LC 894 *（滿二元樹：只有奇數大小）* · LC 241 *（同樣的切開再配對，
用在算式上）*

<!-- 6dba5dbee2fe -->
### Unique Binary Search Trees — LC 96

> **關鍵** — n 個值的數量只跟 n 有關：根 `i` 讓左邊剩 `i - 1` 個值、右邊剩 `n - i` 個，
> 所以 `dp[n] = Σ dp[i-1] · dp[n-i]`。這就是卡塔蘭數。

**歸約到**：[模板 5](#template-5-every-value-as-the-root--lc-95) —— 只數不建。

<!--CODE-->

**陷阱**
- 把 `dp[left] + dp[right]` 相加而不是相乘：每種左邊形狀都要配每種右邊形狀。`n = 3` 必須得到 5。
- `dp[0] = 0` 會讓所有有一側為空的乘積都變成零。

<!-- 95d7ce6aa3fa -->
## 帶著上下界去量測

<!-- fc6070adf92a -->
### Maximum Difference Between Node and Ancestor — LC 1026

> **關鍵** — 把路徑上的最小值與最大值往下帶，路徑結束時讀出 `high − low`；用不到 BST 的順序，
> 也用不到這份表單的任何技巧。

**歸約到**：[bst § Template 4](./bst.md#template-4-validate-with-bounds--lc-98) —— 同樣往下帶兩個值，只是拿來擴張而不是檢查。它的完整解、陷阱
和與 LC 98 的對照，在 [tree_examples.md § LC 1026](./tree_examples.md#maximum-difference-between-node-and-ancestor--lc-1026)。

<!-- 138524dc966c -->
## 不適用時

| 技巧 | 遇到這種情況，改用較簡單的卡… |
|---|---|
| [模板 1](#template-1-lazy-in-order-iterator--lc-173-) 延遲式迭代器 | 從頭到尾只走一次：[bst § Template 5](./bst.md#template-5-in-order-walk-is-sorted--lc-230) 的中序迴圈就是同一個堆疊，不必包成類別 |
| [模板 2](#template-2-in-order-drop-detection--lc-99-) 下降點偵測 | 只問「合不合法？」：[bst § Template 4](./bst.md#template-4-validate-with-bounds--lc-98)。只有被要求 O(1) 空間時才值得寫 Morris |
| [模板 3](#template-3-size-augmented-order-statistic-bst--lc-703-) 附帶子樹大小的 BST | k 固定：大小為 k 的 heap。樹是靜態的：中序走到第 k 個節點，[bst § Template 5](./bst.md#template-5-in-order-walk-is-sorted--lc-230) |
| [模板 4](#template-4-preorder-build-with-bounds--lc-1008) 前序加上下界 | 輸入是有序的，或任何合法 BST 都接受：中間當根，[bst § Template 6](./bst.md#template-6-sorted-array-to-balanced-bst--lc-108) |
| [模板 5](#template-5-every-value-as-the-root--lc-95) 每個值都當根 | 只要一棵樹，或只要數量：中間當根，或 LC 96 的 DP |

<!-- eab513cf72cf -->
## 總結

| 如果 follow-up 是… | 該用 | 成本 |
|---|---|---|
| 「不要一次把整個走訪結果展開」（LC 173） | [模板 1](#template-1-lazy-in-order-iterator--lc-173-) —— 只推入左側脊線 | O(h) 空間，每次 `next()` 攤銷 O(1) |
| 「有兩個節點被換了，修好它」（LC 99） | [模板 2](#template-2-in-order-drop-detection--lc-99-) —— 中序 + `prev`，`first` 只寫一次 | O(n) / O(h)，或用 Morris 做到 O(1) |
| 「如果我們一直插入呢？」（LC 230 → 703） | [模板 3](#template-3-size-augmented-order-statistic-bst--lc-703-) —— 每個節點存子樹大小 | 每次插入與每次 rank 查詢都是 O(h) |
| 「輸入是前序傾印」（LC 1008、449） | [模板 4](#template-4-preorder-build-with-bounds--lc-1008) —— 在串流上用上下界 | O(n) / O(h) |
| 「輸入是鏈結串列」（LC 109） | [LC 109](#convert-sorted-list-to-binary-search-tree--lc-109) —— 快慢指標找中間，或模擬中序 | O(n log n)，或 O(n) |
| 「把這棵 BST 重新平衡」（LC 1382） | [LC 1382](#balance-a-binary-search-tree--lc-1382-) —— 先中序，再中間當根 | O(n) / O(n) |
| 「列出／數出所有 BST」（LC 95、96） | [模板 5](#template-5-every-value-as-the-root--lc-95) —— 每個值都當一次根 | Catalan(n) 棵樹；計數 O(n²) |

**底下唯一的那個想法**：BST 的中序走訪就是一條有序序列，而這裡的每個技巧，不是改變一次握住這條序列的多少、
就是盯著它找逆序，要不然就是在節點上多加一個欄位，讓一次下降就能回答關於這條序列的問題。
