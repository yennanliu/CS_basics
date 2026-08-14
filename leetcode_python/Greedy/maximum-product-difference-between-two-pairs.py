"""

1913. Maximum Product Difference Between Two Pairs
Easy

The product difference between two pairs (a, b) and (c, d) is defined as (a * b) - (c * d).

For example, the product difference between (5, 6) and (2, 7) is (5 * 6) - (2 * 7) = 16.

Given an integer array nums, choose four distinct indices w, x, y, and z such that the product difference between pairs (nums[w], nums[x]) and (nums[y], nums[z]) is maximized.

Return the maximum such product difference.


Example 1:

Input: nums = [5,6,2,7,4]
Output: 34
Explanation: We can choose indices 1 and 3 for the first pair (6, 7) and indices 2 and 4 for the second pair (2, 4).
The product difference is (6 * 7) - (2 * 4) = 34.

Example 2:

Input: nums = [4,2,5,9,7,4,8]
Output: 64
Explanation: We can choose indices 3 and 6 for the first pair (9, 8) and indices 1 and 5 for the second pair (2, 4).
The product difference is (9 * 8) - (2 * 4) = 64.


Constraints:

4 <= nums.length <= 10^4
1 <= nums[i] <= 10^4

"""

# V0
# IDEA : GREEDY (all values are positive -> take the 2 largest & the 2 smallest)
#
#   (a*b) - (c*d) is maximised by maximising a*b and minimising c*d.
#   since 1 <= nums[i], the product of the two biggest values is the max product
#   and the product of the two smallest values is the min product.
#
#   NOTE : a single linear pass tracking (max1, max2, min1, min2) avoids the
#          O(n log n) sort.
#
# time = O(n), space = O(1)
class Solution(object):
    def maxProductDifference(self, nums):
        big1 = big2 = 0                 # big1 >= big2
        small1 = small2 = float('inf')  # small1 <= small2
        for x in nums:
            if x > big1:
                big1, big2 = x, big1
            elif x > big2:
                big2 = x

            if x < small1:
                small1, small2 = x, small1
            elif x < small2:
                small2 = x
        return big1 * big2 - small1 * small2
