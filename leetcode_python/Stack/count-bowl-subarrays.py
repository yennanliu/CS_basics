"""

3676. Count Bowl Subarrays
Medium

You are given an integer array nums with distinct elements.

A subarray nums[l...r] of nums is called a bowl if:

The subarray has length at least 3, that is, r - l + 1 >= 3.
The minimum of its two ends is strictly greater than the maximum of all elements strictly in between, that is
  min(nums[l], nums[r]) > max(nums[l + 1], ..., nums[r - 1]).

Return the number of bowl subarrays in nums.


Example 1:

Input: nums = [2,5,3,1,4]
Output: 2
Explanation:
The bowl subarrays are [5,3,1,4] with min(5,4) = 4 > max(3,1) = 3, and [3,1,4] with min(3,4) = 3 > max(1) = 1.

Example 2:

Input: nums = [5,1,2,3,4]
Output: 3
Explanation:
The bowl subarrays are [5,1,2], [5,1,2,3] and [5,1,2,3,4].


Constraints:

3 <= nums.length <= 10^5
1 <= nums[i] <= 10^9
nums consists of distinct elements.

"""

# V0
# IDEA : MONOTONIC STACK — COUNT "MUTUALLY VISIBLE" INDEX PAIRS
#
#   the bowl condition says that l and r can "see each other over" the valley
#   between them, which is exactly the classic visible-pair relation.
#
#   keeping a strictly decreasing stack of indices, index j sees:
#     * every index it pops (their values are smaller than nums[j], and
#       everything between was popped even earlier, so it is smaller still),
#     * plus the one index left on top afterwards, whose value exceeds nums[j].
#   nothing else is visible, so every pair is emitted exactly once.
#
#   finally keep only the pairs at distance >= 2, since a bowl needs at least
#   one element strictly between the two ends.
#
# time = O(n), space = O(n)
class Solution(object):
    def bowlSubarrays(self, nums):
        res = 0
        st = []
        for j in range(len(nums)):
            x = nums[j]
            while st and nums[st[-1]] < x:
                i = st.pop()
                if j - i > 1:
                    res += 1
            if st and j - st[-1] > 1:
                res += 1
            st.append(j)
        return res
