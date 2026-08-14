"""

1362. Closest Divisors
Medium

Given an integer num, find the closest two integers in absolute difference whose product equals num + 1 or num + 2.

Return the two integers in any order.


Example 1:

Input: num = 8
Output: [3,3]
Explanation: For num + 1 = 9, the closest divisors are 3 & 3, for num + 2 = 10, the closest divisors are 2 & 5, hence 3 & 3 is chosen.

Example 2:

Input: num = 123
Output: [5,25]

Example 3:

Input: num = 999
Output: [40,25]


Constraints:

1 <= num <= 10^9

"""

# V0
# IDEA : ENUMERATE DIVISORS DOWN FROM sqrt(x)
#
#  For a fixed product x, the divisor pair (i, x // i) with the smallest
#  |i - x//i| is the one where i is the LARGEST divisor <= sqrt(x).
#  So walk i down from isqrt(x) and stop at the first i dividing x
#  (i = 1 always works, so the loop terminates).
#
#  Do that for both x = num + 1 and x = num + 2, keep the closer pair.
#
# NOTE : use math.isqrt (exact integer sqrt) instead of int(sqrt(x)),
#        float sqrt can be off by one for large x.
# time = O(sqrt(num))
# space = O(1)
from math import isqrt


class Solution(object):
    def closestDivisors(self, num):

        def f(x):
            for i in range(isqrt(x), 0, -1):
                if x % i == 0:
                    return [i, x // i]
            return [1, x]

        a = f(num + 1)
        b = f(num + 2)
        return a if abs(a[0] - a[1]) < abs(b[0] - b[1]) else b
