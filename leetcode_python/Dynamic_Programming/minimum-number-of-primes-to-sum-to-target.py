"""

3610. Minimum Number of Primes to Sum to Target
Medium

You are given two integers n and m.

You have to select a multiset of prime numbers from the first m prime numbers
such that the sum of the selected primes is exactly n. You may use each prime
number multiple times.

Return the minimum number of prime numbers needed to sum up to n, or -1 if it
is not possible.


Example 1:

Input: n = 10, m = 2
Output: 4
Explanation:
The first 2 primes are [2, 3]. The sum 10 can be formed as 2 + 2 + 3 + 3,
requiring 4 primes.

Example 2:

Input: n = 15, m = 5
Output: 3
Explanation:
The first 5 primes are [2, 3, 5, 7, 11]. The sum 15 can be formed as 5 + 5 + 5,
requiring 3 primes.

Example 3:

Input: n = 7, m = 6
Output: 1
Explanation:
The first 6 primes are [2, 3, 5, 7, 11, 13]. The sum 7 can be formed directly by
prime 7, requiring only 1 prime.


Constraints:

1 <= n <= 1000
1 <= m <= 1000
"""

# V0
# IDEA : UNBOUNDED COIN CHANGE OVER THE FIRST M PRIMES
#
#   "multiset, each prime reusable" is exactly the unbounded coin-change shape:
#   order does not matter and supply is unlimited, so f[i] = min over allowed
#   primes p of f[i - p] + 1 is a complete recurrence — every multiset summing
#   to i has some largest-indexed member p, and dropping one copy of it leaves
#   a valid multiset for i - p.
#
#   iterating i upwards inside the loop over p is the unbounded (not 0/1)
#   sweep: f[i - p] may itself already contain copies of p, which is what lets a
#   prime be reused.
#
#   only primes up to n can ever appear, so the sieve never has to reach the
#   1000th prime unless n allows it — we stop generating as soon as the primes
#   exceed n, which also caps the work at O(n) per prime regardless of m.
#
# time = O(n * min(m, pi(n))), space = O(n)
class Solution(object):
    def minNumberOfPrimes(self, n, m):
        # the first m primes, truncated at n (bigger ones can never be used)
        primes = []
        sieve = [True] * (n + 1)
        for x in range(2, n + 1):
            if sieve[x]:
                primes.append(x)
                if len(primes) == m:
                    break
                for y in range(x * x, n + 1, x):
                    sieve[y] = False

        INF = float('inf')
        f = [0] + [INF] * n
        for p in primes:
            for i in range(p, n + 1):
                if f[i - p] + 1 < f[i]:
                    f[i] = f[i - p] + 1
        return f[n] if f[n] < INF else -1
