"""

2464. Minimum Subarrays in a Valid Split
Medium
(premium / locked problem)

You are given an integer array nums.

Splitting of an integer array nums into subarrays is valid if:

the greatest common divisor of the first and last elements of each subarray is greater than 1, and
each element of nums belongs to exactly one subarray.

Return the minimum number of subarrays in a valid subarray splitting of nums. If a valid subarray splitting is not possible, return -1.

Note that:

The greatest common divisor of two numbers is the largest positive integer that evenly divides both numbers.
A subarray is a contiguous non-empty part of an array.


Example 1:

Input: nums = [2,6,3,4,3]
Output: 2
Explanation: We can create a valid split in the following way: [2,6] | [3,4,3].
- The starting element of the 1st subarray is 2 and the ending is 6. Their greatest common divisor is 2, which is greater than 1.
- The starting element of the 2nd subarray is 3 and the ending is 3. Their greatest common divisor is 3, which is greater than 1.
It can be proved that 2 is the minimum number of subarrays that we can obtain in a valid split.

Example 2:

Input: nums = [3,5]
Output: 2
Explanation: We can create a valid split in the following way: [3] | [5].
- The starting element of the 1st subarray is 3 and the ending is 3. Their greatest common divisor is 3, which is greater than 1.
- The starting element of the 2nd subarray is 5 and the ending is 5. Their greatest common divisor is 5, which is greater than 1.
It can be proved that 2 is the minimum number of subarrays that we can obtain in a valid split.

Example 3:

Input: nums = [1,2,1]
Output: -1
Explanation: It is impossible to create a valid split.


Constraints:

1 <= nums.length <= 1000
1 <= nums[i] <= 10^5

"""

# V0
# IDEA : PREFIX DP OVER THE CUT POSITIONS
#
#   dp[i] = fewest subarrays covering nums[:i]. a final subarray nums[j:i] is
#   allowed when gcd(nums[j], nums[i-1]) > 1, so
#       dp[i] = min over valid j of dp[j] + 1
#
#   a single element is its own valid subarray whenever nums[i-1] > 1
#   (gcd(x, x) = x), which the same test covers with j = i - 1.
#
#   a 1 anywhere in the array makes any subarray containing it as an endpoint
#   invalid, so the DP simply never reaches the end and returns -1.
#
"""

DP def
    dp[i]: FEWEST subarrays covering nums[:i]

DP eq

     a final subarray nums[j..i-1] is allowed when gcd(nums[j], nums[i-1]) > 1:

        dp[i] = min over valid j of  dp[j] + 1


    -> e.g. a SINGLE element is its own valid subarray whenever
              nums[i-1] > 1 (gcd(x, x) = x) - the same test covers it at
              j = i - 1

     a 1 anywhere makes every subarray having it as an endpoint invalid, so
     the DP simply never reaches the end -> -1

     init: dp[0] = 0
     ans = dp[n], or -1 if inf

"""
# time = O(n^2 log(max)), space = O(n)
from math import gcd


class Solution(object):
    def validSubarraySplit(self, nums):
        n = len(nums)
        INF = float('inf')
        dp = [INF] * (n + 1)
        dp[0] = 0

        for i in range(1, n + 1):
            for j in range(i - 1, -1, -1):
                if dp[j] < INF and gcd(nums[j], nums[i - 1]) > 1:
                    dp[i] = min(dp[i], dp[j] + 1)
        return -1 if dp[n] == INF else dp[n]
