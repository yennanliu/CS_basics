"""

1390. Four Divisors
Medium

Given an integer array nums, return the sum of divisors of the integers in that array that have
exactly four divisors. If there is no such integer in the array, return 0.


Example 1:

Input: nums = [21,4,7]
Output: 32
Explanation:
21 has 4 divisors: 1, 3, 7, 21
4 has 3 divisors: 1, 2, 4
7 has 2 divisors: 1, 7
The answer is the sum of divisors of 21 only.

Example 2:

Input: nums = [21,21]
Output: 64

Example 3:

Input: nums = [1,2,3,4,5]
Output: 0


Constraints:

1 <= nums.length <= 10^4
1 <= nums[i] <= 10^5

"""

# V0
# IDEA : TRIAL DIVISION up to sqrt(x), stop as soon as the 4th divisor appears
#
#   divisors come in pairs (d, x // d), so scanning d from 2 to sqrt(x) finds
#   them all. Start with the two trivial divisors 1 and x already counted
#   (sum = x + 1) and add each new pair.
#
#   NOTE : when d * d == x, d and x // d are the SAME divisor -- count it once,
#          otherwise perfect squares are over-counted.
#   NOTE : bail out once the divisor count exceeds 4 -- such x contributes
#          nothing and there is no point enumerating further.
#
# time = O(n * sqrt(m)), space = O(1)   m = max(nums)
class Solution(object):
    def sumFourDivisors(self, nums):
        def divisor_sum(x):
            cnt, total = 2, x + 1   # 1 and x itself
            d = 2
            while d * d <= x:
                if x % d == 0:
                    cnt += 1
                    total += d
                    if d * d != x:
                        cnt += 1
                        total += x // d
                    if cnt > 4:
                        return 0
                d += 1
            return total if cnt == 4 else 0

        return sum(divisor_sum(x) for x in nums)
