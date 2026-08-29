# 併查集（Union Find）— 範例詳解

> **範圍** — [union_find.md](./union_find.md) 背後的解題檔案庫：十九道題，依「一個節點代表什麼」分組——頂點、格子、帶比值的變數，或樹節點——因為題目之間變的就只有這件事。
> **另見** — [union_find.md](./union_find.md)：母文件，收錄 DSU 模板、六大模式、最佳化與陷阱；[graph.md](./graph.md)：圖的表示法通論；[topology_sorting.md](./topology_sorting.md)：有向圖的對應做法；[diff_toposort_quickunion.md](./diff_toposort_quickunion.md)：判斷題目要的是兩者中的哪一個；[tree_lca_distance.md](./tree_lca_distance.md)：LC 236 以及第 19 個範例的遞迴 LCA 觀點。

## LeetCode 題目清單

- [Union Find](https://leetcode.com/problem-list/union-find/)
- [Graph](https://leetcode.com/problem-list/graph/)

## 總覽

這是 [union_find.md](./union_find.md) 的長尾。母文件收模板、六大模式與陷阱；
本文件收*套用*它們的題目。

### 關鍵性質
- **複雜度**：以下每個解法在路徑壓縮 + 按秩合併之下，每次操作都接近 O(α(n))——實務上就是 O(1)；若某個解法的複雜度是由別的東西主導（排序、BFS），它自己的註解會說明
- **核心想法**：DSU 本身從來不變。變的是節點*是什麼*，以及兩個節點何時該合併——這也是這些題目依此分組、而非依難度分組的原因
- **何時使用**：當母文件的模式表已經告訴你這題屬於六種形狀中的哪一種之後


## 環偵測與冗餘邊

### 1) Redundant Connection — LC 684

> 逐一加入邊；若兩個節點已經連通，這條邊就是冗餘的。

```java
// LC 684 - Redundant Connection
// IDEA: Union-Find — detect cycle; redundant edge connects already-connected nodes
// time = O(N * α(N)), space = O(N)
public int[] findRedundantConnection(int[][] edges) {
    int n = edges.length;
    int[] parent = new int[n + 1];
    for (int i = 0; i <= n; i++) parent[i] = i;
    for (int[] edge : edges) {
        if (find(parent, edge[0]) == find(parent, edge[1])) return edge;
        union(parent, edge[0], edge[1]);
    }
    return new int[]{};
}
private int find(int[] parent, int x) {
    if (parent[x] != x) parent[x] = find(parent, parent[x]); // path compression
    return parent[x];
}
private void union(int[] parent, int x, int y) {
    parent[find(parent, x)] = find(parent, y);
}
```

```python
# LC 684 - Redundant Connection
# IDEA: Union-Find (dict-based, union by rank) — process edges; return first edge that forms a cycle
# time = O(N * α(N)), space = O(N)

class Solution(object):
    def findRedundantConnection(self, edges):
        uf = MyUF()
        for a, b in edges:
            if not uf.union(a, b):
                return [a, b]
        return []

class MyUF(object):
    def __init__(self):
        self.parent = {}
        self.rank = {}

    def get_parent(self, x):
        if x not in self.parent:       # lazy init: node becomes its own root on first seen
            self.parent[x] = x
            self.rank[x] = 0
        if self.parent[x] != x:
            self.parent[x] = self.get_parent(self.parent[x])  # path compression
        return self.parent[x]

    def union(self, a, b):
        rootA, rootB = self.get_parent(a), self.get_parent(b)
        if rootA == rootB:
            return False               # cycle detected — this edge is redundant
        if self.rank[rootA] < self.rank[rootB]:
            self.parent[rootA] = rootB
        elif self.rank[rootA] > self.rank[rootB]:
            self.parent[rootB] = rootA
        else:
            self.parent[rootB] = rootA
            self.rank[rootA] += 1
        return True
```

```python
# LC 684 - Redundant Connection (array-based variant, matches Java approach)
# IDEA: Union-Find (1-indexed array, path compression only) — simpler when nodes are 1..n
# time = O(N * α(N)), space = O(N)

class Solution(object):
    def findRedundantConnection(self, edges):
        n = len(edges)
        uf = UF(n)
        for u, v in edges:
            if not uf.union(u, v):
                return [u, v]
        return []

class UF(object):
    def __init__(self, n):
        self.parents = list(range(n + 1))   # 1-indexed; parents[i] = i initially

    def find(self, a):
        if self.parents[a] != a:
            self.parents[a] = self.find(self.parents[a])  # path compression
        return self.parents[a]

    def union(self, a, b):
        root_a, root_b = self.find(a), self.find(b)
        if root_a == root_b:
            return False               # already connected → cycle
        self.parents[root_a] = root_b
        return True
```

### 2) Redundant Connection II — LC 685 — **有向**圖上的 DSU ⭐⭐⭐⭐


> **和 §2-1 (LC 684) 的差別：** 在*有向*的有根樹上，被破壞的不變量可能是 (a) 某個節點有**兩個父節點**，或 (b) 出現**環**，或兩者都有。單純的「union 失敗 ⇒ 就是答案」已經不夠用了。

**核心想法——兩候選淘汰法：**
1. 掃過所有邊並記錄 `parent[v]`。若某個 `v` 已經有父節點，就記下 `cand1 = (parent[v], v)`（較早的那條邊），並記住**較晚**那條邊 `cand2` 的索引。
2. 對所有邊重跑一次單純的 DSU，但**跳過 `cand2`**。
   - 找到環，且**沒有**雙父節點 → 回傳把環閉合的那條邊。
   - 找到環，且存在雙父節點 → `cand2` 是無辜的；回傳 `cand1`。
   - 沒有環 → 回傳 `cand2`。

```java
// java
// LC 685 - Redundant Connection II
// IDEA: directed DSU — locate the two edges into a 2-parent node, drop the later one and
//       re-test with union-find; whether a cycle remains tells you which candidate to remove
// time = O(N * α(N)), space = O(N)
public int[] findRedundantDirectedConnection(int[][] edges) {
    int n = edges.length;
    int[] parent = new int[n + 1];      // parent[v] = u for edge u->v (0 = none yet)
    int[] cand1 = null;
    int dup = -1;                       // index of the LATER of the two edges into the same node

    for (int i = 0; i < n; i++) {
        int u = edges[i][0], v = edges[i][1];
        if (parent[v] != 0) {
            cand1 = new int[]{parent[v], v};   // the earlier in-edge
            dup = i;                            // the later in-edge (this one)
        } else {
            parent[v] = u;
        }
    }

    int[] p = new int[n + 1];
    int[] sz = new int[n + 1];
    for (int i = 0; i <= n; i++) { p[i] = i; sz[i] = 1; }

    for (int i = 0; i < n; i++) {
        if (i == dup) continue;                 // pretend the later in-edge doesn't exist
        int ru = find(p, edges[i][0]), rv = find(p, edges[i][1]);
        if (ru == rv) {
            // a cycle survives without cand2
            return cand1 == null ? edges[i]     // no 2-parent node → this edge closes the cycle
                                 : cand1;       // 2-parent node → the EARLIER edge is the culprit
        }
        // union by size — a plain `p[rv] = ru` can build an O(N) parent chain here
        if (sz[ru] < sz[rv]) { int t = ru; ru = rv; rv = t; }
        p[rv] = ru;
        sz[ru] += sz[rv];
    }
    return edges[dup];                          // no cycle → removing the later in-edge fixes it
}
private int find(int[] p, int x) { return p[x] == x ? x : (p[x] = find(p, p[x])); }
```

```python
# python
# LC 685 - Redundant Connection II
# IDEA: find the 2-parent node's two in-edges, remove the later one, then DSU-test for a cycle
# time = O(N * α(N)), space = O(N)
class Solution(object):
    def findRedundantDirectedConnection(self, edges):
        n = len(edges)
        par = [0] * (n + 1)          # par[v] = u for edge u->v
        cand1, dup = None, -1

        for i, (u, v) in enumerate(edges):
            if par[v] != 0:
                cand1 = [par[v], v]  # earlier in-edge
                dup = i              # later in-edge
            else:
                par[v] = u

        parent = list(range(n + 1))
        size = [1] * (n + 1)

        def find(x):
            if parent[x] != x:
                parent[x] = find(parent[x])
            return parent[x]

        for i, (u, v) in enumerate(edges):
            if i == dup:
                continue             # skip the later in-edge
            ru, rv = find(u), find(v)
            if ru == rv:             # cycle survives without it
                return edges[i] if cand1 is None else cand1
            # union by size — plain `parent[rv] = ru` can build an O(N) chain,
            # which blows Python's recursion limit inside find() when N = 1000
            if size[ru] < size[rv]:
                ru, rv = rv, ru
            parent[rv] = ru
            size[ru] += size[rv]

        return edges[dup]            # no cycle → the later in-edge is redundant
```

**比較 — LC 684 vs LC 685**

| | LC 684（無向） | LC 685（有向） |
|---|---|---|
| 被破壞的不變量 | 剛好一個環 | 雙父節點**或**環（或兩者） |
| 演算法 | 逐邊 union；第一次失敗就是答案 | 找出那 2 條入邊，捨去較晚的，用 DSU 重測 |
| 兩種情況同時成立時的答案 | 不適用 | **較早**的那條入邊（`cand1`） |
| 掃過邊的趟數 | 1 | 2 |
### 3) Satisfiability of Equality Equations — LC 990

> 先處理 '==' 的邊；再檢查 '!=' 的配對是否矛盾。

```java
// LC 990 - Satisfiability of Equality Equations
// IDEA: Union-Find — union on ==, validate != pairs for contradiction
// time = O(N), space = O(26)
public boolean equationsPossible(String[] equations) {
    int[] p = new int[26];
    for (int i = 0; i < 26; i++) p[i] = i;
    for (String eq : equations)
        if (eq.charAt(1) == '=') union(p, eq.charAt(0)-'a', eq.charAt(3)-'a');
    for (String eq : equations)
        if (eq.charAt(1) == '!' && find(p, eq.charAt(0)-'a') == find(p, eq.charAt(3)-'a'))
            return false;
    return true;
}
private int find(int[] p, int x) { return p[x]==x ? x : (p[x]=find(p,p[x])); }
private void union(int[] p, int x, int y) { p[find(p,x)] = find(p,y); }
```

```python
# python
# LC 990 - Satisfiability of Equality Equations
# IDEA: Union-Find — union all '==' pairs first, then verify no '!=' pair shares a root
# time = O(N), space = O(26)
class Solution(object):
    def equationsPossible(self, equations):
        parent = list(range(26))

        def find(x):
            if parent[x] != x:
                parent[x] = find(parent[x])   # path compression
            return parent[x]

        # pass 1: union every equality
        for eq in equations:
            if eq[1] == '=':
                parent[find(ord(eq[0]) - 97)] = find(ord(eq[3]) - 97)
        # pass 2: any inequality inside one component => contradiction
        for eq in equations:
            if eq[1] == '!' and find(ord(eq[0]) - 97) == find(ord(eq[3]) - 97):
                return False
        return True
```

## 連通元件的計數與連通性

### 4) Number of Provinces — LC 547

> 把所有直接的朋友關係 union 起來後，數有幾個相異的根。

```java
// LC 547 - Number of Provinces
// IDEA: Union-Find — count distinct components (roots)
// time = O(N^2 * α(N)), space = O(N)
public int findCircleNum(int[][] isConnected) {
    int n = isConnected.length;
    int[] parent = new int[n];
    for (int i = 0; i < n; i++) parent[i] = i;
    for (int i = 0; i < n; i++)
        for (int j = i + 1; j < n; j++)
            if (isConnected[i][j] == 1) union(parent, i, j);
    int count = 0;
    for (int i = 0; i < n; i++) if (find(parent, i) == i) count++;
    return count;
}
private int find(int[] parent, int x) {
    if (parent[x] != x) parent[x] = find(parent, parent[x]);
    return parent[x];
}
private void union(int[] parent, int x, int y) {
    parent[find(parent, x)] = find(parent, y);
}
```

```python
# python
# LC 547 - Number of Provinces
# IDEA: Union-Find — union every direct friendship; `components` counter = answer
# time = O(N^2 * α(N)), space = O(N)
class Solution(object):
    def findCircleNum(self, isConnected):
        n = len(isConnected)
        uf = UnionFind(n)
        for i in range(n):
            for j in range(i + 1, n):
                if isConnected[i][j] == 1:
                    uf.union(i, j)
        return uf.components   # each successful union decrements the counter

# reuses the `UnionFind` (union by size) class from section 0-3
class UnionFind:
    def __init__(self, n):
        self.parent = list(range(n))
        self.size = [1] * n
        self.components = n

    def find(self, x):
        if self.parent[x] != x:
            self.parent[x] = self.find(self.parent[x])
        return self.parent[x]

    def union(self, x, y):
        rx, ry = self.find(x), self.find(y)
        if rx == ry:
            return False
        if self.size[rx] < self.size[ry]:
            rx, ry = ry, rx
        self.parent[ry] = rx
        self.size[rx] += self.size[ry]
        self.components -= 1
        return True
```

### 5) Graph Valid Tree — LC 261

> 樹恰好有 N-1 條邊且無環；逐邊 union，遇到同一元件內的邊就回傳 false。

```java
// LC 261 - Graph Valid Tree
// IDEA: Union-Find — N-1 edges + no cycle = valid tree
// time = O(N * α(N)), space = O(N)
public boolean validTree(int n, int[][] edges) {
    if (edges.length != n - 1) return false;
    int[] p = new int[n];
    for (int i = 0; i < n; i++) p[i] = i;
    for (int[] e : edges) {
        if (find(p, e[0]) == find(p, e[1])) return false;
        p[find(p, e[0])] = find(p, e[1]);
    }
    return true;
}
private int find(int[] p, int x) { return p[x] == x ? x : (p[x] = find(p, p[x])); }
```

```python
# python
# LC 261 - Graph Valid Tree
# IDEA: Union-Find — a valid tree has exactly N-1 edges AND no cycle
# time = O(N * α(N)), space = O(N)
class Solution(object):
    def validTree(self, n, edges):
        if len(edges) != n - 1:      # tree must have exactly n-1 edges
            return False
        parent = list(range(n))

        def find(x):
            if parent[x] != x:
                parent[x] = find(parent[x])   # path compression
            return parent[x]

        for a, b in edges:
            ra, rb = find(a), find(b)
            if ra == rb:             # both endpoints already connected => cycle
                return False
            parent[ra] = rb
        return True
```

### 6) Number of Connected Components in an Undirected Graph — LC 323

> 逐邊 union；剩下的相異根數就是連通元件數。

```java
// LC 323 - Number of Connected Components in Undirected Graph
// IDEA: Union-Find — count distinct roots after unioning all edges
// time = O(N * α(N)), space = O(N)
public int countComponents(int n, int[][] edges) {
    int[] p = new int[n];
    for (int i = 0; i < n; i++) p[i] = i;
    int components = n;
    for (int[] e : edges) {
        int a = find(p, e[0]), b = find(p, e[1]);
        if (a != b) { p[a] = b; components--; }
    }
    return components;
}
private int find(int[] p, int x) { return p[x] == x ? x : (p[x] = find(p, p[x])); }
```

```python
# python
# LC 323 - Number of Connected Components in Undirected Graph
# IDEA: Union-Find — start with n components, decrement on each successful union
# time = O(N * α(N)), space = O(N)
class Solution(object):
    def countComponents(self, n, edges):
        parent = list(range(n))
        components = n

        def find(x):
            if parent[x] != x:
                parent[x] = find(parent[x])   # path compression
            return parent[x]

        for a, b in edges:
            ra, rb = find(a), find(b)
            if ra != rb:
                parent[ra] = rb
                components -= 1               # two components merged into one
        return components
```

### 7) Number of Operations to Make Network Connected — LC 1319

> 至少需要 N-1 條邊；數出元件數；多餘的邊可以用來把斷開的元件重新接起來。

```java
// LC 1319 - Number of Operations to Make Network Connected
// IDEA: Union-Find — count components; need (components-1) extra cables
// time = O(N * α(N)), space = O(N)
public int makeConnected(int n, int[][] connections) {
    if (connections.length < n - 1) return -1;
    int[] p = new int[n];
    for (int i = 0; i < n; i++) p[i] = i;
    int components = n;
    for (int[] c : connections) {
        int a = find(p, c[0]), b = find(p, c[1]);
        if (a != b) { p[a] = b; components--; }
    }
    return components - 1;
}
private int find(int[] p, int x) { return p[x]==x ? x : (p[x]=find(p,p[x])); }
```

```python
# python
# LC 1319 - Number of Operations to Make Network Connected
# IDEA: Union-Find — need >= n-1 cables; answer = (components - 1) redundant cables reused
# time = O(N * α(N)), space = O(N)
class Solution(object):
    def makeConnected(self, n, connections):
        if len(connections) < n - 1:     # not enough cables to ever connect n nodes
            return -1
        parent = list(range(n))
        components = n

        def find(x):
            if parent[x] != x:
                parent[x] = find(parent[x])   # path compression
            return parent[x]

        for a, b in connections:
            ra, rb = find(a), find(b)
            if ra != rb:
                parent[ra] = rb
                components -= 1
        # (components - 1) cables are needed to join the remaining components
        return components - 1
```

> **變化題 — LC 1579 Remove Max Number of Edges to Keep Graph Fully Traversable**：跑**兩個平行的 DSU**（Alice 的和 Bob 的）。**先**處理第 3 型（共用）邊，同時 union 進兩邊；接著第 1 型只進 Alice、第 2 型只進 Bob。數出所有 `union()` 回傳 `false` 的邊（冗餘邊）——那個數量就是答案，但除非兩個 DSU 最後都剛好剩 1 個元件，否則要回傳 `-1`。

> **變化題 — LC 2076 Process Restricted Friend Requests**：*在真正 union 之前先檢查*。對每個請求 `(a,b)`：若已連通 → 接受；否則掃過所有限制 `(x,y)`，若 `find(x)/find(y)` 以任一順序對應到 `find(a)/find(b)` 就拒絕；只有在沒有違反任何限制時才 union。O(Q · R · α(N))。

### 8) Count Unreachable Pairs of Nodes — LC 2316 — 用剩餘量累計

> union 所有邊 → 把節點分成各個元件 → 數出落在**不同**元件的節點配對數（它們彼此不可達）。

Python 參考：`leetcode_python/Depth-First-Search/count-unreachable-pairs-of-nodes-in-an-undirected-graph.py`

**核心想法：** 兩個節點*不可達* ⟺ 它們屬於**不同的連通元件**。所以答案 = 跨元件的節點配對數 = 對所有元件配對 `i < j` 取 `Σ (s_i · s_j)`，其中 `s_i` 是元件 `i` 的大小。

#### ⭐ `remain` 技巧 — O(k) 的跨配對計數（不用巢狀迴圈）

直覺上你可能會：
- 先算 `C(n, 2)`（所有配對）再減掉元件內部的配對 `Σ C(s_i, 2)`，**或**
- 對每一對元件跑雙層迴圈算 `s_i · s_j` → O(k²)。

改成維護一個持續遞減的 `remain` =「還沒被消耗掉的節點數」，用**單趟**累加：

```python
res = 0
remain = n
for s in size.values():
    remain -= s          # remain now = total nodes in the *remaining* components
    res += s * remain     # pair this component's s nodes with every node still ahead
return res
```

**為什麼會對**（避免重複計數）：

對元件 `i`（依序處理），在 `remain -= s_i` 之後，`remain = n − (s_1 + … + s_i) = Σ_{j>i} s_j`。
所以每一步加的是 `s_i · Σ_{j>i} s_j`。對所有 `i` 求和：

```text
Σ_i  s_i · (Σ_{j>i} s_j)  =  Σ_{i < j} s_i · s_j
```

這恰好就是每一組跨元件配對，而且**只算一次**。先減再乘（*先* `remain -= s` 再相乘）正是排除元件與自己配對、並避免 `(i, j)` / `(j, i)` 重複的關鍵。

**視覺化追蹤**（範例圖 `n = 7, edges = [[0,2],[0,5],[2,4],[1,6],[5,4]]` → 元件大小為 `4, 2, 1`，預期答案 `14`）：

```text
remain = 7
s=4 → remain = 3 → res += 4*3 = 12   (res=12)
s=2 → remain = 1 → res += 2*1 = 2    (res=14)
s=1 → remain = 0 → res += 1*0 = 0    (res=14)  ✅
```
> 每一步的數值會隨遍歷順序而不同，但**總和是不變的**（= `Σ_{i<j} s_i·s_j`）。

#### 完整解法

```python
# LC 2316 - Count Unreachable Pairs of Nodes in an Undirected Graph
# IDEA: Union-Find → component sizes → running-remainder cross-pair count
# time = O((N + E) * α(N)), space = O(N)
class MyUF:
    def __init__(self, n):
        self.parents = list(range(n))

    def get_parent(self, x):
        if self.parents[x] != x:
            self.parents[x] = self.get_parent(self.parents[x])  # path compression
        return self.parents[x]

    def union(self, x, y):
        px, py = self.get_parent(x), self.get_parent(y)
        if px != py:
            self.parents[py] = px

class Solution(object):
    def countPairs(self, n, edges):
        uf = MyUF(n)
        for x, y in edges:
            uf.union(x, y)

        # root -> component size (store the COUNT, not the node list)
        size = {}
        for i in range(n):
            root = uf.get_parent(i)
            size[root] = size.get(root, 0) + 1

        res, remain = 0, n
        for s in size.values():
            remain -= s          # remaining nodes ahead of this component
            res += s * remain     # cross-component pairs, counted once
        return res
```

```java
// LC 2316 - Count Unreachable Pairs of Nodes in an Undirected Graph
// IDEA: Union-Find → component sizes → running-remainder cross-pair count
// time = O((N + E) * α(N)), space = O(N)
public long countPairs(int n, int[][] edges) {
    int[] parent = new int[n], size = new int[n];
    for (int i = 0; i < n; i++) { parent[i] = i; size[i] = 1; }
    for (int[] e : edges) union(parent, size, e[0], e[1]);

    long res = 0, remain = n;              // use long: pairs can exceed int range
    for (int i = 0; i < n; i++) {
        if (find(parent, i) == i) {        // i is a root → this component's size is size[i]
            remain -= size[i];
            res += (long) size[i] * remain;
        }
    }
    return res;
}
private int find(int[] p, int x) { return p[x] == x ? x : (p[x] = find(p, p[x])); }
private void union(int[] p, int[] sz, int x, int y) {
    int rx = find(p, x), ry = find(p, y);
    if (rx == ry) return;
    if (sz[rx] < sz[ry]) { int t = rx; rx = ry; ry = t; }
    p[ry] = rx; sz[rx] += sz[ry];          // union by size keeps size[root] correct
}
```

**注意事項：**
- **存數量，不要存節點。** 你只會用到 `s_i`，所以 `size[root] += 1` 勝過蒐集節點清單 → 空間是 O(N)，而不是每個元件 O(N)。
- **先減再乘**（先 `remain -= s` 再 `res += s * remain`）——兩行對調就會把每個元件跟自己也算進去。
- **注意溢位（Java）。** 當 `n` 高達 `10^5` 時，跨配對數會接近 `~5·10^9` > `Integer.MAX_VALUE`；請用 `long`。
- **同一招的通用版：** 給定分組大小 `[s_1..s_k]` 要數跨組配對數，永遠都是 `Σ_{i<j} s_i·s_j`，而且都能用這個方法在一趟 O(k) 內算完——用途遠不只在併查集。

### 9) Longest Consecutive Sequence — LC 128 — HashSet O(N)

> 對每個數字，只有在 (num-1) 不存在時才開始往上數——那代表序列的起點。

```java
// LC 128 - Longest Consecutive Sequence
// IDEA: HashSet — only extend sequences from their start element
// time = O(N), space = O(N)
public int longestConsecutive(int[] nums) {
    Set<Integer> set = new HashSet<>();
    for (int n : nums) set.add(n);
    int longest = 0;
    for (int n : set) {
        if (!set.contains(n - 1)) {   // sequence start
            int len = 1;
            while (set.contains(n + len)) len++;
            longest = Math.max(longest, len);
        }
    }
    return longest;
}
```
```python
# python
# LC 128 - Longest Consecutive Sequence
# IDEA: a set, and only ever extend a run from its START element -- so across the whole
#       scan the inner while-loop touches each element at most once
# time = O(N), space = O(N)
class Solution(object):
    def longestConsecutive(self, nums):
        pool = set(nums)
        longest = 0
        for n in pool:
            ### NOTE !!! without this guard the inner loop reruns per element -> O(N^2)
            if n - 1 in pool:
                continue                      # not a run start
            length = 1
            while n + length in pool:
                length += 1
            longest = max(longest, length)
        return longest
```

## 格子

### 10) Number of Islands — LC 200 — 用 `row * cols + col` 把格子攤成一維

```java
public int numIslands(char[][] grid) {
    int rows = grid.length, cols = grid[0].length;
    UnionFind uf = new UnionFind(rows * cols);
    int islands = 0;

    for (int r = 0; r < rows; r++) {
        for (int c = 0; c < cols; c++) {
            if (grid[r][c] == '1') {
                islands++;
                int idx = r * cols + c;

                // Check 4 directions
                int[][] dirs = {{0,1}, {1,0}, {0,-1}, {-1,0}};
                for (int[] d : dirs) {
                    int nr = r + d[0], nc = c + d[1];
                    if (nr >= 0 && nr < rows && nc >= 0 && nc < cols
                        && grid[nr][nc] == '1') {
                        int nidx = nr * cols + nc;
                        if (uf.union(idx, nidx)) {
                            islands--;
                        }
                    }
                }
            }
        }
    }
    return islands;
}
```
```python
# python
# LC 200 - Number of Islands (union-find)
# IDEA: count every '1' as its own island up front, then subtract one per SUCCESSFUL union
# time = O(M*N*alpha(M*N)), space = O(M*N)
class Solution(object):
    def numIslands(self, grid):
        if not grid or not grid[0]:
            return 0
        rows, cols = len(grid), len(grid[0])
        parent = list(range(rows * cols))

        def find(x):
            while parent[x] != x:
                parent[x] = parent[parent[x]]     # path halving
                x = parent[x]
            return x

        def union(a, b):
            ra, rb = find(a), find(b)
            if ra == rb:
                return False                      # already joined -> not a merge
            parent[rb] = ra
            return True

        islands = 0
        for r in range(rows):
            for c in range(cols):
                if grid[r][c] != '1':
                    continue
                islands += 1
                ### NOTE !!! right and down only -- each neighbouring pair is then seen once
                for dr, dc in ((0, 1), (1, 0)):
                    nr, nc = r + dr, c + dc
                    if nr < rows and nc < cols and grid[nr][nc] == '1':
                        if union(r * cols + c, nr * cols + nc):
                            islands -= 1
        return islands
```

### 11) Surrounded Regions — LC 130 — 一個虛擬邊界節點

> 把所有邊界上的 'O' 都 union 到一個虛擬節點；任何沒連到它的 'O' 都翻成 'X'。

```java
// LC 130 - Surrounded Regions
// IDEA: Union-Find — connect border O cells to virtual node; flip disconnected O cells
// time = O(M*N), space = O(M*N)
public void solve(char[][] board) {
    int m = board.length, n = board[0].length, virtual = m * n;
    int[] p = new int[virtual + 1];
    for (int i = 0; i <= virtual; i++) p[i] = i;
    int[][] dirs = {{1,0},{-1,0},{0,1},{0,-1}};
    for (int i = 0; i < m; i++) for (int j = 0; j < n; j++) if (board[i][j] == 'O') {
        int id = i * n + j;
        if (i == 0 || i == m-1 || j == 0 || j == n-1) union(p, id, virtual);
        else for (int[] d : dirs) {
            int ni = i+d[0], nj = j+d[1];
            if (board[ni][nj] == 'O') union(p, id, ni*n+nj);
        }
    }
    for (int i = 0; i < m; i++) for (int j = 0; j < n; j++)
        if (board[i][j] == 'O' && find(p, i*n+j) != find(p, virtual)) board[i][j] = 'X';
}
private int find(int[] p, int x) { return p[x]==x ? x : (p[x]=find(p,p[x])); }
private void union(int[] p, int x, int y) { p[find(p,x)] = find(p,y); }
```

```python
# python
# LC 130 - Surrounded Regions
# IDEA: one VIRTUAL node stands for "the border". Every 'O' reachable from an edge
#       ends up in its component; everything else is enclosed and flips to 'X'.
# time = O(M*N*alpha(M*N)), space = O(M*N)
class Solution(object):
    def solve(self, board):
        if not board or not board[0]:
            return
        m, n = len(board), len(board[0])
        virtual = m * n                       # the extra node, index m*n
        parent = list(range(virtual + 1))

        def find(x):
            while parent[x] != x:
                parent[x] = parent[parent[x]]
                x = parent[x]
            return x

        def union(a, b):
            parent[find(a)] = find(b)

        for i in range(m):
            for j in range(n):
                if board[i][j] != 'O':
                    continue
                idx = i * n + j
                if i in (0, m - 1) or j in (0, n - 1):
                    union(idx, virtual)       # touches the border
                else:
                    for di, dj in ((1, 0), (-1, 0), (0, 1), (0, -1)):
                        if board[i + di][j + dj] == 'O':
                            union(idx, (i + di) * n + (j + dj))

        ### NOTE !!! LC 130 mutates in place and returns nothing
        for i in range(m):
            for j in range(n):
                if board[i][j] == 'O' and find(i * n + j) != find(virtual):
                    board[i][j] = 'X'
```

> **變化題 — LC 959 Regions Cut By Slashes**：DSU 的節點是*子格*，不是格子。把每個格子切成 4 個三角形（`0`=上、`1`=右、`2`=下、`3`=左，id = `4*(r*n+c)+k`）。格子內部：`'/'` → union(0,3) 與 union(1,2)；`'\'` → union(0,1) 與 union(2,3)；`' '` → 四個全部 union。跨格子：把本格的 `1` 與右鄰的 `3` union，本格的 `2` 與下鄰的 `0` union。答案 = 元件數。

> **變化題 — LC 1559 Detect Cycles in 2D Grid**：只在字母相同時，把每個格子與它的**右**鄰和**下**鄰 union；若 union 之前就 `find(a) == find(b)`，代表有環（格子上的環長度自動 ≥ 4）。和 LC 684 一樣是「union 失敗 ⇒ 有環」的判斷，只是搬到格子上。

### 12) Making A Large Island — LC 827 — 帶大小的 DSU + 候選翻轉 ⭐⭐⭐


> **模式：**「先把元件連好一次，之後每個候選合併都能 O(1) 評估」。
> 用會追蹤大小的 DSU 一趟標好所有島嶼，然後對每個 `0` 格，把它**相異的鄰居根**的大小加總（再 +1 算上被翻轉的那格本身）。

**核心想法：** `Set<root>` 去重就是整個訣竅——四個鄰居中可能有兩個屬於*同一座*島，把它的大小加兩次就是最經典的錯誤答案。

```java
// java
// LC 827 - Making A Large Island
// IDEA: union all 1-cells with size bookkeeping, then for each 0-cell sum the DISTINCT
//       neighbouring component sizes + 1
// time = O(N^2 * α(N^2)), space = O(N^2)
int[] p, sz;
public int largestIsland(int[][] grid) {
    int n = grid.length, total = n * n;
    p = new int[total];
    sz = new int[total];
    for (int i = 0; i < total; i++) { p[i] = i; sz[i] = 1; }

    // pass 1: merge adjacent land cells (right + down is enough for a full scan)
    for (int r = 0; r < n; r++)
        for (int c = 0; c < n; c++)
            if (grid[r][c] == 1) {
                if (r + 1 < n && grid[r + 1][c] == 1) union(r * n + c, (r + 1) * n + c);
                if (c + 1 < n && grid[r][c + 1] == 1) union(r * n + c, r * n + c + 1);
            }

    int[][] dirs = {{1,0},{-1,0},{0,1},{0,-1}};
    int best = 0;
    // pass 2: try flipping every 0; also cover the "grid is all 1s" case
    for (int r = 0; r < n; r++) {
        for (int c = 0; c < n; c++) {
            if (grid[r][c] == 1) {
                best = Math.max(best, sz[find(r * n + c)]);   // no flip needed
                continue;
            }
            Set<Integer> roots = new HashSet<>();             // dedupe: neighbours may share an island
            for (int[] d : dirs) {
                int nr = r + d[0], nc = c + d[1];
                if (nr >= 0 && nr < n && nc >= 0 && nc < n && grid[nr][nc] == 1)
                    roots.add(find(nr * n + nc));
            }
            int totalSize = 1;                                // the flipped cell itself
            for (int root : roots) totalSize += sz[root];
            best = Math.max(best, totalSize);
        }
    }
    return best;
}
private int find(int x) { return p[x] == x ? x : (p[x] = find(p[x])); }
private void union(int a, int b) {
    int ra = find(a), rb = find(b);
    if (ra == rb) return;
    if (sz[ra] < sz[rb]) { int t = ra; ra = rb; rb = t; }
    p[rb] = ra;
    sz[ra] += sz[rb];
}
```

```python
# python
# LC 827 - Making A Large Island
# IDEA: size-tracking DSU over land cells, then evaluate each 0-flip via distinct neighbour roots
# time = O(N^2 * α(N^2)), space = O(N^2)
class Solution(object):
    def largestIsland(self, grid):
        n = len(grid)
        parent = list(range(n * n))
        size = [1] * (n * n)

        def find(x):
            if parent[x] != x:
                parent[x] = find(parent[x])
            return parent[x]

        def union(a, b):
            ra, rb = find(a), find(b)
            if ra == rb:
                return
            if size[ra] < size[rb]:
                ra, rb = rb, ra
            parent[rb] = ra
            size[ra] += size[rb]

        for r in range(n):
            for c in range(n):
                if grid[r][c] == 1:
                    if r + 1 < n and grid[r + 1][c] == 1:
                        union(r * n + c, (r + 1) * n + c)
                    if c + 1 < n and grid[r][c + 1] == 1:
                        union(r * n + c, r * n + c + 1)

        best = 0
        for r in range(n):
            for c in range(n):
                if grid[r][c] == 1:
                    best = max(best, size[find(r * n + c)])   # handles the all-1s grid
                    continue
                roots = set()                                  # MUST dedupe by root
                for dr, dc in ((1, 0), (-1, 0), (0, 1), (0, -1)):
                    nr, nc = r + dr, c + dc
                    if 0 <= nr < n and 0 <= nc < n and grid[nr][nc] == 1:
                        roots.add(find(nr * n + nc))
                best = max(best, 1 + sum(size[root] for root in roots))
        return best
```

**注意事項：**
- `size[x]` 只有在 `x` 是**根**時才有意義——一律用 `find(...)` 當索引。
- 別忘了全是陸地的情況（沒有 `0` 可翻）：用既有的元件大小來初始化 `best` 就能涵蓋。

## 加權、排序邊與離線變形

### 13) Evaluate Division — LC 399 — 帶比值的加權併查集 ⭐⭐⭐⭐

```java
class WeightedUnionFind {
    Map<String, String> parent;
    Map<String, Double> ratio; // ratio[x] = x / parent[x]

    public WeightedUnionFind() {
        parent = new HashMap<>();
        ratio = new HashMap<>();
    }

    public String find(String x) {
        if (!parent.containsKey(x)) {
            parent.put(x, x);
            ratio.put(x, 1.0);
        }
        if (!x.equals(parent.get(x))) {
            String originalParent = parent.get(x);
            parent.put(x, find(originalParent));
            ratio.put(x, ratio.get(x) * ratio.get(originalParent));
        }
        return parent.get(x);
    }

    public void union(String x, String y, double value) {
        String rootX = find(x);
        String rootY = find(y);
        if (!rootX.equals(rootY)) {
            parent.put(rootX, rootY);
            ratio.put(rootX, value * ratio.get(y) / ratio.get(x));
        }
    }

    public double query(String x, String y) {
        if (!parent.containsKey(x) || !parent.containsKey(y)) {
            return -1.0;
        }
        String rootX = find(x);
        String rootY = find(y);
        if (!rootX.equals(rootY)) return -1.0;
        return ratio.get(x) / ratio.get(y);
    }
}

public double[] calcEquation(List<List<String>> equations,
                              double[] values,
                              List<List<String>> queries) {
    WeightedUnionFind uf = new WeightedUnionFind();

    for (int i = 0; i < equations.size(); i++) {
        String a = equations.get(i).get(0);
        String b = equations.get(i).get(1);
        uf.union(a, b, values[i]);
    }

    double[] results = new double[queries.size()];
    for (int i = 0; i < queries.size(); i++) {
        String c = queries.get(i).get(0);
        String d = queries.get(i).get(1);
        results[i] = uf.query(c, d);
    }
    return results;
}
```
```python
# python
# LC 399 - Evaluate Division (weighted union-find)
# IDEA: ratio[x] = x / parent[x]. Path compression MULTIPLIES the ratios it collapses,
#       so after find(x) the stored ratio is x / root directly.
# time = O((E + Q) * alpha(N)), space = O(N)
class WeightedUnionFind(object):
    def __init__(self):
        self.parent = {}
        self.ratio = {}          # ratio[x] = x / parent[x]

    def find(self, x):
        if x not in self.parent:
            self.parent[x] = x
            self.ratio[x] = 1.0
        if self.parent[x] != x:
            original = self.parent[x]
            self.parent[x] = self.find(original)
            ### NOTE !!! multiply on the way back up -- this is what makes ratio[x] = x / root
            self.ratio[x] *= self.ratio[original]
        return self.parent[x]

    def union(self, x, y, value):        # x / y == value
        rx, ry = self.find(x), self.find(y)
        if rx != ry:
            self.parent[rx] = ry
            self.ratio[rx] = value * self.ratio[y] / self.ratio[x]

    def query(self, x, y):
        if x not in self.parent or y not in self.parent:
            return -1.0                  # an unseen variable is unanswerable
        if self.find(x) != self.find(y):
            return -1.0                  # different components -> no path
        return self.ratio[x] / self.ratio[y]


class Solution(object):
    def calcEquation(self, equations, values, queries):
        uf = WeightedUnionFind()
        for (a, b), v in zip(equations, values):
            uf.union(a, b, v)
        return [uf.query(c, d) for c, d in queries]
```

### 14) Path With Minimum Effort — LC 1631 — 排序邊（Kruskal 風格）


> **模式：**「最小化路徑上的**最大**邊」／「連通 A 與 B 的最小門檻值」。
> 把所有邊由小到大排序，逐一 union，一旦 `find(src) == find(dst)` 就停。剛好讓兩端連通的那條邊的權重**就是**答案——不需要二分搜尋，也不需要 Dijkstra。

**核心想法：** DSU 是一個*單調*的連通性判斷器。依權重遞增加入邊，等同於在看「只保留權重 ≤ w 的圖」；讓起點與終點首次連通的那個 `w`，依定義就是最小可能的瓶頸值。這就是 Kruskal 的 MST 掃描，只是提早停下來。

**何時使用：** 路徑成本是 `max(edge)`（而不是 `sum(edge)`），或問題是「只用權重 ≤ limit 的邊時，A 和 B 是否連通」。

```java
// java
// LC 1631 - Path With Minimum Effort
// IDEA: sorted-edge Union-Find (Kruskal sweep) — add edges cheapest-first; the edge that
//       first connects (0,0) with (m-1,n-1) is the minimum possible bottleneck
// time = O(M*N*log(M*N)), space = O(M*N)
public int minimumEffortPath(int[][] heights) {
    int m = heights.length, n = heights[0].length;

    // build one edge per adjacent cell pair: {weight, cellA, cellB}
    List<int[]> edges = new ArrayList<>();
    for (int r = 0; r < m; r++) {
        for (int c = 0; c < n; c++) {
            int id = r * n + c;
            if (r + 1 < m) edges.add(new int[]{Math.abs(heights[r][c] - heights[r + 1][c]), id, id + n});
            if (c + 1 < n) edges.add(new int[]{Math.abs(heights[r][c] - heights[r][c + 1]), id, id + 1});
        }
    }
    edges.sort((a, b) -> a[0] - b[0]);          // cheapest first

    int[] p = new int[m * n];
    for (int i = 0; i < m * n; i++) p[i] = i;

    for (int[] e : edges) {
        int ra = find(p, e[1]), rb = find(p, e[2]);
        if (ra != rb) p[ra] = rb;
        if (find(p, 0) == find(p, m * n - 1)) return e[0];   // just connected → this weight is the answer
    }
    return 0;                                   // single cell (no edges) → effort 0
}
private int find(int[] p, int x) { return p[x] == x ? x : (p[x] = find(p, p[x])); }
```

```python
# python
# LC 1631 - Path With Minimum Effort
# IDEA: sorted-edge Union-Find (Kruskal sweep) — the first edge that joins start & end is the bottleneck
# time = O(M*N*log(M*N)), space = O(M*N)
class Solution(object):
    def minimumEffortPath(self, heights):
        m, n = len(heights), len(heights[0])
        parent = list(range(m * n))

        def find(x):
            if parent[x] != x:
                parent[x] = find(parent[x])      # path compression
            return parent[x]

        edges = []                               # (weight, cellA, cellB)
        for r in range(m):
            for c in range(n):
                idx = r * n + c
                if r + 1 < m:
                    edges.append((abs(heights[r][c] - heights[r + 1][c]), idx, idx + n))
                if c + 1 < n:
                    edges.append((abs(heights[r][c] - heights[r][c + 1]), idx, idx + 1))
        edges.sort()                             # cheapest first

        for w, a, b in edges:
            ra, rb = find(a), find(b)
            if ra != rb:
                parent[ra] = rb
            if find(0) == find(m * n - 1):       # start & end now connected using weights <= w
                return w
        return 0                                 # 1x1 grid
```

**這個模板的變化題**

> **LC 778 Swim in Rising Water** — 權重長在**格子**上，不在邊上。高度是 `0..n*n-1` 的一個排列，所以先預先算出 `pos[height] = cellId`，接著讓 `t = 0, 1, 2, ...` 依序啟用格子 `pos[t]`，並把它與*已啟用*的鄰居 union；回傳第一個使 `find(0) == find(n*n-1)` 成立的 `t`。同樣的單調掃描，O(N²·α)，而且不必排序。

```java
// java
// LC 778 - Swim in Rising Water
// IDEA: same monotone sweep as LC 1631, but activate CELLS in increasing elevation
// time = O(N^2 * α(N^2)), space = O(N^2)
public int swimInWater(int[][] grid) {
    int n = grid.length, total = n * n;
    int[] pos = new int[total];                       // elevation -> cell id (heights are a permutation)
    for (int r = 0; r < n; r++)
        for (int c = 0; c < n; c++)
            pos[grid[r][c]] = r * n + c;

    int[] p = new int[total];
    for (int i = 0; i < total; i++) p[i] = i;
    boolean[] active = new boolean[total];
    int[][] dirs = {{1,0},{-1,0},{0,1},{0,-1}};

    for (int t = 0; t < total; t++) {
        int id = pos[t], r = id / n, c = id % n;
        active[id] = true;                            // water level t reaches this cell
        for (int[] d : dirs) {
            int nr = r + d[0], nc = c + d[1];
            if (nr < 0 || nr >= n || nc < 0 || nc >= n) continue;
            int nid = nr * n + nc;
            if (!active[nid]) continue;               // only merge with already-flooded cells
            int ra = find(p, id), rb = find(p, nid);
            if (ra != rb) p[ra] = rb;
        }
        if (find(p, 0) == find(p, total - 1)) return t;
    }
    return total - 1;
}
```

```python
# python
# LC 778 - Swim in Rising Water
# IDEA: activate cells in increasing elevation; answer = first time start & end are connected
# time = O(N^2 * α(N^2)), space = O(N^2)
class Solution(object):
    def swimInWater(self, grid):
        n = len(grid)
        total = n * n
        pos = [0] * total
        for r in range(n):
            for c in range(n):
                pos[grid[r][c]] = r * n + c

        parent = list(range(total))

        def find(x):
            if parent[x] != x:
                parent[x] = find(parent[x])
            return parent[x]

        active = [False] * total
        for t in range(total):
            idx = pos[t]
            r, c = divmod(idx, n)
            active[idx] = True
            for dr, dc in ((1, 0), (-1, 0), (0, 1), (0, -1)):
                nr, nc = r + dr, c + dc
                if 0 <= nr < n and 0 <= nc < n and active[nr * n + nc]:
                    ra, rb = find(idx), find(nr * n + nc)
                    if ra != rb:
                        parent[ra] = rb
            if find(0) == find(total - 1):
                return t
        return total - 1
```

> **LC 1697 Checking Existence of Edge Length Limited Paths** — 同一套掃描的**離線查詢**版本：把邊依權重排序，同時把查詢依 `limit` 排序，接著用一個指標同時走兩邊，在回答 `find(p) == find(q)` 之前先把所有 `weight < limit` 的邊 union 進去。記得回傳答案時要還原成原本的查詢順序。

### 15) Bricks Falling When Hit — LC 803 — 離線**逆向**併查集 ⭐⭐⭐


> **模式：** 題目在*刪除*東西，但 DSU 只能*合併*。解法：讓時間倒著走——先全部刪掉，再**一次加回一個刪除**。

**核心想法：** 一塊磚是穩定的，若且唯若它連到虛擬的**屋頂**節點（第 0 列）。抹掉一塊磚在 DSU 裡是做不到的，所以：
1. 一開始就先套用**所有**打擊（把那些格子設為 0）。
2. 用剩下的東西建 DSU，並把第 0 列的磚 union 進屋頂節點 `m*n`。
3. **反向**走過所有打擊，逐一把磚加回來。屋頂元件的大小增量 `after − before − 1`（扣掉被加回的那塊磚本身），正好就是該次打擊掉落的磚數。

**必須用按大小合併**，這樣 `size[find(roof)]` 才有意義。

```java
// java
// LC 803 - Bricks Falling When Hit
// IDEA: offline REVERSE union-find — erase every hit first, then undo them one by one;
//       bricks that fall on hit i == bricks that re-attach to the roof when hit i is undone
// time = O(M*N*α(M*N) + K*α(M*N)), space = O(M*N)
int[] p, sz;
public int[] hitBricks(int[][] grid, int[][] hits) {
    int m = grid.length, n = grid[0].length, roof = m * n;

    int[][] g = new int[m][];
    for (int i = 0; i < m; i++) g[i] = grid[i].clone();

    /** NOTE !!! record whether each hit actually removed a brick.
     *  Two hits on the same cell both see grid[r][c] == 1, so testing the ORIGINAL grid in
     *  the reverse pass credits the fall to the wrong hit. Only the first hit on a cell is
     *  effective; every later one lands on an already-empty cell and must score 0.
     */
    boolean[] effective = new boolean[hits.length];
    for (int i = 0; i < hits.length; i++) {            // step 1: apply ALL hits up front
        int r = hits[i][0], c = hits[i][1];
        if (g[r][c] == 1) { g[r][c] = 0; effective[i] = true; }
    }

    p = new int[m * n + 1];
    sz = new int[m * n + 1];
    for (int i = 0; i <= m * n; i++) { p[i] = i; sz[i] = 1; }

    // step 2: build DSU on the surviving bricks (up/left neighbours suffice for a full scan)
    for (int r = 0; r < m; r++)
        for (int c = 0; c < n; c++)
            if (g[r][c] == 1) {
                if (r == 0) union(r * n + c, roof);
                if (r > 0 && g[r - 1][c] == 1) union(r * n + c, (r - 1) * n + c);
                if (c > 0 && g[r][c - 1] == 1) union(r * n + c, r * n + c - 1);
            }

    // step 3: undo hits in reverse order
    int[][] dirs = {{1,0},{-1,0},{0,1},{0,-1}};
    int[] res = new int[hits.length];
    for (int i = hits.length - 1; i >= 0; i--) {
        if (!effective[i]) continue;                   // empty cell, or a repeat hit → nothing falls
        int r = hits[i][0], c = hits[i][1];
        int before = sz[find(roof)];
        g[r][c] = 1;                                   // put the brick back
        if (r == 0) union(r * n + c, roof);
        for (int[] d : dirs) {
            int nr = r + d[0], nc = c + d[1];
            if (nr >= 0 && nr < m && nc >= 0 && nc < n && g[nr][nc] == 1) union(r * n + c, nr * n + nc);
        }
        int after = sz[find(roof)];
        res[i] = Math.max(0, after - before - 1);      // -1: the restored brick itself never "fell"
    }
    return res;
}
private int find(int x) { return p[x] == x ? x : (p[x] = find(p[x])); }
private void union(int a, int b) {                     // union by SIZE — sz[root] must stay exact
    int ra = find(a), rb = find(b);
    if (ra == rb) return;
    if (sz[ra] < sz[rb]) { int t = ra; ra = rb; rb = t; }
    p[rb] = ra;
    sz[ra] += sz[rb];
}
```

```python
# python
# LC 803 - Bricks Falling When Hit
# IDEA: offline reverse union-find + virtual roof node + union by size
# time = O(M*N*α(M*N) + K*α(M*N)), space = O(M*N)
class Solution(object):
    def hitBricks(self, grid, hits):
        m, n = len(grid), len(grid[0])
        roof = m * n
        g = [row[:] for row in grid]
        ### NOTE !!! only the FIRST hit on a cell removes a brick; later hits on the same
        ###          cell land on an empty slot and must score 0, so record which is which.
        effective = [False] * len(hits)
        for i, (r, c) in enumerate(hits):        # apply ALL hits first
            if g[r][c] == 1:
                g[r][c] = 0
                effective[i] = True

        parent = list(range(m * n + 1))
        size = [1] * (m * n + 1)

        def find(x):
            if parent[x] != x:
                parent[x] = find(parent[x])
            return parent[x]

        def union(a, b):
            ra, rb = find(a), find(b)
            if ra == rb:
                return
            if size[ra] < size[rb]:
                ra, rb = rb, ra
            parent[rb] = ra
            size[ra] += size[rb]                 # union by size keeps size[root] exact

        for r in range(m):
            for c in range(n):
                if g[r][c] == 1:
                    if r == 0:
                        union(r * n + c, roof)   # row 0 hangs from the roof
                    if r > 0 and g[r - 1][c] == 1:
                        union(r * n + c, (r - 1) * n + c)
                    if c > 0 and g[r][c - 1] == 1:
                        union(r * n + c, r * n + c - 1)

        res = [0] * len(hits)
        for i in range(len(hits) - 1, -1, -1):   # undo hits backwards
            if not effective[i]:
                continue                         # empty cell, or a repeat hit → 0
            r, c = hits[i]
            before = size[find(roof)]
            g[r][c] = 1
            if r == 0:
                union(r * n + c, roof)
            for dr, dc in ((1, 0), (-1, 0), (0, 1), (0, -1)):
                nr, nc = r + dr, c + dc
                if 0 <= nr < m and 0 <= nc < n and g[nr][nc] == 1:
                    union(r * n + c, nr * n + nc)
            after = size[find(roof)]
            res[i] = max(0, after - before - 1)  # max(0, ...) also handles duplicate hits
        return res
```

**注意事項：**
- 打在*原始*格子裡本來就是 `0` 的位置，貢獻是 `0`。
- **同一格被重複打擊才是陷阱。** 兩次打擊看到的都是 `grid[r][c] == 1`，所以在反向那一趟用原始格子判斷，會把掉落數算給迴圈先碰到的那一次打擊——而倒著跑時，先碰到的是*較晚*的那次。正確答案應該歸給較早的那次。改成在正向那一趟記錄一個 `effective` 旗標，並依它來跳過。
- 逆向 DSU 只有在刪除是**離線**已知（一開始就全部給定）時才可行。

## 其他結構上的併查集

### 16) Accounts Merge — LC 721 — 在 email 上做併查集

> 把屬於同一個人的 email union 起來；依根分組；排序並整理輸出格式。

```java
// LC 721 - Accounts Merge
// IDEA: Union-Find — union all emails in same account; group by root
// time = O(N * M * α(N*M)), space = O(N*M)
public List<List<String>> accountsMerge(List<List<String>> accounts) {
    Map<String, String> parent = new HashMap<>();
    Map<String, String> emailToName = new HashMap<>();
    // init
    for (List<String> acc : accounts)
        for (int i = 1; i < acc.size(); i++) {
            parent.put(acc.get(i), acc.get(i));
            emailToName.put(acc.get(i), acc.get(0));
        }
    // union
    for (List<String> acc : accounts)
        for (int i = 2; i < acc.size(); i++)
            union(parent, acc.get(1), acc.get(i));
    // group by root
    Map<String, TreeSet<String>> groups = new HashMap<>();
    for (String email : parent.keySet())
        groups.computeIfAbsent(find(parent, email), k -> new TreeSet<>()).add(email);
    List<List<String>> result = new ArrayList<>();
    for (Map.Entry<String, TreeSet<String>> entry : groups.entrySet()) {
        List<String> list = new ArrayList<>();
        list.add(emailToName.get(entry.getKey()));
        list.addAll(entry.getValue());
        result.add(list);
    }
    return result;
}
private String find(Map<String, String> parent, String x) {
    if (!parent.get(x).equals(x)) parent.put(x, find(parent, parent.get(x)));
    return parent.get(x);
}
private void union(Map<String, String> parent, String x, String y) {
    parent.put(find(parent, x), find(parent, y));
}
```
```python
# python
# LC 721 - Accounts Merge
# IDEA: union every email in an account to the account's FIRST email, then group by root.
#       The name is looked up from any email in the group.
# time = O(N*M*alpha + N*M log(N*M)) for the sort, space = O(N*M)
class Solution(object):
    def accountsMerge(self, accounts):
        parent = {}
        email_to_name = {}

        def find(x):
            parent.setdefault(x, x)
            while parent[x] != x:
                parent[x] = parent[parent[x]]
                x = parent[x]
            return x

        def union(a, b):
            parent[find(a)] = find(b)

        for acc in accounts:
            name, emails = acc[0], acc[1:]
            for e in emails:
                parent.setdefault(e, e)
                email_to_name[e] = name
                ### NOTE !!! union to the FIRST email, which links the whole account in one pass
                union(emails[0], e)

        groups = {}
        for e in parent:
            groups.setdefault(find(e), []).append(e)

        return [[email_to_name[root]] + sorted(mails)
                for root, mails in groups.items()]
```

> **變化題 — LC 839 Similar String Groups**：一樣是「先 union 再依根分組」的形狀，但邊*沒有直接給你*。所有字串互為 anagram，所以就跑 O(N² · L) 的兩兩檢查——`union(i, j)` 當且僅當 `s[i] == s[j]` 或它們**恰好**有 2 個位置不同——答案就是元件數。

### 17) Smallest String with Swaps — LC 1202 — 併查集 + 排序

> union 所有可交換的配對；把每個元件內的字元排序；再把排好的字元放回去。

```java
// LC 1202 - Smallest String with Swaps
// IDEA: Union-Find — group indices; sort chars in each group and reassign
// time = O(N log N), space = O(N)
public String smallestStringWithSwaps(String s, List<List<Integer>> pairs) {
    int n = s.length();
    int[] p = new int[n];
    for (int i = 0; i < n; i++) p[i] = i;
    for (List<Integer> pair : pairs) union(p, pair.get(0), pair.get(1));
    Map<Integer, List<Integer>> groups = new HashMap<>();
    for (int i = 0; i < n; i++) groups.computeIfAbsent(find(p, i), k -> new ArrayList<>()).add(i);
    char[] res = s.toCharArray();
    for (List<Integer> idx : groups.values()) {
        char[] chars = new char[idx.size()];
        for (int i = 0; i < idx.size(); i++) chars[i] = s.charAt(idx.get(i));
        Arrays.sort(chars);
        Collections.sort(idx);
        for (int i = 0; i < idx.size(); i++) res[idx.get(i)] = chars[i];
    }
    return new String(res);
}
private int find(int[] p, int x) { return p[x]==x ? x : (p[x]=find(p,p[x])); }
private void union(int[] p, int x, int y) { p[find(p,x)] = find(p,y); }
```
```python
# python
# LC 1202 - Smallest String with Swaps
# IDEA: any two indices in the same component can be swapped freely, so the characters of a
#       component can be permuted arbitrarily -- sort them and write them back in index order
# time = O(N log N), space = O(N)
class Solution(object):
    def smallestStringWithSwaps(self, s, pairs):
        n = len(s)
        parent = list(range(n))

        def find(x):
            while parent[x] != x:
                parent[x] = parent[parent[x]]
                x = parent[x]
            return x

        for a, b in pairs:
            parent[find(a)] = find(b)

        groups = {}
        for i in range(n):
            groups.setdefault(find(i), []).append(i)

        res = list(s)
        for idxs in groups.values():
            ### NOTE !!! idxs is already ascending; sorting the CHARS and zipping them back
            ###          in that order is what yields the lexicographically smallest result
            chars = sorted(res[i] for i in idxs)
            for i, ch in zip(idxs, chars):
                res[i] = ch
        return ''.join(res)
```

### 18) Most Stones Removed with Same Row or Column — LC 947

> 把同列或同欄的石頭 union 起來；答案 = 石頭數 − 元件數。

```java
// LC 947 - Most Stones Removed with Same Row or Column
// IDEA: Union-Find — stones sharing row/column are in same component; remove all but one
// time = O(N^2 * α(N)), space = O(N)
public int removeStones(int[][] stones) {
    int n = stones.length;
    int[] p = new int[n];
    for (int i = 0; i < n; i++) p[i] = i;
    for (int i = 0; i < n; i++)
        for (int j = i+1; j < n; j++)
            if (stones[i][0] == stones[j][0] || stones[i][1] == stones[j][1])
                union(p, i, j);
    Set<Integer> roots = new HashSet<>();
    for (int i = 0; i < n; i++) roots.add(find(p, i));
    return n - roots.size();
}
private int find(int[] p, int x) { return p[x]==x ? x : (p[x]=find(p,p[x])); }
private void union(int[] p, int x, int y) { p[find(p,x)] = find(p,y); }
```
```python
# python
# LC 947 - Most Stones Removed with Same Row or Column
# IDEA: stones sharing a row or column are one component; a component of size k can be
#       reduced to a single stone, so the answer is n - (number of components)
# time = O(N^2 * alpha(N)), space = O(N)
class Solution(object):
    def removeStones(self, stones):
        n = len(stones)
        parent = list(range(n))

        def find(x):
            while parent[x] != x:
                parent[x] = parent[parent[x]]
                x = parent[x]
            return x

        for i in range(n):
            for j in range(i + 1, n):
                if stones[i][0] == stones[j][0] or stones[i][1] == stones[j][1]:
                    parent[find(i)] = find(j)

        ### NOTE !!! count ROOTS, not unions -- a component of any size contributes exactly 1
        return n - len({find(i) for i in range(n)})
```

> **變化題 — LC 765 Couples Holding Hands**：改用**情侶 id** 而不是人的 id 來 union——對每組座位配對 `(2i, 2i+1)` 做 `union(row[2i]/2, row[2i+1]/2)`。答案 = `n_couples − components`（大小為 `k` 的元件需要 `k−1` 次交換）。和 LC 947 是同一套「元件數 → 答案」的算術。

### 19) Smallest Subtree with all the Deepest Nodes — LC 865 — BFS + 併查集式上爬

> 用 BFS 找出最深的節點與父節點對照表；接著讓所有最深節點沿著父節點「往上爬」，直到匯聚到 LCA。這與 LC 1123 是同一題。

```java
// LC 865 - Smallest Subtree with all the Deepest Nodes
// IDEA: BFS to find deepest level + build parent map, then climb upward until convergence
// time = O(N), space = O(N)
public TreeNode subtreeWithAllDeepest(TreeNode root) {
    Map<TreeNode, TreeNode> parent = new HashMap<>();
    Queue<TreeNode> q = new LinkedList<>();
    q.offer(root);
    parent.put(root, null);
    List<TreeNode> level = new ArrayList<>();

    // BFS: build parent map, track each level (last level = deepest)
    while (!q.isEmpty()) {
        int size = q.size();
        level = new ArrayList<>();
        for (int i = 0; i < size; i++) {
            TreeNode cur = q.poll();
            level.add(cur);
            if (cur.left != null) { parent.put(cur.left, cur); q.offer(cur.left); }
            if (cur.right != null) { parent.put(cur.right, cur); q.offer(cur.right); }
        }
    }

    // Climb: replace each node with its parent until all converge
    Set<TreeNode> set = new HashSet<>(level);
    while (set.size() > 1) {
        Set<TreeNode> next = new HashSet<>();
        for (TreeNode node : set) next.add(parent.get(node));
        set = next;
    }
    return set.iterator().next();
}
```
```python
# python
# LC 865 - Smallest Subtree with all the Deepest Nodes  (same problem as LC 1123)
# IDEA: BFS to the deepest level while recording each node's parent, then walk every
#       deepest node upward in lockstep until the set collapses to one node -- the LCA
# time = O(N), space = O(N)
from collections import deque

class Solution(object):
    def subtreeWithAllDeepest(self, root):
        if not root:
            return None
        parent = {root: None}
        q = deque([root])
        level = [root]

        while q:
            level = []
            for _ in range(len(q)):
                cur = q.popleft()
                level.append(cur)
                for child in (cur.left, cur.right):
                    if child:
                        parent[child] = cur
                        q.append(child)
        # `level` now holds the LAST level visited = the deepest nodes

        ### NOTE !!! climbing in LOCKSTEP is what makes this correct -- all nodes are at the
        ###          same depth, so they reach their common ancestor on the same step
        nodes = set(level)
        while len(nodes) > 1:
            nodes = {parent[n] for n in nodes}
        return nodes.pop()
```
