"""

1271. Hexspeak
Easy

A decimal number can be converted to its Hexspeak representation by first
converting it to an uppercase hexadecimal string, then replacing all
occurrences of the digit '0' with the letter 'O', and the digit '1' with the
letter 'I'. Such a representation is valid if and only if it consists only of
the letters in the set {'A', 'B', 'C', 'D', 'E', 'F', 'I', 'O'}.

Given a string num representing a decimal integer n, return the Hexspeak
representation of n if it is valid, otherwise return "ERROR".


Example 1:

Input: num = "257"
Output: "IOI"
Explanation: 257 is 101 in hexadecimal.

Example 2:

Input: num = "3"
Output: "ERROR"


Constraints:

1 <= num.length <= 12
num does not contain leading zeros.
num represents an integer in the range [1, 10^12].

"""

# V0
# IDEA : STRING + BASE CONVERSION
#        to hex (uppercase) -> map '0' to 'O' and '1' to 'I'
#        -> any leftover digit (2..9) makes it invalid
# time = O(log n)
# space = O(log n)
class Solution(object):
    def toHexspeak(self, num):
        s = "{:X}".format(int(num))
        res = []
        for c in s:
            if c == '0':
                res.append('O')
            elif c == '1':
                res.append('I')
            elif c.isdigit():
                # 2 ~ 9 have no letter counterpart
                return "ERROR"
            else:
                res.append(c)
        return "".join(res)
