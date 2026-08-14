"""

972. Equal Rational Numbers
Hard

Given two strings s and t, each of which represents a non-negative rational number, return true if and only if they represent the same number. The strings may use parentheses to denote the repeating part of the rational number.

A rational number can be represented using up to three parts: <IntegerPart>, <NonRepeatingPart>, and a <RepeatingPart>. The number will be represented in one of the following three ways:

<IntegerPart>
    For example, 12, 0, and 123.
<IntegerPart>.<NonRepeatingPart>
    For example, 0.5, 1., 2.12, and 123.0001.
<IntegerPart>.<NonRepeatingPart>(<RepeatingPart>)
    For example, 0.1(6), 1.(9), 123.00(1212).

The repeating portion of a decimal expansion is conventionally denoted within a pair of round brackets. For example:

1/6 = 0.16666666... = 0.1(6) = 0.1666(6) = 0.166(66).

Example 1:

Input: s = "0.(52)", t = "0.5(25)"
Output: true
Explanation: Because "0.(52)" represents 0.52525252..., and "0.5(25)" represents 0.52525252525..... , the strings represent the same number.

Example 2:

Input: s = "0.1666(6)", t = "0.166(66)"
Output: true

Example 3:

Input: s = "0.9(9)", t = "1."
Output: true
Explanation: "0.9(9)" represents 0.999999999... repeated forever, which equals 1.
"1." represents the number 1, which is formed correctly: (IntegerPart) = "1" and (NonRepeatingPart) = "".

Constraints:

Each part consists only of digits.
The <IntegerPart> does not have leading zeros (except for the zero itself).
1 <= <IntegerPart>.length <= 4
0 <= <NonRepeatingPart>.length <= 4
1 <= <RepeatingPart>.length <= 4

"""

# V0
# IDEA : PARSE to an EXACT FRACTION, then compare
#
#  Floating point cannot be trusted here (0.9(9) == 1 must hold exactly),
#  so convert each string to a Fraction:
#
#     I . N ( R )
#       = I
#       + N / 10^len(N)
#       + R / ((10^len(R) - 1) * 10^len(N))
#
#  The repeating tail 0.(R) equals R / (10^len(R) - 1)  (e.g. 0.(52) = 52/99),
#  shifted right by len(N) digits.
#
# time = O(L), L = length of the input strings (fixed, tiny)
# space = O(1)
from fractions import Fraction
class Solution(object):
    def isRationalEqual(self, s, t):

        def parse(num):
            if '.' not in num:
                return Fraction(int(num), 1)

            integer, decimal = num.split('.')
            value = Fraction(int(integer), 1) if integer else Fraction(0, 1)

            if '(' in decimal:
                non_rep, rep = decimal.split('(')
                rep = rep.rstrip(')')
            else:
                non_rep, rep = decimal, ''

            if non_rep:
                value += Fraction(int(non_rep), 10 ** len(non_rep))
            if rep:
                value += Fraction(int(rep), (10 ** len(rep) - 1) * 10 ** len(non_rep))
            return value

        return parse(s) == parse(t)
