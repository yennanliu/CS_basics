#---------------------------------------------------------------
# MINIMUM SPANNING TREE (Kruskal and Prim)
#---------------------------------------------------------------
#
# Scope: connect every vertex of a weighted UNDIRECTED graph as
#        cheaply as possible. See union_find.py (Kruskal is built on
#        it), dijkstra.py (Prim has the same shape), and
#        doc/cheatsheet/minimum_spanning_tree.md for the patterns.
#
# A SPANNING TREE uses V - 1 edges to connect all V vertices with no
# cycle. The MINIMUM one has the smallest total weight.
#
#          1           4
#      A ------ B ---------- D
#      |      /              |
#    6 |   2 /             3 |
#      |    /                |
#      C --+                 E
#      |                     |
#      +----------7----------+
#
#   edges, cheapest first:  A-B 1,  B-C 2,  D-E 3,  B-D 4,  A-C 6,  C-E 7
#
#   The MST is the first four (total 1 + 2 + 3 + 4 = 10). A-C and C-E
#   are left out: each would join two vertices that are already
#   connected, which closes a cycle.
#
# Both algorithms are GREEDY, and both rest on the same fact (the "cut
# property"): split the vertices into any two groups -- the cheapest
# edge crossing between the groups is always safe to take.
#
#   KRUSKAL   Sort ALL the edges. Take them cheapest first, skipping
#             any edge whose two ends are already connected (it would
#             close a cycle). Union-find answers "already connected?".
#             Grows a FOREST that merges into one tree.
#
#   PRIM      Start from one vertex. Repeatedly take the cheapest edge
#             that leads from the tree built so far to a vertex OUTSIDE
#             it. A min-heap hands over that edge. Grows ONE tree.
#
#                 time              best for
#     Kruskal     O(E log E)        sparse graphs, edges given as a list
#     Prim        O(E log V)        dense graphs, adjacency list at hand
#
# Space : O(V + E)
#
# Prim vs Dijkstra: the loops are almost identical. Dijkstra orders the
# heap by distance FROM THE SOURCE (d + weight); Prim orders it by the
# weight of the ONE edge that attaches the vertex (weight alone).
#
# Used by: LC 1584 Min Cost to Connect All Points, LC 1135 Connecting
#          Cities With Minimum Cost, LC 1168 Optimize Water
#          Distribution in a Village.


import heapq


def find(parent, x):
    """Union-find: root of x's tree, with path compression."""
    root = x
    while parent[root] != root:
        root = parent[root]
    while x != root:
        next_x = parent[x]
        parent[x] = root
        x = next_x
    return root


# V0 : Kruskal -- sort the edges, add each one that joins two components
def kruskal(vertices, edges):
    """Minimum spanning tree of vertices 0..vertices-1.

    edges: list of (u, v, weight), undirected, each given once.
    Returns (total_weight, chosen_edges), or (None, []) if the graph is
    not connected (no spanning tree exists).
    """
    parent = list(range(vertices))
    total = 0
    chosen = []

    by_weight = sorted(edges, key=edge_weight)           # cheapest first
    for u, v, weight in by_weight:
        root_u = find(parent, u)
        root_v = find(parent, v)
        if root_u == root_v:
            continue                 # u and v are already connected: this edge is a cycle
        parent[root_u] = root_v      # join the two components
        total += weight
        chosen.append((u, v, weight))
        if len(chosen) == vertices - 1:
            break                    # a tree on V vertices has exactly V - 1 edges

    if len(chosen) != vertices - 1:
        return None, []
    return total, chosen


def edge_weight(edge):
    return edge[2]


# V1 : Prim -- grow one tree, always by the cheapest edge leaving it
def prim(vertices, edges, start=0):
    """Same result contract as kruskal (the chosen edges may differ on ties)."""
    if vertices == 0:
        return None, []

    adjacency = {v: [] for v in range(vertices)}
    for u, v, weight in edges:
        adjacency[u].append((weight, v))
        adjacency[v].append((weight, u))     # undirected: usable from both ends

    in_tree = {start}
    total = 0
    chosen = []

    # every edge leaving the tree, cheapest on top: (weight, from, to)
    heap = [(weight, start, neighbour) for weight, neighbour in adjacency[start]]
    heapq.heapify(heap)

    while heap and len(in_tree) < vertices:
        weight, u, v = heapq.heappop(heap)
        if v in in_tree:
            continue                 # stale: both ends are inside the tree by now
        in_tree.add(v)
        total += weight
        chosen.append((u, v, weight))
        for next_weight, neighbour in adjacency[v]:
            if neighbour not in in_tree:
                heapq.heappush(heap, (next_weight, v, neighbour))

    if len(in_tree) != vertices:
        return None, []
    return total, chosen


# LC 1584 : Min Cost to Connect All Points -- every pair of points is
# an edge whose weight is the Manhattan distance between them
def min_cost_connect_points(points):
    edges = []
    for i in range(len(points)):
        for j in range(i + 1, len(points)):
            distance = abs(points[i][0] - points[j][0]) + abs(points[i][1] - points[j][1])
            edges.append((i, j, distance))
    total, _ = kruskal(len(points), edges)
    return total


if __name__ == "__main__":
    # the graph from the header: A=0, B=1, C=2, D=3, E=4
    edges = [
        (0, 1, 1),       # A - B
        (0, 2, 6),       # A - C
        (1, 2, 2),       # B - C
        (1, 3, 4),       # B - D
        (2, 4, 7),       # C - E
        (3, 4, 3),       # D - E
    ]
    total, chosen = kruskal(5, edges)
    assert total == 10
    assert chosen == [(0, 1, 1), (1, 2, 2), (3, 4, 3), (1, 3, 4)]    # cheapest first
    assert len(chosen) == 5 - 1                          # a tree: V - 1 edges

    total, chosen = prim(5, edges)
    assert total == 10
    assert chosen == [(0, 1, 1), (1, 2, 2), (1, 3, 4), (3, 4, 3)]    # in growth order

    # the same total from any starting vertex
    assert all(prim(5, edges, start=s)[0] == 10 for s in range(5))

    # a disconnected graph has no spanning tree
    assert kruskal(4, [(0, 1, 1), (2, 3, 1)]) == (None, [])
    assert prim(4, [(0, 1, 1), (2, 3, 1)]) == (None, [])

    # a single vertex is already connected, at cost 0
    assert kruskal(1, []) == (0, []) and prim(1, []) == (0, [])

    # LC 1584's examples
    assert min_cost_connect_points([[0, 0], [2, 2], [3, 10], [5, 2], [7, 0]]) == 20
    assert min_cost_connect_points([[3, 12], [-2, 5], [-4, 1]]) == 18

    # Kruskal and Prim always agree on the total, and both beat (or tie)
    # every spanning tree found by brute force
    import itertools
    import random

    random.seed(1)
    for _ in range(100):
        n = random.randint(2, 6)
        graph = []
        for u in range(n):
            for v in range(u + 1, n):
                if random.random() < 0.7:
                    graph.append((u, v, random.randint(1, 9)))

        best = None
        for subset in itertools.combinations(graph, n - 1):
            parent = list(range(n))
            is_tree = True
            for u, v, _ in subset:
                root_u = find(parent, u)
                root_v = find(parent, v)
                if root_u == root_v:
                    is_tree = False
                    break
                parent[root_u] = root_v
            if is_tree:
                weight = sum(w for _, _, w in subset)
                if best is None or weight < best:
                    best = weight

        assert kruskal(n, graph)[0] == best
        assert prim(n, graph)[0] == best

    print("Success.")
