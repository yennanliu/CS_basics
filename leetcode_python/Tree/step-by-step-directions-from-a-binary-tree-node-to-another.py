"""

2096. Step-By-Step Directions From a Binary Tree Node to Another
Medium

You are given the root of a binary tree with n nodes. Each node is uniquely assigned a value from 1 to n. You are also given an integer startValue representing the value of the start node s, and a different integer destValue representing the value of the destination node t.

Find the shortest path starting from node s and ending at node t. Generate step-by-step directions of such path as a string consisting of only the uppercase letters 'L', 'R', and 'U'. Each letter indicates a specific direction:

'L' means to go from a node to its left child node.
'R' means to go from a node to its right child node.
'U' means to go from a node to its parent node.

Return the step-by-step directions of the shortest path from node s to node t.


Example 1:

Input: root = [5,1,2,3,null,6,4], startValue = 3, destValue = 6
Output: "UURL"
Explanation: The shortest path is: 3 -> 1 -> 5 -> 2 -> 6.

Example 2:

Input: root = [2,1], startValue = 2, destValue = 1
Output: "L"
Explanation: The shortest path is: 2 -> 1.


Constraints:

The number of nodes in the tree is n.
2 <= n <= 10^5
1 <= Node.val <= n
All the values in the tree are unique.
1 <= startValue, destValue <= n
startValue != destValue

"""


# V0
# Definition for a binary tree node.
# class TreeNode(object):
#     def __init__(self, val=0, left=None, right=None):
#         self.val = val
#         self.left = left
#         self.right = right
class Solution(object):
    def getDirections(self, root, startValue, destValue):
        """
        :type root: Optional[TreeNode]
        :type startValue: int
        :type destValue: int
        :rtype: str
        """
        pass

# V0-1
# IDEA: DFS (build graph) + BFS (shortest path) (gpt)
from collections import defaultdict, deque


class Solution(object):
    def getDirections(self, root, startValue, destValue):
        """
        :type root: Optional[TreeNode]
        :type startValue: int
        :type destValue: int
        :rtype: str
        """
        # Edge cases
        if not root:
            return ""

        if startValue == destValue:
            return ""

        # Step 1: Build graph using DFS
        # graph[node] = [[neighbor, direction], ...]
        self.graph = defaultdict(list)
        self.start_node = None
        self.end_node = None

        self.dfs(root, None, startValue, destValue)

        if self.start_node is None or self.end_node is None:
            return ""

        # Step 2: BFS to find the path
        # Queue: (node, path)
        q = deque([(self.start_node, "")])
        visited = {self.start_node}

        while q:
            node, path = q.popleft()

            if node == self.end_node:
                return path

            for next_node, direction in self.graph[node]:
                if next_node not in visited:
                    visited.add(next_node)

                    # Append the direction to the current path
                    q.append((next_node, path + direction))

        return ""

    def dfs(self, node, parent, startValue, destValue):
        # Base case
        if not node:
            return

        # Add edge to parent: moving upward means "U"
        if parent:
            self.graph[node].append([parent, "U"])

        # Add edges to children
        if node.left:
            self.graph[node].append([node.left, "L"])

        if node.right:
            self.graph[node].append([node.right, "R"])

        # Record start and destination nodes
        if node.val == startValue:
            self.start_node = node

        if node.val == destValue:
            self.end_node = node

        self.dfs(node.left, node, startValue, destValue)
        self.dfs(node.right, node, startValue, destValue)


# V0-2
# IDEA: DFS (build graph) + BFS (shortest path) (gemini)
from collections import defaultdict, deque
import sys

# 提高遞迴深度限制以防測資為極端單側樹 (N 高達 10^5)
sys.setrecursionlimit(200000)


class Solution(object):

  def getDirections(self, root, startValue, destValue):
    """:type root: Optional[TreeNode]

    :type startValue: int
    :type destValue: int
    :rtype: str
    """
    if startValue == destValue:
      return ""
    if not root:
      return ""

    # Step 1: 建立圖結構 {node: [[neighbor_node, direction], ...]}
    self.graph = defaultdict(list)
    self.start_node = None
    self.end_node = None

    self.dfs(root, None, startValue, destValue)

    # Step 2: 利用 BFS 尋找從 start_node 到 end_node 的最短路徑
    q = deque([[self.start_node, ""]])
    visited = set()

    while q:
      node, path = q.popleft()

      if node == self.end_node:
        return path

      if node in visited:
        continue
      visited.add(node)

      for next_node, direction in self.graph[node]:
        if next_node not in visited:
          # 修正：必須將前一步的累積 path 加上當前方向
          q.append([next_node, path + direction])

    return ""

  def dfs(self, node, parent, startValue, destValue):
    if not node:
      return

    # 建立雙向邊與對應的方向
    if parent:
      self.graph[node].append([parent, "U"])
    if node.left:
      self.graph[node].append([node.left, "L"])
    if node.right:
      self.graph[node].append([node.right, "R"])

    if node.val == startValue:
      self.start_node = node
    if node.val == destValue:
      self.end_node = node

    self.dfs(node.left, node, startValue, destValue)
    self.dfs(node.right, node, startValue, destValue)


# V0-3
# IDEA: LCA (GPT)

class Solution(object):
    def getDirections(self, root, startValue, destValue):
        """
        :type root: Optional[TreeNode]
        :type startValue: int
        :type destValue: int
        :rtype: str
        """
        # Edge case
        if not root or startValue == destValue:
            return ""

        # Step 1: Find the LCA
        lca = self.get_lca(root, startValue, destValue)

        # Step 2: Find paths from LCA to start and destination
        path_s = []
        path_d = []

        self.get_path(lca, startValue, path_s)
        self.get_path(lca, destValue, path_d)

        # Step 3: Convert start path to upward directions
        path_s_rev = "U" * len(path_s)

        # Step 4: Combine both paths
        return path_s_rev + "".join(path_d)

    def get_lca(self, node, startValue, destValue):
        if not node:
            return None

        if node.val == startValue or node.val == destValue:
            return node

        left = self.get_lca(node.left, startValue, destValue)
        right = self.get_lca(node.right, startValue, destValue)

        if left and right:
            return node

        return left if left else right

    def get_path(self, node, target, path):
        """
        Find the path from node to target.
        Append L/R directions to path.
        """
        if not node:
            return False

        if node.val == target:
            return True

        # Try left subtree
        path.append("L")
        if self.get_path(node.left, target, path):
            return True
        path.pop()

        # Try right subtree
        path.append("R")
        if self.get_path(node.right, target, path):
            return True
        path.pop()

        return False

# V0-4
# IDEA : ROOT-TO-NODE PATHS + STRIP THE COMMON PREFIX (the LCA)
#
#   find the 'L'/'R' path from the root down to s and down to t. their common
#   prefix is exactly the path to their lowest common ancestor.
#
#   after dropping that prefix :
#     - every remaining step of the s-path becomes a 'U' (walk up to the LCA)
#     - the remaining t-path is appended unchanged (walk back down)
#
#   NOTE : the DFS builds the path with an explicit list and pops on the way
#          out, so it stays O(n) rather than concatenating strings.
#
# time = O(n), space = O(n)
# Definition for a binary tree node.
# class TreeNode(object):
#     def __init__(self, val=0, left=None, right=None):
#         self.val = val
#         self.left = left
#         self.right = right
class Solution(object):
    def getDirections(self, root, startValue, destValue):
        def find(node, target, path):
            if not node:
                return False
            if node.val == target:
                return True
            path.append('L')
            if find(node.left, target, path):
                return True
            path[-1] = 'R'
            if find(node.right, target, path):
                return True
            path.pop()
            return False

        p_start, p_dest = [], []
        find(root, startValue, p_start)
        find(root, destValue, p_dest)

        # drop the shared prefix (the path down to the LCA)
        i = 0
        while i < len(p_start) and i < len(p_dest) and p_start[i] == p_dest[i]:
            i += 1

        return 'U' * (len(p_start) - i) + ''.join(p_dest[i:])
