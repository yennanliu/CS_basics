"""

291. Word Pattern II
Medium
(premium)

Given a pattern and a string s, return true if s matches the pattern.

A string s matches a pattern if there is some bijective mapping of single characters to
non-empty strings such that if each character in pattern is replaced by the string it maps
to, then the resulting string is s. A bijective mapping means that no two characters map to
the same string, and no character maps to two different strings.


Example 1:

Input: pattern = "abab", s = "redblueredblue"
Output: true
Explanation: One possible mapping is as follows:
'a' -> "red"
'b' -> "blue"

Example 2:

Input: pattern = "aaaa", s = "asdasdasdasd"
Output: true
Explanation: One possible mapping is as follows:
'a' -> "asd"

Example 3:

Input: pattern = "aabb", s = "xyzabcxzyabc"
Output: false


Constraints:

1 <= pattern.length, s.length <= 20
pattern and s consist of only lowercase English letters.

"""

# V0
# IDEA : BACKTRACKING with a bijective (two-way) mapping
#
#  dfs(i, j) = can pattern[i:] match s[j:] ?
#    - if pattern[i] is already mapped -> the mapped word MUST be the next slice of s
#    - otherwise -> try every non-empty prefix s[j:end] as the word for pattern[i]
#
#  `used_words` enforces the OTHER direction of the bijection: two different pattern
#  chars may not map to the same word.
#
# time  = O(n^m)  # m = len(pattern), n = len(s); heavily pruned in practice
# space = O(m + n)
class Solution(object):
    def wordPatternMatch(self, pattern, s):
        m, n = len(pattern), len(s)
        char_to_word = {}
        used_words = set()

        def dfs(i, j):
            # both consumed at the same time -> success
            if i == m and j == n:
                return True
            # one ran out before the other -> failure
            if i == m or j == n:
                return False

            c = pattern[i]

            # already bound: the next chunk of s must be exactly that word
            if c in char_to_word:
                w = char_to_word[c]
                if not s.startswith(w, j):
                    return False
                return dfs(i + 1, j + len(w))

            # not bound yet: try every non-empty prefix
            for end in range(j + 1, n + 1):
                w = s[j:end]
                if w in used_words:
                    continue  # another pattern char already owns this word

                char_to_word[c] = w
                used_words.add(w)

                if dfs(i + 1, end):
                    return True

                # backtrack
                del char_to_word[c]
                used_words.remove(w)

            return False

        return dfs(0, 0)
