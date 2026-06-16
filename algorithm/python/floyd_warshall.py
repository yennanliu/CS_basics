#---------------------------------------------------------------
# FLOYD-WARSHALL (all-pairs shortest path)
#---------------------------------------------------------------
#
# Compute the shortest distance between EVERY pair of vertices.
# Handles negative edge weights (but not negative cycles). The core
# idea: for each intermediate vertex k, relax every pair (i, j) via k:
#   dist[i][j] = min(dist[i][j], dist[i][k] + dist[k][j])
#
# Time  : O(V^3)
# Space : O(V^2)
#
# References:
#   - LC 2642. Design Graph With Shortest Path Calculator
#   - https://www.geeksforgeeks.org/floyd-warshall-algorithm-dp-16/


INF = float("inf")


# V0 : adjacency matrix
# graph[i][j] is the weight of edge i -> j, or INF if there is no edge.
# Returns a new matrix of shortest distances.
def floyd_warshall(graph):
    n = len(graph)
    dist = [row[:] for row in graph]      # copy so the input is untouched
    for i in range(n):
        dist[i][i] = 0

    for k in range(n):
        for i in range(n):
            for j in range(n):
                if dist[i][k] + dist[k][j] < dist[i][j]:
                    dist[i][j] = dist[i][k] + dist[k][j]
    return dist


if __name__ == "__main__":
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
    assert result[3][0] == INF
    print("Success.")
