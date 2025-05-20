# Binary Tree 

## 0) Concept

-  Definition : a binary tree is a tree data structure in which each node has *at most* two children, referred to as the left child and the right child.
    - BST (binary search tree), Heap.. are all binary tree
    - [wiki](https://en.wikipedia.org/wiki/Binary_tree)
    - [Binary Tree - 演算法筆記](https://web.ntnu.edu.tw/~algo/BinaryTree.html)

#### Completed Tree to Array

-  Note if we use an `array` to represent the `complete binary tree`,and `store the root node at index 1`
    - so, index of the `parent` node of any node is `[index of the node / 2]`
    - so, index of the `left child` node is `[index of the node * 2]`
    - so, index of the `right child` node is `[index of the node * 2 + 1]`
    - https://github.com/yennanliu/CS_basics/blob/master/data_structure/python/MinHeap.py#L36-L40
    - [video](https://leetcode.com/explore/learn/card/heap/643/heap/4017/) : very good explanation!!!
    - properties
        - how to store ? 
            - via Array and index
        - how to find the parent node ?
            - n / 2
            - NOTE : `n is "index"`
        - how to find the left and right children ?
            - left children : n * 2
            - right children : n * 2 + 1
        - how to check if a node is leaf node ?
            - check if i > (# of nodes) / 2
        - <p align="center"><img src ="https://github.com/yennanliu/CS_basics/blob/master/doc/pic/complete_tree_to_array.png" ></p>


#### Example:

Let's say you have a complete binary tree like this:

```
        10
       /  \
     15    20
    / \    /
   30 40  50
```

This tree as an **array (1-based)** would be:

```
Index:   1   2   3   4   5   6
Value: [10, 15, 20, 30, 40, 50]
```

Relationships:

* Node at index 2 (15)

  * Parent: 2 / 2 = 1 → 10
  * Left child: 2 * 2 = 4 → 30
  * Right child: 2 * 2 + 1 = 5 → 40

---


- Array to Complete Tree
    - dev

- `Complete binary tree`
    - A complete binary tree is a binary tree in which every level, `except possibly the last`, is completely filled, and all nodes in the last level are as far left as possible.
    - [wiki](https://en.wikipedia.org/wiki/Binary_tree#:~:text=A%20complete%20binary%20tree%20is,tree%20is%20not%20necessarily%20perfect.)
    - example :
        - complete binary tree
        <p align="center"><img src ="https://github.com/yennanliu/CS_basics/blob/master/doc/pic/complete_binary_tree1.png" ></p>
        - NOT complete binary tree
        <p align="center"><img src ="https://github.com/yennanliu/CS_basics/blob/master/doc/pic/not_complete_binary_tree.png" ></p>

### 0-1) Types

- Types

- Algorithm
    - dfs
    - recursive
    - tree op

- Data structure
    - TreeNode
    - dict
    - set
    - array

### 0-2) Pattern

### 0-2-1) Construct Binary Tree from Preorder and Inorder Traversal

- LC 105
- https://github.com/yennanliu/CS_basics/blob/master/leetcode_python/Recursion/construct-binary-tree-from-preorder-and-inorder-traversal.py

- NOTE !!!
    - Binary tree is `symmetric`, so the distance(left-sub-tree, root), distance(right-sub-tree, root) is the same
    - and the distance above is "idx"
    - `root` always at first element when Preorder

- Steps
    - step 1) get root val in Preorder
    - step 2) get root idx in Inorder
    - step 3) split sub left tree, sub right tree in Preorder, Inorder via root idx


```
Preorder :
        
            root ---- left ---- right
                 <-->     <-->
                  idx      idx


Inorder :
        
            left ---- root ---- right
                 <-->      <-->
                  idx      idx
``` 

so we can use below go represent left, right sub tree in Preorder and Inorder 

```python
# python

# Preorder:
# left sub tree : preorder[1 : index + 1]
# right sub tree : preorder[index + 1 : ]

# Inorder
# left sub tree : inorder[ : index]
# right sub tree : inorder[index + 1 :]
```


## 1) General form

### 1-1) Basic OP

## 2) LC Example

### 2-1) Construct Binary Tree from Preorder and Inorder Traversal
```python
# python
# LC 105. Construct Binary Tree from Preorder and Inorder Traversal
# V0
# IDEA : BST property
class Solution(object):
    def buildTree(self, preorder, inorder):
        if len(preorder) == 0:
            return None
        if len(preorder) == 1:
            return TreeNode(preorder[0])
        ### NOTE : init root like below (via TreeNode and root value (preorder[0]))
        root = TreeNode(preorder[0])
        """
        NOTE !!!
        -> # we get index of root.val from "INORDER" to SPLIT TREE
        """
        index = inorder.index(root.val)  # the index of root at inorder, and we can also get the length of left-sub-tree, right-sub-tree ( preorder[1:index+1]) for following using
        # recursion for root.left
        #### NOTE : the idx is from "INORDER"
        #### NOTE : WE idx from inorder in preorder as well 
        #### NOTE : preorder[1 : index + 1] (for left sub tree)
        root.left = self.buildTree(preorder[1 : index + 1], inorder[ : index]) ### since the BST is symmery so the length of left-sub-tree is same in both Preorder and Inorder, so we can use the index to get the left-sub-tree of Preorder as well
        # recursion for root.right 
        root.right = self.buildTree(preorder[index + 1 : ], inorder[index + 1 :]) ### since the BST is symmery so the length of left-sub-tree is same in both Preorder and Inorder, so we can use the index to get the right-sub-tree of Preorder as well
        return root
```

### 2-2) Construct Binary Tree from Inorder and Postorder Traversal
```python
# python
# LC 106 Construct Binary Tree from Inorder and Postorder Traversal
# V0
# IDEA : Binary Tree property, same as LC 105 
class Solution(object):
    def buildTree(self, inorder, postorder):
        if not inorder:
            return None
        if len(inorder) == 1:
            return TreeNode(inorder[0])
        ### NOTE : we get root from postorder
        root = TreeNode(postorder[-1])
        """
        ### NOTE : the index is from inorder
        ### NOTE : we get index of root in inorder
        #    -> and this idx CAN BE USED IN BOTH inorder, postorder (Binary Tree property)
        """
        idx = inorder.index(root.val)
        ### NOTE : inorder[:idx], postorder[:idx]
        root.left = self.buildTree(inorder[:idx], postorder[:idx])
        ### NOTE : postorder[idx:-1]
        root.right =  self.buildTree(inorder[idx+1:], postorder[idx:-1])
        return root
```


### 2-3) Binary Tree Paths
```python
# LC 257 Binary Tree Paths

# V0
# IDEA : BFS
class Solution:
    def binaryTreePaths(self, root):
        res = []
        ### NOTE : we set q like this : [[root, cur]]
        cur = ""
        q = [[root, cur]]
        while q:
            for i in range(len(q)):
                node, cur = q.pop(0)
                ### NOTE : if node exist, but no sub tree (i.e. not root.left and not root.right)
                #         -> append cur to result
                if node:
                    if not node.left and not node.right:
                        res.append(cur + str(node.val))
                ### NOTE : we keep cur to left sub tree
                if node.left:
                    q.append((node.left, cur + str(node.val) + '->'))
                ### NOTE : we keep cur to left sub tree
                if node.right:
                    q.append((node.right, cur + str(node.val) + '->'))
        return res

# V0'
# IDEA : DFS 
class Solution:
    def binaryTreePaths(self, root):
        ans = []
        def dfs(r, tmp):
            if r.left:
                dfs(r.left, tmp + [str(r.left.val)])
            if r.right:
                dfs(r.right, tmp + [str(r.right.val)])
            if not r.left and not r.right:
                ans.append('->'.join(tmp))
        if not root:
            return []
        dfs(root, [str(root.val)])
        return ans
```

### 2-4) Binary Tree Longest Consecutive Sequence
```python
# LC 298 Binary Tree Longest Consecutive Sequence
# V0
# IDEA : DFS
class Solution(object):
    def longestConsecutive(self, root):
        if not root:
            return 0

        self.result = 0
        self.helper(root, 1)

        return self.result

    def helper(self, root, curLen):
        self.result = curLen if curLen > self.result else self.result
        if root.left:
            if root.left.val == root.val + 1:
                self.helper(root.left, curLen + 1)
            else:
                self.helper(root.left, 1)
        if root.right:
            if root.right.val == root.val + 1:
                self.helper(root.right, curLen + 1)
            else:
                self.helper(root.right, 1)

# V0'
# IDEA : BFS
class Solution(object):
    def longestConsecutive(self, root):
        if root is None:
            return 0

        stack = list()
        stack.append((root, 1))
        maxLen = 1
        while len(stack) > 0:
            node, pathLen = stack.pop()
            if node.left is not None:
                if node.val + 1 == node.left.val:
                    stack.append((node.left, pathLen + 1))
                    maxLen = max(maxLen, pathLen + 1)
                else:
                    stack.append((node.left, 1))
            if node.right is not None:
                if node.val + 1 == node.right.val:
                    stack.append((node.right, pathLen + 1))
                    maxLen = max(maxLen, pathLen + 1)
                else:
                    stack.append((node.right, 1))

        return maxLen
```

### 2-5) Binary Search Tree Iterator
```python
# LC 173. Binary Search Tree Iterator

# V0
# IDEA : STACK + tree
class BSTIterator(object):
    def __init__(self, root):
        """
        :type root: TreeNode
        """
        self.stack = []
        self.inOrder(root)
    
    def inOrder(self, root):
        if not root:
            return
        self.inOrder(root.right)
        self.stack.append(root.val)
        self.inOrder(root.left)
    
    def hasNext(self):
        """
        :rtype: bool
        """
        return len(self.stack) > 0

    def next(self):
        """
        :rtype: int
        """
        return self.stack.pop() 
```
