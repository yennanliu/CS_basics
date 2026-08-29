# 掃描線（Line Sweep）演算法

> **範圍** — 掃描線：把每個區間轉成 `+1` / `-1` 事件，依座標排序，掃過一次。
> **另見** — [scanning_line_examples.md](./scanning_line_examples.md)：這些模板背後的六道詳解題；[intervals.md](./intervals.md)：不用事件的排序後合併；[difference_array.md](./difference_array.md)：同一招的陣列索引版本；[heap.md](./heap.md)：需要在存活區間上取最大值的掃描。

## LeetCode 題目清單

- [Sweep Line](https://leetcode.com/problem-list/sweep-line/)
- [Sorting](https://leetcode.com/problem-list/sorting/)
- [Prefix Sum](https://leetcode.com/problem-list/prefix-sum/)

## 總覽
**掃描線**（Scanning Line，又稱 Line Sweep 或 Sweep Line）是一種演算法典範：想像有一條垂直線由左而右掃過平面，一邊移動一邊處理沿途發生的事件，藉此處理幾何物件。

關鍵：把`變化`轉換成`事件`，這樣程式就能處理`狀態變化`，而不必直接面對`連續的資訊`。


<p align="center"><img src="../pic/scanning_line.png"></p>

 
### 關鍵性質
- **時間複雜度**：排序 O(n log n) + 處理 O(n)
- **空間複雜度**：儲存事件需要 O(n)
- **核心想法**：把區間問題轉換成以事件為單位的處理流程
- **何時使用**：區間重疊、天際線問題、行事曆衝突、幾何相交

### 演算法原理
1. 把區間轉換成事件（起點／終點）
2. 依位置排序事件（同位置時再依型別排序）
3. 依序處理事件，同時維護狀態
4. 掃描過程中追蹤最大／最小值或其他統計量

### 參考資料
- [NTNU Algorithm Notes](https://web.ntnu.edu.tw/~algo/Point2.html)
- [Line Sweep Tutorial](https://hackmd.io/@meyr543/SkrRZCwfj)
- [Computational Geometry](https://www.cs.princeton.edu/~rs/AlgsDS07/)

## 題型分類

### **模式 1：區間重疊**
- **說明**：找出任一點上重疊區間數的最大值
- **範例**：LC 253, 1094, 2021, 2406, 2848
- **模式**：用計數器追蹤目前存活的區間

### **模式 2：天際線問題**
- **說明**：由一堆重疊的矩形算出可見的輪廓
- **範例**：LC 218, 850, 391
- **模式**：處理帶高度的建築起點／終點事件

### **模式 3：行事曆預約**
- **說明**：管理行事曆事件與衝突
- **範例**：LC 729, 731, 732, 1851
- **模式**：追蹤每個時間點的預約數

### **模式 4：員工空閒時間／區間交集**
- **說明**：跨多份行程表找出共同空閒時間（空隙）或共同忙碌時間（交集）
- **範例**：LC 759, 986, 1229
- **模式**：一條合併後的事件流 + 一個**對覆蓋計數器的判斷條件**——
  `count == 0` → 空閒時間（LC 759），`count == 2` → 兩份清單的交集（LC 986，見 2-7）

### **模式 5：區間更新**
- **說明**：有效率地對區間套用更新
- **範例**：LC 370, 1109, 1893, 2251
- **模式**：差分陣列搭配掃描線

### **模式 6：幾何相交**
- **說明**：求幾何物件的交集
- **範例**：LC 836, 223, 391, 850
- **模式**：依 x 座標排序，追蹤 y 區間

### **模式 7：前綴和 + 最長正和子陣列**
- **說明**：把值轉成 +1／−1 後，找出元素總和 > 0 的最長子陣列
- **範例**：LC 1124, 525, 560, 974
- **模式**：前綴和搭配雜湊表記錄每個和的首次出現位置；若 `prefix > 0` 直接取整段長度，否則在表中查 `prefix - 1`

### **模式 8：時間掃描 + 期限堆積（貪婪排程）** ⭐⭐⭐⭐
- **說明**：沿時間往前掃；每個時間槽只能服務**一個**項目，而每個項目只在它的視窗 `[start, end]` 內有效
- **範例**：LC 1353, 621, 1834, 630, 767
- **模式**：依視窗**起點**排序（項目依時間順序進場）+ 以視窗**終點**為鍵的最小堆積（先服務最急的）+ 對堆頂過期項目做延遲刪除
- **與模式 1 的關鍵差異**：模式 1 是在*計數*同時存在幾個區間（`+1/−1` 計數器）；模式 8 則是*挑出一個子集*——掃描每走一步就消耗一個時間槽，所以需要堆積來決定這個槽要花在**哪個**區間上
- **特徵**：*「每單位時間只能處理一個項目」* + *「每個項目有截止期限」* → 最早期限優先（earliest-deadline-first）就是最佳解

### **模式 9：加權區間排程（掃描 + 已退役工作堆積）** ⭐⭐⭐⭐⭐
- **說明**：挑出一組**互不重疊**的區間使總權重最大；被選中的區間會佔滿它的**整個**範圍
- **範例**：LC 1235, 452, 1751
- **模式**：依**起點**排序；以**終點**為鍵的最小堆積存放*已選取*的鏈；把所有 `end <= start` 的都彈出並併入單調遞增的 `best`；再推入 `(end, best + weight)`
- **與模式 8 的關鍵差異**：模式 8 只花掉區間內的*一個時間槽*；模式 9 則**佔用整個區間**，所以決定選擇的是相容性（而非急迫性）——而權重讓純貪婪失效
- **無權重的退化情形**：所有權重相同 → 堆積退化成單一 `end` 變數 → 依終點排序的貪婪（LC 452）

## 模板與演算法

### 模板比較表
| 模板類型 | 適用情境 | 事件型別 | 複雜度 | 何時使用 |
|---------------|----------|-------------|------------|-------------|
| **基本掃描** | 計算重疊數 | 起點／終點 | O(n log n) | 會議室、區間 |
| **加權掃描** | 重疊值加總 | 起點／終點 + 值 | O(n log n) | 亮度、頻寬 |
| **天際線** | 追蹤高度 | 起點／終點 + 高度 | O(n log n) | 建築輪廓 |
| **差分陣列** | 區間更新 | 更新點 | O(n) | 批次更新 |
| **區間合併** | 合併區間 | 起點／終點 | O(n log n) | 空閒時間、聯集 |
| **二維掃描** | 矩形面積 | X 與 Y 事件 | O(n² log n) | 面積計算 |
| **時間掃描 + 期限堆積** | 挑最多項目，每槽一個 | 起點（進場）+ 終點（期限） | O(n log n) | 最多可參加的活動、任務排程 |
| **掃描 + 已退役工作堆積** | 互不重疊集合的最大權重 | 起點（進場）+ 終點（退役）+ 權重 | O(n log n) | 加權區間排程（LC 1235） |
| **空隙掃描** | 事件之間最大的空洞 | 只有排序後的座標 | O(n log n) | 切割後的最大塊（LC 1465） |
| **索引掃描 + 有序集合** | 視窗內最接近的值 | 索引進場／離場 | O(n log k) | 近似重複偵測（LC 220） |
| **交集掃描** | 輸出覆蓋數 == k 的範圍 | 起點／終點（+ 清單 id） | O(n log n) | 區間清單交集（LC 986） |

### 模板 1：基本區間重疊 — LC 253
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

### 模板 2：加權區間重疊 — LC 2021
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

### 模板 3：天際線問題 — LC 218
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

### 模板 4：行事曆預約 — LC 731
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

### 模板 5：差分陣列模式 — LC 370
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

### 模板 6：用掃描做區間合併 — LC 56
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

### 模板 7：前綴和 — 最長正和子陣列 — LC 1124
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

> **和 LC 525 (Contiguous Array) 是同一種形狀**，只是那題把 `0` 映射成 `-1`：兩者都在找某個前綴值
> *第一次*出現的索引，因為最早的出現位置才能讓子陣列長度最大。可以對照 LC 560 (Subarray Sum
> Equals K) 與 LC 974 (Subarray Sums Divisible by K)，它們用同一張表，但問的是次數而不是長度。


### 模板 8：時間掃描 + 期限堆積 — LC 1353
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

**順序很重要：PUSH → PURGE → SERVE。** 先清理再推入，堆頂可能還留著過期的期限；先服務再清理，則可能「服務」到已經過期的項目。

### 模板 9：掃描 + 已退役工作堆積（加權區間排程） — LC 1235 ⭐⭐⭐⭐⭐

> **和模板 8 的差別**：這裡一個區間會佔滿**整個** `[start, end)`，而且每個區間帶著一份**利潤**。貪婪會失敗——我們需要 `best = 到掃描位置為止可達到的最大利潤`，由掃描過程一路帶著走。

**核心想法**：依**起始時間**掃描；堆積裡放的是*已選取*的工作，以**結束時間**為鍵。任何 `end <= 目前起點` 的工作都已經*退役*——把它彈出並把它的總額併入持續累積的 `best`。接著 `best + profit` 就是以目前這份工作作結的最佳總額。

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

**為什麼 `best` 是單調的**：工作是依結束時間退役的，而 `best` 只會變大——所以在某個掃描位置併入的值，剛好就是「只用在此之前已完成的工作所能得到的最大利潤」。這正是讓 O(N log N) 單趟掃描成立、而不需要顯式 DP 陣列 + 二分搜尋的原因。

> **等價寫法**：依**終點**排序，`dp[i] = max(dp[i-1], profit[i] + dp[binarySearch(start[i])])`。同一條遞迴式——堆積只是取代了二分搜尋。可以對照 LC 1751（模板 8 表格中那題），它因為還限制了數量上限，所以必須用 DP 形式。

#### 變化 9-1：拿掉權重 → 依 END 的純貪婪 — LC 452

> **差別**：沒有利潤，我們要的是*互相重疊區間所形成的最少組數*。當每份工作價值都一樣時，堆積就退化成單一個 `end` 變數。

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

**依 START 排序 vs 依 END 排序 —— 一行判準：**

| 排序鍵 | 它能回答的問題 | 範例 |
|---|---|---|
| **START** | 「同時有幾個活著？」／「我能把什麼接在已結束的東西後面？」 | 253, 2406, 1094, 1235 |
| **END** | 「我最多能留下幾個／最少幾個點就能覆蓋全部？」（貪婪挑選） | 452, 1353（終點堆積）, 630 |

### 模板 10：在排序座標上做空隙掃描 — LC 1465

> **差別**：不是在計算*覆蓋數*，而是掃過排序後的切割位置，量出**相鄰事件之間的空洞**——包含頭尾兩個邊界空隙。維度可分離 ⇒ 用兩次獨立的一維掃描，而不是真正的二維掃描。

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

**🚫 兩個經典陷阱**：(1) 忘記**邊界空隙** `0 → cuts[0]` 與 `cuts[-1] → limit`；(2) 在相乘*之前*就對每個因子取 `% MOD`——最大面積必須用真實值算出來，最後才取一次模（`max(a%M) * max(b%M)` 不等於 `max(a*b) % M`）。

### 模板 11：索引掃描 + 存活有序集合 — LC 220

> **差別**：掃描線跑的是**陣列索引**，而狀態是*仍在視窗內的值所成的集合*，並保持**排序**。這是掃描線的經典搭檔結構（平衡 BST／`TreeSet`）——它用 `ceiling`／`floor` 在 O(log k) 內回答「有沒有相差在 `valueDiff` 以內的鄰居？」。

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

**為什麼一個桶最多只裝一個值**：如果有兩個值落在同一個桶，我們早就回傳 `True` 了，所以這個不變量是安全的。桶寬取 `valueDiff + 1` 正是「同桶 ⇒ 就是答案」成立的原因。

### 事件排序與同分決勝規則（深入探討）

掃描線最常見的錯誤就是座標相同時的同分處理。要依**區間語意**來決定，而不是憑習慣：

| 區間語意 | 相接的區間算重疊嗎？ | 座標相同時的順序 | 本文範例 |
|---|---|---|---|
| `[s, e)` 半開區間（會議、時間） | **不算** — `[1,5)` 和 `[5,9)` 沒問題 | **END 先於 START**（`sort by (x, delta)`） | LC 253 |
| `[s, e]` 閉區間（日期、分組） | **算** — `[1,5]` 和 `[5,10]` 會衝突 | **START 先於 END**（`sort by (x, -delta)`） | LC 2406 |
| 閉區間，但你懶得想 | — | 把終點事件放在 `e + 1`，這樣任何同分順序都可以 | LC 2021, LC 1094 |

**值得背下來的逃生門**：把閉區間的終點 `e` 轉成開區間的 `e + 1`，同分問題就直接*消失*了，因為相接的兩個區間不會再有起點和終點落在同一個座標上。當座標是整數時，優先用這招，而不是寫一個聰明的比較器。

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

**第三層同分決勝**：當多個事件座標相同*而且*型別也相同時（天際線在同一個 `x` 有多個起點），就依酬載排序——LC 218 把起點依**高度遞減**排序，讓最高的先生效，這樣就不會輸出多餘的關鍵點。搭配的延遲刪除最大堆積可見 [`heap.md`](./heap.md) / [`priority_queue.md`](./priority_queue.md)。

#### **掃描 + 堆積／有序集合題目**
| 題目 | LC # | 關鍵技巧 | 難度 |
|---------|------|---------------|------------|
| Maximum Profit in Job Scheduling | 1235 | 依起點掃描 + 已退役工作堆積（累積 `best`） | Hard |
| Minimum Number of Arrows to Burst Balloons | 452 | 依終點排序 + 貪婪 `end` 指標 | Medium |
| Maximum Area of a Piece of Cake | 1465 | 對排序後的切割線逐軸做空隙掃描 | Medium |
| Contains Duplicate III | 220 | 索引掃描 + TreeSet 視窗（或分桶掃描） | Hard |
| Minimum Cost to Hire K Workers | 857 | 依比值排序 + 工資最大堆積（排序一個鍵，堆積另一個鍵） | Hard |
| Minimum Area Rectangle | 939 | 掃描各欄配對 + 已見配對雜湊集合 | Medium |
| Vertical Order Traversal of a Binary Tree | 987 | 垂直掃描，三層排序鍵 `(col, row, val)` | Hard |

## 依模式分類的題目

### 依模式整理的題目表

#### **區間重疊題**
| 題目 | LC # | 關鍵技巧 | 難度 |
|---------|------|---------------|------------|
| Meeting Rooms II | 253 | 基本掃描線 | Medium |
| Car Pooling | 1094 | 追蹤容量 | Medium |
| Brightest Position on Street | 2021 | 加權區間 | Medium |
| Maximum Population Year | 1854 | 年份區間計數 | Easy |
| Maximum Sum Obtained | 2848 | 線上的點 | Medium |
| Describe the Painting | 1943 | 線段合併 | Medium |
| Divide Intervals Into Minimum Number of Groups | 2406 | 事件掃描，最大同時重疊數 | Medium |

#### **天際線題**
| 題目 | LC # | 關鍵技巧 | 難度 |
|---------|------|---------------|------------|
| The Skyline Problem | 218 | 追蹤高度 | Hard |
| Rectangle Area II | 850 | 二維掃描 | Hard |
| Perfect Rectangle | 391 | 角點計數 | Hard |
| Falling Squares | 699 | 線段樹 + 掃描 | Hard |

#### **行事曆預約題**
| 題目 | LC # | 關鍵技巧 | 難度 |
|---------|------|---------------|------------|
| My Calendar I | 729 | 檢查是否重疊 | Medium |
| My Calendar II | 731 | 允許重複預約兩次 | Medium |
| My Calendar III | 732 | K 重預約 | Hard |
| Minimum Interval to Include Query | 1851 | 查詢 + 掃描 | Hard |

#### **員工行程題**
| 題目 | LC # | 關鍵技巧 | 難度 |
|---------|------|---------------|------------|
| Employee Free Time | 759 | 區間空隙 | Hard |
| Interval List Intersections | 986 | 雙指標**或**覆蓋數 `==2` 的掃描（見 2-7） | Medium |
| Meeting Scheduler | 1229 | 共同時段（與 986 同一套掃描 + 長度過濾） | Medium |
| Remove Covered Intervals | 1288 | 排序 + 掃描 | Medium |

#### **區間更新題**
| 題目 | LC # | 關鍵技巧 | 難度 |
|---------|------|---------------|------------|
| Range Addition | 370 | 差分陣列 | Medium |
| Corporate Flight Bookings | 1109 | 差分陣列 | Medium |
| Plates Between Candles | 2055 | 前綴和 + 二分搜尋 | Medium |
| Count Integers in Intervals | 2276 | 區間合併 | Hard |

#### **前綴和子陣列題**
| 題目 | LC # | 關鍵技巧 | 難度 |
|---------|------|---------------|------------|
| Longest Well-Performing Interval | 1124 | 前綴和 +1/−1，首次出現表 | Medium |
| Contiguous Array | 525 | 前綴和 0→−1，首次出現表 | Medium |
| Subarray Sum Equals K | 560 | 前綴和次數表 | Medium |
| Subarray Sums Divisible by K | 974 | 前綴和取模，次數表 | Medium |

#### **時間掃描 + 期限堆積題**
| 題目 | LC # | 關鍵技巧 | 難度 |
|---------|------|---------------|------------|
| Maximum Number of Events That Can Be Attended | 1353 | 依起點排序 + 結束日最小堆積，最早期限優先 | Medium |
| Max Number of Events That Can Be Attended II | 1751 | DP + 二分搜尋（**不是**掃描／堆積） | Hard |
| Task Scheduler | 621 | 依頻率的最大堆積 + 冷卻佇列 | Medium |
| Single-Threaded CPU | 1834 | 時間跳到下一個到達點 + 以 (處理時間, idx) 為鍵的最小堆積 | Medium |
| Course Schedule III | 630 | 依期限貪婪 + 最大堆積替換 | Hard |
| Reorganize String | 767 | 以剩餘次數為鍵的最大堆積，每個位置放一個 | Medium |

#### **幾何題**
| 題目 | LC # | 關鍵技巧 | 難度 |
|---------|------|---------------|------------|
| Rectangle Overlap | 836 | 二維重疊 | Easy |
| Rectangle Area | 223 | 面積計算 | Medium |
| Number of Airplanes in Sky | 391 | 時間點 | Medium |
| Line Reflection | 356 | 座標映射 | Medium |

## 模式選擇策略

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

## 總結與速查

### 複雜度速查
| 操作 | 時間複雜度 | 空間 | 說明 |
|-----------|-----------------|-------|-------|
| 建立事件 | O(n) | O(n) | 每個區間 2 個事件 |
| 事件排序 | O(n log n) | O(1) | 主導操作 |
| 掃描處理 | O(n) | O(1) | 單趟走訪 |
| 搭配 TreeMap/Heap | O(n log n) | O(n) | 用於天際線問題 |
| 差分陣列 | O(n + m) | O(m) | m = 範圍大小 |
| 二維掃描 | O(n² log n) | O(n²) | 矩形問題 |

### 模板速查
| 模板 | 模式 | 關鍵程式碼 |
|----------|---------|----------|
| **基本掃描** | 計算重疊數 | `events.sort(); count += delta` |
| **加權** | 值加總 | `weight += delta * value` |
| **天際線** | 追蹤高度 | `heapq for max height` |
| **行事曆** | 預約衝突 | `if count >= k: reject` |
| **差分** | 區間更新 | `diff[start]++; diff[end+1]--` |
| **合併** | 合併區間 | `if active==0: new interval` |
| **時間掃描 + 期限堆積** | 每個時間槽挑 1 個 | `push start<=day; pop end<day; pop pq; day+=1` |
| **已退役工作堆積** | 互不重疊集合的最大權重 | `while pq[0].end<=start: best=max(...); push (end, best+w)` |
| **空隙掃描** | 最大空洞 | `sort(cuts); max(cuts[0], limit-cuts[-1], diffs)` |
| **有序集合掃描** | 視窗內最接近的值 | `set.remove(out); set.ceiling(x-t) <= x+t` |
| **交集掃描** | 兩份區間清單取 AND | `if ++count==2: start=x` / `if count==2: emit [start,x]` |

### 常見模式與技巧

#### **事件排序規則**
```python
# Critical: Handle events at same position correctly
# Start before End at same position
events.sort(key=lambda x: (x[0], -x[1]))
# OR End before Start (depends on problem)
events.sort(key=lambda x: (x[0], x[1]))
```

#### **區間轉事件**
```python
# Standard conversion
for start, end in intervals:
    events.append((start, +1))  # Enter
    events.append((end, -1))     # Exit
    
# Inclusive vs Exclusive endpoints
events.append((end, -1))     # Exclusive end
events.append((end+1, -1))   # Inclusive end
```

#### **追蹤最大值的模式**
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

#### **天際線高度管理**
```python
# Use negative for max heap in Python
import heapq
heights = [0]  # Ground level
heapq.heappush(heights, -height)  # Add
max_height = -heights[0]           # Get max
```

### 解題步驟

1. **辨識事件型別**
   - 什麼代表一個區間的開始？
   - 什麼代表結束？
   - 還有其他事件型別嗎？

2. **設計事件結構**
   - 位置／時間
   - 事件型別（起點／終點）
   - 額外資料（值、id 等等）

3. **決定排序順序**
   - 主要：依位置／時間
   - 次要：起點與終點的先後處理
   - 第三：需要時再依值排序

4. **處理事件**
   - 維護當下的狀態
   - 更新最大／最小值
   - 檢查限制條件

5. **處理邊界情況**
   - 同位置的事件
   - 空區間
   - 單點區間
   - 端點重疊

### 常見錯誤與提示

**🚫 常見錯誤：**
- 同位置事件的順序搞錯
- 閉區間／開區間端點造成的 off-by-one 錯誤
- 沒處理空的區間清單
- 忘了記錄最大值出現的位置
- 追蹤高度時用錯資料結構

**✅ 最佳實務：**
- 一定要先問清楚區間是閉的還是開的
- 動態高度查詢請用 TreeMap／TreeSet
- 區間更新可考慮差分陣列
- 用端點重疊的案例做測試
- 把掃描線的移動過程視覺化

### 面試提示

1. **辨識題型**
   - 「最大重疊數」→ 掃描線
   - 「天際線／輪廓」→ 追蹤高度
   - 「空閒時間」→ 先合併再找空隙（`count == 0`）
   - 「兩份區間清單的交集」→ 雙指標（O(m+n)）；若未排序或有 k 份清單則用 `count == 2` 的掃描（模板 2-7）
   - 「區間更新」→ 差分陣列
   - 「每天／每個時間槽一個事件，且各有期限」→ 時間掃描 + 期限堆積
   - 「互不重疊區間的最大**利潤／權重**」→ 依起點掃描 + 已退役工作堆積（模板 9）
   - 「用最少的點／箭覆蓋所有區間」→ 依終點排序 + 貪婪（變化 9-1）
   - 「任兩個值相差在 t 內、索引相差在 k 內」→ 索引掃描 + 有序集合／分桶（模板 11）

2. **釐清需求**
   - 區間是閉的還是開的？
   - 區間長度可以是 0 嗎？
   - 同位置的事件怎麼處理？
   - 答案要的是數量還是具體的區間？

3. **最佳化空間**
   - 範圍很大時做座標壓縮
   - 動態更新用線段樹
   - 單點查詢用二分搜尋
   - 區間更新用懶惰標記傳遞

4. **常見追問**
   - 支援動態新增區間
   - 查詢特定點
   - 找出第 k 大的重疊數
   - 支援修改區間

### 進階技巧

#### **座標壓縮**
```python
# Compress large coordinate space
coords = set()
for start, end in intervals:
    coords.add(start)
    coords.add(end)
coord_map = {v: i for i, v in enumerate(sorted(coords))}
```

#### **整合線段樹**
- 用於動態更新
- 查詢區間最大／最小值
- 用懶惰傳遞提升效率

#### **持久化資料結構**
- 追蹤變更歷史
- 可查詢任一時間點的狀態
- 適合時序資料庫

### 相關主題
- **區間問題**：合併、插入、移除區間
- **貪婪演算法**：活動選擇
- **計算幾何**：線段相交
- **資料串流**：依序處理事件
- **差分陣列**：高效的區間更新


## 詳解範例

六道題目收錄在 **[scanning_line_examples.md](./scanning_line_examples.md)**，
依「掃描在數什麼」分組：

| 分組 | 題目 |
|---|---|
| [計算重疊](./scanning_line_examples.md#counting-overlap) | LC 253, 2406, 731 |
| [加權掃描](./scanning_line_examples.md#weighted-sweeps) | LC 2021 |
| [掃描加堆積](./scanning_line_examples.md#sweep-plus-a-heap) | LC 1353 |
| [雙指標求交集](./scanning_line_examples.md#two-pointer-intersection) | LC 986 |

LC 1124 已經沒有獨立的範例段落：那原本只是把模板 7 用兩種語言再貼一次，
而該段唯一多出來的東西——與 LC 525 的關聯——現在已經寫成模板本身的一則註記。
