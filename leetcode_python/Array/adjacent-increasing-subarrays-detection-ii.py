"""

3350. Adjacent Increasing Subarrays Detection II
Medium

Given an array nums of n integers, find the maximum value of k for which there exist two adjacent subarrays of length k each, such that both subarrays are strictly increasing. Specifically, check if there are two subarrays of length k starting at indices a and b (a < b), where:

Both subarrays nums[a..a + k - 1] and nums[b..b + k - 1] are strictly increasing.
The subarrays must be adjacent, meaning b = a + k.

Return the maximum possible value of k.

A subarray is a contiguous non-empty sequence of elements within an array.


Example 1:

Input: nums = [2,5,7,8,9,2,3,4,3,1]
Output: 3
Explanation:
The subarray starting at index 2 is [7, 8, 9], which is strictly increasing.
The subarray starting at index 5 is [2, 3, 4], which is also strictly increasing.
These two subarrays are adjacent, and 3 is the maximum possible value of k for which two such adjacent strictly increasing subarrays exist.

Example 2:

Input: nums = [1,2,3,4,4,4,4,5,6,7]
Output: 2
Explanation:
The subarray starting at index 0 is [1, 2], which is strictly increasing.
The subarray starting at index 2 is [3, 4], which is also strictly increasing.
These two subarrays are adjacent, and 2 is the maximum possible value of k for which two such adjacent strictly increasing subarrays exist.


Constraints:

2 <= nums.length <= 2 * 10^5
-10^9 <= nums[i] <= 10^9

"""

# V0
# IDEA : SPLIT INTO MAXIMAL RISING RUNS — THE ANSWER LIVES INSIDE OR ACROSS ONE BOUNDARY
#
#   two adjacent blocks of length k are both strictly increasing in exactly
#   two situations :
#
#       both sit inside ONE maximal rising run    -> k can be run // 2
#       they straddle the boundary between two    -> k can be min(prev, cur)
#
#   so a single pass over the run lengths answers it — no per-k scanning.
#
# time = O(n), space = O(1)
class Solution(object):
    def maxIncreasingSubarrays(self, nums):
        n = len(nums)
        best = 0
        prev_run = 0
        run = 1
        for i in range(1, n):
            if nums[i] > nums[i - 1]:
                run += 1
            else:
                best = max(best, run // 2, min(prev_run, run))
                prev_run = run
                run = 1
        best = max(best, run // 2, min(prev_run, run))
        return best
