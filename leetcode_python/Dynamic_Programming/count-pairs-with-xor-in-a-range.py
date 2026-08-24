"""

1803. Count Pairs With XOR in a Range
Hard

Given a (0-indexed) integer array nums and two integers low and high, return the number of nice pairs.

A nice pair is a pair (i, j) where 0 <= i < j < nums.length and low <= (nums[i] XOR nums[j]) <= high.


Example 1:

Input: nums = [1,4,2,7], low = 2, high = 6
Output: 6
Explanation: All nice pairs (i, j) are as follows:
    - (0, 1): nums[0] XOR nums[1] = 5
    - (0, 2): nums[0] XOR nums[2] = 3
    - (0, 3): nums[0] XOR nums[3] = 6
    - (1, 2): nums[1] XOR nums[2] = 6
    - (1, 3): nums[1] XOR nums[3] = 3
    - (2, 3): nums[2] XOR nums[3] = 5

Example 2:

Input: nums = [9,8,4,2,1], low = 5, high = 14
Output: 8
Explanation: All nice pairs (i, j) are as follows:
    - (0, 2): nums[0] XOR nums[2] = 13
    - (0, 3): nums[0] XOR nums[3] = 11
    - (0, 4): nums[0] XOR nums[4] = 8
    - (1, 2): nums[1] XOR nums[2] = 12
    - (1, 3): nums[1] XOR nums[3] = 10
    - (1, 4): nums[1] XOR nums[4] = 9
    - (2, 3): nums[2] XOR nums[3] = 6
    - (2, 4): nums[2] XOR nums[4] = 5


Constraints:

1 <= nums.length <= 2 * 10^4
1 <= nums[i] <= 2 * 10^4
1 <= low <= high <= 2 * 10^4

"""

# V0
# IDEA : BINARY TRIE + PREFIX COUNTING (count(< high+1) - count(< low))
#
#   answer = f(high + 1) - f(low), where f(limit) counts pairs with xor < limit.
#
#   f is computed online : walk nums left -> right, for each x query the trie
#   of already-inserted values, then insert x. this makes every pair counted
#   exactly once with i < j.
#
#   query(x, limit) : descend bit 14..0. let v = bit of x, L = bit of limit.
#     - L == 1 : every stored y whose bit equals v gives xor bit 0 < 1 there,
#                so ALL of that subtree is already < limit -> add its count,
#                then keep walking the branch v^1 (xor bit = 1, still tied).
#     - L == 0 : xor bit must be 0 to stay <= , so walk branch v only.
#
#   NOTE : nums[i] <= 2*10^4 < 2^15, so 15 bits are enough.
#
"""

DP def
    ans = f(high + 1) - f(low), where

    f(limit): number of pairs (i < j) with (nums[i] ^ nums[j]) < limit

    the counting is done on a BINARY TRIE of the already-inserted values,
    node.cnt = how many stored values pass through that node

DP eq

     query(x, limit): descend bits 14..0, v = bit of x, L = bit of limit

        if L == 1: every stored y whose bit == v gives xor bit 0 there,
                   so that WHOLE subtree is already < limit
                       res += cnt(child[v]);  then walk child[v ^ 1]

        if L == 0: the xor bit must be 0 to stay below
                       walk child[v] only


    -> e.g. scan nums left -> right: query the trie first, THEN insert x
              -> every pair is counted exactly once with i < j

     nums[i] <= 2*10^4 < 2^15, so 15 bits suffice

"""
# time = O(n * 15), space = O(n * 15)
class Trie(object):
    def __init__(self):
        self.children = [None, None]
        self.cnt = 0

    def insert(self, x):
        node = self
        for i in range(14, -1, -1):
            v = (x >> i) & 1
            if node.children[v] is None:
                node.children[v] = Trie()
            node = node.children[v]
            node.cnt += 1

    def count_less(self, x, limit):
        # number of stored y with (x ^ y) < limit
        node = self
        res = 0
        for i in range(14, -1, -1):
            if node is None:
                return res
            v = (x >> i) & 1
            if (limit >> i) & 1:
                if node.children[v]:
                    res += node.children[v].cnt
                node = node.children[v ^ 1]
            else:
                node = node.children[v]
        return res


class Solution(object):
    def countPairs(self, nums, low, high):
        root = Trie()
        res = 0
        for x in nums:
            res += root.count_less(x, high + 1) - root.count_less(x, low)
            root.insert(x)
        return res
