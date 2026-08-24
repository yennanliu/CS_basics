"""

2552. Count Increasing Quadruplets
Hard

Given a 0-indexed integer array nums of size n containing all numbers from 1 to n, return the number of increasing quadruplets.

A quadruplet (i, j, k, l) is increasing if:

- 0 <= i < j < k < l < n, and
- nums[i] < nums[k] < nums[j] < nums[l].


Example 1:

Input: nums = [1,3,2,4,5]
Output: 2
Explanation:
- When i = 0, j = 1, k = 2, and l = 3, nums[i] < nums[k] < nums[j] < nums[l].
- When i = 0, j = 1, k = 2, and l = 4, nums[i] < nums[k] < nums[j] < nums[l].
There are no other quadruplets, so we return 2.

Example 2:

Input: nums = [1,2,3,4]
Output: 0
Explanation: There exists only one quadruplet with i = 0, j = 1, k = 2, l = 3, but since nums[j] < nums[k], we return 0.


Constraints:

4 <= nums.length <= 4000
1 <= nums[i] <= nums.length
All the integers of nums are unique. nums is a permutation.

"""

# V0
# IDEA : DP OVER PAIRS (O(n^2) time, O(n) space)
#
#   the pattern is nums[i] < nums[k] < nums[j] < nums[l] with i < j < k < l,
#   i.e. a "132"-like triple (i, j, k) extended by an l on the right.
#
#   dp[j] = number of triples (i, j, k) with i < j < k and
#           nums[i] < nums[k] < nums[j]        (a completed "132" at pivot j)
#
#   we sweep an outer index y (the newest element) and an inner index x < y:
#     * nums[x] > nums[y]  -> (x, y) plays the role of (j, k):
#         dp[x] += cnt, where cnt = #{i < x : nums[i] < nums[y]}
#     * nums[x] < nums[y]  -> (x, y) plays the role of (j, l):
#         every triple already stored in dp[x] used some k < y, and
#         nums[y] > nums[x], so it extends into a valid quadruplet
#         => ans += dp[x]; then cnt += 1 (x can serve as an "i" later)
#
#   NOTE : cnt is maintained incrementally while x scans left->right, so it
#          is exactly #{i < x : nums[i] < nums[y]} at the moment x is read.
#   NOTE : within one outer step each x takes exactly ONE branch, so a dp[x]
#          read in the second branch never includes an update made for the
#          same y — the ordering k < l is preserved.
#   NOTE : values are distinct (a permutation), so no equal-value case exists.
#
"""

DP def
    the pattern is nums[i] < nums[k] < nums[j] < nums[l] with i < j < k < l,
    i.e. a "132"-like triple (i, j, k) extended by an l on the right

    dp[j]: number of triples (i, j, k) with i < j < k and

           nums[i] < nums[k] < nums[j]      (a completed "132" at pivot j)

DP eq

     sweep the newest index y, and x < y inside it, keeping
     cnt = #{ i < x : nums[i] < nums[y] }

        if nums[x] > nums[y]:   # (x, y) acts as (j, k)
            dp[x] += cnt

        if nums[x] < nums[y]:   # (x, y) acts as (j, l)
            ans += dp[x]
            cnt += 1


    -> e.g. within one outer step each x takes exactly ONE branch, so a
              dp[x] read never includes an update made for the same y
              -> the ordering k < l is preserved

     ans = total accumulated

"""
# time = O(n^2), space = O(n)
class Solution(object):
    def countQuadruplets(self, nums):
        n = len(nums)
        dp = [0] * n
        ans = 0
        for y in range(n):
            cnt = 0
            cur = nums[y]
            for x in range(y):
                if nums[x] < cur:
                    ans += dp[x]
                    cnt += 1
                else:
                    dp[x] += cnt
        return ans
