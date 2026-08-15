"""

3083. Existence of a Substring in a String and Its Reverse
Easy

Given a string s, find any substring of length 2 which is also present in the reverse of s.

Return true if such a substring exists, and false otherwise.


Example 1:

Input: s = "leetcode"
Output: true
Explanation: Substring "ee" is of length 2 which is also present in reverse(s) == "edocteel".

Example 2:

Input: s = "abcba"
Output: true
Explanation: All of the substrings of length 2 "ab", "bc", "cb", "ba" are also present in reverse(s) == "abcba".

Example 3:

Input: s = "abcd"
Output: false
Explanation: There is no substring of length 2 in s, which is also present in the reverse of s.


Constraints:

1 <= s.length <= 100
s consists only of lowercase English letters.

"""

# V0
# IDEA : A LENGTH-2 SUBSTRING IS IN reverse(s) IFF ITS REVERSE IS IN s
#
#   "xy" appears in reverse(s) exactly when "yx" appears in s, so the whole
#   question collapses to : does s contain some pair and also that pair
#   flipped ?
#
#   collect every adjacent pair into a set and look for any whose reverse is
#   also there — one pass, no string reversal needed at all.
#
# time = O(n), space = O(n)
class Solution(object):
    def isSubstringPresent(self, s):
        pairs = set(s[i:i + 2] for i in range(len(s) - 1))
        return any(p[::-1] in pairs for p in pairs)
