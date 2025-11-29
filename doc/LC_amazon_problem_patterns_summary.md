# Amazon LeetCode Problem Patterns Summary

**Source:** LeetCode Company Tagged Problems (Amazon)
**Date Analyzed:** February 2022
**Total Problems:** 1125+

## Overview
This document summarizes the most frequently tested algorithmic patterns and problem types in Amazon's LeetCode problem set.

---

## Top Problem Patterns by Frequency

### 1. **Array Manipulation** (Most Common)
- **Key Techniques:**
  - Two Pointers
  - Sliding Window
  - Prefix Sum
  - Sorting
  - Greedy Algorithms

- **Representative Problems:**
  - Two Sum (#1) - Easy
  - Subarray Sum Equals K (#560) - Medium
  - Maximum Subarray (#53) - Easy
  - Merge Intervals (#56) - Medium
  - Product of Array Except Self (#238) - Medium
  - Trapping Rain Water (#42) - Hard

### 2. **String Processing** (Very Common)
- **Key Techniques:**
  - Sliding Window
  - Two Pointers
  - Hash Table for character counting
  - String matching
  - Backtracking

- **Representative Problems:**
  - Longest Substring Without Repeating Characters (#3) - Medium
  - Minimum Window Substring (#76) - Hard
  - Valid Parentheses (#20) - Easy
  - Group Anagrams (#49) - Medium
  - Longest Palindromic Substring (#5) - Medium
  - Most Common Word (#819) - Easy

### 3. **Dynamic Programming** (Very Common)
- **Sub-patterns:**
  - 1D DP (climbing stairs, house robber)
  - 2D DP (matrix path, edit distance)
  - DP with optimization
  - State machine DP

- **Representative Problems:**
  - Climbing Stairs (#70) - Easy
  - Coin Change (#322) - Medium
  - Edit Distance (#72) - Hard
  - Longest Increasing Subsequence (#300) - Medium
  - Best Time to Buy and Sell Stock series (#121, 122, 123, 188, 309, 714) - Easy to Hard
  - Regular Expression Matching (#10) - Hard
  - Word Break (#139) - Medium
  - Minimum Path Sum (#64) - Medium

### 4. **Tree Problems** (Very Common)
- **Types:**
  - Binary Tree Traversal (inorder, preorder, postorder)
  - Binary Search Tree operations
  - Depth-First Search (DFS)
  - Breadth-First Search (BFS)
  - Tree DP

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
  - Critical Connections
  - Connected Components

- **Representative Problems:**
  - Number of Islands (#200) - Medium
  - Course Schedule (#207) - Medium
  - Course Schedule II (#210) - Medium
  - Word Ladder (#127) - Hard
  - Critical Connections in a Network (#1192) - Hard
  - Clone Graph (#133) - Medium

### 6. **Design Problems** (Very Common)
- **Types:**
  - Data structure design
  - Cache design (LRU, LFU)
  - System design simulation
  - Stream processing

- **Representative Problems:**
  - LRU Cache (#146) - Medium
  - LFU Cache (#460) - Hard
  - Design Search Autocomplete System (#642) - Hard
  - Design In-Memory File System (#588) - Hard
  - Design Tic-Tac-Toe (#348) - Medium
  - Insert Delete GetRandom O(1) (#380) - Medium

### 7. **Backtracking** (Common)
- **Problem Types:**
  - Combinations/Permutations
  - Subset generation
  - Constraint satisfaction
  - Word search problems

- **Representative Problems:**
  - Letter Combinations of a Phone Number (#17) - Medium
  - Generate Parentheses (#22) - Medium
  - Permutations (#46) - Medium
  - Subsets (#78) - Medium
  - Word Search (#79) - Medium
  - Word Search II (#212) - Hard
  - Combination Sum (#39) - Medium

### 8. **Hash Table / Hash Map** (Very Common)
- **Use Cases:**
  - Counting frequencies
  - Two sum variants
  - Anagram problems
  - Caching/Memoization

- **Representative Problems:**
  - Two Sum (#1) - Easy
  - Group Anagrams (#49) - Medium
  - Subarray Sum Equals K (#560) - Medium
  - Most Common Word (#819) - Easy

### 9. **Stack & Queue** (Common)
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
  - Daily Temperatures (#739) - Medium
  - Decode String (#394) - Medium

### 10. **Heap (Priority Queue)** (Common)
- **Use Cases:**
  - K-th largest/smallest
  - Merge K sorted lists
  - Top K frequent elements
  - Meeting rooms

- **Representative Problems:**
  - Kth Largest Element (#215) - Medium
  - Merge k Sorted Lists (#23) - Hard
  - Top K Frequent Elements (#347) - Medium
  - K Closest Points to Origin (#973) - Medium
  - Meeting Rooms II (#253) - Medium

### 11. **Binary Search** (Common)
- **Types:**
  - Basic binary search
  - Binary search on answer
  - Search in rotated/modified arrays

- **Representative Problems:**
  - Binary Search (#704) - Easy
  - Search in Rotated Sorted Array (#33) - Medium
  - Find First and Last Position (#34) - Medium
  - Median of Two Sorted Arrays (#4) - Hard
  - Capacity To Ship Packages Within D Days (#1011) - Medium

### 12. **Greedy Algorithms** (Common)
- **Representative Problems:**
  - Jump Game (#55) - Medium
  - Jump Game II (#45) - Medium
  - Meeting Rooms II (#253) - Medium
  - Gas Station (#134) - Medium
  - Partition Labels (#763) - Medium

### 13. **Linked List** (Common)
- **Key Techniques:**
  - Two pointers (fast/slow)
  - Reversal
  - Merge operations
  - Cycle detection

- **Representative Problems:**
  - Reverse Linked List (#206) - Easy
  - Merge Two Sorted Lists (#21) - Easy
  - Copy List with Random Pointer (#138) - Medium
  - Add Two Numbers (#2) - Medium
  - Reverse Nodes in k-Group (#25) - Hard

### 14. **Matrix Problems** (Common)
- **Key Techniques:**
  - DFS/BFS on matrix
  - DP on matrix
  - Matrix traversal patterns

- **Representative Problems:**
  - Spiral Matrix (#54) - Medium
  - Rotate Image (#48) - Medium
  - Set Matrix Zeroes (#73) - Medium
  - Number of Islands (#200) - Medium
  - Rotting Oranges (#994) - Medium

### 15. **Sliding Window** (Common)
- **Representative Problems:**
  - Longest Substring Without Repeating Characters (#3) - Medium
  - Minimum Window Substring (#76) - Hard
  - Sliding Window Maximum (#239) - Hard
  - Longest Substring with At Most K Distinct Characters (#340) - Medium

---

## Difficulty Distribution Analysis

### Easy Problems (~20%)
- **Focus Areas:**
  - Basic array/string manipulation
  - Simple tree traversal
  - Hash table basics
  - Stack/queue fundamentals
  - Two pointers

### Medium Problems (~65%)
- **Focus Areas:**
  - Advanced array/string techniques
  - Dynamic programming
  - Tree/Graph algorithms
  - Design problems
  - Sliding window
  - Backtracking

### Hard Problems (~15%)
- **Focus Areas:**
  - Complex DP
  - Advanced graph algorithms
  - Hard string matching
  - System design
  - Multi-dimensional optimization

---

## Most Important Problem Categories for Amazon

### Top Tier (Must Master):
1. **Array & String** - Two Pointers, Sliding Window, Sorting
2. **Dynamic Programming** - All variants
3. **Tree & Graph** - DFS, BFS, BST operations
4. **Design Problems** - LRU Cache, Data structures
5. **Hash Table** - All use cases

### Second Tier (Very Important):
6. **Backtracking** - Combinations, permutations
7. **Heap/Priority Queue** - K-th problems, Top K
8. **Binary Search** - Including binary search on answer
9. **Stack & Queue** - Including monotonic stack
10. **Linked List** - Two pointers, reversal

### Third Tier (Important):
11. **Greedy Algorithms**
12. **Union Find** - Connected components
13. **Trie** - String problems
14. **Bit Manipulation**
15. **Topological Sort**

---

## Key Patterns by Problem Type

### 1. Interval Problems
- Merge Intervals (#56)
- Meeting Rooms I & II (#252, #253)
- Insert Interval (#57)

### 2. Substring Problems
- Minimum Window Substring (#76)
- Longest Substring Without Repeating Characters (#3)
- Longest Substring with At Most K Distinct Characters (#340)

### 3. Palindrome Problems
- Longest Palindromic Substring (#5)
- Palindrome Partitioning (#131)
- Valid Palindrome (#125)

### 4. Stock Problems
- Best Time to Buy and Sell Stock (I-VI) (#121, 122, 123, 188, 309, 714)

### 5. Path Problems in Matrix
- Unique Paths I & II (#62, #63)
- Minimum Path Sum (#64)

### 6. Word Problems
- Word Ladder (#127)
- Word Break I & II (#139, #140)
- Word Search I & II (#79, #212)

### 7. Calculator Problems
- Basic Calculator I, II, III (#224, #227, #772)

### 8. Cache Design
- LRU Cache (#146)
- LFU Cache (#460)

---

## Amazon-Specific Focus Areas

### 1. **Leadership Principles Integration**
Amazon interviews often connect coding problems to their Leadership Principles:
- **Customer Obsession**: Design problems, optimization
- **Ownership**: System design, end-to-end solutions
- **Invent and Simplify**: Clean, efficient code
- **Bias for Action**: Working solutions over perfect solutions

### 2. **System Design Elements**
- Scalability considerations
- Trade-offs (time vs space)
- Real-world applications
- Error handling

### 3. **Common Amazon Scenarios**
- E-commerce inventory management
- Order processing
- Recommendation systems
- Warehouse optimization
- Delivery route optimization

---

## Practice Recommendations

### Must-Do Problems (Top 50 for Amazon):
1. Two Sum (#1)
2. Add Two Numbers (#2)
3. Longest Substring Without Repeating Characters (#3)
4. Median of Two Sorted Arrays (#4)
5. Longest Palindromic Substring (#5)
6. Container With Most Water (#11)
7. Letter Combinations of a Phone Number (#17)
8. Valid Parentheses (#20)
9. Merge Two Sorted Lists (#21)
10. Generate Parentheses (#22)
11. Merge k Sorted Lists (#23)
12. Trapping Rain Water (#42)
13. Group Anagrams (#49)
14. Maximum Subarray (#53)
15. Merge Intervals (#56)
16. Minimum Window Substring (#76)
17. Word Search (#79)
18. Largest Rectangle in Histogram (#84)
19. Validate Binary Search Tree (#98)
20. Binary Tree Level Order Traversal (#102)
21. Word Ladder (#127)
22. Copy List with Random Pointer (#138)
23. Word Break (#139)
24. LRU Cache (#146)
25. Number of Islands (#200)
26. Course Schedule (#207)
27. Kth Largest Element in an Array (#215)
28. Basic Calculator II (#227)
29. Lowest Common Ancestor of Binary Tree (#236)
30. Sliding Window Maximum (#239)
31. Meeting Rooms II (#253)
32. Alien Dictionary (#269)
33. Integer to English Words (#273)
34. Serialize and Deserialize Binary Tree (#297)
35. Longest Increasing Subsequence (#300)
36. Coin Change (#322)
37. Top K Frequent Elements (#347)
38. Decode String (#394)
39. Partition Labels (#763)
40. Most Common Word (#819)
41. Rotting Oranges (#994)
42. K Closest Points to Origin (#973)
43. Prison Cells After N Days (#957)
44. Critical Connections in a Network (#1192)
45. Reorder Data in Log Files (#937)
46. Robot Bounded In Circle (#1041)
47. Concatenated Words (#472)
48. Top K Frequent Words (#692)
49. Word Ladder II (#126)
50. Design Search Autocomplete System (#642)

---

## Tags Frequency Analysis

**Most Common Tags:**
1. Array (550+)
2. String (320+)
3. Dynamic Programming (270+)
4. Hash Table (260+)
5. Tree (220+)
6. Depth-First Search (210+)
7. Breadth-First Search (180+)
8. Math (200+)
9. Binary Search (160+)
10. Greedy (160+)
11. Sorting (170+)
12. Stack (130+)
13. Two Pointers (130+)
14. Design (110+)
15. Backtracking (110+)

---

## Study Plan Recommendation

### Week 1-2: Fundamentals
- Arrays & Strings (Two Pointers, Sliding Window)
- Hash Tables
- Stacks & Queues
- Basic sorting algorithms

### Week 3-4: Trees & Graphs
- Binary Trees (DFS, BFS)
- Binary Search Trees
- Graph Traversal (DFS, BFS)
- Basic graph algorithms

### Week 5-6: Dynamic Programming
- 1D DP
- 2D DP
- DP with optimization
- Classic DP problems

### Week 7-8: Advanced Topics I
- Binary Search (on arrays and answers)
- Backtracking
- Heap/Priority Queue
- Greedy Algorithms

### Week 9-10: Advanced Topics II
- Design Problems (LRU Cache, data structures)
- Union Find
- Topological Sort
- Advanced String algorithms

### Week 11-12: System Design & Mock Interviews
- Design problems practice
- Mock interviews with time constraints
- Review weak areas
- Amazon Leadership Principles integration

---

## Amazon Interview Tips

### 1. **Communication**
- Think out loud
- Explain your approach before coding
- Discuss trade-offs
- Ask clarifying questions

### 2. **Code Quality**
- Clean, readable code
- Proper variable naming
- Edge case handling
- Time and space complexity analysis

### 3. **Problem-Solving Approach**
- Start with brute force, then optimize
- Consider multiple approaches
- Discuss trade-offs
- Test with examples

### 4. **Amazon-Specific**
- Connect solutions to real-world Amazon scenarios
- Discuss scalability
- Consider customer impact
- Show ownership mentality

---

## Conclusion

Amazon's LeetCode problem set emphasizes:
1. **Strong fundamentals** in arrays, strings, and hash tables
2. **Deep understanding** of trees and graphs
3. **Mastery of dynamic programming**
4. **System design** and data structure design skills
5. **Problem-solving versatility** across multiple patterns
6. **Real-world application** and scalability considerations

**Key to Success:**
- Master the top 10 patterns
- Practice 250-350 problems across all difficulty levels
- Focus on understanding patterns rather than memorizing solutions
- Time yourself during practice
- Review and learn from mistakes
- Practice communicating your thought process
- Connect problems to real-world scenarios

---

*Document Generated: 2025*
*Source: LeetCode Company Tagged Problems (Amazon)*
