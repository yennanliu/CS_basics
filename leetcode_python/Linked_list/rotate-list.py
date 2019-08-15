# Given a linked list, rotate the list to the right by k places, where k is non-negative.

# Example 1:

# Input: 1->2->3->4->5->NULL, k = 2
# Output: 4->5->1->2->3->NULL
# Explanation:
# rotate 1 steps to the right: 5->1->2->3->4->NULL
# rotate 2 steps to the right: 4->5->1->2->3->NULL

# Example 2:

# Input: 0->1->2->NULL, k = 4
# Output: 2->0->1->NULL
# Explanation:
# rotate 1 steps to the right: 2->0->1->NULL
# rotate 2 steps to the right: 1->2->0->NULL
# rotate 3 steps to the right: 0->1->2->NULL
# rotate 4 steps to the right: 2->0->1->NULL


# V0 
# DO VIA STRING (LINKED LIST -> STR)
# DEV 

# V1 
# https://blog.csdn.net/fuxuemingzhu/article/details/80788107
# IDEA : 
# k = k % len(head)  ---> SINCE ROTATE IS A CYCLIC OPERATION
# SET UP 2 POINTERS : slow and fast,
# -> KEEP GO THROUGH THE LINKED LIST 
# -> STOP WHEN fast - slow = k
# -> THEN add fast to slow, 
# -> i.e. fast + slow. which is the needed answer

# Definition for singly-linked list.
# class ListNode:
#     def __init__(self, x):
#         self.val = x
#         self.next = None
class Solution:
    def rotateRight(self, head, k):
        """
        :type head: ListNode
        :type k: int
        :rtype: ListNode
        """
        if not head or not head.next: return head
        _len = 0
        root = head
        while head:
            _len += 1
            head = head.next
        k %= _len
        if k == 0: return root
        fast, slow = root, root
        while k - 1:
            fast = fast.next
            k -= 1
        pre = slow
        while fast.next:
            fast = fast.next
            pre = slow
            slow = slow.next
        pre.next = None
        fast.next = root
        return slow

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

class Solution(object):
    def rotateRight(self, head, k):
        """
        :type head: ListNode
        :type k: int
        :rtype: ListNode
        """
        if not head or not head.__next__:
            return head

        n, cur = 1, head
        while cur.__next__:
            cur = cur.__next__
            n += 1
        cur.next = head

        cur, tail = head, cur
        for _ in range(n - k % n):
            tail = cur
            cur = cur.__next__
        tail.next = None

        return cur