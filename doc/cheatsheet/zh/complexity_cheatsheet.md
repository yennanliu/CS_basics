# 複雜度速查表 — 經典演算法與資料結構

> **範圍** — **查表用**：每個常見資料結構與經典演算法的 Big-O，以及背後的數學直覺。
> **另見**：[time_space_complexity.md](./time_space_complexity.md) — 從真實 LC 程式碼逐行推導複雜度；[complexity_drills.md](./complexity_drills.md) — 自我測驗練習。

> 目標：Google SWE 面試準備
> 涵蓋：時間／空間複雜度、經典 LC 題目，以及數學直覺

---

## 1. Big-O 速查 ⭐⭐⭐⭐⭐

```text
O(1) < O(log N) < O(√N) < O(N) < O(N log N) < O(N²) < O(N³) < O(2^N) < O(N!)
```

| 複雜度 | 名稱 | 範例 |
|------------|------|---------|
| O(1) | 常數 | 陣列索引、HashMap get/put |
| O(log N) | 對數 | 二分搜尋、BST 操作 |
| O(√N) | 平方根 | 試除法、篩法分塊 |
| O(N) | 線性 | 單層迴圈、DFS/BFS |
| O(N log N) | 線性對數 | 合併排序、堆積排序 |
| O(N²) | 平方 | 氣泡／選擇／插入排序、巢狀迴圈 |
| O(N³) | 立方 | Floyd-Warshall、樸素矩陣相乘 |
| O(2^N) | 指數 | 子集合、樸素費氏數 |
| O(N!) | 階乘 | 排列 |

---

## 2. 資料結構 — 時間與空間複雜度

### 2-1) 陣列 / List

| 操作 | 時間 | 說明 |
|-----------|------|-------|
| 存取 `arr[i]` | O(1) | 隨機存取 |
| 搜尋（未排序） | O(N) | 線性掃描 |
| 搜尋（已排序） | O(log N) | 二分搜尋 |
| 在尾端插入 | 攤還 O(1) | 動態陣列擴充 |
| 在指定索引插入 | O(N) | 需要搬移元素 |
| 刪除指定索引 | O(N) | 需要搬移元素 |
| 刪除尾端 | O(1) | |

**空間：** O(N)

---

### 2-2) 雜湊表 / 雜湊集合 ⭐⭐⭐⭐

| 操作 | 平均 | 最差（碰撞） | 說明 |
|-----------|---------|-------------------|-------|
| 插入 | O(1) | O(N) | 假設雜湊函式夠好 |
| 刪除 | O(1) | O(N) | |
| 搜尋 | O(1) | O(N) | |
| 走訪 | O(N) | O(N) | |

**空間：** O(N)

**經典 LC：**
- LC 1 Two Sum — O(N) 時間、O(N) 空間
- LC 49 Group Anagrams — O(N·K) 時間、O(N·K) 空間（K = 平均單字長度）
- LC 128 Longest Consecutive Sequence — O(N) 時間、O(N) 空間

---

### 2-3) 堆疊 / 佇列 / 雙端佇列

| 操作 | 時間 | 說明 |
|-----------|------|-------|
| Push / Enqueue | O(1) | |
| Pop / Dequeue | O(1) | |
| Peek | O(1) | |
| 搜尋 | O(N) | |

**空間：** O(N)

**經典 LC：**
- LC 20 Valid Parentheses — O(N) 時間、O(N) 空間
- LC 84 Largest Rectangle in Histogram — O(N) 時間、O(N) 空間（單調堆疊）
- LC 155 Min Stack — 所有操作 O(1)、O(N) 空間

---

### 2-4) 堆積（優先佇列） ⭐⭐⭐⭐

| 操作 | 時間 | 說明 |
|-----------|------|-------|
| 插入（heappush） | O(log N) | 往上浮 |
| 取出最小／最大 | O(log N) | 往下沉 |
| 讀取最小／最大 | O(1) | |
| 建堆（heapify） | **O(N)** | 不是 O(N log N) — 見數學章節 |
| 堆積排序 | O(N log N) | |

**空間：** O(N)

**經典 LC：**
- LC 215 Kth Largest Element — O(N log K) 時間、O(K) 空間
- LC 347 Top K Frequent Elements — O(N log K) 時間、O(N) 空間
- LC 23 Merge K Sorted Lists — O(N log K) 時間、O(K) 空間（N = 總節點數）
- LC 295 Find Median from Data Stream — 插入 O(log N)、取中位數 O(1)

---

### 2-5) 二元搜尋樹（BST）

| 操作 | 平均 | 最差（退化成鏈） | 說明 |
|-----------|---------|----------------|-------|
| 搜尋 | O(log N) | O(N) | |
| 插入 | O(log N) | O(N) | |
| 刪除 | O(log N) | O(N) | |
| 中序走訪 | O(N) | O(N) | 輸出為排序結果 |

**空間：** 儲存 O(N)、遞迴堆疊 O(H)（H = 樹高）

**平衡 BST（AVL、紅黑樹）：** 所有操作保證 O(log N)

---

### 2-6) 字典樹（Trie，前綴樹）

| 操作 | 時間 | 說明 |
|-----------|------|-------|
| 插入 | O(M) | M = 單字長度 |
| 搜尋 | O(M) | |
| 前綴搜尋 | O(M) | |
| 刪除 | O(M) | |

**空間：** O(ALPHABET_SIZE × M × N) — N 個平均長度為 M 的單字

**經典 LC：**
- LC 208 Implement Trie — 每個操作 O(M)、O(26·M·N) 空間
- LC 212 Word Search II — O(M·N·4·3^(L-1)) 時間（M×N 格子，L = 單字長度）
- LC 720 Longest Word in Dictionary — O(N log N + N·M)

---

### 2-7) 圖

| 表示法 | 空間 | 查詢邊 | 加邊 |
|----------------|-------|-------------|----------|
| 鄰接矩陣 | O(V²) | O(1) | O(1) |
| 鄰接串列 | O(V+E) | O(degree) | O(1) |

| 演算法 | 時間 | 空間 | 使用情境 |
|-----------|------|-------|----------|
| BFS | O(V+E) | O(V) | 最短路徑（無權重） |
| DFS | O(V+E) | O(V) | 偵測環、拓撲排序 |
| Dijkstra（min-heap） | O((V+E) log V) | O(V) | 最短路徑（非負權重） |
| Bellman-Ford | O(V·E) | O(V) | 有負權重 |
| Floyd-Warshall | O(V³) | O(V²) | 全點對最短路徑 |
| 拓撲排序（DFS/BFS） | O(V+E) | O(V) | DAG 排序 |
| 併查集 | O(α(N)) ≈ O(1) | O(N) | 連通性、MST |
| Prim's MST | O(E log V) | O(V) | 最小生成樹 |
| Kruskal's MST | O(E log E) | O(V) | 最小生成樹 |

**經典 LC：**
- LC 200 Number of Islands — O(M·N) 時間、O(M·N) 空間
- LC 207 Course Schedule — O(V+E)、偵測環
- LC 743 Network Delay Time — O((V+E) log V) 的 Dijkstra
- LC 684 Redundant Connection — O(N·α(N)) 的併查集

---

### 2-8) 排序演算法 ⭐⭐⭐⭐

| 演算法 | 最佳 | 平均 | 最差 | 空間 | 穩定？ |
|-----------|------|---------|-------|-------|---------|
| 氣泡排序 | O(N) | O(N²) | O(N²) | O(1) | 是 |
| 選擇排序 | O(N²) | O(N²) | O(N²) | O(1) | 否 |
| 插入排序 | O(N) | O(N²) | O(N²) | O(1) | 是 |
| 合併排序 | O(N log N) | O(N log N) | O(N log N) | O(N) | 是 |
| 快速排序 | O(N log N) | O(N log N) | O(N²) | O(log N) | 否 |
| 堆積排序 | O(N log N) | O(N log N) | O(N log N) | O(1) | 否 |
| 計數排序 | O(N+K) | O(N+K) | O(N+K) | O(K) | 是 |
| 基數排序 | O(N·d) | O(N·d) | O(N·d) | O(N+K) | 是 |
| Tim Sort（Python/Java） | O(N) | O(N log N) | O(N log N) | O(N) | 是 |

---

### 2-9) 動態規劃 — 經典模式

| 題型 | 時間 | 空間 | 可優化？ |
|--------------|------|-------|--------------|
| 一維 DP（費氏數、爬樓梯） | O(N) | O(N) → 用滾動變數降到 O(1) |
| 二維 DP（LCS、編輯距離） | O(M·N) | O(M·N) → 滾動列降到 O(N) |
| 0/1 背包 | O(N·W) | O(N·W) → O(W) |
| 硬幣找零 | O(N·amount) | O(amount) |
| 最長遞增子序列 | O(N²) 或 O(N log N) | O(N) |
| 矩陣連鎖相乘 | O(N³) | O(N²) |
| 字串回文 DP | O(N²) | O(N²) → O(N) |

**經典 LC：**
- LC 70 Climbing Stairs — O(N) 時間、O(1) 空間
- LC 322 Coin Change — O(N·amount) 時間、O(amount) 空間
- LC 300 LIS — 用 patience sort 可達 O(N log N)
- LC 72 Edit Distance — O(M·N) 時間、O(min(M,N)) 空間
- LC 1143 LCS — O(M·N) 時間、O(M·N) 空間

---

### 2-10) 二分搜尋 — 各種模式

| 模式 | 時間 | 空間 |
|---------|------|-------|
| 標準二分搜尋 | O(log N) | O(1) |
| 對答案做二分搜尋 | O(log(MAX) · f(N)) | O(1) |
| 在旋轉有序陣列中搜尋 | O(log N) | O(1) |
| 找第一個／最後一個出現位置 | O(log N) | O(1) |

**經典 LC：**
- LC 704 Binary Search — O(log N)
- LC 33 Search in Rotated Sorted Array — O(log N)
- LC 162 Find Peak Element — O(log N)
- LC 410 Split Array Largest Sum — O(N · log(sum))，對答案做二分搜尋

---

### 2-11) 樹的走訪

| 演算法 | 時間 | 空間 | 說明 |
|-----------|------|-------|-------|
| DFS（中／前／後序） | O(N) | O(H) | H = 樹高，平衡時 O(log N)，退化成鏈時 O(N) |
| BFS（層序） | O(N) | O(W) | W = 最大寬度，最後一層約 O(N/2) |
| Morris 走訪 | O(N) | O(1) | 空間最佳，但會暫時修改樹結構 |

**經典 LC：**
- LC 104 Max Depth of Binary Tree — O(N) 時間、O(H) 空間
- LC 102 Binary Tree Level Order — O(N) 時間、O(W) 空間
- LC 236 LCA of Binary Tree — O(N) 時間、O(H) 空間
- LC 124 Binary Tree Max Path Sum — O(N) 時間、O(H) 空間

---

## 3. 數學技巧與直覺

### 3-1) 等比級數 — 為什麼 N + N/2 + N/4 + ... = 2N（而不是 N log N！） ⭐⭐⭐⭐⭐

```text
S = N + N/2 + N/4 + N/8 + ...
  = N × (1 + 1/2 + 1/4 + 1/8 + ...)
  = N × 1/(1 - 1/2)      ← geometric series formula: 1/(1-r) for |r| < 1
  = N × 2
  = 2N
```

**→ O(N)**，不是 O(N log N)

**這在演算法裡為什麼重要：**

```text
Example: Heapify (build heap from array)
  - Leaf level (N/2 nodes): 0 swaps each   → N/2 × 0
  - Level above (N/4 nodes): 1 swap each   → N/4 × 1
  - Level above (N/8 nodes): 2 swaps each  → N/8 × 2
  - ...
  - Root (1 node): log N swaps             → 1 × log N

  Total = N/4 + 2·N/8 + 3·N/16 + ...
        = N × (1/4 + 2/8 + 3/16 + ...) ≈ N
  → O(N)  ← This is why heapify is O(N), not O(N log N)!
```

```text
Example: Segment tree build
  - Leaf nodes: N
  - Internal nodes: N - 1
  - Work at each node: O(1)
  → Total: O(N)
```

```text
Example: Amortized cost of dynamic array doubling
  Insert N elements:
  Copies at resizes: 1 + 2 + 4 + 8 + ... + N = 2N
  → Amortized O(1) per insert
```

---

### 3-2) 為什麼合併排序是 O(N log N) — 跟上面不一樣 ⭐⭐⭐⭐

```text
Level 0:   1 merge of size N     → N work
Level 1:   2 merges of size N/2  → N work
Level 2:   4 merges of size N/4  → N work
...
Level k:   2^k merges of size N/2^k → N work

Total levels = log₂(N)
Total work   = N × log₂(N)
→ O(N log N)
```

**關鍵差異：**
- 等比級數：每一層的工作量**減半** → 總和收斂到 2N
- 合併排序：每一層的工作量**維持不變** → 總和 = N × 層數 = N log N

```text
Geometric (converging):     Merge Sort (constant per level):
Level 0:  N                 Level 0:  N
Level 1:  N/2               Level 1:  N/2 + N/2 = N
Level 2:  N/4               Level 2:  N/4+N/4+N/4+N/4 = N
Level 3:  N/8               Level 3:  N
...                         ...
Sum ≈ 2N  ✓ O(N)            Sum = N × log N  ✓ O(N log N)
```

---

### 3-2b) 個案研究：LC 109 — Convert Sorted List to BST（O(N log N) 時間、O(log N) 空間）

> 這是「每層 N 的工作量 × log N 層 = O(N log N)」這個模式的**標準範例**，
> 同時也是「遞迴空間 = 樹高，而不是節點數」的經典示範。

#### 為什麼時間是 O(N log N)

這個演算法做兩件事：
1. 用 `getNodeByIdx()` 找中間節點 — 每次都從頭掃描 → **O(N)**
2. 遞迴建構左右子樹

**昂貴的部分：** 找中間節點時每次呼叫都要從頭掃描。

```text
N = 8, getNodeByIdx(head, mid) ≈ O(N) per call

Level 0:  build(0..7)                             → scan ~N    = N
Level 1:  build(0..2) + build(4..7)               → N/2 + N/2  = N
Level 2:  build(0..0)+build(2..2)+build(4..5)+... → N/4×4      = N
Level 3:  8 leaf calls                             → N/8×8      = N
```

圖示：
```text
                N
           /         \
        N/2           N/2
       /   \         /   \
     N/4   N/4    N/4   N/4
```

每一層 = **總共 N 的工作量**。層數 = **log N**（平衡 BST 的高度）。

```text
Total = N + N + N + ... (log N times) = N × log N → O(N log N)
```

**與等比級數（heapify）比較：**
```text
Heapify:        N/2 + N/4 + N/8 + ... = N  →  O(N)      (work HALVES per level)
BST from list:  N   + N   + N   + ... = N log N → O(N log N) (work CONSTANT per level)
```

#### 為什麼空間是 O(log N)  ← 遞迴堆疊深度，不是節點數

**關鍵規則：**
```text
Space complexity for DFS recursion = maximum recursion stack depth
                                    = tree height
```

遞迴不會同時把兩個分支都壓在堆疊上。
它是**一次走一個分支**（深度優先）：

```text
build(root)           ← frame 1 on stack
  -> build(left)      ← frame 2 on stack
       -> build(left) ← frame 3 on stack
            -> null   ← returns, pops frame 3
```

**N = 7 的圖示（有序串列：1→2→3→4→5→6→7）：**

```text
Balanced BST built:
        4
      /   \
     2     6
    / \   / \
   1   3 5   7

Tree height = 3

Max stack at any moment (going down leftmost path):
  build(0,6)  → root 4   ← frame 1
  build(0,2)  → root 2   ← frame 2
  build(0,0)  → root 1   ← frame 3
  build(0,-1) → null     ← frame 4, then returns

Stack depth ≈ log₂(7) ≈ 3
```

**N 與樹高的對照：**

| N         | 樹高（堆疊深度） |
|-----------|----------------------|
| 8         | 3                    |
| 16        | 4                    |
| 1,024     | 10                   |
| 1,000,000 | 約 20                |

**為什麼不是 O(N)？** 只有**退化成鏈**的樹才會用到 O(N) 的堆疊：
```text
1
 \
  2
   \
    3       ← each node is a stack frame → O(N) depth
     \
      4
```
LC 109 每次都取**中間** → 保證平衡 → 堆疊深度 = O(log N)。

#### 小結

```text
Algorithm Variant              | Time        | Space    | How?
-------------------------------|-------------|----------|--------------------------------------
V0-1: getNodeByIdx (from head) | O(N log N)  | O(log N) | N scan × log N levels; balanced tree
V0-2: slow/fast pointer        | O(N log N)  | O(log N) | O(N) per split × log N levels
V0-3: in-order simulation      | O(N)        | O(log N) | advance pointer once per node (!)
```

> **面試提示：** V0-3（用一個共用指標做中序模擬）才是**最佳的 O(N)** 解法。
> 它讓串列指標與 BST 中序走訪同步前進，因此不需要重複掃描。

```text
Interview rule of thumb for recursion:
  Space = recursion stack depth
  Balanced tree → O(log N)
  Skewed tree   → O(N)
  N work per level × log N levels → O(N log N)  ← "merge sort" pattern
```

---

### 3-3) 等差級數 — 為什麼 1 + 2 + 3 + ... + N = N(N+1)/2 ≈ N²/2

```text
S = 1 + 2 + 3 + ... + N
  = N(N+1)/2
  ≈ N²/2
  → O(N²)
```

**這會出現在哪裡：**
```text
Bubble sort comparisons:
  Round 1: N-1 comparisons
  Round 2: N-2 comparisons
  ...
  Round N-1: 1 comparison
  Total = (N-1) + (N-2) + ... + 1 = N(N-1)/2 → O(N²)

Counting all pairs in array:
  For each i, compare with all j > i
  Total pairs = N(N-1)/2 → O(N²)
```

---

### 3-4) 對數恆等式（複雜度分析用）

```text
log₂(N) ≈ ln(N) / 0.693           # log base conversion
log₂(N²) = 2 log₂(N)              # power rule
log₂(N·M) = log₂(N) + log₂(M)    # product rule
log₂(2^N) = N                     # inverse

2^(log₂ N) = N                    # inverse
log₂(N!) ≈ N log₂ N  (Stirling)   # N! ≈ (N/e)^N × √(2πN)

How many times can you halve N before reaching 1?
  → log₂(N) times   (binary search, tree height)

How many times can you double 1 before reaching N?
  → log₂(N) times   (binary tree levels)
```

---

### 3-5) 2 的次方 — 速查

| 表達式 | 值 | 對應情境 |
|------------|-------|---------|
| 2^10 | 1,024 ≈ 10³ | 1K |
| 2^20 | 1,048,576 ≈ 10⁶ | 1M |
| 2^30 | 1,073,741,824 ≈ 10⁹ | 1B |
| 2^32 | 4,294,967,296 ≈ 4×10⁹ | int 上限 |
| 2^63 | ≈ 9.2×10¹⁸ | long 上限 |
| 2^31 - 1 | 2,147,483,647 | Java 的 Integer.MAX_VALUE |

**這對複雜度為什麼重要：**
```text
N = 10^9 operations → too slow for 1 second (typical limit: 10^8 ops/sec)
N = 10^6 → fine for O(N log N)
N = 10^5 → fine for O(N²)... barely
N = 20   → OK for O(2^N)
N = 12   → OK for O(N!)
```

---

### 3-6) 調和級數 — 為什麼 1 + 1/2 + 1/3 + ... + 1/N ≈ ln(N)

```text
H(N) = 1 + 1/2 + 1/3 + ... + 1/N ≈ ln(N) ≈ O(log N)
```

**這會出現在哪裡：**
```text
Sieve of Eratosthenes: O(N log log N)
  - Cross out multiples of 2: N/2 ops
  - Cross out multiples of 3: N/3 ops
  - Cross out multiples of 5: N/5 ops
  - Total ≈ N × (1/2 + 1/3 + 1/5 + ...) = N × log log N

Average case of Quick Sort partitioning ≈ O(N log N)
```

---

### 3-7) 計算子集合與排列的數量

```text
Subsets of N elements:       2^N
Permutations of N elements:  N!
Combinations C(N, K):        N! / (K! × (N-K)!)

Stirling's approximation:    N! ≈ √(2πN) × (N/e)^N
→ log(N!) ≈ N log N         (explains why permutation algos are O(N log N) in log space)
```

**經典 LC：**
- LC 78 Subsets — O(N × 2^N) 時間、O(N × 2^N) 空間
- LC 46 Permutations — O(N × N!) 時間、O(N!) 空間
- LC 77 Combinations — O(K × C(N,K)) 時間

---

### 3-8) 遞迴關係式 — 主定理（Master Theorem）

對於 `T(N) = a·T(N/b) + f(N)`：

```text
Compare f(N) vs N^(log_b(a)):

Case 1: f(N) = O(N^(log_b(a) - ε))  →  T(N) = O(N^log_b(a))
Case 2: f(N) = Θ(N^log_b(a))         →  T(N) = O(N^log_b(a) × log N)
Case 3: f(N) = Ω(N^(log_b(a) + ε))  →  T(N) = O(f(N))
```

**常見例子：**

| 遞迴式 | a | b | f(N) | 結果 | 演算法 |
|------------|---|---|------|--------|-----------|
| T(N) = 2T(N/2) + N | 2 | 2 | N | O(N log N) | 合併排序 |
| T(N) = 2T(N/2) + 1 | 2 | 2 | 1 | O(N) | 樹的走訪 |
| T(N) = T(N/2) + 1 | 1 | 2 | 1 | O(log N) | 二分搜尋 |
| T(N) = T(N/2) + N | 1 | 2 | N | O(N) | 某些分治法 |
| T(N) = 4T(N/2) + N | 4 | 2 | N | O(N²) | 樸素矩陣相乘 |
| T(N) = 3T(N/2) + N² | 3 | 2 | N² | O(N²) | Strassen 類演算法 |

---

### 3-9) 字元題的 ASCII 技巧

```text
'a' - 'A' = 32     (difference between lowercase and uppercase)
'a' = 97,  'A' = 65
'z' = 122, 'Z' = 90
'0' = 48,  '9' = 57

Detect same letter different case:
  Math.abs(char1 - char2) == 32   → O(1) check
  (see LC 1544 Make The String Great)

Convert case:
  lowercase → uppercase:  c - 32  (or c & ~32 or c ^ 32)
  uppercase → lowercase:  c + 32  (or c | 32)

Index in alphabet:
  c - 'a'  (0-indexed: 'a'→0, 'b'→1, ..., 'z'→25)
  c - 'A'  (same for uppercase)
```

---

## 4. Google 經典面試題 — 複雜度總表

### 第一級：必須會

| 題目 | LC # | 時間 | 空間 | 關鍵想法 |
|---------|------|------|-------|----------|
| Two Sum | 1 | O(N) | O(N) | HashMap |
| Valid Parentheses | 20 | O(N) | O(N) | 堆疊 |
| Merge Intervals | 56 | O(N log N) | O(N) | 排序 + 掃描 |
| LRU Cache | 146 | 每次操作 O(1) | O(N) | HashMap + 雙向鏈結串列 |
| Binary Tree Level Order | 102 | O(N) | O(W) | BFS |
| Number of Islands | 200 | O(M·N) | O(M·N) | DFS/BFS/併查集 |
| Course Schedule | 207 | O(V+E) | O(V+E) | 拓撲排序 |
| Clone Graph | 133 | O(V+E) | O(V) | BFS + HashMap |
| Merge K Sorted Lists | 23 | O(N log K) | O(K) | Min-heap |
| Kth Largest Element | 215 | 平均 O(N) | O(1) | QuickSelect |

### 第二級：常考

| 題目 | LC # | 時間 | 空間 | 關鍵想法 |
|---------|------|------|-------|----------|
| Longest Substring Without Repeating | 3 | O(N) | O(min(N,Σ)) | 滑動視窗 |
| Word Ladder | 127 | O(M²·N) | O(M²·N) | BFS |
| Trapping Rain Water | 42 | O(N) | O(1) | 雙指標 |
| Serialize/Deserialize Binary Tree | 297 | O(N) | O(N) | BFS 或 DFS |
| Find Median from Data Stream | 295 | O(log N) | O(N) | 雙堆積 |
| Alien Dictionary | 269 | O(C) | O(1) | 拓撲排序 |
| Regular Expression Matching | 10 | O(M·N) | O(M·N) | DP |
| Word Break | 139 | O(N²) | O(N) | DP |
| Decode Ways | 91 | O(N) | O(1) | DP |
| Coin Change | 322 | O(N·amount) | O(amount) | DP |

### 第三級：困難題／追問題

| 題目 | LC # | 時間 | 空間 | 關鍵想法 |
|---------|------|------|-------|----------|
| Median of Two Sorted Arrays | 4 | O(log(M+N)) | O(1) | 二分搜尋 |
| Sliding Window Maximum | 239 | O(N) | O(K) | 單調雙端佇列 |
| Largest Rectangle in Histogram | 84 | O(N) | O(N) | 單調堆疊 |
| Word Search II | 212 | O(M·N·4·3^(L-1)) | O(L) | 字典樹 + DFS |
| Minimum Window Substring | 76 | O(N+M) | O(Σ) | 滑動視窗 |
| Binary Tree Maximum Path Sum | 124 | O(N) | O(H) | DFS 後序 |
| Longest Increasing Subsequence | 300 | O(N log N) | O(N) | Patience sort |
| Jump Game II | 45 | O(N) | O(1) | 貪婪 |
| Text Justification | 68 | O(N·W) | O(W) | 貪婪模擬 |

---

## 5. 空間複雜度模式

### 堆疊空間（遞迴）
```text
Balanced BST / heap:     O(log N)  ← tree height
Skewed BST / linked list: O(N)     ← degenerates to linear
General graph DFS:        O(V)
```

### 什麼時候該用原地（in-place）演算法
```text
O(1) extra space tricks:
  - Two pointers (reverse array, palindrome)
  - Floyd's cycle detection
  - Morris tree traversal
  - Partition in-place (quick sort, Dutch flag)
  - Bit manipulation for visited flags (if N ≤ 64)
```

### 空間與時間的取捨
```text
Problem                 | Naive Space | Optimized Space | Time Trade-off
------------------------|-------------|-----------------|---------------
DP (2D → 1D rolling)   | O(M·N)      | O(N)            | Same time
Memoization → Bottom-up| O(N) stack  | O(1) or O(N)    | Same time
Fibonacci               | O(N)        | O(1)            | Same O(N)
KMP vs Naive search     | O(M)        | —               | O(N+M) vs O(N·M)
```

---

## 6. 面試決策指南

```text
Input Size → Reasonable Complexity:

N ≤ 10        → O(N!) or O(2^N)     backtracking, all permutations
N ≤ 20        → O(2^N)              bitmask DP, subsets
N ≤ 100       → O(N³)               Floyd-Warshall, 3-loop DP
N ≤ 1,000     → O(N²)               nested loops, naive DP
N ≤ 100,000   → O(N log N)          sorting, heap, balanced BST
N ≤ 1,000,000 → O(N) or O(N log N) single/two pass, sliding window
N ≤ 10^9      → O(log N) or O(√N)  binary search, math tricks
```

---

## 7. 快速健檢

```text
1. O(N log N) sort first → enables O(N) or O(log N) operations after
2. Two passes O(N) each = still O(N)
3. Nested loops with early break ≠ necessarily O(N²)
4. Recursion depth × work per call = space × time relationship
5. BFS uses O(W) space; DFS uses O(H) space — choose based on graph shape
6. HashMap insert/lookup = O(1) avg but O(N) worst — mention in interview
7. N + N/2 + N/4 + ... = 2N = O(N)  ← geometric series always converges!
8. Heapify = O(N),  building heap by N inserts = O(N log N)  ← different!
```
