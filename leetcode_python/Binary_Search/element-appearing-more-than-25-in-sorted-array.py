"""

1287. Element Appearing More Than 25% In Sorted Array
Easy

Given an integer array sorted in non-decreasing order, there is exactly one
integer in the array that occurs more than 25% of the time, return that integer.


Example 1:

Input: arr = [1,2,2,6,6,6,6,7,10]
Output: 6

Example 2:

Input: arr = [1,1]
Output: 1


Constraints:

1 <= arr.length <= 10^4
0 <= arr[i] <= 10^5

"""

# V0
# IDEA: SORTED ARRAY property + fixed offset check
#
#  -> if x occupies more than n/4 of a SORTED array,
#     its run has length > n/4, so arr[i] == arr[i + n//4]
#     must hold at the run's start index i.
#
# time = O(n)
# space = O(1)
class Solution(object):
    def findSpecialInteger(self, arr):
        n = len(arr)
        step = n // 4
        for i in range(n - step):
            if arr[i] == arr[i + step]:
                return arr[i]
        return arr[0]


# V1
# IDEA: BINARY SEARCH on the 3 quarter positions
#
#  -> a run longer than n/4 must cover at least one of
#     the indices n//4, n//2, 3*n//4.
#     for each candidate, binary search its first / last index
#     and check the run length.
#
# time = O(log n)
# space = O(1)
import bisect
class Solution(object):
    def findSpecialInteger(self, arr):
        n = len(arr)
        for i in (n // 4, n // 2, (3 * n) // 4):
            x = arr[i]
            lo = bisect.bisect_left(arr, x)
            hi = bisect.bisect_right(arr, x)
            if (hi - lo) * 4 > n:
                return x
        return arr[0]
