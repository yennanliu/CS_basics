"""

1250. Check If It Is a Good Array
Hard

Given an array nums of positive integers. Your task is to select some subset of nums, multiply each element by an integer and add all these numbers. The array is said to be good if you can obtain a sum of 1 from the array by any possible subset and multiplicand.

Return True if the array is good otherwise return False.


Example 1:

Input: nums = [12,5,7,23]
Output: true
Explanation: Pick numbers 5 and 7.
5*3 + 7*(-2) = 1

Example 2:

Input: nums = [29,6,10]
Output: true
Explanation: Pick numbers 29, 6 and 10.
29*1 + 6*(-3) + 10*(-1) = 1

Example 3:

Input: nums = [3,6]
Output: false


Constraints:

1 <= nums.length <= 10^5
1 <= nums[i] <= 10^9

"""

# V0
# IDEA : MATH - BEZOUT'S IDENTITY
"""
 Bezout: for integers a1..ak there exist integers x1..xk with
     a1*x1 + ... + ak*xk = gcd(a1, ..., ak)
 and gcd is the SMALLEST positive value obtainable.

 -> a sum of 1 is reachable  <=>  gcd(nums) == 1

 (adding more numbers can only keep or shrink the gcd, so we may
  just take the gcd of the WHOLE array instead of hunting subsets)
"""
# time = O(n log m), m = max(nums)
# space = O(1)
class Solution(object):
    def isGoodArray(self, nums):
        g = 0
        for x in nums:
            # inline euclid, so this works on py2 / py3 alike
            a, b = x, g
            while b:
                a, b = b, a % b
            g = a
            if g == 1:
                return True
        return g == 1
