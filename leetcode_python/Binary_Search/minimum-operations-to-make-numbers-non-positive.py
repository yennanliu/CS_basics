"""

2702. Minimum Operations to Make Numbers Non-positive
Hard

You are given a 0-indexed integer array nums and two integers x and y. In one operation, you must choose an index i such that 0 <= i < nums.length and perform the following:

Decrement nums[i] by x.
Decrement values by y at all indices except the ith one.

Return the minimum number of operations to make all the integers in nums less than or equal to zero.


Example 1:

Input: nums = [3,4,1,7,6], x = 4, y = 2
Output: 3
Explanation: You will need three operations. One of the optimal sequence of operations is:
Operation 1: Choose i = 3. Then, nums = [1,2,-1,3,4].
Operation 2: Choose i = 3. Then, nums = [-1,0,-3,-1,2].
Operation 3: Choose i = 4. Then, nums = [-3,-2,-5,-3,-2].
Now all the numbers in nums are non-positive. Therefore, we return 3.

Example 2:

Input: nums = [1,2,1], x = 2, y = 1
Output: 1
Explanation: We can perform the operation once on i = 1. Then, nums becomes [0,0,0].
All the positive numbers are removed, and therefore, we return 1.


Constraints:

1 <= nums.length <= 10^5
1 <= nums[i] <= 10^9
1 <= y < x <= 10^9

"""

# V0
# IDEA : BINARY SEARCH ON THE ANSWER
#
#   suppose we commit to exactly t operations. Every element is hit by y on
#   every operation it is NOT chosen for, so after t operations an element v
#   that was chosen c times ends at  v - c*x - (t - c)*y  =  v - t*y - c*(x-y).
#
#   so with a budget of t operations, element v needs
#       c_v = ceil((v - t*y) / (x - y))     if v > t*y   else   0
#   picks of its own (x > y so x - y > 0). The t picks are shared across all
#   elements, so t operations suffice  <=>  sum(c_v) <= t.
#
#   that predicate is MONOTONE in t (more operations never hurt: t*y grows and
#   x - y is fixed), so binary search the smallest feasible t on [0, max(nums)].
#
#   NOTE : t = max(nums) is always feasible since y >= 1 => t*y >= max(nums),
#          so it is a valid right boundary.
#   NOTE : the reference Python uses float ceil() here - with v, x, y up to
#          10^9 the intermediate t*y reaches ~10^18 and float64 silently loses
#          precision. Use INTEGER ceil-division instead: (a + b - 1) // b.
#
# time = O(n * log(max(nums))), space = O(1)
class Solution(object):
    def minOperations(self, nums, x, y):
        d = x - y

        def feasible(t):
            need = 0
            budget = t * y
            for v in nums:
                if v > budget:
                    need += (v - budget + d - 1) // d
                    if need > t:
                        return False
            return need <= t

        lo, hi = 0, max(nums)
        while lo < hi:
            mid = (lo + hi) // 2
            if feasible(mid):
                hi = mid
            else:
                lo = mid + 1
        return lo
