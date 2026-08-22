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


# V0-1
# IDEA : LET EVERY EQUAL-VALUE PAIR VOTE FOR THE SHIFT THAT ALIGNS IT
#
#   index i of nums1 lands on index (i + s) % n after s right shifts, so a
#   pair (i, j) can only ever help the single shift s = (j - i) % n, and only
#   when nums1[i] == nums2[j].
#   so instead of scoring all n shifts, bucket nums2's indices by value and
#   cast one vote per matching pair; the fullest bucket is the answer.
#   with mostly distinct values this is near O(n); the quadratic blow-up only
#   appears when one value repeats a lot.
#
# time = O(n + P), P = number of equal-value pairs (O(n^2) worst case)
# space = O(n)
from collections import defaultdict


class Solution(object):
    def maximumMatchingIndices(self, nums1, nums2):
        n = len(nums1)
        pos = defaultdict(list)
        for j, v in enumerate(nums2):
            pos[v].append(j)
        votes = [0] * n
        for i, v in enumerate(nums1):
            for j in pos.get(v, ()):
                votes[(j - i) % n] += 1
        return max(votes)


# V0-2
# IDEA : SORT BOTH ARRAYS BY VALUE, THEN COUNT SHIFTS BLOCK BY BLOCK
#
#   same "each equal-value pair votes for one shift" observation as V0-1, but
#   the value -> indices grouping is built by SORTING instead of hashing:
#   sort (value, index) for both arrays, sweep them with two pointers, and for
#   every block sharing a value cross-multiply the two index lists.
#   no dictionary at all -- the version to use when hashing the values is
#   expensive or when a flat, cache-friendly layout matters.
#
# time = O(n log n + P), P = number of equal-value pairs
# space = O(n)
class Solution(object):
    def maximumMatchingIndices(self, nums1, nums2):
        n = len(nums1)
        a = sorted((v, i) for i, v in enumerate(nums1))
        b = sorted((v, j) for j, v in enumerate(nums2))
        votes = [0] * n
        p = q = 0
        while p < n and q < n:
            if a[p][0] < b[q][0]:
                p += 1
            elif a[p][0] > b[q][0]:
                q += 1
            else:
                v = a[p][0]
                p2 = p
                while p2 < n and a[p2][0] == v:
                    p2 += 1
                q2 = q
                while q2 < n and b[q2][0] == v:
                    q2 += 1
                for x in range(p, p2):
                    i = a[x][1]
                    for y in range(q, q2):
                        votes[(b[y][1] - i) % n] += 1
                p, q = p2, q2
        return max(votes)
