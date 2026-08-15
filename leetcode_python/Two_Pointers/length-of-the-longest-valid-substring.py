"""

2781. Length of the Longest Valid Substring
Hard

You are given a string word and an array of strings forbidden.

A string is called valid if none of its substrings are present in forbidden.

Return the length of the longest valid substring of the string word.

A substring is a contiguous sequence of characters in a string, possibly empty.


Example 1:

Input: word = "cbaaaabc", forbidden = ["aaa","cb"]
Output: 4
Explanation: There are 11 valid substrings in word: "c", "b", "a", "ba", "aa", "bc", "baa", "aab", "ab", "abc" and "aabc". The length of the longest valid substring is 4.
It can be shown that all other substrings contain either "aaa" or "cb" as a substring.

Example 2:

Input: word = "leetcode", forbidden = ["de","le","e"]
Output: 4
Explanation: There are 11 valid substrings in word: "l", "t", "c", "o", "d", "tc", "co", "od", "tco", "cod", and "tcod". The length of the longest valid substring is 4.
It can be shown that all other substrings contain either "de", "le", or "e" as a substring.


Constraints:

1 <= word.length <= 10^5
word consists only of lowercase English letters.
1 <= forbidden.length <= 10^5
1 <= forbidden[i].length <= 10
forbidden[i] consists only of lowercase English letters.

"""

# V0
# IDEA : SLIDING WINDOW + BOUNDED SUFFIX LOOKUP
#
#   keep a window [left, right] that contains no forbidden substring.
#   when the window grows by word[right], the ONLY new substrings are the
#   suffixes ending at `right`, so we just have to test those.
#
#   NOTE : forbidden[i] has length <= 10, so a bad suffix can be at most 10
#          chars long -> checking 10 suffixes per right index is enough,
#          which keeps the whole scan linear.
#
#   NOTE : if suffix word[k..right] is forbidden, every window starting at
#          index <= k is dead, so left must jump to k + 1. We probe the
#          suffixes from the longest end toward `right`; the FIRST hit found
#          while walking k downward from right gives the largest k, i.e. the
#          smallest legal left, so we can break right there.
#
#   NOTE : left never moves backwards, hence the `k > left - 1` guard.
#
# time = O(n * L^2) with L = 10 (substring slicing), space = O(total forbidden chars)
class Solution(object):
    def longestValidSubstring(self, word, forbidden):
        bad = set(forbidden)
        max_len = max(len(w) for w in bad)
        res = 0
        left = 0
        for right in range(len(word)):
            # only suffixes of length 1 .. max_len can be forbidden
            low = max(left, right - max_len + 1)
            k = right
            while k >= low:
                if word[k:right + 1] in bad:
                    left = k + 1
                    break
                k -= 1
            res = max(res, right - left + 1)
        return res
