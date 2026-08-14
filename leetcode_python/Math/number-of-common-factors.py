"""

2427. Number of Common Factors
Easy

Given two positive integers a and b, return the number of common factors of a and b.

An integer x is a common factor of a and b if x divides both a and b.


Example 1:

Input: a = 12, b = 6
Output: 4
Explanation: The common factors of 12 and 6 are 1, 2, 3, 6.

Example 2:

Input: a = 25, b = 30
Output: 2
Explanation: The common factors of 25 and 30 are 1, 5.


Constraints:

1 <= a, b <= 1000

"""

# V0
# IDEA : THE COMMON FACTORS OF a AND b ARE EXACTLY THE DIVISORS OF gcd(a, b)
#
#   x divides both a and b iff x divides their greatest common divisor, so
#   the answer is the divisor count of gcd(a, b).
#
#   counting divisors only needs a scan up to sqrt(g), adding 2 per factor
#   pair and 1 when the pair collapses on a perfect square.
#
# time = O(sqrt(min(a, b))), space = O(1)
from math import gcd


class Solution(object):
    def commonFactors(self, a, b):
        g = gcd(a, b)
        res = 0
        d = 1
        while d * d <= g:
            if g % d == 0:
                res += 1 if d * d == g else 2
            d += 1
        return res
