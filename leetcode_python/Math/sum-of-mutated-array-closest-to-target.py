"""

1300. Sum of Mutated Array Closest to Target
Medium

Given an integer array arr and a target value target, return the integer value such that
when we change all the integers larger than value in the given array to be equal to value,
the sum of the array gets as close as possible (in absolute difference) to target.

In case of a tie, return the minimum such integer.

Notice that the answer is not neccesarilly a number from arr.


Example 1:

Input: arr = [4,9,3], target = 10
Output: 3
Explanation: When using 3 arr converts to [3, 3, 3] which sums 9 and that's the optimal answer.

Example 2:

Input: arr = [2,3,5], target = 10
Output: 5

Example 3:

Input: arr = [60864,25176,27249,21296,20204], target = 56803
Output: 11361


Constraints:

1 <= arr.length <= 10^4
1 <= arr[i], target <= 10^5

"""

# V0
# IDEA : BINARY SEARCH on the answer (the mutated sum is monotonic in `value`)
#
#   define S(v) = sum(min(x, v) for x in arr). S is non-decreasing in v, so we
#   can binary search the smallest v with S(v) >= target.
#
#   S(v) is evaluated in O(log n) after sorting + prefix sums :
#     i = first index with arr[i] > v
#     S(v) = prefix[i] + (n - i) * v
#
#   the optimum is then either that v or v - 1 (the last one still below
#   target) -- everything further away is strictly worse by monotonicity.
#   NOTE : ties must return the SMALLER value, so compare with a strict '<'
#          which keeps v - 1 on equality.
#   NOTE : if even S(max(arr)) <= target, capping cannot reach target and
#          max(arr) (i.e. no change at all) is the answer.
#
# time = O(n log n), space = O(n)
import bisect
class Solution(object):
    def findBestValue(self, arr, target):
        arr = sorted(arr)
        n = len(arr)
        prefix = [0] * (n + 1)
        for i, x in enumerate(arr):
            prefix[i + 1] = prefix[i] + x

        def mutated_sum(v):
            i = bisect.bisect_right(arr, v)
            return prefix[i] + (n - i) * v

        if prefix[n] <= target:
            return arr[-1]

        lo, hi = 0, arr[-1]
        while lo < hi:
            mid = (lo + hi) // 2
            if mutated_sum(mid) >= target:
                hi = mid
            else:
                lo = mid + 1

        if lo == 0:
            return 0
        if abs(mutated_sum(lo) - target) < abs(mutated_sum(lo - 1) - target):
            return lo
        return lo - 1
