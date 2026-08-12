# Tree Backtracking Template (gpt)

> **Scope** — Root→leaf **path** problems where the path itself is the state and must be undone on the way back up.
> **See also**: [tree.md](./tree.md) — general tree traversal; [binary_tree.md](./binary_tree.md) — DFS state-flow direction; [backtrack.md](./backtrack.md) — backtracking outside of trees.

Great — many **tree path problems** share the same DFS + backtracking structure. The following template works for problems like:

* LeetCode 113 Path Sum II
* LeetCode 257 Binary Tree Paths
* LeetCode 129 Sum Root to Leaf Numbers
* LeetCode 437 Path Sum III (slightly modified)

I'll show the **universal template**, then how each problem plugs into it.

---

## LeetCode Problem Lists

- [Tree](https://leetcode.com/problem-list/tree/)
- [Backtracking](https://leetcode.com/problem-list/backtracking/)
- [Depth-First Search](https://leetcode.com/problem-list/depth-first-search/)

## 1️⃣ Universal Tree Backtracking Template

```java
void dfs(TreeNode node, ...) {

    if (node == null) return;

    // 1. choose (update path / sum / state)
    
    // 2. check goal (usually leaf condition)
    
    // 3. explore children
    dfs(node.left, ...);
    dfs(node.right, ...);

    // 4. undo choice (backtrack)
}
```

For **path problems**, we typically maintain:

```text
List<Integer> path
some running state (sum / number / string)
```

---

## 2️⃣ Full Generic Template

```java
void dfs(TreeNode node, List<Integer> path, int state) {

    if (node == null) return;

    // choose
    path.add(node.val);
    state = update(state, node.val);

    // goal check
    if (isLeaf(node) && condition(state)) {
        save(path);
    }

    // explore
    dfs(node.left, path, state);
    dfs(node.right, path, state);

    // undo (backtrack)
    path.remove(path.size() - 1);
}
```

Key parts:

| Step    | Purpose             |
| ------- | ------------------- |
| choose  | add node to path    |
| check   | see if goal reached |
| explore | DFS children        |
| undo    | restore state       |

---

## 3️⃣ Apply to LC 113 (Path Sum II)

For LeetCode 113 Path Sum II:

State = **remaining sum**

```java
void dfs(TreeNode node, int remain, List<Integer> path) {

    if (node == null) return;

    path.add(node.val);

    if (node.left == null && node.right == null && remain == node.val) {
        res.add(new ArrayList<>(path));
    }

    dfs(node.left, remain - node.val, path);
    dfs(node.right, remain - node.val, path);

    path.remove(path.size() - 1);
}
```

---

## 4️⃣ Apply to LC 257 (Binary Tree Paths)

For LeetCode 257 Binary Tree Paths:

State = **path string**

```java
void dfs(TreeNode node, String path) {

    if (node == null) return;

    path += node.val;

    if (node.left == null && node.right == null) {
        res.add(path);
        return;
    }

    path += "->";

    dfs(node.left, path);
    dfs(node.right, path);
}
```

---

## 5️⃣ Apply to LC 129 (Root → Leaf Numbers)

For LeetCode 129 Sum Root to Leaf Numbers:

State = **number**

```text
123 means path: 1 → 2 → 3
```

```java
void dfs(TreeNode node, int num) {

    if (node == null) return;

    num = num * 10 + node.val;

    if (node.left == null && node.right == null) {
        total += num;
        return;
    }

    dfs(node.left, num);
    dfs(node.right, num);
}
```

---

## 6️⃣ Special Case: LC 437 (Path Sum III)

LeetCode 437 Path Sum III is different because paths **can start anywhere**, not only root.

So we run DFS **from every node**.

But the inner DFS still follows the same template.

---

## 7️⃣ Mental Model (Very Important)

Whenever you see:

```text
root → leaf
path
all paths
sum of paths
string of paths
```

Think immediately:

```text
DFS + Backtracking
```

Template:

```text
add node
check leaf
dfs children
remove node
```

---

## 8️⃣ Interview Cheat Code ⭐

Just memorize this **5-line skeleton**:

```java
path.add(node.val);

if (leaf condition)
    save result;

dfs(left)
dfs(right)

path.remove(path.size()-1);
```

You can solve **most root-to-leaf problems** with this.

---

✅ **Pro tip for interviews**

If a problem contains these words:

```text
all paths
root to leaf
return list of paths
```

It's almost **guaranteed DFS + backtracking**.

---

## 9️⃣ Quick Decision Table (which path template?)

| Goal on the path | Template | Undo step | Example |
|------------------|----------|-----------|---------|
| Collect every root→leaf path | push node / pop node | `path.remove(size-1)` | LC 113, LC 257 |
| Fold path into a scalar (sum / number) | pass state by value | *none needed* — value copy | LC 129 |
| Find the path down to **one** node | push, pop **only on failure** | pop when subtree returns false | **LC 236** |
| Count subpaths summing to target | prefix-sum counter on path | `count[cur] -= 1` | **LC 437** |
| Walk an **implicit** tree (numbers / text) | rebuild child from parent state | divide out / pop stack | **LC 386**, **LC 388** |

⭐⭐⭐⭐⭐ The only thing that changes between these is **what "path state" means** and **how you undo it**.

---

## 🔟 Template: Root→Node Path, Undo Only On Failure (LC 236)

**Key Idea**: the standard template pops **unconditionally** after exploring children.
When you are searching for **one specific node**, you pop **only if the subtree failed** —
so a successful search leaves the full root→target path sitting in `path`.

**Pattern**: build `path(root→p)` and `path(root→q)`, then the **last common prefix element is the LCA**.

```java
// java
// LC 236 - Lowest Common Ancestor of a Binary Tree
// IDEA: collect root->p and root->q paths with backtracking (pop on failure only),
//       then walk both paths together; last shared node = LCA.
class Solution {
    // time = O(N), space = O(H)  (H = tree height, O(N) worst case)
    public TreeNode lowestCommonAncestor(TreeNode root, TreeNode p, TreeNode q) {
        List<TreeNode> pathP = new ArrayList<>(), pathQ = new ArrayList<>();
        findPath(root, p, pathP);
        findPath(root, q, pathQ);

        TreeNode lca = null;
        for (int i = 0; i < Math.min(pathP.size(), pathQ.size()); i++) {
            if (pathP.get(i) != pathQ.get(i)) break;   // prefix ended
            lca = pathP.get(i);
        }
        return lca;
    }

    private boolean findPath(TreeNode node, TreeNode target, List<TreeNode> path) {
        if (node == null) return false;

        path.add(node);                                 // choose
        if (node == target) return true;                // goal -> KEEP the path

        if (findPath(node.left, target, path)) return true;
        if (findPath(node.right, target, path)) return true;

        path.remove(path.size() - 1);                   // undo ONLY on failure
        return false;
    }
}
```

```python
# python
# LC 236 - Lowest Common Ancestor of a Binary Tree
# IDEA: same backtracking, pop only when the subtree does not contain the target
class Solution(object):
    # time = O(N), space = O(H)
    def lowestCommonAncestor(self, root, p, q):
        def find_path(node, target, path):
            if not node:
                return False
            path.append(node)               # choose
            if node is target:
                return True                 # goal -> keep path
            if find_path(node.left, target, path) or find_path(node.right, target, path):
                return True
            path.pop()                      # undo ONLY on failure
            return False

        path_p, path_q = [], []
        find_path(root, p, path_p)
        find_path(root, q, path_q)

        lca = None
        for a, b in zip(path_p, path_q):
            if a is not b:
                break
            lca = a
        return lca
```

> **Why learn this over the famous 3-line post-order LCA?**
> The path version is the one that survives follow-ups: *"return the distance between p and q"*,
> *"print the path from p to q"*, *"there is no guarantee both nodes exist"* (check the boolean return).
> The 3-line version silently returns a node even when only one of p/q is present.

---

## 1️⃣1️⃣ Template: Prefix Sum **On The Path** + Undo (LC 437)

Section 6️⃣ noted LC 437 needs DFS from every node — that is `O(N^2)`.
The `O(N)` version is the **canonical "backtrack a HashMap instead of a list"** template.

**Key Idea**: treat the current root→node path as an array and apply the classic subarray-sum trick.
`count[prefix]` holds how many ancestors on the **current path** have that running sum.
The map is the path state, so it must be **undone on the way up**.

```java
// java
// LC 437 - Path Sum III
// IDEA: running sum along the path + HashMap<prefixSum, count>;
//       number of paths ending at node = count[cur - target]. Undo the map when leaving the node.
class Solution {
    // time = O(N), space = O(H)
    public int pathSum(TreeNode root, int targetSum) {
        Map<Long, Integer> prefix = new HashMap<>();
        prefix.put(0L, 1);                       // empty prefix = the path starts at root
        return dfs(root, 0L, targetSum, prefix);
    }

    private int dfs(TreeNode node, long cur, int target, Map<Long, Integer> prefix) {
        if (node == null) return 0;

        cur += node.val;                                       // choose
        int cnt = prefix.getOrDefault(cur - target, 0);        // goal check
        prefix.merge(cur, 1, Integer::sum);

        cnt += dfs(node.left, cur, target, prefix)             // explore
             + dfs(node.right, cur, target, prefix);

        prefix.merge(cur, -1, Integer::sum);                   // undo (backtrack)
        return cnt;
    }
}
```

```python
# python
# LC 437 - Path Sum III
# IDEA: prefix-sum counter over the current root->node path, decremented on the way back up
from collections import defaultdict

class Solution(object):
    # time = O(N), space = O(H)
    def pathSum(self, root, targetSum):
        prefix = defaultdict(int)
        prefix[0] = 1                       # empty prefix

        def dfs(node, cur):
            if not node:
                return 0
            cur += node.val                 # choose
            cnt = prefix[cur - targetSum]   # goal check
            prefix[cur] += 1

            cnt += dfs(node.left, cur) + dfs(node.right, cur)   # explore

            prefix[cur] -= 1                # undo (backtrack)
            return cnt

        return dfs(root, 0)
```

**Gotchas**
- `prefix[0] = 1` is what allows a valid path to **start at the root**.
- Use `long` in Java: node values reach `10^9` and paths can be `1000` deep.
- Forgetting the decrement counts paths that live in a **sibling** subtree → wrong answer, not a crash.

---

## 1️⃣2️⃣ Template: Backtracking on an **Implicit** Tree (LC 386)

Not every tree is made of `TreeNode`s. Numbers `1..n` in lexicographic order form a **10-ary trie**:
`1 → 10 → 100 …`, and lexicographic order is exactly its **preorder traversal**.

**Pattern**: child = `parent * 10 + d`. Prune when `child > n`.

```java
// java
// LC 386 - Lexicographical Numbers
// IDEA: preorder DFS over the implicit 10-ary trie of numbers; child = cur*10 + d
class Solution {
    // time = O(N), space = O(log N) recursion depth
    public List<Integer> lexicalOrder(int n) {
        List<Integer> res = new ArrayList<>();
        for (int i = 1; i <= 9; i++) dfs(i, n, res);   // 9 trie roots
        return res;
    }

    private void dfs(int cur, int n, List<Integer> res) {
        if (cur > n) return;
        res.add(cur);                                   // preorder: visit before children
        for (int d = 0; d <= 9; d++) {
            int next = cur * 10 + d;
            if (next > n) break;                        // prune (d only grows)
            dfs(next, n, res);
        }
    }
}
```

```python
# python
# LC 386 - Lexicographical Numbers
# IDEA: preorder DFS on the implicit 10-ary trie of 1..n
class Solution(object):
    # time = O(N), space = O(log N)
    def lexicalOrder(self, n):
        res = []

        def dfs(cur):
            if cur > n:
                return
            res.append(cur)
            for d in range(10):
                nxt = cur * 10 + d
                if nxt > n:
                    break
                dfs(nxt)

        for i in range(1, 10):
            dfs(i)
        return res
```

**Follow-up (`O(1)` extra space)** — the interview twist. Do the same walk **iteratively**;
"going back up the tree" becomes *divide by 10*:

```java
// java
// LC 386 - Lexicographical Numbers (O(1) space, explicit backtracking)
// IDEA: go down (*10) when possible, else move right (+1),
//       else climb (/10) until a right sibling exists.
class Solution {
    // time = O(N), space = O(1) excluding output
    public List<Integer> lexicalOrder(int n) {
        List<Integer> res = new ArrayList<>();
        int cur = 1;
        for (int i = 0; i < n; i++) {
            res.add(cur);
            if ((long) cur * 10 <= n) {
                cur *= 10;                                  // go deeper
            } else {
                while (cur % 10 == 9 || cur + 1 > n) cur /= 10;   // backtrack up
                cur++;                                      // next sibling
            }
        }
        return res;
    }
}
```

```python
# python
# LC 386 - Lexicographical Numbers (O(1) space, explicit backtracking)
class Solution(object):
    # time = O(N), space = O(1) excluding output
    def lexicalOrder(self, n):
        res, cur = [], 1
        for _ in range(n):
            res.append(cur)
            if cur * 10 <= n:
                cur *= 10                       # go deeper
            else:
                while cur % 10 == 9 or cur + 1 > n:
                    cur //= 10                  # backtrack up
                cur += 1                        # next sibling
        return res
```

---

## 1️⃣3️⃣ Template: Path-State **Stack** Keyed By Depth (LC 388)

An indented text block is a serialized tree; the tab count **is** the depth.
You never build the tree — you keep only the **current root→node path length**, and
moving to a shallower depth automatically discards (backtracks) the deeper entries.

**Key Idea**: `pathLen[d]` = length of the directory prefix at depth `d`.
Writing `pathLen[depth + 1]` while scanning top-down **overwrites** any stale sibling branch,
which is exactly the "pop" of the classic template.

```java
// java
// LC 388 - Longest Absolute File Path
// IDEA: implicit tree from indentation; pathLen[depth] = prefix length of current root->node path.
//       A shallower line overwrites the deeper entries => automatic backtrack.
class Solution {
    // time = O(N) over input chars, space = O(D) depth
    public int lengthLongestPath(String input) {
        Map<Integer, Integer> pathLen = new HashMap<>();
        pathLen.put(0, 0);                              // depth 0 has no prefix
        int best = 0;

        for (String line : input.split("\n")) {
            int depth = line.lastIndexOf('\t') + 1;     // tabs are always leading
            String name = line.substring(depth);

            if (name.contains(".")) {                   // leaf = file
                best = Math.max(best, pathLen.get(depth) + name.length());
            } else {                                    // internal node = dir
                pathLen.put(depth + 1, pathLen.get(depth) + name.length() + 1);  // +1 for '/'
            }
        }
        return best;
    }
}
```

```python
# python
# LC 388 - Longest Absolute File Path
# IDEA: depth-keyed path-length map; deeper stale entries are overwritten, never reused
class Solution(object):
    # time = O(N), space = O(D)
    def lengthLongestPath(self, input):
        best = 0
        path_len = {0: 0}

        for line in input.split('\n'):
            name = line.lstrip('\t')
            depth = len(line) - len(name)

            if '.' in name:                                        # file -> leaf
                best = max(best, path_len[depth] + len(name))
            else:                                                  # dir -> internal node
                path_len[depth + 1] = path_len[depth] + len(name) + 1   # +1 for '/'

        return best
```

**Gotchas**
- Return `0` when there is **no file** at all (`"a"` → `0`), not the longest directory path.
- The `+1` is the `/` separator; a file at depth `0` correctly gets no separator.
- A `Deque<Integer>` popping down to `size == depth` is the equivalent explicit-stack phrasing.

---

## 1️⃣4️⃣ Variation: State Flows **Down**, Structure Changes **Up** (LC 1110)

Same skeleton, but instead of pushing onto a path you pass a **boolean down** and
return a **rewired subtree up**.

> **Twist**: a node becomes a new forest root iff *(its parent was deleted or it is the tree root)* **and** *it is not itself deleted* —
> so the "state" you carry is just `parentWasDeleted`. Collect roots **pre-order**, rewire children **post-order**.

```java
// java
// LC 1110 - Delete Nodes And Return Forest
// IDEA: pass "is this node a forest root" down; return null upward for deleted nodes so the parent unlinks it.
class Solution {
    // time = O(N), space = O(H)
    public List<TreeNode> delNodes(TreeNode root, int[] to_delete) {
        Set<Integer> toDel = new HashSet<>();
        for (int v : to_delete) toDel.add(v);
        List<TreeNode> res = new ArrayList<>();
        dfs(root, true, toDel, res);
        return res;
    }

    private TreeNode dfs(TreeNode node, boolean isRoot, Set<Integer> toDel, List<TreeNode> res) {
        if (node == null) return null;

        boolean deleted = toDel.contains(node.val);
        if (isRoot && !deleted) res.add(node);                 // new forest root

        node.left  = dfs(node.left,  deleted, toDel, res);     // children are roots iff I'm deleted
        node.right = dfs(node.right, deleted, toDel, res);

        return deleted ? null : node;                          // unlink myself from my parent
    }
}
```

```python
# python
# LC 1110 - Delete Nodes And Return Forest
# IDEA: carry "parent was deleted" down, return None up so the parent drops the link
class Solution(object):
    # time = O(N), space = O(H)
    def delNodes(self, root, to_delete):
        to_del = set(to_delete)
        res = []

        def dfs(node, is_root):
            if not node:
                return None
            deleted = node.val in to_del
            if is_root and not deleted:
                res.append(node)                 # new forest root

            node.left = dfs(node.left, deleted)  # children become roots iff I'm deleted
            node.right = dfs(node.right, deleted)

            return None if deleted else node     # unlink myself

        dfs(root, True)
        return res
```

---

## 1️⃣5️⃣ Summary Table of Path Templates

| LC | Problem | Path state | Undo |
|----|---------|-----------|------|
| 113 | Path Sum II | `List<Integer>` + remaining sum | `path.remove(size-1)` |
| 257 | Binary Tree Paths | string (passed by value) | implicit |
| 129 | Sum Root to Leaf Numbers | `int num` (passed by value) | implicit |
| **236** | Lowest Common Ancestor | node list, pop on failure | conditional pop |
| **437** | Path Sum III | `Map<prefixSum, count>` | `count[cur] -= 1` |
| **386** | Lexicographical Numbers | current number | `cur /= 10` |
| **388** | Longest Absolute File Path | `pathLen[depth]` | overwrite / pop to depth |
| **1110** | Delete Nodes And Return Forest | `parentWasDeleted` flag | return `null` upward |
