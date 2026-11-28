#---------------------------------------------------------------
# BELLMAN-FORD (single-source shortest path)
#---------------------------------------------------------------
#
# Scope: shortest paths from ONE source when edge weights MAY BE
#        NEGATIVE, plus negative-cycle detection. See dijkstra.py for
#        the faster algorithm when all weights are >= 0, and
#        floyd_warshall.py for every pair of vertices.
#
#           4
#       0 -----> 1
#       |        |               shortest 0 -> 2 is NOT the direct edge (5):
#     5 |        | -3              0 -> 1 -> 2 = 4 + (-3) = 1
#       v        v
#       2 <------+               Dijkstra would finalise 2 at distance 5
#       |                        before ever looking at the -3 edge.
#     4 |
#       v
#       3
#
# THE IDEA
#   RELAX an edge u -> v: "if going to u and then along this edge is
#   shorter than what v has, update v".
#
#   Now relax EVERY edge, and repeat that whole sweep V - 1 times.
#
# WHY V - 1 SWEEPS ARE ENOUGH
#   A shortest path never repeats a vertex, so it has at most V - 1
#   edges. Sweep 1 gets every 1-edge shortest path right, sweep 2
#   every 2-edge one, ... sweep V - 1 gets them all -- whatever order
#   the edges are listed in.
#
# NEGATIVE CYCLES
#   If a sweep AFTER those V - 1 still improves something, a path with
#   V or more edges is shorter than every simple path. That is only
#   possible by going round a cycle whose total weight is negative --
#   and then "shortest" has no meaning, because another lap is always
#   cheaper.
#
# Time  : O(V * E) -- V - 1 sweeps over E edges
# Space : O(V)
#
#     algorithm        negative edges   time
#     ---------------  ---------------  -----------------
#     BFS              (no weights)     O(V + E)
#     Dijkstra         no               O((V + E) log V)
#     Bellman-Ford     yes              O(V * E)
#
# Used by: LC 787 Cheapest Flights Within K Stops (limit the sweeps to
#          K + 1), LC 743 Network Delay Time, currency-arbitrage
#          detection (a negative cycle in -log(rate)).
#
# References:
#   - https://www.geeksforgeeks.org/bellman-ford-algorithm-dp-23/


INF = float("inf")


# V0 : edge list
def bellman_ford(vertices, edges, src):
    """Shortest distance from `src` to every vertex.

    vertices : number of vertices, labelled 0..vertices-1
    edges    : list of (u, v, weight), each a DIRECTED edge u -> v
    Returns {node: distance} (infinity when unreachable), or None if a
    negative cycle can be reached from src.
    """
    dist = {v: INF for v in range(vertices)}
    dist[src] = 0

    # relax all edges V - 1 times
    for _ in range(vertices - 1):
        updated = False
        for u, v, weight in edges:
            # `dist[u] != INF`: an unreachable u has nothing to offer.
            # (inf + a negative weight is still inf in Python, but the
            # check keeps the intent explicit and ports to integer code.)
            if dist[u] != INF and dist[u] + weight < dist[v]:
                dist[v] = dist[u] + weight
                updated = True
        if not updated:              # a whole sweep changed nothing: already final
            break

    # one more sweep: anything that still improves is on a negative cycle
    for u, v, weight in edges:
        if dist[u] != INF and dist[u] + weight < dist[v]:
            return None

    return dist


def count_sweeps(vertices, edges, src):
    """How many sweeps change at least one distance (for the demo below)."""
    dist = {v: INF for v in range(vertices)}
    dist[src] = 0
    sweeps = 0
    for _ in range(vertices - 1):
        updated = False
        for u, v, weight in edges:
            if dist[u] != INF and dist[u] + weight < dist[v]:
                dist[v] = dist[u] + weight
                updated = True
        if not updated:
            break
        sweeps += 1
    return sweeps


if __name__ == "__main__":
    # the graph from the header
    edges = [
        (0, 1, 4),
        (0, 2, 5),
        (1, 2, -3),
        (2, 3, 4),
    ]
    assert bellman_ford(4, edges, 0) == {0: 0, 1: 4, 2: 1, 3: 5}

    # edges are directed: nothing leads back to 0 from 1
    assert bellman_ford(4, edges, 1) == {0: INF, 1: 0, 2: -3, 3: 1}

    # 0 -> 1 -> 2 -> 0 has total weight 1 - 1 - 1 = -1: a negative cycle
    neg_cycle = [(0, 1, 1), (1, 2, -1), (2, 0, -1)]
    assert bellman_ford(3, neg_cycle, 0) is None

    # a negative cycle that src cannot reach does not affect the answer
    far_cycle = [(0, 1, 2), (2, 3, -5), (3, 2, 1)]
    assert bellman_ford(4, far_cycle, 0) == {0: 0, 1: 2, 2: INF, 3: INF}

    # a cycle with total weight 0 or more is harmless
    assert bellman_ford(2, [(0, 1, -2), (1, 0, 2)], 0) == {0: 0, 1: -2}

    # why V - 1 sweeps: a chain 0 -> 1 -> 2 -> 3 -> 4 listed BACKWARDS
    # moves the answer forward only one edge per sweep ...
    chain = [(3, 4, 1), (2, 3, 1), (1, 2, 1), (0, 1, 1)]
    assert count_sweeps(5, chain, 0) == 4
    assert bellman_ford(5, chain, 0) == {0: 0, 1: 1, 2: 2, 3: 3, 4: 4}
    # ... and listed forwards, one sweep does everything
    assert count_sweeps(5, chain[::-1], 0) == 1

    # with non-negative weights it must agree with Dijkstra
    import random

    from dijkstra import dijkstra

    random.seed(1)
    for _ in range(100):
        n = random.randint(1, 8)
        edge_list = []
        adjacency = {v: [] for v in range(n)}
        for u in range(n):
            for v in range(n):
                if u != v and random.random() < 0.4:
                    weight = random.randint(0, 9)
                    edge_list.append((u, v, weight))
                    adjacency[u].append((v, weight))
        assert bellman_ford(n, edge_list, 0) == dijkstra(adjacency, 0)

    print("Success.")
