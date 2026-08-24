"""

3333. Find the Original Typed String II
Hard

Alice is attempting to type a specific string on her computer. However, she tends to be clumsy and may press a key for too long, resulting in a character being typed multiple times.

You are given a string word, which represents the final output displayed on Alice's screen. You are also given a positive integer k.

Return the total number of possible original strings that Alice might have intended to type, if she was trying to type a string of size at least k.

Since the answer may be very large, return it modulo 10^9 + 7.


Example 1:

Input: word = "aabbccdd", k = 7
Output: 5
Explanation:
The possible strings are: "aabbccdd", "aabbccd", "aabbcdd", "aabccdd", and "abbccdd".

Example 2:

Input: word = "aabbccdd", k = 8
Output: 1
Explanation:
The only possible string is "aabbccdd".

Example 3:

Input: word = "aaabbb", k = 3
Output: 8


Constraints:

1 <= word.length <= 5 * 10^5
word consists only of lowercase English letters.
1 <= k <= 2000

"""

# V0
# IDEA : COUNT EVERYTHING, THEN SUBTRACT THE TOO-SHORT ONES WITH A DP
#
#   the intended string keeps at least one character from every run, so with
#   run lengths r_1..r_m the number of candidates is prod(r_i) — each run is
#   independently shortened to anything in [1, r_i].
#
#   the length constraint is the awkward part, but it only bites when the
#   shortest candidate is under k, i.e. when m < k. in that case count the
#   BAD ones (total length < k) and subtract :
#
#       dp[j] = ways for the runs processed so far to total length j
#       adding a run of length r spreads dp over j+1 .. j+r, which a sliding
#       prefix sum applies in O(1) per cell
#
#   only lengths below k matter, so the table is m x k and m < k <= 2000.
#
"""

DP def
    the intended string keeps at least ONE character from every run, so with
    run lengths r_1..r_m the total number of candidates is prod(r_i).

    the length constraint only bites when the SHORTEST candidate is under k,
    i.e. when m < k - so count the BAD ones and subtract.

    dp[j]: ways for the runs processed so far to total length exactly j

           (only j < k matters)

DP eq

     adding a run of length r spreads dp over j+1 .. j+r:

        dp_new[j] = dp[j-1] + dp[j-2] + ... + dp[j-r]

                  = a SLIDING PREFIX SUM -> O(1) per cell


    -> e.g. ans = prod(r_i) - sum(dp[j] for j < k)      mod 10^9 + 7
              (and just prod(r_i) when m >= k)

     init: dp[0] = 1

"""
# time = O(m * k), space = O(k)
class Solution(object):
    def possibleStringCount(self, word, k):
        MOD = 10 ** 9 + 7

        runs = []
        cur = 1
        for i in range(1, len(word)):
            if word[i] == word[i - 1]:
                cur += 1
            else:
                runs.append(cur)
                cur = 1
        runs.append(cur)

        total = 1
        for r in runs:
            total = total * r % MOD

        m = len(runs)
        if m >= k:
            return total

        # dp[j] = ways to reach total length j (only j < k matters)
        dp = [0] * k
        dp[0] = 1
        for r in runs:
            pre = [0] * (k + 1)
            for j in range(k):
                pre[j + 1] = (pre[j] + dp[j]) % MOD
            nxt = [0] * k
            for j in range(1, k):
                lo = max(0, j - r)              # previous length j - take
                nxt[j] = (pre[j] - pre[lo]) % MOD
            dp = nxt

        bad = sum(dp) % MOD
        return (total - bad) % MOD
