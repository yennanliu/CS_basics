"""

1537. Get the Maximum Score
Hard

You are given two sorted arrays of distinct integers nums1 and nums2.

A valid path is defined as follows:

Choose array nums1 or nums2 to traverse (from index-0).
Traverse the current array from left to right.
If you are reading any value that is present in nums1 and nums2 you are allowed to change your path to the other array. (Only one repeated value is considered in the valid path).

The score is defined as the sum of unique values in a valid path.

Return the maximum score you can obtain of all possible valid paths. Since the answer may be too large, return it modulo 10^9 + 7.


Example 1:

Input: nums1 = [2,4,5,8,10], nums2 = [4,6,8,9]
Output: 30
Explanation: Valid paths:
[2,4,5,8,10], [2,4,5,8,9], [2,4,6,8,9], [2,4,6,8,10],  (starting from nums1)
[4,6,8,9], [4,5,8,10], [4,5,8,9], [4,6,8,10]    (starting from nums2)
The maximum is obtained with the path [2,4,6,8,10].

Example 2:

Input: nums1 = [1,3,5,7,9], nums2 = [3,5,100]
Output: 109
Explanation: Maximum sum is obtained with the path [1,3,5,100].

Example 3:

Input: nums1 = [1,2,3,4,5], nums2 = [6,7,8,9,10]
Output: 40
Explanation: There are no common elements between nums1 and nums2.
Maximum sum is obtained with the path [6,7,8,9,10].


Constraints:

1 <= nums1.length, nums2.length <= 10^5
1 <= nums1[i], nums2[i] <= 10^7
nums1 and nums2 are strictly increasing.

"""

# V0
# IDEA : MERGE THE TWO SORTED ARRAYS, SETTLE THE CHOICE AT EACH CROSSING
#
#   the common values cut both arrays into aligned SEGMENTS. between two
#   consecutive common values we are locked into one array, so we simply
#   take whichever of the two segment sums is larger — and the choices in
#   different segments are independent.
#
#   walk both arrays with two pointers, keeping running sums f (nums1 side)
#   and g (nums2 side). when the two pointers land on the SAME value we are
#   at a crossing : both sides may switch, so
#       f = g = max(f, g) + that shared value
#
#   the answer is max(f, g) at the end.
#   NOTE : apply the modulo only at the very end — taking it earlier would
#          corrupt the max(f, g) comparisons.
#
# time = O(m + n), space = O(1)
class Solution(object):
    def maxSum(self, nums1, nums2):
        MOD = 10 ** 9 + 7
        m, n = len(nums1), len(nums2)
        i = j = 0
        f = g = 0
        while i < m or j < n:
            if j == n or (i < m and nums1[i] < nums2[j]):
                f += nums1[i]
                i += 1
            elif i == m or nums2[j] < nums1[i]:
                g += nums2[j]
                j += 1
            else:
                f = g = max(f, g) + nums1[i]
                i += 1
                j += 1
        return max(f, g) % MOD
