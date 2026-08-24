"""

1960. Maximum Product of the Length of Two Palindromic Substrings
Hard

You are given a 0-indexed string s and are tasked with finding two non-intersecting palindromic substrings of odd length such that the product of their lengths is maximized.

More formally, you want to choose four integers i, j, k, l such that 0 <= i <= j < k <= l < s.length and both the substrings s[i...j] and s[k...l] are palindromes and have odd lengths. s[i...j] denotes a substring from index i to index j inclusive.

Return the maximum possible product of the lengths of the two non-intersecting palindromic substrings.

A palindrome is a string that is the same forward and backward. A substring is a contiguous sequence of characters in a string.


Example 1:

Input: s = "ababbb"
Output: 9
Explanation: Substrings "aba" and "bbb" are palindromes with odd length. product = 3 * 3 = 9.

Example 2:

Input: s = "zaaaxbbby"
Output: 9
Explanation: Substrings "aaa" and "bbb" are palindromes with odd length. product = 3 * 3 = 9.


Constraints:

2 <= s.length <= 10^5
s consists of lowercase English letters.

"""

# V0
# IDEA : MANACHER + PREFIX / SUFFIX BEST PALINDROME
#
#   1) Manacher (odd centers only) -> rad[i] = radius of the longest odd
#      palindrome centered at i, so its length is 2*rad[i]+1 and it spans
#      [i-rad[i], i+rad[i]].
#
#   2) left[j]  = longest odd palindrome fully inside s[0..j]
#      right[j] = longest odd palindrome fully inside s[j..n-1]
#      seed left[i+rad[i]] and right[i-rad[i]] with 2*rad[i]+1, then relax:
#        - a palindrome ending at j (len L) shrinks to one ending at j-1 (L-2)
#        - a palindrome starting at j (len L) shrinks to one starting at j+1 (L-2)
#      finally take a running max in each direction.
#
#   3) split the string at every cut: answer = max(left[i-1] * right[i]).
#
#   NOTE : the shrink pass MUST run before the running-max pass, otherwise a
#          long palindrome never propagates to the shorter windows.
#
"""

DP def
    rad[i]  : (MANACHER, odd centres) radius of the longest odd palindrome
              centred at i -> length 2*rad[i]+1, spanning [i-rad[i], i+rad[i]]

    left[j] : length of the longest odd palindrome fully inside s[0..j]

    right[j]: length of the longest odd palindrome fully inside s[j..n-1]

DP eq

     seed:  left[ i + rad[i] ]  = 2*rad[i] + 1
            right[ i - rad[i] ] = 2*rad[i] + 1

     SHRINK pass:
        a palindrome ENDING   at j of length L shrinks to one ending at j-1 (L-2)
        a palindrome STARTING at j of length L shrinks to one starting at j+1 (L-2)

     then a RUNNING MAX in each direction


    -> e.g. NOTE !!! the shrink pass MUST run BEFORE the running-max pass,
              otherwise a long palindrome never propagates to the shorter
              windows

     ans = max over cuts i of ( left[i-1] * right[i] )

"""
# time = O(n), space = O(n)
class Solution(object):
    def maxProduct(self, s):
        n = len(s)

        # --- Manacher on odd centers ---
        rad = [0] * n
        center = 0
        right = 0
        for i in range(n):
            if i < right:
                rad[i] = min(right - i, rad[2 * center - i])
            while (
                i - 1 - rad[i] >= 0
                and i + 1 + rad[i] < n
                and s[i - 1 - rad[i]] == s[i + 1 + rad[i]]
            ):
                rad[i] += 1
            if i + rad[i] > right:
                center = i
                right = i + rad[i]

        # --- best palindrome ending at / starting at each index ---
        pre = [0] * n   # longest odd palindrome ENDING exactly at i (after relax)
        suf = [0] * n   # longest odd palindrome STARTING exactly at i (after relax)
        for i in range(n):
            L = 2 * rad[i] + 1
            pre[i + rad[i]] = max(pre[i + rad[i]], L)
            suf[i - rad[i]] = max(suf[i - rad[i]], L)

        for i in range(n - 2, -1, -1):
            pre[i] = max(pre[i], pre[i + 1] - 2)
        for i in range(1, n):
            suf[i] = max(suf[i], suf[i - 1] - 2)

        # running max -> "fully inside the prefix / suffix"
        for i in range(1, n):
            pre[i] = max(pre[i], pre[i - 1])
        for i in range(n - 2, -1, -1):
            suf[i] = max(suf[i], suf[i + 1])

        res = 0
        for i in range(1, n):
            res = max(res, pre[i - 1] * suf[i])
        return res
