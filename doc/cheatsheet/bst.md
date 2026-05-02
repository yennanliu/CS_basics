# BST (Binary Search Tree)

## Overview
**Binary Search Tree (BST)** is a binary tree data structure where each node follows the ordering property: left child < parent < right child. This property enables efficient searching, insertion, and deletion operations.

### Key Properties
- **Time Complexity**: 
  - Search/Insert/Delete: O(log n) average, O(n) worst case (unbalanced)
  - Traversal: O(n) for all nodes
- **Space Complexity**: O(n) for storing n nodes, O(h) for recursive operations
- **Core Property**: `left < root < right` for all nodes
- **Inorder Traversal**: Produces sorted sequence (ascending order)
- **When to Use**: Sorted data operations, range queries, ordered statistics

### References
- [BST Visualizer](https://www.cs.usfca.edu/~galles/visualization/BST.html)
- [fucking-algorithm - BST pt.1](https://labuladong.github.io/algo/2/21/43/)
- [fucking-algorithm - BST pt.2](https://labuladong.github.io/algo/2/21/44/)
- [fucking-algorithm - BST pt.3](https://labuladong.github.io/algo/2/21/42/)

```java
// below will print BST elements in ascending ordering
// java
void traverse(TreeNode root) {
    if (root == null) return;
    traverse(root.left);
    // in-order traversal
    print(root.val);
    traverse(root.right);
```

## Problem Categories

### **Pattern 1: BST Search & Validation**
- **Description**: Find elements or validate BST properties
- **Recognition**: "Search", "find", "validate", "is valid BST"
- **Examples**: LC 98, LC 700, LC 270, LC 285
- **Template**: Use Search Template with BST property

### **Pattern 2: BST Insertion & Deletion**
- **Description**: Modify BST structure while maintaining properties
- **Recognition**: "Insert", "delete", "remove", "add node"
- **Examples**: LC 450, LC 701, LC 669
- **Template**: Use Modification Template

### **Pattern 3: BST Traversal & Conversion**
- **Description**: Use inorder property for sorting/conversion
- **Recognition**: "Kth smallest", "convert", "flatten", "sorted order"
- **Examples**: LC 230, LC 173, LC 426, LC 538
- **Template**: Use Inorder Template

### **Pattern 4: BST Construction**
- **Description**: Build BST from various inputs or rebuild/balance existing BST
- **Recognition**: "Construct", "build", "generate", "serialize", "balance"
- **Examples**: LC 108, LC 109, LC 95, LC 96, LC 449, LC 1008, LC 1382
- **Template**: Use Construction Template

### **Pattern 5: BST Properties & Optimization**
- **Description**: Find optimal values or properties in BST
- **Recognition**: "Closest", "LCA", "range", "distance"
- **Examples**: LC 235, LC 530, LC 783, LC 776
- **Template**: Use Property Template

### **Pattern 6: Path Problems**
- **Description**: Problems involving root-to-leaf or node-to-node paths
- **Recognition**: "Path sum", "root to leaf", "maximum path", "consecutive sequence"
- **Examples**: LC 112, LC 113, LC 257, LC 124, LC 129, LC 298, LC 437
- **Template**: Use DFS with path tracking, backtracking, or global state

## Templates & Algorithms

### Template Comparison Table
| Template Type | Use Case | Key Operation | Time | Space | When to Use |
|---------------|----------|---------------|------|-------|-------------|
| **Search Template** | Finding values | Binary search | O(log n) | O(1)/O(h) | Value lookup |
| **Insertion Template** | Adding nodes | Find position + insert | O(log n) | O(1)/O(h) | Adding new values |
| **Deletion Template** | Removing nodes | Find + restructure | O(log n) | O(h) | Removing values |
| **Inorder Template** | Sorted operations | Left-root-right | O(n) | O(h) | Kth element, range |
| **Construction Template** | Building BST | Divide & conquer | O(n) | O(n) | Creating from array |
| **Path Template** | Root-to-leaf paths | DFS + tracking | O(n) | O(h) | Path sum, sequences |
### Universal BST Template
```python
def bst_operation(root, target):
    """
    Universal template for BST operations
    Leverages left < root < right property
    """
    if not root:
        return None  # or appropriate base value
    
    # BST property for efficient search
    if target == root.val:
        # Process current node
        return root
    elif target < root.val:
        # Go left for smaller values
        return bst_operation(root.left, target)
    else:
        # Go right for larger values
        return bst_operation(root.right, target)
```

### Template 1: BST Search
```python
def search_bst(root, val):
    """
    Search for a value in BST
    Time: O(log n) average, O(n) worst
    """
    if not root or root.val == val:
        return root
    
    if val < root.val:
        return search_bst(root.left, val)
    return search_bst(root.right, val)

# Iterative version
def search_bst_iterative(root, val):
    while root and root.val != val:
        root = root.left if val < root.val else root.right
    return root
```

### Template 2: BST Insertion
```python
def insert_bst(root, val):
    """
    Insert value into BST
    Always inserts as a leaf node
    """
    if not root:
        return TreeNode(val)
    
    if val < root.val:
        root.left = insert_bst(root.left, val)
    elif val > root.val:
        root.right = insert_bst(root.right, val)
    # If val == root.val, typically don't insert duplicates
    
    return root
```

### Template 3: BST Deletion
```python
def delete_bst(root, key):
    """
    Delete a node from BST
    Three cases: no child, one child, two children
    """
    if not root:
        return None
    
    if key < root.val:
        root.left = delete_bst(root.left, key)
    elif key > root.val:
        root.right = delete_bst(root.right, key)
    else:
        # Node found - handle 3 cases
        if not root.left:  # No left child or leaf
            return root.right
        if not root.right:  # No right child
            return root.left
        
        # Two children: find inorder successor
        min_node = find_min(root.right)
        root.val = min_node.val
        root.right = delete_bst(root.right, min_node.val)
    
    return root

def find_min(node):
    """Find minimum value in BST (leftmost node)"""
    while node.left:
        node = node.left
    return node
```

### Template 4: BST Validation
```python
def validate_bst(root):
    """
    Validate if tree is a valid BST
    Uses min/max bounds approach
    """
    def validate(node, min_val, max_val):
        if not node:
            return True
        
        if node.val <= min_val or node.val >= max_val:
            return False
        
        return (validate(node.left, min_val, node.val) and
                validate(node.right, node.val, max_val))
    
    return validate(root, float('-inf'), float('inf'))
```

### Template 5: BST Inorder Operations
```python
def kth_smallest(root, k):
    """
    Find kth smallest element using inorder property
    Inorder traversal of BST gives sorted order
    """
    def inorder(node):
        if not node:
            return []
        return inorder(node.left) + [node.val] + inorder(node.right)
    
    return inorder(root)[k-1]

# Optimized with early stopping
def kth_smallest_optimized(root, k):
    stack = []
    while True:
        while root:
            stack.append(root)
            root = root.left
        root = stack.pop()
        k -= 1
        if k == 0:
            return root.val
        root = root.right
```

### Template 5b: BST Lazy Traversal (Iterator Pattern)

#### **Pattern Overview**
- **Description**: Simulate inorder traversal on-demand using a stack — only traverse as far as needed, not the whole tree upfront
- **Recognition**: "BST Iterator", "next smallest", "streaming traversal", large dataset where loading all nodes is expensive
- **Key Insight**: Push only the left spine into the stack; when popping a node, push its right subtree's left spine
- **Time Complexity**: O(1) amortized per `next()` call, O(h) space where h = tree height
- **Space Complexity**: O(h) — far better than O(n) from pre-collecting all nodes

#### **Core Idea**

```
Pre-collect ALL nodes (eager):              Lazy traversal:
  O(n) space, O(n) init time                 O(h) space, O(1) amortized per call

  [1, 3, 5, 7, 9, ...]   ← full list        Stack: only current left spine
  load everything first                      push more only when needed
```

**Three-step pattern:**
```
1. INIT:    Push entire left spine from root into stack
             (leftmost path = smallest values on top)

2. next():  Pop top node (= current smallest)
             → if it has a right child, push that subtree's left spine
             → return popped node's value

3. hasNext(): stack is non-empty
```

**Visual walkthrough:**
```
BST:          7
             / \
            3   15
               /  \
              9   20

Init: pushLeft(7) → stack = [7, 3]  (3 on top)

next() → pop 3, no right child → return 3,  stack = [7]
next() → pop 7, pushLeft(15)   → return 7,  stack = [15, 9]
next() → pop 9, no right child → return 9,  stack = [15]
next() → pop 15, pushLeft(20)  → return 15, stack = [20]
next() → pop 20, no right child → return 20, stack = []
hasNext() → false
```

#### **Java Implementation**
```java
class BSTIterator {
    private Stack<TreeNode> stack = new Stack<>();

    public BSTIterator(TreeNode root) {
        pushLeft(root);
    }

    private void pushLeft(TreeNode node) {
        while (node != null) {
            stack.push(node);
            node = node.left;
        }
    }

    public int next() {
        TreeNode node = stack.pop();
        if (node.right != null) {
            pushLeft(node.right);   // lazily expand right subtree
        }
        return node.val;
    }

    public boolean hasNext() {
        return !stack.isEmpty();
    }
}
```

#### **Python Implementation**
```python
class BSTIterator:
    def __init__(self, root):
        self.stack = []
        self._push_left(root)

    def _push_left(self, node):
        while node:
            self.stack.append(node)
            node = node.left

    def next(self) -> int:
        node = self.stack.pop()
        if node.right:
            self._push_left(node.right)
        return node.val

    def hasNext(self) -> bool:
        return bool(self.stack)
```

#### **Eager vs Lazy Comparison**
| Approach | Init Time | Init Space | next() Time | next() Space | Best For |
|----------|-----------|------------|-------------|--------------|----------|
| **Eager** (collect all) | O(n) | O(n) | O(1) | O(1) | Small trees, many calls |
| **Lazy** (stack) | O(h) | O(h) | O(1) amortized | O(1) | Large trees, partial traversal |

#### **Similar LeetCode Problems**
| Problem | LC # | Difficulty | How Lazy Traversal Applies |
|---------|------|------------|----------------------------|
| Binary Search Tree Iterator | 173 | Medium | Core pattern — iterator with `next()` / `hasNext()` |
| Kth Smallest in BST | 230 | Medium | Stop after k pops instead of collecting all |
| Inorder Successor in BST | 285 | Medium | One step of lazy traversal |
| Two Sum IV - Input is BST | 653 | Easy | Two iterators (forward + reverse) meet in middle |
| All Elements in Two BSTs | 1305 | Medium | Merge two lazy iterators |

#### **Key Takeaways**
```
1. Push LEFT SPINE only — this gives smallest-first access
2. On pop: expand right subtree's left spine lazily
3. Stack depth = O(h), not O(n) — critical for tall/large trees
4. Amortized O(1) per next(): each node is pushed and popped exactly once
5. Enables partial traversal — stop early without wasting work
```

### Template 6: BST Construction

#### **Pattern Overview**
- **Description**: Build BST from various inputs (arrays, lists, traversals)
- **Recognition**: "Construct", "build", "generate", "serialize", "from preorder/inorder"
- **Key Concept**: Use recursive divide-and-conquer with BST property
- **Time Complexity**: O(n) for most constructions, O(n log n) for some
- **Space Complexity**: O(n) for tree storage + O(h) for recursion stack

#### **Core Construction Patterns**

##### **Pattern 6.1: From Sorted Array** (LC 108)
```python
def sorted_array_to_bst(nums):
    """
    Convert sorted array to balanced BST
    Uses binary search approach - pick middle as root
    Time: O(n), Space: O(n)
    """
    if not nums:
        return None

    mid = len(nums) // 2
    root = TreeNode(nums[mid])
    root.left = sorted_array_to_bst(nums[:mid])
    root.right = sorted_array_to_bst(nums[mid+1:])

    return root

# Optimized version with index (no array slicing)
def sorted_array_to_bst_optimized(nums):
    def build(left, right):
        if left > right:
            return None

        mid = (left + right) // 2
        root = TreeNode(nums[mid])
        root.left = build(left, mid - 1)
        root.right = build(mid + 1, right)
        return root

    return build(0, len(nums) - 1)
```

##### **Pattern 6.2: From Sorted Linked List** (LC 109)
```python
def sorted_list_to_bst(head):
    """
    Convert sorted linked list to balanced BST
    Two approaches: 1) Two pointers to find middle
                   2) Inorder simulation
    Time: O(n log n) or O(n), Space: O(log n)
    """
    # Approach 1: Find middle with slow-fast pointers
    def find_middle(left, right):
        slow = fast = left
        while fast != right and fast.next != right:
            slow = slow.next
            fast = fast.next.next
        return slow

    def convert(left, right):
        if left == right:
            return None

        mid = find_middle(left, right)
        root = TreeNode(mid.val)
        root.left = convert(left, mid)
        root.right = convert(mid.next, right)
        return root

    return convert(head, None)
```

##### **Pattern 6.3: From Preorder Traversal** (LC 1008)
```python
def bst_from_preorder(preorder):
    """
    Construct BST from preorder traversal
    Use BST property: left < root < right
    Time: O(n), Space: O(h)
    """
    def build(min_val, max_val):
        nonlocal idx
        if idx >= len(preorder):
            return None

        val = preorder[idx]
        if val < min_val or val > max_val:
            return None

        idx += 1
        root = TreeNode(val)
        root.left = build(min_val, val)
        root.right = build(val, max_val)
        return root

    idx = 0
    return build(float('-inf'), float('inf'))
```

##### **Pattern 6.4: Balance a BST** (LC 1382)
```python
def balance_bst(root):
    """
    Balance an existing BST by rebuilding it
    Approach: Inorder traversal + rebuild from sorted nodes
    Time: O(n), Space: O(n)

    Key Steps:
    1. Inorder traversal to get sorted node list
    2. Use middle element as root (like sorted array to BST)
    3. Recursively build left and right subtrees
    """
    # Step 1: Get sorted nodes via inorder traversal
    nodes = []
    def inorder(node):
        if not node:
            return
        inorder(node.left)
        nodes.append(node)
        inorder(node.right)

    inorder(root)

    # Step 2: Rebuild balanced BST from sorted nodes
    def build_balanced(left, right):
        if left > right:
            return None

        # Pick middle as root to ensure balance
        mid = (left + right) // 2
        root = nodes[mid]

        # Recursively build left and right subtrees
        root.left = build_balanced(left, mid - 1)
        root.right = build_balanced(mid + 1, right)

        return root

    return build_balanced(0, len(nodes) - 1)
```

```java
// Java implementation
public TreeNode balanceBST(TreeNode root) {
    List<TreeNode> nodes = new ArrayList<>();

    // Step 1: Inorder traversal to get sorted nodes
    inorderTraversal(root, nodes);

    // Step 2: Rebuild balanced BST
    return buildBalancedBST(nodes, 0, nodes.size() - 1);
}

private void inorderTraversal(TreeNode root, List<TreeNode> nodes) {
    if (root == null) {
        return;
    }
    inorderTraversal(root.left, nodes);
    nodes.add(root);
    inorderTraversal(root.right, nodes);
}

private TreeNode buildBalancedBST(List<TreeNode> nodes, int left, int right) {
    if (left > right) {
        return null;
    }

    // Pick middle as root for balance
    int mid = (left + right) / 2;
    TreeNode root = nodes.get(mid);

    // Recursively build subtrees
    root.left = buildBalancedBST(nodes, left, mid - 1);
    root.right = buildBalancedBST(nodes, mid + 1, right);

    return root;
}
```

##### **Pattern 6.5: Generate All Unique BSTs (Recursive Construction via Cartesian Product)** (LC 95)

**Core Idea**:
- Pick each number `i` in `[start, end]` as the root
- All numbers `[start, i-1]` must form the **left** subtree (BST property)
- All numbers `[i+1, end]` must form the **right** subtree (BST property)
- The total unique trees for root `i` = **Cartesian product** of all left subtrees × all right subtrees
- Base case: when `start > end`, return `[null]` (empty subtree, NOT empty list)
- Total count follows **Catalan number**: C(n) = (2n)! / ((n+1)! × n!)

**Approaches**:
1. **Plain Recursion** — enumerate all combinations directly (overlapping subproblems)
2. **Memoized Recursion** — cache `(start, end) → List<TreeNode>` to avoid recomputation
3. **Iterative DP** — bottom-up table `dp[start][end]` filled by increasing window size
4. **Space-Optimized DP** — `dp[numberOfNodes]` with clone + offset for right subtrees

```python
def generate_trees(n):
    """
    Generate all structurally unique BSTs with n nodes
    Catalan number of trees: C(n) = (2n)! / ((n+1)! * n!)
    Time: O(4^n / n^(3/2)), Space: O(4^n / n^(3/2))
    """
    if n == 0:
        return []

    def generate(start, end):
        if start > end:
            return [None]

        all_trees = []
        for root_val in range(start, end + 1):
            left_trees = generate(start, root_val - 1)
            right_trees = generate(root_val + 1, end)

            for left in left_trees:
                for right in right_trees:
                    root = TreeNode(root_val)
                    root.left = left
                    root.right = right
                    all_trees.append(root)

        return all_trees

    return generate(1, n)
```

```java
// Java — Plain Recursion (LC 95)
public List<TreeNode> generateTrees(int n) {
    if (n == 0) return new ArrayList<>();
    return buildTrees(1, n);
}

private List<TreeNode> buildTrees(int start, int end) {
    List<TreeNode> allTrees = new ArrayList<>();
    if (start > end) {
        allTrees.add(null);  // important: null represents empty subtree
        return allTrees;
    }
    for (int i = start; i <= end; i++) {
        // left subtree candidates: [start, i-1]
        List<TreeNode> leftSubtrees = buildTrees(start, i - 1);
        // right subtree candidates: [i+1, end]
        List<TreeNode> rightSubtrees = buildTrees(i + 1, end);
        // Cartesian product: connect each left × right to root i
        for (TreeNode left : leftSubtrees) {
            for (TreeNode right : rightSubtrees) {
                TreeNode root = new TreeNode(i);
                root.left = left;
                root.right = right;
                allTrees.add(root);
            }
        }
    }
    return allTrees;
}
```

```java
// Java — Memoized Recursion (LC 95)
public List<TreeNode> generateTrees_memo(int n) {
    Map<Pair<Integer, Integer>, List<TreeNode>> memo = new HashMap<>();
    return allPossibleBST(1, n, memo);
}

private List<TreeNode> allPossibleBST(int start, int end,
        Map<Pair<Integer, Integer>, List<TreeNode>> memo) {
    List<TreeNode> res = new ArrayList<>();
    if (start > end) { res.add(null); return res; }
    if (memo.containsKey(new Pair<>(start, end)))
        return memo.get(new Pair<>(start, end));
    for (int i = start; i <= end; ++i) {
        List<TreeNode> leftSubs = allPossibleBST(start, i - 1, memo);
        List<TreeNode> rightSubs = allPossibleBST(i + 1, end, memo);
        for (TreeNode left : leftSubs)
            for (TreeNode right : rightSubs)
                res.add(new TreeNode(i, left, right));
    }
    memo.put(new Pair<>(start, end), res);
    return res;
}
```

**Similar LeetCode Problems**:
- LC 95: Unique Binary Search Trees II (generate all)
- LC 96: Unique Binary Search Trees (count only — Catalan number DP)
- LC 241: Different Ways to Add Parentheses (same Cartesian product pattern)
- LC 894: All Possible Full Binary Trees
- LC 1382: Balance a Binary Search Tree

##### **Pattern 6.6: Count Unique BSTs** (LC 96)
```python
def num_trees(n):
    """
    Count number of structurally unique BSTs with n nodes
    Uses Catalan Number: C(n) = C(0)*C(n-1) + C(1)*C(n-2) + ... + C(n-1)*C(0)
    Time: O(n^2), Space: O(n)
    """
    dp = [0] * (n + 1)
    dp[0] = dp[1] = 1

    for nodes in range(2, n + 1):
        for root in range(1, nodes + 1):
            left = root - 1
            right = nodes - root
            dp[nodes] += dp[left] * dp[right]

    return dp[n]
```

#### **Java Implementations**
```java
// Pattern 6.1: From Sorted Array (LC 108)
public TreeNode sortedArrayToBST(int[] nums) {
    return buildBST(nums, 0, nums.length - 1);
}

private TreeNode buildBST(int[] nums, int left, int right) {
    if (left > right) return null;

    int mid = left + (right - left) / 2;
    TreeNode root = new TreeNode(nums[mid]);
    root.left = buildBST(nums, left, mid - 1);
    root.right = buildBST(nums, mid + 1, right);
    return root;
}

// Pattern 6.3: From Preorder (LC 1008)
private int idx = 0;

public TreeNode bstFromPreorder(int[] preorder) {
    return build(preorder, Integer.MIN_VALUE, Integer.MAX_VALUE);
}

private TreeNode build(int[] preorder, int min, int max) {
    if (idx >= preorder.length) return null;

    int val = preorder[idx];
    if (val < min || val > max) return null;

    idx++;
    TreeNode root = new TreeNode(val);
    root.left = build(preorder, min, val);
    root.right = build(preorder, val, max);
    return root;
}
```

#### **Construction Pattern Summary Table**
| Input Type | Approach | Key Technique | Time | Space | LC # |
|------------|----------|---------------|------|-------|------|
| **Sorted Array** | Binary search | Pick mid as root | O(n) | O(n) | 108 |
| **Sorted List** | Two pointers | Find mid node | O(n log n) | O(log n) | 109 |
| **Preorder** | Bounds checking | Use min/max | O(n) | O(h) | 1008 |
| **Balance BST** | Inorder + Rebuild | Collect sorted nodes | O(n) | O(n) | 1382 |
| **Generate All** | Combinatorial | Try each as root | O(4^n/n^1.5) | O(4^n/n^1.5) | 95 |
| **Count Unique** | Dynamic programming | Catalan numbers | O(n²) | O(n) | 96 |
| **Serialize** | Preorder encoding | BST property | O(n) | O(n) | 449 |

### Template 7: Path Problems

#### **Pattern Overview**
- **Description**: Find or validate paths in binary trees (root-to-leaf, node-to-node)
- **Recognition**: "Path sum", "root to leaf", "maximum path", "consecutive"
- **Key Concept**: Use DFS with path tracking, accumulation, or global state
- **Time Complexity**: O(n) for visiting all nodes
- **Space Complexity**: O(h) for recursion stack + path storage

**📚 Related Patterns**: These path problems use DFS traversal. For general tree path-finding patterns and techniques, see **dfs.md Template 3 (Path Finding)**. The examples here focus on common path problems that work for both BST and general binary trees.

#### **Core Path Patterns**

##### **Pattern 7.1: Simple Path Sum** (LC 112)
```python
def has_path_sum(root, target_sum):
    """
    Check if root-to-leaf path exists with given sum
    Time: O(n), Space: O(h)
    """
    if not root:
        return False

    # Leaf node check
    if not root.left and not root.right:
        return root.val == target_sum

    # Recurse with reduced sum
    remaining = target_sum - root.val
    return (has_path_sum(root.left, remaining) or
            has_path_sum(root.right, remaining))
```

##### **Pattern 7.2: Path Sum II - All Paths** (LC 113)
```python
def path_sum(root, target_sum):
    """
    Find all root-to-leaf paths with given sum
    Uses backtracking to track current path
    Time: O(n), Space: O(h)
    """
    result = []

    def dfs(node, remaining, path):
        if not node:
            return

        # Add current node to path
        path.append(node.val)

        # Check if leaf with target sum
        if not node.left and not node.right and remaining == node.val:
            result.append(path[:])  # Deep copy

        # Recurse on children
        new_remaining = remaining - node.val
        dfs(node.left, new_remaining, path)
        dfs(node.right, new_remaining, path)

        # Backtrack
        path.pop()

    dfs(root, target_sum, [])
    return result
```

##### **Pattern 7.3: Binary Tree Paths** (LC 257)
```python
def binary_tree_paths(root):
    """
    Find all root-to-leaf paths as strings
    Time: O(n), Space: O(h)
    """
    if not root:
        return []

    result = []

    def dfs(node, path):
        if not node:
            return

        # Add current node to path string
        path += str(node.val)

        # Leaf node - add complete path
        if not node.left and not node.right:
            result.append(path)
            return

        # Continue path with arrow
        path += "->"
        dfs(node.left, path)
        dfs(node.right, path)

    dfs(root, "")
    return result
```

##### **Pattern 7.4: Sum Root to Leaf Numbers** (LC 129)
```python
def sum_numbers(root):
    """
    Sum all numbers formed by root-to-leaf paths
    Example: 1->2->3 represents 123
    Time: O(n), Space: O(h)
    """
    def dfs(node, current_sum):
        if not node:
            return 0

        # Build number: current_sum * 10 + node.val
        current_sum = current_sum * 10 + node.val

        # Leaf node - return the number
        if not node.left and not node.right:
            return current_sum

        # Sum from both subtrees
        return dfs(node.left, current_sum) + dfs(node.right, current_sum)

    return dfs(root, 0)
```

##### **Pattern 7.5: Binary Tree Maximum Path Sum** (LC 124)
```python
def max_path_sum(root):
    """
    Find maximum path sum (any node to any node)
    Uses global variable to track maximum
    Time: O(n), Space: O(h)
    """
    max_sum = float('-inf')

    def dfs(node):
        nonlocal max_sum

        if not node:
            return 0

        # Get max contribution from left and right
        # Use max(0, ...) to ignore negative paths
        left_max = max(0, dfs(node.left))
        right_max = max(0, dfs(node.right))

        # Update global max with path through current node
        path_sum = node.val + left_max + right_max
        max_sum = max(max_sum, path_sum)

        # Return max path going through this node (one side only)
        return node.val + max(left_max, right_max)

    dfs(root)
    return max_sum
```

##### **Pattern 7.6: Binary Tree Longest Consecutive Sequence** (LC 298)
```python
def longest_consecutive(root):
    """
    Find length of longest consecutive path
    Values must increase by 1 each step
    Time: O(n), Space: O(h)
    """
    max_length = 0

    def dfs(node, parent_val, length):
        nonlocal max_length

        if not node:
            return

        # Check if consecutive with parent
        if node.val == parent_val + 1:
            length += 1
        else:
            length = 1  # Reset sequence

        # Update global max
        max_length = max(max_length, length)

        # Recurse on children
        dfs(node.left, node.val, length)
        dfs(node.right, node.val, length)

    dfs(root, float('-inf'), 0)
    return max_length
```

##### **Pattern 7.7: Path Sum III** (LC 437)
```python
def path_sum_3(root, target_sum):
    """
    Count paths with given sum (not necessarily root-to-leaf)
    Uses prefix sum technique with hash map
    Time: O(n), Space: O(n)
    """
    from collections import defaultdict

    def dfs(node, current_sum, prefix_sums):
        if not node:
            return 0

        # Update current sum
        current_sum += node.val

        # Count paths ending at current node
        # If (current_sum - target_sum) exists, we found path(s)
        count = prefix_sums[current_sum - target_sum]

        # Add current sum to prefix map
        prefix_sums[current_sum] += 1

        # Recurse on children
        count += dfs(node.left, current_sum, prefix_sums)
        count += dfs(node.right, current_sum, prefix_sums)

        # Backtrack: remove current sum from map
        prefix_sums[current_sum] -= 1

        return count

    # Initialize with 0 sum (for paths starting from root)
    prefix_sums = defaultdict(int)
    prefix_sums[0] = 1

    return dfs(root, 0, prefix_sums)
```

#### **Java Implementations**
```java
// Pattern 7.1: Simple Path Sum (LC 112)
public boolean hasPathSum(TreeNode root, int targetSum) {
    if (root == null) return false;

    // Leaf node check
    if (root.left == null && root.right == null) {
        return root.val == targetSum;
    }

    int remaining = targetSum - root.val;
    return hasPathSum(root.left, remaining) ||
           hasPathSum(root.right, remaining);
}

// Pattern 7.2: Path Sum II (LC 113)
public List<List<Integer>> pathSum(TreeNode root, int targetSum) {
    List<List<Integer>> result = new ArrayList<>();
    dfs(root, targetSum, new ArrayList<>(), result);
    return result;
}

private void dfs(TreeNode node, int remaining,
                 List<Integer> path, List<List<Integer>> result) {
    if (node == null) return;

    path.add(node.val);

    if (node.left == null && node.right == null && remaining == node.val) {
        result.add(new ArrayList<>(path));
    }

    int newRemaining = remaining - node.val;
    dfs(node.left, newRemaining, path, result);
    dfs(node.right, newRemaining, path, result);

    path.remove(path.size() - 1);  // Backtrack
}

// Pattern 7.5: Maximum Path Sum (LC 124)
private int maxSum = Integer.MIN_VALUE;

public int maxPathSum(TreeNode root) {
    dfs(root);
    return maxSum;
}

private int dfs(TreeNode node) {
    if (node == null) return 0;

    int leftMax = Math.max(0, dfs(node.left));
    int rightMax = Math.max(0, dfs(node.right));

    maxSum = Math.max(maxSum, node.val + leftMax + rightMax);

    return node.val + Math.max(leftMax, rightMax);
}

// Pattern 7.7: Path Sum III (LC 437)
public int pathSum(TreeNode root, int targetSum) {
    Map<Long, Integer> prefixSums = new HashMap<>();
    prefixSums.put(0L, 1);
    return dfs(root, 0L, targetSum, prefixSums);
}

private int dfs(TreeNode node, long currentSum, int target,
                Map<Long, Integer> prefixSums) {
    if (node == null) return 0;

    currentSum += node.val;
    int count = prefixSums.getOrDefault(currentSum - target, 0);

    prefixSums.put(currentSum, prefixSums.getOrDefault(currentSum, 0) + 1);

    count += dfs(node.left, currentSum, target, prefixSums);
    count += dfs(node.right, currentSum, target, prefixSums);

    prefixSums.put(currentSum, prefixSums.get(currentSum) - 1);

    return count;
}
```

#### **Path Pattern Summary Table**
| Problem Type | Approach | Key Technique | Time | Space | LC # |
|--------------|----------|---------------|------|-------|------|
| **Simple Path Sum** | DFS recursion | Reduce sum | O(n) | O(h) | 112 |
| **All Paths** | DFS + backtrack | Track path | O(n) | O(h) | 113 |
| **Path Strings** | DFS + string | Concatenate | O(n) | O(h) | 257 |
| **Sum Numbers** | DFS + accumulate | Build number | O(n) | O(h) | 129 |
| **Max Path Sum** | DFS + global | Track max | O(n) | O(h) | 124 |
| **Consecutive** | DFS + counter | Track length | O(n) | O(h) | 298 |
| **Prefix Sum** | DFS + hashmap | Prefix technique | O(n) | O(n) | 437 |

#### **Key Concepts & Principles**

1. **Root-to-Leaf Paths**
   - Always check for leaf nodes: `not node.left and not node.right`
   - Reduce target sum at each level
   - Return result at leaf nodes

2. **Backtracking Pattern**
   - Add current node to path
   - Recurse on children
   - Remove current node from path (restore state)
   - Essential for finding all paths

3. **Global State**
   - Use nonlocal/class variable for maximum values
   - Update during traversal
   - Return contribution, not final answer

4. **Path Through Node**
   - For max path: left_max + node.val + right_max
   - For return: node.val + max(left_max, right_max)
   - Use max(0, ...) to ignore negative contributions

5. **Prefix Sum Technique**
   - Track cumulative sum from root
   - Use hashmap: prefixSum[currentSum - target] = count
   - Backtrack by decrementing counts

#### **Common Mistakes & Pitfalls**

**🚫 Mistake 1: Not Checking Leaf Nodes**
```python
# BAD: Doesn't verify it's a leaf
if root.val == target:
    return True

# GOOD: Check both children are None
if not root.left and not root.right and root.val == target:
    return True
```

**🚫 Mistake 2: Forgetting to Backtrack**
```python
# BAD: Path grows indefinitely
def dfs(node, path):
    path.append(node.val)
    dfs(node.left, path)

# GOOD: Remove after recursion
def dfs(node, path):
    path.append(node.val)
    dfs(node.left, path)
    path.pop()
```

**🚫 Mistake 3: Shallow Copy in Results**
```python
# BAD: All results reference same list
result.append(path)

# GOOD: Create deep copy
result.append(path[:])  # or list(path)
```

**🚫 Mistake 4: Wrong Max Path Logic**
```python
# BAD: Includes both subtrees in return
def dfs(node):
    left = dfs(node.left)
    right = dfs(node.right)
    return node.val + left + right  # Wrong!

# GOOD: Return one path only
return node.val + max(left, right)
```

**🚫 Mistake 5: Not Handling Negative Values**
```python
# BAD: Negative paths reduce maximum
left_max = dfs(node.left)

# GOOD: Ignore negative contributions
left_max = max(0, dfs(node.left))
```

#### **Key Concepts & Principles**

1. **Balanced Construction**
   - Always pick middle element as root to ensure balance
   - Balanced BST has height O(log n)
   - Unbalanced can degenerate to O(n)

2. **Catalan Numbers**
   - Number of unique BSTs with n nodes = nth Catalan number
   - Formula: C(n) = (2n)! / ((n+1)! × n!)
   - Recurrence: C(n) = Σ C(i) × C(n-1-i) for i from 0 to n-1

3. **Traversal Properties**
   - **Preorder**: Can reconstruct BST uniquely (root first)
   - **Inorder**: Gives sorted sequence (need preorder/postorder too)
   - **Postorder**: Can reconstruct BST uniquely (root last)

4. **Optimization Techniques**
   - Use indices instead of array slicing (saves O(n) space per call)
   - Cache results for generate all problems
   - Use iterative approaches where possible

#### **Common Mistakes & Pitfalls**

**🚫 Mistake 1: Array Slicing Overhead**
```python
# BAD: Creates new arrays O(n) space each recursion
def build(nums):
    mid = len(nums) // 2
    root.left = build(nums[:mid])  # O(n) space

# GOOD: Use indices
def build(left, right):
    mid = (left + right) // 2
    root.left = build(left, mid - 1)
```

**🚫 Mistake 2: Off-by-One Errors**
```python
# BAD: Wrong boundary
mid = len(nums) // 2
root.left = build(nums[:mid-1])  # Skips element!

# GOOD: Correct boundaries
root.left = build(nums[:mid])  # Includes all left elements
```

**🚫 Mistake 3: Not Checking Bounds in Preorder**
```python
# BAD: No bounds checking
def build():
    val = preorder[idx]
    root = TreeNode(val)  # May violate BST property!

# GOOD: Check min/max bounds
def build(min_val, max_val):
    if val < min_val or val > max_val:
        return None
```

**🚫 Mistake 4: Wrong Catalan Recurrence**
```python
# BAD: Wrong combination
for i in range(1, n+1):
    dp[n] += dp[i] + dp[n-i]  # Should multiply!

# GOOD: Multiply left and right counts
for i in range(1, n+1):
    dp[n] += dp[i-1] * dp[n-i]
```

**🚫 Mistake 5: Wrong Middle Calculation**
```python
# BAD: Can overflow in Java/C++
mid = (left + right) / 2

# GOOD: Prevent overflow
mid = left + (right - left) // 2
```

## Problems by Pattern

### Pattern-Based Problem Classification

#### **Pattern 1: BST Search & Validation Problems**
| Problem | LC # | Difficulty | Key Technique | Template |
|---------|------|------------|---------------|----------|
| Validate Binary Search Tree | 98 | Medium | Min/Max bounds | Template 4 |
| Search in a BST | 700 | Easy | Binary search | Template 1 |
| Closest Binary Search Tree Value | 270 | Easy | Binary search | Template 1 |
| Inorder Successor in BST | 285 | Medium | Inorder property | Template 1 |
| Two Sum IV - Input is BST | 653 | Easy | Hash + Traversal | Template 5 |
| Find Mode in BST | 501 | Easy | Inorder traversal | Template 5 |

#### **Pattern 2: BST Insertion & Deletion Problems**
| Problem | LC # | Difficulty | Key Technique | Template |
|---------|------|------------|---------------|----------|
| Insert into a BST | 701 | Medium | Recursive insert | Template 2 |
| Delete Node in a BST | 450 | Medium | Three cases | Template 3 |
| Trim a Binary Search Tree | 669 | Medium | Recursive trim | Template 3 |

#### **Pattern 3: BST Traversal & Conversion Problems**
| Problem | LC # | Difficulty | Key Technique | Template |
|---------|------|------------|---------------|----------|
| Kth Smallest Element in BST | 230 | Medium | Inorder traversal | Template 5 |
| BST Iterator | 173 | Medium | Stack + Inorder | Template 5 |
| Convert BST to Greater Tree | 538 | Medium | Reverse inorder | Template 5 |
| Binary Search Tree to Greater Sum Tree | 1038 | Medium | Reverse inorder | Template 5 |
| Convert Sorted List to BST | 109 | Medium | Two pointers | Template 6 |
| Flatten BST to Sorted List | 426 | Medium | Inorder + linking | Template 5 |
| Increasing Order Search Tree | 897 | Easy | Inorder rebuild | Template 5 |

#### **Pattern 4: BST Construction Problems**
| Problem | LC # | Difficulty | Key Technique | Template |
|---------|------|------------|---------------|----------|
| Convert Sorted Array to BST | 108 | Easy | Binary search | Template 6 |
| Unique Binary Search Trees | 96 | Medium | DP/Catalan | Special |
| Unique Binary Search Trees II | 95 | Medium | Generate all | Template 6 |
| Serialize and Deserialize BST | 449 | Medium | Preorder encoding | Special |
| Construct BST from Preorder | 1008 | Medium | Stack/Recursion | Template 6 |
| Balance a Binary Search Tree | 1382 | Medium | Inorder + rebuild | Template 6 |

#### **Pattern 5: BST Properties & Range Problems**
| Problem | LC # | Difficulty | Key Technique | Template |
|---------|------|------------|---------------|----------|
| Lowest Common Ancestor of BST | 235 | Easy | BST property | Special |
| Minimum Distance Between BST Nodes | 783 | Easy | Inorder diff | Template 5 |
| Minimum Absolute Difference in BST | 530 | Easy | Inorder diff | Template 5 |
| Range Sum of BST | 938 | Easy | DFS with pruning | Template 1 |
| Split BST | 776 | Medium | Recursive split | Special |
| Largest BST Subtree | 333 | Medium | Bottom-up validation | Template 4 |

#### **Pattern 6: Path Problems**
| Problem | LC # | Difficulty | Key Technique | Template |
|---------|------|------------|---------------|----------|
| Path Sum | 112 | Easy | DFS recursion | Template 7 |
| Path Sum II | 113 | Medium | DFS + Backtrack | Template 7 |
| Binary Tree Paths | 257 | Easy | DFS + Path Track | Template 7 |
| Sum Root to Leaf Numbers | 129 | Medium | DFS + Accumulate | Template 7 |
| Binary Tree Maximum Path Sum | 124 | Hard | DFS + Global Max | Template 7 |
| Binary Tree Longest Consecutive Sequence | 298 | Medium | DFS + Counter | Template 7 |
| Path Sum III | 437 | Medium | Prefix Sum + DFS | Template 7 |

### Complete Problem List by Difficulty

#### Easy Problems (Foundation)
- LC 700: Search in a Binary Search Tree - Basic BST search
- LC 270: Closest Binary Search Tree Value - Modified search
- LC 108: Convert Sorted Array to BST - Basic construction
- LC 235: Lowest Common Ancestor of a BST - Use BST property
- LC 653: Two Sum IV - Input is a BST - Two pointers on tree
- LC 530: Minimum Absolute Difference in BST - Inorder property
- LC 783: Minimum Distance Between BST Nodes - Inorder traversal
- LC 897: Increasing Order Search Tree - Inorder rebuilding
- LC 938: Range Sum of BST - DFS with pruning
- LC 501: Find Mode in Binary Search Tree - Inorder + counting
- LC 112: Path Sum - Basic DFS path sum
- LC 257: Binary Tree Paths - DFS path tracking

#### Medium Problems (Core)
- LC 98: Validate Binary Search Tree - Classic validation
- LC 173: Binary Search Tree Iterator - Design pattern
- LC 230: Kth Smallest Element in a BST - Inorder application
- LC 450: Delete Node in a BST - Complex restructuring
- LC 701: Insert into a BST - Basic modification
- LC 285: Inorder Successor in BST - BST navigation
- LC 96: Unique Binary Search Trees - Catalan numbers
- LC 95: Unique Binary Search Trees II - Generate all trees
- LC 109: Convert Sorted List to BST - List to tree
- LC 449: Serialize and Deserialize BST - Encoding/decoding
- LC 538: Convert BST to Greater Tree - Reverse inorder
- LC 669: Trim a Binary Search Tree - Recursive trimming
- LC 776: Split BST - Advanced manipulation
- LC 333: Largest BST Subtree - Subtree validation
- LC 1008: Construct BST from Preorder - Stack approach
- LC 1038: Binary Search Tree to Greater Sum Tree - Accumulation
- LC 1382: Balance a Binary Search Tree - Inorder + rebuild balanced BST
- LC 426: Convert BST to Sorted Doubly Linked List - In-place conversion
- LC 113: Path Sum II - All root-to-leaf paths with target sum
- LC 129: Sum Root to Leaf Numbers - DFS accumulation
- LC 298: Binary Tree Longest Consecutive Sequence - Track sequence length
- LC 437: Path Sum III - Any path with target sum (prefix sum)

#### Hard Problems (Advanced)
- LC 99: Recover Binary Search Tree - Fix swapped nodes (see Pattern 8 below)
- LC 1373: Maximum Sum BST in Binary Tree - Complex validation
- LC 124: Binary Tree Maximum Path Sum - Node-to-node max path

### Template 8: Recover/Fix BST Problems

#### **Pattern Overview**
- **Description**: Detect and fix violations in BST by leveraging in-order traversal property
- **Recognition**: "Recover", "fix", "swapped nodes", "invalid BST"
- **Key Insight**: **In-order traversal of valid BST = strictly increasing sequence**
- **Time Complexity**: O(n)
- **Space Complexity**: O(h) for recursion, O(1) with Morris traversal

#### **Why In-Order Traversal? ⭐**

```
Core Insight:
  Valid BST in-order traversal → strictly INCREASING sequence

  Example valid BST:        In-order: [1, 2, 3, 4, 5, 6, 7]
         4                            ↑  ↑  ↑  ↑  ↑  ↑  ↑
        / \                          strictly increasing ✓
       2   6
      / \ / \
     1  3 5  7

  If two nodes SWAPPED:     In-order: [1, 6, 3, 4, 5, 2, 7]
         4                                 ↓        ↓
        / \                            DROP here  DROP here
       6   2    (swapped!)                 ↓        ↓
      / \ / \                         first=6   second=2
     1  3 5  7

Finding "drops" in sequence = finding swapped nodes!
```

#### **Two Cases of Swapped Nodes**

```
Case 1: ADJACENT nodes swapped (1 drop)
  Valid:   [1, 2, 3, 4, 5]
  Swapped: [1, 3, 2, 4, 5]  ← swap 2 and 3
                ↓
           one drop: 3 > 2
           first = 3 (prev at drop)
           second = 2 (current at drop)

Case 2: DISTANT nodes swapped (2 drops)
  Valid:   [1, 2, 3, 4, 5, 6, 7]
  Swapped: [1, 6, 3, 4, 5, 2, 7]  ← swap 2 and 6
                ↓           ↓
           drop 1: 6 > 3    drop 2: 5 > 2
           first = 6        second = 2 (update!)

Key: first is set at FIRST drop, second is ALWAYS updated at each drop
```

#### **Java Implementation**
```java
// LC 99 Recover Binary Search Tree
// Time: O(N), Space: O(H)

/**
 * Key variables:
 * - first:  The first node where order is violated (the larger one in first drop)
 * - second: The second node where order is violated (the smaller one in last drop)
 * - prev:   The previously visited node in in-order traversal
 */
private TreeNode first = null;
private TreeNode second = null;
private TreeNode prev = new TreeNode(Integer.MIN_VALUE);

public void recoverTree(TreeNode root) {
    if (root == null) return;

    // Step 1: In-order traversal to find the two swapped nodes
    inorder(root);

    // Step 2: Swap the values (not the nodes themselves!)
    if (first != null && second != null) {
        int temp = first.val;
        first.val = second.val;
        second.val = temp;
    }
}

private void inorder(TreeNode root) {
    if (root == null) return;

    // 1. Go Left
    inorder(root.left);

    // 2. Process current node - detect violation
    if (prev != null && root.val < prev.val) {
        // Found a DROP in the sequence!
        if (first == null) {
            first = prev;    // First drop: prev is the "too large" node
        }
        second = root;       // Always update: current is the "too small" node
    }

    // Update prev for next comparison
    prev = root;

    // 3. Go Right
    inorder(root.right);
}
```

#### **Python Implementation**
```python
# LC 99 Recover Binary Search Tree
class Solution:
    def recoverTree(self, root: TreeNode) -> None:
        self.first = None
        self.second = None
        self.prev = TreeNode(float('-inf'))

        def inorder(node):
            if not node:
                return

            # Go left
            inorder(node.left)

            # Detect violation (drop in sequence)
            if self.prev and node.val < self.prev.val:
                if self.first is None:
                    self.first = self.prev  # First drop
                self.second = node           # Always update

            self.prev = node

            # Go right
            inorder(node.right)

        inorder(root)

        # Swap values
        if self.first and self.second:
            self.first.val, self.second.val = self.second.val, self.first.val
```

#### **Why This Pattern Works**

```
The algorithm handles BOTH cases with ONE logic:

1. "first" is set only ONCE at the first drop
   → This captures the "too large" node that was swapped

2. "second" is ALWAYS updated at every drop
   → For adjacent swap: only 1 drop, second = the "too small" node ✓
   → For distant swap: 2 drops, second gets overwritten to correct node ✓

Example (distant swap: 2 and 6):
  Sequence: [1, 6, 3, 4, 5, 2, 7]

  At index 2 (val=3): prev=6, curr=3, 6 > 3 → DROP!
    first = 6 (set once)
    second = 3

  At index 5 (val=2): prev=5, curr=2, 5 > 2 → DROP!
    first = 6 (unchanged)
    second = 2 (updated!) ← This is the correct second node

  Swap 6 and 2 → BST recovered ✓
```

#### **Similar Problems Using In-Order Property**

| Problem | LC # | Difficulty | Key Technique | Why In-Order? |
|---------|------|------------|---------------|---------------|
| **Recover BST** | 99 | Medium | Detect 2 drops | Find swapped nodes in sorted sequence |
| **Validate BST** | 98 | Medium | Check increasing | Verify strictly increasing sequence |
| **Kth Smallest** | 230 | Medium | Count in-order | In-order gives sorted order |
| **BST Iterator** | 173 | Medium | Stack-based | Simulate in-order traversal |
| **Two Sum IV** | 653 | Easy | Two pointers | In-order gives sorted array |
| **Find Mode** | 501 | Easy | Track frequency | Process sorted sequence |
| **Min Diff in BST** | 530/783 | Easy | Track prev diff | Adjacent elements in sorted order |
| **Convert to Greater Tree** | 538/1038 | Medium | Reverse in-order | Process in descending order |

#### **Common Mistakes**

**🚫 Mistake 1: Trying to swap nodes instead of values**

```
Why swap VALUES, not NODE OBJECTS?

  Swapping node objects means rewiring parent/child pointers for BOTH nodes,
  plus handling edge cases (root, adjacent nodes, left/right children).
  This is extremely complex and error-prone.

  Swapping values is O(1) and preserves the tree structure:
  - All parent→child links stay the same
  - All left/right subtree relationships stay the same
  - Only the .val fields change — the BST property is restored

  Example:
       4                    4
      / \    swap vals      / \
     6   2   of 6 & 2 →   2   6    ← valid BST!
    / \ / \               / \ / \
   1  3 5  7             1  3 5  7

  The nodes stay in the SAME positions. Only their values change.
  Tree structure (edges) is completely untouched.
```

```java
// BAD: Swapping node references does NOTHING to the tree
TreeNode temp = first;
first = second;       // Only swaps local variable pointers!
second = temp;        // The actual tree is unchanged.

// GOOD: Swap the values stored inside the nodes
int temp = first.val;
first.val = second.val;
second.val = temp;
```

**🚫 Mistake 2: Only handling adjacent swaps**
```java
// BAD: Stops after first drop
if (prev.val > root.val) {
    first = prev;
    second = root;
    return;  // Wrong! Miss the second drop for distant swaps
}

// GOOD: Always update second, handles both cases
if (prev.val > root.val) {
    if (first == null) first = prev;
    second = root;  // Always update!
}
```

**🚫 Mistake 3: Not initializing prev correctly**
```java
// BAD: prev starts as null, first comparison fails
TreeNode prev = null;

// GOOD: prev starts with MIN_VALUE for safe comparison
TreeNode prev = new TreeNode(Integer.MIN_VALUE);
// Or check: if (prev != null && root.val < prev.val)
```

#### **Complexity Analysis**
- **Time**: O(N) — visit each node exactly once
- **Space**: O(H) — recursion stack (H = tree height)
- **Follow-up O(1) Space**: Use Morris Traversal (modifies tree temporarily)

#### **Key Takeaways**

```
1. In-order traversal of valid BST = STRICTLY INCREASING sequence
   → This is the MOST IMPORTANT property for BST problems

2. Detecting violations = finding "drops" in the sequence
   → prev.val > curr.val means we found a violation

3. Two-drop pattern handles both adjacent and distant swaps
   → first: set at FIRST drop (the larger misplaced node)
   → second: ALWAYS update (the smaller misplaced node)

4. Swap VALUES, not nodes
   → Much simpler, preserves tree structure
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

#### 1-1-6) Delete a node from BST


```java
// java
// LC 450

// V1-1
// https://youtu.be/LFzAoJJt92M?feature=shared
// https://github.com/neetcode-gh/leetcode/blob/main/java%2F0450-delete-node-in-a-bst.java
public TreeNode minimumVal(TreeNode root) {
    TreeNode curr = root;
    while (curr != null && curr.left != null) {
        curr = curr.left;
    }
    return curr;
}

public TreeNode deleteNode_1_1(TreeNode root, int key) {
    if (root == null)
        return null;

    if (key > root.val) {
        root.right = deleteNode_1_1(root.right, key);
    } else if (key < root.val) {
        root.left = deleteNode_1_1(root.left, key);
    } else {
        if (root.left == null) {
            return root.right;
        } else if (root.right == null) {
            return root.left;
        } else {
            TreeNode minVal = minimumVal(root.right);
            root.val = minVal.val;
            root.right = deleteNode_1_1(root.right, minVal.val);
        }
    }
    return root;
}
```

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

### 2-4) Split BST (LC 776) - Recursive Partition Pattern

#### Pattern: Recursive BST Partition
Split a BST into two valid BSTs based on a target value. This is a **partition** problem, NOT a delete problem — we preserve all nodes but redistribute them into two trees.

#### Theory: Why Split ≠ Delete

| Operation | Goal | Nodes Lost? | Return Value |
|-----------|------|-------------|--------------|
| **Delete** (LC 450) | Remove one node, keep one tree | Yes (1 node) | Single `TreeNode` |
| **Split** (LC 776) | Separate into two trees | No | `TreeNode[2]` array |

#### Core Idea

```
Return value: TreeNode[2]
  res[0] → BST with all values ≤ target
  res[1] → BST with all values > target
```

**Two cases based on root.val vs target:**

```
Case 1: root.val <= target
  → root belongs to LEFT partition (res[0])
  → But root.right may contain nodes > target
  → So SPLIT root.right, reconnect the pieces

Case 2: root.val > target
  → root belongs to RIGHT partition (res[1])
  → But root.left may contain nodes <= target
  → So SPLIT root.left, reconnect the pieces
```

#### Visual Walkthrough

```
Input:        target = 2
         4
        / \
       2   6
      / \ / \
     1  3 5  7

Step 1: root=4, val=4 > target=2 → root goes to RIGHT partition
        Split root.left (the subtree rooted at 2)

Step 2: root=2, val=2 <= target=2 → root goes to LEFT partition
        Split root.right (the subtree rooted at 3)

Step 3: root=3, val=3 > target=2 → root goes to RIGHT partition
        Split root.left (null) → returns [null, null]
        root.left = null (from split[1])
        Return [null, 3]

Back to Step 2: split of node 2's right returned [null, 3]
        node 2's right = split[0] = null  (was 3, now detached)
        Return [2→(left:1, right:null), 3]

Back to Step 1: split of node 4's left returned [2, 3]
        node 4's left = split[1] = 3  (reconnect!)
        Return [2, 4→(left:3, right:6)]

Result:
  res[0] (≤ 2):     res[1] (> 2):
       2                  4
      /                  / \
     1                  3   6
                           / \
                          5   7
```

#### Key Insight: The Reconnection

```
When root.val <= target and we split root.right:
  split[0] = nodes from right subtree that are still <= target
  split[1] = nodes from right subtree that are > target

  root.right = split[0]   ← keep the small ones attached to root
  return [root, split[1]] ← root is left partition, split[1] is right partition

When root.val > target and we split root.left:
  split[0] = nodes from left subtree that are <= target
  split[1] = nodes from left subtree that are > target

  root.left = split[1]    ← keep the big ones attached to root
  return [split[0], root] ← split[0] is left partition, root is right partition
```

#### Python Implementation
```python
# LC 776 Split BST
class Solution(object):
    def splitBST(self, root, V):
        if not root:
            return None, None
        # root belongs to LEFT partition
        elif root.val <= V:
            result = self.splitBST(root.right, V)
            root.right = result[0]  # keep <= V part
            return root, result[1]  # [smallTree, bigTree]
        # root belongs to RIGHT partition
        else:
            result = self.splitBST(root.left, V)
            root.left = result[1]   # keep > V part
            return result[0], root  # [smallTree, bigTree]
```

#### Java Implementation
```java
// LC 776 Split BST
// Time: O(H), Space: O(H) where H = height of BST
public TreeNode[] splitBST(TreeNode root, int target) {
    if (root == null) {
        return new TreeNode[]{null, null};
    }

    if (root.val <= target) {
        // root goes to LEFT partition (<=target)
        TreeNode[] split = splitBST(root.right, target);
        root.right = split[0];          // reconnect small part
        return new TreeNode[]{root, split[1]};
    } else {
        // root goes to RIGHT partition (>target)
        TreeNode[] split = splitBST(root.left, target);
        root.left = split[1];           // reconnect big part
        return new TreeNode[]{split[0], root};
    }
}
```

#### Similar Problems

| Problem | LC # | Similarity | Key Difference |
|---------|------|-----------|----------------|
| **Split BST** | 776 | Core pattern | Partition into 2 trees by value |
| **Delete Node in BST** | 450 | Both modify BST structure | Delete removes 1 node; split keeps all |
| **Trim a BST** | 669 | Both remove nodes outside range | Trim discards nodes; split preserves all in 2 trees |
| **Search in BST** | 700 | Same left/right branching logic | Search returns 1 node; split returns 2 trees |
| **Insert into BST** | 701 | Same recursive BST traversal | Insert adds; split partitions |
| **Merge Two BSTs** | - | Inverse operation | Split → 2 trees; Merge → 1 tree |

#### Complexity
- **Time**: O(H) — only visits nodes along one root-to-leaf path
- **Space**: O(H) — recursion stack depth = tree height

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

```java
// java
// LC 173
// V1
// IDEA: STACK
// https://leetcode.com/problems/binary-search-tree-iterator/solutions/52647/nice-comparison-and-short-solution-by-st-jcmg/
public class BSTIterator_1 {

    private TreeNode visit;
    private Stack<TreeNode> stack;

    public BSTIterator_1(TreeNode root) {
        visit = root;
        stack = new Stack();
    }

    public boolean hasNext() {
        return visit != null || !stack.empty();
    }

    /**
     * NOTE !!! next() method logic
     */
    public int next() {
  /**
   * NOTE !!!
   *
   *  since BST property is as below:
   *
   *  - For each node
   *     - `left sub node < than current node`
   *     - `right sub node > than current node`
   *
   *  so, within the loop over `left node`, we keep finding
   *  the `smaller` node, and find the `relative smallest` node when the while loop ended
   *  -> so the `relative smallest` node is the final element we put into stack
   *  -> so it's also the 1st element pop from stack
   *  -> which is the `next small` node
   */
  while (visit != null) {
            stack.push(visit);
            visit = visit.left;
        }
        // Stack: LIFO (last in, first out)
        /**
         * NOTE !!! we pop the `next small` node from stack
         */
        TreeNode next = stack.pop();
        /**
         * NOTE !!!  and set the `next small` node's right node as next node
         */
        visit = next.right;
        /**
         * NOTE !!! we return the `next small` node's right node val
         *          because of the requirement
         *
         *         e.g. : int next() Moves the pointer to the right, then returns the number at the pointer.
         */
        return next.val;
    }
}
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
> See also: **Pattern 6.5** above for core idea, multiple approaches, and similar problems.

```python
# LC 95 Unique Binary Search Trees II
# V1
# IDEA : RECURSION (Cartesian product of left/right subtrees)
# https://leetcode.com/problems/unique-binary-search-trees-ii/solution/
#
# Core idea:
#   1. Pick i as root → left subtree from [start, i-1], right from [i+1, end]
#   2. Cartesian product of all left × right subtrees
#   3. Base case: start > end → return [None] (empty subtree)
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

```java
// LC 95 — Java plain recursion
// See UniqueBinarySearchTrees2.java for memoized, iterative DP,
// and space-optimized DP approaches
public List<TreeNode> generateTrees(int n) {
    if (n == 0) return new ArrayList<>();
    return buildTrees(1, n);
}

private List<TreeNode> buildTrees(int start, int end) {
    List<TreeNode> allTrees = new ArrayList<>();
    if (start > end) { allTrees.add(null); return allTrees; }
    for (int i = start; i <= end; i++) {
        List<TreeNode> leftSubtrees = buildTrees(start, i - 1);
        List<TreeNode> rightSubtrees = buildTrees(i + 1, end);
        for (TreeNode left : leftSubtrees) {
            for (TreeNode right : rightSubtrees) {
                TreeNode root = new TreeNode(i);
                root.left = left;
                root.right = right;
                allTrees.add(root);
            }
        }
    }
    return allTrees;
}
```

### 2-8) Insert into a Binary Search Tree
```python
# LC 701 Insert into a Binary Search Tree
# VO : recursion + dfs
class Solution(object):
    def insertIntoBST(self, root, val):
        if not root: 
            return TreeNode(val)
        if root.val < val: 
            root.right = self.insertIntoBST(root.right, val);
        elif root.val > val: 
            root.left = self.insertIntoBST(root.left, val);
        return(root)
```

### 2-9) Validate Binary Search Tree
```python
# LC 98 Validate Binary Search Tree
# V0
# IDEA : BFS
#  -> trick : we make sure current tree and all of sub tree are valid BST
#   -> not only compare tmp.val with tmp.left.val, tmp.right.val,
#   -> but we need compare if tmp.left.val is SMALLER then `previous node val`
#   -> but we need compare if tmp.right.val is BIGGER then `previous node val`
class Solution(object):
    def isValidBST(self, root):
        if not root:
            return True
        _min = -float('inf')
        _max = float('inf')
        ### NOTE : we set q like below
        q = [[root, _min, _max]]
        while q:
            for i in range(len(q)):
                tmp, _min, _max = q.pop(0)
                if tmp.left:
                    """
                    ### NOTE : below condition
                    ### NOTE : we compare "tmp.left.val" with others (BEFORE we visit tmp.left)
                    """
                    if tmp.left.val >= tmp.val or tmp.left.val <= _min:
                        return False
                    ### NOTE : we append tmp.val as _max
                    q.append([tmp.left, _min, tmp.val])
                if tmp.right:
                    """
                    ### NOTE : below condition
                    ### NOTE : we compare "tmp.right.val" with others (BEFORE we visit tmp.right)
                    """
                    if tmp.right.val <= tmp.val or tmp.right.val >= _max:
                        return False
                    ### NOTE : we append tmp.val as _min
                    q.append([tmp.right, tmp.val, _max])
        return True

# V0''
# IDEA: RECURSION 
class Solution(object):
    def isValidBST(self, root):
        """
        :type root: TreeNode
        :rtype: bool
        """
        return self.valid(root, float('-inf'), float('inf'))
        
    def valid(self, root, min_, max_):
        if not root: return True
        if root.val >= max_ or root.val <= min_:
            return False
        return self.valid(root.left, min_, root.val) and self.valid(root.right, root.val, max_)
```


### 2-10) Trim a Binary Search Tree (LC 669) - Recursive Pruning Pattern

#### Pattern: Recursive BST Pruning
Trim a BST so all node values lie within `[L, R]`. This leverages BST property to prune entire subtrees efficiently.

#### Core Idea

```
Three cases based on root.val vs [L, R] range:

Case 1: root.val < L
  → root and entire LEFT subtree are too small (all < L)
  → DISCARD root and left subtree, return trimmed RIGHT subtree

Case 2: root.val > R
  → root and entire RIGHT subtree are too large (all > R)
  → DISCARD root and right subtree, return trimmed LEFT subtree

Case 3: L <= root.val <= R
  → root is in range, KEEP it
  → Recursively trim both subtrees
```

#### Visual Walkthrough

```
Input:        L=1, R=3
         3
        / \
       0   4
        \
         2
        /
       1

Step 1: root=3, val=3 in [1,3] → KEEP, trim both subtrees

Step 2: root=0, val=0 < L=1 → DISCARD 0, return trimBST(right=2)

Step 3: root=2, val=2 in [1,3] → KEEP, trim both subtrees

Step 4: root=1, val=1 in [1,3] → KEEP (leaf)

Step 5: root=4, val=4 > R=3 → DISCARD 4, return trimBST(left=null) = null

Result:
     3
    /
   2
  /
 1
```

#### Key Insight: Why `return trimBST(root.right, L, R)` instead of `return null`?

```
When root.val < L:

  ❌ WRONG: return null
     → This would throw away the ENTIRE subtree, but some right descendants
        could still be in range [L, R]!

  ✅ RIGHT: return trimBST(root.right, L, R)
     → BST property guarantees: left subtree < root.val < right subtree
     → Since root.val < L, ALL left children < root.val < L → all too small
     → But right children > root.val, so SOME may be >= L (in range!)
     → We discard root + left subtree, but continue searching right subtree

  Example:  L=3, R=5
         2          root=2 < L=3, discard 2 and left(1)
        / \         but right subtree has 4 which is in [3,5]!
       1   4        → return trimBST(4, 3, 5) → returns 4 ✅
          / \
         3   5

Same logic (mirrored) for root.val > R:
  → Discard root + right subtree (all too large)
  → But left children may still be <= R (in range!)
  → return trimBST(root.left, L, R)
```

#### Java Implementation
```java
// LC 669 Trim a Binary Search Tree
// Time: O(n), Space: O(h) where h = height
public TreeNode trimBST(TreeNode root, int L, int R) {
    if (root == null) {
        return root;
    }

    // Case 1: root too small, discard root + left subtree
    if (root.val < L) {
        return trimBST(root.right, L, R);
    }

    // Case 2: root too large, discard root + right subtree
    if (root.val > R) {
        return trimBST(root.left, L, R);
    }

    // Case 3: root in range, keep it and trim both subtrees
    root.left = trimBST(root.left, L, R);
    root.right = trimBST(root.right, L, R);

    return root;
}
```

#### Python Implementation
```python
# LC 669 Trim a Binary Search Tree
class Solution:
    def trimBST(self, root: TreeNode, low: int, high: int) -> TreeNode:
        if not root:
            return None

        # root too small → discard root + left subtree
        if root.val < low:
            return self.trimBST(root.right, low, high)

        # root too large → discard root + right subtree
        if root.val > high:
            return self.trimBST(root.left, low, high)

        # root in range → keep it, trim both subtrees
        root.left = self.trimBST(root.left, low, high)
        root.right = self.trimBST(root.right, low, high)

        return root
```

#### Comparison: Trim vs Split vs Delete

| Operation | LC # | Nodes Removed? | Return Value | Key Difference |
|-----------|------|----------------|--------------|----------------|
| **Trim** | 669 | Yes (out of range) | Single `TreeNode` | Keeps nodes in [L,R], discards rest |
| **Split** | 776 | No | `TreeNode[2]` | Partitions into 2 trees, keeps ALL nodes |
| **Delete** | 450 | Yes (1 node) | Single `TreeNode` | Removes exactly 1 specific node |

#### Complexity
- **Time**: O(n) — visit each node at most once
- **Space**: O(h) — recursion stack depth = tree height

#### Common Mistakes

**🚫 Mistake 1: Forgetting to reconnect trimmed subtrees**
```java
// BAD: Trims but doesn't reconnect
if (root.val < L) {
    trimBST(root.right, L, R);  // Missing return!
}

// GOOD: Return the trimmed subtree
if (root.val < L) {
    return trimBST(root.right, L, R);
}
```

**🚫 Mistake 2: Not recursing on both subtrees when root is in range**
```java
// BAD: Only trims one side
if (root.val >= L && root.val <= R) {
    root.left = trimBST(root.left, L, R);
    return root;  // Forgot to trim right!
}

// GOOD: Trim both sides
root.left = trimBST(root.left, L, R);
root.right = trimBST(root.right, L, R);
return root;
```

**Similar Problems:**
- LC 669 Trim a Binary Search Tree (this pattern)
- LC 450 Delete Node in a BST (remove single node, more complex restructuring)
- LC 776 Split BST (partition into two trees, keeps all nodes)
- LC 98 Validate Binary Search Tree (same BST property reasoning)
- LC 938 Range Sum of BST (same "skip subtree" optimization using BST property)

### 2-10-1) Convert BST to Greater Tree
```python
# LC 538
```

### 2-11) Binary Search Tree to Greater Sum Tree
```python
# LC 1038
# Similar to LC 538 - Convert BST to Greater Tree
# Uses reverse inorder traversal
```

## Pattern Selection Strategy

```
BST Problem Analysis Flowchart:

1. Does the problem require finding/searching a value?
   ├── YES → Use Search Template (1)
   │   ├── Exact match? → Basic binary search
   │   ├── Closest value? → Track min difference
   │   └── Range query? → Prune based on BST property
   └── NO → Continue to 2

2. Does the problem require modifying the BST structure?
   ├── YES → Check modification type
   │   ├── Insert new node? → Use Insertion Template (2)
   │   ├── Delete existing node? → Use Deletion Template (3)
   │   └── Trim/Split tree? → Use modified Deletion Template
   └── NO → Continue to 3

3. Does the problem use the sorted property of BST?
   ├── YES → Use Inorder Template (5)
   │   ├── Kth element? → Inorder with counter
   │   ├── Convert to list? → Inorder traversal
   │   └── Range sum? → Modified inorder
   └── NO → Continue to 4

4. Does the problem require validating BST properties?
   ├── YES → Use Validation Template (4)
   │   ├── Entire tree? → Min/max bounds approach
   │   └── Find largest valid subtree? → Bottom-up validation
   └── NO → Continue to 5

5. Does the problem involve constructing a BST?
   ├── YES → Use Construction Template (6)
   │   ├── From sorted array? → Binary search approach
   │   ├── From traversal? → Use BST properties
   │   └── Generate all possible? → Recursive generation
   └── NO → Continue to 6

6. Does the problem involve paths in the tree?
   ├── YES → Use Path Template (7)
   │   ├── Root-to-leaf sum? → DFS with target reduction
   │   ├── All paths? → DFS with backtracking
   │   ├── Max path sum? → DFS with global variable
   │   └── Any path with sum? → Prefix sum technique
   └── NO → Use Universal Template or reconsider

```

### Decision Framework
1. **Identify BST property usage**: Can you leverage left < root < right?
2. **Choose operation type**: Search, Insert, Delete, Validate, or Construct
3. **Select traversal method**: Inorder for sorted, others for structure
4. **Optimize with BST property**: Prune unnecessary branches

## Summary & Quick Reference

### Complexity Quick Reference
| Operation | Average Case | Worst Case | Best Case | Space |
|-----------|--------------|------------|-----------|-------|
| Search | O(log n) | O(n) | O(1) | O(h) |
| Insert | O(log n) | O(n) | O(1) | O(h) |
| Delete | O(log n) | O(n) | O(1) | O(h) |
| Validate | O(n) | O(n) | O(n) | O(h) |
| Inorder Traversal | O(n) | O(n) | O(n) | O(h) |
| Construction | O(n) | O(n) | O(n) | O(n) |

*Note: h = height of tree, n = number of nodes*

### Template Quick Reference
| Template | Best For | Avoid When | Key Pattern |
|----------|----------|------------|-------------|
| Search | Finding values | Need all nodes | `if val < root.val: go left` |
| Insertion | Adding nodes | Balancing needed | Always insert as leaf |
| Deletion | Removing nodes | Multiple deletions | Three cases handling |
| Validation | Checking BST | Simple traversal | Min/max bounds |
| Inorder | Sorted operations | Random access | Left-root-right |
| Construction | Building BST | Incremental updates | Divide & conquer |

### Common Patterns & Tricks

#### **Pattern: Inorder Gives Sorted Sequence**
```python
def get_sorted_values(root):
    """BST inorder traversal always gives sorted order"""
    def inorder(node):
        if not node:
            return []
        return inorder(node.left) + [node.val] + inorder(node.right)
    return inorder(root)
```

#### **Pattern: Reverse Inorder for Descending**
```python
def convert_to_greater_tree(root):
    """Process nodes from largest to smallest"""
    self.sum = 0
    def reverse_inorder(node):
        if not node:
            return
        reverse_inorder(node.right)
        self.sum += node.val
        node.val = self.sum
        reverse_inorder(node.left)
    reverse_inorder(root)
    return root
```

#### **Pattern: Using BST Property for Optimization**
```python
def range_sum_bst(root, low, high):
    """Prune branches that can't contain values in range"""
    if not root:
        return 0
    
    # Prune left subtree if root is already too small
    if root.val < low:
        return range_sum_bst(root.right, low, high)
    
    # Prune right subtree if root is already too large
    if root.val > high:
        return range_sum_bst(root.left, low, high)
    
    # Root is in range, include it and check both subtrees
    return (root.val + 
            range_sum_bst(root.left, low, high) +
            range_sum_bst(root.right, low, high))
```

### Problem-Solving Steps
1. **Identify BST property usage**: Can you use left < root < right?
2. **Choose appropriate template**: Based on operation type
3. **Consider edge cases**: Empty tree, single node, duplicates
4. **Optimize with pruning**: Skip unnecessary subtrees
5. **Test with skewed trees**: Worst case scenarios

### Common Mistakes & Tips

**🚫 Common Mistakes:**
- **Not using BST property**: Treating BST like regular binary tree
- **Forgetting inorder = sorted**: Missing optimization opportunity
- **Wrong deletion handling**: Not covering all three cases
- **Incorrect validation**: Only checking parent-child, not entire subtree
- **Modifying while traversing**: Can break BST property

**✅ Best Practices:**
- **Always leverage BST property**: Prune search space when possible
- **Use inorder for sorted needs**: Don't sort separately
- **Handle duplicates explicitly**: Decide if allowed in your BST
- **Consider tree balance**: Mention O(n) worst case in interviews
- **Test with edge cases**: Empty, single node, all left/right

### Interview Tips
1. **Clarify BST properties**: Can there be duplicates? Is it balanced?
2. **State complexity**: Mention average O(log n) and worst O(n)
3. **Consider self-balancing**: Mention AVL/Red-Black trees if relevant
4. **Use BST property**: Show you understand the optimization
5. **Handle all cases**: Especially for deletion (0, 1, 2 children)

### Related Topics
- **Self-Balancing BSTs**: AVL Tree, Red-Black Tree (guaranteed O(log n))
- **B-Trees**: For database indexes (multiple keys per node)
- **Binary Heap**: Different property (parent > children)
- **Trie**: Prefix tree for strings
- **Segment Tree**: Range queries and updates

### Java Implementation Notes
```java
// Java BST Node
class TreeNode {
    int val;
    TreeNode left, right;
    TreeNode(int x) { val = x; }
}

// Iterative inorder with Stack
Stack<TreeNode> stack = new Stack<>();
TreeNode curr = root;
while (curr != null || !stack.isEmpty()) {
    while (curr != null) {
        stack.push(curr);
        curr = curr.left;
    }
    curr = stack.pop();
    // Process curr
    curr = curr.right;
}
```

### Python Implementation Notes
```python
# TreeNode class
class TreeNode:
    def __init__(self, val=0, left=None, right=None):
        self.val = val
        self.left = left
        self.right = right

# Generator for memory-efficient inorder
def inorder_generator(root):
    if root:
        yield from inorder_generator(root.left)
        yield root.val
        yield from inorder_generator(root.right)
```

## 🚀 Quick Decision Tree: Choosing the Right BST Template

Use this flowchart to quickly identify which template to use for your BST problem:

```
┌─────────────────────────────────────────────────┐
│ START: Analyze the BST Problem                  │
└──────────────────┬──────────────────────────────┘
                   │
                   ▼
        ┌──────────────────────┐
        │ What's the main task? │
        └──────┬───────────────┘
               │
      ┌────────┴────────┐
      │                  │
      ▼                  ▼
┌──────────┐      ┌──────────┐
│ SEARCH?  │      │ MODIFY?  │
└────┬─────┘      └────┬─────┘
     │                 │
     │                 ├─► Insert node? → Template 2 (Insertion)
     │                 ├─► Delete node? → Template 3 (Deletion)
     │                 └─► Trim/Balance? → Template 3 or 6.4 (Balance BST)
     │
     ├─► Find value? → Template 1 (Search)
     ├─► Validate BST? → Template 4 (Validation)
     └─► Closest value? → Template 1 with tracking

      ▼
┌─────────────┐      ┌──────────────┐      ┌──────────────┐
│ TRAVERSAL?  │      │ CONSTRUCT?   │      │ PATH-BASED?  │
└──────┬──────┘      └──────┬───────┘      └──────┬───────┘
       │                     │                      │
       │                     │                      ├─► Root-to-leaf sum? → Template 7.1
       │                     │                      ├─► All paths? → Template 7.2
       │                     │                      ├─► Max path sum? → Template 7.5
       │                     │                      └─► Any path (prefix sum)? → Template 7.7
       │                     │
       │                     ├─► From sorted array? → Template 6.1
       │                     ├─► From sorted list? → Template 6.2
       │                     ├─► From preorder? → Template 6.3
       │                     ├─► Balance existing BST? → Template 6.4
       │                     ├─► Generate all unique? → Template 6.5
       │                     └─► Count unique? → Template 6.6
       │
       ├─► Kth smallest? → Template 5 (Inorder)
       ├─► Convert to list? → Template 5 (Inorder)
       ├─► Range sum/query? → Template 1 with pruning
       └─► Iterator? → Template 5 (Stack-based inorder)
```

### 📋 Quick Template Selection Table

| If Problem Asks... | Use Template | Key Technique | Typical LC Problems |
|-------------------|--------------|---------------|---------------------|
| "Find/search value" | Template 1 | Binary search property | 700, 270, 938 |
| "Insert into BST" | Template 2 | Recursive insertion | 701 |
| "Delete from BST" | Template 3 | Three-case handling | 450, 669 |
| "Is valid BST?" | Template 4 | Min/max bounds | 98, 333 |
| "Kth smallest/largest" | Template 5 | Inorder traversal | 230, 173 |
| "Convert sorted array" | Template 6.1 | Binary search middle | 108 |
| "Balance BST" | Template 6.4 | Inorder + rebuild | 1382 |
| "Path with target sum" | Template 7.1 | DFS + reduce sum | 112 |
| "All paths with sum" | Template 7.2 | DFS + backtracking | 113 |
| "Max path sum" | Template 7.5 | DFS + global max | 124 |
| "Any path with sum" | Template 7.7 | Prefix sum | 437 |

### 🎯 Recognition Patterns

**Keywords → Template Mapping:**
- **"Search", "find", "closest"** → Template 1 (Search)
- **"Insert", "add node"** → Template 2 (Insertion)
- **"Delete", "remove", "trim"** → Template 3 (Deletion)
- **"Valid", "validate", "is BST"** → Template 4 (Validation)
- **"Kth", "sorted", "inorder", "iterator"** → Template 5 (Inorder)
- **"Construct", "build", "convert", "balance", "generate"** → Template 6 (Construction)
- **"Path", "sum", "maximum path", "consecutive"** → Template 7 (Path Problems)

### ⚡ Quick Decision Examples

**Example 1**: "Find the kth smallest element in a BST"
- Keyword: "kth" + "smallest"
- Answer: **Template 5** (Inorder traversal gives sorted order)

**Example 2**: "Convert a sorted array to a height-balanced BST"
- Keywords: "convert" + "sorted array"
- Answer: **Template 6.1** (Pick middle as root)

**Example 3**: "Find if there's a root-to-leaf path with given sum"
- Keywords: "path" + "sum"
- Answer: **Template 7.1** (Simple path sum)

**Example 4**: "Balance an existing BST"
- Keywords: "balance" + "existing BST"
- Answer: **Template 6.4** (Inorder + rebuild)

**Example 5**: "Count paths with given sum (not necessarily root-to-leaf)"
- Keywords: "any path" + "sum"
- Answer: **Template 7.7** (Prefix sum technique)

### 💡 Pro Tips for Template Selection

1. **Leverage BST Property**: If problem can use `left < root < right`, use Templates 1-6
2. **Path Problems**: Most work on any binary tree, not just BST (Template 7)
3. **Construction**: Check input type (array/list/traversal) → different template variant
4. **Modification**: Always return root after modification (Templates 2, 3, 6)
5. **When in Doubt**: Check if inorder traversal helps (Template 5)

---
**Must-Know Problems for Interviews**: LC 98, 108, 112, 113, 124, 173, 230, 235, 450, 700, 701, 1382
**Advanced Problems**: LC 99, 124, 298, 333, 437, 776, 1373
**Keywords**: BST, binary search tree, inorder, sorted, validation, search tree, path sum, DFS, backtracking, balance, construction