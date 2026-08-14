"""

1502. Can Make Arithmetic Progression From Sequence
Easy

A sequence of numbers is called an arithmetic progression
if the difference between any two consecutive elements is the same.

Given an array of numbers arr, return true if the array can be rearranged
to form an arithmetic progression. Otherwise, return false.


Example 1:

Input: arr = [3,5,1]
Output: true
Explanation: We can reorder the elements as [1,3,5] or [5,3,1] with differences 2 and -2
respectively, between each consecutive elements.

Example 2:

Input: arr = [1,2,4]
Output: false
Explanation: There is no way to reorder the elements to obtain an arithmetic progression.


Constraints:

2 <= arr.length <= 1000
-10^6 <= arr[i] <= 10^6

"""

# V0
# IDEA : SORT + CHECK ADJACENT DIFF
# time = O(n log n)
# space = O(n)
class Solution(object):
    def canMakeArithmeticProgression(self, arr):
        arr.sort()
        d = arr[1] - arr[0]
        for i in range(2, len(arr)):
            if arr[i] - arr[i - 1] != d:
                return False
        return True


# V1
# IDEA : MATH + SET (no sorting)
#   with n items, min, max, the common diff must be (max - min) / (n - 1),
#   and all items must be distinct (unless d == 0), so we can just verify
#   that min + i * d exists in the set for every i
# time = O(n)
# space = O(n)
class Solution(object):
    def canMakeArithmeticProgression(self, arr):
        n = len(arr)
        lo, hi = min(arr), max(arr)

        # all the same value -> valid AP with d = 0
        if lo == hi:
            return True

        # (hi - lo) must be evenly split into (n - 1) steps
        if (hi - lo) % (n - 1) != 0:
            return False

        d = (hi - lo) // (n - 1)
        seen = set(arr)

        # duplicates would break the progression (d != 0 here)
        if len(seen) != n:
            return False

        for i in range(n):
            if lo + i * d not in seen:
                return False
        return True
