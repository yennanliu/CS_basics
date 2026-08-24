"""

2369. Check if There is a Valid Partition For The Array
Medium

You are given a 0-indexed integer array nums. You have to partition the array into one or more contiguous subarrays.

We call a partition of the array valid if each of the obtained subarrays satisfies one of the following conditions:

The subarray consists of exactly 2 equal elements. For example, the subarray [2,2] is good.
The subarray consists of exactly 3 equal elements. For example, the subarray [4,4,4] is good.
The subarray consists of exactly 3 consecutive increasing elements, that is, the difference between adjacent elements is 1. For example, the subarray [3,4,5] is good, but the subarray [1,3,5] is not.

Return true if the array has at least one valid partition. Otherwise, return false.


Example 1:

Input: nums = [4,4,4,5,6]
Output: true
Explanation: The array can be partitioned into the subarrays [4,4] and [4,5,6].
This partition is valid, so we return true.

Example 2:

Input: nums = [1,1,1,2]
Output: false
Explanation: There is no valid partition for this array.


Constraints:

2 <= nums.length <= 10^5
1 <= nums[i] <= 10^6

"""

# V0
# IDEA : LINEAR DP ON PREFIX LENGTHS — ONLY BLOCKS OF SIZE 2 OR 3 EXIST
#
#   dp[i] = "the first i elements can be validly partitioned", dp[0] = True.
#   the last block is either 2 or 3 long, so
#       dp[i] = (dp[i-2] and the last two are equal)
#            or (dp[i-3] and the last three are equal)
#            or (dp[i-3] and the last three increase by exactly 1)
#
#   that makes each step O(1) and the whole scan O(n).
#
"""

DP def
    dp[i]: can the FIRST i elements be validly partitioned?

           -> every legal block is exactly 2 or 3 long, which is what keeps
              the transition O(1)

DP eq

     dp[i] = ( dp[i-2] and nums[i-2] == nums[i-1] )              # 2 equal

          or ( dp[i-3] and nums[i-3] == nums[i-2] == nums[i-1] ) # 3 equal

          or ( dp[i-3] and nums[i-2] == nums[i-3] + 1
                       and nums[i-1] == nums[i-2] + 1 )          # 3 consecutive


    -> e.g. only the last block needs deciding - everything before it is
              already summarised by dp[i-2] / dp[i-3]

     init: dp[0] = True (the empty prefix)
     ans = dp[n]

"""
# time = O(n), space = O(n)
class Solution(object):
    def validPartition(self, nums):
        n = len(nums)
        dp = [False] * (n + 1)
        dp[0] = True

        for i in range(2, n + 1):
            a, b = nums[i - 2], nums[i - 1]
            if dp[i - 2] and a == b:
                dp[i] = True
                continue
            if i >= 3 and dp[i - 3]:
                c = nums[i - 3]
                if c == a == b or (a == c + 1 and b == a + 1):
                    dp[i] = True
        return dp[n]
