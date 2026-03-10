# Google SWE Interview Prep Plan (Claude Edition)
> Start Date: 2026-03-10 | Target Interview: ~2026-04-24 (1.5 months)

---

## Strategy Overview

### Anti-Overfitting Approach
- **Do NOT** grind the same 150 problems 5 times each
- **DO** rotate across new variants every week via LC Weekly Contests
- Each Sunday = LC Weekly Contest day (fresh unseen problems = real interview simulation)
- Final 2 weeks = mixed problem sets, NOT topic-specific drilling

### Phase Structure
| Phase | Dates | Focus |
|-------|-------|-------|
| Phase 1 | Mar 10–23 | Arrays, Strings, Binary Search, Linked Lists |
| Phase 2 | Mar 24–Apr 6 | Trees, BST, Graphs, Union-Find |
| Phase 3 | Apr 7–20 | DP, Backtracking, Heap, Stack, Bit Manipulation |
| Phase 4 | Apr 21–23 | Design, Mock Interviews, Final Review |

### Daily Target
- Weekday: 3–4 LC problems (2 medium, 1 easy warm-up, 1 hard stretch)
- Sunday: LC Weekly Contest (4 unseen problems, timed 90 min)
- Monday after contest: Review contest solutions + identify weak spots

---

## Core Topics Checklist

### Must-Master Patterns
- [ ] Array / Hash Map
- [ ] Sliding Window
- [ ] Two Pointers / Fast-Slow Pointers
- [ ] Prefix Sum
- [ ] Binary Search (standard + on-answer)
- [ ] Linked List (reverse, cycle, k-group)
- [ ] Binary Tree DFS/BFS (traversal, path, LCA)
- [ ] BST (validate, kth, range)
- [ ] Graph BFS/DFS (islands, flood fill, connected components)
- [ ] Topological Sort (course schedule, alien dictionary)
- [ ] Union-Find (DSU)
- [ ] Shortest Path (Dijkstra, Bellman-Ford)
- [ ] DP 1D (knapsack, LIS, house robber)
- [ ] DP 2D (grid path, LCS, edit distance)
- [ ] DP Intervals (burst balloons, matrix chain)
- [ ] Backtracking (subsets, permutations, N-Queens)
- [ ] Heap / Priority Queue (top-K, median stream)
- [ ] Monotonic Stack / Queue (next greater, histogram)
- [ ] Trie (prefix tree, word search)
- [ ] Greedy (intervals, jump game)
- [ ] Bit Manipulation (XOR, masks)
- [ ] System Design (LRU, LFU, data streams)

---

## Daily Plan

### PHASE 1: Foundations (Mar 10–23)

---

**2026-03-10**: Phase 1 kickoff — Array & Hash Map basics
- Topics: Array, Hash Map, Counting
- LC: 1 (Two Sum), 242 (Valid Anagram), 49 (Group Anagrams), 128 (Longest Consecutive Sequence)

**2026-03-11**: Sliding Window
- Topics: Fixed/variable window, character frequency map
- LC: 3 (Longest Substring No Repeat), 424 (Longest Repeating Char Replace), 438 (Find All Anagrams), 76 (Min Window Substring)

**2026-03-12**: Two Pointers
- Topics: Opposite ends, sorted input, dedup
- LC: 15 (3Sum), 11 (Container With Most Water), 42 (Trapping Rain Water), 125 (Valid Palindrome)

**2026-03-13**: Prefix Sum
- Topics: Cumulative sum, prefix+hashmap trick
- LC: 238 (Product Except Self), 560 (Subarray Sum=K), 525 (Contiguous Array), 523 (Continuous Subarray Sum)

**2026-03-14**: String Processing
- Topics: Palindrome expand, rolling hash, string comparison
- LC: 5 (Longest Palindromic Substring), 647 (Palindromic Substrings), 387 (First Unique Char), 165 (Compare Version Numbers)

**2026-03-15 (Sun)**: LC Weekly Contest — participate live, 90 min timed
- Strategy: attempt all 4 problems; focus on Q1–Q2 first, attempt Q3
- After: note which patterns appeared

**2026-03-16**: Review + Array Hard
- Topics: Matrix traversal, spiral, in-place rotation
- LC: 54 (Spiral Matrix), 48 (Rotate Image), 53 (Maximum Subarray), 134 (Gas Station)

---

**2026-03-17**: Binary Search — Standard
- Topics: Left/right boundary, rotated array, peak
- LC: 704 (Binary Search), 33 (Rotated Sorted Array), 153 (Min in Rotated), 162 (Find Peak Element)

**2026-03-18**: Binary Search — On Answer
- Topics: Feasibility function, min/max capacity
- LC: 875 (Koko Bananas), 1011 (Ship Packages D Days), 410 (Split Array Largest Sum), 34 (First/Last Position)

**2026-03-19**: Linked List — Basics
- Topics: Reverse, merge, cycle detection
- LC: 206 (Reverse Linked List), 21 (Merge Two Sorted), 141 (Linked List Cycle), 19 (Remove Nth from End)

**2026-03-20**: Linked List — Advanced
- Topics: k-group reverse, palindrome, reorder
- LC: 25 (Reverse Nodes k-Group), 143 (Reorder List), 234 (Palindrome Linked List), 142 (Cycle II)

**2026-03-21**: Sorting + Merge
- Topics: Merge intervals, sort colors, merge sort linked list
- LC: 56 (Merge Intervals), 57 (Insert Interval), 75 (Sort Colors), 148 (Sort List)

**2026-03-22 (Sun)**: LC Weekly Contest — 90 min timed
- After: identify which Week 2 patterns showed up

**2026-03-23**: Review Week 1–2 + Weak Spot Drill
- Revisit 2–3 problems you struggled with in Week 1–2
- LC: 287 (Find Duplicate — Floyd's), 215 (Kth Largest — Quickselect), 4 (Median Two Sorted Arrays)

---

### PHASE 2: Trees & Graphs (Mar 24–Apr 6)

---

**2026-03-24**: Binary Tree — Fundamentals
- Topics: DFS recursion, depth, path
- LC: 104 (Max Depth), 100 (Same Tree), 226 (Invert Tree), 112 (Path Sum), 543 (Diameter)

**2026-03-25**: Binary Tree — Level Order / BFS
- Topics: BFS queue, right side view, zigzag
- LC: 102 (Level Order), 103 (Zigzag Level Order), 199 (Right Side View), 662 (Max Width)

**2026-03-26**: Binary Tree — Hard DFS
- Topics: Max path sum, LCA, serialize/deserialize
- LC: 124 (Max Path Sum), 236 (LCA Binary Tree), 297 (Serialize/Deserialize), 437 (Path Sum III)

**2026-03-27**: BST
- Topics: In-order property, validate, kth, LCA BST
- LC: 98 (Validate BST), 230 (Kth Smallest), 235 (LCA BST), 450 (Delete Node BST), 538 (BST to Greater Tree)

**2026-03-28**: Advanced Trees
- Topics: Construct from traversal, vertical order, all-dist-K
- LC: 105 (Build from Pre+In), 106 (Build from In+Post), 987 (Vertical Order Traversal), 863 (All Nodes Dist K)

**2026-03-29 (Sun)**: LC Weekly Contest — 90 min timed

**2026-03-30**: Review Week 3 + Tree Mock
- Self-mock: pick 2 medium tree problems, solve without hints in 45 min total
- LC: 572 (Subtree of Another), 1457 (Pseudo-Palindromic Paths), 114 (Flatten to Linked List)

---

**2026-03-31**: Graph — BFS/DFS Basics
- Topics: Flood fill, connected components, visited matrix
- LC: 200 (Number of Islands), 695 (Max Area of Island), 733 (Flood Fill), 130 (Surrounded Regions)

**2026-04-01**: Graph — BFS Shortest Path
- Topics: Multi-source BFS, word ladder, rotting oranges
- LC: 127 (Word Ladder), 994 (Rotting Oranges), 1162 (As Far From Land), 863 (All Nodes Distance K)

**2026-04-02**: Graph — Topological Sort
- Topics: Kahn's BFS, DFS cycle detection, course prereq, alien dict
- LC: 207 (Course Schedule), 210 (Course Schedule II), 269 (Alien Dictionary), 329 (Longest Inc Path Matrix)

**2026-04-03**: Union-Find (DSU)
- Topics: Path compression, union by rank, connected components
- LC: 547 (Number of Provinces), 684 (Redundant Connection), 261 (Graph Valid Tree), 990 (Equality Equations)

**2026-04-04**: Graph — Shortest Path
- Topics: Dijkstra with heap, Bellman-Ford, BFS weighted
- LC: 743 (Network Delay Time), 787 (Cheapest Flights K Stops), 1631 (Path Min Effort), 778 (Swim Rising Water)

**2026-04-05 (Sun)**: LC Weekly Contest — 90 min timed

**2026-04-06**: Review Week 4 + Graph Mock
- Self-mock: 1 medium graph + 1 medium tree under 30 min each
- LC: 417 (Pacific Atlantic), 310 (Min Height Trees), 1584 (Min Cost Connect Points)

---

### PHASE 3: DP, Backtracking, Heap, Advanced (Apr 7–20)

---

**2026-04-07**: DP — 1D Fundamentals
- Topics: Climbing stairs pattern, house robber, LIS
- LC: 70 (Climbing Stairs), 198 (House Robber), 322 (Coin Change), 300 (LIS), 152 (Max Product Subarray)

**2026-04-08**: DP — 2D Grid
- Topics: Path counting, min path sum, maximal square
- LC: 62 (Unique Paths), 64 (Min Path Sum), 63 (Unique Paths II), 221 (Maximal Square), 304 (Range Sum 2D)

**2026-04-09**: DP — Sequences / Strings
- Topics: LCS, edit distance, interleaving
- LC: 1143 (LCS), 72 (Edit Distance), 97 (Interleaving String), 115 (Distinct Subsequences), 516 (Longest Palindromic Subseq)

**2026-04-10**: DP — Knapsack Variants
- Topics: 0/1 knapsack, unbounded knapsack, subset sum
- LC: 416 (Partition Equal Subset Sum), 518 (Coin Change II), 474 (Ones and Zeroes), 494 (Target Sum), 139 (Word Break)

**2026-04-11**: DP — Hard / Interval DP
- Topics: Burst balloons, matrix chain, scramble string
- LC: 312 (Burst Balloons), 132 (Palindrome Partitioning II), 188 (Stock IV), 354 (Russian Doll Envelopes), 1235 (Max Profit Job Schedule)

**2026-04-12 (Sun)**: LC Weekly Contest — 90 min timed

**2026-04-13**: Review Week 5 + DP Mock
- Self-mock: 2 DP mediums without hints, 45 min each
- LC: 91 (Decode Ways), 279 (Perfect Squares), 309 (Stock with Cooldown)

---

**2026-04-14**: Backtracking — Core
- Topics: Subsets, permutations, combination sum
- LC: 78 (Subsets), 46 (Permutations), 39 (Combination Sum), 131 (Palindrome Partitioning), 77 (Combinations)

**2026-04-15**: Backtracking — Hard
- Topics: N-Queens, word search II (Trie+BT), expression operators
- LC: 51 (N-Queens), 212 (Word Search II), 301 (Remove Invalid Parentheses), 679 (24 Game), 282 (Expression Add Operators)

**2026-04-16**: Heap / Priority Queue
- Topics: Top-K, two heaps for median, k-merge, scheduling
- LC: 347 (Top K Frequent), 23 (Merge K Lists), 295 (Median from Stream), 973 (K Closest Points), 218 (Skyline Problem)

**2026-04-17**: Monotonic Stack / Queue
- Topics: Next greater element, largest rectangle, sliding max
- LC: 739 (Daily Temperatures), 84 (Largest Rect Histogram), 42 (Trapping Rain Water — stack), 239 (Sliding Window Max), 907 (Sum Subarray Minimums)

**2026-04-18**: Bit Manipulation + Math + Greedy
- Topics: XOR tricks, bit counting, interval greedy
- LC: 136 (Single Number), 191 (Number of 1 Bits), 338 (Counting Bits), 55 (Jump Game), 45 (Jump Game II), 435 (Non-overlapping Intervals)

**2026-04-19 (Sun)**: LC Weekly Contest — 90 min timed

**2026-04-20**: Review Week 6 + Mixed Mock
- Full 45-min mock: 2 problems from different topics
- LC: 763 (Partition Labels), 621 (Task Scheduler), 1675 (Minimize Deviation)

---

### PHASE 4: Design + Mock Interviews + Final Polish (Apr 21–23)

---

**2026-04-21**: Trie + Data Structure Design
- Topics: Prefix tree, LRU, LFU, autocomplete
- LC: 208 (Implement Trie), 211 (Design Add/Search Words), 146 (LRU Cache), 460 (LFU Cache), 642 (Design Autocomplete)
- Review: system-level design patterns (HashMap internals, doubly linked list + hashmap combo)

**2026-04-22**: Full Mock Interview Day
- Morning: 2×45 min sessions (simulate real interview: think aloud, write code, test)
  - Session 1 pick: 1 medium graph + 1 medium DP
  - Session 2 pick: 1 hard tree + 1 medium array
- Afternoon: review + note communication gaps
- LC (for self-check): 1192 (Critical Connections), 126 (Word Ladder II), 847 (Shortest Path All Nodes), 980 (Unique Paths III)

**2026-04-23**: Final Review + Behavioral + Light Coding
- Morning: review STAR stories for behavioral (impact, teamwork, ambiguity)
- Afternoon: light review of 5–10 "sticky" problems you always forget
- LC (optional, low-pressure): 380 (Insert Delete GetRandom), 341 (Flatten Nested Iterator), 173 (BST Iterator)

---

## LC Weekly Contest Schedule

| Date (Sunday) | Contest | Notes |
|---|---|---|
| 2026-03-15 | Weekly Contest | Phase 1 check |
| 2026-03-22 | Weekly Contest | Phase 1 finish |
| 2026-03-29 | Weekly Contest | Phase 2 mid |
| 2026-04-05 | Weekly Contest | Phase 2 finish |
| 2026-04-12 | Weekly Contest | Phase 3 mid |
| 2026-04-19 | Weekly Contest | Phase 3 finish |

**Why LC Weekly?**
- Forces you to solve problems you've **never seen** under time pressure
- Prevents pattern-matching on memorized solutions
- Mirrors real Google interview conditions (you won't have seen the exact problem)
- Rating feedback shows whether you're actually improving

---

## Topic Reference: Key Problems by Pattern

### Arrays & Strings
| Problem | Pattern | Difficulty |
|---|---|---|
| LC 1 - Two Sum | Hash Map | Easy |
| LC 3 - Longest Substr No Repeat | Sliding Window | Medium |
| LC 42 - Trapping Rain Water | Two Pointers / Mono Stack | Hard |
| LC 76 - Min Window Substring | Sliding Window | Hard |
| LC 128 - Longest Consecutive Seq | Hash Set | Medium |
| LC 560 - Subarray Sum = K | Prefix Sum + Hash | Medium |

### Binary Search
| Problem | Pattern | Difficulty |
|---|---|---|
| LC 33 - Search Rotated Array | Modified BS | Medium |
| LC 4 - Median Two Sorted Arrays | BS on partition | Hard |
| LC 875 - Koko Bananas | BS on answer | Medium |
| LC 410 - Split Array Largest Sum | BS on answer | Hard |

### Trees
| Problem | Pattern | Difficulty |
|---|---|---|
| LC 124 - Max Path Sum | Tree DP DFS | Hard |
| LC 236 - LCA Binary Tree | Post-order DFS | Medium |
| LC 297 - Serialize/Deserialize | BFS / DFS | Hard |
| LC 987 - Vertical Order | BFS + coordinate | Hard |

### Graphs
| Problem | Pattern | Difficulty |
|---|---|---|
| LC 207 - Course Schedule | Topo Sort / Cycle | Medium |
| LC 269 - Alien Dictionary | Topo Sort | Hard |
| LC 743 - Network Delay | Dijkstra | Medium |
| LC 1192 - Critical Connections | Tarjan's | Hard |

### Dynamic Programming
| Problem | Pattern | Difficulty |
|---|---|---|
| LC 72 - Edit Distance | 2D DP | Medium |
| LC 300 - LIS | DP + BS | Medium |
| LC 312 - Burst Balloons | Interval DP | Hard |
| LC 1143 - LCS | 2D DP | Medium |

### Backtracking
| Problem | Pattern | Difficulty |
|---|---|---|
| LC 46 - Permutations | BT | Medium |
| LC 51 - N-Queens | BT | Hard |
| LC 212 - Word Search II | Trie + BT | Hard |
| LC 301 - Remove Invalid Parens | BFS / BT | Hard |

### Heap
| Problem | Pattern | Difficulty |
|---|---|---|
| LC 23 - Merge K Lists | Min Heap | Hard |
| LC 295 - Median from Stream | Two Heaps | Hard |
| LC 218 - Skyline Problem | Sweep + Heap | Hard |
| LC 347 - Top K Frequent | Max Heap / Bucket | Medium |

---

## Complexity Quick Reference

| Pattern | Time | Space |
|---|---|---|
| Sliding Window | O(N) | O(K) |
| Two Pointers | O(N) | O(1) |
| Binary Search | O(log N) | O(1) |
| BFS/DFS | O(V+E) | O(V) |
| Dijkstra | O((V+E) log V) | O(V) |
| Union-Find | O(N α(N)) ≈ O(N) | O(N) |
| Topo Sort | O(V+E) | O(V) |
| Heap Push/Pop | O(log N) | O(N) |
| DP 1D | O(N) | O(N) or O(1) |
| DP 2D | O(N×M) | O(N×M) or O(M) |
| Backtracking subsets | O(N × 2^N) | O(N) stack |
| Backtracking perms | O(N × N!) | O(N) stack |
| Monotonic Stack | O(N) amortized | O(N) |
| Trie insert/search | O(L) per word | O(N×L) |

---

## Interview Day Checklist

### Coding Round
- [ ] Clarify constraints first (size, duplicates, edge cases)
- [ ] State approach + complexity before coding
- [ ] Write clean code with meaningful variable names
- [ ] Test with: empty input, single element, duplicates, max size
- [ ] Mention trade-offs if asked

### Behavioral (Google L4/L5 focus)
- [ ] Prepare 3–4 STAR stories: ownership, ambiguity, conflict, impact
- [ ] Know your largest technical contribution in detail
- [ ] Quantify impact (reduced latency by X%, improved throughput Y%)

### Red Flags to Avoid
- Jumping to code without discussing approach
- Silent for >30 seconds (narrate your thinking)
- Not testing your own solution
- Forgetting to handle null/edge cases
