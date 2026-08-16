"""

3659. Partition Array Into K-Distinct Groups
Medium

You are given an integer array nums of length n and an integer k.

Your task is to determine whether it is possible to partition all elements of nums into one or more groups such that:

Each group contains exactly k elements.
All elements within each group are distinct.

Return true if such a partition is possible, otherwise return false.


Example 1:

Input: nums = [1,2,3,4], k = 2
Output: true
Explanation:
nums can be partitioned into the groups [1, 2] and [3, 4], each of size k = 2 and containing distinct elements.

Example 2:

Input: nums = [3,5,2,2], k = 2
Output: true
Explanation:
nums can be partitioned into the groups [3, 2] and [5, 2], each of size k = 2 and containing distinct elements.


Constraints:

1 <= k <= n == nums.length <= 10^5
1 <= nums[i] <= 10^9

"""

# V0
# IDEA : COUNTING ARGUMENT — n % k == 0 AND maxFreq <= n / k
#
#   there will be exactly g = n / k groups, so n must be divisible by k.
#
#   a value appearing c times must be spread over c different groups (no
#   group may hold two copies), so c <= g is necessary. it is also
#   sufficient: sort the multiset by value into one long list and deal it
#   round-robin into the g groups — equal values land k apart in the list, so
#   with c <= g they always land in distinct groups.
#
# time = O(n), space = O(n)
from collections import Counter


class Solution(object):
    def partitionArray(self, nums, k):
        n = len(nums)
        if n % k:
            return False
        groups = n // k
        return max(Counter(nums).values()) <= groups
