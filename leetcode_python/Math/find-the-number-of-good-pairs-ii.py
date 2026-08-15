"""

3164. Find the Number of Good Pairs II
Medium

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

1 <= n, m <= 10^5
1 <= nums1[i], nums2[j] <= 10^6
1 <= k <= 10^3

"""

# V0
# IDEA : DIVIDE OUT k, THEN SWEEP THE *MULTIPLES* OF EACH DISTINCT nums2 VALUE
#
#   nums1[i] % (nums2[j] * k) == 0 requires k | nums1[i] to begin with, so
#   drop the values that fail and replace the survivors by a = nums1[i] / k.
#   the condition collapses to plain divisibility : nums2[j] divides a.
#
#   walking each a's divisors would cost sqrt(10^6) = 1000 steps apiece,
#   i.e. 10^8 for the whole array. going the other way is far cheaper :
#   tally the a values, then for each DISTINCT b in nums2 add up the tallies
#   at b, 2b, 3b, ... — a harmonic sum, roughly max * ln(distinct) steps.
#
# time = O(n + max value * H(distinct)), space = O(max value)
from collections import Counter


class Solution(object):
    def numberOfPairs(self, nums1, nums2, k):
        cnt1 = Counter(a // k for a in nums1 if a % k == 0)
        if not cnt1:
            return 0
        top = max(cnt1)

        res = 0
        for b, freq in Counter(nums2).items():
            if b > top:
                continue
            hits = 0
            for m in range(b, top + 1, b):
                hits += cnt1.get(m, 0)
            res += hits * freq
        return res
