"""

867. Transpose Matrix
Easy

Given a 2D integer array matrix, return the transpose of matrix.

The transpose of a matrix is the matrix flipped over its main diagonal, switching the matrix's row and column indices.

https://assets.leetcode.com/uploads/2021/02/10/hint_transpose.png

Example 1:

Input: matrix = [[1,2,3],[4,5,6],[7,8,9]]
Output: [[1,4,7],[2,5,8],[3,6,9]]

Example 2:

Input: matrix = [[1,2,3],[4,5,6]]
Output: [[1,4],[2,5],[3,6]]

Constraints:

m == matrix.length
n == matrix[i].length
1 <= m, n <= 1000
1 <= m * n <= 10^5
-10^9 <= matrix[i][j] <= 10^9

"""

# V0 

# V1 
# https://www.jiuzhang.com/solution/transpose-matrix/#tag-highlight-lang-python
# time = O(r*c)
# space = O(r*c)
class Solution:
    """
    @param A: A matrix
    @return: A transposed matrix
    """
    def transpose(self, A):
        # write your code here
        n, m = len(A), len(A[0])
        ans=[[0 for i in range(n)] for i in range(m)]
        for i in range(m):
            for j in range(n):
                ans[i][j]=A[j][i]
        return ans

# V1'
# https://blog.csdn.net/fuxuemingzhu/article/details/81015450
# time = O(r*c)
# space = O(r*c)
class Solution:
    def transpose(self, A):
        """
        :type A: List[List[int]]
        :rtype: List[List[int]]
        """
        rows, cols = len(A), len(A[0])
        res = [[0] * rows for _ in range(cols)]
        for row in range(rows):
            for col in range(cols):
                res[col][row] = A[row][col]
        return res
        
# V2 
# time = O(r*c)
# space = O(1)
class Solution(object):
    def transpose(self, A):
        """
        :type A: List[List[int]]
        :rtype: List[List[int]]
        """
        result = [[None] * len(A) for _ in range(len(A[0]))]
        for r, row in enumerate(A):
            for c, val in enumerate(row):
                result[c][r] = val
        return result

# time = O(r*c)
# space = O(1)
class Solution2(object):
    def transpose(self, A):
        """
        :type A: List[List[int]]
        :rtype: List[List[int]]
        """
        return zip(*A)