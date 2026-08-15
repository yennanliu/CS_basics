"""

3084. Count Substrings Starting and Ending with Given Character
Medium

You are given a string s and a character c. Return the total number of substrings of s that start and end with c.


Example 1:

Input: s = "abada", c = "a"
Output: 6
Explanation: Substrings starting and ending with "a" are: "abada", "abada", "abada", "abada", "abada", "abada".

Example 2:

Input: s = "zzz", c = "z"
Output: 6
Explanation: There are a total of 6 substrings in s and all start and end with "z".


Constraints:

1 <= s.length <= 10^5
s and c consist only of lowercase English letters.

"""

# V0
# IDEA : PICK AN ORDERED PAIR OF c-POSITIONS (THE SAME ONE TWICE IS ALLOWED)
#
#   a qualifying substring is fully determined by which occurrence of c it
#   starts at and which it ends at, with start <= end. with m occurrences
#   that is
#
#       m choose 2  +  m   =   m * (m + 1) / 2
#
#   the "+ m" being the single-character substrings.
#
# time = O(n), space = O(1)
class Solution(object):
    def countSubstrings(self, s, c):
        m = s.count(c)
        return m * (m + 1) // 2
