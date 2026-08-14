"""

2124. Check if All A's Appears Before All B's
Easy

Given a string s consisting of only the characters 'a' and 'b', return true if every 'a' appears before every 'b' in the string. Otherwise, return false.


Example 1:

Input: s = "aaabbb"
Output: true
Explanation:
The 'a's are at indices 0, 1, and 2, while the 'b's are at indices 3, 4, and 5.
Hence, every 'a' appears before every 'b' and we return true.

Example 2:

Input: s = "abab"
Output: false
Explanation:
There is an 'a' at index 2 and a 'b' at index 1.
Hence, not every 'a' appears before every 'b' and we return false.

Example 3:

Input: s = "bbb"
Output: true
Explanation:
There are no 'a's, hence, every 'a' appears before every 'b' and we return true.


Constraints:

1 <= s.length <= 100
s[i] is either 'a' or 'b'.

"""

# V0
# IDEA : THE FORBIDDEN PATTERN IS THE SUBSTRING "ba"
#
#   "every a before every b" fails exactly when some 'b' is immediately (or
#   eventually) followed by an 'a' — and since the alphabet is only {a, b},
#   any such inversion forces an ADJACENT "ba" somewhere in the string.
#
#   so a single substring test settles it.
#
# time = O(n), space = O(1)
class Solution(object):
    def checkString(self, s):
        return 'ba' not in s
