"""

3416. Subsequences with a Unique Middle Mode II
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

[1, 1, 1, 1, 1] is the only subsequence of size 5 that can be formed from this
list, and it has a unique middle mode of 1.

Example 2:

Input: nums = [1,2,2,3,3,4]

Output: 4

Explanation:

[1, 2, 2, 3, 4] and [1, 2, 3, 3, 4] have unique middle modes because the number
at index 2 has the greatest frequency in the subsequence. [1, 2, 2, 3, 3] does
not have a unique middle mode because 2 and 3 both appear twice in the
subsequence.

Example 3:

Input: nums = [0,1,2,3,4,5,6,7,8]

Output: 0

Explanation:

There does not exist a subsequence of length 5 with a unique middle mode.

Constraints:

5 <= nums.length <= 10^5
-10^9 <= nums[i] <= 10^9

"""

# V0
# IDEA : SAME COUNTING AS PART I, BUT THE SUM OVER y IS KEPT INCREMENTALLY
#
#   the combinatorics is identical to the easy version: fix the middle index i
#   with value x, split the 5 picks into 2 left + 1 middle + 2 right, and let
#   a + b be how many of the 4 outer picks are copies of x, so c = 1 + a + b.
#     a + b >= 2 always works, a + b == 0 never works, and a + b == 1 works iff
#     the three non-x picks carry three different values.
#
#   part I re-scanned every distinct value at every middle index, which is
#   quadratic and dies at n = 1e5.  the fix is that the correction term for the
#   a + b == 1 bucket is a *sum of separable functions* of (ly_y, ry_y):
#
#     P = sum ly_y*ry_y,  Q = sum ly_y*ry_y^2,  W = sum ly_y^2*ry_y,
#     T = sum C(ry_y,2),  U = sum C(ly_y,2)
#
#   with those five running totals the whole correction is
#     lx * (ry*P - Q + ly*T)  +  rx * (ly*P - W + ry*U)
#   minus the y == x share, which is written out explicitly and subtracted
#   (the sums run over all y, the formula must not).
#
#   moving the middle one step right changes exactly two buckets — nums[i] joins
#   the left side, nums[i+1] leaves the right side — so each of the five totals
#   is repaired in O(1) by subtracting that value's old contribution and adding
#   its new one.
#
# time = O(n), space = O(n)
class Solution(object):
    def subsequencesWithMiddleMode(self, nums):
        MOD = 10 ** 9 + 7
        n = len(nums)
        vals = sorted(set(nums))
        vid = {v: i for i, v in enumerate(vals)}
        ids = [vid[v] for v in nums]
        V = len(vals)

        cntL = [0] * V
        cntR = [0] * V
        for t in ids:
            cntR[t] += 1

        # five running totals over every distinct value: [P, Q, W, T, U]
        agg = [0, 0, 0, 0, 0]
        for v in range(V):
            r = cntR[v]
            agg[3] += r * (r - 1) // 2

        def shift(v, dl, dr):
            # remove value v's contribution, move its counts, put it back
            l, r = cntL[v], cntR[v]
            agg[0] -= l * r
            agg[1] -= l * r * r
            agg[2] -= l * l * r
            agg[3] -= r * (r - 1) // 2
            agg[4] -= l * (l - 1) // 2
            l += dl
            r += dr
            cntL[v] = l
            cntR[v] = r
            agg[0] += l * r
            agg[1] += l * r * r
            agg[2] += l * l * r
            agg[3] += r * (r - 1) // 2
            agg[4] += l * (l - 1) // 2

        shift(ids[0], 0, -1)

        ans = 0
        for i in range(n):
            P, Q, W, T, U = agg
            x = ids[i]
            L, R = i, n - 1 - i
            lx, rx = cntL[x], cntR[x]
            ly, ry = L - lx, R - rx

            total = L * (L - 1) // 2 * (R * (R - 1) // 2)
            zero = ly * (ly - 1) // 2 * (ry * (ry - 1) // 2)
            one = lx * ly * (ry * (ry - 1) // 2) + (ly * (ly - 1) // 2) * rx * ry
            ge2 = total - zero - one

            bad = 0
            if one:
                s1 = ry * P - Q + ly * T
                s1 -= lx * rx * (ry - rx) + ly * (rx * (rx - 1) // 2)
                s2 = ly * P - W + ry * U
                s2 -= rx * lx * (ly - lx) + ry * (lx * (lx - 1) // 2)
                bad = lx * s1 + rx * s2

            ans = (ans + ge2 + one - bad) % MOD

            if i + 1 < n:
                shift(x, 1, 0)
                shift(ids[i + 1], 0, -1)
        return ans % MOD
