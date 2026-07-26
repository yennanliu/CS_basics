# Java Collection FAQ

- https://javaguide.cn/java/collection/java-collection-questions-01.html
- https://javaguide.cn/java/collection/java-collection-questions-02.html

## 1) The Collection Hierarchy

```
Iterable
 └─ Collection
     ├─ List      → ordered, indexed, allows duplicates
     │    ├─ ArrayList        (resizable array)
     │    ├─ LinkedList       (doubly linked list; also a Deque)
     │    └─ Vector           (legacy, synchronized)
     ├─ Set       → no duplicates
     │    ├─ HashSet          (unordered, backed by HashMap)
     │    ├─ LinkedHashSet    (insertion order)
     │    └─ TreeSet          (sorted, backed by TreeMap)
     └─ Queue / Deque → FIFO / double-ended
          ├─ ArrayDeque       (fast stack/queue)
          ├─ LinkedList
          └─ PriorityQueue    (heap-ordered)

Map (NOT a Collection)
 ├─ HashMap            (unordered)
 ├─ LinkedHashMap      (insertion / access order)
 ├─ TreeMap            (sorted by key)
 ├─ Hashtable          (legacy, synchronized)
 └─ ConcurrentHashMap  (thread-safe, high concurrency)
```

---

## 2) ArrayList vs LinkedList

| Operation | ArrayList | LinkedList |
|-----------|-----------|------------|
| Get by index | `O(1)` | `O(n)` |
| Add/remove at end | `O(1)` amortized | `O(1)` |
| Add/remove at head/middle | `O(n)` (shift) | `O(1)` if node known, else `O(n)` to find |
| Memory | Compact (array) | Extra pointers per node |
| Cache locality | Good | Poor |

**Rule of thumb**: use `ArrayList` by default. `LinkedList` is rarely the right
choice; use `ArrayDeque` for stack/queue needs instead.

- `ArrayList` grows by ~1.5x when full — presize with `new ArrayList<>(n)` if the
  size is known to avoid repeated copies.

---

## 3) HashMap Internals

Backed by an **array of buckets** (`Node[] table`). Each bucket holds entries
whose keys hash to that index.

```
index = (n - 1) & hash(key)     // n = table length (always a power of 2)
```

- **hash spreading**: `hash = h ^ (h >>> 16)` mixes high bits into low bits so
  more of the hash influences the bucket index.
- **Collisions** are chained:
  - Java 7: linked list (new nodes prepended).
  - Java 8+: **linked list, then treeified to a red-black tree** when a bucket has
    ≥ 8 entries **and** table size ≥ 64. Lookup in that bucket becomes `O(log n)`
    instead of `O(n)`. Untreeifies back to a list when it shrinks to ≤ 6.
- **Load factor** (default `0.75`): when `size > capacity * loadFactor`, the table
  **resizes** (doubles capacity) and entries are **rehashed**. 0.75 balances space
  vs collision rate.
- **Default capacity**: 16.
- **`null` keys**: allowed (stored in bucket 0); `null` values allowed too.
- **Not thread-safe** — concurrent writes can corrupt the structure.

**equals / hashCode contract** (critical):
- If `a.equals(b)` then `a.hashCode() == b.hashCode()`.
- Unequal objects *may* share a hash (collision) but ideally don't.
- Override **both** or hash-based collections break. Use immutable fields for keys.

---

## 4) HashSet, LinkedHashMap, TreeMap

- **HashSet**: a thin wrapper over a `HashMap` where elements are keys and values
  are a dummy constant. `O(1)` average add/contains/remove; no order.
- **LinkedHashMap**: `HashMap` + a doubly linked list threading entries in
  insertion (or access) order. Access-order mode makes it a ready-made **LRU cache**
  (override `removeEldestEntry`).
- **TreeMap**: red-black tree, keys kept **sorted** (natural order or `Comparator`).
  `O(log n)` ops; supports range queries (`floorKey`, `ceilingKey`, `subMap`).

---

## 5) ConcurrentHashMap

Thread-safe map for high concurrency, far better than `Hashtable` /
`Collections.synchronizedMap` (which lock the whole map).

- **Java 7**: segment locking (lock striping) — the map split into segments,
  each independently locked.
- **Java 8+**: dropped segments. Uses **CAS + synchronized on the head node of a
  bucket**, so locking is per-bucket → high write concurrency. Reads are lock-free.
- Does **not** allow `null` keys or values (ambiguous with "absent" in concurrent
  reads).
- Atomic helpers: `putIfAbsent`, `computeIfAbsent`, `merge`.

---

## 6) Big-O Cheat Sheet

| Structure | Access | Search | Insert | Delete | Notes |
|-----------|--------|--------|--------|--------|-------|
| ArrayList | `O(1)` | `O(n)` | `O(1)`* | `O(n)` | *amortized at end |
| LinkedList | `O(n)` | `O(n)` | `O(1)` | `O(1)` | at known position |
| HashMap / HashSet | — | `O(1)` | `O(1)` | `O(1)` | average; worst `O(log n)` since Java 8 |
| TreeMap / TreeSet | — | `O(log n)` | `O(log n)` | `O(log n)` | sorted |
| ArrayDeque | `O(1)` ends | `O(n)` | `O(1)` ends | `O(1)` ends | best stack/queue |
| PriorityQueue | `O(1)` peek | `O(n)` | `O(log n)` | `O(log n)` | binary heap |

---

## 7) Common Gotchas

- **Fail-fast iterators**: modifying a collection during iteration (except via
  `Iterator.remove()`) throws `ConcurrentModificationException`.
- **`Arrays.asList()`** returns a fixed-size list backed by the array — `add`/`remove` throw.
- Prefer interfaces in declarations: `List<T> x = new ArrayList<>()`.
- Choose `ArrayDeque` over `Stack` (legacy, synchronized) for stacks.

### how does java implement HashMap ?
- `Array + Linked list` (list treeifies to a red-black tree per bucket in Java 8+)
- https://juejin.cn/post/6985369345207566373#heading-7
- https://lushunjian.github.io/blog/2019/01/02/HashMap%E7%9A%84%E5%BA%95%E5%B1%82%E5%AE%9E%E7%8E%B0/
- https://javaguide.cn/java/collection/java-collection-questions-02.html#hashmap-%E7%9A%84%E5%BA%95%E5%B1%82%E5%AE%9E%E7%8E%B0
- https://javaguide.cn/java/collection/hashmap-source-code.html#hashmap-%E5%B8%B8%E7%94%A8%E6%96%B9%E6%B3%95%E6%B5%8B%E8%AF%95
