"""

2395. Find Subarrays With Equal Sum
Easy

Given a 0-indexed integer array nums, determine whether there exist two subarrays of length 2 with equal sum. Note that the two subarrays must begin at different indices.

Return true if these subarrays exist, and false otherwise.

A subarray is a contiguous non-empty sequence of elements within an array.


Example 1:

Input: nums = [4,2,4]
Output: true
Explanation: The subarrays with elements [4,2] and [2,4] have the same sum of 6.

Example 2:

Input: nums = [1,2,3,4,5]
Output: false
Explanation: No two subarrays of size 2 have the same sum.

Example 3:

Input: nums = [0,0,0]
Output: true
Explanation: The subarrays [nums[0],nums[1]] and [nums[1],nums[2]] have the same sum of 0.
Note that even though the subarrays have the same content, the two subarrays are considered different because they are in different positions in the original array.


Constraints:

2 <= nums.length <= 1000
-10^9 <= nums[i] <= 10^9

"""

# V0
# IDEA : HASH TABLE (remember every adjacent-pair sum we have already seen)
#
#   a "subarray of length 2" starting at i is just (nums[i], nums[i+1]),
#   so there are only n-1 candidate sums. two DIFFERENT start indices give
#   equal sums  <=>  some sum value repeats.
#
#   NOTE : the two subarrays only need different START indices, they are
#          allowed to overlap ([0,0,0] -> (0,0) at i=0 and i=1 both sum 0).
#
# time = O(n), space = O(n)
class Solution(object):
    def findSubarrays(self, nums):
        seen = set()
        for i in range(len(nums) - 1):
            s = nums[i] + nums[i + 1]
            if s in seen:
                return True
            seen.add(s)
        return False
