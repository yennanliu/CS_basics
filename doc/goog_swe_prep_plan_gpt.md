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

---

## 3. How to Use This List Without Overgrinding
- **Phase 1:** Do **1–2 problems per pattern** to learn the approach (don’t aim for speed yet).
- **Phase 2:** Revisit **same problems + variants** but under **45 min** limit.
- **Phase 3:** Focus on **mixed problem sets + mock interviews**.