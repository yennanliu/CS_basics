# 鏈結串列 — 範例題解

> **範圍** — [linked_list.md](./linked_list.md) 背後的題解存放處：反轉、合併、切分、複製、攤平與串列排序類題目，每題每語言各一份標準解，並依各自演練的技巧分組。
> **另見**：[linked_list.md](./linked_list.md) — 母文件，擁有虛擬頭節點技巧、reverse-k 基本操作、基本操作與這些解法所演練的選擇表；[2_pointers_linkedlist.md](./2_pointers_linkedlist.md) — 環偵測與快慢指標家族本身；[design.md](./design.md) — LRU 以及其他「串列 + 雜湊表」的設計題；[heap.md](./heap.md) — LC 23 走堆積(heap)的那條路；[monotonic_stack.md](./monotonic_stack.md) — LC 1019 背後的理論；[prefix_sum.md](./prefix_sum.md) — LC 1171 背後的理論。

## LeetCode 題目清單

- [Linked List](https://leetcode.com/problem-list/linked-list/)
- [Doubly-Linked List](https://leetcode.com/problem-list/doubly-linked-list/)

## 總覽

這裡是 [linked_list.md](./linked_list.md) 的長尾。母文件保留技巧本身 —
虛擬頭節點、reverse-k 輔助函式、快慢指標、基本操作 — 而這份檔案收容那些*應用*它們的題目，
免得技巧被上千行的解法給埋沒。

### 關鍵性質
- **複雜度**：見母文件的 [Time Complexity](./linked_list.md#time-complexity) 表格；除非解法自己的註解另有說明，下面每一份解法都是 O(n) 時間、O(1) 空間
- **核心想法**：每一節都是對某個母文件技巧的一次演練 — 技巧才是要背下來的東西，這些只是反覆練習
- **什麼時候用**：當你已經知道這題要用哪個技巧，想看它從頭到尾完整寫一遍時

### 哪些解法刻意*不*放在這裡

有三題改放在母文件裡，因為它們的解說本身*就是*重點，程式碼只是附帶的產物：

| LC | 題目 | 為什麼放在母文件 |
|---|---|---|
| 206 | Reverse Linked List | 它*就是*那個基本操作 — [1-1-1) / 1-1-2)](./linked_list.md#1-1-1-reverse-linked-list-iteration--lc-206) |
| 19 | Remove Nth Node From End | 重點在虛擬節點的分情況討論 — [Why Dummy Node?](./linked_list.md#why-dummy-node-visual-comparison-lc-19)。下面只放兩種 Java 寫法 |
| 92 | Reverse Linked List II | Python 版就是 reverse-k 輔助函式套用一次 — [Reverse K Nodes Helper Pattern](./linked_list.md#reverse-k-nodes-helper-pattern-)。下面只放行內展開的 Java 版 |


## 反轉與重排

### 1) Reverse Linked List II — LC 92

> **核心想法**：*定位*到位置 `left` 前面的那個節點，*反轉* `right - left + 1` 個節點，
> 再把兩端*接回去*。Python 版就是母文件裡的
> [Reverse K Nodes Helper Pattern](./linked_list.md#reverse-k-nodes-helper-pattern-)；
> 下面的 Java 則是同一趟走訪，只是不用輔助函式、直接行內展開。

```java
// java

  // V0-1
  // IDEA: LINKED LIST OP (iteration 1)
  // https://neetcode.io/solutions/reverse-linked-list-ii
  // https://youtu.be/RF_M9tX4Eag?si=vTfAtfbmGwzsmtpi
  public ListNode reverseBetween_0_1(ListNode head, int left, int right) {
      ListNode dummy = new ListNode(0);
      dummy.next = head;
      ListNode leftPrev = dummy, cur = head;

      for (int i = 0; i < left - 1; i++) {
          leftPrev = cur;
          cur = cur.next;
      }

      ListNode prev = null;
      for (int i = 0; i < right - left + 1; i++) {
          ListNode tmpNext = cur.next;
          cur.next = prev;
          prev = cur;
          cur = tmpNext;
      }

      leftPrev.next.next = cur;
      leftPrev.next = prev;

      return dummy.next;
  }
```

### 2) Reorder List — LC 143


```java
// java
    public void reorderList(ListNode head) {
        // Edge case: empty or single node list
        if (head == null || head.next == null) {
            return;
        }

        // Step 1: Find the middle node
        ListNode slow = head, fast = head;
        while (fast != null && fast.next != null) {
            slow = slow.next;
            fast = fast.next.next;
        }

        // Step 2: Reverse the second half of the list
        /** NOTE !!!
         *
         *  reverse on `slow.next` node
         */
        ListNode secondHalf = reverseNode_(slow.next);
        /** NOTE !!!
         *
         *  `cut off` slow node's next nodes via point it to null node
         *  (if not cut off, then in merge step, we will merge duplicated nodes
         */
        slow.next = null; // Break the list into two halves

        // Step 3: Merge two halves
        ListNode firstHalf = head;
        while (secondHalf != null) {

            // NOTE !!! cache `next node` before any op
            ListNode _nextFirstHalf = firstHalf.next;
            ListNode _nextSecondHalf = secondHalf.next;

            // NOTE !!! point first node to second node, then point second node to first node's next node
            firstHalf.next = secondHalf;
            secondHalf.next = _nextFirstHalf;

            // NOTE !!! move both node to `next` node
            firstHalf = _nextFirstHalf;
            secondHalf = _nextSecondHalf;
        }
    }

    // Helper function to reverse a linked list
    private ListNode reverseNode_(ListNode head) {
        ListNode prev = null;
        while (head != null) {
            ListNode next = head.next;
            head.next = prev;
            prev = head;
            head = next;
        }
        return prev;
    }
```

```python
# LC 143. Reorder List
# V0
# IDEA : Reverse the Second Part of the List and Merge Two Sorted Lists
class Solution:
    def reorderList(self, head):
        if not head:
            return 
        
        # find the middle of linked list [Problem 876]
        # in 1->2->3->4->5->6 find 4 
        slow = fast = head
        while fast and fast.next:
            slow = slow.next
            fast = fast.next.next 
            
        # reverse the second part of the list [Problem 206]
        # convert 1->2->3->4->5->6 into 1->2->3->4 and 6->5->4
        # reverse the second half in-place
        prev, curr = None, slow
        while curr:
            tmp = curr.next
            
            curr.next = prev
            prev = curr
            curr = tmp    

        # merge two sorted linked lists [Problem 21]
        # merge 1->2->3->4 and 6->5->4 into 1->6->2->5->3->4
        first, second = head, prev
        while second.next:
            tmp = first.next
            first.next = second
            first = tmp
            
            tmp = second.next
            second.next = first
            second = tmp

# V0'
# IDEA : Reverse the Second Part of the List and Merge Two Sorted Lists (simplified code from V1)
class Solution:
    def reorderList(self, head):
        if not head:
            return 
        
        # find the middle of linked list [Problem 876]
        # in 1->2->3->4->5->6 find 4 
        slow = fast = head
        while fast and fast.next:
            slow = slow.next
            fast = fast.next.next 
            
        # reverse the second part of the list [Problem 206]
        # convert 1->2->3->4->5->6 into 1->2->3->4 and 6->5->4
        # reverse the second half in-place
        prev, curr = None, slow
        while curr:
            curr.next, prev, curr = prev, curr, curr.next       

        # merge two sorted linked lists [Problem 21]
        # merge 1->2->3->4 and 6->5->4 into 1->6->2->5->3->4
        first, second = head, prev
        while second.next:
            first.next, first = second, first.next
            second.next, second = first, second.next

# V0'''
class Solution:
    def reorderList(self, head):
        if head is None:
            return head

        #find mid
        slow = head
        fast = head
        while fast.next and fast.next.next:
            slow = slow.next
            fast = fast.next.next
        mid = slow

        #cut in the mid
        left = head
        right = mid.next
        if right is None:
            return head
        mid.next = None

        #reverse right half
        cursor = right.next
        right.next = None
        while cursor:
            next = cursor.next
            cursor.next = right
            right = cursor
            cursor = next
        
        #merge left and right
        dummy = ListNode(0)
        while left or right:
            if left is not None:
                dummy.next = left
                left = left.next
                dummy = dummy.next
            if right is not None:
                dummy.next = right
                right = right.next
                dummy = dummy.next
        return head
```

### 3) Swap Nodes in Pairs — LC 24


> **兩兩交換相鄰節點**，但不能動到值 — 只能重接 `next` 指標。
> `1 -> 2 -> 3 -> 4`  變成  `2 -> 1 -> 4 -> 3`

#### **1. 核心想法**

每次交換其實牽涉到 **3 個錨點**，而不是 2 個：

```text
prev -> first -> second -> (rest...)
```

- `prev`   — 這一對**前面**的那個節點（第一輪時是 `dummy`）。它掌握著進來的那條連結。
- `first`  — 這一對的**第 1 個**節點（交換後會變成第 2 個）。
- `second` — 這一對的**第 2 個**節點（交換後會變成第 1 個，也就是新的頭）。

交換後這一對翻轉過來，`prev` 指向新的頭：

```text
prev -> second -> first -> (rest...)
```

我們之所以需要 `prev`（因此需要**虛擬頭節點**，見 [Dummy Head Technique](./linked_list.md#dummy-head-technique)），是因為**這一對前面的節點也必須重新指向** — 否則前一對會一直黏在舊的頭（`first`）上，而不是新的頭（`second`）。

#### **2. 模式 — 我們怎麼把節點`接回去`**

有 **3 條連結要重接**，而且**順序很重要**。可以想成*「先從右邊拆開，再往左邊接回去」*：

```python
# LC 24. Swap Nodes in Pairs  (the version we walk through)
# time = O(n), space = O(1)
class Solution(object):
    def swapPairs(self, head):
        if not head or not head.next:
            return head

        dummy = ListNode(0)
        dummy.next = head
        prev = dummy                 # node BEFORE the current pair

        while head and head.next:
            first  = head            # 1st node of the pair
            second = head.next        # 2nd node of the pair

            # ---- reconnect (3 links) ----
            first.next  = second.next  # (A) first jumps OVER second, to the rest
            second.next = first        # (B) second now points back to first  -> pair flipped
            prev.next   = second       # (C) prev adopts second as the new front

            # ---- advance ----
            prev = first             # first is now the tail of this pair -> becomes next `prev`
            head = first.next         # move head to the start of the next pair
        return dummy.next
```

**為什麼一定是這個順序？** 每條連結都會蓋掉某個我們還需要的指標，所以要在覆寫*之前*先存起來：

| 步驟 | 寫入的連結 | 為什麼必須排在這裡 |
|------|--------------|------------------------|
| **(A)** `first.next = second.next` | 在步驟 (B) 摧毀 `second.next` **之前**先抓到 `rest` 的把手。`first`（未來的尾巴）現在正確地指向這一對之後的節點。 |
| **(B)** `second.next = first` | 現在可以安全翻轉了：`second` 指回 `first`。這一對內部完成反轉。 |
| **(C)** `prev.next = second` | 最後把前端勾上：這一對前面的節點現在指向新的頭 `second`。 |

> ⚠️ 如果你在 **(A)** *之前*就做 **(C)** 或 **(B)**，就會覆寫掉 `second.next`，因而**失去對 `rest` 的參照** — 串列的尾巴就整段掉了。

#### **圖解**（`dummy -> 1 -> 2 -> 3 -> 4`，第一輪）

```text
Start:   prev=dummy, first=1, second=2
         dummy -> [1] -> [2] -> 3 -> 4
          prev   first  second  rest=3

(A) first.next = second.next   # 1.next = 3   (1 jumps over 2, onto 3)
         dummy -> [1] --------> 3 -> 4
                  [2] -> 3           (2 still points at 3 for now)
          prev=dummy, second=2 dangling in front

(B) second.next = first        # 2.next = 1   (flip: 2 -> 1)
         dummy    [2] -> [1] -> 3 -> 4
          prev

(C) prev.next = second         # dummy.next = 2   (front adopts new head)
         dummy -> [2] -> [1] -> 3 -> 4   ✓ pair swapped!

Advance: prev = first(1) ;  head = first.next = 3
         dummy -> 2 -> [1] -> [3] -> 4
                       prev  head  ...   → next loop swaps (3,4)
```

第二輪用同樣方式交換 `(3,4)`，得到 `dummy -> 2 -> 1 -> 4 -> 3`；回傳 `dummy.next = 2`。

#### **完整逐步演練**（`[1, 2, 3, 4]`，迴圈的每一輪）

我們照著下面這個迴圈逐字追蹤，追 4 個指標（`prev`、`first`、`second`、`head`），以及每做完 3 次重接 `(A)(B)(C)` 之後的串列狀態：

```python
while head and head.next:
    first  = head
    second = head.next
    first.next  = second.next   # (A)
    second.next = first         # (B)
    prev.next   = second        # (C)
    prev = first                # advance
    head = first.next           # advance
```

**初始狀態**（在 `dummy.next = head`、`prev = dummy` 之後）：
```text
dummy -> 1 -> 2 -> 3 -> 4 -> None
 prev   head
```

---

**第 1 輪** — `head=1`、`head.next=2` → 進入迴圈

```text
cache:  first = 1 ,  second = 2 ,  (second.next = 3 = "rest")

(A) first.next  = second.next   # 1.next = 3
        dummy -> 1 -> 3 -> 4        (2 temporarily off to the side, still 2->3)
(B) second.next = first         # 2.next = 1
        2 -> 1 -> 3 -> 4           (pair flipped internally)
(C) prev.next   = second        # dummy.next = 2
        dummy -> 2 -> 1 -> 3 -> 4  ✓ (1,2) swapped

advance: prev = first  = 1
         head = first.next = 3
```
第 1 輪後的狀態：
```text
dummy -> 2 -> 1 -> 3 -> 4 -> None
              prev head
```

---

**第 2 輪** — `head=3`、`head.next=4` → 進入迴圈

```text
cache:  first = 3 ,  second = 4 ,  (second.next = None = "rest")

(A) first.next  = second.next   # 3.next = None
        ... 1 -> 3 -> None
(B) second.next = first         # 4.next = 3
        4 -> 3 -> None
(C) prev.next   = second        # (prev=1).next = 4
        ... 1 -> 4 -> 3 -> None  ✓ (3,4) swapped

advance: prev = first  = 3
         head = first.next = None
```
第 2 輪後的狀態：
```text
dummy -> 2 -> 1 -> 4 -> 3 -> None
                   prev head=None
```

---

**第 3 輪** — `head = None` → 迴圈條件 `head and head.next` 為 `False` → **結束**

```text
return dummy.next  =>  2 -> 1 -> 4 -> 3   ✓
```

**指標總結表：**

| 輪次 | `first` | `second` | (A) 後 `first.next=` | (B) 後 `second.next=` | (C) 後 `prev.next=` | 新的 `prev` | 新的 `head` |
|------|---------|----------|--------------------------|---------------------------|-------------------------|------------|------------|
| 1 | `1` | `2` | `3` | `1` | `2`（dummy→2） | `1` | `3` |
| 2 | `3` | `4` | `None` | `3` | `4`（1→4） | `3` | `None` |
| — | 停止：`head=None` | | | | | | 回傳 `dummy.next=2` |

> **奇數長度備註** — 以 `[1, 2, 3]` 為例，迴圈只跑一輪（交換 `1,2` → `2 -> 1 -> 3`），接著 `head=3` 但 `head.next=None`，條件不成立，落單的尾巴 `3` 就原封不動留著：結果是 `2 -> 1 -> 3`。

> **等價的指標走訪變體**（讓 `head` 自己停在 dummy 上走，用 `head.next` / `head.next.next` 當作那一對）。同樣是 3 次重接，只是全部改成相對於 `head` 來表達：
```python
# V0' — same idea, `head` acts as `prev`
class Solution:
    def swapPairs(self, head):
        if not head or not head.next:
            return head
        dummy = ListNode(0)
        dummy.next = head
        head = dummy                 # head plays the `prev` role
        while head.next and head.next.next:
            n1, n2 = head.next, head.next.next   # n1=first, n2=second
            n1.next   = n2.next   # (A) first over second
            n2.next   = n1        # (B) flip
            head.next = n2        # (C) prev -> second
            head = n1             # advance prev to tail of swapped pair
        return dummy.next
```

#### **遞迴視角**（同樣的重接，由上而下）

```python
# time = O(n), space = O(n)  (call stack)
class Solution(object):
    def swapPairs(self, head):
        if not head or not head.next:      # base: 0 or 1 node -> nothing to swap
            return head
        first, second = head, head.next
        first.next  = self.swapPairs(second.next)  # (A) first -> swapped rest
        second.next = first                        # (B) flip pair
        return second                              # (C) second is the new head of this segment
```
遞迴回傳的是每一段交換後的**新頭**，再由呼叫端接上去 — 這正是迭代版裡 `prev.next = second` 在做的事。

#### **3. 相似 LC**

| # | 題目 | 與 LC 24 的關係 |
|---|---------|------------------------|
| 206 | Reverse Linked List | 兩兩交換就是 **k=2 的分段反轉**；206 則是反轉整條串列。見 [1-1-1](./linked_list.md#1-1-1-reverse-linked-list-iteration--lc-206) |
| 25  | Reverse Nodes in k-Group | **一般化**：LC 24 恰好是 `k=2` 的情形。同樣是「接好前端 + 內部反轉」。見 [1-1-4](./linked_list.md#1-1-4-reverse-nodes-in-k-group--linked-list-iteration--lc-25) |
| 92  | Reverse Linked List II | 反轉一個**子區間** `[m, n]`；同樣重複使用「把 `prev` 勾到新頭、尾巴接回其餘部分」。見 [1)](#1-reverse-linked-list-ii--lc-92) |
| 143 | Reorder List | 把兩半交錯合併 — 另一種「成對重接 `next` 指標」的合併。見 [2)](#2-reorder-list--lc-143) |
| 1721 | Swapping Nodes in a Linked List | 更簡單 — 通常直接交換**值**；但若要交換節點，就需要同樣的三錨點功夫 |
| 61  | Rotate List | 重接一個切點；同樣需要那套指標記帳的紀律 |

## 合併與切分

### 4) Merge Two Sorted Lists — LC 21

```python
# LC 021
# V0
# IDEA : LOOP 2 LINKED LISTS
class Solution(object):
    def mergeTwoLists(self, l1, l2):
        if not l1 or not l2:
            return l1 or l2
        ### NOTICE THIS
        #   -> we init head, and cur
        #   -> use cur for `link` op
        #   -> and return the `head.next`
        head = cur = ListNode(0)
        while l1 and l2:
            if l1.val < l2.val:
                """
                ### NOTE
                 1) assign node to cur.next !!! (not cur)
                 2) assign node rather than node.val
                """ 
                cur.next = l1
                l1 = l1.next
            else:
                """
                ### NOTE
                 1) assign node to cur.next !!! (not cur)
                 2) assign node rather than node.val
                """ 
                cur.next = l2
                l2 = l2.next
            # note this
            cur = cur.next
        ### NOTE this (in case either l1 or l2 is remaining so we need to append one of them to cur)
        cur.next = l1 or l2
        ### NOTICE THIS : we return head.next
        return head.next
```

### 5) Merge K Sorted Lists — LC 23

```python
# LC 023 Merge k sorted lists
# V0
# IDEA : LC 021 Merge Two Sorted Lists + implement mergeTwoLists on every 2 linedlist
# see 4) Merge Two Sorted Lists above for the mergeTwoLists half of this
class Solution(object):
    def mergeKLists(self, lists):
        if len(lists) == 0:
            return
        if len(lists) == 1:
            return lists[0]
        
        _init_list = lists[0]
        for _list in lists[1:]:
            tmp = self.mergeTwoLists(_init_list, _list)
            _init_list = tmp
        return tmp

    # LC 021 : https://github.com/yennanliu/CS_basics/blob/master/leetcode_python/Linked_list/merge-two-sorted-lists.py
    def mergeTwoLists(self, l1, l2):

        if not l1 or not l2:
            return l1 or l2
            
        res = head = ListNode()
        while l1 and l2:
            if l1.val < l2.val:
                res.next = l1
                l1 = l1.next
            else:
                res.next = l2
                l2 = l2.next
            res = res.next

        if l1 or l2:
            res.next = l1 or l2

        return head.next
```

### 6) Split Linked List in Parts — LC 725

```python
# LC 725. Split Linked List in Parts
# V0
# IDEA : LINKED LIST OP + mod op
class Solution(object):
    def splitListToParts(self, head, k):
        # NO need to deal with edge case !!!
        # get linked list length
        _len = 0
        _head = cur = head
        while _head:
            _len += 1
            _head = _head.next
        # init res
        res = [None] * k
        ### NOTE : we loop over k
        for i in range(k):
            """
            2 cases

            case 1) i < (_len % k) : there is "remainder" ((_len % k)), so we need to add extra 1
                    -> _cnt_elem = (_len // k) + 1
            case 2) i == (_len % k) : there is NO "remainder"
                    -> _cnt_elem = (_len // k)
            """
            # NOTE THIS !!!
            _cnt_elem = (_len // k) + (1 if i < (_len % k) else 0)
            ### NOTE : we loop over _cnt_elem (length of each "split" linkedlist)
            for j in range(_cnt_elem):
                """
                3 cases
                 1) j == 0                (begin of sub linked list)
                 2) j == _cnt_elem - 1    (end of sub linked list)
                 3) 0 < j < _cnt_elem - 1 (middle within sub linked list)
                """
                # NOTE THIS !!!
                # NOTE we need keep if - else in BELOW ORDER !!
                #  -> j == 0, j == _cnt_elem - 1, else
                if j == 0:
                    res[i] = cur
                ### NOTE this !!! : 
                #    -> IF (but not elif)
                #    -> since we also need to deal with j == 0 and j == _cnt_elem - 1 case
                if j == _cnt_elem - 1:  # note this !!!
                    # get next first
                    tmp = cur.next
                    # point cur.next to None
                    cur.next = None
                    # move cur to next (tmp) for op in next i (for i in range(k))
                    cur = tmp
                else:
                    cur = cur.next
        #print ("res = " + str(res))
        return res
```

## 快慢指標與結構

### 7) Palindrome Linked List — LC 234

```python
# LC 234 : palindrome-linked-list
# V0
# IDEA : LINKED LIST -> LIST
# EXAMPLE INPUT :
# [1,2,2,1]
# WHILE GO THROUGH :
# head = ListNode{val: 2, next: ListNode{val: 2, next: ListNode{val: 1, next: None}}}
# head = ListNode{val: 2, next: ListNode{val: 1, next: None}}
# head = ListNode{val: 1, next: None}
class Solution(object):
    def isPalindrome(self, head):
        ### NOTE : THE CONDITION
        if not head or not head.next:
            return True
        r = []
        ### NOTE : THE CONDITION
        while head:
            r.append(head.val)
            head = head.next
        return r == r[::-1]
```

### 8) Intersection of Two Linked Lists — LC 160

```python
# LC 160 Intersection of Two Linked Lists
# V0
# IDEA : if the given 2 linked list have intersection, then 
#        they must overlap in SOMEWHERE if we go through
#        each of them in the same length
#        -> e.g.
#             process1 : headA -> headB -> headA ...
#             process2 : headB -> headA -> headB ...
class Solution(object):
    def getIntersectionNode(self, headA, headB):
        if not headA or not headB:
            return None
        p, q = headA, headB
        while p and q and p != q:
            p = p.next
            q = q.next
            if p == q:
                return p
            if not p:
                p = headB
            if not q:
                q = headA
        return p
```

### 9) Remove Nth Node From End of List — LC 19

> Python 解法與完整的「為什麼要虛擬節點」說明住在
> [linked_list.md](./linked_list.md#why-dummy-node-visual-comparison-lc-19)。下面是兩種
> **Java** 寫法：用快慢指標一趟走完，以及先算長度的兩趟做法。

```java
// java
    public ListNode removeNthFromEnd(ListNode head, int n) {

        if (head == null){
            return head;
        }

        if (head.next == null && head.val == n){
            return null;
        }

        // move fast pointer only with n+1 step
        // 2 cases:
        //   - 1) node count is even
        //   - 2) node count is odd
        /** NOTE !! we init dummy pointer, and let fast, slow pointers point to it */
        ListNode dummy = new ListNode(0);
        dummy.next = head;
        // NOTE here
        ListNode fast = dummy;
        ListNode slow = dummy;
        /**
         *  Explanation V1:
         *
         *   -> So we have fast, and slow pointer,
         *   if we move fast N steps first,
         *   then slow starts to move
         *      -> fast, slow has N step difference
         *      -> what's more, when fast reach the end,
         *      -> fast, slow STILL has N step difference
         *      -> and slow has N step difference with the end,
         *      -> so we can remove N th pointer accordingly
         *
         *  Explanation V2:
         *
         *
         *   // NOTE !!! we let fast pointer move N+1 step first
         *   // so once fast pointers reach the end after fast, slow pointers move together
         *   // we are sure that slow pointer is at N-1 node
         *   // so All we need to do is :
         *   // point slow.next to slow.next.next
         *   // then we remove N node from linked list
         */
        for (int i = 1; i <= n+1; i++){
            //System.out.println("i = " + i);
            fast = fast.next;
        }

        // move fast and slow pointers on the same time
        while (fast != null){
            fast = fast.next;
            slow = slow.next;
        }

        // NOTE here
        slow.next = slow.next.next;
        // NOTE !!! we return dummy.next instead of slow
        return dummy.next;
    }
```

```java
// java
    // V0
    // IDEA : get len of linkedlist, and re-point node
    public ListNode removeNthFromEnd_0(ListNode head, int n) {

        if (head.next == null){
            return null;
        }

        // below op is optional
//        if (head.next.next == null){
//            if (n == 1){
//                return new ListNode(head.val);
//            }
//            return new ListNode(head.next.val);
//        }

        // get len
        int len = 0;
        ListNode head_ = head;
        while (head_ != null){
            head_ = head_.next;
            len += 1;
        }

        ListNode root = new ListNode();
        /** NOTE !!! root_ is the actual final result */
        ListNode root_ = root;

        // if n == len
        if (n == len){
            head = head.next;
            root.next = head;
            root = root.next;
        }

        /**
         *  IDEA: get length of linked list,
         *        then if want to delete n node from the end of linked list,
         *        -> then we need to stop at "len - n" idx,
         *        -> and reconnect "len - n" idx to "len -n + 2" idx
         *        -> (which equals delete "n" idx node
         *
         *
         *  Consider linked list below :
         *
         *   0, 1, 2 , 3, 4 .... k-2, k-1, k
         *
         *   if n = 1, then "k-1" is the node to be removed.
         *   -> so we find "k-2" node, and re-connect it to "k" node
         */
        /** NOTE !!!
         *
         *  idx is the index, that we "stop",  and re-connect
         *  from idx to its next next node (which is the actual "delete" node op
         */
        int idx = len - n; // NOTE !!! this
        while (idx > 0){
            root.next = head;
            root = root.next;
            head = head.next;
            idx -= 1;
        }

        ListNode next = head.next;
        root.next = next;

        return root_.next;
    }
```

## 複製、攤平與連通元件

### 10) Copy List with Random Pointer — LC 138

```python
# LC 138. Copy List with Random Pointer
# V0
# IDEA : 
#   step 1) make 2 objects (m, n) refer to same instance (head)
#   step 2) go through m, and set up the dict
#   step 3) go through n, and get the random pointer via the dict we set up in step 2)
class Node(object):
    def __init__(self, val, next, random):
        self.val = val
        self.next = next
        self.random = random

class Solution:
    def copyRandomList(self, head):
        dic = dict()
        ### NOTE : make m, and n refer to same instance (head)
        m = n = head
        while m:
            ### NOTE : the value in dict is Node type (LinkedList)
            dic[m] = Node(m.val)
            m = m.next
        while n:
            dic[n].next = dic.get(n.next)
            dic[n].random = dic.get(n.random)
            n = n.next
        return dic.get(head)
```

```java
// java
// NOTE : there is also recursive solution
// LC 138
// V2
// IDEA :  Iterative with O(N) Space
// https://leetcode.com/problems/copy-list-with-random-pointer/editorial/
// Visited dictionary to hold old node reference as "key" and new node reference as the "value"
HashMap<Node, Node> visited = new HashMap<Node, Node>();

public Node getClonedNode(Node node) {
    // If the node exists then
    if (node != null) {
        // Check if the node is in the visited dictionary
        if (this.visited.containsKey(node)) {
            // If its in the visited dictionary then return the new node reference from the dictionary
            return this.visited.get(node);
        } else {
            // Otherwise create a new node, add to the dictionary and return it
            this.visited.put(node, new Node(node.val, null, null));
            return this.visited.get(node);
        }
    }
    return null;
}

public Node copyRandomList_3(Node head) {

    if (head == null) {
        return null;
    }

    Node oldNode = head;

    // Creating the new head node.
    Node newNode = new Node(oldNode.val);
    this.visited.put(oldNode, newNode);

    // Iterate on the linked list until all nodes are cloned.
    while (oldNode != null) {
        // Get the clones of the nodes referenced by random and next pointers.
        newNode.random = this.getClonedNode(oldNode.random);
        newNode.next = this.getClonedNode(oldNode.next);

        // Move one step ahead in the linked list.
        oldNode = oldNode.next;
        newNode = newNode.next;
    }
    return this.visited.get(head);
}
```

### 11) Linked List Components — LC 817


```java
// java
// LC 817
    // V1
    // IDEA: set, linkedlist (gpt)
    public int numComponents_1(ListNode head, int[] nums) {
        // Convert nums array to a HashSet for O(1) lookups
        Set<Integer> numsSet = new HashSet<>();
        for (int num : nums) {
            numsSet.add(num);
        }

        int count = 0;
        boolean inComponent = false;

        // Traverse the linked list
        while (head != null) {
            if (numsSet.contains(head.val)) {
                // Start a new component if not already in one
                if (!inComponent) {
                    count++;
                    inComponent = true;
                }
            } else {
                // End the current component
                inComponent = false;
            }
            head = head.next;
        }

        return count;
    }
```

### 12) Flatten a Multilevel Doubly Linked List — LC 430 ⭐⭐⭐⭐


**模式**：**就地接合（in-place splice）**。只要某個節點有 `child`，就把整條子鏈接到 `cur` 和 `cur.next` 之間，把兩個接縫上的 `prev` 指標補好，然後繼續往前走 — 接進來的子鏈自然會被走到，所以不用遞迴或堆疊就能處理巢狀。

**關鍵想法**：不要遞迴。每次接合需要三個指標：`next`（存起來的後繼）、`child`（新的後繼）、`tail`（子鏈的最後一個節點）。務必把 `cur.child` 設成 null — 題目要求最後不能有任何 `child` 指標殘留。

```java
// java
// LC 430 - Flatten a Multilevel Doubly Linked List
// IDEA: IN-PLACE SPLICE — insert the child chain between `cur` and `cur.next`
// time = O(n), space = O(1)   (each chain is tail-scanned exactly once)
public Node flatten(Node head) {
    Node cur = head;
    while (cur != null) {
        if (cur.child != null) {
            Node next  = cur.next;          // save the successor
            Node child = cur.child;
            cur.child = null;               // MUST clear the child pointer

            // seam 1: cur <-> child
            cur.next   = child;
            child.prev = cur;

            // find the child chain's tail
            Node tail = child;
            while (tail.next != null) tail = tail.next;

            // seam 2: tail <-> next
            tail.next = next;
            if (next != null) next.prev = tail;
        }
        cur = cur.next;                     // walks INTO the spliced child
    }
    return head;
}
```

```python
# python
# LC 430 - Flatten a Multilevel Doubly Linked List
# IDEA: IN-PLACE SPLICE — insert the child chain between `cur` and `cur.next`
# time = O(n), space = O(1)   (each chain is tail-scanned exactly once)
def flatten(self, head):
    cur = head
    while cur:
        if cur.child:
            nxt   = cur.next                # save the successor
            child = cur.child
            cur.child = None                # MUST clear the child pointer

            # seam 1: cur <-> child
            cur.next   = child
            child.prev = cur

            # find the child chain's tail
            tail = child
            while tail.next:
                tail = tail.next

            # seam 2: tail <-> nxt
            tail.next = nxt
            if nxt:
                nxt.prev = tail
        cur = cur.next                      # walks INTO the spliced child
    return head
```

**視覺追蹤**：
```text
1 <-> 2 <-> 3 <-> 4
            |
            7 <-> 8 <-> 9
                  |
                  11 <-> 12

at node 3:  1 <-> 2 <-> 3 <-> 7 <-> 8 <-> 9 <-> 4
at node 8:  1 <-> 2 <-> 3 <-> 7 <-> 8 <-> 11 <-> 12 <-> 9 <-> 4
```

**相似的 LC 題目**：
| # | 題目 | 關鍵差異 |
|---|---------|----------------|
| 114 | Flatten Binary Tree to Linked List | 同樣的接合，只是在樹上：把 `left` 子樹掛到 `root` 和 `right` 之間 |
| 116 / 117 | Populating Next Right Pointers in Each Node (I / II) | 反向操作 — 用 O(1) 空間為樹的每一層*建出*一條鏈結串列（`next` 鏈） |

---

## 串列上的算術與排序

### 13) Plus One Linked List — LC 369

```java
// java
// LC 369
// V1
// IDEA : LINKED LIST OP (gpt)
/**
*  Step 1) reverse linked list
*  Step 2) plus 1, bring `carry` to next digit if curSum > 9, ... repeat for all nodes
*  Step 3) reverse linked list again
*/
public ListNode plusOne_1(ListNode head) {
if (head == null) return new ListNode(1); // Handle edge case

// Reverse the linked list
head = reverseList(head);

// Add one to the reversed list
ListNode current = head;
int carry = 1; // Start with adding one

while (current != null && carry > 0) {
  int sum = current.val + carry;
  current.val = sum % 10; // Update the current node value
  carry = sum / 10; // Calculate carry for the next node
  if (current.next == null && carry > 0) {
    current.next = new ListNode(carry); // Add a new node for carry
    carry = 0; // No more carry after this
  }
  current = current.next;
}

// Reverse the list back to original order
return reverseList(head);
}

// Utility to reverse a linked list
private ListNode reverseList(ListNode head) {
ListNode prev = null;
ListNode current = head;

while (current != null) {
  ListNode next = current.next; // Save the next node
  current.next = prev; // Reverse the link
  prev = current; // Move prev forward
  current = next; // Move current forward
}

return prev;
}
```

### 14) Sort List（在鏈結串列上做合併排序） — LC 148 ⭐⭐⭐⭐⭐


**模式**：唯一能在鏈結串列上以 `O(1)` 額外資料空間跑出 `O(n log n)` 的排序。三個動作：**從中間切開 → 遞迴排序兩半 → 合併兩條已排序的串列**（重用 [4) LC 21](#4-merge-two-sorted-lists--lc-21)）。

**關鍵想法**：`slow` 必須停在中點**前面**那個節點，我們才能用 `slow.next = null` 把串列實體切開。把 `fast` 從 `head.next`（而非 `head`）開始，可保證兩節點的情形 `[2,1]` 會切成 `[2]` + `[1]`，而不是 `[2,1]` + `[]`（那會無窮遞迴）。

```java
// java
// LC 148 - Sort List
// IDEA: MERGE SORT — split at middle (slow/fast) -> sort halves -> merge
// time = O(n log n), space = O(log n) (recursion stack)
public ListNode sortList(ListNode head) {
    if (head == null || head.next == null) return head;

    // 1) split: `slow` stops at the node BEFORE the middle
    ListNode slow = head, fast = head.next;
    while (fast != null && fast.next != null) {
        slow = slow.next;
        fast = fast.next.next;
    }
    ListNode mid = slow.next;
    slow.next = null;                       // cut into two halves

    // 2) sort each half
    ListNode l = sortList(head), r = sortList(mid);

    // 3) merge (LC 21)
    return mergeTwo(l, r);
}

private ListNode mergeTwo(ListNode a, ListNode b) {
    ListNode dummy = new ListNode(0), cur = dummy;
    while (a != null && b != null) {
        if (a.val <= b.val) { cur.next = a; a = a.next; }
        else                { cur.next = b; b = b.next; }
        cur = cur.next;
    }
    cur.next = (a != null) ? a : b;         // attach the leftover tail
    return dummy.next;
}
```

```python
# python
# LC 148 - Sort List
# IDEA: MERGE SORT — split at middle (slow/fast) -> sort halves -> merge
# time = O(n log n), space = O(log n) (recursion stack)
def sortList(self, head):
    if not head or not head.next:
        return head

    # 1) split: `slow` stops at the node BEFORE the middle
    slow, fast = head, head.next
    while fast and fast.next:
        slow = slow.next
        fast = fast.next.next
    mid = slow.next
    slow.next = None                        # cut into two halves

    # 2) sort each half
    left, right = self.sortList(head), self.sortList(mid)

    # 3) merge (LC 21)
    return self.merge_two(left, right)

def merge_two(self, a, b):
    dummy = ListNode(0)
    cur = dummy
    while a and b:
        if a.val <= b.val:
            cur.next, a = a, a.next
        else:
            cur.next, b = b, b.next
        cur = cur.next
    cur.next = a if a else b                # attach the leftover tail
    return dummy.next
```

**視覺追蹤**（`4 -> 2 -> 1 -> 3`）：
```text
split:   [4,2]        [1,3]
split:   [4] [2]      [1] [3]
merge:   [2,4]        [1,3]
merge:   [1,2,3,4]
```

**變體 — LC 147 Insertion Sort List**（巧思：`O(n^2)`，但穩定且適合單趟處理；沿著帶虛擬頭的已排序前綴走，找出每個節點該插入的位置）：
```java
// java
// LC 147 - Insertion Sort List
// time = O(n^2), space = O(1)
public ListNode insertionSortList(ListNode head) {
    ListNode dummy = new ListNode(0);
    ListNode cur = head;
    while (cur != null) {
        ListNode next = cur.next;           // detach `cur` first
        ListNode p = dummy;
        while (p.next != null && p.next.val < cur.val) p = p.next;
        cur.next = p.next;                  // splice `cur` after `p`
        p.next = cur;
        cur = next;
    }
    return dummy.next;
}
```
```python
# python
# LC 147 - Insertion Sort List
# time = O(n^2), space = O(1)
def insertionSortList(self, head):
    dummy = ListNode(0)
    cur = head
    while cur:
        nxt = cur.next                      # detach `cur` first
        p = dummy
        while p.next and p.next.val < cur.val:
            p = p.next
        cur.next = p.next                   # splice `cur` after `p`
        p.next = cur
        cur = nxt
    return dummy.next
```

**相似的 LC 題目**：
| # | 題目 | 關鍵差異 |
|---|---------|----------------|
| 21 | Merge Two Sorted Lists | 只有 `merge` 那一步。見 [4)](#4-merge-two-sorted-lists--lc-21) |
| 23 | Merge k Sorted Lists | 同樣的分治，只是對 `k` 條串列做。見 [5)](#5-merge-k-sorted-lists--lc-23) |
| 147 | Insertion Sort List | 上面那個 `O(n^2)` 的變體 |
| 109 | Convert Sorted List to BST | 重用同一套「從中間切開」的切分，然後建出一棵樹 |

---

## 借用到串列上的陣列技巧

### 15) 鏈結串列上的前綴和 + 雜湊表 — LC 1171 ⭐⭐⭐⭐


**模式**：把經典的陣列技巧「**前綴和相等 ⇒ 兩者之間那一段總和為 0**」移植到鏈結串列上。差別在於不是去數子陣列，而是**把 `next` 重接、直接跳過**那段總和為零的區段。

**關鍵想法**：在帶虛擬頭的串列上走兩趟。
1. 建立 `prefixSum -> 到達該前綴和的最後一個節點` 的對照表。
2. 再走一趟；在每個節點設 `cur.next = lastSeen[prefix].next`，這會刪掉該前綴和第一次與最後一次出現之間的所有東西。

從 `dummy`（值為 `0`）開始，正是讓「從 `head` 起算就總和為零」的前綴也能被刪掉的原因。

```java
// java
// LC 1171 - Remove Zero Sum Consecutive Nodes from Linked List
// IDEA: PREFIX SUM + HASHMAP — same prefix twice => the nodes in between sum to 0
// time = O(n), space = O(n)
public ListNode removeZeroSumSublists(ListNode head) {
    ListNode dummy = new ListNode(0);
    dummy.next = head;

    // pass 1: remember the LAST node achieving each prefix sum
    Map<Integer, ListNode> lastSeen = new HashMap<>();
    int prefix = 0;
    for (ListNode cur = dummy; cur != null; cur = cur.next) {
        prefix += cur.val;
        lastSeen.put(prefix, cur);          // overwrite -> keeps the last one
    }

    // pass 2: jump from the FIRST node with prefix p to the LAST node with prefix p
    prefix = 0;
    for (ListNode cur = dummy; cur != null; cur = cur.next) {
        prefix += cur.val;
        cur.next = lastSeen.get(prefix).next;
    }
    return dummy.next;
}
```

```python
# python
# LC 1171 - Remove Zero Sum Consecutive Nodes from Linked List
# IDEA: PREFIX SUM + HASHMAP — same prefix twice => the nodes in between sum to 0
# time = O(n), space = O(n)
def removeZeroSumSublists(self, head):
    dummy = ListNode(0, head)

    # pass 1: remember the LAST node achieving each prefix sum
    last_seen = {}
    prefix, cur = 0, dummy
    while cur:
        prefix += cur.val
        last_seen[prefix] = cur             # overwrite -> keeps the last one
        cur = cur.next

    # pass 2: jump from the FIRST node with prefix p to the LAST node with prefix p
    prefix, cur = 0, dummy
    while cur:
        prefix += cur.val
        cur.next = last_seen[prefix].next
        cur = cur.next
    return dummy.next
```

**視覺追蹤**（`1 -> 2 -> -3 -> 3 -> 1`）：
```text
node    : dummy  1   2   -3   3   1
prefix  :   0    1   3    0   3   4
                 ^        ^        prefix 0 repeats -> drop [1,2,-3]
                     ^        ^    prefix 3 repeats -> drop [3]
result  : 3 -> 1
```

---

### 16) 鏈結串列上的單調堆疊 — LC 1019 ⭐⭐⭐


**模式**：「下一個更大元素」需要**往回看**，而單向鏈結串列做不到。先把值實體化成陣列，再跑標準的**索引遞減單調堆疊** — 見 [monotonic_stack.md](./monotonic_stack.md)。

**關鍵想法**：堆疊裡放索引，不是值。當進來的值大過 `vals[stack.top]` 時，那個索引的答案就找到了 — pop 出來並記錄。最後仍留在堆疊上的，代表沒有更大的節點 ⇒ `0`。

```java
// java
// LC 1019 - Next Greater Node In Linked List
// IDEA: dump list -> array, then MONOTONIC DECREASING STACK of indices
// time = O(n), space = O(n)
public int[] nextLargerNodes(ListNode head) {
    List<Integer> vals = new ArrayList<>();
    for (ListNode cur = head; cur != null; cur = cur.next) vals.add(cur.val);

    int n = vals.size();
    int[] res = new int[n];                 // default 0 = "no greater node"
    Deque<Integer> stack = new ArrayDeque<>();   // indices, values decreasing
    for (int i = 0; i < n; i++) {
        while (!stack.isEmpty() && vals.get(stack.peek()) < vals.get(i)) {
            res[stack.pop()] = vals.get(i);
        }
        stack.push(i);
    }
    return res;
}
```

```python
# python
# LC 1019 - Next Greater Node In Linked List
# IDEA: dump list -> array, then MONOTONIC DECREASING STACK of indices
# time = O(n), space = O(n)
def nextLargerNodes(self, head):
    vals, cur = [], head
    while cur:
        vals.append(cur.val)
        cur = cur.next

    res = [0] * len(vals)                   # default 0 = "no greater node"
    stack = []                              # indices, values decreasing
    for i, v in enumerate(vals):
        while stack and vals[stack[-1]] < v:
            res[stack.pop()] = v
        stack.append(i)
    return res
```

**視覺追蹤**（`2 -> 7 -> 4 -> 3 -> 5`）：
```text
i=0 v=2  stack=[0]
i=1 v=7  pop 0 (res[0]=7)      stack=[1]
i=2 v=4  stack=[1,2]
i=3 v=3  stack=[1,2,3]
i=4 v=5  pop 3 (res[3]=5), pop 2 (res[2]=5)   stack=[1,4]
leftover 1,4 -> res = [7, 0, 5, 5, 0]
```

> 相關：**LC 2487 Remove Nodes From Linked List**（已列在 *Remove Elements* 模式底下）用的是同一套單調堆疊想法，只是拿來*刪除*節點，而不是回報節點。

---

## 相關題目 — 速查


> 快慢指標類技巧（環偵測、用間距找倒數第 n 個、切半 + 反轉判回文、換頭求交點、用 k 間距做旋轉）住在姊妹文件 [2_pointers_linkedlist.md](./2_pointers_linkedlist.md) — 這裡不重複。

| # | 題目 | 一句話想法 |
|---|---------|----------------|
| 142 | Linked List Cycle II | Floyd 環偵測，接著把其中一個指標拉回 `head` 重新走，找出入環節點 — 見 [2_pointers_linkedlist.md](./2_pointers_linkedlist.md) |
| 2130 | Maximum Twin Sum of a Linked List | 從中間切開 + 反轉後半（LC 234 那套回文機制），再兩兩配對 — 見 [7)](#7-palindrome-linked-list--lc-234) |
| 109 | Convert Sorted List to BST | LC 148 的「從中間切開」；中間節點成為 BST 的根，再對兩半遞迴 |
| 382 | Linked List Random Node | **蓄水池抽樣**：一趟走完，以 `1/i` 的機率保留第 `i` 個節點 — O(1) 空間，也不需要先知道長度 |
| 707 | Design Linked List | 虛擬頭 + 一個 `size` 計數器；每個操作都是「走到索引 `i-1`，然後接合」（見 [Dummy Head Technique](./linked_list.md#dummy-head-technique)） |
| 705 / 706 | Design HashSet / HashMap | **鏈結法（separate chaining）** — 一個桶陣列，每個桶是一條線性掃描的鏈結串列 |
| 622 | Design Circular Queue | 固定大小的環；鏈結串列版本就是把尾巴接回頭 |
| 1669 | Merge In Between Linked Lists | 純粹的接合：走到節點 `a-1` 與節點 `b+1`，把 `list2` 的頭尾勾在中間 |
