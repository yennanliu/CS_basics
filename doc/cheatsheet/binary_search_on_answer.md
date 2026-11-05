# Binary Search on the Answer

> **Scope** — Binary searching a *range of candidate answers* against a monotone feasibility predicate — the `canFinish` / `isValid` framing, the minimise-maximum vs maximise-minimum decision, the `[max(nums), sum(nums)]` boundary recipe, and counting predicates over a value domain.
> **See also** — *parent sheet*: [binary_search.md](./binary_search.md) — loop invariants, the boundary (lower/upper bound) templates, rotated arrays and 2D search; [binary_search_examples.md](./binary_search_examples.md) — the worked-problem archive for the index-space templates.
> *Neighbouring sheets*: [greedy.md](./greedy.md) — the greedy scan most of these predicates are built from; [bfs.md](./bfs.md) — the traversal used as a predicate in LC 1631 / LC 778; [heap.md](./heap.md) — the k-th-element alternative to value-domain counting.

## LeetCode Problem Lists

- [Binary Search](https://leetcode.com/problem-list/binary-search/)
- [Binary Search on Answer (tag)](https://leetcode.com/tag/binary-search/)

## Overview
**Critical Pattern** - One of the most important and frequently tested binary search applications in FAANG interviews.

### Concept

Instead of searching for a value IN an array, we binary search on a **range of possible answers** and use a validation function to check feasibility.

**When to Use:**
- "Find minimum/maximum value that satisfies..."
- "What's the smallest/largest X such that..."
- "Can we achieve X? What's the optimal X?"
- Problem has **monotonic** property: if X works, then X+1 (or X-1) also works

**Key Recognition Keywords:**
- "Minimize the maximum..."
- "Maximize the minimum..."
- "Find the smallest capacity/speed/divisor..."
- "Can you split/allocate/distribute..."

**Common Problem Patterns:**
- LC 410: Split Array Largest Sum
- LC 1011: Capacity To Ship Packages Within D Days
- LC 875: Koko Eating Bananas
- LC 1283: Find the Smallest Divisor
- LC 1482: Minimum Number of Days to Make m Bouquets
- LC 2226: Maximum Candies Allocated to K Children

---

## Recognition

| Signal in the statement | Constraint cue | Reach for | Not this if |
|---|---|---|---|
| "minimum speed / capacity / divisor / days such that it finishes within …" | answer range up to 10⁹, n ≤ 10⁵ → O(n log range) | [Template 1](#template-1-smallest-feasible-value--lc-875-) smallest feasible value | the answer must hit an exact count, not "at most" → the predicate is not monotone |
| "maximize the minimum distance / sweetness / candies" | same | [Template 2](#template-2-largest-feasible-value--lc-1552-) largest feasible value, upper mid | the items can be reordered freely and one sort plus a scan answers it → [greedy.md](./greedy.md) |
| "split / ship / distribute the array into k **contiguous** groups, minimize the largest sum" | n ≤ 1000, values ≤ 10⁶ | [Template 3](#template-3-split-into-at-most-k-contiguous-groups--lc-410-) `[max, sum]` bounds + greedy fill | groups need not be contiguous (LC 1723) → backtracking or bitmask DP |
| "k-th smallest" in a sorted matrix, a multiplication table, or among pair distances | memory better than O(n²), so no flattening | [Template 4](#template-4-count-at-most-over-a-value-range--lc-378-) count ≤ mid over the value range | k is small next to n → a heap ([heap.md](./heap.md)) |
| "find the target / first position ≥ x" in a sorted array | | not this sheet: index-space bounds → [binary_search.md](./binary_search.md) | |

The constraint ruler behind the cue column lives in [complexity_cheatsheet.md](./complexity_cheatsheet.md).

---

## Problem Categories

**Pattern 1: Minimize Maximum**
- Goal: Find smallest X where some maximum value ≤ X
- Update: `if valid: right = mid` (try smaller)
- Examples: LC 410, 1011, 1482, **2616**
- Key: Sort first (if applicable) + greedy validation
- **Why BS works**: Monotonic property — larger X is always easier to satisfy

**Pattern 2: Maximize Minimum**
- Goal: Find largest X where some minimum value ≥ X
- Update: `if valid: left = mid` (try larger), with the upper mid `mid = (l + r + 1) / 2` so the loop always moves
- Examples: **LC 1231** (Divide Chocolate), LC 1552, LC 2064, LC 2226
- Key: Greedy validation — can we make enough pieces/groups with each ≥ target?
- **Why BS works**: Monotonic property — smaller X is always easier to satisfy
- **Counterpart to LC 410**: LC 1231 is "maximize min sweetness" vs LC 410 "minimize max sum"

**Pattern 3: Count-Based Validation**
- Check: "Can we do it in at most K groups/days/operations?"
- Greedy approach: Try to fit as much as possible in each group
- Examples: LC 410 (subarrays), LC 1011 (days)

**Pattern 4: Sum-Based Validation**
- Check: "Is the sum/total within bounds?"
- Accumulate values and check threshold
- Examples: LC 1283 (division sum), LC 875 (hours)

---

## Templates & Algorithms

### Monotonic Predicate — the Conceptual Foundation
The real power of binary search: if you can define a predicate `P(x)` such that all `x` satisfying `P` form a contiguous range, binary search finds the boundary in O(log n).

```text
P(x) = False, False, ..., False, True, True, ..., True
                                 ^
                          find this boundary
```

Examples of monotonic predicates:
- `canFinish(speed)` — can Koko eat all bananas in H hours at speed ≥ k? (LC 875)
- `canShip(capacity)` — can we ship all packages in D days with capacity ≥ c? (LC 1011)
- `isEnough(mid)` — are there at least k pairs with distance ≤ mid? (LC 719)

**The two loops.** "Find the minimum x satisfying P" is the first `True` of `False … True`:
[Template 1](#template-1-smallest-feasible-value--lc-875-), lower mid. "Find the maximum x
satisfying P" is the last `True` of `True … False`: [Template 2](#template-2-largest-feasible-value--lc-1552-),
upper mid. The decision matrix below puts the two side by side.

### Decision Matrix: Minimize vs Maximize ⭐⭐⭐⭐⭐

| Goal | Example Problems | Valid Condition | Update Rule | Final Answer |
|------|------------------|----------------|-------------|--------------|
| **Minimize maximum** | LC 410, 1011, 2616 | If mid works | `right = mid` (try smaller) | `left` (smallest valid) |
| **Maximize minimum** | LC 1231, 1552, 2226 | If mid works | `left = mid` (try larger), upper mid | `left` (largest valid) |

**Mnemonic:**
- **Minimize**: When valid, go **left** (smaller values) → Find smallest working value
- **Maximize**: When valid, go **right** (larger values) → Find largest working value

**Critical Template Difference:**

```java
// MINIMIZE pattern (LC 410, 1011)
while (left < right) {
    int mid = left + (right - left) / 2;  // Standard mid
    if (isValid(mid)) right = mid;        // Try smaller
    else left = mid + 1;
}
return left;

// MAXIMIZE pattern (LC 1231, 1552)
while (left < right) {
    int mid = left + (right - left + 1) / 2;  // CRITICAL: +1 to avoid infinite loop!
    if (isValid(mid)) left = mid;             // Try larger
    else right = mid - 1;
}
return left;
```

> **Why `+1` in the Maximize template?**
> `// +1 avoids infinite loop: when left+1==right, (left+right)/2 == left, so left = mid never moves`
> Without `+1`, when `left` and `right` are adjacent (`left + 1 == right`), `mid` computes to `left`.
> If `isValid(mid)` is true, we set `left = mid = left` — no progress, infinite loop.
> Adding `+1` biases `mid` upward so `mid == right`, guaranteeing the loop always shrinks.

---

### Template 1: Smallest Feasible Value — LC 875 ⭐⭐⭐⭐⭐

**Recognise** — "the **minimum** speed / capacity / divisor / number of days such that the job
still finishes", and checking one candidate is easy while computing the optimum directly is
not. Answer range up to 10⁹ with n ≤ 10⁴–10⁵ → O(n log range) is expected.

**Key point** — `feasible(x)` reads `False … False True … True` over `[left, right]`, and the
answer always stays inside `[left, right]`: a feasible `mid` keeps itself (`right = mid`), an
infeasible one is discarded (`left = mid + 1`). The lower mid makes `right = mid` shrink the range
every time, so the loop ends with `left == right == the first True`.

```python
# python
# LC 875 - Koko Eating Bananas
# IDEA: binary search the speed; can_finish(speed) is monotone, so find its first True
# time = O(n log M), space = O(1)   M = max(piles)
def minEatingSpeed(piles, h):
    def can_finish(speed):
        hours = 0
        for pile in piles:
            hours += (pile + speed - 1) // speed   # ceil(pile / speed) without floats
        return hours <= h

    left, right = 1, max(piles)          # speed max(piles) finishes in len(piles) <= h hours
    while left < right:
        mid = left + (right - left) // 2  # lower mid: right = mid always shrinks the range
        if can_finish(mid):
            right = mid                  # mid works -> the answer is mid or smaller
        else:
            left = mid + 1               # mid fails -> the answer is above mid
    return left
```

```java
// java
// LC 875 - Koko Eating Bananas
// IDEA: binary search the speed; canFinish(speed) is monotone, so find its first true
// time = O(n log M), space = O(1)   M = max(piles)
public int minEatingSpeed(int[] piles, int h) {
    int left = 1;
    int right = 0;
    for (int pile : piles) {
        right = Math.max(right, pile);        // speed max(piles) always finishes
    }
    while (left < right) {
        int mid = left + (right - left) / 2;  // lower mid; never overflows
        if (canFinish(piles, h, mid)) {
            right = mid;                      // mid works -> answer is mid or smaller
        } else {
            left = mid + 1;                   // mid fails -> answer is above mid
        }
    }
    return left;
}

private boolean canFinish(int[] piles, int h, int speed) {
    long hours = 0;                           // long: 10^4 piles * 10^9 hours overflows int
    for (int pile : piles) {
        hours += (pile + (long) speed - 1) / speed;   // ceil without floats
    }
    return hours <= h;
}
```

**Complexity**
- **Time O(n log M)**: the `while left < right` loop halves `[1, M]`, so it runs O(log M) times,
  and each pass calls `can_finish`, whose `for pile in piles` loop is O(n).
- **Space O(1)**: only `left`, `right`, `mid` and the running `hours`.

**Dry run**

```text
piles = [3,6,7,11], h = 8          hours(speed) = sum of ceil(pile / speed)
left=1  right=11  mid=6   hours=1+1+2+2=6  <= 8  -> right=6
left=1  right=6   mid=3   hours=1+2+3+4=10 >  8  -> left=4
left=4  right=6   mid=5   hours=1+2+2+3=8  <= 8  -> right=5
left=4  right=5   mid=4   hours=1+2+2+3=8  <= 8  -> right=4
left=4  right=4   stop -> 4
```

**Traps**
- `right = mid - 1` on a feasible `mid` can step past the answer and return an unchecked value:
  `piles = [30,11,23,4,20]`, `h = 6` returns 22 instead of 23. Keep `right = mid`.
- `left = 0` divides by zero once `mid` reaches 0: `piles = [1]`, `h = 1` probes speed 0.
- An `int` hour count overflows in Java: `piles = [10^9, 10^9]`, `h = 3` sums 2·10⁹ hours at speed 1.
  Use `long`.
- Mixing styles: `while left <= right` with `right = mid` never ends once `left == right` is
  feasible. Either `left < right` + `right = mid`, or `left <= right` + `right = mid - 1` + an `ans`
  variable — never half of each.
- `math.ceil(pile / speed)` goes through a float; `(pile + speed - 1) // speed` is exact.

**Not this if** — the question is the **largest** value that still works (maximize the minimum):
the `True` block is on the left, so use [Template 2](#template-2-largest-feasible-value--lc-1552-)
and its upper mid. And if more of `x` can make the job *fail* again (an exact count), the
predicate is not monotone and no binary search applies.

**Drill** — LC 875 ⭐ · LC 1283 *(sum of ceil divisions)* · LC 1011 *(greedy day count; [Template 3](#template-3-split-into-at-most-k-contiguous-groups--lc-410-) bounds)* · LC 1482 *(predicate counts runs of k)* · LC 2616 *(sort, then greedy pairing)*

---

### Template 2: Largest Feasible Value — LC 1552 ⭐⭐⭐⭐⭐

**Recognise** — "**maximize the minimum** distance / sweetness / candies per child", or "the
largest x such that it is still possible". The feasible block is on the *left* now: if a gap of
x can be kept, any smaller gap can too. Same sizes as Template 1 → O(n log range).

**Key point** — `feasible(x)` reads `True … True False … False`, and the answer stays inside
`[left, right]`: a feasible `mid` keeps itself (`left = mid`), an infeasible one is discarded
(`right = mid - 1`). Only the **upper** mid, `left + (right - left + 1) // 2`, makes `left = mid`
move when `left + 1 == right`.

```python
# python
# LC 1552 - Magnetic Force Between Two Balls
# IDEA: binary search the minimum gap; can_place(gap) is monotone (smaller is easier), find its last True
# time = O(n log n + n log R), space = O(n)   R = max(position) - min(position)
def maxDistance(position, m):
    position = sorted(position)          # the greedy placement needs left-to-right order

    def can_place(gap):
        placed = 1                       # the first ball always goes at position[0]
        last = position[0]
        for p in position[1:]:
            if p - last >= gap:          # far enough from the previous ball -> place one
                placed += 1
                last = p
        return placed >= m

    left, right = 1, position[-1] - position[0]   # positions are distinct, so gap 1 always works
    while left < right:
        mid = left + (right - left + 1) // 2      # upper mid: left = mid always shrinks the range
        if can_place(mid):
            left = mid                   # mid works -> the answer is mid or larger
        else:
            right = mid - 1              # mid fails -> the answer is below mid
    return left
```

```java
// java
// LC 1552 - Magnetic Force Between Two Balls
// IDEA: binary search the minimum gap with the upper mid; find the last gap canPlace accepts
// time = O(n log n + n log R), space = O(1) beyond the sort   R = max(position) - min(position)
public int maxDistance(int[] position, int m) {
    Arrays.sort(position);
    int left = 1;
    int right = position[position.length - 1] - position[0];
    while (left < right) {
        int mid = left + (right - left + 1) / 2;   // upper mid: left = mid always moves
        if (canPlace(position, m, mid)) {
            left = mid;                            // mid works -> answer is mid or larger
        } else {
            right = mid - 1;                       // mid fails -> answer is below mid
        }
    }
    return left;
}

private boolean canPlace(int[] position, int m, int gap) {
    int placed = 1;                                // first ball at position[0]
    int last = position[0];
    for (int i = 1; i < position.length; i++) {
        if (position[i] - last >= gap) {           // greedy: place as early as allowed
            placed++;
            last = position[i];
        }
    }
    return placed >= m;
}
```

**Complexity**
- **Time O(n log n + n log R)**: the sort is O(n log n) once; then the `while left < right` loop
  runs O(log R) times and each `can_place` is one O(n) pass.
- **Space O(n)** in Python for the sorted copy; O(1) extra in Java, which sorts in place.

**Dry run**

```text
position = [1,2,3,4,7], m = 3
left=1  right=6  mid=4   place 1, 7          -> 2 balls < 3  -> right=3
left=1  right=3  mid=2   place 1, 3, 7       -> 3 balls      -> left=2
left=2  right=3  mid=3   place 1, 4, 7       -> 3 balls      -> left=3
left=3  right=3  stop -> 3
```

**Traps**
- The lower mid with `left = mid` loops forever once the range is two wide: `position = [1,2,3]`,
  `m = 2` reaches `left=1, right=2`, `mid=1` is feasible, and `left` stays 1.
- Not sorting: `position = [5,4,3,2,1,1000000000]`, `m = 2` returns 999999995 instead of 999999999,
  because the greedy anchors the first ball at 5.
- Placing greedily as **early** as possible is what makes `can_place` exact: placing a ball later
  than necessary can only shrink the room left for the rest.
- The closed form (`while left <= right`, `ans = mid; left = mid + 1`) is also correct, and is what
  LC 1231 uses below. It needs no upper mid — but then it needs `ans`.

**Not this if** — the answer is the **smallest** value that works (minimize the maximum) →
[Template 1](#template-1-smallest-feasible-value--lc-875-), lower mid. If the minimum gap is
asked over a fixed placement (no choice of where the balls go), it is a single scan, not a search.

**Drill** — LC 1552 ⭐ · LC 2226 *(candies: `sum(c // x) >= k`)* · LC 2517 *(same gap greedy on prices)* · LC 1231 *(the mirror of LC 410: cut when the piece reaches x)* · LC 1802 *(feasibility is an arithmetic sum)*

---

### Template 3: Split Into at Most K Contiguous Groups — LC 410 ⭐⭐⭐⭐⭐

**Recognise** — "split / ship / distribute the array into k **contiguous** subarrays, days or
workers, and **minimize the largest** group sum". n ≤ 1000 and values ≤ 10⁶ (LC 410), or
n ≤ 5·10⁴ (LC 1011) → O(n log sum).

**Key point** — the answer lies in `[max(nums), sum(nums)]`: below `max(nums)` the largest element
fits in no group, and `sum(nums)` is one group holding everything. For a cap `x`, filling each group
until the next element would overflow uses the **fewest** groups any split can, so "fits in ≤ k
groups" is monotone in `x` and Template 1's loop finds the smallest cap that fits.

```python
# python
# LC 410 - Split Array Largest Sum
# IDEA: binary search the cap in [max(nums), sum(nums)]; greedy fill counts the fewest groups for a cap
# time = O(n log S), space = O(1)   S = sum(nums)
def splitArray(nums, k):
    def can_split(cap):
        groups = 1
        current = 0
        for num in nums:
            if current + num <= cap:     # num still fits in the current group
                current += num
            else:                        # close the group, start a new one with num
                groups += 1
                current = num
                if groups > k:           # early exit: already too many groups
                    return False
        return True

    left = max(nums)                     # a group must hold the largest element on its own
    right = sum(nums)                    # one group holding everything always works
    while left < right:
        mid = left + (right - left) // 2
        if can_split(mid):
            right = mid                  # fits in <= k groups -> try a smaller cap
        else:
            left = mid + 1               # needs > k groups -> the cap must grow
    return left
```

```java
// java
// LC 410 - Split Array Largest Sum
// IDEA: binary search the cap in [max(nums), sum(nums)]; greedy fill counts the fewest groups for a cap
// time = O(n log S), space = O(1)   S = sum(nums)
public int splitArray(int[] nums, int k) {
    int left = 0;
    int right = 0;
    for (int num : nums) {
        left = Math.max(left, num);   // lower bound: a group must hold the largest element
        right += num;                 // upper bound: one group holds everything
    }
    while (left < right) {
        int mid = left + (right - left) / 2;
        if (canSplit(nums, k, mid)) {
            right = mid;              // fits -> try a smaller cap
        } else {
            left = mid + 1;           // too many groups -> the cap must grow
        }
    }
    return left;
}

private boolean canSplit(int[] nums, int k, int cap) {
    int groups = 1;
    int current = 0;
    for (int num : nums) {
        if (current + num <= cap) {
            current += num;
        } else {
            groups++;
            current = num;
            if (groups > k) {
                return false;
            }
        }
    }
    return true;
}
```

**Complexity**
- **Time O(n log S)**: the `while left < right` loop halves `[max, sum]`, so O(log S) passes, and
  each `can_split` is one O(n) scan.
- **Space O(1)**: the greedy keeps only `groups` and `current`.

**Bounds by problem** — the same loop, with the range set by what one candidate means:

| LC | Problem | What is minimized | `left` | `right` |
|----|---------|-------------------|--------|---------|
| **410** | Split Array Largest Sum | largest subarray sum | `max(nums)` | `sum(nums)` |
| **1011** | Capacity To Ship Packages | ship capacity | `max(weights)` | `sum(weights)` |
| **875** | Koko Eating Bananas | eating speed | `1` | `max(piles)` |
| **1283** | Find the Smallest Divisor | divisor | `1` | `max(nums)` |
| **1482** | Min Days to Make m Bouquets | days | `min(bloomDay)` | `max(bloomDay)` |
| **2064** | Minimized Maximum of Products Distributed | max per store | `1` | `max(quantities)` |

**Dry run**

```text
nums = [7,2,5,10,8], k = 2           search [10, 32]
left=10  right=32  mid=21   [7,2,5]=14 [10,8]=18        2 groups -> right=21
left=10  right=21  mid=15   [7,2,5]=14 [10] [8]         3 groups -> left=16
left=16  right=21  mid=18   [7,2,5]=14 [10,8]=18        2 groups -> right=18
left=16  right=18  mid=17   [7,2,5]=14 [10] [8]         3 groups -> left=18
left=18  right=18  stop -> 18
```

**Traps**
- `left = min(nums)` or `1` lets `can_split` open a group that already exceeds the cap, and it then
  under-counts: `nums = [1,4,4]`, `k = 3` returns 1 instead of 4. Start at `max(nums)`.
- `current + num < cap` instead of `<=` refuses a group that exactly hits the cap: `[7,2,5,10,8]`,
  `k = 2` returns 19 instead of 18.
- `can_split` answers "**at most** k groups", not "exactly k": any split into fewer groups can be cut
  further without raising the maximum, as long as `k <= n`.
- `right` can exceed `int` in a variant with large values: `sum` of 10⁵ values of 10⁹. Use `long`.

**Not this if** — the groups need **not** be contiguous (assign jobs to k workers, LC 1723): the greedy
fill no longer counts the fewest groups, so it is backtracking or bitmask DP. If the cost is the
**sum** of per-group costs rather than the largest one, it is partition DP ([dp.md](./dp.md)).

**Drill** — LC 410 ⭐ · LC 1011 *(same problem, packages and days)* · LC 2064 *(groups are per store: `ceil(q / x)`)* · LC 1231 *(maximize the minimum: [Template 2](#template-2-largest-feasible-value--lc-1552-))*

---

### Template 4: Count-at-Most Over a Value Range — LC 378 ⭐⭐⭐⭐⭐

**Recognise** — "the **k-th smallest**" of something you cannot afford to list: an `n × n` matrix
sorted by row and column, a multiplication table, all pair distances. "Memory better than O(n²)"
or values up to 10⁹ with a cheap way to *count* how many are ≤ x.

**Key point** — `count(x)` = how many values are `≤ x` never decreases as `x` grows, and it only
steps up at values that exist. So the smallest `x` with `count(x) ≥ k` is the k-th smallest **and**
is always a real element: search the value range, not the indices, with Template 1's loop.

```python
# python
# LC 378 - Kth Smallest Element in a Sorted Matrix
# IDEA: binary search the value range; first x with count(<= x) >= k is the answer
# time = O(n log(max - min)), space = O(1)
def kthSmallest(matrix, k):
    n = len(matrix)

    def count_at_most(x):
        # staircase from the bottom-left corner: each step drops a row or a column
        row, col = n - 1, 0
        count = 0
        while row >= 0 and col < n:
            if matrix[row][col] <= x:
                count += row + 1         # this cell and every cell above it are <= x
                col += 1
            else:
                row -= 1
        return count

    left, right = matrix[0][0], matrix[n - 1][n - 1]
    while left < right:
        mid = left + (right - left) // 2
        if count_at_most(mid) >= k:
            right = mid                  # at least k values <= mid -> answer is mid or smaller
        else:
            left = mid + 1               # fewer than k -> answer is above mid
    return left
```

```java
// java
// LC 378 - Kth Smallest Element in a Sorted Matrix
// IDEA: binary search the value range; first x with countAtMost(x) >= k is the answer
// time = O(n log(max - min)), space = O(1)
public int kthSmallest(int[][] matrix, int k) {
    int n = matrix.length;
    int left = matrix[0][0];
    int right = matrix[n - 1][n - 1];
    while (left < right) {
        int mid = left + (right - left) / 2;   // floors even for negatives; (left + right) / 2 does not
        if (countAtMost(matrix, mid) >= k) {
            right = mid;
        } else {
            left = mid + 1;
        }
    }
    return left;
}

private int countAtMost(int[][] matrix, int x) {
    int n = matrix.length;
    int row = n - 1;          // start bottom-left
    int col = 0;
    int count = 0;
    while (row >= 0 && col < n) {
        if (matrix[row][col] <= x) {
            count += row + 1;  // the whole column above is <= x too
            col++;
        } else {
            row--;
        }
    }
    return count;
}
```

**Complexity**
- **Time O(n log(max − min))**: the `while left < right` loop halves the value range, and each
  `count_at_most` is one staircase walk, which moves up or right at most 2n times.
- **Space O(1)**: the walk keeps `row`, `col` and `count`, never a copy of the matrix.

**Dry run**

```text
matrix = [[1,5,9],[10,11,13],[12,13,15]], k = 8
left=1   right=15  mid=8    values <= 8: 1,5                      count=2 < 8  -> left=9
left=9   right=15  mid=12   1,5,9,10,11,12                         count=6 < 8  -> left=13
left=13  right=15  mid=14   1,5,9,10,11,12,13,13                   count=8 >= 8 -> right=14
left=13  right=14  mid=13   same eight                             count=8 >= 8 -> right=13
left=13  right=13  stop -> 13          (mid=14 also had count 8, but 14 is not in the matrix)
```

**Traps**
- Returning `mid` as soon as `count(mid) == k`: on the trace above `mid=14` has count 8 and is not in
  the matrix. Keep narrowing to the first such value.
- `(left + right) / 2` in Java rounds toward zero for negatives: `[[-5,-4],[-4,-3]]`, `k = 2` reaches
  `left=-5, right=-4`, gets `mid=-4`, sets `right=-4` and never moves. Use `left + (right - left) / 2`.
- Starting the staircase top-left: both neighbours are larger, so a step cannot decide its direction.
  Start bottom-left (or top-right).

**Not this if** — k is small next to n²: pop a min-heap `k` times, O(k log n) ([heap.md](./heap.md)).
And when the values are unsorted but bounded, the count still works but the test changes — LC 287
below binary searches `[1, n]` with the pigeonhole test `count(mid) > mid`.

**Drill** — LC 378 ⭐ · LC 287 *(count > mid means the duplicate is ≤ mid)* · LC 668 *(count = Σ min(x // i, n))* · LC 719 *(sort + two-pointer count of pairs ≤ x)* · LC 786 *(fractions: search a real range)*

**Similar problems**

| LC | Problem | Relation |
|----|---------|----------|
| **668** | Kth Smallest Number in Multiplication Table | `count(x) = Σ min(x // i, n)` |
| **719** | Find K-th Smallest Pair Distance | count pairs with distance ≤ x by two pointers |
| **786** | K-th Smallest Prime Fraction | binary search a real value + count |
| **373** | Find K Pairs with Smallest Sums | heap variant of the same k-smallest question |
| **240** | Search a 2D Matrix II | the same staircase walk, searching instead of counting |
| **4**   | Median of Two Sorted Arrays | k-th smallest by a partition binary search |

---

### Why Binary Search Works for "Minimize the Maximum" ⭐⭐⭐⭐⭐

This is the **theoretical foundation** that makes binary search applicable to optimization problems.

#### The Monotonic Property (Key Insight)

Binary search requires a **monotonic** (sorted) property. For "Minimize the Maximum" problems, this property exists in the **feasibility function**:

```text
If we can achieve the goal with maximum value = X,
then we can ALWAYS achieve it with maximum value = X + 1 (or any larger value).
```

This creates a **monotonic feasibility curve**:

```text
Answer Space:  0   1   2   3   4   5   6   7   8   9   ...
               |---|---|---|---|---|---|---|---|---|---|
Feasible?      ✗   ✗   ✗   ✗   ✓   ✓   ✓   ✓   ✓   ✓   ...
                           ↑
                    Decision Boundary (Answer = 4)

The feasibility function is monotonic:
- All values LEFT of boundary: INFEASIBLE (✗)
- All values RIGHT of boundary: FEASIBLE (✓)
- We want to find the LEFTMOST ✓ (minimum feasible value)
```

#### Why This Enables Binary Search

**Standard binary search** finds a target in a sorted array.
**Binary search on answer** finds the boundary in a sorted feasibility function.

| Concept | Standard Binary Search | Binary Search on Answer |
|---------|----------------------|------------------------|
| **Search space** | Sorted array of values | Range of possible answers |
| **Monotonic property** | Values are sorted | Feasibility is monotonic |
| **Goal** | Find exact target | Find boundary (first ✓) |
| **Check** | `nums[mid] == target?` | `isValid(mid)?` |

#### Mathematical Proof

**Theorem:** If `isValid(x)` has the monotonic property:
- `isValid(x) = true` ⟹ `isValid(x + 1) = true`

Then binary search correctly finds the minimum valid `x`.

**Proof:**
1. The answer space `[left, right]` can be partitioned into:
   - `[left, answer-1]`: all invalid
   - `[answer, right]`: all valid
2. Binary search finds this partition point in O(log n) time
3. Each iteration halves the search space while preserving the invariant

#### Visual Example — the Feasibility Curve

```text
Problem: Find p=2 pairs with minimum maximum difference
Array after sorting: [1, 1, 2, 3, 7, 10]

Answer space (max diff): 0  1  2  3  4  5  6  7  8  9
Can form 2 pairs?        ✗  ✓  ✓  ✓  ✓  ✓  ✓  ✓  ✓  ✓
                            ↑
                     Answer = 1 (minimum max diff)

Why monotonic?
- If max_diff = 1 works: pairs (1,1)=0, (2,3)=1 → both ≤ 1 ✓
- If max_diff = 2 works: same pairs still work, more options available ✓
- If max_diff = 0 fails: only (1,1)=0 works, can't form 2 pairs ✗

Larger max_diff → More pairs possible → Easier to satisfy constraint
```

#### Why Not Just Iterate? (Complexity Analysis)

| Approach | Time Complexity | Explanation |
|----------|----------------|-------------|
| **Linear search** | O(range × n) | Check every possible answer |
| **Binary search** | O(log(range) × n) | Halve search space each time |

For LC 2616: range = 10⁹, n = 10⁵
- Linear: 10⁹ × 10⁵ = 10¹⁴ operations ❌ TLE
- Binary: log(10⁹) × 10⁵ ≈ 30 × 10⁵ = 3×10⁶ operations ✓

#### The Three Requirements for Binary Search on Answer

```text
✅ Requirement 1: BOUNDED answer space
   - Must have clear [min, max] range
   - Example: [0, max_element - min_element]

✅ Requirement 2: MONOTONIC feasibility
   - If X works, X+1 must also work (for minimize)
   - If X works, X-1 must also work (for maximize)

✅ Requirement 3: EFFICIENT validation
   - Can check if answer X is valid in O(n) or O(n log n)
   - Usually uses greedy approach
```

---

## LC Examples

Each card below reduces to one of the four templates above, grouped in template order.

### Smallest Feasible Value Examples

#### Find the Smallest Divisor Given a Threshold — LC 1283

> **Key point** — a bigger divisor never raises `sum(ceil(num / d))`, so "the sum fits the
> threshold" is `False … True` over `d`. Search `[1, max(nums)]`: at `max(nums)` every term is 1,
> and the statement guarantees `threshold >= len(nums)`.

**Reduces to**: [Template 1](#template-1-smallest-feasible-value--lc-875-) — the LC 875 loop with a divisor in place of a speed; the predicate is the same sum of ceiling divisions.

```python
# python
# LC 1283 - Find the Smallest Divisor Given a Threshold
# IDEA: first divisor in [1, max(nums)] whose sum of ceil(num / d) is <= threshold
# time = O(n log M), space = O(1)   M = max(nums)
def smallestDivisor(nums, threshold):
    def fits(divisor):
        total = 0
        for num in nums:
            total += (num + divisor - 1) // divisor   # ceil without floats
        return total <= threshold

    left, right = 1, max(nums)
    while left < right:
        mid = left + (right - left) // 2
        if fits(mid):
            right = mid
        else:
            left = mid + 1
    return left
```

```java
// java
// LC 1283 - Find the Smallest Divisor Given a Threshold
// IDEA: first divisor whose sum of ceil(num / d) is <= threshold
// time = O(n log M), space = O(1)   M = max(nums)
public int smallestDivisor(int[] nums, int threshold) {
    int left = 1, right = 0;
    for (int num : nums) right = Math.max(right, num);
    while (left < right) {
        int mid = left + (right - left) / 2;
        if (divisionSum(nums, mid) <= threshold) right = mid;
        else left = mid + 1;
    }
    return left;
}

private long divisionSum(int[] nums, int divisor) {
    long total = 0;
    for (int num : nums) total += (num + divisor - 1) / divisor;
    return total;
}
```

**Traps**
- `left = 0` divides by zero on the first small probe: start at 1.
- `num // divisor` (floor) undercounts: `nums = [1,2,5,9]`, `threshold = 6` returns 3 instead of 5.

#### Minimum Number of Days to Make m Bouquets — LC 1482

> **Key point** — by day `d` every flower with `bloomDay <= d` is open, and more days only open
> more. So "m bouquets of k **adjacent** flowers can be cut by day d" is monotone; count bouquets
> with one left-to-right run counter.

**Reduces to**: [Template 1](#template-1-smallest-feasible-value--lc-875-) — over days `[min(bloomDay), max(bloomDay)]`, with an impossibility check first (`m * k > n`).

```python
# python
# LC 1482 - Minimum Number of Days to Make m Bouquets
# IDEA: first day d where runs of k open flowers make at least m bouquets
# time = O(n log D), space = O(1)   D = max(bloomDay) - min(bloomDay)
def minDays(bloomDay, m, k):
    if m * k > len(bloomDay):
        return -1                            # not enough flowers on any day

    def can_make(day):
        bouquets = 0
        run = 0                              # adjacent open flowers so far
        for bloom in bloomDay:
            if bloom <= day:
                run += 1
                if run == k:
                    bouquets += 1
                    run = 0                  # these k flowers are used
            else:
                run = 0                      # a closed flower breaks adjacency
        return bouquets >= m

    left, right = min(bloomDay), max(bloomDay)
    while left < right:
        mid = left + (right - left) // 2
        if can_make(mid):
            right = mid
        else:
            left = mid + 1
    return left
```

```java
// java
// LC 1482 - Minimum Number of Days to Make m Bouquets
// IDEA: first day where runs of k open flowers make at least m bouquets
// time = O(n log D), space = O(1)
public int minDays(int[] bloomDay, int m, int k) {
    if ((long) m * k > bloomDay.length) return -1;   // m * k overflows int at 10^6 * 10^6
    int left = Integer.MAX_VALUE, right = 0;
    for (int day : bloomDay) {
        left = Math.min(left, day);
        right = Math.max(right, day);
    }
    while (left < right) {
        int mid = left + (right - left) / 2;
        if (canMake(bloomDay, m, k, mid)) right = mid;
        else left = mid + 1;
    }
    return left;
}

private boolean canMake(int[] bloomDay, int m, int k, int day) {
    int bouquets = 0, run = 0;
    for (int bloom : bloomDay) {
        if (bloom <= day) {
            run++;
            if (run == k) {
                bouquets++;
                run = 0;
            }
        } else {
            run = 0;
        }
    }
    return bouquets >= m;
}
```

**Traps**
- Forgetting to reset `run` on a closed flower counts non-adjacent flowers:
  `[7,7,7,7,12,7,7]`, `m = 2`, `k = 3` returns 7 instead of 12.
- `m * k` overflows `int` in Java (both up to 10⁶): compare as `long`.

#### Kth Missing Positive Number — LC 1539

> **Key point** — in a strictly increasing array of positives, `arr[i] − (i + 1)` numbers are
> missing before index `i`, and that count never decreases. The first index where it reaches `k`
> has exactly `left` present numbers before the answer, so the answer is `left + k`.

**Reduces to**: [Template 1](#template-1-smallest-feasible-value--lc-875-) — the first `True` of `missing(i) >= k`, searched over indices `[0, n]` rather than a value range.

```python
# python
# LC 1539 - Kth Missing Positive Number
# IDEA: first index i with arr[i] - (i + 1) >= k; the answer is i + k
# time = O(log n), space = O(1)
def findKthPositive(arr, k):
    left, right = 0, len(arr)                # right = n: the answer can fall past the last element
    while left < right:
        mid = left + (right - left) // 2
        if arr[mid] - (mid + 1) < k:
            left = mid + 1                   # fewer than k missing before mid
        else:
            right = mid
    return left + k                          # left numbers of arr are below the answer
```

```java
// java
// LC 1539 - Kth Missing Positive Number
// IDEA: first index with arr[i] - (i + 1) >= k; the answer is that index + k
// time = O(log n), space = O(1)
public int findKthPositive(int[] arr, int k) {
    int left = 0, right = arr.length;
    while (left < right) {
        int mid = left + (right - left) / 2;
        if (arr[mid] - (mid + 1) < k) left = mid + 1;
        else right = mid;
    }
    return left + k;
}
```

**Traps**
- `right = n − 1` misses answers past the array: `arr = [1,2,3,4]`, `k = 2` must return 6.

**Similar** — LC 1060 *(missing count measured from `nums[0]`: [binary_search_examples.md](./binary_search_examples.md#missing-element-in-sorted-array--lc-1060))*

#### Minimize the Maximum Difference of Pairs — LC 2616 ⭐⭐⭐⭐⭐

> **Key point** — after sorting, an optimal set of pairs uses adjacent elements only. For a cap
> `x`, a left-to-right greedy that takes `(i, i + 1)` whenever the gap is `<= x` (and then skips
> `i + 1`) forms the most pairs possible, so "p pairs fit under x" is monotone.

**Reduces to**: [Template 1](#template-1-smallest-feasible-value--lc-875-) — sort first, search `[0, max − min]`, and the predicate is the greedy pair count.

```python
# python
# LC 2616 - Minimize the Maximum Difference of Pairs
# IDEA: sort; first cap x in [0, max - min] for which greedy adjacent pairing finds p pairs
# time = O(n log n + n log R), space = O(1) besides the sort   R = max - min
def minimizeMax(nums, p):
    nums.sort()

    def enough_pairs(cap):
        count = 0
        i = 0
        while i < len(nums) - 1:
            if nums[i + 1] - nums[i] <= cap:
                count += 1
                i += 2                       # both elements are used
            else:
                i += 1
        return count >= p

    left, right = 0, nums[-1] - nums[0]
    while left < right:
        mid = left + (right - left) // 2
        if enough_pairs(mid):
            right = mid
        else:
            left = mid + 1
    return left
```

```java
// java
// LC 2616 - Minimize the Maximum Difference of Pairs
// IDEA: sort; first cap for which greedy adjacent pairing finds p pairs
// time = O(n log n + n log R), space = O(1) besides the sort
public int minimizeMax(int[] nums, int p) {
    Arrays.sort(nums);
    int left = 0, right = nums[nums.length - 1] - nums[0];
    while (left < right) {
        int mid = left + (right - left) / 2;
        if (enoughPairs(nums, p, mid)) right = mid;
        else left = mid + 1;
    }
    return left;
}

private boolean enoughPairs(int[] nums, int p, int cap) {
    int count = 0;
    int i = 0;
    while (i < nums.length - 1) {
        if (nums[i + 1] - nums[i] <= cap) {
            count++;
            i += 2;                      // both elements are used
        } else {
            i += 1;
        }
    }
    return count >= p;
}
```

```text
nums = [10,1,2,7,1,3], p = 2  -> sorted [1,1,2,3,7,10]
left=0  right=9  mid=4   pairs (1,1) (2,3)        2 >= 2 -> right=4
left=0  right=4  mid=2   pairs (1,1) (2,3)        2 >= 2 -> right=2
left=0  right=2  mid=1   pairs (1,1) (2,3)        2 >= 2 -> right=1
left=0  right=1  mid=0   pairs (1,1)              1 <  2 -> left=1
left=1  right=1  stop -> 1
```

**Traps**
- Not skipping `i + 1` after a pair reuses an element: `[1,2,3,10]`, `p = 2` counts `(1,2)` and
  `(2,3)` under cap 1 and returns 1 instead of 7.
- Picking the globally smallest gaps first (a heap) is a matching problem, not a greedy one; the
  binary search is what makes a one-pass greedy enough.
- `p = 0` needs no special case: `right` may be 0 and the loop never runs.

#### Path With Minimum Effort — LC 1631 ⭐⭐⭐⭐

> **Key point** — `can_reach(limit)` = "a path from top-left to bottom-right exists using only
> steps whose height difference is `<= limit`". A bigger limit only unlocks more edges, so it is
> monotone, and a BFS answers it in O(m·n).

**Reduces to**: [Template 1](#template-1-smallest-feasible-value--lc-875-) — over `[0, max height − min height]`, with a BFS reachability test as the predicate instead of an O(n) scan.

```python
# python
# LC 1631 - Path With Minimum Effort
# IDEA: binary search the effort limit; feasibility = BFS that only takes steps with |diff| <= limit
# time = O(m * n * log H), space = O(m * n)   H = max height - min height
from collections import deque

def minimumEffortPath(heights):
    rows, cols = len(heights), len(heights[0])

    def can_reach(limit):
        seen = [[False] * cols for _ in range(rows)]
        seen[0][0] = True
        queue = deque([(0, 0)])
        while queue:
            r, c = queue.popleft()
            if r == rows - 1 and c == cols - 1:
                return True
            for nr, nc in ((r + 1, c), (r - 1, c), (r, c + 1), (r, c - 1)):
                if 0 <= nr < rows and 0 <= nc < cols and not seen[nr][nc]:
                    if abs(heights[nr][nc] - heights[r][c]) <= limit:
                        seen[nr][nc] = True
                        queue.append((nr, nc))
        return False

    highest = max(max(row) for row in heights)
    lowest = min(min(row) for row in heights)
    left, right = 0, highest - lowest
    while left < right:
        mid = left + (right - left) // 2
        if can_reach(mid):
            right = mid
        else:
            left = mid + 1
    return left
```

```java
// java
// LC 1631 - Path With Minimum Effort
// IDEA: binary search the effort limit; feasibility = BFS reachability under the limit
// time = O(m * n * log H), space = O(m * n)
public int minimumEffortPath(int[][] heights) {
    int highest = Integer.MIN_VALUE, lowest = Integer.MAX_VALUE;
    for (int[] row : heights) {
        for (int h : row) {
            highest = Math.max(highest, h);
            lowest = Math.min(lowest, h);
        }
    }
    int left = 0, right = highest - lowest;
    while (left < right) {
        int mid = left + (right - left) / 2;
        if (canReach(heights, mid)) right = mid;
        else left = mid + 1;
    }
    return left;
}

private boolean canReach(int[][] heights, int limit) {
    int rows = heights.length, cols = heights[0].length;
    boolean[][] seen = new boolean[rows][cols];
    int[][] dirs = {{1, 0}, {-1, 0}, {0, 1}, {0, -1}};
    Deque<int[]> queue = new ArrayDeque<>();
    queue.offer(new int[]{0, 0});
    seen[0][0] = true;
    while (!queue.isEmpty()) {
        int[] cur = queue.poll();
        if (cur[0] == rows - 1 && cur[1] == cols - 1) return true;
        for (int[] d : dirs) {
            int nr = cur[0] + d[0], nc = cur[1] + d[1];
            if (nr >= 0 && nr < rows && nc >= 0 && nc < cols && !seen[nr][nc]
                    && Math.abs(heights[nr][nc] - heights[cur[0]][cur[1]]) <= limit) {
                seen[nr][nc] = true;
                queue.offer(new int[]{nr, nc});
            }
        }
    }
    return false;
}
```

**Traps**
- A 1×1 grid has effort 0: `right = 0`, and the loop must not run.
- Marking a cell seen when it is popped rather than pushed lets it enter the queue many times;
  still correct, but O((m·n)²) on a flat grid.

**Not this if** — the interviewer asks for the faster bound: Dijkstra with the bottleneck
relaxation `max(effort, step)` is O(m·n log(m·n)) ([Dijkstra.md](./Dijkstra.md)). LC 778 (Swim in
Rising Water) is the same search with a cell-value limit instead of a step limit.

### Largest Feasible Value Examples

#### Divide Chocolate — LC 1231 ⭐⭐⭐⭐⭐

> **Key point** — you eat the least sweet of `k + 1` contiguous pieces, so search the largest
> `x` such that the bar cuts into at least `k + 1` pieces of sweetness `>= x`. Cutting as soon as
> a piece reaches `x` makes the most pieces, and a smaller `x` only makes more.

**Reduces to**: [Template 2](#template-2-largest-feasible-value--lc-1552-) — largest feasible value with the upper mid, over `[min(sweetness), sum // (k + 1)]`; the mirror of LC 410.

```python
# python
# LC 1231 - Divide Chocolate
# IDEA: last x for which greedy cutting makes >= k + 1 pieces of sweetness >= x; upper mid
# time = O(n log S), space = O(1)   S = sum(sweetness)
def maximizeSweetness(sweetness, k):
    people = k + 1

    def can_cut(target):
        pieces = 0
        current = 0
        for s in sweetness:
            current += s
            if current >= target:            # this piece is sweet enough: cut it here
                pieces += 1
                current = 0
        return pieces >= people              # leftover sweetness joins the last piece

    left, right = min(sweetness), sum(sweetness) // people
    while left < right:
        mid = left + (right - left + 1) // 2  # upper mid: left = mid must move
        if can_cut(mid):
            left = mid
        else:
            right = mid - 1
    return left
```

```java
// java
// LC 1231 - Divide Chocolate
// IDEA: last x for which greedy cutting makes >= k + 1 pieces of sweetness >= x; upper mid
// time = O(n log S), space = O(1)
public int maximizeSweetness(int[] sweetness, int k) {
    int people = k + 1;
    int left = Integer.MAX_VALUE, sum = 0;
    for (int s : sweetness) {
        left = Math.min(left, s);
        sum += s;
    }
    int right = sum / people;
    while (left < right) {
        int mid = left + (right - left + 1) / 2;
        if (canCut(sweetness, people, mid)) left = mid;
        else right = mid - 1;
    }
    return left;
}

private boolean canCut(int[] sweetness, int people, int target) {
    int pieces = 0, current = 0;
    for (int s : sweetness) {
        current += s;
        if (current >= target) {
            pieces++;
            current = 0;
        }
    }
    return pieces >= people;
}
```

```text
sweetness = [1,2,3,4,5,6,7,8,9], k = 5   -> 6 pieces, search [1, 45 // 6 = 7]
left=1  right=7  mid=4   [1,2,3][4][5][6][7][8][9]   7 pieces >= 6 -> left=4
left=4  right=7  mid=6   [1,2,3][4,5][6][7][8][9]    6 pieces >= 6 -> left=6
left=6  right=7  mid=7   [1,2,3,4][5,6][7][8][9]     5 pieces <  6 -> right=6
left=6  right=6  stop -> 6
```

**Traps**
- The lower mid with `left = mid` never moves once `right = left + 1`: `[1,2]`, `k = 0` reaches
  `left = 2, right = 3`, probes 2 again and loops forever.
- Counting **exactly** `k + 1` pieces is wrong: extra pieces merge into the last one without
  lowering its sweetness, so the test is `>=`.

**Similar** — LC 1552 *(the anchor: gaps between balls)* · LC 2226 *(`sum(c // x) >= k`)* · LC 410 *(the minimize-the-maximum mirror)*

### Split Into K Groups Examples

#### Capacity To Ship Packages Within D Days — LC 1011

> **Key point** — LC 410 with packages for numbers and days for groups: the capacity lies in
> `[max(weights), sum(weights)]`, and loading each day until the next package would overflow uses
> the fewest days any loading can.

**Reduces to**: [Template 3](#template-3-split-into-at-most-k-contiguous-groups--lc-410-) — unchanged; only the names differ.

```python
# python
# LC 1011 - Capacity To Ship Packages Within D Days
# IDEA: first capacity in [max(weights), sum(weights)] whose greedy loading needs <= days days
# time = O(n log S), space = O(1)   S = sum(weights)
def shipWithinDays(weights, days):
    def can_ship(capacity):
        needed = 1
        load = 0
        for weight in weights:
            if load + weight <= capacity:
                load += weight
            else:
                needed += 1                  # this package starts a new day
                load = weight
                if needed > days:
                    return False
        return True

    left, right = max(weights), sum(weights)
    while left < right:
        mid = left + (right - left) // 2
        if can_ship(mid):
            right = mid
        else:
            left = mid + 1
    return left
```

```java
// java
// LC 1011 - Capacity To Ship Packages Within D Days
// IDEA: first capacity in [max, sum] whose greedy loading needs <= days days
// time = O(n log S), space = O(1)
public int shipWithinDays(int[] weights, int days) {
    int left = 0, right = 0;
    for (int weight : weights) {
        left = Math.max(left, weight);   // a day must carry the heaviest package
        right += weight;                 // one day carries everything
    }
    while (left < right) {
        int mid = left + (right - left) / 2;
        if (canShip(weights, days, mid)) right = mid;
        else left = mid + 1;
    }
    return left;
}

private boolean canShip(int[] weights, int days, int capacity) {
    int needed = 1, load = 0;
    for (int weight : weights) {
        if (load + weight <= capacity) {
            load += weight;
        } else {
            needed++;
            load = weight;
            if (needed > days) return false;
        }
    }
    return true;
}
```

**Traps**
- `left = 1` lets a package heavier than the capacity ride alone, and the greedy calls it
  feasible: `weights = [1, 10]`, `days = 2` returns 1 instead of 10. Start at `max(weights)`.

### Count-at-Most Examples

#### Find the Duplicate Number — LC 287 ⭐⭐⭐⭐⭐

> **Key point** — the array is unsorted, so its indices are useless, but its values lie in
> `[1, n]`. `count(v)` = how many values are `<= v` exceeds `v` exactly when the duplicate is
> `<= v` (pigeonhole), so the duplicate is the first `v` with `count(v) > v`.

**Reduces to**: [Template 4](#template-4-count-at-most-over-a-value-range--lc-378-) — count over the value range `[1, n]`, compared with `v` itself instead of with `k`.

```python
# python
# LC 287 - Find the Duplicate Number
# IDEA: first value v in [1, n] with count(<= v) > v (pigeonhole); the array is never modified
# time = O(n log n), space = O(1)
def findDuplicate(nums):
    left, right = 1, len(nums) - 1          # values are in [1, n], n = len(nums) - 1
    while left < right:
        mid = left + (right - left) // 2
        count = 0
        for x in nums:
            if x <= mid:
                count += 1
        if count > mid:
            right = mid                     # too many values <= mid: the duplicate is <= mid
        else:
            left = mid + 1                  # the duplicate is above mid
    return left
```

```java
// java
// LC 287 - Find the Duplicate Number
// IDEA: first value v with count(<= v) > v (pigeonhole)
// time = O(n log n), space = O(1)
public int findDuplicate(int[] nums) {
    int left = 1, right = nums.length - 1;
    while (left < right) {
        int mid = left + (right - left) / 2;
        int count = 0;
        for (int x : nums) if (x <= mid) count++;
        if (count > mid) right = mid;
        else left = mid + 1;
    }
    return left;
}
```

**Traps**
- `count >= mid` is off by one: `[1,3,4,2,2]` has `count(1) = 1`, which would wrongly report 1.
- The duplicate may appear more than twice (`[2,2,2,2,2]`); the pigeonhole test still holds.

**Second approach** — Floyd's cycle detection on `i -> nums[i]` is O(n) time and O(1) space,
and is the bound to name once this one is written
([2_pointers_linkedlist.md](./2_pointers_linkedlist.md)). Sorting or a hash set breaks the
"do not modify, O(1) space" rules.

---

## Problems by Pattern

Every problem worked on this sheet is filed above under the template it reduces to; the order to
drill them is the [Drill Path](#drill-path). LC 69 (Sqrt(x)) is worked in
[math.md](./math.md#1-1-10-integer-square-root--binary-search--newton-), and LC 367, LC 441 and
LC 1060 in [binary_search_examples.md](./binary_search_examples.md#search-on-the-answer-examples).

---

## Pattern Selection Strategy

**How to Recognize:**
1. Problem asks for "minimum/maximum" value
2. You can easily check "is X valid?" but not "what is the optimal X?"
3. Answer has monotonic property (if X works, X+1 or X-1 also works)

**Common Mistakes:**
1. **Wrong search space bounds**
   - Too narrow: Missing the optimal answer
   - Solution: Carefully analyze minimum (e.g., max element) and maximum (e.g., sum)

2. **Off-by-one in validation**
   ```java
   // ❌ WRONG: Using < instead of <=
   if (currentSum + num < maxSum) {...}

   // ✅ CORRECT: Must allow equality
   if (currentSum + num <= maxSum) {...}
   ```

3. **Wrong boundary update**
   - Minimize: `right = mid` (not `mid - 1`)
   - Maximize: `left = mid`, with the upper mid `(l + r + 1) / 2`

4. **Inefficient validation**
   - Add early termination in validation function
   - Use greedy approach for O(n) validation

**Talking Points:**
- "This is a binary search on answer space problem"
- "I'll binary search on [min, max] and use a helper to validate"
- "The answer has monotonic property: if X works, larger X also works"
- "Time complexity: O(n × log(range)) where n is validation cost"

---

## Drill Path

| Stage | Problems (easy → hard) | What it adds |
|---|---|---|
| **Core** ⭐ | LC 875 ⭐ · LC 1552 · LC 410 | the smallest feasible value, the largest feasible value (upper mid), and the `[max, sum]` split |
| **Practice** | LC 1283 · LC 1011 · LC 1482 · LC 2226 · LC 287 · LC 378 | other greedy predicates; then counting over a value range |
| **Stretch** | LC 2616 · LC 1631 · LC 1231 · LC 668 · LC 719 | sort-then-pair, a BFS predicate, maximize the minimum by cutting, counting without listing |

---

## Summary

- **Three preconditions** — a bounded answer range, a *monotone* feasibility predicate, and a validation you can run in `O(n)` / `O(n log n)`. If any one is missing, this is not the pattern.
- **Two shapes only** — minimise the maximum (`if valid: right = mid`, return `left`) and maximise the minimum (`if valid: left = mid`, `mid = left + (right - left + 1) / 2`, return `left`). Everything else is the predicate.
- **Boundary recipe** — "split / ship / distribute array elements into K groups and minimise the maximum" ⇒ `left = max(nums)`, `right = sum(nums)`. "Rate / divisor / days" ⇒ `left = 1`, `right = max(nums)`.
- **Same skeleton, different predicate** — a greedy scan (LC 410, 1011, 875, 1482), a count over a value domain (LC 378, 287, 1539), or a graph walk (LC 1631, LC 778).
- Say it out loud in the interview: *"the answer is monotone in feasibility, so I'll binary search the answer space and write `isValid` as the helper."*
