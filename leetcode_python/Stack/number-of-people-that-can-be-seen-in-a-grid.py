"""

2282. Number of People That Can Be Seen in a Grid
Medium
(premium / locked problem)

You are given an m x n 0-indexed 2D array of positive integers heights where heights[i][j] is the height of the person standing at position (i, j).

A person standing at position (row1, col1) can see a person standing at position (row2, col2) if:

The person at (row2, col2) is to the right or below the person at (row1, col1). More formally, this means that either row1 == row2 and col1 < col2 or row1 < row2 and col1 == col2.
Everyone in between them is shorter than both of them.

Return an m x n 2D array of integers answer where answer[i][j] is the number of people that the person at position (i, j) can see.


Example 1:

Input: heights = [[3,1,4,2,5]]
Output: [[2,1,2,1,0]]
Explanation:
- The person at (0, 0) can see the people at (0, 1) and (0, 2).
  Note that he cannot see the person at (0, 4) because the person at (0, 2) is taller than him.
- The person at (0, 1) can see the person at (0, 2).
- The person at (0, 2) can see the people at (0, 3) and (0, 4).
- The person at (0, 3) can see the person at (0, 4).
- The person at (0, 4) cannot see anybody.

Example 2:

Input: heights = [[5,1],[3,1],[4,1]]
Output: [[3,1],[2,1],[1,0]]
Explanation:
- The person at (0, 0) can see the people at (0, 1), (1, 0) and (2, 0).
- The person at (0, 1) can see the person at (1, 1).
- The person at (1, 0) can see the people at (1, 1) and (2, 0).
- The person at (1, 1) can see the person at (2, 1).
- The person at (2, 0) can see the person at (2, 1).
- The person at (2, 1) cannot see anybody.


Constraints:

1 <= heights.length <= 400
1 <= heights[i].length <= 400
1 <= heights[i][j] <= 10^5

"""

# V0
# IDEA : MONOTONIC DECREASING STACK, ONCE PER ROW AND ONCE PER COLUMN
#
#   the two directions (right / down) are independent, so solve one line at a
#   time and add the results.
#
#   scanning a line BACKWARDS with a non-increasing stack, for the current
#   person of height h :
#     - every stacked person strictly SHORTER than h is visible and then gets
#       blocked by h for anyone further back -> pop and count each
#     - the first person who is >= h is also visible (one more), but blocks
#       everything beyond, so the popping stops there
#     - if that blocker is EXACTLY equal to h, replace it : the two are
#       interchangeable as blockers, and keeping both would let a later
#       person count the same wall twice
#
# time = O(m * n), space = O(max(m, n))
class Solution(object):
    def seePeople(self, heights):
        m, n = len(heights), len(heights[0])
        res = [[0] * n for _ in range(m)]

        def scan(line):
            """how many people each position of `line` can see, looking forward"""
            counts = [0] * len(line)
            stack = []
            for idx in range(len(line) - 1, -1, -1):
                h = line[idx]
                cnt = 0
                while stack and stack[-1] < h:
                    stack.pop()
                    cnt += 1
                if stack:
                    cnt += 1              # the blocker itself is visible
                    if stack[-1] == h:
                        stack.pop()       # equal heights block identically
                stack.append(h)
                counts[idx] = cnt
            return counts

        for i in range(m):
            for j, c in enumerate(scan(heights[i])):
                res[i][j] += c

        for j in range(n):
            col = [heights[i][j] for i in range(m)]
            for i, c in enumerate(scan(col)):
                res[i][j] += c

        return res
