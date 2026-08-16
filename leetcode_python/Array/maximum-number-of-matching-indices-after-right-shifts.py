"""

3400. Maximum Number of Matching Indices After Right Shifts
Medium

You are given two integer arrays, nums1 and nums2, of the same length.

An index i is considered matching if nums1[i] == nums2[i].

Return the maximum number of matching indices after performing any number of
right shifts on nums1.

A right shift is defined as shifting the element at index i to index (i + 1) %
n, for all indices.

Example 1:

Input: nums1 = [3,1,2,3,1,2], nums2 = [1,2,3,1,2,3]

Output: 6

Explanation:

If we right shift nums1 2 times, it becomes [1, 2, 3, 1, 2, 3]. Every index
matches, so the output is 6.

Example 2:

Input: nums1 = [1,4,2,5,3,1], nums2 = [2,3,1,2,4,6]

Output: 3

Explanation:

If we right shift nums1 3 times, it becomes [5, 3, 1, 1, 4, 2]. Indices 1, 2,
and 4 match, so the output is 3.

Constraints:

nums1.length == nums2.length
1 <= nums1.length, nums2.length <= 3000
1 <= nums1[i], nums2[i] <= 10^9

"""

# V0
# IDEA : TRY EVERY ROTATION, COMPARE WITH A DOUBLED ARRAY
#
#   there are only n distinct right shifts, so the answer is just the best of n
#   candidates — no clever structure is needed, only a cheap way to build each
#   rotation.  concatenating nums1 with itself makes every rotation a
#   contiguous slice: shifting right by s puts the element that was at index
#   n - s at the front, so the slice is doubled[n - s : 2n - s].
#
#   counting the matches with map(eq, ...) keeps the inner comparison loop in C
#   rather than in the interpreter, which is what makes the O(n^2) pass fast
#   enough at n = 3000.
#
# time = O(n^2), space = O(n)
import operator


class Solution(object):
    def maximumMatchingIndices(self, nums1, nums2):
        n = len(nums1)
        doubled = nums1 + nums1
        best = 0
        for s in range(n):
            c = sum(map(operator.eq, doubled[n - s: 2 * n - s], nums2))
            if c > best:
                best = c
        return best
