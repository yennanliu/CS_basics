"""

479. Largest Palindrome Product
Hard

Given an integer n, return the largest palindromic integer that can be
represented as the product of two n-digits integers. Since the answer can be
very large, return it modulo 1337.

Example 1:

Input: n = 2
Output: 987
Explanation: 99 x 91 = 9009, 9009 % 1337 = 987

Example 2:

Input: n = 1
Output: 9

Constraints:

1 <= n <= 8

"""

# V0
# IDEA : ENUMERATE PALINDROMES DOWNWARDS, THEN TRIAL-DIVIDE
#
#  For n >= 2 the product of two n-digit numbers that is a palindrome has an
#  EVEN number of digits (2n). So build the palindrome from its first half:
#
#     half = 99  ->  palindrome = 9999
#     half = 98  ->  palindrome = 9889
#     ...
#     half = 90  ->  palindrome = 9009   (= 99 * 91, the answer for n = 2)
#
#  Walking `half` downwards walks the palindromes in DECREASING order, so the
#  first one that factors into two n-digit numbers is the answer.
#
#  To test a palindrome x, try divisors t from the largest n-digit number down
#  while t * t >= x (past that point the other factor would be the bigger one,
#  already tested). If x % t == 0, then x / t <= t and x / t is still n digits
#  (since x >= 9 * 10^(2n-1) and t <= 10^n - 1), so we have our pair.
#
#  n = 1 is the odd one out (the answer 9 = 3 * 3 has one digit) -> handled by
#  the final `return 9`.
#
# time = O(10^n * 10^n) worst case, but in practice the answer is found after a
#        handful of palindromes
# space = O(1)
class Solution(object):
    def largestPalindrome(self, n):
        mx = 10 ** n - 1                    # largest n-digit number

        # `half` runs over the n-digit numbers, biggest first
        for half in range(mx, mx // 10, -1):
            # mirror `half` onto itself -> a 2n-digit palindrome
            x = half
            b = half
            while b:
                x = x * 10 + b % 10
                b //= 10

            t = mx
            while t * t >= x:
                if x % t == 0:
                    return x % 1337
                t -= 1

        # only reachable for n == 1
        return 9
