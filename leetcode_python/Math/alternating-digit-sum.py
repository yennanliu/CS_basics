"""

2544. Alternating Digit Sum
Easy

You are given a positive integer n. Each digit of n has a sign according to the following rules:

The most significant digit is assigned a positive sign.
Each other digit has an opposite sign to its adjacent digits.

Return the sum of all digits with their corresponding sign.


Example 1:

Input: n = 521
Output: 4
Explanation: (+5) + (-2) + (+1) = 4.

Example 2:

Input: n = 111
Output: 1
Explanation: (+1) + (-1) + (+1) = 1.

Example 3:

Input: n = 886996
Output: 0
Explanation: (+8) + (-8) + (+6) + (-9) + (+9) + (-6) = 0.


Constraints:

1 <= n <= 10^9

"""

# V0
# IDEA : SIMULATION (walk digits left -> right, flip the sign each step)
#
#   the sign pattern is fixed by the MOST significant digit being '+', so the
#   easiest handling is to stringify n and iterate from the left: index 0 gets
#   +, index 1 gets -, and so on.
#
#   NOTE : peeling digits with % 10 instead would walk right -> left, where the
#          leading sign depends on the digit COUNT parity — an easy off-by-one.
#          going through str(n) sidesteps that entirely.
#
# time = O(log n), space = O(log n)
class Solution(object):
    def alternateDigitSum(self, n):
        total = 0
        sign = 1
        for c in str(n):
            total += sign * int(c)
            sign = -sign
        return total
