


# V1 




# V2 
# https://blog.csdn.net/fuxuemingzhu/article/details/80785630
# Definition for singly-linked list.
# class ListNode(object):
#     def __init__(self, x):
#         self.val = x
#         self.next = None

class Solution(object):
    def insertionSortList(self, head):
        """
        :type head: ListNode
        :rtype: ListNode
        """
        if not head or not head.next: return head
        root = TreeNode(0)
        root.next = head
        while head.next:
            if head.val <= head.next.val:
                head = head.next
            else:
                temp = head.next
                q = root
                head.next = head.next.next
                while q.next and q.next.val < temp.val:
                    q = q.next
                temp.next = q.next
                q.next = temp
        return root.next

# V3 
# Time:  O(n ^ 2)
# Space: O(1)

class ListNode(object):
    def __init__(self, x):
        self.val = x
        self.next = None

    def __repr__(self):
        if self:
            return "{} -> {}".format(self.val, repr(self.next))
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
        cur, sorted_tail = head.next, head
        while cur:
            prev = dummy
            while prev.next.val < cur.val:
                prev = prev.next
            if prev == sorted_tail:
                cur, sorted_tail = cur.next, cur
            else:
                cur.next, prev.next, sorted_tail.next = prev.next, cur, cur.next
                cur = sorted_tail.next

        return dummy.next

    def isSorted(self, head):
        while head and head.next:
            if head.val > head.next.val:
                return False
            head = head.next
        return True
