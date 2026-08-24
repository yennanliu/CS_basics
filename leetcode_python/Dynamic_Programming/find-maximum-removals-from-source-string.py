"""

3316. Find Maximum Removals From Source String
Medium

You are given a string source of size n, a string pattern that is a subsequence of source, and a sorted integer array targetIndices that contains distinct numbers in the range [0, n - 1].

We define an operation as removing a character at an index idx from source such that:

idx is an element of targetIndices.
pattern remains a subsequence of source after removing the character.

Performing an operation does not change the indices of the other characters in source. For example, if you remove 'c' from "acb", the character at index 2 would still be 'b'.

Return the maximum number of operations that can be performed.


Example 1:

Input: source = "abbaa", pattern = "aba", targetIndices = [0,1,2]
Output: 1
Explanation:
We can't remove source[0] but we can do either of these two operations:
Remove source[1], so that source becomes "a_baa".
Remove source[2], so that source becomes "ab_aa".

Example 2:

Input: source = "bcda", pattern = "d", targetIndices = [0,3]
Output: 2
Explanation:
We can remove source[0] and source[3] in two operations.

Example 3:

Input: source = "dda", pattern = "dda", targetIndices = [0,1,2]
Output: 0
Explanation:
We can't remove any character from source.

Example 4:

Input: source = "yeyeykyded", pattern = "yeyyd", targetIndices = [0,2,3,4]
Output: 2
Explanation:
We can remove source[2] and source[3] in two operations.


Constraints:

1 <= n == source.length <= 3 * 10^3
1 <= pattern.length <= n
1 <= targetIndices.length <= n
targetIndices is sorted in ascending order.
0 <= targetIndices[i] < n
source and pattern consist only of lowercase English letters.
The input is generated such that targetIndices contains distinct elements and pattern is a subsequence of source.

"""

# V0
# IDEA : DP OVER (SOURCE PREFIX, PATTERN PREFIX) MAXIMISING THE DELETIONS
#
#   removals interact — deleting one character can starve a later match — so
#   they cannot be decided greedily one at a time. instead track the matching
#   process itself :
#
#       dp[i][j] = most removable target characters among source[:i] while
#                  pattern[:j] has been matched
#
#   at source position i there are two moves :
#       keep it as pattern[j]  (only if the letters agree)  -> dp[i+1][j+1]
#       skip it                -> dp[i+1][j], plus 1 if i is a target index
#
#   the answer is dp[n][m]; unreachable states stay at -inf so they never win.
#
"""

DP def
    removals INTERACT (deleting one char can starve a later match), so they
    cannot be chosen greedily - track the matching process itself

    dp[i][j]: MOST removable target characters among source[:i]

              while pattern[:j] has been matched

              -> -inf marks an unreachable state

DP eq

     at source position i (gain = 1 if i in targetIndices else 0):

        skip source[i] : dp[i+1][j]   = max(dp[i+1][j],   dp[i][j] + gain)

        match it       : dp[i+1][j+1] = max(dp[i+1][j+1], dp[i][j])
                         (only if source[i] == pattern[j])


    -> e.g. a matched character is KEPT, so it earns no removal even when it
              sits on a target index

     init: dp[0][0] = 0
     ans = dp[n][m]

"""
# time = O(n * m), space = O(m)
class Solution(object):
    def maxRemovals(self, source, pattern, targetIndices):
        n, m = len(source), len(pattern)
        targets = set(targetIndices)
        NEG = float('-inf')

        dp = [NEG] * (m + 1)
        dp[0] = 0
        for i in range(n):
            nxt = [NEG] * (m + 1)
            gain = 1 if i in targets else 0
            for j in range(m + 1):
                if dp[j] == NEG:
                    continue
                # skip source[i]
                if dp[j] + gain > nxt[j]:
                    nxt[j] = dp[j] + gain
                # use source[i] to match pattern[j]
                if j < m and source[i] == pattern[j] and dp[j] > nxt[j + 1]:
                    nxt[j + 1] = dp[j]
            dp = nxt
        return dp[m]
