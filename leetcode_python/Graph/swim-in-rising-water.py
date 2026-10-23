"""

778. Swim in Rising Water
Hard

You are given an n x n integer matrix grid where each value grid[i][j]
represents the elevation at that point (i, j).

It starts raining, and water gradually rises over time. At time t, the water
level is t, meaning any cell with elevation less than or equal to t is
submerged or reachable.

You can swim from a square to another 4-directionally adjacent square if and
only if the elevation of both squares individually are at most t. You can swim
infinite distances in zero time. Of course, you must stay within the
boundaries of the grid during your swim.

Return the minimum time until you can reach the bottom right square
(n - 1, n - 1) if you start at the top left square (0, 0).

Example 1:

Input: grid = [[0,2],[1,3]]

Output: 3

Explanation:

At time 0, you are in grid location (0, 0).
You cannot go anywhere else because 4-directionally adjacent neighbors have a
higher elevation than t = 0.
You cannot reach point (1, 1) until time 3.
When the depth of water is 3, we can swim anywhere inside the grid.

Example 2:

Input: grid = [[0,1,2,3,4],[24,23,22,21,5],[12,13,14,15,16],[11,17,18,19,20],[10,9,8,7,6]]

Output: 16

Explanation: The final route is shown.
We need to wait until time 16 so that (0, 0) and (4, 4) are connected.

Constraints:

n == grid.length
n == grid[i].length
1 <= n <= 50
0 <= grid[i][j] < n^2
Each value grid[i][j] is unique.

"""

# V0
# IDEA : DIJKSTRA (MIN-HEAP), PATH COST = MAX ELEVATION ON THE PATH
#
#   The time a path needs is the HIGHEST cell on it (we must wait until the
#   water covers every cell we step on). So we want the path whose maximum
#   elevation is smallest -- a "minimax" shortest path.
#
#   Dijkstra still works because the cost never decreases along a path:
#   new_cost = max(cost, grid[nr][nc]) >= cost. So the first time the target
#   is popped from the min-heap, its cost is optimal.
#
#   e.g. grid = [[0,2],[1,3]]
#        pop (0,(0,0)) -> push (2,(0,1)), (1,(1,0))
#        pop (1,(1,0)) -> push (max(1,3)=3,(1,1))
#        pop (2,(0,1)) -> (1,1) already has cost 3, no improvement
#        pop (3,(1,1)) -> target -> 3
#
#   NOTE : same shape as LC 1631 Path With Minimum Effort -- there the "edge"
#          is the height diff, here it is the cell's elevation.
#
# time = O(n^2 log n), space = O(n^2)
import heapq


class Solution(object):
    def swimInWater(self, grid):
        """
        :type grid: List[List[int]]
        :rtype: int
        """
        n = len(grid)

        # best_time[r][c] = smallest known "max elevation" of a path (0,0) -> (r,c)
        best_time = [[float('inf')] * n for _ in range(n)]
        # NOTE !!! the start cell counts too: we cannot leave (0,0) before
        #          the water covers it
        best_time[0][0] = grid[0][0]

        # min-heap of (time needed to reach the cell, row, col)
        min_heap = [(grid[0][0], 0, 0)]

        directions = [(0, 1), (1, 0), (0, -1), (-1, 0)]

        while min_heap:
            time_needed, r, c = heapq.heappop(min_heap)

            # target popped first with the smallest cost -> optimal
            if r == n - 1 and c == n - 1:
                return time_needed

            # stale entry: a better path to (r, c) was already processed
            if time_needed > best_time[r][c]:
                continue

            for dr, dc in directions:
                nr = r + dr
                nc = c + dc
                if not (0 <= nr < n and 0 <= nc < n):
                    continue

                # the path's cost is its highest cell so far
                next_time = max(time_needed, grid[nr][nc])

                if next_time < best_time[nr][nc]:
                    best_time[nr][nc] = next_time
                    heapq.heappush(min_heap, (next_time, nr, nc))

        # unreachable for valid input: the grid is always connected
        return -1


# V1
# IDEA : BINARY SEARCH ON THE ANSWER + BFS
#
#   "Can we reach (n-1, n-1) by time t?" is monotonic: if it works at t it
#   also works at t + 1 (more cells are under water). So binary search the
#   smallest t in [grid[0][0], n*n - 1] for which a BFS that only enters
#   cells with elevation <= t reaches the target.
#
#   NOTE : a good interview follow-up -- the "search the answer" pattern --
#          but the extra log factor comes from the value range, not the heap.
#
# time = O(n^2 log n), space = O(n^2)
from collections import deque


class Solution2(object):
    def swimInWater(self, grid):
        """
        :type grid: List[List[int]]
        :rtype: int
        """
        n = len(grid)

        # the answer is at least the start cell (and the target cell),
        # and at most the largest possible elevation
        low = max(grid[0][0], grid[n - 1][n - 1])
        high = n * n - 1

        # find the smallest t with can_reach(t) == True
        while low < high:
            mid = (low + high) // 2
            if self.can_reach(grid, mid):
                high = mid
            else:
                low = mid + 1

        return low

    def can_reach(self, grid, t):
        """BFS from (0, 0) through cells with elevation <= t."""
        n = len(grid)
        if grid[0][0] > t:
            return False

        visited = [[False] * n for _ in range(n)]
        visited[0][0] = True
        queue = deque([(0, 0)])

        directions = [(0, 1), (1, 0), (0, -1), (-1, 0)]

        while queue:
            r, c = queue.popleft()
            if r == n - 1 and c == n - 1:
                return True

            for dr, dc in directions:
                nr = r + dr
                nc = c + dc
                if 0 <= nr < n and 0 <= nc < n and not visited[nr][nc] and grid[nr][nc] <= t:
                    visited[nr][nc] = True
                    queue.append((nr, nc))

        return False


# V2
# IDEA : UNION-FIND, FLOOD CELLS IN ORDER OF ELEVATION (KRUSKAL-LIKE)
#
#   Raise the water one level at a time. Elevations are unique and cover
#   0 .. n*n - 1, so at time t exactly one new cell goes under water: the
#   cell with elevation t. Union it with every already-flooded neighbour.
#   The first t at which (0,0) and (n-1,n-1) share a root is the answer.
#
# time = O(n^2 * a(n^2)), space = O(n^2)
class Solution3(object):
    def swimInWater(self, grid):
        """
        :type grid: List[List[int]]
        :rtype: int
        """
        n = len(grid)
        total = n * n

        # position_of[elevation] = (r, c); valid because elevations are unique
        position_of = [None] * total
        for r in range(n):
            for c in range(n):
                position_of[grid[r][c]] = (r, c)

        # each cell (r, c) is node r * n + c
        parent = list(range(total))
        rank = [0] * total

        start = 0
        target = total - 1
        directions = [(0, 1), (1, 0), (0, -1), (-1, 0)]

        for t in range(total):
            r, c = position_of[t]
            # (r, c) is now under water; join it to flooded neighbours
            for dr, dc in directions:
                nr = r + dr
                nc = c + dc
                if 0 <= nr < n and 0 <= nc < n and grid[nr][nc] <= t:
                    self.union(parent, rank, r * n + c, nr * n + nc)

            if self.find(parent, start) == self.find(parent, target):
                return t

        return -1

    def find(self, parent, x):
        """Root of x, with path halving."""
        while parent[x] != x:
            parent[x] = parent[parent[x]]
            x = parent[x]
        return x

    def union(self, parent, rank, a, b):
        """Merge the sets holding a and b, by rank."""
        root_a = self.find(parent, a)
        root_b = self.find(parent, b)
        if root_a == root_b:
            return
        if rank[root_a] < rank[root_b]:
            root_a, root_b = root_b, root_a
        parent[root_b] = root_a
        if rank[root_a] == rank[root_b]:
            rank[root_a] += 1
