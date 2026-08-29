# 掃描線 — 範例詳解

> **範圍** — [scanning_line.md](./scanning_line.md) 背後的完整解題檔案：六道題目，依「掃描究竟在數什麼」分組 — 重疊深度、加權總和、存活工作的堆積，或兩個有序清單的交集。
> **另見**：[scanning_line.md](./scanning_line.md) — 母文件：十一個模板、事件排序與平手決勝規則，以及模式選擇策略；[intervals.md](./intervals.md) — 不需要掃描線的區間題；[heap.md](./heap.md) — 排程那一組背後的資料結構；[difference_array.md](./difference_array.md) — 事件掃描在陣列上的對應版本；[greedy.md](./greedy.md) — 為什麼截止期限堆積的排程是最佳解。

## LeetCode 題目清單

- [Line Sweep](https://leetcode.com/problem-list/line-sweep/)
- [Interval](https://leetcode.com/tag/interval/)

## 總覽

這裡是 [scanning_line.md](./scanning_line.md) 的長尾部分。母文件保留十一個模板，以及真正決定
正確性的那部分 — 事件排序與平手決勝規則；本檔案保留*套用*這些規則的題目。

### 關鍵性質
- **複雜度**：O(n log n)，由事件排序主導；掃描本身是線性的
- **核心想法**：把每個區間變成兩個事件、排序，然後沿著掃描過程帶著一個累計量。那個累計量是什麼 — 深度、權重、還是堆積 — 就是下面各組的分界
- **什麼時候用**：等母文件的平手決勝規則已經告訴你「起點和終點在同一座標時該怎麼排」之後再用，大部分掃描線的 bug 都藏在那裡


## 計算重疊數

### 1) Meeting Rooms II — LC 253 — 峰值計數 ⭐⭐⭐⭐⭐

> 起點發出 +1、終點發出 -1；把事件排序；同時併發的峰值 = 所需的最少房間數。

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

### 2) Divide Intervals Into Minimum Number of Groups — LC 2406 — LC 253 換個說法

> 最少組數 = 同時重疊的峰值。三種寫法：(1) 掃描線事件（+1/-1）、(2) 排序 + 優先佇列，當 `pq.peek() < start` 時重用該組（用嚴格的 `<` 是因為端點是閉區間）、(3) 座標範圍固定時用差分陣列。
>
> **關鍵陷阱**：`[1,5]` 和 `[5,10]` 是重疊的 — 優先佇列解法要用 `pq.peek() < start`（不是 `<=`），掃描線解法在同一時刻要把 start(+1) 排在 end(−1) 之前。
>
> **相似 LC**：253 Meeting Rooms II、1094 Car Pooling、2021 Brightest Position on Street、1854 Maximum Population Year、729/731/732 My Calendar I/II/III

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

### 3) My Calendar II — LC 731 — 追蹤重複預訂

> 只有當新預訂和一段已被重複預訂（double-booked）的區段重疊時才算不合法；否則就把這次重疊記錄下來。

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

## 加權掃描

### 4) Brightest Position on Street — LC 2021

> 在 p−r 發出 +1、在 p+r+1 發出 −1；追蹤累積亮度最大的位置。

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
    /** NOTE !!! apply EVERY delta at a coordinate before testing the max.
     *  The comparator leaves same-coordinate events unordered, so a +1 that happens to sort
     *  ahead of a -1 at the same x produces a transient brightness no position actually has —
     *  and that phantom peak is what gets returned.
     */
    for (int i = 0; i < events.size(); ) {
        int x = events.get(i)[0];
        while (i < events.size() && events.get(i)[0] == x) {
            brightness += events.get(i)[1];
            i++;
        }
        if (brightness > maxBrightness) { maxBrightness = brightness; ans = x; }
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

## 掃描線加上堆積

### 5) Maximum Number of Events That Can Be Attended — LC 1353 ⭐⭐⭐⭐


> 參考程式：`leetcode_python/Heap/maximum-number-of-events-that-can-be-attended.py`
>
> `events[i] = [start_i, end_i]`。可以在 `start_i <= d <= end_i` 的**任何一天** `d` 參加事件 `i`，但**每天只能參加一個**。回傳最多能參加幾個事件。
>
> ```text
> events = [[1,2],[2,3],[3,4]]        -> 3
> events = [[1,2],[2,3],[3,4],[1,2]]  -> 4
> ```

#### 核心想法

**沿時間往前掃描，貪婪地參加最早結束的那個事件（earliest-deadline-first）。**

這是一種掃描線，只是掃描過程會**消耗**資源而不只是計數：每一天就是一個時間格，所以每一次推進都必須選擇要把它花在*哪一個*開放中的事件上。

| 掃描線的組成 | 具體對應 |
|---|---|
| 事件流 | `events` 依**起點**排序 → 單一向前指標 `i` 讓每個事件只被 push 一次 |
| 掃描狀態 | `pq` = 目前開放中事件**結束日的 min heap** → `pq[0]` = 最急迫的截止期限 |
| 狀態清理 | `while pq and pq[0] < day: pop` → **延遲刪除**（堆積無法移除任意元素） |
| 消耗時間格 | `heappop(pq); ans += 1; day += 1` → 每天一個事件 |
| 掃描推進 | `if not pq: day = events[i][0]` → 跳過閒置的區段 |

每一步都照這個順序 — **PUSH → PURGE → ATTEND**：
1. **PUSH** 把所有 `start <= day` 的事件推進堆積。
2. **PURGE** 從堆頂清掉已過期的事件（`end < day`）。
3. **ATTEND** 參加 `pq[0]`（最早截止），然後 `day += 1`。

順序調換就會壞掉：先清理再 push，堆頂可能還留著過期的截止日；先參加再清理，可能會「參加」到已經過期的事件。

**為什麼要對 `end` 貪婪（而不是 `start`，也不是持續時間）？** 如果今天有兩個事件都開放，先挑較早結束的那個絕不會更差 — 較晚結束的那個剩下可安排的天數至少一樣多（交換論證）。

```text
events = [[1,4],[1,1]]     day 1: pq = [1, 4]
                           pop 1 ✅ -> day 2: pq = [4] -> attend        => 2
                           pop 4 ❌ -> day 2: pq = [1] already expired  => 1
```

**依起點排序 vs 依結束日建堆** — 這兩件事做的是不同的工作：**排序**決定*事件何時變得可見*，**堆積**決定*要把這一天花在哪個可見事件上*。

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

#### 模式：時間掃描 + 截止期限堆積

| 步驟 | 資料結構 | 目的 |
|------|---------------|---------|
| 依起點排序 | 陣列 + 指標 `i` | 事件依時間順序進入掃描；每個只 push 一次 |
| 追蹤開放集合 | `pq` = **結束**日的 min heap | `pq[0]` = 仍開放中、最急迫的截止期限 |
| 丟掉過期的 | `while pq[0] < day: pop` | **延遲刪除** — 堆積無法移除任意元素 |
| 消耗一個時間格 | pop `pq` + `day += 1` | 每天一個事件，貪婪地挑最急迫的 |
| 跳過閒置時間 | `if not pq: day = events[i][0]` | 去掉 O(天數範圍) 這個因子 |

**與計數型掃描（LC 253 / 2406）的差別：** 骨架一樣是「依起點排序」+「結束時間的 min heap」，但在那裡堆積的大小*本身就是*答案（有多少區間同時併發），而且什麼都不會被消耗。這裡的掃描每一格會花掉一個時間格，所以堆積存在的目的是回答**「要選哪一個？」**。

| | 計數型掃描（253、2406） | 截止期限堆積（1353） |
|---|---|---|
| 問題 | 峰值時有幾個重疊？ | 每格挑一個，最多能挑幾個？ |
| 堆積的角色 | 大小 = 併發數 | 堆頂 = 現在該服務誰 |
| 輸出 | `pq.size()` / 最大值計數器 | pop 的次數 |
| 區間語意 | 佔用**整個**區間 | 只佔用其中的**一天** |

#### 相似題目

| LC # | 題目 | 共通模式 | 關鍵差異 |
|------|---------|---------------|----------------|
| 1751 | Max Number of Events Attended II | 同樣的 events 輸入 | 事件佔用**整個**區間且帶有價值 → DP + 二分搜尋，**不是**掃描／堆積 |
| 253 | Meeting Rooms II | 依起點排序、結束時間的 min heap | 計算同時併發的區間數；不挑子集合 |
| 2406 | Divide Intervals Into Min Groups | 依起點排序、結束時間的 min heap | 253 的區間分割版說法（見 2-3） |
| 621 | Task Scheduler | 時間掃描，每格一個時間格 | 依頻率的 max heap + 冷卻佇列（不是截止期限） |
| 1834 | Single-Threaded CPU | 時間跳到下一個抵達點，推入已抵達的、pop 出最好的 | 依 (處理時間, 索引) 的 min heap；任務會佔用多個時間格 |
| 630 | Course Schedule III | 依截止期限貪婪 + 堆積 | max heap **替換**：超時就丟掉最長的那門課 |
| 767 | Reorganize String | 每個位置一個時間格，用堆積貪婪挑選 | 依剩餘次數的 max heap + 上次使用的檢查 |
| 502 | IPO | 依一個鍵排序、依另一個鍵建堆 | 雙堆積貪婪（資本 → 利潤的 max heap） |
| 871 | Min Number of Refueling Stops | 推入可達選項，貪婪地 pop 出最好的 | 油量的 max heap，卡住時才 pop |

> **交叉參考**：完整的堆積視角寫法見 [`heap_examples.md` § 7](./heap_examples.md#7-maximum-number-of-events-that-can-be-attended--lc-1353)

## 雙指標求交集

### 6) Interval List Intersections — LC 986


> 參考程式：`leetcode_python/Two_Pointers/interval-list-intersections.py`（V1-3 / V1-4）
>
> 兩個由**閉區間**組成、**已排序**、**兩兩不相交**的區間清單。回傳*同時*被兩份清單
> 覆蓋的所有區間。
>
> ```text
> firstList  = [[0,2],[5,10],[13,23],[24,25]]
> secondList = [[1,5],[8,12],[15,24],[25,26]]
> ->           [[1,2],[5,5],[8,10],[15,23],[24,24],[25,25]]
> ```
>
> 經典解法是雙指標（見 [`2_pointers.md` § 2-12](./2_pointers.md)）；本節講的是
> **掃描線**的視角，這個視角的推廣性好得多。

#### 核心想法

**把「兩份清單」這個結構丟掉。全部合併成一條事件流，然後輸出覆蓋數為 2 的區段。**

| 掃描線的組成 | 具體對應 |
|---|---|
| 事件 | 每個區間 `[s, e]` → `(s, START)` 與 `(e, END)`，**兩份**清單都放進同一個陣列 |
| 掃描狀態 | `active_count` = 目前有幾個區間覆蓋當前的 x |
| 交集**開始** | `active_count` 上升到 **2** → 記下 `start_pos = x` |
| 交集**結束** | `active_count == 2` 時遇到一個 END → 輸出 `[start_pos, x]` |
| 輸出 | 所有極大的 `coverage == 2` 區段 |

**為什麼 `== 2` 就是全部的訣竅**：在*同一份*清單裡區間兩兩不相交，所以在任何 x 上每份清單
對計數器的貢獻**最多是 1**。因此
`active_count == 2` ⟺ *每份清單各出一個區間*覆蓋 x ⟺ 交集。

> ⚠️ **這個捷徑的脆弱之處正好就在這裡。** 如果任一份清單內部可能自我重疊，
> `active_count == 2` 就可能代表「同一份清單裡的兩個區間」— 一個偽陽性。
> 穩健的寫法是**每份清單各一個計數器**，並檢查 `active_first > 0 and active_second > 0`
> （見下方的變形）。面試時要主動講出這一點；這正是他們想聽的追問。

**平手決勝 — 同一座標上 START 必須排在 END 之前。** 區間是**閉的**，
所以 `[0,2]` 和 `[2,7]` 在單點 `[2,2]` 上有交集。Python 的技巧：

```python
START, END = -1, 1        # -1 < 1  ->  plain events.sort() puts START first at ties
```

如果你反過來寫（`START = 1, END = -1` 配上單純的排序），計數器就會先掉到 1 再升回去，
於是每個像 `[5,5]` / `[24,24]` 這種單點交集都會漏掉。

**複雜度**：`O((m + n) log(m + n))` — 由排序主導；事件需要 `O(m + n)` 空間。
這**嚴格劣於雙指標的 O(m + n)** 解法，因為後者利用了兩個輸入*本來就已排序*這件事。
當這個前提不成立時，掃描線才是對的工具（見下方）。

#### 視覺追蹤

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

#### 寫法（Python）— 單一計數器、`== 2`

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

#### 寫法（Java）

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

#### 變形：每份清單各一個計數器（穩健，可推廣到「k 個集合取 AND」）

> 當某份清單可能**自我重疊**時（此時 `active_count == 2` 不再等價於「每份各出一個」），
> 或當你需要 `k` 份清單的交集時，就用這個寫法。

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

若有 `k` 份清單：維護 `active = [0] * k`，當 `min(active) > 0` 時開始一段交集
（或維護一個 `numPositive` 計數器，避免每個事件都做 O(k) 的檢查）。

#### 模式總結

| 目標 | 輸出時的覆蓋數條件 | 文件中的例子 |
|---|---|---|
| 兩份清單的**交集**（AND） | `count == 2`（或兩個各自的計數器都 > 0） | **LC 986（本節）** |
| **聯集**（OR）／合併 | `count` 由 `0 -> 1` 時開始，`-> 0` 時結束 | LC 56（模板 6） |
| **併發峰值** | 追蹤 `max(count)` | LC 253、2406（模板 1） |
| **k 重預訂衝突** | 當 `count >= k` 時拒絕 | LC 731、732（模板 4） |
| **空閒時間**（NOT） | 輸出 `count == 0` 的空隙 | LC 759 |

**同一個骨架，只差一行** — 那一行就是*對覆蓋數計數器的判斷式*。
認出這件事，整個區間題家族就收斂成單一個模板。

#### LC 986：掃描線 vs 雙指標

| | 雙指標（`2_pointers.md` § 2-12） | 掃描線（本節） |
|---|---|---|
| 時間 | **O(m + n)** ✅ | O((m+n) log(m+n)) — 排序 |
| 空間 | 額外 O(1) | O(m + n) 個事件 |
| 需要輸入已排序 | **需要**（兩份清單都要） | 不需要 — 排序會處理 |
| 需要輸入兩兩不相交 | 不需要 | 只有 `== 2` 捷徑需要；改用各自的計數器就能解除 |
| 推廣到 k 份清單 | 很彆扭（k 個指標、min-heap） | 很自然（`min(active) > 0`） |
| 面試時的答案 | 期待中的最佳解 | 「請推廣它」的答案 |

**經驗法則**：輸入已排序 + 剛好兩份清單 → **雙指標**。未排序、會自我重疊、
k 份清單，或題目變形成「聯集／空閒時間／峰值」→ **掃描線**。

#### 相似題目

| LC # | 題目 | 共通模式 | 關鍵差異 |
|------|---------|---------------|----------------|
| **986** | **Interval List Intersections** | **coverage == 2 掃描** | **兩份不相交有序清單的 AND** |
| 1229 | Meeting Scheduler | 同樣的交集掃描 | 回傳**第一個**長度 >= `duration` 的交集 |
| 759 | Employee Free Time | 同樣的合併事件流 | 輸出 `count == 0` 之處（補集／空隙） |
| 56 | Merge Intervals | 覆蓋數 0↔1 的轉換 | 求聯集而不是交集 |
| 57 | Insert Interval | 只插入一個新區間 | 三階段指標掃描，不需要事件流 |
| 253 | Meeting Rooms II | `+1/-1` 計數器 | 要的是 `max(count)`，不是那些範圍 |
| 2406 | Divide Intervals Into Min Groups | `+1/-1` 計數器、閉區間平手處理 | 峰值計數 = 最少組數（見 2-3） |
| 729 | My Calendar I | 重疊判斷 `max(s) < min(e)` | 線上插入，只要重疊就拒絕 |
| 731 / 732 | My Calendar II / III | 覆蓋數門檻 | 在 `count >= 2` 時輸出／拒絕，或追蹤最大 `count` |
| 715 | Range Module | 覆蓋數帳務 | 動態新增／移除／查詢範圍（有序 map） |
| 1288 | Remove Covered Intervals | 排序 + 掃描 | 丟掉被其他區間完全覆蓋的區間 |
| 850 | Rectangle Area II | 二維掃描 | 在每個 x 條帶上對 y 軸做交集邏輯 |
