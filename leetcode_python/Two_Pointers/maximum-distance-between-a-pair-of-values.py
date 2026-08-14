"""

1855. Maximum Distance Between a Pair of Values
Medium

You are given two non-increasing 0-indexed integer arrays nums1 and nums2.

A pair of indices (i, j), where 0 <= i < nums1.length and 0 <= j < nums2.length, is valid if both i <= j and nums1[i] <= nums2[j]. The distance of the pair is j - i.

Return the maximum distance of any valid pair (i, j). If there are no valid pairs, return 0.

An array arr is non-increasing if arr[i-1] >= arr[i] for every 1 <= i < arr.length.


Example 1:

Input: nums1 = [55,30,5,4,2], nums2 = [100,20,10,10,5]
Output: 2
Explanation: The valid pairs are (0,0), (2,2), (2,3), (2,4), (3,3), (3,4), and (4,4).
The maximum distance is 2 with pair (2,4).

Example 2:

Input: nums1 = [2,2,2], nums2 = [10,10,1]
Output: 1
Explanation: The valid pairs are (0,0), (0,1), and (1,1).
The maximum distance is 1 with pair (0,1).

Example 3:

Input: nums1 = [30,29,19,5], nums2 = [25,25,25,25,25]
Output: 2
Explanation: The valid pairs are (2,2), (2,3), (2,4), and (3,3), and (3,4).
The maximum distance is 2 with pair (2,4).


Constraints:

1 <= nums1.length, nums2.length <= 10^5
1 <= nums1[i], nums2[j] <= 10^5
Both nums1 and nums2 are non-increasing.

"""

# V0
# IDEA : TWO POINTERS (both arrays are non-increasing -> j never moves back)
#
#   for a fixed i we want the LARGEST j with nums1[i] <= nums2[j].
#   because nums1 is non-increasing, when i grows nums1[i] gets smaller,
#   so the frontier j can only move FORWARD -> monotone two pointers.
#
#   keep pushing j while nums1[i] <= nums2[j]; when the loop stops,
#   j - 1 is the last valid index, so the candidate answer is j - 1 - i.
#
#   NOTE : i <= j is required, but since j starts at i's own position
#          (j never lags behind i once we take max with 0), negative
#          candidates are simply discarded by the max().
#
# time = O(m + n), space = O(1)
class Solution(object):
    def maxDistance(self, nums1, nums2):
        m, n = len(nums1), len(nums2)
        res = 0
        i, j = 0, 0

        while i < m:
            if j < i:
                j = i
            while j < n and nums1[i] <= nums2[j]:
                j += 1
            # j is now the first INVALID index -> j - 1 is the last valid one
            if j - 1 - i > res:
                res = j - 1 - i
            i += 1

        return res
