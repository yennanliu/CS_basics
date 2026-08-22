"""

3131. Find the Integer Added to Array I
Easy

You are given two arrays of equal length, nums1 and nums2.

Each element in nums1 has been increased (or decreased in the case of negative) by an integer, represented by the variable x.

As a result, nums1 becomes equal to nums2. Two arrays are considered equal when they contain the same integers with the same frequencies.

Return the integer x.


Example 1:

Input: nums1 = [2,6,4], nums2 = [9,7,5]
Output: 3
Explanation:
The integer added to each element of nums1 is 3.

Example 2:

Input: nums1 = [10], nums2 = [5]
Output: -5
Explanation:
The integer added to each element of nums1 is -5.

Example 3:

Input: nums1 = [1,1,1,1], nums2 = [1,1,1,1]
Output: 0
Explanation:
The integer added to each element of nums1 is 0.


Constraints:

1 <= nums1.length == nums2.length <= 100
0 <= nums1[i], nums2[i] <= 1000
The test cases are generated in a way that there is an integer x such that nums1 can become equal to nums2 by increasing each element of nums1 by x.

"""

# V0
# IDEA : A CONSTANT SHIFT MOVES THE MINIMUM BY EXACTLY THAT CONSTANT
#
#   adding x to every element preserves the ordering, so the smallest element
#   of nums1 maps to the smallest of nums2 :
#       x = min(nums2) - min(nums1)
#
#   (the sums or the maxima would work just as well; the minimum is the
#   cheapest to read.)
#
# time = O(n), space = O(1)
class Solution(object):
    def addedInteger(self, nums1, nums2):
        return min(nums2) - min(nums1)


# V0-1
# IDEA : COMPARE THE SUMS — EVERY ELEMENT GAINED x, SO THE SUM GAINED n * x
#
#       sum(nums2) - sum(nums1) = n * x   =>   x = (sum2 - sum1) / n
#
#   an aggregate argument rather than an order-statistic one : it never looks
#   at any single element, so it is immune to how the two arrays are permuted
#   relative to each other. use exact integer division (the problem promises
#   a valid x, so the difference is divisible by n).
#
# time = O(n), space = O(1)
class Solution(object):
    def addedInteger(self, nums1, nums2):
        n = len(nums1)
        return (sum(nums2) - sum(nums1)) // n


# V0-2
# IDEA : BRUTE FORCE OVER EVERY CANDIDATE x, VALIDATED AS A MULTISET
#
#   values are bounded by 1000, so x can only lie in [-1000, 1000]. try each
#   candidate and check the definition directly : shifting nums1 by x must
#   produce the same multiset as nums2 (Counter equality, which is what
#   "same integers with the same frequencies" means).
#
#   slower, but it is the only version that VERIFIES rather than deduces —
#   handy as an oracle when random-testing the O(n) formulas above.
#
# time = O(V * n) with V = 2001 candidates, space = O(n)
from collections import Counter


class Solution(object):
    def addedInteger(self, nums1, nums2):
        target = Counter(nums2)
        for x in range(-1000, 1001):
            if Counter(v + x for v in nums1) == target:
                return x
        return 0
