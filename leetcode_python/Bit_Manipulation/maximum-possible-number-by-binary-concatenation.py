"""

3309. Maximum Possible Number by Binary Concatenation
Medium

You are given an array of integers nums of size 3.

Return the maximum possible number whose binary representation can be formed by concatenating the binary representation of all elements in nums in some order.

Note that the binary representation of any number does not contain leading zeros.


Example 1:

Input: nums = [1,2,3]
Output: 30
Explanation:
Concatenate the numbers in the order [3, 1, 2] to get the result "11110", which is the binary representation of 30.

Example 2:

Input: nums = [2,8,16]
Output: 1296
Explanation:
Concatenate the numbers in the order [2, 8, 16] to get the result "10100010000", which is the binary representation of 1296.


Constraints:

nums.length == 3
1 <= nums[i] <= 127

"""

# V0
# IDEA : ONLY SIX ORDERINGS EXIST — BUILD THEM ALL
#
#   with exactly three numbers there are 3! = 6 concatenations, so no
#   ordering rule has to be derived (the usual "compare a+b vs b+a" trick
#   would work too, but enumerating is shorter and obviously correct).
#
#   bin(x)[2:] gives the representation without leading zeros, and int(..., 2)
#   reads the concatenation back.
#
# time = O(1), space = O(1)
import itertools


class Solution(object):
    def maxGoodNumber(self, nums):
        return max(int(''.join(bin(v)[2:] for v in perm), 2)
                   for perm in itertools.permutations(nums))
