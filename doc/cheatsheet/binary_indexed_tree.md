# Binary Indexed Tree (Fenwick Tree)

> **Scope** — The Fenwick tree specifically — `i & -i` index arithmetic, point update + prefix query, and the problems it is the tidiest answer to.
> **See also**: [segment_tree.md](./segment_tree.md) — the more general range structure and the BIT-vs-segment-tree decision; [prefix_sum.md](./prefix_sum.md) — the static, no-update version.

## LeetCode Problem Lists

- [Binary Indexed Tree](https://leetcode.com/problem-list/binary-indexed-tree/)
- [Segment Tree](https://leetcode.com/problem-list/segment-tree/)

## Overview

A Binary Indexed Tree (BIT), also known as a Fenwick Tree, is a data structure that efficiently supports both:

Point updates (i.e., update an element in the array).
Prefix sum queries (i.e., query the sum of elements from the start of the array to a given index).
Key Features of a Binary Indexed Tree:
Efficient Updates: You can update an element in the array in O(log n) time.
Efficient Queries: You can compute the sum of elements in a range from the start to a given index in O(log n) time.
How It Works:
A BIT is built over an array and stores partial sums of the array in a tree-like structure. The array's indexes are represented in binary form, and each position in the BIT array stores a sum of a subset of array elements, based on the binary representation of the index.

Structure of the BIT:
The underlying array is 1-indexed (or you can adapt it to 0-indexed).
For each index i, the BIT stores the sum of a range of elements. The range size depends on the least significant set bit of the index i.
For example:

BIT[i] stores the sum of elements from index i - (i & -i) + 1 to i, where i & -i extracts the least significant set bit in the binary representation of i.
Operations:
1. Update Operation:
When you want to update an element in the original array, you adjust the BIT values to reflect this update. The update is propagated to all indices that are responsible for storing sums that include the updated index.

To update an index i in the BIT, you adjust the BIT at i and continue adjusting at i + (i & -i) (i.e., move to the next index that is part of the same group) until you reach the end of the BIT.
2. Prefix Sum Query:
To calculate the sum of elements from index 1 to i (inclusive), you start at i and keep subtracting the least significant set bit (i & -i) to move to previous elements in the BIT. The sum is computed by adding the values at these indices.

Example of a Binary Indexed Tree:
Consider an array: [3, 2, -1, 6, 5, 4].

BIT Initialization: Start with a BIT array of the same length (initially all zeros) and build it by updating the BIT based on the input array.

Update Operation: If you want to update an element in the original array (e.g., changing array[2] from -1 to 4), you update the relevant BIT values.

Prefix Sum Query: To find the sum from index 1 to i, you sum the relevant BIT values.



- LC 307

## Templates & Algorithms

### The BIT class — point update + prefix / range query


```java
// BIT (Fenwick Tree) — Generic Template
// time = O(log N) per update/query, space = O(N)
class BIT {
    int[] tree;
    int n;
    BIT(int n) { this.n = n; tree = new int[n + 1]; }
    void update(int i, int delta) {          // 1-indexed; add delta at position i
        for (; i <= n; i += i & -i) tree[i] += delta;
    }
    int query(int i) {                        // prefix sum [1..i]
        int sum = 0;
        for (; i > 0; i -= i & -i) sum += tree[i];
        return sum;
    }
    int query(int l, int r) { return query(r) - query(l - 1); } // range [l..r]
}
```


### Why `i & -i` — the lowbit mechanics ⭐⭐⭐⭐⭐

> **Key Idea**: `i & -i` isolates the **lowest set bit** of `i`. That value is exactly how many
> original elements `tree[i]` covers, so it is both the *step size* and the *range width*.

```text
n = 8, tree[i] covers the range (i - lowbit(i), i]   (1-indexed)

 i  | binary | lowbit | tree[i] covers
----+--------+--------+----------------
 1  |  0001  |   1    | [1..1]
 2  |  0010  |   2    | [1..2]
 3  |  0011  |   1    | [3..3]
 4  |  0100  |   4    | [1..4]
 5  |  0101  |   1    | [5..5]
 6  |  0110  |   2    | [5..6]
 7  |  0111  |   1    | [7..7]
 8  |  1000  |   8    | [1..8]

QUERY prefix(7): walk DOWN, i -= lowbit(i)   -> ADD disjoint blocks
    7 -> 6 -> 4 -> 0
    tree[7]  + tree[6]  + tree[4]
    [7..7]   + [5..6]   + [1..4]      = [1..7]   (exactly covers, no overlap)

UPDATE at 3: walk UP, i += lowbit(i)         -> every block CONTAINING 3
    3 -> 4 -> 8
    tree[3], tree[4], tree[8]
    [3..3]  [1..4]   [1..8]

Both walks strip / carry one bit per step  =>  O(log N).
```

**Two rules to memorise**

| Operation | Direction | Step | Why |
|-----------|-----------|------|-----|
| `query(i)` — prefix `[1..i]` | down to 0 | `i -= i & -i` | peel off the last covered block, jump to what's left |
| `update(i, d)` — point add | up to `n` | `i += i & -i` | jump to the next block that *contains* `i` |

> **Gotcha**: BIT is **1-indexed**, and index `0` fails *differently* on each side.
> `i & -i` is `0` at `i = 0`, so `update(0, d)` never advances and **loops forever**.
> `query(0)` does not hang — its loop condition is `i > 0`, so it returns `0` immediately and
> silently omits the element you meant. Convert with `i + 1` at the boundary either way
> (as LC 307 above does).


### BIT over POSITIONS — a "live" index after removals — LC 1505 / LC 1409 ⭐⭐⭐⭐

> **Pattern**: the BIT stores `1` at every slot that is **still present** and `0` where an element
> was removed/moved. Then `query(i)` = how many of the first `i` slots are alive = the element's
> **current index**, even though everything keeps shifting.

**Why a BIT**: a naive "how many survivors before me?" scan is `O(N)` per step → `O(N^2)`.
The BIT answers it in `O(log N)`, and the removal itself is a single point update.

**Key Idea**: never physically shift the array. Keep original slots fixed and let prefix sums
translate *original position* → *current position*.

#### Worked example A — LC 1505 (greedy + BIT for the true swap cost)

Greedy: build the answer digit by digit; at each step try digits `0..9` and take the smallest one
that is still affordable. The cost of dragging the digit at original index `i` to the front is
`i - (number of already-removed digits before i)` — that correction is what the BIT provides.

```java
// LC 1505 - Minimum Possible Integer After at Most K Adjacent Swaps On Digits
// IDEA: greedy smallest digit + BIT over POSITIONS to get the real (post-removal) swap cost
// time = O(N * 10 * log N), space = O(N)
public String minInteger(String num, int k) {
    int n = num.length();
    // queue of original indices for each digit, ascending
    List<Deque<Integer>> pos = new ArrayList<>();
    for (int d = 0; d <= 9; d++) pos.add(new ArrayDeque<>());
    for (int i = 0; i < n; i++) pos.get(num.charAt(i) - '0').addLast(i);

    BIT removed = new BIT(n);          // 1 at index -> that digit is already consumed
    StringBuilder sb = new StringBuilder();

    for (int step = 0; step < n; step++) {
        for (int d = 0; d <= 9; d++) {
            if (pos.get(d).isEmpty()) continue;
            int i = pos.get(d).peekFirst();               // earliest remaining occurrence
            // removed.query(i) = consumed digits among original idx 0..i-1
            int cost = i - removed.query(i);              // swaps needed to bring it to the front
            if (cost <= k) {
                k -= cost;
                pos.get(d).pollFirst();
                removed.update(i + 1, 1);                 // +1 -> 1-indexed BIT
                sb.append((char) ('0' + d));
                break;
            }
        }
    }
    return sb.toString();
}
```

```python
# python
# LC 1505 - Minimum Possible Integer After at Most K Adjacent Swaps On Digits
# IDEA: greedy smallest digit + BIT over POSITIONS to get the real (post-removal) swap cost
# time = O(N * 10 * log N), space = O(N)
# (reuses the BIT class from 2-13)
from collections import deque


def minInteger(num, k):
    n = len(num)
    pos = [deque() for _ in range(10)]          # original indices per digit
    for i, ch in enumerate(num):
        pos[int(ch)].append(i)

    removed = BIT(n)                            # 1 at index -> digit already consumed
    out = []

    for _ in range(n):
        for d in range(10):
            if not pos[d]:
                continue
            i = pos[d][0]                       # earliest remaining occurrence
            cost = i - removed.query(i)         # consumed among original idx 0..i-1
            if cost <= k:
                k -= cost
                pos[d].popleft()
                removed.update(i + 1, 1)        # +1 -> 1-indexed BIT
                out.append(str(d))
                break
    return "".join(out)
```

> **Why the greedy never stalls**: the smallest still-present original index always has `cost == 0`
> (nothing alive sits before it), so some digit is always affordable and the loop always emits one
> digit per step.

#### Worked example B — LC 1409 (move-to-front with a spare buffer)

Same BIT-over-positions idea, but elements move to the **front** instead of vanishing. Reserve
`n` empty slots in front of the initial `m` slots, so every move-to-front gets a fresh slot.

```java
// LC 1409 - Queries on a Permutation With Key
// IDEA: BIT over POSITIONS — n spare front slots; query(p-1) = # alive before p = current index
// time = O(N log(N+M)), space = O(N + M)
public int[] processQueries(int[] queries, int m) {
    int n = queries.length, size = n + m;
    BIT bit = new BIT(size);
    int[] posOf = new int[m + 1];
    // value v initially lives at BIT slot n+v  (slots 1..n are the reserved front buffer)
    for (int v = 1; v <= m; v++) { posOf[v] = n + v; bit.update(n + v, 1); }

    int front = n;                       // next free slot, filled right-to-left
    int[] res = new int[n];
    for (int q = 0; q < n; q++) {
        int p = posOf[queries[q]];
        res[q] = bit.query(p - 1);       // # of alive slots before p == current 0-based index
        bit.update(p, -1);               // vacate old slot
        posOf[queries[q]] = front;
        bit.update(front, 1);            // occupy new front slot
        front--;
    }
    return res;
}
```

```python
# python
# LC 1409 - Queries on a Permutation With Key
# IDEA: BIT over POSITIONS — n spare front slots; query(p-1) = # alive before p = current index
# time = O(N log(N+M)), space = O(N + M)
def processQueries(queries, m):
    n = len(queries)
    bit = BIT(n + m)
    pos_of = [0] * (m + 1)
    # value v initially lives at BIT slot n+v (slots 1..n are the reserved front buffer)
    for v in range(1, m + 1):
        pos_of[v] = n + v
        bit.update(n + v, 1)

    front = n                            # next free slot, filled right-to-left
    res = []
    for q in queries:
        p = pos_of[q]
        res.append(bit.query(p - 1))     # alive slots before p == current 0-based index
        bit.update(p, -1)                # vacate old slot
        pos_of[q] = front
        bit.update(front, 1)             # occupy new front slot
        front -= 1
    return res
```

> **Recognise this pattern when**: the problem says *"remove an element and everything after it
> shifts left"*, *"move to front"*, or *"cost = current distance"*. Fixed slots + a 0/1 BIT turn
> every shift into an `O(log N)` prefix count.


## Decision: BIT vs Segment Tree vs Merge Sort


Many BIT-tagged problems are **counting inversions in disguise**, and merge sort answers the same
question with no extra data structure. Pick deliberately:

| | Binary Indexed Tree | Segment Tree | Merge Sort (divide & conquer) |
|---|---|---|---|
| **Answers** | prefix aggregate + point update | arbitrary range query + range update | offline pair counting only |
| **Ops supported** | invertible ops only (sum, xor, count) | any associative op (min/max/gcd/sum) | one fixed count per merge |
| **Online (interleaved updates & queries)?** | yes | yes | **no** — needs the whole input up front |
| **Code size** | ~6 lines | ~40 lines | ~25 lines |
| **Constant factor** | smallest | ~2-3x BIT | small, but allocates temp arrays |
| **Typical LC** | 307, 315, 493, 1395, 1505, 1409, 1649 | 699, 715, 732, 850, 1622 | 315, 327, 493 |

**Rules of thumb**

- Need **min / max / gcd** over a range, or **lazy range updates**? → segment tree, not BIT.
  A BIT only works for operations you can *undo* (`query(r) - query(l-1)`), which rules out min/max.
- Need **interval insert / merge / "is this range covered"** (calendar & rectangle problems)? →
  segment tree with coordinate compression, or an ordered map. See `doc/cheatsheet/segment_tree.md`.
- **Offline** counting of pairs `(i, j)` with `i < j` and some order relation? → merge sort is
  usually the shortest correct answer, and needs no coordinate compression.
  See **Template 7: Merge Sort as a COUNTER** in `doc/cheatsheet/sort.md` for LC 315.
- **Same problem, two tools**: LC 315 / 327 / 493 are solvable both ways.
  - *Merge sort*: no compression, no value-range assumption — but strictly offline.
  - *BIT*: needs coordinate compression first, but extends for free to *online* streams
    (LC 1649) and to "count on both sides" sweeps (LC 1395), which merge sort cannot do.
- **No updates at all**? → don't build anything. A plain prefix-sum array is `O(1)` per query.
  See `doc/cheatsheet/prefix_sum.md`.


### The same problem both ways — LC 307


```java
// LC 307 - Range Sum Query Mutable (BIT version)
// IDEA: BIT — O(log N) point update and prefix sum; range via subtraction
// time = O(log N) per update/query, space = O(N)
class NumArray {
    int[] bit, nums;
    int n;
    public NumArray(int[] nums) {
        n = nums.length;
        this.nums = new int[n];
        bit = new int[n + 1];
        for (int i = 0; i < n; i++) update(i, nums[i]);
    }
    public void update(int i, int val) {
        int delta = val - nums[i];
        nums[i] = val;
        for (int x = i + 1; x <= n; x += x & -x) bit[x] += delta;
    }
    public int sumRange(int l, int r) { return prefix(r+1) - prefix(l); }
    private int prefix(int i) { int s=0; for(;i>0;i-=i&-i) s+=bit[i]; return s; }
}
```


## LC Examples

### Point Update, Prefix Query

#### 1) Range Sum Query — Mutable — LC 307

> BIT supports O(log N) point update and prefix sum query.

```java
// LC 307 - Range Sum Query - Mutable
// IDEA: Binary Indexed Tree (Fenwick Tree)
// time = O(log N) per update/query, space = O(N)
class NumArray {
    int[] bit, nums;
    int n;
    public NumArray(int[] nums) {
        this.n = nums.length;
        this.nums = new int[n];
        this.bit = new int[n + 1];
        for (int i = 0; i < n; i++) update(i, nums[i]);
    }
    public void update(int i, int val) {
        int delta = val - nums[i];
        nums[i] = val;
        for (int x = i + 1; x <= n; x += x & (-x)) bit[x] += delta;
    }
    public int sumRange(int left, int right) {
        return prefixSum(right + 1) - prefixSum(left);
    }
    private int prefixSum(int i) {
        int sum = 0;
        for (int x = i; x > 0; x -= x & (-x)) sum += bit[x];
        return sum;
    }
}
```

#### 2) Range Sum Query 2D — Mutable — LC 308 — a 2D BIT

> Extend BIT to 2D: O(log M * log N) per update/query on a matrix.

#### 1D BIT vs 2D BIT

| | 1D BIT (LC 307) | 2D BIT (LC 308) |
|---|---|---|
| **Structure** | `int[] bit` of size `n+1` | `int[][] bit` of size `(m+1) x (n+1)` |
| **Update** | Single loop: `i += i & -i` | Nested loops: row `i += i & -i`, col `j += j & -j` |
| **Query** | Single loop: `i -= i & -i` | Nested loops: row `i -= i & -i`, col `j -= j & -j` |
| **Range sum** | `prefix(r+1) - prefix(l)` | 2D inclusion-exclusion (4 terms) |
| **Time** | O(log N) | O(log M * log N) |

#### Why 2D Inclusion-Exclusion?

```text
To get sum of rectangle (r1,c1) to (r2,c2):

  query(r2, c2)           = entire top-left block
- query(r1-1, c2)         = remove rows above
- query(r2, c1-1)         = remove cols to the left
+ query(r1-1, c1-1)       = add back double-subtracted corner

Visual:

  ┌──────────┬──────────┐
  │  +added  │  -removed│     query(r1-1, c2) removes this top strip
  │  back    │  (top)   │
  ├──────────┼──────────┤  r1
  │  -removed│ ★ TARGET │
  │  (left)  │  REGION  │
  └──────────┴──────────┘  r2
            c1          c2

  sum = query(r2,c2) - query(r1-1,c2) - query(r2,c1-1) + query(r1-1,c1-1)
```

This is the same inclusion-exclusion as 2D prefix sum (LC 304), but BIT supports **mutable updates**.

#### Java Implementation

```java
// LC 308 - Range Sum Query 2D - Mutable
// IDEA: 2D Binary Indexed Tree (Fenwick Tree)
// time = O(log M * log N) per update/query, space = O(M * N)
class NumMatrix {
    private int[][] tree;  // 2D BIT (1-indexed)
    private int[][] nums;  // original values (for computing delta)
    private int m, n;

    public NumMatrix(int[][] matrix) {
        if (matrix.length == 0 || matrix[0].length == 0) return;
        m = matrix.length;
        n = matrix[0].length;
        tree = new int[m + 1][n + 1];
        nums = new int[m][n];
        for (int i = 0; i < m; i++)
            for (int j = 0; j < n; j++)
                update(i, j, matrix[i][j]);
    }

    // Point update: set matrix[row][col] = val
    public void update(int row, int col, int val) {
        int delta = val - nums[row][col];
        nums[row][col] = val;
        // Propagate delta through 2D BIT (nested lowbit traversal)
        for (int i = row + 1; i <= m; i += i & -i)
            for (int j = col + 1; j <= n; j += j & -j)
                tree[i][j] += delta;
    }

    // Prefix sum query: sum of (0,0) to (row,col)
    private int query(int row, int col) {
        int sum = 0;
        for (int i = row + 1; i > 0; i -= i & -i)
            for (int j = col + 1; j > 0; j -= j & -j)
                sum += tree[i][j];
        return sum;
    }

    // Range sum: rectangle (row1,col1) to (row2,col2)
    public int sumRegion(int row1, int col1, int row2, int col2) {
        // 2D inclusion-exclusion
        return query(row2, col2)
             - query(row1 - 1, col2)
             - query(row2, col1 - 1)
             + query(row1 - 1, col1 - 1);
    }
}
```

#### Alternative: Row-based 1D BIT (simpler but slower)

```java
// Each row has its own 1D BIT
// update = O(log N), sumRegion = O(M * log N)  — slower for large M
class NumMatrix_RowBIT {
    private BIT[] trees;

    public NumMatrix_RowBIT(int[][] matrix) {
        int m = matrix.length, n = matrix[0].length;
        trees = new BIT[m];
        for (int i = 0; i < m; i++) {
            trees[i] = new BIT(n);
            for (int j = 0; j < n; j++)
                trees[i].update(j + 1, matrix[i][j]);
        }
    }

    public void update(int row, int col, int val) {
        int prev = trees[row].query(col + 1) - trees[row].query(col);
        trees[row].update(col + 1, val - prev);
    }

    public int sumRegion(int row1, int col1, int row2, int col2) {
        int sum = 0;
        for (int i = row1; i <= row2; i++)
            sum += trees[i].query(col2 + 1) - trees[i].query(col1);
        return sum;
    }
}
```

#### Approach Comparison for LC 308

| Approach | Update | sumRegion | Space | When to Use |
|----------|--------|-----------|-------|-------------|
| **Brute force** | O(1) | O(M*N) | O(M*N) | Never (TLE) |
| **Row-based 1D BIT** | O(log N) | O(M * log N) | O(M*N) | Few rows, many queries |
| **2D BIT** | O(log M * log N) | O(log M * log N) | O(M*N) | Best balanced performance |
| **2D Segment Tree** | O(log M * log N) | O(log M * log N) | O(M*N) | When need lazy propagation |

#### Related Problems

| Problem | LC # | Relation to LC 308 |
|---------|------|--------------------|
| Range Sum Query - Mutable | 307 | 1D version of same pattern |
| Range Sum Query 2D - Immutable | 304 | 2D prefix sum (no update) |
| Count of Smaller Numbers After Self | 315 | BIT with coordinate compression |
| Count of Range Sum | 327 | BIT/merge sort for range counting |

### Counting Inversions with Coordinate Compression

#### 3) Count of Smaller Numbers After Self — LC 315 ⭐⭐⭐⭐

> Map values to ranks; for each element query how many smaller are already inserted.

```java
// LC 315 - Count of Smaller Numbers After Self
// IDEA: BIT + coordinate compression — process right to left
// time = O(N log N), space = O(N)
public List<Integer> countSmaller(int[] nums) {
    int n = nums.length;
    int[] sorted = nums.clone();
    Arrays.sort(sorted);
    Map<Integer, Integer> rank = new HashMap<>();
    int r = 1;
    for (int v : sorted) if (!rank.containsKey(v)) rank.put(v, r++);
    int[] bit = new int[r];
    Integer[] result = new Integer[n];
    for (int i = n - 1; i >= 0; i--) {
        int pos = rank.get(nums[i]);
        result[i] = query(bit, pos - 1);
        update(bit, pos, r - 1);
    }
    return Arrays.asList(result);
}
private void update(int[] bit, int i, int n) {
    for (; i <= n; i += i & (-i)) bit[i]++;
}
private int query(int[] bit, int i) {
    int sum = 0;
    for (; i > 0; i -= i & (-i)) sum += bit[i];
    return sum;
}
```

#### 4) Reverse Pairs — LC 493

> For each nums[i] (right to left), count previously inserted values < nums[i]; then insert 2*nums[i].

```java
// LC 493 - Reverse Pairs
// IDEA: BIT + coordinate compression — process right to left; query then update
// time = O(N log N), space = O(N)
public int reversePairs(int[] nums) {
    int n = nums.length;
    long[] sorted = new long[2 * n];
    for (int i = 0; i < n; i++) { sorted[i] = nums[i]; sorted[n+i] = 2L * nums[i]; }
    Arrays.sort(sorted);
    Map<Long, Integer> rank = new HashMap<>();
    int r = 1;
    for (long v : sorted) if (!rank.containsKey(v)) rank.put(v, r++);
    int[] bit = new int[r];
    int count = 0;
    for (int i = n - 1; i >= 0; i--) {
        count += queryBIT(bit, rank.get((long)nums[i]) - 1);
        updateBIT(bit, rank.get(2L * nums[i]), r - 1);
    }
    return count;
}
private void updateBIT(int[] b, int i, int n) { for (; i <= n; i += i&-i) b[i]++; }
private int  queryBIT(int[] b, int i)         { int s=0; for(;i>0;i-=i&-i) s+=b[i]; return s; }
```

#### 5) Count of Range Sum — LC 327 — BIT or merge sort

> For each prefix sum, count how many previous prefix sums fall in [prefixSum-upper, prefixSum-lower].

```java
// LC 327 - Count of Range Sum (merge sort approach)
// IDEA: Merge sort — count valid pairs during the merge step
// time = O(N log N), space = O(N)
public int countRangeSum(int[] nums, int lower, int upper) {
    int n = nums.length;
    long[] prefix = new long[n + 1];
    for (int i = 0; i < n; i++) prefix[i+1] = prefix[i] + nums[i];
    return mergeCount(prefix, 0, n + 1, lower, upper);
}
private int mergeCount(long[] arr, int l, int r, int lo, int hi) {
    if (r - l <= 1) return 0;
    int mid = (l + r) / 2;
    int count = mergeCount(arr, l, mid, lo, hi) + mergeCount(arr, mid, r, lo, hi);
    int j = mid, k = mid;
    for (int i = l; i < mid; i++) {
        while (j < r && arr[j] - arr[i] < lo) j++;
        while (k < r && arr[k] - arr[i] <= hi) k++;
        count += k - j;
    }
    long[] tmp = Arrays.copyOfRange(arr, l, r);
    Arrays.sort(tmp);
    System.arraycopy(tmp, 0, arr, l, tmp.length);
    return count;
}
```

#### 6) Global and Local Inversions — LC 775

> Global inversions >= local inversions always; equal iff no non-adjacent inversion exists.

```java
// LC 775 - Global and Local Inversions
// IDEA: Global inversions == local iff no nums[i] > nums[j] for j >= i+2
// time = O(N), space = O(1)
public boolean isIdealPermutation(int[] nums) {
    // global == local iff every element is within 1 of its sorted position
    for (int i = 0; i < nums.length; i++)
        if (Math.abs(nums[i] - i) > 1) return false;
    return true;
}
```

### Counting over VALUES rather than positions

#### 7) Count Number of Teams — LC 1395 — count on both sides of each index


> **Pattern**: BIT indexed by **value** (not array position). Sweep left→right to get
> `leftSmaller[j] / leftLarger[j]`, sweep right→left to get `rightSmaller[j] / rightLarger[j]`,
> then fix each element as the **middle** of the triplet.

**Key Idea**: for a middle element `j`,
`teams(j) = leftSmaller[j] * rightLarger[j] + leftLarger[j] * rightSmaller[j]`
(increasing triplets + decreasing triplets). Summing over `j` counts every triplet exactly once,
because every triplet has exactly one middle.

**Counting trick**: after inserting `t` elements, `larger = t - query(v)`, since
`query(v)` counts everything `<= v`. No second BIT needed for the "larger" side.

```java
// LC 1395 - Count Number of Teams
// IDEA: BIT over VALUES — for each middle soldier, count smaller/larger on the left and right
// time = O(N log M), space = O(M)   M = value range (ratings <= 1e5)
public int numTeams(int[] rating) {
    int MAX = 100000, n = rating.length;
    int[] leftSmaller = new int[n], leftLarger = new int[n];

    // pass 1: left -> right. after j insertions, j elements sit to the left of index j
    BIT left = new BIT(MAX);
    for (int j = 0; j < n; j++) {
        leftSmaller[j] = left.query(rating[j] - 1);          // count(< rating[j])
        leftLarger[j]  = j - left.query(rating[j]);          // j - count(<= rating[j])
        left.update(rating[j], 1);
    }

    // pass 2: right -> left, and combine
    BIT right = new BIT(MAX);
    long res = 0;
    for (int j = n - 1; j >= 0; j--) {
        int rightSmaller = right.query(rating[j] - 1);
        int rightLarger  = (n - 1 - j) - right.query(rating[j]);
        res += (long) leftSmaller[j] * rightLarger           // ascending  i < j < k
             + (long) leftLarger[j]  * rightSmaller;         // descending i > j > k
        right.update(rating[j], 1);
    }
    return (int) res;
}

// BIT helper (1-indexed)
class BIT {
    int[] tree; int n;
    BIT(int n) { this.n = n; tree = new int[n + 1]; }
    void update(int i, int delta) { for (; i <= n; i += i & -i) tree[i] += delta; }
    int query(int i) { int s = 0; for (; i > 0; i -= i & -i) s += tree[i]; return s; }
}
```

```python
# python
# LC 1395 - Count Number of Teams
# IDEA: BIT over VALUES — for each middle soldier, count smaller/larger on the left and right
# time = O(N log M), space = O(M)   M = value range (ratings <= 1e5)
class BIT:
    def __init__(self, n):
        self.n = n
        self.tree = [0] * (n + 1)

    def update(self, i, delta):
        while i <= self.n:
            self.tree[i] += delta
            i += i & (-i)

    def query(self, i):          # prefix sum [1..i]
        s = 0
        while i > 0:
            s += self.tree[i]
            i -= i & (-i)
        return s


def numTeams(rating):
    MAX = 100000
    n = len(rating)
    left_smaller = [0] * n
    left_larger = [0] * n

    # pass 1: left -> right
    left = BIT(MAX)
    for j in range(n):
        left_smaller[j] = left.query(rating[j] - 1)      # count(< rating[j])
        left_larger[j] = j - left.query(rating[j])       # j - count(<= rating[j])
        left.update(rating[j], 1)

    # pass 2: right -> left, and combine
    right = BIT(MAX)
    res = 0
    for j in range(n - 1, -1, -1):
        right_smaller = right.query(rating[j] - 1)
        right_larger = (n - 1 - j) - right.query(rating[j])
        res += left_smaller[j] * right_larger + left_larger[j] * right_smaller
        right.update(rating[j], 1)
    return res
```

> **Variation — sparse / huge values**: if ratings were unbounded, coordinate-compress first
> (same as LC 315 / LC 493 above) and size the BIT by the number of distinct values instead of `MAX`.
>
> **Variation — count 4-tuples / longer chains**: replace the two count arrays with a
> `dp[len][j]` table where `dp[L][j] = Σ dp[L-1][i]` over `i < j, rating[i] < rating[j]`, and use the
> BIT to store **sums of dp values** (not counts of 1s). Same skeleton, different payload.

#### 8) Create Sorted Array through Instructions — LC 1649

> For each instruction, cost = min(count smaller, count larger) already inserted; use BIT.

```java
// LC 1649 - Create Sorted Array through Instructions
// IDEA: BIT on value range — count smaller and greater elements already inserted
// time = O(N log M), space = O(M)  M = max value
public int createSortedArray(int[] instructions) {
    int MOD = 1_000_000_007, n = 100001;
    int[] bit = new int[n + 1];
    long cost = 0;
    for (int i = 0; i < instructions.length; i++) {
        int x = instructions[i];
        int smaller = query(bit, x - 1);
        int larger  = i - query(bit, x);
        cost = (cost + Math.min(smaller, larger)) % MOD;
        update(bit, x, n);
    }
    return (int) cost;
}
private void update(int[] b, int i, int n) { for (; i <= n; i += i&-i) b[i]++; }
private int  query(int[] b, int i)         { int s=0; for(;i>0;i-=i&-i) s+=b[i]; return s; }
```

#### 9) Number of Longest Increasing Subsequences — LC 673 — DP

> Track both length and count at each position; update count when equal-length path found.

```java
// LC 673 - Number of Longest Increasing Subsequences
// IDEA: DP — (len[i], cnt[i]) = (LIS length at i, number of such LIS)
// time = O(N^2), space = O(N)
public int findNumberOfLIS(int[] nums) {
    int n = nums.length, maxLen = 0, result = 0;
    int[] len = new int[n], cnt = new int[n];
    for (int i = 0; i < n; i++) {
        len[i] = cnt[i] = 1;
        for (int j = 0; j < i; j++) {
            if (nums[j] < nums[i]) {
                if (len[j] + 1 > len[i]) { len[i] = len[j]+1; cnt[i] = cnt[j]; }
                else if (len[j] + 1 == len[i]) cnt[i] += cnt[j];
            }
        }
        if (len[i] > maxLen) { maxLen = len[i]; result = cnt[i]; }
        else if (len[i] == maxLen) result += cnt[i];
    }
    return result;
}
```

#### 10) Queue Reconstruction by Height — LC 406 — greedy insertion

> Sort by height DESC (k ASC for ties); insert each person at index k — taller already placed.

```java
// LC 406 - Queue Reconstruction by Height
// IDEA: Sort height DESC (k ASC); insert at index k — taller people already positioned
// time = O(N^2), space = O(N)
public int[][] reconstructQueue(int[][] people) {
    Arrays.sort(people, (a, b) -> a[0] != b[0] ? b[0] - a[0] : a[1] - b[1]);
    List<int[]> res = new ArrayList<>();
    for (int[] p : people) res.add(p[1], p);
    return res.toArray(new int[res.size()][]);
}
```

## Other BIT / Segment-Tree Tagged Problems (Reference)

These carry the BIT/segment-tree tag on LeetCode but are **not** BIT template problems — the
intended solution is a sweep line, heap, or ordered map. Listed so the tag doesn't mislead.

| LC | Title | Diff | Actually solved with |
|----|-------|------|----------------------|
| 218 | The Skyline Problem | Hard | Sweep line + max-heap (or multiset) over active building heights |
| 699 | Falling Squares | Hard | Coordinate-compressed segment tree with lazy max, or `O(N^2)` interval scan |
| 715 | Range Module | Hard | Ordered map of disjoint intervals (`TreeMap`) — see `segment_tree.md` |
| 729 | My Calendar I | Medium | `TreeMap.floorKey/ceilingKey` overlap check |
| 731 | My Calendar II | Medium | Two interval lists, or boundary delta counting |
| 732 | My Calendar III | Hard | Boundary delta counting (`TreeMap` sweep) for max overlap |
| 850 | Rectangle Area II | Hard | Sweep line over x, segment tree for covered y-length |
| 1622 | Fancy Sequence | Hard | Lazy affine transform + modular inverse (no BIT) |
| 1157 | Online Majority Element In Subarray | Hard | Random sampling + binary search on per-value index lists |

> **Interview takeaway**: reach for a BIT only when the query is a **prefix aggregate of an
> invertible operation** with **point updates**. Everything else in this table is a different tool.
