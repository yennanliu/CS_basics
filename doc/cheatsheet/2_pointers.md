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

### Pattern → Template → Problems

| Signal in the problem | Template | Worked in |
|---|---|---|
| in-place remove / deduplicate, order matters | [Template 1](#template-1-fastslow-read-write-compaction--lc-26-lc-27-) | LC 26, LC 27, LC 80, LC 283 |
| sorted array, look for a pair / squeeze a range | [Template 2](#template-2-converging-bidirectional-pointers-) | LC 11, LC 125, LC 167, LC 344 |
| count / find palindromic substrings | [Template 3](#template-3-expand-from-centre--lc-5-lc-647-) | LC 5, LC 647, LC 680 |
| "is A a subsequence of B", pattern matching | [Template 4](#template-4-subsequence-matching-one-pointer-always-moves--lc-392-) | LC 392, LC 524, LC 1023, LC 809 |
| merge two sorted sequences in place | [Template 5](#template-5-merge-two-sorted-arrays-fill-from-the-back--lc-88-) | LC 88, LC 986, LC 977 |
| rearrange into 3 groups / partition by value | [Template 6](#template-6-three-way-partition-dutch-national-flag--lc-75-) | LC 75, LC 905, LC 86 |
| cycle in a linked list, find the duplicate | [Template 7](#template-7-fastslow-cycle-detection--lc-141-lc-142-) | LC 141, LC 142, LC 287 |
| window that grows on one condition, shrinks on another | [Template 8](#template-8-advance-right-then-advance-left-condition-driven) | LC 209, LC 567 (see [sliding_window.md](./sliding_window.md)) |
| kth largest / k closest (select, do not sort) | [2_pointers_quickselect.md](./2_pointers_quickselect.md) | LC 215, LC 973 |

Everything else — the long tail of worked problems — lives in [2_pointers_examples.md](./2_pointers_examples.md).

## Templates & Algorithms

### Template 1: Fast/Slow Read-Write Compaction — LC 26, LC 27 ⭐⭐⭐⭐⭐

#### Core Idea

**Slow-Fast (Write-Read) Pattern:**
- `slow` = the "write" pointer — tracks the last confirmed unique position
- `fast` = the "read" pointer — scans the array looking for new unique values
- When `nums[fast] != nums[slow]`: a new unique value is found
  1. Advance `slow` first (open the next write slot)
  2. Write (or swap) `nums[fast]` into `nums[slow]`
- Return `slow + 1` as the count of unique elements

**Key invariant:** `nums[0..slow]` always contains unique elements in sorted order.

**Two variants:**
- **Overwrite** (`nums[slow] = nums[fast]`): cleaner, preferred — array is already sorted so we just need to copy unique values forward
- **Swap** (`swap(nums[slow], nums[fast])`): also correct but unnecessary for sorted arrays; used when original values need to be preserved elsewhere

```text
Pointer movement rules:
  - fast: moves EVERY iteration (scans all elements)
  - slow: moves ONLY when a new unique value is found (after nums[fast] != nums[slow])
  - Both start at 0 (or slow=0, fast=1 in while-loop variants)
```

---

```java
// java
// LC 26 (LC 83)
// https://labuladong.online/algo/essential-technique/array-two-pointers-summary/#%E5%8E%9F%E5%9C%B0%E4%BF%AE%E6%94%B9
/**
 *  //--------------------------------
 *  Example 1
 *  //--------------------------------
 *
 *  nums = [1,1,2]
 *
 *  [1,1,2]
 *   s f
 *
 *  [1,2, 1]     if nums[f] != nums[s], move s, then swap f, s
 *   s s  f
 *
 *
 *   //--------------------------------
 *   Example 2
 *   //--------------------------------
 *
 *   nums = [0,0,1,1,1,2,2,3,3,4]
 *
 *   [0,0,1,1,1,2,2,3,3,4]
 *    s f
 *
 *   [0,1,0,1,1,2,2,3,3,4]   if nums[f] != nums[s], move s, then swap f, s
 *    s s f
 *
 *   [0,1,0,1,1,2,2,3,3,4]
 *      s   f
 *
 *   [0,1,0,1,1,2,2,3,3,4]
 *      s     f
 *
 *   [0,1,2,1,1,0,2,3,3,4]   if nums[f] != nums[s], move s, then swap f, s
 *      s s     f
 *
 *   [0,1,2,1,1,0,2,3,3,4]
 *        s       f
 *
 *   [0,1,2,3,1,0,2,1,3,4]  if nums[f] != nums[s], move s, then swap f, s
 *        s s       f
 *
 *   [0,1,2,3,1,0,2,1,3,4]
 *          s         f
 *
 *   [0,1,2,3,4,0,2,1,3,1]   if nums[f] != nums[s], move s, then swap f, s
 *          s s         f
 *
 */
// Variant A: OVERWRITE (cleaner, preferred for sorted arrays)
class Solution {
    public int removeDuplicates(int[] nums) {
        if (nums.length == 0) return 0;

        int slow = 0;
        for (int fast = 1; fast < nums.length; fast++) {
            if (nums[fast] != nums[slow]) {
                slow++;                  // open next write slot
                nums[slow] = nums[fast]; // overwrite with new unique value
            }
            // if equal: fast keeps moving, slow stays
        }
        return slow + 1;
    }
}

// Variant B: SWAP (preserves all values, same time/space)
class Solution {
    public int removeDuplicates(int[] nums) {
        if (nums.length == 0) return 0;

        int slow = 0, fast = 0;
        while (fast < nums.length) {
            if (nums[fast] != nums[slow]) {
                slow++;
                // swap: move the new unique value to slow position
                int tmp = nums[slow];
                nums[slow] = nums[fast];
                nums[fast] = tmp;
            }
            fast++;
        }
        return slow + 1;
    }
}
```


#### Allowing at most K copies

Generalise the comparison to `nums[fast] != nums[slow - k]` and start both pointers at `k`: `k = 1` is LC 26, `k = 2` is LC 80. Worked in [2_pointers_examples.md](./2_pointers_examples.md).

#### Remove Element — LC 27
```java
// java
// LC 27
// https://labuladong.online/algo/essential-technique/array-two-pointers-summary/#%E5%8E%9F%E5%9C%B0%E4%BF%AE%E6%94%B9
/**
 *  //--------------------
 *  Example 1
 *  //--------------------
 *
 *  nums = [3,2,2,3], val = 3
 *
 *  [3,2,2,3]
 *   s
 *   f
 *
 *  [2,3,2,3]    if nums[f] != val, swap, move s
 *   s s
 *     f
 *
 *  [2,2,3,3]   if nums[f] != val, swap, move s
 *     s s
 *       f
 *
 * [2,2,3,3]
 *      s
 *        f
 *
 *
 *  //--------------------
 *  Example 2
 *  //--------------------
 *
 *  nums = [0,1,2,2,3,0,4,2], val = 2
 *
 *
 *  [0,1,2,2,3,0,4,2]   if nums[f] != val, swap, move s
 *   s s
 *   f
 *
 *  [0,1,2,2,3,0,4,2]     if nums[f] != val, swap, move s
 *     s s
 *     f
 *
 *  [0,1,2,2,3,0,4,2]
 *       s
 *       f
 *
 * [0,1,2,2,3,0,4,2]
 *      s
 *        f
 *
 * [0,1,3,2,2,0,4,2]   if nums[f] != val, swap, move s
 *      s s
 *          f
 *
 * [0,1,3,0,2,2,4,2]   if nums[f] != val, swap, move s
 *        s s
 *            f
 *
 * [0,1,3,0,4,2,2,2]    if nums[f] != val, swap, move s
 *          s s
 *              f
 *
 *  [0,1,3,0,4,2,2,2]
 *             s
 *                 f
 */
class Solution {
    public int removeElement(int[] nums, int val) {
        int fast = 0, slow = 0;
        while (fast < nums.length) {
            if (nums[fast] != val) {
                nums[slow] = nums[fast];
                slow++;
            }
            fast++;
        }
        return slow;
    }
}
```

```python
# python
# basic
class Solution(object):
    def removeElement(self, nums, val):
        length = 0
        for i in range(len(nums)):
            if nums[i] != val:
                nums[length] = nums[i]
                length += 1
        return length
```

#### Remove Duplicates from Sorted Array (Python) — LC 26

```python
# LC 026 : Remove Duplicates from Sorted Array
# https://github.com/yennanliu/CS_basics/blob/master/leetcode_python/Array/remove-duplicates-from-sorted-array.py
# V0
# IDEA : 2 POINTERS: i, j
class Solution(object):
    def removeDuplicates(self, nums):
        # edge case
        if not nums:
            return
        i = 0
        for j in range(1, len(nums)):
            """
            NOTE !!!
             -> note this condition
             -> we HAVE to swap i+1, j once nums[i], nums[j] are different
             -> so we MAKE SURE there is no duplicate
            """
            if nums[j] != nums[i]:
                nums[i+1], nums[j] = nums[j], nums[i+1]
                i += 1

        #print ("nums = " + str(nums))
        return i+1
```

#### Pattern Summary

| Step | Action | Why |
|------|--------|-----|
| `nums[fast] == nums[slow]` | Only advance `fast` | Duplicate — skip it |
| `nums[fast] != nums[slow]` | `slow++`, then write/swap | New unique found — claim next slot |
| Return | `slow + 1` | `slow` is index, length = index + 1 |

**Why overwrite instead of swap?**
- Array is sorted → we only move values left, never right
- No need to preserve overwritten values (they are duplicates already seen)
- `nums[slow] = nums[fast]` is O(1) and simpler

#### Similar Problems

| Problem | LC# | Difference | Key Trick |
|---------|-----|------------|-----------|
| Remove Duplicates from Sorted Array | 26 | Allow each value once | `nums[slow] = nums[fast]` when different |
| Remove Duplicates from Sorted Array II | 80 | Allow each value **at most twice** | Compare `nums[fast]` with `nums[slow-1]` (two back) |
| Remove Element | 27 | Remove all occurrences of `val` | Write when `nums[fast] != val` |
| Move Zeroes | 283 | Move zeros to end, preserve order | Swap when `nums[fast] != 0` |
| Remove Duplicates from Sorted List | 83 | Linked list version of LC 26 | `node.next = node.next.next` on duplicate |
| Remove Duplicates from Sorted List II | 82 | Delete ALL nodes with duplicate values | Extra sentinel node + skip entire duplicate group |

### Template 2: Converging Bidirectional Pointers ⭐⭐⭐⭐⭐

Two pointers start at the two ends and walk **towards** each other. Each step evaluates the pair `(l, r)` and then discards the side that cannot possibly improve the answer, so no pair is ever examined twice.

```text
l = 0, r = n - 1
while l < r:
    evaluate the pair (l, r)
    move the pointer that cannot improve the answer
      -> the shorter wall (LC 11), the non-alphanumeric char (LC 125),
         the side whose sum is too small / too large (LC 167)
```

#### Container With Most Water — LC 11
Start with widest window, shrink the shorter side to maximize area.

```python
def maxArea(height):
    l, r = 0, len(height) - 1
    ans = 0
    while l < r:
        ans = max(ans, min(height[l], height[r]) * (r - l))
        if height[l] < height[r]:
            l += 1
        else:
            r -= 1
    return ans
```

**Why move the shorter side?** Moving the taller side can only decrease width without increasing the min-height bottleneck — no gain possible.

#### Valid Palindrome — LC 125

```python
# LC 125 — ignore non-alphanumeric
def isPalindrome(s):
    l, r = 0, len(s) - 1
    while l < r:
        while l < r and not s[l].isalnum(): l += 1
        while l < r and not s[r].isalnum(): r -= 1
        if s[l].lower() != s[r].lower(): return False
        l += 1; r -= 1
    return True
```

> One deletion allowed (LC 680) is the same scan with a branch at the first mismatch — see [2_pointers_examples.md](./2_pointers_examples.md).

#### Remove Element, Bidirectional Variant — LC 27

**Pattern: Left-Right pointers, shrink from both ends**

Key difference from the fast-slow (Template 1) approach:
- Fast-slow overwrites sequentially → **preserves relative order**
- Bidirectional replaces `nums[l]` with `nums[r]` → **does NOT preserve order**, but may do fewer writes (good when `val` is rare)

```java
// java
// LC 27 Remove Element - Bidirectional variant
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
 *
 * Time: O(N), Space: O(1)
 */
public int removeElement(int[] nums, int val) {
    int l = 0;
    int r = nums.length - 1;

    while (l <= r) {
        if (nums[l] == val) {
            // Overwrite with rightmost element, shrink right boundary
            // NOTE: do NOT advance l — new nums[l] might also be val
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


### Template 3: Expand from Centre — LC 5, LC 647 ⭐⭐⭐⭐

```python
# LC 005  Longest Palindromic Substring
# LC 647 Palindromic Substrings
# python
# pseudo code
# ...
for i in range(len(s)):

    # NOTE !!!
    # NO NEED to have logic like `if i % 2 == 1`..
    # we can just consider `odd, even len` cases directly

    #--------------------------------------
    # if odd
    # NOTE !!! if `odd`, left = right = i
    #--------------------------------------
    left = right = i
    while left >= 0 and right < len(s) and s[left] == s[right]:
        if right+1-left > len(res):
            res = s[left:right+1]
        left -= 1
        right += 1
    
    #--------------------------------------
    # if even
    # NOTE !!! if `even`, left = i - 1, right = i
    #--------------------------------------
    left = i - 1
    right = i
    while left >= 0 and right < len(s) and s[left] == s[right]:
        if right+1-left > len(res):
            res = s[left:right+1]
        left -= 1
        right += 1
# ...
```


> Full solutions for LC 5 and LC 647 are in [2_pointers_examples.md](./2_pointers_examples.md); the palindrome family in depth is [palindrome.md](./palindrome.md).

### Template 4: Subsequence Matching, One Pointer Always Moves — LC 392 ⭐⭐⭐⭐

```java
// java
// LC 392 Is Subsequence
// https://leetcode.com/problems/is-subsequence/

/**
 * Pattern: Check if string s is a subsequence of string t
 *
 * Key Idea:
 *   - Use two pointers: i for s (target subsequence), j for t (main string)
 *   - ALWAYS move j (scan through entire t)
 *   - ONLY move i when characters match
 *   - If i reaches end of s, we found all characters in order
 *
 * Example:
 *   s = "abc", t = "ahbgdc"
 *
 *   [a h b g d c]    i=0, j=0, s[i]=a, t[j]=a, match! i++, j++
 *    i j
 *
 *   [a h b g d c]    i=1, j=1, s[i]=b, t[j]=h, no match, j++
 *      i j
 *
 *   [a h b g d c]    i=1, j=2, s[i]=b, t[j]=b, match! i++, j++
 *        i j
 *
 *   [a h b g d c]    i=2, j=3, s[i]=c, t[j]=g, no match, j++
 *          i j
 *
 *   [a h b g d c]    i=2, j=4, s[i]=c, t[j]=d, no match, j++
 *            i j
 *
 *   [a h b g d c]    i=2, j=5, s[i]=c, t[j]=c, match! i++, j++
 *              i j
 *
 *   i == s.length() -> return true
 */
public boolean isSubsequence(String s, String t) {
    if (s.isEmpty())
        return true;
    if (t.isEmpty())
        return false;

    int i = 0; // Pointer for s (target subsequence)
    int j = 0; // Pointer for t (main string)

    /** NOTE !!!
     *
     *  the while loop condition:
     *
     *     i < s.length()
     *     &&
     *     j < t.length()
     */
    while (i < s.length() && j < t.length()) {
        // If characters match, move the pointer for s
        if (s.charAt(i) == t.charAt(j)) {
            i++;
        }
        // Always move the pointer for t
        j++;
    }

    // If i reached the end of s, all characters were found in order
    return i == s.length();
}
```

**Classic Problems:**
- LC 392 Is Subsequence
- LC 524 Longest Word in Dictionary through Deleting
- LC 792 Number of Matching Subsequences


### Template 5: Merge Two Sorted Arrays, Fill from the Back — LC 88 ⭐⭐⭐⭐⭐

#### Core Idea

**Merge BACKWARD (right → left), not forward.**

- `nums1` has exactly `m + n` slots: `m` valid elements + `n` empty tail slots.
- Merging **forward** (smallest first) would **overwrite** un-read elements of `nums1` → needs an extra buffer (`O(m+n)` space).
- Merging **backward** (largest first) writes into the **empty tail**, which is always at or ahead of the read pointer → **truly in-place, `O(1)` space**.

```text
Key invariant (why backward is always safe):

  write pointer  p  = m + n - 1
  read pointers  p1 = m - 1  (nums1),  p2 = n - 1  (nums2)

  p >= p1  ALWAYS holds, because p - p1 = n - 1 - p2 >= 0
  -> the slot we write to is never a slot we still need to read

  nums1 = [1, 2, 3, 0, 0, 0]
           ^        ^     ^
           |        |     p (write, from the END)
           |        first empty slot
           p1 (read nums1, from the END of valid part)
```

**Loop condition trick — `while p2 >= 0` (not `p1 >= 0 and p2 >= 0`):**

- If `nums2` is exhausted first → the rest of `nums1` is **already in place**, nothing to do. ✅
- If `nums1` is exhausted first (`p1 < 0`) → the remaining `nums2` elements **must** still be copied.
- So looping on `p2` alone handles both tails automatically — **no leftover-copy step needed**.
- The alternative (`while p1 >= 0 and p2 >= 0`) requires a trailing `nums1[:p2+1] = nums2[:p2+1]` to flush the rest of `nums2`.

#### Visual Trace

```text
nums1 = [1, 2, 3, 0, 0, 0], m = 3
nums2 = [2, 5, 6],          n = 3

| Step | p1 | p2 | p | Compare                | Action  | nums1              |
|------|----|----|---|------------------------|---------|--------------------|
| init |  2 |  2 | 5 | —                      | setup   | [1,2,3,0,0,0]      |
|  1   |  2 |  2 | 5 | nums1[2]=3 < nums2[2]=6| write 6 | [1,2,3,0,0,6]      |
|  2   |  2 |  1 | 4 | nums1[2]=3 < nums2[1]=5| write 5 | [1,2,3,0,5,6]      |
|  3   |  2 |  0 | 3 | nums1[2]=3 > nums2[0]=2| write 3 | [1,2,3,3,5,6]      |
|  4   |  1 |  0 | 2 | nums1[1]=2 == nums2[0]=2| write 2 | [1,2,2,3,5,6]     |
|  5   |  1 | -1 | 1 | p2 < 0                 | STOP    | [1,2,2,3,5,6]      |

Step 5: nums1[0..1] = [1,2] is already correct -> no extra work needed
```

#### Pattern (Python)

```python
# python
# LC 88 - Merge Sorted Array
# IDEA : 2 POINTERS, MERGE FROM RIGHT -> LEFT (in-place)
# time = O(m + n), space = O(1)
class Solution(object):
    def merge(self, nums1, m, nums2, n):
        # read pointers: END of the valid parts
        p1 = m - 1
        p2 = n - 1

        # write pointer: END of the whole nums1 array
        p = m + n - 1

        """
        NOTE !!!
         1) loop on `p2 >= 0` only
            -> if nums2 runs out, remaining nums1 is ALREADY in place
         2) all pointer conditions are `>= 0` (not `> 0`)
        """
        while p2 >= 0:
            # NOTE !!! must check `p1 >= 0` before reading nums1[p1]
            if p1 >= 0 and nums1[p1] > nums2[p2]:
                nums1[p] = nums1[p1]
                p1 -= 1
            else:
                # nums2[p2] is bigger (or equal), or nums1 is exhausted
                nums1[p] = nums2[p2]
                p2 -= 1

            p -= 1
```
#### Pattern (Java)

```java
// java
// LC 88 - Merge Sorted Array
// time = O(m + n), space = O(1)
public void merge(int[] nums1, int m, int[] nums2, int n) {
    int p1 = m - 1;          // read nums1
    int p2 = n - 1;          // read nums2
    int p = m + n - 1;       // write

    /** NOTE !!! loop on p2 only */
    while (p2 >= 0) {
        if (p1 >= 0 && nums1[p1] > nums2[p2]) {
            nums1[p--] = nums1[p1--];
        } else {
            nums1[p--] = nums2[p2--];
        }
    }
}
```

#### Common Pitfalls

| Pitfall | Why it breaks | Fix |
|---------|---------------|-----|
| Merging **left → right** | Overwrites unread `nums1` elements | Merge right → left |
| `while (p1 >= 0 && p2 >= 0)` with no flush | Leftover `nums2` elements never copied | Loop on `p2` only, **or** add `nums1[:p2+1] = nums2[:p2+1]` |
| Reading `nums1[p1]` without `p1 >= 0` guard | Index error when `nums1` exhausted first | Short-circuit: `p1 >= 0 && nums1[p1] > nums2[p2]` |
| Using `nums1 = sorted(nums1 + nums2)` | Rebinds local var, does NOT modify in place | Slice-assign or backward merge |
| Starting `p` at `m - 1` | Wrong write index (`nums1` size is `m + n`) | `p = m + n - 1` |

#### Similar Problems

| Problem | LC# | Key Difference |
|---------|-----|----------------|
| **Merge Sorted Array** | **88** | **In-place into `nums1`'s tail; merge backward** |
| Merge Two Sorted Lists | 21 | Linked list; merge forward with a dummy head |
| Merge k Sorted Lists | 23 | k lists; heap or divide & conquer |
| Squares of a Sorted Array | 977 | Fill result backward (largest square at the ends) |
| Sorted Merge / merge sort step | — | Same routine as merge sort's combine phase |
| Intersection of Two Arrays II | 350 | Two pointers over sorted arrays, keep common elements |
| Interval List Intersections | 986 | Two pointers over sorted intervals |
| Find Median of Two Sorted Arrays | 4 | Conceptual merge, but O(log(m+n)) binary search |
| Move Zeroes | 283 | In-place write pointer (forward direction is safe here) |


### Template 6: Three-Way Partition, Dutch National Flag — LC 75 ⭐⭐⭐⭐
Partition array into three groups in O(n) time, O(1) space using three pointers.

```python
def sortColors(nums):
    lo, mid, hi = 0, 0, len(nums) - 1
    while mid <= hi:
        if nums[mid] == 0:
            nums[lo], nums[mid] = nums[mid], nums[lo]
            lo += 1; mid += 1
        elif nums[mid] == 1:
            mid += 1
        else:
            nums[mid], nums[hi] = nums[hi], nums[mid]
            hi -= 1  # don't advance mid — new nums[mid] is unknown
```

Invariant: `nums[0..lo-1]=0`, `nums[lo..mid-1]=1`, `nums[mid..hi]=unknown`, `nums[hi+1..n-1]=2`.

**Pattern: Three-Way Partitioning with Two Pointers**
- Use three pointers: left (0s), mid (current), right (2s)
- Partition array into three sections
- Single pass solution

```java
// java
// LC 75. Sort Colors
/**
 * Pattern: Dutch National Flag - Three-way partitioning
 *
 * Goal: Sort array with only 0, 1, 2 in one pass
 *
 * Pointers:
 *   - left: boundary for 0s (everything before left is 0)
 *   - mid: current element being examined
 *   - right: boundary for 2s (everything after right is 2)
 *
 * Example:
 *   nums = [2,0,2,1,1,0]
 *
 *   [2,0,2,1,1,0]    mid=0, nums[mid]=2, swap with right, right--
 *    l           r   [0,0,2,1,1,2]
 *    m
 *
 *   [0,0,2,1,1,2]    mid=0, nums[mid]=0, swap with left, left++, mid++
 *    l         r
 *    m
 *
 *   [0,0,2,1,1,2]    mid=1, nums[mid]=0, swap with left, left++, mid++
 *      l       r
 *      m
 *
 *   [0,0,2,1,1,2]    mid=2, nums[mid]=2, swap with right, right--
 *        l     r
 *        m
 *
 *   [0,0,1,1,2,2]    mid=2, nums[mid]=1, mid++
 *        l   r
 *        m
 *
 *   [0,0,1,1,2,2]    mid=3, nums[mid]=1, mid++
 *        l r
 *          m
 *
 *   mid > right, done!
 *
 * Time: O(N), Space: O(1)
 */
public void sortColors(int[] nums) {
    int left = 0;           // Next position for 0
    int mid = 0;            // Current examining position
    int right = nums.length - 1;  // Next position for 2

    while (mid <= right) {
        if (nums[mid] == 0) {
            // Found 0, swap to left
            swap(nums, left, mid);
            left++;
            mid++;
        } else if (nums[mid] == 2) {
            // Found 2, swap to right
            // NOTE: Don't increment mid yet, need to check swapped element
            swap(nums, mid, right);
            right--;
        } else {
            // Found 1, just move mid
            mid++;
        }
    }
}

private void swap(int[] nums, int i, int j) {
    int temp = nums[i];
    nums[i] = nums[j];
    nums[j] = temp;
}
```

**Similar Problems:**
- LC 75 Sort Colors (this pattern)
- LC 26 Remove Duplicates from Sorted Array
- LC 80 Remove Duplicates from Sorted Array II
- LC 283 Move Zeroes


### Template 7: Fast/Slow Cycle Detection — LC 141, LC 142 ⭐⭐⭐⭐
Fast pointer moves 2 steps, slow moves 1. They meet inside the cycle (if one exists).

```python
# LC 141 — Detect cycle
def hasCycle(head):
    slow = fast = head
    while fast and fast.next:
        slow = slow.next
        fast = fast.next.next
        if slow is fast:
            return True
    return False

# LC 142 — Find cycle entry point
def detectCycle(head):
    slow = fast = head
    while fast and fast.next:
        slow = slow.next
        fast = fast.next.next
        if slow is fast:
            break
    else:
        return None
    # Reset one pointer to head; advance both one step at a time
    slow = head
    while slow is not fast:
        slow = slow.next
        fast = fast.next
    return slow   # entry point of cycle
```

> The node-based two-pointer family (middle node, nth from end, reorder, palindrome list) is [2_pointers_linkedlist.md](./2_pointers_linkedlist.md).

### Template 8: Advance Right, Then Advance Left (Condition-Driven)

```java
// java
// LC 567

int l = 0;
for (int r = 0; r < s2.length(); r++){
    // ...

    if(some_condition){

        // ...
        // update map and move left pointer
        l += 1;
    }
}


// ...
```


> This is the sliding-window skeleton; the window family (LC 3, LC 76, LC 209, LC 567) is owned by [sliding_window.md](./sliding_window.md).

### Basic Operation: Reverse an Array
```java
// java
void reverse(int[] nums){

    int left = 0;
    int right = nums.length - 1;

    while (left < right){

        int tmp = nums[left];
        nums[left] = nums[right];
        nums[right] = tmp;
        
        left += 1;
        right -= 1;
    }
}
```

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
