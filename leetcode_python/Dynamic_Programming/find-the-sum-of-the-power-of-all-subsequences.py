"""

3082. Find the Sum of the Power of All Subsequences
Hard

You are given an integer array nums of length n and a positive integer k.

The power of an array of integers is defined as the number of subsequences with their sum equal to k.

Return the sum of power of all subsequences of nums.

Since the answer may be very large, return it modulo 10^9 + 7.


Example 1:

Input: nums = [1,2,3], k = 3
Output: 6
Explanation:
There are 5 subsequences of nums with non-zero power:
The subsequence [1,2,3] has 2 subsequences with sum == 3: [1,2,3] and [1,2,3].
The subsequence [1,2,3] has 1 subsequence with sum == 3: [1,2,3].
The subsequence [1,2,3] has 1 subsequence with sum == 3: [1,2,3].
The subsequence [1,2,3] has 1 subsequence with sum == 3: [1,2,3].
The subsequence [1,2,3] has 1 subsequence with sum == 3: [1,2,3].
Hence the answer is 2 + 1 + 1 + 1 + 1 = 6.

Example 2:

Input: nums = [2,3,3], k = 5
Output: 4
Explanation:
There are 3 subsequences of nums with non-zero power:
The subsequence [2,3,3] has 2 subsequences with sum == 5: [2,3,3] and [2,3,3].
The subsequence [2,3,3] has 1 subsequence with sum == 5: [2,3,3].
The subsequence [2,3,3] has 1 subsequence with sum == 5: [2,3,3].
Hence the answer is 2 + 1 + 1 = 4.

Example 3:

Input: nums = [1,2,3], k = 7
Output: 0
Explanation: There exists no subsequence with sum 7. Hence all subsequences of nums have power = 0.


Constraints:

1 <= n <= 100
1 <= nums[i] <= 10^4
1 <= k <= 100

"""

# V0
# IDEA : COUNT *PAIRS* (SUBSEQUENCE S, SUB-SUBSEQUENCE T OF S WITH SUM k)
#
#   the quantity asked for is a double sum — over subsequences S, of the
#   number of subsequences of S summing to k. rather than enumerate S, count
#   the PAIRS (S, T) directly, since each pair is worth exactly 1.
#
#   for each element there are three mutually exclusive roles :
#       not in S            -> the running sum is unchanged
#       in S but not in T   -> the running sum is unchanged
#       in S and in T       -> the running sum grows by nums[i]
#
#   so with dp[j] = number of partial pairs whose T-sum is j,
#
#       dp_new[j] = 2 * dp[j] + dp[j - nums[i]]
#
#   the factor 2 is the two ways to leave the running sum alone. sweeping j
#   downwards makes the update in-place safe, and dp[k] at the end is the
#   answer.
#
#   NOTE : elements above k can never join T, but they still double the
#          count — the recurrence handles that on its own.
#
"""

DP def
    the answer is a DOUBLE sum - over subsequences S, of the number of
    sub-subsequences T of S with sum k. so instead of enumerating S, count the
    PAIRS (S, T) directly, since each pair is worth exactly 1.

    each element has 3 mutually exclusive roles:
        not in S           -> T-sum unchanged
        in S but not in T  -> T-sum unchanged
        in S and in T      -> T-sum grows by nums[i]

    dp[j]: number of partial pairs whose T-sum is j

DP eq

     dp_new[j] = 2 * dp[j] + dp[j - nums[i]]


    -> e.g. the factor 2 is the TWO ways to leave the T-sum alone
              (skip the element, or put it in S only)

     sweep j DOWNWARD so the in-place update is safe

     init: dp[0] = 1
     ans = dp[k] % (10^9 + 7)

"""
# time = O(n * k), space = O(k)
class Solution(object):
    def sumOfPower(self, nums, k):
        MOD = 10 ** 9 + 7
        dp = [0] * (k + 1)
        dp[0] = 1
        for x in nums:
            for j in range(k, -1, -1):
                dp[j] = dp[j] * 2 % MOD
                if j >= x:
                    dp[j] = (dp[j] + dp[j - x]) % MOD
        return dp[k]
