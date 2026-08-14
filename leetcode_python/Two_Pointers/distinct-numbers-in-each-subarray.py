"""

1852. Distinct Numbers in Each Subarray
Medium

You are given an integer array nums of length n and an integer k. Your task is to find the number of distinct elements in every subarray of size k within nums.

Return an array ans such that ans[i] is the count of distinct elements in nums[i..(i + k - 1)] for each index 0 <= i < n - k.


Example 1:

Input: nums = [1,2,3,2,2,1,3], k = 3
Output: [3,2,2,2,3]
Explanation: The number of distinct elements in each subarray goes as follows:
- nums[0..2] = [1,2,3] so ans[0] = 3
- nums[1..3] = [2,3,2] so ans[1] = 2
- nums[2..4] = [3,2,2] so ans[2] = 2
- nums[3..5] = [2,2,1] so ans[3] = 2
- nums[4..6] = [2,1,3] so ans[4] = 3

Example 2:

Input: nums = [1,1,1,1,2,3,4], k = 4
Output: [1,2,3,4]
Explanation: The number of distinct elements in each subarray goes as follows:
- nums[0..3] = [1,1,1,1] so ans[0] = 1
- nums[1..4] = [1,1,1,2] so ans[1] = 2
- nums[2..5] = [1,1,2,3] so ans[2] = 3
- nums[3..6] = [1,2,3,4] so ans[3] = 4


Constraints:

1 <= k <= nums.length <= 10^5
1 <= nums[i] <= 10^5

"""

# V0
# IDEA : FIXED-SIZE SLIDING WINDOW + COUNTER (distinct == number of live keys)
#
#   build the counter for the first window, then slide one step at a time:
#   add nums[i], drop nums[i - k]. the distinct count is just len(cnt), which
#   stays correct as long as we DELETE a key the moment its count hits zero.
#
#   NOTE : that deletion is the whole trick -- leaving zero-count keys around
#          would silently inflate len(cnt).
#
# time = O(n), space = O(k)
from collections import Counter
class Solution(object):
    def distinctNumbers(self, nums, k):
        cnt = Counter(nums[:k])
        res = [len(cnt)]
        for i in range(k, len(nums)):
            cnt[nums[i]] += 1
            out = nums[i - k]
            cnt[out] -= 1
            if cnt[out] == 0:
                del cnt[out]
            res.append(len(cnt))
        return res
