"""

2098. Subsequence of Size K With the Largest Even Sum
Medium
(premium / locked problem)

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
# IDEA : TAKE THE k LARGEST, THEN FIX THE PARITY WITH ONE SWAP
#
#   sort descending and take the top k. if that sum is already even, done.
#
#   otherwise the parity must flip, which costs exactly one swap : drop one
#   chosen element and pick up one unchosen element of the OPPOSITE parity.
#   to lose as little as possible, only two candidates matter :
#     - drop the SMALLEST chosen ODD  value, add the LARGEST unchosen EVEN
#     - drop the SMALLEST chosen EVEN value, add the LARGEST unchosen ODD
#
#   take the better of the two; if neither swap is available, return -1.
#
# time = O(n log n), space = O(n)
class Solution(object):
    def largestEvenSum(self, nums, k):
        nums = sorted(nums, reverse=True)
        chosen, rest = nums[:k], nums[k:]
        total = sum(chosen)
        if total % 2 == 0:
            return total

        # smallest chosen value of each parity (scan from the tail)
        min_odd = min_even = None
        for x in reversed(chosen):
            if x % 2 and min_odd is None:
                min_odd = x
            if x % 2 == 0 and min_even is None:
                min_even = x

        # largest unchosen value of each parity (rest is already descending)
        max_odd = max_even = None
        for x in rest:
            if x % 2 and max_odd is None:
                max_odd = x
            if x % 2 == 0 and max_even is None:
                max_even = x

        best = -1
        if min_odd is not None and max_even is not None:
            best = max(best, total - min_odd + max_even)
        if min_even is not None and max_odd is not None:
            best = max(best, total - min_even + max_odd)
        return best
