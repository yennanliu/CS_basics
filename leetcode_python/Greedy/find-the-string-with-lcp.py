"""

2573. Find the String with LCP
Hard

We define the lcp matrix of any 0-indexed string word of n lowercase English letters as an n x n grid such that:

lcp[i][j] is equal to the length of the longest common prefix between the substrings word[i,n-1] and word[j,n-1].

Given an n x n matrix lcp, return the alphabetically smallest string word that corresponds to lcp. If there is no such string, return an empty string.

A string a is lexicographically smaller than a string b (of the same length) if in the first position where a and b differ, string a has a letter that appears earlier in the alphabet than the corresponding letter in b. For example, "aabd" is lexicographically smaller than "aaca" because the first position they differ is at the third letter, and 'b' comes before 'c'.


Example 1:

Input: lcp = [[4,0,2,0],[0,3,0,1],[2,0,2,0],[0,1,0,1]]
Output: "abab"
Explanation: lcp corresponds to any 4 letter string with two alternating letters. The lexicographically smallest of them is "abab".

Example 2:

Input: lcp = [[4,3,2,1],[3,3,2,1],[2,2,2,1],[1,1,1,1]]
Output: "aaaa"
Explanation: lcp corresponds to any 4 letter string with a single distinct letter. The lexicographically smallest of them is "aaaa".

Example 3:

Input: lcp = [[4,3,2,1],[3,3,2,1],[2,2,2,1],[1,1,1,3]]
Output: ""
Explanation: lcp[3][3] cannot be equal to 3 since word[3,...,3] consists of only a single letter; Thus, no answer exists.


Constraints:

1 <= n == lcp.length == lcp[i].length <= 1000
0 <= lcp[i][j] <= n

"""

# V0
# IDEA : GREEDY CONSTRUCTION + FULL O(n^2) DP RE-VERIFICATION
#
#   Two halves, and BOTH are needed - the greedy alone can emit a string whose
#   real lcp matrix differs from the input.
#
#   (1) CONSTRUCT. lcp[i][j] > 0 means word[i] == word[j]. That is an
#       equivalence relation on positions, so the matrix already dictates which
#       positions share a letter - we only get to choose WHICH letter each
#       class receives. To make the string lexicographically smallest, hand out
#       'a', 'b', 'c', ... to the classes in order of their leftmost position:
#       scan for the first still-unassigned index i, give its whole class
#       (every j >= i with lcp[i][j] > 0) the next unused letter, repeat.
#       If any position is left unassigned after 'z' the matrix needs more than
#       26 distinct letters -> impossible.
#
#   NOTE : we only need to look at j >= i. Positions j < i were already
#          assigned when their own (earlier) class was processed.
#
#   (2) VERIFY. The construction only ever consumed the sign of lcp[i][j], not
#       its magnitude, so the numbers themselves are still unchecked - and they
#       can be self-contradictory (Example 3). Recompute the true lcp of the
#       candidate string with the standard suffix DP:
#
#           real[i][j] = real[i+1][j+1] + 1   if s[i] == s[j]
#                      = 0                    otherwise
#
#       and bail out the moment any cell disagrees with the input.
#
#   NOTE : the DP runs i, j DOWNWARD because cell (i,j) depends on (i+1,j+1).
#          Only one previous row is ever needed, so keep two rolling rows of
#          length n+1 instead of the full n x n table - at n = 1000 that is
#          O(n) memory instead of 10^6 cells.
#
#   NOTE : the guard row (index n) is all zeroes, which makes the i == n-1 and
#          j == n-1 boundary give real = 1 with no special-casing.
#
# time = O(n^2), space = O(n)
class Solution(object):
    def findTheString(self, lcp):
        n = len(lcp)
        s = [''] * n

        # ---- (1) greedy letter assignment, smallest letter to leftmost class
        i = 0
        for k in range(26):
            while i < n and s[i]:
                i += 1
            if i == n:
                break
            c = chr(ord('a') + k)
            for j in range(i, n):
                if lcp[i][j] > 0:
                    s[j] = c
        if '' in s:
            # needs more than 26 distinct letters -> no such string
            return ''

        # ---- (2) recompute the real lcp matrix and compare, row by row
        prev = [0] * (n + 1)     # real lcp values for row i+1
        cur = [0] * (n + 1)
        for i in range(n - 1, -1, -1):
            ci = s[i]
            for j in range(n - 1, -1, -1):
                real = prev[j + 1] + 1 if ci == s[j] else 0
                if real != lcp[i][j]:
                    return ''
                cur[j] = real
            prev, cur = cur, prev

        return ''.join(s)
