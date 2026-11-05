# Sliding Window — Advanced Window Shapes

> **Scope** — The sliding-window techniques a first pass should skip: the at-most-K-distinct family, exactly-K counting with a forbidden-element reset or in one pass, several non-overlapping windows at once, and windows whose key is a word rather than a character; the seven must-know templates stay in the main sheet, and the advanced shapes another sheet owns (deque extrema, complement windows, value buckets) are pointers here.
> **See also**: [sliding_window.md](./sliding_window.md) — the seven canonical templates every technique here builds on; [sliding_window_examples.md](./sliding_window_examples.md) — the worked problems that need no advanced technique; [monotonic_queue.md](./monotonic_queue.md) — window extrema with a deque; [prefix_sum_advanced.md](./prefix_sum_advanced.md) — the complement window ("take from both ends"); [prefix_sum.md](./prefix_sum.md) — the prefix-based alternative when a window sum can go negative.

## LeetCode Problem Lists

- [Sliding Window](https://leetcode.com/problem-list/sliding-window/)
- [Prefix Sum](https://leetcode.com/problem-list/prefix-sum/)
- [Monotonic Queue](https://leetcode.com/problem-list/monotonic-queue/)
- [Rolling Hash](https://leetcode.com/problem-list/rolling-hash/)

## Overview

Every technique below is a template from [sliding_window.md](./sliding_window.md) with one slot
changed: the shrink test counts distinct keys, the window is reset by a forbidden element, the
count of valid starts is carried across steps, several windows are scored at once, or the window
steps by whole words. At-most-K distinct (Template 1) and the exactly-K reset (Template 2) come
up in L3 interviews; the rest are rarer, and each card says what simpler template usually does
the job instead.

### Prerequisites

- [sliding_window § Template 2](./sliding_window.md#template-2-grow-then-shrink-the-while-invariant--lc-1004-): the grow-then-shrink loop. Every template here keeps it.
- [sliding_window § Template 3](./sliding_window.md#template-3-longest-window-satisfying-p--lc-3-): the longest valid window. Template 1 is it with a distinct-count test.
- [sliding_window § Template 6](./sliding_window.md#template-6-exactly-k-via-at-most-subtraction--lc-992-): exactly K as `at_most(K) − at_most(K − 1)`. Templates 2 and 3 extend it.
- [sliding_window § Template 1](./sliding_window.md#template-1-fixed-size-window--lc-643-): the fixed-size window. Templates 4 and 5 build on it.

## Recognition

| Signal in the statement | Constraint cue | Reach for | Not this if |
|---|---|---|---|
| "longest subarray with **at most K distinct** values" | n ≤ 10⁵ | [Template 1](#template-1-at-most-k-distinct--lc-904-) | the count of distinct values must be exactly K and you are counting, not measuring → Template 2 |
| "count substrings with **exactly K distinct**, using **only** these characters" | n ≤ 10⁵ | [Template 2](#template-2-exactly-k-with-a-forbidden-element-reset--lc-2062-) | every element is allowed → [sliding_window § Template 6](./sliding_window.md#template-6-exactly-k-via-at-most-subtraction--lc-992-) as written |
| "count subarrays with **exactly k** odd numbers / ones", in one pass | n ≤ 5·10⁴, values ≥ 0 | [Template 3](#template-3-one-pass-exactly-k--count-the-left-gap--lc-1248-) | values can be negative, or the target is a sum → prefix sum + hash map |
| "**three** (or two) **non-overlapping** subarrays of length k with maximum sum" | n ≤ 2·10⁴ | [Template 4](#template-4-non-overlapping-fixed-windows--prefixsuffix-argmax--lc-689-) | the number of windows is a parameter → DP over (windows used, prefix) |
| "concatenation of **all the words**", every word the same length | word length ≤ 30 | [Template 5](#template-5-word-level-window-one-pass-per-offset--lc-30-) | the window is single characters → [hash_map § Template 5](./hash_map.md#template-5-fixed-window-character-counts--lc-438) |
| "maximum / minimum of **every window** of size k" | n ≤ 10⁵ | monotonic deque — [monotonic_queue § Template 1](./monotonic_queue.md#template-1-sliding-window-maximum-decreasing-deque--lc-239) | the answer is a median → two heaps, [monotonic_queue § Template 4](./monotonic_queue.md#template-4-sliding-window-median--ordered-multiset--two-heaps-lc-480-) |
| "take k cards from **either end**", "remove from both ends until the sum is x" | n ≤ 10⁵, values ≥ 1 | complement window — [prefix_sum_advanced § Template 9](./prefix_sum_advanced.md#template-9-complement-trick--total--middle-window---lc-1423) | picks may come from the middle → the leftover is not one window |
| "`abs(i − j) ≤ k` **and** `abs(nums[i] − nums[j]) ≤ t`" | n ≤ 10⁵ | value buckets of width `t + 1` — [sort § Template 8](./sort.md#template-8-bucket-sort-by-value-range-bucket--value--width--lc-220-) | the values must be equal → a plain set over the index window (LC 219) |

## At-Most-K and Exactly-K Counting

### Template 1: At Most K Distinct — LC 904 ⭐⭐⭐⭐

**Recognise** — "the longest subarray / substring with **at most K distinct** values", "two
baskets, one fruit type each". n ≤ 10⁵ → O(n).

**Key point** — `len(basket)` is the number of distinct values in the window, because a key is
deleted the moment its count reaches 0. A window with ≤ K distinct values stays valid when it
shrinks, so the Template 3 loop applies with `while len(basket) > K`.

```python
# python
# LC 904 - Fruit Into Baskets
# IDEA: longest window with at most 2 distinct values; shrink while the map has more than 2 keys
# time = O(n), space = O(1)   (the map holds at most 3 keys)
def totalFruit(fruits):
    basket = {}                 # fruit type -> count in the window
    left = 0
    best = 0
    for right in range(len(fruits)):
        f = fruits[right]
        basket[f] = basket.get(f, 0) + 1
        while len(basket) > 2:  # more than 2 types -> shrink
            out = fruits[left]
            basket[out] -= 1
            if basket[out] == 0:
                del basket[out]  # len(basket) must stay the true number of types
            left += 1
        best = max(best, right - left + 1)
    return best
```

```java
// java
// LC 904 - Fruit Into Baskets
// IDEA: longest window with at most 2 distinct values; shrink while the map has more than 2 keys
// time = O(n), space = O(1)
public int totalFruit(int[] fruits) {
    Map<Integer, Integer> basket = new HashMap<>();   // fruit type -> count in the window
    int left = 0;
    int best = 0;
    for (int right = 0; right < fruits.length; right++) {
        basket.put(fruits[right], basket.getOrDefault(fruits[right], 0) + 1);
        while (basket.size() > 2) {                   // more than 2 types -> shrink
            int out = fruits[left];
            basket.put(out, basket.get(out) - 1);
            if (basket.get(out) == 0) {
                basket.remove(out);                   // size() must stay the true type count
            }
            left++;
        }
        best = Math.max(best, right - left + 1);
    }
    return best;
}
```

**Complexity**
- **Time O(n)**: `left` only moves forward, so the inner `while` runs at most n times in total.
- **Space O(K)**: `basket` holds at most K + 1 keys, because the `while` removes the extra one at once.

**Traps**
- Decrementing without `del`: on `[1, 2, 3, 2, 2]` the zero entry for 1 keeps `len(basket)` at 3,
  so the `while` never stops and runs `left` off the end of the array (`IndexError`, not 4).
- Recording `best` inside the `while` measures windows with K + 1 types: `[1, 2, 3]` would report 3.

**Not this if** — you are asked how many subarrays have **exactly** K distinct values → run this
loop twice with the counting slot ([sliding_window § Template 6](./sliding_window.md#template-6-exactly-k-via-at-most-subtraction--lc-992-)).

**Drill** — LC 904 · LC 159 *(the same on characters)* · LC 340 *(`> K` instead of `> 2`)* ·
LC 992 *(exactly K: this loop twice, counting)*

### Template 2: Exactly K with a Forbidden-Element Reset — LC 2062 ⭐⭐⭐⭐

**Recognise** — "count substrings that use **only** vowels and contain **all five**", "exactly K
distinct, and the substring may not contain …". n ≤ 10⁵.

**Key point** — the windows with at most K distinct values, minus those with at most K − 1, are
exactly the windows with K. A forbidden character cannot sit in any counted window, so `at_most`
resets on it: clear the map and move `left` past it. The subtraction is unchanged, because both
calls reset at the same places.

```python
# python
# LC 2062 - Count Vowel Substrings of a String
# IDEA: exactly 5 distinct vowels = at_most(5) - at_most(4); a consonant resets the window
# time = O(n), space = O(1)   (at most 5 keys)
def countVowelSubstrings(word):
    vowels = set("aeiou")

    def at_most(max_distinct):
        count = {}
        left = 0
        total = 0
        for right in range(len(word)):
            ch = word[right]
            if ch not in vowels:          # a consonant ends every window: reset past it
                count.clear()
                left = right + 1
                continue
            count[ch] = count.get(ch, 0) + 1
            while len(count) > max_distinct:
                out = word[left]
                count[out] -= 1
                if count[out] == 0:
                    del count[out]
                left += 1
            total += right - left + 1     # vowel-only substrings ending at right with <= max_distinct
        return total

    return at_most(5) - at_most(4)
```

```java
// java
// LC 2062 - Count Vowel Substrings of a String
// IDEA: exactly 5 distinct vowels = atMost(5) - atMost(4); a consonant resets the window
// time = O(n), space = O(1)
public int countVowelSubstrings(String word) {
    return atMost(word, 5) - atMost(word, 4);
}

private int atMost(String word, int maxDistinct) {
    Map<Character, Integer> count = new HashMap<>();
    int left = 0;
    int total = 0;
    for (int right = 0; right < word.length(); right++) {
        char ch = word.charAt(right);
        if ("aeiou".indexOf(ch) < 0) {               // a consonant ends every window
            count.clear();
            left = right + 1;
            continue;
        }
        count.put(ch, count.getOrDefault(ch, 0) + 1);
        while (count.size() > maxDistinct) {
            char out = word.charAt(left);
            count.put(out, count.get(out) - 1);
            if (count.get(out) == 0) {
                count.remove(out);
            }
            left++;
        }
        total += right - left + 1;
    }
    return total;
}
```

**Complexity**
- **Time O(n)**: two `at_most` passes, each with a `left` that only moves forward.
- **Space O(1)**: `count` never holds more than six vowels.

**Dry run** — why the subtraction counts exactly K, on `[1, 2, 1, 3]` with K = 2:

```text
right  at_most(2) adds          at_most(1) adds
0      [1]              1       [1]           1
1      [2] [1,2]        2       [2]           1
2      [1] [2,1] [1,2,1] 3      [1]           1
3      [3] [1,3]        2       [3]           1
total                   8                     4     exactly 2 = 8 - 4 = 4: [1,2] [1,2,1] [2,1] [1,3]
```

**Traps**
- Forgetting the reset: on `"aeiouxa"` the window runs across `x` and the answer comes out 3
  instead of 1.
- Generalising to `exactly(k)` with `k = 0` calls `at_most(−1)`, whose `while len(count) > −1`
  never stops shrinking; return 0 for `k = 0` before subtracting.
- Subtracting two **longest-window** lengths gives nothing meaningful: the identity holds for
  counts (`total += right − left + 1`), not for `max(len)`.

**Not this if** — the condition is a sum that can go negative, or an equality no "at most" can
express ("sum equals k" over negatives, LC 560) → [hash_map § Template 4](./hash_map.md#template-4-prefix-sum-count-map--lc-560-).

**Drill** — LC 992 *(no forbidden element)* · LC 2062 · LC 930 *(exactly `goal` ones)* · LC 1248
*(exactly k odds; also Template 3)*

### Template 3: One-Pass Exactly K — Count the Left Gap — LC 1248 ⭐⭐⭐

**Recognise** — "count subarrays with **exactly k** odd numbers / ones", values are an indicator
(odd or even, 1 or 0), and a single pass with O(1) space is wanted.

**Key point** — once the window holds k odds, every start from `left` up to the first odd gives
a valid subarray. The `while odd == k` loop walks `left` over those starts and counts them into
`gap`. `gap` stays valid for every later `right` until a new odd arrives, so it is added at each
step and reset only on an odd.

```python
# python
# LC 1248 - Count Number of Nice Subarrays
# IDEA: gap = number of valid starts for the current k odds; add it at every right, reset on a new odd
# time = O(n), space = O(1)
def numberOfSubarrays(nums, k):
    left = 0
    odd = 0
    gap = 0                 # valid starts for the current run of k odds
    total = 0
    for right in range(len(nums)):
        if nums[right] % 2 == 1:
            odd += 1
            gap = 0         # a new odd: the k odds in play changed, recount the starts
        while odd == k:
            gap += 1        # start at `left` is valid; try the next one
            if nums[left] % 2 == 1:
                odd -= 1
            left += 1
        total += gap        # every start counted in gap still works for this right
    return total
```

```java
// java
// LC 1248 - Count Number of Nice Subarrays
// IDEA: gap = number of valid starts for the current k odds; add it at every right, reset on a new odd
// time = O(n), space = O(1)
public int numberOfSubarrays(int[] nums, int k) {
    int left = 0;
    int odd = 0;
    int gap = 0;          // valid starts for the current run of k odds
    int total = 0;
    for (int right = 0; right < nums.length; right++) {
        if (nums[right] % 2 == 1) {
            odd++;
            gap = 0;      // a new odd: recount the valid starts
        }
        while (odd == k) {
            gap++;
            if (nums[left] % 2 == 1) {
                odd--;
            }
            left++;
        }
        total += gap;
    }
    return total;
}
```

**Complexity**
- **Time O(n)**: `left` crosses the array once in total.
- **Space O(1)**: four integers. The prefix-sum + hash map version is O(n) space.

**Dry run** — `nums = [2, 2, 1, 2, 1], k = 2`:

```text
right  value  odd  gap  action                               total
0      2      0    0                                         0
1      2      0    0                                         0
2      1      1    0    new odd, gap reset                   0
3      2      1    0                                         0
4      1      2    0    new odd -> while: skip 2, 2, 1       3
                   3    (starts 0, 1, 2 are valid)
answer 3: [2,2,1,2,1] [2,1,2,1] [1,2,1]
```

**Traps**
- Not resetting `gap` on a new odd: `[1, 1, 1], k = 1` keeps adding the old gap after the second
  odd and returns 6 instead of 3.
- Counting only inside the `while`: the evens after the k-th odd (`[1, 2, 2], k = 1`) each extend
  every counted subarray, and are missed — 1 instead of 3.

**Not this if** — values can be negative or the target is a sum of arbitrary values → prefix
sum + hash map, [prefix_sum § Template 6](./prefix_sum.md#template-6-transform-and-count--lc-1248)
(the same problem, counted with `{prefix: count}`); when clarity matters more than one pass,
Template 2's subtraction is easier to explain.

**Drill** — LC 1248 · LC 930 *(ones instead of odds)* · LC 1358 *(count windows that contain all
three letters: the same "every start before the first needed one" count)*

## Several Windows at Once

### Template 4: Non-Overlapping Fixed Windows — Prefix/Suffix Argmax — LC 689 ⭐⭐⭐

**Recognise** — "**three** non-overlapping subarrays of length k with maximum total", "two
non-overlapping subarrays of lengths L and M". n ≤ 2·10⁴.

**Key point** — fix the middle window. The best left window then lies anywhere in `[0, mid − k]`
and the best right window anywhere in `[mid + k, end]`, independently, so a prefix argmax and a
suffix argmax over the window sums answer both in O(1) per `mid`.

```python
# python
# LC 689 - Maximum Sum of 3 Non-Overlapping Subarrays
# IDEA: window sums w[i]; prefix argmax and suffix argmax of w; try every middle window
# time = O(n), space = O(n)
def maxSumOfThreeSubarrays(nums, k):
    n = len(nums)
    w = [0] * (n - k + 1)                  # w[i] = sum of the window starting at i
    cur = sum(nums[:k])
    w[0] = cur
    for i in range(k, n):
        cur += nums[i] - nums[i - k]
        w[i - k + 1] = cur

    m = len(w)
    left = [0] * m                         # left[i]  = best start in [0, i]
    right = [0] * m                        # right[i] = best start in [i, m - 1]
    best = 0
    for i in range(m):
        if w[i] > w[best]:                 # strict: the earliest start wins a tie
            best = i
        left[i] = best
    best = m - 1
    for i in range(m - 1, -1, -1):
        if w[i] >= w[best]:                # >= while scanning leftwards: the earliest wins
            best = i
        right[i] = best

    ans = None
    best_sum = -1
    for mid in range(k, m - k):            # the middle window leaves room on both sides
        l = left[mid - k]
        r = right[mid + k]
        s = w[l] + w[mid] + w[r]
        if s > best_sum:
            best_sum = s
            ans = [l, mid, r]
    return ans
```

```java
// java
// LC 689 - Maximum Sum of 3 Non-Overlapping Subarrays
// IDEA: window sums w[i]; prefix argmax and suffix argmax of w; try every middle window
// time = O(n), space = O(n)
public int[] maxSumOfThreeSubarrays(int[] nums, int k) {
    int n = nums.length;
    int[] w = new int[n - k + 1];                     // w[i] = sum of the window starting at i
    int cur = 0;
    for (int i = 0; i < n; i++) {
        cur += nums[i];
        if (i >= k) cur -= nums[i - k];
        if (i >= k - 1) w[i - k + 1] = cur;
    }
    int m = w.length;
    int[] left = new int[m];
    int[] right = new int[m];
    int best = 0;
    for (int i = 0; i < m; i++) {                     // strict > : earliest tie wins
        if (w[i] > w[best]) best = i;
        left[i] = best;
    }
    best = m - 1;
    for (int i = m - 1; i >= 0; i--) {                // >= scanning leftwards : earliest tie wins
        if (w[i] >= w[best]) best = i;
        right[i] = best;
    }
    int[] ans = null;
    int bestSum = -1;
    for (int mid = k; mid + k < m; mid++) {           // fix the middle window
        int l = left[mid - k];
        int r = right[mid + k];
        int sum = w[l] + w[mid] + w[r];
        if (sum > bestSum) {
            bestSum = sum;
            ans = new int[]{l, mid, r};
        }
    }
    return ans;
}
```

**Complexity**
- **Time O(n)**: one rolling pass for `w`, one pass each for `left` and `right`, one over `mid`.
- **Space O(n)**: the three arrays of window sums and argmaxes.

**Dry run** — `nums = [1, 2, 1, 2, 6, 7, 5, 1], k = 2`:

```text
w     = [3, 3, 3, 8, 13, 12, 6]
left  = [0, 0, 0, 3, 4, 4, 4]
right = [4, 4, 4, 4, 4, 5, 6]
mid=2: w[0] + w[2] + w[4] = 3 + 3 + 13 = 19
mid=3: w[0] + w[3] + w[5] = 3 + 8 + 12 = 23   <- best
mid=4: w[0] + w[4] + w[6] = 3 + 13 + 6 = 22
answer [0, 3, 5]
```

**Traps**
- Ties: LC 689 wants the lexicographically smallest answer. Using `>=` in the prefix scan moves
  the left window to the latest tie: `[1, 1, 2, 2], k = 1` returns `[1, 2, 3]` instead of `[0, 2, 3]`.
- The middle loop must leave a full window on each side: `mid` runs over `[k, m − k)`, not `[0, m)`.

**Not this if** — the number of windows is an input j → DP, `dp[j][i] = max(dp[j][i − 1],
dp[j − 1][i − k] + w[i − k + 1])`, O(n·j).

**Drill** — LC 1031 *(two windows of different lengths: try both orders, keep a running best
left window; worked in [prefix_sum_examples.md](./prefix_sum_examples.md))* · LC 689

## Windows Whose Key Is Not a Character

### Template 5: Word-Level Window, One Pass per Offset — LC 30 ⭐⭐⭐⭐

**Recognise** — "find every index where a **concatenation of all the words** starts", every
word the same length. The window moves a whole word at a time.

**Key point** — a valid start `s` has `s % wordLen == r` for some `r < wordLen`, and starts with
the same remainder share word boundaries. So run `wordLen` independent windows, one per offset,
each stepping by `wordLen`; inside one, it is the character-count window with words as the keys.

```python
# python
# LC 30 - Substring with Concatenation of All Words
# IDEA: one sliding window per offset in [0, wordLen); count words; shrink on a surplus copy
# time = O(wordLen * n), space = O(m * wordLen)
from collections import Counter, defaultdict

def findSubstring(s, words):
    if not s or not words:
        return []
    wl = len(words[0])
    m = len(words)
    need = Counter(words)
    res = []
    for offset in range(wl):                       # one window per alignment
        left = offset
        count = 0                                  # words in the window
        window = defaultdict(int)
        for right in range(offset, len(s) - wl + 1, wl):
            word = s[right:right + wl]
            if word not in need:                   # unusable word -> hard reset past it
                window.clear()
                count = 0
                left = right + wl
                continue
            window[word] += 1
            count += 1
            while window[word] > need[word]:       # a surplus copy -> shrink
                window[s[left:left + wl]] -= 1
                left += wl
                count -= 1
            if count == m:                         # every word used once: a match at left
                res.append(left)
                window[s[left:left + wl]] -= 1     # drop one word to keep scanning
                left += wl
                count -= 1
    return res
```

```java
// java
// LC 30 - Substring with Concatenation of All Words
// IDEA: one sliding window per offset in [0, wordLen); count words; shrink on a surplus copy
// time = O(wordLen * n), space = O(m * wordLen)
public List<Integer> findSubstring(String s, String[] words) {
    List<Integer> res = new ArrayList<>();
    if (s == null || s.isEmpty() || words.length == 0) return res;
    int wl = words[0].length();
    int m = words.length;
    Map<String, Integer> need = new HashMap<>();
    for (String w : words) need.put(w, need.getOrDefault(w, 0) + 1);
    for (int offset = 0; offset < wl; offset++) {          // one window per alignment
        int left = offset;
        int count = 0;
        Map<String, Integer> window = new HashMap<>();
        for (int right = offset; right + wl <= s.length(); right += wl) {
            String word = s.substring(right, right + wl);
            if (!need.containsKey(word)) {                 // unusable word -> hard reset
                window.clear();
                count = 0;
                left = right + wl;
                continue;
            }
            window.put(word, window.getOrDefault(word, 0) + 1);
            count++;
            while (window.get(word) > need.get(word)) {    // a surplus copy -> shrink
                String out = s.substring(left, left + wl);
                window.put(out, window.get(out) - 1);
                left += wl;
                count--;
            }
            if (count == m) {                              // a match at left
                res.add(left);
                String out = s.substring(left, left + wl);
                window.put(out, window.get(out) - 1);
                left += wl;
                count--;
            }
        }
    }
    return res;
}
```

**Complexity**
- **Time O(wordLen · n)**: `wordLen` passes of about `n / wordLen` steps each, and every step
  slices and hashes a word of length `wordLen`.
- **Space O(m · wordLen)**: `need` and `window` hold at most m distinct words.

**Dry run** — `s = "barfoothefoobarman", words = ["foo", "bar"]`:

```text
offset 0: bar foo -> count 2 == m, record 0, drop "bar"
          the     -> not a word, reset, left = 9
          foo bar -> count 2 == m, record 9
offset 1: arf oot hef oob arm -> none is a word
offset 2: rfo oth efo oba rma -> none is a word
result [0, 9]
```

**Traps**
- A single pass stepping by 1 and re-counting m words at each start is O(n · m · wordLen); the
  offsets are what make each word enter and leave a window once.
- Duplicate words (`["word", "good", "best", "word"]`): compare counts with `need`, never a set,
  or the surplus shrink never fires.

**Not this if** — the window is a fixed length and you only ask "seen before?" (LC 187, repeated
DNA) → a rolling hash, in [string_matching_kmp_rolling_hash.md](./string_matching_kmp_rolling_hash.md).

**Drill** — LC 438 *(the same window on characters)* · LC 30 · LC 187 *(fixed-length window
with a rolling hash)*

## Look-Alikes That Are Not One Window

### Meeting Scheduler — LC 1229

> **Key point** — two sorted lists, one pointer each. The interval that ends first cannot overlap
> anything later in the other list by more than it already does, so advance its pointer; the
> first overlap of at least `duration` is the earliest answer.

**Reduces to**: [intervals § Template 3](./intervals.md#template-3-two-pointer-intersection-lc-986) — the intersection of two sorted interval lists, returning the first overlap long enough instead of collecting all of them. There is no window and no validity test, which is why it is not a sliding-window template.

```python
# python
# LC 1229 - Meeting Scheduler
# IDEA: sort both lists; overlap = [max(starts), min(ends)]; advance whichever slot ends first
# time = O(n log n + m log m), space = O(1) extra
def minAvailableDuration(slots1, slots2, duration):
    slots1 = sorted(slots1)
    slots2 = sorted(slots2)
    i = 0
    j = 0
    while i < len(slots1) and j < len(slots2):
        start = max(slots1[i][0], slots2[j][0])
        end = min(slots1[i][1], slots2[j][1])
        if end - start >= duration:
            return [start, start + duration]
        if slots1[i][1] < slots2[j][1]:    # slots1[i] ends first: it has nothing more to give
            i += 1
        else:
            j += 1
    return []
```

```java
// java
// LC 1229 - Meeting Scheduler
// IDEA: sort both lists; overlap = [max(starts), min(ends)]; advance whichever slot ends first
// time = O(n log n + m log m), space = O(1) extra
public List<Integer> minAvailableDuration(int[][] slots1, int[][] slots2, int duration) {
    Arrays.sort(slots1, (a, b) -> Integer.compare(a[0], b[0]));
    Arrays.sort(slots2, (a, b) -> Integer.compare(a[0], b[0]));
    int i = 0;
    int j = 0;
    while (i < slots1.length && j < slots2.length) {
        int start = Math.max(slots1[i][0], slots2[j][0]);
        int end = Math.min(slots1[i][1], slots2[j][1]);
        if (end - start >= duration) {
            return Arrays.asList(start, start + duration);
        }
        if (slots1[i][1] < slots2[j][1]) {
            i++;
        } else {
            j++;
        }
    }
    return new ArrayList<>();
}
```

**Traps**
- Advancing the pointer whose slot **starts** first: `slots1 = [[0, 50]], slots2 = [[10, 15],
  [20, 40]], duration = 10` drops `[0, 50]` and returns `[]` instead of `[20, 30]`.
- Unsorted input: the problem does not promise sorted slots, so sort both first.

**Similar** — LC 986 *(collect every intersection)* · LC 759 *(free time across many lists)*

## When Not to Use

- **Template 1 (at most K distinct)** — when K is 1 or "no repeats", it is
  [sliding_window § Template 3](./sliding_window.md#template-3-longest-window-satisfying-p--lc-3-)
  with a set; no counts are needed.
- **Template 2 (exactly K with a reset)** — with no forbidden element, it is
  [sliding_window § Template 6](./sliding_window.md#template-6-exactly-k-via-at-most-subtraction--lc-992-)
  as written. A long string of allowed characters can also be split on the forbidden ones first
  and Template 6 run on each piece.
- **Template 3 (one-pass gap count)** — Template 2's two passes are just as fast and easier to
  explain; reach for the gap count only when one pass is asked for. With negatives or a real sum,
  neither works: use the prefix-sum count map.
- **Template 4 (prefix/suffix argmax)** — with **two** windows, a running best-left-window and
  one scan per order is enough (LC 1031); with j windows, DP.
- **Template 5 (one pass per offset)** — when the words differ in length it does not apply at all
  (backtracking or a trie); when the window is single characters, the plain fixed window in
  [hash_map § Template 5](./hash_map.md#template-5-fixed-window-character-counts--lc-438) is it.

## Summary

### Which advanced shape? — Decision Table

| The problem says | Reach for | Where |
|---|---|---|
| "at most K distinct" (longest) | Template 3's loop with `len(map) > K` | [Template 1](#template-1-at-most-k-distinct--lc-904-) |
| "exactly K …" (count), unrestricted alphabet | `at_most(K) − at_most(K − 1)` | [sliding_window § Template 6](./sliding_window.md#template-6-exactly-k-via-at-most-subtraction--lc-992-) |
| "exactly K …" but only certain characters allowed | at-most with a hard reset on a forbidden character | [Template 2](#template-2-exactly-k-with-a-forbidden-element-reset--lc-2062-) |
| "exactly k odds / ones", one pass | count the left gap | [Template 3](#template-3-one-pass-exactly-k--count-the-left-gap--lc-1248-) |
| "exactly K …" and values may be **negative** | prefix sum + hash map — no window works | [hash_map § Template 4](./hash_map.md#template-4-prefix-sum-count-map--lc-560-) |
| "two / three non-overlapping subarrays, max sum" | window sums + prefix/suffix argmax | [Template 4](#template-4-non-overlapping-fixed-windows--prefixsuffix-argmax--lc-689-) |
| "concatenation of all words" | one window per alignment offset | [Template 5](#template-5-word-level-window-one-pass-per-offset--lc-30-) |
| "earliest common availability in two schedules" | two pointers, advance the earlier end | [Meeting Scheduler — LC 1229](#meeting-scheduler--lc-1229) |

### The one question that picks the tool

> **Is validity monotonic?** If a window that is invalid stays invalid as `right` advances, one
> `while (invalid) shrink` is enough and everything is a plain window. If not — "exactly k",
> negative values, or an equality that cannot be rephrased as "at most" — you need the at-most
> subtraction, a prefix map, or the gap count. That single question separates every technique
> on this page.

### Lives Elsewhere

Advanced window problems whose worked solution is in another sheet, by the same approach.

| LC | Problem | Technique | Worked in |
|---|---|---|---|
| 239 | Sliding Window Maximum | decreasing deque of indices | [monotonic_queue § Template 1](./monotonic_queue.md#template-1-sliding-window-maximum-decreasing-deque--lc-239) |
| 480 | Sliding Window Median | two heaps with lazy deletion | [monotonic_queue § Template 4](./monotonic_queue.md#template-4-sliding-window-median--ordered-multiset--two-heaps-lc-480-) |
| 1423 | Maximum Points You Can Obtain from Cards | `total − min window of size n − k` | [prefix_sum_advanced § Template 9](./prefix_sum_advanced.md#template-9-complement-trick--total--middle-window---lc-1423) |
| 1658 | Minimum Operations to Reduce X to Zero | `n − longest window with sum total − x` | [prefix_sum_advanced § Template 9](./prefix_sum_advanced.md#template-9-complement-trick--total--middle-window---lc-1423) (its variation) |
| 220 | Contains Duplicate III | index window + value buckets of width `t + 1` | [sort § Template 8](./sort.md#template-8-bucket-sort-by-value-range-bucket--value--width--lc-220-); the ordered-set version is [scanning_line § Template 11](./scanning_line.md#template-11-index-sweep--active-ordered-set--lc-220) |
| 187 | Repeated DNA Sequences | fixed window of 10 + 2-bit rolling hash | [string_matching_kmp_rolling_hash.md](./string_matching_kmp_rolling_hash.md) |
| 718 | Maximum Length of Repeated Subarray | DP, or binary search + rolling hash | [string_matching_kmp_rolling_hash.md](./string_matching_kmp_rolling_hash.md) |
| 1031 | Maximum Sum of Two Non-Overlapping Subarrays | prefix sums + running best left window | [prefix_sum_examples.md](./prefix_sum_examples.md) |
| 1248 | Count Number of Nice Subarrays (prefix-map version) | `{odd count: frequency}` | [prefix_sum § Template 6](./prefix_sum.md#template-6-transform-and-count--lc-1248) |

Recognition only, no new technique:

| Problem | LC# | One-line insight |
|---|---|---|
| Maximum Number of Occurrences of a Substring | 1297 | a fixed window of `minSize` only: a longer valid substring always contains a valid `minSize` one, so `maxSize` is a red herring |
| New 21 Game | 837 | a sliding window over a DP array: `dp[i]` is the window sum of the previous `maxPts` probabilities over `maxPts` |
| Max Value of Equation | 1499 | a window bounded by `xj − xi ≤ k` with a monotonic deque on `yi − xi` ([monotonic_queue.md](./monotonic_queue.md)) |

### Not covered here

- **Windows over an unbounded stream** — a window whose right edge is a live feed rather than an
  index into an array. Nothing on this sheet assumes random access, so the templates transfer
  directly; the reservoir / decayed-counter techniques for streams live in
  [streaming_algorithms.md](./streaming_algorithms.md).
- **The deque family in depth** — [monotonic_queue.md](./monotonic_queue.md).
