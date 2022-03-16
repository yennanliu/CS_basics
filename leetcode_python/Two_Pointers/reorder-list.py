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
# IDEA : Reverse the Second Part of the List and Merge Two Sorted Lists
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

# V1
# http://bookshadow.com/weblog/2015/01/29/leetcode-reorder-list/
# Definition for singly-linked list.
# class ListNode:
#     def __init__(self, x):
#         self.val = x
#         self.next = None
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
# Time:  O(n)
# Space: O(1)
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