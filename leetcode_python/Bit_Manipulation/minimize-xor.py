"""

2429. Minimize XOR
Medium

Given two positive integers num1 and num2, find the positive integer x such that:

x has the same number of set bits as num2, and
The value x XOR num1 is minimal.

Note that XOR is the bitwise XOR operation.

Return the integer x. The test cases are generated such that x is uniquely determined.

The number of set bits of an integer is the number of 1's in its binary representation.


Example 1:

Input: num1 = 3, num2 = 5
Output: 3
Explanation:
The binary representations of num1 and num2 are 0011 and 0101, respectively.
The integer 3 has the same number of set bits as num2, and the value 3 XOR 3 = 0 is minimal.

Example 2:

Input: num1 = 1, num2 = 12
Output: 3
Explanation:
The binary representations of num1 and num2 are 0001 and 1100, respectively.
The integer 3 has the same number of set bits as num2, and the value 3 XOR 1 = 2 is minimal.


Constraints:

1 <= num1, num2 <= 10^9

"""

# V0
# IDEA : SPEND THE BIT BUDGET ON num1's HIGHEST SET BITS, THEN THE LOWEST GAPS
#
#   x must carry exactly popcount(num2) set bits. every bit shared with num1
#   cancels in the XOR, so matching a set bit of num1 SAVES that bit's weight
#   — and the heaviest ones are worth the most. so hand out the budget to
#   num1's set bits from the top down.
#
#   if bits remain after that, they must land on positions where num1 is 0,
#   and there the cheapest choice is the LOWEST such positions.
#
#   30 bits cover 10^9.
#
# time = O(30), space = O(1)
class Solution(object):
    def minimizeXor(self, num1, num2):
        budget = bin(num2).count('1')
        res = 0

        for b in range(31, -1, -1):          # keep num1's heaviest bits
            if budget and num1 >> b & 1:
                res |= 1 << b
                budget -= 1

        for b in range(32):                  # then the cheapest free slots
            if budget == 0:
                break
            if not (res >> b & 1):
                res |= 1 << b
                budget -= 1
        return res
