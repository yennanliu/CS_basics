# Recursion

## LeetCode Problem Lists

- [Recursion](https://leetcode.com/problem-list/recursion/)

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

```text
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

### 1-1) Complexity Analysis

**Time Complexity**:
Think of recursion as a **tree structure**:
```text
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

### 1-2) Related Concepts

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

### 2-2) Top-Down Recursion — LC 112

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

### 2-3) Bottom-Up Recursion — LC 104

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

### 2-4) Pass State to Next Recursion — LC 404

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

### 2-5) Any-True Status in Recursion — LC 572

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

### 2-6) Cartesian Product Construction — LC 95

**Definition**: Recursively generate all possible structures by dividing a range, building all sub-results for each partition, and combining them via Cartesian product. This is a form of **Divide & Conquer** where the "combine" step enumerates all left × right combinations.

**Time Complexity**: O(4^n / n^(3/2)) — Catalan number growth

**Space Complexity**: O(4^n / n^(3/2)) — storing all generated structures

**Use Cases**:
- Generate all structurally unique trees (BST, full binary trees)
- Enumerate all ways to parenthesize/split an expression
- Any problem where you partition a range and combine all sub-results

**Pattern**:
```text
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

### 3-1) Memoization — LC 70

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

### 3-2) Divide & Conquer — LC 23

**Template**:
```text
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
```text
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

---

## 5) More Recursion Templates

The sections above are tree-centric. The four templates below cover the other recursion
shapes that show up in interviews: **linked-list rewiring**, **recursive-descent parsing**,
**halving recursion**, and **pure recurrence reduction**.

**Quick Decision Table**

| Signal in the problem | Template | Examples |
|-----------------------|----------|----------|
| Rebuild/reorder a linked list | **5-1) Rewire & Return New Head** | LC 206, 24, 25, 203, 234 |
| Nested brackets / grammar in a string | **5-2) Recursive Descent (shared cursor)** | LC 394, 224, 1106, 736 |
| `n` shrinks by a *factor* each step | **5-3) Halving Recursion** | LC 50, 1922, 231/326/342 |
| A closed-form `f(n)` from `f(n-1)` | **5-4) Recurrence Reduction** | LC 779, 1823, 273 |

---

### 5-1) Recursive Linked List Rewiring — LC 206 / 24 / 25 ⭐⭐⭐⭐⭐

**Definition**: A recursive linked-list function takes the head of a sublist and
**returns the new head of the already-processed sublist**. The caller then splices that
returned head onto its own node. All pointer surgery happens *after* the recursive call
returns (i.e. it is bottom-up on a list).

**The 3-step contract** — get these right and every list recursion falls out:

```text
1. Base case   : list too short to change -> return head unchanged
2. Recurse     : newTail/rest = f(<node further down the list>)
3. Rewire      : fix current node's `next`, then RETURN the node that is now first
```

**Time**: O(n) — each node touched once. **Space**: O(n) call stack (O(n/k) for LC 25).

**Key Insight**: Never try to mutate "in place and return void". The returned value *is*
the new head; forgetting to set `head.next = null` in LC 206 is the classic cycle bug.

**Example — LC 206: Reverse Linked List**

```java
// java
// LC 206 - Reverse Linked List
// IDEA: recursion returns the NEW head; on the way back up, make my successor point at me
// time = O(n), space = O(n) (call stack)
public ListNode reverseList(ListNode head) {
    // base: empty or single node -> already reversed
    if (head == null || head.next == null) return head;

    ListNode newHead = reverseList(head.next); // newHead = tail of original list
    head.next.next = head;                     // successor now points back at me
    head.next = null;                          // CRITICAL: cut old link, else cycle
    return newHead;                            // head never changes going back up
}
```

```python
# python
# LC 206 - Reverse Linked List
# IDEA: recursion returns the NEW head; on the way back up, make my successor point at me
# time = O(n), space = O(n) (call stack)
def reverseList(head):
    if not head or not head.next:
        return head
    new_head = reverseList(head.next)
    head.next.next = head
    head.next = None      # CRITICAL: cut old link, else cycle
    return new_head
```

**Example — LC 24: Swap Nodes in Pairs**

```java
// java
// LC 24 - Swap Nodes in Pairs
// IDEA: swap the first two nodes, recurse on the rest, return the 2nd node as new head
// time = O(n), space = O(n)
public ListNode swapPairs(ListNode head) {
    if (head == null || head.next == null) return head; // 0 or 1 node left

    ListNode second = head.next;
    head.next = swapPairs(second.next); // rest of list, already swapped
    second.next = head;
    return second;                      // second is now the head of this pair
}
```

```python
# python
# LC 24 - Swap Nodes in Pairs
# IDEA: swap the first two nodes, recurse on the rest, return the 2nd node as new head
# time = O(n), space = O(n)
def swapPairs(head):
    if not head or not head.next:
        return head
    second = head.next
    head.next = swapPairs(second.next)
    second.next = head
    return second
```

**Example — LC 25: Reverse Nodes in k-Group** (generalises LC 24 from k=2 to any k)

```java
// java
// LC 25 - Reverse Nodes in k-Group
// IDEA: probe k nodes ahead; if a full group exists, recurse on the remainder FIRST,
//       then reverse this group with the recursive result as its new tail
// time = O(n), space = O(n/k) recursion depth
public ListNode reverseKGroup(ListNode head, int k) {
    // step 1: is there a full group of k? if not, leave the tail untouched
    ListNode node = head;
    for (int i = 0; i < k; i++) {
        if (node == null) return head;
        node = node.next;
    }
    // node = (k+1)-th node = start of the remainder

    // step 2: solve the remainder first, it becomes what this group points to
    ListNode prev = reverseKGroup(node, k);

    // step 3: standard iterative reverse of exactly k nodes onto `prev`
    ListNode cur = head;
    for (int i = 0; i < k; i++) {
        ListNode nxt = cur.next;
        cur.next = prev;
        prev = cur;
        cur = nxt;
    }
    return prev; // prev = k-th node = new head of this group
}
```

```python
# python
# LC 25 - Reverse Nodes in k-Group
# IDEA: probe k nodes ahead; if a full group exists, recurse on the remainder FIRST,
#       then reverse this group with the recursive result as its new tail
# time = O(n), space = O(n/k) recursion depth
def reverseKGroup(head, k):
    node = head
    for _ in range(k):
        if not node:
            return head          # fewer than k nodes left -> keep as-is
        node = node.next

    prev = reverseKGroup(node, k)  # reversed remainder
    cur = head
    for _ in range(k):
        nxt = cur.next
        cur.next = prev
        prev = cur
        cur = nxt
    return prev
```

**Variations**

- **LC 203 (Remove Linked List Elements)** — twist: *deletion* instead of reordering, so
  the "rewire" step is a conditional return. No dummy node needed:

```java
// java
// LC 203 - Remove Linked List Elements
// IDEA: clean the rest first, then decide whether to keep myself
// time = O(n), space = O(n)
public ListNode removeElements(ListNode head, int val) {
    if (head == null) return null;
    head.next = removeElements(head.next, val);
    return head.val == val ? head.next : head; // skip myself if I match
}
```

- **LC 234 (Palindrome Linked List)** — twist: don't rewire at all; use the **call stack as
  a reverse iterator**. The unwinding recursion walks backwards while a field walks forwards.

```java
// java
// LC 234 - Palindrome Linked List
// IDEA: recursion unwinds back-to-front; `front` pointer moves front-to-back in lockstep
// time = O(n), space = O(n) (O(1) space alternative: reverse the 2nd half iteratively)
private ListNode front;
public boolean isPalindrome(ListNode head) {
    front = head;
    return check(head);
}
private boolean check(ListNode node) {
    if (node == null) return true;
    if (!check(node.next)) return false; // go to the end first
    if (node.val != front.val) return false;
    front = front.next;                  // compare back-node vs front-node
    return true;
}
```

```python
# python
# LC 234 - Palindrome Linked List
# IDEA: recursion unwinds back-to-front; `front` pointer moves front-to-back in lockstep
# time = O(n), space = O(n)
def isPalindrome(head):
    front = head
    def check(node):
        nonlocal front
        if not node:
            return True
        if not check(node.next):
            return False
        if node.val != front.val:
            return False
        front = front.next
        return True
    return check(head)
```

- **LC 143 (Reorder List)** — twist: composition, not a new recursion. Split at the middle
  (slow/fast), reverse the 2nd half with **LC 206**, then interleave the two halves
  (merge step of **LC 21**).

**Common LeetCode Problems**
- LC 206: Reverse Linked List (the base template)
- LC 24: Swap Nodes in Pairs
- LC 25: Reverse Nodes in k-Group (hard version of LC 24)
- LC 203: Remove Linked List Elements
- LC 234: Palindrome Linked List
- LC 143: Reorder List
- LC 21: Merge Two Sorted Lists (see 4-3)

---

### 5-2) Recursive Descent Parsing — LC 394 / 224 ⭐⭐⭐⭐⭐

**Definition**: Parse a nested string by writing **one function per grammar rule** and
sharing a single **cursor** (index) across all of them. Each function consumes exactly the
characters of its rule and leaves the cursor just past them. Nested brackets = recursion;
multiple precedence levels = **mutual recursion** (`expr` calls `term`, `term` calls `expr`).

**Time**: O(n) tokens (O(output) when the grammar expands, e.g. LC 394).
**Space**: O(nesting depth).

**Pattern**:
```text
1. Keep the cursor OUTSIDE the recursion (field in Java, `nonlocal` in Python).
   Passing `int i` by value does not work - the caller must see how far the callee ate.
2. One function per grammar rule; each one:
     - reads the tokens of its own rule
     - recurses at the nesting point ('(' , '[' , a sub-expression)
     - returns its value with the cursor sitting on the NEXT unconsumed char
3. Be explicit about who consumes the closing delimiter (pick a convention, keep it).
```

**Key Insight**: The single most common bug is a cursor that is a *parameter* instead of
shared state — the parent then re-parses characters the child already consumed. Write the
grammar down in BNF first; the code is a mechanical translation of it.

**Example — LC 394: Decode String** (grammar: `str := (char | int '[' str ']')*`)

```java
// java
// LC 394 - Decode String
// IDEA: recursive descent with a shared cursor; each call handles ONE bracket level
// time = O(total output length), space = O(nesting depth)
private int i = 0;

public String decodeString(String s) {
    i = 0;
    return parse(s);
}

// parses until end-of-string or the ']' that closes the current level
private String parse(String s) {
    StringBuilder sb = new StringBuilder();
    while (i < s.length() && s.charAt(i) != ']') {
        char c = s.charAt(i);
        if (Character.isDigit(c)) {
            int k = 0;
            while (Character.isDigit(s.charAt(i))) {   // multi-digit repeat count
                k = k * 10 + (s.charAt(i) - '0');
                i++;
            }
            i++;                        // consume '['
            String inner = parse(s);    // recurse: body of this bracket
            i++;                        // consume ']'
            for (int t = 0; t < k; t++) sb.append(inner);
        } else {
            sb.append(c);
            i++;
        }
    }
    return sb.toString();
}
```

```python
# python
# LC 394 - Decode String
# IDEA: recursive descent with a shared cursor; each call handles ONE bracket level
# time = O(total output length), space = O(nesting depth)
def decodeString(s):
    i = 0

    def parse():
        nonlocal i
        out = []
        while i < len(s) and s[i] != "]":
            if s[i].isdigit():
                k = 0
                while s[i].isdigit():          # multi-digit repeat count
                    k = k * 10 + int(s[i]); i += 1
                i += 1                          # consume '['
                inner = parse()                 # recurse: body of this bracket
                i += 1                          # consume ']'
                out.append(inner * k)
            else:
                out.append(s[i]); i += 1
        return "".join(out)

    return parse()
```

**Example — LC 224: Basic Calculator** (mutual recursion over a 2-level grammar)

```text
expr := term (('+' | '-') term)*
term := number | '(' expr ')' | '-' term      # unary minus, e.g. "-(3+4)"
```

```java
// java
// LC 224 - Basic Calculator
// IDEA: one function per grammar rule; expr <-> term is mutual recursion, '(' re-enters expr
// time = O(n), space = O(paren depth)
private int p = 0;

public int calculate(String s) {
    p = 0;
    return expr(s);
}

private int expr(String s) {           // left-to-right +/- chain
    int res = term(s);
    while (true) {
        skipSpace(s);
        if (p >= s.length()) break;
        char c = s.charAt(p);
        if (c == '+')      { p++; res += term(s); }
        else if (c == '-') { p++; res -= term(s); }
        else break;                    // hit ')' -> let the caller consume it
    }
    return res;
}

private int term(String s) {           // number | '(' expr ')' | unary minus
    skipSpace(s);
    char c = s.charAt(p);
    if (c == '(') {
        p++;                           // consume '('
        int v = expr(s);               // mutual recursion
        skipSpace(s);
        p++;                           // consume ')'
        return v;
    }
    if (c == '-') { p++; return -term(s); }
    int v = 0;
    while (p < s.length() && Character.isDigit(s.charAt(p))) {
        v = v * 10 + (s.charAt(p) - '0');
        p++;
    }
    return v;
}

private void skipSpace(String s) {
    while (p < s.length() && s.charAt(p) == ' ') p++;
}
```

```python
# python
# LC 224 - Basic Calculator
# IDEA: one function per grammar rule; expr <-> term is mutual recursion, '(' re-enters expr
# time = O(n), space = O(paren depth)
def calculate(s):
    i = 0

    def skip_space():
        nonlocal i
        while i < len(s) and s[i] == " ":
            i += 1

    def expr():                       # term (('+'|'-') term)*
        nonlocal i
        res = term()
        while True:
            skip_space()
            if i >= len(s):
                break
            if s[i] == "+":
                i += 1; res += term()
            elif s[i] == "-":
                i += 1; res -= term()
            else:
                break                 # ')' -> caller consumes it
        return res

    def term():                       # number | '(' expr ')' | '-' term
        nonlocal i
        skip_space()
        if s[i] == "(":
            i += 1
            v = expr()
            skip_space()
            i += 1
            return v
        if s[i] == "-":
            i += 1
            return -term()
        v = 0
        while i < len(s) and s[i].isdigit():
            v = v * 10 + int(s[i]); i += 1
        return v

    return expr()
```

**Variations**
- **LC 1106 (Parsing A Boolean Expression)** — same cursor template; the grammar is
  `expr := 't' | 'f' | '!(' expr ')' | ('&'|'|') '(' expr (',' expr)* ')'`, so the recursion
  collects a *list* of sub-results and folds them with `and` / `or`.
- **LC 736 (Parse Lisp Expression)** — same template plus a **scope stack**: `let` binds
  variables, so each recursive call carries (or pushes/pops) an environment map.
- **LC 770 (Basic Calculator IV)** — same template but each sub-result is a *polynomial*
  (map from sorted variable-tuple → coefficient) instead of an int.

**Common LeetCode Problems**
- LC 394: Decode String
- LC 224: Basic Calculator
- LC 1106: Parsing A Boolean Expression
- LC 736: Parse Lisp Expression
- LC 770: Basic Calculator IV

---

### 5-3) Halving Recursion (Fast Exponentiation) — LC 50 ⭐⭐⭐⭐

**Definition**: When the parameter shrinks by a **factor** (usually /2) rather than by 1,
recursion depth drops from O(n) to O(log n). The canonical case is binary exponentiation:

```text
x^n = (x^(n/2))^2            if n is even
x^n = (x^(n/2))^2 * x        if n is odd
x^0 = 1                      base case
```

**Time**: O(log n). **Space**: O(log n) call stack.

**Key Insight**: Compute `half` **once** and square it. Writing
`fastPow(x, n/2) * fastPow(x, n/2)` looks identical but re-expands the tree to O(n).

**Example — LC 50: Pow(x, n)**

```java
// java
// LC 50 - Pow(x, n)
// IDEA: binary exponentiation - halve the exponent every call, square the result
// time = O(log n), space = O(log n)
public double myPow(double x, int n) {
    long N = n;                     // widen: -Integer.MIN_VALUE overflows an int
    if (N < 0) {
        x = 1 / x;
        N = -N;
    }
    return fastPow(x, N);
}

private double fastPow(double x, long n) {
    if (n == 0) return 1.0;
    double half = fastPow(x, n / 2);   // compute ONCE
    return (n % 2 == 0) ? half * half : half * half * x;
}
```

```python
# python
# LC 50 - Pow(x, n)
# IDEA: binary exponentiation - halve the exponent every call, square the result
# time = O(log n), space = O(log n)
def myPow(x, n):
    if n < 0:
        x, n = 1 / x, -n

    def fast(x, n):
        if n == 0:
            return 1.0
        half = fast(x, n // 2)          # compute ONCE
        return half * half if n % 2 == 0 else half * half * x

    return fast(x, n)
```

**Variations**

- **LC 1922 (Count Good Numbers)** — twist: same recursion under a **modulus**, and `n` can
  be 10^15, so O(log n) is mandatory. Even indices have 5 choices (0,2,4,6,8), odd indices
  have 4 (primes 2,3,5,7) → `5^ceil(n/2) * 4^floor(n/2) mod 1e9+7`.

```java
// java
// LC 1922 - Count Good Numbers
// IDEA: modular fast power; ceil(n/2) even slots x 5 choices, floor(n/2) odd slots x 4
// time = O(log n), space = O(log n)
private static final int MOD = 1_000_000_007;

public int countGoodNumbers(long n) {
    return (int) (powMod(5, (n + 1) / 2) * powMod(4, n / 2) % MOD);
}

private long powMod(long b, long e) {
    if (e == 0) return 1;
    long half = powMod(b, e / 2);
    long sq = half * half % MOD;
    return (e % 2 == 0) ? sq : sq * b % MOD;
}
```

```python
# python
# LC 1922 - Count Good Numbers
# IDEA: modular fast power; ceil(n/2) even slots x 5 choices, floor(n/2) odd slots x 4
# time = O(log n), space = O(log n)
def countGoodNumbers(n):
    MOD = 10 ** 9 + 7

    def pow_mod(b, e):
        if e == 0:
            return 1
        half = pow_mod(b, e // 2)
        sq = half * half % MOD
        return sq if e % 2 == 0 else sq * b % MOD

    return pow_mod(5, (n + 1) // 2) * pow_mod(4, n // 2) % MOD
```

- **LC 231 / 326 / 342 (Power of Two / Three / Four)** — twist: the *inverse* direction —
  divide `n` down to 1 instead of building it up. One template covers all three (change the
  base); guard `n < 1` or the recursion never terminates.

```java
// java
// LC 326 - Power of Three (same shape for LC 231 base 2, LC 342 base 4)
// IDEA: peel one factor per call; n is a power of b iff it divides down to exactly 1
// time = O(log n), space = O(log n)
public boolean isPowerOfThree(int n) {
    if (n < 1) return false;   // 0 and negatives are never powers
    if (n == 1) return true;   // 3^0
    return n % 3 == 0 && isPowerOfThree(n / 3);
}
```

```python
# python
# LC 326 - Power of Three (same shape for LC 231 base 2, LC 342 base 4)
# IDEA: peel one factor per call; n is a power of b iff it divides down to exactly 1
# time = O(log n), space = O(log n)
def isPowerOfThree(n):
    if n < 1:
        return False
    if n == 1:
        return True
    return n % 3 == 0 and isPowerOfThree(n // 3)
```

**Common LeetCode Problems**
- LC 50: Pow(x, n)
- LC 1922: Count Good Numbers (modular fast power)
- LC 231: Power of Two
- LC 326: Power of Three
- LC 342: Power of Four

---

### 5-4) Recurrence Reduction (No Tree, No Search) — LC 779 / 1823 ⭐⭐⭐⭐

**Definition**: Some problems have **no data structure to traverse at all**. The whole
solution is a one-line recurrence relating `f(n)` to `f(n-1)` (or `f(n/2)`, `f(n/1000)`, …).
The interview skill is *deriving* the relation; the code is then 3 lines.

**How to derive**:
```text
1. Write out the answer for n = 1, 2, 3, 4 by hand.
2. Ask: "given the answer for n-1, what single operation produces the answer for n?"
   - index mapping   -> which position in row n-1 does position k in row n come from?
   - shift/rotation  -> after one round, what does the smaller problem's answer become?
3. Base case = the smallest n you can answer without thinking.
```

**Time**: O(depth of the recurrence). **Space**: O(depth) call stack (tail-recursive shapes
convert to an O(1) loop trivially).

**Example — LC 779: K-th Symbol in Grammar**

Row `n` is row `n-1` with every `0` → `01` and every `1` → `10`. So position `k` in row `n`
descends from position `(k+1)/2` in row `n-1`: **odd `k` copies the parent, even `k` flips it.**

```java
// java
// LC 779 - K-th Symbol in Grammar
// IDEA: position k of row n comes from position (k+1)/2 of row n-1; even k flips the bit
// time = O(n), space = O(n)
public int kthGrammar(int n, int k) {
    if (n == 1) return 0;                            // row 1 is just "0"
    int parent = kthGrammar(n - 1, (k + 1) / 2);
    return (k % 2 == 1) ? parent : 1 - parent;       // odd = copy, even = flip
}
```

```python
# python
# LC 779 - K-th Symbol in Grammar
# IDEA: position k of row n comes from position (k+1)//2 of row n-1; even k flips the bit
# time = O(n), space = O(n)
def kthGrammar(n, k):
    if n == 1:
        return 0
    parent = kthGrammar(n - 1, (k + 1) // 2)
    return parent if k % 2 == 1 else 1 - parent
```

**Example — LC 1823: Find the Winner of the Circular Game** (the Josephus recurrence)

After the first elimination, `n-1` people remain and the counting restarts `k` positions
along — so the smaller problem's answer just shifts by `k` (mod `n`).

```java
// java
// LC 1823 - Find the Winner of the Circular Game
// IDEA: Josephus recurrence (0-indexed): f(1) = 0, f(n) = (f(n-1) + k) % n
// time = O(n), space = O(n)
public int findTheWinner(int n, int k) {
    return winner(n, k) + 1;              // convert 0-indexed seat to 1-indexed
}

private int winner(int n, int k) {
    if (n == 1) return 0;
    return (winner(n - 1, k) + k) % n;    // shift the smaller answer by k
}
```

```python
# python
# LC 1823 - Find the Winner of the Circular Game
# IDEA: Josephus recurrence (0-indexed): f(1) = 0, f(n) = (f(n-1) + k) % n
# time = O(n), space = O(n)
def findTheWinner(n, k):
    def winner(m):
        if m == 1:
            return 0
        return (winner(m - 1) + k) % m
    return winner(n) + 1
```

**Variation — decompose by scale, not by 1: LC 273 (Integer to English Words)**

Twist: the recurrence peels off the **largest magnitude unit** (Billion/Million/Thousand/
Hundred) and recurses on the remainder, so the depth is O(log10 n) rather than O(n).

```java
// java
// LC 273 - Integer to English Words
// IDEA: split off the biggest unit, name it, recurse on the remainder; helper always
//       returns a space-terminated chunk so concatenation is uniform
// time = O(log10(n)), space = O(log10(n))
private static final String[] BELOW_20 = {"", "One", "Two", "Three", "Four", "Five", "Six",
        "Seven", "Eight", "Nine", "Ten", "Eleven", "Twelve", "Thirteen", "Fourteen",
        "Fifteen", "Sixteen", "Seventeen", "Eighteen", "Nineteen"};
private static final String[] TENS = {"", "Ten", "Twenty", "Thirty", "Forty", "Fifty",
        "Sixty", "Seventy", "Eighty", "Ninety"};

public String numberToWords(int num) {
    if (num == 0) return "Zero";          // only place "Zero" is ever printed
    return words(num).trim();
}

private String words(int num) {
    if (num == 0)         return "";
    if (num < 20)         return BELOW_20[num] + " ";
    if (num < 100)        return TENS[num / 10] + " " + words(num % 10);
    if (num < 1000)       return BELOW_20[num / 100] + " Hundred " + words(num % 100);
    if (num < 1000000)    return words(num / 1000) + "Thousand " + words(num % 1000);
    if (num < 1000000000) return words(num / 1000000) + "Million " + words(num % 1000000);
    return words(num / 1000000000) + "Billion " + words(num % 1000000000);
}
```

```python
# python
# LC 273 - Integer to English Words
# IDEA: split off the biggest unit, name it, recurse on the remainder; helper always
#       returns a space-terminated chunk so concatenation is uniform
# time = O(log10(n)), space = O(log10(n))
BELOW_20 = ["", "One", "Two", "Three", "Four", "Five", "Six", "Seven", "Eight", "Nine",
            "Ten", "Eleven", "Twelve", "Thirteen", "Fourteen", "Fifteen", "Sixteen",
            "Seventeen", "Eighteen", "Nineteen"]
TENS = ["", "Ten", "Twenty", "Thirty", "Forty", "Fifty", "Sixty", "Seventy", "Eighty",
        "Ninety"]

def numberToWords(num):
    if num == 0:
        return "Zero"

    def words(n):
        if n == 0:
            return ""
        if n < 20:
            return BELOW_20[n] + " "
        if n < 100:
            return TENS[n // 10] + " " + words(n % 10)
        if n < 1000:
            return BELOW_20[n // 100] + " Hundred " + words(n % 100)
        if n < 10 ** 6:
            return words(n // 1000) + "Thousand " + words(n % 1000)
        if n < 10 ** 9:
            return words(n // 10 ** 6) + "Million " + words(n % 10 ** 6)
        return words(n // 10 ** 9) + "Billion " + words(n % 10 ** 9)

    return words(num).strip()
```

**Common LeetCode Problems**
- LC 779: K-th Symbol in Grammar (index mapping)
- LC 1823: Find the Winner of the Circular Game (Josephus)
- LC 390: Elimination Game (recurse on the reversed half-size problem)
- LC 273: Integer to English Words (decompose by magnitude)
- LC 233: Number of Digit One (digit-by-digit recurrence)
- LC 509: Fibonacci Number (the textbook recurrence — memoize it)

---

### 5-5) Other Recursion-Tagged Classics

Problems that fit patterns already covered above — listed for completeness:

| LC | Problem | Fits |
|----|---------|------|
| 10 | Regular Expression Matching | Top-down recursion on `(i, j)` + memo — see `recursion_to_dp.md` |
| 44 | Wildcard Matching | Same as LC 10, `*` matches a run instead of "prev char, 0+ times" |
| 486 | Predict the Winner | Minimax recursion on `(l, r)` + memo — see `recursion_to_dp.md` |
| 60 | Permutation Sequence | Factorial number system: pick each digit by `k / (n-1)!`, recurse on the rest |