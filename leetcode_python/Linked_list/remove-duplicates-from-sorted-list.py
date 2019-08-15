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
