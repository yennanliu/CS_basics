# Given a sorted linked list, delete all nodes that have duplicate numbers, leaving only distinct numbers from the original list.

# Example 1:

# Input: 1->2->3->3->4->4->5
# Output: 1->2->5

# Example 2:

# Input: 1->1->1->2->3
# Output: 2->3

# V0 : TO DOUBLE CHECK 
# class Solution:
#     def deleteDuplicates(self, head):
#         """
#         :type head: ListNode
#         :rtype: ListNode
#         """
#         root = ListNode(0)
#         root.next = head
#         head = root
#         while head and head.next:
#             if head.next.val != head.next.next.val:
#                 head.next = head.next.next
#             else:
#                 head = head.next
#         return root.next

# V1 
# https://blog.csdn.net/fuxuemingzhu/article/details/80786545
# Definition for singly-linked list.
# class ListNode:
#     def __init__(self, x):
#         self.val = x
#         self.next = None
class Solution:
    def deleteDuplicates(self, head):
        """
        :type head: ListNode
        :rtype: ListNode
        """
        root = ListNode(0)
        root.next = head
        val_list = []
        while head:
            val_list.append(head.val)
            head = head.next
        counter = collections.Counter(val_list)
        head = root
        while head and head.next:
            if counter[head.next.val] != 1:
                head.next = head.next.next
            else:
                head = head.next
        return root.next

# V2 
# Time:  O(n)
# Space: O(1)
class ListNode(object):
    def __init__(self, x):
        self.val = x
        self.next = None

    def __repr__(self):
        if self is None:
            return "Nil"
        else:
            return "{} -> {}".format(self.val, repr(self.__next__))

class Solution(object):
    def deleteDuplicates(self, head):
        """
        :type head: ListNode
        :rtype: ListNode
        """
        dummy = ListNode(0)
        pre, cur = dummy, head
        while cur:
            if cur.__next__ and cur.next.val == cur.val:
                val = cur.val
                while cur and cur.val == val:
                    cur = cur.__next__
                pre.next = cur
            else:
                pre.next = cur
                pre = cur
                cur = cur.__next__
        return dummy.__next__
