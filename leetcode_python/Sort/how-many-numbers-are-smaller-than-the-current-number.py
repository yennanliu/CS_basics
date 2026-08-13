"""

1365. How Many Numbers Are Smaller Than the Current Number
Easy

Given the array nums, for each nums[i] find out how many numbers in the array are smaller than it. That is, for each nums[i] you have to count the number of valid j's such that j != i and nums[j] < nums[i].

Return the answer in an array.


Example 1:

Input: nums = [8,1,2,2,3]
Output: [4,0,1,1,3]
Explanation:
For nums[0]=8 there exist four smaller numbers than it (1, 2, 2 and 3).
For nums[1]=1 does not exist any smaller number than it.
For nums[2]=2 there exist one smaller number than it (1).
For nums[3]=2 there exist one smaller number than it (1).
For nums[4]=3 there exist three smaller numbers than it (1, 2 and 2).

Example 2:

Input: nums = [6,5,4,8]
Output: [2,1,0,3]

Example 3:

Input: nums = [7,7,7,7]
Output: [0,0,0,0]


Constraints:

2 <= nums.length <= 500
0 <= nums[i] <= 100

"""

# V0
# IDEA : SORT + BINARY SEARCH (bisect_left)
#
#  In a sorted copy, the index of the FIRST occurrence of x is exactly
#  how many elements are strictly smaller than x -> bisect_left(arr, x).
#  Duplicates are handled for free (they all map to the same index).
#
# time = O(n log n)
# space = O(n)
from bisect import bisect_left


class Solution(object):
    def smallerNumbersThanCurrent(self, nums):
        arr = sorted(nums)
        return [bisect_left(arr, x) for x in nums]


# V1
# IDEA : COUNTING SORT + PREFIX SUM
#        values are bounded by 0 <= nums[i] <= 100, so bucket count them
#        and prefix-sum: s[x] = #elements strictly smaller than x
# time = O(n + M)
# space = O(M)
# M = 100 (max value)
from itertools import accumulate


class Solution2(object):
    def smallerNumbersThanCurrent(self, nums):
        cnt = [0] * 102
        for x in nums:
            cnt[x + 1] += 1
        s = list(accumulate(cnt))
        return [s[x] for x in nums]
