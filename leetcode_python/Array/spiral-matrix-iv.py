"""

2326. Spiral Matrix IV
Medium

You are given two integers m and n, which represent the dimensions of a matrix.

You are also given the head of a linked list of integers.

Generate an m x n matrix that contains the integers in the linked list presented in spiral order (clockwise), starting from the top-left of the matrix. If there are remaining empty spaces, fill them with -1.

Return the generated matrix.


Example 1:

Input: m = 3, n = 5, head = [3,0,2,6,8,1,7,9,4,2,5,5,0]
Output: [[3,0,2,6,8],[5,0,-1,-1,1],[5,2,4,9,7]]
Explanation: The diagram above shows how the values are printed in the matrix.
Note that the remaining spaces in the matrix are filled with -1.

Example 2:

Input: m = 1, n = 4, head = [0,1,2]
Output: [[0,1,2,-1]]
Explanation: The diagram above shows how the values are printed from left to right in the matrix.
The last space in the matrix is set to -1.


Constraints:

1 <= m, n <= 10^5
1 <= m * n <= 10^5
The number of nodes in the list is in the range [1, m * n].
0 <= Node.val <= 1000

"""

# V0
# IDEA : WALK THE SPIRAL WITH FOUR SHRINKING BOUNDARIES
#
#   fill the matrix with -1 first, then traverse it in clockwise spiral order
#   consuming one list node per cell until the list runs out.
#
#   the four boundaries (top, bottom, left, right) close in after each leg.
#   the two guards inside the loop (`if top <= bottom` before the leftward
#   leg, `if left <= right` before the upward leg) are what stop a single
#   remaining row or column from being visited twice.
#
# time = O(m * n), space = O(m * n) for the output
# Definition for singly-linked list.
# class ListNode(object):
#     def __init__(self, val=0, next=None):
#         self.val = val
#         self.next = next
class Solution(object):
    def spiralMatrix(self, m, n, head):
        res = [[-1] * n for _ in range(m)]
        top, bottom, left, right = 0, m - 1, 0, n - 1
        node = head

        while node and top <= bottom and left <= right:
            for c in range(left, right + 1):
                if not node:
                    break
                res[top][c] = node.val
                node = node.next
            top += 1

            for r in range(top, bottom + 1):
                if not node:
                    break
                res[r][right] = node.val
                node = node.next
            right -= 1

            if top <= bottom:
                for c in range(right, left - 1, -1):
                    if not node:
                        break
                    res[bottom][c] = node.val
                    node = node.next
                bottom -= 1

            if left <= right:
                for r in range(bottom, top - 1, -1):
                    if not node:
                        break
                    res[r][left] = node.val
                    node = node.next
                left += 1

        return res
