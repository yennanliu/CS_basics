"""

1664. Ways to Make a Fair Array
Medium

You are given an integer array nums. You can choose exactly one index (0-indexed) and remove the
element. Notice that the index of the elements may change after the removal.

For example, if nums = [6,1,7,4,1]:

- Choosing to remove index 1 results in nums = [6,7,4,1].
- Choosing to remove index 2 results in nums = [6,1,4,1].
- Choosing to remove index 4 results in nums = [6,1,7,4].

An array is fair if the sum of the odd-indexed values equals the sum of the even-indexed values.

Return the number of indices that you could choose such that after the removal, nums is fair.


Example 1:

Input: nums = [2,1,6,4]
Output: 1
Explanation:
Remove index 0: [1,6,4] -> Even sum: 1 + 4 = 5. Odd sum: 6. Not fair.
Remove index 1: [2,6,4] -> Even sum: 2 + 4 = 6. Odd sum: 6. Fair.
Remove index 2: [2,1,4] -> Even sum: 2 + 4 = 6. Odd sum: 1. Not fair.
Remove index 3: [2,1,6] -> Even sum: 2 + 6 = 8. Odd sum: 1. Not fair.
There is 1 index that you can remove to make nums fair.

Example 2:

Input: nums = [1,1,1]
Output: 3
Explanation: You can remove any index and the remaining array is fair.

Example 3:

Input: nums = [1,2,3]
Output: 0
Explanation: You cannot make a fair array after removing any index.


Constraints:

1 <= nums.length <= 10^5
1 <= nums[i] <= 10^4

"""

# V0
# IDEA : PREFIX / SUFFIX PARITY SUMS (removing index i flips the parity of the tail)
#
#   if we delete index i:
#     - everything BEFORE i keeps its parity
#     - everything AFTER  i shifts left by one -> its parity FLIPS
#
#   so with
#     preEven / preOdd  = parity sums over nums[0 .. i-1]
#     sufEven / sufOdd  = parity sums over nums[i+1 .. n-1]
#   the new even sum = preEven + sufOdd, the new odd sum = preOdd + sufEven.
#
#   sweep i left -> right maintaining both prefix and suffix sums in O(1) each step.
#
# time = O(n), space = O(1)
class Solution(object):
    def waysToMakeFair(self, nums):
        n = len(nums)
        sufEven = sum(nums[0::2])
        sufOdd = sum(nums[1::2])
        preEven = preOdd = 0
        res = 0
        for i in range(n):
            v = nums[i]
            # take nums[i] out of the suffix (it is the removed element)
            if i % 2 == 0:
                sufEven -= v
            else:
                sufOdd -= v

            if preEven + sufOdd == preOdd + sufEven:
                res += 1

            # nums[i] now belongs to the prefix for the next iteration
            if i % 2 == 0:
                preEven += v
            else:
                preOdd += v
        return res
