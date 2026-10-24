"""

4085. Find Root of a Colored Tree
Medium

You are given an undirected tree of n nodes labeled from 0 to n - 1,
represented by a 2D integer array edges of length n - 1, where
edges[i] = [ui, vi] indicates that there is an undirected edge between
nodes ui and vi.

You are also given a string colors of length n, where colors[i] is one of
'R', 'B', or 'W', and denotes the color of node i.

A node chosen as root must satisfy both of the following conditions once
the tree is oriented away from it:

Binary-tree property: Every node has at most two children.

Layered color pattern: All nodes at the same depth have the same color.
Starting at depth 0, these colors follow one of the three repeating
sequences:

    R -> B -> W -> R -> B -> W -> ...
    B -> W -> R -> B -> W -> R -> ...
    W -> R -> B -> W -> R -> B -> ...

Return the index of any root that satisfies both conditions, or -1 if no
such root exists.

A tree is a connected undirected graph with no cycles.

The depth of a node is the number of edges on the path from the root to
that node. The root has depth 0. A child of a node is an adjacent node
whose depth is one greater.

The degree of a node is the number of edges incident to it.


Example 1:

Input: edges = [[3,1],[3,2],[1,4],[2,0]], colors = "BRRWB"

Output: 3

Explanation:

Rooting at node 3 gives each node at most two children and every edge
advances to the next color in the cycle (W -> R -> B), so return 3.

Example 2:

Input: edges = [[0,1],[1,2],[2,3]], colors = "RBRB"

Output: -1

Explanation:

No root satisfies the required color step on every edge, so return -1.


Constraints:

2 <= n <= 10^5

edges.length == n - 1

edges[i] = [ui, vi]

0 <= ui, vi < n

colors.length == n

colors[i] is one of 'R', 'B', 'W'.

The input graph is a tree and every node has degree at most three.

"""

# V0
class Solution(object):
    def findColoredRoot(self, edges, colors):
        """
        :type edges: List[List[int]]
        :type colors: str
        :rtype: int
        """
        pass


# V0-1
# IDEA: HASHMAP + GRPAH + COLOR CYCLE (gpt)
"""
CORE IDEA:
    
    -> 利用顏色差值確定邊方向（O(N) 演算法）
"""
class Solution(object):
    def findColoredRoot(self, edges, colors):
        """
        :type edges: List[List[int]]
        :type colors: str
        :rtype: int
        """
        n = len(colors)

        # Build undirected graph
        graph = [[] for _ in range(n)]

        for u, v in edges:
            graph[u].append(v)
            graph[v].append(u)

        # Color cycle: R -> B -> W -> R
        color_map = {
            "R": 0,
            "B": 1,
            "W": 2
        }

        # Each node can have at most 1 parent and 2 children.
        parent = [-1] * n
        children_count = [0] * n

        for u, v in edges:
            color_u = color_map[colors[u]]
            color_v = color_map[colors[v]]

            # Same-colored adjacent nodes cannot be valid.
            if color_u == color_v:
                return -1

            # Determine the parent-child direction.
            # If u's color advances to v's color, u is parent.
            if (color_u + 1) % 3 == color_v:
                p, child = u, v
            else:
                p, child = v, u

            # A node cannot have multiple parents.
            if parent[child] != -1:
                return -1

            parent[child] = p
            children_count[p] += 1

            # Binary-tree property: at most 2 children.
            if children_count[p] > 2:
                return -1

        # The root is the only node without a parent.
        root = -1

        for node in range(n):
            if parent[node] == -1:
                if root != -1:
                    return -1
                root = node

        return root


# V0-2
# IDEA: HASHMAP + GRPAH + COLOR CYCLE (gemini)
"""
CORE IDEA:
    
    -> 利用顏色差值確定邊方向 (O(N) 演算法）
"""
class Solution(object):

  def findColoredRoot(self, edges, colors):
    """:type edges: List[List[int]]

    :type colors: str
    :rtype: int
    """
    n = len(colors)
    color_map = {"W": 0, "R": 1, "B": 2}

    # 1. 建立無向圖
    graph = [[] for _ in range(n)]
    for u, v in edges:
      graph[u].append(v)
      graph[v].append(u)

    # 2. 檢查是否有節點度數超過 3（因為根節點最多 2 個子節點，其餘節點最多 1 個父節點 + 2 個子節點 = 3）
    for u in range(n):
      if len(graph[u]) > 3:
        return -1

    root_candidate = -1

    # 3. 遍歷每個節點，檢查其父節點數量
    for u in range(n):
      u_color = color_map[colors[u]]
      parent_count = 0

      for v in graph[u]:
        v_color = color_map[colors[v]]

        # 父節點的顏色必須是 (u_color + 2) % 3
        if v_color == (u_color + 2) % 3:
          parent_count += 1
        elif v_color == (u_color + 1) % 3:
          # v 是 u 的子節點，合法，不增加 parent_count
          pass
        else:
          # 相鄰節點顏色相同或不符合循環順序，無效
          return -1

      # 一個普通節點最多只能有一個父節點
      if parent_count > 1:
        return -1

      # 根節點沒有父節點，且子節點數（即其度數）最多為 2
      if parent_count == 0:
        if len(graph[u]) > 2:
          return -1
        root_candidate = u

    return root_candidate


# V0-3
# IDEA : EACH EDGE'S COLOR STEP FIXES ITS DIRECTION -> ROOT = THE ONLY NODE WITH NO PARENT (CLAUDE)
#
#   all 3 allowed sequences are the SAME cycle R -> B -> W -> R, just started
#   at a different color. so "same depth, same color" is equivalent to :
#
#     for every edge parent -> child : color[child] == NEXT[color[parent]]
#
#   for an edge {u, v} with different colors exactly one of
#   NEXT[u] == v / NEXT[v] == u holds (3 colors in a cycle), so the edge's
#   direction is FORCED; equal colors -> no root can work -> -1.
#
#   after orienting all n - 1 edges :
#     - a node with 2+ parents      -> impossible -> -1
#     - otherwise exactly ONE node has no parent  -> it is the root
#       (n - 1 edges, every other node has exactly 1 parent)
#     - binary-tree property = every node has at most 2 outgoing edges
#
#   e.g. "BRRWB", edge 3-1 : W -> R is a step, so 3 is the parent of 1
#
#   (brute force - try every node as root and DFS-check it - is O(n^2) and
#    times out at n = 10^5; this is the same check done once per edge.)
#
# time = O(n), space = O(n)
class Solution(object):
    def findColoredRoot(self, edges, colors):
        """
        :type edges: List[List[int]]
        :type colors: str
        :rtype: int
        """
        n = len(colors)

        # edge case: a single node is trivially a valid root
        if n == 1:
            return 0

        # the color a child must have, given its parent's color
        next_color = {"R": "B", "B": "W", "W": "R"}

        parent_count = [0] * n
        child_count = [0] * n

        for u, v in edges:
            if next_color[colors[u]] == colors[v]:
                parent, child = u, v
            elif next_color[colors[v]] == colors[u]:
                parent, child = v, u
            else:
                # NOTE !!! equal colors on an edge: whichever end is the
                #          parent, the child's color would not advance
                return -1

            parent_count[child] += 1
            child_count[parent] += 1

        root = -1
        for node in range(n):
            # a node reached from two parents -> no orientation works
            if parent_count[node] > 1:
                return -1
            # binary-tree property
            if child_count[node] > 2:
                return -1
            if parent_count[node] == 0:
                root = node

        return root
