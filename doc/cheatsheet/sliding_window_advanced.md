# Sliding Window — Advanced Window Shapes

> **Scope** — The sliding-window techniques a first pass should skip: deque-maintained window extrema, the at-most-K-distinct family, exactly-K counting beyond one worked instance, and the windows whose key is not a character — complement, word-level chunks, index-bounded value buckets and sorted intervals; the six must-know templates stay in the main sheet.
> **See also**: [sliding_window.md](./sliding_window.md) — the six canonical templates every technique here builds on, and where `Template 1-6` references point; [sliding_window_examples.md](./sliding_window_examples.md) — the worked-solution archive for the templates themselves; [monotonic_queue.md](./monotonic_queue.md) — the full deque-extrema family, which this sheet points at rather than re-derives; [prefix_sum.md](./prefix_sum.md) — the prefix-based alternative when a window sum can go negative.

## LeetCode Problem Lists

- [Sliding Window](https://leetcode.com/problem-list/sliding-window/)
- [Prefix Sum](https://leetcode.com/problem-list/prefix-sum/)
- [Monotonic Queue](https://leetcode.com/problem-list/monotonic-queue/)
- [Rolling Hash](https://leetcode.com/problem-list/rolling-hash/)

## Overview

Every technique below is one of the six templates in [sliding_window.md](./sliding_window.md)
with something unusual in one slot: the window state is a deque instead of a counter, the window
key is a word or a value bucket instead of a character, the window is the *complement* of the
thing being chosen, or the answer needs two window passes instead of one.

### Technique Index

| Technique | LC# | What is unusual | Weight |
|---|---|---|:---:|
| Monotonic deque for window extrema | 239 | window state is an ordered deque, not a count | 3 |
| At most K distinct elements | 340, 904, 159 | the shrink test is `map.size() > K` | 4 |
| Exactly-K with a restricted alphabet | 2062 | a forbidden character *resets* the window | 4 |
| Prefix sum + HashMap instead of a window | 560, 974, 525 | validity is not monotonic, or values go negative | 4 |
| Prefix trick inside the window | 1248 | counts all valid left boundaries in one pass | 3 |
| Min operations → max middle subarray | 1658 | the answer is the window's *complement length* | 4 |
| Complement window ("take from both ends") | 1423 | the answer is `total - min window` | 4 |
| Multiple non-overlapping fixed windows | 689, 1031 | prefix/suffix argmax over window sums | 3 |
| Fixed-index window + value bucketing | 220 | window bounded by index, tested on value | 4 |
| Word-level window (fixed-length chunks) | 30, 187 | one window per alignment offset | 4 |
| Two pointers on sorted intervals | 1229 | two windows, one per input array | 3 |

> **Weight** = interview frequency on a 1-5 scale, matching the star runs on the headings below.

## Window Extrema and At-Most-K Windows

### Monotonic Deque for Window Extrema — LC 239 ⭐⭐⭐

> **This sheet does not re-derive the deque.** [monotonic_queue.md](./monotonic_queue.md) owns the
> family — the invariant, the amortised-O(n) argument, the min variant, and the harder members
> (LC 862, LC 1438, LC 1499). What follows is the minimum you need to recognise that a window
> problem wants a deque rather than a map: **the answer is an *extreme* of the window**, so a
> dominated element can be discarded forever. When the answer is a *median* instead, nothing can
> be discarded and you need two heaps or an ordered multiset.

When you need window max/min in O(1), use a deque that maintains monotonic order.

```python
from collections import deque

def maxSlidingWindow(nums, k):
    dq = deque()   # stores indices; nums[dq[0]] is always window max
    result = []

    for i, num in enumerate(nums):
        # Remove elements outside window
        if dq and dq[0] == i - k:
            dq.popleft()
        # Maintain decreasing order — remove smaller elements from back
        while dq and nums[dq[-1]] < num:
            dq.pop()
        dq.append(i)

        if i >= k - 1:
            result.append(nums[dq[0]])
    return result
```

**Time**: O(n) — each element enters and leaves deque at most once.

> Maintain a decreasing deque of indices; front is always the max of current window.

```java
// LC 239 - Sliding Window Maximum
// IDEA: Monotonic decreasing deque — front = max of current window
// time = O(N), space = O(k)
public int[] maxSlidingWindow(int[] nums, int k) {
    int n = nums.length;
    int[] ans = new int[n - k + 1];
    Deque<Integer> deque = new ArrayDeque<>(); // stores indices
    for (int i = 0; i < n; i++) {
        // remove out-of-window indices
        while (!deque.isEmpty() && deque.peekFirst() < i - k + 1) deque.pollFirst();
        // maintain decreasing order
        while (!deque.isEmpty() && nums[deque.peekLast()] < nums[i]) deque.pollLast();
        deque.offerLast(i);
        if (i >= k - 1) ans[i - k + 1] = nums[deque.peekFirst()];
    }
    return ans;
}
```

### At Most K Distinct Elements ⭐⭐⭐⭐

**Shape**: Template 3 (longest window) with `while len(window_map) > K: shrink`. The whole family
is one template with a different `K`, and it is also the `atMost` half of the exactly-K
subtraction in [Template 6](./sliding_window.md#template-6-exactly-k-via-at-most-subtraction--lc-992-).

| Problem | LC# | `K` | Note |
|---------|-----|-----|------|
| Longest Substring with At Most K Distinct Characters | 340 | K | the general statement |
| Fruit Into Baskets | 904 | 2 | LC 340 with `K = 2`, worked below |
| Longest Substring with At Most Two Distinct Characters | 159 | 2 | LC 904 on characters |
| Longest Substring Without Repeating Characters | 3 | — | the degenerate case: every count must be 1 |

#### Fruit Into Baskets — LC 904

**Problem restated**: pick the longest contiguous subarray containing **at most 2 distinct** values (2 baskets, 1 fruit type each). This is the canonical *"longest window with at most K distinct elements"* problem with `K = 2`.

**Key idea**: keep a `{fruit_type: count}` map of the window. Expand `right` every step; whenever the map holds more than 2 keys, shrink from `left` until it's back to ≤ 2. The answer is the largest window width `right - left + 1` seen along the way. The window never shrinks *below* the best valid width, so a single pass is O(n).

```python
# python
# LC 904 - Fruit Into Baskets
# IDEA: SLIDING WINDOW + HASHMAP — longest window with at most 2 distinct types
# time = O(n), space = O(1)  (map holds at most 3 keys)
class Solution(object):
    def totalFruit(self, fruits):
        if not fruits:
            return 0

        basket = {}          # fruit_type -> count in current window
        left = 0
        max_fruit = 0

        for right in range(len(fruits)):
            # 1) expand: add the fruit at right
            f = fruits[right]
            basket[f] = basket.get(f, 0) + 1

            # 2) shrink: while > 2 distinct types, drop from the left
            while len(basket) > 2:
                lf = fruits[left]
                basket[lf] -= 1
                if basket[lf] == 0:   # type fully gone -> remove key
                    del basket[lf]
                left += 1

            # 3) record best valid window width
            max_fruit = max(max_fruit, right - left + 1)

        return max_fruit
```

**Why `del` matters**: `len(basket)` is the number of distinct fruit types. If you only decrement counts without deleting zero-count keys, `len(basket)` stays inflated and the `while` loop shrinks the window too aggressively (or never exits correctly). Always remove a key once its count hits 0.

**Generalization**: swap the `> 2` for `> K` and this template solves **LC 340 (Longest Substring with At Most K Distinct Characters)** verbatim — LC 904 is just the `K = 2` special case. See [Template 6: Exactly K via At-Most Subtraction](./sliding_window.md#template-6-exactly-k-via-at-most-subtraction--lc-992-) in the main sheet for turning this into an *exactly-K* counter.

| Piece | Role |
|-------|------|
| `basket` map | tracks distinct types + their counts in the window |
| `while len(basket) > 2` | invariant enforcement — keep window valid |
| `del` on zero count | keeps `len(basket)` = true distinct count |
| `right - left + 1` | current window width, maximized into `max_fruit` |

#### Longest Substring with At Most Two Distinct Characters — LC 159

> Shrink left when distinct chars in window exceed 2; use frequency map.

```java
// LC 159 - Longest Substring with At Most Two Distinct Characters
// IDEA: Sliding window with HashMap — shrink when distinct > 2
// time = O(N), space = O(1)
public int lengthOfLongestSubstringTwoDistinct(String s) {
    Map<Character, Integer> freq = new HashMap<>();
    int l = 0, ans = 0;
    for (int r = 0; r < s.length(); r++) {
        freq.merge(s.charAt(r), 1, Integer::sum);
        while (freq.size() > 2) {
            char lc = s.charAt(l);
            freq.merge(lc, -1, Integer::sum);
            if (freq.get(lc) == 0) freq.remove(lc);
            l++;
        }
        ans = Math.max(ans, r - l + 1);
    }
    return ans;
}
```

## Exactly-K Counting Beyond One Worked Instance

[Template 6](./sliding_window.md#template-6-exactly-k-via-at-most-subtraction--lc-992-) states the
transformation and works LC 992. This section covers the parts a first pass can skip: what happens
when the alphabet is restricted, when to reach for a prefix map instead, and why the direct
"exactly K" window has no clean shrink condition.

### Count Vowel Substrings — the Reset Twist — LC 2062 ⭐⭐⭐⭐

**Problem:** Count substrings that consist **only of vowels** (`a, e, i, o, u`) AND contain **all 5** distinct vowels.

**Key Idea:** `EXACTLY 5 distinct vowels = atMost(5) - atMost(4)`

This is the same `atMost(k) - atMost(k-1)` transformation as LC 992, but with **one extra twist for strings**:

> **The vowels-only constraint.** A substring must contain *no consonants*. So the moment `atMost` hits a consonant, the window is **instantly ruined** — clear the frequency map and jump `left` past the consonant (`left = right + 1`). This guarantees every window we count contains only vowels.

```text
"EXACTLY 5 distinct vowels"  →  atMost(5) - atMost(4)
       └── only counts vowel-only windows (consonant resets window)
```

```python
# Python - LC 2062 Count Vowel Substrings of a String
# IDEA: atMost(5) - atMost(4), with consonant resetting the window
class Solution(object):
    def countVowelSubstrings(self, word):
        # time = O(n) (atMost called twice), space = O(1) (≤ 5 vowels tracked)
        def countAtMost(max_unique):
            vowels = set("aeiou")
            cnt_map = {}
            l = 0
            ans = 0

            for r in range(len(word)):
                # CRITICAL: a consonant ruins the vowel-only window
                # → clear map and jump left past the consonant
                if word[r] not in vowels:
                    cnt_map.clear()
                    l = r + 1
                    continue

                cnt_map[word[r]] = cnt_map.get(word[r], 0) + 1

                # shrink from left while too many distinct vowels
                while len(cnt_map) > max_unique:
                    cnt_map[word[l]] -= 1
                    if cnt_map[word[l]] == 0:
                        del cnt_map[word[l]]
                    l += 1

                # # of valid vowel-only substrings ending at r = window length
                ans += (r - l + 1)

            return ans

        # EXACTLY 5 distinct vowels = atMost(5) - atMost(4)
        return countAtMost(5) - countAtMost(4)
```

```java
// Java - LC 2062 Count Vowel Substrings of a String
// IDEA: atMost(5) - atMost(4), with consonant resetting the window
class Solution {
    /**
     * time = O(n) (atMost called twice), space = O(1) (≤ 5 vowels tracked)
     */
    public int countVowelSubstrings(String word) {
        // EXACTLY 5 distinct vowels = atMost(5) - atMost(4)
        return countAtMost(word, 5) - countAtMost(word, 4);
    }

    private int countAtMost(String word, int maxUnique) {
        Set<Character> vowels = new HashSet<>(Arrays.asList('a', 'e', 'i', 'o', 'u'));
        Map<Character, Integer> cntMap = new HashMap<>();
        int l = 0, ans = 0;

        for (int r = 0; r < word.length(); r++) {
            char c = word.charAt(r);

            // CRITICAL: a consonant ruins the vowel-only window
            // → clear map and jump left past the consonant
            if (!vowels.contains(c)) {
                cntMap.clear();
                l = r + 1;
                continue;
            }

            cntMap.put(c, cntMap.getOrDefault(c, 0) + 1);

            // shrink from left while too many distinct vowels
            while (cntMap.size() > maxUnique) {
                char leftChar = word.charAt(l);
                cntMap.put(leftChar, cntMap.get(leftChar) - 1);
                if (cntMap.get(leftChar) == 0) {
                    cntMap.remove(leftChar);
                }
                l++;
            }

            // # of valid vowel-only substrings ending at r = window length
            ans += (r - l + 1);
        }

        return ans;
    }
}
```

**Why the consonant reset is the only difference from LC 992:**

| | LC 992 (K distinct integers) | LC 2062 (5 distinct vowels) |
|---|---|---|
| Allowed elements | any integer | **vowels only** |
| Invalid element | (none — all allowed) | **consonant → reset window** |
| Transformation | `atMost(k) - atMost(k-1)` | `atMost(5) - atMost(4)` |
| Window count | `ans += r - l + 1` | `ans += r - l + 1` |

> **Takeaway:** when a "count substrings with exactly K distinct" problem also restricts *which characters are allowed*, keep the `atMost` subtraction and just add a reset (`map.clear(); l = r + 1`) whenever a forbidden character appears.

### Why At-Most Subtraction Works ⭐⭐⭐

#### Visual example: index-by-index


```text
Array: [1, 2, 1, 3], K = 2 (exactly 2 distinct)

At Most 2 Distinct:
Index 0 (1): [1] ✓                                 → count = 1
Index 1 (2): [2] ✓, [1,2] ✓                        → count = 2
Index 2 (1): [1] ✓, [2,1] ✓, [1,2,1] ✓             → count = 3
Index 3 (3): [3] ✓, [1,3] ✓, but NOT [2,1,3] ❌    → count = 2
                   (window shrinks to [1,3])

Total At Most 2: 1 + 2 + 3 + 2 = 8

At Most 1 Distinct:
Index 0 (1): [1] ✓                                 → count = 1
Index 1 (2): [2] ✓, but NOT [1,2] ❌               → count = 1
                   (window shrinks to [2])
Index 2 (1): [1] ✓, but NOT [2,1] ❌               → count = 1
                   (window shrinks to [1])
Index 3 (3): [3] ✓, but NOT [1,3] ❌               → count = 1
                   (window shrinks to [3])

Total At Most 1: 1 + 1 + 1 + 1 = 4

Exactly 2 Distinct = 8 - 4 = 4 ✓

The 4 subarrays with exactly 2 distinct:
[1,2], [1,2,1], [2,1], [1,3]
```

#### Pattern recognition: when to use this technique

**Use "Exactly K" transformation when you see:**
```text
✅ "exactly K distinct/different"
✅ "exactly K times"
✅ "exactly K occurrences"
✅ "subarrays with exactly K ..."
✅ COUNTING problems (not max/min length)
```

**Direct sliding window works when:**
```text
✅ "at most K"
✅ "maximum length with ≤ K"
✅ "minimum length with ≥ K"
✅ "longest substring with at most K"
```

#### Common mistakes

**1. Forgetting k=0 Edge Case:**
```python
# Wrong: Doesn't handle k=0
def exactly_k(nums, k):
    return at_most_k(nums, k) - at_most_k(nums, k - 1)
    # at_most_k(nums, -1) may fail!

# Right: Handle k=0 explicitly
def exactly_k(nums, k):
    if k == 0:
        return 0
    return at_most_k(nums, k) - at_most_k(nums, k - 1)
```

**2. Using Wrong Approach for Max/Min Length:**
```python
# Wrong: Using "exactly K" transformation for max length
def longest_k_distinct(s, k):
    # This gives COUNT, not LENGTH!
    return at_most_k(s, k) - at_most_k(s, k - 1)  # ❌

# Right: Direct at_most_k for max length
def longest_k_distinct(s, k):
    # Track max window size during at_most_k
    return at_most_k_max_length(s, k)  # ✓
```

**3. Confusing Count vs Length:**
```python
# For COUNTING subarrays: use right - left + 1
count += right - left + 1

# For MAX LENGTH: track max window size
max_length = max(max_length, right - left + 1)
```

#### Interview talking points

**1. Recognition:**
```text
Interviewer: "Count subarrays with exactly K ..."
→ Think: "Exactly K = At Most K - At Most (K-1)"

Interviewer: "Find longest substring with at most K ..."
→ Think: "Direct sliding window, no subtraction needed"
```

**2. Complexity Analysis:**
```text
Time: O(n) - each element added once, removed at most once in each pass
      Total: 2 passes × O(n) = O(n)

Space: O(k) - HashMap stores at most K distinct elements
```

**4. Talking Points:**
- "Direct 'exactly K' is hard because window validity changes non-monotonically"
- "At most K is monotonic - once valid, stays valid until we shrink"
- "Subtracting at most (K-1) removes all overcounting"
- "This transforms a hard problem into two medium problems"

> The at-most template itself is not repeated here — memorise it from
> [Template 6](./sliding_window.md#template-6-exactly-k-via-at-most-subtraction--lc-992-).

#### Why direct "exactly K" is hard

**Problem with direct approach:**
```python
# Naive attempt (WRONG!)
def exactly_k_direct(nums, k):
    left = 0
    count = 0
    window = {}

    for right in range(len(nums)):
        window[nums[right]] = window.get(nums[right], 0) + 1

        # When to shrink? This is tricky!
        # If len(window) > k: shrink (too many distinct)
        # If len(window) < k: can't count yet (too few distinct)
        # If len(window) == k: count, but should we shrink?

        # If we shrink when == k, we might miss valid subarrays
        # If we don't shrink, we might count invalid subarrays

        # There's no clean condition! ❌

    return count
```

**Why "at most K" works:**
```python
# Window validity is monotonic:
# - If window is valid (≤ K), all sub-windows are valid
# - If window becomes invalid (> K), shrink until valid
# - Clear shrinking condition: while len(window) > k

# This monotonic property makes sliding window perfect!
```

**Mathematical proof of transformation:**
```text
Let S(k) = set of all subarrays with at most k distinct elements

S(2) = {[1], [1,2], [1,2,1], [2], [2,1], [1], [1,3], [3], ...}
S(1) = {[1], [2], [1], [3], ...}  (only single-element subarrays)

S(2) \ S(1) = subarrays in S(2) but not in S(1)
            = subarrays with MORE than 1 but AT MOST 2 distinct
            = subarrays with EXACTLY 2 distinct ✓

Generalized: S(k) \ S(k-1) = subarrays with exactly k distinct
```

### Prefix Sum + HashMap vs Sliding Window — Which to Use? ⭐⭐⭐⭐

#### Core Ideas

**Sliding Window**
- Maintain a window `[left, right]` and shrink/expand it based on a **monotonic** condition.
- Works when validity is monotonic: once the window goes invalid, shrinking from the left always restores validity.
- Naturally handles **"at most K"** and **"longest/shortest"** constraints.

**Prefix Sum + HashMap**
- Track a running cumulative count (e.g., number of odd elements seen so far).
- Store how many times each prefix count has appeared in a HashMap.
- At each index, look up `prefixCount - k` in the map to find how many subarrays ending here have **exactly k** of the target element.
- Works when you need to count subarrays with an **exact** target, especially when "exactly k" breaks sliding window monotonicity.

#### Why "Exactly K" Breaks Pure Sliding Window

```text
nums = [2,2,1,2,1], k = 2

At r = 4 (last element), valid subarrays ending here:
  [1,2,1]        → starts at index 2
  [2,1,2,1]      → starts at index 1
  [2,2,1,2,1]    → starts at index 0

→ 3 valid left boundaries — but pure sliding window finds only 1!
```

The sliding window can only track **one** left boundary. For "exactly k", there are **multiple** valid left boundaries per right position — prefix sum + HashMap counts all of them in O(1) per step.

#### Comparison Table

| Aspect | Sliding Window | Prefix Sum + HashMap |
|---|---|---|
| **Best for** | at most K / longest / shortest | exactly K / count of subarrays |
| **Condition type** | Monotonic (≤ k, ≥ k) | Non-monotonic (== k) |
| **Multiple left boundaries** | ❌ Handles only one | ✅ Counts all |
| **Space** | O(1) | O(n) for the HashMap |
| **Time** | O(n) | O(n) |
| **Code complexity** | Simple two-pointer | Requires prefix tracking + base case `map.put(0, 1)` |
| **Key trick** | `while (invalid) { shrink left }` | `res += map.get(prefixCount - k)` |

#### Decision Guide

```text
Is the condition monotonic? (e.g., sum ≤ k, distinct ≤ k)
  ├── YES → Pure Sliding Window
  └── NO (exactly k, == k) →
        ├── atMost(k) - atMost(k-1)  [two sliding window passes]
        ├── Prefix Sum + HashMap      [one pass, O(n) space]
        └── Prefix Trick in Sliding Window [one pass, O(1) space — see "Prefix Trick Inside the Window" below]
```

#### Code Patterns Side-by-Side

**Sliding Window — "at most K odds":**
```java
private int atMost(int[] nums, int k) {
    int l = 0, res = 0, oddCount = 0;
    for (int r = 0; r < nums.length; r++) {
        if (nums[r] % 2 == 1) oddCount++;
        while (oddCount > k) {
            if (nums[l] % 2 == 1) oddCount--;
            l++;
        }
        res += (r - l + 1);   // all subarrays ending at r with ≤ k odds
    }
    return res;
}
```

**Prefix Sum + HashMap — "exactly K odds":**
```java
public int numberOfSubarrays(int[] nums, int k) {
    Map<Integer, Integer> map = new HashMap<>();
    map.put(0, 1);  // base case: empty prefix has 0 odd numbers
    int oddCount = 0, res = 0;
    for (int val : nums) {
        if (val % 2 == 1) oddCount++;
        // how many previous prefixes had (oddCount - k) odds?
        // → those prefixes + current position = subarray with exactly k odds
        res += map.getOrDefault(oddCount - k, 0);
        map.put(oddCount, map.getOrDefault(oddCount, 0) + 1);
    }
    return res;
}
```

#### Similar LeetCode Problems

| Problem | LC# | Difficulty | Approach | Key Insight |
|---------|-----|------------|----------|-------------|
| Count Number of Nice Subarrays | 1248 | Medium | Both work | Treat odd=1, even=0; prefix sum or atMost trick |
| Binary Subarrays With Sum | 930 | Medium | Both work | Binary array; prefix sum is most direct |
| Subarray Sum Equals K | 560 | Medium | **Prefix Sum only** | Negative numbers → sliding window fails |
| Subarrays with K Different Integers | 992 | Hard | Sliding Window (atMost) | Distinct count; atMost(k)-atMost(k-1) |
| Number of Subarrays with Sum = k | 974 | Medium | **Prefix Sum only** | Divisibility variant; exact match needed |
| Contiguous Array | 525 | Medium | **Prefix Sum only** | Equal 0s and 1s; exact balance needed |

> **Rule of thumb**: If the array can have **negative numbers** or the condition is a hard equality that can't be rephrased as "at most", use **Prefix Sum + HashMap**. If values are non-negative and the condition is a range (≤ k), use **Sliding Window**.

### When Pure Sliding Window Works vs. When You Need Extra Tricks ⭐⭐⭐

#### Core Question: Is the Validity Condition Monotonic?

**Pure sliding window works** when the validity condition is **monotonic**:
- Once the window becomes invalid, it stays invalid as you expand right
- A single `while (invalid) { shrink left }` cleanly restores validity

**You need extra tricks** when the condition is **non-monotonic** (especially "exactly k"):
- For a fixed `r`, there may be **multiple valid left boundaries**
- Simply shrinking until valid gives you one answer, but misses others

#### Decision Table

| Condition Type | Example | Pure Sliding Window? | Fix |
|----------------|---------|---------------------|-----|
| `sum ≤ k` | product < k | ✅ Yes | — |
| `distinct ≤ k` | at most K distinct | ✅ Yes | — |
| `sum ≥ k` (min length) | min subarray sum | ✅ Yes | — |
| `exactly k` odds/distinct | LC 1248, LC 992 | ❌ No | `atMost(k) - atMost(k-1)` OR prefix trick |
| `exactly k` (with even gap) | LC 1248 | ❌ No | prefix trick (count even gap at left) |

**The two fixes**, both covered on this page or the main sheet:

1. **`atMost(k) - atMost(k-1)`** — two clean passes; see [Template 6](./sliding_window.md#template-6-exactly-k-via-at-most-subtraction--lc-992-).
2. **Prefix trick inside the window** — one pass, O(1) space; see the next section.


### Prefix Trick Inside the Window — LC 1248 ⭐⭐⭐


**When to use:** Count subarrays with **exactly k** of some element, where you want a single-pass O(n) solution without calling `atMost` twice.

**Core Idea:**
```text
When oddCount reaches k (window has exactly k odds):
  - Count how many even numbers are at the LEFT edge of the window
    before hitting the (k-th-from-left) odd number
  - Each of these even numbers gives one more valid left boundary
  - Store this count as `prefix`

After the window shrinks past the leftmost odd:
  - oddCount drops below k, so the while loop exits
  - But `prefix` (the "even gap") is PRESERVED
  - For every future r that keeps oddCount == k,
    those same left boundaries are still valid → add `prefix` again
```

**Why `prefix` resets to 0 when a new odd is encountered:**
- A new odd number at `r` changes which odd is the "k-th from left"
- The gap of evens before the new leftmost odd must be recomputed
- So reset `prefix = 0` and let the while loop rebuild it

#### Template

```java
// Prefix Trick + Sliding Window
// time = O(N), space = O(1)
public int exactlyK(int[] nums, int k) {
    int l = 0, res = 0, oddCount = 0, prefix = 0;

    for (int r = 0; r < nums.length; r++) {
        if (nums[r] % 2 == 1) {
            oddCount++;
            prefix = 0;  // reset: new odd changes left boundary gap
        }

        // Shrink left while window has exactly k odds,
        // counting even elements we skip at the left edge
        while (oddCount == k) {
            prefix++;                        // one more valid left boundary
            if (nums[l] % 2 == 1) oddCount--;
            l++;
        }

        // prefix = # of valid left boundaries for subarrays ending at r
        res += prefix;
    }

    return res;
}
```

#### Walkthrough: `nums = [2,2,1,2,1], k = 2`

```text
r=0 (2): oddCount=0, prefix=0  → res=0
r=1 (2): oddCount=0, prefix=0  → res=0
r=2 (1): oddCount=1, prefix=0  → res=0   (new odd, prefix reset)
r=3 (2): oddCount=1, prefix=0  → res=0
r=4 (1): oddCount=2, prefix=0  → new odd, prefix reset to 0
  while oddCount==2:
    prefix=1, nums[0]=2 (even), l=1       → oddCount still 2
    prefix=2, nums[1]=2 (even), l=2       → oddCount still 2
    prefix=3, nums[2]=1 (odd),  l=3, oddCount=1 → exit while
  res += 3 → res=3
```

Answer: 3 ✅ — the three subarrays `[1,2,1]`, `[2,1,2,1]`, `[2,2,1,2,1]`

#### Comparison: Prefix Trick vs atMost Subtraction

| | Prefix Trick | atMost(k) - atMost(k-1) |
|---|---|---|
| **Passes** | 1 | 2 |
| **Space** | O(1) | O(1) |
| **Complexity** | O(n) | O(n) |
| **Readability** | Tricky (reset logic) | Cleaner, more intuitive |
| **Use when** | Single-pass preferred | Clarity preferred |

#### Related Problems

| Problem | LC# | Difficulty | Note |
|---------|-----|------------|------|
| Count Number of Nice Subarrays | 1248 | Medium | Exactly k odds |
| Binary Subarrays With Sum | 930 | Medium | Exactly sum k (0/1 array) |
| Subarrays with K Different Integers | 992 | Hard | Exactly k distinct |
| Number of Substrings Containing All Three Characters | 1358 | Medium | Similar gap counting |

## Window Transformations

### Min Operations → Max Subarray Length — LC 1658 ⭐⭐⭐⭐

#### Core Idea

When a problem asks for the **minimum number of operations removing elements from both ends** of an array until some target is reached, flip the perspective:

```text
Instead of minimizing elements removed from edges,
MAXIMIZE the elements kept in the middle.

Min Edge Removals = Total Length − Max Middle Subarray Length
```

**Why this works:**

```text
removed_sum + remaining_sum = total_sum

If removed_sum must equal x:
  remaining_sum = total_sum - x   ← this becomes the sliding window target

Total Elements − Max Middle Subarray (sum = target) = Min Operations
```

```text
Visual layout:

MIN EDGE PIECES (Ops)              MAX MIDDLE SUBARRAY
 | nums[0] | nums[1] |      | ... | ... | ... |
 \_______________________/  \_______________________/
     Removed from Edges           Left in the Center
          (Sum = x)               (Sum = total_sum - x)
```

#### Pattern

```text
Step 1: Compute total = sum(nums)
Step 2: Compute target = total - x
        • If target == 0 → must remove ALL elements → return nums.length
        • If target < 0  → impossible             → return -1
Step 3: Sliding window to find LONGEST subarray with sum == target
Step 4: return nums.length - maxLen   (or -1 if not found)
```

#### Template (Java)

```java
public int minOperations(int[] nums, int x) {
    int total = 0;
    for (int num : nums) total += num;

    int target = total - x;
    if (target == 0) return nums.length;
    if (target < 0)  return -1;

    int n = nums.length, l = 0, sum = 0, maxLen = -1;

    for (int r = 0; r < n; r++) {
        sum += nums[r];

        // shrink from left while sum exceeds target
        while (l <= r && sum > target) {
            sum -= nums[l++];
        }

        // valid window found — track longest
        if (sum == target) {
            maxLen = Math.max(maxLen, r - l + 1);
        }
    }

    return maxLen == -1 ? -1 : n - maxLen;
}
```

> **Why pure sliding window works here:** `# Note: works for any nums[i] >= 0 (non-negative)` — the window sum is **monotonically non-decreasing** as we expand right. Shrinking from the left always reduces the sum — the validity condition is monotonic → clean two-pointer solution.

#### Dry Run — `nums = [1,1,4,2,3], x = 5`

```text
total = 11,  target = 11 - 5 = 6

r  nums[r]  window     sum   action          maxLen
0    1      [1]          1   sum < target      -1
1    1      [1,1]        2   sum < target      -1
2    4      [1,1,4]      6   sum == target      3   ← window [0..2]
3    2      [1,1,4,2]    8   shrink left
           [1,4,2]       7   shrink left
           [4,2]         6   sum == target      3   ← window [2..3]
4    3      [4,2,3]      9   shrink left
           [2,3]         5   sum < target       3

maxLen = 3  →  answer = 5 - 3 = 2 ✓
```

#### When to Apply This Transformation

| Signal in the problem | Transformation |
|-----------------------|----------------|
| "remove from left or right" | Min removals = n − max middle subarray |
| "minimum operations from both ends" | Find max subarray with sum = total − x |
| "elements can only be taken from edges" | Complement is a contiguous middle subarray |

#### Similar LeetCode Problems

| Problem | LC# | Difficulty | Key Insight |
|---------|-----|------------|-------------|
| **Minimum Operations to Reduce X to Zero** | **1658** | **Medium** | Core example — max subarray with sum = total − x |
| Minimum Size Subarray Sum | 209 | Medium | Min length subarray with sum ≥ target (direct, no flip) |
| Maximum Erasure Value | 1695 | Medium | Max subarray with all unique elements |
| Subarray Sum Equals K | 560 | Medium | Exact subarray sum — use prefix+HashMap (negatives present) |
| Longest Subarray of 1's After Deleting One Element | 1493 | Medium | Max middle subarray, fixed removal budget |
| Count Subarrays Where Max Element Appears at Least K Times | 2962 | Medium | Count valid middle windows, min-ops framing |

### Complement Window ("take from both ends") — LC 1423 ⭐⭐⭐⭐

**When to use:** You must pick `k` elements **from the two ends** of an array (any split between left and right). The chosen elements are not contiguous — but everything you *leave behind* is: it's exactly one contiguous window of size `n - k`. Maximize the pick ⇔ **minimize the complement window**.

#### Core Idea

```text
[ take l from front ][ ....... leftover ....... ][ take r from back ],  l + r = k

leftover is ALWAYS a contiguous block of size n - k.
  answer = total - min(sum of any window of size n - k)

This flips a "choose from both ends" problem into a plain fixed-size window scan.
Edge case: k >= n → take everything → return total (window size would be 0).
```

#### Template (Java)

```java
// LC 1423 - Maximum Points You Can Obtain from Cards
// IDEA: complement trick — maximize ends == total - min fixed window of size n-k
// time = O(n), space = O(1)
public int maxScore(int[] cardPoints, int k) {
    int n = cardPoints.length, total = 0;
    for (int c : cardPoints) total += c;
    if (k >= n) return total;                     // take every card

    int win = n - k, cur = 0;
    for (int i = 0; i < win; i++) cur += cardPoints[i];
    int minWindow = cur;

    for (int i = win; i < n; i++) {               // slide the leftover window
        cur += cardPoints[i] - cardPoints[i - win];
        minWindow = Math.min(minWindow, cur);
    }
    return total - minWindow;
}
```

```python
# python
# LC 1423 - Maximum Points You Can Obtain from Cards
# IDEA: complement trick — maximize ends == total - min fixed window of size n-k
# time = O(n), space = O(1)
def maxScore(cardPoints, k):
    n = len(cardPoints)
    total = sum(cardPoints)
    if k >= n:
        return total                              # take every card

    win = n - k
    cur = sum(cardPoints[:win])
    min_window = cur

    for i in range(win, n):                       # slide the leftover window
        cur += cardPoints[i] - cardPoints[i - win]
        min_window = min(min_window, cur)
    return total - min_window
```

#### Dry Run — `cardPoints = [1,2,3,4,5,6,1], k = 3`

```text
n = 7, total = 22, leftover window size = 7 - 3 = 4

window            sum
[1,2,3,4]         10   ← min so far
  [2,3,4,5]       14
    [3,4,5,6]     18
      [4,5,6,1]   16
min = 10 at [1,2,3,4] (indices 0..3)  →  the cards taken are indices 4,5,6 = 5+6+1 = 12
answer = 22 - 10 = 12   (0 from the front, 3 from the back) ✓
```

#### When to Apply the Complement Trick

```text
✅ "Pick k items from the front and/or back"        → min/max window of size n-k
✅ "Remove a contiguous block to optimize the rest" → same idea, inverted
✅ "Choose a prefix + a suffix under a constraint"  → the gap between them is one window
❌ Picks may come from the middle → complement is no longer contiguous, trick fails
```

### Multiple Non-Overlapping Fixed Windows — LC 689 ⭐⭐⭐

**When to use:** Choose **several non-overlapping fixed-size windows** to maximize the total sum. Fix the *middle* window, then the best left window and the best right window are independent — precompute them with prefix/suffix "argmax" scans. Generalizes the two-window case (LC 1031) to three.

#### Core Idea

```text
Step 1: w[i] = sum of the window starting at i  (rolling sum, i in [0, n-k])
Step 2: left[i]  = index of the BEST window start in [0, i]        (prefix argmax, scan →)
        right[i] = index of the BEST window start in [i, n-k]      (suffix argmax, scan ←)
Step 3: for every middle start `mid` in [k, m-1-k]:
            total = w[left[mid-k]] + w[mid] + w[right[mid+k]]
        keep the max.

Lexicographically smallest indices (LC 689 requires it):
  - prefix scan uses STRICT `>`  → keeps the earliest tie
  - suffix scan uses `>=`        → also keeps the earliest tie (scanning right-to-left)
  - middle loop uses strict `>`  → keeps the earliest mid
```

#### Template (Java)

```java
// LC 689 - Maximum Sum of 3 Non-Overlapping Subarrays
// IDEA: rolling window sums + prefix/suffix argmax, then fix the middle window
// time = O(n), space = O(n)
public int[] maxSumOfThreeSubarrays(int[] nums, int k) {
    int n = nums.length;
    int[] w = new int[n - k + 1];                     // w[i] = sum of window starting at i
    int cur = 0;
    for (int i = 0; i < n; i++) {
        cur += nums[i];
        if (i >= k) cur -= nums[i - k];
        if (i >= k - 1) w[i - k + 1] = cur;
    }

    int m = w.length;
    int[] left = new int[m], right = new int[m];
    int best = 0;
    for (int i = 0; i < m; i++) {                     // strict > → earliest tie wins
        if (w[i] > w[best]) best = i;
        left[i] = best;
    }
    best = m - 1;
    for (int i = m - 1; i >= 0; i--) {                // >= while scanning left → earliest tie
        if (w[i] >= w[best]) best = i;
        right[i] = best;
    }

    int[] ans = null;
    int bestSum = -1;
    for (int mid = k; mid + k < m; mid++) {           // fix the middle window
        int l = left[mid - k], r = right[mid + k];
        int sum = w[l] + w[mid] + w[r];
        if (sum > bestSum) {
            bestSum = sum;
            ans = new int[]{l, mid, r};
        }
    }
    return ans;
}
```

```python
# python
# LC 689 - Maximum Sum of 3 Non-Overlapping Subarrays
# IDEA: rolling window sums + prefix/suffix argmax, then fix the middle window
# time = O(n), space = O(n)
def maxSumOfThreeSubarrays(nums, k):
    n = len(nums)
    w = [0] * (n - k + 1)                      # w[i] = sum of window starting at i
    cur = sum(nums[:k])
    w[0] = cur
    for i in range(k, n):
        cur += nums[i] - nums[i - k]
        w[i - k + 1] = cur

    m = len(w)
    left, right = [0] * m, [0] * m
    best = 0
    for i in range(m):                         # strict > → earliest tie wins
        if w[i] > w[best]:
            best = i
        left[i] = best
    best = m - 1
    for i in range(m - 1, -1, -1):             # >= while scanning left → earliest tie
        if w[i] >= w[best]:
            best = i
        right[i] = best

    ans, best_sum = None, -1
    for mid in range(k, m - k):                # fix the middle window
        l, r = left[mid - k], right[mid + k]
        s = w[l] + w[mid] + w[r]
        if s > best_sum:
            best_sum, ans = s, [l, mid, r]
    return ans
```

#### Dry Run — `nums = [1,2,1,2,6,7,5,1], k = 2`

```text
w  = [3, 3, 3, 8, 13, 12, 6]         (sums of every length-2 window)
left  = [0, 0, 0, 3, 4, 4, 4]        (prefix argmax, earliest tie)
right = [4, 4, 4, 4, 4, 5, 6]        (suffix argmax, earliest tie)

mid = 2 → l=left[0]=0, r=right[4]=4 → 3 + 3 + 13 = 19
mid = 3 → l=left[1]=0, r=right[5]=5 → 3 + 8 + 12 = 23  ← best
mid = 4 → l=left[2]=0, r=right[6]=6 → 3 + 13 + 6 = 22
answer = [0, 3, 5] ✓
```

#### Similar Problems

| Problem | LC# | Difficulty | Key Difference |
|---------|-----|------------|----------------|
| **Maximum Sum of 3 Non-Overlapping Subarrays** | **689** | **Hard** | Core example — 3 windows, lexicographically smallest indices |
| Maximum Sum of Two Non-Overlapping Subarrays | 1031 | Medium | Only 2 windows (and two different sizes) — worked immediately below; no middle loop needed |

#### Generalization

```text
For j windows (j > 3), drop the fixed-middle trick and go DP:
  dp[j][i] = best total using j windows within the prefix ending at i
           = max(dp[j][i-1],  dp[j-1][i-k] + w[i-k+1])
  → O(n * j) time. The 3-window case just hardcodes j = 3 with prefix/suffix argmax.
```


#### LC 1031: Maximum Sum of Two Non-Overlapping Subarrays — Prefix Sum + Sliding Window

> Try both orderings (L before M, M before L). For each ordering, scan with `i` as the **exclusive end** of the M window; maintain `maxL` = best L-window seen so far to the left of M.

**Key index layout (i = exclusive end of M window):**
```text
Indices:  0 . . . [i-M-L] . . . [i-M] . . . [i] . . . n
                   |--- L window ---| |--- M window ---|

L window sum: prefix[i-M]   - prefix[i-M-L]
M window sum: prefix[i]     - prefix[i-M]
```

**Why start at `i = L + M`?**  The minimum prefix length needed to fit both windows end-to-end.  `i` runs up to `<= n` (inclusive) because `prefix` has size `n+1`.

```java
// LC 1031 - Maximum Sum of Two Non-Overlapping Subarrays
// IDEA: Prefix Sum + Sliding Window — try both L-before-M and M-before-L
// time = O(N), space = O(N)
public int maxSumTwoNoOverlap(int[] nums, int firstLen, int secondLen) {
    return Math.max(
        helper(nums, firstLen, secondLen),   // firstLen before secondLen
        helper(nums, secondLen, firstLen));  // secondLen before firstLen
}

// L comes before M; i is the exclusive end of the M window
private int helper(int[] nums, int L, int M) {
    int n = nums.length;
    int[] prefix = new int[n + 1];
    for (int i = 0; i < n; i++) {
        prefix[i + 1] = prefix[i] + nums[i];
    }

    int maxL = 0; // best L-window sum seen so far (left of current M)
    int ans   = 0;

    /**
     * i = ending position (exclusive) of M window
     *
     * 1. i starts from L + M  (minimum length to fit both windows)
     * 2. i ends at <= n       (prefix has size n+1)
     *
     * Index layout:
     *   0 . . . [i-M-L] . . . [i-M] . . . [i] . . . n
     *            |--- L window ---| |--- M window ---|
     *
     *   L window: prefix[i-M]   - prefix[i-M-L]   (range [i-M-L, i-M))
     *   M window: prefix[i]     - prefix[i-M]      (range [i-M,   i))
     */
    for (int i = L + M; i <= n; i++) {
        // L window: [i-M-L, i-M)
        int lSum = prefix[i - M] - prefix[i - M - L];
        maxL = Math.max(maxL, lSum);          // keep best L seen so far

        // M window: [i-M, i)
        int mSum = prefix[i] - prefix[i - M];

        ans = Math.max(ans, maxL + mSum);     // best non-overlapping pair
    }

    return ans;
}
```

**Pattern summary:**
- Build prefix sum once: O(N)
- Single pass per ordering: maintain `maxL` (best left window) while advancing the right window
- Call twice (swap L/M) to cover both orderings → final answer is `Math.max` of both

## Windows Whose Key Is Not a Character

### Fixed-Index Window + Bucketing (value-proximity queries) — LC 220 ⭐⭐⭐⭐

**When to use:** The window is bounded by **index distance** (`|i - j| <= indexDiff`), but the validity test is on **values** (`|nums[i] - nums[j]| <= valueDiff`). A plain frequency map can't answer "is there a *nearby value* in the window?" — you need an ordered structure, or the O(1) bucket trick.

#### Core Idea

```text
Window = the last `indexDiff` elements (a fixed-capacity set, evicted by index).
Question per new element x: does the window hold a value within valueDiff of x?

Bucket trick:
  bucket(x) = floor(x / (valueDiff + 1))     ← width = valueDiff + 1
  - Two values in the SAME bucket always differ by <= valueDiff  → answer immediately.
  - Values that differ by <= valueDiff but sit in different buckets
    must be in ADJACENT buckets → check bucket-1 and bucket+1 only.
  - Any bucket holds at most one live value (a second one would have returned true).

Why width = valueDiff + 1, not valueDiff?
  With width w, same-bucket values differ by <= w-1. Setting w = valueDiff + 1
  makes "same bucket ⇒ valid" exactly true.
```

#### Template (Java)

```java
// LC 220 - Contains Duplicate III
// IDEA: fixed-index sliding window + bucketing by value (width = valueDiff + 1)
// time = O(n), space = O(min(n, indexDiff))
public boolean containsNearbyAlmostDuplicate(int[] nums, int indexDiff, int valueDiff) {
    if (indexDiff <= 0 || valueDiff < 0) return false;
    long w = (long) valueDiff + 1;                 // bucket width
    Map<Long, Long> buckets = new HashMap<>();     // bucketId -> the single value living there

    for (int i = 0; i < nums.length; i++) {
        long x = nums[i];
        long b = Math.floorDiv(x, w);              // floorDiv (NOT /) keeps negatives correct

        if (buckets.containsKey(b)) return true;                                  // same bucket
        if (buckets.containsKey(b - 1) && x - buckets.get(b - 1) <= valueDiff) return true;
        if (buckets.containsKey(b + 1) && buckets.get(b + 1) - x <= valueDiff) return true;

        buckets.put(b, x);
        // evict the element that just fell out of the index window
        if (i >= indexDiff) buckets.remove(Math.floorDiv((long) nums[i - indexDiff], w));
    }
    return false;
}
```

```python
# python
# LC 220 - Contains Duplicate III
# IDEA: fixed-index sliding window + bucketing by value (width = valueDiff + 1)
# time = O(n), space = O(min(n, indexDiff))
def containsNearbyAlmostDuplicate(nums, indexDiff, valueDiff):
    if indexDiff <= 0 or valueDiff < 0:
        return False
    w = valueDiff + 1                      # bucket width
    buckets = {}                           # bucketId -> the single value living there

    for i, x in enumerate(nums):
        b = x // w                         # python floor division already handles negatives
        if b in buckets:
            return True
        if b - 1 in buckets and abs(x - buckets[b - 1]) <= valueDiff:
            return True
        if b + 1 in buckets and abs(x - buckets[b + 1]) <= valueDiff:
            return True

        buckets[b] = x
        if i >= indexDiff:                 # evict element leaving the index window
            del buckets[nums[i - indexDiff] // w]
    return False
```

#### Alternative: Ordered Set Window — `O(n log k)`

```text
Keep a TreeSet (Java) / SortedList (Python) of the last indexDiff values.
For each x: floor/ceiling query → is there a neighbour within valueDiff?
  TreeSet<Long> set; Long lo = set.floor(x); Long hi = set.ceiling(x);
Slower (log k) but far easier to get right under interview pressure —
state the bucket version as the O(n) follow-up.
```

#### Pitfalls

```text
❌ b = x / w in Java → truncates toward zero, so -3/5 == 0 == 3/5 (wrong bucket for negatives).
   ✅ Math.floorDiv(x, w)
❌ Using width = valueDiff → same-bucket pairs may differ by valueDiff+... ; off-by-one bugs.
❌ Forgetting the eviction step → window becomes "whole prefix", indexDiff ignored.
❌ int overflow on `x - neighbour` when values span ±2^31 → widen to long.
```

#### Similar Problems

| Problem | LC# | Difficulty | Key Difference |
|---------|-----|------------|----------------|
| **Contains Duplicate III** | **220** | **Hard** | Core example — index window + value proximity |
| Contains Duplicate II | 219 | Easy | Same index window, but exact equality → plain HashSet suffices |


### Word-Level Sliding Window (fixed-length chunks) — LC 30 ⭐⭐⭐⭐

**When to use:** The window slides over **fixed-length chunks** rather than single characters — concatenation of equal-length words, k-mers, block matching. The trick is running `wordLen` independent sliding windows, one per starting offset, so every possible alignment is covered while each character is still visited O(1) times per offset.

#### Core Idea

```text
words all have length L, there are m of them → answer substrings have length L*m.
Any valid start index s satisfies s % L == r for some r in [0, L).
Two starts with the same remainder share chunk boundaries → they belong to
ONE sliding window pass. So run L passes, offset = 0..L-1, each stepping by L.

Inside a pass, this is just the classic "window with a frequency map + match counter":
  - chunk not in need           → hard reset (clear map, jump left past it)
  - chunk over-counted          → shrink from the left until it fits
  - count == m                  → record start, then shrink one chunk to keep scanning

Total work: L passes * (n / L) chunks = O(n) chunk steps, each O(L) to hash a substring.
```

#### Template (Java)

```java
// LC 30 - Substring with Concatenation of All Words
// IDEA: wordLen independent sliding windows (one per offset) + freq map & match counter
// time = O(wordLen * n), space = O(m * wordLen)
public List<Integer> findSubstring(String s, String[] words) {
    List<Integer> res = new ArrayList<>();
    if (s == null || s.isEmpty() || words.length == 0) return res;
    int wl = words[0].length(), m = words.length;
    if (s.length() < wl * m) return res;

    Map<String, Integer> need = new HashMap<>();
    for (String w : words) need.merge(w, 1, Integer::sum);

    for (int offset = 0; offset < wl; offset++) {           // one window per alignment
        int left = offset, count = 0;
        Map<String, Integer> window = new HashMap<>();

        for (int right = offset; right + wl <= s.length(); right += wl) {
            String word = s.substring(right, right + wl);

            if (!need.containsKey(word)) {                  // unusable chunk → hard reset
                window.clear();
                count = 0;
                left = right + wl;
                continue;
            }

            window.merge(word, 1, Integer::sum);
            count++;
            while (window.get(word) > need.get(word)) {     // too many copies → shrink
                window.merge(s.substring(left, left + wl), -1, Integer::sum);
                left += wl;
                count--;
            }
            if (count == m) {                               // full match at `left`
                res.add(left);
                window.merge(s.substring(left, left + wl), -1, Integer::sum);
                left += wl;
                count--;
            }
        }
    }
    return res;
}
```

```python
# python
# LC 30 - Substring with Concatenation of All Words
# IDEA: wordLen independent sliding windows (one per offset) + freq map & match counter
# time = O(wordLen * n), space = O(m * wordLen)
from collections import Counter, defaultdict

def findSubstring(s, words):
    if not s or not words:
        return []
    wl, m = len(words[0]), len(words)
    need = Counter(words)
    res = []

    for offset in range(wl):                       # one window per alignment
        left, count = offset, 0
        window = defaultdict(int)

        for right in range(offset, len(s) - wl + 1, wl):
            word = s[right:right + wl]

            if word not in need:                   # unusable chunk → hard reset
                window.clear()
                count, left = 0, right + wl
                continue

            window[word] += 1
            count += 1
            while window[word] > need[word]:       # too many copies → shrink
                window[s[left:left + wl]] -= 1
                left += wl
                count -= 1
            if count == m:                         # full match at `left`
                res.append(left)
                window[s[left:left + wl]] -= 1
                left += wl
                count -= 1
    return res
```

#### Dry Run — `s = "barfoothefoobarman", words = ["foo","bar"]` (wl=3, m=2)

```text
offset = 0 → chunks: bar foo the foo bar man
  right=0  "bar" ✓  count=1
  right=3  "foo" ✓  count=2 == m → record left=0, drop "bar", left=3, count=1
  right=6  "the" ✗  reset, left=9
  right=9  "foo" ✓  count=1
  right=12 "bar" ✓  count=2 == m → record left=9 ✓
offset = 1 → chunks: arf oot hef oob arm  → all ✗, nothing
offset = 2 → chunks: rfo oth efo oba rma  → all ✗, nothing
result = [0, 9]
```

#### Variation — LC 187 Repeated DNA Sequences (fixed-length window + rolling hash)

*Twist: the window length is constant (10) and you only need "seen before?", so replace the frequency map with a set — and encode each 4-letter base in 2 bits so the window's identity updates in O(1) instead of re-hashing a 10-char substring.*

```java
// LC 187 - Repeated DNA Sequences
// IDEA: fixed-size window of 10 + 2-bit rolling encode (A=0,C=1,G=2,T=3) + HashSet
// time = O(n), space = O(n)
public List<String> findRepeatedDnaSequences(String s) {
    int L = 10;
    List<String> res = new ArrayList<>();
    if (s.length() < L) return res;

    int[] code = new int[26];
    code['C' - 'A'] = 1; code['G' - 'A'] = 2; code['T' - 'A'] = 3;   // 'A' stays 0
    int mask = (1 << (2 * L)) - 1, h = 0;
    Set<Integer> seen = new HashSet<>(), added = new HashSet<>();

    for (int i = 0; i < s.length(); i++) {
        h = ((h << 2) | code[s.charAt(i) - 'A']) & mask;   // push 2 bits, drop the oldest
        if (i >= L - 1) {
            if (!seen.add(h) && added.add(h)) res.add(s.substring(i - L + 1, i + 1));
        }
    }
    return res;
}
```

```python
# python
# LC 187 - Repeated DNA Sequences
# IDEA: fixed-size window of 10 + 2-bit rolling encode (A=0,C=1,G=2,T=3) + set
# time = O(n), space = O(n)
def findRepeatedDnaSequences(s):
    L = 10
    if len(s) < L:
        return []
    code = {'A': 0, 'C': 1, 'G': 2, 'T': 3}
    mask = (1 << (2 * L)) - 1
    h = 0
    seen, out = set(), set()

    for i, ch in enumerate(s):
        h = ((h << 2) | code[ch]) & mask       # push 2 bits, drop the oldest
        if i >= L - 1:
            if h in seen:
                out.add(s[i - L + 1:i + 1])
            else:
                seen.add(h)
    return list(out)
```


### Two Pointers on Sorted Intervals — LC 1229 ⭐⭐⭐

> Filed here because it is *not* a sliding window: there is no single window with a validity
> test, but two independently advancing pointers over two sorted lists. It reaches for the same
> instinct, so it is worth being able to tell them apart. The interval family proper lives in
> [intervals.md](./intervals.md) and [scanning_line.md](./scanning_line.md).

**When to use:** Two sorted interval arrays; find the first (or all) overlapping interval(s) that satisfy a duration/length requirement.

#### Core Idea

```text
Sort both interval arrays by start time.
Use one pointer per array (i, j).
At each step:
  overlap = [max(start_i, start_j), min(end_i, end_j)]
  If overlap length >= required → answer found.
  Otherwise, advance the pointer whose interval ends EARLIER.

Why advance the earlier-ending interval?
  The interval that ends first can NEVER produce a larger overlap
  with any future interval — it's already exhausted.
  Keeping the later-ending interval gives the best chance of
  overlapping with something further right.
```

#### Pattern

```text
Step 1: Sort both arrays by start time — O(n log n + m log m)
Step 2: i = 0, j = 0 (one pointer per array)
Step 3: while i < len(A) and j < len(B):
          overlapStart = max(A[i][0], B[j][0])
          overlapEnd   = min(A[i][1], B[j][1])
          if overlapEnd - overlapStart >= duration:
              return [overlapStart, overlapStart + duration]
          if A[i][1] < B[j][1]:   # A[i] ends earlier → advance i
              i++
          else:                    # B[j] ends earlier (or tie) → advance j
              j++
Step 4: return [] (no valid overlap found)
```

#### Template (Java) — Meeting Scheduler

```java
// LC 1229 - Meeting Scheduler
// IDEA: Sort + Two Pointers on interval arrays
// time = O(n log n + m log m), space = O(1)
public List<Integer> minAvailableDuration(int[][] slots1, int[][] slots2, int duration) {
    Arrays.sort(slots1, (a, b) -> a[0] - b[0]);
    Arrays.sort(slots2, (a, b) -> a[0] - b[0]);

    int i = 0, j = 0;
    while (i < slots1.length && j < slots2.length) {
        int overlapStart = Math.max(slots1[i][0], slots2[j][0]);
        int overlapEnd   = Math.min(slots1[i][1], slots2[j][1]);

        if (overlapEnd - overlapStart >= duration) {
            return Arrays.asList(overlapStart, overlapStart + duration);
        }

        // advance the pointer whose interval ends earlier
        if (slots1[i][1] < slots2[j][1]) {
            i++;
        } else {
            j++;
        }
    }
    return Collections.emptyList();
}
```

#### Dry Run — `slots1=[[10,50],[60,120],[140,210]], slots2=[[0,15],[60,70]], duration=8`

```text
i  j  overlapStart  overlapEnd  length  action
0  0  max(10,0)=10  min(50,15)=15   5   < 8 → slots1[0][1]=50 > slots2[0][1]=15 → j++
0  1  max(10,60)=60 min(50,70)=50  -10  < 8 → slots1[0][1]=50 < slots2[1][1]=70 → i++
1  1  max(60,60)=60 min(120,70)=70  10  ≥ 8 → return [60, 68] ✓
```

#### Similar LeetCode Problems

| Problem | LC# | Difficulty | Key Insight |
|---------|-----|------------|-------------|
| **Meeting Scheduler** | **1229** | **Medium** | Core example — first overlap of duration d across two slot arrays |
| Interval List Intersections | 986 | Medium | Collect ALL overlaps between two sorted interval lists; same advance-earlier-end rule |
| Employee Free Time | 759 | Hard | Merge all employee intervals, find gaps — same sorted multi-list pointer idea |
| Merge Intervals | 56 | Medium | Single sorted interval list; merge overlapping intervals greedily |
| Insert Interval | 57 | Medium | Insert + merge into a sorted interval list in one pass |
| Meeting Rooms | 252 | Easy | Check if any two intervals overlap (sort by start, compare adjacent ends) |
| Meeting Rooms II | 253 | Medium | Count minimum rooms needed; sort starts/ends separately with two pointers |
| Non-overlapping Intervals | 435 | Medium | Greedy — remove minimum intervals to make remainder non-overlapping |

#### Pattern Recognition

```text
✅ Use Sort + Two Pointers on Intervals when:
   - Two sorted interval arrays, find first/all overlaps
   - "Earliest common availability" type problems
   - Merging or intersecting two independently sorted lists

✅ Related patterns:
   - Single interval list → sort + greedy scan (LC 56, 435)
   - Min rooms / conflicts → sort starts & ends separately (LC 253)
   - All intersections → same two-pointer loop, collect instead of return early (LC 986)
```


## Summary & Quick Reference

### Which advanced shape? — Decision Table

| The problem says | Reach for | Section |
|---|---|---|
| "maximum/minimum of every window of size k" | monotonic deque | Monotonic Deque for Window Extrema — LC 239 |
| "at most K distinct" (longest) | Template 3 with `map.size() > K` | At Most K Distinct Elements |
| "exactly K …" (count), unrestricted alphabet | `atMost(K) - atMost(K-1)` | [Template 6](./sliding_window.md#template-6-exactly-k-via-at-most-subtraction--lc-992-) |
| "exactly K …" but only certain characters allowed | atMost + hard reset on a forbidden char | Count Vowel Substrings — LC 2062 |
| "exactly K …" and values may be **negative** | prefix sum + HashMap — no window works | Prefix Sum + HashMap vs Sliding Window |
| "exactly K …" and you want a single pass | prefix trick (count the left gap) | Prefix Trick Inside the Window — LC 1248 |
| "minimum operations removing from both ends" | `n - max middle subarray with sum = total - x` | Min Operations → Max Subarray Length — LC 1658 |
| "take k cards from either end" | `total - min window of size n-k` | Complement Window — LC 1423 |
| "two/three non-overlapping subarrays, max sum" | window sums + prefix/suffix argmax | Multiple Non-Overlapping Fixed Windows — LC 689 |
| "`|i-j| <= k` **and** `|nums[i]-nums[j]| <= t`" | index window + value buckets | Fixed-Index Window + Bucketing — LC 220 |
| "concatenation of all words" / fixed-length k-mers | one window per alignment offset | Word-Level Sliding Window — LC 30 |
| "earliest common availability in two schedules" | two pointers, advance the earlier end | Two Pointers on Sorted Intervals — LC 1229 |

### The one question that picks the tool

> **Is validity monotonic?** If a window that is invalid stays invalid as `right` advances, one
> `while (invalid) shrink` is enough and everything is a plain window. If not — "exactly k",
> negative values, or an equality that cannot be rephrased as "at most" — you need the at-most
> subtraction, a prefix map, or the prefix trick. That single question separates every technique
> on this page.

### Additional High-Frequency Sliding-Window References

*Famous problems that reuse the templates in [sliding_window.md](./sliding_window.md) — listed for recognition, no new technique.*

| Problem | LC# | Difficulty | Which template / one-line insight |
|---------|-----|------------|-----------------------------------|
| Maximum Length of Repeated Subarray | 718 | Medium | Interview default is DP (`dp[i][j]`); the sliding-window framing slides one array over the other and measures the longest run of matches at each alignment — O(n·m) but O(1) extra space |
| Maximum Number of Occurrences of a Substring | 1297 | Medium | Fixed-size window of `minSize` only — a longer valid substring always contains a valid `minSize` one, so `maxSize` is a red herring |
| New 21 Game | 837 | Medium | Sliding window over a DP array: `dp[i] = (window sum of previous maxPts probabilities) / maxPts`, maintained in O(1) per step |
| Max Value of Equation | 1499 | Hard | Window constrained by `xj - xi <= k` + monotonic deque on `yi - xi` — see [monotonic_queue.md](monotonic_queue.md) |

### Not covered here

- **Windows over an unbounded stream** — a window whose right edge is a live feed rather than an
  index into an array. Nothing on this sheet assumes random access except the fixed-index eviction
  in LC 220, so the templates transfer directly; the reservoir / decayed-counter techniques for
  streams live in [streaming_algorithms.md](./streaming_algorithms.md).
- **The deque family in depth** — [monotonic_queue.md](./monotonic_queue.md).
- **Median of every window (LC 480)** — two heaps with lazy deletion, not a deque; see
  [heap.md](./heap.md).
