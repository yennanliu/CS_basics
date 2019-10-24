# V0

# V1
# http://bookshadow.com/weblog/2017/07/30/leetcode-2-keys-keyboard/
# IDEA : DP
# DP EQUATION :
# dp[x] = min(dp[x], dp[y] + x / y) <- while y ∈[1, x) and x % y == 0
class Solution(object):
    def minSteps(self, n):
        """
        :type n: int
        :rtype: int
        """
        #dp = [0x7FFFFFFF] * (n + 1)
        dp = [2**31-1] * (n + 1)    # 0x7FFFFFFF == 2147483647, and 2**31 == 2147483648, so  0x7FFFFFFF == 2**31 - 1 
        dp[0] = dp[1] = 0
        for x in range(2, n + 1):
            for y in range(1, x):
                if x % y == 0:
                    dp[x] = min(dp[x], dp[y] + x / y)
        return dp[n]

# V1'
# https://www.jiuzhang.com/solution/2-keys-keyboard/#tag-highlight-lang-python
class Solution:
    """
    @param n: The number of 'A'
    @return: the minimum number of steps to get n 'A'
    """
    def minSteps(self, n):
        # Write your code here
        def factors(n):
            d = 2
            while d * d <= n:
                while n % d == 0:
                    n /= d
                    yield d
                d += 1
            if n > 1:
                yield n  
        return sum(factors(n))

# V2
# Time:  O(sqrt(n))
# Space: O(1)
class Solution(object):
    def minSteps(self, n):
        """
        :type n: int
        :rtype: int
        """
        result = 0
        p = 2
        # the answer is the sum of prime factors
        while p**2 <= n:
            while n % p == 0:
                result += p
                n //= p
            p += 1
        if n > 1:
            result += n
        return result