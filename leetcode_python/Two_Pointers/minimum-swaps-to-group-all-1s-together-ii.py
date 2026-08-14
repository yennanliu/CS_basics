"""

2134. Minimum Swaps to Group All 1's Together II
Medium

A swap is defined as taking two distinct positions in an array and swapping the values in them.

A circular array is defined as an array where we consider the first element and the last element to be adjacent.

Given a binary circular array nums, return the minimum number of swaps required to group all 1's present in the array together at any location.


Example 1:

Input: nums = [0,1,0,1,1,0,0]
Output: 1
Explanation: Here are a few of the ways to group all the 1's together:
[0,0,1,1,1,0,0] using 1 swap.
[0,1,1,1,0,0,0] using 1 swap.
[1,1,0,0,0,0,1] using 2 swaps (using the circular property of the array).
There is no way to group all 1's together with 0 swaps.
Thus, the minimum number of swaps required is 1.

Example 2:

Input: nums = [0,1,1,1,0,0,1,1,0]
Output: 2
Explanation: Here are a few of the ways to group all the 1's together:
[1,1,1,0,0,0,0,1,1] using 2 swaps (using the circular property of the array).
[1,1,1,1,1,0,0,0,0] using 2 swaps.
There is no way to group all the 1's together with 0 or 1 swaps.
Thus, the minimum number of swaps required is 2.

Example 3:

Input: nums = [1,1,0,0,1]
Output: 0
Explanation: All the 1's are already grouped together due to the circular property of the array.
Thus, the minimum number of swaps required is 0.


Constraints:

1 <= nums.length <= 10^5
nums[i] is either 0 or 1.

"""

# V0
# IDEA : FIXED-WIDTH CIRCULAR SLIDING WINDOW — MINIMIZE THE ZEROS INSIDE
#
#   in the final array all k ones occupy one contiguous (circular) block of
#   width k = total number of 1s. every 0 currently sitting inside that block
#   costs exactly one swap, so the answer is
#       min over all circular windows of width k of (zeros inside)
#
#   handle the wrap-around by indexing modulo n and running the window for
#   n positions.
#
#   NOTE : k == 0 (no ones at all) needs no swaps.
#
# time = O(n), space = O(1)
class Solution(object):
    def minSwaps(self, nums):
        n = len(nums)
        k = sum(nums)
        if k == 0 or k == n:
            return 0

        zeros = sum(1 for i in range(k) if nums[i] == 0)
        res = zeros
        for i in range(1, n):
            # the window slides from [i-1, i+k-2] to [i, i+k-1]
            if nums[i - 1] == 0:
                zeros -= 1
            if nums[(i + k - 1) % n] == 0:
                zeros += 1
            res = min(res, zeros)
        return res
