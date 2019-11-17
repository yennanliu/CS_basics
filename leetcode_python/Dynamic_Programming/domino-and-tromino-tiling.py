# V0

# V1
# http://bookshadow.com/weblog/2018/02/25/leetcode-domino-and-tromino-tiling/
class Solution(object):
    def numTilings(self, N):
        """
        :type N: int
        :rtype: int
        """
        MOD = 10**9 + 7
        dp = [[0] * 3 for x in range(N + 10)]
        dp[0] = [1, 0, 0]
        dp[1] = [1, 1, 1]
        for x in range(2, N + 1):
            dp[x][0] = (dp[x - 1][0] + sum(dp[x - 2])) % MOD
            dp[x][1] = (dp[x - 1][0] + dp[x - 1][2]) % MOD
            dp[x][2] = (dp[x - 1][0] + dp[x - 1][1]) % MOD
        return dp[N][0]
        
# V1'
# https://www.jiuzhang.com/solution/domino-and-tromino-tiling/#tag-highlight-lang-python
class Solution:
    """
    @param N: a integer
    @return: return a integer
    """
    def numTilings(self, N):
        if N < 3:
            return N
        
        MOD = 1000000007
        
        f = [[0, 0, 0] for i in range(N + 1)]
        f[0][0] = f[1][0] = f[1][1] = f[1][2] = 1
        for i in range(2, N + 1):
            f[i][0] = (f[i - 1][0] + f[i - 2][0] + f[i - 2][1] + f[i - 2][2]) % MOD;
            f[i][1] = (f[i - 1][0] + f[i - 1][2]) % MOD;
            f[i][2] = (f[i - 1][0] + f[i - 1][1]) % MOD;      
        return f[N][0]

# V2
# Time:  O(logn)
# Space: O(1)
import itertools
class Solution(object):
    def numTilings(self, N):
        """
        :type N: int
        :rtype: int
        """
        M = int(1e9+7)

        def matrix_expo(A, K):
            result = [[int(i==j) for j in range(len(A))] \
                      for i in range(len(A))]
            while K:
                if K % 2:
                    result = matrix_mult(result, A)
                A = matrix_mult(A, A)
                K /= 2
            return result

        def matrix_mult(A, B):
            ZB = zip(*B)
            return [[sum(a*b for a, b in itertools.izip(row, col)) % M \
                     for col in ZB] for row in A]

        T = [[1, 0, 0, 1],  # #(|) = #(|) + #(=)
             [1, 0, 1, 0],  # #(「) = #(|) + #(L)
             [1, 1, 0, 0],  # #(L) = #(|) + #(「)
             [1, 1, 1, 0]]  # #(=) = #(|) + #(「) + #(L)

        return matrix_expo(T, N)[0][0]  # T^N * [1, 0, 0, 0]


# Time:  O(n)
# Space: O(1)
class Solution2(object):
    def numTilings(self, N):
        """
        :type N: int
        :rtype: int
        """
        # Prove:
        # dp[n] = dp[n-1](|) + dp[n-2](=) + 2*(dp[n-3](「」) + ... + d[0](「 = ... = 」))
        #       = dp[n-1] + dp[n-2] + dp[n-3] + dp[n-3] + 2*(dp[n-4] + ... + d[0])
        #       = dp[n-1] + dp[n-3] + (dp[n-2] + dp[n-3] + 2*(dp[n-4] + ... + d[0])
        #       = dp[n-1] + dp[n-3] + dp[n-1]
        #       = 2*dp[n-1] + dp[n-3]
        M = int(1e9+7)
        dp = [1, 1, 2]
        for i in range(3, N+1):
            dp[i%3] = (2*dp[(i-1)%3]%M + dp[(i-3)%3])%M
        return dp[N%3]