"""

2809. Minimum Time to Make Array Sum At Most x
Hard

You are given two 0-indexed integer arrays nums1 and nums2 of equal length. Every second, for all indices 0 <= i < nums1.length, value of nums1[i] is incremented by nums2[i]. After this is done, you can do the following operation:

Choose an index 0 <= i < nums1.length and make nums1[i] = 0.

You are also given an integer x.

Return the minimum time in which you can make the sum of all elements of nums1 to be less than or equal to x, or -1 if this is not possible.


Example 1:

Input: nums1 = [1,2,3], nums2 = [1,2,3], x = 4
Output: 3
Explanation:
For the 1st second, we apply the operation on i = 0. Therefore nums1 = [0,2+2,3+3] = [0,4,6].
For the 2nd second, we apply the operation on i = 1. Therefore nums1 = [0+1,0,6+3] = [1,0,9].
For the 3rd second, we apply the operation on i = 2. Therefore nums1 = [1+1,0+2,0] = [2,2,0].
Now sum of nums1 = 4. It can be shown that these operations are optimal, so we return 3.

Example 2:

Input: nums1 = [1,2,3], nums2 = [3,3,3], x = 4
Output: -1
Explanation: It can be shown that the sum of nums1 will always be greater than x, no matter which operations are performed.


Constraints:

1 <= nums1.length <= 10^3
1 <= nums1[i] <= 10^3
0 <= nums2[i] <= 10^3
nums1.length == nums2.length
0 <= x <= 10^6

"""

# V0
# IDEA : SORT BY nums2 + KNAPSACK-STYLE DP OVER "HOW MANY WE ZERO OUT"
#
#   after t seconds with NO operation the sum would be
#       s1 + s2 * t          (s1 = sum(nums1), s2 = sum(nums2))
#   each index we zero out at second j removes, from the final sum,
#       nums1[i] + nums2[i] * j
#   (its own start value plus the j increments it accumulated by then).
#
#   two structural facts :
#     - zeroing the same index twice is wasted : only the last reset counts,
#       so we reset at most n distinct indices and the answer t is in [0, n]
#     - GREEDY ORDER : if we decide to reset a set of indices, the one with
#       the LARGER nums2 should be reset LATER (bigger multiplier j on a
#       bigger growth rate). so sorting by nums2 ascending fixes an optimal
#       order and turns the problem into a subset-selection dp.
#
#   dp : after considering the first i sorted items, f[j] = the maximum
#   total reduction achievable by resetting exactly j of them
#       f[j] = max(f[j], f[j-1] + a + b * j)
#   where the j-th reset happens at second j.
#
#   then the smallest t with  s1 + s2 * t - f[t] <= x  is the answer;
#   if no t in [0, n] works, return -1.
#
#   NOTE : the inner loop must run j from n DOWN to 1 for the 1-D rolling
#          array, otherwise f[j-1] would already be this item's value and
#          the item could be reset twice.
#
#   NOTE : t is never worth pushing beyond n - past that point every index
#          has already been reset once and extra seconds only add growth.
#
#   NOTE : sort the PAIRS by nums2, not the two arrays independently - the
#          (nums1[i], nums2[i]) pairing must be preserved.
#
"""

DP def
    after t seconds with NO operation the sum would be s1 + s2 * t.
    zeroing index i at second j removes  nums1[i] + nums2[i] * j  from the
    final sum (its start value plus the j increments it had accumulated).

    two structural facts:
      - resetting the same index twice is wasted, so at most n indices are
        reset and the answer t lies in [0, n]
      - GREEDY ORDER: among the reset indices the one with the LARGER nums2
        should be reset LATER (bigger multiplier on a bigger growth rate)
        -> sorting the PAIRS by nums2 ascending fixes an optimal order

    f[j]: MAX total reduction achievable by resetting exactly j of the

          sorted items so far   (the j-th reset happens at second j)

DP eq

     f[j] = max( f[j], f[j-1] + a + b * j )       # (a, b) = the item


    -> e.g. NOTE !!! the inner loop must run j from n DOWN to 1 for the 1-D
              rolling array, otherwise f[j-1] already holds THIS item and it
              would be reset twice

     ans = the smallest t in [0, n] with  s1 + s2 * t - f[t] <= x,  else -1

"""
# time = O(n^2), space = O(n)
class Solution(object):
    def minimumTime(self, nums1, nums2, x):
        n = len(nums1)

        # larger growth rate -> reset later -> larger multiplier
        pairs = sorted(zip(nums1, nums2), key=lambda p: p[1])

        f = [0] * (n + 1)
        for a, b in pairs:
            # descending j : each item usable at most once
            for j in range(n, 0, -1):
                cand = f[j - 1] + a + b * j
                if cand > f[j]:
                    f[j] = cand

        s1 = sum(nums1)
        s2 = sum(nums2)
        for t in range(n + 1):
            if s1 + s2 * t - f[t] <= x:
                return t
        return -1
