"""

1347. Minimum Number of Steps to Make Two Strings Anagram
Medium

You are given two strings of the same length s and t.
In one step you can choose any character of t and replace it with
another character.

Return the minimum number of steps to make t an anagram of s.

An Anagram of a string is a string that contains the same characters with a
different (or the same) ordering.


Example 1:

Input: s = "bab", t = "aba"
Output: 1
Explanation: Replace the first 'a' in t with b, t = "bba" which is anagram of s.

Example 2:

Input: s = "leetcode", t = "practice"
Output: 5
Explanation: Replace 'p', 'r', 'a', 'i' and 'c' from t with proper characters
to make t anagram of s.

Example 3:

Input: s = "anagram", t = "mangaar"
Output: 0
Explanation: "anagram" and "mangaar" are anagrams.


Constraints:

1 <= s.length <= 5 * 10^4
s.length == t.length
s and t consist of lowercase English letters only.

"""

# V0
# IDEA: COUNTER (count the surplus characters in t)
#
#  s and t have the SAME length, so:
#    every char that t has too many of must be replaced by exactly one
#    char that t is missing -> answer = total surplus in t.
#
# time = O(n)
# space = O(1), at most 26 keys
from collections import Counter
class Solution(object):
    def minSteps(self, s, t):
        cnt = Counter(s)

        res = 0
        for c in t:
            cnt[c] -= 1
            # this c is NOT needed by s -> must be replaced
            if cnt[c] < 0:
                res += 1
        return res
