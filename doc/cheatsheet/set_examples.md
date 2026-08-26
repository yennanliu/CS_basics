# Set — Worked Examples

> **Scope** — The worked-solution archive behind [set.md](./set.md): fourteen problems grouped by what the set is being used for — a memory of what has been seen, set algebra, an O(1) index that replaces a scan, or a component inside a larger algorithm.
> **See also**: [set.md](./set.md) — the parent sheet: the types, the basic operations, the decision framework and the Python-vs-Java notes; [hash_map.md](./hash_map.md) — when you need the value as well as the key; [hashing.md](./hashing.md) — designing the key itself, which is what LC 694 and LC 939 turn on; [bfs.md](./bfs.md) — the frontier sets in LC 127; [design.md](./design.md) — LC 380 from the design side.

## LeetCode Problem Lists

- [Hash Table](https://leetcode.com/problem-list/hash-table/)

## Overview

This is the long tail of [set.md](./set.md), which was 79% example tail. The parent keeps the
operations, the implementation comparison and the decision framework; this file keeps the
problems that *apply* them.

### Key Properties
- **Complexity**: O(1) average per membership test, O(log n) for the ordered set in LC 975 — the whole point is replacing an O(n) scan
- **Core Idea**: a set is almost never used to *store* things. It is used to answer "have I seen this", to encode a shape as a key, or to be the frontier of a search — which is how these are grouped
- **When to Use**: when you need membership and nothing else; the moment you need an associated value, it is a [hash map](./hash_map.md)


## "Have I Seen This Before?"

### 1) Contains Duplicate — LC 217 ⭐⭐⭐⭐⭐

```python
# LC 217. Contains Duplicate
# V0
# IDEA: Set to detect duplicates
class Solution:
    def containsDuplicate(self, nums):
        return len(nums) != len(set(nums))

# V0'
# IDEA: Build set while checking
class Solution:
    def containsDuplicate(self, nums):
        seen = set()
        for num in nums:
            if num in seen:
                return True
            seen.add(num)
        return False
```

```java
// Java
// LC 217
public boolean containsDuplicate(int[] nums) {
    Set<Integer> seen = new HashSet<>();
    for (int num : nums) {
        if (seen.contains(num)) {
            return true;
        }
        seen.add(num);
    }
    return false;
}
```

#### Variation A: two sets to dedup the *output* — LC 187

**Twist**: one `seen` set is not enough when the answer is "everything that repeats" — an item seen 3x would be reported twice. A **second `repeated` set** absorbs the duplicates for free.

```python
# python
# LC 187 - Repeated DNA Sequences
# IDEA: seen-set for detection + 2nd set so the output is auto-deduped
# time = O(n * L) (L = 10, substring cost), space = O(n * L)
class Solution(object):
    def findRepeatedDnaSequences(self, s):
        seen, repeated = set(), set()
        for i in range(len(s) - 9):
            sub = s[i:i + 10]
            if sub in seen:
                repeated.add(sub)   # 2nd set keeps the output deduped
            else:
                seen.add(sub)
        return list(repeated)
```

```java
// java
// LC 187 - Repeated DNA Sequences
// time = O(n * L), space = O(n * L)
public List<String> findRepeatedDnaSequences(String s) {
    Set<String> seen = new HashSet<>(), repeated = new HashSet<>();
    for (int i = 0; i + 10 <= s.length(); i++) {
        String sub = s.substring(i, i + 10);
        // add() returns false when the element was already present -> one lookup, not two
        if (!seen.add(sub)) repeated.add(sub);
    }
    return new ArrayList<>(repeated);
}
```

> **Idiom**: Java's `set.add(x)` returns `false` if `x` was already there, and `set.remove(x)` returns `true` if it was. Use the return value instead of a separate `contains()` call.

#### Variation B: probe a *transformed* key, not the element itself — LC 532

**Twist**: instead of asking "have I seen `num`?", ask "is `num + k` in the set?". Dedup by iterating the **set** (not the array), so each distinct pair is counted once. `k == 0` is a different question (needs counts) → fall back to a frequency map.

```python
# python
# LC 532 - K-diff Pairs in an Array
# IDEA: pairs = distinct x where x+k also exists; k==0 needs counts, not a set
# time = O(n), space = O(n)
class Solution(object):
    def findPairs(self, nums, k):
        if k > 0:
            pool = set(nums)
            return sum(1 for x in pool if x + k in pool)
        from collections import Counter
        return sum(1 for x, c in Counter(nums).items() if c > 1)
```

```java
// java
// LC 532 - K-diff Pairs in an Array
// time = O(n), space = O(n)
public int findPairs(int[] nums, int k) {
    if (k > 0) {
        Set<Integer> pool = new HashSet<>();
        for (int n : nums) pool.add(n);
        int cnt = 0;
        for (int x : pool) if (pool.contains(x + k)) cnt++;  // probe x+k, not x
        return cnt;
    }
    Map<Integer, Integer> freq = new HashMap<>();
    for (int n : nums) freq.merge(n, 1, Integer::sum);
    int cnt = 0;
    for (int c : freq.values()) if (c > 1) cnt++;
    return cnt;
}
```

### 2) Single Number — LC 136 — and why a set is the wrong tool here

```python
# LC 136. Single Number
# V0
# IDEA: XOR all numbers (duplicates cancel out)
class Solution:
    def singleNumber(self, nums):
        result = 0
        for num in nums:
            result ^= num
        return result

# V0'
# IDEA: Set addition/removal
class Solution:
    def singleNumber(self, nums):
        return 2 * sum(set(nums)) - sum(nums)
```

### 3) Happy Number — LC 202 — a set as a cycle detector

```python
# LC 202. Happy Number
# V0
# IDEA: Use set to detect cycles
class Solution:
    def isHappy(self, n):
        def get_next(num):
            total = 0
            while num > 0:
                digit = num % 10
                total += digit * digit
                num //= 10
            return total

        seen = set()
        while n != 1 and n not in seen:
            seen.add(n)
            n = get_next(n)

        return n == 1
```

### 4) Linked List Cycle Detection — LC 141 — the same idea on a list

```python
# LC 141. Linked List Cycle
# V0
# IDEA: Use set to track visited nodes
class Solution:
    def hasCycle(self, head):
        visited = set()
        current = head

        while current:
            if current in visited:
                return True
            visited.add(current)
            current = current.next

        return False

# V0'
# IDEA: Two pointers (Floyd's algorithm) - O(1) space
class Solution:
    def hasCycle(self, head):
        if not head:
            return False

        slow = head
        fast = head.next

        while slow != fast:
            if not fast or not fast.next:
                return False
            slow = slow.next
            fast = fast.next.next

        return True
```

## Set Algebra

### 5) Intersection of Two Arrays — LC 349

```python
# LC 349. Intersection of Two Arrays
# V0
# IDEA: Set intersection
class Solution:
    def intersection(self, nums1, nums2):
        return list(set(nums1) & set(nums2))

# V0'
# IDEA: Convert to sets and use intersection
class Solution:
    def intersection(self, nums1, nums2):
        set1 = set(nums1)
        set2 = set(nums2)
        return list(set1.intersection(set2))
```

```java
// Java
// LC 349
public int[] intersection(int[] nums1, int[] nums2) {
    Set<Integer> set1 = new HashSet<>();
    for (int num : nums1) {
        set1.add(num);
    }

    Set<Integer> result = new HashSet<>();
    for (int num : nums2) {
        if (set1.contains(num)) {
            result.add(num);
        }
    }

    return result.stream().mapToInt(i -> i).toArray();
}
```

### 6) Word Pattern — LC 290 — a bijection needs two directions

```python
# LC 290. Word Pattern
# V0
# IDEA: Use 2 sets to track bijection
class Solution:
    def wordPattern(self, pattern, s):
        words = s.split()

        if len(pattern) != len(words):
            return False

        char_to_word = {}
        word_to_char = {}

        for c, word in zip(pattern, words):
            if c in char_to_word:
                if char_to_word[c] != word:
                    return False
            else:
                char_to_word[c] = word

            if word in word_to_char:
                if word_to_char[word] != c:
                    return False
            else:
                word_to_char[word] = c

        return True
```

## The Set as an Index

### 7) Longest Consecutive Sequence — LC 128 — only start from a run's head ⭐⭐⭐⭐⭐


#### Core Idea

**Set + "sequence start" gate — O(n) time**

The key observation: a number `num` is the **start of a sequence** only if `num - 1` is NOT in the set. This gate prevents re-counting the same sequence from every element inside it.

```text
Without the gate: starting from 2 in [1,2,3,4] would count [2,3,4] (length 3),
                  double-counting work already done from 1.
With the gate:    only 1 passes (1-1=0 not in set), so we count exactly once.
```

Once a sequence start is found, extend it by checking `num + length` in the set — each step is O(1). Every element is visited at most twice across all sequences → **total O(n)**.

```text
Pointer role:
  num    — sequence start (anchor): only enters if num-1 ∉ set
  length — implicit "right pointer": increments while num+length ∈ set
```

```python
# python
# LC 128. Longest Consecutive Sequence
# Time: O(n), Space: O(n)
class Solution(object):
    def longestConsecutive(self, nums):
        num_set = set(nums)
        longest = 0

        for num in num_set:
            # Gate: only start counting from the sequence's first element
            if num - 1 not in num_set:
                length = 1

                # Extend right as long as the next consecutive number exists
                while num + length in num_set:
                    length += 1

                longest = max(longest, length)

        return longest
```

**Dry run — `nums = [100, 4, 200, 1, 3, 2]`:**
```text
num_set = {100, 4, 200, 1, 3, 2}

num=100: 99 ∉ set → start, extend: 101 ∉ set → length=1
num=4:    3 ∈ set → SKIP (not a start)
num=200: 199 ∉ set → start, extend: 201 ∉ set → length=1
num=1:    0 ∉ set → start, extend: 2∈,3∈,4∈,5∉ → length=4  ← winner
num=3:    2 ∈ set → SKIP
num=2:    1 ∈ set → SKIP

return 4
```

```java
// java
// LC 128 - Longest Consecutive Sequence
// time: O(n), space: O(n)
public int longestConsecutive(int[] nums) {
    Set<Integer> numSet = new HashSet<>();
    for (int num : nums) numSet.add(num);

    int longest = 0;

    for (int num : numSet) {
        // Gate: only process sequence starts
        if (!numSet.contains(num - 1)) {
            int length = 1;

            while (numSet.contains(num + length)) {
                length++;
            }

            longest = Math.max(longest, length);
        }
    }

    return longest;
}
```

#### Why O(n) and not O(n²)?

The inner `while` loop looks like it could be O(n) per outer iteration, but the **gate** ensures each number is the start of at most one sequence. Across all starts, the total steps in all inner loops equals exactly `len(nums)`. So the amortized cost is O(1) per element → **O(n) total**.

#### Similar Problems

| Problem | LC# | Difference | Key Trick |
|---------|-----|------------|-----------|
| Longest Consecutive Sequence | 128 | Unsorted array | Set + sequence-start gate |
| Arithmetic Slices | 413 | Sorted, fixed diff=1 | DP / sliding window |
| Missing Ranges | 163 | Find gaps in range | Iterate expected vs actual |
| Find All Numbers Disappeared | 448 | 1..n range, find missing | In-place marking or set |
| Longest Arithmetic Subsequence | 1027 | Any common diff, not just 1 | DP + hashmap |
| Contains Duplicate | 217 | Just detect any duplicate | Set size check |

### 8) Valid Sudoku — LC 36 — nine row, column and box sets

```python
# LC 36. Valid Sudoku
# V0
# IDEA: Use sets to track seen values
class Solution:
    def isValidSudoku(self, board):
        # Track seen elements in rows, cols, boxes
        seen = set()

        for i in range(9):
            for j in range(9):
                if board[i][j] != '.':
                    val = board[i][j]
                    box_idx = (i // 3) * 3 + j // 3

                    # Create unique keys for row, col, box
                    row_key = f"row_{i}_{val}"
                    col_key = f"col_{j}_{val}"
                    box_key = f"box_{box_idx}_{val}"

                    if row_key in seen or col_key in seen or box_key in seen:
                        return False

                    seen.add(row_key)
                    seen.add(col_key)
                    seen.add(box_key)

        return True
```

#### Variation: the same 3 sets, but **add/remove on backtrack** — LC 37

**Twist**: LC 36 only validates, so sets grow monotonically. LC 37 *solves*, so every placement must be undone — the sets become a **mutable constraint index**: `add` before recursing, `remove` when the branch fails. That O(1) undo is exactly what a set buys over rescanning the row/col/box.

```python
# python
# LC 37 - Sudoku Solver
# IDEA: 3 constraint sets (row/col/box) + backtracking; undo = set.remove()
# time = O(9^E) worst (E = empty cells), space = O(E) recursion + O(81) sets
class Solution(object):
    def solveSudoku(self, board):
        rows = [set() for _ in range(9)]
        cols = [set() for _ in range(9)]
        boxes = [set() for _ in range(9)]
        empties = []

        for i in range(9):
            for j in range(9):
                v = board[i][j]
                if v == '.':
                    empties.append((i, j))
                else:
                    rows[i].add(v); cols[j].add(v); boxes[(i // 3) * 3 + j // 3].add(v)

        def dfs(k):
            if k == len(empties):
                return True
            i, j = empties[k]
            b = (i // 3) * 3 + j // 3
            for v in "123456789":
                if v in rows[i] or v in cols[j] or v in boxes[b]:
                    continue                                       # O(1) legality check
                rows[i].add(v); cols[j].add(v); boxes[b].add(v)     # place
                board[i][j] = v
                if dfs(k + 1):
                    return True
                rows[i].remove(v); cols[j].remove(v); boxes[b].remove(v)  # undo
                board[i][j] = '.'
            return False

        dfs(0)
```

```java
// java
// LC 37 - Sudoku Solver
// time = O(9^E) worst (E = empty cells), space = O(E) recursion + O(81) sets
Set<Character>[] rows, cols, boxes;
List<int[]> empties;

@SuppressWarnings("unchecked")
public void solveSudoku(char[][] board) {
    rows = new HashSet[9]; cols = new HashSet[9]; boxes = new HashSet[9];
    for (int i = 0; i < 9; i++) {
        rows[i] = new HashSet<>(); cols[i] = new HashSet<>(); boxes[i] = new HashSet<>();
    }
    empties = new ArrayList<>();

    for (int i = 0; i < 9; i++) {
        for (int j = 0; j < 9; j++) {
            char v = board[i][j];
            if (v == '.') empties.add(new int[]{i, j});
            else { rows[i].add(v); cols[j].add(v); boxes[(i / 3) * 3 + j / 3].add(v); }
        }
    }
    dfs(board, 0);
}

private boolean dfs(char[][] board, int k) {
    if (k == empties.size()) return true;
    int i = empties.get(k)[0], j = empties.get(k)[1], b = (i / 3) * 3 + j / 3;

    for (char v = '1'; v <= '9'; v++) {
        if (rows[i].contains(v) || cols[j].contains(v) || boxes[b].contains(v)) continue;
        rows[i].add(v); cols[j].add(v); boxes[b].add(v);            // place
        board[i][j] = v;
        if (dfs(board, k + 1)) return true;
        rows[i].remove(v); cols[j].remove(v); boxes[b].remove(v);   // undo on backtrack
        board[i][j] = '.';
    }
    return false;
}
```

### 9) Minimum Area Rectangle — LC 939 — a set of encoded points


#### Core Idea

**Set membership on *composite* keys — turn a geometric search into O(1) lookups.**

Pick any two points as a **diagonal**; the rectangle they define is fully determined, so the other two corners are known *exactly*. The only question is whether they exist — which is a set lookup, not a search.

```text
(x1,y1) and (x2,y2) with x1!=x2 and y1!=y2  ->  need (x1,y2) and (x2,y1)

     (x1,y2) o---------o (x2,y2)
             |         |
             |         |
     (x1,y1) o---------o (x2,y1)
```

Two points on the same row or column can't be a diagonal → skip them. Brute force over all 4-tuples is O(n^4); this is **O(n²)**.

**Encoding the key**: Python can hash a `tuple` directly. Java can't hash `int[]`, so either encode into a single `int`/`long` (`x * BIG + y`) or use a `Set<String>`.

```python
# python
# LC 939 - Minimum Area Rectangle
# IDEA: fix a diagonal pair, the other 2 corners are determined -> O(1) set lookups
# time = O(n^2), space = O(n)
class Solution(object):
    def minAreaRect(self, points):
        pts = set(map(tuple, points))    # tuples are hashable; lists are not
        best = float('inf')
        n = len(points)

        for i in range(n):
            x1, y1 = points[i]
            for j in range(i + 1, n):
                x2, y2 = points[j]
                if x1 == x2 or y1 == y2:
                    continue             # same row/col -> not a diagonal
                if (x1, y2) in pts and (x2, y1) in pts:
                    best = min(best, abs(x1 - x2) * abs(y1 - y2))

        return 0 if best == float('inf') else best
```

```java
// java
// LC 939 - Minimum Area Rectangle
// time = O(n^2), space = O(n)
public int minAreaRect(int[][] points) {
    // int[] has no value-based hashCode -> encode (x,y) into ONE key (0 <= x,y <= 40000)
    Set<Integer> pts = new HashSet<>();
    for (int[] p : points) pts.add(p[0] * 40001 + p[1]);

    int best = Integer.MAX_VALUE;
    for (int i = 0; i < points.length; i++) {
        for (int j = i + 1; j < points.length; j++) {
            int x1 = points[i][0], y1 = points[i][1];
            int x2 = points[j][0], y2 = points[j][1];
            if (x1 == x2 || y1 == y2) continue;          // same row/col -> not a diagonal
            if (pts.contains(x1 * 40001 + y2) && pts.contains(x2 * 40001 + y1)) {
                best = Math.min(best, Math.abs(x1 - x2) * Math.abs(y1 - y2));
            }
        }
    }
    return best == Integer.MAX_VALUE ? 0 : best;
}
```

> **Hashability cheat sheet** — Python: `tuple`/`frozenset` are hashable, `list`/`set`/`dict` are not. Java: `int[]` hashes by **identity** (`new HashSet<int[]>` never finds anything) — encode to `Integer`/`Long`/`String`, or use `List<Integer>`, which does hash by value.

### 10) Number of Distinct Islands — LC 694 — a set of canonical shapes

```python
# LC 694. Number of Distinct Islands
# V0
# IDEA: Use set to store unique island shapes
class Solution:
    def numDistinctIslands(self, grid):
        if not grid:
            return 0

        def dfs(i, j, i0, j0):
            # Record relative position from starting point
            if (0 <= i < len(grid) and 0 <= j < len(grid[0]) and
                grid[i][j] == 1):
                grid[i][j] = 0
                path.append((i - i0, j - j0))
                dfs(i+1, j, i0, j0)
                dfs(i-1, j, i0, j0)
                dfs(i, j+1, i0, j0)
                dfs(i, j-1, i0, j0)

        shapes = set()
        for i in range(len(grid)):
            for j in range(len(grid[0])):
                if grid[i][j] == 1:
                    path = []
                    dfs(i, j, i, j)
                    # Convert list to tuple for hashing
                    shapes.add(tuple(path))

        return len(shapes)
```

## Sets Inside Other Algorithms

### 11) Insert Delete GetRandom O(1) — LC 380 — set semantics, array storage ⭐⭐⭐⭐


#### Core Idea

**Set + dense array — the "randomized set"**

A `HashSet` gives O(1) `insert` / `remove` / `contains`, but it **cannot do `getRandom()` in O(1)** — there is no positional indexing, so picking a uniformly random member costs O(n).

An array can index in O(1) but can't test membership in O(1). **Use both**, and keep them in sync:

```text
arr  : dense array of members        -> getRandom = arr[rand(size)]     O(1)
idx  : member -> its position in arr -> contains / locate for delete    O(1)
```

The only hard part is **delete**: removing from the middle of an array is O(n). Fix it by **swapping the last element into the hole**, then popping the tail — order in `arr` is irrelevant because we only ever sample it randomly.

```text
remove(2) from arr=[1,2,3,4], idx={1:0,2:1,3:2,4:3}

  step 1: overwrite hole with last     arr=[1,4,3,4]  idx[4]=1
  step 2: pop the tail                 arr=[1,4,3]
  step 3: drop the key                 idx={1:0,4:1,3:2}
```

```python
# python
# LC 380 - Insert Delete GetRandom O(1)
# IDEA: hash index (val -> position) + dense array; delete = swap-with-last
# time = O(1) per op, space = O(n)
import random

class RandomizedSet(object):
    def __init__(self):
        self.arr = []      # dense array of members
        self.idx = {}      # val -> position in arr

    def insert(self, val):
        if val in self.idx:
            return False
        self.idx[val] = len(self.arr)
        self.arr.append(val)
        return True

    def remove(self, val):
        if val not in self.idx:
            return False
        i = self.idx[val]
        last = self.arr[-1]
        self.arr[i] = last       # move last member into the hole
        self.idx[last] = i
        self.arr.pop()
        del self.idx[val]        # delete AFTER the overwrite (val may BE the last element)
        return True

    def getRandom(self):
        return random.choice(self.arr)
```

```java
// java
// LC 380 - Insert Delete GetRandom O(1)
// time = O(1) per op, space = O(n)
class RandomizedSet {
    private final List<Integer> arr = new ArrayList<>();        // dense array of members
    private final Map<Integer, Integer> idx = new HashMap<>();  // val -> position in arr
    private final Random rand = new Random();

    public boolean insert(int val) {
        if (idx.containsKey(val)) return false;
        idx.put(val, arr.size());
        arr.add(val);
        return true;
    }

    public boolean remove(int val) {
        Integer i = idx.get(val);
        if (i == null) return false;
        int last = arr.get(arr.size() - 1);
        arr.set(i, last);                 // move last member into the hole
        idx.put(last, i);
        arr.remove(arr.size() - 1);       // remove(int) = remove BY INDEX -> O(1) at the tail
        idx.remove(val);                  // remove AFTER the overwrite (val may BE last)
        return true;
    }

    public int getRandom() {
        return arr.get(rand.nextInt(arr.size()));
    }
}
```

> **Two traps**: (1) do `idx.remove(val)` **after** `idx.put(last, i)` — when `val` *is* the last element the reversed order deletes the key you just wrote; (2) in Java, `arr.remove(arr.size()-1)` picks the `remove(int index)` overload — passing an `Integer` would call `remove(Object)` and delete by value.

### 12) Word Ladder — LC 127 — bidirectional BFS with two frontier sets ⭐⭐⭐⭐


#### Core Idea

**Two frontier sets + "remove == visited" + O(1) meet test**

Sets do three distinct jobs here, and that's why this problem is a set problem rather than a queue problem:

| Set | Job |
|-----|-----|
| `words` | the dictionary — O(1) "is this a real word?" |
| `words.remove(cand)` | **marks visited by deleting**, so no separate `visited` set is needed |
| `begin` / `end` | the two BFS frontiers — `cand in end` is an O(1) **meet test** |

**Key trick**: search from both ends and **always expand the smaller frontier** (just swap the two set references). A one-directional BFS explores `b^d` nodes; meeting in the middle explores `2 * b^(d/2)` — a huge win on branchy word graphs.

```text
one-directional:  begin ------------------------> end     b^d
bidirectional:    begin -------><------- end             2 * b^(d/2)
                            meet here
```

```python
# python
# LC 127 - Word Ladder
# IDEA: bidirectional BFS; frontiers are sets, deleting from the pool = marking visited
# time = O(N * L * 26), space = O(N * L)
import string

class Solution(object):
    def ladderLength(self, beginWord, endWord, wordList):
        words = set(wordList)
        if endWord not in words:
            return 0
        words.discard(beginWord)

        begin, end = {beginWord}, {endWord}
        steps = 1

        while begin and end:
            if len(begin) > len(end):          # always expand the SMALLER frontier
                begin, end = end, begin

            nxt = set()
            for w in begin:
                for i in range(len(w)):
                    for c in string.ascii_lowercase:
                        cand = w[:i] + c + w[i + 1:]
                        if cand in end:        # frontiers met -> done
                            return steps + 1
                        if cand in words:
                            words.remove(cand) # mark visited by deleting from the pool
                            nxt.add(cand)
            begin = nxt
            steps += 1

        return 0
```

```java
// java
// LC 127 - Word Ladder
// time = O(N * L * 26), space = O(N * L)
public int ladderLength(String beginWord, String endWord, List<String> wordList) {
    Set<String> words = new HashSet<>(wordList);
    if (!words.contains(endWord)) return 0;
    words.remove(beginWord);

    Set<String> begin = new HashSet<>(), end = new HashSet<>();
    begin.add(beginWord);
    end.add(endWord);

    int steps = 1;
    while (!begin.isEmpty() && !end.isEmpty()) {
        if (begin.size() > end.size()) {              // always expand the SMALLER frontier
            Set<String> tmp = begin; begin = end; end = tmp;
        }

        Set<String> next = new HashSet<>();
        for (String w : begin) {
            char[] ch = w.toCharArray();
            for (int i = 0; i < ch.length; i++) {
                char old = ch[i];
                for (char c = 'a'; c <= 'z'; c++) {
                    ch[i] = c;
                    String cand = new String(ch);
                    if (end.contains(cand)) return steps + 1;   // frontiers met
                    if (words.remove(cand)) next.add(cand);     // remove() == mark visited
                }
                ch[i] = old;
            }
        }
        begin = next;
        steps++;
    }
    return 0;
}
```

**Dry run — `begin="hit"`, `end="cog"`, dict `[hot,dot,dog,lot,log,cog]`:**
```text
words = {hot,dot,dog,lot,log,cog}          ("hit" discarded up front)

steps=1  begin={hit}      end={cog}         expand hit -> nxt={hot}
steps=2  begin={hot}      end={cog}         expand hot -> nxt={dot,lot}
         |begin|=2 > |end|=1  -> SWAP
steps=3  begin={cog}      end={dot,lot}     expand cog -> nxt={dog,log,cog*}
         |begin|=3 > |end|=2  -> SWAP
steps=4  begin={dot,lot}  end={dog,log,cog}
         "dot" -> "dog"  IS IN end  ->  return steps + 1 = 5

* cog re-enters its own frontier (it is still in the pool). Harmless — add
  words.discard(endWord) up front if you prefer to keep the frontiers clean.
```

> **Why check `cand in end` BEFORE `cand in words`**: frontier words were already deleted from `words` when they were generated, so the membership test would miss them. The meet test must come first.

### 13) Odd Even Jump — LC 975 — an ORDERED set, for floor/ceiling


#### Core Idea

**When you need "closest value ≥ x" or "closest value ≤ x", a hash set is useless — you need an ORDERED set.**

This is the single thing a `TreeSet`/`TreeMap` buys you over a `HashSet`: **predecessor / successor queries in O(log n)**. A hash set can only answer "is `x` present?" exactly.

| Need | Java (`TreeSet` / `TreeMap`) | Python (`bisect` on a sorted list) |
|------|------------------------------|-------------------------------------|
| smallest value **≥ x** (ceiling) | `ceiling(x)` / `ceilingEntry(x)` | `i = bisect_left(a, x)` → `a[i]` |
| largest value **≤ x** (floor) | `floor(x)` / `floorEntry(x)` | `i = bisect_right(a, x)` → `a[i-1]` |
| smallest value **> x** (higher) | `higher(x)` | `i = bisect_right(a, x)` → `a[i]` |
| largest value **< x** (lower) | `lower(x)` | `i = bisect_left(a, x)` → `a[i-1]` |
| min / max | `first()` / `last()` | `a[0]` / `a[-1]` |

**Problem mapping**: scan **right to left**, keeping an ordered set of all values at indices `> i`. Then the odd (upward) jump from `i` is `ceiling(arr[i])` and the even (downward) jump is `floor(arr[i])`. Ties break to the **smallest index**, which comes free: scanning backwards, a later write always holds a smaller index.

The DP is two booleans per index — "can I reach the end starting here with an odd/even jump":
```text
odd[i]  = even[j]   where j = index of ceiling(arr[i])
even[i] = odd[j]    where j = index of floor(arr[i])
odd[n-1] = even[n-1] = True        answer = count of odd[i] == True
```

```java
// java
// LC 975 - Odd Even Jump
// IDEA: ordered set (TreeMap) gives ceiling/floor of the values to the RIGHT of i
// time = O(n log n), space = O(n)
public int oddEvenJumps(int[] arr) {
    int n = arr.length;
    boolean[] odd = new boolean[n], even = new boolean[n];
    odd[n - 1] = even[n - 1] = true;

    // value -> smallest index > i holding it
    TreeMap<Integer, Integer> seen = new TreeMap<>();
    seen.put(arr[n - 1], n - 1);

    for (int i = n - 2; i >= 0; i--) {
        Map.Entry<Integer, Integer> hi = seen.ceilingEntry(arr[i]);  // smallest value >= arr[i]
        Map.Entry<Integer, Integer> lo = seen.floorEntry(arr[i]);    // largest  value <= arr[i]
        if (hi != null) odd[i]  = even[hi.getValue()];
        if (lo != null) even[i] = odd[lo.getValue()];
        seen.put(arr[i], i);   // scanning backwards -> this index is the smallest so far
    }

    int cnt = 0;
    for (boolean b : odd) if (b) cnt++;
    return cnt;
}
```

```python
# python
# LC 975 - Odd Even Jump
# IDEA: python has no TreeSet -> keep a sorted list + bisect for ceiling/floor
# time = O(n^2) worst with insort (O(n log n) with sortedcontainers.SortedList), space = O(n)
import bisect

class Solution(object):
    def oddEvenJumps(self, arr):
        n = len(arr)
        odd, even = [False] * n, [False] * n
        odd[n - 1] = even[n - 1] = True

        vals = [arr[n - 1]]          # sorted DISTINCT values at indices > i
        pos = {arr[n - 1]: n - 1}    # value -> smallest such index

        for i in range(n - 2, -1, -1):
            a = arr[i]
            k = bisect.bisect_left(vals, a)

            if k < len(vals):                       # ceiling = smallest value >= a
                odd[i] = even[pos[vals[k]]]

            if k < len(vals) and vals[k] == a:      # floor = largest value <= a
                even[i] = odd[pos[vals[k]]]
            elif k > 0:
                even[i] = odd[pos[vals[k - 1]]]

            if a not in pos:
                bisect.insort(vals, a)
            pos[a] = i               # later in the loop = smaller index -> overwrite

        return sum(odd)
```

> **Python has no built-in ordered set.** Options: `sortedcontainers.SortedList` (true O(log n), but not on LeetCode's default runtime for every language build), `bisect` over a list you keep sorted (query O(log n), **insert O(n)**), or sidestep it entirely — LC 975 also falls to sorting indices by value plus a monotonic stack in O(n log n).

### 14) Lowest Common Ancestor of a Binary Tree III — LC 1650 — a set of ancestors

```python
# LC 1650. Lowest Common Ancestor of a Binary Tree III
# NOTE : there are also dict, recursive.. approaches

# V0''
# IDEA : set - track ancestry path
# Time: O(h) where h is tree height
# Space: O(h) for storing ancestors
class Solution:
    def lowestCommonAncestor(self, p, q):
        # Store all ancestors of p
        visited = set()
        while p:
            visited.add(p)
            p = p.parent

        # Find first common ancestor with q
        while q:
            if q in visited:
                return q
            q = q.parent
```
