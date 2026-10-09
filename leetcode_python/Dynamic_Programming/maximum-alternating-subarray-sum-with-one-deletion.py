"""

4072. Maximum Alternating Subarray Sum With One Deletion
Medium

You are given an integer array nums.

You may delete at most one element from nums, then choose a subarray of the
resulting array.

Return the maximum possible alternating sum of the chosen subarray.

The alternating sum of an array is the sum of its elements at even indices
minus the sum of its elements at odd indices. The chosen subarray is
reindexed starting from 0 before calculating its alternating sum.

Example 1:

Input: nums = [5,-5,1]

Output: 11

Explanation:

Choose not to delete an element and select the entire array. Its alternating
sum is 5 - (-5) + 1 = 11, which is the maximum possible.

Example 2:

Input: nums = [10,-5,-100]

Output: 110

Explanation:

Delete nums[1] = -5 to obtain [10,-100], then select the entire resulting
array. Its alternating sum is 10 - (-100) = 110, which is the maximum
possible.

Example 3:

Input: nums = [4,7]

Output: 7

Explanation:

Choose not to delete an element and select the subarray [7]. Its alternating
sum is 7, which is the maximum possible.

Constraints:

1 <= nums.length <= 10^5
-10^5 <= nums[i] <= 10^5

"""


# V0
# IDEA : DP (KADANE WITH 4 STATES: sign of the last element x deletion used)
"""

Brute force (the contest draft): delete each element, then run a max
subarray scan -> O(n^2), TLE at n = 10^5.

Only a deletion INSIDE the chosen subarray matters: deleting outside it
changes nothing, and deleting an end is the same as choosing a shorter
subarray. A middle deletion flips the sign of everything after it - that is
the only thing it buys.

DP def  (every state = best alternating sum of a subarray ENDING at i)

    plus0[i]  : nums[i] gets +, no deletion used
    minus0[i] : nums[i] gets -, no deletion used
    plus1[i]  : nums[i] gets +, one deletion used inside the subarray
    minus1[i] : nums[i] gets -, one deletion used inside the subarray

DP eq

    plus0[i]  = nums[i] + max(0, minus0[i-1])           # start here, or extend
    minus0[i] = -nums[i] + plus0[i-1]                   # a - can never start

    plus1[i]  = nums[i] + max(minus1[i-1], minus0[i-2]) # deletion earlier,
    minus1[i] = -nums[i] + max(plus1[i-1], plus0[i-2])  # or delete nums[i-1]

    -> "delete nums[i-1]" = the previous KEPT element is nums[i-2]

init: every state starts at -inf (impossible); answer = max of all states

e.g. nums = [10,-5,-100]
     plus0[0] = 10 -> minus1[2] = 100 + plus0[0] = 110 (delete -5)

"""
# time = O(n), space = O(1)
NEG_INF = float('-inf')


class Solution(object):
    def maxAlternatingSum(self, nums):
        """
        :type nums: List[int]
        :rtype: int
        """
        # edge case: the subarray must be non-empty
        if not nums:
            return 0

        # states ending at i-1 ("prev") and at i-2 ("prev2", no deletion only)
        prev_plus0 = NEG_INF
        prev_minus0 = NEG_INF
        prev_plus1 = NEG_INF
        prev_minus1 = NEG_INF
        prev2_plus0 = NEG_INF
        prev2_minus0 = NEG_INF

        best = NEG_INF

        for val in nums:
            # no deletion: plain alternating Kadane
            plus0 = val + max(0, prev_minus0)
            minus0 = -val + prev_plus0

            # NOTE !!! the deletion is either already behind us (state 1 at
            #          i-1) or is nums[i-1] itself (state 0 at i-2)
            plus1 = val + max(prev_minus1, prev2_minus0)
            minus1 = -val + max(prev_plus1, prev2_plus0)

            best = max(best, plus0, minus0, plus1, minus1)

            # shift the window: i-1 becomes i-2, i becomes i-1
            prev2_plus0 = prev_plus0
            prev2_minus0 = prev_minus0
            prev_plus0 = plus0
            prev_minus0 = minus0
            prev_plus1 = plus1
            prev_minus1 = minus1

        return best


# V1
# IDEA : LEFT / RIGHT DP + TRY DELETING EACH ELEMENT (the LC 1186 shape)
"""

Same split as "Maximum Subarray Sum with One Deletion" (LC 1186): for each
deleted index j, glue the best piece ENDING at j-1 to the best piece STARTING
at j+1 - their signs must alternate across the gap.

    left_plus[i]   : best subarray ending at i, nums[i] gets +
    left_minus[i]  : best subarray ending at i, nums[i] gets -
                     (a left piece is the subarray's START, so it begins with +)

    right_plus[i]  : best piece starting at i, nums[i] gets +
    right_minus[i] : best piece starting at i, nums[i] gets -
                     (a right piece is a TAIL, so it may begin with either sign)

    right_plus[i]  = nums[i]  + max(0, right_minus[i+1])
    right_minus[i] = -nums[i] + max(0, right_plus[i+1])

    delete j -> left_plus[j-1]  + right_minus[j+1]
                left_minus[j-1] + right_plus[j+1]

NOTE : two passes and O(n) space - V0 does it in one pass and O(1). Worth
       knowing because it is the standard "one deletion" decomposition.

"""
# time = O(n), space = O(n)
class Solution2(object):
    def maxAlternatingSum(self, nums):
        """
        :type nums: List[int]
        :rtype: int
        """
        # edge case: the subarray must be non-empty
        if not nums:
            return 0

        n = len(nums)

        # left pass: best subarray ENDING at i (it starts with a +)
        left_plus = [NEG_INF] * n
        left_minus = [NEG_INF] * n
        left_plus[0] = nums[0]
        for i in range(1, n):
            left_plus[i] = nums[i] + max(0, left_minus[i - 1])
            left_minus[i] = -nums[i] + left_plus[i - 1]

        # right pass: best piece STARTING at i (either sign may come first)
        right_plus = [NEG_INF] * n
        right_minus = [NEG_INF] * n
        right_plus[n - 1] = nums[n - 1]
        right_minus[n - 1] = -nums[n - 1]
        for i in range(n - 2, -1, -1):
            right_plus[i] = nums[i] + max(0, right_minus[i + 1])
            right_minus[i] = -nums[i] + max(0, right_plus[i + 1])

        # no deletion: the best subarray ending anywhere
        best = max(max(left_plus), max(left_minus))

        # delete nums[j] from the middle of the subarray
        for j in range(1, n - 1):
            # NOTE !!! the sign flips across the gap: + then -, or - then +
            glue_plus = left_plus[j - 1] + right_minus[j + 1]
            glue_minus = left_minus[j - 1] + right_plus[j + 1]
            best = max(best, glue_plus, glue_minus)

        return best
