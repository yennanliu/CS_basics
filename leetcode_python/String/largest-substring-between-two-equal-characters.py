"""

1624. Largest Substring Between Two Equal Characters
Easy

Given a string s, return the length of the longest substring between two equal characters, excluding the two characters. If there is no such substring return -1.

A substring is a contiguous sequence of characters within a string.


Example 1:

Input: s = "aa"
Output: 0
Explanation: The optimal substring here is an empty substring between the two 'a's.

Example 2:

Input: s = "abca"
Output: 2
Explanation: The optimal substring here is "bc".

Example 3:

Input: s = "cbzxy"
Output: -1
Explanation: There are no characters that appear twice in s.


Constraints:

1 <= s.length <= 300
s contains only lowercase English letters.

"""

# V0
# IDEA : HASH TABLE (remember the FIRST index of every character)
#
#   for a fixed character c, the widest gap is between its first and its
#   last occurrence, so we only ever need the earliest index of c. Scan
#   once: if c was seen before at f, candidate = i - f - 1 (the -1 excludes
#   the two endpoints); otherwise record first[c] = i.
#
#   NOTE : start the answer at -1, which is also the "no repeat" result.
#
# time = O(n), space = O(26)
class Solution(object):
    def maxLengthBetweenEqualCharacters(self, s):
        first = {}
        res = -1
        for i in range(len(s)):
            c = s[i]
            if c in first:
                if i - first[c] - 1 > res:
                    res = i - first[c] - 1
            else:
                first[c] = i
        return res
