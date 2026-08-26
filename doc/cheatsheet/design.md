# Design

> **Scope** — LC "design a X" problems — reading the required operations off the problem and picking the structure combination that makes every one of them O(1) or O(log n); the worked designs themselves live in the examples sheet.
> **See also**: [design_examples.md](./design_examples.md) — the twenty designs written out in full; [design_patterns.md](./design_patterns.md) — consistent hashing, rate limiters and load balancing, asked in the same rounds but not LC problems; [ood_design.md](./ood_design.md) — class modelling, SOLID and design patterns for the LLD round; [iterator.md](./iterator.md) — the iterator contract in isolation; [hash_map.md](./hash_map.md) and [heap.md](./heap.md) — the structures most designs combine.

## LeetCode Problem Lists

- [Design](https://leetcode.com/problem-list/design/)
- [Data Stream](https://leetcode.com/problem-list/data-stream/)

## 0) Concept

### 0-1) Types
- **Data Structure Design**: Design custom data structures (Stack, Queue, HashMap, etc.)
- **Cache Design**: LRU, LFU, Time-based cache systems
- **System Component Design**: File systems, search systems, rate limiters
- **Social Network Design**: Twitter, Instagram feed, following/follower systems
- **Scheduling/Booking Design**: Calendar, meeting rooms, parking systems
- **Stream/Iterator Design**: Data stream processing, custom iterators
- **Game Design**: Tic-Tac-Toe, Snake, game boards

### 0-2) Pattern

#### Pattern 1: HashMap + LinkedList
- **Use Case**: Order-sensitive operations (LRU, LFU, insertion order)
- **Examples**: LRU Cache, LFU Cache, Insert Delete GetRandom O(1)
- **Key Point**: HashMap provides O(1) lookup, LinkedList provides O(1) ordering operations

#### Pattern 2: HashMap + Heap
- **Use Case**: Priority-based operations, top-k problems
- **Examples**: Design Twitter, Top K Frequent Elements in stream
- **Key Point**: HashMap tracks data, Heap maintains priority order

#### Pattern 3: Trie (Prefix Tree)
- **Use Case**: Autocomplete, prefix search, word validation
- **Examples**: Search Autocomplete System, Add and Search Word, Design Search System
- **Key Point**: Efficient prefix-based operations O(L) where L is word length

#### Pattern 4: OOD (Object-Oriented Design)
- **Use Case**: Complex system with multiple components and interactions
- **Examples**: Parking Lot, Elevator System, Library Management
- **Key Point**: Focus on classes, interfaces, relationships, and SOLID principles

#### Pattern 5: Stream/Queue Based
- **Use Case**: Real-time data processing, moving window operations
- **Examples**: Moving Average, Hit Counter, Rate Limiter
- **Key Point**: Deque or Queue for time-window based operations

## 1) General form

### 1-1) Basic OP

#### Step 1: Clarify Requirements
- What operations need to be supported?
- What are the time/space complexity requirements?
- What are the edge cases? (empty input, duplicates, concurrency)
- What is the expected scale? (single machine vs distributed)

#### Step 2: Choose Data Structures
- Map key requirements to appropriate data structures
- Consider trade-offs (time vs space, simplicity vs performance)
- Multiple data structures often needed (HashMap + List, HashMap + Heap, etc.)

#### Step 3: Define Class Structure
```python
class DesignName:
    def __init__(self, params):
        # Initialize data structures
        self.data_structure1 = {}
        self.data_structure2 = []

    def operation1(self, params):
        # Implement operation
        pass

    def operation2(self, params):
        # Implement operation
        pass
```

#### Step 4: Implement Core Operations
- Focus on the required methods
- Maintain invariants (data consistency between structures)
- Handle edge cases

#### Step 5: Optimize
- Identify bottlenecks
- Use appropriate data structures for O(1) operations when needed
- Consider lazy evaluation or caching

### 1-2) Interview Tips

#### Tip 1: Ask Clarifying Questions
- "Should this support concurrent access?" (Usually no for LC problems)
- "What should happen if we try to get a non-existent key?"
- "Are there any constraints on input size or range?"
- "Do we need to support deletion/updates?"

#### Tip 2: Start with Simple Solution
- Start with brute force using basic data structures
- Explain time/space complexity
- Then optimize based on requirements

#### Tip 3: Data Structure Selection
- **Need fast lookup?** → HashMap/HashSet
- **Need ordering?** → LinkedList, TreeMap, Heap
- **Need both?** → Combine them (HashMap + LinkedList for LRU)
- **Prefix operations?** → Trie
- **Range queries?** → Segment Tree, Binary Indexed Tree
- **Time-based operations?** → Queue/Deque with timestamps

#### Tip 4: Common Mistakes to Avoid
- Not maintaining consistency between multiple data structures
- Forgetting to handle edge cases (empty, single element, duplicates)
- Not considering time complexity of helper operations
- Over-engineering (keep it simple if requirements allow)

#### Tip 5: OOD Specific Tips
- Define clear interfaces and responsibilities
- Use meaningful class and method names
- Consider SOLID principles (especially Single Responsibility)
- Think about extensibility and maintainability

### 1-3) Things to Notice

#### Notice 1: OrderedDict in Python
- Combines HashMap and LinkedList functionality
- `move_to_end(key)`: O(1) operation to reorder
- `popitem(last=False)`: Remove first item (FIFO), `last=True` for LIFO
- Perfect for LRU/LFU cache implementations

#### Notice 2: Double Data Structure Pattern
- When using multiple data structures, ensure they stay synchronized
- Example: LRU uses `cache_dict` (lookup) + `cache_list` (order)
- Always update BOTH when adding/removing/modifying

#### Notice 3: Dummy Nodes for LinkedList
- Use dummy head/tail nodes to simplify edge cases
- Avoids null checks for head/tail operations
- Common in LRU Cache implementations

#### Notice 4: Time-based Expiration
- Use timestamp + cleanup strategy
- Lazy cleanup: Remove expired items when accessed
- Eager cleanup: Use heap/queue to track expiration times
- Trade-off: Space (keeping old data) vs Time (cleanup overhead)

#### Notice 5: defaultdict and Counter
```python
from collections import defaultdict, Counter
# Avoid key existence checks
followers = defaultdict(set)  # Auto-creates empty set
tweet_count = defaultdict(int)  # Auto-creates 0
```

### 1-4) Classic LC Problems by Category

#### Category 1: Cache Design ⭐⭐⭐
- **LC 146. LRU Cache** (Medium) - HashMap + DoublyLinkedList
- **LC 460. LFU Cache** (Hard) - HashMap + OrderedDict for frequency buckets
- **LC 432. All O(1) Data Structure** (Hard) - HashMap + DoublyLinkedList of buckets
- **LC 1756. Design Most Recently Used Queue** (Medium)

#### Category 2: Data Structure Design
- **LC 380. Insert Delete GetRandom O(1)** (Medium) - HashMap + ArrayList
- **LC 381. Insert Delete GetRandom O(1) - Duplicates** (Hard)
- **LC 211. Design Add and Search Words Data Structure** (Medium) - Trie
- **LC 208. Implement Trie (Prefix Tree)** (Medium)
- **LC 641. Design Circular Deque** (Medium)
- **LC 622. Design Circular Queue** (Medium)
- **LC 225. Implement Stack using Queues** (Easy)
- **LC 232. Implement Queue using Stacks** (Easy)

#### Category 3: Stream/Time-based Design
- **LC 346. Moving Average from Data Stream** (Easy) - Queue
- **LC 362. Design Hit Counter** (Medium) - Queue with timestamps
- **LC 353. Design Snake Game** (Medium) - Queue + Set
- **LC 1396. Design Underground System** (Medium) - HashMap
- **LC 981. Time Based Key-Value Store** (Medium) - HashMap + Binary Search

#### Category 4: File System Design
- **LC 1166. Design File System** (Medium) - HashMap for path storage
- **LC 588. Design In-Memory File System** (Hard) - Trie-like nested dict structure
- **LC 1244. Design A Leaderboard** (Medium) - HashMap + TreeMap

#### Category 5: Social Network Design
- **LC 355. Design Twitter** (Medium) - HashMap + Heap for feed merging
- **LC 1603. Design Parking System** (Easy) - Simple counter

#### Category 6: Search/Autocomplete Design
- **LC 642. Design Search Autocomplete System** (Hard) - Trie + Heap
- **LC 1268. Search Suggestions System** (Medium) - Trie or Sorting
- **LC 1146. Snapshot Array** (Medium) - HashMap for snapshots

#### Category 7: Iterator Design
- **LC 284. Peeking Iterator** (Medium) - Iterator wrapper with lookahead
- **LC 251. Flatten 2D Vector** (Medium) - Two pointers
- **LC 341. Flatten Nested List Iterator** (Medium) - Stack for DFS
- **LC 281. Zigzag Iterator** (Medium) - Queue of iterators

#### Category 8: Rate Limiter Design
- **LC 362. Design Hit Counter** (Medium) - Sliding window
- Design Token Bucket Rate Limiter (Common interview question)
- Design Leaky Bucket Rate Limiter (Common interview question)

#### Category 9: Game Design
- **LC 348. Design Tic-Tac-Toe** (Medium) - Row/Col/Diagonal counters
- **LC 353. Design Snake Game** (Medium) - Queue + Set
- **LC 1286. Iterator for Combination** (Medium)


## 2) Pattern Selection

A "design a X" question is never solved by one structure. It is solved by **naming the
operation that would be slow with the obvious structure, then adding a second structure whose
only job is to make that one operation fast.** Read the required operations off the problem
statement first, then pick the pair.

| Every operation must be… | The pair | Why neither half is enough alone | Worked at |
|---|---|---|---|
| O(1) get *and* O(1) eviction by recency | **hash map + doubly linked list** | the map finds the node; only a doubly linked node can unlink itself in O(1) | [1) LRU](./design_examples.md#1-lru-cache--lc-146-) |
| O(1) get *and* O(1) eviction by frequency | **hash map + map of frequency → linked list** | frequency turns eviction into "the head of the smallest non-empty bucket" | [2) LFU](./design_examples.md#2-lfu-cache--lc-460-), [3) All O(1)](./design_examples.md#3-all-o1-data-structure--lc-432-) |
| O(1) insert, delete *and* **uniform random** | **hash map + array, with swap-to-end deletion** | random needs contiguous indices; delete needs a lookup — the swap keeps both | [4) Insert Delete GetRandom](./design_examples.md#4-insert-delete-getrandom-o1--lc-380-) |
| O(1) push/pop *plus* O(1) min, max or count | **stack + a parallel stack of the aggregate** | the aggregate is only valid for a prefix of the stack, so it must be pushed and popped with it | [6) Min Stack](./design_examples.md#6-stack--auxiliary-state--o1-min-and-lazy-increment-lc-155--lc-1381-) |
| ordered queries — floor, ceiling, ranges | **balanced BST / TreeMap** (`SortedDict`, `TreeMap`) | a hash map has no order, so overlap and neighbour queries degrade to O(n) | [7) Ordered Map](./design_examples.md#7-ordered-map-treemap-for-booking--interval-design--lc-715--729--731--732--2034-) |
| running median, or "k-th largest so far" | **two heaps** (max-heap of the low half, min-heap of the high half) | keeping the halves balanced puts the answer at the two roots | [8) Two Heaps](./design_examples.md#8-two-heaps--running-median-lc-295-) |
| lookup "as of time T" | **hash map → sorted list + binary search** | values are appended in time order, so the search is over an already-sorted list | [9) Time Based KV](./design_examples.md#9-time-based-key-value-store--lc-981) |
| counting over a sliding time window | **deque or a circular buffer of buckets** | expired entries leave from the front while new ones arrive at the back | [10) Hit Counter](./design_examples.md#10-design-hit-counter--lc-362) |
| prefix / wildcard matching on strings | **trie**, optionally with a heap or cached top-k per node | O(L) in the query length instead of O(n·L) over all words | [11) Autocomplete](./design_examples.md#11-design-search-autocomplete-system--lc-642), [12) Add and Search Words](./design_examples.md#12-design-add-and-search-words-data-structure--lc-211) |
| a feed merged from k followed sources | **hash map + heap over per-source cursors** | merge k sorted lists, but lazily — you only need the first `n` | [16) Design Twitter](./design_examples.md#16-design-twitter--lc-355) |
| a hierarchy addressed by path | **trie of path components**, or a map keyed by the full path | the choice is exactly whether `ls` on a prefix must be supported | [13)](./design_examples.md#13-design-file-system--lc-1166), [14)](./design_examples.md#14-design-in-memory-file-system--lc-588) |

### The four questions to ask before writing a line

1. **Which operations, and what complexity does each need?** Write the class skeleton with the
   required complexity as a comment on every method *before* choosing structures. Most wrong
   answers come from optimising an operation the problem never asked to be fast.
2. **What is the eviction or expiry rule?** Recency, frequency, and a time window need three
   different second structures, and the rule is what tells you which.
3. **Do queries need order?** If any query is "nearest", "before", "overlapping" or "range",
   a hash map cannot be the only index.
4. **What is the invariant, and where is it restored?** For two heaps it is the size balance;
   for LRU it is "head is newest"; for lazy increment it is "the pending delta applies to
   everything below". Name it, then make every method end by restoring it.

## 3) Worked Examples

Twenty designs, grouped by the structure pair each one forces, live in
**[design_examples.md](./design_examples.md)**:

| Group | Problems |
|---|---|
| [Caches & eviction policies](./design_examples.md#caches--eviction-policies) | LC 146, 460, 432, 380 |
| [Stacks with auxiliary state](./design_examples.md#stacks-with-auxiliary-state) | LC 895, 155, 1381 |
| [Ordered maps, heaps & time windows](./design_examples.md#ordered-maps-heaps--time-windows) | LC 715, 729, 731, 732, 2034, 295, 981, 362 |
| [Tries & prefix search](./design_examples.md#tries--prefix-search) | LC 642, 211 |
| [File systems & paths](./design_examples.md#file-systems--paths) | LC 1166, 588, 635 |
| [Feeds, games & simulation](./design_examples.md#feeds-games--simulation) | LC 355, 348, 353, 1396 |

## 4) System Design Coding Patterns

Consistent hashing, the two rate limiters and the load-balancing algorithms moved to
**[design_patterns.md](./design_patterns.md)**. They are asked *as coding questions* in the same
rounds as the problems above, but none of them is a LeetCode problem, and keeping them here made
this sheet's scope two subjects wide.
