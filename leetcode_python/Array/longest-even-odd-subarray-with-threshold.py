"""

2760. Longest Even Odd Subarray With Threshold
Easy

You are given a 0-indexed integer array nums and an integer threshold.

Find the length of the longest subarray of nums starting at index l and ending at index r (0 <= l <= r < nums.length) that satisfies the following conditions:

nums[l] % 2 == 0
For all indices i in the range [l, r - 1], nums[i] % 2 != nums[i + 1] % 2
For all indices i in the range [l, r], nums[i] <= threshold

Return an integer denoting the length of the longest such subarray.

Note: A subarray is a contiguous non-empty sequence of elements within an array.


Example 1:

Input: nums = [3,2,5,4], threshold = 5
Output: 3
Explanation: In this example, we can select the subarray that starts at l = 1 and ends at r = 3 => [2,5,4]. This subarray satisfies the conditions.
Hence, the answer is the length of the subarray, 3. We can show that 3 is the maximum possible achievable length.

Example 2:

Input: nums = [1,2], threshold = 2
Output: 1
Explanation: In this example, we can select the subarray that starts at l = 1 and ends at r = 1 => [2].
It satisfies all the conditions and we can show that 1 is the maximum possible achievable length.

Example 3:

Input: nums = [2,3,4,5], threshold = 4
Output: 3
Explanation: In this example, we can select the subarray that starts at l = 0 and ends at r = 2 => [2,3,4].
It satisfies all the conditions.
Hence, the answer is the length of the subarray, 3. We can show that 3 is the maximum possible achievable length.


Constraints:

1 <= nums.length <= 100
1 <= nums[i] <= 100
1 <= threshold <= 100

"""

# V0
# IDEA : GREEDY SCAN WITHOUT BACKTRACKING (two pointers)
#
#  from a valid start l we extend r as far as the alternating-parity rule and
#  the threshold rule allow -- and that maximal stretch is unique.
#
#  the key observation that turns the O(n^2) brute force into O(n): once a
#  stretch [l, r) stops at r, NO index strictly inside (l, r) can be a better
#  start. Any such index either has odd value (illegal start) or, if even, its
#  own stretch is a suffix of [l, r) and hence shorter. So we may jump
#  l = r directly instead of restarting at l + 1.
#
#   NOTE : a start needs BOTH nums[l] % 2 == 0 AND nums[l] <= threshold;
#          if either fails we just move on by one.
#   NOTE : the parity test compares nums[r] with nums[r-1], so the very first
#          extension already checks against the (even) start element.
#
# time = O(n), space = O(1)
class Solution(object):
    def longestAlternatingSubarray(self, nums, threshold):
        n = len(nums)
        res = 0
        l = 0
        while l < n:
            if nums[l] % 2 != 0 or nums[l] > threshold:
                l += 1
                continue
            r = l + 1
            while r < n and nums[r] <= threshold and nums[r] % 2 != nums[r - 1] % 2:
                r += 1
            res = max(res, r - l)
            l = r
        return res


# V0-1
# IDEA : BRUTE FORCE - VALIDATE EVERY (l, r) PAIR FROM SCRATCH
#
#   the definition, transcribed literally: try every subarray and re-check the
#   three rules on it. n <= 100, so ~10^6 checks is fine and this is the
#   reference the smarter versions get compared against.
#
#   the only subtlety is that the parity rule is checked between CONSECUTIVE
#   elements inside [l, r], while the even-start rule applies to nums[l] alone.
#
# time = O(n^3)
# space = O(1)
class Solution(object):
    def longestAlternatingSubarray(self, nums, threshold):
        n = len(nums)

        def valid(l, r):
            if nums[l] % 2 != 0:
                return False
            for i in range(l, r + 1):
                if nums[i] > threshold:
                    return False
                if i > l and nums[i] % 2 == nums[i - 1] % 2:
                    return False
            return True

        res = 0
        for l in range(n):
            for r in range(l, n):
                if valid(l, r):
                    res = max(res, r - l + 1)
        return res


# V0-2
# IDEA : DP RIGHT-TO-LEFT ON "LONGEST ALTERNATING RUN STARTING HERE"
#
#   define, ignoring the even-start rule for a moment,
#
#       dp[i] = length of the longest run starting at i where every element is
#               <= threshold and adjacent parities alternate
#
#   the recurrence only needs its right neighbour:
#
#       nums[i] > threshold                      -> dp[i] = 0
#       parity(nums[i+1]) != parity(nums[i])     -> dp[i] = 1 + dp[i+1]
#       otherwise                                -> dp[i] = 1
#
#   NOTE : the middle case is still correct when nums[i+1] > threshold, because
#          then dp[i+1] == 0 and the run correctly stops at i.
#
#   the even-start rule is then just a FILTER on which dp values may be taken,
#   so the answer is max(dp[i]) over even, in-threshold starts (0 if none).
#
# time = O(n)
# space = O(n)
class Solution(object):
    def longestAlternatingSubarray(self, nums, threshold):
        n = len(nums)
        dp = [0] * n
        for i in range(n - 1, -1, -1):
            if nums[i] > threshold:
                dp[i] = 0
            elif i + 1 < n and nums[i + 1] % 2 != nums[i] % 2:
                dp[i] = 1 + dp[i + 1]
            else:
                dp[i] = 1

        res = 0
        for i in range(n):
            if nums[i] % 2 == 0 and nums[i] <= threshold:
                res = max(res, dp[i])
        return res
