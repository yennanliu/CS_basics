"""

1844. Replace All Digits with Characters
Easy

You are given a 0-indexed string s that has lowercase English letters in its even indices and digits in its odd indices.

You must perform an operation shift(c, x), where c is a character and x is a digit, that returns the xth character after c.

For example, shift('a', 5) = 'f' and shift('x', 0) = 'x'.

For every odd index i, you want to replace the digit s[i] with the result of the shift(s[i-1], s[i]) operation.

Return s after replacing all digits. It is guaranteed that shift(s[i-1], s[i]) will never exceed 'z'.

Note that shift(c, x) is not a preloaded function, but an operation to be implemented as part of the solution.


Example 1:

Input: s = "a1c1e1"
Output: "abcdef"
Explanation: The digits are replaced as follows:
- s[1] -> shift('a',1) = 'b'
- s[3] -> shift('c',1) = 'd'
- s[5] -> shift('e',1) = 'f'

Example 2:

Input: s = "a1b2c3d4e"
Output: "abbdcfdhe"
Explanation: The digits are replaced as follows:
- s[1] -> shift('a',1) = 'b'
- s[3] -> shift('b',2) = 'd'
- s[5] -> shift('c',3) = 'f'
- s[7] -> shift('d',4) = 'h'


Constraints:

1 <= s.length <= 100
s consists only of lowercase English letters and digits.
shift(s[i-1], s[i]) <= 'z' for all odd indices i.

"""

# V0
# IDEA : IN-PLACE CHARACTER ARITHMETIC ON THE ODD INDICES
#
#   shift(c, x) is just chr(ord(c) + x). walk the odd indices and overwrite
#   each digit with chr(ord(s[i-1]) + int(s[i])).
#
#   NOTE : s[i-1] is always an original LETTER (even index), so reading it after
#          earlier writes is safe -- we only ever write odd positions.
#
# time = O(n), space = O(n)
class Solution(object):
    def replaceDigits(self, s):
        out = list(s)
        for i in range(1, len(out), 2):
            out[i] = chr(ord(out[i - 1]) + int(out[i]))
        return ''.join(out)
