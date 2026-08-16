"""

3503. Longest Palindrome After Substring Concatenation I
Medium

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

1 <= s.length, t.length <= 30

s and t consist of lowercase English letters.

"""

# V0
# IDEA : BRUTE FORCE OVER EVERY (SUBSTRING OF s, SUBSTRING OF t) PAIR
#
#   with |s|, |t| <= 30 there are only ~31*32/2 = 496 substrings on each side
#   (counting the empty one), so all ~250k concatenations can be built and
#   palindrome-tested directly.
#
#   the empty substring must be allowed on either side — that is what lets the
#   answer be a palindrome living entirely inside s or entirely inside t.
#
# time = O(n^2 * m^2 * (n+m)), space = O(n+m)
class Solution(object):
    def longestPalindrome(self, s, t):
        subs_s = [s[i:j] for i in range(len(s) + 1) for j in range(i, len(s) + 1)]
        subs_t = [t[i:j] for i in range(len(t) + 1) for j in range(i, len(t) + 1)]
        # deduplicate to cut the work; identical substrings behave identically
        subs_s = list(set(subs_s))
        subs_t = list(set(subs_t))
        best = 0
        for a in subs_s:
            for b in subs_t:
                if len(a) + len(b) <= best:
                    continue
                cand = a + b
                if cand == cand[::-1]:
                    best = len(cand)
        return best
