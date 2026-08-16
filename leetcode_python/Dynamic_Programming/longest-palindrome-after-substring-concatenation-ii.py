"""

3504. Longest Palindrome After Substring Concatenation II
Hard

You are given two strings, s and t.

You can create a new string by selecting a substring from s (possibly empty) and
a substring from t (possibly empty), then concatenating them in order.

Return the length of the longest palindrome that can be formed this way.

Example 1:

Input: s = "a", t = "a"

Output: 2

Explanation:

Concatenating "a" from s and "a" from t results in "aa", which is a palindrome
of length 2.

Example 2:

Input: s = "abc", t = "def"

Output: 1

Explanation:

Since all characters are different, the longest palindrome is any single
character, so the answer is 1.

Example 3:

Input: s = "b", t = "aaaa"

Output: 4

Explanation:

Selecting "aaaa" from t is the longest palindrome, so the answer is 4.

Example 4:

Input: s = "abcde", t = "ecdba"

Output: 5

Explanation:

Concatenating "abc" from s and "ba" from t results in "abcba", which is a
palindrome of length 5.

Constraints:

1 <= s.length, t.length <= 1000

s and t consist of lowercase English letters.

"""

# V0
# IDEA : PEEL MATCHED OUTER PAIRS UNTIL ONE SIDE RUNS OUT
#
#   fix the left end i in s and the right end j in t.  the concatenation
#   s[i..p] + t[q..j] is a palindrome iff either
#     * s[i] == t[j] and the inner part s[i+1..p] + t[q..j-1] is a palindrome, or
#     * one of the two sides is already empty, in which case the other side must
#       itself be a palindrome.
#
#   that gives f(i, j) = max( bestS[i], bestT[j],
#                             2 + f(i+1, j-1) when s[i] == t[j] )
#   where bestS[i] is the longest palindromic substring of s *starting* at i and
#   bestT[j] the longest one *ending* at j.  the recursion walks the diagonal
#   i+1 / j-1, so all O(n*m) states are filled in one sweep.
#
#   bestS / bestT come from the usual O(n^2) "is s[i..j] a palindrome" table.
#
# time = O(n^2 + m^2 + n*m), space = O(n^2 + m^2)
class Solution(object):
    def longestPalindrome(self, s, t):
        n, m = len(s), len(t)

        def longest_starting(x):
            k = len(x)
            best = [0] * k
            # pal[i][j] via the standard expand-by-length recurrence, kept as
            # rolling rows to stay O(k^2) time with O(k^2) bits of storage
            pal = [[False] * k for _ in range(k)]
            for i in range(k - 1, -1, -1):
                for j in range(i, k):
                    if x[i] == x[j] and (j - i < 2 or pal[i + 1][j - 1]):
                        pal[i][j] = True
                        if j - i + 1 > best[i]:
                            best[i] = j - i + 1
            return best

        bestS = longest_starting(s)
        # longest palindrome ending at j in t  ==  longest starting at (m-1-j)
        # in reversed(t)
        rev_best = longest_starting(t[::-1])
        bestT = [rev_best[m - 1 - j] for j in range(m)]

        # f[i][jj] with jj = j + 1 so that jj == 0 means "t side already empty"
        prev = [0] * (m + 1)          # row i + 1
        # row n : s side empty -> only a palindrome inside t is possible
        for jj in range(1, m + 1):
            prev[jj] = bestT[jj - 1]
        ans = max(prev)
        for i in range(n - 1, -1, -1):
            cur = [0] * (m + 1)
            cur[0] = bestS[i]
            si = s[i]
            for jj in range(1, m + 1):
                v = bestS[i]
                bt = bestT[jj - 1]
                if bt > v:
                    v = bt
                if si == t[jj - 1]:
                    w = 2 + prev[jj - 1]
                    if w > v:
                        v = w
                cur[jj] = v
            row_max = max(cur)
            if row_max > ans:
                ans = row_max
            prev = cur
        return ans
