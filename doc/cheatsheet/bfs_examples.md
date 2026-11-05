# BFS — Worked LeetCode Examples

> **Scope** — The BFS problem set: one problem card per classic level-order, multi-source, state-space and tree-as-graph problem, each filed under the [bfs.md](./bfs.md) template it reduces to — it teaches no templates of its own.
> **See also**: [bfs.md](./bfs.md) — the templates every card here reduces to; [bfs_advanced.md](./bfs_advanced.md) — the rarer BFS variants (bidirectional, 0-1 deque, route-level, all-shortest-paths enumeration, augmented state).

## LeetCode Problem Lists

- [Breadth-First Search](https://leetcode.com/problem-list/breadth-first-search/)
- [Graph Theory](https://leetcode.com/problem-list/graph/)

## Overview

The classic BFS problems, grouped by the [bfs.md](./bfs.md) template each one reduces to and in
that sheet's template order. Every card states the one step it adds to its template; the template
itself is one click away. A problem worked in another sheet appears here only as a pointer row in
the Index. Template 3 (grid BFS with a direction array) has no card of its own here: its grid
problems either start from many cells at once (Template 4) or are the anchor LC 1091 itself.

## Index

| LC | Problem | Reduces to | Difficulty |
|---|---|---|---|
| 116 / 117 | [Populating Next Right Pointers in Each Node](#populating-next-right-pointers-in-each-node--lc-116--117) | [bfs § Template 1](./bfs.md#template-1-level-order-bfs--lc-102-) | Medium |
| 958 | [Check Completeness of a Binary Tree](#check-completeness-of-a-binary-tree--lc-958) | [bfs § Template 1](./bfs.md#template-1-level-order-bfs--lc-102-) | Medium |
| 623 | [Add One Row to Tree](#add-one-row-to-tree--lc-623) | [bfs § Template 1](./bfs.md#template-1-level-order-bfs--lc-102-) | Medium |
| 662 | [Maximum Width of Binary Tree](#maximum-width-of-binary-tree--lc-662) | [bfs § Template 1](./bfs.md#template-1-level-order-bfs--lc-102-) | Medium |
| 909 | [Snakes and Ladders](#snakes-and-ladders--lc-909) | [bfs § Template 2](./bfs.md#template-2-graph-bfs-mark-on-enqueue--lc-1971-) | Medium |
| 207 | Course Schedule | [graph § Template 4](./graph.md#template-4-topological-sort-kahns-bfs--lc-207-) — worked there | Medium |
| 286 | [Walls and Gates](#walls-and-gates--lc-286) | [bfs § Template 4](./bfs.md#template-4-multi-source-bfs--lc-994-) | Medium |
| 130 | [Surrounded Regions](#surrounded-regions--lc-130) | [bfs § Template 4](./bfs.md#template-4-multi-source-bfs--lc-994-) | Medium |
| 994 | Rotting Oranges | [bfs § Template 4](./bfs.md#template-4-multi-source-bfs--lc-994-) — the anchor, worked there | Medium |
| 417 | Pacific Atlantic Water Flow | worked in [dfs_examples.md](./dfs_examples.md) | Medium |
| 310 | Minimum Height Trees | worked in [topology_sorting_examples.md](./topology_sorting_examples.md) (leaf trimming) | Medium |
| 752 | [Open the Lock](#open-the-lock--lc-752) | [bfs § Template 5](./bfs.md#template-5-word-ladder-bfs-over-generated-neighbours--lc-127-) | Medium |
| 279 | [Perfect Squares](#perfect-squares--lc-279) | [bfs § Template 5](./bfs.md#template-5-word-ladder-bfs-over-generated-neighbours--lc-127-) | Medium |
| 773 | [Sliding Puzzle](#sliding-puzzle--lc-773) | [bfs § Template 5](./bfs.md#template-5-word-ladder-bfs-over-generated-neighbours--lc-127-) | Hard |
| 742 | [Closest Leaf in a Binary Tree](#closest-leaf-in-a-binary-tree--lc-742) | [bfs § Template 6](./bfs.md#template-6-parent-map--bfs-radiating-from-a-target--lc-863-) | Medium |
| 863 | All Nodes Distance K in Binary Tree | [bfs § Template 6](./bfs.md#template-6-parent-map--bfs-radiating-from-a-target--lc-863-) — the anchor, worked there | Medium |

## Level-Order BFS Examples

### Populating Next Right Pointers in Each Node — LC 116 / 117

> **Key point** — `next` is "the node to my right on the same level", so it is exactly the order a
> level-order BFS pops a level in: link each popped node to the one popped after it, and the last
> node of the level keeps `next = None`. Nothing about the tree's shape is used, so the same code
> solves LC 116 (perfect tree) and LC 117 (any tree).

**Reduces to**: [bfs § Template 1](./bfs.md#template-1-level-order-bfs--lc-102-) — with a `prev` pointer carried across one level.

```python
# python
# LC 116 / 117 - Populating Next Right Pointers in Each Node
# IDEA: level-order BFS; inside one level, link prev.next = node in pop order
# time = O(n), space = O(w)   w = the widest level
from collections import deque

def connect(root):
    if not root:
        return None
    queue = deque([root])
    while queue:
        prev = None
        for _ in range(len(queue)):        # exactly one level
            node = queue.popleft()
            if prev:
                prev.next = node
            prev = node
            if node.left:
                queue.append(node.left)
            if node.right:
                queue.append(node.right)
        prev.next = None                   # the last node of a level points nowhere
    return root
```

The follow-up asks for O(1) extra space. On a **perfect** tree (LC 116) the level just linked is
itself a linked list, so walk it through `next` to wire the level below — a different complexity,
which is why it is kept:

```python
# python
# LC 116 - Populating Next Right Pointers in Each Node (O(1) space, perfect tree only)
# IDEA: walk level L through the next links it already has, and wire level L + 1
# time = O(n), space = O(1)
def connect_constant_space(root):
    if not root:
        return None
    leftmost = root
    while leftmost.left:                   # stop at the leaf level
        head = leftmost
        while head:
            head.left.next = head.right             # two children of one parent
            if head.next:
                head.right.next = head.next.left    # across two parents
            head = head.next
        leftmost = leftmost.left
    return root
```

**Traps**
- The O(1) version assumes every node has two children; on LC 117 it dereferences a missing
  child. LC 117's O(1) version needs a dummy head per level instead.
- `prev` must be reset at the start of every level, or the last node of one level links to the
  first node of the next.

**Similar** — LC 199 (last node of each level) · LC 102 (the base loop)

### Check Completeness of a Binary Tree — LC 958

> **Key point** — enqueue the `None` children too. The queue then reads the tree as its array
> representation, and a complete tree has every `None` at the tail: once a `None` has been popped,
> any real node after it means a gap.

**Reduces to**: [bfs § Template 1](./bfs.md#template-1-level-order-bfs--lc-102-) — without the level loop, and with `None` children kept in the queue.

```python
# python
# LC 958 - Check Completeness of a Binary Tree
# IDEA: push children unconditionally; after the first None pops, a real node means "not complete"
# time = O(n), space = O(w)
from collections import deque

def isCompleteTree(root):
    queue = deque([root])
    seen_null = False
    while queue:
        node = queue.popleft()
        if node is None:
            seen_null = True
        else:
            if seen_null:
                return False               # a real node after a gap
            queue.append(node.left)        # None children are enqueued on purpose
            queue.append(node.right)
    return True
```

**Traps**
- Checking only "every level but the last is full" misses `[1,2,3,4,5,null,7]`: the last level has
  a hole in the middle.
- Level-order serialization (LC 297) writes `null` markers for the same reason; this is that
  queue read back.

**Similar** — LC 297 (serialize with `null` markers) · LC 222 (count nodes of a complete tree)

### Add One Row to Tree — LC 623

> **Key point** — the row to mutate is `depth - 1`, the **parent** row whose child pointers change.
> Run level-order BFS until the queue holds that row, then give every node in it two new children
> and hang the old subtrees on the **outer** sides. `depth == 1` has no parent row, so the new node
> becomes the root.

**Reduces to**: [bfs § Template 1](./bfs.md#template-1-level-order-bfs--lc-102-) — stop at one level and rewire it instead of descending.

```python
# python
# LC 623 - Add One Row to Tree
# IDEA: level BFS down to depth - 1; splice new nodes under each node there, old subtrees outside
# time = O(n), space = O(w)
from collections import deque

def addOneRow(root, val, depth):
    if depth == 1:                         # no parent row: the new node is the root
        return TreeNode(val, root, None)
    queue = deque([root])
    cur_depth = 1                          # the root is depth 1, not 0
    while queue:
        if cur_depth == depth - 1:         # the queue holds exactly the parent row
            for node in queue:
                node.left = TreeNode(val, node.left, None)     # old left goes on the left
                node.right = TreeNode(val, None, node.right)   # old right goes on the right
            return root
        for _ in range(len(queue)):
            node = queue.popleft()
            if node.left:
                queue.append(node.left)
            if node.right:
                queue.append(node.right)
        cur_depth += 1
    return root
```

A DFS that carries the depth is shorter and uses O(h) instead of O(w) space — kept for that reason:

```python
# python
# LC 623 - Add One Row to Tree (DFS)
# IDEA: recurse with the current depth; at depth - 1 rewire the two children and stop
# time = O(n), space = O(h)
def addOneRow_dfs(root, val, depth):
    if depth == 1:
        return TreeNode(val, root, None)

    def insert(node, d):
        if not node:
            return
        if d == depth - 1:
            node.left = TreeNode(val, node.left, None)
            node.right = TreeNode(val, None, node.right)
            return                         # never descend into the new nodes
        insert(node.left, d + 1)
        insert(node.right, d + 1)

    insert(root, 1)
    return root
```

**Traps**
- Stopping at `cur_depth == depth` is one level too late: the pointers to change live on the
  parent row.
- Reattaching on the inner sides (`new_left.right = old_left`) mirrors the subtree.
- Continuing the BFS after the rewire walks into the new `val` nodes.
- `depth` can reach 10⁴ on a skewed tree, past Python's default recursion limit; the BFS form has
  no such limit.

**Similar** — LC 199 (same level loop) · LC 226 (cache-then-swap child pointers)

### Maximum Width of Binary Tree — LC 662

> **Key point** — give each node its heap index (children of `i` are `2i` and `2i + 1`); a level's
> width is `last - first + 1`, which counts the `None` gaps without storing them. Subtract the
> level's first index from every index in it, or a skewed tree of depth ~60 overflows `int`.

**Reduces to**: [bfs § Template 1](./bfs.md#template-1-level-order-bfs--lc-102-) — the queue carries `(node, index)`.

```python
# python
# LC 662 - Maximum Width of Binary Tree
# IDEA: level BFS carrying a heap index, re-based to 0 at the start of every level
# time = O(n), space = O(w)
from collections import deque

def widthOfBinaryTree(root):
    if not root:
        return 0
    best = 0
    queue = deque([(root, 0)])
    while queue:
        first = queue[0][1]
        last = 0
        for _ in range(len(queue)):
            node, idx = queue.popleft()
            idx -= first                   # re-base: this level now starts at 0
            last = idx
            if node.left:
                queue.append((node.left, 2 * idx))
            if node.right:
                queue.append((node.right, 2 * idx + 1))
        best = max(best, last + 1)
    return best
```

```java
// java
// LC 662 - Maximum Width of Binary Tree
// IDEA: level BFS carrying a heap index; width = last - first + 1 (nulls counted implicitly)
// time = O(n), space = O(w)
public int widthOfBinaryTree(TreeNode root) {
    if (root == null) return 0;
    int ans = 0;
    Queue<TreeNode> nodes = new LinkedList<>();
    Queue<Integer> idxs = new LinkedList<>();
    nodes.offer(root); idxs.offer(0);
    while (!nodes.isEmpty()) {
        int size = nodes.size(), first = 0, last = 0;
        for (int i = 0; i < size; i++) {
            TreeNode node = nodes.poll();
            int id = idxs.poll();
            if (i == 0) first = id;
            id -= first;                    // re-base the level at 0 -> no overflow
            last = id;
            if (node.left  != null) { nodes.offer(node.left);  idxs.offer(2 * id); }
            if (node.right != null) { nodes.offer(node.right); idxs.offer(2 * id + 1); }
        }
        ans = Math.max(ans, last + 1);      // last is already relative to first
    }
    return ans;
}
```

A pre-order DFS needs no queue: it reaches each depth's leftmost node first, so it remembers that
index and measures every later node at the same depth from it — O(h) instead of O(w) space:

```python
# python
# LC 662 - Maximum Width of Binary Tree (DFS)
# IDEA: heap index (2p, 2p + 1); pre-order meets each depth's leftmost node first, so remember it
# time = O(n), space = O(h)
def widthOfBinaryTree_dfs(root):
    first = {}                             # depth -> heap index of the leftmost node there
    best = 0

    def dfs(node, depth, pos):
        nonlocal best
        if not node:
            return
        first.setdefault(depth, pos)
        best = max(best, pos - first[depth] + 1)
        dfs(node.left, depth + 1, 2 * pos)
        dfs(node.right, depth + 1, 2 * pos + 1)

    dfs(root, 0, 0)
    return best
```

**Traps**
- Without the re-base, Java overflows on a 3,000-node chain that zig-zags; Python does not
  overflow but its integers grow to thousands of bits. The DFS form cannot re-base per level, so
  in Java it needs `long` or the depth's first index subtracted.
- Counting only non-null nodes per level gives the level's size, not its width.

**Similar** — LC 958 (same heap-index view of a tree) · LC 102

## Graph BFS Examples

### Snakes and Ladders — LC 909

> **Key point** — the board is a graph on squares `1 … n²`: from square `s` there is one edge to
> each of `s + 1 … s + 6`, redirected by a snake or ladder if the landing square has one. Every
> roll costs one move, so plain BFS gives the fewest rolls. The only work is turning a square
> number into a boustrophedon `(row, col)`.

**Reduces to**: [bfs § Template 2](./bfs.md#template-2-graph-bfs-mark-on-enqueue--lc-1971-) — edges computed from the dice instead of an adjacency list.

```python
# python
# LC 909 - Snakes and Ladders
# IDEA: BFS over squares; a roll lands on s+1..s+6 and is redirected by a snake or ladder
# time = O(n^2), space = O(n^2)   n^2 squares, 6 edges each
from collections import deque

def snakesAndLadders(board):
    n = len(board)

    def cell(square):                      # square number -> (row, col) on the board
        r, c = divmod(square - 1, n)
        row = n - 1 - r                    # square 1 is bottom-left
        col = c if r % 2 == 0 else n - 1 - c   # every other row runs right to left
        return row, col

    target = n * n
    queue = deque([1])
    moves = {1: 0}                         # doubles as the visited set
    while queue:
        square = queue.popleft()
        if square == target:
            return moves[square]
        for nxt in range(square + 1, min(square + 6, target) + 1):
            r, c = cell(nxt)
            if board[r][c] != -1:
                nxt = board[r][c]          # take the snake or ladder
            if nxt not in moves:
                moves[nxt] = moves[square] + 1
                queue.append(nxt)
    return -1
```

```java
// java
// LC 909 - Snakes and Ladders
// IDEA: flatten the board to squares 1..n*n, then BFS; dice roll = edge
// time = O(n^2), space = O(n^2)
public int snakesAndLadders(int[][] board) {
    int n = board.length;
    int[] flat = new int[n * n + 1];
    int idx = 1; boolean leftToRight = true;
    for (int r = n - 1; r >= 0; r--) {
        if (leftToRight) for (int c = 0; c < n; c++) flat[idx++] = board[r][c];
        else for (int c = n - 1; c >= 0; c--) flat[idx++] = board[r][c];
        leftToRight = !leftToRight;
    }
    boolean[] visited = new boolean[n * n + 1];
    Queue<int[]> queue = new LinkedList<>();
    queue.offer(new int[]{1, 0});
    visited[1] = true;
    while (!queue.isEmpty()) {
        int[] curr = queue.poll();
        int pos = curr[0], steps = curr[1];
        for (int dice = 1; dice <= 6 && pos + dice <= n * n; dice++) {
            int next = pos + dice;
            if (flat[next] != -1) next = flat[next];
            if (next == n * n) return steps + 1;
            if (!visited[next]) { visited[next] = true; queue.offer(new int[]{next, steps + 1}); }
        }
    }
    return -1;
}
```

**Traps**
- Mark the square you **end** on after the snake or ladder, not the square the die showed; the
  ladder's foot is never a place you stand.
- A snake or ladder is taken once per roll: landing on the far end of a ladder that starts
  another ladder does not chain.

**Similar** — LC 1971 (the template's anchor) · LC 1306 (jump game as a graph)

## Multi-Source BFS Examples

### Walls and Gates — LC 286

> **Key point** — "distance from each room to its **nearest** gate" is one BFS seeded with every
> gate at distance 0. The first gate to reach a room reaches it by a shortest path, so the room is
> written once, when it is first enqueued; `INF` doubles as "not reached yet".

**Reduces to**: [bfs § Template 4](./bfs.md#template-4-multi-source-bfs--lc-994-) — the grid stores the distances.

```python
# python
# LC 286 - Walls and Gates
# IDEA: seed every gate; the first time BFS reaches an INF room, that distance is final
# time = O(m * n), space = O(m * n)
from collections import deque

def wallsAndGates(rooms):
    if not rooms:
        return
    INF = 2147483647
    m, n = len(rooms), len(rooms[0])
    queue = deque((r, c) for r in range(m) for c in range(n) if rooms[r][c] == 0)
    while queue:
        r, c = queue.popleft()
        for dr, dc in ((1, 0), (-1, 0), (0, 1), (0, -1)):
            nr, nc = r + dr, c + dc
            if 0 <= nr < m and 0 <= nc < n and rooms[nr][nc] == INF:
                rooms[nr][nc] = rooms[r][c] + 1    # mark on enqueue = the distance
                queue.append((nr, nc))
```

```java
// java
// LC 286 - Walls and Gates
// IDEA: multi-source BFS from all gates; write the distance when a room is first reached
// time = O(m * n), space = O(m * n)
public void wallsAndGates(int[][] rooms) {
    int m = rooms.length, n = rooms[0].length;
    int INF = Integer.MAX_VALUE;
    Queue<int[]> queue = new LinkedList<>();
    for (int i = 0; i < m; i++)
        for (int j = 0; j < n; j++)
            if (rooms[i][j] == 0) queue.offer(new int[]{i, j});
    int[][] dirs = {{1,0},{-1,0},{0,1},{0,-1}};
    while (!queue.isEmpty()) {
        int[] cell = queue.poll();
        for (int[] d : dirs) {
            int nr = cell[0] + d[0], nc = cell[1] + d[1];
            if (nr >= 0 && nr < m && nc >= 0 && nc < n && rooms[nr][nc] == INF) {
                rooms[nr][nc] = rooms[cell[0]][cell[1]] + 1;
                queue.offer(new int[]{nr, nc});
            }
        }
    }
}
```

**Traps**
- One BFS per gate, keeping the minimum, is right but k times slower.
- Walls are `-1`, never `INF`, so the `== INF` test already skips them and every gate.

**Similar** — LC 542 (nearest 0, worked in [bfs § Template 4](./bfs.md#template-4-multi-source-bfs--lc-994-)) · LC 1162 · LC 994

### Surrounded Regions — LC 130

> **Key point** — invert the question. An `'O'` survives exactly when it is connected to an `'O'`
> on the border, so seed BFS with every border `'O'`, mark everything it reaches as safe, and flip
> every `'O'` that was never reached.

**Reduces to**: [bfs § Template 4](./bfs.md#template-4-multi-source-bfs--lc-994-) — the sources are the border cells, and only reachability is kept.

```python
# python
# LC 130 - Surrounded Regions
# IDEA: BFS inward from every border 'O' marking 'S'; then O -> X (captured) and S -> O (safe)
# time = O(m * n), space = O(m * n)
from collections import deque

def solve(board):
    if not board:
        return
    m, n = len(board), len(board[0])
    queue = deque()
    for r in range(m):
        for c in range(n):
            if (r in (0, m - 1) or c in (0, n - 1)) and board[r][c] == 'O':
                board[r][c] = 'S'              # safe: touches the border
                queue.append((r, c))
    while queue:
        r, c = queue.popleft()
        for dr, dc in ((1, 0), (-1, 0), (0, 1), (0, -1)):
            nr, nc = r + dr, c + dc
            if 0 <= nr < m and 0 <= nc < n and board[nr][nc] == 'O':
                board[nr][nc] = 'S'
                queue.append((nr, nc))
    for r in range(m):
        for c in range(n):
            if board[r][c] == 'O':
                board[r][c] = 'X'              # never reached from the border: captured
            elif board[r][c] == 'S':
                board[r][c] = 'O'
```

```java
// java
// LC 130 - Surrounded Regions
// IDEA: BFS from border O-cells to find the regions that are not surrounded
// time = O(m * n), space = O(m * n)
public void solve(char[][] board) {
    int m = board.length, n = board[0].length;
    Queue<int[]> queue = new LinkedList<>();
    for (int i = 0; i < m; i++)
        for (int j = 0; j < n; j++)
            if ((i == 0 || i == m - 1 || j == 0 || j == n - 1) && board[i][j] == 'O') {
                board[i][j] = 'S'; queue.offer(new int[]{i, j});
            }
    int[][] dirs = {{1,0},{-1,0},{0,1},{0,-1}};
    while (!queue.isEmpty()) {
        int[] c = queue.poll();
        for (int[] d : dirs) {
            int nr = c[0] + d[0], nc = c[1] + d[1];
            if (nr >= 0 && nr < m && nc >= 0 && nc < n && board[nr][nc] == 'O') {
                board[nr][nc] = 'S'; queue.offer(new int[]{nr, nc});
            }
        }
    }
    for (int i = 0; i < m; i++) for (int j = 0; j < n; j++)
        board[i][j] = board[i][j] == 'S' ? 'O' : (board[i][j] == 'O' ? 'X' : board[i][j]);
}
```

**Traps**
- Flood-filling each interior region and checking whether it touched the border also works, but
  has to undo the region it was wrong about; starting from the border never guesses.
- A third marker (`'S'`) is needed: flipping border-connected cells straight to `'O'` makes them
  indistinguishable from the ones still to visit.

**Similar** — LC 1020 Number of Enclaves · LC 1254 (the DFS form, [dfs § Template 7](./dfs.md#template-7-2-pass-dfs-boundary-elimination--lc-1254)) · LC 417

## Generated-Neighbour BFS Examples

### Open the Lock — LC 752

> **Key point** — each 4-digit combination is a node, and its neighbours are the 8 combinations one
> wheel-turn away, generated on demand. Deadends are nodes removed from the graph. BFS levels
> count turns, so the first time the target is popped is the minimum.

**Reduces to**: [bfs § Template 5](./bfs.md#template-5-word-ladder-bfs-over-generated-neighbours--lc-127-) — the alphabet is the 10 digits and each position turns two ways.

```python
# python
# LC 752 - Open the Lock
# IDEA: BFS over combinations; neighbours = turn one wheel +1 or -1 (mod 10), skipping deadends
# time = O(10^4 * 8), space = O(10^4)   at most 10^4 states, 8 neighbours each
from collections import deque

def openLock(deadends, target):
    dead = set(deadends)
    if "0000" in dead:
        return -1
    queue = deque(["0000"])
    seen = {"0000"}
    turns = 0
    while queue:
        for _ in range(len(queue)):
            code = queue.popleft()
            if code == target:
                return turns
            for i in range(4):
                digit = int(code[i])
                for step in (1, -1):
                    nxt = code[:i] + str((digit + step) % 10) + code[i + 1:]
                    if nxt not in seen and nxt not in dead:
                        seen.add(nxt)
                        queue.append(nxt)
        turns += 1
    return -1
```

```java
// java
// LC 752 - Open the Lock
// IDEA: BFS on 4-digit combinations; each turn = 1 step
// time = O(10^4 * 8), space = O(10^4)
public int openLock(String[] deadends, String target) {
    Set<String> dead = new HashSet<>(Arrays.asList(deadends));
    Set<String> visited = new HashSet<>();
    Queue<String> queue = new LinkedList<>();
    String start = "0000";
    if (dead.contains(start)) return -1;
    queue.offer(start);
    visited.add(start);
    int steps = 0;
    while (!queue.isEmpty()) {
        int size = queue.size();
        for (int i = 0; i < size; i++) {
            String curr = queue.poll();
            if (curr.equals(target)) return steps;
            char[] chars = curr.toCharArray();
            for (int j = 0; j < 4; j++) {
                char orig = chars[j];
                for (int delta : new int[]{1, -1}) {
                    chars[j] = (char) ((orig - '0' + delta + 10) % 10 + '0');
                    String next = new String(chars);
                    if (!visited.contains(next) && !dead.contains(next)) {
                        visited.add(next); queue.offer(next);
                    }
                    chars[j] = orig;            // restore before the next wheel
                }
            }
        }
        steps++;
    }
    return -1;
}
```

**Traps**
- `"0000"` itself can be a deadend: return -1 before the BFS starts.
- `(digit - 1) % 10` is `9` in Python but `-1` in Java; Java needs the `+ 10`.

**Similar** — LC 127 (the anchor) · LC 433 Minimum Genetic Mutation · LC 773

### Perfect Squares — LC 279

> **Key point** — treat each remaining amount as a node and "subtract a perfect square" as an edge.
> The fewest squares summing to `n` is the fewest edges from `n` to 0, so BFS from `n` returns the
> level at which some subtraction first hits 0.

**Reduces to**: [bfs § Template 5](./bfs.md#template-5-word-ladder-bfs-over-generated-neighbours--lc-127-) — the neighbours are `remaining - k²`.

```python
# python
# LC 279 - Perfect Squares
# IDEA: BFS from n; one level = one more square used; the first subtraction that reaches 0 wins
# time = O(n * sqrt(n)), space = O(n)   each amount is enqueued once, sqrt(n) squares tried
from collections import deque

def numSquares(n):
    squares = []
    i = 1
    while i * i <= n:
        squares.append(i * i)
        i += 1
    queue = deque([n])
    seen = {n}
    count = 0
    while queue:
        count += 1                         # squares used by the amounts generated this level
        for _ in range(len(queue)):
            remaining = queue.popleft()
            for sq in squares:
                left = remaining - sq
                if left == 0:
                    return count
                if left < 0:
                    break                  # squares are ascending: the rest are larger
                if left not in seen:
                    seen.add(left)
                    queue.append(left)
    return count
```

**Traps**
- Without `seen`, the same amount is reached by many orderings (`12 - 4 - 1` and `12 - 1 - 4`) and
  the queue explodes.
- The DP `dp[i] = 1 + min(dp[i - k²])` has the same bound and is the other answer interviewers
  expect; BFS can stop early, DP cannot.

**Similar** — LC 322 Coin Change (same graph, weighted coins) · LC 127

### Sliding Puzzle — LC 773

> **Key point** — the same skeleton as LC 752; only the state encoding and the neighbour rule
> change. Flatten the 2×3 board to a string like `"123450"`, and precompute which indices the blank
> can swap with, so generating neighbours is a table lookup instead of bounds checks. Only half of
> the 720 permutations are reachable, so `-1` is a real answer.

**Reduces to**: [bfs § Template 5](./bfs.md#template-5-word-ladder-bfs-over-generated-neighbours--lc-127-) — the state is the whole board.

```text
index layout      swap table (neighbours of each index)
 0 1 2            0:[1,3]  1:[0,2,4]  2:[1,5]
 3 4 5            3:[0,4]  4:[1,3,5]  5:[2,4]
```

```python
# python
# LC 773 - Sliding Puzzle
# IDEA: BFS on the flattened board string; the blank '0' swaps with its table neighbours
# time = O(6! * 6), space = O(6!)   at most 720 board states
from collections import deque

def slidingPuzzle(board):
    start = "".join(str(x) for row in board for x in row)
    target = "123450"
    neighbours = [[1, 3], [0, 2, 4], [1, 5], [0, 4], [1, 3, 5], [2, 4]]
    queue = deque([start])
    seen = {start}
    moves = 0
    while queue:
        for _ in range(len(queue)):
            state = queue.popleft()
            if state == target:
                return moves
            zero = state.index('0')
            for j in neighbours[zero]:
                cells = list(state)
                cells[zero], cells[j] = cells[j], cells[zero]
                nxt = "".join(cells)
                if nxt not in seen:
                    seen.add(nxt)
                    queue.append(nxt)
        moves += 1
    return -1                              # the target permutation is unreachable
```

```java
// java
// LC 773 - Sliding Puzzle
// IDEA: state = flattened board string; BFS levels = number of moves
// time = O(6! * 6), space = O(6!)
public int slidingPuzzle(int[][] board) {
    StringBuilder sb = new StringBuilder();
    for (int[] row : board) for (int v : row) sb.append(v);
    String start = sb.toString(), target = "123450";
    int[][] nbr = {{1,3},{0,2,4},{1,5},{0,4},{1,3,5},{2,4}};   // precomputed adjacency
    Queue<String> q = new LinkedList<>();
    Set<String> seen = new HashSet<>();
    q.offer(start); seen.add(start);
    int steps = 0;
    while (!q.isEmpty()) {
        int size = q.size();
        for (int i = 0; i < size; i++) {
            String cur = q.poll();
            if (cur.equals(target)) return steps;
            int zero = cur.indexOf('0');
            for (int j : nbr[zero]) {              // slide a tile into the blank
                char[] arr = cur.toCharArray();
                char tmp = arr[zero]; arr[zero] = arr[j]; arr[j] = tmp;
                String next = new String(arr);
                if (seen.add(next)) q.offer(next);
            }
        }
        steps++;
    }
    return -1;
}
```

**Traps**
- Hand-writing the swap table for a different board shape is where the bugs are; derive it from
  `(r ± 1, c)` / `(r, c ± 1)` if the board is not 2×3.
- Swapping in a list shared across neighbours (instead of a fresh copy per swap) corrupts every
  later neighbour of the same state.

**Similar** — LC 752 · LC 854 K-Similar Strings

## Parent Map + Radiate Examples

### Closest Leaf in a Binary Tree — LC 742

> **Key point** — turn the tree into an undirected graph (a parent map), then BFS from node `k`; the first leaf popped is the closest.

**Reduces to**: [tree_lca_distance.md § LC 742](./tree_lca_distance.md#closest-leaf-in-a-binary-tree--lc-742), where it is worked; this is a pointer.

