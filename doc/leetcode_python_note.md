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

# DFS (Recursion) 
# leetcode # 515 Find Largest Value in Each Tree Row
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

## 2) Binary Search Tree (BST)

- Tricks

- Algorithm 

- Example 

```python 
# Convert Sorted Array -> Binary  
# leetcode #108 
class Solution(object):
    def sortedArrayToBST(self, nums):
        """
        :type nums: List[int]
        :rtype: TreeNode
        """
        if not nums: return None
        _len = len(nums)
        mid = _len // 2
        root = TreeNode(nums[mid])
        root.left = self.sortedArrayToBST(nums[:mid])
        root.right = self.sortedArrayToBST(nums[mid+1:])
        return root
```

```python 
# linked list -> Binary Search Tree (BST)
class tree:
    def __init__(self):
        self.data = 0
        self.left = None
        self.right = None

class Solution(object):
    def linkedListToBST(self, root, val):
        newnode = tree()
        newnode.data = val
        newnode.left = None
        newnode.right = None 
        if root == None:
            root = newnode
            return root 
        else:
            current = root 
            while current != None:
                backup = current
                if current.data > val:
                    current = current.left
                else:
                    current = current.right 
                if backup.data > val:
                    backup.left = newnode
                else:
                    backup.right = newnode
        return root 
```

```python
# Binary Search Tree (BST) preorder/inorder/postorder traversal (linked list)
# leetcode #94 Binary Tree Inorder Traversal, 
# leetcode #144 Binary Tree Preorder Traversal

class tree:
    def __init__(self):
        self.data = 0
        self.left = None
        self.right = None

def preorder(ptr):
    if ptr != None:
        print (ptr.data)
        preorder(ptr.left)
        preorder(ptr.right)

def inorder(ptr):
    if ptr != None:
        inorder(ptr.left)
        print (prt.data)
        inorder(ptr.right)

def postorder(ptr):
    if ptr != None:
        postorder(ptr.left)
        postorder(ptr.right)
        print (ptr.data)

def linkedListToBST(root, val):
    newnode = tree()
    newnode.data = val
    newnode.left = None
    newnode.right = None 
    if root == None:
        root = newnode
        return root 
    else:
        current = root 
        while current != None:
            backup = current
            if current.data > val:
                current = current.left
            else:
                current = current.right 
            if backup.data > val:
                backup.left = newnode
            else:
                backup.right = newnode
    return root 

```