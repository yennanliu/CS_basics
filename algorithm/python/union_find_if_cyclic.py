#---------------------------------------------------------------
# UNION FIND - CYCLE DETECTION (undirected graph)
#---------------------------------------------------------------
#
# Detect a cycle in an UNDIRECTED graph using union-find: for each edge
# (u, v), if u and v already share a root, adding the edge closes a
# cycle. (Assumes each undirected edge is stored only once.)
#
# Time  : ~O(V + E)
# Space : O(V)
#
# References:
#   - https://www.geeksforgeeks.org/union-find/


from collections import defaultdict


class Graph:
    def __init__(self, vertices):
        self.V = vertices
        self.graph = defaultdict(list)

    def add_edge(self, u, v):
        self.graph[u].append(v)

    def find_parent(self, parent, i):
        if parent[i] == -1:
            return i
        return self.find_parent(parent, parent[i])

    def union(self, parent, x, y):
        parent[self.find_parent(parent, x)] = self.find_parent(parent, y)

    def is_cyclic(self):
        parent = [-1] * self.V
        for u in self.graph:
            for v in self.graph[u]:
                x = self.find_parent(parent, u)
                y = self.find_parent(parent, v)
                if x == y:
                    return True
                self.union(parent, x, y)
        return False


if __name__ == "__main__":
    g = Graph(3)
    g.add_edge(0, 1)
    g.add_edge(1, 2)
    g.add_edge(0, 2)
    assert g.is_cyclic()

    g2 = Graph(3)
    g2.add_edge(0, 1)
    g2.add_edge(1, 2)
    assert not g2.is_cyclic()
    print("Success.")
