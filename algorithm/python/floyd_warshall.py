#---------------------------------------------------------------
# FLOYD-WARSHALL (all-pairs shortest path)
#---------------------------------------------------------------
#
# Scope: the shortest distance between EVERY pair of vertices, in
#        three nested loops. See dijkstra.py / bellman_ford.py when
#        only one source is needed.
#
#            5          3          1
#       0 ------> 1 ------> 2 ------> 3
#       |                             ^
#       +-------------10--------------+
#
#       0 -> 3 directly costs 10, but through 1 and 2 it costs 5 + 3 + 1 = 9
#
# THE IDEA (dynamic programming on "which vertices may be used")
#   Start with dist[i][j] = the direct edge i -> j (infinity if none).
#   Then allow more and more vertices as STOPOVERS, one at a time:
#
#     for each stopover k:
#         for every pair (i, j):
#             is  i -> k -> j  shorter than the best  i -> j  so far?
#             dist[i][j] = min(dist[i][j], dist[i][k] + dist[k][j])
#
#   After the round for k, dist[i][j] is the shortest path that uses
#   only vertices 0..k in between. After the last round, any vertex is
#   allowed -- that is the true shortest path.
#
# THE ONE THING TO GET RIGHT: k is the OUTER loop. With k innermost,
# dist[i][k] and dist[k][j] are read before they have been improved,
# and some paths are missed.
#
# Negative edges are fine. A negative CYCLE is not (no shortest path
# exists); it shows up as a vertex whose distance to ITSELF is below 0.
#
# Time  : O(V^3) -- three loops over the vertices, nothing else
# Space : O(V^2) -- the distance matrix
#
# When to use it: the graph is small (V up to a few hundred) and you
# need many or all pairs. For one source, Dijkstra is far cheaper.
#
# Used by: LC 1334 Find the City With the Smallest Number of
#          Neighbors, LC 2642 Design Graph With Shortest Path
#          Calculator, LC 399 Evaluate Division, LC 1462 Course
#          Schedule IV (reachability: the same loops with `or` / `and`).
#
# References:
#   - https://www.geeksforgeeks.org/floyd-warshall-algorithm-dp-16/


INF = float("inf")


# V0 : adjacency matrix
def floyd_warshall(graph):
    """Shortest distance between every pair of vertices.

    graph[i][j] is the weight of the edge i -> j, or INF if there is none.
    Returns a NEW matrix; the input is left untouched.
    """
    n = len(graph)
    dist = [row[:] for row in graph]             # copy so the input is untouched
    for i in range(n):
        dist[i][i] = min(dist[i][i], 0)          # staying put costs nothing

    for k in range(n):                           # the stopover -- OUTER loop
        for i in range(n):
            for j in range(n):
                through_k = dist[i][k] + dist[k][j]
                if through_k < dist[i][j]:
                    dist[i][j] = through_k
    return dist


def has_negative_cycle(dist):
    """True if the matrix returned by floyd_warshall reveals a negative cycle.

    A vertex that can get back to itself for less than 0 sits on one.
    """
    for i in range(len(dist)):
        if dist[i][i] < 0:
            return True
    return False


# V1 : from an edge list -- the form the input usually arrives in
def floyd_warshall_from_edges(vertices, edges):
    """Same as floyd_warshall, for a list of directed (u, v, weight) edges."""
    graph = [[INF] * vertices for _ in range(vertices)]
    for u, v, weight in edges:
        graph[u][v] = min(graph[u][v], weight)   # keep the cheapest of parallel edges
    return floyd_warshall(graph)


if __name__ == "__main__":
    # the graph from the header
    g = [
        [0, 5, INF, 10],
        [INF, 0, 3, INF],
        [INF, INF, 0, 1],
        [INF, INF, INF, 0],
    ]
    result = floyd_warshall(g)
    # 0 -> 3 is cheaper via 0->1->2->3 (5+3+1=9) than the direct edge (10)
    assert result[0][3] == 9
    assert result[0][2] == 8
    assert result[3][0] == INF                   # edges are directed: no way back
    assert g[0][3] == 10                         # the input matrix was not modified

    assert floyd_warshall_from_edges(4, [(0, 1, 5), (1, 2, 3), (2, 3, 1), (0, 3, 10)]) == result

    # negative edges are fine: 0 -> 1 -> 2 = 4 - 3 = 1 beats the direct 5
    negative = floyd_warshall_from_edges(3, [(0, 1, 4), (0, 2, 5), (1, 2, -3)])
    assert negative[0][2] == 1
    assert not has_negative_cycle(negative)

    # a negative cycle (0 -> 1 -> 0 costs -1) is detectable afterwards
    looped = floyd_warshall_from_edges(2, [(0, 1, 1), (1, 0, -2)])
    assert has_negative_cycle(looped)

    assert floyd_warshall([]) == []
    assert floyd_warshall([[0]]) == [[0]]

    # every row must agree with a Dijkstra run from that vertex
    import random

    from dijkstra import dijkstra

    random.seed(1)
    for _ in range(50):
        n = random.randint(1, 7)
        edge_list = []
        adjacency = {v: [] for v in range(n)}
        for u in range(n):
            for v in range(n):
                if u != v and random.random() < 0.4:
                    weight = random.randint(0, 9)
                    edge_list.append((u, v, weight))
                    adjacency[u].append((v, weight))
        all_pairs = floyd_warshall_from_edges(n, edge_list)
        for source in range(n):
            from_source = dijkstra(adjacency, source)
            assert all_pairs[source] == [from_source[v] for v in range(n)]

    print("Success.")
