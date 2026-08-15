"""

3254. Find the Power of K-Size Subarrays I
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

1 <= n == nums.length <= 500
1 <= nums[i] <= 10^5
1 <= k <= n

"""

# V0
# IDEA : "CONSECUTIVE AND ASCENDING" JUST MEANS EVERY STEP IS EXACTLY +1
#
#   a window qualifies iff nums[t+1] == nums[t] + 1 throughout it, and then
#   its maximum is its LAST element.
#
#   n is only 500 here, so checking each window directly is fine; the sequel
#   (LC 3255) does it with a running streak length instead.
#
# time = O(n * k), space = O(n)
class Solution(object):
    def resultsArray(self, nums, k):
        n = len(nums)
        res = []
        for i in range(n - k + 1):
            ok = all(nums[t + 1] == nums[t] + 1 for t in range(i, i + k - 1))
            res.append(nums[i + k - 1] if ok else -1)
        return res
