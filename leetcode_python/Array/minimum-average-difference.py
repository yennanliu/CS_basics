"""

2256. Minimum Average Difference
Medium

You are given a 0-indexed integer array nums of length n.

The average difference of the index i is the absolute difference between the average of the first i + 1 elements of nums and the average of the last n - i - 1 elements. Both averages should be rounded down to the nearest integer.

Return the index with the minimum average difference. If there are multiple such indices, return the smallest one.

Note:

The absolute difference of two numbers is the absolute value of their difference.
The average of n elements is the sum of the elements divided by n.
The average of 0 elements is considered to be 0.


Example 1:

Input: nums = [2,5,3,9,5,3]
Output: 3
Explanation:
- The average difference of index 0 is: |2 / 1 - (5 + 3 + 9 + 5 + 3) / 5| = |2 / 1 - 25 / 5| = |2 - 5| = 3.
- The average difference of index 1 is: |(2 + 5) / 2 - (3 + 9 + 5 + 3) / 4| = |7 / 2 - 20 / 4| = |3 - 5| = 2.
- The average difference of index 2 is: |(2 + 5 + 3) / 3 - (9 + 5 + 3) / 3| = |10 / 3 - 17 / 3| = |3 - 5| = 2.
- The average difference of index 3 is: |(2 + 5 + 3 + 9) / 4 - (5 + 3) / 2| = |19 / 4 - 8 / 2| = |4 - 4| = 0.
- The average difference of index 4 is: |(2 + 5 + 3 + 9 + 5) / 5 - 3 / 1| = |24 / 5 - 3 / 1| = |4 - 3| = 1.
- The average difference of index 5 is: |(2 + 5 + 3 + 9 + 5 + 3) / 6 - 0| = |27 / 6 - 0| = |4 - 0| = 4.
The average difference of index 3 is the minimum average difference so return 3.

Example 2:

Input: nums = [0]
Output: 0
Explanation:
The only index is 0 so return 0.
The average difference of index 0 is: |0 / 1 - 0| = |0 - 0| = 0.


Constraints:

1 <= n <= 10^5
0 <= nums[i] <= 10^5

"""

# V0
# IDEA : RUNNING PREFIX SUM, THE SUFFIX FOLLOWS FROM THE TOTAL
#
#   sweep i left to right maintaining `prefix`; the suffix sum is
#   total - prefix, so both averages are O(1) per index.
#
#   two details from the statement :
#     * both averages are FLOORED before subtracting, so integer division
#       must happen first
#     * the last index has an empty suffix, whose average is defined as 0 —
#       guard against dividing by zero there
#
#   ties go to the smallest index, so only update on a STRICT improvement.
#
# time = O(n), space = O(1)
class Solution(object):
    def minimumAverageDifference(self, nums):
        n = len(nums)
        total = sum(nums)
        prefix = 0
        best_diff = float('inf')
        best_idx = 0
        for i, x in enumerate(nums):
            prefix += x
            left = prefix // (i + 1)
            right = (total - prefix) // (n - i - 1) if i < n - 1 else 0
            diff = abs(left - right)
            if diff < best_diff:
                best_diff = diff
                best_idx = i
        return best_idx


# V0-1
# IDEA : BRUTE FORCE — RE-SUM BOTH SIDES AT EVERY INDEX
#
#   the literal reading of the statement : for each i, add up the first i + 1
#   elements and the remaining n - i - 1 elements from scratch, floor both
#   averages and compare. no prefix trick at all, which makes it the obvious
#   reference implementation to check the linear versions against — but the
#   repeated summing makes it quadratic.
#
# time = O(n^2), space = O(1)
class Solution(object):
    def minimumAverageDifference(self, nums):
        n = len(nums)
        best_diff = float('inf')
        best_idx = 0
        for i in range(n):
            left = sum(nums[:i + 1]) // (i + 1)
            right = sum(nums[i + 1:]) // (n - i - 1) if i < n - 1 else 0
            diff = abs(left - right)
            if diff < best_diff:
                best_diff = diff
                best_idx = i
        return best_idx


# V0-2
# IDEA : PRECOMPUTED PREFIX TABLE + MIN WITH KEY
#
#   materialise all prefix sums up front with itertools.accumulate, then let
#   min() do the scan : the average difference of index i is a pure function
#   of prefix[i], so the whole answer collapses to one min-with-key call.
#
#   min() returns the FIRST index achieving the minimum, which is exactly the
#   "smallest index wins" tie-break the statement asks for — no manual strict
#   comparison needed.
#
#   trades V0's O(1) running sum for an O(n) table in exchange for a fully
#   declarative formulation.
#
# time = O(n), space = O(n)
import itertools
class Solution(object):
    def minimumAverageDifference(self, nums):
        n = len(nums)
        prefix = list(itertools.accumulate(nums))
        total = prefix[-1]

        def diff(i):
            left = prefix[i] // (i + 1)
            right = (total - prefix[i]) // (n - i - 1) if i < n - 1 else 0
            return abs(left - right)

        return min(range(n), key=diff)
