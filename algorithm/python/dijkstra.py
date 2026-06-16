#---------------------------------------------------------------
#  DIJKSTRA'S SHORTEST PATH
#---------------------------------------------------------------
#
# Single-source shortest paths on a graph with NON-negative edge
# weights. Greedily settle the closest unvisited vertex on each step.
#
# Time  : O(V^2) (matrix, V0) ; O((V + E) log V) (heap, V1)
# Space : O(V)
#
# References:
#   - https://www.geeksforgeeks.org/dijkstras-shortest-path-algorithm-greedy-algo-7/


import heapq
import sys


# V0 : adjacency matrix, O(V^2)
class Graph:
    def __init__(self, vertices):
        self.V = vertices
        self.graph = [[0] * vertices for _ in range(vertices)]

    def _min_distance(self, dist, visited):
        # pick the unvisited vertex with the smallest tentative distance
        minimum = sys.maxsize
        min_index = -1
        for v in range(self.V):
            if not visited[v] and dist[v] < minimum:
                minimum = dist[v]
                min_index = v
        return min_index

    def dijkstra(self, src):
        dist = [sys.maxsize] * self.V
        dist[src] = 0
        visited = [False] * self.V

        for _ in range(self.V):
            u = self._min_distance(dist, visited)
            visited[u] = True
            for v in range(self.V):
                weight = self.graph[u][v]
                if weight > 0 and not visited[v] and dist[u] + weight < dist[v]:
                    dist[v] = dist[u] + weight
        return dist


# V1 : adjacency list + min-heap, O((V + E) log V)
def dijkstra(graph, src):
    """graph: {node: [(neighbor, weight), ...]}. Returns {node: distance}."""
    dist = {node: float("inf") for node in graph}
    dist[src] = 0
    heap = [(0, src)]
    while heap:
        d, u = heapq.heappop(heap)
        if d > dist[u]:                  # stale heap entry, skip
            continue
        for v, weight in graph[u]:
            if d + weight < dist[v]:
                dist[v] = d + weight
                heapq.heappush(heap, (dist[v], v))
    return dist


if __name__ == "__main__":
    g = Graph(9)
    g.graph = [
        [0, 4, 0, 0, 0, 0, 0, 8, 0],
        [4, 0, 8, 0, 0, 0, 0, 11, 0],
        [0, 8, 0, 7, 0, 4, 0, 0, 2],
        [0, 0, 7, 0, 9, 14, 0, 0, 0],
        [0, 0, 0, 9, 0, 10, 0, 0, 0],
        [0, 0, 4, 14, 10, 0, 2, 0, 0],
        [0, 0, 0, 0, 0, 2, 0, 1, 6],
        [8, 11, 0, 0, 0, 0, 1, 0, 7],
        [0, 0, 2, 0, 0, 0, 6, 7, 0],
    ]
    print("matrix :", g.dijkstra(0))

    adj = {
        0: [(1, 4), (7, 8)],
        1: [(0, 4), (2, 8), (7, 11)],
        2: [(1, 8), (3, 7), (5, 4), (8, 2)],
        3: [(2, 7), (4, 9), (5, 14)],
        4: [(3, 9), (5, 10)],
        5: [(2, 4), (3, 14), (4, 10), (6, 2)],
        6: [(5, 2), (7, 1), (8, 6)],
        7: [(0, 8), (1, 11), (6, 1), (8, 7)],
        8: [(2, 2), (6, 6), (7, 7)],
    }
    print("heap   :", dijkstra(adj, 0))
