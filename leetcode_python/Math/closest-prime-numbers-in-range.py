"""

2523. Closest Prime Numbers in Range
Medium

Given two positive integers left and right, find the two integers num1 and num2
such that:

left <= num1 < num2 <= right.
Both num1 and num2 are prime numbers.
num2 - num1 is the minimum amongst all other pairs satisfying the above conditions.

Return the positive integer array ans = [num1, num2]. If there are multiple pairs
satisfying these conditions, return the one with the smallest num1 value.
If no such numbers exist, return [-1, -1].


Example 1:

Input: left = 10, right = 19
Output: [11,13]
Explanation: The prime numbers between 10 and 19 are 11, 13, 17, and 19.
The closest gap between any pair is 2, which can be achieved by [11,13] or [17,19].
Since 11 is smaller than 17, we return the first pair.

Example 2:

Input: left = 4, right = 6
Output: [-1,-1]
Explanation: There exists only one prime number in the given range, so the
conditions cannot be satisfied.


Constraints:

1 <= left <= right <= 10^6

"""

# V0
# IDEA : SIEVE OF ERATOSTHENES + ADJACENT PAIR SCAN
#
#   sieve every prime up to `right` (right <= 10^6, so a plain sieve is fine),
#   keep only the ones inside [left, right] in increasing order, then compare
#   CONSECUTIVE primes -- the closest pair overall must be an adjacent pair in
#   the sorted list, so a single linear pass suffices.
#
#   NOTE : use a strict "<" when updating the best gap so that ties keep the
#          FIRST (smallest num1) pair, as the problem requires.
#
#   NOTE : the smallest twin-prime-ish gap is 1 only for (2, 3); once a gap of
#          1 or 2 is found we could early-exit, but the linear scan is already
#          cheap compared to the sieve.
#
#   NOTE : 0 and 1 are not prime -- mark them out before sieving.
#
# time = O(right * log log right), space = O(right)
class Solution(object):
    def closestPrimes(self, left, right):
        if right < 2:
            return [-1, -1]

        is_prime = [True] * (right + 1)
        is_prime[0] = False
        if right >= 1:
            is_prime[1] = False
        i = 2
        while i * i <= right:
            if is_prime[i]:
                for j in range(i * i, right + 1, i):
                    is_prime[j] = False
            i += 1

        res = [-1, -1]
        best = float('inf')
        prev = -1
        for v in range(max(left, 2), right + 1):
            if not is_prime[v]:
                continue
            if prev != -1 and v - prev < best:
                best = v - prev
                res = [prev, v]
            prev = v
        return res
