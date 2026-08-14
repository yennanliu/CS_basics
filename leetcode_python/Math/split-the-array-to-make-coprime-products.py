"""

2584. Split the Array to Make Coprime Products
Hard

You are given a 0-indexed integer array nums of length n.

A split at an index i where 0 <= i <= n - 2 is called valid if the product of the first i + 1
elements and the product of the remaining elements are coprime.

For example, if nums = [2, 3, 3], then a split at the index i = 0 is valid because 2 and 9 are
coprime, while a split at the index i = 1 is not valid because 6 and 3 are not coprime.
A split at the index i = 2 is not valid because i == n - 1.

Return the smallest index i at which the array can be split validly or -1 if there is no such split.

Two values val1 and val2 are coprime if gcd(val1, val2) == 1 where gcd(val1, val2) is the greatest
common divisor of val1 and val2.


Example 1:

Input: nums = [4,7,8,15,3,5]
Output: 2
Explanation: The table above shows the values of the product of the first i + 1 elements,
the remaining elements, and their gcd at each index i.
The only valid split is at index 2.

Example 2:

Input: nums = [4,7,15,8,3,5]
Output: -1
Explanation: The table above shows the values of the product of the first i + 1 elements,
the remaining elements, and their gcd at each index i.
There is no valid split.


Constraints:

n == nums.length
1 <= n <= 10^4
1 <= nums[i] <= 10^6

"""

# V0
# IDEA : PRIME FACTORIZATION -> INTERVAL MERGING (sweep the furthest reach)
#
#   the two products share a factor iff some PRIME p divides a number on the
#   left AND a number on the right. So for each prime p, look at the indices
#   where it appears: first[p] .. last[p]. Every split strictly inside that
#   window is invalid — i.e. p "glues" the whole interval [first[p], last[p]]
#   together, exactly like the merge-intervals pattern.
#
#   NOTE : we never build the actual products (they'd be astronomically large);
#          gcd is decided purely by the prime supports.
#
#   so : reach[i] = max last[p] over primes p whose FIRST appearance is at i
#        (defaults to i). Sweep i from left, keeping mx = max reach so far.
#        The first i (i <= n - 2) with mx == i is the answer — at that point no
#        glued interval crosses the boundary between i and i + 1.
#
#   NOTE : mx is monotone and always >= i, so a prime that started BEFORE i and
#          ends after i has already pushed mx past i — that is why only tracking
#          the running max is enough.
#
#   NOTE : the loop stops at n - 2 because a split at the last index is invalid
#          by definition (the right part would be empty).
#
#   factorization : trial-divide by primes up to 1000 (sqrt of the 10^6 cap);
#   whatever remains > 1 is itself a prime factor.
#
# time = O(n * P + n) with P = 168 primes below 1000, space = O(n + distinct primes)
class Solution(object):
    def findValidSplit(self, nums):
        n = len(nums)
        if n < 2:
            return -1

        # primes up to 1000 (enough to fully factor any value <= 10^6)
        LIMIT = 1000
        sieve = [True] * (LIMIT + 1)
        sieve[0] = sieve[1] = False
        for p in range(2, int(LIMIT ** 0.5) + 1):
            if sieve[p]:
                for q in range(p * p, LIMIT + 1, p):
                    sieve[q] = False
        primes = [p for p in range(2, LIMIT + 1) if sieve[p]]

        first = {}
        reach = list(range(n))

        def mark(p, i):
            if p in first:
                # p already seen -> it glues first[p] .. i together
                if reach[first[p]] < i:
                    reach[first[p]] = i
            else:
                first[p] = i

        for i, x in enumerate(nums):
            for p in primes:
                if p * p > x:
                    break
                if x % p == 0:
                    mark(p, i)
                    while x % p == 0:
                        x //= p
            if x > 1:
                mark(x, i)

        mx = 0
        for i in range(n - 1):
            if reach[i] > mx:
                mx = reach[i]
            if mx == i:
                return i
        return -1
