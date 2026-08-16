"""

3511. Make a Positive Array
Medium

You are given an array nums. An array is considered positive if the sum of all
numbers in each subarray with more than two elements is positive.

You can perform the following operation any number of times:

Replace one element in nums with any integer between -10^18 and 10^18.

Find the minimum number of operations needed to make nums positive.

Example 1:

Input: nums = [-10,15,-12]

Output: 1

Explanation:

The only subarray with more than 2 elements is the array itself. The sum of all
elements is (-10) + 15 + (-12) = -7. By replacing nums[0] with 0, the new sum
becomes 0 + 15 + (-12) = 3. Thus, the array is now positive.

Example 2:

Input: nums = [-1,-2,3,-1,2,6]

Output: 1

Explanation:

The only subarrays with more than 2 elements and a non-positive sum are:

Subarray Indices | Subarray        | Sum | After Replacement | New Sum
                 |                 |     | (Set nums[1] = 1) |
-----------------+-----------------+-----+-------------------+--------
nums[0...2]      | [-1, -2, 3]     |   0 | [-1, 1, 3]        |       3
nums[0...3]      | [-1, -2, 3, -1] |  -1 | [-1, 1, 3, -1]    |       2
nums[1...3]      | [-2, 3, -1]     |   0 | [1, 3, -1]        |       3

Thus, nums is positive after one operation.

Example 3:

Input: nums = [1,2,3]

Output: 0

Explanation:

The array is already positive, so no operations are needed.

Constraints:

3 <= nums.length <= 10^5

-10^9 <= nums[i] <= 10^9

"""

# V0
# IDEA : TURN IT INTO INTERVAL STABBING, THEN SWEEP WITH A MONOTONIC DEQUE
#
#   a replaced element may be set to 10^18, while the untouched part of the
#   array can only sum to about 10^14 in magnitude (10^5 values of size 10^9).
#   so a single replacement makes *every* subarray containing it positive, and
#   replacing with anything smaller is never better.  the problem collapses to:
#   pick the fewest indices such that every subarray of length >= 3 whose sum
#   is <= 0 contains one of them -- a minimum stabbing set for intervals.
#
#   the classic greedy for that is: sweep the right endpoint, and whenever an
#   unstabbed bad interval finishes, put a marker on its right endpoint, which
#   is the position that stays useful the longest.
#
#   with prefix sums P, the interval [l, r] is bad when P[r+1] <= P[l].  after
#   the last marker at `last` only l > last matters, and length >= 3 forces
#   l <= r - 2, so at each r the test is
#       max{ P[l] : last < l <= r - 2 } >= P[r + 1].
#   both ends of that window only move forward, so a decreasing-prefix-max
#   deque answers it in O(1) amortised.
#
# time = O(n), space = O(n)
from collections import deque


class Solution(object):
    def makeArrayPositive(self, nums):
        n = len(nums)
        pre = [0] * (n + 1)
        for i, v in enumerate(nums):
            pre[i + 1] = pre[i] + v

        dq = deque()          # indices l with strictly decreasing pre[l]
        last = -1             # index of the most recent replacement
        ops = 0
        for r in range(n):
            l = r - 2
            if l >= 0:        # pre[l] becomes eligible once the window reaches it
                while dq and pre[dq[-1]] <= pre[l]:
                    dq.pop()
                dq.append(l)
            while dq and dq[0] <= last:
                dq.popleft()
            if dq and pre[dq[0]] >= pre[r + 1]:
                ops += 1
                last = r
                dq.clear()
        return ops
