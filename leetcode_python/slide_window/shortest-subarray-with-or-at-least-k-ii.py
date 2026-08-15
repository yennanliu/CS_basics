"""

3097. Shortest Subarray With OR at Least K II
Medium

You are given an array nums of non-negative integers and an integer k.

An array is called special if the bitwise OR of all of its elements is at least k.

Return the length of the shortest special non-empty subarray of nums, or return -1 if no special subarray exists.


Example 1:

Input: nums = [1,2,3], k = 2
Output: 1
Explanation: The subarray [3] has OR value of 3. Hence, we return 1.

Example 2:

Input: nums = [2,1,8], k = 10
Output: 3
Explanation: The subarray [2,1,8] has OR value of 11. Hence, we return 3.

Example 3:

Input: nums = [1,2], k = 0
Output: 1
Explanation: The subarray [1] has OR value of 1. Hence, we return 1.


Constraints:

1 <= nums.length <= 2 * 10^5
0 <= nums[i] <= 10^9
0 <= k <= 10^9

"""

# V0
# IDEA : SLIDING WINDOW WITH A COUNTER PER BIT (OR IS NOT INVERTIBLE)
#
#   the OR of a window only grows as it widens, so the window is monotone and
#   two pointers apply : extend right until the OR reaches k, then shrink
#   from the left while it still does.
#
#   the catch is that OR cannot be "undone" when an element leaves — losing
#   a 1 bit only matters if no OTHER element in the window still has it. so
#   keep a count of how many window elements set each of the 30 bits; a bit
#   is present in the OR exactly while its count is non-zero.
#
#   rebuilding the OR from those counts is 30 cheap steps per move.
#
# time = O(30 * n), space = O(1)
class Solution(object):
    def minimumSubarrayLength(self, nums, k):
        BITS = 32
        cnt = [0] * BITS
        cur = 0

        def add(x, delta):
            for b in range(BITS):
                if (x >> b) & 1:
                    cnt[b] += delta

        def value():
            v = 0
            for b in range(BITS):
                if cnt[b]:
                    v |= 1 << b
            return v

        best = float('inf')
        left = 0
        for right, x in enumerate(nums):
            add(x, 1)
            cur = value()
            while left <= right and cur >= k:
                best = min(best, right - left + 1)
                add(nums[left], -1)
                left += 1
                cur = value()
        return -1 if best == float('inf') else best
