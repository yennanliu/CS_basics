# Scanning Line (Line Sweep) Algorithm

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

### **Pattern 4: Employee Free Time**
- **Description**: Finding common free time across schedules
- **Examples**: LC 759, 986, 1229
- **Pattern**: Merge intervals then find gaps

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
| Interval List Intersections | 986 | Two pointers | Medium |
| Meeting Scheduler | 1229 | Common slots | Medium |
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

```
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
   - "Free time" → Merge then find gaps
   - "Range updates" → Difference array
   - "One event per day / per slot, each with a deadline" → Time sweep + deadline heap

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

```
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

> **Cross-ref**: full heap-side write-up in [`heap.md` § 2-7](./heap.md#2-7-maximum-number-of-events-that-can-be-attended--lc-1353)