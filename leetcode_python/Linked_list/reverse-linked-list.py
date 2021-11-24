"""
Given the head of a singly linked list, reverse the list, and return the reversed list.

Example 1:

Input: head = [1,2,3,4,5]
Output: [5,4,3,2,1]

Example 2:

Input: head = [1,2]
Output: [2,1]

Example 3:

Input: head = []
Output: []
 

Constraints:

The number of nodes in the list is the range [0, 5000].
-5000 <= Node.val <= 5000
 

Follow up: A linked list can be reversed either iteratively or recursively. Could you implement both?

"""

# V0
# IDEA : Linkedlist basics
class Solution:
    def reverseList(self, head):
        ### NOTE : we define _prev, _cur first
        _prev = None
        _cur = head
        ### NOTE : while tbere is still node in _cur (but not head)
        while _cur:
            ### STEP 1) get _next
            _next = _cur.next
            ### STEP 2) link _cur to _prev
            _cur.next = _prev 
            ### STEP 3) assign _cur to _prev (make _prev as _cur)
            _prev = _cur
            ### STEP 4) assign _next to _cur (make _cur as _next)
            _cur = _next
        ### STEP 5) assign head as _prev (make head as _prev -> make head as the "inverse" head)
        head = _prev 
        # return the head
        return head
        #return _prev # this one works as well

# V0'
# https://github.com/yennanliu/CS_basics/blob/master/data_structure/python/linkedList.py
# IDEA : Linkedlist basics
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