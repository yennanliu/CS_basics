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

## LC Examples

### 2-1) Range Sum Query - Mutable (LC 307) — BIT Point Update + Prefix Sum
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

### 2-2) Count of Smaller Numbers After Self (LC 315) — BIT with Coordinate Compression
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

### 2-3) BIT Generic Template — Point Update + Prefix/Range Query
> Reusable BIT class supporting point updates and prefix-sum queries.

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

### 2-4) Reverse Pairs (LC 493) — BIT with Coordinate Compression
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

### 2-5) Count of Range Sum (LC 327) — BIT / Merge Sort
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

### 2-6) Queue Reconstruction by Height (LC 406) — Greedy Insertion
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

### 2-7) Range Sum Query Mutable — BIT vs Segment Tree Comparison
> BIT for point update + prefix query; Segment Tree for arbitrary range query/update.

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

### 2-7-1) Range Sum Query 2D - Mutable (LC 308) — 2D BIT
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

```
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

### 2-8) Number of Longest Increasing Subsequences (LC 673) — DP
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

### 2-9) Create Sorted Array through Instructions (LC 1649) — BIT
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

### 2-10) Global and Local Inversions (LC 775) — BIT
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