# V0 

# V1 
# http://bookshadow.com/weblog/2018/04/22/leetcode-binary-trees-with-factors/
"""

DP def
    (arr is SORTED first, so every factor is processed before its multiple)

    dp[a]: number of binary trees whose ROOT value is a

           (each non-leaf node's value == product of its 2 children)

DP eq

     dp[a] = 1 + sum( dp[m] * dp[a/m] * (1 if m == a/m else 2) )

             for every m in arr with m * m <= a and a % m == 0


    -> e.g.
         the leading 1     = the single-node tree (just `a` itself)
         the (1 if .. 2)   = left/right children can be swapped when m != a/m

     ans = sum(dp.values()) % (10^9 + 7)

"""
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
"""

DP def
    (arr is SORTED first, so every factor is processed before its multiple)

    dp[a]: number of binary trees whose ROOT value is a

           (each non-leaf node's value == product of its 2 children)

DP eq

     dp[a] = 1 + sum( dp[m] * dp[a/m] * (1 if m == a/m else 2) )

             for every m in arr with m * m <= a and a % m == 0


    -> e.g.
         the leading 1     = the single-node tree (just `a` itself)
         the (1 if .. 2)   = left/right children can be swapped when m != a/m

     ans = sum(dp.values()) % (10^9 + 7)

"""
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
"""

DP def
    (arr is SORTED first, so every factor is processed before its multiple)

    dp[a]: number of binary trees whose ROOT value is a

           (each non-leaf node's value == product of its 2 children)

DP eq

     dp[a] = 1 + sum( dp[m] * dp[a/m] * (1 if m == a/m else 2) )

             for every m in arr with m * m <= a and a % m == 0


    -> e.g.
         the leading 1     = the single-node tree (just `a` itself)
         the (1 if .. 2)   = left/right children can be swapped when m != a/m

     ans = sum(dp.values()) % (10^9 + 7)

"""
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