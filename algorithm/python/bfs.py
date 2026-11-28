#---------------------------------------------------------------
# BFS (Breadth-First Search)
#---------------------------------------------------------------
#
# Scope: BFS on a graph given as {vertex: [neighbours]}. See dfs.py
#        for the depth-first counterpart, dijkstra.py for shortest
#        paths when the edges have WEIGHTS.
#
# Visit a graph in rings around the start: first the start, then
# everything 1 edge away, then everything 2 edges away, and so on.
#
#         A                level 0:  A
#        / \               level 1:  B  C
#       B   C              level 2:  D  E  F
#      / \   \
#     D   E - F            visiting order: A B C D E F
#
# The data structure that produces this order is a QUEUE (first in,
# first out): vertices are visited in the order they were discovered,
# so everything at distance d is handled before anything at d + 1.
#
# THAT ORDER IS THE POINT: the first time BFS reaches a vertex, it got
# there by a path with the FEWEST EDGES. So BFS answers "shortest path
# in an unweighted graph" -- fewest moves, fewest hops, fewest steps.
#
# THE ONE RULE: mark a vertex as visited when it is ADDED to the queue,
# not when it is taken out. Otherwise the same vertex is queued once
# per neighbour that sees it, and on a dense graph the queue explodes.
#
# Time  : O(V + E) -- every vertex is queued once, every edge looked at once
# Space : O(V) for the queue and the visited set
#
#     BFS                               DFS
#     queue                             stack / recursion
#     level by level                    one branch to the end, then back up
#     shortest path (unweighted)        does a path exist? cycles, ordering
#     memory: the widest level          memory: the longest path
#
# Used by: LC 102 Level Order Traversal, LC 200 Number of Islands,
#          LC 994 Rotting Oranges, LC 127 Word Ladder.
#
# References:
#   - https://eddmann.com/posts/depth-first-search-and-breadth-first-search-in-python/


from collections import deque

# What V2 records as "discovered from" for the START vertex, which was
# discovered from nowhere. A private object, not None, so it can never
# be mistaken for a real vertex (None is a legal dictionary key).
_NO_PREVIOUS = object()


# V0 : visiting order
def bfs(graph, start):
    """Return the vertices reachable from `start`, in BFS order."""
    visited = {start}
    order = []
    queue = deque([start])           # deque: popleft() is O(1); list.pop(0) is O(N)
    while queue:
        node = queue.popleft()
        order.append(node)
        for neighbour in graph[node]:
            if neighbour not in visited:
                visited.add(neighbour)       # mark when QUEUED (see the header)
                queue.append(neighbour)
    return order


# V1 : level by level -- the LC 102 pattern
#
# The trick is to read the queue's length BEFORE the inner loop: at
# that moment the queue holds exactly one whole level, so popping that
# many vertices consumes this level and queues the next one.
def bfs_levels(graph, start):
    """Return a list of levels: levels[d] = the vertices d edges from start."""
    visited = {start}
    levels = []
    queue = deque([start])
    while queue:
        level = []
        for _ in range(len(queue)):          # exactly the vertices of this level
            node = queue.popleft()
            level.append(node)
            for neighbour in graph[node]:
                if neighbour not in visited:
                    visited.add(neighbour)
                    queue.append(neighbour)
        levels.append(level)
    return levels


# V2 : shortest path (fewest edges)
#
# Remember, for every vertex, the vertex it was discovered FROM. When
# the goal is reached, walk those links backwards to get the path.
def bfs_shortest_path(graph, start, goal):
    """Return a shortest path from start to goal as a list, or None."""
    came_from = {start: _NO_PREVIOUS}    # doubles as the visited set
    queue = deque([start])
    while queue and goal not in came_from:
        node = queue.popleft()
        for neighbour in graph[node]:
            if neighbour not in came_from:
                came_from[neighbour] = node
                queue.append(neighbour)

    if goal not in came_from:        # the queue emptied without reaching it
        return None

    # the links run goal -> start, so collect them and reverse
    path = []
    node = goal
    while node is not _NO_PREVIOUS:
        path.append(node)
        node = came_from[node]
    path.reverse()
    return path


# V3 : BFS on a grid -- the same algorithm; the "graph" is implicit.
# A cell's neighbours are the four cells next to it, so there is no
# adjacency list to build. This is the shape most LeetCode BFS takes.
def bfs_grid_distance(grid, start, goal):
    """Fewest moves from start to goal on a grid, or -1 if unreachable.

    grid[r][c] == 1 is a wall; moves are up / down / left / right.
    start and goal are (row, col) pairs.
    """
    rows = len(grid)
    cols = len(grid[0])
    # a walk cannot begin or end inside a wall
    if grid[start[0]][start[1]] == 1 or grid[goal[0]][goal[1]] == 1:
        return -1
    visited = {start}
    queue = deque([(start, 0)])      # (cell, moves taken to get there)
    while queue:
        (row, col), moves = queue.popleft()
        if (row, col) == goal:
            return moves
        for d_row, d_col in ((-1, 0), (1, 0), (0, -1), (0, 1)):
            r = row + d_row
            c = col + d_col
            inside = 0 <= r < rows and 0 <= c < cols
            if inside and grid[r][c] == 0 and (r, c) not in visited:
                visited.add((r, c))
                queue.append(((r, c), moves + 1))
    return -1


if __name__ == "__main__":
    #       A
    #      / \
    #     B   C
    #    / \   \
    #   D   E - F
    graph = {
        "A": ["B", "C"],
        "B": ["A", "D", "E"],
        "C": ["A", "F"],
        "D": ["B"],
        "E": ["B", "F"],
        "F": ["C", "E"],
    }

    assert bfs(graph, "A") == ["A", "B", "C", "D", "E", "F"]
    assert bfs_levels(graph, "A") == [["A"], ["B", "C"], ["D", "E", "F"]]

    # two routes reach F: A-C-F (2 edges) and A-B-E-F (3). BFS finds the short one.
    assert bfs_shortest_path(graph, "A", "F") == ["A", "C", "F"]
    assert bfs_shortest_path(graph, "D", "F") == ["D", "B", "E", "F"]
    assert bfs_shortest_path(graph, "A", "A") == ["A"]

    # a vertex nothing leads to is simply never reached
    graph["Z"] = []
    assert "Z" not in bfs(graph, "A")
    assert bfs_shortest_path(graph, "A", "Z") is None

    # any hashable value can name a vertex, None included
    odd = {"a": [None], None: ["a", "b"], "b": [None]}
    assert bfs_shortest_path(odd, "a", "b") == ["a", None, "b"]
    assert bfs_shortest_path(odd, "a", None) == ["a", None]

    # a cycle (A-B-E-F-C-A) does not trap it: every vertex is queued once
    assert len(bfs(graph, "A")) == len(set(bfs(graph, "A")))

    #   S . . #
    #   # # . #
    #   . . . G        fewest moves from S to G: 5
    grid = [
        [0, 0, 0, 1],
        [1, 1, 0, 1],
        [0, 0, 0, 0],
    ]
    assert bfs_grid_distance(grid, (0, 0), (2, 3)) == 5
    assert bfs_grid_distance(grid, (0, 0), (0, 0)) == 0
    walled = [[0, 1, 0]]
    assert bfs_grid_distance(walled, (0, 0), (0, 2)) == -1
    # a wall is not a place to start from, or to arrive at
    assert bfs_grid_distance([[1, 0]], (0, 0), (0, 1)) == -1
    assert bfs_grid_distance([[0, 1]], (0, 0), (0, 1)) == -1
    assert bfs_grid_distance([[1]], (0, 0), (0, 0)) == -1

    print("Success.")
