# Prefix Sum — Advanced Templates

> **Scope** — The six prefix-sum templates that borrow another structure or another identity: the complement trick, the monotonic deque for arrays with negatives, row-pair compression for 2D, prefix XOR, the sparse difference array via a hash map, and the prefix-sum-on-a-tree counting map.
> **See also**: [prefix_sum.md](./prefix_sum.md) — the parent sheet: templates 1–8, the concept and the pattern-selection strategy; [prefix_sum_examples.md](./prefix_sum_examples.md) — the worked problems; [monotonic_queue.md](./monotonic_queue.md) — the deque behind template 10; [difference_array.md](./difference_array.md) — the dense counterpart to template 13; [bit_manipulation.md](./bit_manipulation.md) — why XOR supports the same subtraction identity as addition; [matrix.md](./matrix.md) — the 2D geometry template 11 collapses; [tree_backtrack.md](./tree_backtrack.md) — the root→leaf path templates template 14 generalises, and the undo-on-the-way-up habit it needs.

## LeetCode Problem Lists

- [Prefix Sum](https://leetcode.com/problem-list/prefix-sum/)
- [Matrix](https://leetcode.com/problem-list/matrix/)

## Overview

Templates 1–8 in [prefix_sum.md](./prefix_sum.md) are all the same move: build the array,
subtract two entries. These six are where that stops being enough.

### Key Properties
- **Complexity**: stated per template; the point of each is turning an O(n²) or O(n·m²) scan into O(n) or O(n·m)
- **Core Idea**: the prefix-sum identity `sum(l, r) = P[r+1] - P[l]` survives any *invertible* combining operation — which is why XOR works and why min/max do not
- **When to Use**: when a plain prefix sum is the obvious idea and something about the problem breaks it — negatives, two dimensions, a wrap-around, coordinates too large to allocate, or an "array" that is really a root→node chain in a tree


### Template 9: Complement Trick — "Total − Middle Window" ⭐⭐⭐⭐⭐ — LC 1423

**Key Idea:** When elements are taken **from both ends** of the array, the elements *left behind* always form ONE contiguous middle subarray. So instead of enumerating `(leftTake, rightTake)` pairs, flip the problem:

```text
take k from the two ends, MAXIMIZE taken
        ⇕  (complement)
leave a contiguous window of length n-k, MINIMIZE it

answer = total - min(window of fixed length n-k)

[ 1, 2, 3, 4, 5, 6, 1 ]   n=7, k=3  → window length w = 4
  └──── take ────┘           windows: [1,2,3,4]=10  [2,3,4,5]=14
        ↑ leave the cheapest length-4 window          [3,4,5,6]=18  [4,5,6,1]=16
  total = 22, min window = 10  →  answer = 22 - 10 = 12 ✓
```

This turns a "both ends" problem into a plain **fixed-length window over the prefix sum** — the reason it belongs here rather than in a two-pointer doc.

```java
// java
// LC 1423 - Maximum Points You Can Obtain from Cards
// IDEA: complement — maximize both ends == minimize the middle window of length n-k
// time = O(n), space = O(1)
public int maxScore(int[] cardPoints, int k) {
    int n = cardPoints.length, w = n - k;
    int total = 0;
    for (int x : cardPoints) total += x;
    if (w == 0) return total;               // take everything

    int win = 0;
    for (int i = 0; i < w; i++) win += cardPoints[i];
    int minWin = win;

    // slide the fixed-length window (rolling prefix-sum difference)
    for (int i = w; i < n; i++) {
        win += cardPoints[i] - cardPoints[i - w];
        minWin = Math.min(minWin, win);
    }
    return total - minWin;
}
```

```python
# python
# LC 1423 - Maximum Points You Can Obtain from Cards
# IDEA: complement — maximize both ends == minimize the middle window of length n-k
# time = O(n), space = O(1)
def maxScore(cardPoints, k):
    n = len(cardPoints)
    w = n - k
    total = sum(cardPoints)
    if w == 0:
        return total

    win = sum(cardPoints[:w])
    min_win = win
    for i in range(w, n):
        win += cardPoints[i] - cardPoints[i - w]   # slide right, drop left
        min_win = min(min_win, win)

    return total - min_win
```

#### Variation — **window length is NOT fixed** (LC 1658)

Same complement, but here we remove from the ends until they sum to `x`, so the middle window has a *fixed sum* (`total - x`) and a *variable length* that we want to **maximize**. Since `nums[i] >= 1`, prefix sums are strictly increasing → a shrinking window works.

```java
// java
// LC 1658 - Minimum Operations to Reduce X to Zero
// IDEA: remove-from-ends summing to x  ==  keep the LONGEST middle subarray summing to (total - x)
// time = O(n), space = O(1)
public int minOperations(int[] nums, int x) {
    int n = nums.length, total = 0;
    for (int v : nums) total += v;
    int target = total - x;
    if (target < 0) return -1;

    int left = 0, sum = 0, best = -1;
    for (int right = 0; right < n; right++) {
        sum += nums[right];
        while (sum > target) sum -= nums[left++];     // values are positive → safe to shrink
        if (sum == target) best = Math.max(best, right - left + 1);
    }
    return best == -1 ? -1 : n - best;                // ops = n - kept
}
```

```python
# python
# LC 1658 - Minimum Operations to Reduce X to Zero
# time = O(n), space = O(1)
def minOperations(nums, x):
    n = len(nums)
    target = sum(nums) - x
    if target < 0:
        return -1

    left = s = 0
    best = -1
    for right, v in enumerate(nums):
        s += v
        while s > target:
            s -= nums[left]
            left += 1
        if s == target:
            best = max(best, right - left + 1)

    return -1 if best == -1 else n - best
```

> If values could be **negative**, the shrink loop breaks — fall back to Template 2 (`{prefix_sum: first_index}`) to find the longest subarray with sum `target`.

### Template 10: Prefix Sum + Monotonic Deque (Shortest Subarray, allows NEGATIVES) ⭐⭐⭐⭐⭐ — LC 862

**Key Idea:** "Shortest subarray with sum ≥ K" is trivially sliding-window **only when all values are non-negative** (LC 209). With negatives, prefix sums are no longer monotonic and the window can't be shrunk safely. Fix: keep an **increasing monotonic deque of prefix-sum indices**.

```text
Two rules, both discard indices that can never be the best LEFT end:

1) POP FRONT while  p[i] - p[dq.front] >= K
   → dq.front already satisfies the condition at this i.
     Any later i' > i gives a LONGER subarray, so this front is used once and dropped.

2) POP BACK while  p[dq.back] >= p[i]
   → index i has a prefix sum <= dq.back but is FURTHER RIGHT.
     It is better as a left end in every way (smaller sum AND shorter span). dq.back is dead.

Deque therefore holds indices with STRICTLY INCREASING prefix sums. Each index is
pushed once and popped once → O(n) total.
```

**Trace on `nums = [2, -1, 2], K = 3` (`p = [0, 2, 1, 3]`):**

```text
i=0 p=0 : deque empty          → push 0            dq = [0]
i=1 p=2 : 2-0=2 < 3            → push 1            dq = [0,1]
i=2 p=1 : 1-0=1 < 3 ; p[1]=2 >= 1 → pop back 1, push 2   dq = [0,2]
i=3 p=3 : 3-p[0]=3 >= 3 → ans = 3-0 = 3, pop front
          3-p[2]=2 < 3 → stop ; push 3             dq = [2,3]
answer = 3
```

```java
// java
// LC 862 - Shortest Subarray with Sum at Least K
// IDEA: prefix sum + monotonic increasing deque (handles negative numbers)
// time = O(n), space = O(n)
public int shortestSubarray(int[] nums, int k) {
    int n = nums.length;
    long[] p = new long[n + 1];                       // long: sums can overflow int
    for (int i = 0; i < n; i++) p[i + 1] = p[i] + nums[i];

    Deque<Integer> dq = new ArrayDeque<>();           // indices, p[] increasing
    int ans = n + 1;

    for (int i = 0; i <= n; i++) {
        // rule 1: front satisfies the condition -> record and retire it
        while (!dq.isEmpty() && p[i] - p[dq.peekFirst()] >= k) {
            ans = Math.min(ans, i - dq.pollFirst());
        }
        // rule 2: keep prefix sums increasing
        while (!dq.isEmpty() && p[dq.peekLast()] >= p[i]) {
            dq.pollLast();
        }
        dq.offerLast(i);
    }
    return ans <= n ? ans : -1;
}
```

```python
# python
# LC 862 - Shortest Subarray with Sum at Least K
# IDEA: prefix sum + monotonic increasing deque (handles negative numbers)
# time = O(n), space = O(n)
from collections import deque
from itertools import accumulate

def shortestSubarray(nums, k):
    n = len(nums)
    p = list(accumulate(nums, initial=0))
    dq = deque()          # indices with increasing p[]
    ans = n + 1

    for i in range(n + 1):
        while dq and p[i] - p[dq[0]] >= k:      # rule 1: found a valid left end
            ans = min(ans, i - dq.popleft())
        while dq and p[dq[-1]] >= p[i]:         # rule 2: keep p[] increasing
            dq.pop()
        dq.append(i)

    return ans if ans <= n else -1
```

| Problem | LC # | Values | Right tool |
|---------|------|--------|------------|
| Minimum Size Subarray Sum | 209 | all positive | plain sliding window (see `sliding_window.md`) |
| Shortest Subarray with Sum at Least K | 862 | may be negative | **prefix sum + monotonic deque** |
| Subarray Sum Equals K | 560 | any | Template 2 (HashMap, exact sum) |

### Template 11: Row-Pair Compression — collapse 2D into 1D prefix sum ⭐⭐⭐⭐ — LC 363

**Key Idea:** Every submatrix is defined by a **row pair** `(top, bottom)` plus a column range. Fix the row pair, sum each column between those rows into a 1-D array `colSum`, and the 2-D question becomes the corresponding **1-D subarray question** — which you already know how to solve.

```text
fix top/bottom rows                  1-D array of column sums
┌───────────────┐
│ . . . . . . . │
│ a b c d e f g │ ← top          colSum = [a+h, b+i, c+j, ...]
│ h i j k l m n │ ← bottom
│ . . . . . . . │                then answer the 1-D version of the problem
└───────────────┘

O(m²) row pairs × O(n · cost of the 1-D solver)
```

For **LC 363 (max rectangle sum ≤ k)** the 1-D subproblem is "max subarray sum ≤ k":
`run - prefix_j <= k`  ⟹  `prefix_j >= run - k`  ⟹  look up the **smallest prefix ≥ run − k** in a sorted set (`ceiling` / `bisect_left`).

```java
// java
// LC 363 - Max Sum of Rectangle No Larger Than K
// IDEA: fix row pair -> column sums -> 1D "max subarray sum <= k" via TreeSet ceiling
// time = O(m^2 * n * log n), space = O(n)
public int maxSumSubmatrix(int[][] matrix, int k) {
    int m = matrix.length, n = matrix[0].length;
    int best = Integer.MIN_VALUE;

    for (int top = 0; top < m; top++) {
        int[] colSum = new int[n];                     // reset per top row
        for (int bot = top; bot < m; bot++) {
            for (int c = 0; c < n; c++) colSum[c] += matrix[bot][c];   // extend downward

            // ---- 1D: max subarray sum <= k ----
            TreeSet<Integer> seen = new TreeSet<>();
            seen.add(0);                               // empty prefix sentinel
            int run = 0;
            for (int c = 0; c < n; c++) {
                run += colSum[c];
                // want smallest prefix_j with prefix_j >= run - k
                Integer lo = seen.ceiling(run - k);
                if (lo != null) best = Math.max(best, run - lo);
                seen.add(run);
            }
        }
    }
    return best;
}
```

```python
# python
# LC 363 - Max Sum of Rectangle No Larger Than K
# IDEA: fix row pair -> column sums -> 1D "max subarray sum <= k" via bisect on sorted prefixes
# time = O(m^2 * n * log n), space = O(n)
from bisect import bisect_left, insort

def maxSumSubmatrix(matrix, k):
    m, n = len(matrix), len(matrix[0])
    best = float('-inf')

    for top in range(m):
        col = [0] * n
        for bot in range(top, m):
            for c in range(n):
                col[c] += matrix[bot][c]

            # ---- 1D: max subarray sum <= k ----
            seen = [0]          # sorted prefix sums, 0 = empty prefix
            run = 0
            for c in range(n):
                run += col[c]
                idx = bisect_left(seen, run - k)   # smallest prefix >= run - k
                if idx < len(seen):
                    best = max(best, run - seen[idx])
                insort(seen, run)

    return best
```

> **Swap the 1-D solver, get another problem.** Same outer double loop, different inner routine:
> - **LC 1074** (count submatrices summing to target) → inner solver = Template 2 HashMap.
> - **LC 363** (max sum ≤ k) → inner solver = sorted set + `ceiling`, as above.
> - If `n < m`, transpose first so the squared factor lands on the smaller dimension.

### Template 12: Prefix XOR ⭐⭐⭐⭐ — LC 1310

**Key Idea:** XOR is its own inverse (`a ^ a = 0`), which is exactly what subtraction does for sums. So the whole prefix-sum toolkit transfers by replacing `+`/`-` with `^`:

| | Sum | XOR |
|---|-----|-----|
| Build | `p[i+1] = p[i] + a[i]` | `p[i+1] = p[i] ^ a[i]` |
| Range `[l, r]` | `p[r+1] - p[l]` | `p[r+1] ^ p[l]` |
| Sentinel | `p[0] = 0` | `p[0] = 0` |

```java
// java
// LC 1310 - XOR Queries of a Subarray
// IDEA: prefix XOR; xor(l..r) = p[r+1] ^ p[l]  (XOR is its own inverse)
// time = O(n + q), space = O(n)
public int[] xorQueries(int[] arr, int[][] queries) {
    int n = arr.length;
    int[] p = new int[n + 1];
    for (int i = 0; i < n; i++) p[i + 1] = p[i] ^ arr[i];

    int[] res = new int[queries.length];
    for (int i = 0; i < queries.length; i++) {
        res[i] = p[queries[i][1] + 1] ^ p[queries[i][0]];
    }
    return res;
}
```

```python
# python
# LC 1310 - XOR Queries of a Subarray
# time = O(n + q), space = O(n)
from itertools import accumulate

def xorQueries(arr, queries):
    p = list(accumulate(arr, lambda a, b: a ^ b, initial=0))
    return [p[r + 1] ^ p[l] for l, r in queries]
```

#### Variation — **XOR bitmask as a parity fingerprint** (LC 1915)

The twist: instead of XOR-ing *values*, XOR a **1-bit-per-letter mask** so that bit `c` of the running mask = "letter `c` appeared an odd number of times so far". Then a substring `(j, i]` has all-even counts iff `mask[i] == mask[j]` — a Template-2 HashMap lookup on masks instead of sums.

```java
// java
// LC 1915 - Number of Wonderful Substrings
// IDEA: prefix XOR bitmask (parity fingerprint) + counting map, like Template 2
// time = O(10n), space = O(2^10)
public long wonderfulSubstrings(String word) {
    long[] cnt = new long[1 << 10];       // mask -> how many prefixes had it
    cnt[0] = 1;                           // empty prefix sentinel (== the {0:1} trick)
    int mask = 0;
    long res = 0;

    for (char ch : word.toCharArray()) {
        mask ^= 1 << (ch - 'a');          // flip this letter's parity bit

        res += cnt[mask];                 // all letters even  (mask ^ mask == 0)
        for (int b = 0; b < 10; b++) {
            res += cnt[mask ^ (1 << b)];  // exactly ONE letter odd
        }
        cnt[mask]++;
    }
    return res;
}
```

```python
# python
# LC 1915 - Number of Wonderful Substrings
# time = O(10n), space = O(2^10)
def wonderfulSubstrings(word):
    cnt = [0] * 1024
    cnt[0] = 1                       # empty prefix
    mask = res = 0

    for ch in word:
        mask ^= 1 << (ord(ch) - ord('a'))
        res += cnt[mask]             # 0 odd letters
        for b in range(10):
            res += cnt[mask ^ (1 << b)]   # exactly 1 odd letter
        cnt[mask] += 1

    return res
```

> **Same skeleton, other flavours:** LC 1738 (Find Kth Largest XOR Coordinate Value) is Template 5's inclusion–exclusion with `^` instead of `+`/`-`; LC 1829 (Maximum XOR for Each Query) is a running suffix XOR peeled one element at a time.

### Template 13: Sparse Difference Array via HashMap (Line Sweep) ⭐⭐⭐⭐⭐ — LC 2021

**Key Idea**: Template 4's difference array, but the coordinate space is **too large (or negative) to allocate as an array**. Swap the array for a HashMap, then walk `sorted(keys)` instead of `range(n)`.

**When to reach for it**:
- Coordinates are huge (`-10^8 <= pos <= 10^8`) → an array of size `2 * 10^8` blows memory
- Coordinates can be **negative** → array indices need an offset shift
- Only `O(n)` positions actually matter — the value between two consecutive events never changes, so **only event points can be the answer**

| | Array diff (Template 4) | HashMap diff (Template 13) |
|---|---|---|
| Storage | `[0] * (maxCoord + 2)` | `defaultdict(int)`, only 2n keys |
| Space | O(coordinate range) | **O(n)** |
| Iterate | `for i in range(n)` | `for k in sorted(d)` |
| Time | O(range + n) | **O(n log n)** (the sort) |
| Negative coords | need offset shift | **works as-is** |

**The `+1` trick**: the range `[p-r, p+r]` is **inclusive**, so the "stop" marker goes at `p + r + 1`, not `p + r`. Off-by-one here is the #1 bug in this pattern.

```python
# python
# LC 2021 - Brightest Position on Street
# IDEA: hashmap difference array + line sweep over sorted event keys
# time = O(n log n), space = O(n)
from collections import defaultdict

class Solution:
    def brightestPosition(self, lights):
        events = defaultdict(int)

        # 1) mark events: +1 where coverage starts, -1 right AFTER it ends
        for p, r in lights:
            events[p - r] += 1          # starts illuminating at (p - r)
            events[p + r + 1] -= 1      # stops illuminating AFTER (p + r)  <-- note the +1

        max_brightness = curr = ans_pos = 0

        # 2) sweep positions in ascending order (sorted keys == the prefix sum walk)
        for pos in sorted(events.keys()):
            curr += events[pos]
            # STRICT `>` + ascending order => ties keep the SMALLEST position
            if curr > max_brightness:
                max_brightness = curr
                ans_pos = pos

        return ans_pos
```

```java
// java
// LC 2021 - Brightest Position on Street
// IDEA: TreeMap keeps keys sorted, so the sweep is just an in-order walk
// time = O(n log n), space = O(n)
public int brightestPosition(int[][] lights) {
    TreeMap<Integer, Integer> events = new TreeMap<>();
    for (int[] l : lights) {
        int p = l[0], r = l[1];
        events.merge(p - r, 1, Integer::sum);
        events.merge(p + r + 1, -1, Integer::sum);
    }

    int max = 0, curr = 0, ans = 0;
    for (Map.Entry<Integer, Integer> e : events.entrySet()) {  // already ascending
        curr += e.getValue();
        if (curr > max) {          // strict > => smallest position wins ties
            max = curr;
            ans = e.getKey();
        }
    }
    return ans;
}
```

#### Two subtleties worth memorizing

1. **Why `sorted()` gives the smallest answer** — the prefix sum is only valid if events are applied left-to-right. Combined with a **strict** `>` (not `>=`), the first position reaching a new max is recorded and never overwritten by a later tie. LC 2021 explicitly asks for "the smallest one".
2. **Why the HashMap beats a sorted event list** — bucketing by key means all `+1`/`-1` at the *same* coordinate are merged **before** the sweep sees them. With a `List<int[]>` you must also think about tie-break ordering within one coordinate; with a map that problem disappears.

#### Same pattern, other problems

| Problem | LC # | What the events are | Note |
|---------|------|---------------------|------|
| Brightest Position on Street | 2021 | lamp `[p-r, p+r]` | inclusive → `+r+1` |
| Meeting Rooms II | 253 | `+1` at start, `-1` at end | end **exclusive** → no `+1` |
| Car Pooling | 1094 | `+num` on, `-num` off | fixed small range → array is fine |
| Corporate Flight Bookings | 1109 | `+seats` over `[first, last]` | inclusive → `last+1` |
| Maximum Population Year | 1854 | `+1` birth, `-1` death | death year exclusive |
| My Calendar III | 732 | booking intervals | needs a live TreeMap (online) |
| Describe the Painting | 1943 | colored segments | map value = sum of colors |

> **Rule of thumb**: coordinate range ≤ ~10^6 and non-negative → plain array (Template 4). Otherwise, or if coordinates are negative → HashMap/TreeMap (Template 13).

### Template 14: Prefix Sum on a Tree (DFS + HashMap + Backtrack) ⭐⭐⭐⭐⭐ — LC 437

**Key Idea**: a **downward path** in a tree is nothing but a *subarray of the root→node chain*. So
Template 2 (`cur - k` in a HashMap) applies unchanged — "the array" is just the DFS call stack
instead of `nums`. The single new move is that the chain is a **branch, not a prefix of one global
array**, so the map entry must be **undone when the recursion leaves the node**.

| | Array (Template 2) | Tree (Template 14) |
|---|---|---|
| "the array" | `nums[0..i]` | the root→node chain = the current DFS stack |
| running sum | `cur += nums[i]` | `cur += node.val` |
| count paths ending here | `cnt += map[cur - k]` | same |
| record this prefix | `map[cur] += 1` | same |
| **un-record it** | never — the array only grows | **`map[cur] -= 1` after both children** |
| result | # subarrays with sum `k` | # downward paths with sum `k` |

**When to reach for it** — all three must hold:

- the path must go **downward only** (parent → child), so every candidate path is `chain[i..j]`;
- the path may **start and end anywhere** — not pinned to root or to a leaf (that is what kills the
  plain root→leaf DFS of [tree_backtrack.md](./tree_backtrack.md));
- you are **counting** (or testing existence of) paths with a target sum, not maximizing over a path
  that may *bend* through a node — see [When not to use it](#when-not-to-use-it--the-path-bends-or-the-answer-is-per-subtree) below.

#### Why the undo is mandatory

Without it, a prefix left behind by a **sibling** subtree is still in the map, and a "path" that
jumps sideways across the tree gets counted. The smallest failing case, `targetSum = 1`:

```text
tree:        1              chains:   1        (prefix 1)
            / \                       1 -> 4   (prefix 5)
           4   5                       1 -> 5   (prefix 6)

correct answer = 1   (the single node `1`)

DFS pre-order, map starts {0: 1}:
  node 1 : cur=1  cnt += map[1-1=0] = 1   -> cnt=1   map{0:1, 1:1}
  node 4 : cur=5  cnt += map[5-1=4] = 0   -> cnt=1   map{0:1, 1:1, 5:1}
  << no undo here: prefix 5 stays in the map >>
  node 5 : cur=6  cnt += map[6-1=5] = 1   -> cnt=2   WRONG
                  ^ that "5" is the left branch's prefix; 4 is not an ancestor of 5
```

`map[cur] -= 1` on the way out drops prefix `5` before the right branch is entered, and the count
stays 1. The map's **non-zero** entries are then exactly the prefixes of the current node's
ancestors — at most `h` of them, which is where the `O(h)` space comes from.

> **`-= 1` is not `del`.** A decremented key stays in the dict with value `0`, so a long-running
> map can accumulate up to `O(n)` dead keys. That is harmless for correctness — a `0` count adds
> nothing to `cnt` — but if you want the `O(h)` bound to be literal, `del prefix[cur]` (Java:
> `prefix.remove(cur)`) once the count reaches `0`.

> **Python-specific**: the map is a mutable object shared by every frame, so the undo is the only
> thing scoping it. A plain `cur` (an `int`) needs no undo — it is passed by value down each call.

#### Python template

```python
# python
# LC 437 - Path Sum III
# IDEA: pre-order DFS + prefix sum HashMap + backtrack ("2-sum on the root->node chain")
# time = O(n), space = O(h)
from collections import defaultdict

class Solution:
    def pathSum(self, root, targetSum):
        prefix = defaultdict(int)
        prefix[0] = 1                 # the empty prefix: a path starting AT the root
        self.cnt = 0

        def dfs(node, cur):
            if not node:
                return

            cur += node.val                       # 1) extend the chain

            # 2) cur - old = targetSum  ->  old = cur - targetSum
            #    every recorded ancestor prefix `old` is one path ending at `node`
            self.cnt += prefix[cur - targetSum]

            prefix[cur] += 1                      # 3) publish this prefix to the subtree

            dfs(node.left, cur)
            dfs(node.right, cur)

            prefix[cur] -= 1                      # 4) BACKTRACK - leave no trace for siblings

        dfs(root, 0)
        return self.cnt
```

#### Java template

```java
// java
// LC 437 - Path Sum III
// IDEA: pre-order DFS + prefix sum HashMap + backtrack
// time = O(n), space = O(h)
public int pathSum(TreeNode root, int targetSum) {
    Map<Long, Integer> prefix = new HashMap<>();
    prefix.put(0L, 1);                       // empty prefix
    return dfs(root, 0L, targetSum, prefix);
}

private int dfs(TreeNode node, long cur, int target, Map<Long, Integer> prefix) {
    if (node == null) return 0;

    cur += node.val;                                            // 1) extend
    int res = prefix.getOrDefault(cur - target, 0);              // 2) count

    prefix.merge(cur, 1, Integer::sum);                          // 3) publish
    res += dfs(node.left,  cur, target, prefix);
    res += dfs(node.right, cur, target, prefix);
    prefix.merge(cur, -1, Integer::sum);                         // 4) BACKTRACK

    return res;
}
```

> **Use `long` for the running sum in Java.** LC 437 allows `-10^9 <= node.val <= 10^9` over up to
> 1000 nodes, so an `int` chain sum overflows. The *key* must then be `Long`, or the lookup silently
> misses.

#### Two spellings of the base case — pick one, never both

| | Sentinel (preferred) | Explicit check |
|---|---|---|
| init | `prefix = {0: 1}` | `prefix = {}` |
| count | `cnt += prefix[cur - k]` | `if cur == k: cnt += 1`<br>`cnt += prefix.get(cur - k, 0)` |
| handles "path starts at root" | the `0` entry does it | the `if` does it |
| risk | none | writing **both** double-counts every root-started path |

The sentinel is the same `prefix[0] = 0` idea as the array templates: it is the *empty* prefix, and
it is what makes a path that begins at the root need no special case.

#### Trace — LC 437, the leftmost chain

```text
root = [10,5,-3,3,2,null,11,3,-2,null,1], targetSum = 8

chain 10 -> 5 -> 3 -> 3        map = {0:1}
  10 : cur=10  need 10-8=2   map[2]=0            cnt=0   map{0:1,10:1}
   5 : cur=15  need 15-8=7   map[7]=0            cnt=0   map{...,15:1}
   3 : cur=18  need 18-8=10  map[10]=1  <-- HIT  cnt=1   map{...,18:1}
       the hit prefix 10 is node `10`, so the path is (10..18] = 5 -> 3  = 8  ✓
   3 : cur=21  need 21-8=13  map[13]=0           cnt=1
  ... unwind: 21, 18, 15, 10 each -= 1

the other two paths are found the same way: 5 -> 2 -> 1, and -3 -> 11
```

#### Complexity, and what it replaces

| Approach | Time | Space | Note |
|---|---|---|---|
| DFS from **every** node (`pathSum(root) = dfs(root) + pathSum(left) + pathSum(right)`) | O(n²) worst case, O(n log n) balanced | O(h) | the obvious first answer; fine to state, then improve |
| BFS every node + DFS from each | O(n²) | O(n) | same work, more space |
| **Template 14** | **O(n)** — one visit per node | **O(h)** — the map holds only ancestors | say the `O(h)` out loud; interviewers expect `O(n)` here |

#### Same skeleton, other problems

| Problem | LC # | What changes | Template |
|---------|------|--------------|----------|
| Path Sum III | 437 | — the canonical form | Template 14 |
| Subarray Sum Equals K | 560 | the array version; the tree adds only the undo | Template 2 |
| Number of Submatrices That Sum to Target | 1074 | the same counting map, "chain" = a fixed row pair | Template 11 + 2 |
| Path Sum | 112 | path pinned to root→leaf → **no map**, just carry `cur` | Template 14 degenerate |
| Path Sum II | 113 | root→leaf **and** collect paths → carry a list, undo it | Template 14 degenerate + backtrack |
| Binary Tree Paths | 257 | same, string instead of sum | — |
| Sum Root to Leaf Numbers | 129 | `cur = cur * 10 + val` — a running *number*, still a downward prefix | — |
| Sum of Root To Leaf Binary Numbers | 1022 | `cur = cur * 2 + val` | — |
| Path Sum IV | 666 | tree given as `depth-position-value` triples; rebuild parent links, then the same chain sum | — |
| Sum of Nodes with Even-Valued Grandparent | 1315 | carry the last **two** ancestors down instead of a sum | — |

**Swap the combining operation**, and the identity still holds as long as it is *invertible*
(see [Key Properties](#key-properties) above):

- **XOR** — count downward paths whose XOR is `k`: `cur ^= node.val`, look up `cur ^ k`
  ([Template 12](#template-12-prefix-xor---lc-1310));
- **modulo** — count downward paths whose sum is divisible by `k`: key on `cur % k`
  ([Template 3](./prefix_sum.md#template-3-modulo-prefix-sum-divisibility-problems--lc-974));
- **min / max** — does **not** work. There is no undo for `min`, so a "prefix min" cannot be
  subtracted back out; those problems are post-order instead.

#### When not to use it — the path bends, or the answer is per-subtree

If the path may turn at a node (`left → node → right`) or the quantity is a property of a *subtree*,
the chain framing is wrong and the answer is **post-order DFS returning a value up**:

| Problem | LC # | Why not prefix sum |
|---------|------|--------------------|
| Binary Tree Maximum Path Sum | 124 | the path bends; maximize per node with `left + val + right` |
| Diameter of Binary Tree | 543 | bends; `leftDepth + rightDepth` |
| Longest Univalue Path | 687 | bends |
| Most Frequent Subtree Sum | 508 | a HashMap on **subtree** sums, computed bottom-up — not a chain prefix |
| Count Nodes Equal to Average of Subtree | 2265 | needs `(sum, count)` returned up from each subtree |

> **Interview cue**: "does not need to start or end at the root or a leaf, but must go **downwards**"
> → Template 14. "Path may pass through a node" / "any two nodes" → post-order (or LCA, see
> [tree_lca_distance.md](./tree_lca_distance.md)).

### Templates 9-14 — Problem Index

| Problem | LC # | Key Technique | Difficulty | Template |
|---------|------|---------------|------------|----------|
| Maximum Points You Can Obtain from Cards | 1423 | total − min fixed window | Medium | Template 9 |
| Minimum Operations to Reduce X to Zero | 1658 | total − longest window with sum = total−x | Medium | Template 9 variant |
| Shortest Subarray with Sum at Least K | 862 | prefix sum + monotonic deque | Hard | Template 10 |
| Minimum Size Subarray Sum | 209 | positives only → plain sliding window | Medium | Template 10 (contrast) |
| Max Sum of Rectangle No Larger Than K | 363 | row-pair compression + sorted set | Hard | Template 11 |
| Number of Submatrices That Sum to Target | 1074 | row-pair compression + HashMap | Hard | Template 11 + 2 |
| XOR Queries of a Subarray | 1310 | prefix XOR range query | Medium | Template 12 |
| Number of Wonderful Substrings | 1915 | prefix XOR bitmask parity + count | Medium | Template 12 variant |
| Find Kth Largest XOR Coordinate Value | 1738 | 2D prefix XOR (inclusion–exclusion) | Medium | Template 12 + 5 |
| Number of Sub-arrays With Odd Sum | 1524 | prefix parity count (Template 3 with k = 2) | Medium | Template 3 variant |
| Max Consecutive Ones III | 1004 | 0/1 transform, longest window with ≤ k zeros | Medium | Template 6 / sliding window |
| Number of Good Ways to Split a String | 1525 | prefix distinct-count vs suffix distinct-count | Medium | Template 1 (prefix + suffix) |
| Minimum Number of Operations to Move All Balls to Each Box | 1769 | left→right and right→left running (count, cost) sweep | Medium | Template 7 variant |
| Plates Between Candles | 2055 | prefix plate count + nearest-candle index arrays | Medium | Template 1 (offline queries) |
| Find Good Days to Rob the Bank | 2100 | prefix non-increasing / suffix non-decreasing run lengths | Medium | Template 1 variant |
| Product of the Last K Numbers | 1352 | prefix **product** (reset the list on a 0) | Medium | Template 1 variant |
| Brightest Position on Street | 2021 | hashmap diff array + sorted-key sweep | Medium | Template 13 |
| Describe the Painting | 1943 | hashmap diff array, value = color sum | Medium | Template 13 |
| My Calendar III | 732 | TreeMap diff array, online max overlap | Hard | Template 13 |
| Path Sum III | 437 | prefix sum on the root→node chain + backtrack | Medium | Template 14 |
| Path Sum | 112 | root→leaf chain sum, no map needed | Easy | Template 14 (degenerate) |
| Path Sum II | 113 | root→leaf chain + path backtracking | Medium | Template 14 (degenerate) |
| Sum Root to Leaf Numbers | 129 | running *number* down the chain (`cur*10 + val`) | Medium | Template 14 (degenerate) |
| Path Sum IV | 666 | rebuild the tree from `depth-pos-val`, then chain sum | Medium | Template 14 (degenerate) |

> **Cross-reference:** the exact-sum HashMap complement (`prefix_sum - k`) is also written up as a "2-sum on prefix sums" template in [`n_sum.md`](./n_sum.md) — see Template 2 above for the version used throughout this doc. Template 14 is that same complement run along a DFS stack; the tree-side view of it lives in [`tree.md`](./tree.md) and [`binary_tree.md`](./binary_tree.md).
