"""

2309. Greatest English Letter in Upper and Lower Case
Easy

Given a string of English letters s, return the greatest English letter which occurs as both a lowercase and uppercase letter in s. The returned letter should be in uppercase. If no such letter exists, return an empty string.

An English letter b is greater than another letter a if b appears after a in the English alphabet.


Example 1:

Input: s = "lEeTcOdE"
Output: "E"
Explanation:
The letter 'E' is the only letter to appear in both lower and upper case.

Example 2:

Input: s = "arRAzFif"
Output: "R"
Explanation:
The letter 'R' is the greatest letter to appear in both lower and upper case.
Note that 'A' and 'F' also appear in both lower and upper case, but 'R' is greater than 'F' or 'A'.

Example 3:

Input: s = "AbCdEfGhIjK"
Output: ""
Explanation:
There is no letter that appears in both lower and upper case.


Constraints:

1 <= s.length <= 1000
s consists of lowercase and uppercase English letters.

"""

# V0
# IDEA : BITMASK OF SEEN LETTERS (26 bits for lower, 26 for upper)
#
#   build two 26-bit masks: bit i set means letter i was seen in that case.
#   the letters present in BOTH cases are (lower & upper); the greatest one
#   is the highest set bit, i.e. bit_length() - 1.
#
#   NOTE : an empty intersection -> mask == 0 -> return "".
#
# time = O(n), space = O(1)
class Solution(object):
    def greatestLetter(self, s):
        lower = 0
        upper = 0
        for c in s:
            if 'a' <= c <= 'z':
                lower |= 1 << (ord(c) - 97)
            else:
                upper |= 1 << (ord(c) - 65)
        both = lower & upper
        if not both:
            return ""
        return chr(both.bit_length() - 1 + 65)
