#---------------------------------------------------------------
# UNION FIND - CYCLE DETECTION (undirected graph)
#---------------------------------------------------------------
#
# Scope: one use of union-find -- does an UNDIRECTED graph contain a
#        cycle? See union_find.py for the structure itself, and
#        dfs.py (has_cycle) for DIRECTED graphs, where this method
#        does not work.
#
# Add the edges one at a time. Before adding edge u - v, ask whether
# u and v are ALREADY connected. If they are, there is already a path
# from u to v, and this edge closes it into a loop.
#
#     edges: 0-1, 1-2, 0-2
#
#     0-1   0 and 1 separate  -> union        groups: {0,1} {2}
#     1-2   1 and 2 separate  -> union        groups: {0,1,2}
#     0-2   0 and 2 TOGETHER  -> cycle!       (0 - 1 - 2 - 0)
#
# A graph with no cycle is a forest, and every edge of a forest joins
# two separate trees -- so "this edge joins nothing new" is exactly
# the definition of a cycle.
#
# Each undirected edge must be given ONCE. Listing both 0-1 and 1-0
# would report a cycle that is not there (the edge would "close" itself).
#
# Why not for directed graphs: a -> c and b -> c puts a, b, c in one
# group, but nothing loops. Union-find forgets direction.
#
# Time  : O(E * a(V)) -- effectively O(E)
# Space : O(V)
#
# Used by: LC 684 Redundant Connection (which edge closes the cycle?),
#          LC 261 Graph Valid Tree (no cycle + one component),
#          Kruskal's minimum spanning tree (skip edges that would
#          close a cycle).
#
# References:
#   - https://www.geeksforgeeks.org/union-find/


def find(parent, x):
    """Root of x's tree, with path compression."""
    root = x
    while parent[root] != root:
        root = parent[root]
    while x != root:
        next_x = parent[x]
        parent[x] = root
        x = next_x
    return root


# V0 : does any edge close a cycle?
def has_cycle(vertices, edges):
    """True if the undirected graph on 0..vertices-1 has a cycle."""
    return first_cycle_edge(vertices, edges) is not None


# V1 : WHICH edge closes it -- the LC 684 question
def first_cycle_edge(vertices, edges):
    """The first edge (u, v) whose endpoints were already connected, or None."""
    parent = list(range(vertices))
    for u, v in edges:
        root_u = find(parent, u)
        root_v = find(parent, v)
        if root_u == root_v:
            return (u, v)            # u and v were already joined by a path
        parent[root_u] = root_v      # otherwise this edge joins two trees
    return None


# V2 : the original class interface, kept for callers that build the
# graph edge by edge.
class Graph:
    def __init__(self, vertices):
        self.V = vertices
        self.edges = []

    def add_edge(self, u, v):
        """Add the undirected edge u - v. Add each edge once only."""
        self.edges.append((u, v))

    def is_cyclic(self):
        return has_cycle(self.V, self.edges)


if __name__ == "__main__":
    # the triangle from the header
    triangle = [(0, 1), (1, 2), (0, 2)]
    assert has_cycle(3, triangle)
    assert first_cycle_edge(3, triangle) == (0, 2)       # the edge that closes it

    # a path has no cycle, and neither does a star
    assert not has_cycle(3, [(0, 1), (1, 2)])
    assert not has_cycle(4, [(0, 1), (0, 2), (0, 3)])
    assert first_cycle_edge(3, [(0, 1), (1, 2)]) is None
    assert not has_cycle(3, [])

    # a self-loop is the smallest cycle; a repeated edge is the next smallest
    assert has_cycle(2, [(0, 0)])
    assert has_cycle(2, [(0, 1), (0, 1)])

    # a cycle in one component is found even with other components around
    assert has_cycle(6, [(0, 1), (3, 4), (4, 5), (5, 3)])

    g = Graph(3)
    g.add_edge(0, 1)
    g.add_edge(1, 2)
    assert not g.is_cyclic()
    g.add_edge(0, 2)
    assert g.is_cyclic()

    # LC 684's example: edges are 1-based there, so allow one extra vertex
    assert first_cycle_edge(4, [(1, 2), (1, 3), (2, 3)]) == (2, 3)

    # a tree on N vertices has N - 1 edges and no cycle; any extra edge makes one
    import random

    random.seed(1)
    for _ in range(100):
        n = random.randint(2, 12)
        tree = [(random.randrange(v), v) for v in range(1, n)]
        assert not has_cycle(n, tree)
        a = random.randrange(n)
        b = random.randrange(n)
        assert has_cycle(n, tree + [(a, b)])

    print("Success.")
