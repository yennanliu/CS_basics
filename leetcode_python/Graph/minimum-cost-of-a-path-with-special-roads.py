"""

2662. Minimum Cost of a Path With Special Roads
Medium

You are given an array start where start = [startX, startY] represents your initial position (startX, startY) in a 2D space. You are also given the array target where target = [targetX, targetY] represents your target position (targetX, targetY).

The cost of going from a position (x1, y1) to any other position in the space (x2, y2) is |x2 - x1| + |y2 - y1|.

There are also some special roads. You are given a 2D array specialRoads where specialRoads[i] = [x1i, y1i, x2i, y2i, costi] indicates that the ith special road goes in one direction from (x1i, y1i) to (x2i, y2i) with a cost equal to costi. You can use each special road any number of times.

Return the minimum cost required to go from (startX, startY) to (targetX, targetY).


Example 1:

Input: start = [1,1], target = [4,5], specialRoads = [[1,2,3,3,2],[3,4,4,5,1]]
Output: 5
Explanation:
1. (1,1) to (1,2) with a cost of |1 - 1| + |2 - 1| = 1.
2. (1,2) to (3,3). Use specialRoads[0] with the cost 2.
3. (3,3) to (3,4) with a cost of |3 - 3| + |4 - 3| = 1.
4. (3,4) to (4,5). Use specialRoads[1] with the cost 1.
So the total cost is 1 + 2 + 1 + 1 = 5.

Example 2:

Input: start = [3,2], target = [5,7], specialRoads = [[5,7,3,2,1],[3,2,3,4,4],[3,3,5,5,5],[3,4,5,6,6]]
Output: 7
Explanation:
It is optimal not to use any special edges and go directly from the starting to the ending position with a cost |5 - 3| + |7 - 2| = 7.
Note that the specialRoads[0] is directed from (5,7) to (3,2).

Example 3:

Input: start = [1,1], target = [10,4], specialRoads = [[4,2,1,1,3],[1,2,7,4,4],[10,3,6,1,2],[6,1,1,2,3]]
Output: 8
Explanation:
1. (1,1) to (1,2) with a cost of |1 - 1| + |2 - 1| = 1.
2. (1,2) to (7,4). Use specialRoads[1] with the cost 4.
3. (7,4) to (10,4) with a cost of |10 - 7| + |4 - 4| = 3.


Constraints:

start.length == target.length == 2
1 <= startX <= targetX <= 10^5
1 <= startY <= targetY <= 10^5
1 <= specialRoads.length <= 200
specialRoads[i].length == 5
startX <= x1i, x2i <= targetX
startY <= y1i, y2i <= targetY
1 <= costi <= 10^5

"""

# V0
# IDEA : DIJKSTRA ON A TINY GRAPH (start + the <=200 special-road EXITS)
#
#   the plane has 10^10 points, so we cannot run a shortest path over the
#   grid. The key observation: walking freely costs Manhattan distance, so
#   the only points where a route "changes character" are the exit points
#   (x2, y2) of the special roads (plus the start). Between any two of them
#   you just pay the Manhattan distance.
#
#   so build an implicit graph with at most 201 nodes:
#     node -1  = start
#     node i   = the EXIT point (x2i, y2i) of special road i
#   edge cost from node u (at point p) to node i:
#       dist(p, (x1i, y1i)) + costi
#   i.e. walk to the road's entrance, then ride the road.
#
#   the answer is min over all reached nodes of  best[node] + dist(node, target),
#   seeded with the plain walk dist(start, target).
#
#   NOTE : roads are ONE-WAY (example 2's road goes target -> start and is
#          useless), so only the entrance is used as a source point and only
#          the exit becomes a node.
#
#   NOTE : a special road may cost MORE than walking its own span. That is
#          handled automatically -- it just never beats the walking option
#          in the min.
#
#   NOTE : the same exit point can be relaxed several times, so keep the
#          usual "stale heap entry" skip (d > best[i] -> continue).
#
# time = O(n^2 * log n), space = O(n^2) worst case heap (n = #specialRoads)
import heapq


class Solution(object):
    def minimumCost(self, start, target, specialRoads):
        tx, ty = target[0], target[1]
        n = len(specialRoads)

        # plain walk, no special road at all
        res = abs(tx - start[0]) + abs(ty - start[1])

        INF = float('inf')
        best = [INF] * n
        heap = []
        for i in range(n):
            x1, y1, _, _, c = specialRoads[i]
            d = abs(start[0] - x1) + abs(start[1] - y1) + c
            if d < best[i]:
                best[i] = d
                heapq.heappush(heap, (d, i))

        while heap:
            d, i = heapq.heappop(heap)
            if d > best[i]:
                continue
            x, y = specialRoads[i][2], specialRoads[i][3]
            res = min(res, d + abs(tx - x) + abs(ty - y))
            for j in range(n):
                x1, y1, _, _, c = specialRoads[j]
                nd = d + abs(x - x1) + abs(y - y1) + c
                if nd < best[j]:
                    best[j] = nd
                    heapq.heappush(heap, (nd, j))
        return res
