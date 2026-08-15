"""

3356. Zero Array Transformation II
Medium

You are given an integer array nums of length n and a 2D array queries where queries[i] = [l_i, r_i, val_i].

Each queries[i] represents the following action on nums:

Decrement the value at each index in the range [l_i, r_i] in nums by at most val_i.
The amount by which each value is decremented can be chosen independently for each index.

A Zero Array is an array with all its elements equal to 0.

Return the minimum possible non-negative value of k, such that after processing the first k queries in sequence, nums becomes a Zero Array. If no such k exists, return -1.


Example 1:

Input: nums = [2,0,2], queries = [[0,2,1],[0,2,1],[1,1,3]]
Output: 2
Explanation:
For i = 0 (l = 0, r = 2, val = 1): Decrement values at indices [0, 1, 2] by [1, 0, 1] respectively. The array will become [1, 0, 1].
For i = 1 (l = 0, r = 2, val = 1): Decrement values at indices [0, 1, 2] by [1, 0, 1] respectively. The array will become [0, 0, 0], which is a Zero Array. Therefore, the minimum value of k is 2.

Example 2:

Input: nums = [4,3,2,1], queries = [[1,3,2],[0,2,1]]
Output: -1
Explanation:
For i = 0 (l = 1, r = 3, val = 2): Decrement values at indices [1, 2, 3] by [2, 2, 1] respectively. The array will become [4, 1, 0, 0].
For i = 1 (l = 0, r = 2, val = 1): Decrement values at indices [0, 1, 2] by [1, 1, 0] respectively. The array will become [3, 0, 0, 0], which is not a Zero Array.


Constraints:

1 <= nums.length <= 10^5
0 <= nums[i] <= 5 * 10^5
1 <= queries.length <= 10^5
queries[i].length == 3
0 <= l_i <= r_i < nums.length
1 <= val_i <= 5

"""

# V0
# IDEA : BINARY SEARCH ON THE PREFIX LENGTH, DIFFERENCE ARRAY FOR THE CHECK
#
#   using MORE queries can never hurt — each one may decrement by anything
#   from 0 to val — so feasibility is monotone in k and binary search
#   applies.
#
#   for a fixed k the amounts are independent per index, so index i is
#   satisfiable exactly when the total val of the first k queries covering it
#   reaches nums[i]. a difference array sums those vals in one pass.
#
# time = O((n + q) log q), space = O(n)
class Solution(object):
    def minZeroArray(self, nums, queries):
        n = len(nums)
        q = len(queries)

        def feasible(k):
            diff = [0] * (n + 1)
            for t in range(k):
                l, r, val = queries[t]
                diff[l] += val
                diff[r + 1] -= val
            cover = 0
            for i in range(n):
                cover += diff[i]
                if cover < nums[i]:
                    return False
            return True

        if all(v == 0 for v in nums):
            return 0
        lo, hi = 1, q
        res = -1
        while lo <= hi:
            mid = (lo + hi) // 2
            if feasible(mid):
                res = mid
                hi = mid - 1
            else:
                lo = mid + 1
        return res
