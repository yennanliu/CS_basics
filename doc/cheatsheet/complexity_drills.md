# Complexity Analysis Drills

> Practice identifying time and space complexity from code snippets.
> Google interviewers heavily probe this — expect follow-up questions like
> "Can you do better?" and "What's the space if we optimize?"

---

## How to Use

1. Read the code snippet
2. Determine Time and Space complexity **before** looking at the answer
3. Check your reasoning against the explanation
4. Star (⭐) problems you got wrong — revisit them

---

## Drill 1: Nested Loops

```java
for (int i = 0; i < n; i++) {
    for (int j = i; j < n; j++) {
        // O(1) work
    }
}
```

<details>
<summary>Answer</summary>

**Time: O(N²)**
Inner loop runs: N + (N-1) + (N-2) + ... + 1 = N(N+1)/2 → O(N²)

**Space: O(1)**

Common mistake: saying O(N) because "j starts at i". The sum of the series is still quadratic.
</details>

---

## Drill 2: Loop with Multiply

```java
int i = 1;
while (i < n) {
    // O(1) work
    i *= 2;
}
```

<details>
<summary>Answer</summary>

**Time: O(log N)**
i takes values 1, 2, 4, 8, ..., until i ≥ N. That's log₂(N) iterations.

**Space: O(1)**
</details>

---

## Drill 3: Loop with Divide

```java
for (int i = n; i >= 1; i /= 2) {
    for (int j = 0; j < i; j++) {
        // O(1) work
    }
}
```

<details>
<summary>Answer</summary>

**Time: O(N)**
Inner loop runs: N + N/2 + N/4 + ... + 1 = 2N → O(N) (geometric series!)

**Space: O(1)**

Common mistake: saying O(N log N). The work **halves** each iteration — geometric series converges to 2N.
</details>

---

## Drill 4: Recursive Fibonacci (Naive)

```python
def fib(n):
    if n <= 1: return n
    return fib(n-1) + fib(n-2)
```

<details>
<summary>Answer</summary>

**Time: O(2^N)** — more precisely O(φ^N) where φ ≈ 1.618 (golden ratio)
Each call branches into 2 subcalls. Tree has ~2^N nodes.

**Space: O(N)** — recursion stack depth is N (goes all the way down the left branch before returning)

Common mistake: saying space is O(2^N). The stack only holds one path at a time.
</details>

---

## Drill 5: HashMap in a Loop

```java
Map<Integer, Integer> map = new HashMap<>();
for (int i = 0; i < n; i++) {
    map.put(nums[i], i);
}
for (int i = 0; i < n; i++) {
    int complement = target - nums[i];
    if (map.containsKey(complement)) return true;
}
```

<details>
<summary>Answer</summary>

**Time: O(N)** average — HashMap put/get are O(1) amortized
**Space: O(N)** — storing up to N entries

Note: Worst case is O(N²) if all keys hash to the same bucket. Mention this in interviews but state the average case.
</details>

---

## Drill 6: Sorting + Binary Search

```python
nums.sort()  # Timsort
for x in nums:
    idx = bisect_left(nums, target - x)
```

<details>
<summary>Answer</summary>

**Time: O(N log N)** — sort is O(N log N), loop is N × O(log N) = O(N log N). Total: O(N log N).

**Space: O(N)** for Timsort (Python) or O(log N) for in-place sort (quicksort)
</details>

---

## Drill 7: BFS on Grid

```python
from collections import deque
queue = deque([(0, 0)])
visited = set()
visited.add((0, 0))
while queue:
    r, c = queue.popleft()
    for dr, dc in [(0,1),(0,-1),(1,0),(-1,0)]:
        nr, nc = r+dr, c+dc
        if 0 <= nr < m and 0 <= nc < n and (nr,nc) not in visited:
            visited.add((nr, nc))
            queue.append((nr, nc))
```

<details>
<summary>Answer</summary>

**Time: O(M·N)** — each cell visited at most once
**Space: O(M·N)** — visited set + queue can hold up to M·N cells

Common mistake: saying O(M·N·4) for the 4 directions. The 4 is a constant — drop it.
</details>

---

## Drill 8: Merge Sort

```python
def mergeSort(arr):
    if len(arr) <= 1: return arr
    mid = len(arr) // 2
    left = mergeSort(arr[:mid])
    right = mergeSort(arr[mid:])
    return merge(left, right)  # merge is O(N)
```

<details>
<summary>Answer</summary>

**Time: O(N log N)** — log N levels, each level does O(N) merge work total

**Space: O(N)** — merge creates new arrays; recursion stack is O(log N) but dominated by O(N) for the merged arrays

Note: `arr[:mid]` creates a copy — this is the O(N) space. In-place merge sort uses O(log N) space but is harder to implement.
</details>

---

## Drill 9: Subsets Generation

```python
def subsets(nums):
    result = [[]]
    for num in nums:
        result += [curr + [num] for curr in result]
    return result
```

<details>
<summary>Answer</summary>

**Time: O(N × 2^N)** — 2^N subsets, each up to length N to copy

**Space: O(N × 2^N)** — storing all subsets

Iteration breakdown: after processing k elements, result has 2^k subsets. We copy all of them and add num → 2^k copies of avg length k/2.
</details>

---

## Drill 10: Sliding Window

```python
left = 0
max_len = 0
freq = {}
for right in range(len(s)):
    freq[s[right]] = freq.get(s[right], 0) + 1
    while len(freq) > k:
        freq[s[left]] -= 1
        if freq[s[left]] == 0: del freq[s[left]]
        left += 1
    max_len = max(max_len, right - left + 1)
```

<details>
<summary>Answer</summary>

**Time: O(N)** — `left` pointer only moves forward, total moves ≤ N. Each character enters and exits the window at most once.

**Space: O(K)** or O(min(N, Σ)) where Σ = alphabet size

Common mistake: saying O(N²) because of the while loop inside the for loop. But left never goes backward — amortized O(N).
</details>

---

## Drill 11: Union-Find with Path Compression + Union by Rank

```java
int find(int x) {
    if (parent[x] != x) parent[x] = find(parent[x]);  // path compression
    return parent[x];
}
void union(int x, int y) {
    int px = find(x), py = find(y);
    if (px == py) return;
    if (rank[px] < rank[py]) parent[px] = py;
    else if (rank[px] > rank[py]) parent[py] = px;
    else { parent[py] = px; rank[px]++; }
}
```

<details>
<summary>Answer</summary>

**Time: O(α(N))** per operation — α is the inverse Ackermann function, effectively O(1) for all practical inputs (α(N) ≤ 5 for N ≤ 2^65536)

**Space: O(N)** for parent and rank arrays

Key insight: path compression alone gives O(log N) amortized. Union by rank alone gives O(log N). Together: O(α(N)) ≈ O(1).
</details>

---

## Drill 12: Trie Insert + Search

```java
void insert(String word) {
    TrieNode node = root;
    for (char c : word.toCharArray()) {
        if (node.children[c - 'a'] == null)
            node.children[c - 'a'] = new TrieNode();
        node = node.children[c - 'a'];
    }
    node.isEnd = true;
}
```

<details>
<summary>Answer</summary>

**Time: O(M)** where M = length of word

**Space: O(M)** for the new nodes in worst case (no shared prefix)

Total trie space for N words of avg length M: O(26 × M × N) worst case, but typically much less due to shared prefixes.
</details>

---

## Drill 13: Heap — K Closest Points

```python
import heapq
def kClosest(points, k):
    return heapq.nsmallest(k, points, key=lambda p: p[0]**2 + p[1]**2)
```

<details>
<summary>Answer</summary>

**Time: O(N log K)** — `nsmallest` maintains a max-heap of size K, processes all N points

**Space: O(K)** for the heap

Alternative: QuickSelect gives O(N) average time, O(1) extra space (mutates input).
</details>

---

## Drill 14: DP with 2D Table

```java
// LC 72 — Edit Distance
int[][] dp = new int[m + 1][n + 1];
for (int i = 0; i <= m; i++) dp[i][0] = i;
for (int j = 0; j <= n; j++) dp[0][j] = j;
for (int i = 1; i <= m; i++) {
    for (int j = 1; j <= n; j++) {
        if (word1.charAt(i-1) == word2.charAt(j-1))
            dp[i][j] = dp[i-1][j-1];
        else
            dp[i][j] = 1 + Math.min(dp[i-1][j-1],
                            Math.min(dp[i-1][j], dp[i][j-1]));
    }
}
```

<details>
<summary>Answer</summary>

**Time: O(M·N)** — fill M×N table, O(1) per cell

**Space: O(M·N)** — but optimizable to O(min(M,N)) using rolling array (only need previous row)

Follow-up: "Can you optimize space?" → Use 1D array of size min(M,N)+1, update in-place with a temp variable for the diagonal.
</details>

---

## Drill 15: Backtracking — N-Queens

```python
def solveNQueens(n):
    def backtrack(row, cols, diags, anti_diags):
        if row == n:
            result.append(board_snapshot())
            return
        for col in range(n):
            if col in cols or (row-col) in diags or (row+col) in anti_diags:
                continue
            # place queen and recurse
            backtrack(row+1, cols|{col}, diags|{row-col}, anti_diags|{row+col})
```

<details>
<summary>Answer</summary>

**Time: O(N!)** — at row 0: N choices, row 1: ~N-1, row 2: ~N-2, etc. (with pruning, much less in practice)

**Space: O(N)** — recursion depth is N, sets hold at most N elements each

Note: The number of solutions grows much slower than N!. For N=8, there are only 92 solutions out of 8! = 40320 possible placements.
</details>

---

## Drill 16: Amortized — Dynamic Array

```java
ArrayList<Integer> list = new ArrayList<>();
for (int i = 0; i < n; i++) {
    list.add(i);  // occasionally triggers resize and copy
}
```

<details>
<summary>Answer</summary>

**Time: O(N) total, O(1) amortized per add**

Resizes happen at sizes 1, 2, 4, 8, ..., N. Total copies: 1 + 2 + 4 + ... + N = 2N → O(N).

**Space: O(N)**

Key insight: even though individual adds can be O(N) (during resize), the amortized cost is O(1) because resizes are exponentially rare.
</details>

---

## Drill 17: String Concatenation in Loop

```java
String result = "";
for (int i = 0; i < n; i++) {
    result += chars[i];  // creates new String each time
}
```

<details>
<summary>Answer</summary>

**Time: O(N²)** — each concatenation copies the entire existing string. Copies: 1 + 2 + 3 + ... + N = O(N²)

**Space: O(N)** for the result string (intermediate strings are GC'd)

Fix: Use `StringBuilder` for O(N) total time.

This is a classic interview trap. Always mention StringBuilder/StringBuffer in Java.
</details>

---

## Drill 18: Two Pointers — Container with Most Water

```python
left, right = 0, len(height) - 1
max_area = 0
while left < right:
    max_area = max(max_area, min(height[left], height[right]) * (right - left))
    if height[left] < height[right]:
        left += 1
    else:
        right -= 1
```

<details>
<summary>Answer</summary>

**Time: O(N)** — each iteration moves one pointer, total at most N iterations

**Space: O(1)**

Why it works: moving the shorter side is the only way to potentially increase area (moving the taller side can only decrease width without increasing height).
</details>

---

## Drill 19: DFS on Tree with Serialization

```python
def serialize(root):
    if not root: return "null"
    return str(root.val) + "," + serialize(root.left) + "," + serialize(root.right)
```

<details>
<summary>Answer</summary>

**Time: O(N²)** in languages where string concatenation creates copies (Python, Java String). Each concat copies growing string.

**Space: O(N)** for recursion stack (if balanced: O(log N) stack + O(N) result string)

Fix: Use a list and join at the end → O(N) time.

```python
def serialize(root):
    parts = []
    def dfs(node):
        if not node: parts.append("null"); return
        parts.append(str(node.val))
        dfs(node.left)
        dfs(node.right)
    dfs(root)
    return ",".join(parts)  # O(N) total
```
</details>

---

## Drill 20: Dijkstra with Priority Queue

```java
PriorityQueue<int[]> pq = new PriorityQueue<>((a,b) -> a[1] - b[1]);
pq.offer(new int[]{src, 0});
int[] dist = new int[V];
Arrays.fill(dist, Integer.MAX_VALUE);
dist[src] = 0;

while (!pq.isEmpty()) {
    int[] curr = pq.poll();
    int u = curr[0], d = curr[1];
    if (d > dist[u]) continue;  // skip outdated entries
    for (int[] edge : graph[u]) {
        int v = edge[0], w = edge[1];
        if (dist[u] + w < dist[v]) {
            dist[v] = dist[u] + w;
            pq.offer(new int[]{v, dist[v]});
        }
    }
}
```

<details>
<summary>Answer</summary>

**Time: O((V + E) log V)** — each vertex extracted once (skip check), each edge relaxed once, heap operations are O(log V). Heap can have up to E entries → technically O((V + E) log E) = O((V + E) log V) since log E ≤ log V² = 2 log V.

**Space: O(V + E)** — dist array O(V), heap can hold O(E) entries

Note: Without the `if (d > dist[u]) continue` check, you'd process stale entries and potentially get O(E log E) with worse constants.
</details>

---

## Score Yourself

| Score | Level | Next Step |
|-------|-------|-----------|
| 18-20 correct | Ready for Google | Focus on speed — explain in 30 seconds |
| 14-17 correct | Almost there | Review the math intuitions in complexity_cheatsheet.md |
| 10-13 correct | Needs work | Study geometric series, amortized analysis, recursion trees |
| < 10 correct | Fundamentals | Start with Big-O basics, practice with simple examples |
