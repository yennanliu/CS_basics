"""

1318. Minimum Flips to Make a OR b Equal to c
Medium

Given 3 positives numbers a, b and c. Return the minimum flips required in some bits of a and b
to make ( a OR b == c ). (bitwise OR operation).

Flip operation consists of change any single bit 1 to 0 or change the bit 0 to 1
in their binary representation.


Example 1:

Input: a = 2, b = 6, c = 5

Output: 3

Explanation: After flips a = 1 , b = 4 , c = 5 such that (a OR b == c)

Example 2:

Input: a = 4, b = 2, c = 7

Output: 1

Example 3:

Input: a = 1, b = 2, c = 3

Output: 0


Constraints:

1 <= a <= 10^9
1 <= b <= 10^9
1 <= c <= 10^9

"""

# V0
# IDEA : BIT MANIPULATION (bits are independent -> decide each one alone)
#
#   OR acts bitwise, so bit i of the answer only depends on (x, y, z) =
#   (bit i of a, of b, of c). Two cases per position :
#
#     z == 0 : both a and b must end at 0 -> cost = x + y   (flip every 1 down)
#     z == 1 : at least one of a, b must be 1
#              -> cost = 1 if x == y == 0 (flip one of them up), else 0
#
#   NOTE : when z == 1 we never need 2 flips -- one 1 already satisfies OR.
#   NOTE : a, b, c <= 10^9 < 2^30, so 32 iterations cover every set bit.
#
# time = O(32), space = O(1)
class Solution(object):
    def minFlips(self, a, b, c):
        res = 0
        for i in range(32):
            x, y, z = (a >> i) & 1, (b >> i) & 1, (c >> i) & 1
            if z == 0:
                res += x + y
            elif x == 0 and y == 0:
                res += 1
        return res
