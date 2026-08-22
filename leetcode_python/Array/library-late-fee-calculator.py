"""

3687. Library Late Fee Calculator
Easy

You are given an integer array daysLate where daysLate[i] indicates how
many days late the ith book was returned.

The penalty is calculated as follows:

If daysLate[i] == 1, penalty is 1.
If 2 <= daysLate[i] <= 5, penalty is 2 * daysLate[i].
If daysLate[i] > 5, penalty is 3 * daysLate[i].

Return the total penalty for all books.


Example 1:

Input: daysLate = [5,1,7]
Output: 32
Explanation:
daysLate[0] = 5: Penalty is 2 * daysLate[0] = 2 * 5 = 10.
daysLate[1] = 1: Penalty is 1.
daysLate[2] = 7: Penalty is 3 * daysLate[2] = 3 * 7 = 21.
Thus, the total penalty is 10 + 1 + 21 = 32.

Example 2:

Input: daysLate = [1,1]
Output: 2
Explanation:
daysLate[0] = 1: Penalty is 1.
daysLate[1] = 1: Penalty is 1.
Thus, the total penalty is 1 + 1 = 2.


Constraints:

1 <= daysLate.length <= 100
1 <= daysLate[i] <= 100

"""

# V0
# IDEA : PER-BOOK PIECEWISE FEE, SUMMED
#
#   the fee schedule is a piecewise function of a single book's lateness and
#   books do not interact, so the total is just the sum of f(x) over the
#   array. the only thing to get right is the tier boundaries: the tiers are
#   {1}, [2..5], [6..], and since the constraints guarantee x >= 1 there is
#   no zero/negative case to worry about -- testing x == 1 then x > 5 covers
#   the middle band by elimination.
#
# time = O(n), space = O(1)
class Solution(object):
    def lateFee(self, daysLate):
        total = 0
        for x in daysLate:
            if x == 1:
                total += 1
            elif x > 5:
                total += 3 * x
            else:
                total += 2 * x
        return total


# V0-1
# IDEA : PRECOMPUTED FEE TABLE + FREQUENCY COUNTING
#
#   daysLate[i] <= 100, so the fee schedule can be tabulated ONCE for every
#   possible lateness and then only looked up. Counter collapses repeated
#   values, so a book count of n with only d distinct latenesses costs d
#   multiplications instead of n branches.
#
#   the branch is evaluated 100 times regardless of n rather than n times,
#   which is what makes this a table lookup rather than a re-spelling of V0.
#
# time = O(n + M), M = 101 possible day counts
# space = O(M)
from collections import Counter


class Solution(object):
    def lateFee(self, daysLate):
        M = 101
        fee = [0] * M
        for d in range(1, M):
            fee[d] = 1 if d == 1 else (2 * d if d <= 5 else 3 * d)
        return sum(fee[d] * c for d, c in Counter(daysLate).items())


# V0-2
# IDEA : SORT + BINARY SEARCH THE TIER BOUNDARIES + PREFIX SUMS
#
#   the fee is `multiplier(tier) * x` on each tier (with tier 1 being the
#   degenerate 1 * 1), so within a tier the total is just the multiplier times
#   the SUM of that tier's values. sorting puts each tier in a contiguous
#   block, bisect finds the two cut points, and a prefix-sum array gives each
#   block's sum in O(1):
#
#       [ ... 1 ... | ... 2..5 ... | ... >5 ... ]
#                   i              j
#
#       total = 1*count(1) + 2*sum(arr[i:j]) + 3*sum(arr[j:])
#
#   no per-element branching at all - the tiers are located, not tested.
#
# time = O(n log n)
# space = O(n)
import bisect


class Solution(object):
    def lateFee(self, daysLate):
        arr = sorted(daysLate)
        pre = [0]
        for x in arr:
            pre.append(pre[-1] + x)
        i = bisect.bisect_left(arr, 2)    # arr[:i] are the 1-day-late books
        j = bisect.bisect_right(arr, 5)   # arr[i:j] fall in the 2..5 band
        return i + 2 * (pre[j] - pre[i]) + 3 * (pre[-1] - pre[j])
