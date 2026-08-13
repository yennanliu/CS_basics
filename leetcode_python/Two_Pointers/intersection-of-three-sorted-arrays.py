"""

1213. Intersection of Three Sorted Arrays
Easy

Given three integer arrays arr1, arr2 and arr3 sorted in strictly increasing
order, return a sorted array of only the integers that appeared in all three
arrays.


Example 1:

Input: arr1 = [1,2,3,4,5], arr2 = [1,2,5,7,9], arr3 = [1,3,4,5,8]
Output: [1,5]
Explanation: Only 1 and 5 appeared in the three arrays.

Example 2:

Input: arr1 = [197,418,523,876,1356], arr2 = [501,880,1593,1710,1870],
       arr3 = [521,682,1337,1395,1764]
Output: []


Constraints:

1 <= arr1.length, arr2.length, arr3.length <= 1000
1 <= arr1[i], arr2[i], arr3[i] <= 2000

"""

# V0
# IDEA: 3 POINTERS (k-way merge on sorted arrays)
"""

 -> all 3 arrays are sorted STRICTLY increasing.

 -> keep one pointer per array.
    if the 3 pointed values are equal -> it is a common element, advance all 3.
    otherwise -> advance every pointer whose value is BELOW the current max
                 (those values can never be matched again).
"""
# time = O(n1 + n2 + n3)
# space = O(1), excluding the output
class Solution(object):
    def arraysIntersection(self, arr1, arr2, arr3):
        i = j = k = 0
        res = []

        while i < len(arr1) and j < len(arr2) and k < len(arr3):
            if arr1[i] == arr2[j] == arr3[k]:
                res.append(arr1[i])
                i += 1
                j += 1
                k += 1
            else:
                mx = max(arr1[i], arr2[j], arr3[k])
                if arr1[i] < mx:
                    i += 1
                if arr2[j] < mx:
                    j += 1
                if arr3[k] < mx:
                    k += 1

        return res
