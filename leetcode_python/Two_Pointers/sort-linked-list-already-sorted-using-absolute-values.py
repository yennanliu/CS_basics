"""

2046. Sort Linked List Already Sorted Using Absolute Values
Medium
(premium / locked problem)

Given the head of a singly linked list that is sorted in non-decreasing order using the absolute values of its nodes, return the list sorted in non-decreasing order using the actual values of its nodes.


Example 1:

Input: head = [0,2,-5,5,10,-10]
Output: [-10,-5,0,2,5,10]
Explanation:
The list sorted in non-descending order using the absolute values of the nodes is [0,2,-5,5,10,-10].
The list sorted in non-descending order using the actual values is [-10,-5,0,2,5,10].

Example 2:

Input: head = [0,1,2]
Output: [0,1,2]
Explanation:
The linked list is already sorted in non-decreasing order.

Example 3:

Input: head = [1]
Output: [1]
Explanation:
The linked list is already sorted in non-decreasing order.


Constraints:

The number of nodes in the list is the range [1, 10^5].
-5000 <= Node.val <= 5000
head is sorted in non-decreasing order using the absolute value of its nodes.

"""

# V0
# IDEA : THE NEGATIVES ARE ALREADY IN REVERSE ORDER — MOVE THEM TO THE FRONT
#
#   sorted by absolute value means the negative nodes, read left to right,
#   have DECREASING actual values (-5 before -10 because 5 < 10). so the
#   correct output is simply :
#       (negatives, reversed) ++ (non-negatives, untouched)
#
#   one pass with `prev` : whenever the next node is negative, unlink it and
#   splice it at the very front. because we take them in encounter order and
#   always push to the front, they come out reversed — exactly what is needed.
#
# time = O(n), space = O(1)
# Definition for singly-linked list.
# class ListNode(object):
#     def __init__(self, val=0, next=None):
#         self.val = val
#         self.next = next
class Solution(object):
    def sortLinkedList(self, head):
        prev, cur = head, head.next
        while cur:
            if cur.val < 0:
                prev.next = cur.next      # unlink cur
                cur.next = head           # push it to the front
                head = cur
                cur = prev.next
            else:
                prev, cur = cur, cur.next
        return head
