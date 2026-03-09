# Intervals

## Overview

**Intervals** are problems involving ranges of values, typically represented as `[start, end]` pairs, requiring operations like merging overlapping ranges, finding intersections, or scheduling non-overlapping events.

### Key Properties
- **Time Complexity**: O(n log n) for sorting + O(n) for processing = O(n log n) overall
- **Space Complexity**: O(1) to O(n) depending on output requirements
- **Core Idea**: Sort intervals by start time, then process linearly to handle overlaps
- **When to Use**: Problems involving ranges, scheduling, calendar management, resource allocation

### Core Algorithm Steps
1. **Sort intervals** by start time (occasionally by end time for greedy problems)
2. **Process sequentially** to identify overlaps or non-overlaps
3. **Apply merge/remove strategy** based on problem requirements
4. **Handle edge cases** like empty intervals or single intervals

### When to Use Interval Algorithms
- **Merge overlapping ranges**: Calendar conflicts, memory allocation
- **Scheduling optimization**: Meeting rooms, task assignment
- **Range queries**: Time series data, genomic sequences
- **Resource management**: Bandwidth allocation, CPU scheduling

### References
- [labuladong: Interval Merge](https://github.com/labuladong/fucking-algorithm/blob/master/%E7%AE%97%E6%B3%95%E6%80%9D%E7%BB%B4%E7%B3%BB%E5%88%97/%E5%8C%BA%E9%97%B4%E8%B0%83%E5%BA%A6%E9%97%AE%E9%A2%98%E4%B9%8B%E5%8C%BA%E9%97%B4%E5%90%88%E5%B9%B6.md)
- [labuladong: Interval Overlap](https://github.com/labuladong/fucking-algorithm/blob/master/%E7%AE%97%E6%B3%95%E6%80%9D%E7%BB%B4%E7%B3%BB%E5%88%97/%E5%8C%BA%E9%97%B4%E4%BA%A4%E9%9B%86%E9%97%AE%E9%A2%98.md)
- [Visualization Explanation](https://github.com/yennanliu/CS_basics/blob/master/doc/cheatsheet/array_overlap_explaination.md)

## 1) Problem Categories

### **Pattern 1: Interval Merging**
- **Description**: Combine overlapping intervals into single merged intervals
- **Examples**: LC 56 (Merge Intervals), LC 57 (Insert Interval)
- **Recognition**: "Merge", "combine", "overlapping intervals"
- **Sorting**: By start time (ascending)

### **Pattern 2: Interval Scheduling (Greedy)**
- **Description**: Find maximum non-overlapping intervals or minimum intervals to remove
- **Examples**: LC 435 (Non-overlapping Intervals), LC 452 (Minimum Arrows)
- **Recognition**: "Maximum", "minimum", "non-overlapping", "remove"
- **Sorting**: By end time (ascending) for greedy approach

### **Pattern 3: Interval Intersection**
- **Description**: Find common time slots or overlapping regions between interval lists
- **Examples**: LC 986 (Interval List Intersections), LC 1288 (Remove Covered Intervals)
- **Recognition**: "Intersection", "overlap", "common", "covered"
- **Sorting**: By start time, process two pointers

### **Pattern 4: Interval Point Coverage**
- **Description**: Determine points that can cover multiple intervals or find gaps
- **Examples**: LC 452 (Minimum Arrows), LC 1024 (Video Stitching)
- **Recognition**: "Cover", "points", "arrows", "minimum coverage"
- **Sorting**: By start or end time depending on strategy

### **Pattern 5: Meeting Room Scheduling**
- **Description**: Determine meeting room requirements or check scheduling conflicts
- **Examples**: LC 252 (Meeting Rooms), LC 253 (Meeting Rooms II)
- **Recognition**: "Meeting", "conference", "rooms", "schedule conflicts"
- **Sorting**: By start time, use priority queue for room management

### **Pattern 6: Calendar and Booking**
- **Description**: Handle calendar bookings with conflict detection and resolution
- **Examples**: LC 729 (My Calendar I), LC 731 (My Calendar II), LC 732 (My Calendar III)
- **Recognition**: "Calendar", "booking", "double booking", "k-booking"
- **Sorting**: Maintain sorted intervals, binary search for insertion

## 2) Templates & Algorithms

### Template Comparison Table
| Template Type | Use Case | Sorting Strategy | When to Use |
|---------------|----------|------------------|-------------|
| **Merge Template** | Combine overlapping intervals | Sort by start time | LC 56, 57, merging problems |
| **Greedy Template** | Maximum non-overlapping | Sort by end time | LC 435, 452, scheduling optimization |
| **Two Pointer Template** | Intersection/comparison | Sort both lists by start | LC 986, comparing interval lists |
| **Priority Queue Template** | Resource management | Sort by start, heap by end | LC 253, meeting room problems |
| **Binary Search Template** | Calendar/booking | Maintain sorted order | LC 729-732, dynamic interval insertion |

### Universal Interval Template
```python
def solve_interval_problem(intervals):
    """
    Universal template for interval problems
    """
    # Step 1: Handle edge cases
    if not intervals or len(intervals) <= 1:
        return intervals
    
    # Step 2: Sort intervals (by start time or end time based on problem)
    intervals.sort(key=lambda x: x[0])  # Sort by start time
    # intervals.sort(key=lambda x: x[1])  # Sort by end time for greedy problems
    
    # Step 3: Initialize result
    result = []
    
    # Step 4: Process intervals sequentially
    for current in intervals:
        # Step 5: Check overlap condition with last processed interval
        if not result or no_overlap_condition(result[-1], current):
            result.append(current)
        else:
            # Step 6: Handle overlap (merge, count, or remove)
            handle_overlap(result, current)
    
    return result

def no_overlap_condition(prev, curr):
    """Check if two intervals don't overlap"""
    return prev[1] < curr[0]  # prev ends before curr starts

def handle_overlap(result, current):
    """Handle overlapping intervals based on problem type"""
    # For merging: extend the last interval
    result[-1][1] = max(result[-1][1], current[1])
    # For counting: increment counter
    # For removal: choose which interval to keep
```

### Specific Templates

#### Template 1: Interval Merging (LC 56, 57)
```python
def merge_intervals(intervals):
    """
    Merge overlapping intervals
    Time: O(n log n), Space: O(n)
    """
    if not intervals:
        return []
    
    # Sort by start time
    intervals.sort(key=lambda x: x[0])
    merged = [intervals[0]]
    
    for current in intervals[1:]:
        last = merged[-1]
        
        # No overlap: add current interval
        if last[1] < current[0]:
            merged.append(current)
        # Overlap: merge intervals
        else:
            last[1] = max(last[1], current[1])
    
    return merged

# Java version
public int[][] merge(int[][] intervals) {
    if (intervals.length <= 1) return intervals;
    
    Arrays.sort(intervals, (a, b) -> Integer.compare(a[0], b[0]));
    List<int[]> merged = new ArrayList<>();
    
    for (int[] current : intervals) {
        if (merged.isEmpty() || merged.get(merged.size() - 1)[1] < current[0]) {
            merged.add(current);
        } else {
            merged.get(merged.size() - 1)[1] = Math.max(
                merged.get(merged.size() - 1)[1], current[1]
            );
        }
    }
    
    return merged.toArray(new int[merged.size()][]);
}
```

#### Template 2: Greedy Scheduling (LC 435, 452)
```python
def min_intervals_to_remove(intervals):
    """
    Find minimum intervals to remove for non-overlapping set
    Time: O(n log n), Space: O(1)
    """
    if not intervals:
        return 0
    
    # Sort by end time (greedy strategy)
    intervals.sort(key=lambda x: x[1])
    
    count = 0
    prev_end = intervals[0][1]
    
    for i in range(1, len(intervals)):
        # Overlap detected
        if intervals[i][0] < prev_end:
            count += 1  # Remove current interval
        else:
            prev_end = intervals[i][1]  # Update end time
    
    return count

# Java version
public int eraseOverlapIntervals(int[][] intervals) {
    if (intervals.length <= 1) return 0;
    
    Arrays.sort(intervals, (a, b) -> Integer.compare(a[1], b[1]));
    
    int count = 0;
    int prevEnd = intervals[0][1];
    
    for (int i = 1; i < intervals.length; i++) {
        if (intervals[i][0] < prevEnd) {
            count++;
        } else {
            prevEnd = intervals[i][1];
        }
    }
    
    return count;
}
```

#### Template 3: Two Pointer Intersection (LC 986)
```python
def interval_intersection(firstList, secondList):
    """
    Find intersection of two interval lists
    Time: O(m + n), Space: O(min(m, n))
    """
    result = []
    i = j = 0
    
    while i < len(firstList) and j < len(secondList):
        # Find intersection
        start = max(firstList[i][0], secondList[j][0])
        end = min(firstList[i][1], secondList[j][1])
        
        # Valid intersection
        if start <= end:
            result.append([start, end])
        
        # Move pointer of interval that ends first
        if firstList[i][1] < secondList[j][1]:
            i += 1
        else:
            j += 1
    
    return result
```

#### Template 4: Meeting Rooms with Priority Queue (LC 253)
```python
import heapq

def min_meeting_rooms(intervals):
    """
    Find minimum meeting rooms required
    Time: O(n log n), Space: O(n)
    """
    if not intervals:
        return 0
    
    # Sort by start time
    intervals.sort(key=lambda x: x[0])
    
    # Min heap to track end times
    heap = []
    
    for start, end in intervals:
        # If earliest meeting ends before current starts
        if heap and heap[0] <= start:
            heapq.heappop(heap)
        
        # Add current meeting's end time
        heapq.heappush(heap, end)
    
    return len(heap)

# Java version
public int minMeetingRooms(int[][] intervals) {
    if (intervals.length == 0) return 0;
    
    Arrays.sort(intervals, (a, b) -> Integer.compare(a[0], b[0]));
    PriorityQueue<Integer> heap = new PriorityQueue<>();
    
    for (int[] interval : intervals) {
        if (!heap.isEmpty() && heap.peek() <= interval[0]) {
            heap.poll();
        }
        heap.offer(interval[1]);
    }
    
    return heap.size();
}
```

#### Template 5: Calendar Booking (LC 729)
```python
class MyCalendar:
    """
    Calendar with overlap detection using binary search
    Time: O(log n) per booking, Space: O(n)
    """
    def __init__(self):
        self.bookings = []
    
    def book(self, start, end):
        # Binary search for insertion position
        left, right = 0, len(self.bookings)
        
        while left < right:
            mid = (left + right) // 2
            if self.bookings[mid][1] <= start:
                left = mid + 1
            else:
                right = mid
        
        # Check overlap with neighbors
        if left > 0 and self.bookings[left - 1][1] > start:
            return False
        if left < len(self.bookings) and self.bookings[left][0] < end:
            return False
        
        # No overlap, insert booking
        self.bookings.insert(left, [start, end])
        return True
```

## 3) Problems by Pattern

### **Merging Pattern Problems**
| Problem | LC # | Key Technique | Difficulty | Template |
|---------|------|---------------|------------|----------|
| Merge Intervals | 56 | Sort by start, merge overlaps | Medium | Merge Template |
| Insert Interval | 57 | Insert and merge | Medium | Merge Template |
| Summary Ranges | 228 | Consecutive number ranges | Easy | Merge Template |
| Data Stream as Disjoint Intervals | 352 | TreeMap/SortedDict | Hard | Merge Template |
| Merge Similar Items | 2363 | Merge by weight | Easy | Merge Template |

### **Greedy Scheduling Problems**
| Problem | LC # | Key Technique | Difficulty | Template |
|---------|------|---------------|------------|----------|
| Non-overlapping Intervals | 435 | Sort by end, greedy removal | Medium | Greedy Template |
| Minimum Arrows to Burst Balloons | 452 | Sort by end, count arrows | Medium | Greedy Template |
| Maximum Length of Pair Chain | 646 | Sort by second element | Medium | Greedy Template |
| Activity Selection Problem | - | Classic greedy algorithm | Medium | Greedy Template |
| Car Pooling | 1094 | Timeline + capacity | Medium | Greedy Template |

### **Intersection & Coverage Problems**
| Problem | LC # | Key Technique | Difficulty | Template |
|---------|------|---------------|------------|----------|
| Interval List Intersections | 986 | Two pointers | Medium | Two Pointer Template |
| Remove Covered Intervals | 1288 | Sort and filter | Medium | Merge Template |
| Find Right Interval | 436 | Binary search | Medium | Binary Search |
| Employee Free Time | 759 | Merge + find gaps | Hard | Merge Template |
| Video Stitching | 1024 | Greedy coverage | Medium | Greedy Template |

### **Meeting Room & Scheduling Problems**
| Problem | LC # | Key Technique | Difficulty | Template |
|---------|------|---------------|------------|----------|
| Meeting Rooms | 252 | Sort and check conflicts | Easy | Basic Template |
| Meeting Rooms II | 253 | Priority queue | Medium | Priority Queue Template |
| Meeting Scheduler | 1229 | Two pointers + duration | Medium | Two Pointer Template |
| Minimum Time to Make Rope Colorful | 1578 | Consecutive intervals | Medium | Greedy Template |
| Course Schedule III | 630 | Priority queue + greedy | Hard | Priority Queue Template |

### **Calendar & Booking Problems**
| Problem | LC # | Key Technique | Difficulty | Template |
|---------|------|---------------|------------|----------|
| My Calendar I | 729 | Sorted list + binary search | Medium | Calendar Template |
| My Calendar II | 731 | Double booking detection | Medium | Calendar Template |
| My Calendar III | 732 | K-booking with timeline | Hard | Calendar Template |
| Exam Room | 855 | Max gap maintenance | Medium | Binary Search |
| Range Module | 715 | Segment tree/intervals | Hard | Advanced Template |

### **Advanced Interval Problems**
| Problem | LC # | Key Technique | Difficulty | Template |
|---------|------|---------------|------------|----------|
| Falling Squares | 699 | Coordinate compression | Hard | Advanced Template |
| The Skyline Problem | 218 | Sweep line + priority queue | Hard | Advanced Template |
| Rectangle Area II | 850 | Coordinate compression | Hard | Advanced Template |
| Perfect Rectangle | 391 | Area calculation + validation | Hard | Advanced Template |
| Count Integers in Intervals | 2276 | Dynamic intervals | Hard | Advanced Template |

## 4) Pattern Selection Strategy

### Decision Framework Flowchart

```
Problem Analysis for Interval Problems:

1. Are you merging overlapping intervals?
   ├── YES → Use Merge Template (LC 56, 57)
   │   ├── Single interval insertion? → Insert Interval Template
   │   └── Multiple overlaps? → Standard Merge Template
   └── NO → Continue to 2

2. Are you finding maximum non-overlapping intervals?
   ├── YES → Use Greedy Template (LC 435, 452)
   │   ├── Sort by end time
   │   └── Greedy selection strategy
   └── NO → Continue to 3

3. Are you finding intersections between interval lists?
   ├── YES → Use Two Pointer Template (LC 986)
   │   ├── Two sorted lists? → Standard Two Pointer
   │   └── Multiple lists? → Merge then process
   └── NO → Continue to 4

4. Are you managing meeting rooms or resources?
   ├── YES → Use Priority Queue Template (LC 253)
   │   ├── Count resources needed? → Min heap approach
   │   └── Check availability? → Sort + scan
   └── NO → Continue to 5

5. Are you handling dynamic bookings/calendar?
   ├── YES → Use Calendar Template (LC 729-732)
   │   ├── Single booking? → Binary search insertion
   │   ├── Double booking allowed? → Two lists approach
   │   └── K-booking? → Timeline/sweep line
   └── NO → Consider Advanced Templates

6. Advanced cases (Skyline, Rectangles, etc.)
   ├── Coordinate compression needed?
   ├── Sweep line algorithm required?
   └── Segment tree for range operations?
```

### Template Selection Guide

**Quick Decision Tree:**
1. **Overlap Detection**: `prev[1] >= curr[0]` (assuming sorted by start)
2. **Merge Strategy**: Extend `prev[1] = max(prev[1], curr[1])`
3. **Greedy Strategy**: Sort by end time, keep earliest ending
4. **Resource Management**: Use min heap for end times
5. **Dynamic Insertion**: Maintain sorted order with binary search

## 5) Key Patterns & Overlap Detection

### Overlap Detection Methods

#### Method 1: After Sorting by Start Time
```python
def has_overlap(interval1, interval2):
    """Check if two intervals overlap (sorted by start)"""
    return interval1[1] > interval2[0]
```

#### Method 2: General Case (Any Order)
```python
def has_overlap(interval1, interval2):
    """Check if two intervals overlap (any order)"""
    start1, end1 = interval1
    start2, end2 = interval2
    return start1 < end2 and start2 < end1
```

### Overlap Visualization
```
Case 1 - No Overlap:
|----| interval1
        |----| interval2

Case 2 - Overlap:
|-------|
    |-------|

Case 3 - Complete Overlap:
|-----------|
   |-----|
```

### Common Interval Operations

```python
def merge_two_intervals(a, b):
    """Merge two overlapping intervals"""
    return [min(a[0], b[0]), max(a[1], b[1])]

def interval_length(interval):
    """Calculate interval length"""
    return interval[1] - interval[0]

def intervals_intersection(a, b):
    """Find intersection of two intervals"""
    start = max(a[0], b[0])
    end = min(a[1], b[1])
    return [start, end] if start <= end else None

def point_in_interval(point, interval):
    """Check if point is in interval"""
    return interval[0] <= point <= interval[1]
```

## 6) Summary & Quick Reference

### Complexity Quick Reference
| Operation | Time | Space | Notes |
|-----------|------|-------|-------|
| Sort intervals | O(n log n) | O(1) | Essential first step |
| Merge overlapping | O(n) | O(n) | After sorting |
| Find intersections | O(m + n) | O(min(m,n)) | Two pointer approach |
| Meeting rooms | O(n log n) | O(n) | Priority queue for end times |
| Calendar booking | O(log n) | O(n) | Binary search per insertion |
| Greedy scheduling | O(n log n) | O(1) | Sort by end time |

### Template Quick Reference
| Template | Pattern | Key Code |
|----------|---------|----------|
| **Merge** | Overlapping intervals | `if last[1] < curr[0]: append else: merge` |
| **Greedy** | Non-overlapping max | `sort(key=end); if curr[0] >= prev[1]: count++` |
| **Two Pointer** | List intersection | `start=max(starts), end=min(ends)` |
| **Priority Queue** | Resource management | `heappush(end_time); if heap[0] <= start: heappop` |
| **Binary Search** | Dynamic insertion | `bisect.insort` or custom binary search |

### Common Patterns & Tricks

#### **Pattern 1: Merge Overlapping**
```python
# Standard merging after sorting
intervals.sort()
merged = [intervals[0]]
for curr in intervals[1:]:
    if merged[-1][1] < curr[0]:
        merged.append(curr)
    else:
        merged[-1][1] = max(merged[-1][1], curr[1])
```

#### **Pattern 2: Greedy Selection**
```python
# Sort by end time for optimal selection
intervals.sort(key=lambda x: x[1])
count = 1
prev_end = intervals[0][1]
for start, end in intervals[1:]:
    if start >= prev_end:
        count += 1
        prev_end = end
```

#### **Pattern 3: Timeline Events**
```python
# Convert intervals to events for sweep line
events = []
for start, end in intervals:
    events.append((start, 1))    # start event
    events.append((end, -1))     # end event
events.sort()
```

### Problem-Solving Steps
1. **Identify Pattern**: Merging, scheduling, intersection, or resource management?
2. **Choose Sorting Strategy**: By start time (merging) or end time (greedy)
3. **Select Template**: Use appropriate template from above
4. **Handle Edge Cases**: Empty arrays, single intervals, identical intervals
5. **Optimize**: Consider space optimization for large datasets

### Common Mistakes & Tips

**🚫 Common Mistakes:**
- **Wrong sorting order**: Sorting by start when should sort by end (greedy problems)
- **Off-by-one errors**: Using `<=` vs `<` in overlap conditions
- **Edge case handling**: Not checking empty arrays or single intervals  
- **Merge logic errors**: Forgetting to update both start and end during merge
- **Greedy strategy confusion**: Not understanding why sorting by end time works
- **Space complexity**: Creating unnecessary intermediate data structures

**✅ Best Practices:**
- **Always sort first**: Most interval problems require sorted input
- **Clear overlap definition**: Define overlap condition clearly before coding
- **Use appropriate template**: Match template to problem pattern
- **Test edge cases**: Empty input, single interval, identical intervals
- **Visualize examples**: Draw intervals to understand overlap patterns
- **Choose right sorting key**: Start time for merging, end time for greedy

### Interview Tips
1. **Start with examples**: Draw intervals on paper to visualize
2. **Clarify edge cases**: What about empty intervals? Point intervals?
3. **Explain sorting choice**: Why sorting by start/end time?
4. **Walk through algorithm**: Show merge/greedy logic step by step
5. **Optimize incrementally**: Start with working solution, then optimize
6. **Practice common patterns**: Master the 5 main templates above
7. **Time complexity analysis**: Always explain O(n log n) sorting + O(n) processing

### Data Structure Conversion Tricks

#### List to Array (Java)
```java
List<int[]> result = new ArrayList<>();
// ... populate result
return result.toArray(new int[result.size()][]);
```

#### Efficient Merging in Python
```python
# Using list comprehension for functional style
def merge_intervals(intervals):
    intervals.sort()
    result = [intervals[0]]
    [result.append(curr) if result[-1][1] < curr[0] 
     else result[-1].__setitem__(1, max(result[-1][1], curr[1]))
     for curr in intervals[1:]]
    return result
```

### Related Topics
- **Greedy Algorithms**: Interval scheduling optimization
- **Binary Search**: Calendar booking and insertion problems  
- **Priority Queue**: Meeting room and resource management
- **Two Pointers**: Intersection and comparison problems
- **Sweep Line**: Advanced problems like skyline and rectangles
- **Segment Trees**: Range updates and queries on intervals

## LC Examples

### 2-1) Merge Intervals (LC 56) — Sort + Merge
> Sort by start time; merge overlapping intervals by comparing with last merged.

```java
// LC 56 - Merge Intervals
// IDEA: Sort by start, merge when current.start <= last.end
// time = O(N log N), space = O(N)
public int[][] merge(int[][] intervals) {
    Arrays.sort(intervals, (a, b) -> a[0] - b[0]);
    List<int[]> merged = new ArrayList<>();
    for (int[] interval : intervals) {
        if (merged.isEmpty() || merged.get(merged.size()-1)[1] < interval[0]) {
            merged.add(interval);
        } else {
            merged.get(merged.size()-1)[1] = Math.max(merged.get(merged.size()-1)[1], interval[1]);
        }
    }
    return merged.toArray(new int[merged.size()][]);
}
```

### 2-2) Non-overlapping Intervals (LC 435) — Greedy Interval Scheduling
> Sort by end time; greedily keep intervals that end earliest to minimize removals.

```java
// LC 435 - Non-overlapping Intervals
// IDEA: Greedy — sort by end, count overlapping intervals to remove
// time = O(N log N), space = O(1)
public int eraseOverlapIntervals(int[][] intervals) {
    Arrays.sort(intervals, (a, b) -> a[1] - b[1]);
    int removals = 0, prevEnd = Integer.MIN_VALUE;
    for (int[] interval : intervals) {
        if (interval[0] < prevEnd) {
            removals++;   // overlap: remove current (keep the one ending earlier)
        } else {
            prevEnd = interval[1];
        }
    }
    return removals;
}
```

### 2-3) Insert Interval (LC 57) — Linear Scan + Merge
> Insert new interval and merge all overlapping intervals in one pass.

```java
// LC 57 - Insert Interval
// IDEA: Three phases — add non-overlapping left, merge overlapping, add right
// time = O(N), space = O(N)
public int[][] insert(int[][] intervals, int[] newInterval) {
    List<int[]> result = new ArrayList<>();
    int i = 0, n = intervals.length;
    // Phase 1: add all intervals that end before newInterval starts
    while (i < n && intervals[i][1] < newInterval[0]) result.add(intervals[i++]);
    // Phase 2: merge overlapping intervals
    while (i < n && intervals[i][0] <= newInterval[1]) {
        newInterval[0] = Math.min(newInterval[0], intervals[i][0]);
        newInterval[1] = Math.max(newInterval[1], intervals[i][1]);
        i++;
    }
    result.add(newInterval);
    // Phase 3: add remaining intervals
    while (i < n) result.add(intervals[i++]);
    return result.toArray(new int[result.size()][]);
}
```

### 2-4) Meeting Rooms II (LC 253) — Min-Heap on End Times
> Sort by start; heap tracks earliest ending room — reuse if room ends before next meeting.

```java
// LC 253 - Meeting Rooms II
// IDEA: Sort by start; min-heap of end times — reuse room if heap.peek() <= start
// time = O(N log N), space = O(N)
public int minMeetingRooms(int[][] intervals) {
    Arrays.sort(intervals, (a, b) -> a[0] - b[0]);
    PriorityQueue<Integer> heap = new PriorityQueue<>();
    for (int[] iv : intervals) {
        if (!heap.isEmpty() && heap.peek() <= iv[0]) heap.poll();
        heap.offer(iv[1]);
    }
    return heap.size();
}
```

### 2-5) Minimum Number of Arrows to Burst Balloons (LC 452) — Greedy
> Sort by end; one arrow at interval's end bursts all overlapping; advance when gap appears.

```java
// LC 452 - Minimum Number of Arrows to Burst Balloons
// IDEA: Greedy — sort by end; new arrow only when next start > current end
// time = O(N log N), space = O(1)
public int findMinArrowShots(int[][] points) {
    Arrays.sort(points, (a, b) -> Integer.compare(a[1], b[1]));
    int arrows = 1, end = points[0][1];
    for (int i = 1; i < points.length; i++)
        if (points[i][0] > end) { arrows++; end = points[i][1]; }
    return arrows;
}
```

### 2-6) Non-Overlapping Intervals (LC 435) — Greedy
> Sort by end; keep interval with earliest end greedily; count how many must be removed.

```java
// LC 435 - Non-Overlapping Intervals
// IDEA: Greedy — sort by end; skip (remove) overlapping intervals
// time = O(N log N), space = O(1)
public int eraseOverlapIntervals(int[][] intervals) {
    Arrays.sort(intervals, (a, b) -> a[1] - b[1]);
    int removals = 0, end = Integer.MIN_VALUE;
    for (int[] iv : intervals) {
        if (iv[0] >= end) end = iv[1];
        else removals++;
    }
    return removals;
}
```

### 2-7) Interval List Intersections (LC 986) — Two Pointers
> Advance the pointer whose interval ends first; record overlap when ranges intersect.

```java
// LC 986 - Interval List Intersections
// IDEA: Two pointers — compute intersection, advance pointer with smaller end
// time = O(M+N), space = O(M+N)
public int[][] intervalIntersection(int[][] A, int[][] B) {
    List<int[]> res = new ArrayList<>();
    int i = 0, j = 0;
    while (i < A.length && j < B.length) {
        int lo = Math.max(A[i][0], B[j][0]);
        int hi = Math.min(A[i][1], B[j][1]);
        if (lo <= hi) res.add(new int[]{lo, hi});
        if (A[i][1] < B[j][1]) i++;
        else j++;
    }
    return res.toArray(new int[res.size()][]);
}
```

### 2-8) Remove Covered Intervals (LC 1288) — Sort + Greedy
> Sort by start asc, end desc; interval is covered if its end ≤ current max end.

```java
// LC 1288 - Remove Covered Intervals
// IDEA: Sort start ASC, end DESC; count intervals not covered by running maxEnd
// time = O(N log N), space = O(1)
public int removeCoveredIntervals(int[][] intervals) {
    Arrays.sort(intervals, (a, b) -> a[0] != b[0] ? a[0] - b[0] : b[1] - a[1]);
    int count = 0, maxEnd = 0;
    for (int[] iv : intervals)
        if (iv[1] > maxEnd) { count++; maxEnd = iv[1]; }
    return count;
}
```

### 2-9) Video Stitching (LC 1024) — Greedy Interval Cover
> Sort by start; at each frontier pick the clip extending coverage the furthest.

```java
// LC 1024 - Video Stitching
// IDEA: Greedy — at current end, pick clip reaching farthest next position
// time = O(N log N), space = O(1)
public int videoStitching(int[][] clips, int time) {
    Arrays.sort(clips, (a, b) -> a[0] - b[0]);
    int count = 0, curEnd = 0, farthest = 0, i = 0;
    while (i < clips.length && curEnd < time) {
        while (i < clips.length && clips[i][0] <= curEnd)
            farthest = Math.max(farthest, clips[i++][1]);
        if (farthest == curEnd) return -1;
        curEnd = farthest;
        count++;
    }
    return curEnd >= time ? count : -1;
}
```

### 2-10) Maximum Profit in Job Scheduling (LC 1235) — DP + Binary Search
> Sort jobs by end; dp[i] = max profit using first i jobs; binary search for last non-conflicting job.

```java
// LC 1235 - Maximum Profit in Job Scheduling
// IDEA: Sort by end; DP + binary search for latest non-overlapping job
// time = O(N log N), space = O(N)
public int jobScheduling(int[] startTime, int[] endTime, int[] profit) {
    int n = startTime.length;
    int[][] jobs = new int[n][3];
    for (int i = 0; i < n; i++) jobs[i] = new int[]{endTime[i], startTime[i], profit[i]};
    Arrays.sort(jobs, (a, b) -> a[0] - b[0]);
    int[] dp = new int[n + 1];
    for (int i = 0; i < n; i++) {
        int lo = 0, hi = i;
        while (lo < hi) {
            int mid = (lo + hi + 1) / 2;
            if (jobs[mid-1][0] <= jobs[i][1]) lo = mid;
            else hi = mid - 1;
        }
        dp[i+1] = Math.max(dp[i], dp[lo] + jobs[i][2]);
    }
    return dp[n];
}
```

### 2-11) My Calendar I (LC 729) — TreeMap Overlap Check
> TreeMap floor/ceiling gives O(log N) overlap detection per booking.

```java
// LC 729 - My Calendar I
// IDEA: TreeMap — O(log N) overlap check with floorKey / ceilingKey
// time = O(log N) per booking, space = O(N)
class MyCalendar {
    TreeMap<Integer, Integer> cal = new TreeMap<>();
    public boolean book(int start, int end) {
        Integer prev = cal.floorKey(start), next = cal.ceilingKey(start);
        if ((prev == null || cal.get(prev) <= start) && (next == null || next >= end)) {
            cal.put(start, end);
            return true;
        }
        return false;
    }
}
```

### 2-12) Meeting Rooms I (LC 252) — Sort + Adjacent Check
> Sort by start time; if any meeting starts before previous ends, overlap exists.

```java
// LC 252 - Meeting Rooms
// IDEA: Sort by start; adjacent overlap check
// time = O(N log N), space = O(1)
public boolean canAttendMeetings(int[][] intervals) {
    Arrays.sort(intervals, (a, b) -> a[0] - b[0]);
    for (int i = 1; i < intervals.length; i++)
        if (intervals[i][0] < intervals[i-1][1]) return false;
    return true;
}
```