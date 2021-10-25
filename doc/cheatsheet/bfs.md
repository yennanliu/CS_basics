# BFS
- Shortest path searching
- Breadth first, then deep
- Usually choice data structure such as *queue*, *array*

## 0) Concept  

### 0-1) Types

### 0-2) Pattern
```python
# python
import colllections 
queue = colllections.deque()
queue.append(root)
while len(queue) > 0:
    ### NOTE THIS : `range(len(queue))`
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
            node = queue.popleft() 
            # do sth 
            if node.left:
                queue.append(node.left)
            if node.right:
                queue.append(node.right)
        return queue

# V2 : via python default
def bfs(root):
    if not root:
        return
    q =[]
    q.append(root)
    while root:
        for i in range(len(q)):
            root = q.pop()
            # do sth
            if root.left:
                q.append(root.left)
            if root.right:
                q.append(root.right)
    # do sth

# V3 : via python default
def bfs(root):
    if not root: return 
    q=[root]
    while len(q) > 0:
        ### NOTE this op
        for i in range(0,len(q)):
            # do sth
            if q[0].left:
                q.append(q[0].left)
            if q[0].right:
                q.append(q[0].right)
            del q[0]
    return q
```

### 1-1) Basic OP

## 2) LC Example

### 2-1) Binary Tree Level Order Traversal
```python
# 102 Binary Tree Level Order Traversal
# 107 Binary Tree Level Order Traversal II
# 103 Binary Tree Zigzag Level Order Traversal
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
# 127 Word Ladder
class Solution(object):
    def ladderLength(self, beginWord, endWord, wordList):

        wordset = set(wordList)
        bfs = collections.deque()
        bfs.append((beginWord, 1))
        while bfs:
            word, length = bfs.popleft()
            if word == endWord:
                return length
            for i in range(len(word)):
                for c in "abcdefghijklmnopqrstuvwxyz":
                    newWord = word[:i] + c + word[i + 1:]
                    if newWord in wordset and newWord != word:
                        wordset.remove(newWord)
                        bfs.append((newWord, length + 1))
        return 0

```
### 2-3) Closest Leaf in a Binary Tree
```python
# 742 Closest Leaf in a Binary Tree
import collections
class Solution:
    # search via DFS
    def findClosestLeaf(self, root, k):
        self.start = None
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
```

### 2-4) Surrounded Regions
```python
# 130 Surrounded Regions
# note : there is also a dfs solution
# IDEA : BFS
class Solution(object):
    def solve(self, board):
        import collections 
        if not board: return 
        l, w = len(board), len(board[0])
        q=collections.deque()

        # get the index of all O on the boarder
        for i in range(l):
            for j in range(w):
                if i in [0, l-1] or j in [0, w-1] and board[i][j] == "O":
                    q.append((i,j))

        # bfs, make the adjacent O into D
        while q:
            x,y = q.popleft()
            if  0 <= x < l and 0 <= y < w and board[x][y] == "O":
                board[x][y] = 'D' 
                for dx, dy in [(0,1), (0,-1),(1,0),(-1,0)]:
                    q.append((x+dx,y+dy))

        # make the rest of O into X and make the D into 0
        for i in range(l):
            for j in range(w):
                if board[i][j] == "O":
                    board[i][j] = "X"
                elif board[i][j] == "D":
                    board[i][j] = "O"
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