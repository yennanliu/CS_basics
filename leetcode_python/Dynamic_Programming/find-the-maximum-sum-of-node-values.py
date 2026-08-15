"""

3068. Find the Maximum Sum of Node Values
Hard

There exists an undirected tree with n nodes numbered 0 to n - 1. You are given a 0-indexed 2D integer array edges of length n - 1, where edges[i] = [ui, vi] indicates that there is an edge between nodes ui and vi in the tree. You are also given a positive integer k, and a 0-indexed array of non-negative integers nums of length n, where nums[i] represents the value of the node numbered i.

Alice wants the sum of values of tree nodes to be maximum, for which Alice can perform the following operation any number of times (including zero) on the tree:

Choose any edge [u, v] connecting the nodes u and v, and update their values as follows:
    nums[u] = nums[u] XOR k
    nums[v] = nums[v] XOR k

Return the maximum possible sum of the values Alice can achieve by performing the operation any number of times.


Example 1:

Input: nums = [1,2,1], k = 3, edges = [[0,1],[0,2]]
Output: 6
Explanation: Alice can achieve the maximum sum of 6 using a single operation:
- Choose the edge [0,2]. nums becomes [1 XOR 3, 2, 1 XOR 3] = [2, 2, 2].
The total sum of values is 2 + 2 + 2 = 6.
It can be shown that 6 is the maximum achievable sum of values.

Example 2:

Input: nums = [2,3], k = 7, edges = [[0,1]]
Output: 9
Explanation: Alice can achieve the maximum sum of 9 using a single operation:
- Choose the edge [0,1]. nums becomes [2 XOR 7, 3 XOR 7] = [5, 4].
The total sum of values is 5 + 4 = 9.
It can be shown that 9 is the maximum achievable sum of values.

Example 3:

Input: nums = [7,7,7,7,7,7], k = 3, edges = [[0,1],[0,2],[0,3],[0,4],[0,5]]
Output: 42
Explanation: The maximum achievable sum is 42 which can be achieved by Alice performing no operations.


Constraints:

2 <= n == nums.length <= 2 * 10^4
1 <= k <= 10^9
0 <= nums[i] <= 10^9
edges.length == n - 1
edges[i].length == 2
0 <= edges[i][0], edges[i][1] <= n - 1
The input is generated such that edges represent a valid tree.

"""

# V0
# IDEA : THE TREE IS A RED HERRING — ANY *EVEN-SIZED* SUBSET CAN BE FLIPPED
#
#   one operation flips two endpoints of an edge. chaining operations along a
#   path u -> ... -> v flips every interior node TWICE (back to its original
#   value) and the two ends once. since a tree connects every pair, any two
#   nodes can be flipped together — and therefore any subset of EVEN size,
#   and nothing of odd size (each operation changes the flipped count by 0
#   or 2, so its parity is invariant).
#
#   so the problem reduces to : pick an even number of nodes to XOR with k,
#   maximising the total. a two-state DP over the nodes does it :
#
#       even = best total so far having flipped an even count
#       odd  = best total so far having flipped an odd count
#
#   each node is either left alone (+x) or flipped (+ x^k, parity switches).
#   the answer is `even` at the end.
#
# time = O(n), space = O(1)
class Solution(object):
    def maximumValueSum(self, nums, k, edges):
        even, odd = 0, float('-inf')
        for x in nums:
            flipped = x ^ k
            even, odd = (max(even + x, odd + flipped),
                         max(odd + x, even + flipped))
        return even
