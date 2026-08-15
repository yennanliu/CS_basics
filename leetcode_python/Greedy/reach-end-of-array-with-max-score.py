"""

3282. Reach End of Array With Max Score
Medium

You are given an integer array nums of length n.

Your goal is to start at index 0 and reach index n - 1. You can only jump to indices greater than your current index.

The score for a jump from index i to index j is calculated as (j - i) * nums[i].

Return the maximum possible total score by the time you reach the last index.


Example 1:

Input: nums = [1,3,1,5]
Output: 7
Explanation:
First, jump to index 1 and then jump to the last index. The final score is 1 * 1 + 2 * 3 = 7.

Example 2:

Input: nums = [4,3,1,3,2]
Output: 16
Explanation:
Jump directly to the last index. The final score is 4 * 4 = 16.


Constraints:

1 <= nums.length <= 10^5
1 <= nums[i] <= 10^5

"""

# V0
# IDEA : EVERY UNIT OF DISTANCE IS PAID AT THE BEST RATE SEEN SO FAR
#
#   a jump from i to j earns nums[i] for each of the (j - i) steps it covers,
#   so the total is really "sum over the n-1 unit steps of the value of
#   whatever index we were standing on".
#
#   splitting a jump is always allowed, so the best rate for the step from
#   position t to t+1 is the largest nums value among indices 0..t — we can
#   always be standing there.
#
#   so sweep once with a running maximum and add it per step; no DP needed.
#
# time = O(n), space = O(1)
class Solution(object):
    def findMaximumScore(self, nums):
        best = 0
        total = 0
        for i in range(len(nums) - 1):
            if nums[i] > best:
                best = nums[i]
            total += best
        return total
