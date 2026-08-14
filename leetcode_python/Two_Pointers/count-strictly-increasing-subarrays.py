"""

2393. Count Strictly Increasing Subarrays
Medium
(premium / locked problem)

You are given an array nums consisting of positive integers.

Return the number of subarrays of nums that are in strictly increasing order.

A subarray is a contiguous part of an array.


Example 1:

Input: nums = [1,3,5,4,4,6]
Output: 10
Explanation: The strictly increasing subarrays are the following:
- Subarrays of length 1: [1], [3], [5], [4], [4], [6].
- Subarrays of length 2: [1,3], [3,5], [4,6].
- Subarrays of length 3: [1,3,5].
The total number of subarrays is 6 + 3 + 1 = 10.

Example 2:

Input: nums = [1,2,3,4,5]
Output: 15
Explanation: Every subarray is strictly increasing. There are 15 possible subarrays that we can take.


Constraints:

1 <= nums.length <= 10^5
1 <= nums[i] <= 10^6

"""

# V0
# IDEA : SPLIT INTO MAXIMAL INCREASING RUNS AND COUNT EACH ONE'S SUBARRAYS
#
#   a subarray is strictly increasing iff it lies inside one maximal
#   increasing run. a run of length L contributes
#       L * (L + 1) / 2
#   subarrays (all its contiguous pieces).
#
#   the equivalent one-pass form used below keeps `run` = the length of the
#   increasing run ending at the current index and adds it each step, which
#   sums to the same thing without materialising the runs.
#
# time = O(n), space = O(1)
class Solution(object):
    def countSubarrays(self, nums):
        res = 0
        run = 0
        for i, x in enumerate(nums):
            run = run + 1 if i and nums[i - 1] < x else 1
            res += run
        return res
