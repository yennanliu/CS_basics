"""

2333. Minimum Sum of Squared Difference
Medium

You are given two positive 0-indexed integer arrays nums1 and nums2, both of length n.

The sum of squared difference of arrays nums1 and nums2 is defined as the sum of (nums1[i] - nums2[i])^2 for each 0 <= i < n.

You are also given two positive integers k1 and k2. You can modify any of the elements of nums1 by +1 or -1 at most k1 times. Similarly, you can modify any of the elements of nums2 by +1 or -1 at most k2 times.

Return the minimum sum of squared difference after modifying array nums1 at most k1 times and modifying array nums2 at most k2 times.

Note: You are allowed to modify the array elements to become negative integers.


Example 1:

Input: nums1 = [1,2,3,4], nums2 = [2,10,20,19], k1 = 0, k2 = 0
Output: 579
Explanation: The elements in nums1 and nums2 cannot be modified because k1 = 0 and k2 = 0.
The sum of square difference will be: (1 - 2)^2 + (2 - 10)^2 + (3 - 20)^2 + (4 - 19)^2 = 579.

Example 2:

Input: nums1 = [1,4,10,12], nums2 = [5,8,6,9], k1 = 1, k2 = 1
Output: 43
Explanation: One way to obtain the minimum sum of square difference is:
- Increase nums1[0] once.
- Increase nums2[2] once.
The minimum of the sum of square difference will be:
(2 - 5)^2 + (4 - 8)^2 + (10 - 7)^2 + (12 - 9)^2 = 43.
Note that, there are other ways to obtain the minimum of the sum of square difference, but there is no way to obtain a sum smaller than 43.


Constraints:

n == nums1.length == nums2.length
1 <= n <= 10^5
0 <= nums1[i], nums2[i] <= 10^5
0 <= k1, k2 <= 10^9

"""

# V0
# IDEA : ONLY abs(nums1[i] - nums2[i]) MATTERS — BINARY SEARCH THE CAP
#
#   a modification on either array moves one difference by 1, so the two
#   budgets merge into a single k = k1 + k2 and the problem becomes "spend k
#   decrements across the diffs to minimise the sum of squares".
#
#   squares are convex, so the optimum LEVELS the largest diffs down. that
#   means the result is characterised by a cap t :
#       cost(t) = sum(max(0, d - t))   must be <= k
#   and cost is monotone, so binary search the smallest feasible t.
#
#   any leftover budget after reaching t is then spent pushing individual
#   entries from t down to t - 1, one unit each.
#
#   NOTE : if k already covers the whole sum of diffs, everything can be
#          zeroed — answer 0.
#
# time = O(n log(max diff)), space = O(n)
class Solution(object):
    def minSumSquareDiff(self, nums1, nums2, k1, k2):
        diffs = [abs(a - b) for a, b in zip(nums1, nums2)]
        k = k1 + k2
        if sum(diffs) <= k:
            return 0

        lo, hi = 0, max(diffs)
        while lo < hi:
            mid = (lo + hi) // 2
            if sum(max(0, d - mid) for d in diffs) <= k:
                hi = mid
            else:
                lo = mid + 1
        t = lo

        spent = sum(max(0, d - t) for d in diffs)
        left = k - spent                       # budget still unused
        at_cap = sum(1 for d in diffs if d >= t)   # entries now sitting at t

        res = sum(d * d for d in diffs if d < t)
        res += (at_cap - left) * t * t + left * (t - 1) * (t - 1)
        return res
