# BFS —— LeetCode 解題實作

> **範圍** — BFS 的解題實作庫：網格、狀態空間、樹的改寫與剝葉子這幾類題目，每題一個標準解，另外附上 LC 994 關於「時間該在哪裡加一」的完整討論 —— 這裡不教任何新模板。
> **另見**：[bfs.md](./bfs.md) — 這裡每個範例所實例化的標準模板，也是所有 `Pattern N` 引用的去處；[bfs_advanced.md](./bfs_advanced.md) — 比較少見的 BFS 變形（雙向、0-1 雙端佇列、路線層級、列舉所有最短路徑）。

## LeetCode 題目清單

- [Breadth-First Search](https://leetcode.com/problem-list/breadth-first-search/)
- [Graph Theory](https://leetcode.com/problem-list/graph/)

## 總覽

這是從 [bfs.md](./bfs.md) 拆出來的範例庫。章節編號（`§2-6` … `§2-18`）是主表和 [bfs_advanced.md](./bfs_advanced.md) 引用時用的編號，所以刻意維持不變、不重新編號 —— 中間有缺號代表那個範例已經升格成主表裡的模板。

| 分組 | 題目 |
|---|---|
| 層序走訪的變形 | LC 662, LC 958 |
| 層數計算與計時 | LC 994 |
| 狀態空間 BFS | LC 752, LC 773, LC 909 |
| 網格 BFS | LC 130, LC 286, LC 417 |
| 圖 BFS | LC 207, LC 279, LC 310 |
| 樹 BFS | LC 742, LC 116 / 117, LC 623, LC 863 |

## 層序走訪的變形

> 兩者都是 [bfs.md](./bfs.md) 裡 **Pattern 2**（逐層 BFS）的延伸。

### 變形 A：讓每個節點帶著**堆積索引** —— LC 662（Maximum Width of Binary Tree）

> **變化點**：佇列裡放的是 `(node, index)`，索引為 `i` 的節點，子節點是 `2i` / `2i+1`。一層的寬度 = `lastIndex - firstIndex + 1`，這樣就把中間 `null` 的空隙一起算進去，卻不用真的存它們。每一層都要減掉該層第一個索引做**正規化**，否則深度約 60 的歪斜樹會讓索引把 `int` 撐爆。

```java
// java
// LC 662 - Maximum Width of Binary Tree
// time = O(N), space = O(W)   W = max level width
// IDEA: level BFS carrying a heap index; width = last - first + 1 (nulls counted implicitly)
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

```python
# python
# LC 662 - Maximum Width of Binary Tree
# time = O(N), space = O(W)
# IDEA: queue holds (node, index); re-base index per level to keep numbers small
def widthOfBinaryTree(root):
    if not root:
        return 0
    ans = 0
    q = deque([(root, 0)])
    while q:
        first = q[0][1]
        last = first
        for _ in range(len(q)):
            node, idx = q.popleft()
            idx -= first                    # normalize against this level's start
            last = idx
            if node.left:
                q.append((node.left, 2 * idx))
            if node.right:
                q.append((node.right, 2 * idx + 1))
        ans = max(ans, last + 1)
    return ans
```

### 變形 C：連 `null` 子節點也放進佇列 —— LC 958（Check Completeness of a Binary Tree）

> **變化點**：把 `null` 也 push 進去，佇列就成了這棵樹如假包換的陣列表示法。完全二元樹的 `null` 全部集中在尾端，所以規則是：一旦 pop 到 `null`，後面就不可以再出現非 `null`。這也正是層序**序列化**的形狀（LC 297 / LC 449 寫出 `null` 標記就是為了這個原因）。

```python
# python
# LC 958 - Check Completeness of a Binary Tree
# time = O(N), space = O(W)
# IDEA: push nulls; after the first null pops, any real node means "not complete"
def isCompleteTree(root):
    q = deque([root])
    seen_null = False
    while q:
        node = q.popleft()
        if node is None:
            seen_null = True
        else:
            if seen_null:
                return False        # a real node after a gap -> not complete
            q.append(node.left)     # push children unconditionally, nulls included
            q.append(node.right)
    return True
```

## 層數計算與計時

### 時間／距離該在哪裡加一：BFS 這一層的開頭還是結尾

逐層 BFS 常見的 bug 來源就是**時間／距離的遞增放在哪裡**。兩種寫法都對，但取捨不同。

#### 兩種寫法

**寫法 A：在這一層的開頭遞增（處理之前）**
```java
// From LC 994 - RottingOranges.java V0
while (!queue.isEmpty() && freshOrange > 0) {  // NOTE: extra condition!
    int size = queue.size();
    time++;  // Increment FIRST - we're about to process a "minute" level

    for (int i = 0; i < size; i++) {
        int[] cur = queue.poll();
        // process neighbors, infect fresh oranges...
    }
}
return freshOrange == 0 ? time : -1;
```

**寫法 B：在這一層的結尾遞增（而且只在真的做了事情時）**
```java
// From LC 994 - RottingOranges.java V0-0-2, V0-1, V0-4
while (!queue.isEmpty()) {
    int size = queue.size();
    boolean rottedThisMinute = false;

    for (int i = 0; i < size; i++) {
        int[] cur = queue.poll();
        // process neighbors...
        if (/* infected a fresh neighbor */) {
            rottedThisMinute = true;
        }
    }

    if (rottedThisMinute) time++;  // Only count if actual infection happened
}
return freshOrange == 0 ? time : -1;
```

#### 詳細比較

| 面向 | 寫法 A（開頭） | 寫法 B（結尾 + 旗標） |
|--------|------------------------|---------------------------|
| **何時遞增** | 處理這一層之前 | 處理完之後，且只在做了事情時 |
| **要多加 while 條件嗎？** | 要：`freshOrange > 0` | 不用，旗標會處理掉邊界情況 |
| **風險** | 漏了條件就會多算 | 旗標用對就沒有 |
| **程式複雜度** | 迴圈本體比較單純 | 要多維護一個布林旗標 |
| **什麼時候回傳 0？** | 沒有新鮮橘子時自然回 0 | 自然而然：沒做事就不遞增 |

#### 為什麼寫法 A 的 while 條件需要 `freshOrange > 0`

**問題出在哪：** 如果只檢查 `!queue.isEmpty()`，那些已經腐爛、身邊也沒東西可感染的格子也會被處理，時間就多加了。

```text
Scenario: After all oranges are infected

Layer N: Queue = [(2,1)], freshOrange = 1
  - time++ → time = 4
  - Process (2,1): infect (2,2)
  - freshOrange = 0, Queue = [(2,2)]

Layer N+1: Queue = [(2,2)], freshOrange = 0
  - WITHOUT `freshOrange > 0`: time++ → time = 5 (WRONG! over-count)
  - WITH `freshOrange > 0`: Exit loop, return time = 4 (CORRECT!)
```

**關鍵洞見：** 當 `freshOrange == 0`，代表所有橘子**早就**被感染（標記成 2）了。佇列裡可能還留著腐爛的格子，但它們身邊沒有新鮮的鄰居可以感染。處理它們只是白做工，還會多算時間。

```java
// CORRECT: Exit early when nothing left to infect
while (!queue.isEmpty() && freshOrange > 0) {
    time++;
    // ...
}
```

#### 為什麼寫法 B 天生就處理好了邊界情況

```java
while (!queue.isEmpty()) {
    int size = queue.size();
    boolean rottedThisMinute = false;

    for (int i = 0; i < size; i++) {
        // process...
        if (/* infected a neighbor */) {
            rottedThisMinute = true;
        }
    }

    if (rottedThisMinute) time++;  // Only count if actual infection happened
}
```

**為什麼有效：**
- 就算佇列裡還有東西（先前被感染的格子）
- 只要它們沒有感染到任何**新**格子 → `rottedThisMinute = false`
- 不遞增 → 不會多算

#### 具體例子：LC 994 Rotting Oranges —— 時間該在哪裡加一

```text
Grid: [[2,1,1],    Initial: 6 fresh oranges, 1 rotten at (0,0)
       [1,1,0],
       [0,1,1]]    Expected answer: 4 minutes
```

**寫法 A 的追蹤（在開頭 time++，並帶 `freshOrange > 0`）：**

```text
Initial: Queue=[(0,0)], fresh=6, time=0

Check: queue not empty && fresh>0 → TRUE
  time++ → time=1
  Process (0,0): infect (0,1), (1,0)
  fresh=4, Queue=[(0,1),(1,0)]

Check: queue not empty && fresh>0 → TRUE
  time++ → time=2
  Process (0,1): infect (0,2), (1,1)
  Process (1,0): nothing new
  fresh=2, Queue=[(0,2),(1,1)]

Check: queue not empty && fresh>0 → TRUE
  time++ → time=3
  Process (0,2): nothing (neighbor (1,2)=0)
  Process (1,1): infect (2,1)
  fresh=1, Queue=[(2,1)]

Check: queue not empty && fresh>0 → TRUE
  time++ → time=4
  Process (2,1): infect (2,2)
  fresh=0, Queue=[(2,2)]

Check: queue not empty && fresh>0 → FALSE (fresh=0)
  EXIT LOOP
  Return fresh==0 ? time : -1 → time=4 ✓ CORRECT!
```

**如果把 while 條件裡的 `freshOrange > 0` 拿掉會怎樣？**

```text
...continuing from above...

Check: queue not empty → TRUE (Queue=[(2,2)])
  time++ → time=5  ← WRONG! Over-counting
  Process (2,2): no fresh neighbors
  Queue=[]

Return time=5 ✗ WRONG!
```

#### 決策指南：該用哪一種寫法？

**以下情況用寫法 A（開頭 time++）：**
- ✅ 你有一個明確的「完成」條件（例如 `freshOrange == 0`）
- ✅ 你想要迴圈本體單純一點，不想多維護旗標
- ✅ 題目語意是：「時間先過，然後感染才擴散」
- ⚠️ while 迴圈裡**一定**要加上完成條件！

**以下情況用寫法 B（結尾 time++ + 旗標）：**
- ✅ 沒有明確的完成條件可用
- ✅ 想確保絕對不會多算
- ✅ 題目語意是：「感染先擴散，然後時間才過」
- ✅ 有多種不同的「做了事情」需要追蹤

#### Rotting Oranges 解法的常見寫法

| 版本 | 策略 | 關鍵程式碼 |
|---------|----------|----------|
| V0, V0-0-1 | 開頭 time++ | `while (!q.isEmpty() && freshOrange > 0) { time++; ... }` |
| V0-0-2, V0-1, V0-4 | 結尾 time++ + 旗標 | `if (rottedThisMinute) time++;` |
| V1-1 | 結尾 time++（不用旗標） | `while (fresh > 0 && !q.isEmpty()) { ... } time++;` |

#### 小結 —— 時間加在一層的開頭還是結尾

| 情境 | 建議寫法 |
|----------|---------------------|
| 有完成計數器（新鮮橘子、收集到的鑰匙） | 寫法 A，把計數器放進 while 條件 |
| 沒有完成計數器 | 寫法 B，用布林旗標 |
| 想要最不容易寫錯的程式碼 | 寫法 B（比較難搞砸） |
| 想要效率最好的程式碼 | 寫法 A（沒有旗標的額外負擔） |

> **經驗法則：** 如果你把 `time++` 放在**開頭**，while 迴圈裡就**必須**有提早離開的條件。否則就把 `time++` 放在**結尾**並搭配旗標。

---

## LC 範例

### 2-6) Open the Lock（LC 752）—— 在狀態空間上做 BFS
> 把每一種鎖的組合當成一個節點；用 BFS 找出轉到目標所需的最少次數。

```java
// LC 752 - Open the Lock
// IDEA: BFS on 4-digit combinations; each turn = 1 step
// time = O(10^4 * 4 * 2), space = O(10^4)
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
                    chars[j] = (char)((orig - '0' + delta + 10) % 10 + '0');
                    String next = new String(chars);
                    if (!visited.contains(next) && !dead.contains(next)) {
                        visited.add(next); queue.offer(next);
                    }
                    chars[j] = orig;
                }
            }
        }
        steps++;
    }
    return -1;
}
```

#### 變形：Sliding Puzzle（LC 773）—— 同樣的狀態空間 BFS，只是把盤面壓平成字串

> **變化點**：骨架跟 LC 752 一模一樣 —— 只有*狀態編碼*和*鄰居規則*不同。把 2×3 的盤面序列化成 `"123450"`，並預先算好空格（`'0'`）可以跟哪些索引交換，這樣「產生鄰居」就變成查表，而不是二維邊界計算。目標是 `"123450"`；到不了就回傳 `-1`（6! = 720 種排列裡只有一半可達）。

```text
index layout      swap table (neighbors of each index)
 0 1 2            0:[1,3]  1:[0,2,4]  2:[1,5]
 3 4 5            3:[0,4]  4:[1,3,5]  5:[2,4]
```

```java
// java
// LC 773 - Sliding Puzzle
// time = O(6! * 6), space = O(6!)   at most 720 board states
// IDEA: state = flattened board string; BFS levels = number of moves
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
    return -1;                                     // target permutation unreachable
}
```

```python
# python
# LC 773 - Sliding Puzzle
# time = O(6! * 6), space = O(6!)
# IDEA: BFS on the flattened board string, blank '0' swaps with its table neighbors
def slidingPuzzle(board):
    start = "".join(str(x) for row in board for x in row)
    target = "123450"
    nbr = [[1,3], [0,2,4], [1,5], [0,4], [1,3,5], [2,4]]
    q = deque([start])
    seen = {start}
    steps = 0
    while q:
        for _ in range(len(q)):
            cur = q.popleft()
            if cur == target:
                return steps
            i = cur.index('0')
            for j in nbr[i]:
                lst = list(cur)
                lst[i], lst[j] = lst[j], lst[i]
                nxt = "".join(lst)
                if nxt not in seen:
                    seen.add(nxt)
                    q.append(nxt)
        steps += 1
    return -1
```

**帶走的重點**：LC 752、LC 773 和 LC 433 是同一個模板 —— *把狀態雜湊起來、定義一個 `neighbors(state)` 函式、數 BFS 的層數*。面試的價值在於看出「一個拼圖／單字／密碼鎖其實是一張隱含的圖」。

### 2-7) Surrounded Regions（LC 130）—— 從邊界開始 BFS
> 從所有邊界上的 'O' 開始 BFS；把碰得到的標成安全，其餘的翻掉。

```java
// LC 130 - Surrounded Regions
// IDEA: BFS from border O-cells to find non-surrounded regions
// time = O(M*N), space = O(M*N)
public void solve(char[][] board) {
    int m = board.length, n = board[0].length;
    Queue<int[]> queue = new LinkedList<>();
    for (int i = 0; i < m; i++)
        for (int j = 0; j < n; j++)
            if ((i==0||i==m-1||j==0||j==n-1) && board[i][j]=='O') {
                board[i][j] = 'S'; queue.offer(new int[]{i,j});
            }
    int[][] dirs = {{1,0},{-1,0},{0,1},{0,-1}};
    while (!queue.isEmpty()) {
        int[] c = queue.poll();
        for (int[] d : dirs) {
            int nr=c[0]+d[0], nc=c[1]+d[1];
            if (nr>=0&&nr<m&&nc>=0&&nc<n&&board[nr][nc]=='O') {
                board[nr][nc]='S'; queue.offer(new int[]{nr,nc});
            }
        }
    }
    for (int i=0;i<m;i++) for (int j=0;j<n;j++)
        board[i][j] = board[i][j]=='S' ? 'O' : (board[i][j]=='O' ? 'X' : board[i][j]);
}
```

### 2-8) Course Schedule（LC 207）—— BFS 拓撲排序（Kahn）
> 建入度陣列；BFS 迭代地處理入度為零的節點。

```java
// LC 207 - Course Schedule
// IDEA: Kahn's BFS topological sort — detect cycle in directed graph
// time = O(V+E), space = O(V+E)
public boolean canFinish(int numCourses, int[][] prerequisites) {
    int[] inDegree = new int[numCourses];
    List<List<Integer>> adj = new ArrayList<>();
    for (int i = 0; i < numCourses; i++) adj.add(new ArrayList<>());
    for (int[] pre : prerequisites) {
        adj.get(pre[1]).add(pre[0]);
        inDegree[pre[0]]++;
    }
    Queue<Integer> queue = new LinkedList<>();
    for (int i = 0; i < numCourses; i++) if (inDegree[i] == 0) queue.offer(i);
    int processed = 0;
    while (!queue.isEmpty()) {
        int course = queue.poll();
        processed++;
        for (int next : adj.get(course))
            if (--inDegree[next] == 0) queue.offer(next);
    }
    return processed == numCourses;
}
```

### 2-9) Walls and Gates（LC 286）—— 多源 BFS
> 從所有的門（0）同時開始 BFS；用最短距離填滿每個房間。

```java
// LC 286 - Walls and Gates
// IDEA: Multi-source BFS from all gates — propagate distances
// time = O(M*N), space = O(M*N)
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
            int nr = cell[0]+d[0], nc = cell[1]+d[1];
            if (nr>=0&&nr<m&&nc>=0&&nc<n&&rooms[nr][nc]==INF) {
                rooms[nr][nc] = rooms[cell[0]][cell[1]] + 1;
                queue.offer(new int[]{nr, nc});
            }
        }
    }
}
```

### 2-10) Minimum Height Trees（LC 310）—— BFS 剝葉子
> 反覆移除葉節點；最後剩下的 1～2 個節點就是 MHT 的根。

**核心想法 —— BFS／逐層剝除（剝洋蔥）：**
- 把樹想成一顆**洋蔥**。MHT 的根在最內層
- 這是**從葉子往內的多源 BFS** —— 不是從單一根節點出發的 BFS
- 葉子 = 度數為 1 的節點。同時移除所有葉子 → 它們的鄰居可能變成新的葉子
- 重複到剩下 ≤ 2 個節點為止。這些就是**重心**（MHT 的根）
- 為什麼是 ≤ 2？一棵樹最多有 2 個重心（直徑為偶數 → 2 個，奇數 → 1 個）

```text
Example: 0 - 1 - 2 - 3 - 4

Layer 1: remove 0, 4  (leaves)
Layer 2: remove 1, 3  (new leaves)
Result:  [2] ✅        (centroid)
```

**為什麼不用暴力解？**
- 從每個節點各跑一次 BFS 算高度 → O(N²) → TLE
- 剝葉子 → O(N) —— 每個節點和每條邊只處理一次

**模式 —— 什麼時候該想到它：**

| 訊號 | 含意 |
|--------|---------|
| 無向樹 + 找最佳的根 | 剝葉子 |
| 最小化「到任一葉子的最大距離」 | 找重心 |
| 「由外往內一層一層剝」 | 多源 BFS |
| 在樹上依度數處理 | 跟 DAG 上的 Kahn 演算法類似 |

**兩種實作風格：**

風格 1 —— `int[] degree` 陣列（比較單純，建議用這個）：
```java
// LC 310 - Minimum Height Trees
// IDEA: BFS leaf trimming with degree array
// time = O(N), space = O(N)
public List<Integer> findMinHeightTrees(int n, int[][] edges) {
    if (n == 1) return Collections.singletonList(0);

    List<List<Integer>> graph = new ArrayList<>();
    for (int i = 0; i < n; i++) graph.add(new ArrayList<>());
    int[] degree = new int[n];

    for (int[] e : edges) {
        graph.get(e[0]).add(e[1]);
        graph.get(e[1]).add(e[0]);
        degree[e[0]]++;
        degree[e[1]]++;
    }

    Queue<Integer> leaves = new LinkedList<>();
    for (int i = 0; i < n; i++)
        if (degree[i] == 1) leaves.offer(i);

    int remaining = n;
    while (remaining > 2) {
        int size = leaves.size();
        remaining -= size;
        for (int i = 0; i < size; i++) {
            int leaf = leaves.poll();
            for (int nei : graph.get(leaf)) {
                degree[nei]--;
                if (degree[nei] == 1) leaves.offer(nei);
            }
        }
    }
    return new ArrayList<>(leaves);
}
```

風格 2 —— `Set<Integer>` 鄰接表（O(1) 移除，真的維護邊）：
```java
// LC 310 - Using Set for adjacency
// time = O(N), space = O(N)
public List<Integer> findMinHeightTrees_set(int n, int[][] edges) {
    if (n == 1) return Collections.singletonList(0);
    List<Set<Integer>> adj = new ArrayList<>();
    for (int i = 0; i < n; i++) adj.add(new HashSet<>());
    for (int[] e : edges) { adj.get(e[0]).add(e[1]); adj.get(e[1]).add(e[0]); }
    Queue<Integer> leaves = new LinkedList<>();
    for (int i = 0; i < n; i++) if (adj.get(i).size() == 1) leaves.offer(i);
    int remaining = n;
    while (remaining > 2) {
        int size = leaves.size();
        remaining -= size;
        for (int i = 0; i < size; i++) {
            int leaf = leaves.poll();
            int neighbor = adj.get(leaf).iterator().next();
            adj.get(neighbor).remove(leaf);
            if (adj.get(neighbor).size() == 1) leaves.offer(neighbor);
        }
    }
    return new ArrayList<>(leaves);
}
```

**經典的相似題：**

| LC # | 題目 | 關聯 |
|------|---------|------------|
| 310 | Minimum Height Trees | 剝葉子的核心題 |
| 207 | Course Schedule | Kahn 演算法 —— DAG 上同樣的 BFS + 度數模式 |
| 210 | Course Schedule II | Kahn 演算法，還要輸出順序 |
| 834 | Sum of Distances in Tree | 樹重心／換根 DP |
| 1245 | Tree Diameter | 直徑 → 重心在中點 |
| 2603 | Collect Coins in a Tree | 用剝葉子剪掉不必要的節點 |
| 863 | All Nodes Distance K in Binary Tree | 在樹的結構上做 BFS |
| 994 | Rotting Oranges | 多源 BFS（同樣的逐層模式） |
| 542 | 01 Matrix | 從所有 0 出發的多源 BFS |

### 2-11) Snakes and Ladders（LC 909）—— 在棋盤上做 BFS
> 把棋盤建模成圖；BFS 找出到終點格所需的最少擲骰次數。

```java
// LC 909 - Snakes and Ladders
// IDEA: BFS — each square is a node, dice roll = edges
// time = O(N^2), space = O(N^2)
public int snakesAndLadders(int[][] board) {
    int n = board.length;
    int[] flat = new int[n * n + 1];
    int idx = 1; boolean leftToRight = true;
    for (int r = n-1; r >= 0; r--) {
        if (leftToRight) for (int c = 0; c < n; c++) flat[idx++] = board[r][c];
        else for (int c = n-1; c >= 0; c--) flat[idx++] = board[r][c];
        leftToRight = !leftToRight;
    }
    boolean[] visited = new boolean[n*n+1];
    Queue<int[]> queue = new LinkedList<>();
    queue.offer(new int[]{1, 0});
    visited[1] = true;
    while (!queue.isEmpty()) {
        int[] curr = queue.poll();
        int pos = curr[0], steps = curr[1];
        for (int dice = 1; dice <= 6 && pos+dice <= n*n; dice++) {
            int next = pos + dice;
            if (flat[next] != -1) next = flat[next];
            if (next == n*n) return steps + 1;
            if (!visited[next]) { visited[next] = true; queue.offer(new int[]{next, steps+1}); }
        }
    }
    return -1;
}
```

### 2-13) Pacific Atlantic Water Flow（LC 417）—— 從兩個大洋各做一次 BFS
> 從太平洋和大西洋的邊界反向 BFS；同時落在兩個集合裡的格子就能流向兩邊。

```java
// LC 417 - Pacific Atlantic Water Flow
// IDEA: BFS from Pacific border + Atlantic border; intersection = answer
// time = O(M*N), space = O(M*N)
public List<List<Integer>> pacificAtlantic(int[][] heights) {
    int m = heights.length, n = heights[0].length;
    boolean[][] pac = new boolean[m][n], atl = new boolean[m][n];
    Queue<int[]> pq = new LinkedList<>(), aq = new LinkedList<>();
    for (int i = 0; i < m; i++) {
        pq.offer(new int[]{i,0}); pac[i][0]=true;
        aq.offer(new int[]{i,n-1}); atl[i][n-1]=true;
    }
    for (int j = 0; j < n; j++) {
        pq.offer(new int[]{0,j}); pac[0][j]=true;
        aq.offer(new int[]{m-1,j}); atl[m-1][j]=true;
    }
    bfs(heights, pq, pac, m, n);
    bfs(heights, aq, atl, m, n);
    List<List<Integer>> res = new ArrayList<>();
    for (int i=0;i<m;i++) for (int j=0;j<n;j++)
        if (pac[i][j]&&atl[i][j]) res.add(Arrays.asList(i,j));
    return res;
}
private void bfs(int[][] h, Queue<int[]> q, boolean[][] visited, int m, int n) {
    int[][] dirs={{1,0},{-1,0},{0,1},{0,-1}};
    while (!q.isEmpty()) {
        int[] c=q.poll();
        for (int[] d:dirs) {
            int nr=c[0]+d[0],nc=c[1]+d[1];
            if (nr>=0&&nr<m&&nc>=0&&nc<n&&!visited[nr][nc]&&h[nr][nc]>=h[c[0]][c[1]]) {
                visited[nr][nc]=true; q.offer(new int[]{nr,nc});
            }
        }
    }
}
```

### 2-14) Perfect Squares（LC 279）—— 在抽象圖上做 BFS（數字分解）
> 從 `n` 往 `0` 做 BFS；每一層減掉一個完全平方數。第一次抵達 0 就是最少個數。

```java
// LC 279 - Perfect Squares
// IDEA: BFS — treat each number as a node, edges = subtracting a perfect square
// time = O(N * sqrt(N)), space = O(N)
public int numSquares(int n) {
    // Pre-calculate perfect squares up to n
    List<Integer> squares = new ArrayList<>();
    for (int i = 1; i * i <= n; i++) {
        squares.add(i * i);
    }

    Queue<Integer> queue = new LinkedList<>();
    Set<Integer> visited = new HashSet<>();

    queue.offer(n);
    visited.add(n);

    int level = 0;

    while (!queue.isEmpty()) {
        level++;
        int size = queue.size();

        for (int i = 0; i < size; i++) {
            int remaining = queue.poll();

            for (int square : squares) {
                int nextVal = remaining - square;

                if (nextVal == 0)
                    return level; // Found shortest path
                if (nextVal < 0)
                    break; // Squares are sorted, so we can stop

                if (!visited.contains(nextVal)) {
                    visited.add(nextVal);
                    queue.offer(nextVal);
                }
            }
        }
    }
    return -1;
}
```

### 2-15) Closest Leaf in a Binary Tree（LC 742）—— 樹 → 圖 + BFS ⭐⭐⭐⭐
> 「最近」指的是二元樹上**邊**最少。麻煩的地方在於：從目標出發，除了往下走（到子節點），可能還得**往上走**（到父節點）。單純的樹只有指向子節點的指標，所以要先**把樹轉成無向圖**（每個節點 ↔ 它的父節點和子節點），再從目標跑一次普通的 BFS —— **第一個被 pop 出來的葉子就是答案**（無權圖上的 BFS 給的就是最少邊數）。

**1）核心想法**

- **用 DFS 建出無向圖** + 記下 `target` 節點 + 收集 `leaves`。
  - 對每個節點，*雙向*加邊：`graph[node]→parent` 和 `graph[parent]→node`。
  - 這是關鍵那一步 —— 它讓父節點變成可達的，於是「往上走」就成了普通的一條邊。
- **從目標節點做 BFS**；第一個被 pop 出來、且沒有子節點的節點就是最近的葉子。
  - 邊權重都相同 ⇒ BFS 保證邊數最少；不需要另外記距離。

```python
# python — LC 742 (DFS build graph + BFS from target)
from collections import defaultdict, deque

class Solution(object):
    def findClosestLeaf(self, root, k):
        graph = defaultdict(list)   # node -> [neighbors]  (undirected)
        leaves = set()
        target = [None]

        def build(node, parent):
            if not node:
                return
            if node.val == k:
                target[0] = node
            if parent:                          # connect BOTH directions
                graph[node].append(parent)
                graph[parent].append(node)
            if not node.left and not node.right: # leaf = no children
                leaves.add(node)
            build(node.left, node)
            build(node.right, node)

        build(root, None)

        # BFS from target; first leaf reached is the closest
        q = deque([target[0]])
        visited = {target[0]}
        while q:
            node = q.popleft()
            if node in leaves:
                return node.val                  # earliest pop = fewest edges
            for nei in graph[node]:
                if nei not in visited:
                    visited.add(nei)
                    q.append(nei)
```

**2）為什麼用 BFS（而不是 DFS）？**

| | |
|---|---|
| 目標 | target → 任一葉子的**最少邊數** |
| 邊權重 | 全部相同（1）⇒ BFS 的層數就是精確距離 |
| 為什麼要轉成圖而不是留在樹 | 答案的葉子可能在目標*上方* → 需要指向父節點的邊 |
| 為什麼「第一個葉子就贏」 | BFS pop 出節點的順序，距離是非遞減的 |

```text
Tree (k=2):                 As undirected graph, BFS from 2:
       1                    dist 0: 2
      / \                   dist 1: 4, 1
     2   3   (leaf)         dist 2: 5, 3(leaf) <- returned (3 closer than the 5→6 chain)
    /
   4
  /
 5
/
6 (leaf)
```

**3）相似的 LC**

| LC | 題目 | 關聯 |
|----|---------|----------|
| 742 | Closest Leaf in a Binary Tree | 就是本題 —— 樹→圖，BFS 找最近的葉子 |
| 863 | All Nodes Distance K in Binary Tree | 同樣的樹→圖手法，往外 BFS K 層 —— Pattern 11／§2-18 |
| 1192 | Critical Connections | 把樹／圖視為無向，走訪邊 |
| 994 | Rotting Oranges | 多源 BFS，「第一次抵達 = 最短距離」的想法 |
| 542 | 01 Matrix | 無權網格上的 BFS 最短距離 |

> **模式重點**：只要一道*樹*的題目需要**往上（朝父節點）**移動，就把它轉成**無向圖**（用 DFS 補上指向父節點的邊），改用圖的 BFS／DFS。這個「樹 → 圖」的重新框架，就是 LC 742 和 LC 863 的關鍵。

### 2-16) Populating Next Right Pointers（LC 116 / 117）—— 用層序 BFS 接好 `next` 指標 ⭐⭐⭐⭐

> 每個節點都有一個 `next` 指標，要指向**同一層中緊鄰它右邊**的節點（最右邊的節點則指 `NULL`）。這就只是一個**層序 BFS**：處理某一層時，把每個節點接到它之後出隊的那個節點。追問（「額外空間 O(1)」）的做法是把剛接好的 `next` 指標當成**上一層的鏈結串列**，用它來接好下一層 —— 不需要佇列。

**1）核心想法**

- **`next` = 「同一層中我右邊的那個節點」。** 所以要**逐層**處理這棵樹，而且在每一層內部，隨著節點從佇列 pop 出來就接上 `prev.next = cur`。
- **每一層的最後一個節點**的 `next` 是 `None`（每層都會把佇列清空，所以它絕不會指到下一層去）。
- LC 116（完美二元樹）和 LC 117（任意二元樹）都適用 —— BFS 不在乎樹的形狀，子節點存在就放進佇列。
- **O(1) 空間的追問**：第 *L* 層完全接好之後，就把它當成鏈結串列沿著 `next` 走，順手設定第 *L+1* 層的 `next` —— 回收利用你已經建好的結構，取代佇列。

**2）模式**

```python
# python — LC 116/117: BFS by layer, chain nodes via prev pointer
from collections import deque

class Solution(object):
    def connect(self, root):
        # time = O(N), space = O(W)  (W = max width / one level)
        if not root:
            return None

        q = deque([root])
        while q:
            size = len(q)
            prev = None
            for _ in range(size):          # one full level per outer iteration
                cur = q.popleft()
                if prev:                    # link previous node -> current
                    prev.next = cur
                prev = cur
                if cur.left:
                    q.append(cur.left)
                if cur.right:
                    q.append(cur.right)
            prev.next = None                # last node of the level -> NULL
        return root
```

**另一種寫法（改成偷看佇列最前端，不維護 `prev`）：**

```python
# python — same BFS, use i < size - 1 to point at the next node still in queue
for i in range(size):
    cur = q.popleft()
    if i < size - 1:
        cur.next = q[0]                     # front of queue = node to the right
    if cur.left:  q.append(cur.left)
    if cur.right: q.append(cur.right)
```

**追問 —— O(1) 空間（完美二元樹，LC 116）：** 重複利用 `next` 指標，不用佇列。

```python
# python — walk each level as a linked list to wire the next level
class Solution(object):
    def connect(self, root):
        # time = O(N), space = O(1)
        if not root:
            return None
        leftmost = root
        while leftmost.left:               # stop once we reach the leaf level
            head = leftmost
            while head:
                head.left.next = head.right             # (1) same parent
                if head.next:
                    head.right.next = head.next.left    # (2) across parents
                head = head.next                        # move right via existing links
            leftmost = leftmost.left        # drop to next level's leftmost
        return root
```

```text
Visual (LC 116):
        1 -> NULL
      /   \
     2  -> 3 -> NULL
    / \   / \
   4-> 5->6->7 -> NULL

BFS level 2: prev walks 4→5→6→7, chaining next; last (7) -> NULL.
O(1) trick: from level [2,3], (1) 2.left→2.right = 4→5, (2) 2.right→2.next.left = 5→6, ...
```

**3）相似的 LC**

| LC | 題目 | 關聯 |
|----|---------|----------|
| 116 | Populating Next Right Pointers in Each Node | 就是本題 —— **完美**二元樹，BFS 或 O(1) 的 `next` 重用 |
| 117 | Populating Next Right Pointers II | 同樣的 BFS；樹**不是**完美的，所以 O(1) 版本每層需要一個 dummy head |
| 102 | Binary Tree Level Order Traversal | 這一切建立在其上的基本層序 BFS |
| 199 | Binary Tree Right Side View | 每層最右邊的節點 = `next = None` 之前的最後一個節點 |
| 314 | Binary Tree Vertical Order Traversal | 同樣是層序 BFS 分組，但鍵是欄位而不是 `next` |

> **模式重點**：「指向我右邊的節點」⇒ **層序 BFS**，依出隊順序把節點串起來，每層結尾用 `next = None` 收尾。要 O(1) 空間，就把已經串好的那一層當成鏈結串列，用它來建下一層。

### 2-17) Add One Row to Tree（LC 623）—— 層序 BFS 停在 `depth - 1` 再重接指標 ⭐⭐⭐⭐

> 在 `depth` 這一層插入一整排值為 `val` 的節點。訣竅是：你動手的地方不是 `depth`，而是 **`depth - 1`** —— 那是*父節點*那一排，指標要重接的是它們。所以就跑一個普通的層序 BFS，一邊數層數，**一數到 `cur_depth == depth - 1` 就停**；對那一層的每個節點插入兩個新節點，並**把原本的子樹重新接回去**（`old_left` 掛在 `new_left.left`、`old_right` 掛在 `new_right.right`）。

**1）核心想法**

- **BFS 很自然合用**，因為這個操作是*以層為單位*定義的 —— 逐層 BFS（`size = len(q)`）給的正是這個。不需要父指標，也不用擔心遞迴深度。
- **先處理邊界情況：`depth == 1`。** 沒有第 0 層可以重接，所以要建一個新的根，把整棵原樹掛成它的**左**子樹，然後回傳新的根。
- **覆寫前先暫存。** `node.left = TreeNode(val)` 會毀掉原本的指標，所以要*先*把 `old_left`／`old_right` 存起來。這一行漏了，整個解就壞了。
- **不對稱地重接**：原本的左子樹接到 `new_left.left`，原本的右子樹接到 `new_right.right` —— 都是外側 —— 這樣樹才能保持原來的左右形狀。
- 重接完那一層之後要**立刻 return／break**。繼續 BFS 會走進剛建出來的新節點，而且（更糟的是）佇列已經不再對應插入前的那棵樹。
- 子節點是 `None` 的節點一樣要長出**兩個**新的子節點（新節點自己的子節點是 `None`）—— 規則適用於 `depth - 1` 上每個非空的節點，不是只有原本有子節點的那些。

**2）模式**

```python
# python — LC 623 Add One Row to Tree (level BFS, stop at depth-1)
# time = O(N), space = O(W)   N = #nodes, W = max level width
from collections import deque

class Solution(object):
    def addOneRow(self, root, val, depth):
        # (1) no `depth - 1` row exists -> new node becomes the new root
        if depth == 1:
            new_root = TreeNode(val)
            new_root.left = root
            return new_root

        q = deque([root])
        cur_depth = 1                       # root is at depth 1 (NOT 0)

        while q:
            size = len(q)

            # NOTE !!! treat `cur_depth == depth - 1` as a SEPARATE path:
            #          inside it we rewire instead of descending, then stop
            if cur_depth == depth - 1:
                for _ in range(size):
                    node = q.popleft()

                    old_left = node.left     # (2) cache BEFORE overwriting
                    old_right = node.right

                    node.left = TreeNode(val)   # (3) splice the new row in
                    node.right = TreeNode(val)

                    node.left.left = old_left   # (4) reattach on OUTER sides
                    node.right.right = old_right
                break                        # (5) done — never descend further

            # otherwise: ordinary level-order descent
            for _ in range(size):
                node = q.popleft()
                if node.left:
                    q.append(node.left)
                if node.right:
                    q.append(node.right)

            cur_depth += 1

        return root
```

**變化版 —— 單一迴圈，裡面用 `if/else`**（邏輯相同，這一層只掃一趟）：

```python
# python — branch per node instead of per level; break after the level finishes
while q:
    size = len(q)
    for _ in range(size):
        node = q.popleft()
        if cur_depth == depth - 1:
            old_left, old_right = node.left, node.right
            node.left, node.right = TreeNode(val), TreeNode(val)
            node.left.left = old_left
            node.right.right = old_right
        else:
            if node.left:  q.append(node.left)
            if node.right: q.append(node.right)

    if cur_depth == depth - 1:   # break AFTER the whole level is rewired
        break
    cur_depth += 1
```

**另一種寫法 —— DFS 遞迴**（比較短，但要 O(h) 的堆疊 —— 完整討論在 [dfs.md §2-31](./dfs.md)）：

```python
# python — recurse down to d == 2, then rewire that node's children
# time = O(N), space = O(h)
class Solution(object):
    def addOneRow(self, root, v, d):
        if not root:
            return None
        if d == 1:                                   # new root
            new_root = TreeNode(v)
            new_root.left = root
            return new_root
        if d == 2:                                   # root IS the depth-1 parent
            root.left,  root.left.left   = TreeNode(v), root.left
            root.right, root.right.right = TreeNode(v), root.right
            return root
        root.left  = self.addOneRow(root.left,  v, d - 1)
        root.right = self.addOneRow(root.right, v, d - 1)
        return root
```

```text
Visual — root = [4,2,6,3,1,5], val = 1, depth = 2   (rewire level depth-1 = 1, i.e. node 4)

before                    cache 4's children       after (new row of 1s)
      4                   old_left  = 2                  4
     / \                  old_right = 6                 / \
    2   6                                              1   1
   / \   \                4.left  = new 1              /     \
  3   1   5               4.right = new 1             2       6
                          1.left  = 2  (outer)       / \       \
                          1.right = 6  (outer)      3   1       5

depth == 1 case: brand-new node becomes root, whole old tree hangs on its LEFT.
```

**常見陷阱**

| 陷阱 | 為什麼會壞 |
|---|---|
| 停在 `cur_depth == depth` | 太晚了 —— 要重接的指標在**父節點**那一排 |
| 沒暫存就覆寫 `node.left` | 原本的子樹永遠遺失（再也走不到） |
| 寫成 `new_left.right = old_left`（內側） | 樹被鏡射了；必須是 `new_left.left`／`new_right.right` |
| 忘了 `depth == 1` | `depth - 1 == 0` 永遠不會碰到，於是什麼都沒插入 |
| 重接完沒有 break | BFS 會往下走進剛建出來的 `val` 節點 |
| `cur_depth` 從 0 開始 | 差一錯誤 —— 題目定義**根是第 1 層** |

**3）相似的 LC**

| LC | 題目 | 關聯 |
|----|---------|----------|
| 623 | Add One Row to Tree | 就是本題 —— 層序 BFS 走到 `depth - 1`，再重接指標 |
| 102 | Binary Tree Level Order Traversal | 基本的層序 BFS 迴圈（`size = len(q)`） |
| 199 | Binary Tree Right Side View | 同樣的逐層迴圈，取每層最後一個節點 |
| 116 / 117 | Populating Next Right Pointers | §2-16 —— 同樣會**改寫指標**的層序 BFS |
| 971 | Flip Binary Tree To Match Preorder | 走訪的同時交換左右子節點 |
| 226 | Invert Binary Tree | 先暫存再交換子指標（同樣的別名危險） |

> **模式重點**：當一道樹的題目說「在深度 `d` 做 X」，你實際上要**改動的是 `d - 1` 那一排** —— 在那裡做層序 BFS，賦值前先暫存舊的子節點，再把它們接到**外側**，然後立刻停手，這樣才不會走進自己剛造出來的新節點。

### 2-18) All Nodes Distance K in Binary Tree（LC 863）—— 父節點對照表 + 往外擴散的 BFS ⭐⭐⭐⭐⭐

> 距離是從 **`target`** 算起，不是從根算起，所以答案可能在目標的**下方**、**上方**，或**兄弟子樹**裡。先用一次 DFS 記下 `{node: parent}`，補上缺的那條「往上」的邊，然後從 `target` 做 BFS，此時每個節點有 **3 個鄰居：`left`、`right`、`parent`**。每條邊成本都是 1 ⇒ BFS 的層數就是樹上的距離。
> 完整說明：[bfs.md](./bfs.md) 裡的 **Pattern 11**。

**形狀 B —— 剛好往外擴 `k` 層，此時佇列本身就是答案**

> 形狀 A（在佇列裡帶著 `(node, dist)`）是 [bfs.md](./bfs.md) → **Pattern 11** 的標準模板。

```python
# python — shape B: expand exactly k levels, then the queue IS the answer
# (no distance stored; naturally returns [] when the tree is smaller than k)
# time = O(n), space = O(n)
class Solution(object):
    def distanceK(self, root, target, k):
        parents = {}
        def add_parents(node, parent):
            if not node:
                return
            parents[node] = parent
            add_parents(node.left, node)
            add_parents(node.right, node)
        add_parents(root, None)

        q = collections.deque([target])
        visited = {target}
        for _ in range(k):                     # k full level expansions
            for _ in range(len(q)):            # snapshot the level size FIRST
                node = q.popleft()
                for nxt in (node.left, node.right, parents[node]):
                    if nxt and nxt not in visited:
                        visited.add(nxt)
                        q.append(nxt)
        return [node.val for node in q]        # everything left is exactly k away
```

```java
// java — shape B: k level expansions, remaining queue = answer
// LC 863 - All Nodes Distance K in Binary Tree
// time = O(n), space = O(n)
public List<Integer> distanceK(TreeNode root, TreeNode target, int k) {
    Map<TreeNode, TreeNode> parents = new HashMap<>();
    build(root, null, parents);

    Queue<TreeNode> q = new LinkedList<>();
    q.offer(target);
    Set<TreeNode> visited = new HashSet<>();
    visited.add(target);

    for (int step = 0; step < k; step++) {
        int size = q.size();                    // snapshot: level boundary
        for (int i = 0; i < size; i++) {
            TreeNode node = q.poll();
            for (TreeNode nei : new TreeNode[]{node.left, node.right, parents.get(node)}) {
                if (nei != null && visited.add(nei)) q.offer(nei);
            }
        }
    }
    List<Integer> ans = new ArrayList<>();
    for (TreeNode node : q) ans.add(node.val);  // may be empty → correct
    return ans;
}

private void build(TreeNode node, TreeNode parent, Map<TreeNode, TreeNode> parents) {
    if (node == null) return;
    parents.put(node, parent);
    build(node.left, node, parents);
    build(node.right, node, parents);
}
```

| | 形狀 A `(node, dist)` | 形狀 B 擴展 `k` 層 |
|---|---|---|
| 距離怎麼記 | 存在每一筆佇列資料裡 | 隱含在迴圈計數器裡 |
| 怎麼收答案 | `dist == k` 的時候 | 佇列裡剩下的就是 |
| `k = 0` | 回傳 `[target.val]` ✅ | 迴圈跳過，佇列 = `[target]` ✅ |
| `k >` 樹高 | 自然回 `[]` ✅ | 佇列被清空 → `[]` ✅ |
| 什麼時候比較好用 | 順便需要距離／要提早離開 | 精簡，貼合「層數 = 距離」的直覺 |

**常見陷阱**

| 陷阱 | 為什麼會壞 |
|---|---|
| 沒有 `visited` 集合 | 父邊讓圖變成無向 → `5 → 3 → 5 → 3 …` 無限來回 |
| 在 **pop 時**才標記走訪過，而不是入隊時 | 同一個節點可能經由 2 條路徑入隊 ⇒ 答案出現重複 |
| `dist == k` 之後還繼續擴展 | 白做工；`visited` 寫得不嚴謹的話還會撈到距離 `k+1` 的節點 |
| 用 `node.val` 當對照表／`visited` 的鍵 | 這題沒問題（限制保證值唯一），但**值有重複時就會壞** —— 建議用節點本身的識別 |
| 對沒被 DFS 走過的節點取 `parents[node]` | `KeyError` —— 父對照表要從 `root` 建，不是從 `target` |
| 忘了 `k = 0` | 答案是 `[target.val]`，不是 `[]` |
| 在層迴圈裡重新算 `len(q)`（形狀 B） | 佇列在該層中途變長 → 把距離 `k` 和 `k+1` 混在一起 |

**相似的 LC**

| LC | 題目 | 關聯 |
|----|---------|----------|
| 863 | All Nodes Distance K in Binary Tree | 就是本題 —— 父對照表 + 往外走 `k` 步的 BFS |
| 2385 | Amount of Time for Binary Tree to Be Infected | 設定完全相同；答案 = BFS 的層數（最大距離） |
| 742 | Closest Leaf in a Binary Tree | §2-15 —— 從目標往外 BFS，第一個 pop 出來的葉子獲勝 |
| 1740 | Find Distance in a Binary Tree | 從節點 `p` 開始 BFS，直到 `q` 被 pop 出來 |
| 1530 | Number of Good Leaf Nodes Pairs | Pattern 10 —— 從每個葉子出發、有距離上限的 BFS |
| 993 | Cousins in Binary Tree | 每個節點記父節點 + 深度（不需要往外擴散） |
| 236 | LCA of a Binary Tree | LCA 就是「先上後下」那條路徑轉折的地方 |
| 542 / 994 | 01 Matrix / Rotting Oranges | 網格上同一套「BFS 層數 == 距離」的引擎 |

## 總結

| 題目要是說… | 就拿出 | 這裡的實作 |
|---|---|---|
| 「拼圖或密碼鎖的最少轉動／移動次數」 | 把狀態雜湊起來、`neighbors(state)`、數 BFS 層數 | §2-6（LC 752／LC 773）、§2-11（LC 909） |
| 「**沒有**碰到邊界的區域」 | **從邊界往內** BFS，把碰不到的翻掉 | §2-7（LC 130） |
| 「到每個門／大洋／零的距離」 | 把所有源點一起放進去的多源 BFS | §2-9（LC 286）、§2-13（LC 417） |
| 「所有課程修得完嗎」 | Kahn 的入度 BFS | §2-8（LC 207） |
| 「湊出 n 所需最少的完全平方數／硬幣」 | 在抽象數字圖上做 BFS | §2-14（LC 279） |
| 「讓樹高最小的那個根」 | 往內剝葉子，直到剩下 ≤ 2 個節點 | §2-10（LC 310） |
| 「**從某個節點**算起最近的葉子／距離 k」 | 樹 → 無向圖（補上父邊），再往外 BFS | §2-15（LC 742）、§2-18（LC 863） |
| 「把整層樹接起來／插入／改寫」 | 層序 BFS，在 `for _ in range(len(q))` 本體裡動手 | §2-16（LC 116/117）、§2-17（LC 623） |
| 「還要幾分鐘全部才會變成 X」 | 一層 = 一個時間單位 —— 注意 `time++` 放哪裡 | *層數計算與計時*（LC 994） |

這些範例實例化的每個模板都在 [bfs.md](./bfs.md)；比較少見的變形在 [bfs_advanced.md](./bfs_advanced.md)。
