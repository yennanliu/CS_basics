"""

83. Remove Duplicates from Sorted List

Easy

Given the head of a sorted linked list, delete all duplicates such that each element appears only once. Return the linked list sorted as well.

 

Example 1:


Input: head = [1,1,2]
Output: [1,2]
Example 2:


Input: head = [1,1,2,3,3]
Output: [1,2,3]
 

Constraints:

The number of nodes in the list is in the range [0, 300].
-100 <= Node.val <= 100
The list is guaranteed to be sorted in ascending order.

"""

# V0

# V1 
# https://blog.csdn.net/coder_orz/article/details/51506143
# Definition for singly-linked list.
# class ListNode(object):
#     def __init__(self, x):
#         self.val = x
#         self.next = None
class Solution(object):
    def deleteDuplicates(self, head):
        """
        :type head: ListNode
        :rtype: ListNode
        """
        p = head
        while p:
            if p.next and p.next.val == p.val:
                p.next = p.next.next
            else:
                p = p.next
        return head

# V1' 
# https://blog.csdn.net/coder_orz/article/details/51506143
class Solution(object):
    def deleteDuplicates(self, head):
        """
        :type head: ListNode
        :rtype: ListNode
        """
        p = q = head
        while p:
            while q and q.val == p.val:
                q = q.next
            p.next = q
            p = q
        return head

# V1'' 
# https://blog.csdn.net/coder_orz/article/details/51506143
# IDEA : ITERATION 
class Solution(object):
    def deleteDuplicates(self, head):
        """
        :type head: ListNode
        :rtype: ListNode
        """
        if not head or not head.next:
            return head
        head.next = self.deleteDuplicates(head.next)
        return head if head.val != head.next.val else head.next

# V2 
# Time:  O(n)
# Space: O(1)
class ListNode(object):
    def __init__(self, x):
        self.val = x
        self.next = None


class Solution(object):
    def deleteDuplicates(self, head):
        """
        :type head: ListNode
        :rtype: ListNode
        """
        cur = head
        while cur:
            runner = cur.__next__
            while runner and runner.val == cur.val:
                runner = runner.__next__
            cur.next = runner
            cur = runner
        return head

    def deleteDuplicates2(self, head):
        """
        :type head: ListNode
        :rtype: ListNode
        """
        if not head: return head
        if head.__next__:
            if head.val == head.next.val:
                head = self.deleteDuplicates2(head.__next__)
            else:
                head.next = self.deleteDuplicates2(head.next)
        return head