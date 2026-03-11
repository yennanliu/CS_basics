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

### **Pattern 7: Greedy String/Sequence Building with Constraint**
- **Description**: Build a string/sequence greedily using the most frequent element, but skip it when adding it would violate a constraint (e.g., 3 consecutive same chars). Use a max-heap to always have the current most frequent element ready.
- **Examples**: LC 1405 (Longest Happy String), LC 767 (Reorganize String), LC 621 (Task Scheduler), LC 358 (Rearrange String k Distance Apart)
- **Pattern**: Max-heap ordered by count; on each step try the top element — if it violates the constraint, temporarily use the 2nd element, then put the 1st back
- **Key Trick**: Two-case loop
  1. **Case 1 — constraint violated**: poll `second`, append it, decrement, re-add if > 0; then re-add `first` (it was NOT consumed)
  2. **Case 2 — safe**: append `first`, decrement, re-add if > 0

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
| **Greedy + Constraint** | Build string avoiding consecutive repeats | Max heap | O(n log k) | Reorganize/happy string |

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
            pq.offer(new int[]{arrays[i][0], i, 0});
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
    
    pq.offer(new int[]{0, start});
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
                        pq.offer(new int[]{newDist, neighbor});
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

### Template 7: Greedy String Building with Consecutive Constraint
```java
// Java - Longest Happy String (LC 1405) / Reorganize String (LC 767)
// IDEA: Max-heap by count; two-case loop:
//   Case 1: top char would create 3 consecutive → use 2nd, put 1st back
//   Case 2: safe → use top char directly
// time = O((a+b+c) * log(3)) = O(n), space = O(1) heap size bounded by alphabet

class ValCnt {
    char val;
    int cnt;
    ValCnt(char val, int cnt) { this.val = val; this.cnt = cnt; }
}

public String longestDiverseString(int a, int b, int c) {
    PriorityQueue<ValCnt> pq = new PriorityQueue<>((x, y) -> y.cnt - x.cnt);
    if (a > 0) pq.add(new ValCnt('a', a));
    if (b > 0) pq.add(new ValCnt('b', b));
    if (c > 0) pq.add(new ValCnt('c', c));

    StringBuilder sb = new StringBuilder();

    while (!pq.isEmpty()) {
        ValCnt first = pq.poll();
        int len = sb.length();

        // Case 1: adding `first` would create 3 consecutive → use second instead
        if (len >= 2
                && sb.charAt(len - 1) == first.val
                && sb.charAt(len - 2) == first.val) {

            if (pq.isEmpty()) break;          // no alternative → stop

            ValCnt second = pq.poll();        // use 2nd most frequent
            sb.append(second.val);
            second.cnt--;

            if (second.cnt > 0) pq.add(second);
            pq.add(first);                    // first was NOT used, put it back

        // Case 2: safe to use the most frequent character
        } else {
            sb.append(first.val);
            first.cnt--;
            if (first.cnt > 0) pq.add(first);
        }
    }

    return sb.toString();
}
```

**Key Observations:**
- Always greedily pick the most frequent (max-heap ensures this).
- When the constraint is about to be violated, **temporarily skip** the top element and use the next — then **put the top back unchanged**.
- `first` is only consumed in Case 2; in Case 1 it is re-inserted untouched.
- Works for any "at most K consecutive" constraint by changing the look-back window check.

**Variant: Reorganize String (LC 767) — at most 1 consecutive**
```java
// Only Case 1 check changes: len >= 1 && sb.charAt(len-1) == first.val
// Everything else is identical to the template above.
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

#### **Greedy + Consecutive Constraint Problems**
| Problem | LC # | Key Technique | Difficulty |
|---------|------|---------------|------------|
| Longest Happy String | 1405 | Max heap + greedy two-case loop | Medium |
| Reorganize String | 767 | Max heap + greedy (no adjacent same) | Medium |
| Task Scheduler | 621 | Max heap + greedy cooldown | Medium |
| Rearrange String k Distance Apart | 358 | Max heap + greedy (k-distance) | Hard |
| Distant Barcodes | 1054 | Max heap + greedy (no adjacent same) | Medium |

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
   └── NO → Continue to 7

7. Build string/sequence with "no K consecutive same" constraint?
   ├── YES → Use Greedy + Constraint pattern
   │         ├── Max-heap ordered by frequency
   │         ├── Case 1 (constraint violated): use 2nd element, put 1st back
   │         └── Case 2 (safe): use 1st element
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
| **Greedy+Constraint** | Two-case loop | case1: use 2nd, put 1st back; case2: use 1st |

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
- In greedy+constraint pattern: forgetting to re-add `first` back to PQ in Case 1 (it was NOT consumed)

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

---

## Classic LC Problems with Java Solutions

### 2-1) Kth Largest Element in an Array (LC 215)
```java
// Java
// LC 215 - Find the kth largest element in an unsorted array
// IDEA: Use min-heap of size K
// Time: O(N log K), Space: O(K)

public int findKthLargest(int[] nums, int k) {
    // Min heap - keeps k largest elements
    PriorityQueue<Integer> minHeap = new PriorityQueue<>();

    for (int num : nums) {
        minHeap.offer(num);
        // Maintain size k
        if (minHeap.size() > k) {
            minHeap.poll();  // Remove smallest
        }
    }

    // Top of heap is kth largest
    return minHeap.peek();
}

// Alternative: Quick Select (O(N) average)
public int findKthLargest_QuickSelect(int[] nums, int k) {
    // Convert to find (n-k)th smallest (0-indexed)
    int targetIdx = nums.length - k;
    return quickSelect(nums, 0, nums.length - 1, targetIdx);
}

private int quickSelect(int[] nums, int left, int right, int k) {
    if (left == right) return nums[left];

    int pivotIdx = partition(nums, left, right);

    if (k == pivotIdx) {
        return nums[k];
    } else if (k < pivotIdx) {
        return quickSelect(nums, left, pivotIdx - 1, k);
    } else {
        return quickSelect(nums, pivotIdx + 1, right, k);
    }
}

private int partition(int[] nums, int left, int right) {
    int pivot = nums[right];
    int i = left;
    for (int j = left; j < right; j++) {
        if (nums[j] <= pivot) {
            swap(nums, i, j);
            i++;
        }
    }
    swap(nums, i, right);
    return i;
}

private void swap(int[] nums, int i, int j) {
    int temp = nums[i];
    nums[i] = nums[j];
    nums[j] = temp;
}
```

### 2-2) Top K Frequent Elements (LC 347)
```java
// Java
// LC 347 - Return the k most frequent elements
// IDEA: HashMap for frequency + Min heap of size K
// Time: O(N log K), Space: O(N)

public int[] topKFrequent(int[] nums, int k) {
    // Step 1: Count frequency
    Map<Integer, Integer> freqMap = new HashMap<>();
    for (int num : nums) {
        freqMap.put(num, freqMap.getOrDefault(num, 0) + 1);
    }

    // Step 2: Min heap based on frequency (keep k largest frequencies)
    PriorityQueue<Integer> minHeap = new PriorityQueue<>(
        (a, b) -> freqMap.get(a) - freqMap.get(b)
    );

    for (int num : freqMap.keySet()) {
        minHeap.offer(num);
        if (minHeap.size() > k) {
            minHeap.poll();  // Remove element with smallest frequency
        }
    }

    // Step 3: Build result
    int[] result = new int[k];
    for (int i = k - 1; i >= 0; i--) {
        result[i] = minHeap.poll();
    }

    return result;
}

// Alternative: Bucket Sort (O(N) time)
public int[] topKFrequent_BucketSort(int[] nums, int k) {
    Map<Integer, Integer> freqMap = new HashMap<>();
    for (int num : nums) {
        freqMap.put(num, freqMap.getOrDefault(num, 0) + 1);
    }

    // Buckets: index = frequency, value = list of numbers with that frequency
    List<Integer>[] buckets = new List[nums.length + 1];
    for (int i = 0; i < buckets.length; i++) {
        buckets[i] = new ArrayList<>();
    }

    for (Map.Entry<Integer, Integer> entry : freqMap.entrySet()) {
        buckets[entry.getValue()].add(entry.getKey());
    }

    // Collect k most frequent from highest frequency bucket
    int[] result = new int[k];
    int idx = 0;
    for (int i = buckets.length - 1; i >= 0 && idx < k; i--) {
        for (int num : buckets[i]) {
            result[idx++] = num;
            if (idx == k) break;
        }
    }

    return result;
}
```

### 2-3) Merge K Sorted Lists (LC 23)
```java
// Java
// LC 23 - Merge k sorted linked lists
// IDEA: Min heap to always get the smallest node
// Time: O(N log K) where N = total nodes, K = number of lists
// Space: O(K) for the heap

public ListNode mergeKLists(ListNode[] lists) {
    if (lists == null || lists.length == 0) {
        return null;
    }

    // Min heap: compare by node value
    PriorityQueue<ListNode> minHeap = new PriorityQueue<>(
        (a, b) -> a.val - b.val
    );

    // Add first node of each list to heap
    for (ListNode node : lists) {
        if (node != null) {
            minHeap.offer(node);
        }
    }

    // Dummy head for result
    ListNode dummy = new ListNode(0);
    ListNode curr = dummy;

    while (!minHeap.isEmpty()) {
        // Get smallest node
        ListNode smallest = minHeap.poll();
        curr.next = smallest;
        curr = curr.next;

        // Add next node from same list
        if (smallest.next != null) {
            minHeap.offer(smallest.next);
        }
    }

    return dummy.next;
}

// Alternative: Divide and Conquer
public ListNode mergeKLists_DivideConquer(ListNode[] lists) {
    if (lists == null || lists.length == 0) return null;
    return mergeRange(lists, 0, lists.length - 1);
}

private ListNode mergeRange(ListNode[] lists, int start, int end) {
    if (start == end) return lists[start];

    int mid = start + (end - start) / 2;
    ListNode left = mergeRange(lists, start, mid);
    ListNode right = mergeRange(lists, mid + 1, end);

    return mergeTwoLists(left, right);
}

private ListNode mergeTwoLists(ListNode l1, ListNode l2) {
    ListNode dummy = new ListNode(0);
    ListNode curr = dummy;

    while (l1 != null && l2 != null) {
        if (l1.val <= l2.val) {
            curr.next = l1;
            l1 = l1.next;
        } else {
            curr.next = l2;
            l2 = l2.next;
        }
        curr = curr.next;
    }

    curr.next = (l1 != null) ? l1 : l2;
    return dummy.next;
}
```

### 2-4) Find Median from Data Stream (LC 295)
```java
// Java
// LC 295 - Design a data structure to find median from a stream
// IDEA: Two heaps - max heap for smaller half, min heap for larger half
// Time: O(log N) for addNum, O(1) for findMedian
// Space: O(N)

class MedianFinder {
    // Max heap for smaller half (largest of small at top)
    private PriorityQueue<Integer> small;
    // Min heap for larger half (smallest of large at top)
    private PriorityQueue<Integer> large;

    public MedianFinder() {
        small = new PriorityQueue<>(Collections.reverseOrder());
        large = new PriorityQueue<>();
    }

    public void addNum(int num) {
        // Add to small (max heap) first
        small.offer(num);

        // Balance property: max of small <= min of large
        if (!small.isEmpty() && !large.isEmpty() &&
            small.peek() > large.peek()) {
            large.offer(small.poll());
        }

        // Size property: sizes differ by at most 1
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
        // Equal sizes - average of two middle elements
        return (small.peek() + large.peek()) / 2.0;
    }
}

/**
 * Dry Run Example:
 * addNum(1): small=[1], large=[]           → median=1
 * addNum(2): small=[1], large=[2]          → median=1.5
 * addNum(3): small=[1], large=[2,3]        → rebalance → small=[2,1], large=[3] → median=2
 * addNum(4): small=[2,1], large=[3,4]      → median=2.5
 */
```

### 2-5) Meeting Rooms II (LC 253)
```java
// Java
// LC 253 - Find minimum number of conference rooms required
// IDEA: Sort by start time, use min heap to track end times
// Time: O(N log N), Space: O(N)

public int minMeetingRooms(int[][] intervals) {
    if (intervals == null || intervals.length == 0) {
        return 0;
    }

    // Sort by start time
    Arrays.sort(intervals, (a, b) -> a[0] - b[0]);

    // Min heap to track end times of ongoing meetings
    PriorityQueue<Integer> endTimes = new PriorityQueue<>();

    // First meeting
    endTimes.offer(intervals[0][1]);

    for (int i = 1; i < intervals.length; i++) {
        int start = intervals[i][0];
        int end = intervals[i][1];

        // If current meeting starts after earliest ending meeting
        // Reuse that room (remove from heap)
        if (start >= endTimes.peek()) {
            endTimes.poll();
        }

        // Add current meeting's end time
        endTimes.offer(end);
    }

    // Heap size = number of rooms needed
    return endTimes.size();
}

// Alternative: Two Arrays (Chronological Ordering)
public int minMeetingRooms_TwoArrays(int[][] intervals) {
    int n = intervals.length;
    int[] starts = new int[n];
    int[] ends = new int[n];

    for (int i = 0; i < n; i++) {
        starts[i] = intervals[i][0];
        ends[i] = intervals[i][1];
    }

    Arrays.sort(starts);
    Arrays.sort(ends);

    int rooms = 0;
    int endPtr = 0;

    for (int i = 0; i < n; i++) {
        if (starts[i] < ends[endPtr]) {
            rooms++;  // Need new room
        } else {
            endPtr++;  // Reuse room
        }
    }

    return rooms;
}
```

### 2-6) Kth Largest Element in a Stream (LC 703)
```java
// Java
// LC 703 - Design a class to find kth largest element in a stream
// IDEA: Min heap of size K - top is always kth largest
// Time: O(log K) for add, Space: O(K)

class KthLargest {
    private PriorityQueue<Integer> minHeap;
    private int k;

    public KthLargest(int k, int[] nums) {
        this.k = k;
        this.minHeap = new PriorityQueue<>();

        for (int num : nums) {
            add(num);
        }
    }

    public int add(int val) {
        minHeap.offer(val);

        // Maintain size k
        if (minHeap.size() > k) {
            minHeap.poll();
        }

        // Top of min heap is kth largest
        return minHeap.peek();
    }
}

/**
 * Example:
 * KthLargest kthLargest = new KthLargest(3, [4, 5, 8, 2]);
 * Heap after init: [4, 5, 8] (size 3, sorted as min-heap)
 *
 * kthLargest.add(3);  // heap=[4,5,8], return 4
 * kthLargest.add(5);  // heap=[5,5,8], return 5
 * kthLargest.add(10); // heap=[5,8,10], return 5
 * kthLargest.add(9);  // heap=[8,9,10], return 8
 * kthLargest.add(4);  // heap=[8,9,10], return 8
 */
```

### 2-7) K Closest Points to Origin (LC 973)
```java
// Java
// LC 973 - Find K closest points to origin (0,0)
// IDEA: Max heap of size K (to keep K smallest distances)
// Time: O(N log K), Space: O(K)

public int[][] kClosest(int[][] points, int k) {
    // Max heap based on distance (squared, no need for sqrt)
    PriorityQueue<int[]> maxHeap = new PriorityQueue<>(
        (a, b) -> (b[0]*b[0] + b[1]*b[1]) - (a[0]*a[0] + a[1]*a[1])
    );

    for (int[] point : points) {
        maxHeap.offer(point);
        if (maxHeap.size() > k) {
            maxHeap.poll();  // Remove farthest point
        }
    }

    // Convert heap to result array
    int[][] result = new int[k][2];
    for (int i = 0; i < k; i++) {
        result[i] = maxHeap.poll();
    }

    return result;
}

// Alternative: Min heap (push all, pop k)
public int[][] kClosest_MinHeap(int[][] points, int k) {
    PriorityQueue<int[]> minHeap = new PriorityQueue<>(
        (a, b) -> (a[0]*a[0] + a[1]*a[1]) - (b[0]*b[0] + b[1]*b[1])
    );

    for (int[] point : points) {
        minHeap.offer(point);
    }

    int[][] result = new int[k][2];
    for (int i = 0; i < k; i++) {
        result[i] = minHeap.poll();
    }

    return result;
}
```

### 2-8) Kth Smallest Element in a Sorted Matrix (LC 378)
```java
// Java
// LC 378 - Find kth smallest element in n x n sorted matrix
// IDEA: Min heap with (value, row, col)
// Time: O(K log N), Space: O(N)

public int kthSmallest(int[][] matrix, int k) {
    int n = matrix.length;

    // Min heap: [value, row, col]
    PriorityQueue<int[]> minHeap = new PriorityQueue<>(
        (a, b) -> a[0] - b[0]
    );

    // Add first element of each row
    for (int i = 0; i < Math.min(n, k); i++) {
        minHeap.offer(new int[]{matrix[i][0], i, 0});
    }

    int result = 0;
    // Pop k times
    for (int i = 0; i < k; i++) {
        int[] curr = minHeap.poll();
        result = curr[0];
        int row = curr[1];
        int col = curr[2];

        // Add next element from same row
        if (col + 1 < n) {
            minHeap.offer(new int[]{matrix[row][col + 1], row, col + 1});
        }
    }

    return result;
}

// Alternative: Binary Search
public int kthSmallest_BinarySearch(int[][] matrix, int k) {
    int n = matrix.length;
    int lo = matrix[0][0];
    int hi = matrix[n - 1][n - 1];

    while (lo < hi) {
        int mid = lo + (hi - lo) / 2;
        int count = countLessOrEqual(matrix, mid);

        if (count < k) {
            lo = mid + 1;
        } else {
            hi = mid;
        }
    }

    return lo;
}

private int countLessOrEqual(int[][] matrix, int target) {
    int n = matrix.length;
    int count = 0;
    int row = n - 1;
    int col = 0;

    // Start from bottom-left corner
    while (row >= 0 && col < n) {
        if (matrix[row][col] <= target) {
            count += row + 1;  // All elements in this column up to row
            col++;
        } else {
            row--;
        }
    }

    return count;
}
```

### 2-9) Task Scheduler (LC 621)
```java
// Java
// LC 621 - Return minimum intervals to finish all tasks with cooldown
// IDEA: Greedy - always execute most frequent task
// Time: O(N), Space: O(1) - only 26 letters

public int leastInterval(char[] tasks, int n) {
    // Count frequency of each task
    int[] freq = new int[26];
    for (char task : tasks) {
        freq[task - 'A']++;
    }

    // Max heap for task frequencies
    PriorityQueue<Integer> maxHeap = new PriorityQueue<>(Collections.reverseOrder());
    for (int f : freq) {
        if (f > 0) {
            maxHeap.offer(f);
        }
    }

    int time = 0;

    while (!maxHeap.isEmpty()) {
        List<Integer> temp = new ArrayList<>();

        // Process n+1 tasks (cooldown cycle)
        for (int i = 0; i <= n; i++) {
            if (!maxHeap.isEmpty()) {
                int f = maxHeap.poll();
                if (f > 1) {
                    temp.add(f - 1);  // Task still has remaining count
                }
            }
            time++;

            // If all tasks done, break
            if (maxHeap.isEmpty() && temp.isEmpty()) {
                break;
            }
        }

        // Add back remaining tasks
        for (int f : temp) {
            maxHeap.offer(f);
        }
    }

    return time;
}

// Mathematical Approach (O(N) time, O(1) space)
public int leastInterval_Math(char[] tasks, int n) {
    int[] freq = new int[26];
    int maxFreq = 0;
    int maxCount = 0;

    for (char task : tasks) {
        freq[task - 'A']++;
        if (freq[task - 'A'] > maxFreq) {
            maxFreq = freq[task - 'A'];
            maxCount = 1;
        } else if (freq[task - 'A'] == maxFreq) {
            maxCount++;
        }
    }

    // (maxFreq - 1) full cycles + last partial cycle
    int partCount = (maxFreq - 1) * (n + 1) + maxCount;

    // Result is max of calculated intervals or total tasks
    return Math.max(partCount, tasks.length);
}
```

### 2-10) Reorganize String (LC 767)
```java
// Java
// LC 767 - Rearrange string so no adjacent characters are same
// IDEA: Max heap - always pick most frequent, alternate placement
// Time: O(N log 26) = O(N), Space: O(26) = O(1)

public String reorganizeString(String s) {
    // Count frequency
    int[] freq = new int[26];
    for (char c : s.toCharArray()) {
        freq[c - 'a']++;
    }

    // Check if possible: no char should appear more than (n+1)/2 times
    int n = s.length();
    for (int f : freq) {
        if (f > (n + 1) / 2) {
            return "";
        }
    }

    // Max heap: [frequency, char]
    PriorityQueue<int[]> maxHeap = new PriorityQueue<>(
        (a, b) -> b[0] - a[0]
    );

    for (int i = 0; i < 26; i++) {
        if (freq[i] > 0) {
            maxHeap.offer(new int[]{freq[i], i});
        }
    }

    StringBuilder sb = new StringBuilder();

    while (maxHeap.size() >= 2) {
        // Take two most frequent characters
        int[] first = maxHeap.poll();
        int[] second = maxHeap.poll();

        sb.append((char) (first[1] + 'a'));
        sb.append((char) (second[1] + 'a'));

        // Put back if remaining
        if (--first[0] > 0) maxHeap.offer(first);
        if (--second[0] > 0) maxHeap.offer(second);
    }

    // Handle last character if any
    if (!maxHeap.isEmpty()) {
        sb.append((char) (maxHeap.poll()[1] + 'a'));
    }

    return sb.toString();
}
```

### 2-11) Sliding Window Median (LC 480)
```java
// Java
// LC 480 - Return median of each sliding window of size k
// IDEA: Two heaps (TreeMap/Multiset for lazy removal)
// Time: O(N log K), Space: O(K)

public double[] medianSlidingWindow(int[] nums, int k) {
    // Use TreeMap to support removal
    TreeMap<Integer, Integer> small = new TreeMap<>(Collections.reverseOrder()); // max heap
    TreeMap<Integer, Integer> large = new TreeMap<>(); // min heap

    int smallSize = 0, largeSize = 0;
    double[] result = new double[nums.length - k + 1];

    for (int i = 0; i < nums.length; i++) {
        // Add to appropriate heap
        if (small.isEmpty() || nums[i] <= small.firstKey()) {
            add(small, nums[i]);
            smallSize++;
        } else {
            add(large, nums[i]);
            largeSize++;
        }

        // Rebalance
        while (smallSize > largeSize + 1) {
            int val = small.firstKey();
            remove(small, val);
            smallSize--;
            add(large, val);
            largeSize++;
        }
        while (largeSize > smallSize) {
            int val = large.firstKey();
            remove(large, val);
            largeSize--;
            add(small, val);
            smallSize++;
        }

        // Window is full
        if (i >= k - 1) {
            // Calculate median
            if (k % 2 == 1) {
                result[i - k + 1] = small.firstKey();
            } else {
                result[i - k + 1] = ((double) small.firstKey() + large.firstKey()) / 2.0;
            }

            // Remove element leaving window
            int toRemove = nums[i - k + 1];
            if (toRemove <= small.firstKey()) {
                remove(small, toRemove);
                smallSize--;
            } else {
                remove(large, toRemove);
                largeSize--;
            }
        }
    }

    return result;
}

private void add(TreeMap<Integer, Integer> map, int val) {
    map.put(val, map.getOrDefault(val, 0) + 1);
}

private void remove(TreeMap<Integer, Integer> map, int val) {
    int count = map.get(val);
    if (count == 1) {
        map.remove(val);
    } else {
        map.put(val, count - 1);
    }
}
```

### 2-12) Ugly Number II (LC 264)
```java
// Java
// LC 264 - Find nth ugly number (only prime factors 2, 3, 5)
// IDEA: Min heap to generate ugly numbers in order
// Time: O(N log N), Space: O(N)

public int nthUglyNumber(int n) {
    PriorityQueue<Long> minHeap = new PriorityQueue<>();
    Set<Long> seen = new HashSet<>();

    minHeap.offer(1L);
    seen.add(1L);

    int[] primes = {2, 3, 5};
    long ugly = 1;

    for (int i = 0; i < n; i++) {
        ugly = minHeap.poll();

        for (int prime : primes) {
            long next = ugly * prime;
            if (!seen.contains(next)) {
                seen.add(next);
                minHeap.offer(next);
            }
        }
    }

    return (int) ugly;
}

// Three Pointers Approach (O(N) time, O(N) space)
public int nthUglyNumber_ThreePointers(int n) {
    int[] ugly = new int[n];
    ugly[0] = 1;

    int p2 = 0, p3 = 0, p5 = 0;

    for (int i = 1; i < n; i++) {
        int next2 = ugly[p2] * 2;
        int next3 = ugly[p3] * 3;
        int next5 = ugly[p5] * 5;

        int next = Math.min(next2, Math.min(next3, next5));
        ugly[i] = next;

        // Move pointers (handle duplicates)
        if (next == next2) p2++;
        if (next == next3) p3++;
        if (next == next5) p5++;
    }

    return ugly[n - 1];
}
```

### 2-13) Network Delay Time (LC 743 - Dijkstra)
```java
// Java
// LC 743 - Find time for all nodes to receive signal
// IDEA: Dijkstra's shortest path algorithm
// Time: O(E log V), Space: O(V + E)

public int networkDelayTime(int[][] times, int n, int k) {
    // Build adjacency list
    Map<Integer, List<int[]>> graph = new HashMap<>();
    for (int[] time : times) {
        graph.computeIfAbsent(time[0], x -> new ArrayList<>())
             .add(new int[]{time[1], time[2]});
    }

    // Min heap: [distance, node]
    PriorityQueue<int[]> minHeap = new PriorityQueue<>(
        (a, b) -> a[0] - b[0]
    );

    int[] dist = new int[n + 1];
    Arrays.fill(dist, Integer.MAX_VALUE);
    dist[k] = 0;

    minHeap.offer(new int[]{0, k});

    while (!minHeap.isEmpty()) {
        int[] curr = minHeap.poll();
        int d = curr[0];
        int node = curr[1];

        // Skip if already processed with shorter distance
        if (d > dist[node]) continue;

        if (graph.containsKey(node)) {
            for (int[] edge : graph.get(node)) {
                int neighbor = edge[0];
                int weight = edge[1];
                int newDist = d + weight;

                if (newDist < dist[neighbor]) {
                    dist[neighbor] = newDist;
                    minHeap.offer(new int[]{newDist, neighbor});
                }
            }
        }
    }

    // Find max distance (time for all nodes to receive)
    int maxDist = 0;
    for (int i = 1; i <= n; i++) {
        if (dist[i] == Integer.MAX_VALUE) {
            return -1;  // Unreachable node
        }
        maxDist = Math.max(maxDist, dist[i]);
    }

    return maxDist;
}
```

### 2-14) Sort Characters By Frequency (LC 451)
```java
// Java
// LC 451 - Sort characters in string by frequency (descending)
// IDEA: Count frequency, use max heap to build result
// Time: O(N log K) where K = unique chars, Space: O(N)

public String frequencySort(String s) {
    // Count frequency
    Map<Character, Integer> freq = new HashMap<>();
    for (char c : s.toCharArray()) {
        freq.put(c, freq.getOrDefault(c, 0) + 1);
    }

    // Max heap based on frequency
    PriorityQueue<Character> maxHeap = new PriorityQueue<>(
        (a, b) -> freq.get(b) - freq.get(a)
    );

    maxHeap.addAll(freq.keySet());

    // Build result
    StringBuilder sb = new StringBuilder();
    while (!maxHeap.isEmpty()) {
        char c = maxHeap.poll();
        int count = freq.get(c);
        for (int i = 0; i < count; i++) {
            sb.append(c);
        }
    }

    return sb.toString();
}

// Bucket Sort Alternative (O(N) time)
public String frequencySort_Bucket(String s) {
    Map<Character, Integer> freq = new HashMap<>();
    for (char c : s.toCharArray()) {
        freq.put(c, freq.getOrDefault(c, 0) + 1);
    }

    // Bucket: index = frequency
    List<Character>[] buckets = new List[s.length() + 1];
    for (int i = 0; i < buckets.length; i++) {
        buckets[i] = new ArrayList<>();
    }

    for (Map.Entry<Character, Integer> entry : freq.entrySet()) {
        buckets[entry.getValue()].add(entry.getKey());
    }

    StringBuilder sb = new StringBuilder();
    for (int i = buckets.length - 1; i >= 0; i--) {
        for (char c : buckets[i]) {
            for (int j = 0; j < i; j++) {
                sb.append(c);
            }
        }
    }

    return sb.toString();
}
```

### 2-15) Last Stone Weight (LC 1046)
```java
// Java
// LC 1046 - Smash two heaviest stones, return remaining weight
// IDEA: Max heap - always get two largest
// Time: O(N log N), Space: O(N)

public int lastStoneWeight(int[] stones) {
    // Max heap
    PriorityQueue<Integer> maxHeap = new PriorityQueue<>(
        Collections.reverseOrder()
    );

    for (int stone : stones) {
        maxHeap.offer(stone);
    }

    while (maxHeap.size() > 1) {
        int stone1 = maxHeap.poll();  // Heaviest
        int stone2 = maxHeap.poll();  // Second heaviest

        if (stone1 != stone2) {
            maxHeap.offer(stone1 - stone2);  // Remaining weight
        }
        // If equal, both destroyed
    }

    return maxHeap.isEmpty() ? 0 : maxHeap.peek();
}
```

---

## Quick Reference: PQ Pattern → Problem Mapping

| Pattern | Classic Problems | Key Insight |
|---------|-----------------|-------------|
| **Top K** | LC 215, 347, 692, 973, 1046 | Min heap size K for K largest |
| **K-Way Merge** | LC 23, 378, 373 | Track (value, source_index) |
| **Two Heaps** | LC 295, 480 | Max heap for small, min heap for large |
| **Scheduling** | LC 253, 621, 767 | Sort by start/priority, heap for end times |
| **Dijkstra** | LC 743, 787, 1514, 1631 | Min heap with (distance, node) |
| **Stream** | LC 703, 295, 346 | Fixed size heap or two heaps |
| **Ugly Numbers** | LC 264, 313, 373 | Generate in sorted order |
| **Greedy+Constraint** | LC 1405, 767, 621, 358, 1054 | Max-heap + two-case loop (use 2nd if constraint violated, put 1st back) |

## LC Examples

### 2-1) Kth Largest Element in a Stream (LC 703) — Min-Heap of Size K
> Maintain a min-heap of size k; the top is always the kth largest element.

```java
// LC 703 - Kth Largest Element in a Stream
// IDEA: Min-heap of size k — top = kth largest
// time = O(N log k), space = O(k)
class KthLargest {
    PriorityQueue<Integer> heap;
    int k;
    public KthLargest(int k, int[] nums) {
        this.k = k;
        this.heap = new PriorityQueue<>();
        for (int num : nums) add(num);
    }
    public int add(int val) {
        heap.offer(val);
        if (heap.size() > k) heap.poll();
        return heap.peek();
    }
}
```

### 2-2) Top K Frequent Elements (LC 347) — Min-Heap with Frequency
> Count frequencies with HashMap, then maintain min-heap of size k by frequency.

```java
// LC 347 - Top K Frequent Elements
// IDEA: HashMap for frequency + min-heap of size k ordered by frequency
// time = O(N log k), space = O(N)
public int[] topKFrequent(int[] nums, int k) {
    Map<Integer, Integer> freq = new HashMap<>();
    for (int num : nums) freq.merge(num, 1, Integer::sum);
    PriorityQueue<Integer> pq = new PriorityQueue<>((a, b) -> freq.get(a) - freq.get(b));
    for (int key : freq.keySet()) {
        pq.offer(key);
        if (pq.size() > k) pq.poll();
    }
    int[] ans = new int[k];
    for (int i = k - 1; i >= 0; i--) ans[i] = pq.poll();
    return ans;
}
```

### 2-3) Merge K Sorted Lists (LC 23) — Min-Heap
> Use min-heap to always extract the global minimum node across all lists.

```java
// LC 23 - Merge K Sorted Lists
// IDEA: Min-heap ordered by node value; pop min, push its next
// time = O(N log k), space = O(k)  N = total nodes, k = number of lists
public ListNode mergeKLists(ListNode[] lists) {
    PriorityQueue<ListNode> pq = new PriorityQueue<>((a, b) -> a.val - b.val);
    for (ListNode node : lists) if (node != null) pq.offer(node);
    ListNode dummy = new ListNode(0), curr = dummy;
    while (!pq.isEmpty()) {
        curr.next = pq.poll();
        curr = curr.next;
        if (curr.next != null) pq.offer(curr.next);
    }
    return dummy.next;
}
```
