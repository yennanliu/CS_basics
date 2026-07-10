# V0 

# V1 
# http://bookshadow.com/weblog/2018/04/22/leetcode-binary-trees-with-factors/
# time = O(n^2)
# space = O(n)
import collections
class Solution(object):
    def numFactoredBinaryTrees(self, A):
        """
        :type A: List[int]
        :rtype: int
        """
        A.sort()
        dp = collections.defaultdict(int)
        MOD = 10 ** 9 + 7
        for i, a in enumerate(A):
            num = 0
            for j in range(i):
                m = A[j]
                if m * m > a: break
                if a % m: continue
                n = a / m
                num = (num + dp[m] * dp[n] * (1 + (m != n))) % MOD
            dp[a] = num + 1
        return sum(dp.values()) % MOD

# V1'
# https://www.jiuzhang.com/solution/binary-trees-with-factors/#tag-highlight-lang-python
# time = O(n^2)
# space = O(n)
class Solution:
    def numFactoredBinaryTrees(self, A):
        A.sort()
        dp = {}
        for i in range(len(A)):
            dp[A[i]] = 1
            for j in range(i):
                if A[i] % A[j] == 0 and A[i] / A[j] in dp:
                    dp[A[i]] += dp[A[j]] * dp[A[i] / A[j]]
        return sum(dp.values()) % (10**9 + 7)

# V2
# time = O(n^2)
# space = O(n)
class Solution(object):
    def numFactoredBinaryTrees(self, A):
        """
        :type A: List[int]
        :rtype: int
        """
        M = 10**9 + 7
        A.sort()
        dp = {}
        for i in range(len(A)):
            dp[A[i]] = 1
            for j in range(i):
                if A[i] % A[j] == 0 and A[i] // A[j] in dp:
                    dp[A[i]] += dp[A[j]] * dp[A[i] // A[j]]
                    dp[A[i]] %= M
        return sum(dp.values()) % M