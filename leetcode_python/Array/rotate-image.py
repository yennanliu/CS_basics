# Time:  O(n^2)
# Space: O(1)
#
# You are given an n x n 2D matrix representing an image.
#
# Rotate the image by 90 degrees (clockwise).
#
# Follow up:
# Could you do this in-place?

# V0
# IDEA : TRANSPOSE -> REVERSE 
class Solution:
    def rotate(self, matrix):
        ### NOTE : TRANSPOSE matrix
        n = len(matrix)
        for i in range(n):
             for j in range(i+1, n):
                matrix[i][j], matrix[j][i] = matrix[j][i], matrix[i][j]
        for i in range(n):
            matrix[i].reverse()
        return matrix

### Test case
s = Solution()
assert s.rotate([[1,2,3],[4,5,6],[7,8,9]]) == [[7,4,1],[8,5,2],[9,6,3]]
assert s.rotate([]) == []
assert s.rotate([[1,2]]) == [[2,1]]
assert s.rotate([[[],[]]]) == [[[],[]]]


# V1 
# https://www.cnblogs.com/zuoyuan/p/3772978.html
# IDEA : TRANSPOSE -> REVERSE 
class Solution:
    # @param matrix, a list of lists of integers
    # @return a list of lists of integers
    def rotate(self, matrix):
        n = len(matrix)
        for i in range(n):
            for j in range(i+1, n):
                matrix[i][j], matrix[j][i] = matrix[j][i], matrix[i][j]
        for i in range(n):
            matrix[i].reverse()
        return matrix

# V1'
# https://blog.csdn.net/fuxuemingzhu/article/details/79451733
class Solution(object):
    def rotate(self, matrix):
        """
        :type matrix: List[List[int]]
        :rtype: void Do not return anything, modify matrix in-place instead.
        """
        if matrix:
            rows = len(matrix)
            cols = len(matrix[0])
            for i in range(rows / 2):
                for j in range(cols):
                    matrix[i][j], matrix[rows - i - 1][j] = matrix[rows - i - 1][j], matrix[i][j]
            for i in range(rows):
                for j in range(i):
                    matrix[i][j], matrix[j][i] = matrix[j][i], matrix[i][j]

# V2 
# Time:  O(n^2)
# Space: O(1)
class Solution(object):
    # @param matrix, a list of lists of integers
    # @return a list of lists of integers
    def rotate(self, matrix):
        n = len(matrix)

        # anti-diagonal mirror
        for i in range(n):
            for j in range(n - i):
                matrix[i][j], matrix[n-1-j][n-1-i] = matrix[n-1-j][n-1-i], matrix[i][j]

        # horizontal mirror
        for i in range(n / 2):
            for j in range(n):
                matrix[i][j], matrix[n-1-i][j] = matrix[n-1-i][j], matrix[i][j]

        return matrix

# Time:  O(n^2)
# Space: O(n^2)
class Solution2(object):
    # @param matrix, a list of lists of integers
    # @return a list of lists of integers
    def rotate(self, matrix):
        return [list(reversed(x)) for x in zip(*matrix)]
