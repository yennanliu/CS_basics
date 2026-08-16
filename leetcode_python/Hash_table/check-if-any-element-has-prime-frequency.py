"""

3591. Check if Any Element Has Prime Frequency
Easy

You are given an integer array nums.

Return true if the frequency of any element of the array is prime, otherwise, return false.

The frequency of an element x is the number of times it occurs in the array.

A prime number is a natural number greater than 1 with only two factors, 1 and itself.


Example 1:

Input: nums = [1,2,3,4,5,4]
Output: True
Explanation:
4 appears 2 times. 2 is a prime number.

Example 2:

Input: nums = [1,2,3,4,5]
Output: False
Explanation:
All elements appear only once. 1 is not a prime number.

Example 3:

Input: nums = [2,2,2,4,4]
Output: True
Explanation:
Both 2 and 4 appear 3 and 2 times respectively. Both 3 and 2 are prime numbers.


Constraints:

1 <= nums.length <= 100
0 <= nums[i] <= 100

"""

# V0
# IDEA : COUNT, THEN TRIAL-DIVIDE THE COUNTS
#
#   frequencies are bounded by the array length (100 here), so a plain
#   trial-division primality test on each distinct frequency is more than
#   fast enough — no sieve or caching needed.
#
# time = O(n * sqrt(n)), space = O(n)
from collections import Counter


class Solution(object):
    def checkPrimeFrequency(self, nums):
        def is_prime(x):
            if x < 2:
                return False
            d = 2
            while d * d <= x:
                if x % d == 0:
                    return False
                d += 1
            return True

        return any(is_prime(c) for c in Counter(nums).values())
