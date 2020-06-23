# BFS cheatsheet
- Shortest path searching
- Breadth first, then deep
- Usually choice data structure such as *queue*, *array*

## 0) Concept  

### 0-1) Framework

### 0-2) Pattern
```python
import colllections 
queue = colllections.deque()
queue.append(root)
while len(queue) > 0:
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
        for i in range(len(queue)):
            node=queue.popleft() 
            # do sth 
            if node.left:
                queue.append(node.left)
            if node.right:
                queue.append(node.right)
        return queue

# V2 : via python default
def bfs(root):
    if not root: return 
    q=[root]
    while len(q) > 0:
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