# BFS
- search algorithm
- Shortest path searching
    - LC 127
    - LC 1730
- Breadth first, then deep
- Usually use data structure such as `queue, array`

## 0) Concept  

### 0-1) Types

- Types
    - Tree problems
    - String problems
    - Array problems
    - Graph problems

- Algorithm
    - bfs

- Data structure
    - queue
    - defaultDict
    - array

### 0-2) Pattern
```python
# python
import colllections 
queue = colllections.deque()
queue.append(root)
"""
NOTE THIS : (while len(queue) > 0)
"""
while len(queue) > 0:
    """
    NOTE THIS : (range(len(queue)))
    """
    for i in range(len(queue)):
        node = queue.popleft()
        # op
        if node.left:
            queue.append(node.left)
        if node.right:
            queue.append(node.right) 
```

## 1) General form
```python
# V0 : via python default
def bfs(root):
    if not root:
        return
    q =[]
    q.append(root)
    while root:
        for i in range(len(q)):
            ### NOTE : we need to pop the 1st element (idx = 0), BUT NOT THE LAST element
            #        -> default is pop last element (e.g. q.pop() equals q.pop(-1))
            root = q.pop(0)
            # do sth
            if root.left:
                q.append(root.left)
            if root.right:
                q.append(root.right)
    # do sth

# V1 : via collections.deque
import collections
queue = collections.deque() 
def bfs(root):
    if not root:return 
    queue.append(root)
    while len(queue) > 0:
        ### NOTE this op
        for i in range(len(queue)):
            ### NOTE : the variable from popleft
            #          should be used in node.left, node.right op below
            ### NOTE : we need to pop the 1st element (idx = 0), BUT NOT THE LAST element
            node = queue.popleft() 
            # do sth 
            if node.left:
                queue.append(node.left)
            if node.right:
                queue.append(node.right)
            # do sth

# V2 : via python default
def bfs(root):
    if not root: return 
    q=[root]
    while len(q) > 0:
        ### NOTE this op
        for i in range(0,len(q)):
            tmp = q[0]
            # do sth
            if tmp.left:
                q.append(tmp.left)
            if tmp.right:
                q.append(tmp.right)
            # do sth
```

### 1-1) Basic OP

#### 1-1) Maintain cache along binary tree path
```python
# LC 257 Binary Tree Paths
cur = ""
q = [[cur, root]]
res = []
while q:
    for i in range(len(q)):
        cur, tmp = q.pop(0)
        if tmp:
            if not tmp.left and not tmp.right:
                res.append(cur + str(tmp.val))
        if tmp.left:
            q.append([cur + str(tmp.val) + "->", tmp.left])
        if root.right:
            q.append([cur + str(tmp.val) + "->", tmp.right])
```

## 2) LC Example

### 2-1) Binary Tree Level Order Traversal
```python
# 102 Binary Tree Level Order Traversal
# 107 Binary Tree Level Order Traversal II
# 103 Binary Tree Zigzag Level Order Traversal

# V0
# IDEA : BFS
class Solution(object):
    def levelOrder(self, root):
        if not root:
            return []
        res = []
        _layer=0
        q = [[root, _layer]]
        while q:
            for i in range(len(q)):
                tmp, _layer = q.pop(0)
                if len(res) <= _layer:
                    res.append([])
                res[_layer].append(tmp.val)
                if tmp.left:
                    q.append([tmp.left, _layer+1])
                if tmp.right:
                    q.append([tmp.right, _layer+1])
        return res

# V0'
# note : there is also a dfs solution
class Solution(object):
    def levelOrder(self, root):
        res = []
        if not root: return res
        queue = collections.deque()
        queue.append(root)
        while queue:
            level = []
            for i in range(len(queue)):  # NOTE THAT HERE WE HAVE TO GO THROUGH EVERY ELEMENT IN THE SAME LAYER OF BST 
                node = queue.popleft()
                level.append(node.val)
                if node.left:
                    queue.append(node.left)
                if node.right:
                    queue.append(node.right)
            res.append(level)
        return res
```

### 2-2) Word Ladder
```python
# LC 227 : Word Ladder
# V0
# IDEA : BFS
# NOTE !!! 
#  1) since we use BFS, so the solution will be shortest one
#  2) from problem : All the words in wordList are unique.
#     -> so there is ONLY one possibility for next word within each iteration
class Solution(object):
    def ladderLength(self, beginWord, endWord, wordList):
        # we get non-duplicated list via set here
        wordset = set(wordList)
        bfs = collections.deque()
        ### NOTE : we use (beginWord, 1) data structure for not only get current word, but also sequence length
        bfs.append((beginWord, 1))
        while bfs:
            word, length = bfs.popleft()
            if word == endWord:
                return length
            """
            ### NOTE : we looping elements in bfs here
            ### NOTE : we have 2 looping here :  for i in range(len(word)), for c in "abcdefghijklmnopqrstuvwxyz"
            """
            for i in range(len(word)):
                for c in "abcdefghijklmnopqrstuvwxyz":
                    newWord = word[:i] + c + word[i + 1:]
                    # if newword is not qeual to original word as well (newWord != word )
                    if newWord in wordset and newWord != word:
                        # note : we need to remove the used word from wordset
                        wordset.remove(newWord)
                        bfs.append((newWord, length + 1))
        return 0
```
### 2-3) Closest Leaf in a Binary Tree
```python
# 742 Closest Leaf in a Binary Tree
# V0
# IDEA : DFS build GRAPH + BFS find ans
### NOTE :  closest to a leaf means the least number of edges travelled on the binary tree to reach any leaf of the tree. Also, a node is called a leaf if it has no children.
#         -> We only consider the min distance between left (no sub tree) and k
### NOTE : we need DFS create the graph
# https://www.youtube.com/watch?v=x1wXkRrpavw
# https://blog.csdn.net/qq_17550379/article/details/87778889
import collections
class Solution:
    # build graph via DFS
    # node : current node
    # parent : parent of current node
    def buildGraph(self, node, parent, k):
        if not node:
            return
        # if node.val == k, THEN GET THE start point FROM current "node",
        # then build graph based on above
        if node.val == k:
            self.start = node
        if parent:
            self.graph[node].append(parent)
            self.graph[parent].append(node)
        self.buildGraph(node.left, node, k)
        self.buildGraph(node.right, node, k)

    # search via BFS
    def findClosestLeaf(self, root, k):
        self.start = None
        ### NOTE : we need DFS create the graph
        self.buildGraph(root, None, k)
        q, visited = [root], set()
        #q, visited = [self.start], set() # need to validate this
        self.graph = collections.defaultdict(list)
        while q:
            for i in range(len(q)):
                cur = q.pop(0)
                # add cur to visited, NOT to visit this node again
                visited.add(cur)
                ### NOTICE HERE 
                # if not cur.left and not cur.right: means this is the leaf (HAS NO ANY left/right node) of the tree
                # so the first value of this is what we want, just return cur.val as answer directly
                if not cur.left and not cur.right:
                    # return the answer
                    return cur.val
                # if not find the leaf, then go through all neighbors of current node, and search again
                for node in self.graph:
                    if node not in visited: # need to check if "if node not in visited" or "if node in visited"
                        q.append(node)
```

### 2-4) Surrounded Regions
```python
# 130 Surrounded Regions
# note : there is also a dfs, union find solution
# V0
# IDEA : BFS
class Solution(object):
    def solve(self, board):
        # edge case
        if not board:
            return board

        l = len(board)
        w = len(board[0])
        """
        NOTE : z_list : get "O" in boarder
        """
        z_list = []
        for i in range(l):
            for j in range(w):
                if board[i][j] == "O":
                    if ( i == l-1 or i == 0  or j == 0 or j == w-1)  and (board[i][j] == "O"):
                    #if i in [0, l-1] or j in [0, w-1] and board[i][j] == "O":
                        z_list.append([i, j])

        # bfs
        #print ("z_list = " + str(z_list))
        moves = [[0,1],[0,-1],[1,0],[-1,0]]
        """
        NOTE !!!
            1) we use bfs go through z_list (list of "O" at boarder)
            2) if there is [y, x] with condition : 0 <= x < w and 0 <= y < l and board[y][x] == "O"
                -> we lebel them as "#"
                -> since those points CONNECT to "O" at boarder,
                -> they SHOULD NOT be transformed to "X"
            3) we use moves for each move ([y + dy, x + dx])
        """
        q = z_list
        while q:
            y, x = q.pop(0)
            if 0 <= x < w and 0 <= y < l and board[y][x] == "O":
                board[y][x] = "#"
                for dy, dx in moves:
                    q.append([y + dy, x + dx])

        """
        NOTE !!!
            2 op here
                1) if board[i][j] == "#" -> make them as "O"
                    -> since they connect to "O" at boarder
                2) if board[i][j] == "O" -> make them as "X"
                    -> since they are "O" not connect to any "O" at boarder
        """
        for i in range(l):
            for j in range(w):
                if board[i][j] == "#":
                    board[i][j] = "O"
                elif board[i][j] == "O":
                    board[i][j] = "X"
        return board
```

### 2-5) Clone graph
```python
# 133 Clone graph
# note : there is also a dfs solution
# IDEA : BFS
class Solution(object):
    def cloneGraph(self, node):
        """
        :type node: Node
        :rtype: Node
        """
        if not node: return
        que = collections.deque()
        hashd = dict()
        que.append(node)
        node_copy = Node(node.val, [])
        hashd[node] = node_copy
        while que:
            t = que.popleft()
            if not t: continue
            for n in t.neighbors:
                if n not in hashd:
                    hashd[n] = Node(n.val, [])
                    que.append(n)
                hashd[t].neighbors.append(hashd[n])
        return node_copy
```

### 2-6) Course Schedule
```python
# 207 Couese Schedule
# 210 Course Schedule II

# note : there is also a dfs solution
# IDEA : BFS
class Solution(object):
    def canFinish(self, N, prerequisites):
        graph = collections.defaultdict(list)
        indegrees = collections.defaultdict(int)
        for u, v in prerequisites:
            graph[v].append(u)
            indegrees[u] += 1
        for i in range(N):
            zeroDegree = False
            for j in range(N):
                if indegrees[j] == 0:
                    zeroDegree = True
                    break
            if not zeroDegree: return False
            indegrees[j] = -1
            for node in graph[j]:
                indegrees[node] -= 1
        return True           
```

### 2-7) Graph Valid Tree
```python
# 261 Graph Valid Tree

# note : there is also a dfs solution
# IDEA : BFS
class Solution:
    def validTree(self, n: int, edges: List[List[int]]) -> bool:        
        if len(edges) != n - 1:  # Check number of edges.
            return False
 
        # init node's neighbors in dict
        neighbors = collections.defaultdict(list)
        for u, v in edges:
            neighbors[u].append(v)
            neighbors[v].append(u)
 
        # BFS to check whether the graph is valid tree.
        visited = {}
        q = collections.deque([0])
        while q:
            curr = q.popleft()
            visited[curr] = True
            for node in neighbors[curr]:
                if node not in visited:
                    visited[node] = True
                    q.append(node)
 
        return len(visited)==n
```
### 2-7) Walls and gates
```python
# 286 Walls and Gates

# note : there is also a dfs solution
# IDEA : BFS
class Solution:
    def wallsAndGates(self, rooms):
        # base case:
        if not rooms:
            return
        row, col = len(rooms), len(rooms[0])
        # find the index of a gate
        q = [(i, j) for i in range(row) for j in range(col) if rooms[i][j] == 0]
        for x, y in q:
            # get the distance from a gate
            distance = rooms[x][y]+1
            directions = [(-1,0), (1,0), (0,-1), (0,1)]
            for dx, dy in directions:
                # find the INF around the gate
                new_x, new_y = x+dx, y+dy
                if 0 <= new_x < row and 0 <= new_y < col and rooms[new_x][new_y] == 2147483647:
                    # update the value
                    rooms[new_x][new_y] = distance
                    q.append((new_x, new_y))
```

### 2-8) The Maze
```python
# 490 The Maze

# note : there is also a dfs solution
# IDEA : BFS
class Solution:
    def hasPath(self, maze, start, destination):
        # write your code here
        Q = [start]
        n = len(maze)
        m = len(maze[0])
        dirs = ((0, 1), (0, -1), (1, 0), (-1, 0))
        while Q:
            i, j = Q.pop(0)
            maze[i][j] = 2
            if i == destination[0] and j == destination[1]:
                return True
            for x, y in dirs:
                row = i + x
                col = j + y
                ### NOTICE : THE OPTIMIZATION HERE
                while 0 <= row < n and 0 <= col < m and maze[row][col] != 1:
                    row += x
                    col += y
                row -= x
                col -= y
                if maze[row][col] == 0:
                    Q.append([row, col])
                    
        return False
```

### 2-9) Binary Tree Right Side View
```python
# LC 199
class Solution(object):
    def rightSideView(self, root):
        if not root:
            return []
        q = []
        layer=0
        res = []
        ### NOTE : we need layer, so we design our `node` in queue as (root, layer)
        q.append((root, layer))
        while q:
            for i in range(len(q)):
                tmp = q.pop()
                ### NOTE : every time we pop root
                #         -> and do the root.left, root.right op below
                root = tmp[0]
                layer = tmp[1]
                if len(res) == layer:
                    res.append([])
                res[layer].append(root.val)
                if root.right:
                    q.append((root.right, layer+1))
                if root.left:
                    q.append((root.left, layer+1))
        return [x[-1] for x in res]
```

### 2-10) Convert BST to Greater Tree
```python
# LC 538 Convert BST to Greater Tree
# note : there is also DFS solution
class Solution(object):
    def convertBST(self, root):
        total = 0      
        node = root
        stack = []
        while stack or node is not None:
            while node is not None:
                stack.append(node)
                node = node.right
            node = stack.pop()
            total += node.val
            node.val = total
            node = node.left
        return root
```

### 2-11) Shortest Path to Get Food
```python
# LC 1730. Shortest Path to Get Food
# V0
# IDEA : BFS
class Solution:
    def getFood(self, grid):
        # edge case
        if not grid:
            return 0
        l = len(grid)
        w = len(grid[0])
        #initx = inity = -1
        
        # step 1)
        # find (initx, inity) ("*" location)
        for i in range(l):
            for j in range(w):
                if grid[i][j] == "*":
                    initx = i
                    inity = j
                    break

        dirt = [(1,0),(0,1),(-1,0),(0,-1)]
        q = []
        
        # step 2) start from (initx, inity)
        q.append((initx, inity))
        step = 1
        seen = set()
        seen.add((initx,inity))
        # step 3) bfs
        while q:
            size = len(q)
            #print(q)
            for _ in range(size):
                x,y = q.pop(0)
                
                for dx,dy in dirt:
                    newx = x + dx
                    newy = y + dy
                    if 0 <= newx < l and 0 <= newy < w and (newx, newy) not in seen:
                        if grid[newx][newy] == '#':
                            return step
                        elif grid[newx][newy] == 'O':
                            q.append((newx, newy))
                            seen.add((newx,newy))
                        elif grid[newx][newy] == 'X':
                            seen.add((newx,newy))                          
            step += 1
        return -1
```

### 2-12) Kill Process
```python
# LC 582 Kill Process
# NOTE : there is also dfs approach
# V0
# IDEA : BFS + defaultdict
from collections import defaultdict
class Solution(object):
    def killProcess(self, pid, ppid, kill):
        d = defaultdict(set)
        for a, b in zip(ppid, pid):
            d[a].add(b)
        q = [kill]
        res = []
        while q:
            for i in range(len(q)):
                tmp = q.pop(-1)
                res.append(tmp)
                for _ in d[tmp]:
                    q.append(_)
        return res
```

### 2-13) Word Break
```python
# LC 139 Word Break
# NOTE : there is also dp approach
# V0
# IDEA : BFS
class Solution:
    def wordBreak(self, s, wordDict):
        if not s or not wordDict:
            return
        q = collections.deque()
        q.append(0)
        visited = [False]*len(s)
        while q:
            i = q.popleft()
            if not visited[i]:
                for j in range(i+1,len(s)+1):                 
                    if s[i:j] in wordDict:                    
                        if j == len(s):
                            return True  
                        q.append(j)
                visited[i]=True
        return False
```

### 2-14) Longest ZigZag Path in a Binary Tree
```python
# LC 1372. Longest ZigZag Path in a Binary Tree
# NOTE : there is also dfs, post order approaches
# V1
# IDEA : BFS
# https://leetcode.com/problems/longest-zigzag-path-in-a-binary-tree/discuss/534968/Python-BFS-solution-(100)
class Solution:
    def longestZigZag(self, root):
        q = [(root, 1, 0)] # node, direction, length; direction can be initialized to be 1 (to right child) or -1 (to left child)
        res = 0
        while q:
            node, direction, length = q.pop(0)
            while True:
                if direction == 1:
                    if node.left:
                        q.append((node.left, direction, 1))
                    if node.right:
                        node, direction, length = node.right, -direction, length + 1
                    else:
                        res = max(res, length)
                        break
                else:
                    if node.right:
                        q.append((node.right, direction, 1))
                    if node.left:
                        node, direction, length = node.left, -direction, length + 1
                    else:
                        res = max(res, length)
                        break
        return res
```

### 2-14) Coin Change
```python
# LC 322 Coin Change
# note : there is also dp approach
# IDEA :  BFS
class Solution(object):
    def coinChange(self, coins, amount):
        steps = collections.defaultdict(int)
        queue = collections.deque([0])
        steps[0] = 0
        while queue:
            front = queue.popleft()
            level = steps[front]
            if front == amount:
                return level
            for c in coins:
                if front + c > amount:
                    continue
                if front + c not in steps:
                    queue += front + c,
                    steps[front + c] = level + 1
        return -1
```