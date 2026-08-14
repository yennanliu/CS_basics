"""

2563. Count the Number of Fair Pairs
Medium

Given a 0-indexed integer array nums of size n and two integers lower and upper, return the number of fair pairs.

A pair (i, j) is fair if:

- 0 <= i < j < n, and
- lower <= nums[i] + nums[j] <= upper


Example 1:

Input: nums = [0,1,7,4,4,5], lower = 3, upper = 6
Output: 6
Explanation: There are 6 fair pairs: (0,3), (0,4), (0,5), (1,3), (1,4), and (1,5).

Example 2:

Input: nums = [1,7,9,2,5], lower = 11, upper = 11
Output: 1
Explanation: There is a single fair pair: (2,3).


Constraints:

1 <= nums.length <= 10^5
nums.length == n
-10^9 <= nums[i] <= 10^9
-10^9 <= lower <= upper <= 10^9

"""

# V0
# IDEA : SORTING + TWO POINTERS (count(<= x) via a shrinking window)
#
#   the pair condition only involves the SUM, so the original index order is
#   irrelevant — we may sort nums freely and count unordered pairs {i, j}.
#
#   answer = (#pairs with sum <= upper) - (#pairs with sum <= lower - 1)
#
#   count_le(x) on a sorted array is a classic two-pointer sweep:
#     l = 0, r = n - 1; if nums[l] + nums[r] <= x then EVERY index in
#     (l, r] pairs with l validly -> add (r - l) and advance l;
#     otherwise the sum is too big, so shrink r.
#
#   NOTE : the >= lower side is handled as "<= lower - 1" and subtracted;
#          this keeps a single helper instead of two different searches.
#   NOTE : the sweep never double counts because each step permanently
#          removes one endpoint from consideration.
#
# time = O(n log n), space = O(1) beyond the sort
class Solution(object):
    def countFairPairs(self, nums, lower, upper):
        nums.sort()

        def count_le(x):
            res = 0
            l, r = 0, len(nums) - 1
            while l < r:
                if nums[l] + nums[r] <= x:
                    res += r - l
                    l += 1
                else:
                    r -= 1
            return res

        return count_le(upper) - count_le(lower - 1)
