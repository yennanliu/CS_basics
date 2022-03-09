"""

53. Maximum Subarray
Easy

Given an integer array nums, find the contiguous subarray (containing at least one number) which has the largest sum and return its sum.

A subarray is a contiguous part of an array.

 

Example 1:

Input: nums = [-2,1,-3,4,-1,2,1,-5,4]
Output: 6
Explanation: [4,-1,2,1] has the largest sum = 6.
Example 2:

Input: nums = [1]
Output: 1
Example 3:

Input: nums = [5,4,-1,7,8]
Output: 23
 

Constraints:

1 <= nums.length <= 105
-104 <= nums[i] <= 104
 

Follow up: If you have figured out the O(n) solution, try coding another solution using the divide and conquer approach, which is more subtle.

"""

# V0
# IDEA : DP
# DP EQUATION :
# -> dp[i+1] = dp[i] + s[i+1] (if dp[i] >= 0 )
# -> dp[i+1] = s[i]           (if dp[i] < 0 )
class Solution(object):
    def maxSubArray(self, nums):
        """
        :type nums: List[int]
        :rtype: int
        """
        if not nums:
            return 0
        dp = [nums[0] for i in range(len(nums))]
        max_result = nums[0]  # if start from nums[0], then must get smaller if there is minus number; will get larger if interger
        for i in range(1, len(nums)):
            if dp[i-1] < 0:
                dp[i] = nums[i]
            else:
                dp[i] = dp[i-1] + nums[i]
            max_result = max(max_result, dp[i])
        return max_result

# V0'
# IDEA : DP 
class Solution(object):
    def maxSubArray(self, nums):
        cumsum = nums[0]
        max_ = cumsum
        if not nums: return 0 
        for i in range(1, len(nums)):
            if cumsum < 0:
                cumsum = 0
            cumsum += nums[i]
            max_ = max(max_, cumsum)
        return max_ 

# V0'
# IDEA : BRUTE FORCE (TLE)
class Solution(object):
    def maxSubArray(self, nums):
        # edge case
        if len(nums) == 1:
            return nums[0]
        if not nums:
            return 0
        res = -float('inf')
        for i in range(len(nums)):
            tmp = []
            for j in range(i, len(nums)):
                tmp.append(nums[j])
                res = max(res, sum(tmp))
        return res

# V1''
# https://blog.csdn.net/qqxx6661/article/details/78167981
# IDEA : DP
# DP EQUATION :
# -> dp[i+1] = dp[i] + s[i+1] (if dp[i] >= 0 )
# -> dp[i+1] = s[i]           (if dp[i] < 0 )
class Solution(object):
    def maxSubArray(self, nums):
        """
        :type nums: List[int]
        :rtype: int
        """
        if not nums:
            return 0
        current = nums[0]
        m = current
        for i in range(1, len(nums)):
            if current < 0:
                current = 0
            current += nums[i]
            m = max(current, m)
        return m

# V1''
# https://blog.csdn.net/qqxx6661/article/details/78167981
# IDEA : DP
# DP STATUS EQUATION : 
# dp[i] = dp[i-1] + s[i] (dp[i-1] >= 0)
# dp[i] = s[i] (dp[i-1] < 0)
class Solution(object):
    def maxSubArray(self, nums):
        """
        :type nums: List[int]
        :rtype: int
        """
        if not nums:
            return 0
        dp = [nums[0] for i in range(len(nums))]
        max_result = nums[0]  # if start from nums[0], then must get smaller if there is minus number; will get larger if interger
        for i in range(1, len(nums)):
            if dp[i-1] < 0:
                dp[i] = nums[i]
            else:
                dp[i] = dp[i-1] + nums[i]
            if max_result < dp[i]:
                max_result = dp[i]
        return max_result

# V1 
# https://blog.csdn.net/hyperbolechi/article/details/43038749
# IDEA : BRUTE FORCE
class Solution:
    def maxSubArray(self, arr):
        maxval=-10000  
        for i in range(len(arr)):
            for j in range(i,len(arr)):
                if maxval<sum(arr[i:j]):
                    print((i,j))
                    maxval=max(maxval,sum(arr[i:j]))
                    result=arr[i:j]
        return result

# V1
# IDEA :  Optimized Brute Force
# https://leetcode.com/problems/maximum-subarray/solution/
class Solution:
    def maxSubArray(self, nums: List[int]) -> int:
        max_subarray = -math.inf
        for i in range(len(nums)):
            current_subarray = 0
            for j in range(i, len(nums)):
                current_subarray += nums[j]
                max_subarray = max(max_subarray, current_subarray)
        
        return max_subarray

# V1
# IDEA : Dynamic Programming, Kadane's Algorithm
# https://leetcode.com/problems/maximum-subarray/solution/
class Solution:
    def maxSubArray(self, nums: List[int]) -> int:
        # Initialize our variables using the first element.
        current_subarray = max_subarray = nums[0]
        
        # Start with the 2nd element since we already used the first one.
        for num in nums[1:]:
            # If current_subarray is negative, throw it away. Otherwise, keep adding to it.
            current_subarray = max(num, current_subarray + num)
            max_subarray = max(max_subarray, current_subarray)
        
        return max_subarray

# V1
# IDEA : Divide and Conquer
# https://leetcode.com/problems/maximum-subarray/solution/
class Solution:
    def maxSubArray(self, nums: List[int]) -> int:
        def findBestSubarray(nums, left, right):
            # Base case - empty array.
            if left > right:
                return -math.inf

            mid = (left + right) // 2
            curr = best_left_sum = best_right_sum = 0

            # Iterate from the middle to the beginning.
            for i in range(mid - 1, left - 1, -1):
                curr += nums[i]
                best_left_sum = max(best_left_sum, curr)

            # Reset curr and iterate from the middle to the end.
            curr = 0
            for i in range(mid + 1, right + 1):
                curr += nums[i]
                best_right_sum = max(best_right_sum, curr)

            # The best_combined_sum uses the middle element and
            # the best possible sum from each half.
            best_combined_sum = nums[mid] + best_left_sum + best_right_sum

            # Find the best subarray possible from both halves.
            left_half = findBestSubarray(nums, left, mid - 1)
            right_half = findBestSubarray(nums, mid + 1, right)

            # The largest of the 3 is the answer for any given input array.
            return max(best_combined_sum, left_half, right_half)
        
        # Our helper function is designed to solve this problem for
        # any array - so just call it using the entire input!
        return findBestSubarray(nums, 0, len(nums) - 1)

# V2 
# Time:  O(n)
# Space: O(1)
class Solution(object):
    def maxSubArray(self, nums):
        """
        :type nums: List[int]
        :rtype: int
        """
        max_nums = max(nums)
        if max_nums < 0:
            return max_nums
        global_max, local_max = 0, 0
        for x in nums:
            local_max = max(0, local_max + x)
            global_max = max(global_max, local_max)
        return global_max