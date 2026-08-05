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
# IDEA: DFS + BFS (GPT)
"""
CORE IDEA:


-> Use BFS to radiate outward from the target node


1. build the graph: {node: parent}
2. use BFS, init q as [target, 0]    (node, dist)
3. run BFS, collect nodes when dist == k
"""
from collections import defaultdict, deque

class Solution(object):
    def distanceK(self, root, target, k):
        self.graph = defaultdict(list)
        self.build_graph(root)

        res = []
        visited = {target}
        q = deque([(target, 0)])

        while q:
            node, dist = q.popleft()

            if dist == k:
                res.append(node.val)
                continue

            for nxt in self.graph[node]:
                if nxt not in visited:
                    visited.add(nxt)
                    q.append((nxt, dist + 1))

        return res


    # NOTE !!
    # helper func ONLY has 1 param
    def build_graph(self, root):
        if not root:
            return

        """
        NOTE !!!


        1. we need to record `bi - direction` path
           -> e.g.

             root <--> root.left



        2. ONLY do above if child exists
            e.g.

            if root.left
            if root.right

        """
        if root.left:
            self.graph[root].append(root.left)
            self.graph[root.left].append(root)

        if root.right:
            self.graph[root].append(root.right)
            self.graph[root.right].append(root)

        self.build_graph(root.left)
        self.build_graph(root.right)


# V0
# IDEA: DFS + BFS (gemini)
"""
CORE IDEA:


-> Use BFS to radiate outward from the target node


1. build the graph: {node: parent}
2. use BFS, init q as [target, 0]    (node, dist)
3. run BFS, collect nodes when dist == k
"""
from collections import defaultdict, deque

class Solution(object):
    def distanceK(self, root, target, k):
        self.graph = defaultdict(list)

        self.build_graph(root, None)

        q = deque([(target, 0)])

        # NOTE !!!
        # use `visited` to avoid duplicated visiting
        visited = {target}
        res = []

        while q:
            node, dist = q.popleft()

            if dist == k:
                res.append(node.val)
                continue

            for nxt in self.graph[node]:
                if nxt not in visited:
                    visited.add(nxt)
                    q.append((nxt, dist + 1))

        return res


    # pre-order DFS
    # NOTE !!
    # helper func has 2 param (e.g. node, parent)
    def build_graph(self, node, parent):
        if not node:
            return

        """
        NOTE !!!

        how we build the graph

        -> move `2 directions`
        -> e.g.  node -> parent, parent -> node
            -> so we can both record the relation from node <--> parent


        -> also NOTE the `if parent` condition
        """
        if parent:
            self.graph[node].append(parent)
            self.graph[parent].append(node)


        # NOTE: dfs calling
        self.build_graph(node.left, node)
        self.build_graph(node.right, node)


# V0-0-1
# IDEA: BFS (gemini)
"""
CORE IDEA:


-> Use BFS to radiate outward from the target node


1. build the graph: {node: parent}
2. use BFS, init q as [target, 0]    (node, dist)
3. run BFS, collect nodes when dist == k
"""
import collections

class Solution(object):
    def distanceK(self, root, target, k):
        """
        :type root: TreeNode
        :type target: TreeNode
        :type k: int
        :rtype: List[int]
        """
        # Step 1: Traverse the tree to map every node to its parent
        parents = {}
        def add_parents(node, parent):
            if not node:
                return
            parents[node] = parent
            add_parents(node.left, node)
            add_parents(node.right, node)
            
        add_parents(root, None)
        
        # Step 2: Use BFS to radiate outward from the target node
        queue = collections.deque([(target, 0)]) # (current_node, distance)
        visited = set([target])
        ans = []
        
        while queue:
            node, dist = queue.popleft()
            
            # If we reached distance k, add to answer. 
            # We don't need to go any further down this path!
            if dist == k:
                ans.append(node.val)
                continue
                
            # Look at all 3 possible directions: Left, Right, and Up (Parent)
            for neighbor in (node.left, node.right, parents[node]):
                # If the neighbor exists and we haven't visited it yet
                if neighbor and neighbor not in visited:
                    visited.add(neighbor)
                    queue.append((neighbor, dist + 1))
                    
        return ans


# V0-1

# V0-2

# V0-3
# IDEA: BFS (gemini)
"""
CORE IDEA:


-> Use BFS to radiate outward from the target node


1. build the graph: {node: parent}
2. use BFS, init q as [target, 0]    (node, dist)
3. run BFS, collect nodes when dist == k
"""
import collections

class Solution(object):
    def distanceK(self, root, target, k):
        """
        :type root: TreeNode
        :type target: TreeNode
        :type k: int
        :rtype: List[int]
        """
        # Step 1: Traverse the tree to map every node to its parent
        parents = {}
        def add_parents(node, parent):
            if not node:
                return
            parents[node] = parent
            add_parents(node.left, node)
            add_parents(node.right, node)
            
        add_parents(root, None)
        
        # Step 2: Use BFS to radiate outward from the target node
        queue = collections.deque([(target, 0)]) # (current_node, distance)
        visited = set([target])
        ans = []
        
        while queue:
            node, dist = queue.popleft()
            
            # If we reached distance k, add to answer. 
            # We don't need to go any further down this path!
            if dist == k:
                ans.append(node.val)
                continue
                
            # Look at all 3 possible directions: Left, Right, and Up (Parent)
            for neighbor in (node.left, node.right, parents[node]):
                # If the neighbor exists and we haven't visited it yet
                if neighbor and neighbor not in visited:
                    visited.add(neighbor)
                    queue.append((neighbor, dist + 1))
                    
        return ans

# V0-4
# IDEA: DFS (gpt)
class Solution(object):
    def distanceK(self, root, target, k):
        self.ans = []
        self.k = k

        self.dfs(root, target)
        return self.ans

    def collect(self, root, dist):
        if not root:
            return

        if dist == self.k:
            self.ans.append(root.val)
            return

        self.collect(root.left, dist + 1)
        self.collect(root.right, dist + 1)

    def dfs(self, root, target):
        if not root:
            return -1

        if root == target:
            self.collect(root, 0)
            return 0

        left = self.dfs(root.left, target)

        if left != -1:
            if left + 1 == self.k:
                self.ans.append(root.val)
            else:
                self.collect(root.right, left + 2)
            return left + 1

        right = self.dfs(root.right, target)

        if right != -1:
            if right + 1 == self.k:
                self.ans.append(root.val)
            else:
                self.collect(root.left, right + 2)
            return right + 1

        return -1


# V0
# IDEA : DFS + BFS
# https://leetcode.com/problems/all-nodes-distance-k-in-binary-tree/discuss/604718/Python-BFS-solution
# DEMO
# root = TreeNode{val: 3, left: TreeNode{val: 5, left: TreeNode{val: 6, left: None, right: None}, right: TreeNode{val: 2, left: TreeNode{val: 7, left: None, right: None}, right: TreeNode{val: 4, left: None, right: None}}}, right: TreeNode{val: 1, left: TreeNode{val: 0, left: None, right: None}, right: TreeNode{val: 8, left: None, right: None}}}
# self.graph = defaultdict(<class 'list'>, {3: [5, 1], 5: [3, 6, 2], 6: [5], 2: [5, 7, 4], 7: [2], 4: [2], 1: [3, 0, 8], 0: [1], 8: [1]})
# time = O(n)  # n = number of tree nodes
# space = O(n)
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
# time = O(n)  # n = number of tree nodes
# space = O(n)
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
# time = O(n)  # n = number of tree nodes
# space = O(n)
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
# time = O(n)  # n = number of tree nodes
# space = O(n)
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
# time = O(n)  # n = number of tree nodes
# space = O(n)
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
# time = O(n)  # n = number of tree nodes
# space = O(n)
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
# time = O(n)  # n = number of tree nodes
# space = O(n)
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
# time = O(n)  # n = number of tree nodes
# space = O(n)
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
# time = O(n)  # n = number of tree nodes
# space = O(n)
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
# PROCESS:
# 0. If we know the parent of every node x, we know all nodes that are distance 1 from x. We can then perform a breadth first search from the target node to find the answer.
# 1. We first do a depth first search where we annotate every node with information about it's parent.
# 2. After, we do a breadth first search to find all nodes a distance K from the target.
# time = O(n)  # n = number of tree nodes
# space = O(n)
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
# time = O(n)  # n = number of tree nodes
# space = O(n)
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
# time = O(n)
# space = O(n)
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
