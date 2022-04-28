# DFS 
- search algorithm
- Deep first, then breadth
- To check if some value exists
- Inorder, preorder, postorder (can recreate a tree)
- not most efficient (VS bfs), but can handle some specific problems
- Ref

## 0) Concept

### 0-1) Types

- Types
    - normal transversal (pre-order, in-order, post-order)
    - normal transversal with special op:
        - `root.right -> do sth -> root.left`

- Algorithm
    - dfs
    - recursive
    - graph

- Data structure
    - TreeNode
    - dict
    - set
    - array

### 0-2) Pattern

```python
# python
# form I : tree transversal
def dfs(root, target):

    if root.val == target:
       # do sth

    if root.val < target:
       dfs(root.left, target)
       # do sth

    if root.val > target:
       dfs(root.right, target)
       # do sth
```

```python
# form II : modify values in tree

# 669 Trim a Binary Search Tree
class Solution:
    def trimBST(self, root, L, R):
        if not root:
            return 
        # NOTICE HERE 
        # SINCE IT'S BST
        # SO if root.val < L, THE root.right MUST LARGER THAN L
        # SO USE self.trimBST(root.right, L, R) TO FIND THE NEXT "VALIDATE" ROOT AFTER TRIM
        # THE REASON USE self.trimBST(root.right, L, R) IS THAT MAYBE NEXT ROOT IS TRIMMED AS WELL, SO KEEP FINDING VIA RECURSION
        if root.val < L:
            return self.trimBST(root.right, L, R)
        # NOTICE HERE 
        # SINCE IT'S BST
        # SO if root.val > R, THE root.left MUST SMALLER THAN R
        # SO USE self.trimBST(root.left, L, R) TO FIND THE NEXT "VALIDATE" ROOT AFTER TRIM
        if root.val > R:
            return self.trimBST(root.left, L, R)
        root.left = self.trimBST(root.left, L, R)
        root.right = self.trimBST(root.right, L, R)
        return root 

# 701 Insert into a Binary Search Tree
class Solution(object):
    def insertIntoBST(self, root, val):
        """
        NOTE !!!
            1) we ALWAYS do op first, then do recursive
                -> e.g.
                        ...
                        if not root: 
                            return TreeNode(val)
                        if root.val < val:
                            root.right = self.insertIntoBST(root.right, val)
                        ...
        """
        if not root: 
            return TreeNode(val)
        if root.val < val: 
            root.right = self.insertIntoBST(root.right, val);
        elif root.val > val: 
            root.left = self.insertIntoBST(root.left, val);
        return(root)
```

```python
# form III : check if a value exist in the BST

def dfs(root, value):
    if not root:
        return False
    if root.val == value:
        return True
    if root.val > value:
        return dfs(root.left, value) 
    if root.val < value:
        return dfs(root.right, value)
```

```python
# form IV : check if duplicated nodes in tree
# LC 652
# python
m = collections.defaultdict(int)
# m is collection for record visited nodes
def dfs(root, m, res):
    if not root:
        return "#"

    ### NOTE : we get path via below, so we can know duplicated nodes
    path = root.val + "-" + self.dfs(root.left, m, res) + "-" + self.dfs(root.right, m, res)

    if m[path] == 1:
        res.append(path)

    m[path] += 1
    return path
```

### 0-3) Tricks
```python
# we don't need to declare y,z in func, but we can use them in the func directly
# and can get the returned value as well, this trick is being used a lot in the dfs
def test():
    def func(x):
        print ("x = " + str(x) + " y = " + str(y))
        for i in range(3):
            z.append(i)

    x = 0
    y = 100
    z = []
    func(x)
test()
print (z)
```

## 1) General form
```python
# form I
def dfs(root):

    # if root, do sth
    if root:
        # do sth

    # if not root, nothing to do

    # if root.left exist
    if root.left:
        dfs(root.left)
    # if root.right exist
    if root.right:
        dfs(root.right)

# form II
def dfs(root):

    # if root, do sth
    if root:
        # do sth

    # if not root, nothing to do

    if root.left:
    dfs(root.left)
    dfs(root.right)
```

### 1-1) Basic OP

#### 1-1-1) Add 1 to all node.value in Binary tree?
```python
# Example) Add 1 to all node.value in Binary tree?
def dfs(root):
    if not root:
        return 
    root.val += 1 
    dfs(root.left)
    dfs(root.right)
```

#### 1-1-2) check if 2 Binary tree are the same
```python
# Example) check if 2 Binary tree are the same ? 
def dfs(root1, root2):
    if root1 == root2 == None:
        return True 
    if root1 is not None and root2 is None:
        return False 
    if root1 is None and root2 is not None:
        return False 
    else:
        if root1.val != root2.value:
            return False 
    return dfs(root1.left, root2.left) \
           and dfs(root1.right, root2.right)
```

#### 1-1-3) check if a value exist in the BST
```python
# Example) check if a value exist in the BST
def dfs(root, value):
    if not root:
        return False
    if root.val == value:
        return True
    return dfs(root.left, value) or dfs(root.right, value)

# optimized : BST prpoerty :  root.right > root.val > root.left
def dfs(root, value):
    if not root:
        return False
    if root.val == value:
        return True
    if root.val > value:
        return dfs(root.left, value) 
    if root.val < value:
        return dfs(root.right, value)
```

#### 1-1-4) get sum of sub tree

```python
# get sum of sub tree
# LC 508 Most Frequent Subtree Sum
def get_sum(root):
    if not root:
        return 0
    ### NOTE THIS !!!
    #  -> we need to do get_sum(root.left), get_sum(root.right) on the same time
    s = get_sum(root.left) + root.val + get_sum(root.right)
    res.append(s)
    return s
```

#### 1-1-5) get `aggregated sum` for every node in tree
```python
# LC 663 Equal Tree Partition
# LC 508 Most Frequent Subtree Sum
seen = []
def _sum(root):
    if not root:
        return 0
    seen.append( root.val + _sum(root.left) + _sum(root.right) )
```

#### 1-1-6) Convert BST to Greater Tree 
```python
# Convert BST to Greater Tree 
# LC 538
_sum = 0
def dfs(root):
    dfs(root.right)
    _sum += root.val
    root.val = _sum
    dfs(root.left)
```

#### 1-1-7) Serialize and Deserialize Binary Tree
```python
# LC 297. Serialize and Deserialize Binary Tree
# please check below 2) LC Example
# V0
# IDRA : DFS
class Codec:

    def serialize(self, root):
        """ Encodes a tree to a single string.
        :type root: TreeNode
        :rtype: str
        """
        def rserialize(root, string):
            """ a recursive helper function for the serialize() function."""
            # check base case
            if root is None:
                string += 'None,'
            else:
                string += str(root.val) + ','
                string = rserialize(root.left, string)
                string = rserialize(root.right, string)
            return string
        
        return rserialize(root, '')    

    def deserialize(self, data):
        """Decodes your encoded data to tree.
        :type data: str
        :rtype: TreeNode
        """
        def rdeserialize(l):
            """ a recursive helper function for deserialization."""
            if l[0] == 'None':
                l.pop(0)
                return None
                
            root = TreeNode(l[0])
            l.pop(0)
            root.left = rdeserialize(l)
            root.right = rdeserialize(l)
            return root

        data_list = data.split(',')
        root = rdeserialize(data_list)
        return root
```

#### 1-1-8) Serialize and Deserialize BST
```python
# LC 449. Serialize and Deserialize BST
# please check below 2) LC Example
# NOTE : there is also a bfs approach
# V1'
# IDEA : BST property
# https://leetcode.com/problems/serialize-and-deserialize-bst/discuss/212043/Python-solution
class Codec:

    def serialize(self, root):
        """Encodes a tree to a single string.
        
        :type root: TreeNode
        :rtype: str
        """
        def dfs(root):
            if not root:
                return 
            res.append(str(root.val) + ",")
            dfs(root.left)
            dfs(root.right)
            
        res = []
        dfs(root)
        return "".join(res)

    def deserialize(self, data):
        """Decodes your encoded data to tree.
        
        :type data: str
        :rtype: TreeNode
        """
        lst = data.split(",")
        lst.pop()
        stack = []
        head = None
        for n in lst:
            n = int(n)
            if not head:
                head = TreeNode(n)
                stack.append(head)
            else:
                node = TreeNode(n)
                if n < stack[-1].val:
                    stack[-1].left = node
                else:
                    while stack and stack[-1].val < n: 
                        u = stack.pop()
                    u.right = node
                stack.append(node)
        return head
```

## 2) LC Example

### 2-1) Validate Binary Search Tree
```python
# 098 Validate Binary Search Tree
### NOTE : there is also bfs solution
# https://github.com/yennanliu/CS_basics/blob/master/leetcode_python/Recursion/validate-binary-search-tree.py
class Solution(object):
    def isValidBST(self, root):
        return self.valid(root, float('-inf'), float('inf'))
        
    def valid(self, root, min_, max_):
        if not root: return True
        if root.val >= max_ or root.val <= min_:
            return False
        return self.valid(root.left, min_, root.val) and self.valid(root.right, root.val, max_)
```

### 2-2) Insert into a Binary Search Tree
```python
# 701 Insert into a Binary Search Tree

# VO : recursion + dfs
class Solution(object):
    def insertIntoBST(self, root, val):
        if not root: 
            return TreeNode(val)
        if root.val < val: 
            root.right = self.insertIntoBST(root.right, val);
        elif root.val > val: 
            root.left = self.insertIntoBST(root.left, val);
        return(root)
```

### 2-3) Delete Node in a BST
```python
# 450 Delete Node in a BST
# V0
# IDEA : RECURSION + BST PROPERTY
#### 2 CASES :
#   -> CASE 1 : root.val == key and NO right subtree 
#                -> swap root and root.left, return root.left
#   -> CASE 2 : root.val == key and THERE IS right subtree
#                -> 1) go to 1st RIGHT sub tree
#                -> 2) iterate to deepest LEFT subtree
#                -> 3) swap root and  `deepest LEFT subtree` then return root
class Solution(object):
    def deleteNode(self, root, key):
        if not root: return None
        if root.val == key:
            # case 1 : NO right subtree 
            if not root.right:
                left = root.left
                return left
            # case 2 : THERE IS right subtree
            else:
                ### NOTE : find min in "right" sub-tree
                #           -> because BST property, we ONLY go to 1st right tree (make sure we find the min of right sub-tree)
                #           -> then go to deepest left sub-tree
                right = root.right
                while right.left:
                    right = right.left
                ### NOTE : we need to swap root, right ON THE SAME TIME
                root.val, right.val = right.val, root.val
        root.left = self.deleteNode(root.left, key)
        root.right = self.deleteNode(root.right, key)
        return root
```

### 2-4) Find Duplicate Subtrees
```python
# 652 Find Duplicate Subtrees
import collections
class Solution(object):
    def findDuplicateSubtrees(self, root):
        res = []
        m = collections.defaultdict(int)
        self.dfs(root, m, res)
        return res

    def dfs(self, root, m, res):
        if not root:
            return '#'
        path = str(root.val) + '-' + self.dfs(root.left, m, res) + '-' + self.dfs(root.right, m, res)
        if m[path] == 1:
            res.append(root) 
        m[path] += 1
        return path
```

### 2-5) Trim a BST
```python
# 669 Trim a Binary Search Tree
class Solution:
    def trimBST(self, root, L, R):
        if not root:
            return None
        if root.val > R:
            return self.trimBST(root.left, L, R)
        elif root.val < L:
            return self.trimBST(root.right, L, R)
        else:
            root.left = self.trimBST(root.left, L, R)
            root.right = self.trimBST(root.right, L, R)
            return root
```

### 2-6) Maximum Width of Binary Tree
```python
# 662 Maximum Width of Binary Tree
class Solution(object):
    def widthOfBinaryTree(self, root):
        self.ans = 0
        left = {}
        def dfs(node, depth = 0, pos = 0):
            if node:
                left.setdefault(depth, pos)
                self.ans = max(self.ans, pos - left[depth] + 1)
                dfs(node.left, depth + 1, pos * 2)
                dfs(node.right, depth + 1, pos * 2 + 1)

        dfs(root)
        return self.ans
```

### 2-7) Equal Tree Partition
```python
# 663 Equal Tree Partition
# V0
# IDEA : DFS + cache
class Solution(object):
    def checkEqualTree(self, root):
        seen = []

        def sum_(node):
            if not node: return 0
            seen.append(sum_(node.left) + sum_(node.right) + node.val)
            return seen[-1]

        sum_(root)
        #print ("seen = " + str(seen))
        return seen[-1] / 2.0 in seen[:-1]

# V0'
class Solution(object):
    def checkEqualTree(self, root):
        seen = []

        def sum_(node):
            if not node: return 0
            seen.append(sum_(node.left) + sum_(node.right) + node.val)
            return seen[-1]
            
        total = sum_(root)
        seen.pop()
        return total / 2.0 in seen
```

### 2-8) Split BST
```python
# 776 Split BST
# V0
# IDEA : BST properties (left < root < right) + recursion
# https://blog.csdn.net/magicbean2/article/details/79679927
# https://www.itdaan.com/tw/d58594b92742689b5769f9827365e8b4
### STEPS
#  -> 1) check whether root.val > or < V
#     -> if root.val > V : 
#           - NO NEED TO MODIFY ALL RIGHT SUB TREE
#           - BUT NEED TO re-connect nodes in LEFT SUB TREE WHICH IS BIGGER THAN V (root.left = right)
#     -> if root.val < V : 
#           - NO NEED TO MODIFY ALL LEFT SUB TREE
#           - BUT NEED TO re-connect nodes in RIGHT SUB TREE WHICH IS SMALLER THAN V (root.right = left)
# -> 2) return result
class Solution(object):
    def splitBST(self, root, V):
        if not root: return [None, None]
        ### NOTE : if root.val <= V
        if root.val > V:
            left, right = self.splitBST(root.left, V)
            root.left = right
            return [left, root]
        ### NOTE : if root.val > V
        else:
            left, right = self.splitBST(root.right, V)
            root.right = left
            return [root, right]
```

### 2-9) Evaluate Division
```python
# 399 Evaluate Division
# there is also an "union find" solution
class Solution:
    def calcEquation(self, equations, values, queries):
        from collections import defaultdict
        # build graph
        graph = defaultdict(dict)
        for (x, y), v in zip(equations, values):
            graph[x][y] = v
            graph[y][x] = 1.0/v
        ans = [self.dfs(x, y, graph, set()) for (x, y) in queries]
        return ans

    def dfs(self, x, y, graph, visited):
        if not graph:
            return
        if x not in graph or y not in graph:
            return -1
        if x == y:
            return 1
        visited.add(x)
        for n in graph[x]:
            if n in visited:
                continue
            visited.add(n)
            d = self.dfs(n, y, graph, visited)
            if d > 0:
                return d * graph[x][n]
        return -1.0
```

### 2-10) Most Frequent Subtree Sum
```python
# LC 508 Most Frequent Subtree Sum
# V0
# IDEA : DFS + TREE
class Solution(object):
    def findFrequentTreeSum(self, root):
        """
        ### NOTE : this trick : get sum of sub tree
        # LC 663 Equal Tree Partition
        """
        def get_sum(root):
            if not root:
                return 0
            s = get_sum(root.left) + root.val + get_sum(root.right)
            res.append(s)
            return s

        if not root:
            return []
        res = []
        get_sum(root)
        counts = collections.Counter(res)
        _max = max(counts.values())
        return [x for x in counts if counts[x] == _max]

# V0'
# IDEA : DFS + COUNTER
from collections import Counter
class Solution(object):
    def findFrequentTreeSum(self, root):
        def helper(root, d):
            if not root:
                return 0
            left = helper(root.left, d)
            right = helper(root.right, d)
            subtreeSum = left + right + root.val
            d[subtreeSum] = d.get(subtreeSum, 0) + 1
            return subtreeSum      
        d = {}
        helper(root, d)
        mostFreq = 0
        ans = []
        print ("d = " + str(d))
        _max_cnt = max(d.values())
        ans = []
        return [x for x in d if d[x] == _max_cnt]
```

### 2-11) Convert BST to Greater Tree
```python
# LC 538 Convert BST to Greater Tree
# V0
# IDEA : DFS + recursion
#      -> NOTE : via DFS, the op will being executed in `INVERSE` order (last visit will be run first, then previous, then ...)
#      -> e.g. node1 -> node2 -> ... nodeN
#      ->      will run nodeN -> nodeN-1 ... node1
class Solution(object):

    def convertBST(self, root):
        self.sum = 0
        self.dfs(root)
        return root

    def dfs(self, node):
        if not node: 
            return
        #print ("node.val = " + str(node.val))
        self.dfs(node.right)
        self.sum += node.val
        node.val = self.sum
        self.dfs(node.left)

# V0'
# NOTE : the implementation difference on cur VS self.cur
# 1) if cur : we need to ssign output of help() func to cur
# 2) if self.cur : no need to assign, plz check V0 as reference
class Solution(object):
    def convertBST(self, root):
        def help(cur, root):
            if not root:
                ### NOTE : if not root, still need to return cur
                return cur
            ### NOTE : need to assign output of help() func to cur
            cur = help(cur, root.right)
            cur += root.val
            root.val = cur
            ### NOTE : need to assign output of help() func to cur
            cur = help(cur, root.left)
            ### NOTE : need to return cur
            return cur

        if not root:
            return

        cur = 0
        help(cur, root)
        return root
```

### 2-12) Number of Islands
```python
# LC 200 Number of Islands, check LC 694, 711 as well
# V0 
# IDEA : DFS
class Solution(object):
    def numIslands(self, grid):
        def dfs(grid, item):
            if grid[item[0]][item[1]] == "0":
                return

            ### NOTE : MAKE grid[item[0]][item[1]] = 0 -> avoid visit again
            grid[item[0]][item[1]] = 0
            moves = [(0,1),(0,-1),(1,0),(-1,0)]
            for move in moves:
                _x = item[0] + move[0]
                _y = item[1] + move[1]
                ### NOTE : the boundary
                #       -> _x < l, _y < w
                if 0 <= _x < l and 0 <= _y < w and grid[_x][_y] != 0:
                    dfs(grid, [_x, _y])
  
        if not grid:
            return 0
        res = 0
        l = len(grid)
        w = len(grid[0])
        for i in range(l):
            for j in range(w):
                if grid[i][j] == "1":
                    ### NOTE : we go through every "1" in grids, and run dfs once
                    #         -> once dfs completed, we make res += 1 in each iteration
                    dfs(grid, [i,j])
                    res += 1
        return res
```


### 2-13) Max Area of Island
```python
# LC 695. Max Area of Island
# V1
# https://blog.csdn.net/fuxuemingzhu/article/details/79182435
# IDEA : DFS 
# * PLEASE NOTE THAT IT IS NEEDED TO GO THROUGH EVERY ELEMENT IN THE GRID 
#   AND RUN THE DFS WITH IN THIS PROBLEM
class Solution(object):
    def maxAreaOfIsland(self, grid):
        """
        :type grid: List[List[int]]
        :rtype: int
        """
        self.res = 0
        self.island = 0
        M, N = len(grid), len(grid[0])
        for i in range(M):
            for j in range(N):
                if grid[i][j]:
                    self.dfs(grid, i, j)
                    self.res = max(self.res, self.island)
                    self.island = 0
        return self.res
    
    def dfs(self, grid, i, j): # ensure grid[i][j] == 1
        M, N = len(grid), len(grid[0])
        grid[i][j] = 0
        self.island += 1
        dirs = [(0, 1), (0, -1), (-1, 0), (1, 0)]
        for d in dirs:
            x, y = i + d[0], j + d[1]
            if 0 <= x < M and 0 <= y < N and grid[x][y]:
                self.dfs(grid, x, y)
```

### 2-14) Binary Tree Paths
```python
# LC 257. Binary Tree Paths
# V0 
# IDEA : DFS 
class Solution:
    # @param {TreeNode} root
    # @return {string[]}
    def binaryTreePaths(self, root):
        res, path_list = [], []
        self.dfs(root, path_list, res)
        return res

    def dfs(self, root, path_list, res):
        if not root:
            return
        path_list.append(str(root.val))
        if not root.left and not root.right:
            res.append('->'.join(path_list))
        if root.left:
            self.dfs(root.left, path_list, res)
        if root.right:
            self.dfs(root.right, path_list, res)
        path_list.pop()
```

### 2-15) Lowest Common Ancestor of a Binary Tree
```python
# LC 236 Lowest Common Ancestor of a Binary Tree
# V0
# IDEA : RECURSION + POST ORDER TRANSVERSAL
class Solution(object):
    def lowestCommonAncestor(self, root, p, q):

        ### NOTE here
        # if not root or find p in tree or find q in tree
        # -> then we quit the recursion and return root

        ### NOTE : we compare `p == root` and  `q == root`
        if not root or p == root or q == root:
            return root
        ### NOTE here
        #  -> not root.left, root.right, BUT left, right
        left = self.lowestCommonAncestor(root.left, p, q)
        right = self.lowestCommonAncestor(root.right, p, q)

        ### NOTE here
        # find q and p on the same time -> LCA is the current node (root)
        # if left and right -> p, q MUST in left, right sub tree respectively

        ### NOTE : if left and right, means this root is OK for next recursive
        if left and right:
            return root
        ### NOTE here
        # if p, q both in left sub tree or both in right sub tree
        return left if left else right
```

### 2-16) Path Sum
```python
# LC 112 Path Sum
# V0
# IDEA : DFS 
class Solution(object):
    def hasPathSum(self, root, sum):
        if not root:
            return False
        if not root.left and not root.right:
            return True if sum == root.val else False
        else:
            return self.hasPathSum(root.left, sum-root.val) or self.hasPathSum(root.right, sum-root.val)
```

### 2-17) Path Sum II
```python
# LC 113 Path Sum II
# V0
# IDEA : DFS
class Solution(object):
    def pathSum(self, root, sum):
        if not root: return []
        res = []
        self.dfs(root, sum, res, [root.val])
        return res

    def dfs(self, root, target, res, path):
        if not root: return
        if sum(path) == target and not root.left and not root.right:
            res.append(path)
            return
        if root.left:
            self.dfs(root.left, target, res, path + [root.left.val])
        if root.right:
            self.dfs(root.right, target, res, path + [root.right.val])
```

### 2-7) Clone graph
```python
# 133 Clone graph
# note : there is also a BFS solution
# V0
# IDEA : DFS
# NOTE :
#  -> 1) we init node via : node_copy = Node(node.val, [])
#  -> 2) we copy graph via dict
class Solution(object):
    def cloneGraph(self, node):
        """
        :type node: Node
        :rtype: Node
        """
        node_copy = self.dfs(node, dict())
        return node_copy
    
    def dfs(self, node, hashd):
        if not node: return None
        if node in hashd: return hashd[node]
        node_copy = Node(node.val, [])
        hashd[node] = node_copy
        for n in node.neighbors:
            n_copy = self.dfs(n, hashd)
            if n_copy:
                node_copy.neighbors.append(n_copy)
        return node_copy
```

### 2-8) Sentence Similarity II
```python
# LC 737. Sentence Similarity II
# NOTE : there is also union-find solution
# V0
# IDEA : DFS
from collections import defaultdict
class Solution(object):
    def areSentencesSimilarTwo(self, sentence1, sentence2, similarPairs):
        # helper func
        def dfs(w1, w2, visited):
            for j in d[w2]:
                if w1 == w2:
                    return True
                elif j not in visited:
                    visited.add(j)
                    if dfs(w1, j, visited):
                        return True
            return False
        
        # edge case
        if len(sentence1) != len(sentence2):
            return False
      
        d = defaultdict(list)
        for a, b in similarPairs:
            d[a].append(b)
            d[b].append(a)
            
        for i in range(len(sentence1)):
            visited =  set([sentence2[i]])
            if sentence1[i] != sentence2[i] and not dfs(sentence1[i],  sentence2[i], visited):
                return False
        return True
```

### 2-9) Concatenated Words
```python
# LC 472. Concatenated Words
# V1
# http://bookshadow.com/weblog/2016/12/18/leetcode-concatenated-words/
# IDEA : DFS 
class Solution(object):
    def findAllConcatenatedWordsInADict(self, words):
        """
        :type words: List[str]
        :rtype: List[str]
        """
        ans = []
        self.wordSet = set(words)
        for word in words:
            self.wordSet.remove(word) # avoid the search process find itself (word) when search all word in words  
            if self.search(word):
                ans.append(word)
            self.wordSet.add(word)    # add the word back for next search with new "word"
        return ans

    def search(self, word):
        if word in self.wordSet:
            return True
        for idx in range(1, len(word)):
            if word[:idx] in self.wordSet and self.search(word[idx:]):
                return True
        return False
```

### 2-10) Maximum Product of Splitted Binary Tree
```python
# LC 1339. Maximum Product of Splitted Binary Tree
# V0
# IDEA : DFS
class Solution(object):
    def maxProduct(self, root):
        all_sums = []

        def tree_sum(subroot):
            if subroot is None: return 0
            left_sum = tree_sum(subroot.left)
            right_sum = tree_sum(subroot.right)
            total_sum = left_sum + right_sum + subroot.val
            all_sums.append(total_sum)
            return total_sum

        total = tree_sum(root)
        best = 0
        for s in all_sums:
            best = max(best, s * (total - s))   
        return best % (10 ** 9 + 7)
```

### 2-11) Serialize and Deserialize Binary Tree
```python
# LC 297. Serialize and Deserialize Binary Tree
# V0
# IDRA : DFS
class Codec:

    def serialize(self, root):
        """ Encodes a tree to a single string.
        :type root: TreeNode
        :rtype: str
        """
        def rserialize(root, string):
            """ a recursive helper function for the serialize() function."""
            # check base case
            if root is None:
                string += 'None,'
            else:
                string += str(root.val) + ','
                string = rserialize(root.left, string)
                string = rserialize(root.right, string)
            return string
        
        return rserialize(root, '')    

    def deserialize(self, data):
        """Decodes your encoded data to tree.
        :type data: str
        :rtype: TreeNode
        """
        def rdeserialize(l):
            """ a recursive helper function for deserialization."""
            if l[0] == 'None':
                l.pop(0)
                return None
                
            root = TreeNode(l[0])
            l.pop(0)
            root.left = rdeserialize(l)
            root.right = rdeserialize(l)
            return root

        data_list = data.split(',')
        root = rdeserialize(data_list)
        return root

# V1
# IDEA : same as LC 297
# https://leetcode.com/problems/serialize-and-deserialize-bst/discuss/93283/Python-solution-using-BST-property
class Codec:

    def serialize(self, root):
        vals = []
        self._preorder(root, vals)
        return ','.join(vals)
        
    def _preorder(self, node, vals):
        if node:
            vals.append(str(node.val))
            self._preorder(node.left, vals)
            self._preorder(node.right, vals)
        
    def deserialize(self, data):
        vals = collections.deque(map(int, data.split(','))) if data else []
        return self._build(vals, -float('inf'), float('inf'))

    def _build(self, vals, minVal, maxVal):
        if vals and minVal < vals[0] < maxVal:
            val = vals.popleft()
            root = TreeNode(val)
            root.left = self._build(vals, minVal, val)
            root.right = self._build(vals, val, maxVal)
            return root
        else:
            return None
```

### 2-12) Serialize and Deserialize BST
```python
# LC 449. Serialize and Deserialize BST
# V0
# IDEA : BFS + queue op
class Codec:
    def serialize(self, root):
        if not root:
            return '{}'

        res = [root.val]
        q = [root]

        while q:
            new_q = []
            for i in range(len(q)):
                tmp = q.pop(0)
                if tmp.left:
                    q.append(tmp.left)
                    res.extend( [tmp.left.val] )
                else:
                    res.append('#')
                if tmp.right:
                    q.append(tmp.right)
                    res.extend( [tmp.right.val] )
                else:
                    res.append('#')

        while res and res[-1] == '#':
                    res.pop()

        return '{' + ','.join(map(str, res)) + '}' 


    def deserialize(self, data):
        if data == '{}':
            return

        nodes = [ TreeNode(x) for x in data[1:-1].split(",") ]
        root = nodes.pop(0)
        p = [root]
        while p:
            new_p = []
            for n in p:
                if nodes:
                    left_node = nodes.pop(0)
                    if left_node.val != '#':
                        n.left = left_node
                        new_p.append(n.left)
                    else:
                        n.left = None
                if nodes:
                    right_node = nodes.pop(0)
                    if right_node.val != '#':
                        n.right = right_node
                        new_p.append(n.right)
                    else:
                        n.right = None
            p = new_p 
             
        return root

# V1
# IDEA : same as LC 297
# https://leetcode.com/problems/serialize-and-deserialize-bst/discuss/93283/Python-solution-using-BST-property
class Codec:

    def serialize(self, root):
        vals = []
        self._preorder(root, vals)
        return ','.join(vals)
        
    def _preorder(self, node, vals):
        if node:
            vals.append(str(node.val))
            self._preorder(node.left, vals)
            self._preorder(node.right, vals)
        
    def deserialize(self, data):
        vals = collections.deque(map(int, data.split(','))) if data else []
        return self._build(vals, -float('inf'), float('inf'))

    def _build(self, vals, minVal, maxVal):
        if vals and minVal < vals[0] < maxVal:
            val = vals.popleft()
            root = TreeNode(val)
            root.left = self._build(vals, minVal, val)
            root.right = self._build(vals, val, maxVal)
            return root
        else:
            return None
```