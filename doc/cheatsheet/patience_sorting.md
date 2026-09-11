# Patience Sorting — LIS in O(N log N)

> **Scope** — The card-game algorithm behind `O(N log N)` longest increasing subsequence: the piles, the `tails` array they collapse to, recovering the subsequence itself, and the problem family that reduces to it.
> **See also**: [binary_search.md](./binary_search.md) §1.5 — the lower-bound template the scan runs on, and the proof that one write per element is the *complete* DP update; [binary_search_examples.md](./binary_search_examples.md) §18 — the worked LC 300 / LC 354 solutions; [dp_pattern.md](./dp_pattern.md) — the `O(n²)` LIS DP this replaces, and the LIS-shaped DPs that cannot be replaced; [sort.md](./sort.md) — the sorting-algorithm neighbours.

- **Core idea**: Deal the array out as a game of patience — each card onto the leftmost pile whose top is `>= card`, a new pile if there is none — and the **number of piles is the LIS length**
- **When to use it**: A longest increasing / chainable run, when only its **length** is wanted and `O(n²)` DP is too slow
- **Key LeetCode problems**: LC 300, LC 334, LC 354, LC 1964, LC 2111, LC 1713, LC 1671
- **Data structures**: one sorted array of pile tops (`tails`); plus parent pointers if the subsequence itself is wanted
- **Typical states**: `tails[k]` = the smallest possible ending value of an increasing run of length `k + 1`

**Time Complexity:** O(N log N) — N cards × one binary search over at most N pile tops
**Space Complexity:** O(N)

**Implementation**: [`algorithm/python/patience_sorting.py`](../../algorithm/python/patience_sorting.py) — all five shapes below, with a randomized cross-check against the `O(n²)` DP.

## LeetCode Problem Lists

- [Binary Search](https://leetcode.com/problem-list/binary-search/)
- [Dynamic Programming](https://leetcode.com/problem-list/dynamic-programming/)
- [Greedy](https://leetcode.com/problem-list/greedy/)

## 0) Concept

### 0-0) Core Principle — Deal the Array Into Piles ⭐⭐⭐⭐⭐

Three rules, played left to right over the input:

1. Place the card on the **leftmost pile whose top card is `>= card`**.
2. If no pile qualifies, **start a new pile** to the right.
3. When the deal ends, **the number of piles is the LIS length**.

```text
nums = [10, 9, 2, 5, 3, 7]

10 -> new pile           | 10 |
 9 -> 10 >= 9, pile 0    | 10 9 |
 2 ->  9 >= 2, pile 0    | 10 9 2 |
 5 ->  2 <  5, new pile  | 10 9 2 | 5 |
 3 ->  5 >= 3, pile 1    | 10 9 2 | 5 3 |
 7 ->  3 <  7, new pile  | 10 9 2 | 5 3 | 7 |

3 piles -> LIS length 3   (e.g. [2, 3, 7])
```

Two facts make this an algorithm rather than a card trick:

- **Pile tops increase from left to right.** A card that lands on a pile is *smaller or equal* to that pile's top and *strictly greater* than the top to its left; a card that starts a new pile is greater than every top. Either way the tops stay sorted — which means "leftmost pile whose top is `>= x`" is a plain `lower_bound` (`bisect_left`) and costs `O(log P)`, not `O(P)`.
- **Only the tops are ever read.** Collapse the piles to their tops and you have the `tails` array everyone writes in an interview:

```text
piles  | 10 9 2 | 5 3 | 7 |
tops   [   2,     3,    7 ]   ==  tails
                                  tails[k] = smallest tail of a run of length k+1
```

### 0-1) Types

1. **Length only** — the bare scan, answer `len(tails)` (LC 300)
2. **Length at every index** — report the insertion position as you go, one answer per element (LC 1964)
3. **The subsequence itself** — the same scan plus parent pointers (§1-4)
4. **2-D, after a sort** — sort one dimension so the other becomes a 1-D LIS (LC 354)
5. **A reduction** — a different problem rewritten as LIS: LCS with one side distinct, `k` interleaved sequences (LC 1713, LC 2111)
6. **Non-decreasing instead of strict** — one character, `bisect_right` (§1-2)

### 0-2) Why the Pile Count *Is* the LIS ⭐⭐⭐⭐⭐

Two directions, each one line, and they are the whole correctness proof:

```text
LIS <= piles :  a pile read in DEALING ORDER (bottom -> top) is NON-INCREASING,
                so an increasing subsequence can take at most ONE card from each
                pile -> it cannot be longer than the number of piles

LIS >= piles :  a card on pile k landed there because pile k-1 already had a
                SMALLER top; chain that back pile by pile and you get an actual
                increasing subsequence of length k + 1
                -> the last pile witnesses a run as long as the pile count
```

Squeezed from both sides, `piles == LIS`. Naming it in an interview is worth a signal: this is **Dilworth's theorem** — the piles form a *minimum non-increasing cover* of the array, and the minimum size of such a cover equals the longest increasing subsequence.

> The same fact stated in `tails` terms — the sorted-array invariant, and the proof that
> exactly one slot can ever improve per element — is in
> [binary_search.md](./binary_search.md) §1.5. Pile view for the *why*, `tails` view for
> the *code*.

### 0-3) Pattern — When It Applies ⭐⭐⭐⭐⭐

Reach for patience sorting when **all three** hold:

| Condition | Why it matters |
|---|---|
| The answer is a **length** (or `n − length`), not a count of ways and not a weighted sum | One value per pile can only carry "how long", never "how many" or "how much" |
| "Chainable" is a **total order** on a single key — `<` on a number, or on a key you can sort by first | A pile top has to summarise *everything* about the progress so far |
| The obvious solution is `dp[i] = max(dp[j]) + 1` over `j < i`, and you need it faster | That inner `max` is exactly what the lower bound replaces |

**The discriminator, in one line**: `tails` works iff progress on a chain is summarisable by **one comparable number** and you only want **how long**.

Fail any of the three and you are back to `O(n²)` DP or a Fenwick tree — see the traps table in §2-8.

## 1) General form

### 1-1) Basic OP — the `tails` Scan ⭐⭐⭐⭐⭐

```python
# python
# IDEA: patience sorting - tails[k] = smallest tail of an increasing run of
#       length k+1; lower-bound it to find the pile, then extend or overwrite
# time = O(n log n), space = O(n)
def lis_length(nums):
    tails = []

    for num in nums:
        # lower bound : first index with tails[l] >= num
        l, r = 0, len(tails) - 1
        while l <= r:
            mid = l + (r - l) // 2
            if tails[mid] < num:
                l = mid + 1      # NOTE !!! strict < -> an equal tail FAILS this test,
                                 #          so it takes the else and pushes r left
            else:
                r = mid - 1

        if l == len(tails):
            tails.append(num)    # beats every top -> a NEW pile, a longer run exists
        else:
            tails[l] = num       # same length, cheaper tail -> overwrite

    return len(tails)            # NOTE !!! the LENGTH is the answer, not the contents
```

```python
# python - the same scan, with the library doing the search
# IDEA: bisect_left IS the lower bound written out above
# time = O(n log n), space = O(n)
import bisect

def lis_length_bisect(nums):
    tails = []
    for num in nums:
        l = bisect.bisect_left(tails, num)   # first tail >= num
        if l == len(tails):
            tails.append(num)
        else:
            tails[l] = num
    return len(tails)
```

```java
// java
// IDEA: same scan on a fixed array - `size` is the pile count, so tails[0..size)
//       is the sorted window the lower bound runs on
// time = O(n log n), space = O(n)
public int lisLength(int[] nums) {
    int[] tails = new int[nums.length];
    int size = 0;

    for (int num : nums) {
        int l = 0, r = size;
        while (l < r) {                     // lower_bound over tails[0..size)
            int mid = l + (r - l) / 2;
            if (tails[mid] < num) l = mid + 1;
            else r = mid;
        }
        tails[l] = num;                     // overwrite ...
        if (l == size) size++;              // ... or extend, when l lands past the end
    }
    return size;
}
```

### 1-2) Strict vs Non-Decreasing — the One-Character Change

The most common wrong answer on this family is the wrong `bisect`:

| Wanted | Query on `tails` | Python | Effect on an equal value |
|---|---|---|---|
| **strictly** increasing (LC 300) | first tail `>= num` | `bisect_left` | lands *on* the equal tail → **overwrites** it, no growth |
| **non-decreasing** (duplicates allowed) | first tail `> num` | `bisect_right` | lands *past* the equal tail → **extends** the run |
| non-increasing / decreasing | negate the input, then the above | `bisect_*` on `-num` | — |

```python
# python - longest NON-DECREASING subsequence
# IDEA: identical scan, bisect_right so an equal value extends instead of replacing
# time = O(n log n), space = O(n)
import bisect

def lnds_length(nums):
    tails = []
    for num in nums:
        l = bisect.bisect_right(tails, num)   # first tail > num
        if l == len(tails):
            tails.append(num)
        else:
            tails[l] = num
    return len(tails)
```

```text
nums = [2, 2, 2, 3]
bisect_left  -> tails [2, 3]           -> 2   (strictly increasing)
bisect_right -> tails [2, 2, 2, 3]     -> 4   (non-decreasing)
```

### 1-3) Keeping the Piles

Rarely needed for a length, but it is the honest version of the algorithm — and it is what makes patience *sorting* a sort (merge the piles afterwards, the way Timsort merges runs):

```python
# python
# IDEA: same placement rule, but append to the pile instead of only tracking its top
# time = O(n log n), space = O(n)
import bisect

def patience_piles(nums):
    piles = []       # piles[k] : the cards on pile k, top card LAST
    tops = []        # tops[k] == piles[k][-1], kept flat so bisect can search it

    for num in nums:
        l = bisect.bisect_left(tops, num)
        if l == len(piles):
            piles.append([num])
            tops.append(num)
        else:
            piles[l].append(num)
            tops[l] = num

    return piles     # len(piles) == LIS length; each pile is non-increasing
                     # bottom -> top, i.e. in the order it was dealt
```

### 1-4) Recovering the Subsequence, Not Just Its Length ⭐⭐⭐⭐

`tails` is **not** a subsequence — only its length means anything:

```text
nums  = [3, 4, 5, 1]
tails = [1, 4, 5]      <- 1 sits at index 0 but arrives LAST in the input
len   = 3              <- correct anyway: the LIS is [3, 4, 5]
```

To hand back the actual run, remember *who each card landed on*: the tail of the pile to its left at placement time.

```python
# python
# IDEA: same scan + two side arrays - the input index of each pile's top, and a
#       parent pointer per element; then walk back from the last pile's top
# time = O(n log n), space = O(n)
import bisect

def lis_reconstruct(nums):
    if not nums:
        return []

    tails = []                    # tail VALUES (the array bisect searches)
    tails_idx = []                # index in nums of each tail
    prev = [-1] * len(nums)

    for i, num in enumerate(nums):
        l = bisect.bisect_left(tails, num)

        # whatever currently ends the run of length l becomes num's predecessor
        if l > 0:
            prev[i] = tails_idx[l - 1]

        if l == len(tails):
            tails.append(num)
            tails_idx.append(i)
        else:
            tails[l] = num
            tails_idx[l] = i

    out = []
    i = tails_idx[-1]             # the top of the last pile ends a longest run
    while i != -1:
        out.append(nums[i])
        i = prev[i]
    return out[::-1]
```

- The walk-back returns **a** longest subsequence, not a canonical one:
  `[10,9,2,5,3,7,101,18]` gives `[2,3,7,18]`, because `18` replaced `101` as the length-4 tail.
- `prev[i]` is safe even though `tails_idx[l-1]` is later overwritten: it was recorded **at placement time**, when that element really did precede `nums[i]`.

### 1-5) Complexity and the Pitfalls That Actually Bite

| | |
|---|---|
| Time | `O(n log n)` — one `bisect` over at most `n` tops per element |
| Space | `O(n)` for `tails`; `O(n)` more for reconstruction; `O(n)` total for the full piles |
| Worst case | An increasing input makes `n` piles, a decreasing one makes 1 — both still `O(n log n)` |

- ❌ Reading `tails` as the answer subsequence (§1-4).
- ❌ `bisect_left` when the problem allows duplicates, or `bisect_right` when it does not (§1-2).
- ❌ Sorting the input "to help". Sorting destroys the order the subsequence is defined over — the only legal sort is the *deliberate* one in LC 354, where the second dimension is what the scan then runs on.
- ❌ Reaching for it when the question is "how many" (LC 673) or "maximum sum" (LC 2926) — see §2-8.

## 2) LC Example

### 2-1) Longest Increasing Subsequence — LC 300 ⭐⭐⭐⭐⭐

The bare scan of §1-1, answer `len(tails)`. The `O(n²)` DP is the expected first answer and the `O(n log n)` scan is the follow-up; the worked write-up of both, with the dry-run table, is in [binary_search_examples.md](./binary_search_examples.md) §18.

### 2-2) Increasing Triplet Subsequence — LC 334 ⭐⭐⭐⭐

The famous two-variable trick **is** this algorithm with `tails` capped at two piles: `first`/`second` are `tails[0]`/`tails[1]`, and "a third pile would open" is the answer.

```python
# python
# IDEA: tails of size <= 2 - first/second ARE tails[0]/tails[1]; the moment a
#       value beats both, a third pile (an increasing triplet) exists
# time = O(n), space = O(1)
class Solution:
    def increasingTriplet(self, nums):
        first = second = float('inf')
        for num in nums:
            if num <= first:
                first = num          # tails[0] = a cheaper length-1 tail
            elif num <= second:
                second = num         # tails[1] = a cheaper length-2 tail
            else:
                return True          # would append tails[2] -> length 3 exists
        return False
```

No binary search: over two slots a linear scan *is* the lower bound, which is why this looks like a different algorithm and is not.

### 2-3) Russian Doll Envelopes — LC 354 ⭐⭐⭐⭐⭐

LIS in two dimensions. Sort widths **ascending** and, on ties, heights **descending**, then run the scan on the heights alone. The tie rule is the entire problem: with heights descending, two envelopes of equal width can never both be picked, because the later one's height is smaller and so cannot extend the earlier one.

Code — and the same trick applied to LC 1996 — in [binary_search_examples.md](./binary_search_examples.md) §18.

### 2-4) Longest Valid Obstacle Course at Each Position — LC 1964 ⭐⭐⭐⭐

The scan already knows the answer *at every step*: the pile a card lands on is the length of the longest run ending at that card. Report it instead of only the final count.

```python
# python
# IDEA: non-decreasing variant (bisect_right); the landing index + 1 IS the
#       longest valid course ending at this obstacle
# time = O(n log n), space = O(n)
import bisect

class Solution:
    def longestObstacleCourseAtEachPosition(self, obstacles):
        tails, ans = [], []
        for h in obstacles:
            i = bisect.bisect_right(tails, h)   # heights may repeat -> bisect_right
            if i == len(tails):
                tails.append(h)
            else:
                tails[i] = h
            ans.append(i + 1)                   # NOTE !!! per-index answer, no extra pass
        return ans
```

### 2-5) Minimum Operations to Make the Array K-Increasing — LC 2111 ⭐⭐⭐⭐

`arr[i-k] <= arr[i]` couples only indices in the same residue class mod `k`, so the array is really `k` independent sequences. Keep the longest non-decreasing subsequence of each and replace everything else.

```python
# python
# IDEA: k independent slices arr[r::k]; keep each one's longest NON-DECREASING
#       subsequence (values may repeat) and pay 1 for every element kept out
# time = O(n log n), space = O(n)
import bisect

class Solution:
    def kIncreasing(self, arr, k):
        def lnds(seq):
            tails = []
            for x in seq:
                i = bisect.bisect_right(tails, x)
                if i == len(tails):
                    tails.append(x)
                else:
                    tails[i] = x
            return len(tails)

        return sum(len(arr[r::k]) - lnds(arr[r::k]) for r in range(k))
```

### 2-6) Minimum Operations to Make a Subsequence — LC 1713 ⭐⭐⭐⭐⭐

The one worth recognising cold: it reads as LCS, which is `O(n·m)` — but `target` has **distinct** values, so rewriting each `arr` value as its position in `target` turns "common subsequence" into "increasing subsequence".

```python
# python
# IDEA: distinct target -> map arr values to target indices; a common subsequence
#       of (target, arr) is exactly an INCREASING subsequence of the mapped list
# time = O(n log n), space = O(n)
import bisect

class Solution:
    def minOperations(self, target, arr):
        pos = {v: i for i, v in enumerate(target)}   # value -> index in target

        tails = []
        for x in arr:
            if x not in pos:                          # not in target -> unusable
                continue
            p = pos[x]
            i = bisect.bisect_left(tails, p)          # strictly increasing indices
            if i == len(tails):
                tails.append(p)
            else:
                tails[i] = p

        return len(target) - len(tails)               # insert whatever the LIS missed
```

The reduction needs the distinctness: with duplicates a value maps to several indices and "increasing" no longer captures "common".

### 2-7) Minimum Number of Removals to Make Mountain Array — LC 1671 ⭐⭐⭐⭐

A mountain is an increasing run meeting a decreasing one at a shared peak, so run the scan **twice** — forwards for the run ending at `i`, backwards for the run starting at `i`.

```python
# python
# IDEA: left[i] = LIS ending at i, right[i] = LIS starting at i (the same scan on
#       the reversed array); a peak needs both sides > 1, so keep the best sum - 1
# time = O(n log n), space = O(n)
import bisect

class Solution:
    def minimumMountainRemovals(self, nums):
        def lis_ending_at_each(seq):
            out, tails = [], []
            for x in seq:
                i = bisect.bisect_left(tails, x)     # strictly increasing
                if i == len(tails):
                    tails.append(x)
                else:
                    tails[i] = x
                out.append(i + 1)
            return out

        left = lis_ending_at_each(nums)
        right = lis_ending_at_each(nums[::-1])[::-1]

        # NOTE !!! a peak cannot be an endpoint -> both sides must exceed 1
        best = max(l + r - 1
                   for l, r in zip(left, right)
                   if l > 1 and r > 1)
        return len(nums) - best
```

### 2-8) The Classics at a Glance

**Solved by the scan itself** (sometimes after a deliberate sort):

| LC # | Problem | What changes |
|------|---------|--------------|
| **300** | Longest Increasing Subsequence | Baseline — `bisect_left`, answer `len(tails)` |
| **334** | Increasing Triplet Subsequence | `tails` capped at 2 → two variables, `O(1)` space |
| **354** | Russian Doll Envelopes | Sort `(w asc, h desc)`, then the scan on heights |
| **646** | Maximum Length of Pair Chain | Same longest-chain question; sort-by-end greedy is simpler but this works |
| **435** | Non-overlapping Intervals | `n −` the LC 646 chain |
| **1964** | Longest Valid Obstacle Course at Each Position | `bisect_right`, and report the landing index at every step |
| **2111** | Minimum Operations to Make the Array K-Increasing | `k` residue classes, non-decreasing variant each |
| **1671** | Minimum Number of Removals to Make Mountain Array | The scan run forwards and backwards |
| **1713** | Minimum Operations to Make a Subsequence | LCS with a distinct side → LIS on mapped indices |

**Cousins** — still "binary search a structure you maintain", but the stored value is a DP result rather than a tail: LC 1235 (Maximum Profit in Job Scheduling), LC 1751 (Maximum Number of Events That Can Be Attended II), LC 981, LC 528 — all in [binary_search_examples.md](./binary_search_examples.md).

**Traps** — LIS-shaped, but not this algorithm:

| LC # | Problem | Why `tails` fails | Use instead |
|------|---------|-------------------|-------------|
| **673** | Number of Longest Increasing Subsequence | A **count**, not a length — one tail per length cannot carry multiplicities | `O(n²)` DP with a parallel count array, or a BIT over values |
| **368** | Largest Divisible Subset | Divisibility is **not a total order**, so no single value summarises a chain | `O(n²)` DP + parent pointers |
| **1691** | Maximum Height by Stacking Cuboids | Chaining needs all three dimensions `<=` — the same non-total-order failure | Sort each cuboid's dims, sort the list, `O(n²)` DP |
| **1027** | Longest Arithmetic Subsequence | State is `(index, difference)` | Hash-map DP |
| **1218** | Longest Arithmetic Subsequence of Given Difference | Chain is keyed by value, not by order | One hash map, `O(n)` |
| **2926** | Maximum Balanced Subsequence Sum | Maximises a **sum**, so the per-length optimum is not a single comparable tail | Fenwick tree over prefix maxima |

## Summary

- **Same three rules every time**: leftmost pile with top `>= x`, else a new pile, answer = pile count. Collapse the piles to their tops and you have `tails` and a `lower_bound`.
- **Correctness is two lines**: a pile is non-increasing, so `LIS <= piles`; a card's chain back through the piles is increasing, so `LIS >= piles` (Dilworth).
- **`bisect_left` strict, `bisect_right` non-decreasing** — the most common bug on the whole family.
- **The landing index is per-element information** — LC 1964, LC 1671 need no second pass.
- **A length, a total order, one comparable key.** Lose any of the three and the answer is `O(n²)` DP or a Fenwick tree, not a cleverer `bisect`.

### References

- [Patience sorting (Wikipedia)](https://en.wikipedia.org/wiki/Patience_sorting)
- [Longest increasing subsequence (Wikipedia)](https://en.wikipedia.org/wiki/Longest_increasing_subsequence)
- [Dilworth's theorem (Wikipedia)](https://en.wikipedia.org/wiki/Dilworth%27s_theorem)
- [`algorithm/python/patience_sorting.py`](../../algorithm/python/patience_sorting.py) — runnable, with tests
