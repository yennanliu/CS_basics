# Algorithms Under a Memory Cap

> **Scope** — What to do when the input does not fit in RAM: bit vectors, bucket-then-refine passes, sharding a file by hash, and external merge sort — the techniques that still return an **exact** answer by paying with extra passes.
> **See also**: [streaming_algorithms.md](./streaming_algorithms.md) — the one-pass, *approximate* half of the same problem (Bloom filter, count-min, reservoir sampling); [bit_manipulation.md](./bit_manipulation.md) — the bit operators used below; [sort.md](./sort.md) — the in-RAM sorts these build on.

## LeetCode Problem Lists

- [Bit Manipulation](https://leetcode.com/problem-list/bit-manipulation/)
- [Sorting](https://leetcode.com/problem-list/sorting/)
- [Design](https://leetcode.com/problem-list/design/)

## 0) Concept

Interviewers phrase this family the same way every time: *"you have 4 billion numbers and
10 MB of memory"*, *"find duplicates in an array of 1 to 32,000 with 4 KB"*, *"sort 1 TB
with 1 GB of RAM"*. The question is never which algorithm is fastest — it is **which
representation of the data fits in the budget**, and how many passes you are willing to
spend to keep the answer exact.

Three moves cover nearly all of it:

1. **Shrink each item** — one bit instead of one 4-byte int is a 32x win.
2. **Shrink the problem** — one cheap pass narrows the search to a slice small enough to
   solve properly in a second pass.
3. **Split the problem** — cut the data into independent pieces that each fit, using a
   split rule that keeps related items together.

### 0-1) Do the budget arithmetic first ⭐⭐⭐⭐⭐

Say the numbers out loud before proposing anything. This is the part being graded.

| Values to track | As 4-byte `int`s | As **one bit each** |
|---|---|---|
| 32,000 | 128 KB | **4 KB** |
| 1 million | 4 MB | **125 KB** |
| 2^31 (all non-negative `int`s) | 8 GB | **256 MB** |
| 2^32 (all `int`s) | 16 GB | **512 MB** |

And the other direction — what a budget buys:

```text
10 MB  = 10 * 2^20 bytes * 8  ~= 83.9 million bits    -> 83.9M distinct flags
       = 10 * 2^20 / 4        ~= 2.6 million ints     -> 2.6M counters
1 GB   = 2^30 bytes * 8        = 8.6 billion bits     -> a bit per 32-bit value, twice over
```

So "4 billion numbers, 1 GB" is a **one-pass bit vector** problem, and "4 billion numbers,
10 MB" is not — 10 MB cannot hold 2^32 bits, so it needs the two-pass refinement in §1-2.

### 0-2) Which technique ⭐⭐⭐⭐

| Signal in the question | Technique | Memory | Passes |
|---|---|---|---|
| Membership / duplicates over a **bounded value range** | Bit vector (§1-1) | 1 bit per possible value | 1 |
| A bit vector for the whole range does **not** fit | Bucket count, then refine (§1-2) | 1 counter per block + 1 block of bits | 2 |
| Keys must be grouped (dedup, count, join, top-k) | Shard by hash (§1-3) | 1 bucket at a time | 2 |
| The output must be **ordered** | External merge sort (§1-4) | 1 chunk, then 1 record per run | 1 + log_k(runs) |
| An approximate answer is acceptable | Sketches — see [streaming_algorithms.md](./streaming_algorithms.md) | sublinear | 1 |

## 1) General form

### 1-1) Bit vector — one bit per possible value ⭐⭐⭐⭐⭐

A hash set of ints costs tens of bytes per entry — a boxed `Integer` plus a `HashMap` node
is ~32–48 bytes in Java, and a Python `set` is in the same league once its load factor is
counted. When the values are **dense and bounded**, drop to one bit and index it
arithmetically: `pos >> 3` picks the byte, `pos & 7` picks the bit inside it.

```python
# python
# GENERAL PATTERN: bit vector (bitset) over values 0 .. size-1
# IDEA: byte i of the array holds the flags for values 8i .. 8i+7
# time = O(1) per op, space = size BITS (size/8 bytes)
class BitVector(object):
    def __init__(self, size):
        self.bits = bytearray((size + 7) // 8)   # 1 byte == 8 flags

    def get(self, pos):
        return (self.bits[pos >> 3] >> (pos & 7)) & 1

    def set(self, pos):
        self.bits[pos >> 3] |= 1 << (pos & 7)
```

```java
// java
// GENERAL PATTERN: bit vector over values 0 .. size-1
// IDEA: word (pos >> 5) of an int[] holds the flags for 32 consecutive values
// time = O(1) per op, space = size BITS
class BitVector {
    private final int[] words;

    BitVector(int size) { words = new int[(size >> 5) + 1]; }   // /32, rounded up

    boolean get(int pos) { return (words[pos >> 5] & (1 << (pos & 31))) != 0; }

    void set(int pos)    { words[pos >> 5] |= 1 << (pos & 31); }
}
```

> `pos & 31` is `pos % 32` and `pos >> 5` is `pos / 32` — both exact because 32 is a power
> of two. Java's own `java.util.BitSet` does exactly this; write it out anyway when the
> question says *"with 4 KB of memory"*, because the arithmetic **is** the answer.

**Worked use — find duplicates in `[1, 32000]` with 4 KB** (CtCI 10.8): 32,000 bits is
4,000 bytes, so the whole value range fits and one pass is enough.

```python
# python
# CtCI 10.8 - print duplicates in an array of values in [1, 32000], memory ~ 4 KB
# IDEA: the value IS the index — flip its bit; a bit already set means a repeat
# time = O(n), space = 32000 bits = 4 KB
def print_duplicates(nums):
    seen = BitVector(32000)
    for n in nums:
        if seen.get(n - 1):          # values start at 1, bits start at 0
            print(n)
        else:
            seen.set(n - 1)
```

**The LeetCode version of this trick** is *the array is its own bit vector*: LC 41 (First
Missing Positive) and LC 448 mark "value `v` was seen" by negating `nums[v - 1]` in place,
which is a bit vector costing no extra memory at all.

### 1-2) Bucket count, then refine ⭐⭐⭐⭐⭐

When one bit per value still overflows the budget, spend a **first pass counting into
blocks**. A block that received fewer values than it has slots must be missing one — so the
second pass only has to bit-vector that single block.

```python
# python
# CtCI 10.7 - find a missing non-negative int among ~4 billion, memory ~ 10 MB
# IDEA: pass 1 counts values per block of 2^20; a block holding < 2^20 values must miss one
#       pass 2 builds a bit vector for THAT BLOCK ONLY (2^20 bits = 128 KB)
# time = O(n) over 2 passes, space = 2048 counters (8 KB) + 128 KB
BLOCK = 1 << 20                              # 2^20 values per block

def find_missing(read_all, limit=1 << 31):   # read_all() -> a fresh iterator each call
    counts = [0] * (limit // BLOCK)          # 2048 counters
    for v in read_all():                     # ---- pass 1
        counts[v // BLOCK] += 1

    block = next(i for i, c in enumerate(counts) if c < BLOCK)
    lo = block * BLOCK
    seen = bytearray(BLOCK // 8)             # ---- pass 2, 128 KB
    for v in read_all():
        if lo <= v < lo + BLOCK:
            off = v - lo
            seen[off >> 3] |= 1 << (off & 7)

    for off in range(BLOCK):
        if not (seen[off >> 3] >> (off & 7)) & 1:
            return lo + off
    return -1                                # no gap: the input held every value
```

Two things to say out loud:

- **Why a count is enough to pick the block.** The values are distinct, so a block of size
  `2^20` that received fewer than `2^20` of them provably has a hole. No need to know which.
- **How to size the block.** Pass 1 needs `limit / BLOCK` counters, pass 2 needs `BLOCK`
  bits, and both must fit. `BLOCK = 2^20` costs 8 KB + 128 KB, far inside 10 MB.

The shape generalises as *count coarse, then zoom*. It is the on-disk form of a counting
sort on the high bits, and it is how a k-th-largest query over a huge file is answered:
count per bucket, find the bucket that contains rank k, re-scan only that bucket.

### 1-3) Shard by hash — make the pieces independent ⭐⭐⭐⭐

Counting words in a 10 GB file, deduplicating URLs, joining two huge files: the blocker is
that **the same key can appear anywhere**. Remove that by choosing the split rule so it
cannot — send each record to bucket `h(key) % B` and every copy of a key lands in the same
bucket. Each bucket is then an ordinary in-RAM problem and the results concatenate.

```python
# python
# GENERAL PATTERN: shard a too-big file into buckets that each fit in RAM
# IDEA: h(key) % B decides the bucket -> identical keys can never split across buckets
# time = O(n) to shard + O(n) to process, space = O(largest bucket)
import hashlib

def bucket_of(key, n_buckets):
    # NOT the builtin hash(): Python salts str hashing per process, so a re-run
    # would send the same key to a different file
    digest = hashlib.md5(key.encode()).digest()
    return int.from_bytes(digest[:4], "big") % n_buckets

def count_words(lines, n_buckets, tmpdir):
    files = [open("%s/part-%d" % (tmpdir, i), "w") for i in range(n_buckets)]
    for line in lines:                                   # ---- pass 1: scatter
        for word in line.split():
            files[bucket_of(word, n_buckets)].write(word + "\n")
    for f in files:
        f.close()

    for i in range(n_buckets):                           # ---- pass 2: gather
        counts = {}                                      # one bucket fits in RAM
        with open("%s/part-%d" % (tmpdir, i)) as f:
            for word in f:
                word = word.rstrip("\n")
                counts[word] = counts.get(word, 0) + 1
        for word, c in counts.items():
            yield word, c
```

Pick `B` so the **largest** bucket fits, not the average — one skewed key (a URL hit a
billion times) still lands in a single file; if a bucket overflows, shard it again with a
different hash. This is what `GROUP BY` does in every distributed engine, and it is the
honest answer to "how would you scale this": the buckets are independent, so they can move
to separate machines unchanged.

### 1-4) External merge sort ⭐⭐⭐⭐

Sorting 1 TB with 1 GB of RAM: cut the input into chunks that fit, sort each in memory and
write it out as a sorted **run**, then merge the runs with a heap holding one record per run.

```python
# python
# GENERAL PATTERN: external merge sort — sort more data than fits in memory
# IDEA: phase 1 turns the input into sorted runs; phase 2 k-way merges them, keeping
#       only k heads (one per run) in RAM
# time = O(n log n) compares; I/O = O(n) per pass, passes = 1 + log_k(#runs)
import heapq

def external_sort(records, chunk_size, tmpdir):
    runs, chunk = [], []
    for rec in records:                                  # ---- phase 1: sorted runs
        chunk.append(rec)
        if len(chunk) == chunk_size:
            runs.append(_flush(chunk, tmpdir, len(runs)))
            chunk = []
    if chunk:
        runs.append(_flush(chunk, tmpdir, len(runs)))

    files = [open(p) for p in runs]                      # ---- phase 2: k-way merge
    for line in heapq.merge(*files, key=int):            # heap of k heads only
        yield int(line)

def _flush(chunk, tmpdir, i):
    chunk.sort()
    path = "%s/run-%d" % (tmpdir, i)
    with open(path, "w") as f:
        f.writelines("%d\n" % x for x in chunk)
    return path
```

- **Why a heap.** Merging `k` runs by scanning every head is `O(k)` per record; a heap makes
  it `O(log k)`. It is LC 23 (Merge k Sorted Lists) — the only difference is that each list
  lives on disk.
- **Why `k` is bounded.** Every open run needs a read buffer, so `k ~= RAM / buffer size`.
  More runs than that means more than one merge pass: `passes = 1 + ceil(log_k(runs))`.
- **What it buys.** Once the data is sorted, dedup, grouping and set intersection are each a
  single linear scan with two pointers and no extra memory.

## 2) LC Example

These are the in-RAM problems that drill the same reflexes; name the connection in the room.

| # | Problem | The memory idea it drills |
|---|---|---|
| 41 | First Missing Positive | The array is its own bit vector — mark by negating in place |
| 448 | Find All Numbers Disappeared in an Array | Same in-place marking, no extra space |
| 268 | Missing Number | Sum / XOR instead of storing anything |
| 287 | Find the Duplicate Number | Read-only input, O(1) space — Floyd cycle, not a seen-set |
| 23 | Merge k Sorted Lists | The k-way merge phase of an external sort |
| 692 / 703 | Top K Frequent Words / Kth Largest in a Stream | Bounded heap — the per-bucket step after sharding |

### 2-1) The four questions to answer out loud ⭐⭐⭐⭐

1. **What is the value range?** Bounded and dense → bit vector. Unbounded → hash shard.
2. **What is the budget in bits?** Convert it, then say whether the whole range fits.
3. **How many passes may I make?** One → a sketch, and the answer is approximate. Two or
   more → count-then-refine, and the answer stays exact.
4. **Can the input be re-read?** A file can. A true stream cannot, which is exactly what
   pushes the problem into [streaming_algorithms.md](./streaming_algorithms.md).

## 3) Common Pitfalls

- **Quoting memory in items, not bytes.** "I'd use a set of 4 billion ints" is 16 GB. Do the
  multiplication before proposing the structure.
- **A bit vector over a sparse range.** One bit per value only wins when the values are
  dense; for 1,000 values spread over 2^63, a hash set is smaller by orders of magnitude.
- **Sharding with an unstable hash.** Python salts `hash()` on `str` per process, so a re-run
  scatters keys differently. Use `hashlib` (or Java's `String.hashCode`, which is specified
  and stable) whenever the buckets outlive the process.
- **Assuming buckets are balanced.** Size for the worst bucket and re-shard the overflow.
- **Sorting when grouping is enough.** Sorting costs `O(n log n)` I/O passes; sharding by
  hash costs two. Sort only when the output has to be ordered or you need range scans.
- **Forgetting that the second pass re-reads the input.** If the source is a network stream,
  count-then-refine is not available — say so before choosing it.

## 4) Summary

| Technique | Exact? | Memory | Passes | Use when |
|---|---|---|---|---|
| Bit vector | yes | 1 bit / value | 1 | dense bounded range |
| Count, then refine | yes | counters + 1 block | 2 | range too big for one bit each |
| Shard by hash | yes | largest bucket | 2 | keys must be grouped |
| External merge sort | yes | 1 chunk / 1 record per run | 1 + log_k | output must be ordered |
| Sketch (Bloom, count-min) | no | sublinear | 1 | one pass, error acceptable |
