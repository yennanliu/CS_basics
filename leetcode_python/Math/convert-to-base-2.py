"""

1017. Convert to Base -2
Medium

Given an integer n, return a binary string representing its representation in base -2.

Note that the returned string should not have leading zeros unless the string is "0".


Example 1:

Input: n = 2
Output: "110"
Explantion: (-2)^2 + (-2)^1 = 2

Example 2:

Input: n = 3
Output: "111"
Explantion: (-2)^2 + (-2)^1 + (-2)^0 = 3

Example 3:

Input: n = 4
Output: "100"
Explantion: (-2)^2 = 4


Constraints:

0 <= n <= 10^9

"""

# V0
# IDEA : REPEATED DIVISION BY -2
#
#   same idea as base 2, but the quotient flips sign each step:
#     digit = n mod 2         (always 0 or 1)
#     n     = -(n - digit) / 2  ==  -((n - digit) >> 1)
#
#   the remainder must stay in {0, 1}; taking `n & 1` gives exactly that
#   for negative n too (python's `&` / `>>` behave as arbitrary-precision
#   two's complement), so `n = -(n >> 1)` is the whole recurrence.
#
#   trace for n = 2 :
#     n=2  -> digit 0, n = -(2>>1)  = -1
#     n=-1 -> digit 1, n = -(-1>>1) = 1
#     n=1  -> digit 1, n = -(1>>1)  = 0
#     digits collected low->high = 0,1,1 -> reversed = "110"
#
# time = O(log n)
# space = O(log n)
class Solution(object):
    def baseNeg2(self, n):
        if n == 0:
            return "0"
        digits = []
        while n != 0:
            digits.append(str(n & 1))
            n = -(n >> 1)
        return ''.join(reversed(digits))
