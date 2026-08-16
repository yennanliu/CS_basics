"""

3395. Subsequences with a Unique Middle Mode I
Hard

Given an integer array nums, find the number of subsequences of size 5 of nums
with a unique middle mode.

Since the answer may be very large, return it modulo 10^9 + 7.

A mode of a sequence of numbers is defined as the element that appears the
maximum number of times in the sequence.

A sequence of numbers contains a unique mode if it has only one mode.

A sequence of numbers seq of size 5 contains a unique middle mode if the middle
element (seq[2]) is a unique mode.

Example 1:

Input: nums = [1,1,1,1,1,1]

Output: 6

Explanation:

[1, 1, 1, 1, 1] is the only subsequence of size 5 that can be formed, and it has
a unique middle mode of 1. This subsequence can be formed in 6 different ways,
so the output is 6.

Example 2:

Input: nums = [1,2,2,3,3,4]

Output: 4

Explanation:

[1, 2, 2, 3, 4] and [1, 2, 3, 3, 4] each have a unique middle mode because the
number at index 2 has the greatest frequency in the subsequence. [1, 2, 2, 3, 3]
does not have a unique middle mode because 2 and 3 appear twice.

Example 3:

Input: nums = [0,1,2,3,4,5,6,7,8]

Output: 0

Explanation:

There is no subsequence of length 5 with a unique middle mode.

Constraints:

5 <= nums.length <= 1000
-10^9 <= nums[i] <= 10^9

"""

# V0
# IDEA : FIX THE MIDDLE, COUNT BY HOW MANY COPIES OF IT WE TAKE
#
#   let x = seq[2] and c = how often x occurs in the 5 chosen elements.  x is
#   the unique mode iff every other value occurs strictly fewer than c times,
#   and since the other 5 - c slots hold at most 5 - c copies of any one value:
#
#     c = 1  ->  impossible (another value would tie or beat it);
#     c = 2  ->  the 3 non-x picks must be pairwise distinct;
#     c >= 3 ->  automatically fine, at most 2 slots are left for anyone else.
#
#   so fix the index i of the middle element.  we choose 2 indices on its left
#   and 2 on its right; let a / b be how many of those are copies of x, so
#   c = 1 + a + b.  the a + b >= 2 bucket is counted by subtraction:
#     ge2 = C(L,2)*C(R,2) - (a+b == 0) - (a+b == 1).
#
#   only the a + b == 1 bucket needs the distinctness check.  three elements can
#   fail it in only one way — some single value y shows up twice or thrice — and
#   two different values cannot both do that inside three slots, so the failures
#   for different y are disjoint and a plain sum over y (no inclusion-exclusion)
#   is exact.  for the branch that takes the x from the left, the count of
#   arrangements where y appears at least twice simplifies to
#     ly_y*ry_y*(ry - ry_y) + ly*C(ry_y, 2),
#   and the branch that takes the x from the right is its mirror image.
#
#   n <= 1000 here, so we can afford to re-sum over every distinct value at each
#   middle index.
#
# time = O(n * D) with D distinct values, space = O(D)
class Solution(object):
    def subsequencesWithMiddleMode(self, nums):
        MOD = 10 ** 9 + 7
        n = len(nums)
        vals = sorted(set(nums))
        vid = {v: i for i, v in enumerate(vals)}
        ids = [vid[v] for v in nums]
        V = len(vals)

        c2 = lambda t: t * (t - 1) // 2

        cntL = [0] * V
        cntR = [0] * V
        for t in ids:
            cntR[t] += 1
        cntR[ids[0]] -= 1

        ans = 0
        for i in range(n):
            x = ids[i]
            L, R = i, n - 1 - i
            lx, rx = cntL[x], cntR[x]
            ly, ry = L - lx, R - rx

            total = c2(L) * c2(R)
            zero = c2(ly) * c2(ry)
            one = lx * ly * c2(ry) + c2(ly) * rx * ry
            ge2 = total - zero - one

            bad = 0
            if one:
                for y in range(V):
                    if y == x:
                        continue
                    lyy = cntL[y]
                    ryy = cntR[y]
                    if lyy == 0 and ryy == 0:
                        continue
                    bad += lx * (lyy * ryy * (ry - ryy) + ly * c2(ryy))
                    bad += rx * (ryy * lyy * (ly - lyy) + ry * c2(lyy))

            ans = (ans + ge2 + one - bad) % MOD

            if i + 1 < n:
                cntL[x] += 1
                cntR[ids[i + 1]] -= 1
        return ans % MOD
