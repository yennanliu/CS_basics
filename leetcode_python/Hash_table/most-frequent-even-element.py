"""

2404. Most Frequent Even Element
Easy

Given an integer array nums, return the most frequent even element.

If there is a tie, return the smallest one. If there is no such element, return -1.


Example 1:

Input: nums = [0,1,2,2,4,4,1]
Output: 2
Explanation:
The even elements are 0, 2, 2, 4, and 4. Of these, 2 and 4 appear the most.
We return the smallest one, which is 2.

Example 2:

Input: nums = [4,4,4,9,2,4]
Output: 4
Explanation: 4 is the even element appears the most.

Example 3:

Input: nums = [29,47,21,41,13,37,25,7]
Output: -1
Explanation: There is no even element.


Constraints:

1 <= nums.length <= 2000
0 <= nums[i] <= 10^5

"""

# V0
# IDEA : COUNT ONLY THE EVEN VALUES, THEN ARGMAX WITH A SMALLEST-VALUE TIE-BREAK
#
#   filtering to the even elements first makes the "no such element" case a
#   simple empty-counter check.
#
#   the tie-break wants the largest count but the SMALLEST value, so the sort
#   key negates the count and keeps the value ascending — min() then picks
#   the right one in a single pass.
#
# time = O(n), space = O(n)
from collections import Counter


class Solution(object):
    def mostFrequentEven(self, nums):
        cnt = Counter(x for x in nums if x % 2 == 0)
        if not cnt:
            return -1
        return min(cnt, key=lambda v: (-cnt[v], v))
