"""

1780. Check if Number is a Sum of Powers of Three
Medium

Given an integer n, return true if it is possible to represent n as the sum of distinct powers of three. Otherwise, return false.

An integer y is a power of three if there exists an integer x such that y == 3^x.

Example 1:

Input: n = 12
Output: true
Explanation: 12 = 3^1 + 3^2

Example 2:

Input: n = 91
Output: true
Explanation: 91 = 3^0 + 3^2 + 3^4

Example 3:

Input: n = 21
Output: false

Constraints:

1 <= n <= 10^7

"""

# V0
# IDEA : BASE-3 REPRESENTATION (distinct powers of 3 <=> every trit is 0 or 1)
#
#   writing n in base 3, the digit at position k says how many times 3^k is
#   used. "distinct powers" means each 3^k appears at most once, i.e. every
#   base-3 digit must be 0 or 1.
#   NOTE : so the check is simply: repeatedly take n % 3, reject on a 2.
#
# time = O(log n), space = O(1)
class Solution(object):
    def checkPowersOfThree(self, n):
        while n:
            if n % 3 == 2:
                return False
            n //= 3
        return True
