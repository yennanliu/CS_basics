"""

2321. Maximum Score Of Spliced Array
Hard

You are given two 0-indexed integer arrays nums1 and nums2, both of length n.

You can choose two integers left and right where 0 <= left <= right < n and swap the subarray nums1[left...right] with the subarray nums2[left...right].

For example, if nums1 = [1,2,3,4,5] and nums2 = [11,12,13,14,15] and you choose left = 1 and right = 2, nums1 becomes [1,12,13,4,5] and nums2 becomes [11,2,3,14,15].

You may choose to apply the mentioned operation once or not do anything.

The score of the arrays is the maximum of sum(nums1) and sum(nums2), where sum(arr) is the sum of all the elements in the array arr.

Return the maximum possible score.

A subarray is a contiguous sequence of elements within an array. arr[left...right] denotes the subarray that contains the elements of nums starting at index left and ending at index right (inclusive).


Example 1:

Input: nums1 = [60,60,60], nums2 = [10,90,10]
Output: 210
Explanation: Choosing left = 1 and right = 1, we have nums1 = [60,90,60] and nums2 = [10,60,10].
The score is max(sum(nums1), sum(nums2)) = max(210, 80) = 210.

Example 2:

Input: nums1 = [20,40,20,70,30], nums2 = [50,20,50,40,20]
Output: 220
Explanation: Choosing left = 3, right = 4, we have nums1 = [20,40,20,40,20] and nums2 = [50,20,50,70,30].
The score is max(sum(nums1), sum(nums2)) = max(140, 220) = 220.

Example 3:

Input: nums1 = [7,11,13], nums2 = [1,1,1]
Output: 31
Explanation: We choose not to swap any subarray.
The score is max(sum(nums1), sum(nums2)) = max(31, 3) = 31.


Constraints:

n == nums1.length == nums2.length
1 <= n <= 10^5
1 <= nums1[i], nums2[i] <= 10^5

"""

# V0
# IDEA : THE SWAP'S EFFECT IS A SUBARRAY SUM OF THE DIFFERENCE — KADANE
#
#   swapping [l, r] changes sum(nums1) by
#       sum over the window of (nums2[i] - nums1[i])
#   so the best achievable sum for nums1 is
#       sum(nums1) + max subarray sum of (nums2 - nums1)
#   and symmetrically for nums2 with the difference reversed.
#
#   the operation is OPTIONAL, so the Kadane run is clamped at 0 (an empty
#   window), which also covers example 3.
#
#   the answer is the larger of the two options.
#
# time = O(n), space = O(1)
class Solution(object):
    def maximumsSplicedArray(self, nums1, nums2):
        def best_gain(a, b):
            """max subarray sum of (b - a), at least 0"""
            best = cur = 0
            for x, y in zip(a, b):
                cur = max(0, cur + y - x)
                best = max(best, cur)
            return best

        return max(sum(nums1) + best_gain(nums1, nums2),
                   sum(nums2) + best_gain(nums2, nums1))
