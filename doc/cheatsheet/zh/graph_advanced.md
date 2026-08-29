# 進階圖論演算法

> **範圍** — 第一輪面試準備可以先跳過的圖論技巧：Tarjan 的 low-link 家族（強連通分量、橋、關節點）、Euler 路徑與迴路、最大流／最小割，以及二分圖的延伸主題 — 用併查集判定、最大匹配與貪婪 k-著色。
> **另見**：[graph.md](./graph.md) — 圖的表示法、走訪、連通性與環偵測，以及把你導向這裡的選擇表；[graph_examples.md](./graph_examples.md) — 解法範例的存放處；[dfs_advanced.md](./dfs_advanced.md) — DFS 那份文件自己的 Hierholzer 與 Tarjan 找橋模板；[union_find.md](./union_find.md) — 這些模板所依賴的併查集原語。

## LeetCode 題目清單

- [Graph Theory](https://leetcode.com/problem-list/graph/)
- [Strongly Connected Component](https://leetcode.com/problem-list/strongly-connected-component/)
- [Eulerian Circuit](https://leetcode.com/problem-list/eulerian-circuit/)
- [Biconnected Component](https://leetcode.com/problem-list/biconnected-component/)

## 總覽

這裡的東西全都是**low-link DFS**、**吃掉邊的 DFS**，或**找增廣路徑的 BFS**。它們共用同一個形狀：一次普通的走訪，再加一個額外的陣列 — `low[]`、被消耗掉的鄰接表，或殘餘容量矩陣 — 就把走訪變成了一個證明。

### 關鍵性質
- **複雜度**：見總結區的[複雜度速查](#complexity-quick-reference)表
- **核心想法**：一趟 DFS/BFS，帶著足夠的記帳資訊，回答一個結構性的問題
- **什麼時候用**：題目問哪條*邊*或哪個*頂點*是關鍵、要你把每條邊剛好用一次，或要一個容量／匹配的上界

## 題型分類

### **類型 1：關鍵結構（Low-Link）**
- **描述**：哪些頂點或邊撐住整張圖
- **範例**：LC 1192（Critical Connections）、LC 1568（Minimum Days to Disconnect Island）
- **模式**：Tarjan `disc[]` / `low[]` DFS

### **類型 2：強連通性**
- **描述**：有向圖中互相可達的極大集合
- **範例**：LC 685（Redundant Connection II）、LC 1557（Minimum Vertices to Reach All Nodes）
- **模式**：Tarjan SCC 或 Kosaraju 兩趟法

### **類型 3：覆蓋所有邊的走法**
- **描述**：每條邊剛好用一次
- **範例**：LC 332（Reconstruct Itinerary）、LC 753（Cracking the Safe）
- **模式**：Hierholzer — 後序輸出，最後反轉

### **類型 4：流量與匹配**
- **描述**：容量上界、指派問題、最小割
- **範例**：LC 1349（Maximum Students Taking Exam）、LC 1595（Minimum Cost to Connect Two Groups）
- **模式**：Ford-Fulkerson / Edmonds-Karp、Kuhn 匹配

### **類型 5：超過兩種顏色的著色**
- **描述**：分配 `k` 個標籤，相鄰不得衝突
- **範例**：LC 1042（Flower Planting With No Adjacent）
- **模式**：`k > max_degree` 時用貪婪，否則回溯

## 模板與演算法

### 模板比較表

| 模板 | 回答什麼問題 | 時間 | 圖的類型 |
|---|---|---|---|
| Tarjan SCC | 哪些頂點互相可達？ | O(V + E) | 有向 |
| Tarjan 找橋 | 哪條邊是關鍵？ | O(V + E) | 無向 |
| Tarjan 關節點 | 哪個頂點是關鍵？ | O(V + E) | 無向 |
| Hierholzer | 能不能把每條邊剛好走一次？ | O(E) | 皆可 |
| 併查集判二分圖 | 有沒有奇環？ | O(E·α(V)) | 無向 |
| Kuhn 匹配 | 兩側之間最大的配對數？ | O(V·E) | 二分圖 |
| Edmonds-Karp | 最大流／最小割？ | O(V·E²) | 有向、有權重 |
| 貪婪 k-著色 | `k` 個標籤，相鄰不衝突？ | O(V + E) | 無向、`k > max_deg` |

### 模板 1：Tarjan 的 Low-Link DFS — SCC、橋、關節點 — LC 1192 ⭐⭐⭐⭐

**總覽：**
Tarjan 演算法是一套以 DFS 為基礎、用來找出圖中關鍵結構的技巧：
1. **強連通分量（SCC）** - 互相可達的極大頂點集合（有向圖）
2. **橋（Bridges）** - 移除後會讓圖斷開的邊（無向圖）
3. **關節點（Articulation Points / Cut Vertices）** - 移除後會讓圖斷開的頂點（無向圖）

**核心概念：**
用 DFS 搭配兩個關鍵陣列：
- `disc[v]`：頂點 v 的發現時間（第一次被走訪的時刻）
- `low[v]`：從 v 的子樹能回到的最小發現時間

**時間複雜度**：O(V + E) - 單趟 DFS 走訪
**空間複雜度**：O(V) - 遞迴堆疊 + 陣列

---

#### 1.1) 強連通分量（SCC）

**定義**：在有向圖中，SCC 是一個極大的頂點集合，集合內任兩點都能互相到達。

**關鍵洞見：**
- 用一個堆疊記錄目前 DFS 路徑上的頂點
- 當 `low[v] == disc[v]` 時，v 就是某個 SCC 的根
- 從堆疊一路彈到 v 為止，就得到完整的 SCC

**演算法步驟：**
1. 初始化 `disc[]`、`low[]` 與堆疊
2. 從每個還沒走訪的頂點開始 DFS
3. 對每個頂點 v：
   - 設 `disc[v] = low[v] = timer++`
   - 把 v 推進堆疊
   - 對每個鄰居 u：
     - 若還沒走訪：DFS(u)，然後更新 `low[v] = min(low[v], low[u])`
     - 若 u 還在堆疊上：更新 `low[v] = min(low[v], disc[u])`
   - 若 `low[v] == disc[v]`：彈堆疊直到 v，組成一個 SCC

##### Python 實作

```python
# Tarjan's Algorithm for SCC
def tarjan_scc(n, graph):
    """
    Find all strongly connected components using Tarjan's algorithm.

    Args:
        n: number of vertices (0 to n-1)
        graph: adjacency list (directed graph)

    Returns:
        List of SCCs, where each SCC is a list of vertices

    Time: O(V + E)
    Space: O(V)
    """
    disc = [-1] * n  # Discovery times
    low = [-1] * n   # Lowest reachable
    on_stack = [False] * n
    stack = []
    sccs = []
    timer = [0]  # Use list for mutability

    def dfs(v):
        # Initialize discovery time and low value
        disc[v] = low[v] = timer[0]
        timer[0] += 1
        stack.append(v)
        on_stack[v] = True

        # Explore neighbors
        for u in graph[v]:
            if disc[u] == -1:
                # Unvisited neighbor
                dfs(u)
                low[v] = min(low[v], low[u])
            elif on_stack[u]:
                # Back edge to vertex on stack
                low[v] = min(low[v], disc[u])

        # If v is a root of SCC, pop the SCC
        if low[v] == disc[v]:
            scc = []
            while True:
                u = stack.pop()
                on_stack[u] = False
                scc.append(u)
                if u == v:
                    break
            sccs.append(scc)

    # Run DFS from each unvisited vertex
    for i in range(n):
        if disc[i] == -1:
            dfs(i)

    return sccs

# Example:
# graph = {0: [1], 1: [2], 2: [0, 3], 3: [4], 4: [5], 5: [3]}
#    0 → 1 → 2
#    ↑       ↓
#    └───────┘    3 ⇄ 4 → 5
#                     ↑   ↓
#                     └───┘
# SCCs: [[0, 2, 1], [3, 5, 4]]
```

##### Java 實作

```java
// Tarjan's SCC Algorithm
/**
 * LC 1192 - Critical Connections in a Network (related)
 *
 * time = O(V + E)
 * space = O(V)
 */
class TarjanSCC {
    private int timer = 0;
    private int[] disc;
    private int[] low;
    private boolean[] onStack;
    private Stack<Integer> stack;
    private List<List<Integer>> sccs;

    public List<List<Integer>> findSCCs(int n, List<List<Integer>> graph) {
        disc = new int[n];
        low = new int[n];
        onStack = new boolean[n];
        stack = new Stack<>();
        sccs = new ArrayList<>();

        Arrays.fill(disc, -1);
        Arrays.fill(low, -1);

        // DFS from each unvisited vertex
        for (int i = 0; i < n; i++) {
            if (disc[i] == -1) {
                dfs(i, graph);
            }
        }

        return sccs;
    }

    private void dfs(int v, List<List<Integer>> graph) {
        // Initialize
        disc[v] = low[v] = timer++;
        stack.push(v);
        onStack[v] = true;

        // Explore neighbors
        for (int u : graph.get(v)) {
            if (disc[u] == -1) {
                // Unvisited
                dfs(u, graph);
                low[v] = Math.min(low[v], low[u]);
            } else if (onStack[u]) {
                // Back edge
                low[v] = Math.min(low[v], disc[u]);
            }
        }

        // Root of SCC found
        if (low[v] == disc[v]) {
            List<Integer> scc = new ArrayList<>();
            while (true) {
                int u = stack.pop();
                onStack[u] = false;
                scc.add(u);
                if (u == v) break;
            }
            sccs.add(scc);
        }
    }
}
```

---

#### 1.2) 找橋（Critical Connections）

**定義**：橋是一條移除後會讓連通分量數量增加（也就是讓圖斷開）的邊。

**關鍵洞見：**
- 邊 (u, v) 是橋，若且唯若 `low[v] > disc[u]`
- 這代表 v 若不走 (u, v) 這條邊，就回不到任何比 u 更早被發現的頂點

**演算法步驟：**
1. 帶著 `disc[]` 與 `low[]` 跑 DFS
2. 對 DFS 樹中的每條邊 (u, v)：
   - 若 `low[v] > disc[u]`：(u, v) 是橋

##### Python 實作

```python
# Tarjan's Algorithm for Bridges
def find_bridges(n, edges):
    """
    Find all bridges (critical connections) in an undirected graph.

    Args:
        n: number of vertices
        edges: list of [u, v] edges

    Returns:
        List of bridges (critical edges)

    Time: O(V + E)
    Space: O(V + E)
    """
    # Build adjacency list
    graph = [[] for _ in range(n)]
    for u, v in edges:
        graph[u].append(v)
        graph[v].append(u)

    disc = [-1] * n
    low = [-1] * n
    bridges = []
    timer = [0]

    def dfs(v, parent):
        disc[v] = low[v] = timer[0]
        timer[0] += 1

        for u in graph[v]:
            if u == parent:
                # Skip edge to parent (undirected graph)
                continue

            if disc[u] == -1:
                # Unvisited neighbor
                dfs(u, v)
                low[v] = min(low[v], low[u])

                # Check if (v, u) is a bridge
                if low[u] > disc[v]:
                    bridges.append([v, u])
            else:
                # Back edge
                low[v] = min(low[v], disc[u])

    # Run DFS from each component
    for i in range(n):
        if disc[i] == -1:
            dfs(i, -1)

    return bridges

# Example:
# n = 4, edges = [[0,1],[1,2],[2,0],[1,3]]
#
#    0 --- 1 --- 3
#     \   /
#      \ /
#       2
#
# Bridge: [1, 3] (removing this disconnects 3 from rest)
```

##### Java 實作

```java
// LC 1192 - Critical Connections in a Network
/**
 * time = O(V + E)
 * space = O(V + E)
 */
class Solution {
    private int timer = 0;
    private int[] disc;
    private int[] low;
    private List<List<Integer>> bridges;

    public List<List<Integer>> criticalConnections(int n, List<List<Integer>> connections) {
        // Build adjacency list
        List<List<Integer>> graph = new ArrayList<>();
        for (int i = 0; i < n; i++) {
            graph.add(new ArrayList<>());
        }
        for (List<Integer> conn : connections) {
            int u = conn.get(0);
            int v = conn.get(1);
            graph.get(u).add(v);
            graph.get(v).add(u);
        }

        disc = new int[n];
        low = new int[n];
        bridges = new ArrayList<>();
        Arrays.fill(disc, -1);

        // DFS from vertex 0 (graph is connected in this problem)
        dfs(0, -1, graph);

        return bridges;
    }

    private void dfs(int v, int parent, List<List<Integer>> graph) {
        disc[v] = low[v] = timer++;

        for (int u : graph.get(v)) {
            if (u == parent) continue;  // Skip parent edge

            if (disc[u] == -1) {
                // Unvisited
                dfs(u, v, graph);
                low[v] = Math.min(low[v], low[u]);

                // Check for bridge
                if (low[u] > disc[v]) {
                    bridges.add(Arrays.asList(v, u));
                }
            } else {
                // Back edge
                low[v] = Math.min(low[v], disc[u]);
            }
        }
    }
}
```

---

#### 1.3) 找關節點（Cut Vertices）

**定義**：關節點是一個移除後會讓連通分量數量增加的頂點。

**關鍵洞見：**
- 頂點 u 是關節點，若：
  - **是 DFS 樹的根**：有 2 個以上的子節點
  - **不是根**：存在某個子節點 v 使得 `low[v] >= disc[u]`

**演算法步驟：**
1. 帶著 `disc[]` 與 `low[]` 跑 DFS
2. 對每個頂點 u：
   - 若是根：數子節點個數，≥ 2 就是關節點
   - 若不是根：檢查是否有子節點 v 滿足 `low[v] >= disc[u]`

##### Python 實作

```python
# Tarjan's Algorithm for Articulation Points
def find_articulation_points(n, edges):
    """
    Find all articulation points (cut vertices).

    Args:
        n: number of vertices
        edges: list of [u, v] edges

    Returns:
        Set of articulation points

    Time: O(V + E)
    Space: O(V + E)
    """
    # Build adjacency list
    graph = [[] for _ in range(n)]
    for u, v in edges:
        graph[u].append(v)
        graph[v].append(u)

    disc = [-1] * n
    low = [-1] * n
    ap = set()  # Articulation points
    timer = [0]

    def dfs(v, parent):
        children = 0
        disc[v] = low[v] = timer[0]
        timer[0] += 1

        for u in graph[v]:
            if u == parent:
                continue

            if disc[u] == -1:
                # Unvisited child
                children += 1
                dfs(u, v)
                low[v] = min(low[v], low[u])

                # Check if v is articulation point
                # Case 1: Root with 2+ children
                if parent == -1 and children > 1:
                    ap.add(v)

                # Case 2: Non-root with child that can't reach ancestor
                if parent != -1 and low[u] >= disc[v]:
                    ap.add(v)
            else:
                # Back edge
                low[v] = min(low[v], disc[u])

    # Run DFS from each component
    for i in range(n):
        if disc[i] == -1:
            dfs(i, -1)

    return list(ap)

# Example:
# n = 5, edges = [[0,1],[1,2],[2,0],[1,3],[3,4]]
#
#    0 --- 1 --- 3 --- 4
#     \   /
#      \ /
#       2
#
# Articulation points: [1, 3]
# (Removing 1 disconnects {0,2} from {3,4})
# (Removing 3 disconnects 4 from rest)
```

##### Java 實作

```java
// Articulation Points Algorithm
/**
 * time = O(V + E)
 * space = O(V + E)
 */
class ArticulationPoints {
    private int timer = 0;
    private int[] disc;
    private int[] low;
    private Set<Integer> ap;

    public List<Integer> findArticulationPoints(int n, int[][] edges) {
        // Build adjacency list
        List<List<Integer>> graph = new ArrayList<>();
        for (int i = 0; i < n; i++) {
            graph.add(new ArrayList<>());
        }
        for (int[] edge : edges) {
            int u = edge[0];
            int v = edge[1];
            graph.get(u).add(v);
            graph.get(v).add(u);
        }

        disc = new int[n];
        low = new int[n];
        ap = new HashSet<>();
        Arrays.fill(disc, -1);

        // DFS from each component
        for (int i = 0; i < n; i++) {
            if (disc[i] == -1) {
                dfs(i, -1, graph);
            }
        }

        return new ArrayList<>(ap);
    }

    private void dfs(int v, int parent, List<List<Integer>> graph) {
        int children = 0;
        disc[v] = low[v] = timer++;

        for (int u : graph.get(v)) {
            if (u == parent) continue;

            if (disc[u] == -1) {
                children++;
                dfs(u, v, graph);
                low[v] = Math.min(low[v], low[u]);

                // Root with 2+ children
                if (parent == -1 && children > 1) {
                    ap.add(v);
                }

                // Non-root with blocking child
                if (parent != -1 && low[u] >= disc[v]) {
                    ap.add(v);
                }
            } else {
                // Back edge
                low[v] = Math.min(low[v], disc[u]);
            }
        }
    }
}
```

---

#### 1.4) 圖解範例：Tarjan 演算法逐步走一遍

```text
Graph (Directed):
    0 → 1 → 2
    ↑       ↓
    └───────┘     3 ⇄ 4
                  ↓   ↑
                  5 ──┘

DFS Traversal:

Step 1: Start at 0
  disc[0] = low[0] = 0
  stack = [0]

Step 2: Visit 1 from 0
  disc[1] = low[1] = 1
  stack = [0, 1]

Step 3: Visit 2 from 1
  disc[2] = low[2] = 2
  stack = [0, 1, 2]

Step 4: Back edge 2→0 (0 already on stack)
  low[2] = min(2, disc[0]) = 0
  Backtrack to 1: low[1] = min(1, low[2]) = 0
  Backtrack to 0: low[0] = min(0, low[1]) = 0

Step 5: At 0, low[0] == disc[0] → SCC found!
  Pop stack: [2, 1, 0]
  SCC #1: {0, 1, 2}

Step 6: Start at 3
  disc[3] = low[3] = 3
  stack = [3]

Step 7: Visit 4 from 3
  disc[4] = low[4] = 4
  stack = [3, 4]

Step 8: Visit 5 from 4
  disc[5] = low[5] = 5
  stack = [3, 4, 5]

Step 9: Edge 5→3 (back edge)
  low[5] = min(5, disc[3]) = 3
  Backtrack to 4: low[4] = min(4, low[5]) = 3
  Backtrack to 3: low[3] = min(3, low[4]) = 3

Step 10: At 3, low[3] == disc[3] → SCC found!
  Pop stack: [5, 4, 3]
  SCC #2: {3, 4, 5}

Final SCCs: [{0,1,2}, {3,4,5}]
```

---

#### 1.5) 經典 LeetCode 題目

| 題目 | LC# | 變形 | 難度 | 關鍵洞見 |
|---------|-----|---------|------------|-------------|
| **Critical Connections in Network** | **1192** | **橋** | **Hard** | 用 Tarjan 找出所有橋 |
| Number of Provinces | 547 | 基本連通性 | Medium | 數連通分量 |
| Redundant Connection | 684 | 環偵測 | Medium | 找出造成環的那條邊 |
| Redundant Connection II | 685 | 有向圖 | Hard | 有向圖的 SCC + 環 |
| Minimum Number of Vertices | 1557 | SCC 的源點 | Medium | 找出沒有入邊的頂點 |

---

#### 1.6) 比較：SCC 用 Tarjan 還是 Kosaraju

| 面向 | Tarjan 演算法 | Kosaraju 演算法 |
|--------|-------------------|---------------------|
| **趟數** | 單趟 DFS | 兩趟 DFS |
| **時間** | O(V + E) | O(V + E) |
| **空間** | O(V) 堆疊 | O(V) + 轉置圖 |
| **複雜度** | 較複雜（一趟搞定） | 較單純（分兩趟） |
| **額外空間** | 存 SCC 的堆疊 | 反向圖 |
| **取捨** | 效率較好（只跑一趟） | 比較好理解 |

---

#### 1.7) 面試技巧

**1. 辨識模式：**
```text
"critical connections" → Bridges (Tarjan)
"strongly connected" → SCC (Tarjan or Kosaraju)
"cut vertices" → Articulation points (Tarjan)
"remove vertex/edge disconnects graph" → Articulation/Bridge
```

**2. 關鍵差異：**
```text
SCC: Directed graph, maximal mutually reachable sets
Bridges: Undirected graph, critical edges
Articulation Points: Undirected graph, critical vertices

low[v] == disc[v] → Root of SCC (directed)
low[v] > disc[u] → (u,v) is bridge (undirected)
low[v] >= disc[u] → u is articulation point (undirected)
```

**3. 常見錯誤：**
- 無向圖忘了跳過連回父節點的邊
- 關節點的判斷條件搞錯（根 vs 非根）
- SCC 沒用 `on_stack` 陣列（會算出錯的 SCC）
- 處理 back edge 時把 `disc[u]` 和 `low[u]` 搞混

**4. 要背起來的模板：**
```python
def tarjan_template(v, parent=-1):
    disc[v] = low[v] = timer
    timer += 1

    for u in graph[v]:
        if u == parent:  # Undirected graphs only
            continue

        if disc[u] == -1:
            # Tree edge
            dfs(u, v)
            low[v] = min(low[v], low[u])
            # Check condition here (bridge, AP, etc.)
        else:
            # Back edge
            low[v] = min(low[v], disc[u])  # Or check on_stack for SCC
```

**5. 可以說出口的重點：**
- 「Tarjan 只用一趟 DFS，搭配發現時間」
- 「low[v] 記錄 v 的子樹能回到的最早頂點」
- 「橋／關節點代表圖的關鍵結構」
- 「SCC 代表極大的強連通區域」

---

#### 1.8) 關節點 vs 橋 — 一行講完差別

| | 關節點 | 橋 |
|--|-------------------|--------|
| 是什麼 | 移除後會讓圖斷開的頂點 | 移除後會讓圖斷開的邊 |
| 條件 | `low[v] >= disc[u]`（非根時） | `low[v] > disc[u]` |
| LC | 1192（Critical Connections = 橋） | 1192 |

---

### 模板 2：Euler 路徑／迴路（Hierholzer） — LC 753 ⭐⭐⭐

**核心想法**：**Euler 迴路**把每條*邊*剛好用一次（對照：Hamiltonian 路徑是每個*頂點*用一次）。Hierholzer 演算法就是一趟 DFS，在某個節點的出邊**全部用完之後**才把它加進輸出，最後反轉。

**存在條件**：

| 圖 | Euler 迴路 | Euler 路徑 |
|-------|---------------|------------|
| 無向 | 每個頂點的度數都是偶數 | 奇數度頂點剛好 0 個或 2 個 |
| 有向 | 每個頂點 `in == out` | 一個頂點 `out-in==1`（起點），一個 `in-out==1`（終點） |

**LC 753 的建模技巧**：不要去搜尋全部 `k^n` 個字串。改建一張 **de Bruijn 圖** — 節點 = 最後 `n-1` 位數字，邊 = 補上一位數字（每個節點有 `k` 條，所以每個節點都滿足 `in == out == k` → Euler 迴路必定存在）。走完這條迴路產生的字串，會讓**每個**長度為 `n` 的密碼都剛好出現一次。

```java
// java
// LC 753 - Cracking the Safe
// IDEA: de Bruijn graph + Hierholzer Euler circuit.
//       node = (n-1)-digit prefix, edge = one appended digit.
// time = O(k^n), space = O(k^n)
import java.util.*;

public class Solution {
    private Set<String> seen;      // visited EDGES (the n-digit strings)
    private StringBuilder sb;

    public String crackSafe(int n, int k) {
        seen = new HashSet<>();
        sb = new StringBuilder();

        StringBuilder s = new StringBuilder();
        for (int i = 0; i < n - 1; i++) {
            s.append('0');
        }
        String start = s.toString();

        dfs(start, k);
        // post-order emission => append the starting node back at the end
        return sb.toString() + start;
    }

    private void dfs(String node, int k) {
        for (int d = 0; d < k; d++) {
            String edge = node + d;          // the n-digit password = an edge
            if (seen.add(edge)) {            // add() returns false if already used
                dfs(edge.substring(1), k);   // move to next node = drop first digit
                sb.append(d);                // emit AFTER exhausting the subtree
            }
        }
    }
}
```

```python
# python
# LC 753 - Cracking the Safe
# IDEA: de Bruijn graph + Hierholzer Euler circuit
# time = O(k^n), space = O(k^n)
class Solution(object):
    def crackSafe(self, n, k):
        seen = set()      # visited EDGES (n-digit strings)
        out = []

        def dfs(node):
            for d in map(str, range(k)):
                edge = node + d
                if edge not in seen:
                    seen.add(edge)
                    dfs(edge[1:])     # next node = drop the first digit
                    out.append(d)     # emit AFTER the subtree is exhausted

        start = "0" * (n - 1)
        dfs(start)
        return "".join(out) + start

# crackSafe(2, 2) -> "01100"  (contains 00, 01, 10, 11)
# length is always k^n + n - 1
```

**迭代版 Hierholzer（想法相同，不用遞迴 — LC 332 Reconstruct Itinerary 就用這個）**：
```python
# python
# time = O(E log E) with sorting, space = O(E)
def euler_path(graph, start):
    """graph: node -> list of next nodes (mutable, consumed as we walk)"""
    stack, route = [start], []
    while stack:
        while graph[stack[-1]]:
            stack.append(graph[stack[-1]].pop())   # walk until stuck
        route.append(stack.pop())                  # stuck => this node is final
    return route[::-1]
```

**面試訊號**：「每條邊／每個轉換剛好用一次」、「包含所有組合的最短字串」→ 想 Euler，不是 Hamiltonian，也不是暴力。

---

### 模板 3：用併查集判定二分圖

*二著色的變形*：不要傳播顏色，改成給每個頂點一個代表「另一邊」的**分身** `v + n`。這樣邊 `(u, v)` 就代表 *u 和 v 不能在同一個集合*，做法是把 `u` 和 `v` 的分身 union 起來、反過來也做一次；只要 `u` 和 `v` 已經連通，衝突就當場現形。邊是即時一條條進來時特別好用，因為 BFS/DFS 著色每次都得從頭重跑。

```python
def is_bipartite_union_find(n, edges):
    """Check bipartite using Union-Find for conflict detection"""

    class UnionFind:
        def __init__(self, n):
            self.parent = list(range(2 * n))  # 2n for opposite groups

        def find(self, x):
            if self.parent[x] != x:
                self.parent[x] = self.find(self.parent[x])
            return self.parent[x]

        def union(self, x, y):
            px, py = self.find(x), self.find(y)
            if px != py:
                self.parent[px] = py

        def connected(self, x, y):
            return self.find(x) == self.find(y)

    uf = UnionFind(n)

    # For each edge (u,v), union u with opposite of v, and v with opposite of u
    for u, v in edges:
        if uf.connected(u, v):  # Same group conflict
            return False

        # u should be in opposite group of v
        uf.union(u, v + n)  # u with opposite of v
        uf.union(v, u + n)  # v with opposite of u

    return True
```

### 模板 4：二分圖最大匹配（Kuhn 演算法）

**1. 二分圖最大匹配**
```python
def max_bipartite_matching(graph, n, m):
    """Find maximum matching in bipartite graph"""
    match = [-1] * m

    def dfs(u, visited):
        for v in graph[u]:
            if not visited[v]:
                visited[v] = True
                if match[v] == -1 or dfs(match[v], visited):
                    match[v] = u
                    return True
        return False

    result = 0
    for u in range(n):
        visited = [False] * m
        if dfs(u, visited):
            result += 1

    return result
```

### 模板 5：帶自訂衝突規則的二分圖驗證

**2. 用自訂邏輯驗證二分圖**
```python
def validate_bipartite_assignment(assignments, conflicts):
    """
    Validate if assignment is bipartite given conflict pairs
    assignments: list of items to assign
    conflicts: list of (item1, item2) that cannot be in same group
    """
    from collections import defaultdict

    graph = defaultdict(list)
    for u, v in conflicts:
        graph[u].append(v)
        graph[v].append(u)

    colors = {}

    def can_color(item, color):
        if item in colors:
            return colors[item] == color

        colors[item] = color
        for conflict_item in graph[item]:
            if not can_color(conflict_item, 1 - color):
                return False
        return True

    for item in assignments:
        if item not in colors:
            if not can_color(item, 0):
                return False, {}

    # Return partition
    group_a = [item for item, color in colors.items() if color == 0]
    group_b = [item for item, color in colors.items() if color == 1]

    return True, {"Group A": group_a, "Group B": group_b}
```

### 模板 6：貪婪 k-著色（兩種顏色不夠用時） — LC 1042 ⭐⭐⭐

*二分圖的變形*：有 `k` 種顏色、而且保證每個頂點的度數 `< k` 時，根本不需要搜尋或回溯 — 照順序走過每個頂點，挑一個已著色鄰居沒用過的顏色就好。LC 1042 保證度數 ≤ 3、可用顏色有 4 種，所以貪婪一趟一定成功。

```python
# python
# LC 1042 - Flower Planting With No Adjacent
# IDEA: degree <= 3 and 4 colors available => a free color ALWAYS exists.
#       greedy single pass, no bipartite check / no backtracking needed.
# time = O(V + E), space = O(V + E)
from collections import defaultdict

class Solution(object):
    def gardenNoAdj(self, n, paths):
        g = defaultdict(list)
        for a, b in paths:
            g[a].append(b)
            g[b].append(a)

        res = [0] * n                      # res[i-1] = flower type of garden i
        for i in range(1, n + 1):
            used = {res[j - 1] for j in g[i]}   # 0 = "not yet colored"
            res[i - 1] = next(c for c in (1, 2, 3, 4) if c not in used)
        return res
```

```java
// java
// LC 1042 - Flower Planting With No Adjacent
// time = O(V + E), space = O(V + E)
public int[] gardenNoAdj(int n, int[][] paths) {
    List<List<Integer>> g = new ArrayList<>();
    for (int i = 0; i <= n; i++) {
        g.add(new ArrayList<>());
    }
    for (int[] p : paths) {
        g.get(p[0]).add(p[1]);
        g.get(p[1]).add(p[0]);
    }

    int[] res = new int[n];
    for (int i = 1; i <= n; i++) {
        boolean[] used = new boolean[5];
        for (int nb : g.get(i)) {
            used[res[nb - 1]] = true;      // res = 0 for uncolored, harmless
        }
        for (int c = 1; c <= 4; c++) {
            if (!used[c]) { res[i - 1] = c; break; }
        }
    }
    return res;
}
```

**關鍵差別**：二著色（二分圖）需要 BFS/DFS 傳播，因為選了一個顏色就**強制**了鄰居的顏色。當顏色數 `k > max_degree` 時，選擇之間永遠不會衝突，所以貪婪就是最佳解 — 把這句話講出來，不要急著搬回溯。

### 模板 7：最大流／最小割 — Ford-Fulkerson（Edmonds-Karp）

**最小割 = 最大流**（最大流最小割定理）。

```python
from collections import defaultdict, deque

def max_flow(graph, source, sink, n):
    """graph[u][v] = capacity. Returns max flow from source to sink."""
    def bfs(source, sink, parent):
        visited = set([source])
        queue = deque([source])
        while queue:
            u = queue.popleft()
            for v in range(n):
                if v not in visited and graph[u][v] > 0:
                    visited.add(v)
                    parent[v] = u
                    if v == sink: return True
                    queue.append(v)
        return False

    flow = 0
    while True:
        parent = [-1] * n
        if not bfs(source, sink, parent):
            break
        # Find min capacity along the path
        path_flow = float('inf')
        s = sink
        while s != source:
            u = parent[s]
            path_flow = min(path_flow, graph[u][s])
            s = parent[s]
        # Update capacities
        s = sink
        while s != source:
            u = parent[s]
            graph[u][s] -= path_flow
            graph[s][u] += path_flow
            s = parent[s]
        flow += path_flow
    return flow
```

**時間**：Edmonds-Karp 是 O(VE²)。**用在**：網路容量、匹配、排班。

## 總結與速查

### 複雜度速查

| 演算法 | 時間 | 空間 | 備註 |
|---|---|---|---|
| Tarjan（SCC／橋／關節點） | O(V + E) | O(V) | 單趟 DFS，`disc[]` + `low[]` |
| Kosaraju SCC | O(V + E) | O(V + E) | 兩趟 DFS，需要轉置圖 |
| Hierholzer Euler 路徑 | O(E) | O(E) | 邊需要排序時是 O(E log E)（LC 332） |
| 併查集判二分圖 | O(E·α(V)) | O(V) | `2n` 個節點：`v` 和它的分身 `v + n` |
| Kuhn 二分圖匹配 | O(V·E) | O(V) | 每個左側頂點跑一次增廣 DFS |
| Edmonds-Karp 最大流 | O(V·E²) | O(V²) | 用 BFS 找增廣路徑；最小割 = 最大流 |
| 貪婪 k-著色 | O(V + E) | O(V + E) | 只在 `k > max_degree` 時才正確 |

### 面試訊號 → 模板

| 訊號 | 模板 |
|---|---|
| 「關鍵連線」、「拔掉一條線路網路就斷了」 | Tarjan 找橋 |
| 「割點」、「移掉一台伺服器叢集就裂了」 | Tarjan 關節點 |
| 「強連通」、「互相可達」 | Tarjan SCC（或 Kosaraju） |
| 「每條邊／每個轉換剛好用一次」 | Hierholzer Euler |
| 「包含所有組合的最短字串」 | de Bruijn 圖 + Euler 迴路 |
| 「兩組，這些配對不能同組」，而且邊是即時進來的 | 併查集判二分圖 |
| 「最大的配對集合」 | Kuhn 匹配 |
| 「最大吞吐量」、「最便宜的切斷方案」 | Edmonds-Karp |
| 「k 個標籤，每個頂點的鄰居少於 k 個」 | 貪婪 k-著色 |

### 相關主題
- **[graph.md](./graph.md)**：圖的表示法、走訪與環偵測 — 先讀那份
- **[union_find.md](./union_find.md)**：模板 3 和 4 預設你已經會的併查集原語
- **[dfs_advanced.md](./dfs_advanced.md)**：DFS 那份文件自己的 Hierholzer 與 Tarjan 找橋模板
- **[topology_sorting.md](./topology_sorting.md)**：把 SCC 縮點之後會得到一張可以排序的 DAG
- **最小生成樹**：Kruskal 就是 [union_find.md](./union_find.md) 加上一份排序好的邊清單；
  Prim 則是 [heap.md](./heap.md) 加上一個 visited 集合。LC 1135、LC 1584。
