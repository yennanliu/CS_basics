"""

1400. Construct K Palindrome Strings
Medium

Given a string s and an integer k, return true if you can use all the characters in s to construct non-empty k palindrome strings or false otherwise.


Example 1:

Input: s = "annabelle", k = 2
Output: true
Explanation: You can construct two palindromes using all characters in s.
Some possible constructions "anna" + "elble", "anbna" + "elle", "anellena" + "b"

Example 2:

Input: s = "leetcode", k = 3
Output: false
Explanation: It is impossible to construct 3 palindromes using all the characters of s.

Example 3:

Input: s = "true", k = 4
Output: true
Explanation: The only possible solution is to put each character in a separate string.


Constraints:

1 <= s.length <= 10^5
s consists of lowercase English letters.
1 <= k <= 10^5

"""

# V0
# IDEA : GREEDY / PARITY COUNT (odd-count chars force separate palindromes)
#
#   a palindrome tolerates at most ONE character with odd frequency (its
#   centre), so if `odd` characters occur an odd number of times we need at
#   least `odd` palindromes -> need odd <= k.
#   we also need enough letters to fill k non-empty strings -> len(s) >= k.
#   those two conditions are also sufficient: pairs can be split freely.
#
# time = O(n), space = O(26)
from collections import Counter
class Solution(object):
    def canConstruct(self, s, k):
        if len(s) < k:
            return False
        cnt = Counter(s)
        odd = sum(1 for v in cnt.values() if v % 2 == 1)
        return odd <= k
