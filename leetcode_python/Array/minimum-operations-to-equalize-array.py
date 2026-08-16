"""

3674. Minimum Operations to Equalize Array
Easy

You are given an integer array nums of length n.

In one operation, you may choose any subarray nums[l...r] and replace each element of that subarray with the bitwise AND of all elements of the subarray.

Return the minimum number of operations needed to make all elements of nums equal.


Example 1:

Input: nums = [1,2,3]
Output: 1
Explanation:
Apply the operation on the whole array, replacing every element with 1 & 2 & 3 = 0, so nums becomes [0,0,0].

Example 2:

Input: nums = [5,5,5]
Output: 0
Explanation:
All elements are already equal, so no operation is needed.


Constraints:

1 <= n == nums.length <= 10^5
0 <= nums[i] <= 10^9

"""

# V0
# IDEA : ONE WHOLE-ARRAY OPERATION ALWAYS SUFFICES
#
#   applying the operation to the entire array replaces every element with
#   the AND of all of them, which is one value repeated n times — equal by
#   construction. so the answer is never more than 1.
#
#   it is 0 exactly when the array is already uniform, and otherwise 1,
#   because a single operation on a proper subarray cannot touch the elements
#   outside it and therefore cannot equalise a non-uniform array in fewer
#   than one move.
#
# time = O(n), space = O(1)
class Solution(object):
    def minOperations(self, nums):
        first = nums[0]
        for x in nums:
            if x != first:
                return 1
        return 0
