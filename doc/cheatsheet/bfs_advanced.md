# BFS — Advanced Variants

> **Scope** — The BFS techniques a first pass should skip: bidirectional BFS, 0-1 BFS with a deque, multi-source beyond the canonical template, BFS over implicit or augmented state spaces, and all-shortest-path DAG enumeration — the must-know queue templates stay in the main sheet.
> **See also**: [bfs.md](./bfs.md) — the templates these variants build on; [bfs_examples.md](./bfs_examples.md) — the classic BFS problems, one card each; [Dijkstra.md](./Dijkstra.md) — once edges carry arbitrary weights.

## LeetCode Problem Lists

- [Breadth-First Search](https://leetcode.com/problem-list/breadth-first-search/)
- [Shortest Path](https://leetcode.com/problem-list/shortest-path/)

## Overview

Everything here is a variant of a template in [bfs.md](./bfs.md): the queue payload changes, the
queue becomes a deque, the graph becomes implicit, or one BFS becomes many. Most of it is not asked
at L3. What is — augmented state (Pattern 16) and 0-1 BFS (Pattern 15) — shows up as a Medium/Hard
twist on a grid problem. The `Pattern N` names are the ones [bfs.md](./bfs.md) cites.

| Variant | Push / payload rule | Cost |
|---|---|---|
| Multi-source (canonical: [bfs § Template 4](./bfs.md#template-4-multi-source-bfs--lc-994-)) | all sources seeded at level 0 | O(V + E) |
| Independent BFS runs (Pattern 4.6) | fresh `visited` per source | O(k(V + E)) |
| Augmented state (Pattern 16) | `visited` keyed on `(node, resource)` | O(V · \|resource\|) |
| 0-1 BFS (Pattern 15) | deque: cost-0 front, cost-1 back | O(V + E) |
| Bidirectional | expand the smaller frontier | ~O(b^(d/2)) |

### Prerequisites

- [bfs § Template 2](./bfs.md#template-2-graph-bfs-mark-on-enqueue--lc-1971-): graph BFS with mark-on-enqueue. Every pattern below keeps that invariant.
- [bfs § Template 4](./bfs.md#template-4-multi-source-bfs--lc-994-): multi-source BFS. Patterns 4.5 and 4.6 extend it.
- [bfs § Template 5](./bfs.md#template-5-word-ladder-bfs-over-generated-neighbours--lc-127-): BFS over generated neighbours. Patterns 8, 8.5, 12 and the bidirectional search extend it.

## Recognition

| Signal in the statement | Constraint cue | Reach for | Not this if |
|---|---|---|---|
| "shortest bridge", connect two components with the fewest flips | grid ≤ 100 × 100 | Pattern 4.5 — mark one component, multi-source BFS out of all of it | only one source cell → plain [bfs § Template 3](./bfs.md#template-3-grid-bfs-with-a-direction-array--lc-1091-) |
| "**sum** of distances to all buildings", "reachable from every source" | k sources × grid ≤ 10⁶ | Pattern 4.6 — one BFS per source, fresh `visited` each | "distance to the **nearest** source" → one multi-source BFS |
| "minimum number of buses / transfers / lines" | routes ≤ 500 | Pattern 8 — BFS whose nodes are routes | counting stops, not transfers → BFS over stops |
| "return **all** shortest transformation sequences" | word list ≤ 500 | Pattern 8.5 — BFS builds a predecessor DAG, DFS enumerates it | one shortest length → [bfs § Template 5](./bfs.md#template-5-word-ladder-bfs-over-generated-neighbours--lc-127-) |
| "every combination", one choice from each of k independent groups | output size ≤ 10⁴ | Pattern 9 — one group per level | choices constrain each other → backtracking |
| "remove the **minimum** number of characters to make it valid", list every answer | n ≤ 25 | Pattern 12 — one deletion per level, stop at the first fruitful level | only the count is asked → a greedy counter |
| "collect all keys", "remove at most k obstacles", "at most k stops" | m·n·\|resource\| ≤ 10⁶ | Pattern 16 — `visited` on `(cell, resource)` | the resource never changes a later move → plain grid BFS |
| moves cost 0 or 1 ("follow the arrow for free", "flip a sign for 1") | grid ≤ 10⁴ cells | Pattern 15 — 0-1 BFS with a deque | weights larger than 1 → [Dijkstra.md](./Dijkstra.md) |
| both endpoints known, branching factor large | word list ≤ 5,000 | bidirectional BFS | one endpoint, or the target set is unknown → one-sided BFS |
| visit targets in a forced order (shortest tree first) | targets × grid ≤ 10⁶ | Pattern 6 — sort, then one BFS per consecutive pair | any order allowed → TSP over a distance matrix |

## Multi-Source BFS — Deep Dive

The canonical multi-source BFS and its distance variant, LC 542 01 Matrix, are worked in
[bfs § Template 4](./bfs.md#template-4-multi-source-bfs--lc-994-) and its
[01 Matrix card](./bfs.md#01-matrix--lc-542). The two patterns below are what that template cannot
do on its own: seed from a component that has to be found first, and keep the sources apart.

### Pattern 4.5: DFS + Multi-Source BFS (Island Expansion) — LC 934

> **Key point** — the shortest bridge between two islands starts from *some* cell of the first
> island, so seed the BFS with **all** of them. Flood-fill the first island (marking it `2` and
> enqueuing every cell), then BFS outward over water; the level at which the BFS first touches a
> `1` is the number of cells to flip.

**Recognise** — "exactly two islands", "flip the fewest `0`s to connect them", or any shortest
distance between two *components* rather than two cells. n ≤ 100, so one island can hold 10⁴ cells.

**Reduces to**: [bfs § Template 4](./bfs.md#template-4-multi-source-bfs--lc-994-) — the sources are a whole component found by a flood fill first.

```python
# python
# LC 934 - Shortest Bridge
# IDEA: flood-fill island A (mark 2, enqueue every cell), then multi-source BFS over water to island B
# time = O(n^2), space = O(n^2)   each cell is marked once by the fill and once by the BFS
from collections import deque

def shortestBridge(grid):
    n = len(grid)
    queue = deque()

    def mark(r, c):                        # iterative flood fill: no recursion limit
        stack = [(r, c)]
        grid[r][c] = 2
        while stack:
            r, c = stack.pop()
            queue.append((r, c))           # every cell of island A is a source
            for dr, dc in ((1, 0), (-1, 0), (0, 1), (0, -1)):
                nr, nc = r + dr, c + dc
                if 0 <= nr < n and 0 <= nc < n and grid[nr][nc] == 1:
                    grid[nr][nc] = 2
                    stack.append((nr, nc))

    first = next((r, c) for r in range(n) for c in range(n) if grid[r][c] == 1)
    mark(*first)
    flips = 0
    while queue:
        for _ in range(len(queue)):
            r, c = queue.popleft()
            for dr, dc in ((1, 0), (-1, 0), (0, 1), (0, -1)):
                nr, nc = r + dr, c + dc
                if not (0 <= nr < n and 0 <= nc < n) or grid[nr][nc] == 2:
                    continue
                if grid[nr][nc] == 1:
                    return flips           # touched island B
                grid[nr][nc] = 2           # water is now part of the bridge frontier
                queue.append((nr, nc))
        flips += 1
    return -1
```

```java
// java
// LC 934 - Shortest Bridge
// IDEA: DFS marks the first island and enqueues all of it; BFS over water until island B is touched
// time = O(n^2), space = O(n^2)
public int shortestBridge(int[][] grid) {
    int n = grid.length;
    Queue<int[]> queue = new LinkedList<>();
    boolean found = false;
    for (int y = 0; y < n && !found; y++) {
        for (int x = 0; x < n && !found; x++) {
            if (grid[y][x] == 1) {
                dfsMarkIsland(grid, x, y, queue);
                found = true;
            }
        }
    }
    int[][] dirs = {{1,0}, {-1,0}, {0,1}, {0,-1}};
    int steps = 0;
    boolean[][] visited = new boolean[n][n];
    while (!queue.isEmpty()) {
        int size = queue.size();
        for (int i = 0; i < size; i++) {
            int[] cur = queue.poll();
            int x = cur[0], y = cur[1];
            for (int[] d : dirs) {
                int nx = x + d[0], ny = y + d[1];
                if (nx >= 0 && nx < n && ny >= 0 && ny < n && !visited[ny][nx]) {
                    visited[ny][nx] = true;
                    if (grid[ny][nx] == 1) return steps;     // reached the second island
                    if (grid[ny][nx] == 0) queue.add(new int[]{nx, ny});
                }
            }
        }
        steps++;
    }
    return -1;
}

void dfsMarkIsland(int[][] grid, int x, int y, Queue<int[]> queue) {
    int n = grid.length;
    if (x < 0 || x >= n || y < 0 || y >= n || grid[y][x] != 1) return;
    grid[y][x] = 2;                       // part of the first island
    queue.add(new int[]{x, y});
    dfsMarkIsland(grid, x + 1, y, queue);
    dfsMarkIsland(grid, x - 1, y, queue);
    dfsMarkIsland(grid, x, y + 1, queue);
    dfsMarkIsland(grid, x, y - 1, queue);
}
```

**Complexity**
- **Time O(n²)**: the fill's `while stack` marks each island-A cell `2` before pushing it, and the
  BFS marks each water cell `2` before enqueuing it, so every cell enters a container at most once
  and looks at 4 neighbours.
- **Space O(n²)**: `queue` starts with every cell of island A, which can be most of the grid.

**Dry run**

```text
grid = [[0,1],      fill marks (0,1) as 2, queue = [(0,1)]
        [1,0]]
level 0 (flips = 0): (0,1) -> water (0,0), (1,1) enqueued
level 1 (flips = 1): (0,0) -> (1,0) is 1 -> return 1
```

**Traps**
- Seeding the BFS from one cell of island A finds *a* bridge from that cell, not the shortest one.
- The recursive fill on a 100 × 100 island is 10⁴ frames deep: past Python's default recursion
  limit, which is why the Python form fills with a stack.

**Not this if** — there is one source cell and one target, not two components: plain
[bfs § Template 3](./bfs.md#template-3-grid-bfs-with-a-direction-array--lc-1091-).

**Drill** — LC 934 ⭐ · LC 1020 Number of Enclaves *(the fill alone, seeded from the border)* ·
LC 542 *(every `0` a source, no fill)* · LC 1162 As Far from Land as Possible *(every land cell a
source; the answer is the last level)*

### Pattern 4.6: Multi-Source BFS vs Independent BFS Runs (Critical Distinction)

> **Key point** — one shared BFS seeded with every source answers "distance to the **nearest**
> source", because the first source to reach a cell wins and the cell is closed. A question about
> **every** source — the **sum** of distances, or "reachable from all of them" — needs each source's
> own distance to each cell, so it is one BFS per source with a **fresh** `visited` each time.

**Recognise** — "the **total** travel distance to all buildings", "a cell every source can reach",
any answer that aggregates over *all* sources rather than taking the nearest. m, n ≤ 50 with up to
m·n buildings, so k BFS runs of m·n each still fit.

| Aspect | Simultaneous multi-source | Independent BFS runs |
|---|---|---|
| Queue init | every source at once | each source starts its own BFS |
| `visited` | one, shared | fresh for each run |
| Time | O(m·n) | O(k · m·n), k = number of sources |
| First visit means | distance to the nearest source | distance from the current source |
| Example | LC 542, 994, 1162 | LC 317 |

The independent form, on LC 317 Shortest Distance from All Buildings (minimise the **total**
walking distance to every building):

```python
# python
# LC 317 - Shortest Distance from All Buildings
# IDEA: one BFS per building with a fresh visited; add its distances into total, count who reached each cell
# time = O(k * m * n), space = O(m * n)   k = number of buildings
from collections import deque

def shortestDistance(grid):
    m, n = len(grid), len(grid[0])
    total = [[0] * n for _ in range(m)]    # summed distance from every building so far
    reach = [[0] * n for _ in range(m)]    # how many buildings reached this cell
    buildings = 0
    for sr in range(m):
        for sc in range(n):
            if grid[sr][sc] != 1:
                continue
            buildings += 1
            seen = [[False] * n for _ in range(m)]     # fresh for every building
            queue = deque([(sr, sc)])
            seen[sr][sc] = True
            dist = 0
            while queue:
                dist += 1
                for _ in range(len(queue)):
                    r, c = queue.popleft()
                    for dr, dc in ((1, 0), (-1, 0), (0, 1), (0, -1)):
                        nr, nc = r + dr, c + dc
                        if 0 <= nr < m and 0 <= nc < n and not seen[nr][nc] and grid[nr][nc] == 0:
                            seen[nr][nc] = True
                            total[nr][nc] += dist
                            reach[nr][nc] += 1
                            queue.append((nr, nc))
    return min((total[r][c] for r in range(m) for c in range(n)
                if grid[r][c] == 0 and reach[r][c] == buildings), default=-1)
```

```java
// java
// LC 317 - Shortest Distance from All Buildings
// IDEA: independent BFS per building, fresh visited each; accumulate distance and reach count
// time = O(k * m * n), space = O(m * n)
public int shortestDistance(int[][] grid) {
    int rows = grid.length, cols = grid[0].length;
    int[][] totalDist = new int[rows][cols];
    int[][] reachCount = new int[rows][cols];
    int buildingCount = 0;
    for (int r = 0; r < rows; r++) {
        for (int c = 0; c < cols; c++) {
            if (grid[r][c] == 1) {
                buildingCount++;
                boolean[][] visited = new boolean[rows][cols];   // fresh for this building
                bfsSingleSource(grid, r, c, visited, totalDist, reachCount);
            }
        }
    }
    int minDist = Integer.MAX_VALUE;
    for (int r = 0; r < rows; r++)
        for (int c = 0; c < cols; c++)
            if (grid[r][c] == 0 && reachCount[r][c] == buildingCount)
                minDist = Math.min(minDist, totalDist[r][c]);
    return minDist == Integer.MAX_VALUE ? -1 : minDist;
}

private void bfsSingleSource(int[][] grid, int sr, int sc, boolean[][] visited,
                             int[][] totalDist, int[][] reachCount) {
    Queue<int[]> queue = new LinkedList<>();
    queue.offer(new int[]{sr, sc});
    visited[sr][sc] = true;
    int[][] dirs = {{0,1}, {0,-1}, {1,0}, {-1,0}};
    int dist = 0;
    while (!queue.isEmpty()) {
        int size = queue.size();
        dist++;
        for (int i = 0; i < size; i++) {
            int[] cur = queue.poll();
            for (int[] d : dirs) {
                int nr = cur[0] + d[0], nc = cur[1] + d[1];
                if (nr >= 0 && nr < grid.length && nc >= 0 && nc < grid[0].length
                        && !visited[nr][nc] && grid[nr][nc] == 0) {
                    visited[nr][nc] = true;
                    totalDist[nr][nc] += dist;
                    reachCount[nr][nc]++;
                    queue.offer(new int[]{nr, nc});
                }
            }
        }
    }
}
```

**Complexity**
- **Time O(k·m·n)**: the outer `for sr / for sc` starts one BFS per building, and each BFS enqueues
  every empty cell at most once because its `seen` grid is fresh.
- **Space O(m·n)**: `total`, `reach` and one `seen` grid at a time; each building's `seen` is
  dropped before the next is built.

**Traps**
- Reusing one `visited` across buildings: the second building never sees the cells the first one
  closed, so its distances are never added and the answer is wrong, not just slow.
- An empty cell that some building cannot reach is not a candidate: check `reach == buildings`.
- The space-saving trick replaces `visited` with the grid itself: the i-th BFS walks only cells
  whose value is `-i` and writes `-(i + 1)`, so later BFSs skip every cell an earlier building
  could not reach. Same answer, no `visited` arrays.

**Not this if** — the question is about the **nearest** source: one
[multi-source BFS](./bfs.md#template-4-multi-source-bfs--lc-994-) gives the same distances k times faster.

**Drill** — LC 317 ⭐ · LC 542 *(nearest source: the one shared BFS, for contrast)* · LC 1162 *(the
same contrast, maximised)* · LC 296 Best Meeting Point *(sum of distances with no obstacles:
Manhattan medians, no BFS needed)*

## State-Space & Implicit-Graph BFS

The canonical state-space BFS — generate each neighbour, test it against a dictionary, restore the
character — is [bfs § Template 5](./bfs.md#template-5-word-ladder-bfs-over-generated-neighbours--lc-127-),
worked on its anchor LC 127 with the restore step in its Traps. Its classic problems (LC 752,
LC 773, LC 279) are cards in [bfs_examples.md](./bfs_examples.md). The patterns below change what
a node *is*.

### Pattern 8: BFS on Abstract Graph (Route-Level BFS) — LC 815

> **Key point** — the answer counts **buses**, so make each route a node: two routes are adjacent
> when they share a stop. Seed every route through `source`; BFS level = buses taken, and the first
> route that contains `target` ends the search. A `visited` set on stops keeps a busy transfer stop
> from re-expanding every route through it.

**Recognise** — "the least number of **buses** you must take", "fewest transfers / lines", with
each route given as a list of stops. routes ≤ 500 but the stops add up to 10⁵, so a graph with an
edge between every two stops on a route is too big to build.

**Reduces to**: [bfs § Template 2](./bfs.md#template-2-graph-bfs-mark-on-enqueue--lc-1971-) — on a graph whose nodes are routes, built from a stop → routes map.

```python
# python
# LC 815 - Bus Routes
# IDEA: BFS over routes; a stop links every route through it; level = buses taken
# time = O(sum of route lengths), space = O(sum of route lengths)
from collections import defaultdict, deque

def numBusesToDestination(routes, source, target):
    if source == target:
        return 0
    stop_to_routes = defaultdict(list)
    for i, route in enumerate(routes):
        for stop in route:
            stop_to_routes[stop].append(i)
    queue = deque()
    used_routes = set()
    for r in stop_to_routes[source]:       # every bus through the source is ride 1
        queue.append(r)
        used_routes.add(r)
    seen_stops = {source}
    buses = 1
    while queue:
        for _ in range(len(queue)):
            route = queue.popleft()
            for stop in routes[route]:
                if stop == target:
                    return buses
                if stop in seen_stops:
                    continue               # this stop's transfers were already enqueued
                seen_stops.add(stop)
                for nxt in stop_to_routes[stop]:
                    if nxt not in used_routes:
                        used_routes.add(nxt)
                        queue.append(nxt)
        buses += 1
    return -1
```

```java
// java
// LC 815 - Bus Routes
// IDEA: build stop -> routes; BFS on route ids with two visited sets (routes and stops)
// time = O(sum of route lengths), space = O(sum of route lengths)
public int numBusesToDestination(int[][] routes, int source, int target) {
    if (source == target) return 0;
    Map<Integer, List<Integer>> stopToRoutes = new HashMap<>();
    for (int i = 0; i < routes.length; i++)
        for (int stop : routes[i])
            stopToRoutes.computeIfAbsent(stop, k -> new ArrayList<>()).add(i);
    Queue<Integer> queue = new LinkedList<>();
    Set<Integer> visitedRoutes = new HashSet<>();
    Set<Integer> visitedStops = new HashSet<>();
    for (int routeId : stopToRoutes.getOrDefault(source, new ArrayList<>())) {
        queue.offer(routeId);
        visitedRoutes.add(routeId);
    }
    int busCount = 1;                     // already on the first bus
    while (!queue.isEmpty()) {
        int size = queue.size();
        for (int i = 0; i < size; i++) {
            int currRoute = queue.poll();
            for (int stop : routes[currRoute]) {
                if (stop == target) return busCount;
                if (!visitedStops.add(stop)) continue;
                for (int nextRoute : stopToRoutes.getOrDefault(stop, new ArrayList<>())) {
                    if (visitedRoutes.add(nextRoute)) queue.offer(nextRoute);
                }
            }
        }
        busCount++;
    }
    return -1;
}
```

**Complexity**
- **Time O(S)**, S = the sum of route lengths: building `stop_to_routes` reads every stop once;
  `used_routes` enqueues each route once, so `for stop in routes[route]` scans each route once; and
  `seen_stops` lets each stop's `stop_to_routes` list be scanned once.
- **Space O(S)**: `stop_to_routes` holds one entry per (stop, route) pair.

**Dry run**

```text
routes = [[1,2,7],[3,6,7]], source = 1, target = 6
stop -> routes: 1:[0] 2:[0] 7:[0,1] 3:[1] 6:[1]
buses 1: route 0 -> stops 1, 2, 7; stop 7 enqueues route 1
buses 2: route 1 -> stop 3, stop 6 == target -> return 2
```

**Traps**
- BFS over stops with an edge between every two stops on a route is O(L²) per route and counts
  stops, not buses.
- `source == target` is 0 buses, even when no route passes through it.
- Without `seen_stops`, a hub stop on many routes re-scans its route list once per route that
  reaches it.

**Not this if** — the answer counts stops or edges, not routes: BFS over the stops,
[bfs § Template 2](./bfs.md#template-2-graph-bfs-mark-on-enqueue--lc-1971-).

**Drill** — LC 815 ⭐ · LC 841 Keys and Rooms *(a room's keys are its edges)* · LC 127 *(words as
nodes)* · LC 1345 Jump Game IV *(value → indices map; clear a value's list once used, the same job
as `seen_stops`)*

---

### Pattern 8.5: BFS + DFS (Find All Shortest Paths - DAG Enumeration) — LC 126

> **Key point** — BFS by levels, and for every word record **all** of its predecessors on the
> previous level. Remove a whole level from the dictionary only after the level is finished, so a
> word reached by two parents on the same level keeps both. Stop after the level that reaches the
> end word; then DFS backwards through the predecessor lists, which hold only shortest paths.

**Recognise** — "return **all** the shortest transformation sequences", "every shortest path from
s to t" — a list of paths, not a length. word list ≤ 500 and words of length ≤ 5, but the number of
shortest paths can still be exponential, so only shortest ones may ever be walked.

**Reduces to**: [bfs § Template 5](./bfs.md#template-5-word-ladder-bfs-over-generated-neighbours--lc-127-) — with a predecessor DAG recorded instead of a single distance.

```python
# python
# LC 126 - Word Ladder II
# IDEA: level BFS records parents[word] = every word one level above it; DFS back from endWord
# time = O(N * L * 26 + paths * L), space = O(N * L)   N words of length L; output not counted
from collections import defaultdict

def findLadders(beginWord, endWord, wordList):
    words = set(wordList)
    if endWord not in words:
        return []
    parents = defaultdict(list)
    level = {beginWord}
    words.discard(beginWord)
    found = False
    while level and not found:
        words -= level                     # remove the WHOLE level only after it is built
        next_level = set()
        for word in level:
            for i in range(len(word)):
                for ch in 'abcdefghijklmnopqrstuvwxyz':
                    nxt = word[:i] + ch + word[i + 1:]
                    if nxt in words:
                        if nxt == endWord:
                            found = True   # finish this level, never build the next
                        next_level.add(nxt)
                        parents[nxt].append(word)
        level = next_level
    if not found:
        return []

    paths, path = [], [endWord]

    def walk(word):                        # DFS from endWord back to beginWord
        if word == beginWord:
            paths.append(path[::-1])
            return
        for p in parents[word]:
            path.append(p)
            walk(p)
            path.pop()

    walk(endWord)
    return paths
```

```java
// java
// LC 126 - Word Ladder II
// IDEA: BFS records every predecessor at the shortest distance; DFS from endWord enumerates the DAG
// time = O(N * L * 26 + paths * L), space = O(N * L)
public List<List<String>> findLadders(String beginWord, String endWord, List<String> wordList) {
    List<List<String>> result = new ArrayList<>();
    Set<String> wordSet = new HashSet<>(wordList);
    if (!wordSet.contains(endWord)) return result;
    Map<String, List<String>> parents = new HashMap<>();
    Map<String, Integer> distances = new HashMap<>();
    Queue<String> queue = new LinkedList<>();
    queue.add(beginWord);
    distances.put(beginWord, 0);
    boolean found = false;
    while (!queue.isEmpty() && !found) {
        int size = queue.size();
        for (int i = 0; i < size; i++) {
            String word = queue.poll();
            char[] chars = word.toCharArray();
            for (int j = 0; j < chars.length; j++) {
                char original = chars[j];
                for (char c = 'a'; c <= 'z'; c++) {
                    if (c == original) continue;
                    chars[j] = c;
                    String nextWord = new String(chars);
                    if (!wordSet.contains(nextWord)) continue;
                    int newDistance = distances.get(word) + 1;
                    if (!distances.containsKey(nextWord)) {             // first time: enqueue once
                        distances.put(nextWord, newDistance);
                        parents.computeIfAbsent(nextWord, k -> new ArrayList<>()).add(word);
                        queue.add(nextWord);
                        if (nextWord.equals(endWord)) found = true;
                    } else if (distances.get(nextWord) == newDistance) {  // another shortest parent
                        parents.get(nextWord).add(word);
                    }
                }
                chars[j] = original;
            }
        }
    }
    if (distances.containsKey(endWord)) {
        dfsEnumeratePaths(endWord, beginWord, parents, new LinkedList<>(), result);
    }
    return result;
}

private void dfsEnumeratePaths(String current, String beginWord, Map<String, List<String>> parents,
                               List<String> path, List<List<String>> result) {
    path.add(0, current);                 // building the path backwards
    if (current.equals(beginWord)) {
        result.add(new ArrayList<>(path));
    } else {
        for (String prev : parents.getOrDefault(current, new ArrayList<>())) {
            dfsEnumeratePaths(prev, beginWord, parents, path, result);
        }
    }
    path.remove(0);
}
```

**Complexity**
- **Time O(N·L·26 + paths·L)**: `words -= level` removes each word as its level closes, so each of
  the N words is expanded once by the `for i / for ch` loops (26·L candidates, each a length-L
  string, which the bound treats as constant for L ≤ 5); `walk` then spends O(L) per output path.
- **Space O(N·L)**: `parents` holds each word's predecessors on the level above; the output is not
  counted.

**Dry run**

```text
hit -> cog, words = [hot, dot, dog, lot, log, cog]
level 1: hot            parents[hot] = [hit]
level 2: dot, lot       parents[dot] = [hot], parents[lot] = [hot]
level 3: dog, log       parents[dog] = [dot], parents[log] = [lot]
level 4: cog            parents[cog] = [dog, log]   <- two parents on one level: found, stop
DFS from cog: cog<-dog<-dot<-hot<-hit  and  cog<-log<-lot<-hot<-hit
```

**Traps**
- Removing a word from the dictionary the moment it is first reached drops its second parent on
  the same level (`cog` above), and with it every path through `log`.
- Running DFS forward from `beginWord` over the whole word graph explores paths longer than the
  shortest and times out; walk the predecessor lists backwards.
- Building the next level after `found` admits longer paths into `parents`.

**Not this if** — only the length, or any one shortest path, is asked: [bfs § Template 5](./bfs.md#template-5-word-ladder-bfs-over-generated-neighbours--lc-127-)
plus a single parent per word.

**Drill** — LC 126 ⭐ · LC 433 Minimum Genetic Mutation *(the length only)* · LC 127 *(the length
only, on a larger list)* · LC 1976 Number of Ways to Arrive at Destination *(count the shortest
paths instead of listing them; weighted, so Dijkstra)*

---

### Pattern 9: BFS-Style Cartesian Product Generation (Level-by-Level Combination Building) — LC 1087

> **Key point** — when the output is one choice from each of k independent groups, each group is
> one BFS level: draining level i and appending every option of group i to every prefix leaves
> exactly the Cartesian product of groups `0 … i`. There is no graph and no `visited`; the level
> structure is all that is borrowed from BFS.

**Recognise** — "return all words that can be formed", "`{a,b}c{d,e}`", "every combination" where
each position picks one option from its own group and no choice limits another. The output caps
the work: LC 1087 has s ≤ 50 characters, so the product stays small.

**Reduces to**: [bfs § Template 1](./bfs.md#template-1-level-order-bfs--lc-102-) — one level per group, where a node's children are its prefix extended by each option.

```python
# python
# LC 1087 - Brace Expansion
# IDEA: parse into groups, sort each; level i = every prefix extended by every option of group i
# time = O(G * |result| * L), space = O(|result| * L)   G groups, L = output word length
def expand(s):
    groups = []
    i = 0
    while i < len(s):
        if s[i] == '{':
            j = s.index('}', i)
            groups.append(sorted(s[i + 1:j].split(',')))   # sorted -> output is sorted
            i = j + 1
        else:
            groups.append([s[i]])
            i += 1
    level = [""]
    for group in groups:
        next_level = []
        for prefix in level:
            for option in group:
                next_level.append(prefix + option)
        level = next_level
    return level
```

```java
// java
// LC 1087 - Brace Expansion (the level loop, given the parsed groups)
// IDEA: snapshot the queue size, extend every prefix of this level by every option of the group
// time = O(G * |result| * L), space = O(|result| * L)
public String[] cartesianBFS(List<List<String>> groups) {
    Queue<String> queue = new LinkedList<>();
    queue.add("");                        // one empty prefix at depth 0
    for (List<String> group : groups) {
        int size = queue.size();          // snapshot the current level
        for (int k = 0; k < size; k++) {
            String prefix = queue.poll();
            for (String option : group) queue.add(prefix + option);
        }
    }
    return queue.toArray(new String[0]);
}
```

**Complexity**
- **Time O(G·|result|·L)**: the `for group` loop runs G times, each level is no larger than the
  final one, and every `prefix + option` copies up to L characters.
- **Space O(|result|·L)**: `level` and `next_level` together hold at most two levels, the larger
  being the output itself.

**Dry run**

```text
s = "{a,b}c{d,e}f"   groups = [[a,b], [c], [d,e], [f]]
level 0: [""]
level 1: [a, b]
level 2: [ac, bc]
level 3: [acd, ace, bcd, bce]
level 4: [acdf, acef, bcdf, bcef]   <- the answer, already sorted
```

**Traps**
- Sort each group before expanding; sorting the output afterwards costs an extra
  O(|result| log |result|). `"{b,a}c"` expanded unsorted returns `["bc", "ac"]`.
- The final level holds the whole output at once; when choices constrain each other (no repeated
  letter, a sum bound), switch to backtracking, which can prune and needs only O(depth) memory.

**Not this if** — the choices constrain each other: backtracking
([dfs § Template 4](./dfs.md#template-4-backtracking--lc-46)), which prunes a dead prefix early.

**Drill** — LC 1087 ⭐ · LC 784 Letter Case Permutation *(a group is a letter's two cases)* · LC 17
Letter Combinations of a Phone Number *(a group per digit)* · LC 320 Generalized Abbreviation *(each
letter: keep it or count it)* · LC 1096 Brace Expansion II *(nested braces: union and product,
parsed with a stack)*

---

### Pattern 12: BFS over String States — Stop at the First Fruitful Level — LC 301 ⭐⭐⭐⭐⭐

> **Key point** — "remove the **minimum** number of characters" makes one deletion one BFS level:
> level k holds every string reachable by exactly k deletions. The first level that contains a
> valid string is the answer level — return all its valid strings and never build the next.

**Recognise** — "remove the **minimum** number of invalid parentheses", "return **all** possible
results". s ≤ 25 with at most 20 parentheses: only that bound makes a level set of up to 2ⁿ
strings affordable.

**Reduces to**: [bfs § Template 5](./bfs.md#template-5-word-ladder-bfs-over-generated-neighbours--lc-127-) — neighbours are the string with one parenthesis deleted.

**Why BFS beats DFS here**: DFS finds *some* valid string but you'd still have to prove minimality; BFS gets minimality for free from the level number, and returns **all** answers of that length in one shot.

```python
# python
# LC 301 - Remove Invalid Parentheses
# IDEA: level = number of deletions; return the first level that has valid strings
# time = O(2^n * n), space = O(2^n)
def removeInvalidParentheses(s):
    def valid(t):
        cnt = 0
        for ch in t:
            if ch == '(':
                cnt += 1
            elif ch == ')':
                cnt -= 1
                if cnt < 0:
                    return False
        return cnt == 0

    level = {s}                       # a set IS the visited-dedup for this level
    while level:
        found = [t for t in level if valid(t)]
        if found:
            return found              # minimal deletions -> all answers of this size
        nxt = set()
        for t in level:
            for i, ch in enumerate(t):
                if ch in '()':        # letters are never removed
                    nxt.add(t[:i] + t[i + 1:])
        level = nxt
    return [""]
```

```java
// java
// LC 301 - Remove Invalid Parentheses
// IDEA: 1 BFS level = 1 deletion. First level containing a valid string is the answer level.
// time = O(2^n * n), space = O(2^n)
public List<String> removeInvalidParentheses(String s) {
    List<String> res = new ArrayList<>();
    Set<String> visited = new HashSet<>();
    Queue<String> q = new LinkedList<>();
    q.offer(s);
    visited.add(s);
    boolean found = false;
    while (!q.isEmpty()) {
        int size = q.size();
        for (int i = 0; i < size; i++) {
            String cur = q.poll();
            if (isValid(cur)) { res.add(cur); found = true; }
            if (found) continue;               // drain this level, but stop expanding
            for (int j = 0; j < cur.length(); j++) {
                char c = cur.charAt(j);
                if (c != '(' && c != ')') continue;   // only parens are removable
                String next = cur.substring(0, j) + cur.substring(j + 1);
                if (visited.add(next)) q.offer(next); // add() returns false if dup
            }
        }
        if (found) return res;                 // this level is minimal -> done
    }
    return res;
}

private boolean isValid(String t) {
    int cnt = 0;
    for (char c : t.toCharArray()) {
        if (c == '(') cnt++;
        else if (c == ')' && --cnt < 0) return false;  // ')' before its '('
    }
    return cnt == 0;
}
```

**Complexity**
- **Time O(2ⁿ·n)**: a string is a subset of the original positions, so at most 2ⁿ distinct strings
  ever enter a `level` set, and `valid(t)` scans each in O(n).
- **Space O(2ⁿ)**: `level` and `nxt` — the widest level, about C(n, n/2) strings.

**Traps**
- Without dedup, `"(())"` is reached by many deletion orders and the levels blow up.
- Stop **expanding** once a valid string is found, but finish scanning the level: there may be
  several answers of the same length. `"()())()"` has two, `"(())()"` and `"()()()"`.
- `")("` has no valid non-empty answer; the loop reaches the empty string, which is valid, and
  returns `[""]`.

**Not this if** — only the **number** of deletions, or any one valid string, is asked: a single
left-to-right balance counter (LC 921) or a stack (LC 1249), both O(n).

**Drill** — LC 301 ⭐ · LC 20 Valid Parentheses *(the validity check)* · LC 921 *(the count only)* ·
LC 1249 Minimum Remove to Make Valid Parentheses *(one answer: a stack, O(n))*

---

### Pattern 16: BFS over an Augmented State — `(cell, resource)` — LC 864 & LC 1293 ⭐⭐⭐⭐⭐

> **Key point** — plain grid BFS keys `visited` on the **cell**. That is only correct when arriving
> at a cell makes every future identical. The moment you carry something along the walk — keys
> collected, eliminations left, fuel, a colour, a parity — two arrivals at the same cell are
> **different positions in the search**, and the fix is always the same one line.

**Recognise** — "collect **all keys**, a lock needs its key", "you may eliminate **at most k**
obstacles", "at most k stops": something carried along the walk changes which moves are legal
later. The state count m·n·|resource| has to fit — LC 864 is 30·30·2⁶, LC 1293 is 40·40·(k+1).

**Reduces to**: [bfs § Template 3](./bfs.md#template-3-grid-bfs-with-a-direction-array--lc-1091-) — with the resource added to the `visited` key.

```text
visited on (r, c)              ->  visited on (r, c, resource)
```

Everything else stays a textbook queue BFS, so the first time you pop the goal you still have the
shortest path. The cost is the state count: `m * n * |resource|`.

| Signal in the statement | The extra dimension |
|---|---|
| "collect keys, doors need the matching key" (LC 864) | key bitmask, `2^k` values |
| "you may remove at most `k` obstacles" (LC 1293) | eliminations **remaining**, `k+1` values |
| "at most `k` stops / edges" (LC 787) | edges used so far |
| "you may reverse at most one edge" | a 0/1 flag |
| "moves alternate between two players / colours" (LC 1129) | last colour used |

> **Why `k` must be part of the key, not a `best[r][c]` scalar** — reaching a cell with *more*
> budget left is never worse, so a cell is worth revisiting when the new arrival has strictly more
> budget. Keying only on the cell throws that arrival away and reports `-1` on grids that are
> solvable. Keying on `(cell, k)` is always correct; `best[r][c] = max budget seen` is the same
> thing compressed, and is the usual memory optimisation.

#### LC 864 — the resource is a bitmask of keys

> **Key point** — lowercase `a..f` are keys and uppercase `A..F` are locks; `k ≤ 6`, so the key
> ring fits in 6 bits and the state space is `m * n * 64`. A lock is passable only when its bit is
> already in the mask; the answer is the first pop holding every key.

**Recognise** — "the lowest number of moves to acquire all keys", with `a..f` keys and `A..F` locks
on the grid. k ≤ 6 is the cue that the key ring is a bitmask.

**Reduces to**: [bfs § Template 3](./bfs.md#template-3-grid-bfs-with-a-direction-array--lc-1091-) — with `visited` on `(r, c, mask)`.

```python
# python
# LC 864 - Shortest Path to Get All Keys
# IDEA: same BFS, state = (r, c, keyMask); picking up a key moves you to a DIFFERENT
#       layer of the search space, which is why a cell can be visited up to 2^k times
# time = O(m*n*2^k), space = O(m*n*2^k)
from collections import deque

def shortestPathAllKeys(grid):
    m, n = len(grid), len(grid[0])
    all_keys = 0
    start = (0, 0)
    for r in range(m):
        for c in range(n):
            ch = grid[r][c]
            if ch == '@':
                start = (r, c)
            elif ch.islower():
                all_keys |= 1 << (ord(ch) - ord('a'))

    q = deque([(start[0], start[1], 0, 0)])       # r, c, mask, steps
    seen = {(start[0], start[1], 0)}
    while q:
        r, c, mask, steps = q.popleft()
        if mask == all_keys:
            return steps
        for dr, dc in ((0, 1), (0, -1), (1, 0), (-1, 0)):
            nr, nc = r + dr, c + dc
            if not (0 <= nr < m and 0 <= nc < n):
                continue
            ch = grid[nr][nc]
            if ch == '#':
                continue
            if ch.isupper() and not (mask >> (ord(ch) - ord('A'))) & 1:
                continue                          # locked door, no key -> a wall
            n_mask = mask | (1 << (ord(ch) - ord('a'))) if ch.islower() else mask
            if (nr, nc, n_mask) in seen:
                continue
            seen.add((nr, nc, n_mask))
            q.append((nr, nc, n_mask, steps + 1))
    return -1
```

```java
// java
// LC 864 - Shortest Path to Get All Keys
// IDEA: BFS over (row, col, keyMask). A lock is passable only when its bit is already
//       in the mask; stepping on a key ORs its bit in. Answer = first pop with all keys.
// time = O(m*n*2^k), space = O(m*n*2^k)
public int shortestPathAllKeys(String[] grid) {
    int m = grid.length, n = grid[0].length();
    int startR = 0, startC = 0, allKeys = 0;
    for (int r = 0; r < m; r++) {
        for (int c = 0; c < n; c++) {
            char ch = grid[r].charAt(c);
            if (ch == '@') { startR = r; startC = c; }
            else if (ch >= 'a' && ch <= 'f') allKeys |= 1 << (ch - 'a');
        }
    }

    int[][] dirs = {{0,1},{0,-1},{1,0},{-1,0}};
    boolean[][][] seen = new boolean[m][n][1 << 6];   // the mask is part of the visited key
    Queue<int[]> q = new LinkedList<>();
    q.offer(new int[]{startR, startC, 0});
    seen[startR][startC][0] = true;

    int steps = 0;
    while (!q.isEmpty()) {
        int size = q.size();
        for (int s = 0; s < size; s++) {
            int[] cur = q.poll();
            int r = cur[0], c = cur[1], mask = cur[2];
            if (mask == allKeys) return steps;

            for (int[] d : dirs) {
                int nr = r + d[0], nc = c + d[1];
                if (nr < 0 || nr >= m || nc < 0 || nc >= n) continue;
                char ch = grid[nr].charAt(nc);
                if (ch == '#') continue;
                // a lock we have no key for is a wall
                if (ch >= 'A' && ch <= 'F' && (mask & (1 << (ch - 'A'))) == 0) continue;

                int nMask = mask;
                if (ch >= 'a' && ch <= 'f') nMask |= 1 << (ch - 'a');
                if (seen[nr][nc][nMask]) continue;
                seen[nr][nc][nMask] = true;
                q.offer(new int[]{nr, nc, nMask});
            }
        }
        steps++;
    }
    return -1;
}
```

**Complexity**
- **Time O(m·n·2^k)**: `seen` is keyed on `(nr, nc, n_mask)`, so each of the m·n·2^k states is
  enqueued once and tries 4 moves; the scan for `all_keys` is only O(m·n).
- **Space O(m·n·2^k)**: `seen` and the queue range over those states.

**Traps**
- Marking `seen` on the **cell**: LC 864 then reports `-1` whenever the path must cross its own
  earlier route after picking a key up, which is most of the test set.
- Counting a key you already hold as a new state — harmless but doubles the queue; `n_mask == mask`
  is caught by the `seen` check anyway.

**Drill** — LC 864 ⭐ · LC 1129 Shortest Path with Alternating Colors *(`(node, lastColour)`)* ·
LC 847 Shortest Path Visiting All Nodes *(`(node, visitedMask)`, every node a source — see
[dp_bitmask.md](./dp_bitmask.md))*

#### LC 1293 — the resource is a countdown

> **Key point** — the extra dimension is "obstacles I may still remove". An arrival is worth
> keeping only when it has **strictly more** budget than any earlier arrival at that cell, so a
> `best[r][c]` of the largest budget seen replaces a 3-D `visited`.

**Recognise** — "you can eliminate **at most k** obstacles", "minimum steps from the upper-left to
the lower-right corner". m, n ≤ 40 and k ≤ m·n, so k has to be capped before it multiplies the
state count.

**Reduces to**: [bfs § Template 3](./bfs.md#template-3-grid-bfs-with-a-direction-array--lc-1091-) — with the budget carried in the queue entry.

Two things are worth carrying into the interview:

- **The shortcut.** If `k >= m + n - 2` you can bulldoze straight through, so the answer is the
  Manhattan distance `m + n - 2` and that branch returns in `O(1)`. It is worth more than one early
  exit, though: past it every surviving input has `k < m + n - 2`, so the bound is
  `O(m*n*(m+n))` rather than growing with an unbounded `k`.
- **`best[r][c]` instead of a 3-D `visited`.** Store the largest remaining budget ever seen at a
  cell and skip any arrival that is not strictly better. Same answers, and the visited table is
  `O(m*n)` instead of `O(m*n*k)`.

```python
# python
# LC 1293 - Shortest Path in a Grid with Obstacles Elimination
# IDEA: BFS over (r, c, k remaining); prune with the best budget ever seen at a cell
# time = O(m*n*k), space = O(m*n*k)   the queue's worst case; best[][] itself is O(m*n)
from collections import deque

def shortestPath(grid, k):
    m, n = len(grid), len(grid[0])
    if k >= m + n - 2:
        return m + n - 2

    best = [[-1] * n for _ in range(m)]
    best[0][0] = k
    q = deque([(0, 0, k, 0)])                     # r, c, k left, steps
    while q:
        r, c, left, steps = q.popleft()
        if (r, c) == (m - 1, n - 1):
            return steps
        for dr, dc in ((0, 1), (0, -1), (1, 0), (-1, 0)):
            nr, nc = r + dr, c + dc
            if not (0 <= nr < m and 0 <= nc < n):
                continue
            n_left = left - grid[nr][nc]
            if n_left < 0 or n_left <= best[nr][nc]:
                continue                          # out of budget, or already been here richer
            best[nr][nc] = n_left
            q.append((nr, nc, n_left, steps + 1))
    return -1
```

```java
// java
// LC 1293 - Shortest Path in a Grid with Obstacles Elimination
// IDEA: BFS over (row, col, k left). best[r][c] = most budget ever seen here; an arrival
//       with <= that budget can never do better, so drop it.
// time = O(m*n*k), space = O(m*n*k)   the queue's worst case; best[][] itself is O(m*n)
public int shortestPath(int[][] grid, int k) {
    int m = grid.length, n = grid[0].length;
    if (k >= m + n - 2) return m + n - 2;        // enough budget to walk the diagonal

    int[][] best = new int[m][n];
    for (int[] row : best) Arrays.fill(row, -1);
    int[][] dirs = {{0,1},{0,-1},{1,0},{-1,0}};

    Queue<int[]> q = new LinkedList<>();
    q.offer(new int[]{0, 0, k});
    best[0][0] = k;

    int steps = 0;
    while (!q.isEmpty()) {
        int size = q.size();
        for (int s = 0; s < size; s++) {
            int[] cur = q.poll();
            int r = cur[0], c = cur[1], left = cur[2];
            if (r == m - 1 && c == n - 1) return steps;

            for (int[] d : dirs) {
                int nr = r + d[0], nc = c + d[1];
                if (nr < 0 || nr >= m || nc < 0 || nc >= n) continue;
                int nLeft = left - grid[nr][nc];   // grid is 0/1, so this spends the budget
                // `<=`, not `<`: an equal budget arriving no earlier can never win
                if (nLeft < 0 || nLeft <= best[nr][nc]) continue;
                best[nr][nc] = nLeft;
                q.offer(new int[]{nr, nc, nLeft});
            }
        }
        steps++;
    }
    return -1;
}
```

**Complexity**
- **Time O(m·n·k)**: `n_left <= best[nr][nc]` admits a cell only with strictly more budget than
  before, so each cell is enqueued at most k + 1 times and tries 4 moves. Past the
  `k >= m + n - 2` shortcut k < m + n, so this is also O(m·n·(m + n)).
- **Space O(m·n·k)** in the worst case, for the queue, which can hold a cell once per larger
  budget; `best` itself is O(m·n).

**Traps**
- Checking the budget on the cell you **enter** but spending it on the cell you **leave**. Either
  convention alone is correct — both endpoints are `0`, so a path pays for the same obstacles — but
  mixing them lets a run of adjacent obstacles through on a budget of one.
- `<` instead of `<=` in the `best` check re-admits an equal budget arriving later: still correct,
  but on an all-`0` grid with k just under `m + n - 2` the budget never drops, so cells already
  settled are enqueued again from every neighbour.

**Not this if** — the extra dimension makes edges *weighted* (a cost or a time per move): the same
state goes into a heap instead of a queue — [Dijkstra.md](./Dijkstra.md).

**Drill** — LC 1293 ⭐ · LC 787 Cheapest Flights Within K Stops *(`(node, stops)`; weighted, so
Dijkstra or Bellman-Ford)* · LC 1928 Minimum Cost to Reach Destination in Time *(`(node, time)`,
weighted)*

---

## 0-1 and Bidirectional BFS

Two BFS shapes that change the queue rather than the node. Edges with arbitrary non-negative
weights are not BFS at all: use a heap ([Dijkstra.md](./Dijkstra.md)). Carrying a value along the
path, such as the product of ratios in LC 399 Evaluate Division, needs no new technique: it is
[dfs § Template 10](./dfs.md#template-10-weighted-graph-dfs-divisionratio-queries--lc-399), and a
BFS that carries `(node, product)` gives the same answers.

### Pattern 15: 0-1 BFS with a Deque — LC 1368 ⭐⭐⭐⭐

> **Key point** — when every edge costs **0 or 1**, a deque replaces Dijkstra's heap: a cost-0 edge
> goes to the front (same layer), a cost-1 edge to the back (next layer). The deque then holds at
> most two distinct distances, in order, so the first pop of a node is final — Dijkstra's guarantee
> at **O(V + E)** instead of `O(E log V)`.

**Recognise** — "the minimum **cost** to make a valid path", "the minimum number of obstacles to
**remove**": some moves are free and the rest cost exactly 1. m, n ≤ 100; Dijkstra would also pass,
so 0-1 BFS is the clean answer rather than the only one.

**Reduces to**: [bfs § Template 3](./bfs.md#template-3-grid-bfs-with-a-direction-array--lc-1091-) — with a deque, and `dist` relaxation instead of mark-on-enqueue.

**LC 1368**: the grid tells you the "free" direction of each cell. Following the arrow costs `0`; any other of the 4 moves costs `1` (one sign change).

```python
# python
# LC 1368 - Minimum Cost to Make at Least One Valid Path in a Grid
# IDEA: deque BFS - appendleft for 0-cost moves, append for 1-cost moves
# time = O(m*n), space = O(m*n)
from collections import deque

def minCost(grid):
    m, n = len(grid), len(grid[0])
    dirs = [(0, 1), (0, -1), (1, 0), (-1, 0)]      # grid value 1,2,3,4
    INF = float('inf')
    dist = [[INF] * n for _ in range(m)]
    dist[0][0] = 0
    dq = deque([(0, 0)])
    while dq:
        r, c = dq.popleft()
        for k, (dr, dc) in enumerate(dirs):
            nr, nc = r + dr, c + dc
            if not (0 <= nr < m and 0 <= nc < n):
                continue
            cost = 0 if grid[r][c] == k + 1 else 1
            if dist[r][c] + cost < dist[nr][nc]:
                dist[nr][nc] = dist[r][c] + cost
                if cost == 0:
                    dq.appendleft((nr, nc))        # free move -> front
                else:
                    dq.append((nr, nc))            # paid move -> back
    return dist[m - 1][n - 1]
```

```java
// java
// LC 1368 - Minimum Cost to Make at Least One Valid Path in a Grid
// IDEA: 0-1 BFS. Following grid[r][c]'s arrow costs 0 -> push FRONT; turning costs 1 -> push BACK.
// time = O(m*n), space = O(m*n)
public int minCost(int[][] grid) {
    int m = grid.length, n = grid[0].length;
    int[][] dirs = {{0,1},{0,-1},{1,0},{-1,0}};    // index k <-> grid value k+1
    int[][] dist = new int[m][n];
    for (int[] row : dist) Arrays.fill(row, Integer.MAX_VALUE);
    dist[0][0] = 0;
    Deque<int[]> dq = new ArrayDeque<>();
    dq.offerFirst(new int[]{0, 0});
    while (!dq.isEmpty()) {
        int[] cur = dq.pollFirst();
        int r = cur[0], c = cur[1];
        for (int k = 0; k < 4; k++) {
            int nr = r + dirs[k][0], nc = c + dirs[k][1];
            if (nr < 0 || nr >= m || nc < 0 || nc >= n) continue;
            int cost = (grid[r][c] == k + 1) ? 0 : 1;
            if (dist[r][c] + cost < dist[nr][nc]) {
                dist[nr][nc] = dist[r][c] + cost;
                if (cost == 0) dq.offerFirst(new int[]{nr, nc});   // 0-weight: front
                else           dq.offerLast(new int[]{nr, nc});    // 1-weight: back
            }
        }
    }
    return dist[m - 1][n - 1];
}
```

**Complexity**
- **Time O(m·n)**: the deque holds at most two distances, d then d + 1, so a cell is pushed at most
  twice — once by a cost-1 `dq.append` and once by a cheaper cost-0 `dq.appendleft` — and each pop
  scans 4 moves.
- **Space O(m·n)**: `dist`, and the deque's at most two entries per cell.

**BFS vs 0-1 BFS vs Dijkstra**

| Edge weights | Structure | Push rule | Time |
|---|---|---|---|
| all 1 | Queue | always back | O(V + E) |
| 0 or 1 | **Deque** | 0 → front, 1 → back | O(V + E) |
| arbitrary ≥ 0 | PriorityQueue | by distance | O(E log V) |

**Traps**
- Relax with `dist[r][c] + cost < dist[nr][nc]` rather than a `visited` set: a node first reached by
  a cost-1 edge can later be reached for less through a cost-0 edge.
- Pushing every edge to the back turns this into plain BFS, which counts moves, not sign changes:
  on `[[1,1,3],[3,2,2],[1,1,4]]` the answer is 0, but plain BFS returns 4, the fewest moves.

**Not this if** — every move costs the same (a plain queue), or costs other than 0 and 1 appear
([Dijkstra.md](./Dijkstra.md)).

**Drill** — LC 1368 ⭐ · LC 2290 Minimum Obstacle Removal to Reach Corner *(entering a wall costs
1)* · LC 1263 Minimum Moves to Move a Box to Their Target Location *(pushing the box costs 1,
walking the player costs 0 — state is `(box, player)`)*

### Bidirectional BFS

> **Key point** — when both endpoints are known, search from both and stop when the frontiers meet.
> With branching factor b and distance d, two searches of depth d/2 visit about `2·b^(d/2)` nodes
> instead of `b^d`. Always expand the **smaller** frontier, and expand it one whole level at a time,
> so the first meeting is at the shortest length.

**Recognise** — "the number of words in the shortest transformation sequence from `beginWord` to
`endWord`": **both** endpoints given, and a large branching factor (26·L generated neighbours per
word). word list ≤ 5,000 — one-sided BFS passes too; this is the follow-up that cuts the search.

**Reduces to**: [bfs § Template 5](./bfs.md#template-5-word-ladder-bfs-over-generated-neighbours--lc-127-) — run from both ends of LC 127 Word Ladder.

```python
# python
# LC 127 - Word Ladder (bidirectional)
# IDEA: two frontier sets; expand the smaller one level at a time; a neighbour in the other set ends it
# time = O(N * L * 26) worst case, far less in practice, space = O(N * L)
def ladderLength(beginWord, endWord, wordList):
    words = set(wordList)
    if endWord not in words:
        return 0
    front, back = {beginWord}, {endWord}
    words.discard(beginWord)
    length = 1                             # words on the path so far
    while front and back:
        if len(front) > len(back):
            front, back = back, front      # always grow the smaller side
        next_front = set()
        for word in front:
            for i in range(len(word)):
                for ch in 'abcdefghijklmnopqrstuvwxyz':
                    nxt = word[:i] + ch + word[i + 1:]
                    if nxt in back:
                        return length + 1  # the two searches meet
                    if nxt in words:
                        words.discard(nxt) # mark on enqueue, shared by both sides
                        next_front.add(nxt)
        front = next_front
        length += 1
    return 0
```

**Complexity**
- **Time O(N·L·26)** in the worst case: `words.discard(nxt)` marks a word once for both sides, so
  each word is expanded at most once by the `for i / for ch` loops. The gain is in practice — two
  frontiers of depth d/2 instead of one of depth d.
- **Space O(N·L)**: `words` plus the two frontier sets.

**Traps**
- Checking for the meeting only when a word is *popped* (instead of when it is generated) can
  overshoot by one level.
- Expanding node by node instead of level by level can meet on a longer path first.
- When the end node is unknown (any leaf, any gate), there is nothing to search backwards from:
  use one-sided BFS.

**Not this if** — the target is a set or a predicate rather than one node, or the graph is small:
one-sided [bfs § Template 5](./bfs.md#template-5-word-ladder-bfs-over-generated-neighbours--lc-127-).

**Drill** — LC 127 ⭐ · LC 433 Minimum Genetic Mutation *(the same search on 8-letter genes)* ·
LC 752 Open the Lock *(both ends known: `"0000"` and the target)* · LC 1197 Minimum Knight Moves
*(an unbounded board, where halving the search depth matters most)*

## Repeated & Sequential BFS

### Pattern 6: Sort + Repeated BFS (Sequential Shortest Paths) — LC 675

> **Key point** — the visiting order is forced (shortest tree first), so the total is the sum of
> shortest paths between consecutive targets. Sort the targets, then run one ordinary grid BFS per
> consecutive pair; any unreachable pair makes the whole answer -1.

**Recognise** — "you must cut off the trees in order from shortest to tallest", "visit the targets
in a given order" on a grid with obstacles. m, n ≤ 50, so even one BFS per tree is about
2,500 × 2,500 cell visits.

**Reduces to**: [bfs § Template 3](./bfs.md#template-3-grid-bfs-with-a-direction-array--lc-1091-) — called once per pair of consecutive targets.

```python
# python
# LC 675 - Cut Off Trees for Golf Event
# IDEA: sort trees by height; total = sum of grid-BFS distances between consecutive trees
# time = O(t * m * n), space = O(m * n)   t = number of trees, one BFS each
from collections import deque

def cutOffTree(forest):
    m, n = len(forest), len(forest[0])
    trees = sorted((forest[r][c], r, c) for r in range(m) for c in range(n) if forest[r][c] > 1)

    def bfs(sr, sc, tr, tc):
        if (sr, sc) == (tr, tc):
            return 0
        queue = deque([(sr, sc)])
        seen = {(sr, sc)}
        steps = 0
        while queue:
            steps += 1
            for _ in range(len(queue)):
                r, c = queue.popleft()
                for dr, dc in ((1, 0), (-1, 0), (0, 1), (0, -1)):
                    nr, nc = r + dr, c + dc
                    if 0 <= nr < m and 0 <= nc < n and (nr, nc) not in seen and forest[nr][nc] != 0:
                        if (nr, nc) == (tr, tc):
                            return steps
                        seen.add((nr, nc))
                        queue.append((nr, nc))
        return -1

    total = 0
    r, c = 0, 0
    for _, tr, tc in trees:
        steps = bfs(r, c, tr, tc)
        if steps == -1:
            return -1                      # one unreachable tree fails the whole walk
        total += steps
        r, c = tr, tc
    return total
```

```java
// java
// LC 675 - Cut Off Trees for Golf Event
// IDEA: sort the targets by height, then one BFS per consecutive pair; sum the distances
// time = O(t * m * n), space = O(m * n)
public int cutOffTree(List<List<Integer>> grid) {
    int rows = grid.size(), cols = grid.get(0).size();
    List<int[]> targets = new ArrayList<>();
    for (int r = 0; r < rows; r++)
        for (int c = 0; c < cols; c++)
            if (grid.get(r).get(c) > 1) targets.add(new int[]{grid.get(r).get(c), r, c});
    targets.sort((a, b) -> Integer.compare(a[0], b[0]));
    int totalSteps = 0, startR = 0, startC = 0;
    for (int[] target : targets) {
        int steps = bfs(grid, startR, startC, target[1], target[2]);
        if (steps == -1) return -1;
        totalSteps += steps;
        startR = target[1];
        startC = target[2];
    }
    return totalSteps;
}

private int bfs(List<List<Integer>> grid, int sr, int sc, int tr, int tc) {
    if (sr == tr && sc == tc) return 0;
    int rows = grid.size(), cols = grid.get(0).size();
    Queue<int[]> queue = new LinkedList<>();
    queue.offer(new int[]{sr, sc});
    boolean[][] visited = new boolean[rows][cols];
    visited[sr][sc] = true;
    int[][] dirs = {{0,1}, {0,-1}, {1,0}, {-1,0}};
    int steps = 0;
    while (!queue.isEmpty()) {
        int size = queue.size();
        steps++;
        for (int i = 0; i < size; i++) {
            int[] cur = queue.poll();
            for (int[] dir : dirs) {
                int nr = cur[0] + dir[0], nc = cur[1] + dir[1];
                if (nr < 0 || nr >= rows || nc < 0 || nc >= cols
                        || visited[nr][nc] || grid.get(nr).get(nc) == 0) continue;
                if (nr == tr && nc == tc) return steps;
                visited[nr][nc] = true;
                queue.offer(new int[]{nr, nc});
            }
        }
    }
    return -1;
}
```

**Complexity**
- **Time O(t·m·n)**: `for _, tr, tc in trees` runs one `bfs` per tree, and each BFS visits a cell
  at most once (fresh `seen`); the `sorted` is O(t log t) and is dominated.
- **Space O(m·n)**: one `seen` set and queue at a time, plus the `trees` list.

**Dry run**

```text
forest = [[1,2,3],    trees by height: 2 (0,1), 3 (0,2), 4 (1,2), 5 (2,2), 6 (2,1), 7 (2,0)
          [0,0,4],
          [7,6,5]]
(0,0)->(0,1) 1 · (0,1)->(0,2) 1 · (0,2)->(1,2) 1 · (1,2)->(2,2) 1 · (2,2)->(2,1) 1 · (2,1)->(2,0) 1
total = 6
```

**Traps**
- A tree may stand on `(0, 0)`; the first BFS then returns 0 rather than failing.
- Cut trees become `1` (walkable), never `0`; and every BFS needs a fresh `visited`.
- A* with a Manhattan heuristic speeds up each search but does not change the bound.

**Not this if** — the targets may be visited in any order: that is a TSP-style search over a
distance matrix, not a sum of consecutive BFSs.

**Drill** — LC 675 ⭐ · LC 1091 *(the single grid BFS each step runs)* · LC 317 *(one BFS per
source, summed per cell)*

## When Not to Use

| Technique | The simpler core template that usually suffices |
|---|---|
| Pattern 4.5 (fill, then multi-source BFS) | one source cell → [bfs § Template 3](./bfs.md#template-3-grid-bfs-with-a-direction-array--lc-1091-) |
| Pattern 4.6 (independent runs) | the question is about the **nearest** source → one [multi-source BFS](./bfs.md#template-4-multi-source-bfs--lc-994-); one BFS per source there is k times slower for the same answer |
| Pattern 8 (route-level BFS) | the answer counts stops or edges, not routes → [bfs § Template 2](./bfs.md#template-2-graph-bfs-mark-on-enqueue--lc-1971-) |
| Pattern 8.5 (predecessor DAG) | only the length or one path is asked → [bfs § Template 5](./bfs.md#template-5-word-ladder-bfs-over-generated-neighbours--lc-127-), plus a parent map for the path |
| Pattern 9 (Cartesian product BFS) | the choices constrain each other, or memory matters → backtracking ([dfs § Template 4](./dfs.md#template-4-backtracking--lc-46)) |
| Pattern 12 (one deletion per level) | only the **number** of deletions is asked → a single left-to-right balance counter, O(n) |
| Pattern 16 (augmented state) | the carried value never changes which moves are legal → plain grid BFS; it is only the answer |
| Pattern 15 (0-1 BFS) | every move costs the same → a plain queue; costs other than 0 and 1 → [Dijkstra.md](./Dijkstra.md) |
| Bidirectional BFS | the graph is small, or the target is a set or a predicate → one-sided BFS; it is rarely needed to pass at L3 |
| Pattern 6 (sorted repeated BFS) | the targets may be visited in any order → that is a TSP-style search, not this |
| Tree distances between leaves (LC 1530) | a post-order DFS that returns per-depth leaf counts is O(n) with no graph ([dfs_advanced.md](./dfs_advanced.md)) |

## Summary

| Signal in the problem | Variant | Section |
|---|---|---|
| "distance to the **nearest** source" vs "**sum** of distances to all sources" | one shared `visited` vs a fresh `visited` per source | Pattern 4.6 |
| Two components, connect them with the fewest flips | flood-fill one component, multi-source BFS out from all of it | Pattern 4.5 |
| "minimum **transfers** / buses / lines" | BFS whose nodes are **routes**, not stops | Pattern 8 |
| "return **all** shortest sequences" | BFS builds a predecessor DAG, DFS enumerates it | Pattern 8.5 |
| "minimum removals to make it valid" | one BFS level = one removal; stop at the first fruitful level | Pattern 12 |
| Enumerate every combination from independent groups | one group per BFS level (Cartesian product) | Pattern 9 |
| A key, a budget or a colour changes what moves are legal | `visited` on `(cell, resource)` | Pattern 16 |
| Every edge costs **0 or 1** | deque: cost-0 to the front, cost-1 to the back | Pattern 15 |
| Both endpoints are known and the path is long | expand the smaller frontier from each end | Bidirectional BFS |
| Visit targets in a **forced order** | sort, then one BFS per consecutive pair | Pattern 6 |

The must-know queue templates these build on are in [bfs.md](./bfs.md); the classic problems are in [bfs_examples.md](./bfs_examples.md).
