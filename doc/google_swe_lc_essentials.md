# Google SWE Interview — Essential LeetCode Problems

Curated list of classic/must-know problems for Google SWE interviews, organized by topic.
Each entry includes a brief comment on **what to focus on** or **why it's classic**.

> Difficulty: 🟢 Easy · 🟡 Medium · 🔴 Hard

---

## Table of Contents

1. [Hash Tables](#1-hash-tables)
2. [Linked List · Stacks · Queues · Two Pointers / Sliding Window](#2-linked-list--stacks--queues--two-pointers--sliding-window)
3. [Binary Heaps (Priority Queues)](#3-binary-heaps-priority-queues)
4. [Arrays](#4-arrays)
5. [Binary Search](#5-binary-search)
6. [Graphs — BFS / DFS / Flood Fill](#6-graphs--bfs--dfs--flood-fill)
7. [Tree Traversals](#7-tree-traversals)
8. [Dynamic Programming](#8-dynamic-programming)
9. [Union Find (Disjoint Set)](#9-union-find-disjoint-set)
10. [Ad Hoc / String Manipulations](#10-ad-hoc--string-manipulations)
11. [Recursion · Backtracking · Greedy](#11-recursion--backtracking--greedy)
12. [Trie · Segment Tree / Fenwick Tree · Bitmasks](#12-trie--segment-tree--fenwick-tree--bitmasks)

---

## 1. Hash Tables

| # | Problem | Difficulty | Key Insight |
|---|---------|-----------|-------------|
| 1 | [Two Sum](https://leetcode.com/problems/two-sum/) | 🟢 | Store complement in map; O(n) single pass |
| 49 | [Group Anagrams](https://leetcode.com/problems/group-anagrams/) | 🟡 | Sort each string as map key (or use char-frequency tuple) |
| 128 | [Longest Consecutive Sequence](https://leetcode.com/problems/longest-consecutive-sequence/) | 🟡 | Only start counting from sequence roots; O(n) with set |
| 560 | [Subarray Sum Equals K](https://leetcode.com/problems/subarray-sum-equals-k/) | 🟡 | Prefix-sum + map: count how many times `sum - k` has appeared |
| 347 | [Top K Frequent Elements](https://leetcode.com/problems/top-k-frequent-elements/) | 🟡 | Frequency map → bucket sort by freq (O(n)) or min-heap (O(n log k)) |
| 76 | [Minimum Window Substring](https://leetcode.com/problems/minimum-window-substring/) | 🔴 | Two-map sliding window; classic hard template |
| 438 | [Find All Anagrams in a String](https://leetcode.com/problems/find-all-anagrams-in-a-string/) | 🟡 | Fixed-size window + char-frequency map comparison |
| 454 | [4Sum II](https://leetcode.com/problems/4sum-ii/) | 🟡 | Split into two halves; store pair sums of A+B, look up -(C+D) |
| 205 | [Isomorphic Strings](https://leetcode.com/problems/isomorphic-strings/) | 🟢 | Two-way bijective character mapping |
| 146 | [LRU Cache](https://leetcode.com/problems/lru-cache/) | 🟡 | HashMap + doubly-linked list for O(1) get and put |
| 460 | [LFU Cache](https://leetcode.com/problems/lfu-cache/) | 🔴 | HashMap of freq → doubly-linked list; harder variant of LRU |
| 138 | [Copy List with Random Pointer](https://leetcode.com/problems/copy-list-with-random-pointer/) | 🟡 | Map old→new nodes for O(n) deep copy |
| 706 | [Design HashMap](https://leetcode.com/problems/design-hashmap/) | 🟢 | Implement from scratch with chaining; understand collision handling |
| 525 | [Contiguous Array](https://leetcode.com/problems/contiguous-array/) | 🟡 | Replace 0→-1; longest subarray with sum 0 via prefix-sum map |
| 974 | [Subarray Sums Divisible by K](https://leetcode.com/problems/subarray-sums-divisible-by-k/) | 🟡 | Prefix sum mod K; `(a - b) % K == 0` ↔ same remainder |
| 953 | [Verifying an Alien Dictionary](https://leetcode.com/problems/verifying-an-alien-dictionary/) | 🟢 | Store order in map, compare adjacent words char by char |
| 380 | [Insert Delete GetRandom O(1)](https://leetcode.com/problems/insert-delete-getrandom-o1/) | 🟡 | HashMap + array; swap-with-last trick for O(1) delete |

---

## 2. Linked List · Stacks · Queues · Two Pointers / Sliding Window

| # | Problem | Difficulty | Key Insight |
|---|---------|-----------|-------------|
| 206 | [Reverse Linked List](https://leetcode.com/problems/reverse-linked-list/) | 🟢 | Foundation: three-pointer iterative or recursive |
| 21 | [Merge Two Sorted Lists](https://leetcode.com/problems/merge-two-sorted-lists/) | 🟢 | Dummy head simplifies edge cases |
| 19 | [Remove Nth Node From End of List](https://leetcode.com/problems/remove-nth-node-from-end-of-list/) | 🟡 | Two pointers n apart; single pass |
| 141 | [Linked List Cycle](https://leetcode.com/problems/linked-list-cycle/) | 🟢 | Floyd's tortoise-and-hare |
| 142 | [Linked List Cycle II](https://leetcode.com/problems/linked-list-cycle-ii/) | 🟡 | Floyd's extended: reset one pointer to head after meeting |
| 143 | [Reorder List](https://leetcode.com/problems/reorder-list/) | 🟡 | Find mid → reverse second half → merge interleaved |
| 20 | [Valid Parentheses](https://leetcode.com/problems/valid-parentheses/) | 🟢 | Classic stack matching |
| 155 | [Min Stack](https://leetcode.com/problems/min-stack/) | 🟡 | Keep a parallel min-tracking stack |
| 739 | [Daily Temperatures](https://leetcode.com/problems/daily-temperatures/) | 🟡 | Monotonic decreasing stack; pop when warmer day found |
| 84 | [Largest Rectangle in Histogram](https://leetcode.com/problems/largest-rectangle-in-histogram/) | 🔴 | Monotonic increasing stack; sentinel 0 at both ends |
| 85 | [Maximal Rectangle](https://leetcode.com/problems/maximal-rectangle/) | 🔴 | Build histogram row by row, apply LC 84 each time |
| 239 | [Sliding Window Maximum](https://leetcode.com/problems/sliding-window-maximum/) | 🔴 | Monotonic deque (decreasing); front is always the max |
| 3 | [Longest Substring Without Repeating Characters](https://leetcode.com/problems/longest-substring-without-repeating-characters/) | 🟡 | Classic variable-size sliding window |
| 11 | [Container With Most Water](https://leetcode.com/problems/container-with-most-water/) | 🟡 | Two pointers from edges; always move the shorter side |
| 15 | [3Sum](https://leetcode.com/problems/3sum/) | 🟡 | Sort + two pointers; careful duplicate skipping |
| 42 | [Trapping Rain Water](https://leetcode.com/problems/trapping-rain-water/) | 🔴 | Two pointers tracking left/right max; or monotonic stack |
| 23 | [Merge k Sorted Lists](https://leetcode.com/problems/merge-k-sorted-lists/) | 🔴 | Priority queue (min-heap) over list heads |

---

## 3. Binary Heaps (Priority Queues)

| # | Problem | Difficulty | Key Insight |
|---|---------|-----------|-------------|
| 215 | [Kth Largest Element in an Array](https://leetcode.com/problems/kth-largest-element-in-an-array/) | 🟡 | Min-heap of size k; or QuickSelect O(n) average |
| 347 | [Top K Frequent Elements](https://leetcode.com/problems/top-k-frequent-elements/) | 🟡 | Min-heap of size k on (freq, val) pairs |
| 295 | [Find Median from Data Stream](https://leetcode.com/problems/find-median-from-data-stream/) | 🔴 | Two heaps: max-heap (lower half) + min-heap (upper half) |
| 23 | [Merge k Sorted Lists](https://leetcode.com/problems/merge-k-sorted-lists/) | 🔴 | Min-heap of size k; pop-push per step |
| 973 | [K Closest Points to Origin](https://leetcode.com/problems/k-closest-points-to-origin/) | 🟡 | Max-heap of size k (evict farthest); or QuickSelect |
| 378 | [Kth Smallest Element in a Sorted Matrix](https://leetcode.com/problems/kth-smallest-element-in-a-sorted-matrix/) | 🟡 | Min-heap with (val, row, col); or binary search on value |
| 253 | [Meeting Rooms II](https://leetcode.com/problems/meeting-rooms-ii/) | 🟡 | Min-heap of end times; count concurrent meetings |
| 621 | [Task Scheduler](https://leetcode.com/problems/task-scheduler/) | 🟡 | Max-heap + cooling window; greedy scheduling |
| 767 | [Reorganize String](https://leetcode.com/problems/reorganize-string/) | 🟡 | Max-heap; always pick most frequent, then second most frequent |
| 502 | [IPO](https://leetcode.com/problems/ipo/) | 🔴 | Two heaps: min-heap by capital (locked projects) + max-heap by profit |
| 264 | [Ugly Number II](https://leetcode.com/problems/ugly-number-ii/) | 🟡 | Min-heap or DP with three pointers (×2, ×3, ×5) |
| 1046 | [Last Stone Weight](https://leetcode.com/problems/last-stone-weight/) | 🟢 | Max-heap simulation |
| 2542 | [Maximum Subsequence Score](https://leetcode.com/problems/maximum-subsequence-score/) | 🟡 | Sort by one dimension; min-heap to track top-k sum of other |
| 1882 | [Process Tasks Using Servers](https://leetcode.com/problems/process-tasks-using-servers/) | 🟡 | Two heaps: free servers + busy servers (time-ordered) |
| 632 | [Smallest Range Covering Elements from K Lists](https://leetcode.com/problems/smallest-range-covering-elements-from-k-lists/) | 🔴 | Min-heap across k lists + global max pointer |
| 1167 | [Minimum Cost to Connect Sticks](https://leetcode.com/problems/minimum-cost-to-connect-sticks/) | 🟡 | Greedy: always merge two smallest (Huffman coding insight) |

---

## 4. Arrays

| # | Problem | Difficulty | Key Insight |
|---|---------|-----------|-------------|
| 238 | [Product of Array Except Self](https://leetcode.com/problems/product-of-array-except-self/) | 🟡 | Left-pass × right-pass; no division, no extra space |
| 53 | [Maximum Subarray](https://leetcode.com/problems/maximum-subarray/) | 🟡 | Kadane's algorithm: reset when running sum goes negative |
| 56 | [Merge Intervals](https://leetcode.com/problems/merge-intervals/) | 🟡 | Sort by start; merge when next.start ≤ current.end |
| 57 | [Insert Interval](https://leetcode.com/problems/insert-interval/) | 🟡 | Binary search or linear scan for overlap region |
| 54 | [Spiral Matrix](https://leetcode.com/problems/spiral-matrix/) | 🟡 | Shrink four boundaries layer by layer |
| 73 | [Set Matrix Zeroes](https://leetcode.com/problems/set-matrix-zeroes/) | 🟡 | Use first row/col as flags for O(1) extra space |
| 31 | [Next Permutation](https://leetcode.com/problems/next-permutation/) | 🟡 | Find rightmost ascent, swap with next greater, reverse suffix |
| 41 | [First Missing Positive](https://leetcode.com/problems/first-missing-positive/) | 🔴 | Index-as-hash: place each num at its correct index |
| 48 | [Rotate Image](https://leetcode.com/problems/rotate-image/) | 🟡 | Transpose then reverse each row (or four-way swap) |
| 169 | [Majority Element](https://leetcode.com/problems/majority-element/) | 🟢 | Boyer-Moore voting; O(1) space |
| 229 | [Majority Element II](https://leetcode.com/problems/majority-element-ii/) | 🟡 | Extended Boyer-Moore with two candidates |
| 189 | [Rotate Array](https://leetcode.com/problems/rotate-array/) | 🟡 | Triple reverse trick for O(1) space |
| 152 | [Maximum Product Subarray](https://leetcode.com/problems/maximum-product-subarray/) | 🟡 | Track both max and min (negatives flip sign) |
| 287 | [Find the Duplicate Number](https://leetcode.com/problems/find-the-duplicate-number/) | 🟡 | Floyd's cycle detection on implicit linked list (index → value) |
| 75 | [Sort Colors](https://leetcode.com/problems/sort-colors/) | 🟡 | Dutch national flag: three-pointer single pass |
| 560 | [Subarray Sum Equals K](https://leetcode.com/problems/subarray-sum-equals-k/) | 🟡 | Prefix-sum + hash map (also listed in Hash Tables) |
| 918 | [Maximum Sum Circular Subarray](https://leetcode.com/problems/maximum-sum-circular-subarray/) | 🟡 | max(Kadane, total\_sum − min\_subarray); handle all-negative edge case |

---

## 5. Binary Search

| # | Problem | Difficulty | Key Insight |
|---|---------|-----------|-------------|
| 704 | [Binary Search](https://leetcode.com/problems/binary-search/) | 🟢 | Foundation: get the loop/boundary conditions exactly right |
| 33 | [Search in Rotated Sorted Array](https://leetcode.com/problems/search-in-rotated-sorted-array/) | 🟡 | One half is always sorted; decide which half to search |
| 153 | [Find Minimum in Rotated Sorted Array](https://leetcode.com/problems/find-minimum-in-rotated-sorted-array/) | 🟡 | Compare mid to right boundary |
| 162 | [Find Peak Element](https://leetcode.com/problems/find-peak-element/) | 🟡 | Move toward the higher neighbor; always finds a peak |
| 74 | [Search a 2D Matrix](https://leetcode.com/problems/search-a-2d-matrix/) | 🟡 | Treat as flattened sorted array; map index back to (row, col) |
| 875 | [Koko Eating Bananas](https://leetcode.com/problems/koko-eating-bananas/) | 🟡 | Binary search on answer space; feasibility check |
| 1011 | [Capacity to Ship Packages Within D Days](https://leetcode.com/problems/capacity-to-ship-packages-within-d-days/) | 🟡 | Same pattern as Koko: search on capacity |
| 410 | [Split Array Largest Sum](https://leetcode.com/problems/split-array-largest-sum/) | 🔴 | Binary search on max-sum; greedy feasibility |
| 34 | [Find First and Last Position in Sorted Array](https://leetcode.com/problems/find-first-and-last-position-of-element-in-sorted-array/) | 🟡 | Two binary searches: leftmost and rightmost |
| 4 | [Median of Two Sorted Arrays](https://leetcode.com/problems/median-of-two-sorted-arrays/) | 🔴 | Binary search on partition point; O(log(min(m,n))) |
| 981 | [Time Based Key-Value Store](https://leetcode.com/problems/time-based-key-value-store/) | 🟡 | Binary search on timestamps per key (sorted by insertion) |
| 378 | [Kth Smallest Element in a Sorted Matrix](https://leetcode.com/problems/kth-smallest-element-in-a-sorted-matrix/) | 🟡 | Binary search on value + count elements ≤ mid |
| 2616 | [Minimize the Maximum Difference of Pairs](https://leetcode.com/problems/minimize-the-maximum-difference-of-pairs/) | 🟡 | Binary search on answer + greedy pairing |
| 69 | [Sqrt(x)](https://leetcode.com/problems/sqrtx/) | 🟢 | Binary search for largest k where k² ≤ x |
| 240 | [Search a 2D Matrix II](https://leetcode.com/problems/search-a-2d-matrix-ii/) | 🟡 | Start from top-right: eliminate row or column each step |
| 658 | [Find K Closest Elements](https://leetcode.com/problems/find-k-closest-elements/) | 🟡 | Binary search for left boundary of the k-window |
| 1283 | [Find the Smallest Divisor Given a Threshold](https://leetcode.com/problems/find-the-smallest-divisor-given-a-threshold/) | 🟡 | Binary search on divisor; classic "search on answer" |

---

## 6. Graphs — BFS / DFS / Flood Fill

| # | Problem | Difficulty | Key Insight |
|---|---------|-----------|-------------|
| 200 | [Number of Islands](https://leetcode.com/problems/number-of-islands/) | 🟡 | DFS/BFS flood fill; mark visited in-place |
| 695 | [Max Area of Island](https://leetcode.com/problems/max-area-of-island/) | 🟡 | Flood fill + size accumulator |
| 994 | [Rotting Oranges](https://leetcode.com/problems/rotting-oranges/) | 🟡 | Multi-source BFS from all rotten oranges simultaneously |
| 133 | [Clone Graph](https://leetcode.com/problems/clone-graph/) | 🟡 | BFS/DFS with old→new node map |
| 207 | [Course Schedule](https://leetcode.com/problems/course-schedule/) | 🟡 | Cycle detection in directed graph (DFS 3-color or Kahn's BFS) |
| 210 | [Course Schedule II](https://leetcode.com/problems/course-schedule-ii/) | 🟡 | Topological sort (Kahn's or DFS post-order) |
| 417 | [Pacific Atlantic Water Flow](https://leetcode.com/problems/pacific-atlantic-water-flow/) | 🟡 | Reverse BFS from both oceans; find intersection |
| 127 | [Word Ladder](https://leetcode.com/problems/word-ladder/) | 🔴 | BFS on word graph; wildcard key preprocessing for speed |
| 743 | [Network Delay Time](https://leetcode.com/problems/network-delay-time/) | 🟡 | Dijkstra's SSSP; master priority-queue Dijkstra template |
| 787 | [Cheapest Flights Within K Stops](https://leetcode.com/problems/cheapest-flights-within-k-stops/) | 🟡 | Bellman-Ford with K relaxation rounds; or modified Dijkstra |
| 1091 | [Shortest Path in Binary Matrix](https://leetcode.com/problems/shortest-path-in-binary-matrix/) | 🟡 | BFS on 8-directional grid |
| 269 | [Alien Dictionary](https://leetcode.com/problems/alien-dictionary/) | 🔴 | Build order graph from adjacent words → topological sort |
| 323 | [Number of Connected Components](https://leetcode.com/problems/number-of-connected-components-in-an-undirected-graph/) | 🟡 | DFS/BFS or Union Find; count components |
| 721 | [Accounts Merge](https://leetcode.com/problems/accounts-merge/) | 🟡 | Union Find (or DFS) to group emails; sort at end |
| 827 | [Making A Large Island](https://leetcode.com/problems/making-a-large-island/) | 🔴 | Label each island + size; try flipping each 0 |
| 1376 | [Time Needed to Inform All Employees](https://leetcode.com/problems/time-needed-to-inform-all-employees/) | 🟡 | DFS/BFS on tree; propagate max inform time down |
| 1584 | [Min Cost to Connect All Points](https://leetcode.com/problems/min-cost-to-connect-all-points/) | 🟡 | Prim's or Kruskal's MST on complete graph |

---

## 7. Tree Traversals

| # | Problem | Difficulty | Key Insight |
|---|---------|-----------|-------------|
| 102 | [Binary Tree Level Order Traversal](https://leetcode.com/problems/binary-tree-level-order-traversal/) | 🟡 | BFS; record queue size at start of each level |
| 199 | [Binary Tree Right Side View](https://leetcode.com/problems/binary-tree-right-side-view/) | 🟡 | BFS: take last element per level (or DFS preorder right-first) |
| 104 | [Maximum Depth of Binary Tree](https://leetcode.com/problems/maximum-depth-of-binary-tree/) | 🟢 | DFS: 1 + max(left, right) |
| 543 | [Diameter of Binary Tree](https://leetcode.com/problems/diameter-of-binary-tree/) | 🟢 | DFS: update global max with left+right at each node |
| 226 | [Invert Binary Tree](https://leetcode.com/problems/invert-binary-tree/) | 🟢 | Recursive swap children |
| 98 | [Validate Binary Search Tree](https://leetcode.com/problems/validate-binary-search-tree/) | 🟡 | Pass min/max bounds down; inorder produces sorted sequence |
| 230 | [Kth Smallest Element in a BST](https://leetcode.com/problems/kth-smallest-element-in-a-bst/) | 🟡 | Inorder traversal; iterative for O(H+k) |
| 105 | [Construct Binary Tree from Preorder and Inorder Traversal](https://leetcode.com/problems/construct-binary-tree-from-preorder-and-inorder-traversal/) | 🟡 | First preorder element is root; find in inorder to split |
| 124 | [Binary Tree Maximum Path Sum](https://leetcode.com/problems/binary-tree-maximum-path-sum/) | 🔴 | DFS returns max single-side gain; update global with both sides |
| 236 | [Lowest Common Ancestor of a Binary Tree](https://leetcode.com/problems/lowest-common-ancestor-of-a-binary-tree/) | 🟡 | Post-order: LCA is node where p and q split |
| 297 | [Serialize and Deserialize Binary Tree](https://leetcode.com/problems/serialize-and-deserialize-binary-tree/) | 🔴 | BFS or preorder DFS with null markers |
| 1448 | [Count Good Nodes in Binary Tree](https://leetcode.com/problems/count-good-nodes-in-binary-tree/) | 🟡 | DFS passing running max; node is good if val ≥ max |
| 173 | [Binary Search Tree Iterator](https://leetcode.com/problems/binary-search-tree-iterator/) | 🟡 | Iterative inorder with explicit stack |
| 114 | [Flatten Binary Tree to Linked List](https://leetcode.com/problems/flatten-binary-tree-to-linked-list/) | 🟡 | Post-order; attach right subtree after leftmost leaf |
| 337 | [House Robber III](https://leetcode.com/problems/house-robber-iii/) | 🟡 | Post-order DFS returning (rob, skip) pair per node |
| 222 | [Count Complete Tree Nodes](https://leetcode.com/problems/count-complete-tree-nodes/) | 🟡 | Compare left/right heights to exploit completeness; O(log²n) |
| 652 | [Find Duplicate Subtrees](https://leetcode.com/problems/find-duplicate-subtrees/) | 🟡 | Serialize subtrees as strings; count in hash map |

---

## 8. Dynamic Programming

| # | Problem | Difficulty | Key Insight |
|---|---------|-----------|-------------|
| 70 | [Climbing Stairs](https://leetcode.com/problems/climbing-stairs/) | 🟢 | Fibonacci recurrence; canonical DP warmup |
| 322 | [Coin Change](https://leetcode.com/problems/coin-change/) | 🟡 | Bottom-up unbounded knapsack: dp[i] = min over coins |
| 1143 | [Longest Common Subsequence](https://leetcode.com/problems/longest-common-subsequence/) | 🟡 | 2D DP; match → dp[i-1][j-1]+1, else max(skip one) |
| 300 | [Longest Increasing Subsequence](https://leetcode.com/problems/longest-increasing-subsequence/) | 🟡 | O(n log n) patience sorting with binary search |
| 72 | [Edit Distance](https://leetcode.com/problems/edit-distance/) | 🔴 | 2D DP: insert/delete/replace → min of three neighbors |
| 416 | [Partition Equal Subset Sum](https://leetcode.com/problems/partition-equal-subset-sum/) | 🟡 | 0/1 knapsack: can we reach target sum? |
| 139 | [Word Break](https://leetcode.com/problems/word-break/) | 🟡 | dp[i] = any dp[j] where s[j:i] in dictionary |
| 91 | [Decode Ways](https://leetcode.com/problems/decode-ways/) | 🟡 | Count valid decodings; handle leading zeros carefully |
| 309 | [Best Time to Buy and Sell Stock with Cooldown](https://leetcode.com/problems/best-time-to-buy-and-sell-stock-with-cooldown/) | 🟡 | State-machine DP: held, sold, rest |
| 312 | [Burst Balloons](https://leetcode.com/problems/burst-balloons/) | 🔴 | Interval DP: think of last balloon burst in range |
| 1143 | [Longest Common Subsequence](https://leetcode.com/problems/longest-common-subsequence/) | 🟡 | Foundation for many string DP problems |
| 152 | [Maximum Product Subarray](https://leetcode.com/problems/maximum-product-subarray/) | 🟡 | Track max and min simultaneously |
| 647 | [Palindromic Substrings](https://leetcode.com/problems/palindromic-substrings/) | 🟡 | Expand-around-center or DP[i][j]; Manacher's for O(n) |
| 5 | [Longest Palindromic Substring](https://leetcode.com/problems/longest-palindromic-substring/) | 🟡 | Same approach as LC 647 |
| 494 | [Target Sum](https://leetcode.com/problems/target-sum/) | 🟡 | DFS with memo, or reframe as 0/1 knapsack |
| 518 | [Coin Change II](https://leetcode.com/problems/coin-change-ii/) | 🟡 | Count ways (unbounded knapsack); order matters vs LC 322 |
| 1235 | [Maximum Profit in Job Scheduling](https://leetcode.com/problems/maximum-profit-in-job-scheduling/) | 🔴 | Sort by end time; binary search for last non-overlapping job |

---

## 9. Union Find (Disjoint Set)

| # | Problem | Difficulty | Key Insight |
|---|---------|-----------|-------------|
| 684 | [Redundant Connection](https://leetcode.com/problems/redundant-connection/) | 🟡 | Adding the edge that first creates a cycle |
| 323 | [Number of Connected Components](https://leetcode.com/problems/number-of-connected-components-in-an-undirected-graph/) | 🟡 | Count roots; decrement on successful union |
| 261 | [Graph Valid Tree](https://leetcode.com/problems/graph-valid-tree/) | 🟡 | n-1 edges + no cycle = tree |
| 721 | [Accounts Merge](https://leetcode.com/problems/accounts-merge/) | 🟡 | Union emails in same account; group by root |
| 947 | [Most Stones Removed with Same Row or Column](https://leetcode.com/problems/most-stones-removed-with-same-row-or-column/) | 🟡 | Union row/col indices; answer = stones − components |
| 990 | [Satisfiability of Equality Equations](https://leetcode.com/problems/satisfiability-of-equality-equations/) | 🟡 | Process == first (union), then verify != |
| 1202 | [Smallest String With Swaps](https://leetcode.com/problems/smallest-string-with-swaps/) | 🟡 | Union swap pairs; sort chars within each component |
| 1319 | [Number of Operations to Make Network Connected](https://leetcode.com/problems/number-of-operations-to-make-network-connected/) | 🟡 | Count extra edges and disconnected components |
| 1579 | [Remove Max Number of Edges to Keep Graph Fully Traversable](https://leetcode.com/problems/remove-max-number-of-edges-to-keep-graph-fully-traversable/) | 🔴 | Two Union-Finds (Alice + Bob); add type-3 edges first |
| 778 | [Swim in Rising Water](https://leetcode.com/problems/swim-in-rising-water/) | 🔴 | Binary search + union find; or Dijkstra on grid |
| 1971 | [Find if Path Exists in Graph](https://leetcode.com/problems/find-if-path-exists-in-graph/) | 🟢 | Simplest Union Find or BFS/DFS |
| 952 | [Largest Component Size by Common Factor](https://leetcode.com/problems/largest-component-size-by-common-factor/) | 🔴 | Union number with each of its factors |
| 399 | [Evaluate Division](https://leetcode.com/problems/evaluate-division/) | 🟡 | Weighted Union Find or DFS on ratio graph |
| 1061 | [Lexicographically Smallest Equivalent String](https://leetcode.com/problems/lexicographically-smallest-equivalent-string/) | 🟡 | Union chars; root of each component = lexicographic min |
| 200 | [Number of Islands](https://leetcode.com/problems/number-of-islands/) | 🟡 | Alternative to DFS; great Union Find practice |
| 803 | [Bricks Falling When Hit](https://leetcode.com/problems/bricks-falling-when-hit/) | 🔴 | Reverse time: add bricks back; union with virtual top node |

---

## 10. Ad Hoc / String Manipulations

| # | Problem | Difficulty | Key Insight |
|---|---------|-----------|-------------|
| 125 | [Valid Palindrome](https://leetcode.com/problems/valid-palindrome/) | 🟢 | Two pointers; skip non-alphanumeric |
| 680 | [Valid Palindrome II](https://leetcode.com/problems/valid-palindrome-ii/) | 🟢 | Allow one delete; try skipping left or right on mismatch |
| 151 | [Reverse Words in a String](https://leetcode.com/problems/reverse-words-in-a-string/) | 🟡 | Split on whitespace; reverse array; rejoin |
| 8 | [String to Integer (atoi)](https://leetcode.com/problems/string-to-integer-atoi/) | 🟡 | Careful sign/overflow handling; finite state machine approach |
| 13 | [Roman to Integer](https://leetcode.com/problems/roman-to-integer/) | 🟢 | When current < next, subtract; else add |
| 12 | [Integer to Roman](https://leetcode.com/problems/integer-to-roman/) | 🟡 | Greedy: pre-build value-symbol pairs in descending order |
| 38 | [Count and Say](https://leetcode.com/problems/count-and-say/) | 🟡 | Iterative string construction with run-length encoding |
| 14 | [Longest Common Prefix](https://leetcode.com/problems/longest-common-prefix/) | 🟢 | Sort and compare first vs. last (differ most) |
| 271 | [Encode and Decode Strings](https://leetcode.com/problems/encode-and-decode-strings/) | 🟡 | Length-prefix encoding; handle delimiter in content |
| 28 | [Find the Index of the First Occurrence in a String](https://leetcode.com/problems/find-the-index-of-the-first-occurrence-in-a-string/) | 🟢 | Know KMP; implement brute force + understand O(n·m) vs O(n+m) |
| 443 | [String Compression](https://leetcode.com/problems/string-compression/) | 🟡 | Two-pointer in-place write |
| 49 | [Group Anagrams](https://leetcode.com/problems/group-anagrams/) | 🟡 | Sort-key or char-count-key grouping |
| 22 | [Generate Parentheses](https://leetcode.com/problems/generate-parentheses/) | 🟡 | Backtracking with open/close counters |
| 6 | [Zigzag Conversion](https://leetcode.com/problems/zigzag-conversion/) | 🟡 | Simulate row assignment; direction toggle |
| 165 | [Compare Version Numbers](https://leetcode.com/problems/compare-version-numbers/) | 🟡 | Split on `.`; compare integer segments |
| 394 | [Decode String](https://leetcode.com/problems/decode-string/) | 🟡 | Stack of (count, built_string); unwind on `]` |
| 1071 | [Greatest Common Divisor of Strings](https://leetcode.com/problems/greatest-common-divisor-of-strings/) | 🟢 | s+t == t+s iff GCD string exists; length = gcd(len) |

---

## 11. Recursion · Backtracking · Greedy

| # | Problem | Difficulty | Key Insight |
|---|---------|-----------|-------------|
| 78 | [Subsets](https://leetcode.com/problems/subsets/) | 🟡 | Choose/skip each element; 2ⁿ subsets |
| 90 | [Subsets II](https://leetcode.com/problems/subsets-ii/) | 🟡 | Sort + skip duplicates at same recursion depth |
| 46 | [Permutations](https://leetcode.com/problems/permutations/) | 🟡 | Swap in-place or use visited array |
| 39 | [Combination Sum](https://leetcode.com/problems/combination-sum/) | 🟡 | Allow reuse; prune when remaining < 0 |
| 40 | [Combination Sum II](https://leetcode.com/problems/combination-sum-ii/) | 🟡 | Sort + skip duplicate starts at same level |
| 79 | [Word Search](https://leetcode.com/problems/word-search/) | 🟡 | DFS + backtrack; mark visited in-place then restore |
| 131 | [Palindrome Partitioning](https://leetcode.com/problems/palindrome-partitioning/) | 🟡 | Backtrack over cut points; check palindrome per substring |
| 51 | [N-Queens](https://leetcode.com/problems/n-queens/) | 🔴 | Track col / diagonal / anti-diagonal sets; place one per row |
| 37 | [Sudoku Solver](https://leetcode.com/problems/sudoku-solver/) | 🔴 | Backtrack cell-by-cell; track row/col/box constraint sets |
| 55 | [Jump Game](https://leetcode.com/problems/jump-game/) | 🟡 | Greedy: track farthest reachable index |
| 45 | [Jump Game II](https://leetcode.com/problems/jump-game-ii/) | 🟡 | Greedy BFS layers: extend range, count jumps at layer end |
| 134 | [Gas Station](https://leetcode.com/problems/gas-station/) | 🟡 | Total gain ≥ 0 → solution exists; start where running sum first went negative |
| 452 | [Minimum Number of Arrows to Burst Balloons](https://leetcode.com/problems/minimum-number-of-arrows-to-burst-balloons/) | 🟡 | Sort by end; shoot at current end, skip all overlapping |
| 435 | [Non-overlapping Intervals](https://leetcode.com/problems/non-overlapping-intervals/) | 🟡 | Sort by end; greedy keep non-overlapping (interval scheduling) |
| 406 | [Queue Reconstruction by Height](https://leetcode.com/problems/queue-reconstruction-by-height/) | 🟡 | Sort tallest first; insert each at index k |
| 135 | [Candy](https://leetcode.com/problems/candy/) | 🔴 | Two passes: left→right then right→left; take max at each pos |
| 17 | [Letter Combinations of a Phone Number](https://leetcode.com/problems/letter-combinations-of-a-phone-number/) | 🟡 | Backtracking over digit→letters mapping |

---

## 12. Trie · Segment Tree / Fenwick Tree · Bitmasks

### Trie

| # | Problem | Difficulty | Key Insight |
|---|---------|-----------|-------------|
| 208 | [Implement Trie](https://leetcode.com/problems/implement-trie-prefix-tree/) | 🟡 | Build the data structure; know insert/search/startsWith |
| 211 | [Design Add and Search Words](https://leetcode.com/problems/design-add-and-search-words-data-structure/) | 🟡 | Trie + DFS for wildcard `.` matching |
| 212 | [Word Search II](https://leetcode.com/problems/word-search-ii/) | 🔴 | Build trie of words; DFS on grid, prune via trie |
| 648 | [Replace Words](https://leetcode.com/problems/replace-words/) | 🟡 | Insert roots in trie; replace each word with shortest prefix match |
| 720 | [Longest Word in Dictionary](https://leetcode.com/problems/longest-word-in-dictionary/) | 🟡 | Trie + BFS/DFS; only traverse nodes where word is complete |
| 421 | [Maximum XOR of Two Numbers in an Array](https://leetcode.com/problems/maximum-xor-of-two-numbers-in-an-array/) | 🟡 | Binary trie; greedily choose opposite bit |
| 1268 | [Search Suggestions System](https://leetcode.com/problems/search-suggestions-system/) | 🟡 | Sort + binary search (or trie); return top 3 lexicographic |

### Segment Tree / Fenwick Tree (BIT)

| # | Problem | Difficulty | Key Insight |
|---|---------|-----------|-------------|
| 307 | [Range Sum Query - Mutable](https://leetcode.com/problems/range-sum-query-mutable/) | 🟡 | Fenwick tree or segment tree; O(log n) update and query |
| 315 | [Count of Smaller Numbers After Self](https://leetcode.com/problems/count-of-smaller-numbers-after-self/) | 🔴 | BIT with coordinate compression; or merge sort |
| 493 | [Reverse Pairs](https://leetcode.com/problems/reverse-pairs/) | 🔴 | BIT or merge sort; count during merge step |
| 327 | [Count of Range Sum](https://leetcode.com/problems/count-of-range-sum/) | 🔴 | Prefix sums + merge sort or segment tree |
| 218 | [The Skyline Problem](https://leetcode.com/problems/the-skyline-problem/) | 🔴 | Sweep line + max-heap (or segment tree); critical points |

### Bitmasks

| # | Problem | Difficulty | Key Insight |
|---|---------|-----------|-------------|
| 136 | [Single Number](https://leetcode.com/problems/single-number/) | 🟢 | XOR cancels pairs; a ⊕ a = 0 |
| 137 | [Single Number II](https://leetcode.com/problems/single-number-ii/) | 🟡 | Count bits mod 3; or two-variable state machine |
| 260 | [Single Number III](https://leetcode.com/problems/single-number-iii/) | 🟡 | XOR all → get a⊕b → use any set bit to partition |
| 191 | [Number of 1 Bits](https://leetcode.com/problems/number-of-1-bits/) | 🟢 | n & (n-1) clears lowest set bit; count iterations |
| 338 | [Counting Bits](https://leetcode.com/problems/counting-bits/) | 🟢 | dp[i] = dp[i >> 1] + (i & 1) |
| 371 | [Sum of Two Integers](https://leetcode.com/problems/sum-of-two-integers/) | 🟡 | Half-adder: sum = a⊕b, carry = (a&b)<<1; iterate |
| 268 | [Missing Number](https://leetcode.com/problems/missing-number/) | 🟢 | XOR 0..n with array elements; remainder is missing |
| 1239 | [Maximum Length of a Concatenated String with Unique Characters](https://leetcode.com/problems/maximum-length-of-a-concatenated-string-with-unique-characters/) | 🟡 | Encode each string as 26-bit mask; backtrack or DP |
| 847 | [Shortest Path Visiting All Nodes](https://leetcode.com/problems/shortest-path-visiting-all-nodes/) | 🔴 | BFS with state = (node, visited_bitmask) |

---

## Quick Reference — Difficulty Breakdown

| Topic | Easy | Medium | Hard | Total |
|-------|------|--------|------|-------|
| Hash Tables | 5 | 9 | 3 | 17 |
| Linked List/Stack/Queue/Two Ptr | 4 | 8 | 5 | 17 |
| Binary Heaps | 1 | 11 | 4 | 16 |
| Arrays | 2 | 13 | 2 | 17 |
| Binary Search | 3 | 12 | 2 | 17 |
| Graphs BFS/DFS | 0 | 13 | 4 | 17 |
| Tree Traversals | 3 | 11 | 3 | 17 |
| Dynamic Programming | 1 | 12 | 4 | 17 |
| Union Find | 1 | 11 | 4 | 16 |
| Ad Hoc / Strings | 6 | 10 | 1 | 17 |
| Recursion/Backtrack/Greedy | 0 | 12 | 5 | 17 |
| Trie | 0 | 5 | 2 | 7 |
| Segment Tree / Fenwick | 0 | 1 | 4 | 5 |
| Bitmasks | 3 | 5 | 1 | 9 |

---

*Last updated: 2026-05-16*
