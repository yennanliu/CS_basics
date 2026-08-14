"""

1439. Find the Kth Smallest Sum of a Matrix With Sorted Rows
Hard

You are given an m x n matrix mat that has its rows sorted in non-decreasing
order and an integer k.

You are allowed to choose exactly one element from each row to form an array.

Return the kth smallest array sum among all possible arrays.


Example 1:

Input: mat = [[1,3,11],[2,4,6]], k = 5
Output: 7
Explanation: Choosing one element from each row, the first k smallest sum are:
[1,2], [1,4], [3,2], [3,4], [1,6]. Where the 5th sum is 7.

Example 2:

Input: mat = [[1,3,11],[2,4,6]], k = 9
Output: 17

Example 3:

Input: mat = [[1,10,10],[1,4,5],[2,3,6]], k = 7
Output: 9
Explanation: Choosing one element from each row, the first k smallest sum are:
[1,1,2], [1,1,3], [1,4,2], [1,4,3], [1,1,6], [1,5,2], [1,5,3]. Where the 7th
sum is 9.


Constraints:

m == mat.length
n == mat.length[i]
1 <= m, n <= 40
1 <= mat[i][j] <= 5000
1 <= k <= min(200, n^m)
mat[i] is a non-decreasing array.

"""

# V0
# IDEA : ROW BY ROW MERGE, KEEP ONLY THE k SMALLEST
#
#  -> fold the rows one at a time:
#       pre = the k smallest sums using the rows seen so far
#       pre = k smallest of { a + b : a in pre, b in row }
#  -> only the first k entries of each row can ever matter
#     (the row is sorted), so slice row[:k]
#
# time = O(m * k * n log(k * n))
# space = O(k * n)
class Solution(object):
    def kthSmallest(self, mat, k):
        pre = [0]
        for row in mat:
            cur = []
            for a in pre:
                for b in row[:k]:
                    cur.append(a + b)
            cur.sort()
            pre = cur[:k]
        return pre[-1]


# V1
# IDEA : MIN HEAP + BFS ON INDEX TUPLES
#
#  -> start from picking column 0 of every row (the smallest possible sum)
#  -> pop the smallest sum, then push its m "advance one row by one column"
#     neighbours; the k-th pop is the answer
#  -> a visited set keeps each index tuple from being pushed twice
#
# time = O(k * m log(k * m))
# space = O(k * m)
import heapq
class Solution(object):
    def kthSmallest(self, mat, k):
        m = len(mat)
        n = len(mat[0])

        start = tuple([0] * m)
        base = sum(row[0] for row in mat)

        heap = [(base, start)]
        seen = {start}

        for _ in range(k - 1):
            total, idx = heapq.heappop(heap)
            for i in range(m):
                if idx[i] + 1 < n:
                    nxt = list(idx)
                    nxt[i] += 1
                    nxt = tuple(nxt)
                    if nxt not in seen:
                        seen.add(nxt)
                        heapq.heappush(
                            heap,
                            (total - mat[i][idx[i]] + mat[i][nxt[i]], nxt),
                        )

        return heap[0][0]
