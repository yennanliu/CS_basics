# BST (Binary Search Tree)

## 0) Concept

### 0-1) Types
- the property of BST : inorder traversal of BST is an array sorted in the ascending order. (LC 230)

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
## 1) General form

### 1-1) Basic OP

#### 1-1-1) add 1 to all nodes
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

#### 1-1-2) check if 2 BST are totally same 
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

    return isSameTree(root1.left, root2.right) && isSameTree(root1.right, root2.right)
}
```

#### 1-1-3) validate a BST
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
    if (max != null && root.val > max.val){
        return false;
    }
    return isValidBST(root.left, min, root) && isValidBST(root.right, root, max)
}
```

#### 1-1-4) find if a number is in BST

#### 1-1-5) insert a number into BST

#### 1-1-6) delete a number from BST


## 2) LC Example

### 2-1) Serialize and Deserialize BST
```python

# LC 449 Serialize and Deserialize BST

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