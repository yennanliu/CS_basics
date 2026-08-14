"""

1922. Count Good Numbers
Medium

A digit string is good if the digits (0-indexed) at even indices are even and the digits at odd indices are prime (2, 3, 5, or 7).

For example, "2582" is good because the digits (2 and 8) at even positions are even and the digits (5 and 2) at odd positions are prime. However, "3245" is not good because 3 is at an even index but is not even.

Given an integer n, return the total number of good digit strings of length n. Since the answer may be large, return it modulo 10^9 + 7.

A digit string is a string consisting of digits 0 through 9 that may contain leading zeros.


Example 1:

Input: n = 1
Output: 5
Explanation: The good numbers of length 1 are "0", "2", "4", "6", "8".

Example 2:

Input: n = 4
Output: 400

Example 3:

Input: n = 50
Output: 564908303


Constraints:

1 <= n <= 10^15

"""

# V0
# IDEA : COMBINATORICS + FAST POWER (each position is independent)
#
#   even indices 0, 2, 4, ...  -> 5 choices each (0,2,4,6,8)
#   odd  indices 1, 3, 5, ...  -> 4 choices each (2,3,5,7)
#
#   count of even indices = ceil(n/2) = (n + 1) // 2
#   count of odd  indices = floor(n/2) = n // 2
#
#   answer = 5^((n+1)//2) * 4^(n//2)  (mod 10^9 + 7)
#
#   NOTE : n is up to 10^15, so the exponent must be handled by binary
#          exponentiation - pow(base, exp, mod) already does that in O(log n).
#
# time = O(log n), space = O(1)
class Solution(object):
    def countGoodNumbers(self, n):
        MOD = 10 ** 9 + 7
        return pow(5, (n + 1) // 2, MOD) * pow(4, n // 2, MOD) % MOD
