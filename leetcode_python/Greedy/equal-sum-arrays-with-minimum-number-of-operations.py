"""

1775. Equal Sum Arrays With Minimum Number of Operations
Medium

You are given two arrays of integers nums1 and nums2, possibly of different lengths. The values in the arrays are between 1 and 6, inclusive.

In one operation, you can change any integer's value in any of the arrays to any value between 1 and 6, inclusive.

Return the minimum number of operations required to make the sum of values in nums1 equal to the sum of values in nums2. Return -1 if it is not possible to make the sum of the two arrays equal.

Example 1:

Input: nums1 = [1,2,3,4,5,6], nums2 = [1,1,2,2,2,2]
Output: 3
Explanation: You can make the sums of nums1 and nums2 equal with 3 operations. All indices are 0-indexed.
- Change nums2[0] to 6. nums1 = [1,2,3,4,5,6], nums2 = [6,1,2,2,2,2].
- Change nums1[5] to 1. nums1 = [1,2,3,4,5,1], nums2 = [6,1,2,2,2,2].
- Change nums1[2] to 2. nums1 = [1,2,2,4,5,1], nums2 = [6,1,2,2,2,2].

Example 2:

Input: nums1 = [1,1,1,1,1,1,1], nums2 = [6]
Output: -1
Explanation: There is no way to decrease the sum of nums1 or to increase the sum of nums2 to make them equal.

Example 3:

Input: nums1 = [6,6], nums2 = [1]
Output: 3
Explanation: You can make the sums of nums1 and nums2 equal with 3 operations. All indices are 0-indexed.
- Change nums1[0] to 2. nums1 = [2,6], nums2 = [1].
- Change nums1[1] to 2. nums1 = [2,2], nums2 = [1].
- Change nums2[0] to 4. nums1 = [2,2], nums2 = [4].

Constraints:

1 <= nums1.length, nums2.length <= 10^5
1 <= nums1[i], nums2[i] <= 6

"""

# V0
# IDEA : GREEDY ON THE "GAIN" OF EACH SINGLE CHANGE
#
#   assume sum(nums1) < sum(nums2) (otherwise swap). the gap d must be closed.
#   raising a value v in nums1 gains at most 6 - v, lowering a value v in
#   nums2 gains at most v - 1. every operation is independent, so to use the
#   fewest operations we always spend the biggest available gain first.
#   NOTE : if the total of all gains is still < d the answer is -1 (e.g. seven
#          1s versus a single 6).
#
# time = O(n + m) with counting sort over gains 0..5, space = O(1)
class Solution(object):
    def minOperations(self, nums1, nums2):
        s1, s2 = sum(nums1), sum(nums2)
        if s1 > s2:
            nums1, nums2 = nums2, nums1
            s1, s2 = s2, s1
        d = s2 - s1
        if d == 0:
            return 0

        # gains bucketed by size (1..5) : counting sort, values are tiny
        gains = [0] * 6
        for v in nums1:
            gains[6 - v] += 1
        for v in nums2:
            gains[v - 1] += 1

        res = 0
        for g in range(5, 0, -1):
            if gains[g] == 0:
                continue
            need = (d + g - 1) // g          # how many ops of gain g we need
            take = min(need, gains[g])
            res += take
            d -= take * g
            if d <= 0:
                return res
        return -1
