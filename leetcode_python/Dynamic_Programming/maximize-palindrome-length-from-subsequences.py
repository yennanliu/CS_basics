"""

1771. Maximize Palindrome Length From Subsequences
Hard

You are given two strings, word1 and word2. You want to construct a string in the following manner:

Choose some non-empty subsequence subsequence1 from word1.
Choose some non-empty subsequence subsequence2 from word2.
Concatenate the subsequences: subsequence1 + subsequence2, to make the string.

Return the length of the longest palindrome that can be constructed in the described manner. If no palindromes can be constructed, return 0.

A subsequence of a string s is a string that can be made by deleting some (possibly none) characters from s without changing the order of the remaining characters.

A palindrome is a string that reads the same forward as well as backward.

Example 1:

Input: word1 = "cacb", word2 = "cbba"
Output: 5
Explanation: Choose "ab" from word1 and "cba" from word2 to make "abcba", which is a palindrome.

Example 2:

Input: word1 = "ab", word2 = "ab"
Output: 3
Explanation: Choose "ab" from word1 and "a" from word2 to make "aba", which is a palindrome.

Example 3:

Input: word1 = "aa", word2 = "bb"
Output: 0
Explanation: You cannot construct a palindrome from the described method, so return 0.

Constraints:

1 <= word1.length, word2.length <= 1000
word1 and word2 consist of lowercase English letters.

"""

# V0
# IDEA : LONGEST PALINDROMIC SUBSEQUENCE ON word1 + word2, WITH A CROSSING PAIR
#
#   concatenate s = word1 + word2 and run the classic interval DP
#     f[i][j] = f[i + 1][j - 1] + 2            if s[i] == s[j]
#             = max(f[i + 1][j], f[i][j - 1])  otherwise
#   the extra requirement is that BOTH subsequences are non-empty, i.e. the
#   palindrome must contain at least one index in word1 and one in word2.
#   whenever s[i] == s[j] with i inside word1 and j inside word2, f[i][j] is a
#   valid palindrome that straddles the boundary -> take the max of those.
#   NOTE : if no such matching pair exists the answer is 0.
#
# time = O((n1 + n2)^2), space = O((n1 + n2)^2)
class Solution(object):
    def longestPalindrome(self, word1, word2):
        s = word1 + word2
        n = len(s)
        k = len(word1)

        f = [[0] * n for _ in range(n)]
        for i in range(n):
            f[i][i] = 1

        res = 0
        for i in range(n - 2, -1, -1):
            for j in range(i + 1, n):
                if s[i] == s[j]:
                    f[i][j] = f[i + 1][j - 1] + 2
                    if i < k <= j:
                        res = max(res, f[i][j])
                else:
                    f[i][j] = max(f[i + 1][j], f[i][j - 1])
        return res
