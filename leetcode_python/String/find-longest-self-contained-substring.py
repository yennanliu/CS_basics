"""

3104. Find Longest Self-Contained Substring
Hard
🔒 (premium)

Given a string s, your task is to find the length of the longest self-contained substring of s.

A substring t of a string s is called self-contained if t != s and for every character in t, it doesn't exist in the rest of s.

Return the length of the longest self-contained substring of s if it exists, otherwise, return -1.


Example 1:

Input: s = "abba"
Output: 2
Explanation:
Let's check the substring "bb". You can see that no other "b" is outside of this substring. Hence the answer is 2.

Example 2:

Input: s = "abab"
Output: -1
Explanation:
Every substring we choose does not satisfy the described property (there is some character which is inside and outside that substring).


Constraints:

2 <= s.length <= 5 * 10^4
s consists only of lowercase English letters.

"""

# V0
# IDEA : THE START MUST BE A FIRST OCCURRENCE — SO THERE ARE AT MOST 26 STARTS
#
#   for t = s[i..j] to be self-contained, EVERY letter inside must have all
#   its occurrences inside :
#       first[c] >= i   and   last[c] <= j   for each letter c in t
#
#   apply that to the letter at position i itself : first[s[i]] >= i, and it
#   obviously occurs at i, so first[s[i]] == i. the start is always a letter's
#   FIRST occurrence — at most 26 candidates.
#
#   from each such start, walk j rightwards carrying
#       mn = min first[c] over the letters seen
#       mx = max last[c]  over the letters seen
#   the moment mn < i the window has swallowed a letter that also lives to
#   the left, and no larger j can recover — stop. whenever mx <= j the window
#   is self-contained, so record its length (excluding the whole string,
#   since t != s).
#
# time = O(26 * n), space = O(1)
class Solution(object):
    def maxSubstringLength(self, s):
        n = len(s)
        first, last = {}, {}
        for i, ch in enumerate(s):
            if ch not in first:
                first[ch] = i
            last[ch] = i

        res = -1
        for start in sorted(set(first[c] for c in first)):
            mn, mx = n, -1
            for j in range(start, n):
                ch = s[j]
                mn = min(mn, first[ch])
                mx = max(mx, last[ch])
                if mn < start:
                    break                     # a letter also lives to the left
                if mx <= j and (j - start + 1) < n:
                    res = max(res, j - start + 1)
        return res
