"""

2587. Rearrange Array to Maximize Prefix Score
Medium

You are given a 0-indexed integer array nums. You can rearrange the elements of nums to any order (including the given order).

Let prefix be the array containing the prefix sums of nums after rearranging it. In other words, prefix[i] is the sum of the elements from 0 to i in nums after rearranging it. The score of nums is the number of positive integers in the array prefix.

Return the maximum score you can achieve.


Example 1:

Input: nums = [2,-1,0,1,-3,3,-3]
Output: 6
Explanation: We can rearrange the array into nums = [2,3,1,-1,-3,0,-3].
prefix = [2,5,6,5,2,2,-1], so the score is 6.
It can be shown that 6 is the maximum score we can obtain.

Example 2:

Input: nums = [-2,-3,0]
Output: 0
Explanation: Any rearrangement of the array will result in a score of 0.


Constraints:

1 <= nums.length <= 10^5
-10^6 <= nums[i] <= 10^6

"""

# V0
# IDEA : GREEDY + SORT DESCENDING
#
#   the prefix sums are non-increasing once we place elements from largest to
#   smallest, so the set of positive prefixes is exactly a PREFIX of indices.
#   -> put the biggest numbers first, and the answer is the first index where
#      the running sum stops being positive.
#
#   why descending is optimal : whatever multiset of k elements you commit to
#   the first k slots, prefix[k-1] is their total sum -- the sum is maximized
#   by taking the k largest. And ordering those k largest in descending order
#   keeps every earlier prefix at its maximum possible value too.
#
#   NOTE : "positive" means > 0, so a prefix sum of exactly 0 already stops
#          the count (see example 2, where 0 contributes nothing).
#
# time = O(n log n), space = O(n) for the sorted copy
class Solution(object):
    def maxScore(self, nums):
        s = 0
        for i, x in enumerate(sorted(nums, reverse=True)):
            s += x
            if s <= 0:
                return i
        return len(nums)
