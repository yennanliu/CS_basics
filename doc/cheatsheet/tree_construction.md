# Tree Construction

> **Scope** — Building a binary tree from something flat: two traversal arrays, an index range over one array, or a parenthesised / infix string — the inverse direction of every tree problem that only reads a tree.
> **See also**: [tree_codec.md](./tree_codec.md) — the encode side and the full codec family, including the LC 536 parser code; [tree.md](./tree.md) — traversal templates and the pattern catalogue; [tree_examples.md](./tree_examples.md) — the rest of the worked tree problems; [bst.md](./bst.md) — building and rebuilding ordered trees (LC 108, 449).

## LeetCode Problem Lists

- [Tree](https://leetcode.com/problem-list/tree/)
- [Binary Tree](https://leetcode.com/problem-list/binary-tree/)
- [Divide and Conquer](https://leetcode.com/problem-list/divide-and-conquer/)

## Overview

Every construction problem answers one question at each step: **which element is the root of this
range?** Once the root is named the range splits in two and the same function recurses. What differs
between problems is only *how the root is identified*:

```text
LC 105 / 106   root = the next element of the pre/post-order array, split by its index in inorder
LC 654         root = the MAXIMUM of the range
LC 108         root = the MIDDLE of the range (sorted input -> balanced BST)
LC 536         root = the value before the first '(' — parentheses delimit the child ranges
LC 1597        root = the LAST lowest-precedence operator outside any parenthesis
```

### Key Properties
- **Complexity**: O(N) with a hash map from value → inorder index, or with a single shared cursor; O(N²) if you re-scan or re-slice per call
- **Core Idea**: identify the root of the range, recurse on the two sub-ranges, never copy the input
- **When to Use**: the output is a `TreeNode`, not a value read off one
- **Pitfall**: passing sliced copies instead of index bounds is what turns these from O(N) into O(N²)

## Problem Categories

| Category | Root is found by | Examples |
|----------|------------------|----------|
| **Two traversals** | its position in the *other* traversal | LC 105, 106, 889 |
| **Index range over one array** | max / middle of the range | LC 654, 108, 1008 |
| **Parenthesised string** | the value before the first `(` | LC 536, 1597 |
| **Linear codec stream** | the next token | LC 297, 449, 1028 |

## Templates & Algorithms

### 1) Maximum Binary Tree — LC 654 (Build Tree from an Array by Index Range) ⭐⭐⭐⭐

**Pattern**: *"build a tree from an array by index range"* — the generic sibling of LC 105 (build from pre-order + in-order). The recursion is always the same three steps:

1. pick the **root index** inside `[lo, hi]` (here: index of the max; LC 105: the pre-order head; LC 108: the middle),
2. recurse on `[lo, rootIdx - 1]` → left subtree,
3. recurse on `[rootIdx + 1, hi]` → right subtree.

**Key Idea**: never slice/copy the array — pass `(lo, hi)` indices down. The base case `lo > hi` returns `null`, which is what makes empty sub-ranges work without special casing.

```java
// java
// LC 654 - Maximum Binary Tree
// IDEA: root of range [lo, hi] = the MAX element; recurse on the two sub-ranges
class Solution {
    public TreeNode constructMaximumBinaryTree(int[] nums) {
        // time = O(N^2) worst case (already-sorted input), O(N log N) average
        // space = O(N) for the recursion stack in the worst case
        return build(nums, 0, nums.length - 1);
    }

    private TreeNode build(int[] nums, int lo, int hi) {
        if (lo > hi) return null;             // NOTE: empty range -> null child

        int idx = lo;                          // find index of max in [lo, hi]
        for (int i = lo + 1; i <= hi; i++) {
            if (nums[i] > nums[idx]) idx = i;
        }

        TreeNode root = new TreeNode(nums[idx]);
        root.left  = build(nums, lo, idx - 1);   // everything LEFT of the max
        root.right = build(nums, idx + 1, hi);   // everything RIGHT of the max
        return root;
    }
}
```

```python
# python
# LC 654 - Maximum Binary Tree
# IDEA: pick max index as root of the range, recurse on left / right sub-ranges
class Solution:
    def constructMaximumBinaryTree(self, nums):
        # time = O(N^2) worst case, space = O(N)
        def build(lo, hi):
            if lo > hi:
                return None
            idx = lo
            for i in range(lo + 1, hi + 1):
                if nums[i] > nums[idx]:
                    idx = i
            root = TreeNode(nums[idx])
            root.left = build(lo, idx - 1)
            root.right = build(idx + 1, hi)
            return root

        return build(0, len(nums) - 1)
```

**Variation — Convert Sorted Array to BST (LC 108)**: identical skeleton, the only change is the *root-picking rule* — take `mid = (lo + hi) // 2` instead of the argmax, which drops the cost to `O(N)` and yields a height-balanced tree.

```python
# python
# LC 108 - Convert Sorted Array to Binary Search Tree
# IDEA: same "build by index range" skeleton as LC 654, root = MIDDLE element
class Solution:
    def sortedArrayToBST(self, nums):
        # time = O(N), space = O(log N)
        def build(lo, hi):
            if lo > hi:
                return None
            mid = (lo + hi) // 2          # middle -> balanced tree
            root = TreeNode(nums[mid])
            root.left = build(lo, mid - 1)
            root.right = build(mid + 1, hi)
            return root

        return build(0, len(nums) - 1)
```

| Problem | Root-picking rule inside `[lo, hi]` | Time |
|---------|--------------------------------------|------|
| LC 654 Maximum Binary Tree | index of the **max** value | O(N^2) worst / O(N) with a monotonic stack |
| LC 108 Sorted Array → BST  | **middle** index | O(N) |
| LC 105 Preorder + Inorder  | pre-order head, split in-order by its position (HashMap for O(1) lookup) | O(N) |

### 2) Construct Binary Tree from Preorder and Inorder Traversal — LC 105
```python
#  Construct Binary Tree from Preorder and Inorder Traversal
# V0
# IDEA: the pre-order head is the root; its position in in-order splits the
#       remaining elements into the two subtrees. LC 105 builds a GENERAL binary
#       tree -- no BST ordering is assumed or required.
# time = O(N^2), space = O(N^2)
#   -> `inorder.index` rescans the range on every call and each recursion copies
#      four slices. Readable, but NOT the O(N) the table above quotes -- that is
#      the Java form below, which passes index bounds and a value -> index map.
class Solution(object):
    def buildTree(self, preorder, inorder):
        if len(preorder) == 0:
            return None
        if len(preorder) == 1:
            return TreeNode(preorder[0])
        ### NOTE : init root like below (via TreeNode and root value (preorder[0]))
        root = TreeNode(preorder[0])
        # get the index of root.val in order to SPLIT TREE
        index = inorder.index(root.val)  # the index of root at inorder, and we can also get the length of left-sub-tree, right-sub-tree ( preorder[1:index+1]) for following using
        # recursion for root.left
        #### NOTE : preorder[1 : index + 1] (for left sub tree)
        root.left = self.buildTree(preorder[1 : index + 1], inorder[ : index]) ### the two traversals cover the SAME elements, so the left subtree has the same length in both -- that is what lets one index split both arrays
        # recursion for root.right 
        root.right = self.buildTree(preorder[index + 1 : ], inorder[index + 1 :]) ### same on the right: everything after `index` in in-order is the right subtree
        return root
```

**The same split, in Java** — a `HashMap` from value to in-order index turns the
`inorder.index(...)` scan into O(1), which is what takes the whole build from O(n²) to O(n):

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

**Inorder + postorder — LC 106.** The only change is where the root comes from: the *last*
element of post-order instead of the first of pre-order, so the two child slices shift by one.

```python
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

### 3) Construct Binary Tree from String — LC 536 (Recursive Descent Parsing) ⭐⭐⭐⭐

> Reference: `leetcode_python/Tree/construct-binary-tree-from-string.py`
>
> ```
> Input:  "4(2(3)(1))(6(5))"
>
>        4
>      /   \
>     2     6
>    / \   /
>   3   1 5
> ```
> Chars are only `'('`, `')'`, `'-'` and `'0'`–`'9'`. An empty tree is `""`, **never** `"()"`.

#### Core Idea

**The string is a pre-order serialization where parentheses — not null markers — carry the
structure.** So this is not a tree problem with a parsing step; it is a **parsing problem** whose
output happens to be a tree. Write the grammar first and the code falls out:

```text
tree   := number ( '(' tree ')' )? ( '(' tree ')' )?
number := '-'? digit+
```

Three rules do all the work:

1. **A node is a number, optionally followed by 1 or 2 parenthesised subtrees.**
2. **The first group is always the left child** (problem guarantee) — so a node with only a right
   child is *impossible* to express. That is exactly why `"()"` is banned as an empty tree.
3. **Each `(...)` group is balanced**, so the parser needs to know where its matching `)` is.

**Two ways to answer "where does this subtree end?"** — this is the only real design decision:

| | **Index cursor** (V0' / V2) | **Balance counter + slice** (V0 / V1) |
|---|---|---|
| How | one shared `i` walks the string; each call **returns the new `i`** | count `+1` on `(`, `-1` on `)`; cut when it returns to 0 |
| Recursion signature | `helper(s, i) -> (node, i)` | `str2tree(substring)` |
| Finds the match by | never needing to — the callee leaves `i` past its own group | explicitly scanning for the balance-0 point |
| Time | **O(n)** — every char consumed once | O(n²) in Python (`s = s[1:]` copies the string each step) |
| Space | **O(h)** stack only | O(n) for the slices |
| Verdict | **the one to write** | easier to explain, fine for interviews, quadratic |

**Why the cursor must be returned (or made global)**: after building the left child, the parent has
no idea how many characters were eaten. Returning `i` is what threads that state back up. In Python
you'll see all three spellings — return a tuple, use `self.idx`, or a `nonlocal` — they are the
same trick.

**The two parsing traps** (both easy to miss, both tested by the input alphabet):
- **Multi-digit numbers** — `int(s[i])` is wrong; you must consume `while s[i].isdigit()`.
- **Negative numbers** — check `'-'` **before** the digit loop. Note `'-'` is unambiguous here:
  it can only be a sign, never subtraction, because it always follows `(` or the string start.

**Complexity**: `O(n)` time, `O(h)` space for the cursor version (`h` = tree height, `O(n)` in the
degenerate chain case).

#### Visual Trace (index cursor)

```text
        0 1 2 3 4 5 6 7 8 9 10 11 12 13 14 15
   s =  4 ( 2 ( 3 ) ( 1 ) )  (  6  (  5  )  )

parse(i=0)  num "4"        -> i=1
  s[1]=='(' -> i=2, recurse LEFT
  parse(i=2)  num "2"      -> i=3
    s[3]=='(' -> i=4, recurse LEFT
    parse(i=4)  num "3"    -> i=5   s[5]==')' -> no children, return (3, 5)
    i=5 -> skip ')' -> i=6
    s[6]=='(' -> i=7, recurse RIGHT
    parse(i=7)  num "1"    -> i=8   s[8]==')' -> return (1, 8)
    i=8 -> skip ')' -> i=9
  return (2, 9)
  i=9 -> skip ')' -> i=10
  s[10]=='(' -> i=11, recurse RIGHT
  parse(i=11) num "6"      -> i=12
    s[12]=='(' -> i=13, recurse LEFT
    parse(i=13) num "5"    -> i=14  return (5, 14)
    i=14 -> skip ')' -> i=15
  return (6, 15)
  i=15 -> skip ')' -> i=16 == len -> done
```

**Read the rhythm**: every child parse is sandwiched by `i += 1` (eat `(`) … `i += 1` (eat `)`).
Forgetting the trailing skip is the single most common bug — the parser then sees a stray `)` and
treats the node as childless.

> **Code**: the cursor implementation (Python **and** Java) plus the O(n²) slice variant live in
> [tree_codec.md](./tree_codec.md), section *3) Tree ⟷ String Codec Pattern*, where LC 536 sits next
> to its inverse LC 606. Not repeated here.

#### Pattern Summary

| Step | Code | Why |
|------|------|-----|
| Guard empty | `if not s: return None` | `""` is the empty tree |
| Parse sign | `if s[i] == '-': i += 1` | `-` only ever means "negative" here |
| Parse digits | `while s[i].isdigit(): i += 1` | values are multi-digit |
| Left child | `if s[i] == '(': i+=1; left, i = go(i); i+=1` | first group is **always** left |
| Right child | same block, repeated once | at most 2 groups |
| Return | `return node, i` | hands the cursor back to the parent |

**Generalised recursive-descent skeleton** (works for LC 394 / 726 / 1106 / 385 too):

```text
parse(i):
    consume the ATOM at i            # number, letter, literal
    while next char opens a group:   # '(' , '[' , '{'
        i += 1                       # eat the opener
        child, i = parse(i)          # recurse
        i += 1                       # eat the closer
    return built_node, i
```

#### Common Pitfalls

| Pitfall | Symptom | Fix |
|---------|---------|-----|
| `int(s[i])` for the value | `"42"` becomes node `4` with junk after | loop `while s[i].isdigit()` |
| Sign checked after digits | `-4` crashes or parses as `4` | check `'-'` **before** the digit loop |
| Missing `i += 1` after a child | stray `)` seen; right child silently dropped | eat the closer after every recursive call |
| Assuming a group could be the right child | wrong tree for `"4(2)"` | the first group is always **left** |
| `s = s[1:]` inside the loop (Python) | accepted but **O(n²)** | move a cursor instead of slicing |
| Treating `"()"` as an empty child | crash / phantom `0` node | not a legal input — `""` is the empty tree |

> **Note on the file's two `TODO: validate` variants**: I ran V0-1 (balance-scan + slice) and V0-2
> (cursor) against V0'/V2 on `"4(2(3)(1))(6(5))"`, `""`, `"42"`, `"-4"`, `"-4(2(-3))"`, `"4(2)"`,
> `"1(2(3(4)))"`, `"10(-20(30)(-40))(50)"`, `"0"` — **all four agree on every legal input**.
> They only diverge on the illegal `"4()(6)"` (cursor version raises, V0-2 invents a `0` node),
> which the problem statement explicitly rules out.

#### Similar LC

| LC # | Problem | Shared pattern | Key difference |
|------|---------|---------------|----------------|
| **536** | **Construct Binary Tree from String** | **recursive descent, cursor returned** | **parens delimit children** |
| 606 | Construct String from Binary Tree | **exact inverse** of 536 | tree → string; must keep `()` for a missing left child when a right exists |
| 1597 | Build Binary Expression Tree From Infix | Parenthesised recursive descent | operator precedence decides the root (see section 4) |
| 297 | Serialize and Deserialize Binary Tree | Pre-order + cursor | uses **null markers** (`#`) instead of parentheses |
| 449 | Serialize and Deserialize BST | Pre-order + cursor | BST bounds make markers unnecessary |
| 428 | Serialize and Deserialize N-ary Tree | Same descent | arbitrary child count → loop, not 2 `if`s |
| 105 / 106 | Construct from Preorder + Inorder | Build tree from a linear encoding | index ranges over **two** arrays (see section 2) |
| 331 | Verify Preorder Serialization | Consume a pre-order stream | validate only, no tree built (slot counting) |
| 394 | Decode String | `k[...]` nested groups | repetition instead of children |
| 385 | Mini Parser | Nested `[...]` descent | builds a NestedInteger, negatives too |
| 726 | Number of Atoms | Nested `(...)` + multiplier | multi-char atoms + counts to merge |
| 1106 | Parsing A Boolean Expression | `&(...)`, `|(...)`, `!(...)` | operator before the group, n-ary |
| 224 / 227 / 772 | Basic Calculator I / II / III | Same paren descent | precedence + evaluation, no tree kept |
| 20 | Valid Parentheses | The balance-counter primitive | just matching, the sub-step V0 uses |

### 4) Build Binary Expression Tree From Infix Expression — LC 1597
```python
# LC 1597 Build Binary Expression Tree From Infix Expression
# V0
# IDEA : LC 224 Basic Calculator
class Solution(object):

    def help(self, numSt, opSt):
        right = numSt.pop()
        left = numSt.pop()
        # Node(val=op, left=lhs, right=rhs)
        return Node(opSt.pop(), left, right)

    def expTree(self, s):
        # hashmap for operator ordering
        pr = {'*': 1, '/': 1, '+': 2, '-': 2, ')': 3, '(': 4}
        numSt = []
        opSt = []
        i = 0
        while i < len(s):
            c = s[i]
            i += 1
            # check if int(c) if string
            if c.isnumeric():
                numSt.append(Node(c))
            else:                
                if c == '(':
                    opSt.append('(')
                else:
                    while(len(opSt) > 0 and pr[c] >= pr[opSt[-1]]):
                        numSt.append(self.help(numSt, opSt))
                    if c == ')':
                        opSt.pop() # Now what remains is the closing bracket ')'
                    else:
                        opSt.append(c)
        while len(opSt) > 0:
            numSt.append(self.help(numSt, opSt))
        print (">>> numSt = {}, opSt = {}".format(str(numSt), opSt))
        return numSt.pop()

# V0'
# IDEA : RECURSIVE
class Solution:
    def expTree(self, s):
        n = len(s)
        if n == 1:
            return Node(s)

        fstOpIdx = None
        kets = 0
        for i in range(n-1, 0, -1):
            if s[i] == ")":
                kets += 1
            elif s[i] == "(":
                kets -= 1
            elif kets == 0:
                if s[i] in "+-":
                    fstOpIdx = i
                    break
                elif s[i] in "*/" and fstOpIdx is None:
                    fstOpIdx = i
        if fstOpIdx is None:
            return self.expTree(s[1:-1])
        rtNd = Node(s[fstOpIdx])
        rtNd.left = self.expTree(s[:fstOpIdx])
        rtNd.right = self.expTree(s[fstOpIdx+1:])
        return rtNd
```

## Summary

| Input | Root of the range | Recurse on | Complexity |
|---|---|---|---|
| preorder + inorder | `preorder[cursor++]` | the inorder index ranges left / right of the root | O(N) with a value → index map |
| an unsorted array | index of the max | `[lo, maxIdx-1]`, `[maxIdx+1, hi]` | O(N²) worst case, O(N) with a monotonic stack |
| a sorted array | the middle index | `[lo, mid-1]`, `[mid+1, hi]` | O(N) |
| `4(2(3)(1))(6(5))` | the digits before the first `(` | the two parenthesised groups, via one shared cursor | O(N) |
| `2-3*4` | last lowest-precedence operator outside parens | the substrings either side | O(N²) naive, O(N) with two stacks |

**The one habit that fixes all of them**: pass `(lo, hi)` index bounds or a shared cursor — never a
sliced copy of the input.
