"""

1981. Minimize the Difference Between Target and Chosen Elements
Medium

You are given an m x n integer matrix mat and an integer target.

Choose one integer from each row in the matrix such that the absolute difference between target and the sum of the chosen elements is minimized.

Return the minimum absolute difference.

The absolute difference between two numbers a and b is the absolute value of a - b.


Example 1:

Input: mat = [[1,2,3],[4,5,6],[7,8,9]], target = 13
Output: 0
Explanation: One possible choice is to:
- Choose 1 from the first row.
- Choose 5 from the second row.
- Choose 7 from the third row.
The sum of the chosen elements is 13, which equals the target, so the absolute difference is 0.

Example 2:

Input: mat = [[1],[2],[3]], target = 100
Output: 94
Explanation: The best possible choice is to:
- Choose 1 from the first row.
- Choose 2 from the second row.
- Choose 3 from the third row.
The sum of the chosen elements is 6, and the absolute difference is 94.

Example 3:

Input: mat = [[1,2,9,8,7]], target = 6
Output: 1
Explanation: The best choice is to choose 7 from the first row.
The absolute difference is 1.


Constraints:

m == mat.length
n == mat[i].length
1 <= m, n <= 70
1 <= mat[i][j] <= 70
1 <= target <= 800

"""

# V0
# IDEA : GROUPED KNAPSACK (reachable-sum bitset, one group per row)
#
#   we only care about WHICH sums are reachable, not how many ways.
#   keep the reachable set as a python big-int bitmask :
#       bit j of `mask` is 1  <=>  sum j is reachable
#   picking value x from the next row shifts every reachable sum by x, so
#       mask = OR over x in row of (mask << x)
#
#   dedupe each row first - repeated values add nothing.
#   the total sum is at most 70 * 70 = 4900, so the mask stays tiny.
#
#   NOTE : the answer may overshoot the target (example 2 undershoots,
#          example 3 overshoots), so scan every reachable sum at the end.
#
# time = O(m * n * S / 64) with S = max total sum, space = O(S)
class Solution(object):
    def minimizeTheDifference(self, mat, target):
        mask = 1  # only sum 0 is reachable before we start
        for row in mat:
            nxt = 0
            for x in set(row):
                nxt |= mask << x
            mask = nxt

        res = float("inf")
        total = mask.bit_length()
        for j in range(total):
            if (mask >> j) & 1:
                res = min(res, abs(j - target))
        return res
