"""

3653. XOR After Range Multiplication Queries I
Medium

You are given an integer array nums of length n and a 2D integer array queries of size q, where queries[j] = [lj, rj, kj, vj].

For each query, apply the following operations in order:

Set idx = lj.
While idx <= rj:
  Update nums[idx] = (nums[idx] * vj) % (10^9 + 7).
  Set idx = idx + kj.

Return the bitwise XOR of all elements in nums after processing all queries.


Example 1:

Input: nums = [1,1,1], queries = [[0,2,1,4]]
Output: 4
Explanation:
The query multiplies nums[0], nums[1] and nums[2] by 4, so nums becomes [4,4,4].
The bitwise XOR of all elements is 4 XOR 4 XOR 4 = 4.


Constraints:

1 <= n == nums.length <= 10^3
1 <= nums[i] <= 10^9
1 <= q == queries.length <= 10^3
queries[j].length == 4
0 <= lj <= rj < n
1 <= kj <= n
1 <= vj <= 10^9

"""

# V0
# IDEA : DIRECT SIMULATION — THE ARITHMETIC PROGRESSION IS SHORT ENOUGH
#
#   one query touches ceil((r - l + 1) / k) cells, which is at most n. with
#   n and q both bounded by 1000 the total work is at most 10^6 modular
#   multiplications, so nothing clever is required (the "II" version raises
#   the bounds and does need a per-k difference trick).
#
#   the only subtlety is that values must stay reduced mod 10^9+7 as the
#   problem prescribes, so the final XOR is taken over the reduced values.
#
# time = O(q * n), space = O(1)
class Solution(object):
    def xorAfterQueries(self, nums, queries):
        MOD = 10 ** 9 + 7
        nums = list(nums)
        for l, r, k, v in queries:
            for idx in range(l, r + 1, k):
                nums[idx] = (nums[idx] * v) % MOD
        res = 0
        for x in nums:
            res ^= x
        return res
