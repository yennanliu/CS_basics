"""

1464. Maximum Product of Two Elements in an Array
Easy

Given the array of integers nums, you will choose two different indices i and j of that array. Return the maximum value of (nums[i]-1)*(nums[j]-1).


Example 1:

Input: nums = [3,4,5,2]
Output: 12
Explanation: If you choose the indices i=1 and j=2 (indexed from 0), you will get the maximum value, that is, (nums[1]-1)*(nums[2]-1) = (4-1)*(5-1) = 3*4 = 12.

Example 2:

Input: nums = [1,5,4,5]
Output: 16
Explanation: Choosing the indices i=1 and j=3 (indexed from 0), you will get the maximum value of (5-1)*(5-1) = 16.

Example 3:

Input: nums = [3,7]
Output: 12


Constraints:

2 <= nums.length <= 500
1 <= nums[i] <= 10^3

"""

# V0
# IDEA : TRACK THE TWO LARGEST VALUES (all values are positive)
#
#   nums[i] >= 1, so (nums[i]-1) is never negative and the product is
#   maximised by the two biggest entries -> no need to sort or to worry
#   about a "two most negative" case.
#   keep the largest (a) and second largest (b) in one pass.
#   NOTE : duplicates are fine, the two picks only need different indices.
#
# time = O(n), space = O(1)
class Solution(object):
    def maxProduct(self, nums):
        a = b = 0                     # a = largest, b = second largest
        for x in nums:
            if x > a:
                a, b = x, a
            elif x > b:
                b = x
        return (a - 1) * (b - 1)
