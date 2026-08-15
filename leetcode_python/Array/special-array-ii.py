"""

3152. Special Array II
Medium

An array is considered special if every pair of its adjacent elements contains two numbers with different parity.

You are given an array of integer nums and a 2D integer matrix queries, where for queries[i] = [from_i, to_i] your task is to check that subarray nums[from_i..to_i] is special or not.

Return an array of booleans answer such that answer[i] is true if nums[from_i..to_i] is special.


Example 1:

Input: nums = [3,4,1,2,6], queries = [[0,4]]
Output: [false]
Explanation:
The subarray is [3,4,1,2,6]. 2 and 6 are both even.

Example 2:

Input: nums = [4,3,1,6], queries = [[0,2],[2,3]]
Output: [false,true]
Explanation:
1. The subarray is [4,3,1]. 3 and 1 are both odd. So the answer to this query is false.
2. The subarray is [1,6]. There is only one pair: (1,6) and it contains numbers with different parity. So the answer to this query is true.


Constraints:

1 <= nums.length <= 10^5
1 <= nums[i] <= 10^5
1 <= queries.length <= 10^5
queries[i].length == 2
0 <= queries[i][0] <= queries[i][1] <= nums.length - 1

"""

# V0
# IDEA : PREFIX-COUNT THE *BAD* ADJACENT PAIRS, THEN EACH QUERY IS O(1)
#
#   a subarray is special exactly when it contains no adjacent pair of equal
#   parity. so mark each boundary i (between i-1 and i) as bad when the two
#   share a parity, and prefix-sum those marks.
#
#   the pairs inside nums[l..r] are the boundaries l+1 .. r, so the query is
#       bad[r + 1] - bad[l + 1] == 0
#   which also handles l == r (no boundaries, always special).
#
# time = O(n + q), space = O(n)
class Solution(object):
    def isArraySpecial(self, nums, queries):
        n = len(nums)
        bad = [0] * (n + 1)
        for i in range(1, n):
            bad[i + 1] = bad[i] + (1 if nums[i] % 2 == nums[i - 1] % 2 else 0)
        return [bad[r + 1] - bad[l + 1] == 0 for l, r in queries]
