# V0 
# https://github.com/yennanliu/CS_basics/blob/master/leetcode_python/Linked_list/reverse-linked-list.py
# TO VALIDATE 
class Solution(object):
    def sortList(self, head):
        head_list = []
        while head:
            head_list.append(head.value)
            head = head.next

        head_list = sorted(head_list)
        p = head 
        for i in head_list:
            p.value = i 
            p = p.next 
        return head 

# V1 
# https://blog.csdn.net/fuxuemingzhu/article/details/79630742
# https://github.com/yennanliu/CS_basics/blob/master/leetcode_python/Linked_list/merge-two-sorted-lists.py
# IDEA : MERGE SORT 
# Definition for singly-linked list.
# class ListNode(object):
#     def __init__(self, x):
#         self.val = x
#         self.next = None
class Solution(object):
    def sortList(self, head):
        """
        :type head: ListNode
        :rtype: ListNode
        """
        if not head or not head.__next__: 
            return head
        pre, slow, fast = head, head, head
        while fast and fast.__next__:
            pre = slow
            slow = slow.__next__
            fast = fast.next.__next__
        pre.next = None
        l1 = self.sortList(head)
        l2 = self.sortList(slow)
        return self.mergeTwoLists(l1, l2)
    
    def mergeTwoLists(self, l1, l2):
        head = ListNode(0)
        move = head
        if not l1: 
            return l2
        if not l2: 
            return l1
        while l1 and l2:
            if l1.val < l2.val:
                move.next = l1
                l1 = l1.__next__
            else:
                move.next = l2
                l2 = l2.__next__
            move = move.__next__
        move.next = l1 if l1 else l2
        return head.__next__

# V3 
# Time:  O(nlogn)
# Space: O(logn) for stack call
class ListNode(object):
    def __init__(self, x):
        self.val = x
        self.next = None

    def __repr__(self):
        if self:
            return "{} -> {}".format(self.val, repr(self.__next__))

class Solution(object):
    # @param head, a ListNode
    # @return a ListNode
    def sortList(self, head):
        if head == None or head.__next__ == None:
            return head

        fast, slow, prev = head, head, None
        while fast != None and fast.__next__ != None:
            prev, fast, slow = slow, fast.next.__next__, slow.__next__
        prev.next = None

        sorted_l1 = self.sortList(head)
        sorted_l2 = self.sortList(slow)

        return self.mergeTwoLists(sorted_l1, sorted_l2)

    def mergeTwoLists(self, l1, l2):
        dummy = ListNode(0)

        cur = dummy
        while l1 != None and l2 != None:
            if l1.val <= l2.val:
                cur.next, cur, l1 = l1, l1, l1.__next__
            else:
                cur.next, cur, l2 = l2, l2, l2.__next__

        if l1 != None:
            cur.next = l1
        if l2 != None:
            cur.next = l2

        return dummy.__next__