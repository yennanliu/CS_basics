# Heap — Worked LC Examples

> **Scope** — The problem set for the heap / priority-queue family: each problem filed under the [heap.md](./heap.md) template it reduces to, with the one step it adds; the templates themselves, and the problems that need an advanced technique, live in the sibling sheets.
> **See also** — *parent sheet*: [heap.md](./heap.md) — the canonical templates and pattern-selection guide these examples instantiate. *Siblings split out of the same file*: [heap_advanced.md](./heap_advanced.md) — lazy deletion, regret greedy, sweep-line and the rarer heap templates; [heap_language_apis.md](./heap_language_apis.md) — the full `heapq` / `PriorityQueue` API reference.

## LeetCode Problem Lists

- [Heap (Priority Queue)](https://leetcode.com/problem-list/heap-priority-queue/)

## Overview

[heap.md](./heap.md) holds the eight heap templates; this sheet is the problems. Each section below
mirrors one template, in the same order, and each card names the single step the problem adds to
it. A problem whose solution needs a technique the core templates do not have (lazy deletion,
regret, a grid of range jumps) is worked in [heap_advanced.md](./heap_advanced.md), and a problem
another sheet already works is a pointer row here, never a second copy.

## Index

| LC | Problem | Reduces to | Difficulty | Where |
|---|---|---|---|---|
| LC 1046 | Last Stone Weight | [heap § Template 1](./heap.md#template-1-size-k-min-heap-for-the-kth-largest--lc-215-) | Easy | [card](#last-stone-weight--lc-1046) |
| LC 703 | Kth Largest Element in a Stream | [heap § Template 1](./heap.md#template-1-size-k-min-heap-for-the-kth-largest--lc-215-) | Easy | [card](#kth-largest-element-in-a-stream--lc-703-) |
| LC 1167 | Minimum Cost to Connect Sticks | [heap § Template 1](./heap.md#template-1-size-k-min-heap-for-the-kth-largest--lc-215-) | Medium | [card](#minimum-cost-to-connect-sticks--lc-1167) |
| LC 1492 | The kth Factor of n | [heap § Template 1](./heap.md#template-1-size-k-min-heap-for-the-kth-largest--lc-215-) | Medium | [card](#the-kth-factor-of-n--lc-1492) |
| LC 973 | K Closest Points to Origin | [heap § Template 1](./heap.md#template-1-size-k-min-heap-for-the-kth-largest--lc-215-) | Medium | [card](#k-closest-points-to-origin--lc-973-) |
| LC 1383 | Maximum Performance of a Team | [heap_advanced § 6](./heap_advanced.md#6-sort-by-one-criterion--fixed-size-heap-on-the-other) | Hard | worked in heap_advanced.md |
| LC 347 | Top K Frequent Elements | [heap § Template 2](./heap.md#template-2-count-then-a-size-k-heap-on-the-count--lc-347-) | Medium | worked in heap.md |
| LC 451 | Sort Characters By Frequency | [heap § Template 2](./heap.md#template-2-count-then-a-size-k-heap-on-the-count--lc-347-) | Medium | [card](#sort-characters-by-frequency--lc-451) |
| LC 1481 | Least Number of Unique Integers after K Removals | [heap § Template 2](./heap.md#template-2-count-then-a-size-k-heap-on-the-count--lc-347-) | Medium | [card](#least-number-of-unique-integers-after-k-removals--lc-1481) |
| LC 1647 | Minimum Deletions to Make Character Frequencies Unique | [heap § Template 2](./heap.md#template-2-count-then-a-size-k-heap-on-the-count--lc-347-) | Medium | [card](#minimum-deletions-to-make-character-frequencies-unique--lc-1647) |
| LC 3092 | Most Frequent IDs | [heap_advanced § 1](./heap_advanced.md#1-lazy-deletion--heap--hashmap-of-truth-) | Medium | worked in heap_advanced.md |
| LC 23 | Merge k Sorted Lists | [heap § Template 3](./heap.md#template-3-k-way-merge--lc-23-) | Hard | worked in heap.md |
| LC 264 | Ugly Number II | [Dijkstra_examples § Variation B](./Dijkstra_examples.md#variation-b--lc-264-ugly-number-ii) | Medium | worked in Dijkstra_examples.md |
| LC 373 | Find K Pairs with Smallest Sums | [heap § Template 3](./heap.md#template-3-k-way-merge--lc-23-) | Medium | [card](#find-k-pairs-with-smallest-sums--lc-373) |
| LC 378 | Kth Smallest Element in a Sorted Matrix | [heap § Template 3](./heap.md#template-3-k-way-merge--lc-23-) | Medium | [card](#kth-smallest-element-in-a-sorted-matrix--lc-378-) |
| LC 1675 | Minimize Deviation in Array | [heap § Template 3](./heap.md#template-3-k-way-merge--lc-23-) | Hard | [card](#minimize-deviation-in-array--lc-1675) |
| LC 295 | Find Median from Data Stream | [heap § Template 4](./heap.md#template-4-two-heaps-for-a-running-median--lc-295-) | Hard | worked in heap.md |
| LC 480 | Sliding Window Median | [monotonic_queue § Template 4](./monotonic_queue.md#template-4-sliding-window-median--ordered-multiset--two-heaps-lc-480-) | Hard | worked in monotonic_queue.md |
| LC 1353 | Maximum Number of Events That Can Be Attended | [scanning_line_examples § LC 1353](./scanning_line_examples.md#5-maximum-number-of-events-that-can-be-attended--lc-1353-) | Medium | worked in scanning_line_examples.md |
| LC 2406 | Divide Intervals Into Minimum Number of Groups | [scanning_line_examples § LC 2406](./scanning_line_examples.md#2-divide-intervals-into-minimum-number-of-groups--lc-2406--lc-253-restated) | Medium | worked in scanning_line_examples.md |
| LC 767 | Reorganize String | [heap § Template 7](./heap.md#template-7-greedy-build-with-a-run-length-cap--lc-1405) | Medium | [card](#reorganize-string--lc-767-) |
| LC 621 | Task Scheduler | [heap § Template 8](./heap.md#template-8-max-heap--cooldown-queue--lc-358) | Medium | [card](#task-scheduler--lc-621-) |
| LC 871 | Minimum Number of Refueling Stops | [heap_advanced § 4](./heap_advanced.md#4-greedy-with-regret--undo-the-worst-past-decision-) | Hard | worked in heap_advanced.md |
| LC 2617 | Minimum Number of Visited Cells in a Grid | [heap_advanced § 8](./heap_advanced.md#8-grid-shortest-path-with-range-jumps) | Hard | worked in heap_advanced.md |
| LC 895 | Maximum Frequency Stack | [design_examples § LC 895](./design_examples.md#maximum-frequency-stack--lc-895-) | Hard | worked in design_examples.md — frequency buckets, no heap needed |

## Size-k Heap Examples

### Last Stone Weight — LC 1046

> **Key point** — the heap is not capped at k: it is the **whole pool**, and every step pops the
> two heaviest and pushes back their difference. The heaviest two are always the top two pops,
> so each smash is O(log n).

**Reduces to**: [heap § Template 1](./heap.md#template-1-size-k-min-heap-for-the-kth-largest--lc-215-) — a max-heap that is the entire input rather than the k best, popped two at a time.

```python
# python
# LC 1046 - Last Stone Weight
# IDEA: max-heap (negated) of every stone; smash the top two, push back the difference
# time = O(n log n), space = O(n)
import heapq

def lastStoneWeight(stones):
    heap = [-s for s in stones]          # negated: heaviest on top
    heapq.heapify(heap)
    while len(heap) > 1:
        first = -heapq.heappop(heap)     # heaviest
        second = -heapq.heappop(heap)    # second heaviest
        if first != second:
            heapq.heappush(heap, -(first - second))
    return -heap[0] if heap else 0
```

```java
// java
// LC 1046 - Last Stone Weight
// IDEA: max-heap of every stone; smash the top two, offer back the difference
// time = O(n log n), space = O(n)
public int lastStoneWeight(int[] stones) {
    PriorityQueue<Integer> maxHeap = new PriorityQueue<>(Collections.reverseOrder());
    for (int stone : stones) {
        maxHeap.offer(stone);
    }
    while (maxHeap.size() > 1) {
        int first = maxHeap.poll();          // heaviest
        int second = maxHeap.poll();         // second heaviest
        if (first != second) {
            maxHeap.offer(first - second);   // the survivor goes back into the pool
        }
    }
    return maxHeap.isEmpty() ? 0 : maxHeap.peek();
}
```

**Traps**
- `[2,2]` destroys both stones: the heap ends **empty**, so read `heap[0]` only after the
  emptiness check, and return 0.
- Pushing a zero when the two stones are equal leaves a `0` stone that later "smashes" for free —
  harmless for the answer, but it is a wasted push; skip it.

### Kth Largest Element in a Stream — LC 703 ⭐⭐⭐⭐

> **Key point** — the size-k min-heap is kept **across calls**: `add` pushes, evicts the smallest
> if the heap passes k, and returns the top. Building the object is just k-capped `add` calls on
> the initial array, which may hold fewer than k numbers.

**Reduces to**: [heap § Template 1](./heap.md#template-1-size-k-min-heap-for-the-kth-largest--lc-215-) — the same heap, made a field so it survives between queries.

```python
# python
# LC 703 - Kth Largest Element in a Stream
# IDEA: a min-heap of the k largest seen so far, kept between calls; its top is the kth largest
# time = O(log k) per add (O(n log k) to build), space = O(k)
import heapq

class KthLargest:
    def __init__(self, k, nums):
        self.k = k
        self.heap = []                   # the k largest seen so far
        for num in nums:
            self.add(num)

    def add(self, val):
        heapq.heappush(self.heap, val)
        if len(self.heap) > self.k:
            heapq.heappop(self.heap)     # evict the smallest; the k largest remain
        return self.heap[0]              # smallest of the k largest = kth largest
```

```java
// java
// LC 703 - Kth Largest Element in a Stream
// IDEA: a min-heap of the k largest seen so far, kept between calls; its top is the kth largest
// time = O(log k) per add, space = O(k)
class KthLargest {
    private final PriorityQueue<Integer> minHeap = new PriorityQueue<>();   // the k largest
    private final int k;

    public KthLargest(int k, int[] nums) {
        this.k = k;
        for (int num : nums) {
            add(num);
        }
    }

    public int add(int val) {
        minHeap.offer(val);
        if (minHeap.size() > k) {
            minHeap.poll();                  // evict the smallest; the k largest remain
        }
        return minHeap.peek();
    }
}
```

**Traps**
- `nums` may be shorter than k (`KthLargest(1, [])`): never assume the heap is full in the
  constructor — routing it through `add` handles both cases.
- Heapifying `nums` and then popping down to k mutates the caller's list; build your own heap.

**Similar** — the order-statistic BST answers the same query for *any* rank:
[bst_advanced.md](./bst_advanced.md#template-3-size-augmented-order-statistic-bst--lc-703-).

### Minimum Cost to Connect Sticks — LC 1167

> **Key point** — always merge the **two shortest** sticks: a stick's length is paid once for every
> merge it takes part in, so the shortest ones should sit deepest in the merge tree (Huffman
> coding). The merged stick goes back into the pool.

**Reduces to**: [heap § Template 1](./heap.md#template-1-size-k-min-heap-for-the-kth-largest--lc-215-) — an uncapped min-heap of the whole pool, popped two at a time like LC 1046.

```python
# python
# LC 1167 - Minimum Cost to Connect Sticks
# IDEA: Huffman merge - pop the two shortest, pay their sum, push the merged stick back
# time = O(n log n), space = O(n)
import heapq

def connectSticks(sticks):
    heap = list(sticks)
    heapq.heapify(heap)
    cost = 0
    while len(heap) > 1:
        a = heapq.heappop(heap)          # the two shortest sticks
        b = heapq.heappop(heap)
        cost += a + b
        heapq.heappush(heap, a + b)      # the merged stick goes back into the pool
    return cost
```

**Traps**
- One stick costs **0**: the loop must not run, so the condition is `len(heap) > 1`.
- Sorting once and merging left to right is wrong: `[1,8,3,5]` merges 1+3 = 4, then **4+5**, not
  4+8 — the merged stick has to compete with the rest again.

### The kth Factor of n — LC 1492

> **Key point** — divisors come in pairs `(i, n // i)` with `i ≤ √n`, so √n trial divisions find
> them all. A size-k **max**-heap of the divisors seen keeps the k smallest; its top is the kth
> smallest, provided n has at least k divisors.

**Reduces to**: [heap § Template 1](./heap.md#template-1-size-k-min-heap-for-the-kth-largest--lc-215-) — mirrored for the kth **smallest**, over divisors generated in pairs.

```python
# python
# LC 1492 - The kth Factor of n
# IDEA: enumerate divisor pairs up to sqrt(n); a size-k max-heap keeps the k smallest
# time = O(sqrt(n) log k), space = O(k)
import heapq

def kthFactor(n, k):
    heap = []                            # max-heap (negated) of the k smallest divisors
    def push(d):
        heapq.heappush(heap, -d)
        if len(heap) > k:
            heapq.heappop(heap)          # drop the largest; the k smallest remain
    i = 1
    while i * i <= n:
        if n % i == 0:
            push(i)
            if i != n // i:
                push(n // i)             # the paired divisor
        i += 1
    return -heap[0] if len(heap) == k else -1
```

**Traps**
- A perfect square has its root once: `n = 16` → `4` must not be pushed twice, or k = 3 returns 4
  for the wrong reason and k = 4 is off by one.
- Fewer than k divisors (`n = 4, k = 4`) returns **-1**; check the heap size, not just emptiness.
- Without the heap, the small divisors already come out in order and the large ones in reverse —
  two lists give O(√n) time. Name that if asked for better.

### K Closest Points to Origin — LC 973 ⭐⭐⭐⭐

> **Key point** — "closest k" is "k smallest by distance": keep a size-k **max**-heap keyed by
> squared distance, so the farthest of the kept k is on top and is the one evicted.

**Reduces to**: [heap § Template 1](./heap.md#template-1-size-k-min-heap-for-the-kth-largest--lc-215-) — mirrored for the k smallest, with the key derived from the point.

```python
# python
# LC 973 - K Closest Points to Origin
# IDEA: size-k max-heap (negated) by squared distance; evict the farthest when it passes k
# time = O(n log k), space = O(k)
import heapq

def kClosest(points, k):
    heap = []                            # max-heap (negated distance) of the k closest
    for x, y in points:
        dist = x * x + y * y             # squared distance: no sqrt, no float
        heapq.heappush(heap, (-dist, x, y))
        if len(heap) > k:
            heapq.heappop(heap)          # evict the farthest of the k + 1
    return [[x, y] for _, x, y in heap]
```

```java
// java
// LC 973 - K Closest Points to Origin
// IDEA: size-k max-heap by squared distance; evict the farthest when it passes k
// time = O(n log k), space = O(k)
public int[][] kClosest(int[][] points, int k) {
    // max-heap by squared distance: the farthest of the k kept is on top
    PriorityQueue<int[]> maxHeap = new PriorityQueue<>(
        (a, b) -> Integer.compare(b[0] * b[0] + b[1] * b[1], a[0] * a[0] + a[1] * a[1]));
    for (int[] point : points) {
        maxHeap.offer(point);
        if (maxHeap.size() > k) {
            maxHeap.poll();                  // evict the farthest
        }
    }
    int[][] result = new int[k][];
    for (int i = 0; i < k; i++) {
        result[i] = maxHeap.poll();
    }
    return result;
}
```

**Traps**
- A **min**-heap of all n points popped k times is O(n log n) and O(n) space — the cap is the
  point of the template.
- Comparing `Math.sqrt` distances adds float error for nothing; squared distances order the same.

**Similar** — the O(n) average answer for one static query is quickselect:
[quickselect § Template 2](./quickselect.md#template-2-quickselect-on-a-derived-key--lc-973-).
The heap wins when points stream in or memory must stay O(k).

| LC | Problem | Home | Why it is not here |
|---|---|---|---|
| LC 1383 | Maximum Performance of a Team | [heap_advanced § 6](./heap_advanced.md#6-sort-by-one-criterion--fixed-size-heap-on-the-other) | the size-k heap runs inside a sort on a second attribute |

## Count-then-Heap Examples

| LC | Problem | Home | Note |
|---|---|---|---|
| LC 347 | Top K Frequent Elements | [heap § Template 2](./heap.md#template-2-count-then-a-size-k-heap-on-the-count--lc-347-) | the anchor, with the O(n) bucket-sort version beside it |
| LC 3092 | Most Frequent IDs | [heap_advanced § 1](./heap_advanced.md#1-lazy-deletion--heap--hashmap-of-truth-) | counts change after they are pushed → lazy deletion |

### Sort Characters By Frequency — LC 451

> **Key point** — once the counts are known, the heap emits **whole runs**: pop the most frequent
> character and append it `count` times. There is no k, so the heap is uncapped and drained.

**Reduces to**: [heap § Template 2](./heap.md#template-2-count-then-a-size-k-heap-on-the-count--lc-347-) — count, then order by the count; k is the number of distinct characters.

```python
# python
# LC 451 - Sort Characters By Frequency
# IDEA: count, then a max-heap by count; pop each character and emit its whole run
# time = O(n + d log d), space = O(n)   d = distinct characters
import heapq
from collections import Counter

def frequencySort(s):
    count = Counter(s)
    heap = [(-freq, ch) for ch, freq in count.items()]   # most frequent on top
    heapq.heapify(heap)
    parts = []
    while heap:
        neg_freq, ch = heapq.heappop(heap)
        parts.append(ch * -neg_freq)     # emit the whole run at once
    return "".join(parts)
```

```java
// java
// LC 451 - Sort Characters By Frequency
// IDEA: count, then a max-heap of characters by count; pop each and emit its whole run
// time = O(n + d log d), space = O(n)   d = distinct characters
public String frequencySort(String s) {
    Map<Character, Integer> freq = new HashMap<>();
    for (char c : s.toCharArray()) {
        freq.put(c, freq.getOrDefault(c, 0) + 1);
    }
    PriorityQueue<Character> maxHeap = new PriorityQueue<>(
        (a, b) -> Integer.compare(freq.get(b), freq.get(a)));   // most frequent on top
    maxHeap.addAll(freq.keySet());
    StringBuilder sb = new StringBuilder();
    while (!maxHeap.isEmpty()) {
        char c = maxHeap.poll();
        for (int i = 0; i < freq.get(c); i++) {
            sb.append(c);                    // emit the whole run at once
        }
    }
    return sb.toString();
}
```

**Traps**
- Upper and lower case are different characters: `"Aabb"` → `"bbAa"` or `"bbaA"`, never `"bbaa"`.
- Building the string with `+=` inside the loop is O(n²) in Java; use a `StringBuilder`.
- Counts are at most n, so bucket sort by count is O(n) — the same trick as LC 347's second
  solution in [heap § Template 2](./heap.md#template-2-count-then-a-size-k-heap-on-the-count--lc-347-).

### Least Number of Unique Integers after K Removals — LC 1481

> **Key point** — to leave the fewest distinct values, wipe out the values with the **smallest
> counts first**: a min-heap of counts, popped while the whole count still fits in k. A value that
> only partly fits is not worth touching.

**Reduces to**: [heap § Template 2](./heap.md#template-2-count-then-a-size-k-heap-on-the-count--lc-347-) — count, then pop by the count; the stopping rule is the budget k, not a size.

```python
# python
# LC 1481 - Least Number of Unique Integers after K Removals
# IDEA: min-heap of counts; remove whole values, cheapest first, while k covers them
# time = O(n + d log d), space = O(d)   d = distinct values
import heapq
from collections import Counter

def findLeastNumOfUniqueInts(arr, k):
    heap = list(Counter(arr).values())   # min-heap of counts: cheapest to wipe out on top
    heapq.heapify(heap)
    while heap and heap[0] <= k:
        k -= heapq.heappop(heap)         # remove every copy of that value
    return len(heap)
```

**Traps**
- Decrementing one copy at a time (`k` iterations of pop/push) is O(k log d) and passes, but it
  spends removals on values it never finishes; pop whole counts.
- `heap and` comes first: `[1]`, k = 1 empties the heap and the answer is 0.

### Minimum Deletions to Make Character Frequencies Unique — LC 1647

> **Key point** — process counts from the **largest down** in a max-heap: when the top equals the
> next count, shave one off it and push it back. Every count leaves the heap only once it is
> strictly larger than everything still inside, so the counts that leave are all distinct.

**Reduces to**: [heap § Template 2](./heap.md#template-2-count-then-a-size-k-heap-on-the-count--lc-347-) — count, then a max-heap over the counts; the step it adds is decrement-and-reinsert on a tie.

```python
# python
# LC 1647 - Minimum Deletions to Make Character Frequencies Unique
# IDEA: max-heap of counts; on a tie with the next count, delete one and push it back
# time = O(n + K log K), space = O(K)   K = distinct letters (26), each count shaved at most n times
import heapq
from collections import Counter

def minDeletions(s):
    heap = [-f for f in Counter(s).values()]   # max-heap of counts
    heapq.heapify(heap)
    deletions = 0
    while len(heap) > 1:
        top = -heapq.heappop(heap)
        if top == -heap[0]:              # the next count is the same: shave one off this one
            deletions += 1
            if top - 1 > 0:
                heapq.heappush(heap, -(top - 1))
        # otherwise `top` is now unique and leaves the heap for good
    return deletions
```

```java
// java
// LC 1647 - Minimum Deletions to Make Character Frequencies Unique
// IDEA: max-heap of counts; on a tie with the next count, delete one and offer it back
// time = O(n + K log K), space = O(K)   K = distinct letters (26)
public int minDeletions(String s) {
    int[] freq = new int[26];
    for (char c : s.toCharArray()) {
        freq[c - 'a']++;
    }
    PriorityQueue<Integer> pq = new PriorityQueue<>(Collections.reverseOrder());   // max-heap of counts
    for (int f : freq) {
        if (f > 0) {
            pq.add(f);
        }
    }
    int deletions = 0;
    while (pq.size() > 1) {
        int top = pq.poll();
        if (top == pq.peek()) {              // tied with the next count: shave one off
            deletions++;
            if (top - 1 > 0) {
                pq.add(top - 1);
            }
        }                                    // otherwise top is unique and leaves for good
    }
    return deletions;
}
```

**Traps**
- A count shaved to **0** has deleted the letter entirely; do not push it back, or two zeros
  "collide" and get charged again (`"abcabc"` is 3, not more).
- The sort-based version needs care: after a count is zeroed, the counts **after** it can still
  collide (`"aebacabbbad"` → 4,4,1,1,1 needs 3 deletions). Breaking out of the loop at the first
  zero returns 2.
- A `set` of used counts (decrement until unused) is the other common answer; it is the same
  greedy, without the ordering.

## K-Way Merge Examples

| LC | Problem | Home | Note |
|---|---|---|---|
| LC 23 | Merge k Sorted Lists | [heap § Template 3](./heap.md#template-3-k-way-merge--lc-23-) | the anchor, with the pairwise divide-and-conquer merge beside it |
| LC 264 | Ugly Number II | [Dijkstra_examples § Variation B](./Dijkstra_examples.md#variation-b--lc-264-ugly-number-ii) | heap + seen set over `v·2, v·3, v·5` — best-first generation |

### Find K Pairs with Smallest Sums — LC 373

> **Key point** — row i of the virtual grid, `(i, 0), (i, 1), …`, is already sorted by sum, so the
> rows are **k sorted lists**. Seed each row's head `(i, 0)` and, on a pop, advance only that
> row to `(i, j + 1)` — each pair is pushed once, so no `seen` set is needed.

**Reduces to**: [heap § Template 3](./heap.md#template-3-k-way-merge--lc-23-) — the sources are rows of a grid that is never built, and only the first k rows can matter.

```python
# python
# LC 373 - Find K Pairs with Smallest Sums
# IDEA: k-way merge over rows: seed (i, 0) for the first k rows, pop k times, advance the popped row
# time = O(k log k), space = O(k)
import heapq

def kSmallestPairs(nums1, nums2, k):
    # one source per row i: the pairs (i, 0), (i, 1), ... are sorted by sum
    heap = [(nums1[i] + nums2[0], i, 0) for i in range(min(k, len(nums1)))]
    heapq.heapify(heap)
    res = []
    while heap and len(res) < k:
        _, i, j = heapq.heappop(heap)
        res.append([nums1[i], nums2[j]])
        if j + 1 < len(nums2):
            heapq.heappush(heap, (nums1[i] + nums2[j + 1], i, j + 1))   # advance row i
    return res
```

```java
// java
// LC 373 - Find K Pairs with Smallest Sums
// IDEA: k-way merge over rows: seed (i, 0) for the first k rows, poll k times, advance the polled row
// time = O(k log k), space = O(k)
public List<List<Integer>> kSmallestPairs(int[] nums1, int[] nums2, int k) {
    List<List<Integer>> res = new ArrayList<>();
    // {sum, i, j}: one entry per row i, the row's current head (i, j)
    PriorityQueue<int[]> minHeap = new PriorityQueue<>((a, b) -> Integer.compare(a[0], b[0]));
    for (int i = 0; i < nums1.length && i < k; i++) {
        minHeap.offer(new int[]{nums1[i] + nums2[0], i, 0});   // (i, 0) is row i's smallest
    }
    while (res.size() < k && !minHeap.isEmpty()) {
        int[] cur = minHeap.poll();
        int i = cur[1], j = cur[2];
        res.add(Arrays.asList(nums1[i], nums2[j]));
        if (j + 1 < nums2.length) {
            minHeap.offer(new int[]{nums1[i] + nums2[j + 1], i, j + 1});   // advance row i
        }
    }
    return res;
}
```

**Traps**
- Seeding every row is O(n₁ log n₁) for nothing: a pair in row i ≥ k has at least k smaller pairs
  before it, so only `min(k, len(nums1))` rows are seeded.
- k can exceed `len(nums1) · len(nums2)` (`[1,2]`, `[3]`, k = 3 → two pairs): stop when the heap
  empties.
- Expanding both `(i + 1, j)` and `(i, j + 1)` from every pop is the best-first variant, and it
  **does** need a `seen` set — see
  [Dijkstra_examples § 11](./Dijkstra_examples.md#11-best-first-search-on-an-implicit-graph--lc-373).

### Kth Smallest Element in a Sorted Matrix — LC 378 ⭐⭐⭐⭐

> **Key point** — each row is a sorted source; merge the rows with one head per row and the
> **kth pop** is the answer. Only the first `min(k, n)` rows can hold one of the k smallest values.

**Reduces to**: [heap § Template 3](./heap.md#template-3-k-way-merge--lc-23-) — the rows are the k sorted lists, and the merge stops after k values.

```python
# python
# LC 378 - Kth Smallest Element in a Sorted Matrix
# IDEA: k-way merge of the rows; pop k - 1 values, the top is then the kth smallest
# time = O(k log n), space = O(n)
import heapq

def kthSmallest(matrix, k):
    n = len(matrix)
    heap = [(matrix[r][0], r, 0) for r in range(min(k, n))]   # head of each row
    heapq.heapify(heap)
    for _ in range(k - 1):               # pop k - 1 values; the next top is the kth
        _, r, c = heapq.heappop(heap)
        if c + 1 < n:
            heapq.heappush(heap, (matrix[r][c + 1], r, c + 1))
    return heap[0][0]
```

```java
// java
// LC 378 - Kth Smallest Element in a Sorted Matrix
// IDEA: k-way merge of the rows; poll k - 1 values, the top is then the kth smallest
// time = O(k log n), space = O(n)
public int kthSmallest(int[][] matrix, int k) {
    int n = matrix.length;
    // {value, row, col}: the current head of each row
    PriorityQueue<int[]> minHeap = new PriorityQueue<>((a, b) -> Integer.compare(a[0], b[0]));
    for (int r = 0; r < n && r < k; r++) {
        minHeap.offer(new int[]{matrix[r][0], r, 0});
    }
    for (int i = 0; i < k - 1; i++) {        // poll k - 1 values; the next top is the kth
        int[] cur = minHeap.poll();
        int r = cur[1], c = cur[2];
        if (c + 1 < n) {
            minHeap.offer(new int[]{matrix[r][c + 1], r, c + 1});
        }
    }
    return minHeap.peek()[0];
}
```

**Traps**
- A size-k max-heap over **all n² cells** also works, but it ignores that the rows are sorted:
  O(n² log k). Say why the merge is better before writing either.
- Duplicates count separately: `[[1,2],[1,3]]`, k = 2 is **1**.
- When k is close to n², binary search on the value range is O(n log(max − min)) and beats the
  heap: [binary_search_on_answer § Template 4](./binary_search_on_answer.md#template-4-count-at-most-over-a-value-range--lc-378-).

### Minimize Deviation in Array — LC 1675

> **Key point** — first raise every odd number to its **largest** reachable value (× 2); from there
> values can only halve. Each number is then a source of values going down, and the deviation is
> `max − min` over the current heads: always halve the current **max**, track the running min, and
> stop when the max is odd — that source is exhausted.

**Reduces to**: [heap § Template 3](./heap.md#template-3-k-way-merge--lc-23-) — a k-way merge run downwards (one head per number, max on top), the shape of LC 632's smallest covering range.

```python
# python
# LC 1675 - Minimize Deviation in Array
# IDEA: normalise odds up (x2); max-heap of current values; halve the max while it is even,
#       tracking the running min; the best max - min seen is the answer
# time = O(n log n log M), space = O(n)   M = the largest value
import heapq

def minimumDeviation(nums):
    heap = []                            # max-heap (negated) of each number's current value
    low = float('inf')
    for x in nums:
        if x % 2 == 1:
            x *= 2                       # an odd number's largest reachable value
        heapq.heappush(heap, -x)
        low = min(low, x)
    best = float('inf')
    while True:
        high = -heapq.heappop(heap)
        best = min(best, high - low)
        if high % 2 == 1:                # the max cannot shrink any further
            return best
        high //= 2
        low = min(low, high)
        heapq.heappush(heap, -high)
```

```java
// java
// LC 1675 - Minimize Deviation in Array
// IDEA: normalise odds up (x2); max-heap of current values; halve the max while it is even
// time = O(n log n log M), space = O(n)   M = the largest value
public int minimumDeviation(int[] nums) {
    PriorityQueue<Integer> maxHeap = new PriorityQueue<>(Collections.reverseOrder());
    int min = Integer.MAX_VALUE;
    for (int x : nums) {
        if (x % 2 == 1) {
            x *= 2;                          // an odd number's largest reachable value
        }
        maxHeap.offer(x);
        min = Math.min(min, x);
    }
    int deviation = Integer.MAX_VALUE;
    while (true) {
        int max = maxHeap.poll();
        deviation = Math.min(deviation, max - min);
        if (max % 2 == 1) {
            break;                           // the max cannot shrink any further
        }
        max /= 2;
        min = Math.min(min, max);            // the halved value may be the new minimum
        maxHeap.offer(max);
    }
    return deviation;
}
```

**Traps**
- Multiplying small values up instead of halving the max: after normalisation every move is a
  halving, and only halving the **max** can shrink `max − min`.
- The min is not in the heap's top — it must be tracked by hand, and updated after every halving
  (`[2,10,8]` reaches 3 only once 8 → 4 → 2 is accounted for).
- Recording the deviation **before** the odd check: the final odd max still gives a valid answer.

## Two-Heap Median Examples

| LC | Problem | Home | Note |
|---|---|---|---|
| LC 295 | Find Median from Data Stream | [heap § Template 4](./heap.md#template-4-two-heaps-for-a-running-median--lc-295-) | the anchor |
| LC 480 | Sliding Window Median | [monotonic_queue § Template 4](./monotonic_queue.md#template-4-sliding-window-median--ordered-multiset--two-heaps-lc-480-) | the window also removes values → ordered multiset, or two heaps with lazy deletion |

## End-Time Heap Examples

| LC | Problem | Home | Note |
|---|---|---|---|
| LC 1353 | Maximum Number of Events That Can Be Attended | [scanning_line_examples § LC 1353](./scanning_line_examples.md#5-maximum-number-of-events-that-can-be-attended--lc-1353-) | each event costs **one** day of its span → heap of deadlines, not of end times; [heap § Template 6](./heap.md#template-6-min-heap-of-end-times--lc-253-) says why the LC 253 count over-counts |
| LC 2406 | Divide Intervals Into Minimum Number of Groups | [scanning_line_examples § LC 2406](./scanning_line_examples.md#2-divide-intervals-into-minimum-number-of-groups--lc-2406--lc-253-restated) | LC 253 with closed intervals: reuse a group only when `end < start` |

## Run-Length Cap Examples

### Reorganize String — LC 767 ⭐⭐⭐⭐

> **Key point** — it is possible **iff** the most frequent letter fits in every other slot:
> `max_freq ≤ (n + 1) // 2`. Once that holds, take the **two** most frequent letters each round:
> they differ, and the next round's first letter cannot be this round's second, because the
> second's count never exceeds the first's.

**Reduces to**: [heap § Template 7](./heap.md#template-7-greedy-build-with-a-run-length-cap--lc-1405) — a run-length cap of 1, where popping two at a time replaces the look-back check.

```python
# python
# LC 767 - Reorganize String
# IDEA: feasible iff max_freq <= (n + 1) // 2; then pop the two most frequent letters each round
# time = O(n log 26) = O(n), space = O(26)
import heapq
from collections import Counter

def reorganizeString(s):
    count = Counter(s)
    if max(count.values()) > (len(s) + 1) // 2:
        return ""                        # the most frequent letter cannot be spread out
    heap = [(-cnt, ch) for ch, cnt in count.items()]
    heapq.heapify(heap)
    res = []
    while len(heap) >= 2:
        cnt1, ch1 = heapq.heappop(heap)  # the two most frequent letters, different by construction
        cnt2, ch2 = heapq.heappop(heap)
        res.append(ch1)
        res.append(ch2)
        if cnt1 + 1 < 0:
            heapq.heappush(heap, (cnt1 + 1, ch1))
        if cnt2 + 1 < 0:
            heapq.heappush(heap, (cnt2 + 1, ch2))
    if heap:
        res.append(heap[0][1])           # one letter with one copy left
    return "".join(res)
```

```java
// java
// LC 767 - Reorganize String
// IDEA: feasible iff maxFreq <= (n + 1) / 2; then poll the two most frequent letters each round
// time = O(n log 26) = O(n), space = O(26)
public String reorganizeString(String s) {
    int[] freq = new int[26];
    for (char c : s.toCharArray()) {
        freq[c - 'a']++;
    }
    int n = s.length();
    for (int f : freq) {
        if (f > (n + 1) / 2) {
            return "";                       // the most frequent letter cannot be spread out
        }
    }
    PriorityQueue<int[]> maxHeap = new PriorityQueue<>((a, b) -> Integer.compare(b[0], a[0]));   // {count, letter}
    for (int i = 0; i < 26; i++) {
        if (freq[i] > 0) {
            maxHeap.offer(new int[]{freq[i], i});
        }
    }
    StringBuilder sb = new StringBuilder();
    while (maxHeap.size() >= 2) {
        int[] first = maxHeap.poll();        // the two most frequent, different letters
        int[] second = maxHeap.poll();
        sb.append((char) (first[1] + 'a'));
        sb.append((char) (second[1] + 'a'));
        if (--first[0] > 0) maxHeap.offer(first);
        if (--second[0] > 0) maxHeap.offer(second);
    }
    if (!maxHeap.isEmpty()) {
        sb.append((char) (maxHeap.poll()[1] + 'a'));   // one letter with one copy left
    }
    return sb.toString();
}
```

**Second solution — no heap, O(n).** The feasibility test also builds the answer: write the
letters, most frequent first, into slots 0, 2, 4, … and then 1, 3, 5, …. Two copies of a letter
land two apart, and the wrap from even to odd slots is safe because the most frequent letter fits
in the even slots by the test.

```python
# python
# LC 767 - Reorganize String (even/odd fill)
# IDEA: most frequent letter first into the even slots, then wrap to the odd ones
# time = O(n), space = O(n)
from collections import Counter

def reorganizeString_fill(s):
    count = Counter(s)
    if max(count.values()) > (len(s) + 1) // 2:
        return ""
    res = [''] * len(s)
    i = 0
    for ch, cnt in count.most_common():  # most frequent first: it must fit the even slots
        for _ in range(cnt):
            if i >= len(s):
                i = 1                    # even slots used up: continue on the odd ones
            res[i] = ch
            i += 2
    return "".join(res)
```

**Traps**
- Skipping the feasibility test: `"aaab"` must return `""`; the two-at-a-time loop would instead
  end with `a` left over twice.
- The even/odd fill only works with the most frequent letter **first** — `"abbccc"` filled in
  alphabetical order puts two `c`s side by side at the wrap.
- The heap is at most 26 entries, so the `log 26` is a constant; say O(n).

**Similar** — LC 1054 (the same on numbers) · LC 358 (a general gap k → Template 8)

## Cooldown Queue Examples

### Task Scheduler — LC 621 ⭐⭐⭐⭐⭐

> **Key point** — only the **length** is asked, so only the most frequent task matters: it forces
> `max_freq − 1` frames of width `n + 1`, plus a last frame holding every task tied for the max.
> If there are more tasks than that layout has slots, no idle is ever needed and the answer is
> `len(tasks)`.

**Reduces to**: [heap § Template 8](./heap.md#template-8-max-heap--cooldown-queue--lc-358) — the cooldown schedule, counted in closed form instead of simulated.

```text
tasks = AAABBB, n = 2   max_freq = 3, tied for it = 2 (A and B)

  | A B idle | A B idle | A B
  \_ n + 1 _/\_ n + 1 _/\_ 2 _/

  slots = (3 - 1) * (2 + 1) + 2 = 8
```

```python
# python
# LC 621 - Task Scheduler
# IDEA: (max_freq - 1) frames of width n + 1, plus every task tied for max_freq; never below len(tasks)
# time = O(N), space = O(26)
from collections import Counter

def leastInterval(tasks, n):
    count = Counter(tasks)
    max_freq = max(count.values())
    num_max = sum(1 for f in count.values() if f == max_freq)   # tasks tied for the max
    frames = (max_freq - 1) * (n + 1) + num_max
    return max(len(tasks), frames)       # enough distinct tasks fill every gap: no idle
```

```java
// java
// LC 621 - Task Scheduler
// IDEA: (maxFreq - 1) frames of width n + 1, plus every task tied for maxFreq; never below tasks.length
// time = O(N), space = O(26)
public int leastInterval(char[] tasks, int n) {
    int[] freq = new int[26];
    int maxFreq = 0;
    for (char t : tasks) {
        freq[t - 'A']++;
        maxFreq = Math.max(maxFreq, freq[t - 'A']);
    }
    int numMax = 0;                          // tasks tied for the max
    for (int f : freq) {
        if (f == maxFreq) numMax++;
    }
    int frames = (maxFreq - 1) * (n + 1) + numMax;
    return Math.max(tasks.length, frames);   // enough distinct tasks fill every gap: no idle
}
```

**Why the formula is V0** — it is O(N) time and O(1) space, it is what an interviewer expects
for "return the least number of intervals", and the frame picture is a proof you can draw. The
simulation below is Template 8 itself; reach for it when the **order** of the tasks is asked, or
when the cooldown rule is not uniform.

**Second solution — simulate with the heap and a cooldown queue.** A run task waits in a FIFO
with the time it may run again; at that tick it returns to the heap.

```python
# python
# LC 621 - Task Scheduler (simulation)
# IDEA: max-heap of remaining counts; a run task waits in a FIFO until time + n, then returns
# time = O(N * n) in the worst case (idle ticks are simulated), space = O(26)
import heapq
from collections import Counter, deque

def leastInterval_heap(tasks, n):
    heap = [-cnt for cnt in Counter(tasks).values()]   # most remaining on top
    heapq.heapify(heap)
    cooldown = deque()                   # (negated remaining, time it may run again)
    time = 0
    while heap or cooldown:
        time += 1
        if heap:
            remaining = heapq.heappop(heap) + 1        # run one copy
            if remaining < 0:
                cooldown.append((remaining, time + n))
        if cooldown and cooldown[0][1] == time:
            heapq.heappush(heap, cooldown.popleft()[0])
    return time
```

**Traps**
- Forgetting `max(len(tasks), …)`: `["A","C","A","B","D","B"]`, n = 1 gives a frame count of 4,
  but six tasks need six intervals.
- Counting only one task at the max: `AAABBB`, n = 2 is 8, not 7 — the last frame holds every
  task tied for `max_freq`.
- In the simulation, counts are negated: `remaining < 0` means copies are left; `== 0` drops the
  task for good.

**Similar** — LC 358 (the order is asked → Template 8) · LC 767 (gap 2, Template 7) · LC 2365 (Task
Scheduler II: the order is fixed, a hash map of next-allowed day)
