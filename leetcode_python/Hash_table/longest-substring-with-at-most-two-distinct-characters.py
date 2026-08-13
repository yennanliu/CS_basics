"""

159. Longest Substring with At Most Two Distinct Characters
Medium
(premium)

Given a string s, return the length of the longest substring that contains
at most two distinct characters.


Example 1:

Input: s = "eceba"
Output: 3
Explanation: The substring is "ece" which its length is 3.

Example 2:

Input: s = "ccaabbb"
Output: 5
Explanation: The substring is "aabbb" which its length is 5.


Constraints:

1 <= s.length <= 10^5
s consists of English letters.

"""

# V0
# IDEA : SLIDING WINDOW + HASH TABLE (char -> count inside window)
#        grow the right edge, shrink the left edge while > 2 distinct chars
# time = O(n)
# space = O(1)   # the counter holds at most 3 keys
from collections import defaultdict
class Solution(object):
    def lengthOfLongestSubstringTwoDistinct(self, s):
        cnt = defaultdict(int)
        res = 0
        left = 0

        for right, c in enumerate(s):
            cnt[c] += 1

            # too many distinct chars -> shrink from the left
            while len(cnt) > 2:
                cnt[s[left]] -= 1
                if cnt[s[left]] == 0:
                    # NOTE: must DELETE the key, otherwise len(cnt) is wrong
                    del cnt[s[left]]
                left += 1

            res = max(res, right - left + 1)

        return res
