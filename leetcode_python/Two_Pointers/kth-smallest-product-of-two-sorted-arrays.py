"""

2040. Kth Smallest Product of Two Sorted Arrays
Hard

Given two sorted 0-indexed integer arrays nums1 and nums2 as well as an integer k, return the kth (1-based) smallest product of nums1[i] * nums2[j] where 0 <= i < nums1.length and 0 <= j < nums2.length.


Example 1:

Input: nums1 = [2,5], nums2 = [3,4], k = 2
Output: 8
Explanation: The 2 smallest products are:
- nums1[0] * nums2[0] = 2 * 3 = 6
- nums1[0] * nums2[1] = 2 * 4 = 8
The 2nd smallest product is 8.

Example 2:

Input: nums1 = [-4,-2,0,3], nums2 = [2,4], k = 6
Output: 0
Explanation: The 6 smallest products are:
- nums1[0] * nums2[1] = (-4) * 4 = -16
- nums1[0] * nums2[0] = (-4) * 2 = -8
- nums1[1] * nums2[1] = (-2) * 4 = -8
- nums1[1] * nums2[0] = (-2) * 2 = -4
- nums1[2] * nums2[0] = 0 * 2 = 0
- nums1[2] * nums2[1] = 0 * 4 = 0
The 6th smallest product is 0.

Example 3:

Input: nums1 = [-2,-1,0,1,2], nums2 = [-3,-1,2,4,5], k = 3
Output: -6
Explanation: The 3 smallest products are:
- nums1[0] * nums2[4] = (-2) * 5 = -10
- nums1[0] * nums2[3] = (-2) * 4 = -8
- nums1[4] * nums2[0] = 2 * (-3) = -6
The 3rd smallest product is -6.


Constraints:

1 <= nums1.length, nums2.length <= 5 * 10^4
-10^5 <= nums1[i], nums2[j] <= 10^5
k >= 1
k <= nums1.length * nums2.length
nums1 and nums2 are sorted.

"""

# V0
# IDEA : BINARY SEARCH ON THE ANSWER + O(n log m) COUNTER
#
#   count(x) = how many products are <= x. it is monotone in x, so binary
#   search x over [-10^10, 10^10] for the smallest x with count(x) >= k.
#
#   counting, for one a in nums1 (nums2 is sorted ascending) :
#     a > 0 : a * b <= x  <=>  b <= x / a  -> take a prefix of nums2
#             (use floor division: bisect_right on floor(x / a))
#     a < 0 : a * b <= x  <=>  b >= x / a  -> take a suffix of nums2
#             (use ceil division)
#     a == 0: every b works iff x >= 0, contributing len(nums2) or 0
#
#   NOTE : integer floor/ceil division is what keeps this exact — floats
#          would lose precision at 10^10.
#
# time = O(n log m log R), space = O(1)
import bisect


class Solution(object):
    def kthSmallestProduct(self, nums1, nums2, k):
        m = len(nums2)

        def count(x):
            # how many pairs give a product <= x
            res = 0
            for a in nums1:
                if a > 0:
                    # b <= floor(x / a)
                    res += bisect.bisect_right(nums2, x // a)
                elif a < 0:
                    # b >= ceil(x / a)
                    res += m - bisect.bisect_left(nums2, -((-x) // a))
                else:
                    if x >= 0:
                        res += m
            return res

        lo, hi = -10 ** 10, 10 ** 10
        while lo < hi:
            mid = (lo + hi) // 2
            if count(mid) >= k:
                hi = mid
            else:
                lo = mid + 1
        return lo
