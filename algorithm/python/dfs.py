#---------------------------------------------------------------
# DFS (Depth-First Search)
#---------------------------------------------------------------
#
# Explore a graph as deep as possible before backtracking. Good for
# checking reachability / whether something exists.
#
# Time  : O(V + E)
# Space : O(V)
#
# References:
#   - https://www.jianshu.com/p/66508acedd79


# V0 : recursion
def dfs_recursion(graph, s, visited=None, res=None):
    if visited is None:
        visited = set()
    if res is None:
        res = []
    res.append(s)
    visited.add(s)
    for u in graph[s]:
        if u not in visited:
            dfs_recursion(graph, u, visited, res)
    return res


# V1 : iteration (explicit stack)
def dfs_iteration(graph, s):
    stack = [s]
    visited = set()
    while stack:
        u = stack.pop()
        if u in visited:
            continue
        visited.add(u)
        stack.extend(graph[u])
        yield u


if __name__ == "__main__":
    graph = {
        "a": {"b", "f"},
        "b": {"c", "d", "f"},
        "c": {"d"},
        "d": {"e", "f"},
        "e": {"f"},
        "f": set(),
    }
    print("dfs_recursion :", dfs_recursion(graph, "a"))
    print("dfs_iteration :", list(dfs_iteration(graph, "a")))
