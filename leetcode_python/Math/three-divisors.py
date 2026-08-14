"""

1952. Three Divisors
Easy

Given an integer n, return true if n has exactly three positive divisors. Otherwise, return false.

An integer m is a divisor of n if there exists an integer k such that n = k * m.


Example 1:

Input: n = 2
Output: false
Explantion: 2 has only two divisors: 1 and 2.

Example 2:

Input: n = 4
Output: true
Explantion: 4 has three divisors: 1, 2, and 4.


Constraints:

1 <= n <= 10^4

"""

# V0
# IDEA : NUMBER THEORY (exactly 3 divisors <=> n is the SQUARE OF A PRIME)
#
#   divisors come in pairs (d, n/d), so the count is odd only for perfect
#   squares. an odd count of exactly 3 means the divisors are {1, r, n} with
#   r = sqrt(n) -> r must be prime, otherwise r would drag in more divisors.
#
#   so : n is a perfect square AND its root is prime.
#
#   NOTE : use an integer root and verify r*r == n instead of trusting a float
#          sqrt near the boundary.
#
# time = O(sqrt(n)), space = O(1)
class Solution(object):
    def isThree(self, n):
        r = int(n ** 0.5)
        while r * r > n:
            r -= 1
        while (r + 1) * (r + 1) <= n:
            r += 1
        if r * r != n or r < 2:
            return False
        d = 2
        while d * d <= r:
            if r % d == 0:
                return False
            d += 1
        return True
