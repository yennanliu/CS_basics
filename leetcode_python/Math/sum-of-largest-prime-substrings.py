"""

3556. Sum of Largest Prime Substrings
Medium

Given a string s, find the sum of the 3 largest unique prime numbers that can be
formed using any of its substrings.

Return the sum of the three largest unique prime numbers that can be formed. If
fewer than three exist, return the sum of all available primes. If no prime
numbers can be formed, return 0.

Note: Each prime number should be counted only once, even if it appears in
multiple substrings. Additionally, when converting a substring to an integer,
any leading zeros are ignored.

Example 1:

Input: s = "12234"

Output: 1469

Explanation:

The unique prime numbers formed from the substrings of "12234" are 2, 3, 23,
223, and 1223.

The 3 largest primes are 1223, 223, and 23. Their sum is 1469.

Example 2:

Input: s = "111"

Output: 11

Explanation:

The unique prime number formed from the substrings of "111" is 11.

Since there is only one prime number, the sum is 11.

Constraints:

1 <= s.length <= 10

s consists of only digits.

"""

# V0
# IDEA : ENUMERATE ALL SUBSTRINGS, DEDUPLICATE, TAKE THE TOP THREE
#
#   the string has at most 10 characters, so there are at most 55 substrings —
#   small enough to test every one for primality by trial division up to the
#   square root (values stay under 10^10).
#
#   the substrings are put into a set first, which handles both "count each
#   prime once" and the leading-zero rule (int("07") == 7 collapses onto 7).
#
# time = O(n^2 * sqrt(10^n)) with n <= 10, space = O(n^2)
class Solution(object):
    def sumOfLargestPrimes(self, s):
        def is_prime(x):
            if x < 2:
                return False
            if x < 4:
                return True
            if x % 2 == 0:
                return False
            f = 3
            while f * f <= x:
                if x % f == 0:
                    return False
                f += 2
            return True

        n = len(s)
        primes = set()
        for i in range(n):
            for j in range(i + 1, n + 1):
                v = int(s[i:j])
                if is_prime(v):
                    primes.add(v)
        return sum(sorted(primes, reverse=True)[:3])
