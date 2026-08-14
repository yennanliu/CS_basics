"""

2454. Next Greater Element IV
Hard

You are given a 0-indexed array of non-negative integers nums. For each integer in nums, you must find its respective second greater integer.

The second greater integer of nums[i] is nums[j] such that:

j > i
nums[j] > nums[i]
There exists exactly one index k such that nums[k] > nums[i] and i < k < j.

If there is no such nums[j], the second greater integer is considered to be -1.

For example, in the array [1, 2, 4, 3], the second greater integer of 1 is 4, 2 is 3, and that of 3 and 4 is -1.

Return an integer array answer, where answer[i] is the second greater integer of nums[i].


Example 1:

Input: nums = [2,4,0,9,6]
Output: [9,6,6,-1,-1]
Explanation:
0th index: 4 is the first integer greater than 2, and 9 is the second integer greater than 2, to the right of 2.
1st index: 9 is the first, and 6 is the second integer greater than 4, to the right of 4.
2nd index: 9 is the first, and 6 is the second integer greater than 0, to the right of 0.
3rd index: There is no integer greater than 9 to its right, so the second greater integer is considered to be -1.
4th index: There is no integer greater than 6 to its right, so the second greater integer is considered to be -1.
Thus, we return [9,6,6,-1,-1].

Example 2:

Input: nums = [3,3]
Output: [-1,-1]
Explanation:
We return [-1,-1] since neither integer has any integer greater than it.


Constraints:

1 <= nums.length <= 10^5
0 <= nums[i] <= 10^9

"""

# V0
# IDEA : TWO MONOTONIC STACKS — "WAITING FOR THE FIRST" AND "WAITING FOR THE SECOND"
#
#   an index lives in `first` until something bigger appears; at that moment
#   it has met greater #1 and GRADUATES to `second`. the next time something
#   bigger appears it has met greater #2 and is answered.
#
#   per element x at index i :
#     1. resolve everything in `second` that x exceeds (these get their answer)
#     2. move everything in `first` that x exceeds over to `second`
#     3. push i onto `first`
#
#   step 2 must preserve the decreasing order, hence popping into a temp list
#   and pushing it back reversed — otherwise `second` would stop being
#   monotonic and step 1 could miss entries.
#
#   both stacks stay decreasing, and each index moves at most twice, so the
#   whole sweep is linear.
#
# time = O(n), space = O(n)
class Solution(object):
    def secondGreaterElement(self, nums):
        n = len(nums)
        res = [-1] * n
        first = []       # seen no greater element yet
        second = []      # seen exactly one greater element

        for i, x in enumerate(nums):
            while second and nums[second[-1]] < x:
                res[second.pop()] = x

            promoted = []
            while first and nums[first[-1]] < x:
                promoted.append(first.pop())
            second.extend(reversed(promoted))

            first.append(i)
        return res
