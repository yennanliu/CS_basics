# Given an integer n, generate a square matrix filled with elements from 1 to n2 in spiral order.
#
# For example,
# Given n = 3,
#
# You should return the following matrix:
# [
#  [ 1, 2, 3 ],
#  [ 8, 9, 4 ],
#  [ 7, 6, 5 ]
# ]

# V0 
class Solution:
    def generateMatrix(self, n):
        # Build n by n matrix
        matrix = [[None] * n for row in range(n)]
        min_row, max_row, min_column, max_column = 0, n - 1, 0, n - 1
        spiral_element = 1
        # while not go through all elements in matrix
        while spiral_element <= n ** 2:
            # right
            for column in range(min_column, max_column + 1):
                matrix[min_row][column] = spiral_element
                spiral_element += 1    
            min_row += 1
            # down 
            for row in range(min_row, max_row + 1):
                matrix[row][max_column] = spiral_element
                spiral_element += 1 
            max_column -= 1
            # left 
            for column in range(max_column, min_column - 1 , -1):
                matrix[max_row][column] = spiral_element
                spiral_element += 1
            max_row -= 1
            # up 
            for row in range(max_row, min_row - 1, -1):
                matrix[row][min_column] = spiral_element
                spiral_element += 1             
            min_column += 1
        return matrix

# V1
# https://leetcode.com/problems/spiral-matrix-ii/discuss/572973/easy-to-understand-python-solution-beats-93
# ALSO ref 054 spiral-matrix: 
# https://github.com/yennanliu/CS_basics/blob/master/leetcode_python/Array/spiral-matrix.py
# IDEA : SPIRAL MATRIX
# NOTICE 
# In [4]: 
#    ...: for i in range(3, -1 ,-1):
#    ...:     print (i)
#    ...:     
# 3
# 2
# 1
# 0
class Solution:
    def generateMatrix(self, n):
        # Build n by n matrix
        matrix = [[None] * n for row in range(n)]
        min_row, max_row, min_column, max_column = 0, n - 1, 0, n - 1
        spiral_element = 1
        # while not go through all elements in matrix
        while spiral_element <= n ** 2:
            # right
            for column in range(min_column, max_column + 1):
                matrix[min_row][column] = spiral_element
                spiral_element += 1    
            min_row += 1
            # down 
            for row in range(min_row, max_row + 1):
                matrix[row][max_column] = spiral_element
                spiral_element += 1 
            max_column -= 1
            # left 
            for column in range(max_column, min_column - 1 , -1):
                matrix[max_row][column] = spiral_element
                spiral_element += 1
            max_row -= 1
            # up 
            for row in range(max_row, min_row - 1, -1):
                matrix[row][min_column] = spiral_element
                spiral_element += 1             
            min_column += 1
        return matrix

### Test case
s = Solution()
s.generateMatrix(0) == []
assert s.generateMatrix(1) == [[1]]
assert s.generateMatrix(2) == [[1, 2], [4, 3]]
assert s.generateMatrix(3) == [[ 1, 2, 3 ],[ 8, 9, 4 ],[ 7, 6, 5 ]]
assert s.generateMatrix(4) == [[1, 2, 3, 4], [12, 13, 14, 5], [11, 16, 15, 6], [10, 9, 8, 7]]

# V1'
# https://blog.csdn.net/fuxuemingzhu/article/details/79541687
class Solution(object):
    def generateMatrix(self, n):
        """
        :type n: int
        :rtype: List[List[int]]
        """
        visited = [[0] * n for _ in range(n)]
        matrix = [[0] * n for _ in range(n)]
        self.row, self.col = 0, 0
        self.curr = 1
        def spiral():
            move = False
            while self.col < n and not visited[self.row][self.col]:
                matrix[self.row][self.col] = self.curr
                self.curr += 1
                visited[self.row][self.col] = 1
                self.col += 1
                move = True
            self.col -= 1
            self.row += 1
            while self.row < n and not visited[self.row][self.col]:
                matrix[self.row][self.col] = self.curr
                self.curr += 1
                visited[self.row][self.col] = 1
                self.row += 1
                move = True
            self.row -= 1
            self.col -= 1
            while self.col >= 0  and not visited[self.row][self.col]:
                matrix[self.row][self.col] = self.curr
                self.curr += 1
                visited[self.row][self.col] = 1
                self.col -= 1
                move = True
            self.col += 1
            self.row -= 1
            while self.row >= 0 and not visited[self.row][self.col]:
                matrix[self.row][self.col] = self.curr
                self.curr += 1
                visited[self.row][self.col] = 1
                self.row -= 1
                move = True
            self.row += 1
            self.col += 1
            if move:
                spiral()
        spiral()
        return matrix

# V1'
class Solution:
    # @return a list of lists of integer
    def generateMatrix(self, n):
        if n == 0: return []
        matrix = [[0 for i in range(n)] for j in range(n)]
        up = 0; down = len(matrix)-1
        left = 0; right = len(matrix[0])-1
        direct = 0; count = 0
        while True:
            if direct == 0:
                for i in range(left, right+1):
                    count += 1; matrix[up][i] = count
                up += 1
            if direct == 1:
                for i in range(up, down+1):
                    count += 1; matrix[i][right] = count
                right -= 1
            if direct == 2:
                for i in range(right, left-1, -1):
                    count += 1; matrix[down][i] = count
                down -= 1
            if direct == 3:
                for i in range(down, up-1, -1):
                    count += 1; matrix[i][left] = count
                left += 1
            if count == n*n: return matrix
            direct = (direct+1) % 4
            
# V2 
# Time:  O(n^2)
# Space: O(1)
class Solution(object):
    # @return a list of lists of integer
    def generateMatrix(self, n):
        matrix = [[0 for _ in range(n)] for _ in range(n)]

        left, right, top, bottom, num = 0, n - 1, 0, n - 1, 1

        while left <= right and top <= bottom:
            for j in range(left, right + 1):
                matrix[top][j] = num
                num += 1
            for i in range(top + 1, bottom):
                matrix[i][right] = num
                num += 1
            for j in reversed(range(left, right + 1)):
                if top < bottom:
                    matrix[bottom][j] = num
                    num += 1
            for i in reversed(range(top + 1, bottom)):
                if left < right:
                    matrix[i][left] = num
                    num += 1
            left, right, top, bottom = left + 1, right - 1, top + 1, bottom - 1

        return matrix