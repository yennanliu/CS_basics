# 進階堆積技巧

> **範圍** — 第一輪複習可以先跳過的堆積模式：延遲刪除、掃描線的「存活」堆積、後悔貪婪、資源池配置器、格子圖上的最佳優先搜尋，以及二元堆積之外的結構；六個必背模板留在母文件裡。
> **另見** — *母文件*：[heap.md](./heap.md) — 標準的 top-k／k 路合併／雙堆積／區間排程模板與模式選擇指南。*從同一份檔案拆出來的姊妹篇*：[heap_examples.md](./heap_examples.md) — 完整解過的 LC 題庫；[heap_language_apis.md](./heap_language_apis.md) — `heapq`／`PriorityQueue` API 參考。*鄰近文件*：[Dijkstra.md](./Dijkstra.md) — 這些格子圖搜尋所特化的優先佇列最短路徑演算法；[monotonic_queue.md](./monotonic_queue.md) — 什麼時候雙端佇列比延遲刪除堆積更好；[streaming_algorithms.md](./streaming_algorithms.md) — 無界串流上的 top-k。

## LeetCode 題目清單

- [Heap (Priority Queue)](https://leetcode.com/problem-list/heap-priority-queue/)

## 總覽

這裡每一個技巧的存在，都是因為二元堆積少了某個操作：它沒有 **decrease-key**、沒有**移除任意元素**，也沒辦法**修改先前的決定**。下面每個模式，都是用「只能 push、只能 pop 堆頂」來繞過這件事的不同做法。

### 關鍵性質
- **複雜度**：各模板分別標示；在攤還 pop 的前提下都是 `O(n log n)` 或 `O(n log k)`
- **核心想法**：永遠不要去搜尋堆積 — 直接 push 一筆新資料，而且只在**讀取**的時候清理**堆頂**
- **什麼時候用**：等到 [heap.md](./heap.md) 的模板已經變成反射動作之後；這些是 tier-4 的模式，也是「堆積題」和「困難堆積題」的分水嶺

### 題型分類

| 模式 | 題目敘述裡的特徵 | 代表題 |
|---|---|---|
| **延遲刪除** | 已經 push 進去的值後來*被改掉*或*被移除* | LC 3092、2349、2034、480、1825 |
| **掃描線 + 存活堆積** | *「在每個 x，求所有覆蓋 x 的區間之最大／最小值」* | LC 218、1851 |
| **有上限的後悔堆積** | *k 次免費機會* + 其餘部分的預算 | LC 1642、1792 |
| **帶後悔的貪婪** | 你要*到後來*才發現自己拿太多了 | LC 871、630、502 |
| **資源池** | 配置編號最小的空閒資源，並在已知時間釋放 | LC 1942、1606、1801、2073、2102 |
| **排序 + 固定大小堆積** | 目標式 = `sum(A) × max/min(B)` | LC 857、1383 |
| **格子圖最佳優先** | 展開*代價最小*的格子，而不是最近的 | LC 407、778、1631、1368、675 |
| **範圍跳躍的格子 DP** | 每個格子可以跳到一個*範圍*內的格子 | LC 2617 |
| **頻率唯一化** | 用最少刪除次數讓所有頻率互不相同 | LC 1647、1481 |
| **k 路合併的變形** | 合併*虛擬*或*巢狀*的有序來源 | LC 632、1439 |

## 模板與演算法

### 1) 延遲刪除 — 堆積 + 記錄真值的雜湊表 ⭐⭐⭐⭐⭐

**核心想法**

二元堆積能用 O(log n) 做 `push`／`pop-top`，但**不能**「更新某個元素」或「移除某個任意元素」— 光是找到它就要 O(n)。

所以當某個元素的鍵值變了，我們**完全不去動舊的那筆**。改成這樣：

```text
HEAP    = a bag of CANDIDATES   (some are stale / outdated)
HASHMAP = the SOURCE OF TRUTH   (current real value of each key)
```

一筆 `(value, key)` 是**過期的**，若且唯若 `value != hashmap[key]`。我們從不主動去獵捕過期資料；只清理**堆頂**，而且只在**讀取時**清：

```python
# the whole pattern in 3 lines
c_map[key] = new_value                          # 1. update truth
heapq.heappush(pq, (-new_value, key))           # 2. push new candidate (old one stays!)
while pq and -pq[0][0] != c_map[pq[0][1]]:      # 3. pop stale tops until top is valid
    heapq.heappop(pq)
```

> **注意！！** 我們**只**刪到**遇到第一筆計數正確的資料為止**。
> **堆積裡其他所有過期資料都原封不動** — 它們可能永遠不會被 pop 出來。
> 清理是*延遲*的：只為真正擋住答案的那些資料付出代價。
>
> ```text
> pq (max-heap by count):  [5:A]  [4:B]  [3:A]  [2:C]  [1:B]  ...
>                            ^stale (A is really 3 now)
>                            |
>            pop it ────────┘, now top = [4:B]
>                                          ^ valid? -> STOP. Done.
>                                            [3:A], [1:B] stay stale in the heap forever
>                                            (or until they bubble to the top someday)
> ```

**為什麼這樣是對的？**
- 如果堆頂是**有效的**，它就是真正的最大值 — 堆積裡其他資料都 ≤ 它，而且任何鍵的真值都不可能超過它自己最近一次 push 進去的那筆（那筆*就在*堆積裡）。
- 如果堆頂是**過期的**，那個鍵的真值一定存在堆積的別處（更新時我們有 push 進去），所以丟掉這份過期副本不會損失任何東西。

**為什麼這樣夠快？**
- 每次 push 最多產生**一筆**資料，而每筆資料在整趟執行中最多被 pop **一次** → 總 pop 次數 ≤ 總 push 次數 = O(n)。
- 每個操作攤還 **O(log n)**；堆積大小上界為 O(n)。

**Python 模板**

```python
# python
# IDEA: heap holds stale candidates; hashmap holds truth; clean top lazily
import heapq

class LazyMaxTracker:
    def __init__(self):
        self.truth = {}   # key -> current real value  (SOURCE OF TRUTH)
        self.pq = []      # max-heap of (-value, key)  (CANDIDATES, may be stale)

    def update(self, key, delta):
        # 1. update the truth
        self.truth[key] = self.truth.get(key, 0) + delta
        # 2. push new candidate -- do NOT remove the old entry
        heapq.heappush(self.pq, (-self.truth[key], key))

    def top(self):
        # 3. lazy delete: pop stale tops ONLY until the top is valid
        while self.pq and -self.pq[0][0] != self.truth[self.pq[0][1]]:
            heapq.heappop(self.pq)
        return -self.pq[0][0] if self.pq else 0
```

**Java 模板**

```java
// java
// IDEA: PriorityQueue of stale candidates + HashMap of truth
class LazyMaxTracker {
    Map<Integer, Long> truth = new HashMap<>();               // key -> real value
    // max-heap by value: long[]{value, key}
    PriorityQueue<long[]> pq =
        new PriorityQueue<>((a, b) -> Long.compare(b[0], a[0]));

    public void update(int key, long delta) {
        long v = truth.getOrDefault(key, 0L) + delta;
        truth.put(key, v);
        pq.offer(new long[]{v, key});                          // old entry left behind
    }

    public long top() {
        /** NOTE !!! pop stale tops ONLY until top is valid, then STOP */
        while (!pq.isEmpty() && pq.peek()[0] != truth.get((int) pq.peek()[1])) {
            pq.poll();
        }
        return pq.isEmpty() ? 0 : pq.peek()[0];
    }
}
```

**「過期」的三種口味** — 挑一個符合題目的失效判定：

| 口味 | 對 `pq[0]` 的過期判定 | 典型題目 |
|--------|----------------------|-----------------|
| **值不符**（雜湊表存真值） | `-pq[0][0] != c_map[pq[0][1]]` | LC 3092 Most Frequent IDs |
| **刪除集合／計數器** | `pq[0] in removed`（然後遞減） | LC 480 Sliding Window Median、LC 1825 MK Average |
| **因時間／索引而過期** | `pq[0].end < day` 或 `pq[0].idx <= i - k` | LC 1353 Max Events、LC 239 Sliding Window Max |

**容易踩到的坑**
- ⚠️ **在讀取時清，不要在寫入時清。** 每次 push 完就 pop，可能會丟掉你之後還要用的資料；讀取前才 pop 既正確又便宜。
- ⚠️ **用 `while`，不是 `if`。** 好幾筆過期資料可能疊在一起。
- ⚠️ **在 while 條件裡*以及*讀 `pq[0]` 前，都要檢查 `pq` 非空** — 這個集合是有可能合法地變空的（LC 3092 範例 2 → 答案 `0`）。
- ⚠️ **不要試圖去刪掉舊的那筆。** 那是 O(n) 搜尋，整個做法的意義就沒了。
- ⚠️ 就算相異的鍵只有幾個，堆積仍可能長到 O(n) 筆 — 這就是你用空間換速度付出的代價。

### 2) 掃描線 + 「存活」區間的最大堆積 ⭐⭐⭐⭐⭐

**核心想法**

這裡把堆積當成**目前存活值的多重集合**。我們沿著座標由左往右掃；每遇到一個事件，就*插入*剛剛開始存活的值、*延遲淘汰*區間已經結束的值，然後讀 `heap[0]` = 目前所有存活值裡的極值。

```text
HEAP  = (value, endCoordinate)   sorted by value
ALIVE = heap entries with endCoordinate > sweepPosition
```

區間結束時你不會去刪它 — 而是等它**浮到堆頂**、且 `end <= pos` 時才刪。這就是模板 8 的延遲刪除想法，只是把過期判定換成**「依座標過期」**。

**辨認特徵**：*「在每個 x，所有覆蓋 x 的區間的最大／最小值是多少？」*

**實例 — LC 218 The Skyline Problem**

每棟建築 `[L, R, H]` 在 `[L, R)` 上存活。天際線只有在最大存活高度變化時才改變，所以：掃過排序後的事件 x 座標，維護一個 `(H, R)` 的最大堆積，只要堆頂高度和上一次輸出的高度不同就輸出一個關鍵點。

```python
# python
# LC 218 - The Skyline Problem
# time = O(N log N), space = O(N)
# IDEA: sweep x; max-heap of (height, end); lazy-pop buildings whose end <= x; emit on height change
import heapq

class Solution(object):
    def getSkyline(self, buildings):
        # start event: (L, -H, R)   |   end event: (R, 0, 0)
        # NOTE !!! sorting on (-H) puts starts BEFORE ends at the same x,
        #          and taller starts before shorter starts
        events = [(L, -H, R) for L, R, H in buildings]
        events += list({(R, 0, 0) for _, R, _ in buildings})
        events.sort()

        res = [[0, 0]]                      # sentinel: ground level
        live = [(0, float('inf'))]          # max-heap of (-H, end); ground never expires

        for x, negH, R in events:
            # 1) LAZY EVICT: drop every alive entry that already ended
            while live[0][1] <= x:
                heapq.heappop(live)
            # 2) INSERT: only start events carry a height
            if negH:
                heapq.heappush(live, (negH, R))
            # 3) READ: top of heap = current skyline height
            if res[-1][1] != -live[0][0]:
                res.append([x, -live[0][0]])

        return res[1:]
```

```java
// java
// LC 218 - The Skyline Problem
// time = O(N log N), space = O(N)
// IDEA: sweep x; max-heap of {height, end}; lazy-pop ended buildings; emit on height change
public List<List<Integer>> getSkyline(int[][] buildings) {
    List<int[]> events = new ArrayList<>();
    for (int[] b : buildings) {
        events.add(new int[]{b[0], -b[2], b[1]});   // start: NEGATIVE height
        events.add(new int[]{b[1], 0, 0});          // end marker
    }
    // same x -> starts (neg) before ends (0); taller start first
    events.sort((a, b) -> a[0] != b[0] ? Integer.compare(a[0], b[0])
                                       : Integer.compare(a[1], b[1]));

    // max-heap of {height, end}; ground sentinel never expires
    PriorityQueue<int[]> live = new PriorityQueue<>((a, b) -> Integer.compare(b[0], a[0]));
    live.offer(new int[]{0, Integer.MAX_VALUE});

    List<List<Integer>> res = new ArrayList<>();
    int prevH = 0;

    for (int[] e : events) {
        int x = e[0];
        // 1) LAZY EVICT  (size > 1 protects the sentinel: R can be 2^31 - 1 == Integer.MAX_VALUE)
        while (live.size() > 1 && live.peek()[1] <= x) live.poll();
        // 2) INSERT
        if (e[1] < 0) live.offer(new int[]{-e[1], e[2]});
        // 3) READ
        int curH = live.peek()[0];
        if (curH != prevH) {
            res.add(Arrays.asList(x, curH));
            prevH = curH;
        }
    }
    return res;
}
```

**為什麼要放地面哨兵 `(0, ∞)`？** 它保證堆積永遠不空，所以 `live[0]` 隨時可讀 — 當最後一棟建築結束時，堆頂變成高度 `0`，我們就能正確輸出「天際線落回地面」的關鍵點。⚠️ 在 Java 中哨兵的 end 是 `Integer.MAX_VALUE`，而 LC 218 允許真實的 `R` 剛好等於它（`0 <= left < right <= 2^31 - 1`），所以淘汰迴圈必須加上 `live.size() > 1` 的保護 — 否則哨兵會被 pop 掉，下一次 `peek()` 就會在空堆積上 NPE。Python 的 `float('inf')` 則不需要保護。

**容易踩到的坑**
- ⚠️ **事件的同分處理就是整題的重點。** 在同一個 x 上，**先處理開始、再處理結束**（否則一棟建築剛好在另一棟結束處開始，就會產生假的凹陷），而且**較高的開始要排在較矮的開始前面**（否則會冒出假的階梯）。
- ⚠️ 結束事件要去重（`set(...)`），或乾脆接受重複 — 重複是無害的，因為輸出高度不會變兩次。
- ⚠️ 只有在高度**真的改變**時才輸出，否則會吐出多餘的關鍵點。

**變形 — LC 1851 Minimum Interval to Include Each Query**：用**離線**方式回答查詢。把區間依 `left` 排序、查詢依大小遞增排序；對每個查詢 `q`，把所有 `left <= q` 的區間以**區間長度** `(right-left+1, right)` 為鍵推進最小堆積，接著在 `heap[0].right < q` 時延遲 pop。堆頂就是覆蓋 `q` 的最小區間。一樣是插入、延遲淘汰、讀取這三步，只是掃描由查詢驅動，而不是由 x 座標驅動。

### 3) 有上限的「後悔」堆積 — 留下最好的 k 個，其餘照付 ⭐⭐⭐⭐

**核心想法**

你有 **k 次免費機會**（梯子、VIP 名額、一次性折扣）加上其餘部分的**預算**，而且必須在還沒看到未來成本前就**線上**做決定。訣竅是：

```text
Optimistically give EVERY cost a free pass.
Keep a MIN-HEAP of the costs currently holding a pass, capped at size k.
When the heap overflows -> the SMALLEST pass-holder is evicted and paid from the budget.
```

任何時刻堆積裡剛好是**目前為止最大的 k 個成本** — 這正好就是那段已處理前綴上，k 次免費機會的最佳分配。完全不需要回溯。

> 對照 **LC 630 Course Schedule III**（最大堆積*替換*：超支時淘汰**最大**的項目）— 一樣是「先承諾再後悔」的想法，比較器方向相反。這裡我們淘汰**最小的**，因為把免費機會用在便宜的項目上是浪費。

**實例 — LC 1642 Furthest Building You Can Reach**

每個往上的落差 `d = heights[i+1] - heights[i] > 0`，要嘛用一把梯子，要嘛用 `d` 塊磚。

```python
# python
# LC 1642 - Furthest Building You Can Reach
# time = O(N log L), space = O(L)   (L = ladders)
# IDEA: give every climb a ladder; when > L ladders are in use, downgrade the SMALLEST to bricks
import heapq

class Solution(object):
    def furthestBuilding(self, heights, bricks, ladders):
        ladder_jumps = []   # min-heap of the climbs currently using a ladder

        for i in range(len(heights) - 1):
            d = heights[i + 1] - heights[i]
            if d <= 0:
                continue                       # going down / flat is free

            heapq.heappush(ladder_jumps, d)    # optimistically use a ladder

            if len(ladder_jumps) > ladders:
                # NOTE !!! smallest climb loses its ladder and is paid with bricks
                bricks -= heapq.heappop(ladder_jumps)
                if bricks < 0:
                    return i                   # stuck standing on building i

        return len(heights) - 1
```

```java
// java
// LC 1642 - Furthest Building You Can Reach
// time = O(N log L), space = O(L)   (L = ladders)
// IDEA: min-heap of climbs holding a ladder, capped at L; evicted (smallest) climb costs bricks
public int furthestBuilding(int[] heights, int bricks, int ladders) {
    PriorityQueue<Integer> ladderJumps = new PriorityQueue<>();   // min-heap

    for (int i = 0; i + 1 < heights.length; i++) {
        int d = heights[i + 1] - heights[i];
        if (d <= 0) continue;

        ladderJumps.offer(d);

        if (ladderJumps.size() > ladders) {
            bricks -= ladderJumps.poll();       // smallest climb downgraded to bricks
            if (bricks < 0) return i;
        }
    }
    return heights.length - 1;
}
```

**容易踩到的坑**
- ⚠️ 磚塊用完時，要回傳 `i`（你**正站著**的那棟樓），不是 `i+1`。
- ⚠️ `ladders == 0` 也必須能跑：堆積在每次攀爬時立刻溢位，所以每次攀爬都用磚塊付 — 不需要特例處理。
- ⚠️ 非正的 `d` 要在 push **之前**就跳過，否則零或負的攀爬會白白佔用梯子。

**變形 — LC 1792 Maximum Average Pass Ratio**：堆積的鍵是**邊際增益**，不是原始值。把每個班級的 `(pass+1)/(total+1) - pass/total` 推進最大堆積；每個額外學生都分配給增益最大的班級，然後該班級用更新後的增益重新 push 回去。關鍵洞見：每個班級的增益單調遞減，所以貪婪地取堆頂就是最佳解。

### 4) 帶後悔的貪婪 — 撤銷過去最糟的決定 ⭐⭐⭐⭐

> **什麼時候用**：你必須一路往前掃並取走項目，而且要*到後來*才發現自己拿太多／拿太重。先樂觀地全部拿走，把拿過的東西放進堆積，等到超出預算時就**撤銷目前為止最糟的那個決定**（`poll()`）。形式上這是一個交換論證 — 換成最好的那個被延後的項目，永遠不會變差。

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

**變形：Course Schedule III（LC 630）** — 轉折：依**截止時間**排序，每門課都先修，一旦 `time > deadline` 就**丟掉目前為止修過最長的那門課**。丟掉最長的那門，永遠不會破壞先前那些截止時間的可行性。

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

```python
# python
# LC 630 - Course Schedule III
# IDEA: sort by deadline; take every course, and regret the longest one when it overruns
# time = O(n log n), space = O(n)
import heapq

def scheduleCourse(courses):
    courses.sort(key=lambda x: x[1])   # sort by deadline
    heap = []   # max-heap (store negated durations)
    time = 0
    for duration, deadline in courses:
        if time + duration <= deadline:
            time += duration
            heapq.heappush(heap, -duration)
        elif heap and -heap[0] > duration:
            # Replace longest course with current shorter one
            time += duration + heap[0]   # heap[0] is negative
            heapq.heapreplace(heap, -duration)
    return len(heap)
```

**關鍵洞見**：把較長的課換成較短的課，總時間絕不會增加，卻可能塞下更多課。

**變形：Furthest Building You Can Reach（LC 1642）** — 轉折：堆積裡放的是目前**分配給梯子**的那些攀爬（最小堆積）。一旦梯子用完，*最小*的那個已配梯攀爬就被降級成用磚塊 — 所以梯子最後一定落在最大的幾個攀爬上。程式碼見上面的 [3) 有上限的「後悔」堆積](#3-bounded-regret-heap--keep-the-k-best-pay-for-the-rest-)。

**變形：IPO（LC 502）** — 用兩個堆積而不是一個：一份依資本排序的*鎖定*清單，加上一個依利潤排序的*可用*最大堆積。把目前資金負擔得起的專案全部解鎖，然後只取當下最好的那個專案；重複 `k` 次。

```python
# python
# LC 502 - IPO
# IDEA: unlock projects by capital, then greedily take the highest profit available
# time = O(n log n), space = O(n)
import heapq

def findMaximizedCapital(k, w, profits, capital):
    # Min-heap by capital (locked projects)
    locked = sorted(zip(capital, profits))
    # Max-heap by profit (available projects, negate for max-heap)
    available = []
    i = 0
    for _ in range(k):
        # Unlock all projects we can afford
        while i < len(locked) and locked[i][0] <= w:
            heapq.heappush(available, -locked[i][1])
            i += 1
        if not available: break
        w += -heapq.heappop(available)   # pick highest profit
    return w
```

**重點觀察：**
- 這個模式的特徵：*「最少幾次 X」*／*「最多能做幾個 Y」*，而且某個決定可以**事後零成本地修改**。
- 堆積的方向就編碼了後悔的形式：想*撤銷最糟的／取走延後選項中最好的*時用**最大堆積**（LC 871、630）；想把某個有限的高級資源中*最便宜的降級*時用**最小堆積**（LC 1642）。
- 對照 [heap.md § 模板 4 區間排程](./heap.md#template-4-interval-scheduling-pattern--lc-253)：區間排程從不修改決定；後悔貪婪則整套建立在修改決定上。

### 5) 把兩個堆積當資源池用（空閒池 + 使用中池） ⭐⭐⭐⭐

**核心想法**

不是每個「兩個堆積」的題目都是中位數題。非常常見的一種變形是**資源配置器**，兩個堆積用**不同的鍵**排序：

```text
freeHeap = min-heap by RESOURCE ID     -> "which resource do I hand out next?"
busyHeap = min-heap by RELEASE TIME    -> "which resource comes back first?"
```

迴圈永遠是同樣三個步驟，而且順序就是這個：

```text
1. RELEASE : while busyHeap and busyHeap.top.releaseTime <= now:  move it to freeHeap
2. ASSIGN  : take freeHeap.top   (or mint a brand-new resource if the pool is empty)
3. OCCUPY  : push (releaseTime, resourceId) into busyHeap
```

**實例 — LC 1942 The Number of the Smallest Unoccupied Chair**

```python
# python
# LC 1942 - The Number of the Smallest Unoccupied Chair
# time = O(N log N), space = O(N)
# IDEA: free chairs = min-heap by chair id; occupied = min-heap by leaving time; release -> assign
import heapq

class Solution(object):
    def smallestChair(self, times, targetFriend):
        # process friends in ARRIVAL order, but remember original index
        order = sorted(range(len(times)), key=lambda i: times[i][0])

        free = []          # min-heap of chair ids
        busy = []          # min-heap of (leave_time, chair_id)
        next_chair = 0

        for i in order:
            arrive, leave = times[i]

            # 1) RELEASE : chair frees exactly AT leave time -> `<=`
            while busy and busy[0][0] <= arrive:
                _, c = heapq.heappop(busy)
                heapq.heappush(free, c)

            # 2) ASSIGN : smallest free id, else mint a new chair
            if free:
                chair = heapq.heappop(free)
            else:
                chair = next_chair
                next_chair += 1

            if i == targetFriend:
                return chair

            # 3) OCCUPY
            heapq.heappush(busy, (leave, chair))

        return -1
```

```java
// java
// LC 1942 - The Number of the Smallest Unoccupied Chair
// time = O(N log N), space = O(N)
// IDEA: free chairs = min-heap by id; busy chairs = min-heap by leaving time
public int smallestChair(int[][] times, int targetFriend) {
    int n = times.length;
    Integer[] order = new Integer[n];
    for (int i = 0; i < n; i++) order[i] = i;
    Arrays.sort(order, (a, b) -> Integer.compare(times[a][0], times[b][0]));   // by arrival

    PriorityQueue<Integer> free = new PriorityQueue<>();                    // chair ids
    PriorityQueue<int[]> busy = new PriorityQueue<>((a, b) -> Integer.compare(a[0], b[0])); // {leave, chair}
    int nextChair = 0;

    for (int i : order) {
        int arrive = times[i][0], leave = times[i][1];

        // 1) RELEASE
        while (!busy.isEmpty() && busy.peek()[0] <= arrive) {
            free.offer(busy.poll()[1]);
        }
        // 2) ASSIGN
        int chair = free.isEmpty() ? nextChair++ : free.poll();

        if (i == targetFriend) return chair;

        // 3) OCCUPY
        busy.offer(new int[]{leave, chair});
    }
    return -1;
}
```

**容易踩到的坑**
- ⚠️ **依抵達時間排序，但要保留原始索引** — 題目問的是 `targetFriend` 這位朋友，不是第 k 個抵達的人。
- ⚠️ 釋放步驟要用 `<=`：在時間 `t` 空出來的椅子，對同樣在 `t` 抵達的人是可用的。
- ⚠️ 先釋放**再**分配，否則你會生出根本不需要存在的椅子。

**這個模板的各種變形**：

| LC | 題目 | 轉折 |
|----|---------|-----------|
| 1606 | Find Servers That Handled Most Number of Requests | 空閒池必須從索引 `i % k` 開始**環狀**搜尋 — 用兩個空閒堆積（編號 `>= i%k` 與編號 `< i%k`），或用附 `ceiling()` 的 `TreeSet`。使用中堆積照舊以結束時間為鍵。 |
| 1801 | Number of Orders in the Backlog | 兩個**方向相反**、會互相吃掉對方的堆積：`buy` 是依價格的最大堆積，`sell` 是依價格的最小堆積。每筆新訂單在價格交叉時就跟另一個堆積撮合，剩下的再 push 進去。 |
| 2102 | Sequentially Ordinal Rank Tracker | 兩個堆積以查詢指標為界把串流切開：最大堆積放「已回傳／更好的」，最小堆積放其餘；每次 `get()` 把一個元素跨過邊界搬過去。 |
### 6) 依一個標準排序 + 對另一個標準用固定大小堆積
> **什麼時候用**：目標式是兩個屬性的乘積／組合，例如 `cost = (選中的 k 個的 A 總和) * (選中的 k 個的 B 最大值)`。**依 B 排序**，這樣走訪時就固定住「B 的最大值」這個因子，再用一個大小為 `k` 的堆積在 A 上求最小／最大總和。這是 [Kth Element 模板](./heap.md#1-kth-element-template)的雙屬性版本。

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

**變形：Maximum Performance of a Team（LC 1383）** — 形狀相同、方向相反：依**效率遞減**排序（當前效率就是團隊最小值），維護一個大小 `k`、放速度的**最小堆積**，讓總和最大化。
LC 1383 的程式碼：[heap_examples.md § Maximum Performance of a Team](./heap_examples.md#12-maximum-performance-of-a-team--lc-1383)。

**重點觀察：**
- **排序固定住乘法因子，堆積最佳化加法因子。** 認出該依哪個屬性排序，就是整題的關鍵。
- 取模數只能放在最後（LC 1383）— 在迴圈裡取模會破壞 `max` 的比較。
- 堆積方向必須跟你要保留的東西*相反*：要留最小的 k 個 → 最大堆積；要留最大的 k 個 → 最小堆積（跟 [Kth Element 模板](./heap.md#1-kth-element-template)是同一條規則）。
### 7) 格子圖上的最小堆積最佳優先搜尋
> **什麼時候用**：在格子圖上，下一個要展開的格子不是步數最近的，而是**目前為止代價最低的** — 也就是把格子圖當成隱式圖跑 Dijkstra（戴克斯特拉）。模板 5 假設有鄰接串列；這個變形則是從邊界播種堆積、往內展開。

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

**重點觀察：**
- 之所以貪婪是正確的，關鍵在於每次 pop **全域最低的邊界格子**：水只能從最矮的牆溢出，所以那個格子的水位就已經定案。
- `seen` 要在 **push 時**標記，不是 pop 時，否則同一格會被重複排進佇列。
- 骨架相同，只差優先度的鍵：

| LC | 題目 | 推進堆積的優先度鍵 |
|----|---------|-----------------------------------|
| 407 | Trapping Rain Water II | `max(current level, neighbour height)` — 有效牆高 |
| 778 | Swim in Rising Water | `max(current level, neighbour height)` — 最小化路徑上的*最大*格子 |
| 1631 | Path With Minimum Effort | `max(current effort, abs(height diff))` — minimax 邊權 |
| 1368 | Minimum Cost to Make at Least One Valid Path in a Grid | `cost + (0 if arrow points at neighbour else 1)` — 0/1 權重（用雙端佇列也可以） |
| 675 | Cut Off Trees for Golf Event | 樹必須**由矮到高**砍：把目標依高度排序或放堆積，再在相鄰目標之間跑 BFS |
### 8) 帶範圍跳躍的格子圖最短路徑
```java
/**
 * Template for Grid Shortest Path with Variable Jump Ranges
 *
 * Pattern: DP + Per-row/column Priority Queues with Lazy Deletion
 *
 * Problem Type: From (0,0), each cell (i,j) can jump to:
 *   - Right: (i, k) where j < k <= j + grid[i][j]
 *   - Down:  (k, j) where i < k <= i + grid[i][j]
 * Find minimum cells to reach (m-1, n-1)
 *
 * Key Insight: Standard BFS would be O(N²) per cell; PQ reduces to O(log N)
 *
 * Time: O(M*N*log(M+N))
 * Space: O(M*N)
 */
public int gridShortestPathTemplate(int[][] grid) {
    int m = grid.length, n = grid[0].length;
    int[][] dist = new int[m][n];
    for (int[] row : dist) Arrays.fill(row, -1);

    // Create one PQ per row and one PQ per column
    // Each PQ stores {distance, index} sorted by distance
    PriorityQueue<int[]>[] rowPQs = new PriorityQueue[m];
    PriorityQueue<int[]>[] colPQs = new PriorityQueue[n];

    for (int i = 0; i < m; i++)
        rowPQs[i] = new PriorityQueue<>((a, b) -> Integer.compare(a[0], b[0]));
    for (int j = 0; j < n; j++)
        colPQs[j] = new PriorityQueue<>((a, b) -> Integer.compare(a[0], b[0]));

    dist[0][0] = 1;  // Starting cell counts as 1

    for (int i = 0; i < m; i++) {
        for (int j = 0; j < n; j++) {

            // 1. Check cells from same row that can reach (i,j)
            while (!rowPQs[i].isEmpty()) {
                int[] top = rowPQs[i].peek();
                int prevCol = top[1];
                // Can previous cell jump far enough to reach column j?
                if (prevCol + grid[i][prevCol] >= j) {
                    int d = top[0] + 1;
                    if (dist[i][j] == -1 || d < dist[i][j])
                        dist[i][j] = d;
                    break;  // First valid = best (PQ sorted by distance)
                }
                // Lazy deletion: cell can never reach future columns
                rowPQs[i].poll();
            }

            // 2. Check cells from same column that can reach (i,j)
            while (!colPQs[j].isEmpty()) {
                int[] top = colPQs[j].peek();
                int prevRow = top[1];
                if (prevRow + grid[prevRow][j] >= i) {
                    int d = top[0] + 1;
                    if (dist[i][j] == -1 || d < dist[i][j])
                        dist[i][j] = d;
                    break;
                }
                colPQs[j].poll();
            }

            // 3. Add current cell to PQs for future cells
            if (dist[i][j] != -1 && grid[i][j] > 0) {
                rowPQs[i].offer(new int[]{dist[i][j], j});
                colPQs[j].offer(new int[]{dist[i][j], i});
            }
        }
    }

    return dist[m - 1][n - 1];
}
```

```python
# Python Template: Grid Shortest Path with Range Jumps
import heapq

def grid_shortest_path(grid):
    """
    Pattern: DP + Per-row/column heaps with lazy deletion

    Key insight: Each cell can be processed once per row/column heap,
    and expired cells are removed lazily when encountered.
    """
    m, n = len(grid), len(grid[0])
    dist = [[-1] * n for _ in range(m)]

    # One min-heap per row and per column
    # Each heap stores (distance, index)
    row_pqs = [[] for _ in range(m)]
    col_pqs = [[] for _ in range(n)]

    dist[0][0] = 1

    for i in range(m):
        for j in range(n):
            # Check row heap for cells that can reach (i, j)
            while row_pqs[i]:
                d, prev_col = row_pqs[i][0]
                if prev_col + grid[i][prev_col] >= j:
                    if dist[i][j] == -1 or d + 1 < dist[i][j]:
                        dist[i][j] = d + 1
                    break
                heapq.heappop(row_pqs[i])  # Lazy deletion

            # Check column heap for cells that can reach (i, j)
            while col_pqs[j]:
                d, prev_row = col_pqs[j][0]
                if prev_row + grid[prev_row][j] >= i:
                    if dist[i][j] == -1 or d + 1 < dist[i][j]:
                        dist[i][j] = d + 1
                    break
                heapq.heappop(col_pqs[j])

            # Add current cell to heaps if reachable and can jump
            if dist[i][j] != -1 and grid[i][j] > 0:
                heapq.heappush(row_pqs[i], (dist[i][j], j))
                heapq.heappush(col_pqs[j], (dist[i][j], i))

    return dist[m - 1][n - 1]
```

### 9) 頻率唯一化 — 貪婪 + 堆積／HashSet
```python
def make_frequencies_unique(s):
    """
    Pattern: Make all character frequencies unique with minimum deletions
    Used in: LC 1647, LC 1481
    """
    from collections import Counter
    import heapq

    # Approach 1: Max Heap (process high to low)
    def heap_approach():
        freq_count = Counter(s)
        max_heap = [-f for f in freq_count.values()]
        heapq.heapify(max_heap)

        deletions = 0
        while len(max_heap) > 1:
            top = -heapq.heappop(max_heap)
            next_val = -max_heap[0]

            if top == next_val:
                top -= 1
                deletions += 1
                if top > 0:
                    heapq.heappush(max_heap, -top)

        return deletions

    # Approach 2: HashSet (track used frequencies)
    def hashset_approach():
        freq_count = Counter(s)
        used_freq = set()
        deletions = 0

        for freq in freq_count.values():
            # Decrement until finding unused frequency
            while freq > 0 and freq in used_freq:
                freq -= 1
                deletions += 1
            used_freq.add(freq)

        return deletions

    # Approach 3: Sorting (ensure strictly decreasing)
    def sort_approach():
        freq = [0] * 26
        for c in s:
            freq[ord(c) - ord('a')] += 1

        freq.sort(reverse=True)
        deletions = 0

        for i in range(len(freq) - 1):
            if freq[i] == 0:
                break
            if freq[i] <= freq[i + 1]:
                prev = freq[i + 1]
                freq[i + 1] = max(0, freq[i] - 1)
                deletions += prev - freq[i + 1]

        return deletions

    # Best approach depends on constraints
    return hashset_approach()  # Generally most intuitive
```

最大堆積做法的 Java 版本，以及排序與 `HashSet` 兩種替代解：
[heap_examples.md § Minimum Deletions to Make Character Frequencies Unique](./heap_examples.md#11-minimum-deletions-to-make-character-frequencies-unique--lc-1647)。

### 10) 帶去重的堆積
```python
def solve_with_unique_heap(nums):
    import heapq

    heap = []
    seen = set()

    for num in nums:
        if num not in seen:
            heapq.heappush(heap, num)
            seen.add(num)

    return heap
```

### 11) k 路合併的各種變形

**模板 2 的變形（骨架相同，只是簿記方式不同）：**

| LC | 題目 | k 路合併上的轉折 |
|----|---------|--------------------------|
| 632 | Smallest Range Covering Elements from K Lists | 額外追蹤 k 個堆積元素的**最大值**；每次 pop 都給出一個涵蓋所有清單的視窗 `[heap_min, running_max]` — 任一清單耗盡就停 |
| 355 | Design Twitter | 這裡的「k 個有序清單」是被追蹤者的推文清單（新的在前）；把每位被追蹤者的第一則推進以時間戳為鍵的最大堆積，pop 10 次 |
| 373 / 378 | K Pairs with Smallest Sums／Kth Smallest in Sorted Matrix | 清單是有序格子中的**虛擬**列 — 見 [heap_examples.md § LC 373](./heap_examples.md#9-find-k-pairs-with-smallest-sums--lc-373)／[§ LC 378](./heap_examples.md#10-kth-smallest-element-in-a-sorted-matrix--lc-378) |

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

**變形 — LC 1439 Find the Kth Smallest Sum of a Matrix With Sorted Rows**：不要去合併*各列*，而是**一列一列**地合併。維護一份由前 `r` 列組成的、最小的 k 個和的清單，再用跟 LC 373 相同的「k 個最小配對」技巧把它和第 `r+1` 列合併（最小堆積以 `(prev[0] + row[0], i=0, j=0)` 播種，展開 `(i+1, j)`／`(i, j+1)`，pop k 次後停）。這把指數級的搜尋降到 `O(m * k log k)`。

### 12) 延遲刪除 — 「刪除計數器」口味

當你沒辦法從堆積裡移除任意元素時，就把它標記成無效，pop 的時候跳過。

> 這是**「刪除計數器」**口味。至於**「雜湊表存真值」**口味（值被改掉而非元素被移除），見 [1) 延遲刪除](#1-lazy-deletion--heap--hashmap-of-truth-) 以及 [heap_examples.md § Most Frequent IDs](./heap_examples.md#18-most-frequent-ids--lc-3092)。

```python
import heapq

class LazyHeap:
    def __init__(self):
        self.heap = []
        self.removed = {}   # val -> count of removed instances

    def push(self, val):
        heapq.heappush(self.heap, val)

    def remove(self, val):
        self.removed[val] = self.removed.get(val, 0) + 1

    def pop(self):
        while self.heap:
            val = self.heap[0]
            if self.removed.get(val, 0) > 0:
                heapq.heappop(self.heap)
                self.removed[val] -= 1
            else:
                return heapq.heappop(self.heap)
        return None

# Used in: LC 480 Sliding Window Median, LC 1825 Finding MK Average
```


## 二元堆積之外

### 索引式優先佇列
- 支援 decrease-key 操作
- 對 Dijkstra 的最佳化很有用
- 需要追蹤元素在堆積中的位置

### Fibonacci 堆積
- 插入與 decrease-key 攤還 O(1)
- extract-min 為 O(log n)
- 實作複雜，實務上很少用

### 二元堆積的變種
- d 元堆積：快取效能較好
- 二項式堆積：合併操作較好
- 配對堆積：實作簡單，實務效能不錯

## 總結與速查

| 題目裡的訊號 | 模式 | 章節 |
|---|---|---|
| push 進去的值被改掉／被移除 | 候選堆積 + 記錄真值的雜湊表 | [1](#1-lazy-deletion--heap--hashmap-of-truth-) |
| 「所有覆蓋 x 的東西的最大／最小值」 | 掃描線 + 存活堆積，依座標淘汰 | [2](#2-sweep-line--max-heap-of-alive-intervals-) |
| 「k 把梯子／k 次免費升級」 | 上限為 k 的最小堆積，被淘汰的照付 | [3](#3-bounded-regret-heap--keep-the-k-best-pay-for-the-rest-) |
| 「最少停幾次／最多修幾門課」 | 全部先拿，卡住時 `poll()` 掉最糟的 | [4](#4-greedy-with-regret--undo-the-worst-past-decision-) |
| 「編號最小的空椅子／伺服器」 | 依編號的空閒堆積 + 依釋放時間的使用中堆積 | [5](#5-two-heaps-as-resource-pools-free-pool--busy-pool-) |
| 目標式是 `sum(A) × max/min(B)` | 依 B 排序，對 A 用大小 k 的堆積 | [6](#6-sort-by-one-criterion--fixed-size-heap-on-the-other) |
| 格子圖，代價是 minimax 或累加權重 | 在隱式格子圖上跑 Dijkstra | [7](#7-min-heap-best-first-search-on-a-grid) |
| 每個格子跳到一個*範圍* | 逐列／逐行的優先佇列 + 延遲 pop | [8](#8-grid-shortest-path-with-range-jumps) |
| 「讓所有頻率互不相同」 | 最大堆積遞減，或用一個已用頻率集合 | [9](#9-frequency-uniqueness--greedy--heap--hashset) |
| 合併*虛擬*／*巢狀*的有序來源 | 會重建前緣的 k 路合併 | [11](#11-k-way-merge-variants) |

| 訊號 | 模式 |
|--------|---------|
| 「第 k 大／第 k 小」 | 大小為 k 的最小堆積 |
| 「出現頻率前 k 高」 | Counter + nlargest／桶排序 |
| 「串流的中位數」 | 兩個堆積（最大 + 最小） |
| 「在限制下總是取當前最好的」 | 貪婪 + 最大堆積（IPO 模式） |
| 「塞下最多的任務／課程」 | 依截止時間排序 + 替換式堆積 |
| 「從堆積中移除任意元素」 | 延遲刪除 |
| 「合併 k 個有序串列」 | 以 (val, list_idx, elem_idx) 為鍵的最小堆積 |

### 複雜度比較
| 操作 | 二元堆積 | 有序陣列 | AVL 樹 |
|-----------|------------|-------------|---------|
| 插入 | O(log n) | O(n) | O(log n) |
| 刪除最小／最大 | O(log n) | O(1) | O(log n) |
| 查看最小／最大 | O(1) | O(1) | O(log n) |
| 搜尋任意元素 | O(n) | O(log n) | O(log n) |
| 從陣列建堆 | O(n) | O(n log n) | O(n log n) |
