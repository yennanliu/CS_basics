"""

2413. Smallest Even Multiple
Easy

Given a positive integer n, return the smallest positive integer that is a multiple of both 2 and n.


Example 1:

Input: n = 5
Output: 10
Explanation: The smallest multiple of both 5 and 2 is 10.

Example 2:

Input: n = 6
Output: 6
Explanation: The smallest multiple of both 6 and 2 is 6. Note that a number is a multiple of itself.


Constraints:

1 <= n <= 150

"""

# V0
# IDEA : lcm(2, n) — WHICH IS n IF n IS EVEN, ELSE 2n
#
#   the smallest common multiple of 2 and n is 2n / gcd(2, n), and gcd(2, n)
#   is 2 exactly when n is even. so the whole thing is a parity check.
#
# time = O(1), space = O(1)
class Solution(object):
    def smallestEvenMultiple(self, n):
        return n if n % 2 == 0 else 2 * n
