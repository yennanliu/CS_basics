# Set

## LeetCode Problem Lists

- [Hash Table](https://leetcode.com/problem-list/hash-table/)
- [Ordered Set](https://leetcode.com/problem-list/ordered-set/)

## Time Complexity

| Data structure | Search   | Insert   | Delete   | Min/Max  |
| -------------- | -------- | -------- | -------- | -------- |
| Hash Set (avg) | O(1)     | O(1)     | O(1)     | O(n)     |

> Average case shown. **Worst case (all elements collide): O(n).** Min/Max requires a full scan (hashing imposes no ordering).

<img src="https://github.com/yennanliu/CS_basics/blob/master/doc/pic/set_operations.png">

## Overview
**Set** is a collection data structure that stores unique elements with no duplicates. It provides efficient membership testing, insertion, and deletion operations.

### Key Properties
- **Time Complexity**:
  - Add: O(1) average, O(n) worst
  - Remove: O(1) average, O(n) worst
  - Contains: O(1) average, O(n) worst
  - Union/Intersection: O(min(len(s1), len(s2)))
- **Space Complexity**: O(n)
- **Core Features**: No duplicates, unordered (HashSet), O(1) lookups
- **When to Use**: Remove duplicates, membership testing, set operations (union, intersection, difference)

## 0) Concept

### 0-1) Types

#### HashSet
- **Python**: `set()` - unordered, fastest operations
- **Java**: `HashSet<T>` - backed by HashMap
- **Time**: O(1) average for add/remove/contains
- **Use case**: When order doesn't matter, need fast lookups

#### LinkedHashSet
- **Python**: No native support (use OrderedDict keys)
- **Java**: `LinkedHashSet<T>` - maintains insertion order
- **Time**: O(1) for operations, preserves order
- **Use case**: Need set operations + insertion order

#### TreeSet
- **Python**: No native support (use sorted containers)
- **Java**: `TreeSet<T>` - sorted, uses Red-Black tree
- **Time**: O(log n) for add/remove/contains
- **Use case**: Need sorted elements, range queries

### Implementation Comparison
| Type | Ordering | Time | Space | Use Case |
|------|----------|------|-------|----------|
| **HashSet** | None | O(1) | O(n) | Fast lookups, no order needed |
| **LinkedHashSet** | Insertion | O(1) | O(n) | Preserve insertion order |
| **TreeSet** | Sorted | O(log n) | O(n) | Sorted data, range queries |

### 0-2) Pattern

#### Pattern 1: Set Operations
```python
# Union, Intersection, Difference
s1 = {1, 2, 3}
s2 = {2, 3, 4}

union = s1 | s2          # {1, 2, 3, 4}
intersection = s1 & s2   # {2, 3}
difference = s1 - s2     # {1}
symmetric_diff = s1 ^ s2 # {1, 4}
```

#### Pattern 2: Duplicate Detection
```python
# Check for duplicates in array
def has_duplicate(nums):
    return len(nums) != len(set(nums))

# Find duplicates
def find_duplicates(nums):
    seen = set()
    duplicates = set()
    for num in nums:
        if num in seen:
            duplicates.add(num)
        seen.add(num)
    return duplicates
```

#### Pattern 3: Two-Set Tracking
```python
# Track visited and current path (for cycle detection)
def has_cycle(graph, start):
    visited = set()
    current_path = set()

    def dfs(node):
        if node in current_path:
            return True  # Cycle detected
        if node in visited:
            return False

        visited.add(node)
        current_path.add(node)

        for neighbor in graph[node]:
            if dfs(neighbor):
                return True

        current_path.remove(node)
        return False

    return dfs(start)
```

#### Pattern 4: Set for Path/Ancestry Tracking
```python
# LC 1650 - Find LCA using set to track ancestors
def lowestCommonAncestor(p, q):
    # Track all ancestors of p
    ancestors = set()
    while p:
        ancestors.add(p)
        p = p.parent

    # Find first common ancestor
    while q:
        if q in ancestors:
            return q
        q = q.parent
    return None
```

## 1) General form

### 1-1) Basic OP

#### 1-1-1) Set Creation and Basic Operations
```python
# Python
# Create empty set
s = set()
s = {}  # Wrong! This creates a dict

# Create with elements
s = {1, 2, 3}
s = set([1, 2, 3])
s = set("abc")  # {'a', 'b', 'c'}

# Add element
s.add(4)

# Remove element
s.remove(3)     # Raises KeyError if not exists
s.discard(3)    # No error if not exists
s.pop()         # Remove and return arbitrary element

# Check membership
if 2 in s:
    print("Found")

# Size
len(s)

# Clear all
s.clear()
```

```java
// Java
// Create HashSet
Set<Integer> set = new HashSet<>();

// Add element
set.add(1);
set.add(2);
set.add(3);

// Remove element
set.remove(2);

// Check membership
if (set.contains(1)) {
    System.out.println("Found");
}

// Size
int size = set.size();

// Clear
set.clear();

// Iterate
for (int num : set) {
    System.out.println(num);
}
```

#### 1-1-2) Set Operations
```python
# Python set operations
s1 = {1, 2, 3, 4}
s2 = {3, 4, 5, 6}

# Union (elements in either set)
union1 = s1 | s2
union2 = s1.union(s2)           # {1, 2, 3, 4, 5, 6}

# Intersection (elements in both sets)
inter1 = s1 & s2
inter2 = s1.intersection(s2)    # {3, 4}

# Difference (elements in s1 but not s2)
diff1 = s1 - s2
diff2 = s1.difference(s2)       # {1, 2}

# Symmetric difference (elements in either but not both)
sym1 = s1 ^ s2
sym2 = s1.symmetric_difference(s2)  # {1, 2, 5, 6}

# Subset check
is_subset = s1.issubset(s2)     # False
is_superset = s1.issuperset(s2) # False

# Disjoint check (no common elements)
is_disjoint = s1.isdisjoint(s2) # False
```

```java
// Java set operations
Set<Integer> s1 = new HashSet<>(Arrays.asList(1, 2, 3, 4));
Set<Integer> s2 = new HashSet<>(Arrays.asList(3, 4, 5, 6));

// Union
Set<Integer> union = new HashSet<>(s1);
union.addAll(s2);  // {1, 2, 3, 4, 5, 6}

// Intersection
Set<Integer> intersection = new HashSet<>(s1);
intersection.retainAll(s2);  // {3, 4}

// Difference
Set<Integer> difference = new HashSet<>(s1);
difference.removeAll(s2);  // {1, 2}

// Subset check
boolean isSubset = s2.containsAll(s1);  // false
```

#### 1-1-3) Converting Between Collections
```python
# Python conversions
arr = [1, 2, 2, 3, 3, 4]

# Array to set (remove duplicates)
s = set(arr)  # {1, 2, 3, 4}

# Set to array
arr_unique = list(s)

# Set to sorted array
arr_sorted = sorted(s)

# String to set
char_set = set("hello")  # {'h', 'e', 'l', 'o'}

# Set to string
s = {'a', 'b', 'c'}
string = ''.join(sorted(s))  # 'abc'
```

```java
// Java conversions
Integer[] arr = {1, 2, 2, 3, 3, 4};

// Array to set
Set<Integer> set = new HashSet<>(Arrays.asList(arr));

// Set to array
Integer[] arrUnique = set.toArray(new Integer[0]);

// Set to list
List<Integer> list = new ArrayList<>(set);

// List to set
Set<Integer> set2 = new HashSet<>(list);
```

## 2) LC Example

### 2-1) Lowest Common Ancestor of a Binary Tree III — LC 1650
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

### 2-2) Contains Duplicate — LC 217
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

### 2-3) Intersection of Two Arrays — LC 349
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

### 2-4) Happy Number — LC 202
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

### 2-5) Longest Consecutive Sequence — LC 128

#### Core Idea

**Set + "sequence start" gate — O(n) time**

The key observation: a number `num` is the **start of a sequence** only if `num - 1` is NOT in the set. This gate prevents re-counting the same sequence from every element inside it.

```
Without the gate: starting from 2 in [1,2,3,4] would count [2,3,4] (length 3),
                  double-counting work already done from 1.
With the gate:    only 1 passes (1-1=0 not in set), so we count exactly once.
```

Once a sequence start is found, extend it by checking `num + length` in the set — each step is O(1). Every element is visited at most twice across all sequences → **total O(n)**.

```
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
```
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

### 2-6) Single Number — LC 136
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

### 2-7) Valid Sudoku — LC 36
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

### 2-8) Number of Distinct Islands — LC 694
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

### 2-9) Linked List Cycle Detection — LC 141
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

### 2-10) Word Pattern — LC 290
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

### 2-11) Insert Delete GetRandom O(1) — LC 380

#### Core Idea

**Set + dense array — the "randomized set"**

A `HashSet` gives O(1) `insert` / `remove` / `contains`, but it **cannot do `getRandom()` in O(1)** — there is no positional indexing, so picking a uniformly random member costs O(n).

An array can index in O(1) but can't test membership in O(1). **Use both**, and keep them in sync:

```
arr  : dense array of members        -> getRandom = arr[rand(size)]     O(1)
idx  : member -> its position in arr -> contains / locate for delete    O(1)
```

The only hard part is **delete**: removing from the middle of an array is O(n). Fix it by **swapping the last element into the hole**, then popping the tail — order in `arr` is irrelevant because we only ever sample it randomly.

```
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

### 2-12) Word Ladder — bidirectional BFS with two sets — LC 127

#### Core Idea

**Two frontier sets + "remove == visited" + O(1) meet test**

Sets do three distinct jobs here, and that's why this problem is a set problem rather than a queue problem:

| Set | Job |
|-----|-----|
| `words` | the dictionary — O(1) "is this a real word?" |
| `words.remove(cand)` | **marks visited by deleting**, so no separate `visited` set is needed |
| `begin` / `end` | the two BFS frontiers — `cand in end` is an O(1) **meet test** |

**Key trick**: search from both ends and **always expand the smaller frontier** (just swap the two set references). A one-directional BFS explores `b^d` nodes; meeting in the middle explores `2 * b^(d/2)` — a huge win on branchy word graphs.

```
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
```
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

### 2-13) Odd Even Jump — ordered set floor/ceiling — LC 975

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
```
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

### 2-14) Minimum Area Rectangle — set of encoded points — LC 939

#### Core Idea

**Set membership on *composite* keys — turn a geometric search into O(1) lookups.**

Pick any two points as a **diagonal**; the rectangle they define is fully determined, so the other two corners are known *exactly*. The only question is whether they exist — which is a set lookup, not a search.

```
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

## Problem Categories

### Category 1: Duplicate Detection (10 problems)
| Problem | LC # | Difficulty | Pattern | Key Insight |
|---------|------|------------|---------|-------------|
| Contains Duplicate | 217 | Easy | Set size | len(nums) != len(set(nums)) |
| Contains Duplicate II | 219 | Easy | Sliding window set | Keep window of k elements |
| Contains Duplicate III | 220 | Medium | TreeSet/SortedList | Maintain sorted window |
| Find Duplicate | 287 | Medium | Cycle detection | Floyd's algorithm or set |
| Find All Duplicates | 442 | Medium | Index marking | Use array as hashmap |
| Single Number | 136 | Easy | XOR/Set | XOR cancels duplicates |
| Single Number II | 137 | Medium | Bit manipulation | Count bits mod 3 |
| Single Number III | 260 | Medium | XOR + grouping | Group by differing bit |
| Missing Number | 268 | Easy | Set/XOR | Expected vs actual |
| First Missing Positive | 41 | Hard | In-place set | Use array indices |

### Category 2: Set Operations (8 problems)
| Problem | LC # | Difficulty | Pattern | Key Insight |
|---------|------|------------|---------|-------------|
| Intersection of Two Arrays | 349 | Easy | Set intersection | set1 & set2 |
| Intersection of Two Arrays II | 350 | Easy | Counter | Track frequencies |
| Union of Two Arrays | - | Easy | Set union | set1 | set2 |
| Distribute Candies | 575 | Easy | Set size | min(len(set), n/2) |
| Uncommon Words | 884 | Easy | Set difference | Count once in either |
| Set Mismatch | 645 | Easy | Set difference | Find duplicate & missing |
| Fair Candy Swap | 888 | Easy | Set membership | Target difference |
| Buddy Strings | 859 | Easy | Set of pairs | Check swap possible |

### Category 3: Path/Ancestry Tracking (6 problems)
| Problem | LC # | Difficulty | Pattern | Key Insight |
|---------|------|------------|---------|-------------|
| Lowest Common Ancestor III | 1650 | Medium | Ancestor set | Track parent path |
| Linked List Cycle | 141 | Easy | Visited set | Two pointers better |
| Linked List Cycle II | 142 | Medium | Visited set | Floyd's algorithm |
| Course Schedule | 207 | Medium | DFS + set | Detect cycle |
| Course Schedule II | 210 | Medium | Topological sort | Track visited/path |
| Find Eventual Safe Nodes | 802 | Medium | DFS + states | Terminal vs unsafe |

### Category 4: Sequence Problems (7 problems)
| Problem | LC # | Difficulty | Pattern | Key Insight |
|---------|------|------------|---------|-------------|
| Longest Consecutive Sequence | 128 | Medium | Set lookups | Start from sequence begin |
| Longest Substring Without Repeat | 3 | Medium | Sliding window set | Track seen chars |
| Longest Palindrome | 409 | Easy | Char frequency | Pairs + one odd |
| Maximum Length of Repeated Subarray | 718 | Medium | Set of tuples | Rolling hash |
| Arithmetic Slices | 413 | Medium | Set of differences | Track valid sequences |
| Happy Number | 202 | Easy | Cycle detection | Track seen sums |
| Valid Sudoku | 36 | Medium | Multiple sets | Row/col/box tracking |

### Category 5: Graph/Island Problems (5 problems)
| Problem | LC # | Difficulty | Pattern | Key Insight |
|---------|------|------------|---------|-------------|
| Number of Islands | 200 | Medium | DFS/BFS visited | Track processed cells |
| Number of Distinct Islands | 694 | Medium | Shape hashing | Normalize positions |
| Max Area of Island | 695 | Medium | DFS + visited | Track seen cells |
| Island Perimeter | 463 | Easy | Border counting | Count land-water edges |
| Surrounded Regions | 130 | Medium | Border DFS | Mark connected to border |

### Category 6: String/Pattern Matching (6 problems)
| Problem | LC # | Difficulty | Pattern | Key Insight |
|---------|------|------------|---------|-------------|
| Isomorphic Strings | 205 | Easy | Bijection | Two maps or sets |
| Word Pattern | 290 | Easy | Bijection | Char ↔ word mapping |
| Group Anagrams | 49 | Medium | Sorted key | Use sorted string |
| Find Anagrams | 438 | Medium | Window + counter | Sliding character counts |
| Jewels and Stones | 771 | Easy | Set membership | Set of jewels |
| Unique Email Addresses | 929 | Easy | Normalize + set | Clean emails |

### Category 7: Ordered Set (TreeSet) & Set-as-Index (8 problems)

Problems where a **plain HashSet is not enough** — you need order (floor/ceiling), positional indexing, or a composite key.

| Problem | LC # | Difficulty | Pattern | Key Insight |
|---------|------|------------|---------|-------------|
| Insert Delete GetRandom O(1) | 380 | Medium | Set + dense array | Swap-with-last delete → §2-11 |
| Word Ladder | 127 | Hard | Two frontier sets | Bidirectional BFS, `remove` = visited → §2-12 |
| Odd Even Jump | 975 | Hard | TreeMap floor/ceiling | Closest value ≥ / ≤ x → §2-13 |
| Minimum Area Rectangle | 939 | Medium | Set of encoded points | Diagonal determines the other 2 corners → §2-14 |
| The Skyline Problem | 218 | Hard | Ordered multiset | Need max **with arbitrary delete** → `TreeMap<height,count>` |
| Falling Squares | 699 | Hard | Ordered interval set | Query max height over a range, then overwrite it |
| Set Matrix Zeroes | 73 | Medium | Row set + col set | Two sets mark what to zero (O(1)-space follow-up: use row 0 / col 0) |
| Intersection of Two Linked Lists | 160 | Easy | Visited node set | Set of nodes works; two-pointer switch is the O(1)-space answer |

## Decision Framework

### When to Use Set vs Other Data Structures

```
Problem Analysis:

1. Need to track unique elements?
   ├── YES → Consider Set
   │   ├── Need ordering?
   │   │   ├── YES → TreeSet (Java) / sorted list (Python)
   │   │   └── NO → HashSet
   │   ├── Need count?
   │   │   └── NO → Use Counter/HashMap instead
   │   └── Need fast lookups?
   │       └── YES → HashSet (O(1) average)
   └── NO → Consider other structures

2. Performing set operations (union, intersection)?
   ├── YES → Use Set
   │   └── Multiple operations → Build set once
   └── NO → Continue analysis

3. Detecting duplicates/cycles?
   ├── YES → Use Set for visited tracking
   │   ├── Space constrained?
   │   │   └── YES → Consider Floyd's algorithm
   │   └── NO → Set is ideal
   └── NO → Continue analysis

4. Checking membership repeatedly?
   ├── YES → Convert to Set first
   │   └── O(n) conversion + O(1) lookups
   └── NO → Linear search may be fine
```

### Set vs HashMap Choice

| Use Set When | Use HashMap When |
|--------------|------------------|
| Only need existence check | Need key-value mapping |
| Removing duplicates | Counting frequencies |
| Set operations (∪, ∩, -) | Need associated data |
| Memory efficient (no values) | Need to track counts/indices |

### Python Set vs Java Set

| Feature | Python `set` | Java `HashSet` |
|---------|-------------|----------------|
| **Creation** | `s = {1,2,3}` or `set()` | `Set<T> s = new HashSet<>()` |
| **Add** | `s.add(x)` | `s.add(x)` |
| **Remove** | `s.remove(x)` / `s.discard(x)` | `s.remove(x)` |
| **Contains** | `x in s` | `s.contains(x)` |
| **Union** | `s1 | s2` or `s1.union(s2)` | `s1.addAll(s2)` |
| **Intersection** | `s1 & s2` or `s1.intersection(s2)` | `s1.retainAll(s2)` |
| **Difference** | `s1 - s2` or `s1.difference(s2)` | `s1.removeAll(s2)` |
| **Size** | `len(s)` | `s.size()` |
| **Empty check** | `not s` or `len(s) == 0` | `s.isEmpty()` |

## Summary & Best Practices

### Key Takeaways

1. **When to Use Set**:
   - Remove duplicates from collection
   - Fast membership testing (O(1) average)
   - Performing set operations (union, intersection, difference)
   - Tracking visited nodes in graphs/trees
   - Detecting cycles

2. **Performance Characteristics**:
   - HashSet: O(1) average, O(n) worst (hash collisions)
   - TreeSet: O(log n) for all operations
   - LinkedHashSet: O(1) operations + insertion order

3. **Common Patterns**:
   - Convert array to set to remove duplicates
   - Use set for O(1) lookups instead of O(n) linear search
   - Track visited nodes with set
   - Detect cycles by checking if element already in set

4. **Space-Time Tradeoffs**:
   - Set uses O(n) extra space for O(1) operations
   - Consider two-pointer techniques if space is constrained
   - For small inputs, linear search may be faster

### Interview Tips

**Common Mistakes to Avoid:**
- Using `{}` to create empty set in Python (creates dict instead)
- Forgetting that sets are unordered (don't assume order)
- Not considering TreeSet when you need sorted elements
- Using set when you need to count occurrences (use Counter/HashMap)

**Optimization Tips:**
- Convert lists to sets before repeated membership checks
- Use set operations instead of manual loops
- Consider frozenset for immutable/hashable sets
- Use set comprehensions for cleaner code

**Follow-up Questions:**
- "Can you solve it with O(1) space?" → Consider Floyd's algorithm
- "What if we need to preserve order?" → LinkedHashSet or OrderedDict
- "What if we need sorted elements?" → TreeSet or sorted list
- "What about duplicates with different data?" → Use HashMap instead
