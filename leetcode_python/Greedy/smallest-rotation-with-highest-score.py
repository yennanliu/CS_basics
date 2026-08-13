"""

798. Smallest Rotation with Highest Score
Hard

You are given an array nums. You can rotate it by a non-negative integer k so that
the array becomes [nums[k], nums[k + 1], ... nums[nums.length - 1], nums[0], nums[1], ..., nums[k-1]].
Afterward, any entries that are less than or equal to their index are worth one point.

For example, if we have nums = [2,4,1,3,0], and we rotate by k = 2, it becomes
[1,3,0,2,4]. This is worth 3 points because 1 > 0 [no points], 3 > 1 [no points],
0 <= 2 [one point], 2 <= 3 [one point], 4 <= 4 [one point].

Return the rotation index k that corresponds to the highest score we can achieve
if we rotated nums by it. If there are multiple answers, return the smallest such index k.


Example 1:

Input: nums = [2,3,1,4,0]
Output: 3
Explanation: Scores for each k are listed below:
k = 0,  nums = [2,3,1,4,0],    score 2
k = 1,  nums = [3,1,4,0,2],    score 3
k = 2,  nums = [1,4,0,2,3],    score 3
k = 3,  nums = [4,0,2,3,1],    score 4
k = 4,  nums = [0,2,3,1,4],    score 3
So we should choose k = 3, which has the highest score.

Example 2:

Input: nums = [1,3,0,2,4]
Output: 0
Explanation: nums will always have 3 points no matter how it shifts.
So we will choose the smallest k, which is 0.


Constraints:

1 <= nums.length <= 10^5
0 <= nums[i] < nums.length

"""

# V0
# IDEA : DIFFERENCE ARRAY (circular interval add)
#
#   After rotating by k, the element originally at index i moves to
#   index (i - k) mod n, and it scores when nums[i] <= (i - k) mod n.
#
#   For each i, the set of k values that make it score is a contiguous
#   (circular) range of length n - nums[i]. Instead of computing the
#   absolute score, we only need RELATIVE scores between different k,
#   so we can mark each range on a difference array d:
#
#       l = (i + 1) % n
#       r = (n + i + 1 - nums[i]) % n
#       d[l] += 1 ; d[r] -= 1
#
#   Then prefix-sum d and take the FIRST index reaching the maximum.
#
# time = O(n)
# space = O(n)
class Solution(object):
    def bestRotation(self, nums):
        n = len(nums)
        d = [0] * n

        for i, v in enumerate(nums):
            l = (i + 1) % n
            r = (n + i + 1 - v) % n
            d[l] += 1
            d[r] -= 1

        # NOTE: `cur` is a RELATIVE score (off by a constant), so it can be
        #       negative -> seed `best` with -inf, not -1
        best, ans, cur = float('-inf'), 0, 0
        for k in range(n):
            cur += d[k]
            # strict ">" keeps the SMALLEST k on ties
            if cur > best:
                best = cur
                ans = k
        return ans
