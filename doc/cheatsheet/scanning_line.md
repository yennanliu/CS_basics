# Scanning Line (Line Sweep) Algorithm

> **Scope** — Line sweep — turn each interval into `+1` / `-1` events, sort by coordinate, sweep once.
> **See also**: [intervals.md](./intervals.md) — sort-and-merge without events; [difference_array.md](./difference_array.md) — the array-indexed version of the same trick; [heap.md](./heap.md) — sweeps that need a max over live intervals.

## LeetCode Problem Lists

- [Sweep Line](https://leetcode.com/problem-list/sweep-line/)
- [Sorting](https://leetcode.com/problem-list/sorting/)
- [Prefix Sum](https://leetcode.com/problem-list/prefix-sum/)

## Overview
**Scanning Line** (also known as Line Sweep or Sweep Line) is an algorithmic paradigm that processes geometric objects by imagining a vertical line sweeping across the plane from left to right, processing events as they occur.

Key: transform `change` to `event`, so we can handle the `changed state` via program, instead of dealing with `continouous info`.


<p align="center"><img src="../pic/scanning_line.png"></p>

 
### Key Properties
- **Time Complexity**: O(n log n) for sorting + O(n) for processing
- **Space Complexity**: O(n) for storing events
- **Core Idea**: Convert interval problems into event-based processing
- **When to Use**: Interval overlaps, skyline problems, calendar conflicts, geometric intersections

### Algorithm Principle
1. Convert intervals into events (start/end points)
2. Sort events by position (and type if at same position)
3. Process events in order while maintaining state
4. Track maximum/minimum or other statistics during sweep

### References
- [NTNU Algorithm Notes](https://web.ntnu.edu.tw/~algo/Point2.html)
- [Line Sweep Tutorial](https://hackmd.io/@meyr543/SkrRZCwfj)
- [Computational Geometry](https://www.cs.princeton.edu/~rs/AlgsDS07/)

## Problem Categories

### **Pattern 1: Interval Overlap**
- **Description**: Finding maximum overlapping intervals at any point
- **Examples**: LC 253, 1094, 2021, 2406, 2848
- **Pattern**: Track active intervals using counter

### **Pattern 2: Skyline Problems**
- **Description**: Computing visible outline from overlapping rectangles
- **Examples**: LC 218, 850, 391
- **Pattern**: Process building start/end with heights

### **Pattern 3: Calendar Booking**
- **Description**: Managing calendar events and conflicts
- **Examples**: LC 729, 731, 732, 1851
- **Pattern**: Track booking counts at each time point

### **Pattern 4: Employee Free Time / Interval Intersection**
- **Description**: Finding common free time (gaps) or common busy time (intersections) across schedules
- **Examples**: LC 759, 986, 1229
- **Pattern**: One merged event stream + a **predicate on the coverage counter** —
  `count == 0` → free time (LC 759), `count == 2` → intersection of 2 lists (LC 986, see 2-7)

### **Pattern 5: Range Updates**
- **Description**: Applying updates to ranges efficiently
- **Examples**: LC 370, 1109, 1893, 2251
- **Pattern**: Difference array with sweep line

### **Pattern 6: Geometric Intersection**
- **Description**: Finding intersections of geometric objects
- **Examples**: LC 836, 223, 391, 850
- **Pattern**: Sort by x-coordinate, track y-intervals

### **Pattern 7: Prefix Sum + Longest Positive-Sum Subarray**
- **Description**: Find the longest subarray whose element sum > 0 after transforming values to +1/−1
- **Examples**: LC 1124, 525, 560, 974
- **Pattern**: Prefix sum with HashMap storing first-occurrence of each sum; if `prefix > 0` take full length; else look up `prefix - 1` in map

### **Pattern 8: Time Sweep + Deadline Heap (greedy scheduling)** ⭐⭐⭐⭐
- **Description**: Sweep forward in time; each time slot can serve **one** item, and each item is only valid inside its window `[start, end]`
- **Examples**: LC 1353, 621, 1834, 630, 767
- **Pattern**: Sort by window **start** (items enter in time order) + min heap of window **end** (serve the most urgent) + lazy-delete expired tops
- **Key difference from Pattern 1**: Pattern 1 *counts* how many intervals are concurrent (`+1/−1` counter). Pattern 8 *picks a subset* — the sweep consumes one slot per tick, so it needs a heap to decide **which** interval to spend the slot on
- **Signature**: *"one item per unit of time"* + *"each item has a deadline"* → earliest-deadline-first is optimal

### **Pattern 9: Weighted Interval Scheduling (sweep + retired-job heap)** ⭐⭐⭐⭐⭐
- **Description**: Pick a set of **non-overlapping** intervals maximising total weight; each chosen interval occupies its **whole** range
- **Examples**: LC 1235, 452, 1751
- **Pattern**: Sort by **start**; min heap keyed by **end** holding *taken* chains; pop everything with `end <= start` into a monotone running `best`; push `(end, best + weight)`
- **Key difference from Pattern 8**: Pattern 8 spends *one slot* inside the interval; Pattern 9 **blocks the whole interval**, so compatibility (not urgency) drives the choice — and the weights kill pure greedy
- **Unweighted degenerate case**: all weights equal → the heap collapses to one `end` variable → sort-by-end greedy (LC 452)

## Templates & Algorithms

### Template Comparison Table
| Template Type | Use Case | Event Types | Complexity | When to Use |
|---------------|----------|-------------|------------|-------------|
| **Basic Sweep** | Count overlaps | Start/End | O(n log n) | Meeting rooms, intervals |
| **Weighted Sweep** | Sum of overlaps | Start/End + value | O(n log n) | Brightness, bandwidth |
| **Skyline** | Height tracking | Start/End + height | O(n log n) | Building outline |
| **Difference Array** | Range updates | Update points | O(n) | Batch updates |
| **Interval Merge** | Combine intervals | Start/End | O(n log n) | Free time, union |
| **2D Sweep** | Rectangle area | X and Y events | O(n² log n) | Area calculation |
| **Time Sweep + Deadline Heap** | Pick max items, 1 per slot | Start (enter) + End (deadline) | O(n log n) | Max events attended, task scheduling |
| **Sweep + Retired-Job Heap** | Max weight of non-overlapping set | Start (enter) + End (retire) + weight | O(n log n) | Weighted interval scheduling (LC 1235) |
| **Gap Sweep** | Largest hole between events | Sorted coordinates only | O(n log n) | Max piece after cuts (LC 1465) |
| **Index Sweep + Ordered Set** | Nearest value in a window | Index enter/leave | O(n log k) | Near-duplicate detection (LC 220) |
| **Intersection Sweep** | Emit ranges where coverage == k | Start/End (+ list id) | O(n log n) | Interval list intersections (LC 986) |

### Template 1: Basic Interval Overlap — LC 253
```python
# Python - Count maximum overlapping intervals
def maxOverlap(intervals):
    events = []
    
    # Create events for each interval
    for start, end in intervals:
        events.append((start, 1))   # Start event
        events.append((end, -1))     # End event
    
    # Sort events (by time, then by type)
    events.sort(key=lambda x: (x[0], -x[1]))  # Process start before end at same time
    
    # Sweep through events
    max_overlap = 0
    current_overlap = 0
    
    for time, delta in events:
        current_overlap += delta
        max_overlap = max(max_overlap, current_overlap)
    
    return max_overlap

# With position tracking
def maxOverlapPosition(intervals):
    events = []
    for start, end in intervals:
        events.append((start, 1))
        events.append((end, -1))
    
    events.sort(key=lambda x: (x[0], -x[1]))
    
    max_overlap = 0
    max_position = 0
    current_overlap = 0
    
    for time, delta in events:
        current_overlap += delta
        if current_overlap > max_overlap:
            max_overlap = current_overlap
            max_position = time
    
    return max_overlap, max_position
```

```java
// Java - Maximum interval overlap
public int maxOverlap(int[][] intervals) {
    List<int[]> events = new ArrayList<>();
    
    // Create events
    for (int[] interval : intervals) {
        events.add(new int[]{interval[0], 1});   // Start
        events.add(new int[]{interval[1], -1});  // End
    }
    
    // Sort events
    Collections.sort(events, (a, b) -> {
        if (a[0] != b[0]) return a[0] - b[0];
        return b[1] - a[1];  // Start before end
    });
    
    // Sweep
    int maxOverlap = 0;
    int currentOverlap = 0;
    
    for (int[] event : events) {
        currentOverlap += event[1];
        maxOverlap = Math.max(maxOverlap, currentOverlap);
    }
    
    return maxOverlap;
}
```

### Template 2: Weighted Interval Overlap — LC 2021
```python
# Python - Sum of overlapping values (e.g., brightness)
def maxWeightedOverlap(weighted_intervals):
    events = []
    
    # weighted_intervals: [(start, end, weight)]
    for start, end, weight in weighted_intervals:
        events.append((start, weight))   # Add weight
        events.append((end, -weight))    # Remove weight
    
    events.sort()
    
    max_weight = 0
    current_weight = 0
    result_position = 0
    
    for position, delta in events:
        current_weight += delta
        if current_weight > max_weight:
            max_weight = current_weight
            result_position = position
    
    return max_weight, result_position

# Track all positions with their weights
def allWeightedPositions(weighted_intervals):
    from collections import defaultdict
    events = defaultdict(int)
    
    for start, end, weight in weighted_intervals:
        events[start] += weight
        events[end] -= weight
    
    sorted_positions = sorted(events.keys())
    positions_weights = {}
    current_weight = 0
    
    for pos in sorted_positions:
        current_weight += events[pos]
        positions_weights[pos] = current_weight
    
    return positions_weights
```

```java
// Java - Weighted intervals
public int maxWeightedOverlap(int[][] weightedIntervals) {
    // weightedIntervals: [start, end, weight]
    TreeMap<Integer, Integer> events = new TreeMap<>();
    
    for (int[] interval : weightedIntervals) {
        events.put(interval[0], 
                  events.getOrDefault(interval[0], 0) + interval[2]);
        events.put(interval[1], 
                  events.getOrDefault(interval[1], 0) - interval[2]);
    }
    
    int maxWeight = 0;
    int currentWeight = 0;
    
    for (int delta : events.values()) {
        currentWeight += delta;
        maxWeight = Math.max(maxWeight, currentWeight);
    }
    
    return maxWeight;
}
```

### Template 3: Skyline Problem — LC 218
```python
# Python - Building skyline
def getSkyline(buildings):
    events = []
    
    # buildings: [[left, right, height]]
    for left, right, height in buildings:
        events.append((left, -height))  # Start (negative for max heap)
        events.append((right, height))  # End
    
    events.sort(key=lambda x: (x[0], x[1]))
    
    result = []
    heights = [0]  # Ground level
    
    import heapq
    for x, h in events:
        if h < 0:  # Building start
            heapq.heappush(heights, h)
        else:  # Building end
            heights.remove(-h)
            heapq.heapify(heights)
        
        # Check if max height changed
        max_h = -heights[0]
        if not result or result[-1][1] != max_h:
            result.append([x, max_h])
    
    return result
```

```java
// Java - Skyline
public List<List<Integer>> getSkyline(int[][] buildings) {
    List<int[]> events = new ArrayList<>();
    
    for (int[] b : buildings) {
        events.add(new int[]{b[0], -b[2]});  // Start
        events.add(new int[]{b[1], b[2]});   // End
    }
    
    Collections.sort(events, (a, b) -> {
        if (a[0] != b[0]) return a[0] - b[0];
        return a[1] - b[1];
    });
    
    List<List<Integer>> result = new ArrayList<>();
    TreeMap<Integer, Integer> heights = new TreeMap<>();
    heights.put(0, 1);  // Ground
    
    for (int[] event : events) {
        int x = event[0], h = event[1];
        
        if (h < 0) {  // Start
            heights.put(-h, heights.getOrDefault(-h, 0) + 1);
        } else {  // End
            if (heights.get(h) == 1) {
                heights.remove(h);
            } else {
                heights.put(h, heights.get(h) - 1);
            }
        }
        
        int maxH = heights.lastKey();
        if (result.isEmpty() || 
            result.get(result.size() - 1).get(1) != maxH) {
            result.add(Arrays.asList(x, maxH));
        }
    }
    
    return result;
}
```

### Template 4: Calendar Booking — LC 731
```python
# Python - Calendar with multiple bookings
class MyCalendarTwo:
    def __init__(self):
        self.events = []  # List of (time, delta)
    
    def book(self, start, end):
        # Temporarily add new booking
        self.events.append((start, 1))
        self.events.append((end, -1))
        self.events.sort()
        
        # Check if triple booking
        booked = 0
        for time, delta in self.events:
            booked += delta
            if booked >= 3:
                # Remove the temporary booking
                self.events.remove((start, 1))
                self.events.remove((end, -1))
                return False
        
        return True
```

```java
// Java - Calendar booking
class MyCalendarTwo {
    List<int[]> events;
    
    public MyCalendarTwo() {
        events = new ArrayList<>();
    }
    
    public boolean book(int start, int end) {
        events.add(new int[]{start, 1});
        events.add(new int[]{end, -1});
        
        Collections.sort(events, (a, b) -> {
            if (a[0] != b[0]) return a[0] - b[0];
            return a[1] - b[1];
        });
        
        int booked = 0;
        for (int[] event : events) {
            booked += event[1];
            if (booked >= 3) {
                events.remove(new int[]{start, 1});
                events.remove(new int[]{end, -1});
                return false;
            }
        }
        
        return true;
    }
}
```

### Template 5: Difference Array Pattern — LC 370
```python
# Python - Range addition using sweep line
def rangeAddition(length, updates):
    # updates: [[start, end, inc]]
    diff = [0] * (length + 1)
    
    for start, end, inc in updates:
        diff[start] += inc
        diff[end + 1] -= inc
    
    # Sweep to get final values
    result = [0] * length
    current = 0
    for i in range(length):
        current += diff[i]
        result[i] = current
    
    return result

# 2D range addition
def rangeAddition2D(m, n, updates):
    diff = [[0] * (n + 1) for _ in range(m + 1)]
    
    for r1, c1, r2, c2, inc in updates:
        diff[r1][c1] += inc
        diff[r1][c2 + 1] -= inc
        diff[r2 + 1][c1] -= inc
        diff[r2 + 1][c2 + 1] += inc
    
    # 2D prefix sum
    result = [[0] * n for _ in range(m)]
    for i in range(m):
        for j in range(n):
            result[i][j] = diff[i][j]
            if i > 0:
                result[i][j] += result[i-1][j]
            if j > 0:
                result[i][j] += result[i][j-1]
            if i > 0 and j > 0:
                result[i][j] -= result[i-1][j-1]
    
    return result
```

### Template 6: Interval Merge with Sweep — LC 56
```python
# Python - Merge overlapping intervals using sweep
def mergeIntervals(intervals):
    if not intervals:
        return []
    
    events = []
    for start, end in intervals:
        events.append((start, 1))
        events.append((end, -1))
    
    events.sort(key=lambda x: (x[0], -x[1]))
    
    merged = []
    active = 0
    start = 0
    
    for time, delta in events:
        if active == 0 and delta == 1:
            start = time  # New interval starts
        
        active += delta
        
        if active == 0:  # Interval ends
            merged.append([start, time])
    
    return merged
```

### Template 7: Prefix Sum — Longest Positive-Sum Subarray — LC 1124
```python
# Python - Longest subarray with sum > 0 after +1/-1 transform
def longestWPI(hours):
    prefix = 0
    max_len = 0
    seen = {}  # { prefix_sum: first_index }

    for i, h in enumerate(hours):
        prefix += 1 if h > 8 else -1

        if prefix > 0:
            # entire [0..i] is valid
            max_len = i + 1
        else:
            # look for earliest j where prefix[j] == prefix[i] - 1
            # subarray [j+1..i] then has sum == 1 > 0
            if (prefix - 1) in seen:
                max_len = max(max_len, i - seen[prefix - 1])

        seen.setdefault(prefix, i)  # only store first occurrence

    return max_len
```

```java
// Java - LC 1124 Longest Well-Performing Interval
// IDEA: prefix sum +1/-1 transform + HashMap (first occurrence of each sum)
// Key insight: if prefix[i] <= 0, find earliest j where prefix[j] = prefix[i]-1
//              then subarray [j+1..i] has sum = 1 > 0 (well-performing)
// time = O(N), space = O(N)
public int longestWPI(int[] hours) {
    Map<Integer, Integer> map = new HashMap<>();
    int prefix = 0, maxLen = 0;

    for (int i = 0; i < hours.length; i++) {
        prefix += hours[i] > 8 ? 1 : -1;

        if (prefix > 0) {
            maxLen = i + 1;                          // whole prefix is valid
        } else {
            if (map.containsKey(prefix - 1))
                maxLen = Math.max(maxLen, i - map.get(prefix - 1));
        }

        map.putIfAbsent(prefix, i);                  // first occurrence only
    }
    return maxLen;
}
```

### Template 8: Time Sweep + Deadline Heap — LC 1353
```python
# Python - sweep time forward, one slot per tick, serve earliest deadline
# time = O(n log n), space = O(n)
import heapq

def maxItemsServed(items):
    # items: [(start, end)]  -> item valid on ANY single day in [start, end]
    items.sort()             # 1) sort by START -> items become available in time order

    pq = []                  # 2) MIN heap of END days (deadlines) of open items
    i, day, served = 0, 0, 0
    n = len(items)

    while i < n or pq:
        if not pq:
            day = items[i][0]                 # nothing open -> JUMP time forward

        while i < n and items[i][0] <= day:   # PUSH: everything opened by `day`
            heapq.heappush(pq, items[i][1])
            i += 1

        while pq and pq[0] < day:             # PURGE: lazy-delete expired deadlines
            heapq.heappop(pq)

        if pq:                                # SERVE: earliest deadline, consume the day
            heapq.heappop(pq)
            served += 1
            day += 1

    return served
```

```java
// Java - Time sweep + deadline heap (LC 1353 shape)
// IDEA: sort by start; min-PQ of end days; each day serve the earliest deadline
// time = O(N log N), space = O(N)
public int maxItemsServed(int[][] items) {
    Arrays.sort(items, (a, b) -> a[0] - b[0]);
    PriorityQueue<Integer> pq = new PriorityQueue<>();  // end days
    int i = 0, day = 0, served = 0, n = items.length;

    while (i < n || !pq.isEmpty()) {
        if (pq.isEmpty()) day = items[i][0];                 // jump time
        while (i < n && items[i][0] <= day) pq.add(items[i++][1]);  // push
        while (!pq.isEmpty() && pq.peek() < day) pq.poll();  // purge expired
        if (!pq.isEmpty()) { pq.poll(); served++; day++; }    // serve + consume day
    }
    return served;
}
```

**Order matters: PUSH → PURGE → SERVE.** Purging before pushing can leave stale deadlines on top; serving before purging can "serve" an already-expired item.

### Template 9: Sweep + Heap of Retired Jobs (Weighted Interval Scheduling) — LC 1235 ⭐⭐⭐⭐⭐

> **Twist vs Template 8**: here an interval occupies its **whole** `[start, end)`, and each interval carries a **profit**. Greedy fails — we need `best = max profit achievable up to the sweep position`, carried forward by the sweep.

**Key Idea**: sweep by **start time**; the heap holds *taken* jobs keyed by their **end time**. Every job whose `end <= current start` has *retired* — pop it and fold its total into the running `best`. Then `best + profit` is the best total that ends with the current job.

```java
// java
// LC 1235 - Maximum Profit in Job Scheduling
// IDEA: sweep by start; min-heap of (endTime, totalProfitEndingHere);
//       retire jobs with end <= start into a running `best`; push (end, best + profit)
// time = O(N log N), space = O(N)
public int jobScheduling(int[] startTime, int[] endTime, int[] profit) {
    int n = startTime.length;
    int[][] jobs = new int[n][3];
    for (int i = 0; i < n; i++) jobs[i] = new int[]{startTime[i], endTime[i], profit[i]};
    Arrays.sort(jobs, (a, b) -> Integer.compare(a[0], b[0]));      // sweep order = START

    // min-heap on end time: (end, best total profit of a chain ENDING with that job)
    PriorityQueue<int[]> pq = new PriorityQueue<>((a, b) -> Integer.compare(a[0], b[0]));
    int best = 0;                                                  // best profit fully behind the line

    for (int[] j : jobs) {
        // RETIRE: every job finished by the time this one starts is now compatible
        while (!pq.isEmpty() && pq.peek()[0] <= j[0]) best = Math.max(best, pq.poll()[1]);
        pq.add(new int[]{j[1], best + j[2]});                      // take j on top of `best`
    }
    while (!pq.isEmpty()) best = Math.max(best, pq.poll()[1]);     // drain the tail
    return best;
}
```

```python
# python
# LC 1235 - Maximum Profit in Job Scheduling
# IDEA: sweep by start; min heap of (end, total profit ending with that job)
# time = O(N log N), space = O(N)
import heapq

class Solution:
    def jobScheduling(self, startTime, endTime, profit):
        jobs = sorted(zip(startTime, endTime, profit))   # sweep order = START
        pq = []          # min heap of (end_time, best_total_profit_ending_with_that_job)
        best = 0         # best profit among jobs already fully behind the sweep line

        for s, e, p in jobs:
            while pq and pq[0][0] <= s:                  # RETIRE finished jobs
                best = max(best, heapq.heappop(pq)[1])
            heapq.heappush(pq, (e, best + p))            # take this job on top of `best`

        while pq:                                        # drain
            best = max(best, heapq.heappop(pq)[1])
        return best
```

**Why `best` is monotone**: jobs retire in end-time order, and `best` only ever grows — so the value folded in at a given sweep position is exactly "best profit using only jobs that finished before now". That is what makes the O(N log N) one-pass valid without an explicit DP array + binary search.

> **Equivalent formulation**: sort by **end**, `dp[i] = max(dp[i-1], profit[i] + dp[binarySearch(start[i])])`. Same recurrence — the heap just replaces the binary search. Compare with LC 1751 (Template 8's table), which needs the DP form because it also caps the count.

#### Variation 9-1: drop the weights → plain greedy by END — LC 452

> **Twist**: no profits, and we want the *minimum number of groups of mutually overlapping intervals*. With every job worth the same, the heap collapses into a single `end` variable.

```java
// java
// LC 452 - Minimum Number of Arrows to Burst Balloons
// IDEA: sort by END; keep the current arrow at the smallest end seen; a balloon
//       starting after it forces a new arrow (classic activity-selection sweep)
// time = O(N log N), space = O(1) extra
public int findMinArrowShots(int[][] points) {
    if (points.length == 0) return 0;
    Arrays.sort(points, (a, b) -> Integer.compare(a[1], b[1]));  // Integer.compare: avoids overflow
    int arrows = 1, end = points[0][1];
    for (int[] p : points) {
        if (p[0] > end) { arrows++; end = p[1]; }   // strict > : touching ends still burst together
    }
    return arrows;
}
```

```python
# python
# LC 452 - Minimum Number of Arrows to Burst Balloons
# IDEA: sort by END, greedily extend the current shot; new shot when start > current end
# time = O(N log N), space = O(1) extra
class Solution:
    def findMinArrowShots(self, points):
        points.sort(key=lambda x: x[1])          # sweep order = END
        arrows, end = 0, float('-inf')
        for s, e in points:
            if s > end:                          # current arrow cannot reach -> new arrow
                arrows += 1
                end = e
        return arrows
```

**Sort by START vs sort by END — the one-line rule:**

| Sort key | Question it answers | Examples |
|---|---|---|
| **START** | "how many are alive at once?" / "what can I chain onto what's finished?" | 253, 2406, 1094, 1235 |
| **END** | "how many can I keep / how few points cover all?" (greedy pick) | 452, 1353 (heap of ends), 630 |

### Template 10: Gap Sweep over Sorted Coordinates — LC 1465

> **Twist**: instead of counting *coverage*, sweep the sorted cut positions and measure the **holes between consecutive events** — including the two boundary gaps. Separable dimensions ⇒ two independent 1-D sweeps instead of a real 2-D sweep.

```java
// java
// LC 1465 - Maximum Area of a Piece of Cake After Horizontal and Vertical Cuts
// IDEA: 1-D gap sweep per axis: sort cuts, max( first cut, last->limit, consecutive diffs )
//       dimensions are independent -> answer = maxGap(h) * maxGap(w)
// time = O(H log H + V log V), space = O(1) extra
public int maxArea(int h, int w, int[] horizontalCuts, int[] verticalCuts) {
    long MOD = 1_000_000_007L;
    return (int) (maxGap(horizontalCuts, h) * maxGap(verticalCuts, w) % MOD);  // multiply in long!
}

private long maxGap(int[] cuts, int limit) {
    Arrays.sort(cuts);
    long g = Math.max(cuts[0], limit - cuts[cuts.length - 1]);   // the two EDGE gaps
    for (int i = 1; i < cuts.length; i++)
        g = Math.max(g, cuts[i] - cuts[i - 1]);                  // interior gaps
    return g;
}
```

```python
# python
# LC 1465 - Maximum Area of a Piece of Cake After Horizontal and Vertical Cuts
# IDEA: sort each cut list, take the max gap (edges included), multiply the two axes
# time = O(H log H + V log V), space = O(1) extra
class Solution:
    def maxArea(self, h, w, horizontalCuts, verticalCuts):
        MOD = 10 ** 9 + 7

        def max_gap(cuts, limit):
            cuts = sorted(cuts)
            g = max(cuts[0], limit - cuts[-1])          # edge gaps: 0->first, last->limit
            for a, b in zip(cuts, cuts[1:]):
                g = max(g, b - a)                       # interior gaps
            return g

        return (max_gap(horizontalCuts, h) * max_gap(verticalCuts, w)) % MOD
```

**🚫 Two classic traps**: (1) forgetting the **boundary gaps** `0 → cuts[0]` and `cuts[-1] → limit`; (2) applying `% MOD` to each factor *before* multiplying — the max area must be computed on the true values, then reduced once (`max(a%M) * max(b%M)` is not `max(a*b) % M`).

### Template 11: Index Sweep + Active Ordered Set — LC 220

> **Twist**: the sweep line runs over **array indices**, and the state is the *set of values still inside the window*, kept **sorted**. This is the classic sweep-line companion structure (balanced BST / `TreeSet`) — it answers "is there a neighbour within `valueDiff`?" via `ceiling`/`floor` in O(log k).

```java
// java
// LC 220 - Contains Duplicate III
// IDEA: sweep index i; TreeSet holds the last `indexDiff` values (evict as the window slides)
//       nearest candidate >= nums[i]-valueDiff is ceiling(); check it is <= nums[i]+valueDiff
// time = O(N log K), space = O(K)   K = indexDiff
public boolean containsNearbyAlmostDuplicate(int[] nums, int indexDiff, int valueDiff) {
    TreeSet<Long> active = new TreeSet<>();                    // long: |nums[i]| can reach 2^31
    for (int i = 0; i < nums.length; i++) {
        if (i > indexDiff) active.remove((long) nums[i - indexDiff - 1]);   // EVICT out-of-window
        Long c = active.ceiling((long) nums[i] - valueDiff);               // QUERY nearest above
        if (c != null && c <= (long) nums[i] + valueDiff) return true;
        active.add((long) nums[i]);                                        // INSERT current
    }
    return false;
}
```

```python
# python
# LC 220 - Contains Duplicate III  (bucket sweep: O(N) alternative to an ordered set)
# IDEA: bucket width = valueDiff+1, so two values in the SAME bucket always qualify;
#       otherwise only the two neighbouring buckets can hold a match
# time = O(N), space = O(K)
class Solution:
    def containsNearbyAlmostDuplicate(self, nums, indexDiff, valueDiff):
        if valueDiff < 0 or indexDiff <= 0:
            return False
        w = valueDiff + 1
        buckets = {}                                # bucket id -> the single value in it

        for i, x in enumerate(nums):
            b = x // w                              # floor division: correct for negatives too
            if b in buckets:                        # same bucket -> diff <= valueDiff, guaranteed
                return True
            if b - 1 in buckets and x - buckets[b - 1] <= valueDiff:
                return True
            if b + 1 in buckets and buckets[b + 1] - x <= valueDiff:
                return True
            buckets[b] = x
            if i >= indexDiff:                      # EVICT the value leaving the window
                del buckets[nums[i - indexDiff] // w]
        return False
```

**Why a bucket holds at most one value**: if two values shared a bucket we would already have returned `True`, so the invariant is safe. Bucket width `valueDiff + 1` is what makes "same bucket ⇒ answer" true.

### Event Ordering & Tie-Break Rules (deep dive)

The single most common sweep-line bug is the tie at an identical coordinate. Decide it from the **interval semantics**, not by habit:

| Interval semantics | Do touching intervals overlap? | Tie order at equal coordinate | Doc example |
|---|---|---|---|
| `[s, e)` half-open (meetings, times) | **No** — `[1,5)` and `[5,9)` are fine | **END before START** (`sort by (x, delta)`) | LC 253 |
| `[s, e]` inclusive (days, groups) | **Yes** — `[1,5]` and `[5,10]` clash | **START before END** (`sort by (x, -delta)`) | LC 2406 |
| Inclusive, but you'd rather not think | — | emit the end event at `e + 1`, then any tie order works | LC 2021, LC 1094 |

**Escape hatch worth memorising**: converting an inclusive end `e` into an exclusive `e + 1` makes the tie-break *disappear*, because a start and an end can no longer land on the same coordinate for touching intervals. When coordinates are integers, prefer this over a clever comparator.

```python
# python - the two orderings, side by side
events.sort(key=lambda x: (x[0],  x[1]))   # -1 before +1  -> touching does NOT overlap  (half-open)
events.sort(key=lambda x: (x[0], -x[1]))   # +1 before -1  -> touching DOES overlap      (inclusive)
```

```java
// java - same two orderings
events.sort((a, b) -> a[0] != b[0] ? a[0] - b[0] : a[1] - b[1]);  // end(-1) first : half-open
events.sort((a, b) -> a[0] != b[0] ? a[0] - b[0] : b[1] - a[1]);  // start(+1) first: inclusive
```

**Third tie-break level**: when several events share a coordinate *and* a type (skyline starts at the same `x`), order by the payload — LC 218 sorts starts by **descending height** so the tallest wins immediately and no spurious key point is emitted. See [`heap.md`](./heap.md) / [`priority_queue.md`](./priority_queue.md) for the lazy-deletion max-heap that pairs with it.

#### **Sweep + Heap / Ordered-Set Problems**
| Problem | LC # | Key Technique | Difficulty |
|---------|------|---------------|------------|
| Maximum Profit in Job Scheduling | 1235 | Sweep by start + heap of retired jobs (running `best`) | Hard |
| Minimum Number of Arrows to Burst Balloons | 452 | Sort by end + greedy `end` pointer | Medium |
| Maximum Area of a Piece of Cake | 1465 | Gap sweep on sorted cuts, per axis | Medium |
| Contains Duplicate III | 220 | Index sweep + TreeSet window (or bucket sweep) | Hard |
| Minimum Cost to Hire K Workers | 857 | Sort by ratio + max heap of wages (sort one key, heap another) | Hard |
| Minimum Area Rectangle | 939 | Sweep column pairs + seen-pair hash set | Medium |
| Vertical Order Traversal of a Binary Tree | 987 | Vertical sweep with 3-level sort key `(col, row, val)` | Hard |

## Problems by Pattern

### Pattern-Based Problem Tables

#### **Interval Overlap Problems**
| Problem | LC # | Key Technique | Difficulty |
|---------|------|---------------|------------|
| Meeting Rooms II | 253 | Basic sweep line | Medium |
| Car Pooling | 1094 | Capacity tracking | Medium |
| Brightest Position on Street | 2021 | Weighted intervals | Medium |
| Maximum Population Year | 1854 | Year range counting | Easy |
| Maximum Sum Obtained | 2848 | Points on line | Medium |
| Describe the Painting | 1943 | Segment merging | Medium |
| Divide Intervals Into Minimum Number of Groups | 2406 | Event sweep, max concurrent overlaps | Medium |

#### **Skyline Problems**
| Problem | LC # | Key Technique | Difficulty |
|---------|------|---------------|------------|
| The Skyline Problem | 218 | Height tracking | Hard |
| Rectangle Area II | 850 | 2D sweep | Hard |
| Perfect Rectangle | 391 | Corner counting | Hard |
| Falling Squares | 699 | Segment tree + sweep | Hard |

#### **Calendar Booking Problems**
| Problem | LC # | Key Technique | Difficulty |
|---------|------|---------------|------------|
| My Calendar I | 729 | No overlap check | Medium |
| My Calendar II | 731 | Double booking | Medium |
| My Calendar III | 732 | K-booking | Hard |
| Minimum Interval to Include Query | 1851 | Query + sweep | Hard |

#### **Employee Schedule Problems**
| Problem | LC # | Key Technique | Difficulty |
|---------|------|---------------|------------|
| Employee Free Time | 759 | Interval gaps | Hard |
| Interval List Intersections | 986 | Two pointers **or** coverage-`==2` sweep (see 2-7) | Medium |
| Meeting Scheduler | 1229 | Common slots (same sweep as 986 + length filter) | Medium |
| Remove Covered Intervals | 1288 | Sorting + sweep | Medium |

#### **Range Update Problems**
| Problem | LC # | Key Technique | Difficulty |
|---------|------|---------------|------------|
| Range Addition | 370 | Difference array | Medium |
| Corporate Flight Bookings | 1109 | Difference array | Medium |
| Plates Between Candles | 2055 | Prefix + binary search | Medium |
| Count Integers in Intervals | 2276 | Interval merge | Hard |

#### **Prefix Sum Subarray Problems**
| Problem | LC # | Key Technique | Difficulty |
|---------|------|---------------|------------|
| Longest Well-Performing Interval | 1124 | Prefix sum +1/−1, first-occurrence map | Medium |
| Contiguous Array | 525 | Prefix sum 0→−1, first-occurrence map | Medium |
| Subarray Sum Equals K | 560 | Prefix sum count map | Medium |
| Subarray Sums Divisible by K | 974 | Prefix mod, count map | Medium |

#### **Time Sweep + Deadline Heap Problems**
| Problem | LC # | Key Technique | Difficulty |
|---------|------|---------------|------------|
| Maximum Number of Events That Can Be Attended | 1353 | Sort by start + min heap of end days, earliest-deadline-first | Medium |
| Max Number of Events That Can Be Attended II | 1751 | DP + binary search (**not** sweep/heap) | Hard |
| Task Scheduler | 621 | Max heap on frequency + cooling queue | Medium |
| Single-Threaded CPU | 1834 | Jump time to next arrival + min heap on (proc time, idx) | Medium |
| Course Schedule III | 630 | Greedy by deadline + max heap replace | Hard |
| Reorganize String | 767 | Max heap on remaining count, one slot per position | Medium |

#### **Geometric Problems**
| Problem | LC # | Key Technique | Difficulty |
|---------|------|---------------|------------|
| Rectangle Overlap | 836 | 2D overlap | Easy |
| Rectangle Area | 223 | Area calculation | Medium |
| Number of Airplanes in Sky | 391 | Time points | Medium |
| Line Reflection | 356 | Coordinate mapping | Medium |

## Pattern Selection Strategy

```text
Problem Analysis Flowchart:

0. Does each time slot serve only ONE interval (pick a subset, not count)?
   ├── YES → Use Time Sweep + Deadline Heap (Template 8)
   │         ├── Sort by start, min heap of END, serve earliest deadline
   │         └── Jump time when heap empty → drops the O(day-range) factor
   └── NO → Continue to 1

1. Are you counting overlapping intervals?
   ├── YES → Use Basic Sweep Line
   │         ├── Fixed capacity? → Track current count
   │         └── Variable weight? → Track weighted sum
   └── NO → Continue to 2

2. Is it about building heights/skyline?
   ├── YES → Use Skyline Template
   │         ├── 1D skyline → Height events
   │         └── 2D rectangles → Coordinate compression
   └── NO → Continue to 3

3. Managing calendar/bookings?
   ├── YES → Use Calendar Template
   │         ├── Single booking → Simple overlap
   │         ├── Double booking → Count = 2 check
   │         └── K-booking → Count = K check
   └── NO → Continue to 4

4. Finding free time/gaps?
   ├── YES → Use Interval Merge
   │         ├── Merge all intervals
   │         └── Find gaps between merged
   └── NO → Continue to 5

5. Batch range updates?
   ├── YES → Use Difference Array
   │         ├── 1D ranges → Simple difference
   │         └── 2D ranges → 2D difference
   └── NO → Use appropriate combination
```

## Summary & Quick Reference

### Complexity Quick Reference
| Operation | Time Complexity | Space | Notes |
|-----------|-----------------|-------|-------|
| Event Creation | O(n) | O(n) | 2 events per interval |
| Event Sorting | O(n log n) | O(1) | Dominant operation |
| Sweep Processing | O(n) | O(1) | Single pass |
| With TreeMap/Heap | O(n log n) | O(n) | For skyline problems |
| Difference Array | O(n + m) | O(m) | m = range size |
| 2D Sweep | O(n² log n) | O(n²) | Rectangle problems |

### Template Quick Reference
| Template | Pattern | Key Code |
|----------|---------|----------|
| **Basic Sweep** | Count overlaps | `events.sort(); count += delta` |
| **Weighted** | Sum values | `weight += delta * value` |
| **Skyline** | Track heights | `heapq for max height` |
| **Calendar** | Booking conflicts | `if count >= k: reject` |
| **Difference** | Range updates | `diff[start]++; diff[end+1]--` |
| **Merge** | Combine intervals | `if active==0: new interval` |
| **Time Sweep + Deadline Heap** | Pick 1 item per slot | `push start<=day; pop end<day; pop pq; day+=1` |
| **Retired-Job Heap** | Max-weight non-overlapping set | `while pq[0].end<=start: best=max(...); push (end, best+w)` |
| **Gap Sweep** | Largest hole | `sort(cuts); max(cuts[0], limit-cuts[-1], diffs)` |
| **Ordered-Set Sweep** | Nearest value in window | `set.remove(out); set.ceiling(x-t) <= x+t` |
| **Intersection Sweep** | AND of 2 interval lists | `if ++count==2: start=x` / `if count==2: emit [start,x]` |

### Common Patterns & Tricks

#### **Event Ordering Rule**
```python
# Critical: Handle events at same position correctly
# Start before End at same position
events.sort(key=lambda x: (x[0], -x[1]))
# OR End before Start (depends on problem)
events.sort(key=lambda x: (x[0], x[1]))
```

#### **Interval to Events Conversion**
```python
# Standard conversion
for start, end in intervals:
    events.append((start, +1))  # Enter
    events.append((end, -1))     # Exit
    
# Inclusive vs Exclusive endpoints
events.append((end, -1))     # Exclusive end
events.append((end+1, -1))   # Inclusive end
```

#### **Maximum Tracking Pattern**
```python
max_value = 0
current = 0
max_position = 0

for pos, delta in events:
    current += delta
    if current > max_value:
        max_value = current
        max_position = pos
```

#### **Skyline Height Management**
```python
# Use negative for max heap in Python
import heapq
heights = [0]  # Ground level
heapq.heappush(heights, -height)  # Add
max_height = -heights[0]           # Get max
```

### Problem-Solving Steps

1. **Identify Event Types**
   - What marks the start of an interval?
   - What marks the end?
   - Are there other event types?

2. **Design Event Structure**
   - Position/time
   - Event type (start/end)
   - Additional data (value, id, etc.)

3. **Determine Sort Order**
   - Primary: By position/time
   - Secondary: Start vs End handling
   - Tertiary: By value if needed

4. **Process Events**
   - Maintain running state
   - Update maximum/minimum
   - Check constraints

5. **Handle Edge Cases**
   - Same position events
   - Empty intervals
   - Single point intervals
   - Overlapping endpoints

### Common Mistakes & Tips

**🚫 Common Mistakes:**
- Wrong event ordering at same position
- Off-by-one errors with inclusive/exclusive ends
- Not handling empty interval list
- Forgetting to track position of maximum
- Using wrong data structure for height tracking

**✅ Best Practices:**
- Always clarify inclusive vs exclusive intervals
- Use TreeMap/TreeSet for dynamic height queries
- Consider difference array for range updates
- Test with overlapping endpoints
- Visualize the sweep line movement

### Interview Tips

1. **Problem Recognition**
   - "Maximum overlapping" → Sweep line
   - "Skyline/outline" → Height tracking
   - "Free time" → Merge then find gaps (`count == 0`)
   - "Intersection of TWO interval lists" → 2 pointers (O(m+n)); sweep with `count == 2` if unsorted / k lists (Template 2-7)
   - "Range updates" → Difference array
   - "One event per day / per slot, each with a deadline" → Time sweep + deadline heap
   - "Max **profit/weight** from non-overlapping intervals" → Sweep by start + retired-job heap (Template 9)
   - "Fewest points/arrows covering every interval" → Sort by end + greedy (Variation 9-1)
   - "Any two values within t, indices within k" → Index sweep + ordered set / buckets (Template 11)

2. **Clarify Requirements**
   - Are intervals inclusive or exclusive?
   - Can intervals have zero length?
   - How to handle same-position events?
   - Is the answer count or specific intervals?

3. **Optimization Opportunities**
   - Coordinate compression for large ranges
   - Segment tree for dynamic updates
   - Binary search for point queries
   - Lazy propagation for range updates

4. **Common Follow-ups**
   - Handle dynamic interval additions
   - Query at specific points
   - Find k-th largest overlap
   - Support interval modifications

### Advanced Techniques

#### **Coordinate Compression**
```python
# Compress large coordinate space
coords = set()
for start, end in intervals:
    coords.add(start)
    coords.add(end)
coord_map = {v: i for i, v in enumerate(sorted(coords))}
```

#### **Segment Tree Integration**
- Use for dynamic updates
- Query range maximum/minimum
- Lazy propagation for efficiency

#### **Persistent Data Structure**
- Track history of changes
- Query at any timestamp
- Useful for temporal databases

### Related Topics
- **Interval Problems**: Merge, insert, remove intervals
- **Greedy Algorithms**: Activity selection
- **Computational Geometry**: Line intersection
- **Data Stream**: Processing events in order
- **Difference Array**: Efficient range updates

## LC Examples

### 2-1) Meeting Rooms II (LC 253) — Sweep Line Peak Count
> Emit +1 on start, -1 on end; sort events; peak concurrent count = min rooms needed.

```java
// LC 253 - Meeting Rooms II
// IDEA: Sweep line — +1 on start, -1 on end; sort (end before start at ties); track peak
// time = O(N log N), space = O(N)
public int minMeetingRooms(int[][] intervals) {
    List<int[]> events = new ArrayList<>();
    for (int[] inv : intervals) {
        events.add(new int[]{inv[0], 1});
        events.add(new int[]{inv[1], -1});
    }
    events.sort((a, b) -> a[0] != b[0] ? a[0] - b[0] : a[1] - b[1]); // end before start at same time
    int rooms = 0, maxRooms = 0;
    for (int[] e : events) { rooms += e[1]; maxRooms = Math.max(maxRooms, rooms); }
    return maxRooms;
}
```

```python
# LC 253 Meeting Rooms II
# NOTE : there're also priority queue, sorting approaches

# V0
# IDEA : SCANNING LINE : Sort all time points and label the start and end points. Move a vertical line from left to right.
class Solution:
     def minMeetingRooms(self, intervals):
            lst = []
            """
            NOTE THIS !!!
            """
            for start, end in intervals:
                lst.append((start, 1))
                lst.append((end, -1))
            # all of below sort work
            #lst.sort()
            lst.sort(key = lambda x : [x[0], x[1]])
            res, curr_rooms = 0, 0
            for t, n in lst:
                curr_rooms += n
                res = max(res, curr_rooms)
            return res

# V0''
# IDEA : SCANNING LINE
# Step 1 : split intervals to points, and label start, end point
# Step 2 : reorder the points
# Step 3 : go through every point, if start : result + 1, if end : result -1, and record the maximum result in every iteration
class Solution:
    def minMeetingRooms(self, intervals):
        if intervals is None or len(intervals) == 0:
            return 0

        tmp = []

        # set up start and end points 
        for inter in intervals:
            tmp.append((inter[0], True))
            tmp.append((inter[1], False))

        # sort 
        tmp = sorted(tmp, key=lambda v: (v[0], v[1]))

        n = 0
        max_num = 0
        for arr in tmp:
            # start point +1 
            if arr[1]:
                n += 1
            # end point -1 
            else:
                n -= 1 # release the meeting room
            max_num = max(n, max_num)
        return max_num
```

### 2-2) Brightest Position on Street (LC 2021) — Weighted Sweep Line
> Emit +1 at p−r and −1 at p+r+1; track position with maximum accumulated brightness.

```java
// LC 2021 - Brightest Position on Street
// IDEA: Sweep line — +1 at range start, -1 at range end+1; track max brightness position
// time = O(N log N), space = O(N)
public int brightestPosition(int[][] lights) {
    List<int[]> events = new ArrayList<>();
    for (int[] light : lights) {
        events.add(new int[]{light[0] - light[1], 1});
        events.add(new int[]{light[0] + light[1] + 1, -1});
    }
    events.sort((a, b) -> a[0] - b[0]);
    int brightness = 0, maxBrightness = 0, ans = 0;
    for (int[] e : events) {
        brightness += e[1];
        if (brightness > maxBrightness) { maxBrightness = brightness; ans = e[0]; }
    }
    return ans;
}
```

```python
# LC 2021. Brightest Position on Street
# V0
# IDEA : Scanning line, LC 253 MEETING ROOM II
class Solution:
    def brightestPosition(self, lights: List[List[int]]) -> int:
        # light range array
        light_r = []
        for p,r in lights:
            light_r.append((p-r,'start'))
            light_r.append((p+r+1,'end'))
        light_r.sort(key = lambda x:x[0])
        # focus on the boundary of light range 
        
        bright = collections.defaultdict(int)
        power = 0
        for l in light_r:
            if 'start' in l:
                power += 1
            else:
                power -= 1
            bright[l[0]] = power # NOTE : we update "power" in each iteration
                
        list_bright = list(bright.values())
        list_position = list(bright.keys())
        
        max_bright = max(list_bright)
        max_bright_index = list_bright.index(max_bright)
        
        return list_position[max_bright_index]

# V0'
# IDEA : Scanning line, meeting room
from collections import defaultdict
class Solution(object):
    def brightestPosition(self, lights):
        # edge case
        if not lights:
            return
        _lights = []
        for x in lights:
            """
            NOTE this !!!
             -> 1) scanning line trick
             -> 2) we add 1 to idx for close session (_lights.append([x[0]+x[1]+1, -1]))
            """
            _lights.append([x[0]-x[1], 1])
            _lights.append([x[0]+x[1]+1, -1])
        _lights.sort(key = lambda x : x)
        #print ("_lights = " + str(_lights))
        d = defaultdict(int)
        up = 0
        for a, b in _lights:
            if b == 1:
                up += 1
            else:
                up -= 1
            d[a] = up
        print ("d = " + str(d))
        _max = max(d.values())
        res = [i for i in d if d[i] == _max]
        #print ("res = " + str(res))
        return min (res)

# V1
# IDEA : LC 253 MEETING ROOM II
# https://leetcode.com/problems/brightest-position-on-street/discuss/1494005/Python%3A-Basically-meeting-room-II
# IDEA :
# So, the only difference in this problem in comparison to meeting room II is that we have to convert our input into intervals, which is straightforward and basically suggested to use by the first example. So, here is my code and here is meeting rooms II https://leetcode.com/problems/meeting-rooms-ii/
class Solution:
    def brightestPosition(self, lights: List[List[int]]) -> int:
        intervals, heap, res, best = [], [], 0, 0
        for x, y in lights:
            intervals.append([x-y, x+y])
            
        intervals.sort()

        for left, right in intervals:            
            while heap and heap[0] < left: 
                heappop(heap)
            heappush(heap, right)
            if len(heap) > best:
                best = len(heap)
                res = left
        return res
```

### 2-3) Divide Intervals Into Minimum Number of Groups (LC 2406) — Sweep Line Peak Count
> Min groups = peak concurrent overlaps. Three patterns: (1) sweep line events (+1/-1), (2) sort+PQ reuse group when `pq.peek() < start` (strict `<` because endpoints are inclusive), (3) difference array for fixed-range coordinates.
>
> **Key trap**: `[1,5]` and `[5,10]` overlap — use `pq.peek() < start` (not `<=`) in PQ approach, and sort start(+1) before end(−1) at same time in sweep approach.
>
> **Similar LC**: 253 Meeting Rooms II, 1094 Car Pooling, 2021 Brightest Position on Street, 1854 Maximum Population Year, 729/731/732 My Calendar I/II/III

```java
// LC 2406 - Divide Intervals Into Minimum Number of Groups
// IDEA: Sweep line — +1 on start, -1 on end; start before end at same time (inclusive overlap)
// time = O(N log N), space = O(N)
public int minGroups(int[][] intervals) {
    List<int[]> events = new ArrayList<>();
    for (int[] inv : intervals) {
        events.add(new int[]{inv[0], 1});
        events.add(new int[]{inv[1], -1});
    }
    events.sort((a, b) -> a[0] != b[0] ? a[0] - b[0] : b[1] - a[1]); // start(+1) before end(-1)
    int cur = 0, max = 0;
    for (int[] e : events) { cur += e[1]; max = Math.max(max, cur); }
    return max;
}

// Alt: Sort + Min-PQ (reuse group when earliest end < current start)
// time = O(N log N), space = O(N)
public int minGroups_pq(int[][] intervals) {
    Arrays.sort(intervals, (a, b) -> a[0] - b[0]);
    PriorityQueue<Integer> pq = new PriorityQueue<>(); // stores end times
    for (int[] inv : intervals) {
        if (!pq.isEmpty() && pq.peek() < inv[0]) pq.poll(); // reuse group
        pq.add(inv[1]);
    }
    return pq.size(); // active groups = peak overlap
}

// Alt: Difference array (fixed coordinate range)
// time = O(N + M), space = O(M)  where M = max coordinate
public int minGroups_diff(int[][] intervals) {
    int[] diff = new int[1_000_002];
    for (int[] inv : intervals) { diff[inv[0]]++; diff[inv[1] + 1]--; }
    int max = 0, cur = 0;
    for (int d : diff) { cur += d; max = Math.max(max, cur); }
    return max;
}
```

### 2-4) My Calendar II (LC 731) — Track Double-Booked Intervals
> New booking is invalid only if it overlaps a double-booked segment; otherwise record overlap.

```java
// LC 731 - My Calendar II
// IDEA: Track booked and overlaps lists; reject if new booking intersects any overlap
// time = O(N^2), space = O(N)
class MyCalendarTwo {
    List<int[]> booked = new ArrayList<>(), overlaps = new ArrayList<>();
    public boolean book(int start, int end) {
        for (int[] ov : overlaps)
            if (start < ov[1] && end > ov[0]) return false; // triple overlap
        for (int[] bk : booked)
            if (start < bk[1] && end > bk[0])
                overlaps.add(new int[]{Math.max(start, bk[0]), Math.min(end, bk[1])});
        booked.add(new int[]{start, end});
        return true;
    }
}
```

### 2-5) Longest Well-Performing Interval (LC 1124) — Prefix Sum + First-Occurrence HashMap
> Transform each day to +1 (tiring, >8 h) or −1 (non-tiring). Find the longest subarray whose sum > 0.
>
> **Core idea**:
> - If `prefix > 0` at index `i` → entire range `[0..i]` is valid (length = `i+1`).
> - Otherwise, look up the **first index** where `prefix - 1` was seen; the subarray from that index+1 to i has sum = 1 > 0.
>
> **Key insight**: storing only the *first* occurrence maximises the subarray length. This is identical to LC 525 (Contiguous Array) where 0 is mapped to -1.
>
> **Similar LC**: 525 Contiguous Array, 560 Subarray Sum Equals K, 974 Subarray Sums Divisible by K

```java
// LC 1124 - Longest Well-Performing Interval
// IDEA: prefix sum (+1/-1 transform) + HashMap (first occurrence of each prefix sum)
// Case 1: prefix > 0  → maxLen = i + 1 (whole prefix valid)
// Case 2: prefix <= 0 → find earliest j where prefix[j] = prefix[i]-1; length = i - j
// time = O(N), space = O(N)
public int longestWPI(int[] hours) {
    Map<Integer, Integer> map = new HashMap<>();
    int prefix = 0, maxLen = 0;

    for (int i = 0; i < hours.length; i++) {
        prefix += hours[i] > 8 ? 1 : -1;

        if (prefix > 0) {
            maxLen = i + 1;
        } else {
            if (map.containsKey(prefix - 1))
                maxLen = Math.max(maxLen, i - map.get(prefix - 1));
        }

        map.putIfAbsent(prefix, i);   // only first occurrence
    }
    return maxLen;
}
```

```python
# LC 1124 - Longest Well-Performing Interval
# IDEA: prefix sum +1/-1 + dict of first occurrence
class Solution:
    def longestWPI(self, hours: List[int]) -> int:
        prefix, max_len = 0, 0
        seen = {}  # { prefix_sum: first_index }

        for i, h in enumerate(hours):
            prefix += 1 if h > 8 else -1

            if prefix > 0:
                max_len = i + 1
            else:
                if (prefix - 1) in seen:
                    max_len = max(max_len, i - seen[prefix - 1])

            seen.setdefault(prefix, i)

        return max_len
```

### 2-6) Maximum Number of Events That Can Be Attended (LC 1353) — Time Sweep + Deadline Heap

> Reference: `leetcode_python/Heap/maximum-number-of-events-that-can-be-attended.py`
>
> `events[i] = [start_i, end_i]`. Attend event `i` on **any single day** `d` with `start_i <= d <= end_i`, **one event per day**. Return the max number of events attendable.
>
> ```
> events = [[1,2],[2,3],[3,4]]        -> 3
> events = [[1,2],[2,3],[3,4],[1,2]]  -> 4
> ```

#### Core Idea

**Sweep time forward and greedily attend the event that ends soonest (earliest-deadline-first).**

This is a sweep line where the sweep **consumes** a resource instead of just counting: each day is one slot, so at every tick we must choose *which* open event to spend it on.

| Sweep component | Concretely |
|---|---|
| Event stream | `events` sorted by **start** → a single forward pointer `i` pushes each event exactly once |
| Sweep state | `pq` = **min heap of end days** of currently-open events → `pq[0]` = the most urgent deadline |
| State cleanup | `while pq and pq[0] < day: pop` → **lazy deletion** (a heap can't remove arbitrary elements) |
| Slot consumption | `heappop(pq); ans += 1; day += 1` → one event per day |
| Sweep advance | `if not pq: day = events[i][0]` → skip idle stretches |

Each step, in this order — **PUSH → PURGE → ATTEND**:
1. **PUSH** every event with `start <= day` into the heap.
2. **PURGE** expired events (`end < day`) off the top.
3. **ATTEND** `pq[0]` (earliest deadline), then `day += 1`.

Reordering these breaks it: purging before pushing can leave stale deadlines on top; attending before purging can "attend" an expired event.

**Why greedy on `end` (not `start`, not duration)?** If two events are both open today, taking the earlier-ending one never hurts — the later-ending one still has at least as many days left to be scheduled (exchange argument).

```text
events = [[1,4],[1,1]]     day 1: pq = [1, 4]
                           pop 1 ✅ -> day 2: pq = [4] -> attend        => 2
                           pop 4 ❌ -> day 2: pq = [1] already expired  => 1
```

**Sorting by start vs heap by end** — these do two different jobs: the **sort** controls *when an event becomes visible*, the **heap** controls *which visible event we spend the day on*.

```python
# python
# LC 1353. Maximum Number of Events That Can Be Attended
# IDEA: TIME SWEEP + MIN HEAP OF DEADLINES (greedy, earliest-deadline-first)

# V0 : day-jumping  -> time = O(n log n), space = O(n)  (independent of day range)
import heapq

class Solution(object):
    def maxEvents(self, events):
        events.sort()          # by start day
        pq = []                # NOTE !!! min heap of END days
        i = day = ans = 0
        n = len(events)

        while i < n or pq:
            # nothing open -> fast-forward the sweep to the next start day
            if not pq:
                day = events[i][0]

            # PUSH: all events opened by `day`
            while i < n and events[i][0] <= day:
                heapq.heappush(pq, events[i][1])
                i += 1

            # PURGE: lazy-delete expired deadlines
            while pq and pq[0] < day:
                heapq.heappop(pq)

            # ATTEND: earliest deadline, consume this day
            if pq:
                heapq.heappop(pq)
                ans += 1
                day += 1

        return ans


# V0-1 : scan every day  -> time = O(D + n log n), D = day range (1e5), space = O(n)
class Solution(object):
    def maxEvents(self, events):
        events.sort(key=lambda x: -x[0])     # DESC so events.pop() = smallest start
        end_days = []
        ans = 0
        for day in range(1, 100001):
            while events and events[-1][0] <= day:      # PUSH
                heapq.heappush(end_days, events.pop()[1])
            while end_days and end_days[0] < day:       # PURGE
                heapq.heappop(end_days)
            if end_days:                                # ATTEND
                heapq.heappop(end_days)
                ans += 1
        return ans
```

```java
// LC 1353 - Maximum Number of Events That Can Be Attended
// IDEA: time sweep + min-PQ of end days; each day attend the earliest deadline
// time = O(N log N), space = O(N)
public int maxEvents(int[][] events) {
    Arrays.sort(events, (a, b) -> a[0] - b[0]);          // by start day
    PriorityQueue<Integer> pq = new PriorityQueue<>();   // end days (deadlines)
    int i = 0, day = 0, ans = 0, n = events.length;

    while (i < n || !pq.isEmpty()) {
        if (pq.isEmpty()) day = events[i][0];                        // jump time
        while (i < n && events[i][0] <= day) pq.add(events[i++][1]); // PUSH
        while (!pq.isEmpty() && pq.peek() < day) pq.poll();          // PURGE expired
        if (!pq.isEmpty()) { pq.poll(); ans++; day++; }               // ATTEND
    }
    return ans;
}
```

#### Pattern: Time Sweep + Deadline Heap

| Step | Data structure | Purpose |
|------|---------------|---------|
| Sort by start | array + pointer `i` | Events enter the sweep in time order; each pushed once |
| Track open set | `pq` = min heap of **end** days | `pq[0]` = most urgent deadline still open |
| Drop expired | `while pq[0] < day: pop` | **Lazy deletion** — heap can't remove arbitrary items |
| Consume a slot | pop `pq` + `day += 1` | One event per day, greedily the most urgent |
| Skip idle time | `if not pq: day = events[i][0]` | Removes the O(day-range) factor |

**vs. the counting sweep (LC 253 / 2406):** same `sort by start` + `min heap of end times` skeleton, but there the heap size *is* the answer (how many intervals are concurrent) and nothing is consumed. Here the sweep spends one slot per tick, so the heap exists to answer **"which one?"**.

| | Counting sweep (253, 2406) | Deadline heap (1353) |
|---|---|---|
| Question | How many overlap at peak? | How many can I pick, 1 per slot? |
| Heap role | Size = concurrent count | Top = who to serve now |
| Output | `pq.size()` / max counter | Count of pops |
| Interval semantics | Occupies the **whole** interval | Occupies **one day** inside it |

#### Similar LC

| LC # | Problem | Shared pattern | Key difference |
|------|---------|---------------|----------------|
| 1751 | Max Number of Events Attended II | Same events input | Events occupy the **whole** interval + have values → DP + binary search, **not** sweep/heap |
| 253 | Meeting Rooms II | Sort by start, min heap of end times | Counts concurrent intervals; doesn't pick a subset |
| 2406 | Divide Intervals Into Min Groups | Sort by start, min heap of end times | Interval-partition framing of 253 (see 2-3) |
| 621 | Task Scheduler | Time sweep, one slot per tick | Max heap on frequency + cooling queue (not deadlines) |
| 1834 | Single-Threaded CPU | Jump time to next arrival, push arrived, pop best | Min heap on (processing time, index); tasks occupy multiple ticks |
| 630 | Course Schedule III | Greedy by deadline + heap | Max heap **replace**: drop the longest course when overrunning |
| 767 | Reorganize String | One slot per position, greedy heap pick | Max heap on remaining count + last-used guard |
| 502 | IPO | Sort by one key, heap by another | Two-heap greedy (capital → max heap of profit) |
| 871 | Min Number of Refueling Stops | Push reachable options, greedily pop best | Max heap of fuel, pop only when stuck |

> **Cross-ref**: full heap-side write-up in [`heap_examples.md` § 7](./heap_examples.md#7-maximum-number-of-events-that-can-be-attended--lc-1353)

### 2-7) Interval List Intersections (LC 986) — Intersection Sweep (coverage == 2) ⭐⭐⭐⭐

> Reference: `leetcode_python/Two_Pointers/interval-list-intersections.py` (V1-3 / V1-4)
>
> Two lists of **closed**, **sorted**, **pairwise-disjoint** intervals. Return every interval
> covered by *both* lists.
>
> ```
> firstList  = [[0,2],[5,10],[13,23],[24,25]]
> secondList = [[1,5],[8,12],[15,24],[25,26]]
> ->           [[1,2],[5,5],[8,10],[15,23],[24,24],[25,25]]
> ```
>
> The classic solution is 2 pointers (see [`2_pointers.md` § 2-12](./2_pointers.md)); this section
> is the **sweep-line** framing, which generalises far better.

#### Core Idea

**Throw away the "two lists" structure. Merge everything into one event stream and emit the
stretches where coverage is 2.**

| Sweep component | Concretely |
|---|---|
| Events | each interval `[s, e]` → `(s, START)` and `(e, END)`, from **both** lists into one array |
| Sweep state | `active_count` = how many intervals cover the current x |
| Overlap **opens** | `active_count` rises to **2** → record `start_pos = x` |
| Overlap **closes** | an END fires while `active_count == 2` → emit `[start_pos, x]` |
| Output | list of maximal `coverage == 2` stretches |

**Why `== 2` is the whole trick**: within *one* list the intervals are pairwise disjoint, so at
any x each list contributes **at most 1** to the counter. Therefore
`active_count == 2` ⟺ *one interval from each list* covers x ⟺ intersection.

> ⚠️ **This is exactly where the shortcut is fragile.** If either list could self-overlap,
> `active_count == 2` might mean "two intervals from the same list" — a false positive.
> The robust form keeps **one counter per list** and tests `active_first > 0 and active_second > 0`
> (see the variation below). Say this out loud in an interview; it's the follow-up they want.

**Tie-break — START must be processed before END at the same coordinate.** Intervals are
**closed**, so `[0,2]` and `[2,7]` intersect at the single point `[2,2]`. The Python trick:

```python
START, END = -1, 1        # -1 < 1  ->  plain events.sort() puts START first at ties
```

If you flipped it (`START = 1, END = -1` with a plain sort), the counter would drop to 1 before
rising again and every single-point intersection like `[5,5]` / `[24,24]` would be lost.

**Complexity**: `O((m + n) log(m + n))` — the sort dominates; `O(m + n)` space for the events.
This is **strictly worse than the 2-pointer O(m + n)** solution, which exploits the fact that both
inputs are *already sorted*. Sweep line is the right tool when that assumption dies (see below).

#### Visual Trace

```text
firstList = [[0,2],[5,10]]   secondList = [[1,5],[8,12]]

events (sorted, START=-1 first at ties):
  (0,S) (1,S) (2,E) (5,S) (5,E) (8,S) (10,E) (12,E)

x   type   active  action
--------------------------------------------------
0   START  0->1    -
1   START  1->2    overlap OPENS   -> start_pos = 1
2   END    2->1    active==2       -> emit [1, 2]
5   START  1->2    overlap OPENS   -> start_pos = 5   (START before END at x=5 !!)
5   END    2->1    active==2       -> emit [5, 5]     <- single-point intersection
8   START  1->2    overlap OPENS   -> start_pos = 8
10  END    2->1    active==2       -> emit [8, 10]
12  END    1->0    active==1       -> nothing

ans = [[1,2],[5,5],[8,10]]
```

#### Pattern (Python) — single counter, `== 2`

```python
# python
# LC 986 - Interval List Intersections
# IDEA: SCAN LINE — merge both lists into one event stream, emit stretches where coverage == 2
# time = O((m+n) log(m+n)), space = O(m+n)
class Solution(object):
    def intervalIntersection(self, firstList, secondList):
        # NOTE !!! START = -1 so that a plain sort() puts START BEFORE END at ties
        #          (closed intervals -> touching intervals DO intersect, e.g. [5,5])
        START, END = -1, 1

        events = []
        # 1) intervals -> discrete events (BOTH lists go into the SAME stream)
        for s, e in firstList:
            events.append((s, START))
            events.append((e, END))
        for s, e in secondList:
            events.append((s, START))
            events.append((e, END))

        # 2) sort by coordinate; ties -> START(-1) before END(1)
        events.sort()

        ans = []
        active_count = 0
        start_pos = None

        # 3) sweep the timeline
        for x, event_type in events:
            if event_type == START:
                active_count += 1
                if active_count == 2:        # both lists now cover x -> overlap OPENS
                    start_pos = x
            else:  # END
                if active_count == 2:        # overlap was open -> it CLOSES here
                    ans.append([start_pos, x])
                active_count -= 1

        return ans
```

#### Pattern (Java)

```java
// java
// LC 986 - Interval List Intersections
// IDEA: scan line — one merged event stream; emit while coverage == 2
// time = O((m+n) log(m+n)), space = O(m+n)
public int[][] intervalIntersection(int[][] firstList, int[][] secondList) {
    List<int[]> events = new ArrayList<>();
    for (int[] iv : firstList)  { events.add(new int[]{iv[0], -1}); events.add(new int[]{iv[1], 1}); }
    for (int[] iv : secondList) { events.add(new int[]{iv[0], -1}); events.add(new int[]{iv[1], 1}); }

    // ties: -1 (START) before 1 (END)  -> closed intervals, single-point overlaps survive
    events.sort((a, b) -> a[0] != b[0] ? Integer.compare(a[0], b[0]) : Integer.compare(a[1], b[1]));

    List<int[]> ans = new ArrayList<>();
    int active = 0, startPos = 0;

    for (int[] ev : events) {
        if (ev[1] == -1) {                       // START
            if (++active == 2) startPos = ev[0];
        } else {                                 // END
            if (active == 2) ans.add(new int[]{startPos, ev[0]});
            active--;
        }
    }
    return ans.toArray(new int[ans.size()][2]);
}
```

#### Variation: per-list counters (robust, generalises to "AND of k sets")

> Use this when a list may **self-overlap** (then `active_count == 2` is no longer equivalent to
> "one from each"), or when you need the intersection of `k` lists.

```python
# python
# LC 986 - Interval List Intersections (scan line, per-list counters)
# IDEA: track active count PER LIST; intersection is open iff EVERY list has coverage > 0
# time = O((m+n) log(m+n)), space = O(m+n)
class Solution(object):
    def intervalIntersection(self, firstList, secondList):
        events = []
        for s, e in firstList:                 # list_type = 0
            events.append((s, 1, 0))
            events.append((e, -1, 0))
        for s, e in secondList:                # list_type = 1
            events.append((s, 1, 1))
            events.append((e, -1, 1))

        # ties: start(+1) BEFORE end(-1)  ->  -x[1] as secondary key
        events.sort(key=lambda x: (x[0], -x[1]))

        ans = []
        active_first = active_second = 0
        intersection_start = None

        for pos, delta, list_type in events:
            if list_type == 0:
                active_first += delta
            else:
                active_second += delta

            if active_first > 0 and active_second > 0:      # intersection is OPEN
                if intersection_start is None:
                    intersection_start = pos
            else:                                            # it just CLOSED here
                if intersection_start is not None:
                    ans.append([intersection_start, pos])
                    intersection_start = None

        return ans
```

For `k` lists: keep `active = [0] * k` and open the intersection when `min(active) > 0`
(or maintain a `numPositive` counter to avoid the O(k) check per event).

#### Pattern Summary

| Goal | Coverage condition to emit | Doc example |
|---|---|---|
| **Intersection** (AND) of 2 lists | `count == 2` (or both per-list counters > 0) | **LC 986 (here)** |
| **Union** (OR) / merge | `count` goes `0 -> 1` opens, `-> 0` closes | LC 56 (Template 6) |
| **Peak concurrency** | track `max(count)` | LC 253, 2406 (Template 1) |
| **k-booking conflict** | reject when `count >= k` | LC 731, 732 (Template 4) |
| **Free time** (NOT) | emit gaps where `count == 0` | LC 759 |

**Same skeleton, one line different** — that line is the *predicate on the coverage counter*.
Recognising this collapses the whole interval family into a single template.

#### Sweep line vs 2 pointers for LC 986

| | 2 pointers (`2_pointers.md` § 2-12) | Sweep line (here) |
|---|---|---|
| Time | **O(m + n)** ✅ | O((m+n) log(m+n)) — sort |
| Space | O(1) extra | O(m + n) events |
| Requires sorted input | **Yes** (both lists) | No — the sort handles it |
| Requires disjoint input | No | Only for the `== 2` shortcut; per-list counters lift it |
| Extends to k lists | Awkward (k pointers, min-heap) | Natural (`min(active) > 0`) |
| Interview answer | The expected optimal | The "generalise it" answer |

**Rule of thumb**: inputs already sorted + exactly 2 lists → **2 pointers**. Unsorted, self-overlapping,
k lists, or the question morphs into "union / free time / peak" → **sweep line**.

#### Similar LC

| LC # | Problem | Shared pattern | Key difference |
|------|---------|---------------|----------------|
| **986** | **Interval List Intersections** | **coverage == 2 sweep** | **AND of 2 disjoint sorted lists** |
| 1229 | Meeting Scheduler | Same intersection sweep | Return the **first** intersection of length >= `duration` |
| 759 | Employee Free Time | Same merged event stream | Emit where `count == 0` (the complement / gaps) |
| 56 | Merge Intervals | Coverage 0↔1 transitions | Union instead of intersection |
| 57 | Insert Interval | Single new interval | 3-phase pointer scan, no event stream needed |
| 253 | Meeting Rooms II | `+1/-1` counter | Wants `max(count)`, not the ranges |
| 2406 | Divide Intervals Into Min Groups | `+1/-1` counter, inclusive ties | Peak count = min groups (see 2-3) |
| 729 | My Calendar I | Overlap test `max(s) < min(e)` | Online insert, reject on any overlap |
| 731 / 732 | My Calendar II / III | Coverage threshold | Emit/reject at `count >= 2` / track max `count` |
| 715 | Range Module | Coverage bookkeeping | Add/remove/query ranges dynamically (ordered map) |
| 1288 | Remove Covered Intervals | Sort + sweep | Drop intervals fully covered by another |
| 850 | Rectangle Area II | 2-D sweep | Intersection logic on the y-axis at each x slab |