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




# V1  : dev  (timeout error )
"""
class Solution(object):
    def findMaxAverage(self, nums, k):
        output=[]
        for i in range(len(nums)):
            len_ = k 
            if i+len_ > len(nums):
                pass
            else:
                sub_nums = nums[i:i+len_]
            avg_ = float(sum(sub_nums)/len(sub_nums))
            output.append(avg_)
        return max(output)
"""



# V2 
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


