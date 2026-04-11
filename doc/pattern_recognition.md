# Pattern Recognition Guide

> See a keyword in the problem? Jump to the right pattern instantly.
> This page maps **problem signals** to **algorithm patterns** — the fastest way to crack coding interviews.

---

## How to Use This Page

1. Read the problem statement
2. Spot keywords or structural clues below
3. Jump to the matching pattern and its template

---

## Keyword → Pattern Map

### Array / String Keywords

| Keyword / Clue in Problem | Pattern | Example Problems | Cheat Sheet |
|---------------------------|---------|-----------------|-------------|
| "subarray sum equals k", "sum of subarray" | **Prefix Sum** | LC 560, 974, 523, 1248 | [prefix_sum](cheatsheets/prefix_sum.html) |
| "contiguous subarray", "maximum subarray" | **Kadane's / Sliding Window** | LC 53, 918, 152 | [kadane](cheatsheets/kadane.html) |
| "longest substring without repeating", "at most k distinct" | **Sliding Window** | LC 3, 159, 340, 424, 76 | [sliding_window](cheatsheets/sliding_window.html) |
| "minimum window", "smallest subarray" | **Sliding Window (shrink)** | LC 76, 209, 862 | [sliding_window](cheatsheets/sliding_window.html) |
| "sorted array", "find target", "search" | **Binary Search** | LC 704, 33, 153, 34 | [binary_search](cheatsheets/binary_search.html) |
| "minimum/maximum possible value", "capacity", "can you achieve" | **Binary Search on Answer** | LC 875, 1011, 410, 1283 | [binary_search](cheatsheets/binary_search.html) |
| "two sum", "pair with sum", "three sum" | **Two Pointers / HashMap** | LC 1, 15, 167, 18 | [2_pointers](cheatsheets/2_pointers.html) |
| "remove duplicates", "move zeroes", "sort colors" | **Two Pointers (in-place)** | LC 26, 283, 75, 27 | [2_pointers](cheatsheets/2_pointers.html) |
| "merge intervals", "overlapping intervals" | **Interval Sorting** | LC 56, 57, 253, 435 | [intervals](cheatsheets/intervals.html) |
| "next greater element", "daily temperatures" | **Monotonic Stack** | LC 496, 739, 84, 42 | [monotonic_stack](cheatsheets/monotonic_stack.html) |
| "median", "top k", "kth largest" | **Heap / Quick Select** | LC 215, 295, 347, 973 | [heap](cheatsheets/heap.html) |
| "difference", "range update", "increment subarray" | **Difference Array** | LC 370, 1109, 1094 | [difference_array](cheatsheets/difference_array.html) |

### Graph / Tree Keywords

| Keyword / Clue in Problem | Pattern | Example Problems | Cheat Sheet |
|---------------------------|---------|-----------------|-------------|
| "number of islands", "connected components", "flood fill" | **BFS / DFS on Grid** | LC 200, 695, 733, 130 | [bfs](cheatsheets/bfs.html), [dfs](cheatsheets/dfs.html) |
| "shortest path" (unweighted) | **BFS** | LC 127, 752, 994, 1091 | [bfs](cheatsheets/bfs.html) |
| "shortest path" (weighted, non-negative) | **Dijkstra** | LC 743, 787, 1631 | [Dijkstra](cheatsheets/Dijkstra.html) |
| "shortest path" (negative weights) | **Bellman-Ford** | LC 787 (k stops) | [graph](cheatsheets/graph.html) |
| "course schedule", "prerequisites", "ordering" | **Topological Sort** | LC 207, 210, 269, 1136 | [topology_sorting](cheatsheets/topology_sorting.html) |
| "detect cycle" (directed) | **Topological Sort / DFS coloring** | LC 207, 802 | [topology_sorting](cheatsheets/topology_sorting.html) |
| "detect cycle" (undirected) | **Union-Find** | LC 684, 685 | [union_find](cheatsheets/union_find.html) |
| "connected", "groups", "redundant connection" | **Union-Find** | LC 547, 684, 721, 1319 | [union_find](cheatsheets/union_find.html) |
| "lowest common ancestor", "LCA" | **DFS / Binary Lifting** | LC 236, 235, 1644 | [tree](cheatsheets/tree.html) |
| "level order", "zigzag", "right side view" | **BFS (level-by-level)** | LC 102, 103, 199, 515 | [bfs](cheatsheets/bfs.html) |
| "serialize/deserialize tree" | **BFS or Preorder DFS** | LC 297, 449 | [tree](cheatsheets/tree.html) |
| "diameter", "max path sum" | **DFS (post-order + global max)** | LC 543, 124, 687 | [tree](cheatsheets/tree.html) |
| "validate BST", "inorder" | **DFS (in-order traversal)** | LC 98, 230, 99 | [bst](cheatsheets/bst.html) |

### Dynamic Programming Keywords

| Keyword / Clue in Problem | Pattern | Example Problems | Cheat Sheet |
|---------------------------|---------|-----------------|-------------|
| "minimum/maximum cost", "minimum number of" | **DP (optimization)** | LC 322, 64, 120, 746 | [dp](cheatsheets/dp.html) |
| "how many ways", "number of ways", "count paths" | **DP (counting)** | LC 62, 63, 70, 96 | [dp](cheatsheets/dp.html) |
| "can you", "is it possible", "true or false" | **DP (feasibility) / Backtracking** | LC 139, 416, 97, 55 | [dp](cheatsheets/dp.html) |
| "longest increasing subsequence" | **DP + Binary Search** | LC 300, 354, 1143 | [dp](cheatsheets/dp.html) |
| "edit distance", "transform", "common subsequence" | **Two-String Grid DP** | LC 72, 1143, 97, 115 | [dp](cheatsheets/dp.html) |
| "palindrome partitioning", "palindrome subsequence" | **Palindrome DP** | LC 5, 131, 516, 647 | [palindrome](cheatsheets/palindrome.html) |
| "knapsack", "subset sum", "partition equal" | **0/1 Knapsack DP** | LC 416, 494, 474, 1049 | [dp](cheatsheets/dp.html) |
| "coin change", "combinations that sum to" | **Unbounded Knapsack DP** | LC 322, 518, 377 | [dp](cheatsheets/dp.html) |
| "buy and sell stock" | **State Machine DP** | LC 121, 122, 123, 188, 309 | [stock_trading](cheatsheets/stock_trading.html) |
| "word break", "can segment" | **DP + Trie/HashSet** | LC 139, 140, 472 | [dp](cheatsheets/dp.html) |
| "house robber", "non-adjacent" | **Linear DP (take/skip)** | LC 198, 213, 337, 740 | [dp](cheatsheets/dp.html) |

### Backtracking / Recursion Keywords

| Keyword / Clue in Problem | Pattern | Example Problems | Cheat Sheet |
|---------------------------|---------|-----------------|-------------|
| "all combinations", "all permutations", "generate all" | **Backtracking** | LC 46, 47, 77, 78, 90 | [backtrack](cheatsheets/backtrack.html) |
| "subsets", "power set" | **Backtracking / Bit Manipulation** | LC 78, 90, 784 | [backtrack](cheatsheets/backtrack.html) |
| "word search", "exist in board" | **Backtracking (grid)** | LC 79, 212 | [backtrack](cheatsheets/backtrack.html) |
| "generate parentheses", "valid combinations" | **Backtracking (pruning)** | LC 22, 17, 39 | [backtrack](cheatsheets/backtrack.html) |
| "N-Queens", "Sudoku" | **Backtracking (constraint)** | LC 51, 37 | [backtrack](cheatsheets/backtrack.html) |

### Design / Data Structure Keywords

| Keyword / Clue in Problem | Pattern | Example Problems | Cheat Sheet |
|---------------------------|---------|-----------------|-------------|
| "implement LRU cache" | **HashMap + Doubly Linked List** | LC 146 | [design](cheatsheets/design.html) |
| "implement LFU cache" | **HashMap + Min-Heap / Freq Map** | LC 460 | [design](cheatsheets/design.html) |
| "implement trie", "prefix search", "autocomplete" | **Trie** | LC 208, 211, 212, 648 | [trie](cheatsheets/trie.html) |
| "random", "shuffle", "pick random" | **Reservoir Sampling / HashMap** | LC 380, 384, 398 | [design](cheatsheets/design.html) |
| "iterator", "flatten", "next/hasNext" | **Stack-based Iterator** | LC 173, 341, 284 | [iterator](cheatsheets/iterator.html) |
| "stream", "moving average", "sliding window median" | **Heap / Queue** | LC 295, 346, 480 | [heap](cheatsheets/heap.html) |

### Greedy Keywords

| Keyword / Clue in Problem | Pattern | Example Problems | Cheat Sheet |
|---------------------------|---------|-----------------|-------------|
| "jump game", "minimum jumps" | **Greedy (furthest reach)** | LC 55, 45, 1024 | [greedy](cheatsheets/greedy.html) |
| "assign cookies", "meeting rooms" | **Greedy (sort + scan)** | LC 455, 253, 435 | [greedy](cheatsheets/greedy.html) |
| "task scheduler", "reorganize string" | **Greedy + Heap** | LC 621, 767 | [greedy](cheatsheets/greedy.html) |
| "gas station", "circular" | **Greedy (running sum)** | LC 134 | [greedy](cheatsheets/greedy.html) |

### Bit Manipulation Keywords

| Keyword / Clue in Problem | Pattern | Example Problems | Cheat Sheet |
|---------------------------|---------|-----------------|-------------|
| "single number", "appears once" | **XOR** | LC 136, 137, 260 | [bit_manipulation](cheatsheets/bit_manipulation.html) |
| "power of two", "count bits" | **Bit tricks** | LC 191, 231, 338 | [bit_manipulation](cheatsheets/bit_manipulation.html) |
| "subsets" (alternative) | **Bitmask enumeration** | LC 78, 784 | [bit_manipulation](cheatsheets/bit_manipulation.html) |

---

## Decision Flowchart

```
Is input SORTED or can you sort it?
├── YES → Is it "find target"? → Binary Search
│         Is it "find pair"? → Two Pointers
│         Is it "merge intervals"? → Sort + Scan
│         Is it "find min/max possible"? → Binary Search on Answer
│
├── NO → Is it a GRAPH/GRID?
│        ├── Unweighted shortest path → BFS
│        ├── Weighted shortest path → Dijkstra
│        ├── Dependencies/ordering → Topological Sort
│        ├── Connected components → Union-Find / DFS
│        └── All paths / combinations → DFS + Backtracking
│
├── Is it asking for OPTIMIZATION (min/max)?
│   ├── Overlapping subproblems → DP
│   ├── Local choice = global optimal → Greedy
│   └── Not sure → Try Greedy first, verify with DP
│
├── Is it asking for ALL combinations/permutations?
│   └── Backtracking
│
├── Is it a SUBARRAY/SUBSTRING problem?
│   ├── Sum related → Prefix Sum or Sliding Window
│   ├── Longest/shortest with condition → Sliding Window
│   └── Next greater/smaller → Monotonic Stack
│
└── Is it a DESIGN problem?
    ├── Cache → HashMap + LinkedList
    ├── Prefix operations → Trie
    └── Stream/online → Heap or Queue
```

---

## Quick Pattern Comparison

| When you see TWO patterns that could work | How to choose |
|------------------------------------------|---------------|
| BFS vs DFS | BFS for shortest path / level-order; DFS for exhaustive search / path finding |
| DP vs Greedy | Greedy if local optimal = global optimal; DP if choices depend on subproblems |
| DP vs Backtracking | DP for counting / optimization; Backtracking for enumerating all solutions |
| HashMap vs Sorting | HashMap for O(n) time, O(n) space; Sorting for O(n log n) time, O(1) space |
| Binary Search vs Two Pointers | Binary Search on sorted single array; Two Pointers for pair/triplet problems |
| Union-Find vs DFS | Union-Find for dynamic connectivity; DFS for static graph traversal |
| Heap vs Sorting | Heap for streaming / top-k; Sorting when you need full order |
| Prefix Sum vs Sliding Window | Prefix Sum for any subarray sum query; Sliding Window for contiguous with constraint |
