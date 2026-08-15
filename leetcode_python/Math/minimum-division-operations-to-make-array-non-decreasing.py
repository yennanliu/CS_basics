"""

3326. Minimum Division Operations to Make Array Non Decreasing
Medium

You are given an integer array nums.

Any positive divisor of a natural number x that is strictly less than x is called a proper divisor of x. For example, 2 is a proper divisor of 4, while 6 is not a proper divisor of 6.

You are allowed to perform an operation any number of times on nums, where in each operation you select any one element from nums and divide it by its greatest proper divisor.

Return the minimum number of operations required to make the array non-decreasing.

If it is not possible to make the array non-decreasing using any number of operations, return -1.


Example 1:

Input: nums = [25,7]
Output: 1
Explanation:
Using a single operation, 25 gets divided by 5 and nums becomes [5, 7].

Example 2:

Input: nums = [7,7,6]
Output: -1

Example 3:

Input: nums = [1,1,1,1]
Output: 0


Constraints:

1 <= nums.length <= 10^5
1 <= nums[i] <= 10^6

"""

# V0
# IDEA : DIVIDING BY THE GREATEST PROPER DIVISOR LEAVES THE SMALLEST PRIME FACTOR
#
#   for x > 1 the greatest proper divisor is x / spf(x), so one operation
#   turns x into its smallest prime factor — and a second operation is
#   impossible, since a prime has no proper divisor above 1 (dividing by 1
#   changes nothing). so every element has exactly two possible values :
#   itself, or spf(x).
#
#   sweeping RIGHT TO LEFT makes the choice forced : keep nums[i] if it
#   already fits under its right neighbour, otherwise spend one operation and
#   drop to spf. if even that is too big, the array cannot be fixed.
#
#   a linear sieve precomputes spf for every value up to 10^6.
#
# time = O(max value log log max + n), space = O(max value)
class Solution(object):
    def minOperations(self, nums):
        top = max(nums)
        spf = list(range(top + 1))
        p = 2
        while p * p <= top:
            if spf[p] == p:                     # p is prime
                for m in range(p * p, top + 1, p):
                    if spf[m] == m:
                        spf[m] = p
            p += 1

        res = 0
        limit = float('inf')                    # value of the element to the right
        for i in range(len(nums) - 1, -1, -1):
            v = nums[i]
            if v <= limit:
                limit = v
                continue
            small = spf[v]
            if small > limit:
                return -1
            res += 1
            limit = small
        return res
