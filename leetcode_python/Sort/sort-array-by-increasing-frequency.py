"""

1636. Sort Array by Increasing Frequency
Easy

Given an array of integers nums, sort the array in increasing order based on the frequency of the values. If multiple values have the same frequency, sort them in decreasing order.

Return the sorted array.


Example 1:

Input: nums = [1,1,2,2,2,3]
Output: [3,1,1,2,2,2]
Explanation: '3' has a frequency of 1, '1' has a frequency of 2, and '2' has a frequency of 3.

Example 2:

Input: nums = [2,3,1,3,2]
Output: [1,3,3,2,2]
Explanation: '2' and '3' both have a frequency of 2, so they are sorted in decreasing order.

Example 3:

Input: nums = [-1,1,-6,4,5,-6,1,4,1]
Output: [5,-1,4,4,-6,-6,1,1,1]


Constraints:

1 <= nums.length <= 100
-100 <= nums[i] <= 100

"""

# V0
# IDEA : COUNTER + CUSTOM SORT KEY
#
#   count occurrences, then sort every element by the composite key
#     (frequency ASC, value DESC)
#   which is expressed as (cnt[x], -x) so a single ascending sort handles
#   both directions.
#
#   NOTE : sort the ORIGINAL list (not the distinct keys) so each value is
#          emitted exactly `frequency` times, already grouped together.
#
# time = O(n log n), space = O(n)
from collections import Counter
class Solution(object):
    def frequencySort(self, nums):
        cnt = Counter(nums)
        return sorted(nums, key=lambda x: (cnt[x], -x))
