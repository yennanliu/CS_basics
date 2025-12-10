---
layout: cheatsheet
title: "Difference Array"
description: "Efficient range update technique using difference arrays"
category: "Technique"
difficulty: "Medium"
tags: ["difference-array", "range-update", "prefix-sum", "optimization"]
patterns:
  - Range updates
  - Batch processing
  - Array reconstruction
---

# Difference Array

## Overview
**Difference Array** is a technique for efficiently performing range update operations on an array. Instead of updating each element individually (O(n) per update), we can perform range updates in O(1) time and reconstruct the final array in O(n) time.

### Key Properties
- **Time Complexity**: 
  - Range Update: O(1)
  - Build Difference Array: O(n)
  - Reconstruct Original: O(n)
  - Multiple Updates: O(m) for m updates
- **Space Complexity**: O(n) for storing difference array
- **Core Idea**: Store differences between consecutive elements to enable efficient range updates
- **When to Use**: Multiple range update operations, interval modifications, booking systems, resource allocation

### References
- [Difference Array Visualization](https://www.geeksforgeeks.org/difference-array-range-update-query-o1/)
- [fucking algorithm - Difference Array](https://labuladong.online/algo/data-structure/diff-array/)
- [Prefix Sum vs Difference Array](https://leetcode.com/discuss/general-discussion/1093346/)

## Problem Categories

### **Pattern 1: Basic Range Updates**
- **Description**: Add/subtract a value to all elements in a range
- **Recognition**: "Update range [i, j]", "increment by val", "modify interval"
- **Examples**: LC 370, LC 1109, LC 1893
- **Template**: Use Basic Difference Array Template

### **Pattern 2: Resource Allocation**
- **Description**: Track resource usage across intervals
- **Recognition**: "Booking", "capacity", "overlapping intervals"
- **Examples**: LC 1094, LC 731, LC 732
- **Template**: Use Resource Tracking Template

### **Pattern 3: Event Timeline**
- **Description**: Process events happening at different times
- **Recognition**: "Start/end times", "scheduling", "timeline"
- **Examples**: LC 253, LC 1851, LC 2021
- **Template**: Use Event Processing Template

### **Pattern 4: 2D Difference Array**
- **Description**: Range updates on 2D matrices
- **Recognition**: "Rectangle updates", "2D range modifications"
- **Examples**: LC 2132, LC 2536
- **Template**: Use 2D Difference Array Template

## Templates & Algorithms

### Template Comparison Table
| Template Type | Use Case | Update Time | Query Time | Space | When to Use |
|---------------|----------|-------------|------------|-------|-------------|
| **Basic Difference** | Range updates | O(1) | O(n) rebuild | O(n) | Multiple range updates |
| **With Prefix Sum** | Range updates + queries | O(1) | O(1) | O(n) | Updates and queries |
| **2D Difference** | Matrix range updates | O(1) | O(mn) rebuild | O(mn) | 2D range updates |
| **Lazy Propagation** | Dynamic queries | O(log n) | O(log n) | O(n) | Many queries between updates |

### Universal Difference Array Template
```python
def difference_array_template(nums, updates):
    """
    Universal template for difference array problems
    nums: original array
    updates: list of [start, end, value] operations
    """
    n = len(nums)
    # Build difference array
    diff = [0] * n
    diff[0] = nums[0]
    for i in range(1, n):
        diff[i] = nums[i] - nums[i-1]
    
    # Apply range updates in O(1) each
    for start, end, val in updates:
        diff[start] += val
        if end + 1 < n:
            diff[end + 1] -= val
    
    # Reconstruct final array
    result = [0] * n
    result[0] = diff[0]
    for i in range(1, n):
        result[i] = result[i-1] + diff[i]
    
    return result
```

### Template 1: Basic Difference Array
```python
class DifferenceArray:
    def __init__(self, nums):
        """Initialize difference array from original array"""
        self.n = len(nums)
        self.diff = [0] * self.n
        
        # Build difference array
        self.diff[0] = nums[0]
        for i in range(1, self.n):
            self.diff[i] = nums[i] - nums[i-1]
    
    def update(self, start, end, val):
        """Add val to all elements in range [start, end] in O(1)"""
        self.diff[start] += val
        if end + 1 < self.n:
            self.diff[end + 1] -= val
    
    def get_result(self):
        """Reconstruct the final array in O(n)"""
        result = [0] * self.n
        result[0] = self.diff[0]
        for i in range(1, self.n):
            result[i] = result[i-1] + self.diff[i]
        return result
```

### Template 2: Resource Allocation
```python
def check_resource_allocation(intervals, capacity, resource_field=2):
    """
    Check if resource allocation is valid
    intervals: [[start, end, resource_needed], ...]
    capacity: maximum available resource
    """
    # Find the range of positions
    max_pos = max(interval[1] for interval in intervals) + 1
    diff = [0] * max_pos
    
    # Apply all resource allocations
    for interval in intervals:
        start, end, resource = interval[0], interval[1], interval[resource_field]
        diff[start] += resource
        if end + 1 < max_pos:
            diff[end + 1] -= resource
    
    # Check if any position exceeds capacity
    current = 0
    for i in range(max_pos):
        current += diff[i]
        if current > capacity:
            return False
    
    return True
```

### Template 3: Event Timeline
```python
def process_events(events):
    """
    Process events on a timeline
    events: [[start_time, end_time, value], ...]
    Returns: timeline with accumulated values
    """
    if not events:
        return []
    
    # Create timeline
    max_time = max(e[1] for e in events) + 1
    timeline = [0] * max_time
    
    # Process each event
    for start, end, value in events:
        timeline[start] += value
        if end + 1 < max_time:
            timeline[end + 1] -= value
    
    # Calculate prefix sum to get actual values
    for i in range(1, max_time):
        timeline[i] += timeline[i-1]
    
    return timeline
```

### Template 4: 2D Difference Array
```python
class DifferenceArray2D:
    def __init__(self, matrix):
        """Initialize 2D difference array"""
        self.m, self.n = len(matrix), len(matrix[0])
        self.diff = [[0] * self.n for _ in range(self.m)]
        
        # Build 2D difference array
        for i in range(self.m):
            for j in range(self.n):
                self.diff[i][j] = matrix[i][j]
                if i > 0:
                    self.diff[i][j] -= matrix[i-1][j]
                if j > 0:
                    self.diff[i][j] -= matrix[i][j-1]
                if i > 0 and j > 0:
                    self.diff[i][j] += matrix[i-1][j-1]
    
    def update(self, r1, c1, r2, c2, val):
        """Add val to all elements in rectangle [r1,c1] to [r2,c2]"""
        self.diff[r1][c1] += val
        if r2 + 1 < self.m:
            self.diff[r2 + 1][c1] -= val
        if c2 + 1 < self.n:
            self.diff[r1][c2 + 1] -= val
        if r2 + 1 < self.m and c2 + 1 < self.n:
            self.diff[r2 + 1][c2 + 1] += val
    
    def get_result(self):
        """Reconstruct the final 2D array"""
        result = [[0] * self.n for _ in range(self.m)]
        
        for i in range(self.m):
            for j in range(self.n):
                result[i][j] = self.diff[i][j]
                if i > 0:
                    result[i][j] += result[i-1][j]
                if j > 0:
                    result[i][j] += result[i][j-1]
                if i > 0 and j > 0:
                    result[i][j] -= result[i-1][j-1]
        
        return result
```

### Template 5: Optimized with Coordinates Compression
```python
def difference_array_compressed(updates):
    """
    Handle large coordinate space with compression
    updates: [[start, end, value], ...]
    """
    # Collect all unique points
    points = set()
    for start, end, _ in updates:
        points.add(start)
        points.add(end + 1)
    
    # Sort and create mapping
    sorted_points = sorted(points)
    point_to_idx = {p: i for i, p in enumerate(sorted_points)}
    
    # Apply updates on compressed coordinates
    n = len(sorted_points)
    diff = [0] * n
    
    for start, end, val in updates:
        start_idx = point_to_idx[start]
        end_idx = point_to_idx.get(end + 1, n)
        diff[start_idx] += val
        if end_idx < n:
            diff[end_idx] -= val
    
    # Calculate values at each point
    for i in range(1, n):
        diff[i] += diff[i-1]
    
    # Return results with original coordinates
    result = {}
    for i, point in enumerate(sorted_points[:-1]):  # Exclude the last dummy point
        if diff[i] != 0:
            result[point] = diff[i]
    
    return result
```

## 1) General form

```java

// java
// https://labuladong.online/algo/data-structure/diff-array/

// 差分数组工具类
// V1
class Difference {
    // 差分数组
    private int[] diff;
    
    // 输入一个初始数组，区间操作将在这个数组上进行
    public Difference(int[] nums) {
        assert nums.length > 0;
        diff = new int[nums.length];
        // 根据初始数组构造差分数组
        diff[0] = nums[0];
        for (int i = 1; i < nums.length; i++) {
            diff[i] = nums[i] - nums[i - 1];
        }
    }

    // 给闭区间 [i, j] 增加 val（可以是负数）
    public void increment(int i, int j, int val) {
        diff[i] += val;
        if (j + 1 < diff.length) {
            diff[j + 1] -= val;
        }
    }

    // 返回结果数组
    public int[] result() {
        int[] res = new int[diff.length];
        // 根据差分数组构造结果数组
        res[0] = diff[0];
        for (int i = 1; i < diff.length; i++) {
            res[i] = res[i - 1] + diff[i];
        }
        return res;
    }
}
```

```java
// V2
// https://github.com/yennanliu/CS_basics/blob/master/leetcode_java/src/main/java/AlgorithmJava/DifferenceArray.java


// method
public int[] getDifferenceArray(int[][] input, int n) {

/** LC 1109. Corporate Flight Bookings input : [start, end, seats]
 *
 *  NOTE !!!
 *
 *   in java, index start from 0;
 *   but in LC 1109, index start from 1
 *
 */
int[] tmp = new int[n + 1];
for (int[] x : input) {
  int start = x[0];
  int end = x[1];
  int seats = x[2];

  // add
  tmp[start] += seats;

  // subtract
  if (end + 1 <= n) {
    tmp[end + 1] -= seats;
  }
}

for (int i = 1; i < tmp.length; i++) {
  //tmp[i] = tmp[i - 1] + tmp[i];
    tmp[i] += tmp[i - 1];
}

return Arrays.copyOfRange(tmp, 1, n+1);
}
```

## Problems by Pattern

### Pattern-Based Problem Classification

#### **Pattern 1: Basic Range Update Problems**
| Problem | LC # | Difficulty | Key Technique | Template |
|---------|------|------------|---------------|----------|
| Range Addition | 370 | Medium | Basic difference array | Template 1 |
| Corporate Flight Bookings | 1109 | Medium | Range updates | Template 1 |
| Range Addition II | 598 | Easy | Minimum overlap | Template 1 |
| Apply Operations to Make All Array Elements Equal to Zero | 2772 | Medium | Range updates | Template 1 |

#### **Pattern 2: Resource Allocation Problems**  
| Problem | LC # | Difficulty | Key Technique | Template |
|---------|------|------------|---------------|----------|
| Car Pooling | 1094 | Medium | Capacity check | Template 2 |
| Meeting Rooms II | 253 | Medium | Timeline events | Template 2 |
| My Calendar I | 729 | Medium | Interval booking | Template 2 |
| My Calendar II | 731 | Medium | Double booking | Template 2 |
| My Calendar III | 732 | Hard | K-booking | Template 2 |

#### **Pattern 3: Event Timeline Problems**
| Problem | LC # | Difficulty | Key Technique | Template |
|---------|------|------------|---------------|----------|
| Number of Flowers in Full Bloom | 2251 | Hard | Timeline query | Template 3 |
| Describe the Painting | 2158 | Medium | Color mixing | Template 3 |
| Maximum Population Year | 1854 | Easy | Timeline count | Template 3 |
| Count Positions on Street With Required Brightness | 2021 | Medium | Light coverage | Template 3 |

#### **Pattern 4: 2D Difference Array Problems**
| Problem | LC # | Difficulty | Key Technique | Template |
|---------|------|------------|---------------|----------|
| Stamping the Grid | 2132 | Hard | 2D range update | Template 4 |
| Increment Submatrices by One | 2536 | Medium | Rectangle updates | Template 4 |

### Complete Problem List by Difficulty

#### Easy Problems (Foundation)
- LC 598: Range Addition II - Find minimum affected area
- LC 1854: Maximum Population Year - Simple timeline
- LC 1893: Check if All Integers in Range Are Covered - Range coverage

#### Medium Problems (Core)
- LC 370: Range Addition - Classic difference array
- LC 1109: Corporate Flight Bookings - Flight seat allocation
- LC 1094: Car Pooling - Resource capacity validation
- LC 253: Meeting Rooms II - Minimum rooms needed
- LC 729: My Calendar I - No double booking
- LC 731: My Calendar II - Allow one double booking
- LC 2021: Street Light Brightness - Range illumination
- LC 2158: Amount of New Area Painted - Color segments
- LC 2536: Increment Submatrices by One - 2D updates
- LC 2772: Apply Operations to Array - Make all zeros

#### Hard Problems (Advanced)
- LC 732: My Calendar III - Maximum K-booking
- LC 2132: Stamping the Grid - 2D stamp validation
- LC 2251: Number of Flowers in Full Bloom - Point queries on timeline

### 1-1) Basic OP

## 2) LC Example

### 2-1) Range Addition

```java
// java
// LC 370


// V0
// IDEA : DIFFERENCE ARRAY
public static int[] getModifiedArray(int length, int[][] updates) {

int[] tmp = new int[length + 1]; // or new int[length]; both works
for (int[] x : updates) {
  int start = x[0];
  int end = x[1];
  int amount = x[2];

  // add
  tmp[start] += amount;

  // subtract (remove the "adding affect" on "NEXT" element)
  /**
   * NOTE !!!
   *
   * <p>we remove the "adding affect" on NEXT element (e.g. end + 1)
   */
  if (end + 1 < length) { // NOTE !!! use `end + 1`
    tmp[end + 1] -= amount;
  }
}

// prepare final result
for (int i = 1; i < tmp.length; i++) {
  tmp[i] += tmp[i - 1];
}

return Arrays.copyOfRange(tmp, 0, length); // return the sub array between 0, lengh index
}

// V1
class Solution {
    public int[] getModifiedArray(int length, int[][] updates) {
        // nums 初始化为全 0
        int[] nums = new int[length];
        // 构造差分解法
        Difference df = new Difference(nums);
        
        for (int[] update : updates) {
            int i = update[0];
            int j = update[1];
            int val = update[2];
            df.increment(i, j, val);
        }
        
        return df.result();
    }
}
```

### 2-2) Corporate Flight Bookings

```java
// java
// LC 1109

// V1
class Solution {
    public int[] corpFlightBookings(int[][] bookings, int n) {
        // nums 初始化为全 0
        int[] nums = new int[n];
        // 构造差分解法
        Difference df = new Difference(nums);

        for (int[] booking : bookings) {
            // 注意转成数组索引要减一哦
            int i = booking[0] - 1;
            int j = booking[1] - 1;
            int val = booking[2];
            // 对区间 nums[i..j] 增加 val
            df.increment(i, j, val);
        }
        // 返回最终的结果数组
        return df.result();
    }
}
```

### 2-3) Car Pooling

```java
// java
// LC 1094
// https://leetcode.com/problems/car-pooling/description/

class Solution {
    public boolean carPooling(int[][] trips, int capacity) {
        // 最多有 1001 个车站
        int[] nums = new int[1001];

        // 构造差分解法
        Difference df = new Difference(nums);

        for (int[] trip : trips) {
            // 乘客数量
            int val = trip[0];

            // 第 trip[1] 站乘客上车
            int i = trip[1];

            // 第 trip[2] 站乘客已经下车，
            // 即乘客在车上的区间是 [trip[1], trip[2] - 1]
            int j = trip[2] - 1;

            // 进行区间操作
            df.increment(i, j, val);
        }

        int[] res = df.result();

        // 客车自始至终都不应该超载
        for (int i = 0; i < res.length; i++) {
            if (capacity < res[i]) {
                return false;
            }
        }
        return true;
    }
}
```

## Pattern Selection Strategy

```
Difference Array Problem Analysis Flowchart:

1. Does the problem involve range updates?
   ├── YES → Check update pattern
   │   ├── Multiple ranges need same update? → Use Difference Array
   │   ├── Single element updates? → Use direct array
   │   └── Need immediate query results? → Consider Segment Tree
   └── NO → Not a difference array problem

2. What type of range operation?
   ├── Add/Subtract constant to range → Basic Difference Array (Template 1)
   ├── Track resource usage → Resource Allocation (Template 2)
   ├── Timeline/Event processing → Event Timeline (Template 3)
   └── 2D matrix updates → 2D Difference Array (Template 4)

3. Space/Time Trade-offs:
   ├── Large coordinate space? → Use Coordinate Compression (Template 5)
   ├── Many queries between updates? → Consider Lazy Propagation
   └── Only final result needed? → Basic Difference Array

4. Special Considerations:
   ├── Online vs Offline → Difference array is offline
   ├── Need range queries? → Combine with Prefix Sum
   └── Overlapping intervals? → Check maximum overlap
```

### Decision Framework
1. **Identify range updates**: Look for "update range [l, r]" operations
2. **Count operations**: Multiple updates = good for difference array
3. **Check query pattern**: Final result only vs intermediate queries
4. **Consider alternatives**: Segment tree for dynamic queries

## Summary & Quick Reference

### Complexity Quick Reference
| Operation | Time Complexity | Space Complexity | Notes |
|-----------|-----------------|------------------|-------|
| Build Difference Array | O(n) | O(n) | From original array |
| Range Update | O(1) | O(1) | Add value to range |
| Reconstruct Array | O(n) | O(1) | Get final result |
| M Updates + Reconstruct | O(m + n) | O(n) | Total complexity |
| 2D Range Update | O(1) | O(1) | Rectangle update |
| 2D Reconstruct | O(mn) | O(1) | Get final matrix |

### Template Quick Reference
| Template | Best For | Avoid When | Key Pattern |
|----------|----------|------------|-------------|
| Basic Difference | Multiple range updates | Single updates | diff[i] = arr[i] - arr[i-1] |
| Resource Allocation | Capacity constraints | Unbounded resources | Check max usage |
| Event Timeline | Time-based events | Spatial problems | Timeline array |
| 2D Difference | Matrix range updates | 1D problems | 4-point update |
| Coordinate Compression | Large sparse space | Dense arrays | Map coordinates |

### Common Patterns & Tricks

#### **Pattern: Range Update Formula**
```python
# To add val to range [start, end]:
diff[start] += val
if end + 1 < n:
    diff[end + 1] -= val
```

#### **Pattern: Off-by-One for Intervals**
```python
# If passengers get off at station x, they're on board [start, x-1]
# If event ends at time t, it's active [start, t]
# Be careful with inclusive/exclusive boundaries!

# Car pooling example:
for passengers, pickup, dropoff in trips:
    diff[pickup] += passengers
    diff[dropoff] -= passengers  # Not dropoff-1!
```

#### **Pattern: Maximum Concurrent Events**
```python
def max_concurrent(intervals):
    events = []
    for start, end in intervals:
        events.append((start, 1))   # Start event
        events.append((end + 1, -1)) # End event
    
    events.sort()
    max_concurrent = current = 0
    
    for time, delta in events:
        current += delta
        max_concurrent = max(max_concurrent, current)
    
    return max_concurrent
```

### Problem-Solving Steps
1. **Identify range operations**: Look for [start, end] updates
2. **Initialize difference array**: Usually all zeros
3. **Apply updates**: O(1) per update using formula
4. **Reconstruct if needed**: Prefix sum to get final array
5. **Validate constraints**: Check capacity, overlaps, etc.

### Common Mistakes & Tips

**🚫 Common Mistakes:**
- **Off-by-one errors**: Careful with inclusive/exclusive ranges
- **Array bounds**: Check end+1 before updating
- **Initial values**: Don't forget original array values
- **2D formula errors**: Four points need correct signs
- **Overflow**: Large values × many updates

**✅ Best Practices:**
- **Use clear variable names**: `start/end` not `i/j`
- **Comment boundary logic**: Explain inclusive/exclusive
- **Test edge cases**: Empty ranges, full array updates
- **Consider compression**: For large coordinate spaces
- **Validate early**: Check impossible cases first

### Interview Tips
1. **Recognize the pattern**: "Update multiple ranges" → Difference array
2. **Explain the technique**: "Convert range updates to point updates"
3. **Mention trade-offs**: Offline processing, O(n) reconstruction
4. **Know alternatives**: Segment tree for online queries
5. **Handle edge cases**: Empty input, single element, overlapping ranges

### Related Topics
- **Prefix Sum**: Opposite operation, range queries
- **Segment Tree**: When need both updates and queries
- **Fenwick Tree (BIT)**: Alternative for range operations
- **Sweep Line**: Related technique for interval problems
- **Coordinate Compression**: Handling large sparse ranges

### Java Implementation Notes
```java
// Java Difference Array Class
class Difference {
    private int[] diff;
    
    public Difference(int[] nums) {
        diff = new int[nums.length];
        diff[0] = nums[0];
        for (int i = 1; i < nums.length; i++) {
            diff[i] = nums[i] - nums[i-1];
        }
    }
    
    public void increment(int i, int j, int val) {
        diff[i] += val;
        if (j + 1 < diff.length) {
            diff[j + 1] -= val;
        }
    }
    
    public int[] result() {
        int[] res = new int[diff.length];
        res[0] = diff[0];
        for (int i = 1; i < diff.length; i++) {
            res[i] = res[i-1] + diff[i];
        }
        return res;
    }
}
```

### Python Implementation Notes
```python
# Python class implementation
class DifferenceArray:
    def __init__(self, nums):
        self.n = len(nums)
        self.diff = [0] * self.n
        if nums:
            self.diff[0] = nums[0]
            for i in range(1, self.n):
                self.diff[i] = nums[i] - nums[i-1]
    
    def update(self, start, end, val):
        self.diff[start] += val
        if end + 1 < self.n:
            self.diff[end + 1] -= val
    
    def get_result(self):
        result = [self.diff[0]]
        for i in range(1, self.n):
            result.append(result[-1] + self.diff[i])
        return result
```

---
**Must-Know Problems for Interviews**: LC 370, 1109, 1094, 253, 732
**Advanced Problems**: LC 732, 2132, 2251
**Keywords**: difference array, range update, interval modification, sweep line, prefix sum