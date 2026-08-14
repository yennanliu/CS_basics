"""

2674. Split a Circular Linked List
Medium

Given a circular linked list list of positive integers, your task is to split it into 2 circular linked lists so that the first one contains the first half of the nodes in list (exactly ceil(list.length / 2) nodes) in the same order they appeared in list, and the second one contains the rest of the nodes in list in the same order they appeared in list.

Return an array answer of length 2 in which the first element is a circular linked list representing the first half and the second element is a circular linked list representing the second half.

A circular linked list is a normal linked list with the only difference being that the last node's next node, is the first node.


Example 1:

Input: nums = [1,5,7]
Output: [[1,5],[7]]
Explanation: The initial list has 3 nodes so the first half would be the first 2 elements since ceil(3 / 2) = 2 and the rest which is 1 node is in the second half.

Example 2:

Input: nums = [2,6,1,5]
Output: [[2,6],[1,5]]
Explanation: The initial list has 4 nodes so the first half would be the first 2 elements since ceil(4 / 2) = 2 and the rest which is 2 nodes are in the second half.


Constraints:

The number of nodes in list is in the range [2, 10^5]
0 <= Node.val <= 10^9
LastNode.next = FirstNode where LastNode is the last node of the list and FirstNode is the first one

"""

# V0
# IDEA : FAST / SLOW POINTERS ON A CIRCULAR LIST
#
#   walk `slow` 1 step and `fast` 2 steps per round, stopping as soon as
#   `fast` is the tail (fast.next == head) or one node before it
#   (fast.next.next == head). when the loop ends:
#     - odd length  -> fast IS the tail, slow sits on node #ceil(n/2)
#     - even length -> fast is one before the tail, so push it forward once,
#                      and slow again sits on node #ceil(n/2) == n/2
#   NOTE : because the list is circular there is no None sentinel, every
#          stop condition must be compared against `head` instead.
#   NOTE : after cutting we must close BOTH rings -
#          tail.next -> second head, slow.next -> head.
#
# time = O(n), space = O(1)
# Definition for singly-linked list.
# class ListNode(object):
#     def __init__(self, val=0, next=None):
#         self.val = val
#         self.next = next
class Solution(object):
    def splitCircularLinkedList(self, list):
        head = list
        slow = fast = head
        while fast.next != head and fast.next.next != head:
            slow = slow.next
            fast = fast.next.next
        if fast.next != head:
            fast = fast.next          # even length -> step onto the real tail
        second = slow.next
        fast.next = second            # close the 2nd ring
        slow.next = head              # close the 1st ring
        return [head, second]
