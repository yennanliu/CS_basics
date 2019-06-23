
# V1 : DEV 



# V2 
# https://blog.csdn.net/qqxx6661/article/details/78817958
"""

PROCESS 

1) SPLIT THE LINKED LIST into 2 sub-list : sub list 1, sub list 2  (from midpint)
2) reverse the 2nd sub list (sub list 2 )
3) merge sub list 1 and sub list 2 


"""
class Solution:
    def reorderList(self, head):
        """
        :type head: ListNode
        :rtype: void Do not return anything, modify head in-place instead.
        """
        if not head:
            return
        # split {1,2,3,4,5} to {1,2,3}{4,5}
        fast = slow = head 
        while fast and fast.__next__:
            slow = slow.__next__
            fast = fast.next.__next__
        head1 = head
        head2 = slow.__next__
        slow.next = None
        # reverse the second {4,5} to {5,4}
        cur, pre = head2, None
        while cur:
            nex = cur.__next__
            cur.next = pre
            pre = cur
            cur = nex
        # merge
        cur1, cur2 = head1, pre
        while cur2:
            nex1, nex2 = cur1.__next__, cur2.__next__
            cur1.next = cur2
            cur2.next = nex1
            cur1, cur2 = nex1, nex2


# V3 
# class ListNode(object):
#     def __init__(self, x):
#         self.val = x
#         self.next = None

#     def __repr__(self):
#         if self:
#             return "{} -> {}".format(self.val, repr(self.next))

# class Solution(object):
#     # @param head, a ListNode
#     # @return nothing
#     def reorderList(self, head):
#         if head == None or head.next == None:
#             return head

#         fast, slow, prev = head, head, None
#         while fast != None and fast.next != None:
#             fast, slow, prev = fast.next.next, slow.next, slow
#         current, prev.next, prev = slow, None, None

#         while current != None:
#             current.next, prev, current = prev, current, current.next

#         l1, l2 = head, prev
#         dummy = ListNode(0)
#         current = dummy

#         while l1 != None and l2 != None:
#             current.next, current, l1 = l1, l1, l1.next
#             current.next, current, l2 = l2, l2, l2.next

#         return dummy.next

