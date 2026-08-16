"""

3514. Number of Unique XOR Triplets II
Medium

You are given an integer array nums.

A XOR triplet is defined as the XOR of three elements nums[i] XOR nums[j] XOR
nums[k] where i <= j <= k.

Return the number of unique XOR triplet values from all possible triplets
(i, j, k).


Example 1:

Input: nums = [1,3]
Output: 2
Explanation:
The possible XOR triplet values are:
(0, 0, 0) -> 1 XOR 1 XOR 1 = 1
(0, 0, 1) -> 1 XOR 1 XOR 3 = 3
(0, 1, 1) -> 1 XOR 3 XOR 3 = 1
(1, 1, 1) -> 3 XOR 3 XOR 3 = 3
The unique XOR values are {1, 3}, so the output is 2.

Example 2:

Input: nums = [6,7,8,9]
Output: 4
Explanation:
The possible XOR triplet values are {6, 7, 8, 9}. Thus, the output is 4.


Constraints:

1 <= nums.length <= 1500
1 <= nums[i] <= 1500

"""

# V0
# IDEA : PAIR XORS FIRST, THEN ONE MORE XOR PASS
#
#   i <= j <= k with repeats allowed means the reachable set is
#   { a ^ b ^ c : a, b, c in nums }, which already contains every nums[k]
#   (take a == b).
#
#   values are bounded by 1500 < 2048, so any xor also stays below 2048.  that
#   means the set of pairwise xors has at most 2048 members no matter how big
#   nums is — compute it in O(n^2), then xor that small set against the
#   (also <= 2048) distinct values of nums.
#
#   the second stage is therefore O(2048 * 2048) at worst rather than O(n^3).
#
# time = O(n^2 + V^2) with V = 2048, space = O(V)
class Solution(object):
    def uniqueXorTriplets(self, nums):
        vals = sorted(set(nums))
        pair = set()
        for a in vals:
            for b in vals:
                pair.add(a ^ b)
        res = set()
        for p in pair:
            for c in vals:
                res.add(p ^ c)
        return len(res)
