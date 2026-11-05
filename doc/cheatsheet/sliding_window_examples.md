# Sliding Window — Worked Examples

> **Scope** — The worked sliding-window LeetCode problems that are not a template's anchor, one problem card each, filed under the template in the main sheet it reduces to; the templates, the concepts and the decision tables stay in the main sliding-window sheet.
> **See also**: [sliding_window.md](./sliding_window.md) — the seven canonical templates each example reduces to, and the decision table that picks between them; [sliding_window_advanced.md](./sliding_window_advanced.md) — the rarer window shapes (exactly-K with resets, word-level, several windows at once); [hash_map.md](./hash_map.md) — the frequency map most of these windows carry; [2_pointers_examples.md](./2_pointers_examples.md) — the converging-pointer catalogue.

## LeetCode Problem Lists

- [Sliding Window](https://leetcode.com/problem-list/sliding-window/)
- [Two Pointers](https://leetcode.com/problem-list/two-pointers/)
- [Hash Table](https://leetcode.com/problem-list/hash-table/)
- [String](https://leetcode.com/problem-list/string/)

## Overview

Every problem here is one of the templates in [sliding_window.md](./sliding_window.md) with one
slot filled differently: the window state, the validity test, or what is recorded. Each card
names that template and the one step it adds, so read the template first. The templates' own
anchor problems (LC 643, 1004, 3, 209, 76, 992, 4067) are worked in their cards there and are
not restated here.

## Index

| Pattern | Problems here (easy → hard) | Core ⭐ |
|---|---|---|
| [Template 1 — Fixed-Size Window](./sliding_window.md#template-1-fixed-size-window--lc-643-) | LC 1151 | — |
| [Template 3 — Longest Window Satisfying P](./sliding_window.md#template-3-longest-window-satisfying-p--lc-3-) | LC 424 · LC 1838 | LC 424 |
| [Template 6 — Exactly K via At-Most Subtraction](./sliding_window.md#template-6-exactly-k-via-at-most-subtraction--lc-992-) (its counting slot) | LC 413 · LC 713 | — |

These sliding-window problems are worked in another sheet; each is solved there by the same
approach, so it is not repeated here.

| LC | Problem | Lives in |
|---|---|---|
| 438 | Find All Anagrams in a String | [hash_map § Template 5](./hash_map.md#template-5-fixed-window-character-counts--lc-438) — a fixed window compared by letter counts |
| 567 | Permutation in String | [2_pointers § Template 8](./2_pointers.md#template-8-grow-right-then-shrink-left--lc-567) — the same window, returning on the first match |
| 763 | Partition Labels | [string § Template 8](./string.md#template-8-greedy-partition-by-last-occurrence--lc-763-) — a greedy cut at the last occurrence, not a shrinking window |
| 1004 | Max Consecutive Ones III | [sliding_window § Template 2](./sliding_window.md#template-2-grow-then-shrink-the-while-invariant--lc-1004-) — the template's anchor problem |

## Fixed-Size Window Examples

### Minimum Swaps to Group All 1's Together — LC 1151

> **Key point** — the 1s end up in some block of exactly `ones = sum(data)` cells, and each 0 in
> that block costs one swap. So the answer is `ones − (most 1s inside any window of width ones)`.

**Reduces to**: [sliding_window § Template 1](./sliding_window.md#template-1-fixed-size-window--lc-643-) — the window width is derived from the input (`ones`), and the score is a count of 1s rather than a sum.

```python
# python
# LC 1151 - Minimum Swaps to Group All 1's Together
# IDEA: fixed window of width ones = sum(data); the window holding the most 1s needs the fewest swaps
# time = O(n), space = O(1)
def minSwaps(data):
    ones = sum(data)
    if ones == 0:
        return 0
    in_window = 0                         # 1s inside the current window
    best = 0
    for right in range(len(data)):
        in_window += data[right]
        if right >= ones:                 # window is one too wide: drop the cell that fell off
            in_window -= data[right - ones]
        if right >= ones - 1:             # window is exactly `ones` wide
            best = max(best, in_window)
    return ones - best
```

**Traps**
- All zeros (`[0, 0, 0]`): `ones = 0` gives a zero-width window, and `data[right - 0]` would
  evict the cell just added; return 0 before the loop.

**Similar** — LC 2134 *(the same on a circular array: run the window over `data + data`)*

## Longest Window Examples

### Longest Repeating Character Replacement — LC 424 ⭐⭐⭐⭐⭐

> **Key point** — a window can become one letter with at most `k` replacements exactly when
> `len − maxFreq ≤ k`. `maxFreq` is never lowered on a shrink: a stale value can leave the window
> sitting on an invalid stretch, but the window then only slides (never grows), so `best` is
> only raised once a genuinely higher `maxFreq` makes a longer valid window possible.

**Reduces to**: [sliding_window § Template 3](./sliding_window.md#template-3-longest-window-satisfying-p--lc-3-) — the validity test is `len − maxFreq ≤ k` instead of "no repeats".

```python
# python
# LC 424 - Longest Repeating Character Replacement
# IDEA: grow right; while the window needs more than k replacements, shrink left; maxFreq only rises
# time = O(n), space = O(1)   (26 uppercase letters)
def characterReplacement(s, k):
    count = {}
    left = 0
    max_freq = 0                          # highest count of one letter seen in any window so far
    best = 0
    for right in range(len(s)):
        ch = s[right]
        count[ch] = count.get(ch, 0) + 1
        max_freq = max(max_freq, count[ch])
        while (right - left + 1) - max_freq > k:   # more than k letters to replace
            count[s[left]] -= 1
            left += 1
        best = max(best, right - left + 1)
    return best
```

```java
// java
// LC 424 - Longest Repeating Character Replacement
// IDEA: grow right; while the window needs more than k replacements, shrink left; maxFreq only rises
// time = O(n), space = O(1)
public int characterReplacement(String s, int k) {
    int[] count = new int[26];
    int left = 0;
    int maxFreq = 0;
    int best = 0;
    for (int right = 0; right < s.length(); right++) {
        int ch = s.charAt(right) - 'A';
        count[ch]++;
        maxFreq = Math.max(maxFreq, count[ch]);
        while ((right - left + 1) - maxFreq > k) {   // more than k replacements needed
            count[s.charAt(left) - 'A']--;
            left++;
        }
        best = Math.max(best, right - left + 1);
    }
    return best;
}
```

**Second approach — one window per letter.** Run Template 3 once for each distinct letter,
asking "can this window become all `letter`?". It is O(26·n) instead of O(n), but it has no
stale `maxFreq` to justify, so it is the easier one to argue correct under pressure.

```python
# python
# LC 424 - Longest Repeating Character Replacement (one window per letter)
# IDEA: for each letter, the longest window whose non-letter count is <= k
# time = O(26 * n), space = O(26)
def characterReplacement_per_letter(s, k):
    best = 0
    for letter in set(s):
        left = 0
        matches = 0                       # copies of `letter` inside the window
        for right in range(len(s)):
            if s[right] == letter:
                matches += 1
            while (right - left + 1) - matches > k:
                if s[left] == letter:
                    matches -= 1
                left += 1
            best = max(best, right - left + 1)
    return best
```

**Traps**
- Recording `best` before the shrink measures an invalid window: `"ABAB", k = 0` returns 2
  instead of 1.
- Recomputing `max(count.values())` on every step is also correct, but it is O(26) per step;
  lowering `max_freq` on a shrink buys nothing, because a stale value never inflates `best`.

**Similar** — LC 1004 *(binary alphabet: count zeros instead of `len − maxFreq`)* · LC 2024

### Frequency of the Most Frequent Element — LC 1838

> **Key point** — after sorting, the cheapest target for a window is its rightmost value, and the
> cost to raise the whole window to it is `nums[right] · len − windowSum`. That cost only grows
> as the window grows on the right, so it is a Template 3 validity test.

**Reduces to**: [sliding_window § Template 3](./sliding_window.md#template-3-longest-window-satisfying-p--lc-3-) — after an O(n log n) sort, with validity `nums[right] · len − windowSum ≤ k`.

```python
# python
# LC 1838 - Frequency of the Most Frequent Element
# IDEA: sort; the longest window whose cost to level up to nums[right] is <= k
# time = O(n log n), space = O(n)   (the sorted copy; the window itself is O(1))
def maxFrequency(nums, k):
    nums = sorted(nums)
    left = 0
    window_sum = 0
    best = 0
    for right in range(len(nums)):
        window_sum += nums[right]
        # raising every value in [left, right] to nums[right] costs this many increments
        while nums[right] * (right - left + 1) - window_sum > k:
            window_sum -= nums[left]
            left += 1
        best = max(best, right - left + 1)
    return best
```

```java
// java
// LC 1838 - Frequency of the Most Frequent Element
// IDEA: sort; the longest window whose cost to level up to nums[right] is <= k
// time = O(n log n), space = O(1) extra
public int maxFrequency(int[] nums, int k) {
    Arrays.sort(nums);
    int left = 0;
    long windowSum = 0;
    int best = 0;
    for (int right = 0; right < nums.length; right++) {
        windowSum += nums[right];
        // cost to raise every value in the window to nums[right]
        while ((long) nums[right] * (right - left + 1) - windowSum > k) {
            windowSum -= nums[left];
            left++;
        }
        best = Math.max(best, right - left + 1);
    }
    return best;
}
```

**Traps**
- Starting `right` at 1 with `windowSum = 0` leaves `nums[0]` out of the sum, so every cost is
  overstated: `[1, 2, 4], k = 5` returns 2 instead of 3. (The Java copy here did exactly that.)
- `nums[right] * len` overflows `int` at `nums[i] = 10⁵, n = 10⁵`: multiply in `long`.

## Exactly-K Counting Examples

### Arithmetic Slices — LC 413

> **Key point** — an arithmetic run that ends at `i` and has `run + 2` elements contains exactly
> `run` slices ending at `i` (lengths 3 … run + 2). So keep `run`, add it at every index that
> extends the run, and reset it to 0 when the difference changes.

**Reduces to**: [sliding_window § Template 6](./sliding_window.md#template-6-exactly-k-via-at-most-subtraction--lc-992-) (its counting slot) — the window is the current run, it is reset rather than shrunk when the difference changes, and each step adds `len − 2` instead of `len`, because a slice needs three elements.

```python
# python
# LC 413 - Arithmetic Slices
# IDEA: run = slices ending at i; it grows by 1 while the difference holds and resets when it breaks
# time = O(n), space = O(1)
def numberOfArithmeticSlices(nums):
    total = 0
    run = 0                               # arithmetic slices that end at index i
    for i in range(2, len(nums)):
        if nums[i] - nums[i - 1] == nums[i - 1] - nums[i - 2]:
            run += 1                      # every slice ending at i-1, extended, plus the new length-3 one
            total += run
        else:
            run = 0                       # the difference changed: no slice crosses i-1..i
    return total
```

**Traps**
- Fewer than three elements (`[1]`, `[1, 2]`): the loop never runs and the answer is 0.
- Looking back from every `i` until the difference breaks is also correct, but it is O(n²) on
  one long run (`[1, 2, …, n]`); `run` carries that count forward instead.

**Similar** — LC 446 *(subsequences, not subarrays: DP over a map of differences, see [dp.md](./dp.md))*

### Subarray Product Less Than K — LC 713

> **Key point** — with positive values the product only grows as the window grows, so "product
> < k" is already an at-most condition: after the shrink, every subarray ending at `right` that
> starts in `[left, right]` is valid, which is `right − left + 1` of them.

**Reduces to**: [sliding_window § Template 6](./sliding_window.md#template-6-exactly-k-via-at-most-subtraction--lc-992-) — only the `at_most` half; the condition is already "at most", so there is no subtraction.

```python
# python
# LC 713 - Subarray Product Less Than K
# IDEA: shrink while product >= k; every start in [left, right] gives one valid subarray ending at right
# time = O(n), space = O(1)
def numSubarrayProductLessThanK(nums, k):
    if k <= 1:
        return 0                          # products of positive integers are never < 1
    left = 0
    product = 1
    count = 0
    for right in range(len(nums)):
        product *= nums[right]
        while product >= k:
            product //= nums[left]        # exact: nums[left] divides the product
            left += 1
        count += right - left + 1
    return count
```

```java
// java
// LC 713 - Subarray Product Less Than K
// IDEA: shrink while product >= k; every start in [left, right] gives one valid subarray ending at right
// time = O(n), space = O(1)
public int numSubarrayProductLessThanK(int[] nums, int k) {
    if (k <= 1) {
        return 0;                       // no product of positive ints is < 1
    }
    int left = 0;
    long product = 1;
    int count = 0;
    for (int right = 0; right < nums.length; right++) {
        product *= nums[right];
        while (product >= k) {
            product /= nums[left];
            left++;
        }
        count += right - left + 1;      // subarrays ending at right, starting in [left, right]
    }
    return count;
}
```

**Traps**
- `k ≤ 1`: no window is ever valid, so without the guard the `while` runs `left` past `right`
  and reads past the array: `[1, 2, 3], k = 0` raises `IndexError` instead of returning 0.
- Recording `max(len)` instead of adding `len` answers "longest", not "how many".

**Similar** — LC 992 *(exactly K distinct: this count, twice)* · LC 2302
