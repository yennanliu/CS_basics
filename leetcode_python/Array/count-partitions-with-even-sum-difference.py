"""

3432. Count Partitions with Even Sum Difference
Easy

You are given an integer array nums of length n.

A partition is defined as an index i where 0 <= i < n - 1, splitting the array
into two non-empty subarrays such that:

Left subarray contains indices [0, i].
Right subarray contains indices [i + 1, n - 1].

Return the number of partitions where the difference between the sum of the left
and right subarrays is even.

Example 1:

Input: nums = [10,10,3,7,6]

Output: 4

Explanation:

The 4 partitions are:

[10], [10, 3, 7, 6] with a sum difference of 10 - 26 = -16, which is even.
[10, 10], [3, 7, 6] with a sum difference of 20 - 16 = 4, which is even.
[10, 10, 3], [7, 6] with a sum difference of 23 - 13 = 10, which is even.
[10, 10, 3, 7], [6] with a sum difference of 30 - 6 = 24, which is even.

Example 2:

Input: nums = [1,2,2]

Output: 0

Explanation:

No partition results in an even sum difference.

Example 3:

Input: nums = [2,4,6,8]

Output: 3

Explanation:

All partitions result in an even sum difference.

Constraints:

2 <= n == nums.length <= 100
1 <= nums[i] <= 100

"""

# V0
# IDEA : THE PARITY OF THE DIFFERENCE DOES NOT DEPEND ON WHERE YOU CUT
#
#   with L + R fixed at the total sum, L - R = 2L - total.  the 2L term is
#   always even, so L - R has the parity of the total sum alone — the cut point
#   never enters into it.
#
#   so it is all or nothing: an even total makes every one of the n - 1 cuts
#   work, an odd total makes none of them work.
#
# time = O(n), space = O(1)
class Solution(object):
    def countPartitions(self, nums):
        return len(nums) - 1 if sum(nums) % 2 == 0 else 0


# V0-1
# IDEA : RUNNING PREFIX SUM — EVALUATE EVERY CUT EXPLICITLY
#
#   keep the left sum as a running total and read the right sum off as
#   total - left, then test the parity of the difference at each of the n - 1
#   cuts. no parity insight required, and this is the shape that survives if
#   the scoring rule is changed to something the O(1) trick cannot absorb.
#
# time = O(n), space = O(1)
class Solution(object):
    def countPartitions(self, nums):
        total = sum(nums)
        left = 0
        res = 0
        for i in range(len(nums) - 1):
            left += nums[i]
            if (left - (total - left)) % 2 == 0:
                res += 1
        return res


# V0-2
# IDEA : BRUTE FORCE — RE-SUM BOTH SIDES OF EVERY CUT FROM SCRATCH
#
#   for each split index build the two subarrays by slicing and sum them
#   independently. the most literal transcription of the statement, and with
#   n <= 100 the quadratic cost never matters.
#
# time = O(n^2), space = O(n)   (slice copies)
class Solution(object):
    def countPartitions(self, nums):
        res = 0
        for i in range(len(nums) - 1):
            if (sum(nums[:i + 1]) - sum(nums[i + 1:])) % 2 == 0:
                res += 1
        return res
