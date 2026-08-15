"""

3354. Make Array Elements Equal to Zero
Easy

You are given an integer array nums.

Start by selecting a starting position curr such that nums[curr] == 0, and choose a movement direction of either left or right.

After that, you repeat the following process:

If curr is out of the range [0, n - 1], this process ends.
If nums[curr] == 0, move in the current direction by incrementing curr if you are moving right, or decrementing curr if you are moving left.
Else if nums[curr] > 0:
    Decrement nums[curr] by 1.
    Reverse your movement direction (left becomes right and vice versa).
    Take a step in your new direction.

A selection of the initial position curr and movement direction is considered valid if every element in nums becomes 0 by the end of the process.

Return the number of possible valid selections.


Example 1:

Input: nums = [1,0,2,0,3]
Output: 2
Explanation:
The only possible valid selections are the following:
Choose curr = 3, and a movement direction to the left.
    [1,0,2,0,3] -> [1,0,2,0,3] -> [1,0,1,0,3] -> [1,0,1,0,3] -> [1,0,1,0,2] -> [1,0,1,0,2] -> [1,0,0,0,2] -> [1,0,0,0,2] -> [1,0,0,0,1] -> [1,0,0,0,1] -> [0,0,0,0,1] -> [0,0,0,0,1] -> [0,0,0,0,0].
Choose curr = 3, and a movement direction to the right.
    [1,0,2,0,3] -> [1,0,2,0,3] -> [1,0,2,0,2] -> [1,0,2,0,2] -> [1,0,1,0,2] -> [1,0,1,0,2] -> [1,0,1,0,1] -> [1,0,1,0,1] -> [1,0,0,0,1] -> [1,0,0,0,1] -> [0,0,0,0,1] -> [0,0,0,0,1] -> [0,0,0,0,0].

Example 2:

Input: nums = [2,3,4,0,4,1,0]
Output: 0
Explanation:
There are no possible valid selections.


Constraints:

1 <= nums.length <= 100
0 <= nums[i] <= 100
There is at least one element i where nums[i] == 0.

"""

# V0
# IDEA : THE INPUT IS SMALL — REPLAY THE WHOLE PROCESS FOR EACH START
#
#   there are at most n starting zeros and two directions, and every bounce
#   decrements some element, so a run ends after at most sum(nums) bounces
#   plus the walking. with n <= 100 and values <= 100 that is a few thousand
#   steps per attempt.
#
#   so the honest simulation is affordable and needs no invariant : copy the
#   array, run the rules, and check whether it ended all zero.
#
# time = O(n * (n + sum(nums))), space = O(n)
class Solution(object):
    def countValidSelections(self, nums):
        n = len(nums)
        res = 0
        for start in range(n):
            if nums[start] != 0:
                continue
            for step in (1, -1):
                arr = list(nums)
                curr = start
                d = step
                while 0 <= curr < n:
                    if arr[curr] == 0:
                        curr += d
                    else:
                        arr[curr] -= 1
                        d = -d
                        curr += d
                if all(v == 0 for v in arr):
                    res += 1
        return res
