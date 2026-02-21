# LeetCode Weekly Contest Classics — By Pattern

> **How to read this guide:**
> Each entry shows: contest number, problem list, and "Why Classic" — the key insight that makes the problem a great pattern trainer.

---

## Table of Contents

1. [Sliding Window (Variable Size)](#1-sliding-window-variable-size)
2. [Two Pointers](#2-two-pointers)
3. [Binary Search on Answer](#3-binary-search-on-answer)
4. [Prefix Sum](#4-prefix-sum)
5. [Monotonic Stack](#5-monotonic-stack)
6. [Monotonic Queue + Sliding Window Deque](#6-monotonic-queue--sliding-window-deque)
7. [Dynamic Programming (1D / 2D)](#7-dynamic-programming-1d--2d)
8. [DP + Bitmask](#8-dp--bitmask)
9. [BFS / Multi-Source BFS](#9-bfs--multi-source-bfs)
10. [DFS + Memoization](#10-dfs--memoization)
11. [Graph: Topological Sort](#11-graph-topological-sort)
12. [Graph: Shortest Path (Dijkstra / BFS with State)](#12-graph-shortest-path-dijkstra--bfs-with-state)
13. [Union Find (Offline Processing)](#13-union-find-offline-processing)
14. [Heap / Priority Queue](#14-heap--priority-queue)
15. [Greedy + Sorting](#15-greedy--sorting)
16. [Segment Tree / Binary Indexed Tree](#16-segment-tree--binary-indexed-tree)
17. [Tree: LCA / Rerooting DP](#17-tree-lca--rerooting-dp)
18. [Trie](#18-trie)
19. [Backtracking](#19-backtracking)
20. [Math / Bit Manipulation](#20-math--bit-manipulation)
21. [String: KMP / Rolling Hash](#21-string-kmp--rolling-hash)

---

## 1. Sliding Window (Variable Size)

**Core idea**: Expand right pointer, shrink left pointer when constraint violated.
**Trigger words**: "longest/shortest subarray/substring", "at most K distinct", "max sum with constraint"

---

### Weekly Contest 220 — #1695 Maximum Erasure Value
**Problems:**
- 2092. Find All People With a Secret (Union Find — bonus)
- **1695. Maximum Erasure Value** ★ Medium
- 1696. Jump Game VI (DP + Deque)
- 1697. Checking Existence of Edge Length Limited Paths (Union Find + Offline)

**Why Classic**: #1695 is a textbook sliding window — find longest subarray with all unique elements, maximize sum. The key trick: use a set/map to track what's in the window, shrink when duplicate found.

```
Pattern Template:
  left = 0, seen = {}
  for right in range(n):
      while nums[right] in seen: remove seen[left], left++
      add seen[nums[right]]
      ans = max(ans, sum_window)
```

---

### Weekly Contest 325 — #2516 Take K Characters From Left and Right
**Problems:**
- 2515. Shortest Distance to Target in Circular Array (Easy)
- **2516. Take K of Each Character From Left and Right** ★ Medium
- 2517. Maximum Tastiness of Candy Basket (Binary Search on Answer)
- 2518. Number of Great Partitions (DP + Combinatorics)

**Why Classic**: Classic "complement" trick — instead of taking from left/right, find the longest middle window to *exclude*. Any window where remaining elements still satisfy constraint → sliding window on complement.

---

### Weekly Contest 186 — #1423 Maximum Points from Cards
**Problems:**
- 1422. Maximum Score After Splitting String (Easy)
- **1423. Maximum Points You Can Obtain from Cards** ★ Medium
- 1424. Diagonal Traverse II (Medium)
- 1425. Constrained Subsequence Sum (Hard — DP + Deque)

**Why Classic**: Teaches "fixed-size window on circular/split array". Instead of iterating all (left, right) splits, keep a fixed window of size `n-k` in the middle to minimize, leaving max at the ends.

---

### Weekly Contest 2831 — #2831 Find Longest Equal Subarray
**Problems:**
- 2828. Check if String Is Acronym (Easy)
- 2829. Minimum Sum of k-Avoiding Array (Easy/Medium)
- 2830. Maximize Profit as Salesman (DP)
- **2831. Find the Longest Equal Subarray** ★ Medium

**Why Classic**: Sliding window with a twist — window can have at most `k` "bad" elements (elements ≠ majority). Group indices by value, then slide within each group. Key insight: answer = max window where (window_size - count_of_dominant) ≤ k.

---

### Weekly Contest 375 — #2968 Frequency Score
**Problems:**
- 2965. Find Missing and Repeated Values (Easy)
- 2966. Divide Array Into Arrays With Max Difference (Greedy)
- 2967. Minimum Cost to Make Array Equalindromic (Math + Median)
- **2968. Apply Operations to Maximize Frequency Score** ★ Hard

**Why Classic**: Sliding window + prefix sum. Key: sort the array, then for any window, cheapest cost to make all elements equal = pick median, use prefix sum to compute cost in O(1). Then slide window.

---

## 2. Two Pointers

**Core idea**: Two indices moving toward each other (or same direction) to avoid O(N²) brute force.
**Trigger words**: "pair/triplet with sum = X", "closest/minimum difference", "remove duplicates"

---

### Weekly Contest 240 — #1855 Max Distance Between Pair of Values
**Problems:**
- 1854. Maximum Population Year (Easy)
- **1855. Maximum Distance Between a Pair of Values** ★ Medium
- 1856. Maximum Subarray Min-Product (Monotonic Stack)
- 1857. Largest Color Value in Directed Graph (Topological Sort + DP)

**Why Classic**: Two pointers on two sorted arrays. For each i in nums1, binary search (or pointer) for rightmost j in nums2 where nums2[j] ≥ nums1[i]. Clean O(N+M) with two pointers.

---

### Weekly Contest 253 — #1963 Minimum Swaps for Balanced String
**Problems:**
- 1961. Check If String Is Prefix of Array (Easy)
- 1962. Remove Stones to Minimize Total (Heap)
- **1963. Minimum Number of Swaps to Make String Balanced** ★ Medium
- 1964. Find Longest Valid Obstacle Course at Each Position (LIS + BSearch)

**Why Classic**: Greedy + two pointers on brackets. Count unmatched `[` from left scan, unmatched `]` from right scan. Answer = ceil(unmatched / 2). The "inward" two-pointer swap intuition is reusable.

---

### Weekly Contest 295 — #2616 Minimize Max Difference of Pairs
**Problems:**
- 2614. Prime In Diagonal (Easy)
- 2615. Sum of Distances (Prefix Sum + Math)
- **2616. Minimize the Maximum Difference of Pairs** ★ Medium
- 2617. Minimum Number of Visited Cells in a Grid (Hard)

**Why Classic**: Binary search on answer + greedy two-pointer verification. "Can we form k pairs with max diff ≤ mid?" — sort, greedily pair adjacent elements with diff ≤ mid. Classic "check" function template.

---

## 3. Binary Search on Answer

**Core idea**: Answer is monotonic — binary search on the answer value, verify feasibility.
**Trigger words**: "minimize the maximum", "maximize the minimum", "at least k ...", "feasibility check"

---

### Weekly Contest 187 — #1439 Kth Smallest Sum
**Problems:**
- 1436. Destination City (Easy)
- 1437. Check If 1s Are K Places Apart (Easy)
- **1438. Longest Subarray With Abs Diff ≤ Limit** ★ Medium (Sliding Window + Deque)
- **1439. Find Kth Smallest Sum of Matrix With Sorted Rows** ★ Hard

**Why Classic**: #1439 — binary search on answer + heap merge. Binary search on value X, count how many row-sums ≤ X using heap/merge. Key pattern for "kth smallest in sorted structure".

---

### Weekly Contest 517 (renamed 517→) / Contest 290 — #2251 Flowers in Full Bloom
**Problems:**
- 2248. Intersection of Multiple Arrays (Easy)
- 2249. Count Lattice Points Inside Circle (Medium)
- 2250. Count Rectangles Containing Point (Medium)
- **2251. Number of Flowers in Full Bloom** ★ Hard

**Why Classic**: Binary search on sorted events. For each query time t: flowers bloomed = bisect_right(starts, t), flowers faded = bisect_left(ends, t). ans[i] = bloomed - faded. Pure offline binary search, no data structure needed.

---

### Weekly Contest 245 — #1898 Maximum Number of Removable Characters
**Problems:**
- 1897. Redistribute Characters (Easy)
- **1898. Maximum Number of Removable Characters** ★ Medium
- 1899. Merge Triplets to Form Target Triplet (Greedy)
- 1900. Earliest and Latest Rounds (DP)

**Why Classic**: Binary search on k (how many characters to remove). For a given k, remove first k indices, check if t is still subsequence of s. Monotonic property: if k works, k-1 also works.

---

### Weekly Contest 340 — #2616 + #2617
**Problems:**
- 2614. Prime In Diagonal (Easy)
- 2615. Sum of Distances (Medium — Prefix Sum)
- **2616. Minimize the Maximum Difference of Pairs** ★ Medium
- **2617. Minimum Number of Visited Cells in a Grid** ★ Hard

**Why Classic**: #2617 combines Dijkstra + Segment Tree for range queries. Classic "binary search on answer for grid problems" and "BFS + sorted set for reachable positions".

---

## 4. Prefix Sum

**Core idea**: Precompute cumulative sums for O(1) range queries.
**Trigger words**: "sum of subarray", "number of subarrays with sum = K", "range sum queries"

---

### Weekly Contest 150 — #1162 As Far from Land as Possible
**Problems:**
- 1160. Find Words That Can Be Formed by Characters (Easy)
- 1161. Maximum Level Sum of Binary Tree (BFS)
- **1162. As Far from Land as Possible** ★ Medium (Multi-source BFS — bonus)
- 1163. Last Substring in Lexicographical Order (Hard — Two Pointers)

**Why Classic**: #1160 uses character frequency (1D prefix count). Key: prefix sum on character counts for "can we form string from available characters" in O(1) per query.

---

### Weekly Contest 260 — #2017 Grid Game
**Problems:**
- 2016. Maximum Difference Between Increasing Elements (Easy)
- **2017. Grid Game** ★ Medium
- 2018. Check if Word Can Be Placed in Crossword (Medium)
- 2019. Score of Students Solving Math Expression (Hard — DP + Stack)

**Why Classic**: #2017 — 2D prefix sum + game theory. Robot 1 cuts the grid; Robot 2 takes max of (top-right sum) or (bottom-left sum). Prefix sum lets you compute both halves in O(1) as you scan the split column. Classic "prefix + suffix" competition pattern.

---

### Weekly Contest 265 — #2055 Plates Between Candles
**Problems:**
- 2053. Kth Distinct String in Array (Easy)
- 2054. Two Best Non-Overlapping Events (Heap/DP)
- **2055. Plates Between Candles** ★ Medium
- 2056. Number of Valid Move Combinations on Chessboard (Hard)

**Why Classic**: Prefix sum + binary search. Precompute: prefix count of plates, nearest left-candle[], nearest right-candle[]. For each query [l,r]: find innermost candles, subtract prefix sums. Template for "count elements between boundary markers".

---

## 5. Monotonic Stack

**Core idea**: Maintain a stack where elements are strictly increasing/decreasing. Pop when current element violates order.
**Trigger words**: "next greater/smaller element", "largest rectangle", "stock span", "min-product"

---

### Weekly Contest 84 — #845 Longest Mountain in Array
**Problems:**
- 844. Backspace String Compare (Easy — Two Pointers)
- **845. Longest Mountain in Array** ★ Medium
- 847. Shortest Path Visiting All Nodes (Hard — BFS + Bitmask)
- 848. Shifting Letters (Medium)

**Why Classic**: #845 — expand from peaks pattern (precursor to monotonic stack). For each index, find how far left/right is strictly increasing/decreasing. Sets up intuition for "find boundaries" problems.

---

### Weekly Contest 230 — #1776 Car Fleet II
**Problems:**
- 1773. Count Items Matching a Rule (Easy)
- 1774. Closest Dessert Cost (Medium — Backtracking)
- 1775. Equal Sum Arrays With Min Operations (Medium — Greedy)
- **1776. Car Fleet II** ★ Hard

**Why Classic**: Monotonic stack processed right-to-left. For each car, find the first car ahead it catches up to (using stack). If car i catches car j before car j catches car j+1, then car i's answer is the time to reach car j. Classic "nearest element with condition" pattern.

---

### Weekly Contest 240 — #1856 Maximum Subarray Min-Product
**Problems:**
- 1854. Maximum Population Year (Easy)
- 1855. Maximum Distance Between Pair of Values (Two Pointers)
- **1856. Maximum Subarray Min-Product** ★ Medium
- 1857. Largest Color Value in Directed Graph (Hard)

**Why Classic**: For each element as the minimum of a subarray — find leftmost and rightmost bounds where it remains minimum. Classic monotonic stack "previous/next smaller element" pattern. Then use prefix sum to compute subarray sum in O(1).

---

### Weekly Contest 285 — #2211 Count Collisions on a Road
**Problems:**
- 2210. Count Hills and Valleys (Easy)
- **2211. Count Collisions on a Road** ★ Medium
- 2212. Maximum Points in Archery Competition (Bitmask DP)
- 2213. Longest Substring of One Repeating Character (Hard — Segment Tree)

**Why Classic**: Stack simulation of collision physics. Process from edges inward; cars moving away from center never collide. Stack tracks collision outcomes. Reinforces "stack for simulation" thinking.

---

### Weekly Contest 295 — #2289 Steps to Make Array Non-decreasing
**Problems:**
- 2287. Rearrange Characters to Make Target String (Easy)
- 2288. Apply Discount to Prices (Medium — String Parsing)
- **2289. Steps to Make Array Non-decreasing** ★ Medium
- 2290. Minimum Obstacle Removal to Reach Corner (Hard — Dijkstra)

**Why Classic**: Monotonic stack where each element tracks how many steps it takes to "consume" smaller elements to its right. `dp[i] = max(dp[j]) + 1` for all j consumed by i. Elegantly combines stack with DP.

---

## 6. Monotonic Queue + Sliding Window Deque

**Core idea**: Deque maintains indices of max/min in a sliding window in O(1) amortized.
**Trigger words**: "max/min in sliding window of size k", "DP with sliding window optimization"

---

### Weekly Contest 186 — #1425 Constrained Subsequence Sum
**Problems:**
- 1422. Maximum Score After Splitting String (Easy)
- 1423. Maximum Points You Can Obtain from Cards (Sliding Window)
- 1424. Diagonal Traverse II (Medium)
- **1425. Constrained Subsequence Sum** ★ Hard

**Why Classic**: DP with sliding window max optimization. `dp[i] = nums[i] + max(0, max(dp[i-k..i-1]))`. Naive O(NK) → O(N) with monotonic deque. Key template for "DP where transition looks back at most K positions".

---

### Weekly Contest 220 — #1696 Jump Game VI
**Problems:**
- 1694. Reformat Phone Number (Easy)
- 1695. Maximum Erasure Value (Sliding Window)
- **1696. Jump Game VI** ★ Medium
- 1697. Checking Existence of Edge Length Limited Paths (Hard — Union Find)

**Why Classic**: `dp[i] = nums[i] + max(dp[i-k..i-1])`. Identical template to #1425. Deque front = max index in window. When front < i-k, pop front. When dp[i] ≥ dp[deque.back()], pop back. Widely applicable pattern.

---

### Weekly Contest 187 — #1438 Longest Subarray With Abs Diff ≤ Limit
**Problems:**
- 1436. Destination City (Easy)
- 1437. Check 1s Are K Apart (Easy)
- **1438. Longest Continuous Subarray With Abs Diff ≤ Limit** ★ Medium
- 1439. Kth Smallest Sum (Hard)

**Why Classic**: Two deques simultaneously — one for sliding max, one for sliding min. Window valid when max - min ≤ limit. Best showcase of "dual monotonic deque" technique.

---

## 7. Dynamic Programming (1D / 2D)

**Core idea**: Build solution from subproblems. State definition is the hardest part.
**Trigger words**: "ways to ...", "minimum cost to ...", "can we achieve ...", "count distinct ..."

---

### Weekly Contest 253 — #1964 Longest Valid Obstacle Course (LIS)
**Problems:**
- 1961. Check If String Is Prefix of Array (Easy)
- 1962. Remove Stones to Minimize Total (Heap)
- 1963. Minimum Swaps for Balanced String (Greedy)
- **1964. Find the Longest Valid Obstacle Course at Each Position** ★ Hard

**Why Classic**: LIS (Longest Increasing Subsequence) with binary search. `tails[]` array maintained with bisect_right for non-decreasing LIS. Answer at each index = length of LIS ending here. O(N log N) classic.

---

### Weekly Contest 305 — #2369 Check Valid Partition + #2370 Longest Ideal Subsequence
**Problems:**
- 2367. Number of Arithmetic Triplets (Easy)
- 2368. Reachable Nodes With Restrictions (BFS/DFS)
- **2369. Check if There is a Valid Partition For The Array** ★ Medium
- **2370. Longest Ideal Subsequence** ★ Medium

**Why Classic**: #2369 — straightforward 1D DP. `dp[i] = can we partition nums[0..i-1]?`. 3 transitions (pairs, same triplets, consecutive triplets). #2370 — `dp[c] = longest ideal subsequence ending with character c`. O(N*26).

---

### Weekly Contest 315 — #2444 Count Subarrays With Fixed Bounds
**Problems:**
- 2441. Largest Positive Integer with Its Negative (Easy)
- 2442. Count Distinct Integers After Reverse (Medium)
- 2443. Sum of Number and Its Reverse (Medium)
- **2444. Count Subarrays With Fixed Bounds** ★ Hard

**Why Classic**: Sliding window + three-pointer counting. Track: last bad index (out of [minK, maxK]), last minK index, last maxK index. Count = max(0, min(last_min, last_max) - last_bad). Elegant O(N) formula — must know this trick.

---

### Weekly Contest 199 — #1531 String Compression II
**Problems:**
- 1528. Shuffle String (Easy)
- 1529. Minimum Suffix Flips (Medium — Greedy)
- 1530. Number of Good Leaf Nodes Pairs (Medium — Tree DFS)
- **1531. String Compression II** ★ Hard

**Why Classic**: Hard 2D DP on strings. `dp[i][k]` = min length to compress s[0..i] after deleting k characters. Complex but teaches state design for "delete up to k elements to minimize cost" pattern.

---

### Weekly Contest 330 — #2552 Count Increasing Quadruplets
**Problems:**
- 2549. Count Distinct Numbers on Board (Easy)
- 2550. Count Collisions of Monkeys on Polygon (Math)
- 2551. Put Marbles in Bags (Greedy + Sort)
- **2552. Count Increasing Quadruplets** ★ Hard

**Why Classic**: `dp[j] = number of valid (i,j) pairs where nums[i] < nums[j]`. For each k, contribution = sum of dp[j] where nums[j] < nums[k] < nums[l]. Classic "count pairs then combine" DP technique.

---

## 8. DP + Bitmask

**Core idea**: Use bitmask to represent subsets/states when N ≤ 20.
**Trigger words**: "assign to groups", "visit all nodes", "cover all requirements", N ≤ 20

---

### Weekly Contest 84 — #847 Shortest Path Visiting All Nodes
**Problems:**
- 844. Backspace String Compare (Easy)
- 845. Longest Mountain in Array (Medium)
- **847. Shortest Path Visiting All Nodes** ★ Hard
- 848. Shifting Letters (Medium)

**Why Classic**: BFS + bitmask state `(node, visited_mask)`. State space = N * 2^N. Classic "traveling all nodes with BFS for shortest path". The pattern: when you need both position and visited set as state, bitmask is the tool.

---

### Weekly Contest 175 — #1349 Maximum Students Taking Exam
**Problems:**
- 1346. Check If N and Its Double Exist (Easy)
- 1347. Min Steps to Make Two Strings Anagram (Medium)
- 1348. Tweet Counts Per Frequency (Medium)
- **1349. Maximum Students Taking Exam** ★ Hard

**Why Classic**: Row-by-row bitmask DP. `dp[mask]` = max students when current row has seating `mask`. Validity check = no adjacent in same row, no diagonal conflicts with prev row. Clean bitmask DP template for grid problems.

---

### Weekly Contest 212 — #1659 Maximize Grid Happiness
**Problems:**
- 1656. Design an Ordered Stream (Easy)
- 1657. Determine if Two Strings Are Close (Medium)
- 1658. Minimum Operations to Reduce X to Zero (Sliding Window)
- **1659. Maximize Grid Happiness** ★ Hard

**Why Classic**: 3D bitmask DP with "sliding window" of bitmask states. State = (position, last_row_mask, counts_of_introverts_extroverts). Advanced bitmask DP with profile DP technique.

---

## 9. BFS / Multi-Source BFS

**Core idea**: Level-by-level graph exploration. Multi-source: start BFS from all sources simultaneously.
**Trigger words**: "shortest path", "minimum steps", "nearest X to all cells", "spread from multiple origins"

---

### Weekly Contest 150 — #1162 As Far from Land as Possible
**Problems:**
- 1160. Find Words Formed by Characters (Easy)
- 1161. Maximum Level Sum of Binary Tree (Medium)
- **1162. As Far from Land as Possible** ★ Medium
- 1163. Last Substring in Lexicographical Order (Hard)

**Why Classic**: Multi-source BFS from all land cells simultaneously. Distance to nearest land = BFS level. Key insight: instead of BFS from each water cell (O(N² * N²)), BFS once from all land cells. Classic "invert source" optimization.

---

### Weekly Contest 167 — #1293 Shortest Path With Obstacle Elimination
**Problems:**
- 1290. Convert Binary Linked List to Integer (Easy)
- 1291. Sequential Digits (Medium)
- 1292. Max Side Length of Square with Sum ≤ Target (Prefix Sum + BSearch)
- **1293. Shortest Path in Grid With Obstacles Elimination** ★ Hard

**Why Classic**: BFS with extended state `(row, col, remaining_eliminations)`. State space expansion = add constraint as part of state. Classic pattern for "BFS where you have a limited resource".

---

### Weekly Contest 270 — #2097 Valid Arrangement of Pairs (Eulerian Path)
**Problems:**
- 2094. Finding 3-Digit Even Numbers (Easy)
- 2095. Delete Middle Node of Linked List (Medium)
- **2096. Step-By-Step Directions From BST Node to Another** ★ Medium (LCA)
- **2097. Valid Arrangement of Pairs** ★ Hard

**Why Classic**: Eulerian path in directed graph. Find start node (out_degree - in_degree = 1) or any node if Eulerian circuit. DFS/Hierholzer's algorithm. Recognizing "arrange all edges" = Eulerian path is a key insight.

---

### Weekly Contest 100 — #864 Shortest Path to Get All Keys
**Problems:**
- 861. Score After Flipping Matrix (Greedy)
- 863. All Nodes Distance K in Binary Tree (BFS on Tree)
- **864. Shortest Path to Get All Keys** ★ Hard
- 865. Smallest Subtree with All Deepest Nodes (DFS)

**Why Classic**: BFS with bitmask state for keys collected. State = `(row, col, keys_bitmask)`. Each time you pick up a key, new state. Classic "collect items while pathfinding" pattern. Combines BFS + bitmask beautifully.

---

## 10. DFS + Memoization

**Core idea**: Recursive exploration with caching (top-down DP).
**Trigger words**: "count paths", "reachability", "game theory", "minimum cost path in grid"

---

### Weekly Contest 300 — #2328 Number of Increasing Paths in a Grid
**Problems:**
- 2325. Decode the Message (Easy)
- 2326. Spiral Matrix IV (Medium — Simulation)
- 2327. Number of People Aware of a Secret (Medium — Simulation/DP)
- **2328. Number of Increasing Paths in a Grid** ★ Hard

**Why Classic**: DFS + memoization on directed acyclic graph (DAG) formed by strictly increasing neighbors. `memo[i][j]` = number of increasing paths starting from (i,j). No need for visited array since graph is a DAG (strictly increasing = no cycles).

---

### Weekly Contest 365 — #2876 Count Visited Nodes in Directed Graph
**Problems:**
- 2873. Maximum Value of Ordered Triplet I (Easy)
- 2874. Maximum Value of Ordered Triplet II (Medium — Prefix Max)
- 2875. Minimum Size Subarray in Infinite Array (Sliding Window + Math)
- **2876. Count Visited Nodes in a Directed Graph** ★ Hard

**Why Classic**: Functional graph (each node has exactly one outgoing edge). Find cycles using DFS coloring; nodes on cycle get count = cycle length; nodes pointing to cycle get count = distance to cycle + cycle length. Classic functional graph traversal.

---

### Weekly Contest 199 — #1530 Number of Good Leaf Nodes Pairs
**Problems:**
- 1528. Shuffle String (Easy)
- 1529. Minimum Suffix Flips (Medium)
- **1530. Number of Good Leaf Nodes Pairs** ★ Medium
- 1531. String Compression II (Hard — DP)

**Why Classic**: Post-order DFS returning distance arrays from leaves. For each node, combine left and right leaf distances, count pairs with sum ≤ distance. Key: return compact distance-to-leaf arrays from each subtree.

---

## 11. Graph: Topological Sort

**Core idea**: Process nodes in dependency order (DAG). Detect cycles.
**Trigger words**: "prerequisite", "order of tasks", "dependency", "DAG"

---

### Weekly Contest 240 — #1857 Largest Color Value in Directed Graph
**Problems:**
- 1854. Maximum Population Year (Easy)
- 1855. Maximum Distance Between Pair of Values (Two Pointers)
- 1856. Maximum Subarray Min-Product (Monotonic Stack)
- **1857. Largest Color Value in a Directed Graph** ★ Hard

**Why Classic**: Topological sort + DP. `dp[node][c]` = max count of color c on any path ending at node. Process in topological order. If cycle exists, return -1. Template for "longest path with attribute" on DAG.

---

### Weekly Contest 305 — #2368 Reachable Nodes With Restrictions
**Problems:**
- 2367. Number of Arithmetic Triplets (Easy)
- **2368. Reachable Nodes With Restrictions** ★ Medium
- 2369. Check Valid Partition (Medium)
- 2370. Longest Ideal Subsequence (Medium)

**Why Classic**: BFS/DFS on tree with restricted nodes. Mark restricted nodes, do BFS/DFS from node 0 avoiding them. Clean template for "tree traversal with forbidden nodes".

---

### Weekly Contest 320 — #2477 Minimum Fuel Cost to Report to Capital
**Problems:**
- 2475. Number of Unequal Triplets in Array (Easy)
- 2476. Closest Nodes Queries in BST (Inorder + Binary Search)
- **2477. Minimum Fuel Cost to Report to the Capital** ★ Medium
- 2478. Number of Beautiful Partitions (Hard — DP)

**Why Classic**: Post-order DFS on tree. For each subtree, you need ceil(subtree_size / seats) trips to carry everyone to parent. Classic "gather to root" tree greedy with post-order processing.

---

## 12. Graph: Shortest Path (Dijkstra / BFS with State)

**Core idea**: Weighted shortest path (Dijkstra) or 0-1 BFS for unit/binary weights.
**Trigger words**: "minimum cost path", "cheapest route", "minimum effort"

---

### Weekly Contest 167 — #1293 Shortest Path (see Section 9)

---

### Weekly Contest 290 — #2290 Minimum Obstacle Removal to Reach Corner
**Problems:**
- 2287. Rearrange Characters (Easy)
- 2288. Apply Discount to Prices (Medium)
- 2289. Steps to Make Array Non-decreasing (Medium — Monotonic Stack)
- **2290. Minimum Obstacle Removal to Reach Corner** ★ Hard

**Why Classic**: 0-1 BFS. Moving through empty cell costs 0, through obstacle costs 1. Use deque: cost-0 moves go to front, cost-1 to back. O(N*M) vs Dijkstra's O(N*M*log(N*M)). Key: recognize when 0-1 BFS beats Dijkstra.

---

### Weekly Contest 285 — #2290 (alternate) / Weekly Contest 370 — #2931
**Problems (WC370):**
- 2928. Distribute Candies Among Children I (Easy)
- 2929. Distribute Candies Among Children II (Medium — Math)
- 2930. Number of Strings Rearranged to Contain Substring (Medium — DP)
- **2931. Maximum Spending After Buying Items** ★ Hard

**Why Classic**: Greedy + min-heap across sorted shop arrays. Always buy globally minimum available item, sell for day number. Demonstrates "merge sorted arrays with heap" efficiently.

---

## 13. Union Find (Offline Processing)

**Core idea**: Disjoint sets for connectivity. Offline = sort queries and process in order.
**Trigger words**: "connected components", "edges added over time", "minimum spanning tree", "offline queries on graph"

---

### Weekly Contest 220 — #1697 Edge Length Limited Paths
**Problems:**
- 1694. Reformat Phone Number (Easy)
- 1695. Maximum Erasure Value (Sliding Window)
- 1696. Jump Game VI (DP + Deque)
- **1697. Checking Existence of Edge Length Limited Paths** ★ Hard

**Why Classic**: Offline Union Find. Sort both edges and queries by weight limit. For each query, add all edges with weight < limit into UF, check connectivity. Classic "offline queries + Union Find" template.

---

### Weekly Contest 335 — #2580 Count Ways to Group Overlapping Ranges
**Problems:**
- 2578. Split With Minimum Sum (Easy)
- 2579. Count Total Colored Cells (Medium — Math)
- **2580. Count Ways to Group Overlapping Ranges** ★ Medium
- 2581. Count Number of Possible Root Nodes (Hard — Rerooting DP)

**Why Classic**: Union Find / Greedy on intervals. Sort by start, merge overlapping intervals. Number of independent groups = 2^(group_count) mod 1e9+7. Teaches "count independent components" pattern.

---

### Weekly Contest 345 — #2685 Count the Number of Complete Components
**Problems:**
- 2682. Find the Losers of the Circular Game (Easy)
- 2683. Neighboring Bitwise XOR (Medium)
- 2684. Maximum Number of Moves in a Grid (Medium — DP)
- **2685. Count the Number of Complete Components** ★ Medium

**Why Classic**: Union Find where you track both node count and edge count per component. A component is "complete" if edges = nodes*(nodes-1)/2. Key: augment UF to carry component statistics.

---

## 14. Heap / Priority Queue

**Core idea**: Efficiently access max/min element. For K-th problems, top-K, or greedy-by-priority.
**Trigger words**: "K largest/smallest", "find kth", "greedily pick maximum/minimum"

---

### Weekly Contest 253 — #1962 Remove Stones to Minimize Total
**Problems:**
- 1961. Check If String Is Prefix of Array (Easy)
- **1962. Remove Stones to Minimize the Total** ★ Medium
- 1963. Minimum Swaps for Balanced String (Medium)
- 1964. Longest Valid Obstacle Course (LIS)

**Why Classic**: Max-heap greedy. K operations: always take largest pile, replace with ceil(pile/2). Heap.poll() + heap.add(val/2) × K times. Clean "repeated greedy with heap" template.

---

### Weekly Contest 187 — #1439 Kth Smallest Matrix Sum (see Section 3)

---

### Weekly Contest 370 — #2931 Maximum Spending (see Section 12)

---

### Weekly Contest 209 — #1606 Find Servers That Handled Most Requests
**Problems:**
- 1603. Design Parking System (Easy — Design)
- 1604. Alert Using Same Key-Card (Medium — HashMap + Sorting)
- 1605. Find Valid Matrix Given Row and Column Sums (Medium — Greedy)
- **1606. Find Servers That Handled Most Requests** ★ Hard

**Why Classic**: Two heaps (available servers + busy servers) + sorted set. Complex multi-heap management. Key: when a request arrives at time t, free all servers finishing before t (min-heap by finish time), then assign to next available server ≥ i (sorted set with bisect).

---

## 15. Greedy + Sorting

**Core idea**: Local optimal choice leads to global optimum. Often requires sorting first.
**Trigger words**: "minimum number of ...", "assign jobs to machines", "interval scheduling", "partition array"

---

### Weekly Contest 235 — #1818 Minimum Absolute Sum Difference
**Problems:**
- 1816. Truncate Sentence (Easy)
- 1817. Finding Users Active Minutes (Medium — HashMap)
- **1818. Minimum Absolute Sum Difference** ★ Medium
- 1819. Number of Different Subsequences GCDs (Hard — Math)

**Why Classic**: Sort a copy + binary search. For each index, find closest value in nums1 to nums2[i] using binary search. Greedily replace only the index giving maximum improvement. "Sort one array, binary search for best match" is widely applicable.

---

### Weekly Contest 275 — #2136 Earliest Possible Day of Full Bloom
**Problems:**
- 2133. Check if Every Row/Column Contains All Numbers (Easy)
- 2134. Minimum Swaps to Group 1s Together II (Sliding Window)
- 2135. Count Words Obtained After Adding a Letter (Medium)
- **2136. Earliest Possible Day of Full Bloom** ★ Hard

**Why Classic**: Pure greedy. Sort plants by grow time descending. Plant greediest first (longer grow time = more urgent to start). Proof by exchange argument. Key: identifying the right sorting key for greedy.

---

### Weekly Contest 330 — #2551 Put Marbles in Bags
**Problems:**
- 2549. Count Distinct Numbers on Board (Easy)
- 2550. Count Collisions of Monkeys (Math)
- **2551. Put Marbles in Bags** ★ Hard
- 2552. Count Increasing Quadruplets (Hard — DP)

**Why Classic**: Reduce to choosing k-1 "split points". Each split between i and i+1 costs nums[i]+nums[i+1]. Sort these pair-costs; take k-1 smallest for min, k-1 largest for max. Elegant reduction from seemingly complex problem to sort+select.

---

### Weekly Contest 406 — #2406 Divide Intervals Into Minimum Number of Groups
**Problems:**
- 2404. Most Frequent Even Element (Easy)
- 2405. Optimal Partition of String (Medium — Greedy)
- **2406. Divide Intervals Into Minimum Number of Groups** ★ Medium
- 2407. Longest Increasing Subsequence II (Hard — Segment Tree)

**Why Classic**: Event/sweep line. Sort all starts and ends; use min-heap to track earliest ending group. When new interval starts > heap.top(), reuse that group. Answer = max heap size. Classic interval scheduling / room allocation template.

---

## 16. Segment Tree / Binary Indexed Tree

**Core idea**: O(log N) range queries and point updates.
**Trigger words**: "range sum/max/min with updates", "count elements in range", "2D range query"

---

### Weekly Contest 310 — #2407 Longest Increasing Subsequence II
**Problems:**
- 2404. Most Frequent Even Element (Easy)
- 2405. Optimal Partition of String (Medium)
- 2406. Divide Intervals Into Minimum Groups (Greedy)
- **2407. Longest Increasing Subsequence II** ★ Hard

**Why Classic**: Segment tree for range max query. `dp[v]` = LIS ending with value v. For each num, query max(dp[num-k..num-1]), set dp[num] = that + 1. O(N log M). Classic "LIS with constraint on value difference" using segment tree.

---

### Weekly Contest 285 — #2213 Longest Substring of One Repeating Character
**Problems:**
- 2210. Count Hills and Valleys (Easy)
- 2211. Count Collisions on Road (Greedy + Stack)
- 2212. Maximum Points in Archery (Bitmask DP)
- **2213. Longest Substring of One Repeating Character** ★ Hard

**Why Classic**: Segment tree with interval information. Each node stores: length, left char, right char, prefix run, suffix run, max run. Point updates propagate interval merge logic up the tree. Classic "segment tree with composite interval data".

---

### Weekly Contest 250 — #1938 Maximum Genetic Difference Query
**Problems:**
- 1935. Maximum Number of Words You Can Type (Easy)
- 1936. Add Minimum Number of Rungs (Greedy)
- 1937. Maximum Number of Points with Cost (Medium — DP Optimization)
- **1938. Maximum Genetic Difference Query** ★ Hard

**Why Classic**: Offline DFS + Trie. Process tree nodes in DFS order, queries on each node = XOR max between path-to-root nodes and query values. Insert/delete from Trie during DFS traversal. "Online Trie with DFS" pattern.

---

## 17. Tree: LCA / Rerooting DP

**Core idea**: LCA for tree path queries. Rerooting: compute answer for each node as root without re-running DFS.
**Trigger words**: "path between nodes in tree", "answer for every node as root", "sum of distances"

---

### Weekly Contest 270 — #2096 Step-By-Step Directions From BST Node
**Problems:**
- 2094. Finding 3-Digit Even Numbers (Easy)
- 2095. Delete Middle Node of Linked List (Medium)
- **2096. Step-By-Step Directions From BST Node to Another** ★ Medium
- 2097. Valid Arrangement of Pairs (Hard — Eulerian Path)

**Why Classic**: LCA on BST. Find LCA, then path = L-moves up from start to LCA + R-moves down to end. Key: any "path between two nodes" problem in a tree goes through LCA.

---

### Weekly Contest 320 — #2477 Minimum Fuel Cost (see Section 11)

---

### Weekly Contest 335 — #2581 Count Number of Possible Root Nodes
**Problems:**
- 2578. Split With Minimum Sum (Easy)
- 2579. Count Total Colored Cells (Math)
- 2580. Count Ways to Group Overlapping Ranges (Medium)
- **2581. Count Number of Possible Root Nodes** ★ Hard

**Why Classic**: Rerooting DP. First DFS: compute `dp[root]` = correct guesses with node 0 as root. Second DFS: when "re-rooting" from parent p to child c, adjust count: if edge (p→c) in guesses, dp[c] = dp[p]+1; if edge (c→p) in guesses, dp[c] = dp[p]-1. O(N) rerooting classic.

---

### Weekly Contest 615 / #834 Sum of Distances in Tree
**Note**: Though #834 is not from a recent weekly, the rerooting DP pattern it introduces is fundamental:
- First DFS: compute subtree sizes and sum of distances from root.
- Second DFS: re-root by adjusting `dist[child] = dist[parent] - size[child] + (N - size[child])`.
Many hard tree problems in recent contests use this exact rerooting template.

---

## 18. Trie

**Core idea**: Prefix tree for string lookups, XOR maximum queries.
**Trigger words**: "prefix matching", "count strings with prefix", "maximum XOR in array"

---

### Weekly Contest 250 — #1938 Maximum Genetic Difference (see Section 16)

---

### Weekly Contest 355 — #2791 Count Paths That Form Palindrome in Tree
**Problems:**
- 2788. Split Strings by Separator (Easy)
- 2789. Largest Element After Merge Operations (Greedy)
- 2790. Maximum Number of Groups With Increasing Length (Medium — Greedy)
- **2791. Count Paths That Can Form a Palindrome in a Tree** ★ Hard

**Why Classic**: XOR of character frequencies. A path can form a palindrome iff at most 1 bit is set in the XOR of character bitmasks (26 bits). Map from XOR mask to count. For each node, count paths to ancestors with compatible masks. DFS + XOR hashing.

---

## 19. Backtracking

**Core idea**: Try all possibilities, undo choices (backtrack) when constraint violated.
**Trigger words**: "generate all", "find all combinations/permutations/subsets", "N-Queens"

---

### Weekly Contest 3 — Classic Subset/Combination Problems
**Classic reference problems** (not single contest but foundational):
- **78. Subsets** — base backtracking template
- **39. Combination Sum** — with repetition allowed
- **40. Combination Sum II** — with duplicates, sort+skip
- **46. Permutations** — swap-based permutation

**Template:**
```
def backtrack(start, path):
    result.append(path[:])
    for i in range(start, len(nums)):
        if i > start and nums[i] == nums[i-1]: continue  # skip dups
        path.append(nums[i])
        backtrack(i+1, path)
        path.pop()
```

---

### Weekly Contest 175 — #1900 Earliest and Latest Rounds
**Problems:**
- 1346. Check If N and Double Exist (Easy)
- 1347. Min Steps to Make Two Strings Anagram (Medium)
- 1348. Tweet Counts Per Frequency (Design)
- **1900. The Earliest and Latest Rounds Where Players Compete** ★ Hard

**Why Classic**: Backtracking/memoization on tournament bracket. `dp[l][r][n]` = (earliest, latest) rounds for players at positions l and r in n-player tournament. Recursion over all possible match outcomes. Teaches pruning and state design in game simulations.

---

## 20. Math / Bit Manipulation

**Core idea**: Algebraic properties, modular arithmetic, XOR tricks, Gray code.
**Trigger words**: "XOR", "power of 2", "modular inverse", "circular", "parity"

---

### Weekly Contest 209 — #1611 Minimum One Bit Operations to Make Integer Zero
**Problems:**
- 1608. Special Array With X Elements ≥ X (Easy)
- 1609. Even Odd Tree (Medium — BFS)
- 1610. Maximum Number of Visible Points (Medium — Sliding Window on angles)
- **1611. Minimum One Bit Operations to Make Integers Zero** ★ Hard

**Why Classic**: Gray code interpretation. The minimum operations = Gray code value of n. Key: `f(n) = n ^ (n>>1) ^ (n>>2) ^ ...` or recursion `f(n) = 2^(bit_pos) - 1 - f(n ^ (1<<bit_pos))`. Teaches recognizing bit manipulation patterns.

---

### Weekly Contest 285 — #2212 Maximum Points in Archery (see Section 8)

---

### Weekly Contest 350 — #2749 Minimum Operations to Make Integer Zero
**Problems:**
- 2748. Number of Beautiful Pairs (Easy)
- **2749. Minimum Operations to Make Integer Zero** ★ Medium
- 2750. Ways to Split Array Into Good Subarrays (Medium — Math)
- 2751. Robot Collisions (Hard — Stack)

**Why Classic**: XOR + bit counting. Each operation: `num XOR 2^i XOR 2^(i+1)`. Key insight: we need to reduce num to 0. After k operations: result = num XOR (k * (2^i values)). Since XOR is self-inverse, recognize that answer involves popcount and structure of binary representation.

---

### Weekly Contest 550 / Biweekly — #1835 Find XOR Sum of All Pairs Bitwise AND
**Why Classic**: XOR distribution over AND: `XOR(a_i AND b_j) = (XOR of a_i) AND (XOR of b_j)`. Pure algebraic identity, teaches distributing bitwise operations.

---

## 21. String: KMP / Rolling Hash

**Core idea**: Pattern matching in O(N+M). Rolling hash for O(1) hash computation per window.
**Trigger words**: "find pattern in text", "repeated substring", "string matching"

---

### Weekly Contest 310 — #2405 Optimal Partition of String
**Problems:**
- 2404. Most Frequent Even Element (Easy)
- **2405. Optimal Partition of String** ★ Medium
- 2406. Divide Intervals Into Groups (Medium — Greedy)
- 2407. LIS II (Hard — Segment Tree)

**Why Classic**: Greedy string partition using bit mask tracking. Track seen characters per partition with a bitmask; when duplicate found, start new partition. O(N) greedy — teaches "bitmask for character sets" trick.

---

### Weekly Contest 3 / Classic — KMP Building Block
**Core KMP problems** (foundational):
- **28. Find the Index of the First Occurrence in a String** — KMP template
- **459. Repeated Substring Pattern** — KMP failure function
- **1392. Longest Happy Prefix** — directly constructs KMP table

**KMP Template:**
```java
int[] buildLPS(String pattern) {
    int[] lps = new int[pattern.length()];
    int len = 0, i = 1;
    while (i < pattern.length()) {
        if (pattern.charAt(i) == pattern.charAt(len)) {
            lps[i++] = ++len;
        } else if (len > 0) {
            len = lps[len - 1];
        } else {
            lps[i++] = 0;
        }
    }
    return lps;
}
```

---

## Summary: Pattern Recognition Cheat Sheet

| When you see... | Think... |
|----------------|----------|
| Longest/shortest subarray with constraint | **Sliding Window** |
| Pairs, triplets, sorted array traversal | **Two Pointers** |
| "Minimize maximum" or "maximize minimum" | **Binary Search on Answer** |
| Range sum queries, "sum of subarray" | **Prefix Sum** |
| Next greater/smaller element | **Monotonic Stack** |
| Max/min in sliding window of size K | **Monotonic Deque** |
| DP looking back at most K positions | **Deque + DP** |
| Count/min ways, grid/sequence choices | **DP (1D/2D)** |
| Subset/state with N ≤ 20 items | **Bitmask DP** |
| Shortest path, unweighted grid | **BFS** |
| Weighted shortest path | **Dijkstra** |
| 0/1 weights | **0-1 BFS (Deque)** |
| Collect items while pathfinding | **BFS + Bitmask State** |
| Dependencies, ordering | **Topological Sort** |
| Dynamic connectivity queries | **Union Find (Offline)** |
| K largest/smallest, greedy priority | **Heap** |
| Sort + local optimal choice | **Greedy** |
| Range updates + range queries | **Segment Tree / BIT** |
| Path between tree nodes | **LCA** |
| Answer for every node as root | **Rerooting DP** |
| Prefix matching, XOR max | **Trie** |
| Generate all subsets/permutations | **Backtracking** |
| XOR patterns, power of 2 | **Bit Manipulation** |
| Pattern in string, O(N+M) | **KMP** |
