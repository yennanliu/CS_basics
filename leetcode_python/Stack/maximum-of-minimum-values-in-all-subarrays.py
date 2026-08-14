"""

1950. Maximum of Minimum Values in All Subarrays
Medium

You are given an integer array nums of size n. You are asked to solve n queries for each integer i in the range 0 <= i < n.

To solve the ith query:

1. Find the minimum value in each possible subarray of size i + 1 of the array nums.
2. Find the maximum of those minimum values. This maximum is the answer to the query.

Return a 0-indexed integer array ans of size n such that ans[i] is the answer to the ith query.

A subarray is a contiguous sequence of elements in an array.


Example 1:

Input: nums = [0,1,2,4]
Output: [4,2,1,0]
Explanation:
i=0:
- The subarrays of size 1 are [0], [1], [2], [4]. The minimum values are 0, 1, 2, 4.
- The maximum of the minimum values is 4.
i=1:
- The subarrays of size 2 are [0,1], [1,2], [2,4]. The minimum values are 0, 1, 2.
- The maximum of the minimum values is 2.
i=2:
- The subarrays of size 3 are [0,1,2], [1,2,4]. The minimum values are 0, 1.
- The maximum of the minimum values is 1.
i=3:
- There is one subarray of size 4, which is [0,1,2,4]. The minimum value is 0.
- There is only one value, so the maximum is 0.

Example 2:

Input: nums = [10,20,50,10]
Output: [50,20,10,10]
Explanation:
i=0:
- The subarrays of size 1 are [10], [20], [50], [10]. The minimum values are 10, 20, 50, 10.
- The maximum of the minimum values is 50.
i=1:
- The subarrays of size 2 are [10,20], [20,50], [50,10]. The minimum values are 10, 20, 10.
- The maximum of the minimum values is 20.
i=2:
- The subarrays of size 3 are [10,20,50], [20,50,10]. The minimum values are 10, 10.
- The maximum of the minimum values is 10.
i=3:
- There is one subarray of size 4, which is [10,20,50,10]. The minimum value is 10.
- There is only one value, so the maximum is 10.


Constraints:

n == nums.length
1 <= n <= 10^5
0 <= nums[i] <= 10^9

"""

# V0
# IDEA : MONOTONIC STACK (widest window where nums[i] is the minimum) + SUFFIX MAX
#
#   for every index i, find the previous / next strictly smaller element with a
#   monotonic stack. between them, nums[i] IS the minimum, and that window has
#   length  w = right[i] - left[i] - 1.
#
#   so nums[i] is achievable as "min of a window of size w" :
#       best[w - 1] = max(best[w - 1], nums[i])
#
#   any window of size w also CONTAINS a window of every smaller size whose min
#   is >= nums[i], so the answer array is non-increasing in the window size ->
#   sweep right to left taking a running max to fill the sizes nobody claimed.
#
#   NOTE : popping with >= on one side and >= on the other still yields the
#          correct widest window for the extremal occurrence of duplicates.
#
# time = O(n), space = O(n)
class Solution(object):
    def findMaximums(self, nums):
        n = len(nums)
        left = [-1] * n
        right = [n] * n

        stack = []
        for i in range(n):
            while stack and nums[stack[-1]] >= nums[i]:
                stack.pop()
            if stack:
                left[i] = stack[-1]
            stack.append(i)

        stack = []
        for i in range(n - 1, -1, -1):
            while stack and nums[stack[-1]] >= nums[i]:
                stack.pop()
            if stack:
                right[i] = stack[-1]
            stack.append(i)

        res = [0] * n
        for i in range(n):
            w = right[i] - left[i] - 1
            if nums[i] > res[w - 1]:
                res[w - 1] = nums[i]
        for i in range(n - 2, -1, -1):
            if res[i + 1] > res[i]:
                res[i] = res[i + 1]
        return res
