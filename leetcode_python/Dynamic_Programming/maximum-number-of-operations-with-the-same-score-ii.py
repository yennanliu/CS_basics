"""

3040. Maximum Number of Operations With the Same Score II
Medium

Given an array of integers called nums, you can perform any of the following operation while nums contains at least 2 elements:

Choose the first two elements of nums and delete them.
Choose the last two elements of nums and delete them.
Choose the first and the last elements of nums and delete them.

The score of the operation is the sum of the deleted elements.

Your task is to find the maximum number of operations that can be performed, such that all operations have the same score.

Return the maximum number of operations possible that satisfy the condition mentioned above.


Example 1:

Input: nums = [3,2,1,2,3,4]
Output: 3
Explanation: We perform the following operations:
- Delete the first two elements, with score 3 + 2 = 5, nums = [1,2,3,4].
- Delete the first and the last elements, with score 1 + 4 = 5, nums = [2,3].
- Delete the first and the last elements, with score 2 + 3 = 5, nums = [].
We are unable to perform any more operations as nums is empty.

Example 2:

Input: nums = [3,2,6,1,4]
Output: 2
Explanation: We perform the following operations:
- Delete the first two elements, with score 3 + 2 = 5, nums = [6,1,4].
- Delete the last two elements, with score 1 + 4 = 5, nums = [6].
It can be proven that we can perform at most 2 operations.


Constraints:

2 <= nums.length <= 2000
1 <= nums[i] <= 1000

"""

# V0
# IDEA : ONLY 3 POSSIBLE SCORES — FIX ONE, THEN MEMOISED (l, r) SEARCH
#
#   the very first operation must be one of the three shapes, so the shared
#   score is one of
#       nums[0] + nums[1],  nums[-1] + nums[-2],  nums[0] + nums[-1]
#   just try each and keep the best.
#
#   with the score FIXED, the array is always a contiguous window [l, r] and
#   the three moves are
#       take (l, l+1) -> (l+2, r)
#       take (r-1, r) -> (l, r-2)
#       take (l, r)   -> (l+1, r-1)
#   each allowed only when the pair sums to the target. memoising on (l, r)
#   makes it O(n^2) states with O(1) work each.
#
# time = O(n^2), space = O(n^2)
class Solution(object):
    def maxOperations(self, nums):
        n = len(nums)

        def solve(target):
            memo = {}

            def go(l, r):
                if l >= r:
                    return 0
                key = (l, r)
                if key in memo:
                    return memo[key]
                best = 0
                if nums[l] + nums[l + 1] == target:
                    best = max(best, 1 + go(l + 2, r))
                if nums[r] + nums[r - 1] == target:
                    best = max(best, 1 + go(l, r - 2))
                if nums[l] + nums[r] == target:
                    best = max(best, 1 + go(l + 1, r - 1))
                memo[key] = best
                return best

            return go(0, n - 1)

        candidates = set([nums[0] + nums[1], nums[-1] + nums[-2], nums[0] + nums[-1]])
        return max(solve(t) for t in candidates)
