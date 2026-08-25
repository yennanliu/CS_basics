# BST (Binary Search Tree)

> **Scope** — Ordered trees only — what the `left < root < right` invariant buys you (O(log n) search, sorted inorder, range pruning, order statistics).

> **See also**: [bst_examples.md](./bst_examples.md) — the worked LC solution archive for these templates, plus the tree path-sum family; [bst_advanced.md](./bst_advanced.md) — order-statistic (rank) queries, the lazy O(h) iterator, recovering a broken BST and the construction-variant catalogue; [binary_tree.md](./binary_tree.md) — unordered binary trees; [tree.md](./tree.md) — general tree concepts; [segment_tree.md](./segment_tree.md) — range queries over an array instead of a tree.

## LeetCode Problem Lists

- [Binary Search Tree](https://leetcode.com/problem-list/binary-search-tree/)
- [Binary Tree](https://leetcode.com/problem-list/binary-tree/)

## Time Complexity

| Data structure | Search   | Insert   | Delete   | Min/Max  |
| -------------- | -------- | -------- | -------- | -------- |
| BST (average)  | O(log n) | O(log n) | O(log n) | O(log n) |

> Average case shown (reasonably balanced tree). **Worst case (unbalanced / skewed): all operations O(n).** Min/Max = leftmost / rightmost node, i.e. O(h). Full traversal is always **O(n)**. Space is **O(n)** for storage plus **O(h)** for recursion.

## Overview
**Binary Search Tree (BST)** is a binary tree data structure where each node follows the ordering property: left child < parent < right child. This property enables efficient searching, insertion, and deletion operations.

### Key Properties
- **Complexity**: see the [Time Complexity](#time-complexity) table above
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
}
```

### Basic Operations on a Tree Node

The plain recursion every BST template is built on. Neither of these uses the ordering
property — this is the shape you fall back to when `left < root < right` does not help.

#### **Mutate every node — plain pre-order recursion**
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

#### **Compare two trees for identity — LC 100**
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

    return isSameTree(root1.left, root2.left) && isSameTree(root1.right, root2.right);
}
```

> The Python treatment of LC 100 lives in [tree_examples.md](./tree_examples.md).

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

> **Where the other templates live.** Template numbering is preserved across the whole
> BST family, so the gaps below are not typos:
>
> - **Template 5b** (lazy BST iterator), **Template 8** (recover a broken BST), **Template 9**
>   (order-statistic / rank queries) and **Templates 3c / 4b / 6b** (detach, bounds-propagation
>   and construction variants) → [bst_advanced.md](./bst_advanced.md)
> - **Template 7** (root-to-leaf and node-to-node path problems — not BST-specific) plus the
>   worked LC archive → [bst_examples.md](./bst_examples.md)

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

**Java** — two alternatives shown under one name (they do not compile side by side; pick one).
`V1` is the generic tree scan, kept deliberately: it returns the same answer in O(n), and the
contrast with `V2`'s O(h) descent is the whole point of the BST property.
```java
// java
// V1 : general (for tree and BST)
boolean isInBST(TreeNode root, int target){
    if (root == null) return false;
    if (root.val == target) return true;

    return isInBST(root.left, target) || isInBST(root.right, target);
}

// V2 : optimization for BST
boolean isInBST(TreeNode root, int target){
    if (root == null) return false;
    if (root.val == target) return true;

    // optimize here
    if (root.val < target){
        return isInBST(root.right, target);
    }
    // root.val > target
    return isInBST(root.left, target);
}

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

**Java**:
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

**Java — LC 450**. The first block below is a **pseudo-code skeleton** (outline only, the
`// delete` branch is left empty on purpose) showing the recursion shape; the working
implementation follows it, with the three delete cases and the alternative "max of the left
subtree" swap spelled out in between.
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
        root.right = deleteNode(root.right, minNode.val);
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

### Template 3b: Trim BST (Range Pruning) ⭐

#### **Core Idea**

```text
Goal: keep ONLY nodes whose value lies in [low, high],
      WITHOUT changing the relative structure of the survivors.

Key insight — exploit BST property (left < root < right):
  - If root.val < low  → the ENTIRE left subtree is also < low.
                         Discard root AND its left subtree.
                         The answer must come from the RIGHT subtree.
  - If root.val > high → the ENTIRE right subtree is also > high.
                         Discard root AND its right subtree.
                         The answer must come from the LEFT subtree.
  - If low <= root.val <= high → keep root, recursively trim BOTH children.

Why return the recursive call (not None)?
  When a node is out of range we don't just delete it — its valid
  descendants must be "promoted" to take its place. Returning
  trimBST(child, ...) reconnects the next valid node to the parent.
```

```text
Visual (low=1, high=3):

        3                 3
       / \               /
      0   4    trim →   2
       \               /
        2             1
       /
      1

  - root 3 in range → keep, trim children
  - left child 0 < low(1) → drop 0 AND its (empty) left, recurse right → 2
  - 2 in range → keep, trim children
  - 1 in range → keep (leaf)
  - right child 4 > high(3) → drop 4 AND its (empty) right → None
```

#### **Pattern**

```python
# LC 669 - Trim a Binary Search Tree
# IDEA: BST PROPERTY + DFS (post-order reconnect)
# Time: O(n), Space: O(h)
class Solution(object):
    def trimBST(self, root, low, high):
        # Base case: empty tree needs no trimming
        if not root:
            return None

        # root too small → left subtree all < low too.
        # Drop root + left, answer is in the trimmed right subtree.
        if root.val < low:
            return self.trimBST(root.right, low, high)

        # root too large → right subtree all > high too.
        # Drop root + right, answer is in the trimmed left subtree.
        if root.val > high:
            return self.trimBST(root.left, low, high)

        # root in range → keep it, trim & reconnect both children.
        root.left = self.trimBST(root.left, low, high)
        root.right = self.trimBST(root.right, low, high)
        return root
```

```java
// java - LC 669
// Time: O(n), Space: O(h)
public TreeNode trimBST(TreeNode root, int low, int high) {
    if (root == null) return null;

    // root too small → promote trimmed right subtree
    if (root.val < low) return trimBST(root.right, low, high);

    // root too large → promote trimmed left subtree
    if (root.val > high) return trimBST(root.left, low, high);

    // in range → keep node, trim both children
    root.left = trimBST(root.left, low, high);
    root.right = trimBST(root.right, low, high);
    return root;
}
```

**🚫 Common Mistake**: returning `None` for an out-of-range node deletes its
**valid descendants** too. You must return the *trimmed surviving subtree* so
the next valid node gets reconnected to the parent.

```python
# BAD: drops valid descendants of an out-of-range node
if root.val < low or root.val > high:
    return None   # loses the in-range nodes hanging below!

# GOOD: promote the side that may still contain valid nodes
if root.val < low:  return self.trimBST(root.right, low, high)
if root.val > high: return self.trimBST(root.left,  low, high)
```

#### **Similar LeetCode Problems**
| Problem | LC # | Difficulty | Relation to Trim |
|---------|------|------------|------------------|
| Trim a Binary Search Tree | 669 | Medium | Core problem — prune nodes outside `[low, high]` |
| Delete Node in a BST | 450 | Medium | Same "recurse + reconnect via return value" pattern |
| Range Sum of BST | 938 | Easy | Same BST pruning logic, but sum instead of restructure |
| Split BST | 776 | Medium | Partition into two trees by value (mirror of trimming) |
| Convert Sorted Array to BST | 108 | Easy | Recursive build returning subtree roots (same reconnect idiom) |
| Insert into a BST | 701 | Medium | Recurse + return reconnected child pointer |

#### Comparison: Trim vs Split vs Delete

| Operation | LC # | Nodes Removed? | Return Value | Key Difference |
|-----------|------|----------------|--------------|----------------|
| **Trim** | 669 | Yes (out of range) | Single `TreeNode` | Keeps nodes in [L,R], discards rest |
| **Split** | 776 | No | `TreeNode[2]` | Partitions into 2 trees, keeps ALL nodes |
| **Delete** | 450 | Yes (1 node) | Single `TreeNode` | Removes exactly 1 specific node |

#### **Variation: Sum Instead of Restructure — LC 938**

Same three-way pruning as `trimBST`; only the combine step changes.
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

**Java** — node pointers instead of `float('inf')` sentinels, so `Integer.MIN_VALUE` is still a legal node value:
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
    return isValidBST(root.left, min, root) && isValidBST(root.right, root, max);
}
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

#### **Pattern: Reverse Inorder for Descending**

Right → root → left visits values in descending order, so a running sum turns each node
into the sum of everything greater than it (LC 538 / LC 1038). Sketch — `self.sum` assumes
this sits inside a `Solution` class:

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

#### **Java Implementation: From Sorted Array (LC 108)**
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
```

#### **The Other Construction Inputs**

`Pattern 6.2` (sorted linked list, LC 109), `6.3` (preorder, LC 1008), `6.4` (balance an
existing BST, LC 1382), `6.5` (generate all unique BSTs, LC 95) and `6.6` (count them,
LC 96) are collected as **Template 6b** in [bst_advanced.md](./bst_advanced.md).

The one line worth carrying in your head: **LC 1382 = LC 94 + LC 108** — flatten the BST
with an in-order walk (Template 5), then rebuild it with the mid-as-root recursion above.

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

### Template 10: LCA via BST Ordering — LC 235 ⭐⭐⭐⭐

**Key Idea**: in a BST you never search both subtrees for a lowest common ancestor.
Compare *both* targets against `root.val` and descend the single side that can still
contain them. The first node that does **not** send both targets the same way is the
**split point**, and the split point is the LCA.

```python
# python
# LC 235 Lowest Common Ancestor of a Binary Search Tree
# IDEA: BST PROPERTY — descend while p and q sit on the SAME side of root
# time = O(h), space = O(h) recursion (O(1) if rewritten as a while-loop)
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

> Contrast with **LC 236** (LCA in a general binary tree): with no ordering to exploit you
> must post-order *both* subtrees and combine the results — see
> [tree_lca_distance.md](./tree_lca_distance.md).

## Problems by Pattern

> Index only — no code. The `Template` column keeps the family-wide numbering: Templates 1–6 and 10 are on this page, Templates 5b / 8 / 9 / 3c / 4b / 6b are in [bst_advanced.md](./bst_advanced.md), and Template 7 (path problems) is in [bst_examples.md](./bst_examples.md).

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
| Convert Sorted List to BST | 109 | Medium | Two pointers | Template 6b |
| Flatten BST to Sorted List | 426 | Medium | Inorder + linking | Template 5 |
| Increasing Order Search Tree | 897 | Easy | Inorder rebuild | Template 5 |
| All Elements in Two BSTs | 1305 | Medium | Two lazy iterators + merge | Template 5b |

#### **Pattern 4: BST Construction Problems**
| Problem | LC # | Difficulty | Key Technique | Template |
|---------|------|------------|---------------|----------|
| Convert Sorted Array to BST | 108 | Easy | Binary search | Template 6 |
| Unique Binary Search Trees | 96 | Medium | DP/Catalan | Special |
| Unique Binary Search Trees II | 95 | Medium | Generate all | Template 6b |
| Serialize and Deserialize BST | 449 | Medium | Preorder encoding | Special |
| Construct BST from Preorder | 1008 | Medium | Stack/Recursion | Template 6b |
| Balance a Binary Search Tree | 1382 | Medium | Inorder + rebuild | Template 6b |

#### **Pattern 5: BST Properties & Range Problems**
| Problem | LC # | Difficulty | Key Technique | Template |
|---------|------|------------|---------------|----------|
| Lowest Common Ancestor of BST | 235 | Easy | BST property | Template 10 |
| Minimum Distance Between BST Nodes | 783 | Easy | Inorder diff | Template 5 |
| Minimum Absolute Difference in BST | 530 | Easy | Inorder diff | Template 5 |
| Range Sum of BST | 938 | Easy | DFS with pruning | Template 1 |
| Split BST | 776 | Medium | Recursive split | Special |
| Largest BST Subtree | 333 | Medium | Bottom-up validation | Template 4 |
| Kth Largest Element in a Stream | 703 | Easy | Size-augmented BST (rank query) | Template 9 |
| Maximum Difference Between Node and Ancestor | 1026 | Medium | Bounds propagation (min/max down) | Template 4b |
| Delete Nodes And Return Forest | 1110 | Medium | Recurse + return `null` to detach | Template 3c |

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
- LC 99: Recover Binary Search Tree - Fix swapped nodes (see Template 8 in [bst_advanced.md](./bst_advanced.md))
- LC 1373: Maximum Sum BST in Binary Tree - Complex validation
- LC 124: Binary Tree Maximum Path Sum - Node-to-node max path

## Summary & Quick Reference

**Complexity** — one table only, at the top of this sheet: see
[Time Complexity](#time-complexity). Everything below is about *which* template to reach for.

### Decision Flowchart
```text
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
   └── NO → Fall back to the plain BST descent (Template 1) or reconsider

```

### Which Template? — Quick Selection

| If Problem Asks... | Use Template | Key Technique | Typical LC Problems |
|-------------------|--------------|---------------|---------------------|
| "Find/search value" | Template 1 | Binary search property | 700, 270, 938 |
| "Insert into BST" | Template 2 | Recursive insertion | 701 |
| "Delete from BST" | Template 3 | Three-case handling | 450, 669 |
| "Is valid BST?" | Template 4 | Min/max bounds | 98, 333 |
| "Kth smallest/largest" | Template 5 | Inorder traversal | 230, 173 |
| "LCA of two nodes" | Template 10 | Descend the one side holding both | 235 |
| "Convert sorted array" | Template 6.1 | Binary search middle | 108 |
| "Balance BST" | Template 6.4 → [advanced](./bst_advanced.md) | Inorder + rebuild | 1382 |
| "Path with target sum" | Template 7.1 → [examples](./bst_examples.md) | DFS + reduce sum | 112 |
| "All paths with sum" | Template 7.2 → [examples](./bst_examples.md) | DFS + backtracking | 113 |
| "Max path sum" | Template 7.5 → [examples](./bst_examples.md) | DFS + global max | 124 |
| "Any path with sum" | Template 7.7 → [examples](./bst_examples.md) | Prefix sum | 437 |
| "Recover / fix a broken BST" | Template 8 → [advanced](./bst_advanced.md) | In-order + `prev` drop detection | 99, 501, 530 |
| "Kth largest in a stream / rank query" | Template 9 → [advanced](./bst_advanced.md) | Size-augmented BST | 703 |

### Recognition Patterns

**Keywords → Template Mapping:**
- **"Search", "find", "closest"** → Template 1 (Search)
- **"Insert", "add node"** → Template 2 (Insertion)
- **"Delete", "remove", "trim"** → Template 3 (Deletion)
- **"Valid", "validate", "is BST"** → Template 4 (Validation)
- **"Kth", "sorted", "inorder", "iterator"** → Template 5 (Inorder)
- **"Lowest common ancestor", "split point"** → Template 10 (LCA via ordering)
- **"Construct", "build", "convert", "balance", "generate"** → Template 6 (Construction)
- **"Path", "sum", "maximum path", "consecutive"** → Template 7 (Path Problems)
- **"Recover", "swapped", "fix", "adjacent values", "successor", "mode"** → Template 8 (In-order + `prev`)
- **"Stream", "after each insert", "rank", "how many are less than"** → Template 9 (Order-Statistic BST)

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

### Pro Tips for Template Selection

1. **Leverage BST Property**: If problem can use `left < root < right`, use Templates 1-6
2. **Path Problems**: Most work on any binary tree, not just BST (Template 7)
3. **Construction**: Check input type (array/list/traversal) → different template variant
4. **Modification**: Always return root after modification (Templates 2, 3, 6)
5. **When in Doubt**: Check if inorder traversal helps (Template 5)

---
**Must-Know Problems for Interviews**: LC 98, 108, 112, 113, 124, 173, 230, 235, 450, 700, 701, 1382
**Advanced Problems**: LC 99, 124, 298, 333, 437, 776, 1373
**Keywords**: BST, binary search tree, inorder, sorted, validation, search tree, path sum, DFS, backtracking, balance, construction