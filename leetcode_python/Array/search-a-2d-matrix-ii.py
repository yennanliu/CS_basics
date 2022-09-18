"""

240. Search a 2D Matrix II
Medium

Write an efficient algorithm that searches for a target value in an m x n integer matrix. The matrix has the following properties:

Integers in each row are sorted in ascending from left to right.
Integers in each column are sorted in ascending from top to bottom.
 

Example 1:


Input: matrix = [[1,4,7,11,15],[2,5,8,12,19],[3,6,9,16,22],[10,13,14,17,24],[18,21,23,26,30]], target = 5
Output: true
Example 2:


Input: matrix = [[1,4,7,11,15],[2,5,8,12,19],[3,6,9,16,22],[10,13,14,17,24],[18,21,23,26,30]], target = 20
Output: false
 

Constraints:

m == matrix.length
n == matrix[i].length
1 <= n, m <= 300
-109 <= matrix[i][j] <= 109
All the integers in each row are sorted in ascending order.
All the integers in each column are sorted in ascending order.
-109 <= target <= 109

"""

# V0
# IDEA : 2D array op + matrix properties (this problem only)
## NOTE :
# The matrix has the following properties:
# Integers in each row are sorted in ascending from left to right.
# Integers in each column are sorted in ascending from top to bottom.
### NOTE : 
#    row : --------->
#    cols : ⬇
#           ⬇    
class Solution:
    def searchMatrix(self, matrix, target):
        if len(matrix) == 0:
            return False
        ### NOTE : col starts from len(matrix[0]) - 1; while row starts from 0     
        row, col = 0, len(matrix[0]) - 1
        ### NOTE : since row = 0 at first, so col >= 0
        #          row = len(matrix[0]) - 1, so  row < len(matrix)
        while row < len(matrix) and col >= 0:
            # 3 cases
            # case 1) matrix[row][col] == target -> find the target
            if matrix[row][col] == target: 
                return True
            # case 2) matrix[row][col] < target -> move to next row
            elif matrix[row][col] < target: 
                row += 1
            # case 3) matrix[row][col] > target -> move to next col
            elif matrix[row][col] > target: 
                col -= 1
        return False

# V0'
# IDEA : py array op (may not acceptable to interviewer)
class Solution(object):
    def searchMatrix(self, matrix, target):
        # edge case
        if not matrix:
            return False
        l = len(matrix) - 1
        w = len(matrix[0]) - 1
        i = j = 0
        while i < l:
            if target in matrix[i]:
                return True
            i += 1
        return target in matrix[l]

# V0''
# IDEA : DFS (brute force)
class Solution(object):
    def searchMatrix(self, matrix, target):
        def dfs(matrix, target, x, y):
            if matrix[y][x] == target:
                res.append(True)
            matrix[y][x] = "#"
            moves = [[0,1],[0,-1],[1,0],[-1,0]]
            for move in moves:
                _x = x + move[1]
                _y = y + move[0]
                #print ("_x = " + str(_x) + " _y = " + str(_y))
                if 0 <= _x < w and 0 <= _y < l:
                    if matrix[_y][_x] != "#":
                        dfs(matrix, target, _x, _y)

        if not matrix:
            return False
        l = len(matrix)
        w = len(matrix[0])
        res = []
        dfs(matrix, target, 0, 0)
        return True in res

# V0'''
# IDEA : BINARY SEARCH
class Solution:
    def searchMatrix(self, matrix, target):

        def binSearch(nums, low, high):
            while low <= high:
                mid = (low + high) // 2
                if nums[mid] > target:
                    high = mid - 1
                else:
                    low = mid + 1
            return high

        y = len(matrix[0]) - 1
        for x in range(len(matrix)):
            ### NOTE : the binSearch here is searching between 0 and y
            y = binSearch(matrix[x], 0, y)
            if matrix[x][y] == target:
                return True
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
                mid = (low + high) // 2
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