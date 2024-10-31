# Two pointers - Linkedlist

- Ref
    - [fucking-algorithm : 2 pointers Linkedlist](https://labuladong.online/algo/essential-technique/linked-list-skills-summary/)

### 0-1) Types

- Pointer types
    - `Fast - Slow pointers`
        - fast, slow pointers from `same start point`
        - Usualy set
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

### 1-1) Basic OP

#### 1-1-1 : Check if there is a circular linked list 
```java
// java
boolean hasCycle(ListNode head){
    fast = slow = head;
    // NOTE : while loop condition
    while (fast != null and fast.next != null){
        /** NOTE : need to do move slow, fast pointer then compare them */
        slow = slow.next;
        fast = fast.next.next;
        if (fast == slow){
            return True
        }
    }
    return False;
}
```

#### 1-1-2 : return the "ring start point" of circular linked list 
```java
// java
// LC 141
ListNode detectCycle(ListNode head){
    ListNode fast, slow;
    fast = slow = head;
    while (fast != null and fast.next != null){
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

## 2) LC Example


### 2-1) Remove Duplicates from Sorted List
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