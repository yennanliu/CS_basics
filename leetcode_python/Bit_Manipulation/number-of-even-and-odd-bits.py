"""

2595. Number of Even and Odd Bits
Easy

You are given a positive integer n.

Let even denote the number of even indices in the binary representation of n with value 1.

Let odd denote the number of odd indices in the binary representation of n with value 1.

Note that bits are indexed from right to left in the binary representation of a number.

Return the array [even, odd].


Example 1:

Input: n = 50
Output: [1,2]
Explanation:
The binary representation of 50 is 110010.
It contains 1 on indices 1, 4, and 5.

Example 2:

Input: n = 2
Output: [0,1]
Explanation:
The binary representation of 2 is 10.
It contains 1 only on index 1.


Constraints:

1 <= n <= 1000

"""

# V0
# IDEA : BIT SCAN (walk bits low -> high, flip the bucket each step)
#
#   bit index 0 is the LEAST significant bit, so shifting right by one moves to
#   the next index. Keep a toggle i that alternates 0 (even index) / 1 (odd
#   index) with i ^= 1, and add the current bit into res[i].
#
#   NOTE : "even/odd" refers to the bit's INDEX, not to its value -- index 0
#          (the units bit) is an even index.
#
# time = O(log n), space = O(1)
class Solution(object):
    def evenOddBit(self, n):
        res = [0, 0]
        i = 0
        while n:
            res[i] += n & 1
            n >>= 1
            i ^= 1
        return res
