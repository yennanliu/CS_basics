"""

2518. Number of Great Partitions
Hard

You are given an array nums consisting of positive integers and an integer k.

Partition the array into two ordered groups such that each element is in exactly one group. A partition is called great if the sum of elements of each group is greater than or equal to k.

Return the number of distinct great partitions. Since the answer may be too large, return it modulo 10^9 + 7.

Two partitions are considered distinct if some element nums[i] is in different groups in the two partitions.


Example 1:

Input: nums = [1,2,3,4], k = 4
Output: 6
Explanation: The great partitions are: ([1,2,3], [4]), ([1,3], [2,4]), ([1,4], [2,3]), ([2,3], [1,4]), ([2,4], [1,3]) and ([4], [1,2,3]).

Example 2:

Input: nums = [3,3,3], k = 4
Output: 0
Explanation: There are no great partitions for this array.

Example 3:

Input: nums = [6,6], k = 2
Output: 2
Explanation: We can either put nums[0] in the first partition or in the second partition.
The great partitions will be ([6], [6]) and ([6], [6]).


Constraints:

1 <= nums.length, k <= 1000
1 <= nums[i] <= 10^9

"""

# V0
# IDEA : COMPLEMENTARY COUNTING + 0/1 KNAPSACK (count subsets with sum < k)
#
#   counting great partitions directly is hard; counting BAD ones is easy.
#   there are 2^n ordered partitions in total. a partition is bad iff group A
#   sums to < k, or group B sums to < k.
#
#   key observation : if total sum >= 2k those two bad cases are DISJOINT (both
#   groups cannot be under k at once), so
#
#       answer = 2^n - 2 * (# subsets with sum < k)
#
#   and the "# subsets with sum < k" is a bounded knapsack count: f[j] = number
#   of subsets summing to exactly j, for j in [0, k-1].
#
#   NOTE : the sum(nums) < 2 * k early return is not just an optimisation — it
#          is what makes the two bad cases disjoint, so the formula above is
#          only valid after it.
#
#   NOTE : nums[i] can be up to 1e9, way past k. such an element can never fit
#          in a sub-k subset, so the inner loop simply never picks it up (the
#          `j >= v` guard) — no overflow of the dp table.
#
# time = O(n * k), space = O(k)
class Solution(object):
    def countPartitions(self, nums, k):
        MOD = 10 ** 9 + 7

        if sum(nums) < 2 * k:
            return 0

        n = len(nums)
        # f[j] = number of subsets whose sum is exactly j  (j < k)
        f = [0] * k
        f[0] = 1
        for v in nums:
            if v < k:
                for j in range(k - 1, v - 1, -1):
                    f[j] = (f[j] + f[j - v]) % MOD

        total = pow(2, n, MOD)
        under = sum(f) % MOD
        return (total - 2 * under) % MOD
