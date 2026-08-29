# 區間（Intervals）

> **範圍** — 先排序再合併的區間題型 — 合併、插入、數重疊、最少刪除數。
> **另見**：[scanning_line.md](./scanning_line.md) — 需要維護一個即時計數時的事件掃描；[difference_array.md](./difference_array.md) — 大量區間*更新*、最後只讀一次；[array_overlap_explaination.md](./array_overlap_explaination.md) — 重疊條件本身；[heap.md](./heap.md) — 用優先佇列做區間排程。

## LeetCode 題目清單

- [Sorting](https://leetcode.com/problem-list/sorting/)
- [Greedy](https://leetcode.com/problem-list/greedy/)
- [Sweep Line](https://leetcode.com/problem-list/sweep-line/)

## 總覽

**區間**類題目處理的是數值範圍，通常表示成 `[start, end]` 配對，要做的操作像是合併重疊範圍、找交集，或安排互不重疊的事件。

### 關鍵性質
- **時間複雜度**：排序 O(n log n) + 處理 O(n) = 整體 O(n log n)
- **空間複雜度**：O(1) 到 O(n)，看輸出需求而定
- **核心想法**：依起點排序，然後線性掃過去處理重疊
- **什麼時候用**：牽涉到範圍、排程、行事曆管理、資源配置的題目

### 核心演算法步驟
1. 依起點**排序區間**（貪婪題偶爾改成依終點）
2. **依序處理**，判斷哪些重疊、哪些不重疊
3. 依題目需求**套用合併／移除策略**
4. **處理邊界情況**，像是空區間或只有一個區間

### 什麼時候用區間演算法
- **合併重疊範圍**：行事曆衝突、記憶體配置
- **排程最佳化**：會議室、任務分派
- **範圍查詢**：時間序列資料、基因序列
- **資源管理**：頻寬配置、CPU 排程

### 參考資料
- [labuladong: Interval Merge](https://github.com/labuladong/fucking-algorithm/blob/master/%E7%AE%97%E6%B3%95%E6%80%9D%E7%BB%B4%E7%B3%BB%E5%88%97/%E5%8C%BA%E9%97%B4%E8%B0%83%E5%BA%A6%E9%97%AE%E9%A2%98%E4%B9%8B%E5%8C%BA%E9%97%B4%E5%90%88%E5%B9%B6.md)
- [labuladong: Interval Overlap](https://github.com/labuladong/fucking-algorithm/blob/master/%E7%AE%97%E6%B3%95%E6%80%9D%E7%BB%B4%E7%B3%BB%E5%88%97/%E5%8C%BA%E9%97%B4%E4%BA%A4%E9%9B%86%E9%97%AE%E9%A2%98.md)
- [Visualization Explanation](https://github.com/yennanliu/CS_basics/blob/master/doc/cheatsheet/array_overlap_explaination.md)

## 1) 題型分類

### **模式 1：區間合併** — LC 56 ⭐⭐⭐⭐⭐
- **說明**：把重疊的區間併成一個
- **例題**：LC 56（Merge Intervals）、LC 57（Insert Interval）
- **辨認關鍵字**：「合併」、「combine」、「重疊區間」
- **排序**：依起點（遞增）

### **模式 2：區間排程（貪婪）** — LC 435 ⭐⭐⭐⭐
- **說明**：求最多的互不重疊區間，或最少要移除幾個區間
- **例題**：LC 435（Non-overlapping Intervals）、LC 452（Minimum Arrows）
- **辨認關鍵字**：「最多」、「最少」、「不重疊」、「移除」
- **排序**：貪婪解法依終點（遞增）

### **模式 3：區間交集** — LC 986 ⭐⭐⭐
- **說明**：找兩份區間清單的共同時段或重疊區域
- **例題**：LC 986（Interval List Intersections）、LC 1288（Remove Covered Intervals）
- **辨認關鍵字**：「交集」、「重疊」、「共同」、「被覆蓋」
- **排序**：依起點排序，用雙指標處理

### **模式 4：用點覆蓋區間** — LC 452 ⭐⭐⭐
- **說明**：找出能同時覆蓋多個區間的點，或找出空隙
- **例題**：LC 452（Minimum Arrows）、LC 1024（Video Stitching）
- **辨認關鍵字**：「覆蓋」、「點」、「箭」、「最少覆蓋數」
- **排序**：依策略決定用起點或終點

### **模式 5：會議室排程** — LC 253 ⭐⭐⭐⭐⭐
- **說明**：算出需要幾間會議室，或檢查排程有沒有衝突
- **例題**：LC 252（Meeting Rooms）、LC 253（Meeting Rooms II）
- **辨認關鍵字**：「會議」、「會議室」、「排程衝突」
- **排序**：依起點排序，用優先佇列管理房間

### **模式 6：行事曆與訂位** — LC 729
- **說明**：處理行事曆訂位，偵測並解決衝突
- **例題**：LC 729（My Calendar I）、LC 731（My Calendar II）、LC 732（My Calendar III）
- **辨認關鍵字**：「行事曆」、「訂位」、「重複訂位」、「k 重訂位」
- **排序**：維持區間有序，用二分搜尋找插入位置

## 2) 模板與演算法

### 模板比較表
| 模板類型 | 適用情境 | 排序策略 | 什麼時候用 |
|---------------|----------|------------------|-------------|
| **合併模板** | 把重疊區間併起來 | 依起點排序 | LC 56、57，合併類題目 |
| **貪婪模板** | 最多互不重疊 | 依終點排序 | LC 435、452，排程最佳化 |
| **雙指標模板** | 交集／比對 | 兩份清單都依起點排序 | LC 986，比對兩份區間清單 |
| **優先佇列模板** | 資源管理 | 依起點排序，堆積依終點 | LC 253，會議室類題目 |
| **二分搜尋模板** | 行事曆／訂位 | 維持有序 | LC 729-732，動態插入區間 |

### 通用區間模板 ⭐⭐⭐⭐
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

### 各別模板

#### 模板 1：區間合併（LC 56、57）
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
```

```java
// Java version
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

#### 模板 2：貪婪排程（LC 435、452）
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
```

```java
// Java version
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

#### 模板 3：雙指標求交集（LC 986）
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

#### 模板 4：用優先佇列做會議室（LC 253）
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
```

```java
// Java version
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

#### 模板 5：行事曆訂位（LC 729）
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

## 3) 依模式分類的題目

### **合併模式題目**
| 題目 | LC # | 關鍵技巧 | 難度 | 模板 |
|---------|------|---------------|------------|----------|
| Merge Intervals | 56 | 依起點排序，合併重疊 | Medium | 合併模板 |
| Insert Interval | 57 | 插入後合併 | Medium | 合併模板 |
| Summary Ranges | 228 | 連續數字的範圍 | Easy | 合併模板 |
| Data Stream as Disjoint Intervals | 352 | TreeMap／SortedDict | Hard | 合併模板 |
| Merge Similar Items | 2363 | 依權重合併 | Easy | 合併模板 |

### **貪婪排程題目**
| 題目 | LC # | 關鍵技巧 | 難度 | 模板 |
|---------|------|---------------|------------|----------|
| Non-overlapping Intervals | 435 | 依終點排序，貪婪移除 | Medium | 貪婪模板 |
| Minimum Arrows to Burst Balloons | 452 | 依終點排序，數箭數 | Medium | 貪婪模板 |
| Maximum Length of Pair Chain | 646 | 依第二個元素排序 | Medium | 貪婪模板 |
| Activity Selection Problem | - | 經典貪婪演算法 | Medium | 貪婪模板 |
| Car Pooling | 1094 | 時間軸 + 容量 | Medium | 貪婪模板 |
| Partition Labels | 763 | 最後出現位置構成的區間 + 單趟合併 | Medium | 合併模板 |
| Jump Game II | 45 | 隱含區間 + 貪婪覆蓋 | Medium | 貪婪模板 |
| Jump Game | 55 | 掃描最遠可達位置 | Medium | 貪婪模板 |

### **交集與覆蓋題目**
| 題目 | LC # | 關鍵技巧 | 難度 | 模板 |
|---------|------|---------------|------------|----------|
| Interval List Intersections | 986 | 雙指標 | Medium | 雙指標模板 |
| Remove Covered Intervals | 1288 | 排序後過濾 | Medium | 合併模板 |
| Find Right Interval | 436 | 二分搜尋 | Medium | 二分搜尋 |
| Employee Free Time | 759 | 合併後找空隙 | Hard | 合併模板 |
| Video Stitching | 1024 | 貪婪覆蓋 | Medium | 貪婪模板 |
| Maximum Area of a Piece of Cake After Horizontal and Vertical Cuts | 1465 | 排序後切點之間的最大間隔 | Medium | 間隔掃描模板 |

### **會議室與排程題目**
| 題目 | LC # | 關鍵技巧 | 難度 | 模板 |
|---------|------|---------------|------------|----------|
| Meeting Rooms | 252 | 排序後檢查衝突 | Easy | 基本模板 |
| Meeting Rooms II | 253 | 優先佇列 | Medium | 優先佇列模板 |
| Meeting Scheduler | 1229 | 雙指標 + 時長 | Medium | 雙指標模板 |
| Minimum Time to Make Rope Colorful | 1578 | 連續區間 | Medium | 貪婪模板 |
| Course Schedule III | 630 | 優先佇列 + 貪婪 | Hard | 優先佇列模板 |

### **行事曆與訂位題目**
| 題目 | LC # | 關鍵技巧 | 難度 | 模板 |
|---------|------|---------------|------------|----------|
| My Calendar I | 729 | 有序清單 + 二分搜尋 | Medium | 行事曆模板 |
| My Calendar II | 731 | 偵測重複訂位 | Medium | 行事曆模板 |
| My Calendar III | 732 | 用時間軸做 k 重訂位 | Hard | 行事曆模板 |
| Exam Room | 855 | 維護最大間隔 | Medium | 二分搜尋 |
| Range Module | 715 | 線段樹／區間 | Hard | 進階模板 |

### **進階區間題目**
| 題目 | LC # | 關鍵技巧 | 難度 | 模板 |
|---------|------|---------------|------------|----------|
| Falling Squares | 699 | 座標壓縮 | Hard | 進階模板 |
| The Skyline Problem | 218 | 掃描線 + 優先佇列 | Hard | 進階模板 |
| Rectangle Area II | 850 | 座標壓縮 | Hard | 進階模板 |
| Perfect Rectangle | 391 | 面積計算 + 驗證 | Hard | 進階模板 |
| Count Integers in Intervals | 2276 | 動態區間 | Hard | 進階模板 |

## 4) 模式選擇策略

### 決策流程圖

```text
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

### 模板選擇指南

**快速決策樹：**
1. **重疊判斷**：`prev[1] >= curr[0]`（假設已依起點排序）
2. **合併策略**：延伸 `prev[1] = max(prev[1], curr[1])`
3. **貪婪策略**：依終點排序，留下最早結束的
4. **資源管理**：對終點用最小堆
5. **動態插入**：用二分搜尋維持有序

## 5) 關鍵模式與重疊判斷

### 重疊判斷方法

#### 方法 1：已依起點排序之後
```python
def has_overlap(interval1, interval2):
    """Check if two intervals overlap (sorted by start)"""
    return interval1[1] > interval2[0]
```

#### 方法 2：一般情況（任意順序）
```python
def has_overlap(interval1, interval2):
    """Check if two intervals overlap (any order)"""
    start1, end1 = interval1
    start2, end2 = interval2
    return start1 < end2 and start2 < end1
```

### 重疊示意圖
```text
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

### 常見區間操作

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

## 6) 總結與速查

### 複雜度速查
| 操作 | 時間 | 空間 | 備註 |
|-----------|------|-------|-------|
| 排序區間 | O(n log n) | O(1) | 一定要做的第一步 |
| 合併重疊 | O(n) | O(n) | 排序之後 |
| 求交集 | O(m + n) | O(min(m,n)) | 雙指標解法 |
| 會議室 | O(n log n) | O(n) | 對終點用優先佇列 |
| 行事曆訂位 | O(log n) | O(n) | 每次插入做一次二分搜尋 |
| 貪婪排程 | O(n log n) | O(1) | 依終點排序 |

### 模板速查
| 模板 | 模式 | 關鍵程式碼 |
|----------|---------|----------|
| **合併** | 重疊區間 | `if last[1] < curr[0]: append else: merge` |
| **貪婪** | 最多互不重疊 | `sort(key=end); if curr[0] >= prev[1]: count++` |
| **雙指標** | 兩份清單求交集 | `start=max(starts), end=min(ends)` |
| **優先佇列** | 資源管理 | `heappush(end_time); if heap[0] <= start: heappop` |
| **二分搜尋** | 動態插入 | `bisect.insort` 或自己寫二分搜尋 |

### 常見模式與技巧

#### **模式 1：合併重疊**
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

#### **模式 2：貪婪挑選**
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

#### **模式 3：時間軸事件**
```python
# Convert intervals to events for sweep line
events = []
for start, end in intervals:
    events.append((start, 1))    # start event
    events.append((end, -1))     # end event
events.sort()
```

### 解題步驟
1. **辨認模式**：是合併、排程、交集，還是資源管理？
2. **選排序策略**：依起點（合併）還是依終點（貪婪）
3. **選模板**：用上面對應的模板
4. **處理邊界情況**：空陣列、只有一個區間、完全相同的區間
5. **最佳化**：資料量大時考慮空間最佳化

### 常見錯誤與提示

**🚫 常見錯誤：**
- **排序順序搞錯**：該依終點排序（貪婪題）卻依起點排序
- **差一錯誤**：重疊條件裡 `<=` 和 `<` 用混
- **沒處理邊界情況**：沒檢查空陣列或只有一個區間
- **合併邏輯出錯**：合併時忘了同時更新起點和終點
- **對貪婪策略一知半解**：不理解為什麼依終點排序會對
- **空間複雜度**：建了不必要的中間資料結構

**✅ 最佳實務：**
- **一律先排序**：多數區間題都需要有序輸入
- **把重疊定義講清楚**：動手寫程式前先明確定義重疊條件
- **用對模板**：讓模板對上題目的模式
- **測邊界情況**：空輸入、單一區間、完全相同的區間
- **把例子畫出來**：把區間畫出來才看得懂重疊的樣態
- **選對排序 key**：合併用起點，貪婪用終點

### 面試提示
1. **先從例子開始**：在紙上把區間畫出來
2. **釐清邊界情況**：空區間怎麼辦？長度為零的點區間呢？
3. **解釋排序的選擇**：為什麼依起點／終點排序？
4. **走一遍演算法**：一步一步展示合併／貪婪的邏輯
5. **逐步最佳化**：先寫出能跑的解，再最佳化
6. **練熟常見模式**：把上面五個主要模板練到精
7. **分析時間複雜度**：一定要說明是排序 O(n log n) + 處理 O(n)

### 資料結構轉換技巧

#### List 轉陣列（Java）
```java
List<int[]> result = new ArrayList<>();
// ... populate result
return result.toArray(new int[result.size()][]);
```

#### Python 中的高效合併
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

### 相關主題
- **貪婪演算法**：區間排程最佳化
- **二分搜尋**：行事曆訂位與插入類題目
- **優先佇列**：會議室與資源管理
- **雙指標**：交集與比對類題目
- **掃描線**：天際線、矩形這類進階題
- **線段樹**：區間上的範圍更新與查詢

## LC 範例

### 2-1) Merge Intervals (LC 56) — 排序 + 合併
> 依起點排序；跟最後一個已合併的區間比對，把重疊的併起來。

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

### 2-2) Non-overlapping Intervals (LC 435) — 貪婪區間排程
> 依終點排序；貪婪地留下最早結束的區間，讓移除數最少。

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

### 2-3) Insert Interval (LC 57) — 線性掃描 + 合併
> 插入新區間，並在一趟掃描中把所有重疊的區間合併掉。

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

### 2-4) Meeting Rooms II (LC 253) — 對終點用最小堆
> 依起點排序；堆積追蹤最早結束的那間房 — 如果它在下一場會議開始前就結束，就重複使用。

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

### 2-5) Minimum Number of Arrows to Burst Balloons (LC 452) — 貪婪
> 依終點排序；在區間終點射一箭就能戳破所有重疊的；出現空隙時才往前推進。

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

### 2-6) Interval List Intersections (LC 986) — 雙指標
> 推進終點較早的那個指標；範圍相交時記錄下重疊部分。

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

### 2-7) Remove Covered Intervals (LC 1288) — 排序 + 貪婪
> 依起點遞增、終點遞減排序；若某區間的終點 ≤ 目前的最大終點，它就是被覆蓋的。

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

### 2-8) Video Stitching (LC 1024) — 貪婪區間覆蓋
> 依起點排序；在每個邊界上挑那個能把覆蓋範圍推得最遠的片段。

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

### 2-9) Maximum Profit in Job Scheduling (LC 1235) — DP + 二分搜尋
> 工作依終點排序；dp[i] = 用前 i 個工作能得到的最大利潤；用二分搜尋找最後一個不衝突的工作。

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

### 2-10) My Calendar I (LC 729) — TreeMap 重疊檢查
> TreeMap 的 floor/ceiling 讓每次訂位的重疊偵測是 O(log N)。

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

### 2-11) Meeting Rooms I (LC 252) — 排序 + 相鄰檢查
> 依起點排序；只要有任何一場會議在前一場結束前開始，就存在重疊。

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

### 2-12) Partition Labels (LC 763) — 從資料造出區間，再合併 ⭐⭐⭐⭐⭐

> **關鍵想法**：區間不是給你的 — **是你造出來的**。每個字元 `c` 擁有區間 `[first(c), last(c)]`；一個合法的分段就是一個*合併後*的區間。因為我們是從左掃到右，這些區間抵達時就**已經依起點排好序**，所以不需要 `sort()`：維護一個滾動的 `end = max(end, last[c])`，`i == end` 的那一刻就切一刀。
>
> **模式**：`輸入 → 推導出區間 → 合併` 是面試中最常見的區間偽裝（LC 56 的引擎也是它，只是輸入變成隱含的）。

**為什麼 `i == end` 是正確的切點**：`end` 是目前為止看過的每個字元其最後出現位置的最大值。當掃描索引追上它時，`[start, i]` 裡的任何字元都不會在後面再出現 ⇒ 這個區塊就封閉了，不可能再跟右邊的東西合併。

```text
s = a b a b c b a c a d e f e g d e h i j h k l i j
i:  0 1 2 3 4 5 6 7 8 9 ...
last[a]=8, last[b]=5, last[c]=7   -> end grows 0,5,5,5,7,7,8,8,8 -> cut at i=8  (len 9)
                                     next block starts at 9 ...
```

```java
// java
// LC 763 - Partition Labels
// IDEA: last occurrence of each char = that char's interval end; extend & cut in one pass (no sort)
// time = O(N), space = O(1)  (26 letters)
public List<Integer> partitionLabels(String s) {
    int[] last = new int[26];
    for (int i = 0; i < s.length(); i++) last[s.charAt(i) - 'a'] = i;

    List<Integer> res = new ArrayList<>();
    int start = 0, end = 0;
    for (int i = 0; i < s.length(); i++) {
        end = Math.max(end, last[s.charAt(i) - 'a']);   // extend current interval
        if (i == end) {                                 // interval closed -> cut here
            res.add(end - start + 1);
            start = i + 1;
        }
    }
    return res;
}
```

```python
# python
# LC 763 - Partition Labels
# IDEA: last[c] = that char's interval end; extend running end, cut when i == end
# time = O(N), space = O(1)  (26 letters)
def partitionLabels(s):
    last = {c: i for i, c in enumerate(s)}   # dict comprehension keeps the LAST index
    res, start, end = [], 0, 0
    for i, c in enumerate(s):
        end = max(end, last[c])              # extend current interval
        if i == end:                         # interval closed -> cut here
            res.append(end - start + 1)
            start = i + 1
    return res
```

**跟 LC 56（2-1）的對照**：LC 56 非排序不可，因為區間是以任意順序給的；這裡掃描順序*就是*起點順序，所以成本降到 O(N)。

### 2-13) Jump Game II (LC 45) — 對*隱含*區間做貪婪覆蓋

> **2-8（Video Stitching）的變形**：這裡的轉折是根本沒給你區間陣列 — 索引 `i` 隱含地覆蓋 `[i, i + nums[i]]`，而且這些區間本來就依起點排好了，所以 O(N log N) 的排序消失，貪婪覆蓋只要 O(N)。
>
> **模式**：跟 LC 1024 一樣的雙邊界貪婪 — `curEnd` = 目前為止這些跳躍買到的覆蓋邊界，`farthest` = 所有起點落在其中的區間裡最遠能到哪。碰到 `i == curEnd` 代表這一層用完了 ⇒ 再花一次跳躍。

```java
// java
// LC 45 - Jump Game II
// IDEA: index i = interval [i, i+nums[i]]; greedy cover, +1 jump when current coverage is exhausted
// time = O(N), space = O(1)
public int jump(int[] nums) {
    int jumps = 0, curEnd = 0, farthest = 0;
    for (int i = 0; i < nums.length - 1; i++) {   // stop at n-1: no jump needed once we can reach it
        farthest = Math.max(farthest, i + nums[i]);
        if (i == curEnd) {                        // exhausted current layer -> must jump
            jumps++;
            curEnd = farthest;
        }
    }
    return jumps;
}
```

```python
# python
# LC 45 - Jump Game II
# IDEA: index i = interval [i, i+nums[i]]; greedy cover, +1 jump when current coverage is exhausted
# time = O(N), space = O(1)
def jump(nums):
    jumps = cur_end = farthest = 0
    for i in range(len(nums) - 1):      # stop before last index
        farthest = max(farthest, i + nums[i])
        if i == cur_end:                # exhausted current layer -> must jump
            jumps += 1
            cur_end = farthest
    return jumps
```

#### 變形 — LC 55 Jump Game：*只問能不能到，把跳躍計數器拿掉*

> 一樣的掃描；不再數層數，而是在掃描索引越過最遠覆蓋位置的那一刻就宣告失敗（覆蓋出現了**空隙**）。

```java
// java
// LC 55 - Jump Game
// IDEA: same coverage scan; unreachable once i > farthest (gap in the cover)
// time = O(N), space = O(1)
public boolean canJump(int[] nums) {
    int farthest = 0;
    for (int i = 0; i < nums.length; i++) {
        if (i > farthest) return false;               // gap: cover breaks here
        farthest = Math.max(farthest, i + nums[i]);
    }
    return true;
}
```

```python
# python
# LC 55 - Jump Game
# IDEA: same coverage scan; unreachable once i > farthest (gap in the cover)
# time = O(N), space = O(1)
def canJump(nums):
    farthest = 0
    for i, n in enumerate(nums):
        if i > farthest:
            return False                              # gap: cover breaks here
        farthest = max(farthest, i + n)
    return True
```

| 題目 | 有給區間嗎？ | 需要排序嗎？ | 問的問題 |
|---------|------------------|--------------|----------------|
| LC 1024 Video Stitching | 明確給了 `clips[i] = [s, e]` | 要 — O(N log N) | 覆蓋 `[0, time]` 最少要幾段 |
| LC 45 Jump Game II | 隱含的 `[i, i+nums[i]]` | 不用 — 本來就依起點排好 | 到 `n-1` 最少要幾個區間 |
| LC 55 Jump Game | 隱含的 `[i, i+nums[i]]` | 不用 | `[0, n-1]` 到底能不能被覆蓋 |

### 2-14) Maximum Area of a Piece of Cake (LC 1465) — 間隔掃描（區間的補集）

> **關鍵想法**：一組切點的*補集*就是一組區間。把邊界排序之後，每一塊就只是相鄰的差值 — 再加上兩個**邊界間隔**（`0 → 第一刀` 和 `最後一刀 → 邊界`），幾乎所有錯誤答案都是漏了這兩個。
>
> **模式**：同一套「排序後邊界之間的間隔」掃描，也能解各種找空檔的題目（例如 LC 759 Employee Free Time 合併後的空隙、LC 228 Summary Ranges）。這題兩個軸是獨立的，所以 `maxArea = maxGap(h) * maxGap(w)`。

```java
// java
// LC 1465 - Maximum Area of a Piece of Cake After Horizontal and Vertical Cuts
// IDEA: complement of sorted cuts = piece intervals; max gap per axis, multiply (axes independent)
// time = O(H log H + V log V), space = O(1)
public int maxArea(int h, int w, int[] horizontalCuts, int[] verticalCuts) {
    Arrays.sort(horizontalCuts);
    Arrays.sort(verticalCuts);
    long maxH = maxGap(horizontalCuts, h);
    long maxV = maxGap(verticalCuts, w);
    return (int) ((maxH * maxV) % 1_000_000_007L);   // multiply as long: 1e9 * 1e9 overflows int
}

private long maxGap(int[] cuts, int border) {
    long best = cuts[0];                                    // border gap: 0 -> first cut
    for (int i = 1; i < cuts.length; i++)
        best = Math.max(best, cuts[i] - cuts[i-1]);         // inner gaps: cut -> cut
    return Math.max(best, border - cuts[cuts.length - 1]);  // border gap: last cut -> border
}
```

```python
# python
# LC 1465 - Maximum Area of a Piece of Cake After Horizontal and Vertical Cuts
# IDEA: complement of sorted cuts = piece intervals; max gap per axis, multiply (axes independent)
# time = O(H log H + V log V), space = O(1)
def maxArea(h, w, horizontalCuts, verticalCuts):
    def max_gap(cuts, border):
        cuts = sorted(cuts)
        best = max(cuts[0], border - cuts[-1])              # the two border gaps
        for a, b in zip(cuts, cuts[1:]):
            best = max(best, b - a)                         # inner gaps
        return best

    return (max_gap(horizontalCuts, h) * max_gap(verticalCuts, w)) % (10 ** 9 + 7)
```
