"""

2652. Sum Multiples
Easy

Given a positive integer n, find the sum of all integers in the range [1, n] inclusive that are divisible by 3, 5, or 7.

Return an integer denoting the sum of all numbers in the given range satisfying the constraint.


Example 1:

Input: n = 7
Output: 21
Explanation: Numbers in the range [1, 7] that are divisible by 3, 5, or 7 are 3, 5, 6, 7. The sum of these numbers is 21.

Example 2:

Input: n = 10
Output: 40
Explanation: Numbers in the range [1, 10] that are divisible by 3, 5, or 7 are 3, 5, 6, 7, 9, 10. The sum of these numbers is 40.

Example 3:

Input: n = 9
Output: 30
Explanation: Numbers in the range [1, 9] that are divisible by 3, 5, or 7 are 3, 5, 6, 7, 9. The sum of these numbers is 30.


Constraints:

1 <= n <= 10^3

"""

# V0
# IDEA : DIRECT SCAN
#
#   n <= 1000, so a single sweep over [1, n] is plenty. each number is
#   counted at MOST once thanks to the `or` (15 is divisible by both 3 and
#   5 but is still added a single time).
#
#   NOTE : the inclusion-exclusion closed form
#          f(3) + f(5) + f(7) - f(15) - f(21) - f(35) + f(105)
#          with f(d) = d * m * (m + 1) / 2, m = n // d
#          gives the same answer in O(1) — the loop is kept for clarity.
#
# time = O(n), space = O(1)
class Solution(object):
    def sumOfMultiples(self, n):
        return sum(i for i in range(1, n + 1) if i % 3 == 0 or i % 5 == 0 or i % 7 == 0)
