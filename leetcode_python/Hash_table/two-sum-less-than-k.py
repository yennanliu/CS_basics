"""

LeetCode 1099. Two Sum Less Than K

https://goodtecher.com/leetcode-1099-two-sum-less-than-k/

Description
https://leetcode.com/problems/two-sum-less-than-k/

Given an array nums of integers and integer k, return the maximum sum such that there exists i < j with nums[i] + nums[j] = sum and sum < k. If no i, j exist satisfying this equation, return -1.

Example 1:

Input: nums = [34,23,1,24,75,33,54,8], k = 60
Output: 58
Explanation: We can use 34 and 24 to sum 58 which is less than 60.
Example 2:

Input: nums = [10,20,30], k = 15
Output: -1
Explanation: In this case it is not possible to get a pair sum less that 15.
Constraints:

1 <= nums.length <= 100
1 <= nums[i] <= 1000
1 <= k <= 2000
Explanation
Sorted and then use two pointers technique.

"""

# V0

# V1
# https://goodtecher.com/leetcode-1099-two-sum-less-than-k/
class Solution:
    def twoSumLessThanK(self, nums, k):    
        nums = sorted(nums)
        i = 0
        j = len(nums) - 1
      
        max_sum = -1
        
        while i < j:
            if nums[i] + nums[j] >= k:
                j -= 1
            else:
                max_sum = max(max_sum, nums[i] + nums[j])
                i += 1      
        return max_sum

# V1'
# https://blog.51cto.com/u_15344287/3647641
class Solution:
    def twoSumLessThanK(self, nums: List[int], k: int) -> int:
        nums.sort()
        ans = -1
        for i1 in range(len(nums)):
            n1 = nums[i1]
            i2 = bisect.bisect_left(nums, k - nums[i1], lo=i1 + 1) - 1
            n2 = nums[i2]
            if i2 > i1:
                ans = max(ans, n1 + n2)
        return ans

# V1'
# https://blog.csdn.net/qq_32424059/article/details/94226165

# V2