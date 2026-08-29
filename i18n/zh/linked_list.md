<!-- dc1374c03ff7 -->
# Linked List（鏈結串列）

> **範圍** — 單向與雙向鏈結串列的指標手術：反轉、合併、重排、虛擬頭節點技巧，以及環的處理。
> **另見**：[linked_list_examples.md](./linked_list_examples.md) — 這些模板對應的完整題解；[2_pointers_linkedlist.md](./2_pointers_linkedlist.md) — 快慢指標的專門篇；[design.md](./design.md) — LRU 以及其他「串列 + 表」的設計題；[heap.md](./heap.md) — k 路串列合併；[recursion.md](./recursion.md) — 用遞迴改寫串列。

<!-- 1d43e2ef29a3 -->
## LeetCode 題目清單

- [Linked List](https://leetcode.com/problem-list/linked-list/)
- [Doubly-Linked List](https://leetcode.com/problem-list/doubly-linked-list/)

<!-- 10098144e6eb -->
## 時間複雜度

| 資料結構 | 搜尋   | 插入   | 刪除   | 最小／最大值  |
| -------------- | -------- | -------- | -------- | -------- |
| Linked List    | O(n)     | O(1)     | O(1)     | O(n)     |

> 只要你手上已經握著目標節點（例如 head，或某個你本來就持有的節點），插入／刪除就是 **O(1)**；但*先找到*那個節點是 **O(n)**。

<!-- 34bdf1760c2c -->
## 0) 概念
- [fucking algorithm : reverse part of linked list](https://github.com/labuladong/fucking-algorithm/blob/master/%E6%95%B0%E6%8D%AE%E7%BB%93%E6%9E%84%E7%B3%BB%E5%88%97/%E9%80%92%E5%BD%92%E5%8F%8D%E8%BD%AC%E9%93%BE%E8%A1%A8%E7%9A%84%E4%B8%80%E9%83%A8%E5%88%86.md)
- [fucking algorithm : reverse k set of linked list](https://github.com/labuladong/fucking-algorithm/blob/master/%E9%AB%98%E9%A2%91%E9%9D%A2%E8%AF%95%E7%B3%BB%E5%88%97/k%E4%B8%AA%E4%B8%80%E7%BB%84%E5%8F%8D%E8%BD%AC%E9%93%BE%E8%A1%A8.md)
- [fucking algorithm : check palindrome linked list](https://github.com/labuladong/fucking-algorithm/blob/master/%E9%AB%98%E9%A2%91%E9%9D%A2%E8%AF%95%E7%B3%BB%E5%88%97/%E5%88%A4%E6%96%AD%E5%9B%9E%E6%96%87%E9%93%BE%E8%A1%A8.md)


- 善用「虛擬頭節點」（pseudo head node）
    - [代碼隨想錄: LC 203 Remove Linked List Elements](https://youtu.be/Y4oQJklHxVo?t=1111)
- 要從串列刪除節點，必須站在「前一個」節點，才能刪掉下一個節點
    - 也就是說，要站在 `cur`，才能刪掉 `cur.next`
<!--CODE-->

<!--CODE-->

<!--CODE-->


<!--CODE-->

<!-- 81eb595c9395 -->
### 0-1) 型別
- 鏈結串列
- 環狀鏈結串列
- 雙向鏈結串列
- 雙向串列（Double Linked list）
    - LC 146
- 其他
    - LC 138：
<!--CODE-->
    - LC 208：
    - [trie](https://github.com/yennanliu/CS_basics/blob/master/doc/cheatsheet/trie.md)
<!--CODE-->
- 題型
    - 反轉
        - 反轉整條串列
            - LC 206
        - 反轉指定起訖點之間的串列
            - LC 92、LC 25
        - 反轉串列的一部分
        - 每 k 個一組反轉
    - 合併
        - 合併兩條串列
    - 檢查
        - 檢查串列有沒有環
        - 找出環的起點
    - 移除倒數第 N 個節點
        - Remove Nth Node From End of List — LC 19
    - 組合題
        - 上述情況的各種組合

<!-- 2dbfc4a4b745 -->
### 0-2) 模式

<!-- ca5588a21781 -->
#### **虛擬頭節點技巧**

**定義**：建一個指向真正 head 的虛擬／假頭節點，讓邊界情況與節點刪除操作變得好寫。

**什麼時候用**：
- 要刪掉串列開頭的節點
- head 節點本身可能被改動
- 想簡化邊界情況的處理
- 需要一路追蹤前一個節點的操作

**時間複雜度**：O(n) — 跟不用虛擬頭一樣
**空間複雜度**：O(1) — 只多一個節點

**模板**：
<!--CODE-->

**好處**：
- 不必為 head 節點寫特例
- 邏輯更簡單
- 減少邊界情況的 bug
- 整趟走訪都有一致的 prev 指標

---

<!-- ddcc57c7e947 -->
#### **為什麼要用虛擬節點？圖解對照（LC 19）**

> **題目**：從 `[1, 2, 3, 4, 5]` 移除**倒數第 n 個**節點。

---

<!-- 89f72b647c8b -->
##### 情況 A — 一般刪除：`n = 2`（刪掉節點 `4`）

**不用虛擬節點** — 這裡沒問題：

<!--CODE-->

**用虛擬節點** — 一樣可行，邏輯相同：

<!--CODE-->

---

<!-- dcc68c71a726 -->
##### 情況 B — 邊界情況：`n = 5`（要刪掉的是 **head** 節點 `1`）

**不用虛擬節點** — 壞掉了，得寫特例：

<!--CODE-->

**用虛擬節點** — 一視同仁，**不需要**特例：

<!--CODE-->

---

<!-- cf265cb8d8d6 -->
##### 小結：虛擬節點贏在哪

| | 不用虛擬節點 | 用虛擬節點 |
|---|---|---|
| 一般刪除 | ✓ 可行 | ✓ 可行 |
| 刪掉 head（n = len） | ❌ 得多一句 `if not fast: return head.next` | ✓ 一視同仁 |
| 程式分支 | 多一個條件判斷 | 沒有 |
| `slow` 的起點 | `head`（沒辦法站到 head 之前） | `dummy`（剛好在 head 前一步） |

**關鍵洞見**：虛擬節點給了 `slow` 一個站在 **head 前一個節點**的位置，於是它能跨過任何節點重新接線 — 包括 head 本身 — 完全不用特殊處理。

<!--CODE-->

---

<!-- dcbcc80a0b64 -->
#### **虛擬頭節點 — 其他應用**

再看兩題「虛擬節點把特例消掉」的例子。這個家族其餘的題目寫在它們該待的地方，不在這裡重貼：

| LC | 題目 | 寫在哪 |
|---|---|---|
| 19 | Remove Nth Node From End | 上面那段圖解，以及 [linked_list_examples.md](./linked_list_examples.md#9-remove-nth-node-from-end-of-list--lc-19) 的兩種 Java 寫法 |
| 21 | Merge Two Sorted Lists | [linked_list_examples.md](./linked_list_examples.md#4-merge-two-sorted-lists--lc-21) |
| 2 | Add Two Numbers | [下面的 1-1-7)](#1-1-7-add-2-linked-list--lc-2) |
| 203 | Remove Linked List Elements | 下面的 [依值移除節點的模式](#remove-elements-by-value-pattern) |

**從已排序串列移除重複 — LC 83**：虛擬節點握著最後一個*保留下來*的節點，於是一整串相同的值會自然塌縮，也永遠不用為「head 就是重複值」寫特例。
<!--CODE-->

**Partition List — LC 86**：用*兩個*虛擬節點。分別把 `< x` 這條鏈和 `>= x` 這條鏈各自接好，最後串起來 — 不做原地手術，穩定性也免費附贈。
<!--CODE-->
**虛擬頭節點的主要好處**：

| 面向 | 不用虛擬節點 | 用虛擬節點 |
|--------|---------------|------------|
| **邊界情況** | head 的處理很囉唆 | 統一處理 |
| **程式長度** | 條件判斷更多 | 更乾淨、更短 |
| **出錯機率** | 較高（邊界情況） | 較低（邏輯一致） |
| **可讀性** | 比較難跟 | 直覺得多 |

**相關題目**：
- LC 19: Remove Nth Node From End of List
- LC 21: Merge Two Sorted Lists
- LC 83: Remove Duplicates from Sorted List
- LC 86: Partition List
- LC 203: Remove Linked List Elements
- LC 328: Odd Even Linked List

---

<!-- 7855a5e04c4d -->
#### **依值移除節點的模式**

**定義**：把串列中所有等於某個值的節點都移除。做法是虛擬頭節點加上「往前看一格」的技巧 — 當前指標檢查的是 `curr.next`，而不是 `curr` 自己。

**核心概念**：
- **關鍵洞見**：找到要刪的節點時，我們**只**更新指標連接（`curr.next = curr.next.next`），`curr` 本身**不往前走**
- 這樣才能處理連續命中的節點（例如 `[6,6,6,3]`、val=6）
- 只有在 `curr.next.val != val` 時才把 `curr` 往前推

**什麼時候用**：
- 要依值移除串列中任何位置的節點
- 開頭的節點也可能需要被移除
- 要移除連續重複的值

**時間複雜度**：O(n)
**空間複雜度**：O(1)

**模板**：
<!--CODE-->

<!--CODE-->

**手動追蹤範例**（`[6,6,6,3]`、val=6）：
<!--CODE-->

**為什麼這樣就能處理連續命中**：
| 情境 | 沒有「原地不動」 | 有「原地不動」 |
|----------|------------------------|---------------------|
| `[6,6,3]` val=6 | 會漏掉第二個 6 | 全部的 6 都抓到 |
| 刪除 head | 需要特例 | 一視同仁 |

**類似的 LC 題目**：
- LC 203: Remove Linked List Elements（就是這個模式本身）
- LC 83: Remove Duplicates from Sorted List（類似，比較相鄰節點）
- LC 82: Remove Duplicates from Sorted List II（重複的全部刪掉）
- LC 237: Delete Node in a Linked List（不一樣 — 拿不到前一個節點）
- LC 1474: Delete N Nodes After M Nodes（模式的變化）
- LC 2487: Remove Nodes From Linked List（改用堆疊的變化）

---

<!-- 533a1c52863a -->
#### **雙向鏈結串列 + HashMap（LRU Cache 模式）** ⭐⭐⭐⭐⭐

**核心想法**：用 HashMap 做 O(1) 的 key 查找，配上雙向鏈結串列做 O(1) 的有序淘汰。最近用過的節點靠近**尾端**；最久沒用的靠近**開頭**。頭尾各放一個哨兵節點，所有邊界情況的指標檢查就全消失了。

**結構配置**：
<!--CODE-->

**什麼時候用**：
- 需要 O(1) 的 get + O(1) 的 put，而且要有序淘汰（LRU/MFU）
- 任何需要「照存取順序追蹤」的集合

**時間複雜度**：get 與 put 都是 O(1)  
**空間複雜度**：O(capacity)

**關鍵輔助操作**：
- `_remove(node)` — 用 O(1) 把節點從串列中摘掉
- `_insert(node)` — 用 O(1) 把節點插到 tail 前面（MRU 位置）

**模板**：
<!--CODE-->

**視覺追蹤**（capacity=2）：
<!--CODE-->

**為什麼要哨兵節點？**
- `_remove` 和 `_insert` 永遠拿得到合法的 `.prev`/`.next` 鄰居
- 不需要 `if node.prev is None` 或 `if node.next is None` 這種防護
- 刪頭、刪尾、刪中間都是同一套程式碼

**類似的 LC 題目**：
| # | 題目 | 差別在哪 |
|---|---------|----------------|
| 146 | LRU Cache | 經典模式 — 淘汰最久沒用的 |
| 460 | LFU Cache | 兩層結構：頻率表 + 每個頻率一條雙向鏈結串列 |
| 432 | All O(1) Data Structure | 由計數桶組成的雙向鏈結串列 |
| 1472 | Design Browser History | 雙向鏈結串列，訪問新頁時把前方截斷 |
| 641 | Design Circular Deque | 固定容量的雙向鏈結串列，兩端都能操作 |
| 716 | Max Stack | 堆疊 + 雙向鏈結串列 + TreeMap，做到 O(log n) 的 popMax |

---

<!-- 1960db2abf58 -->
#### **反轉 K 個節點的輔助函式模式** ⭐⭐⭐⭐⭐

**核心想法**：幾乎每一題「反轉某一*段*」（LC 92、LC 25、LC 24、LC 206）都是**同一個基本操作** — 從某個 `head` 開始反轉 `k` 個節點，然後把線接回去。把這個基本操作抽成一個可重用的輔助函式，外層解法就只要煩惱**定位那一段**和**把兩頭縫回去**。

這個輔助函式反轉 `k` 個節點，並回傳你重新接線所需的**三個把手**：

<!--CODE-->

**為什麼要回傳三個東西？** 反轉一段*中間*的節點之後，**兩個邊界**都得重新接線：

| 回傳值 | 它是什麼 | 用來接哪裡 |
|----------|-----------|-------------------|
| `prev`（`new_head`） | 反轉後這一塊的新 **head** | `prev_of_segment.next = new_head` |
| `head`（`new_tail`） | 新的 **tail**（原本的第一個節點） | `new_tail.next = next_node` |
| `curr`（`next_node`） | 這一段**之後**的第一個節點 | tail 必須指到這裡 |

**什麼時候用**：
- 反轉子區間 `[left, right]`（LC 92）→ 反轉 `right - left + 1` 個節點
- 每 k 個一組反轉（LC 25）→ 迴圈呼叫輔助函式，直到剩不到 `k` 個
- 反轉整條串列（LC 206）→ 呼叫一次，`k = length`（或 `k = ∞`）

**模板 — 把輔助函式套到 LC 92（Reverse Linked List II）**：
<!--CODE-->

**圖解**（`[1,2,3,4,5]`、`left=2`、`right=4` → 反轉 `2,3,4` 這 3 個節點）：

<!--CODE-->

**三個邊界把手的視覺化**：
<!--CODE-->

**把輔助函式重用到 LC 25（Reverse Nodes in k-Group）**：
<!--CODE-->

> **關鍵洞見**：*同一個* `reverse_helper` 就撐起了 LC 206 / 92 / 25。差別只在外圍邏輯 — **206** 呼叫一次，**92** 先定位一段再呼叫一次，**25** 用迴圈每組呼叫一次。把三把手的回傳值（`new_head, new_tail, next_node`）練熟，這三題就都塌縮成「定位 → 反轉 → 接回去」。

**類似的 LC 題目**：
| # | 題目 | 輔助函式怎麼套 |
|---|---------|------------------------|
| 206 | Reverse Linked List | 呼叫一次，`k = length` — 只有 `new_head` 有用 |
| 92  | Reverse Linked List II | 定位那一段，用 `k = right - left + 1` 呼叫一次，兩頭都接回去 |
| 25  | Reverse Nodes in k-Group | 每組呼叫一次；最後不足 `k` 的尾巴跳過 |
| 24  | Swap Nodes in Pairs | 就是每組 `k = 2` 的特例 |
| 61  | Rotate List | 操作不同，但一樣是「定位邊界 + 重新縫合」那套紀律 |

---

<!-- 12d1a2f093a9 -->
## 1) 通用形式
<!--CODE-->
<!--CODE-->

<!-- f7f9ea61f2fe -->
### 1-1) 基本操作

<!-- b4f944c718f4 -->
#### 1-1-1) 反轉鏈結串列（迭代） — LC 206
<!--CODE-->

<!--CODE-->

<!-- f7321c644ab0 -->
#### 1-1-2) 反轉鏈結串列（遞迴） — LC 206
<!--CODE-->

<!-- c5a20faca9ce -->
#### 1-1-3) 反轉 *[a,b] 區間內的節點*（迭代） — LC 92
<!--CODE-->

<!-- 02d455ec21c5 -->
#### 1-1-4) *每 k 個一組*反轉串列（迭代） — LC 25
<!--CODE-->
<!--CODE-->

<!-- 46a208c32a2c -->
#### 1-1-5) 反轉*前 N 個*節點（遞迴）
<!--CODE-->

<!-- 28c7fa85697f -->
#### 1-1-6) 反轉串列*中間的 N 個節點*（以 *start, end* 表示區間）（遞迴） — LC 92
<!--CODE-->

<!-- 2f8d3a119e84 -->
#### 1-1-7) 兩條串列相加 — LC 2
<!--CODE-->

<!--CODE-->

<!-- 7054ede58ee2 -->
#### 1-1-8) 找出串列中點 — LC 876
<!--CODE-->

<!--CODE-->

<!-- 60b02c28c4c7 -->
## 2) 模式選擇

鏈結串列題其實很少真的在考串列。它們考的是：動手術的當下，**你手上必須握著哪個把手** — 這份文件上的每個技巧，存在的理由都是確保你握著它。挑法要看答案需要什麼，不是看題目叫什麼名字。

| 如果題目要你… | 就用 | 因為 | 詳寫在 |
|---|---|---|---|
| 在**任何位置**刪除或插入，包含 head | **虛擬頭節點** | 它讓 `prev` 有個站在 head *之前*的位置，「刪掉 head」就不再是特例 | [虛擬頭節點技巧](#dummy-head-technique) |
| 移除**所有**符合某個值的節點 | **虛擬節點 + 檢查 `curr.next`** | 刪完之後你必須能*原地不動*，否則像 `[6,6,6]` 這種連續值會漏掉一個 | [依值移除節點的模式](#remove-elements-by-value-pattern) |
| 反轉**整條**串列 | **三步迴圈**：先存下 next → 翻指標 → 前進 | O(1) 空間；遞迴版答案一樣，卻要每個節點吃掉一個 stack frame | [1-1-1)](#1-1-1-reverse-linked-list-iteration--lc-206) |
| 反轉**一段** — `[left, right]`、每 `k` 個，或成對 | **反轉 k 個的輔助函式，回傳三個把手** | LC 92 / 25 / 24 的差別只在*那一段在哪*，反轉的方式完全一樣 | [反轉 K 個節點的輔助函式模式](#reverse-k-nodes-helper-pattern-) |
| 找中點、偵測環，或走到**倒數**第 n 個 | **快慢指標** | 一趟掃完、O(1) 空間，也不用先算長度 | [1-1-8)](#1-1-8-find-linked-list-middle-point--lc-876)、[2_pointers_linkedlist.md](./2_pointers_linkedlist.md) |
| **重排** — 交錯、切分、旋轉、回文判斷 | **快慢指標切一半 → 反轉後半 → 合併** | 每一題重排都是這三個基本操作依序組合；沒有一個是新東西 | [examples 2)](./linked_list_examples.md#2-reorder-list--lc-143)、[7)](./linked_list_examples.md#7-palindrome-linked-list--lc-234) |
| 合併**兩條**已排序串列 | **虛擬節點 + 一趟合併走訪**，接節點而不是複製值 | 尾端指標就是全部的訣竅：`cur.next = l1 or l2` 收尾 | [examples 4)](./linked_list_examples.md#4-merge-two-sorted-lists--lc-21) |
| 合併 **k** 條已排序串列，或把一條串列排序 | **分治法** — 兩兩合併，或用中點做合併排序 | O(n log k) / O(n log n)；用堆積則是拿 O(k) 空間換掉遞迴 | [examples 5)](./linked_list_examples.md#5-merge-k-sorted-lists--lc-23)、[14)](./linked_list_examples.md#14-sort-list-merge-sort-on-a-linked-list--lc-148-)、[heap.md](./heap.md) |
| 同時要**任意位置**讀取*和* O(1) 淘汰 | **雙向鏈結串列 + 雜湊表** | 表給你節點，雙向節點給你它的鄰居 — 少了任一個都不夠 | [雙向鏈結串列 + HashMap](#doubly-linked-list--hashmap-lru-cache-pattern-)、[design.md](./design.md) |
| 對存成串列的位數做**算術** | **在虛擬節點上跑進位迴圈**，若最高位在前就先反轉 | 進位會活得比兩個輸入都久，所以迴圈條件是 `l1 or l2 or carry` | [1-1-7)](#1-1-7-add-2-linked-list--lc-2)、[examples 13)](./linked_list_examples.md#13-plus-one-linked-list--lc-369) |
| 回答需要**隨機存取或視窗**的問題 | **先倒進陣列，再用陣列的技巧** | 前綴和與單調堆疊都需要索引，串列沒有；而且通常允許 O(n) 額外空間 | [examples 15)](./linked_list_examples.md#15-prefix-sum--hashmap-on-a-linked-list--lc-1171-)、[16)](./linked_list_examples.md#16-monotonic-stack-over-a-linked-list--lc-1019-) |

<!-- 6ef117bacd71 -->
### 四個陷阱

1. **把串列弄丟。** 還沒存下 `curr.next` 就寫 `curr.next = prev`，後面整串就沒了。先存起來 — 這就是反轉迴圈非得照那個順序寫的原因。
2. **回傳錯的 head。** 任何可能動到第一個節點的操作之後，要回傳 `dummy.next`，不是 `head`：`head` 可能已經不在串列裡了。
3. **留下一個環。** 遞迴反轉裡只寫 `head.next.next = head`，卻漏掉後面的 `head.next = null`，最後兩個節點就會互指。
4. **走過頭。** 兩步跳要寫成 `while (fast != null && fast.next != null)`。兩個條件順序寫反，在偶數長度的串列上就會對 null 解參考。

<!-- 98a511f05456 -->
## 3) 實戰題解

完整解法搬到 **[linked_list_examples.md](./linked_list_examples.md)** 了，免得上面的模板被它們埋掉。十七題，依各自演練的技巧分組：

| 分組 | 題目 |
|---|---|
| [反轉與重排](./linked_list_examples.md#reversal--reordering) | LC 92, 143, 24 |
| [合併與切分](./linked_list_examples.md#merging--splitting) | LC 21, 23, 725 |
| [快慢指標與結構](./linked_list_examples.md#fastslow-pointers--structure) | LC 234, 160, 19 |
| [複製、攤平與連通分量](./linked_list_examples.md#copying-flattening--components) | LC 138, 817, 430 |
| [算術與排序](./linked_list_examples.md#arithmetic--sorting-on-a-list) | LC 369, 148, 147 |
| [把陣列技巧借到串列上](./linked_list_examples.md#array-techniques-borrowed-onto-a-list) | LC 1171, 1019 |
