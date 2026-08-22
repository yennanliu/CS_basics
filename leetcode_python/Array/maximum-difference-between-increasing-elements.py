"""

2016. Maximum Difference Between Increasing Elements
Easy

Given a 0-indexed integer array nums of size n, find the maximum difference between nums[i] and nums[j] (i.e., nums[j] - nums[i]), such that 0 <= i < j < n and nums[i] < nums[j].

Return the maximum difference. If no such i and j exists, return -1.


Example 1:

Input: nums = [7,1,5,4]
Output: 4
Explanation:
The maximum difference occurs with i = 1 and j = 2, nums[j] - nums[i] = 5 - 1 = 4.
Note that with i = 1 and j = 3, the difference nums[j] - nums[i] = 4 - 1 = 3 is also valid, but 4 is a bigger difference.

Example 2:

Input: nums = [9,4,3,2]
Output: -1
Explanation:
There is no i and j such that i < j and nums[i] < nums[j].

Example 3:

Input: nums = [1,5,2,10]
Output: 9
Explanation:
The maximum difference occurs with i = 0 and j = 3, nums[j] - nums[i] = 10 - 1 = 9.


Constraints:

n == nums.length
2 <= n <= 1000
1 <= nums[i] <= 10^9

"""

# V0
# IDEA : RUNNING MIN (same shape as LC 121 "best time to buy and sell stock")
#
#   scan left -> right, keep the smallest value seen so far (`cur_min`).
#   for every j, the best partner i is that running minimum, so the answer
#   is max over j of (nums[j] - cur_min) — but ONLY when it is > 0, since
#   the problem requires nums[i] < nums[j] strictly.
#
#   NOTE : return -1 when the array is non-increasing (no positive diff).
#
# time = O(n), space = O(1)
class Solution(object):
    def maximumDifference(self, nums):
        res = -1
        cur_min = nums[0]
        for x in nums[1:]:
            if x > cur_min:
                res = max(res, x - cur_min)
            else:
                cur_min = x
        return res


# V0-1
# IDEA : BRUTE FORCE - PRICE EVERY (i, j) PAIR
#
#   check all pairs i < j directly and keep the best positive difference.
#   n <= 1000 so the ~5 * 10^5 pairs are cheap enough to pass, and this is the
#   literal statement of the problem - useful as the ground truth the O(n)
#   versions are checked against.
#
# time = O(n^2)
# space = O(1)
class Solution(object):
    def maximumDifference(self, nums):
        n = len(nums)
        res = -1
        for i in range(n):
            for j in range(i + 1, n):
                if nums[i] < nums[j]:
                    res = max(res, nums[j] - nums[i])
        return res


# V0-2
# IDEA : SUFFIX MAXIMUM ARRAY (TWO PASSES, RIGHT -> LEFT THEN LEFT -> RIGHT)
#
#   the V0 scan fixes j and keeps the best i so far. flip it: fix i and ask for
#   the largest value anywhere to its RIGHT.
#
#     pass 1 (backwards) : suf[i] = max(nums[i:])
#     pass 2 (forwards)  : the best partner for i is suf[i+1], so the answer
#                          is max(suf[i+1] - nums[i]) over the i where that
#                          difference is strictly positive
#
#   this trades O(1) space for an explicit table, but it is the shape that
#   generalises when the right-hand aggregate is something a running scalar
#   cannot maintain (e.g. queried repeatedly, or a max over a suffix window).
#
# time = O(n)
# space = O(n)
class Solution(object):
    def maximumDifference(self, nums):
        n = len(nums)
        suf = [0] * n
        suf[n - 1] = nums[n - 1]
        for i in range(n - 2, -1, -1):
            suf[i] = max(nums[i], suf[i + 1])

        res = -1
        for i in range(n - 1):
            if suf[i + 1] > nums[i]:
                res = max(res, suf[i + 1] - nums[i])
        return res
