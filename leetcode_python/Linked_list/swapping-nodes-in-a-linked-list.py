"""

1721. Swapping Nodes in a Linked List
Medium

You are given the head of a linked list, and an integer k.

Return the head of the linked list after swapping the values of the kth node from the beginning and the kth node from the end (the list is 1-indexed).


Example 1:

Input: head = [1,2,3,4,5], k = 2
Output: [1,4,3,2,5]

Example 2:

Input: head = [7,9,6,6,7,8,3,0,9,5], k = 5
Output: [7,9,6,6,8,7,3,0,9,5]


Constraints:

The number of nodes in the list is n.
1 <= k <= n <= 10^5
0 <= Node.val <= 100

"""

# V0
# IDEA : TWO POINTERS, FIXED GAP (one pass, no length precomputation)
#
#   advance `fast` k-1 steps -> it now sits on the kth node from the front.
#   remember it as p.
#
#   then walk `fast` and `slow` together until fast hits the LAST node.
#   fast moved n-k more steps, so slow (started at head) is at index n-k
#   0-based = the kth node from the END. remember it as q.
#
#   swap p.val and q.val - the problem only asks for the VALUES to swap,
#   so no pointer surgery is needed.
#
#   NOTE : p and q may be the same node (odd length, k = (n+1)/2); the swap
#          is then a harmless no-op.
#
# time = O(n), space = O(1)
# Definition for singly-linked list.
# class ListNode(object):
#     def __init__(self, val=0, next=None):
#         self.val = val
#         self.next = next
class Solution(object):
    def swapNodes(self, head, k):
        fast = head
        for _ in range(k - 1):
            fast = fast.next
        p = fast

        slow = head
        while fast.next:
            fast = fast.next
            slow = slow.next
        q = slow

        p.val, q.val = q.val, p.val
        return head
