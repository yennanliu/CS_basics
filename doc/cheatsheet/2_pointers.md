# Two pointers

> **Scope** — The two-pointer family on arrays and strings — opposite-ends convergence, fast/slow, expand-from-centre and the read/write partition, with one canonical template for each; window problems that grow and shrink on a condition live elsewhere.
> **See also** — *deep dives split out of this file*: [2_pointers_examples.md](./2_pointers_examples.md) — the worked LC catalogue, one canonical solution per problem; [2_pointers_quickselect.md](./2_pointers_quickselect.md) — QuickSelect / partition-based Kth-element selection, which is a selection algorithm rather than a two-pointer scan.
> *Neighbouring sheets*: [sliding_window.md](./sliding_window.md) — variable-size windows driven by a condition; [2_pointers_linkedlist.md](./2_pointers_linkedlist.md) — the same idea on nodes instead of indices; [n_sum.md](./n_sum.md) — the sorted-array k-sum specialisation; [palindrome.md](./palindrome.md) — expand-from-centre in depth.

## LeetCode Problem Lists

- [Two Pointers](https://leetcode.com/problem-list/two-pointers/)
- [Sliding Window](https://leetcode.com/problem-list/sliding-window/)
- [Array](https://leetcode.com/problem-list/array/)

## Overview

**Two pointers** keeps two indices into the same sequence (or one into each of two sequences) and advances them under a rule, replacing a nested loop with a single linear pass.

### Key Properties
- **Complexity**: O(N) for a single scan, O(N log N) when the input must be sorted first, O(N^2) for expand-from-centre over every index; O(1) extra space — the pointers are the only state
- **Core Idea**: every iteration advances at least one pointer and never revisits an index, so total work is bounded by the number of pointer moves
- **When to Use**: sorted input, in-place rewriting, palindromes, merging two sorted sequences, subsequence checks, cycle detection
- **Key Techniques**: fast/slow (same direction), left/right (converging), expand-from-centre, one-pointer-always-moves

### References
- [fucking-algorithm : 2 pointers](https://labuladong.online/algo/essential-technique/array-two-pointers-summary/#%E5%8E%9F%E5%9C%B0%E4%BF%AE%E6%94%B9)

## Recognition

| Signal in the statement | Constraint cue | Reach for | Not this if |
|---|---|---|---|
| "remove … **in-place**", "return k, the first k elements", "O(1) extra memory" | n ≤ 3·10⁴, sorted or a fixed `val` | [Template 1](#template-1-fastslow-read-write-compaction--lc-26-) fast/slow compaction — LC 26, LC 27, LC 80, LC 283 | the order of the kept elements does not matter and writes are costly → Template 2's bidirectional LC 27 |
| sorted array, "a pair whose sum …", "max area between two lines", "palindrome" | n ≤ 10⁵ → O(n), or O(n log n) with a sort first | [Template 2](#template-2-converging-pointers-from-both-ends--lc-11-) converging pointers — LC 11, LC 125, LC 167, LC 344 | unsorted and the answer is indices → hash map complement lookup ([hash_map.md](./hash_map.md)) |
| "longest / count palindromic **substring**" | n ≤ 1000 → O(n²) is fine | [Template 3](#template-3-expand-from-centre--lc-5-) expand from centre — LC 5, LC 647 | palindromic **subsequence** → interval DP (LC 516) |
| "is s a **subsequence** of t", "formed by deleting some characters" | len(t) ≤ 10⁴ | [Template 4](#template-4-subsequence-matching-one-pointer-always-moves--lc-392-) one pointer always moves — LC 392, LC 524, LC 1023, LC 809 | contiguous **substring** match → KMP / window; longest common subsequence → DP (LC 1143) |
| "merge nums2 into nums1", nums1 has trailing empty slots | m + n ≤ 200, O(1) space asked | [Template 5](#template-5-merge-two-sorted-arrays-fill-from-the-back--lc-88-) fill from the back — LC 88, LC 977, LC 986 | two linked lists → merge forward with a dummy head (LC 21) |
| "sort an array of 0, 1, 2 in one pass", "partition by value into three groups" | O(1) space, one pass | [Template 6](#template-6-three-way-partition-dutch-national-flag--lc-75-) Dutch national flag — LC 75, LC 905 | the relative order must be kept → a **stable** list partition (LC 86) is [linked_list.md's two dummies](./linked_list.md#two-dummies--split-into-chains-then-join-lc-86-) |
| "does the linked list have a cycle", "where does it begin", "find the duplicate without modifying" | O(1) memory | [Template 7](#template-7-fastslow-cycle-detection--lc-141-) Floyd fast/slow — LC 141, LC 142, LC 287 | O(n) memory is allowed → a visited set is simpler |
| window that grows on one condition, shrinks on another | n ≤ 10⁵ | [Template 8](#template-8-grow-right-then-shrink-left--lc-567) grow right, shrink left — LC 567, LC 209 | any real window problem → [sliding_window.md](./sliding_window.md), which owns the family |
| kth largest / k closest (select, do not sort) | n ≤ 10⁵, average O(n) | [2_pointers_quickselect.md](./2_pointers_quickselect.md) — LC 215, LC 973 | k is tiny or the input streams → heap |

Constraint cues come from the ruler in [complexity_cheatsheet.md](./complexity_cheatsheet.md). Everything else — the long tail of worked problems — lives in [2_pointers_examples.md](./2_pointers_examples.md).

## Problem Categories

### Pointer Types

- Pointer types

    - `Fast - Slow pointers`
        - fast, slow pointers from `same start point`

    - `Left- Right pointers`
        - left, right pointers from `idx = 0, idx = len(n) - 1` respectively
        - Usually set
            - left pointer = 0
            - right pointer = len(nums)
        - [binary search](https://github.com/yennanliu/CS_basics/blob/master/doc/cheatsheet/binary_search.md)
        - array reverse
        - [2 sum](https://github.com/yennanliu/CS_basics/blob/master/doc/cheatsheet/n_sum.md)
        - [sliding window](https://github.com/yennanliu/CS_basics/blob/master/doc/cheatsheet/sliding_window.md)

- `Expand` from center (and Deal with `odd, even` cases)
    - LC 680
    - LC 647
    - LC 005

- Merge Sorted Array
    - LC 88

- Minimum Swaps to Group All 1's Together
    - LC 1151 (check [sliding_window.md](https://github.com/yennanliu/CS_basics/blob/master/doc/cheatsheet/sliding_window.md))

- Boats to Save People
    - LC 881

- `Sort + Fix-One + Two-Pointer` (closest / smaller sum)
    - Fix `i`, squeeze `l`/`r` inward; track closest by `|sum - target|`
    - LC 16 (3Sum Closest)
    - LC 259 (3Sum Smaller)

- move `right pointer first`, then move `left point` per condition
    - LC 567
    - LC 209 (see `sliding window cheatsheet`)

- `Subsequence Matching` with character type constraints
    - One pointer always moves, one conditionally moves
    - Extra validation on non-matching characters
    - LC 392 (Is Subsequence)
    - LC 1023 (Camelcase Matching - with uppercase/lowercase constraint)

- `Group-by-Group String Comparison`
    - Both pointers advance by group (run of same char), not by single char
    - Validate each aligned group: size must allow extension (>= 3) if counts differ
    - LC 809 (Expressive Words)

- `Find-Pivot + Find-Successor + Reverse-Suffix` (Next Permutation)
    - Scan right-to-left to find first ascending pair (pivot), then smallest-greater successor
    - Swap pivot and successor, then reverse the descending suffix → ascending
    - LC 31 (Next Permutation), LC 556 (Next Greater Element III)

- `Converging Low/High pointers to build a permutation` (greedy)
    - Consume smallest available value on one signal, largest on the other
    - `low`/`high` walk inward over the range `[0, n]`; the survivor fills the last slot
    - LC 942 (DI String Match)

- `Last-occurrence + expanding right boundary` (greedy partition)
    - Pre-compute each char's last index; cut when `i == end`
    - LC 763 (Partition Labels)

- `Staircase pointers on a sorted 2D matrix`
    - Start at the `top-right` saddle: too big → `col--`, too small → `row++`
    - LC 240 (Search a 2D Matrix II)

- `Two pointers over TWO strings (lockstep / chunk-wise)`
    - Each pointer consumes one chunk from its own string per round
    - Exhausted side → implicit `0` (LC 165) or prefix rule (LC 953)
    - LC 165 (Compare Version Numbers), LC 953 (Verifying an Alien Dictionary)

- `3-phase pointer scan over sorted intervals`
    - before → merge-overlap → after
    - LC 57 (Insert Interval)

- Algorithm
    - binary search
    - sliding window
    - for loop + "expand `left`, `right` from center"

- Data structure
    - Array
    - Linked list

## Templates & Algorithms

### Template 1: Fast/Slow Read-Write Compaction — LC 26 ⭐⭐⭐⭐⭐

**Recognise** — "remove the duplicates / every `val` **in-place** and return k, the number of
elements kept", "do not allocate extra space", "the first k elements must hold the result".
n ≤ 3·10⁴ → one O(n) pass.

**Key point** — `nums[0..slow]` always holds the kept elements, in order. `fast` reads every
index once; `slow` advances only when a value is kept, so the write slot never overtakes the
read slot and nothing unread is overwritten.

```python
# python
# LC 26 - Remove Duplicates from Sorted Array
# IDEA: fast reads every index; slow is the last kept slot, advanced only for a new value
# time = O(n), space = O(1)
def removeDuplicates(nums):
    if not nums:
        return 0
    slow = 0                          # nums[0..slow] = the unique values seen so far
    for fast in range(1, len(nums)):
        if nums[fast] != nums[slow]:  # a value not kept yet
            slow += 1                 # open the next write slot
            nums[slow] = nums[fast]   # copy it forward
        # equal: a duplicate, fast moves on and slow stays
    return slow + 1                   # slow is an index; the count is index + 1
```

```java
// java
// LC 26 - Remove Duplicates from Sorted Array
// IDEA: fast reads every index; slow is the last kept slot, advanced only for a new value
// time = O(n), space = O(1)
public int removeDuplicates(int[] nums) {
    if (nums.length == 0) {
        return 0;
    }
    int slow = 0;                        // nums[0..slow] = the unique values seen so far
    for (int fast = 1; fast < nums.length; fast++) {
        if (nums[fast] != nums[slow]) {
            slow++;                      // open the next write slot
            nums[slow] = nums[fast];     // overwrite with the new unique value
        }
    }
    return slow + 1;
}
```

**Complexity**
- **Time O(n)**: the single `for fast` loop; each index is read once and written at most once.
- **Space O(1)**: the two indices are the only state — the result is written into `nums` itself.

**Dry run**

```text
nums = [0,0,1,1,2]
fast=1  0 == nums[0]   skip                  slow=0
fast=2  1 != 0         slow=1, nums[1]=1     [0,1,1,1,2]
fast=3  1 == nums[1]   skip                  slow=1
fast=4  2 != 1         slow=2, nums[2]=2     [0,1,2,1,2]
return 3 -> nums[:3] = [0,1,2]
```

**Traps**
- `[]`: returning bare `return` gives `None` in Python, not `0`. Guard it explicitly.
- Allowing **k** copies (LC 80): compare against the *written* prefix, `nums[fast] != nums[write - k]`,
  never against the original neighbour `nums[fast - k]`. On `[1,1,1,2,2,3]`, k = 2, the
  `fast - k` version reads a slot already overwritten and returns `[1,1,2,3]`, losing a `2`.
- Swapping instead of overwriting (`nums[slow], nums[fast] = nums[fast], nums[slow]`) is also correct
  and keeps every value somewhere in the array; overwriting is simpler because the tail is never read.

**Not this if** — the order of the kept elements does not matter and writes are expensive: the
bidirectional LC 27 under [Template 2](#template-2-converging-pointers-from-both-ends--lc-11-)
copies from the right end and touches fewer slots when `val` is rare.

**Drill** — LC 26 ⭐ · LC 27 *(keep when `!= val`; `slow` is the next free slot)* · LC 283
*(swap with the write slot so the zeros drift right)* · LC 80 *(at most two copies: compare with
`nums[write - 2]`)* · LC 83 / LC 82 *(the same rule on a linked list, see
[2_pointers_linkedlist.md](./2_pointers_linkedlist.md))*

#### Allowing at most K copies

Generalise the comparison to `nums[fast] != nums[write - k]`, with `write` the next free slot and
the first k elements always kept: `k = 1` is LC 26, `k = 2` is LC 80. Worked in
[2_pointers_examples.md](./2_pointers_examples.md).

#### Remove Element — LC 27

> **Key point** — the same compaction with a different keep test: keep `nums[fast]` whenever it is
> not `val`. Here `slow` is the next free slot rather than the last kept one, so the answer is `slow` itself.

**Reduces to**: [Template 1](#template-1-fastslow-read-write-compaction--lc-26-) — only the keep test changes.

```python
# python
# LC 27 - Remove Element
# IDEA: copy every element that is not val down to the write slot
# time = O(n), space = O(1)
def removeElement(nums, val):
    slow = 0                          # next free write slot
    for fast in range(len(nums)):
        if nums[fast] != val:
            nums[slow] = nums[fast]
            slow += 1
    return slow
```

```java
// java
// LC 27 - Remove Element
// IDEA: copy every element that is not val down to the write slot
// time = O(n), space = O(1)
public int removeElement(int[] nums, int val) {
    int slow = 0;                        // next free write slot
    for (int fast = 0; fast < nums.length; fast++) {
        if (nums[fast] != val) {
            nums[slow] = nums[fast];
            slow++;
        }
    }
    return slow;
}
```

### Template 2: Converging Pointers from Both Ends — LC 11 ⭐⭐⭐⭐⭐

**Recognise** — a **sorted** array and "a pair whose sum is …", "two lines that hold the most
water", "is it a palindrome", "reverse in place". n ≤ 10⁵ → O(n) after an optional O(n log n) sort.

**Key point** — every step discards one end for good, and only pairs that cannot beat the current
one are discarded. For LC 11, the shorter wall caps every pair it is part of, and every remaining
partner is closer, so no pair using the shorter wall can beat the one just measured.

```python
# python
# LC 11 - Container With Most Water
# IDEA: measure the widest pair, then drop the shorter wall: it cannot do better with a narrower partner
# time = O(n), space = O(1)
def maxArea(height):
    left = 0
    right = len(height) - 1
    best = 0
    while left < right:
        width = right - left
        best = max(best, min(height[left], height[right]) * width)
        if height[left] < height[right]:   # left is the bottleneck: discard it
            left += 1
        else:                              # right is (or ties): discard it
            right -= 1
    return best
```

```java
// java
// LC 11 - Container With Most Water
// IDEA: measure the widest pair, then drop the shorter wall: it cannot do better with a narrower partner
// time = O(n), space = O(1)
public int maxArea(int[] height) {
    int left = 0;
    int right = height.length - 1;
    int best = 0;
    while (left < right) {
        int width = right - left;
        best = Math.max(best, Math.min(height[left], height[right]) * width);
        if (height[left] < height[right]) {
            left++;                       // left is the bottleneck: discard it
        } else {
            right--;
        }
    }
    return best;
}
```

**Complexity**
- **Time O(n)**: each pass of `while left < right` moves exactly one pointer inward, so the loop
  runs n − 1 times.
- **Space O(1)**: two indices and the running best.

**Traps**
- Moving the **taller** wall: `[1,8,6,2,5,4,8,3,7]` returns 8 instead of 49 — the shorter wall stays
  and keeps capping every pair.
- `while left <= right` pairs an index with itself. Harmless for LC 11 (width 0), wrong for LC 167:
  `numbers = [2,3,4]`, target 4 returns `[1,1]`, using the `2` twice.
- An unsorted input for a pair-sum: the "too small → move left" rule needs sorted order. Sort first,
  or use a hash map when the original indices are the answer.

**Not this if** — unsorted input with indices as the answer → complement lookup in
[hash_map.md](./hash_map.md) (LC 1); a contiguous range with a condition → [sliding_window.md](./sliding_window.md).

**Drill** — LC 11 ⭐ · LC 344 *(reverse: swap, then move both)* · LC 125 ⭐ *(skip non-alphanumerics
before comparing)* · LC 167 *(sum too small → `left++`)* · LC 15 *(fix one, converge the rest — see
[n_sum.md](./n_sum.md))* · LC 42 *(move the side with the lower max)*

#### Valid Palindrome — LC 125

> **Key point** — converging pointers with a skip: each side first steps past characters that do not
> count, then the pair is compared. The inner loops keep `left < right`, so `" "` is a palindrome.

**Reduces to**: [Template 2](#template-2-converging-pointers-from-both-ends--lc-11-) — the pair test is a character comparison.

```python
# python
# LC 125 - Valid Palindrome (ignore non-alphanumeric, case-insensitive)
# IDEA: converge from both ends, skipping characters that do not count
# time = O(n), space = O(1)
def isPalindrome(s):
    left, right = 0, len(s) - 1
    while left < right:
        while left < right and not s[left].isalnum():
            left += 1
        while left < right and not s[right].isalnum():
            right -= 1
        if s[left].lower() != s[right].lower():
            return False
        left += 1
        right -= 1
    return True
```

> One deletion allowed (LC 680) is the same scan with a branch at the first mismatch — see [2_pointers_examples.md](./2_pointers_examples.md).

#### Remove Element, Bidirectional Variant — LC 27

> **Key point** — a `val` at `l` is overwritten by `nums[r]` and `r` shrinks, but `l` stays put:
> the value just copied in may itself be `val`. When `l > r`, `l` is the count of kept elements.

**Reduces to**: [Template 2](#template-2-converging-pointers-from-both-ends--lc-11-) — converging ends, used as a partition instead of a pair search.

Key difference from the fast-slow (Template 1) approach:
- Fast-slow overwrites sequentially → **preserves relative order**
- Bidirectional replaces `nums[l]` with `nums[r]` → **does NOT preserve order**, but may do fewer writes (good when `val` is rare)

```java
// java
// LC 27 Remove Element - Bidirectional variant
// IDEA: a val at l is overwritten by the rightmost element; l only advances past a kept element
// time = O(n), space = O(1)
/**
 * Key Idea:
 *   - l starts at 0, r starts at nums.length - 1
 *   - If nums[l] == val, OVERWRITE it with nums[r] and shrink r
 *     (do NOT advance l yet — the new nums[l] might also be val)
 *   - If nums[l] != val, it is a "good" element → advance l
 *   - When l > r, l equals the count of valid elements
 *
 * //--------------------
 * Example 1
 * //--------------------
 * nums = [3,2,2,3], val = 3
 *
 * [3,2,2,3]   nums[l]=3==val, nums[l]=nums[r]=3, r--
 *  l     r
 *
 * [3,2,2,3]   nums[l]=3==val, nums[l]=nums[r]=2, r--
 *  l   r
 *
 * [2,2,2,3]   nums[l]=2!=val, l++
 *  l r
 *
 * [2,2,2,3]   nums[l]=2!=val, l++
 *    lr
 *
 * l(2) > r(1), return l = 2
 *
 * //--------------------
 * Example 2
 * //--------------------
 * nums = [0,1,2,2,3,0,4,2], val = 2
 *
 * [0,1,2,2,3,0,4,2]   nums[l]=0!=val, l++
 *  l               r
 *
 * [0,1,2,2,3,0,4,2]   nums[l]=1!=val, l++
 *    l             r
 *
 * [0,1,2,2,3,0,4,2]   nums[l]=2==val, nums[l]=nums[r]=2, r--
 *      l           r
 *
 * [0,1,2,2,3,0,4,2]   nums[l]=2==val, nums[l]=nums[r]=4, r--
 *      l         r
 *
 * [0,1,4,2,3,0,4,2]   nums[l]=4!=val, l++
 *      l       r
 *
 * [0,1,4,2,3,0,4,2]   nums[l]=2==val, nums[l]=nums[r]=0, r--
 *        l     r
 *
 * [0,1,4,0,3,0,4,2]   nums[l]=0!=val, l++
 *        l   r
 *
 * [0,1,4,0,3,0,4,2]   nums[l]=3!=val, l++
 *          l r
 *
 * l(5) > r(4), return l = 5
 */
public int removeElement(int[] nums, int val) {
    int l = 0;
    int r = nums.length - 1;

    while (l <= r) {
        if (nums[l] == val) {
            // Overwrite with rightmost element, shrink right boundary
            // do NOT advance l — the new nums[l] might also be val
            nums[l] = nums[r];
            r--;
        } else {
            // Good element confirmed, advance left
            l++;
        }
    }

    // l is exactly the count of non-val elements
    return l;
}
```

**Comparison: Fast-Slow vs Bidirectional**

| Aspect | Fast-Slow (Template 1) | Bidirectional (this) |
|--------|-------------------|----------------------|
| **Order** | Preserves relative order | Does NOT preserve order |
| **Writes** | One write per valid element | Fewer writes when val is rare |
| **Loop style** | `for` loop (fast advances always) | `while (l <= r)` |
| **When to use** | Order matters | Order doesn't matter, minimal writes |

**Similar Problems:**
- LC 27 Remove Element (this pattern)
- LC 905 Sort Array By Parity — move evens left, odds right (same bidirectional shrink idea)
- LC 75 Sort Colors (Dutch National Flag) — three-way bidirectional partition
- LC 283 Move Zeroes — order matters, use fast-slow instead
- LC 26 Remove Duplicates from Sorted Array — order matters, use fast-slow instead
- LC 80 Remove Duplicates from Sorted Array II — order matters, use fast-slow instead

#### Reverse an Array

The bare skeleton of the template: no pair to evaluate, just swap and move both.

```java
// java
// IDEA: swap the two ends, move both inward until they meet
// time = O(n), space = O(1)
void reverse(int[] nums) {
    int left = 0;
    int right = nums.length - 1;
    while (left < right) {
        int tmp = nums[left];
        nums[left] = nums[right];
        nums[right] = tmp;
        left++;
        right--;
    }
}
```

### Template 3: Expand from Centre — LC 5 ⭐⭐⭐⭐

**Recognise** — "longest palindromic **substring**", "count the palindromic substrings".
n ≤ 1000 → O(n²) is the intended bound.

**Key point** — every palindrome is the expansion of its centre, and there are only 2n − 1 centres:
n characters (odd length) and n − 1 gaps (even length). Expanding each centre while the two ends
match visits every palindromic substring exactly once.

```python
# python
# LC 5 - Longest Palindromic Substring
# IDEA: try all 2n - 1 centres (each char, each gap); expand while the ends match; keep the widest
# time = O(n^2), space = O(1)
def longestPalindrome(s):
    def expand(left, right):
        # grow while s[left..right] is a palindrome
        while left >= 0 and right < len(s) and s[left] == s[right]:
            left -= 1
            right += 1
        # the loop stopped one step too far on each side
        return left + 1, right          # half-open [start, end)

    best_start, best_end = 0, 0
    for center in range(len(s)):
        for left, right in ((center, center), (center, center + 1)):   # odd, then even
            start, end = expand(left, right)
            if end - start > best_end - best_start:
                best_start, best_end = start, end
    return s[best_start:best_end]
```

```java
// java
// LC 5 - Longest Palindromic Substring
// IDEA: try all 2n - 1 centres (each char, each gap); expand while the ends match; keep the widest
// time = O(n^2), space = O(1)
public String longestPalindrome(String s) {
    int bestStart = 0;
    int bestEnd = 0;                                 // half-open [bestStart, bestEnd)
    for (int center = 0; center < s.length(); center++) {
        int[] odd = expand(s, center, center);
        int[] even = expand(s, center, center + 1);
        if (odd[1] - odd[0] > bestEnd - bestStart) {
            bestStart = odd[0];
            bestEnd = odd[1];
        }
        if (even[1] - even[0] > bestEnd - bestStart) {
            bestStart = even[0];
            bestEnd = even[1];
        }
    }
    return s.substring(bestStart, bestEnd);
}

private int[] expand(String s, int left, int right) {
    while (left >= 0 && right < s.length() && s.charAt(left) == s.charAt(right)) {
        left--;
        right++;
    }
    return new int[]{left + 1, right};               // undo the last failed step
}
```

**Complexity**
- **Time O(n²)**: the `for center` loop runs 2n − 1 expansions, and each `while` inside `expand`
  can run up to n/2 steps (all-equal input `"aaaa"`).
- **Space O(1)**: only indices are kept; the substring is sliced once at the end.

**Dry run**

```text
s = "cbbd"
center 0  odd  "c"          even c|b  stop      best "c"
center 1  odd  "b"          even b|b  -> "bb", then c|d stop   best "bb"
center 2  odd  "b"          even b|d  stop
center 3  odd  "d"
return "bb"
```

**Traps**
- Odd centres only: `"cbbd"` returns `"c"` — the even centre between the two `b`s is never tried.
- The `while` exits one step past the palindrome on each side; reading `s[left:right + 1]` after it
  includes two characters that do not match. Use `left + 1` and `right` (half-open).
- Slicing a new string on every successful step turns O(n²) into O(n³); compare lengths, slice once.

**Not this if** — palindromic **subsequence** (characters may be skipped) → interval DP (LC 516);
n ≤ 10⁵ needs Manacher's O(n). The palindrome family in depth is [palindrome.md](./palindrome.md).

**Drill** — LC 5 ⭐ · LC 647 ⭐ *(count: add 1 on every successful expand step)* · LC 680 *(converging
pointers with one skip — Template 2)* · LC 516 *(subsequence → DP; the look-alike)*

> Full solutions for LC 5 and LC 647 are in [2_pointers_examples.md](./2_pointers_examples.md).

### Template 4: Subsequence Matching, One Pointer Always Moves — LC 392 ⭐⭐⭐⭐

**Recognise** — "is `s` a **subsequence** of `t`", "can be formed by deleting some characters
without changing the order", "does the query match the pattern". len(t) ≤ 10⁴ → one O(|t|) scan.

**Key point** — matching `s[i]` with the **earliest** equal `t[j]` is never worse than a later one: it
leaves the longest possible suffix of `t` for `s[i+1:]`. So `j` moves every step and `i` moves only on
a match, and `i == len(s)` at the end means every character was placed in order.

```python
# python
# LC 392 - Is Subsequence
# IDEA: j scans t every step; i advances only when s[i] matches (greedy earliest match)
# time = O(|t|), space = O(1)
def isSubsequence(s, t):
    i = 0                                   # next character of s to place
    for j in range(len(t)):                 # t's pointer always moves
        if i < len(s) and s[i] == t[j]:
            i += 1                          # matched: move s's pointer too
    return i == len(s)
```

```java
// java
// LC 392 - Is Subsequence
// IDEA: j scans t every step; i advances only when s[i] matches (greedy earliest match)
// time = O(|t|), space = O(1)
public boolean isSubsequence(String s, String t) {
    int i = 0;                                      // next character of s to place
    for (int j = 0; j < t.length(); j++) {          // t's pointer always moves
        if (i < s.length() && s.charAt(i) == t.charAt(j)) {
            i++;
        }
    }
    return i == s.length();
}
```

**Complexity**
- **Time O(|t|)**: the `for j` loop visits each character of `t` once; `i` only follows.
- **Space O(1)**: two indices.

**Dry run**

```text
s = "abc", t = "ahbgdc"
j=0 'a' == s[0]  i=1
j=1 'h'          i=1
j=2 'b' == s[1]  i=2
j=3 'g'  j=4 'd' i=2
j=5 'c' == s[2]  i=3 == len(s) -> true
```

**Traps**
- Reading `s[i]` without `i < len(s)`: `s = "a"`, `t = "aa"` indexes past the end of `s` on the second `a`.
- Returning `j == len(t)` instead of `i == len(s)`: `j` always reaches the end, so `"axc"` in
  `"ahbgdc"` would return true.
- `s = ""` is a subsequence of anything, including `""`; the loop must not require a match to return true.
- Many queries against one `t` (LC 392 follow-up, LC 792): rescanning `t` per query is O(k·|t|).
  Precompute each character's sorted index list and binary-search the next position.

**Not this if** — a **contiguous** match (substring) → KMP or a window; the longest common subsequence
of two strings → DP (LC 1143), because there the greedy choice is no longer safe.

**Drill** — LC 392 ⭐ · LC 524 *(test each dictionary word with this scan)* · LC 1023 *(an unmatched
`t` character must be lowercase)* · LC 809 *(advance by groups of equal characters)* · LC 792 *(many
queries: index lists + binary search)*

### Template 5: Merge Two Sorted Arrays, Fill from the Back — LC 88 ⭐⭐⭐⭐⭐

**Recognise** — "merge `nums2` into `nums1` **in-place**", "`nums1` has length m + n, the last n are
0 and should be ignored". m + n ≤ 200, but the point is O(1) extra space.

**Key point** — write the **largest** remaining value into the last free slot. The write index
`write = p1 + p2 + 1` is always ≥ `p1`, so a slot is only written after it has been read (or was
empty): merging backward needs no buffer, merging forward would overwrite unread `nums1` values.

```python
# python
# LC 88 - Merge Sorted Array
# IDEA: two read pointers at the ends of the valid parts, one write pointer at the very end;
#       always write the larger tail. Loop on p2 only: leftover nums1 is already in place.
# time = O(m + n), space = O(1)
def merge(nums1, m, nums2, n):
    p1 = m - 1              # read nums1 from the end of its valid part
    p2 = n - 1              # read nums2 from its end
    write = m + n - 1       # write from the very end of nums1
    while p2 >= 0:
        if p1 >= 0 and nums1[p1] > nums2[p2]:
            nums1[write] = nums1[p1]
            p1 -= 1
        else:               # nums2's tail is larger or equal, or nums1 is used up
            nums1[write] = nums2[p2]
            p2 -= 1
        write -= 1
```

```java
// java
// LC 88 - Merge Sorted Array
// IDEA: write the larger tail into the last free slot; loop until nums2 is used up
// time = O(m + n), space = O(1)
public void merge(int[] nums1, int m, int[] nums2, int n) {
    int p1 = m - 1;           // read nums1
    int p2 = n - 1;           // read nums2
    int write = m + n - 1;    // write slot
    while (p2 >= 0) {
        if (p1 >= 0 && nums1[p1] > nums2[p2]) {
            nums1[write] = nums1[p1];
            p1--;
        } else {
            nums1[write] = nums2[p2];
            p2--;
        }
        write--;
    }
}
```

**Complexity**
- **Time O(m + n)**: each pass of `while p2 >= 0` writes one slot and moves `write` left; at most
  m + n slots exist.
- **Space O(1)**: the merge happens in `nums1`'s own empty tail.

**Dry run**

```text
nums1 = [1, 2, 3, 0, 0, 0], m = 3
nums2 = [2, 5, 6],          n = 3

| Step | p1 | p2 | write | Compare                 | Action  | nums1          |
|------|----|----|-------|-------------------------|---------|----------------|
| init |  2 |  2 |   5   | —                       | setup   | [1,2,3,0,0,0]  |
|  1   |  2 |  2 |   5   | nums1[2]=3 < nums2[2]=6 | write 6 | [1,2,3,0,0,6]  |
|  2   |  2 |  1 |   4   | nums1[2]=3 < nums2[1]=5 | write 5 | [1,2,3,0,5,6]  |
|  3   |  2 |  0 |   3   | nums1[2]=3 > nums2[0]=2 | write 3 | [1,2,3,3,5,6]  |
|  4   |  1 |  0 |   2   | nums1[1]=2 == nums2[0]=2| write 2 | [1,2,2,3,5,6]  |
|  5   |  1 | -1 |   1   | p2 < 0                  | STOP    | [1,2,2,3,5,6]  |

nums1[0..1] = [1,2] is already in place -> no flush step
```

**Traps**
- Merging left → right overwrites unread values: `nums1 = [4,5,6,0,0,0]`, `nums2 = [1,2,3]` writes `1`
  over the `4` before reading it.
- `while p1 >= 0 and p2 >= 0` with no flush: `nums1 = [0]`, m = 0, `nums2 = [1]` never copies the `1`.
  Loop on `p2` alone, or add `nums1[:p2 + 1] = nums2[:p2 + 1]` afterwards.
- Reading `nums1[p1]` without `p1 >= 0`: in Python `nums1[-1]` silently reads the last slot instead of failing.
- `nums1 = sorted(nums1 + nums2)` rebinds the local name; the caller's array is unchanged.
- Starting `write` at `m - 1`: `nums1` has m + n slots; the first write must be `m + n - 1`.

**Not this if** — two **linked lists**: there is no empty tail to fill, so merge forward with a dummy
head (LC 21); k sorted inputs → a heap (LC 23).

**Drill** — LC 88 ⭐ · LC 977 *(largest square is at one of the two ends: fill a result backward)* ·
LC 350 *(two sorted pointers, keep the common values)* · LC 986 *(advance the interval that ends first)* ·
LC 4 *(the merge only conceptually — binary search the partition instead)*

### Template 6: Three-Way Partition, Dutch National Flag — LC 75 ⭐⭐⭐⭐

**Recognise** — "sort an array of 0s, 1s and 2s **in one pass**, constant space", "partition around
a pivot into less / equal / greater". O(1) space, a single pass.

**Key point** — four regions are kept true at every step: `nums[0..low-1]` = 0,
`nums[low..mid-1]` = 1, `nums[mid..high]` = unseen, `nums[high+1..]` = 2. Each step shrinks the
unseen region by one, so the loop ends when `mid > high`.

```python
# python
# LC 75 - Sort Colors
# IDEA: 0 -> swap into the low region, 2 -> swap into the high region, 1 -> leave in place
# time = O(n), space = O(1)
def sortColors(nums):
    low, mid, high = 0, 0, len(nums) - 1
    while mid <= high:                  # nums[mid..high] is still unseen
        if nums[mid] == 0:
            nums[low], nums[mid] = nums[mid], nums[low]
            low += 1
            mid += 1                    # what came from low is a 1 (or mid itself): already seen
        elif nums[mid] == 1:
            mid += 1
        else:
            nums[mid], nums[high] = nums[high], nums[mid]
            high -= 1                   # do NOT move mid: the value from high is unseen
```

```java
// java
// LC 75 - Sort Colors
// IDEA: 0 -> swap into the low region, 2 -> swap into the high region, 1 -> leave in place
// time = O(n), space = O(1)
public void sortColors(int[] nums) {
    int low = 0;                  // next slot for a 0
    int mid = 0;                  // current element
    int high = nums.length - 1;   // next slot for a 2
    while (mid <= high) {
        if (nums[mid] == 0) {
            swap(nums, low, mid);
            low++;
            mid++;
        } else if (nums[mid] == 1) {
            mid++;
        } else {
            swap(nums, mid, high);
            high--;               // the swapped-in value is unseen: check it next round
        }
    }
}

private void swap(int[] nums, int i, int j) {
    int temp = nums[i];
    nums[i] = nums[j];
    nums[j] = temp;
}
```

**Complexity**
- **Time O(n)**: every pass of `while mid <= high` either moves `mid` right or `high` left, so the
  unseen region shrinks by one each time.
- **Space O(1)**: three indices; the swaps are in place.

**Dry run**

```text
nums = [2,0,2,1,1,0]          low mid high
mid=0 is 2 -> swap with high   [0,0,2,1,1,2]   0  0  4
mid=0 is 0 -> swap with low    [0,0,2,1,1,2]   1  1  4
mid=1 is 0 -> swap with low    [0,0,2,1,1,2]   2  2  4
mid=2 is 2 -> swap with high   [0,0,1,1,2,2]   2  2  3
mid=2 is 1                                     2  3  3
mid=3 is 1                                     2  4  3   mid > high: done
```

**Traps**
- Advancing `mid` after the swap with `high`: `[1,2,0]` ends as `[1,0,2]` — the `0` that arrived from
  the right is never examined.
- `while mid < high`: `[1,0]` stops with `mid == high` unexamined and stays `[1,0]`.

**Not this if** — the relative order must be kept (a **stable** partition): a stable list partition (LC 86)
is [linked_list.md's two dummies](./linked_list.md#two-dummies--split-into-chains-then-join-lc-86-), and an
array keeps order with Template 1's fast/slow (LC 283).

**Drill** — LC 75 ⭐ · LC 905 *(two groups: one swap pointer is enough)* · LC 2161 *(stable — needs a
second pass or an extra array)* · LC 215 *(the same 3-way partition inside quickselect, see
[2_pointers_quickselect.md](./2_pointers_quickselect.md))*

### Template 7: Fast/Slow Cycle Detection — LC 141 ⭐⭐⭐⭐

**Recognise** — "does the linked list have a cycle", "return the node where the cycle begins",
"find the duplicate without modifying the array, O(1) extra space". O(1) memory rules out a visited set.

**Key point** — `fast` moves two steps, `slow` one. Once both are inside a cycle of length c, the gap
between them shrinks by one per step, so they meet within c steps; with no cycle `fast` reaches `None`.
For the entry (LC 142): head-to-entry distance ≡ meet-to-entry distance (mod c), so a pointer from
`head` and one from the meeting point, both moving one step, meet exactly at the entry.

```python
# python
# LC 141 - Linked List Cycle; LC 142 - Linked List Cycle II (the entry node)
# IDEA: slow 1 step, fast 2 steps -> they meet iff there is a cycle; then head and meet point
#       walk 1 step each and meet at the entry
# time = O(n), space = O(1)
def hasCycle(head):
    slow = fast = head
    while fast is not None and fast.next is not None:
        slow = slow.next
        fast = fast.next.next
        if slow is fast:          # identity, not value
            return True
    return False

def detectCycle(head):
    slow = fast = head
    while fast is not None and fast.next is not None:
        slow = slow.next
        fast = fast.next.next
        if slow is fast:          # inside the cycle: now find where it starts
            entry = head
            while entry is not slow:
                entry = entry.next
                slow = slow.next
            return entry
    return None                   # fast fell off the end: no cycle
```

```java
// java
// LC 141 - Linked List Cycle; LC 142 - Linked List Cycle II (the entry node)
// IDEA: slow 1 step, fast 2 steps -> they meet iff there is a cycle; then head and meet point
//       walk 1 step each and meet at the entry
// time = O(n), space = O(1)
public boolean hasCycle(ListNode head) {
    ListNode slow = head;
    ListNode fast = head;
    while (fast != null && fast.next != null) {
        slow = slow.next;
        fast = fast.next.next;
        if (slow == fast) {
            return true;
        }
    }
    return false;
}

public ListNode detectCycle(ListNode head) {
    ListNode slow = head;
    ListNode fast = head;
    while (fast != null && fast.next != null) {
        slow = slow.next;
        fast = fast.next.next;
        if (slow == fast) {
            ListNode entry = head;
            while (entry != slow) {
                entry = entry.next;
                slow = slow.next;
            }
            return entry;
        }
    }
    return null;                  // no cycle: the guard above is what prevents an NPE
}
```

**Complexity**
- **Time O(n)**: before the meet, `slow` takes at most n steps (tail length + one lap); the entry
  walk is at most another n.
- **Space O(1)**: two or three node references.

**Traps**
- Checking only `fast.next`: an acyclic `[1,2]` makes `fast` `None` after one jump, and `fast.next`
  throws. Test `fast` first, then `fast.next`.
- LC 142 on an acyclic list must return `None`; running the entry walk unconditionally (no meet
  happened) dereferences `null`.
- Starting `fast = head.next` changes the distances: on `[3,2,0,-4]` (tail to index 1) the head/meet
  walk never reaches the entry and loops forever.
- Comparing `slow.val == fast.val` reports a cycle on the acyclic `[1,1,1]`. Compare the nodes.

**Not this if** — O(n) memory is allowed and clarity matters → a visited set; the node-based family
(middle node, nth from end, reorder, palindrome list) is [2_pointers_linkedlist.md](./2_pointers_linkedlist.md).

**Drill** — LC 141 ⭐ · LC 876 *(stop when `fast` ends: `slow` is the middle)* · LC 202 *(an implicit
list: n → sum of squared digits)* · LC 142 *(the entry walk)* · LC 287 *(the array as a list
`i → nums[i]`; the duplicate is the entry)*

### Template 8: Grow Right, Then Shrink Left — LC 567

**Recognise** — "does `s2` contain a permutation of `s1`", "a window that grows on one condition and
shrinks on another". n ≤ 10⁴. This is the sliding-window skeleton, kept here as the two-pointer view
of it; [sliding_window.md](./sliding_window.md) owns the family.

**Key point** — `right` only grows and `left` only grows, so each index enters and leaves the window
once. After the `while` the window is never longer than `s1`, so comparing counts there tests every
length-`len(s1)` window exactly once.

```python
# python
# LC 567 - Permutation in String
# IDEA: grow right; shrink left until the window is len(s1) long; equal counts = a permutation
# time = O(26 * n), space = O(26)
def checkInclusion(s1, s2):
    need = [0] * 26
    for ch in s1:
        need[ord(ch) - ord('a')] += 1
    window = [0] * 26
    left = 0
    for right in range(len(s2)):
        window[ord(s2[right]) - ord('a')] += 1          # grow
        while right - left + 1 > len(s1):               # too long -> shrink
            window[ord(s2[left]) - ord('a')] -= 1
            left += 1
        if window == need:
            return True
    return False
```

```java
// java
// LC 567 - Permutation in String
// IDEA: grow right; shrink left until the window is len(s1) long; equal counts = a permutation
// time = O(26 * n), space = O(26)
public boolean checkInclusion(String s1, String s2) {
    int[] need = new int[26];
    for (int i = 0; i < s1.length(); i++) {
        need[s1.charAt(i) - 'a']++;
    }
    int[] window = new int[26];
    int left = 0;
    for (int right = 0; right < s2.length(); right++) {
        window[s2.charAt(right) - 'a']++;               // grow
        while (right - left + 1 > s1.length()) {        // too long -> shrink
            window[s2.charAt(left) - 'a']--;
            left++;
        }
        if (Arrays.equals(window, need)) {
            return true;
        }
    }
    return false;
}
```

**Complexity**
- **Time O(26·n)**: `right` and `left` each cross `s2` once (the inner `while` is amortised O(1));
  the 26-slot comparison runs once per `right`.
- **Space O(26)**: the two count arrays.

**Traps**
- `len(s1) > len(s2)` (`"abc"`, `"ab"`): the loop never reaches a full window and correctly returns
  false — do not slice `s2[:len(s1)]` up front and assume it exists.
- Testing `window == need` before the shrink compares a window one character too long.

**Not this if** — the window size is not fixed and the condition is a sum or a count → the
variable-window cards in [sliding_window.md](./sliding_window.md); negative values in a sum → prefix sum.

**Drill** — LC 567 ⭐ · LC 438 *(collect every start instead of returning)* · LC 209 *(shrink while the
sum is still ≥ target)* · LC 76 *(have/need counter — sliding_window's card)*

## Drill Path

| Stage | Problems (easy → hard) | What it adds |
|---|---|---|
| **Core** ⭐ | LC 125 · LC 141 · LC 11 · LC 5 · LC 647 | converging with skips, Floyd, the shorter-wall argument, both centre kinds, counting by expansion |
| **Practice** | LC 26 · LC 88 · LC 392 · LC 167 · LC 75 · LC 142 · LC 15 | compaction, backward merge, greedy subsequence, sorted pair, three regions, the cycle entry, fix-one + converge ([n_sum.md](./n_sum.md)) |
| **Stretch** | LC 80 · LC 881 · LC 287 · LC 42 | at most k copies, greedy pairing after a sort, Floyd on an array, two running maxima |

## Summary & Quick Reference

### Pattern Selection Table

| Pattern | When to Use | Example Problems |
|---------|-------------|------------------|
| **Opposite Direction** | Sorted array, palindrome check | LC 167, LC 344, LC 125 |
| **Same Direction (Fast-Slow)** | Remove duplicates, cycle detection | LC 26, LC 27, LC 142 |
| **Sliding Window** | Subarray/substring problems | LC 3, LC 76, LC 209 |
| **Merge Two Lists** | Merge sorted arrays/lists | LC 88, LC 21 |
| **Partition** | Rearrange elements | LC 75, LC 86 |
| **Palindrome with Deletion** | Allow k changes | LC 680, LC 1216 |
| **Fixed + Two Pointers (exact)** | Sum == target; collect all | LC 15, LC 18 |
| **Fixed + Two Pointers (closest)** | Sum nearest to target | LC 16, LC 259 |
| **Subsequence Matching** | Check if one string is subsequence of another | LC 392, LC 524, LC 792 |
| **Pattern Match with Constraints** | Subsequence + character type validation | LC 1023 |
| **Longest Palindromic Prefix** | Find longest palindromic prefix, prepend reversed suffix | LC 214, LC 336 |
| **Length-Prefixed (Encode/Decode)** | Parse `len#word` blocks; `i` jumps by declared length | LC 271, LC 297 |
| **Converging Low/High (build permutation)** | Greedy: consume smallest/largest available per signal | LC 942 |
| **Last-Occurrence + Expanding End** | Greedy partition; cut when `i == end` | LC 763 |
| **Staircase (sorted 2D matrix)** | Search grid sorted by row AND column | LC 240 |
| **Two Strings in Lockstep** | Compare/parse two sequences chunk by chunk | LC 165, LC 953, LC 14 |
| **3-Phase Interval Scan** | Insert/merge one interval into a sorted list | LC 57, LC 56 |

### Classic Problems by Difficulty

#### Easy

- LC 26 Remove Duplicates from Sorted Array
- LC 27 Remove Element
- LC 125 Valid Palindrome
- LC 283 Move Zeroes
- LC 344 Reverse String
- LC 345 Reverse Vowels of a String
- LC 349 Intersection of Two Arrays
- LC 350 Intersection of Two Arrays II
- LC 392 Is Subsequence
- LC 680 Valid Palindrome II
- LC 844 Backspace String Compare
- LC 942 DI String Match
- LC 953 Verifying an Alien Dictionary
- LC 977 Squares of a Sorted Array
- LC 14 Longest Common Prefix (lockstep char scan across all strings)

#### Medium

- LC 3 Longest Substring Without Repeating Characters (Sliding Window)
- LC 5 Longest Palindromic Substring
- LC 11 Container With Most Water
- LC 15 3Sum
- LC 16 3Sum Closest
- LC 18 4Sum
- LC 75 Sort Colors (Dutch National Flag)
- LC 80 Remove Duplicates from Sorted Array II
- LC 86 Partition List
- LC 88 Merge Sorted Array
- LC 142 Linked List Cycle II
- LC 165 Compare Version Numbers
- LC 167 Two Sum II - Input Array Is Sorted
- LC 240 Search a 2D Matrix II (staircase two pointers)
- LC 57 Insert Interval (3-phase pointer scan)
- LC 763 Partition Labels
- LC 209 Minimum Size Subarray Sum (Sliding Window)
- LC 287 Find the Duplicate Number
- LC 567 Permutation in String (Sliding Window)
- LC 647 Palindromic Substrings
- LC 713 Subarray Product Less Than K
- LC 881 Boats to Save People
- LC 986 Interval List Intersections
- LC 1023 Camelcase Matching

#### Hard

- LC 42 Trapping Rain Water
- LC 76 Minimum Window Substring (Sliding Window)
- LC 214 Shortest Palindrome
- LC 828 Count Unique Characters of All Substrings

### Interview Tips

| Signal | Pattern |
|--------|---------|
| "sort + find pair" | Left-right pointers after sorting |
| "in-place remove/deduplicate" | Slow-fast write pointer |
| "cycle in linked list" | Tortoise and hare |
| "partition into 3 groups" | Dutch national flag |
| "merge sorted in-place" | Fill from back |
| "palindrome check" | Pointers from both ends |
| "maximum area / container" | Shrink shorter side |

### Related Sheets

- [binary_search.md](./binary_search.md) — left/right pointers halving a range instead of scanning it
- [sliding_window.md](./sliding_window.md) — the condition-driven window
- [n_sum.md](./n_sum.md) — fixed element + converging pointers (LC 15, LC 16, LC 18)
- [2_pointers_examples.md](./2_pointers_examples.md) — worked LC catalogue
- [2_pointers_quickselect.md](./2_pointers_quickselect.md) — Kth element by partition
