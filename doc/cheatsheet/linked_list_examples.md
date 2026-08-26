# Linked List — Worked Examples

> **Scope** — The worked-solution archive behind [linked_list.md](./linked_list.md): one canonical solution per problem per language for the reversal, merging, splitting, copying, flattening and list-sorting problems, grouped by the technique each one exercises.
> **See also**: [linked_list.md](./linked_list.md) — the parent sheet, which owns the dummy-head technique, the reverse-k primitive, the basic operations and the chooser table these solutions rehearse; [2_pointers_linkedlist.md](./2_pointers_linkedlist.md) — cycle detection and the fast/slow family in their own right; [design.md](./design.md) — LRU and the other list-plus-map designs; [heap.md](./heap.md) — the heap-based route through LC 23; [monotonic_stack.md](./monotonic_stack.md) — the theory behind LC 1019; [prefix_sum.md](./prefix_sum.md) — the theory behind LC 1171.

## LeetCode Problem Lists

- [Linked List](https://leetcode.com/problem-list/linked-list/)
- [Doubly-Linked List](https://leetcode.com/problem-list/doubly-linked-list/)

## Overview

This is the long tail of [linked_list.md](./linked_list.md). The parent sheet keeps the
techniques — dummy head, the reverse-k helper, fast/slow, the basic operations — and this file
keeps the problems that *apply* them, so the techniques are not buried under a thousand lines of
solutions.

### Key Properties
- **Complexity**: see the [Time Complexity](./linked_list.md#time-complexity) table in the parent sheet; every solution below is O(n) time and O(1) space unless its own comment says otherwise
- **Core Idea**: each section is a rehearsal of one parent technique — the technique is the thing to memorise, these are the reps
- **When to Use**: after you already know which technique the problem wants, and want to see it written out end to end

### Where a solution deliberately is *not* here

Three problems are worked in the parent sheet instead, because the explanation *is* the lesson
and the code is a by-product of it:

| LC | Problem | Why it lives in the parent |
|---|---|---|
| 206 | Reverse Linked List | it *is* the basic operation — [1-1-1) / 1-1-2)](./linked_list.md#1-1-1-reverse-linked-list-iteration--lc-206) |
| 19 | Remove Nth Node From End | the dummy-node case analysis is the point — [Why Dummy Node?](./linked_list.md#why-dummy-node-visual-comparison-lc-19). Only the two Java forms are below |
| 92 | Reverse Linked List II | the Python form is the reverse-k helper applied once — [Reverse K Nodes Helper Pattern](./linked_list.md#reverse-k-nodes-helper-pattern-). Only the inline Java is below |


## Reversal & Reordering

### 1) Reverse Linked List II — LC 92

> **Core idea**: *locate* the node before position `left`, *reverse* `right - left + 1`
> nodes, then *reconnect* both boundaries. The Python form is the
> [Reverse K Nodes Helper Pattern](./linked_list.md#reverse-k-nodes-helper-pattern-) in the
> parent sheet; the Java below is the same walk written inline, without the helper.

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


> Swap **every two adjacent nodes**, without touching the values — only re-wire the `next` pointers.
> `1 -> 2 -> 3 -> 4`  becomes  `2 -> 1 -> 4 -> 3`

#### **1. Core Idea**

Every swap really involves **3 anchors**, not 2:

```text
prev -> first -> second -> (rest...)
```

- `prev`   — the node **before** the pair (a `dummy` on the first iteration). It owns the incoming link.
- `first`  — the **1st** node of the pair (will become the 2nd).
- `second` — the **2nd** node of the pair (will become the 1st, i.e. the new front).

After the swap the pair is flipped and `prev` points to the new front:

```text
prev -> second -> first -> (rest...)
```

The reason we need `prev` (hence the **dummy head**, see the [Dummy Head Technique](./linked_list.md#dummy-head-technique)) is that **the node in front of the pair must be re-pointed too** — otherwise the previous pair stays glued to the old front (`first`) instead of the new front (`second`).

#### **2. Pattern — how we `reconnect` the nodes**

There are **3 pointers to re-wire**, and **order matters**. Think of it as *"detach from the right, then re-attach leftward"*:

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

**Why this exact order?** Each link overwrites a pointer we still need, so we save it *before* overwriting:

| Step | Link written | Why it must come here |
|------|--------------|------------------------|
| **(A)** `first.next = second.next` | Grab a handle to `rest` **before** step (B) destroys `second.next`. `first` (the future tail) now correctly points past the pair. |
| **(B)** `second.next = first` | Now safe to flip: `second` points back to `first`. The pair is internally reversed. |
| **(C)** `prev.next = second` | Finally hook the front: the node before the pair now points to `second`, the new front. |

> ⚠️ If you did **(C)** or **(B)** *before* **(A)**, you'd overwrite `second.next` and **lose the reference to `rest`** — the tail of the list would be dropped.

#### **Visualization** (`dummy -> 1 -> 2 -> 3 -> 4`, first iteration)

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

Second iteration swaps `(3,4)` the same way, giving `dummy -> 2 -> 1 -> 4 -> 3`; return `dummy.next = 2`.

#### **Full dry run** (`[1, 2, 3, 4]`, every iteration of the loop)

We trace the exact loop below, tracking the 4 pointers (`prev`, `first`, `second`, `head`) and the list after each of the 3 reconnections `(A)(B)(C)`:

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

**Initial state** (after `dummy.next = head`, `prev = dummy`):
```text
dummy -> 1 -> 2 -> 3 -> 4 -> None
 prev   head
```

---

**Iteration 1** — `head=1`, `head.next=2` → enter loop

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
State after iter 1:
```text
dummy -> 2 -> 1 -> 3 -> 4 -> None
              prev head
```

---

**Iteration 2** — `head=3`, `head.next=4` → enter loop

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
State after iter 2:
```text
dummy -> 2 -> 1 -> 4 -> 3 -> None
                   prev head=None
```

---

**Iteration 3** — `head = None` → loop condition `head and head.next` is `False` → **exit**

```text
return dummy.next  =>  2 -> 1 -> 4 -> 3   ✓
```

**Pointer summary table:**

| iter | `first` | `second` | after (A) `first.next=` | after (B) `second.next=` | after (C) `prev.next=` | new `prev` | new `head` |
|------|---------|----------|--------------------------|---------------------------|-------------------------|------------|------------|
| 1 | `1` | `2` | `3` | `1` | `2` (dummy→2) | `1` | `3` |
| 2 | `3` | `4` | `None` | `3` | `4` (1→4) | `3` | `None` |
| — | stop: `head=None` | | | | | | return `dummy.next=2` |

> **Odd-length note** — for `[1, 2, 3]` the loop runs once (swaps `1,2` → `2 -> 1 -> 3`), then `head=3` but `head.next=None`, so the condition fails and the lone tail `3` is left untouched: result `2 -> 1 -> 3`.

> **Equivalent pointer-walk variant** (`head` itself walks on the dummy, using `head.next` / `head.next.next` as the pair). Same 3 reconnections, just addressed relative to `head`:
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

#### **Recursive view** (same reconnection, top-down)

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
The recursion returns the **new front** of each swapped segment, which the caller wires in — exactly the job `prev.next = second` does in the iterative version.

#### **3. Similar LC**

| # | Problem | Relationship to LC 24 |
|---|---------|------------------------|
| 206 | Reverse Linked List | Swap-in-pairs is a **k=2, segment-wise reversal**; 206 reverses the whole list. See [1-1-1](./linked_list.md#1-1-1-reverse-linked-list-iteration--lc-206) |
| 25  | Reverse Nodes in k-Group | **Generalization**: LC 24 is exactly the `k=2` case. Same "reconnect front + internal reverse". See [1-1-4](./linked_list.md#1-1-4-reverse-nodes-in-k-group--linked-list-iteration--lc-25) |
| 92  | Reverse Linked List II | Reverse a **sub-range** `[m, n]`; reuses "hook `prev` to the new front, tail to the rest". See [1)](#1-reverse-linked-list-ii--lc-92) |
| 143 | Reorder List | Interleaves two halves — another "re-wire `next` pointers pairwise" merge. See [2)](#2-reorder-list--lc-143) |
| 1721 | Swapping Nodes in a Linked List | Simpler — usually swap **values**; but node-swap needs the same 3-anchor care |
| 61  | Rotate List | Re-connects a cut point; same pointer-bookkeeping discipline |

## Merging & Splitting

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

## Fast/Slow Pointers & Structure

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

> The Python solution and the full "why a dummy node" walkthrough live in
> [linked_list.md](./linked_list.md#why-dummy-node-visual-comparison-lc-19). Below are the two
> **Java** forms: one pass with fast/slow, and two passes by length.

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

## Copying, Flattening & Components

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


**Pattern**: **In-place splice**. Whenever a node has a `child`, cut the child chain in between `cur` and `cur.next`, fix `prev` pointers on both seams, then keep walking — the spliced-in child will be visited naturally, so nesting is handled without recursion or a stack.

**Key Idea**: Do NOT recurse. Three pointers per splice: `next` (saved successor), `child` (new successor), `tail` (last node of the child chain). Always null out `cur.child` — the problem requires no `child` pointer survives.

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

**Visual Trace**:
```text
1 <-> 2 <-> 3 <-> 4
            |
            7 <-> 8 <-> 9
                  |
                  11 <-> 12

at node 3:  1 <-> 2 <-> 3 <-> 7 <-> 8 <-> 9 <-> 4
at node 8:  1 <-> 2 <-> 3 <-> 7 <-> 8 <-> 11 <-> 12 <-> 9 <-> 4
```

**Similar LC Problems**:
| # | Problem | Key Difference |
|---|---------|----------------|
| 114 | Flatten Binary Tree to Linked List | Same splice, on a tree: hook `left` subtree between `root` and `right` |
| 116 / 117 | Populating Next Right Pointers in Each Node (I / II) | Inverse move — *build* a linked list (`next` chain) per tree level in O(1) space |

---

## Arithmetic & Sorting on a List

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

### 14) Sort List (merge sort on a linked list) — LC 148 ⭐⭐⭐⭐⭐


**Pattern**: The only `O(n log n)` sort that runs in `O(1)` extra data space on a linked list. Three moves: **split at the middle → sort each half recursively → merge two sorted lists** (reuse [4) LC 21](#4-merge-two-sorted-lists--lc-21)).

**Key Idea**: `slow` must stop at the node **before** the middle so we can `slow.next = null` to physically cut the list. Starting `fast = head.next` (not `head`) guarantees the 2-node case `[2,1]` splits into `[2]` + `[1]` instead of `[2,1]` + `[]` (infinite recursion).

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

**Visual Trace** (`4 -> 2 -> 1 -> 3`):
```text
split:   [4,2]        [1,3]
split:   [4] [2]      [1] [3]
merge:   [2,4]        [1,3]
merge:   [1,2,3,4]
```

**Variation — LC 147 Insertion Sort List** (twist: `O(n^2)` but stable and single-pass-friendly; walk a dummy-headed sorted prefix to find each node's slot):
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

**Similar LC Problems**:
| # | Problem | Key Difference |
|---|---------|----------------|
| 21 | Merge Two Sorted Lists | The `merge` step alone. See [4)](#4-merge-two-sorted-lists--lc-21) |
| 23 | Merge k Sorted Lists | Same divide-and-conquer, but on `k` lists. See [5)](#5-merge-k-sorted-lists--lc-23) |
| 147 | Insertion Sort List | `O(n^2)` variation above |
| 109 | Convert Sorted List to BST | Reuses the same "cut at the middle" split, then builds a tree |

---

## Array Techniques Borrowed onto a List

### 15) Prefix Sum + HashMap on a Linked List — LC 1171 ⭐⭐⭐⭐


**Pattern**: The classic array trick "**equal prefix sums ⇒ the segment between them sums to 0**" ported to a linked list. Instead of counting subarrays, you **rewire `next` to jump over** the zero-sum stretch.

**Key Idea**: Two passes over a dummy-headed list.
1. Map `prefixSum -> the LAST node reaching it`.
2. Walk again; at each node set `cur.next = lastSeen[prefix].next`, which deletes everything between the first and last occurrence of that prefix.

Starting from `dummy` (value `0`) is what lets a zero-sum prefix starting at `head` be removed.

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

**Visual Trace** (`1 -> 2 -> -3 -> 3 -> 1`):
```text
node    : dummy  1   2   -3   3   1
prefix  :   0    1   3    0   3   4
                 ^        ^        prefix 0 repeats -> drop [1,2,-3]
                     ^        ^    prefix 3 repeats -> drop [3]
result  : 3 -> 1
```

---

### 16) Monotonic Stack over a Linked List — LC 1019 ⭐⭐⭐


**Pattern**: "Next greater element" needs to look **backwards**, which a singly linked list cannot do. Materialize the values into an array first, then run the standard **monotonic decreasing stack of indices** — see [monotonic_stack.md](./monotonic_stack.md).

**Key Idea**: Push indices, not values. When the incoming value beats `vals[stack.top]`, that index's answer is found — pop and record. Anything still on the stack at the end has no greater node ⇒ `0`.

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

**Visual Trace** (`2 -> 7 -> 4 -> 3 -> 5`):
```text
i=0 v=2  stack=[0]
i=1 v=7  pop 0 (res[0]=7)      stack=[1]
i=2 v=4  stack=[1,2]
i=3 v=3  stack=[1,2,3]
i=4 v=5  pop 3 (res[3]=5), pop 2 (res[2]=5)   stack=[1,4]
leftover 1,4 -> res = [7, 0, 5, 5, 0]
```

> Related: **LC 2487 Remove Nodes From Linked List** (already listed under the *Remove Elements* pattern) is the same monotonic-stack idea used to *delete* nodes instead of reporting them.

---

## Related Problems — Quick Reference


> Fast/slow-pointer techniques (cycle detection, nth-from-end via a gap, palindrome via split+reverse, intersection via head switching, rotate via a k-gap) live in the sibling doc [2_pointers_linkedlist.md](./2_pointers_linkedlist.md) — not duplicated here.

| # | Problem | One-line idea |
|---|---------|----------------|
| 142 | Linked List Cycle II | Floyd's cycle detection, then restart one pointer at `head` to find the entry node — see [2_pointers_linkedlist.md](./2_pointers_linkedlist.md) |
| 2130 | Maximum Twin Sum of a Linked List | Split at the middle + reverse the second half (LC 234 palindrome machinery), then pair up — see [7)](#7-palindrome-linked-list--lc-234) |
| 109 | Convert Sorted List to BST | LC 148's "cut at the middle" split; the mid node becomes the BST root, recurse on both halves |
| 382 | Linked List Random Node | **Reservoir sampling**: keep the `i`-th node with probability `1/i` in one pass — O(1) space, no length needed |
| 707 | Design Linked List | Dummy head + a `size` counter; every op is "walk to index `i-1`, then splice" (see the [Dummy Head Technique](./linked_list.md#dummy-head-technique)) |
| 705 / 706 | Design HashSet / HashMap | **Separate chaining** — an array of buckets, each bucket a linked list scanned linearly |
| 622 | Design Circular Queue | Fixed-size ring; a linked-list version just wraps the tail back to the head |
| 1669 | Merge In Between Linked Lists | Pure splice: walk to node `a-1` and node `b+1`, hook `list2`'s head and tail in between |
