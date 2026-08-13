"""

1009. Complement of Base 10 Integer
Easy

The complement of an integer is the integer you get when you flip all the 0's to 1's and all the 1's to 0's in its binary representation.

For example, The integer 5 is "101" in binary and its complement is "010" which is the integer 2.

Given an integer n, return its complement.


Example 1:

Input: n = 5
Output: 2
Explanation: 5 is "101" in binary, with complement "010" in binary, which is 2 in base-10.

Example 2:

Input: n = 7
Output: 0
Explanation: 7 is "111" in binary, with complement "000" in binary, which is 0 in base-10.

Example 3:

Input: n = 10
Output: 5
Explanation: 10 is "1010" in binary, with complement "0101" in binary, which is 5 in base-10.


Constraints:

0 <= n < 10^9


Note: This question is the same as 476: Number Complement

"""

# V0
# IDEA : BIT MANIPULATION, build an all-1 mask of the SAME bit length as n
#
#   n    = 1010
#   mask = 1111
#   ans  = n XOR mask = 0101
#
#   edge : n = 0 -> its binary is "0", complement is "1" -> return 1
#
# time = O(log n)
# space = O(1)
class Solution(object):
    def bitwiseComplement(self, n):
        if n == 0:
            return 1
        mask = 1
        while mask < n:
            mask = (mask << 1) | 1
        return n ^ mask


# V1
# IDEA : STRING, flip every char of bin(n)
# time = O(log n)
# space = O(log n)
class Solution(object):
    def bitwiseComplement(self, n):
        bits = bin(n)[2:]
        flipped = ''.join('1' if c == '0' else '0' for c in bits)
        return int(flipped, 2)
