"""

3622. Check Divisibility by Digit Sum and Product
Easy

You are given a positive integer n. Determine whether n is divisible by the sum
of the following two values:

The digit sum of n (the sum of its digits).
The digit product of n (the product of its digits).

Return true if n is divisible by this sum; otherwise, return false.


Example 1:

Input: n = 99
Output: true
Explanation:
Since 99 is divisible by the sum (9 + 9 = 18) plus product (9 * 9 = 81) of its
digits (total 99), the output is true.

Example 2:

Input: n = 23
Output: false
Explanation:
Since 23 is not divisible by the sum (2 + 3 = 5) plus product (2 * 3 = 6) of its
digits (total 11), the output is false.


Constraints:

1 <= n <= 10^6

"""

# V0
# IDEA : DIGIT SCAN
#
#   nothing subtle here: peel the decimal digits off with divmod, accumulate
#   the sum and the product as we go, then test one divisibility.
#
#   the only value worth thinking about is the divisor s + p. n >= 1 so it
#   has at least one digit, and its leading digit is non-zero, hence s >= 1
#   and s + p >= 1 — the modulo can never divide by zero even when some digit
#   is 0 and drags the product down to 0.
#
# time = O(log n), space = O(1)
class Solution(object):
    def checkDivisibility(self, n):
        s, p = 0, 1
        x = n
        while x:
            x, d = divmod(x, 10)
            s += d
            p *= d
        return n % (s + p) == 0
