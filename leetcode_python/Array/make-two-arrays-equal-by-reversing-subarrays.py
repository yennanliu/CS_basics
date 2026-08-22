"""

1460. Make Two Arrays Equal by Reversing Sub-arrays
Easy

You are given two integer arrays of equal length target and arr. In one step, you can select any non-empty subarray of arr and reverse it. You are allowed to make any number of steps.

Return true if you can make arr equal to target or false otherwise.


Example 1:

Input: target = [1,2,3,4], arr = [2,4,1,3]
Output: true
Explanation: You can follow the next steps to convert arr to target:
1- Reverse subarray [2,4,1], arr becomes [1,4,2,3]
2- Reverse subarray [4,2], arr becomes [1,2,4,3]
3- Reverse subarray [4,3], arr becomes [1,2,3,4]
There are multiple ways to convert arr to target, this is not the only way to do so.

Example 2:

Input: target = [7], arr = [7]
Output: true
Explanation: arr is equal to target without any reverses.

Example 3:

Input: target = [3,7,9], arr = [3,7,11]
Output: false
Explanation: arr does not have value 9 and it can never be converted to target.


Constraints:

target.length == arr.length
1 <= target.length <= 1000
1 <= target[i] <= 1000
1 <= arr[i] <= 1000

"""

# V0
# IDEA : MULTISET EQUALITY (reversals generate every permutation)
#
#   reversing a length-2 subarray is an adjacent swap, and adjacent swaps
#   compose into any permutation, so the order of arr is fully free.
#   the only thing that cannot change is WHICH values are present and how
#   many times -> compare the two multisets.
#   NOTE : counts matter, so a plain set comparison would be wrong.
#
# time = O(n), space = O(n)
from collections import Counter
class Solution(object):
    def canBeEqual(self, target, arr):
        return Counter(target) == Counter(arr)


# V0-1
# IDEA : SORT BOTH AND COMPARE ELEMENTWISE
#
#   two multisets are equal exactly when their sorted forms are identical,
#   so sorting normalises away the order that the reversals are free to
#   change. no auxiliary counting structure at all.
#
# time = O(n log n), space = O(n)
class Solution(object):
    def canBeEqual(self, target, arr):
        return sorted(target) == sorted(arr)


# V0-2
# IDEA : GREEDY MATCH-AND-DELETE (no hashing, no sorting)
#
#   walk target and consume one equal element from a working copy of arr for
#   each value. a value with no partner left proves the multisets differ; if
#   every value finds one the copy ends empty.
#   the linear search hidden inside list.remove is what makes this the
#   O(n^2) baseline that the Counter version above optimises away.
#
# time = O(n^2), space = O(n)
class Solution(object):
    def canBeEqual(self, target, arr):
        if len(target) != len(arr):
            return False
        pool = list(arr)
        for v in target:
            try:
                pool.remove(v)
            except ValueError:
                return False
        return not pool
