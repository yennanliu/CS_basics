"""

2761. Prime Pairs With Target Sum
Medium

You are given an integer n. We say that two integers x and y form a prime number pair if:

1 <= x <= y <= n
x + y == n
x and y are prime numbers

Return the 2D sorted list of prime number pairs [xi, yi]. The list should be sorted in increasing order of xi. If there are no prime number pairs at all, return an empty array.

Note: A prime number is a natural number greater than 1 with only two factors, itself and 1.


Example 1:

Input: n = 10
Output: [[3,7],[5,5]]
Explanation: In this example, there are two prime pairs that satisfy the criteria.
These pairs are [3,7] and [5,5], and we return them in the sorted order as described in the problem statement.

Example 2:

Input: n = 2
Output: []
Explanation: We can show that there is no prime number pair that gives a sum of 2, so we return an empty array.


Constraints:

1 <= n <= 10^6

"""

# V0
# IDEA : SIEVE OF ERATOSTHENES + ENUMERATE THE SMALLER PRIME
#
#  x <= y and x + y == n means x ranges over [2, n // 2] only, and y = n - x
#  is then fully determined -> one pass once we can test primality in O(1).
#
#  so: sieve every prime up to n first, then walk x upward. Because x only
#  increases, the pairs come out already sorted -> no final sort needed.
#
#   NOTE : n goes up to 10^6, so the sieve must be the real thing; marking the
#          multiples with a SLICE assignment (`sieve[i*i::i] = [False] * ...`)
#          keeps the inner loop in C and is what makes 10^6 comfortable.
#   NOTE : start crossing out at i * i -- smaller multiples of i already got
#          removed by a smaller prime factor.
#   NOTE : 0 and 1 are not prime; also guard n < 4 so the sieve list is never
#          indexed out of range (n = 1, 2, 3 simply have no pair).
#
# time = O(n * log log n), space = O(n)
class Solution(object):
    def findPrimePairs(self, n):
        if n < 4:
            return []

        sieve = [True] * (n + 1)
        sieve[0] = sieve[1] = False
        i = 2
        while i * i <= n:
            if sieve[i]:
                sieve[i * i::i] = [False] * len(sieve[i * i::i])
            i += 1

        res = []
        for x in range(2, n // 2 + 1):
            if sieve[x] and sieve[n - x]:
                res.append([x, n - x])
        return res
