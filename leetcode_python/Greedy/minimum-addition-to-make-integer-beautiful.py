"""

2457. Minimum Addition to Make Integer Beautiful
Medium

You are given two positive integers n and target.

An integer is considered beautiful if the sum of its digits is less than or equal to target.

Return the minimum non-negative integer x such that n + x is beautiful. The input will be generated such that it is always possible to make n beautiful.


Example 1:

Input: n = 16, target = 6
Output: 4
Explanation: Initially n is 16 and its digit sum is 1 + 6 = 7. After adding 4, n becomes 20 and digit sum becomes 2 + 0 = 2. It can be shown that we can not make n beautiful with adding non-negative integer less than 4.

Example 2:

Input: n = 467, target = 6
Output: 33
Explanation: Initially n is 467 and its digit sum is 4 + 6 + 7 = 17. After adding 33, n becomes 500 and digit sum becomes 5 + 0 + 0 = 5. It can be shown that we can not make n beautiful with adding non-negative integer less than 33.

Example 3:

Input: n = 1, target = 1
Output: 0
Explanation: Initially n is 1 and its digit sum is 1, which is already smaller than or equal to target.


Constraints:

1 <= n <= 10^12
1 <= target <= 20

"""

# V0
# IDEA : CLEAR THE TRAILING DIGITS ONE PLACE AT A TIME
#
#   the cheapest way to shrink a digit sum is to carry : rounding n UP to the
#   next multiple of 10^k zeroes out its last k digits at the cost of a
#   single carry into the higher part.
#
#   so try k = 1, 2, 3, ... — each step rounds up to the next multiple of the
#   current power of ten — and stop at the first value whose digit sum fits
#   under target. every smaller addition leaves some of those low digits
#   non-zero and therefore a larger digit sum, so the first hit is minimal.
#
#   n reaches 10^12, so this loop runs at most ~13 times.
#
# time = O(digits^2), space = O(1)
class Solution(object):
    def makeIntegerBeautiful(self, n, target):
        def digit_sum(x):
            return sum(int(c) for c in str(x))

        value = n
        power = 10
        while digit_sum(value) > target:
            value = (value // power + 1) * power
            power *= 10
        return value - n
