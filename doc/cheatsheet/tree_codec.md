# Tree Serialization & String Codec

> **Scope** — Turning a tree into a string and back — subtree fingerprints for identity and duplicate detection, and the full encode/decode codec family (parenthesis, comma plus null marker, depth prefix).
> **See also**: [tree.md](./tree.md) — the traversal templates the encoders are built from; [tree_construction.md](./tree_construction.md) — building a tree from traversal arrays and index ranges; [tree_examples.md](./tree_examples.md) — the rest of the worked tree problems; [trie.md](./trie.md) — the prefix-tree structure LC 1948 hashes.

## LeetCode Problem Lists

- [Tree](https://leetcode.com/problem-list/tree/)
- [Binary Tree](https://leetcode.com/problem-list/binary-tree/)
- [String](https://leetcode.com/problem-list/string/)
- [Depth-First Search](https://leetcode.com/problem-list/depth-first-search/)

## Overview

Every "tree ⟷ string" problem is one half of a **codec**, and both halves are the same pre-order
recursion read in opposite directions. Two distinct jobs share that machinery:

- **Identity** — serialise a subtree into a key so two subtrees can be compared in O(1). Needs
  **post-order**, because the parent's key is built from its children's keys. LC 652, 572, 508.
- **Reconstruction** — serialise so the string can be parsed back into the same tree. Needs
  **pre-order** plus an unambiguous null representation. LC 297, 449, 606, 536, 1028.

### Key Properties
- **Complexity**: O(N) time and space with a `StringBuilder`/list-join and a single shared parse cursor; naive `+=` or per-call string slicing degrades to O(N²)
- **Core Idea**: `key(node) = f(node.val, key(left), key(right))` — the format changes, the recursion does not
- **When to Use**: subtree comparison, duplicate detection, or any round-trip tree ⟷ string
- **Non-negotiables**: an explicit null marker and a delimiter — without both, `1,2` is ambiguous

## Problem Categories

| Category | Traversal | Null representation | Examples |
|----------|-----------|--------------------|----------|
| **Subtree fingerprint** | post-order | `#` | LC 652, 572, 508, 250, 1948 |
| **Parenthesis codec** | pre-order | omitted pair, with the left-placeholder rule | LC 606, 536 |
| **Comma + marker codec** | pre-order | explicit `#` / `N` | LC 297, 449, 331 |
| **Depth-prefix codec** | pre-order | implied by the dash count | LC 1028 |
| **Two-traversal build** | — | absent — which is *why* a second array is needed | LC 105, 106 |

## Templates & Algorithms

### 1) Post-order DFS + Node Path Serialization Template (Java)

> Used when you need to **identify or compare subtrees** by structure + values.
> Inspired by LC 652 Find Duplicate Subtrees.

**Core Idea — Subtree Fingerprinting:**
```text
Serialize each subtree as a unique string: "val,left,right"
  → null nodes become "#" (marker) to preserve tree structure
  → Store in Map<String, Integer> to count occurrences
  → If count reaches 2, it's a duplicate → add to result
```

**Why Post-order?**
- Must know left and right subtree identities **before** building current node's string
- Children are processed first (bottom-up) → then combined at parent
- Pre-order would build the string before knowing children's structure

**Visual: Why pre-order fails for LC 652**
```text
Tree:        1
            / \
           2   2
          /     \
         4       4

Pre-order serialization (WRONG — builds string top-down):
  node 2 (left)  → "2,4,#"   ← built before knowing full subtree
  node 2 (right) → "2,#,4"   ← different string, but structurally different → OK here

Post-order serialization (CORRECT — builds string bottom-up):
  node 4 (left)  → "4,#,#"
  node 4 (right) → "4,#,#"   ← same! correctly identified as duplicate
  node 2 (left)  → "2,4,#,#,#"
  node 2 (right) → "2,#,4,#,#"  ← different (4 is left child vs right child)

Key insight: only post-order guarantees the full subtree structure is
captured before building the parent's key.
```

**Why include `#` for null?**
- Prevents ambiguity: `"1,2"` vs `"12"` — delimiter alone is not enough
- `"1,#,#"` vs `"1,2,#"` — null markers distinguish leaf from internal node

```java
// Template: Post-order DFS + Subtree Serialization (LC 652)
Map<String, Integer> pathMap = new HashMap<>();  // { serialized_string : count }
List<TreeNode> result = new ArrayList<>();

List<TreeNode> findDuplicateSubtrees(TreeNode root) {
    serialize(root);
    return result;
}

private String serialize(TreeNode node) {
    if (node == null) {
        return "#";  // null marker — preserves tree structure
    }

    // 1. Post-order: recurse into children FIRST
    String left  = serialize(node.left);
    String right = serialize(node.right);

    // 2. Build current subtree's unique identity
    //    Use delimiter to prevent "1,11" vs "11,1" ambiguity
    String key = node.val + "," + left + "," + right;

    // 3. Count occurrences
    int count = pathMap.getOrDefault(key, 0);

    // 4. Add to result ONLY when count == 1 (second occurrence = first duplicate)
    //    count == 1 means: this subtree appeared before, so current is a duplicate
    if (count == 1) {
        result.add(node);
    }

    // 5. Update count regardless
    pathMap.put(key, count + 1);

    // 6. Return serialization so parent can use it
    return key;
}
```

**Serialization format — why `val,left,right` works:**

```text
Tree:       1
           / \
          2   3
         /
        4

Serialization (post-order):
  node 4 → "4,#,#"
  node 2 → "2,4,#,#,#"    (val=2, left="4,#,#", right="#")
  node 3 → "3,#,#"
  node 1 → "1,2,4,#,#,#,3,#,#"
```

**Duplicate detection logic:**
```text
count == 0  → first time seen, just record
count == 1  → seen exactly once before → current is a DUPLICATE → add to result
count >= 2  → already recorded, skip (avoid adding same duplicate multiple times)
```

**Delimiter variants (all work, pick one and be consistent):**
```java
// All of these produce unambiguous serializations:
node.val + "," + left + "," + right   // V0, V1 in FindDuplicateSubtrees.java (recommended)
node.val + "$" + left + "$" + right   // V2 (alternative)
node.val + " "  + left + " "  + right // V3 (using space)
node.val + "#"  + left + "#"  + right // V4 (using # as delimiter)
// Avoid concatenating without delimiter: "112" is ambiguous (1+12 vs 11+2)
```

**Key Implementation Note — Count Logic Variants:**

Both of these counting approaches are equivalent and commonly seen:

```java
// Approach 1: Add when count == 1 (this subtree appeared before)
int count = pathMap.getOrDefault(key, 0);
if (count == 1) {
    result.add(node);  // 2nd occurrence → first duplicate
}
pathMap.put(key, count + 1);
```

```java
// Approach 2: Add when count == 2 (we just found second occurrence)
pathMap.put(key, pathMap.getOrDefault(key, 0) + 1);
if (pathMap.get(key) == 2) {
    result.add(node);  // Just became a duplicate
}
```

**Which to use?** Both are correct. Approach 1 is slightly cleaner (check before update), but Approach 2 is more intuitive (add after incrementing). Choose based on personal preference.

**Interview Trick (from LC 652):**
> If the problem asks to **identify/compare subtrees by structure**,
> use **Post-order DFS + serialize as `"val,left,right"` string + HashMap**.

**Pattern summary — 3 post-order DFS variants:**

| Pattern                           | Returns from DFS   | Map key            | Use case                          |
|-----------------------------------|--------------------|--------------------|------------------------------------|
| Height computation                | `int` (height)     | —                  | Depth, balance, diameter           |
| Subtree serialization + count     | `String` (serial)  | serialized string  | Duplicate subtrees (LC 652)        |
| Subtree sum / DP                  | `int` (result)     | —                  | Max path sum, subtree sum (LC 124) |
| Greedy multi-state                | `int` (state)      | —                  | Cameras, coloring, coverage (LC 968) |

**Similar LC Problems using post-order + subtree serialization:**

| LC #  | Problem                              | Pattern Variant                                                    | Difficulty |
|-------|--------------------------------------|--------------------------------------------------------------------|------------|
| 652   | Find Duplicate Subtrees              | Serialize → HashMap count; add node when count == 1               | Medium     |
| 572   | Subtree of Another Tree              | Serialize both trees; check if serialized `s` contains serialized `t` | Easy   |
| 508   | Most Frequent Subtree Sum            | Post-order compute subtree sum → HashMap freq → return max freq keys | Medium  |
| 250   | Count Univalue Subtrees              | Post-order: leaf OR (children univalue AND val matches parent)     | Medium     |
| 297   | Serialize and Deserialize Binary Tree| Pre/post-order encode tree as string, then reconstruct             | Hard       |
| 449   | Serialize and Deserialize BST        | Post-order serialization leveraging BST ordering property          | Medium     |
| 1948  | Delete Duplicate Folders in System   | Trie + post-order subtree hashing — advanced LC 652 variant        | Hard       |

**Implementation Style Variations:**

There are several equivalent ways to structure the solution:

```java
// Style 1: Instance variables (mutable state in class)
class Solution {
    Map<String, Integer> pathMap = new HashMap<>();
    List<TreeNode> result = new ArrayList<>();

    public List<TreeNode> findDuplicateSubtrees(TreeNode root) {
        serialize(root);
        return result;
    }

    private String serialize(TreeNode node) {
        // ... implementation ...
    }
}
```

```java
// Style 2: Local variables + pass through parameters
public List<TreeNode> findDuplicateSubtrees(TreeNode root) {
    Map<String, Integer> pathMap = new HashMap<>();
    List<TreeNode> result = new ArrayList<>();
    serialize(root, pathMap, result);
    return result;
}

private String serialize(TreeNode node, Map<String, Integer> pathMap, List<TreeNode> result) {
    // ... implementation ...
}
```

**Which style?** Style 1 (instance variables) is more common and cleaner for interviews. Style 2 is more functional. Both are equally valid.

---

**Why LC 652 specifically requires post-order:**
```text
Goal: build a unique "fingerprint" string for each subtree.

Pre-order (root → left → right):
  → builds the key at the ROOT first, before children are known
  → can't include children's serialized forms at build time
  → would require a separate recursive pass → O(N²) and messy

Post-order (left → right → root):
  → left child already returned its serialized string
  → right child already returned its serialized string
  → current key = val + "," + leftKey + "," + rightKey   ← O(1) to build
  → naturally bubbles the full subtree fingerprint up to the parent
  → one DFS pass is enough: O(N) time

Rule: if you need the COMPLETE subtree structure in the key, use post-order.
```

**Common Pitfalls:**
- ❌ Forgetting null markers → produces ambiguous strings like "1,2" (is it 1,2 or 12?)
- ❌ Checking `count == 0` instead of `count == 1` → adds every occurrence instead of just duplicates
- ❌ Checking `count == 2` and adding in same line → can add the same duplicate multiple times
- ✅ Always include null markers and delimiters for unambiguous serialization
- ✅ Add to result **only once** when count transitions from 0→1 or reaches exactly 2

### 2) Node Path Pattern - Subtree Serialization

The Node Path pattern serializes each subtree into a unique string for efficient comparison and duplicate detection.

#### **LC 652: Find Duplicate Subtrees**

> Java implementation: the template in section 1) above is exactly this solution.

```python
# python
# LC 652 Find Duplicate Subtrees
from collections import defaultdict

def findDuplicateSubtrees(root):
    """
    IDEA: Node Path Pattern

    Time: O(N²) worst case (string operations), O(N) with optimization
    Space: O(N) for HashMap and recursion
    """
    result = []
    path_count = defaultdict(int)

    def get_node_path(node):
        if not node:
            return "#"

        # Post-order: left → right → node
        left = get_node_path(node.left)
        right = get_node_path(node.right)

        # Build serialization string
        path = f"{node.val},{left},{right}"

        # Track frequency
        if path_count[path] == 1:
            result.append(node)
        path_count[path] += 1

        return path

    get_node_path(root)
    return result
```

#### **LC 572: Subtree of Another Tree**

```java
// java
// LC 572 Subtree of Another Tree
/**
 * IDEA: Node Path Pattern for Subtree Matching
 *
 * Approach 1: Serialize both trees and check if subRoot is substring of root
 * Approach 2: Traditional recursive comparison (shown below)
 */

// Method 1: Using Node Path Serialization
public boolean isSubtree(TreeNode root, TreeNode subRoot) {
    String rootPath = serialize(root);
    String subPath = serialize(subRoot);

    // NOTE !!! serialize() already wraps every node in commas, so subPath arrives
    //          delimited on both sides. Adding another pair searches for ",,...,,"
    //          and returns false even for root = [1], subRoot = [1].
    return rootPath.contains(subPath);
}

private String serialize(TreeNode node) {
    if (node == null) {
        return "#";
    }

    // Post-order serialization with delimiters
    String left = serialize(node.left);
    String right = serialize(node.right);

    // Add delimiters to prevent false matches (e.g., "12" in "123")
    return "," + node.val + "," + left + "," + right + ",";
}

// Method 2: Traditional Recursive Comparison
public boolean isSubtree_v2(TreeNode root, TreeNode subRoot) {
    if (root == null) {
        return false;
    }

    // Check if current tree matches OR check subtrees
    return isSameTree(root, subRoot) ||
           isSubtree_v2(root.left, subRoot) ||
           isSubtree_v2(root.right, subRoot);
}

private boolean isSameTree(TreeNode p, TreeNode q) {
    if (p == null && q == null) return true;
    if (p == null || q == null) return false;
    if (p.val != q.val) return false;

    return isSameTree(p.left, q.left) && isSameTree(p.right, q.right);
}
```

```python
# python
# LC 572 Subtree of Another Tree

# Method 1: Node Path Serialization
def isSubtree(root, subRoot):
    """
    Serialize both trees and check substring match

    Time: O(M + N) where M, N are tree sizes
    Space: O(M + N) for serialization strings
    """
    def serialize(node):
        if not node:
            return "#"

        # Post-order with delimiters
        left = serialize(node.left)
        right = serialize(node.right)

        return f",{node.val},{left},{right},"

    root_path = serialize(root)
    sub_path = serialize(subRoot)

    return sub_path in root_path

# Method 2: Traditional Recursive
def isSubtree_v2(root, subRoot):
    if not root:
        return False

    def is_same(p, q):
        if not p and not q:
            return True
        if not p or not q:
            return False
        return (p.val == q.val and
                is_same(p.left, q.left) and
                is_same(p.right, q.right))

    return (is_same(root, subRoot) or
            isSubtree_v2(root.left, subRoot) or
            isSubtree_v2(root.right, subRoot))
```

#### **Node Path Pattern Insights**

**When to use Node Path Pattern:**
- Finding duplicate or similar subtrees
- Checking if one tree is subtree of another
- Counting unique tree structures
- Tree pattern matching problems

**Key Implementation Points:**
1. **Post-order traversal**: Process children before parent
2. **Null markers**: Use "#" or "null" to represent null nodes
3. **Delimiters**: Use "," to separate values and prevent ambiguity
4. **HashMap tracking**: Store serialization → frequency mapping
5. **Early termination**: Add to result at count == 1 for duplicates

**Common Pitfalls:**
- ❌ Forgetting delimiters → "12" matches "1,2" incorrectly
- ❌ Not handling null nodes → different structures have same serialization
- ❌ Using pre-order → harder to build complete subtree representation
- ❌ Not caching serialization strings → O(N²) time complexity

**Complexity Analysis:**
- **Time**: O(N²) worst case (string concatenation), O(N) with StringBuilder
- **Space**: O(N) for HashMap and recursion stack, O(N²) for all paths

### 3) Tree ⟷ String Codec Pattern ⭐⭐⭐⭐⭐

**Key Idea**: every "tree ↔ string" problem is one half of a **codec**. Both halves are the same
recursion, run in opposite directions:

| Direction | Name | Recursion returns | Traversal | Template shape |
|-----------|------|-------------------|-----------|----------------|
| Tree → String | **encode** / serialize | the string of **this subtree** | pre-order (root first) | `f(node) = FMT(val, f(left), f(right))` |
| String → Tree | **decode** / deserialize | the node + **how much string it ate** | pre-order (root first) | `g(i) = (node(val), i')` — a recursive-descent parser |

```text
      encode                                    decode
   ┌──────────┐                              ┌──────────┐
   │  tree    │ ── "{}({})({})".format() ──► │  string  │
   │          │ ◄── parse val, then '(' ──── │          │
   └──────────┘                              └──────────┘
   returns str going UP                      returns (node, idx) going UP
```

**The one invariant**: `decode(encode(t)) == t`. That holds only if encoder and decoder agree on
**3 things** — and these 3 questions are the whole pattern:

1. **Delimiter** — how do I know where one value ends? (`,` / `(` `)` / `-` prefix)
2. **Null representation** — explicit marker (`#`, `null`) **or** structural nesting (parens)?
3. **Order** — pre-order / post-order / level-order (must match on both sides)

#### **0) Encode — the universal one-liner** 🎯

```python
# python — every tree→string problem is this, with a different FMT + NULL
def encode(node):
    if not node:
        return NULL          # "" for parens, "#" for comma-format
    return FMT.format(node.val, encode(node.left), encode(node.right))
```

| LC | Format | `FMT` | `NULL` | Example output |
|----|--------|-------|--------|----------------|
| **606** Construct String from Binary Tree | nested parens | `"{}({})({})"` | `""` (pairs omitted) | `1(2(4))(3)` |
| **536** Construct Binary Tree from String | *(same format, decode side)* | — | — | `4(2(3)(1))(6(5))` |
| **297** Serialize/Deserialize Binary Tree | comma pre-order | `"{},{},{}"` | `"#"` | `1,2,#,#,3,#,#` |
| **449** Serialize/Deserialize BST | comma pre-order | `"{},{},{}"` | `"#"` (or omit — BST order implies it) | `2,1,3` |
| **652 / 572** subtree identity key | comma **post-order** | `"{},{},{}"` (children first) | `"#"` | `4,#,#,2,#,#,1` |
| **1028** Recover a Tree From Preorder | depth-prefixed lines | `"-"*depth + str(val)` | omitted (depth implies shape) | `1-2--3--4-5--6--7` |
| **331** Verify Preorder Serialization | comma pre-order | *(validate only — never builds tree)* | `"#"` | `9,3,4,#,#,1,#,#,2,#,6,#,#` |

> **Why parens need no `#` but commas do**: nesting is *positional* — `(A)(B)` says "A is left, B is
> right" structurally. A flat comma list has no nesting, so a missing child must be spelled out,
> otherwise `1,2` could mean "2 is 1's left child" or "2 is 1's right child".

#### **1) LC 606 — the parenthesis format + the omission rule** ⭐⭐⭐⭐⭐

Full format is `val(left)(right)`. The problem says drop empty `()` pairs — **but not always**:

```text
 Case                      Full form        Emitted        Why
 ─────────────────────────────────────────────────────────────────────────────────
 leaf                      1()()            1              both pairs useless
 left only                 1(2)()           1(2)           trailing () carries no info
 right only  ⚠️            1()(3)           1()(3)         MUST keep the left placeholder!
 both                      1(2)(3)          1(2)(3)        nothing to drop
```

**The whole trick is row 3**: if you drop the empty left pair you emit `1(3)`, which decodes as
"3 is the **left** child" — the mapping stops being one-to-one. So the rule is:

> Keep the **left** pair whenever **any** child exists. Keep the **right** pair only when a right child exists.

That collapses the 4 cases into 2 `if`s:

```python
# python
# LC 606 - Construct String from Binary Tree  (2-rule form — preferred)
# IDEA: pre-order DFS returning a string; left pair kept if ANY child exists
# time = O(N) with a list/StringBuilder (O(N^2) with naive `+` on strings), space = O(H)
class Solution:
    def tree2str(self, root):
        if not root:
            return ""
        s = str(root.val)
        if root.left or root.right:          # left placeholder needed if ANY child
            s += "(" + self.tree2str(root.left) + ")"
        if root.right:                       # right only when it exists
            s += "(" + self.tree2str(root.right) + ")"
        return s
```

```python
# python
# LC 606 - same thing, written as the explicit `format` template (matches the FMT table above)
# time = O(N), space = O(H)
class Solution:
    def tree2str(self, root):
        if not root:
            return ""
        # Case 1: leaf -> bare value
        if not root.left and not root.right:
            return str(root.val)
        # Case 2: no right child -> omit the trailing ()
        if not root.right:
            return "{}({})".format(root.val, self.tree2str(root.left))
        # Case 3: right child exists -> left may render as the empty "()" placeholder
        return "{}({})({})".format(
            root.val,
            self.tree2str(root.left),
            self.tree2str(root.right)
        )
```

```java
// java
// LC 606 - Construct String from Binary Tree
// IDEA: pre-order DFS + StringBuilder (avoid O(N^2) string concat)
// time = O(N), space = O(H)
public String tree2str(TreeNode root) {
    StringBuilder sb = new StringBuilder();
    dfs(root, sb);
    return sb.toString();
}

private void dfs(TreeNode node, StringBuilder sb) {
    if (node == null) return;
    sb.append(node.val);
    if (node.left != null || node.right != null) {   // left placeholder rule
        sb.append('(');
        dfs(node.left, sb);
        sb.append(')');
    }
    if (node.right != null) {
        sb.append('(');
        dfs(node.right, sb);
        sb.append(')');
    }
}
```

**Visual trace** — `root = [1,2,3,4]`:

```text
        1              tree2str(4) = "4"                      (leaf)
       / \             tree2str(2) = "2" + "(4)"     = "2(4)"  (left only)
      2   3            tree2str(3) = "3"                      (leaf)
     /                 tree2str(1) = "1" + "(2(4))" + "(3)"
    4                              = "1(2(4))(3)"
```

`root = [1,2,3,null,4]` — the placeholder case:

```text
        1              tree2str(4) = "4"
       / \             tree2str(2) = "2" + "()" + "(4)" = "2()(4)"   ← left pair KEPT
      2   3                          ^^^^ empty, but required
       \
        4              tree2str(1) = "1(2()(4))(3)"
```

#### **2) LC 536 — decode the same format (recursive descent + index pointer)** ⭐⭐⭐⭐⭐

The decoder mirrors the encoder exactly: read a value, then read up to two parenthesised subtrees.
**Carry the cursor** so each call reports where it stopped — that keeps it O(N).

```python
# python
# LC 536 - Construct Binary Tree from String   (input: "4(2(3)(1))(6(5))")
# IDEA: recursive descent parser; helper returns (node, next_index)
# time = O(N)  each char consumed once
# space = O(H) recursion depth
class Solution(object):
    def str2tree(self, s):
        if not s:
            return None
        root, _ = self.helper(s, 0)
        return root

    def helper(self, s, idx):
        n = len(s)

        # 1) parse the value: optional '-' then digits (multi-digit!)
        sign = 1
        if s[idx] == '-':
            sign = -1
            idx += 1
        num = 0
        while idx < n and s[idx].isdigit():
            num = num * 10 + int(s[idx])
            idx += 1

        node = TreeNode(sign * num)

        # 2) first '(' -> LEFT subtree
        if idx < n and s[idx] == '(':
            node.left, idx = self.helper(s, idx + 1)   # +1 skips '('
            idx += 1                                   # skip the matching ')'

        # 3) second '(' -> RIGHT subtree
        if idx < n and s[idx] == '(':
            node.right, idx = self.helper(s, idx + 1)
            idx += 1

        return node, idx
```

```java
// java
// LC 536 - Construct Binary Tree from String
// IDEA: recursive descent; int[] idx acts as a by-reference cursor
// time = O(N), space = O(H)
public TreeNode str2tree(String s) {
    if (s == null || s.isEmpty()) return null;
    return helper(s, new int[]{0});
}

private TreeNode helper(String s, int[] idx) {
    // 1) value (handles '-' and multi-digit)
    int sign = 1;
    if (s.charAt(idx[0]) == '-') { sign = -1; idx[0]++; }
    int num = 0;
    while (idx[0] < s.length() && Character.isDigit(s.charAt(idx[0]))) {
        num = num * 10 + (s.charAt(idx[0]) - '0');
        idx[0]++;
    }
    TreeNode node = new TreeNode(sign * num);

    // 2) left
    if (idx[0] < s.length() && s.charAt(idx[0]) == '(') {
        idx[0]++;                       // skip '('
        node.left = helper(s, idx);
        idx[0]++;                       // skip ')'
    }
    // 3) right
    if (idx[0] < s.length() && s.charAt(idx[0]) == '(') {
        idx[0]++;
        node.right = helper(s, idx);
        idx[0]++;
    }
    return node;
}
```

**Alternative — balance scan + slicing** (easier to see, but O(N²) because of the slices):

```python
# python
# LC 536 - split on the '(' that closes at balance == 0
# time = O(N^2) (string slicing), space = O(N)
class Solution(object):
    def str2tree(self, s):
        if not s:
            return None

        first = s.find('(')
        if first == -1:                       # no children -> pure value
            return TreeNode(int(s))

        root = TreeNode(int(s[:first]))       # int() handles the '-' for us

        # find the ')' matching the FIRST '(' via parenthesis balance
        bal, left_end = 0, -1
        for i in range(first, len(s)):
            if s[i] == '(':
                bal += 1
            elif s[i] == ')':
                bal -= 1
            if bal == 0:
                left_end = i
                break

        root.left = self.str2tree(s[first + 1: left_end])   # inside 1st pair
        if left_end + 1 < len(s):
            root.right = self.str2tree(s[left_end + 2: -1]) # inside 2nd pair
        return root
```

> ⚠️ **606 output is not always legal 536 input.** LC 536 guarantees an empty tree is `""`, never
> `"()"` — so the `2()(4)` placeholder from LC 606 never appears. If you *do* want a true round-trip
> decoder, add one guard at the top of the parser:
> ```python
> if idx < n and s[idx] == ')':   # empty placeholder pair
>     return None, idx
> ```
> Without it, `helper` reads zero digits and happily builds a bogus `TreeNode(0)`.

#### **3) LC 297 — same recursion, comma format + explicit null** ⭐⭐⭐⭐⭐

```python
# python
# LC 297 - Serialize and Deserialize Binary Tree
# IDEA: pre-order with "#" for null; decode consumes the token stream via iter()
# time = O(N) both ways, space = O(N)
class Codec:
    def serialize(self, root):
        def dfs(node):
            if not node:
                return "#"
            # SAME shape as LC 606 -- only FMT and NULL changed
            return "{},{},{}".format(node.val, dfs(node.left), dfs(node.right))
        return dfs(root)

    def deserialize(self, data):
        it = iter(data.split(","))          # the cursor, for free
        def dfs():
            v = next(it)
            if v == "#":
                return None
            node = TreeNode(int(v))
            node.left = dfs()               # pre-order: left BEFORE right
            node.right = dfs()
            return node
        return dfs()
```

The Python `iter()` + `next()` trick is exactly the `idx` cursor from LC 536 — the iterator *is* the
index, so you don't have to thread it through the return value.

#### **4) LC 1028 — depth as the delimiter**

```python
# python
# LC 1028 - Recover a Tree From Preorder Traversal   ("1-2--3--4-5--6--7")
# IDEA: dashes = depth; stack holds the current root-to-node path
# time = O(N), space = O(H)
class Solution:
    def recoverFromPreorder(self, traversal):
        stack, i, n = [], 0, len(traversal)
        while i < n:
            depth = 0
            while traversal[i] == '-':      # 1) count dashes -> depth
                depth += 1
                i += 1
            j = i
            while j < n and traversal[j] != '-':
                j += 1
            node = TreeNode(int(traversal[i:j]))
            i = j

            while len(stack) > depth:       # 2) pop back up to this node's parent
                stack.pop()
            if stack:
                parent = stack[-1]
                if not parent.left:         # 3) pre-order -> left fills first
                    parent.left = node
                else:
                    parent.right = node
            stack.append(node)
        return stack[0] if stack else None
```

#### **Codec Pattern Insights** ⭐⭐⭐⭐⭐

**Encode checklist (tree → string)**
1. Base case returns the **null representation**, not `None` — `""` (parens) or `"#"` (comma).
2. Recurse first, combine after — the parent's string is built from the children's strings.
3. Use `StringBuilder` / list-join for O(N); naive `+=` in a loop is O(N²).
4. **Pre-order** for reconstruction (LC 297/606), **post-order** for identity keys (LC 652/572).

**Decode checklist (string → tree)**
1. Keep **one cursor** shared across all frames — `int[]` / instance field / `iter()`. Returning
   `(node, idx)` works too; slicing does not (O(N²)).
2. Parse the value with a `while isdigit()` loop — values are **multi-digit** and may be **negative**.
3. Consume the delimiters explicitly: `+1` past `(`, `+1` past `)`. Off-by-one here is the #1 bug.
4. Left before right, always — that's what makes it the inverse of a pre-order encode.

**Common Pitfalls**
- ❌ Dropping the empty left `()` in LC 606 → `1(3)` decodes as a left child (mapping broken)
- ❌ `int(s[i])` instead of a digit loop → `"12"` becomes value `1`
- ❌ Forgetting `'-'` → LC 536 explicitly allows negatives
- ❌ Re-slicing the string per recursive call → O(N²)
- ❌ No null marker in a flat/comma format → `1,2` is ambiguous (left vs right child)
- ❌ Pre-order-only **without** nulls can't be decoded at all — that's why LC 105/106 need a *second*
  traversal, while LC 297 gets away with one by spelling out the nulls

**Related problems**

| LC | Problem | Direction | Key difference |
|----|---------|-----------|----------------|
| 606 | Construct String from Binary Tree | tree → string | parens + omission rule (keep left placeholder) |
| 536 | Construct Binary Tree from String | string → tree | recursive descent, cursor, negative values |
| 297 | Serialize and Deserialize Binary Tree | both | comma + `#` null marker |
| 449 | Serialize and Deserialize BST | both | BST order lets you skip null markers |
| 331 | Verify Preorder Serialization | validate only | slot counting — never builds the tree |
| 652 | Find Duplicate Subtrees | tree → string | **post-order** key + HashMap (see section 2) |
| 572 | Subtree of Another Tree | tree → string | serialize both, substring check (see section 2) |
| 1028 | Recover a Tree From Preorder Traversal | string → tree | depth prefix + stack instead of parens |
| 105 / 106 | Construct Tree from 2 traversals | arrays → tree | needs a 2nd traversal *because* nulls are absent |


> The same encode/decode recursion, written out per traversal order.

#### Serialize binary tree with pre-order traverse
```java
// java
// algorithm book (labu) p.256

String SEP = ",";
String NULL = "#";

/* main func : serialize binary tree to string */
String serialize(TreeNode root){
    StringBuilder sb = new StringBuilder();
    serialize(root, sb);
    return sb.toString();
}

/* help func : put binary tree to StringBuilder */
void serialize(TreeNode root, StringBuilder sb){
    if (root == null){
        sb.append(NULL).append(SEP);
        return;
    }

    /********* pre-order traverse *********/
    sb.append(root.val).append(SEP);
    /**************************************/

    serialize(root.left, sb);
    serialize(root.right, sb);
}
``` 

#### Deserialize binary tree with pre-order traverse
```java
// java
// algorithm book (labu) p.256

String SEP = ",";
String NULL = "#";

/* main func : dserialize string to binary tree */
TreeNode deserlialize(String data){
    // transform string to linkedlist
    LinkedList<String> nodes = new LinkedList<>();
    for (String s: data.split(SEP)){
        nodes.addLast(s);
    }
    return deserlialize(nodes);
}


/* **** help func : build binary tree via linkedlist (nodes) */
TreeNode deserlialize(LinkedList<String> nodes){
    if (nodes.isEmpty()) return null;

    /********* pre-order traverse *********/
    // the 1st element on left is the root node of the binary tree
    String first = nodes.removeFirst();
    if (first.equals(NULL)) return null;
    TreeNode root = new TreeNode(Integer.parseInt(first));
    /**************************************/

    root.left = deserlialize(nodes);
    root.right = deserlialize(nodes);

    return root;
}
```

#### Serialize binary tree with post-order traverse
```java
// java
// algorithm book (labu) p.258

String SEP = ",";
String NULL = "#";

StringBuilder sb = new StringBuilder();

/* help func : pit binary tree to StringBuilder*/
void serialize(TreeNode root, StringBuilder sb){
    if (root == null){
        sb.append(NULL).append(SEP);
        return;
    }

    serialize(root.left, sb);
    serialize(root.right, sb);

    /********* post-order traverse *********/
    sb.append(root.val).append(SEP);
    /**************************************/
}
```

#### Deserialize binary tree with post-order traverse
```java
// java
// algorithm book (labu) p.260

/* main func : deserialize string to binary tree */
TreeNode deserlialize(String data){
    LinkedList<String> nodes = new LinkedList<>();
    for (String s : data.split(SEP)){
        nodes.addLast(s);
    }
    return deserlialize(nodes);
}

/* help func : build binary tree via linkedlist */
TreeNode deserlialize(LinkedList<String> nodes){
    if (nodes.isEmpty()) return null;
    // get element from last to beginning
    String last = nodes.removeLast();

    if (last.equals(NULL)) return null;
    TreeNode root = new TreeNode(Integer.parseInt(last));
    // build right sub tree first, then left sub tree
    root.right = deserlialize(nodes);
    root.left = deserlialize(nodes);

    return root;
}
```

#### Serialize binary tree with layer traverse
```java
// java
// layer traverse : https://github.com/yennanliu/CS_basics/blob/master/doc/cheatsheet/tree.md#1-1-basic-op
// algorithm book (labu) p.263
String SEP = ",";
String NULL = "#";

/* Serialize binary tree to string */
String serialize(TreeNode root){

    if (root == null) return "";
    StringBuilder sb = new StringBuilder();

    // init queue, put root into it
    Queue<TreeNode> q = new LinkedList<>();
    q.offer(root);

    while (!q.isEmpty()){
        TreeNode cur = q.poll();

        /***** layer traverse ******/
        if (cur == null){
            sb.append(NULL).append(SEP);
            continue;
        }
        sb.append(cur.val).append(SEP);
        /**************************/

        q.offer(cur.left);
        q.offer(cur.right);
    }
    return sb.toString();
}
```

#### Deserialize binary tree with layer traverse
```java
// java
// algorithm book (labu) p.264

String SEP = ",";
String NULL = "#";

/* Deserialize binary tree to string */
TreeNode deserlialize(String data){
    
    if (data.isEmpty()) return null;

    String[] nodes = data.split(SEP);

    // root's value = 1st element's value
    TreeNode root = new TreeNode(Integer.parseInt(nodes[0]));

    // queue records parent node, put root into queue
    Queue<TreeNode> q = new LinkedList<>();
    q.offer(root);

    for (int i = 1; i < nodes.length){

        // queue saves parent nodes
        TreeNode parent = q.poll();
        
        // parent node's left sub node
        String left = nodes[i++];
        if (!left.equals(NULL)){
            parent.left = new TreeNode(Integer.parseInt(left));
            q.offer(parent.left);
        }else {
            parent.left = null;
        }
        // parent node's right sub node
        String right = nodes[i++];
        if (!right.equals(NULL)){
            parent.right = new TreeNode(Integer.parseInt(right));
            q.offer(parent.right);
        }else{
            parent.right = null;
        }
    }

    return root;
}
```


#### Serialize and Deserialize Binary Tree

```java
// java
// LC 297
public class Codec{
    public String serialize(TreeNode root) {

        /** NOTE !!!
         *
         *     if root == null, return "#"
         */
        if (root == null){
            return "#";
        }

        /** NOTE !!! return result via pre-order, split with "," */
        return root.val + "," + serialize(root.left) + "," + serialize(root.right);
    }

    public TreeNode deserialize(String data) {

        /** NOTE !!!
         *
         *   1) init queue and append serialize output
         *   2) even use queue, but helper func still using DFS
         */
        Queue<String> queue = new LinkedList<>(Arrays.asList(data.split(",")));
        return helper(queue);
    }

    private TreeNode helper(Queue<String> queue) {

        // get val from queue first
        String s = queue.poll();

        if (s.equals("#")){
            return null;
        }
        /** NOTE !!! init current node  */
        TreeNode root = new TreeNode(Integer.valueOf(s));
        /** NOTE !!!
         *
         *    since serialize is "pre-order",
         *    deserialize we use "pre-order" as well
         *    e.g. root -> left sub tree -> right sub tree
         *    -> so we get sub tree via below :
         *
         *       root.left = helper(queue);
         *       root.right = helper(queue);
         *
         */
        root.left = helper(queue);
        root.right = helper(queue);
        /** NOTE !!! don't forget to return final deserialize result  */
        return root;
    }
}
```

## Summary

| You are asked to... | Traversal | Format |
|---|---|---|
| compare / count subtrees | post-order | `val,left,right` with `#`, hashed |
| round-trip a general binary tree | pre-order | comma + explicit null marker (LC 297) |
| round-trip a BST | pre-order | no markers needed — the bounds imply them (LC 449) |
| render the LC 606 parenthesis form | pre-order | keep the left pair whenever **any** child exists |
| parse a parenthesis form back | recursive descent | one shared cursor, digit loop, eat `(` and `)` |
| validate only, without building | pre-order | slot counting (LC 331) |
