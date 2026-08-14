"""

2414. Length of the Longest Alphabetical Continuous Substring
Medium

An alphabetical continuous string is a string consisting of consecutive letters in the alphabet. In other words, it is any substring of the string "abcdefghijklmnopqrstuvwxyz".

For example, "abc" is an alphabetical continuous string, while "acb" and "za" are not.

Given a string s consisting of lowercase letters only, return the length of the longest alphabetical continuous substring.


Example 1:

Input: s = "abacaba"
Output: 2
Explanation: There are 4 distinct continuous substrings: "a", "b", "c" and "ab".
"ab" is the longest continuous substring.

Example 2:

Input: s = "abcde"
Output: 5
Explanation: "abcde" is the longest continuous substring.


Constraints:

1 <= s.length <= 10^5
s consists of only English lowercase letters.

"""

# V0
# IDEA : RUN LENGTH OF "EACH CHARACTER IS THE PREVIOUS ONE PLUS 1"
#
#   scan once keeping `run` = the length of the continuous stretch ending at
#   the current index. it extends when ord(s[i]) == ord(s[i-1]) + 1 and
#   restarts at 1 otherwise.
#
#   NOTE : the wrap 'z' -> 'a' is NOT continuous, and the ord() comparison
#          already rejects it.
#
# time = O(n), space = O(1)
class Solution(object):
    def longestContinuousSubstring(self, s):
        res = run = 1
        for i in range(1, len(s)):
            run = run + 1 if ord(s[i]) == ord(s[i - 1]) + 1 else 1
            res = max(res, run)
        return res
