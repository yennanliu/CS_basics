# Topological Sorting — Worked Examples

> **Scope** — The problem set behind [topology_sorting.md](./topology_sorting.md): one problem card per problem, grouped under the template each one reduces to, with no template or theory material of its own.
> **See also**: [topology_sorting.md](./topology_sorting.md) — the ten templates, the problem classification and the decision framework these cards apply; [graph.md](./graph.md) — where LC 207 and LC 802 are the anchor problems of the Kahn and three-state DFS cards; [bfs.md](./bfs.md) and [dfs.md](./dfs.md) — the two traversals Kahn's and the three-colour template are built on.

## LeetCode Problem Lists

- [Topological Sort](https://leetcode.com/problem-list/topological-sort/)
- [Graph](https://leetcode.com/problem-list/graph/)

## Overview

The classic dependency-order problems, each as a problem card under the
[topology_sorting.md](./topology_sorting.md) template it reduces to. The sort itself is almost
never the work. The work is deciding what the nodes and edges *are*: courses, letters, the
numbers in a sequence, the leaves of a tree. [topology_sorting.md](./topology_sorting.md)
holds the templates; this sheet is the problems.

## Index

| LC | Problem | Reduces to | Difficulty |
|---|---|---|---|
| 210 | [Course Schedule II](#course-schedule-ii--lc-210-) | [Template 1](./topology_sorting.md#template-1-bfs-kahns-algorithm-) — Kahn's, return the order | Medium |
| 444 | [Sequence Reconstruction](#sequence-reconstruction--lc-444) | [Template 1](./topology_sorting.md#template-1-bfs-kahns-algorithm-) — Kahn's, the queue never holds two | Medium |
| 269 | [Alien Dictionary](#alien-dictionary--lc-269-) | [Template 1](./topology_sorting.md#template-1-bfs-kahns-algorithm-) — Kahn's on edges read from adjacent words | Hard |
| 1136 | [Parallel Courses](#parallel-courses--lc-1136) | [Template 6](./topology_sorting.md#template-6-parallel-task-scheduling) — one Kahn level = one semester | Medium |
| 310 | [Minimum Height Trees](#minimum-height-trees--lc-310-) | [Template 7](./topology_sorting.md#template-7-tree-centroid-finding-leaf-trimming-for-undirected-trees-) — leaf trimming | Medium |

**Worked in another sheet** — one home per problem, so these are pointers:

| LC | Problem | Worked in |
|---|---|---|
| 207 | Course Schedule | [graph.md § Template 4](./graph.md#template-4-topological-sort-kahns-bfs--lc-207-) |
| 802 | Find Eventual Safe States | [graph.md § Template 5](./graph.md#template-5-three-state-dfs-cycle-detection--lc-802-) |
| 547 | Number of Provinces | [graph.md § Template 1](./graph.md#template-1-connected-components-by-traversal--lc-547-) (traversal); [union_find_examples.md](./union_find_examples.md) (union-find) |
| 329 | Longest Increasing Path in a Matrix | [topology_sorting.md § Template 9](./topology_sorting.md#template-9-topological-sort--dp-on-an-implicit-dag-) |

## Kahn's Algorithm Examples

### Course Schedule II — LC 210 ⭐⭐⭐⭐⭐

> **Key point** — a course is popped only once all of its prerequisites have been popped, so
> the pop order **is** a valid schedule. A cycle leaves its courses with in-degree > 0 forever,
> so the order comes out short: `len(order) < numCourses` means no schedule exists.

**Reduces to**: [Template 1](./topology_sorting.md#template-1-bfs-kahns-algorithm-) — Kahn's algorithm returning the order instead of a yes/no (LC 207, [graph.md § Template 4](./graph.md#template-4-topological-sort-kahns-bfs--lc-207-), is the yes/no).

```python
# python
# LC 210 - Course Schedule II
# IDEA: Kahn's — pop courses whose prerequisites are all taken; a short order means a cycle
# time = O(V + E), space = O(V + E)
from collections import defaultdict, deque

def findOrder(numCourses, prerequisites):
    graph = defaultdict(list)
    in_degree = [0] * numCourses
    for course, pre in prerequisites:
        graph[pre].append(course)          # pre must come before course
        in_degree[course] += 1
    queue = deque(c for c in range(numCourses) if in_degree[c] == 0)
    order = []
    while queue:
        node = queue.popleft()
        order.append(node)                 # every prerequisite of node is already in order
        for nxt in graph[node]:
            in_degree[nxt] -= 1
            if in_degree[nxt] == 0:
                queue.append(nxt)
    return order if len(order) == numCourses else []

# findOrder(4, [[1,0],[2,0],[3,1],[3,2]]) -> [0, 1, 2, 3]
# findOrder(2, [[1,0],[0,1]])             -> []
```

```java
// java
// LC 210 - Course Schedule II
// IDEA: Kahn's — pop courses whose prerequisites are all taken; a short order means a cycle
// time = O(V + E), space = O(V + E)
import java.util.*;

class Solution {
    public int[] findOrder(int numCourses, int[][] prerequisites) {
        List<List<Integer>> graph = new ArrayList<>();
        for (int i = 0; i < numCourses; i++) {
            graph.add(new ArrayList<>());
        }
        int[] inDegree = new int[numCourses];
        for (int[] p : prerequisites) {
            graph.get(p[1]).add(p[0]);      // p[1] must come before p[0]
            inDegree[p[0]]++;
        }
        Deque<Integer> queue = new ArrayDeque<>();
        for (int i = 0; i < numCourses; i++) {
            if (inDegree[i] == 0) {
                queue.offer(i);
            }
        }
        int[] order = new int[numCourses];
        int size = 0;
        while (!queue.isEmpty()) {
            int node = queue.poll();
            order[size++] = node;
            for (int next : graph.get(node)) {
                if (--inDegree[next] == 0) {
                    queue.offer(next);
                }
            }
        }
        return size == numCourses ? order : new int[0];
    }
}
```

**Traps**
- `[a, b]` means *b before a*: the edge is `b → a`. Getting it backwards returns the reverse
  order, which still passes the cycle check and fails every ordering test.
- A course with no prerequisites and no dependents is still a node: seed the queue from
  `range(numCourses)`, not from the keys of `graph`.
- The DFS alternative (post-order over `course → prerequisite` edges) also works. Its
  three-state cycle check is [graph.md § Template 5](./graph.md#template-5-three-state-dfs-cycle-detection--lc-802-).

### Sequence Reconstruction — LC 444

> **Key point** — `nums` is the **only** shortest supersequence exactly when the topological
> order is unique, and the order is unique exactly when Kahn's queue **never holds more than
> one node**. Two nodes ready at once means two valid orders.

**Reduces to**: [Template 1](./topology_sorting.md#template-1-bfs-kahns-algorithm-) — Kahn's, with a uniqueness check on the queue and a match against `nums` at every pop.

```python
# python
# LC 444 - Sequence Reconstruction
# IDEA: edges from each adjacent pair; run Kahn's; fail if the queue ever holds 2 or the pop order leaves nums
# time = O(n + total sequence length), space = O(n + edges)
from collections import defaultdict, deque

def sequenceReconstruction(nums, sequences):
    n = len(nums)
    graph = defaultdict(set)
    in_degree = [0] * (n + 1)              # values are 1..n
    for seq in sequences:
        for a, b in zip(seq, seq[1:]):
            if b not in graph[a]:          # a duplicate edge must not count twice
                graph[a].add(b)
                in_degree[b] += 1
    queue = deque(v for v in range(1, n + 1) if in_degree[v] == 0)
    index = 0
    while queue:
        if len(queue) > 1:
            return False                   # two candidates for the next slot: not unique
        node = queue.popleft()
        if node != nums[index]:
            return False
        index += 1
        for nxt in graph[node]:
            in_degree[nxt] -= 1
            if in_degree[nxt] == 0:
                queue.append(nxt)
    return index == n

# sequenceReconstruction([1,2,3], [[1,2],[1,3]])       -> False  (2 and 3 can swap)
# sequenceReconstruction([1,2,3], [[1,2],[1,3],[2,3]]) -> True
```

**Traps**
- Count an edge once. `[[1,2],[1,2]]` would otherwise raise `in_degree[2]` to 2, and 2 would
  never be freed.
- A value that appears in no sequence has in-degree 0 from the start, so it shares the queue
  with the real first element and the check fails. That is the right answer: nothing fixes its place.

### Alien Dictionary — LC 269 ⭐⭐⭐⭐

> **Key point** — the only ordering fact a pair of adjacent words gives is at their **first
> differing letter**. Everything after it says nothing. Read one edge per adjacent pair, then
> topologically sort the letters. The one input no order can explain is a word followed by its
> own proper prefix (`"abc"` before `"ab"`).

**Reduces to**: [Template 1](./topology_sorting.md#template-1-bfs-kahns-algorithm-) — Kahn's, after the graph is built from the word list.

```python
# python
# LC 269 - Alien Dictionary
# IDEA: one edge per adjacent word pair (first differing letter); Kahn's over all letters; short order = cycle
# time = O(C), space = O(1)   C = total letters; the graph has at most 26 nodes
from collections import deque

def alienOrder(words):
    graph = {c: set() for word in words for c in word}   # every letter is a node, even with no edge
    in_degree = {c: 0 for c in graph}
    for first, second in zip(words, words[1:]):
        for a, b in zip(first, second):
            if a != b:                     # the first difference is the only fact this pair gives
                if b not in graph[a]:
                    graph[a].add(b)
                    in_degree[b] += 1
                break
        else:
            if len(first) > len(second):   # "abc" before "ab": no order can explain it
                return ""
    queue = deque(c for c in in_degree if in_degree[c] == 0)
    order = []
    while queue:
        c = queue.popleft()
        order.append(c)
        for nxt in graph[c]:
            in_degree[nxt] -= 1
            if in_degree[nxt] == 0:
                queue.append(nxt)
    return "".join(order) if len(order) == len(graph) else ""

# alienOrder(["wrt","wrf","er","ett","rftt"]) -> "wertf"
# alienOrder(["z","x","z"])                    -> ""   (z < x < z is a cycle)
```

**Traps**
- Stop at the first differing letter (`break`). Comparing every position adds false edges.
- The prefix case is only detectable when the loop finds **no** difference; that is what the
  `for … else` is for. `["abc", "ab"]` must return `""`.
- Letters that appear in no edge still belong in the answer: create a node for every letter
  of every word before reading edges.

## Parallel Task Scheduling Examples

### Parallel Courses — LC 1136

> **Key point** — taking every available course each semester is optimal, so one **level** of
> Kahn's BFS is one semester and the answer is the number of levels, the longest chain in the
> DAG. A cycle leaves courses untaken: return −1.

**Reduces to**: [Template 6](./topology_sorting.md#template-6-parallel-task-scheduling) — level-by-level Kahn's, with every task costing one unit of time.

```python
# python
# LC 1136 - Parallel Courses
# IDEA: Kahn's by levels; each level is one semester; fewer than n taken means a cycle
# time = O(V + E), space = O(V + E)
from collections import defaultdict, deque

def minimumSemesters(n, relations):
    graph = defaultdict(list)
    in_degree = [0] * (n + 1)              # courses are 1..n
    for pre, course in relations:
        graph[pre].append(course)
        in_degree[course] += 1
    queue = deque(c for c in range(1, n + 1) if in_degree[c] == 0)
    semesters = 0
    taken = 0
    while queue:
        semesters += 1                     # one BFS level = one semester
        for _ in range(len(queue)):
            course = queue.popleft()
            taken += 1
            for nxt in graph[course]:
                in_degree[nxt] -= 1
                if in_degree[nxt] == 0:
                    queue.append(nxt)      # unlocked now, taken next semester
    return semesters if taken == n else -1

# minimumSemesters(3, [[1,3],[2,3]])       -> 2
# minimumSemesters(3, [[1,2],[2,3],[3,1]]) -> -1
```

**Traps**
- Freeze `len(queue)` before the level loop. Courses unlocked during a semester are appended to
  the same deque and must wait for the next one.
- With a cap of `k` courses per semester (LC 1494) greedy levels are no longer optimal; that
  one is a bitmask DP.

## Leaf Trimming Examples

### Minimum Height Trees — LC 310 ⭐⭐⭐⭐

> **Key point** — the roots of minimum height are the **centre** of the tree's longest path, so
> peel the leaves off layer by layer until at most two nodes remain. A path has one centre or
> two, so the answer always has one node or two. Each node is peeled once, which makes it O(n);
> a BFS from every node is O(n²).

**Reduces to**: [Template 7](./topology_sorting.md#template-7-tree-centroid-finding-leaf-trimming-for-undirected-trees-) — Kahn's peeling on an undirected tree, with degree 1 as the "ready" test.

```python
# python
# LC 310 - Minimum Height Trees
# IDEA: peel all current leaves (degree 1) per round until <= 2 nodes remain; the survivors are the centres
# time = O(n), space = O(n)
from collections import deque

def findMinHeightTrees(n, edges):
    if n == 1:
        return [0]                         # no edges, so no leaf of degree 1
    graph = [[] for _ in range(n)]
    degree = [0] * n
    for u, v in edges:
        graph[u].append(v)
        graph[v].append(u)
        degree[u] += 1
        degree[v] += 1
    leaves = deque(i for i in range(n) if degree[i] == 1)
    remaining = n
    while remaining > 2:
        remaining -= len(leaves)           # peel one whole layer
        for _ in range(len(leaves)):
            leaf = leaves.popleft()
            for nxt in graph[leaf]:
                degree[nxt] -= 1
                if degree[nxt] == 1:       # nxt is a leaf of the trimmed tree
                    leaves.append(nxt)
    return list(leaves)

# findMinHeightTrees(4, [[1,0],[1,2],[1,3]])             -> [1]
# findMinHeightTrees(6, [[3,0],[3,1],[3,2],[3,4],[5,4]]) -> [3, 4]
```

```java
// java
// LC 310 - Minimum Height Trees
// IDEA: peel all current leaves (degree 1) per round until <= 2 nodes remain; the survivors are the centres
// time = O(n), space = O(n)
import java.util.*;

class Solution {
    public List<Integer> findMinHeightTrees(int n, int[][] edges) {
        if (n == 1) {
            return Collections.singletonList(0);
        }
        List<List<Integer>> graph = new ArrayList<>();
        for (int i = 0; i < n; i++) {
            graph.add(new ArrayList<>());
        }
        int[] degree = new int[n];
        for (int[] e : edges) {
            graph.get(e[0]).add(e[1]);
            graph.get(e[1]).add(e[0]);
            degree[e[0]]++;
            degree[e[1]]++;
        }
        Deque<Integer> leaves = new ArrayDeque<>();
        for (int i = 0; i < n; i++) {
            if (degree[i] == 1) {
                leaves.offer(i);
            }
        }
        int remaining = n;
        while (remaining > 2) {
            int layer = leaves.size();
            remaining -= layer;
            for (int i = 0; i < layer; i++) {
                int leaf = leaves.poll();
                for (int next : graph.get(leaf)) {
                    if (--degree[next] == 1) {
                        leaves.offer(next);
                    }
                }
            }
        }
        return new ArrayList<>(leaves);
    }
}
```

**Traps**
- `n == 1` has no edges and no node of degree 1; without the guard the answer is `[]`.
- Stop on `remaining > 2`, not on "queue empty". Peeling past two nodes deletes the centres.
- Peel a whole layer per round (freeze the size). Peeling one leaf at a time can strip one side
  of the tree faster than the other and stop off-centre.

**Similar** — LC 2603 (trim leaves without coins first) · LC 1245 (tree diameter: its midpoint is the centre)
