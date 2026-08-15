"""

2855. Minimum Right Shifts to Sort the Array
Easy

You are given a 0-indexed array nums of length n containing distinct positive integers. Return the minimum number of right shifts required to sort nums and -1 if this is not possible.

A right shift is defined as shifting the element at index i to index (i + 1) % n, for all indices.


Example 1:

Input: nums = [3,4,5,1,2]
Output: 2
Explanation:
After the first right shift, nums = [2,3,4,5,1].
After the second right shift, nums = [1,2,3,4,5].
Now nums is sorted; therefore the answer is 2.

Example 2:

Input: nums = [1,3,5]
Output: 0
Explanation: nums is already sorted therefore, the answer is 0.

Example 3:

Input: nums = [2,1,4]
Output: -1
Explanation: It's impossible to sort the array using right shifts.


Constraints:

1 <= nums.length <= 100
1 <= nums[i] <= 100
nums contains distinct integers.

"""

# V0
# IDEA : COUNT THE "DROPS" (rotated sorted array detection)
#
#   a right shift is a cyclic rotation, so nums can be sorted by right
#   shifts iff nums is a rotation of its sorted order — i.e. iff walking
#   the array CYCLICALLY there is at most one index i with
#   nums[i] > nums[(i + 1) % n] (a "drop").
#
#   - 0 drops -> already sorted, answer 0.
#   - 1 drop at index i -> the sorted array starts at index i + 1, and
#     bringing index i + 1 to position 0 takes n - (i + 1) right shifts.
#   - 2 or more drops -> impossible, answer -1.
#
#   NOTE : the wrap-around comparison nums[n-1] vs nums[0] MUST be included,
#          otherwise [3,4,5,1,2] and [2,1,4] look alike from the inside.
#          With the wrap included, the "already sorted" case naturally has
#          exactly one drop at i = n - 1, which yields n - n = 0 shifts.
#
# time = O(n), space = O(1)
class Solution(object):
    def minimumRightShifts(self, nums):
        n = len(nums)
        drops = []
        for i in range(n):
            if nums[i] > nums[(i + 1) % n]:
                drops.append(i)
        if not drops:
            return 0
        if len(drops) > 1:
            return -1
        return (n - 1 - drops[0]) % n
