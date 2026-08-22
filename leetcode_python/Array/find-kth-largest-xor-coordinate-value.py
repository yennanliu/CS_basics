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


# V0-1
# IDEA : ROW PREFIX XOR + RUNNING COLUMN XOR + MIN-HEAP OF SIZE K
#
#   the 2D prefix XOR does not need the inclusion-exclusion recurrence at all:
#   it factors into two independent 1D passes. XOR-prefix each row on its own
#   (`run`), then fold every row into the column accumulator (`col`), so after
#   visiting row i the cell col[j] already holds the value of coordinate
#   (i, j) = XOR of the whole top-left rectangle.
#
#   and we never need every value ordered, only the kth largest -> keep a
#   MIN-heap capped at k. its root is the kth largest seen so far, which is
#   exactly the answer once all m*n values have been offered.
#
# time = O(m * n * log k), space = O(n + k)
import heapq
class Solution(object):
    def kthLargestValue(self, matrix, k):
        m, n = len(matrix), len(matrix[0])
        col = [0] * n                       # XOR of the rows above, per column
        heap = []
        for i in range(m):
            run = 0
            row = matrix[i]
            for j in range(n):
                run ^= row[j]               # prefix XOR inside this row
                col[j] ^= run               # fold in all rows seen so far
                v = col[j]
                if len(heap) < k:
                    heapq.heappush(heap, v)
                elif v > heap[0]:
                    heapq.heapreplace(heap, v)
        return heap[0]


# V0-2
# IDEA : IN-PLACE PREFIX XOR + QUICKSELECT (no sort, no heap)
#
#   overwrite the matrix itself with its 2D prefix XOR — in row-major order
#   the three cells the recurrence reads (up, left, up-left) are already
#   finalised, so no extra table is needed (the input is mutated).
#
#   then pick the kth largest by SELECTION instead of sorting: quickselect
#   with a random pivot and a 3-way (Dutch-flag) partition, which is O(N)
#   expected. the 3-way split matters here because XOR values repeat a lot,
#   and a 2-way partition degrades to O(N^2) on heavy duplicates.
#   kth largest == index N - k of the ascending order.
#
# time = O(m * n) expected, space = O(m * n) for the flattened values
import random
class Solution(object):
    def kthLargestValue(self, matrix, k):
        m, n = len(matrix), len(matrix[0])
        for i in range(m):
            for j in range(n):
                if i:
                    matrix[i][j] ^= matrix[i - 1][j]
                if j:
                    matrix[i][j] ^= matrix[i][j - 1]
                if i and j:
                    matrix[i][j] ^= matrix[i - 1][j - 1]

        vals = [v for row in matrix for v in row]
        target = len(vals) - k              # 0-based rank in ASCENDING order
        lo, hi = 0, len(vals) - 1
        while lo < hi:
            pivot = vals[random.randint(lo, hi)]
            i, j, t = lo, hi, lo
            while t <= j:                   # [lo,i) < pivot, (j,hi] > pivot
                if vals[t] < pivot:
                    vals[i], vals[t] = vals[t], vals[i]
                    i += 1
                    t += 1
                elif vals[t] > pivot:
                    vals[t], vals[j] = vals[j], vals[t]
                    j -= 1
                else:
                    t += 1
            if target < i:
                hi = i - 1
            elif target > j:
                lo = j + 1
            else:
                return pivot
        return vals[lo]
