"""

3397. Maximum Number of Distinct Elements After Operations
Medium

You are given an integer array nums and an integer k.

You are allowed to perform the following operation on each element of the array
at most once:

Add an integer in the range [-k, k] to the element.

Return the maximum possible number of distinct elements in nums after performing
the operations.

Example 1:

Input: nums = [1,2,2,3,3,4], k = 2

Output: 6

Explanation:

nums changes to [-1, 0, 1, 2, 3, 4] after performing operations on the first
four elements.

Example 2:

Input: nums = [4,4,4,4], k = 1

Output: 3

Explanation:

By adding -1 to nums[0] and 1 to nums[1], nums changes to [3, 5, 4, 4].

Constraints:

1 <= nums.length <= 10^5
1 <= nums[i] <= 10^9
0 <= k <= 10^9

"""

# V0
# IDEA : SORT, THEN GREEDILY PUSH EACH VALUE AS FAR LEFT AS ALLOWED
#
#   each element can land anywhere in the window [x - k, x + k], and we want as
#   many pairwise-different landing spots as possible.  process the values in
#   increasing order and always assign the *smallest* still-unused integer that
#   the current window permits, i.e. max(prev + 1, x - k).
#
#   why greedy is safe: the windows are sorted by their left endpoint, so they
#   form a "staircase" — an element considered later never has a window that
#   starts earlier.  spending a bigger slot now can only take a slot that a
#   later element might also have wanted, while leaving the small slot unused
#   forever.  so keeping our own assignment minimal never hurts anybody later.
#
#   if even the smallest legal slot exceeds x + k, this element cannot be made
#   distinct from everything before it and is simply dropped (parked on top of
#   some earlier value), which costs nothing.
#
# time = O(n log n), space = O(1) beyond the sort
class Solution(object):
    def maxDistinctElements(self, nums, k):
        nums.sort()
        prev = float('-inf')
        ans = 0
        for x in nums:
            v = max(prev + 1, x - k)
            if v <= x + k:
                ans += 1
                prev = v
        return ans
