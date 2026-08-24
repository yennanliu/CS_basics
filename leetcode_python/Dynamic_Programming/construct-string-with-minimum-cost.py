"""

3213. Construct String with Minimum Cost
Hard

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

1 <= target.length <= 5 * 10^4
1 <= words.length == costs.length <= 5 * 10^4
1 <= words[i].length <= target.length
The total sum of words[i].length is less than or equal to 5 * 10^4
target and words[i] consist only of lowercase English letters.
1 <= costs[i] <= 10^4

"""

# V0
# IDEA : DP OVER PREFIXES, WITH A ROLLING HASH SO EACH WORD LENGTH IS O(1)
#
#   dp[i] = cheapest way to build target[:i], so
#       dp[i] = min over words w that end at i of  dp[i - len(w)] + cost(w)
#
#   the saving comes from the "total word length <= 5*10^4" guarantee : the
#   number of DISTINCT word lengths L satisfies 1+2+...+L <= 5*10^4, so
#   L <= 316. that bounds the transitions per position.
#
#   to test "does target[i-len : i] equal some word of this length" in O(1),
#   precompute a polynomial rolling hash of target and store, per length, a
#   map {hash -> cheapest cost}. duplicate words collapse to their minimum
#   cost automatically.
#
#   NOTE : two moduli would make a collision essentially impossible; one
#          64-bit modulus is used here for brevity.
#
"""

DP def
    dp[i]: MINIMUM cost to build the prefix target[0:i]

           -> inf means that prefix cannot be assembled at all

DP eq

     dp[i] = min over words w that END at i of

                dp[i - len(w)] + cost(w)


    -> e.g. the speed-up:
         "sum of word lengths <= 5*10^4" => at most ~316 DISTINCT word
         lengths (1+2+...+L <= 5*10^4), so per position we only try ~316 L's

         for each length L keep a map {rolling hash of the word -> min cost},
         so the test "does target[i-L:i] equal some word of length L" is O(1)

     init: dp[0] = 0, dp[i > 0] = inf
     ans = dp[n], or -1 if it is still inf

"""
# time = O(n * distinct lengths), space = O(n + total word length)
class Solution(object):
    def minimumCost(self, target, words, costs):
        MOD = (1 << 61) - 1
        BASE = 131

        n = len(target)
        # prefix hashes of target
        h = [0] * (n + 1)
        pw = [1] * (n + 1)
        for i, ch in enumerate(target):
            h[i + 1] = (h[i] * BASE + ord(ch)) % MOD
            pw[i + 1] = pw[i] * BASE % MOD

        def sub_hash(lo, hi):                    # hash of target[lo:hi]
            return (h[hi] - h[lo] * pw[hi - lo]) % MOD

        by_len = {}                              # length -> {hash: min cost}
        for w, c in zip(words, costs):
            L = len(w)
            if L > n:
                continue
            hw = 0
            for ch in w:
                hw = (hw * BASE + ord(ch)) % MOD
            table = by_len.setdefault(L, {})
            if hw not in table or c < table[hw]:
                table[hw] = c

        lengths = sorted(by_len)
        INF = float('inf')
        dp = [INF] * (n + 1)
        dp[0] = 0
        for i in range(1, n + 1):
            for L in lengths:
                if L > i:
                    break
                if dp[i - L] == INF:
                    continue
                c = by_len[L].get(sub_hash(i - L, i))
                if c is not None and dp[i - L] + c < dp[i]:
                    dp[i] = dp[i - L] + c
        return -1 if dp[n] == INF else dp[n]
