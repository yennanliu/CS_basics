# Time:  O(n)
# Space: O(1)

# Given an array consisting of n integers,
# find the contiguous subarray of given length k that has the maximum average value.
# And you need to output the maximum average value.
#
# Example 1:
# Input: [1,12,-5,-6,50,3], k = 4
# Output: 12.75
# Explanation: Maximum average is (12-5-6+50)/4 = 51/4 = 12.75
# Note:
# 1 <= k <= n <= 30,000.
# Elements of the given array will be in the range [-10,000, 10,000].


#
# Example 1:
# Input: [1,12,-5,-6,50,3], k = 4
# Output: 12.75
# Explanation: Maximum average is (12-5-6+50)/4 = 51/4 = 12.75

# V0 

# V1
# http://bookshadow.com/weblog/2017/07/16/leetcode-maximum-average-subarray-i/
# IDEA : SLIDING WINDOW 
class Solution(object):
    def findMaxAverage(self, nums, k):
        """
        :type nums: List[int]
        :type k: int
        :rtype: float
        """
        ans = None
        sums = 0
        for x in range(len(nums)):
            sums += nums[x]
            if x >= k: sums -= nums[x - k]
            if x >= k - 1: ans = max(ans, 1.0 * sums / k)
        return ans 

# V1'
# https://www.jiuzhang.com/solution/maximum-average-subarray/#tag-highlight-lang-python
class Solution:
    """
    @param nums: an array
    @param k: an integer
    @return: the maximum average value
    """
    def findMaxAverage(self, nums, k):
        # Write your code here
        n = len(nums)
        sum = [0 for i in range(n + 1)]
        for i in range(1, n + 1):
            sum[i] = sum[i - 1] + nums[i - 1]
        ans = sum[k]
        for i in range(k + 1, n + 1):
            ans = max(ans, sum[i] - sum[i - k])
        return ans * 1.0 / k

# V2 
# Time:  O(n)
# Space: O(1)
class Solution(object):
    def findMaxAverage(self, nums, k):
        """
        :type nums: List[int]
        :type k: int
        :rtype: float
        """
        result = total = sum(nums[:k])
        for i in range(k, len(nums)):
            total += nums[i] - nums[i-k]
            result = max(result, total)
        return float(result) / k
