# V0

# V1 
# https://blog.csdn.net/fuxuemingzhu/article/details/82708723
# IDEA : DP 
class Solution(object):
    def numSubarrayBoundedMax(self, A, L, R):
        """
        :type A: List[int]
        :type L: int
        :type R: int
        :rtype: int
        """
        if not A: return 0
        dp = [0] * len(A)
        prev = -1
        for i, a in enumerate(A):
            if a < L and i > 0:
                dp[i] = dp[i - 1]
            elif a > R:
                dp[i] = 0
                prev = i
            elif L <= a <= R:
                dp[i] = i - prev
        return sum(dp)

# V1' 
# https://blog.csdn.net/fuxuemingzhu/article/details/82708723
# IDEA : DP
class Solution(object):
    def numSubarrayBoundedMax(self, A, L, R):
        """
        :type A: List[int]
        :type L: int
        :type R: int
        :rtype: int
        """
        dp = 0
        res = 0
        prev = -1
        for i, a in enumerate(A):
            if a < L and i > 0:
                res += dp
            elif a > R:
                dp = 0
                prev = i
            elif L <= a <= R:
                dp = i - prev
                res += dp
        return res

# V1'''
# http://bookshadow.com/weblog/2018/03/04/leetcode-number-of-subarrays-with-bounded-maximum/
class Solution(object):
    def numSubarrayBoundedMax(self, A, L, R):
        """
        :type A: List[int]
        :type L: int
        :type R: int
        :rtype: int
        """
        ans = lastIdx = 0
        for i, x in enumerate(A + [10**10]):
            if x > R:
                ans += self.numSubarrayMinimumMax(A[lastIdx:i], L)
                lastIdx = i + 1
        return ans

    def numSubarrayMinimumMax(self, A, L):
        """
        :type A: List[int]
        :type L: int
        :rtype: int
        """
        ans = lastIdx = 0
        for i, x in enumerate(A):
            if x >= L:
                ans += (i - lastIdx + 1) * (len(A) - i)
                lastIdx = i + 1
        return ans
        
# V2
# Time:  O(n)
# Space: O(1)
class Solution(object):
    def numSubarrayBoundedMax(self, A, L, R):
        """
        :type A: List[int]
        :type L: int
        :type R: int
        :rtype: int
        """
        def count(A, bound):
            result, curr = 0, 0
            for i in A :
                curr = curr + 1 if i <= bound else 0
                result += curr
            return result

        return count(A, R) - count(A, L-1)