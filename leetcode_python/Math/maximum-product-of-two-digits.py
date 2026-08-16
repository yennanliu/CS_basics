"""

3536. Maximum Product of Two Digits
Easy

You are given a positive integer n.

Return the maximum product of any two digits in n.

Note: You may use the same digit twice only if it appears at least twice in the
number.


Example 1:

Input: n = 31
Output: 3
Explanation:
The digits of n are [3, 1].
The possible products of any two digits are: 3 * 1 = 3.
The maximum product is 3.

Example 2:

Input: n = 22
Output: 4
Explanation:
The digits of n are [2, 2].
The possible products of any two digits are: 2 * 2 = 4.
The maximum product is 4.

Example 3:

Input: n = 124
Output: 8
Explanation:
The digits of n are [1, 2, 4].
The possible products of any two digits are: 1 * 2 = 2, 1 * 4 = 4, 2 * 4 = 8.
The maximum product is 8.


Constraints:

10 <= n <= 10^9

"""

# V0
# IDEA : TAKE THE TWO LARGEST DIGITS
#
#   digits are non-negative, so the product of the two largest digits dominates
#   any other pair.  sorting the digit list descending and multiplying the first
#   two is enough — and because n has at least two digits the pair always exists.
#
#   "the same digit twice only if it appears twice" is automatically respected:
#   we take two distinct positions from the digit list.
#
# time = O(d log d) with d <= 10 digits, space = O(d)
class Solution(object):
    def maxProduct(self, n):
        digits = sorted((int(c) for c in str(n)), reverse=True)
        return digits[0] * digits[1]
