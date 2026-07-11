"""

143. Reorder List
Medium

You are given the head of a singly linked-list. The list can be represented as:

L0 → L1 → … → Ln - 1 → Ln
Reorder the list to be on the following form:

L0 → Ln → L1 → Ln - 1 → L2 → Ln - 2 → …
You may not modify the values in the list's nodes. Only nodes themselves may be changed.

 

Example 1:


Input: head = [1,2,3,4]
Output: [1,4,2,3]
Example 2:


Input: head = [1,2,3,4,5]
Output: [1,5,2,4,3]
 

Constraints:

The number of nodes in the list is in the range [1, 5 * 104].
1 <= Node.val <= 1000


# NOTE :

This problem is a combination of these three easy problems:

LC 876 Middle of the Linked List.

LC 206 Reverse Linked List.

LC 021 Merge Two Sorted Lists.

"""

# V0
# IDEA:  MIDDLE OF LINLED LIST,  Reverse the second half, Merge the two halves
# https://github.com/yennanliu/CS_basics/blob/master/leetcode_java/src/main/java/LeetCodeJava/LinkedList/ReorderList.java#L61
# time = O(n)
# space = O(1)
class Solution(object):
    def reorderList(self, head):
        """
        Modify the linked list in-place.

        Example:
        1 -> 2 -> 3 -> 4 -> 5

        becomes

        1 -> 5 -> 2 -> 4 -> 3
        """

        # Edge case:
        # Empty list or single-node list is already reordered.
        if not head or not head.next:
            return

        # ==================================================
        # STEP 1: Find the middle of the linked list
        # ==================================================

        # Both pointers start at head.
        slow = fast = head

        # slow moves 1 step each iteration
        # fast moves 2 steps each iteration
        #
        # When fast reaches the end,
        # slow will be at the middle.
        while fast and fast.next:
            slow = slow.next
            fast = fast.next.next

        # Example:
        # 1 -> 2 -> 3 -> 4 -> 5
        #
        # slow ends at node 3

        # ==================================================
        # STEP 2: Reverse the second half
        # ==================================================

        # Start reversing from the node AFTER the middle.
        curr = slow.next

        # Disconnect first half from second half.
        slow.next = None

        # Standard LC 206 reverse-list setup.
        prev = None

        while curr:
            # Save next node before changing pointers.
            nxt = curr.next

            # Reverse current node's pointer.
            curr.next = prev

            # Move prev forward.
            prev = curr

            # Move curr forward.
            curr = nxt

        # Example:
        #
        # Original second half:
        # 4 -> 5
        #
        # After reverse:
        # 5 -> 4
        #
        # prev points to 5.

        # ==================================================
        # STEP 3: Merge the two halves
        # ==================================================

        # First half:
        # 1 -> 2 -> 3
        #
        # Second half:
        # 5 -> 4
        first = head
        second = prev

        while second:

            # Save next nodes before rewiring.
            tmp1 = first.next
            tmp2 = second.next

            # Insert node from second half
            # after current node from first half.
            first.next = second

            # Connect back to next node
            # in the first half.
            second.next = tmp1

            # Advance pointers.
            first = tmp1
            second = tmp2

        # Done.
        # The list is modified in-place.



# V0
# IDEA : Reverse the Second Part of the List and Merge Two Sorted Lists
# time = O(n)
# space = O(1)
class Solution:
    def reorderList(self, head):
        if not head:
            return 
        
        #---------------------------------------------
        # find the middle of linked list [Problem 876]
        #---------------------------------------------
        # in 1->2->3->4->5->6 find 4 
        slow = fast = head
        while fast and fast.next:
            slow = slow.next
            fast = fast.next.next 
            
        #---------------------------------------------
        # reverse the second part of the list [Problem 206]
        #---------------------------------------------
        # convert 1->2->3->4->5->6 into 1->2->3->4 and 6->5->4
        # reverse the second half in-place
        prev, curr = None, slow  #  NOTE !!! we get curr from slow
        while curr:
            tmp = curr.next
            
            curr.next = prev
            prev = curr
            curr = tmp    

        #---------------------------------------------
        # merge two sorted linked lists [Problem 21]
        #---------------------------------------------
        # merge 1->2->3->4 and 6->5->4 into 1->6->2->5->3->4
        first, second = head, prev # NOTE !!! : we get first from head, second from prev
        # NOTE !!! while second.next as condition
        while second.next:
            tmp = first.next
            first.next = second
            first = tmp
            
            tmp = second.next
            second.next = first
            second = tmp


# V0-2
# time = O(n)
# space = O(n)
class Solution(object):
    def reorderList(self, head):
        if not head or not head.next:
            return
            
        # Step 1: Collect the ACTUAL Node objects into a list
        nodes = []
        curr = head
        while curr:
            nodes.append(curr)
            curr = curr.next
            
        # Step 2: Use two pointers to interleave the original nodes in-place
        l = 0
        r = len(nodes) - 1
        
        while l < r:
            # Point the left node to the right node
            nodes[l].next = nodes[r]
            l += 1
            
            # If pointers met, stop immediately to prevent circular link loops
            if l == r:
                break
                
            # Point the right node to the next left node
            nodes[r].next = nodes[l]
            r -= 1
            
        # Step 3: CRITICAL FIX: Cut off the tail node's next pointer 
        # to prevent a circular reference loop cycle error
        nodes[l].next = None

# V0'
# IDEA : Reverse the Second Part of the List and Merge Two Sorted Lists (simplified code from V1)
# time = O(n)
# space = O(1)
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
# time = O(n)
# space = O(1)
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

# V1
# http://bookshadow.com/weblog/2015/01/29/leetcode-reorder-list/
# Definition for singly-linked list.
# class ListNode:
#     def __init__(self, x):
#         self.val = x
#         self.next = None
# time = O(n)
# space = O(1)
class Solution:
    # @param head, a ListNode
    # @return nothing
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

# V1'
# IDEA : Reverse the Second Part of the List and Merge Two Sorted Lists
# https://leetcode.com/problems/reorder-list/solution/
# time = O(n)
# space = O(1)
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

# V1''
# IDEA : Reverse the Second Part of the List and Merge Two Sorted Lists (simplified code from V1)
# https://leetcode.com/problems/reorder-list/solution/
# time = O(n)
# space = O(1)
class Solution:
    def reorderList(self, head: ListNode) -> None:
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

# V1''
# https://www.jiuzhang.com/solution/reorder-list/#tag-highlight-lang-python
# time = O(n)
# space = O(1)
from lintcode import ListNode
# Definition of ListNode
# class ListNode(object):
#     def __init__(self, val, next=None):
#         self.val = val
#         self.next = next
class Solution:
    """
    @param head: The first node of the linked list.
    @return: nothing
    """
    def reorderList(self, head):
        # write your code here
        if None == head or None == head.next:
            return head

        pfast = head
        pslow = head
        
        while pfast.next and pfast.next.next:
            pfast = pfast.next.next
            pslow = pslow.next
        pfast = pslow.next
        pslow.next = None
        
        pnext = pfast.next
        pfast.next = None
        while pnext:
            q = pnext.next
            pnext.next = pfast
            pfast = pnext
            pnext = q

        tail = head
        while pfast:
            pnext = pfast.next
            pfast.next = tail.next
            tail.next = pfast
            tail = tail.next.next
            pfast = pnext
        return head

# V2
# time = O(n)
# space = O(1)
class ListNode(object):
    def __init__(self, x):
        self.val = x
        self.next = None

    def __repr__(self):
        if self:
            return "{} -> {}".format(self.val, repr(self.next))

class Solution(object):
    # @param head, a ListNode
    # @return nothing
    def reorderList(self, head):
        if head == None or head.next == None:
            return head

        fast, slow, prev = head, head, None
        while fast != None and fast.next != None:
            fast, slow, prev = fast.next.next, slow.next, slow
        current, prev.next, prev = slow, None, None

        while current != None:
            current.next, prev, current = prev, current, current.next

        l1, l2 = head, prev
        dummy = ListNode(0)
        current = dummy

        while l1 != None and l2 != None:
            current.next, current, l1 = l1, l1, l1.next
            current.next, current, l2 = l2, l2, l2.next

        return dummy.next