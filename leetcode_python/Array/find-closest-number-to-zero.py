"""

2239. Find Closest Number to Zero
Easy

Given an integer array nums of size n, return the number with the value closest to 0 in nums. If there are multiple answers, return the number with the largest value.


Example 1:

Input: nums = [-4,-2,1,4,8]
Output: 1
Explanation:
The distance from -4 to 0 is |-4| = 4.
The distance from -2 to 0 is |-2| = 2.
The distance from 1 to 0 is |1| = 1.
The distance from 4 to 0 is |4| = 4.
The distance from 8 to 0 is |8| = 8.
Thus, the closest number to 0 in the array is 1.

Example 2:

Input: nums = [2,-1,1]
Output: 1
Explanation: 1 and -1 are both the closest numbers to 0, so 1 being larger is returned.


Constraints:

1 <= n <= 1000
-10^5 <= nums[i] <= 10^5

"""

# V0
# IDEA : SORT KEY = (DISTANCE TO ZERO, NEGATED VALUE)
#
#   the primary criterion is abs(x); the tie-break wants the LARGER value, so
#   negating x makes python's "smallest wins" pick the larger one.
#
#   using max()/min() with a key avoids a full sort and keeps it one pass.
#
# time = O(n), space = O(1)
class Solution(object):
    def findClosestNumber(self, nums):
        return min(nums, key=lambda x: (abs(x), -x))
