"""

2730. Find the Longest Semi-Repetitive Substring
Medium

You are given a digit string s that consists of digits from 0 to 9.

A string is called semi-repetitive if there is at most one adjacent pair of the same digit. For example, "0010", "002020", "0123", "2002", and "54944" are semi-repetitive while the following are not: "00101022" (adjacent same digit pairs are 00 and 22), and "1101234883" (adjacent same digit pairs are 11 and 88).

Return the length of the longest semi-repetitive substring of s.


Example 1:

Input: s = "52233"
Output: 4
Explanation:
The longest semi-repetitive substring is "5223". Picking the whole string "52233" has two adjacent same digit pairs 22 and 33, but at most one is allowed.

Example 2:

Input: s = "5494"
Output: 4
Explanation:
s is a semi-repetitive string.

Example 3:

Input: s = "1111111"
Output: 2
Explanation:
The longest semi-repetitive substring is "11". Picking the substring "111" has two adjacent same digit pairs, but at most one is allowed.


Constraints:

1 <= s.length <= 50
'0' <= s[i] <= '9'

"""

# V0
# IDEA : TWO POINTERS / SLIDING WINDOW ON ADJACENT-EQUAL PAIRS
#
#   the property "at most one adjacent equal pair" is monotone: shrinking a
#   window can only remove pairs. So a plain sliding window works.
#
#   count `cnt` = number of indices p in [l+1, r] with s[p] == s[p-1], i.e.
#   the pairs that live entirely inside the window s[l..r].
#
#   NOTE : entering r adds the pair (r-1, r); advancing l from l to l+1 drops
#          the pair (l, l+1) — the pair is indexed by its RIGHT end, so the
#          add/remove tests are s[r]==s[r-1] and s[l]==s[l+1] respectively.
#   NOTE : answer starts at 1 — a single char is always semi-repetitive, and
#          for n == 1 the loop body never runs.
#
# time = O(n), space = O(1)
class Solution(object):
    def longestSemiRepetitiveSubstring(self, s):
        n = len(s)
        res = 1
        l = 0
        cnt = 0
        for r in range(1, n):
            if s[r] == s[r - 1]:
                cnt += 1
            while cnt > 1:
                if s[l] == s[l + 1]:
                    cnt -= 1
                l += 1
            res = max(res, r - l + 1)
        return res
