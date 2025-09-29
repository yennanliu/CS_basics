# LeetCode Pattern Guide

⸻

## 🔹 1. Arrays & Strings

### **Two Pointers**
- **Opposite Ends**: Move from both ends toward center
  - LC 1: Two Sum (sorted), LC 15: 3Sum, LC 42: Trapping Rain Water
  - LC 125: Valid Palindrome, LC 167: Two Sum II
- **Fast/Slow Pointers**: Different speeds for cycle detection
  - LC 141: Linked List Cycle, LC 142: Cycle II, LC 287: Find Duplicate
  - LC 26: Remove Duplicates, LC 80: Remove Duplicates II
- **Same Direction**: Both pointers move in same direction
  - LC 283: Move Zeros, LC 75: Sort Colors, LC 11: Container With Water

### **Sliding Window**
- **Fixed Size Window**: Constant window size
  - LC 643: Max Average Subarray, LC 1456: Max Vowels in Substring
  - LC 424: Longest Repeating Character Replacement
- **Variable Size Window**: Expand/shrink based on conditions
  - LC 3: Longest Substring Without Repeating, LC 76: Minimum Window Substring
  - LC 209: Minimum Size Subarray Sum, LC 904: Fruit Into Baskets
- **Template**: Expand right, shrink left when invalid

### **Prefix Sum & Difference Array**
- **Prefix Sum**: Efficient subarray sum calculations
  - LC 560: Subarray Sum Equals K, LC 523: Continuous Subarray Sum
  - LC 325: Maximum Size Subarray Sum Equals K
- **2D Prefix Sum**: Matrix range sum queries
  - LC 304: Range Sum Query 2D, LC 1314: Matrix Block Sum
- **Difference Array**: Range updates in O(1)
  - LC 1109: Corporate Flight Bookings, LC 370: Range Addition

### **Binary Search**
- **Classic Search**: Find target in sorted array
  - LC 704: Binary Search, LC 35: Search Insert Position
  - LC 34: Find First/Last Position, LC 33: Search in Rotated Array
- **Binary Search on Answer**: Search answer space
  - LC 875: Koko Eating Bananas, LC 1011: Ship Packages in D Days
  - LC 410: Split Array Largest Sum, LC 774: Minimize Max Distance
- **Matrix Binary Search**: 2D sorted matrix
  - LC 74: Search 2D Matrix, LC 240: Search 2D Matrix II

### **Sorting + Greedy**
- **Intervals**: Merge, schedule, non-overlapping
  - LC 56: Merge Intervals, LC 57: Insert Interval
  - LC 435: Non-overlapping Intervals, LC 252: Meeting Rooms
- **Array Pairing**: Optimal matching strategies
  - LC 455: Assign Cookies, LC 881: Boats to Save People
  - LC 870: Advantage Shuffle, LC 976: Largest Perimeter Triangle

### **String Algorithms**
- **Pattern Matching**: KMP, Rabin-Karp, Z-algorithm
  - LC 28: Implement strStr(), LC 214: Shortest Palindrome
- **Palindromes**: Center expansion, Manacher's algorithm
  - LC 5: Longest Palindromic Substring, LC 647: Palindromic Substrings

⸻

## 🔹 2. Linked List

### **Reversal Patterns**
- **Basic Reversal**: Iterative and recursive approaches
  - LC 206: Reverse Linked List, LC 92: Reverse Linked List II
- **Group Reversal**: Reverse in groups of k
  - LC 25: Reverse Nodes in k-Group, LC 24: Swap Nodes in Pairs
- **Conditional Reversal**: Reverse based on conditions
  - LC 2130: Maximum Twin Sum, LC 143: Reorder List

### **Cycle Detection & Fast/Slow Pointers**
- **Floyd's Algorithm**: Detect cycle and find cycle start
  - LC 141: Linked List Cycle, LC 142: Linked List Cycle II
- **Middle Element**: Find middle using fast/slow pointers
  - LC 876: Middle of Linked List, LC 234: Palindrome Linked List
- **Remove Nth Node**: Use gap between fast/slow pointers
  - LC 19: Remove Nth Node from End

### **Merge & Sort**
- **Two List Merge**: Merge two sorted lists
  - LC 21: Merge Two Sorted Lists, LC 1669: Merge In Between
- **Multiple List Merge**: Use heap or divide & conquer
  - LC 23: Merge k Sorted Lists
- **List Sorting**: Sort linked list efficiently
  - LC 148: Sort List (merge sort), LC 147: Insertion Sort List

### **Dummy Head Technique**
- **Node Removal**: Easier deletion with dummy head
  - LC 203: Remove Linked List Elements, LC 83: Remove Duplicates
- **List Construction**: Build result list with dummy head
  - LC 2: Add Two Numbers, LC 445: Add Two Numbers II
- **Partition**: Split list based on criteria
  - LC 86: Partition List, LC 725: Split Linked List in Parts

### **Advanced Operations**
- **Deep Copy**: Clone complex linked structures
  - LC 138: Copy List with Random Pointer
- **Intersection**: Find intersection of two lists
  - LC 160: Intersection of Two Linked Lists
- **Design**: Implement linked list based structures
  - LC 146: LRU Cache, LC 460: LFU Cache

⸻

## 🔹 3. Binary Trees

### **DFS Traversal Patterns**
- **Preorder**: Root → Left → Right (top-down)
  - LC 144: Binary Tree Preorder, LC 257: Binary Tree Paths
  - LC 112: Path Sum, LC 113: Path Sum II
- **Inorder**: Left → Root → Right (BST sorted order)
  - LC 94: Binary Tree Inorder, LC 98: Validate BST
  - LC 230: Kth Smallest in BST, LC 285: Inorder Successor
- **Postorder**: Left → Right → Root (bottom-up)
  - LC 145: Binary Tree Postorder, LC 543: Diameter of Tree
  - LC 124: Binary Tree Maximum Path Sum

### **BFS / Level Order Patterns**
- **Level-by-Level**: Process nodes level by level
  - LC 102: Level Order Traversal, LC 107: Level Order II
  - LC 199: Right Side View, LC 515: Find Largest Value
- **Zigzag Traversal**: Alternate left-right, right-left
  - LC 103: Binary Tree Zigzag Traversal
- **Connect Pointers**: Link nodes at same level
  - LC 116: Populating Next Right Pointers, LC 117: Populating Next Right II

### **Tree Construction (Divide & Conquer)**
- **From Traversals**: Build tree from inorder/preorder/postorder
  - LC 105: Construct from Preorder/Inorder, LC 106: Construct from Inorder/Postorder
  - LC 889: Construct from Preorder/Postorder
- **From Special Arrays**: Build tree from other representations
  - LC 108: Convert Sorted Array to BST, LC 109: Convert Sorted List to BST
  - LC 297: Serialize/Deserialize Binary Tree

### **Binary Search Tree Patterns**
- **BST Validation**: Check BST property
  - LC 98: Validate BST, LC 99: Recover BST
- **BST Search & Insert**: Utilize BST property
  - LC 700: Search in BST, LC 701: Insert into BST
  - LC 450: Delete Node in BST
- **BST Statistics**: Find kth element, closest value
  - LC 230: Kth Smallest, LC 272: Closest BST Values
- **LCA in BST**: Leverage BST ordering
  - LC 235: LCA in BST, LC 270: Closest BST Value

### **Tree DP (Bottom-Up)**
- **Subtree Properties**: Calculate properties from children
  - LC 543: Diameter of Tree, LC 124: Maximum Path Sum
  - LC 687: Longest Univalue Path, LC 968: Binary Tree Cameras
- **House Robber**: Optimal selection with constraints
  - LC 337: House Robber III
- **Tree Coloring**: Assign colors/states optimally
  - LC 979: Distribute Coins, LC 1145: Binary Tree Coloring Game

### **Path & Ancestor Problems**
- **Root to Leaf Paths**: All paths from root to leaves
  - LC 257: Binary Tree Paths, LC 113: Path Sum II
- **Any Path**: Paths between any two nodes
  - LC 124: Binary Tree Maximum Path Sum, LC 687: Longest Univalue Path
- **Lowest Common Ancestor**: Find LCA of two nodes
  - LC 236: LCA of Binary Tree, LC 1644: LCA II, LC 1650: LCA III

### **Tree Modification**
- **Tree Flattening**: Convert tree to linked list structure
  - LC 114: Flatten Binary Tree, LC 430: Flatten Multilevel List
- **Tree Mirroring**: Mirror or invert tree
  - LC 226: Invert Binary Tree, LC 951: Flip Equivalent Trees
- **Pruning**: Remove subtrees based on conditions
  - LC 814: Binary Tree Pruning, LC 1325: Delete Leaves with Given Value

⸻

## 🔹 4. Graphs

### **Graph Traversal**
- **BFS (Breadth-First Search)**: Level-by-level exploration
  - LC 200: Number of Islands, LC 994: Rotting Oranges
  - LC 127: Word Ladder, LC 815: Bus Routes
  - **Shortest Path** (unweighted): LC 1091: Shortest Path in Binary Matrix
- **DFS (Depth-First Search)**: Go deep then backtrack
  - LC 695: Max Area of Island, LC 130: Surrounded Regions
  - LC 417: Pacific Atlantic Water Flow, LC 79: Word Search
  - **Connected Components**: LC 323: Number of Connected Components

### **Union-Find (Disjoint Set Union)**
- **Basic Union-Find**: Connect components dynamically
  - LC 200: Number of Islands, LC 547: Number of Provinces
  - LC 684: Redundant Connection, LC 685: Redundant Connection II
- **Union by Rank + Path Compression**: Optimized operations
  - LC 721: Accounts Merge, LC 990: Satisfiability of Equality Equations
- **Applications**: Minimum Spanning Tree, cycle detection
  - LC 1135: Connecting Cities, LC 1584: Min Cost to Connect Points

### **Topological Sort**
- **Kahn's Algorithm (BFS)**: Indegree-based approach
  - LC 207: Course Schedule, LC 210: Course Schedule II
  - LC 269: Alien Dictionary, LC 1136: Parallel Courses
- **DFS-based**: Detect cycles and generate topological order
  - LC 802: Find Eventual Safe States
- **Applications**: Task scheduling, dependency resolution

### **Shortest Path Algorithms**
- **Dijkstra's Algorithm**: Single-source shortest path (non-negative weights)
  - LC 743: Network Delay Time, LC 787: Cheapest Flights Within K Stops
  - LC 1631: Path With Minimum Effort, LC 1514: Path with Maximum Probability
- **Bellman-Ford**: Handle negative edge weights
  - Detect negative cycles, shortest path with constraints
- **Floyd-Warshall**: All-pairs shortest paths
  - LC 1334: Find City With Smallest Number of Neighbors

### **Advanced Graph Patterns**
- **Bipartite Graph**: Two-coloring using BFS/DFS
  - LC 785: Is Graph Bipartite, LC 886: Possible Bipartition
- **Minimum Spanning Tree**: Kruskal's and Prim's algorithms
  - LC 1135: Connecting Cities, LC 1584: Min Cost to Connect All Points
- **Strongly Connected Components**: Tarjan's and Kosaraju's algorithms
  - Find bridges and articulation points
- **Maximum Flow**: Ford-Fulkerson, Edmonds-Karp
  - Network flow problems, bipartite matching

### **Grid-Based Graph Problems**
- **Island Problems**: Connected components in 2D grid
  - LC 200: Number of Islands, LC 695: Max Area of Island
  - LC 305: Number of Islands II (Union-Find)
- **Path Finding in Grid**: BFS for shortest path
  - LC 1091: Shortest Path in Binary Matrix, LC 542: 01 Matrix
  - LC 934: Shortest Bridge, LC 1293: Shortest Path in Grid with Obstacles

### **Special Graph Types**
- **Tree Graphs**: No cycles, n-1 edges for n nodes
  - LC 310: Minimum Height Trees, LC 1245: Tree Diameter
- **DAG (Directed Acyclic Graph)**: Topological sort possible
  - LC 329: Longest Increasing Path in Matrix
- **Complete Graphs**: Every pair of vertices connected
  - Traveling salesman variants, Hamiltonian paths

⸻

## 🔹 5. Dynamic Programming (DP)

### **1D Linear DP**
- **Fibonacci Variants**: Classic sequence problems
  - LC 70: Climbing Stairs, LC 198: House Robber, LC 213: House Robber II
  - LC 91: Decode Ways, LC 264: Ugly Number II
- **Decision DP**: Include/exclude current element
  - LC 198: House Robber, LC 152: Maximum Product Subarray
  - LC 53: Maximum Subarray (Kadane's Algorithm)
- **Counting DP**: Count ways to achieve target
  - LC 62: Unique Paths, LC 70: Climbing Stairs, LC 96: Unique BSTs

### **2D Grid DP**
- **Path Counting**: Count paths in matrix
  - LC 62: Unique Paths, LC 63: Unique Paths II
  - LC 64: Minimum Path Sum, LC 120: Triangle
- **Matrix Optimization**: Find optimal submatrix
  - LC 221: Maximal Square, LC 85: Maximal Rectangle
  - LC 1277: Count Square Submatrices with All Ones
- **2D Decision**: DP on 2D state space
  - LC 174: Dungeon Game, LC 741: Cherry Pickup

### **Knapsack Patterns**
- **0/1 Knapsack**: Each item used once
  - LC 416: Partition Equal Subset Sum, LC 494: Target Sum
  - LC 1049: Last Stone Weight II
- **Unbounded Knapsack**: Items can be reused
  - LC 322: Coin Change, LC 518: Coin Change II
  - LC 279: Perfect Squares, LC 377: Combination Sum IV
- **Multi-dimensional Knapsack**: Multiple constraints
  - LC 474: Ones and Zeroes, LC 879: Profitable Schemes

### **String DP**
- **Edit Distance**: String transformation
  - LC 72: Edit Distance, LC 583: Delete Operation for Two Strings
  - LC 712: Minimum ASCII Delete Sum, LC 97: Interleaving String
- **Longest Common Subsequence (LCS)**:
  - LC 1143: Longest Common Subsequence, LC 1035: Uncrossed Lines
  - LC 300: Longest Increasing Subsequence
- **Palindromes**: Palindromic substrings and subsequences
  - LC 5: Longest Palindromic Substring, LC 516: Longest Palindromic Subsequence
  - LC 131: Palindrome Partitioning, LC 132: Palindrome Partitioning II
- **Word Break**: String segmentation
  - LC 139: Word Break, LC 140: Word Break II

### **Interval DP**
- **Matrix Chain**: Optimal parenthesization
  - LC 312: Burst Balloons, LC 1000: Minimum Cost to Merge Stones
- **Range Queries**: Optimize over intervals
  - LC 877: Stone Game, LC 1039: Minimum Score Triangulation
- **Palindromic Ranges**: Check palindromes in ranges
  - LC 1312: Minimum Insertion Steps to Make String Palindrome

### **State Compression DP (Bitmask)**
- **Traveling Salesman**: Visit all states
  - LC 943: Find Shortest Superstring, LC 980: Unique Paths III
- **Subset DP**: Track which elements are used
  - LC 691: Stickers to Spell Word, LC 1125: Smallest Sufficient Team
- **Graph State**: Track visited nodes
  - LC 847: Shortest Path Visiting All Nodes

### **Advanced DP Patterns**
- **Tree DP**: DP on tree structures
  - LC 337: House Robber III, LC 968: Binary Tree Cameras
  - LC 124: Binary Tree Maximum Path Sum
- **Digit DP**: DP on number digits
  - Count numbers with certain properties
- **Probability DP**: Expected value calculations
  - LC 808: Soup Servings, LC 837: New 21 Game
- **Game Theory DP**: Minimax with optimal play
  - LC 292: Nim Game, LC 464: Can I Win, LC 486: Predict the Winner

### **DP Optimization Techniques**
- **Space Optimization**: Reduce from 2D to 1D
  - Rolling array, only keep necessary states
- **Monotonic Queue/Stack**: Optimize range queries
  - LC 239: Sliding Window Maximum with DP
- **Matrix Exponentiation**: Fast computation of recurrence
  - For large Fibonacci-like sequences

⸻

## 🔹 6. Backtracking

### **Subsets & Combinations**
- **Subsets**: Generate all possible subsets
  - LC 78: Subsets, LC 90: Subsets II (with duplicates)
  - LC 320: Generalized Abbreviation
- **Combinations**: Choose k elements from n
  - LC 77: Combinations, LC 39: Combination Sum
  - LC 40: Combination Sum II, LC 216: Combination Sum III
- **Template**: Use start index to avoid duplicates

### **Permutations**
- **Basic Permutations**: All arrangements
  - LC 46: Permutations, LC 47: Permutations II (with duplicates)
  - LC 31: Next Permutation, LC 60: Permutation Sequence
- **Conditional Permutations**: With constraints
  - LC 996: Number of Squareful Arrays
- **No start index needed** - use visited array

### **Grid & Board Problems**
- **N-Queens**: Place queens without conflicts
  - LC 51: N-Queens, LC 52: N-Queens II
- **Word Search**: Find words in grid
  - LC 79: Word Search, LC 212: Word Search II (Trie)
- **Path Finding**: Explore all paths
  - LC 980: Unique Paths III, LC 1219: Path with Maximum Gold

### **String Partitioning**
- **Palindrome Partitioning**: Split into palindromes
  - LC 131: Palindrome Partitioning, LC 132: Palindrome Partitioning II
- **IP Address**: Generate valid IP addresses
  - LC 93: Restore IP Addresses
- **Word Break**: Split string using dictionary
  - LC 140: Word Break II

### **Constraint Satisfaction**
- **Sudoku**: Fill grid with constraints
  - LC 37: Sudoku Solver
- **Expression**: Generate target expressions
  - LC 282: Expression Add Operators, LC 241: Different Ways to Add Parentheses
- **Parentheses**: Generate valid combinations
  - LC 22: Generate Parentheses

### **Advanced Backtracking**
- **Game Playing**: Optimal strategy
  - LC 464: Can I Win, LC 294: Flip Game II
- **Scheduling**: Assign tasks optimally
  - LC 698: Partition to K Equal Sum Subsets
- **Graph Coloring**: Color nodes without conflicts
  - Extension of N-Queens concept

### **Pruning Techniques**
- **Constraint Pruning**: Early termination on invalid states
  - Check bounds before recursive calls
- **Bound Pruning**: Use upper/lower bounds
  - LC 39: Combination Sum (sort + break when sum exceeds)
- **Symmetry Pruning**: Skip equivalent states
  - LC 40: Combination Sum II (skip duplicates at same level)
- **Memoization**: Cache results for repeated states
  - Convert backtracking to dynamic programming

### **Backtracking Templates**
- **Choice-based**: Make choice → recurse → undo
- **Index-based**: Process each position sequentially
- **State-based**: Track current state and modify it

⸻

## 🔹 7. Heaps & Priority Queues

### **Top-K Problems**
- **Kth Largest/Smallest**: Use heap to maintain k elements
  - LC 215: Kth Largest Element, LC 703: Kth Largest in Stream
  - LC 973: K Closest Points to Origin, LC 692: Top K Frequent Words
- **Frequency-based**: Combine counting with heap
  - LC 347: Top K Frequent Elements, LC 451: Sort Characters by Frequency
- **Min-heap for largest, max-heap for smallest**

### **Merge Multiple Sorted Structures**
- **Merge k Sorted Lists**: Use heap to track current minimums
  - LC 23: Merge k Sorted Lists, LC 378: Kth Smallest in Sorted Matrix
- **Merge Intervals**: Sort + heap for overlapping intervals
  - LC 253: Meeting Rooms II, LC 1229: Meeting Scheduler
- **Merge k Arrays**: Extension of merge k lists
  - LC 632: Smallest Range Covering Elements

### **Data Stream & Online Algorithms**
- **Median Maintenance**: Two heaps (max-heap + min-heap)
  - LC 295: Find Median from Data Stream
  - LC 480: Sliding Window Median
- **Running Statistics**: Maintain statistics as data arrives
  - Running kth largest, running average
- **Stream Processing**: Handle infinite data streams

### **Scheduling & Intervals**
- **Task Scheduling**: Optimize task execution
  - LC 621: Task Scheduler, LC 358: Rearrange String k Distance Apart
- **Meeting Rooms**: Schedule meetings optimally
  - LC 252: Meeting Rooms, LC 253: Meeting Rooms II
- **CPU Scheduling**: Process scheduling algorithms
  - LC 1834: Single-Threaded CPU

### **Graph Algorithms with Heaps**
- **Dijkstra's Algorithm**: Shortest path using min-heap
  - LC 743: Network Delay Time, LC 787: Cheapest Flights
- **A* Search**: Heuristic search with priority queue
- **MST (Prim's Algorithm)**: Minimum spanning tree
  - LC 1584: Min Cost to Connect All Points

### **Advanced Heap Techniques**
- **Lazy Deletion**: Mark elements as deleted instead of removing
- **Multi-level Heaps**: Heap of heaps for complex structures
- **Custom Comparators**: Define custom ordering rules
  - Lambda functions, custom objects
- **Heap + Hash Map**: Combine for O(log n) updates
  - LC 355: Design Twitter, LC 146: LRU Cache with heap

⸻

## 🔹 8. Hashing & Counting

### **Frequency Maps**
- **Character/Element Counting**: Count occurrences for analysis
  - LC 242: Valid Anagram, LC 383: Ransom Note, LC 387: First Unique Character
  - LC 169: Majority Element, LC 229: Majority Element II
- **Group by Frequency**: Categorize elements by count
  - LC 49: Group Anagrams, LC 347: Top K Frequent Elements
  - LC 451: Sort Characters by Frequency
- **Anagram Detection**: Use frequency maps or sorted strings
  - LC 438: Find All Anagrams in a String, LC 567: Permutation in String
- **Template**: `Counter()` or manual dictionary counting

### **Prefix Hash / Rolling Hash**
- **String Pattern Matching**: Rabin-Karp algorithm
  - LC 28: Find the Index of First Occurrence, LC 459: Repeated Substring Pattern
- **Substring Problems**: Efficient hash-based comparison
  - LC 187: Repeated DNA Sequences, LC 1044: Longest Duplicate Substring
- **Rolling Window Hash**: Update hash incrementally
  - LC 1316: Distinct Echo Substrings, LC 1554: Strings Differ by One Character
- **Polynomial Hash**: Base-power hashing for strings
  - Use prime modulus to reduce collisions

### **HashSet for Seen States**
- **Cycle Detection**: Track visited states
  - LC 202: Happy Number, LC 141: Linked List Cycle
  - LC 287: Find the Duplicate Number
- **Duplicate Detection**: Fast O(1) lookups
  - LC 217: Contains Duplicate, LC 219: Contains Duplicate II
  - LC 128: Longest Consecutive Sequence
- **Path Tracking**: Remember visited positions
  - LC 36: Valid Sudoku, LC 694: Number of Distinct Islands
- **State Space Search**: Avoid revisiting states
  - LC 127: Word Ladder, LC 752: Open the Lock

### **Hash-based Data Structures**
- **HashMap for Relationships**: Key-value mappings
  - LC 1: Two Sum, LC 454: 4Sum II, LC 525: Contiguous Array
  - LC 560: Subarray Sum Equals K, LC 523: Continuous Subarray Sum
- **Hash + Index Tracking**: Position-based problems
  - LC 409: Longest Palindrome, LC 290: Word Pattern
  - LC 205: Isomorphic Strings, LC 890: Find and Replace Pattern
- **Prefix Sum + Hash**: Cumulative sum with frequency
  - LC 930: Binary Subarrays with Sum, LC 974: Subarray Sums Divisible by K

### **Advanced Hashing Techniques**
- **Multi-key Hashing**: Combine multiple attributes
  - LC 356: Line Reflection, LC 447: Number of Boomerangs
- **Custom Hash Functions**: Problem-specific hashing
  - Coordinate hashing for 2D problems
  - String normalization for pattern matching
- **Hash Collision Handling**: Manage hash conflicts
  - Double hashing, separate chaining considerations

⸻

## 🔹 9. Advanced Patterns

### **Monotonic Stack / Queue**
- **Next Greater/Smaller Element**: Stack-based pattern
  - LC 496: Next Greater Element I, LC 503: Next Greater Element II
  - LC 739: Daily Temperatures, LC 901: Online Stock Span
- **Largest Rectangle**: Stack for histogram problems
  - LC 84: Largest Rectangle in Histogram, LC 85: Maximal Rectangle
- **Sliding Window Maximum**: Deque-based pattern
  - LC 239: Sliding Window Maximum, LC 1438: Longest Subarray with Difference ≤ K
- **Monotonic Property**: Maintain increasing/decreasing order
  - LC 402: Remove K Digits, LC 321: Create Maximum Number

### **Greedy Algorithms with Sorting**
- **Interval Scheduling**: Sort by end time for optimal selection
  - LC 435: Non-overlapping Intervals, LC 452: Minimum Number of Arrows
  - LC 253: Meeting Rooms II, LC 1024: Video Stitching
- **Fractional Knapsack**: Sort by value-to-weight ratio
  - LC 134: Gas Station, LC 135: Candy
- **Activity Selection**: Choose maximum non-overlapping activities
  - LC 646: Maximum Length of Pair Chain
- **Lexicographic Ordering**: String/array construction
  - LC 316: Remove Duplicate Letters, LC 1081: Smallest Subsequence

### **Binary Indexed Tree (BIT) / Segment Tree**
- **Range Sum Queries**: Efficient prefix sums
  - LC 307: Range Sum Query - Mutable, LC 308: Range Sum Query 2D
- **Inversion Count**: Count inversions in array
  - LC 315: Count of Smaller Numbers After Self
  - LC 493: Reverse Pairs, LC 327: Count of Range Sum
- **Coordinate Compression**: Handle large coordinate ranges
  - LC 218: The Skyline Problem (with Fenwick Tree)
- **Range Updates**: Lazy propagation for updates
  - Segment tree with range increment/decrement

### **Union-Find (Disjoint Set Union)**
- **Connected Components**: Group elements into components
  - LC 200: Number of Islands, LC 305: Number of Islands II
  - LC 547: Number of Provinces, LC 684: Redundant Connection
- **Path Compression**: Optimize find operations
  - LC 721: Accounts Merge, LC 737: Sentence Similarity II
- **Union by Rank**: Balance tree height
  - LC 1101: The Earliest Moment When Everyone Become Friends
- **Minimum Spanning Tree**: Kruskal's algorithm
  - LC 1135: Connecting Cities, LC 1584: Min Cost to Connect All Points

### **Trie (Prefix Tree)**
- **Word Search & Storage**: Efficient prefix operations
  - LC 208: Implement Trie, LC 211: Design Add and Search Words
  - LC 212: Word Search II, LC 472: Concatenated Words
- **Prefix Matching**: Find words with common prefixes
  - LC 14: Longest Common Prefix, LC 421: Maximum XOR
- **Autocomplete**: Suggest completions
  - LC 642: Design Search Autocomplete System
- **XOR Trie**: Binary trie for XOR problems
  - LC 421: Maximum XOR of Two Numbers
  - LC 1707: Maximum XOR With an Element From Array

### **Advanced Graph Algorithms**
- **Topological Sort**: Order nodes in DAG
  - LC 207: Course Schedule, LC 210: Course Schedule II
  - LC 269: Alien Dictionary, LC 329: Longest Increasing Path
- **Tarjan's Algorithm**: Find strongly connected components
  - LC 1192: Critical Connections in a Network
- **Bipartite Matching**: Maximum matching in bipartite graphs
  - Hungarian algorithm, König's theorem applications
- **Network Flow**: Max flow min cut problems
  - Ford-Fulkerson, Edmonds-Karp algorithms

### **String Algorithms**
- **KMP (Knuth-Morris-Pratt)**: Pattern matching with failure function
  - LC 28: Find Index of First Occurrence
- **Manacher's Algorithm**: Find all palindromes in O(n)
  - LC 5: Longest Palindromic Substring optimization
- **Z-Algorithm**: String matching and pattern finding
  - Linear time string matching
- **Suffix Arrays**: Advanced string processing
  - LC 1044: Longest Duplicate Substring (advanced approach)

### **Mathematical & Number Theory**
- **Fast Exponentiation**: Compute powers efficiently
  - LC 50: Pow(x, n), LC 372: Super Pow
- **Extended Euclidean Algorithm**: Solve Diophantine equations
  - LC 365: Water and Jug Problem
- **Sieve of Eratosthenes**: Prime number generation
  - LC 204: Count Primes, LC 279: Perfect Squares
- **Matrix Exponentiation**: Fast computation of recurrences
  - Fibonacci sequence, linear recurrence relations

### **Game Theory**
- **Minimax Algorithm**: Optimal strategy in zero-sum games
  - LC 464: Can I Win, LC 486: Predict the Winner
- **Nim Game**: XOR-based winning strategy
  - LC 292: Nim Game, LC 294: Flip Game II
- **Dynamic Programming on Games**: Game state analysis
  - LC 877: Stone Game, LC 1140: Stone Game II