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
