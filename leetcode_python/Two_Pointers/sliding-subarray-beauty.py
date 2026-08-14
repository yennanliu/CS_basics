"""

2653. Sliding Subarray Beauty
Medium

Given an integer array nums containing n integers, find the beauty of each subarray of size k.

The beauty of a subarray is the xth smallest integer in the subarray if it is negative, or 0 if there are fewer than x negative integers.

Return an integer array containing n - k + 1 integers, which denote the beauty of the subarrays in order from the first index in the array.

A subarray is a contiguous non-empty sequence of elements within an array.


Example 1:

Input: nums = [1,-1,-3,-2,3], k = 3, x = 2
Output: [-1,-2,-2]
Explanation: There are 3 subarrays with size k = 3.
The first subarray is [1, -1, -3] and the 2nd smallest negative integer is -1.
The second subarray is [-1, -3, -2] and the 2nd smallest negative integer is -2.
The third subarray is [-3, -2, 3] and the 2nd smallest negative integer is -2.

Example 2:

Input: nums = [-1,-2,-3,-4,-5], k = 2, x = 2
Output: [-1,-2,-3,-4]
Explanation: There are 4 subarrays with size k = 2.
For [-1, -2], the 2nd smallest negative integer is -1.
For [-2, -3], the 2nd smallest negative integer is -2.
For [-3, -4], the 2nd smallest negative integer is -3.
For [-4, -5], the 2nd smallest negative integer is -4.

Example 3:

Input: nums = [-3,1,2,-3,0,-3], k = 2, x = 1
Output: [-3,0,-3,-3,-3]
Explanation: There are 5 subarrays with size k = 2.
For [-3, 1], the 1st smallest negative integer is -3.
For [1, 2], there is no negative integer so the beauty is 0.
For [2, -3], the 1st smallest negative integer is -3.
For [-3, 0], the 1st smallest negative integer is -3.
For [0, -3], the 1st smallest negative integer is -3.


Constraints:

n == nums.length
1 <= n <= 10^5
1 <= k <= n
1 <= x <= k
-50 <= nums[i] <= 50

"""

# V0
# IDEA : SLIDING WINDOW + COUNTING SORT (values are bounded by [-50, 50])
#
#   we only ever need the x-th SMALLEST NEGATIVE value, and negatives live in
#   the tiny range [-50, -1]. so instead of a heap / ordered multiset we keep
#   a frequency table cnt[v + 50] and, for every window, walk v from -50
#   upwards accumulating counts until we have seen x of them.
#
#   the window itself slides in O(1) : add nums[right], drop nums[left].
#
#   NOTE : only NEGATIVE values need to be tracked — zeros / positives can
#          never be the answer, so they are skipped entirely on add/remove.
#
#   NOTE : if the running count never reaches x, fewer than x negatives are
#          in the window and the beauty is 0 (not "the smallest negative").
#
# time = O(n * 50), space = O(1)  (the table has a fixed size of 50)
class Solution(object):
    def getSubarrayBeauty(self, nums, k, x):
        cnt = [0] * 50                      # cnt[v + 50] for v in [-50, -1]
        ans = []
        for i in range(len(nums)):
            if nums[i] < 0:
                cnt[nums[i] + 50] += 1
            if i >= k:
                out = nums[i - k]
                if out < 0:
                    cnt[out + 50] -= 1
            if i >= k - 1:
                seen = 0
                beauty = 0
                for v in range(50):
                    seen += cnt[v]
                    if seen >= x:
                        beauty = v - 50
                        break
                ans.append(beauty)
        return ans
