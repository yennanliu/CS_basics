"""

1385. Find the Distance Value Between Two Arrays
Easy

Given two integer arrays arr1 and arr2, and the integer d, return the distance
value between the two arrays.

The distance value is defined as the number of elements arr1[i] such that there
is not any element arr2[j] where |arr1[i]-arr2[j]| <= d.


Example 1:

Input: arr1 = [4,5,8], arr2 = [10,9,1,8], d = 2
Output: 2
Explanation:
For arr1[0]=4 we have:
|4-10|=6 > d=2
|4-9|=5 > d=2
|4-1|=3 > d=2
|4-8|=4 > d=2
For arr1[1]=5 we have:
|5-10|=5 > d=2
|5-9|=4 > d=2
|5-1|=4 > d=2
|5-8|=3 > d=2
For arr1[2]=8 we have:
|8-10|=2 <= d=2
|8-9|=1 <= d=2
|8-1|=7 > d=2
|8-8|=0 <= d=2

Example 2:

Input: arr1 = [1,4,2,3], arr2 = [-4,-3,6,10,20,30], d = 3
Output: 2

Example 3:

Input: arr1 = [2,1,100,3], arr2 = [-5,-2,10,-3,7], d = 6
Output: 1


Constraints:

1 <= arr1.length, arr2.length <= 500
-1000 <= arr1[i], arr2[j] <= 1000
0 <= d <= 100

"""

# V0
# IDEA: sort arr2 + binary search
#
#  for x in arr1 we only need to know whether arr2 has ANY value inside
#  the window [x - d, x + d].
#  -> bisect_left gives the first arr2 value >= x - d;
#     if that value exists and is <= x + d, x is disqualified.
#
# time = O((m + n) log n)
# space = O(n), sorting
from bisect import bisect_left
class Solution(object):
    def findTheDistanceValue(self, arr1, arr2, d):
        arr2 = sorted(arr2)
        res = 0
        for x in arr1:
            i = bisect_left(arr2, x - d)
            if i == len(arr2) or arr2[i] > x + d:
                res += 1
        return res


# V1
# IDEA: brute force double loop (arr lengths <= 500, so 250k checks)
# time = O(m * n)
# space = O(1)
class Solution(object):
    def findTheDistanceValue(self, arr1, arr2, d):
        return sum(all(abs(x - y) > d for y in arr2) for x in arr1)
