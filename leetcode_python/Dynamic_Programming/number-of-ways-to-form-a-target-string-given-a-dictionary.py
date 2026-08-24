"""

1639. Number of Ways to Form a Target String Given a Dictionary
Hard

You are given a list of strings of the same length words and a string target.

Your task is to form target using the given words under the following rules:

- target should be formed from left to right.
- To form the ith character (0-indexed) of target, you can choose the kth character of the jth string in words if target[i] = words[j][k].
- Once you use the kth character of the jth string of words, you can no longer use the xth character of any string in words where x <= k. In other words, all characters to the left of or at index k become unusuable for every string.
- Repeat the process until you form the string target.

Notice that you can use multiple characters from the same string in words provided the conditions above are met.

Return the number of ways to form target from words. Since the answer may be too large, return it modulo 10^9 + 7.


Example 1:

Input: words = ["acca","bbbb","caca"], target = "aba"
Output: 6
Explanation: There are 6 ways to form target.
"aba" -> index 0 ("acca"), index 1 ("bbbb"), index 3 ("caca")
"aba" -> index 0 ("acca"), index 2 ("bbbb"), index 3 ("caca")
"aba" -> index 0 ("acca"), index 1 ("bbbb"), index 3 ("acca")
"aba" -> index 0 ("acca"), index 2 ("bbbb"), index 3 ("acca")
"aba" -> index 1 ("caca"), index 2 ("bbbb"), index 3 ("acca")
"aba" -> index 1 ("caca"), index 2 ("bbbb"), index 3 ("caca")

Example 2:

Input: words = ["abba","baab"], target = "bab"
Output: 4
Explanation: There are 4 ways to form target.
"bab" -> index 0 ("baab"), index 1 ("baab"), index 2 ("abba")
"bab" -> index 0 ("baab"), index 1 ("baab"), index 3 ("baab")
"bab" -> index 0 ("baab"), index 2 ("baab"), index 3 ("baab")
"bab" -> index 1 ("abba"), index 2 ("baab"), index 3 ("baab")


Constraints:

1 <= words.length <= 1000
1 <= words[i].length <= 1000
All strings in words have the same length.
1 <= target.length <= 1000
words[i] and target contain only lowercase English letters.

"""

# V0
# IDEA : DP over COLUMNS (which word we pick never matters, only the count)
#
#   the "x <= k becomes unusable" rule means column indices must strictly
#   increase, and words are otherwise interchangeable. So precompute
#     cnt[j][c] = how many words have character c at column j
#
#   dp[i] = #ways to have formed target[:i] using columns processed so far
#     for each column j:  dp[i+1] += dp[i] * cnt[j][target[i]]
#   (either skip column j entirely, or spend it on target[i])
#
#   NOTE : iterate i DOWNWARD when updating in place, so a single column
#          is not used twice in the same pass.
#
"""

DP def
    the "x <= k becomes unusable" rule means the chosen COLUMN indices must
    strictly increase, and the words are otherwise interchangeable - so which
    word we pick never matters, only how many offer the needed letter

    cnt[j][c]: how many words have character c at column j

    dp[i]    : number of ways to have formed target[:i] using the columns

               processed so far

DP eq

     for each column j:

        dp[i+1] += dp[i] * cnt[j][ target[i] ]


    -> e.g. either SKIP column j entirely (dp unchanged) or SPEND it on
              target[i]

     NOTE !!! iterate i DOWNWARD when updating in place, so a single column
              is not used twice in the same pass

     init: dp[0] = 1
     ans = dp[m] % (10^9 + 7)

"""
# time = O(n * (L + m)), space = O(26 * n + m) with n = word len, m = len(target)
class Solution(object):
    def numWays(self, words, target):
        MOD = 10 ** 9 + 7
        m, n = len(target), len(words[0])

        cnt = [[0] * 26 for _ in range(n)]
        for w in words:
            for j in range(n):
                cnt[j][ord(w[j]) - 97] += 1

        dp = [0] * (m + 1)
        dp[0] = 1
        for j in range(n):
            col = cnt[j]
            for i in range(min(j, m - 1), -1, -1):
                c = col[ord(target[i]) - 97]
                if c:
                    dp[i + 1] = (dp[i + 1] + dp[i] * c) % MOD
        return dp[m]
