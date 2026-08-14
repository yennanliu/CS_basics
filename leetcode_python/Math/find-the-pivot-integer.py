"""

2485. Find the Pivot Integer
Easy

Given a positive integer n, find the pivot integer x such that:

The sum of all elements between 1 and x inclusively equals the sum of all elements between x and n inclusively.

Return the pivot integer x. If no such integer exists, return -1. It is guaranteed that there will be at most one pivot index for the given input.


Example 1:

Input: n = 8
Output: 6
Explanation: 6 is the pivot integer since: 1 + 2 + 3 + 4 + 5 + 6 = 6 + 7 + 8 = 21.

Example 2:

Input: n = 1
Output: 1
Explanation: 1 is the pivot integer since: 1 = 1.

Example 3:

Input: n = 4
Output: -1
Explanation: It can be proven that no such integer exist.


Constraints:

1 <= n <= 1000

"""

# V0
# IDEA : SOLVE IT ALGEBRAICALLY — x^2 = n(n+1)/2
#
#   the two sums overlap at x, so the condition
#       1 + ... + x  ==  x + ... + n
#   expands to  x(x+1)/2 == n(n+1)/2 - x(x-1)/2, which simplifies to
#       x^2 = n(n+1)/2
#
#   so the pivot exists exactly when the triangular number n(n+1)/2 is a
#   perfect square, and then x is its integer square root.
#
#   isqrt keeps the check exact — a float sqrt could misjudge the boundary.
#
# time = O(1), space = O(1)
from math import isqrt


class Solution(object):
    def pivotInteger(self, n):
        total = n * (n + 1) // 2
        x = isqrt(total)
        return x if x * x == total else -1
