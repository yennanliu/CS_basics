"""

378. Kth Smallest Element in a Sorted Matrix
Solved
Medium
Topics
premium lock icon
Companies
Given an n x n matrix where each of the rows and columns is sorted in ascending order, return the kth smallest element in the matrix.

Note that it is the kth smallest element in the sorted order, not the kth distinct element.

You must find a solution with a memory complexity better than O(n2).

 

Example 1:

Input: matrix = [[1,5,9],[10,11,13],[12,13,15]], k = 8
Output: 13
Explanation: The elements in the matrix are [1,5,9,10,11,12,13,13,15], and the 8th smallest number is 13
Example 2:

Input: matrix = [[-5]], k = 1
Output: -5
 

Constraints:

n == matrix.length == matrix[i].length
1 <= n <= 300
-109 <= matrix[i][j] <= 109
All the rows and columns of matrix are guaranteed to be sorted in non-decreasing order.
1 <= k <= n2
 

Follow up:

Could you solve the problem with a constant memory (i.e., O(1) memory complexity)?
Could you solve the problem in O(n) time complexity? The solution may be too advanced for an interview but you may find reading this paper fun.


"""


# V0
# IDEA: binary search + MATRIX (gpt)
# time = O(n log(max-min)), n = matrix dimension (countLessEqual is O(n) per binary search step)
# space = O(1)
class Solution(object):
    def kthSmallest(self, matrix, k):
        # Number of rows (the matrix is n x n).
        n = len(matrix)

        # The smallest possible value is the top-left element.
        left = matrix[0][0]

        # The largest possible value is the bottom-right element.
        right = matrix[n - 1][n - 1]

        # Continue binary searching until left and right meet.
        while left < right:

            # Compute the middle value.
            mid = left + (right - left) // 2

            # Count how many numbers in the matrix are <= mid.
            count = self.countLessEqual(matrix, mid)

            # If there are fewer than k numbers <= mid,
            # the kth smallest must be larger.
            if count < k:
                left = mid + 1
            else:
                # Otherwise, mid could still be the answer,
                # so continue searching the left half.
                right = mid

        # When left == right, we have found the kth smallest value.
        return left

    def countLessEqual(self, matrix, target):
        # Matrix dimension.
        n = len(matrix)

        # Start from the bottom-left corner.
        row = n - 1
        col = 0

        # Number of elements <= target.
        count = 0

        # Continue while we remain inside the matrix.
        while row >= 0 and col < n:

            # If current value is <= target,
            # then every value above it in this column
            # is also <= target because columns are sorted.
            if matrix[row][col] <= target:

                # Add all elements from row 0 to current row.
                count += row + 1

                # Move to the next column.
                col += 1

            else:
                # Current value is too large.
                # Move upward to find smaller values.
                row -= 1

        return count


# V0-1
# IDEA: binary search (gemini)
# time = O(n log(max-min)), n = matrix dimension
# space = O(1)
class Solution(object):
    def kthSmallest(self, matrix, k):
        n = len(matrix)
        low, high = matrix[0][0], matrix[n-1][n-1]
        
        # Helper function to count how many elements are less than or equal to 'mid'
        def countLessOrEqual(mid):
            count = 0
            col = n - 1  # Start at the top-right corner
            for row in range(n):
                while col >= 0 and matrix[row][col] > mid:
                    col -= 1
                count += (col + 1)
            return count

        while low < high:
            mid = low + (high - low) // 2
            if countLessOrEqual(mid) < k:
                low = mid + 1
            else:
                high = mid
                
        return low


# V0-2
# IDEA: BRUTE FORCE
# time = O(n^2 log n), n = matrix dimension (flatten + sort)
# space = O(n^2)
class Solution(object):
    def kthSmallest(self, matrix, k):
        if not matrix or len(matrix) == 0 or len(matrix[0]) == 0:
            return -1
        list = []
        l = len(matrix)
        w = len(matrix[0])
        # ??
        if l * w == 1:
            return matrix[0][0]

        for y in range(l):
            for x in range(w):
                list.append(matrix[y][x])
        
        list.sort()
        #print("after sort, list = " + str(list))

        return list[k-1]

# V1
# http://bookshadow.com/weblog/2016/08/01/leetcode-kth-smallest-element-in-a-sorted-matrix/
# time = O(k log k) (heap holds up to O(k) entries)
# space = O(m*n) (visited matrix)
class Solution(object):
    def kthSmallest(self, matrix, k):
        """
        :type matrix: List[List[int]]
        :type k: int
        :rtype: int
        """
        m, n = len(matrix), len(matrix[0])
        visited = [[False] * n for _ in range(m)]
        q = [(matrix[0][0], 0, 0)]
        ans = None
        visited[0][0] = True
        for _ in range(k):
            ans, i, j = heapq.heappop(q)
            if i + 1 < m and not visited[i + 1][j]:
                visited[i + 1][j] = True
                heapq.heappush(q, (matrix[i + 1][j], i + 1, j))
            if j + 1 < n and not visited[i][j + 1]:
                visited[i][j + 1] = True
                heapq.heappush(q, (matrix[i][j + 1], i, j + 1))
        return ans

# V1'
# http://bookshadow.com/weblog/2016/08/01/leetcode-kth-smallest-element-in-a-sorted-matrix/
# time = O(k log k)
# space = O(k)
class Solution(object):
    def kthSmallest(self, matrix, k):
        """
        :type matrix: List[List[int]]
        :type k: int
        :rtype: int
        """
        m, n = len(matrix), len(matrix[0])
        q = [(matrix[0][0], 0, 0)]
        ans = None
        for _ in range(k):
            ans, i, j = heapq.heappop(q)
            if j == 0 and i + 1 < m:
                heapq.heappush(q, (matrix[i + 1][j], i + 1, j))
            if j + 1 < n:
                heapq.heappush(q, (matrix[i][j + 1], i, j + 1))
        return ans

# V1''
# http://bookshadow.com/weblog/2016/08/01/leetcode-kth-smallest-element-in-a-sorted-matrix/
# time = O(n log n * log(max-min)), n = matrix dimension (bisect over each row per binary search step)
# space = O(1)
class Solution(object):
    def kthSmallest(self, matrix, k):
        """
        :type matrix: List[List[int]]
        :type k: int
        :rtype: int
        """
        lo, hi = matrix[0][0], matrix[-1][-1]
        while lo <= hi:
            mid = (lo + hi) >> 1
            loc = sum(bisect.bisect_right(m, mid) for m in matrix)
            if loc >= k:
                hi = mid - 1
            else:
                lo = mid + 1
        return lo

# V1'''
# http://bookshadow.com/weblog/2016/08/01/leetcode-kth-smallest-element-in-a-sorted-matrix/
# time = O(n log(max-min)), n = matrix dimension (countLower is O(n) per binary search step)
# space = O(1)
class Solution(object):
    def kthSmallest(self, matrix, k):
        """
        :type matrix: List[List[int]]
        :type k: int
        :rtype: int
        """
        lo, hi = matrix[0][0], matrix[-1][-1]
        while lo <= hi:
            mid = (lo + hi) >> 1
            loc = self.countLower(matrix, mid)
            if loc >= k:
                hi = mid - 1
            else:
                lo = mid + 1
        return lo

    def countLower(self, matrix, num):
        i, j = len(matrix) - 1, 0
        cnt = 0
        while i >= 0 and j < len(matrix[0]):
            if matrix[i][j] <= num:
                cnt += i + 1
                j += 1
            else:
                i -= 1
        return cnt
        
# V2
# time = O(k * log(min(n, m, k))), with n x m matrix
# space = O(min(n, m, k))
from heapq import heappush, heappop
class Solution(object):
    def kthSmallest(self, matrix, k):
        """
        :type matrix: List[List[int]]
        :type k: int
        :rtype: int
        """
        kth_smallest = 0
        min_heap = []

        def push(i, j):
            if len(matrix) > len(matrix[0]):
                if i < len(matrix[0]) and j < len(matrix):
                    heappush(min_heap, [matrix[j][i], i, j])
            else:
                if i < len(matrix) and j < len(matrix[0]):
                    heappush(min_heap, [matrix[i][j], i, j])

        push(0, 0)
        while min_heap and k > 0:
            kth_smallest, i, j = heappop(min_heap)
            push(i, j + 1)
            if j == 0:
                push(i + 1, 0)
            k -= 1

        return kth_smallest
