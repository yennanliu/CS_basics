"""

878. Nth Magical Number
Hard

A positive integer is magical if it is divisible by either a or b.

Given the three integers n, a, and b, return the nth magical number.
Since the answer may be very large, return it modulo 10^9 + 7.


Example 1:

Input: n = 1, a = 2, b = 3
Output: 2

Example 2:

Input: n = 4, a = 2, b = 3
Output: 6


Constraints:

1 <= n <= 10^9
2 <= a, b <= 4 * 10^4

"""

# V0
# IDEA : BINARY SEARCH ON ANSWER + INCLUSION-EXCLUSION
#
#   count(x) = how many magical numbers are <= x
#            = x//a + x//b - x//lcm(a, b)
#
#   count(x) is monotonically non-decreasing, so we binary search the
#   smallest x with count(x) >= n. That x is itself magical (the count
#   only increases at multiples of a or b).
#
# time = O(log(n * min(a, b)))
# space = O(1)
class Solution(object):
    def nthMagicalNumber(self, n, a, b):
        MOD = 10 ** 9 + 7

        def gcd(x, y):
            while y:
                x, y = y, x % y
            return x

        lcm = a // gcd(a, b) * b

        # the n-th magical number is at most n * min(a, b)
        lo, hi = min(a, b), min(a, b) * n
        while lo < hi:
            mid = (lo + hi) // 2
            if mid // a + mid // b - mid // lcm >= n:
                hi = mid
            else:
                lo = mid + 1

        # NOTE: take the modulo only at the very end,
        #       the binary search must run on the true value
        return lo % MOD
