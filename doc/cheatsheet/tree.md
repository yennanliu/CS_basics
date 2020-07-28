# tree

> Tree data structure and algorithm/LC relative to it

## 0) Concept  

### 0-1) Framework
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

### 1-1) Basic OP
- pre-order traverse
    - root -> left -> right
- in-order traverse
    - left -> root -> right
- post-order traverse
    - left -> right -> root

## 2) LC Example
