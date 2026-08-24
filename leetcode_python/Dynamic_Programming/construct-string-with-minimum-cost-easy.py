"""

3253. Construct String with Minimum Cost (Easy)
Medium
🔒 (premium)

You are given a string target, an array of strings words, and an integer array costs, both arrays of the same length.

Imagine an empty string s.

You can perform the following operation any number of times (including zero):

Choose an index i in the range [0, words.length - 1].
Append words[i] to s.
The cost of operation is costs[i].

Return the minimum cost to make s equal to target. If it's not possible, return -1.


Example 1:

Input: target = "abcdef", words = ["abdef","abc","d","def","ef"], costs = [100,1,1,10,5]
Output: 7
Explanation:
The minimum cost can be achieved by performing the following operations:
Select index 1 and append "abc" to s at a cost of 1, resulting in s = "abc".
Select index 2 and append "d" to s at a cost of 1, resulting in s = "abcd".
Select index 4 and append "ef" to s at a cost of 5, resulting in s = "abcdef".

Example 2:

Input: target = "aaaa", words = ["z","zz","zzz"], costs = [1,10,100]
Output: -1
Explanation:
It is impossible to make s equal to target, so we return -1.


Constraints:

1 <= target.length <= 2000
1 <= words.length == costs.length <= 50
1 <= words[i].length <= target.length
target and words[i] consist only of lowercase English letters.
1 <= costs[i] <= 10^4

"""

# V0
# IDEA : DP OVER PREFIXES, TESTING EACH WORD AS THE LAST PIECE
#
#   dp[i] = cheapest way to build target[:i], so
#       dp[i] = min over words w with target[i-len(w):i] == w of
#                   dp[i - len(w)] + cost(w)
#
#   with only 50 words and a 2000-character target, the direct slice
#   comparison is affordable — the harder sibling (LC 3213) pushes both to
#   5*10^4 and needs a rolling hash keyed by word length.
#
#   dp starts at infinity everywhere except dp[0] = 0, and an unreachable
#   dp[n] means the target cannot be assembled at all.
#
"""

DP def
    dp[i]: MINIMUM cost to build the prefix target[0:i]

           -> inf means that prefix cannot be assembled at all

DP eq

     dp[i] = min over words w with target[i-len(w):i] == w of

                dp[i - len(w)] + cost(w)


    -> e.g.
         dp[i] = min(
            dp[i - len(w)] + costs[k]   # append word w as the LAST piece
            for every word w that ends exactly at i
         )

     init: dp[0] = 0, dp[i > 0] = inf
     ans = dp[n], or -1 if it is still inf

"""
# time = O(n * m * L), space = O(n)
class Solution(object):
    def minimumCost(self, target, words, costs):
        n = len(target)
        INF = float('inf')
        dp = [INF] * (n + 1)
        dp[0] = 0

        pairs = [(w, c) for w, c in zip(words, costs) if len(w) <= n]
        for i in range(1, n + 1):
            for w, c in pairs:
                L = len(w)
                if L <= i and dp[i - L] != INF and target[i - L:i] == w:
                    if dp[i - L] + c < dp[i]:
                        dp[i] = dp[i - L] + c
        return -1 if dp[n] == INF else dp[n]
