"""

3523. Make Array Non-decreasing
Medium

You are given an integer array nums. In one operation, you can select a subarray
and replace it with a single element equal to its maximum value.

Return the maximum possible size of the array after performing zero or more
operations.


Example 1:

Input: nums = [4,2,5,3,5]
Output: 3
Explanation:
Replace the subarray nums[1..3] = [2, 5, 3] with its maximum 5. The array
becomes [4, 5, 5], which is non-decreasing and has size 3. No sequence of
operations leaves a larger non-decreasing array.

Example 2:

Input: nums = [1,2,3]
Output: 3
Explanation:
No operation is needed as the array [1,2,3] is already non-decreasing.


Constraints:

1 <= nums.length <= 2 * 10^5
1 <= nums[i] <= 2 * 10^5

"""

# V0
# IDEA : GREEDY — MERGE A DIP INTO THE BLOCK ON ITS LEFT
#
#   any sequence of operations ends up partitioning nums into consecutive
#   blocks, each collapsed to its own maximum.  so the task is: cut nums into
#   as many blocks as possible with non-decreasing block maxima.
#
#   scan left to right holding `cur`, the max of the block we just closed.  if
#   the next value is >= cur it can start a fresh block, which is never worse
#   than swallowing it (more blocks, and the running max stays as small as
#   possible).  if it is < cur it cannot start a block, so it must be absorbed
#   into the current one — and absorbing it does not change that block's max.
#
# time = O(n), space = O(1)
class Solution(object):
    def maximumPossibleSize(self, nums):
        cur = 0
        blocks = 0
        for v in nums:
            if v >= cur:
                blocks += 1
                cur = v
        return blocks
