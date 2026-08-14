"""

2448. Minimum Cost to Make Array Equal
Hard

You are given two 0-indexed arrays nums and cost consisting each of n positive integers.

You can do the following operation any number of times:

Increase or decrease any element of the array nums by 1.

The cost of doing one operation on the ith element is cost[i].

Return the minimum total cost such that all the elements of the array nums become equal.


Example 1:

Input: nums = [1,3,5,2], cost = [2,3,1,14]
Output: 8
Explanation: We can make all the elements equal to 2 in the following way:
- Increase the 0th element one time. The cost is 2.
- Decrease the 1st element one time. The cost is 3.
- Decrease the 2nd element three times. The cost is 1 + 1 + 1 = 3.
The total cost is 2 + 3 + 3 = 8.
It can be shown that we cannot make the array equal with a smaller cost.

Example 2:

Input: nums = [2,2,2,2,2], cost = [4,2,8,1,3]
Output: 0
Explanation: All the elements are already equal, so no operations are needed.


Constraints:

n == nums.length == cost.length
1 <= n <= 10^5
1 <= nums[i], cost[i] <= 10^6
Test cases are generated in a way that the output doesn't exceed 2^53-1

"""

# V0
# IDEA : WEIGHTED MEDIAN (f(x) = sum cost[i] * |nums[i] - x| is convex piecewise linear)
#
#   think of nums[i] as a point repeated cost[i] times. minimizing the sum of
#   weighted absolute deviations is the classic median problem, so the optimum
#   x is the WEIGHTED MEDIAN of nums with weights cost.
#
#   sort by value, walk while accumulating weight, and stop at the first value
#   whose running weight passes half of the total weight - that value is an
#   optimal x. then just evaluate f(x) once.
#
#   NOTE : the optimum is always attained at one of the nums[i], because the
#          slope of f only changes at those breakpoints.
#
# time = O(n log n), space = O(n)
class Solution(object):
    def minCost(self, nums, cost):
        pairs = sorted(zip(nums, cost))

        half = sum(cost) / 2.0
        run = 0
        best = pairs[-1][0]
        for v, c in pairs:
            run += c
            if run >= half:
                best = v
                break

        return sum(abs(v - best) * c for v, c in pairs)
