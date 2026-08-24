"""

1182. Shortest Distance to Target Color
Medium

You are given an array colors, in which there are three colors: 1, 2 and 3.

You are also given some queries. Each query consists of two integers i and c,
return the shortest distance between the given index i and the target color c.
If there is no solution return -1.


Example 1:

Input: colors = [1,1,2,1,3,2,2,3,3], queries = [[1,3],[2,2],[6,1]]
Output: [3,0,3]
Explanation:
The nearest 3 from index 1 is at index 4 (3 steps away).
The nearest 2 from index 2 is at index 2 itself (0 steps away).
The nearest 1 from index 6 is at index 3 (3 steps away).

Example 2:

Input: colors = [1,2], queries = [[0,3]]
Output: [-1]
Explanation: There is no 3 in the array.


Constraints:

1 <= colors.length <= 5*10^4
1 <= colors[i] <= 3
1 <= queries.length <= 5*10^4
queries[i].length == 2
0 <= queries[i][0] < colors.length
1 <= queries[i][1] <= 3

"""

# V0
# IDEA : PREFIX / SUFFIX DP (nearest occurrence of each color on each side)
#
#   only 3 colors exist, so precompute two tables :
#     left[i][c]  = largest index <= i holding color c   (-1 if none)
#     right[i][c] = smallest index >= i holding color c  (n  if none)
#
#   both are simple scans :
#     left[i]  = copy of left[i-1],  then left[i][colors[i]] = i
#     right[i] = copy of right[i+1], then right[i][colors[i]] = i
#
#   a query (i, c) is then answered in O(1) :
#     min(i - left[i][c], right[i][c] - i), or -1 when both sides miss.
#   NOTE : keep the sentinels large/small enough that a missing side never
#          wins the min -- we test against n before returning.
#
"""

DP def
    only 3 colours exist, so precompute two tables:

    left[i][c] : the LARGEST index <= i holding colour c   (-1 if none)

    right[i][c]: the SMALLEST index >= i holding colour c  (n if none)

DP eq

     left[i]  = copy of left[i-1],   then left[i][colors[i]]  = i

     right[i] = copy of right[i+1],  then right[i][colors[i]] = i


    -> e.g. a query (i, c) is then O(1):

         min( i - left[i][c], right[i][c] - i )
         or -1 when BOTH sides miss

     NOTE !!! keep the sentinels (-1 / n) far enough out that a missing side
              never wins the min - test against them before returning

     ans = one answer per query

"""
# time = O(n + q), space = O(n)
class Solution(object):
    def shortestDistanceColor(self, colors, queries):
        n = len(colors)
        left = [[-1] * 3 for _ in range(n)]
        right = [[n] * 3 for _ in range(n)]

        for i in range(n):
            if i > 0:
                left[i] = list(left[i - 1])
            left[i][colors[i] - 1] = i

        for i in range(n - 1, -1, -1):
            if i < n - 1:
                right[i] = list(right[i + 1])
            right[i][colors[i] - 1] = i

        res = []
        for i, c in queries:
            l, r = left[i][c - 1], right[i][c - 1]
            best = n
            if l != -1:
                best = min(best, i - l)
            if r != n:
                best = min(best, r - i)
            res.append(-1 if best == n else best)
        return res
