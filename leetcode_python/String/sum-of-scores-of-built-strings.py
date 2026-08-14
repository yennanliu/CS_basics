"""

2223. Sum of Scores of Built Strings
Hard

You are building a string s of length n one character at a time, prepending each new character to the front of the string. The strings are labeled from 1 to n, where the string with length i is labeled si.

For example, for s = "abaca", s1 == "a", s2 == "ca", s3 == "aca", etc.

The score of si is the length of the longest common prefix between si and sn (Note that s == sn).

Given the final string s, return the sum of the score of every si.


Example 1:

Input: s = "babab"
Output: 9
Explanation:
For s1 == "b", the longest common prefix is "b" which has a score of 1.
For s2 == "ab", there is no common prefix so the score is 0.
For s3 == "bab", the longest common prefix is "bab" which has a score of 3.
For s4 == "abab", there is no common prefix so the score is 0.
For s5 == "babab", the longest common prefix is "babab" which has a score of 5.
The sum of the scores is 1 + 0 + 3 + 0 + 5 = 9, so we return 9.

Example 2:

Input: s = "azbazbzaz"
Output: 14
Explanation:
For s2 == "az", the longest common prefix is "az" which has a score of 2.
For s6 == "azbzaz", the longest common prefix is "azb" which has a score of 3.
For s9 == "azbazbzaz", the longest common prefix is "azbazbzaz" which has a score of 9.
For all other si, the score is 0.
The sum of the scores is 2 + 3 + 9 = 14, so we return 14.


Constraints:

1 <= s.length <= 10^5
s consists of lowercase English letters.

"""

# V0
# IDEA : Z-ALGORITHM (every si is a SUFFIX of s, so score(si) = z[n - i])
#
#   prepending characters means the string of length i is exactly s[n-i:].
#   score(si) = length of the longest common prefix of s[n-i:] and s
#             = z[n - i], the classic Z-array value.
#   so the answer is simply sum(z), with z[0] defined as n.
#
#   z-box invariant : [l, r) is the rightmost match of a prefix seen so far.
#     if i < r we can copy z[i - l], capped at r - i, then extend by brute
#     force; each extension advances r, so the total work is linear.
#
# time = O(n), space = O(n)
class Solution(object):
    def sumScores(self, s):
        n = len(s)
        z = [0] * n
        z[0] = n
        l, r = 0, 0
        for i in range(1, n):
            if i < r:
                z[i] = min(r - i, z[i - l])
            while i + z[i] < n and s[z[i]] == s[i + z[i]]:
                z[i] += 1
            if i + z[i] > r:
                l, r = i, i + z[i]
        return sum(z)
