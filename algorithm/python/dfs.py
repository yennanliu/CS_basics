#---------------------------------------------------------------
# DFS (Depth-First Search)
#---------------------------------------------------------------
#
# Scope: DFS on a graph given as {vertex: [neighbours]}. See bfs.py
#        for the breadth-first counterpart, topological_sort.py for
#        DFS used to order a dependency graph, and
#        data_structure/python/binary_tree.py for the tree walks
#        (pre-order / in-order / post-order), which are DFS on a tree.
#
# Follow one path as far as it goes; only when it dead-ends, step back
# to the last vertex that still has an unexplored neighbour.
#
#         A                A -> B -> D            (dead end, back to B)
#        / \                      -> E -> F -> C  (dead end, all done)
#       B   C
#      / \   \             visiting order: A B D E F C
#     D   E - F
#
# The structure behind it is a STACK (last in, first out). Recursion
# gives you one for free -- the call stack -- which is why the
# recursive version is four lines. The iterative version manages the
# stack by hand, and is the one to use when the graph can be deeper
# than Python's recursion limit (about 1000 calls).
#
# DFS does NOT find shortest paths: it reaches F above through
# A-B-E-F (3 edges) although A-C-F (2 edges) exists. Use BFS for that.
# What DFS is good at is questions about STRUCTURE:
#     - is there any path from X to Y?
#     - how many separate pieces (connected components) are there?
#     - is there a cycle?
#     - in what order can dependencies be done? (topological sort)
#
# Time  : O(V + E)
# Space : O(V) -- the visited set, plus a stack as deep as the longest path
#
# Used by: LC 200 Number of Islands, LC 133 Clone Graph, LC 207 Course
#          Schedule, LC 417 Pacific Atlantic Water Flow.
#
# References:
#   - https://www.jianshu.com/p/66508acedd79


# V0 : recursion
def dfs_recursion(graph, start, visited=None, order=None):
    """Return the vertices reachable from `start`, in DFS order."""
    if visited is None:
        visited = set()
    if order is None:
        order = []
    visited.add(start)
    order.append(start)
    for neighbour in graph[start]:
        if neighbour not in visited:
            dfs_recursion(graph, neighbour, visited, order)
    return order


# V1 : iteration with an explicit stack
#
# Two details make it visit in the SAME order as the recursion:
#   - a vertex is marked visited when it is POPPED, not when pushed
#     (so a vertex reachable two ways is visited via the deeper route)
#   - neighbours are pushed in REVERSE, so the first neighbour is on
#     top of the stack and is explored first
def dfs_iteration(graph, start):
    """Same order as dfs_recursion, with no recursion."""
    visited = set()
    order = []
    stack = [start]
    while stack:
        node = stack.pop()
        if node in visited:          # it was pushed more than once; skip the repeats
            continue
        visited.add(node)
        order.append(node)
        for neighbour in reversed(graph[node]):
            if neighbour not in visited:
                stack.append(neighbour)
    return order


# V2 : is there a path? -- stop as soon as the target is seen
def has_path(graph, start, goal):
    """True if `goal` can be reached from `start`."""
    visited = set()
    stack = [start]
    while stack:
        node = stack.pop()
        if node == goal:
            return True
        if node in visited:
            continue
        visited.add(node)
        stack.extend(graph[node])
    return False


# V3 : connected components -- run a DFS from every vertex that no
# earlier DFS reached. Each run discovers exactly one component.
def connected_components(graph):
    """Return the groups of mutually reachable vertices (undirected graph)."""
    visited = set()
    components = []
    for vertex in graph:
        if vertex not in visited:
            component = dfs_recursion(graph, vertex, visited)
            components.append(component)
    return components


# V4 : cycle detection in a DIRECTED graph -- the three-colour DFS
#
#   WHITE  not visited yet
#   GREY   on the path we are currently walking (still in the call stack)
#   BLACK  completely finished: everything reachable from it is explored
#
# Meeting a GREY vertex means we walked back onto our own path: a
# cycle. Meeting a BLACK one is fine -- it is just a second route to
# something already cleared. A plain "visited" set cannot tell those
# two apart, which is why it gives false alarms on directed graphs.
WHITE, GREY, BLACK = 0, 1, 2


def has_cycle(graph):
    """True if the DIRECTED graph contains a cycle."""
    colour = {vertex: WHITE for vertex in graph}

    def visit(node):
        colour[node] = GREY
        for neighbour in graph[node]:
            if colour[neighbour] == GREY:        # back onto the current path
                return True
            if colour[neighbour] == WHITE and visit(neighbour):
                return True
        colour[node] = BLACK                     # done with this vertex for good
        return False

    for vertex in graph:
        if colour[vertex] == WHITE and visit(vertex):
            return True
    return False


if __name__ == "__main__":
    #       A
    #      / \
    #     B   C
    #    / \   \
    #   D   E - F
    graph = {
        "A": ["B", "C"],
        "B": ["A", "D", "E"],
        "C": ["A", "F"],
        "D": ["B"],
        "E": ["B", "F"],
        "F": ["C", "E"],
    }

    # deep first: C is reached LAST, through F, although it is next to A
    assert dfs_recursion(graph, "A") == ["A", "B", "D", "E", "F", "C"]
    assert dfs_iteration(graph, "A") == dfs_recursion(graph, "A")

    assert has_path(graph, "D", "C")
    graph["Z"] = []
    assert not has_path(graph, "A", "Z")

    # two islands plus a vertex on its own
    islands = {1: [2], 2: [1, 3], 3: [2], 4: [5], 5: [4], 6: []}
    assert connected_components(islands) == [[1, 2, 3], [4, 5], [6]]

    # directed: a -> b -> c -> a is a cycle
    assert has_cycle({"a": ["b"], "b": ["c"], "c": ["a"]})
    # a diamond is NOT a cycle: d is reached twice, but never while on the path
    assert not has_cycle({"a": ["b", "c"], "b": ["d"], "c": ["d"], "d": []})
    assert has_cycle({"a": ["a"]})               # a self-loop counts
    assert not has_cycle({})

    # a 5000-long chain: fine with an explicit stack, too deep for recursion
    chain = {i: [i + 1] for i in range(5000)}
    chain[5000] = []
    assert dfs_iteration(chain, 0) == list(range(5001))
    assert has_path(chain, 0, 5000)

    print("Success.")
