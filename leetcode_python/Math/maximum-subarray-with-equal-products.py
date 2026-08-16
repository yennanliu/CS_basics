"""

3411. Maximum Subarray With Equal Products
Easy

You are given an array of positive integers nums.

An array arr is called product equivalent if prod(arr) == lcm(arr) * gcd(arr),
where:

prod(arr) is the product of all elements of arr.
gcd(arr) is the GCD of all elements of arr.
lcm(arr) is the LCM of all elements of arr.

Return the length of the longest product equivalent subarray of nums.

Example 1:

Input: nums = [1,2,1,2,1,1,1]

Output: 5

Explanation:

The longest product equivalent subarray is [1, 2, 1, 1, 1], where prod([1, 2, 1,
1, 1]) = 2, gcd([1, 2, 1, 1, 1]) = 1, and lcm([1, 2, 1, 1, 1]) = 2.

Example 2:

Input: nums = [2,3,4,5,6]

Output: 3

Explanation:

The longest product equivalent subarray is [3, 4, 5].

Example 3:

Input: nums = [1,2,3,1,4,5,1]

Output: 5

Constraints:

2 <= nums.length <= 100
1 <= nums[i] <= 10

"""

# V0
# IDEA : BRUTE FORCE EVERY SUBARRAY, GROWING PROD / GCD / LCM INCREMENTALLY
#
#   prod == lcm * gcd is a strong condition: for two numbers it is an identity,
#   but from three elements on it forces the "extra" shared factors to vanish.
#   there is no clean local test for it, so we simply evaluate it on every
#   subarray.
#
#   the saving grace is that all three quantities are *foldable*: extending a
#   subarray by one element updates prod, gcd and lcm in O(log) each, so the
#   whole O(n^2) family of subarrays costs one multiply and two gcds per step
#   instead of a fresh O(n) recomputation.
#
#   values are <= 10 and n <= 100, so the products stay tiny.
#
# time = O(n^2 log(max)), space = O(1)
from math import gcd


class Solution(object):
    def maxLength(self, nums):
        n = len(nums)
        ans = 1
        for i in range(n):
            p = 1
            g = 0
            l = 1
            for j in range(i, n):
                v = nums[j]
                p *= v
                g = gcd(g, v)
                l = l * v // gcd(l, v)
                if p == l * g and j - i + 1 > ans:
                    ans = j - i + 1
        return ans
