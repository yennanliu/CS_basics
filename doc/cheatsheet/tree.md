# Tree

> Tree data structure and algorithm/LC relative to it

## 0) Concept  

### 0-1) Types
- Tree
- BST (binary search tree)
- Heap
- Trie (dictionary tree)

### 0-2) Pattern
```python
# python
def traverse(TreeNode):
    for child in root.childern:
        # op in pre-traverse
        traverse(child)
        # op in post-traverse

# pre-order traverse
r = []
def pre_order_traverse(TreeNode):
    r.append(root.value)
    if root.left:
        pre_order_traverse(root.left)
    if root.right:
        pre_order_traverse(root.right)

# in-order traverse
r = []
def in_order_traverse(TreeNode):
    if root.left:
        in_order_traverse(root.left)
    r.append(root.value)
    if root.right:
        in_order_traverse(root.right)

# postorder traverse
r = []
def post_order_traverse(TreeNode):
    if root.left:
        post_order_traverse(root.left)
    if root.right:
        post_order_traverse(root.right)
    r.append(root.value)
```

## 1) General form

- When `tree` -> think about *recursion*

### 1-1) Basic OP
- pre-order traverse
    - root -> left -> right
- in-order traverse
    - left -> root -> right
- post-order traverse
    - left -> right -> root

## 2) LC Example

### 2-1) Binary Tree Right Side View
```python 
# LC 199 Binary Tree Right Side View
# V0
# IDEA : DFS
class Solution(object):
    def rightSideView(self, root):
        def dfs(root, layer):
            if not root:
                return
            if len(res) <= layer+1:
            #if len(res) == layer:     # this works as well
                res.append([])
            res[layer].append(root.val)
            if root.right:
                dfs(root.right, layer+1)
            if root.left:
                dfs(root.left, layer+1)
            
        if not root:
            return []
        res =[[]]
        dfs(root, 0)
        return [x[0] for x in res if len(x) > 0]
```