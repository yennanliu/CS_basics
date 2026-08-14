"""

1122. Relative Sort Array
Easy

Given two arrays arr1 and arr2, the elements of arr2 are distinct, and all elements in arr2 are also in arr1.

Sort the elements of arr1 such that the relative ordering of items in arr1 are the same as in arr2.
Elements that do not appear in arr2 should be placed at the end of arr1 in ascending order.


Example 1:

Input: arr1 = [2,3,1,3,2,4,6,7,9,2,19], arr2 = [2,1,4,3,9,6]
Output: [2,2,2,1,4,3,3,9,6,7,19]

Example 2:

Input: arr1 = [28,6,22,8,44,17], arr2 = [22,28,8,6]
Output: [22,28,8,6,17,44]


Constraints:

1 <= arr1.length, arr2.length <= 1000
0 <= arr1[i], arr2[i] <= 1000
All the elements of arr2 are distinct.
Each arr2[i] is in arr1.

"""

# V0
# IDEA : CUSTOM SORT KEY (rank from arr2, then value)
#
#   build rank[x] = position of x inside arr2.
#   sort arr1 by that rank :
#     - x present in arr2  -> key = rank[x]        (small, keeps arr2 order)
#     - x absent from arr2 -> key = 1000 + x       (all bigger than any rank,
#                                                   and ordered ascending by x)
#   NOTE : arr2 has at most 1000 entries and values are <= 1000, so the
#          offset 1000 cleanly separates the two groups.
#
# time = O(n log n), space = O(n)   n = len(arr1)
class Solution(object):
    def relativeSortArray(self, arr1, arr2):
        rank = {}
        for i, x in enumerate(arr2):
            rank[x] = i
        return sorted(arr1, key=lambda x: rank[x] if x in rank else 1000 + x)
