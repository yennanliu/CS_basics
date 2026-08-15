"""

3247. Number of Subsequences with Odd Sum
Medium
🔒 (premium)

Given an array nums, return the number of subsequences with an odd sum.

Since the answer may be very large, return it modulo 10^9 + 7.


Example 1:

Input: nums = [1,1,1]
Output: 4
Explanation:
The odd-sum subsequences are: [1, 1, 1], [1, 1, 1], [1, 1, 1], [1, 1, 1].

Example 2:

Input: nums = [1,2,2]
Output: 4
Explanation:
The odd-sum subsequences are: [1, 2, 2], [1, 2, 2], [1, 2, 2], [1, 2, 2].


Constraints:

1 <= nums.length <= 10^5
1 <= nums[i] <= 10^9

"""

# V0
# IDEA : THE SUM'S PARITY DEPENDS ONLY ON HOW MANY *ODD* ELEMENTS ARE TAKEN
#
#   even elements never change the parity, so they can be included or not
#   freely — 2^even choices. the sum is odd exactly when an ODD number of the
#   odd elements is chosen, and the number of ways to pick an odd-sized
#   subset of `odd` items is 2^(odd - 1).
#
#   so the answer is 2^(odd-1) * 2^even, or 0 when there is no odd element at
#   all.
#
# time = O(n), space = O(1)
class Solution(object):
    def subsequenceCount(self, nums):
        MOD = 10 ** 9 + 7
        odd = sum(1 for x in nums if x % 2)
        even = len(nums) - odd
        if odd == 0:
            return 0
        return pow(2, odd - 1, MOD) * pow(2, even, MOD) % MOD
