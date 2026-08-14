"""

2540. Minimum Common Value
Easy

Given two integer arrays nums1 and nums2, sorted in non-decreasing order, return the minimum integer common to both arrays. If there is no common integer amongst nums1 and nums2, return -1.

Note that an integer is said to be common to nums1 and nums2 if both arrays have at least one occurrence of that integer.


Example 1:

Input: nums1 = [1,2,3], nums2 = [2,4]
Output: 2
Explanation: The smallest element common to both arrays is 2, so we return 2.

Example 2:

Input: nums1 = [1,2,3,6], nums2 = [2,3,4,5]
Output: 2
Explanation: There are two common elements in the array 2 and 3 out of which 2 is the smallest, so 2 is returned.


Constraints:

1 <= nums1.length, nums2.length <= 10^5
1 <= nums1[i], nums2[j] <= 10^9
Both nums1 and nums2 are sorted in non-decreasing order.

"""

# V0
# IDEA : TWO POINTERS (merge-style walk over two sorted arrays)
#
#   walk both arrays at once. whenever the two values match we have found a
#   common value, and because both arrays are sorted ascending the FIRST match
#   we see is automatically the smallest one -> return it immediately.
#
#   otherwise advance the pointer sitting on the smaller value: that value can
#   never be matched later (the other array only grows from here on).
#
#   NOTE : duplicates need no special handling — advancing on the smaller side
#          simply skips over repeated values without breaking the invariant.
#
# time = O(m + n), space = O(1)
class Solution(object):
    def getCommon(self, nums1, nums2):
        i, j = 0, 0
        m, n = len(nums1), len(nums2)
        while i < m and j < n:
            if nums1[i] == nums2[j]:
                return nums1[i]
            if nums1[i] < nums2[j]:
                i += 1
            else:
                j += 1
        return -1
