# BST — Advanced Patterns & Deep Dives

> **Scope** — The BST material a first pass should skip: order-statistic (rank) queries, the lazy O(h)-space iterator, in-order drop detection for a corrupted BST, the full construction-variant catalogue, and the detach/bounds variations that go beyond the canonical delete and validate templates.
> **See also**: [bst.md](./bst.md) — the canonical templates every section here extends; [bst_examples.md](./bst_examples.md) — the worked LC solution archive; [segment_tree.md](./segment_tree.md) — range queries over an array instead of a tree; [binary_indexed_tree.md](./binary_indexed_tree.md) — prefix-sum ranks without a tree of nodes.

## LeetCode Problem Lists

- [Binary Search Tree](https://leetcode.com/problem-list/binary-search-tree/)
- [Binary Tree](https://leetcode.com/problem-list/binary-tree/)
- [Design](https://leetcode.com/problem-list/design/)

## Overview

Six template groups, each one a deep dive on a BST idea that
[bst.md](./bst.md) states in a line or two. Read `bst.md` first: every section here assumes
you can already write the canonical search / insert / delete / validate / inorder /
construct forms from memory.

### Key Properties
- **Complexity**: O(h) per operation for the rank and iterator templates — O(log n) on a
  balanced tree, O(n) on a skewed one; O(n) for the whole-tree walks (Templates 8, 6b)
- **Core Idea**: each group augments one canonical template — with a subtree-size field, with
  an explicit stack, with a `prev` pointer, or with a different input format
- **When to Use**: interview follow-ups — "what if the data keeps changing?", "can you do it
  in O(1) space?", "what if the input is a linked list instead?"

## Templates & Algorithms

| Template | Extends | Question it answers | Core LC |
|---|---|---|---|
| **5b** — lazy iterator | Template 5 (inorder) | stream sorted values in O(h) space | 173, 1305 |
| **8** — recover a broken BST | Template 5 (inorder) | which two nodes were swapped? | 99 |
| **9** — order-statistic tree | Templates 1 + 2 | kth largest / rank, under insertions | 703 |
| **6b** — construction variants | Template 6 | build from a list / preorder / an unbalanced BST | 109, 1008, 1382, 95, 96 |
| **3c** — detach variations | Template 3 (delete) | delete into a *forest*; value-swap delete | 1110, 450 |
| **4b** — bounds propagation | Template 4 (validate) | *measure* with the bounds instead of rejecting | 1026 |

### Template 5b: BST Lazy Traversal (Iterator Pattern)

#### **Pattern Overview**
- **Description**: Simulate inorder traversal on-demand using a stack — only traverse as far as needed, not the whole tree upfront
- **Recognition**: "BST Iterator", "next smallest", "streaming traversal", large dataset where loading all nodes is expensive
- **Key Insight**: Push only the left spine into the stack; when popping a node, push its right subtree's left spine
- **Time Complexity**: O(1) amortized per `next()` call, O(h) space where h = tree height
- **Space Complexity**: O(h) — far better than O(n) from pre-collecting all nodes

#### **Core Idea**

```text
Pre-collect ALL nodes (eager):              Lazy traversal:
  O(n) space, O(n) init time                 O(h) space, O(1) amortized per call

  [1, 3, 5, 7, 9, ...]   ← full list        Stack: only current left spine
  load everything first                      push more only when needed
```

**Three-step pattern:**
```text
1. INIT:    Push entire left spine from root into stack
             (leftmost path = smallest values on top)

2. next():  Pop top node (= current smallest)
             → if it has a right child, push that subtree's left spine
             → return popped node's value

3. hasNext(): stack is non-empty
```

**Visual walkthrough:**
```text
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

#### **Variation: Merge Two Lazy Iterators (LC 1305)**

> **Twist**: run **two independent left-spine stacks side by side** and always pop from
> whichever stack's top is smaller — i.e. the merge step of merge-sort, but over two
> BSTs instead of two arrays. This beats the naive "flatten both to lists, then merge"
> on space: `O(h1 + h2)` extra instead of `O(m + n)`.

```java
// java
// LC 1305 - All Elements in Two Binary Search Trees
// IDEA: TWO LAZY IN-ORDER ITERATORS + merge step
// time = O(m + n), space = O(h1 + h2) excluding the output list
public List<Integer> getAllElements(TreeNode root1, TreeNode root2) {
    Deque<TreeNode> s1 = new ArrayDeque<>(), s2 = new ArrayDeque<>();
    List<Integer> res = new ArrayList<>();
    pushLeft(s1, root1);
    pushLeft(s2, root2);

    while (!s1.isEmpty() || !s2.isEmpty()) {
        // take from whichever iterator currently exposes the smaller value
        Deque<TreeNode> pick;
        if (s2.isEmpty() || (!s1.isEmpty() && s1.peek().val <= s2.peek().val)) {
            pick = s1;
        } else {
            pick = s2;
        }
        TreeNode node = pick.pop();
        pushLeft(pick, node.right);   // lazily expand only the stack we consumed
        res.add(node.val);
    }
    return res;
}

private void pushLeft(Deque<TreeNode> st, TreeNode node) {
    while (node != null) { st.push(node); node = node.left; }
}
```

```python
# python
# LC 1305 - All Elements in Two Binary Search Trees
# IDEA: TWO LAZY IN-ORDER ITERATORS + merge step
# time = O(m + n), space = O(h1 + h2) excluding the output list
class Solution(object):
    def getAllElements(self, root1, root2):
        st1, st2, res = [], [], []

        def push_left(st, node):
            while node:
                st.append(node)
                node = node.left

        push_left(st1, root1)
        push_left(st2, root2)

        while st1 or st2:
            # pop from the iterator exposing the smaller current value
            if not st2 or (st1 and st1[-1].val <= st2[-1].val):
                node = st1.pop()
                push_left(st1, node.right)
            else:
                node = st2.pop()
                push_left(st2, node.right)
            res.append(node.val)

        return res
```

**🚫 Common Mistake**: `if st1[-1].val <= st2[-1].val` without first checking that both
stacks are non-empty — one tree is exhausted long before the other. Guard the empty
stack *first*, then compare.

#### **Key Takeaways**
```text
1. Push LEFT SPINE only — this gives smallest-first access
2. On pop: expand right subtree's left spine lazily
3. Stack depth = O(h), not O(n) — critical for tall/large trees
4. Amortized O(1) per next(): each node is pushed and popped exactly once
5. Enables partial traversal — stop early without wasting work
```

### Template 8: Recover/Fix BST Problems ⭐⭐⭐⭐⭐

#### **Pattern Overview**
- **Description**: Detect and fix violations in BST by leveraging in-order traversal property
- **Recognition**: "Recover", "fix", "swapped nodes", "invalid BST"
- **Key Insight**: **In-order traversal of valid BST = strictly increasing sequence**
- **Time Complexity**: O(n)
- **Space Complexity**: O(h) for recursion, O(1) with Morris traversal

#### **Core Idea**

**Don't look at the tree — look at the in-order sequence it prints.**

A valid BST's in-order walk is strictly increasing. Swapping exactly two node
*values* corrupts that sequence in a way that is fully characterised by the
**drops** (positions where `prev.val > cur.val`). So the whole problem reduces to:

```text
1. Walk in-order, keeping ONE extra pointer: `prev` (the previously visited node)
2. Every time prev.val > cur.val  -> that's a DROP
      first  = prev   (only on the FIRST drop)   <- the "too large" node
      second = cur    (on EVERY drop)            <- the "too small" node
3. Swap first.val <-> second.val
```

There is no need to store the sequence — `prev` is the only state the detection
needs, which is exactly why this fits inside a plain recursive in-order walk
(and, later, inside Morris traversal for O(1) space).



```text
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

```text
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

> **One rule covers both cases**: `if (first == null) first = prev; second = cur;`
> With 1 drop it assigns `(prev, cur)` of that drop; with 2 drops it keeps `prev`
> of the first and overwrites `second` with `cur` of the last. No case-split needed.

#### **Pattern**

```python
# python
# LC 99 - Recover Binary Search Tree
# IDEA: IN-ORDER DFS + BST PROPERTY (in-order of a valid BST is strictly increasing)
# time = O(n), space = O(h) recursion stack
class Solution(object):
    def recoverTree(self, root):
        self.first = None    # the "too large" node  (prev at the 1st drop)
        self.second = None   # the "too small" node  (cur  at the last drop)
        self.prev = None     # previously visited node in in-order order

        self.helper(root)

        # NOTE !!! swap VALUES, never rewire the nodes
        if self.first and self.second:
            self.first.val, self.second.val = self.second.val, self.first.val

    def helper(self, node):
        if not node:
            return

        # 1) left
        self.helper(node.left)

        # 2) current : compare against the previous in-order node
        #    NOTE !!! `self.prev` guard — prev is None only for the leftmost node
        if self.prev and self.prev.val > node.val:
            if self.first is None:
                self.first = self.prev   # FIRST drop only
            self.second = node           # EVERY drop (overwrites on the 2nd)

        self.prev = node                 # current becomes previous

        # 3) right
        self.helper(node.right)
```

```java
// java
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
// NOTE: keep prev = null and guard with `prev != null`.
// A `new TreeNode(Integer.MIN_VALUE)` sentinel is NOT safe here — LC 99 allows
// node values down to -2^31, so a real node can equal the sentinel.
private TreeNode prev = null;

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

#### **Follow-up: O(1) Space via Morris Traversal**

The detection logic is *unchanged* — only the traversal mechanism differs. Morris
threads each node's in-order predecessor to point at it, walks, then unthreads.

```python
# python
# LC 99 - Recover BST, O(1) extra space
# IDEA: MORRIS IN-ORDER + the same (first / second / prev) drop detection
# time = O(n) (each edge traversed at most 3x), space = O(1)
class Solution(object):
    def recoverTree(self, root):
        first = second = prev = None
        cur = root

        while cur:
            if not cur.left:
                # no left subtree -> visit cur, then go right
                if prev and prev.val > cur.val:
                    if not first:
                        first = prev
                    second = cur
                prev = cur
                cur = cur.right
            else:
                # find in-order predecessor of cur
                pred = cur.left
                while pred.right and pred.right is not cur:
                    pred = pred.right

                if not pred.right:
                    pred.right = cur      # thread it, descend left
                    cur = cur.left
                else:
                    pred.right = None     # NOTE !!! un-thread (restore structure)
                    if prev and prev.val > cur.val:
                        if not first:
                            first = prev
                        second = cur
                    prev = cur
                    cur = cur.right

        if first and second:
            first.val, second.val = second.val, first.val
```

> Morris temporarily **mutates** the tree (right pointers become threads). Every
> thread is removed before the walk ends, so the final structure is intact — but
> the tree is not safe to read concurrently mid-traversal.

#### **Why This Pattern Works**

```text
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

#### **Similar LeetCode Problems**

All of these are the **same skeleton**: an in-order walk carrying a `prev` pointer.
Only the "process current node" step changes.

| Problem | LC # | Difficulty | What the `prev` step does | Why In-Order? |
|---------|------|------------|---------------------------|---------------|
| **Recover BST** | 99 | Medium | Record drops (`prev.val > cur.val`) | Find swapped nodes in sorted sequence |
| **Validate BST** | 98 | Medium | Reject on any drop | Verify strictly increasing sequence |
| **Min Diff in BST** | 530 / 783 | Easy | `ans = min(ans, cur.val - prev.val)` | Closest pair is adjacent when sorted |
| **Find Mode in BST** | 501 | Easy | Count run length of equal values | Duplicates are contiguous when sorted |
| **Kth Smallest** | 230 | Medium | Decrement a counter, stop at 0 | In-order gives sorted order |
| **Inorder Successor in BST** | 285 | Medium | Return `cur` once `prev == target` | Successor = next in-order node |
| **BST Iterator** | 173 | Medium | Pause/resume via explicit stack | Simulate in-order traversal lazily |
| **Convert BST to Sorted DLL** | 426 | Medium | Link `prev.right = cur; cur.left = prev` | Sorted order = list order |
| **Increasing Order Search Tree** | 897 | Easy | Re-hang each node on `prev.right` | Flatten into a right-only chain |
| **Two Sum IV** | 653 | Easy | Two pointers over the sorted list | In-order gives sorted array |
| **Convert to Greater Tree** | 538 / 1038 | Medium | **Reverse** in-order + running sum | Process in descending order |

**Recognition trigger** — whenever a problem is about *the ordering relationship
between neighbouring values* in a BST (successor, min gap, mode, a swap that broke
the order), reach for in-order + `prev` before anything else.

#### **Common Mistakes**

**🚫 Mistake 1: Trying to swap nodes instead of values**

```text
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

**🚫 Mistake 3: Using a sentinel `prev` instead of a null guard**
```java
// BAD: LC 99 allows node values down to -2^31, so a real node can EQUAL
//      the sentinel — and a float('-inf') sentinel in Python isn't a TreeNode.
TreeNode prev = new TreeNode(Integer.MIN_VALUE);

// GOOD: prev = null, and guard the comparison. null happens exactly once
//       (the leftmost node), where there is nothing to compare against anyway.
TreeNode prev = null;
...
if (prev != null && prev.val > root.val) { ... }
```

**🚫 Mistake 4: Resetting `first` on the second drop**
```python
# BAD: overwrites the correct answer on a distant swap
if prev.val > node.val:
    first = prev      # gets clobbered at the 2nd drop -> wrong pair
    second = node

# GOOD: first is write-once, second is write-always
if prev.val > node.val:
    if first is None:
        first = prev
    second = node
```

#### **Complexity Analysis**
- **Time**: O(N) — visit each node exactly once
- **Space**: O(H) — recursion stack (H = tree height)
- **Follow-up O(1) Space**: Use Morris Traversal (modifies tree temporarily)

#### **Key Takeaways**

```text
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

### Template 9: BST as an Ordered Set / Order-Statistic Tree ⭐⭐⭐⭐⭐

#### **Pattern Overview**
- **Description**: Use a BST as a **live, ordered multiset** — values stream in one at a
  time and you must answer *rank / order* questions (kth largest, kth smallest,
  "how many are less than x") after every insertion.
- **Recognition**: "in a stream", "after each insert", "kth largest so far", "running
  median", "count of smaller elements", any time you'd reach for a `TreeMap` /
  `SortedList` in a language that has one.
- **Key Insight**: **Augment every node with `count` = the size of its subtree.** That one
  extra field turns the BST into an *order-statistic tree*: a rank query becomes a single
  root-to-leaf descent, exactly like a search.
- **Time**: O(h) per insert and per rank query — O(log n) on a balanced tree, O(n) worst case
- **Space**: O(n) for the tree

#### **Core Idea**

```text
Plain BST answers "is x present?".
Augmented BST answers "what is the kth largest?" — because each node knows
how many values live beneath it.

           node
          /    \
      left      right
    (a nodes) (b nodes)

  The b values in `right` are ALL larger than node.val.
  So, ranking from the largest downwards:

    ranks 1 .. b        → live in the right subtree
    rank  b + 1         → is node itself
    ranks b + 2 ..      → live in the left subtree

  kthLargest(node, k):
      b = size(node.right)
      if k <= b       → recurse right, k unchanged
      if k == b + 1   → return node.val
      else            → recurse left with k - (b + 1)
                         (we just skipped the right subtree AND node)
```

#### **Visual Trace** — `k = 3`, stream `[4, 5, 8, 2]` then `add(3)`

```text
BST after [4,5,8,2] then inserting 3   (count in parentheses)

              4(5)
             /    \
          2(2)     5(2)
             \        \
             3(1)      8(1)

kthLargest(root=4, k=3):
  b = size(node.right = 5) = 2      → subtree {5, 8} holds the 1st and 2nd largest
  k = 3 == b + 1                    → node 4 IS the 3rd largest → return 4   ✓
  (sorted values {2,3,4,5,8}; 3rd largest = 4)

add(5) next → tree grows to {2,3,4,5,5,8}, kthLargest(root, 3) = 5
```

#### **Java Implementation**
```java
// java
// LC 703 - Kth Largest Element in a Stream
// IDEA: ORDER-STATISTIC BST — augment each node with subtree size, then rank-descend
// time = O(h) per add (O(log n) balanced, O(n) skewed), space = O(n)
class KthLargest {

    private static class Node {
        int val;
        int count = 1;          // number of nodes in the subtree rooted here
        Node left, right;
        Node(int v) { val = v; }
    }

    private final int k;
    private Node root;

    public KthLargest(int k, int[] nums) {
        this.k = k;
        for (int v : nums) root = insert(root, v);
    }

    private Node insert(Node node, int val) {
        if (node == null) return new Node(val);
        node.count++;                                    // one more node below/at this root
        if (val < node.val) node.left = insert(node.left, val);
        else node.right = insert(node.right, val);       // duplicates go right (multiset)
        return node;
    }

    private int size(Node node) { return node == null ? 0 : node.count; }

    private int kthLargest(Node node, int k) {
        int rightSize = size(node.right);                // count of values > node.val
        if (k <= rightSize) return kthLargest(node.right, k);
        if (k == rightSize + 1) return node.val;         // node itself is the answer
        return kthLargest(node.left, k - rightSize - 1); // skipped right subtree + node
    }

    public int add(int val) {
        root = insert(root, val);
        return kthLargest(root, k);
    }
}
```

#### **Python Implementation**
```python
# python
# LC 703 - Kth Largest Element in a Stream
# IDEA: ORDER-STATISTIC BST — augment each node with subtree size, then rank-descend
# time = O(h) per add (O(log n) balanced, O(n) skewed), space = O(n)
class CountNode(object):
    def __init__(self, val):
        self.val = val
        self.left = None
        self.right = None
        self.count = 1          # size of the subtree rooted here


class KthLargest(object):
    def __init__(self, k, nums):
        self.k = k
        self.root = None
        for v in nums:
            self.root = self._insert(self.root, v)

    def _insert(self, node, val):
        if not node:
            return CountNode(val)
        node.count += 1                                   # subtree grew by 1
        if val < node.val:
            node.left = self._insert(node.left, val)
        else:
            node.right = self._insert(node.right, val)    # duplicates go right
        return node

    def _size(self, node):
        return node.count if node else 0

    def _kth_largest(self, node, k):
        right_size = self._size(node.right)               # values greater than node.val
        if k <= right_size:
            return self._kth_largest(node.right, k)
        if k == right_size + 1:
            return node.val                               # node itself
        return self._kth_largest(node.left, k - right_size - 1)

    def add(self, val):
        self.root = self._insert(self.root, val)
        return self._kth_largest(self.root, self.k)
```

#### **BST vs Min-Heap for LC 703**

| Approach | add() Time | Space | Can answer arbitrary rank? | Notes |
|----------|-----------|-------|----------------------------|-------|
| **Min-heap of size k** | O(log k) | O(k) | ❌ only the kth largest | Shortest interview answer; say this first |
| **Order-statistic BST** | O(log n) avg / O(n) worst | O(n) | ✅ any k, kth smallest, rank of x | Generalizes; needed for follow-ups |
| **Sorted list + binary insert** | O(n) shift | O(n) | ✅ | Search is O(log n) but insert shifts memory |

> **Interview move**: give the heap solution for the literal problem, then say
> *"if k can change per query, or you also need 'how many are below x', I'd augment a
> BST with subtree counts — that's an order-statistic tree, O(log n) for any rank."*
> Add: *"in production I'd use a balanced BST (`TreeMap` / Red-Black) so the O(n)
> skewed worst case can't happen."*

#### **The `count` Field Unlocks More Than kth-Largest**

```text
With size-augmented nodes, all of these are O(h) descents:

  kthLargest(k)        → shown above
  kthSmallest(k)       → mirror it: use size(node.left)
  rank(x)              → count of values < x: add size(left)+1 each time you go right
  countInRange(lo,hi)  → rank(hi) - rank(lo)
  median              → kthSmallest(n/2) using size(root)

  ⚠️ The ONLY maintenance cost: every insert/delete must fix `count`
     on every node along the path. Forgetting this silently corrupts all ranks.
```

#### **Similar LeetCode Problems**
| Problem | LC # | Difficulty | Relation |
|---------|------|------------|----------|
| Kth Largest Element in a Stream | 703 | Easy | Core problem — dynamic rank query |
| Kth Smallest Element in a BST | 230 | Medium | **Static** version; the classic follow-up is *"what if the BST is modified often?"* → answer is this template |
| BST Iterator | 173 | Medium | Streaming in-order, but positional not rank-based |
| Insert into a BST | 701 | Medium | The insert half of this template, without `count` |

#### **Key Takeaways**
```text
1. A bare BST answers membership; a SIZE-AUGMENTED BST answers RANK
2. kth largest = descend using size(right): k<=b → right, k==b+1 → node, else left with k-b-1
3. Bump `count` on the way DOWN during insert — every node on the path
4. Duplicates: send them consistently to one side (right here) → BST becomes a multiset
5. State the O(n) skewed worst case and name TreeMap / Red-Black as the fix

This is the standard answer to "what if the data keeps changing?" follow-ups
on LC 230 and any kth-element question.
```

### Template 6b: BST Construction Variants

Everything `Template 6` in [bst.md](./bst.md) does not keep: the non-array inputs, the
rebuild-an-existing-BST case, and the enumerate/count pair.

#### **Core Construction Patterns (continued)**

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

##### **Pattern 6.4: Balance a BST** (LC 1382) ⭐⭐⭐⭐⭐

**a. Core Idea**

> **Don't rebalance the tree — flatten it and rebuild it.** LC 1382 = **LC 94 (in-order) + LC 108 (sorted array → BST)** glued together.

The whole pattern rests on the **BST ⟷ sorted array duality**:

```text
   in-order DFS                      mid-as-root recursion
  ────────────────►                  ─────────────────────►
  BST  ──────────► sorted array ───► balanced BST
  (any shape)      [1,2,3,4]         (height = ceil(log2(n+1)))
   any BST flattens to               any sorted array rebuilds
   the SAME sorted order             to a BALANCED BST
```

**Two questions, two answers — that's the entire problem:**

| Question | Answer | Why |
|----------|--------|-----|
| Which traversal? | **in-order** (left → root → right) | in-order is the *only* traversal that yields **sorted** output for a BST — and we need sorted input to rebuild a BST |
| Which element becomes the root? | the **middle** one | mid splits the range into two halves differing by ≤ 1 → the size balance holds at *every* level → depth condition satisfied recursively |

**Why "mid as root" gives a balanced tree (the key argument):**

```text
build(l, r) puts nodes[mid] on top, with
   left  subtree = build(l, mid-1)   -> size = mid - l
   right subtree = build(mid+1, r)   -> size = r - mid
   |left size - right size| <= 1     at EVERY node
=> heights differ by at most 1       at EVERY node   ← exactly the problem's definition
=> total height = floor(log2(n)) + 1 = ceil(log2(n+1))
```

*(verified: n=7 → h=3, n=15 → h=4, n=100 → h=7, n=10000 → h=14)*

**Values vs nodes — two legal flavors:**

| Flavor | Collect | Rebuild | Note |
|--------|---------|---------|------|
| **Collect values** | `arr.append(node.val)` | `TreeNode(arr[mid])` | allocates n new nodes; old tree untouched. Simplest to reason about. |
| **Collect nodes** (reuse) | `nodes.append(node)` | `root = nodes[mid]` | no allocation. **Safe only because you assign BOTH `.left` and `.right` on every reused node** — every stale pointer gets overwritten. |

> ⚠️ If you reuse nodes but *conditionally* assign children (e.g. only set `.left` when the range is non-empty), stale pointers from the old tree survive and you get a cycle or a duplicated subtree. Always assign both — `build()` returning `None` is what clears them.

**b. Pattern**

```python
# python
# LC 1382 - Balance a Binary Search Tree
# IDEA: IN-ORDER DFS (BST -> sorted array) + mid-as-root rebuild (LC 108)
# time  = O(n)  -- n for the traversal, n for the rebuild
# space = O(n)  -- the array; + O(h_in) recursion for in-order, O(log n) for the build
class Solution(object):
    def balanceBST(self, root):
        if not root:
            return None

        self.arr = []
        self.in_order(root)                              # step 1: BST -> sorted
        return self.build(0, len(self.arr) - 1)          # step 2: sorted -> balanced BST

    # NOTE !!! in-order, because ONLY in-order gives sorted output for a BST
    def in_order(self, node):
        if not node:
            return
        self.in_order(node.left)
        self.arr.append(node.val)
        self.in_order(node.right)

    # NOTE !!! index bounds, NOT slicing -> avoids O(n log n) copying
    def build(self, l, r):
        # base case: empty range -> None (this also CLEARS stale child pointers)
        if l > r:
            return None

        mid = l + (r - l) // 2                           # middle becomes root
        node = TreeNode(self.arr[mid])
        node.left = self.build(l, mid - 1)               # left half
        node.right = self.build(mid + 1, r)              # right half
        return node
```

```python
# python
# LC 1382 - node-reuse variant (no new allocations)
# time = O(n), space = O(n) for the node list
class Solution(object):
    def balanceBST(self, root):
        nodes = []

        def in_order(node):
            if not node:
                return
            in_order(node.left)
            nodes.append(node)                # collect NODES, not values
            in_order(node.right)

        def build(l, r):
            if l > r:
                return None
            mid = l + (r - l) // 2
            node = nodes[mid]
            node.left = build(l, mid - 1)      # MUST assign both children,
            node.right = build(mid + 1, r)     # else old pointers linger
            return node

        in_order(root)
        return build(0, len(nodes) - 1)
```

```java
// java
// LC 1382 - Balance a Binary Search Tree
// IDEA: in-order into a list, then rebuild with mid-as-root
// time = O(n), space = O(n)
public TreeNode balanceBST(TreeNode root) {
    List<Integer> sorted = new ArrayList<>();
    inorder(root, sorted);
    return build(sorted, 0, sorted.size() - 1);
}

private void inorder(TreeNode node, List<Integer> out) {
    if (node == null) return;
    inorder(node.left, out);
    out.add(node.val);           // in-order => ascending
    inorder(node.right, out);
}

private TreeNode build(List<Integer> a, int l, int r) {
    if (l > r) return null;
    int mid = l + (r - l) / 2;   // NOTE: not (l + r) / 2 -> avoids int overflow
    TreeNode node = new TreeNode(a.get(mid));
    node.left  = build(a, l, mid - 1);
    node.right = build(a, mid + 1, r);
    return node;
}
```

**Visual trace** — `root = [1,null,2,null,3,null,4]` (fully right-skewed, h = 4):

```text
 step 1: in-order          step 2: build(0, 3), mid = 0 + (3-0)//2 = 1
   1                          arr = [1, 2, 3, 4]
    \                                    ^ mid
     2       in-order                            2                h = 3
      \      ────────►  [1, 2, 3, 4]           /   \
       3                                      1     3      build(0,0) | build(2,3)
        \                                            \
         4                                            4     build(3,3), mid=3
```

> **Both mid conventions are accepted** (LC says "return any of them"):
> - `mid = (l + r) // 2` on index bounds → root `2` → `[2,1,3,null,null,null,4]` (matches LC's sample output)
> - `mid = len(arr) // 2` with slicing → root `3` → `[3,2,4,1]`
>
> Both have height 3 and in-order `[1,2,3,4]`. Don't waste interview time worrying which — just say "left-mid or right-mid, both balanced".

**Common Pitfalls**

```python
# ❌ Pre-order / post-order -> NOT sorted -> the rebuilt tree isn't a BST at all
# ❌ Slicing: build(arr[:mid]) + build(arr[mid+1:])  -> O(n log n) copying (works, but wasteful)
# ❌ Reusing nodes with only `if l <= mid-1: node.left = ...`  -> stale pointers survive
# ❌ mid = (l + r) / 2 in Java -> int overflow on huge ranges; use l + (r - l) / 2
# ❌ Recursive in-order on a FULLY SKEWED BST with n = 10^4 (LC 1382's max):
#      Python default recursionlimit is 1000 -> RecursionError.
#      Fix: iterative in-order (stack), or sys.setrecursionlimit(10**5)
```

```python
# ✅ iterative in-order — immune to the skewed-tree recursion blowup
def in_order_iter(root):
    out, stack, cur = [], [], root
    while cur or stack:
        while cur:
            stack.append(cur)
            cur = cur.left
        cur = stack.pop()
        out.append(cur.val)
        cur = cur.right
    return out
```

> Note: the **rebuild** recursion is only `O(log n)` deep by construction, so only the *flatten* step
> is at risk.

**Optional: O(1) space — Day–Stout–Warren (DSW)**

Interview-rare, but it's the answer to "can you do it **in place**?":

```text
1. VINE     : right-rotate away every left child  -> a right-leaning linked list
              (root)-> 1 -> 2 -> 3 -> 4 -> ...
2. COUNT    : walk the vine to get n
3. COMPRESS : m = 2^floor(log2(n+1)) - 1          (size of the largest perfect tree)
              make_rotations(n - m)               (level the excess leaves)
              while m > 1: m //= 2; make_rotations(m)   (halve repeatedly)
```
Each `make_rotations(k)` performs `k` left rotations spaced along the vine, folding it one level at a time. **time = O(n), space = O(1)** — vs O(n) space for the flatten-and-rebuild. See `V2-2` in `leetcode_python/Binary_Search_Tree/balance-a-binary-search-tree.py` (needs `import math`).

**c. Similar LC**

**Direct siblings — "sorted sequence → balanced BST"** (same `build(l, r)` mid-as-root recursion):

| Problem | LC # | Difficulty | Input | Difference from 1382 |
|---------|------|------------|-------|----------------------|
| Balance a Binary Search Tree | **1382** | Medium | unbalanced BST | canonical: needs the in-order flatten step first |
| Convert Sorted Array to BST | **108** | Easy | sorted array | **1382 minus step 1** — the array is handed to you |
| Convert Sorted List to BST | **109** | Medium | sorted linked list | no random access → find mid with slow/fast pointers (O(n log n)), or in-order simulation (O(n)) |
| Construct BST from Preorder Traversal | 1008 | Medium | preorder array | use `(min, max)` bounds instead of mid — see Pattern 6.3 |
| Serialize and Deserialize BST | 449 | Medium | BST → string → BST | round-trip; deserialize is the same bounded rebuild |

**The other half — "BST → sorted sequence"** (step 1 of 1382, reused everywhere):

| Problem | LC # | What it does with the in-order sequence |
|---------|------|------------------------------------------|
| Binary Tree Inorder Traversal | 94 | the raw flatten itself |
| Kth Smallest Element in a BST | 230 | stop at the k-th element (early exit) |
| Validate Binary Search Tree | 98 | check the sequence is strictly increasing |
| Recover Binary Search Tree | 99 | find the 1–2 inversions in the sequence (Template 8) |
| Minimum Distance Between BST Nodes | 783 / 530 | min gap between adjacent elements |
| Convert BST to Sorted Doubly Linked List | 426 | rewire in-order neighbors instead of rebuilding |
| Increasing Order Search Tree | 897 | rebuild as a right-skewed vine — **the exact opposite of 1382** |
| All Elements in Two BSTs | 1305 | merge two in-order streams (Template 5b) |
| Convert BST to Greater Tree | 538 / 1038 | **reverse** in-order + running suffix sum |

**Also worth pairing:**

| Problem | LC # | Relation |
|---------|------|----------|
| Balanced Binary Tree | 110 | *checks* the balance condition that 1382 *produces* |
| Maximum Depth of Binary Tree | 104 | the height measure behind "differs by more than 1" |
| Unique BSTs II / I | 95 / 96 | same mid-pick recursion, but enumerate/count *all* roots instead of only the middle (Pattern 6.5 / 6.6) |

**Key Takeaways**
1. **BST + "sorted" in your head → in-order.** In-order is the bridge in both directions.
2. **"Balanced" → pick the middle.** Mid-as-root makes `|left| - |right| ≤ 1` hold recursively.
3. **1382 = 94 + 108.** Recognize it and you write it in 15 lines.
4. Prefer **index bounds** over slicing, and **assign both children** if you reuse nodes.
5. **O(1) space?** → say "Day–Stout–Warren: vine, then compress".

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

#### **Java Implementation: From Preorder (LC 1008)**
```java
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

### Template 3c: Detach & Delete Variations

`Template 3` in [bst.md](./bst.md) removes one node and returns one root. These two change
what "remove" means: LC 1110 returns a *forest*, and the LC 450 variant below swaps values
instead of relinking.

#### **Variation: Delete Nodes And Return Forest (LC 1110)**

> **Twist**: the *recurse + reconnect via the return value* idiom, but on a **general binary tree** and you must **collect the orphaned roots** instead of returning one root. A node becomes a new forest root exactly when its parent was just deleted — so push that fact down the recursion as an `isRoot` flag.

```java
// java
// LC 1110 - Delete Nodes And Return Forest
// IDEA: DFS + "return null to detach" + pass `isRoot` down
// time = O(n), space = O(h + d)   d = |to_delete|
// NOTE: keep the state LOCAL — instance fields would leak between calls
//       (LeetCode reuses one Solution object for every test case)
public List<TreeNode> delNodes(TreeNode root, int[] to_delete) {
    Set<Integer> toDelete = new HashSet<>();
    for (int v : to_delete) toDelete.add(v);
    List<TreeNode> forest = new ArrayList<>();
    walk(root, true, toDelete, forest);
    return forest;
}

private TreeNode walk(TreeNode node, boolean isRoot, Set<Integer> toDelete, List<TreeNode> forest) {
    if (node == null) return null;
    boolean deleted = toDelete.contains(node.val);

    // a surviving node whose parent vanished starts a new tree
    if (isRoot && !deleted) forest.add(node);

    // children are "roots" iff THIS node is being deleted
    node.left  = walk(node.left,  deleted, toDelete, forest);
    node.right = walk(node.right, deleted, toDelete, forest);

    // returning null is what actually detaches this node from its parent
    return deleted ? null : node;
}
```

```python
# python
# LC 1110 - Delete Nodes And Return Forest
# IDEA: DFS + "return None to detach" + pass `is_root` down
# time = O(n), space = O(h + d)
class Solution(object):
    def delNodes(self, root, to_delete):
        to_del = set(to_delete)
        forest = []

        def dfs(node, is_root):
            if not node:
                return None
            deleted = node.val in to_del

            # surviving node with a deleted parent → new forest root
            if is_root and not deleted:
                forest.append(node)

            # children become roots only if THIS node is deleted
            node.left = dfs(node.left, deleted)
            node.right = dfs(node.right, deleted)

            return None if deleted else node

        dfs(root, True)
        return forest
```

**Why post-order?** You must recurse *before* returning, otherwise you detach a node
before its surviving descendants have been promoted into the forest — the same trap as
returning `None` too early in `trimBST`.

#### **Variation: Value-Swap Delete (LC 450, Python)**

> **Twist**: instead of returning the replacement subtree, swap the target's value with the
> minimum of its right subtree and then recurse into **both** children. Kept beside the
> canonical form because it is the shape most people write first — and because the cost is
> worth seeing: recursing both sides makes it O(n), not O(h).

```python
# python code

# LC 450 Delete Node in a BST
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

### Template 4b: Bounds Propagation Beyond Validation

`Template 4` threads a legal `(min, max)` window down the recursion and **rejects** when a
node steps outside it. Keep the window but change what you do at the boundary and the same
skeleton answers a different question.

#### **Variation: Bounds Propagation to *Measure* Instead of Validate (LC 1026)**

> **Twist**: exactly the same `(min, max)` threaded down the recursion as `isValidBST`,
> but instead of **rejecting** when a node steps outside the bounds you **measure** how far
> it stepped. Carry the running min/max of all ancestors on the current path; the answer is
> the largest `max - min` reached at any leaf. Works on any binary tree — no BST order needed.

```java
// java
// LC 1026 - Maximum Difference Between Node and Ancestor
// IDEA: BOUNDS PROPAGATION (same skeleton as LC 98, but maximize instead of reject)
// time = O(n), space = O(h)
public int maxAncestorDiff(TreeNode root) {
    if (root == null) return 0;
    return dfs(root, root.val, root.val);
}

private int dfs(TreeNode node, int mn, int mx) {
    // at a leaf's child, the widest ancestor spread on this path is mx - mn
    if (node == null) return mx - mn;

    mn = Math.min(mn, node.val);
    mx = Math.max(mx, node.val);

    return Math.max(dfs(node.left, mn, mx), dfs(node.right, mn, mx));
}
```

```python
# python
# LC 1026 - Maximum Difference Between Node and Ancestor
# IDEA: BOUNDS PROPAGATION (same skeleton as LC 98, but maximize instead of reject)
# time = O(n), space = O(h)
class Solution(object):
    def maxAncestorDiff(self, root):
        def dfs(node, mn, mx):
            if not node:
                return mx - mn          # widest spread seen on this path
            mn = min(mn, node.val)
            mx = max(mx, node.val)
            return max(dfs(node.left, mn, mx), dfs(node.right, mn, mx))

        if not root:
            return 0
        return dfs(root, root.val, root.val)
```

```text
Key idea: |a - b| over an ancestor/descendant pair on a path is maximized by
          the path's MIN and MAX — so you never need to compare all pairs,
          only carry two numbers down.

Validate BST (98)          Max ancestor diff (1026)
  bounds = legal window      bounds = running min/max of ancestors
  node outside → return F    node outside → widen the window
  combine with AND           combine with MAX
```

## Summary

| If the follow-up is… | Reach for | Cost |
|---|---|---|
| "what if we keep inserting?" (LC 230 → 703) | Template 9 — augment nodes with a subtree-size `count` | O(h) per insert *and* per rank query |
| "don't materialise the whole traversal" (LC 173) | Template 5b — push only the left spine | O(h) space, O(1) amortised per `next()` |
| "two nodes got swapped, fix it" (LC 99) | Template 8 — in-order + one `prev` pointer, `first` write-once | O(n) / O(h), or O(1) with Morris |
| "can you do it in O(1) space?" (LC 99) | Morris in-order threading | O(n) time, tree mutated then restored |
| "can you do it in O(1) space?" (LC 1382) | Day–Stout–Warren: vine, then compress | O(n) time, O(1) space |
| "the input is a linked list / a preorder dump" | Template 6b — Patterns 6.2 / 6.3 | O(n) with an in-order simulation, or `(min, max)` bounds |
| "delete several nodes at once" (LC 1110) | Template 3c — return `null` to detach, push an `isRoot` flag down | O(n) / O(h + d) |
| "how far apart can an ancestor and a descendant be?" (LC 1026) | Template 4b — carry running min/max, maximise instead of reject | O(n) / O(h) |

**The one idea underneath all of it**: a BST's in-order walk is a sorted sequence, and every
template here is either *augmenting a node* so a descent can answer a rank question, or
*changing how much of that sequence you materialise at once*.

---
**Prerequisites**: the canonical templates in [bst.md](./bst.md).
**Worked archive**: [bst_examples.md](./bst_examples.md).
**Keywords**: order-statistic tree, rank query, subtree size, lazy iterator, Morris traversal, Day–Stout–Warren, Catalan number, forest, bounds propagation