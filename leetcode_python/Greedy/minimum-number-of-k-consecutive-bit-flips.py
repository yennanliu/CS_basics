"""

995. Minimum Number of K Consecutive Bit Flips
Hard

You are given a binary array nums and an integer k.

A k-bit flip is choosing a subarray of length k from nums and simultaneously changing every 0 in the subarray to 1, and every 1 in the subarray to 0.

Return the minimum number of k-bit flips required so that there is no 0 in the array. If it is not possible, return -1.

A subarray is a contiguous part of an array.

Example 1:

Input: nums = [0,1,0], k = 1
Output: 2
Explanation: Flip nums[0], then flip nums[2].

Example 2:

Input: nums = [1,1,0], k = 2
Output: -1
Explanation: No matter how we flip subarrays of size 2, we cannot make the array become [1,1,1].

Example 3:

Input: nums = [0,0,0,1,0,1,1,0], k = 3
Output: 3
Explanation:
Flip nums[0],nums[1],nums[2]: nums becomes [1,1,1,1,0,1,1,0]
Flip nums[4],nums[5],nums[6]: nums becomes [1,1,1,1,1,0,0,0]
Flip nums[5],nums[6],nums[7]: nums becomes [1,1,1,1,1,1,1,1]

Constraints:

1 <= nums.length <= 10^5
1 <= k <= nums.length

"""

# V0
# IDEA : GREEDY + DIFFERENCE ARRAY (sliding flip count)
#
#  - Scan left to right. Index 0 can only ever be fixed by a flip starting
#    at 0, so the leftmost still-zero position FORCES a flip starting there.
#    That makes the greedy choice unique, hence optimal.
#  - Flipping is expensive to apply literally (O(n*k)). Instead track how many
#    flips currently COVER position i: a flip started at i stops covering at
#    i + k, so record that expiry in a difference array.
#  - Position i is effectively 1 iff (nums[i] + covering_flips) is odd.
#  - If a flip must start at i but i + k > n, the tail can never be fixed -> -1.
#
# time = O(n)
# space = O(n)
class Solution(object):
    def minKBitFlips(self, nums, k):
        n = len(nums)
        expire = [0] * (n + 1)   # expire[i] = -(flips that stop covering at i)
        covering = 0             # how many flips currently cover position i
        res = 0

        for i in range(n):
            covering += expire[i]
            if (nums[i] + covering) % 2 == 0:
                # still a 0 here -> we are forced to start a flip at i
                if i + k > n:
                    return -1
                res += 1
                covering += 1
                expire[i + k] -= 1

        return res
