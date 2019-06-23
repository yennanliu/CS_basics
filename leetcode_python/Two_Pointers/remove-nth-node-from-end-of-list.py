

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


# V1  
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

"""
if __name__ == "__main__":
    head = ListNode(1)
    head.next = ListNode(2)
    head.next.next = ListNode(3)
    head.next.next.next = ListNode(4)
    head.next.next.next.next = ListNode(5)

    print Solution().removeNthFromEnd(head, 2)

"""



