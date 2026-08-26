# Python Insertion, Slicing & Index Arithmetic

> **Scope** — The two things that produce most wrong answers in Python solutions that are otherwise correct: where an insertion actually lands, and whether an index range means a count or a distance.
> **See also**: [python_trick.md](./python_trick.md) — the language idioms these build on; [python_trick_stdlib.md](./python_trick_stdlib.md) — `bisect.insort` for keeping a list sorted while inserting; [prefix_sum.md](./prefix_sum.md) — the technique behind the prefix-sum section here; [array.md](./array.md) — the same operations as array algorithms rather than Python calls.

## LeetCode Problem Lists

- [Array](https://leetcode.com/problem-list/array/)
- [Prefix Sum](https://leetcode.com/problem-list/prefix-sum/)

## Overview

Split out of [python_trick.md](./python_trick.md). Two of its longest sections — insertion into
a list, and index distance vs element count — are about the same thing from opposite ends: what
`i` and `j` mean, and what happens to everything after them.

### Key Properties
- **Core Idea**: `list.insert(i, x)` puts `x` *at* index `i` and shifts the rest right — O(n), not O(1); `x[i:j]` excludes `j`, so a range of `j - i + 1` elements needs `x[i:j+1]`
- **When to Use**: when an answer is off by exactly one, or an element lands one slot from where you expected


## Inserting & Moving Elements

### Insert into a list, in place ⭐⭐⭐⭐⭐

```python

# syntax : 
# arr.insert(<index>,<value>)
In [12]: x = [1,2,3]
    ...: x.insert(2,77)

In [13]: x
Out[13]: [1, 2, 77, 3]
```

#### **Core Idea — Insert into a list, in place**

```text
arr.insert(idx, val)
   -> val is placed AT index `idx`  (i.e. inserted BEFORE the old arr[idx])
   -> everything from old arr[idx] onward SHIFTS RIGHT by 1
   -> mutates IN PLACE and returns None   (NOT a new list!)
   -> time = O(n)  (because of the shifting), space = O(1)
```

```text
x = [1, 2, 3]        x.insert(2, 77)

 idx:  0    1    2                idx:  0    1    2     3
     [ 1 ][ 2 ][ 3 ]     ───►         [ 1 ][ 2 ][ 77 ][ 3 ]
                ^                                ^     ^
           insert HERE                       new val   old x[2] pushed right
```

**Key property (why LC 406 works):** after `insert(k, v)`, the value `v` sits at
**exactly index `k`** — so `insert` is the tool when you must *place an element at a
specific position* rather than just append.

#### **Edge cases / behaviors**

```python
#----------------------------
# 1) index == len(arr)  -> same as append
#----------------------------
In [1]: x = [1,2,3]; x.insert(3, 99); x
Out[1]: [1, 2, 3, 99]

#----------------------------
# 2) index > len(arr)   -> NO IndexError, clamped to the end (append)
#----------------------------
In [2]: x = [1,2,3]; x.insert(100, 99); x
Out[2]: [1, 2, 3, 99]

#----------------------------
# 3) index == 0         -> insert at FRONT (see 1-6')
#----------------------------
In [3]: x = [1,2,3]; x.insert(0, 0); x
Out[3]: [0, 1, 2, 3]

#----------------------------
# 4) NEGATIVE index     -> counts from the END, inserts BEFORE that element
#----------------------------
In [4]: x = [1,2,3]; x.insert(-1, 99); x
Out[4]: [1, 2, 99, 3]        # before the LAST element, NOT at the end

In [5]: x = [1,2,3]; x.insert(-100, 99); x
Out[5]: [99, 1, 2, 3]        # clamped to the front

#----------------------------
# 5) returns None (IN-PLACE!) -> classic bug
#----------------------------
In [6]: x = [1,2,3]
In [7]: y = x.insert(1, 9)   # ❌ y is None
In [8]: print(y, x)
None [1, 9, 2, 3]
```

**❌ Common bugs**

```python
arr = arr.insert(0, x)        # ❌ arr becomes None  (insert returns None)
arr.insert(0, x)              # ✅ just call it

# ❌ mutating the list you are iterating -> infinite loop / skipped items
for v in arr:
    arr.insert(0, v)          # ❌ never do this
res = []                      # ✅ build a NEW list instead
for v in arr:
    res.insert(pos, v)
```

#### **`insert` vs `append` vs `extend` vs `+`**

| Op | Effect | Time | Returns |
|----|--------|------|---------|
| `arr.append(v)` | add ONE item at the end | `O(1)` amortized | `None` (in place) |
| `arr.insert(i, v)` | add ONE item at index `i`, shift right | `O(n)` | `None` (in place) |
| `arr.insert(0, v)` | add at FRONT (worst case shift) | `O(n)` | `None` (in place) |
| `arr.extend([a,b])` | add MANY items at the end | `O(k)` | `None` (in place) |
| `arr = arr + [v]` | build a NEW list | `O(n)` | new list |
| `arr[i:i] = [a,b]` | slice-insert MANY items at index `i` | `O(n+k)` | `None` (in place) |
| `deque.appendleft(v)` | add at FRONT | **`O(1)`** | `None` (in place) |
| `bisect.insort(arr, v)` | insert keeping array SORTED | `O(n)` (search `O(log n)`) | `None` (in place) |

```python
# bulk insert via slice assignment (insert MANY at once)
In [9]: x = [1, 2, 5]
In [10]: x[2:2] = [3, 4]      # insert [3,4] AT index 2, nothing removed
In [11]: x
Out[11]: [1, 2, 3, 4, 5]
```

> **Performance note**: `insert` shifts every element after `idx`, so it is `O(n)`.
> Calling it inside a loop → `O(n²)`. That is acceptable for LC constraints like
> `n <= 2000` (LC 406), but if you only ever insert at the FRONT, use
> `collections.deque.appendleft()` (`O(1)`) — see [1-32) deque](./python_trick_stdlib.md#deque-double-ended-queue).

#### **Use case 1 — LC 406 Queue Reconstruction by Height ⭐⭐⭐⭐⭐**

`people[i] = [h, k]` = height `h`, with exactly `k` people **taller or equal** in front.

**Key insight**: sort by height **DESC**, then by `k` **ASC**; now insert each person at
index `k`. Since everyone already placed is **taller or equal**, "index `k`" literally
means "`k` taller-or-equal people in front" — and inserting a **shorter** person later
never breaks an earlier person's count (shorter people don't count toward `k`).

```python
# LC 406 Queue Reconstruction by Height
# time = O(n^2)   (n inserts × O(n) shift)
# space = O(n)
class Solution(object):
    def reconstructQueue(self, people):
        # sort: height DESC (-x[0]), then k ASC (x[1])
        people.sort(key=lambda x: (-x[0], x[1]))

        # py insert syntax:
        # python_trick.html#1-6-insert-into-array-in-place
        # arr.insert(<index>, <value>)
        res = []
        for p in people:
            res.insert(p[1], p)   # place person AT index k
        return res
```

**Visual trace** — `people = [[7,0],[4,4],[7,1],[5,0],[6,1],[5,2]]`

```text
after sort (h DESC, k ASC):
  [[7,0], [7,1], [6,1], [5,0], [5,2], [4,4]]

step | person | insert(k, p)  | res
-----+--------+---------------+------------------------------------------
  1  | [7,0]  | insert(0, ..) | [[7,0]]
  2  | [7,1]  | insert(1, ..) | [[7,0], [7,1]]
  3  | [6,1]  | insert(1, ..) | [[7,0], [6,1], [7,1]]
  4  | [5,0]  | insert(0, ..) | [[5,0], [7,0], [6,1], [7,1]]
  5  | [5,2]  | insert(2, ..) | [[5,0], [7,0], [5,2], [6,1], [7,1]]
  6  | [4,4]  | insert(4, ..) | [[5,0], [7,0], [5,2], [6,1], [4,4], [7,1]]
                                                              ^ landed at idx 4
```

**Why the two sort keys matter**

```python
people.sort(key=lambda x: (-x[0], x[1]))
#                          ^^^^^  ^^^^
#  -x[0] : TALLEST first  -> everyone already in `res` is >= current height,
#                            so "index k" == "k taller-or-equal in front"
#   x[1] : k ASC on ties  -> among SAME height, smaller k inserted first,
#                            otherwise [7,1] before [7,0] would misplace [7,0]
```

> Related: the sort key itself is section
> [1-11'') Multi-key tuple sort](./python_trick.md#multi-key-tuple-sort-keylambda-x-x0-x1).

#### **Use case 2 — insert while keeping the array SORTED (`bisect.insort`)**

Don't hand-roll "find the position, then insert" — `bisect` does the search for you.

```python
import bisect

# manual (2 steps)
idx = bisect.bisect_left(a, val)
a.insert(idx, val)

# one-liner (identical result)
bisect.insort_left(a, val)
```

```python
# LC 315 Count of Smaller Numbers After Self — scan right → left,
# keep a sorted list of seen values; the insert position IS the answer
def countSmaller(nums):
    seen, res = [], []
    for n in reversed(nums):
        idx = bisect.bisect_left(seen, n)   # how many seen values are < n
        res.append(idx)
        seen.insert(idx, n)                 # keep `seen` sorted
    return res[::-1]
```

> See [1-27) bisect](./python_trick_stdlib.md#bisect_left-and-bisect_right).

#### **Use case 3 — insert at the FRONT (build result in reverse)**

Common when you walk a path/linked-list backwards but want forward output.

```python
# BFS/DFS: walk parent pointers backwards, insert(0, ..) to get the path in order
path = []
while node:
    path.insert(0, node.val)   # O(n) each -> O(n^2) total
    node = parent[node]

# ✅ FASTER equivalents
path.append(node.val); ...; path = path[::-1]      # append then reverse — O(n)
from collections import deque
path = deque(); path.appendleft(node.val)          # O(1) per push
```

#### **Use case 4 — LC 57 Insert Interval (insert into a sorted-by-start list)**

```python
# find where the new interval starts, then insert & merge
import bisect

def insert(intervals, newInterval):
    idx = bisect.bisect_left(intervals, newInterval)
    intervals.insert(idx, newInterval)      # now list is still sorted by start
    # ... then do the standard merge pass
    res = []
    for it in intervals:
        if res and res[-1][1] >= it[0]:
            res[-1][1] = max(res[-1][1], it[1])
        else:
            res.append(it)
    return res
```

#### **Similar LC problems — Insert into a list, in place**

| LC # | Problem | How `insert` is used |
|------|---------|----------------------|
| 406 | Queue Reconstruction by Height | `res.insert(k, person)` after height-DESC sort ⭐ |
| 57 | Insert Interval | insert at sorted position, then merge |
| 315 | Count of Smaller Numbers After Self | `bisect` position + `insert` to keep sorted |
| 220 | Contains Duplicate III | sorted window (`SortedList.add` = insert) |
| 148 | Sort List | insertion-sort variant on a list |
| 147 | Insertion Sort List | linked-list version of the same idea |
| 146 | LRU Cache | `remove` + `append` to move to the end (see [1-21](#moving-an-element-to-the-rightmost--leftmost-position)) |
| 155 | Min Stack | `append` / `pop` at the end only — `O(1)`, no insert needed |
| 622 | Design Circular Queue | why you avoid `insert(0, ..)` → use `deque` |

### Adding to the front of a list, in place

```python
In [1]: x = [1,2,3]

In [2]: x
Out[2]: [1, 2, 3]

In [3]: x.insert(0,0)

In [4]: x
Out[4]: [0, 1, 2, 3]

In [5]: x.insert(0,-1)

In [6]: x
Out[6]: [-1, 0, 1, 2, 3]
```

### Moving an element to the rightmost / leftmost position

```python
# LC 146 LRU Cache
In [18]: x
Out[18]: [1, 3, 2]

In [19]: x = [1,2,3]

# NOTE this !!!!
# LC 146
In [20]: x.remove(2)
#x
#[1,2]

In [21]: x.append(2)

In [22]: x
Out[22]: [1, 3, 2]

In [23]:

In [23]: x.remove(1)

In [24]: x.append(1)

In [25]: x
Out[25]: [3, 2, 1]
```

### List `extend`

```python
# LC 969. Pancake Sorting

In [10]: x = [1,2,3]

In [11]: x.extend([4])

In [12]: x
Out[12]: [1, 2, 3, 4]

In [13]: x = [1,2,3]

In [14]: x = x + [4]

In [15]: x
Out[15]: [1, 2, 3, 4]
```


## Slicing

### Array slicing (subarray / substring)


**Syntax**: `arr[start:end]` — **end is exclusive**, so the slice covers indices `[start, end-1]`.

```python
arr = [0, 1, 2, 3, 4]

arr[1:4]     # [1, 2, 3]  → indices 1, 2, 3  (end=4 is excluded)
arr[i:j+1]   # indices i .. j  (inclusive on both ends)
arr[:3]      # [0, 1, 2]  → from start up to index 2
arr[2:]      # [2, 3, 4]  → index 2 to end
arr[:]       # full copy
arr[::-1]    # reversed copy

# Common pattern: get subarray from index i to j (inclusive)
sub = arr[i : j + 1]

# String slicing works the same way
s = "abcde"
s[1:4]       # "bcd"  → indices 1, 2, 3
s[i:j+1]     # chars from i to j (inclusive)
```

| Expression | Meaning |
|-----------|---------|
| `arr[i:j+1]` | indices `i` to `j` inclusive |
| `arr[:j+1]` | indices `0` to `j` inclusive |
| `arr[i:]` | indices `i` to end |
| `arr[:]` | full shallow copy |
| `arr[::-1]` | reversed |

#### `x[i:j+1]` vs `x[i:j]` — include or exclude index `j`?

```python
x = [1, 3, 2]
#    0  1  2   ← indices

x[0:2]   # [1, 3]  → j=2 is NOT included  (indices 0, 1)
x[0:3]   # [1, 3, 2] → j=3 is NOT included, but covers all (indices 0, 1, 2)

# To include index j, use j+1 as the stop:
x[0:1+1]  # [1, 3]  → includes index j=1
x[0:2+1]  # [1, 3, 2] → includes index j=2
```

**Rule**:
```text
x[i:j]   → j index is NOT included  (standard Python — end is exclusive)
x[i:j+1] → j index IS included      (add +1 to make end inclusive)
```

#### Concrete example — LC 105 (Construct Binary Tree from Preorder + Inorder)

```python
# preorder = [3, 9, 20, 15, 7]
# inorder  = [9, 3, 15, 20,  7]
#
# root = preorder[0] = 3
# idx  = inorder.index(3) = 1   ← root sits at index 1 in inorder
#
# inorder layout:
#   index:   0   1   2   3   4
#   value:  [9,  3, 15, 20,  7]
#             ^   ^
#           left root  right subtree starts at idx+1=2
#
# Left subtree of inorder  = elements BEFORE root  = inorder[:idx]
# Right subtree of inorder = elements AFTER  root  = inorder[idx+1:]

# ✅ CORRECT: inorder[:idx]   → [9]           (excludes root at idx=1)
# ❌ WRONG:   inorder[:idx+1] → [9, 3]        (includes root — builds wrong tree)

root.left = self.buildTree(
    preorder[1 : 1 + idx],   # left subtree has `idx` nodes
    inorder[:idx]             # everything LEFT of root (exclusive stop = idx)
)
root.right = self.buildTree(
    preorder[1 + idx:],       # remaining nodes after left subtree
    inorder[idx + 1:]         # everything RIGHT of root (skip root at idx)
)

# Why inorder[:idx] and NOT inorder[:idx+1]?
#   Python slice stop is EXCLUSIVE, so inorder[:idx] gives indices 0..idx-1,
#   which is exactly the elements to the LEFT of root (root at idx is excluded).
#   Using inorder[:idx+1] would mistakenly include the root itself in the left subtree.
```

### Enumerating ALL substrings — why the inner `j` loop needs `+1` ⭐⭐⭐⭐⭐


```python
# LC 647 Palindromic Substrings (brute force)
count = 0
# NOTE: since i from 0 to len(s) - 1,
#  -> so for j we need to "+1" then can go through all elements in str
for i in range(len(s)):
    # Note : for j we need to "+1"
    for j in range(i+1, len(s)+1):
        if s[i:j] == s[i:j][::-1]:
            count += 1
```

#### **Core Idea — Enumerating ALL substrings**

**`j` is NOT an index here — it is a slice BOUNDARY (a "cut position").**

- An **index** points AT a character → valid range `0 … n-1`  (`n` values)
- A **boundary** points BETWEEN characters → valid range `0 … n`  (`n+1` values)

`s[i:j]` is defined by two *boundaries*, so `j` must be able to reach `n`
(the cut AFTER the last char). That is exactly why the loop is
`range(i+1, len(s)+1)` and not `range(i+1, len(s))`.

```text
s = "abc"

index:        0     1     2
           +--a--+--b--+--c--+
boundary:  0     1     2     3        ← j lives HERE (0 .. n, so n+1 = 4 slots)

s[0:1] = "a"      s[0:3] = "abc"   ← needs j = 3 = len(s)  → stop must be len(s)+1
s[1:3] = "bc"     s[3:3] = ""
```

#### **Explanation — two equivalent forms**

```python
n = len(s)

# ── Form A: j as BOUNDARY (slice end, exclusive) ──
for i in range(n):
    for j in range(i+1, n+1):     # +1 on BOTH start and stop
        sub = s[i:j]              # substring s[i .. j-1], length = j - i

# ── Form B: j as INDEX (last char of the substring) ──
for i in range(n):
    for j in range(i, n):         # no +1 anywhere in range()
        sub = s[i:j+1]            # +1 moves INTO the slice, length = j - i + 1
```

| Form | `j` means | loop | slice | substring length |
|------|-----------|------|-------|------------------|
| **A** | boundary / cut | `range(i+1, n+1)` | `s[i:j]` | `j - i` |
| **B** | last char index | `range(i, n)` | `s[i:j+1]` | `j - i + 1` |

> **Rule**: the `+1` appears **exactly once** — either in `range()` (Form A)
> or in the slice (Form B). Putting it in **both** or **neither** is the bug.

**The 3 classic mistakes**

```python
n = len(s)

# ❌ 1) forgot +1 on stop → MISSES every substring ending at the LAST char
for j in range(i+1, n):
    s[i:j]          # for s="abc", i=0 -> only "a","ab"   ("abc" never checked!)

# ❌ 2) forgot +1 on start → produces the EMPTY string s[i:i] = ""
for j in range(i, n+1):
    s[i:j]          # j == i gives "", and "" == ""[::-1] is True → OVER-counts

# ❌ 3) mixed the two forms → out of range / duplicated work
for j in range(i+1, n+1):
    s[i:j+1]        # j+1 can reach n+1 → silently returns the same string again
```

**Why total count is `n*(n+1)/2`** — a quick sanity check for your loop:

```python
s = "abc"                      # n = 3  ->  3*4/2 = 6 substrings
# i=0: "a", "ab", "abc"        (j = 1,2,3)
# i=1: "b", "bc"               (j = 2,3)
# i=2: "c"                     (j = 3)

n = len(s)
print(sum(1 for i in range(n) for j in range(i+1, n+1)))   # 6  ✅
```

**Same rule for SUBARRAYS** (identical logic, list instead of string):

```python
# all contiguous subarrays of nums
for i in range(len(nums)):
    for j in range(i+1, len(nums)+1):
        sub = nums[i:j]        # e.g. sum(sub), max(sub), ...
```

> **Related**: this is the same exclusive-stop rule as [1-51) Array slicing](#array-slicing-subarray--substring)
> (`x[i:j]` excludes `j`) and [1-52) Index distance vs element count](#index-distance-vs-element-count-off-by-one).
> Careful: **DP** on substrings usually uses `dp[i][j]` with `j` as an **INDEX**
> (Form B, `s[i:j+1]`) — don't mix the convention inside one solution.

#### **Similar LC problems — Enumerating ALL substrings**

| LC # | Problem | How `j` is used |
|------|---------|-----------------|
| 647 | Palindromic Substrings | boundary `s[i:j]` (brute force) / index `dp[i][j]` (DP) |
| 5 | Longest Palindromic Substring | boundary — track best `s[i:j]` by length |
| 3 | Longest Substring Without Repeating Chars | sliding window: `right` behaves like a boundary |
| 76 | Minimum Window Substring | window `s[left:right+1]` → index form |
| 131 | Palindrome Partitioning | `for j in range(i+1, n+1): s[i:j]` then backtrack from `j` |
| 139 | Word Break | `for j in range(i+1, n+1): s[i:j] in wordDict` |
| 560 | Subarray Sum Equals K | subarray `nums[i:j]`, boundary form (prefix-sum uses same cuts) |
| 53 | Maximum Subarray | subarray enumeration (brute force) / Kadane |
| 209 | Minimum Size Subarray Sum | window length = `right - left + 1` → index form |
| 516 | Longest Palindromic Subsequence | DP `dp[i][j]`, `j` as INDEX (Form B) |
| 1143 | Longest Common Subsequence | DP `dp[i][j]`, `i`/`j` as **lengths** (0 … n) — boundary-like |

## Index Arithmetic

### Index distance vs element count (off-by-one)


**Core rule:** distance between two indices ≠ number of elements between them.

```python
a = [1, 2, 3]
#    0  1  2     ← indices

# distance (span between indices, e.g. window width in pixels)
# last_idx - first_idx  =  2 - 0  =  2

# element count (how many items are IN the range [first_idx, last_idx] inclusive)
# last_idx - first_idx + 1  =  2 - 0 + 1  =  3
```

| Expression | Value | Meaning |
|-----------|-------|---------|
| `last - first` | `2` | distance / span (fence gaps) |
| `last - first + 1` | `3` | number of elements (fence posts) |

**Visualisation — the "fence post" analogy:**
```text
index:   0    1    2
         |    |    |       ← 3 posts  (= last - first + 1 = 3)
         +----+----+       ← 2 gaps   (= last - first     = 2)
```

**Common LC applications:**

```python
# 1. Sliding window length
#    window covers indices [l, r] inclusive
window_len = r - l + 1      # NOT r - l

# 2. Substring / subarray length
s = "abcde"
# substring s[i:j] in Python has j - i characters (Python end is exclusive)
# substring from index i to j INCLUSIVE has j - i + 1 characters
length = j - i + 1

# 3. Array midpoint (binary search)
mid = (lo + hi) // 2        # mid is an index, not a count

# 4. Difference array / prefix sum length
#    to cover indices 0..n-1, need n+1 slots in prefix sum array
prefix = [0] * (n + 1)

# 5. Range check: does [l, r] contain at least k elements?
if r - l + 1 >= k:          # NOT r - l >= k
    ...
```

**Quick rule of thumb:**
```text
result = right - left       → use when you need a GAP / DISTANCE
result = right - left + 1  → use when you need an ELEMENT COUNT
```
```python
#-------------------------------
# Sliding window template
#-------------------------------
def sliding_window(s, k):
    left = 0
    window = {}
    result = 0
    for right, ch in enumerate(s):
        window[ch] = window.get(ch, 0) + 1
        while len(window) > k:       # shrink condition
            lch = s[left]
            window[lch] -= 1
            if window[lch] == 0:
                del window[lch]
            left += 1
        result = max(result, right - left + 1)
    return result

#-------------------------------
# Binary search template
#-------------------------------
def binary_search(nums, target):
    lo, hi = 0, len(nums) - 1
    while lo <= hi:
        mid = (lo + hi) // 2
        if nums[mid] == target:
            return mid
        elif nums[mid] < target:
            lo = mid + 1
        else:
            hi = mid - 1
    return -1

# Binary search on answer (find leftmost valid value)
def binary_search_left(lo, hi, feasible):
    while lo < hi:
        mid = (lo + hi) // 2
        if feasible(mid):
            hi = mid
        else:
            lo = mid + 1
    return lo

#-------------------------------
# DFS template (iterative)
#-------------------------------
def dfs_iterative(graph, start):
    visited = set()
    stack = [start]
    while stack:
        node = stack.pop()
        if node in visited:
            continue
        visited.add(node)
        for neighbor in graph[node]:
            stack.append(neighbor)

#-------------------------------
# Backtracking template
#-------------------------------
def backtrack(result, current, choices):
    if is_complete(current):
        result.append(current[:])
        return
    for choice in choices:
        current.append(choice)
        backtrack(result, current, next_choices(choice))
        current.pop()

#-------------------------------
# Union-Find (Disjoint Set Union)
#-------------------------------
class UnionFind:
    def __init__(self, n):
        self.parent = list(range(n))
        self.rank = [0] * n

    def find(self, x):
        if self.parent[x] != x:
            self.parent[x] = self.find(self.parent[x])  # path compression
        return self.parent[x]

    def union(self, x, y):
        px, py = self.find(x), self.find(y)
        if px == py:
            return False
        if self.rank[px] < self.rank[py]:
            px, py = py, px
        self.parent[py] = px
        if self.rank[px] == self.rank[py]:
            self.rank[px] += 1
        return True

#-------------------------------
# Trie (Prefix Tree)
#-------------------------------
class TrieNode:
    def __init__(self):
        self.children = {}
        self.is_end = False

class Trie:
    def __init__(self):
        self.root = TrieNode()

    def insert(self, word):
        node = self.root
        for ch in word:
            node = node.children.setdefault(ch, TrieNode())
        node.is_end = True

    def search(self, word):
        node = self.root
        for ch in word:
            if ch not in node.children:
                return False
            node = node.children[ch]
        return node.is_end

    def startsWith(self, prefix):
        node = self.root
        for ch in prefix:
            if ch not in node.children:
                return False
            node = node.children[ch]
        return True
```

### Building a prefix-sum array


The cumulative-sum idiom: precompute running totals so any range sum becomes O(1).
See [`prefix_sum.md`](./prefix_sum.md) for the full cheatsheet.

```python
cnt = [1, 0, 1, 1, 1]

# Step 1: allocate size n+1, fill with 0
#   prefix[0] = 0 is the "empty sum" sentinel
#   -> makes sum starting at index 0 work without a special case
prefix = [0] * (len(cnt) + 1)
# prefix = [0, 0, 0, 0, 0, 0]

# Step 2 (CORE) : prefix[i+1] = running total up to (and including) cnt[i]
for i in range(len(cnt)):
    prefix[i + 1] = prefix[i] + cnt[i]

# prefix = [0, 1, 1, 2, 3, 4]
```

**The one line to memorize:**
```python
for i in range(len(cnt)):
    prefix[i + 1] = prefix[i] + cnt[i]
```

**Trace (note the result is ONE element longer than `cnt`):**
```text
cnt:        [ 1,  0,  1,  1,  1 ]
index i:      0   1   2   3   4

prefix[0] = 0                            ← sentinel (empty prefix)
prefix[1] = prefix[0] + cnt[0] = 0 + 1 = 1
prefix[2] = prefix[1] + cnt[1] = 1 + 0 = 1
prefix[3] = prefix[2] + cnt[2] = 1 + 1 = 2
prefix[4] = prefix[3] + cnt[3] = 2 + 1 = 3
prefix[5] = prefix[4] + cnt[4] = 3 + 1 = 4

prefix = [0, 1, 1, 2, 3, 4]
          ↑                 ↑
       empty sum        sum of ALL cnt
```

**Why index `i + 1` (not `i`)?** `prefix` has size `n+1` and `prefix[k]` = "sum of
the first `k` elements". So writing into `prefix[i+1]` keeps the leading `prefix[0]=0`
intact — which lets you query `sum(l, r) = prefix[r+1] - prefix[l]` with no edge case.

**One-liner alternative** — `itertools.accumulate` with `initial=0`:
```python
from itertools import accumulate
prefix = list(accumulate(cnt, initial=0))   # [0, 1, 1, 2, 3, 4]

# without initial=0 -> same length as cnt, no leading sentinel
list(accumulate(cnt))                        # [1, 1, 2, 3, 4]
```

**Range sum query (O(1) after the O(n) build):**
```python
# sum of cnt[l .. r] inclusive
def range_sum(l, r):
    return prefix[r + 1] - prefix[l]

range_sum(1, 3)   # cnt[1]+cnt[2]+cnt[3] = 0+1+1 = 2  -> prefix[4]-prefix[1] = 3-1 = 2
```
