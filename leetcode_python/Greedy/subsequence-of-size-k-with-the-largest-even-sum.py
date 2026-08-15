"""

2098. Subsequence of Size K With the Largest Even Sum
Medium
🔒 (premium)

You are given an integer array nums and an integer k. Find the largest even sum of any subsequence of nums that has a length of k.

Return this sum, or -1 if such a sum does not exist.

A subsequence is an array that can be derived from another array by deleting some or no elements without changing the order of the remaining elements.


Example 1:

Input: nums = [4,1,5,3,1], k = 3
Output: 12
Explanation:
The subsequence with the largest possible even sum is [4,5,3]. It has a sum of 4 + 5 + 3 = 12.

Example 2:

Input: nums = [4,6,2], k = 3
Output: 12
Explanation:
The subsequence with the largest possible even sum is [4,6,2]. It has a sum of 4 + 6 + 2 = 12.

Example 3:

Input: nums = [1,3,5], k = 1
Output: -1
Explanation:
No subsequence of nums with length 1 has an even sum.


Constraints:

1 <= nums.length <= 10^5
0 <= nums[i] <= 10^9
1 <= k <= nums.length

"""

# V0
# IDEA : TAKE THE K LARGEST, THEN FIX THE PARITY WITH ONE SWAP
#
#   the k largest values give the biggest sum of any length-k subsequence.
#   if that sum is already even we are done.
#
#   if it is odd, the parity must flip, and flipping costs exactly one swap :
#   drop one taken value and pick up one untaken value OF THE OPPOSITE
#   PARITY. two candidate swaps, and each should lose as little as possible :
#
#       (a) drop the smallest ODD  taken, add the largest EVEN untaken
#       (b) drop the smallest EVEN taken, add the largest ODD  untaken
#
#   take whichever swap exists and loses less; if neither exists, -1.
#
#   NOTE : order inside a subsequence never matters here, so sorting is free.
#
# time = O(n log n), space = O(n)
class Solution(object):
    def largestEvenSum(self, nums, k):
        nums = sorted(nums)
        taken = nums[len(nums) - k:]
        rest = nums[:len(nums) - k]
        total = sum(taken)
        if total % 2 == 0:
            return total

        # smallest taken of each parity (taken is ascending)
        min_taken = {0: None, 1: None}
        for x in taken:
            if min_taken[x % 2] is None:
                min_taken[x % 2] = x
        # largest untaken of each parity (rest is ascending -> scan backwards)
        max_rest = {0: None, 1: None}
        for x in reversed(rest):
            if max_rest[x % 2] is None:
                max_rest[x % 2] = x

        best = -1
        for p in (0, 1):
            if min_taken[p] is not None and max_rest[1 - p] is not None:
                best = max(best, total - min_taken[p] + max_rest[1 - p])
        return best
