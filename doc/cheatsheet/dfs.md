# DFS cheatsheet 

### 1) General form
- dev

### 2-1)
- dev

### 2-2) Find Duplicate Subtrees
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

### 2-3) Trim a BST
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