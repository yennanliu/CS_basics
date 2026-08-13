"""

1163. Last Substring in Lexicographical Order
Hard

Given a string s, return the last substring of s in lexicographical order.

Example 1:

Input: s = "abab"
Output: "bab"
Explanation: The substrings are ["a", "ab", "aba", "abab", "b", "ba", "bab"]. The lexicographically maximum substring is "bab".

Example 2:

Input: s = "leetcode"
Output: "tcode"

Constraints:

1 <= s.length <= 4 * 10^5
s contains only lowercase English letters.

"""

# V0
# IDEA : TWO POINTERS
#
#  the answer must be a SUFFIX of s
#  (if a substring starts at i, extending it to the end only makes it bigger),
#  so we only need to find the largest suffix.
#
#  i = start of the best suffix so far
#  j = start of the candidate suffix
#  k = length already matched between the two
#
# time = O(n)
# space = O(1)
class Solution(object):
    def lastSubstring(self, s):
        n = len(s)
        i, j, k = 0, 1, 0
        while j + k < n:
            if s[i + k] == s[j + k]:
                # still tied, compare next char
                k += 1
            elif s[i + k] < s[j + k]:
                # candidate j wins -> every start in [i, i+k] is worse
                i = i + k + 1
                k = 0
                if i >= j:
                    j = i + 1
            else:
                # current best i wins -> every start in [j, j+k] is worse
                j = j + k + 1
                k = 0
        return s[i:]


# V1
# IDEA : BRUTE FORCE (only OK for small n)
# time = O(n^2)
# space = O(n)
class Solution(object):
    def lastSubstring(self, s):
        return max(s[i:] for i in range(len(s)))
