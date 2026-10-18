"""

4079. Prime Subset Selection I
Medium

You are given two integers n and s.

Consider all prime numbers less than or equal to n. Select some of these
primes such that:

- The sum of the selected primes is less than or equal to s.
- The number of selected primes is maximized.
- Each prime can be selected at most once.

Return an integer array containing the selected primes in increasing order.
If multiple optimal selections exist, return any of them.

If no prime can be selected, return an empty array.


Example 1:

Input: n = 7, s = 20

Output: [2,3,5,7]

Explanation:

- The prime numbers less than or equal to n = 7 are 2, 3, 5, and 7.
- The sum of all these primes is 2 + 3 + 5 + 7 = 17, which is less than or
  equal to s = 20.
- Thus, the answer is [2, 3, 5, 7].

Example 2:

Input: n = 15, s = 15

Output: [3,5,7]

Explanation:

- The prime numbers less than or equal to n = 15 are 2, 3, 5, 7, 11, and 13.
- One possible selection is [3, 5, 7], whose sum is 3 + 5 + 7 = 15.
- The four smallest available primes have sum 2 + 3 + 5 + 7 = 17, which
  exceeds s = 15. Therefore, selecting four or more primes is impossible, and
  the maximum number of selected primes is three.
- Thus, [3, 5, 7] is one possible answer. Other valid answers include
  [2, 3, 5], [2, 3, 7], and [2, 5, 7].

Example 3:

Input: n = 5, s = 1

Output: []

Explanation:

- The smallest prime number is 2, which is greater than s = 1.
- Thus, no prime can be selected, so the answer is an empty array.


Constraints:

1 <= n <= 10^6
1 <= s <= 10^9

"""

# V0
# IDEA : SIEVE OF ERATOSTHENES + GREEDY (TAKE THE SMALLEST PRIMES FIRST)
#
#   to maximise HOW MANY primes fit under the budget s, always spend the
#   budget on the cheapest ones : the k smallest primes have the smallest
#   possible sum of any k primes, so if they do not fit, no k primes fit.
#
#   (a sliding window over the sorted primes, as in the draft, ends up here
#   too : for every window length the cheapest window is the prefix, so the
#   left pointer never needs to move.)
#
#   e.g. n = 15, s = 15 -> primes 2,3,5,7,... -> 2+3+5 = 10, +7 = 17 > 15
#        -> [2,3,5]   (any 3 primes summing <= 15 is accepted)
#
# time = O(n log log n), space = O(n)
class Solution(object):
    def maxPrimes(self, n, s):
        """
        :type n: int
        :type s: int
        :rtype: List[int]
        """
        # edge case: no prime <= 1
        if n < 2:
            return []

        # is_prime[x] -> whether x is prime, for 0 <= x <= n
        is_prime = [True] * (n + 1)
        is_prime[0] = False
        is_prime[1] = False

        i = 2
        while i * i <= n:
            if is_prime[i]:
                # every multiple of i from i*i on is composite
                # (smaller multiples were already crossed out by smaller primes)
                for multiple in range(i * i, n + 1, i):
                    is_prime[multiple] = False
            i += 1

        # greedy : walk the primes in increasing order, take while it fits
        selected = []
        total = 0
        for x in range(2, n + 1):
            if not is_prime[x]:
                continue
            # NOTE !!! primes only get bigger, so the first one that does not
            #          fit means none of the later ones fit either
            if total + x > s:
                break
            selected.append(x)
            total += x

        return selected


# V1
# IDEA : TRIAL DIVISION BY THE PRIMES FOUND SO FAR + GREEDY, STOP EARLY
#
#   same greedy as V0, but primes are generated one by one in increasing
#   order instead of sieving all of [2, n] up front : a candidate is prime
#   iff no already-found prime p with p * p <= candidate divides it.
#
#   because the selected primes ARE the found primes, the loop stops as soon
#   as the budget runs out. the sum of primes up to x grows like x^2, so with
#   s <= 10^9 we never test candidates much beyond ~1.6 * 10^5, even when
#   n = 10^6.
#
#   NOTE : V0 is the one to write in an interview ; V1 shows why the early
#          stop matters when s is small relative to n
#
# time = O(m * sqrt(m) / log m) with m = min(n, largest prime that still fits), space = O(m / log m)
class Solution2(object):
    def maxPrimes(self, n, s):
        """
        :type n: int
        :type s: int
        :rtype: List[int]
        """
        selected = []
        total = 0

        candidate = 2
        while candidate <= n:
            if self.is_prime(candidate, selected):
                # the budget ran out : every later prime is bigger, stop
                if total + candidate > s:
                    break
                selected.append(candidate)
                total += candidate
            candidate += 1

        return selected

    def is_prime(self, candidate, primes):
        """True if no prime <= sqrt(candidate) in `primes` divides candidate."""
        for p in primes:
            if p * p > candidate:
                break
            if candidate % p == 0:
                return False
        return True
