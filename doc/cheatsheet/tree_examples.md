# Tree — Worked LeetCode Examples

> **Scope** — The worked-solution archive for the tree patterns taught elsewhere: one canonical solution per problem per language, grouped by what the problem asks for rather than by technique.
> **See also**: [tree.md](./tree.md) — the concepts, traversal strategy and templates these examples apply; [tree_lca_distance.md](./tree_lca_distance.md) — LCA, distance and path problems; [tree_codec.md](./tree_codec.md) — serialization and codec problems; [tree_construction.md](./tree_construction.md) — build-a-tree problems.

## LeetCode Problem Lists

- [Tree](https://leetcode.com/problem-list/tree/)
- [Binary Tree](https://leetcode.com/problem-list/binary-tree/)
- [Depth-First Search](https://leetcode.com/problem-list/depth-first-search/)
- [Breadth-First Search](https://leetcode.com/problem-list/breadth-first-search/)

## Overview

Nineteen worked problems, grouped by the question they ask. Each one applies one of the nine
patterns in [tree.md](./tree.md); the pattern itself is explained there and is not restated here.

### Key Properties
- **Complexity**: O(N) time for all but LC 222 (O(log² N) on a complete tree) and LC 545 (three passes, still O(N))
- **Core Idea**: each example is one of tree.md's nine patterns plus the problem's own twist
- **When to Use**: after you know the pattern and want to see it land on a real problem

## Problem Categories

| Group | Problems | Dominant pattern |
|-------|----------|------------------|
| **Traversal & level order** | LC 199, 662, 2415 | BFS with per-level bookkeeping |
| **Structure & property** | LC 222, 101, 100, 951, 98, 110, 545 | post-order validation |
| **Height, depth & path** | LC 104, 111, 124, 1448 | height bottom-up / depth top-down |
| **Modification & multi-state** | LC 1110, 114, 617, 226, 968 | post-order rewiring, state returned upward |

## Traversal & Level-Order Examples

### 1) Tree Right Side View — LC 199

```java
// java

// LC 199
List<Integer> res = new ArrayList<>();
Queue<TreeNode> q = new LinkedList<>();
while (!q.isEmpty()) {
    TreeNode rightSide = null;
    int qLen = q.size();

    /**
     *  NOTE !!!
     *
     *   1) via for loop, we can get `most right node` (since the order is root -> left -> right)
     *   2) via `TreeNode rightSide = null;`, we can get the `most right node` object
     *      - rightSide could be `right sub tree` or `left sub tree`
     *
     *      e.g.
     *         1
     *       2   3
     *       
     *       
     *       1
     *     2   3
     *   4
     *
     */
    for (int i = 0; i < qLen; i++) {
        TreeNode node = q.poll();
        if (node != null) {
            rightSide = node;
            q.offer(node.left);
            q.offer(node.right);
        }
    }
    if (rightSide != null) {
        res.add(rightSide.val);
    }
}
```


**Python — DFS (right-first preorder), same O(n) work without a queue:**

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

### 2) Maximum Width of Binary Tree — LC 662
```python
# LC 662 Maximum Width of Binary Tree
# V0
# IDEA : defaultdict + DFS
from collections import defaultdict
class Solution:
    def widthOfBinaryTree(self, root):
        
        def dfs(node, level, idx):
            if node:
                d[level] += [idx]
                dfs(node.left, level+1, 2*idx)
                dfs(node.right, level+1, 2*idx+1)
                
        d = defaultdict(list)
        dfs(root, 0, 0)
        return max(v[-1] - v[0] + 1 for _, v in d.items())

# V0'
# IDEA : BFS
# IDEA : GIVEN index = idx -> its left tree index = idx*2 ; its right tree index = idx*2 + 1
#        -> SO GO THROUGH ALL LAYERS IN THE TREE, CALCULATE THEIR WIDTH, AND RETRUN THE MAX WIDTH WHICH IS THE NEEDED RESPONSE
from collections import defaultdict
class Solution(object):
    def widthOfBinaryTree(self, root):
        # edge case
        if not root:
            return 0
        layer = 0
        idx = 0
        q = [[root, layer, idx]]
        res = defaultdict(list)
        while q:
            for i in range(len(q)):
                tmp, layer, idx = q.pop(0)
                res[layer].append(idx)
                if tmp.left:
                    q.append([tmp.left, layer+1, idx*2])
                if tmp.right:
                    q.append([tmp.right, layer+1, idx*2+1])
        #print ("res = " + str(res))
        _res = [max(res[x]) - min(res[x]) + 1 for x in list(res.keys()) if res[x] > 1]
        #print ("_res = " + str(_res))
        return max(_res)
```

### 3) Reverse Odd Levels of Binary Tree — LC 2415

```java
// java
// LC 2415

// V0-1
// IDEA: DFS + `left, right, layer as helper func parameter` (fixed by gpt)
public TreeNode reverseOddLevels_0_1(TreeNode root) {
    if (root == null)
        return null;

    reverseHelper(root.left, root.right, 1);
    return root;
}

/**
 *  NOTE !!!
 *
 *   we NEED to setup 3 parameter in the helper func
 *
 *   1. left node
 *   2. right node
 *   3. layer
 *
 *
 *  NOTE !!!
 *
 *   the helper func return NOTHING !!! (e.g. void)
 */
private void reverseHelper(TreeNode left, TreeNode right, int level) {
    if (left == null || right == null)
        return;

    // Swap values if we're at an odd level
    if (level % 2 == 1) {
        int temp = left.val;
        left.val = right.val;
        right.val = temp;
    }

    /**  NOTE !!! below
     *
     *
     */
    // Recurse into symmetric children
    reverseHelper(left.left, right.right, level + 1);
    reverseHelper(left.right, right.left, level + 1);
}
```

## Structure & Property Examples

### 4) Node Count Algorithms — LC 222
```java
// get nodes count of binary tree

// get nodes count of perfect tree

// get nodes count of complete tree
// LC 222

// dfs
class Solution {
    public int countNodes(TreeNode root) {
        if (root == null) {
            return 0;
        }

        // Recursively count the nodes in the left subtree
        int leftCount = countNodes(root.left);

        // Recursively count the nodes in the right subtree
        int rightCount = countNodes(root.right);

        // Return the total count (current node + left subtree + right subtree)
        return 1 + leftCount + rightCount;
    }
}


// bfs
public int countNodes_2(TreeNode root) {

    if (root == null){
        return 0;
    }
    List<TreeNode> collected = new ArrayList<>();
    Queue<TreeNode> q = new LinkedList<>();
    q.add(root);
    while (!q.isEmpty()){
        TreeNode cur = q.poll();
        collected.add(cur);
        if (cur.left != null) {
            q.add(cur.left);
        }
        if (cur.right != null) {
            q.add(cur.right);
        }
    }

    //return this.count;
    System.out.println("collected = " + collected.toString());
    return collected.size();
}
```


> The three shapes of the counting problem — the generic tree, the perfect tree, and the complete tree (LC 222) — differ only in how much structure lets you skip.

#### Count nodes on a `basic` binary tree
```java
// java
// algorithm book (labu) p. 250
public int countNodes (TreeNode root){
    if (root == null) return 0;
    return 1 + countNodes(root.left) + countNodes(root.right);
}
```

#### Count nodes on a `perfect` binary tree
```java
// java
// algorithm book (labu) p. 250
public int countNodes(TreeNode root){
    int h = 0;
    // get tree depth
    while (root != null){
        root = root.left;
        h += 1;
    }
    // total nodes = 2**n + 1
    return (int)Math.pow(2, h) - 1;
}
```

#### Count nodes on a `complete` binary tree
```java
// java
// algorithm book (labu) p. 251
public int countNodes(TreeNode root){

    TreeNode l = root;
    TreeNode r = root;
    int hl = 0;
    int hr = 0;

    while (l != null){
        l = l = left;
        h1 += 1;
    }

    while (r != null){
        r = r.right;
        hr += 1;
    }

    // if left, right sub tree have SAME depth -> this is a perfect binary tree
    if (hl == hr){
        return (int)Math.pow(2, hl) - 1;
    }

    // if left, right sub tree have DIFFERENT depth, then we follow the simple bianry tree approach
    return 1 + countNodes(root.left) + countNodes(root.right);
}
```

### 5) check Symmetric Tree — LC 101
```python
# LC 101
class Solution(object):
    def isSymmetric(self, root):
        if not root:
            return True
        return self.mirror(root.left, root.right)

    def mirror(self, left, right):
        if not left or not right:
            return left == right
        if left.val != right.val:
            return False
        return self.mirror(left.left, right.right) and self.mirror(left.right, right.left)
```

### 6) Same Tree — LC 100
```python
# LC 100 Same tree
# V0
# IDEA : Recursion
class Solution(object):
    def isSameTree(self, p, q):
        
        def dfs(p, q):
            ### NOTE : we need to put this as 1st condition, or will cause "not sub tree" error
            if not p and not q:
                return True
            ### NOTE : elif (but not `if`)
            elif (not p and q) or (p and not q):
                return False
            ### NOTE : elif (but not `if`)
            elif p.val != q.val:
                return False
            return dfs(p.left, q.left) and dfs(p.right, q.right)
        
        res = dfs(p, q)
        return res
```

#### Variation — Flip Equivalent Binary Trees (LC 951)

**Twist**: same skeleton as LC 100, but at every node the children are allowed to be **swapped** — so instead of one recursive check, try BOTH pairings and `or` them.

```java
// java
// LC 951 - Flip Equivalent Binary Trees
// IDEA: LC 100 (Same Tree) + at each node accept `left-left / right-right`
//       OR the flipped `left-right / right-left` pairing
class Solution {
    public boolean flipEquiv(TreeNode root1, TreeNode root2) {
        // time = O(N), space = O(H)
        if (root1 == null && root2 == null) return true;
        if (root1 == null || root2 == null || root1.val != root2.val) return false;

        return (flipEquiv(root1.left, root2.left) && flipEquiv(root1.right, root2.right))   // not flipped
            || (flipEquiv(root1.left, root2.right) && flipEquiv(root1.right, root2.left));  // flipped
    }
}
```

```python
# python
# LC 951 - Flip Equivalent Binary Trees
# IDEA: Same Tree check, but allow the children pair to be swapped at each node
class Solution:
    def flipEquiv(self, root1, root2):
        # time = O(N), space = O(H)
        if not root1 and not root2:
            return True
        if not root1 or not root2 or root1.val != root2.val:
            return False
        return (self.flipEquiv(root1.left, root2.left) and
                self.flipEquiv(root1.right, root2.right)) or \
               (self.flipEquiv(root1.left, root2.right) and
                self.flipEquiv(root1.right, root2.left))
```

> Do NOT try to "normalize" both trees first (e.g. sorting children by value) — values are only unique in this problem's constraints; the two-way check is the general form.

### 7) Validate Binary Search Tree — LC 98
```python
# 98. Validate Binary Search Tree
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
                    ### NOTE : below condition
                    if tmp.left.val >= tmp.val or tmp.left.val <= _min:
                        return False
                    ### NOTE : we append tmp.val as _max
                    q.append([tmp.left, _min, tmp.val])
                if tmp.right:
                    ### NOTE : below condition
                    if tmp.right.val <= tmp.val or tmp.right.val >= _max:
                        return False
                    ### NOTE : we append tmp.val as _min
                    q.append([tmp.right, tmp.val, _max])
        return True

# V0'
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

### 8) Balanced Binary Tree — LC 110
```java
// java
// LC 110


// V0
// IDEA : DFS
// https://www.bilibili.com/video/BV1Ug411S7my/?share_source=copy_web
public boolean isBalanced(TreeNode root) {
    // edge
    if (root == null) {
        return true;
    }
    if (root.left == null && root.right == null) {
        return true;
    }

    int leftDepth = getDepthDFS(root.left);
    int rightDepth = getDepthDFS(root.right);

    // check if `current` node is `balanced`
    if (Math.abs(leftDepth - rightDepth) > 1) {
        return false;
    }

    // dfs call
    // recursively check if `sub left node` and  `sub right node` are `balanced`
    return isBalanced(root.left) && isBalanced(root.right);
}

// LC 104
public int getDepthDFS(TreeNode root) {
    if (root == null) {
        return 0;
    }

  return Math.max(getDepthDFS(root.left), getDepthDFS(root.right)) + 1;
}

// V1
// IDEA :  TOP DOWN RECURSION
// https://leetcode.com/problems/balanced-binary-tree/editorial/
// Recursively obtain the height of a tree. An empty tree has -1 height
private int height(TreeNode root) {
    // An empty tree has height -1
    if (root == null) {
        return -1;
    }
    return 1 + Math.max(height(root.left), height(root.right));
}

public boolean isBalanced(TreeNode root) {
    // An empty tree satisfies the definition of a balanced tree
    if (root == null) {
        return true;
    }

    // Check if subtrees have height within 1. If they do, check if the
    // subtrees are balanced
    return Math.abs(height(root.left) - height(root.right)) < 2
            && isBalanced(root.left)
            && isBalanced(root.right);
}
```

### 9) Boundary of Binary Tree — LC 545
```python
# LC 545. Boundary of Binary Tree
# V0
# IDEA : DFS
# https://xiaoguan.gitbooks.io/leetcode/content/LeetCode/545-boundary-of-binary-tree-medium.html
# https://www.cnblogs.com/lightwindy/p/9583723.html
class Solution(object):
    def boundaryOfBinaryTree(self, root):
        def leftBoundary(root, nodes):
            if not root or (not root.left and not root.right):
                return
            nodes.append(root.val)
            """
            NOTE this !!!
            """
            if not root.left:
                leftBoundary(root.right, nodes)
            else:
                leftBoundary(root.left, nodes)
 
        def rightBoundary(root, nodes):
            if not root or (not root.left and not root.right):
                return
            """
            NOTE this !!!
            """
            if not root.right:
                rightBoundary(root.left, nodes)
            else:
                rightBoundary(root.right, nodes)
            nodes.append(root.val)
 
        def leaves(root, nodes):
            if not root:
                return
            if not root.left and not root.right:
                nodes.append(root.val)
                return
            leaves(root.left, nodes)
            leaves(root.right, nodes)
 
        if not root:
            return []
 
        nodes = [root.val]
        leftBoundary(root.left, nodes)
        """
        NOTE this !!!
        """
        leaves(root.left, nodes)
        leaves(root.right, nodes)
        rightBoundary(root.right, nodes)
        return nodes

# V0'
class Solution(object):
    def boundaryOfBinaryTree(self, root):
        if not root: return []

        left_bd_nodes = [root]
        cur = root.left
        while cur:
            left_bd_nodes.append(cur)
            cur = cur.left or cur.right

        right_bd_nodes = [root]
        cur = root.right
        while cur:
            right_bd_nodes.append(cur)
            cur = cur.right or cur.left

        leaf_nodes = []
        stack = [root]
        while stack:
            node = stack.pop()
            if node.right:
                stack.append(node.right)
            if node.left:
                stack.append(node.left)
            if not node.left and not node.right:
                leaf_nodes.append(node)

        ans = []
        seen = set()
        def visit(node):
            if node not in seen:
                seen.add(node)
                ans.append(node.val)

        for node in left_bd_nodes: visit(node)
        for node in leaf_nodes: visit(node)
        for node in reversed(right_bd_nodes): visit(node)

        return ans
```

## Height, Depth & Path Examples

### 10) Tree Height and Depth Operations

#### **Core Concepts: Height vs Depth**

| Concept | Definition | Direction | Traversal | Use Case |
|---------|------------|-----------|-----------|----------|
| **Height** | Distance from node to deepest leaf | Bottom-up | Post-order DFS | Tree properties, balance check |
| **Depth** | Distance from root to target node | Top-down | Pre-order DFS | Node distance, level finding |

**Key Insight**: `getDepth()` is the fundamental algorithm for calculating distance between nodes (from root to any target node).

#### **Visual Comparison**

```text
        1           Height of 1: 2 (to deepest leaf)
       / \          Depth of 1: 0 (root)
      2   3         Height of 2: 1, Depth of 2: 1
     / \            Height of 4: 0, Depth of 4: 2
    4   5           Height of 5: 0, Depth of 5: 2

Height measures "how far down can I go?"
Depth measures "how far am I from the root?"
```

#### **1. Get Height (Post-order DFS)**

**Purpose**: Calculate distance from node to its deepest descendant leaf.

```java
// java
/**
 * 🌳 Get Height - Bottom-up approach
 *
 * Time: O(N) - visit each node once
 * Space: O(h) - recursion stack depth
 *
 * Returns: Height of tree (number of edges from node to deepest leaf)
 */
public int getHeight(TreeNode root) {
    if (root == null) {
        return -1;  // ✅ Return -1 for null (so leaf height = 0)
    }

    // Post-order: process children first, then current node
    int leftHeight = getHeight(root.left);
    int rightHeight = getHeight(root.right);

    return Math.max(leftHeight, rightHeight) + 1;
}

/**
 * Why return -1 for null?
 *
 * • By definition: leaf node has height 0
 * • If root is a leaf:
 *   - leftHeight = -1 (null)
 *   - rightHeight = -1 (null)
 *   - max(-1, -1) + 1 = 0 ✅ (correct leaf height)
 *
 * Alternative: return 0 for null
 * Then leaf height would be 1 (which is also valid but less standard)
 */
```

```python
# python
def get_height(node):
    """
    Height: Distance from node to deepest leaf (bottom-up)

    Returns -1 for null so leaf height = 0
    """
    if not node:
        return -1

    left_height = get_height(node.left)
    right_height = get_height(node.right)

    return max(left_height, right_height) + 1
```

#### **2. Get Depth (Pre-order DFS)**

**Purpose**: Calculate distance from root to target node. **This is the core distance-finding algorithm.**

```java
// java
/**
 * 🌿 Get Depth - Top-down approach
 *
 * This is the fundamental algorithm for finding distance between nodes:
 * - Distance from root to any target node
 * - Can be used as building block for node-to-node distance (via LCA)
 *
 * Time: O(N) - worst case visit all nodes
 * Space: O(h) - recursion stack depth
 *
 * Returns:
 * - Depth (number of edges from root to target) if found
 * - -1 if target not found
 */
public int getDepth(TreeNode root, TreeNode target) {
    return getDepthHelper(root, target, 0);
}

private int getDepthHelper(TreeNode root, TreeNode target, int depth) {
    if (root == null) {
        return -1; // ❌ Target not found in this branch
    }

    if (root == target) {
        return depth; // ✅ Found! Return current distance from root
    }

    // Pre-order: try left subtree first
    int leftDepth = getDepthHelper(root.left, target, depth + 1);
    if (leftDepth != -1) {
        return leftDepth; // Found in left subtree
    }

    // If not in left, try right subtree
    return getDepthHelper(root.right, target, depth + 1);
}

/**
 * Why return -1 for null?
 *
 * • -1 is a sentinel value meaning "not found"
 * • Allows us to distinguish between:
 *   - "Found at root" (depth = 0)
 *   - "Not found" (depth = -1)
 * • Enables early termination when target is found
 */
```

```python
# python
def get_depth(root, target, depth=0):
    """
    Depth: Distance from root to target node (top-down)

    This is the core distance algorithm - finds distance from root to any node.

    Returns:
    - depth (number of edges) if target found
    - -1 if target not found
    """
    if not root:
        return -1  # Not found

    if root == target or root.val == target:
        return depth  # Found!

    # Try left subtree
    left_depth = get_depth(root.left, target, depth + 1)
    if left_depth != -1:
        return left_depth

    # Try right subtree
    return get_depth(root.right, target, depth + 1)
```

#### **3. Comparison Table**

| Aspect | Get Height | Get Depth |
|--------|------------|-----------|
| **Traversal Order** | Post-order (left → right → root) | Pre-order (root → left → right) |
| **Direction** | Bottom-up (leaf to node) | Top-down (root to node) |
| **Null Return** | `-1` (so leaf height = 0) | `-1` (meaning "not found") |
| **Parameter Passing** | None (computed from children) | Pass `depth` down through recursion |
| **Use Case** | Tree balance, tree properties | **Distance calculation**, level finding |
| **When to Use** | Need children data first | Need parent data for children |
| **Example Problems** | LC 104 (Max Depth), LC 110 (Balanced Tree) | LC 1740 (Distance in Tree), LC 863 (Distance K) |

#### **4. Relationship to Distance Between Nodes**

```java
/**
 * Finding distance between ANY two nodes uses getDepth() as building block:
 *
 * 1. Find Lowest Common Ancestor (LCA) of node1 and node2
 * 2. Distance = getDepth(LCA, node1) + getDepth(LCA, node2)
 *
 * See LC 1740 for full implementation.
 */
public int findDistance(TreeNode root, int p, int q) {
    TreeNode lca = findLCA(root, p, q);
    return getDepth(lca, p) + getDepth(lca, q);
}
```

#### **5. Common Variations**

```java
// java

// Variation 1: Get depth by value instead of node reference
public int getDepth(TreeNode root, int targetVal) {
    return getDepthHelper(root, targetVal, 0);
}

private int getDepthHelper(TreeNode node, int targetVal, int depth) {
    if (node == null) {
        return -1;
    }
    if (node.val == targetVal) {
        return depth;
    }

    int left = getDepthHelper(node.left, targetVal, depth + 1);
    if (left != -1) return left;

    return getDepthHelper(node.right, targetVal, depth + 1);
}

// Variation 2: Get height returning 0 for null (leaf height = 1)
public int getHeightAlternative(TreeNode root) {
    if (root == null) {
        return 0;  // Leaf node height = 1 with this approach
    }
    return 1 + Math.max(getHeightAlternative(root.left),
                        getHeightAlternative(root.right));
}

// Variation 3: Check if tree is balanced (height difference ≤ 1)
public boolean isBalanced(TreeNode root) {
    return checkBalance(root) != -1;
}

private int checkBalance(TreeNode node) {
    if (node == null) {
        return 0;
    }

    int leftHeight = checkBalance(node.left);
    if (leftHeight == -1) return -1;

    int rightHeight = checkBalance(node.right);
    if (rightHeight == -1) return -1;

    if (Math.abs(leftHeight - rightHeight) > 1) {
        return -1;  // Unbalanced
    }

    return 1 + Math.max(leftHeight, rightHeight);
}
```

#### **6. Key Takeaways**

1. **Height (Post-order)**: Used for tree properties, needs children info first
2. **Depth (Pre-order)**: **Core distance algorithm**, passes info down to children
3. **Return -1 for null**:
   - Height: Makes leaf height = 0 (standard definition)
   - Depth: Signals "target not found"
4. **Distance between nodes** = Use `getDepth()` twice with LCA
5. **Choose based on info flow**:
   - Need children data? → Use height (post-order)
   - Need parent data? → Use depth (pre-order)


> Concrete implementations of the height/depth ideas above.

#### Get Maximum depth

- LC 104 : Maximum Depth of Binary Tree
- LC 110 : Balanced Binary Tree

```java
// java
// V0
// IDEA : RECURSIVE (DFS)
public int maxDepth(TreeNode root) {

    if (root == null){
        return 0;
    }

    // NOTE : below conditon is optional (have or not use is OK)
//        if (root.left == null && root.right == null){
//            return 1;
//        }

    int leftD = maxDepth(root.left) + 1;
    int rightD = maxDepth(root.right) + 1;

    return Math.max(leftD, rightD);
}
```

```python
#-----------------
# BFS
#-----------------
# ....
layer = 1
q = [[layer, root]]
res = []
while q:
    # NOTE !!! FIFO, so we pop first added element (new element added at right hand side)
    layer, tmp = root.pop(0)
    """
    KEY here !!!!
    """
    if tmp and not tmp.left and not tmp.right:
        res.append(layer)
    if tmp.left:
        q.append([layer+1, tmp.left])
    if tmp.right:
        q.append([layer+1, tmp.right])
    # ...
```

#### Get Minimum depth
- LC 111 : Minimum Depth of Binary Tree 

```java
// java

// V0'
// IDEA : DFS
public int minDepth(TreeNode root) {

    if (root == null){
        return  0;
    }

    return getDepth(root);
}

private int getDepth(TreeNode root){

    if (root == null){
        return 0;
    }

    /**
     *  NOTE !!! below condition
     *  -> we need to go till meat a node, then calculate min depths (number of node)
     *  -> Note: A leaf is a node with no children.
     *  -> plz check below example for idea
     *  example : [2,null,3,null,4,null,5,null,6]
     *
     *
     */
    if (root.left == null) {
        return 1 + getDepth(root.right);
    } else if (root.right == null) {
        return 1 + getDepth(root.left);
    }

    return 1 + Math.min(getDepth(root.left), getDepth(root.right));
}
```

### 11) Maximum Depth of Binary Tree — LC 104
```python
# LC 104 Maximum Depth of Binary Tree
# V0
# IDEA : DFS
class Solution(object):
    def maxDepth(self, root):

        if root == None:
            return 0
        return 1 + max(self.maxDepth(root.left), self.maxDepth(root.right))

# V0'
# bfs
class Solution(object):
    def maxDepth(self, root):
        # edge case
        if not root:
            return 0
        res = 0
        layer = 0
        q = [[root, layer]]
        while q:
            for i in range(len(q)):
                tmp, layer = q.pop(0)
                res = max(res, layer)
                if tmp.left:
                    q.append([tmp.left, layer+1])
                if tmp.right:
                    q.append([tmp.right, layer+1])                   
        return res + 1
```

### 12) Minimum Depth of Binary Tree — LC 111

> Reference: [MinimumDepthOfBinaryTree.java](https://github.com/yennanliu/CS_basics/blob/master/leetcode_java/src/main/java/LeetCodeJava/Recursion/MinimumDepthOfBinaryTree.java)

```python
# LC 111 Minimum Depth of Binary Tree

# V0
# IDEA : BFS
class Solution(object):
    def minDepth(self, root):
        # edge case
        if not root:
            return 0
        if root and not root.left and not root.right:
            return 1
        layer = 1
        q = [[layer, root]]
        res = []
        while q:
            for i in range(len(q)):
                layer, tmp = q.pop(0)
                """
                NOTE !!! : via below condition, we get "layer" of " A leaf is a node with no children."
                """
                if tmp and not tmp.left and not tmp.right:
                    res.append(layer)
                if tmp.left:
                    q.append([layer+1, tmp.left])
                if tmp.right:
                    q.append([layer+1, tmp.right])
        # get min
        #print ("res = " + str(res))
        return min(res)

# V0'
# IDEA : DFS
# compare with LC 104 : Maximum Depth of Binary Tree
class Solution(object):
    def minDepth(self, root):
        if not root:
            return 0
        ### NOTE here : we need min depth, so if not root.left, then we need to return directly
        if not root.left:
            return 1 + self.minDepth(root.right)
        ### NOTE here : we need min depth, so if not root.right, then we need to return directly
        elif not root.right:
            return 1 + self.minDepth(root.left)
        else:
            return 1 + min(self.minDepth(root.left), self.minDepth(root.right))
```

### 13) Binary Tree Maximum Path Sum — LC 124

```java
// java
// LC 124

// V0-1
// IDEA: DFS (GPT)
private int maxSum = Integer.MIN_VALUE;

public int maxPathSum_0_1(TreeNode root) {
    if (root == null) {
        return Integer.MIN_VALUE; // Handle null case
    }

    dfs(root);
    return maxSum;
}

/** NOTE !!!
 *
 *  the response type of dfs is `integer`
 *  e.g. the `max path sum` per input node
 */
private int dfs(TreeNode node) {
    if (node == null) {
        return 0;
    }

    // Compute max path sum of left and right children, discard negative values
    /**
     *  NOTE !!!
     *
     *   we cache `leftMax` on current node
     *   we cache `rightMax` on current node
     *
     *   so we can update global `max path sum` below
     */
    int leftMax = Math.max(dfs(node.left), 0);
    int rightMax = Math.max(dfs(node.right), 0);

    // Update global max sum with current node as the highest ancestor
    /**
     *  NOTE !!!
     *
     *  we update global `max path sum`,
     *  but the `maxSum` is NOT return as method reponse,
     *  we simply update the global variable `maxSum`
     *
     *  -> the method return val is local max path (node.val + Math.max(leftMax, rightMax))
     */
    maxSum = Math.max(maxSum, node.val + leftMax + rightMax);

    // Return max sum path including this node (but only one subtree path)
    /**
     *  NOTE !!!
     *
     *
     *  -> the method return val is local max path (node.val + Math.max(leftMax, rightMax)),
     *     instead of `maxSum`
     *
     */
    return node.val + Math.max(leftMax, rightMax);
}

```

```python
# 124. Binary Tree Maximum Path Sum
# V0
# IDEA : DFS
# https://leetcode.com/problems/binary-tree-maximum-path-sum/discuss/209995/Python-solution
class Solution(object):
    def maxPathSum(self, root):
        def dfs(root):
            if not root:
                return 0
            l_max = dfs(root.left)
            r_max = dfs(root.right)
            """
            handle if l_max < 0:
                    -> start again from root.val
                   else:
                    -> l_max += root.val
            """
            if l_max < 0:
                l_max = root.val
            else:
                l_max += root.val
            """
            handle if r_max < 0:
                    -> start again from root.val
                   else:
                    -> r_max += root.val
            """
            if r_max < 0:
                r_max = root.val
            else:
                r_max += root.val

            self.maximum = max(self.maximum, l_max + r_max - root.val)
            return max(l_max, r_max)
           
        self.maximum = -float('inf')
        dfs(root)
        return self.maximum
```

### 14) Count Good Nodes in Binary Tree — LC 1448
```java
// java
// LC 1448
 // V1
    // IDEA : DFS
    // https://leetcode.com/problems/count-good-nodes-in-binary-tree/editorial/
    private int numGoodNodes = 0;

    public int goodNodes_2(TreeNode root) {
        dfs(root, Integer.MIN_VALUE);
        return numGoodNodes;
    }

    private void dfs(TreeNode node, int maxSoFar) {
        if (maxSoFar <= node.val) {
            numGoodNodes++;
        }

        if (node.right != null) {
            dfs(node.right, Math.max(node.val, maxSoFar));
        }

        if (node.left != null) {
            dfs(node.left, Math.max(node.val, maxSoFar));
        }
    }


    // V2
    // IDEA : DFS + Iterative
    // https://leetcode.com/problems/count-good-nodes-in-binary-tree/editorial/
    class Pair {

        public TreeNode node;
        public int maxSoFar;

        public Pair(TreeNode node, int maxSoFar) {
            this.node = node;
            this.maxSoFar = maxSoFar;
        }
    }

    public int goodNodes_3(TreeNode root) {
        int numGoodNodes = 0;
        Stack<Pair> stack = new Stack<>();
        stack.push(new Pair(root, Integer.MIN_VALUE));

        while (stack.size() > 0) {
            Pair curr = stack.pop();
            if (curr.maxSoFar <= curr.node.val) {
                numGoodNodes++;
            }

            if (curr.node.left != null) {
                stack.push(new Pair(curr.node.left, Math.max(curr.node.val, curr.maxSoFar)));
            }

            if (curr.node.right != null) {
                stack.push(new Pair(curr.node.right, Math.max(curr.node.val, curr.maxSoFar)));
            }
        }

        return numGoodNodes;
    }
```

## Modification & Multi-State Examples

### 15) Delete Nodes And Return Forest — LC 1110

**Problem**: Given a binary tree root and an array of values to delete, remove those nodes and return a list of the roots of the remaining trees (forest).

**Core Idea**: 
- Use DFS with two state tracking: whether current node should be deleted, and whether parent was deleted
- A node becomes a forest root if it's NOT deleted but its parent IS deleted
- Post-order DFS processes children first, allowing clean disconnection

**Approach 1: DFS + State Tracking (Recommended)**

```java
public List<TreeNode> delNodes(TreeNode root, int[] to_delete) {
    HashSet<Integer> deleteSet = new HashSet<>();
    for (int x : to_delete) {
        deleteSet.add(x);
    }
    
    List<TreeNode> forest = new ArrayList<>();
    dfs(root, deleteSet, true, forest);  // root has no parent → treated as deleted
    return forest;
}

private TreeNode dfs(TreeNode node, HashSet<Integer> deleteSet, boolean isParentDeleted, List<TreeNode> forest) {
    if (node == null)
        return null;

    boolean isDeleted = deleteSet.contains(node.val);

    // If this node is a new root (NOT deleted AND parent WAS deleted or doesn't exist)
    if (!isDeleted && isParentDeleted) {
        forest.add(node);
    }

    // Post-order: process children first (their isParentDeleted = current node's isDeleted)
    node.left = dfs(node.left, deleteSet, isDeleted, forest);
    node.right = dfs(node.right, deleteSet, isDeleted, forest);

    // Return null to parent if deleted (automatically disconnects), else return node
    return isDeleted ? null : node;
}
```

**Complexity**: Time O(N), Space O(N)
- Visit each node exactly once
- HashSet operations: O(1)
- Recursion depth: O(h) worst case O(N)

**Approach 2: BFS (Level-Order Traversal)**

```java
public List<TreeNode> delNodes_BFS(TreeNode root, int[] to_delete) {
    Set<Integer> deleteSet = new HashSet<>();
    for (int val : to_delete) {
        deleteSet.add(val);
    }
    
    List<TreeNode> forest = new ArrayList<>();
    Queue<TreeNode> q = new LinkedList<>();
    q.add(root);
    
    while (!q.isEmpty()) {
        TreeNode curNode = q.poll();
        
        // Disconnect children if they need to be deleted
        if (curNode.left != null) {
            q.add(curNode.left);
            if (deleteSet.contains(curNode.left.val)) {
                curNode.left = null;  // Disconnect
            }
        }
        
        if (curNode.right != null) {
            q.add(curNode.right);
            if (deleteSet.contains(curNode.right.val)) {
                curNode.right = null;  // Disconnect
            }
        }
        
        // If current node is deleted, add its children as forest roots
        if (deleteSet.contains(curNode.val)) {
            if (curNode.left != null) {
                forest.add(curNode.left);
            }
            if (curNode.right != null) {
                forest.add(curNode.right);
            }
        }
    }
    
    // Add original root if not deleted
    if (!deleteSet.contains(root.val)) {
        forest.add(root);
    }
    
    return forest;
}
```

**Complexity**: Time O(N), Space O(N)

**Example Walkthrough**: 
```text
Input: root = [1,2,3,4,5,6,7], to_delete = [3,5]

       1
      / \
     2   3
    / \ /  \
   4  5 6   7

Step 1: DFS processes:
- Node 4: isParentDeleted=false (parent 2 not deleted) → NOT a root
- Node 5: isDeleted=true, Node 2 disconnects it
- Node 2: isParentDeleted=false (parent 1 not deleted) → NOT a root
- Node 6: isParentDeleted=true (parent 3 deleted) → IS a root! Add 6
- Node 7: isParentDeleted=true (parent 3 deleted) → IS a root! Add 7
- Node 3: isDeleted=true, Node 1 disconnects it
- Node 1: isParentDeleted=true (root, treated as parent deleted) and NOT deleted → IS a root! Add 1

Result: [1(with subtree [2,4]), 6, 7]
```

**Key Insights**:
1. **Two-State Pattern**: Track both `isDeleted` and `isParentDeleted`
2. **Forest Root Condition**: `(!isDeleted && isParentDeleted)` OR `(!isDeleted && isRoot)`
3. **Post-order DFS**: Children processed before parent decision, allowing clean disconnection
4. **Automatic Disconnection**: Returning null from `dfs()` automatically sets parent's child to null
5. **Why BFS works**: By queuing all children first, then processing, we naturally discover which nodes become roots

**Common Pitfalls** ⚠️:
1. **Forgetting root special case**: Root has no parent, so treat it as "parent deleted" to allow it as forest root
2. **Wrong traversal order**: Must process children before parent to know if node is deleted
3. **Not disconnecting properly**: BFS approach needs explicit `curNode.left = null` disconnection
4. **Missing forest roots**: Check both initial root and nodes whose parent is deleted

**Similar Problems**:
| Problem | LC # | Key Difference |
|---------|------|-----------------|
| Delete Nodes And Return Forest | 1110 | Base pattern |
| Delete Leaves With Given Value | 1325 | Recursive deletion (delete after children are processed) |
| Trim a Binary Search Tree | 669 | Range-based filtering instead of value-based deletion |
| Lowest Common Ancestor III | 1676 | Find LCA in forest after deletion |

**Common Applications:**
- Tree pruning with multiple resulting subtrees
- Forest formation from selective node removal
- File system operations (delete nodes and keep remaining structure)
- Hierarchical data management with cascading deletions

**Pattern Recognition:**
- ✅ Need to delete specific nodes and keep rest of tree structure
- ✅ Result is a forest (multiple tree roots)
- ✅ Deleted node's children should survive
- ✅ State depends on both current node and parent's decision

### 16) Flatten Binary Tree to Linked List — LC 114

**Problem**: Flatten a binary tree into a "linked list" **in-place**, where every `right` pointer is the next node in **pre-order**, and every `left` pointer is `null`.

```text
Input:                Output (right-linked, all left = null):

      1                 1
     / \                 \
    2   5                 2
   / \   \                 \
  3   4   6                 3
                            \
                             4
                              \
                               5
                                \
                                 6
```

**Core Idea — Post-order DFS returning the "tail"**

The cleanest recursive solution flattens left & right subtrees first (post-order), then rewires the current node. The key trick: **each `helper` call returns the *tail* (last node in pre-order) of the subtree it flattened**, so the parent knows where to splice the original right subtree.

```python
# python
# LC 114 Flatten Binary Tree to Linked List
# IDEA: DFS (post-order) — return the TAIL of each flattened subtree
class Solution(object):
    def flatten(self, root):
        """Do not return anything, modify root in-place instead."""
        self.helper(root)

    def helper(self, node):
        # Base case: an empty subtree has no tail
        if not node:
            return None

        # 1) Flatten BOTH subtrees first (post-order)
        left_tail  = self.helper(node.left)   # last node of flattened left
        right_tail = self.helper(node.right)  # last node of flattened right

        # 2) If a left subtree exists, splice it between node and node.right
        if left_tail:
            left_tail.right = node.right  # left's tail -> original right
            node.right = node.left        # move left subtree to the right
            node.left = None              # left must be null per problem

        # 3) Return the tail of THIS flattened subtree (pre-order last node)
        #    priority: right_tail > left_tail > node itself
        if right_tail:
            return right_tail
        if left_tail:
            return left_tail
        return node
```

**Why return the tail?** When we move the left subtree to the right, we must reconnect the *original* right subtree to the **end** of the flattened left subtree — not to its root. The only node that knows where that end is, is the recursive call that flattened the left subtree. So it returns its tail.

#### Rewiring visualization (the `if left_tail:` block)

```text
Before:                 After:
      node                node
     /    \                  \
   left   right             left            (node.right = node.left)
                               \
                              ...  (flattened left chain)
                                  \
                                  right     (left_tail.right = node.right)
```

#### Dry run — `root = [1,2,5,3,4,null,6]`

```text
        1
       / \
      2   5
     / \   \
    3   4   6
```

Post-order visits the **deepest-left** nodes first. Trace of `helper` returns (the tail each call hands back):

```text
helper(3): no children          -> left_tail=None, right_tail=None       -> return 3
helper(4): no children          -> return 4
helper(2): left_tail=3, right_tail=4
           left_tail(3).right = node.right (4)   =>  3 -> 4
           node.right = node.left (3)            =>  2 -> 3
           node.left = None
           subtree now: 2 -> 3 -> 4              -> return right_tail = 4
helper(6): no children          -> return 6
helper(5): left_tail=None, right_tail=6
           (no left subtree, nothing to rewire)
           subtree: 5 -> 6                       -> return right_tail = 6
helper(1): left_tail=4 (tail of "2->3->4"), right_tail=6
           left_tail(4).right = node.right (5)   =>  4 -> 5
           node.right = node.left (2)            =>  1 -> 2
           node.left = None
           => 1 -> 2 -> 3 -> 4 -> 5 -> 6         -> return 6
```

Final flattened list (all `left=None`): `1 -> 2 -> 3 -> 4 -> 5 -> 6` ✅ (matches pre-order).

#### Pattern & alternatives

| Approach | Idea | Time | Space | Notes |
|----------|------|------|-------|-------|
| **Post-order + return tail** (above) | Flatten subtrees, splice via returned tail | O(N) | O(h) recursion | Clean, intuitive |
| **Reverse pre-order + `prev`** | Visit `right → left → node`, set `node.right = prev` | O(N) | O(h) | Mirror of building list backwards (see `V0-1`) |
| **Pre-order collect to list** | Store nodes in pre-order, relink in a loop | O(N) | O(N) | Easiest to reason about |
| **Morris-style iterative** | For each node, find left subtree's rightmost, splice | O(N) | **O(1)** | Best for the *follow-up* (true in-place) |

**Reverse pre-order (`prev` pointer) — the slick O(h) variant:**

```python
# Visit right first, then left, building the list from tail to head
class Solution(object):
    def __init__(self):
        self.prev = None
    def flatten(self, root):
        if not root:
            return
        self.flatten(root.right)
        self.flatten(root.left)
        root.right = self.prev   # link to the chain built so far
        root.left = None
        self.prev = root         # current node becomes new head
```

**True O(1) space (iterative, the follow-up answer):**

```python
class Solution(object):
    def flatten(self, root):
        curr = root
        while curr:
            if curr.left:
                rightmost = curr.left
                while rightmost.right:        # find left subtree's rightmost
                    rightmost = rightmost.right
                rightmost.right = curr.right  # splice original right after it
                curr.right = curr.left        # move left to right
                curr.left = None
            curr = curr.right                 # advance down the new right spine
```

**Key insight**: `right_tail > left_tail > node` priority for the return value mirrors **pre-order's last node** — pre-order ends in the rightmost branch, so the right subtree's tail (if any) is the overall tail.

**Similar problems**:

| Problem | LC # | Key Difference |
|---------|------|-----------------|
| Flatten Binary Tree to Linked List | 114 | Base pattern (pre-order flatten in-place) |
| Binary Tree Preorder Traversal | 144 | Same visit order, just collect values |
| Convert BST to Sorted Doubly Linked List | 426 | In-order flatten into a doubly linked list |
| Increasing Order Search Tree | 897 | In-order flatten into right-only chain |
| Flatten a Multilevel Doubly Linked List | 430 | Same "splice child chain before next" idea on a list |

### 17) Merge Two Binary Trees — LC 617

```python
# LC 617 Merge Two Binary Trees
# NOTE !!! there is also BFS solution
# V0
# IDEA : DFS + BACKTRACK
class Solution:
    def mergeTrees(self, t1, t2):
        return self.dfs(t1,t2)

    def dfs(self, t1, t2):
        if not t1 and not t2:
            return
        
        if t1 and t2:
            ### NOTE here
            newT = TreeNode(t1.val +  t2.val)
            newT.right = self.mergeTrees(t1.right, t2.right)
            newT.left = self.mergeTrees(t1.left, t2.left)   
            return newT
        
        ### NOTE here
        else:
            return t1 or t2
```

```java
// java
// V0
// IDEA : RECURSIVE
public TreeNode mergeTrees(TreeNode t1, TreeNode t2) {

    if (t1 == null && t2 == null){
        return null;
    }

    if (t1 != null && t2 != null){
        t1.val += t2.val;
    }

    if (t1 == null && t2 != null){
        // NOTE!!! return t2 directly here
        return t2;
    }

    if (t1 != null && t2 == null){
        // NOTE!!! return t1 directly here
        return t1;
    }

    t1.left = mergeTrees(t1.left, t2.left);
    t1.right = mergeTrees(t1.right, t2.right);

    return t1;
}
```

### 18) Invert Binary Tree — LC 226
```python
# LC 226 Invert Binary Tree

# V0
# IDEA : DFS
# -> below code shows a good example that tree is a type of "linked list"
# -> we don't really modify tree's "value", but we modify the pointer
# -> e.g. make root.left point to root.right, make root.right point to root.left
class Solution(object):
    def invertTree(self, root):
        def dfs(root):
            if not root:
                return root
            ### NOTE THIS
            if root:
                # NOTE : have to do root.left, root.right ON THE SAME TIME
                root.left, root.right = dfs(root.right), dfs(root.left)
        dfs(root)
        return root

# V0'
# IDEA BFS
class Solution(object):
    def invertTree(self, root):
        if root == None:
            return root
        queue = [root]
        while queue:
            # queue = queue[::-1] <-- this one is NOT working
            for i in range(len(queue)):         
                tmp = queue.pop()
                ### NOTE here !!!!!!
                # -> we do invert op via below
                tmp.left, tmp.right = tmp.right, tmp.left
                if tmp.left:
                    queue.append(tmp.left)
                if tmp.right:
                    queue.append(tmp.right)
        return root
```

```java
// java
// DFS
    // V0
    // IDEA : DFS
    public TreeNode invertTree(TreeNode root) {
        if (root == null) {
            return null;
        }
        /** NOTE !!!!
         *
         *   instead of calling invertTree and assign value to sub tree directly,
         *   we need to CACHE invertTree result, and assign later
         *   -> since assign directly will cause tree changed, and affect the other invertTree call
         *
         *   e.g. below is WRONG,
         *      root.left = invertTree(root.right);
         *      root.right = invertTree(root.left);
         *
         *   need to cache result
         *
         *       TreeNode left = invertTree(root.left);
         *       TreeNode right = invertTree(root.right);
         *
         *   then assign to sub tree
         *
         *       root.left = right;
         *       root.right = left;
         */
        TreeNode left = invertTree(root.left);
        TreeNode right = invertTree(root.right);
        root.left = right;
        root.right = left;
        /** NOTE !!!! below is WRONG */
//        root.left = invertTree(root.right);
//        root.right = invertTree(root.left);
        return root;
    }
```

### 19) Binary Tree Cameras — LC 968 (Bottom-Up Greedy with Multi-State)


> Reference: [BinaryTreeCameras.java](https://github.com/yennanliu/CS_basics/blob/master/leetcode_java/src/main/java/LeetCodeJava/BinarySearchTree/BinaryTreeCameras.java)

Some problems require each node to return a **state** (not a numeric value) to its parent, and the parent makes a **greedy decision** based on children's states. This is a distinct bottom-up pattern.

**Core Idea — 3-State Greedy:**
```text
State 0: NOT covered (needs a camera from parent)
State 1: HAS a camera (covers parent, self, children)
State 2: COVERED (by a child's camera, but has no camera itself)

null nodes → return 2 (covered), so leaves are forced to be state 0 (uncovered),
which forces their parents to place cameras — this is the greedy insight.
```

**Why bottom-up (post-order)?**
- Leaves are the most "wasteful" place for cameras (they only cover 1 node upward)
- By processing leaves first, we force cameras onto their parents (which cover 3 nodes)
- This greedy strategy from bottom to top minimizes total cameras

```java
// LC 968 — Binary Tree Cameras: bottom-up greedy with 3 states
int cameraCnt = 0;

public int minCameraCover(TreeNode root) {
    // If root itself is uncovered, it needs a camera too
    if (dfs(root) == 0) {
        cameraCnt++;
    }
    return cameraCnt;
}

private int dfs(TreeNode node) {
    // null = covered (so leaves become uncovered → forces parent to place camera)
    if (node == null) return 2;

    int left = dfs(node.left);    // post-order: solve children first
    int right = dfs(node.right);

    // Rule 1: Any child uncovered → MUST place camera here
    if (left == 0 || right == 0) {
        cameraCnt++;
        return 1;  // has camera
    }

    // Rule 2: Any child has camera → this node is covered
    if (left == 1 || right == 1) {
        return 2;  // covered
    }

    // Rule 3: Both children covered (no cameras) → this node is NOT covered
    // Rely on parent to cover it (greedy: delay camera placement upward)
    return 0;  // uncovered
}
```

**Visual — Why greedy works bottom-up:**
```text
        1 ← if uncovered, add camera here (special root check)
       / \
      2   3 ← children covered (state 2), no camera needed
     / \
    4   5 ← camera HERE (state 1), covers parent + children
   / \
  6   7 ← uncovered (state 0), forces parent to place camera

Processing order (post-order): 6,7 → 4,5 → 2,3 → 1
  node 6,7: null children return 2 → both children covered → return 0 (uncovered)
  node 4: left=0 (uncovered!) → place camera → return 1
  node 5: similar logic
  node 2: left=1 (has camera) → return 2 (covered)
  node 1: depends on children's states
```

**State transition rules (decision at each node):**

| Left State | Right State | Decision | Return |
|:----------:|:-----------:|----------|:------:|
| 0 (uncovered) | any | Place camera | 1 |
| any | 0 (uncovered) | Place camera | 1 |
| 1 (camera) | any non-0 | Covered by child | 2 |
| any non-0 | 1 (camera) | Covered by child | 2 |
| 2 (covered) | 2 (covered) | Not covered, rely on parent | 0 |

**Key insight — why `null → 2` (covered)?**
If null returned 0 (uncovered), every leaf would be forced to have a camera — wasteful. By treating null as "covered", leaves become state 0 (uncovered), forcing their **parents** to place cameras, which is strictly better (covers 3 nodes vs 1).

**Similar LC problems using bottom-up greedy with states:**

| LC # | Problem | States | Greedy Insight |
|------|---------|--------|----------------|
| 968 | Binary Tree Cameras | 0/1/2 (uncovered/camera/covered) | Delay cameras upward, place at parents of leaves |
| 337 | House Robber III | rob/skip per node | Max(rob current + skip children, skip current + best of children) |
| 979 | Distribute Coins in Binary Tree | excess coins per subtree | Each edge transfer = 1 move; count |excess| bottom-up |
| 1373 | Max Sum BST in Binary Tree | valid/invalid BST + sum | Bottom-up validate BST property + track max sum |

## Summary

| Problem | Pattern | The one thing to remember |
|---|---|---|
| LC 199 | BFS / DFS by depth | take the **last** node of each level (or the first, visiting right → left) |
| LC 222 | complete-tree recursion | compare left and right spine heights → O(log² N), not O(N) |
| LC 662 | index-encoded BFS | node `i` → children `2i`, `2i+1`; width = last − first + 1 |
| LC 2415 | level-order + swap | reverse the **values** of a level, not the nodes |
| LC 101 | mirrored DFS | compare `(a.left, b.right)` and `(a.right, b.left)` |
| LC 100 / 951 | paired DFS | LC 951 additionally allows the child pair to be swapped |
| LC 98 | in-order / bounds | pass `(low, high)` down — comparing with the direct children is not enough |
| LC 110 | post-order height | return `-1` to signal "already unbalanced" and stop early |
| LC 545 | three passes | left boundary, leaves, reversed right boundary — de-duplicate the corners |
| LC 104 / 111 | height | min depth needs the null-child guard; max depth does not |
| LC 124 | post-order + global max | return one branch upward, record `left + node + right` globally |
| LC 1448 | top-down max-so-far | carry the path maximum down as a parameter |
| LC 1110 | two-state DFS | a survivor whose parent died becomes a forest root |
| LC 114 | post-order returning the tail | or the O(1)-space Morris rewire in tree.md |
| LC 617 / 226 | structural recursion | build or swap on the way down, return the node |
| LC 968 | bottom-up 3-state greedy | `null → covered`, which forces cameras onto the parents of leaves |
