# BST — Advanced Patterns & Deep Dives

> **Scope** — The BST material a first pass should skip: the lazy O(h)-space iterator, in-order drop detection for a corrupted BST, order-statistic (rank) queries, and the construction variants beyond a sorted array — each with when the simpler `bst.md` card is enough.
> **See also**: [bst.md](./bst.md) — the canonical templates every section here extends; [bst_examples.md](./bst_examples.md) — the worked BST problems; [binary_tree.md](./binary_tree.md#template-10-post-order-structural-modification--lc-1110-) — deleting many nodes into a forest (LC 1110); [segment_tree.md](./segment_tree.md) — range queries over an array instead of a tree; [binary_indexed_tree.md](./binary_indexed_tree.md) — prefix-sum ranks without a tree of nodes.

## LeetCode Problem Lists

- [Binary Search Tree](https://leetcode.com/problem-list/binary-search-tree/)
- [Binary Tree](https://leetcode.com/problem-list/binary-tree/)
- [Design](https://leetcode.com/problem-list/design/)

## Overview

Five techniques, each one a canonical [bst.md](./bst.md) template with one thing added: an
explicit stack that pauses the in-order walk, a `prev` pointer that spots the inversions, a
subtree-size field that answers rank, interval bounds that rebuild from preorder, and a loop over
every possible root. They show up as interview **follow-ups** ("what if the data keeps
changing?", "can you do it in O(1) space?", "what if the input is a linked list?") far more
often than as the first question.

### Prerequisites
- [bst § Template 5](./bst.md#template-5-in-order-walk-is-sorted--lc-230): the iterative
  in-order walk. Templates 1 and 2 below pause it and watch it.
- [bst § Template 2](./bst.md#template-2-insert-at-the-null-leaf--lc-701): insert by returning
  the child. Template 3 adds one counter to it.
- [bst § Template 4](./bst.md#template-4-validate-with-bounds--lc-98): the `(low, high)`
  interval carried down. Template 4 builds with it.
- [bst § Template 6](./bst.md#template-6-sorted-array-to-balanced-bst--lc-108): the middle of a
  sorted range as the root. LC 109 and LC 1382 reduce to it; Template 5 tries every root instead.

## Recognition

| Signal in the statement | Constraint cue | Reach for | Not this if |
|---|---|---|---|
| "iterator", "`next()` / `hasNext()` in O(h) memory" | calls ≤ 10⁵, average O(1) | [Template 1](#template-1-lazy-in-order-iterator--lc-173-) lazy in-order | one pass is enough → [bst § Template 5](./bst.md#template-5-in-order-walk-is-sorted--lc-230) |
| "two nodes were swapped by mistake, recover the tree" | follow-up: O(1) space | [Template 2](#template-2-in-order-drop-detection--lc-99-) in-order drop detection | only "is it valid?" → [bst § Template 4](./bst.md#template-4-validate-with-bounds--lc-98) |
| "kth largest in a stream", "rank of x", "kth smallest under frequent inserts" | ≤ 10⁴ adds, O(log n) each | [Template 3](#template-3-size-augmented-order-statistic-bst--lc-703-) size-augmented BST | k is fixed → a size-k min-heap is shorter ([heap.md](./heap.md)) |
| "construct the BST from its preorder" | n ≤ 100 is fine either way | [Template 4](#template-4-preorder-build-with-bounds--lc-1008) bounds on a preorder stream | the input is sorted → [bst § Template 6](./bst.md#template-6-sorted-array-to-balanced-bst--lc-108) |
| "all structurally unique BSTs", "how many unique BSTs" | n ≤ 8 to list, n ≤ 19 to count | [Template 5](#template-5-every-value-as-the-root--lc-95) every value as the root | one balanced tree is wanted → [bst § Template 6](./bst.md#template-6-sorted-array-to-balanced-bst--lc-108) |
| "delete these nodes and return the forest" | | not a BST technique → [binary_tree § Template 10](./binary_tree.md#template-10-post-order-structural-modification--lc-1110-) | |

## Lazy In-order Iteration

### Template 1: Lazy In-order Iterator — LC 173 ⭐⭐⭐⭐

**Recognise** — "implement an iterator over a BST", "`next()` returns the next smallest",
"`next()` and `hasNext()` in average O(1) time and O(h) memory", "merge two BSTs" (LC 1305).

**Key point** — the stack holds exactly the nodes whose value has not been returned yet but whose
left subtree is done: the left spine below the current position. Popping gives the next value in
order; pushing that node's right child's left spine restores the invariant.

```python
# python
# LC 173 - Binary Search Tree Iterator
# IDEA: keep the unvisited left spine on a stack; pop = next value, then push the right child's spine
# time = O(1) amortised per next(), space = O(h)
class BSTIterator:
    def __init__(self, root):
        self.stack = []
        self._push_left(root)

    def _push_left(self, node):
        while node:                       # the smallest unvisited value ends up on top
            self.stack.append(node)
            node = node.left

    def next(self):
        node = self.stack.pop()
        self._push_left(node.right)       # its right subtree comes next in order
        return node.val

    def hasNext(self):
        return len(self.stack) > 0
```

```java
// java
// LC 173 - Binary Search Tree Iterator
// IDEA: unvisited left spine on a stack; pop = next value, then push the right child's spine
// time = O(1) amortised per next(), space = O(h)
class BSTIterator {
    private final Deque<TreeNode> stack = new ArrayDeque<>();

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
        pushLeft(node.right);
        return node.val;
    }

    public boolean hasNext() {
        return !stack.isEmpty();
    }
}
```

**Complexity**
- **Time O(1) amortised per `next()`**: each node is pushed once by `_push_left` and popped once
  by `next`, so n calls cost O(n) in total, although one call can push h nodes.
- **Space O(h)**: the stack never holds more than one root-to-leaf spine.

**Dry run** — `[7,3,15,null,null,9,20]`

```text
init        push 7, 3                stack [7, 3]
next() = 3  pop 3, no right child    stack [7]
next() = 7  pop 7, push 15, 9        stack [15, 9]
next() = 9  pop 9                    stack [15]
next() = 15 pop 15, push 20          stack [20]
next() = 20 pop 20                   stack []      hasNext() = false
```

**Traps**
- Flattening the whole tree in the constructor passes the tests, but it is O(n) memory, which is
  exactly what the follow-up rules out.
- Pushing `node.right` itself instead of its left spine returns 15 before 9 in the dry run.

**Not this if** — the whole sorted order is needed at once, or only the kth value: the plain
in-order walk of [bst § Template 5](./bst.md#template-5-in-order-walk-is-sorted--lc-230) is
shorter.

**Drill** — LC 173 · LC 230 *(stop after k pops)* · LC 1305 *(two iterators, merged)* · LC 653
*(a forward and a reverse iterator meet in the middle)*

### All Elements in Two Binary Search Trees — LC 1305

> **Key point** — two lazy iterators are two sorted streams, so the answer is the merge step of
> merge sort: always take from the stream whose next value is smaller. It needs O(h1 + h2) extra
> memory instead of the O(m + n) of two flattened lists.

**Reduces to**: [Template 1](#template-1-lazy-in-order-iterator--lc-173-) — twice, side by side.

```python
# python
# LC 1305 - All Elements in Two Binary Search Trees
# IDEA: two left-spine stacks; pop from the one whose top is smaller, then push its right spine
# time = O(m + n), space = O(h1 + h2) excluding the output
def getAllElements(root1, root2):
    stack1, stack2, result = [], [], []

    def push_left(stack, node):
        while node:
            stack.append(node)
            node = node.left

    push_left(stack1, root1)
    push_left(stack2, root2)
    while stack1 or stack2:
        # take from stack1 when stack2 is empty, or when both are live and stack1's top is smaller
        if not stack2 or (stack1 and stack1[-1].val <= stack2[-1].val):
            node = stack1.pop()
            push_left(stack1, node.right)
        else:
            node = stack2.pop()
            push_left(stack2, node.right)
        result.append(node.val)
    return result
```

**Traps**
- Comparing `stack1[-1].val <= stack2[-1].val` before checking that both stacks are non-empty:
  one tree runs out first, and `[1,8]` with `[8,1]` raises an `IndexError` partway through.

## Repairing a Broken BST

### Template 2: In-order Drop Detection — LC 99 ⭐⭐⭐⭐⭐

**Recognise** — "exactly two nodes of the BST were swapped by mistake; recover the tree without
changing its structure", "follow-up: O(1) space". The same walk answers "minimum difference
between any two nodes" (LC 530) and "mode" (LC 501).

**Key point** — a valid BST's in-order sequence is strictly increasing, so a swap shows up as a
**drop**, `prev.val > node.val`. Adjacent swapped values make one drop; distant ones make two.
`first` is `prev` at the first drop and never changes; `second` is `node` at every drop, so it
ends at the last one. That single rule covers both cases.

```python
# python
# LC 99 - Recover Binary Search Tree
# IDEA: in-order walk with a prev pointer; first = prev at the first drop, second = node at the last
# time = O(n), space = O(h)
def recoverTree(root):
    first = second = prev = None

    def inorder(node):
        nonlocal first, second, prev
        if not node:
            return
        inorder(node.left)
        if prev and prev.val > node.val:     # a drop in the sorted order
            if first is None:
                first = prev                 # the too-large value, from the first drop only
            second = node                    # the too-small value, from the last drop
        prev = node
        inorder(node.right)

    inorder(root)
    first.val, second.val = second.val, first.val   # swap values; the structure stays
```

```java
// java
// LC 99 - Recover Binary Search Tree
// IDEA: in-order with prev; first is set once, second at every drop; then swap the values
// time = O(n), space = O(h)
private TreeNode first, second, prev;

public void recoverTree(TreeNode root) {
    first = second = prev = null;
    inorder(root);
    int temp = first.val;
    first.val = second.val;
    second.val = temp;
}

private void inorder(TreeNode node) {
    if (node == null) {
        return;
    }
    inorder(node.left);
    if (prev != null && prev.val > node.val) {
        if (first == null) {
            first = prev;
        }
        second = node;
    }
    prev = node;
    inorder(node.right);
}
```

A second solution, kept for its **O(1) space**: the same drop detection over a Morris walk, which
threads each node's in-order predecessor to it instead of using a stack.

```python
# python
# LC 99 - Recover Binary Search Tree, O(1) extra space
# IDEA: Morris in-order (thread the predecessor, unthread on the second visit) + the same drop rule
# time = O(n), space = O(1)
def recoverTree(root):
    first = second = prev = None
    cur = root
    while cur:
        if cur.left:
            pred = cur.left
            while pred.right and pred.right is not cur:
                pred = pred.right
            if not pred.right:               # first visit: thread, then go left
                pred.right = cur
                cur = cur.left
                continue
            pred.right = None                # second visit: unthread, then visit cur
        if prev and prev.val > cur.val:
            if first is None:
                first = prev
            second = cur
        prev = cur
        cur = cur.right
    first.val, second.val = second.val, first.val
```

**Complexity**
- **Time O(n)**: the in-order walk visits each node once. Morris crosses each edge at most three
  times, still O(n).
- **Space O(h)**: the recursion stack of `inorder`. Morris keeps only `cur`, `pred` and the three
  markers, O(1); the threads reuse null pointers that already exist.

**Dry run** — in-order of the broken tree is `[1, 6, 3, 4, 5, 2, 7]` (2 and 6 swapped)

```text
6 > 3   first drop  -> first = 6, second = 3
5 > 2   second drop -> first stays 6, second = 2
swap 6 and 2        -> [1, 2, 3, 4, 5, 6, 7]
```

**Traps**
- Setting `first = prev` at every drop: on the dry run it ends as 5, and swapping 5 with 2 leaves
  the tree broken.
- Stopping at the first drop: `[1, 6, 3, 4, 5, 2, 7]` swaps 6 with 3.
- A `TreeNode(-2**31)` sentinel for `prev`: LC 99 allows that value in a real node. Start at
  `None` and guard the comparison.
- Swapping the two node references instead of their `.val` changes nothing in the tree.

**Not this if** — the question is only whether the tree is valid: carry the bounds down,
[bst § Template 4](./bst.md#template-4-validate-with-bounds--lc-98).

**Drill** — LC 530 *(min gap between neighbours)* · LC 501 *(run length of equal neighbours)* ·
LC 99 · LC 426 *(link `prev` and `node` into a list)*

## Order Statistics

### Template 3: Size-Augmented Order-Statistic BST — LC 703 ⭐⭐⭐⭐

**Recognise** — "kth largest element in a stream", "return the kth largest after each `add`",
"kth smallest when the BST is modified often" (the LC 230 follow-up), "how many values are below
x" — any query you would answer with a `TreeMap` or `SortedList` and a rank.

**Key point** — every node stores `count`, the size of its subtree, and insert increments it on
every node of the path. Then the `count` of `node.right` says how many values are larger than
`node.val`, so a rank query descends one path like a search: right if k fits there, `node` if it
is next, else left with k reduced by everything skipped.

```python
# python
# LC 703 - Kth Largest Element in a Stream
# IDEA: each node keeps its subtree size; kth largest descends using size(node.right)
# time = O(h) per add, space = O(n)
class CountNode:
    def __init__(self, val):
        self.val = val
        self.left = None
        self.right = None
        self.count = 1                       # nodes in the subtree rooted here


class KthLargest:
    def __init__(self, k, nums):
        self.k = k
        self.root = None
        for v in nums:
            self.root = self._insert(self.root, v)

    def _insert(self, node, val):
        if not node:
            return CountNode(val)
        node.count += 1                      # the new value lands somewhere below
        if val < node.val:
            node.left = self._insert(node.left, val)
        else:
            node.right = self._insert(node.right, val)   # duplicates go right
        return node

    def _size(self, node):
        return node.count if node else 0

    def add(self, val):
        self.root = self._insert(self.root, val)
        node, k = self.root, self.k
        while True:
            right_size = self._size(node.right)          # values larger than node.val
            if k <= right_size:
                node = node.right
            elif k == right_size + 1:
                return node.val
            else:
                k -= right_size + 1                      # skip the right subtree and node
                node = node.left
```

```java
// java
// LC 703 - Kth Largest Element in a Stream
// IDEA: subtree sizes on every node; rank-descend using the right subtree's size
// time = O(h) per add, space = O(n)
class KthLargest {
    private static class Node {
        int val;
        int count = 1;
        Node left, right;
        Node(int val) { this.val = val; }
    }

    private final int k;
    private Node root;

    public KthLargest(int k, int[] nums) {
        this.k = k;
        for (int v : nums) {
            root = insert(root, v);
        }
    }

    private Node insert(Node node, int val) {
        if (node == null) {
            return new Node(val);
        }
        node.count++;
        if (val < node.val) {
            node.left = insert(node.left, val);
        } else {
            node.right = insert(node.right, val);
        }
        return node;
    }

    private int size(Node node) {
        return node == null ? 0 : node.count;
    }

    public int add(int val) {
        root = insert(root, val);
        Node node = root;
        int rank = k;
        while (true) {
            int rightSize = size(node.right);
            if (rank <= rightSize) {
                node = node.right;
            } else if (rank == rightSize + 1) {
                return node.val;
            } else {
                rank -= rightSize + 1;
                node = node.left;
            }
        }
    }
}
```

**Complexity**
- **Time O(h) per `add`**: `_insert` and the rank loop each walk one root-to-leaf path. O(log n)
  on a balanced tree, O(n) when the stream arrives sorted.
- **Space O(n)**: one node per value ever added.

**Dry run** — `k = 3`, `nums = [4,5,8,2]`, then `add(3)`

```text
tree (count):  4(5) -> left 2(2) -> right 3(1);  right 5(2) -> right 8(1)
at 4: size(right) = 2, k = 3 == 2 + 1  -> answer 4
```

**Traps**
- Incrementing `count` only on the new leaf: every ancestor's size goes stale, and the second
  `add` in LC 703's example returns the wrong rank.
- Sending duplicates left on one insert and right on another: LC 703's `[7,7,7,7,8,3]` counts
  the 7s inconsistently. Pick one side.
- The skewed worst case is O(n) per call; say so, and name a balanced tree (`TreeMap`, a
  red-black tree) as the production fix.

**Not this if** — k is fixed and only the kth largest is ever asked: a size-k min-heap is
O(log k) per add and five lines long ([heap.md](./heap.md)). Give that first, and this as the
answer to "what if k changes, or you need rank(x)?".

**Drill** — LC 703 · LC 230 *(follow-up)* · LC 315 *(count smaller after self)* · LC 295
*(median: the k = n / 2 query)*

## Construction Variants

### Template 4: Preorder Build with Bounds — LC 1008

**Recognise** — "construct the BST from its preorder traversal", "deserialize a BST that was
written in preorder" (LC 449), "verify that this sequence is a BST's preorder" (LC 255).

**Key point** — preorder lists a root before everything in its subtrees, so read it as a stream
with one cursor. A call owns the open interval `(low, high)`; the next value starts this subtree
exactly when it fits, and otherwise belongs to an ancestor, so the call returns `None` without
consuming it. Every value is consumed once.

```python
# python
# LC 1008 - Construct Binary Search Tree from Preorder Traversal
# IDEA: one cursor over the preorder; a call builds a node only if the next value fits (low, high)
# time = O(n), space = O(h)
def bstFromPreorder(preorder):
    index = 0

    def build(low, high):
        nonlocal index
        if index == len(preorder) or not (low < preorder[index] < high):
            return None                          # the next value belongs to an ancestor
        node = TreeNode(preorder[index])
        index += 1
        node.left = build(low, node.val)         # smaller values, until one is too big
        node.right = build(node.val, high)
        return node

    return build(float("-inf"), float("inf"))
```

```java
// java
// LC 1008 - Construct Binary Search Tree from Preorder Traversal
// IDEA: a shared cursor; build a node only while the next value fits the interval
// time = O(n), space = O(h)
private int index;

public TreeNode bstFromPreorder(int[] preorder) {
    index = 0;
    return build(preorder, Long.MIN_VALUE, Long.MAX_VALUE);
}

private TreeNode build(int[] preorder, long low, long high) {
    if (index == preorder.length || preorder[index] <= low || preorder[index] >= high) {
        return null;
    }
    TreeNode node = new TreeNode(preorder[index++]);
    node.left = build(preorder, low, node.val);
    node.right = build(preorder, node.val, high);
    return node;
}
```

**Complexity**
- **Time O(n)**: each value is consumed by exactly one `build` call; every other call returns at
  the bound check in O(1), and there are at most two of those per node.
- **Space O(h)**: the recursion stack.

**Dry run** — `[8,5,1,7,10,12]`

```text
build(-inf, inf)   takes 8
  build(-inf, 8)   takes 5
    build(-inf, 5) takes 1;  its children see 7, which is outside (-inf, 1) and (1, 5) -> None
    build(5, 8)    takes 7;  its children see 10, outside (5, 7) and (7, 8)       -> None
  build(8, inf)    takes 10; left sees 12, outside (8, 10) -> None; right takes 12
result [8,5,10,1,7,null,12]
```

**Traps**
- Inserting the values one by one with [bst § Template 2](./bst.md#template-2-insert-at-the-null-leaf--lc-701)
  works, but it is O(n²) on a sorted preorder such as `[1,2,3,…]`.
- Advancing the cursor before the bound check consumes a value that belongs to an ancestor.

**Not this if** — the input is sorted (an in-order): any root works, so take the middle for a
balanced tree, [bst § Template 6](./bst.md#template-6-sorted-array-to-balanced-bst--lc-108).

**Drill** — LC 1008 · LC 449 *(serialize with this as the decoder)* · LC 255 *(verify only:
the same bounds, nothing built)*

### Convert Sorted List to Binary Search Tree — LC 109

> **Key point** — the middle is still the root, but a list has no index. Slow/fast pointers find
> the middle of `[left, right)` in O(length), which makes the whole build O(n log n).

**Reduces to**: [bst § Template 6](./bst.md#template-6-sorted-array-to-balanced-bst--lc-108) —
with the middle found by walking instead of by index.

```python
# python
# LC 109 - Convert Sorted List to Binary Search Tree
# IDEA: slow/fast pointers find the middle of [left, right); it is the root, recurse on both halves
# time = O(n log n), space = O(log n)
def sortedListToBST(head):
    def middle(left, right):
        slow = fast = left
        while fast is not right and fast.next is not right:
            slow = slow.next
            fast = fast.next.next
        return slow

    def convert(left, right):                  # builds the nodes in [left, right)
        if left is right:
            return None
        mid = middle(left, right)
        node = TreeNode(mid.val)
        node.left = convert(left, mid)
        node.right = convert(mid.next, right)
        return node

    return convert(head, None)
```

A second solution, kept because it is **O(n)**: build in in-order order. Count the nodes, then
recurse on index ranges; the left subtree is built first, so the list's head is always the next
value to place.

```python
# python
# LC 109 - in-order simulation
# IDEA: build(lo, hi) makes the left subtree first, then takes the list's current head as its root
# time = O(n), space = O(log n)
def sortedListToBST(head):
    n = 0
    node = head
    while node:
        n += 1
        node = node.next
    current = head

    def build(lo, hi):
        nonlocal current
        if lo > hi:
            return None
        mid = (lo + hi) // 2
        left = build(lo, mid - 1)              # consumes exactly the values before mid
        root = TreeNode(current.val)
        current = current.next
        root.left = left
        root.right = build(mid + 1, hi)
        return root

    return build(0, n - 1)
```

**Traps**
- Copying the list into an array first is O(n) time too, and acceptable; it costs O(n) memory,
  which is the usual follow-up.

### Balance a Binary Search Tree — LC 1382 ⭐⭐⭐⭐

> **Key point** — do not rotate. Any BST flattens to its sorted values with an in-order walk, and
> any sorted range rebuilds into a balanced BST by taking the middle as the root. LC 1382 is
> LC 94 followed by LC 108.

**Reduces to**: [bst § Template 5](./bst.md#template-5-in-order-walk-is-sorted--lc-230), then
[bst § Template 6](./bst.md#template-6-sorted-array-to-balanced-bst--lc-108).

```python
# python
# LC 1382 - Balance a Binary Search Tree
# IDEA: in-order into a sorted list, then rebuild with the middle of each range as its root
# time = O(n), space = O(n)
def balanceBST(root):
    values = []

    def inorder_walk(node):
        if not node:
            return
        inorder_walk(node.left)
        values.append(node.val)
        inorder_walk(node.right)

    def build(lo, hi):
        if lo > hi:
            return None
        mid = (lo + hi) // 2
        node = TreeNode(values[mid])
        node.left = build(lo, mid - 1)
        node.right = build(mid + 1, hi)
        return node

    inorder_walk(root)
    return build(0, len(values) - 1)
```

**Traps**
- Pre-order or post-order is not sorted, so the rebuilt tree is not a BST.
- LC 1382 allows 10⁴ nodes in a skewed chain: the recursive in-order walk exceeds Python's
  default recursion limit of 1000. Walk it iteratively ([bst § Template 5](./bst.md#template-5-in-order-walk-is-sorted--lc-230)).
- Reusing the old nodes instead of allocating is fine only if `build` assigns **both** children
  of every node; a stale pointer from the old tree otherwise survives.
- In place, O(1) space, is the Day–Stout–Warren algorithm (rotate into a vine, then compress). It
  is rare in interviews; naming it is enough.

### Template 5: Every Value as the Root — LC 95

**Recognise** — "generate all structurally unique BSTs that store `1..n`", "how many structurally
unique BSTs" (LC 96), "all possible full binary trees" (LC 894). n ≤ 8 to list them: the answer
itself is exponential.

**Key point** — choosing `i` as the root forces `lo..i-1` into the left subtree and `i+1..hi` into
the right, so the trees with root `i` are every left subtree paired with every right subtree. An
empty range must return `[None]`, one empty tree, not `[]`, or the pairing loop produces nothing.

```python
# python
# LC 95 - Unique Binary Search Trees II
# IDEA: each value in [lo, hi] as the root; pair every left subtree with every right subtree
# time = O(n * Catalan(n)), space = O(n * Catalan(n)) for the output
def generateTrees(n):
    def generate(lo, hi):
        if lo > hi:
            return [None]                        # one empty tree, so the pairing still runs
        trees = []
        for root_val in range(lo, hi + 1):
            for left in generate(lo, root_val - 1):
                for right in generate(root_val + 1, hi):
                    trees.append(TreeNode(root_val, left, right))
        return trees

    return generate(1, n) if n else []
```

```java
// java
// LC 95 - Unique Binary Search Trees II
// IDEA: each value as the root; the cartesian product of the left and right subtree lists
// time = O(n * Catalan(n)), space = O(n * Catalan(n))
public List<TreeNode> generateTrees(int n) {
    return n == 0 ? new ArrayList<>() : generate(1, n);
}

private List<TreeNode> generate(int lo, int hi) {
    List<TreeNode> trees = new ArrayList<>();
    if (lo > hi) {
        trees.add(null);
        return trees;
    }
    for (int rootVal = lo; rootVal <= hi; rootVal++) {
        for (TreeNode left : generate(lo, rootVal - 1)) {
            for (TreeNode right : generate(rootVal + 1, hi)) {
                trees.add(new TreeNode(rootVal, left, right));
            }
        }
    }
    return trees;
}
```

**Complexity**
- **Time O(n · Catalan(n))**: Catalan(n) trees come out (Catalan(8) = 1430), and each costs O(n)
  nodes to build. Subtrees are shared between results, which is fine for LC 95.
- **Space O(n · Catalan(n))**: the output dominates.

**Traps**
- Returning `[]` for an empty range: every leaf's pairing loop runs zero times, and `n = 1`
  returns no trees.
- `n = 0` must return `[]`, not `[None]`.

**Not this if** — only one balanced tree is wanted: take the middle,
[bst § Template 6](./bst.md#template-6-sorted-array-to-balanced-bst--lc-108). Only the count is
wanted: see LC 96 below; building the trees is pointless.

**Drill** — LC 96 · LC 95 · LC 894 *(full binary trees: odd sizes only)* · LC 241 *(the same
split-and-pair recursion over an expression)*

### Unique Binary Search Trees — LC 96

> **Key point** — the count for n values depends only on n: root `i` leaves `i - 1` values on the
> left and `n - i` on the right, so `dp[n] = Σ dp[i-1] · dp[n-i]`. These are the Catalan numbers.

**Reduces to**: [Template 5](#template-5-every-value-as-the-root--lc-95) — counted instead of built.

```python
# python
# LC 96 - Unique Binary Search Trees
# IDEA: dp[nodes] = sum over every root of dp[left size] * dp[right size]
# time = O(n^2), space = O(n)
def numTrees(n):
    dp = [0] * (n + 1)
    dp[0] = 1                                    # the empty tree
    for nodes in range(1, n + 1):
        for root in range(1, nodes + 1):
            dp[nodes] += dp[root - 1] * dp[nodes - root]
    return dp[n]
```

**Traps**
- Adding `dp[left] + dp[right]` instead of multiplying: every left shape pairs with every right
  shape. `n = 3` must give 5.
- `dp[0] = 0` zeroes every product that has an empty side.

## Carrying Bounds to Measure

### Maximum Difference Between Node and Ancestor — LC 1026

> **Key point** — the largest `|ancestor − descendant|` on a path is the path's max minus its min,
> so carry those two numbers down and read `high − low` when the path ends. No pair is ever
> compared, and no BST order is used.

**Reduces to**: [bst § Template 4](./bst.md#template-4-validate-with-bounds--lc-98) — the same
two values carried down, widened instead of checked.

```python
# python
# LC 1026 - Maximum Difference Between Node and Ancestor
# IDEA: carry the path's min and max down; at the end of a path the answer is max - min
# time = O(n), space = O(h)
def maxAncestorDiff(root):
    def dfs(node, low, high):
        if not node:
            return high - low                    # the path ended: its widest spread
        low = min(low, node.val)
        high = max(high, node.val)
        return max(dfs(node.left, low, high), dfs(node.right, low, high))

    return dfs(root, root.val, root.val)
```

**Traps**
- Comparing each node only with its parent: LC 1026's example 1,
  `[8,3,10,1,6,null,14,null,null,4,7,13]`, returns 5 (8 − 3) instead of 7 (8 − 1, two edges
  apart).

| | Validate (LC 98) | Max ancestor diff (LC 1026) |
|---|---|---|
| carried down | the legal interval | the path's min and max |
| at a node outside it | return false | widen it |
| children combine with | `and` | `max` |

## When Not to Use

| Technique | Use the simpler card instead when… |
|---|---|
| [Template 1](#template-1-lazy-in-order-iterator--lc-173-) lazy iterator | the walk runs once, start to finish: the in-order loop of [bst § Template 5](./bst.md#template-5-in-order-walk-is-sorted--lc-230) is the same stack, without the class |
| [Template 2](#template-2-in-order-drop-detection--lc-99-) drop detection | the question is only "is it valid?": [bst § Template 4](./bst.md#template-4-validate-with-bounds--lc-98). Morris is worth writing only when O(1) space is asked for |
| [Template 3](#template-3-size-augmented-order-statistic-bst--lc-703-) size-augmented BST | k is fixed: a size-k heap. The tree is static: in-order to the kth node, [bst § Template 5](./bst.md#template-5-in-order-walk-is-sorted--lc-230) |
| [Template 4](#template-4-preorder-build-with-bounds--lc-1008) preorder with bounds | the input is sorted, or any valid BST is accepted: middle as root, [bst § Template 6](./bst.md#template-6-sorted-array-to-balanced-bst--lc-108) |
| [Template 5](#template-5-every-value-as-the-root--lc-95) every root | only one tree, or only the count, is wanted: middle as root, or the LC 96 DP |

## Summary

| If the follow-up is… | Reach for | Cost |
|---|---|---|
| "don't materialise the whole traversal" (LC 173) | [Template 1](#template-1-lazy-in-order-iterator--lc-173-) — push only the left spine | O(h) space, O(1) amortised per `next()` |
| "two nodes got swapped, fix it" (LC 99) | [Template 2](#template-2-in-order-drop-detection--lc-99-) — in-order + `prev`, `first` write-once | O(n) / O(h), or O(1) with Morris |
| "what if we keep inserting?" (LC 230 → 703) | [Template 3](#template-3-size-augmented-order-statistic-bst--lc-703-) — subtree sizes on every node | O(h) per insert and per rank query |
| "the input is a preorder dump" (LC 1008, 449) | [Template 4](#template-4-preorder-build-with-bounds--lc-1008) — bounds on a stream | O(n) / O(h) |
| "the input is a linked list" (LC 109) | [LC 109](#convert-sorted-list-to-binary-search-tree--lc-109) — slow/fast middle, or in-order simulation | O(n log n), or O(n) |
| "rebalance this BST" (LC 1382) | [LC 1382](#balance-a-binary-search-tree--lc-1382-) — in-order, then middle as root | O(n) / O(n) |
| "list / count every BST" (LC 95, 96) | [Template 5](#template-5-every-value-as-the-root--lc-95) — every value as the root | Catalan(n) trees; O(n²) to count |

**The one idea underneath all of it**: a BST's in-order walk is a sorted sequence, and every
technique here either changes how much of that sequence is held at once, watches it for an
inversion, or adds one field to a node so a single descent answers a question about the sequence.
