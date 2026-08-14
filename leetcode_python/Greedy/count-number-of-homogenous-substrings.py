"""

1759. Count Number of Homogenous Substrings
Medium

Given a string s, return the number of homogenous substrings of s. Since the answer may be too large, return it modulo 10^9 + 7.

A string is homogenous if all the characters of the string are the same.

A substring is a contiguous sequence of characters within a string.

Example 1:

Input: s = "abbcccaa"
Output: 13
Explanation: The homogenous substrings are listed as below:
"a"   appears 3 times.
"aa"  appears 1 time.
"b"   appears 2 times.
"bb"  appears 1 time.
"c"   appears 3 times.
"cc"  appears 2 times.
"ccc" appears 1 time.
3 + 1 + 2 + 1 + 3 + 2 + 1 = 13.

Example 2:

Input: s = "xy"
Output: 2
Explanation: The homogenous substrings are "x" and "y".

Example 3:

Input: s = "zzzzz"
Output: 15

Constraints:

1 <= s.length <= 10^5
s consists of lowercase letters.

"""

# V0
# IDEA : GROUP CONSECUTIVE RUNS (a run of length L holds L * (L + 1) / 2 substrings)
#
#   every homogenous substring lives inside a maximal run of one repeated
#   character. a run of length L contributes L substrings of size 1, L - 1 of
#   size 2, ... , 1 of size L -> L * (L + 1) / 2 in total.
#   NOTE : sum them over all runs and take the result modulo 10^9 + 7.
#
# time = O(n), space = O(1)
class Solution(object):
    def countHomogenous(self, s):
        MOD = 10 ** 9 + 7
        res = 0
        run = 0
        prev = ""
        for c in s:
            if c == prev:
                run += 1
            else:
                run = 1
                prev = c
            # each new char closes `run` new homogenous substrings
            res = (res + run) % MOD
        return res
