# LeetCode 634. Find the Derangement of An Array

# In combinatorial mathematics, a derangement is a permutation of the elements of a set, such that no element appears in its original position.

# There's originally an array consisting of n integers from 1 to n in ascending order, you need to find the number of derangement it can generate.

# Also, since the answer may be very large, you should return the output mod 109 + 7.

# Example 1:

# Input: 3
# Output: 2
# Explanation: The original array is [1,2,3]. The two derangements are [2,3,1] and [3,1,2].
# Note:
# n is in the range of [1, 106].


# V1 : DEV 


# V2 
# https://blog.csdn.net/magicbean2/article/details/79112988
# http://bookshadow.com/weblog/2017/07/02/leetcode-find-the-derangement-of-an-array/
class Solution(object):
    def findDerangement(self, n):
        """
        :type n: int
        :rtype: int
        """
        dp = [0, 1]
        for x in range(2, n + 1):
            dp.append(x * (dp[- 1] + dp[-2]) % (10**9 + 7))
        return dp[n - 1]


# V2' 
# http://bookshadow.com/weblog/2017/07/02/leetcode-find-the-derangement-of-an-array/
class Solution(object):
    def findDerangement(self, n):
        """
        :type n: int
        :rtype: int
        """
        MOD = 10**9 + 7
        mul, sig = 1, (-1) ** n
        ans = 0
        for x in range(n, -1, -1):
            ans = (ans + mul * sig) % MOD
            mul = (mul * x) % MOD
            sig *= -1
        return ans % MOD

# V3 
# Time:  O(n)
# Space: O(1)
class Solution(object):
    def findDerangement(self, n):
        """
        :type n: int
        :rtype: int
        """
        M = 1000000007
        mul, total = 1, 0
        for i in reversed(range(n+1)):
            total = (total + M + (1 if i % 2 == 0 else -1) * mul) % M
            mul = (mul * i) % M
        return total
