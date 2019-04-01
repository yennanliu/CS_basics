
#################################################################
# ALGORITHM DEMO : BFS (Breadth-First Search) 
#################################################################


# https://super9.space/archives/1562
# http://www.csie.ntnu.edu.tw/~u91029/Graph.html
# https://changyuheng.github.io/2014/implementations-of-dfs-and-bfs-in-python.html
# https://eddmann.com/posts/depth-first-search-and-breadth-first-search-in-python/

# BFS V1 
class BFS:
    """
    For BFS, use queue; For DFS, use stack or recursion
    """
    def __init__(self, start):
        self.queue = []
        self.start = start
 
    def traversal(self):
        self.start.visited = True
        self.queue.append(self.start)
 
        while self.queue:  # O(V)
 
            node = self.queue.pop(0)
            yield node
 
            for n in node.neighbors:  # O(E)
                if not n.visited:
                    n.visited = True
                    self.queue.append(n) 


# BFS V2 
def bfs(graph, queue, visited=None):
    if visited is None:
        visited = set()
    if not queue:
        return
    start = queue.pop(0)
    yield start
    visited.add(start)
    queue += [vertex for vertex in graph[start] - set(queue) - visited]
    yield from bfs(graph, queue, visited=visited)


def bfs_paths(graph, queue, goal):
    if not queue:
        return
    (start, path) = queue.pop(0)
    if start == goal:
        yield path
    queue += [(vertex, path + [vertex]) for vertex in graph[start] - set(path)]
    yield from bfs_paths(graph, queue, goal)


# graph = {
#     'A': set(['B', 'C']),
#     'B': set(['A', 'D', 'E']),
#     'C': set(['A', 'F']),
#     'D': set(['B']),
#     'E': set(['B', 'F']),
#     'F': set(['C', 'E']),
# }

# print(repr([vertex for vertex in bfs(graph, ['A'])]))
# print(repr([path for path in bfs_paths(graph, [('A', ['A'])], 'F')]))