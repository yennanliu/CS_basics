"""

2060. Check if an Original String Exists Given Two Encoded Strings
Hard

An original string, consisting of lowercase English letters, can be encoded by the following steps:

Arbitrarily split it into a sequence of some number of non-empty substrings.
Arbitrarily choose some elements (possibly none) of the sequence, and replace each with its length (as a numeric string).
Concatenate the sequence as the encoded string.

For example, one way to encode an original string "abcdefghijklmnop" might be:

Split it as a sequence: ["ab", "cdefghijklmn", "o", "p"].
Choose the second and third elements to be replaced by their lengths, respectively. The sequence becomes ["ab", "12", "1", "p"].
Concatenate the elements of the sequence to get the encoded string: "ab121p".

Given two encoded strings s1 and s2, consisting of lowercase English letters and digits 1-9 (inclusive), return true if there exists an original string that could be encoded as both s1 and s2. Otherwise, return false.

Note: The test cases are generated such that the number of consecutive digits in s1 and s2 does not exceed 3.


Example 1:

Input: s1 = "internationalization", s2 = "i18n"
Output: true
Explanation: It is possible that "internationalization" was the original string.
- "internationalization"
  -> Split:       ["internationalization"]
  -> Do not replace any element
  -> Concatenate:  "internationalization", which is s1.
- "internationalization"
  -> Split:       ["i", "nternationalizatio", "n"]
  -> Replace:     ["i", "18",                 "n"]
  -> Concatenate:  "i18n", which is s2

Example 2:

Input: s1 = "l123e", s2 = "44"
Output: true
Explanation: It is possible that "leetcode" was the original string.
- "leetcode"
  -> Split:      ["l", "e", "et", "cod", "e"]
  -> Replace:    ["l", "1", "2",  "3",   "e"]
  -> Concatenate: "l123e", which is s1.
- "leetcode"
  -> Split:      ["leet", "code"]
  -> Replace:    ["4",    "4"]
  -> Concatenate: "44", which is s2.

Example 3:

Input: s1 = "a5b", s2 = "c5b"
Output: false
Explanation: It is impossible.
- The original string encoded as s1 must start with the letter 'a'.
- The original string encoded as s2 must start with the letter 'c'.


Constraints:

1 <= s1.length, s2.length <= 40
s1 and s2 consist of digits 1-9 (inclusive), and lowercase English letters only.
The number of consecutive digits in s1 and s2 does not exceed 3.

"""

# V0
# IDEA : MEMOISED DFS ON (i, j, diff) - "wildcard budget" matching
#
#   state = (i, j, diff) where diff = (original chars already produced by
#   s1[:i]) - (original chars already produced by s2[:j]).
#
#     diff > 0 -> s1 ran ahead using a NUMBER (a run of wildcards), so the
#                 next literal letter of s2 is swallowed by it: diff -= 1.
#     diff < 0 -> symmetric, s1 gives up one literal letter: diff += 1.
#     diff = 0 -> both sides must literally agree: s1[i] == s2[j].
#
#   when s1[i] starts a digit run we must consume the whole run; a run like
#   "123" may be cut into several numbers ("1"+"23", "12"+"3", ...), so
#   splits() returns every achievable SUM (<= 3 digits, so it is tiny).
#
#   NOTE : diff stays bounded (a number is < 1000 and each literal step
#          shrinks |diff| by 1), so the state space is small.
#
"""

DP def
    dp(i, j, diff): can s1[i:] and s2[j:] still be made equal, given that

                    diff = (# original chars already produced by s1[:i])
                         - (# original chars already produced by s2[:j])

                    -> diff is the "wildcard budget" one side owes the other

DP eq

     if s1[i] starts a digit run (ends at k):
         dp(i, j, diff) = OR over x in splits(s1[i:k]) of dp(k, j, diff + x)

     if s2[j] starts a digit run (ends at k):
         dp(i, j, diff) = OR over x in splits(s2[j:k]) of dp(i, k, diff - x)

     if diff == 0: dp = (s1[i] == s2[j]) and dp(i+1, j+1, 0)   # literal match

     if diff  > 0: dp = dp(i, j+1, diff - 1)   # s1's number eats s2's letter

     if diff  < 0: dp = dp(i+1, j, diff + 1)   # s2's number eats s1's letter


    -> e.g. splits("123") = every achievable SUM of a split
              ("1"+"23" -> 24, "12"+"3" -> 15, "123" -> 123, ...)

     base: i == n and j == m  ->  diff == 0

"""
# time = O(n * m * D) states with D = O(1000), space = same
from functools import lru_cache
class Solution(object):
    def possiblyEquals(self, s1, s2):
        n, m = len(s1), len(s2)

        def splits(t):
            res = set([int(t)])
            for i in range(1, len(t)):
                for a in splits(t[:i]):
                    for b in splits(t[i:]):
                        res.add(a + b)
            return res

        @lru_cache(None)
        def dfs(i, j, diff):
            if i == n and j == m:
                return diff == 0
            if i < n and s1[i].isdigit():
                k = i
                while k < n and s1[k].isdigit():
                    k += 1
                for x in splits(s1[i:k]):
                    if dfs(k, j, diff + x):
                        return True
                return False
            if j < m and s2[j].isdigit():
                k = j
                while k < m and s2[k].isdigit():
                    k += 1
                for x in splits(s2[j:k]):
                    if dfs(i, k, diff - x):
                        return True
                return False
            if diff == 0:
                return i < n and j < m and s1[i] == s2[j] and dfs(i + 1, j + 1, 0)
            if diff > 0:
                return j < m and dfs(i, j + 1, diff - 1)
            return i < n and dfs(i + 1, j, diff + 1)

        res = dfs(0, 0, 0)
        dfs.cache_clear()
        return res
