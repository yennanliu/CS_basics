"""

795. Number of Subarrays with Bounded Maximum
Medium

Given an integer array nums and two integers left and right, return the number of contiguous non-empty subarrays such that the value of the maximum array element in that subarray is in the range [left, right].

The test cases are generated so that the answer will fit in a 32-bit integer.

Example 1:

Input: nums = [2,1,4,3], left = 2, right = 3
Output: 3
Explanation: There are three subarrays that meet the requirements: [2], [2, 1], [3].

Example 2:

Input: nums = [2,9,2,5,6], left = 2, right = 8
Output: 7

Constraints:

1 <= nums.length <= 10^5
0 <= nums[i] <= 10^9
0 <= left <= right <= 10^9

"""

# V0

# V1
# https://blog.csdn.net/fuxuemingzhu/article/details/82708723
# IDEA : DP
# time = O(n)
# space = O(n)
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
# time = O(n)
# space = O(1)
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

# V1''
# http://bookshadow.com/weblog/2018/03/04/leetcode-number-of-subarrays-with-bounded-maximum/
class Solution(object):
    def numSubarrayBoundedMax(self, A, L, R):
        """
        :type A: List[int]
        :type L: int
        :type R: int
        :rtype: int
        """
        # time = O(n)
        # space = O(n)
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
        # time = O(n)
        # space = O(1)
        ans = lastIdx = 0
        for i, x in enumerate(A):
            if x >= L:
                ans += (i - lastIdx + 1) * (len(A) - i)
                lastIdx = i + 1
        return ans
        
# V2
# time = O(n)
# space = O(1)
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