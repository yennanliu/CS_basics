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
#   count every word. then :
#     - for a word "xy" with x != y, each copy pairs with a copy of "yx",
#       contributing 4 characters per pair. counting each unordered pair once
#       (only when x < y) avoids double counting.
#     - for a double letter "xx", every TWO copies form a pair (4 chars). one
#       leftover copy may go in the exact middle of the palindrome — but only
#       ONE such centre exists across the whole answer.
#
# time = O(n), space = O(n)
from collections import Counter


class Solution(object):
    def longestPalindrome(self, words):
        cnt = Counter(words)
        res = 0
        has_center = False
        for w, c in cnt.items():
            if w[0] == w[1]:
                res += (c // 2) * 4
                if c % 2:
                    has_center = True
            elif w[0] < w[1]:
                res += min(c, cnt[w[1] + w[0]]) * 4
        if has_center:
            res += 2
        return res
