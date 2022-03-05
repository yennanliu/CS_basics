"""

863. All Nodes Distance K in Binary Tree
Medium


Given the root of a binary tree, the value of a target node target, and an integer k, return an array of the values of all nodes that have a distance k from the target node.

You can return the answer in any order.

 

Example 1:


Input: root = [3,5,1,6,2,0,8,null,null,7,4], target = 5, k = 2
Output: [7,4,1]
Explanation: The nodes that are a distance 2 from the target node (with value 5) have values 7, 4, and 1.
Example 2:

Input: root = [1], target = 1, k = 3
Output: []
 

Constraints:

The number of nodes in the tree is in the range [1, 500].
0 <= Node.val <= 500
All the values Node.val are unique.
target is the value of one of the nodes in the tree.
0 <= k <= 1000

"""

# V0
# IDEA : DFS + BFS
# https://leetcode.com/problems/all-nodes-distance-k-in-binary-tree/discuss/604718/Python-BFS-solution
# DEMO
# root = TreeNode{val: 3, left: TreeNode{val: 5, left: TreeNode{val: 6, left: None, right: None}, right: TreeNode{val: 2, left: TreeNode{val: 7, left: None, right: None}, right: TreeNode{val: 4, left: None, right: None}}}, right: TreeNode{val: 1, left: TreeNode{val: 0, left: None, right: None}, right: TreeNode{val: 8, left: None, right: None}}}
# self.graph = defaultdict(<class 'list'>, {3: [5, 1], 5: [3, 6, 2], 6: [5], 2: [5, 7, 4], 7: [2], 4: [2], 1: [3, 0, 8], 0: [1], 8: [1]})
from collections import defaultdict
class Solution:
    
    def build(self,parent,child):
        if parent and child:
            self.graph[parent.val].append(child.val)
            self.graph[child.val].append(parent.val)
        if child.left:
            self.build(child,child.left)
        if child.right:
            self.build(child,child.right)
            
    def distanceK(self, root, target, K):
        self.graph=defaultdict(list)
        self.build(None,root)
        print ("root = " + str(root))
        print ("self.graph = " + str(self.graph))
        q=[(target.val,1)]
        vis=set([target.val])
        ans=[]
        while q:
            i,j=q.pop(0)
            for node in self.graph[i]:
                if node not in vis:
                    if j==K:
                        ans.append(node)
                    vis.add(node)
                    q.append((node,j+1))
        return ans if len(q) < K else [target.val]

# V0'
# IDEA : DFS + BFS
class Solution(object):
    def distanceK(self, root, target, K):
        # DFS
        def connect(parent, child):
            # build graph
            if parent and child:
                conn[parent.val].append(child.val)
                conn[child.val].append(parent.val)
            if child.left:
                connect(child, child.left)
            if child.right:
                connect(child, child.right)
        # init
        conn = collections.defaultdict(list)
        connect(None, root)
        print ("root = " + str(root))
        print ("conn = " + str(conn))
        # BFS
        #q = collections.deque()
        """
        NOTE THIS !!!
        """
        q = [target.val]
        #q.append(target.val)
        """
        NOTE THIS !!!
        """
        visited = set([target.val])
        for k in range(K):
            size = len(q)
            for i in range(size):
                node = q.pop(0)
                for j in conn[node]:
                    if j not in visited:
                        q.append(j)
                        visited.add(j)
        return list(q)

# V1
# IDEA : DFS + BFS
# https://leetcode.com/problems/all-nodes-distance-k-in-binary-tree/discuss/604718/Python-BFS-solution
from collections import defaultdict,deque
class Solution:
    def __init__(self):
        self.graph=defaultdict(list)
    
    def build(self,parent,child):
        if parent and child:
            self.graph[parent.val].append(child.val)
            self.graph[child.val].append(parent.val)
        if child.left:
            self.build(child,child.left)
        if child.right:
            self.build(child,child.right)
            
    def distanceK(self, root, target, K):
        self.build(None,root)
        q=deque()
        q.append((target.val,1))
        vis=set([target.val])
        ans=[]
        while q:
            i,j=q.popleft()
            for node in self.graph[i]:
                if node not in vis:
                    if j==K:
                        ans.append(node)
                    vis.add(node)
                    q.append((node,j+1))
        return ans if len(q) < K else [target.val]

# V1'
# IDEA :  Annotate Parent
# https://leetcode.com/problems/all-nodes-distance-k-in-binary-tree/solution/
class Solution(object):
    def distanceK(self, root, target, K):
        def dfs(node, par = None):
            if node:
                node.par = par
                dfs(node.left, node)
                dfs(node.right, node)

        dfs(root)

        queue = collections.deque([(target, 0)])
        seen = {target}
        while queue:
            if queue[0][1] == K:
                return [node.val for node, d in queue]
            node, d = queue.popleft()
            for nei in (node.left, node.right, node.par):
                if nei and nei not in seen:
                    seen.add(nei)
                    queue.append((nei, d+1))

        return []

# V1''
# IDEA : Percolate Distance
# https://leetcode.com/problems/all-nodes-distance-k-in-binary-tree/solution/
class Solution(object):
    def distanceK(self, root, target, K):
        ans = []

        # Return distance from node to target if exists, else -1
        # Vertex distance: the # of vertices on the path from node to target
        def dfs(node):
            if not node:
                return -1
            elif node is target:
                subtree_add(node, 0)
                return 1
            else:
                L, R = dfs(node.left), dfs(node.right)
                if L != -1:
                    if L == K: ans.append(node.val)
                    subtree_add(node.right, L + 1)
                    return L + 1
                elif R != -1:
                    if R == K: ans.append(node.val)
                    subtree_add(node.left, R + 1)
                    return R + 1
                else:
                    return -1

        # Add all nodes 'K - dist' from the node to answer.
        def subtree_add(node, dist):
            if not node:
                return
            elif dist == K:
                ans.append(node.val)
            else:
                subtree_add(node.left, dist + 1)
                subtree_add(node.right, dist + 1)

        dfs(root)
        return ans

# V1''' 
# https://blog.csdn.net/fuxuemingzhu/article/details/82709619
# Definition for a binary tree node.
# class TreeNode(object):
#     def __init__(self, x):
#         self.val = x
#         self.left = None
#         self.right = None
class Solution(object):
    def distanceK(self, root, target, K):
        """
        :type root: TreeNode
        :type target: TreeNode
        :type K: int
        :rtype: List[int]
        """
        # DFS
        conn = collections.defaultdict(list)
        def connect(parent, child):
            if parent and child:
                conn[parent.val].append(child.val)
                conn[child.val].append(parent.val)
            if child.left: connect(child, child.left)
            if child.right: connect(child, child.right)
        connect(None, root)
        # BFS
        que = collections.deque()
        que.append(target.val)
        visited = set([target.val])
        for k in range(K):
            size = len(que)
            for i in range(size):
                node = que.popleft()
                for j in conn[node]:
                    if j not in visited:
                        que.append(j)
                        visited.add(j)
        return list(que)

### Test case : dev 

# V1''''
# https://leetcode.com/problems/all-nodes-distance-k-in-binary-tree/discuss/1044354/easy-python
class Solution:
    def distanceK(self, root: TreeNode, target: TreeNode, K: int) -> List[int]:
        def dfs(node, dist):
            if not node:
                return
            
            cur_dist = dist
            if node.val in path:
                cur_dist = path[node.val]
                
            if cur_dist == K:
                res.append(node.val)
                
            dfs(node.left, cur_dist+1)
            dfs(node.right, cur_dist+1)
            return 
        
        def find_path(node, path):
            if not node:
                return -1
            
            if node.val == target.val:
                path[node.val] = 0
                return 0
            
            left = find_path(node.left, path)
            if left >= 0:
                path[node.val] = left+1
                return left+1
            
            right = find_path(node.right, path)
            if right >= 0:
                path[node.val] = right+1
                return right+1
            
            return -1
        
        res = []
        path = {}           
        find_path(root, path)
        dfs(root, path[root.val])
        return res

# V1''''''
# https://blog.csdn.net/fuxuemingzhu/article/details/82709619
# Definition for a binary tree node.
# class TreeNode(object):
#     def __init__(self, x):
#         self.val = x
#         self.left = None
#         self.right = None
class Solution(object):
    def distanceK(self, root, target, K):
        """
        :type root: TreeNode
        :type target: TreeNode
        :type K: int
        :rtype: List[int]
        """
        # DFS
        conn = collections.defaultdict(list)
        def connect(parent, child):
            if parent and child:
                conn[parent.val].append(child.val)
                conn[child.val].append(parent.val)
            if child.left: connect(child, child.left)
            if child.right: connect(child, child.right)
        connect(None, root)
        # BFS
        bfs = [target.val]
        visited = set([target.val])
        for k in range(K):
            bfs = [y for x in bfs for y in conn[x] if y not in visited]
            visited |= set(bfs)
        return bfs

# V1'''''''
# https://www.jiuzhang.com/solution/all-nodes-distance-k-in-binary-tree/#tag-highlight-lang-python
class Solution(object):
    def distanceK(self, root, target, K):
        def dfs(node, par = None):
            if node:
                node.par = par
                dfs(node.left, node)
                dfs(node.right, node)

        dfs(root)

        queue = collections.deque([(target, 0)])
        seen = {target}
        while queue:
            if queue[0][1] == K:
                return [node.val for node, d in queue]
            node, d = queue.popleft()
            for nei in (node.left, node.right, node.par):
                if nei and nei not in seen:
                    seen.add(nei)
                    queue.append((nei, d+1))
        return []

# V1''''''''''
# https://leetcode.com/problems/all-nodes-distance-k-in-binary-tree/solution/
# IDEA :  Annotate Parent
# Time Complexity: O(N)
# Space Complexity: O(N)
# PROCESS:
# 0. If we know the parent of every node x, we know all nodes that are distance 1 from x. We can then perform a breadth first search from the target node to find the answer.
# 1. We first do a depth first search where we annotate every node with information about it's parent.
# 2. After, we do a breadth first search to find all nodes a distance K from the target.
class Solution(object):
    def distanceK(self, root, target, K):
        def dfs(node, par = None):
            if node:
                node.par = par
                dfs(node.left, node)
                dfs(node.right, node)

        dfs(root)

        queue = collections.deque([(target, 0)])
        seen = {target}
        while queue:
            if queue[0][1] == K:
                return [node.val for node, d in queue]
            node, d = queue.popleft()
            for nei in (node.left, node.right, node.par):
                if nei and nei not in seen:
                    seen.add(nei)
                    queue.append((nei, d+1))

        return []

# V1''''''''''
# https://leetcode.com/problems/all-nodes-distance-k-in-binary-tree/solution/
# IDEA : Percolate Distance
# Time Complexity: O(N)
# Space Complexity: O(N)
class Solution(object):
    def distanceK(self, root, target, K):
        ans = []

        # Return distance from node to target if exists, else -1
        # Vertex distance: the # of vertices on the path from node to target
        def dfs(node):
            if not node:
                return -1
            elif node is target:
                subtree_add(node, 0)
                return 1
            else:
                L, R = dfs(node.left), dfs(node.right)
                if L != -1:
                    if L == K: ans.append(node.val)
                    subtree_add(node.right, L + 1)
                    return L + 1
                elif R != -1:
                    if R == K: ans.append(node.val)
                    subtree_add(node.left, R + 1)
                    return R + 1
                else:
                    return -1

        # Add all nodes 'K - dist' from the node to answer.
        def subtree_add(node, dist):
            if not node:
                return
            elif dist == K:
                ans.append(node.val)
            else:
                subtree_add(node.left, dist + 1)
                subtree_add(node.right, dist + 1)

        dfs(root)
        return ans

# V2 
# Time:  O(n)
# Space: O(n)
import collections
class Solution(object):
    def distanceK(self, root, target, K):
        """
        :type root: TreeNode
        :type target: TreeNode
        :type K: int
        :rtype: List[int]
        """
        def dfs(parent, child, neighbors):
            if not child:
                return
            if parent:
                neighbors[parent.val].append(child.val)
                neighbors[child.val].append(parent.val)
            dfs(child, child.left, neighbors)
            dfs(child, child.right, neighbors)

        neighbors = collections.defaultdict(list)
        dfs(None, root, neighbors)
        bfs = [target.val]
        lookup = set(bfs)
        for _ in xrange(K):
            bfs = [nei for node in bfs
                   for nei in neighbors[node]
                   if nei not in lookup]
            lookup |= set(bfs)
        return bfs