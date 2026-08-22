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


# V0-1
# IDEA : COUNT MULTIPLES — FREQUENCY TABLE OVER THE VALUE RANGE (HARMONIC SUM)
#
#   group nums1 by value once. then for each b in nums2 the divisor is
#   d = b * k, and every good partner is a MULTIPLE of d, so walk
#   d, 2d, 3d, ... up to max(nums1) and add the frequencies.
#
#   the walk costs V / d steps, and summing V / d over the distinct divisors
#   is the harmonic sum ~ V log V — independent of how long nums1 is. this is
#   the version that scales to the sequel (LC 3164, arrays of 10^5).
#
# time = O(n + m + V log V) with V = max(nums1), space = O(V)
from collections import Counter


class Solution(object):
    def numberOfPairs(self, nums1, nums2, k):
        cnt1 = Counter(nums1)
        top = max(nums1)
        res = 0
        for b in nums2:
            d = b * k
            if d > top:
                continue
            for mult in range(d, top + 1, d):
                res += cnt1[mult]
        return res


# V0-2
# IDEA : ENUMERATE THE DIVISORS OF EACH nums1[i] IN O(sqrt(a))
#
#   the mirror image of V0-1 : group nums2 instead, then for each a in nums1
#   list its divisors by trial division up to sqrt(a). a divisor d is usable
#   only when d is itself a multiple of k, and it then corresponds to the
#   nums2 value b = d // k — so add cnt2[d // k].
#
#   watch the perfect-square case (d == a // d) so the divisor is not counted
#   twice.
#
#   preferable to V0-1 when the values are large but the arrays are short,
#   since it never touches the value range as a whole.
#
# time = O(m + n * sqrt(V)), space = O(m)
from collections import Counter


class Solution(object):
    def numberOfPairs(self, nums1, nums2, k):
        cnt2 = Counter(nums2)
        res = 0
        for a in nums1:
            d = 1
            while d * d <= a:
                if a % d == 0:
                    for div in {d, a // d}:
                        if div % k == 0:
                            res += cnt2[div // k]
                d += 1
        return res
