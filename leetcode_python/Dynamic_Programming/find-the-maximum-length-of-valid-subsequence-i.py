"""

3201. Find the Maximum Length of Valid Subsequence I
Medium

You are given an integer array nums.

A subsequence sub of nums with length x is called valid if it satisfies:

(sub[0] + sub[1]) % 2 == (sub[1] + sub[2]) % 2 == ... == (sub[x - 2] + sub[x - 1]) % 2.

Return the length of the longest valid subsequence of nums.

Note: A subsequence is an array that can be derived from another array by deleting some or no elements without changing the order of the remaining elements.


Example 1:

Input: nums = [1,2,3,4]
Output: 4
Explanation:
The longest valid subsequence is [1, 2, 3, 4].

Example 2:

Input: nums = [1,2,1,1,2,1,2]
Output: 6
Explanation:
The longest valid subsequence is [1, 2, 1, 2, 1, 2].

Example 3:

Input: nums = [1,3]
Output: 2
Explanation:
The longest valid subsequence is [1, 3].


Constraints:

2 <= nums.length <= 2 * 10^5
1 <= nums[i] <= 10^7

"""

# V0
# IDEA : THE COMMON PARITY IS EITHER 0 OR 1 — SO THERE ARE ONLY TWO SHAPES
#
#   consecutive sums all even means every neighbouring pair shares a parity,
#   i.e. the WHOLE subsequence has one parity. the best such subsequence is
#   simply all the evens or all the odds.
#
#   consecutive sums all odd means the parity FLIPS at every step, so the
#   best one is the longest strictly alternating pick — greedily take each
#   element whose parity differs from the last one taken.
#
#   the answer is the largest of the three.
#
# time = O(n), space = O(1)
class Solution(object):
    def maximumLength(self, nums):
        evens = sum(1 for x in nums if x % 2 == 0)
        odds = len(nums) - evens

        alt = 0
        want = -1                       # parity expected next; -1 = anything
        for x in nums:
            p = x % 2
            if want == -1 or p == want:
                alt += 1
                want = 1 - p
        return max(evens, odds, alt)
