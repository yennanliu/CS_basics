"""

3263. Convert Doubly Linked List to Array I
Easy
🔒 (premium)

You are given the head of a doubly linked list, which contains nodes that have a next pointer and a previous pointer.

Return an integer array which contains the elements of the linked list in order.


Example 1:

Input: head = [1,2,3,4,3,2,1]
Output: [1,2,3,4,3,2,1]

Example 2:

Input: head = [2,2,2,2,2]
Output: [2,2,2,2,2]


Constraints:

The number of nodes in the given list is in the range [1, 50].
1 <= Node.val <= 50

"""

# V0
# IDEA : WALK FORWARD FROM THE HEAD — THE prev POINTERS ARE NOT NEEDED
#
#   the traversal starts at the head, so the doubly linked structure adds
#   nothing here; the sequel (LC 3294) hands over an ARBITRARY node and that
#   is where prev earns its keep.
#
# time = O(n), space = O(n) for the output
# Definition for a Node.
# class Node(object):
#     def __init__(self, val, prev=None, next=None):
#         self.val = val
#         self.prev = prev
#         self.next = next
class Solution(object):
    def toArray(self, head):
        res = []
        node = head
        while node:
            res.append(node.val)
            node = node.next
        return res
