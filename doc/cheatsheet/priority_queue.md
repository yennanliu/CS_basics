# Priority Queue (PQ) Data Structure 

## LeetCode Problem Lists

- [Heap (Priority Queue)](https://leetcode.com/problem-list/heap-priority-queue/)

## Time Complexity

| Data structure | Search   | Insert   | Delete   | Min/Max  |
| -------------- | -------- | -------- | -------- | -------- |
| Priority Queue | O(n)     | O(log n) | O(log n) | O(1)     |

> Binary-heap backed — same complexities as [heap.md](./heap.md). "Min/Max" = peek the highest-priority element (**O(1)**).

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

### **Pattern 1: Top K Elements** — LC 215
- **Description**: Finding K largest/smallest elements efficiently
- **Examples**: LC 215, 347, 692, 973, 1985
- **Pattern**: Use min-heap for K largest, max-heap for K smallest

### **Pattern 2: Merge K Sorted** — LC 23
- **Description**: Merging multiple sorted sequences
- **Examples**: LC 23, 88, 313, 373, 786
- **Pattern**: Use PQ to track current smallest/largest from each sequence

### **Pattern 3: Scheduling & Intervals** — LC 253
- **Description**: Task scheduling and interval processing
- **Examples**: LC 253, 1094, 1353, 1834, 2402
- **Pattern**: Sort by start time, use PQ for end times or priorities

### **Pattern 4: Sliding Window with Order** — LC 239
- **Description**: Maintaining order statistics in sliding windows
- **Examples**: LC 239, 480, 703, 1438, 2542
- **Pattern**: Use PQ to track min/max in current window

### **Pattern 5: Graph Algorithms** — LC 743
- **Description**: Shortest path and MST algorithms
- **Examples**: LC 743, 787, 1514, 1584, 1631
- **Pattern**: Dijkstra's algorithm, Prim's algorithm

### **Pattern 6: Data Stream & Median** — LC 295
- **Description**: Processing continuous data streams
- **Examples**: LC 295, 346, 352, 703, 1825
- **Pattern**: Two-heap technique for median, PQ for percentiles

### **Pattern 7: Greedy String/Sequence Building with Constraint** — LC 1405
- **Description**: Build a string/sequence greedily using the most frequent element, but skip it when adding it would violate a constraint (e.g., 3 consecutive same chars). Use a max-heap to always have the current most frequent element ready.
- **Examples**: LC 1405 (Longest Happy String), LC 767 (Reorganize String), LC 621 (Task Scheduler), LC 358 (Rearrange String k Distance Apart)
- **Pattern**: Max-heap ordered by count; on each step try the top element — if it violates the constraint, temporarily use the 2nd element, then put the 1st back
- **Key Trick**: Two-case loop
  1. **Case 1 — constraint violated**: poll `second`, append it, decrement, re-add if > 0; then re-add `first` (it was NOT consumed)
  2. **Case 2 — safe**: append `first`, decrement, re-add if > 0
- **Worked example**: section `2-10-2) Longest Happy String (LC 1405)` below — Python version, dry-run trace, and how the run-length cap (1 vs 2) makes LC 767 and LC 1405 differ

### **Pattern 8: PQ + Cooldown Queue (k-Distance Scheduling)** — LC 358
- **Description**: Greedily pick the most frequent element from a max-heap, then lock it in a cooldown queue for k steps before it can be reused. This is the canonical pattern for "same element must be at least k distance apart" problems.
- **Examples**: LC 358 (Rearrange String k Distance Apart), LC 621 (Task Scheduler), LC 767 (Reorganize String — k=2 special case)
- **Pattern**: Max-heap picks next element; after use, element enters a cooldown queue with `releaseTime = time + k`; when `time == releaseTime`, element is moved back to the heap
- **Key Insight**: PQ alone cannot track "last used position" — the cooldown queue acts as a k-slot delay line that automatically re-enables elements after k steps
- **When to Use**:
  1. Problem says "same element at least k apart" or "cooldown of k"
  2. Need to greedily pick most frequent available element
  3. Elements cycle through available → used → cooling → available
- **Difference from Pattern 7**: Pattern 7 checks a look-back window and swaps elements; Pattern 8 uses an explicit cooldown queue to enforce the distance, which is cleaner for variable k

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
| **PQ + Cooldown Queue** | k-distance apart scheduling | Max heap + Queue | O(n log k) | Rearrange k-dist, task scheduler |
| **PQ + Sweep Line** | Max over an active set while sweeping x | Max heap + lazy delete | O(n log n) | Skyline (218), min interval per query (1851) |
| **Greedy with Regret** | Undo the worst past decision when stuck | Max/Min heap | O(n log n) | Refueling stops (871), course schedule III (630), furthest building (1642) |
| **Sort + Fixed-Size Heap** | Objective = sum(A) × max/min(B) | Heap over A | O(n log n) | Hire K workers (857), team performance (1383) |
| **Grid Best-First (Dijkstra)** | Expand cheapest cell of an implicit grid graph | Min heap | O(mn log(mn)) | Trapping rain water II (407), 778, 1631, 1368 |

### Template 1: Top K Elements Pattern — LC 215
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

### Template 2: K-Way Merge Pattern — LC 23
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

**Variations of Template 2 (same skeleton, different bookkeeping):**

| LC | Problem | Twist on the k-way merge |
|----|---------|--------------------------|
| 632 | Smallest Range Covering Elements from K Lists | Also track the **max** of the k heap elements; every pop gives a window `[heap_min, running_max]` that covers all lists — stop when any list is exhausted |
| 355 | Design Twitter | The "k sorted lists" are the followees' tweet lists (newest first); push each followee's head into a max-heap by timestamp, pop 10 times |
| 373 / 378 | K Pairs with Smallest Sums / Kth Smallest in Sorted Matrix | Lists are **virtual** rows of a sorted grid — see section `2-16` |

```python
# python
# LC 632 - Smallest Range Covering Elements from K Lists
# IDEA: k-way merge frontier; window = [heap top, max of frontier]
# time = O(N log k), space = O(k)   N = total elements
import heapq

def smallestRange(nums):
    pq = [(row[0], i, 0) for i, row in enumerate(nums)]
    heapq.heapify(pq)
    cur_max = max(row[0] for row in nums)
    best = [pq[0][0], cur_max]

    while pq:
        val, i, j = heapq.heappop(pq)
        if cur_max - val < best[1] - best[0]:
            best = [val, cur_max]
        if j + 1 == len(nums[i]):
            break                      # a list is exhausted -> no more covering window
        nxt = nums[i][j + 1]
        cur_max = max(cur_max, nxt)
        heapq.heappush(pq, (nxt, i, j + 1))

    return best
```

### Template 3: Two Heaps Pattern (Median Finding) — LC 295
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

### Template 4: Interval Scheduling Pattern — LC 253
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

**Variation: Maximum Number of Events That Can Be Attended (LC 1353)** — twist: the heap holds **end days of currently-open events** and you sweep *day by day* (not interval by interval), attending the event that **ends soonest** each day. LC 253 counts concurrent intervals; LC 1353 *picks one per day* greedily.

```java
// java
// LC 1353 - Maximum Number of Events That Can Be Attended
// IDEA: sort by start day; each day push all events that opened, drop expired ones,
//       then attend the one with the earliest end day (min-heap)
// time = O(n log n), space = O(n)
public int maxEvents(int[][] events) {
    Arrays.sort(events, (a, b) -> Integer.compare(a[0], b[0]));
    PriorityQueue<Integer> pq = new PriorityQueue<>();   // end days of open events
    int i = 0, n = events.length, res = 0, day = 0;

    while (i < n || !pq.isEmpty()) {
        // idle -> jump to the next start day, otherwise advance one day
        day = pq.isEmpty() ? events[i][0] : day + 1;

        while (i < n && events[i][0] <= day) pq.offer(events[i++][1]);   // now open
        while (!pq.isEmpty() && pq.peek() < day) pq.poll();              // expired

        if (!pq.isEmpty()) { pq.poll(); res++; }   // attend earliest-ending event
    }
    return res;
}
```

```python
# python
# LC 1353 - Maximum Number of Events That Can Be Attended
# IDEA: day sweep + min-heap of end days; each day attend the event ending soonest
# time = O(n log n), space = O(n)
import heapq

def maxEvents(events):
    events.sort()
    pq, i, n, res, day = [], 0, len(events), 0, 0

    while i < n or pq:
        day = events[i][0] if not pq else day + 1
        while i < n and events[i][0] <= day:
            heapq.heappush(pq, events[i][1])
            i += 1
        while pq and pq[0] < day:
            heapq.heappop(pq)
        if pq:
            heapq.heappop(pq)
            res += 1

    return res
```

### Template 5: Graph Shortest Path (Dijkstra) — LC 743
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

### Template 7: Greedy String Building with Consecutive Constraint — LC 1405
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

### Template 8: PQ + Cooldown Queue (k-Distance Scheduling) — LC 358
```java
// Java - Rearrange String k Distance Apart (LC 358)
// IDEA: Max-heap picks most frequent available char;
//       cooldown queue locks used chars for k steps.
//
// Flow: PQ → poll → append → cooldown.offer([char, releaseTime])
//       when time == releaseTime → move back to PQ
//
// time = O(n log 26) = O(n), space = O(26) = O(1)

public String rearrangeString(String s, int k) {
    if (k <= 1) return s;

    int[] freq = new int[26];
    for (char c : s.toCharArray()) {
        freq[c - 'a']++;
    }

    // Max-heap ordered by remaining frequency
    PriorityQueue<Integer> pq = new PriorityQueue<>((a, b) -> freq[b] - freq[a]);
    for (int i = 0; i < 26; i++) {
        if (freq[i] > 0) pq.offer(i);
    }

    // Cooldown queue: [charIndex, remainingCount]
    // Size reaches k → front element has cooled for k steps → re-enable
    Queue<int[]> cooldown = new LinkedList<>();
    StringBuilder res = new StringBuilder();

    while (!pq.isEmpty()) {
        int idx = pq.poll();
        res.append((char) ('a' + idx));
        freq[idx]--;

        // Enter cooldown with current remaining count
        cooldown.offer(new int[]{idx, freq[idx]});

        // Release from cooldown after k steps
        if (cooldown.size() == k) {
            int[] ready = cooldown.poll();
            if (ready[1] > 0) {
                pq.offer(ready[0]);  // re-add to heap
            }
        }
    }

    return res.length() == s.length() ? res.toString() : "";
}
```

**Key Observations:**
- The cooldown queue acts as a **fixed-size delay line of length k**. When its size reaches k, the oldest entry has waited exactly k steps and is ready.
- If `pq` is empty but `cooldown` still has entries → impossible to place anything → return `""`.
- **Alternative cooldown approach**: store `[char, releaseTime]` and check `cooldown.peek()[1] == time` instead of checking queue size. Both are equivalent.
- This pattern generalizes: LC 621 (Task Scheduler) uses the same idea but counts idle slots; LC 767 is k=2 special case.

**Comparison: Cooldown Queue vs Skip-and-Swap (Template 7)**
| Aspect | Cooldown Queue (Template 8) | Skip-and-Swap (Template 7) |
|--------|----------------------------|---------------------------|
| Best for | Variable k, large k | Small k (k=2 or k=3) |
| Mechanism | Explicit queue delays re-entry | Look-back window + swap |
| Impossible detection | `pq.isEmpty()` while cooldown non-empty | N/A (stops when no option) |
| Cleaner for | LC 358, LC 621 | LC 1405, LC 767 |

### Template 9: PQ + Sweep Line (Max-Heap with Lazy Deletion) — LC 218

> **When**: you sweep an x-axis / timeline and need *"what is the current maximum among all still-active items?"*. A heap cannot delete an arbitrary element, so you **never delete** — you store `(value, expiry)` and discard stale tops only when they surface.

```java
// java
// LC 218 - The Skyline Problem
// IDEA: sweep x left->right; max-heap of (height, endX) = "currently alive buildings";
//       lazy deletion pops tops whose endX <= x. Emit a point when the max height changes.
// time = O(n log n), space = O(n)
public List<List<Integer>> getSkyline(int[][] buildings) {
    List<int[]> events = new ArrayList<>();
    for (int[] b : buildings) {
        events.add(new int[]{b[0], -b[2], b[1]});   // start: NEGATIVE height + its right edge
        events.add(new int[]{b[1], 0, 0});          // end marker (height 0)
    }
    // same x -> starts first (negative height), taller start first; ends last
    events.sort((a, b) -> a[0] != b[0] ? Integer.compare(a[0], b[0])
                                       : Integer.compare(a[1], b[1]));

    // max-heap of {height, endX}; sentinel = ground level, never expires
    PriorityQueue<int[]> live = new PriorityQueue<>((a, b) -> Integer.compare(b[0], a[0]));
    live.offer(new int[]{0, Integer.MAX_VALUE});

    List<List<Integer>> res = new ArrayList<>();
    int prev = 0;

    for (int[] e : events) {
        int x = e[0];
        // size > 1 protects the sentinel: R can be 2^31 - 1 == Integer.MAX_VALUE
        while (live.size() > 1 && live.peek()[1] <= x) live.poll();   // LAZY DELETE expired tops
        if (e[1] < 0) live.offer(new int[]{-e[1], e[2]}); // a building starts here

        int cur = live.peek()[0];                          // current skyline height
        if (cur != prev) {
            res.add(Arrays.asList(x, cur));
            prev = cur;
        }
    }
    return res;
}
```

```python
# python
# LC 218 - The Skyline Problem
# IDEA: events (x, -h, right) for starts + (x, 0, 0) for ends; min-heap of (-h, right)
#       acts as a max-heap; pop tops whose right <= x (lazy deletion)
# time = O(n log n), space = O(n)
import heapq

def getSkyline(buildings):
    events = [(l, -h, r) for l, r, h in buildings]
    events += list({(r, 0, 0) for _, r, _ in buildings})
    events.sort()                       # start (neg h) sorts before end (0) at same x

    res = [[0, 0]]                      # sentinel, stripped at the end
    live = [(0, float('inf'))]          # (-height, endX); ground sentinel

    for x, neg_h, r in events:
        while live[0][1] <= x:          # lazy deletion of expired buildings
            heapq.heappop(live)
        if neg_h:                       # start event
            heapq.heappush(live, (neg_h, r))
        if res[-1][1] != -live[0][0]:   # skyline height changed -> emit key point
            res.append([x, -live[0][0]])

    return res[1:]
```

**Key Observations:**
- **Lazy deletion is the whole trick**: an expired building buried under a taller live one is harmless — it only matters if it ever becomes the top, and by then the `while` loop removes it.
- The `(0, INF)` sentinel removes all "heap is empty" edge cases and naturally emits the `height = 0` drop points. ⚠️ In Java `INF` is only `Integer.MAX_VALUE`, and LC 218 allows a real `R` to equal it (`0 <= left < right <= 2^31 - 1`), so guard the eviction loop with `live.size() > 1` or the sentinel gets popped and the next `peek()` NPEs. Python's `float('inf')` needs no guard.
- Sort order at equal `x` is where most bugs live: **starts before ends**, and among starts **taller first** (achieved by storing `-h`).
- Same skeleton solves any *"max/min over an active set while sweeping"* question — e.g. LC 1851 (Minimum Interval to Include Each Query: sort queries, push intervals whose start ≤ q, lazily pop those whose end < q).

### Template 10: Greedy with Regret (Heap-Based Exchange Argument) — LC 871

> **When**: you must scan forward taking items, and it is only *later* that you discover you took too many / too much. Take everything optimistically, keep the taken items in a heap, and when you violate the budget **undo the worst decision so far** (`poll()`). Formally an exchange argument — swapping in the best deferred item never hurts.

```java
// java
// LC 871 - Minimum Number of Refueling Stops
// IDEA: drive as far as fuel allows, pushing every passed station's fuel into a MAX-heap
//       ("I could have stopped there"); when stuck, retroactively refuel at the biggest one
// time = O(n log n), space = O(n)
public int minRefuelStops(int target, int startFuel, int[][] stations) {
    PriorityQueue<Integer> pq = new PriorityQueue<>(Collections.reverseOrder());
    int fuel = startFuel, i = 0, stops = 0;

    while (fuel < target) {
        // every station within reach becomes a "regret option"
        while (i < stations.length && stations[i][0] <= fuel) {
            pq.offer(stations[i][1]);
            i++;
        }
        if (pq.isEmpty()) return -1;    // no option left -> unreachable
        fuel += pq.poll();              // retroactively take the biggest tank
        stops++;
    }
    return stops;
}
```

```python
# python
# LC 871 - Minimum Number of Refueling Stops
# IDEA: max-heap of fuel at already-passed stations; refuel from it only when stuck
# time = O(n log n), space = O(n)
import heapq

def minRefuelStops(target, startFuel, stations):
    pq = []                              # max-heap via negation
    fuel, i, stops = startFuel, 0, 0

    while fuel < target:
        while i < len(stations) and stations[i][0] <= fuel:
            heapq.heappush(pq, -stations[i][1])
            i += 1
        if not pq:
            return -1
        fuel -= heapq.heappop(pq)        # -(-max) => add the largest tank
        stops += 1

    return stops
```

**Variation: Course Schedule III (LC 630)** — twist: sort by **deadline**, take every course, and the moment `time > deadline` **drop the longest course taken so far**. Dropping the longest never breaks feasibility of the earlier deadlines.

```java
// java
// LC 630 - Course Schedule III
// IDEA: sort by deadline; greedily take each course, and if the schedule overflows,
//       regret the longest course already taken (max-heap of durations)
// time = O(n log n), space = O(n)
public int scheduleCourse(int[][] courses) {
    Arrays.sort(courses, (a, b) -> Integer.compare(a[1], b[1]));   // by deadline
    PriorityQueue<Integer> pq = new PriorityQueue<>(Collections.reverseOrder());
    int time = 0;

    for (int[] c : courses) {
        time += c[0];
        pq.offer(c[0]);
        if (time > c[1]) time -= pq.poll();   // undo the longest one taken
    }
    return pq.size();
}
```

**Variation: Furthest Building You Can Reach (LC 1642)** — twist: the heap holds the climbs currently **assigned to ladders** (min-heap). Once ladders run out, the *smallest* laddered climb is demoted to bricks — so ladders always end up on the largest climbs.

```java
// java
// LC 1642 - Furthest Building You Can Reach
// IDEA: give every positive climb a ladder; when ladders overflow, pay bricks for the
//       SMALLEST laddered climb (min-heap). Ladders therefore cover the biggest jumps.
// time = O(n log n), space = O(ladders)
public int furthestBuilding(int[] heights, int bricks, int ladders) {
    PriorityQueue<Integer> pq = new PriorityQueue<>();   // min-heap of laddered climbs

    for (int i = 0; i + 1 < heights.length; i++) {
        int d = heights[i + 1] - heights[i];
        if (d <= 0) continue;                            // going down is free
        pq.offer(d);
        if (pq.size() > ladders) {
            bricks -= pq.poll();
            if (bricks < 0) return i;                    // stuck between i and i+1
        }
    }
    return heights.length - 1;
}
```

**Key Observations:**
- Signature of the pattern: *"minimum number of X"* / *"maximum count of Y"* where a decision can be **revised later at no cost**.
- Heap direction encodes the regret: **max-heap** when you want to *undo the worst / take the best deferred option* (LC 871, 630); **min-heap** when you want to *demote the cheapest of a limited premium resource* (LC 1642).
- Contrast with Template 4: interval scheduling never revises a decision; regret-greedy is built entirely on revising them.

### Template 11: Sort by One Criterion + Fixed-Size Heap on the Other — LC 857

> **When**: the objective is a product/combination of two attributes, e.g. `cost = (sum of A over the chosen k) * (max of B over the chosen k)`. **Sort by B** so that iterating fixes the "max B" factor, then keep a size-`k` heap over A to minimise/maximise the sum. This is the two-attribute cousin of Template 1.

```java
// java
// LC 857 - Minimum Cost to Hire K Workers
// IDEA: pay ratio = wage/quality; sort workers by ratio ascending -> the current worker's
//       ratio is the ratio the whole group must be paid. Keep the k SMALLEST qualities
//       with a max-heap; answer = min(sumQuality * ratio).
// time = O(n log n), space = O(n)
public double mincostToHireWorkers(int[] quality, int[] wage, int k) {
    int n = quality.length;
    double[][] workers = new double[n][2];             // [ratio, quality]
    for (int i = 0; i < n; i++) {
        workers[i] = new double[]{(double) wage[i] / quality[i], quality[i]};
    }
    Arrays.sort(workers, (a, b) -> Double.compare(a[0], b[0]));

    PriorityQueue<Double> pq = new PriorityQueue<>(Collections.reverseOrder()); // max-heap
    double sumQ = 0, res = Double.MAX_VALUE;

    for (double[] w : workers) {
        pq.offer(w[1]);
        sumQ += w[1];
        if (pq.size() > k) sumQ -= pq.poll();          // drop the largest quality
        if (pq.size() == k) res = Math.min(res, sumQ * w[0]);
    }
    return res;
}
```

```python
# python
# LC 857 - Minimum Cost to Hire K Workers
# IDEA: sort by wage/quality ratio; max-heap keeps the k smallest qualities seen so far
# time = O(n log n), space = O(n)
import heapq

def mincostToHireWorkers(quality, wage, k):
    workers = sorted((w / q, q) for q, w in zip(quality, wage))
    pq, sum_q, res = [], 0, float('inf')

    for ratio, q in workers:
        heapq.heappush(pq, -q)          # max-heap via negation
        sum_q += q
        if len(pq) > k:
            sum_q += heapq.heappop(pq)  # pop returns -max -> adding it subtracts
        if len(pq) == k:
            res = min(res, sum_q * ratio)

    return res
```

**Variation: Maximum Performance of a Team (LC 1383)** — same shape, opposite directions: sort by **efficiency descending** (the current efficiency is the team minimum), keep a **min-heap of speeds** of size `k` so the sum is maximised.

```java
// java
// LC 1383 - Maximum Performance of a Team
// IDEA: sort by efficiency DESC -> current efficiency = min efficiency of the team;
//       min-heap keeps the k largest speeds. answer = max(sumSpeed * efficiency)
// time = O(n log n), space = O(n)
public int maxPerformance(int n, int[] speed, int[] efficiency, int k) {
    Integer[] idx = new Integer[n];
    for (int i = 0; i < n; i++) idx[i] = i;
    Arrays.sort(idx, (a, b) -> Integer.compare(efficiency[b], efficiency[a]));

    PriorityQueue<Integer> pq = new PriorityQueue<>();   // min-heap of speeds
    long sum = 0, best = 0;

    for (int i : idx) {
        pq.offer(speed[i]);
        sum += speed[i];
        if (pq.size() > k) sum -= pq.poll();             // drop the slowest
        best = Math.max(best, sum * efficiency[i]);      // team of size <= k is allowed
    }
    return (int) (best % 1_000_000_007L);
}
```

**Key Observations:**
- **Sort fixes the multiplicative factor, the heap optimises the additive factor.** Recognising which attribute to sort by is the entire problem.
- Take the modulo only at the very end (LC 1383) — applying it inside the loop breaks the `max` comparison.
- The heap must be the *opposite* direction of what you keep: keep k smallest → max-heap; keep k largest → min-heap (same rule as Template 1).

### Template 12: Min-Heap Best-First Search on a Grid — LC 407

> **When**: a grid where the next cell to expand is not the nearest in steps but the **cheapest/lowest so far** — Dijkstra with the grid as an implicit graph. Template 5 assumes an adjacency list; this variant seeds the heap from a boundary and expands inward.

```java
// java
// LC 407 - Trapping Rain Water II
// IDEA: water level is decided by the lowest wall on the border. Seed a min-heap with the
//       whole border, always expand the lowest cell; an inner neighbour lower than the
//       current level traps (level - height) and then becomes a wall of that level.
// time = O(m*n*log(m*n)), space = O(m*n)
public int trapRainWater(int[][] heightMap) {
    int m = heightMap.length, n = heightMap[0].length;
    if (m < 3 || n < 3) return 0;

    boolean[][] seen = new boolean[m][n];
    // min-heap of {height, row, col}
    PriorityQueue<int[]> pq = new PriorityQueue<>((a, b) -> Integer.compare(a[0], b[0]));

    for (int i = 0; i < m; i++) {
        for (int j = 0; j < n; j++) {
            if (i == 0 || j == 0 || i == m - 1 || j == n - 1) {
                pq.offer(new int[]{heightMap[i][j], i, j});
                seen[i][j] = true;
            }
        }
    }

    int[][] dirs = {{1, 0}, {-1, 0}, {0, 1}, {0, -1}};
    int water = 0;

    while (!pq.isEmpty()) {
        int[] cur = pq.poll();
        int level = cur[0], r = cur[1], c = cur[2];

        for (int[] d : dirs) {
            int nr = r + d[0], nc = c + d[1];
            if (nr < 0 || nc < 0 || nr >= m || nc >= n || seen[nr][nc]) continue;
            seen[nr][nc] = true;
            water += Math.max(0, level - heightMap[nr][nc]);          // trapped water
            pq.offer(new int[]{Math.max(level, heightMap[nr][nc]), nr, nc});
        }
    }
    return water;
}
```

```python
# python
# LC 407 - Trapping Rain Water II
# IDEA: min-heap seeded with the border; pop the lowest wall, water on a lower neighbour
#       = level - height, and the neighbour joins the border at max(level, height)
# time = O(m*n*log(m*n)), space = O(m*n)
import heapq

def trapRainWater(heightMap):
    if not heightMap or len(heightMap) < 3 or len(heightMap[0]) < 3:
        return 0

    m, n = len(heightMap), len(heightMap[0])
    seen = [[False] * n for _ in range(m)]
    pq = []

    for i in range(m):
        for j in range(n):
            if i in (0, m - 1) or j in (0, n - 1):
                heapq.heappush(pq, (heightMap[i][j], i, j))
                seen[i][j] = True

    water = 0
    while pq:
        level, r, c = heapq.heappop(pq)
        for dr, dc in ((1, 0), (-1, 0), (0, 1), (0, -1)):
            nr, nc = r + dr, c + dc
            if 0 <= nr < m and 0 <= nc < n and not seen[nr][nc]:
                seen[nr][nc] = True
                water += max(0, level - heightMap[nr][nc])
                heapq.heappush(pq, (max(level, heightMap[nr][nc]), nr, nc))

    return water
```

**Key Observations:**
- Popping the **globally lowest boundary cell** is what makes the greedy correct: water can only escape over the lowest wall, so that cell's level is final.
- Mark `seen` **at push time**, not at pop time, or cells get queued repeatedly.
- Same skeleton, different priority key:

| LC | Problem | Priority key pushed into the heap |
|----|---------|-----------------------------------|
| 407 | Trapping Rain Water II | `max(current level, neighbour height)` — the effective wall |
| 778 | Swim in Rising Water | `max(current level, neighbour height)` — minimise the *maximum* cell on the path |
| 1631 | Path With Minimum Effort | `max(current effort, abs(height diff))` — minimax edge |
| 1368 | Minimum Cost to Make at Least One Valid Path in a Grid | `cost + (0 if arrow points at neighbour else 1)` — 0/1 weights (a deque also works) |
| 675 | Cut Off Trees for Golf Event | Trees must be cut **shortest first**: sort/heap the targets by height, then run a BFS between consecutive targets |

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

# Get smallest without removing (PEEK) - O(1), see "Peek" section below
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

### Peek: Get TOP Element WITHOUT Popping ⭐⭐⭐⭐⭐

**Key Idea**: Python's `heapq` has **NO `peek()` function** — the heap *is* a plain `list`, and the
heap invariant guarantees the min sits at index `0`. So **`pq[0]` IS the peek**, and it is `O(1)`.

#### **Ways to peek (Python)**

| Way | Time | Verdict |
|-----|------|---------|
| `pq[0]` | O(1) | ✅ **the idiomatic way** |
| `heapq.nsmallest(1, pq)[0]` | O(n) | ❌ scans the whole list, ignores heap structure |
| `min(pq)` | O(n) | ❌ same problem |
| `pq.queue[0]` | O(1) | only for `queue.PriorityQueue` (list + lock wrapper, thread-safe but slower) |

```python
# python
import heapq

pq = []
heapq.heappush(pq, 5)
heapq.heappush(pq, 3)
heapq.heappush(pq, 7)

# ── PEEK (no pop) ─────────────────────────────
top = pq[0]            # time = O(1), space = O(1)  -> 3
print(pq)              # [3, 5, 7]  <- heap UNCHANGED

# ── safe peek on possibly-empty heap ──────────
top = pq[0] if pq else None      # pq[0] raises IndexError when empty

# ── max-heap: push NEGATED keys, negate back on peek ──
max_pq = []
for v in [5, 3, 7]:
    heapq.heappush(max_pq, -v)
largest = -max_pq[0]   # 7   (peek, NOT pop)
```

#### **⚠️ Gotchas**

```python
# python
pq = [1, 3, 9, 7, 5]   # a VALID min-heap

# ✅ ONLY index 0 is meaningful
pq[0]     # 1  -> guaranteed smallest

# ❌ a heap is only PARTIALLY ordered - these mean NOTHING
pq[1]     # 3  -> NOT necessarily the 2nd smallest
pq[-1]    # 5  -> NOT the largest
sorted(pq)[1]   # if you truly need the 2nd smallest, this is O(n log n)

# ❌ IndexError on empty heap -> always guard
empty = []
# empty[0]                     # IndexError: list index out of range
while empty and empty[0] < 10: # ✅ short-circuit: `empty and ...` MUST come first
    heapq.heappop(empty)
```

#### **Classic use: lazy deletion (peek → discard stale tops)**

The most common reason to peek is **lazy deletion** — you never remove a stale entry from the middle
of the heap (heapq can't), you just pop it off the top once it surfaces.

```python
# python
# LC 3092 - Most Frequent IDs  (also LC 218 Skyline, LC 1834 Single-Threaded CPU)
# IDEA: max-heap of (-count, id); an entry is STALE if its stored count != the live count.
#       Peek at pq[0], drop stale tops, then pq[0] is the true answer.
# time = O(n log n), space = O(n)
while pq and -pq[0][0] != c_map[pq[0][1]]:   # peek, compare, discard
    heapq.heappop(pq)

ans = -pq[0][0] if pq else 0                 # now the top is VALID
```

#### **Ops that avoid a separate peek**

When you are going to *replace* the top anyway, these do it in one sift instead of two:

```python
# python
heapq.heapreplace(pq, item)   # pop THEN push -> returns old top. Heap must be non-empty.
heapq.heappushpop(pq, item)   # push THEN pop -> cheaper when item <= current top

# typical "keep k largest" loop - peek to compare, replace in one shot
for num in nums:
    if len(pq) < k:
        heapq.heappush(pq, num)
    elif num > pq[0]:                 # peek
        heapq.heapreplace(pq, num)    # 1 sift instead of heappop + heappush
```

#### **Java equivalent**

```java
// java
PriorityQueue<Integer> pq = new PriorityQueue<>();

Integer top = pq.peek();    // time = O(1), returns NULL on empty (no exception)
Integer top2 = pq.element();// time = O(1), THROWS NoSuchElementException on empty

// NOTE: Java's peek() is a real method; Python has no peek() -> use pq[0]
// NOTE: iterating a Java PQ (for/toString) does NOT give sorted order - same
//       partial-order caveat as python's pq[1], pq[-1]
```

| | Python `heapq` | Java `PriorityQueue` |
|---|---|---|
| Peek | `pq[0]` | `pq.peek()` |
| Empty behaviour | `IndexError` | `peek()` → `null`, `element()` → throws |
| Empty check | `if pq:` | `pq.isEmpty()` |
| Max-heap peek | `-pq[0]` (negated push) | `pq.peek()` with `Collections.reverseOrder()` |

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
| Find K Closest Elements | 658 | Max-heap by `(abs(a - x), a)` of size k, or binary search on window | Medium |
| Find the Kth Largest Integer in the Array | 1985 | Min-heap of size k with numeric-string comparator | Medium |

#### **Merge K Sorted Problems**
| Problem | LC # | Key Technique | Difficulty |
|---------|------|---------------|------------|
| Merge k Sorted Lists | 23 | K-way merge | Hard |
| Merge Sorted Array | 88 | Two pointers | Easy |
| Smallest Range Covering K Lists | 632 | Multi-pointer + heap | Hard |
| Find K Pairs with Smallest Sums | 373 | Heap with pairs | Medium |
| Super Ugly Number | 313 | Multiple pointers | Medium |
| Design Twitter | 355 | K-way merge over followees' feeds (max-heap by timestamp) | Medium |

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
| Minimum Number of Refueling Stops | 871 | Greedy with regret (max-heap) — Template 10 | Hard |
| Course Schedule III | 630 | Sort by deadline + regret max-heap — Template 10 | Hard |
| Furthest Building You Can Reach | 1642 | Regret min-heap of laddered climbs — Template 10 | Medium |
| Process Tasks Using Servers | 1882 | Two heaps: free servers + busy servers keyed by release time | Medium |
| The Skyline Problem | 218 | Sweep line + max-heap with lazy deletion — Template 9 | Hard |

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
| Trapping Rain Water II | 407 | Grid best-first from the border — Template 12 | Hard |
| Minimum Cost to Make at Least One Valid Path in a Grid | 1368 | 0/1 Dijkstra on a grid — Template 12 | Hard |
| Cut Off Trees for Golf Event | 675 | Cut trees shortest-first, BFS between consecutive targets | Hard |

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

```text
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

### 2-1) Kth Largest Element in an Array (LC 215) — LC 215
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

### 2-2) Top K Frequent Elements (LC 347) — LC 347
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

### 2-3) Merge K Sorted Lists (LC 23) — LC 23
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

### 2-4) Find Median from Data Stream (LC 295) — LC 295
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

### 2-5) Meeting Rooms II (LC 253) — LC 253
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

### 2-6) Kth Largest Element in a Stream (LC 703) — LC 703
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

### 2-7) K Closest Points to Origin (LC 973) — LC 973
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

### 2-8) Kth Smallest Element in a Sorted Matrix (LC 378) — LC 378
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

### 2-9) Task Scheduler (LC 621) — LC 621
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

### 2-10) Reorganize String (LC 767) — LC 767
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

### 2-10-1) Rearrange String k Distance Apart (LC 358) — LC 358 — PQ + Cooldown Queue
```java
// Java
// LC 358 - Rearrange string so same chars are at least k distance apart
// IDEA: Max-heap + cooldown queue (k-slot delay line)
// Time: O(N log 26) = O(N), Space: O(26) = O(1)

public String rearrangeString(String s, int k) {
    if (k <= 1) return s;

    int[] counts = new int[26];
    for (char c : s.toCharArray()) {
        counts[c - 'a']++;
    }

    // Max-heap: store char indices, ordered by remaining count
    PriorityQueue<Integer> maxHeap = new PriorityQueue<>((a, b) -> counts[b] - counts[a]);
    for (int i = 0; i < 26; i++) {
        if (counts[i] > 0) maxHeap.add(i);
    }

    // Cooldown queue: [charIndex, remainingCount]
    // When queue size reaches k, front element is ready to re-enter heap
    Queue<int[]> cooldown = new LinkedList<>();
    StringBuilder result = new StringBuilder();

    while (!maxHeap.isEmpty()) {
        int charIdx = maxHeap.poll();
        result.append((char) ('a' + charIdx));
        counts[charIdx]--;

        // Lock this char for k steps
        cooldown.offer(new int[]{charIdx, counts[charIdx]});

        // Release oldest char after k steps
        if (cooldown.size() == k) {
            int[] ready = cooldown.poll();
            if (ready[1] > 0) {
                maxHeap.add(ready[0]);
            }
        }
    }

    // If we couldn't place all chars, return ""
    return result.length() == s.length() ? result.toString() : "";
}
```

**Walkthrough** — `s = "aabbcc", k = 3`:
```text
t=0: pick 'a'(2), cooldown=[(a,1)]          → "a"
t=1: pick 'b'(2), cooldown=[(a,1),(b,1)]    → "ab"
t=2: pick 'c'(2), cooldown size=3=k → release 'a'(1) back to PQ
     cooldown=[(b,1),(c,1)]                  → "abc"
t=3: pick 'a'(1), cooldown size=3=k → release 'b'(1) back to PQ
     cooldown=[(c,1),(a,0)]                  → "abca"
t=4: pick 'b'(1), cooldown size=3=k → release 'c'(1) back to PQ
     cooldown=[(a,0),(b,0)]                  → "abcab"
t=5: pick 'c'(1), cooldown size=3=k → release 'a'(0) → don't re-add
                                             → "abcabc" ✓
```

### 2-10-2) Longest Happy String (LC 1405) — LC 1405 — Max-Heap + Skip-and-Swap

Given counts `a`, `b`, `c`, build the **longest** string over `{'a','b','c'}` containing no `"aaa"`, `"bbb"` or `"ccc"`. Leftover characters may be discarded.

#### 1) Core Idea
- **Spend the most abundant character first.** A max-heap on remaining count gives it in `O(log k)`. Hoarding the majority character is what kills you — every copy needs a separator, so a large surplus left to the end can no longer be placed.
- **The only blocker is the run-length cap.** If the last two emitted characters already equal the heap's top, emitting it again would make three in a row.
- **When blocked, swap in the runner-up and put the top back untouched.** The top is *not* consumed in this branch — breaking the run makes it legal again on the very next turn.
- **Emit exactly one character per iteration.** Don't try to append `"aa"` in one go: the loop produces `"aa"` naturally across two iterations, and one-at-a-time keeps the count bookkeeping trivial (no special case for "only 1 copy left", no second-place arithmetic).
- **Nothing is mandatory.** Unlike LC 767 this problem asks for the *longest* string, not a full rearrangement, so when the top is blocked and the heap is empty you simply `break` and return what you have.

> `heapq` is a min-heap, so push `-count`. Incrementing a negative count by `1` **shrinks its magnitude** — that is what "consume one copy" looks like.

#### 2) Pattern — Max-heap + two-case loop (skip-and-swap)
Pop the top each turn, then take exactly one of two branches:

| | Condition | Action |
|---|---|---|
| **Case 1 — blocked** | `len(res) >= 2 and res[-1] == res[-2] == top` | If heap empty → `break`. Else pop the runner-up, emit **it**, push it back if any left, and push **top back unconsumed**. |
| **Case 2 — safe** | otherwise | Emit `top`, push it back if any left. |

```python
# python
# LC 1405 - Longest Happy String
# IDEA: max-heap by remaining count; skip-and-swap when the top would make 3 in a row
# time = O(n log k), k = 3 distinct chars -> O(n); space = O(k) = O(1)
import heapq

class Solution(object):
    def longestDiverseString(self, a, b, c):
        # max-heap via negated counts: most-remaining pops first
        pq = []
        if a > 0: heapq.heappush(pq, (-a, 'a'))
        if b > 0: heapq.heappush(pq, (-b, 'b'))
        if c > 0: heapq.heappush(pq, (-c, 'c'))

        res = []
        while pq:
            cnt1, ch1 = heapq.heappop(pq)

            # Case 1) using ch1 would create 3 in a row -> use the runner-up
            if len(res) >= 2 and res[-1] == ch1 and res[-2] == ch1:
                if not pq:
                    break              # no alternative left -> stop, keep what we have

                cnt2, ch2 = heapq.heappop(pq)
                res.append(ch2)
                cnt2 += 1              # negative count: += 1 consumes one copy
                if cnt2 < 0:
                    heapq.heappush(pq, (cnt2, ch2))

                heapq.heappush(pq, (cnt1, ch1))   # ch1 was NOT used -> back unchanged

            # Case 2) safe to use the most frequent character
            else:
                res.append(ch1)
                cnt1 += 1
                if cnt1 < 0:
                    heapq.heappush(pq, (cnt1, ch1))

        return "".join(res)
```

For the Java version see **Template 7** above — the same two-case loop with an explicit `ValCnt` comparator.

**Visual Trace** — `a = 1, b = 1, c = 7` → `"ccaccbcc"`:
```text
step  heap (count,char)        branch                          res
0     c:7  a:1  b:1            safe, use 'c'                   c
1     c:6  a:1  b:1            safe, use 'c'                   cc
2     c:5  a:1  b:1            'c' would make 3 -> use 'a'     cca
3     c:5  b:1                 safe, use 'c'                   ccac
4     c:4  b:1                 safe, use 'c'                   ccacc
5     c:3  b:1                 'c' would make 3 -> use 'b'     ccaccb
6     c:3                      safe, use 'c'                   ccaccbc
7     c:2                      safe, use 'c'                   ccaccbcc
8     c:1                      blocked, heap empty -> STOP     ccaccbcc  ✓
```
Note step 8: one `'c'` is **discarded**. That is correct — the answer is the longest *happy* string, not all 9 characters.

#### 3) Compare with LC 767 (Reorganize String)
Same family — max-heap greedy on remaining frequency — but the constraint strength changes the loop shape:

| Aspect | **LC 767** Reorganize String | **LC 1405** Longest Happy String |
|--------|------------------------------|----------------------------------|
| Constraint | No two **adjacent** equal → max run **1** | No `"xxx"` → max run **2** |
| Must consume everything? | **Yes** — output uses all `n` chars or is `""` | **No** — leftovers are dropped |
| Feasibility check | Pre-checkable: `maxFreq > (n+1)/2` → return `""` | None needed; the greedy just stops early |
| Idiomatic loop | Pop **two**, emit **both** per round | Pop **one**, emit **one**; skip-and-swap on violation |
| Why that shape | Run ≤ 1 forces strict alternation, so placing a pair per round is always safe | Run ≤ 2 *permits* a double, and you want it to maximise length — pairwise placement would over-constrain and lose characters |
| Termination | Heap empty (≤ 1 element left → append it) | Heap empty **or** top blocked with no alternative → `break` |
| Failure output | `""` when impossible | Best-effort prefix (never fails) |

**The key insight:** LC 1405's skip-and-swap loop is the more general of the two. Change the look-back window from two characters to one and it solves LC 767 unchanged (see Template 7's variant note) — you only lose the `""`-on-impossible signal, which you recover by checking `len(result) == n` at the end. The reverse does **not** hold: LC 767's pop-two-emit-both loop cannot produce `"aa"`, so it can never build the optimal answer for LC 1405.

Both are the "greedy + constraint" pattern; the run-length cap `k` decides the mechanism — see the **Cooldown Queue vs Skip-and-Swap** table under Template 8 for when to reach for LC 358's explicit delay queue instead.

#### 4) Similar LC Problems
| Problem | LC # | Relation to LC 1405 | Key Difference |
|---------|------|---------------------|----------------|
| Reorganize String | 767 | Same max-heap greedy, run cap 1 | Must use every char or return `""` |
| String Without AAA or BBB | 984 | Identical run-≤-2 rule | Only 2 letters, so plain comparison beats a heap |
| Rearrange String k Distance Apart | 358 | Generalises the gap to arbitrary `k` | Cooldown queue instead of skip-and-swap |
| Task Scheduler | 621 | Same "spread out the most frequent" greedy | Counts idle slots; answer is a length, not a string |
| Distant Barcodes | 1054 | LC 767 over integers | Same run cap 1, different alphabet |
| Construct String With Repeat Limit | 2182 | Run cap is a parameter `repeatLimit` | Greedy picks the **lexicographically largest** char, not the most frequent |

**Common signal for this pattern**: *"build a sequence where no value may repeat more than k times in a row"* → max-heap on remaining count, emit one per turn, and when the top is blocked substitute the runner-up while returning the top to the heap unconsumed.

---

### 2-11) Sliding Window Median (LC 480) — LC 480
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

### 2-12) Ugly Number II (LC 264) — LC 264
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

### 2-13) Network Delay Time (LC 743 - Dijkstra) — LC 743
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

### 2-14) Sort Characters By Frequency (LC 451) — LC 451
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

### 2-15) Last Stone Weight (LC 1046) — LC 1046
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

### 2-16) Find K Pairs with Smallest Sums (LC 373) — LC 373 — K-Way Merge on a Sorted Grid

Given two **sorted** arrays `nums1`, `nums2`, return the `k` pairs `(u, v)` with the smallest sums `u + v`.

#### 1) Core Idea
- Think of all possible pairs as a **conceptual sorted matrix** `M[i][j] = nums1[i] + nums2[j]`.
  Because both arrays are sorted, each **row** (fix `i`, increase `j`) and each **column** (fix `j`, increase `i`) is non-decreasing.
- Brute force builds all `m*n` pairs → too slow. Instead, use a **min-heap to explore only the frontier** of candidates, popping the global minimum `k` times.
- **Key pruning**: the smallest pairs must involve small indices, so we only ever seed the first `min(k, len(nums1))` rows — `nums1[k] + nums2[0]` can never be in the top `k`.
- This is the exact same skeleton as **LC 378 (Kth Smallest in Sorted Matrix)** — the pairs form a virtual sorted grid.

#### 2) Pattern — Seed rows, then walk right (K-Way Merge)
- **Seed**: push `(nums1[i] + nums2[0], i, 0)` for `i` in `[0, min(k, len(nums1)))`. Each entry is the head of a "row/list" starting at column `0`.
- **Expand**: pop the smallest `(sum, i, j)`, record `[nums1[i], nums2[j]]`, then push its **right neighbor** `(nums1[i] + nums2[j+1], i, j+1)` if it exists.
- Only pushing `(i, j+1)` (not `(i+1, j)`) is enough because every column-0 head was already seeded — this keeps the heap size bounded by `min(k, len(nums1))` and avoids needing a `visited` set.
- **Why it works**: the heap always holds exactly one "next candidate" per active row, so each pop yields the next-globally-smallest sum, just like merging `k` sorted lists.

```python
# python
# LC 373 - Find K Pairs with Smallest Sums
# IDEA: K-way merge — seed first min(k, m) rows, pop min, push right neighbor
# time = O(k * log(min(k, m))), space = O(min(k, m))
import heapq
class Solution:
    def kSmallestPairs(self, nums1, nums2, k):
        if not nums1 or not nums2 or k == 0:
            return []

        # PQ holds: (pair_sum, idx_1, idx_2)
        heap = []
        # seed: pair each of first min(k, m) nums1 values with nums2[0]
        for i in range(min(len(nums1), k)):
            heapq.heappush(heap, (nums1[i] + nums2[0], i, 0))

        res = []
        while heap and len(res) < k:
            _, i, j = heapq.heappop(heap)
            res.append([nums1[i], nums2[j]])
            # only advance j (right neighbor); column-0 heads already seeded
            if j + 1 < len(nums2):
                heapq.heappush(heap, (nums1[i] + nums2[j + 1], i, j + 1))

        return res
```

```java
// java
// LC 373 - Find K Pairs with Smallest Sums
// IDEA: K-way merge — seed first min(k, m) rows, pop min, push right neighbor
// time = O(k * log(min(k, m))), space = O(min(k, m))
public List<List<Integer>> kSmallestPairs(int[] nums1, int[] nums2, int k) {
    List<List<Integer>> res = new ArrayList<>();
    if (nums1.length == 0 || nums2.length == 0 || k == 0) return res;

    // min-heap: [sum, i, j]
    PriorityQueue<int[]> pq = new PriorityQueue<>((a, b) -> a[0] - b[0]);
    for (int i = 0; i < Math.min(nums1.length, k); i++) {
        pq.offer(new int[]{nums1[i] + nums2[0], i, 0});
    }

    while (!pq.isEmpty() && res.size() < k) {
        int[] cur = pq.poll();
        int i = cur[1], j = cur[2];
        res.add(Arrays.asList(nums1[i], nums2[j]));
        if (j + 1 < nums2.length) {
            pq.offer(new int[]{nums1[i] + nums2[j + 1], i, j + 1});
        }
    }
    return res;
}
```

**Alternative seeding** (see `find-k-pairs-with-smallest-sums.py` V2): seed only `(0, 0)` and push **both** `(i+1, j)` and `(i, j+1)` on each pop, guarding with a `visited` set. Equivalent result; the row-seeding version above avoids the `visited` set and is the more common template.

#### 3) Similar LC Problems
| Problem | LC # | Relation to LC 373 | Key Difference |
|---------|------|--------------------|----------------|
| Kth Smallest Element in a Sorted Matrix | 378 | Same virtual sorted-grid + heap frontier | Grid is given explicitly; return the k-th, not first k |
| Merge k Sorted Lists | 23 | Same k-way-merge heap skeleton | Merge real lists via `(val, list_idx, node)` |
| Ugly Number II / Super Ugly Number | 264 / 313 | Generate values in sorted order via heap frontier | Next candidates = current × prime factors |
| Kth Smallest Prime Fraction | 786 | Sorted pairs `(num[i], num[j])` explored by heap | Order by fraction value instead of sum |
| Smallest Range Covering K Lists | 632 | Multi-pointer + heap over sorted lists | Track a covering range, not smallest sums |
| Find K-th Smallest Pair Distance | 719 | "K-th smallest over all pairs" | Uses binary search on answer (heap too slow) |

**Common signal for this pattern**: *"k smallest / k-th smallest combination of two (or more) sorted sequences"* → seed a heap with the minimal frontier, pop `k` times, push each popped element's successor(s).

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
| **Sweep Line + Lazy Delete** | LC 218, 1851 | Store `(value, expiry)`; discard stale tops only when they surface |
| **Greedy with Regret** | LC 871, 630, 1642 | Take everything, then `poll()` the worst past decision when the budget breaks |
| **Sort + Fixed-Size Heap** | LC 857, 1383 | Sort fixes the `max/min` factor, heap optimises the `sum` factor |
| **Grid Best-First** | LC 407, 778, 1631, 1368 | Dijkstra on an implicit grid; priority = minimax / accumulated cost |
| **Day-Sweep Scheduling** | LC 1353, 1882 | Min-heap of end/release times; act on the earliest-expiring item each tick |

## LC Examples

### 2-17) Kth Largest Element in a Stream (LC 703) — LC 703 — Min-Heap of Size K
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

### 2-18) Top K Frequent Elements (LC 347) — LC 347 — Min-Heap with Frequency
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

### 2-19) Merge K Sorted Lists (LC 23) — LC 23 — Min-Heap
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
