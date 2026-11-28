#---------------------------------------------------------------
# TOPOLOGICAL SORT
#---------------------------------------------------------------
#
# Put the vertices of a DIRECTED graph in a line so that every edge
# points forward: if there is an edge u -> v, u comes before v.
#
# Read an edge u -> v as "u must be done before v":
#
#     shirt ----> tie ------> jacket
#        \                      ^
#         +----> belt ----------+        (shirt before belt: it gets tucked in)
#                 ^
#     trousers ---+
#
#     one valid order:  shirt, tie, trousers, belt, jacket
#     another:          trousers, shirt, belt, tie, jacket
#
# There are usually SEVERAL valid orders, and there is NONE when the
# graph has a cycle (a before b before a is impossible). So every
# topological sort is also a cycle detector, and it only exists for a
# DAG -- a Directed Acyclic Graph.
#
# TWO ALGORITHMS, both O(V + E):
#
#   Kahn (BFS)   Repeatedly take a vertex that has NO incoming edge
#                left -- nothing is waiting in front of it -- and
#                remove it together with its outgoing edges. If
#                vertices are left over at the end, they sit on a cycle.
#
#   DFS          Finish a vertex only after everything it points to
#                is finished (post-order), then REVERSE that order.
#
# Time  : O(V + E)
# Space : O(V + E) for the graph, O(V) for the bookkeeping
#
# Used by: LC 207 Course Schedule (is there an order?), LC 210 Course
#          Schedule II (give me one), LC 269 Alien Dictionary, build
#          systems, spreadsheet recalculation, package installers.
#
# References:
#   - https://blog.techbridge.cc/2020/05/10/leetcode-topological-sort/
#   - https://www.geeksforgeeks.org/topological-sorting/


from collections import deque


def build_graph(vertices, edges):
    """Adjacency list for vertices 0..vertices-1 from [before, after] pairs."""
    graph = {v: [] for v in range(vertices)}
    for before, after in edges:
        graph[before].append(after)
    return graph


# V0 : Kahn's algorithm (BFS on in-degrees) -- the one to reach for
#
# in_degree[v] = how many edges point INTO v = how many prerequisites
# v is still waiting for.
#
#     edges: 0->1, 0->2, 1->3, 2->3          in-degree:  0:0  1:1  2:1  3:2
#     take 0   -> 1 and 2 lose an incoming edge          1:0  2:0  3:2
#     take 1   -> 3 loses one                            2:0  3:1
#     take 2   -> 3 loses one                            3:0
#     take 3                                 order: 0, 1, 2, 3
def topological_sort(vertices, edges):
    """Return one valid order of 0..vertices-1, or [] if there is a cycle.

    edges is a list of [before, after] pairs.
    """
    if vertices <= 0:
        return []

    graph = build_graph(vertices, edges)
    in_degree = {v: 0 for v in range(vertices)}
    for before, after in edges:
        in_degree[after] += 1

    # start with every vertex that waits for nothing
    ready = deque()
    for v in range(vertices):
        if in_degree[v] == 0:
            ready.append(v)

    order = []
    while ready:
        v = ready.popleft()
        order.append(v)
        for after in graph[v]:
            in_degree[after] -= 1            # v is done: one prerequisite fewer
            if in_degree[after] == 0:
                ready.append(after)

    # a vertex on a cycle never reaches in-degree 0, so it is never taken
    if len(order) != vertices:
        return []
    return order


# V1 : DFS, post-order reversed
#
# A vertex is appended only AFTER everything reachable from it, so it
# ends up BEHIND all of them; reversing the list puts it in front.
#
# The three colours detect a cycle on the way (see dfs.py): meeting a
# GREY vertex means the walk came back onto its own current path.
WHITE, GREY, BLACK = 0, 1, 2


def topological_sort_dfs(vertices, edges):
    """Return one valid order of 0..vertices-1, or [] if there is a cycle."""
    graph = build_graph(vertices, edges)
    colour = [WHITE] * vertices
    finished = []                    # vertices in the order they were completed

    def visit(v):
        """Explore everything after v. Returns False if a cycle was found."""
        colour[v] = GREY
        for after in graph[v]:
            if colour[after] == GREY:
                return False
            if colour[after] == WHITE and not visit(after):
                return False
        colour[v] = BLACK
        finished.append(v)           # AFTER the loop: this is the post-order
        return True

    for v in range(vertices):
        if colour[v] == WHITE and not visit(v):
            return []

    finished.reverse()
    return finished


# V2 : the original class-based DFS, kept for the GeeksforGeeks-style
# interface (g.add_edge(u, v), g.topological_sort()).
class Graph:
    def __init__(self, vertices):
        self.V = vertices
        self.edges = []

    def add_edge(self, u, v):
        """u must come before v."""
        self.edges.append([u, v])

    def topological_sort(self):
        """One valid order, or [] if the graph has a cycle."""
        return topological_sort_dfs(self.V, self.edges)


def is_valid_order(order, vertices, edges):
    """True if `order` lists every vertex once and respects every edge."""
    if sorted(order) != list(range(vertices)):
        return False
    position = {v: i for i, v in enumerate(order)}
    for before, after in edges:
        if position[before] >= position[after]:
            return False
    return True


if __name__ == "__main__":
    # the diamond from the header comment of V0
    diamond = [[0, 1], [0, 2], [1, 3], [2, 3]]
    assert topological_sort(4, diamond) == [0, 1, 2, 3]
    assert topological_sort_dfs(4, diamond) == [0, 2, 1, 3]     # also valid
    assert is_valid_order(topological_sort_dfs(4, diamond), 4, diamond)

    # getting dressed: 0 shirt, 1 tie, 2 jacket, 3 trousers, 4 belt
    dressing = [[0, 1], [1, 2], [0, 4], [3, 4], [4, 2]]
    for sort in (topological_sort, topological_sort_dfs):
        order = sort(5, dressing)
        assert is_valid_order(order, 5, dressing)
        assert order[-1] == 2                                   # the jacket goes on last

    g = Graph(4)
    g.add_edge(0, 1)
    g.add_edge(0, 2)
    g.add_edge(2, 3)
    g.add_edge(3, 1)
    assert g.topological_sort() == [0, 2, 3, 1]

    # a cycle has no valid order: 0 -> 1 -> 2 -> 0
    cycle = [[0, 1], [1, 2], [2, 0]]
    assert topological_sort(3, cycle) == []
    assert topological_sort_dfs(3, cycle) == []
    # ... even when only part of the graph is on it (3 hangs off the cycle)
    assert topological_sort(4, cycle + [[2, 3]]) == []
    assert topological_sort_dfs(4, cycle + [[2, 3]]) == []

    # no edges: any order is valid, and both return all the vertices
    assert is_valid_order(topological_sort(3, []), 3, [])
    assert topological_sort(0, []) == [] and topological_sort_dfs(0, []) == []

    # LC 207 / 210: prerequisites are [course, needed_first] -> edge needed_first -> course
    prerequisites = [[1, 0], [2, 0], [3, 1], [3, 2]]
    edges = [[needed, course] for course, needed in prerequisites]
    assert is_valid_order(topological_sort(4, edges), 4, edges)

    # random DAGs: only edges from a smaller to a larger number, then relabelled
    import random

    random.seed(1)
    for _ in range(200):
        n = random.randint(1, 8)
        labels = list(range(n))
        random.shuffle(labels)
        dag = []
        for a in range(n):
            for b in range(a + 1, n):
                if random.random() < 0.3:
                    dag.append([labels[a], labels[b]])
        assert is_valid_order(topological_sort(n, dag), n, dag)
        assert is_valid_order(topological_sort_dfs(n, dag), n, dag)

    print("Success.")
