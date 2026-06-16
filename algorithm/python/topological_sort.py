#---------------------------------------------------------------
# TOPOLOGICAL SORT
#---------------------------------------------------------------
#
# Produce a linear ordering of the vertices of a Directed Acyclic
# Graph (DAG) such that for every edge u -> v, u comes before v.
#
# Time  : O(V + E)
# Space : O(V)
#
# References:
#   - https://blog.techbridge.cc/2020/05/10/leetcode-topological-sort/
#   - https://www.geeksforgeeks.org/topological-sorting/


from collections import defaultdict, deque


# V0 : DFS based
# After visiting all of a vertex's descendants, push it on a stack;
# the reversed stack is a valid topological order.
class Graph:
    def __init__(self, vertices):
        self.graph = defaultdict(list)
        self.V = vertices

    def add_edge(self, u, v):
        self.graph[u].append(v)

    def _topological_sort_util(self, v, visited, stack):
        visited[v] = True
        for nxt in self.graph[v]:
            if not visited[nxt]:
                self._topological_sort_util(nxt, visited, stack)
        # push AFTER recursing into all neighbors (post-order)
        stack.append(v)

    def topological_sort(self):
        visited = [False] * self.V
        stack = []
        for v in range(self.V):
            if not visited[v]:
                self._topological_sort_util(v, visited, stack)
        return stack[::-1]


# V1 : BFS based (Kahn's algorithm, using in-degrees)
def topological_sort(vertices, edges):
    if vertices <= 0:
        return []

    in_degree = {i: 0 for i in range(vertices)}
    graph = {i: [] for i in range(vertices)}
    for parent, child in edges:
        graph[parent].append(child)
        in_degree[child] += 1

    # start from every vertex that has no incoming edge
    sources = deque(v for v in in_degree if in_degree[v] == 0)

    order = []
    while sources:
        v = sources.popleft()
        order.append(v)
        for child in graph[v]:
            in_degree[child] -= 1
            if in_degree[child] == 0:
                sources.append(child)

    # if not all vertices are ordered, the graph has a cycle
    if len(order) != vertices:
        return []
    return order


if __name__ == "__main__":
    g = Graph(4)
    g.add_edge(0, 1)
    g.add_edge(0, 2)
    g.add_edge(2, 3)
    g.add_edge(3, 1)
    print("DFS  topological sort:", g.topological_sort())

    print("Kahn topological sort:",
          topological_sort(4, [[3, 2], [3, 0], [2, 0], [2, 1]]))
