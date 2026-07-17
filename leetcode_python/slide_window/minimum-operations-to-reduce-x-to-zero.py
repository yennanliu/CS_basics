# https://leetcode.com/problems/minimum-operations-to-reduce-x-to-zero/description/

"""

1658. Minimum Operations to Reduce X to Zero
Solved
Medium
Topics
premium lock icon
Companies
Hint
You are given an integer array nums and an integer x. In one operation, you can either remove the leftmost or the rightmost element from the array nums and subtract its value from x. Note that this modifies the array for future operations.

Return the minimum number of operations to reduce x to exactly 0 if it is possible, otherwise, return -1.

 

Example 1:

Input: nums = [1,1,4,2,3], x = 5
Output: 2
Explanation: The optimal solution is to remove the last two elements to reduce x to zero.
Example 2:

Input: nums = [5,6,7,8,9], x = 4
Output: -1
Example 3:

Input: nums = [3,2,20,1,1,3], x = 10
Output: 5
Explanation: The optimal solution is to remove the last three elements and the first two elements (5 operations in total) to reduce x to zero.
 

Constraints:

1 <= nums.length <= 105
1 <= nums[i] <= 104
1 <= x <= 109

"""


# V0
# IDEA: `subtract` + slide window
"""

-> remain = 5 -> slide window get sub array sum = sum(nums) -x

e.g.

nums = [1,1,4,2,3], x = 5

-> find longest sub array (continuous) = 11-5 = 6

"""
class Solution(object):
    def minOperations(self, nums, x):
        # edge
        if sum(nums) < x:
            return -1
       
        if sum(nums) == x:
            return len(nums)


        total = sum(nums)

        target = total - x

        lonest_sub_len = -1

        n = len(nums)

        # slide window
        l = 0
        sub_sum = 0

        for r in range(n):
            right_val = nums[r]
            sub_sum += right_val

            # ??
            while sub_sum > target:
                left_val = nums[l]
                sub_sum -= left_val
                l += 1

            if sub_sum == target:
                if lonest_sub_len == -1:
                    lonest_sub_len = r - l + 1
                else:
                    lonest_sub_len = max(lonest_sub_len,  r - l + 1)


        if lonest_sub_len > 0:
            return len(nums) - lonest_sub_len
        return -1


# V0-1
# IDEA: `subtract` + slide window (gpt)
class Solution(object):
    def minOperations(self, nums, x):
        total = sum(nums)
        target = total - x

        if target < 0:
            return -1
        if target == 0:
            return len(nums)

        longest_sub_len = -1

        l = 0
        sub_sum = 0

        for r in range(len(nums)):
            sub_sum += nums[r]

            while l <= r and sub_sum > target:
                sub_sum -= nums[l]
                l += 1

            if sub_sum == target:
                longest_sub_len = max(longest_sub_len, r - l + 1)

        if longest_sub_len == -1:
            return -1

        return len(nums) - longest_sub_len


# V0-2
# IDEA: `subtract` + slide window (GEMINI)
class Solution(object):
    def minOperations(self, nums, x):
        """
        :type nums: List[int]
        :type x: int
        :rtype: int
        """
        target = sum(nums) - x
        
        # Edge case: If x is greater than the total sum, it's impossible
        if target < 0:
            return -1
            
        longest_sub_len = -1
        n = len(nums)
        l = 0
        sub_sum = 0
        
        # Sliding window
        for r in range(n):
            sub_sum += nums[r]
            
            # Shrink the window if our sum is too large
            # (Make sure l doesn't pass r!)
            while l <= r and sub_sum > target:
                sub_sum -= nums[l]
                l += 1
                
            # If we found the exact target, track the MAXIMUM length
            if sub_sum == target:
                longest_sub_len = max(longest_sub_len, r - l + 1)
                
        # If we found a valid subarray, return the total elements minus the subarray length
        if longest_sub_len != -1:
            return n - longest_sub_len
            
        return -1
       