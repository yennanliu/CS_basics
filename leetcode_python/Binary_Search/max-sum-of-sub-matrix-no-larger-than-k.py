"""

363. Max Sum of Rectangle No Larger Than K
Hard

Given an m x n matrix matrix and an integer k, return the max sum of a rectangle in the
matrix such that its sum is no larger than k.

It is guaranteed that there will be a rectangle with a sum no larger than k.


Example 1:

Input: matrix = [[1,0,1],[0,-2,3]], k = 2
Output: 2
Explanation: Because the sum of the blue rectangle [[0, 1], [-2, 3]] is 2, and 2 is the
max number no larger than k (k = 2).

Example 2:

Input: matrix = [[2,2,-1]], k = 3
Output: 3


Constraints:

m == matrix.length
n == matrix[i].length
1 <= m, n <= 100
-100 <= matrix[i][j] <= 100
-10^5 <= k <= 10^5


Follow up: What if the number of rows is much larger than the number of columns?

"""

# V0
# IDEA : ENUMERATE ROW BOUNDARIES -> 1D "max subarray sum <= k" via SORTED PREFIX SUMS
#
#  Step 1: fix a top row and a bottom row, collapsing those rows into a single 1D
#          array `col_sum` (column-wise sums). Every rectangle with those boundaries
#          is now a subarray of col_sum.
#
#  Step 2: for the 1D array, a subarray sum is prefix[j] - prefix[i] (i < j).
#          We want the LARGEST value <= k, i.e. we want the SMALLEST earlier prefix
#          `p` satisfying  cur - p <= k   <=>   p >= cur - k.
#          Keeping earlier prefixes in a sorted list, bisect_left finds it in O(log n).
#
#  NOTE: a plain Kadane does NOT work here - the constraint is "<= k", not "maximum".
#
# time  = O(m^2 * n * log n)
# space = O(n)
from bisect import bisect_left, insort
class Solution(object):
    def maxSumSubmatrix(self, matrix, k):
        if not matrix or not matrix[0]:
            return 0

        m, n = len(matrix), len(matrix[0])
        best = float("-inf")

        for top in range(m):
            col_sum = [0] * n
            for bottom in range(top, m):
                # extend the band downward by one row
                for j in range(n):
                    col_sum[j] += matrix[bottom][j]

                # 1D pass: max subarray sum of col_sum that is <= k
                prefixes = [0]  # sorted list of prefix sums seen so far
                cur = 0
                for v in col_sum:
                    cur += v
                    i = bisect_left(prefixes, cur - k)
                    if i < len(prefixes):
                        best = max(best, cur - prefixes[i])
                    insort(prefixes, cur)

                if best == k:  # cannot do better than k
                    return k

        return best
