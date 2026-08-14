"""

1513. Number of Substrings With Only 1s
Medium

Given a binary string s, return the number of substrings with all characters 1's. Since the answer may be too large, return it modulo 10^9 + 7.


Example 1:

Input: s = "0110111"
Output: 9
Explanation: There are 9 substring in total with only 1's characters.
"1" -> 5 times.
"11" -> 3 times.
"111" -> 1 time.

Example 2:

Input: s = "101"
Output: 2
Explanation: Substring "1" is shown 2 times in s.

Example 3:

Input: s = "111111"
Output: 21
Explanation: Each substring contains only 1's characters.


Constraints:

1 <= s.length <= 10^5
s[i] is either '0' or '1'.

"""

# V0
# IDEA : COUNT RUNS INCREMENTALLY
#
#   let cur = length of the run of 1s ending exactly at index i.
#   every all-1 substring ending at i is determined by where it starts,
#   and there are exactly `cur` such choices -> add cur to the answer.
#
#   a '0' breaks the run, so reset cur = 0.
#   (equivalently a run of length L contributes L*(L+1)/2.)
#
# time = O(n), space = O(1)
class Solution(object):
    def numSub(self, s):
        MOD = 10 ** 9 + 7
        res = 0
        cur = 0
        for c in s:
            if c == '0':
                cur = 0
            else:
                cur += 1
                res = (res + cur) % MOD
        return res
