<!-- d64d7aa460bd -->
# 鏈結串列 — 實戰題解

> **範圍** — [linked_list.md](./linked_list.md) 背後的題目集：每題歸檔在它歸約到的核心模板之下，寫成一張題目卡，點出它多加的那一步；模板本身與各模板的錨點題都放在核心文件。
> **另見**：[linked_list.md](./linked_list.md) — 這些題目歸約到的七個模板；[2_pointers_linkedlist.md](./2_pointers_linkedlist.md) — 環偵測、相交與回文判斷，在那裡詳寫；[heap.md](./heap.md) — LC 23 的 k 路合併；[hash_map.md](./hash_map.md) — LC 1171 背後的前綴和表，以及 LC 138 背後的節點對照表；[monotonic_stack.md](./monotonic_stack.md) — LC 1019 背後的堆疊。

<!-- 1d43e2ef29a3 -->
## LeetCode 題目清單

- [Linked List](https://leetcode.com/problem-list/linked-list/)
- [Doubly-Linked List](https://leetcode.com/problem-list/doubly-linked-list/)

<!-- 801f906d4b2a -->
## 總覽

[linked_list.md](./linked_list.md) 收著模板 — 迭代反轉、虛擬頭節點、兩個虛擬節點、反轉一段、快慢指標、進位迴圈，以及雜湊表加雙向鏈結串列 — 每個都在它的錨點題上詳寫。這份文件是套用它們的題目，每個模板一節，順序相同。每張卡說明它歸約到哪個模板、多加了哪一步；在別的文件詳寫過的題目只放一列指標，不放第二份。

<!-- 453e24d1929f -->
## 索引

| LC | 題目 | 歸約到 | 難度 | 位置 |
|---|---|---|---|---|
| 143 | Reorder List | [linked_list § Template 1](./linked_list.md#template-1-iterative-reversal--lc-206-) | Medium | [題目卡](#reorder-list--lc-143) |
| 1019 | Next Greater Node In Linked List | [monotonic_stack § Template 1](./monotonic_stack.md#template-1-next-greater-element-decreasing-stack--lc-496) | Medium | [題目卡](#next-greater-node-in-linked-list--lc-1019) |
| 19 | Remove Nth Node From End of List | [linked_list § Template 2](./linked_list.md#template-2-dummy-head-deletion--lc-203-) | Medium | [題目卡](#remove-nth-node-from-end-of-list--lc-19) |
| 82 | Remove Duplicates from Sorted List II | [linked_list § Template 2](./linked_list.md#template-2-dummy-head-deletion--lc-203-) | Medium | 詳寫於 [2_pointers_linkedlist § 2-2](./2_pointers_linkedlist.md#2-2-remove-duplicates-from-sorted-list-ii--lc-82-) |
| 147 | Insertion Sort List | [linked_list § Template 2](./linked_list.md#template-2-dummy-head-deletion--lc-203-) | Medium | [題目卡](#insertion-sort-list--lc-147) |
| 1171 | Remove Zero Sum Consecutive Nodes from Linked List | [linked_list § Template 2](./linked_list.md#template-2-dummy-head-deletion--lc-203-) + [hash_map § Template 4](./hash_map.md#template-4-prefix-sum-count-map--lc-560-) | Medium | [題目卡](#remove-zero-sum-consecutive-nodes-from-linked-list--lc-1171) |
| 21 | Merge Two Sorted Lists | [linked_list § Template 3](./linked_list.md#template-3-two-dummies-split-then-join--lc-86-) | Easy | [題目卡](#merge-two-sorted-lists--lc-21) |
| 725 | Split Linked List in Parts | [linked_list § Template 3](./linked_list.md#template-3-two-dummies-split-then-join--lc-86-) | Medium | [題目卡](#split-linked-list-in-parts--lc-725) |
| 817 | Linked List Components | [linked_list § Template 3](./linked_list.md#template-3-two-dummies-split-then-join--lc-86-) | Medium | [題目卡](#linked-list-components--lc-817) |
| 23 | Merge k Sorted Lists | [heap § Template 3](./heap.md#template-3-k-way-merge--lc-23-) | Hard | 詳寫於 heap.md |
| 92 | Reverse Linked List II | [linked_list § Template 4](./linked_list.md#template-4-reverse-a-segment-reconnect-both-ends--lc-92-) | Medium | 錨點題，詳寫於 linked_list.md |
| 24 | Swap Nodes in Pairs | [linked_list § Template 4](./linked_list.md#template-4-reverse-a-segment-reconnect-both-ends--lc-92-) | Medium | [題目卡](#swap-nodes-in-pairs--lc-24) |
| 430 | Flatten a Multilevel Doubly Linked List | [linked_list § Template 4](./linked_list.md#template-4-reverse-a-segment-reconnect-both-ends--lc-92-) | Medium | [題目卡](#flatten-a-multilevel-doubly-linked-list--lc-430-) |
| 25 | Reverse Nodes in k-Group | [linked_list § Template 4](./linked_list.md#template-4-reverse-a-segment-reconnect-both-ends--lc-92-) | Hard | [題目卡](#reverse-nodes-in-k-group--lc-25) |
| 160 | Intersection of Two Linked Lists | [2_pointers_linkedlist § 1-1-5](./2_pointers_linkedlist.md#1-1-5--intersection-merge-point-of-2-linked-lists) | Easy | 詳寫於 2_pointers_linkedlist.md |
| 234 | Palindrome Linked List | [linked_list § Template 5](./linked_list.md#template-5-fastslow-middle--lc-876-) | Easy | 詳寫於 [2_pointers_linkedlist § 1-1-6](./2_pointers_linkedlist.md#1-1-6--palindrome-linked-list-find-mid---reverse-2nd-half---compare) |
| 142 | Linked List Cycle II | [2_pointers_linkedlist § 1-1-2](./2_pointers_linkedlist.md#1-1-2--return-the-ring-start-point-of-circular-linked-list) | Medium | 詳寫於 2_pointers_linkedlist.md |
| 148 | Sort List | [linked_list § Template 5](./linked_list.md#template-5-fastslow-middle--lc-876-) | Medium | 詳寫於 [advanced_divide_and_conquer § LC 148](./advanced_divide_and_conquer.md#variation-lc-148--sort-list) |
| 369 | Plus One Linked List | [linked_list § Template 6](./linked_list.md#template-6-carry-loop-over-a-dummy--lc-2-) | Medium | [題目卡](#plus-one-linked-list--lc-369) |
| 146 | LRU Cache | [linked_list § Template 7](./linked_list.md#template-7-hash-map--doubly-linked-list-lru--lc-146-) | Medium | 錨點題，詳寫於 linked_list.md |
| 138 | Copy List with Random Pointer | [hash_map § Template 8](./hash_map.md#template-8-old-to-new-node-map--lc-133) | Medium | [題目卡](#copy-list-with-random-pointer--lc-138) |
| 460 | LFU Cache | [linked_list § Template 7，LFU 變體](./linked_list.md#lfu-variant--one-list-per-frequency-plus-a-min_freq-pointer-lc-460-) | Hard | 詳寫於 linked_list.md |

<!-- 10a7ffbaa211 -->
## 迭代反轉題目

<!-- ba6fc12cb7a6 -->
### Reorder List — LC 143

> **關鍵** — 答案是前半段與*反轉後*的後半段交錯。在**第一個**中點之後切開，
> 兩半就不共用任何節點；反轉後半段，再輪流各取一個節點。後半段永遠不會比較長，
> 所以迴圈跑到後半段用完為止。

**歸約到**：[linked_list § Template 1](./linked_list.md#template-1-iterative-reversal--lc-206-)
— 只對後半段跑，後半段用 [Template 5](./linked_list.md#template-5-fastslow-middle--lc-876-) 的第一個中點迴圈找出，最後再一趟交錯走訪。

<!--CODE-->

<!--CODE-->

**試跑**（`[1,2,3,4,5]`）

<!--CODE-->

**陷阱**
- 漏掉切斷 `slow.next = None`：在 `[1,2,3,4]` 上，節點 2 仍指著 3，反轉後的後半段也以 3 結尾，交錯之後形成環 `3 -> 3`。
- 在 `first.next = second` *之後*才讀 `first.next`：一步之後前半段就弄丟了。

**相似題** — LC 234 *（中點 + 反轉，再比較而不是交錯）* · LC 2130 *（中點 + 反轉，再兩兩求和）*

<!-- 97830db01755 -->
### Next Greater Node In Linked List — LC 1019

> **關鍵** — 堆疊裡放的是還在等更大值的索引，值由底到頂遞減。新值會彈出每個比它小的索引，
> 並成為它們的答案；最後留在堆疊裡的沒有答案，維持 `0`。串列只會被*讀*，
> 所以先複製成陣列：堆疊需要索引。

**歸約到**：[monotonic_stack § Template 1](./monotonic_stack.md#template-1-next-greater-element-decreasing-stack--lc-496)
— 唯一的改變是用一趟 [Template 1](./linked_list.md#template-1-iterative-reversal--lc-206-) 式的走訪，把串列轉成陣列。

<!--CODE-->

<!--CODE-->

**試跑**（`[2,7,4,3,5]`）

<!--CODE-->

**陷阱**
- 用 `<=` 而不是 `<` 來彈出：相等的值不算*更大*，所以 `[1,1,2]` 會得到 `[1,2,0]`，而不是 `[2,2,0]`。
- 推入值而不是索引：你就沒辦法知道一次彈出是*誰*的答案。

<!-- c25f19aad8f7 -->
## 虛擬頭節點刪除題目

<!-- 16f9aeda3ea0 -->
### Remove Nth Node From End of List — LC 19

> **關鍵** — `fast` 和 `slow` 都從虛擬節點出發，先讓 `fast` 走 n + 1 步；當 `fast`
> 走出尾端時，`slow` 正站在目標的**前一個**節點。從虛擬節點出發，
> 才能讓目標就是 head 時也成立。

**歸約到**：[linked_list § Template 2](./linked_list.md#template-2-dummy-head-deletion--lc-203-)
— 虛擬節點，再加上兩個指標之間的固定間隔，取代值的比對。

<!--CODE-->

<!--CODE-->

**試跑** — 刪掉 head（`[1,2,3,4,5]`、`n = 5`），也就是虛擬節點存在的理由：

<!--CODE-->

**陷阱**
- 兩個都從 `head` 出發、間隔 n：`[1,2,3,4,5]`、n = 2 時正確，但 `n = 5` 需要一個 `if fast is None: return head.next` 分支，正是虛擬節點省掉的那個。
- 回傳 `head` 而不是 `dummy.next`：`[1]`、n = 1，會回傳已刪掉的節點，而不是 `[]`。

**相似題** — LC 82 *（下方，一列指標）* · LC 61 *（同樣的間隔找出旋轉後的新尾巴）*

<!-- cd6808de1544 -->
### Insertion Sort List — LC 147

> **關鍵** — 虛擬節點是*已排序*前綴的頭，所以把節點插到目前最小值之前，
> 跟插到任何其他位置是同一個接線動作。每拆下一個節點，就讓 `p` 從虛擬節點走到
> 最後一個比它小的節點，再把它接在 `p` 之後。

**歸約到**：[linked_list § Template 2](./linked_list.md#template-2-dummy-head-deletion--lc-203-)
— 虛擬節點站在一條*正在建立*的串列的 head 之前，而對 `p.next` 的前瞻找的是插入點，不是命中。

<!--CODE-->

<!--CODE-->

**陷阱**
- 接線前忘了 `nxt = cur.next`：`cur.next = p.next` 會改掉它，`[4,2,1,3]` 未排序的剩餘部分在第一次插入後就弄丟了。
- 先設 `dummy.next = head`：已排序部分必須從空的開始，否則第一個節點會跟自己比較，串列形成環。

**不適用時** — 追問要求 O(n log n)：那是串列上的合併排序，[LC 148](#index)，用 Template 5 的第一個中點來切。

<!-- 3eacbc00d89f -->
### Remove Zero Sum Consecutive Nodes from Linked List — LC 1171

> **關鍵** — 兩個相同的前綴和，代表中間的節點加總為 0。記下每個前綴和**最後**
> 一次出現的節點，再走一趟，從每個節點直接跳到那個最後出現節點的下一個：
> 中間每一段零和區段一次跳過。

**歸約到**：[linked_list § Template 2](./linked_list.md#template-2-dummy-head-deletion--lc-203-)
— 虛擬節點（前綴 0）讓從 head 開始的零和區段也能被移除 — 再結合
[hash_map § Template 4](./hash_map.md#template-4-prefix-sum-count-map--lc-560-) 的前綴和表，只是這裡存的是節點而不是次數。

<!--CODE-->

<!--CODE-->

**試跑**（`[1,2,-3,3,1]`）

<!--CODE-->

**陷阱**
- 前綴和從 `head` 開始，而不是從值為 0 的虛擬節點開始：`[1,-1]` 應該回傳 `[]`，但 head 之前沒有記錄前綴 0，結果回傳 `[-1]`。
- 每個前綴只記**第一個**節點而不是最後一個：在第二次出現時，跳躍會指回區段的開頭，於是 `[1,-1]` 變成環，第二趟永遠不會結束。

<!-- 71a2a5912d4e -->
## 兩個虛擬節點，先拆再接題目

<!-- 1d7d609be242 -->
### Merge Two Sorted Lists — LC 21

> **關鍵** — 一個虛擬節點、一個尾指標、兩個輸入：把較小的 head 接到尾巴上，
> 並讓那個輸入前進。一個輸入用完時，另一個本來就已排序，
> 所以 `tail.next = l1 or l2` 以 O(1) 收尾。

**歸約到**：[linked_list § Template 3](./linked_list.md#template-3-two-dummies-split-then-join--lc-86-)
— 反過來跑：兩條鏈進、一條鏈出，由比較決定下一個節點由哪個輸入提供。

<!--CODE-->

**陷阱**
- 寫成 `tail = l1`，而不是 `tail.next = l1` 再 `tail = tail.next`：什麼都沒接上，`[1,2,4]` + `[1,3,4]` 回傳 `[]`。
- 把值複製成新節點也行，但多花了題目不需要的 O(m + n) 空間。

**不適用時** — 面試官要遞迴寫法：見
[recursion.md § 4-3](./recursion.md#4-3-merge-two-sorted-lists-lc-21)，O(m + n) 呼叫堆疊。k 條串列見 [LC 23](#index)。

<!-- ab12cbd98993 -->
### Split Linked List in Parts — LC 725

> **關鍵** — n 個節點時，第 i 段有 `n // k` 個節點，`i < n % k` 時再多一個，
> 所以較長的段在前面。從目前的 head 走那麼多個節點，在最後一個之後切開；
> 大小為 0 的段是 `None`。

**歸約到**：[linked_list § Template 3](./linked_list.md#template-3-two-dummies-split-then-join--lc-86-)
— k 條鏈而不是兩條，依數量而不是依條件切，每條都用 `.next = None` 收尾，就像第二條鏈的尾巴一樣。

<!--CODE-->

**陷阱**
- 把多出來的節點分給*最後*幾段：`[1..10]`、k = 3，必須是 `[1,2,3,4]`、`[5,6,7]`、`[8,9,10]`；前面的段比較長。
- 漏掉切斷：每一段都一路連到原串列的結尾。

<!-- ebbbd47b7d4c -->
### Linked List Components — LC 817

> **關鍵** — 一個連通分量是值都在 `nums` 裡的一段最長連續節點。數這些段的**結尾**：
> 一個在集合裡、而下一個節點不存在或不在集合裡的節點，
> 正好結束一個分量。

**歸約到**：[linked_list § Template 3](./linked_list.md#template-3-two-dummies-split-then-join--lc-86-)
— 集合成員判斷把節點切成一段段，就像 Template 3 的條件把節點切成兩條鏈，只是這些段只被計數，從不被建立。

<!--CODE-->

<!--CODE-->

**陷阱**
- 把集合裡的每個節點都算進去：`[0,1,2,3]`、nums = `[0,1,3]`，有 3 個這樣的節點，但只有 2 個分量。
- 用串列而不是集合做成員判斷，每次判斷就變成 O(len(nums))。

| LC | 題目 | 歸約到 | 難度 | 位置 |
|---|---|---|---|---|
| 23 | Merge k Sorted Lists | [heap § Template 3](./heap.md#template-3-k-way-merge--lc-23-) | Hard | 詳寫於 heap.md |

<!-- 49e34d0aa69f -->
## 反轉一段題目

<!-- 5456e2b0ca77 -->
### Swap Nodes in Pairs — LC 24

> **關鍵** — 在 k = 2 時，反轉一段的輔助函式的三個把手變成區域變數：`second` 是
> 新的前端、`first` 是新的尾巴、`second.next` 是之後的節點。`prev` 是唯一的
> 游標：下一對的兩個節點都能從它走到。

**歸約到**：[linked_list § Template 4](./linked_list.md#template-4-reverse-a-segment-reconnect-both-ends--lc-92-)
— k = 2 的情況，展開來寫，因為反轉兩個節點只是兩行賦值。

| 輔助函式回傳 | 在 `k = 2` 時就是 | 實際寫成 |
|---|---|---|
| `new_head` | `second` | `prev.next = second` |
| `new_tail` | `first` | `prev = first` — 下一輪的 `prev` |
| `next_node` | `second.next` | 直接折進 `first.next = second.next` |

<!--CODE-->

**試跑**（`[1,2,3,4]`）

<!--CODE-->

**陷阱**
- 把 `(B)` 放在 `(A)` 之前會先蓋掉 `second.next`，於是 `(A)` 讓 `first` 指向自己：`[1,2,3,4]` 留下 `1 -> 1`，迴圈永遠不會結束。
- 讓 `prev` 移到 `second` 而不是 `first`：交換後 `second` 是這一對的*前端*，所以下一「對」橫跨了兩對，`[1,2,3,4]` 回傳 `[2,3,4,1]`。
- `[]`、`[1]` 或奇數長度的尾巴都不需要防呆：`[1,2,3]` 交換一次，留下 `3`。

**不適用時** — 面試官要遞迴寫法（O(n) 堆疊）：詳寫於
[recursion.md § 5-1](./recursion.md#5-1-recursive-linked-list-rewiring--lc-206--24--25-)。

<!-- ae8870587286 -->
### Flatten a Multilevel Doubly Linked List — LC 430 ⭐⭐⭐⭐

> **關鍵** — 把子鏈接進來就是一次「接回一段」：兩個接縫，`cur <-> child`
> 和 `tail <-> next`，兩個方向都要設。之後繼續往前走：接進來的鏈會是下一個被走訪的，
> 所以更深的層級不用遞迴也不用堆疊就會被攤平。

**歸約到**：[linked_list § Template 4](./linked_list.md#template-4-reverse-a-segment-reconnect-both-ends--lc-92-)
— 接入而不是反轉：那一段（子鏈）整段放進去，只剩兩次接線，而且是在雙向鏈結串列上。

<!--CODE-->

<!--CODE-->

**試跑**

<!--CODE-->

**陷阱**
- 沒把 `cur.child` 清掉：攤平後仍留有 child 指標的串列會被判錯。
- 接縫只設了 `next` 那一側：從 `4` 往回走就會跳過子鏈。
- 最後一個節點有 child：`nxt` 是 `None`，所以 `nxt.prev = tail` 必須先檢查。

**相似題** — LC 114 *（同樣的接入，用在樹上：把左子樹掛在 root 和 right 之間）*

<!-- ff43c9f80f7c -->
### Reverse Nodes in k-Group — LC 25

> **關鍵** — 每一組做完後，這組的舊 head 就是它的新 tail，而那正是下一組的入邊
> 必須掛上去的節點：`prev = new_tail`。反轉之前先數 k 個節點，
> 不足 k 個的最後一組就保持原狀。

**歸約到**：[linked_list § Template 4](./linked_list.md#template-4-reverse-a-segment-reconnect-both-ends--lc-92-)
— 同一個三把手輔助函式，每個滿 k 個的組呼叫一次，而不是只呼叫一次。

<!--CODE-->

**試跑**（`[1,2,3,4,5]`、`k = 2`）

<!--CODE-->

**陷阱**
- 不用 `has_k` 就反轉：輔助函式也會把不足 k 個的最後一組反轉，所以 `[1,2,3,4,5]`、k = 3，回傳 `[3,2,1,5,4]`，而不是 `[3,2,1,4,5]`。
- 設成 `prev = new_head`：下一組被掛在這一組的前端而不是尾巴，`[1,2,3,4,5]`、k = 2，回傳 `[2,3,4,5,1]`。

**不適用時** — 面試官要遞迴寫法（O(n / k) 堆疊）：詳寫於
[recursion.md § 5-1](./recursion.md#5-1-recursive-linked-list-rewiring--lc-206--24--25-)。

| LC | 題目 | 歸約到 | 難度 | 位置 |
|---|---|---|---|---|
| 92 | Reverse Linked List II | [linked_list § Template 4](./linked_list.md#template-4-reverse-a-segment-reconnect-both-ends--lc-92-) | Medium | 錨點題，詳寫於 linked_list.md |

<!-- 3b9393debc9e -->
## 快慢指標找中點題目

這個模板的題目都在別的文件詳寫；列在這裡，是因為讀完 Template 5 的人會到這一節找它們。

| LC | 題目 | 歸約到 | 難度 | 位置 |
|---|---|---|---|---|
| 160 | Intersection of Two Linked Lists | [2_pointers_linkedlist § 1-1-5](./2_pointers_linkedlist.md#1-1-5--intersection-merge-point-of-2-linked-lists) | Easy | 詳寫於 2_pointers_linkedlist.md |
| 234 | Palindrome Linked List | [linked_list § Template 5](./linked_list.md#template-5-fastslow-middle--lc-876-) + Template 1 | Easy | 詳寫於 [2_pointers_linkedlist § 1-1-6](./2_pointers_linkedlist.md#1-1-6--palindrome-linked-list-find-mid---reverse-2nd-half---compare) |
| 142 | Linked List Cycle II | [2_pointers_linkedlist § 1-1-2](./2_pointers_linkedlist.md#1-1-2--return-the-ring-start-point-of-circular-linked-list) | Medium | 詳寫於 2_pointers_linkedlist.md |
| 148 | Sort List | [linked_list § Template 5](./linked_list.md#template-5-fastslow-middle--lc-876-) + LC 21 的合併 | Medium | 詳寫於 [advanced_divide_and_conquer § LC 148](./advanced_divide_and_conquer.md#variation-lc-148--sort-list) |

<!-- 5c4b7659f140 -->
## 進位迴圈題目

<!-- 40b8ee2fb034 -->
### Plus One Linked List — LC 369

> **關鍵** — 進位從最低位開始，而最低位是串列的**尾巴**。
> 先反轉串列，讓進位迴圈能往前走；進位一變成 0 就停，進位撐過最後一位就補一個節點，
> 最後再反轉回來。

**歸約到**：[linked_list § Template 6](./linked_list.md#template-6-carry-loop-over-a-dummy--lc-2-)
— 一個輸入、起始進位為 1，外面包兩次 [Template 1](./linked_list.md#template-1-iterative-reversal--lc-206-)
反轉，因為位數是最高位在前。

<!--CODE-->

<!--CODE-->

**陷阱**
- 漏掉最後的進位：`[9,9]` 必須變成 `[1,0,0]`；沒有補上的節點就是 `[0,0]`。
- 把串列轉成整數在 Python 可行，但在 Java 上長串列會讓 `long` 溢位。

**相似題** — LC 2 *（錨點題：兩個輸入，位數已經是最低位在前）* · LC 445 *（最高位在前，兩個輸入）*

<!-- 9286b665a295 -->
## 雜湊表 + 雙向鏈結串列題目

<!-- 29d38ccc24c5 -->
### Copy List with Random Pointer — LC 138

> **關鍵** — `random` 指標可能指向還沒被複製的節點，所以分兩趟複製：
> 先把每個舊節點對應到一個新副本，再透過對照表接好每個副本的 `next` 和
> `random`。`map.get(None)` 回傳 `None`，所以串列尾端不需要分支。

**歸約到**：[hash_map § Template 8](./hash_map.md#template-8-old-to-new-node-map--lc-133)
— 舊到新的節點對照表，用在一條因多出一個指標而變成圖的串列上。

<!--CODE-->

<!--CODE-->

**陷阱**
- 在第一趟就接 `random`：`random` 可能指向*前方*、還沒有副本的節點。
- 把副本的 `random` 指向舊節點（`cur.random`）而不是它的副本：印出來看起來沒錯，但跟輸入共用節點，會被判錯。

**不適用時** — 追問要求 O(1) 額外空間：把每個副本接在原節點之後（`A -> A' -> B -> B'`），設 `A'.random = A.random.next`，再拆開。

| LC | 題目 | 歸約到 | 難度 | 位置 |
|---|---|---|---|---|
| 146 | LRU Cache | [linked_list § Template 7](./linked_list.md#template-7-hash-map--doubly-linked-list-lru--lc-146-) | Medium | 錨點題，詳寫於 linked_list.md |
| 460 | LFU Cache | [linked_list § Template 7，LFU 變體](./linked_list.md#lfu-variant--one-list-per-frequency-plus-a-min_freq-pointer-lc-460-) | Hard | 詳寫於 linked_list.md |
