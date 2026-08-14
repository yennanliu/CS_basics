"""

1885. Count Pairs in Two Arrays
Medium

Given two integer arrays nums1 and nums2 of length n, count the pairs of indices (i, j) such that i < j and nums1[i] + nums1[j] > nums2[i] + nums2[j].

Return the number of pairs satisfying the condition.


Example 1:

Input: nums1 = [2,1,2,1], nums2 = [1,2,1,2]
Output: 1
Explanation: The pairs satisfying the condition are:
- (0, 2) where 2 + 2 > 1 + 1.

Example 2:

Input: nums1 = [1,10,6,2], nums2 = [1,4,1,5]
Output: 5
Explanation: The pairs satisfying the condition are:
- (0, 1) where 1 + 10 > 1 + 4.
- (0, 2) where 1 + 6 > 1 + 1.
- (1, 2) where 10 + 6 > 4 + 1.
- (1, 3) where 10 + 2 > 4 + 5.
- (2, 3) where 6 + 2 > 1 + 5.


Constraints:

n == nums1.length == nums2.length
1 <= n <= 10^5
1 <= nums1[i], nums2[i] <= 10^5

"""

# V0
# IDEA : REWRITE THE INEQUALITY, THEN SORT + TWO POINTERS
#
#   nums1[i] + nums1[j] > nums2[i] + nums2[j]
#     <=> (nums1[i] - nums2[i]) + (nums1[j] - nums2[j]) > 0
#     <=> d[i] + d[j] > 0        with d[k] = nums1[k] - nums2[k]
#
#   the index constraint i < j disappears : we only need UNORDERED pairs,
#   so d can be sorted freely.
#
#   classic two pointers on the sorted d :
#     if d[l] + d[r] > 0, then every index in (l .. r-1) also pairs with r
#     -> add (r - l) at once and shrink r.
#     otherwise d[l] is too small for ANY partner <= r -> advance l.
#
# time = O(n log n), space = O(n)
class Solution(object):
    def countPairs(self, nums1, nums2):
        d = [nums1[i] - nums2[i] for i in range(len(nums1))]
        d.sort()

        res = 0
        l, r = 0, len(d) - 1
        while l < r:
            if d[l] + d[r] > 0:
                res += r - l
                r -= 1
            else:
                l += 1

        return res
