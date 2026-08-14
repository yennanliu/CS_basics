"""

2539. Count the Number of Good Subsequences
Medium

A subsequence of a string is good if it is not empty and the frequency of each one of its characters is the same.

Given a string s, return the number of good subsequences of s. Since the answer may be too large, return it modulo 10^9 + 7.

A subsequence is a string that can be derived from another string by deleting some or no characters without changing the order of the remaining characters.


Example 1:

Input: s = "aabb"
Output: 11
Explanation: The total number of subsequences is 2^4. There are five subsequences which are not good: "aab", "abb", "abb", "aab", and the empty subsequence. Hence, the number of good subsequences is 2^4 - 5 = 11.

Example 2:

Input: s = "leet"
Output: 12
Explanation: There are four subsequences which are not good: "lee", "eet", "leet", and the empty subsequence. Hence, the number of good subsequences is 2^4 - 4 = 12.

Example 3:

Input: s = "abcd"
Output: 15
Explanation: All of the non-empty subsequences are good subsequences. Hence, the number of good subsequences is 2^4 - 1 = 15.


Constraints:

1 <= s.length <= 10^4
s consists of only lowercase English letters.

"""

# V0
# IDEA : COMBINATORICS -- FIX THE COMMON FREQUENCY, MULTIPLY OVER LETTERS
#
#   a good subsequence is fully described by (i) the common frequency f and
#   (ii) which occurrences of each participating letter are taken. so enumerate
#   f from 1 to max(count) and, for each letter c with cnt[c] >= f, decide
#   independently either to skip c or to pick f of its cnt[c] occurrences :
#
#       ways(f) = PROD over c with cnt[c] >= f of ( C(cnt[c], f) + 1 )
#
#   the "+1" is the skip option; subtracting 1 removes the all-skipped case,
#   which would be the empty subsequence.
#
#       answer = SUM over f of ( ways(f) - 1 )   (mod 1e9+7)
#
#   NOTE : two different f never produce the same subsequence, so there is no
#          double counting -- the frequency is determined by the subsequence.
#   NOTE : nCr is done modularly with Fermat's little theorem (MOD is prime),
#          so the inverse factorial is pow(fact, MOD - 2, MOD).
#
# time = O(n * 26), space = O(n)
from collections import Counter


class Solution(object):
    def countGoodSubsequences(self, s):
        MOD = 10 ** 9 + 7
        n = len(s)

        fact = [1] * (n + 1)
        for i in range(1, n + 1):
            fact[i] = fact[i - 1] * i % MOD
        inv_fact = [1] * (n + 1)
        inv_fact[n] = pow(fact[n], MOD - 2, MOD)
        for i in range(n, 0, -1):
            inv_fact[i - 1] = inv_fact[i] * i % MOD

        def comb(a, b):
            if b < 0 or b > a:
                return 0
            return fact[a] * inv_fact[b] % MOD * inv_fact[a - b] % MOD

        cnt = Counter(s)
        counts = list(cnt.values())
        ans = 0
        for f in range(1, max(counts) + 1):
            ways = 1
            for c in counts:
                if c >= f:
                    ways = ways * (comb(c, f) + 1) % MOD
            ans = (ans + ways - 1) % MOD
        return ans % MOD
