"""

1063. Number of Valid Subarrays
Hard

Given an integer array nums, return the number of non-empty subarrays with the
leftmost element of the subarray not larger than other elements in the subarray.

A subarray is a contiguous part of an array.


Example 1:

Input: nums = [1,4,2,5,3]
Output: 11
Explanation: There are 11 valid subarrays: [1],[4],[2],[5],[3],[1,4],[2,5],[1,4,2],[2,5,3],[1,4,2,5],[1,4,2,5,3].

Example 2:

Input: nums = [3,2,1]
Output: 3
Explanation: The 3 valid subarrays are: [3],[2],[1].

Example 3:

Input: nums = [2,2,2]
Output: 6
Explanation: There are 6 valid subarrays: [2],[2],[2],[2,2],[2,2],[2,2,2].


Constraints:

1 <= nums.length <= 5 * 10^4
0 <= nums[i] <= 10^5

"""

# V0
# IDEA : MONOTONIC STACK (next strictly smaller element to the right)
#
#  a subarray starting at i is valid <-> every element in it is >= nums[i]
#  -> it may extend up to (but not include) the FIRST j > i with nums[j] < nums[i]
#  -> # of valid subarrays starting at i = right[i] - i, where
#     right[i] = index of that first smaller element (n if none)
#
#  scanning from the right with a stack that keeps indices of
#  increasing values gives every right[i] in O(n)
# time = O(n)
# space = O(n)
class Solution(object):
    def validSubarrays(self, nums):
        n = len(nums)
        stack = []      # indices, values strictly increasing from top to bottom
        ans = 0
        for i in range(n - 1, -1, -1):
            # NOTE !!! pop with `>=` so equal values are NOT treated as a blocker
            while stack and nums[stack[-1]] >= nums[i]:
                stack.pop()
            right = stack[-1] if stack else n
            ans += right - i
            stack.append(i)
        return ans
