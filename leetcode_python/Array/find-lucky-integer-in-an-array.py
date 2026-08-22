"""

1394. Find Lucky Integer in an Array
Easy

Given an array of integers arr, a lucky integer is an integer that has a frequency in the array equal to its value.

Return the largest lucky integer in the array. If there is no lucky integer return -1.


Example 1:

Input: arr = [2,2,3,4]
Output: 2
Explanation: The only lucky number in the array is 2 because frequency[2] == 2.

Example 2:

Input: arr = [1,2,2,3,3,3]
Output: 3
Explanation: 1, 2 and 3 are all lucky numbers, return the largest of them.

Example 3:

Input: arr = [2,2,2,3,3]
Output: -1
Explanation: There are no lucky numbers in the array.


Constraints:

1 <= arr.length <= 500
1 <= arr[i] <= 500

"""

# V0
# IDEA : COUNTING (a value is lucky when count == value)
#
#   build freq[x] once, then scan the distinct values and keep the
#   largest x with freq[x] == x.
#   NOTE : return -1 when nothing qualifies, not 0.
#
# time = O(n), space = O(n)
from collections import Counter
class Solution(object):
    def findLucky(self, arr):
        cnt = Counter(arr)
        res = -1
        for x, v in cnt.items():
            if x == v and x > res:
                res = x
        return res


# V0-1
# IDEA : FIXED BUCKET ARRAY + SCAN FROM THE LARGEST VALUE DOWN
#
#   the constraints cap arr[i] at 500, so a 501-slot tally replaces the hash
#   map entirely, and because the buckets are already in value order we can
#   walk them DOWNWARDS and return on the first hit instead of comparing every
#   candidate against a running maximum.
#   the tally size does not depend on n, so the extra space is constant.
#
# time = O(n + 500), space = O(1)
class Solution(object):
    def findLucky(self, arr):
        freq = [0] * 501
        for x in arr:
            freq[x] += 1
        for x in range(500, 0, -1):
            if freq[x] == x:
                return x
        return -1


# V0-2
# IDEA : SORT, THEN MEASURE THE EQUAL-VALUE RUNS FROM THE RIGHT
#
#   sorting puts equal values next to each other, so a run's LENGTH is that
#   value's frequency — no counting structure at all. scanning the runs from
#   the right visits the candidates in decreasing order, so the first run
#   whose length equals its value is already the answer.
#
# time = O(n log n), space = O(n) for the sorted copy (O(1) if sorted in place)
class Solution(object):
    def findLucky(self, arr):
        vals = sorted(arr)
        i = len(vals) - 1
        while i >= 0:
            j = i
            while j >= 0 and vals[j] == vals[i]:
                j -= 1
            if i - j == vals[i]:
                return vals[i]
            i = j
        return -1
