# V0 

# V1 
# https://www.jiuzhang.com/solution/largest-sum-of-averages/#tag-highlight-lang-python
class Solution:
    """
    @param A: an array
    @param K: an integer
    @return: the largest score
    """
    def largestSumOfAverages(self, A, K):
        # Write your code here
        n = len(A)
        dp = [[0.0 for i in range(n + 1)] for j in range(K + 1)]
        sums = [0.0 for i in range(n + 1)]
        for i in range(1, n + 1):
            sums[i] = sums[i - 1] + A[i - 1]
            dp[1][i] = sums[i] / i
        for i in range(2, K + 1):
            for j in range(i, n + 1):
                for k in range(i - 1, j):
                    dp[i][j] = max(dp[i][j], dp[i - 1][k] + (sums[j]-sums[k]) / (j-k))
        return dp[K][n]

# V1'
# https://www.jiuzhang.com/solution/largest-sum-of-averages/#tag-highlight-lang-python
class Solution:
    """
    @param A: an array
    @param K: an integer
    @return: the largest score
    """
    def largestSumOfAverages(self, A, K):
        # Write your code here
        n = len(A)
        dp = [[0.0 for i in range(n + 1)] for j in range(K + 1)]
        sums = [0.0 for i in range(n + 1)]
        for i in range(1, n + 1):
            sums[i] = sums[i - 1] + A[i - 1]
            dp[1][i] = sums[i] / i
        for i in range(2, K + 1):
            for j in range(i, n + 1):
                for k in range(i - 1, j):
                    dp[i][j] = max(dp[i][j], dp[i - 1][k] + (sums[j]-sums[k]) / (j-k))
        return dp[K][n]

# V2 
# Time:  O(k * n^2)
# Space: O(n)
class Solution(object):
    def largestSumOfAverages(self, A, K):
        """
        :type A: List[int]
        :type K: int
        :rtype: float
        """
        accum_sum = [A[0]]
        for i in range(1, len(A)):
            accum_sum.append(A[i]+accum_sum[-1])

        dp = [[0]*len(A) for _ in range(2)]
        for k in range(1, K+1):
            for i in range(k-1, len(A)):
                if k == 1:
                    dp[k % 2][i] = float(accum_sum[i])/(i+1)
                else:
                    for j in range(k-2, i):
                        dp[k % 2][i] = \
                            max(dp[k % 2][i],
                                dp[(k-1) % 2][j] +
                                float(accum_sum[i]-accum_sum[j])/(i-j))
        return dp[K % 2][-1]
