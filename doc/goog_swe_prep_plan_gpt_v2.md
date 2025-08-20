
## 1. Core Patterns to Master
These are the “templates” Google interview problems often fall into. Master **patterns**, not just solutions.

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

## 2. Curated Google SWE Problem Set (Extended to ~400 problems)
Balanced mix of **Google-tagged** + **Top 100 Liked** + **Pattern Coverage**.

### A. Arrays & Strings
| Problem | Pattern |
|---|---|
| 1. LC 1 - Two Sum | Hash Map, Array |
| 2. LC 121 - Best Time to Buy and Sell Stock | Greedy, Array |
| 3. LC 238 - Product of Array Except Self | Array, Prefix Sum |
| 4. LC 3 - Longest Substring Without Repeating Characters | Sliding Window, Hash Map |
| 5. LC 424 - Longest Repeating Character Replacement | Sliding Window |
| 6. LC 76 - Minimum Window Substring | Sliding Window, Hash Map |
| 7. LC 11 - Container With Most Water | Two Pointers |
| 8. LC 42 - Trapping Rain Water | Two Pointers, Dynamic Programming |
| 9. LC 15 - 3Sum | Two Pointers, Sorting |
| 10. LC 560 - Subarray Sum Equals K | Hash Map, Prefix Sum |
| 11. LC 49 - Group Anagrams | Hash Map, String Processing |
| 12. LC 5 - Longest Palindromic Substring | Dynamic Programming, Two Pointers |
| 13. LC 53 - Maximum Subarray | Dynamic Programming |
| 14. LC 128 - Longest Consecutive Sequence | Hash Set, Union-Find |
| 15. LC 179 - Largest Number | Sorting, String Processing |
| 16. LC 287 - Find the Duplicate Number | Floyd's Cycle Detection, Two Pointers |
| 17. LC 48 - Rotate Image | Array, In-place modification |
| 18. LC 647 - Palindromic Substrings | Dynamic Programming, Two Pointers |
| 19. LC 344 - Reverse String | Two Pointers |
| 20. LC 125 - Valid Palindrome | Two Pointers |
| 21. LC 242 - Valid Anagram | Hash Map, Counting |
| 22. LC 134 - Gas Station | Greedy |
| 23. LC 165 - Compare Version Numbers | String Processing, Two Pointers |
| 24. LC 277 - Find the Celebrity | Graph, Two Pointers |
| 25. LC 43 - Multiply Strings | String Processing, Array |
| 26. LC 67 - Add Binary | String Processing, Math |
| 27. LC 283 - Move Zeroes | Two Pointers |
| 28. LC 387 - First Unique Character in a String | Hash Map, String Processing |
| 29. LC 54 - Spiral Matrix | Matrix Traversal |
| 30. LC 59 - Spiral Matrix II | Matrix Traversal |

### B. Binary Search / Sorting
| Problem | Pattern |
|---|---|
| 31. LC 704 - Binary Search | Binary Search |
| 32. LC 33 - Search in Rotated Sorted Array | Binary Search |
| 33. LC 153 - Find Minimum in Rotated Sorted Array | Binary Search |
| 34. LC 4 - Median of Two Sorted Arrays | Binary Search |
| 35. LC 215 - Kth Largest Element in Array | Quickselect, Heap |
| 36. LC 240 - Search a 2D Matrix II | Binary Search, Two Pointers |
| 37. LC 410 - Split Array Largest Sum | Binary Search on Answer |
| 38. LC 1011 - Capacity To Ship Packages Within D Days | Binary Search on Answer |
| 39. LC 162 - Find Peak Element | Binary Search |
| 40. LC 658 - Find K Closest Elements | Binary Search |
| 41. LC 875 - Koko Eating Bananas | Binary Search on Answer |
| 42. LC 148 - Sort List | Merge Sort, Linked List |
| 43. LC 75 - Sort Colors | Two Pointers, Counting Sort |
| 44. LC 50 - Pow(x, n) | Binary Search (Exponents) |
| 45. LC 34 - Find First and Last Position of Element in Sorted Array | Binary Search |

### C. Linked Lists
| Problem | Pattern |
|---|---|
| 46. LC 206 - Reverse Linked List | Linked List, Iteration/Recursion |
| 47. LC 21 - Merge Two Sorted Lists | Linked List, Two Pointers |
| 48. LC 141 - Linked List Cycle | Fast/Slow Pointers |
| 49. LC 143 - Reorder List | Linked List, Two Pointers |
| 50. LC 2 - Add Two Numbers | Linked List, Math |
| 51. LC 19 - Remove Nth Node From End of List | Two Pointers |
| 52. LC 234 - Palindrome Linked List | Fast/Slow Pointers, Two Pointers |
| 53. LC 83 - Remove Duplicates from Sorted List | Linked List |
| 54. LC 92 - Reverse Linked List II | Linked List |
| 55. LC 142 - Linked List Cycle II | Fast/Slow Pointers |
| 56. LC 25 - Reverse Nodes in k-Group | Linked List, Recursion |
| 57. LC 876 - Middle of the Linked List | Fast/Slow Pointers |
| 58. LC 328 - Odd Even Linked List | Linked List, Two Pointers |
| 59. LC 445 - Add Two Numbers II | Linked List, Stack |

### D. Trees & BST
| Problem | Pattern |
|---|---|
| 60. LC 104 - Maximum Depth of Binary Tree | Tree Traversal, DFS/BFS |
| 61. LC 100 - Same Tree | Tree Traversal, Recursion |
| 62. LC 226 - Invert Binary Tree | Tree Traversal, Recursion |
| 63. LC 235 - Lowest Common Ancestor of BST | BST, Tree Traversal |
| 64. LC 124 - Binary Tree Maximum Path Sum | Tree DP, DFS |
| 65. LC 98 - Validate Binary Search Tree | BST, Recursion |
| 66. LC 230 - Kth Smallest Element in BST | BST, In-order Traversal |
| 67. LC 297 - Serialize and Deserialize Binary Tree | Tree Traversal, DFS/BFS |
| 68. LC 199 - Binary Tree Right Side View | Tree Traversal, BFS |
| 69. LC 105 - Construct Binary Tree from Preorder & Inorder | Tree, Recursion |
| 70. LC 102 - Binary Tree Level Order Traversal | BFS |
| 71. LC 103 - Binary Tree Zigzag Level Order Traversal | BFS |
| 72. LC 108 - Convert Sorted Array to Binary Search Tree | BST, Recursion |
| 73. LC 437 - Path Sum III | Tree Traversal, DFS, Prefix Sum |
| 74. LC 543 - Diameter of Binary Tree | Tree Traversal, DFS |
| 75. LC 662 - Maximum Width of Binary Tree | BFS, Queue |
| 76. LC 110 - Balanced Binary Tree | Tree Traversal, Recursion |
| 77. LC 112 - Path Sum | Tree Traversal, DFS |
| 78. LC 113 - Path Sum II | Backtracking, DFS |
| 79. LC 236 - Lowest Common Ancestor of a Binary Tree | Tree Traversal, DFS |
| 80. LC 863 - All Nodes Distance K in Binary Tree | Graph Traversal, BFS |
| 81. LC 987 - Vertical Order Traversal of a Binary Tree | Tree Traversal, BFS |
| 82. LC 1457 - Pseudo-Palindromic Paths in a Binary Tree | Backtracking, Tree Traversal |
| 83. LC 111 - Minimum Depth of Binary Tree | BFS |
| 84. LC 257 - Binary Tree Paths | Backtracking, DFS |
| 85. LC 572 - Subtree of Another Tree | Tree Traversal |
| 86. LC 298 - Binary Tree Longest Consecutive Sequence | Tree Traversal, DFS |
| 87. LC 508 - Most Frequent Subtree Sum | Tree Traversal, Hash Map |
| 88. LC 114 - Flatten Binary Tree to Linked List | Tree Traversal, Recursion |
| 89. LC 106 - Construct Binary Tree from Inorder and Postorder Traversal | Tree, Recursion |

### E. Graphs
| Problem | Pattern |
|---|---|
| 90. LC 200 - Number of Islands | Graph Traversal, DFS/BFS |
| 91. LC 133 - Clone Graph | Graph Traversal, BFS/DFS, Hash Map |
| 92. LC 207 - Course Schedule | Topological Sort, DFS/BFS |
| 93. LC 210 - Course Schedule II | Topological Sort, DFS/BFS |
| 94. LC 127 - Word Ladder | BFS, Graph Traversal |
| 95. LC 417 - Pacific Atlantic Water Flow | Graph Traversal, DFS |
| 96. LC 743 - Network Delay Time | Dijkstra's Algorithm, Heap |
| 97. LC 684 - Redundant Connection | Union-Find |
| 98. LC 310 - Minimum Height Trees | Graph Traversal, BFS |
| 99. LC 399 - Evaluate Division | Graph, DFS, Union-Find |
| 100. LC 733 - Flood Fill | Graph Traversal, DFS/BFS |
| 101. LC 695 - Max Area of Island | Graph Traversal, DFS/BFS |
| 102. LC 1557 - Minimum Number of Vertices to Reach All Nodes | Graph, In-degree |
| 103. LC 261 - Graph Valid Tree | Union-Find, DFS |
| 104. LC 802 - Find Eventual Safe States | Graph Traversal, DFS, Cycle Detection |
| 105. LC 997 - Find the Town Judge | Graph, In-degree/Out-degree |
| 106. LC 1334 - Find the City With the Smallest Number of Neighbors | Floyd-Warshall, Dijkstra |
| 107. LC 1905 - Count Sub Islands | DFS, Grid Traversal |
| 108. LC 200 - Number of Islands | DFS, Grid Traversal |
| 109. LC 1584 - Min Cost to Connect All Points | Minimum Spanning Tree (MST), Union-Find, Kruskal's |
| 110. LC 1192 - Critical Connections in a Network | Tarjan's Algorithm, DFS |
| 111. LC 787 - Cheapest Flights Within K Stops | Bellman-Ford, BFS |
| 112. LC 1631 - Path With Minimum Effort | Dijkstra's Algorithm, Heap |
| 113. LC 847 - Shortest Path Visiting All Nodes | BFS, Bitmask DP |
| 114. LC 1293 - Shortest Path in a Grid with Obstacles Elimination | BFS, Dijkstra's |
| 115. LC 1466 - Reorder Routes to Make All Paths Lead to the City Zero | DFS, Graph Traversal |
| 116. LC 332 - Reconstruct Itinerary | Graph Traversal, DFS, Hierholzer's Algorithm |
| 117. LC 269 - Alien Dictionary | Topological Sort |
| 118. LC 778 - Swim in Rising Water | Dijkstra's Algorithm, Heap |
| 119. LC 317 - Shortest Distance from All Buildings | BFS, Grid Traversal |
| 120. LC 815 - Bus Routes | BFS, Graph Traversal |

### F. Backtracking
| Problem | Pattern |
|---|---|
| 121. LC 78 - Subsets | Backtracking |
| 122. LC 90 - Subsets II | Backtracking |
| 123. LC 46 - Permutations | Backtracking |
| 124. LC 39 - Combination Sum | Backtracking |
| 125. LC 40 - Combination Sum II | Backtracking |
| 126. LC 79 - Word Search | Backtracking, DFS |
| 127. LC 51 - N-Queens | Backtracking |
| 128. LC 77 - Combinations | Backtracking |
| 129. LC 47 - Permutations II | Backtracking |
| 130. LC 216 - Combination Sum III | Backtracking |
| 131. LC 17 - Letter Combinations of a Phone Number | Backtracking |
| 132. LC 37 - Sudoku Solver | Backtracking |
| 133. LC 131 - Palindrome Partitioning | Backtracking |
| 134. LC 93 - Restore IP Addresses | Backtracking |
| 135. LC 52 - N-Queens II | Backtracking |
| 136. LC 212 - Word Search II | Trie, Backtracking |
| 137. LC 980 - Unique Paths III | Backtracking, DFS |
| 138. LC 489 - Robot Room Cleaner | Backtracking, DFS |
| 139. LC 679 - 24 Game | Backtracking |
| 140. LC 301 - Remove Invalid Parentheses | Backtracking, BFS |

### G. Dynamic Programming
| Problem | Pattern |
|---|---|
| 141. LC 70 - Climbing Stairs | Dynamic Programming, 1D |
| 142. LC 198 - House Robber | Dynamic Programming, 1D |
| 143. LC 322 - Coin Change | Dynamic Programming, Unbounded Knapsack |
| 144. LC 62 - Unique Paths | Dynamic Programming, 2D |
| 145. LC 300 - Longest Increasing Subsequence | Dynamic Programming, Binary Search |
| 146. LC 1143 - Longest Common Subsequence | Dynamic Programming, 2D |
| 147. LC 72 - Edit Distance | Dynamic Programming, 2D |
| 148. LC 416 - Partition Equal Subset Sum | Dynamic Programming, Knapsack |
| 149. LC 139 - Word Break | Dynamic Programming, Trie |
| 150. LC 518 - Coin Change II | Dynamic Programming, Unbounded Knapsack |
| 151. LC 120 - Triangle | Dynamic Programming, 2D |
| 152. LC 152 - Maximum Product Subarray | Dynamic Programming, 1D |
| 153. LC 221 - Maximal Square | Dynamic Programming, 2D |
| 154. LC 1048 - Longest String Chain | Dynamic Programming |
| 155. LC 123 - Best Time to Buy and Sell Stock III | Dynamic Programming |
| 156. LC 368 - Largest Divisible Subset | Dynamic Programming |
| 157. LC 309 - Best Time to Buy and Sell Stock with Cooldown | Dynamic Programming |
| 158. LC 132 - Palindrome Partitioning II | Dynamic Programming |
| 159. LC 97 - Interleaving String | Dynamic Programming, 2D |
| 160. LC 115 - Distinct Subsequences | Dynamic Programming, 2D |

### H. Heap / Priority Queue
| Problem | Pattern |
|---|---|
| 161. LC 23 - Merge k Sorted Lists | Heap, Divide & Conquer |
| 162. LC 347 - Top K Frequent Elements | Heap, Hash Map |
| 163. LC 295 - Find Median from Data Stream | Two Heaps |
| 164. LC 973 - K Closest Points to Origin | Heap |
| 165. LC 218 - The Skyline Problem | Sweep Line, Heap |
| 166. LC 871 - Minimum Number of Refueling Stops | Greedy, Heap |
| 167. LC 1942 - The Number of the Smallest Unoccupied Chair | Heap |
| 168. LC 1845 - Seat Reservation Manager | Heap |
| 169. LC 2335 - Minimum Amount of Time to Fill Cups | Heap |
| 170. LC 1383 - Maximum Performance of a Team | Greedy, Heap |
| 171. LC 373 - Find K Pairs with Smallest Sums | Heap |
| 172. LC 502 - IPO | Greedy, Heap |
| 173. LC 480 - Sliding Window Median | Sliding Window, Two Heaps |
| 174. LC 239 - Sliding Window Maximum | Monotonic Queue |
| 175. LC 1642 - Furthest Building You Can Reach | Greedy, Heap |
| 176. LC 630 - Course Schedule III | Greedy, Heap |
| 177. LC 759 - Employee Free Time | Sweep Line, Heap |
| 178. LC 1851 - Minimum Interval to Include Each Query | Sweep Line, Heap |
| 179. LC 135 - Candy | Greedy |

### I. Greedy
| Problem | Pattern |
|---|---|
| 180. LC 55 - Jump Game | Greedy |
| 181. LC 45 - Jump Game II | Greedy |
| 182. LC 56 - Merge Intervals | Greedy, Sorting |
| 183. LC 435 - Non-overlapping Intervals | Greedy, Sorting |
| 184. LC 670 - Maximum Swap | Greedy |
| 185. LC 452 - Minimum Number of Arrows to Burst Balloons | Greedy, Sorting |
| 186. LC 763 - Partition Labels | Greedy, Hash Map |
| 187. LC 621 - Task Scheduler | Greedy, Heap |
| 188. LC 871 - Minimum Number of Refueling Stops | Greedy, Heap |
| 189. LC 134 - Gas Station | Greedy |

### J. Advanced
| Problem | Pattern |
|---|---|
| 190. LC 208 - Implement Trie | Trie, Data Structure Design |
| 191. LC 211 - Design Add and Search Words | Trie, DFS |
| 192. LC 460 - LFU Cache | Data Structure Design, Hash Map |
| 193. LC 146 - LRU Cache | Data Structure Design, Hash Map, Doubly Linked List |
| 194. LC 428 - Serialize and Deserialize N-ary Tree | DFS, BFS, Data Structure Design |
| 195. LC 588 - Design In-Memory File System | Trie, Data Structure Design |
| 196. LC 380 - Insert Delete GetRandom O(1) | Hash Map, Array |
| 197. LC 381 - Insert Delete GetRandom O(1) - Duplicates allowed | Hash Map, Array |
| 198. LC 341 - Flatten Nested List Iterator | Stack, Data Structure Design |
| 199. LC 173 - Binary Search Tree Iterator | Stack, BST, Data Structure Design |
| 200. LC 642 - Design Search Autocomplete System | Trie, Data Structure Design |

---

### K. More Linked Lists
| Problem | Pattern |
|---|---|
| 201. LC 160 - Intersection of Two Linked Lists | Linked List, Two Pointers |
| 202. LC 147 - Insertion Sort List | Linked List, Sorting |
| 203. LC 61 - Rotate List | Linked List, Two Pointers |
| 204. LC 82 - Remove Duplicates from Sorted List II | Linked List, Two Pointers |
| 205. LC 203 - Remove Linked List Elements | Linked List |
| 206. LC 237 - Delete Node in a Linked List | Linked List |
| 207. LC 708 - Insert into a Sorted Circular Linked List | Linked List |
| 208. LC 1171 - Remove Zero Sum Sublists | Linked List, Hash Map |
| 209. LC 24 - Swap Nodes in Pairs | Linked List, Recursion |
| 210. LC 86 - Partition List | Linked List, Two Pointers |

### L. More Trees & BSTs
| Problem | Pattern |
|---|---|
| 211. LC 116 - Populating Next Right Pointers in Each Node | Tree Traversal, BFS |
| 212. LC 117 - Populating Next Right Pointers in Each Node II | Tree Traversal, BFS |
| 213. LC 95 - Unique Binary Search Trees II | Dynamic Programming, Backtracking |
| 214. LC 99 - Recover Binary Search Tree | Tree Traversal, Morris Traversal |
| 215. LC 250 - Count Univalue Subtrees | Tree Traversal, DFS |
| 216. LC 270 - Closest Binary Search Tree Value | BST, Traversal |
| 217. LC 272 - Closest Binary Search Tree Value II | BST, Stack |
| 218. LC 333 - Largest BST Subtree | Tree Traversal, DFS |
| 219. LC 450 - Delete Node in a BST | BST, Recursion |
| 220. LC 538 - Convert BST to Greater Tree | BST, Traversal |
| 221. LC 687 - Longest Univalue Path | Tree Traversal, DFS |
| 222. LC 814 - Binary Tree Pruning | Tree Traversal, Recursion |
| 223. LC 965 - Univalued Binary Tree | Tree Traversal, DFS/BFS |
| 224. LC 1379 - Find a Corresponding Node... | Tree Traversal, DFS/BFS |
| 225. LC 1530 - Number of Good Leaf Nodes Pairs | Tree Traversal, DFS |
| 226. LC 1609 - Even Odd Tree | Tree Traversal, BFS |
| 227. LC 1022 - Sum of Root To Leaf Binary Numbers | Tree Traversal, DFS |
| 228. LC 1302 - Deepest Leaves Sum | Tree Traversal, BFS |
| 229. LC 1372 - Longest ZigZag Path in a Binary Tree | Tree Traversal, DFS |
| 230. LC 1325 - Delete Leaves With a Given Value | Tree Traversal, Recursion |

### M. More Graphs & BFS/DFS
| Problem | Pattern |
|---|---|
| 231. LC 797 - All Paths From Source to Target | Backtracking, DFS |
| 232. LC 990 - Satisfiability of Equality Equations | Union-Find |
| 233. LC 130 - Surrounded Regions | Graph Traversal, DFS/BFS |
| 234. LC 547 - Number of Provinces | Union-Find, DFS |
| 235. LC 1319 - Number of Operations to Make Network Connected | Union-Find |
| 236. LC 685 - Redundant Connection II | Union-Find |
| 237. LC 745 - Prefix and Suffix Search | Trie, Data Structure Design |
| 238. LC 1514 - Path With Maximum Probability | Dijkstra's, Bellman-Ford |
| 239. LC 785 - Is Graph Bipartite? | BFS, DFS, Graph Traversal |
| 240. LC 841 - Keys and Rooms | Graph Traversal, DFS/BFS |
| 241. LC 934 - Shortest Bridge | BFS, DFS |
| 242. LC 1129 - Shortest Path with Alternating Colors | BFS, Graph Traversal |
| 243. LC 1254 - Number of Closed Islands | DFS, Grid Traversal |
| 244. LC 1579 - Remove Max Number of Edges... | Union-Find, Greedy |
| 245. LC 1615 - Maximal Network Rank | Graph, DFS |
| 246. LC 1639 - Number of Ways to Form a Target String... | Dynamic Programming, Array |
| 247. LC 1971 - Find if Path Exists in Graph | DFS/BFS, Union-Find |
| 248. LC 1993 - Operations on a Binary Tree | Tree, Data Structure Design |
| 249. LC 2092 - Find All People With Secret | Graph Traversal, Sorting |
| 250. LC 2492 - Minimum Score of a Path Between Two Cities | Graph Traversal, DFS/BFS |

### N. More Backtracking
| Problem | Pattern |
|---|---|
| 251. LC 140 - Word Break II | Backtracking, Dynamic Programming |
| 252. LC 247 - Strobogrammatic Number II | Backtracking |
| 253. LC 254 - Factor Combinations | Backtracking |
| 254. LC 267 - Palindrome Permutation II | Backtracking |
| 255. LC 320 - Generalized Abbreviation | Backtracking |
| 256. LC 377 - Combination Sum IV | Dynamic Programming, Backtracking |
| 257. LC 401 - Binary Watch | Backtracking, Bit Manipulation |
| 258. LC 491 - Non-decreasing Subsequences | Backtracking |
| 259. LC 698 - Partition to K Equal Sum Subsets | Backtracking, Dynamic Programming |
| 260. LC 743 - Network Delay Time | Dijkstra's, Heap |
| 261. LC 784 - Letter Case Permutation | Backtracking |
| 262. LC 90 - Subsets II | Backtracking |
| 263. LC 93 - Restore IP Addresses | Backtracking |
| 264. LC 131 - Palindrome Partitioning | Backtracking |
| 265. LC 491 - Increasing Subsequences | Backtracking |

### O. More Dynamic Programming
| Problem | Pattern |
|---|---|
| 266. LC 32 - Longest Valid Parentheses | Dynamic Programming, Stack |
| 267. LC 64 - Minimum Path Sum | Dynamic Programming, 2D |
| 268. LC 1048 - Longest String Chain | Dynamic Programming |
| 269. LC 279 - Perfect Squares | Dynamic Programming, BFS |
| 270. LC 304 - Range Sum Query 2D - Immutable | Dynamic Programming, Prefix Sum |
| 271. LC 377 - Combination Sum IV | Dynamic Programming |
| 272. LC 474 - Ones and Zeroes | Dynamic Programming, Knapsack |
| 273. LC 583 - Delete Operation for Two Strings | Dynamic Programming, 2D |
| 274. LC 651 - 4 Keys Keyboard | Dynamic Programming |
| 275. LC 746 - Min Cost Climbing Stairs | Dynamic Programming, 1D |
| 276. LC 91 - Decode Ways | Dynamic Programming |
| 277. LC 516 - Longest Palindromic Subsequence | Dynamic Programming, 2D |
| 278. LC 87 - Scramble String | Dynamic Programming, Recursion with Memoization |
| 279. LC 1575 - Count All Possible Routes | Dynamic Programming |
| 280. LC 1335 - Minimum Difficulty of a Job Schedule | Dynamic Programming |
| 281. LC 1049 - Last Stone Weight II | Dynamic Programming, Subset Sum |
| 282. LC 1235 - Maximum Profit in Job Scheduling | Dynamic Programming, Binary Search |
| 283. LC 887 - Super Egg Drop | Dynamic Programming, Binary Search |
| 284. LC 174 - Dungeon Game | Dynamic Programming, 2D |
| 285. LC 940 - Distinct Subsequences II | Dynamic Programming |
| 286. LC 188 - Best Time to Buy and Sell Stock IV | Dynamic Programming |
| 287. LC 354 - Russian Doll Envelopes | Dynamic Programming, Sorting |
| 288. LC 10 - Regular Expression Matching | Dynamic Programming, Recursion |
| 289. LC 44 - Wildcard Matching | Dynamic Programming, Recursion |
| 290. LC 312 - Burst Balloons | Dynamic Programming |

### P. Monotonic Stack/Queue
| Problem | Pattern |
|---|---|
| 291. LC 84 - Largest Rectangle in Histogram | Monotonic Stack |
| 292. LC 739 - Daily Temperatures | Monotonic Stack |
| 293. LC 901 - Online Stock Span | Monotonic Stack |
| 294. LC 496 - Next Greater Element I | Monotonic Stack |
| 295. LC 503 - Next Greater Element II | Monotonic Stack |
| 296. LC 42 - Trapping Rain Water | Monotonic Stack, Two Pointers |
| 297. LC 239 - Sliding Window Maximum | Monotonic Queue |
| 298. LC 907 - Sum of Subarray Minimums | Monotonic Stack |
| 299. LC 316 - Remove Duplicate Letters | Monotonic Stack, Greedy |
| 300. LC 1130 - Minimum Cost Tree From Leaf Values | Monotonic Stack, Dynamic Programming |

### Q. Union-Find (DSU)
| Problem | Pattern |
|---|---|
| 301. LC 261 - Graph Valid Tree | Union-Find, Graph |
| 302. LC 305 - Number of Islands II | Union-Find, Grid Traversal |
| 303. LC 323 - Number of Connected Components... | Union-Find, Graph |
| 304. LC 399 - Evaluate Division | Union-Find, Graph |
| 305. LC 547 - Number of Provinces | Union-Find, Graph |
| 306. LC 684 - Redundant Connection | Union-Find, Graph |
| 307. LC 765 - Couples Holding Hands | Union-Find |
| 308. LC 959 - Regions Cut By Slashes | Union-Find, Grid Traversal |
| 309. LC 1101 - The Earliest Moment When Everyone... | Union-Find, Sorting |
| 310. LC 1202 - Smallest String With Swaps | Union-Find, Graph |

### R. Bit Manipulation
| Problem | Pattern |
|---|---|
| 311. LC 191 - Number of 1 Bits | Bit Manipulation |
| 312. LC 338 - Counting Bits | Bit Manipulation, Dynamic Programming |
| 313. LC 136 - Single Number | Bit Manipulation, XOR |
| 314. LC 137 - Single Number II | Bit Manipulation |
| 315. LC 268 - Missing Number | Bit Manipulation, XOR |
| 316. LC 190 - Reverse Bits | Bit Manipulation |
| 317. LC 231 - Power of Two | Bit Manipulation |
| 318. LC 393 - UTF-8 Validation | Bit Manipulation, String Processing |
| 319. LC 405 - Convert a Number to Hexadecimal | Bit Manipulation |
| 320. LC 169 - Majority Element | Boyer-Moore Voting Algorithm, Bit Manipulation |

### S. Data Structure Design
| Problem | Pattern |
|---|---|
| 321. LC 155 - Min Stack | Stack, Data Structure Design |
| 322. LC 355 - Design Twitter | Data Structure Design, Heap, Hash Map |
| 323. LC 622 - Design Circular Queue | Data Structure Design, Queue |
| 324. LC 706 - Design HashMap | Data Structure Design, Hash Table |
| 325. LC 707 - Design Linked List | Data Structure Design, Linked List |
| 326. LC 146 - LRU Cache | Data Structure Design, Hash Map, Doubly Linked List |
| 327. LC 460 - LFU Cache | Data Structure Design, Hash Map, Doubly Linked List |
| 328. LC 642 - Design Search Autocomplete System | Trie, Data Structure Design |
| 329. LC 295 - Find Median from Data Stream | Two Heaps, Data Structure Design |
| 330. LC 380 - Insert Delete GetRandom O(1) | Hash Map, Array |
| 331. LC 211 - Design Add and Search Words | Trie, DFS |
| 332. LC 208 - Trie (Prefix Tree) | Trie, Data Structure Design |
| 333. LC 588 - Design In-Memory File System | Trie, Data Structure Design |
| 334. LC 173 - Binary Search Tree Iterator | Stack, BST |
| 335. LC 341 - Flatten Nested List Iterator | Stack, Data Structure Design |

### T. Math & Geometry
| Problem | Pattern |
|---|---|
| 336. LC 9 - Palindrome Number | Math |
| 337. LC 50 - Pow(x, n) | Math, Binary Search |
| 338. LC 149 - Max Points on a Line | Math, Hash Map |
| 339. LC 172 - Factorial Trailing Zeroes | Math |
| 340. LC 204 - Count Primes | Math |
| 341. LC 440 - K-th Smallest in Lexicographical Order | Math, Binary Search |
| 342. LC 412 - Fizz Buzz | Math |
| 343. LC 681 - Next Closest Time | Math, Brute Force |
| 344. LC 1610 - Maximum Number of Visible Points | Math, Geometry, Sorting |
| 345. LC 202 - Happy Number | Math, Fast/Slow Pointers |

### U. Hard DP & Graph Problems
| Problem | Pattern |
|---|---|
| 346. LC 1335 - Minimum Difficulty of a Job Schedule | Dynamic Programming |
| 347. LC 1049 - Last Stone Weight II | Dynamic Programming, Knapsack |
| 348. LC 1235 - Maximum Profit in Job Scheduling | Dynamic Programming, Binary Search |
| 349. LC 1575 - Count All Possible Routes | Dynamic Programming |
| 350. LC 887 - Super Egg Drop | Dynamic Programming, Binary Search |
| 351. LC 140 - Word Break II | Backtracking, Dynamic Programming |
| 352. LC 174 - Dungeon Game | Dynamic Programming, 2D |
| 353. LC 87 - Scramble String | Dynamic Programming, Recursion |
| 354. LC 329 - Longest Increasing Path in a Matrix | Dynamic Programming, DFS |
| 355. LC 115 - Distinct Subsequences | Dynamic Programming, 2D |
| 356. LC 126 - Word Ladder II | BFS, Backtracking |
| 357. LC 363 - Max Sum of Rectangle No Larger Than K | Dynamic Programming, Prefix Sum |
| 358. LC 488 - Zuma Game | Backtracking, Recursion |
| 359. LC 828 - Count Unique Characters... | Dynamic Programming, Math |
| 360. LC 956 - Tallest Billboard | Dynamic Programming |
| 361. LC 1301 - Path with Maximum Gold | Backtracking, DFS |
| 362. LC 1027 - Longest Arithmetic Subsequence | Dynamic Programming |
| 363. LC 1035 - Uncrossed Lines | Dynamic Programming, 2D |
| 364. LC 1187 - Make Array Strictly Increasing | Dynamic Programming, Binary Search |
| 365. LC 1220 - Count Vowels Permutation | Dynamic Programming |

### V. More Google-Tagged Hards
| Problem | Pattern |
|---|---|
| 366. LC 1630 - Arithmetic Subarrays | Array, Sorting |
| 367. LC 1032 - Stream of Characters | Trie, Data Structure Design |
| 368. LC 1268 - Search Suggestions System | Trie, String Processing |
| 369. LC 1102 - Path With Maximum Minimum Value | Binary Search on Answer, Graph Traversal |
| 370. LC 1675 - Minimize Deviation in Array | Heap, Greedy |
| 371. LC 164 - Maximum Gap | Radix Sort, Bucket Sort |
| 372. LC 727 - Minimum Window Subsequence | Dynamic Programming, Two Pointers |
| 373. LC 1423 - Maximum Points You Can Obtain from Cards | Sliding Window, Two Pointers |
| 374. LC 855 - Exam Room | Heap, Data Structure Design |
| 375. LC 829 - Consecutive Numbers Sum | Math |
| 376. LC 849 - Maximize Distance to Closest Person | Two Pointers |
| 377. LC 857 - Minimum Cost to Hire K Workers | Greedy, Heap |
| 378. LC 913 - Cat and Mouse | Dynamic Programming, Game Theory |
| 379. LC 952 - Largest Component Size by Common Factor | Union-Find, Math |
| 380. LC 1044 - Longest Duplicate Substring | Binary Search, Rolling Hash, Suffix Array |
| 381. LC 1092 - Shortest Common Supersequence | Dynamic Programming, String Processing |
| 382. LC 1249 - Minimum Remove to Make Valid Parentheses | Stack, String Processing |
| 383. LC 1424 - Diagonal Traverse II | Hash Map, Array |
| 384. LC 1498 - Number of Subsequences... | Sorting, Two Pointers, Math |
| 385. LC 1570 - Dot Product of Two Sparse Vectors | Hash Map, Two Pointers |

### W. Final Boss Problems (Mix of Patterns)
| Problem | Pattern |
|---|---|
| 386. LC 295 - Find Median from Data Stream | Two Heaps, Data Structure Design |
| 387. LC 23 - Merge k Sorted Lists | Heap, Divide & Conquer |
| 388. LC 218 - The Skyline Problem | Sweep Line, Heap |
| 389. LC 269 - Alien Dictionary | Topological Sort, Graph |
| 390. LC 126 - Word Ladder II | BFS, Backtracking |
| 391. LC 1192 - Critical Connections in a Network | Tarjan's Algorithm, Graph |
| 392. LC 312 - Burst Balloons | Dynamic Programming |
| 393. LC 980 - Unique Paths III | Backtracking, DFS |
| 394. LC 847 - Shortest Path Visiting All Nodes | BFS, Dynamic Programming, Bitmasking |
| 395. LC 778 - Swim in Rising Water | Dijkstra's, Heap |
| 396. LC 10 - Regular Expression Matching | Dynamic Programming |
| 397. LC 44 - Wildcard Matching | Dynamic Programming |
| 398. LC 327 - Count of Range Sum | Merge Sort, Divide & Conquer |
| 399. LC 768 - Max Chunks To Make Sorted II | Array, Stack |
| 400. LC 1220 - Count Vowels Permutation | Dynamic Programming |

---

## 3. How to Use This List Without Overgrinding
- **Phase 1:** Do **1–2 problems per pattern** to learn the approach (don’t aim for speed yet).
- **Phase 2:** Revisit **same problems + variants** but under **45 min** limit.
- **Phase 3:** Focus on **mixed problem sets + mock interviews**.