"""

3641. Longest Semi-Repeating Subarray
Medium

You are given an integer array nums of length n and an integer k.

A semi-repeating subarray is a contiguous subarray in which at most k
elements repeat (i.e., appear more than once).

Return the length of the longest semi-repeating subarray in nums.


Example 1:

Input: nums = [1,2,3,1,2,3,4], k = 2
Output: 6
Explanation:
The longest semi-repeating subarray is [2, 3, 1, 2, 3, 4], which has two
repeating elements (2 and 3).

Example 2:

Input: nums = [1,1,1,1,1], k = 4
Output: 5
Explanation:
The longest semi-repeating subarray is [1, 1, 1, 1, 1], which has only one
repeating element (1).

Example 3:

Input: nums = [1,1,1,1,1], k = 0
Output: 1
Explanation:
The longest semi-repeating subarray is [1], which has no repeating
elements.


Constraints:

1 <= nums.length <= 10^5
1 <= nums[i] <= 10^5
0 <= k <= nums.length

"""

# V0
# IDEA : SLIDING WINDOW ON THE NUMBER OF *DISTINCT VALUES* THAT REPEAT
#
#   read the condition carefully: it counts DISTINCT values that occur two
#   or more times, not the number of surplus occurrences. so [1,1,1] has one
#   repeating element, not two -- which is why example 2 answers 5.
#
#   that quantity is monotone in the window: extending the right end can
#   only push a count from 1 to 2 (never back), so `rep` never decreases;
#   shrinking from the left can only push a count from 2 to 1, so `rep`
#   never increases. monotonicity in both directions is exactly what makes
#   the classic two-pointer window valid -- for each right end there is a
#   single leftmost feasible left end, and that boundary only moves right.
#
#   so we maintain a count table, bump `rep` when a count reaches 2 on the
#   way in, drop it when a count falls back to 1 on the way out, and shrink
#   while rep > k.
#
# time = O(n), space = O(n)
class Solution(object):
    def longestSubarray(self, nums, k):
        cnt = {}
        rep = 0
        left = 0
        ans = 0
        for right, x in enumerate(nums):
            c = cnt.get(x, 0) + 1
            cnt[x] = c
            if c == 2:
                rep += 1
            while rep > k:
                y = nums[left]
                if cnt[y] == 2:
                    rep -= 1
                cnt[y] -= 1
                left += 1
            if right - left + 1 > ans:
                ans = right - left + 1
        return ans
