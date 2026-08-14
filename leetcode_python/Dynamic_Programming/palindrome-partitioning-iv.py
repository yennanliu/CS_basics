"""

1745. Palindrome Partitioning IV
Hard

Given a string s, return true if it is possible to split the string s into three non-empty palindromic substrings. Otherwise, return false.

A string is said to be palindrome if it the same string when reversed.


Example 1:

Input: s = "abcbdd"
Output: true
Explanation: "abcbdd" = "a" + "bcb" + "dd", and all three substrings are palindromes.

Example 2:

Input: s = "bcbddxy"
Output: false
Explanation: s cannot be split into 3 palindromes.


Constraints:

3 <= s.length <= 2000
s consists only of lowercase English letters.

"""

# V0
# IDEA : INTERVAL DP PALINDROME TABLE + ENUMERATE THE TWO CUTS
#
#   step 1 - build pal[i][j] = "s[i..j] is a palindrome":
#
#       pal[i][j] = (s[i] == s[j]) and (j - i < 2 or pal[i+1][j-1])
#
#   the recurrence reads pal[i+1][j-1], a SHORTER interval one row below,
#   so sweep i from n-1 down to 0 and j upward - then the value we need is
#   always already filled in. length 0 and 1 intervals are palindromes by
#   definition, which is why the table starts all-True.
#
#   step 2 - a 3-way split is fixed by two cut points i < j:
#       s[0..i], s[i+1..j], s[j+1..n-1]
#   test all pairs, but skip the whole j-loop as soon as s[0..i] is not a
#   palindrome - that prunes most of the O(n^2) pairs in practice.
#
# time = O(n^2), space = O(n^2)
class Solution(object):
    def checkPartitioning(self, s):
        n = len(s)

        pal = [[True] * n for _ in range(n)]
        for i in range(n - 1, -1, -1):
            for j in range(i + 1, n):
                pal[i][j] = s[i] == s[j] and (j - i < 2 or pal[i + 1][j - 1])

        for i in range(n - 2):
            if not pal[0][i]:
                continue
            for j in range(i + 1, n - 1):
                if pal[i + 1][j] and pal[j + 1][n - 1]:
                    return True

        return False
