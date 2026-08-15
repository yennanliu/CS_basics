"""

3165. Maximum Sum of Subsequence With Non-adjacent Elements
Hard

You are given an array nums consisting of integers. You are also given a 2D array queries, where queries[i] = [pos_i, x_i].

For query i, we first set nums[pos_i] equal to x_i, then we calculate the answer to query i which is the maximum sum of a subsequence of nums where no two adjacent elements are selected.

Return the sum of the answers to all queries.

Since the final answer may be very large, return it modulo 10^9 + 7.

A subsequence is an array that can be derived from another array by deleting some or no elements without changing the order of the remaining elements.


Example 1:

Input: nums = [3,5,9], queries = [[1,-2],[0,-3]]
Output: 21
Explanation:
After the 1st query, nums = [3,-2,9] and the maximum sum of a subsequence with non-adjacent elements is 3 + 9 = 12.
After the 2nd query, nums = [-3,-2,9] and the maximum sum of a subsequence with non-adjacent elements is 9.

Example 2:

Input: nums = [0,-1], queries = [[0,-5]]
Output: 0
Explanation:
After the 1st query, nums = [-5,-1] and the maximum sum of a subsequence with non-adjacent elements is 0 (choosing an empty subsequence).


Constraints:

1 <= nums.length <= 5 * 10^4
-10^5 <= nums[i] <= 10^5
1 <= queries.length <= 5 * 10^4
queries[i] == [pos_i, x_i]
0 <= pos_i <= nums.length - 1
-10^5 <= x_i <= 10^5

"""

# V0
# IDEA : SEGMENT TREE WHOSE NODES CARRY A 2x2 "BOUNDARY" TABLE
#
#   the classic house-robber DP is a left-to-right scan, which a point update
#   would invalidate. to make it updatable, store per segment the four
#   answers
#
#       f[a][b] = best sum inside this segment, where a says whether its
#                 FIRST element is taken and b whether its LAST is
#
#   merging two children is then a small max-plus product : the only illegal
#   combination is left's last taken together with right's first taken, so
#
#       res[i][j] = max(L[i][0] + R[0][j],
#                       L[i][0] + R[1][j],
#                       L[i][1] + R[0][j])
#
#   a leaf holding v has f[1][1] = v and f[0][0] = 0, the mixed entries being
#   impossible (-inf). the root's maximum is the answer, and f[0][0] = 0
#   covers the empty subsequence for all-negative arrays.
#
#   each query is one leaf write plus O(log n) merges.
#
# time = O((n + q) log n), space = O(n)
class Solution(object):
    def maximumSumSubsequence(self, nums, queries):
        MOD = 10 ** 9 + 7
        n = len(nums)
        NEG = float('-inf')

        size = 1
        while size < n:
            size <<= 1
        # node = [f00, f01, f10, f11]
        tree = [[NEG] * 4 for _ in range(2 * size)]

        def leaf(v):
            # f00 = take nothing, f11 = take the single element
            return [0, NEG, NEG, v]

        def merge(L, R):
            out = [NEG] * 4
            for i in range(2):
                for j in range(2):
                    li0, li1 = L[i * 2], L[i * 2 + 1]
                    r0j, r1j = R[j], R[2 + j]
                    best = NEG
                    if li0 != NEG:
                        if r0j != NEG:
                            best = max(best, li0 + r0j)
                        if r1j != NEG:
                            best = max(best, li0 + r1j)
                    if li1 != NEG and r0j != NEG:
                        best = max(best, li1 + r0j)
                    out[i * 2 + j] = best
            return out

        for i in range(size):
            tree[size + i] = leaf(nums[i]) if i < n else [0, NEG, NEG, NEG]
        for i in range(size - 1, 0, -1):
            tree[i] = merge(tree[2 * i], tree[2 * i + 1])

        res = 0
        for pos, x in queries:
            i = size + pos
            tree[i] = leaf(x)
            i >>= 1
            while i:
                tree[i] = merge(tree[2 * i], tree[2 * i + 1])
                i >>= 1
            best = max(v for v in tree[1] if v != NEG)
            res = (res + max(best, 0)) % MOD
        return res
