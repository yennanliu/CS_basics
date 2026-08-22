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


# V0-1
# IDEA : SORT + BINARY SEARCH FOR THE INSERTION POINT OF 0
#
#   once the array is sorted, the value closest to 0 has to be one of the two
#   neighbours of the slot where 0 would be inserted: the last negative and the
#   first non-negative. compare just those two.
#
#   NOTE : the tie-break "return the larger value" is exactly "prefer the
#          non-negative side", so the test is hi <= -lo (abs(lo) == -lo since
#          lo < 0).
#
# time = O(n log n)
# space = O(n)
import bisect
class Solution(object):
    def findClosestNumber(self, nums):
        arr = sorted(nums)
        k = bisect.bisect_left(arr, 0)
        lo = arr[k - 1] if k > 0 else None
        hi = arr[k] if k < len(arr) else None
        if lo is None:
            return hi
        if hi is None:
            return lo
        return hi if hi <= -lo else lo


# V0-2
# IDEA : MEMBERSHIP SET + SWEEP THE DISTANCE OUTWARD FROM 0
#
#   search the VALUE space instead of ranking the elements: drop the numbers in
#   a set, then try distances d = 0, 1, 2, ... and ask whether +d or -d is
#   present. the first hit is the answer, and probing +d before -d hands us the
#   "larger value wins" tie-break for free.
#
#   NOTE : the loop is bounded by max(abs(x)), which the constraints cap at
#          10^5, so this trades element comparisons for value-range steps.
#
# time = O(n + M) with M = max abs value
# space = O(n)
class Solution(object):
    def findClosestNumber(self, nums):
        s = set(nums)
        limit = max(abs(x) for x in nums)
        for d in range(limit + 1):
            if d in s:
                return d
            if -d in s:
                return -d
        return 0
