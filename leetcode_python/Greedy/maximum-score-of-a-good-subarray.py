"""

1793. Maximum Score of a Good Subarray
Hard

You are given an array of integers nums (0-indexed) and an integer k.

The score of a subarray (i, j) is defined as min(nums[i], nums[i+1], ..., nums[j]) * (j - i + 1). A good subarray is a subarray where i <= k <= j.

Return the maximum possible score of a good subarray.

Example 1:

Input: nums = [1,4,3,7,4,5], k = 3
Output: 15
Explanation: The optimal subarray is (1, 5) with a score of min(4,3,7,4,5) * (5-1+1) = 3 * 5 = 15.

Example 2:

Input: nums = [5,5,4,5,4,1,1,1], k = 0
Output: 20
Explanation: The optimal subarray is (0, 4) with a score of min(5,5,4,5,4) * (4-0+1) = 4 * 5 = 20.

Constraints:

1 <= nums.length <= 10^5
1 <= nums[i] <= 2 * 10^4
0 <= k < nums.length

"""

# V0
# IDEA : TWO POINTERS EXPANDING OUT OF k (greedily keep the larger side)
#
#   every good subarray contains k, so start with the window [k, k] and widen
#   it one step at a time. the window minimum can only go down, and the length
#   always grows by 1, so the best move is to swallow the LARGER of the two
#   neighbours - that keeps the minimum as high as possible.
#   record min * length after each step; every candidate window that could ever
#   be optimal is produced along the way.
#
# time = O(n), space = O(1)
class Solution(object):
    def maximumScore(self, nums, k):
        n = len(nums)
        i = j = k
        mn = nums[k]
        res = mn
        while i > 0 or j < n - 1:
            if i == 0:
                j += 1
            elif j == n - 1:
                i -= 1
            elif nums[i - 1] > nums[j + 1]:
                i -= 1
            else:
                j += 1
            if nums[i] < mn:
                mn = nums[i]
            if nums[j] < mn:
                mn = nums[j]
            res = max(res, mn * (j - i + 1))
        return res
