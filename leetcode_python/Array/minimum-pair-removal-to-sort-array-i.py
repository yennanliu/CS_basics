"""

3507. Minimum Pair Removal to Sort Array I
Easy

Given an array nums, you can perform the following operation any number of
times:

Select the adjacent pair with the minimum sum in nums. If multiple such pairs
exist, choose the leftmost one.

Replace the pair with their sum.

Return the minimum number of operations needed to make the array non-decreasing.

An array is said to be non-decreasing if each element is greater than or equal
to its previous element (if it exists).

Example 1:

Input: nums = [5,2,3,1]

Output: 2

Explanation:

The pair (3,1) has the minimum sum of 4. After replacement, nums = [5,2,4].

The pair (2,4) has the minimum sum of 6. After replacement, nums = [5,6].

The array nums became non-decreasing in two operations.

Example 2:

Input: nums = [1,2,2]

Output: 0

Explanation:

The array nums is already sorted.

Constraints:

1 <= nums.length <= 50

-1000 <= nums[i] <= 1000

"""

# V0
# IDEA : DIRECT SIMULATION
#
#   the array is tiny (n <= 50) and every operation shrinks it by one element,
#   so at most n - 1 operations can ever happen.  that bounds the whole
#   simulation at O(n^2) and lets us just re-scan for the minimum-sum adjacent
#   pair each round.
#
#   the "leftmost on ties" rule is handled for free by using a strict `<` when
#   looking for a better pair, which keeps the first index that achieved the
#   minimum.
#
# time = O(n^2), space = O(n)
class Solution(object):
    def minimumPairRemoval(self, nums):
        arr = list(nums)
        ops = 0
        while True:
            if all(arr[i] <= arr[i + 1] for i in range(len(arr) - 1)):
                return ops
            best = 0
            best_sum = arr[0] + arr[1]
            for i in range(1, len(arr) - 1):
                s = arr[i] + arr[i + 1]
                if s < best_sum:
                    best_sum = s
                    best = i
            arr[best:best + 2] = [best_sum]
            ops += 1
