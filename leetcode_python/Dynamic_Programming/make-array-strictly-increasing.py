"""

1187. Make Array Strictly Increasing
Hard

Given two integer arrays arr1 and arr2, return the minimum number of operations
(possibly zero) needed to make arr1 strictly increasing.

In one operation, you can choose two indices 0 <= i < arr1.length and
0 <= j < arr2.length and do the assignment arr1[i] = arr2[j].

If there is no way to make arr1 strictly increasing, return -1.


Example 1:

Input: arr1 = [1,5,3,6,7], arr2 = [1,3,2,4]
Output: 1
Explanation: Replace 5 with 2, then arr1 = [1, 2, 3, 6, 7].

Example 2:

Input: arr1 = [1,5,3,6,7], arr2 = [4,3,1]
Output: 2
Explanation: Replace 5 with 3 and then replace 3 with 4. arr1 = [1, 3, 4, 6, 7].

Example 3:

Input: arr1 = [1,5,3,6,7], arr2 = [1,6,3,3]
Output: -1
Explanation: You can't make arr1 strictly increasing.


Constraints:

1 <= arr1.length, arr2.length <= 2000
0 <= arr1[i], arr2[i] <= 10^9

"""

# V0
# IDEA: DP over "last value kept" + BINARY SEARCH
"""

 DP def:
    - dp = { last_value : min operations to make arr1[0..i] strictly increasing
             AND end with `last_value` }


 DP eq (for each new element `a` of arr1, for each (prev, cost) in dp):
    - keep a      : if a > prev            -> ndp[a] = min(ndp[a], cost)
    - replace a   : v = smallest arr2 value > prev
                                           -> ndp[v] = min(ndp[v], cost + 1)

 -> only the SMALLEST valid replacement matters (any bigger one is dominated)
"""
# time = O(n * m log m)
# n = len(arr1), m = len(arr2); the dp dict holds at most m + 1 keys
# space = O(m)
import bisect
class Solution(object):
    def makeArrayIncreasing(self, arr1, arr2):
        # dedup + sort, so binary search can find the next bigger candidate
        arr2 = sorted(set(arr2))

        # -inf sentinel = "nothing placed yet"
        dp = {float('-inf'): 0}

        for a in arr1:
            ndp = {}
            for prev, cost in dp.items():
                # case 1) keep arr1[i] as is
                if a > prev:
                    if a not in ndp or ndp[a] > cost:
                        ndp[a] = cost

                # case 2) replace arr1[i] with the smallest arr2 value > prev
                j = bisect.bisect_right(arr2, prev)
                if j < len(arr2):
                    v = arr2[j]
                    if v not in ndp or ndp[v] > cost + 1:
                        ndp[v] = cost + 1

            if not ndp:
                return -1
            dp = ndp

        return min(dp.values())
