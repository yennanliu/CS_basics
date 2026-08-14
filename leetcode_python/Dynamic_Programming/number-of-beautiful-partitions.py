"""

2478. Number of Beautiful Partitions
Hard

You are given a string s that consists of the digits '1' to '9' and two integers k and minLength.

A partition of s is called beautiful if:

s is partitioned into k non-intersecting substrings.
Each substring has a length of at least minLength.
Each substring starts with a prime digit and ends with a non-prime digit. Prime digits are '2', '3', '5', and '7', and the rest of the digits are non-prime.

Return the number of beautiful partitions of s. Since the answer may be very large, return it modulo 10^9 + 7.

A substring is a contiguous sequence of characters within a string.


Example 1:

Input: s = "23542185131", k = 3, minLength = 2
Output: 3
Explanation: There exists three ways to create a beautiful partition:
"2354 | 218 | 5131"
"2354 | 21851 | 31"
"2354218 | 51 | 31"

Example 2:

Input: s = "23542185131", k = 3, minLength = 3
Output: 1
Explanation: There exists one way to create a beautiful partition: "2354 | 218 | 5131".

Example 3:

Input: s = "3312958", k = 3, minLength = 1
Output: 1
Explanation: There exists one way to create a beautiful partition: "331 | 29 | 58".


Constraints:

1 <= k, minLength <= s.length <= 1000
s consists of the digits '1' to '9'.

"""

# V0
# IDEA : DP OVER CUT POSITIONS + PREFIX SUMS
#
#   f[i][j] = #ways to split the first i chars into j beautiful pieces.
#   position i can end the j-th piece only if s[i-1] is NON-prime and the
#   next char (if any) is prime -- i.e. i is a legal cut point.
#   then f[i][j] = sum(f[t][j-1]) for t <= i - minLength, which is exactly
#   the prefix sum g[i - minLength][j - 1] with g[i][j] = sum(f[0..i][j]).
#   NOTE : s[0] must be prime and s[-1] must be non-prime, else answer is 0.
#
# time = O(n * k), space = O(n * k)
class Solution(object):
    def beautifulPartitions(self, s, k, minLength):
        primes = set('2357')
        n = len(s)
        if s[0] not in primes or s[-1] in primes:
            return 0

        MOD = 10 ** 9 + 7
        f = [[0] * (k + 1) for _ in range(n + 1)]
        g = [[0] * (k + 1) for _ in range(n + 1)]
        f[0][0] = g[0][0] = 1
        for i in range(1, n + 1):
            c = s[i - 1]
            if i >= minLength and c not in primes and (i == n or s[i] in primes):
                for j in range(1, k + 1):
                    f[i][j] = g[i - minLength][j - 1]
            for j in range(k + 1):
                g[i][j] = (g[i - 1][j] + f[i][j]) % MOD
        return f[n][k]
