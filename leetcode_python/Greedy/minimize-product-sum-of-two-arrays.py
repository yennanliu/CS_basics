"""

1874. Minimize Product Sum of Two Arrays
Medium

The product sum of two equal-length arrays a and b is equal to the sum of a[i] * b[i] for all 0 <= i < a.length (0-indexed).

For example, if a = [1,2,3,4] and b = [5,2,3,1], the product sum would be 1*5 + 2*2 + 3*3 + 4*1 = 22.

Given two arrays nums1 and nums2 of length n, return the minimum product sum if you are allowed to rearrange the order of the elements in nums1.


Example 1:

Input: nums1 = [5,3,4,2], nums2 = [4,2,2,5]
Output: 40
Explanation: We can rearrange nums1 to become [3,5,4,2]. The product sum of [3,5,4,2] and [4,2,2,5] is 3*4 + 5*2 + 4*2 + 2*5 = 40.

Example 2:

Input: nums1 = [2,1,4,5,7], nums2 = [3,2,4,8,6]
Output: 65
Explanation: We can rearrange nums1 to become [5,7,4,1,2]. The product sum of [5,7,4,1,2] and [3,2,4,8,6] is 5*3 + 7*2 + 4*4 + 1*8 + 2*6 = 65.


Constraints:

n == nums1.length == nums2.length
1 <= n <= 10^5
1 <= nums1[i], nums2[i] <= 100

"""

# V0
# IDEA : GREEDY / REARRANGEMENT INEQUALITY (pair smallest with largest)
#
#   for any two pairs (a1, b1), (a2, b2) with a1 < a2 and b1 < b2,
#     a1*b2 + a2*b1  <=  a1*b1 + a2*b2
#   (their difference is (a2 - a1) * (b2 - b1) >= 0), so swapping to the
#   "opposite order" pairing never increases the total.
#
#   applying that exchange argument repeatedly gives : the minimum is
#   reached when nums1 ascending is matched with nums2 descending.
#
# time = O(n log n), space = O(n) for the sorted copies
class Solution(object):
    def minProductSum(self, nums1, nums2):
        a = sorted(nums1)
        b = sorted(nums2, reverse=True)

        res = 0
        for i in range(len(a)):
            res += a[i] * b[i]
        return res
