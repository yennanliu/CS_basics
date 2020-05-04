# Write an efficient algorithm that searches for a value in an m x n matrix. This matrix has the following properties:
#
# Integers in each row are sorted in ascending from left to right.
# Integers in each column are sorted in ascending from top to bottom.
# For example,
#
# Consider the following matrix:
#
# [
#   [1,   4,  7, 11, 15],
#   [2,   5,  8, 12, 19],
#   [3,   6,  9, 16, 22],
#   [10, 13, 14, 17, 24],
#   [18, 21, 23, 26, 30]
# ]
# Given target = 5, return true.
#
# Given target = 20, return false.

# V0 
class Solution:
    def searchMatrix(self, matrix, target):
        if len(matrix) == 0:
            return False        
        row, col = 0, len(matrix[0]) - 1
        while row < len(matrix) and col >= 0:
            if matrix[row][col] == target: 
                return True
            elif matrix[row][col] < target: 
                row += 1
            elif matrix[row][col] > target: 
                col -= 1
        return False

# V1
# SAME AS #74 Search a 2D Matrix
class Solution:
    def searchMatrix(self, matrix, target):
        if len(matrix) == 0:
            return False        
        row, col = 0, len(matrix[0]) - 1
        while row < len(matrix) and col >= 0:
            if matrix[row][col] == target: 
                return True
            elif matrix[row][col] < target: 
                row += 1
            elif matrix[row][col] > target: 
                col -= 1
        return False

### Test case
s=Solution()
assert s.searchMatrix([[1,2,3],[4,5,6],[7,8,9]], 9) == True
assert s.searchMatrix([[1,2,3],[4,5,6],[7,8,9]], 1) == True
assert s.searchMatrix([[1,2,3],[4,5,6],[7,8,9]], 99) == False
assert s.searchMatrix([[]], 0) == False
assert s.searchMatrix([[]], 100) == False
assert s.searchMatrix([], 100) == False
assert s.searchMatrix([[-1,3,4,-4]], -1) == False
assert s.searchMatrix([[_ for _ in range(3)] for _ in range(4)], -1) == False
assert s.searchMatrix([[_ for _ in range(3)] for _ in range(4)], 2) == True
assert s.searchMatrix([[_ for _ in range(99)] for _ in range(999)], 2) == True

# V1'
# http://bookshadow.com/weblog/2015/07/23/leetcode-search-2d-matrix-ii/
# IDEA : GO THROUGH THE MATRIX 
class Solution:
    # @param {integer[][]} matrix
    # @param {integer} target
    # @return {boolean}
    def searchMatrix(self, matrix, target):
        y = len(matrix[0]) - 1
        for x in range(len(matrix)):
            while y and matrix[x][y] > target:
                y -= 1
            if matrix[x][y] == target:
                return True
        return False

# V1''
# http://bookshadow.com/weblog/2015/07/23/leetcode-search-2d-matrix-ii/
# IDEA : BINARY SEARCH
class Solution:
    # @param {integer[][]} matrix
    # @param {integer} target
    # @return {boolean}
    def searchMatrix(self, matrix, target):
        y = len(matrix[0]) - 1
        def binSearch(nums, low, high):
            while low <= high:
                mid = (low + high) / 2
                if nums[mid] > target:
                    high = mid - 1
                else:
                    low = mid + 1
            return high
        for x in range(len(matrix)):
            y = binSearch(matrix[x], 0, y)
            if matrix[x][y] == target:
                return True
        return False 

# V1'''
# https://blog.csdn.net/fuxuemingzhu/article/details/79459314
# IDEA : GREEDY 
class Solution(object):
    def searchMatrix(self, matrix, target):
        """
        :type matrix: List[List[int]]
        :type target: int
        :rtype: bool
        """
        return any(target in row for row in matrix)
        
# V2 
# Time:  O(m + n)
# Space: O(1)
class Solution(object):
    # @param {integer[][]} matrix
    # @param {integer} target
    # @return {boolean}
    def searchMatrix(self, matrix, target):
        m = len(matrix)
        if m == 0:
            return False
        n = len(matrix[0])
        if n == 0:
            return False
        i, j = 0, n - 1
        while i < m and j >= 0:
            if matrix[i][j] == target:
                return True
            elif matrix[i][j] > target:
                j -= 1
            else:
                i += 1
        return False