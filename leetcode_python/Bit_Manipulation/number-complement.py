"""

476. Number Complement
Easy

The complement of an integer is the integer you get when you flip all the 0's to
1's and all the 1's to 0's in its binary representation.

- For example, The integer 5 is "101" in binary and its complement is "010"
  which is the integer 2.

Given an integer num, return its complement.

Example 1:

Input: num = 5
Output: 2
Explanation: The binary representation of 5 is 101 (no leading zero bits), and
its complement is 010. So you need to output 2.

Example 2:

Input: num = 1
Output: 0
Explanation: The binary representation of 1 is 1 (no leading zero bits), and its
complement is 0. So you need to output 0.

Constraints:

1 <= num < 2^31

Note: This question is the same as 1009: Complement of Base 10 Integer.

"""

# V0
# IDEA : XOR WITH AN ALL-ONES MASK OF THE SAME BIT WIDTH
#
#  Flipping every bit == XOR with 1. We only want to flip the bits that are
#  actually part of num (no leading zeros), so build a mask of exactly
#  num.bit_length() ones:
#
#     num  = 5  = 101
#     mask = (1 << 3) - 1 = 111
#     5 ^ 7 = 010 = 2
#
# time = O(1)
# space = O(1)
class Solution(object):
    def findComplement(self, num):
        mask = (1 << num.bit_length()) - 1
        return num ^ mask
