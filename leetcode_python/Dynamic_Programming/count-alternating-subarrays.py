"""

3101. Count Alternating Subarrays
Medium

You are given a binary array nums.

We call a subarray alternating if no two adjacent elements in the subarray have the same value.

Return the number of alternating subarrays in nums.


Example 1:

Input: nums = [0,1,1,1]
Output: 5
Explanation:
The following subarrays are alternating: [0], [1], [1], [1], and [0,1].

Example 2:

Input: nums = [1,0,1,0]
Output: 10
Explanation:
Every subarray of the array is alternating. There are 10 possible subarrays that we can choose.


Constraints:

1 <= nums.length <= 10^5
nums[i] is either 0 or 1.

"""

# V0
# IDEA : COUNT THE ALTERNATING SUBARRAYS *ENDING* AT EACH INDEX
#
#   let run be the length of the longest alternating stretch ending at i.
#   every suffix of that stretch is itself alternating and ends at i, so
#   index i contributes exactly `run` subarrays.
#
#   run extends when nums[i] != nums[i-1] and restarts at 1 otherwise, so a
#   single counter suffices and each subarray is counted once — by its right
#   end.
#
"""

DP def
    dp[i]: length of the LONGEST alternating subarray ending at index i

           -> every suffix of that stretch is itself alternating and ends
              at i, so index i contributes exactly dp[i] subarrays

DP eq

     dp[i] = dp[i-1] + 1   if nums[i] != nums[i-1]

     dp[i] = 1             otherwise


    -> e.g.
         ans = sum(dp[i])

     counting by RIGHT END means every subarray is counted exactly once,
     and dp collapses to a single rolling counter -> O(1) space

"""
# time = O(n), space = O(1)
class Solution(object):
    def countAlternatingSubarrays(self, nums):
        res = 0
        run = 0
        for i, v in enumerate(nums):
            run = run + 1 if i and nums[i - 1] != v else 1
            res += run
        return res
