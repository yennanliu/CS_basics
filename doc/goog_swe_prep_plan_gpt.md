# Google SWE Interview Prep Roadmap

## 1. Core Patterns to Master
These are the “templates” Google interview problems often fall into.  
Master **patterns**, not just solutions.

| Pattern | Key Techniques | Example Problems |
|---------|---------------|------------------|
| **Sliding Window** | Two pointers, dynamic window size, prefix sums | LC 3, 76, 424, 438 |
| **Two Pointers** | Opposite ends, sorted arrays | LC 1, 15, 42, 125 |
| **Fast/Slow Pointers** | Cycle detection, middle finding | LC 141, 142, 876 |
| **Binary Search** | Search space, lower/upper bound | LC 33, 34, 153, 4 |
| **Prefix Sum** | Cumulative sum, hash mapping | LC 560, 525, 238 |
| **DFS / BFS** | Graph/tree traversal | LC 102, 200, 994, 127 |
| **Backtracking** | Subsets, permutations, combinations | LC 46, 78, 39, 51 |
| **Union-Find (DSU)** | Connected components, Kruskal's | LC 547, 684, 261 |
| **Heap/Priority Queue** | Top-K, scheduling | LC 347, 215, 23 |
| **Binary Tree Recursion** | DFS w/ return values, path sums | LC 124, 543, 236 |
| **BST** | Insert/search, validate | LC 98, 230, 235 |
| **Graph Shortest Path** | Dijkstra, BFS | LC 743, 787, 1631 |
| **Topological Sort** | Course schedule | LC 207, 210 |
| **Dynamic Programming – 1D** | Fib, knapsack | LC 70, 198, 322 |
| **Dynamic Programming – 2D** | Grid path, subsequence | LC 62, 1143, 72 |
| **Greedy** | Interval scheduling, merging | LC 55, 45, 435 |
| **Bit Manipulation** | XOR tricks, masks | LC 136, 137, 190 |
| **String Processing** | KMP, rolling hash | LC 28, 686, 459 |
| **Monotonic Stack/Queue** | Next greater element | LC 739, 84, 42 |
| **Matrix Traversal** | DFS/BFS in matrix | LC 79, 130, 329 |

---

## 2. Curated Google SWE Problem Set (100–150)
Balanced mix of **Google-tagged** + **Top 100 Liked** + **Pattern Coverage**.

### A. Arrays & Strings
1. LC 1 - Two Sum ✅
2. LC 121 - Best Time to Buy and Sell Stock ✅
3. LC 238 - Product of Array Except Self ✅
4. LC 3 - Longest Substring Without Repeating Characters ✅
5. LC 424 - Longest Repeating Character Replacement
6. LC 76 - Minimum Window Substring ✅
7. LC 11 - Container With Most Water
8. LC 42 - Trapping Rain Water ✅
9. LC 15 - 3Sum ✅
10. LC 560 - Subarray Sum Equals K ✅

### B. Binary Search / Sorting
11. LC 704 - Binary Search
12. LC 33 - Search in Rotated Sorted Array ✅
13. LC 153 - Find Minimum in Rotated Sorted Array
14. LC 4 - Median of Two Sorted Arrays ✅
15. LC 215 - Kth Largest Element in Array ✅

### C. Linked Lists
16. LC 206 - Reverse Linked List ✅
17. LC 21 - Merge Two Sorted Lists ✅
18. LC 141 - Linked List Cycle ✅
19. LC 143 - Reorder List
20. LC 2 - Add Two Numbers ✅

### D. Trees & BST
21. LC 104 - Maximum Depth of Binary Tree ✅
22. LC 100 - Same Tree
23. LC 226 - Invert Binary Tree ✅
24. LC 235 - Lowest Common Ancestor of BST ✅
25. LC 124 - Binary Tree Maximum Path Sum ✅
26. LC 98 - Validate Binary Search Tree ✅
27. LC 230 - Kth Smallest Element in BST ✅
28. LC 297 - Serialize and Deserialize Binary Tree ✅
29. LC 199 - Binary Tree Right Side View
30. LC 105 - Construct Binary Tree from Preorder & Inorder

### E. Graphs
31. LC 200 - Number of Islands ✅
32. LC 133 - Clone Graph ✅
33. LC 207 - Course Schedule ✅
34. LC 210 - Course Schedule II
35. LC 127 - Word Ladder ✅
36. LC 417 - Pacific Atlantic Water Flow ✅
37. LC 743 - Network Delay Time
38. LC 684 - Redundant Connection
39. LC 310 - Minimum Height Trees

### F. Backtracking
40. LC 78 - Subsets ✅
41. LC 90 - Subsets II ✅
42. LC 46 - Permutations ✅
43. LC 39 - Combination Sum ✅
44. LC 40 - Combination Sum II
45. LC 79 - Word Search ✅
46. LC 51 - N-Queens ✅

### G. Dynamic Programming
47. LC 70 - Climbing Stairs ✅
48. LC 198 - House Robber ✅
49. LC 322 - Coin Change ✅
50. LC 62 - Unique Paths ✅
51. LC 300 - Longest Increasing Subsequence ✅
52. LC 1143 - Longest Common Subsequence
53. LC 72 - Edit Distance ✅
54. LC 416 - Partition Equal Subset Sum ✅
55. LC 139 - Word Break ✅

### H. Heap / Priority Queue
56. LC 23 - Merge k Sorted Lists ✅
57. LC 347 - Top K Frequent Elements ✅
58. LC 295 - Find Median from Data Stream ✅
59. LC 973 - K Closest Points to Origin ✅

### I. Greedy
60. LC 55 - Jump Game ✅
61. LC 45 - Jump Game II
62. LC 56 - Merge Intervals ✅
63. LC 435 - Non-overlapping Intervals ✅

### J. Advanced
64. LC 208 - Implement Trie ✅
65. LC 211 - Design Add and Search Words
66. LC 460 - LFU Cache
67. LC 146 - LRU Cache ✅
68. LC 76 - Minimum Window Substring ✅ (repeat for mastery)
69. LC 428 - Serialize and Deserialize N-ary Tree

*(Continue until ~150 problems with harder Google-tagged variants)*


======================================

(Hard/advanced)

### K. Advanced Trees & Graphs
70. LC 236 - Lowest Common Ancestor of a Binary Tree ✅
71. LC 863 - All Nodes Distance K in Binary Tree ✅
72. LC 1245 - Tree Diameter
73. LC 987 - Vertical Order Traversal of a Binary Tree ✅
74. LC 1457 - Pseudo-Palindromic Paths in a Binary Tree
75. LC 332 - Reconstruct Itinerary ✅
76. LC 269 - Alien Dictionary (Topological Sort + DFS)
77. LC 778 - Swim in Rising Water (Dijkstra / Heap)
78. LC 317 - Shortest Distance from All Buildings
79. LC 815 - Bus Routes
80. LC 928 - Minimize Malware Spread II

### L. Advanced Backtracking & Combinatorics
81. LC 301 - Remove Invalid Parentheses
82. LC 126 - Word Ladder II
83. LC 679 - 24 Game
84. LC 282 - Expression Add Operators
85. LC 51 - N-Queens ✅ (Repeat for mastery)
86. LC 52 - N-Queens II
87. LC 212 - Word Search II (Trie + Backtracking)
88. LC 980 - Unique Paths III
89. LC 489 - Robot Room Cleaner (Google robot control simulation)
90. LC 679 - 24 Game (Math recursion variant)

### M. Advanced Dynamic Programming
91. LC 312 - Burst Balloons
92. LC 97 - Interleaving String
93. LC 115 - Distinct Subsequences
94. LC 44 - Wildcard Matching
95. LC 72 - Edit Distance ✅ (Repeat with tabulation & memoization)
96. LC 188 - Best Time to Buy and Sell Stock IV
97. LC 354 - Russian Doll Envelopes
98. LC 87 - Scramble String
99. LC 940 - Distinct Subsequences II
100. LC 132 - Palindrome Partitioning II

### N. Advanced Graph + Shortest Path
101. LC 1192 - Critical Connections in a Network (Tarjan's algorithm)
102. LC 743 - Network Delay Time ✅ (Repeat for mastery)
103. LC 332 - Reconstruct Itinerary ✅ (DFS + Stack)
104. LC 787 - Cheapest Flights Within K Stops
105. LC 1631 - Path With Minimum Effort
106. LC 847 - Shortest Path Visiting All Nodes
107. LC 1293 - Shortest Path in a Grid with Obstacles Elimination
108. LC 1466 - Reorder Routes to Make All Paths Lead to the City Zero
109. LC 778 - Swim in Rising Water ✅ (Repeat for heap mastery)
110. LC 269 - Alien Dictionary ✅ (Repeat for graph parsing mastery)

### O. Hard Greedy & Interval Problems
111. LC 759 - Employee Free Time
112. LC 1851 - Minimum Interval to Include Each Query
113. LC 135 - Candy
114. LC 630 - Course Schedule III
115. LC 1642 - Furthest Building You Can Reach
116. LC 871 - Minimum Number of Refueling Stops
117. LC 502 - IPO
118. LC 295 - Find Median from Data Stream ✅ (Repeat for heap mastery)
119. LC 480 - Sliding Window Median
120. LC 239 - Sliding Window Maximum ✅

### P. Hard Heap / Priority Queue
121. LC 218 - The Skyline Problem
122. LC 871 - Minimum Number of Refueling Stops ✅ (Repeat for PQ mastery)
123. LC 1942 - The Number of the Smallest Unoccupied Chair
124. LC 1845 - Seat Reservation Manager
125. LC 2335 - Minimum Amount of Time to Fill Cups
126. LC 1383 - Maximum Performance of a Team
127. LC 373 - Find K Pairs with Smallest Sums
128. LC 502 - IPO ✅ (Repeat for heap mastery)
129. LC 778 - Swim in Rising Water ✅ (Graph + PQ hybrid)

### Q. Specialized / Google-Style Simulation Problems
130. LC 642 - Design Search Autocomplete System
131. LC 295 - Find Median from Data Stream ✅
132. LC 211 - Design Add and Search Words ✅
133. LC 208 - Implement Trie ✅
134. LC 588 - Design In-Memory File System
135. LC 380 - Insert Delete GetRandom O(1)
136. LC 381 - Insert Delete GetRandom O(1) - Duplicates allowed
137. LC 460 - LFU Cache
138. LC 146 - LRU Cache ✅ (Repeat for implementation mastery)
139. LC 341 - Flatten Nested List Iterator
140. LC 173 - Binary Search Tree Iterator ✅

### R. Final Boss Problems (Mix of Patterns)
141. LC 295 - Find Median from Data Stream ✅
142. LC 23 - Merge k Sorted Lists ✅
143. LC 218 - The Skyline Problem ✅
144. LC 269 - Alien Dictionary ✅
145. LC 126 - Word Ladder II ✅
146. LC 1192 - Critical Connections in a Network ✅
147. LC 312 - Burst Balloons ✅
148. LC 980 - Unique Paths III ✅
149. LC 847 - Shortest Path Visiting All Nodes ✅
150. LC 778 - Swim in Rising Water ✅

---

## 3. How to Use This List Without Overgrinding
- **Phase 1:** Do **1–2 problems per pattern** to learn the approach (don’t aim for speed yet).
- **Phase 2:** Revisit **same problems + variants** but under **45 min** limit.
- **Phase 3:** Focus on **mixed problem sets + mock interviews**.