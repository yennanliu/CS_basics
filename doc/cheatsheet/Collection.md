# Collection 

> **Scope** — Cross-collection **chooser** — which Java / Python container for which job, and the API gotchas of each.
> **See also**: [hash_map.md](./hash_map.md); [set.md](./set.md); [heap.md](./heap.md); [queue.md](./queue.md); [stack.md](./stack.md).

## LeetCode Problem Lists

- [Array](https://leetcode.com/problem-list/array/)
- [Hash Table](https://leetcode.com/problem-list/hash-table/)
- [Linked List](https://leetcode.com/problem-list/linked-list/)
- [Heap (Priority Queue)](https://leetcode.com/problem-list/heap-priority-queue/)

## 0) Concept  

### 0-1) Types

- Algorithm
    - dict/collections op
        - collections.Counter
        - collections.Counter().most_common()
    - sort
    - get most freq
    - get sub-string with validated alphabets
    - custom sort
        - LC 791

- Data structure
    - dict
    - set
    - array

## 1) General form

### 1-0) get element if existed in collections (custom sort)
```python
# LC 791. Custom Sort String
# ...
s_map = Counter(s)
res = ""
for o in order:
    if o in s_map:
        res += (o * s_map[o])
        del s_map[o]
for s in s_map:
    res += (s * s_map[s])
# ...
```

#### 1-1) collection.Counter
```python
import collections
s = ['a','b','c','c']
c= collections.Counter(s)
print (c)
print (c.keys())
print (c.values())
```

#### 1-2) Important method - most_common()
```python
# 451 Sort Characters By Frequency
import collections
s = ['a','b','c','c']
count = collections.Counter(s).most_common()
for item, freq in count:
    print (item, freq)   
#c 2
#a 1
#b 1
```

#### 1-3) collection.defaultdict (int, list...)
```python
import collections
s = ['a','b','c','c']
count = collections.defaultdict(int)
for i in s:
    count[i] += 1 

print (count)
print (dict(count))
```

```python
import collections
s=[('yellow', 1), ('blue', 2), ('yellow', 3), ('blue', 4), ('red', 1)]
count = collections.defaultdict(list)
for k, v in s:
    count[k].append(v)

print (count)
print (count.keys())
print (count.values())
print(count.items())
```  

#### 1-4) collection.update()
```python
# LC # 554 rick Wall
import collections
In [87]: _counter = collections.Counter()

In [88]: _counter
Out[88]: Counter()

In [89]: _counter.update([1])

In [90]: _counter
Out[90]: Counter({1: 1})

In [91]: _counter.update([1])

In [92]: _counter
Out[92]: Counter({1: 2})

In [93]: _counter.update([2])

In [94]: _counter
Out[94]: Counter({1: 2, 2: 1})
```

#### 1-5) `OrderedDict` ( hashmap + linked list) — LC 146
```python
# LC 146 LRU Cache

# There is a structure called ordered dictionary, it combines behind both hashmap and linked list. In Python this structure is called OrderedDict and in Java LinkedHashMap.

# https://docs.python.org/3/library/collections.html#collections.OrderedDict
# https://codertw.com/%E7%A8%8B%E5%BC%8F%E8%AA%9E%E8%A8%80/367557/
# https://www.w3help.cc/a/202107/420653.html

"""

# OrderedDict = hashmap + linked list
# CAN make dict ordering (default dict is NOT ordering)
# Return an instance of a dict subclass that has methods specialized for rearranging dictionary order

* popitem(last=True)
    The popitem() method for ordered dictionaries returns and removes a (key, value) pair. The pairs are returned in LIFO order if last is true or FIFO order if false.

* move_to_end(key, last=True)
    Move an existing key to either end of an ordered dictionary. The item is moved to the right end if last is true (the default) or to the beginning if last is false. Raises KeyError if the key does not exist:

"""

#----------------------------
# example 0
#----------------------------

# default dict
In [34]: d = {}
    ...: d['a'] = 'A'
    ...: d['b'] = 'B'
    ...: d['c'] = 'C'
    ...: d['d'] = 'D'
    ...: d['e'] = 'E'
    ...:
    ...: for k, v in d.items():
    ...:     print (k, v)
    ...:
# NON ordering
a A
b B
c C
d D
e E

# OrderedDict
In [35]: from collections import OrderedDict
    ...: d = OrderedDict()
    ...: d['a'] = 'A'
    ...: d['b'] = 'B'
    ...: d['c'] = 'C'
    ...: d['d'] = 'D'
    ...: d['e'] = 'E'
    ...:
    ...: for k, v in d.items():
    ...:     print (k, v)
    ...:

# ordering !!!
a A
b B
c C
d D
e E


#----------------------------
# example 1
#----------------------------
In [28]:  d = OrderedDict.fromkeys('abcde')

In [29]: d
Out[29]: OrderedDict([('a', None), ('b', None), ('c', None), ('d', None), ('e', None)])

In [30]: d.move_to_end('b')

In [31]: "".join(d)
Out[31]: 'acdeb'

In [32]:

In [32]: d.move_to_end('b', last=False)

In [33]: "".join(d)
Out[33]: 'bacde'

#----------------------------
# example 2
#----------------------------
class LastUpdatedOrderedDict(OrderedDict):
    'Store items in the order the keys were last added'

    def __setitem__(self, key, value):
        super().__setitem__(key, value)
        self.move_to_end(key)

#----------------------------
# example 3
#----------------------------
from time import time

class TimeBoundedLRU:
    "LRU Cache that invalidates and refreshes old entries."

    def __init__(self, func, maxsize=128, maxage=30):
        self.cache = OrderedDict()      # { args : (timestamp, result)}
        self.func = func
        self.maxsize = maxsize
        self.maxage = maxage

    def __call__(self, *args):
        if args in self.cache:
            self.cache.move_to_end(args)
            timestamp, result = self.cache[args]
            if time() - timestamp <= self.maxage:
                return result
        result = self.func(*args)
        self.cache[args] = time(), result
        if len(self.cache) > self.maxsize:
            self.cache.popitem(0)
        return result
```

## 2) LC Example

### 2-1) Custom Sort String — LC 791
```python
# LC 791. Custom Sort String
# V0
# IDEA : COUNTER
from collections import Counter
class Solution(object):
    def customSortString(self, order, s):
        s_map = Counter(s)
        res = ""
        for o in order:
            if o in s_map:
                res += (o * s_map[o])
                del s_map[o]
        for s in s_map:
            res += s * s_map[s]
        return res
```

## 3) Which collection should I reach for? ⭐⭐⭐⭐⭐

> This doc is the **selection guide**. Once the structure is chosen, the algorithm depth lives in the
> specialised docs — see [3-5) Deep-dive docs](#3-5-deep-dive-docs).

### 3-1) Collection cheat table

| Collection | Lookup by key | Lookup by index | Min / Max | Ordered iteration | Java / Python |
|---|---|---|---|---|---|
| **Array / List** | O(N) | **O(1)** | O(N) | insertion order | `ArrayList` / `list` |
| **Hash Map** | **O(1)** avg | – | O(N) | no order | `HashMap` / `dict`, `Counter` |
| **Hash Set** | **O(1)** avg | – | O(N) | no order | `HashSet` / `set` |
| **Heap (PQ)** | O(N) | – | **O(1)** peek, O(log N) pop | no order | `PriorityQueue` / `heapq` |
| **Deque** | O(N) | O(1) both ends | O(1) *if monotonic* | insertion order | `ArrayDeque` / `collections.deque` |
| **Linked List** | O(N) | O(N) | O(N) | insertion order | manual node / manual node |
| **Ordered Map** | O(log N) + **floor/ceiling** | – | O(log N) | **sorted by key** | `TreeMap` / `bisect` on sorted list |
| **Insertion-ordered Map** | **O(1)** avg | – | O(N) | **insertion / access order** | `LinkedHashMap` / `OrderedDict` |

**Key Idea**: pick the *weakest* structure that still answers the query. Reaching for a heap when a
counter suffices (or a `TreeMap` when a plain `HashMap` suffices) costs a `log N` factor and interview points.

### 3-2) Problem signal → collection → why

| Signal in the problem | Reach for | Why | LC |
|---|---|---|---|
| "count / frequency / anagram / can we build X from Y" | **hash map** (`Counter`) | O(1) per char, replaces a nested loop | 383, 819 |
| "top K / K closest / K most frequent" | **heap of size K** | keep only K, drop the rest → O(N log K) | 973, 692, 621 |
| "merge K sorted things" | **min-heap of the K heads** | next global min in O(log K) | 23 |
| "longest / shortest window satisfying a constraint" | **sliding window + counter map** | window state is O(1) to update on both ends | 3, 904 |
| "subarray sums to K / count such subarrays" | **prefix sum + hash map** | turn "range" into "difference of two prefixes" | 560 |
| "seen the same value within distance k" | **hash map `value → last index`** | last index is all you need, not all indices | 219 |
| "split so each letter appears in one part" | **hash map `char → last index`** | last index defines the mandatory reach | 763 |
| "custom alphabet / non-standard ordering" | **array-of-26 rank map** | rank lookup O(1), beats a comparator | 953, 791 |
| "clone a structure with arbitrary pointers" | **hash map `old node → new node`** | resolves forward references in one pass | 138 |
| "insert / delete / **getRandom** all O(1)" | **array + `value → index` map** | array gives random pick; swap-with-last keeps delete O(1) | 380 |
| "cache with eviction, everything O(1)" | **hash map + doubly linked list** | map = lookup, list = recency order | 146, 460 |
| "duplicate inside each row / col / box" | **array of hash sets** | one set per constraint group, single pass | 36 |
| "max / min of **every** window of size k" | **monotonic deque** (heap only with lazy deletion) | dominated elements are never needed again | 239 |
| "do two lists / paths meet?" | **hash set of nodes** first, then optimise to 2 pointers | set proves the idea; pointers remove O(N) space | 160 |
| "floor / ceiling / predecessor of a key" | **ordered map** (`TreeMap` / `bisect`) | plain hash map cannot answer "nearest key" | 699, 218 |
| "sweep line: what is active *right now*" | **heap + hash map** (lazy deletion) or ordered multiset | no decrease-key → mark stale, pop on read | 218 |
| "value → symbol, greedy largest first" | **two parallel arrays** (descending) | ordering is fixed and tiny; a map loses it | 12 |
| "merge two sorted arrays **in place**" | **no extra collection** — 2 pointers from the back | writing backwards never overwrites unread data | 88 |
| "grid: islands / regions / reachability" | **`visited` 2D array + queue/stack** | dense integer keys → array beats a hash set | 200, 419 |
| "visit grid targets in a required order" | **sorted list of targets + BFS per leg** | order is global, distance is local | 675 |
| "iterator needs one-element lookahead" | **cache the next element** in a field | the underlying iterator is not rewindable | 284 |
| "build / merge a list, head may change" | **dummy head node**, no aux collection | removes all head special-casing | 21, 2 |

### 3-3) Combinations that matter in interviews

| Combination | Invariant to keep | LC | Full template |
|---|---|---|---|
| **hash map + doubly linked list** | map value points *at the node*, so unlink is O(1) | 146, 460 | [design.md](design.md) `2-3` / `2-4` |
| **array + `value → index` map** | on delete: copy last element into the hole, **then** fix its index in the map, then `pop()` | 380 | [array.md](array.md) — LC 380 pattern |
| **heap + hash map ("lazy deletion")** | heap may hold stale entries; pop from the top **only while** the top disagrees with the map | 218 | [heap.md](heap.md) Template 8 |
| **ordered map (floor / ceiling)** | keys stay sorted; `floorKey(x)` = greatest key ≤ x | 699, 218 | [hash_map.md](hash_map.md), [java_trick.md](java_trick.md) |
| **prefix sum + hash map** | seed `{0: 1}` before the loop, and read *before* writing the current prefix | 560 | [prefix_sum.md](prefix_sum.md) |
| **sliding window + counter map** | shrink while invalid; delete zero-count keys or `len(map)` lies | 3, 904 | [sliding_window.md](sliding_window.md) |
| **array of hash sets** | one set per independent constraint, all filled in a single pass | 36 | [array.md](array.md) |

> **Gotcha**: `Counter` / `defaultdict` **create keys on read** (`d[k]` inserts). When the answer depends on
> `len(d)` (distinct-count windows), delete keys that hit 0, or read with `d.get(k, 0)`.

### 3-4) Java equivalents of the Python ops above

The sections above are Python-only; this is the same toolbox in Java.

| Python | Java |
|---|---|
| `Counter(s)` / `defaultdict(int)` | `map.merge(k, 1, Integer::sum)` or `map.getOrDefault(k, 0) + 1` |
| `defaultdict(list)` | `map.computeIfAbsent(k, x -> new ArrayList<>()).add(v)` |
| `Counter(...).most_common(k)` | size-K `PriorityQueue` with a comparator |
| `OrderedDict` (+ `move_to_end`, `popitem`) | `LinkedHashMap(cap, 0.75f, true)` (+ `removeEldestEntry`) |
| `bisect` on a sorted list | `TreeMap.floorKey` / `ceilingKey` |
| `set` | `HashSet` (`LinkedHashSet` to keep insertion order) |

```java
// java
// IDEA: Java counterparts of Counter / defaultdict / most_common / OrderedDict / bisect
import java.util.*;

public class CollectionOps {

    // 1) COUNT  == Counter(s)
    // time = O(N), space = O(K)   (K = distinct keys)
    public static Map<Character, Integer> count(String s) {
        Map<Character, Integer> cnt = new HashMap<>();
        for (char c : s.toCharArray()) {
            cnt.merge(c, 1, Integer::sum);   // or cnt.put(c, cnt.getOrDefault(c, 0) + 1)
        }
        return cnt;
    }

    // 2) GROUP  == defaultdict(list)
    // time = O(N), space = O(N)
    public static Map<String, List<Integer>> group(String[] keys, int[] vals) {
        Map<String, List<Integer>> m = new HashMap<>();
        for (int i = 0; i < keys.length; i++) {
            m.computeIfAbsent(keys[i], k -> new ArrayList<>()).add(vals[i]);
        }
        return m;
    }

    // 3) MOST COMMON  == Counter(...).most_common(k)      // LC 692 Top K Frequent Words
    // time = O(N + K log K), space = O(K)
    public static List<String> topK(String[] words, int k) {
        Map<String, Integer> cnt = new HashMap<>();
        for (String w : words) cnt.merge(w, 1, Integer::sum);
        // min-heap of size k: LOWEST freq at the top; tie -> lexicographically LARGER pops first
        PriorityQueue<String> pq = new PriorityQueue<>(
            (a, b) -> cnt.get(a).equals(cnt.get(b)) ? b.compareTo(a) : cnt.get(a) - cnt.get(b));
        for (String w : cnt.keySet()) {
            pq.offer(w);
            if (pq.size() > k) pq.poll();   // evict the worst -> heap never exceeds k
        }
        LinkedList<String> res = new LinkedList<>();
        while (!pq.isEmpty()) res.addFirst(pq.poll());   // heap pops worst-first -> push front
        return res;
    }

    // 4) ORDERED DICT  == OrderedDict                      // LC 146 LRU Cache
    // time = O(1) per get/put, space = O(capacity)
    static class LRUCache extends LinkedHashMap<Integer, Integer> {
        private final int cap;
        LRUCache(int cap) {
            super(cap, 0.75f, true);   // accessOrder = true -> get() moves the key to the end
            this.cap = cap;
        }
        @Override
        protected boolean removeEldestEntry(Map.Entry<Integer, Integer> eldest) {
            return size() > cap;       // auto-evict the least recently used
        }
    }

    // 5) FLOOR / CEILING  == bisect on a sorted list       // LC 699 / LC 218
    // time = O(log N) per query, space = O(N)
    public static String floorCeil(TreeMap<Integer, Integer> tm, int q) {
        return tm.floorKey(q) + " / " + tm.ceilingKey(q);   // greatest <= q / smallest >= q
    }
}
```

```python
# python
# IDEA: the same 5 ops in Python (this is what the Java above mirrors)
import bisect, heapq
from collections import Counter, defaultdict, OrderedDict

# 1) COUNT
# time = O(N), space = O(K)
Counter("abcc")                       # Counter({'c': 2, 'a': 1, 'b': 1})

# 2) GROUP
# time = O(N), space = O(N)
g = defaultdict(list)
for k, v in [('yellow', 1), ('blue', 2), ('yellow', 3)]:
    g[k].append(v)                    # {'yellow': [1, 3], 'blue': [2]}

# 3) MOST COMMON : freq DESC, then lexicographically ASC     # LC 692
# time = O(N + K log K), space = O(K)
def top_k(words, k):
    cnt = Counter(words)
    return heapq.nsmallest(k, cnt.keys(), key=lambda w: (-cnt[w], w))

# 4) ORDERED DICT : LRU ops                                   # LC 146
# time = O(1) per op, space = O(capacity)
d = OrderedDict()
d[1] = 1; d[2] = 2
d.move_to_end(1)                      # "touch" key 1 -> most recent
d[3] = 3
d.popitem(last=False)                 # evict LEAST recent (key 2) -> keys are [1, 3]

# 5) FLOOR / CEILING on a sorted list (Python has no TreeMap) # LC 699 / LC 218
# time = O(log N) query, O(N) insert  (use sortedcontainers.SortedList for O(log N) insert)
keys, q = [10, 20, 30], 25
i = bisect.bisect_right(keys, q)
floor_ = keys[i - 1] if i else None            # 20
ceil_ = keys[i] if i < len(keys) else None     # 30
```

### 3-5) Deep-dive docs

| Doc | Use it for |
|---|---|
| [hash_map.md](hash_map.md) | hash map patterns, collisions, `TreeMap` ordered-map API |
| [hashing.md](hashing.md) | rolling hash, custom hash keys |
| [set.md](set.md) | set-specific patterns, dedup, membership |
| [array.md](array.md) | in-place tricks, index-as-hash, LC 380 pattern |
| [linked_list.md](linked_list.md) | dummy head, 2 pointers, reversal |
| [heap.md](heap.md) | top-K, K-way merge, lazy deletion (Template 8) |
| [queue.md](queue.md) / [monotonic_queue.md](monotonic_queue.md) | deque, sliding-window extrema (LC 239) |
| [design.md](design.md) | LRU / LFU / iterator-style design problems (LC 146, 460, 284) |
| [java_trick.md](java_trick.md) / [python_trick.md](python_trick.md) | language-specific collection APIs |

### 3-6) More practice

- LC 380 Insert Delete GetRandom O(1) — array + `value → index` map
- LC 460 LFU Cache — hash map + frequency buckets of ordered maps
- LC 692 Top K Frequent Words — counter + size-K heap (tie-break on the word)
- LC 763 Partition Labels — `char → last index` map
- LC 560 Subarray Sum Equals K — prefix sum + hash map
- LC 36 Valid Sudoku — array of hash sets
- LC 138 Copy List with Random Pointer — `old node → new node` map
- LC 219 Contains Duplicate II — `value → last index` map
- LC 953 Verifying an Alien Dictionary — char rank array
- LC 699 Falling Squares — ordered map / coordinate sweep