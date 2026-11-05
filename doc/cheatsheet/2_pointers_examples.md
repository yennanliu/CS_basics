# Two Pointers — Worked Examples

> **Scope** — The worked two-pointer LeetCode catalogue, one canonical solution per problem, grouped by the template in the main two-pointer sheet that it reduces to; the concepts, pointer types and templates themselves stay in the main two-pointer sheet.
> **See also**: [2_pointers.md](./2_pointers.md) — the concepts and the canonical template each example instantiates; [quickselect.md](./quickselect.md) — Kth-element selection, split out of the same sheet; [sliding_window.md](./sliding_window.md) — condition-driven windows; [n_sum.md](./n_sum.md) — the k-sum family in depth; [palindrome.md](./palindrome.md) — the palindrome family in depth.

## LeetCode Problem Lists

- [Two Pointers](https://leetcode.com/problem-list/two-pointers/)
- [Array](https://leetcode.com/problem-list/array/)
- [String](https://leetcode.com/problem-list/string/)

## Overview

The two-pointer problems that add one step to a template. [2_pointers.md](./2_pointers.md) holds the
templates; this sheet is the problems, each filed under the card it reduces to, with the difference
named in its **Reduces to** line. A problem whose worked solution lives in another sheet is a pointer
row at the end, not a second copy.

## Index

| Pattern | Problems (easy → hard) | Core ⭐ |
|---|---|---|
| [Template 1 — Fast/Slow Read-Write Compaction](./2_pointers.md#template-1-fastslow-read-write-compaction--lc-26-) | [LC 283](#move-zeroes--lc-283) · [LC 80](#remove-duplicates-from-sorted-array-ii--lc-80) | — |
| [Template 2 — Converging Pointers from Both Ends](./2_pointers.md#template-2-converging-pointers-from-both-ends--lc-11-) | [LC 942](#di-string-match--lc-942) · [LC 16](#3sum-closest--lc-16) · [LC 31](#next-permutation--lc-31) · [LC 42](#trapping-rain-water--lc-42) | — |
| [Template 3 — Expand from Centre](./2_pointers.md#template-3-expand-from-centre--lc-5-) | [LC 647](#palindromic-substrings--lc-647-) · [LC 845](#longest-mountain-in-array--lc-845) | LC 647 |
| [Template 4 — Subsequence Matching](./2_pointers.md#template-4-subsequence-matching-one-pointer-always-moves--lc-392-) | [LC 524](#longest-word-in-dictionary-through-deleting--lc-524-) · [LC 1023](#camelcase-matching--lc-1023) | — |
| [Template 9 — Two Sequences in Lockstep](./2_pointers.md#template-9-two-sequences-in-lockstep--lc-165-) | [LC 161](#one-edit-distance--lc-161) · [LC 271](#encode-and-decode-strings--lc-271) | LC 271 |
| [Lives elsewhere](#pointers) | LC 5 · LC 15 · LC 57 · LC 125 · LC 128 · LC 165 · LC 214 · LC 240 · LC 344 · LC 680 · LC 763 · LC 809 · LC 953 · LC 986 · LC 2104 | LC 5 · LC 15 · LC 57 · LC 125 · LC 128 |

## Fast/Slow Compaction Examples

### Move Zeroes — LC 283

> **Key point** — the compaction of Template 1 with a **swap** instead of a copy: every non-zero is
> swapped down to `slow`, so the zeros drift to the tail and nothing has to be filled in afterwards.
> The relative order of the non-zeros is kept because `slow` only ever moves forward.

**Reduces to**: [2_pointers § Template 1](./2_pointers.md#template-1-fastslow-read-write-compaction--lc-26-) — keep test is `nums[fast] != 0`, and the write is a swap so the dropped values survive at the end.

```python
# python
# LC 283 - Move Zeroes
# IDEA: fast scans every index; each non-zero is swapped down to the write slot `slow`
# time = O(n), space = O(1)
def moveZeroes(nums):
    slow = 0                                   # next slot for a non-zero
    for fast in range(len(nums)):
        if nums[fast] != 0:
            nums[slow], nums[fast] = nums[fast], nums[slow]
            slow += 1
```

```java
// java
// LC 283 - Move Zeroes
// IDEA: fast scans every index; each non-zero is swapped down to the write slot `slow`
// time = O(n), space = O(1)
public void moveZeroes(int[] nums) {
    int slow = 0;                              // next slot for a non-zero
    for (int fast = 0; fast < nums.length; fast++) {
        if (nums[fast] != 0) {
            int tmp = nums[slow];
            nums[slow] = nums[fast];
            nums[fast] = tmp;
            slow++;
        }
    }
}
```

**Traps**
- Copying instead of swapping (`nums[slow] = nums[fast]`) on `[0,1,0,3,12]` leaves `[1,3,12,3,12]`:
  a copy needs a second pass that writes zeros from `slow` to the end.
- A swap with `slow == fast` (`[1,2,3]`, no zeros) is a harmless self-swap; an `if slow != fast`
  guard only saves writes, it is not a correctness fix.

**Similar** — LC 27 (drop a value, Template 1) · LC 905 (evens first) · LC 2460 (apply, then move zeroes)

### Remove Duplicates from Sorted Array II — LC 80

> **Key point** — "at most k copies" is Template 1 with the keep test moved back k slots: keep
> `nums[fast]` when it differs from `nums[slow - k]`, the value **k writes ago**. Because the array
> is sorted, an equal value there means k copies are already kept. LC 26 is the same loop with k = 1.

**Reduces to**: [2_pointers § Template 1](./2_pointers.md#template-1-fastslow-read-write-compaction--lc-26-) — compare against `nums[slow - k]` instead of `nums[slow - 1]`; the first k values are always kept.

```python
# python
# LC 80 - Remove Duplicates from Sorted Array II
# IDEA: keep nums[fast] unless the slot k writes back already holds the same value (k = 2)
# time = O(n), space = O(1)
def removeDuplicates(nums):
    k = 2                                      # copies allowed
    slow = 0                                   # next write slot = count kept so far
    for fast in range(len(nums)):
        if slow < k or nums[fast] != nums[slow - k]:
            nums[slow] = nums[fast]
            slow += 1
    return slow
```

```java
// java
// LC 80 - Remove Duplicates from Sorted Array II
// IDEA: keep nums[fast] unless the slot k writes back already holds the same value (k = 2)
// time = O(n), space = O(1)
public int removeDuplicates(int[] nums) {
    int k = 2;                                 // copies allowed
    int slow = 0;                              // next write slot = count kept so far
    for (int fast = 0; fast < nums.length; fast++) {
        if (slow < k || nums[fast] != nums[slow - k]) {
            nums[slow] = nums[fast];
            slow++;
        }
    }
    return slow;
}
```

```text
nums = [0,0,1,1,1,1,2,3,3], k = 2

fast  nums[fast]  slow  nums[slow-2]  keep?   kept so far
0     0           0     -             yes     [0]
1     0           1     -             yes     [0,0]
2     1           2     0             yes     [0,0,1]
3     1           3     0             yes     [0,0,1,1]
4     1           4     1             no
5     1           4     1             no
6     2           4     1             yes     [0,0,1,1,2]
7     3           5     1             yes     [0,0,1,1,2,3]
8     3           6     2             yes     [0,0,1,1,2,3,3]   -> return 7
```

**Traps**
- Comparing with `nums[fast - k]` (the read side) instead of `nums[slow - k]` (the written side)
  fails on `[1,1,1,2,2,3]`: the read side still holds the third `1` after it has been skipped.
- Starting `slow` at 1 and `fast` at 2 (an earlier version of this card) drops a value on
  `[0,0,1,1,1,1,2,3,3]`: the guard `slow < 2` lets index 1 be overwritten.

**Similar** — LC 26 (k = 1) · LC 27 (drop one value) · LC 283 (swap instead of copy)

## Converging Pointers Examples

### DI String Match — LC 942

> **Key point** — the two pointers converge over the **values** `0..n`, not over indices. On `"I"`
> place the smallest unused value (everything left is larger); on `"D"` place the largest (everything
> left is smaller). The unused values always form the range `[low, high]`, so the last one is `low`.

**Reduces to**: [2_pointers § Template 2](./2_pointers.md#template-2-converging-pointers-from-both-ends--lc-11-) — the ends are the smallest and largest unused value, and the character decides which end moves.

```python
# python
# LC 942 - DI String Match
# IDEA: 'I' -> take the smallest unused value, 'D' -> take the largest; the survivor goes last
# time = O(n), space = O(n) for the output
def diStringMatch(s):
    low, high = 0, len(s)
    ans = []
    for ch in s:
        if ch == "I":
            ans.append(low)                    # whatever comes next is larger
            low += 1
        else:
            ans.append(high)                   # whatever comes next is smaller
            high -= 1
    ans.append(low)                            # low == high: the one value left
    return ans
```

```java
// java
// LC 942 - DI String Match
// IDEA: 'I' -> take the smallest unused value, 'D' -> take the largest; the survivor goes last
// time = O(n), space = O(n) for the output
public int[] diStringMatch(String s) {
    int n = s.length();
    int low = 0, high = n;
    int[] ans = new int[n + 1];
    for (int i = 0; i < n; i++) {
        if (s.charAt(i) == 'I') {
            ans[i] = low;
            low++;
        } else {
            ans[i] = high;
            high--;
        }
    }
    ans[n] = low;                              // low == high: the one value left
    return ans;
}
```

```text
s = "IDID"   low = 0, high = 4

char  takes      ans           low  high
I     low  = 0   [0]           1    4
D     high = 4   [0,4]         1    3
I     low  = 1   [0,4,1]       2    3
D     high = 3   [0,4,1,3]     2    2
end   low  = 2   [0,4,1,3,2]
```

**Traps**
- The output has n + 1 values for n characters; forgetting the final `low` returns a permutation
  of the wrong size.

**Similar** — LC 31 (next permutation) · LC 903 (count DI permutations — DP, not pointers)

### 3Sum Closest — LC 16

> **Key point** — fix `nums[i]`, then converge on the rest of the sorted array. The comparison with
> `target` still says which end to move (too small → `left++`, too big → `right--`), and every sum
> visited is a candidate, so keep the one with the smallest `|sum − target|`. An exact hit ends it.

**Reduces to**: [2_pointers § Template 2](./2_pointers.md#template-2-converging-pointers-from-both-ends--lc-11-) — after a sort and one fixed element; the answer is the closest sum seen, not a match. The fix-one loop is [n_sum.md](./n_sum.md)'s.

```python
# python
# LC 16 - 3Sum Closest
# IDEA: sort; fix i, converge left/right on the rest; keep the sum nearest to target
# time = O(n^2), space = O(1) besides the sort
def threeSumClosest(nums, target):
    nums.sort()
    closest = nums[0] + nums[1] + nums[2]
    for i in range(len(nums) - 2):
        left, right = i + 1, len(nums) - 1
        while left < right:
            total = nums[i] + nums[left] + nums[right]
            if abs(total - target) < abs(closest - target):
                closest = total
            if total == target:
                return total                   # cannot get closer than 0
            if total < target:
                left += 1                      # need a bigger sum
            else:
                right -= 1                     # need a smaller sum
    return closest
```

```java
// java
// LC 16 - 3Sum Closest
// IDEA: sort; fix i, converge left/right on the rest; keep the sum nearest to target
// time = O(n^2), space = O(1) besides the sort
public int threeSumClosest(int[] nums, int target) {
    Arrays.sort(nums);
    int closest = nums[0] + nums[1] + nums[2];
    for (int i = 0; i < nums.length - 2; i++) {
        int left = i + 1, right = nums.length - 1;
        while (left < right) {
            int sum = nums[i] + nums[left] + nums[right];
            if (Math.abs(sum - target) < Math.abs(closest - target)) {
                closest = sum;
            }
            if (sum == target) {
                return sum;
            } else if (sum < target) {
                left++;
            } else {
                right--;
            }
        }
    }
    return closest;
}
```

**Traps**
- Initialising `closest` to `0` or `Integer.MAX_VALUE` breaks: `0` may beat every real sum, and
  `MAX_VALUE - target` overflows for a negative target. Start from a real triplet.
- No duplicate skipping is needed: the answer is one number, not a set of triplets as in LC 15.

**Similar** — LC 15 (all exact triplets, [n_sum.md](./n_sum.md)) · LC 259 (count sums below target) · LC 18 (4Sum)

### Next Permutation — LC 31

> **Key point** — scan from the right for the first `nums[i] < nums[i + 1]`: everything after `i`
> is descending, so it is already the largest arrangement of its values. Swap `nums[i]` with the
> smallest larger value on its right (the rightmost one bigger than it), and the suffix is **still**
> descending — so reversing it with converging pointers turns it into the smallest arrangement.

**Reduces to**: [2_pointers § Template 2](./2_pointers.md#template-2-converging-pointers-from-both-ends--lc-11-) — the converging swap is the last step (reverse the suffix after the pivot), after a right-to-left scan finds the pivot and its successor.

```python
# python
# LC 31 - Next Permutation
# IDEA: pivot = rightmost i with nums[i] < nums[i+1]; swap it with the rightmost larger value;
#       reverse the (descending) suffix after the pivot
# time = O(n), space = O(1)
def nextPermutation(nums):
    i = len(nums) - 2
    while i >= 0 and nums[i] >= nums[i + 1]:   # find the pivot
        i -= 1
    if i >= 0:                                 # no pivot -> the whole array is descending
        j = len(nums) - 1
        while nums[j] <= nums[i]:              # rightmost value larger than the pivot
            j -= 1
        nums[i], nums[j] = nums[j], nums[i]
    left, right = i + 1, len(nums) - 1         # reverse the suffix: converging swaps
    while left < right:
        nums[left], nums[right] = nums[right], nums[left]
        left += 1
        right -= 1
```

```java
// java
// LC 31 - Next Permutation
// IDEA: pivot = rightmost i with nums[i] < nums[i+1]; swap it with the rightmost larger value;
//       reverse the (descending) suffix after the pivot
// time = O(n), space = O(1)
public void nextPermutation(int[] nums) {
    int i = nums.length - 2;
    while (i >= 0 && nums[i] >= nums[i + 1]) {
        i--;
    }
    if (i >= 0) {
        int j = nums.length - 1;
        while (nums[j] <= nums[i]) {
            j--;
        }
        int tmp = nums[i];
        nums[i] = nums[j];
        nums[j] = tmp;
    }
    int left = i + 1, right = nums.length - 1;
    while (left < right) {
        int tmp = nums[left];
        nums[left] = nums[right];
        nums[right] = tmp;
        left++;
        right--;
    }
}
```

```text
nums = [1,2,5,4,3]

pivot      : i = 1 (2 < 5); suffix [5,4,3] is descending
successor  : j = 4 (3 is the rightmost value > 2)
swap       : [1,3,5,4,2]   suffix [5,4,2] still descending
reverse    : [1,3,2,4,5]
```

**Traps**
- `>=` / `<=` in the two scans, not `>` / `<`: with duplicates (`[1,5,1]`) a strict test picks an
  equal value as the successor and returns the same permutation.
- No pivot (`[3,2,1]`) is not a special case: `i = -1`, the swap is skipped, and reversing from
  index 0 wraps to the first permutation.

**Similar** — LC 556 (the same on the digits of an int, with an overflow check) · LC 1053 (previous permutation with one swap) · LC 60 (k-th permutation directly)

### Trapping Rain Water — LC 42

> **Key point** — the water over a bar is `min(maxLeft, maxRight) − height`. Move the side whose
> bar is **lower**: if `height[left] < height[right]`, the right side already has a wall at least
> as tall as anything on the left, so `left_max` alone bounds the water at `left`.

**Reduces to**: [2_pointers § Template 2](./2_pointers.md#template-2-converging-pointers-from-both-ends--lc-11-) — the lower side moves, as in LC 11, and each pointer carries a running maximum.

```python
# python
# LC 42 - Trapping Rain Water
# IDEA: converge; the side with the lower bar is bounded by its own running max, so settle it
# time = O(n), space = O(1)
def trap(height):
    left, right = 0, len(height) - 1
    left_max = right_max = 0
    water = 0
    while left < right:
        if height[left] < height[right]:
            left_max = max(left_max, height[left])
            water += left_max - height[left]   # right wall is at least as tall
            left += 1
        else:
            right_max = max(right_max, height[right])
            water += right_max - height[right]
            right -= 1
    return water
```

**Traps**
- Moving the **taller** side settles a bar whose real bound is on the other side:
  `[4,2,0,3,2,5]` gives 7 instead of 9.
- `max` before the add, not after: on `[0,1,0,2,1,0,1,3,2,1,2,1]` updating after adds
  `0 - 1` at the first rising bar and returns 2 instead of 6.

**Similar** — LC 11 (the same move rule, area instead of water) · LC 407 (2-D — a heap, not pointers) · the monotonic-stack approach: [monotonic_stack.md](./monotonic_stack.md)

## Expand from Centre Examples

### Palindromic Substrings — LC 647 ⭐⭐⭐⭐

> **Key point** — every palindrome has one centre, and there are `2n − 1` of them (each character,
> each gap). Expanding from a centre visits its palindromes from shortest to longest and stops at
> the first mismatch, so **each successful step is one palindrome**: count steps instead of keeping
> the longest.

**Reduces to**: [2_pointers § Template 3](./2_pointers.md#template-3-expand-from-centre--lc-5-) — the expansion returns a count, not a span.

```python
# python
# LC 647 - Palindromic Substrings
# IDEA: expand from all 2n - 1 centres; every matching step is one more palindrome
# time = O(n^2), space = O(1)
def countSubstrings(s):
    def expand(left, right):
        count = 0
        while left >= 0 and right < len(s) and s[left] == s[right]:
            count += 1                         # s[left..right] is a palindrome
            left -= 1
            right += 1
        return count

    total = 0
    for centre in range(len(s)):
        total += expand(centre, centre)        # odd length
        total += expand(centre, centre + 1)    # even length
    return total
```

**Traps**
- Only odd centres: `"aaa"` gives 4 instead of 6.
- The brute force (check every substring with `s[i:j] == s[i:j][::-1]`) is O(n³); the interval DP
  ([dp.md](./dp.md)) is O(n²) time **and** space — expansion is the O(1)-space answer.

**Similar** — LC 5 (keep the longest — [Template 3](./2_pointers.md#template-3-expand-from-centre--lc-5-)) · LC 516 (subsequence — interval DP) · LC 131 (partition into palindromes)

### Longest Mountain in Array — LC 845

> **Key point** — expand outward from a **peak** instead of a centre: walk left while the values
> keep rising toward the peak, right while they keep falling. Then jump `i` to the right base —
> the descending slope cannot hold another peak — so every index is visited a bounded number of times.

**Reduces to**: [2_pointers § Template 3](./2_pointers.md#template-3-expand-from-centre--lc-5-) — the centre is a strict local maximum, the two sides expand independently (by monotone slope, not by matching), and `i` skips the expanded span.

```python
# python
# LC 845 - Longest Mountain in Array
# IDEA: at each strict peak, expand left up the ascent and right down the descent; skip i to the base
# time = O(n), space = O(1)
def longestMountain(arr):
    n = len(arr)
    best = 0
    i = 1
    while i < n - 1:
        is_peak = arr[i - 1] < arr[i] and arr[i] > arr[i + 1]
        if is_peak:
            left = i - 1
            while left > 0 and arr[left - 1] < arr[left]:
                left -= 1
            right = i + 1
            while right < n - 1 and arr[right] > arr[right + 1]:
                right += 1
            best = max(best, right - left + 1)
            i = right                          # the descent holds no other peak
        i += 1
    return best
```

```java
// java
// LC 845 - Longest Mountain in Array
// IDEA: at each strict peak, expand left up the ascent and right down the descent; skip i to the base
// time = O(n), space = O(1)
public int longestMountain(int[] arr) {
    int n = arr.length;
    int best = 0;
    for (int i = 1; i < n - 1; i++) {
        if (arr[i - 1] < arr[i] && arr[i] > arr[i + 1]) {
            int left = i - 1;
            while (left > 0 && arr[left - 1] < arr[left]) {
                left--;
            }
            int right = i + 1;
            while (right < n - 1 && arr[right] > arr[right + 1]) {
                right++;
            }
            best = Math.max(best, right - left + 1);
            i = right;                         // the descent holds no other peak
        }
    }
    return best;
}
```

```text
arr = [2,1,4,7,3,2,5]

i = 3: 4 < 7 > 3 -> peak
  left : 2 -> 1 (arr[0] = 2 > arr[1] = 1, stop)
  right: 4 -> 5 (arr[5] = 2 < arr[6] = 5, stop)
  length = 5 - 1 + 1 = 5   [1,4,7,3,2]
i jumps to 5, then 6 = n - 1 -> done
```

**Traps**
- Strict comparisons everywhere: `[2,2,2]` has no mountain, and a plateau at the top
  (`[1,3,3,1]`) is not a peak.
- Without `i = right` the loop re-checks every index on each descent — still correct, but the
  O(n) argument is gone.

**Similar** — LC 941 (is the whole array one mountain) · LC 852 / LC 162 (find a peak — binary search) · the `up[]` / `down[]` arrays version: [prefix_sum.md](./prefix_sum.md#run-length-variant--the-side-resets-so-build-both-arrays-lc-845-)

## Subsequence Matching Examples

### Longest Word in Dictionary through Deleting — LC 524 ⭐⭐⭐⭐

> **Key point** — "formed by deleting characters of `s`" means "is a subsequence of `s`", so this is
> LC 392 run once per word. The only new part is the winner: longer beats shorter, and on equal
> length the lexicographically smaller word wins — compare against the running best, never return
> the first match.

**Reduces to**: [2_pointers § Template 4](./2_pointers.md#template-4-subsequence-matching-one-pointer-always-moves--lc-392-) — once per dictionary word, keeping the best by (length, then lexicographic order).

```python
# python
# LC 524 - Longest Word in Dictionary through Deleting
# IDEA: subsequence check per word (i on s always moves, j on word moves on a match); keep the best
# time = O(d * (n + L)) for d words of length up to L, n = len(s); space = O(1) besides the answer
def findLongestWord(s, dictionary):
    best = ""
    for word in dictionary:
        i = j = 0
        while i < len(s) and j < len(word):
            if s[i] == word[j]:
                j += 1                         # matched one more character of word
            i += 1                             # s always moves
        if j == len(word):                     # word is a subsequence of s
            if len(word) > len(best) or (len(word) == len(best) and word < best):
                best = word
    return best
```

```java
// java
// LC 524 - Longest Word in Dictionary through Deleting
// IDEA: subsequence check per word (i on s always moves, j on word moves on a match); keep the best
// time = O(d * (n + L)), space = O(1) besides the answer
public String findLongestWord(String s, List<String> dictionary) {
    String best = "";
    for (String word : dictionary) {
        int i = 0, j = 0;
        while (i < s.length() && j < word.length()) {
            if (s.charAt(i) == word.charAt(j)) {
                j++;
            }
            i++;
        }
        if (j == word.length()) {
            boolean longer = word.length() > best.length();
            boolean tieButSmaller = word.length() == best.length() && word.compareTo(best) < 0;
            if (longer || tieButSmaller) {
                best = word;
            }
        }
    }
    return best;
}
```

**Traps**
- Returning the first subsequence found: on `s = "abpcplea"`, `["ale","apple"]` returns `"ale"`.
  Either compare with the running best (above) or sort by `(-len, word)` first and then return the
  first match — the sort costs O(d log d) more.
- Comparing only lengths: `["b","a"]` must return `"a"`.

**Similar** — LC 392 (the inner check) · LC 792 (many words — bucket by next needed character) · LC 2486 (count the unmatched tail)

### Camelcase Matching — LC 1023

> **Key point** — the pattern must be a subsequence of the query, **and** every query character the
> pattern skips must be lowercase. So one extra branch: a non-matching uppercase letter fails at once.

**Reduces to**: [2_pointers § Template 4](./2_pointers.md#template-4-subsequence-matching-one-pointer-always-moves--lc-392-) — the always-moving pointer may only skip lowercase letters.

```python
# python
# LC 1023 - Camelcase Matching
# IDEA: query pointer always moves, pattern pointer moves on a match; a skipped uppercase fails
# time = O(total length of queries), space = O(1) besides the output
def camelMatch(queries, pattern):
    def matches(query):
        j = 0
        for ch in query:
            if j < len(pattern) and ch == pattern[j]:
                j += 1                         # consumed one pattern character
            elif ch.isupper():
                return False                   # an extra capital cannot be inserted
        return j == len(pattern)

    result = []
    for query in queries:
        result.append(matches(query))
    return result
```

```java
// java
// LC 1023 - Camelcase Matching
// IDEA: query pointer always moves, pattern pointer moves on a match; a skipped uppercase fails
// time = O(total length of queries), space = O(1) besides the output
public List<Boolean> camelMatch(String[] queries, String pattern) {
    List<Boolean> result = new ArrayList<>();
    for (String query : queries) {
        result.add(matches(query, pattern));
    }
    return result;
}

private boolean matches(String query, String pattern) {
    int j = 0;
    for (int i = 0; i < query.length(); i++) {
        char ch = query.charAt(i);
        if (j < pattern.length() && ch == pattern.charAt(j)) {
            j++;
        } else if (Character.isUpperCase(ch)) {
            return false;
        }
    }
    return j == pattern.length();
}
```

**Traps**
- Checking the match before the bounds: once the pattern is used up (`"FooBarTest"`, `"FB"`), the
  `j < len(pattern)` guard is what sends the trailing `T` to the uppercase test.
- Matching first is required: an uppercase letter that **is** the next pattern character is
  consumed, not rejected.

**Similar** — LC 392 (no type rule) · LC 524 (many words, keep the best)

## Two Sequences in Lockstep Examples

### One Edit Distance — LC 161

> **Key point** — walk both strings together until the first mismatch. There is exactly one repair
> that can apply there — replace (same lengths) or insert into the shorter — and after it the two
> remainders must be **equal**, so one comparison of the suffixes decides it.

**Reduces to**: [2_pointers § Template 9](./2_pointers.md#template-9-two-sequences-in-lockstep--lc-165-) — the lockstep scan stops at the first difference, and the rest is a single suffix comparison.

```python
# python
# LC 161 - One Edit Distance
# IDEA: keep s the shorter; at the first mismatch apply the only possible edit and compare suffixes
# time = O(n), space = O(n) for the suffix slices
def isOneEditDistance(s, t):
    if len(s) > len(t):
        return isOneEditDistance(t, s)         # s is the shorter one
    if len(t) - len(s) > 1:
        return False
    for i in range(len(s)):
        if s[i] != t[i]:
            if len(s) == len(t):
                return s[i + 1:] == t[i + 1:]  # replace s[i]
            return s[i:] == t[i + 1:]          # insert t[i] into s
    return len(t) == len(s) + 1                # no mismatch: only a trailing insert is one edit
```

```java
// java
// LC 161 - One Edit Distance
// IDEA: keep s the shorter; at the first mismatch apply the only possible edit and compare suffixes
// time = O(n), space = O(n) for the substrings
public boolean isOneEditDistance(String s, String t) {
    if (s.length() > t.length()) {
        return isOneEditDistance(t, s);
    }
    if (t.length() - s.length() > 1) {
        return false;
    }
    for (int i = 0; i < s.length(); i++) {
        if (s.charAt(i) != t.charAt(i)) {
            if (s.length() == t.length()) {
                return s.substring(i + 1).equals(t.substring(i + 1));
            }
            return s.substring(i).equals(t.substring(i + 1));
        }
    }
    return t.length() == s.length() + 1;
}
```

**Traps**
- Equal strings are **zero** edits: `("abc", "abc")` and `("", "")` return false.
- `("ab", "ba")` needs two replaces; the suffix comparison after the first mismatch is what
  catches it.

**Similar** — LC 72 (minimum edits — DP, [dp.md](./dp.md)) · LC 680 (one deletion to a palindrome) · LC 844 (compare after backspaces)

### Encode and Decode Strings — LC 271

> **Key point** — a delimiter alone cannot work, because any character may appear inside a word.
> Write each word as `len#word`: the decoder reads digits up to the first `#`, then **jumps** exactly
> `len` characters, so a `#` inside a word is never read as a separator.

**Reduces to**: [2_pointers § Template 9](./2_pointers.md#template-9-two-sequences-in-lockstep--lc-165-) — one sequence, consumed one token per round; the token's end comes from its length header instead of a delimiter.

```python
# python
# LC 271 - Encode and Decode Strings
# IDEA: encode each word as len + "#" + word; decode: j finds the "#", the digits before it
#       are the length, then i jumps past the word to the next header
# time = O(N) for N total characters, space = O(N)
class Codec:
    def encode(self, strs):
        parts = []
        for word in strs:
            parts.append(str(len(word)) + "#" + word)
        return "".join(parts)

    def decode(self, s):
        result = []
        i = 0                                  # start of the next length header
        while i < len(s):
            j = i
            while s[j] != "#":                 # the first '#' after i ends the header
                j += 1
            length = int(s[i:j])
            result.append(s[j + 1:j + 1 + length])
            i = j + 1 + length                 # jump over the word, '#'s and all
        return result
```

```text
encode(["Hello", "World"]) = "5#Hello5#World"

i    j (the '#')   length   word      next i
0    1             5        Hello     7
7    8             5        World     14 = len(s) -> stop
```

**Traps**
- Splitting on `#` breaks on `["a#b"]`; the length header is what makes the format unambiguous.
- `[""]` and `[]` must round-trip differently: `"0#"` versus `""`.
- Building the encoded string with `+=` in a loop is quadratic in the worst case; collect parts and
  `join`. The `s.find("#", i)` form of the loop is in [python_trick.md](./python_trick.md).

**Similar** — LC 297 (serialize a tree) · LC 443 (string compression — length after the run)

## Pointers

These problems are worked in full elsewhere; this sheet keeps only the row.

| LC | Problem | Reduces to here | Worked in |
|---|---|---|---|
| 5 | Longest Palindromic Substring | Template 3 anchor | [2_pointers.md § Template 3](./2_pointers.md#template-3-expand-from-centre--lc-5-); Manacher's O(n) in [advanced_string_algorithms.md](./advanced_string_algorithms.md) |
| 15 | 3Sum | fix one + Template 2 | [n_sum.md](./n_sum.md) |
| 57 | Insert Interval | one list, three phases (before / overlap / after) | [intervals.md](./intervals.md) |
| 125 | Valid Palindrome | Template 2 with skips | [string.md § Template 1](./string.md#template-1-two-pointer-scan--in-place-reversal--lc-125-) |
| 128 | Longest Consecutive Sequence | not two pointers: a hash set, start only at a run's head | [set_examples.md](./set_examples.md) |
| 165 | Compare Version Numbers | Template 9 anchor | [2_pointers.md § Template 9](./2_pointers.md#template-9-two-sequences-in-lockstep--lc-165-) |
| 214 | Shortest Palindrome | longest palindromic prefix — KMP on `s + "#" + reverse(s)` | [string_matching_kmp_rolling_hash.md](./string_matching_kmp_rolling_hash.md) |
| 240 | Search a 2D Matrix II | staircase from the top-right corner | [matrix_examples.md](./matrix_examples.md) |
| 344 | Reverse String | Template 2, swap then move both | [2_pointers.md § Reverse an Array](./2_pointers.md#reverse-an-array) |
| 680 | Valid Palindrome II | Template 2, branch twice at the first mismatch | [palindrome.md](./palindrome.md) |
| 763 | Partition Labels | last occurrence, cut when `i == end` | [string.md § Template 8](./string.md#template-8-greedy-partition-by-last-occurrence--lc-763-) |
| 809 | Expressive Words | Template 9 over runs of equal characters | [string_examples.md](./string_examples.md) |
| 953 | Verifying an Alien Dictionary | Template 9 per adjacent pair, with a rank map | [hash_map.md § Template 6](./hash_map.md#template-6-rank-map--value-to-position--lc-953-) |
| 986 | Interval List Intersections | Template 9 over two interval lists: advance the one that ends first | [intervals.md](./intervals.md) |
| 2104 | Sum of Subarray Ranges | not two pointers: monotonic-stack contribution | [monotonic_stack.md](./monotonic_stack.md) |
