# Advanced Heap Techniques

> **Scope** — The heap patterns a first pass should skip: lazy deletion, sweep-line "alive" heaps, regret greedy, resource-pool allocators, grid best-first search, and the structures beyond a plain binary heap; the six must-know templates stay in the parent sheet.
> **See also** — *parent sheet*: [heap.md](./heap.md) — the canonical top-k / k-way-merge / two-heap / interval-scheduling templates and the pattern-selection guide. *Siblings split out of the same file*: [heap_examples.md](./heap_examples.md) — the worked LC solution archive; [heap_language_apis.md](./heap_language_apis.md) — `heapq` / `PriorityQueue` API reference. *Neighbouring sheets*: [Dijkstra.md](./Dijkstra.md) — the PQ shortest-path algorithm these grid searches specialise; [monotonic_queue.md](./monotonic_queue.md) — when a deque beats a lazy heap; [streaming_algorithms.md](./streaming_algorithms.md) — top-k over an unbounded stream.

## LeetCode Problem Lists

- [Heap (Priority Queue)](https://leetcode.com/problem-list/heap-priority-queue/)

## Overview

Every technique here exists because a binary heap is missing an operation: it has no
**decrease-key**, no **remove-arbitrary**, and no way to **revise a past decision**. Each pattern
below is a different way of working around that with pushes and top-of-heap pops only.

### Key Properties
- **Complexity**: stated per template; all are `O(n log n)` or `O(n log k)` with amortised pops
- **Core Idea**: never search the heap — push a new entry and clean only the **top**, only at
  **read** time
- **When to Use**: after the [heap.md](./heap.md) templates are automatic; these are the tier-4
  patterns that separate a "heap problem" from a "hard heap problem"

### Problem Categories

| Pattern | Signature in the problem statement | Anchor problems |
|---|---|---|
| **Lazy deletion** | a pushed value later *changes* or *is removed* | LC 3092, 2349, 2034, 480, 1825 |
| **Sweep + alive heap** | *"at every x, the max/min over all intervals covering x"* | LC 218, 1851 |
| **Bounded regret heap** | *k free passes* + a budget for everything else | LC 1642, 1792 |
| **Greedy with regret** | you only learn you overcommitted *later* | LC 871, 630, 502 |
| **Resource pools** | allocate the smallest free id, release at a known time | LC 1942, 1606, 1801, 2073, 2102 |
| **Sort + fixed-size heap** | objective = `sum(A) × max/min(B)` | LC 857, 1383 |
| **Grid best-first** | expand the *cheapest* cell, not the nearest | LC 407, 778, 1631, 1368, 675 |
| **Range-jump grid DP** | each cell jumps to a *range* of cells | LC 2617 |
| **Frequency uniqueness** | make all frequencies distinct with min deletions | LC 1647, 1481 |
| **K-way merge variants** | merge *virtual* or *nested* sorted sources | LC 632, 1439 |

## Templates & Algorithms

### 1) Lazy Deletion — Heap + HashMap of Truth ⭐⭐⭐⭐⭐

**Core Idea**

A binary heap supports `push` / `pop-top` in O(log n), but **not** "update this element" or
"remove that arbitrary element" — finding it alone is O(n).

So when an element's key changes, we **don't touch the old entry**. Instead:

```text
HEAP    = a bag of CANDIDATES   (some are stale / outdated)
HASHMAP = the SOURCE OF TRUTH   (current real value of each key)
```

An entry `(value, key)` is **stale** iff `value != hashmap[key]`. We never hunt for stale entries;
we only clean the **top**, and only **at read time**:

```python
# the whole pattern in 3 lines
c_map[key] = new_value                          # 1. update truth
heapq.heappush(pq, (-new_value, key))           # 2. push new candidate (old one stays!)
while pq and -pq[0][0] != c_map[pq[0][1]]:      # 3. pop stale tops until top is valid
    heapq.heappop(pq)
```

> **NOTE !!!** we ONLY delete **until we reach a correct-count one**.
> We leave **ALL other stale entries in the heap untouched** — they may never be popped at all.
> Cleaning is *lazy*: pay the cost only for the entries that actually block the answer.
>
> ```
> pq (max-heap by count):  [5:A]  [4:B]  [3:A]  [2:C]  [1:B]  ...
>                            ^stale (A is really 3 now)
>                            |
>            pop it ────────┘, now top = [4:B]
>                                          ^ valid? -> STOP. Done.
>                                            [3:A], [1:B] stay stale in the heap forever
>                                            (or until they bubble to the top someday)
> ```

**Why is this correct?**
- If the top is **valid**, it is the true max — every other entry in the heap is ≤ it, and no
  key's true value can exceed its own most-recently-pushed entry (which *is* in the heap).
- If the top is **stale**, its key's true value is stored elsewhere in the heap (we pushed it on
  update), so discarding the stale copy loses nothing.

**Why is this fast?**
- Each push creates **at most one** entry, and each entry is popped **at most once** over the
  entire run → total pops ≤ total pushes = O(n).
- Amortized **O(log n)** per operation; heap size bounded by O(n).

**Python template**

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

**Java template**

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

**Three flavors of "stale"** — pick the invalidation test that fits the problem:

| Flavor | Stale test on `pq[0]` | Typical problem |
|--------|----------------------|-----------------|
| **Value mismatch** (hashmap of truth) | `-pq[0][0] != c_map[pq[0][1]]` | LC 3092 Most Frequent IDs |
| **Deleted set / counter** | `pq[0] in removed` (then decrement) | LC 480 Sliding Window Median, LC 1825 MK Average |
| **Expired by time / index** | `pq[0].end < day` or `pq[0].idx <= i - k` | LC 1353 Max Events, LC 239 Sliding Window Max |

**Gotchas**
- ⚠️ **Clean at READ time, not write time.** Popping after every push may throw away entries you
  still need; popping before reading is both correct and cheaper.
- ⚠️ **`while`, not `if`.** Several stale entries can pile up on top of each other.
- ⚠️ **Guard `pq` non-empty** inside the while condition *and* before reading `pq[0]` — the
  collection can legitimately become empty (LC 3092 example 2 → answer `0`).
- ⚠️ **Don't try to delete the old entry.** That's O(n) search and defeats the whole point.
- ⚠️ Heap can grow to O(n) entries even if only a few distinct keys exist — that's the space
  you trade for the speed.

### 2) Sweep Line + Max Heap of "Alive" Intervals ⭐⭐⭐⭐⭐

**Core Idea**

The heap is used as a **multiset of currently-alive values**. We sweep a coordinate left→right; at
each event we *insert* the value that just became alive, *lazily evict* values whose interval already
ended, and read `heap[0]` = the current extreme among everything alive.

```text
HEAP  = (value, endCoordinate)   sorted by value
ALIVE = heap entries with endCoordinate > sweepPosition
```

You never delete an interval when it ends — you delete it **when it surfaces at the top** and its
`end <= pos`. This is the lazy-deletion idea from Template 8 with **"expired by coordinate"** as the
staleness test.

**Signature to recognize**: *"at every x, what is the max/min over all intervals covering x?"*

**Worked example — LC 218 The Skyline Problem**

Each building `[L, R, H]` is alive on `[L, R)`. The skyline changes exactly when the max alive
height changes, so: sweep the sorted event x-coordinates, keep a max heap of `(H, R)`, and emit a
key point whenever the top height differs from the previously emitted height.

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

**Why the ground sentinel `(0, ∞)`?** It guarantees the heap is never empty, so `live[0]` is always
readable — when the last building ends, the top becomes height `0` and we correctly emit the
"skyline drops to ground" key point. ⚠️ In Java the sentinel end is `Integer.MAX_VALUE`, and LC 218
allows a real `R` to equal it (`0 <= left < right <= 2^31 - 1`), so the eviction loop must be guarded
with `live.size() > 1` — otherwise the sentinel is popped and the next `peek()` NPEs on an empty heap.
Python's `float('inf')` needs no guard.

**Gotchas**
- ⚠️ **Event tie-breaking is the whole problem.** At a shared x, process **starts before ends**
  (otherwise a building that starts exactly where another ends produces a spurious dip), and
  **taller starts before shorter starts** (otherwise a spurious step appears).
- ⚠️ Deduplicate end events (`set(...)`) or just accept duplicates — they are harmless because the
  emitted height won't change twice.
- ⚠️ Only emit when the height **actually changes**, otherwise you output redundant key points.

**Variation — LC 1851 Minimum Interval to Include Each Query**: answer queries **offline**. Sort
intervals by `left` and queries ascending; for each query `q` push every interval with `left <= q`
into a min-heap keyed by **interval length** `(right-left+1, right)`, then lazy-pop while
`heap[0].right < q`. The top is the smallest interval covering `q`. Same three steps — insert,
lazy evict, read — with the sweep driven by queries instead of x-coordinates.

### 3) Bounded "Regret" Heap — Keep the k Best, Pay for the Rest ⭐⭐⭐⭐

**Core Idea**

You have **k free passes** (ladders, VIP slots, one-time discounts) and a **budget** for everything
else, and you must decide **online**, before seeing future costs. The trick:

```text
Optimistically give EVERY cost a free pass.
Keep a MIN-HEAP of the costs currently holding a pass, capped at size k.
When the heap overflows -> the SMALLEST pass-holder is evicted and paid from the budget.
```

At any moment the heap holds exactly the **k largest costs seen so far** — which is precisely the
optimal assignment of the k free passes for the prefix processed so far. No backtracking needed.

> Contrast with **LC 630 Course Schedule III** (max-heap *replace*: evict the **largest** item when
> you overrun) — same "commit then regret" idea, opposite comparator. Here we evict the **smallest**,
> because a free pass is wasted on a cheap item.

**Worked example — LC 1642 Furthest Building You Can Reach**

Each upward step `d = heights[i+1] - heights[i] > 0` costs either one ladder or `d` bricks.

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

**Gotchas**
- ⚠️ Return `i` (the building you are **standing on**), not `i+1`, when the bricks run out.
- ⚠️ `ladders == 0` must still work: the heap overflows immediately on every climb, so every climb
  is paid with bricks — no special case needed.
- ⚠️ Skip non-positive `d` **before** pushing, otherwise zero/negative climbs occupy ladders.

**Variation — LC 1792 Maximum Average Pass Ratio**: the heap is keyed by **marginal gain**, not by
raw value. Push `(pass+1)/(total+1) - pass/total` for each class into a max heap; each extra student
goes to the class with the biggest gain, then that class is re-pushed with its updated gain. Key
insight: the gain is monotonically decreasing per class, so a greedy heap pick is optimal.

### 4) Greedy with Regret — Undo the Worst Past Decision ⭐⭐⭐⭐

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

**Key insight**: Replacing a longer course with a shorter one never increases total time but may
allow fitting more courses.

**Variation: Furthest Building You Can Reach (LC 1642)** — twist: the heap holds the climbs currently **assigned to ladders** (min-heap). Once ladders run out, the *smallest* laddered climb is demoted to bricks — so ladders always end up on the largest climbs. Code: [3) Bounded "Regret" Heap](#3-bounded-regret-heap--keep-the-k-best-pay-for-the-rest-) above.

**Variation: IPO (LC 502)** — two heaps instead of one: a *locked* list sorted by capital and an
*available* max-heap by profit. Unlock everything you can currently afford, then take the single
best available project; repeat `k` times.

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

**Key Observations:**
- Signature of the pattern: *"minimum number of X"* / *"maximum count of Y"* where a decision can be **revised later at no cost**.
- Heap direction encodes the regret: **max-heap** when you want to *undo the worst / take the best deferred option* (LC 871, 630); **min-heap** when you want to *demote the cheapest of a limited premium resource* (LC 1642).
- Contrast with [heap.md § Template 4 Interval Scheduling](./heap.md#template-4-interval-scheduling-pattern--lc-253): interval scheduling never revises a decision; regret-greedy is built entirely on revising them.

### 5) Two Heaps as Resource Pools (free pool + busy pool) ⭐⭐⭐⭐

**Core Idea**

Not every "two heaps" problem is a median problem. A very common variant is a **resource
allocator**, where the two heaps are ordered by *different* keys:

```text
freeHeap = min-heap by RESOURCE ID     -> "which resource do I hand out next?"
busyHeap = min-heap by RELEASE TIME    -> "which resource comes back first?"
```

The loop is always the same three steps, in this exact order:

```text
1. RELEASE : while busyHeap and busyHeap.top.releaseTime <= now:  move it to freeHeap
2. ASSIGN  : take freeHeap.top   (or mint a brand-new resource if the pool is empty)
3. OCCUPY  : push (releaseTime, resourceId) into busyHeap
```

**Worked example — LC 1942 The Number of the Smallest Unoccupied Chair**

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

**Gotchas**
- ⚠️ **Sort by arrival, but keep the original index** — the answer is asked about friend
  `targetFriend`, not about the k-th arrival.
- ⚠️ `<=` in the release step: a chair vacated at time `t` is available to someone arriving at `t`.
- ⚠️ Release **before** assigning, or you mint chairs that did not need to exist.

**Variations of this template**:

| LC | Problem | The twist |
|----|---------|-----------|
| 1606 | Find Servers That Handled Most Number of Requests | Free pool must be searched **circularly** from index `i % k` — use two free heaps (ids `>= i%k` and ids `< i%k`), or a `TreeSet` with `ceiling()`. Busy heap is keyed by finish time as usual. |
| 1801 | Number of Orders in the Backlog | Two heaps of **opposite** polarity that consume each other: `buy` = max-heap by price, `sell` = min-heap by price. Each new order is matched against the other heap while the prices cross; the remainder is pushed. |
| 2102 | Sequentially Ordinal Rank Tracker | Two heaps split the stream around the query pointer: max-heap for "already returned / better" and min-heap for the rest; each `get()` moves one element across the boundary. |
### 6) Sort by One Criterion + Fixed-Size Heap on the Other
> **When**: the objective is a product/combination of two attributes, e.g. `cost = (sum of A over the chosen k) * (max of B over the chosen k)`. **Sort by B** so that iterating fixes the "max B" factor, then keep a size-`k` heap over A to minimise/maximise the sum. This is the two-attribute cousin of the [Kth Element template](./heap.md#1-kth-element-template).

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
Code for LC 1383: [heap_examples.md § Maximum Performance of a Team](./heap_examples.md#12-maximum-performance-of-a-team--lc-1383).

**Key Observations:**
- **Sort fixes the multiplicative factor, the heap optimises the additive factor.** Recognising which attribute to sort by is the entire problem.
- Take the modulo only at the very end (LC 1383) — applying it inside the loop breaks the `max` comparison.
- The heap must be the *opposite* direction of what you keep: keep k smallest → max-heap; keep k largest → min-heap (same rule as the [Kth Element template](./heap.md#1-kth-element-template)).
### 7) Min-Heap Best-First Search on a Grid
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
### 8) Grid Shortest Path with Range Jumps
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

### 9) Frequency Uniqueness — Greedy + Heap / HashSet
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

Java version of the max-heap approach, plus the sorting and `HashSet` alternatives:
[heap_examples.md § Minimum Deletions to Make Character Frequencies Unique](./heap_examples.md#11-minimum-deletions-to-make-character-frequencies-unique--lc-1647).

### 10) Heap with Deduplication
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

### 11) K-Way Merge Variants

**Variations of Template 2 (same skeleton, different bookkeeping):**

| LC | Problem | Twist on the k-way merge |
|----|---------|--------------------------|
| 632 | Smallest Range Covering Elements from K Lists | Also track the **max** of the k heap elements; every pop gives a window `[heap_min, running_max]` that covers all lists — stop when any list is exhausted |
| 355 | Design Twitter | The "k sorted lists" are the followees' tweet lists (newest first); push each followee's head into a max-heap by timestamp, pop 10 times |
| 373 / 378 | K Pairs with Smallest Sums / Kth Smallest in Sorted Matrix | Lists are **virtual** rows of a sorted grid — see [heap_examples.md § LC 373](./heap_examples.md#9-find-k-pairs-with-smallest-sums--lc-373) / [§ LC 378](./heap_examples.md#10-kth-smallest-element-in-a-sorted-matrix--lc-378) |

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

**Variation — LC 1439 Find the Kth Smallest Sum of a Matrix With Sorted Rows**: instead of merging *rows*, merge **row by row**. Keep a running list of the k smallest sums built from the first `r` rows, then combine it with row `r+1` using the same k-smallest-pairs trick as LC 373 (min-heap seeded with `(prev[0] + row[0], i=0, j=0)`, expand `(i+1, j)` / `(i, j+1)`, stop after k pops). Reduces an exponential search to `O(m * k log k)`.

### 12) Lazy Deletion — the "removed counter" Flavour

When you can\'t remove arbitrary elements from a heap, mark them as invalid and skip on pop.

> This is the **"removed counter"** flavour. For the **"hashmap of truth"** flavour (value changed
> rather than element removed), see [1) Lazy Deletion](#1-lazy-deletion--heap--hashmap-of-truth-)
> and [heap_examples.md § Most Frequent IDs](./heap_examples.md#18-most-frequent-ids--lc-3092).

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


## Beyond the Binary Heap

### Indexed Priority Queue
- Allows decrease-key operation
- Useful for Dijkstra optimization
- Track element positions in heap

### Fibonacci Heap
- O(1) amortized insert, decrease-key
- O(log n) extract-min
- Complex implementation, rarely used

### Binary Heap Variants
- d-ary heap: Better cache performance
- Binomial heap: Better merge operation
- Pairing heap: Simple, good practical performance

## Summary & Quick Reference

| Signal in the problem | Pattern | Section |
|---|---|---|
| a pushed value changed / was removed | heap of candidates + hashmap of truth | [1](#1-lazy-deletion--heap--hashmap-of-truth-) |
| "max/min over everything covering x" | sweep + alive heap, evict by coordinate | [2](#2-sweep-line--max-heap-of-alive-intervals-) |
| "k ladders / k free upgrades" | min-heap capped at k, pay for the evicted | [3](#3-bounded-regret-heap--keep-the-k-best-pay-for-the-rest-) |
| "minimum number of stops / max courses" | take everything, `poll()` the worst when stuck | [4](#4-greedy-with-regret--undo-the-worst-past-decision-) |
| "smallest free chair / server" | free-by-id heap + busy-by-release-time heap | [5](#5-two-heaps-as-resource-pools-free-pool--busy-pool-) |
| objective is `sum(A) × max/min(B)` | sort by B, size-k heap over A | [6](#6-sort-by-one-criterion--fixed-size-heap-on-the-other) |
| grid, cost is a minimax or accumulated weight | Dijkstra on the implicit grid graph | [7](#7-min-heap-best-first-search-on-a-grid) |
| each grid cell jumps to a *range* | per-row / per-column PQ + lazy pop | [8](#8-grid-shortest-path-with-range-jumps) |
| "make all frequencies unique" | max-heap decrement, or a used-frequency set | [9](#9-frequency-uniqueness--greedy--heap--hashset) |
| merge *virtual* / *nested* sorted sources | k-way merge with a rebuilt frontier | [11](#11-k-way-merge-variants) |

| Signal | Pattern |
|--------|---------|
| "kth largest/smallest" | Min-heap of size k |
| "top k frequent" | Counter + nlargest / bucket sort |
| "median of stream" | Two heaps (max + min) |
| "always pick best available with constraint" | Greedy + max-heap (IPO pattern) |
| "fit maximum number of tasks/courses" | Sort by deadline + replace heap |
| "remove arbitrary element from heap" | Lazy deletion |
| "merge k sorted lists" | Min-heap with (val, list_idx, elem_idx) |

### Complexity Comparison
| Operation | Binary Heap | Sorted Array | AVL Tree |
|-----------|------------|-------------|---------|
| Insert | O(log n) | O(n) | O(log n) |
| Delete min/max | O(log n) | O(1) | O(log n) |
| Peek min/max | O(1) | O(1) | O(log n) |
| Search arbitrary | O(n) | O(log n) | O(log n) |
| Build from array | O(n) | O(n log n) | O(n log n) |