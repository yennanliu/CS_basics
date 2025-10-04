# Tree Pattern Templates - Comprehensive Guide

## Overview

This document provides detailed templates for all tree problem patterns, organized by categories with example code, explanations, and corresponding LeetCode problems.

---

## 1) Tree Traversal Templates

### 1.1) Preorder Template

**Pattern**: Root → Left → Right
**Use Case**: When you need parent data before processing children
**Time Complexity**: O(n)
**Space Complexity**: O(h) for recursion stack

#### Template Code

```python
# Python - Recursive
def preorder_traversal(root):
    result = []

    def preorder(node):
        if not node:
            return

        # Process root first
        result.append(node.val)

        # Then left subtree
        preorder(node.left)

        # Then right subtree
        preorder(node.right)

    preorder(root)
    return result

# Python - Iterative
def preorder_iterative(root):
    if not root:
        return []

    result = []
    stack = [root]

    while stack:
        node = stack.pop()
        result.append(node.val)

        # Add right first (LIFO - will process left first)
        if node.right:
            stack.append(node.right)
        if node.left:
            stack.append(node.left)

    return result
```

```java
// Java - Recursive
public void preorderTraversal(TreeNode root, List<Integer> result) {
    if (root == null) return;

    result.add(root.val);              // Process root
    preorderTraversal(root.left, result);   // Left subtree
    preorderTraversal(root.right, result);  // Right subtree
}

// Java - Iterative
public List<Integer> preorderIterative(TreeNode root) {
    List<Integer> result = new ArrayList<>();
    if (root == null) return result;

    Stack<TreeNode> stack = new Stack<>();
    stack.push(root);

    while (!stack.isEmpty()) {
        TreeNode node = stack.pop();
        result.add(node.val);

        if (node.right != null) stack.push(node.right);
        if (node.left != null) stack.push(node.left);
    }

    return result;
}
```

#### LeetCode Problems
- LC 144: Binary Tree Preorder Traversal (Easy)
- LC 589: N-ary Tree Preorder Traversal (Easy)

---

### 1.2) Inorder Template

**Pattern**: Left → Root → Right
**Use Case**: BST sorted order, tree validation
**Time Complexity**: O(n)
**Space Complexity**: O(h)

#### Template Code

```python
# Python - Recursive
def inorder_traversal(root):
    result = []

    def inorder(node):
        if not node:
            return

        # Left subtree first
        inorder(node.left)

        # Process root
        result.append(node.val)

        # Right subtree
        inorder(node.right)

    inorder(root)
    return result

# Python - Iterative
def inorder_iterative(root):
    result = []
    stack = []
    current = root

    while stack or current:
        # Go to leftmost node
        while current:
            stack.append(current)
            current = current.left

        # Process current node
        current = stack.pop()
        result.append(current.val)

        # Move to right subtree
        current = current.right

    return result
```

```java
// Java - Recursive
public void inorderTraversal(TreeNode root, List<Integer> result) {
    if (root == null) return;

    inorderTraversal(root.left, result);    // Left subtree
    result.add(root.val);                   // Current node
    inorderTraversal(root.right, result);   // Right subtree
}

// Java - Iterative
public List<Integer> inorderIterative(TreeNode root) {
    List<Integer> result = new ArrayList<>();
    Stack<TreeNode> stack = new Stack<>();
    TreeNode current = root;

    while (!stack.isEmpty() || current != null) {
        while (current != null) {
            stack.push(current);
            current = current.left;
        }

        current = stack.pop();
        result.add(current.val);
        current = current.right;
    }

    return result;
}
```

#### LeetCode Problems
- LC 94: Binary Tree Inorder Traversal (Easy)
- LC 98: Validate Binary Search Tree (Medium)
- LC 230: Kth Smallest Element in a BST (Medium)

---

### 1.3) Postorder Template

**Pattern**: Left → Right → Root
**Use Case**: Need children data before parent processing
**Time Complexity**: O(n)
**Space Complexity**: O(h)

#### Template Code

```python
# Python - Recursive
def postorder_traversal(root):
    result = []

    def postorder(node):
        if not node:
            return

        # Left subtree first
        postorder(node.left)

        # Right subtree
        postorder(node.right)

        # Process root last
        result.append(node.val)

    postorder(root)
    return result

# Python - Iterative (Two Stacks)
def postorder_iterative(root):
    if not root:
        return []

    stack1 = [root]
    stack2 = []

    # Collect nodes in reverse postorder
    while stack1:
        node = stack1.pop()
        stack2.append(node)

        if node.left:
            stack1.append(node.left)
        if node.right:
            stack1.append(node.right)

    # Pop from stack2 to get postorder
    result = []
    while stack2:
        result.append(stack2.pop().val)

    return result
```

```java
// Java - Recursive
public void postorderTraversal(TreeNode root, List<Integer> result) {
    if (root == null) return;

    postorderTraversal(root.left, result);   // Left subtree
    postorderTraversal(root.right, result);  // Right subtree
    result.add(root.val);                    // Current node
}
```

#### LeetCode Problems
- LC 145: Binary Tree Postorder Traversal (Easy)
- LC 590: N-ary Tree Postorder Traversal (Easy)

---

### 1.4) BFS Template (Level-order)

**Pattern**: Process nodes level by level
**Use Case**: Shortest path, level-based problems
**Time Complexity**: O(n)
**Space Complexity**: O(w) where w is max width

#### Template Code

```python
# Python - BFS with Level Grouping
from collections import deque

def level_order_traversal(root):
    if not root:
        return []

    result = []
    queue = deque([root])

    while queue:
        level_size = len(queue)
        current_level = []

        for _ in range(level_size):
            node = queue.popleft()
            current_level.append(node.val)

            if node.left:
                queue.append(node.left)
            if node.right:
                queue.append(node.right)

        result.append(current_level)

    return result

# Python - Simple BFS (Flat List)
def level_order_simple(root):
    if not root:
        return []

    result = []
    queue = deque([root])

    while queue:
        node = queue.popleft()
        result.append(node.val)

        if node.left:
            queue.append(node.left)
        if node.right:
            queue.append(node.right)

    return result
```

```java
// Java - BFS with Level Grouping
public List<List<Integer>> levelOrder(TreeNode root) {
    List<List<Integer>> result = new ArrayList<>();
    if (root == null) return result;

    Queue<TreeNode> queue = new LinkedList<>();
    queue.offer(root);

    while (!queue.isEmpty()) {
        int levelSize = queue.size();
        List<Integer> currentLevel = new ArrayList<>();

        for (int i = 0; i < levelSize; i++) {
            TreeNode node = queue.poll();
            currentLevel.add(node.val);

            if (node.left != null) queue.offer(node.left);
            if (node.right != null) queue.offer(node.right);
        }

        result.add(currentLevel);
    }

    return result;
}
```

#### LeetCode Problems
- LC 102: Binary Tree Level Order Traversal (Medium)
- LC 107: Binary Tree Level Order Traversal II (Medium)
- LC 103: Binary Tree Zigzag Level Order Traversal (Medium)
- LC 199: Binary Tree Right Side View (Medium)

---

### 1.5) BFS + Direction Template

**Pattern**: Alternating direction per level
**Use Case**: Zigzag traversal
**Time Complexity**: O(n)
**Space Complexity**: O(w)

#### Template Code

```python
# Python - Zigzag Level Order
from collections import deque

def zigzag_level_order(root):
    if not root:
        return []

    result = []
    queue = deque([root])
    left_to_right = True

    while queue:
        level_size = len(queue)
        current_level = deque()

        for _ in range(level_size):
            node = queue.popleft()

            # Add to level based on direction
            if left_to_right:
                current_level.append(node.val)
            else:
                current_level.appendleft(node.val)

            if node.left:
                queue.append(node.left)
            if node.right:
                queue.append(node.right)

        result.append(list(current_level))
        left_to_right = not left_to_right

    return result
```

```java
// Java - Zigzag Level Order
public List<List<Integer>> zigzagLevelOrder(TreeNode root) {
    List<List<Integer>> result = new ArrayList<>();
    if (root == null) return result;

    Queue<TreeNode> queue = new LinkedList<>();
    queue.offer(root);
    boolean leftToRight = true;

    while (!queue.isEmpty()) {
        int levelSize = queue.size();
        LinkedList<Integer> currentLevel = new LinkedList<>();

        for (int i = 0; i < levelSize; i++) {
            TreeNode node = queue.poll();

            if (leftToRight) {
                currentLevel.addLast(node.val);
            } else {
                currentLevel.addFirst(node.val);
            }

            if (node.left != null) queue.offer(node.left);
            if (node.right != null) queue.offer(node.right);
        }

        result.add(currentLevel);
        leftToRight = !leftToRight;
    }

    return result;
}
```

#### LeetCode Problems
- LC 103: Binary Tree Zigzag Level Order Traversal (Medium)

---

## 2) Tree Property Templates

### 2.1) Postorder Height Template

**Pattern**: Calculate height bottom-up
**Use Case**: Tree height/depth calculation
**Time Complexity**: O(n)
**Space Complexity**: O(h)

#### Template Code

```python
# Python - Height Calculation
def max_depth(root):
    if not root:
        return 0

    left_height = max_depth(root.left)
    right_height = max_depth(root.right)

    return 1 + max(left_height, right_height)
```

```java
// Java - Height Calculation
public int maxDepth(TreeNode root) {
    if (root == null) {
        return 0;
    }

    int leftHeight = maxDepth(root.left);
    int rightHeight = maxDepth(root.right);

    return 1 + Math.max(leftHeight, rightHeight);
}
```

#### LeetCode Problems
- LC 104: Maximum Depth of Binary Tree (Easy)

---

### 2.2) BFS Early Stop Template

**Pattern**: Stop when condition met
**Use Case**: Minimum depth to leaf
**Time Complexity**: O(n) worst case, better in practice
**Space Complexity**: O(w)

#### Template Code

```python
# Python - Minimum Depth
from collections import deque

def min_depth(root):
    if not root:
        return 0

    queue = deque([(root, 1)])

    while queue:
        node, depth = queue.popleft()

        # Found first leaf - return immediately
        if not node.left and not node.right:
            return depth

        if node.left:
            queue.append((node.left, depth + 1))
        if node.right:
            queue.append((node.right, depth + 1))

    return 0
```

```java
// Java - Minimum Depth
public int minDepth(TreeNode root) {
    if (root == null) return 0;

    Queue<TreeNode> queue = new LinkedList<>();
    queue.offer(root);
    int depth = 1;

    while (!queue.isEmpty()) {
        int levelSize = queue.size();

        for (int i = 0; i < levelSize; i++) {
            TreeNode node = queue.poll();

            if (node.left == null && node.right == null) {
                return depth;
            }

            if (node.left != null) queue.offer(node.left);
            if (node.right != null) queue.offer(node.right);
        }

        depth++;
    }

    return depth;
}
```

#### LeetCode Problems
- LC 111: Minimum Depth of Binary Tree (Easy)

---

### 2.3) Height Validation Template

**Pattern**: Validate tree properties during height calculation
**Use Case**: Check if tree is balanced
**Time Complexity**: O(n)
**Space Complexity**: O(h)

#### Template Code

```python
# Python - Balanced Tree Check
def is_balanced(root):
    def check_height(node):
        if not node:
            return 0

        left_height = check_height(node.left)
        if left_height == -1:
            return -1

        right_height = check_height(node.right)
        if right_height == -1:
            return -1

        # Check balance condition
        if abs(left_height - right_height) > 1:
            return -1

        return 1 + max(left_height, right_height)

    return check_height(root) != -1
```

```java
// Java - Balanced Tree Check
public boolean isBalanced(TreeNode root) {
    return checkHeight(root) != -1;
}

private int checkHeight(TreeNode node) {
    if (node == null) {
        return 0;
    }

    int leftHeight = checkHeight(node.left);
    if (leftHeight == -1) return -1;

    int rightHeight = checkHeight(node.right);
    if (rightHeight == -1) return -1;

    if (Math.abs(leftHeight - rightHeight) > 1) {
        return -1;
    }

    return 1 + Math.max(leftHeight, rightHeight);
}
```

#### LeetCode Problems
- LC 110: Balanced Binary Tree (Easy)

---

### 2.4) Mirror Validation Template

**Pattern**: Compare symmetric subtrees
**Use Case**: Check if tree is symmetric
**Time Complexity**: O(n)
**Space Complexity**: O(h)

#### Template Code

```python
# Python - Symmetric Tree
def is_symmetric(root):
    def is_mirror(left, right):
        if not left and not right:
            return True
        if not left or not right:
            return False

        return (left.val == right.val and
                is_mirror(left.left, right.right) and
                is_mirror(left.right, right.left))

    if not root:
        return True
    return is_mirror(root.left, root.right)
```

```java
// Java - Symmetric Tree
public boolean isSymmetric(TreeNode root) {
    if (root == null) return true;
    return isMirror(root.left, root.right);
}

private boolean isMirror(TreeNode left, TreeNode right) {
    if (left == null && right == null) return true;
    if (left == null || right == null) return false;

    return left.val == right.val &&
           isMirror(left.left, right.right) &&
           isMirror(left.right, right.left);
}
```

#### LeetCode Problems
- LC 101: Symmetric Tree (Easy)

---

### 2.5) Tree Comparison Template

**Pattern**: Compare two trees node by node
**Use Case**: Check if two trees are identical
**Time Complexity**: O(n)
**Space Complexity**: O(h)

#### Template Code

```python
# Python - Same Tree
def is_same_tree(p, q):
    if not p and not q:
        return True
    if not p or not q:
        return False

    return (p.val == q.val and
            is_same_tree(p.left, q.left) and
            is_same_tree(p.right, q.right))
```

```java
// Java - Same Tree
public boolean isSameTree(TreeNode p, TreeNode q) {
    if (p == null && q == null) return true;
    if (p == null || q == null) return false;

    return p.val == q.val &&
           isSameTree(p.left, q.left) &&
           isSameTree(p.right, q.right);
}
```

#### LeetCode Problems
- LC 100: Same Tree (Easy)
- LC 572: Subtree of Another Tree (Easy)

---

## 3) Path-Based Templates

### 3.1) Global Max Update Template

**Pattern**: Track global maximum during traversal
**Use Case**: Maximum path sum problems
**Time Complexity**: O(n)
**Space Complexity**: O(h)

#### Template Code

```python
# Python - Binary Tree Maximum Path Sum
def max_path_sum(root):
    max_sum = float('-inf')

    def max_gain(node):
        nonlocal max_sum

        if not node:
            return 0

        # Max sum on left and right (ignore negative)
        left_gain = max(max_gain(node.left), 0)
        right_gain = max(max_gain(node.right), 0)

        # Update global max with path through current node
        current_path_sum = node.val + left_gain + right_gain
        max_sum = max(max_sum, current_path_sum)

        # Return max gain if continue from this node
        return node.val + max(left_gain, right_gain)

    max_gain(root)
    return max_sum
```

```java
// Java - Binary Tree Maximum Path Sum
private int maxSum = Integer.MIN_VALUE;

public int maxPathSum(TreeNode root) {
    maxGain(root);
    return maxSum;
}

private int maxGain(TreeNode node) {
    if (node == null) return 0;

    int leftGain = Math.max(maxGain(node.left), 0);
    int rightGain = Math.max(maxGain(node.right), 0);

    int currentPathSum = node.val + leftGain + rightGain;
    maxSum = Math.max(maxSum, currentPathSum);

    return node.val + Math.max(leftGain, rightGain);
}
```

#### LeetCode Problems
- LC 124: Binary Tree Maximum Path Sum (Hard)

---

### 3.2) Path Accumulation Template

**Pattern**: Track sum along path
**Use Case**: Check if path sum exists
**Time Complexity**: O(n)
**Space Complexity**: O(h)

#### Template Code

```python
# Python - Path Sum
def has_path_sum(root, target_sum):
    if not root:
        return False

    # Leaf node - check if sum matches
    if not root.left and not root.right:
        return root.val == target_sum

    # Recurse with updated target
    remaining = target_sum - root.val
    return (has_path_sum(root.left, remaining) or
            has_path_sum(root.right, remaining))
```

```java
// Java - Path Sum
public boolean hasPathSum(TreeNode root, int targetSum) {
    if (root == null) {
        return false;
    }

    if (root.left == null && root.right == null) {
        return root.val == targetSum;
    }

    int remaining = targetSum - root.val;
    return hasPathSum(root.left, remaining) ||
           hasPathSum(root.right, remaining);
}
```

#### LeetCode Problems
- LC 112: Path Sum (Easy)

---

### 3.3) Path + Backtrack Template

**Pattern**: Collect all paths with backtracking
**Use Case**: Find all paths matching criteria
**Time Complexity**: O(n)
**Space Complexity**: O(h)

#### Template Code

```python
# Python - Path Sum II
def path_sum(root, target_sum):
    result = []

    def dfs(node, remaining, path):
        if not node:
            return

        path.append(node.val)

        if not node.left and not node.right and remaining == node.val:
            result.append(path[:])

        dfs(node.left, remaining - node.val, path)
        dfs(node.right, remaining - node.val, path)

        path.pop()  # Backtrack

    dfs(root, target_sum, [])
    return result
```

```java
// Java - Path Sum II
public List<List<Integer>> pathSum(TreeNode root, int targetSum) {
    List<List<Integer>> result = new ArrayList<>();
    List<Integer> path = new ArrayList<>();
    dfs(root, targetSum, path, result);
    return result;
}

private void dfs(TreeNode node, int remaining, List<Integer> path,
                 List<List<Integer>> result) {
    if (node == null) return;

    path.add(node.val);

    if (node.left == null && node.right == null && remaining == node.val) {
        result.add(new ArrayList<>(path));
    }

    dfs(node.left, remaining - node.val, path, result);
    dfs(node.right, remaining - node.val, path, result);

    path.remove(path.size() - 1);  // Backtrack
}
```

#### LeetCode Problems
- LC 113: Path Sum II (Medium)
- LC 257: Binary Tree Paths (Easy)

---

### 3.4) Path Count Tracking Template

**Pattern**: Count paths using prefix sum
**Use Case**: Paths with target sum (any start/end)
**Time Complexity**: O(n)
**Space Complexity**: O(n)

#### Template Code

```python
# Python - Path Sum III
def path_sum(root, target_sum):
    def dfs(node, current_sum):
        if not node:
            return 0

        current_sum += node.val

        # Count paths ending at current node
        count = prefix_sum.get(current_sum - target_sum, 0)

        # Add current sum to map
        prefix_sum[current_sum] = prefix_sum.get(current_sum, 0) + 1

        # Recurse to children
        count += dfs(node.left, current_sum)
        count += dfs(node.right, current_sum)

        # Backtrack
        prefix_sum[current_sum] -= 1

        return count

    prefix_sum = {0: 1}
    return dfs(root, 0)
```

```java
// Java - Path Sum III
private int count = 0;

public int pathSum(TreeNode root, int targetSum) {
    Map<Long, Integer> prefixSum = new HashMap<>();
    prefixSum.put(0L, 1);
    dfs(root, 0L, targetSum, prefixSum);
    return count;
}

private void dfs(TreeNode node, long currentSum, int targetSum,
                 Map<Long, Integer> prefixSum) {
    if (node == null) return;

    currentSum += node.val;

    count += prefixSum.getOrDefault(currentSum - targetSum, 0);

    prefixSum.put(currentSum, prefixSum.getOrDefault(currentSum, 0) + 1);

    dfs(node.left, currentSum, targetSum, prefixSum);
    dfs(node.right, currentSum, targetSum, prefixSum);

    prefixSum.put(currentSum, prefixSum.get(currentSum) - 1);
}
```

#### LeetCode Problems
- LC 437: Path Sum III (Medium)

---

### 3.5) Path Value Building Template

**Pattern**: Build value from root to leaf
**Use Case**: Calculate number from root-to-leaf path
**Time Complexity**: O(n)
**Space Complexity**: O(h)

#### Template Code

```python
# Python - Sum Root to Leaf Numbers
def sum_numbers(root):
    def dfs(node, current_number):
        if not node:
            return 0

        current_number = current_number * 10 + node.val

        # Leaf node - return the number
        if not node.left and not node.right:
            return current_number

        # Sum from both subtrees
        return dfs(node.left, current_number) + dfs(node.right, current_number)

    return dfs(root, 0)
```

```java
// Java - Sum Root to Leaf Numbers
public int sumNumbers(TreeNode root) {
    return dfs(root, 0);
}

private int dfs(TreeNode node, int currentNumber) {
    if (node == null) return 0;

    currentNumber = currentNumber * 10 + node.val;

    if (node.left == null && node.right == null) {
        return currentNumber;
    }

    return dfs(node.left, currentNumber) + dfs(node.right, currentNumber);
}
```

#### LeetCode Problems
- LC 129: Sum Root to Leaf Numbers (Medium)

---

### 3.6) Path State Tracking Template

**Pattern**: Track maximum value along path
**Use Case**: Count good nodes (nodes >= max in path)
**Time Complexity**: O(n)
**Space Complexity**: O(h)

#### Template Code

```python
# Python - Count Good Nodes
def good_nodes(root):
    def dfs(node, max_so_far):
        if not node:
            return 0

        count = 1 if node.val >= max_so_far else 0

        new_max = max(max_so_far, node.val)
        count += dfs(node.left, new_max)
        count += dfs(node.right, new_max)

        return count

    return dfs(root, float('-inf'))
```

```java
// Java - Count Good Nodes
public int goodNodes(TreeNode root) {
    return dfs(root, Integer.MIN_VALUE);
}

private int dfs(TreeNode node, int maxSoFar) {
    if (node == null) return 0;

    int count = node.val >= maxSoFar ? 1 : 0;

    int newMax = Math.max(maxSoFar, node.val);
    count += dfs(node.left, newMax);
    count += dfs(node.right, newMax);

    return count;
}
```

#### LeetCode Problems
- LC 1448: Count Good Nodes in Binary Tree (Medium)

---

### 3.7) Longest Path Template

**Pattern**: Find longest path between any two nodes
**Use Case**: Diameter of tree
**Time Complexity**: O(n)
**Space Complexity**: O(h)

#### Template Code

```python
# Python - Diameter of Binary Tree
def diameter_of_binary_tree(root):
    diameter = 0

    def depth(node):
        nonlocal diameter

        if not node:
            return 0

        left_depth = depth(node.left)
        right_depth = depth(node.right)

        # Update diameter
        diameter = max(diameter, left_depth + right_depth)

        return 1 + max(left_depth, right_depth)

    depth(root)
    return diameter
```

```java
// Java - Diameter of Binary Tree
private int diameter = 0;

public int diameterOfBinaryTree(TreeNode root) {
    depth(root);
    return diameter;
}

private int depth(TreeNode node) {
    if (node == null) return 0;

    int leftDepth = depth(node.left);
    int rightDepth = depth(node.right);

    diameter = Math.max(diameter, leftDepth + rightDepth);

    return 1 + Math.max(leftDepth, rightDepth);
}
```

#### LeetCode Problems
- LC 543: Diameter of Binary Tree (Easy)

---

### 3.8) Same Value Path Template

**Pattern**: Find longest path with same values
**Use Case**: Longest univalue path
**Time Complexity**: O(n)
**Space Complexity**: O(h)

#### Template Code

```python
# Python - Longest Univalue Path
def longest_univalue_path(root):
    longest = 0

    def dfs(node):
        nonlocal longest

        if not node:
            return 0

        left_length = dfs(node.left)
        right_length = dfs(node.right)

        left_path = left_length + 1 if node.left and node.left.val == node.val else 0
        right_path = right_length + 1 if node.right and node.right.val == node.val else 0

        longest = max(longest, left_path + right_path)

        return max(left_path, right_path)

    dfs(root)
    return longest
```

```java
// Java - Longest Univalue Path
private int longest = 0;

public int longestUnivaluePath(TreeNode root) {
    dfs(root);
    return longest;
}

private int dfs(TreeNode node) {
    if (node == null) return 0;

    int leftLength = dfs(node.left);
    int rightLength = dfs(node.right);

    int leftPath = 0, rightPath = 0;

    if (node.left != null && node.left.val == node.val) {
        leftPath = leftLength + 1;
    }
    if (node.right != null && node.right.val == node.val) {
        rightPath = rightLength + 1;
    }

    longest = Math.max(longest, leftPath + rightPath);

    return Math.max(leftPath, rightPath);
}
```

#### LeetCode Problems
- LC 687: Longest Univalue Path (Medium)

---

## 4) Distance and LCA Templates

### 4.1) LCA Standard Template

**Pattern**: Find lowest common ancestor using postorder
**Use Case**: Find LCA in binary tree
**Time Complexity**: O(n)
**Space Complexity**: O(h)

#### Template Code

```python
# Python - Lowest Common Ancestor
def lowest_common_ancestor(root, p, q):
    if not root or root == p or root == q:
        return root

    left = lowest_common_ancestor(root.left, p, q)
    right = lowest_common_ancestor(root.right, p, q)

    # Found both in different subtrees
    if left and right:
        return root

    # Return non-null child
    return left if left else right
```

```java
// Java - Lowest Common Ancestor
public TreeNode lowestCommonAncestor(TreeNode root, TreeNode p, TreeNode q) {
    if (root == null || root == p || root == q) {
        return root;
    }

    TreeNode left = lowestCommonAncestor(root.left, p, q);
    TreeNode right = lowestCommonAncestor(root.right, p, q);

    if (left != null && right != null) {
        return root;
    }

    return left != null ? left : right;
}
```

#### LeetCode Problems
- LC 236: Lowest Common Ancestor of a Binary Tree (Medium)

---

### 4.2) Value Comparison Template

**Pattern**: Use BST property for LCA
**Use Case**: Find LCA in BST
**Time Complexity**: O(h)
**Space Complexity**: O(1) iterative

#### Template Code

```python
# Python - LCA of BST
def lowest_common_ancestor_bst(root, p, q):
    while root:
        # Both in left subtree
        if p.val < root.val and q.val < root.val:
            root = root.left
        # Both in right subtree
        elif p.val > root.val and q.val > root.val:
            root = root.right
        else:
            # Found split point
            return root

    return None
```

```java
// Java - LCA of BST
public TreeNode lowestCommonAncestor(TreeNode root, TreeNode p, TreeNode q) {
    while (root != null) {
        if (p.val < root.val && q.val < root.val) {
            root = root.left;
        } else if (p.val > root.val && q.val > root.val) {
            root = root.right;
        } else {
            return root;
        }
    }

    return null;
}
```

#### LeetCode Problems
- LC 235: Lowest Common Ancestor of a Binary Search Tree (Easy)

---

### 4.3) Path Distance Template

**Pattern**: Find distance using LCA
**Use Case**: Distance between two nodes
**Time Complexity**: O(n)
**Space Complexity**: O(h)

#### Template Code

```python
# Python - Find Distance in Binary Tree
def find_distance(root, p, q):
    def find_lca(node, p, q):
        if not node or node.val == p or node.val == q:
            return node

        left = find_lca(node.left, p, q)
        right = find_lca(node.right, p, q)

        if left and right:
            return node
        return left if left else right

    def get_distance(node, target):
        if not node:
            return -1
        if node.val == target:
            return 0

        left_dist = get_distance(node.left, target)
        right_dist = get_distance(node.right, target)

        if left_dist != -1:
            return left_dist + 1
        if right_dist != -1:
            return right_dist + 1
        return -1

    lca = find_lca(root, p, q)
    return get_distance(lca, p) + get_distance(lca, q)
```

```java
// Java - Find Distance in Binary Tree
public int findDistance(TreeNode root, int p, int q) {
    TreeNode lca = findLCA(root, p, q);
    return getDistance(lca, p) + getDistance(lca, q);
}

private TreeNode findLCA(TreeNode node, int p, int q) {
    if (node == null || node.val == p || node.val == q) {
        return node;
    }

    TreeNode left = findLCA(node.left, p, q);
    TreeNode right = findLCA(node.right, p, q);

    if (left != null && right != null) return node;
    return left != null ? left : right;
}

private int getDistance(TreeNode node, int target) {
    if (node == null) return -1;
    if (node.val == target) return 0;

    int leftDist = getDistance(node.left, target);
    int rightDist = getDistance(node.right, target);

    if (leftDist != -1) return leftDist + 1;
    if (rightDist != -1) return rightDist + 1;
    return -1;
}
```

#### LeetCode Problems
- LC 1740: Find Distance in a Binary Tree (Medium)

---

### 4.4) Tree to Graph Template

**Pattern**: Convert tree to graph for distance queries
**Use Case**: Find nodes at distance K
**Time Complexity**: O(n)
**Space Complexity**: O(n)

#### Template Code

```python
# Python - All Nodes Distance K
from collections import defaultdict, deque

def distance_k(root, target, k):
    # Build graph
    graph = defaultdict(list)

    def build_graph(parent, child):
        if parent and child:
            graph[parent.val].append(child.val)
            graph[child.val].append(parent.val)

        if child.left:
            build_graph(child, child.left)
        if child.right:
            build_graph(child, child.right)

    build_graph(None, root)

    # BFS from target
    result = []
    visited = {target.val}
    queue = deque([(target.val, 0)])

    while queue:
        node, distance = queue.popleft()

        if distance == k:
            result.append(node)
            continue

        for neighbor in graph[node]:
            if neighbor not in visited:
                visited.add(neighbor)
                queue.append((neighbor, distance + 1))

    return result
```

```java
// Java - All Nodes Distance K
Map<Integer, List<Integer>> graph = new HashMap<>();

public List<Integer> distanceK(TreeNode root, TreeNode target, int k) {
    buildGraph(null, root);

    List<Integer> result = new ArrayList<>();
    Set<Integer> visited = new HashSet<>();
    Queue<int[]> queue = new LinkedList<>();

    queue.offer(new int[]{target.val, 0});
    visited.add(target.val);

    while (!queue.isEmpty()) {
        int[] current = queue.poll();
        int node = current[0];
        int distance = current[1];

        if (distance == k) {
            result.add(node);
            continue;
        }

        for (int neighbor : graph.getOrDefault(node, new ArrayList<>())) {
            if (!visited.contains(neighbor)) {
                visited.add(neighbor);
                queue.offer(new int[]{neighbor, distance + 1});
            }
        }
    }

    return result;
}

private void buildGraph(TreeNode parent, TreeNode child) {
    if (parent != null && child != null) {
        graph.computeIfAbsent(parent.val, k -> new ArrayList<>()).add(child.val);
        graph.computeIfAbsent(child.val, k -> new ArrayList<>()).add(parent.val);
    }

    if (child != null) {
        if (child.left != null) buildGraph(child, child.left);
        if (child.right != null) buildGraph(child, child.right);
    }
}
```

#### LeetCode Problems
- LC 863: All Nodes Distance K in Binary Tree (Medium)

---

## 5) Height and Depth Templates

### 5.1) Height Calculation Template

**Pattern**: Bottom-up height calculation
**Use Case**: Get tree height
**Time Complexity**: O(n)
**Space Complexity**: O(h)

#### Template Code

```python
# Python - Height Calculation
def height(root):
    if not root:
        return 0

    return 1 + max(height(root.left), height(root.right))
```

```java
// Java - Height Calculation
public int height(TreeNode root) {
    if (root == null) {
        return 0;
    }

    return 1 + Math.max(height(root.left), height(root.right));
}
```

#### LeetCode Problems
- LC 104: Maximum Depth of Binary Tree (Easy)

---

### 5.2) Depth to Leaf Template

**Pattern**: Find minimum depth to leaf
**Use Case**: Shortest path to leaf
**Time Complexity**: O(n)
**Space Complexity**: O(h)

#### Template Code

```python
# Python - Minimum Depth DFS
def min_depth(root):
    if not root:
        return 0

    # If one child is missing, only consider the other
    if not root.left:
        return 1 + min_depth(root.right)
    if not root.right:
        return 1 + min_depth(root.left)

    return 1 + min(min_depth(root.left), min_depth(root.right))
```

```java
// Java - Minimum Depth DFS
public int minDepth(TreeNode root) {
    if (root == null) return 0;

    if (root.left == null) {
        return 1 + minDepth(root.right);
    }
    if (root.right == null) {
        return 1 + minDepth(root.left);
    }

    return 1 + Math.min(minDepth(root.left), minDepth(root.right));
}
```

#### LeetCode Problems
- LC 111: Minimum Depth of Binary Tree (Easy)

---

### 5.3) Balance Check Template

**Pattern**: Check balance during height calculation
**Use Case**: Validate tree balance
**Time Complexity**: O(n)
**Space Complexity**: O(h)

#### Template Code

```python
# Python - Balanced Binary Tree
def is_balanced(root):
    def check_height(node):
        if not node:
            return 0

        left_height = check_height(node.left)
        if left_height == -1:
            return -1

        right_height = check_height(node.right)
        if right_height == -1:
            return -1

        if abs(left_height - right_height) > 1:
            return -1

        return 1 + max(left_height, right_height)

    return check_height(root) != -1
```

```java
// Java - Balanced Binary Tree
public boolean isBalanced(TreeNode root) {
    return checkHeight(root) != -1;
}

private int checkHeight(TreeNode node) {
    if (node == null) return 0;

    int leftHeight = checkHeight(node.left);
    if (leftHeight == -1) return -1;

    int rightHeight = checkHeight(node.right);
    if (rightHeight == -1) return -1;

    if (Math.abs(leftHeight - rightHeight) > 1) {
        return -1;
    }

    return 1 + Math.max(leftHeight, rightHeight);
}
```

#### LeetCode Problems
- LC 110: Balanced Binary Tree (Easy)

---

### 5.4) Leftmost at Depth Template

**Pattern**: Find leftmost node at maximum depth
**Use Case**: Bottom-left tree value
**Time Complexity**: O(n)
**Space Complexity**: O(w)

#### Template Code

```python
# Python - Find Bottom Left Tree Value
from collections import deque

def find_bottom_left_value(root):
    queue = deque([root])
    leftmost = root.val

    while queue:
        level_size = len(queue)

        for i in range(level_size):
            node = queue.popleft()

            # First node of level
            if i == 0:
                leftmost = node.val

            if node.left:
                queue.append(node.left)
            if node.right:
                queue.append(node.right)

    return leftmost
```

```java
// Java - Find Bottom Left Tree Value
public int findBottomLeftValue(TreeNode root) {
    Queue<TreeNode> queue = new LinkedList<>();
    queue.offer(root);
    int leftmost = root.val;

    while (!queue.isEmpty()) {
        int levelSize = queue.size();

        for (int i = 0; i < levelSize; i++) {
            TreeNode node = queue.poll();

            if (i == 0) {
                leftmost = node.val;
            }

            if (node.left != null) queue.offer(node.left);
            if (node.right != null) queue.offer(node.right);
        }
    }

    return leftmost;
}
```

#### LeetCode Problems
- LC 513: Find Bottom Left Tree Value (Medium)

---

## 6) Tree Construction Templates

### 6.1) Tree Building Template

**Pattern**: Build tree from traversal arrays
**Use Case**: Construct from preorder/inorder or inorder/postorder
**Time Complexity**: O(n)
**Space Complexity**: O(n)

#### Template Code

```python
# Python - Build Tree from Preorder and Inorder
def build_tree(preorder, inorder):
    if not preorder or not inorder:
        return None

    # First element in preorder is root
    root_val = preorder[0]
    root = TreeNode(root_val)

    # Find root in inorder to split left/right
    root_index = inorder.index(root_val)

    # Recursively build subtrees
    root.left = build_tree(preorder[1:root_index+1], inorder[:root_index])
    root.right = build_tree(preorder[root_index+1:], inorder[root_index+1:])

    return root

# Python - Build Tree from Inorder and Postorder
def build_tree_post(inorder, postorder):
    if not inorder or not postorder:
        return None

    # Last element in postorder is root
    root_val = postorder[-1]
    root = TreeNode(root_val)

    root_index = inorder.index(root_val)

    root.left = build_tree_post(inorder[:root_index], postorder[:root_index])
    root.right = build_tree_post(inorder[root_index+1:], postorder[root_index:-1])

    return root
```

```java
// Java - Build Tree from Preorder and Inorder
private int preIndex = 0;
private Map<Integer, Integer> inorderMap = new HashMap<>();

public TreeNode buildTree(int[] preorder, int[] inorder) {
    for (int i = 0; i < inorder.length; i++) {
        inorderMap.put(inorder[i], i);
    }
    return build(preorder, 0, inorder.length - 1);
}

private TreeNode build(int[] preorder, int left, int right) {
    if (left > right) return null;

    int rootVal = preorder[preIndex++];
    TreeNode root = new TreeNode(rootVal);

    int index = inorderMap.get(rootVal);

    root.left = build(preorder, left, index - 1);
    root.right = build(preorder, index + 1, right);

    return root;
}
```

#### LeetCode Problems
- LC 105: Construct Binary Tree from Preorder and Inorder Traversal (Medium)
- LC 106: Construct Binary Tree from Inorder and Postorder Traversal (Medium)

---

### 6.2) String Conversion Template

**Pattern**: Serialize/deserialize tree
**Use Case**: Tree persistence, transmission
**Time Complexity**: O(n)
**Space Complexity**: O(n)

#### Template Code

```python
# Python - Serialize and Deserialize
class Codec:
    def serialize(self, root):
        if not root:
            return "#"

        return f"{root.val},{self.serialize(root.left)},{self.serialize(root.right)}"

    def deserialize(self, data):
        def build():
            val = next(values)
            if val == "#":
                return None

            node = TreeNode(int(val))
            node.left = build()
            node.right = build()
            return node

        values = iter(data.split(","))
        return build()
```

```java
// Java - Serialize and Deserialize
public class Codec {
    public String serialize(TreeNode root) {
        if (root == null) {
            return "#";
        }

        return root.val + "," + serialize(root.left) + "," + serialize(root.right);
    }

    public TreeNode deserialize(String data) {
        Queue<String> queue = new LinkedList<>(Arrays.asList(data.split(",")));
        return build(queue);
    }

    private TreeNode build(Queue<String> queue) {
        String val = queue.poll();
        if (val.equals("#")) {
            return null;
        }

        TreeNode node = new TreeNode(Integer.parseInt(val));
        node.left = build(queue);
        node.right = build(queue);
        return node;
    }
}
```

#### LeetCode Problems
- LC 297: Serialize and Deserialize Binary Tree (Hard)
- LC 449: Serialize and Deserialize BST (Medium)

---

### 6.3) String Construction Template

**Pattern**: Build string representation
**Use Case**: Tree to string conversion
**Time Complexity**: O(n)
**Space Complexity**: O(h)

#### Template Code

```python
# Python - Construct String from Binary Tree
def tree2str(root):
    if not root:
        return ""

    if not root.left and not root.right:
        return str(root.val)

    if not root.right:
        return f"{root.val}({tree2str(root.left)})"

    return f"{root.val}({tree2str(root.left)})({tree2str(root.right)})"
```

```java
// Java - Construct String from Binary Tree
public String tree2str(TreeNode root) {
    if (root == null) return "";

    if (root.left == null && root.right == null) {
        return String.valueOf(root.val);
    }

    if (root.right == null) {
        return root.val + "(" + tree2str(root.left) + ")";
    }

    return root.val + "(" + tree2str(root.left) + ")(" + tree2str(root.right) + ")";
}
```

#### LeetCode Problems
- LC 606: Construct String from Binary Tree (Easy)

---

## 7) Tree Modification Templates

### 7.1) Tree Inversion Template

**Pattern**: Swap left and right subtrees
**Use Case**: Mirror/invert tree
**Time Complexity**: O(n)
**Space Complexity**: O(h)

#### Template Code

```python
# Python - Invert Binary Tree
def invert_tree(root):
    if not root:
        return None

    # Swap children
    root.left, root.right = root.right, root.left

    # Recursively invert subtrees
    invert_tree(root.left)
    invert_tree(root.right)

    return root
```

```java
// Java - Invert Binary Tree
public TreeNode invertTree(TreeNode root) {
    if (root == null) return null;

    // Cache children
    TreeNode left = invertTree(root.left);
    TreeNode right = invertTree(root.right);

    // Swap
    root.left = right;
    root.right = left;

    return root;
}
```

#### LeetCode Problems
- LC 226: Invert Binary Tree (Easy)

---

### 7.2) Tree Flattening Template

**Pattern**: Flatten tree to linked list
**Use Case**: Convert to right-skewed tree
**Time Complexity**: O(n)
**Space Complexity**: O(h)

#### Template Code

```python
# Python - Flatten Binary Tree to Linked List
def flatten(root):
    if not root:
        return

    flatten(root.left)
    flatten(root.right)

    # Save right subtree
    right = root.right

    # Move left subtree to right
    root.right = root.left
    root.left = None

    # Attach original right subtree to end
    current = root
    while current.right:
        current = current.right
    current.right = right
```

```java
// Java - Flatten Binary Tree to Linked List
public void flatten(TreeNode root) {
    if (root == null) return;

    flatten(root.left);
    flatten(root.right);

    TreeNode right = root.right;

    root.right = root.left;
    root.left = null;

    TreeNode current = root;
    while (current.right != null) {
        current = current.right;
    }
    current.right = right;
}
```

#### LeetCode Problems
- LC 114: Flatten Binary Tree to Linked List (Medium)

---

### 7.3) Tree Merging Template

**Pattern**: Merge two trees node by node
**Use Case**: Combine two trees
**Time Complexity**: O(min(n, m))
**Space Complexity**: O(min(h1, h2))

#### Template Code

```python
# Python - Merge Two Binary Trees
def merge_trees(t1, t2):
    if not t1 and not t2:
        return None
    if not t1:
        return t2
    if not t2:
        return t1

    # Merge current nodes
    merged = TreeNode(t1.val + t2.val)

    # Recursively merge children
    merged.left = merge_trees(t1.left, t2.left)
    merged.right = merge_trees(t1.right, t2.right)

    return merged
```

```java
// Java - Merge Two Binary Trees
public TreeNode mergeTrees(TreeNode t1, TreeNode t2) {
    if (t1 == null && t2 == null) return null;
    if (t1 == null) return t2;
    if (t2 == null) return t1;

    TreeNode merged = new TreeNode(t1.val + t2.val);

    merged.left = mergeTrees(t1.left, t2.left);
    merged.right = mergeTrees(t1.right, t2.right);

    return merged;
}
```

#### LeetCode Problems
- LC 617: Merge Two Binary Trees (Easy)

---

## Summary Table: All Templates

| Template Name | Pattern | Time | Space | LeetCode Problems |
|--------------|---------|------|-------|-------------------|
| **Preorder Template** | Root → Left → Right | O(n) | O(h) | LC 144 |
| **Inorder Template** | Left → Root → Right | O(n) | O(h) | LC 94, 98, 230 |
| **Postorder Template** | Left → Right → Root | O(n) | O(h) | LC 145 |
| **BFS Template** | Level-by-level | O(n) | O(w) | LC 102, 103, 107, 199 |
| **BFS + Direction** | Alternating levels | O(n) | O(w) | LC 103 |
| **Postorder Height** | Bottom-up height | O(n) | O(h) | LC 104 |
| **BFS Early Stop** | Stop at condition | O(n) | O(w) | LC 111 |
| **Height Validation** | Balance check | O(n) | O(h) | LC 110 |
| **Mirror Validation** | Symmetric check | O(n) | O(h) | LC 101 |
| **Tree Comparison** | Compare trees | O(n) | O(h) | LC 100, 572 |
| **Global Max Update** | Track global max | O(n) | O(h) | LC 124 |
| **Path Accumulation** | Sum along path | O(n) | O(h) | LC 112 |
| **Path + Backtrack** | Collect all paths | O(n) | O(h) | LC 113, 257 |
| **Path Count Tracking** | Prefix sum paths | O(n) | O(n) | LC 437 |
| **Path Value Building** | Build path value | O(n) | O(h) | LC 129 |
| **Path State Tracking** | Track max in path | O(n) | O(h) | LC 1448 |
| **Longest Path** | Diameter calculation | O(n) | O(h) | LC 543 |
| **Same Value Path** | Univalue path | O(n) | O(h) | LC 687 |
| **LCA Standard** | Find LCA | O(n) | O(h) | LC 236 |
| **Value Comparison** | BST LCA | O(h) | O(1) | LC 235 |
| **Path Distance** | Distance via LCA | O(n) | O(h) | LC 1740 |
| **Tree to Graph** | Convert for queries | O(n) | O(n) | LC 863 |
| **Height Calculation** | Calculate height | O(n) | O(h) | LC 104 |
| **Depth to Leaf** | Min depth to leaf | O(n) | O(h) | LC 111 |
| **Balance Check** | Validate balance | O(n) | O(h) | LC 110 |
| **Leftmost at Depth** | Bottom-left value | O(n) | O(w) | LC 513 |
| **Tree Building** | Build from arrays | O(n) | O(n) | LC 105, 106 |
| **String Conversion** | Serialize/deserialize | O(n) | O(n) | LC 297, 449 |
| **String Construction** | Tree to string | O(n) | O(h) | LC 606 |
| **Tree Inversion** | Mirror tree | O(n) | O(h) | LC 226 |
| **Tree Flattening** | Flatten to list | O(n) | O(h) | LC 114 |
| **Tree Merging** | Merge two trees | O(n) | O(h) | LC 617 |

---

## Quick Reference Guide

### When to Use Each Template

1. **Need to process root before children?** → Use Preorder Template
2. **Need sorted order (BST)?** → Use Inorder Template
3. **Need children data for parent?** → Use Postorder Template
4. **Need level-by-level processing?** → Use BFS Template
5. **Need to track path sum/values?** → Use Path Tracking Templates
6. **Need to find LCA?** → Use LCA Standard or Value Comparison
7. **Need to build tree from arrays?** → Use Tree Building Template
8. **Need to modify tree structure?** → Use Tree Modification Templates
9. **Need to validate tree properties?** → Use Validation Templates
10. **Need distance/path information?** → Use Path Distance Templates

---

## Practice Recommendations

### Easy Problems (Start Here)
- LC 144, 94, 145: Basic traversals
- LC 100, 101: Tree comparison
- LC 104, 111: Depth calculation
- LC 226: Tree inversion
- LC 617: Tree merging

### Medium Problems (Build Skills)
- LC 102, 103, 107: Level-order variants
- LC 105, 106: Tree construction
- LC 113, 129, 437: Path problems
- LC 236: LCA
- LC 114: Tree flattening

### Hard Problems (Master Level)
- LC 124: Maximum path sum
- LC 297: Serialization
- LC 1740: Distance calculation

---

**Note**: All templates assume TreeNode definition:
```python
class TreeNode:
    def __init__(self, val=0, left=None, right=None):
        self.val = val
        self.left = left
        self.right = right
```

```java
class TreeNode {
    int val;
    TreeNode left;
    TreeNode right;
    TreeNode(int x) { val = x; }
}
```
