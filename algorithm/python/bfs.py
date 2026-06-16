#---------------------------------------------------------------
# BFS (Breadth-First Search)
#---------------------------------------------------------------
#
# Explore a graph level by level using a queue. Finds the shortest
# path (fewest edges) in an unweighted graph.
#
# Time  : O(V + E)
# Space : O(V)
#
# References:
#   - https://eddmann.com/posts/depth-first-search-and-breadth-first-search-in-python/


# V0 : class based (graph of Node objects with .neighbors / .visited)
class BFS:
    """For BFS use a queue; for DFS use a stack or recursion."""

    def __init__(self, start):
        self.queue = []
        self.start = start

    def traversal(self):
        self.start.visited = True
        self.queue.append(self.start)
        while self.queue:                 # O(V)
            node = self.queue.pop(0)
            yield node
            for n in node.neighbors:      # O(E)
                if not n.visited:
                    n.visited = True
                    self.queue.append(n)


# V1 : functional (graph as {vertex: set(neighbors)})
def bfs(graph, queue, visited=None):
    if visited is None:
        visited = set()
    if not queue:
        return
    start = queue.pop(0)
    yield start
    visited.add(start)
    queue += [v for v in graph[start] - set(queue) - visited]
    yield from bfs(graph, queue, visited=visited)


def bfs_paths(graph, queue, goal):
    """Yield every path from the start vertex to goal."""
    if not queue:
        return
    start, path = queue.pop(0)
    if start == goal:
        yield path
    queue += [(v, path + [v]) for v in graph[start] - set(path)]
    yield from bfs_paths(graph, queue, goal)


if __name__ == "__main__":
    graph = {
        "A": {"B", "C"},
        "B": {"A", "D", "E"},
        "C": {"A", "F"},
        "D": {"B"},
        "E": {"B", "F"},
        "F": {"C", "E"},
    }
    print("bfs       :", list(bfs(graph, ["A"])))
    print("bfs_paths :", list(bfs_paths(graph, [("A", ["A"])], "F")))
