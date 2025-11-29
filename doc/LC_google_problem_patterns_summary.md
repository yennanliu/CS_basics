# Google LeetCode Problem Patterns Summary

**Source:** LeetCode Company Tagged Problems (Google)
**Date Analyzed:** February 2022
**Total Problems:** 1115+

## Overview
This document summarizes the most frequently tested algorithmic patterns and problem types in Google's LeetCode problem set.

---

## Top Problem Patterns by Frequency

### 1. **Array Manipulation** (Most Common)
- **Key Techniques:**
  - Two Pointers
  - Sliding Window
  - Prefix Sum
  - Sorting
  - Binary Search on Arrays

- **Representative Problems:**
  - Two Sum (#1) - Easy
  - Subarray Sum Equals K (#560) - Medium
  - Maximum Subarray (#53) - Easy
  - Merge Intervals (#56) - Medium
  - Product of Array Except Self (#238) - Medium
  - Sliding Window Maximum (#239) - Hard

### 2. **String Processing** (Very Common)
- **Key Techniques:**
  - Sliding Window
  - Two Pointers
  - Hash Table for character counting
  - String matching (KMP, Rolling Hash)
  - Backtracking

- **Representative Problems:**
  - Longest Substring Without Repeating Characters (#3) - Medium
  - Minimum Window Substring (#76) - Hard
  - Valid Parentheses (#20) - Easy
  - Group Anagrams (#49) - Medium
  - Longest Palindromic Substring (#5) - Medium
  - Text Justification (#68) - Hard

### 3. **Dynamic Programming** (Very Common)
- **Sub-patterns:**
  - 1D DP (climbing stairs, house robber)
  - 2D DP (matrix path, edit distance)
  - DP with backtracking
  - Optimization problems
  - Game theory DP

- **Representative Problems:**
  - Climbing Stairs (#70) - Easy
  - Coin Change (#322) - Medium
  - Edit Distance (#72) - Hard
  - Longest Increasing Subsequence (#300) - Medium
  - Best Time to Buy and Sell Stock series (#121, 122, 123, 188, 309, 714) - Easy to Hard
  - Regular Expression Matching (#10) - Hard
  - Word Break (#139) - Medium
  - Maximum Product Subarray (#152) - Medium

### 4. **Tree Problems** (Very Common)
- **Types:**
  - Binary Tree Traversal (inorder, preorder, postorder)
  - Binary Search Tree operations
  - Depth-First Search (DFS)
  - Breadth-First Search (BFS)
  - Tree DP
  - Lowest Common Ancestor

- **Representative Problems:**
  - Binary Tree Level Order Traversal (#102) - Medium
  - Validate Binary Search Tree (#98) - Medium
  - Lowest Common Ancestor of Binary Tree (#236) - Medium
  - Serialize and Deserialize Binary Tree (#297) - Hard
  - Binary Tree Maximum Path Sum (#124) - Hard
  - Diameter of Binary Tree (#543) - Easy

### 5. **Graph Algorithms** (Common)
- **Key Techniques:**
  - DFS/BFS
  - Union Find (Disjoint Set)
  - Topological Sort
  - Shortest Path (Dijkstra, BFS)
  - Minimum Spanning Tree

- **Representative Problems:**
  - Number of Islands (#200) - Medium
  - Course Schedule (#207) - Medium
  - Course Schedule II (#210) - Medium
  - Word Ladder (#127) - Hard
  - Evaluate Division (#399) - Medium
  - Critical Connections in a Network (#1192) - Hard
  - Network Delay Time (#743) - Medium

### 6. **Backtracking** (Common)
- **Problem Types:**
  - Combinations/Permutations
  - Subset generation
  - Constraint satisfaction
  - Game problems

- **Representative Problems:**
  - Letter Combinations of a Phone Number (#17) - Medium
  - Generate Parentheses (#22) - Medium
  - Permutations (#46) - Medium
  - Subsets (#78) - Medium
  - Word Search (#79) - Medium
  - Word Search II (#212) - Hard
  - N-Queens (#51) - Hard

### 7. **Hash Table / Hash Map** (Very Common)
- **Use Cases:**
  - Counting frequencies
  - Two sum variants
  - Anagram problems
  - Caching/Memoization

- **Representative Problems:**
  - Two Sum (#1) - Easy
  - Group Anagrams (#49) - Medium
  - LRU Cache (#146) - Medium
  - Design HashMap (#706) - Easy

### 8. **Stack & Queue** (Common)
- **Sub-patterns:**
  - Monotonic Stack
  - Stack for parsing/evaluation
  - Queue for BFS
  - Design problems

- **Representative Problems:**
  - Valid Parentheses (#20) - Easy
  - Min Stack (#155) - Easy
  - Largest Rectangle in Histogram (#84) - Hard
  - Trapping Rain Water (#42) - Hard
  - Decode String (#394) - Medium
  - Daily Temperatures (#739) - Medium

### 9. **Binary Search** (Common)
- **Types:**
  - Basic binary search
  - Binary search on answer
  - Search in rotated/modified arrays
  - Binary search on 2D matrix

- **Representative Problems:**
  - Binary Search (#704) - Easy
  - Search in Rotated Sorted Array (#33) - Medium
  - Find First and Last Position (#34) - Medium
  - Median of Two Sorted Arrays (#4) - Hard
  - Kth Smallest Element in Sorted Matrix (#378) - Medium

### 10. **Greedy Algorithms** (Common)
- **Representative Problems:**
  - Jump Game (#55) - Medium
  - Jump Game II (#45) - Medium
  - Meeting Rooms II (#253) - Medium
  - Task Scheduler (#621) - Medium
  - Gas Station (#134) - Medium

### 11. **Heap (Priority Queue)** (Common)
- **Use Cases:**
  - K-th largest/smallest
  - Merge K sorted lists
  - Top K frequent elements
  - Meeting rooms

- **Representative Problems:**
  - Kth Largest Element (#215) - Medium
  - Merge k Sorted Lists (#23) - Hard
  - Top K Frequent Elements (#347) - Medium
  - Find Median from Data Stream (#295) - Hard
  - Meeting Rooms II (#253) - Medium

### 12. **Design Problems** (Common)
- **Types:**
  - Data structure design
  - System design simulation
  - Cache design

- **Representative Problems:**
  - LRU Cache (#146) - Medium
  - LFU Cache (#460) - Hard
  - Design Search Autocomplete System (#642) - Hard
  - Design In-Memory File System (#588) - Hard
  - Design Hit Counter (#362) - Medium

### 13. **Matrix Problems** (Common)
- **Key Techniques:**
  - DFS/BFS on matrix
  - DP on matrix
  - Matrix traversal patterns

- **Representative Problems:**
  - Spiral Matrix (#54) - Medium
  - Rotate Image (#48) - Medium
  - Set Matrix Zeroes (#73) - Medium
  - Number of Islands (#200) - Medium
  - Word Search (#79) - Medium

### 14. **Bit Manipulation** (Less Common but Important)
- **Representative Problems:**
  - Single Number (#136) - Easy
  - Single Number II (#137) - Medium
  - Counting Bits (#338) - Easy
  - Maximum XOR of Two Numbers (#421) - Medium

### 15. **Trie (Prefix Tree)** (Moderately Common)
- **Representative Problems:**
  - Implement Trie (#208) - Medium
  - Word Search II (#212) - Hard
  - Design Search Autocomplete System (#642) - Hard

---

## Difficulty Distribution Analysis

### Easy Problems (~25%)
- **Focus Areas:**
  - Basic array/string manipulation
  - Simple tree traversal
  - Hash table basics
  - Stack/queue fundamentals

### Medium Problems (~60%)
- **Focus Areas:**
  - Advanced array/string techniques
  - Dynamic programming
  - Tree/Graph algorithms
  - Design problems
  - Two pointers, sliding window

### Hard Problems (~15%)
- **Focus Areas:**
  - Complex DP
  - Advanced graph algorithms
  - Hard string matching
  - System design
  - Multi-dimensional optimization

---

## Most Important Problem Categories for Google

### Top Tier (Must Master):
1. **Array & String** - Two Pointers, Sliding Window
2. **Dynamic Programming** - All variants
3. **Tree & Graph** - DFS, BFS, BST operations
4. **Hash Table** - All use cases
5. **Stack & Queue** - Including monotonic stack

### Second Tier (Very Important):
6. **Backtracking** - Combinations, permutations
7. **Binary Search** - Including binary search on answer
8. **Heap/Priority Queue** - K-th problems
9. **Design Problems** - Cache, data structures
10. **Greedy Algorithms**

### Third Tier (Important):
11. **Union Find** - Connected components
12. **Trie** - String problems
13. **Bit Manipulation**
14. **Topological Sort**
15. **Segment Tree / Binary Indexed Tree**

---

## Key Patterns by Problem Type

### 1. Interval Problems
- Merge Intervals
- Meeting Rooms I & II
- Insert Interval
- Minimum Number of Arrows to Burst Balloons

### 2. Substring Problems
- Minimum Window Substring
- Longest Substring Without Repeating Characters
- Longest Substring with At Most K Distinct Characters
- Substring with Concatenation of All Words

### 3. Palindrome Problems
- Longest Palindromic Substring
- Palindrome Partitioning
- Palindromic Substrings

### 4. Stock Problems
- Best Time to Buy and Sell Stock (I-VI)

### 5. Path Problems in Matrix
- Unique Paths I & II
- Minimum Path Sum
- Dungeon Game

### 6. Word Problems
- Word Ladder
- Word Break I & II
- Word Search I & II

### 7. Calculator Problems
- Basic Calculator I, II, III

### 8. Game Theory
- Game Theory DP problems
- Predict the Winner
- Stone Game series

---

## Advanced Topics (Occasionally Tested)

1. **Rolling Hash** - String matching problems
2. **Segment Tree** - Range query problems
3. **Binary Indexed Tree (Fenwick Tree)** - Range sum queries
4. **Eulerian Circuit** - Graph traversal
5. **Shortest Path Algorithms** - Dijkstra, Bellman-Ford
6. **Minimum Spanning Tree** - Prim's, Kruskal's
7. **String Matching** - KMP, Rabin-Karp
8. **Monotonic Queue** - Sliding window problems

---

## Problem-Solving Strategies for Google Interviews

### 1. Pattern Recognition
- Identify if the problem fits a known pattern
- Look for keywords: "minimum", "maximum", "k-th", "all combinations"

### 2. Data Structure Selection
- Array/String → Two pointers, sliding window, binary search
- Tree → DFS, BFS, recursion
- Graph → DFS, BFS, Union Find, topological sort
- Need fast lookup → Hash table
- Need ordering → Heap, BST, Ordered Set

### 3. Common Optimization Techniques
- Use hash table to reduce O(n²) to O(n)
- Use binary search to reduce O(n) to O(log n)
- Use DP to avoid redundant calculations
- Use sliding window for subarray/substring problems
- Use monotonic stack for next greater/smaller problems

### 4. Space-Time Tradeoffs
- Many problems can trade space for time (memoization)
- Consider both O(n) space and O(1) space solutions

---

## Practice Recommendations

### Must-Do Problems (Top 50 for Google):
1. Two Sum (#1)
2. Longest Substring Without Repeating Characters (#3)
3. Median of Two Sorted Arrays (#4)
4. Regular Expression Matching (#10)
5. Container With Most Water (#11)
6. Letter Combinations of a Phone Number (#17)
7. Generate Parentheses (#22)
8. Merge k Sorted Lists (#23)
9. Trapping Rain Water (#42)
10. Maximum Subarray (#53)
11. Merge Intervals (#56)
12. Unique Paths (#62)
13. Minimum Window Substring (#76)
14. Largest Rectangle in Histogram (#84)
15. Decode Ways (#91)
16. Binary Tree Level Order Traversal (#102)
17. Word Ladder (#127)
18. LRU Cache (#146)
19. Word Break (#139)
20. LRU Cache (#146)
21. Edit Distance (#72)
22. Number of Islands (#200)
23. Course Schedule (#207)
24. Kth Largest Element (#215)
25. Meeting Rooms II (#253)
26. Longest Increasing Subsequence (#300)
27. Coin Change (#322)
28. Top K Frequent Elements (#347)
29. Decode String (#394)
30. Evaluate Division (#399)
31. Subarray Sum Equals K (#560)
32. Task Scheduler (#621)
33. Daily Temperatures (#739)
34. Sliding Window Maximum (#239)
35. Word Search II (#212)
36. Design Search Autocomplete System (#642)
37. Design In-Memory File System (#588)
38. Group Anagrams (#49)
39. Validate Binary Search Tree (#98)
40. Serialize and Deserialize Binary Tree (#297)
41. Critical Connections in a Network (#1192)
42. Basic Calculator I, II, III (#224, #227, #772)
43. The Skyline Problem (#218)
44. Text Justification (#68)
45. Minimum Cost to Hire K Workers (#857)
46. Split Array Largest Sum (#410)
47. Most Stones Removed with Same Row or Column (#947)
48. Fruit Into Baskets (#904)
49. Campus Bikes (#1057)
50. Next Closest Time (#681)

---

## Tags Frequency Analysis

**Most Common Tags:**
1. Array (500+)
2. String (300+)
3. Dynamic Programming (250+)
4. Hash Table (250+)
5. Tree (200+)
6. Depth-First Search (200+)
7. Math (200+)
8. Binary Search (150+)
9. Greedy (150+)
10. Breadth-First Search (150+)
11. Sorting (150+)
12. Stack (120+)
13. Two Pointers (120+)
14. Backtracking (100+)
15. Design (100+)

---

## Study Plan Recommendation

### Week 1-2: Fundamentals
- Arrays & Strings (Two Pointers, Sliding Window)
- Hash Tables
- Stacks & Queues

### Week 3-4: Trees & Graphs
- Binary Trees (DFS, BFS)
- Binary Search Trees
- Graph Traversal (DFS, BFS)

### Week 5-6: Dynamic Programming
- 1D DP
- 2D DP
- DP with Backtracking

### Week 7-8: Advanced Topics
- Binary Search (on arrays and answers)
- Backtracking
- Greedy Algorithms
- Heap/Priority Queue

### Week 9-10: System Design & Specialized
- Design Problems
- Union Find
- Trie
- Advanced Graph (Topological Sort, Shortest Path)

### Week 11-12: Mock Interviews & Review
- Practice with time constraints
- Review weak areas
- Mock interviews

---

## Conclusion

Google's LeetCode problem set emphasizes:
1. **Strong fundamentals** in arrays, strings, and hash tables
2. **Deep understanding** of trees and graphs
3. **Mastery of dynamic programming**
4. **System design** and data structure design skills
5. **Problem-solving versatility** across multiple patterns

**Key to Success:**
- Master the top 10 patterns
- Practice 200-300 problems across all difficulty levels
- Focus on understanding patterns rather than memorizing solutions
- Time yourself during practice
- Review and learn from mistakes

---

*Document Generated: 2025*
*Source: LeetCode Company Tagged Problems (Google)*
