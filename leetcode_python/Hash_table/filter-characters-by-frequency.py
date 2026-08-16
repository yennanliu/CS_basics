"""

3662. Filter Characters by Frequency
Easy

You are given a string s consisting of lowercase English letters and an
integer k.

Your task is to construct a new string that contains only those characters
from s which appear fewer than k times in the entire string. The order of
characters in the new string must be the same as their order in s.

Return the resulting string. If no characters qualify, return an empty
string.

Note: Every occurrence of a character that occurs fewer than k times is
kept.


Example 1:

Input: s = "aadbbcccca", k = 3
Output: "dbb"
Explanation:
Character frequencies in s:
'a' appears 3 times
'd' appears 1 time
'b' appears 2 times
'c' appears 4 times
Only 'd' and 'b' appear fewer than 3 times. Preserving their order, the
result is "dbb".

Example 2:

Input: s = "xyz", k = 2
Output: "xyz"
Explanation:
All characters ('x', 'y', 'z') appear exactly once, which is fewer than 2.
Thus the whole string is returned.


Constraints:

1 <= s.length <= 100
s consists of lowercase English letters.
1 <= k <= s.length

"""

# V0
# IDEA : COUNT THEN FILTER (TWO PASSES)
#
#   the keep/drop decision for a character depends on its GLOBAL frequency,
#   which is not knowable while streaming the string left to right. so the
#   only way to answer it in one scan would be to buffer everything anyway.
#
#   two passes is therefore the natural shape: pass 1 builds the frequency
#   table, pass 2 walks s again and emits every position whose character
#   passed the test. walking s (not the frequency table) is what preserves
#   the original order and what keeps *every* occurrence of a kept
#   character, as the note demands.
#
# time = O(n), space = O(1) -- the counter holds at most 26 keys
class Solution(object):
    def filterCharacters(self, s, k):
        cnt = {}
        for c in s:
            cnt[c] = cnt.get(c, 0) + 1
        return "".join(c for c in s if cnt[c] < k)
