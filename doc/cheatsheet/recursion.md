# Recursion

## 0) Quick Reference

**When should I use recursion?**
- When a problem has **overlapping subproblems** that reduce the scope
- When you can clearly define a **base case** and **recursive case**
- When the problem naturally decomposes into smaller instances of itself
- For **tree/graph traversal** or **backtracking** problems

**Quick Decision Guide**

| Use Case | Pattern | Key Idea |
|----------|---------|----------|
| Need info from parent nodes | **Top-Down** | Pass context down while traversing |
| Need results from children | **Bottom-Up** | Solve children first, combine results |
| Need to split & merge results | **Divide & Conquer** | Partition problem, solve parts, merge |
| Need to explore all possibilities | **Backtracking** | DFS with decision making |
| Multiple recursive calls, same subproblems | **Memoization** | Cache results to avoid redundant work |

### Core Principle

For a problem F(X) where X is the input:

```
1. Break down into smaller scopes: x₀, x₁, ..., xₙ ∈ X
2. Recursively solve: F(x₀), F(x₁), ..., F(xₙ)
3. Combine results to solve F(X)
```

### Quick Tips

- **When in doubt**: Write down the **recurrence relationship** (how F(n) relates to F(n-1), F(n-2), etc.)
- **For redundant calls**: Apply **memoization** (cache intermediate results)
- **For stack overflow**: Use **tail recursion** or convert to **iteration**

---

## 1) Concepts

### 1-1) Core Ideas

### 1-2) Complexity Analysis

**Time Complexity**:
Think of recursion as a **tree structure**:
```
        fib(5)
       /      \
    fib(4)    fib(3)
    /    \     /    \
fib(3)  fib(2) fib(2) fib(1)
 /   \    /  \   /  \
fib(2) fib(1) fib(1) fib(0)
 /   \
fib(1) fib(0)
```

Given a recursion algorithm: **O(T) = R × O(S)**
- **R** = number of recursion invocations
- **O(S)** = time complexity of work per call
- For Fibonacci without memoization: **O(2^n)** (exponential)

**Space Complexity**:

**Recursion-Related Space** (Call Stack):
- Local variables in recursive function calls
- Input parameters
- Output variables
- **Stack overflow risk**: when allocated stack space reaches system limit

**Recursion-Independent Space** (Heap):
- Global variables
- Memoization cache (stores intermediate results)
- **Important**: Count memoization space when analyzing overall complexity

### 1-3) Related Concepts

Recursion is used in:
- **DFS** (Depth-First Search) — tree/graph traversal
- **Backtracking** — exploring all possibilities with pruning
- **Tree problems** — natural fit for recursive algorithms
- **Dynamic Programming** — with memoization optimization

---

## 2) Patterns

### 2-1) Basic Operation

Endless loop through elements in list (common in backtracking/generation):
```python
# Example: LC 22 (Generate Parentheses)
_list = ["(", ")"]
for x in _list:
    _tmp = tmp + x
    help(_tmp)
```

---

### 2-2) Top-Down Recursion

**Definition**: Start from the root and make decisions at each node based on information passed down from parent nodes. Also known as "preorder" approach.

**Time Complexity**:
- Usually O(n) where n is the number of nodes
- Can be O(n²) if same subproblems are solved repeatedly without memoization

**Space Complexity**:
- O(h) where h is the height of recursion tree (call stack)
- O(n) additional space if memoization is used

**Use Cases**:
- When you need to pass information from parent to child
- Tree traversal with accumulated state
- Path-based problems
- Validation problems

**Pros**:
- Intuitive and easy to understand
- Natural for problems requiring parent-to-child information flow
- Good for early termination conditions

**Cons**:
- May do redundant calculations without memoization
- Can have higher space complexity due to call stack

**Pattern**:
```python
def topDown(node, parentInfo):
    # Base case
    if not node:
        return baseResult

    # Use parentInfo to make decision
    currentResult = processWithParentInfo(node, parentInfo)

    # Pass updated info to children
    newParentInfo = updateParentInfo(parentInfo, node)
    leftResult = topDown(node.left, newParentInfo)
    rightResult = topDown(node.right, newParentInfo)

    # Combine results
    return combineResults(currentResult, leftResult, rightResult)
```

**Common LeetCode Problems**:
- LC 104: Maximum Depth of Binary Tree
- LC 110: Balanced Binary Tree
- LC 112: Path Sum
- LC 113: Path Sum II
- LC 124: Binary Tree Maximum Path Sum
- LC 236: Lowest Common Ancestor
- LC 257: Binary Tree Paths
- LC 404: Sum of Left Leaves
- LC 437: Path Sum III

**Example - Path Sum (LC 112)**:
```python
def hasPathSum(self, root, targetSum):
    def topDown(node, currentSum):
        if not node:
            return False

        currentSum += node.val

        # Leaf node check
        if not node.left and not node.right:
            return currentSum == targetSum

        # Continue to children with updated sum
        return (topDown(node.left, currentSum) or
                topDown(node.right, currentSum))

    return topDown(root, 0)
```

### 2-3) Bottom-Up Recursion

**Definition**: Start from leaf nodes and build up the solution by combining results from child nodes. Also known as "postorder" approach.

**Time Complexity**:
- Usually O(n) where n is the number of nodes
- Generally more efficient as each node is visited exactly once

**Space Complexity**:
- O(h) where h is the height of recursion tree (call stack)
- Usually no additional space needed for memoization

**Use Cases**:
- When solution depends on results from subtrees
- Tree property calculations (height, diameter, etc.)
- Aggregation problems
- Dynamic programming on trees

**Pros**:
- More efficient - each subproblem solved exactly once
- Natural for problems requiring child-to-parent information flow
- Often leads to cleaner code
- Better performance in most cases

**Cons**:
- Can be less intuitive for some problems
- May need to return multiple values from recursive calls

**Pattern**:
```python
def bottomUp(node):
    # Base case
    if not node:
        return baseResult

    # Get results from children first
    leftResult = bottomUp(node.left)
    rightResult = bottomUp(node.right)

    # Process current node using children results
    currentResult = processNode(node, leftResult, rightResult)

    return currentResult
```

**Common LeetCode Problems**:
- LC 104: Maximum Depth of Binary Tree
- LC 110: Balanced Binary Tree
- LC 543: Diameter of Binary Tree
- LC 124: Binary Tree Maximum Path Sum
- LC 968: Binary Tree Cameras
- LC 979: Distribute Coins in Binary Tree
- LC 1120: Maximum Average Subtree
- LC 1130: Minimum Cost Tree From Leaf Values
- LC 1372: Longest ZigZag Path in a Binary Tree

**Example - Maximum Depth (LC 104)**:
```python
def maxDepth(self, root):
    def bottomUp(node):
        if not node:
            return 0

        # Get depths from children
        leftDepth = bottomUp(node.left)
        rightDepth = bottomUp(node.right)

        # Current depth is max of children + 1
        return max(leftDepth, rightDepth) + 1

    return bottomUp(root)
```

**Example - Balanced Binary Tree (LC 110)**:
```python
def isBalanced(self, root):
    def bottomUp(node):
        if not node:
            return True, 0  # (isBalanced, height)

        # Check left subtree
        leftBalanced, leftHeight = bottomUp(node.left)
        if not leftBalanced:
            return False, 0

        # Check right subtree
        rightBalanced, rightHeight = bottomUp(node.right)
        if not rightBalanced:
            return False, 0

        # Check current node balance
        isCurrentBalanced = abs(leftHeight - rightHeight) <= 1
        currentHeight = max(leftHeight, rightHeight) + 1

        return isCurrentBalanced, currentHeight

    balanced, _ = bottomUp(root)
    return balanced
```

**Comparison Table**:

| Aspect | Top Down | Bottom Up |
|--------|----------|-----------|
| **Direction** | Root → Leaves | Leaves → Root |
| **Information Flow** | Parent → Child | Child → Parent |
| **When to Use** | Need parent context | Need subtree results |
| **Efficiency** | May have redundancy | Usually more efficient |
| **Intuition** | More intuitive for path problems | More intuitive for aggregation |
| **Memoization Need** | Often needed | Rarely needed |

---

### 2-4) Pass State to Next Recursion

Pass accumulated state/context as parameters to child recursive calls. Useful when you need to track information from parent nodes.

**Example: LC 404 (Sum of Left Leaves)**
```java
// LC 404 - Sum of Left Leaves
// IDEA: Pre-order traversal, pass isLeft flag to track if node is left child
private int processSubtree(TreeNode subtree, boolean isLeft) {
    // Base case: empty subtree
    if (subtree == null) {
        return 0;
    }

    // Base case: leaf node
    if (subtree.left == null && subtree.right == null) {
        return isLeft ? subtree.val : 0;
    }

    // Recursive case: process left and right subtrees
    return processSubtree(subtree.left, true) + processSubtree(subtree.right, false);
}
```

**Key Insight**: By passing `isLeft` as a parameter, we track parent context without needing global state.

---

### 2-5) Any-True Status in Recursion

When you need to find ANY true result among recursive calls, use OR logic. Stop early if any recursive call returns true.

**Example: LC 572 (Subtree of Another Tree)**

```java
// LC 572 - Subtree of Another Tree
public boolean isSubtree(TreeNode root, TreeNode subRoot) {
    // Check if subtree rooted at 'root' matches 'subRoot'
    // Use OR: if ANY recursive call returns true, short-circuit and return true
    return isSameTree(root, subRoot) 
        || isSubtree(root.left, subRoot) 
        || isSubtree(root.right, subRoot);
}

private boolean isSameTree(TreeNode node1, TreeNode node2) {
    if (node1 == null || node2 == null) {
        return node1 == null && node2 == null;
    }
    return node1.val == node2.val 
        && isSameTree(node1.left, node2.left) 
        && isSameTree(node1.right, node2.right);
}
```

**Key Insight**: Using OR (`||`) allows early exit when a true result is found, avoiding unnecessary recursive calls.

---

### 2-6) Cartesian Product Construction

**Definition**: Recursively generate all possible structures by dividing a range, building all sub-results for each partition, and combining them via Cartesian product. This is a form of **Divide & Conquer** where the "combine" step enumerates all left × right combinations.

**Time Complexity**: O(4^n / n^(3/2)) — Catalan number growth

**Space Complexity**: O(4^n / n^(3/2)) — storing all generated structures

**Use Cases**:
- Generate all structurally unique trees (BST, full binary trees)
- Enumerate all ways to parenthesize/split an expression
- Any problem where you partition a range and combine all sub-results

**Pattern**:
```
1. Pick each element i in [start, end] as the "root" / split point
2. Recursively build all left results from [start, i-1]
3. Recursively build all right results from [i+1, end]
4. Cartesian product: for each left × right, construct and collect result
5. Base case: empty range → return [null/None] (one empty result, NOT empty list)
```

```java
// Template: Recursive Construction via Cartesian Product
private List<TreeNode> build(int start, int end) {
    List<TreeNode> res = new ArrayList<>();
    if (start > end) {
        res.add(null);  // CRITICAL: null = valid empty subtree
        return res;
    }
    for (int i = start; i <= end; i++) {
        List<TreeNode> lefts = build(start, i - 1);
        List<TreeNode> rights = build(i + 1, end);
        for (TreeNode l : lefts)
            for (TreeNode r : rights)
                res.add(new TreeNode(i, l, r));
    }
    return res;
}
```

**Key Insight**: Base case must return `[null]` (list containing null), NOT empty list. Otherwise Cartesian product loses all trees with empty left/right subtrees.

**Optimization**: Add memoization with `Map<Pair<Integer,Integer>, List<TreeNode>>` to avoid recomputing overlapping subproblems.

**Common LeetCode Problems**:
- LC 95: Unique Binary Search Trees II
- LC 96: Unique Binary Search Trees (Catalan count)
- LC 241: Different Ways to Add Parentheses
- LC 894: All Possible Full Binary Trees
- LC 1382: Balance a Binary Search Tree

**Example — LC 95: Unique Binary Search Trees II**:
```python
def generateTrees(n):
    if n == 0: return []
    def generate(start, end):
        if start > end:
            return [None]
        all_trees = []
        for i in range(start, end + 1):
            for left in generate(start, i - 1):
                for right in generate(i + 1, end):
                    root = TreeNode(i)
                    root.left = left
                    root.right = right
                    all_trees.append(root)
        return all_trees
    return generate(1, n)
```

---

## 3) Advanced Techniques

### 3-1) Memoization

**Idea**: Cache results of recursive calls to avoid redundant work when the same subproblem is encountered multiple times.

**When to Use**:
- When recursive calls repeat (overlapping subproblems)
- When time complexity without memoization is exponential
- Trade space for time (use a hash map for cache)

**Example 1: Fibonacci**
```python
# Without memoization: O(2^n) — exponential
def fibonacci(n):
    if n < 2:
        return n
    return fibonacci(n - 1) + fibonacci(n - 2)

# With memoization: O(n) — linear
def fibonacci(n):
    cache = {}
    def helper(n):
        if n in cache:
            return cache[n]
        if n < 2:
            res = n
        else:
            res = helper(n - 1) + helper(n - 2)
        cache[n] = res
        return res
    return helper(n)
```

**Example 2: Climbing Stairs (LC 70)**
```python
# Without memoization: O(2^n)
class Solution:
    def climbStairs(self, n):
        if n == 1:
            return 1
        if n == 2:
            return 2
        return self.climbStairs(n - 1) + self.climbStairs(n - 2)

# With memoization: O(n)
class Solution:
    def climbStairs(self, n):
        cache = {}
        def helper(n):
            if n in cache:
                return cache[n]
            if n <= 2:
                res = n
            else:
                res = helper(n - 2) + helper(n - 1)
            cache[n] = res
            return res
        return helper(n)
```

**Reference**: https://leetcode.com/explore/learn/card/recursion-i/255/recursion-memoization/1495/

---

### 3-2) Divide & Conquer

**Template**:
```
1. Divide: Split problem into subproblems
2. Conquer: Solve each subproblem recursively
3. Combine: Merge subproblem results
```

**Pseudo-code**:
```python
def divide_and_conquer(problem):
    # (1) Divide
    subproblems = divide(problem)
    
    # (2) Conquer
    results = [divide_and_conquer(sub) for sub in subproblems]
    
    # (3) Combine
    return combine(results)
```

**Common Examples**:
- Merge Sort — O(n log n)
- Quick Sort — O(n log n) average
- Binary Search — O(log n)

**Common LeetCode Problems**:
- LC 22: Generate Parentheses
- LC 84: Largest Rectangle in Histogram
- LC 315: Count of Smaller Numbers After Self
- LC 493: Reverse Pairs
- LC 1649: Create Sorted Array Through Instructions

**Reference**: https://leetcode.com/explore/learn/card/recursion-ii/470/divide-and-conquer/2869/

---

### 3-3) Recursion to Iteration (Unfold Recursion)

**Why Convert**:
- Avoid stack overflow risk
- Improve space/time efficiency
- Reduce function call overhead

**How to Convert**:
```
1. Use a stack or queue to replace the system call stack
2. At each recursion point, push parameters onto data structure
3. Replace recursive chain with loop over the data structure
```

**Example**: https://leetcode.com/explore/learn/card/recursion-ii/503/recursion-to-iteration/2693/

---

## 4) Complete LeetCode Examples

### 4-1) Symmetric Tree (LC 101)

**Pattern**: Bottom-up recursion, comparing two subtrees in parallel.

```python
class Solution:
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

---

### 4-2) One Edit Distance (LC 161)

**Pattern**: Pruning branches early (abs difference > 1), then checking each position.

```python
class Solution:
    def isOneEditDistance(self, s, t):
        m, n = len(s), len(t)
        if abs(m - n) > 1:
            return False
        if m > n:
            return self.isOneEditDistance(t, s)
        for i in range(m):
            if s[i] != t[i]:
                if m == n:
                    return s[i + 1:] == t[i + 1:]
                return s[i:] == t[i + 1:]
        return m != n
```

---

### 4-3) Merge Two Sorted Lists (LC 21)

**Pattern**: Simple recursion with local state update.

```python
class Solution:
    def mergeTwoLists(self, l1, l2):
        if not l1 or not l2:
            return l1 or l2
        if l1.val < l2.val:
            l1.next = self.mergeTwoLists(l1.next, l2)
            return l1
        else:
            l2.next = self.mergeTwoLists(l1, l2.next)
            return l2
```

---

### 4-4) Subtree of Another Tree (LC 572)

**Pattern**: Any-true status with recursive helper.

```python
class Solution:
    def isSubtree(self, root, subRoot):
        def isSameTree(p, q):
            if not p and not q:
                return True
            if not p or not q:
                return False
            return (p.val == q.val and 
                    isSameTree(p.left, q.left) and 
                    isSameTree(p.right, q.right))
        
        if not root and not subRoot:
            return True
        if not root or not subRoot:
            return False
        # Use OR: if any recursive call returns True, stop early
        return (isSameTree(root, subRoot) or 
                self.isSubtree(root.left, subRoot) or 
                self.isSubtree(root.right, subRoot))
```

**Java Version**:
```java
public boolean isSubtree(TreeNode root, TreeNode subRoot) {
    if (root == null) {
        return false;
    }
    if (isIdentical(root, subRoot)) {
        return true;
    }
    return isSubtree(root.left, subRoot) || isSubtree(root.right, subRoot);
}

private boolean isIdentical(TreeNode node1, TreeNode node2) {
    if (node1 == null || node2 == null) {
        return node1 == null && node2 == null;
    }
    return node1.val == node2.val && 
           isIdentical(node1.left, node2.left) && 
           isIdentical(node1.right, node2.right);
}
```