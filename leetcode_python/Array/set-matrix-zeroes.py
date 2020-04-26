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
                if row[i] == True or col[j] == True:
                    matrix[i][j] = 0

# V1 
# https://www.cnblogs.com/zuoyuan/p/3769698.html
# TIME COMPLEXITY : O(N*M)
# SPACE COMPLEXITY : O(N+M)
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
        # for test case
        #return matrix 

### Test case
s=Solution()
assert s.setZeroes([[]]) == [[]]
assert s.setZeroes([[0]]) == [[0]]
assert s.setZeroes([[1]]) == [[1]]
assert s.setZeroes([[1,2,3]]) == [[1,2,3]]
assert s.setZeroes([[0,2,3]]) == [[0,0,0]]
assert s.setZeroes([[1,2,3],[4,5,6]]) == [[1,2,3],[4,5,6]]
assert s.setZeroes([[1,2,3], [4,5,6], [7,8,9]]) == [[1,2,3], [4,5,6], [7,8,9]]
assert s.setZeroes([[1,2,3], [4,0,6], [7,8,9]]) == [[1,0,3], [0,0,0], [7,0,9]]
assert s.setZeroes([[1,2,3,999], [4,0,6,0], [0,7,8,9]]) == [[0, 0, 3, 0], [0, 0, 0, 0], [0, 0, 0, 0]]
assert s.setZeroes([[0,0,0], [0,0,0], [0,0,0]]) == [[0,0,0], [0,0,0], [0,0,0]]

# V1'
# https://leetcode.com/problems/set-matrix-zeroes/solution/
# IDEA : BRUTE FORCE + DOUBLE LOOP
# TIME COMPLEXITY : O(N*M)
# SPACE COMPLEXITY : O(N+M)
class Solution(object):
    def setZeroes(self, matrix):
        """
        :type matrix: List[List[int]]
        :rtype: void Do not return anything, modify matrix in-place instead.
        """
        R = len(matrix)
        C = len(matrix[0])
        rows, cols = set(), set()

        # Essentially, we mark the rows and columns that are to be made zero
        for i in range(R):
            for j in range(C):
                if matrix[i][j] == 0:
                    rows.add(i)
                    cols.add(j)

        # Iterate over the array once again and using the rows and cols sets, update the elements
        for i in range(R):
            for j in range(C):
                if i in rows or j in cols:
                    matrix[i][j] = 0

# V1''
# https://leetcode.com/problems/set-matrix-zeroes/solution/
# TIME COMPLEXITY : O((N*M)*(N+M))
# SPACE COMPLEXITY : O(1)
class Solution(object):
    def setZeroes(self, matrix):
        """
        :type matrix: List[List[int]]
        :rtype: void Do not return anything, modify matrix in-place instead.
        """
        MODIFIED = -1000000
        R = len(matrix)
        C = len(matrix[0])
        for r in range(R):
            for c in range(C):
                if matrix[r][c] == 0:
                    # We modify the elements in place. Note, we only change the non zeros to MODIFIED
                    for k in range(C):
                        matrix[r][k] = MODIFIED if matrix[r][k] != 0 else 0
                    for k in range(R):
                        matrix[k][c] = MODIFIED if matrix[k][c] != 0 else 0
        for r in range(R):
            for c in range(C):
                # Make a second pass and change all MODIFIED elements to 0 """
                if matrix[r][c] == MODIFIED:
                    matrix[r][c] = 0

# V1'''
# https://leetcode.com/problems/set-matrix-zeroes/solution/
# TIME COMPLEXITY : O((N*M))
# SPACE COMPLEXITY : O(1)
class Solution(object):
    def setZeroes(self, matrix):
        """
        :type matrix: List[List[int]]
        :rtype: void Do not return anything, modify matrix in-place instead.
        """
        is_col = False
        R = len(matrix)
        C = len(matrix[0])
        for i in range(R):
            # Since first cell for both first row and first column is the same i.e. matrix[0][0]
            # We can use an additional variable for either the first row/column.
            # For this solution we are using an additional variable for the first column
            # and using matrix[0][0] for the first row.
            if matrix[i][0] == 0:
                is_col = True
            for j in range(1, C):
                # If an element is zero, we set the first element of the corresponding row and column to 0
                if matrix[i][j]  == 0:
                    matrix[0][j] = 0
                    matrix[i][0] = 0

        # Iterate over the array once again and using the first row and first column, update the elements.
        for i in range(1, R):
            for j in range(1, C):
                if not matrix[i][0] or not matrix[0][j]:
                    matrix[i][j] = 0

        # See if the first row needs to be set to zero as well
        if matrix[0][0] == 0:
            for j in range(C):
                matrix[0][j] = 0

        # See if the first column needs to be set to zero as well        
        if is_col:
            for i in range(R):
                matrix[i][0] = 0

# V1''''
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

# V1'''''''
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