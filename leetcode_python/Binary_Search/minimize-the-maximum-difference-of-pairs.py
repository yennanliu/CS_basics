"""

2616. Minimize the Maximum Difference of Pairs
Medium

You are given a 0-indexed integer array nums and an integer p. Find p pairs of
indices of nums such that the maximum difference amongst all the pairs is
minimized. Also, ensure no index appears more than once amongst the p pairs.

Note that for a pair of elements at the index i and j, the difference of this
pair is |nums[i] - nums[j]|, where |x| represents the absolute value of x.

Return the minimum maximum difference among all p pairs. We define the maximum
of an empty set to be zero.

Example 1:

Input: nums = [10,1,2,7,1,3], p = 2

Output: 1

Explanation:

The first pair is formed from the indices 1 and 4, and the second pair is
formed from the indices 2 and 5.
The maximum difference is max(|nums[1] - nums[4]|, |nums[2] - nums[5]|) =
max(0, 1) = 1. Therefore, we return 1.

Example 2:

Input: nums = [4,2,1,2], p = 1

Output: 0

Explanation:

Let the indices 1 and 3 form a pair. The difference of that pair is
|2 - 2| = 0, which is the minimum we can attain.

Constraints:

1 <= nums.length <= 10^5
0 <= nums[i] <= 10^9
0 <= p <= (nums.length)/2

"""

# V0
# IDEA : SORT + BINARY SEARCH ON THE ANSWER (MINIMIZE THE MAXIMUM) + GREEDY CHECK
#
#   Binary search the answer `max_diff` over [0, max(nums) - min(nums)].
#   The check "can we form p disjoint pairs, each with diff <= max_diff?" is
#   MONOTONIC: if it works for d, it works for every d' > d. So find the
#   smallest d that works.
#
#   Why sort: in sorted order, the best partner for any element is a NEIGHBOUR
#   (pairing i with j > i + 1 is never better than pairing i with i + 1).
#
#   Why the greedy check is optimal: scan left to right; if nums[i + 1] - nums[i]
#   <= max_diff, take the pair and skip both. Taking it never hurts — any
#   solution that instead uses i + 1 with i + 2 can swap that pair for (i, i + 1)
#   without losing a pair (exchange argument).
#
#   e.g. nums = [10,1,2,7,1,3], p = 2 -> sorted [1,1,2,3,7,10]
#        max_diff = 0 -> (1,1) only            -> 1 pair  < 2 -> too small
#        max_diff = 1 -> (1,1), (2,3)          -> 2 pairs >= 2 -> answer 1
#
#   (DP over (index, pairs used) is also correct, but O(n * p) times out here.)
#
# time = O(n log n + n log(max - min)), space = O(1) extra (O(n) for the sorted copy)
class Solution(object):
    def minimizeMax(self, nums, p):
        """
        :type nums: List[int]
        :type p: int
        :rtype: int
        """
        # edge case: the maximum of an empty set of pairs is 0
        if p == 0:
            return 0

        # sort a copy so the closest values sit next to each other
        sorted_nums = sorted(nums)

        # the answer is a difference: at least 0, at most the full spread
        left = 0
        right = sorted_nums[-1] - sorted_nums[0]
        answer = right

        while left <= right:
            mid = (left + right) // 2
            if self.can_form_pairs(sorted_nums, p, mid):
                # mid works -> record it, then try a smaller maximum
                answer = mid
                right = mid - 1
            else:
                # mid is too tight -> allow a larger difference
                left = mid + 1

        return answer

    def can_form_pairs(self, sorted_nums, p, max_diff):
        """Return True if p disjoint adjacent pairs with diff <= max_diff exist."""
        pair_count = 0
        i = 0
        while i < len(sorted_nums) - 1:
            if sorted_nums[i + 1] - sorted_nums[i] <= max_diff:
                pair_count += 1
                # NOTE !!! skip BOTH indices: each index may be used only once
                i += 2
            else:
                # sorted_nums[i] cannot pair with its neighbour -> leave it unpaired
                i += 1

            # early exit: enough pairs already
            if pair_count >= p:
                return True

        return False
