"""

1175. Prime Arrangements
Easy

Return the number of permutations of 1 to n so that prime numbers are at prime indices (1-indexed.)

(Recall that an integer is prime if and only if it is greater than 1, and cannot be written as a
product of two positive integers both smaller than it.)

Since the answer may be large, return the answer modulo 10^9 + 7.

Example 1:

Input: n = 5
Output: 12
Explanation: For example [1,2,5,4,3] is a valid permutation, but [5,2,3,4,1] is not because the
prime number 5 is at index 1.

Example 2:

Input: n = 100
Output: 682289015

Constraints:

1 <= n <= 100

"""

# V0
# IDEA : MATH (SIEVE + FACTORIAL)
#
#  there are `cnt` primes in [1, n], hence `cnt` prime indices.
#  primes can be permuted among prime indices      -> cnt!
#  non-primes can be permuted among the rest       -> (n - cnt)!
#  answer = cnt! * (n - cnt)!  (mod 1e9 + 7)
#
# time = O(n log log n)
# space = O(n)
class Solution(object):
    def numPrimeArrangements(self, n):
        MOD = 10 ** 9 + 7

        is_prime = [True] * (n + 1)
        if n >= 0:
            is_prime[0] = False
        if n >= 1:
            is_prime[1] = False
        i = 2
        while i * i <= n:
            if is_prime[i]:
                for j in range(i * i, n + 1, i):
                    is_prime[j] = False
            i += 1
        cnt = sum(is_prime[2:]) if n >= 2 else 0

        def fact(x):
            r = 1
            for k in range(2, x + 1):
                r = (r * k) % MOD
            return r

        return (fact(cnt) * fact(n - cnt)) % MOD
