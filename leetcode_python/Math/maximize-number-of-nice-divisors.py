"""

1808. Maximize Number of Nice Divisors
Medium

You are given a positive integer primeFactors. You are asked to construct a positive integer n that satisfies the following conditions:

The number of prime factors of n (not necessarily distinct) is at most primeFactors.
The number of nice divisors of n is maximized. Note that a divisor of n is nice if it is divisible by every prime factor of n. For example, if n = 12, then its prime factors are [2,2,3], then 6 and 12 are nice divisors, while 3 and 4 are not.

Return the number of nice divisors of n. Since that number can be too large, return it modulo 10^9 + 7.

Note that a prime number is a natural number greater than 1 that is not a product of two smaller natural numbers. The prime factors of a number n is a list of prime numbers such that their product equals n.


Example 1:

Input: primeFactors = 5
Output: 6
Explanation: 200 is a valid value of n.
It has 5 prime factors: [2,2,2,5,5], and it has 6 nice divisors: [10,20,40,50,100,200].
There is not other value of n that has at most 5 prime factors and more nice divisors.

Example 2:

Input: primeFactors = 8
Output: 18


Constraints:

1 <= primeFactors <= 10^9

"""

# V0
# IDEA : INTEGER BREAK (maximize a product of parts summing to primeFactors)
#
#   if n = p1^a1 * p2^a2 * ... then a "nice" divisor must contain every pi at
#   least once, so it is (p1^b1 * ...) with 1 <= bi <= ai  ->  count = prod(ai).
#   we may pick as many distinct primes as we like, so the problem reduces to:
#   split primeFactors into positive parts and maximize their product.
#
#   classic result : use as many 3s as possible.
#     r = primeFactors % 3
#     r == 0 -> 3^(k)
#     r == 1 -> take one 3 back and use 2+2 : 4 * 3^(k-1)
#     r == 2 -> 2 * 3^(k)
#   NOTE : primeFactors < 4 is a special case (1,2,3 -> answer is itself).
#   NOTE : primeFactors is up to 10^9, so use fast modular pow, not a loop.
#
# time = O(log primeFactors), space = O(1)
class Solution(object):
    def maxNiceDivisors(self, primeFactors):
        MOD = 10 ** 9 + 7
        if primeFactors < 4:
            return primeFactors
        q, r = divmod(primeFactors, 3)
        if r == 0:
            return pow(3, q, MOD)
        if r == 1:
            return 4 * pow(3, q - 1, MOD) % MOD
        return 2 * pow(3, q, MOD) % MOD
