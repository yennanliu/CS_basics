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
There is no way to group all 1's together with 0 or 1 swaps.
Thus, the minimum number of swaps required is 2.

Example 3:

Input: nums = [1,1,0,0,1]
Output: 0
Explanation: All the 1's are already grouped together due to the circular property of the array.
Thus, we do not need any swaps.


Constraints:

1 <= nums.length <= 10^5
nums[i] is either 0 or 1.

"""

# V0
# IDEA : FIXED WINDOW OF SIZE (TOTAL ONES), MINIMISE THE ZEROS INSIDE IT
#
#   after the swaps all the 1s occupy one contiguous circular block, and
#   that block has exactly k = (number of 1s) cells. every 0 sitting inside
#   the chosen block costs one swap (trade it with a 1 from outside), so
#
#       answer = min over all windows of length k of (zeros in the window)
#
#   the circular wrap is handled by letting the window index run over
#   0 .. n-1 with modular reads, keeping the sliding update O(1).
#
#   NOTE : k == 0 (no 1s at all) needs no swap.
#
# time = O(n), space = O(1)
class Solution(object):
    def minSwaps(self, nums):
        n = len(nums)
        k = sum(nums)
        if k == 0 or k == n:
            return 0

        ones = sum(nums[:k])
        best = ones
        for i in range(1, n):
            ones -= nums[i - 1]
            ones += nums[(i + k - 1) % n]
            best = max(best, ones)
        return k - best
