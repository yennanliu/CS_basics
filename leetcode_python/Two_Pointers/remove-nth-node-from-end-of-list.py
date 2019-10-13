"""
Given a linked list, remove the n-th node from the end of list and return its head.

Example:

Given linked list: 1->2->3->4->5, and n = 2.

After removing the second node from the end, the linked list becomes 1->2->3->5.
Note:

Given n will always be valid.

Follow up:

Could you do this in one pass?

"""
# Time:  O(n)
# Space: O(1)
#
# Given a linked list, remove the nth node from the end of list and return its head.
#
# For example,
#
#    Given linked list: 1->2->3->4->5, and n = 2.
#
#    After removing the second node from the end, the linked list becomes 1->2->3->5.
# Note:
# Given n will always be valid.
# Try to do this in one pass.
#

# V0


# V1 
# https://blog.csdn.net/coder_orz/article/details/51691267
class Solution(object):
    def removeNthFromEnd(self, head, n):
        """
        :type head: ListNode
        :type n: int
        :rtype: ListNode
        """
        new_head = ListNode(0)
        new_head.next = head
        fast = slow = new_head
        for i in range(n+1):
            fast = fast.next
        while fast:
            fast = fast.next
            slow = slow.next
        slow.next = slow.next.next
        return new_head.next

# V1'
# https://www.jiuzhang.com/solution/remove-nth-node-from-end-of-list/#tag-highlight-lang-python
class Solution(object):
    def removeNthFromEnd(self, head, n):
        res = ListNode(0)
        res.next = head
        tmp = res
        for i in range(0, n):
            head = head.next
        while head != None:
            head = head.next
            tmp = tmp.next
        tmp.next = tmp.next.next
        return res.next

# V1'' 
# https://blog.csdn.net/coder_orz/article/details/51691267
class Solution(object):
    def removeNthFromEnd(self, head, n):
        """
        :type head: ListNode
        :type n: int
        :rtype: ListNode
        """
        def getIndex(node):
            if not node:
                return 0
            index = getIndex(node.next) + 1
            if index > n:
                node.next.val = node.val
            return index
        getIndex(head)
        return head.next

# V1''' 
# https://blog.csdn.net/coder_orz/article/details/51691267
class Solution(object):
    def removeNthFromEnd(self, head, n):
        """
        :type head: ListNode
        :type n: int
        :rtype: ListNode
        """
        def remove(node):
            if not node:
                return 0, node
            index, node.next = remove(node.next)
            next_node = node if n != index+1 else node.next
            return index+1, next_node
        ind, new_head = remove(head)
        return new_head

# V1'''''
class ListNode:
    def __init__(self, x):
        self.val = x
        self.next = None

    def __repr__(self):
        if self is None:
            return "Nil"
        else:
            return "{} -> {}".format(self.val, repr(self.__next__))

class Solution:
    # @return a ListNode
    def removeNthFromEnd(self, head, n):
        my_list = []
        cur = head 
        while cur:
            my_list.append(cur.val)
            cur = cur.__next__ 
        my_list.pop(-n)
        return my_list

# V2 
# https://www.cnblogs.com/zuoyuan/p/3701971.html
# -- idea: 
# make a dummy node, make 2 pointers : p1 p2, 
# then move p1 only for n steps 
# then move p1 and p2 on the same time 
# when p1.next == None  --->  p2.next is the to-delete node 
class Solution:
    # @return a ListNode
    def removeNthFromEnd(self, head, n):
        dummy=ListNode(0); dummy.next=head
        p1=p2=dummy
        for i in range(n): p1=p1.__next__
        while p1.__next__:
            p1=p1.__next__; p2=p2.__next__
        p2.next=p2.next.__next__
        return dummy.__next__

# V3  
# Definition for singly-linked list. 
class ListNode:
    def __init__(self, x):
        self.val = x
        self.next = None

    def __repr__(self):
        if self is None:
            return "Nil"
        else:
            return "{} -> {}".format(self.val, repr(self.__next__))

class Solution:
    # @return a ListNode
    def removeNthFromEnd(self, head, n):
        dummy = ListNode(-1)
        dummy.next = head
        slow, fast = dummy, dummy

        for i in range(n):
            fast = fast.__next__

        while fast.__next__:
            slow, fast = slow.__next__, fast.__next__

        slow.next = slow.next.__next__

        return dummy.__next__
        # head = ListNode(1)
        # head.next = ListNode(2)
        # head.next.next = ListNode(3)
        # head.next.next.next = ListNode(4)
        # head.next.next.next.next = ListNode(5)
        # print Solution().removeNthFromEnd(head, 2)
