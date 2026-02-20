# Google SWE Coding Interview — Daily LC Review Plan

**Interview Date**: May 15, 2026 (Friday)
**Start Date**: February 20, 2026 (Friday)
**Total Days**: 85

---

## Strategy

| Day Type | Activity |
|----------|----------|
| **Mon–Fri** | Pattern focus — 2 problems/day (~1.5–2 hrs) |
| **Saturday** | Hard variants OR Mini-Mock (timed, no hints) |
| **Sunday** | **LC Weekly Contest** — simulate with real/recent contest problems for unseen exposure |

> Patterns first. Speed comes after. Never grind volume at the cost of understanding.

---

## Mock & Contest Schedule

| Type | Frequency | Count | Purpose |
|------|-----------|-------|---------|
| LC Weekly Contest (Sun) | Every Sunday | 12 | Unseen problems, speed, adaptability |
| Mini-Mock (every 2nd Sat, Phase 1–3) | Every 2 weeks | 5 | Pattern consolidation under time pressure |
| Full Mock (Sat, Phase 4) | Weekly | 2 | Full interview simulation |
| Full Mock (Phase 5) | Near-daily | 5 | Final sharpening |
| **Total** | | **24** | |

> **LC Weekly Contest**: Participate in the real Sunday contest at leetcode.com/contest,
> OR simulate using the most recent 2–3 unread Weekly Contest problems if timing doesn't work.
> Aim to finish Q1–Q2 cleanly and attempt Q3. Track your performance each week.

---

## Overview & Phases

| Phase | Dates | Focus | Days |
|-------|-------|-------|------|
| 1 | Feb 20 – Mar 15 | Arrays, Two Pointers, Sliding Window, Binary Search, Linked Lists | 25 |
| 2 | Mar 16 – Apr 5  | Trees, BST, Graphs, BFS/DFS, Union Find, Topological Sort | 21 |
| 3 | Apr 6 – Apr 20  | Dynamic Programming, Backtracking, Heap, Greedy, Monotonic Stack | 15 |
| 4 | Apr 21 – May 4  | Design, Trie, Bit Manipulation, Hard Variants + Weekly Mocks | 14 |
| 5 | May 5 – May 15  | Daily Mocks, Weak Area Review, Final Sharpening | 11 |

---

## Phase 1: Foundation (Feb 20 – Mar 15)

> Weekdays: pattern drills | Saturdays (alt): Mini-Mocks | Sundays: LC Weekly Contest

---

### Week 1 — Two Pointers & Array Fundamentals (Feb 20–26)

| Day | Date | Type | Topic | Problems |
|-----|------|------|-------|----------|
| 1  | Feb 20 (Fri) | Pattern | Two Pointers Basics | LC 1 - Two Sum, LC 125 - Valid Palindrome, LC 283 - Move Zeroes |
| 2  | Feb 21 (Sat) | Hard Drill | Two Pointers Hard Variants | LC 15 - 3Sum, LC 42 - Trapping Rain Water |
| 3  | Feb 22 (Sun) | **🏆 LC Weekly Contest #1** | Unseen Problems | Participate in / simulate LC Weekly Contest |
| 4  | Feb 23 (Mon) | Pattern | Prefix Sum | LC 238 - Product of Array Except Self, LC 560 - Subarray Sum Equals K |
| 5  | Feb 24 (Tue) | Pattern | Array Manipulation + **Kadane's Algo** | LC 53 - Maximum Subarray *(Kadane's)*, LC 48 - Rotate Image, LC 152 - Max Product Subarray |
| 6  | Feb 25 (Wed) | Pattern | Sorting + **Interval Pattern** | LC 56 - Merge Intervals, LC 57 - Insert Interval, LC 435 - Non-overlapping Intervals |
| 7  | Feb 26 (Thu) | Pattern | Array Hard + Review | LC 128 - Longest Consecutive Sequence, LC 54 - Spiral Matrix |

**Week 1 Core Concepts:**
- Two pointers: opposite-end scan for sorted arrays; same-direction for window
- Prefix sum: `pre[i] = pre[i-1] + nums[i]` → range sum in O(1)
- **Kadane's**: `cur = max(nums[i], cur + nums[i])`; local max decides to extend or restart
- **Interval Pattern**: sort by start → merge (extend end) or insert; for scheduling sort by end (greedy)
- Know when to sort first vs. use a hash map

---

### Week 2 — Sliding Window & Strings (Feb 27 – Mar 5)

| Day | Date | Type | Topic | Problems |
|-----|------|------|-------|----------|
| 8  | Feb 27 (Fri) | Pattern | Sliding Window Basics | LC 3 - Longest Substring Without Repeating Chars, LC 424 - Longest Repeating Character Replacement |
| 9  | Feb 28 (Sat) | **🎯 Mini-Mock #1** | Arrays / Two Pointers | 2 unseen medium problems — 45 min each, no hints. Review after. |
| 10 | Mar 1 (Sun)  | **🏆 LC Weekly Contest #2** | Unseen Problems | Participate in / simulate LC Weekly Contest |
| 11 | Mar 2 (Mon)  | Pattern | Sliding Window Hard | LC 76 - Minimum Window Substring, LC 239 - Sliding Window Maximum |
| 12 | Mar 3 (Tue)  | Pattern | String + Hash Map | LC 49 - Group Anagrams, LC 242 - Valid Anagram |
| 13 | Mar 4 (Wed)  | Pattern | Palindrome Strings | LC 5 - Longest Palindromic Substring, LC 647 - Palindromic Substrings |
| 14 | Mar 5 (Thu)  | Pattern | String Hard (Google Fav) | LC 68 - Text Justification, LC 165 - Compare Version Numbers |

**Week 2 Core Concepts:**
- Sliding window: expand right → check constraint → shrink left
- LC 76 template: two pointers + freq map + `formed` counter — memorize this
- LC 239 uses monotonic deque (not heap) for O(n)

---

### Week 3 — Binary Search (Mar 6–12)

| Day | Date | Type | Topic | Problems |
|-----|------|------|-------|----------|
| 15 | Mar 6 (Fri)  | Pattern | Binary Search Basics | LC 704 - Binary Search, LC 33 - Search in Rotated Sorted Array |
| 16 | Mar 7 (Sat)  | Hard Drill | Binary Search Variants | LC 153 - Find Min in Rotated Array, LC 34 - First & Last Position |
| 17 | Mar 8 (Sun)  | **🏆 LC Weekly Contest #3** | Unseen Problems | Participate in / simulate LC Weekly Contest |
| 18 | Mar 9 (Mon)  | Pattern | Binary Search Hard | LC 4 - Median of Two Sorted Arrays, LC 162 - Find Peak Element |
| 19 | Mar 10 (Tue) | Pattern | Binary Search on Answer | LC 410 - Split Array Largest Sum, LC 875 - Koko Eating Bananas |
| 20 | Mar 11 (Wed) | Pattern | Binary Search + K-th | LC 215 - Kth Largest Element, LC 240 - Search a 2D Matrix II |
| 21 | Mar 12 (Thu) | Pattern | Binary Search Review | LC 658 - Find K Closest Elements, LC 1011 - Capacity To Ship Packages |

**Week 3 Core Concepts:**
- Invariant: at loop exit, `lo` = first position satisfying condition
- "Binary search on answer": feasibility check replaces array search
- LC 4: O(log min(m,n)) — eliminate half the smaller array each step

---

### Week 4 — Linked Lists (Mar 13–15)

| Day | Date | Type | Topic | Problems |
|-----|------|------|-------|----------|
| 22 | Mar 13 (Fri) | Pattern | Linked List Basics | LC 206 - Reverse Linked List, LC 21 - Merge Two Sorted Lists, LC 141 - Linked List Cycle |
| 23 | Mar 14 (Sat) | **🎯 Mini-Mock #2** | Sliding Window / Binary Search | 2 unseen medium/hard — 45 min each. Review pattern gaps after. |
| 24 | Mar 15 (Sun) | **🏆 LC Weekly Contest #4** | Unseen Problems | Participate in / simulate LC Weekly Contest |

> **Phase 1 wrap-up**: After contest, spend 20 min reviewing your performance log from Mocks #1–#2 and Contests #1–#4. Note weak patterns.

**Linked List Core Concepts:**
- Fast/slow pointer: cycle detection, midpoint finding
- Dummy head eliminates edge cases for head modifications
- Reverse in k-group: save `start`, reverse k, reconnect `start.next = recurse(next)`

---

## Phase 2: Trees & Graphs (Mar 16 – Apr 5)

> Each Sunday: LC Weekly Contest. Every other Saturday: Mini-Mock.

---

### Week 5 — Binary Trees (Mar 16–22)

| Day | Date | Type | Topic | Problems |
|-----|------|------|-------|----------|
| 25 | Mar 16 (Mon) | Pattern | Tree Basics (DFS) | LC 104 - Maximum Depth, LC 100 - Same Tree, LC 226 - Invert Binary Tree |
| 26 | Mar 17 (Tue) | Pattern | Tree Path DFS | LC 112 - Path Sum, LC 113 - Path Sum II, LC 257 - Binary Tree Paths |
| 27 | Mar 18 (Wed) | Pattern | Tree BFS | LC 102 - Level Order Traversal, LC 103 - Zigzag Level Order, LC 199 - Right Side View |
| 28 | Mar 19 (Thu) | Pattern | Tree Properties | LC 543 - Diameter of Binary Tree, LC 110 - Balanced Binary Tree, LC 572 - Subtree of Another Tree |
| 29 | Mar 20 (Fri) | Pattern | LCA + Path Sum | LC 235 - LCA of BST, LC 236 - LCA of Binary Tree, LC 437 - Path Sum III |
| 30 | Mar 21 (Sat) | Hard Drill | Tree Hard | LC 124 - Binary Tree Max Path Sum, LC 297 - Serialize & Deserialize Binary Tree |
| 31 | Mar 22 (Sun) | **🏆 LC Weekly Contest #5** | Unseen Problems | Participate in / simulate LC Weekly Contest |

**Week 5 Core Concepts:**
- DFS return value pattern: compute from children, aggregate at node
- LCA: return root if `root == p || root == q`; post-order left/right
- LC 124: `maxGain(node) = node.val + max(0, left) + max(0, right)` — track global max

---

### Week 6 — BST & Advanced Trees (Mar 23–29)

| Day | Date | Type | Topic | Problems |
|-----|------|------|-------|----------|
| 32 | Mar 23 (Mon) | Pattern | BST Core | LC 98 - Validate BST, LC 230 - Kth Smallest in BST |
| 33 | Mar 24 (Tue) | Pattern | BST Operations | LC 450 - Delete Node in BST, LC 108 - Sorted Array to BST |
| 34 | Mar 25 (Wed) | Pattern | Tree Advanced BFS | LC 662 - Maximum Width of Binary Tree, LC 863 - All Nodes Distance K |
| 35 | Mar 26 (Thu) | Pattern | Tree Traversal Special | LC 987 - Vertical Order Traversal, LC 114 - Flatten Binary Tree to Linked List |
| 36 | Mar 27 (Fri) | Pattern | Trie Introduction | LC 208 - Implement Trie, LC 211 - Design Add and Search Words |
| 37 | Mar 28 (Sat) | **🎯 Mini-Mock #3** | Trees / BST | 2 unseen tree problems — 45 min each. Identify if DFS/BFS/DP approach. |
| 38 | Mar 29 (Sun) | **🏆 LC Weekly Contest #6** | Unseen Problems | Participate in / simulate LC Weekly Contest |

**Week 6 Core Concepts:**
- BST inorder = sorted; validate by passing `(min, max)` bounds
- Trie node: `children[26]` array + `isEnd` boolean
- LC 987 Vertical Order: BFS with `(node, col, row)` → sort by (col, row, val)

---

### Week 7 — Graphs (Mar 30 – Apr 5)

| Day | Date | Type | Topic | Problems |
|-----|------|------|-------|----------|
| 39 | Mar 30 (Mon) | Pattern | Graph Basics DFS/BFS | LC 200 - Number of Islands, LC 133 - Clone Graph |
| 40 | Mar 31 (Tue) | Pattern | Graph DFS Advanced | LC 417 - Pacific Atlantic Water Flow, LC 130 - Surrounded Regions |
| 41 | Apr 1 (Wed)  | Pattern | Topological Sort | LC 207 - Course Schedule, LC 210 - Course Schedule II |
| 42 | Apr 2 (Thu)  | Pattern | Graph BFS + Multi-source | LC 127 - Word Ladder, LC 310 - Minimum Height Trees |
| 43 | Apr 3 (Fri)  | Pattern | Union Find (DSU) | LC 684 - Redundant Connection, LC 547 - Number of Provinces, LC 261 - Graph Valid Tree |
| 44 | Apr 4 (Sat)  | Hard Drill | Shortest Path (All Variants) | LC 743 - Network Delay (Dijkstra), LC 787 - Cheapest Flights *(Bellman-Ford K-steps)*, LC 1334 - Find City *(Floyd-Warshall)* |
| 45 | Apr 5 (Sun)  | **🏆 LC Weekly Contest #7** | Unseen Problems | Participate in / simulate LC Weekly Contest |

> **Phase 2 wrap-up**: Review contest + mock performance. List top 3 weak spots.

**Week 7 Core Concepts:**
- BFS → unweighted shortest path; Dijkstra → non-negative weighted
- **Bellman-Ford**: relax all edges V-1 times → O(V·E); use when K-hop constraint or negative weights
- **Floyd-Warshall**: `dp[i][j] = min(dp[i][j], dp[i][k]+dp[k][j])` → O(V³) all-pairs shortest path
- Topological sort: Kahn's BFS (track in-degree) — handle cycle detection
- Union Find: path compression + union by rank → near O(1) per op

---

## Phase 3: DP, Backtracking & Heap (Apr 6 – Apr 20)

---

### Week 8 — Dynamic Programming (Apr 6–12)

| Day | Date | Type | Topic | Problems |
|-----|------|------|-------|----------|
| 46 | Apr 6 (Mon)  | Pattern | 1D DP Basics | LC 70 - Climbing Stairs, LC 198 - House Robber, LC 322 - Coin Change |
| 47 | Apr 7 (Tue)  | Pattern | 1D DP Advanced | LC 300 - Longest Increasing Subsequence, LC 139 - Word Break, LC 152 - Max Product Subarray |
| 48 | Apr 8 (Wed)  | Pattern | 2D DP Basics | LC 62 - Unique Paths, LC 1143 - Longest Common Subsequence |
| 49 | Apr 9 (Thu)  | Pattern | 2D DP Advanced | LC 72 - Edit Distance, LC 97 - Interleaving String |
| 50 | Apr 10 (Fri) | Pattern | Knapsack + **Interval DP** + **Bitmask DP** | LC 416 - Partition Equal Subset Sum, LC 518 - Coin Change II, LC 312 - Burst Balloons *(Interval DP)*, LC 847 - Shortest Path All Nodes *(Bitmask DP)* |
| 51 | Apr 11 (Sat) | **🎯 Mini-Mock #4** | Graphs / DP Mixed | 2 unseen problems (1 graph, 1 DP) — 45 min each. Focus on state definition. |
| 52 | Apr 12 (Sun) | **🏆 LC Weekly Contest #8** | Unseen Problems | Participate in / simulate LC Weekly Contest; then review Stock DP: LC 121, 122, 309 |

**Week 8 Core Concepts:**
- DP: define state → recurrence → base case → iteration order
- 1D DP: `dp[i]` = best answer using first `i` elements
- Knapsack: outer loop = items, inner loop = capacity (0/1: backward; unbounded: forward)
- **Interval DP**: `dp[i][j]` = optimal answer for sub-range `[i,j]`; iterate by length, not by start
- **Bitmask DP**: state = bitmask of visited nodes/items; `dp[mask][i]` = best answer visiting nodes in `mask` ending at `i`

---

### Week 9 — Backtracking (Apr 13–16)

| Day | Date | Type | Topic | Problems |
|-----|------|------|-------|----------|
| 53 | Apr 13 (Mon) | Pattern | Backtracking Basics | LC 78 - Subsets, LC 46 - Permutations, LC 39 - Combination Sum |
| 54 | Apr 14 (Tue) | Pattern | Backtracking Medium | LC 17 - Letter Combinations of a Phone Number, LC 79 - Word Search, LC 131 - Palindrome Partitioning |
| 55 | Apr 15 (Wed) | Pattern | Backtracking Hard | LC 51 - N-Queens, LC 37 - Sudoku Solver |
| 56 | Apr 16 (Thu) | Pattern | Backtracking + BFS | LC 301 - Remove Invalid Parentheses, LC 212 - Word Search II |

**Week 9 Core Concepts:**
- Template: `choose → recurse → unchoose`
- Prune early: sort input + skip duplicates + break on invalid state
- N-Queens: boolean arrays for cols, diag1 (`r-c`), diag2 (`r+c`)

---

### Week 10 — Heap, Greedy & Monotonic Stack (Apr 17–20)

| Day | Date | Type | Topic | Problems |
|-----|------|------|-------|----------|
| 57 | Apr 17 (Fri) | Pattern | Heap Basics | LC 347 - Top K Frequent Elements, LC 23 - Merge k Sorted Lists, LC 973 - K Closest Points |
| 58 | Apr 18 (Sat) | **🎯 Mini-Mock #5** | Backtracking / DP | 2 unseen problems — 45 min each. Identify backtracking vs DP decision. |
| 59 | Apr 19 (Sun) | **🏆 LC Weekly Contest #9** | Unseen Problems | Participate in / simulate LC Weekly Contest |
| 60 | Apr 20 (Mon) | Pattern | Heap Hard + Monotonic + Greedy | LC 295 - Find Median from Data Stream, LC 739 - Daily Temperatures, LC 84 - Largest Rectangle in Histogram, LC 55 - Jump Game |

> **Phase 3 wrap-up**: Review all 5 mini-mocks. Note recurring mistakes (off-by-one, missed edge cases, wrong complexity). Prioritize in Phase 4.

**Week 10 Core Concepts:**
- Min-heap for top-K largest; max-heap for top-K smallest
- LC 295: two heaps (max-heap left half, min-heap right half) — balance sizes
- Monotonic stack: maintain decreasing/increasing stack, pop when violated
- Greedy: local optimum → global optimum; prove by exchange argument

---

## Phase 4: Design & Hard Problems (Apr 21 – May 4)

> Weekly Full Mocks on Saturdays. Weekly Contests on Sundays. Weekdays: design + hard patterns.

---

### Week 11 — Design & Special Structures (Apr 21–27)

| Day | Date | Type | Topic | Problems |
|-----|------|------|-------|----------|
| 61 | Apr 21 (Tue) | Pattern | Design Basics | LC 146 - LRU Cache, LC 155 - Min Stack |
| 62 | Apr 22 (Wed) | Pattern | Design Advanced | LC 460 - LFU Cache, LC 380 - Insert Delete GetRandom O(1) |
| 63 | Apr 23 (Thu) | Pattern | Trie + **Advanced String Algorithms** | LC 208 - Implement Trie, LC 642 - Design Autocomplete; Concepts: **KMP** (LC 28), **Rolling Hash** (LC 1044), **Manacher's** (LC 5 O(n)) |
| 64 | Apr 24 (Fri) | Pattern | Bit Manipulation + **BIT/Fenwick Tree** | LC 136, LC 137, LC 338, LC 190; **Fenwick Tree**: LC 307 - Range Sum Query Mutable |
| 65 | Apr 25 (Sat) | **🎯 Full Mock #6** | Mixed Hard (90 min) | 2 Hard problems back-to-back, 45 min each — talk aloud, write clean code |
| 66 | Apr 26 (Sun) | **🏆 LC Weekly Contest #10** | Unseen Problems | Participate in / simulate LC Weekly Contest |
| 67 | Apr 27 (Mon) | Pattern | **Sweep Line + Difference Array** | LC 253 - Meeting Rooms II *(sweep)*, LC 218 - Skyline *(sweep+heap)*, LC 759 - Employee Free Time; **Diff Array**: LC 1109 - Corporate Flight Bookings, LC 370 - Range Addition |

**Week 11 Core Concepts:**
- LRU: `HashMap<key, Node>` + doubly linked list → O(1) get/put
- LFU: 3 maps — `(key→val)`, `(key→freq)`, `(freq→LinkedHashSet<key>)`
- Design problems: clarify API → define data structures → implement → analyze edge cases
- **KMP**: build failure function `lps[]`; avoid redundant comparisons → O(n+m)
- **Manacher's**: expand around center with mirror trick → O(n) for all palindromes
- **Rolling Hash**: `hash = (hash * base + char) % mod`; compare substrings in O(1)
- **Fenwick Tree (BIT)**: point update + prefix query both O(log n); use `i += (i & -i)` to update
- **Sweep Line**: convert intervals to events `(pos, type)` → sort → process with sorted set or heap
- **Difference Array**: `diff[l] += v; diff[r+1] -= v`; reconstruct with prefix sum → O(1) range update

---

### Week 12 — Hard Problems & Google-Style (Apr 28 – May 4)

| Day | Date | Type | Topic | Problems |
|-----|------|------|-------|----------|
| 68 | Apr 28 (Tue) | Pattern | Hard DP + **Divide & Conquer** | LC 354 - Russian Doll Envelopes, LC 1235 - Max Profit in Job Scheduling; **D&C**: LC 315 - Count of Smaller Numbers After Self *(merge sort D&C)* |
| 69 | Apr 29 (Wed) | Pattern | Hard Graph + Advanced Graph | LC 778 - Swim in Rising Water, LC 329 - Longest Increasing Path in Matrix, LC 399 - Evaluate Division, LC 269 - Alien Dictionary |
| 70 | Apr 30 (Thu) | Pattern | Hard Backtracking + N-Sum Variants | LC 282 - Expression Add Operators, LC 126 - Word Ladder II; Review N-Sum pattern: 2-Sum/3-Sum/4-Sum generalization |
| 71 | May 1 (Fri)  | Pattern | Google Simulation + Advanced Intervals | LC 588 - Design In-Memory File System, LC 1631 - Path With Min Effort; **Interval advanced**: LC 1851 - Min Interval per Query |
| 72 | May 2 (Sat)  | **🎯 Full Mock #7** | Google-Style Full Sim (90 min) | 2 problems: 1 medium + 1 hard. Clarify → brute force → optimize → code → test |
| 73 | May 3 (Sun)  | **🏆 LC Weekly Contest #11** | Unseen Problems | Participate in / simulate LC Weekly Contest |
| 74 | May 4 (Mon)  | Review | Phase 4 Wrap-up + Google Classics | LC 68 - Text Justification, LC 149 - Max Points on a Line; review Phase 4 weak spots |

**Week 12 Core Concepts:**
- Russian Doll Envelopes: 2D LIS — sort by `w` asc, `h` desc → LIS on `h`
- LC 282: backtracking with `eval`, `prev` (for multiplication precedence)
- Simulation problems: read spec carefully, clarify all edge cases upfront

---

## Phase 5: Mock Interviews & Final Sharpening (May 5 – May 15)

> Simulate full Google interview conditions daily. 45 min per problem. Talk out loud.
> After each mock: identify pattern gaps → do targeted 30-min review.

---

| Day | Date | Type | Activity | Focus |
|-----|------|------|----------|-------|
| 75 | May 5 (Tue)  | **🎯 Full Mock #8**  | Arrays/Strings (unseen) | 2 medium/hard — pick from recent unseen Google-tagged problems |
| 76 | May 6 (Wed)  | Review | Weak Area Drill #1 | Fix gaps from Mock #8; revisit LC 76, LC 239, LC 560 if needed |
| 77 | May 7 (Thu)  | **🎯 Full Mock #9**  | Trees/Graphs (unseen) | 2 problems — one tree, one graph; no notes |
| 78 | May 8 (Fri)  | Review | Weak Area Drill #2 | Fix gaps from Mock #9; revisit LC 124, LC 297, LC 207 if needed |
| 79 | May 9 (Sat)  | **🎯 Full Mock #10** | DP/Backtracking (full timed) | 2 problems, 90 min total; analyze after |
| 80 | May 10 (Sun) | **🏆 LC Weekly Contest #12** | Final Unseen Contest | Last contest before interview — assess adaptability on brand new problems |
| 81 | May 11 (Mon) | **🎯 Full Mock #11** | Mixed (3 problems, 75 min) | 1 Easy + 1 Medium + 1 Hard — practice pacing across difficulty levels |
| 82 | May 12 (Tue) | Review | Pattern Template Drills | Write full approach (not code) for Top 20 problems in < 2 min each |
| 83 | May 13 (Wed) | **🎯 Full Mock #12** | Full Talk-Aloud Simulation | 2 problems, 90 min — record yourself or use a peer; focus on communication |
| 84 | May 14 (Thu) | Light Review | Template Refresh + Rest | Scan key templates (BFS, union find, DP, backtracking); NO new problems; sleep well |
| 85 | May 15 (Fri) | **INTERVIEW DAY** | Trust your prep | Clarify → Brute Force → Optimize → Code → Test |

---

## Mock Interview Protocol

Use this protocol for every mock session (Mini-Mock and Full Mock):

```
1. Read problem (2 min)
2. Clarify: input type, constraints, edge cases, expected output format
3. State brute force approach + complexity
4. Optimize: identify bottleneck → apply pattern
5. Code: write clean, readable code
6. Test: trace through with given example + 1 edge case (empty, single element, negatives)
7. State time & space complexity
```

**After each mock:**
- ✅ What did you get right?
- ❌ Where did you get stuck? (wrong pattern, off-by-one, missed edge case)
- 📝 Which template/concept to revisit?

---

## Cheatsheet Coverage Map

> Cross-reference with `doc/cheatsheet/` — all patterns below are addressed in the plan.
> Priority: ⭐ = Google frequently tests | 📌 = know the concept | 🔖 = nice-to-have

| Pattern | Cheatsheet | Covered In Plan | Priority |
|---------|-----------|-----------------|----------|
| Two Pointers | `2_pointers.md` | Week 1 | ⭐ |
| Sliding Window | `sliding_window.md` | Week 2 | ⭐ |
| Prefix Sum | `prefix_sum.md` | Week 1 | ⭐ |
| Binary Search | `binary_search.md` | Week 3 | ⭐ |
| **Kadane's Algorithm** | `kadane_algo.md` | Week 1 Day 5 | ⭐ |
| **Interval Pattern** | `intervals.md` | Week 1 Day 6, Phase 4 | ⭐ |
| **Sweep Line** | `scanning_line.md` | Phase 4 Day 67 | ⭐ |
| **Difference Array** | `difference_array.md` | Phase 4 Day 67 | 📌 |
| Linked List | `linked_list.md` | Week 4 | ⭐ |
| Tree / BST | `tree.md`, `bst.md`, `binary_tree.md` | Week 5–6 | ⭐ |
| DFS | `dfs.md` | Week 5–7 | ⭐ |
| BFS | `bfs.md` | Week 5–7 | ⭐ |
| Graph | `graph.md` | Week 7 | ⭐ |
| Topological Sort | `topology_sorting.md` | Week 7 | ⭐ |
| Union Find | `union_find.md` | Week 7 | ⭐ |
| Dijkstra | `Dijkstra.md` | Week 7 | ⭐ |
| **Bellman-Ford** | `Bellman-Ford.md` | Week 7 Day 44 | 📌 |
| **Floyd-Warshall** | `Floyd-Warshall.md` | Week 7 Day 44 | 📌 |
| DP (1D/2D) | `dp.md`, `dp_pattern.md` | Week 8 | ⭐ |
| Knapsack DP | `dp_pattern.md` | Week 8 | ⭐ |
| **Interval DP** | `dp_pattern.md` | Week 8 Day 50 | ⭐ |
| **Bitmask DP** | `dp_pattern.md` | Week 8 Day 50 | 📌 |
| Stock Trading DP | `stock_trading.md` | Week 8 | ⭐ |
| Backtracking | `backtrack.md` | Week 9 | ⭐ |
| Heap / Priority Queue | `heap.md`, `priority_queue.md` | Week 10 | ⭐ |
| Greedy | `greedy.md` | Week 10 | ⭐ |
| Monotonic Stack | `monotonic_stack.md` | Week 10 | ⭐ |
| Design | `design.md` | Phase 4 Week 11 | ⭐ |
| Trie | `trie.md` | Week 6 + Phase 4 | ⭐ |
| **Advanced Strings (KMP, Manacher's, Rolling Hash)** | `advanced_string_algorithms.md`, `string_matching_kmp_rolling_hash.md` | Phase 4 Day 63 | 📌 |
| **Binary Indexed Tree (Fenwick)** | `binary_indexed_tree.md` | Phase 4 Day 64 | 📌 |
| Segment Tree | `segment_tree.md` | *(not scheduled — know concept)* | 🔖 |
| Bit Manipulation | `bit_manipulation.md` | Phase 4 Day 64 | 📌 |
| **Divide & Conquer** | `advanced_divide_and_conquer.md` | Phase 4 Day 68 | 📌 |
| Matrix Traversal | `matrix.md` | Week 1 + Week 5 | 📌 |
| N-Sum Variants | `n_sum.md` | Week 1 + Phase 4 Day 70 | 📌 |
| Hashing / Rolling Hash | `hashing.md` | Phase 4 Day 63 | 📌 |
| Palindrome | `palindrome.md` | Week 2 + Phase 4 Day 63 | 📌 |
| String | `string.md` | Week 2 | ⭐ |
| Recursion + DP | `recursion_and_dp.md` | Week 8 | ⭐ |
| Scanning Line | `scanning_line.md` | Phase 4 Day 67 | ⭐ |

---

## Quick Reference: Key Patterns

### Templates to Memorize

**Sliding Window**
```java
int left = 0, res = 0;
Map<Character, Integer> freq = new HashMap<>();
for (int right = 0; right < s.length(); right++) {
    freq.merge(s.charAt(right), 1, Integer::sum);
    while (/* window invalid */) {
        freq.merge(s.charAt(left), -1, Integer::sum);
        left++;
    }
    res = Math.max(res, right - left + 1);
}
```

**Binary Search (on answer)**
```java
int lo = min_possible, hi = max_possible;
while (lo < hi) {
    int mid = lo + (hi - lo) / 2;
    if (feasible(mid)) hi = mid;  // find minimum feasible
    else lo = mid + 1;
}
// lo = answer
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
            visited.add(nei); q.offer(nei);
        }
    }
}
```

**Union Find**
```java
int[] parent, rank;
int find(int x) {
    if (parent[x] != x) parent[x] = find(parent[x]); // path compression
    return parent[x];
}
void union(int x, int y) {
    int px = find(x), py = find(y);
    if (px == py) return;
    if (rank[px] < rank[py]) { int t = px; px = py; py = t; }
    parent[py] = px;
    if (rank[px] == rank[py]) rank[px]++;
}
```

**DFS Backtracking**
```java
void backtrack(int start, List<Integer> path, List<List<Integer>> res) {
    if (/* base case */) { res.add(new ArrayList<>(path)); return; }
    for (int i = start; i < n; i++) {
        if (/* prune */) continue;
        path.add(nums[i]);
        backtrack(i + 1, path, res); // use i for reuse, i+1 for no-reuse
        path.remove(path.size() - 1);
    }
}
```

**Dijkstra**
```java
PriorityQueue<int[]> pq = new PriorityQueue<>(Comparator.comparingInt(a -> a[1]));
int[] dist = new int[n]; Arrays.fill(dist, Integer.MAX_VALUE);
dist[src] = 0; pq.offer(new int[]{src, 0});
while (!pq.isEmpty()) {
    int[] cur = pq.poll();
    int u = cur[0], d = cur[1];
    if (d > dist[u]) continue;
    for (int[] e : graph.get(u)) {
        int v = e[0], w = e[1];
        if (dist[u] + w < dist[v]) {
            dist[v] = dist[u] + w; pq.offer(new int[]{v, dist[v]});
        }
    }
}
```

---

## Top 20 Must-Solve Problems for Google

| # | Problem | Pattern | Why Google Loves It |
|---|---------|---------|---------------------|
| 1 | LC 76 - Minimum Window Substring | Sliding Window | Template mastery |
| 2 | LC 295 - Find Median from Data Stream | Two Heaps | Design + invariant |
| 3 | LC 124 - Binary Tree Maximum Path Sum | Tree DP | DFS return value |
| 4 | LC 297 - Serialize & Deserialize BT | DFS/BFS | System thinking |
| 5 | LC 146 - LRU Cache | Design | Data structure combo |
| 6 | LC 72 - Edit Distance | 2D DP | Classic DP state |
| 7 | LC 207 - Course Schedule | Topological Sort | Cycle detection |
| 8 | LC 127 - Word Ladder | BFS | State-space BFS |
| 9 | LC 312 - Burst Balloons | Interval DP | Hard DP insight |
| 10 | LC 4 - Median of Two Sorted Arrays | Binary Search | Reduction thinking |
| 11 | LC 23 - Merge k Sorted Lists | Heap | PQ usage |
| 12 | LC 236 - LCA of Binary Tree | DFS | Recursion clarity |
| 13 | LC 560 - Subarray Sum Equals K | Prefix Sum + HashMap | Pattern recognition |
| 14 | LC 300 - LIS | DP / Binary Search | Two approaches |
| 15 | LC 51 - N-Queens | Backtracking | Constraint modeling |
| 16 | LC 42 - Trapping Rain Water | Two Pointers / Stack | Multiple approaches |
| 17 | LC 218 - The Skyline Problem | Sweep + Heap | Hard implementation |
| 18 | LC 1192 - Critical Connections | Tarjan's DFS | Advanced graph |
| 19 | LC 68 - Text Justification | Simulation | Spec reading |
| 20 | LC 410 - Split Array Largest Sum | Binary Search on Answer | Optimization thinking |

---

## Google Interview Framework

```
Step 1 — Clarify (2 min)
  → Input/output types, constraints, edge cases (null, empty, duplicates, negatives)
  → Ask: "Can I assume the array is sorted?" / "Is the graph directed?"

Step 2 — Brute Force (1 min)
  → State O(n²) or naive approach verbally
  → "A naive approach would be... but that's O(n²) space/time"

Step 3 — Optimize (3–5 min)
  → Identify bottleneck: repeated work, redundant scans, wrong data structure
  → Apply pattern: hash map, two pointers, binary search, DP, etc.

Step 4 — Code (15–20 min)
  → Write clean, readable code
  → Meaningful variable names, no magic numbers

Step 5 — Test (3–5 min)
  → Trace through given example
  → Check edge cases: empty input, single element, all same, max/min values

Step 6 — Complexity
  → State time AND space; explain why
```

---

## Weekly Contest Performance Log

| Week | Contest # | Q1 | Q2 | Q3 | Q4 | Notes |
|------|-----------|----|----|----|----|-------|
| Feb 22 | #1 | | | | | |
| Mar 1 | #2 | | | | | |
| Mar 8 | #3 | | | | | |
| Mar 15 | #4 | | | | | |
| Mar 22 | #5 | | | | | |
| Mar 29 | #6 | | | | | |
| Apr 5 | #7 | | | | | |
| Apr 12 | #8 | | | | | |
| Apr 19 | #9 | | | | | |
| Apr 26 | #10 | | | | | |
| May 3 | #11 | | | | | |
| May 10 | #12 | | | | | |

> Track: ✅ solved | ⏱ solved but slow | ❌ did not solve | 💡 learned new pattern

---

*Last updated: February 2026 | Interview: May 15, 2026*
*Reference: LC_google_problem_patterns_summary.md, goog_swe_prep_plan_gpt_v2.md*
