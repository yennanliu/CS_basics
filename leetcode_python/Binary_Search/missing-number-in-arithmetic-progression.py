"""

1228. Missing Number In Arithmetic Progression
Easy

In some array arr, the values were in arithmetic progression: the values
arr[i + 1] - arr[i] are all equal for every 0 <= i < arr.length - 1.

A value from arr was removed that was not the first or last value in the array.

Given arr, return the removed value.

Example 1:

Input: arr = [5,7,11,13]
Output: 9
Explanation: The previous array was [5,7,9,11,13].

Example 2:

Input: arr = [15,13,12]
Output: 14
Explanation: The previous array was [15,14,13,12].


Constraints:

3 <= arr.length <= 1000
0 <= arr[i] <= 10^5
The given array is guaranteed to be a valid array.

"""

# V0
# IDEA : BINARY SEARCH ON THE "STILL IN PLACE" PREDICATE
"""
 The original progression had n + 1 terms (n = len(arr)), so the real common
 difference is:

     d = (arr[-1] - arr[0]) / n

 Every element BEFORE the gap satisfies arr[i] == arr[0] + i * d,
 every element AFTER the gap does NOT (it is shifted left by one slot).
 That predicate is monotonic -> binary search for the first index where it
 breaks. The missing value is arr[0] + idx * d.
"""
# time = O(log n)
# space = O(1)
class Solution(object):
    def missingNumber(self, arr):
        n = len(arr)
        d = (arr[-1] - arr[0]) // n

        lo, hi = 0, n - 1
        while lo < hi:
            mid = (lo + hi) // 2
            if arr[mid] == arr[0] + mid * d:
                lo = mid + 1
            else:
                hi = mid
        return arr[0] + lo * d


# V1
# IDEA : ARITHMETIC SERIES SUM
#        the complete progression (n + 1 terms) sums to (a1 + an) * (n + 1) / 2,
#        so the missing value is that total minus sum(arr)
# time = O(n)
# space = O(1)
class Solution_1(object):
    def missingNumber(self, arr):
        n = len(arr)
        return (arr[0] + arr[-1]) * (n + 1) // 2 - sum(arr)
