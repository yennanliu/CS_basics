# V1 


# V2 
# https://blog.csdn.net/fuxuemingzhu/article/details/80785630
# Definition for singly-linked list.
# class ListNode(object):
#     def __init__(self, x):
#         self.val = x
#         self.next = None
class TreeNode:
    def __init__(self, data):
        self.data = data
        self.lLink = None
        self.rLink = None

class Solution(object):
    def insertionSortList(self, head):
        """
        :type head: ListNode
        :rtype: ListNode
        """
        if not head or not head.__next__: return head
        root = TreeNode(0)
        root.next = head
        while head.__next__:
            if head.val <= head.next.val:
                head = head.__next__
            else:
                temp = head.__next__
                q = root
                head.next = head.next.__next__
                while q.__next__ and q.next.val < temp.val:
                    q = q.__next__
                temp.next = q.__next__
                q.next = temp
        return root.__next__

# V3 
# Time:  O(n ^ 2)
# Space: O(1)

class ListNode(object):
    def __init__(self, x):
        self.val = x
        self.next = None

    def __repr__(self):
        if self:
            return "{} -> {}".format(self.val, repr(self.__next__))
        else:
            return "Nil"

class Solution(object):
    # @param head, a ListNode
    # @return a ListNode
    def insertionSortList(self, head):
        if head is None or self.isSorted(head):
            return head

        dummy = ListNode(-2147483648)
        dummy.next = head
        cur, sorted_tail = head.__next__, head
        while cur:
            prev = dummy
            while prev.next.val < cur.val:
                prev = prev.__next__
            if prev == sorted_tail:
                cur, sorted_tail = cur.__next__, cur
            else:
                cur.next, prev.next, sorted_tail.next = prev.next, cur, cur.next
                cur = sorted_tail.__next__

        return dummy.__next__

    def isSorted(self, head):
        while head and head.__next__:
            if head.val > head.next.val:
                return False
            head = head.__next__
        return True
