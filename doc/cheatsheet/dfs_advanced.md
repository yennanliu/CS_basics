# DFS — Advanced Patterns

> **Scope** — The rare DFS tricks a first pass should skip: two-grid validation, edge-direction tracking, cross-component pair counting, trie-backed wildcard search, post-order distance buckets and N-ary rollups, and trees that arrive encoded as indentation or a `parent[]` array. Graph theory proper (Tarjan, Euler paths) is in graph_advanced.md.
> **See also** — *parent sheet*: [dfs.md](./dfs.md) — the ten core DFS templates, the recognition table and the pattern-selection flowchart; [dfs_examples.md](./dfs_examples.md) — the classic problems on those templates.
> *Neighbouring sheets*: [graph_advanced.md](./graph_advanced.md) — Tarjan low-link (bridges, SCC) and Hierholzer Euler paths; [trie.md](./trie.md) — the trie structure itself, without the branching query; [union_find.md](./union_find.md) — the DSU alternative to a connectivity DFS; [tree_codec.md](./tree_codec.md) — DFS that encodes a tree to a string and parses it back; [bfs.md](./bfs.md) — the breadth-first counterpart.

## LeetCode Problem Lists

- [Depth-First Search](https://leetcode.com/problem-list/depth-first-search/)
- [Graph Theory](https://leetcode.com/problem-list/graph/)
- [Trie](https://leetcode.com/problem-list/trie/)

## Overview

These DFS patterns appear **once** in an interview loop, not every round. Each is a core
template from [dfs.md](./dfs.md) plus one extra piece of bookkeeping — a second grid, a
direction flag, a running total, a branching trie query, a distance bucket, a depth-indexed
stack, a depth memo — and each is recognisable from the statement, because the statement names
that extra structure. Read dfs.md first; come here when a problem fits none of its ten templates.

Most inputs here reach n = 10⁵, where a recursive Python DFS passes the default 1,000-frame
limit on a path-shaped input. The templates below are written with an explicit stack wherever
that can happen.

### Prerequisites
- [dfs.md § Template 2](./dfs.md#template-2-graph--grid-dfs-flood-fill--lc-200-): flood fill.
  Templates 1–3 are flood fill carrying one more value.
- [dfs.md § Template 6](./dfs.md#template-6-bottom-up-post-order-dfs--lc-543-): bottom-up DFS
  that returns a value to the parent. Templates 5 and 6 return a bucket array or a min / max.
- [dfs.md § Template 1](./dfs.md#template-1-tree-traversal--lc-94-): the traversal Templates 7
  and 8 run on a tree that was never built.
- [graph.md § Template 1](./graph.md#template-1-connected-components-by-traversal--lc-547-):
  one traversal per component, which Template 3 counts across.

## Recognition

| Signal in the statement | Constraint cue | Reach for | Not this if |
|---|---|---|---|
| "islands in `grid2` that are also land in `grid1`", "sub-islands" | m, n ≤ 500 | [Template 1](#template-1-flood-fill-with-a-validity-flag--lc-1905) — flood fill + a flag | one grid only → [dfs.md § Template 2](./dfs.md#template-2-graph--grid-dfs-flood-fill--lc-200-) |
| "minimum edges to reverse so every city reaches 0" | tree, n ≤ 5·10⁴ | [Template 2](#template-2-direction-flag-on-an-undirected-walk--lc-1466) — undirected walk, flagged edges | the answer is wanted for **every** root → rerooting DP (LC 2858) |
| "pairs of nodes that cannot reach each other" | n ≤ 10⁵ | [Template 3](#template-3-cross-component-pair-counting--lc-2316) — sizes × running total | pairs **within** a component → Σ size·(size − 1)/2 |
| "`.` matches any letter", "exactly one character different" | words ≤ 25 letters, ≤ 10⁴ calls | [Template 4](#template-4-trie-dfs-with-a-branching-query--lc-211) — trie, search branches | no wildcard → plain trie walk ([trie.md](./trie.md)) |
| "pairs of leaves whose distance ≤ d" | d ≤ 10 | [Template 5](#template-5-post-order-distance-buckets--lc-1530) — bucket array per node | distance unbounded → pairs through each LCA cannot be bucketed |
| tree given as `edges` rooted at 0, value from children's min / max | n ≤ 10⁵ | [Template 6](#template-6-n-ary-post-order-rollup--lc-3965) — N-ary rollup | `TreeNode` with `.left / .right` → [dfs.md § Template 6](./dfs.md#template-6-bottom-up-post-order-dfs--lc-543-) |
| tab-indented text, `/`-separated paths | input ≤ 10⁴ chars | [Template 7](#template-7-depth-indexed-stack--lc-388) — `stack[depth]` | the nesting is brackets → a parse stack ([stack.md](./stack.md)) |
| input is `parent[]` / `manager[]` with `-1` at the root, answer needs depth | n ≤ 10⁵ | [Template 8](#template-8-memoized-climb-on-a-parent-array--lc-4015) — memoized climb | subtree sums wanted → invert to children, then post-order |
| "which connection is critical", "use every ticket exactly once" | V ≤ 10⁵ | Tarjan / Hierholzer — [graph_advanced.md](./graph_advanced.md) | — |
| a tree to a string and back | n ≤ 10⁴ | [tree_codec.md](./tree_codec.md) | — |

## Grid and Graph Bookkeeping

### Template 1: Flood Fill with a Validity Flag — LC 1905

**Recognise** — two grids of the same shape; "count the islands of `grid2` that are entirely land
in `grid1`" (sub-islands), or any "component counts only if every cell passes a test". m, n ≤ 500,
so an island can hold 250,000 cells.

**Key point** — flood the whole island of `grid2` and AND every cell's test into one flag. The
island must be flooded to the end even after the flag goes false: a cell left unflooded would
start a "new" island later and be counted on its own.

```python
# python
# LC 1905 - Count Sub Islands
# IDEA: flood each island of grid2 with an explicit stack; it counts if every cell is land in grid1
# time = O(m * n), space = O(m * n)
def countSubIslands(grid1, grid2):
    rows, cols = len(grid2), len(grid2[0])
    count = 0
    for r in range(rows):
        for c in range(cols):
            if grid2[r][c] != 1:
                continue
            is_sub = True
            grid2[r][c] = 0                    # sink on push: each cell enters the stack once
            stack = [(r, c)]
            while stack:
                y, x = stack.pop()
                if grid1[y][x] == 0:
                    is_sub = False             # keep flooding: the rest of the island must sink too
                for dy, dx in ((1, 0), (-1, 0), (0, 1), (0, -1)):
                    ny, nx = y + dy, x + dx
                    if 0 <= ny < rows and 0 <= nx < cols and grid2[ny][nx] == 1:
                        grid2[ny][nx] = 0
                        stack.append((ny, nx))
            if is_sub:
                count += 1
    return count
```

```java
// java
// LC 1905 - Count Sub Islands
// IDEA: flood each island of grid2 with an explicit stack; it counts if every cell is land in grid1
// time = O(m * n), space = O(m * n)
public int countSubIslands(int[][] grid1, int[][] grid2) {
    int rows = grid2.length, cols = grid2[0].length;
    int[][] dirs = {{1, 0}, {-1, 0}, {0, 1}, {0, -1}};
    int count = 0;
    for (int r = 0; r < rows; r++) {
        for (int c = 0; c < cols; c++) {
            if (grid2[r][c] != 1) {
                continue;
            }
            boolean isSub = true;
            grid2[r][c] = 0;
            Deque<int[]> stack = new ArrayDeque<>();
            stack.push(new int[]{r, c});
            while (!stack.isEmpty()) {
                int[] cell = stack.pop();
                if (grid1[cell[0]][cell[1]] == 0) {
                    isSub = false;                 // keep flooding
                }
                for (int[] d : dirs) {
                    int ny = cell[0] + d[0], nx = cell[1] + d[1];
                    if (ny >= 0 && ny < rows && nx >= 0 && nx < cols && grid2[ny][nx] == 1) {
                        grid2[ny][nx] = 0;
                        stack.push(new int[]{ny, nx});
                    }
                }
            }
            if (isSub) {
                count++;
            }
        }
    }
    return count;
}
```

**Complexity**
- **Time O(m·n)**: a cell is set to 0 before it is pushed, so the `while stack` loop pops each
  land cell once and looks at 4 neighbours.
- **Space O(m·n)**: the stack can hold a whole island.

**Traps**
- The recursive form written `return dfs(up) and dfs(down) and ...` short-circuits on the first
  false and leaves the rest of the island standing; those cells are later counted as a separate
  island. Recursively it must be `ok = dfs(nbr) and ok` (call first), for all four.
- A recursive flood is 250,000 frames deep on a full grid: past Python's default limit and
  likely past Java's default thread stack. The explicit stack has no limit.
- The input is mutated (`grid2` is sunk). Copy it first if the caller needs it.

**Drill** — LC 1905 ⭐ · LC 1254 *(the test is "never touches the border": [dfs.md § Template 7](./dfs.md#template-7-2-pass-dfs-boundary-elimination--lc-1254))* · LC 827 *(label each island with its size, then try each 0)*

### Template 2: Direction Flag on an Undirected Walk — LC 1466

**Recognise** — a tree of one-way roads; "the minimum number of roads to reverse so every city
can reach city 0". n ≤ 5·10⁴, n − 1 edges.

**Key point** — store every road in both directions, marked `1` if it is the original direction
and `0` if it is the reverse. Walk outward from city 0: each time the walk crosses a road
**forwards** (flag `1`), that road points away from 0 and must be reversed. In a tree each road
is crossed exactly once, so the count is exact.

```python
# python
# LC 1466 - Reorder Routes to Make All Paths Lead to the City Zero
# IDEA: undirected walk from 0; an edge crossed in its original direction must be reversed
# time = O(n), space = O(n)
def minReorder(n, connections):
    graph = [[] for _ in range(n)]
    for a, b in connections:
        graph[a].append((b, 1))      # a -> b as given: points away from 0 if walked from a
        graph[b].append((a, 0))      # the reverse direction costs nothing

    seen = [False] * n
    seen[0] = True
    stack = [0]
    reversals = 0
    while stack:
        city = stack.pop()
        for nxt, original in graph[city]:
            if not seen[nxt]:
                seen[nxt] = True
                reversals += original    # walked away from 0 along the original direction
                stack.append(nxt)
    return reversals
```

```java
// java
// LC 1466 - Reorder Routes to Make All Paths Lead to the City Zero
// IDEA: undirected walk from 0; an edge crossed in its original direction must be reversed
// time = O(n), space = O(n)
public int minReorder(int n, int[][] connections) {
    List<List<int[]>> graph = new ArrayList<>();
    for (int i = 0; i < n; i++) {
        graph.add(new ArrayList<>());
    }
    for (int[] c : connections) {
        graph.get(c[0]).add(new int[]{c[1], 1});
        graph.get(c[1]).add(new int[]{c[0], 0});
    }
    boolean[] seen = new boolean[n];
    seen[0] = true;
    Deque<Integer> stack = new ArrayDeque<>();
    stack.push(0);
    int reversals = 0;
    while (!stack.isEmpty()) {
        int city = stack.pop();
        for (int[] edge : graph.get(city)) {
            if (!seen[edge[0]]) {
                seen[edge[0]] = true;
                reversals += edge[1];
                stack.push(edge[0]);
            }
        }
    }
    return reversals;
}
```

**Complexity**
- **Time O(n)**: each city is pushed once (`seen` is set on push) and its two-way list is read
  once; there are 2(n − 1) entries in total.
- **Space O(n)**: the doubled adjacency list, `seen` and the stack.

**Dry run** — `n = 6`, roads `0→1, 1→3, 2→3, 4→0, 4→5`:

```text
pop  crosses           flag  reversals
0    0->1 (original)    1    1
0    0->4 (reverse)     0    1
4    4->5 (original)    1    2
1    1->3 (original)    1    3
3    3->2 (reverse)     0    3
answer 3
```

**Traps**
- Walking only the original directed edges from 0 reaches nothing on `[[1,0],[2,0]]`; the
  graph must be stored both ways.
- Counting flag-`0` edges instead gives n − 1 − answer. Fix which direction the flag marks
  before writing the loop.

**Not this if** — the question asks the reversal count **for every possible root** (LC 2858):
compute it for root 0 with this template, then reroot, moving the root across an edge changes
the count by ±1.

**Drill** — LC 1466 ⭐ · LC 2858 *(every root: rerooting DP)*

### Template 3: Cross-Component Pair Counting — LC 2316

**Recognise** — "the number of pairs of nodes that are unreachable from each other",
"pairs in different groups". n ≤ 10⁵ rules out checking pairs; only component sizes matter.

**Key point** — every node of a new component pairs with every node of the components already
seen, so add `size × seen_so_far` and then grow `seen_so_far`. Each cross pair is counted once,
when its second component is found.

```python
# python
# LC 2316 - Count Unreachable Pairs of Nodes in an Undirected Graph
# IDEA: component sizes by flood fill; each new component pairs with every node seen before it
# time = O(n + E), space = O(n + E)
def countPairs(n, edges):
    graph = [[] for _ in range(n)]
    for a, b in edges:
        graph[a].append(b)
        graph[b].append(a)

    seen = [False] * n
    pairs = 0
    seen_so_far = 0                 # nodes in the components already counted
    for start in range(n):
        if seen[start]:
            continue
        seen[start] = True
        stack = [start]
        size = 0
        while stack:
            node = stack.pop()
            size += 1
            for nxt in graph[node]:
                if not seen[nxt]:
                    seen[nxt] = True
                    stack.append(nxt)
        pairs += size * seen_so_far
        seen_so_far += size
    return pairs
```

```java
// java
// LC 2316 - Count Unreachable Pairs of Nodes in an Undirected Graph
// IDEA: component sizes by flood fill; each new component pairs with every node seen before it
// time = O(n + E), space = O(n + E)
public long countPairs(int n, int[][] edges) {
    List<List<Integer>> graph = new ArrayList<>();
    for (int i = 0; i < n; i++) {
        graph.add(new ArrayList<>());
    }
    for (int[] e : edges) {
        graph.get(e[0]).add(e[1]);
        graph.get(e[1]).add(e[0]);
    }
    boolean[] seen = new boolean[n];
    long pairs = 0, seenSoFar = 0;           // long: up to ~5 * 10^9 pairs
    for (int start = 0; start < n; start++) {
        if (seen[start]) {
            continue;
        }
        seen[start] = true;
        Deque<Integer> stack = new ArrayDeque<>();
        stack.push(start);
        long size = 0;
        while (!stack.isEmpty()) {
            int node = stack.pop();
            size++;
            for (int nxt : graph.get(node)) {
                if (!seen[nxt]) {
                    seen[nxt] = true;
                    stack.push(nxt);
                }
            }
        }
        pairs += size * seenSoFar;
        seenSoFar += size;
    }
    return pairs;
}
```

**Complexity**
- **Time O(n + E)**: the outer `for start` launches one flood per component, and every node is
  pushed once.
- **Space O(n + E)**: the adjacency list, `seen` and the stack.

**Traps**
- `int` overflows in Java: with no edges and n = 10⁵ the answer is about 5·10⁹. Keep `pairs`,
  `size` and `seenSoFar` as `long` — `size * seenSoFar` with two `int`s overflows before it is
  widened.
- `size × (n − size)` summed over components counts every pair twice; halve it, or use the
  running total.
- Union-find gives the same sizes (count roots after all unions) and is the better fit when
  edges arrive one at a time.

**Drill** — LC 2316 ⭐ · LC 323 *(just the component count)* · LC 2492 *(minimum edge inside the
component of node 1)*

## Trie-Backed Search

### Template 4: Trie DFS with a Branching Query — LC 211

**Recognise** — a word dictionary with "`.` matches any letter", "one character different",
"magic dictionary". Insert is a plain trie loop; only search branches. Words ≤ 25 letters, at
most 2 dots per query on LC 211.

**Key point** — `dfs(node, i)` answers "can `word[i:]` be matched from this trie node". A letter
follows one child; a `.` tries every child and returns on the first success. At the end of the
query the answer is whether a word **ends** at that node, not `True`.

```python
# python
# LC 211 - Design Add and Search Words Data Structure
# IDEA: trie; a '.' in the query branches the DFS into every child, a letter follows one
# time = O(L) per addWord; search O(L) without '.', O(26^d * L) with d dots, space = O(total chars)
class WordDictionary:
    def __init__(self):
        self.root = {}

    def addWord(self, word):
        node = self.root
        for ch in word:
            if ch not in node:
                node[ch] = {}
            node = node[ch]
        node['$'] = True                       # end-of-word marker

    def search(self, word):
        def dfs(node, i):
            if i == len(word):
                return '$' in node             # a word must END here
            ch = word[i]
            if ch == '.':
                for key, child in node.items():
                    if key != '$' and dfs(child, i + 1):
                        return True            # first match is enough
                return False
            if ch not in node:
                return False
            return dfs(node[ch], i + 1)

        return dfs(self.root, 0)
```

```java
// java
// LC 211 - Design Add and Search Words Data Structure
// IDEA: trie; a '.' in the query branches the DFS into every child, a letter follows one
// time = O(L) per addWord; search O(L) without '.', O(26^d * L) with d dots, space = O(total chars)
class WordDictionary {
    private final WordDictionary[] children = new WordDictionary[26];
    private boolean isWord = false;

    public void addWord(String word) {
        WordDictionary node = this;
        for (char c : word.toCharArray()) {
            if (node.children[c - 'a'] == null) {
                node.children[c - 'a'] = new WordDictionary();
            }
            node = node.children[c - 'a'];
        }
        node.isWord = true;
    }

    public boolean search(String word) {
        return dfs(word, 0, this);
    }

    private boolean dfs(String word, int i, WordDictionary node) {
        if (node == null) {
            return false;
        }
        if (i == word.length()) {
            return node.isWord;                       // must end a word, not just be a prefix
        }
        char c = word.charAt(i);
        if (c == '.') {
            for (WordDictionary child : node.children) {
                if (dfs(word, i + 1, child)) {
                    return true;
                }
            }
            return false;
        }
        return dfs(word, i + 1, node.children[c - 'a']);
    }
}
```

**Complexity**
- **Time**: `addWord` walks L letters. `search` follows one child per letter, but each `.` runs
  the `for key, child` loop over up to 26 children, so d dots cost O(26^d · L) in the worst case.
- **Space O(total chars)**: one trie node per distinct prefix; recursion depth L.

**Traps**
- Returning `True` at the end of the query makes `"ba"` match the prefix of `"bad"`. Check the
  end marker.
- With a dict trie, the `'$'` marker is one of the keys: skip it when a `.` iterates children,
  or the DFS recurses into `True`.
- Not returning on the first successful branch turns the 26^d worst case into every case.

**Not this if** — the query has no wildcard: that is a plain trie walk ([trie.md](./trie.md)),
or just a hash set of words.

**Drill** — LC 211 ⭐ · LC 208 *(the plain trie)* · LC 676 *(below)* · LC 212 *(trie driving a
grid DFS)*

### Implement Magic Dictionary — LC 676

> **Key point** — instead of a wildcard at a known position, carry a **mismatch budget** of 1
> down the trie. A child whose letter differs from the query spends it; at the end of the query
> the budget must be exactly 0, because an unchanged word does not count.

**Reduces to**: [Template 4](#template-4-trie-dfs-with-a-branching-query--lc-211) — the branch is
"every child, paying 1 if its letter differs" instead of "every child on a dot".

```python
# python
# LC 676 - Implement Magic Dictionary
# IDEA: trie DFS carrying a mismatch budget of 1; it must be fully spent at the word's end
# time = O(26 * L^2) per search, space = O(total chars)
class MagicDictionary:
    def __init__(self):
        self.root = {}

    def buildDict(self, dictionary):
        for word in dictionary:
            node = self.root
            for ch in word:
                if ch not in node:
                    node[ch] = {}
                node = node[ch]
            node['$'] = True

    def search(self, searchWord):
        def dfs(node, i, budget):
            if i == len(searchWord):
                return budget == 0 and '$' in node     # exactly one change, and a whole word
            for ch, child in node.items():
                if ch == '$':
                    continue
                cost = 0 if ch == searchWord[i] else 1
                if cost <= budget and dfs(child, i + 1, budget - cost):
                    return True
            return False

        return dfs(self.root, 0, 1)
```

**Traps**
- `budget <= 1` at the end instead of `== 0` accepts the dictionary word itself:
  `search("hello")` must be `False` when `"hello"` is in the dictionary.
- Once the budget is spent the DFS follows matching letters only, so a search has at most
  26 · L places to spend it, each followed by one path of at most L steps: O(26 · L²), not
  exponential.

## Post-Order Aggregation

### Template 5: Post-Order Distance Buckets — LC 1530

**Recognise** — "pairs of leaves whose shortest path is at most `distance`", with a small bound
(`distance ≤ 10`) and a tree of up to 2¹⁰ nodes.

**Key point** — each node returns `cnt[d]` = the number of leaves exactly `d` edges below it. A
left leaf at depth `d1` and a right leaf at depth `d2` are `d1 + d2 + 2` apart through this node,
and this node is their lowest common ancestor, so every pair is counted exactly once — here.

```python
# python
# LC 1530 - Number of Good Leaf Nodes Pairs
# IDEA: post-order; each node returns leaf counts by depth below it and pairs left x right at the LCA
# time = O(N * distance^2), space = O(N + distance * height)
def countPairs(root, distance):
    pairs = [0]

    def leaves_by_depth(node):
        cnt = [0] * (distance + 1)        # cnt[d] = leaves exactly d edges below node
        if node is None:
            return cnt
        if node.left is None and node.right is None:
            cnt[0] = 1                    # a leaf is 0 edges from itself
            return cnt
        left = leaves_by_depth(node.left)
        right = leaves_by_depth(node.right)
        for d1 in range(distance + 1):    # join a left leaf and a right leaf through node
            for d2 in range(distance + 1):
                if d1 + d2 + 2 <= distance:
                    pairs[0] += left[d1] * right[d2]
        for d in range(distance):         # one edge farther for the parent; deeper leaves drop off
            cnt[d + 1] = left[d] + right[d]
        return cnt

    leaves_by_depth(root)
    return pairs[0]
```

```java
// java
// LC 1530 - Number of Good Leaf Nodes Pairs
// IDEA: post-order; each node returns leaf counts by depth below it and pairs left x right at the LCA
// time = O(N * distance^2), space = O(N + distance * height)
class Solution {
    private int pairs = 0;

    public int countPairs(TreeNode root, int distance) {
        pairs = 0;                                        // a reused Solution must not carry counts over
        leavesByDepth(root, distance);
        return pairs;
    }

    // cnt[d] = number of leaves exactly d edges below node
    private int[] leavesByDepth(TreeNode node, int distance) {
        int[] cnt = new int[distance + 1];
        if (node == null) return cnt;
        if (node.left == null && node.right == null) {
            cnt[0] = 1;                                   // a leaf is 0 edges from itself
            return cnt;
        }
        int[] left = leavesByDepth(node.left, distance);
        int[] right = leavesByDepth(node.right, distance);
        for (int d1 = 0; d1 <= distance; d1++) {          // join a left leaf and a right leaf through node
            for (int d2 = 0; d2 <= distance; d2++) {
                if (d1 + d2 + 2 <= distance) pairs += left[d1] * right[d2];
            }
        }
        for (int d = 0; d < distance; d++) {              // one edge farther for the parent
            cnt[d + 1] = left[d] + right[d];
        }
        return cnt;
    }
}
```

**Complexity**
- **Time O(N·distance²)**: the `d1` / `d2` double loop at each of the N nodes. A running prefix
  sum over `right` makes the join O(distance), for O(N·distance) in total.
- **Space O(N + distance·height)**: recursion depth plus one bucket per open frame.

**Traps**
- The bucket shift drops leaves deeper than `distance` (`range(distance)`, not
  `range(distance + 1)`); writing `cnt[d + 1]` for `d = distance` is out of range.
- Joining at every ancestor instead of only through the left / right split double-counts; the
  pair belongs to its LCA alone.

**Not this if** — the bound on distance is large: per-node buckets become O(N) each. Converting
the tree to a graph and running a bounded BFS from every leaf is the slower fallback (O(L·N)),
and it is not worked anywhere in these sheets.

**Drill** — LC 1530 · LC 543 *(join depths at each node)* · LC 124 *(join best downward sums)* · LC 687 ·
LC 1530 ⭐

### Template 6: N-ary Post-Order Rollup — LC 3965

**Recognise** — a tree given as `edges` with "`u` is the parent of `v`", rooted at 0; a node's
value is a formula over its children's values (min, max, sum) plus its own weight; only the root's
value is asked. n ≤ 10⁵.

**Key point** — a node can be computed once all its children are. Popping the DFS order in
reverse visits every child before its parent, so a single array of finished values replaces the
recursion. LC 3965's formula: `own = (latest − earliest) + baseTime[node]`,
`finish = latest + own`, with `latest` / `earliest` over the children.

```python
# python
# LC 3965 - Finish Time of Tasks I
# IDEA: build children lists; compute nodes in reverse DFS pre-order so children finish first
# time = O(n), space = O(n)
def finishTime(n, edges, baseTime):
    children = [[] for _ in range(n)]
    for parent, child in edges:              # directed: parent -> child
        children[parent].append(child)

    order = []                               # DFS pre-order from the root
    stack = [0]
    while stack:
        node = stack.pop()
        order.append(node)
        for child in children[node]:
            stack.append(child)

    finish = [0] * n
    for node in reversed(order):             # every child appears after its parent in `order`
        if not children[node]:
            finish[node] = baseTime[node]    # leaf
            continue
        earliest = min(finish[c] for c in children[node])
        latest = max(finish[c] for c in children[node])
        own_duration = (latest - earliest) + baseTime[node]
        finish[node] = latest + own_duration
    return finish[0]
```

```java
// java
// LC 3965 - Finish Time of Tasks I
// IDEA: build children lists; compute nodes in reverse DFS pre-order so children finish first
// time = O(n), space = O(n)
public long finishTime(int n, int[][] edges, int[] baseTime) {
    List<List<Integer>> children = new ArrayList<>();
    for (int i = 0; i < n; i++) {
        children.add(new ArrayList<>());
    }
    for (int[] e : edges) {
        children.get(e[0]).add(e[1]);
    }
    int[] order = new int[n];
    int size = 0;
    Deque<Integer> stack = new ArrayDeque<>();
    stack.push(0);
    while (!stack.isEmpty()) {
        int node = stack.pop();
        order[size++] = node;
        for (int c : children.get(node)) {
            stack.push(c);
        }
    }
    long[] finish = new long[n];             // long: values compound with depth
    for (int i = n - 1; i >= 0; i--) {
        int node = order[i];
        if (children.get(node).isEmpty()) {
            finish[node] = baseTime[node];
            continue;
        }
        long earliest = Long.MAX_VALUE, latest = Long.MIN_VALUE;
        for (int c : children.get(node)) {
            earliest = Math.min(earliest, finish[c]);
            latest = Math.max(latest, finish[c]);
        }
        finish[node] = latest + (latest - earliest) + baseTime[node];
    }
    return finish[0];
}
```

**Complexity**
- **Time O(n)**: each node is pushed once, and the `for node in reversed(order)` pass reads each
  child list once.
- **Space O(n)**: the children lists, `order`, `finish` and the stack.

**Traps**
- Building the graph undirected breaks the leaf test: every non-root node then has its parent as
  a neighbour and nothing looks like a leaf.
- Java `int` overflows: a node's value is close to twice its slowest child's, compounding with
  depth. The statement only promises the answer is < 2⁵³; use `long`.
- The recursive `dfs(child)` form is the easiest to explain but is 10⁵ frames deep on a chain;
  say so and write the order-based form.

**Not this if** — the tree is a binary `TreeNode`: the same rollup is
[dfs.md § Template 6](./dfs.md#template-6-bottom-up-post-order-dfs--lc-543-), and recursion depth
is the tree height.

**Drill** — LC 3965 ⭐ ·
LC 3967 *(the same rollup, answered for many queries)* · LC 559 *(1 + max child depth)* · LC 1376 *(max over children + own time)*

## Trees Encoded in the Input

### Template 7: Depth-Indexed Stack — LC 388

**Recognise** — the input string **is** a tree: tab-indented lines, `/`-separated paths, a
depth prefix per token. The answer is a root-to-leaf aggregate such as the longest path.

**Key point** — `stack[d]` holds the aggregate of the open ancestor at depth `d`. Before handling
a line at depth `d`, pop until `len(stack) == d + 1`: those pops are the DFS returning up, and
`stack[d]` is then exactly this line's parent prefix. No tree is ever built.

```python
# python
# LC 388 - Longest Absolute File Path
# IDEA: stack[d] = path length up to the open directory at depth d; popping == returning up the DFS
# time = O(N), space = O(D)   N = input length, D = deepest nesting
def lengthLongestPath(input):
    longest = 0
    stack = [0]                                # depth 0 has an empty prefix
    for line in input.split('\n'):
        name = line.lstrip('\t')
        depth = len(line) - len(name)          # leading tabs = depth
        while len(stack) > depth + 1:          # close the directories this line is not inside
            stack.pop()
        if '.' in name:                        # a file is a leaf: score it, never push it
            longest = max(longest, stack[depth] + len(name))
        else:                                  # a directory: its prefix plus the name plus '/'
            stack.append(stack[depth] + len(name) + 1)
    return longest
```

```java
// java
// LC 388 - Longest Absolute File Path
// IDEA: stack[d] = path length up to the open directory at depth d; popping == returning up the DFS
// time = O(N), space = O(D)
public int lengthLongestPath(String input) {
    int longest = 0;
    Deque<Integer> stack = new ArrayDeque<>();
    stack.push(0);
    for (String line : input.split("\n")) {
        int depth = 0;
        while (depth < line.length() && line.charAt(depth) == '\t') {
            depth++;
        }
        String name = line.substring(depth);
        while (stack.size() > depth + 1) {
            stack.pop();
        }
        if (name.contains(".")) {
            longest = Math.max(longest, stack.peek() + name.length());
        } else {
            stack.push(stack.peek() + name.length() + 1);
        }
    }
    return longest;
}
```

**Complexity**
- **Time O(N)**: `split` and `lstrip` read every character once; each directory is pushed and
  popped at most once by the `while len(stack) > depth + 1` loop.
- **Space O(D)**: the stack holds one entry per open directory.

**Traps**
- Pushing a file corrupts every deeper prefix; a file is a leaf.
- No file at all returns 0 (`"a"` → 0), not the longest directory path.
- The `+ 1` is the `/` that a **directory** contributes, so a top-level file is scored against
  `stack[0] = 0` with no leading slash.

**Drill** — LC 388 ⭐ · LC 1233 *(below)* · LC 1028 *(depth given by dashes: rebuild the tree)*

### Remove Sub-Folders from the Filesystem — LC 1233

> **Key point** — split each path into its parts and store them in a trie, marking the node where
> a folder ends. A DFS from the root collects a marked node and **stops descending** there:
> everything below a stored folder is a sub-folder by definition.

**Reduces to**: [Template 7](#template-7-depth-indexed-stack--lc-388) — the same "path parts
are depth levels" reading, kept in a trie because the paths arrive unsorted.

```python
# python
# LC 1233 - Remove Sub-Folders from the Filesystem
# IDEA: path trie; DFS collects the first stored folder on each branch and cuts below it
# time = O(total path chars), space = O(total path chars)
def removeSubfolders(folder):
    root = {}
    for path in folder:
        node = root
        for part in path.split('/')[1:]:       # [0] is the empty string before the first '/'
            if part not in node:
                node[part] = {}
            node = node[part]
        node['$'] = path                       # the full path ends here

    kept = []
    stack = [root]
    while stack:
        node = stack.pop()
        if '$' in node:
            kept.append(node['$'])
            continue                           # cut: anything deeper is a sub-folder
        for child in node.values():
            stack.append(child)
    return kept
```

**Traps**
- Comparing raw string prefixes says `/a/bc` is inside `/a/b`. Splitting on `/` compares whole
  names; with sorting instead, test `path.startswith(kept[-1] + '/')`.

### Template 8: Memoized Climb on a Parent Array — LC 4015

**Recognise** — the tree arrives as `parent[]` (or `manager[]`) with `parent[root] = -1`, and the
answer needs each node's depth or the tree's height. n ≤ 10⁵, and the labelling does not
promise `parent[i] < i`.

**Key point** — `parent[]` already is the edge upward, so `depth[x] = depth[parent[x]] + 1`
needs no other structure. Memoize it: each node's depth is then computed once and every climb
stops at the first known ancestor, so all climbs together are O(n). Depths are 1-based, so
`depth[x] == 0` means "not computed".

```python
# python
# LC 4015 - Weighted Sum of a Tree
# IDEA: memoized climb up parent[] (explicit stack, no recursion); height = max depth
# time = O(n), space = O(n)
def weightedSum(parent, nums):
    n = len(parent)
    depth = [0] * n                    # 0 = not computed yet; depths are 1-based
    for start in range(n):
        chain = []
        x = start
        while depth[x] == 0:           # climb until a computed node, or past the root
            if parent[x] == -1:
                depth[x] = 1
                break
            chain.append(x)
            x = parent[x]
        d = depth[x]
        while chain:                   # unwind: fill every node climbed through
            d += 1
            depth[chain.pop()] = d

    height = max(depth)
    total = 0
    for i in range(n):
        total += nums[i] * (height - depth[i] + 1)
    return total
```

```java
// java
// LC 4015 - Weighted Sum of a Tree
// IDEA: memoized climb up parent[] with an explicit stack; height = max depth
// time = O(n), space = O(n)
class Solution {
    public long weightedSum(int[] parent, int[] nums) {
        int n = parent.length;
        int[] depth = new int[n];                         // 0 = not computed yet; depths are 1-based
        java.util.ArrayDeque<Integer> chain = new java.util.ArrayDeque<>();
        for (int start = 0; start < n; start++) {
            int x = start;
            while (depth[x] == 0) {                       // climb until a computed node, or past the root
                if (parent[x] == -1) {
                    depth[x] = 1;
                    break;
                }
                chain.push(x);
                x = parent[x];
            }
            int d = depth[x];
            while (!chain.isEmpty()) {                    // unwind: fill every node climbed through
                d++;
                depth[chain.pop()] = d;
            }
        }
        int height = 0;
        for (int dep : depth) height = Math.max(height, dep);
        long total = 0;
        for (int i = 0; i < n; i++) {
            total += (long) nums[i] * (height - depth[i] + 1);
        }
        return total;
    }
}
```

**Complexity**
- **Time O(n)**: a node is appended to `chain` only while `depth[x] == 0`, and it is filled when
  the chain unwinds, so every node is climbed through once over all starts.
- **Space O(n)**: `depth` and one `chain`, at most n long on a path-shaped tree.

**Dry run** — `parent = [-1,0,0,0,2,2]`, `nums = [5,2,3,1,4,6]`:

```text
start  climb                 depths filled
0      root                  depth0 = 1
1      1 -> 0 (known)        depth1 = 2
2      2 -> 0                depth2 = 2
3      3 -> 0                depth3 = 2
4      4 -> 2 (known)        depth4 = 3
5      5 -> 2                depth5 = 3
height 3: 5*3 + 2*2 + 3*2 + 1*2 + 4*1 + 6*1 = 37
```

**Traps**
- Without the memo a path-shaped tree costs 1 + 2 + … + n = O(n²).
- Sweeping `i = 0 … n − 1` once with `depth[i] = depth[parent[i]] + 1` is only right when
  `parent[i] < i`; LC 4015 does not promise it, so the sweep reads unfilled entries.
- The recursive climb is n frames deep on a chain; at n = 10⁵ that needs the explicit stack.

**Not this if** — the answer needs subtree aggregates (sizes, sums, child min / max): invert
`parent[]` into children lists and use [Template 6](#template-6-n-ary-post-order-rollup--lc-3965).
The climb also looks like union-find's `find`, but there is no `union` here; a DSU adds nothing.

**Drill** — LC 4015 ⭐ · LC 1376 *(accumulate time down the manager chain)* · LC 1483 *(the climb
precomputed at 2^k strides: binary lifting)* · LC 1650 *(climb both chains: two-list
intersection)*

## When Not to Use

| Technique | The simpler card that usually suffices | When it is enough |
|---|---|---|
| Template 1 (validity flag) | [dfs.md § Template 2](./dfs.md#template-2-graph--grid-dfs-flood-fill--lc-200-) | one grid, every component counts |
| Template 2 (direction flag) | a plain directed DFS / BFS | the question is only reachability along the given directions |
| Template 3 (pair counting) | [graph.md § Template 1](./graph.md#template-1-connected-components-by-traversal--lc-547-) | only the number of components is wanted |
| Template 4 (trie DFS) | a hash set of words, or a plain trie walk | queries have no wildcard, or the dictionary is small enough to compare word by word |
| Template 5 (distance buckets) | [dfs.md § Template 6](./dfs.md#template-6-bottom-up-post-order-dfs--lc-543-) | the answer is one best path (diameter, max path sum), not a count of pairs |
| Template 6 (N-ary rollup) | recursive post-order | n is small or the tree is shallow; the recursion is clearer to explain |
| Template 7 (depth-indexed stack) | build the tree, then DFS it | the interviewer wants the tree itself, or the format is not one line per node |
| Template 8 (memoized climb) | BFS from the root over inverted children lists | the labelling guarantees `parent[i] < i` (then one sweep), or subtree data is needed anyway |

## Summary

| Template | Extra bookkeeping | Time | Space |
|---|---|---|---|
| 1. Validity flag | one boolean per flood | O(m·n) | O(m·n) |
| 2. Direction flag | `0/1` on each stored edge | O(n) | O(n) |
| 3. Pair counting | running total of nodes seen | O(n + E) | O(n + E) |
| 4. Trie DFS | branch on `.` / a mismatch budget | O(26^d · L) per search | O(total chars) |
| 5. Distance buckets | `cnt[d]` per node | O(N·d²) | O(N) |
| 6. N-ary rollup | reverse pre-order instead of recursion | O(n) | O(n) |
| 7. Depth-indexed stack | `stack[d]` per open ancestor | O(N) | O(D) |
| 8. Memoized climb | `depth[]` memo, 0 = not computed | O(n) | O(n) |

**Pitfalls unique to this sheet**
- A flood whose result is a flag must still flood the whole component.
- A trie DFS checks the end-of-word marker, and skips it when iterating children.
- A depth-indexed stack never pushes a leaf.
- n up to 10⁵ with a possible path-shaped input: Templates 1, 2, 3, 6 and 8 are written with an
  explicit stack for that reason.

### Language notes
- **Python recursion**: the default limit is 1,000 frames. `sys.setrecursionlimit(10**6)` raises
  it but a very deep recursion can still exhaust the C stack; an explicit stack is the safe form.
- **Java recursion**: the thread stack is not settable from a LeetCode solution; tens of
  thousands of frames can throw `StackOverflowError`.
- **Java shared state**: a `StringBuilder`, `List` or `int[]` passed into a recursive call is the
  same object in every frame, so appends persist after the call returns — which is how
  [dfs.md § Template 8](./dfs.md#template-8-path-signature-shape-encoding--lc-694) builds a path
  signature. An `int` or `String` parameter is a copy; return it instead.

### Related Topics
- **[graph_advanced.md](./graph_advanced.md)**: Tarjan bridges / SCC and Hierholzer Euler paths.
- **[union_find.md](./union_find.md)**: Template 3 has a DSU form; Template 8 looks like DSU but
  has no `union()`.
- **[trie.md](./trie.md)**: the structure Template 4 searches.
- **[tree_codec.md](./tree_codec.md)**: DFS that returns a string, and the parser that reads it back.
- **[bfs.md](./bfs.md)**: the breadth-first form of every flood here.
