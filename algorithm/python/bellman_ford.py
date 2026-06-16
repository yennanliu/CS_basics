#---------------------------------------------------------------
# BELLMAN-FORD (single-source shortest path)
#---------------------------------------------------------------
#
# Single-source shortest paths that, unlike Dijkstra, works with
# NEGATIVE edge weights and can detect negative-weight cycles.
# Relax all E edges V-1 times; one extra pass that still relaxes an
# edge proves a negative cycle is reachable.
#
# Time  : O(V * E)
# Space : O(V)
#
# References:
#   - https://www.geeksforgeeks.org/bellman-ford-algorithm-dp-23/


# V0 : edge list
# vertices : number of vertices, labelled 0..vertices-1
# edges    : list of (u, v, weight)
# Returns {node: distance}, or None if a negative cycle is reachable.
def bellman_ford(vertices, edges, src):
    dist = {v: float("inf") for v in range(vertices)}
    dist[src] = 0

    # relax all edges V - 1 times
    for _ in range(vertices - 1):
        updated = False
        for u, v, w in edges:
            if dist[u] != float("inf") and dist[u] + w < dist[v]:
                dist[v] = dist[u] + w
                updated = True
        if not updated:          # no change -> distances are final
            break

    # one more pass: any further relaxation means a negative cycle
    for u, v, w in edges:
        if dist[u] != float("inf") and dist[u] + w < dist[v]:
            return None

    return dist


if __name__ == "__main__":
    edges = [
        (0, 1, 4),
        (0, 2, 5),
        (1, 2, -3),
        (2, 3, 4),
    ]
    assert bellman_ford(4, edges, 0) == {0: 0, 1: 4, 2: 1, 3: 5}

    # 0 -> 1 -> 2 -> 0 forms a negative cycle
    neg_cycle = [(0, 1, 1), (1, 2, -1), (2, 0, -1)]
    assert bellman_ford(3, neg_cycle, 0) is None
    print("Success.")
