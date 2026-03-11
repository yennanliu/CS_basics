# Complexity Cheatsheet — Classic Algorithms & Data Structures

> Target: Google SWE interview preparation
> Covers: Time/Space complexity, classic LC problems, and math intuitions

---

## 1. Big-O Quick Reference

```
O(1) < O(log N) < O(√N) < O(N) < O(N log N) < O(N²) < O(N³) < O(2^N) < O(N!)
```

| Complexity | Name | Example |
|------------|------|---------|
| O(1) | Constant | Array index, HashMap get/put |
| O(log N) | Logarithmic | Binary search, BST ops |
| O(√N) | Root | Trial division, sieve block |
| O(N) | Linear | Single loop, DFS/BFS |
| O(N log N) | Linearithmic | Merge sort, heap sort |
| O(N²) | Quadratic | Bubble/selection/insertion sort, nested loops |
| O(N³) | Cubic | Floyd-Warshall, naive matrix multiply |
| O(2^N) | Exponential | Subsets, Fibonacci naive |
| O(N!) | Factorial | Permutations |

---

## 2. Data Structures — Time & Space Complexity

### 2-1) Array / List

| Operation | Time | Notes |
|-----------|------|-------|
| Access `arr[i]` | O(1) | Random access |
| Search (unsorted) | O(N) | Linear scan |
| Search (sorted) | O(log N) | Binary search |
| Insert at end | O(1) amortized | Dynamic array resize |
| Insert at index | O(N) | Shift elements |
| Delete at index | O(N) | Shift elements |
| Delete at end | O(1) | |

**Space:** O(N)

---

### 2-2) Hash Map / Hash Set

| Operation | Average | Worst (collision) | Notes |
|-----------|---------|-------------------|-------|
| Insert | O(1) | O(N) | Good hash function assumed |
| Delete | O(1) | O(N) | |
| Search | O(1) | O(N) | |
| Iteration | O(N) | O(N) | |

**Space:** O(N)

**Classic LC:**
- LC 1 Two Sum — O(N) time, O(N) space
- LC 49 Group Anagrams — O(N·K) time, O(N·K) space (K = avg word len)
- LC 128 Longest Consecutive Sequence — O(N) time, O(N) space

---

### 2-3) Stack / Queue / Deque

| Operation | Time | Notes |
|-----------|------|-------|
| Push / Enqueue | O(1) | |
| Pop / Dequeue | O(1) | |
| Peek | O(1) | |
| Search | O(N) | |

**Space:** O(N)

**Classic LC:**
- LC 20 Valid Parentheses — O(N) time, O(N) space
- LC 84 Largest Rectangle in Histogram — O(N) time, O(N) space (monotonic stack)
- LC 155 Min Stack — O(1) all ops, O(N) space

---

### 2-4) Heap (Priority Queue)

| Operation | Time | Notes |
|-----------|------|-------|
| Insert (heappush) | O(log N) | Bubble up |
| Extract min/max | O(log N) | Bubble down |
| Peek min/max | O(1) | |
| Build heap (heapify) | **O(N)** | NOT O(N log N) — see math section |
| Heap sort | O(N log N) | |

**Space:** O(N)

**Classic LC:**
- LC 215 Kth Largest Element — O(N log K) time, O(K) space
- LC 347 Top K Frequent Elements — O(N log K) time, O(N) space
- LC 23 Merge K Sorted Lists — O(N log K) time, O(K) space (N = total nodes)
- LC 295 Find Median from Data Stream — O(log N) insert, O(1) median

---

### 2-5) Binary Search Tree (BST)

| Operation | Average | Worst (skewed) | Notes |
|-----------|---------|----------------|-------|
| Search | O(log N) | O(N) | |
| Insert | O(log N) | O(N) | |
| Delete | O(log N) | O(N) | |
| In-order traversal | O(N) | O(N) | Sorted output |

**Space:** O(N) storage, O(H) recursion stack (H = height)

**Balanced BST (AVL, Red-Black):** all ops guaranteed O(log N)

---

### 2-6) Trie (Prefix Tree)

| Operation | Time | Notes |
|-----------|------|-------|
| Insert | O(M) | M = word length |
| Search | O(M) | |
| Prefix search | O(M) | |
| Delete | O(M) | |

**Space:** O(ALPHABET_SIZE × M × N) — N words of avg length M

**Classic LC:**
- LC 208 Implement Trie — O(M) ops, O(26·M·N) space
- LC 212 Word Search II — O(M·N·4·3^(L-1)) time (M×N grid, L = word len)
- LC 720 Longest Word in Dictionary — O(N log N + N·M)

---

### 2-7) Graph

| Representation | Space | Edge lookup | Add edge |
|----------------|-------|-------------|----------|
| Adjacency Matrix | O(V²) | O(1) | O(1) |
| Adjacency List | O(V+E) | O(degree) | O(1) |

| Algorithm | Time | Space | Use Case |
|-----------|------|-------|----------|
| BFS | O(V+E) | O(V) | Shortest path (unweighted) |
| DFS | O(V+E) | O(V) | Cycle detection, topo sort |
| Dijkstra (min-heap) | O((V+E) log V) | O(V) | Shortest path (non-negative weights) |
| Bellman-Ford | O(V·E) | O(V) | Negative weights |
| Floyd-Warshall | O(V³) | O(V²) | All-pairs shortest path |
| Topological Sort (DFS/BFS) | O(V+E) | O(V) | DAG ordering |
| Union-Find | O(α(N)) ≈ O(1) | O(N) | Connectivity, MST |
| Prim's MST | O(E log V) | O(V) | Minimum spanning tree |
| Kruskal's MST | O(E log E) | O(V) | Minimum spanning tree |

**Classic LC:**
- LC 200 Number of Islands — O(M·N) time, O(M·N) space
- LC 207 Course Schedule — O(V+E), cycle detection
- LC 743 Network Delay Time — O((V+E) log V) Dijkstra
- LC 684 Redundant Connection — O(N·α(N)) Union-Find

---

### 2-8) Sorting Algorithms

| Algorithm | Best | Average | Worst | Space | Stable? |
|-----------|------|---------|-------|-------|---------|
| Bubble Sort | O(N) | O(N²) | O(N²) | O(1) | Yes |
| Selection Sort | O(N²) | O(N²) | O(N²) | O(1) | No |
| Insertion Sort | O(N) | O(N²) | O(N²) | O(1) | Yes |
| Merge Sort | O(N log N) | O(N log N) | O(N log N) | O(N) | Yes |
| Quick Sort | O(N log N) | O(N log N) | O(N²) | O(log N) | No |
| Heap Sort | O(N log N) | O(N log N) | O(N log N) | O(1) | No |
| Counting Sort | O(N+K) | O(N+K) | O(N+K) | O(K) | Yes |
| Radix Sort | O(N·d) | O(N·d) | O(N·d) | O(N+K) | Yes |
| Tim Sort (Python/Java) | O(N) | O(N log N) | O(N log N) | O(N) | Yes |

---

### 2-9) Dynamic Programming — Classic Patterns

| Problem Type | Time | Space | Optimizable? |
|--------------|------|-------|--------------|
| 1D DP (Fibonacci, climb stairs) | O(N) | O(N) → O(1) with rolling var |
| 2D DP (LCS, edit distance) | O(M·N) | O(M·N) → O(N) rolling row |
| Knapsack 0/1 | O(N·W) | O(N·W) → O(W) |
| Coin Change | O(N·amount) | O(amount) |
| Longest Increasing Subsequence | O(N²) or O(N log N) | O(N) |
| Matrix Chain Multiplication | O(N³) | O(N²) |
| String palindrome DP | O(N²) | O(N²) → O(N) |

**Classic LC:**
- LC 70 Climbing Stairs — O(N) time, O(1) space
- LC 322 Coin Change — O(N·amount) time, O(amount) space
- LC 300 LIS — O(N log N) with patience sort
- LC 72 Edit Distance — O(M·N) time, O(min(M,N)) space
- LC 1143 LCS — O(M·N) time, O(M·N) space

---

### 2-10) Binary Search — Patterns

| Pattern | Time | Space |
|---------|------|-------|
| Standard binary search | O(log N) | O(1) |
| Binary search on answer | O(log(MAX) · f(N)) | O(1) |
| Search in rotated sorted array | O(log N) | O(1) |
| Find first/last occurrence | O(log N) | O(1) |

**Classic LC:**
- LC 704 Binary Search — O(log N)
- LC 33 Search in Rotated Sorted Array — O(log N)
- LC 162 Find Peak Element — O(log N)
- LC 410 Split Array Largest Sum — O(N · log(sum)) binary search on answer

---

### 2-11) Tree Traversals

| Algorithm | Time | Space | Notes |
|-----------|------|-------|-------|
| DFS (in/pre/post-order) | O(N) | O(H) | H = height, O(log N) balanced, O(N) skewed |
| BFS (level-order) | O(N) | O(W) | W = max width ≈ O(N/2) in last level |
| Morris Traversal | O(N) | O(1) | Space-optimal, modifies tree temporarily |

**Classic LC:**
- LC 104 Max Depth of Binary Tree — O(N) time, O(H) space
- LC 102 Binary Tree Level Order — O(N) time, O(W) space
- LC 236 LCA of Binary Tree — O(N) time, O(H) space
- LC 124 Binary Tree Max Path Sum — O(N) time, O(H) space

---

## 3. Math Tricks & Intuitions

### 3-1) Geometric Series — Why N + N/2 + N/4 + ... = 2N (NOT N log N!)

```
S = N + N/2 + N/4 + N/8 + ...
  = N × (1 + 1/2 + 1/4 + 1/8 + ...)
  = N × 1/(1 - 1/2)      ← geometric series formula: 1/(1-r) for |r| < 1
  = N × 2
  = 2N
```

**→ O(N)**, NOT O(N log N)

**Why this matters in algorithms:**

```
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

```
Example: Segment tree build
  - Leaf nodes: N
  - Internal nodes: N - 1
  - Work at each node: O(1)
  → Total: O(N)
```

```
Example: Amortized cost of dynamic array doubling
  Insert N elements:
  Copies at resizes: 1 + 2 + 4 + 8 + ... + N = 2N
  → Amortized O(1) per insert
```

---

### 3-2) Why Merge Sort is O(N log N) — NOT the same as above

```
Level 0:   1 merge of size N     → N work
Level 1:   2 merges of size N/2  → N work
Level 2:   4 merges of size N/4  → N work
...
Level k:   2^k merges of size N/2^k → N work

Total levels = log₂(N)
Total work   = N × log₂(N)
→ O(N log N)
```

**Key difference:**
- Geometric series: work **halves** each level → sum converges to 2N
- Merge sort: work **stays the same** each level → sum = N × levels = N log N

```
Geometric (converging):     Merge Sort (constant per level):
Level 0:  N                 Level 0:  N
Level 1:  N/2               Level 1:  N/2 + N/2 = N
Level 2:  N/4               Level 2:  N/4+N/4+N/4+N/4 = N
Level 3:  N/8               Level 3:  N
...                         ...
Sum ≈ 2N  ✓ O(N)            Sum = N × log N  ✓ O(N log N)
```

---

### 3-3) Arithmetic Series — Why 1 + 2 + 3 + ... + N = N(N+1)/2 ≈ N²/2

```
S = 1 + 2 + 3 + ... + N
  = N(N+1)/2
  ≈ N²/2
  → O(N²)
```

**Where this appears:**
```
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

### 3-4) Logarithm Identities (for complexity analysis)

```
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

### 3-5) Powers of 2 — Quick Reference

| Expression | Value | Context |
|------------|-------|---------|
| 2^10 | 1,024 ≈ 10³ | 1K |
| 2^20 | 1,048,576 ≈ 10⁶ | 1M |
| 2^30 | 1,073,741,824 ≈ 10⁹ | 1B |
| 2^32 | 4,294,967,296 ≈ 4×10⁹ | int max |
| 2^63 | ≈ 9.2×10¹⁸ | long max |
| 2^31 - 1 | 2,147,483,647 | Integer.MAX_VALUE in Java |

**Why this matters for complexity:**
```
N = 10^9 operations → too slow for 1 second (typical limit: 10^8 ops/sec)
N = 10^6 → fine for O(N log N)
N = 10^5 → fine for O(N²)... barely
N = 20   → OK for O(2^N)
N = 12   → OK for O(N!)
```

---

### 3-6) Harmonic Series — Why 1 + 1/2 + 1/3 + ... + 1/N ≈ ln(N)

```
H(N) = 1 + 1/2 + 1/3 + ... + 1/N ≈ ln(N) ≈ O(log N)
```

**Where this appears:**
```
Sieve of Eratosthenes: O(N log log N)
  - Cross out multiples of 2: N/2 ops
  - Cross out multiples of 3: N/3 ops
  - Cross out multiples of 5: N/5 ops
  - Total ≈ N × (1/2 + 1/3 + 1/5 + ...) = N × log log N

Average case of Quick Sort partitioning ≈ O(N log N)
```

---

### 3-7) Counting Subsets and Permutations

```
Subsets of N elements:       2^N
Permutations of N elements:  N!
Combinations C(N, K):        N! / (K! × (N-K)!)

Stirling's approximation:    N! ≈ √(2πN) × (N/e)^N
→ log(N!) ≈ N log N         (explains why permutation algos are O(N log N) in log space)
```

**Classic LC:**
- LC 78 Subsets — O(N × 2^N) time, O(N × 2^N) space
- LC 46 Permutations — O(N × N!) time, O(N!) space
- LC 77 Combinations — O(K × C(N,K)) time

---

### 3-8) Recurrence Relations — Master Theorem

For `T(N) = a·T(N/b) + f(N)`:

```
Compare f(N) vs N^(log_b(a)):

Case 1: f(N) = O(N^(log_b(a) - ε))  →  T(N) = O(N^log_b(a))
Case 2: f(N) = Θ(N^log_b(a))         →  T(N) = O(N^log_b(a) × log N)
Case 3: f(N) = Ω(N^(log_b(a) + ε))  →  T(N) = O(f(N))
```

**Common examples:**

| Recurrence | a | b | f(N) | Result | Algorithm |
|------------|---|---|------|--------|-----------|
| T(N) = 2T(N/2) + N | 2 | 2 | N | O(N log N) | Merge Sort |
| T(N) = 2T(N/2) + 1 | 2 | 2 | 1 | O(N) | Tree traversal |
| T(N) = T(N/2) + 1 | 1 | 2 | 1 | O(log N) | Binary Search |
| T(N) = T(N/2) + N | 1 | 2 | N | O(N) | Certain divide & conquer |
| T(N) = 4T(N/2) + N | 4 | 2 | N | O(N²) | Naive matrix multiply |
| T(N) = 3T(N/2) + N² | 3 | 2 | N² | O(N²) | Strassen-like |

---

### 3-9) ASCII Tricks for Character Problems

```
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

## 4. Classic Google Interview Problems — Complexity Summary

### Tier 1: Must Know

| Problem | LC # | Time | Space | Key Idea |
|---------|------|------|-------|----------|
| Two Sum | 1 | O(N) | O(N) | HashMap |
| Valid Parentheses | 20 | O(N) | O(N) | Stack |
| Merge Intervals | 56 | O(N log N) | O(N) | Sort + sweep |
| LRU Cache | 146 | O(1) per op | O(N) | HashMap + DLL |
| Binary Tree Level Order | 102 | O(N) | O(W) | BFS |
| Number of Islands | 200 | O(M·N) | O(M·N) | DFS/BFS/Union-Find |
| Course Schedule | 207 | O(V+E) | O(V+E) | Topological sort |
| Clone Graph | 133 | O(V+E) | O(V) | BFS + HashMap |
| Merge K Sorted Lists | 23 | O(N log K) | O(K) | Min-heap |
| Kth Largest Element | 215 | O(N) avg | O(1) | QuickSelect |

### Tier 2: Frequently Asked

| Problem | LC # | Time | Space | Key Idea |
|---------|------|------|-------|----------|
| Longest Substring Without Repeating | 3 | O(N) | O(min(N,Σ)) | Sliding window |
| Word Ladder | 127 | O(M²·N) | O(M²·N) | BFS |
| Trapping Rain Water | 42 | O(N) | O(1) | Two pointers |
| Serialize/Deserialize Binary Tree | 297 | O(N) | O(N) | BFS or DFS |
| Find Median from Data Stream | 295 | O(log N) | O(N) | Two heaps |
| Alien Dictionary | 269 | O(C) | O(1) | Topological sort |
| Regular Expression Matching | 10 | O(M·N) | O(M·N) | DP |
| Word Break | 139 | O(N²) | O(N) | DP |
| Decode Ways | 91 | O(N) | O(1) | DP |
| Coin Change | 322 | O(N·amount) | O(amount) | DP |

### Tier 3: Hard / Follow-ups

| Problem | LC # | Time | Space | Key Idea |
|---------|------|------|-------|----------|
| Median of Two Sorted Arrays | 4 | O(log(M+N)) | O(1) | Binary search |
| Sliding Window Maximum | 239 | O(N) | O(K) | Monotonic deque |
| Largest Rectangle in Histogram | 84 | O(N) | O(N) | Monotonic stack |
| Word Search II | 212 | O(M·N·4·3^(L-1)) | O(L) | Trie + DFS |
| Minimum Window Substring | 76 | O(N+M) | O(Σ) | Sliding window |
| Binary Tree Maximum Path Sum | 124 | O(N) | O(H) | DFS post-order |
| Longest Increasing Subsequence | 300 | O(N log N) | O(N) | Patience sort |
| Jump Game II | 45 | O(N) | O(1) | Greedy |
| Text Justification | 68 | O(N·W) | O(W) | Greedy simulation |

---

## 5. Space Complexity Patterns

### Stack Space (Recursion)
```
Balanced BST / heap:     O(log N)  ← tree height
Skewed BST / linked list: O(N)     ← degenerates to linear
General graph DFS:        O(V)
```

### When to Use In-Place Algorithms
```
O(1) extra space tricks:
  - Two pointers (reverse array, palindrome)
  - Floyd's cycle detection
  - Morris tree traversal
  - Partition in-place (quick sort, Dutch flag)
  - Bit manipulation for visited flags (if N ≤ 64)
```

### Space-Time Tradeoffs
```
Problem                 | Naive Space | Optimized Space | Time Trade-off
------------------------|-------------|-----------------|---------------
DP (2D → 1D rolling)   | O(M·N)      | O(N)            | Same time
Memoization → Bottom-up| O(N) stack  | O(1) or O(N)    | Same time
Fibonacci               | O(N)        | O(1)            | Same O(N)
KMP vs Naive search     | O(M)        | —               | O(N+M) vs O(N·M)
```

---

## 6. Interview Decision Guide

```
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

## 7. Quick Sanity Checks

```
1. O(N log N) sort first → enables O(N) or O(log N) operations after
2. Two passes O(N) each = still O(N)
3. Nested loops with early break ≠ necessarily O(N²)
4. Recursion depth × work per call = space × time relationship
5. BFS uses O(W) space; DFS uses O(H) space — choose based on graph shape
6. HashMap insert/lookup = O(1) avg but O(N) worst — mention in interview
7. N + N/2 + N/4 + ... = 2N = O(N)  ← geometric series always converges!
8. Heapify = O(N),  building heap by N inserts = O(N log N)  ← different!
```
