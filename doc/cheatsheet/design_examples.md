# Design — Worked Examples

> **Scope** — The worked "design a X" problems behind [design.md](./design.md), one problem card each, filed under the structure pair (design.md's patterns) that each one forces.
> **See also**: [design.md](./design.md) — the parent sheet: the patterns, the five-step method and the chooser table these solutions rehearse; [design_patterns.md](./design_patterns.md) — consistent hashing, rate limiters and load balancing, which are asked in the same rounds but are not LC problems; [ood_design.md](./ood_design.md) — class modelling and SOLID for the LLD round; [iterator.md](./iterator.md) — the iterator contract on its own; [trie.md](./trie.md), [heap.md](./heap.md), [hash_map.md](./hash_map.md), [linked_list.md](./linked_list.md) — the individual structures being combined.

## LeetCode Problem Lists

- [Design](https://leetcode.com/problem-list/design/)
- [Data Stream](https://leetcode.com/problem-list/data-stream/)

## Overview

[design.md](./design.md) holds the patterns: a design problem is one structure that holds the data
plus a second structure whose only job is to make one operation fast. This sheet is the problems.
Each card names the pattern it reduces to, the one step it adds, and one canonical solution. The
designs whose structure is a core card elsewhere (LRU in [linked_list.md](./linked_list.md), the
running median in [heap.md](./heap.md), the min stack in [stack.md](./stack.md)) are worked there
and listed here as pointers.

## Index

| LC | Problem | Reduces to | Difficulty | Where |
|---|---|---|---|---|
| 146 | LRU Cache | [linked_list § Template 7](./linked_list.md#template-7-hash-map--doubly-linked-list-lru--lc-146-) | Medium | worked in linked_list.md |
| 460 | LFU Cache | [linked_list § LFU variant](./linked_list.md#lfu-variant--one-list-per-frequency-plus-a-min_freq-pointer-lc-460-) | Hard | worked in linked_list.md |
| 432 | All O(1) Data Structure | [linked_list § Template 7](./linked_list.md#template-7-hash-map--doubly-linked-list-lru--lc-146-) | Hard | [below](#all-o1-data-structure--lc-432-) |
| 703 | Kth Largest Element in a Stream | [heap § Template 1](./heap.md#template-1-size-k-min-heap-for-the-kth-largest--lc-215-) | Easy | worked in heap_examples.md |
| 355 | Design Twitter | [heap § Template 3](./heap.md#template-3-k-way-merge--lc-23-) | Medium | [below](#design-twitter--lc-355) |
| 295 | Find Median from Data Stream | [heap § Template 4](./heap.md#template-4-two-heaps-for-a-running-median--lc-295-) | Hard | worked in heap.md |
| 211 | Design Add and Search Words Data Structure | [trie § Template 3](./trie.md#template-3-trie-with-wildcard-support--lc-211) | Medium | worked in trie.md |
| 642 | Design Search Autocomplete System | [trie § Template 4](./trie.md#template-4-autocomplete-trie--lc-1268) | Hard | [below](#design-search-autocomplete-system--lc-642) |
| 362 | Design Hit Counter | [queue § Template 6](./queue.md#template-6-stream-processing-pattern--lc-346) | Medium | [below](#design-hit-counter--lc-362) |
| 353 | Design Snake Game | [queue § Template 6](./queue.md#template-6-stream-processing-pattern--lc-346) | Medium | [below](#design-snake-game--lc-353) |
| 380 | Insert Delete GetRandom O(1) | [design § Pattern 6](./design.md#pattern-6-hashmap--array-swap-to-end-delete) | Medium | worked in [set_examples.md](./set_examples.md#11-insert-delete-getrandom-o1--lc-380--set-semantics-array-storage-) |
| 155 | Min Stack | [stack § Template 4](./stack.md#template-4-min-stack--o1-getmin--lc-155-) | Medium | worked in stack.md |
| 1381 | Design a Stack With Increment Operation | [stack § Template 4](./stack.md#template-4-min-stack--o1-getmin--lc-155-) | Medium | [below](#design-a-stack-with-increment-operation--lc-1381) |
| 895 | Maximum Frequency Stack | [stack § Template 4](./stack.md#template-4-min-stack--o1-getmin--lc-155-) | Hard | [below](#maximum-frequency-stack--lc-895-) |
| 981 | Time Based Key-Value Store | [design § Pattern 8](./design.md#pattern-8-sorted-map--time-index) | Medium | [below](#time-based-key-value-store--lc-981) |
| 729 | My Calendar I | [design § Pattern 8](./design.md#pattern-8-sorted-map--time-index) | Medium | [below](#my-calendar-i--lc-729) |
| 731 | My Calendar II | [design § Pattern 8](./design.md#pattern-8-sorted-map--time-index) | Medium | [below](#my-calendar-ii--lc-731) |
| 732 | My Calendar III | [design § Pattern 8](./design.md#pattern-8-sorted-map--time-index) | Hard | [below](#my-calendar-iii--lc-732) |
| 715 | Range Module | [design § Pattern 8](./design.md#pattern-8-sorted-map--time-index) | Hard | [below](#range-module--lc-715) |
| 2034 | Stock Price Fluctuation | [heap_advanced § two lazy heaps](./heap_advanced.md#variant--two-mirrored-lazy-heaps-over-one-map-lc-2034-) | Medium | worked in heap_advanced.md |
| 1166 | Design File System | [design § Pattern 9](./design.md#pattern-9-path-hierarchy) | Medium | [below](#design-file-system--lc-1166) |
| 635 | Design Log Storage System | [design § Pattern 9](./design.md#pattern-9-path-hierarchy) | Medium | [below](#design-log-storage-system--lc-635) |
| 588 | Design In-Memory File System | [design § Pattern 9](./design.md#pattern-9-path-hierarchy) | Hard | [below](#design-in-memory-file-system--lc-588) |
| 348 | Design Tic-Tac-Toe | [design § Pattern 10](./design.md#pattern-10-aggregate-counters-updated-on-write) | Medium | [below](#design-tic-tac-toe--lc-348) |
| 1396 | Design Underground System | [design § Pattern 10](./design.md#pattern-10-aggregate-counters-updated-on-write) | Medium | [below](#design-underground-system--lc-1396) |

Other `design`-tagged problems, not worked here:

| LC | Problem | The idea | Where |
|---|---|---|---|
| 705 / 706 | Design HashSet / HashMap | bucket array + separate chaining | [hash_map.md](./hash_map.md) |
| 707 | Design Linked List | dummy head + a size field | [linked_list.md](./linked_list.md) |
| 1472 | Design Browser History | array + a cursor; `visit` truncates the forward history | [linked_list § Template 7](./linked_list.md#template-7-hash-map--doubly-linked-list-lru--lc-146-) drill |
| 1352 | Product of the Last K Numbers | prefix products; `add(0)` resets the list, and `k` past its length answers 0 | [prefix_sum.md](./prefix_sum.md) |
| 297 / 449 | Serialize and Deserialize Binary Tree / BST | preorder with a null marker; a BST can drop the markers and rebuild with `(lower, upper)` bounds | [tree.md](./tree.md) |
| 745 | Prefix and Suffix Search | insert every `suffix + '{' + word` into one trie, query `suf + '{' + pre` | [trie.md](./trie.md) |
| 676 / 677 | Implement Magic Dictionary / Map Sum Pairs | trie with a one-mismatch DFS / a prefix-sum per node | [trie.md](./trie.md) |

## HashMap + Linked List Examples

### All O(1) Data Structure — LC 432 ⭐⭐⭐⭐

> **Key point** — the list holds **count buckets**, not keys, in increasing count order, so the
> min key is in `head.next` and the max key in `tail.prev`. A count changes by exactly 1, so a key
> only ever moves to the **adjacent** bucket, which is created next to its old one in O(1).

**Reduces to**: [linked_list § Template 7](./linked_list.md#template-7-hash-map--doubly-linked-list-lru--lc-146-) — the same sentinel doubly linked list, but each node is a bucket of keys with one count, and there is no `min_freq` to maintain (LFU's integer becomes the list's own order).

```python
# python
# LC 432 - All O`one Data Structure
# IDEA: key -> count map + a doubly linked list of buckets ordered by count;
#       inc / dec move a key to the neighbouring bucket, creating it beside the old one
# time = O(1) per call, space = O(number of keys)
class Bucket:
    def __init__(self, count):
        self.count = count
        self.keys = set()
        self.prev = None
        self.next = None

class AllOne:
    def __init__(self):
        self.count_of = {}          # key -> count
        self.bucket_of = {}         # count -> Bucket
        self.head = Bucket(0)       # sentinel before the smallest count
        self.tail = Bucket(0)       # sentinel after the largest count
        self.head.next = self.tail
        self.tail.prev = self.head

    def _insert_after(self, node, new_node):
        new_node.prev = node
        new_node.next = node.next
        node.next.prev = new_node
        node.next = new_node

    def _remove_if_empty(self, bucket):
        if not bucket.keys:
            bucket.prev.next = bucket.next
            bucket.next.prev = bucket.prev
            del self.bucket_of[bucket.count]

    def inc(self, key):
        count = self.count_of.get(key, 0)
        here = self.bucket_of[count] if count else self.head
        if count + 1 not in self.bucket_of:              # the next bucket goes right after this one
            new_bucket = Bucket(count + 1)
            self.bucket_of[count + 1] = new_bucket
            self._insert_after(here, new_bucket)
        self.bucket_of[count + 1].keys.add(key)
        self.count_of[key] = count + 1
        if count:
            here.keys.remove(key)
            self._remove_if_empty(here)

    def dec(self, key):
        count = self.count_of[key]                      # the problem guarantees key exists
        here = self.bucket_of[count]
        if count == 1:
            del self.count_of[key]
        else:
            if count - 1 not in self.bucket_of:          # the previous bucket goes right before this one
                new_bucket = Bucket(count - 1)
                self.bucket_of[count - 1] = new_bucket
                self._insert_after(here.prev, new_bucket)
            self.bucket_of[count - 1].keys.add(key)
            self.count_of[key] = count - 1
        here.keys.remove(key)
        self._remove_if_empty(here)

    def getMaxKey(self):
        if self.tail.prev is self.head:
            return ""
        return next(iter(self.tail.prev.keys))

    def getMinKey(self):
        if self.head.next is self.tail:
            return ""
        return next(iter(self.head.next.keys))
```

**Traps**
- Removing the old bucket **before** inserting the new one next to it: `inc` on the only key of
  bucket 1 unlinks bucket 1, and bucket 2 then has no anchor to be inserted after.
- Scanning for the min / max bucket: with a hash map from count to bucket alone, `getMinKey` is
  O(distinct counts). The list order is what makes it O(1).
- `dec` to 0 must delete the key from `count_of`, or a later `inc` starts from a stale count.

## HashMap + Heap Examples

### Design Twitter — LC 355

> **Key point** — every user's tweets are already in time order, so the feed is a **k-way merge**
> over the followees' lists, stopped after 10. A global clock makes timestamps comparable across
> users; pushing it negated lets a min-heap pop the newest tweet first.

**Reduces to**: [heap § Template 3](./heap.md#template-3-k-way-merge--lc-23-) — merging k sorted lists, but lazily: only the first 10 results are pulled. The entity modelling (why `Tweet` carries a time, why the clock is global) is in [ood_design.md](./ood_design.md).

```python
# python
# LC 355 - Design Twitter
# IDEA: per-user tweet lists in post order + a follow set; getNewsFeed = lazy k-way merge, newest first
# time = O(1) post / follow / unfollow, O(k + 10 log k) getNewsFeed for k followees; space = O(users + tweets)
import heapq
from collections import defaultdict

class Twitter:
    def __init__(self):
        self.clock = 0
        self.tweets = defaultdict(list)     # user -> [(time, tweetId)], oldest first
        self.following = defaultdict(set)   # user -> users they follow

    def postTweet(self, userId, tweetId):
        self.tweets[userId].append((self.clock, tweetId))
        self.clock += 1

    def getNewsFeed(self, userId):
        heap = []                                       # (-time, tweetId, user, index in that user's list)
        for user in self.following[userId] | {userId}: # a user always sees their own tweets
            if self.tweets[user]:
                i = len(self.tweets[user]) - 1          # newest tweet of this user
                time, tweet_id = self.tweets[user][i]
                heap.append((-time, tweet_id, user, i))
        heapq.heapify(heap)
        feed = []
        while heap and len(feed) < 10:
            _, tweet_id, user, i = heapq.heappop(heap)
            feed.append(tweet_id)
            if i > 0:                                   # this user's next-newest tweet
                time, next_id = self.tweets[user][i - 1]
                heapq.heappush(heap, (-time, next_id, user, i - 1))
        return feed

    def follow(self, followerId, followeeId):
        self.following[followerId].add(followeeId)

    def unfollow(self, followerId, followeeId):
        self.following[followerId].discard(followeeId)
```

**Traps**
- Adding the user to their own follow set inside `getNewsFeed` mutates state: a later
  `unfollow(u, u)` then hides the user's own tweets. Union with `{userId}` instead.
- `remove` instead of `discard` in `unfollow` raises on a user who was never followed.
- Merging every tweet and slicing the first 10 is O(total tweets); the heap stops after 10 pops.

## Trie Examples

| LC | Problem | Where |
|---|---|---|
| 211 | Design Add and Search Words Data Structure | [trie § Template 3](./trie.md#template-3-trie-with-wildcard-support--lc-211) — the anchor problem of that card |

### Design Search Autocomplete System — LC 642

> **Key point** — the cursor into the trie **moves with the typed prefix**, so each keystroke is
> one child step, not a fresh walk from the root. Once a character falls off the trie the cursor
> stays `None` until `#`, and every later keystroke of that sentence answers `[]`.

**Reduces to**: [trie § Template 4](./trie.md#template-4-autocomplete-trie--lc-1268) — the same prefix walk, plus a hot count per sentence that changes at runtime (so the top 3 are ranked at query time, not cached at insert time).

```python
# python
# LC 642 - Design Search Autocomplete System
# IDEA: each trie node keeps the set of sentences under it; a live cursor follows the typed prefix;
#       '#' records the sentence (count + 1) and resets the cursor
# time = O(m log m) per keystroke for the m sentences under the cursor, space = O(total characters)
from collections import defaultdict

class TrieNode:
    def __init__(self):
        self.children = {}
        self.sentences = set()

class AutocompleteSystem:
    def __init__(self, sentences, times):
        self.root = TrieNode()
        self.count = defaultdict(int)       # sentence -> hot degree
        for sentence, t in zip(sentences, times):
            self.count[sentence] += t
            self._add(sentence)
        self.typed = ""
        self.cursor = self.root

    def _add(self, sentence):
        node = self.root
        for ch in sentence:
            node = node.children.setdefault(ch, TrieNode())
            node.sentences.add(sentence)

    def input(self, c):
        if c == "#":
            self.count[self.typed] += 1
            self._add(self.typed)
            self.typed = ""
            self.cursor = self.root
            return []
        self.typed += c
        if self.cursor is not None:
            self.cursor = self.cursor.children.get(c)
        if self.cursor is None:
            return []
        ranked = sorted(self.cursor.sentences, key=lambda s: (-self.count[s], s))
        return ranked[:3]
```

**Traps**
- Resetting the cursor to the root when a character is missing: `"i a"` after a miss on `"i "`
  would start matching from the root again and suggest sentences that do not have the prefix.
- Ties break by ASCII order of the sentence (`' '` sorts before letters), which is plain string
  comparison; sorting by count alone fails LC 642's example 1.

## Stream / Queue Examples

### Design Hit Counter — LC 362

> **Key point** — timestamps arrive in non-decreasing order, so expired hits are always at the
> **front**: the queue holds exactly the hits of the last 300 seconds after `getHits` pops from
> the left.

**Reduces to**: [queue § Template 6](./queue.md#template-6-stream-processing-pattern--lc-346) — a moving window over a stream, keyed by time instead of by count.

```python
# python
# LC 362 - Design Hit Counter
# IDEA: deque of hit timestamps; evict from the left everything at or before timestamp - 300
# time = O(1) amortised per call, space = O(hits in the last 300 s)
from collections import deque

class HitCounter:
    def __init__(self):
        self.hits = deque()

    def hit(self, timestamp):
        self.hits.append(timestamp)

    def getHits(self, timestamp):
        while self.hits and self.hits[0] <= timestamp - 300:
            self.hits.popleft()
        return len(self.hits)
```

Follow-up (many hits per second): bucket by `timestamp % 300`, so memory is O(300) however
many hits arrive. This is the second solution to give, with that reason.

```python
# python
# LC 362 - Design Hit Counter (fixed 300 buckets)
# IDEA: bucket i holds the hits of the last second that mapped to i; a stale bucket is reset on reuse
# time = O(1) hit, O(300) getHits; space = O(300)
class HitCounterBuckets:
    def __init__(self):
        self.times = [0] * 300
        self.counts = [0] * 300

    def hit(self, timestamp):
        i = timestamp % 300
        if self.times[i] != timestamp:      # this bucket still holds an older second
            self.times[i] = timestamp
            self.counts[i] = 0
        self.counts[i] += 1

    def getHits(self, timestamp):
        total = 0
        for i in range(300):
            if timestamp - self.times[i] < 300:
                total += self.counts[i]
        return total
```

**Traps**
- The window is `(timestamp - 300, timestamp]`: a hit at 1 is gone at 301. `< timestamp - 300`
  as the eviction test keeps it one second too long.

### Design Snake Game — LC 353

> **Key point** — the body is a deque (head at the left, tail at the right) **plus** a set of the
> same cells. Move the tail out of the set **before** testing the new head, because the head may
> legally enter the cell the tail is leaving.

**Reduces to**: [queue § Template 6](./queue.md#template-6-stream-processing-pattern--lc-346) — a queue whose front is consumed as the stream advances, with a set beside it for O(1) membership.

```python
# python
# LC 353 - Design Snake Game
# IDEA: deque body + set of occupied cells; pop the tail unless eating, then test the new head
# time = O(1) per move, space = O(width * height)
from collections import deque

class SnakeGame:
    def __init__(self, width, height, food):
        self.width = width
        self.height = height
        self.food = deque(food)
        self.body = deque([(0, 0)])     # head at index 0
        self.occupied = {(0, 0)}
        self.score = 0

    def move(self, direction):
        dr, dc = {"U": (-1, 0), "D": (1, 0), "L": (0, -1), "R": (0, 1)}[direction]
        row, col = self.body[0][0] + dr, self.body[0][1] + dc
        if not (0 <= row < self.height and 0 <= col < self.width):
            return -1
        if self.food and [row, col] == self.food[0]:
            self.food.popleft()             # eat: the tail stays, the snake grows
            self.score += 1
        else:
            tail = self.body.pop()
            self.occupied.remove(tail)
        if (row, col) in self.occupied:
            return -1
        self.body.appendleft((row, col))
        self.occupied.add((row, col))
        return self.score
```

**Traps**
- Testing the collision before removing the tail: a snake of length 4 moving in a 2×2 loop is
  reported dead when its head enters the cell the tail is vacating.
- Food is eaten one item at a time and in order: only `food[0]` is live.

## HashMap + Array Examples

| LC | Problem | Where |
|---|---|---|
| 380 | Insert Delete GetRandom O(1) | [set_examples § 11](./set_examples.md#11-insert-delete-getrandom-o1--lc-380--set-semantics-array-storage-) — swap the victim with the last element, then pop |

## Stack + Auxiliary State Examples

### Design a Stack With Increment Operation — LC 1381

> **Key point** — `increment(k, val)` adds to the bottom k elements, so record it **once**, on the
> k-th element, as a pending delta for everything below; `pop` hands its delta down to the next
> element before returning. Every element therefore sees exactly the increments that covered it.

**Reduces to**: [stack § Template 4](./stack.md#template-4-min-stack--o1-getmin--lc-155-) — one auxiliary value per layer, pushed and popped with it; here the value is a pending delta instead of a running minimum.

```python
# python
# LC 1381 - Design a Stack With Increment Operation
# IDEA: inc[i] = pending amount for stack[0..i]; pop adds inc[top] and passes it down to top - 1
# time = O(1) per push / pop / increment, space = O(maxSize)
class CustomStack:
    def __init__(self, maxSize):
        self.max_size = maxSize
        self.stack = []
        self.inc = []           # inc[i] applies to stack[0..i]

    def push(self, x):
        if len(self.stack) < self.max_size:
            self.stack.append(x)
            self.inc.append(0)

    def pop(self):
        if not self.stack:
            return -1
        add = self.inc.pop()
        if self.inc:
            self.inc[-1] += add  # the element below inherits the pending delta
        return self.stack.pop() + add

    def increment(self, k, val):
        i = min(k, len(self.stack)) - 1
        if i >= 0:
            self.inc[i] += val
```

```java
// java
// LC 1381 - Design a Stack With Increment Operation
// IDEA: inc[i] = amount to add to stack[0..i]; on pop, hand the delta down to i - 1
// time = O(1) per push / pop / increment, space = O(maxSize)
class CustomStack {
    private final int[] stack;
    private final int[] inc;
    private int size = 0;

    public CustomStack(int maxSize) {
        stack = new int[maxSize];
        inc = new int[maxSize];
    }

    public void push(int x) {
        if (size < stack.length) {
            stack[size++] = x;
        }
    }

    public int pop() {
        if (size == 0) {
            return -1;
        }
        size--;
        int result = stack[size] + inc[size];
        if (size > 0) {
            inc[size - 1] += inc[size];   // propagate the pending delta downwards
        }
        inc[size] = 0;                    // the slot will be reused by the next push
        return result;
    }

    public void increment(int k, int val) {
        int i = Math.min(k, size) - 1;    // mark only the k-th element
        if (i >= 0) {
            inc[i] += val;
        }
    }
}
```

**Traps**
- Looping over the bottom k elements in `increment` is O(k) per call; the delta makes it O(1).
- Java: forgetting `inc[size] = 0` on pop leaks the old delta into the next element pushed there.

### Maximum Frequency Stack — LC 895 ⭐⭐⭐⭐

> **Key point** — `group[f]` is a stack of every value whose count **reached** f, in push order.
> A push adds the value to the bucket of its new count without removing it from the lower ones, so
> after a pop the earlier copy is already waiting in bucket `f - 1`: no cleanup, and the buckets
> are dense, so `max_freq` only ever drops by 1.

**Reduces to**: [stack § Template 4](./stack.md#template-4-min-stack--o1-getmin--lc-155-) — state kept per layer; here the layers are frequencies and each layer is a stack, so ties break by recency for free.

```python
# python
# LC 895 - Maximum Frequency Stack
# IDEA: value -> count, count -> stack of values; pop = top of the max-count stack
# time = O(1) per push / pop, space = O(n)
from collections import defaultdict

class FreqStack:
    def __init__(self):
        self.freq = defaultdict(int)     # value -> count
        self.group = defaultdict(list)   # count -> values that reached it, in push order
        self.max_freq = 0

    def push(self, val):
        self.freq[val] += 1
        f = self.freq[val]
        self.max_freq = max(self.max_freq, f)
        self.group[f].append(val)

    def pop(self):
        val = self.group[self.max_freq].pop()
        self.freq[val] -= 1
        if not self.group[self.max_freq]:
            self.max_freq -= 1
        return val
```

```java
// java
// LC 895 - Maximum Frequency Stack
// IDEA: bucket values by frequency, each bucket a stack -> pop = top of the maxFreq bucket
// time = O(1) per push / pop, space = O(n)
class FreqStack {
    private final Map<Integer, Integer> freq = new HashMap<>();          // value -> count
    private final Map<Integer, Deque<Integer>> group = new HashMap<>();  // count -> stack of values
    private int maxFreq = 0;

    public void push(int val) {
        int f = freq.merge(val, 1, Integer::sum);
        maxFreq = Math.max(maxFreq, f);
        group.computeIfAbsent(f, x -> new ArrayDeque<>()).push(val);     // keep copies in 1..f
    }

    public int pop() {
        Deque<Integer> top = group.get(maxFreq);
        int val = top.pop();
        freq.merge(val, -1, Integer::sum);
        if (top.isEmpty()) {
            maxFreq--;   // buckets are dense: maxFreq only ever drops by 1
        }
        return val;
    }
}
```

**Traps**
- Moving the value out of its old bucket on push (as LFU does) breaks the pop: after popping
  from bucket 2 the value must still be in bucket 1, in its original push position.
- A heap of `(-freq, -push_index, value)` also works, but it is O(log n) per call — say it as
  the first idea, then give the buckets.

## Sorted Map / Time Index Examples

### Time Based Key-Value Store — LC 981

> **Key point** — `set` is called with strictly increasing timestamps, so each key's list is
> **already sorted by time**; `get` is a binary search for the last entry with time ≤ t.

**Reduces to**: [design § Pattern 8](./design.md#pattern-8-sorted-map--time-index) — a hash map to a time-sorted list; the "floor" query is `bisect_right - 1`.

```python
# python
# LC 981 - Time Based Key-Value Store
# IDEA: key -> parallel lists of times and values (appended in time order); get = floor by bisect
# time = O(1) set, O(log n) get; space = O(n)
import bisect
from collections import defaultdict

class TimeMap:
    def __init__(self):
        self.times = defaultdict(list)    # key -> timestamps, increasing
        self.values = defaultdict(list)   # key -> values, aligned with times

    def set(self, key, value, timestamp):
        self.times[key].append(timestamp)
        self.values[key].append(value)

    def get(self, key, timestamp):
        i = bisect.bisect_right(self.times[key], timestamp) - 1   # last time <= timestamp
        return self.values[key][i] if i >= 0 else ""
```

```java
// java
// LC 981 - Time Based Key-Value Store
// IDEA: key -> list of (time, value) appended in time order; get = binary search for the last time <= t
// time = O(1) set, O(log n) get; space = O(n)
class TimeMap {
    private final Map<String, List<Integer>> times = new HashMap<>();
    private final Map<String, List<String>> values = new HashMap<>();

    public void set(String key, String value, int timestamp) {
        times.computeIfAbsent(key, k -> new ArrayList<>()).add(timestamp);
        values.computeIfAbsent(key, k -> new ArrayList<>()).add(value);
    }

    public String get(String key, int timestamp) {
        List<Integer> ts = times.get(key);
        if (ts == null) return "";
        int lo = 0, hi = ts.size() - 1, found = -1;
        while (lo <= hi) {                      // last index with ts[i] <= timestamp
            int mid = lo + (hi - lo) / 2;
            if (ts.get(mid) <= timestamp) {
                found = mid;
                lo = mid + 1;
            } else {
                hi = mid - 1;
            }
        }
        return found == -1 ? "" : values.get(key).get(found);
    }
}
```

**Traps**
- `bisect` on a list of `(time, value)` tuples needs a sentinel for the second field
  (`(t, chr(127))`), and a value starting with a later character breaks it. Bisect the times alone.
- A query earlier than every `set` must return `""`, not `values[-1]`: `i == -1` is the check.

### My Calendar I — LC 729

> **Key point** — the stored bookings are pairwise disjoint and keyed by start, so a new
> `[start, end)` can only collide with two of them: the one starting at or before `start` (floor)
> and the one starting at or after it (ceiling).

**Reduces to**: [design § Pattern 8](./design.md#pattern-8-sorted-map--time-index) — the floor / ceiling query on a sorted map.

```python
# python
# LC 729 - My Calendar I
# IDEA: sorted starts with aligned ends; check the floor (spills into us?) and the ceiling (we spill into it?)
# time = O(log n) search + O(n) list insert per book, space = O(n)
import bisect

class MyCalendar:
    def __init__(self):
        self.starts = []
        self.ends = []      # aligned with starts

    def book(self, start, end):
        i = bisect.bisect_right(self.starts, start) - 1     # floor: last start <= start
        if i >= 0 and self.ends[i] > start:
            return False
        j = i + 1                                           # ceiling: first start > start
        if j < len(self.starts) and self.starts[j] < end:
            return False
        self.starts.insert(j, start)
        self.ends.insert(j, end)
        return True
```

```java
// java
// LC 729 - My Calendar I
// IDEA: TreeMap start -> end; a booking is legal iff it fits between its two neighbours
// time = O(log n) per book, space = O(n)
class MyCalendar {
    private final TreeMap<Integer, Integer> booked = new TreeMap<>();   // start -> end

    public boolean book(int start, int end) {
        Integer prev = booked.floorKey(start);     // latest booking starting <= start
        if (prev != null && booked.get(prev) > start) {
            return false;                          // prev spills into us
        }
        Integer next = booked.ceilingKey(start);   // earliest booking starting >= start
        if (next != null && next < end) {
            return false;                          // we spill into next
        }
        booked.put(start, end);
        return true;
    }
}
```

**Traps**
- Intervals are half-open: `[10, 20)` then `[20, 30)` is legal. `>=` in the floor test rejects it.
- Python has no `TreeMap`: `list.insert` is O(n), fine for LC 729's 1,000 calls; say so, or use
  `sortedcontainers.SortedList`.

### My Calendar II — LC 731

> **Key point** — store `+1` at each start and `-1` at each end; a running sum over the keys in
> sorted order is the number of bookings active at that moment. Tentatively add the new booking,
> and roll it back if the sum ever reaches 3.

**Reduces to**: [design § Pattern 8](./design.md#pattern-8-sorted-map--time-index) — the sorted map used as a sparse difference array (the sweep-line count of [scanning_line.md](./scanning_line.md)).

```python
# python
# LC 731 - My Calendar II
# IDEA: delta map +1 / -1, sweep in key order; reject and roll back if 3 bookings overlap
# time = O(n log n) per book, space = O(n)
from collections import defaultdict

class MyCalendarTwo:
    def __init__(self):
        self.delta = defaultdict(int)

    def book(self, start, end):
        self.delta[start] += 1
        self.delta[end] -= 1
        active = 0
        for t in sorted(self.delta):
            active += self.delta[t]
            if active >= 3:                 # triple booking: undo and refuse
                self.delta[start] -= 1
                self.delta[end] += 1
                return False
        return True
```

**Traps**
- Checking before adding forces a second pass; adding first and rolling back is one sweep.
- The O(n²)-total cost is fine at 1,000 calls; the overlap-list solution (keep the double-booked
  intervals) is the same bound.

### My Calendar III — LC 732

> **Key point** — the same delta map as LC 731, never rolled back: the answer is the **maximum**
> running sum over the sweep.

**Reduces to**: [design § Pattern 8](./design.md#pattern-8-sorted-map--time-index) — the sweep of LC 731, reporting the peak instead of testing a cap.

```python
# python
# LC 732 - My Calendar III
# IDEA: delta map +1 / -1; the max prefix sum in key order is the max concurrent bookings
# time = O(n log n) per book, space = O(n)
from collections import defaultdict

class MyCalendarThree:
    def __init__(self):
        self.delta = defaultdict(int)

    def book(self, start, end):
        self.delta[start] += 1
        self.delta[end] -= 1
        active = best = 0
        for t in sorted(self.delta):
            active += self.delta[t]
            best = max(best, active)
        return best
```

```java
// java
// LC 732 - My Calendar III
// IDEA: TreeMap as a delta array on a sparse timeline; prefix-sum in key order
// time = O(n) per book (n = distinct endpoints), space = O(n)
class MyCalendarThree {
    private final TreeMap<Integer, Integer> delta = new TreeMap<>();

    public int book(int start, int end) {
        delta.merge(start, 1, Integer::sum);
        delta.merge(end, -1, Integer::sum);
        int active = 0;
        int best = 0;
        for (int d : delta.values()) {     // TreeMap iterates keys ascending
            active += d;
            best = Math.max(best, active);
        }
        return best;
    }
}
```

**Traps**
- A `-1` and a `+1` on the same key must cancel before the sum is read: `[10, 20)` and
  `[20, 30)` never overlap. The map merges them into one key, so this holds automatically.

### Range Module — LC 715

> **Key point** — keep the tracked ranges **disjoint, sorted, non-adjacent and non-empty**. Every
> operation finds the run of stored intervals it touches with two binary searches and replaces
> that whole run with at most two intervals, restoring the invariant before it returns.

**Reduces to**: [design § Pattern 8](./design.md#pattern-8-sorted-map--time-index) — the sorted interval map of LC 729, made mutable: writes merge neighbours, deletes split them. The segment-tree form is in [segment_tree.md](./segment_tree.md).

```python
# python
# LC 715 - Range Module
# IDEA: parallel sorted lists of disjoint [start, end); add/remove replace the touched run by slice assignment
# time = O(log n) search + O(n) slice per call, space = O(n)
import bisect

class RangeModule:
    def __init__(self):
        self.starts = []
        self.ends = []

    def addRange(self, left, right):
        i = bisect.bisect_left(self.ends, left)        # first interval ending at or after left
        j = bisect.bisect_right(self.starts, right)    # first interval starting after right
        if i < j:                                      # intervals i..j-1 touch [left, right): absorb them
            left = min(left, self.starts[i])
            right = max(right, self.ends[j - 1])
        self.starts[i:j] = [left]
        self.ends[i:j] = [right]

    def queryRange(self, left, right):
        i = bisect.bisect_right(self.starts, left) - 1  # the interval that could contain left
        return i >= 0 and self.ends[i] >= right

    def removeRange(self, left, right):
        i = bisect.bisect_right(self.ends, left)       # first interval ending after left
        j = bisect.bisect_left(self.starts, right)     # first interval starting at or after right
        if i >= j:
            return                                     # nothing overlaps [left, right)
        new_starts, new_ends = [], []
        if self.starts[i] < left:                      # keep the head piece [start, left)
            new_starts.append(self.starts[i])
            new_ends.append(left)
        if self.ends[j - 1] > right:                   # keep the tail piece [right, end)
            new_starts.append(right)
            new_ends.append(self.ends[j - 1])
        self.starts[i:j] = new_starts
        self.ends[i:j] = new_ends
```

```java
// java
// LC 715 - Range Module
// IDEA: TreeMap of merged disjoint [start, end); add absorbs neighbours, remove re-inserts the surviving pieces
// time = O(log n) amortised per call, space = O(n)
class RangeModule {
    private final TreeMap<Integer, Integer> ranges = new TreeMap<>();   // start -> end

    public void addRange(int left, int right) {
        Integer s = ranges.floorKey(left);
        Integer e = ranges.floorKey(right);
        if (s != null && ranges.get(s) >= left) {
            left = s;                                   // touches or overlaps on the left: absorb
        }
        if (e != null && ranges.get(e) > right) {
            right = ranges.get(e);                      // extends past right: absorb
        }
        ranges.subMap(left, true, right, true).clear(); // drop everything swallowed
        ranges.put(left, right);
    }

    public boolean queryRange(int left, int right) {
        Integer s = ranges.floorKey(left);
        return s != null && ranges.get(s) >= right;     // one merged interval must cover it all
    }

    public void removeRange(int left, int right) {
        Integer s = ranges.floorKey(left);
        Integer e = ranges.floorKey(right);
        if (e != null && ranges.get(e) > right) {
            ranges.put(right, ranges.get(e));           // keep the tail piece [right, oldEnd)
        }
        if (s != null && ranges.get(s) > left) {
            ranges.put(s, left);                        // keep the head piece [oldStart, left)
        }
        ranges.subMap(left, true, right, false).clear();
    }
}
```

**Dry run**

```text
add [10, 20)      -> [10,20)
remove [14, 16)   -> [10,14) [16,20)       (one interval split in two)
query [10, 14)    -> True
query [13, 15)    -> False                 (14..16 was removed)
query [16, 17)    -> True
```

**Traps**
- Adjacent ranges must merge: `add [10, 20)` then `add [20, 30)` is one range, or `query [15, 25)`
  wrongly fails. In `addRange` the search uses `ends >= left` and `starts <= right`, both inclusive.
- In `removeRange` the head and the tail can come from the **same** interval (a split); compute
  both pieces before replacing the run.

## Path Hierarchy Examples

### Design File System — LC 1166

> **Key point** — `createPath` only needs to know that the **parent path** exists, and the parent
> is the path up to its last `/`. A map from full path to value answers that in one lookup; no
> tree is needed because there is no `ls`.

**Reduces to**: [design § Pattern 9](./design.md#pattern-9-path-hierarchy) — the "map keyed by the full path" side of the choice.

```python
# python
# LC 1166 - Design File System
# IDEA: map full path -> value; a path may be created only if it is new and its parent exists
# time = O(L) per call for a path of length L, space = O(total path length)
class FileSystem:
    def __init__(self):
        self.value_of = {"": -1}             # the root's parent string, so "/a" has a parent

    def createPath(self, path, value):
        if not path or path == "/" or path in self.value_of:
            return False
        parent = path[:path.rfind("/")]
        if parent not in self.value_of:
            return False
        self.value_of[path] = value
        return True

    def get(self, path):
        if path == "":
            return -1
        return self.value_of.get(path, -1)
```

**Traps**
- `"/a/b"` before `"/a"` must fail: the parent check is the whole problem.
- Creating an existing path returns `False` and leaves the old value.

### Design Log Storage System — LC 635

> **Key point** — the timestamps are fixed-width `Year:Month:Day:Hour:Minute:Second` strings, so
> truncating to a granularity is a **prefix**, and comparing two prefixes as strings compares them
> as times. A range query is then two string comparisons per log.

**Reduces to**: [design § Pattern 9](./design.md#pattern-9-path-hierarchy) — a timestamp is a path of six components, and the granularity says how deep to compare.

```python
# python
# LC 635 - Design Log Storage System
# IDEA: keep (timestamp, id); compare the first k characters, where k is set by the granularity
# time = O(1) put, O(n) retrieve; space = O(n)
class LogSystem:
    PREFIX = {"Year": 4, "Month": 7, "Day": 10, "Hour": 13, "Minute": 16, "Second": 19}

    def __init__(self):
        self.logs = []                       # (timestamp, id)

    def put(self, id, timestamp):
        self.logs.append((timestamp, id))

    def retrieve(self, start, end, granularity):
        k = self.PREFIX[granularity]
        low, high = start[:k], end[:k]
        return [log_id for ts, log_id in self.logs if low <= ts[:k] <= high]
```

**Traps**
- Parsing into integers and comparing field by field works, but the fixed-width format is the
  trick the problem is built around.
- Both ends are inclusive at the chosen granularity.

### Design In-Memory File System — LC 588

> **Key point** — `ls`, `mkdir` and file writes all walk the same path, so the structure is a
> **trie of path components** whose nodes are directories holding child dirs and files. `ls` on a
> file returns just its name; on a directory, the sorted names of its children.

**Reduces to**: [design § Pattern 9](./design.md#pattern-9-path-hierarchy) — the trie side of the choice, needed because `ls` lists what is under a prefix.

```python
# python
# LC 588 - Design In-Memory File System
# IDEA: nested dict trie: each dir is {"dirs": {}, "files": {}}; walk the path's components
# time = O(L) per walk for a path of L components (+ O(k log k) to sort k names in ls), space = O(total content)
class FileSystem:
    def __init__(self):
        self.root = {"dirs": {}, "files": {}}

    def _walk(self, parts):
        """Return the directory at parts, creating missing ones."""
        node = self.root
        for part in parts:
            node = node["dirs"].setdefault(part, {"dirs": {}, "files": {}})
        return node

    def ls(self, path):
        parts = [p for p in path.split("/") if p]
        if parts:
            parent = self._walk(parts[:-1])
            if parts[-1] in parent["files"]:     # a file lists only itself
                return [parts[-1]]
        node = self._walk(parts)
        return sorted(list(node["dirs"]) + list(node["files"]))

    def mkdir(self, path):
        self._walk([p for p in path.split("/") if p])

    def addContentToFile(self, filePath, content):
        parts = [p for p in filePath.split("/") if p]
        directory = self._walk(parts[:-1])
        directory["files"][parts[-1]] = directory["files"].get(parts[-1], "") + content

    def readContentFromFile(self, filePath):
        parts = [p for p in filePath.split("/") if p]
        return self._walk(parts[:-1])["files"][parts[-1]]
```

**Traps**
- Python 3 `dict.keys() + dict.keys()` raises `TypeError`; build a list first.
- `ls("/")` splits into no components; the walk must return the root, not fail.

## Aggregate Counter Examples

### Design Tic-Tac-Toe — LC 348

> **Key point** — a player wins when one row, column or diagonal holds n of their marks. Count
> player 1 as `+1` and player 2 as `-1` per line; a line belongs to one player exactly when its
> sum is `±n`, and a move touches at most four lines.

**Reduces to**: [design § Pattern 10](./design.md#pattern-10-aggregate-counters-updated-on-write) — update the aggregates on the write so the read (who won?) is O(1).

```python
# python
# LC 348 - Design Tic-Tac-Toe
# IDEA: one running sum per row, column and both diagonals; +1 for player 1, -1 for player 2
# time = O(1) per move, space = O(n)
class TicTacToe:
    def __init__(self, n):
        self.n = n
        self.rows = [0] * n
        self.cols = [0] * n
        self.diagonal = 0
        self.anti_diagonal = 0

    def move(self, row, col, player):
        value = 1 if player == 1 else -1
        self.rows[row] += value
        self.cols[col] += value
        if row == col:
            self.diagonal += value
        if row + col == self.n - 1:
            self.anti_diagonal += value
        if self.n in (abs(self.rows[row]), abs(self.cols[col]), abs(self.diagonal), abs(self.anti_diagonal)):
            return player
        return 0
```

**Traps**
- The centre of an odd board is on both diagonals: two `if`s, not `if / elif`.
- Scanning the board after each move is O(n²) per move; the counters are O(1).

### Design Underground System — LC 1396

> **Key point** — the query is an average per `(start, end)` route, so keep the route's **total
> time and trip count**, updated at check-out. A passenger's open trip is the only per-person
> state, and it is deleted when the trip closes.

**Reduces to**: [design § Pattern 10](./design.md#pattern-10-aggregate-counters-updated-on-write) — the average is a sum and a count kept on write.

```python
# python
# LC 1396 - Design Underground System
# IDEA: open trips id -> (station, time); route (start, end) -> [total time, trips]
# time = O(1) per call, space = O(passengers + routes)
from collections import defaultdict

class UndergroundSystem:
    def __init__(self):
        self.open_trip = {}                          # id -> (station, time)
        self.route = defaultdict(lambda: [0, 0])     # (start, end) -> [total time, trips]

    def checkIn(self, id, stationName, t):
        self.open_trip[id] = (stationName, t)

    def checkOut(self, id, stationName, t):
        start_station, start_time = self.open_trip.pop(id)
        totals = self.route[(start_station, stationName)]
        totals[0] += t - start_time
        totals[1] += 1

    def getAverageTime(self, startStation, endStation):
        total, trips = self.route[(startStation, endStation)]
        return total / trips
```

**Traps**
- Storing every trip time and averaging on read makes `getAverageTime` O(trips).
- The route is directed: `(A, B)` and `(B, A)` are different keys.
