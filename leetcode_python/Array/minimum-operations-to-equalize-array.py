"""

3674. Minimum Operations to Equalize Array
Easy

You are given an integer array nums of length n.

In one operation, choose any subarray nums[l...r] (0 <= l <= r < n) and
replace each element in that subarray with the bitwise AND of all elements.

Return the minimum number of operations required to make all elements of
nums equal.

A subarray is a contiguous non-empty sequence of elements within an array.

Example 1:

Input: nums = [1,2]
Output: 1
Explanation:
Choose nums[0...1]: (1 AND 2) = 0, so the array becomes [0, 0] and all
elements are equal in 1 operation.

Example 2:

Input: nums = [5,5,5]
Output: 0
Explanation:
nums is [5, 5, 5] which already has all elements equal, so 0 operations are
required.

Constraints:

1 <= n == nums.length <= 100
1 <= nums[i] <= 10^5

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
