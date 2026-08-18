# Two pointers - Linkedlist

> **Scope** — Two pointers over **linked-list nodes** — fast/slow cycle detection, middle finding, k-th from end, and offset pointers. No array indexing.
> **See also**: [2_pointers.md](./2_pointers.md) — the array/string form; [linked_list.md](./linked_list.md) — the full linked-list catalogue including reversal and merging.

- Ref
    - [fucking-algorithm : 2 pointers Linkedlist](https://labuladong.online/algo/essential-technique/linked-list-skills-summary/)

## LeetCode Problem Lists

- [Two Pointers](https://leetcode.com/problem-list/two-pointers/)
- [Linked List](https://leetcode.com/problem-list/linked-list/)

### 0-1) Types

- Pointer types
    - `Fast - Slow pointers`
        - fast, slow pointers from `same start point`
        - Usually set
            - slow pointer moves 1 idx
            - fast pointer moves 2 idx
        - linked list
            - find mid point of linked list
            - check if linked list is circular
                - LC 141
                - LC 142
            - if a circular linked list, return beginning point of circular
            - find last k elements of a single linked list
                - LC 19 : Remove Nth Node From End of List

## 1) General form

### 1-1) Basic OP ⭐⭐⭐⭐⭐

#### 1-1-1 : Check if there is a circular linked list 
```java
// java
boolean hasCycle(ListNode head){
    fast = slow = head;
    // NOTE : while loop condition
    while (fast != null && fast.next != null){
        /** NOTE : need to do move slow, fast pointer then compare them */
        slow = slow.next;
        fast = fast.next.next;
        if (fast == slow){
            return true;
        }
    }
    return false;
}
```

#### 1-1-2 : return the "ring start point" of circular linked list 
```java
// java
// LC 141
ListNode detectCycle(ListNode head){
    ListNode fast, slow;
    fast = slow = head;
    while (fast != null && fast.next != null){
        /** NOTE !!! We move pointers first */
        fast = fast.next.next;
        slow = slow.next;
        if (fast == slow){
            break;
        }
    }
    slow = head;
    // may need below logic to check whether is cycle linked list or not
    // if (! fast or ! fast.next){
    //     return null;
    // }
    while (slow != fast){
        slow = slow.next;
        fast = fast.next;
    }
    return slow;
}
```

```python
# LC 142. Linked List Cycle II
# python
class Solution:
    def detectCycle(self, head):
        if not head or not head.next:
            return
        slow = fast = head
        while fast and fast.next:
            fast = fast.next.next
            slow = slow.next
            if fast == slow:
                break
        #print ("slow = " + str(slow) + " fast = " + str(fast))
        ### NOTE : via below condition check if is a cycle linked list
        if not fast or not fast.next:
            return
        """
        ### NOTE : re-init slow or fast as head (from starting point)
        -> can init slow or head
        """
        slow = head
        #fast = head 
        """
        ### NOTE : check while slow != fast
        ### NOTE : use the same speed
        """
        while slow != fast:
            # NOTE this !!! : fast, slow move SAME speed (in this step)
            fast = fast.next
            slow = slow.next
        return slow

# V0'
# IDEA : SET
class Solution(object):
    def detectCycle(self, head):
        if not head or not head.next:
            return
        s = set()
        while head:
            s.add(head)
            head = head.next
            if head in s:
                return head
        return
```
#### 1-1-2-1 : Variation — Floyd cycle detection on an `array` (implicit linked list)

> Twist : there is no `ListNode`. Treat `i -> nums[i]` as the "next" edge, then the **exact same 2-phase Floyd** (meet, then re-init one pointer to start & walk at same speed) returns the cycle entrance = the duplicated value.

```java
// java
// LC 287 - Find the Duplicate Number
// IDEA: index -> nums[index] forms a linked list; the duplicate value is the cycle ENTRANCE
// time = O(n), space = O(1)
class Solution {
    public int findDuplicate(int[] nums) {
        /** NOTE !!! use do-while, since slow == fast at the very beginning */
        int slow = nums[0], fast = nums[0];
        do {
            slow = nums[slow];          // 1 step
            fast = nums[nums[fast]];    // 2 steps
        } while (slow != fast);

        /** NOTE !!! phase 2 : re-init slow to start, both move 1 step */
        slow = nums[0];
        while (slow != fast) {
            slow = nums[slow];
            fast = nums[fast];
        }
        return slow;
    }
}
```

```python
# python
# LC 287 - Find the Duplicate Number
# IDEA: index -> nums[index] forms a linked list; the duplicate value is the cycle ENTRANCE
# time = O(n), space = O(1)
class Solution(object):
    def findDuplicate(self, nums):
        slow = fast = nums[0]
        # NOTE : while-True (not while slow != fast), since slow == fast at start
        while True:
            slow = nums[slow]
            fast = nums[nums[fast]]
            if slow == fast:
                break
        # NOTE : re-init slow as nums[0], move SAME speed
        slow = nums[0]
        while slow != fast:
            slow = nums[slow]
            fast = nums[fast]
        return slow
```

- Same trick works on any `x -> f(x)` sequence, e.g. `LC 202 Happy Number` (`f(x)` = sum of squares of digits) : if the "fast/slow" meet at a value != 1, the sequence loops forever.

#### 1-1-3 : find mid point of a single linked list
```java
// java
while (fast != null and fast.next != null){
    fast = fast.next.next;
    slow = slow.next;
}
return slow;
```

#### 1-1-4 : find last k elements in a single linked list
```java
// java
ListNode fast, slow;
slow = fast = head;
while (k > 0){
    fast = fast.next;
    k -= 1;
}
while (fast != null){
    fast = fast.next;
    slow = slow.next;
}
return slow;
```

#### 1-1-4-1 : Variation — rotate list right by k (`gap pointer` + close the ring)

> Twist : same `k-gap` idea as 1-1-4, but you first need `k %= len` (k can be huge), and after finding the new tail you **link old tail -> old head** and cut.

```java
// java
// LC 61 - Rotate List
// IDEA: gap-k 2 pointers -> slow stops at the NEW TAIL (the (n-k)th node), then re-link
// time = O(n), space = O(1)
class Solution {
    public ListNode rotateRight(ListNode head, int k) {
        if (head == null || head.next == null || k == 0) return head;

        /** NOTE !!! get length AND the tail in one pass */
        int n = 1;
        ListNode tail = head;
        while (tail.next != null) { tail = tail.next; n++; }

        /** NOTE !!! k can be > n */
        k %= n;
        if (k == 0) return head;

        // build the k gap
        ListNode fast = head;
        for (int i = 0; i < k; i++) fast = fast.next;

        // move together -> slow lands on the new tail
        ListNode slow = head;
        while (fast.next != null) { fast = fast.next; slow = slow.next; }

        ListNode newHead = slow.next;
        slow.next = null;    // cut
        tail.next = head;    // close the ring
        return newHead;
    }
}
```

```python
# python
# LC 61 - Rotate List
# IDEA: gap-k 2 pointers -> slow stops at the NEW TAIL (the (n-k)th node), then re-link
# time = O(n), space = O(1)
class Solution(object):
    def rotateRight(self, head, k):
        if not head or not head.next or k == 0:
            return head
        n, tail = 1, head
        while tail.next:
            tail = tail.next
            n += 1
        # NOTE : k may be bigger than list length
        k %= n
        if k == 0:
            return head
        fast = head
        for _ in range(k):
            fast = fast.next
        slow = head
        while fast.next:
            fast = fast.next
            slow = slow.next
        new_head = slow.next
        slow.next = None   # cut
        tail.next = head   # close the ring
        return new_head
```

#### 1-1-5 : intersection (merge point) of 2 linked lists

**Key Idea** : let `p1` walk `A` then `B`, and `p2` walk `B` then `A`. Both travel exactly `m + n` steps, so the **length difference is cancelled** and they arrive at the intersection node at the same time. If there is no intersection, both become `null` at step `m + n` -> loop ends and returns `null`.

```text
A:      a1 -> a2 ->
                     c1 -> c2 -> c3
B: b1 -> b2 -> b3 ->

p1 path : a1 a2 c1 c2 c3 | b1 b2 b3 c1  <- meet
p2 path : b1 b2 b3 c1 c2 c3 | a1 a2 c1  <- meet
```

```java
// java
// LC 160 - Intersection of Two Linked Lists
// IDEA: 2 pointers, switch to the OTHER head on reaching null -> both walk (m+n) steps
// time = O(m+n), space = O(1)
class Solution {
    public ListNode getIntersectionNode(ListNode headA, ListNode headB) {
        if (headA == null || headB == null) return null;
        ListNode p1 = headA, p2 = headB;
        /** NOTE !!! compare NODE identity (not val) */
        while (p1 != p2) {
            // NOTE !!! switch head when hitting null (NOT when hitting the last node)
            p1 = (p1 == null) ? headB : p1.next;
            p2 = (p2 == null) ? headA : p2.next;
        }
        return p1;   // either the intersection node, or null
    }
}
```

```python
# python
# LC 160 - Intersection of Two Linked Lists
# IDEA: 2 pointers, switch to the OTHER head on reaching null -> both walk (m+n) steps
# time = O(m+n), space = O(1)
class Solution(object):
    def getIntersectionNode(self, headA, headB):
        if not headA or not headB:
            return None
        p1, p2 = headA, headB
        while p1 != p2:
            # NOTE : switch to the other head when reaching None
            p1 = headB if p1 is None else p1.next
            p2 = headA if p2 is None else p2.next
        return p1
```

#### 1-1-6 : palindrome linked list (find mid -> reverse 2nd half -> compare)

**Pattern** : `slow/fast to find mid` (1-1-3) + `in-place reverse` + `2 pointers walking toward each other from both ends`. This "split + reverse + merge/compare" combo is the backbone of many O(1)-space linked list problems.

```java
// java
// LC 234 - Palindrome Linked List
// IDEA: 1) slow/fast -> mid  2) reverse from mid  3) compare front half vs reversed back half
// time = O(n), space = O(1)
class Solution {
    public boolean isPalindrome(ListNode head) {
        // 1) find mid (for even len, slow stops at the 1st node of the 2nd half)
        ListNode slow = head, fast = head;
        while (fast != null && fast.next != null) {
            slow = slow.next;
            fast = fast.next.next;
        }

        // 2) reverse the 2nd half, `prev` becomes its new head
        ListNode prev = null;
        while (slow != null) {
            ListNode nxt = slow.next;
            slow.next = prev;
            prev = slow;
            slow = nxt;
        }

        /** NOTE !!! loop on the REVERSED half (it is the shorter/equal one)
         *  -> odd length simply compares the mid node with itself */
        ListNode l = head, r = prev;
        while (r != null) {
            if (l.val != r.val) return false;
            l = l.next;
            r = r.next;
        }
        return true;
    }
}
```

```python
# python
# LC 234 - Palindrome Linked List
# IDEA: 1) slow/fast -> mid  2) reverse from mid  3) compare front half vs reversed back half
# time = O(n), space = O(1)
class Solution(object):
    def isPalindrome(self, head):
        # 1) find mid
        slow = fast = head
        while fast and fast.next:
            slow = slow.next
            fast = fast.next.next
        # 2) reverse 2nd half
        prev = None
        while slow:
            slow.next, prev, slow = prev, slow, slow.next
        # 3) NOTE : loop on the reversed half (right), so odd length is auto handled
        left, right = head, prev
        while right:
            if left.val != right.val:
                return False
            left = left.next
            right = right.next
        return True
```

## 2) LC Example


### 2-1) Remove Duplicates from Sorted List — LC 83 ⭐⭐⭐
```java
// LC 83 (LC 26)
// https://labuladong.online/algo/essential-technique/array-two-pointers-summary/#%E5%8E%9F%E5%9C%B0%E4%BF%AE%E6%94%B9
class Solution {
    public ListNode deleteDuplicates(ListNode head) {
        if (head == null) return null;
        ListNode slow = head, fast = head;
        while (fast != null) {
            if (fast.val != slow.val) {
                // nums[slow] = nums[fast];
                slow.next = fast;
                // slow++;
                slow = slow.next;
            }
            // fast++
            fast = fast.next;
        }
        // 断开与后面重复元素的连接
        slow.next = null;
        return head;
    }
}
```

### 2-2) Remove Duplicates from Sorted List II — LC 82 ⭐⭐⭐⭐

> Twist vs LC 83 : here a duplicated value must be deleted **entirely** (keep only distinct values), so the head itself can be removed -> need a `dummy` node, and `prev` must stay **behind** the run of duplicates.

| | LC 83 | LC 82 |
|---|---|---|
| keep | 1 copy of each value | only values appearing **once** |
| dummy needed | no (head always survives) | **yes** (head may be deleted) |
| pointers | `slow` = last kept node, `fast` = scanner | `prev` = last kept node, `cur` = scanner |

```java
// java
// LC 82 - Remove Duplicates from Sorted List II
// IDEA: dummy head + prev/cur 2 pointers, skip the WHOLE run of equal values
// time = O(n), space = O(1)
class Solution {
    public ListNode deleteDuplicates(ListNode head) {
        /** NOTE !!! dummy, since head may be deleted */
        ListNode dummy = new ListNode(0, head);
        ListNode prev = dummy, cur = head;
        while (cur != null) {
            if (cur.next != null && cur.val == cur.next.val) {
                int dup = cur.val;
                // NOTE !!! skip ALL nodes with the duplicated val
                while (cur != null && cur.val == dup) {
                    cur = cur.next;
                }
                /** NOTE !!! do NOT move prev here, just re-link */
                prev.next = cur;
            } else {
                prev = cur;
                cur = cur.next;
            }
        }
        return dummy.next;
    }
}
```

```python
# python
# LC 82 - Remove Duplicates from Sorted List II
# IDEA: dummy head + prev/cur 2 pointers, skip the WHOLE run of equal values
# time = O(n), space = O(1)
class Solution(object):
    def deleteDuplicates(self, head):
        dummy = ListNode(0, head)
        prev, cur = dummy, head
        while cur:
            if cur.next and cur.val == cur.next.val:
                dup = cur.val
                # NOTE : skip ALL nodes with the duplicated val
                while cur and cur.val == dup:
                    cur = cur.next
                # NOTE : prev does NOT move, only re-link
                prev.next = cur
            else:
                prev = cur
                cur = cur.next
        return dummy.next
```