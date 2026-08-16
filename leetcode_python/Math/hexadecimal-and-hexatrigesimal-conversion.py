"""

3602. Hexadecimal and Hexatrigesimal Conversion
Easy

You are given an integer n.

Return the concatenation of the hexadecimal representation of n^2 and the hexatrigesimal representation of n^3.

A hexadecimal number is defined as a base-16 numeral system that uses the digits 0 - 9 along with the uppercase letters A - F to represent values from 0 to 15.

A hexatrigesimal number is defined as a base-36 numeral system that uses the digits 0 - 9 along with the uppercase letters A - Z to represent values from 0 to 35.


Example 1:

Input: n = 13
Output: "A91P1"
Explanation:
n^2 = 13 * 13 = 169. In hexadecimal, 169 is A9.
n^3 = 13 * 13 * 13 = 2197. In hexatrigesimal, 2197 is 1P1.
Concatenating both gives us A9 + 1P1 = A91P1.

Example 2:

Input: n = 36
Output: "5101000"
Explanation:
n^2 = 36 * 36 = 1296. In hexadecimal, 1296 is 510.
n^3 = 36 * 36 * 36 = 46656. In hexatrigesimal, 46656 is 1000.
Concatenating both gives us 510 + 1000 = 5101000.


Constraints:

1 <= n <= 1000

"""

# V0
# IDEA : ONE GENERIC BASE-CONVERSION HELPER, CALLED TWICE
#
#   repeated divmod peels off the least significant digit first, so the
#   digits come out reversed and get flipped at the end. both bases draw
#   from the same "0-9 then A-Z" alphabet, base 16 just stops at F, so a
#   single digit table serves both calls.
#
#   n >= 1 means neither power is ever zero, so the "0" special case cannot
#   arise here — it is kept only for safety.
#
# time = O(log n), space = O(log n)
class Solution(object):
    def concatHex36(self, n):
        DIGITS = "0123456789ABCDEFGHIJKLMNOPQRSTUVWXYZ"

        def to_base(x, base):
            if x == 0:
                return "0"
            out = []
            while x:
                x, r = divmod(x, base)
                out.append(DIGITS[r])
            return "".join(reversed(out))

        return to_base(n * n, 16) + to_base(n * n * n, 36)
