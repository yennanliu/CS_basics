# Array — Worked Examples

> **Scope** — The problems behind [array.md](./array.md)'s own templates (index as hash, the one-pass running state), each filed under the card it reduces to; an array-tagged problem whose home is another sheet is a one-row pointer here.
> **See also**: [array.md](./array.md) — the parent sheet: the basic operations, the templates these problems reduce to, and the chooser table that says which sheet a problem actually belongs to; [2_pointers.md](./2_pointers.md), [sliding_window.md](./sliding_window.md), [prefix_sum.md](./prefix_sum.md), [difference_array.md](./difference_array.md) — the four pattern families that own most array-tagged problems; [sort.md](./sort.md), [matrix.md](./matrix.md), [kadane_algorithm.md](./kadane_algorithm.md), [stock_trading.md](./stock_trading.md) — the other owners the chooser routes to.

## LeetCode Problem Lists

- [Array](https://leetcode.com/problem-list/array/)

## Overview

[array.md](./array.md) holds the templates; this sheet is the problems that apply them. Each
card names the template it reduces to and the one step it adds. Most problems tagged *array* are
really window, pointer, prefix-sum, sorting or DP problems: they are listed under
[Lives Elsewhere](#lives-elsewhere) with a link to the sheet that works them, so each one is
solved in exactly one place.

## Index

| Pattern | Problems (easy → hard) | Core ⭐ |
|---|---|---|
| [Template 9: Cyclic Sort, Index as Hash](./array.md#1-2-8-index-as-hash-cyclic-sort-and-sign-marking-) | LC 448 · LC 442 | LC 41 (the anchor, in array.md) |
| [Template 11: Running State Scan](./array.md#1-2-10-one-pass-running-state-) | LC 1375 · LC 1041 · LC 670 | LC 849 (the anchor, in array.md) |

### Lives Elsewhere

| LC | Problem | Home | Why it lives there |
|---|---|---|---|
| 41 | First Missing Positive | [array § Template 9](./array.md#1-2-8-index-as-hash-cyclic-sort-and-sign-marking-) | the anchor of the cyclic-sort card |
| 88 | Merge Sorted Array | [2_pointers § Template 5](./2_pointers.md#template-5-merge-two-sorted-arrays-fill-from-the-back--lc-88-) | the backward write is two read pointers and one write pointer |
| 121 | Best Time to Buy and Sell Stock | [stock_trading.md](./stock_trading.md#lc-121-best-time-to-buy-and-sell-stock-single-transaction) | the minimum price so far; the stock family shares one state machine |
| 122 | Best Time to Buy and Sell Stock II | [greedy § Template 3](./greedy.md#template-3-greedy-accumulation--lc-122-) | the anchor of greedy accumulation: sum every rise |
| 189 | Rotate Array | [array § Template 10](./array.md#1-2-9-rotate-in-place-by-three-reversals-) | the anchor of the three-reversals card |
| 238 | Product of Array Except Self | [prefix_sum.md](./prefix_sum.md#product-variant--no-inverse-so-build-both-sides-lc-238-) | a prefix product times a suffix product; the output array doubles as the prefix array |
| 251 | Flatten 2D Vector | [array § Template 1](./array.md#1-1-6-flatten-array) | the anchor of the lazy two-cursor iterator |
| 287 | Find the Duplicate Number | [binary_search_on_answer.md](./binary_search_on_answer.md#find-the-duplicate-number--lc-287-) | it forbids modifying `nums`, so sign marking is not allowed: binary search the value range, or Floyd's cycle detection |
| 334 | Increasing Triplet Subsequence | [patience_sorting.md](./patience_sorting.md#2-2-increasing-triplet-subsequence--lc-334-) | `first` / `second` are the patience-sorting tails capped at two piles |
| 406 | Queue Reconstruction by Height | [sort.md](./sort.md#variation--sort-by-derived-key-then-insert-greedily--lc-406) | sort tallest first, then insert at index `k` |
| 849 | Maximize Distance to Closest Person | [array § Template 11](./array.md#1-2-10-one-pass-running-state-) | the anchor of the running-state card |
| 1109 | Corporate Flight Bookings | [difference_array.md](./difference_array.md#2-2-corporate-flight-bookings--lc-1109) | range updates on endpoints, then one prefix sum |
| 1567 | Maximum Length of Subarray With Positive Product | [kadane_algorithm.md](./kadane_algorithm.md#6-8-maximum-length-of-subarray-with-positive-product-lc-1567--track-posneg-product-lengths) | the longest positive and negative run ending here, Kadane style |

## Index as Hash Examples

### Find All Numbers Disappeared in an Array — LC 448

> **Key point** — mark "value `v` was seen" by making `nums[v - 1]` negative. After one pass, a
> slot that is still positive names a value that never appeared. Every read goes through `abs()`,
> because the slot it reads may already have been marked.

**Reduces to**: [array § Template 9](./array.md#1-2-8-index-as-hash-cyclic-sort-and-sign-marking-) — it marks by sign instead of moving values, and collects every gap instead of the first.

```python
# python
# LC 448 - Find All Numbers Disappeared in an Array
# IDEA: sign marking - negate nums[v - 1] for every v; the indices left positive are missing
# time = O(n), space = O(1) besides the output
def findDisappearedNumbers(nums):
    for x in nums:
        i = abs(x) - 1          # x may already be negative: it was marked by an earlier value
        if nums[i] > 0:         # mark once; a second negation would unmark a duplicate
            nums[i] = -nums[i]
    missing = []
    for i in range(len(nums)):
        if nums[i] > 0:
            missing.append(i + 1)
    return missing
```

**Traps**
- **Negating without the `> 0` guard.** `[1, 1]` flips slot 0 twice, back to positive, and
  returns `[1, 2]` instead of `[2]`.
- **Reading `x` raw.** After marking, `x` can be negative; `nums[x - 1]` then indexes from the end
  in Python, silently, and throws in Java.
- **The caller's array is changed.** Restore it with `abs` if it is reused.

**Similar** — LC 268 (one missing value: a sum or XOR is simpler) · LC 645 (one duplicate and one
missing, from the same marks)

### Find All Duplicates in an Array — LC 442

> **Key point** — the second time a value `v` is read, slot `v - 1` is already negative: that read
> is the duplicate. Each value appears at most twice, so one test per read reports every duplicate
> exactly once.

**Reduces to**: [array § Template 9](./array.md#1-2-8-index-as-hash-cyclic-sort-and-sign-marking-) — sign marking, where a slot found already negative is the answer rather than a gap.

```python
# python
# LC 442 - Find All Duplicates in an Array
# IDEA: sign marking - the first visit negates nums[v - 1], a second visit finds it negative
# time = O(n), space = O(1) besides the output
def findDuplicates(nums):
    duplicates = []
    for x in nums:
        i = abs(x) - 1          # always through abs(): x itself may have been marked
        if nums[i] < 0:
            duplicates.append(i + 1)   # second visit to value i + 1
        else:
            nums[i] = -nums[i]         # first visit
    return duplicates
```

**Traps**
- **A value seen three times** is reported twice by this test, but LC 442 guarantees at most two;
  if it did not, keep a separate "already reported" mark.
- **LC 287 looks the same and is not.** It forbids modifying `nums`, so this solution is wrong
  there; see [Lives Elsewhere](#lives-elsewhere).

**Similar** — LC 287 (one duplicate, read-only input) · LC 645 (set mismatch)

## One-Pass Running State Examples

### Number of Times Binary String Is Prefix-Aligned — LC 1375

> **Key point** — after step `i`, bits `1..i+1` are all on exactly when the largest index flipped
> so far is `i + 1`. `i + 1` distinct indices have been flipped (the input is a permutation), and
> none of them is above `i + 1`, so they are exactly `1..i+1`.

**Reduces to**: [array § Template 11](./array.md#1-2-10-one-pass-running-state-) — the carried value is the maximum so far. Because the input is a permutation, it replaces the frequency array of [Template 4](./array.md#1-2-2-frequency-array--running-count).

```python
# python
# LC 1375 - Number of Times Binary String Is Prefix-Aligned (formerly Bulb Switcher III)
# IDEA: carry the largest index flipped so far; the prefix is aligned when it equals the step count
# time = O(n), space = O(1)
def numTimesAllBlue(flips):
    largest = 0
    aligned = 0
    for step, index in enumerate(flips, start=1):
        largest = max(largest, index)
        if largest == step:        # step indices flipped, none above step -> exactly 1..step
            aligned += 1
    return aligned
```

**Traps**
- **Comparing with the 0-based position.** `largest == i` with `i` from `enumerate(flips)` never
  matches on `[3, 2, 4, 1, 5]` and returns 0 instead of 2; the indices are 1-based.
- **Checking the flipped bits each step** is O(n²), which times out at n = 5·10⁴.

### Robot Bounded In Circle — LC 1041

> **Key point** — run the instructions once. A robot back at the origin is bounded. A robot facing
> any direction but north is bounded too: after four runs (two, if it faces south) its moves
> cancel. Only a robot that ends away from the origin *and* still faces north drifts away.

**Reduces to**: [array § Template 11](./array.md#1-2-10-one-pass-running-state-) — the carried state is `(x, y, heading)`, and the answer is read off the state after one pass.

```python
# python
# LC 1041 - Robot Bounded In Circle
# IDEA: simulate one pass; bounded iff back at the origin or no longer facing north
# time = O(n), space = O(1)
def isRobotBounded(instructions):
    moves = [(0, 1), (1, 0), (0, -1), (-1, 0)]   # north, east, south, west: clockwise
    x = 0
    y = 0
    heading = 0                                   # index into moves; 0 = north
    for c in instructions:
        if c == "L":
            heading = (heading + 3) % 4           # one step counter-clockwise
        elif c == "R":
            heading = (heading + 1) % 4           # one step clockwise
        else:                                     # "G"
            x += moves[heading][0]
            y += moves[heading][1]
    return (x == 0 and y == 0) or heading != 0
```

**Traps**
- **Testing the origin only.** `"GL"` ends at `(0, 1)` facing west, not at the origin, but it
  walks a square and is bounded; the heading test catches it.
- **`(heading - 1) % 4` in Java** is `-1` when `heading` is 0, an out-of-range index. Add 3
  instead of subtracting 1, as above.

### Maximum Swap — LC 670

> **Key point** — scan the digits right to left, carrying the index of the largest digit seen so
> far (the rightmost one on ties). Any digit smaller than that maximum could swap with it; the
> best swap is the leftmost such digit, and since the scan moves left, the last one recorded is it.

**Reduces to**: [array § Template 11](./array.md#1-2-10-one-pass-running-state-) — scanned from the right, carrying the index of the maximum to the right of each digit.

```python
# python
# LC 670 - Maximum Swap
# IDEA: right-to-left scan carrying the index of the largest digit to the right;
#       the leftmost digit smaller than that maximum is swapped with it
# time = O(d), space = O(d)   d = number of digits (at most 9)
def maximumSwap(num):
    digits = list(str(num))
    max_idx = len(digits) - 1          # index of the largest digit in digits[i:]
    left = -1                          # the best swap found so far
    right = -1
    for i in range(len(digits) - 1, -1, -1):
        if digits[i] > digits[max_idx]:
            max_idx = i                # strictly larger: ties keep the rightmost copy
        elif digits[i] < digits[max_idx]:
            left = i                   # further left than any earlier find, so better
            right = max_idx
    if left == -1:
        return num                     # already the largest arrangement
    digits[left], digits[right] = digits[right], digits[left]
    return int("".join(digits))
```

**Traps**
- **`>=` when updating the maximum.** `1993` must swap with the *rightmost* 9 (9913); `>=` moves
  `max_idx` to the leftmost 9 and returns 9193.
- **Stopping at the first candidate found.** On `98368` the first find from the right is the 6,
  giving 98386; the leftmost smaller digit is the 3, giving 98863.
- **Brute force over every pair** is O(d²) — fine at 9 digits, but it is not the answer an
  interviewer is looking for.

**Second approach** — a `{digit: last index}` map and a left-to-right scan, in
[hash_map_examples.md](./hash_map_examples.md#2-17-maximum-swap-lc-670). Same O(d); it asks "is
there a larger digit later?" instead of carrying the maximum.
