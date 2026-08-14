"""

1577. Number of Ways Where Square of Number Is Equal to Product of Two Numbers
Medium

Given two arrays of integers nums1 and nums2, return the number of triplets formed (type 1 and type 2) under the following rules:

Type 1: Triplet (i, j, k) if nums1[i]^2 == nums2[j] * nums2[k] where 0 <= i < nums1.length and 0 <= j < k < nums2.length.
Type 2: Triplet (i, j, k) if nums2[i]^2 == nums1[j] * nums1[k] where 0 <= i < nums2.length and 0 <= j < k < nums1.length.

Example 1:

Input: nums1 = [7,4], nums2 = [5,2,8,9]
Output: 1
Explanation: Type 1: (1, 1, 2), nums1[1]^2 = nums2[1] * nums2[2]. (4^2 = 2 * 8).

Example 2:

Input: nums1 = [1,1], nums2 = [1,1,1]
Output: 9
Explanation: All Triplets are valid, because 1^2 = 1 * 1.
Type 1: (0,0,1), (0,0,2), (0,1,2), (1,0,1), (1,0,2), (1,1,2).  nums1[i]^2 = nums2[j] * nums2[k].
Type 2: (0,0,1), (1,0,1), (2,0,1). nums2[i]^2 = nums1[j] * nums1[k].

Example 3:

Input: nums1 = [7,7,8,3], nums2 = [1,2,9,7]
Output: 2
Explanation: There are 2 valid triplets.
Type 1: (3,0,2).  nums1[3]^2 = nums2[0] * nums2[2].
Type 2: (3,0,1).  nums2[3]^2 = nums1[0] * nums1[1].

Constraints:

1 <= nums1.length, nums2.length <= 1000
1 <= nums1[i], nums2[i] <= 10^5

"""

# V0
# IDEA : HASH TABLE (count value pairs whose product equals x*x)
#
#   for a target t = x*x, count the unordered index pairs (j < k) of the
#   other array with nums[j] * nums[k] == t :
#     - walk the DISTINCT values v of that array, let o = t // v
#     - v == o -> C(cnt[v], 2) pairs
#     - v <  o -> cnt[v] * cnt[o] pairs   (v < o keeps each pair once)
#   NOTE : do it in both directions and add the two totals.
#
# time = O((n1 + n2) * D), space = O(D), D = #distinct values
from collections import Counter
class Solution(object):
    def numTriplets(self, nums1, nums2):
        def count(nums, cnt):
            res = 0
            for x in nums:
                t = x * x
                for v, c in cnt.items():
                    if t % v:
                        continue
                    o = t // v
                    if o == v:
                        res += c * (c - 1) // 2
                    elif o > v and o in cnt:
                        res += c * cnt[o]
            return res

        c1, c2 = Counter(nums1), Counter(nums2)
        return count(nums1, c2) + count(nums2, c1)
