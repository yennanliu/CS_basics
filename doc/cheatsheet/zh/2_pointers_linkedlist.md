# Two pointers - Linkedlist（雙指標：鏈結串列）

> **範圍** — 走在**鏈結串列節點**上的雙指標：快慢指標找環、找中點、找倒數第 k 個、以及固定間距指標。不談陣列索引。
> **另見**：[2_pointers.md](./2_pointers.md) — 陣列／字串版的雙指標；[linked_list.md](./linked_list.md) — 完整的鏈結串列題型目錄，含反轉與合併。

- 參考
    - [fucking-algorithm : 2 pointers Linkedlist](https://labuladong.online/algo/essential-technique/linked-list-skills-summary/)

## LeetCode 題目清單

- [Two Pointers](https://leetcode.com/problem-list/two-pointers/)
- [Linked List](https://leetcode.com/problem-list/linked-list/)

### 0-1) 題型分類

- 指標的種類
    - `Fast - Slow pointers`（快慢指標）
        - 快、慢指標從`同一個起點`出發
        - 通常設定成
            - 慢指標一次走 1 格
            - 快指標一次走 2 格
        - 鏈結串列
            - 找鏈結串列的中點
            - 判斷鏈結串列有沒有環
                - LC 141
                - LC 142
            - 若有環，回傳環的起點
            - 找單向鏈結串列的最後 k 個元素
                - LC 19 : Remove Nth Node From End of List

## 1) 通用形式

### 1-1) 基本操作 ⭐⭐⭐⭐⭐

#### 1-1-1 : 判斷鏈結串列有沒有環
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

#### 1-1-2 : 回傳環的「入口節點」
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
#### 1-1-2-1 : 變形 — 在`陣列`上做 Floyd 找環（隱式鏈結串列）

> 變化點：這題沒有 `ListNode`。把 `i -> nums[i]` 當成「next」這條邊，接著用**完全一樣的兩階段 Floyd**（先相遇，再把其中一個指標重設回起點、兩者同速前進），最後停下來的環入口就是重複出現的那個值。

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

- 同一招適用於任何 `x -> f(x)` 的數列，例如 `LC 202 Happy Number`（`f(x)` = 各位數字的平方和）：若「快／慢」相遇在一個不等於 1 的值，代表這個數列會永遠繞圈。

#### 1-1-3 : 找單向鏈結串列的中點
```java
// java
while (fast != null and fast.next != null){
    fast = fast.next.next;
    slow = slow.next;
}
return slow;
```

#### 1-1-4 : 找單向鏈結串列的最後 k 個元素
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

#### 1-1-4-1 : 變形 — 把串列向右旋轉 k 位（`固定間距指標` + 接成環再剪開）

> 變化點：間距 `k` 的想法跟 1-1-4 一樣，但要先算 `k %= len`（k 可能超大），而且找到新的尾節點後，要先**把舊尾接回舊頭**再剪斷。

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

#### 1-1-5 : 兩條鏈結串列的交會點

**核心想法**：讓 `p1` 走完 `A` 再走 `B`，`p2` 走完 `B` 再走 `A`。兩者都剛好走 `m + n` 步，**長度差被抵消掉**，於是會同時抵達交會節點。若根本沒有交會，兩者會在第 `m + n` 步同時變成 `null`，迴圈結束並回傳 `null`。

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

#### 1-1-6 : 回文鏈結串列（找中點 -> 反轉後半段 -> 兩邊比對）

**模式**：`快慢指標找中點`（1-1-3）+ `原地反轉` + `兩個指標從頭尾往中間靠`。這套「切開 + 反轉 + 合併／比對」的組合，是很多 O(1) 空間鏈結串列題的骨架。

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

## 2) LC 範例


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

> 跟 LC 83 的差別：這裡只要有重複的值就要**整組刪光**（只留下唯一出現過一次的值），所以連頭節點都可能被刪掉 -> 需要 `dummy` 節點，而且 `prev` 必須停在整串重複值的**前面**。

| | LC 83 | LC 82 |
|---|---|---|
| 保留 | 每個值留 1 份 | 只留**出現一次**的值 |
| 需要 dummy | 不用（頭節點一定活著） | **要**（頭節點可能被刪） |
| 指標 | `slow` = 最後保留的節點，`fast` = 掃描指標 | `prev` = 最後保留的節點，`cur` = 掃描指標 |

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
