"""

1669. Merge In Between Linked Lists
Medium

You are given two linked lists: list1 and list2 of sizes n and m respectively.

Remove list1's nodes from the ath node to the bth node, and put list2 in their place.

Build the result list and return its head.


Example 1:

Input: list1 = [10,1,13,6,9,5], a = 3, b = 4, list2 = [1000000,1000001,1000002]
Output: [10,1,13,1000000,1000001,1000002,5]
Explanation: We remove the nodes 3 and 4 and put the entire list2 in their place.

Example 2:

Input: list1 = [0,1,2,3,4,5,6], a = 2, b = 5, list2 = [1000000,1000001,1000002,1000003,1000004]
Output: [0,1,1000000,1000001,1000002,1000003,1000004,6]


Constraints:

3 <= list1.length <= 10^4
1 <= a <= b < list1.length - 1
1 <= list2.length <= 10^4

"""

# V0
# IDEA : POINTER SURGERY (find the two splice points, then relink)
#
#   we need two nodes of list1 :
#     p = node at index a-1  (the last node we KEEP before the cut)
#     q = node at index b    (the last node we REMOVE)
#   then :
#     p.next = head of list2
#     tail of list2 .next = q.next
#
#   NOTE : constraints guarantee a >= 1 and b < n-1, so p and q.next both exist
#          -> no dummy head needed and the returned head is still list1.
#
# time = O(n + m), space = O(1)
# Definition for singly-linked list.
# class ListNode(object):
#     def __init__(self, val=0, next=None):
#         self.val = val
#         self.next = next
class Solution(object):
    def mergeInBetween(self, list1, a, b, list2):
        p = list1
        for _ in range(a - 1):
            p = p.next

        q = p
        for _ in range(b - a + 1):
            q = q.next          # q is now the node at index b

        tail = list2
        while tail.next:
            tail = tail.next

        p.next = list2
        tail.next = q.next
        q.next = None
        return list1
