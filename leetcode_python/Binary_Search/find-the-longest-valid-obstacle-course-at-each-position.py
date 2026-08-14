"""

1964. Find the Longest Valid Obstacle Course at Each Position
Hard

You want to build some obstacle courses. You are given a 0-indexed integer array obstacles of length n, where obstacles[i] describes the height of the ith obstacle.

For every index i between 0 and n - 1 (inclusive), find the length of the longest obstacle course in obstacles such that:

You choose any number of obstacles between 0 and i inclusive.
You must include the ith obstacle in the course.
You must put the chosen obstacles in the same order as they appear in obstacles.
Every obstacle (except the first) is taller than or the same height as the obstacle immediately before it.

Return an array ans of length n, where ans[i] is the length of the longest obstacle course for index i as described above.


Example 1:

Input: obstacles = [1,2,3,2]
Output: [1,2,3,3]
Explanation: The longest valid obstacle course at each position is:
- i = 0: [1], [1] has length 1.
- i = 1: [1,2], [1,2] has length 2.
- i = 2: [1,2,3], [1,2,3] has length 3.
- i = 3: [1,2,3,2], [1,2,2] has length 3.

Example 2:

Input: obstacles = [2,2,1]
Output: [1,2,1]
Explanation: The longest valid obstacle course at each position is:
- i = 0: [2], [2] has length 1.
- i = 1: [2,2], [2,2] has length 2.
- i = 2: [2,2,1], [1] has length 1.

Example 3:

Input: obstacles = [3,1,5,6,4,2]
Output: [1,1,2,3,2,2]
Explanation: The longest valid obstacle course at each position is:
- i = 0: [3], [3] has length 1.
- i = 1: [3,1], [1] has length 1.
- i = 2: [3,1,5], [3,5] has length 2. [1,5] is also valid.
- i = 3: [3,1,5,6], [3,5,6] has length 3. [1,5,6] is also valid.
- i = 4: [3,1,5,6,4], [3,4] has length 2. [1,4] is also valid.
- i = 5: [3,1,5,6,4,2], [1,2] has length 2.


Constraints:

n == obstacles.length
1 <= n <= 10^5
1 <= obstacles[i] <= 10^7

"""

# V0
# IDEA : LIS (non-decreasing) via PATIENCE / BINARY SEARCH ON TAILS
#
#   this is "length of the longest non-decreasing subsequence ENDING at i".
#   keep tails[L] = smallest possible tail of a non-decreasing subsequence of
#   length L+1 seen so far. tails is non-decreasing, so we can binary search.
#
#   for each x :
#     - pos = first index with tails[pos] > x  (bisect_right, because EQUAL
#       heights are allowed and must extend the run, not replace it)
#     - ans[i] = pos + 1
#     - tails[pos] = x   (append when pos == len(tails))
#
#   NOTE : bisect_right (not bisect_left) is the whole trick here - with
#          bisect_left [2,2,1] would give [1,1,1] instead of [1,2,1].
#
# time = O(n log n), space = O(n)
from bisect import bisect_right
class Solution(object):
    def longestObstacleCourseAtEachPosition(self, obstacles):
        tails = []
        res = []
        for x in obstacles:
            pos = bisect_right(tails, x)
            res.append(pos + 1)
            if pos == len(tails):
                tails.append(x)
            else:
                tails[pos] = x
        return res
