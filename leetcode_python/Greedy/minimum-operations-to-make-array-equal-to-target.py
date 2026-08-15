"""

3229. Minimum Operations to Make Array Equal to Target
Hard

You are given two positive integer arrays nums and target, of the same length.

In a single operation, you can select any subarray of nums and increment or decrement each element within that subarray by 1.

Return the minimum number of operations required to make nums equal to the array target.


Example 1:

Input: nums = [3,5,1,2], target = [4,6,2,4]
Output: 2
Explanation:
We will perform the following operations to make nums equal to target:
- Increment nums[0..3] by 1, nums = [4,6,2,3].
- Increment nums[3..3] by 1, nums = [4,6,2,4].

Example 2:

Input: nums = [1,3,2], target = [2,1,4]
Output: 5
Explanation:
We will perform the following operations to make nums equal to target:
- Increment nums[0..0] by 1, nums = [2,3,2].
- Decrement nums[1..1] by 1, nums = [2,2,2].
- Decrement nums[1..1] by 1, nums = [2,1,2].
- Increment nums[2..2] by 1, nums = [2,1,3].
- Increment nums[2..2] by 1, nums = [2,1,4].


Constraints:

1 <= nums.length == target.length <= 10^5
1 <= nums[i], target[i] <= 10^8

"""

# V0
# IDEA : WORK ON THE DIFFERENCE ARRAY — ONLY THE *NEW* DEMAND COSTS ANYTHING
#
#   let d[i] = target[i] - nums[i]. a subarray operation shifts a whole
#   stretch by 1, so the cost is really about how the demand CHANGES from one
#   position to the next : an operation covering positions i..j can serve
#   both as long as the demands point the same way.
#
#   walking left to right :
#       d[0] costs abs(d[0]) — nothing before it to piggyback on
#       if d[i] keeps the sign of d[i-1], only the EXCESS costs extra,
#           max(0, abs(d[i]) - abs(d[i-1]))
#       if the sign flips, none of the previous operations can be reused, so
#           the full abs(d[i]) is paid
#
# time = O(n), space = O(1)
class Solution(object):
    def minimumOperations(self, nums, target):
        n = len(nums)
        prev = target[0] - nums[0]
        res = abs(prev)
        for i in range(1, n):
            cur = target[i] - nums[i]
            if (cur > 0 and prev > 0) or (cur < 0 and prev < 0):
                if abs(cur) > abs(prev):
                    res += abs(cur) - abs(prev)
            else:
                res += abs(cur)
            prev = cur
        return res
