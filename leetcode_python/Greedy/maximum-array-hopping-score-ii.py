"""

3221. Maximum Array Hopping Score II
Medium
🔒 (premium)

Given an array nums, you have to get the maximum score starting from index 0 and hopping until you reach the last element of the array.

In each hop, you can jump from index i to an index j > i, and you get a score of (j - i) * nums[j].

Return the maximum score you can get.


Example 1:

Input: nums = [1,5,8]
Output: 16
Explanation:
There are two possible ways to reach the last element:
0 -> 1 -> 2 with a score of (1 - 0) * 5 + (2 - 1) * 8 = 13.
0 -> 2 with a score of (2 - 0) * 8 = 16.

Example 2:

Input: nums = [4,5,2,8,9,1,3]
Output: 42
Explanation:
We can do the hopping 0 -> 4 -> 6 with a score of (4 - 0) * 9 + (6 - 4) * 3 = 42.


Constraints:

2 <= nums.length <= 10^5
1 <= nums[i] <= 10^5

"""

# V0
# IDEA : ALWAYS HOP TO THE LARGEST VALUE STILL AHEAD — THE SUFFIX MAXIMA
#
#   splitting a hop is free : jumping i -> j -> k scores
#       (j-i)*nums[j] + (k-j)*nums[k]
#   which beats the direct i -> k only when nums[j] is at least as large as
#   nums[k]. so nothing is lost by landing on every SUFFIX MAXIMUM along the
#   way, and everything smaller is worth skipping.
#
#   so collect the indices whose value exceeds everything to their right
#   (scanning from the end), then walk them left to right adding
#   (gap * value). the last element is always the final suffix maximum, so
#   the walk ends where it must.
#
#   LC 3205 is the same problem at n = 1000 where an O(n^2) DP suffices; this
#   greedy is the linear version.
#
# time = O(n), space = O(n)
class Solution(object):
    def maxScore(self, nums):
        n = len(nums)
        maxima = []
        cur = 0
        for i in range(n - 1, 0, -1):          # index 0 is the start, never a target
            if nums[i] > cur:
                maxima.append(i)
                cur = nums[i]
        maxima.reverse()

        total = 0
        prev = 0
        for idx in maxima:
            total += (idx - prev) * nums[idx]
            prev = idx
        return total
