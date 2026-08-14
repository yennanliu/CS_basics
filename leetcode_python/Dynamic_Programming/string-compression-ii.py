"""

1531. String Compression II
Hard

Run-length encoding is a string compression method that works by replacing consecutive identical characters (repeated 2 or more times) with the concatenation of the character and the number marking the count of the characters (length of the run). For example, to compress the string "aabccc" we replace "aa" by "a2" and replace "ccc" by "c3". Thus the compressed string becomes "a2bc3".

Notice that in this problem, we are not adding '1' after single characters.

Given a string s and an integer k. You need to delete at most k characters from s such that the run-length encoded version of s has minimum length.

Find the minimum length of the run-length encoded version of s after deleting at most k characters.


Example 1:

Input: s = "aaabcccd", k = 2
Output: 4
Explanation: Compressing s without deleting anything will give us "a3bc3d" of length 6. Deleting any of the characters 'a' or 'c' would at most decrease the length of the compressed string to 5, for instance delete 2 'a' then we will have s = "abcccd" which compressed is abc3d. Therefore, the optimal way is to delete 'b' and 'd', then the compressed version of s will be "a3c3" of length 4.

Example 2:

Input: s = "aabbaa", k = 2
Output: 2
Explanation: If we delete both 'b' characters, the resulting compressed string would be "a4" of length 2.

Example 3:

Input: s = "aaaaaaaaaaa", k = 0
Output: 3
Explanation: Since k is zero, we cannot delete anything. The compressed string is "a11" of length 3.


Constraints:

1 <= s.length <= 100
0 <= k <= s.length
s contains only lowercase English letters.

"""

# V0
# IDEA : DP ON (index, deletions left), BUILDING ONE RUN AT A TIME
#
#   dfs(i, k) = min encoded length of s[i:] when we may still delete k chars.
#
#   at position i we either
#     (a) delete s[i]           -> dfs(i + 1, k - 1)
#     (b) KEEP s[i] and make it the head of a run : sweep j from i forward,
#         keeping every s[j] == s[i] (run length cnt) and deleting every
#         s[j] != s[i] (costing one deletion each). the run then contributes
#         width(cnt) and we recurse with dfs(j + 1, k - deleted).
#
#   width(cnt) = 1 for cnt == 1, 2 for cnt <= 9, 3 for cnt <= 99, else 4.
#
#   NOTE : building a whole run in one shot (rather than one char per state)
#          is what lets us charge the digit cost exactly once and correctly.
#   NOTE : if n - i <= k we can delete the entire tail -> length 0.
#
# time = O(n^2 * k), space = O(n * k)
class Solution(object):
    def getLengthOfOptimalCompression(self, s, k):
        n = len(s)
        INF = float('inf')

        def width(cnt):
            if cnt == 1:
                return 1
            if cnt < 10:
                return 2
            if cnt < 100:
                return 3
            return 4

        memo = {}

        def dfs(i, rem):
            if rem < 0:
                return INF
            if n - i <= rem:
                return 0
            key = (i, rem)
            if key in memo:
                return memo[key]

            # (a) delete s[i]
            best = dfs(i + 1, rem - 1)

            # (b) keep s[i], grow a run of that char
            cnt = 0
            deleted = 0
            for j in range(i, n):
                if s[j] == s[i]:
                    cnt += 1
                    sub = dfs(j + 1, rem - deleted)
                    if sub != INF:
                        cand = width(cnt) + sub
                        if cand < best:
                            best = cand
                else:
                    deleted += 1
                    if deleted > rem:
                        break

            memo[key] = best
            return best

        return dfs(0, k)
