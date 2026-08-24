"""

629. K Inverse Pairs Array
Hard

For an integer array nums, an inverse pair is a pair of integers [i, j] where
0 <= i < j < nums.length and nums[i] > nums[j].

Given two integers n and k, return the number of different arrays consisting of
numbers from 1 to n such that there are exactly k inverse pairs.
Since the answer can be huge, return it modulo 10^9 + 7.

Example 1:

Input: n = 3, k = 0
Output: 1
Explanation: Only the array [1,2,3] which consists of numbers from 1 to 3 has exactly 0 inverse pairs.

Example 2:

Input: n = 3, k = 1
Output: 2
Explanation: The array [1,3,2] and [2,1,3] have exactly 1 inverse pair.

Constraints:

1 <= n <= 1000
0 <= k <= 1000

"""

# V0
# IDEA : DP + PREFIX SUM (rolling 1D array)
#
#  DP def:
#    - f[i][j] = # of permutations of 1..i having exactly j inverse pairs
#
#  Transition: build the permutation of 1..i by inserting the largest number i
#  into a permutation of 1..i-1. Inserting i at the position that leaves t
#  numbers to its right creates exactly t new inverse pairs (i is bigger than
#  everything after it), with t in [0, i-1]. So:
#
#    f[i][j] = sum_{t=0}^{i-1} f[i-1][j-t]
#            = prefix[j] - prefix[j - i]        (a sliding window of width i)
#
#  The inner sum is a contiguous window of the previous row -> use prefix sums
#  so each row costs O(k) instead of O(k * i).
#
# time = O(n * k)
# space = O(k)
class Solution(object):
    def kInversePairs(self, n, k):
        MOD = 10 ** 9 + 7

        # f = row for i = 0 : the empty permutation has 0 inverse pairs
        f = [0] * (k + 1)
        f[0] = 1

        for i in range(1, n + 1):
            # prefix[j] = f[0] + ... + f[j-1]
            prefix = [0] * (k + 2)
            for j in range(k + 1):
                prefix[j + 1] = (prefix[j] + f[j]) % MOD

            g = [0] * (k + 1)
            for j in range(k + 1):
                # window is f[lo .. j], where lo = max(0, j - (i - 1))
                lo = max(0, j - (i - 1))
                g[j] = (prefix[j + 1] - prefix[lo]) % MOD
            f = g

        return f[k] % MOD


# V1
# IDEA : PLAIN 2D DP (no prefix sum) -- clearer, but O(n * k^2), TLE on max input
"""

DP def
    f[i][j]: number of permutations of 1..i having EXACTLY j inverse pairs

DP eq

     build a permutation of 1..i by INSERTING the largest number i into a
     permutation of 1..i-1. inserting i so that t numbers sit to its right
     creates exactly t new inverse pairs, with t in [0, i-1]:

     f[i][j] = sum over t = 0..i-1 of f[i-1][j-t]

             = prefix[j+1] - prefix[max(0, j-i+1)]


    -> e.g. that inner sum is a CONTIGUOUS WINDOW of width i over the
              previous row, so prefix sums make each row O(k) instead of
              O(k * i)

     init: f[0][0] = 1 (the empty permutation has 0 inverse pairs)
     ans = f[n][k] % (10^9 + 7)

"""
# time = O(n * k * k)
# space = O(n * k)
class Solution(object):
    def kInversePairs(self, n, k):
        MOD = 10 ** 9 + 7
        f = [[0] * (k + 1) for _ in range(n + 1)]
        f[0][0] = 1
        for i in range(1, n + 1):
            for j in range(k + 1):
                # insert i so that t numbers end up to its right
                for t in range(min(i - 1, j) + 1):
                    f[i][j] = (f[i][j] + f[i - 1][j - t]) % MOD
        return f[n][k]
