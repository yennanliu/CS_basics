"""

2158. Amount of New Area Painted Each Day
Hard
(premium / locked problem)

There is a long and thin painting that can be represented by a number line. You are given a 0-indexed 2D integer array paint of length n, where paint[i] = [start_i, end_i]. This means that on the ith day you need to paint the area between start_i and end_i.

Painting the same area multiple times will create an uneven painting so you only want to paint each area of the painting at most once.

Return an integer array worklog of length n, where worklog[i] is the amount of new area that you painted on the ith day.


Example 1:

Input: paint = [[1,4],[4,7],[5,8]]
Output: [3,3,1]
Explanation:
On day 0, paint everything between 1 and 4.
The amount of new area painted on day 0 is 4 - 1 = 3.
On day 1, paint everything between 4 and 7.
The amount of new area painted on day 1 is 7 - 4 = 3.
On day 2, paint everything between 7 and 8.
Everything between 5 and 7 was already painted on day 1.
The amount of new area painted on day 2 is 8 - 7 = 1.

Example 2:

Input: paint = [[1,4],[5,8],[4,7]]
Output: [3,3,1]
Explanation:
On day 0, paint everything between 1 and 4.
The amount of new area painted on day 0 is 4 - 1 = 3.
On day 1, paint everything between 5 and 8.
The amount of new area painted on day 1 is 8 - 5 = 3.
On day 2, paint everything between 4 and 5.
Everything between 5 and 7 was already painted on day 1.
The amount of new area painted on day 2 is 5 - 4 = 1.

Example 3:

Input: paint = [[1,5],[2,4]]
Output: [4,0]
Explanation:
On day 0, paint everything between 1 and 5.
The amount of new area painted on day 0 is 5 - 1 = 4.
On day 1, paint nothing because everything between 2 and 4 was already painted on day 0.
The amount of new area painted on day 1 is 0.


Constraints:

1 <= paint.length <= 10^5
paint[i].length == 2
0 <= start_i < end_i <= 5 * 10^4

"""

# V0
# IDEA : UNION-FIND AS A "JUMP TO THE NEXT UNPAINTED UNIT" POINTER
#
#   treat the line as unit cells [x, x+1). parent[x] answers "the first
#   unpainted cell at or after x" — initially parent[x] = x.
#
#   for a day covering [start, end) : hop with find(start); each landing that
#   is still < end is one fresh unit, and painting it points that cell at the
#   NEXT one (parent[x] = x + 1) so it is skipped forever after.
#
#   every cell is painted at most once across the whole run, so the total
#   work is O(RANGE + n) times the inverse-Ackermann of path compression —
#   far better than re-scanning the interval each day.
#
#   NOTE : find() is written iteratively; a recursive version would blow the
#          stack on a 5 * 10^4 long chain.
#
# time = O((n + RANGE) * alpha), space = O(RANGE)
class Solution(object):
    def amountPainted(self, paint):
        LIMIT = 50001                     # end_i <= 5 * 10^4
        parent = list(range(LIMIT + 1))

        def find(x):
            root = x
            while parent[root] != root:
                root = parent[root]
            while parent[x] != root:      # path compression
                parent[x], x = root, parent[x]
            return root

        res = []
        for start, end in paint:
            painted = 0
            x = find(start)
            while x < end:
                painted += 1
                parent[x] = x + 1
                x = find(x + 1)
            res.append(painted)
        return res
