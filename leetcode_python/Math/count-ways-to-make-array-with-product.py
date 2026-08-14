"""

1735. Count Ways to Make Array With Product
Hard

You are given a 2D integer array, queries. For each queries[i], where queries[i] = [ni, ki], find the number of different ways you can place positive integers into an array of size ni such that the product of the integers is ki. As the number of ways may be too large, the answer to the ith query is the number of ways modulo 10^9 + 7.

Return an integer array answer where answer.length == queries.length, and answer[i] is the answer to the ith query.


Example 1:

Input: queries = [[2,6],[5,1],[73,660]]
Output: [4,1,50734910]
Explanation: Each query is independent.
[2,6]: There are 4 ways to fill an array of size 2 that multiply to 6: [1,6], [2,3], [3,2], [6,1].
[5,1]: There is 1 way to fill an array of size 5 that multiply to 1: [1,1,1,1,1].
[73,660]: There are 1050734917 ways to fill an array of size 73 that multiply to 660. 1050734917 modulo 10^9 + 7 = 50734910.

Example 2:

Input: queries = [[1,1],[2,2],[3,3],[4,4],[5,5]]
Output: [1,2,3,10,5]


Constraints:

1 <= queries.length <= 10^4
1 <= ni, ki <= 10^4

"""

# V0
# IDEA : PRIME FACTORISATION + STARS AND BARS
#
#   write k = p1^e1 * p2^e2 * ... . every array slot gets some share of each
#   prime, and the primes are INDEPENDENT of each other, so
#
#       ways(n, k) = product over primes of  (ways to split e_i among n slots)
#
#   splitting e identical items into n ordered boxes, EMPTY BOXES ALLOWED,
#   is the classic stars-and-bars count:
#
#       C(e + n - 1, n - 1)
#
#   (e stars and n-1 bars laid out in a row; the bars pick the cut points.)
#
#   two precomputations make each query cheap:
#     - smallest-prime-factor sieve up to 10^4 -> factorise any k in O(log k)
#     - factorials + inverse factorials mod 1e9+7 -> C(a, b) in O(1)
#       (inverse via Fermat: x^(p-2) = x^-1 for prime p, then walk down)
#
#   NOTE : k = 1 has no prime factors, so the empty product gives 1 - which
#          is right, the array must be all ones.
#
# time = O(K log log K + q * log K), space = O(K), K = 10^4
class Solution(object):
    def waysToFillArray(self, queries):
        MOD = 10 ** 9 + 7
        LIMIT = 10 ** 4
        # n + e - 1 <= 10^4 + log2(10^4) -> a little headroom is enough
        SIZE = LIMIT + 20

        # smallest prime factor sieve
        spf = list(range(LIMIT + 1))
        i = 2
        while i * i <= LIMIT:
            if spf[i] == i:
                for j in range(i * i, LIMIT + 1, i):
                    if spf[j] == j:
                        spf[j] = i
            i += 1

        # factorials + inverse factorials
        fact = [1] * SIZE
        for i in range(1, SIZE):
            fact[i] = fact[i - 1] * i % MOD
        inv = [1] * SIZE
        inv[SIZE - 1] = pow(fact[SIZE - 1], MOD - 2, MOD)
        for i in range(SIZE - 1, 0, -1):
            inv[i - 1] = inv[i] * i % MOD

        def comb(a, b):
            return fact[a] * inv[b] % MOD * inv[a - b] % MOD

        res = []
        for n, k in queries:
            cur = 1
            while k > 1:
                p = spf[k]
                e = 0
                while k % p == 0:
                    k //= p
                    e += 1
                cur = cur * comb(e + n - 1, e) % MOD
            res.append(cur)

        return res
