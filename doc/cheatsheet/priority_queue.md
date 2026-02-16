---
layout: cheatsheet
title: "Priority Queue"
description: "Abstract data type for priority-based element processing"
category: "Data Structure"
difficulty: "Medium"
tags: ["priority-queue", "heap", "priority-based", "queue"]
patterns:
  - Priority-based processing
  - Heap implementation
  - Task scheduling
---

# Priority Queue (PQ) Data Structure 

## Overview
**Priority Queue** is an abstract data type that operates similar to a regular queue but with an added priority element. Elements are served based on their priority rather than the order they were added.

### Key Properties
- **Time Complexity**: 
  - Insert: O(log n)
  - Delete max/min: O(log n)
  - Get max/min: O(1)
  - Heapify: O(n)
- **Space Complexity**: O(n)
- **Core Idea**: Elements with higher priority are served before elements with lower priority
- **When to Use**: When you need to process elements based on priority/order, not just insertion time

### Implementation
- Usually implemented using **Binary Heap** (min-heap or max-heap)
- Can also use balanced BST or Fibonacci heap for advanced operations
- Python: `heapq` module (min-heap by default)
- Java: `PriorityQueue` class (min-heap by default)

### References
- [Python heapq documentation](https://docs.python.org/3/library/heapq.html)
- [Java PriorityQueue](https://docs.oracle.com/javase/8/docs/api/java/util/PriorityQueue.html)
- [Priority Queue Implementation Notes](https://docs.python.org/3/library/heapq.html#priority-queue-implementation-notes)

## Problem Categories

### **Pattern 1: Top K Elements**
- **Description**: Finding K largest/smallest elements efficiently
- **Examples**: LC 215, 347, 692, 973, 1985
- **Pattern**: Use min-heap for K largest, max-heap for K smallest

### **Pattern 2: Merge K Sorted**
- **Description**: Merging multiple sorted sequences
- **Examples**: LC 23, 88, 313, 373, 786
- **Pattern**: Use PQ to track current smallest/largest from each sequence

### **Pattern 3: Scheduling & Intervals**
- **Description**: Task scheduling and interval processing
- **Examples**: LC 253, 1094, 1353, 1834, 2402
- **Pattern**: Sort by start time, use PQ for end times or priorities

### **Pattern 4: Sliding Window with Order**
- **Description**: Maintaining order statistics in sliding windows
- **Examples**: LC 239, 480, 703, 1438, 2542
- **Pattern**: Use PQ to track min/max in current window

### **Pattern 5: Graph Algorithms**
- **Description**: Shortest path and MST algorithms
- **Examples**: LC 743, 787, 1514, 1584, 1631
- **Pattern**: Dijkstra's algorithm, Prim's algorithm

### **Pattern 6: Data Stream & Median**
- **Description**: Processing continuous data streams
- **Examples**: LC 295, 346, 352, 703, 1825
- **Pattern**: Two-heap technique for median, PQ for percentiles

## Templates & Algorithms

### Template Comparison Table
| Template Type | Use Case | Heap Type | Complexity | When to Use |
|---------------|----------|-----------|------------|-------------|
| **Top K Elements** | Find K largest/smallest | Min/Max heap | O(n log k) | Fixed K selection |
| **K-Way Merge** | Merge sorted lists | Min heap | O(n log k) | Multiple sorted sources |
| **Two Heaps** | Find median | Min + Max heap | O(log n) | Stream median/percentile |
| **Interval Scheduling** | Process intervals | Min heap | O(n log n) | Meeting rooms, events |
| **Graph Shortest Path** | Dijkstra's | Min heap | O(E log V) | Weighted graphs |
| **Custom Priority** | Complex ordering | Custom comparator | O(log n) | Multi-criteria sorting |

### Template 1: Top K Elements Pattern
```python
# Python - Find K largest elements
def topKElements(nums, k):
    import heapq
    
    # Min heap of size k for k largest
    min_heap = []
    
    for num in nums:
        heapq.heappush(min_heap, num)
        if len(min_heap) > k:
            heapq.heappop(min_heap)
    
    return min_heap  # Contains k largest elements

# With custom key for frequency
def topKFrequent(nums, k):
    from collections import Counter
    import heapq
    
    count = Counter(nums)
    # Use negative count for max heap effect
    return heapq.nlargest(k, count.keys(), key=count.get)
```

```java
// Java - Top K elements with frequency
public int[] topKFrequent(int[] nums, int k) {
    Map<Integer, Integer> map = new HashMap<>();
    for (int n : nums) {
        map.put(n, map.getOrDefault(n, 0) + 1);
    }
    
    // Min heap based on frequency
    PriorityQueue<Integer> pq = new PriorityQueue<>(
        (a, b) -> map.get(a) - map.get(b)
    );
    
    for (int key : map.keySet()) {
        pq.add(key);
        if (pq.size() > k) {
            pq.poll();
        }
    }
    
    int[] result = new int[k];
    for (int i = k - 1; i >= 0; i--) {
        result[i] = pq.poll();
    }
    return result;
}
```

### Template 2: K-Way Merge Pattern
```python
# Python - Merge K sorted lists
def mergeKSortedLists(lists):
    import heapq
    
    min_heap = []
    
    # Initialize with first element from each list
    for i, lst in enumerate(lists):
        if lst:
            heapq.heappush(min_heap, (lst[0], i, 0))
    
    result = []
    while min_heap:
        val, list_idx, elem_idx = heapq.heappop(min_heap)
        result.append(val)
        
        # Add next element from same list
        if elem_idx + 1 < len(lists[list_idx]):
            next_val = lists[list_idx][elem_idx + 1]
            heapq.heappush(min_heap, (next_val, list_idx, elem_idx + 1))
    
    return result
```

```java
// Java - Merge K sorted arrays
public int[] mergeKSortedArrays(int[][] arrays) {
    PriorityQueue<int[]> pq = new PriorityQueue<>(
        (a, b) -> a[0] - b[0]  // Compare values
    );
    
    int totalSize = 0;
    // Initialize PQ with first element from each array
    for (int i = 0; i < arrays.length; i++) {
        if (arrays[i].length > 0) {
            pq.offer(new int[]{% raw %}{arrays[i][0], i, 0}{% endraw %});
            totalSize += arrays[i].length;
        }
    }
    
    int[] result = new int[totalSize];
    int idx = 0;
    
    while (!pq.isEmpty()) {
        int[] curr = pq.poll();
        result[idx++] = curr[0];
        
        int arrIdx = curr[1];
        int elemIdx = curr[2];
        
        if (elemIdx + 1 < arrays[arrIdx].length) {
            pq.offer(new int[]{
                arrays[arrIdx][elemIdx + 1], 
                arrIdx, 
                elemIdx + 1
            });
        }
    }
    
    return result;
}
```

### Template 3: Two Heaps Pattern (Median Finding)
```python
# Python - Find median from data stream
class MedianFinder:
    def __init__(self):
        self.small = []  # Max heap (negate values)
        self.large = []  # Min heap
    
    def addNum(self, num):
        import heapq
        
        # Add to max heap (small values)
        heapq.heappush(self.small, -num)
        
        # Balance: ensure max of small <= min of large
        if self.small and self.large and (-self.small[0] > self.large[0]):
            val = -heapq.heappop(self.small)
            heapq.heappush(self.large, val)
        
        # Balance sizes
        if len(self.small) > len(self.large) + 1:
            val = -heapq.heappop(self.small)
            heapq.heappush(self.large, val)
        if len(self.large) > len(self.small) + 1:
            val = heapq.heappop(self.large)
            heapq.heappush(self.small, -val)
    
    def findMedian(self):
        if len(self.small) > len(self.large):
            return -self.small[0]
        if len(self.large) > len(self.small):
            return self.large[0]
        return (-self.small[0] + self.large[0]) / 2.0
```

```java
// Java - Two heaps for median
class MedianFinder {
    private PriorityQueue<Integer> small;  // Max heap
    private PriorityQueue<Integer> large;  // Min heap
    
    public MedianFinder() {
        small = new PriorityQueue<>(Collections.reverseOrder());
        large = new PriorityQueue<>();
    }
    
    public void addNum(int num) {
        small.offer(num);
        
        // Balance property
        if (!small.isEmpty() && !large.isEmpty() && 
            small.peek() > large.peek()) {
            large.offer(small.poll());
        }
        
        // Size property
        if (small.size() > large.size() + 1) {
            large.offer(small.poll());
        }
        if (large.size() > small.size() + 1) {
            small.offer(large.poll());
        }
    }
    
    public double findMedian() {
        if (small.size() > large.size()) {
            return small.peek();
        }
        if (large.size() > small.size()) {
            return large.peek();
        }
        return (small.peek() + large.peek()) / 2.0;
    }
}
```

### Template 4: Interval Scheduling Pattern
```python
# Python - Meeting rooms (minimum rooms needed)
def minMeetingRooms(intervals):
    import heapq
    
    if not intervals:
        return 0
    
    # Sort by start time
    intervals.sort(key=lambda x: x[0])
    
    # Min heap to track end times
    heap = []
    heapq.heappush(heap, intervals[0][1])
    
    for i in range(1, len(intervals)):
        # If current meeting starts after earliest end
        if intervals[i][0] >= heap[0]:
            heapq.heappop(heap)
        
        # Add current meeting end time
        heapq.heappush(heap, intervals[i][1])
    
    return len(heap)
```

```java
// Java - Interval scheduling
public int minMeetingRooms(int[][] intervals) {
    if (intervals.length == 0) return 0;
    
    // Sort by start time
    Arrays.sort(intervals, (a, b) -> a[0] - b[0]);
    
    // Min heap for end times
    PriorityQueue<Integer> pq = new PriorityQueue<>();
    pq.offer(intervals[0][1]);
    
    for (int i = 1; i < intervals.length; i++) {
        // Room becomes free
        if (intervals[i][0] >= pq.peek()) {
            pq.poll();
        }
        pq.offer(intervals[i][1]);
    }
    
    return pq.size();
}
```

### Template 5: Graph Shortest Path (Dijkstra)
```python
# Python - Dijkstra's algorithm with PQ
def dijkstra(graph, start, end):
    import heapq
    
    # Min heap: (distance, node)
    pq = [(0, start)]
    distances = {start: 0}
    visited = set()
    
    while pq:
        curr_dist, curr_node = heapq.heappop(pq)
        
        if curr_node in visited:
            continue
        visited.add(curr_node)
        
        if curr_node == end:
            return curr_dist
        
        for neighbor, weight in graph[curr_node]:
            if neighbor not in visited:
                new_dist = curr_dist + weight
                if neighbor not in distances or new_dist < distances[neighbor]:
                    distances[neighbor] = new_dist
                    heapq.heappush(pq, (new_dist, neighbor))
    
    return -1  # Path not found
```

```java
// Java - Dijkstra with Priority Queue
public int dijkstra(Map<Integer, List<int[]>> graph, int start, int end) {
    // Min heap: [distance, node]
    PriorityQueue<int[]> pq = new PriorityQueue<>(
        (a, b) -> a[0] - b[0]
    );
    
    Map<Integer, Integer> distances = new HashMap<>();
    Set<Integer> visited = new HashSet<>();
    
    pq.offer(new int[]{% raw %}{0, start}{% endraw %});
    distances.put(start, 0);
    
    while (!pq.isEmpty()) {
        int[] curr = pq.poll();
        int currDist = curr[0];
        int currNode = curr[1];
        
        if (visited.contains(currNode)) continue;
        visited.add(currNode);
        
        if (currNode == end) return currDist;
        
        if (graph.containsKey(currNode)) {
            for (int[] edge : graph.get(currNode)) {
                int neighbor = edge[0];
                int weight = edge[1];
                
                if (!visited.contains(neighbor)) {
                    int newDist = currDist + weight;
                    if (!distances.containsKey(neighbor) || 
                        newDist < distances.get(neighbor)) {
                        distances.put(neighbor, newDist);
                        pq.offer(new int[]{% raw %}{newDist, neighbor}{% endraw %});
                    }
                }
            }
        }
    }
    
    return -1;
}
```

### Template 6: Custom Priority Pattern
```python
# Python - Custom priority with multiple criteria
class Task:
    def __init__(self, name, priority, deadline):
        self.name = name
        self.priority = priority
        self.deadline = deadline
    
    def __lt__(self, other):
        # Higher priority first, then earlier deadline
        if self.priority != other.priority:
            return self.priority > other.priority
        return self.deadline < other.deadline

def processTasks(tasks):
    import heapq
    
    heap = []
    for task in tasks:
        heapq.heappush(heap, task)
    
    result = []
    while heap:
        task = heapq.heappop(heap)
        result.append(task.name)
    
    return result
```

```java
// Java - Custom comparator for complex ordering
class Task {
    String name;
    int priority;
    int deadline;
    
    Task(String name, int priority, int deadline) {
        this.name = name;
        this.priority = priority;
        this.deadline = deadline;
    }
}

public List<String> processTasks(List<Task> tasks) {
    PriorityQueue<Task> pq = new PriorityQueue<>((a, b) -> {
        // Higher priority first
        if (a.priority != b.priority) {
            return b.priority - a.priority;
        }
        // Earlier deadline first
        return a.deadline - b.deadline;
    });
    
    for (Task task : tasks) {
        pq.offer(task);
    }
    
    List<String> result = new ArrayList<>();
    while (!pq.isEmpty()) {
        result.add(pq.poll().name);
    }
    
    return result;
}
```

## Basic Operations

### Python heapq Operations
```python
import heapq

# Create heap (min-heap by default)
heap = []

# Push element
heapq.heappush(heap, 5)

# Pop smallest
smallest = heapq.heappop(heap)

# Push and pop in one operation
val = heapq.heappushpop(heap, 3)  # Push 3, then pop smallest

# Pop and push in one operation  
val = heapq.heapreplace(heap, 3)  # Pop smallest, then push 3

# Get smallest without removing
smallest = heap[0] if heap else None

# Convert list to heap in-place
nums = [3, 1, 4, 1, 5]
heapq.heapify(nums)  # O(n) time

# Get n largest/smallest
largest_k = heapq.nlargest(k, nums)
smallest_k = heapq.nsmallest(k, nums)

# Max heap trick (negate values)
max_heap = []
heapq.heappush(max_heap, -5)  # Push
max_val = -heapq.heappop(max_heap)  # Pop
```

### Java PriorityQueue Operations
```java
import java.util.*;

// Create PQ (min-heap by default)
PriorityQueue<Integer> minHeap = new PriorityQueue<>();

// Create max-heap
PriorityQueue<Integer> maxHeap = new PriorityQueue<>(Collections.reverseOrder());

// Add elements
minHeap.offer(5);
minHeap.add(3);  // Same as offer

// Remove and return smallest/largest
Integer smallest = minHeap.poll();

// Peek without removing
Integer top = minHeap.peek();

// Check if empty
boolean isEmpty = minHeap.isEmpty();

// Get size
int size = minHeap.size();

// Clear all elements
minHeap.clear();

// Custom comparator
PriorityQueue<int[]> customPQ = new PriorityQueue<>(
    (a, b) -> a[0] - b[0]  // Compare first element
);
```

## Problems by Pattern

### Pattern-Based Problem Tables

#### **Top K Elements Problems**
| Problem | LC # | Key Technique | Difficulty |
|---------|------|---------------|------------|
| Kth Largest Element in an Array | 215 | Quick select or min-heap | Medium |
| Top K Frequent Elements | 347 | Frequency map + heap | Medium |
| Top K Frequent Words | 692 | Custom comparator | Medium |
| K Closest Points to Origin | 973 | Distance calculation | Medium |
| Find K Pairs with Smallest Sums | 373 | K-way merge pattern | Medium |
| Kth Smallest Element in a Sorted Matrix | 378 | Binary search or heap | Medium |
| Find K-th Smallest Pair Distance | 719 | Binary search + sliding | Hard |
| K-th Smallest Prime Fraction | 786 | Binary search or heap | Medium |

#### **Merge K Sorted Problems**
| Problem | LC # | Key Technique | Difficulty |
|---------|------|---------------|------------|
| Merge k Sorted Lists | 23 | K-way merge | Hard |
| Merge Sorted Array | 88 | Two pointers | Easy |
| Smallest Range Covering K Lists | 632 | Multi-pointer + heap | Hard |
| Find K Pairs with Smallest Sums | 373 | Heap with pairs | Medium |
| Super Ugly Number | 313 | Multiple pointers | Medium |

#### **Scheduling & Interval Problems**
| Problem | LC # | Key Technique | Difficulty |
|---------|------|---------------|------------|
| Meeting Rooms II | 253 | Sort + min heap | Medium |
| Task Scheduler | 621 | Greedy + counting | Medium |
| Reorganize String | 767 | Max heap + greedy | Medium |
| Car Pooling | 1094 | Timeline events | Medium |
| Maximum Events That Can Be Attended | 1353 | Sort + greedy | Medium |
| Single-Threaded CPU | 1834 | Two heaps | Medium |
| Maximum Number of Tasks You Can Assign | 2071 | Binary search + greedy | Hard |

#### **Sliding Window with Order Problems**
| Problem | LC # | Key Technique | Difficulty |
|---------|------|---------------|------------|
| Sliding Window Maximum | 239 | Deque or heap | Hard |
| Sliding Window Median | 480 | Two heaps | Hard |
| Kth Largest Element in a Stream | 703 | Min heap of size k | Easy |
| Longest Continuous Subarray | 1438 | Two deques | Medium |
| Maximum Score of a Good Subarray | 1793 | Monotonic stack | Hard |

#### **Graph Algorithm Problems**
| Problem | LC # | Key Technique | Difficulty |
|---------|------|---------------|------------|
| Network Delay Time | 743 | Dijkstra's | Medium |
| Cheapest Flights Within K Stops | 787 | Modified Dijkstra | Medium |
| Path with Maximum Probability | 1514 | Dijkstra variant | Medium |
| Path With Minimum Effort | 1631 | Binary search + DFS/Dijkstra | Medium |
| Min Cost to Connect All Points | 1584 | Prim's MST | Medium |
| Swim in Rising Water | 778 | Binary search or Dijkstra | Hard |
| Reachable Nodes In Subdivided Graph | 882 | Dijkstra's | Hard |

#### **Data Stream & Median Problems**
| Problem | LC # | Key Technique | Difficulty |
|---------|------|---------------|------------|
| Find Median from Data Stream | 295 | Two heaps | Hard |
| Moving Average from Data Stream | 346 | Queue | Easy |
| Data Stream as Disjoint Intervals | 352 | TreeMap or heap | Hard |
| Kth Largest Element in a Stream | 703 | Min heap | Easy |
| Finding MK Average | 1825 | Multiset simulation | Hard |

## Pattern Selection Strategy

```
Problem Analysis Flowchart:

1. Do you need to find K largest/smallest elements?
   ├── YES → Use Top K Elements pattern
   │         ├── K largest → Min heap of size K
   │         └── K smallest → Max heap of size K
   └── NO → Continue to 2

2. Are you merging multiple sorted sequences?
   ├── YES → Use K-Way Merge pattern
   │         └── Track position in each sequence
   └── NO → Continue to 3

3. Is it a scheduling/interval problem?
   ├── YES → Use Interval Scheduling pattern
   │         ├── Sort by start time
   │         └── Use heap for end times
   └── NO → Continue to 4

4. Do you need to maintain order in sliding window?
   ├── YES → Use Sliding Window with Order
   │         └── Consider deque vs heap trade-offs
   └── NO → Continue to 5

5. Is it a graph shortest path problem?
   ├── YES → Use Dijkstra pattern with PQ
   │         └── Min heap with distances
   └── NO → Continue to 6

6. Processing data stream for statistics?
   ├── YES → Use Data Stream pattern
   │         ├── Median → Two heaps
   │         └── Top K → Fixed size heap
   └── NO → Use Custom Priority pattern
```



## Summary & Quick Reference

### Complexity Quick Reference
| Operation | Time Complexity | Space | Notes |
|-----------|-----------------|-------|-------|
| Insert | O(log n) | O(1) | Heap rebalancing |
| Extract min/max | O(log n) | O(1) | Heap rebalancing |
| Peek min/max | O(1) | O(1) | Direct access |
| Build heap | O(n) | O(n) | Bottom-up heapify |
| K-way merge | O(n log k) | O(k) | k = number of lists |
| Top K elements | O(n log k) | O(k) | Maintain heap of size k |
| Heap sort | O(n log n) | O(1) | In-place sorting |

### Template Quick Reference
| Template | Pattern | Key Code |
|----------|---------|----------|
| **Top K** | Min heap for K largest | `if len(heap) > k: heappop(heap)` |
| **K-Way Merge** | Track indices | `(val, list_idx, elem_idx)` |
| **Two Heaps** | Balance sizes | `if len(small) > len(large) + 1` |
| **Intervals** | Sort + heap | `sort by start, heap for end times` |
| **Dijkstra** | Min distance | `(distance, node)` in heap |
| **Custom** | Comparator | `__lt__` in Python, `Comparator` in Java |

### Common Patterns & Tricks

#### **Heap Direction Trick**
```python
# Python max heap using negation
max_heap = []
heapq.heappush(max_heap, -value)  # Push
max_value = -heapq.heappop(max_heap)  # Pop
```

#### **K-Size Maintenance**
```python
# Maintain exactly k elements
if len(heap) > k:
    heapq.heappop(heap)
# Now heap contains k largest (min-heap) or k smallest (max-heap)
```

#### **Lazy Deletion Pattern**
```python
# Mark as deleted without immediate removal
deleted = set()
while heap and heap[0] in deleted:
    heapq.heappop(heap)
```

#### **Two Heaps Balance**
```python
# Keep sizes balanced within 1
if len(small) > len(large) + 1:
    large.push(small.pop())
if len(large) > len(small) + 1:
    small.push(large.pop())
```

### Problem-Solving Steps

1. **Identify the Pattern**
   - Is it about K elements? → Top K pattern
   - Multiple sorted sources? → K-way merge
   - Need median/percentiles? → Two heaps
   - Graph shortest path? → Dijkstra with PQ

2. **Choose Heap Type**
   - K largest → Min heap of size K
   - K smallest → Max heap of size K
   - Median → Two heaps (max + min)

3. **Design the Element**
   - Simple value or tuple?
   - What to compare? (value, index, custom)
   - Need to track source? (for merging)

4. **Handle Edge Cases**
   - Empty heap checks
   - K > n scenarios
   - Duplicate elements
   - Custom comparator edge cases

### Common Mistakes & Tips

**🚫 Common Mistakes:**
- Using max heap for K largest (should use min heap)
- Forgetting to maintain heap size ≤ K
- Not handling empty heap before peek
- Wrong comparator direction
- Not considering duplicates in custom comparators

**✅ Best Practices:**
- Always check heap empty before peek/pop
- Use tuples for complex comparisons
- Maintain heap invariants after each operation
- Consider space-time trade-offs for large K
- Use heapify for batch initialization

### Interview Tips

1. **Clarify Requirements**
   - Ask about K relative to N
   - Confirm if duplicates are allowed
   - Check if online/offline algorithm needed

2. **Optimize for K**
   - K small → Heap approach O(n log k)
   - K ≈ n → Quick select O(n) average
   - K = 1 → Simple scan O(n)

3. **Explain Trade-offs**
   - Heap: Good for streaming, K << n
   - Sorting: Simple but O(n log n)
   - Quick select: Best average case but unstable

4. **Implementation Details**
   - Python: heapq is min-heap only
   - Java: PriorityQueue with Comparator
   - Remember heap is not sorted internally

### Advanced Techniques

#### **Indexed Priority Queue**
- Allows decrease-key operation
- Useful for Dijkstra optimization
- Track element positions in heap

#### **Fibonacci Heap**
- O(1) amortized insert, decrease-key
- O(log n) extract-min
- Complex implementation, rarely used

#### **Binary Heap Variants**
- d-ary heap: Better cache performance
- Binomial heap: Better merge operation
- Pairing heap: Simple, good practical performance

### Related Topics
- **Binary Heap**: Implementation details and properties
- **Sorting**: Heap sort algorithm
- **Graph Algorithms**: Dijkstra, Prim's MST
- **Greedy Algorithms**: Often use PQ for optimal selection
- **Data Streams**: Real-time processing with PQ
