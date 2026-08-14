"""

2405. Optimal Partition of String
Medium

Given a string s, partition the string into one or more substrings such that the characters in each substring are unique. That is, no letter appears in a single substring more than once.

Return the minimum number of substrings in such a partition.

Note that each character should belong to exactly one substring in a partition.


Example 1:

Input: s = "abacaba"
Output: 4
Explanation:
Two possible partitions are ("a","ba","cab","a") and ("ab","a","ca","ba").
It can be shown that 4 is the minimum number of substrings needed.

Example 2:

Input: s = "ssssss"
Output: 6
Explanation:
The only valid partition is ("s","s","s","s","s","s").


Constraints:

1 <= s.length <= 10^5
s consists of only English lowercase letters.

"""

# V0
# IDEA : EXTEND THE CURRENT PIECE AS FAR AS POSSIBLE, CUT ON THE FIRST REPEAT
#
#   keep the set of letters in the piece being built. when the next character
#   is already in it, the piece MUST end here — no partition could have made
#   this piece longer — so start a fresh one containing just that character.
#
#   that greedy is optimal by the usual prefix argument: the first cut is
#   forced no later than this point, and cutting later is impossible.
#
# time = O(n), space = O(26)
class Solution(object):
    def partitionString(self, s):
        res = 1
        seen = set()
        for c in s:
            if c in seen:
                res += 1
                seen = {c}
            else:
                seen.add(c)
        return res
