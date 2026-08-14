"""

1221. Split a String in Balanced Strings
Easy

Balanced strings are those that have an equal quantity of 'L' and 'R' characters.

Given a balanced string s, split it into some number of substrings such that:

Each substring is balanced.

Return the maximum number of balanced strings you can obtain.

Example 1:

Input: s = "RLRRLLRLRL"
Output: 4
Explanation: s can be split into "RL", "RRLL", "RL", "RL", each substring contains same
number of 'L' and 'R'.

Example 2:

Input: s = "RLRRRLLRLL"
Output: 2
Explanation: s can be split into "RL", "RRRLLRLL", each substring contains same number
of 'L' and 'R'.
Note that s cannot be split into "RL", "RR", "RL", "LR", "LL", because the 2nd and 5th
substrings are not balanced.

Example 3:

Input: s = "LLLLRRRR"
Output: 1
Explanation: s can be split into "LLLLRRRR".


Constraints:

2 <= s.length <= 1000
s[i] is either 'L' or 'R'.
s is a balanced string.

"""

# V0
# IDEA : GREEDY + RUNNING BALANCE
#        keep `cnt` = (#L - #R) so far. Every time it hits 0 we have closed a
#        balanced chunk, and cutting as early as possible is always optimal
#        (a later cut can only merge chunks together, never create more).
# time = O(n)
# space = O(1)
class Solution(object):
    def balancedStringSplit(self, s):
        res = 0
        cnt = 0
        for c in s:
            cnt += 1 if c == 'L' else -1
            if cnt == 0:
                res += 1
        return res
