"""

2614. Prime In Diagonal
Easy

You are given a 0-indexed two-dimensional integer array nums.

Return the largest prime number that lies on at least one of the diagonals of
nums. In case, no prime is present on any of the diagonals, return 0.

Note that:

An integer is prime if it is greater than 1 and has no positive integer
divisors other than 1 and itself.

An integer val is on one of the diagonals of nums if there exists an integer i
for which nums[i][i] = val or an i for which nums[i][nums.length - i - 1] = val.

In the above diagram, one diagonal is [1,5,9] and another diagonal is [3,5,7].


Example 1:

Input: nums = [[1,2,3],[5,6,7],[9,10,11]]

Output: 11

Explanation: The numbers 1, 3, 6, 9, and 11 are the only numbers present on at
least one of the diagonals. Since 11 is the largest prime, we return 11.

Example 2:

Input: nums = [[1,2,3],[5,17,7],[9,11,10]]

Output: 17

Explanation: The numbers 1, 3, 9, 10, and 17 are all present on at least one
of the diagonals. 17 is the largest prime, so we return 17.


Constraints:

1 <= nums.length <= 300

nums.length == nums[i].length

1 <= nums[i][j] <= 4 * 10^6

"""

# V0
# IDEA : DIAGONAL WALK + TRIAL DIVISION UP TO sqrt(x)
#
#   only 2n cells matter: row i holds one cell of each diagonal,
#     main diagonal -> (i, i)
#     anti diagonal -> (i, n - 1 - i)     (row + col == n - 1)
#   so one loop over the rows visits both.
#
#   a composite x always has a divisor d with d * d <= x, so testing
#   d = 2, 3, ... while d * d <= x is enough to prove x prime.
#
#   e.g. [[1,2,3],[5,6,7],[9,10,11]] -> main [1,6,11], anti [3,6,9]
#        -> primes 3, 11 -> answer 11
#
# time = O(n * sqrt(M)), space = O(1)    (M = largest value, 4 * 10^6)
class Solution(object):
    def diagonalPrime(self, nums):
        """
        :type nums: List[List[int]]
        :rtype: int
        """
        n = len(nums)
        largest_prime = 0

        for i in range(n):
            main_val = nums[i][i]
            # NOTE !!! the anti diagonal's column is n - 1 - i, not n - i
            anti_val = nums[i][n - 1 - i]

            # only pay for the primality test when the value could win
            # (on an odd n the centre cell is checked twice, which is harmless)
            for val in (main_val, anti_val):
                if val > largest_prime and self.is_prime(val):
                    largest_prime = val

        return largest_prime

    def is_prime(self, x):
        """Trial division: x is prime iff no d in [2, sqrt(x)] divides it."""
        if x < 2:
            return False
        divisor = 2
        while divisor * divisor <= x:
            if x % divisor == 0:
                return False
            divisor += 1
        return True


# V1
# IDEA : SIEVE OF ERATOSTHENES UP TO THE LARGEST DIAGONAL VALUE
#
#   mark every multiple of each prime p (starting at p * p, since smaller
#   multiples were already marked by a smaller prime) as composite; whatever
#   is left unmarked is prime. then every diagonal value is an O(1) lookup.
#
#   NOTE : worse than V0 here - only 2n <= 600 values are tested, but the
#          sieve pays for all M = 4 * 10^6 of them. it wins when many values
#          need testing; learn V0 first for this problem.
#
# time = O(M log log M + n), space = O(M)
class Solution2(object):
    def diagonalPrime(self, nums):
        """
        :type nums: List[List[int]]
        :rtype: int
        """
        n = len(nums)

        # collect both diagonals first, so the sieve is only as big as needed
        diagonal_vals = []
        for i in range(n):
            diagonal_vals.append(nums[i][i])
            diagonal_vals.append(nums[i][n - 1 - i])

        limit = max(diagonal_vals)
        if limit < 2:
            return 0

        # is_prime[x] == 1 means x has not been marked composite
        is_prime = bytearray([1]) * (limit + 1)
        is_prime[0] = 0
        is_prime[1] = 0

        p = 2
        while p * p <= limit:
            if is_prime[p]:
                # p is prime -> every multiple from p * p upward is composite
                for multiple in range(p * p, limit + 1, p):
                    is_prime[multiple] = 0
            p += 1

        largest_prime = 0
        for val in diagonal_vals:
            if is_prime[val] and val > largest_prime:
                largest_prime = val

        return largest_prime
