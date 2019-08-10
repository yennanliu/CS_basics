# Given a m x n matrix, if an element is 0, set its entire row and column to 0. Do it in-place.
# Example 1:
# Input: 
# [
#   [1,1,1],
#   [1,0,1],
#   [1,1,1]
# ]
# Output: 
# [
#   [1,0,1],
#   [0,0,0],
#   [1,0,1]
#
# Example 2:
# Input: 
# [
#   [0,1,2,0],
#   [3,4,5,2],
#   [1,3,1,5]
# ]
# Output: 
# [
#   [0,0,0,0],
#   [0,4,5,0],
#   [0,3,1,0]
# ]
# Follow up:
# A straight forward solution using O(mn) space is probably a bad idea.
# A simple improvement uses O(m + n) space, but still not the best solution.
# Could you devise a constant space solution?


# V0 

# V1 
# https://www.cnblogs.com/zuoyuan/p/3769698.html
# IDEA : RECORD X, Y (X, Y AXIS) TO CHECK IF THERE IS 0 EXISTING 
# THEN GO THROUGH ARRAY TO CHECK RELATIVE ROWS AND COLUMNS 
# AND UPDATE THE VALUES (value -> 0)
class Solution:
    # @param matrix, a list of lists of integers
    # RETURN NOTHING, MODIFY matrix IN PLACE.
    def setZeroes(self, matrix):
        rownum = len(matrix)
        colnum = len(matrix[0])
        row = [False for i in range(rownum)]
        col = [False for i in range(colnum)]
        for i in range(rownum):
            for j in range(colnum):
                if matrix[i][j] == 0:
                    row[i] = True
                    col[j] = True
        for i in range(rownum):
            for j in range(colnum):
                if row[i] or col[j]:
                    matrix[i][j] = 0

# V1'
# https://blog.csdn.net/qqxx6661/article/details/78279728
class Solution:
    # @param matrix, a list of lists of integers
    # RETURN NOTHING, MODIFY matrix IN PLACE.
    def setZeroes(self, matrix):
        m , n = len(matrix), len(matrix[0])
        row , col = [0 for i in range(m)] , [0 for i in range(n)]
        for i in range(m):
            for j in range(n):
                if not matrix[i][j]:
                    row[i]=col[j]=1
        for i in range(m):
            if row[i]:
                for j in range(n):
                    matrix[i][j]=0

        for j in range(n):
            if col[j]:
                for i in range(m):
                    matrix[i][j]=0

# V1'' 
# https://blog.csdn.net/qqxx6661/article/details/78279728
class Solution:
    # @param matrix, a list of lists of integers
    # RETURN NOTHING, MODIFY matrix IN PLACE.
    def setZeroes(self, matrix):
        m , n = len(matrix), len(matrix[0])
        temp = [[matrix[i][j] for j in range(n)] for i in range(m)]
        for i in range(m):
            for j in range(n):
                if not temp[i][j]:
                    self.setZero(i,j,n,m,matrix)

    def setZero(self,row,col,n,m,matrix):
        for i in range(m):
            matrix[i][col]=0
        for j in range(n):
            matrix[row][j]=0
            
# V2 
from functools import reduce
# Time:  O(m * n)
# Space: O(1)
class Solution(object):
    # @param matrix, a list of lists of integers
    # RETURN NOTHING, MODIFY matrix IN PLACE.
    def setZeroes(self, matrix):
        first_col = reduce(lambda acc, i: acc or matrix[i][0] == 0, range(len(matrix)), False)
        first_row = reduce(lambda acc, j: acc or matrix[0][j] == 0, range(len(matrix[0])), False)

        for i in range(1, len(matrix)):
            for j in range(1, len(matrix[0])):
                if matrix[i][j] == 0:
                    matrix[i][0], matrix[0][j] = 0, 0

        for i in range(1, len(matrix)):
            for j in range(1, len(matrix[0])):
                if matrix[i][0] == 0 or matrix[0][j] == 0:
                    matrix[i][j] = 0

        if first_col:
            for i in range(len(matrix)):
                matrix[i][0] = 0

        if first_row:
            for j in range(len(matrix[0])):
                matrix[0][j] = 0