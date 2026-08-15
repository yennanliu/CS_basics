"""

3325. Count Substrings With K-Frequency Characters I
Medium

Given a string s and an integer k, return the total number of substrings of s where at least one character appears at least k times.


Example 1:

Input: s = "abacb", k = 2
Output: 4
Explanation:
The valid substrings are:
"aba" (character 'a' appears 2 times).
"abac" (character 'a' appears 2 times).
"abacb" (character 'a' appears 2 times).
"bacb" (character 'b' appears 2 times).

Example 2:

Input: s = "abcde", k = 1
Output: 15
Explanation:
All substrings are valid because every character appears at least once.


Constraints:

1 <= s.length <= 3000
1 <= k <= s.length
s consists only of lowercase English letters.

"""

# V0
# IDEA : SLIDING WINDOW — "SOME CHARACTER HITS k" ONLY GETS EASIER AS THE WINDOW GROWS
#
#   a substring stays valid when extended, so for each right end there is a
#   threshold : every start at or before some index gives a valid substring,
#   and every later start does not.
#
#   so push `left` forward as long as the window still has a character
#   reaching k, and the count of valid substrings ending here is exactly
#   `left` — the starts 0 .. left-1.
#
#   tracking the running maximum count is enough to test validity in O(1),
#   since only the character just added can create a new maximum.
#
# time = O(n), space = O(1)  (26 counters)
class Solution(object):
    def numberOfSubstrings(self, s, k):
        cnt = [0] * 26
        left = 0
        res = 0
        for ch in s:
            c = ord(ch) - 97
            cnt[c] += 1
            while cnt[c] >= k:
                d = ord(s[left]) - 97
                cnt[d] -= 1
                left += 1
            res += left
        return res
