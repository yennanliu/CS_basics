# Note for Leetcode Python 

## 1) BFS & DFS 

- Tricks 
    - DFS = Stack + go through
    - BFS = Queue + go through 

- Algorithm 

```python
import collections 

# DFS (Stack)
def dfs(root):    
    queue = collections.deque() # the data structure save input node 
    res = []  # the demand output 
    queue.append(root)
    while queue:
        # do something 
        node =  queue.pop()  ### Stack : first in, last out
        # do something 
        queue.append(node.left)
        queue.append(node.right)
        res.append(node.val)
    return res 

# BFS (Queue)
def bfs(root):    
    queue = collections.deque() # the data structure save input node 
    res = []  # the demand output 
    queue.append(root)
    while queue:
        # do something 
        node =  queue.popleft() ### queue : first in, first out 
        # do something 
        queue.append(node.left)
        queue.append(node.right)
        res.append(node.val)
    return res 

```
- Example 

```python

# DFS (Iteration)

# DFS (Recursion) : Leetcode #515 Find Largest Value in Each Tree Row
class Solution(object):

    def largestValues(self, root):
        levels = []
        self.dfs(root, levels, 0)
        return [ max(l) for l in levels ]

    def dfs(self, root, levels, level):
        if not root: 
            return
        if level == len(levels):
            levels.append([])
        levels[level].append(root.val)
        self.dfs(root.left, levels, level + 1)
        self.dfs(root.right, levels, level + 1)

# BFS (Iteration)

# BFS (Recursion)

```

```python
# Tree - DFS

# Tree- BFS  

# Graph- DFS

# Graph -BFS  

```

- Ref
    - https://github.com/yennanliu/CS_basics/blob/master/leetcode_python/Breadth-First-Search/walls-and-gates.py
    - https://github.com/yennanliu/CS_basics/blob/master/leetcode_python/Depth-First-Search/path-sum.py

### 2) Binary Search Tree 