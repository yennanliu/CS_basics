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
- Update: `if valid: left = mid` (try larger), with the upper mid `mid = (l + r + 1) // 2` (Java: `(int) (((long) l + r + 1) / 2)`, so the sum cannot overflow) so the loop always moves
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
- An `int` hour count overflows in Java: `piles = [10^9, 10^9, 10^9]`, `h = 3` sums 3·10⁹ hours at speed 1,
  past `Integer.MAX_VALUE` (≈ 2.15·10⁹).
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

### Capacity To Ship Packages Within D Days — LC 1011

**Problem:** Ship packages within D days. Find minimum capacity needed.

```java
// LC 1011 - Capacity To Ship Packages Within D Days
class Solution {
    /**
     * time = O(N × log(sum))
     * space = O(1)
     */
    public int shipWithinDays(int[] weights, int days) {
        // Search space: [max_weight, sum_of_weights]
        int left = 0, right = 0;

        for (int weight : weights) {
            left = Math.max(left, weight);  // Must hold largest package
            right += weight;                // Upper bound
        }

        while (left < right) {
            int mid = left + (right - left) / 2;

            // Can we ship all packages within D days with capacity = mid?
            if (canShip(weights, days, mid)) {
                right = mid;  // Try smaller capacity (minimize)
            } else {
                left = mid + 1;
            }
        }

        return left;
    }

    // Check if we can ship within D days with given capacity
    private boolean canShip(int[] weights, int days, int capacity) {
        int daysNeeded = 1;
        int currentLoad = 0;

        for (int weight : weights) {
            if (currentLoad + weight <= capacity) {
                currentLoad += weight;
            } else {
                daysNeeded++;
                currentLoad = weight;

                if (daysNeeded > days) {
                    return false;
                }
            }
        }

        return true;
    }
}
```

**Approach**: Binary search on capacity + greedy validation
```python
# LC 1011
# V1
# IDEA : BINARY SEARCH
# https://leetcode.com/problems/capacity-to-ship-packages-within-d-days/discuss/390359/Simple-Python-Binary-Search
# V0
# IDEA : BINARY SEARCH
class Solution(object):
     def shipWithinDays(self, weights, D):
            """
            NOTE !!!
                -> for this help func,
                -> we ONLY need to check weights can split by offered max_wgt
                -> so the return val is boolean (True or False)
            """
            # help func
            def cannot_split(weights, D, max_wgt):
                s = 0
                days = 1
                for w in weights:
                    s += w
                    if s > max_wgt:
                        s = w
                        days += 1
                return days > D

            """
            NOTE this !!!
                -> for l, we use max(weights)
                -> for r, we use sum(weights)
            """
            l = max(weights)
            r = sum(weights)
            while l <= r:
                mid = l + (r - l) // 2
                if cannot_split(weights, D, mid):
                    l = mid + 1
                else:
                    r = mid - 1
            return l
```

---

### Find the Smallest Divisor — LC 1283
**Problem:** Find smallest divisor such that sum(ceil(num/divisor)) ≤ threshold.

```java
// LC 1283 - Find the Smallest Divisor
class Solution {
    /**
     * time = O(N × log(max_num))
     * space = O(1)
     */
    public int smallestDivisor(int[] nums, int threshold) {
        int left = 1;
        int right = 0;

        for (int num : nums) {
            right = Math.max(right, num);
        }

        while (left < right) {
            int mid = left + (right - left) / 2;

            if (getDivisionSum(nums, mid) <= threshold) {
                right = mid;  // Valid, try smaller divisor (minimize)
            } else {
                left = mid + 1;  // Sum too large, need larger divisor
            }
        }

        return left;
    }

    private int getDivisionSum(int[] nums, int divisor) {
        int sum = 0;
        for (int num : nums) {
            sum += (num + divisor - 1) / divisor;  // Ceiling division
        }
        return sum;
    }
}
```
---

### Minimum Number of Days to Make m Bouquets — LC 1482
**Pattern**: `while (l < r - 1)` - Complex validation with helper function
```python
# LC 1482 Minimum Number of Days to Make m Bouquets
class Solution(object):
    def minDays(self, bloomDay, m, k):
        if m * k > len(bloomDay):
            return -1
        
        def canMakeBouquets(days):
            bouquets = consecutive = 0
            for bloom in bloomDay:
                if bloom <= days:
                    consecutive += 1
                    if consecutive == k:
                        bouquets += 1
                        consecutive = 0
                else:
                    consecutive = 0
            return bouquets >= m
        
        l, r = min(bloomDay), max(bloomDay)
        
        while l < r:
            mid = l + (r - l) // 2
            if canMakeBouquets(mid):
                r = mid
            else:
                l = mid + 1
        
        return l
```

> Binary search on the number of days; check if m bouquets of k adjacent flowers can be made.

```java
// LC 1482 - Minimum Number of Days to Make m Bouquets
// IDEA: Binary search on days [min, max]; check feasibility
// time = O(N log D), space = O(1)
public int minDays(int[] bloomDay, int m, int k) {
    if ((long) m * k > bloomDay.length) return -1;
    int l = 1, r = 0;
    for (int d : bloomDay) r = Math.max(r, d);
    while (l < r) {
        int mid = (l + r) / 2;
        if (canMake(bloomDay, m, k, mid)) r = mid;
        else l = mid + 1;
    }
    return l;
}
private boolean canMake(int[] bloomDay, int m, int k, int day) {
    int bouquets = 0, consecutive = 0;
    for (int d : bloomDay) {
        if (d <= day) { if (++consecutive == k) { bouquets++; consecutive = 0; } }
        else consecutive = 0;
    }
    return bouquets >= m;
}
```

---

### Divide Chocolate — LC 1231 (Maximize Minimum) ⭐⭐⭐⭐⭐

**Problem:** Divide chocolate bar into K+1 pieces (sharing with K friends). You eat the piece with minimum sweetness. Maximize that minimum sweetness.

**Key Insight:** This is a **"Maximize Minimum"** problem - the counterpart to "Minimize Maximum":
1. **Binary search** on the "minimum sweetness" you can get
2. **Greedy validation**: Can we cut into ≥ K+1 pieces where each piece has sweetness ≥ mid?
3. If valid → try larger minimum (go right)
4. If invalid → need smaller target (go left)

**Why Greedy Works:**
- Greedily accumulate sweetness until reaching target, then cut
- This maximizes the number of valid pieces for a given target
- Monotonic property: if minSweetness X works, X-1 also works (easier to split)

```java
// LC 1231 - Divide Chocolate
class Solution {
    /**
     * time = O(N × log(sum))
     * space = O(1)
     *
     * Approach: Binary search on answer space [1, sum/totalPeople]
     * Pattern: MAXIMIZE MINIMUM
     */
    public int maximizeSweetness(int[] sweetness, int k) {
        int totalPeople = k + 1;  // K friends + yourself

        // Step 1: Define search space
        int left = 1;  // Minimum possible sweetness
        int right = 0;
        for (int s : sweetness) right += s;
        right /= totalPeople;  // Upper bound: average sweetness

        int ans = 0;

        // Step 2: Binary search on "minimum sweetness you can get"
        while (left <= right) {
            int mid = left + (right - left) / 2;

            // Step 3: Check if we can make at least totalPeople pieces
            // where each piece has at least 'mid' sweetness
            if (canSplit(sweetness, totalPeople, mid)) {
                ans = mid;        // Valid! This could be our answer
                left = mid + 1;   // Try larger minimum (MAXIMIZE)
            } else {
                right = mid - 1;  // Can't split, need smaller target
            }
        }

        return ans;
    }

    // Validation: Can we make at least 'totalPeople' pieces with each >= minTarget?
    private boolean canSplit(int[] sweetness, int totalPeople, int minTarget) {
        int currentSweetness = 0;
        int pieces = 0;

        for (int s : sweetness) {
            currentSweetness += s;
            // When current piece reaches target, cut it
            if (currentSweetness >= minTarget) {
                pieces++;
                currentSweetness = 0;
            }
        }

        return pieces >= totalPeople;  // Can we make enough pieces?
    }
}
```

**Alternative Template (while l < r):**

```java
public int maximizeSweetness(int[] sweetness, int k) {
    int left = 1;
    int right = 0;
    for (int s : sweetness) right += s;

    // Binary search with half-open interval
    while (left < right) {
        // CRITICAL: Use (l + r + 1) / 2 for maximize problems to avoid infinite loop
        int mid = left + (right - left + 1) / 2;

        if (canSplit(sweetness, k + 1, mid)) {
            left = mid;       // Valid → try larger (maximize)
        } else {
            right = mid - 1;  // Invalid → reduce target
        }
    }

    return left;
}
```

**Step-by-Step Trace:** `sweetness = [1,2,3,4,5,6,7,8,9], k = 5`

```text
Total people = 6, Sum = 45
Search space: [1, 7]  (1 to 45/6)

Iteration 1: mid = 4
  Pieces with sweetness >= 4:
  [1,2,3]=6 ✓, [4]=4 ✓, [5]=5 ✓, [6]=6 ✓, [7]=7 ✓, [8]=8 ✓, [9]=9 ✓
  Actually: [1,2,3]=6, [4,5]=9... need to re-trace

  Greedy: 1+2+3=6≥4 ✓, 4≥4 ✓, 5≥4 ✓, 6≥4 ✓, 7≥4 ✓, 8≥4 ✓
  Pieces = 6 ≥ 6 ✓
  Valid! Try larger: left = 5

Iteration 2: mid = 6
  Greedy: 1+2+3=6≥6 ✓, 4+5=9≥6 ✓, 6≥6 ✓, 7≥6 ✓, 8≥6 ✓, 9≥6 ✓
  Pieces = 6 ≥ 6 ✓
  Valid! Try larger: left = 7

Iteration 3: mid = 7
  Greedy: 1+2+3+4=10≥7 ✓, 5+6=11≥7 ✓, 7≥7 ✓, 8≥7 ✓, 9≥7 ✓
  Pieces = 5 < 6 ✗
  Invalid! Reduce: right = 6

Final: left = 7 > right = 6, return ans = 6
```

**Similar Problems (Maximize Minimum Pattern):**

| Problem | Description | Validation Logic |
|---------|-------------|------------------|
| **LC 1231** | Divide Chocolate | Can split into K+1 pieces with each ≥ target |
| LC 1552 | Magnetic Force | Can place m balls with min distance ≥ target |
| LC 2226 | Maximum Candies | Can give k children candies with each ≥ target |
| LC 2064 | Minimized Maximum Products | Distribute products to stores |

---

### Minimize the Maximum Difference of Pairs — LC 2616 ⭐⭐⭐⭐⭐

**Problem:** Find p pairs of indices such that the maximum difference amongst all pairs is minimized. Each index can only be used once.

**Key Insight:** This is a classic "Minimize the Maximum" problem:
1. **Sort** the array so closest numbers are adjacent
2. **Binary search** on the "maximum difference" value
3. **Greedy check**: Can we find ≥ p pairs where each pair's diff ≤ mid?

**Why Greedy Works (but PQ Doesn't):**
- After sorting, adjacent pairs give minimum differences
- Greedy pairing rule: `if (nums[i+1] - nums[i] <= maxDiff) → take pair, skip i+1`
- This guarantees **maximum number of pairs** for that maxDiff
- PQ approach fails because it's a **matching optimization problem** where local greedy selection doesn't guarantee optimality
- Binary search has **monotonic property**: if maxDiff X works, any larger diff also works

```java
// LC 2616 - Minimize the Maximum Difference of Pairs
class Solution {
    /**
     * time = O(N log N + N log(max-min))
     * space = O(1)
     *
     * Approach:
     * 1. Sort array → adjacent elements have minimum differences
     * 2. Binary search on answer space [0, max_diff]
     * 3. Greedy validation: count pairs with diff <= mid
     */
    public int minimizeMax(int[] nums, int p) {
        if (p == 0) return 0;

        // Step 1: Sort to make closest numbers adjacent
        Arrays.sort(nums);

        int n = nums.length;
        // Search space: [0, max_difference]
        int left = 0;
        int right = nums[n - 1] - nums[0];

        // Step 2: Binary search on possible "maximum difference"
        while (left < right) {
            int mid = left + (right - left) / 2;

            // Step 3: Check if we can form at least p pairs with diff <= mid
            if (canFormPairs(nums, p, mid)) {
                right = mid;  // Valid! Try smaller max diff (minimize)
            } else {
                left = mid + 1;  // Can't form enough pairs, need larger diff
            }
        }

        return left;
    }

    // Greedy validation: count maximum pairs with diff <= maxDiff
    private boolean canFormPairs(int[] nums, int p, int maxDiff) {
        int count = 0;

        for (int i = 0; i < nums.length - 1; i++) {
            // If adjacent pair fits constraint, take it!
            if (nums[i + 1] - nums[i] <= maxDiff) {
                count++;
                i++;  // CRITICAL: Skip next index (element can only be in one pair)
            }
            if (count >= p) return true;  // Early termination
        }

        return count >= p;
    }
}
```

**Step-by-Step Trace:** `nums = [10,1,2,7,1,3], p = 2`

```text
After sorting: [1, 1, 2, 3, 7, 10]
Adjacent diffs: [0, 1, 1, 4, 3]

Search space: [0, 9]  (min diff to max diff)

Iteration 1: mid = 4
  Pairs with diff ≤ 4: (1,1)=0 ✓, skip, (2,3)=1 ✓ → 2 pairs
  Valid! Try smaller: right = 4

Iteration 2: mid = 2
  Pairs with diff ≤ 2: (1,1)=0 ✓, skip, (2,3)=1 ✓ → 2 pairs
  Valid! Try smaller: right = 2

Iteration 3: mid = 1
  Pairs with diff ≤ 1: (1,1)=0 ✓, skip, (2,3)=1 ✓ → 2 pairs
  Valid! Try smaller: right = 1

Final: left = 1 (minimum maximum difference)
```

**Why PQ Approach Fails - Counterexample:**

```text
nums = [1, 3, 4, 6, 7, 20], p = 2
Sorted diffs: (3,4)=1, (6,7)=1, (1,3)=2, (4,6)=2, (7,20)=13

PQ picks smallest first:
  1. (3,4)=1 → use 3,4
  2. (6,7)=1 → use 6,7
  Result: max = 1 ✓ (happens to be correct here)

But in general, PQ may pick overlapping or suboptimal pairs.
Binary search guarantees correctness via monotonic property.
```

---

### Path With Minimum Effort — LC 1631, a Graph Predicate ⭐⭐⭐⭐

> Same "minimize the maximum" skeleton as [Template 1](#template-1-smallest-feasible-value--lc-875-) above, but the feasibility check is a **BFS/DFS reachability test** instead of an `O(n)` scan.

`canReach(limit)` = "can I walk from top-left to bottom-right using only steps whose cost `<= limit`?" — monotone in `limit` (a bigger limit only unlocks more edges), which is exactly what binary search needs.

```java
// java
// LC 1631 - Path With Minimum Effort
// IDEA: binary search the answer (max allowed |height diff|); feasibility = BFS reachability
// time = O(m*n*log(maxH-minH)), space = O(m*n)
public int minimumEffortPath(int[][] heights) {
    int mx = Integer.MIN_VALUE, mn = Integer.MAX_VALUE;
    for (int[] row : heights) for (int v : row) { mx = Math.max(mx, v); mn = Math.min(mn, v); }
    int lo = 0, hi = mx - mn;                 // effort 0 is possible (flat grid)
    while (lo < hi) {
        int mid = lo + (hi - lo) / 2;
        if (canReach(heights, mid)) hi = mid; // mid works -> try smaller
        else lo = mid + 1;
    }
    return lo;
}

private boolean canReach(int[][] h, int limit) {
    int m = h.length, n = h[0].length;
    boolean[][] seen = new boolean[m][n];
    int[][] dirs = {{1,0},{-1,0},{0,1},{0,-1}};
    Deque<int[]> q = new ArrayDeque<>();
    q.offer(new int[]{0, 0});
    seen[0][0] = true;
    while (!q.isEmpty()) {
        int[] cur = q.poll();
        if (cur[0] == m - 1 && cur[1] == n - 1) return true;
        for (int[] d : dirs) {
            int nr = cur[0] + d[0], nc = cur[1] + d[1];
            if (nr >= 0 && nr < m && nc >= 0 && nc < n && !seen[nr][nc]
                    && Math.abs(h[nr][nc] - h[cur[0]][cur[1]]) <= limit) {
                seen[nr][nc] = true;
                q.offer(new int[]{nr, nc});
            }
        }
    }
    return false;
}
```

```python
# python
# LC 1631 - Path With Minimum Effort
# IDEA: binary search on the answer + BFS feasibility check (monotone in `limit`)
# time = O(m*n*log(maxH-minH)), space = O(m*n)
from collections import deque

class Solution:
    def minimumEffortPath(self, heights):
        m, n = len(heights), len(heights[0])

        def can_reach(limit):
            seen = [[False] * n for _ in range(m)]
            seen[0][0] = True
            q = deque([(0, 0)])
            while q:
                r, c = q.popleft()
                if r == m - 1 and c == n - 1:
                    return True
                for nr, nc in ((r + 1, c), (r - 1, c), (r, c + 1), (r, c - 1)):
                    if 0 <= nr < m and 0 <= nc < n and not seen[nr][nc] \
                            and abs(heights[nr][nc] - heights[r][c]) <= limit:
                        seen[nr][nc] = True
                        q.append((nr, nc))
            return False

        lo, hi = 0, max(map(max, heights)) - min(map(min, heights))
        while lo < hi:
            mid = (lo + hi) // 2
            if can_reach(mid):
                hi = mid
            else:
                lo = mid + 1
        return lo
```

**Sibling problems (identical skeleton, different edge rule)**

| LC | Problem | `canReach(limit)` means |
|----|---------|-------------------------|
| **1631** | Path With Minimum Effort | every step's absolute height difference is `<= limit` |
| 778 | Swim in Rising Water | every visited **cell value** is `<= limit` (the time `t`) |

> Alternative for both: **Dijkstra with a min-max ("bottleneck") relaxation** `nd = max(d, cost)`, or **union-find** processing cells in sorted order. Binary search + BFS is the easiest to derive under time pressure; say the Dijkstra variant out loud as the `O(mn log mn)` improvement.

---

### Find the Duplicate Number — LC 287, Searching a Value Domain ⭐⭐⭐⭐⭐

> `n + 1` integers, each in `[1, n]`. Exactly one number repeats. Find it **without modifying the array** and in `O(1)` extra space.

#### Core Idea — the index space is useless, the value space is not

The array is **not sorted**, so binary searching indices is meaningless. But the **values** live in a known range `[1, n]`, and the counting function

```text
count(v) = #{ x in nums : x <= v }
```

is **monotonically non-decreasing** in `v`. By pigeonhole:

```text
count(v) >  v   ->  a duplicate lives in [1, v]     -> hi = v
count(v) <= v   ->  the duplicate is above v        -> lo = v + 1
```

Converge with the standard "first `True`" template → `lo` is the duplicate.

```java
// java
// LC 287 - Find the Duplicate Number
// IDEA: binary search the VALUE range [1, n]; count(mid) > mid (pigeonhole) => duplicate is <= mid
// time = O(n log n), space = O(1)
public int findDuplicate(int[] nums) {
    int lo = 1, hi = nums.length - 1;          // value range [1, n]
    while (lo < hi) {
        int mid = lo + (hi - lo) / 2;
        int cnt = 0;
        for (int x : nums) if (x <= mid) cnt++;
        if (cnt > mid) hi = mid;               // too many small values -> dup in [lo, mid]
        else lo = mid + 1;                     // dup is strictly above mid
    }
    return lo;
}
```

```python
# python
# LC 287 - Find the Duplicate Number
# IDEA: binary search on the value domain [1, n] + pigeonhole count
# time = O(n log n), space = O(1)
class Solution:
    def findDuplicate(self, nums):
        lo, hi = 1, len(nums) - 1
        while lo < hi:
            mid = (lo + hi) // 2
            if sum(1 for x in nums if x <= mid) > mid:
                hi = mid                # duplicate is in [lo, mid]
            else:
                lo = mid + 1            # duplicate is above mid
        return lo
```

**Interview notes**
- Mention the **`O(n)` Floyd cycle-detection** alternative (treat `i -> nums[i]` as a linked list, find the cycle entrance) — the binary search is the one interviewers accept when you can't recall Floyd, and it is easier to prove.
- Same skeleton as the LC 378 section above (kth smallest in a sorted matrix) and LC 719 — only the `count()` implementation changes. See also `matrix.md` for the matrix-flavoured version.

**Variation — derived monotone predicate (LC 1539 Kth Missing Positive Number)**
The twist: instead of counting values, derive a monotone quantity **from the index**. In a strictly increasing positive array, the number of missing positives before index `i` is `arr[i] - (i + 1)` — non-decreasing in `i`. Find the first index where it reaches `k`:

```java
// java
// LC 1539 - Kth Missing Positive Number
// IDEA: missing(i) = arr[i] - (i+1) is monotone -> lower bound on "missing(i) >= k"
// time = O(log n), space = O(1)
public int findKthPositive(int[] arr, int k) {
    int lo = 0, hi = arr.length;               // note hi = n (answer may be past the end)
    while (lo < hi) {
        int mid = lo + (hi - lo) / 2;
        if (arr[mid] - (mid + 1) < k) lo = mid + 1;
        else hi = mid;
    }
    return lo + k;                             // lo numbers present before the answer
}
```

```python
# python
# LC 1539 - Kth Missing Positive Number
# IDEA: binary search the first index whose "missing count so far" >= k
# time = O(log n), space = O(1)
class Solution:
    def findKthPositive(self, arr, k):
        lo, hi = 0, len(arr)          # hi = n, the answer can fall past the last element
        while lo < hi:
            mid = (lo + hi) // 2
            if arr[mid] - (mid + 1) < k:
                lo = mid + 1
            else:
                hi = mid
        return lo + k
```

---

## Problems by Pattern

| Problem | Difficulty | Pattern | Key Insight |
|---------|------------|---------|-------------|
| LC 69 | Easy | Integer square root | Search on [0, x] |
| LC 875 | Medium | Minimize speed | Eating bananas, greedy validation |
| LC 1011 | Medium | Minimize capacity | Ship packages, similar to LC 410 |
| **LC 1231** | **Hard** | **Maximize minimum** | **Divide chocolate, greedy cut when sum ≥ target** |
| LC 1283 | Medium | Minimize divisor | Ceiling division, sum constraint |
| LC 1482 | Medium | Minimize days | Make bouquets, range validation |
| LC 1552 | Medium | Maximize minimum | Magnetic force, aggressive cows |
| LC 2226 | Medium | Maximize candies | Per-child allocation |
| **LC 2616** | **Medium** | **Minimize max diff** | **Sort + greedy pairing, skip used** |
| LC 410 | Hard | Minimize maximum | Split array, subarray sum |
| LC 2064 | Medium | Minimize max | Distribute products to stores |

**Practice Progression:**
1. Start with LC 875 (clearest example of minimize pattern)
2. Then LC 1011 (similar to 410 but easier)
3. Master LC 410 (classic minimize maximum, frequently asked)
4. Try LC 1231 (maximize minimum - counterpart to LC 410)
5. Try LC 2616 (minimize max with pairing constraint)
6. Explore LC 1283, 1482 (variations)
7. Challenge: LC 1552, 2064, 2226 (maximize minimum pattern)

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
   - Maximize: `left = mid`, with the upper mid `(l + r + 1) // 2` (Java: `(int) (((long) l + r + 1) / 2)`)

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
