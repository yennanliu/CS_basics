"""

2605. Form Smallest Number From Two Digit Arrays
Easy

Given two arrays of unique digits nums1 and nums2, return the smallest number that contains at least one digit from each array.


Example 1:

Input: nums1 = [4,1,3], nums2 = [5,7]
Output: 15
Explanation: The number 15 contains the digit 1 from nums1 and the digit 5 from nums2. It can be proven that 15 is the smallest number we can have.

Example 2:

Input: nums1 = [3,5,2,6], nums2 = [3,1,7]
Output: 3
Explanation: The number 3 contains the digit 3 which exists in both arrays.


Constraints:

1 <= nums1.length, nums2.length <= 9
1 <= nums1[i], nums2[i] <= 9
All digits in each array are unique.

"""

# V0
# IDEA : HASH SET INTERSECTION + 2-DIGIT FALLBACK
#
#   two cases only:
#
#   1) some digit d appears in BOTH arrays -> the single digit d already
#      satisfies "at least one digit from each array". a 1-digit number always
#      beats any 2-digit one, so answer = min(nums1 & nums2).
#
#   2) no shared digit -> the answer needs at least 2 digits, one from each
#      array, and 2 digits are enough. to minimise, take the smallest digit
#      a of nums1 and the smallest digit b of nums2 and try both orders:
#      min(10a + b, 10b + a).
#
#   NOTE : greedily picking the smallest from each array is safe because in a
#          2-digit number both positions are strictly better off with a
#          smaller digit (the tens place dominates, and it gets whichever of
#          a, b is smaller).
#   NOTE : digits are 1..9, so no leading-zero worries.
#
# time = O(m + n), space = O(m + n)
class Solution(object):
    def minNumber(self, nums1, nums2):
        both = set(nums1) & set(nums2)
        if both:
            return min(both)
        a, b = min(nums1), min(nums2)
        return min(10 * a + b, 10 * b + a)
