"""

709. To Lower Case
Easy

Given a string s, return the string after replacing every uppercase letter
with the same lowercase letter.


Example 1:

Input: s = "Hello"
Output: "hello"

Example 2:

Input: s = "here"
Output: "here"

Example 3:

Input: s = "LOVELY"
Output: "lovely"


Constraints:

1 <= s.length <= 100
s consists of printable ASCII characters.

"""

# V0
# IDEA : ASCII BIT TRICK
#
#   For ASCII letters, lowercase = uppercase | 32 ('A' = 65, 'a' = 97).
#   Every other printable character is left untouched.
#
# time = O(n)
# space = O(n)
class Solution(object):
    def toLowerCase(self, s):
        res = []
        for c in s:
            if "A" <= c <= "Z":
                res.append(chr(ord(c) | 32))
            else:
                res.append(c)
        return "".join(res)


# V1
# IDEA : BUILT IN
# time = O(n)
# space = O(n)
class Solution(object):
    def toLowerCase(self, s):
        return s.lower()
