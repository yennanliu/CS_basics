# V0 
# https://github.com/yennanliu/CS_basics/blob/master/data_structure/python/linkedList.py
# IDEA : ITERATION
class Solution:
    def reverseList(self, head: ListNode):
        prev = None
        current = head 
        while(current is not None): 
            next_ = current.next
            current.next = prev 
            prev = current 
            current = next_
        head = prev 
        return head

# V1 
# https://blog.csdn.net/coder_orz/article/details/51306170
# IDEA : STACK 
# Definition for singly-linked list.
# class ListNode(object):
#     def __init__(self, x):
#         self.val = x
#         self.next = None
class Solution(object):
    def reverseList(self, head):
        """
        :type head: ListNode
        :rtype: ListNode
        """
        p = head
        newList = []
        while p:
            newList.insert(0, p.val)
            p = p.next

        p = head
        for v in newList:
            p.val = v
            p = p.next
        return head

# V1'
# http://bookshadow.com/weblog/2015/05/05/leetcode-reverse-linked-list/
# IDEA :  LINKED LIST 
class Solution:
    # @param {ListNode} head
    # @return {ListNode}
    def reverseList(self, head):
        dummy = ListNode(0)
        while head:
            next = head.next
            head.next = dummy.next
            dummy.next = head
            head = next
        return dummy.next

# V1'
# http://bookshadow.com/weblog/2015/05/05/leetcode-reverse-linked-list/
# IDEA :  ITERATION  
class Solution:
    # @param {ListNode} head
    # @return {ListNode}
    def reverseList(self, head):
        return self.doReverse(head, None)
    def doReverse(self, head, newHead):
        if head is None:
            return newHead
        next = head.next
        head.next = newHead
        return self.doReverse(next, head)

# V2 
# Time:  O(n)
# Space: O(1)
class ListNode(object):
    def __init__(self, x):
        self.val = x
        self.next = None

    def __repr__(self):
        if self:
            return "{} -> {}".format(self.val, repr(self.__next__))

# Iterative solution.
class Solution(object):
    # @param {ListNode} head
    # @return {ListNode}
    def reverseList(self, head):
        dummy = ListNode(float("-inf"))
        while head:
            dummy.next, head.next, head = head, dummy.next, head.next
        return dummy.__next__

# Time:  O(n)
# Space: O(n)
# Recursive solution.
class Solution2(object):
    # @param {ListNode} head
    # @return {ListNode}
    def reverseList(self, head):
        [begin, end] = self.reverseListRecu(head)
        return begin

    def reverseListRecu(self, head):
        if not head:
            return [None, None]

        [begin, end] = self.reverseListRecu(head.__next__)

        if end:
            end.next = head
            head.next = None
            return [begin, head]
        else:
            return [head, head]
