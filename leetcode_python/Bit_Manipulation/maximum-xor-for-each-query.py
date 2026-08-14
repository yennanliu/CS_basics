"""

1829. Maximum XOR for Each Query
Medium

You are given a sorted array nums of n non-negative integers and an integer maximumBit. You want to perform the following query n times:

Find a non-negative integer k < 2^maximumBit such that nums[0] XOR nums[1] XOR ... XOR nums[nums.length-1] XOR k is maximized. k is the answer to the ith query.
Remove the last element from the current array nums.

Return an array answer, where answer[i] is the answer to the ith query.


Example 1:

Input: nums = [0,1,1,3], maximumBit = 2
Output: [0,3,2,3]
Explanation: The queries are answered as follows:
1st query: nums = [0,1,1,3], k = 0 since 0 XOR 1 XOR 1 XOR 3 XOR 0 = 3.
2nd query: nums = [0,1,1], k = 3 since 0 XOR 1 XOR 1 XOR 3 = 3.
3rd query: nums = [0,1], k = 2 since 0 XOR 1 XOR 2 = 3.
4th query: nums = [0], k = 3 since 0 XOR 3 = 3.

Example 2:

Input: nums = [2,3,4,7], maximumBit = 3
Output: [5,2,6,5]
Explanation: The queries are answered as follows:
1st query: nums = [2,3,4,7], k = 5 since 2 XOR 3 XOR 4 XOR 7 XOR 5 = 7.
2nd query: nums = [2,3,4], k = 2 since 2 XOR 3 XOR 4 XOR 2 = 7.
3rd query: nums = [2,3], k = 6 since 2 XOR 3 XOR 6 = 7.
4th query: nums = [2], k = 5 since 2 XOR 5 = 7.

Example 3:

Input: nums = [0,1,2,2,5,7], maximumBit = 3
Output: [4,3,6,4,6,7]


Constraints:

nums.length == n
1 <= n <= 10^5
1 <= maximumBit <= 20
0 <= nums[i] < 2^maximumBit
nums is sorted in ascending order.

"""

# V0
# IDEA : RUNNING PREFIX XOR + COMPLEMENT UNDER THE MASK
#
#   let x be the xor of the current array. we want k < 2^maximumBit maximising
#   x ^ k. every value fits in maximumBit bits, so the best possible result is
#   all-ones, i.e. mask = 2^maximumBit - 1, and the unique k achieving it is
#     k = x ^ mask
#   (flip exactly the bits of x that are 0 within the mask).
#
#   the queries strip the array from the right, so walk nums backwards and xor
#   the removed element out of x each step -- no recomputation.
#
#   NOTE : answers come out in query order naturally because query i uses the
#          prefix of length n - i.
#
# time = O(n), space = O(1) extra (O(n) output)
class Solution(object):
    def getMaximumXor(self, nums, maximumBit):
        mask = (1 << maximumBit) - 1
        x = 0
        for v in nums:
            x ^= v

        res = []
        for v in reversed(nums):
            res.append(x ^ mask)
            x ^= v
        return res
