"""

1200. Minimum Absolute Difference
Easy

Given an array of distinct integers arr, find all pairs of elements with the
minimum absolute difference of any two elements.

Return a list of pairs in ascending order (with respect to pairs), each pair
[a, b] follows

a, b are from arr
a < b
b - a equals to the minimum absolute difference of any two elements in arr


Example 1:

Input: arr = [4,2,1,3]
Output: [[1,2],[2,3],[3,4]]
Explanation: The minimum absolute difference is 1. List all pairs with
difference equal to 1 in ascending order.

Example 2:

Input: arr = [1,3,6,10,15]
Output: [[1,3]]

Example 3:

Input: arr = [3,8,-10,23,19,-4,-14,27]
Output: [[-14,-10],[19,23],[23,27]]


Constraints:

2 <= arr.length <= 10^5
-10^6 <= arr[i] <= 10^6

"""

# V0
# IDEA: SORT + ADJACENT PAIRS
"""

 -> after sorting, the minimum difference can ONLY come from
    two ADJACENT elements (any non adjacent pair has a bigger gap).

 -> so: 1 pass to find the min gap, 1 pass to collect all pairs with that gap.
    (the collected list is already ascending, since arr is sorted)
"""
# time = O(n log n)
# space = O(n)
class Solution(object):
    def minimumAbsDifference(self, arr):
        arr = sorted(arr)

        # pass 1: min adjacent gap
        mn = min(arr[i + 1] - arr[i] for i in range(len(arr) - 1))

        # pass 2: collect
        res = []
        for i in range(len(arr) - 1):
            if arr[i + 1] - arr[i] == mn:
                res.append([arr[i], arr[i + 1]])

        return res


# V0-1
# IDEA : COUNTING SORT OVER THE VALUE RANGE (NO COMPARISON SORT)
#
#   values are bounded by |arr[i]| <= 10^6, so mark every value present in a
#   boolean bucket array and then walk the value axis upward. the previous
#   marked value is exactly the sorted predecessor, which gives the adjacent
#   gaps without ever comparing two elements.
#
#   two sweeps over the same buckets : one for the min gap, one to collect.
#
# time = O(n + R), R = max(arr) - min(arr)
# space = O(R)
class Solution(object):
    def minimumAbsDifference(self, arr):
        lo, hi = min(arr), max(arr)
        seen = [False] * (hi - lo + 1)
        for x in arr:
            seen[x - lo] = True

        # sweep 1 : smallest gap between consecutive marked values
        mn = float('inf')
        prev = None
        for i in range(hi - lo + 1):
            if seen[i]:
                if prev is not None and i - prev < mn:
                    mn = i - prev
                prev = i

        # sweep 2 : collect every consecutive pair whose gap == mn
        res = []
        prev = None
        for i in range(hi - lo + 1):
            if seen[i]:
                if prev is not None and i - prev == mn:
                    res.append([prev + lo, i + lo])
                prev = i
        return res


# V0-2
# IDEA : SORT + SINGLE PASS, RESET THE ANSWER WHEN A SMALLER GAP APPEARS
#
#   instead of learning the min gap first and collecting after, keep the best
#   gap seen so far together with its pairs : a strictly smaller gap wipes the
#   list, an equal gap appends to it. one pass over the sorted array.
#
# time = O(n log n)
# space = O(n)
class Solution(object):
    def minimumAbsDifference(self, arr):
        arr = sorted(arr)
        best = float('inf')
        res = []
        for a, b in zip(arr, arr[1:]):
            gap = b - a
            if gap < best:
                best = gap
                res = [[a, b]]
            elif gap == best:
                res.append([a, b])
        return res
