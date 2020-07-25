# DFS cheatsheet 
- To check is some value exists
- Inorder, postorder, postorder (can recreate a tree)
- Deep first, then breadth

## 0) Concept  

### 0-1) Framework

### 0-2) Pattern

```python
# python
def dfs(root, target):
    if root.val == target:
        # do sth
    if root.val < target:
       dfs(root.left, target)

    if root.val > target:
       dfs(root.right, target)
```

## 1) General form
```python
def dfs(root):
    # if root, do sth
    # if root, if no thing to do
    # else, do sth
    dfs(root.left)
    dfs(root.right)
```

### 1-1) Basic OP
```python
# Example) Add 1 to all node.value in Binary tree?
def dfs(root):
    if not root:
        return 
    root.val += 1 
    dfs(root.left)
    dfs(root.right)

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

## 2) LC Example

### 2-1) Validate Binary Search Tree
```python
# 098 Validate Binary Search Tree
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
class Solution(object):
    def insertIntoBST(self, root, val):
        if(root == None): return TreeNode(val);
        if(root.val < val): root.right = self.insertIntoBST(root.right, val);
        else: root.left = self.insertIntoBST(root.left, val);
        return(root)
```

### 2-3) Delete Node in a BST
```python
# 450 Delete Node in a BST
class Solution(object):
    def deleteNode(self, root, key):

        if not root:
            return root

        if root.val > key:
            root.left = self.deleteNode(root.left, key)
        elif root.val < key:
            root.right = self.deleteNode(root.right, key)
        else:
            if not root.left:
                right = root.right
                del root
                return right
            elif not root.right:
                left = root.left
                del root
                return left
            else:
                successor = root.right
                while successor.left:
                    successor = successor.left

                root.val = successor.val
                root.right = self.deleteNode(root.right, successor.val)
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
class Solution(object):
    def splitBST(self, root, V):
        if not root: return [None, None]
        if root.val > V:
            left, right = self.splitBST(root.left, V)
            root.left = right
            return [left, root]
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


### Ref
- https://github.com/labuladong/fucking-algorithm/blob/master/%E6%95%B0%E6%8D%AE%E7%BB%93%E6%9E%84%E7%B3%BB%E5%88%97/%E4%BA%8C%E5%8F%89%E6%90%9C%E7%B4%A2%E6%A0%91%E6%93%8D%E4%BD%9C%E9%9B%86%E9%94%A6.md