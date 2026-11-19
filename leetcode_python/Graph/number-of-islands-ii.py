"""

305. Number of Islands II
Hard

You are given an empty 2D binary grid grid of size m x n. The grid represents a map
where 0's represent water and 1's represent land. Initially, all the cells of grid are
water cells (i.e., all the cells are 0's).

We may perform an add land operation which turns the water at position into a land.
You are given an array positions where positions[i] = [ri, ci] is the position
(ri, ci) at which we should operate the ith operation.

Return an array of integers answer where answer[i] is the number of islands after
turning the cell (ri, ci) into a land.

An island is surrounded by water and is formed by connecting adjacent lands
horizontally or vertically. You may assume all four edges of the grid are all
surrounded by water.

Example 1:

Input: m = 3, n = 3, positions = [[0,0],[0,1],[1,2],[2,1]]

Output: [1,1,2,3]

Explanation:

Initially, the 2d grid is filled with water.
- Operation #1: addLand(0, 0) turns the water at grid[0][0] into a land. We have 1 island.
- Operation #2: addLand(0, 1) turns the water at grid[0][1] into a land. We still have 1 island.
- Operation #3: addLand(1, 2) turns the water at grid[1][2] into a land. We have 2 islands.
- Operation #4: addLand(2, 1) turns the water at grid[2][1] into a land. We have 3 islands.

Example 2:

Input: m = 1, n = 1, positions = [[0,0]]

Output: [1]

Constraints:

1 <= m, n, positions.length <= 10^4

1 <= m * n <= 10^4

positions[i].length == 2

0 <= ri < m

0 <= ci < n

Follow up: Could you solve it in time complexity O(k log(mn)), where k == positions.length?

"""

# V0
# IDEA : UNION FIND (ONLINE / INCREMENTAL CONNECTIVITY)
#
#   Re-running LC 200's DFS after every add is O(k * m * n) = 10^8 -> too slow.
#   Union find keeps the islands as disjoint sets, so each add only touches
#   the new cell and its 4 neighbours:
#
#     1) the new land is its own island        -> island_count += 1
#     2) for each neighbour that is land:
#          if it sits in a DIFFERENT set, union -> island_count -= 1
#          (two islands just became one)
#
#   Water cells are marked parent = -1, so "is land?" is one lookup.
#   A position added twice must not count again -> answer stays the same.
#
#   e.g. m = 3, n = 3, [[0,0],[0,1],[1,2],[2,1]]
#        (0,0): +1           -> 1
#        (0,1): +1, merge (0,0) -1 -> 1
#        (1,2): +1, no land neighbour -> 2
#        (2,1): +1, no land neighbour -> 3
#
# time = O(m * n + k * a(m * n)), space = O(m * n)
class Solution(object):
    def numIslands2(self, m, n, positions):
        """
        :type m: int
        :type n: int
        :type positions: List[List[int]]
        :rtype: List[int]
        """
        WATER = -1
        DIRECTIONS = [(0, 1), (0, -1), (1, 0), (-1, 0)]

        # cell (r, c) is flattened to id r * n + c; -1 means still water
        parent = [WATER] * (m * n)
        rank = [0] * (m * n)

        def find(x):
            """Return the root of x's island, compressing the path on the way."""
            root = x
            while parent[root] != root:
                root = parent[root]
            # point every node on the path straight at the root
            while parent[x] != root:
                next_node = parent[x]
                parent[x] = root
                x = next_node
            return root

        def union(a, b):
            """Merge the islands of a and b; return True if they were separate."""
            root_a = find(a)
            root_b = find(b)
            if root_a == root_b:
                return False
            # attach the shorter tree under the taller one (union by rank)
            if rank[root_a] < rank[root_b]:
                parent[root_a] = root_b
            elif rank[root_a] > rank[root_b]:
                parent[root_b] = root_a
            else:
                parent[root_b] = root_a
                rank[root_a] += 1
            return True

        answer = []
        island_count = 0

        for r, c in positions:
            cell = r * n + c

            # NOTE !!! a repeated position is already land: do not count it again
            if parent[cell] != WATER:
                answer.append(island_count)
                continue

            # the new land starts as an island of its own
            parent[cell] = cell
            island_count += 1

            # merge with every adjacent land cell that is in a different island
            for dr, dc in DIRECTIONS:
                nr = r + dr
                nc = c + dc
                if 0 <= nr < m and 0 <= nc < n:
                    neighbour = nr * n + nc
                    if parent[neighbour] != WATER and union(cell, neighbour):
                        island_count -= 1

            answer.append(island_count)

        return answer
