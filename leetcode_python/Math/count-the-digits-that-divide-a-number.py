"""

2520. Count the Digits That Divide a Number
Easy

Given an integer num, return the number of digits in num that divide num.

An integer val divides nums if nums % val == 0.


Example 1:

Input: num = 7
Output: 1
Explanation: 7 divides itself, hence the answer is 1.

Example 2:

Input: num = 121
Output: 2
Explanation: 121 is divisible by 1, but not 2. Since 1 occurs twice as a digit, we return 2.

Example 3:

Input: num = 1248
Output: 4
Explanation: 1248 is divisible by all of its digits, hence the answer is 4.


Constraints:

1 <= num <= 10^9
num does not contain 0 as one of its digits.

"""

# V0
# IDEA : DIGIT ENUMERATION (peel digits off with divmod)
#
#   walk the decimal digits of num one at a time (x % 10 gives the last digit,
#   x // 10 drops it) and count how many of them divide the ORIGINAL num.
#
#   NOTE : the divisibility test is always against the original `num`, not
#          against the shrinking remainder `x`. Keep the two variables apart.
#
#   NOTE : the constraints promise no digit is 0, so `num % digit` can never
#          hit a ZeroDivisionError. Duplicated digits are counted each time
#          they occur (121 -> the digit 1 counts twice).
#
# time = O(log num), space = O(1)
class Solution(object):
    def countDigits(self, num):
        res = 0
        x = num
        while x:
            x, digit = divmod(x, 10)
            if num % digit == 0:
                res += 1
        return res
