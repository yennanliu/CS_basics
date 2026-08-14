"""

2420. Find All Good Indices
Medium

You are given a 0-indexed integer array nums of size n and a positive integer k.

We call an index i in the range k <= i < n - k good if the following conditions are satisfied:

The k elements that are just before the index i are in non-increasing order.
The k elements that are just after the index i are in non-decreasing order.

Return an array of all good indices sorted in increasing order.


Example 1:

Input: nums = [2,1,1,1,3,4,1], k = 2
Output: [2,3]
Explanation: There are two good indices in the array:
- Index 2. The subarray [2,1] is in non-increasing order, and the subarray [1,3] is in non-decreasing order.
- Index 3. The subarray [1,1] is in non-increasing order, and the subarray [3,4] is in non-decreasing order.
Note that the index 4 is not good because [4,1] is not non-decreasing.

Example 2:

Input: nums = [2,1,1,2], k = 2
Output: []
Explanation: There are no good indices in this array.


Constraints:

n == nums.length
3 <= n <= 10^5
1 <= nums[i] <= 10^6
1 <= k <= n / 2

"""

# V0
# IDEA : TWO RUN-LENGTH ARRAYS, ONE FROM EACH DIRECTION
#
#   dec[i] = how many elements ending at i are non-increasing
#   inc[i] = how many elements starting at i are non-decreasing
#   both are single sweeps with the usual "extend or reset to 1" rule.
#
#   index i is good iff the k elements BEFORE it form a non-increasing run
#   and the k elements AFTER it a non-decreasing one :
#       dec[i - 1] >= k   and   inc[i + 1] >= k
#   over the range k <= i < n - k.
#
# time = O(n), space = O(n)
class Solution(object):
    def goodIndices(self, nums, k):
        n = len(nums)

        dec = [1] * n
        for i in range(1, n):
            if nums[i] <= nums[i - 1]:
                dec[i] = dec[i - 1] + 1

        inc = [1] * n
        for i in range(n - 2, -1, -1):
            if nums[i] <= nums[i + 1]:
                inc[i] = inc[i + 1] + 1

        return [i for i in range(k, n - k)
                if dec[i - 1] >= k and inc[i + 1] >= k]
