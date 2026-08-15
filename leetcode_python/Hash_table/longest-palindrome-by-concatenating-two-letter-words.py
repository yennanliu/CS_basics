"""

2131. Longest Palindrome by Concatenating Two Letter Words
Medium

You are given an array of strings words. Each element of words consists of two lowercase English letters.

Create the longest possible palindrome by selecting some elements from words and concatenating them in any order. Each element can be selected at most once.

Return the length of the longest palindrome that you can create. If it is impossible to create any palindrome, return 0.

A palindrome is a string that reads the same forward and backward.


Example 1:

Input: words = ["lc","cl","gg"]
Output: 6
Explanation: One longest palindrome is "lc" + "gg" + "cl" = "lcggcl", of length 6.
Note that "clgglc" is another longest palindrome that can be created.

Example 2:

Input: words = ["ab","ty","yt","lc","cl","ab"]
Output: 8
Explanation: One longest palindrome is "ty" + "lc" + "cl" + "yt" = "tylcclyt", of length 8.
Note that "lcyttycl" is another longest palindrome that can be created.

Example 3:

Input: words = ["cc","ll","xx"]
Output: 2
Explanation: One longest palindrome is "cc", of length 2.
Note that "ll" is another longest palindrome that can be created, and so is "xx".


Constraints:

1 <= words.length <= 10^5
words[i].length == 2
words[i] consists of lowercase English letters.

"""

# V0
# IDEA : PAIR EACH WORD WITH ITS REVERSE; ONE DOUBLE LETTER MAY SIT IN THE MIDDLE
#
#   a palindrome built from 2-letter blocks mirrors block by block : a block
#   "xy" on the left must be answered by "yx" on the right. so
#
#     - for x != y : the number of usable pairs is min(cnt["xy"], cnt["yx"]),
#       and each pair contributes 4 characters. count each unordered pair
#       once (guard with x < y).
#
#     - for x == y ("gg") : two copies mirror each other (4 chars each pair),
#       and ONE leftover copy may sit in the exact middle — but only one such
#       leftover in the whole answer, worth 2 more characters.
#
# time = O(n), space = O(1)  (at most 26*26 keys)
from collections import Counter


class Solution(object):
    def longestPalindrome(self, words):
        cnt = Counter(words)
        res = 0
        center = False
        for w, c in cnt.items():
            if w[0] == w[1]:
                res += (c // 2) * 4
                if c % 2:
                    center = True
            elif w[0] < w[1]:
                res += min(c, cnt[w[::-1]]) * 4
        return res + (2 if center else 0)
