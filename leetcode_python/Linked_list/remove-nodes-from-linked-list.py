"""

2487. Remove Nodes From Linked List
Medium

You are given the head of a linked list.

Remove every node which has a node with a greater value anywhere to the right side of it.

Return the head of the modified linked list.


Example 1:

Input: head = [5,2,13,3,8]
Output: [13,8]
Explanation: The nodes that should be removed are 5, 2 and 3.
- Node 13 is to the right of node 5.
- Node 13 is to the right of node 2.
- Node 8 is to the right of node 3.

Example 2:

Input: head = [1,1,1,1]
Output: [1,1,1,1]
Explanation: Every node has value 1, so no nodes are removed.


Constraints:

The number of the nodes in the given list is in the range [1, 10^5].
1 <= Node.val <= 10^5

"""

# V0
# IDEA : REVERSE, KEEP A RUNNING MAXIMUM, REVERSE BACK
#
#   "is there anything bigger to my RIGHT?" is awkward to answer while
#   walking forward. reversing the list turns it into "is there anything
#   bigger to my LEFT?", which one running maximum settles in a single pass :
#   keep a node iff its value is >= the max seen so far.
#
#   the survivors come out in reverse order, so reverse once more at the end.
#   equivalently this is a monotonic-stack solution, done in O(1) extra space
#   by reusing the list's own pointers.
#
# time = O(n), space = O(1)
# Definition for singly-linked list.
# class ListNode(object):
#     def __init__(self, val=0, next=None):
#         self.val = val
#         self.next = next
class Solution(object):
    def removeNodes(self, head):
        def reverse(node):
            prev = None
            while node:
                nxt = node.next
                node.next = prev
                prev = node
                node = nxt
            return prev

        head = reverse(head)

        best = 0
        kept = None
        node = head
        while node:
            nxt = node.next
            if node.val >= best:
                best = node.val
                node.next = kept
                kept = node
            node = nxt

        return kept
