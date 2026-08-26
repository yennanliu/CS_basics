# Tree LCA, Distance & Path Problems

> **Scope** — Lowest common ancestor, node-to-node distance, parent-map (bidirectional) traversal and the root-to-leaf path templates — every tree problem whose answer is a path or a meeting point rather than a shape.
> **See also**: [tree.md](./tree.md) — the pattern catalogue and traversal templates these build on; [tree_examples.md](./tree_examples.md) — the rest of the worked tree problems; [tree_backtrack.md](./tree_backtrack.md) — root→leaf paths that undo state on the way back up; [bst.md](./bst.md) — LCA on an ordered tree (LC 235).

## LeetCode Problem Lists

- [Tree](https://leetcode.com/problem-list/tree/)
- [Binary Tree](https://leetcode.com/problem-list/binary-tree/)
- [Depth-First Search](https://leetcode.com/problem-list/depth-first-search/)
- [Breadth-First Search](https://leetcode.com/problem-list/breadth-first-search/)

## Overview

Three questions dominate this family: *where do two nodes meet* (LCA), *how far apart are they*
(distance), and *which root-to-leaf paths satisfy a property* (path sum / path length). All three
are one DFS — what changes is whether the number travels **down** as an argument or **up** as a
return value, and whether you are allowed to walk **upward** to a parent.

### Key Properties
- **Complexity**: O(N) time for every template here; space O(H) for recursion, O(N) once a parent map or graph is materialised
- **Core Idea**: the LCA is the split point — `dist(p, q) = dist(lca, p) + dist(lca, q)`
- **When to Use**: the answer is a path, a distance, or a meeting point rather than a subtree property
- **Sentinel rule**: a distance helper must return `-1` (not `0`) for "not found", because `0` is a valid distance

## Problem Categories

| Category | Question it answers | Template | Examples |
|----------|--------------------|----------|----------|
| **LCA** | where do `p` and `q` meet? | post-order DFS returning the first node that sees both | LC 236, 235, 1650, 865, 1123 |
| **Distance (downward)** | how many edges from an ancestor to a target? | pre-order DFS carrying `depth`, `-1` sentinel | LC 1740 |
| **Distance (any direction)** | which nodes are `k` away, in any direction? | parent map → undirected graph → BFS | LC 863, 742 |
| **Root-to-leaf paths** | which paths sum to / look like X? | pre-order DFS + backtracking | LC 112, 113, 257 |
| **Any-to-any downward paths** | how many sub-paths sum to X? | pre-order DFS + prefix-sum HashMap | LC 437 |
| **Longest path** | how long is the longest path through a node? | post-order DFS returning height, global max | LC 543, 687 |

## Templates & Algorithms

### 1) Lowest Common Ancestor (LCA) — LC 236

```python
# LC 236 Lowest Common Ancestor of a Binary Tree
# LC 235 Lowest Common Ancestor of a Binary Search Tree
# LC 1650 Lowest Common Ancestor of a Binary Tree III
# V0
# IDEA : RECURSION + POST ORDER TRANSVERSAL
### NOTE : we need POST ORDER TRANSVERSAL for this problem
#          -> left -> right -> root
#          -> we can make sure that if p == q, then the root must be p and q's "common ancestor"
class Solution(object):
    def lowestCommonAncestor(self, root, p, q):
        ### NOTE here
        # if not root or find p in tree or find q in tree
        # -> then we quit the recursion and return root
        if not root or p == root or q == root:
            return root
        ### NOTE here
        #  -> not root.left, root.right, BUT left, right
        left = self.lowestCommonAncestor(root.left, p, q)
        right = self.lowestCommonAncestor(root.right, p, q)
        ### NOTE here
        # find q and p on the same time -> LCA is the current node (root)
        # if left and right -> p, q MUST in left, right sub tree respectively
        if left and right:
            return root
        ### NOTE here
        # if p, q both in left sub tree or both in right sub tree
        return left if left else right
```

```java
// java
// algorithm book p. 271
TreeNode lowestCommonAncestor(TreeNode root, TreeNode p, TreeNode q){
    // base case
    if (root == null) return null;
    if (root == p || root == q) return root;
    TreeNode left = lowestCommonAncestor(root.left, p, q);
    TreeNode right = lowestCommonAncestor(root.right, p, q);
    // case 1
    if (left != null && right != null){
        return root;
    }
    // case 2
    if (left == null && right == null){
        return null;
    }
    // case 3
    return left == null ? right: left;
}
```

#### LCA Variant — Smallest Subtree with All Deepest Nodes (LC 865 / LC 1123) ⭐⭐⭐⭐

##### **1. Core Idea**

**Key Insight**: This is LCA in disguise. Instead of being given target nodes `p` and `q`, the targets are **implicitly** all nodes at the maximum depth.

```text
Standard LCA (LC 236)                 Deepest Subtree LCA (LC 865)
-----------------------               --------------------------------
Targets p, q are GIVEN                Targets = nodes at max depth (discovered)
Find where p and q paths meet         Find where left/right deepest paths meet
```

The trick is that **one post-order pass computes both things at once**: you can't know
which nodes are deepest until you've walked the whole tree, but you also need the LCA
of those nodes. So each recursive call returns a **pair**:

```text
dfs(node) -> (depth, lca_candidate)
             ^^^^^  ^^^^^^^^^^^^^^^
             max depth      the answer for THIS subtree only
             below node
```

The `depth` half is just LC 104 (`max depth`). The `lca_candidate` half is carried
upward alongside it, and gets **re-decided at every node** by comparing the two depths.

**Three Cases** (this comparison IS the whole algorithm):

| Case | Meaning | Return |
|------|---------|--------|
| `left.depth > right.depth` | all deepest nodes live on the left | `(left.depth + 1, left.node)` — bubble left's answer up |
| `right.depth > left.depth` | all deepest nodes live on the right | `(right.depth + 1, right.node)` — bubble right's answer up |
| `left.depth == right.depth` | deepest nodes exist on **both** sides | `(left.depth + 1, node)` — **current node becomes the LCA** |

**Why case 3 is correct**: if both sides bottom out at the same depth, the deepest set
straddles the current node, so no child can contain all of them — the current node is
the smallest subtree that does. And since the answer is overwritten on the way up
whenever depths tie again, the final root call holds the *lowest* such node.

**Why case 1/2 is correct**: if one side is strictly deeper, the shallower side has no
deepest nodes at all, so the answer is entirely inside the deeper subtree — pass it
through untouched (do **not** replace it with the current node).

**Base case**: `dfs(null) -> (0, None)`. Depth 0 for a null child makes a leaf return
`(1, leaf)` via case 3 — a leaf is trivially the LCA of itself.

##### **2. Pattern: Post-order DFS returning `(metric, payload)`**

This is the generalized shape — a **bottom-up aggregate carrying a candidate answer**.
Whenever a problem says *"the smallest subtree such that …"* or *"the node where the
extremes on both sides meet"*, reach for this.

```text
# pattern skeleton
def dfs(node):
    if not node:
        return (BASE_METRIC, None)

    l_metric, l_ans = dfs(node.left)      # post-order: children FIRST
    r_metric, r_ans = dfs(node.right)

    if l_metric > r_metric:               # one side dominates -> pass its answer up
        return (l_metric + 1, l_ans)
    if r_metric > l_metric:
        return (r_metric + 1, r_ans)
    return (l_metric + 1, node)           # tie -> current node is the meeting point
```

**Pattern checklist:**
- **Traversal**: post-order (must know both children before deciding)
- **Return type**: tuple / helper class — a *scalar metric* + a *node reference*
- **Decision**: made by comparing the two children's metrics, never by global state
- **No second pass**: don't compute max depth first and then re-scan; one pass is enough
- **time = O(N)**, **space = O(H)** — H = tree height (recursion stack)

> **Contrast with the "global variable" style** (LC 543 / LC 124): those problems return
> only a scalar and stash the answer in a member field. Here we return the answer *in
> the tuple* because the answer must be **selected** on the way up, not maximized.

**Common pitfalls:**
- ❌ Returning `node` in case 1/2 as well → you'd always get the root back
- ❌ Using `>=` instead of `==` for the tie case → merges case 1 into case 3 wrongly
- ❌ Two-pass (find max depth, then find LCA of all nodes at that depth) → works but O(N) extra space and much more code

```java
// java
// LC 865 / LC 1123 — Smallest Subtree with All the Deepest Nodes
// Same as: LCA of the deepest leaves

// Helper class carries both the LCA candidate and its max depth below
class Result {
    TreeNode node;
    int dist;
    Result(TreeNode node, int dist) {
        this.node = node;
        this.dist = dist;
    }
}

/**
 * time = O(N)
 * space = O(H)  — recursion stack; O(log N) balanced, O(N) skewed
 */
public TreeNode subtreeWithAllDeepest(TreeNode root) {
    return dfs(root).node;
}

private Result dfs(TreeNode node) {
    if (node == null) {
        return new Result(null, 0);
    }

    Result left  = dfs(node.left);
    Result right = dfs(node.right);

    // Case 1: left subtree is deeper — LCA is buried there
    if (left.dist > right.dist) {
        return new Result(left.node, left.dist + 1);
    }

    // Case 2: right subtree is deeper — LCA is buried there
    if (right.dist > left.dist) {
        return new Result(right.node, right.dist + 1);
    }

    // Case 3: equal depth — current node is the LCA of all deepest nodes
    return new Result(node, left.dist + 1);
}
```

```python
# python
# LC 865 / LC 1123 — Smallest Subtree with All the Deepest Nodes
# IDEA: post-order DFS returning (depth, lca_node)
# time = O(N), space = O(H)
class Solution(object):
    def subtreeWithAllDeepest(self, root):
        return self.helper(root)[1]

    def helper(self, node):
        # base case: null has depth 0 and no LCA
        if not node:
            return (0, None)

        # NOTE !!! post-order — children resolved BEFORE the decision
        left_depth, left_node = self.helper(node.left)
        right_depth, right_node = self.helper(node.right)

        # case 1) left deeper -> all deepest nodes on left, keep left's answer
        if left_depth > right_depth:
            return (left_depth + 1, left_node)

        # case 2) right deeper -> all deepest nodes on right, keep right's answer
        if right_depth > left_depth:
            return (right_depth + 1, right_node)

        # case 3) SAME depth -> deepest nodes on both sides -> current node is LCA
        return (left_depth + 1, node)
```

**Visualization:**
```text
        [3]          ← left.dist(3) == right.dist(2)? No → left wins
       /   \
     [5]   [1]       ← left.dist(2) == right.dist(1)? No → left wins
    /   \
  [6]  [2]           ← left.dist(0) == right.dist(1)? No → right wins
       /  \
      [7] [4]        ← both null, dist=0 → node [2] is LCA ✓
```

**Pro Tip**: Whenever a problem asks for the "smallest subtree containing [condition X]", think **Post-Order DFS + LCA logic**.

##### **3. Similar LC Problems**

**Identical problem (same code, different wording):**

| Problem | LC # | Note |
|---------|------|------|
| Smallest Subtree with all the Deepest Nodes | 865 | this problem |
| Lowest Common Ancestor of Deepest Leaves | 1123 | **literally the same** — copy-paste the solution |

**Same pattern — post-order DFS returning `(metric, node)` / meeting-point logic:**

| Problem | LC # | Metric returned | Key difference |
|---------|------|-----------------|----------------|
| Lowest Common Ancestor of a Binary Tree | 236 | `node` (found-or-null) | targets `p`, `q` are **given**; tie case = both children non-null |
| LCA of a BST | 235 | — | BST property lets you walk down in O(H), no post-order needed |
| LCA of a Binary Tree II | 1644 | `(node, count)` | `p`/`q` may not exist → must also return a found-count |
| LCA of a Binary Tree III | 1650 | — | parent pointers → becomes "intersection of two linked lists" |
| LCA of a Binary Tree IV | 1676 | `node` | N target nodes instead of 2 |
| Find Distance in a Binary Tree | 1740 | depth | find LCA first, then `d(root,p) + d(root,q) - 2*d(root,lca)` |

**Same "depth half" — the `(depth, …)` component alone:**

| Problem | LC # | What changes |
|---------|------|--------------|
| Maximum Depth of Binary Tree | 104 | return **only** the depth — this problem minus the payload |
| Balanced Binary Tree | 110 | return depth + a bool; `abs(l - r) > 1` short-circuits |
| Find Bottom Left Tree Value | 513 | deepest node again, but **leftmost** → BFS level-order is simpler |
| Find Leaves of Binary Tree | 366 | group nodes by height instead of picking one |
| Maximum Depth of N-ary Tree | 559 | loop over `children` instead of `left`/`right` |

**Same "combine left + right at each node" post-order shape (but global-variable style):**

| Problem | LC # | Combined value |
|---------|------|----------------|
| Diameter of Binary Tree | 543 | `left + right` edges, answer kept in a member field |
| Binary Tree Maximum Path Sum | 124 | `left + right + node.val`, clamp negatives to 0 |
| Longest Univalue Path | 687 | extend left/right only when `child.val == node.val` |
| Count Good Nodes in Binary Tree | 1448 | pre-order instead — info flows **down**, not up |

**Decision hint:**
```text
"smallest subtree containing X"      -> post-order (metric, node)   [LC 865]
"LCA of given nodes p, q"            -> post-order found-or-null     [LC 236]
"longest/max path through any node"  -> post-order + global var      [LC 543, 124]
"deepest / leftmost / level info"    -> BFS level-order              [LC 513, 199]
```

### 2) Move Parent Pattern - Bidirectional Tree Traversal

**Core Concept**: Convert tree to graph by building parent map, then use BFS for multi-directional exploration.

#### **Pattern Overview**
```text
Standard Tree (Unidirectional)        →    Tree with Parent Map (Bidirectional)

      1                                          1
     / \              Build Parent Map          / \
    2   3             ===============>         2 ← 3
   / \                                        / \
  4   5                                      4 ← 5

Can only go down (left/right)          Can go down (left/right) AND up (parent)
```

#### **LC 863: All Nodes Distance K in Binary Tree**

```java
// java
// IDEA: DFS + Parent Map + BFS
/**
 * Why this works?
 *
 * Tree → Graph → BFS (visiting)
 *
 * • From target you need to explore all directions reachable in k steps:
 *   left, right, and up (to parent).
 *   Converting the tree to an undirected graph (children + parent edges)
 *   and then running BFS from target to depth k returns the desired nodes.
 *
 * • visited ensures we don't revisit nodes (which would otherwise make the BFS
 *   incorrect/infinite once parent edges are present).
 */

public List<Integer> distanceK(TreeNode root, TreeNode target, int k) {
    List<Integer> res = new ArrayList<>();
    // parentMap stores parent pointers for every node (node -> parent)
    Map<TreeNode, TreeNode> parentMap = new HashMap<>();

    if (root == null) return res;

    // Step 1: Build parent map for all nodes
    buildParentMap(root, null, parentMap);

    // Step 2: BFS starting from target, stop at distance k
    Queue<TreeNode> queue = new LinkedList<>();
    Set<TreeNode> visited = new HashSet<>();
    queue.offer(target);
    visited.add(target);
    int dist = 0;

    /**
     * • Each loop iteration processes one BFS "level"
     *   (all nodes at the same distance from target).
     *
     * • If current distance dist equals k, the nodes currently
     *   in queue are exactly the nodes at distance k.
     */
    while (!queue.isEmpty()) {
        int size = queue.size();

        if (dist == k) {
            // Collect all nodes currently in the queue
            for (TreeNode node : queue) {
                res.add(node.val);
            }
            break;
        }

        /**
         * NOTE!!!
         * For each node, we visit cur.left, cur.right, and its parent via BFS
         *
         * • Process the size nodes of the current level:
         *   - For each cur, try to move to cur.left, cur.right, and its parent
         *   - visited.add(node) returns true only if node was not already present
         *     That both checks and marks in one call
         *
         * • After processing the whole level, increment dist and continue
         */
        for (int i = 0; i < size; i++) {
            TreeNode cur = queue.poll();

            // Explore neighbors: left, right, parent
            if (cur.left != null && visited.add(cur.left)) {
                queue.offer(cur.left);
            }
            if (cur.right != null && visited.add(cur.right)) {
                queue.offer(cur.right);
            }
            TreeNode parent = parentMap.get(cur);
            if (parent != null && visited.add(parent)) {
                queue.offer(parent);
            }
        }
        dist++;
    }

    return res;
}

/**
 * NOTE!!! Helper function to build parent map
 *
 * • We need to be able to move upwards from any node (to parent).
 *   A binary tree node only knows left/right children, so we precompute
 *   parents by a DFS.
 *
 * • Simple DFS that records parent of each node (parentMap.put(node, parent))
 * • For root we pass parent = null
 * • After this every node maps to its parent (or null for root)
 */
private void buildParentMap(TreeNode node, TreeNode parent,
                            Map<TreeNode, TreeNode> parentMap) {
    if (node == null) return;

    parentMap.put(node, parent);
    buildParentMap(node.left, node, parentMap);
    buildParentMap(node.right, node, parentMap);
}
```

```python
# python
# LC 863. All Nodes Distance K in Binary Tree
from collections import defaultdict, deque

def distanceK(root, target, k):
    """
    IDEA: Build bidirectional graph + BFS

    Step 1: DFS to build parent-child bidirectional edges
    Step 2: BFS from target to find all nodes at distance k
    """

    # Build undirected graph
    graph = defaultdict(list)

    def build_graph(parent, child):
        """DFS to build bidirectional edges"""
        if parent and child:
            graph[parent.val].append(child.val)
            graph[child.val].append(parent.val)
        if child.left:
            build_graph(child, child.left)
        if child.right:
            build_graph(child, child.right)

    # Build graph from root
    build_graph(None, root)

    # BFS from target
    queue = deque([(target.val, 0)])
    visited = {target.val}
    result = []

    while queue:
        node_val, dist = queue.popleft()

        if dist == k:
            result.append(node_val)
            continue

        # Explore all neighbors (left, right, parent)
        for neighbor in graph[node_val]:
            if neighbor not in visited:
                visited.add(neighbor)
                queue.append((neighbor, dist + 1))

    return result
```

#### **Key Points**
1. **Parent Map Construction**: O(N) time, O(N) space
2. **BFS Exploration**: O(N) time in worst case
3. **Visited Set**: Critical to prevent infinite loops
4. **Applications**:
   - Distance-based problems
   - Finding paths between arbitrary nodes
   - Closest node with property
   - Problems requiring upward traversal

#### **Pattern Comparison: Standard Tree vs Move Parent**

| Aspect | Standard Tree Traversal | Move Parent Pattern |
|--------|-------------------------|---------------------|
| **Direction** | Unidirectional (down only) | Bidirectional (down + up) |
| **Preprocessing** | None required | Build parent map (O(N)) |
| **Space Complexity** | O(h) recursion stack | O(N) parent map + visited |
| **Visited Tracking** | Usually not needed | **Critical** to prevent cycles |
| **Traversal Method** | DFS recursive | DFS (build) + BFS (explore) |
| **Use Cases** | Standard tree problems | Distance, path, multi-directional |
| **Graph Conversion** | Tree remains tree | Tree → Undirected graph |

#### **Common Mistakes to Avoid**
1. ❌ Forgetting visited set → infinite loops
2. ❌ Not handling null parent for root → NPE
3. ❌ Using DFS instead of BFS for distance → incorrect results
4. ❌ Building graph with values instead of node references → fails with duplicate values

#### **Move Parent Recipe**

**Template Structure:**
```text
1. Build parent map (DFS preprocessing)
2. Convert tree to undirected graph (children + parent edges)
3. BFS from target node, exploring all neighbors (left, right, parent)
4. Track visited nodes to avoid cycles
5. Stop at desired distance/condition
```

### 3) Distance Between Nodes — LC 1740


```java
// java
// LC 1740 Find Distance in a Binary Tree

// V1
/**
 *  IDEA of `getPathLen` help func:
 *
 *  🧠 Summary of Logic Flow
 *  1.  Stop when null (return -1) or when target is found (return distance).
 *  2.  Search left first. If found, return immediately.
 *  3.  Otherwise, search right.
 *  4.  If neither side contains the target, the function will bubble up -1.
 */
private int getPathLen(TreeNode root, int target, int dist) {
    /** NOTE !!!
     *
     *   base case:
     *
     *  •   Base case #1:
     *        if we hit a null node,
     *        -> the target DOES NOT exist in this branch.
     *
     *  •   Returning -1 is a sentinel value
     *      meaning “not found in this subtree”
     */
    if (root == null) {
        return -1;  // not found
    }
    /**
     *  NOTE !!!
     *
     *  •   Base case #2: if the current node matches the target, return dist, which is the current number of edges from the starting node (typically the LCA) to this node.
     *  •   This is the successful termination of the recursion.
     */
    if (root.val == target) {
        return dist;
    }

    /**
     *  NOTE !!!
     *
     *  •   Recurse into the left subtree.
     *  •   Increment dist by 1 because we moved down one level.
     *  •   Store the result in left.
     *        - If target is in this subtree,
     *          left will contain the distance.
     *       - Otherwise, left will be -1.
     */
    int left = getPathLen(root.left, target, dist + 1);
    /**
     *  •   If we found the target in the left subtree,
     *      return that distance immediately.
     *  •   This avoids unnecessary searching in the right subtree.
     */
    if (left != -1) {
        return left;
    }

    /**
     *  NOTE !!!
     *
     *  •   If not found on the left, search the right subtree with dist + 1.
     *  •   Return the result directly:
     *       - Either a valid distance if found,
     *       - Or -1 if not found in right subtree either.
     *
     */
    int right = getPathLen(root.right, target, dist + 1);
    return right;
}
    

// V2
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

    /** NOTE !!! check left BEFORE recursing right -- this is the short-circuit the
     *  walkthrough below describes. Evaluating both first still returns the right
     *  answer, but it visits the whole right subtree after the target was found. */
    int leftDist = getDistance(node.left, target);
    if (leftDist != -1) return leftDist + 1;

    int rightDist = getDistance(node.right, target);
    if (rightDist != -1) return rightDist + 1;
    return -1;
}
```

#### **Python — `get_dist` helper (the key part)** ⭐⭐⭐⭐⭐

**Idea:** `findDistance` = `get_lca` (find split point) + `get_dist` twice (measure each branch from the LCA). The whole trick lives in `get_dist`:

```python
# python
# LC 1740 Find Distance in a Binary Tree
class Solution:
    def findDistance(self, root, p, q):
        if p == q or not root:
            return 0
        # Step 1: find the split point (Lowest Common Ancestor)
        lca = self.get_lca(root, p, q)
        # Step 2: measure edge distance from LCA down to each target
        dist_p = self.get_dist(lca, p, 0)
        dist_q = self.get_dist(lca, q, 0)
        # dist is ALWAYS the sum — see the 2 structural cases below
        return dist_p + dist_q

    def get_lca(self, root, p, q):
        if not root:
            return None
        if root.val == p or root.val == q:  # match by VALUE (p, q are ints)
            return root
        left = self.get_lca(root.left, p, q)
        right = self.get_lca(root.right, p, q)
        if left and right:      # p, q split here -> this node is the LCA
            return root
        return left if left else right

    # NOTE !!! below helper func -- pre-order DFS
    def get_dist(self, root, target, dist):
        """
        NOTE !!!
        If not root, we return `-1` (sentinel = "not found in this branch"),
        but NOT `0`, since 0 is also a VALID answer (target found at current node).
        """
        if not root:
            return -1          # <-- sentinel: dead end, target not on this path

        if root.val == target:
            return dist        # <-- found: dist = # edges from LCA to here

        # NOTE !!! the right recursion is INSIDE the else path -- that is what makes
        #          the short-circuit real. Calling both first and then testing `left`
        #          walks the right subtree even when the target was already found.
        left = self.get_dist(root.left, target, dist + 1)
        if left != -1:
            # left subtree found the target -- pass that valid distance up, skip right
            return left

        # Otherwise return whatever the right subtree finds (a valid dist, or -1)
        return self.get_dist(root.right, target, dist + 1)
```

**Why `-1` and not `0` for "not found"?**

| Return value | Meaning |
|--------------|---------|
| `0`          | **VALID** — target found exactly at the current node (0 edges away) |
| `dist > 0`   | **VALID** — target found `dist` edges below the start node |
| `-1`         | **SENTINEL** — target is NOT in this branch (dead end) |

If we returned `0` for "not found", we could not distinguish *"found here, distance 0"* from *"not found"*. So `0` is reserved as a real distance and `-1` is the only safe "not found" flag.

**How the valid distance bubbles up (pre-order DFS):**
1. Hit `None` → return `-1` (this path is a dead end).
2. Hit the target → return the accumulated `dist` (an edge count ≥ 0).
3. Otherwise recurse `left` / `right` with `dist + 1`.
   - `left != -1` → target lives in the left subtree, forward that distance up **immediately** (short-circuit, skip right).
   - else return `right` (either the right subtree's valid distance, or `-1` if both sides failed).

**Why `dist_p + dist_q` is always correct** — starting from the LCA there are only 2 shapes:

```text
case 1: p and q are in different subtrees      case 2: one target IS the LCA
                                                        (ancestor of the other)
          LCA                                        p (= LCA)
         /   \                                          \
        p     q                                          ...
                                                           q
   dist = dist_p + dist_q                        dist_p = 0, so dist = dist_q
```

In both cases `get_dist(lca, p) + get_dist(lca, q)` gives the exact edge count on the path `p … q`.

#### **Visualization — how `get_dist` actually walks the tree** 🎨

Tree from the LC 1740 example: `root = [3,5,1,6,2,0,8,null,null,7,4]`, `p = 5`, `q = 0` → answer `3`.

```text
                      3          <- depth 0  (this is also the LCA of 5 and 0)
                   /     \
                  5       1      <- depth 1
                /   \    /  \
               6     2  0    8   <- depth 2
                    / \
                   7   4         <- depth 3
```

**Step 1 — `get_lca(root, 5, 0)` → node `3`** (5 is in the left subtree, 0 is in the right → they split at `3`).

**Step 2 — `get_dist(3, target=5, depth=0)`**

```text
get_dist(3, 5, 0)                 3 != 5  -> recurse left with depth+1
└── get_dist(5, 5, 1)             5 == 5  -> RETURN 1  ✅
        (right subtree never visited — short-circuited by `if left != -1`)

=> dist_p = 1
```

**Step 3 — `get_dist(3, target=0, depth=0)`** — the interesting one, because the left half is a **dead end**:

```text
get_dist(3, 0, 0)                       3 != 0
│
├── get_dist(5, 0, 1)                   5 != 0
│   ├── get_dist(6, 0, 2)               6 != 0
│   │   ├── get_dist(None, 0, 3) -> -1      ❌ dead end
│   │   └── get_dist(None, 0, 3) -> -1      ❌ dead end
│   │   RETURN -1                            ❌ bubbles up
│   └── get_dist(2, 0, 2)               2 != 0
│       ├── get_dist(7, 0, 3) -> -1 (both children None)   ❌
│       └── get_dist(4, 0, 3) -> -1 (both children None)   ❌
│       RETURN -1                            ❌
│   RETURN -1   <- whole LEFT subtree of 3 says "not here"
│
│   (left == -1, so we DO NOT short-circuit — we must try right)
│
└── get_dist(1, 0, 1)                   1 != 0
    └── get_dist(0, 0, 2)               0 == 0  -> RETURN 2  ✅
    RETURN 2  <- passed up UNCHANGED (no `+1`!)
RETURN 2

=> dist_q = 2
```

**Answer:** `dist_p + dist_q = 1 + 2 = 3` ✅ (path `5 - 3 - 1 - 0`, 3 edges)

**The two things to notice in that trace:**

```text
1) depth grows going DOWN      2) the found value flows UP untouched
   (as an argument)               (no accumulation on the way back)

     get_dist(.., depth=0)          RETURN 2  ▲
            │  depth+1                        │  same 2
            ▼                        RETURN 2 ▲
     get_dist(.., depth=1)                    │  same 2
            │  depth+1              RETURN 2  ▲
            ▼                                 │
     get_dist(.., depth=2)  ==  target  ->  emit `depth` (= 2)
```

Because the target value is **unique**, at most one branch can return a non-`-1` value — so there is nothing to `max()` or add on the way up. That is the key difference from height/depth problems:

| | Direction of the number | Combine step |
|---|---|---|
| `get_height` (LC 104) | computed **bottom-up** | `1 + max(left, right)` |
| `get_dist` (this pattern) | carried **top-down**, echoed back up | none — just forward the non-`-1` value |

**Sentinel propagation cheat-sheet:**

```text
            left      right     ->  return        meaning
            ----      -----         ------        -------
            -1        -1        ->  -1            target in NEITHER subtree
            d≥0       (skipped) ->  d             found left (short-circuit)
            -1        d≥0       ->  d             found right
```

#### **Variants of `get_dist` — where the counter lives** ⭐⭐⭐⭐⭐

All of these return the **same edge count** (and `-1` when the target isn't in the subtree). They
differ only in *where the counter lives*. The canonical top-down version is the one above; only the
two variants that are not merely a re-spelling of it are written out below.

| # | Variant | Signature | Counter lives | Short-circuits? | Notes |
|---|---------|-----------|---------------|-----------------|-------|
| **V1** | Top-down + explicit guard | `(node, target, depth)` | passed **down** as arg | ✅ yes (`if left != -1`) | the canonical version shown above — most explicit |
| **V2** | Top-down + `max()` trick | `(node, target, depth)` | passed **down** as arg | ❌ no | V1 with `return max(left, right)` in place of the `!= -1` guard: the target is unique, so at most one side returns `>= 0` and `max` bubbles it up (and yields `-1` when both fail). Shortest code, but it always scans **both** subtrees — strictly more work than V1, so prefer V1 |
| **V3** | Bottom-up (`+1` on return) | `(node, target)` | built **up** on the way back | ✅ yes | no `depth` param needed — written out below |
| **V4** | Iterative BFS | `(root, target)` | stored **in the queue** | ✅ yes (early return) | no recursion → no stack-overflow risk — written out below |

##### **V3 — Bottom-up DFS (no `depth` parameter)**

**Key Idea**: instead of carrying a counter *down*, start at `0` when you hit the target and add `1` per edge on the way *up*. The function then only needs two arguments.

```python
# python
# time = O(N), space = O(H)
def get_dist(self, node, target):
    if not node:
        return -1
    if node.val == target:
        return 0                 # distance to itself is 0

    _left  = self.get_dist(node.left,  target)
    _right = self.get_dist(node.right, target)

    # If found on the left, add 1 for the current edge and return it
    if _left >= 0:
        return _left + 1

    # If found on the right, add 1 for the current edge and return it
    if _right >= 0:
        return _right + 1

    return -1
```

**Direction contrast (V1 vs V3) on `get_dist(3, target=0)`:**

```text
        V1 (top-down)                      V3 (bottom-up)
        depth flows DOWN                   +1 flows UP

   3   get_dist(3, .., depth=0)       3   return 1 + 1 = 2   ▲
   |            │                     |                      │  +1
   1   get_dist(1, .., depth=1)       1   return 0 + 1 = 1   ▲
   |            │                     |                      │  +1
   0   match -> return depth = 2      0   match -> return 0  ▲

   answer emitted at the BOTTOM       answer assembled on the WAY BACK
```

⚠️ **Pitfall**: never write `1 + max(_left, _right)` here — if both are `-1` that yields `0`, which falsely reports *"found at this node"*. Always gate the `+1` behind a `>= 0` check (or, like the LeetCode-CA version, check `if left == right == -1: return -1` **before** the `1 + max(...)`).

##### **V4 — Iterative BFS (level-order, no recursion)**

**Key Idea**: push `(node, dist)` pairs; the level a node sits on **is** its distance from the start. Useful when the tree is very deep and recursion would risk a stack overflow.

```python
# python
# time = O(N), space = O(W)  -- W = max tree width
from collections import deque

def get_dist(self, root, target):
    if not root:
        return -1

    q = deque([(root, 0)])   # [node, current_distance]

    while q:
        node, dist = q.popleft()

        if node.val == target:
            return dist      # first hit IS the answer (level == distance)

        if node.left:
            q.append((node.left, dist + 1))
        if node.right:
            q.append((node.right, dist + 1))

    return -1                # scanned everything, target not below `root`
```

**Queue trace — `get_dist(3, target=0)` on the example tree:**

```text
                  3(0)
                /      \
             5(1)      1(1)
            /   \     /   \
          6(2) 2(2) 0(2)  8(2)      <- (n) = dist stored alongside the node

 pop        queue after push                        check
 ----       -------------------                     -----
 (3,0)      [(5,1), (1,1)]                          3 != 0
 (5,1)      [(1,1), (6,2), (2,2)]                   5 != 0
 (1,1)      [(6,2), (2,2), (0,2), (8,2)]            1 != 0
 (6,2)      [(2,2), (0,2), (8,2)]                   6 != 0
 (2,2)      [(0,2), (8,2), (7,3), (4,3)]            2 != 0
 (0,2)      -                                       0 == 0  -> RETURN 2  ✅
```

⚠️ **Note**: only `None` children are pushed-guarded here, so no `-1` sentinel is needed *inside* the loop — the sentinel is the single `return -1` after the queue drains.

##### **Which one to use?**

- **Interview / clarity** → **V1** (explicit sentinel handling is the thing interviewers want to see you reason about)
- **Code golf / clean one-liner** → **V2** (state the lost short-circuit out loud)
- **Fewer params, want the "returns 0 at target" convention** → **V3** (matches the `getDistance` Java version above)
- **Very deep / skewed tree, recursion depth is a real risk** → **V4**

All are `O(N)` time. Space: `O(H)` for V1–V3 (recursion stack, `H = N` worst case for a skewed tree), `O(W)` for V4 (queue width).

**Common mistakes:**
- ❌ returning `0` instead of `-1` for `None` → cannot distinguish "found at this node" from "not found"
- ❌ forgetting the `if left != -1: return left` guard → returns the right subtree's `-1` and loses the found distance
- ❌ adding `+1` in the depth-passing version → double counts (depth is already incremented on the way down)
- ❌ calling `get_dist` from `root` instead of from the `lca` → measures the wrong path

**Where this pattern shows up again:**

| Problem | LC # | How `get_dist` is used |
|---------|------|------------------------|
| Find Distance in a Binary Tree | 1740 | base pattern — `get_lca` + `get_dist` × 2 |
| All Nodes Distance K | 863 | distance downward from target; upward handled by parent map (section 2) |
| Maximum Depth | 104 | same DFS shape, but bottom-up `1 + max(...)` instead of a sentinel |
| Path Sum | 112 | identical top-down accumulation, carrying `remaining_sum` instead of `depth` |
| Smallest Subtree w/ Deepest Nodes | 865/1123 | LCA + depth combined into one `(depth, node)` return |

> **Ref:** `leetcode_python/Tree/find-distance-in-a-binary-tree.py`

#### **Pattern Recognition — is this a `get_dist` problem?**

**Pattern Recognition:**
- ✅ Need an **edge count**, not a node count (a node at depth 0 is 0 edges away)
- ✅ The target is **unique** in the tree (values are distinct)
- ✅ Distance is measured **downward** from a known start node (root or LCA)
- ❌ If both nodes may be in unrelated subtrees → find LCA first (see section 1)
- ❌ If you need distance in **all** directions (including upward) → use the Move Parent pattern in section 2

### 4) Root-to-Leaf Path Templates

#### Pre-order DFS + Backtracking Template (Java)

```java
// Template for root-to-leaf path collection (LC 112 / 113 / 257)
void dfs(TreeNode node, int remaining, List<Integer> path, List<List<Integer>> result) {
    if (node == null) return;

    // 1. Pre-order: add current node FIRST
    path.add(node.val);
    remaining -= node.val;

    // 2. Check leaf condition
    if (node.left == null && node.right == null && remaining == 0) {
        result.add(new ArrayList<>(path));  // save a COPY
    } else {
        // 3. Recurse
        dfs(node.left, remaining, path, result);
        dfs(node.right, remaining, path, result);
    }

    // 4. Backtrack: remove current node
    path.remove(path.size() - 1);
}
```

#### Path Update Strategies: Immutable String vs. Mutable List + Backtrack

> Two ways to track path state during DFS. Choosing the right one simplifies code significantly.

**Strategy 1: Immutable String — pass updated path in the DFS call (no backtrack needed)**

The key insight: when you pass `path + "->" + node.val` directly as an argument, each recursive call gets its **own copy** of the string. The parent's `path` is never modified, so **no explicit backtracking is needed**.

```java
// LC 257 — Binary Tree Paths (String path, no backtrack)
// Reference: ref_code/interviews-master/leetcode/tree/BinaryTreePaths.java
public List<String> binaryTreePaths(TreeNode root) {
    List<String> res = new ArrayList<>();
    if (root == null) return res;
    dfs(root, String.valueOf(root.val), res);
    return res;
}

private void dfs(TreeNode node, String path, List<String> res) {
    // 1. Leaf check: path is complete
    if (node.left == null && node.right == null) {
        res.add(path);
        return;
    }

    // 2. Traverse Left: path update happens INSIDE the DFS call
    if (node.left != null) {
        /** NOTE !!!
         *  We do `path update` within DFS call itself.
         *  path + "->" + node.left.val creates a NEW string,
         *  so `path` in the current frame is unchanged — no backtrack needed.
         */
        dfs(node.left, path + "->" + node.left.val, res);
    }

    // 3. Traverse Right: same pattern
    if (node.right != null) {
        /** NOTE !!!
         *  Same idea: path is NOT mutated here.
         *  Each branch gets its own copy of the string.
         */
        dfs(node.right, path + "->" + node.right.val, res);
    }
}
```

**Strategy 2: Mutable List — modify in place, then backtrack**

When using a mutable data structure (e.g., `List<Integer>`), the **same object** is shared across all recursive calls. You **must** undo changes after recursion returns.

```java
// LC 113 — Path Sum II (List path, explicit backtrack)
void dfs(TreeNode node, int remaining, List<Integer> path, List<List<Integer>> result) {
    if (node == null) return;

    path.add(node.val);           // ← mutate shared list
    remaining -= node.val;

    if (node.left == null && node.right == null && remaining == 0) {
        result.add(new ArrayList<>(path));  // save a COPY
    } else {
        dfs(node.left, remaining, path, result);
        dfs(node.right, remaining, path, result);
    }

    path.remove(path.size() - 1); // ← BACKTRACK: undo mutation
}
```

**Comparison:**

| Aspect | Immutable String | Mutable List + Backtrack |
|--------|-----------------|--------------------------|
| Path update location | Inside DFS call argument | Before DFS call |
| Backtrack needed? | No (each call gets own copy) | Yes (must undo mutation) |
| Memory | O(N) new strings per path | O(N) shared, reused list |
| Best for | String paths (LC 257) | Numeric paths (LC 113, 112) |
| Bug risk | Low (no shared state) | Medium (forget to backtrack) |

**Rule of thumb:**
- **Immutable (String, int)** → pass updated value in the call → no backtrack
- **Mutable (List, StringBuilder)** → modify before call → backtrack after call

```python
# Python equivalent — immutable string path (LC 257)
def binaryTreePaths(root):
    res = []
    def dfs(node, path):
        if not node.left and not node.right:
            res.append(path)
            return
        if node.left:
            dfs(node.left, path + "->" + str(node.left.val))  # new string, no backtrack
        if node.right:
            dfs(node.right, path + "->" + str(node.right.val))
    if root:
        dfs(root, str(root.val))
    return res
```

#### Pre-order DFS + Prefix Sum HashMap Template (Java)

> Used when path can start/end at **any node** (not just root-to-leaf).
> Inspired by LC 437 Path Sum III.

**Core Idea — "2-Sum on Tree":**
```text
curSum - targetSum = ancestorSum
→ if ancestorSum exists in map, a valid sub-path ends at current node
```

**Why Pre-order?**
- Prefix sums must be calculated **top-down** (pre-order)
- Post-order would calculate subtree sums, not root-to-node prefix sums

```java
// Template: Pre-order DFS + Prefix Sum HashMap (LC 437)
int count = 0;
Map<Long, Integer> prefixMap = new HashMap<>();

int pathSum(TreeNode root, int targetSum) {
    prefixMap.put(0L, 1);  // base case: empty path has sum 0
    dfs(root, 0L, targetSum);
    return count;
}

void dfs(TreeNode node, long curSum, int targetSum) {
    if (node == null) return;

    // 1. Pre-order: update prefix sum with current node
    curSum += node.val;

    // 2. Check: curSum - targetSum = a previous prefix sum?
    //    → means a valid sub-path ends here
    //    (2-sum trick: curSum - ancestorSum = targetSum)
    count += prefixMap.getOrDefault(curSum - targetSum, 0);

    // 3. Record current prefix sum BEFORE recursing into children
    prefixMap.put(curSum, prefixMap.getOrDefault(curSum, 0) + 1);

    // 4. Recurse (pre-order: process node before children)
    dfs(node.left, curSum, targetSum);
    dfs(node.right, curSum, targetSum);

    // 5. BACKTRACK: remove curSum so sibling branches are not affected
    prefixMap.put(curSum, prefixMap.get(curSum) - 1);
}
```

**Key differences vs. root-to-leaf backtracking:**

| Pattern                      | Path constraint          | Data structure        | Backtrack what?         |
|------------------------------|--------------------------|-----------------------|-------------------------|
| DFS + path list + backtrack  | Root → leaf only         | `List<Integer>` path  | Remove last element     |
| DFS + prefix sum + backtrack | Any node → any node ↓   | `Map<Long, Integer>`  | Decrement map count     |

## LC Examples

### 5-1) Find Paths with Specific Properties

#### Path Sum Problems
```python
# LC 112 Path Sum - Has Path with Target Sum
def hasPathSum(self, root, targetSum):
    if not root:
        return False
    if not root.left and not root.right:
        return root.val == targetSum
    return (self.hasPathSum(root.left, targetSum - root.val) or
            self.hasPathSum(root.right, targetSum - root.val))

# LC 113 Path Sum II - All Paths with Target Sum
def pathSum(self, root, targetSum):
    result = []

    def dfs(node, remaining, path):
        if not node:
            return

        path.append(node.val)

        if not node.left and not node.right and remaining == node.val:
            result.append(path[:])

        dfs(node.left, remaining - node.val, path)
        dfs(node.right, remaining - node.val, path)

        path.pop()  # backtrack

    dfs(root, targetSum, [])
    return result

# LC 437 Path Sum III - Number of Paths with Target Sum (any start/end)
def pathSum(self, root, targetSum):
    def dfs(node, current_sum):
        if not node:
            return 0

        current_sum += node.val
        result = prefix_sum.get(current_sum - targetSum, 0)

        prefix_sum[current_sum] = prefix_sum.get(current_sum, 0) + 1

        result += dfs(node.left, current_sum)
        result += dfs(node.right, current_sum)

        prefix_sum[current_sum] -= 1
        return result

    prefix_sum = {0: 1}
    return dfs(root, 0)
```

#### Path Length Problems
```python
# LC 543 Diameter of Binary Tree - Longest Path Between Any Two Nodes
def diameterOfBinaryTree(self, root):
    self.diameter = 0

    def dfs(node):
        if not node:
            return 0

        left_depth = dfs(node.left)
        right_depth = dfs(node.right)

        # Update diameter through current node
        self.diameter = max(self.diameter, left_depth + right_depth)

        return 1 + max(left_depth, right_depth)

    dfs(root)
    return self.diameter

# LC 687 Longest Univalue Path - Longest Path with Same Values
def longestUnivaluePath(self, root):
    self.longest = 0

    def dfs(node):
        if not node:
            return 0

        left_length = dfs(node.left)
        right_length = dfs(node.right)

        left_path = left_length + 1 if node.left and node.left.val == node.val else 0
        right_path = right_length + 1 if node.right and node.right.val == node.val else 0

        self.longest = max(self.longest, left_path + right_path)

        return max(left_path, right_path)

    dfs(root)
    return self.longest
```

```java
// java
// LC 112 Path Sum
public boolean hasPathSum(TreeNode root, int targetSum) {
    if (root == null) {
        return false;
    }

    if (root.left == null && root.right == null) {
        return root.val == targetSum;
    }

    return hasPathSum(root.left, targetSum - root.val) ||
           hasPathSum(root.right, targetSum - root.val);
}

// LC 113 Path Sum II
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

    path.remove(path.size() - 1); // backtrack
}
```

### 5-2) Closest Leaf in a Binary Tree (Move Parent Pattern) — LC 742
```python
# LeetCode 742. Closest Leaf in a Binary Tree
# V0
# IDEA : DFS build GRAPH + BFS find ans (MOVE PARENT PATTERN)
# See section 2 (Move Parent Pattern) for detailed explanation of this pattern
### NOTE :  closest to a leaf means the least number of edges travelled on the binary tree to reach any leaf of the tree. Also, a node is called a leaf if it has no children.
#         -> We only consider the min distance between left (no sub tree) and k
### NOTE : we need DFS create the graph
# https://www.youtube.com/watch?v=x1wXkRrpavw
# https://blog.csdn.net/qq_17550379/article/details/87778889
import collections
class Solution:
    # build graph via DFS
    # node : current node
    # parent : parent of current node
    def buildGraph(self, node, parent, k):
        if not node:
            return
        # if node.val == k, THEN GET THE start point FROM current "node",
        # then build graph based on above
        if node.val == k:
            self.start = node
        if parent:
            self.graph[node].append(parent)
            self.graph[parent].append(node)
        self.buildGraph(node.left, node, k)
        self.buildGraph(node.right, node, k)

    # search via DFS
    def findClosestLeaf(self, root, k):


        self.start = None
        ### NOTE : we need DFS create the graph
        self.buildGraph(root, None, k)
        q, visited = [root], set()
        #q, visited = [self.start], set() # need to validate this
        self.graph = collections.defaultdict(list)
        while q:
            for i in range(len(q)):
                cur = q.pop(0)
                # add cur to visited, NOT to visit this node again
                visited.add(cur)
                ### NOTICE HERE 
                # if not cur.left and not cur.right: means this is the leaf (HAS NO ANY left/right node) of the tree
                # so the first value of this is what we want, just return cur.val as answer directly
                if not cur.left and not cur.right:
                    # return the answer
                    return cur.val
                # if not find the leaf, then go through all neighbors of current node, and search again
                for node in self.graph:
                    if node not in visited: # need to check if "if node not in visited" or "if node in visited"
                        q.append(node)
```

## Pattern Selection Strategy

**Step 2 — Apply the pattern:**

```text
Root-to-leaf path problem?
  → Pre-order DFS + backtracking
  → Pattern: add node → check leaf → recurse → remove node (backtrack)

Path sum from ANY node to ANY node (downward)?
  → Pre-order DFS + prefix sum HashMap (2-sum trick)
  → Pattern: map.put(0,1) → curSum += val → check (curSum-target) in map
             → add to map → recurse → backtrack (decrement map)

Subtree computation (bottom-up)?
  → Post-order DFS
  → Pattern: recurse left, recurse right → combine at current node

Identify or compare subtrees by structure?
  → Post-order DFS + serialize "val,left,right" + HashMap
  → Pattern: serialize(left) + serialize(right) → build key "val,L,R"
             → map.getOrDefault(key,0) == 1 → duplicate! → add to result
             → map.put(key, count+1) → return key to parent

BST / sorted property?
  → In-order DFS
  → Pattern: recurse left → process node → recurse right
```

## Summary

| You are asked for... | Reach for | Section |
|---|---|---|
| the meeting point of two nodes | post-order LCA | 1) |
| the deepest-nodes subtree | post-order returning `(depth, node)` | 1) |
| nodes `k` steps away in **any** direction | parent map + BFS | 2) |
| edges between two arbitrary nodes | LCA + `get_dist` twice | 3) |
| all root-to-leaf paths matching a rule | pre-order DFS + backtracking | 4) |
| the count of any-to-any downward paths | pre-order DFS + prefix-sum HashMap | 4) |
| the longest path through any node | post-order height + global max | 5-1) |

**The three mistakes that cost the most here:**
- returning `0` instead of `-1` for "not found" — `0` is a valid distance
- forgetting the `visited` set once parent edges exist — the BFS loops forever
- calling `get_dist` from `root` instead of from the `lca` — measures the wrong path
