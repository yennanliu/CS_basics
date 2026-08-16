"""

3682. Minimum Index Sum of Common Elements
Medium

You are given two integer arrays nums1 and nums2 of equal length n.

We define a pair of indices (i, j) as a good pair if nums1[i] == nums2[j].

Return the minimum index sum i + j among all possible good pairs. If no
such pairs exist, return -1.


Example 1:

Input: nums1 = [3,2,1], nums2 = [1,3,1]
Output: 1
Explanation:
Common elements between nums1 and nums2 are 1 and 3.
For 3, [i, j] = [0, 1], giving an index sum of i + j = 1.
For 1, [i, j] = [2, 0], giving an index sum of i + j = 2.
The minimum index sum is 1.

Example 2:

Input: nums1 = [5,1,2], nums2 = [2,1,3]
Output: 2
Explanation:
Common elements between nums1 and nums2 are 1 and 2.
For 1, [i, j] = [1, 1], giving an index sum of i + j = 2.
For 2, [i, j] = [2, 0], giving an index sum of i + j = 2.
The minimum index sum is 2.

Example 3:

Input: nums1 = [6,4], nums2 = [7,8]
Output: -1
Explanation:
Since no common elements between nums1 and nums2, the output is -1.


Constraints:

1 <= nums1.length == nums2.length <= 10^5
-10^5 <= nums1[i], nums2[i] <= 10^5

"""

# V0
# IDEA : PER-VALUE EARLIEST INDEX, THEN ONE PASS
#
#   the sum i + j separates: once a value v is fixed, the best i is the
#   earliest occurrence of v in nums1 and the best j is the earliest in
#   nums2, chosen independently. so the whole search over O(n^2) pairs
#   collapses to a search over distinct values.
#
#   one dictionary holding the first index of each value in nums2 is enough:
#   scanning nums1 left to right, the first time we meet a value is also its
#   earliest index there, so `i + first2[v]` evaluated at every i already
#   covers every value's best pair (later duplicates only give larger sums
#   and are harmlessly considered too).
#
# time = O(n), space = O(n)
class Solution(object):
    def minimumSum(self, nums1, nums2):
        first2 = {}
        for j, v in enumerate(nums2):
            if v not in first2:
                first2[v] = j

        best = -1
        for i, v in enumerate(nums1):
            j = first2.get(v)
            if j is not None:
                s = i + j
                if best == -1 or s < best:
                    best = s
        return best
