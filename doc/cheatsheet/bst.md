# BST (Binary Search Tree)
- Ref
    - [fucking-algorithm - BST pt.1](https://labuladong.github.io/algo/2/21/43/)
    - [fucking-algorithm - BST pt.1](https://labuladong.github.io/algo/2/21/44/)
    - [fucking-algorithm - BST pt.1](https://labuladong.github.io/algo/2/21/42/)

## 0) Concept

### 0-1) Types

- Types
    - the property of BST : inorder traversal of BST is an array sorted in the ascending order. (LC 230)
    - Trim BST
        - LC 669 (check [dfs.md](https://github.com/yennanliu/CS_basics/blob/master/doc/cheatsheet/dfs.md))

- Algorithm
    - dfs
    - recursive

- Data structure
    - TreeNode
    - dict
    - set

### 0-2) Pattern
```java
// java
public class TreeNode{
    // attr
    int val;  // value on node
    TreeNode left;  // point to left clild
    TreeNode right;  // point to right clild

    // constructor
    TreeNode(int val){
        this.val = val;
        this.left = null;
        this.right = null;
    }
}

// init a BST
TreeNode node1 = new TreeNode(2);
TreeNode node2 = new TreeNode(3);
TreeNode node3 = new TreeNode(4);

// modify node1's val
node1.val = 10;

// connect nodes
node1.left = node2;
node1.right = node3;
```

```python
# python
# Init a tree in py
class TreeNode(object):
    def __init__(self, val=0, left=None, right=None):
        self.val = val
        self.left = left
        self.right = right

root = TreeNode(0)
root.left = TreeNode(1)
root.right =  TreeNode(2)
print (root)
```

## 1) General form

### 1-1) Basic OP

#### 1-1-1) Add 1 to all nodes
```java
// java
void plusOne(TreeNode root){
    if (root == null){
        return;
    }
    root.val += 1;
    plusOne(root.left);
    plusOne(root.right);
}
```

```python
# python
class TreeNode(object):
    def __init__(self, val=0, left=None, right=None):
        self.val = val
        self.left = left
        self.right = right

root = TreeNode(0)
root.left = TreeNode(1)
root.right =  TreeNode(2)

print (root.val)
print (root.left.val)
print (root.right.val)

print("==============")

def add_one(root):
    if not root:
        return
    root.val += 1
    add_one(root.left)
    add_one(root.right)

add_one(root)
print (root.val)
print (root.left.val)
print (root.right.val)
```

#### 1-1-2) Check if 2 BST are totally the same 
```java
// java
boolean isSameTree(TreeNode root1, TreeNode root2){
    // if all null, then same
    if (root1 == null && root2 == null){
        return true;
    }
    if (root1 == null || root2 == null){
        return false;
    }
    if (root1.val != root2.val){
        return false;
    }

    return isSameTree(root1.left, root2.left) && isSameTree(root1.right, root2.right)
}
```

#### 1-1-3) Validate a BST
```java
// java
boolean isValidBST(TreeNode root){
    return isValidBST(root, null, null);
}

// help func
boolean isValidBST(TreeNode root, TreeNode min, TreeNode max){
    if (root == null){
        return true;
    }
    if (min != null && root.val <= min.val){
        return false;
    }
    if (max != null && root.val >= max.val){
        return false;
    }
    return isValidBST(root.left, min, root) && isValidBST(root.right, root, max)
}
```

#### 1-1-4) Find if a number is in BST
```java
// java
// V1 : general (for tree and BST)
boolean isInBST(TreeNode root, int target){
    if (root == null) return false;
    if (root.val == target) return target;

    return isInBST(root.left, target) || isInBST(root.right, target);
}

// V2 : optimization for BST
boolean isInBST(TreeNode root, int target){
    if (root == null) return false;
    if (root.val == target) return target;

    // optimize here
    if (root.val < target){
        return isInBST(root.right, target);
    }
    if (root.val > target){
        return isInBST(root.left, target);
    }
}

```

#### 1-1-5) Insert a number into BST
```java
// java
TreeNode insertIntoBST(TreeNode root, int val){
    // if null root, then just find a space and insert new value
    if (root == null) return new TreeNode(val);
    // if already exist, no need to insert, return directly
    if (root.val == val) return root;

    if (root.val < val){
        root.right = insertIntoBST(root.right, val);
    }
    if (root.val > val){
        root.left = insertIntoBST(root.left, val);
    }
    return root;
}
```

#### 1-1-6) Delete a number from BST
```java
// java

// pseudo java code
TreeNode deleteNode(TreeNode root, int key){
    if (root.val == key){
        // delete
    }
    else if (root.val > key){
        // to left sub tree
        root.left = deleteNode(root.left, key);
    }
    else if (root.val < key){
        // to right sub tree
        root.right = deleteNode(root.right, key);
    }
    return root;
}

/** 
 *   NOTE : 3 cases (algorithm book (labu) p.246)
 * 
 *   1) the value (to-delete value) is at the bottom (no sub left/right tree) -> delete directly
 *   
 *   2) there is only one left/right tree -> replace value with the sub-tree, then delete sub-tree
 * 
 *   3) there is BOTH left/right tree
 *      -> approach 3-1)  find the MIN right sub-tree and replace with value, then delete MIN right sub-tree
 *      -> approach 3-2)  find the MAX left sub-tree and replace with value, then delete MAX left sub-tree
 */

// java code
TreeNode deleteNode(TreeNode root, int key){
    if (root.val == key){
        // case 1) & case 2)
        if (root.left == null) return root.right;
        if (root.right == null) return root.left;
        
        // case 3)
        TreeNode minNode = getMin(root.right);
        root.val = minNode.val;
        root.right = deleteNode(root.right, key);
    }
    else if (root.val > key){
        // to left sub tree
        root.left = deleteNode(root.left, key);
    }
    else if (root.val < key){
        // to right sub tree
        root.right = deleteNode(root.right, key);
    }
    return root;
}

// help func
TreeNode getMin(TreeNode node){
    // min node is on the left of BST
    while (node.left != null) node = node.left;
    return node;
}
```

```python
# python code

# LC 450 Delete Node in a BST
# V0
# IDEA : RECURSION + BST PROPERTY
#### 2 CASES :
#   -> CASE 1 : root.val == key and NO right subtree 
#                -> swap root and root.left, return root.left
#   -> CASE 2 : root.val == key and THERE IS right subtree
#                -> 1) go to 1st RIGHT sub tree
#                -> 2) iterate to deepest LEFT subtree
#                -> 3) swap root and  `deepest LEFT subtree` then return root
class Solution(object):
    def deleteNode(self, root, key):
        if not root: return None
        if root.val == key:
            # case 1 : NO right subtree 
            if not root.right:
                left = root.left
                return left
            # case 2 : THERE IS right subtree
            else:
                ### NOTE : find min in "right" sub-tree
                #           -> because BST property, we ONLY go to 1st right tree (make sure we find the min of right sub-tree)
                #           -> then go to deepest left sub-tree
                right = root.right
                while right.left:
                    right = right.left
                root.val, right.val = right.val, root.val
        root.left = self.deleteNode(root.left, key)
        root.right = self.deleteNode(root.right, key)
        return root
```

## 2) LC Example

### 2-1) Serialize and Deserialize BST
```python
# LC 449 Serialize and Deserialize BST
# V0
# IDEA : BFS + queue op
class Codec:
    def serialize(self, root):
        if not root:
            return '{}'

        res = [root.val]
        q = [root]

        while q:
            new_q = []
            for i in range(len(q)):
                tmp = q.pop(0)
                if tmp.left:
                    q.append(tmp.left)
                    res.extend( [tmp.left.val] )
                else:
                    res.append('#')
                if tmp.right:
                    q.append(tmp.right)
                    res.extend( [tmp.right.val] )
                else:
                    res.append('#')

        while res and res[-1] == '#':
                    res.pop()

        return '{' + ','.join(map(str, res)) + '}' 


    def deserialize(self, data):
        if data == '{}':
            return

        nodes = [ TreeNode(x) for x in data[1:-1].split(",") ]
        root = nodes.pop(0)
        p = [root]
        while p:
            new_p = []
            for n in p:
                if nodes:
                    left_node = nodes.pop(0)
                    if left_node.val != '#':
                        n.left = left_node
                        new_p.append(n.left)
                    else:
                        n.left = None
                if nodes:
                    right_node = nodes.pop(0)
                    if right_node.val != '#':
                        n.right = right_node
                        new_p.append(n.right)
                    else:
                        n.right = None
            p = new_p 
             
        return root

# V1
# IDEA : same as LC 297
# https://leetcode.com/problems/serialize-and-deserialize-bst/discuss/93283/Python-solution-using-BST-property
class Codec:

    def serialize(self, root):
        vals = []
        self._preorder(root, vals)
        return ','.join(vals)
        
    def _preorder(self, node, vals):
        if node:
            vals.append(str(node.val))
            self._preorder(node.left, vals)
            self._preorder(node.right, vals)
        
    def deserialize(self, data):
        vals = collections.deque(map(int, data.split(','))) if data else []
        return self._build(vals, -float('inf'), float('inf'))

    def _build(self, vals, minVal, maxVal):
        if vals and minVal < vals[0] < maxVal:
            val = vals.popleft()
            root = TreeNode(val)
            root.left = self._build(vals, minVal, val)
            root.right = self._build(vals, val, maxVal)
            return root
        else:
            return None
```

### 2-2) Kth Smallest Element in a BST

```python
# LC 230
# V1'
# IDEA : Approach 2: Iterative Inorder Traversal
#        -> The above recursion could be converted into iteration, with the help of stack. This way one could speed up the solution because there is no need to build the entire inorder traversal, and one could stop after the kth element.
# https://leetcode.com/problems/kth-smallest-element-in-a-bst/solution/
class Solution:
    def kthSmallest(self, root, k):
        stack = []
        
        while True:
            while root:
                stack.append(root)
                root = root.left
            root = stack.pop()
            k -= 1
            if not k:
                return root.val
            root = root.right
```

### 2-3) Lowest Common Ancestor of a Binary Search Tree
```python
# LC 235 Lowest Common Ancestor of a Binary Search Tree
# V0
# IDEA : RECURSION + POST ORDER TRANSVERSAL
### NOTE : we need POST ORDER TRANSVERSAL for this problem
#          -> left -> right -> root
#          -> we can make sure that if p == q, then the root must be p and q's "common ancestor"
class Solution:
    def lowestCommonAncestor(self, root, p, q):
        ### NOTE : we need to assign root.val, p, q to other var first (before they are changed)
        # Value of current node or parent node.
        parent_val = root.val

        # Value of p
        p_val = p.val

        # Value of q
        q_val = q.val

        # If both p and q are greater than parent
        if p_val > parent_val and q_val > parent_val:
            ### NOTE : we need to `return` below func call   
            return self.lowestCommonAncestor(root.right, p, q)
        # If both p and q are lesser than parent
        elif p_val < parent_val and q_val < parent_val: 
            ### NOTE : we need to `return` below func call   
            return self.lowestCommonAncestor(root.left, p, q)
        # We have found the split point, i.e. the LCA node.
        else:
            ### NOTE : not root.val but root
            return root

# V0'
# IDEA : RECURSION + POST ORDER TRANSVERSAL
### NOTE : we need POST ORDER TRANSVERSAL for this problem
#          -> left -> right -> root
#          -> we can make sure that if p == q, then the root must be p and q's "common ancestor"
class Solution(object):
    def lowestCommonAncestor(self, root, p, q):
        if not root:
            return root

        p_val = p.val
        q_val = q.val

        if root.val < p_val and root.val < q_val:
            return self.lowestCommonAncestor(root.right, p, q)

        elif root.val > p_val and root.val > q_val:
            return self.lowestCommonAncestor(root.left, p, q)

        else:
            return root
```

### 2-4) Split BST
```python
# LC 776 Split BST
# V0
# IDEA : BST properties (left < root < right) + recursion
class Solution(object):
    def splitBST(self, root, V):
        if not root:
            return None, None
        ### NOTE : if root.val <= V
        elif root.val <= V:
            result = self.splitBST(root.right, V)
            root.right = result[0]
            return root, result[1]
        ### NOTE : if root.val > V
        else:
            result = self.splitBST(root.left, V)
            root.left = result[1]
            return result[0], root
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
        """
        NOTE !!! how we do inorder traversal here

        irOrder : left -> root -> right
        """
        self.inOrder(root.left)
        self.stack.append(root.val)
        self.inOrder(root.right)
    
    def hasNext(self):
        """
        :rtype: bool
        """
        return len(self.stack) > 0

    def next(self):
        """
        :rtype: int
        """
        return self.stack.pop(0)  # NOTE here
```

### 2-6) Search in a Binary Search Tree
```python
# LC 700 Search in a Binary Search Tree
# V1
# IDEA : RECURSION + BST property
# https://leetcode.com/problems/search-in-a-binary-search-tree/solution/
class Solution:
    def searchBST(self, root: TreeNode, val: int) -> TreeNode:
        if root is None or val == root.val:
            return root
        
        return self.searchBST(root.left, val) if val < root.val \
            else self.searchBST(root.right, val)

# V1'
# IDEA : ITERATION
# https://leetcode.com/problems/search-in-a-binary-search-tree/solution/
class Solution:
    def searchBST(self, root: TreeNode, val: int) -> TreeNode:
        while root is not None and root.val != val:
            root = root.left if val < root.val else root.right
        return root
```

### 2-7) Unique Binary Search Trees II
```python
# LC 95 Unique Binary Search Trees II
# V1
# IDEA : RECURSION
# https://leetcode.com/problems/unique-binary-search-trees-ii/solution/
class Solution:
    def generateTrees(self, n):
        """
        :type n: int
        :rtype: List[TreeNode]
        """
        def generate_trees(start, end):
            if start > end:
                return [None,]
            
            all_trees = []
            for i in range(start, end + 1):  # pick up a root
                # all possible left subtrees if i is choosen to be a root
                left_trees = generate_trees(start, i - 1)
                
                # all possible right subtrees if i is choosen to be a root
                right_trees = generate_trees(i + 1, end)
                
                # connect left and right subtrees to the root i
                for l in left_trees:
                    for r in right_trees:
                        current_tree = TreeNode(i)
                        current_tree.left = l
                        current_tree.right = r
                        all_trees.append(current_tree)
            
            return all_trees
        
        return generate_trees(1, n) if n else []
```