"""

3255. Find the Power of K-Size Subarrays II
Medium

You are given an array of integers nums of length n and a positive integer k.

The power of an array is defined as:

Its maximum element if all of its elements are consecutive and sorted in ascending order.
-1 otherwise.

You need to find the power of all subarrays of nums of size k.

Return an integer array results of size n - k + 1, where results[i] is the power of nums[i..(i + k - 1)].


Example 1:

Input: nums = [1,2,3,4,3,2,5], k = 3
Output: [3,4,-1,-1,-1]
Explanation:
There are 5 subarrays of nums of size 3:
[1, 2, 3] with the maximum element 3.
[2, 3, 4] with the maximum element 4.
[3, 4, 3] whose elements are not consecutive.
[4, 3, 2] whose elements are not sorted.
[3, 2, 5] whose elements are not consecutive.

Example 2:

Input: nums = [2,2,2,2,2], k = 4
Output: [-1,-1]

Example 3:

Input: nums = [3,2,3,2,3,2], k = 2
Output: [-1,3,-1,3,-1]


Constraints:

1 <= n == nums.length <= 10^5
1 <= nums[i] <= 10^6
1 <= k <= n

"""

# V0
# IDEA : ONE RUNNING STREAK OF "+1 STEPS" ANSWERS EVERY WINDOW
#
#   track `run` = the length of the longest consecutive-ascending stretch
#   ending at the current index. it extends when nums[i] == nums[i-1] + 1 and
#   resets to 1 otherwise.
#
#   the window ending at i is fully inside that stretch exactly when
#   run >= k, in which case its power is nums[i]; otherwise -1.
#
#   this is LC 3254 at 10^5 elements, where re-scanning each window would be
#   O(n * k).
#
# time = O(n), space = O(n) for the output
class Solution(object):
    def resultsArray(self, nums, k):
        n = len(nums)
        res = []
        run = 1
        for i in range(n):
            if i and nums[i] == nums[i - 1] + 1:
                run += 1
            else:
                run = 1
            if i >= k - 1:
                res.append(nums[i] if run >= k else -1)
        return res
