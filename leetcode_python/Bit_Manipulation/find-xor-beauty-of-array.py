"""

2527. Find Xor-Beauty of Array
Medium

You are given a 0-indexed integer array nums.

The effective value of three indices i, j, and k is defined as
((nums[i] | nums[j]) & nums[k]).

The xor-beauty of the array is the XORing of the effective values of all the
possible triplets of indices (i, j, k) where 0 <= i, j, k < n.

Return the xor-beauty of nums.

Note that:
val1 | val2 is bitwise OR of val1 and val2.
val1 & val2 is bitwise AND of val1 and val2.


Example 1:

Input: nums = [1,4]
Output: 5
Explanation:
The triplets and their corresponding effective values are listed below:
- (0,0,0) with effective value ((1 | 1) & 1) = 1
- (0,0,1) with effective value ((1 | 1) & 4) = 0
- (0,1,0) with effective value ((1 | 4) & 1) = 1
- (0,1,1) with effective value ((1 | 4) & 4) = 4
- (1,0,0) with effective value ((4 | 1) & 1) = 1
- (1,0,1) with effective value ((4 | 1) & 4) = 4
- (1,1,0) with effective value ((4 | 4) & 1) = 0
- (1,1,1) with effective value ((4 | 4) & 4) = 4
Xor-beauty of array will be bitwise XOR of all beauties = 1 ^ 0 ^ 1 ^ 4 ^ 1 ^ 4 ^ 0 ^ 4 = 5.

Example 2:

Input: nums = [15,45,20,2,34,35,5,44,32,30]
Output: 34
Explanation: The xor-beauty of the given array is 34.


Constraints:

1 <= nums.length <= 10^5
1 <= nums[i] <= 10^9

"""

# V0
# IDEA : BIT MANIPULATION / CANCELLATION -> ANSWER IS JUST XOR OF ALL ELEMENTS
#
#   the n^3 triplets cancel in pairs because XOR kills duplicates:
#
#   1) if i != j, the triplets (i, j, k) and (j, i, k) give the SAME effective
#      value (OR is commutative), so every such pair XORs to 0. Only i == j
#      survives, leaving terms of the form (nums[i] & nums[k]).
#
#   2) among those, if i != k then (i, k) and (k, i) both give
#      nums[i] & nums[k] (AND is commutative), so they cancel too.
#
#   3) only i == j == k remains, contributing (nums[i] | nums[i]) & nums[i]
#      = nums[i]. So the answer is XOR of every element.
#
#   NOTE : the cancellation relies on the triplets ranging over ALL ordered
#          (i, j, k) INCLUDING repeats -- the counting breaks if the problem
#          had asked for distinct indices.
#
# time = O(n), space = O(1)
class Solution(object):
    def xorBeauty(self, nums):
        res = 0
        for x in nums:
            res ^= x
        return res
