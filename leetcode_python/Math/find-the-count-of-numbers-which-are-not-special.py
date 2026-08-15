"""

3233. Find the Count of Numbers Which Are Not Special
Medium

You are given 2 positive integers l and r. For any number x, all positive divisors of x except x are called the proper divisors of x.

A number is called special if it has exactly 2 proper divisors. For example:

The number 4 is special because it has proper divisors 1 and 2.
The number 6 is not special because it has proper divisors 1, 2, and 3.

Return the count of numbers in the range [l, r] that are not special.


Example 1:

Input: l = 5, r = 7
Output: 3
Explanation:
There are no special numbers in the range [5, 7].

Example 2:

Input: l = 4, r = 16
Output: 11
Explanation:
The special numbers in the range [4, 16] are 4 and 9.


Constraints:

1 <= l <= r <= 10^9

"""

# V0
# IDEA : "EXACTLY TWO PROPER DIVISORS" MEANS x IS THE SQUARE OF A PRIME
#
#   the proper divisors are 1 and one other value d, so x = d * d and d must
#   itself be prime — any factorisation of d would add more divisors.
#
#   so the special numbers in [l, r] are p^2 for primes p in
#   [ceil(sqrt(l)), floor(sqrt(r))], and r <= 10^9 caps p at 31623 — a tiny
#   sieve.
#
#   the answer is the range size minus that count.
#
# time = O(sqrt(r) log log sqrt(r)), space = O(sqrt(r))
class Solution(object):
    def nonSpecialCount(self, l, r):
        limit = int(r ** 0.5)
        while (limit + 1) * (limit + 1) <= r:
            limit += 1
        while limit * limit > r:
            limit -= 1

        sieve = [True] * (limit + 1)
        if limit >= 0:
            sieve[0] = False
        if limit >= 1:
            sieve[1] = False
        p = 2
        while p * p <= limit:
            if sieve[p]:
                for m in range(p * p, limit + 1, p):
                    sieve[m] = False
            p += 1

        special = sum(1 for p in range(2, limit + 1) if sieve[p] and p * p >= l)
        return (r - l + 1) - special
