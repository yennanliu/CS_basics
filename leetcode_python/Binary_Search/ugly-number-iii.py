"""

1201. Ugly Number III
Medium

An ugly number is a positive integer that is divisible by a, b, or c.

Given four integers n, a, b, and c, return the nth ugly number.


Example 1:

Input: n = 3, a = 2, b = 3, c = 5
Output: 4
Explanation: The ugly numbers are 2, 3, 4, 5, 6, 8, 9, 10... The 3rd is 4.

Example 2:

Input: n = 4, a = 2, b = 3, c = 4
Output: 6
Explanation: The ugly numbers are 2, 3, 4, 6, 8, 9, 10, 12... The 4th is 6.

Example 3:

Input: n = 5, a = 2, b = 11, c = 13
Output: 10
Explanation: The ugly numbers are 2, 4, 6, 8, 10, 11, 12, 13... The 5th is 10.


Constraints:

1 <= n, a, b, c <= 10^9
1 <= a * b * c <= 10^18
It is guaranteed that the result will be in range [1, 2 * 10^9].

"""

# V0
# IDEA: BINARY SEARCH on ANSWER + INCLUSION-EXCLUSION
"""

 -> count(x) = how many ugly numbers are <= x
             = x/a + x/b + x/c
               - x/lcm(a,b) - x/lcm(a,c) - x/lcm(b,c)
               + x/lcm(a,b,c)          (all floor division)

 -> count(x) is MONOTONIC non decreasing in x,
    so we binary search the SMALLEST x with count(x) >= n.

 -> that x is itself ugly: if it were not, count(x) == count(x-1),
    and x-1 would already satisfy the predicate.
"""
# time = O(log(2 * 10^9))
# space = O(1)
from math import gcd
class Solution(object):
    def nthUglyNumber(self, n, a, b, c):

        def lcm(x, y):
            return x // gcd(x, y) * y

        ab = lcm(a, b)
        ac = lcm(a, c)
        bc = lcm(b, c)
        abc = lcm(ab, c)

        def count(x):
            return (x // a + x // b + x // c
                    - x // ab - x // ac - x // bc
                    + x // abc)

        """
        NOTE !!!

        -> "left bisect" template: find the FIRST x with count(x) >= n
        """
        left, right = 1, 2 * 10 ** 9
        while left < right:
            mid = (left + right) // 2
            if count(mid) >= n:
                right = mid
            else:
                left = mid + 1

        return left
