"""

2486. Append Characters to String to Make Subsequence
Medium

You are given two strings s and t consisting of only lowercase English letters.

Return the minimum number of characters that need to be appended to the end of s so that t becomes a subsequence of s.

A subsequence is a string that can be derived from another string by deleting some or no characters without changing the order of the remaining characters.


Example 1:

Input: s = "coaching", t = "coding"
Output: 4
Explanation: Append the characters "ding" to the end of s so that s = "coachingding".
Now, t is a subsequence of s ("coachingding").
It can be shown that appending any 3 characters to the end of s will never make t a subsequence.

Example 2:

Input: s = "abcde", t = "a"
Output: 0
Explanation: t is already a subsequence of s ("abcde").

Example 3:

Input: s = "z", t = "abcde"
Output: 5
Explanation: Append the characters "abcde" to the end of s so that s = "zabcde".
Now, t is a subsequence of s ("zabcde").
It can be shown that appending any 4 characters to the end of s will never make t a subsequence.


Constraints:

1 <= s.length, t.length <= 10^5
s and t consist only of lowercase English letters.

"""

# V0
# IDEA : GREEDY TWO-POINTER MATCH — WHATEVER OF t IS LEFT MUST BE APPENDED
#
#   characters can only be added at the END, so the prefix of t that s
#   already contains as a subsequence must be matched as EARLY as possible.
#   the standard greedy does that : walk s once, advancing the t pointer on
#   every match.
#
#   whatever remains of t after the walk cannot be found inside s and has to
#   be appended verbatim, so the answer is len(t) - matched.
#
# time = O(len(s) + len(t)), space = O(1)
class Solution(object):
    def appendCharacters(self, s, t):
        j = 0
        for c in s:
            if j < len(t) and c == t[j]:
                j += 1
        return len(t) - j
