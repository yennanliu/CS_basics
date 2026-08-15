"""

3294. Convert Doubly Linked List to Array II
Medium
🔒 (premium)

You are given an arbitrary node from a doubly linked list, which contains nodes that have a next pointer and a previous pointer.

Return an integer array which contains the elements of the linked list in order.


Example 1:

Input: head = [1,2,3,4,5], node = 5
Output: [1,2,3,4,5]

Example 2:

Input: head = [4,5,6,7,8], node = 8
Output: [4,5,6,7,8]


Constraints:

The number of nodes in the given list is in the range [1, 500].
1 <= Node.val <= 1000
All nodes have unique Node.val.

"""

# V0
# IDEA : WALK BACK TO THE HEAD FIRST — THAT IS WHAT prev IS FOR
#
#   unlike LC 3263 the given node can be anywhere in the list, so the
#   traversal has two phases : follow prev until it runs out (that node is
#   the head), then follow next collecting the values.
#
#   each node is touched at most twice, so it stays linear.
#
# time = O(n), space = O(n) for the output
# Definition for a Node.
# class Node(object):
#     def __init__(self, val, prev=None, next=None):
#         self.val = val
#         self.prev = prev
#         self.next = next
class Solution(object):
    def toArray(self, node):
        while node.prev:
            node = node.prev

        res = []
        while node:
            res.append(node.val)
            node = node.next
        return res
