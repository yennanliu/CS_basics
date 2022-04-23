"""

48. Rotate Image
Medium

You are given an n x n 2D matrix representing an image, rotate the image by 90 degrees (clockwise).

You have to rotate the image in-place, which means you have to modify the input 2D matrix directly. DO NOT allocate another 2D matrix and do the rotation.

 

Example 1:


Input: matrix = [[1,2,3],[4,5,6],[7,8,9]]
Output: [[7,4,1],[8,5,2],[9,6,3]]
Example 2:


Input: matrix = [[5,1,9,11],[2,4,8,10],[13,3,6,7],[15,14,12,16]]
Output: [[15,13,2,5],[14,3,4,1],[12,6,8,9],[16,7,10,11]]
 

Constraints:

n == matrix.length == matrix[i].length
1 <= n <= 20
-1000 <= matrix[i][j] <= 1000


"""

# V0
# IDEA : TRANSPOSE (i,j -> j,i) -> REVERSE 
class Solution(object):
    def rotate(self, matrix):
        # edge case
        if not matrix:
            return
        # step 1) i, j -> j, i
        l = len(matrix)
        w = len(matrix[0])
        for i in range(l):
            """
            NOTE !!!
                -> j start from i+1 to len(matrix[0])
            """
            for j in range(i+1, w):
                #print ("i = " + str(i) + " j = " + str(j) + " matrix = " + str(matrix))
                matrix[i][j], matrix[j][i] = matrix[j][i], matrix[i][j]
        # step 2) inverse
        for i in range(l):
            matrix[i] = matrix[i][::-1]
        return matrix

# V0'
# IDEA : TRANSPOSE (i,j -> j,i) -> REVERSE 
class Solution:
    def rotate(self, matrix):
        ### NOTE !!! the ordering : transpose -> reverse
        ### NOTE : TRANSPOSE matrix
        n = len(matrix)
        # transpose
        for i in range(n):
            # NOTE !!! j range starts from i+i to n
             for j in range(i+1, n):
                matrix[i][j], matrix[j][i] = matrix[j][i], matrix[i][j]
        # reverse
        # NOTE !!! : we have to use below op
        for i in range(n):
            #matrix[i].reverse()
            matrix[i] = matrix[i][::-1]
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