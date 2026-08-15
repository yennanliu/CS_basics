"""

3162. Find the Number of Good Pairs I
Easy

You are given 2 integer arrays nums1 and nums2 of lengths n and m respectively. You are also given a positive integer k.

A pair (i, j) is called good if nums1[i] is divisible by nums2[j] * k (0 <= i <= n - 1, 0 <= j <= m - 1).

Return the total number of good pairs.


Example 1:

Input: nums1 = [1,3,4], nums2 = [1,3,4], k = 1
Output: 5
Explanation:
The 5 good pairs are (0, 0), (1, 0), (1, 1), (2, 0), and (2, 2).

Example 2:

Input: nums1 = [1,2,4,12], nums2 = [2,4], k = 3
Output: 2
Explanation:
The 2 good pairs are (3, 0) and (3, 1).


Constraints:

1 <= n, m <= 50
1 <= nums1[i], nums2[j] <= 50
1 <= k <= 50

"""

# V0
# IDEA : 50 x 50 PAIRS — TEST THE DIVISIBILITY DIRECTLY
#
#   at most 2500 pairs, so the definition can be checked verbatim. the
#   sequel (LC 3164) scales both arrays to 10^5 and needs the divisor-counting
#   trick instead.
#
# time = O(n * m), space = O(1)
class Solution(object):
    def numberOfPairs(self, nums1, nums2, k):
        return sum(1
                   for a in nums1
                   for b in nums2
                   if a % (b * k) == 0)
