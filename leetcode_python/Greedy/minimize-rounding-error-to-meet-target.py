"""

1058. Minimize Rounding Error to Meet Target
Medium

Given an array of prices [p1, p2, ..., pn] and a target, round each price pi to
Round_i(pi) so that the rounded array [Round_1(p1), Round_2(p2), ..., Round_n(pn)]
sums to the given target. Each operation Round_i(pi) could be either Floor(pi) or Ceil(pi).

Return the string "-1" if the rounded array is impossible to sum to target.
Otherwise, return the smallest rounding error, which is defined as
Sigma |Round_i(pi) - pi| for i from 1 to n, as a string with three places
after the decimal.


Example 1:

Input: prices = ["0.700","2.800","4.900"], target = 8
Output: "1.000"
Explanation:
Use Floor, Ceil and Ceil operations to get (0.7 - 0) + (3 - 2.8) + (5 - 4.9) = 0.7 + 0.2 + 0.1 = 1.0 .

Example 2:

Input: prices = ["1.500","2.500","3.500"], target = 10
Output: "-1"
Explanation: It is impossible to meet the target.

Example 3:

Input: prices = ["1.500","2.500","3.500"], target = 9
Output: "1.500"


Constraints:

1 <= prices.length <= 500
Each string prices[i] represents a real number in the range [0.0, 1000.0] and has exactly 3 decimal places.
0 <= target <= 10^6

"""

# V0
# IDEA : GREEDY + exact integer (thousandths) arithmetic
#
#  - floor sum is the minimum reachable total,
#    floor sum + (# of prices with a non zero fraction) is the maximum.
#  - we must "ceil" exactly d = target - floorSum of the fractional prices.
#  - ceiling a price with fraction f costs (1 - f), flooring it costs f,
#    so we should ceil the d LARGEST fractions.
#
# NOTE : fractions are kept as integers in 1/1000 units to dodge float error
# time = O(n log n)
# space = O(n)
class Solution(object):
    def minimizeError(self, prices, target):
        base = 0        # sum of floor(price)
        fracs = []      # non zero fractional parts, in thousandths
        for p in prices:
            whole, dot, frac = p.partition(".")
            whole = int(whole)
            frac = int(frac) if dot else 0
            base += whole
            if frac:
                fracs.append(frac)

        # target must sit between "floor everything" and "ceil every fractional price"
        if not (base <= target <= base + len(fracs)):
            return "-1"

        d = target - base
        fracs.sort(reverse=True)

        # ceil the d largest fractions -> cost (1000 - f) each
        # floor the rest -> cost f each
        total = d * 1000 - sum(fracs[:d]) + sum(fracs[d:])
        return "{:.3f}".format(total / 1000.0)

# V1
# IDEA : same greedy, but done with float fractions
# time = O(n log n)
# space = O(n)
class Solution(object):
    def minimizeError(self, prices, target):
        base = 0
        fracs = []
        for p in prices:
            v = float(p)
            base += int(v)
            f = v - int(v)
            if f > 0:
                fracs.append(f)

        if target < base or target > base + len(fracs):
            return "-1"

        d = target - base
        fracs.sort(reverse=True)
        ans = d - sum(fracs[:d]) + sum(fracs[d:])
        return "{:.3f}".format(ans)
