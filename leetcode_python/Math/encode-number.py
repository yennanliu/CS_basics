"""

1256. Encode Number
Medium

Given a non-negative integer num, Return its encoding string.

The encoding is done by converting the integer to a string using a secret function
that you should deduce from the following table:

n    f(n)
0    ""
1    "0"
2    "1"
3    "00"
4    "01"
5    "10"
6    "11"
7    "000"
8    "001"


Example 1:

Input: num = 23

Output: "1000"

Example 2:

Input: num = 107

Output: "101100"


Constraints:

0 <= num <= 10^9

"""

# V0
# IDEA : MATH / BIT TRICK (the table is just binary of num + 1 with the leading 1 dropped)
#
#   group the table by output length :
#     length 0 : n = 0          (1 value)
#     length 1 : n = 1, 2       (2 values)
#     length 2 : n = 3..6       (4 values)
#     length 3 : n = 7..14      (8 values)
#   so the block of length L starts at n = 2^L - 1, i.e. n + 1 lands in
#   [2^L, 2^(L+1)) -- exactly the numbers whose binary form has L+1 bits.
#
#   writing num + 1 in binary always yields a leading '1' followed by the
#   L bits that encode the offset inside the block -> drop that leading bit.
#   NOTE : bin(x) gives '0b1...', so slicing from index 3 removes both the
#          '0b' prefix and the leading '1' in one step.
#
# time = O(log num), space = O(log num)
class Solution(object):
    def encode(self, num):
        return bin(num + 1)[3:]
