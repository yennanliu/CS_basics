"""

2342. Max Sum of a Pair With Equal Sum of Digits
Medium

You are given a 0-indexed array nums consisting of positive integers. You can choose two indices i and j, such that i != j, and the sum of digits of the number nums[i] is equal to that of nums[j].

Return the maximum value of nums[i] + nums[j] that you can obtain over all possible indices i and j that satisfy the conditions. If no such pair of indices exists, return -1.


Example 1:

Input: nums = [18,43,36,13,7]
Output: 54
Explanation: The pairs (i, j) that satisfy the conditions are:
- (0, 2), both numbers have a sum of digits equal to 9, and their sum is 18 + 36 = 54.
- (1, 4), both numbers have a sum of digits equal to 7, and their sum is 43 + 7 = 50.
So the maximum sum that we can obtain is 54.

Example 2:

Input: nums = [10,12,19,14]
Output: -1
Explanation: There are no two numbers that satisfy the conditions, so we return -1.


Constraints:

1 <= nums.length <= 10^5
1 <= nums[i] <= 10^9

"""

# V0
# IDEA : BUCKET BY DIGIT SUM, KEEP ONLY THE LARGEST SEEN PER BUCKET
#
#   within one digit-sum bucket the best pair is always the two biggest
#   numbers, so there is no need to store the whole bucket. sweep once
#   holding `best[key]` = the largest number seen with that digit sum :
#       on each x, the candidate answer is x + best[key] (if the bucket is
#       non-empty), then raise best[key] to max(best[key], x)
#
#   answering before updating is what guarantees the two elements are at
#   DIFFERENT indices.
#
# time = O(n * d), space = O(number of distinct digit sums)
class Solution(object):
    def maximumSum(self, nums):
        best = {}
        res = -1
        for x in nums:
            key = sum(int(c) for c in str(x))
            if key in best:
                res = max(res, best[key] + x)
                best[key] = max(best[key], x)
            else:
                best[key] = x
        return res
