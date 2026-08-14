"""

2521. Distinct Prime Factors of Product of Array
Medium

Given an array of positive integers nums, return the number of distinct prime
factors in the product of the elements of nums.

Note that:

A number greater than 1 is called prime if it is divisible by only 1 and itself.
An integer val1 is a factor of another integer val2 if val2 / val1 is an integer.


Example 1:

Input: nums = [2,4,3,7,10,6]
Output: 4
Explanation:
The product of all the elements in nums is: 2 * 4 * 3 * 7 * 10 * 6 = 10080 = 2^5 * 3^2 * 5 * 7.
There are 4 distinct prime factors so we return 4.

Example 2:

Input: nums = [2,4,8,16]
Output: 1
Explanation:
The product of all the elements in nums is: 2 * 4 * 8 * 16 = 1024 = 2^10.
There is 1 distinct prime factor so we return 1.


Constraints:

1 <= nums.length <= 10^4
2 <= nums[i] <= 1000

"""

# V0
# IDEA : TRIAL DIVISION + SET
#
#   the set of prime factors of a PRODUCT is exactly the union of the prime
#   factor sets of the individual terms, so we never have to build the (huge)
#   product. Factorize each nums[i] by trial division and dump the primes into
#   a set; the answer is the set size.
#
#   NOTE : trial division only needs divisors up to sqrt(n) -- after stripping
#          all of those, whatever is left of n is either 1 or a single prime
#          bigger than sqrt(n), which must also be recorded.
#
#   NOTE : divide out each factor completely (`while n % i == 0`) so the loop
#          variable only ever meets primes, and so exponents don't matter --
#          we only care about DISTINCT primes.
#
# time = O(n * sqrt(m)), space = O(number of distinct primes <= m)  (m = max(nums))
class Solution(object):
    def distinctPrimeFactors(self, nums):
        primes = set()
        for num in nums:
            n = num
            i = 2
            while i * i <= n:
                if n % i == 0:
                    primes.add(i)
                    while n % i == 0:
                        n //= i
                i += 1
            # leftover > 1 is itself a prime
            if n > 1:
                primes.add(n)
        return len(primes)
