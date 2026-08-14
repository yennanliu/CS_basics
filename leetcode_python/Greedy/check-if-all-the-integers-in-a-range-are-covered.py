"""

1893. Check if All the Integers in a Range Are Covered
Easy

You are given a 2D integer array ranges and two integers left and right. Each ranges[i] = [starti, endi] represents an inclusive interval between starti and endi.

Return true if each integer in the inclusive range [left, right] is covered by at least one interval in ranges. Return false otherwise.

An integer x is covered by an interval ranges[i] = [starti, endi] if starti <= x <= endi.


Example 1:

Input: ranges = [[1,2],[3,4],[5,6]], left = 2, right = 5
Output: true
Explanation: Every integer between 2 and 5 is covered:
- 2 is covered by the first range.
- 3 and 4 are covered by the second range.
- 5 is covered by the third range.

Example 2:

Input: ranges = [[1,10],[10,20]], left = 21, right = 21
Output: false
Explanation: 21 is not covered by any range.


Constraints:

1 <= ranges.length <= 50
1 <= starti <= endi <= 50
1 <= left <= right <= 50

"""

# V0
# IDEA : DIFFERENCE ARRAY (values are bounded by 50)
#
#   every interval [s, e] bumps a difference array :
#     diff[s]     += 1
#     diff[e + 1] -= 1
#   the running prefix sum at position x == how many intervals cover x.
#
#   then a single scan of [left, right] : if any position has coverage 0,
#   the answer is False.
#
#   NOTE : size the array to 52 so that diff[e + 1] with e = 50 is safe.
#
# time = O(n + M), M = 52
# space = O(M)
class Solution(object):
    def isCovered(self, ranges, left, right):
        diff = [0] * 52
        for s, e in ranges:
            diff[s] += 1
            diff[e + 1] -= 1

        cover = 0
        for x in range(52):
            cover += diff[x]
            if left <= x <= right and cover <= 0:
                return False

        return True
