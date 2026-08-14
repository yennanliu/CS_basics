"""

1930. Unique Length-3 Palindromic Subsequences
Medium

Given a string s, return the number of unique palindromes of length three that are a subsequence of s.

Note that even if there are multiple ways to obtain the same subsequence, it is still only counted once.

A palindrome is a string that reads the same forwards and backwards.

A subsequence of a string is a new string generated from the original string with some characters (can be none) deleted without changing the relative order of the remaining characters.

For example, "ace" is a subsequence of "abcde".


Example 1:

Input: s = "aabca"
Output: 3
Explanation: The 3 palindromic subsequences of length 3 are:
- "aba" (subsequence of "aabca")
- "aaa" (subsequence of "aabca")
- "aca" (subsequence of "aabca")

Example 2:

Input: s = "adc"
Output: 0
Explanation: There are no palindromic subsequences of length 3 in "adc".

Example 3:

Input: s = "bbcbaba"
Output: 4
Explanation: The 4 palindromic subsequences of length 3 are:
- "bbb" (subsequence of "bbcbaba")
- "bcb" (subsequence of "bbcbaba")
- "bab" (subsequence of "bbcbaba")
- "aba" (subsequence of "bbcbaba")


Constraints:

3 <= s.length <= 10^5
s consists of only lowercase English letters.

"""

# V0
# IDEA : FIX THE OUTER CHARACTER (26 passes) + COUNT DISTINCT MIDDLES
#
#   a length-3 palindrome is "c ? c". for a given outer letter c the widest
#   possible pair of c's is (first occurrence l, last occurrence r) -- using the
#   widest pair can only ADD candidates for the middle, never remove any.
#
#   so the number of DISTINCT palindromes with outer letter c is exactly the
#   number of distinct characters strictly between l and r.
#
#   NOTE : if c never occurs, or occurs only adjacent / once, then r - l <= 1
#          and it contributes nothing.
#
# time = O(26 * n), space = O(26)
from string import ascii_lowercase
class Solution(object):
    def countPalindromicSubsequence(self, s):
        res = 0
        for c in ascii_lowercase:
            l, r = s.find(c), s.rfind(c)
            if r - l > 1:
                res += len(set(s[l + 1:r]))
        return res
