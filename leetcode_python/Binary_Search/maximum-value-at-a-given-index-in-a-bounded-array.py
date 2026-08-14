"""

1802. Maximum Value at a Given Index in a Bounded Array
Medium

You are given three positive integers: n, index, and maxSum. You want to construct an array nums (0-indexed) that satisfies the following conditions:

nums.length == n
nums[i] is a positive integer where 0 <= i < n.
abs(nums[i] - nums[i+1]) <= 1 where 0 <= i < n-1.
The sum of all the elements of nums does not exceed maxSum.
nums[index] is maximized.

Return nums[index] of the constructed array.

Note that abs(x) equals x if x >= 0, and -x otherwise.


Example 1:

Input: n = 4, index = 2,  maxSum = 6
Output: 2
Explanation: nums = [1,2,2,1] is one array that satisfies all the conditions.
There are no arrays that satisfy all the conditions and have nums[2] == 3, so 2 is the maximum nums[2].

Example 2:

Input: n = 6, index = 1,  maxSum = 10
Output: 3


Constraints:

1 <= n <= maxSum <= 10^9
0 <= index < n

"""

# V0
# IDEA : BINARY SEARCH ON THE ANSWER (feasibility is monotone)
#
#   if nums[index] = v is feasible, then any smaller value is feasible too,
#   so we can binary search v in [1, maxSum].
#
#   the CHEAPEST array with nums[index] = v decreases by 1 per step away from
#   index and then flattens at 1 :  ... 1,1,2,3,v,3,2,1,1 ...
#
#   cost of a side that holds `cnt` cells whose top value is x (x, x-1, ...):
#     - if x >= cnt : full arithmetic run  -> (x + (x-cnt+1)) * cnt / 2
#     - else        : run down to 1 then pad with 1s -> x*(x+1)/2 + (cnt - x)
#
#   NOTE : the left side excludes index itself (top value v-1, cnt = index),
#          the right side INCLUDES index (top value v, cnt = n - index).
#
# time = O(log(maxSum)), space = O(1)
class Solution(object):
    def maxValue(self, n, index, maxSum):
        def cost(x, cnt):
            # sum of cnt cells whose values are x, x-1, ... floored at 1
            if x >= cnt:
                return (x + x - cnt + 1) * cnt // 2
            return (x + 1) * x // 2 + (cnt - x)

        left, right = 1, maxSum
        while left < right:
            mid = (left + right + 1) // 2
            if cost(mid - 1, index) + cost(mid, n - index) <= maxSum:
                left = mid
            else:
                right = mid - 1
        return left
