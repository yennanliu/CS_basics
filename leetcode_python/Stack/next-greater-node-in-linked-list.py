"""

1019. Next Greater Node In Linked List
Medium

You are given the head of a linked list with n nodes.

For each node in the list, find the value of the next greater node. That is, for each node, find the value of the first node that is next to it and has a strictly larger value than it.

Return an integer array answer where answer[i] is the value of the next greater node of the ith node (1-indexed). If the ith node does not have a next greater node, set answer[i] = 0.


Example 1:

Input: head = [2,1,5]
Output: [5,5,0]

Example 2:

Input: head = [2,7,4,3,5]
Output: [7,0,5,5,0]


Constraints:

The number of nodes in the list is n.
1 <= n <= 10^4
1 <= Node.val <= 10^9

"""

# Definition for singly-linked list.
class ListNode(object):
    def __init__(self, val=0, next=None):
        self.val = val
        self.next = next


# V0
# IDEA : MONOTONIC (DECREASING) STACK
#
#   1) dump the list into an array (so we can index it)
#   2) walk left to right keeping a stack of INDICES whose answer
#      is still unknown, with strictly decreasing values.
#      when the current value is bigger than the value at the stack top,
#      that top's "next greater" is the current value -> pop and fill it in.
#
#   whatever is left on the stack at the end has no next greater -> stays 0.
#
# time = O(n)
# space = O(n)
class Solution(object):
    def nextLargerNodes(self, head):
        vals = []
        node = head
        while node:
            vals.append(node.val)
            node = node.next

        res = [0] * len(vals)
        stack = []  # indices, values are decreasing from bottom to top
        for i, v in enumerate(vals):
            while stack and vals[stack[-1]] < v:
                res[stack.pop()] = v
            stack.append(i)
        return res
