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
        """
        Stack : first in, last out 
        """
        node =  queue.pop() 
        # do something 
        queue.append(node.left)
        queue.append(node.right)
        res.append(node.val)
    return res 

# BFS  (Queue)
def bfs(root):	
    queue = collections.deque() # the data structure save input node 
    res = []  # the demand output 
    queue.append(root)
    while queue:
        # do something 
        """
        queue : first in, first out 
        """
        node =  queue.popleft()
        # do something 
        queue.append(node.left)
        queue.append(node.right)
        res.append(node.val)
    return res 

```
- Example 

```python

# DFS (Iteration)

# DFS (Recursion)

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