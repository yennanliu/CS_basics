"""

3517. Smallest Palindromic Rearrangement I
Medium

You are given a palindromic string s.

Return the lexicographically smallest palindromic permutation of s.

Example 1:

Input: s = "z"

Output: "z"

Explanation:

A string of only one character is already the lexicographically smallest
palindrome.

Example 2:

Input: s = "babab"

Output: "abbba"

Explanation:

Rearranging "babab" → "abbba" gives the smallest lexicographic palindrome.

Example 3:

Input: s = "daccad"

Output: "acddca"

Explanation:

Rearranging "daccad" → "acddca" gives the smallest lexicographic palindrome.

Constraints:

1 <= s.length <= 10^5

s consists of lowercase English letters.

s is guaranteed to be palindromic.

"""

# V0
# IDEA : A PALINDROME IS DETERMINED BY ITS FIRST HALF
#
#   s is already a palindrome, so at most one letter has an odd count and it is
#   forced into the middle.  every other letter contributes count/2 copies to
#   the left half, and the right half is the mirror of the left.
#
#   the whole string is minimised by minimising that left half, and the smallest
#   arrangement of a multiset is simply its sorted order.
#
# time = O(n + 26 log 26), space = O(n)
from collections import Counter


class Solution(object):
    def smallestPalindrome(self, s):
        cnt = Counter(s)
        half = []
        middle = ""
        for ch in sorted(cnt):
            c = cnt[ch]
            half.append(ch * (c // 2))
            if c % 2:
                middle = ch
        left = "".join(half)
        return left + middle + left[::-1]
