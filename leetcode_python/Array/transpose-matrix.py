# V0 

# V1 
# https://www.jiuzhang.com/solution/transpose-matrix/#tag-highlight-lang-python
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
# Time:  O(r * c)
# Space: O(1)
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

# Time:  O(r * c)
# Space: O(1)
class Solution2(object):
    def transpose(self, A):
        """
        :type A: List[List[int]]
        :rtype: List[List[int]]
        """
        return zip(*A)