<!-- b3404098f2d5 -->
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

<!-- ebf7c8ce1dc6 -->
## 0) 概念
- [fucking algorithm : reverse part of linked list](https://github.com/labuladong/fucking-algorithm/blob/master/%E6%95%B0%E6%8D%AE%E7%BB%93%E6%9E%84%E7%B3%BB%E5%88%97/%E9%80%92%E5%BD%92%E5%8F%8D%E8%BD%AC%E9%93%BE%E8%A1%A8%E7%9A%84%E4%B8%80%E9%83%A8%E5%88%86.md)
- [fucking algorithm : reverse k set of linked list](https://github.com/labuladong/fucking-algorithm/blob/master/%E9%AB%98%E9%A2%91%E9%9D%A2%E8%AF%95%E7%B3%BB%E5%88%97/k%E4%B8%AA%E4%B8%80%E7%BB%84%E5%8F%8D%E8%BD%AC%E9%93%BE%E8%A1%A8.md)
- [fucking algorithm : check palindrome linked list](https://github.com/labuladong/fucking-algorithm/blob/master/%E9%AB%98%E9%A2%91%E9%9D%A2%E8%AF%95%E7%B3%BB%E5%88%97/%E5%88%A4%E6%96%AD%E5%9B%9E%E6%96%87%E9%93%BE%E8%A1%A8.md)

**辨識** — 輸入是一個 `ListNode head`，答案是一條重新接線過的串列，或其中某個節點。追問幾乎都是「只用 O(1) 額外記憶體」，這就排除了把值複製進陣列的做法。

**關鍵** — 單向節點只知道它*後面*是誰。要在某個節點刪除或插入，你必須站在它**前一個**節點上（`cur.next = cur.next.next`），所以下面每一個技巧，都是在確保動手術的當下你握著對的把手。

<!--CODE-->

<!--CODE-->


<!--CODE-->

<!-- 021e93bf5c07 -->
### 0-1) 型別
- 鏈結串列
- 環狀鏈結串列
- 雙向鏈結串列
- 雙向串列（Double Linked list）
    - LC 146、LC 460
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

<!-- a60ee060d400 -->
### 辨識

| 題目敘述裡的訊號 | 限制條件提示 | 該用 | 不是這個，如果 |
|---|---|---|---|
| 「反轉這條鏈結串列」 | O(1) 額外記憶體 | [Template 1](#template-1-iterative-reversal--lc-206-) 迭代反轉 | 只反轉一段區間或每 k 個一組 → [Template 4](#template-4-reverse-a-segment-reconnect-both-ends--lc-92-) |
| 「移除所有值為 … 的節點」、「刪除重複」、head 可能被刪 | 一趟 | [Template 2](#template-2-dummy-head-deletion--lc-203-) 虛擬頭節點 | 你只拿到要刪的那個節點、拿不到 head（LC 237）→ 把下一個節點的值複製進來 |
| 「所有小於 x 的節點排在 … 之前」、「奇數位再接偶數位」、「保持相對順序」 | O(1) 額外記憶體 | [Template 3](#template-3-two-dummies-split-then-join--lc-86-) 兩個虛擬節點 | 組內順序可以改變 → 陣列分割就夠了 |
| 「反轉位置 left 到 right 的節點」、「每 k 個一組」、「兩兩交換相鄰節點」 | 一趟、O(1) 記憶體 | [Template 4](#template-4-reverse-a-segment-reconnect-both-ends--lc-92-) 反轉一段 | 整條都要反轉 → Template 1 |
| 「中間節點」、「倒數第 n 個節點」、「切成兩半」、「重排」、「回文」 | 一趟、長度未知 | [Template 5](#template-5-fastslow-middle--lc-876-) 快慢指標 | 「有沒有環／環從哪開始」→ [2_pointers_linkedlist.md](./2_pointers_linkedlist.md) |
| 「位數以逆序存放」、「把兩個數相加」 | 串列最長 100 個節點，任何固定寬度的整數都放不下 | [Template 6](#template-6-carry-loop-over-a-dummy--lc-2-) 進位迴圈 | 最高位在前（LC 445）→ 先把兩條都反轉，或用兩個堆疊 |
| 「設計一個快取」、「get 和 put 都要 O(1)」、「淘汰最久沒用的」 | 每個操作 O(1) | [Template 7](#template-7-hash-map--doubly-linked-list-lru--lc-146-) 雜湊表 + 雙向鏈結串列 | 依使用頻率淘汰（LC 460）→ [LFU 變體](#lfu-variant--one-list-per-frequency-plus-a-min_freq-pointer-lc-460-) |

<!-- e893b4e71370 -->
## 1) 通用形式

下面每個模板都跑在上面定義的 `ListNode` 上。排列順序就是面試時一層層往上疊的順序：先反轉，再虛擬頭節點，接著是把兩者結合的技巧，最後才是快取設計。

<!-- 8a9258780cca -->
### Template 1: Iterative Reversal — LC 206 ⭐⭐⭐⭐⭐

**辨識** — 「反轉一條單向鏈結串列」，或任何解法步驟裡有「反轉後半段」的題目（LC 143、LC 234）。追問：O(1) 額外記憶體，所以用迭代，不用遞迴。

**關鍵** — `prev` 永遠是已反轉部分的頭，`curr` 是還沒動過那部分的頭；在翻指標之前先存下 `curr.next`，才讓沒動過的那部分還拿得到。

<!--CODE-->

<!--CODE-->

**複雜度**
- **時間 O(n)**：`while curr` 迴圈每個節點走一次，在那裡做四次賦值。
- **空間 O(1)**：只有 `prev`、`curr` 和 `nxt`；節點是重新接線，從不複製。

**手動追蹤**

<!--CODE-->

**陷阱**
- 在 `nxt = curr.next` 之前寫 `curr.next = prev`，後面整串就弄丟了：`1 -> 2 -> 3` 回傳 `1`。
- 回傳 `head` 而不是 `prev`：`head` 現在是*尾巴*，所以 `[1,2,3]` 回傳 `[1]`。
- `[]` 或 `[1]` 不需要特例：迴圈跑零次或一次，`prev` 就是對的。

**不是這個，如果** — 只反轉一部分（一段區間、每 k 個、成對）→ [Template 4](#template-4-reverse-a-segment-reconnect-both-ends--lc-92-) 的反轉一段，也就是這個迴圈跑 k 次再加兩次接線。

**練習** — LC 206 ⭐ · LC 234 *（反轉後半再比對）* · LC 143 ⭐ *（中點、反轉、交錯）* · LC 92 *（一段區間：Template 4）*

<!-- a7efa3bb1aaf -->
#### 遞迴寫法 — O(n) 堆疊

同一個答案，由上而下：先反轉 `head` 之後的全部，再把 `head` 掛到原本跟在它後面的那個節點之後。每個節點要吃一個 stack frame，所以它是第二個答案，不是第一個。

<!--CODE-->

<!--CODE-->

<!-- d5d46f5dda34 -->
### Template 2: Dummy Head Deletion — LC 203 ⭐⭐⭐⭐⭐

**辨識** — 「移除所有值為 … 的節點」、「刪除重複」、「移除第 n 個節點」，或任何 **head 本身**可能被刪掉或換掉的修改。沒有虛擬節點，每一題都得多一個 `if head 就是目標` 的分支。

**關鍵** — 虛擬節點讓 `curr` 有一個站在 head *之前*的位置，而 `curr` 只看 `curr.next`。刪完之後 `curr` 原地不動，所以一整串命中（`[6,6,6]`）會一個連結一個連結地被移掉，一個都不漏。

<!--CODE-->

<!--CODE-->

**複雜度**
- **時間 O(n)**：`while curr.next` 的每一輪不是刪掉一個節點，就是讓 `curr` 前進，所以總共最多 n 輪。
- **空間 O(1)**：一個虛擬節點和一個游標。

**手動追蹤**（`[6,6,6,3]`、val=6）

<!--CODE-->

**陷阱**
- 刪完之後還讓 `curr` 前進，會跳過滑進 `curr.next` 的那個節點：`[6,6,3]`、val=6，回傳 `[6,3]`。
- 回傳 `head` 而不是 `dummy.next`：`[7,7,7]`、val=7，回傳已被刪掉的 `7`，而不是 `[]`。
- 檢查 `curr.val` 而不是 `curr.next.val`：等你看到命中時已經站在它上面了，而單向節點走不回前一個節點。

**不是這個，如果** — 你只拿到要刪的那個節點、從來拿不到 head（LC 237）：沒有 `prev` 可以站，所以把 `node.next.val` 複製進來，改刪下一個節點。

**練習** — LC 203 · LC 83 *（比較相鄰的值）* · LC 19 ⭐ *（間隔 n）* · LC 82 *（重複的全部刪）* · LC 1474

詳寫：[LC 19 與其他 Template 2 題目](./linked_list_examples.md#dummy-head-deletion-examples)在 linked_list_examples；LC 82 在 [2_pointers_linkedlist § 2-2](./2_pointers_linkedlist.md#2-2-remove-duplicates-from-sorted-list-ii--lc-82-)。

<!-- ed306fa6055a -->
### Template 3: Two Dummies, Split then Join — LC 86 ⭐⭐⭐⭐

**辨識** — 每個節點都要分進兩組之一，而且每一組都得**保持原本的順序**：「所有小於 x 的節點排在大於等於 x 的節點之前 … 保持相對順序」（LC 86）、「把奇數位的節點放在一起，後面接偶數位」（LC 328）。

**關鍵** — 每條鏈都有一個虛擬節點（握著它的頭的固定把手）和一個尾指標；每個節點只會接到其中一條的尾巴，所以走完一趟後 `p1.next = dummy_2.next` 就能把兩條接起來，從來不用記第二條鏈的第一個節點。

| 把手 | 它是什麼 | 用在 |
|---|---|---|
| `dummy_1` | 站在第一條鏈的頭之前 | 答案 — `return dummy_1.next` |
| `p1` | 第一條鏈的**尾巴** | 接起來：第一條鏈的尾巴要指向第二條鏈的頭 |
| `dummy_2` | 站在第二條鏈的頭之前 | 接起來：`dummy_2.next` **就是**第二條鏈的頭 |
| `p2` | 第二條鏈的**尾巴** | 收尾：`p2.next = None` |

<!--CODE-->

<!--CODE-->

**複雜度**
- **時間 O(n)**：單一一趟 `while curr` 把每個節點接到某一條的尾巴；接起來是 O(1)。
- **空間 O(1)**：節點是重新串接，不是複製；新增的只有兩個虛擬節點。

**手動追蹤**（`[1,4,3,2,5,2]`，`x = 3`）

<!--CODE-->

**陷阱**
- **忘了 `p2.next = None` 會形成環。** 節點是重新串接、不是複製，所以第二條鏈的最後一個節點還帶著它*舊的* `next`。在上面的追蹤裡就是 `5 -> 2`，而最後那個 2 在第一條鏈裡、排在 5 *之前* — 繞成一圈。只有第二條鏈需要切：第一條鏈的尾巴反正馬上會被接起來那一步覆寫。
- **用 `dummy_2.next` 接，不是 `dummy_2`。** 虛擬節點只是個值為 `0` 的佔位；把虛擬節點本身掛到 `p1` 後面，會多出一個幽靈節點。
- **空組不需要防呆。** 全部節點都 `< x`：`dummy_2.next` 是 `None`，接起來那步只是重新把第一條鏈收尾。全部都 `>= x`：`p1` 還停在 `dummy_1`，所以接起來會讓 `dummy_1.next` 變成第二條鏈的頭。空輸入也一樣回傳 `None`。

**不是這個，如果** — 組內順序可以改變，或輸入是陣列：那裡 LC 75 的原地荷蘭國旗交換比較省，但它不穩定，而 LC 86 要求每組內保持原本的相對順序。

| # | 題目 | 決定進哪條鏈的條件 |
|---|---------|-----------------------------------|
| 86 | Partition List | `node.val < x` — 就是上面的模板 |
| 328 | Odd Even Linked List | 節點的**索引**是偶數 — 一樣兩個虛擬節點、`p2.next = None`、`p1.next = dummy_2.next` |
| 2161 | Partition Array According to Given Pivot | 陣列版：三桶（`<`、`==`、`>`），依序串接 |
| 21 | Merge Two Sorted Lists | 反方向的操作 — 一個虛擬節點、*兩條*輸入、輸出一條鏈 |

**練習** — LC 86 · LC 21 ⭐ *（一個虛擬節點、兩條輸入）* · LC 328 *（依索引拆）* · LC 725 *（k 條鏈）*

<!-- cdab16bdc4b6 -->
### Template 4: Reverse a Segment, Reconnect Both Ends — LC 92 ⭐⭐⭐⭐⭐

**辨識** — 「反轉位置 left 到 right 的節點」（LC 92）、「每 k 個節點一組反轉」（LC 25）、「兩兩交換相鄰節點」（LC 24）。追問：一趟、O(1) 記憶體。

**關鍵** — 從 `start` 反轉 k 個節點後會留下三個把手：新的前端（第 k 個節點）、新的尾巴（`start` 本身），以及這一段之後的第一個節點。接線永遠是 `prev.next = new_head` 和 `new_tail.next = next_node`；這些題目的差別只在那一段**在哪裡**。

| 回傳值 | 它是什麼 | 用來接哪裡 |
|----------|-----------|-------------------|
| `prev`（`new_head`） | 反轉後這一塊的新 **head** | `prev_of_segment.next = new_head` |
| `head`（`new_tail`） | 新的 **tail**（原本的第一個節點） | `new_tail.next = next_node` |
| `curr`（`next_node`） | 這一段**之後**的第一個節點 | tail 必須指到這裡 |

<!--CODE-->

<!--CODE-->

**複雜度**
- **時間 O(n)**：走到 `left` 加上反轉 k 步，合起來每個節點最多碰一次。
- **空間 O(1)**：固定數量的指標；輔助函式是迴圈，不是遞迴。

**手動追蹤**（`[1,2,3,4,5]`、`left=2`、`right=4` → 反轉 `2,3,4` 這 3 個節點）

<!--CODE-->

**陷阱**
- 從 `head` 而不是 `dummy` 開始走 `prev`：`left = 1` 時這一段前面沒有節點，所以 `[1,2,3]`、left=1、right=2 的前端接不回去。
- 忘了 `new_tail.next = next_node` 會弄丟尾巴的 `5`：輔助函式已經把原本起點的 `next` 設成 `None`，所以結果是 `1 -> 4 -> 3 -> 2 -> None`，不會形成環。
- 在 LC 25 裡，對最後一組不足 k 個的節點也呼叫輔助函式，會把它一起反轉；先數 k 個（`has_k`），不足的尾巴保持原狀。

**不是這個，如果** — 整條都要反轉 → [Template 1](#template-1-iterative-reversal--lc-206-)；串列是*旋轉*（LC 61）而不是反轉 → 用間隔找到新的尾巴再重新縫合，完全不用反轉。

**練習** — LC 92 · LC 206 ⭐ *（k = 長度）* · LC 24 *（k = 2，展開）* · LC 25 *（迴圈呼叫輔助函式）* · LC 61

詳寫：[LC 24、LC 25 與 LC 430](./linked_list_examples.md#reverse-a-segment-examples) 在 linked_list_examples。

<!-- 4e4cc4008250 -->
#### 遞迴寫法 — 先反轉前 n 個，再反轉一段區間

同一個反轉一段的遞迴版本。`reverseN` 反轉前 `n` 個節點，並把它們之後的節點記在 `successor`；`reverseBetween` 把 `left` 一路減到 1，再呼叫它。它要 O(n) 的堆疊，所以是第二個答案。

<!--CODE-->

<!-- 5db11bf32a46 -->
### Template 5: Fast/Slow Middle — LC 876 ⭐⭐⭐⭐

**辨識** — 「回傳中間節點」、「把串列切成兩半」（LC 143、LC 148、LC 234）、「倒數第 n 個節點」 — 任何需要從尾端或中間量位置、但沒給長度的串列題。

**關鍵** — `slow` 每走一個節點，`fast` 就走兩個，所以當 `fast` 再也走不了完整的一步時，`slow` 剛好走了一半：它站在中點上。

<!--CODE-->

<!--CODE-->

**複雜度**
- **時間 O(n)**：`fast` 在 `while` 跑 n / 2 輪後到達尾端。
- **空間 O(1)**：兩個指標。

**手動追蹤**

<!--CODE-->

**陷阱**
- `while fast.next and fast` 在偶數長度的串列上會對 `None` 解參考；先檢查 `fast`。
- 為了合併排序（LC 148）或重排（LC 143）而切串列時，你需要的是**第一個**中點，左半才不會比較長：迴圈條件寫 `while fast.next and fast.next.next`。用第二個中點的寫法，`[1,2]` 永遠切不開，合併排序就會無窮遞迴。

**不是這個，如果** — 「串列有沒有環／環從哪開始」（LC 141、LC 142）用的是同樣兩種速度但不同的停止條件 → [2_pointers_linkedlist.md](./2_pointers_linkedlist.md)；「倒數第 n 個」用的是同樣速度、間隔 n → [LC 19 那張卡](./linked_list_examples.md#remove-nth-node-from-end-of-list--lc-19)。

**練習** — LC 876 · LC 141 ⭐ *（環：同樣的速度，相遇就停）* · LC 234 *（中點 + 反轉）* · LC 143 ⭐ *（第一個中點、反轉、交錯）* · LC 148 *（第一個中點、合併排序）*

<!-- 1d385c7a7e51 -->
### Template 6: Carry Loop over a Dummy — LC 2 ⭐⭐⭐

**辨識** — 「位數以逆序存放，每個節點只有一位數；把兩個數相加」。串列可以長達 100 個節點，所以這些數放不進任何固定寬度的整數：要一位一位地加。

**關鍵** — 每一步寫下 `(a + b + carry) % 10`，並把 `// 10` 進位；只要**任一條串列或進位**還沒空，迴圈就繼續，因為進位可能活得比兩個輸入都久。

<!--CODE-->

<!--CODE-->

**複雜度**
- **時間 O(max(m, n))**：`while` 每一步從每條串列各取一位，最後頂多再為進位多跑一步。
- **空間 O(1)**，不算結果串列；結果最多 max(m, n) + 1 個節點。

**陷阱**
- 迴圈寫成 `while l1 and l2` 會丟掉較長那條的尾巴：`[9,9,9]` + `[1]` 回傳 `[0]`。
- 迴圈寫成 `while l1 or l2` 會丟掉最後的進位：`[5]` + `[5]` 回傳 `[0]`，而不是 `[0,1]`。
- 把每條串列轉成整數在 Python 可行，但在 Java 遇到 100 位數的輸入會讓 `long` 溢位；這不是面試官要的答案。

**不是這個，如果** — 最高位在前（LC 445）：用 [Template 1](#template-1-iterative-reversal--lc-206-) 把兩條串列都反轉，結果再反轉回來；或把位數推進兩個堆疊，用往前插入的方式建結果。

**練習** — LC 2 · LC 369 *（加一）* · LC 445 *（最高位在前）*

<!-- 1cf0ac408eb3 -->
### Template 7: Hash Map + Doubly Linked List (LRU) — LC 146 ⭐⭐⭐⭐⭐

**辨識** — 「設計一個快取」、「`get` 和 `put` 都必須平均 O(1)」、「淘汰最久沒用的 key」。任何同時需要依 key 做 O(1) 查找、又要以 O(1) 維護順序的結構。

**關鍵** — 表把 key 帶到它的節點，而節點因為是雙向連結，能在 O(1) 內把自己摘下來。最近使用度就是串列的順序：最近用的節點在 `tail` 前面，所以淘汰對象永遠是 `head.next`。

<!--CODE-->

**關鍵輔助操作** — 整個類別就是這兩個，成對呼叫：
- `remove(node)` — 用 O(1) 把節點從串列中摘掉；只需要節點本身，因為它帶著前後兩個鄰居
- `add_to_tail(node)` — 用 O(1) 把節點插到 dummy tail 前面（MRU 位置）
- 「碰一下」某個 key = `remove(node)` 再 `add_to_tail(node)` — `get` **和** `put` 更新既有 key 時都用它

<!--CODE-->

<!--CODE-->

**第二種解法 — 函式庫寫法。** Python 的 `OrderedDict` 和 Java 的 `LinkedHashMap` *本身就是*
架在雙向鏈結串列上的 hash map。面試時先講這個。多數面試官接著會要你手刻上面那條串列，
所以兩種都要能寫。

<!--CODE-->

<!--CODE-->

**複雜度**
- **每次呼叫 O(1) 時間**：`get` 和 `put` 做一次表查找，加上 `remove` / `add_to_tail` 裡固定數量的指標賦值；沒有任何東西會走訪串列。
- **空間 O(capacity)**：表和串列各自最多存 capacity + 1 筆，而且只在插入到淘汰之間那一瞬間。

**手動追蹤**（capacity=2，LC 146 範例 1）

<!--CODE-->

**陷阱**
- **節點必須存自己的 `key`。** 淘汰是透過 `head.next` 找到要淘汰的節點，接著必須刪掉它在表裡的 entry；少了 `node.key`，表會留著過期的 entry，`len(kv_map)` 永遠不會變小，`get` 還會回傳一個早已不在串列裡的節點。
- **淘汰 `head` 而不是 `head.next`** 會把哨兵刪掉：下一次 `remove` 就會對 `None` 解參考。
- **在檢查既有 key 之前就淘汰**，單純的更新也會丟掉一筆 entry：capacity 2，`put(1,1) put(2,2) put(1,10)` 會弄丟 key 2。
- **兩種方向混用** — 插在尾端卻淘汰 `tail.prev` — 會把剛碰過的 key 淘汰掉。

| | 先插入、再淘汰（上面的模板） | 先淘汰、再插入 |
|---|---|---|
| 容量判斷 | 插入後 `len(kv_map) > capacity` | 插入前 `len(kv_map) == capacity` |
| 淘汰對象 | `head.next` — 新節點在尾端，所以絕不會是它自己 | `head.next` |
| 陷阱 | 沒有額外陷阱 | 淘汰必須放在「新 key」分支**裡面**；如果在檢查 key 是否存在之前就淘汰，單純的更新也會丟掉一筆 entry |

**不是這個，如果** — 依**使用頻率**淘汰、最近使用度只用來打破平手（LC 460）→ 每個頻率一條串列，見下。一個普通 `dict` 加一個 key 的 list 看起來像 LRU，但不是：每次 `keys.remove(key)` 都是 O(n)。

| # | 題目 | 差別在哪 |
|---|---------|----------------|
| 146 | LRU Cache | 經典模式 — 淘汰最久沒用的 |
| 460 | LFU Cache | 兩層結構：每個頻率一條 LRU 串列 + `min_freq` — [詳見下方](#lfu-variant--one-list-per-frequency-plus-a-min_freq-pointer-lc-460-) |
| 432 | All O(1) Data Structure | 由計數桶組成的雙向鏈結串列 |
| 1472 | Design Browser History | 雙向鏈結串列，訪問新頁時把前方截斷 |
| 641 | Design Circular Deque | 固定容量的雙向鏈結串列，兩端都能操作 |
| 716 | Max Stack | 堆疊 + 雙向鏈結串列 + TreeMap，做到 O(log n) 的 popMax |

**練習** — LC 146 ⭐ · LC 1472 *（一個游標，往前截斷）* · LC 641 *（固定容量、兩端）* · LC 460 *（每個頻率一條串列：見下）* · LC 432

<!-- ea152097323c -->
#### **LFU 變體 — 每個頻率一條串列，再加一個 `min_freq` 指標（LC 460）** ⭐⭐⭐⭐

> **關鍵** — 每一條頻率串列都是上面那條 LRU 串列；`min_freq` 指出淘汰對象住在哪一條，而它只會在 touch 時 `+1`，或在插入時重設為 `1`。

**化約為**：[Template 7](#template-7-hash-map--doubly-linked-list-lru--lc-146-) — 同一條帶哨兵的串列，每個頻率一條。

**改了什麼**：LRU 依*最近使用度*淘汰，所以一條串列就夠 — 它的 `head.next` 永遠是淘汰對象。
LFU 依*使用頻率*淘汰，最近使用度只拿來打破平手 — 所以那一條串列變成**每個頻率一條 LRU
串列**，再用一個整數 `min_freq` 指出淘汰對象住在哪一條。每一條頻率串列都正是上面模板裡那條
帶哨兵的串列；指標手術沒有任何新東西，新的只有外圍的記帳。

**結構配置**：
<!--CODE-->

**三個動作** — 每個操作都是其中的一到兩個，依這個順序：

| 動作 | 做什麼 | `min_freq` 怎麼走 |
|---|---|---|
| **touch(node)** — `get`，以及 `put` 更新既有 key | 從 `freq_to_list[f]` 摘下、`f += 1`、接到 `freq_to_list[f]` 的尾端 | 如果剛離開的那條串列空了**而且** `f == min_freq`：`min_freq += 1`。這是精確值，不是搜尋 — 這個節點自己剛落在 `f + 1`，所以那條串列必定非空 |
| **evict** — `put` 新 key 且快取已滿 | 淘汰對象是 `freq_to_list[min_freq].head.next`；把它摘下並刪掉 map 裡的 entry | 不動 — 接下來的插入會重設它 |
| **insert** — `put` 新 key | 新的 `Node(key, value, 1)` 接到 `freq_to_list[1]` 的尾端 | `min_freq = 1`，每次都是 — 全新的 key 依定義就是最不常用的 |

**模板**：
<!--CODE-->

**視覺追蹤**（capacity=2，LC 460 範例 1；每條串列依 LRU → MRU 顯示）：
<!--CODE-->

**陷阱 — 會把 O(1) 賠掉的那幾個**：
- **`min_freq += 1` 是精確值，從來不是掃描。** 執行途中最小那條串列會變空的唯一方式，是它
  最後一個節點搬到 `min_freq + 1`，所以新的最小值不用找就知道。在這裡寫 `min(freq_to_list)`，
  每次 touch 就變成 O(不同頻率的數量)。
- **每次插入新 key 都要 `min_freq = 1`**，不是只有快取為空時：不管裡面已經有什麼，新 key
  都是最不常用的。
- **先淘汰再插入，而且只在新 key 的路徑上。** `put` 更新既有 key 是一次 touch，絕不是淘汰 —
  跟 LRU「先淘汰、再插入」順序的陷阱一模一樣。
- **`capacity == 0` 的防護是必要的。** 少了它，第一次 `put` 會去 `freq_to_list[0]` 淘汰，
  而那條串列不存在。
- **平手是用*同一條串列內*的最近使用度打破。** 接在尾端、從開頭淘汰；兩端混用會把剛碰過的
  key 淘汰掉，跟 LRU 完全一樣。
- **節點除了 `key` 也要存 `freq`。** `key` 是讓淘汰能走回 map；`freq` 是讓 touch 能找到它
  必須離開的那條串列。

**函式庫捷徑**：每個頻率一個 `OrderedDict` — `popitem(last=False)` 就是 `pop_head`，
把 key 接到尾端就是 `add_to_tail` — Java 則是每個頻率一個 `LinkedHashSet<Integer>`。結構一樣、
`min_freq` 規則一樣，程式碼只有三分之一：

<!--CODE-->

帶你走到這組搭配的那張表在 [design.md](./design.md)。LC 432 再往前一步，把*桶本身*也串成一條雙向鏈結串列，於是連
`min_freq` 這個整數都不必維護。

---

<!-- 82c7bb0a5ce6 -->
## 2) 模式選擇

鏈結串列題其實很少真的在考串列。它們考的是：動手術的當下，**你手上必須握著哪個把手** — 這份文件上的每個技巧，存在的理由都是確保你握著它。挑法要看答案需要什麼，不是看題目叫什麼名字。

| 如果題目要你… | 就用 | 因為 | 詳寫在 |
|---|---|---|---|
| 在**任何位置**刪除或插入，包含 head | **虛擬頭節點** | 它讓 `prev` 有個站在 head *之前*的位置，「刪掉 head」就不再是特例 | [Template 2](#template-2-dummy-head-deletion--lc-203-) |
| 移除**所有**符合某個值的節點 | **虛擬節點 + 檢查 `curr.next`** | 刪完之後你必須能*原地不動*，否則像 `[6,6,6]` 這種連續值會漏掉一個 | [Template 2](#template-2-dummy-head-deletion--lc-203-) |
| 把節點**拆成兩組**，各自保持原本順序 — 依值或依位置 | **兩個虛擬節點，每條鏈一個尾指標，最後接起來** | `p1.next = dummy_2.next` 不用另外記第二條鏈的頭就能接上；`p2.next = None` 擋掉環 | [Template 3](#template-3-two-dummies-split-then-join--lc-86-) |
| 反轉**整條**串列 | **三步迴圈**：先存下 next → 翻指標 → 前進 | O(1) 空間；遞迴版答案一樣，卻要每個節點吃掉一個 stack frame | [Template 1](#template-1-iterative-reversal--lc-206-) |
| 反轉**一段** — `[left, right]`、每 `k` 個，或成對 | **反轉 k 個的輔助函式，回傳三個把手** | LC 92 / 25 / 24 的差別只在*那一段在哪*，反轉的方式完全一樣 | [Template 4](#template-4-reverse-a-segment-reconnect-both-ends--lc-92-) |
| 找中點、偵測環，或走到**倒數**第 n 個 | **快慢指標** | 一趟掃完、O(1) 空間，也不用先算長度 | [Template 5](#template-5-fastslow-middle--lc-876-)、[2_pointers_linkedlist.md](./2_pointers_linkedlist.md) |
| **重排** — 交錯、切分、旋轉、回文判斷 | **快慢指標切一半 → 反轉後半 → 合併** | 每一題重排都是這三個基本操作依序組合；沒有一個是新東西 | [LC 143](./linked_list_examples.md#reorder-list--lc-143)、[LC 234](./2_pointers_linkedlist.md#1-1-6--palindrome-linked-list-find-mid---reverse-2nd-half---compare) |
| 合併**兩條**已排序串列 | **虛擬節點 + 一趟合併走訪**，接節點而不是複製值 | 尾端指標就是全部的訣竅：`cur.next = l1 or l2` 收尾 | [LC 21](./linked_list_examples.md#merge-two-sorted-lists--lc-21) |
| 合併 **k** 條已排序串列，或把一條串列排序 | **分治法** — 兩兩合併，或用中點做合併排序 | O(n log k) / O(n log n)；用堆積則是拿 O(k) 空間換掉遞迴 | [heap § Template 3](./heap.md#template-3-k-way-merge--lc-23-)、[LC 148](./advanced_divide_and_conquer.md#variation-lc-148--sort-list) |
| 同時要**任意位置**讀取*和* O(1) 淘汰 — 依最近使用度，或依使用頻率 | **雙向鏈結串列 + 雜湊表** — 一條串列，或每個頻率一條 | 表給你節點，雙向節點給你它的鄰居 — 少了任一個都不夠 | [Template 7](#template-7-hash-map--doubly-linked-list-lru--lc-146-)、[LFU 變體](#lfu-variant--one-list-per-frequency-plus-a-min_freq-pointer-lc-460-)、[design.md](./design.md) |
| 對存成串列的位數做**算術** | **在虛擬節點上跑進位迴圈**，若最高位在前就先反轉 | 進位會活得比兩個輸入都久，所以迴圈條件是 `l1 or l2 or carry` | [Template 6](#template-6-carry-loop-over-a-dummy--lc-2-)、[LC 369](./linked_list_examples.md#plus-one-linked-list--lc-369) |
| 回答需要**隨機存取或視窗**的問題 | **先倒進陣列，再用陣列的技巧** | 前綴和與單調堆疊都需要索引，串列沒有；而且通常允許 O(n) 額外空間 | [LC 1171](./linked_list_examples.md#remove-zero-sum-consecutive-nodes-from-linked-list--lc-1171)、[LC 1019](./linked_list_examples.md#next-greater-node-in-linked-list--lc-1019) |

<!-- 6ef117bacd71 -->
### 四個陷阱

1. **把串列弄丟。** 還沒存下 `curr.next` 就寫 `curr.next = prev`，後面整串就沒了。先存起來 — 這就是反轉迴圈非得照那個順序寫的原因。
2. **回傳錯的 head。** 任何可能動到第一個節點的操作之後，要回傳 `dummy.next`，不是 `head`：`head` 可能已經不在串列裡了。
3. **留下一個環。** 遞迴反轉裡只寫 `head.next.next = head`，卻漏掉後面的 `head.next = null`，最後兩個節點就會互指。
4. **走過頭。** 兩步跳要寫成 `while (fast != null && fast.next != null)`。兩個條件順序寫反，在偶數長度的串列上就會對 null 解參考。

<!-- 09092017ed67 -->
## 3) 實戰題解

套用這些模板的題目，都以題目卡的形式放在 **[linked_list_examples.md](./linked_list_examples.md)**，每個模板一節，順序相同：

| 模板 | 題目 |
|---|---|
| [迭代反轉](./linked_list_examples.md#iterative-reversal-examples) | LC 143, 1019 |
| [虛擬頭節點刪除](./linked_list_examples.md#dummy-head-deletion-examples) | LC 19, 147, 1171 |
| [兩個虛擬節點，先拆再接](./linked_list_examples.md#two-dummies-split-then-join-examples) | LC 21, 725, 817 |
| [反轉一段](./linked_list_examples.md#reverse-a-segment-examples) | LC 24, 430, 25 |
| [快慢指標找中點](./linked_list_examples.md#fastslow-middle-examples) | 指向 LC 160, 234, 142, 148 |
| [進位迴圈](./linked_list_examples.md#carry-loop-examples) | LC 369 |
| [雜湊表 + 雙向鏈結串列](./linked_list_examples.md#hash-map--doubly-linked-list-examples) | LC 138 |

<!-- b1833857d3d1 -->
## 4) 練習路線

| 階段 | 題目（由易到難） | 它帶來什麼 |
|---|---|---|
| **核心** ⭐ | LC 206 · LC 21 · LC 141 · LC 19 · LC 143 · LC 146 · LC 23 | 反轉、一個虛擬節點加一趟合併走訪、快慢指標（環與間隔）、三個重排基本操作一起用、快取設計，最後是 k 路合併 |
| **練習** | LC 203 · LC 876 · LC 83 · LC 234 · LC 2 · LC 86 · LC 92 · LC 24 · LC 328 · LC 82 · LC 445 · LC 148 | 每個模板在它的錨點題上各做一次，再做一個變化 |
| **進階** | LC 138 · LC 25 · LC 460 · LC 432 | 用節點表複製；每組反轉；每個頻率一條串列；把桶串在串列上 |
