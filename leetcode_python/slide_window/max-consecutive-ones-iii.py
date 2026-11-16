"""

1004. Max Consecutive Ones III
Medium

Given a binary array nums and an integer k, return the maximum number of
consecutive 1's in the array if you can flip at most k 0's.

Example 1:

Input: nums = [1,1,1,0,0,0,1,1,1,1,0], k = 2

Output: 6

Explanation: [1,1,1,0,0,1,1,1,1,1,1]
Bolded numbers were flipped from 0 to 1. The longest subarray is underlined.

Example 2:

Input: nums = [0,0,1,1,0,0,1,1,1,0,1,1,0,0,0,1,1,1,1], k = 3

Output: 10

Explanation: [0,0,1,1,1,1,1,1,1,1,1,1,0,0,0,1,1,1,1]
Bolded numbers were flipped from 0 to 1. The longest subarray is underlined.

Constraints:

1 <= nums.length <= 10^5
nums[i] is either 0 or 1.
0 <= k <= nums.length

"""

# V0
# IDEA : SLIDING WINDOW (GROW THEN SHRINK) — LONGEST WINDOW WITH AT MOST K ZEROS
#
#   Flipping at most k zeros = finding the longest subarray holding at most k
#   zeros (flip all of them). "zeros <= k" stays true when the window shrinks,
#   so a window works: grow right, and while the window holds more than k
#   zeros, move left forward. After the while, [left, right] is the longest
#   valid window ending at right, so that is where it is measured.
#
#   e.g. nums = [1,0,0,1], k = 1
#        right=2 -> zeros=2 > 1 -> drop nums[0]=1, drop nums[1]=0 -> [0] (len 1)
#        right=3 -> [0,1] (len 2); best was [1,0] (len 2) -> answer 2
#
# time = O(n), space = O(1)
class Solution(object):
    def longestOnes(self, nums, k):
        """
        :type nums: List[int]
        :type k: int
        :rtype: int
        """
        left = 0
        zeros = 0      # zeros inside the window [left, right]
        best = 0

        for right in range(len(nums)):
            # 1. grow: take nums[right] into the window
            if nums[right] == 0:
                zeros += 1

            # 2. shrink until the window needs at most k flips again
            # NOTE !!! `while`, not `if`, and the result is updated AFTER it,
            #          so only a valid window is ever measured
            while zeros > k:
                if nums[left] == 0:
                    zeros -= 1
                left += 1

            # 3. the window is valid here
            best = max(best, right - left + 1)

        return best


# V0-1
# IDEA : SLIDING WINDOW THAT NEVER SHRINKS
#
#   We only care about the LONGEST window, so the window never needs to get
#   smaller than the best one found so far. On an invalid step, move left by
#   exactly one (an `if`, not a `while`): the window keeps its size and slides
#   right. It only grows when a longer valid window exists, so its final size
#   is the answer: len(nums) - left.
#
#   The window may be invalid while it slides; that is fine, because its size
#   only ever records a length that WAS valid at some point.
#
#   NOTE : same O(n), shorter, but harder to explain live — learn V0 first.
#
# time = O(n), space = O(1)
class Solution2(object):
    def longestOnes(self, nums, k):
        """
        :type nums: List[int]
        :type k: int
        :rtype: int
        """
        left = 0
        flips_left = k     # flips still available for the window [left, right]

        for right in range(len(nums)):
            if nums[right] == 0:
                flips_left -= 1

            # window over budget: slide it by one instead of shrinking it
            if flips_left < 0:
                if nums[left] == 0:
                    flips_left += 1
                left += 1

        # the window's size never dropped, and only grew on a valid step
        return len(nums) - left


# V1
# IDEA : PREFIX COUNT OF ZEROS + BINARY SEARCH
#
#   zeros_before[i] = number of zeros in nums[0 .. i-1]; it never decreases.
#   For each right end, the window [left, right] is valid when
#       zeros_before[right + 1] - zeros_before[left] <= k
#   so the best left is the FIRST index whose zeros_before is at least
#   zeros_before[right + 1] - k — one binary search per right end.
#
#   NOTE : slower than V0 (O(n log n)); worth knowing because the same
#          prefix + binary search shape works when a window can't be slid.
#
# time = O(n log n), space = O(n)
from bisect import bisect_left


class Solution3(object):
    def longestOnes(self, nums, k):
        """
        :type nums: List[int]
        :type k: int
        :rtype: int
        """
        n = len(nums)

        # zeros_before[i] = zeros in nums[0 .. i-1], non-decreasing
        zeros_before = [0] * (n + 1)
        for i in range(n):
            is_zero = 1 if nums[i] == 0 else 0
            zeros_before[i + 1] = zeros_before[i] + is_zero

        best = 0
        for right in range(n):
            # smallest left with zeros_before[left] >= zeros_before[right + 1] - k
            target = zeros_before[right + 1] - k
            left = bisect_left(zeros_before, target, 0, right + 1)
            best = max(best, right - left + 1)

        return best
