"""

3550. Smallest Index With Digit Sum Equal to Index
Easy

You are given an integer array nums.

Return the smallest index i such that the sum of the digits of nums[i] is equal
to i.

If no such index exists, return -1.


Example 1:

Input: nums = [1,3,2]
Output: 2
Explanation:
For nums[0] = 1, the digit sum is 1, but index 0 != 1.
For nums[1] = 3, the digit sum is 3, but index 1 != 3.
For nums[2] = 2, the digit sum is 2, and index 2 == 2. Thus, the answer is 2.

Example 2:

Input: nums = [1,10,11]
Output: 1
Explanation:
For nums[0] = 1, the digit sum is 1, but index 0 != 1.
For nums[1] = 10, the digit sum is 1 + 0 = 1, and index 1 == 1. Thus, the answer
is 1.
For nums[2] = 11, the digit sum is 1 + 1 = 2, but index 2 != 2.

Example 3:

Input: nums = [1,2,3]
Output: -1
Explanation:
Since no index satisfies the condition, the answer is -1.


Constraints:

1 <= nums.length <= 100
0 <= nums[i] <= 1000

"""

# V0
# IDEA : SCAN LEFT TO RIGHT AND STOP AT THE FIRST HIT
#
#   because we want the *smallest* index, a plain left-to-right scan returning
#   on the first match is already optimal — no need to collect all matches.
#
#   digit sums are cheap: nums[i] <= 1000 so the digit sum never exceeds 28,
#   which also means only the first 29 indices can ever match.
#
# time = O(n * d), space = O(1)
class Solution(object):
    def smallestIndex(self, nums):
        for i, v in enumerate(nums):
            if sum(int(c) for c in str(v)) == i:
                return i
        return -1
