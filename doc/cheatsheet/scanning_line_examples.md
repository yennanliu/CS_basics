# Sweep Line — Worked Examples

> **Scope** — The worked-solution archive behind [scanning_line.md](./scanning_line.md): six problems grouped by what the sweep is actually counting — overlap depth, a weighted sum, a heap of live jobs, or the intersection of two sorted lists.
> **See also**: [scanning_line.md](./scanning_line.md) — the parent sheet: eleven templates, the event-ordering and tie-break rules, and the pattern-selection strategy; [intervals.md](./intervals.md) — interval problems that do not need a sweep; [heap.md](./heap.md) — the structure behind the scheduling group; [difference_array.md](./difference_array.md) — the array counterpart to an event sweep; [greedy.md](./greedy.md) — why the deadline-heap schedule is optimal.

## LeetCode Problem Lists

- [Line Sweep](https://leetcode.com/problem-list/line-sweep/)
- [Interval](https://leetcode.com/tag/interval/)

## Overview

This is the long tail of [scanning_line.md](./scanning_line.md). The parent keeps the eleven
templates and — the part that actually decides correctness — the event-ordering and tie-break
rules; this file keeps the problems that *apply* them.

### Key Properties
- **Complexity**: O(n log n), dominated by sorting the events; the sweep itself is linear
- **Core Idea**: turn each interval into two events, sort them, and carry one running quantity across the sweep. What that quantity is — a depth, a weight, a heap — is what separates the groups below
- **When to Use**: after the parent's tie-break rules have told you how to order a start that coincides with an end, which is where most sweep bugs live


## Counting Overlap

### 1) Meeting Rooms II — LC 253 — the peak count ⭐⭐⭐⭐⭐

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

### 2) Divide Intervals Into Minimum Number of Groups — LC 2406 — LC 253 restated

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

### 3) My Calendar II — LC 731 — tracking double bookings

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

## Weighted Sweeps

### 4) Brightest Position on Street — LC 2021

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

## Sweep Plus a Heap

### 5) Maximum Number of Events That Can Be Attended — LC 1353 ⭐⭐⭐⭐


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

## Two-Pointer Intersection

### 6) Interval List Intersections — LC 986


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
