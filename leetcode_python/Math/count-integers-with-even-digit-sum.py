"""

2180. Count Integers With Even Digit Sum
Easy

Given a positive integer num, return the number of positive integers less than or equal to num whose digit sums are even.

The digit sum of a positive integer is the sum of all its digits.

Example 1:

Input: num = 4
Output: 2
Explanation:
The only integers less than or equal to 4 whose digit sums are even are 2 and 4.

Example 2:

Input: num = 30
Output: 14
Explanation:
The 14 integers less than or equal to 30 whose digit sums are even are
2, 4, 6, 8, 11, 13, 15, 17, 19, 20, 22, 24, 26, and 28.

Constraints:

1 <= num <= 1000

"""

# V0
# IDEA : MATH / COUNTING (every block of 10 holds exactly 5 even digit sums)
#
#   write num = 10*q + r.  inside any fixed prefix q the last digit runs
#   0..9, and exactly 5 of those 10 give an even digit sum.
#   -> the full decades 0..10q-1 contribute 5*q, minus 1 because the
#      number 0 itself is counted there but is not positive.
#   the tail 10q .. 10q+r contributes the d in [0, r] with
#   d parity == parity of digitsum(q) -> (r + 2 - (s & 1)) // 2.
#
# time = O(log num), space = O(1)
class Solution(object):
    def countEven(self, num):
        q, r = num // 10, num % 10
        res = q * 5 - 1
        s, x = 0, q
        while x:
            s += x % 10
            x //= 10
        res += (r + 2 - (s & 1)) // 2
        return res
