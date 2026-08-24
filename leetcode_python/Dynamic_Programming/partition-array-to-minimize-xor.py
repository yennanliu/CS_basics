"""

3599. Partition Array to Minimize XOR
Medium

You are given an integer array nums and an integer k.

Your task is to partition nums into k non-empty subarrays. For each subarray, compute the bitwise XOR of all its elements.

Return the minimum possible value of the maximum XOR among these k subarrays.


Example 1:

Input: nums = [1,2,3], k = 2
Output: 1
Explanation:
The optimal partition is [1] and [2, 3].
XOR of the first subarray is 1.
XOR of the second subarray is 2 XOR 3 = 1.
The maximum XOR among the subarrays is 1, which is the minimum possible.

Example 2:

Input: nums = [2,3,3,2], k = 3
Output: 2
Explanation:
The optimal partition is [2], [3, 3], and [2].
XOR of the first subarray is 2.
XOR of the second subarray is 3 XOR 3 = 0.
XOR of the third subarray is 2.
The maximum XOR among the subarrays is 2, which is the minimum possible.

Example 3:

Input: nums = [1,1,2,3,1], k = 2
Output: 0
Explanation:
The optimal partition is [1, 1] and [2, 3, 1].
XOR of the first subarray is 1 XOR 1 = 0.
XOR of the second subarray is 2 XOR 3 XOR 1 = 0.
The maximum XOR among the subarrays is 0, which is the minimum possible.


Constraints:

1 <= nums.length <= 250
1 <= nums[i] <= 10^9
1 <= k <= n

"""

# V0
# IDEA : DP OVER (NUMBER OF PARTS, PREFIX LENGTH), MINIMISING A RUNNING MAX
#
#   dp[j][i] = the smallest achievable "maximum segment xor" when the first
#   i elements are cut into exactly j parts. the last part is nums[t..i-1],
#   whose xor is prefix[i] ^ prefix[t], and the objective for that choice is
#   max(dp[j-1][t], that xor).
#
#   the objective is a max, so it composes cleanly: extending an optimal
#   (j-1)-part solution can never be beaten by a worse one, which is what
#   makes the plain minimisation over t correct.
#
"""

DP def
    dp[j][i]: the SMALLEST achievable "maximum segment xor" when the first

              i elements are cut into exactly j parts

    pre[]   : prefix xor, so xor(nums[t..i-1]) = pre[i] ^ pre[t]

DP eq

     dp[j][i] = min over t in [j-1, i) of

                   max( dp[j-1][t], pre[i] ^ pre[t] )


    -> e.g. the objective is a MAX, so it composes cleanly - extending an
              optimal (j-1)-part solution can never be beaten by a worse
              one, which is what makes the plain minimisation over t correct

     an early break once best == 0 (nothing can be lower)

     init: dp[0][0] = 0
     ans = dp[k][n]

"""
# time = O(k * n^2), space = O(n)
class Solution(object):
    def minXor(self, nums, k):
        n = len(nums)
        INF = float('inf')
        pre = [0] * (n + 1)
        for i, v in enumerate(nums):
            pre[i + 1] = pre[i] ^ v

        prev = [INF] * (n + 1)
        prev[0] = 0                      # zero parts covering zero elements
        for j in range(1, k + 1):
            cur = [INF] * (n + 1)
            for i in range(j, n - (k - j) + 1):
                best = INF
                pi = pre[i]
                for t in range(j - 1, i):
                    p = prev[t]
                    if p == INF:
                        continue
                    seg = pi ^ pre[t]
                    v = p if p > seg else seg
                    if v < best:
                        best = v
                        if best == 0:
                            break
                cur[i] = best
            prev = cur
        return prev[n]
