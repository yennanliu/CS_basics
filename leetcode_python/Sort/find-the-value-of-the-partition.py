"""

2740. Find the Value of the Partition
Medium

You are given a positive integer array nums.

Partition nums into two arrays, nums1 and nums2, such that:

Each element of the array nums belongs to either the array nums1 or the array nums2.
Both arrays are non-empty.
The value of the partition is minimized.

The value of the partition is |max(nums1) - min(nums2)|.

Here, max(nums1) denotes the maximum element of the array nums1, and min(nums2) denotes the minimum element of the array nums2.

Return the integer denoting the value of such partition.


Example 1:

Input: nums = [1,3,2,4]
Output: 1
Explanation: We can partition the array nums into nums1 = [1,2] and nums2 = [3,4].
- The maximum element of the array nums1 is equal to 2.
- The minimum element of the array nums2 is equal to 3.
The value of the partition is |2 - 3| = 1.
It can be proven that 1 is the minimum value out of all partitions.

Example 2:

Input: nums = [100,1,10]
Output: 9
Explanation: We can partition the array nums into nums1 = [10] and nums2 = [100,1].
- The maximum element of the array nums1 is equal to 10.
- The minimum element of the array nums2 is equal to 1.
The value of the partition is |10 - 1| = 9.
It can be proven that 9 is the minimum value out of all partitions.


Constraints:

2 <= nums.length <= 10^5
1 <= nums[i] <= 10^9

"""

# V0
# IDEA : SORT + MIN ADJACENT DIFFERENCE
#
#   Let a = max(nums1) and b = min(nums2). The value is |a - b|, and a, b are
#   two DISTINCT positions of the array. So the answer is at least the
#   smallest |nums[i] - nums[j]| over any pair i != j, i.e. after sorting,
#   the smallest gap between two ADJACENT elements.
#
#   That bound is always reachable : sort the array, pick the adjacent pair
#   (nums[i], nums[i+1]) with the smallest gap, put nums[0..i] into nums1 and
#   nums[i+1..] into nums2. Then max(nums1) = nums[i], min(nums2) = nums[i+1],
#   and both halves are non-empty.
#
#   NOTE : no abs() is needed after sorting — nums[i+1] >= nums[i].
#
#   NOTE : duplicates give a gap of 0, which the same argument handles.
#
# time = O(n * log n), space = O(n) for the sort
class Solution(object):
    def findValueOfPartition(self, nums):
        nums = sorted(nums)
        res = nums[1] - nums[0]
        for i in range(1, len(nums) - 1):
            gap = nums[i + 1] - nums[i]
            if gap < res:
                res = gap
        return res
