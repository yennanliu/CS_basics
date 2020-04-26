# V0 
# IDEA : MATRIX IN ORDER + BRUTE FORCE
# Space: O(1)
# Time:  O(m+n) # worst case
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

# V0'
# IDEA : BINARY SEARCH
# Space: O(1)
# Time:  O(logm + logn)
class Solution(object):
    def searchMatrix(self, matrix, target):
        """
        :type matrix: List[List[int]]
        :type target: int
        :rtype: bool
        """
        if not matrix:
            return False

        m, n = len(matrix), len(matrix[0])
        left, right = 0, m * n
        while left < right:
            mid = left + (right - left) / 2
            if matrix[int(mid / n)][int(mid % n)]>= target:
                right = mid
            else:
                left = mid + 1
        return left < m * n and matrix[int(left / n)][int(left % n)] == target

# V1
# https://leetcode.com/problems/search-a-2d-matrix/discuss/351404/Python-Simple-Solution
class Solution:
    def searchMatrix(self, matrix, target):
        if len(matrix) == 0:
            return False

        row, col = 0, len(matrix[0]) - 1
        
        while row < len(matrix) and col >= 0:
            if matrix[row][col] == target: return True
            elif matrix[row][col] < target: row += 1
            elif matrix[row][col] > target: col -= 1
        
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
# https://leetcode.com/problems/search-a-2d-matrix/discuss/592696/python-super-easy
# IDEA : BRUTE FORCE
class Solution:
    def searchMatrix(self, matrix: List[List[int]], target: int) -> bool:
        for x in matrix:
            if target in x:
                return True
        return False

# V1''
# https://blog.csdn.net/fuxuemingzhu/article/details/79459314
# https://blog.csdn.net/fuxuemingzhu/article/details/79459200
class Solution(object):
    def searchMatrix(self, matrix, target):
        """
        :type matrix: List[List[int]]
        :type target: int
        :rtype: bool
        """
        if not matrix or not matrix[0]:
            return False
        rows = len(matrix)
        cols = len(matrix[0])
        row, col = 0, cols - 1
        while True:
            if row < rows and col >= 0:
                if matrix[row][col] == target:
                    return True
                elif matrix[row][col] < target:
                    row += 1
                else:
                    col -= 1
            else:
                return False

# V1' 
# https://blog.csdn.net/fuxuemingzhu/article/details/79459314
# https://blog.csdn.net/fuxuemingzhu/article/details/79459200
class Solution(object):
    def searchMatrix(self, matrix, target):
        """
        :type matrix: List[List[int]]
        :type target: int
        :rtype: bool
        """
        return any(target in row for row in matrix)
        
# V2 
# IDEA : BINARY SEARCH
# Space: O(1)
# Time:  O(logm + logn)
class Solution(object):
    def searchMatrix(self, matrix, target):
        """
        :type matrix: List[List[int]]
        :type target: int
        :rtype: bool
        """
        if not matrix:
            return False

        m, n = len(matrix), len(matrix[0])
        left, right = 0, m * n
        while left < right:
            mid = left + (right - left) / 2
            if matrix[int(mid / n)][int(mid % n)]>= target:
                right = mid
            else:
                left = mid + 1
        return left < m * n and matrix[int(left / n)][int(left % n)] == target