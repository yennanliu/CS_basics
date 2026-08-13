"""

1006. Clumsy Factorial
Medium

The factorial of a positive integer n is the product of all positive integers less than or equal to n.

For example, factorial(10) = 10 * 9 * 8 * 7 * 6 * 5 * 4 * 3 * 2 * 1.

We make a clumsy factorial using the integers in decreasing order by swapping out the multiply operations for a fixed rotation of operations with multiply '*', divide '/', add '+', and subtract '-' in this order.

For example, clumsy(10) = 10 * 9 / 8 + 7 - 6 * 5 / 4 + 3 - 2 * 1.

However, these operations are still applied using the usual order of operations of arithmetic. We do all multiplication and division steps before any addition or subtraction steps, and multiplication and division steps are processed left to right.

Additionally, the division that we use is floor division such that 10 * 9 / 8 = 90 / 8 = 11.

Given an integer n, return the clumsy factorial of n.


Example 1:

Input: n = 4
Output: 7
Explanation: 7 = 4 * 3 / 2 + 1

Example 2:

Input: n = 10
Output: 12
Explanation: 12 = 10 * 9 / 8 + 7 - 6 * 5 / 4 + 3 - 2 * 1


Constraints:

1 <= n <= 10^4

"""

# V0
# IDEA : SIMULATION, group the terms 4 at a time
#
#   clumsy(n) = (n*(n-1)/(n-2) + (n-3))
#               - (n-4)*(n-5)/(n-6) + (n-7)
#               - ...
#
#   so : the FIRST block "a*b/c + d" is added,
#        every LATER block contributes "- a*b/c + d".
#   the tail (fewer than 4 numbers left) is just subtracted as
#   1 / 2 / 3*2*1 -> 1 / 2 / 6.
#
#   NOTE : every a*b/c here has a > b > c > 0, so the value is
#          positive and python's `//` matches the required floor division.
#
# time = O(n)
# space = O(1)
class Solution(object):
    def clumsy(self, n):
        # tail values : clumsy(1)=1, clumsy(2)=2, clumsy(3)=6, clumsy(4)=7
        if n <= 2:
            return n
        if n == 3:
            return 6
        if n == 4:
            return 7

        res = n * (n - 1) // (n - 2) + (n - 3)
        n -= 4
        while n >= 4:
            res += -(n * (n - 1) // (n - 2)) + (n - 3)
            n -= 4

        # leftover : n in {0, 1, 2, 3}, subtracted as a whole
        if n == 1:
            res -= 1
        elif n == 2:
            res -= 2
        elif n == 3:
            res -= 6
        return res


# V1
# IDEA : O(1) FORMULA (the answer is periodic in n % 4 once n >= 5)
# time = O(1)
# space = O(1)
class Solution(object):
    def clumsy(self, n):
        if n <= 2:
            return n
        if n == 3:
            return 6
        if n == 4:
            return 7
        r = n % 4
        if r == 0:
            return n + 1
        if r == 1 or r == 2:
            return n + 2
        return n - 1
