"""

2044. Count Number of Maximum Bitwise-OR Subsets
Medium

Given an integer array nums, find the maximum possible bitwise OR of a subset of nums and return the number of different non-empty subsets with the maximum bitwise OR.

An array a is a subset of an array b if a can be obtained from b by deleting some (possibly zero) elements of b. Two subsets are considered different if the indices of the elements chosen are different.

The bitwise OR of an array a is equal to a[0] OR a[1] OR ... OR a[a.length - 1] (0-indexed).


Example 1:

Input: nums = [3,1]
Output: 2
Explanation: The maximum possible bitwise OR of a subset is 3. There are 2 subsets with a bitwise OR of 3:
- [3]
- [3,1]

Example 2:

Input: nums = [2,2,2]
Output: 7
Explanation: All non-empty subsets of [2,2,2] have a bitwise OR of 2. There are 2^3 - 1 = 7 total subsets.

Example 3:

Input: nums = [3,2,1,5]
Output: 6
Explanation: The maximum possible bitwise OR of a subset is 7. There are 6 subsets with a bitwise OR of 7:
- [3,5]
- [3,1,5]
- [3,2,5]
- [3,2,1,5]
- [2,1,5]
- [2,5]


Constraints:

1 <= nums.length <= 16
1 <= nums[i] <= 10^5

"""

# V0
# IDEA : DP OVER REACHABLE OR VALUES (counts per OR value)
#
#   dp[v] = number of subsets whose OR is exactly v. process one element at
#   a time :  new_dp[v | x] += dp[v]  for every currently reachable v.
#
#   the maximum OR is always OR-of-everything (adding an element can only
#   set more bits), so the answer is dp[target] at the end.
#
#   NOTE : n <= 16, so plain 2^n brute force also works, but the OR-value DP
#          stays small because distinct reachable ORs are far fewer than 2^n.
#
"""

DP def
    dp[v]: number of subsets whose bitwise OR is EXACTLY v

DP eq

     for each x in nums:

        dp_new[v | x] += dp[v]      for every currently reachable v


    -> e.g. the maximum OR is always the OR of EVERYTHING
              (adding an element can only set more bits), so

         target = nums[0] | nums[1] | ... | nums[n-1]
         ans    = dp[target]

     init: dp[0] = 1 (the empty subset)

"""
# time = O(n * |distinct ORs|), space = O(|distinct ORs|)
from collections import defaultdict


class Solution(object):
    def countMaxOrSubsets(self, nums):
        target = 0
        for x in nums:
            target |= x

        dp = defaultdict(int)
        dp[0] = 1                      # the empty subset
        for x in nums:
            for v, c in list(dp.items()):
                dp[v | x] += c
        return dp[target]
