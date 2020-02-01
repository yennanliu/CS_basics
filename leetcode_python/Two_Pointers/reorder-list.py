# V0 
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
