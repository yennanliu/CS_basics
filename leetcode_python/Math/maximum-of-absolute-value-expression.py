"""

1131. Maximum of Absolute Value Expression
Medium

Given two arrays of integers with equal lengths, return the maximum value of:

|arr1[i] - arr1[j]| + |arr2[i] - arr2[j]| + |i - j|

where the maximum is taken over all 0 <= i, j < arr1.length.


Example 1:

Input: arr1 = [1,2,3,4], arr2 = [-1,4,5,6]
Output: 13

Example 2:

Input: arr1 = [1,-2,-5,0,10], arr2 = [0,-2,-1,-7,-4]
Output: 20


Constraints:

2 <= arr1.length == arr2.length <= 40000
-10^6 <= arr1[i], arr2[i] <= 10^6

"""

# V0
# IDEA : MATH (remove abs by enumerating signs)
#        WLOG i >= j, then
#          |x_i - x_j| + |y_i - y_j| + (i - j)
#          = max over a,b in {+1,-1} of
#              (a*x_i + b*y_i + i) - (a*x_j + b*y_j + j)
#        (a wrong sign choice only UNDER-estimates, so max is still correct)
#        -> for each of the 4 (a, b) combos, answer candidate is
#           max(f) - min(f) where f(i) = a*arr1[i] + b*arr2[i] + i
# time = O(n)
# space = O(1)
class Solution(object):
    def maxAbsValExpr(self, arr1, arr2):
        n = len(arr1)
        res = 0
        for a in (1, -1):
            for b in (1, -1):
                mx = float('-inf')
                mi = float('inf')
                for i in range(n):
                    v = a * arr1[i] + b * arr2[i] + i
                    mx = max(mx, v)
                    mi = min(mi, v)
                res = max(res, mx - mi)
        return res
