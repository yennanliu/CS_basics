# Google SWE Coding Interview — Daily LC Review Plan

**Interview Date**: May 15, 2026
**Start Date**: February 20, 2026
**Total Days**: 85

> **Strategy**: Master patterns, not just solutions. Aim for 2–3 problems/day.
> Each phase builds on the previous. Never skip review days.

---

## Table of Contents

- [Overview & Phases](#overview--phases)
- [Phase 1: Foundation (Feb 20 – Mar 15)](#phase-1-foundation-feb-20--mar-15)
- [Phase 2: Trees & Graphs (Mar 16 – Apr 5)](#phase-2-trees--graphs-mar-16--apr-5)
- [Phase 3: DP, Backtracking & Heap (Apr 6 – Apr 20)](#phase-3-dp-backtracking--heap-apr-6--apr-20)
- [Phase 4: Design & Hard Problems (Apr 21 – May 4)](#phase-4-design--hard-problems-apr-21--may-4)
- [Phase 5: Mock Interviews & Final Review (May 5 – May 15)](#phase-5-mock-interviews--final-review-may-5--may-15)
- [Quick Reference: Key Patterns](#quick-reference-key-patterns)

---

## Overview & Phases

| Phase | Dates | Focus | Days |
|-------|-------|-------|------|
| 1 | Feb 20 – Mar 15 | Arrays, Two Pointers, Sliding Window, Binary Search, Linked Lists | 25 |
| 2 | Mar 16 – Apr 5  | Trees, BST, Graphs, BFS/DFS, Union Find, Topological Sort | 21 |
| 3 | Apr 6 – Apr 20  | Dynamic Programming, Backtracking, Heap, Greedy, Monotonic Stack | 15 |
| 4 | Apr 21 – May 4  | Design Problems, Trie, Bit Manipulation, Hard Variants | 14 |
| 5 | May 5 – May 15  | Mock Interviews, Review Weak Areas, Mental Prep | 11 |

**Daily Time Budget**:
- Weekdays: 1.5–2 hrs (2 problems)
- Weekends: 2–3 hrs (3 problems + review)

---

## Phase 1: Foundation (Feb 20 – Mar 15)

> Focus: Arrays, Two Pointers, Sliding Window, Binary Search, Linked Lists

---

### Week 1 — Two Pointers & Array Fundamentals (Feb 20–26)

| Day | Date | Topic | Problems |
|-----|------|-------|----------|
| 1 | Feb 20 (Fri) | Two Pointers Basics | LC 1 - Two Sum, LC 125 - Valid Palindrome, LC 283 - Move Zeroes |
| 2 | Feb 21 (Sat) | Two Pointers — Sorted Arrays | LC 15 - 3Sum, LC 11 - Container With Most Water |
| 3 | Feb 22 (Sun) | Two Pointers — Hard + Cycle | LC 42 - Trapping Rain Water, LC 287 - Find the Duplicate Number |
| 4 | Feb 23 (Mon) | Prefix Sum | LC 238 - Product of Array Except Self, LC 560 - Subarray Sum Equals K |
| 5 | Feb 24 (Tue) | Array Manipulation | LC 53 - Maximum Subarray, LC 48 - Rotate Image |
| 6 | Feb 25 (Wed) | Sorting-Based Array | LC 56 - Merge Intervals, LC 179 - Largest Number |
| 7 | Feb 26 (Thu) | Array Review + Hard | LC 128 - Longest Consecutive Sequence, LC 54 - Spiral Matrix |

**Week 1 Key Takeaways:**
- Two pointers reduce O(n²) to O(n) for sorted arrays
- Prefix sum enables O(1) range sum queries
- Know when to sort first vs. use a hash map

---

### Week 2 — Sliding Window & Strings (Feb 27 – Mar 5)

| Day | Date | Topic | Problems |
|-----|------|-------|----------|
| 8  | Feb 27 (Fri) | Sliding Window Basics | LC 3 - Longest Substring Without Repeating Chars, LC 424 - Longest Repeating Character Replacement |
| 9  | Feb 28 (Sat) | Sliding Window Hard | LC 76 - Minimum Window Substring, LC 239 - Sliding Window Maximum |
| 10 | Mar 1 (Sun)  | String + Hash Map | LC 49 - Group Anagrams, LC 242 - Valid Anagram |
| 11 | Mar 2 (Mon)  | Palindrome Strings | LC 5 - Longest Palindromic Substring, LC 647 - Palindromic Substrings |
| 12 | Mar 3 (Tue)  | String Hard (Google Fav) | LC 68 - Text Justification, LC 165 - Compare Version Numbers |
| 13 | Mar 4 (Wed)  | String Math | LC 43 - Multiply Strings, LC 67 - Add Binary |
| 14 | Mar 5 (Thu)  | Review + Catch-up | Revisit any problem from Week 1–2 you struggled with |

**Week 2 Key Takeaways:**
- Sliding window: expand right, shrink left — track with hash map or frequency array
- LC 76 (Min Window Substring) is a Google classic — memorize the template
- LC 239 (Sliding Window Max) uses monotonic deque

---

### Week 3 — Binary Search (Mar 6–12)

| Day | Date | Topic | Problems |
|-----|------|-------|----------|
| 15 | Mar 6 (Fri)  | Binary Search Basics | LC 704 - Binary Search, LC 33 - Search in Rotated Sorted Array |
| 16 | Mar 7 (Sat)  | Binary Search Variants | LC 153 - Find Min in Rotated Array, LC 34 - First & Last Position |
| 17 | Mar 8 (Sun)  | Binary Search Hard | LC 4 - Median of Two Sorted Arrays, LC 162 - Find Peak Element |
| 18 | Mar 9 (Mon)  | Binary Search on Answer | LC 410 - Split Array Largest Sum, LC 875 - Koko Eating Bananas |
| 19 | Mar 10 (Tue) | Binary Search + K-th | LC 215 - Kth Largest Element, LC 240 - Search a 2D Matrix II |
| 20 | Mar 11 (Wed) | Binary Search + Closest | LC 658 - Find K Closest Elements, LC 1011 - Capacity To Ship Packages |
| 21 | Mar 12 (Thu) | Review + Math | LC 50 - Pow(x, n), LC 148 - Sort List |

**Week 3 Key Takeaways:**
- Template: `lo, hi = 0, n; while lo < hi: mid = (lo+hi)//2`
- "Binary search on answer" pattern: binary search over the result space, check feasibility
- LC 4 (Median Two Sorted Arrays) is O(log(min(m,n))) — know it well

---

### Week 4 — Linked Lists (Mar 13–15)

| Day | Date | Topic | Problems |
|-----|------|-------|----------|
| 22 | Mar 13 (Fri) | Linked List Basics | LC 206 - Reverse Linked List, LC 21 - Merge Two Sorted Lists, LC 141 - Linked List Cycle |
| 23 | Mar 14 (Sat) | Linked List Advanced | LC 2 - Add Two Numbers, LC 19 - Remove Nth Node From End, LC 143 - Reorder List |
| 24 | Mar 15 (Sun) | Linked List Hard + Review | LC 25 - Reverse Nodes in k-Group, LC 142 - Linked List Cycle II, LC 234 - Palindrome Linked List |

**Week 4 Key Takeaways:**
- Fast/slow pointer detects cycles and finds midpoint
- Dummy head node simplifies edge cases
- Reverse in k-group: mark start, reverse k nodes, reconnect

---

## Phase 2: Trees & Graphs (Mar 16 – Apr 5)

> Focus: Binary Trees, BST, Graph DFS/BFS, Union Find, Topological Sort, Shortest Path

---

### Week 5 — Binary Trees (Mar 16–22)

| Day | Date | Topic | Problems |
|-----|------|-------|----------|
| 25 | Mar 16 (Mon) | Tree Basics (DFS) | LC 104 - Maximum Depth, LC 100 - Same Tree, LC 226 - Invert Binary Tree |
| 26 | Mar 17 (Tue) | Tree Path DFS | LC 112 - Path Sum, LC 113 - Path Sum II, LC 257 - Binary Tree Paths |
| 27 | Mar 18 (Wed) | Tree BFS | LC 102 - Level Order Traversal, LC 103 - Zigzag Level Order, LC 199 - Right Side View |
| 28 | Mar 19 (Thu) | Tree Properties | LC 543 - Diameter of Binary Tree, LC 110 - Balanced Binary Tree, LC 572 - Subtree of Another Tree |
| 29 | Mar 20 (Fri) | LCA & Path Sum | LC 235 - LCA of BST, LC 236 - LCA of Binary Tree, LC 437 - Path Sum III |
| 30 | Mar 21 (Sat) | Tree Hard | LC 124 - Binary Tree Maximum Path Sum, LC 297 - Serialize and Deserialize Binary Tree |
| 31 | Mar 22 (Sun) | Tree Construction | LC 105 - Construct from Preorder & Inorder, LC 106 - Construct from Inorder & Postorder |

**Week 5 Key Takeaways:**
- Most tree problems: DFS with return value pattern
- LCA: if root == p or q, return root; check left/right subtrees
- LC 124 (Max Path Sum): track local max path through each node

---

### Week 6 — BST & Advanced Trees (Mar 23–29)

| Day | Date | Topic | Problems |
|-----|------|-------|----------|
| 32 | Mar 23 (Mon) | BST Core | LC 98 - Validate BST, LC 230 - Kth Smallest in BST |
| 33 | Mar 24 (Tue) | BST Operations | LC 450 - Delete Node in BST, LC 108 - Sorted Array to BST |
| 34 | Mar 25 (Wed) | Tree + BFS Advanced | LC 662 - Maximum Width of Binary Tree, LC 863 - All Nodes Distance K |
| 35 | Mar 26 (Thu) | Tree Traversal Special | LC 987 - Vertical Order Traversal, LC 114 - Flatten Binary Tree to Linked List |
| 36 | Mar 27 (Fri) | Trie Introduction | LC 208 - Implement Trie, LC 211 - Design Add and Search Words |
| 37 | Mar 28 (Sat) | Trie + Word Search | LC 212 - Word Search II (Trie + Backtracking) |
| 38 | Mar 29 (Sun) | Trees Review Day | Revisit problems you found hardest in Week 5–6 |

**Week 6 Key Takeaways:**
- BST inorder traversal = sorted sequence
- Validate BST: pass min/max bounds through recursion
- Trie: each node has `children[26]` + `isEnd` flag

---

### Week 7 — Graphs (Mar 30 – Apr 5)

| Day | Date | Topic | Problems |
|-----|------|-------|----------|
| 39 | Mar 30 (Mon) | Graph Basics (DFS/BFS) | LC 200 - Number of Islands, LC 133 - Clone Graph |
| 40 | Mar 31 (Tue) | Graph DFS Advanced | LC 417 - Pacific Atlantic Water Flow, LC 130 - Surrounded Regions |
| 41 | Apr 1 (Wed)  | Topological Sort | LC 207 - Course Schedule, LC 210 - Course Schedule II |
| 42 | Apr 2 (Thu)  | Graph BFS | LC 127 - Word Ladder, LC 310 - Minimum Height Trees |
| 43 | Apr 3 (Fri)  | Union Find (DSU) | LC 684 - Redundant Connection, LC 547 - Number of Provinces, LC 261 - Graph Valid Tree |
| 44 | Apr 4 (Sat)  | Shortest Path (Dijkstra) | LC 743 - Network Delay Time, LC 787 - Cheapest Flights Within K Stops |
| 45 | Apr 5 (Sun)  | Graph Hard | LC 1192 - Critical Connections (Tarjan), LC 332 - Reconstruct Itinerary |

**Week 7 Key Takeaways:**
- BFS for shortest path (unweighted); Dijkstra for weighted
- Topological sort: Kahn's BFS (in-degree) or DFS (reverse finish time)
- Union Find: `find()` with path compression + `union()` with rank

---

## Phase 3: DP, Backtracking & Heap (Apr 6 – Apr 20)

> Focus: 1D/2D DP, Backtracking, Heap, Greedy, Monotonic Stack

---

### Week 8 — Dynamic Programming (Apr 6–12)

| Day | Date | Topic | Problems |
|-----|------|-------|----------|
| 46 | Apr 6 (Mon)  | 1D DP Basics | LC 70 - Climbing Stairs, LC 198 - House Robber, LC 322 - Coin Change |
| 47 | Apr 7 (Tue)  | 1D DP Advanced | LC 300 - Longest Increasing Subsequence, LC 139 - Word Break, LC 152 - Max Product Subarray |
| 48 | Apr 8 (Wed)  | 2D DP Basics | LC 62 - Unique Paths, LC 1143 - Longest Common Subsequence |
| 49 | Apr 9 (Thu)  | 2D DP Advanced | LC 72 - Edit Distance, LC 97 - Interleaving String |
| 50 | Apr 10 (Fri) | Knapsack DP | LC 416 - Partition Equal Subset Sum, LC 518 - Coin Change II |
| 51 | Apr 11 (Sat) | Stock DP Series | LC 121 (I), LC 122 (II), LC 123 (III), LC 309 (Cooldown) |
| 52 | Apr 12 (Sun) | DP Hard (Google Favs) | LC 312 - Burst Balloons, LC 10 - Regular Expression Matching |

**Week 8 Key Takeaways:**
- DP state definition is the most critical step
- 1D DP: `dp[i]` = optimal answer for first `i` elements
- 2D DP: `dp[i][j]` = optimal for subproblems on `i`, `j`
- Knapsack: iterate items outer, capacity inner

---

### Week 9 — Backtracking (Apr 13–16)

| Day | Date | Topic | Problems |
|-----|------|-------|----------|
| 53 | Apr 13 (Mon) | Backtracking Basics | LC 78 - Subsets, LC 46 - Permutations, LC 39 - Combination Sum |
| 54 | Apr 14 (Tue) | Backtracking Medium | LC 17 - Letter Combinations, LC 79 - Word Search, LC 131 - Palindrome Partitioning |
| 55 | Apr 15 (Wed) | Backtracking Hard | LC 51 - N-Queens, LC 37 - Sudoku Solver |
| 56 | Apr 16 (Thu) | Backtracking + BFS | LC 301 - Remove Invalid Parentheses, LC 212 - Word Search II |

**Week 9 Key Takeaways:**
- Backtracking template: choose → explore → unchoose
- Pruning is key for efficiency — sort input first
- N-Queens: track columns, diagonals with boolean arrays

---

### Week 10 — Heap, Greedy & Monotonic Stack (Apr 17–20)

| Day | Date | Topic | Problems |
|-----|------|-------|----------|
| 57 | Apr 17 (Fri) | Heap Basics | LC 347 - Top K Frequent Elements, LC 23 - Merge k Sorted Lists, LC 973 - K Closest Points |
| 58 | Apr 18 (Sat) | Heap Hard | LC 295 - Find Median from Data Stream, LC 218 - The Skyline Problem |
| 59 | Apr 19 (Sun) | Greedy | LC 55 - Jump Game, LC 45 - Jump Game II, LC 435 - Non-overlapping Intervals |
| 60 | Apr 20 (Mon) | Monotonic Stack | LC 739 - Daily Temperatures, LC 84 - Largest Rectangle in Histogram, LC 42 - Trapping Rain Water |

**Week 10 Key Takeaways:**
- Heap: min-heap for top-K largest; max-heap for top-K smallest
- LC 295 (Median Stream): two heaps — max-heap left, min-heap right
- Monotonic stack: think "next greater/smaller element"
- Greedy: make locally optimal choice; prove correctness by exchange argument

---

## Phase 4: Design & Hard Problems (Apr 21 – May 4)

> Focus: Data Structure Design, Trie, Bit Manipulation, Hard Google-Tagged Problems

---

### Week 11 — Design & Special Structures (Apr 21–27)

| Day | Date | Topic | Problems |
|-----|------|-------|----------|
| 61 | Apr 21 (Tue) | Design Basics | LC 146 - LRU Cache, LC 155 - Min Stack |
| 62 | Apr 22 (Wed) | Design Advanced | LC 460 - LFU Cache, LC 380 - Insert Delete GetRandom O(1) |
| 63 | Apr 23 (Thu) | Trie Design | LC 208 - Implement Trie (review), LC 642 - Design Search Autocomplete System |
| 64 | Apr 24 (Fri) | Bit Manipulation | LC 136 - Single Number, LC 137 - Single Number II, LC 338 - Counting Bits, LC 190 - Reverse Bits |
| 65 | Apr 25 (Sat) | Union Find Advanced | LC 399 - Evaluate Division, LC 990 - Satisfiability of Equality Equations |
| 66 | Apr 26 (Sun) | Advanced Graphs | LC 269 - Alien Dictionary, LC 1631 - Path With Minimum Effort |
| 67 | Apr 27 (Mon) | Graph + DP Hybrid | LC 329 - Longest Increasing Path in Matrix, LC 847 - Shortest Path Visiting All Nodes |

**Week 11 Key Takeaways:**
- LRU Cache: HashMap + Doubly Linked List → all O(1) operations
- LFU Cache: HashMap + frequency map + ordered set/linked list
- Design problems: always clarify API contract before coding

---

### Week 12 — Hard Problems & Google-Style (Apr 28 – May 4)

| Day | Date | Topic | Problems |
|-----|------|-------|----------|
| 68 | Apr 28 (Tue) | Hard DP | LC 354 - Russian Doll Envelopes, LC 1235 - Max Profit in Job Scheduling |
| 69 | Apr 29 (Wed) | Hard Graph | LC 778 - Swim in Rising Water, LC 317 - Shortest Distance from All Buildings |
| 70 | Apr 30 (Thu) | Hard Backtracking | LC 282 - Expression Add Operators, LC 126 - Word Ladder II |
| 71 | May 1 (Fri)  | Google Simulation | LC 588 - Design In-Memory File System, LC 489 - Robot Room Cleaner |
| 72 | May 2 (Sat)  | Sweep Line + Heap | LC 759 - Employee Free Time, LC 1851 - Minimum Interval to Include Each Query |
| 73 | May 3 (Sun)  | Advanced Greedy | LC 135 - Candy, LC 630 - Course Schedule III, LC 871 - Min Number of Refueling Stops |
| 74 | May 4 (Mon)  | Google Classics | LC 68 - Text Justification, LC 218 - Skyline (review), LC 149 - Max Points on a Line |

**Week 12 Key Takeaways:**
- Russian Doll Envelopes = 2D LIS; sort by width asc, height desc
- Text Justification: pure implementation — read the spec carefully
- LC 282 (Expression Add Operators): backtracking with `curr`, `prev` tracking

---

## Phase 5: Mock Interviews & Final Review (May 5 – May 15)

> Simulate real interview conditions. 45 min per problem, no hints.

---

| Day | Date | Activity | Focus / Problems |
|-----|------|----------|-----------------|
| 75 | May 5 (Tue)  | Mock Interview #1 | Arrays/Strings — pick 2 unsolved medium/hard problems, 45 min each |
| 76 | May 6 (Wed)  | Review + Weak Areas | Review missed patterns from Mock #1; LC 128 (Consecutive Seq), LC 560 (Subarray Sum K) |
| 77 | May 7 (Thu)  | Mock Interview #2 | Trees/Graphs — LC 124 + LC 207 (timed, no notes) |
| 78 | May 8 (Fri)  | Review + Trees Hard | Review LC 297 (Serialize Tree), LC 987 (Vertical Order); note edge cases |
| 79 | May 9 (Sat)  | Mock Interview #3 | DP/Backtracking — LC 312 + LC 51 (timed) |
| 80 | May 10 (Sun) | Review + Stock DP | LC 309 (Buy Sell Cooldown), LC 188 (Buy Sell IV); tabulation vs memoization |
| 81 | May 11 (Mon) | Mock Interview #4 | Mixed — 3 problems in 75 min (1 Easy, 1 Medium, 1 Hard) |
| 82 | May 12 (Tue) | Final Pattern Review | Scan Top 50 Must-Do list; write out approach for each in < 2 min |
| 83 | May 13 (Wed) | Mock Interview #5 | Full Simulation — 2 problems, 90 min, talk out loud |
| 84 | May 14 (Thu) | Light Review + Mental Prep | Review key templates (BFS, DFS, binary search, union find, DP); rest |
| 85 | May 15 (Fri) | **INTERVIEW DAY** | Trust your prep. Clarify → Brute Force → Optimize → Code → Test |

---

## Quick Reference: Key Patterns

### Templates to Memorize

**Sliding Window**
```java
int left = 0, maxLen = 0;
Map<Character, Integer> freq = new HashMap<>();
for (int right = 0; right < s.length(); right++) {
    freq.merge(s.charAt(right), 1, Integer::sum);
    while (/* window invalid */) {
        freq.merge(s.charAt(left), -1, Integer::sum);
        left++;
    }
    maxLen = Math.max(maxLen, right - left + 1);
}
```

**Binary Search**
```java
int lo = 0, hi = n - 1;
while (lo <= hi) {
    int mid = lo + (hi - lo) / 2;
    if (check(mid)) hi = mid - 1;   // or lo = mid + 1
    else lo = mid + 1;              // adjust based on problem
}
```

**BFS (Graph/Tree)**
```java
Queue<Integer> q = new LinkedList<>();
Set<Integer> visited = new HashSet<>();
q.offer(start); visited.add(start);
while (!q.isEmpty()) {
    int node = q.poll();
    for (int nei : graph.get(node)) {
        if (!visited.contains(nei)) {
            visited.add(nei);
            q.offer(nei);
        }
    }
}
```

**Union Find**
```java
int[] parent, rank;
int find(int x) {
    if (parent[x] != x) parent[x] = find(parent[x]);
    return parent[x];
}
void union(int x, int y) {
    int px = find(x), py = find(y);
    if (rank[px] < rank[py]) parent[px] = py;
    else if (rank[px] > rank[py]) parent[py] = px;
    else { parent[py] = px; rank[px]++; }
}
```

**DFS Backtracking**
```java
void backtrack(int start, List<Integer> path, List<List<Integer>> res) {
    res.add(new ArrayList<>(path));   // or: if (base case) { add; return; }
    for (int i = start; i < nums.length; i++) {
        path.add(nums[i]);
        backtrack(i + 1, path, res);  // i+1 for subsets, i for combinations with reuse
        path.remove(path.size() - 1);
    }
}
```

---

### Problem Difficulty Distribution

| Phase | Easy | Medium | Hard |
|-------|------|--------|------|
| Phase 1 (Foundation) | 8 | 22 | 5 |
| Phase 2 (Trees & Graphs) | 5 | 25 | 7 |
| Phase 3 (DP, BT, Heap) | 3 | 18 | 9 |
| Phase 4 (Design & Hard) | 2 | 12 | 14 |
| Phase 5 (Mock) | 2 | 12 | 10 |
| **Total** | **20** | **89** | **45** |

---

### Top 20 Must-Solve Problems for Google

| # | Problem | Pattern | Priority |
|---|---------|---------|----------|
| 1 | LC 76 - Minimum Window Substring | Sliding Window | ⭐⭐⭐ |
| 2 | LC 295 - Find Median from Data Stream | Two Heaps | ⭐⭐⭐ |
| 3 | LC 124 - Binary Tree Maximum Path Sum | Tree DP | ⭐⭐⭐ |
| 4 | LC 297 - Serialize & Deserialize BT | DFS/BFS | ⭐⭐⭐ |
| 5 | LC 146 - LRU Cache | Design | ⭐⭐⭐ |
| 6 | LC 72 - Edit Distance | 2D DP | ⭐⭐⭐ |
| 7 | LC 207 - Course Schedule | Topological Sort | ⭐⭐⭐ |
| 8 | LC 127 - Word Ladder | BFS | ⭐⭐⭐ |
| 9 | LC 312 - Burst Balloons | DP (Interval) | ⭐⭐⭐ |
| 10 | LC 4 - Median of Two Sorted Arrays | Binary Search | ⭐⭐⭐ |
| 11 | LC 23 - Merge k Sorted Lists | Heap | ⭐⭐⭐ |
| 12 | LC 236 - LCA of Binary Tree | DFS | ⭐⭐⭐ |
| 13 | LC 560 - Subarray Sum Equals K | Prefix Sum | ⭐⭐⭐ |
| 14 | LC 300 - Longest Increasing Subsequence | DP/BinSearch | ⭐⭐⭐ |
| 15 | LC 51 - N-Queens | Backtracking | ⭐⭐⭐ |
| 16 | LC 42 - Trapping Rain Water | Two Pointers | ⭐⭐⭐ |
| 17 | LC 218 - The Skyline Problem | Sweep + Heap | ⭐⭐⭐ |
| 18 | LC 1192 - Critical Connections | Tarjan's DFS | ⭐⭐⭐ |
| 19 | LC 68 - Text Justification | Simulation | ⭐⭐⭐ |
| 20 | LC 410 - Split Array Largest Sum | BS on Answer | ⭐⭐⭐ |

---

### Google Interview Tips

1. **Always clarify**: Ask about input size, edge cases (empty input, negatives, duplicates), expected output format
2. **Think aloud**: Say what pattern you recognize, even if you're not sure yet
3. **Brute force first**: State O(n²) solution, then optimize
4. **Test with examples**: Trace through your code with the given example before submitting
5. **Know complexity**: For every solution, state time AND space complexity
6. **Handle edge cases**: `null`, empty array, single element, all duplicates
7. **Clean code**: Meaningful variable names, no magic numbers

---

*Plan generated: February 2026 | Interview: May 15, 2026*
*Reference: LC_google_problem_patterns_summary.md, goog_swe_prep_plan_gpt_v2.md*
