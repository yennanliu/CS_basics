# V0 

# V1 

# V1'
# https://www.jiuzhang.com/solution/minimum-falling-path-sum/#tag-highlight-lang-python
"""

DP def
    dp[i][j]: MIN falling path sum from row 0 down to cell (i, j)

DP eq

     dp[i][j] = A[i][j] + min( dp[i-1][j-1], dp[i-1][j], dp[i-1][j+1] )


    -> e.g. the three predecessors are "directly above" plus the two
              diagonals; clamp j-1 / j+1 at the borders

     init: dp[0][j] = A[0][j]
     ans = min(dp[n-1])

     (can be done IN PLACE on A, giving O(1) extra space)

"""
# time = O(n^2)
# space = O(1)
class Solution:
    """
    @param A: the given array
    @return: the minimum sum of a falling path
    """
    def minFallingPathSum(self, A):
        # Write your code here
        for i in range(1, len(A)):
            for j in range(len(A[0])):
                topleft = A[i-1][max(j-1, 0)] 
                topright = A[i-1][min(j+1, len(A) - 1)]
                A[i][j] += min(topleft, topright, A[i-1][j])          
        return min(A[-1])
        
# V2
"""

DP def
    dp[i][j]: MIN falling path sum from row 0 down to cell (i, j)

DP eq

     dp[i][j] = A[i][j] + min( dp[i-1][j-1], dp[i-1][j], dp[i-1][j+1] )


    -> e.g. the three predecessors are "directly above" plus the two
              diagonals; clamp j-1 / j+1 at the borders

     init: dp[0][j] = A[0][j]
     ans = min(dp[n-1])

     (can be done IN PLACE on A, giving O(1) extra space)

"""
# time = O(n^2)
# space = O(1)
class Solution(object):
    def minFallingPathSum(self, A):
        """
        :type A: List[List[int]]
        :rtype: int
        """
        for i in range(1, len(A)):
            for j in range(len(A[i])):
                A[i][j] += min(A[i-1][max(j-1, 0):j+2])
        return min(A[-1])