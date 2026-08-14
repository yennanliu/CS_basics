"""

2198. Number of Single Divisor Triplets
Medium
(premium / locked problem)

You are given a 0-indexed array nums consisting of positive integers. A triplet of indices (i, j, k) is a single divisor triplet of nums if nums[i] + nums[j] + nums[k] is divisible by exactly one of nums[i], nums[j], or nums[k].

Return the number of single divisor triplets of nums.


Example 1:

Input: nums = [4,6,7,3,2]
Output: 12
Explanation:
The triplet (0, 3, 4) is a single divisor triplet because nums[0] + nums[3] + nums[4] = 4 + 3 + 2 = 9 and 9 is divisible by only nums[3].
The triplet (0, 4, 3) is a single divisor triplet because nums[0] + nums[4] + nums[3] = 4 + 2 + 3 = 9 and 9 is divisible by only nums[4].
The triplet (3, 0, 4), (3, 4, 0), (4, 0, 3), and (4, 3, 0) are the same as the previous two triplets.
The triplet (0, 2, 3) is a single divisor triplet because nums[0] + nums[2] + nums[3] = 4 + 7 + 3 = 14 and 14 is divisible by only nums[2].
The triplet (0, 3, 2), (2, 0, 3), (2, 3, 0), (3, 0, 2), and (3, 2, 0) are the same as the previous triplet.
There are 12 single divisor triplets in total.

Example 2:

Input: nums = [1,2,2]
Output: 6
Explanation:
The triplet (0, 1, 2) is a single divisor triplet because nums[0] + nums[1] + nums[2] = 1 + 2 + 2 = 5 and 5 is divisible by only nums[0].
The triplet (0, 2, 1), (1, 0, 2), (1, 2, 0), (2, 0, 1), and (2, 1, 0) are the same as the previous triplet.
There are 6 single divisor triplets in total.

Example 3:

Input: nums = [1,1,1]
Output: 0
Explanation:
There are no single divisor triplets.
Note that (0, 1, 2) is not a single divisor triplet because nums[0] + nums[1] + nums[2] = 3 and 3 is divisible by nums[0], nums[1], and nums[2].


Constraints:

3 <= nums.length <= 10^5
1 <= nums[i] <= 100

"""

# V0
# IDEA : VALUES ARE <= 100 — ENUMERATE VALUE TRIPLES, NOT INDEX TRIPLES
#
#   n can be 10^5 so the O(n^3) index scan is hopeless, but there are only
#   100 possible VALUES. iterate a <= b <= c over the values present and test
#   the condition once per combination :
#       d = (s % a == 0) + (s % b == 0) + (s % c == 0),  s = a + b + c
#   keeping the combination when d == 1.
#
#   then multiply by how many ORDERED index triples realise that multiset
#   (the problem counts (i,j,k) permutations separately) :
#       all three distinct : 6 * cnt[a] * cnt[b] * cnt[c]
#       exactly two equal  : 3 * cnt[a] * (cnt[a]-1) * cnt[c]
#       all three equal    : cnt[a] * (cnt[a]-1) * (cnt[a]-2)
#
# time = O(n + 100^3), space = O(100)
from collections import Counter


class Solution(object):
    def singleDivisorTriplet(self, nums):
        cnt = Counter(nums)
        vals = sorted(cnt)

        res = 0
        for ia, a in enumerate(vals):
            for ib in range(ia, len(vals)):
                b = vals[ib]
                for ic in range(ib, len(vals)):
                    c = vals[ic]
                    s = a + b + c
                    d = (s % a == 0) + (s % b == 0) + (s % c == 0)
                    if d != 1:
                        continue
                    if a == b == c:
                        res += cnt[a] * (cnt[a] - 1) * (cnt[a] - 2)
                    elif a == b:
                        res += 3 * cnt[a] * (cnt[a] - 1) * cnt[c]
                    elif b == c:
                        res += 3 * cnt[b] * (cnt[b] - 1) * cnt[a]
                    else:
                        res += 6 * cnt[a] * cnt[b] * cnt[c]
        return res
