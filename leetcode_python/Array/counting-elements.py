"""

1426. Counting Elements
Easy

Given an integer array arr, count how many elements x there are, such that
x + 1 is also in arr. If there are duplicates in arr, count them separately.


Example 1:

Input: arr = [1,2,3]
Output: 2
Explanation: 1 and 2 are counted cause 2 and 3 are in arr.

Example 2:

Input: arr = [1,1,3,3,5,5,7,7]
Output: 0
Explanation: No numbers are counted, cause there is no 2, 4, 6, or 8 in arr.


Constraints:

1 <= arr.length <= 1000
0 <= arr[i] <= 1000

"""

# V0
# IDEA : HASH TABLE (counter)
#
#  -> for every distinct x, if (x + 1) is present,
#     ALL occurrences of x count -> add cnt[x]
#
# time = O(n)
# space = O(n)
from collections import Counter
class Solution(object):
    def countElements(self, arr):
        cnt = Counter(arr)
        res = 0
        for x, c in cnt.items():
            if x + 1 in cnt:
                res += c
        return res


# V1
# IDEA : SET
#
#  -> no counting needed, just membership of (x + 1)
#
# time = O(n)
# space = O(n)
class Solution(object):
    def countElements(self, arr):
        seen = set(arr)
        return sum(1 for x in arr if x + 1 in seen)
