# V0 : DEV 

# V1 
# https://blog.csdn.net/fuxuemingzhu/article/details/79367789
class Solution(object):
    def __init__(self):
        self.dp = dict()
    
    def numTrees(self, n):
        """
        :type n: int
        :rtype: int
        """
        if n in self.dp:
            return self.dp[n]
        if n == 0 or n == 1:
            return 1
        ans = 0
        for i in range(1, n + 1):
            ans += self.numTrees(i - 1) * self.numTrees(n - i)
        self.dp[n] = ans
        return ans

# V1' 
class Solution(object):
    def numTrees(self, n):
        """
        :type n: int
        :rtype: int
        """
        dp = [1, 1]
        for i in range(2, n + 1):
            count = 0
            for j in range(i):
                count += dp[j] * dp[i - j - 1]
            dp.append(count)
        return dp.pop()

# V2 
# Time:  O(n)
# Space: O(1)
class Solution(object):
    def numTrees(self, n):
        """
        :type n: int
        :rtype: int
        """
        if n == 0:
            return 1

        def combination(n, k):
            count = 1
            # C(n, k) = (n) / 1 * (n - 1) / 2 ... * (n - k + 1) / k
            for i in range(1, k + 1):
                count = count * (n - i + 1) / i
            return count

        return combination(2 * n, n) - combination(2 * n, n - 1)

# Time:  O(n^2)
# Space: O(n)
# DP solution.
class Solution2(object):
    # @return an integer
    def numTrees(self, n):
        counts = [1, 1]
        for i in range(2, n + 1):
            count = 0
            for j in range(i):
                count += counts[j] * counts[i - j - 1]
            counts.append(count)
        return counts[-1]
