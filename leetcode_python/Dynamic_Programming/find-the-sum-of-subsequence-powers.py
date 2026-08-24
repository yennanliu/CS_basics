"""

3098. Find the Sum of Subsequence Powers
Hard

You are given an integer array nums of length n, and a positive integer k.

The power of a subsequence is defined as the minimum absolute difference between any two elements in the subsequence.

Return the sum of powers of all subsequences of nums which have length equal to k.

Since the answer may be large, return it modulo 10^9 + 7.


Example 1:

Input: nums = [1,2,3,4], k = 3
Output: 4
Explanation:
There are 4 subsequences in nums which have length 3: [1,2,3], [1,3,4], [1,2,4], and [2,3,4]. The sum of powers is |2 - 3| + |3 - 4| + |2 - 1| + |3 - 4| = 4.

Example 2:

Input: nums = [2,2], k = 2
Output: 0
Explanation:
The only subsequence in nums which has length 2 is [2,2]. The sum of powers is |2 - 2| = 0.

Example 3:

Input: nums = [4,3,-1], k = 2
Output: 10
Explanation:
There are 3 subsequences in nums which have length 2: [4,3], [4,-1], and [3,-1]. The sum of powers is |4 - 3| + |4 - (-1)| + |3 - (-1)| = 10.


Constraints:

2 <= n == nums.length <= 50
-10^8 <= nums[i] <= 10^8
2 <= k <= n

"""

# V0
# IDEA : SUM THE MINIMUM BY *LAYERS* — count(power >= d) SUMMED OVER d
#
#   for a non-negative quantity,  value = sum over d >= 1 of [value >= d],
#   so the answer equals
#
#       sum over d >= 1 of  #{ length-k subsequences with min gap >= d }
#
#   the count only changes at the distinct pairwise differences, so let those
#   be D_1 < D_2 < ... and collapse the sum to
#
#       sum over t of (D_t - D_{t-1}) * count(min gap >= D_t)
#
#   with only 50 values there are at most 1225 distinct differences.
#
#   counting for a fixed d : sort nums; then "min gap >= d" means every pair
#   of CONSECUTIVE chosen elements is at least d apart. so
#       f[i][j] = ways to choose j elements ending at index i
#               = sum of f[t][j-1] over t with nums[i] - nums[t] >= d
#   and since nums is sorted the eligible t form a prefix — a moving pointer
#   plus prefix sums makes each d cost O(n * k).
#
"""

DP def
    sum the MINIMUM by LAYERS: for a non-negative value,
    value = sum over d >= 1 of [value >= d], so

        ans = sum over d >= 1 of #{ length-k subsequences with min gap >= d }

    the count only changes at the DISTINCT pairwise differences D_1 < D_2 <...
    so it collapses to  sum over t of (D_t - D_{t-1}) * count(min gap >= D_t)

    f[j][i]: (for a fixed d, nums SORTED) ways to choose j elements with

             all consecutive gaps >= d, ENDING exactly at index i

DP eq

     f[j][i] = sum of f[j-1][t]  for t with nums[i] - nums[t] >= d


    -> e.g. nums is sorted, so the eligible t form a PREFIX
              -> a moving pointer + prefix sums makes each d cost O(n * k)

     init: f[1][i] = 1
     count_at_least(d) = sum over i of f[k][i]

     with n <= 50 there are at most 1225 distinct differences

"""
# time = O(n^2 * n * k) = O(n^3 * k) worst case, space = O(n * k)
class Solution(object):
    def sumOfPowers(self, nums, k):
        MOD = 10 ** 9 + 7
        nums = sorted(nums)
        n = len(nums)

        diffs = set([0])
        for i in range(n):
            for j in range(i + 1, n):
                diffs.add(nums[j] - nums[i])
        diffs = sorted(diffs)

        def count_at_least(d):
            """length-k subsequences whose consecutive gaps are all >= d."""
            # f[j][i] = ways of length j ending exactly at index i
            f = [[0] * n for _ in range(k + 1)]
            for i in range(n):
                f[1][i] = 1
            for j in range(2, k + 1):
                pre = [0] * (n + 1)             # prefix sums of f[j-1]
                for i in range(n):
                    pre[i + 1] = (pre[i] + f[j - 1][i]) % MOD
                t = 0                           # first index with nums[i]-nums[t] < d
                for i in range(n):
                    while t < i and nums[i] - nums[t] >= d:
                        t += 1
                    f[j][i] = pre[t] % MOD      # indices 0..t-1 are far enough
            return sum(f[k]) % MOD

        res = 0
        for t in range(1, len(diffs)):
            gap = (diffs[t] - diffs[t - 1]) % MOD
            res = (res + gap * count_at_least(diffs[t])) % MOD
        return res
