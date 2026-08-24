"""

2370. Longest Ideal Subsequence
Medium

You are given a string s consisting of lowercase letters and an integer k. We call a string t ideal if the following conditions are satisfied:

t is a subsequence of the string s.
The absolute difference in the alphabet order of every two adjacent letters in t is less than or equal to k.

Return the length of the longest ideal string.

A subsequence is a string that can be derived from another string by deleting some or no characters without changing the order of the remaining characters.

Note that the alphabet order is not cyclic. For example, the absolute difference in the alphabet order of 'a' and 'z' is 25, not 1.


Example 1:

Input: s = "acfgbd", k = 2
Output: 4
Explanation: The longest ideal string is "acbd". The length of this string is 4, so 4 is returned.
Note that "acfgbd" is not ideal because 'c' and 'f' have a difference of 3 in alphabet order.

Example 2:

Input: s = "abcd", k = 3
Output: 4
Explanation: The longest ideal string is "abcd". The length of this string is 4, so 4 is returned.


Constraints:

1 <= s.length <= 10^5
0 <= k <= 25
s consists of lowercase English letters.

"""

# V0
# IDEA : DP KEYED ON THE LAST LETTER, NOT ON THE INDEX
#
#   best[c] = length of the longest ideal subsequence seen so far that ENDS
#   with the letter c. only 26 states exist, so the whole table fits in a
#   fixed array.
#
#   for each character ch of s, it can extend any subsequence whose last
#   letter lies within k of it :
#       best[ch] = 1 + max(best[c] for c in [ch - k, ch + k])
#
#   scanning that 51-wide window per character keeps it O(26 n) — far better
#   than the O(n^2) index-based LIS formulation.
#
"""

DP def
    keyed on the LAST LETTER, not on the index - only 26 states exist

    best[c]: length of the longest ideal subsequence seen so far

             that ENDS with letter c

DP eq

     for each character ch of s (c = ord(ch) - 'a'):

        best[c] = 1 + max( best[p] for p in [c - k, c + k] )


    -> e.g. that 51-wide window scan per character keeps it O(26 n),
              far better than the O(n^2) index-based LIS formulation

     init: best[.] = 0
     ans = max(best)

"""
# time = O(26 * n), space = O(26)
class Solution(object):
    def longestIdealString(self, s, k):
        best = [0] * 26
        for ch in s:
            c = ord(ch) - ord('a')
            lo, hi = max(0, c - k), min(25, c + k)
            best[c] = 1 + max(best[lo:hi + 1])
        return max(best)
