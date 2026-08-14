"""

1593. Split a String Into the Max Number of Unique Substrings
Medium

Given a string s, return the maximum number of unique substrings that the given string can be split into.

You can split string s into any list of non-empty substrings, where the concatenation of the substrings forms the original string. However, you must split the substrings such that all of them are unique.

A substring is a contiguous sequence of characters within a string.

Example 1:

Input: s = "ababccc"
Output: 5
Explanation: One way to split maximally is ['a', 'b', 'ab', 'c', 'cc']. Splitting like ['a', 'b', 'a', 'b', 'c', 'cc'] is not valid as you have 'a' and 'b' multiple times.

Example 2:

Input: s = "aba"
Output: 2
Explanation: One way to split maximally is ['a', 'ba'].

Example 3:

Input: s = "aa"
Output: 1
Explanation: It is impossible to split the string any further.

Constraints:

1 <= s.length <= 16
s contains only lower case English letters.

"""

# V0
# IDEA : BACKTRACKING (n <= 16, try every cut with a "used pieces" set)
#
#   at position i try every next cut j, and if s[i:j] has not been used
#   yet take it and recurse.
#   NOTE : prune with "current count + remaining characters <= best",
#          since every remaining char can add at most one more piece.
#
# time = O(2^n * n), space = O(n^2)
class Solution(object):
    def maxUniqueSplit(self, s):
        n = len(s)
        used = set()
        self.best = 0

        def dfs(i, cnt):
            if cnt + (n - i) <= self.best:
                return
            if i == n:
                self.best = cnt
                return
            for j in range(i + 1, n + 1):
                piece = s[i:j]
                if piece in used:
                    continue
                used.add(piece)
                dfs(j, cnt + 1)
                used.remove(piece)

        dfs(0, 0)
        return self.best
