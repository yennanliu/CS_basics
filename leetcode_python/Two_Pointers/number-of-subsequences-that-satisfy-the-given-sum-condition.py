"""

1498. Number of Subsequences That Satisfy the Given Sum Condition
Medium

You are given an array of integers nums and an integer target.

Return the number of non-empty subsequences of nums such that the sum of the
minimum and maximum element on it is less or equal to target. Since the answer may
be too large, return it modulo 10^9 + 7.


Example 1:

Input: nums = [3,5,6,7], target = 9
Output: 4
Explanation: There are 4 subsequences that satisfy the condition.
[3] -> Min value + max value <= target (3 + 3 <= 9)
[3,5] -> (3 + 5 <= 9)
[3,5,6] -> (3 + 6 <= 9)
[3,6] -> (3 + 6 <= 9)

Example 2:

Input: nums = [3,3,6,8], target = 10
Output: 6
Explanation: There are 6 subsequences that satisfy the condition. (nums can have
repeated numbers).
[3] , [3] , [3,3], [3,6] , [3,6] , [3,3,6]

Example 3:

Input: nums = [2,3,3,4,6,7], target = 12
Output: 61
Explanation: There are 63 non-empty subsequences, two of them do not satisfy the
condition ([6,7], [7]).
Number of valid subsequences (63 - 2 = 61).


Constraints:

1 <= nums.length <= 10^5
1 <= nums[i] <= 10^6
1 <= target <= 10^6

"""

# V0
# IDEA : SORT + TWO POINTERS (only the MIN and MAX matter, so order does not)
#
#   the condition names min + max only -- nothing about the elements in between.
#   so the array can be sorted freely, and then for a fixed left end l the
#   question becomes "how far right can the max go".
#
#   with r the largest index where nums[l] + nums[r] <= target, every subset of
#   the elements strictly between l and r may be included or not:
#
#     count for this l = 2^(r - l)
#
#   e.g. [3,5,6,7], target = 9 :
#        l=0 (3) -> r=2 (6)  -> 2^2 = 4 subsequences with min 3
#        l=1 (5) -> 5+5 > 9  -> none      -> total 4
#
#   NOTE !!! l and r only ever move inward, so this is O(n) after the sort --
#            not a binary search per l.
#   NOTE !!! powers of two are precomputed; pow(2, r-l, MOD) inside the loop is
#            an extra log factor for no reason.
#
# time = O(nlogn), space = O(n)
class Solution(object):
    def numSubseq(self, nums, target):
        """
        :type nums: List[int]
        :type target: int
        :rtype: int
        """
        # edge
        if not nums:
            return 0

        MOD = 10 ** 9 + 7
        nums.sort()
        n = len(nums)

        pw = [1] * n
        for i in range(1, n):
            pw[i] = pw[i - 1] * 2 % MOD

        res = 0
        l, r = 0, n - 1
        while l <= r:
            if nums[l] + nums[r] > target:
                # this max is too big for ANY left end -> drop it
                r -= 1
            else:
                res = (res + pw[r - l]) % MOD
                l += 1

        return res
