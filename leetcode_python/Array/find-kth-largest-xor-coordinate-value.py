"""

1738. Find Kth Largest XOR Coordinate Value
Medium

You are given a 2D matrix of size m x n, consisting of non-negative integers. You are also given an integer k.

The value of coordinate (a, b) of the matrix is the XOR of all matrix[i][j] where 0 <= i <= a < m and 0 <= j <= b < n (0-indexed).

Find the kth largest value (1-indexed) of all the coordinates of matrix.


Example 1:

Input: matrix = [[5,2],[1,6]], k = 1
Output: 7
Explanation: The value of coordinate (0,1) is 5 XOR 2 = 7, which is the largest value.

Example 2:

Input: matrix = [[5,2],[1,6]], k = 2
Output: 5
Explanation: The value of coordinate (0,0) is 5 = 5, which is the 2nd largest value.

Example 3:

Input: matrix = [[5,2],[1,6]], k = 3
Output: 4
Explanation: The value of coordinate (1,0) is 5 XOR 1 = 4, which is the 3rd largest value.


Constraints:

m == matrix.length
n == matrix[i].length
1 <= m, n <= 1000
0 <= matrix[i][j] <= 10^6
1 <= k <= m * n

"""

# V0
# IDEA : 2D PREFIX XOR + SORT
#
#   inclusion-exclusion works for XOR just like for sums, except that the
#   "subtract the double-counted corner" step is itself an XOR (a ^ a = 0):
#
#     s[i][j] = s[i-1][j] ^ s[i][j-1] ^ s[i-1][j-1] ^ matrix[i-1][j-1]
#
#   both s[i-1][j] and s[i][j-1] contain the s[i-1][j-1] block, so XOR-ing
#   s[i-1][j-1] back in once cancels the duplicate copy exactly.
#
#   compute all m*n coordinate values, sort, take the kth from the end.
#
#   NOTE : only the PREVIOUS row is ever needed, so keep two rolling rows
#          instead of the full (m+1) x (n+1) table.
#
# time = O(m * n * log(m * n)), space = O(m * n) for the value list
class Solution(object):
    def kthLargestValue(self, matrix, k):
        m, n = len(matrix), len(matrix[0])

        vals = []
        prev = [0] * (n + 1)
        for i in range(m):
            cur = [0] * (n + 1)
            row = matrix[i]
            for j in range(n):
                cur[j + 1] = cur[j] ^ prev[j + 1] ^ prev[j] ^ row[j]
                vals.append(cur[j + 1])
            prev = cur

        vals.sort()
        return vals[len(vals) - k]
