# V1 : dev 






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
