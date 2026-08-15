"""

3160. Find the Number of Distinct Colors Among the Balls
Medium

You are given an integer limit and a 2D array queries of size n x 2.

There are limit + 1 balls with distinct labels in the range [0, limit]. Initially, all balls are uncolored. For every query in queries that is of the form [x, y], you mark ball x with the color y. After each query, you need to find the number of distinct colors among the balls.

Return an array result of length n, where result[i] denotes the number of distinct colors after ith query.

Note that when answering a query, lack of a color will not be considered as a color.


Example 1:

Input: limit = 4, queries = [[1,4],[2,5],[1,3],[3,4]]
Output: [1,2,2,3]
Explanation:
After query 0, ball 1 has color 4.
After query 1, ball 1 has color 4, and ball 2 has color 5.
After query 2, ball 1 has color 3, and ball 2 has color 5.
After query 3, ball 1 has color 3, ball 2 has color 5, and ball 3 has color 4.

Example 2:

Input: limit = 4, queries = [[0,1],[1,2],[2,2],[3,4],[4,5]]
Output: [1,2,2,3,4]
Explanation:
After query 0, ball 0 has color 1.
After query 1, ball 0 has color 1, and ball 1 has color 2.
After query 2, ball 0 has color 1, and balls 1 and 2 have color 2.
After query 3, ball 0 has color 1, balls 1 and 2 have color 2, and ball 3 has color 4.
After query 4, ball 0 has color 1, balls 1 and 2 have color 2, ball 3 has color 4, and ball 4 has color 5.


Constraints:

1 <= limit <= 10^9
1 <= n == queries.length <= 10^5
queries[i].length == 2
0 <= queries[i][0] <= limit
1 <= queries[i][1] <= 10^9


"""

# V0
# IDEA : TWO MAPS — BALL -> ITS COLOR, AND COLOR -> HOW MANY BALLS WEAR IT
#
#   limit reaches 10^9, so the balls cannot be an array; but only the ones
#   actually painted matter, and there are at most n of those.
#
#   recolouring ball x is a decrement on its old color and an increment on
#   the new one. a color leaves the answer exactly when its counter hits 0,
#   so the number of distinct colors is just the size of the color map once
#   emptied entries are deleted.
#
# time = O(n), space = O(n)
class Solution(object):
    def queryResults(self, limit, queries):
        ball_color = {}
        color_count = {}
        res = []

        for x, y in queries:
            old = ball_color.get(x)
            if old is not None:
                color_count[old] -= 1
                if color_count[old] == 0:
                    del color_count[old]
            ball_color[x] = y
            color_count[y] = color_count.get(y, 0) + 1
            res.append(len(color_count))
        return res
