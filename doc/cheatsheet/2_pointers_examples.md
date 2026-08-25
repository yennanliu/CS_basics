# Two Pointers — Worked Examples

> **Scope** — The worked two-pointer LeetCode catalogue, one canonical solution per problem per language, grouped by the template it instantiates; the concepts, pointer types and templates themselves stay in the main two-pointer sheet.
> **See also**: [2_pointers.md](./2_pointers.md) — the concepts and the canonical template each example instantiates; [2_pointers_quickselect.md](./2_pointers_quickselect.md) — Kth-element selection, split out of the same sheet; [sliding_window.md](./sliding_window.md) — condition-driven windows; [n_sum.md](./n_sum.md) — the k-sum family in depth; [palindrome.md](./palindrome.md) — the palindrome family in depth.

## LeetCode Problem Lists

- [Two Pointers](https://leetcode.com/problem-list/two-pointers/)
- [Array](https://leetcode.com/problem-list/array/)
- [String](https://leetcode.com/problem-list/string/)

## Overview

One section per problem. Each problem appears exactly once, filed under the template in [2_pointers.md](./2_pointers.md) that it instantiates — read the template first, then the example.

### Problem Index

| Group | Problems |
|---|---|
| [In-Place Rewriting (Fast/Slow)](#in-place-rewriting-fast-slow) | LC 80, LC 283 |
| [Converging Bidirectional Pointers](#converging-bidirectional-pointers) | LC 15, LC 16, LC 31, LC 42, LC 128, LC 344, LC 942 |
| [Expand from Centre](#expand-from-centre) | LC 5, LC 214, LC 647, LC 680, LC 845 |
| [Subsequence & Two-String Matching](#subsequence-two-string-matching) | LC 161, LC 165, LC 271, LC 524, LC 809, LC 953, LC 1023 |
| [Pointers over Intervals, Matrices and Partitions](#pointers-over-intervals-matrices-and-partitions) | LC 57, LC 240, LC 763, LC 986, LC 2104 |

## In-Place Rewriting (Fast/Slow)

> Template: [Template 1 — Fast/Slow Read-Write Compaction](./2_pointers.md#template-1-fast-slow-read-write-compaction-lc-26-lc-27).

### Remove Duplicates from Sorted Array II — LC 80

#### Core Idea

**"Compare with two positions back" trick:**
- Allow each element **at most twice** → keep an element only if it differs from `nums[slow - 2]`
- Since the array is sorted, if `nums[fast] == nums[slow - 2]`, writing it would create a 3rd consecutive duplicate → skip
- Both `slow` and `fast` start at index 2 (first two elements always allowed)

```text
Key condition: nums[fast] != nums[slow - 2]
  → write nums[fast] to nums[slow], slow++
  
Pointer initialization:
  slow = 2  (write pointer, first 2 slots are always valid)
  fast = 2  (read pointer, scans from index 2 onward)
```

**Why `slow - 2` and not `slow - 1`?**
- `slow - 1` would only prevent 3rd+ duplicates if the LAST two wrote the same value
- `slow - 2` directly checks if the slot two positions back already holds the same value — guaranteeing at most 2 copies

---

```java
// java
// LC 80 - Remove Duplicates from Sorted Array II
// time: O(N), space: O(1)
/**
 *  //--------------------------------
 *  Example 1
 *  //--------------------------------
 *
 *  nums = [1,1,1,2,2,3]
 *
 *  Initial: slow=2, fast=2
 *  [1,1,1,2,2,3]
 *       s
 *       f
 *
 *  fast=2: nums[2]=1, nums[slow-2]=nums[0]=1  → EQUAL, skip  (would be 3rd '1')
 *  fast=3: nums[3]=2, nums[slow-2]=nums[0]=1  → DIFFERENT, write nums[slow]=2, slow=3
 *  [1,1,2,2,2,3]
 *         s
 *           f
 *
 *  fast=4: nums[4]=2, nums[slow-2]=nums[1]=1  → DIFFERENT, write, slow=4
 *  [1,1,2,2,2,3]
 *           s
 *             f
 *
 *  fast=5: nums[5]=3, nums[slow-2]=nums[2]=2  → DIFFERENT, write, slow=5
 *  [1,1,2,2,3,3]
 *             s
 *
 *  return slow = 5  → nums[0..4] = [1,1,2,2,3]
 *
 *  //--------------------------------
 *  Example 2
 *  //--------------------------------
 *
 *  nums = [0,0,1,1,1,1,2,3,3]
 *
 *  Initial: slow=2, fast=2
 *  fast=2: nums[2]=1, nums[0]=0 → DIFFERENT, write, slow=3
 *  fast=3: nums[3]=1, nums[1]=0 → DIFFERENT, write, slow=4
 *  fast=4: nums[4]=1, nums[2]=1 → EQUAL, skip  (3rd '1')
 *  fast=5: nums[5]=1, nums[2]=1 → EQUAL, skip  (4th '1')
 *  fast=6: nums[6]=2, nums[2]=1 → DIFFERENT, write, slow=5
 *  fast=7: nums[7]=3, nums[3]=1 → DIFFERENT, write, slow=6
 *  fast=8: nums[8]=3, nums[4]=1 → DIFFERENT, write, slow=7
 *
 *  return slow = 7  → nums[0..6] = [0,0,1,1,2,3,3]
 */
public int removeDuplicates(int[] nums) {
    if (nums.length <= 2) return nums.length;

    int slow = 2; // write pointer; first 2 elements always valid
    for (int fast = 2; fast < nums.length; fast++) {
        // Only write if current element != element two slots back
        if (nums[fast] != nums[slow - 2]) {
            nums[slow] = nums[fast];
            slow++;
        }
        // else: would create 3rd duplicate → skip
    }
    return slow;
}
```

#### Generalized Pattern: Allow at most K duplicates

```java
// Generic template: allow each element at most K times
// LC 26 is K=1, LC 80 is K=2
public int removeDuplicatesAtMostK(int[] nums, int k) {
    int slow = k;
    for (int fast = k; fast < nums.length; fast++) {
        if (nums[fast] != nums[slow - k]) {
            nums[slow] = nums[fast];
            slow++;
        }
    }
    return slow;
}
// LC 26: call with k=1  →  compare nums[fast] != nums[slow - 1]
// LC 80: call with k=2  →  compare nums[fast] != nums[slow - 2]
```

#### LC 26 vs LC 80 Comparison

| Aspect | LC 26 (at most 1) | LC 80 (at most 2) |
|--------|-------------------|-------------------|
| **Condition** | `nums[fast] != nums[slow - 1]` | `nums[fast] != nums[slow - 2]` |
| **Init** | `slow = 1, fast = 1` | `slow = 2, fast = 2` |
| **Returns** | `slow` | `slow` |
| **Generalized** | `k = 1` | `k = 2` |

#### Similar Problems

| Problem | LC# | Key Difference |
|---------|-----|----------------|
| Remove Duplicates I | 26 | At most 1 copy — compare `nums[slow-1]` |
| Remove Duplicates II | 80 | At most 2 copies — compare `nums[slow-2]` |
| Remove Element | 27 | Remove all of a specific value |
| Move Zeroes | 283 | Keep zeros, move to end |

```python
# LC 080 : Remove Duplicates from Sorted Array II
# V0
# IDEA : 2 POINTERS
#### NOTE : THE nums already ordering
# DEMO
# example 1
# nums = [1,1,1,2,2,3]
#           i j
#           i   j
#        [1,1,2,1,2,3]
#             i   j
#        [1,1,2,2,1,3]
#               i   j
#
# example 2
# nums = [0,0,1,1,1,1,2,3,3] 
#           i j
#        [0,0,1,1,1,1,2,3,3]
#             i j
#        [0,0,1,1,1,1,2,3,3]
#               i j
#        [0,0,1,1,1,1,2,3,3]
#               i   j
#               i     j
#        [0,0,1,1,2,1,1,3,3]
#                 i     j  
#        [0,0,1,1,2,3,1,1,3]
#                   i     j
#        [0,0,1,1,2,3,3,1,1]
class Solution:
    def removeDuplicates(self, nums):
        if len(nums) < 3:
            return len(nums)

        ### NOTE : slow starts from 1
        slow = 1
        ### NOTE : fast starts from 2
        for fast in range(2, len(nums)):
            """
            NOTE : BELOW CONDITION

            1) nums[slow] != nums[fast]: for adding "1st" element
            2) nums[slow] != nums[slow-1] : for adding "2nd" element
            """
            if slow < 2 or nums[fast] != nums[slow - 2]:
                nums[slow] = nums[fast]
                slow += 1
        return slow
```


### Move Zeroes — LC 283
```java
// java
// LC 283 Move Zeroes
// https://leetcode.com/problems/move-zeroes/
/**
 * Pattern: Move all zeros (or specific elements) to the end while maintaining
 * the relative order of non-zero elements. Must be done in-place.
 *
 * Key Idea:
 *   - Both pointers (l and r) start from index 0
 *   - l tracks the position where the next non-zero should be placed
 *   - r scans through the array
 *   - When r finds a non-zero, swap with l and move l forward
 *   - This moves all zeros to the end naturally
 *
 * Difference from "Remove Element" pattern:
 *   - Remove Element: overwrites without caring about moved elements
 *   - Move Zeros: uses SWAP to preserve all elements in array
 *
 *  //--------------------
 *  Example 1
 *  //--------------------
 *
 *  nums = [0,1,0,3,12]
 *
 *  [0,1,0,3,12]
 *   l
 *   r
 *
 *  [0,1,0,3,12]    nums[r]=0, no swap, move r
 *   l
 *     r
 *
 *  [1,0,0,3,12]    nums[r]!=0, swap(l,r), move l and r
 *   l l
 *     r
 *
 *  [1,0,0,3,12]    nums[r]=0, no swap, move r
 *     l
 *       r
 *
 *  [1,3,0,0,12]    nums[r]!=0, swap(l,r), move l and r
 *     l l
 *         r
 *
 *  [1,3,12,0,0]    nums[r]!=0, swap(l,r), move l and r
 *        l  l
 *            r
 *
 *  //--------------------
 *  Example 2
 *  //--------------------
 *
 *  nums = [0]
 *  [0]
 *   l
 *   r
 *  -> only one element, no change
 *
 *  //--------------------
 *  Example 3
 *  //--------------------
 *
 *  nums = [1,0,2,0,3]
 *
 *  [1,0,2,0,3]    nums[r]!=0, swap(l,r), move l and r
 *   l l
 *   r
 *
 *  [1,0,2,0,3]    nums[r]=0, no swap, move r
 *     l
 *     r
 *
 *  [1,2,0,0,3]    nums[r]!=0, swap(l,r), move l and r
 *     l l
 *       r
 *
 *  [1,2,0,0,3]    nums[r]=0, no swap, move r
 *       l
 *         r
 *
 *  [1,2,3,0,0]    nums[r]!=0, swap(l,r), move l and r
 *       l l
 *           r
 *
 * Time: O(N), Space: O(1)
 */
class Solution {
    public void moveZeroes(int[] nums) {
        if (nums == null || nums.length <= 1)
            return;

        // 'l' is the position where the next non-zero number should be placed
        int l = 0;

        /** NOTE !!!
         *
         *  BOTH l, r start from idx = 0
         */
        // Iterate through the array with 'r'
        for (int r = 0; r < nums.length; r++) {
            // If we find a non-zero element
            if (nums[r] != 0) {
                // Swap it with the element at position 'l'
                int tmp = nums[r];
                nums[r] = nums[l];
                nums[l] = tmp;

                /** NOTE !!!
                 *
                 *  Move 'l' forward if `we swap`
                 */
                // Move 'l' forward
                l++;
            }
        }
    }
}
```

**Similar Problems:**
- LC 283 Move Zeroes (this pattern)
- LC 27 Remove Element (overwrite version)
- LC 905 Sort Array By Parity (move even numbers to front)
- LC 922 Sort Array By Parity II (even/odd positioning)
- LC 2460 Apply Operations to an Array
- LC 1089 Duplicate Zeros (expanding array version)


```python
# LC 283 move-zeroes
# V0
class Solution(object):
    def moveZeroes(self, nums):
        y = 0
        for x in range(len(nums)):
            if nums[x] != 0:
                nums[x], nums[y] = nums[y], nums[x]
                y += 1
        return nums
```

## Converging Bidirectional Pointers

> Template: [Template 2 — Converging Bidirectional Pointers](./2_pointers.md#template-2-converging-bidirectional-pointers).

### Next Permutation — LC 31

#### Core Idea

**Find-Pivot → Find-Successor → Swap → Reverse-Suffix:**

1. **Find pivot** — scan right-to-left for the first index `i` where `nums[i] < nums[i+1]`. The suffix `nums[i+1:]` is fully descending (by definition). If no such `i` exists, the whole array is descending → reverse it and return.
2. **Find successor** — scan right-to-left for the first index `j` where `nums[j] > nums[i]`. This is the smallest value in the suffix that beats the pivot.
3. **Swap** `nums[i]` and `nums[j]`. The suffix is still descending after the swap.
4. **Reverse suffix** `nums[i+1:]` — descending → ascending, giving the smallest possible tail.

```text
Key invariant:
  suffix after pivot is ALWAYS descending when we find the pivot.
  After the swap it's still descending (we swapped the smallest-greater element in).
  Reversing descending → ascending gives the smallest suffix.
```

**Why it works:**
- Pivot is the rightmost position where we can increment the number.
- Picking the smallest successor ensures the minimum possible increase at position `i`.
- Reversing the suffix ensures the tail is as small as possible.

**One-sentence mnemonic (3 moves):**
> 1. **Find the first number that can be increased** (the pivot).
> 2. **Swap it with the smallest larger number on its right** (the successor).
> 3. **Reverse everything after it** to make the result as small as possible.

---

#### Visual Trace

```text
nums = [1, 2, 5, 4, 3]

Step 1 — Find pivot (right-to-left, first nums[i] < nums[i+1]):
  i=3: nums[3]=4, nums[4]=3  → 4 >= 3, skip
  i=2: nums[2]=5, nums[3]=4  → 5 >= 4, skip
  i=1: nums[1]=2, nums[2]=5  → 2 < 5  ✓ pivot = index 1, value 2

Step 2 — Find successor (right-to-left, first nums[j] > nums[pivot]):
  j=4: nums[4]=3 > 2  ✓ successor = index 4, value 3

Step 3 — Swap pivot and successor:
  [1, 2, 5, 4, 3]  →  [1, 3, 5, 4, 2]
      ^        ^
      i        j

Step 4 — Reverse suffix nums[2:]:
  [1, 3, 5, 4, 2]  →  [1, 3, 2, 4, 5]
         -------            -------

Result: [1, 3, 2, 4, 5]
```

---

#### Pattern (Python)

```python
# python
# LC 31 - Next Permutation
# time = O(N), space = O(1)
def nextPermutation(nums):
    n = len(nums)
    i = n - 2

    # Step 1: find pivot
    while i >= 0 and nums[i] >= nums[i + 1]:
        i -= 1

    # Step 2 & 3: find successor and swap
    if i >= 0:
        j = n - 1
        while nums[j] <= nums[i]:
            j -= 1
        nums[i], nums[j] = nums[j], nums[i]

    # Step 4: reverse suffix
    left, right = i + 1, n - 1
    while left < right:
        nums[left], nums[right] = nums[right], nums[left]
        left += 1
        right -= 1
```

#### Pattern (Java)

```java
// java
// LC 31 - Next Permutation
// time = O(N), space = O(1)
public void nextPermutation(int[] nums) {
    int n = nums.length;
    int i = n - 2;

    // Step 1: find pivot (right-to-left, first ascending pair)
    while (i >= 0 && nums[i] >= nums[i + 1]) i--;

    // Step 2 & 3: find successor and swap
    if (i >= 0) {
        int j = n - 1;
        while (nums[j] <= nums[i]) j--;
        int tmp = nums[i]; nums[i] = nums[j]; nums[j] = tmp;
    }

    // Step 4: reverse suffix (descending → ascending)
    int l = i + 1, r = n - 1;
    while (l < r) {
        int tmp = nums[l]; nums[l] = nums[r]; nums[r] = tmp;
        l++; r--;
    }
}
```

#### Algorithm Steps Summary

| Step | Action | Condition | Effect |
|------|--------|-----------|--------|
| Find pivot | Scan right-to-left | Stop at first `nums[i] < nums[i+1]` | Marks leftmost improvable position |
| No pivot found | `i == -1` | Whole array descending | Reverse entire array (wrap to first permutation) |
| Find successor | Scan right-to-left from end | First `nums[j] > nums[i]` | Smallest value that beats pivot |
| Swap | `nums[i], nums[j]` | — | Minimum increment at position `i` |
| Reverse suffix | `nums[i+1:]` | Always | Descending → ascending = smallest tail |

#### Similar Problems

| Problem | LC# | Key Pattern |
|---------|-----|-------------|
| Next Permutation | 31 | Pivot + successor + reverse suffix |
| Previous Permutation with One Swap | 1053 | Scan left for first descending pair, swap with rightmost smaller |
| Next Greater Element III | 556 | Same algorithm on integer digits (with overflow check) |
| Permutation Sequence | 60 | Build Kth permutation directly using factorial number system |
| Permutations | 46 | Generate all permutations (backtracking) |
| Permutations II | 47 | All permutations with duplicates (backtracking + dedup) |
| Find the Next Palindrome | 3348 | Permutation-style digit manipulation |


#### Alternative (Python) — forward scan for the pivot

```python
# LC 31. Next Permutation
# V0'
class Solution(object):
    def nextPermutation(self, num):
        k, l = -1, 0
        for i in range(len(num) - 1):
            if num[i] < num[i + 1]:
                k = i

        if k == -1:
            num.reverse()
            return

        for i in range(k + 1, len(num)):
            if num[i] > num[k]:
                l = i
        num[k], num[l] = num[l], num[k]
        num[k + 1:] = num[:k:-1] ### double check here ###
```

### DI String Match — LC 942

#### Core Idea

Reconstruct a permutation of `[0, n]` from a `"I"`/`"D"` string. Keep two pointers over the **available value range**:

- `low = 0` (smallest unused value), `high = n` (largest unused value)
- On `"I"` (next must be **larger**) → append `low`, then `low++`
- On `"D"` (next must be **smaller**) → append `high`, then `high--`
- After the loop `low == high` → append that final survivor

**Why it's always valid:** picking `low` for `"I"` guarantees whatever comes next is bigger (all remaining values are `> low`); picking `high` for `"D"` guarantees whatever comes next is smaller. We never "use up" a value we needed, so any greedy choice produces one valid answer.

```text
Pointer roles:
  low  — smallest value not yet placed (consumed on "I")
  high — largest value not yet placed  (consumed on "D")

Invariant: after k chars processed, exactly (n+1) - k values remain,
           and they are the contiguous range [low, high].
           The final leftover (low == high) fills the last slot.
```

---

#### Visual Trace

```text
s = "IDID"   →   n = 4,  low = 0, high = 4

| Step  | char | Action          | ans         | low | high |
| ----- | ---- | --------------- | ----------- | --- | ---- |
| start | -    | -               | []          | 0   | 4    |
| i=0   | I    | append low  (0) | [0]         | 1   | 4    |
| i=1   | D    | append high (4) | [0,4]       | 1   | 3    |
| i=2   | I    | append low  (1) | [0,4,1]     | 2   | 3    |
| i=3   | D    | append high (3) | [0,4,1,3]   | 2   | 2    |
| end   | -    | append low  (2) | [0,4,1,3,2] | 2   | 2    |

Result: [0, 4, 1, 3, 2]
```

---

#### Pattern (Python)

```python
# python
# LC 942 - DI String Match
# IDEA: converging low/high pointers over range [0, n]
# time = O(N), space = O(N) for output (O(1) extra)
def diStringMatch(s):
    low, high = 0, len(s)
    ans = []
    for c in s:
        if c == "I":
            ans.append(low)   # next value will be larger
            low += 1
        else:                 # c == "D"
            ans.append(high)  # next value will be smaller
            high -= 1
    ans.append(low)           # low == high: last remaining value
    return ans
```

#### Pattern (Java)

```java
// java
// LC 942 - DI String Match
// IDEA: converging low/high pointers over range [0, n]
// time = O(N), space = O(N) for output (O(1) extra)
public int[] diStringMatch(String s) {
    int n = s.length();
    int low = 0, high = n;
    int[] ans = new int[n + 1];
    for (int i = 0; i < n; i++) {
        if (s.charAt(i) == 'I') {
            ans[i] = low++;   // next value will be larger
        } else {              // 'D'
            ans[i] = high--;  // next value will be smaller
        }
    }
    ans[n] = low;             // low == high: last remaining value
    return ans;
}
```

#### Similar Problems

| Problem | LC# | Key Pattern |
|---------|-----|-------------|
| DI String Match | 942 | Greedy: `"I"`→low, `"D"`→high, converge inward |
| Next Permutation | 31 | Pivot + successor + reverse suffix |
| Valid Permutations for DI Sequence | 903 | Count (not construct) DI permutations via DP |
| Score After Flipping Matrix | 861 | Greedy per-position optimal choice |


### 3Sum — LC 15

**Pattern: Two Pointers with Fixed First Element**
- Fix first element, use two pointers for remaining two
- Avoid duplicates by skipping same values
- Sort array first

```java
// java
// LC 15. 3Sum
/**
 * Pattern: Fixed element + Two pointers
 *
 * Steps:
 *   1. Sort array
 *   2. Fix first element (i)
 *   3. Use two pointers (l, r) to find remaining two elements
 *   4. Skip duplicates
 *
 * Example:
 *   nums = [-1,0,1,2,-1,-4]
 *   After sort: [-4,-1,-1,0,1,2]
 *
 *   i=0, nums[i]=-4, l=1, r=5
 *   [-4,-1,-1,0,1,2]
 *     i  l       r    sum=-4+-1+2=-3 < 0, l++
 *
 *   i=1, nums[i]=-1, l=2, r=5
 *   [-4,-1,-1,0,1,2]
 *        i  l     r   sum=-1+-1+2=0, found! [-1,-1,2]
 *                     l++, r--, skip duplicates
 *
 *   [-4,-1,-1,0,1,2]
 *        i    l r     sum=-1+0+1=0, found! [-1,0,1]
 *
 * Time: O(N^2), Space: O(1) excluding result
 */
public List<List<Integer>> threeSum(int[] nums) {
    List<List<Integer>> result = new ArrayList<>();
    Arrays.sort(nums);

    for (int i = 0; i < nums.length - 2; i++) {
        // Skip duplicates for first element
        if (i > 0 && nums[i] == nums[i - 1]) {
            continue;
        }

        int left = i + 1;
        int right = nums.length - 1;
        int target = -nums[i];

        while (left < right) {
            int sum = nums[left] + nums[right];

            if (sum == target) {
                result.add(Arrays.asList(nums[i], nums[left], nums[right]));

                // Skip duplicates for second element
                while (left < right && nums[left] == nums[left + 1]) {
                    left++;
                }
                // Skip duplicates for third element
                while (left < right && nums[right] == nums[right - 1]) {
                    right--;
                }

                left++;
                right--;
            } else if (sum < target) {
                left++;
            } else {
                right--;
            }
        }
    }

    return result;
}
```

**Similar Problems:**
- LC 15 3Sum (this pattern)
- LC 16 3Sum Closest
- LC 18 4Sum
- LC 259 3Sum Smaller
- LC 1 Two Sum


### 3Sum Closest — LC 16

#### Core Idea

**Sort + Fix-One + Two-Pointer Squeeze:**
- Sort the array so the two-pointer direction is deterministic
- Fix the first element at index `i` (outer loop `i = 0..n-3`)
- For the remaining sub-array, set `l = i+1`, `r = n-1` and squeeze inward
- At each step compute `sum = nums[i] + nums[l] + nums[r]` and update `closest` when `|sum - target| < |closest - target|`
- Exact match → return immediately (can't do better)
- `sum > target` → `r--`  (reduce sum, need smaller right value)
- `sum < target` → `l++`  (increase sum, need larger left value)

```text
Key invariant:
  closest always holds the best (minimum-distance) sum seen so far
  
Pointer movement:
  i   — fixed anchor, advances each outer iteration
  l   — moves right when sum is too small
  r   — moves left when sum is too large
```

---

```java
// java
// LC 16 - 3Sum Closest
// time: O(N^2), space: O(1)
/**
 * Dry run: nums = [-1, 2, 1, -4], target = 1
 * After sort: [-4, -1, 1, 2]
 *
 * ==================================================================
 * | i | l | r | sum              | |sum-1| | closest | action      |
 * ==================================================================
 * | 0 | 1 | 3 | -4 + -1 + 2 = -3 |   4    |  -3     | l++         |
 * | 0 | 2 | 3 | -4 +  1 + 2 = -1 |   2    |  -1     | l++         |
 * | 0 | 3 | 3 | l >= r, inner loop ends                            |
 * | 1 | 2 | 3 | -1 +  1 + 2 =  2 |   1    |   2     | r-- (>1)    |
 * | 1 | 2 | 2 | l >= r, inner loop ends                            |
 * | 2 | 3 | 3 | l >= r, inner loop ends                            |
 * ==================================================================
 * return closest = 2
 */
public int threeSumClosest(int[] nums, int target) {
    Arrays.sort(nums);

    // initialise with first possible triplet
    int closest = nums[0] + nums[1] + nums[2];

    /** NOTE !!!
     *  outer loop ends at nums.length - 2
     *  (need at least 2 elements after i for l and r)
     */
    for (int i = 0; i < nums.length - 2; i++) {

        /** NOTE !!!
         *  l = i + 1
         *  r = last index
         */
        int l = i + 1;
        int r = nums.length - 1;

        while (l < r) {
            int sum = nums[i] + nums[l] + nums[r];

            // update closest if this sum is nearer to target
            if (Math.abs(sum - target) < Math.abs(closest - target)) {
                closest = sum;
            }

            if (sum == target) {
                return sum;           // exact match — can't improve
            } else if (sum > target) {
                r--;                  // need a smaller sum
            } else {
                l++;                  // need a larger sum
            }
        }
    }

    return closest;
}
```

#### Pattern vs 3Sum (LC 15)

| Aspect | 3Sum (LC 15) | 3Sum Closest (LC 16) |
|--------|-------------|----------------------|
| **Goal** | All triplets summing to 0 | Single triplet closest to `target` |
| **Track** | Result list | `closest` scalar |
| **On exact match** | Record & skip duplicates | Return immediately |
| **Duplicate skip** | Required (avoid repeated triplets) | Optional (problem guarantees unique answer) |
| **Return** | `List<List<Integer>>` | `int` |

#### Similar Problems

| Problem | LC# | Key Difference |
|---------|-----|----------------|
| 3Sum | 15 | Sum == 0 exactly; collect all triplets |
| **3Sum Closest** | **16** | **Closest sum to arbitrary target** |
| 3Sum Smaller | 259 | Count triplets with sum < target |
| 4Sum | 18 | Four elements; add one more fixed outer loop |
| Two Sum II | 167 | Two elements, sorted array |
| Two Sum (closest) | — | Two-pointer variant of this pattern |


### Trapping Rain Water — LC 42
```python
# LC 42. Trapping Rain Water
# NOTE : there is also 2 scan, dp approaches
# V0'
# IDEA : TWO POINTERS 
# IDEA : CORE
#     -> step 1) use left_max, right_mex : record "highest" "wall" in left, right handside at current idx
#     -> step 2) 
#                case 2-1) if height[left] < height[right] : 
#                   -> all left passed idx's height is LOWER than height[right]
#                   -> so the "short" wall MUST on left
#                   -> and since we record left_max, so we can get trap amount based on left_max, height[left]
#                
#                case 2-2) if height[left] > height[right]
#                   -> .... (similar as above)
class Solution:
    def trap(self, height):
 
        if not height:
            return 0

        left_max = right_max = res = 0
        left, right = 0, len(height) - 1
 
        while left < right:
            if height[left] < height[right]:  # left pointer op
                if height[left] < left_max:
                    res += left_max - height[left]
                else:
                    left_max = height[left]
                left += 1  # move left pointer 
            else:
                if height[right] < right_max:  # right pointer op
                    res += right_max - height[right]
                else:
                    right_max = height[right]
                right -= 1  # move right pointer 
        return res
```


### Longest Consecutive Sequence — LC 128
```python
# LC 128 Longest Consecutive Sequence

# V0
# IDEA : sliding window
class Solution(object):
    def longestConsecutive(self, nums):
        # edge case
        if not nums:
            return 0
        nums = list(set(nums))
        # if len(nums) == 1: # not necessary
        #     return 1
        # sort first
        nums.sort()
        res = 0
        l = 0
        r = 1
        """
        NOTE !!!

        Sliding window here :
            condition :  l, r are still in list (r < len(nums) and l < len(nums))

            2 cases

                case 1) nums[r] != nums[r-1] + 1
                    -> means not continous, 
                        -> so we need to move r to right (1 idx)
                        -> and MOVE l to r - 1, since it's NOT possible to have any continous subarray within [l, r] anymore
                case 2) nums[r] == nums[r-1] + 1
                        -> means there is continous subarray currently, so we keep moving r to right (r+=1) and get current max sub array length (res = max(res, r-l+1))
        """
        while r < len(nums) and l < len(nums):
            # case 1)
            if nums[r] != nums[r-1] + 1:
                r += 1
                l = (r-1)
            # case 2)
            else:
                res = max(res, r-l+1)
                r += 1
        # edge case : if res == 0, means no continous array (with len > 1), so we return 1 (a single alphabet can be recognized as a "continous assay", and its len = 1)
        return res if res > 1 else 1

# V0'
# IDEA : SORTING + 2 POINTERS
class Solution(object):
    def longestConsecutive(self, nums):
        # edge case
        if not nums:
            return 0

        nums.sort()
        cur_len = 1
        max_len = 1
        #print ("nums = " + str(nums))

        # NOTE : start from idx = 1
        for i in range(1, len(nums)):
            ### NOTE : start from nums[i] != nums[i-1] case
            if nums[i] != nums[i-1]:
                ### NOTE : if nums[i] == nums[i-1]+1 : cur_len += 1
                if nums[i] == nums[i-1]+1:
                    cur_len += 1
                ### NOTE : if nums[i] != nums[i-1]+1 : get max len, and reset cur_lent as 1
                else:
                    max_len = max(max_len, cur_len)
                    cur_len = 1
        # check max len again
        return max(max_len, cur_len)
```


### Reverse String / Reverse Words — LC 344

**Pattern: In-place Reversal with Two Pointers**

```java
// java
// LC 344. Reverse String
/**
 * Pattern: Swap from both ends moving toward center
 *
 * Example:
 *   s = ['h','e','l','l','o']
 *
 *   ['h','e','l','l','o']
 *     l           r       swap, l++, r--
 *
 *   ['o','e','l','l','h']
 *       l       r         swap, l++, r--
 *
 *   ['o','l','l','e','h']
 *           l r           l >= r, done!
 *
 * Time: O(N), Space: O(1)
 */
public void reverseString(char[] s) {
    int left = 0;
    int right = s.length - 1;

    while (left < right) {
        char temp = s[left];
        s[left] = s[right];
        s[right] = temp;
        left++;
        right--;
    }
}
```

**Similar Problems:**
- LC 344 Reverse String
- LC 345 Reverse Vowels of a String
- LC 541 Reverse String II
- LC 186 Reverse Words in a String II
- LC 151 Reverse Words in a String


## Expand from Centre

> Template: [Template 3 — Expand from Centre](./2_pointers.md#template-3-expand-from-centre-lc-5-lc-647).

### Longest Palindromic Substring — LC 5
```python
# LC 005 Longest Palindromic Substring
# V0
# IDEA : TWO POINTERS
# -> DEAL WITH odd, even len cases
#  -> step 1) for loop on idx 
#  -> step 2) and start from "center" 
#  -> step 3) and do a while loop
#  -> step 4) check if len of sub str > 1
# https://leetcode.com/problems/longest-palindromic-substring/discuss/1025355/Easy-to-understand-solution-with-O(n2)-time-complexity
# Time complexity = best case O(n) to worse case O(n^2)
# Space complexity = O(1) if not considering the space complexity for result, as all the comparison happens in place.
class Solution:
    # The logic I have used is very simple, iterate over each character in the array and assming that its the center of a palindrome step in either direction to see how far you can go by keeping the property of palindrome true. The trick is that the palindrome can be of odd or even length and in each case the center will be different.
    # For odd length palindrome i am considering the index being iterating on is the center, thereby also catching the scenario of a palindrome with a length of 1.
    # For even length palindrome I am considering the index being iterating over and the next element on the left is the center.
    def longestPalindrome(self, s):

        if len(s) <= 1:
            return s

        res = []

        for idx in range(len(s)):
        
            """
            # CASE 1) : odd len
            # Check for odd length palindrome with idx at its center

            -> NOTE : the only difference (between odd, even len)
            
            -> NOTE !!!  : 2 idx : left = right = idx
            """
            left = right = idx
            # note the condition !!!
            while left >= 0 and right < len(s) and s[left] == s[right]:
                if right - left + 1 > len(res):
                    res = s[left:right + 1]
                left -= 1
                right += 1
              
            """"
            # CASE 2) : even len  
            # Check for even length palindrome with idx and idx-1 as its center

            -> NOTE : the only difference (between odd, even len)

            -> NOTE !!!  : 2 idx : left = idx - 1,  right = idx
            """
            left = idx - 1
            right = idx
            # note the condition !!!
            while left >= 0 and right < len(s) and s[left] == s[right]:
                if right - left + 1 > len(res):
                    res = s[left:right + 1]
                left -= 1
                right += 1

        return res

# V0'
# IDEA : TWO POINTER + RECURSION
# https://leetcode.com/problems/longest-palindromic-substring/discuss/1057629/Python.-Super-simple-and-easy-understanding-solution.-O(n2).
class Solution:
    def longestPalindrome(self, s):
        res = ""
        length = len(s)
        def helper(left, right):
            while left >= 0 and right < length and s[left] == s[right]:
                left -= 1
                right += 1      
            return s[left + 1 : right]
        
        for index in range(len(s)):
            res = max(helper(index, index), helper(index, index + 1), res, key = len)           
        return res
```


### Palindromic Substrings — LC 647
```python
# LC 647. Palindromic Substrings
# V0'
# IDEA : TWO POINTERS
# https://leetcode.com/problems/palindromic-substrings/discuss/1041760/Python-Easy-Solution-Beats-85
# https://github.com/yennanliu/CS_basics/blob/master/leetcode_python/String/longest-palindromic-substring.py
class Solution:
    def countSubstrings(self, s):
        ans = 0    
        for i in range(len(s)):
            # odd
            ans += self.helper(s, i, i)
            # even
            ans += self.helper(s, i, i + 1)  
        return ans
        
    def helper(self, s, l, r):     
        ans = 0    
        while l >= 0 and r < len(s) and s[l] == s[r]:
            l -= 1
            r += 1
            ans += 1          
        return ans

# V0
# IDEA : BRUTE FORCE
class Solution(object):
    def countSubstrings(self, s):
        count = 0
        # NOTE: since i from 0 to len(s) - 1, so for j we need to "+1" then can get go throgh all elements in str
        for i in range(len(s)):
            # Note : for j we need to "+1"
            for j in range(i+1, len(s)+1):
                if s[i:j] == s[i:j][::-1]:
                    count += 1
        return count
```

### Valid Palindrome II — LC 680

**Pattern: Two Pointers with Mismatch Handling**
- Check palindrome from both ends
- On first mismatch, try TWO possibilities:
  1. Skip left character (check `s[l+1...r]`)
  2. Skip right character (check `s[l...r-1]`)
- If EITHER works, return true
- Use helper function to check palindrome in range

**Key Insight:**
- Don't remove character and create new string (O(N) space)
- Instead, use pointers to check substring in-place (O(1) space)

```java
// java
// LC 680. Valid Palindrome II
/**
 * Pattern: Palindrome with at most 1 deletion allowed
 *
 * Example:
 *   s = "abca"
 *
 *   [a b c a]    l=0, r=3, s[l]=a, s[r]=a, match! l++, r--
 *    l     r
 *
 *   [a b c a]    l=1, r=2, s[l]=b, s[r]=c, MISMATCH!
 *      l r       Try: skip b (check "ca") OR skip c (check "ba")
 *                     "ca" is NOT palindrome
 *                     "ba" is NOT palindrome
 *                BUT we need to check full substring!
 *
 *   Actually for "abca":
 *   - Try skip l: check "cba" -> isPali("abca", 2, 3) = true (just "a")
 *   - OR skip r: check "aba" -> isPali("abca", 1, 2) = true (just "b")
 *
 *   Either works -> return true
 *
 * Time: O(N), Space: O(1)
 */
public boolean validPalindrome(String s) {
    int l = 0;
    int r = s.length() - 1;

    while (l < r) {
        if (s.charAt(l) != s.charAt(r)) {
            /** NOTE !!!
             *
             *  On mismatch, try BOTH possibilities:
             *    1. Skip left char  -> check s[l+1...r]
             *    2. Skip right char -> check s[l...r-1]
             *
             *  If EITHER is palindrome, we can make it work with 1 deletion
             */
            return isPalindrome(s, l + 1, r) || isPalindrome(s, l, r - 1);
        }
        l++;
        r--;
    }

    return true; // Already a perfect palindrome
}

/** NOTE !!!
 *
 *  Helper function with left, right pointers as parameters
 *  Checks if substring s[l...r] is palindrome
 *  NO new string created - check in place!
 */
private boolean isPalindrome(String s, int l, int r) {
    while (l < r) {
        if (s.charAt(l) != s.charAt(r)) {
            return false;
        }
        l++;
        r--;
    }
    return true;
}
```

```python
# python
# LC 680. Valid Palindrome II
class Solution:
    def validPalindrome(self, s):

        l, r = 0, len(s) - 1

        while l < r:
            if s[l] != s[r]:
                """
                # NOTE this !!!!
                -> On mismatch, try skipping left OR right character
                -> Check if either resulting substring is palindrome
                """
                skip_left = s[l+1:r+1]   # skip s[l]
                skip_right = s[l:r]      # skip s[r]
                # NOTE this !!!!
                return skip_left == skip_left[::-1] or skip_right == skip_right[::-1]
            else:
                l += 1
                r -= 1

        return True
```

**Common Mistakes:**
- ❌ Creating new strings (O(N) space and time)
- ❌ Only trying to skip one side
- ✅ Use helper with pointers (O(1) space)
- ✅ Try BOTH skip possibilities

**Similar Problems:**
- LC 680 Valid Palindrome II (this pattern)
- LC 125 Valid Palindrome
- LC 1216 Valid Palindrome III (k deletions allowed - DP)
- LC 234 Palindrome Linked List


### Longest Mountain in Array — LC 845

**Core Idea:**

Find a valid **peak** (a local maximum where both neighbors are strictly smaller), then **expand left and right** from that peak to find the full mountain base. This is different from the "expand from center" palindrome pattern: here you first validate the peak, then walk outward along strictly monotone slopes.

**Key Optimization — `i = right` skip:**

After fully processing a mountain, jump `i` directly to `right` (the right base). Without this, the outer loop would re-examine every index on the descending slope — giving O(N²). With it, no index is ever revisited, so total work across all mountains is O(N).

```text
Without skip: outer loop backtracks over already-visited slope indices → O(N²)
With i = right: outer loop only ever moves forward → amortized O(N)
```

**Pattern (Java — find-peak + expand-left/right):**

```java
// LC 845 - Longest Mountain in Array
// IDEA: For each valid peak, expand left and right; skip i to right base
// time = O(N), space = O(1)
public int longestMountain(int[] arr) {
    if (arr == null || arr.length < 3) return 0;
    int maxLen = 0, n = arr.length;

    for (int i = 1; i < n - 1; i++) {
        // Step 1: Check for a valid peak (strictly greater than both neighbors)
        if (arr[i] > arr[i - 1] && arr[i] > arr[i + 1]) {

            // Step 2: Expand LEFT — walk back while strictly increasing
            int left = i - 1;
            while (left > 0 && arr[left] > arr[left - 1]) left--;

            // Step 3: Expand RIGHT — walk forward while strictly decreasing
            int right = i + 1;
            while (right < n - 1 && arr[right] > arr[right + 1]) right++;

            // Step 4: Record length
            maxLen = Math.max(maxLen, right - left + 1);

            /** NOTE !!!
             *  Skip i to the right base to avoid re-scanning the descending slope.
             *  Without this, time complexity degrades to O(N²).
             *  With this, each element is visited at most twice → O(N).
             */
            i = right;
        }
    }
    return maxLen;
}
```

**Dry-run — `arr = [2,1,4,7,3,2,5]`:**

```text
i=1: arr[1]=1, 1 > 2? No → skip
i=2: arr[2]=4, 4 > 1 && 4 > 7? No → skip
i=3: arr[3]=7, 7 > 4 && 7 > 3? YES → peak found
       expand left:  left=2 → left=1 (arr[1]=1 < arr[2]=4, stop)
       expand right: right=4 → right=5 (arr[5]=2 < arr[4]=3, stop; arr[6]=5 > arr[5]=2, STOP)
       len = right(5) - left(1) + 1 = 5  → maxLen = 5
       i = right = 5  ← SKIP over the descending slope

i=6: arr[6]=5, 5 > arr[5]=2 but 5 > arr[7]? out of bounds → skip (i=6 = n-2, loop ends)

Result: 5  (mountain = [1,4,7,3,2])
```

**Alternative — `while` loop (explicit base tracking, V1 pattern):**

```java
// Scan from base; find ascending slope, peak, then descending slope in one pass
// time = O(N), space = O(1)
public int longestMountain_v1(int[] A) {
    int N = A.length, ans = 0, base = 0;
    while (base < N) {
        int end = base;
        if (end + 1 < N && A[end] < A[end + 1]) {      // found left slope
            while (end + 1 < N && A[end] < A[end + 1]) end++;  // climb to peak
            if (end + 1 < N && A[end] > A[end + 1]) {  // confirmed peak
                while (end + 1 < N && A[end] > A[end + 1]) end++; // descend
                ans = Math.max(ans, end - base + 1);
            }
        }
        base = Math.max(end, base + 1);  // advance base past processed segment
    }
    return ans;
}
```

**Alternative — precompute two arrays `up[]` / `down[]` (prefix/suffix slopes):**

Instead of expanding from each peak, precompute at every index how far the strictly-increasing run reaches from the left and the strictly-decreasing run reaches to the right. Any index where **both** are non-zero is a peak, and its mountain length is `up[i] + down[i] + 1`. This trades O(1) space for O(N) space but is often the most intuitive to reason about.

- `up[i]`   = length of the increasing run **ending** at `i`   (built left → right)
- `down[i]` = length of the decreasing run **starting** at `i` (built right → left)
- Valid peak ⇔ `up[i] > 0 && down[i] > 0`

```python
# python
# LC 845 - Longest Mountain in Array
# IDEA: precompute up[] (left slope) and down[] (right slope), combine at peaks
# time = O(N), space = O(N)
class Solution(object):
    def longestMountain(self, arr):
        n = len(arr)
        if n < 3:
            return 0

        up = [0] * n    # up[i]   = length of increasing run ending at i
        down = [0] * n  # down[i] = length of decreasing run starting at i

        for i in range(1, n):            # build left slopes
            if arr[i] > arr[i - 1]:
                up[i] = up[i - 1] + 1

        for i in range(n - 2, -1, -1):   # build right slopes
            if arr[i] > arr[i + 1]:
                down[i] = down[i + 1] + 1

        ans = 0
        for i in range(n):
            if up[i] and down[i]:        # both slopes present → valid peak
                ans = max(ans, up[i] + down[i] + 1)
        return ans
```

```java
// java
// LC 845 - Longest Mountain in Array
// time = O(N), space = O(N)
public int longestMountain(int[] arr) {
    int n = arr.length;
    if (n < 3) return 0;

    int[] up = new int[n];    // increasing run ending at i
    int[] down = new int[n];  // decreasing run starting at i

    for (int i = 1; i < n; i++)
        if (arr[i] > arr[i - 1]) up[i] = up[i - 1] + 1;

    for (int i = n - 2; i >= 0; i--)
        if (arr[i] > arr[i + 1]) down[i] = down[i + 1] + 1;

    int ans = 0;
    for (int i = 0; i < n; i++)
        if (up[i] > 0 && down[i] > 0)
            ans = Math.max(ans, up[i] + down[i] + 1);
    return ans;
}
```

**Comparison of approaches:**

| Approach | Core pointer | Skip trick | When to use |
|----------|-------------|------------|-------------|
| Peak + expand (V0) | `i` scans for peaks; `left`/`right` expand | `i = right` after each mountain | Clearest structure |
| Base + climb (V1) | `base` tracks mountain start | `base = max(end, base+1)` | Single-pass, no look-around |
| Two arrays `up[]`/`down[]` (V2) | precomputed slope lengths per index | none — direct combine at peaks | Most intuitive; O(N) space |

**Invariant for a valid mountain:**
1. Peak must not be at index 0 or n-1
2. At least one element strictly ascending on the left
3. At least one element strictly descending on the right
4. No flat segments (strict inequality required on both slopes)

**Similar LC problems:**

| Problem | LC# | Key Pattern |
|---------|-----|-------------|
| Longest Mountain in Array | 845 | Find peak → expand left/right → skip to right base |
| Valid Mountain Array | 941 | Single pass: go up then down, verify full coverage |
| Peak Index in Mountain Array | 852 | Binary search for peak in guaranteed mountain |
| Find Peak Element | 162 | Binary search: always move toward higher neighbor |
| Trapping Rain Water | 42 | Left/right expansion + height tracking |
| Longest Palindromic Substring | 5 | Expand from center (symmetric version of this pattern) |
| Count of Subarrays with Score less than K | 2302 | Slope expansion with sum tracking |

---

### Shortest Palindrome — LC 214

**Pattern: Scan from right, track left pointer to find longest palindromic prefix**

**Core Idea:**
- Find the longest prefix of `s` that is already a palindrome
- Reverse the remaining suffix and prepend it to `s`
- Use two pointers: `j` anchored at 0 (left), `i` scans right-to-left
- When `s[i] == s[j]`, advance `j` — after the scan `s[0..j-1]` is the matched prefix
- If `j < n`, recurse on `s[0..j]` and sandwich the non-palindrome suffix around it

**Dry Run — `s = "aacecaaa"`:**
```text
i scans right-to-left, j starts at 0

i=7 s[7]='a' == s[0]='a'  -> j=1
i=6 s[6]='a' == s[1]='a'  -> j=2
i=5 s[5]='a' != s[2]='c'  -> skip
i=4 s[4]='c' == s[2]='c'  -> j=3
i=3 s[3]='e' == s[3]='e'  -> j=4
i=2 s[2]='c' == s[4]='c'  -> j=5
i=1 s[1]='a' == s[5]='a'  -> j=6
i=0 s[0]='a' == s[6]='a'  -> j=7

j == n? No (j=7 < 8). suffix = s.substring(7) = "a"
reversed("a") + shortestPalindrome("aacecaa") + "a"
```

**Key Insight — why does `j` track the prefix?**
```text
Scanning i from right to left acts like a "sieve":
- Every time s[i] matches s[j], j advances one step right
- After the full scan, s[0..j-1] is the longest possible palindromic prefix
  (not a strict palindrome proof, but works with the recursive structure)
- The characters NOT in the prefix (s[j..n-1]) form the suffix that
  must be reversed and prepended to make the whole string a palindrome
```

```java
// java
// LC 214. Shortest Palindrome
/**
 * Pattern: Find longest palindromic prefix via right-to-left scan
 *
 * Step 1: Scan i from n-1 to 0, advance j when s[i] == s[j]
 * Step 2: j is now the length of the "matched" prefix
 * Step 3: suffix  = s.substring(j)        (non-palindrome tail)
 *         prefix  = reverse(suffix)        (chars to prepend)
 * Step 4: return prefix + shortestPalindrome(s[0..j]) + suffix
 *
 * Time: O(N^2) average (O(N) per recursion level, O(N) depth)
 * Space: O(N) recursion stack
 *
 * Example 1: s = "aacecaaa" -> "aaacecaaa"
 * Example 2: s = "abcd"     -> "dcbabcd"
 */
public String shortestPalindrome(String s) {
    if (s == null || s.length() <= 1) return s;

    int j = 0;

    /** NOTE !!!
     *  Scan from the RIGHT end toward left.
     *  j tracks how far into s we've "matched" from the front.
     */
    for (int i = s.length() - 1; i >= 0; i--) {
        if (s.charAt(i) == s.charAt(j)) {
            j++;
        }
    }

    // Whole string is already a palindrome
    if (j == s.length()) return s;

    // suffix is the part NOT covered by the palindromic prefix
    String suffix = s.substring(j);
    String prefix = new StringBuilder(suffix).reverse().toString();

    /** NOTE !!!
     *  Recurse on s[0..j] to handle the inner part,
     *  then sandwich the current suffix around it.
     */
    return prefix + shortestPalindrome(s.substring(0, j)) + suffix;
}
```

```java
// java
// LC 214. Shortest Palindrome — KMP approach (O(N) time)
/**
 * IDEA: KMP Prefix Table
 *
 * Combine s + "#" + reverse(s) into one string.
 * The KMP prefix table's last value gives the length of the
 * longest palindromic prefix of s.
 *
 * Time: O(N), Space: O(N)
 */
public String shortestPalindromeKMP(String s) {
    String rev = new StringBuilder(s).reverse().toString();
    String combined = s + "#" + rev;
    int[] table = buildPrefixTable(combined);

    int palindromeLen = table[combined.length() - 1];
    String suffix = new StringBuilder(s.substring(palindromeLen)).reverse().toString();
    return suffix + s;
}

private int[] buildPrefixTable(String s) {
    int[] table = new int[s.length()];
    int len = 0;
    for (int i = 1; i < s.length(); i++) {
        while (len > 0 && s.charAt(i) != s.charAt(len))
            len = table[len - 1];
        if (s.charAt(i) == s.charAt(len))
            len++;
        table[i] = len;
    }
    return table;
}
```

**Brute-force version (TLE on large inputs):**
```java
// java — O(N^2) brute force
// Find largest i such that s[0..i] is a palindrome, then prepend reverse(s[i+1..n-1])
public String shortestPalindromeBrute(String s) {
    int n = s.length();
    if (n <= 1) return s;
    int end = 0;
    for (int i = n - 1; i >= 0; i--) {
        if (isPalindrome(s, 0, i)) { end = i; break; }
    }
    String suffix = s.substring(end + 1);
    return new StringBuilder(suffix).reverse() + s;
}

private boolean isPalindrome(String s, int l, int r) {
    while (l < r) {
        if (s.charAt(l++) != s.charAt(r--)) return false;
    }
    return true;
}
```

**Pointer Movement Comparison:**

| Approach | Left pointer `j` | Right pointer `i` | When `j` advances |
|----------|-----------------|-------------------|-------------------|
| Right-to-left scan | Anchored at 0, moves right | Scans n-1 → 0 | `s[i] == s[j]` |
| Brute force isPalindrome | Expands from both ends | Starts at n-1, decrements | Always (matching chars) |
| KMP | N/A — uses prefix table | N/A | N/A |

**Similar Problems:**
- LC 214 Shortest Palindrome (this pattern)
- LC 5 Longest Palindromic Substring (expand from center)
- LC 647 Palindromic Substrings (expand from center)
- LC 680 Valid Palindrome II (skip one char)
- LC 516 Longest Palindromic Subsequence (DP)
- LC 132 Palindrome Partitioning II (DP + palindrome check)
- LC 336 Palindrome Pairs (hash map + palindrome prefix/suffix)


## Subsequence & Two-String Matching

> Template: [Template 4 — Subsequence Matching](./2_pointers.md#template-4-subsequence-matching-one-pointer-always-moves-lc-392).

### Longest Word in Dictionary through Deleting — LC 524 ⭐⭐⭐⭐

**Core Idea**

> LC 524 is **LC 392 (Is Subsequence) run once per dictionary word**, wrapped in a
> "keep the best valid word so far" layer. The two-pointer subsequence check is
> identical — the only new part is the **tie-break rule** when picking the winner.

Given `s` and a `dictionary`, return the longest word that is a **subsequence** of `s`.
On a length tie, return the **smallest lexicographical** one.

For each `word`:
1. **Subsequence check** (same as LC 392): scan `s` with pointer `i`, advance `word`
   pointer `j` only on a match. `j == len(word)` at the end ⇒ `word` is a subsequence.
2. **Candidate selection**: keep `word` if it beats the current best on `(length, lexicographic)`.

```text
s = "abpcplea",  word = "apple"

 a b p c p l e a       i=0 j=0  s[i]=a == a → j=1, i=1
 i,j
 a b p c p l e a       i=1 j=1  s[i]=b != p → i=2
   i j
 a b p c p l e a       i=2 j=1  s[i]=p == p → j=2, i=3
     i j
 ...                   → eventually j == 5 == len("apple") ✅ subsequence
```

**Pattern — 2 pointers + string comparison (`word < res`)**

```python
# python
# LC 524 Longest Word in Dictionary through Deleting
# V0 — IDEA: 2 POINTERS + string comparison (word < res)
# time = O(d * (n + l)), space = O(1)   (d = #words, n = len(s), l = word len)
class Solution(object):
    def findLongestWord(self, s, dictionary):
        res = ""

        for word in dictionary:
            i = 0   # pointer for s (main string)
            j = 0   # pointer for word (target subsequence)

            # ---- LC 392 subsequence check: always move i, move j on match ----
            while i < len(s) and j < len(word):
                if s[i] == word[j]:
                    j += 1
                i += 1

            # whole word matched (word is a subsequence of s)
            if j == len(word):
                # NOTE !!! tie-break rule:
                #   longer wins; on equal length, smaller lexicographic wins
                if len(word) > len(res):
                    res = word
                elif len(word) == len(res) and word < res:
                    res = word   # `word < res` → true string lexicographic compare

        return res
```

> **NOTE on `word < res`**: Python compares strings **lexicographically** (dictionary
> order) out of the box — `"apple" < "apply"` is `True`. This one line replaces an
> explicit char-by-char comparison. See
> [python_trick.md](https://github.com/yennanliu/CS_basics/blob/master/doc/cheatsheet/python_trick.md).

**Alternative — sort first, return the first match (no tie-break logic)**

```python
# python
# V0-1 — IDEA: SORT (len DESC, lexicographic ASC) + 2 pointers
# time = O(d log d + d * n), space = O(1)
class Solution(object):
    def findLongestWord(self, s, dictionary):
        # longest first; ties broken by lexicographic ascending
        dictionary.sort(key=lambda x: (-len(x), x))
        for word in dictionary:
            i = j = 0
            while i < len(s) and j < len(word):
                if s[i] == word[j]:
                    j += 1
                i += 1
            if j == len(word):
                return word   # first fit IS the answer (sorted order guarantees it)
        return ""
```

**Two ways to handle the "longest + smallest lexicographic" requirement**

| Approach | How the winner is chosen | Trade-off |
|----------|--------------------------|-----------|
| **Scan + compare** (`V0`) | keep best via `(len(word) > len(res))` and `word < res` | no sort — `O(1)` extra, but tie-break logic inline |
| **Sort + first-fit** (`V0-1`) | `sort(key=lambda x: (-len(x), x))`, return first subsequence | cleaner loop, but `O(d log d)` sort up front |

> **Pitfall**: don't `return word` on the first subsequence found **without sorting** —
> a later word may be longer or (same length) lexicographically smaller. Either sort
> first (V0-1) *or* compare against the running best (V0), never mix them.

**Similar Problems**

| Problem | LC# | Relation to LC 524 |
|---------|-----|--------------------|
| Is Subsequence | 392 | the inner check — is one string a subsequence of another |
| Longest Word through Deleting | **524** | LC 392 per word + longest/lexicographic tie-break |
| Number of Matching Subsequences | 792 | count how many words are subsequences (bucket by next-char for scale) |
| Shortest Way to Form String | 1055 | greedy repeated subsequence matching |
| Append Characters to Make Subsequence | 2486 | single subsequence scan, count leftover chars |

### One Edit Distance — LC 161

**Core Idea:**
Check whether two strings differ by exactly one edit (insert, delete, or replace).

Key observations:
1. If `|len(s) - len(t)| > 1` → impossible, return false
2. If `s == t` → zero edits, return false
3. Always work with `s` as the shorter string (swap if needed)
4. Scan left-to-right: on the **first mismatch**, try the only possible operation and verify the remainder in O(1) with `substring.equals()`

**Three cases at first mismatch:**

| Lengths | Operation | Check |
|---------|-----------|-------|
| `len(s) == len(t)` | Replace `s[i]` | `s[i+1..] == t[i+1..]` |
| `len(s) < len(t)` | Insert into s (skip t[i]) | `s[i..] == t[i+1..]` |
| `len(s) > len(t)` | Delete from s (skip s[i]) | `s[i+1..] == t[i..]` |

After the loop (no mismatch found): valid only if `len(t) == len(s) + 1` (one trailing insert).

**Pattern (Java):**
```java
// LC 161 - One Edit Distance
public boolean isOneEditDistance(String s, String t) {
    int ns = s.length(), nt = t.length();

    // Ensure s is always the shorter string
    if (ns > nt) return isOneEditDistance(t, s);

    // Length gap > 1 → impossible
    if (nt - ns > 1) return false;

    for (int i = 0; i < ns; i++) {
        if (s.charAt(i) != t.charAt(i)) {
            if (ns == nt) {
                // Replace: rest of both strings must match
                return s.substring(i + 1).equals(t.substring(i + 1));
            } else {
                // Insert into s (skip one char in t)
                return s.substring(i).equals(t.substring(i + 1));
            }
        }
    }

    // No mismatch in s — valid only if t has exactly one extra trailing char
    return ns + 1 == nt;
}
```

**Why `substring` comparison instead of continuing the loop?**
Once we find the first mismatch, there is only ONE valid repair move. Checking the suffix via `substring.equals()` resolves this in O(n) without needing extra flags or pointer bookkeeping.

**Pointer movement summary:**
```text
Both i and j advance together while chars match.
At FIRST mismatch:
  - Same length  → advance both (replace): check suffix
  - Diff length  → advance j only (insert): check suffix
No second chance — any further mismatch = false.
```

**Similar LC problems:**
| Problem | LC# | Key Difference |
|---------|-----|----------------|
| One Edit Distance | 161 | Exactly 1 edit (insert/delete/replace) |
| Edit Distance | 72 | Minimum edits (DP) |
| Is Subsequence | 392 | Deletions only, any count |
| Longest Common Subsequence | 1143 | Max common chars (DP) |
| Valid Palindrome II | 680 | At most 1 delete to form palindrome |

### Camelcase Matching — LC 1023

```java
// java
// LC 1023 Camelcase Matching
// https://leetcode.com/problems/camelcase-matching/

/**
 * Pattern: Subsequence matching with character type validation
 *
 * Key Idea:
 *   - Similar to subsequence matching, but with EXTRA CONSTRAINT
 *   - Use two pointers: i for query, j for pattern
 *   - ALWAYS move i (scan through entire query)
 *   - ONLY move j when characters match
 *   - CRITICAL: Any non-matching character in query MUST be lowercase
 *     (uppercase non-match = invalid)
 *
 * Core Logic:
 *   1. All pattern characters must appear in query in same order (subsequence)
 *   2. Any extra characters in query MUST be lowercase
 *   3. If we encounter an extra uppercase letter → immediate failure
 *
 * Example 1:
 *   query = "FooBar", pattern = "FB"
 *
 *   [F o o B a r]    i=0, j=0, query[i]=F, pattern[j]=F, match! j++
 *    i j
 *
 *   [F o o B a r]    i=1, j=1, query[i]=o, pattern[j]=B, no match
 *      i j           but 'o' is lowercase → OK, i++
 *
 *   [F o o B a r]    i=2, j=1, query[i]=o, pattern[j]=B, no match
 *        i j         but 'o' is lowercase → OK, i++
 *
 *   [F o o B a r]    i=3, j=1, query[i]=B, pattern[j]=B, match! j++
 *          i j
 *
 *   [F o o B a r]    i=4, j=2, query[i]=a, pattern[j]=none, no match
 *            i       but 'a' is lowercase → OK, i++
 *
 *   [F o o B a r]    i=5, j=2, query[i]=r, pattern[j]=none, no match
 *              i     but 'r' is lowercase → OK, i++
 *
 *   j == pattern.length() → return true
 *
 * Example 2:
 *   query = "FooBarTest", pattern = "FB"
 *
 *   ... (matches F, o, o, B, a, r) ...
 *
 *   [F o o B a r T e s t]    i=6, j=2, query[i]=T
 *                  i         'T' is UPPERCASE but not in pattern
 *                            → return false immediately!
 *
 * Pointer Behavior:
 *   - i (Explorer): Moves EVERY step, scans all characters
 *   - j (Goal Tracker): ONLY moves when finding matching character
 *   - Safety Check: Non-matching uppercase → instant failure
 *
 * Time: O(M) where M = query length
 * Space: O(1)
 */
public List<Boolean> camelMatch(String[] queries, String pattern) {
    List<Boolean> result = new ArrayList<>();

    for (String query : queries) {
        result.add(isMatch(query, pattern));
    }

    return result;
}

private boolean isMatch(String query, String pattern) {
    /** NOTE !!!
     *
     *  Two pointers:
     *    i: query pointer (always moves)
     *    j: pattern pointer (conditionally moves)
     */
    int i = 0; // Pointer for query
    int j = 0; // Pointer for pattern

    while (i < query.length()) {
        char qChar = query.charAt(i);

        /** NOTE !!!
         *
         *  Three cases:
         *
         *  Case 1: Characters match
         *    → Move both pointers
         *
         *  Case 2: Characters don't match AND query char is lowercase
         *    → OK! This is allowed insertion, move i only
         *
         *  Case 3: Characters don't match AND query char is UPPERCASE
         *    → FAIL! Extra uppercase not allowed
         */

        // Case 1: If characters match, move the pattern pointer
        if (j < pattern.length() && qChar == pattern.charAt(j)) {
            j++;
        }
        // Case 3: If characters don't match, the extra character MUST be lowercase
        else if (Character.isUpperCase(qChar)) {
            return false;
        }
        // Case 2: Lowercase character that doesn't match → skip it

        // Always move the query pointer
        i++;
    }

    // Match is only valid if we successfully navigated through the entire pattern
    return j == pattern.length();
}
```

```python
# python
# LC 1023 Camelcase Matching

def camelMatch(queries, pattern):
    """
    Pattern: Subsequence with character type constraints

    Core Trick:
      - query pointer ALWAYS moves (explorer)
      - pattern pointer ONLY moves on match (goal tracker)
      - Extra validation: non-matching chars MUST be lowercase

    Example:
      query = "FooBar", pattern = "FB"

      'F' == 'F' → match, j++
      'o' != 'B' → but lowercase, OK
      'o' != 'B' → but lowercase, OK
      'B' == 'B' → match, j++
      'a' (no pattern) → but lowercase, OK
      'r' (no pattern) → but lowercase, OK

      j reached end → True
    """
    result = []

    for query in queries:
        i, j = 0, 0
        is_valid = True

        while i < len(query):
            # Case 1: Match found
            if j < len(pattern) and query[i] == pattern[j]:
                j += 1
            # Case 2: Uppercase non-match → fail
            elif query[i].isupper():
                is_valid = False
                break
            # Case 3: Lowercase non-match → skip

            i += 1

        # Valid only if all pattern chars matched
        result.append(is_valid and j == len(pattern))

    return result
```

**Key Differences from Standard Subsequence:**

| Aspect | Subsequence (LC 392) | CamelCase Matching (LC 1023) |
|--------|---------------------|------------------------------|
| **Pattern** | Any subsequence | Subsequence with type constraint |
| **Non-match chars** | Ignored | MUST be lowercase |
| **Uppercase non-match** | Ignored | **Instant failure** |
| **Use case** | General matching | Identifier/name matching |

**Visualization:**

```text
Pattern = "FB"

Query 1: "FooBar"
  F → match ✓
  o → lowercase non-match ✓
  o → lowercase non-match ✓
  B → match ✓
  a → lowercase non-match ✓
  r → lowercase non-match ✓
  Result: TRUE

Query 2: "FooBarTest"
  F → match ✓
  o → lowercase non-match ✓
  o → lowercase non-match ✓
  B → match ✓
  a → lowercase non-match ✓
  r → lowercase non-match ✓
  T → UPPERCASE non-match ✗ FAIL!
  Result: FALSE
```

**Pointer Movement Rules:**

1. **i (Query Explorer):**
   - Moves forward **EVERY step**
   - Scans every character in query
   - Never goes backward

2. **j (Pattern Goal Tracker):**
   - **ONLY moves** when finding matching character
   - If `j == pattern.length()`, all pattern chars found

3. **Safety Check:**
   - Non-matching uppercase → **immediate return false**
   - Non-matching lowercase → **continue** (allowed insertion)

**Classic Problems:**
- LC 1023 Camelcase Matching (this pattern)
- LC 392 Is Subsequence (simpler version)
- LC 524 Longest Word in Dictionary through Deleting
- LC 792 Number of Matching Subsequences

### Expressive Words — LC 809

#### Core Idea

Compare two strings **group by group**, where a group is a maximal run of the same character. For each aligned group:
1. Characters must match
2. The source group count `cntS` must be **≥** query group count `cntW` (can only expand, not shrink)
3. If counts differ (`cntS != cntW`), `cntS` must be **≥ 3** — otherwise the source can't have been "extended" from the query

```text
Key invariant:
  cntS < cntW          → impossible (word has more chars than s)
  cntS != cntW && cntS < 3  → impossible (s has too few to be an extension)
  otherwise            → valid group match
```

Both pointers must reach the end of their strings simultaneously.

---

```java
// java
// LC 809 - Expressive Words
// time: O(S + W) per word, O(N * (S + W)) total
// space: O(1)
/**
 * Example:
 *   s = "heeellooo", word = "hello"
 *
 *   Group 'h': cntS=1, cntW=1  → equal, OK
 *   Group 'e': cntS=3, cntW=1  → differ, but cntS=3 >= 3, OK (extended)
 *   Group 'l': cntS=2, cntW=2  → equal, OK
 *   Group 'o': cntS=3, cntW=1  → differ, but cntS=3 >= 3, OK (extended)
 *   Both exhausted → true (stretchy)
 *
 *   s = "heeellooo", word = "helo"
 *   Group 'l': cntS=2, cntW=1  → differ, but cntS=2 < 3, FAIL
 */
public int expressiveWords(String s, String[] words) {
    int cnt = 0;
    for (String word : words) {
        if (isStretchy(s, word)) cnt++;
    }
    return cnt;
}

private boolean isStretchy(String s, String word) {
    int i = 0, j = 0;

    while (i < s.length() && j < word.length()) {
        if (s.charAt(i) != word.charAt(j)) return false;

        char ch = s.charAt(i);

        // count group in s
        int cntS = 0;
        while (i < s.length() && s.charAt(i) == ch) { cntS++; i++; }

        // count group in word
        int cntW = 0;
        while (j < word.length() && word.charAt(j) == ch) { cntW++; j++; }

        if (cntS < cntW) return false;              // word has more than s → can't shrink
        if (cntS != cntW && cntS < 3) return false; // extension requires group size >= 3
    }

    return i == s.length() && j == word.length();
}
```

#### Decision Table Per Group

| `cntS` vs `cntW` | `cntS >= 3`? | Result |
|-----------------|-------------|--------|
| `cntS == cntW` | — | Valid (exact match) |
| `cntS < cntW` | — | **Invalid** (s is shorter) |
| `cntS > cntW` | Yes (>= 3) | Valid (extended) |
| `cntS > cntW` | No (< 3) | **Invalid** (can't extend a small group) |

#### Similar Problems

| Problem | LC# | Key Difference |
|---------|-----|----------------|
| Expressive Words | 809 | Multi-word: count stretchy words |
| String Compression | 443 | Encode groups as `char + count` in-place |
| Count and Say | 38 | Generate next sequence by reading groups |
| Consecutive Characters | 1446 | Find longest single-char run |
| Run-Length Encoding | — | Encode/decode character groups |


### Encode and Decode Strings — LC 271

**Pattern: Parse a length header, then jump `i` forward by the declared length**

The key idea: encode each string as `len(s) + "#" + s`. Decoding uses two pointers (`i`, `j`) where:
- `i` points to the **start of the length header** at each iteration
- `j` scans forward from `i` until it hits `"#"`, revealing the word length
- After extracting the word, `i` jumps directly to `j + 1 + length` (the next header)

This differs from normal two-pointer patterns because the jump distance is **variable** and **encoded in the string itself** — no fixed window size.

```text
Pointer roles:
  i  — "header start": marks the beginning of each encoded block
  j  — "separator finder": scans forward until s[j] == "#"

Per-iteration flow:
  1. j starts at i, advances until s[j] == "#"
  2. length = int(s[i:j])         ← word length from header
  3. word   = s[j+1 : j+1+length] ← extract word
  4. i      = j + 1 + length      ← jump to next block's header
```

```python
# python
# LC 271 - Encode and Decode Strings

class Codec:

    def encode(self, strs):
        # time: O(N), space: O(N)
        res = ""
        for s in strs:
            res += str(len(s)) + "#" + s
        return res

    def decode(self, s):
        # time: O(N), space: O(N)
        if not s:
            return []

        res = []
        i = 0

        while i < len(s):
            j = i

            # NOTE: j scans right until it hits "#"
            while s[j] != "#":
                j += 1

            # NOTE: everything between i and j is the length header
            length = int(s[i:j])

            # NOTE: extract exactly `length` chars after the "#"
            word = s[j + 1 : j + 1 + length]
            res.append(word)

            # NOTE: jump i to the start of the next block
            i = j + 1 + length

        return res
```

**Dry Run — `strs = ["Hello", "World"]`:**

```text
encode → "5#Hello5#World"

decode:
  i=0: j scans → s[1]="#", length=5, word="Hello", i=7
  i=7: j scans → s[8]="#", length=5, word="World", i=14
  i=14: loop ends

result: ["Hello", "World"]
```

**Why `#` as separator works safely:**
- The length header tells exactly how many bytes to read — so even if the word contains `#`, `j+1+length` jumps past it correctly
- The only `#` that matters is the **first** one after `i` (which `j` finds by scanning)

**Alternative: `str.find("#", i)` — same O(N), slightly cleaner:**
```python
def decode(self, s):
    res = []
    i = 0
    while i < len(s):
        sep = s.find("#", i)        # find first "#" from position i
        length = int(s[i:sep])
        res.append(s[sep + 1 : sep + 1 + length])
        i = sep + 1 + length
    return res
```

**Similar Problems:**
- LC 271 Encode and Decode Strings (this pattern)
- LC 297 Serialize and Deserialize Binary Tree (variable-length encoding of tree nodes)
- LC 449 Serialize and Deserialize BST

---


### Compare Version Numbers — LC 165 ⭐⭐⭐⭐

#### Core Idea

**Two independent pointers walking two different strings, each consuming one "chunk" per round.**

Unlike `l`/`r` on one array, here `i` walks `version1` and `j` walks `version2`, and each
loop iteration parses **one revision number** from each side.

Two traps this template solves for free:
1. **Different lengths** — `"1.0"` vs `"1.0.0.0"`. Loop while `i < n1 **||** j < n2`
   (OR, not AND). An exhausted side simply yields `0` → **implicit zero padding**.
2. **Leading zeros** — `"1.01"` vs `"1.001"`. Building the number with
   `a = a * 10 + digit` turns both into `1` → no string comparison needed.

```text
Per round:
  parse int a from version1 until '.' or end
  parse int b from version2 until '.' or end
  a != b  -> return -1 / 1 immediately
  a == b  -> skip the '.' on both sides (i++, j++) and continue
Loop ends with all chunks equal -> return 0
```

---

#### Pattern (Java)

```java
// java
// LC 165 - Compare Version Numbers
// IDEA: 2 pointers (one per string); parse one revision per round; missing side = 0
// time = O(N1 + N2), space = O(1)
public int compareVersion(String version1, String version2) {
    int i = 0, j = 0;
    int n1 = version1.length(), n2 = version2.length();

    /** NOTE !!!
     *  condition is `||` (OR) — keep going while EITHER side has chunks left,
     *  so the exhausted side contributes 0 (implicit padding)
     */
    while (i < n1 || j < n2) {
        int a = 0, b = 0;

        // parse one revision from version1
        while (i < n1 && version1.charAt(i) != '.') {
            a = a * 10 + (version1.charAt(i) - '0'); // leading zeros vanish here
            i++;
        }
        // parse one revision from version2
        while (j < n2 && version2.charAt(j) != '.') {
            b = b * 10 + (version2.charAt(j) - '0');
            j++;
        }

        if (a != b) {
            return a < b ? -1 : 1;
        }

        i++; // skip '.' (harmless if already past the end)
        j++;
    }
    return 0; // all revisions equal
}
```

#### Pattern (Python)

```python
# python
# LC 165 - Compare Version Numbers
# IDEA: 2 pointers (one per string); parse one revision per round; missing side = 0
# time = O(N1 + N2), space = O(1)
class Solution(object):
    def compareVersion(self, version1, version2):
        i, j = 0, 0
        n1, n2 = len(version1), len(version2)

        # NOTE !!! `or` -> the exhausted side keeps yielding 0
        while i < n1 or j < n2:
            a = b = 0

            while i < n1 and version1[i] != ".":
                a = a * 10 + int(version1[i])
                i += 1

            while j < n2 and version2[j] != ".":
                b = b * 10 + int(version2[j])
                j += 1

            if a != b:
                return -1 if a < b else 1

            i += 1  # skip "."
            j += 1

        return 0
```

> **Python shortcut**: `v1 = list(map(int, version1.split(".")))` then pad with zeros —
> shorter, but O(N) extra space. The two-pointer version is the O(1)-space answer
> interviewers usually push for.

#### Variation — Verifying an Alien Dictionary (LC 953)

> **Twist**: same "walk two strings in lockstep" scan, but the comparison uses a
> **custom alphabet rank** and the loop is run over every *adjacent pair* of words.
> The "shorter string is a prefix" rule replaces the implicit-zero padding rule.

```java
// java
// LC 953 - Verifying an Alien Dictionary
// IDEA: rank[] for custom order; lockstep 2-pointer compare on each adjacent word pair
// time = O(total chars), space = O(1)
public boolean isAlienSorted(String[] words, String order) {
    int[] rank = new int[26];
    for (int i = 0; i < order.length(); i++) {
        rank[order.charAt(i) - 'a'] = i;
    }

    for (int k = 0; k + 1 < words.length; k++) {
        String w1 = words[k], w2 = words[k + 1];
        int i = 0, j = 0;
        boolean decided = false; // did a differing char settle the order?

        while (i < w1.length() && j < w2.length()) {
            char c1 = w1.charAt(i), c2 = w2.charAt(j);
            if (c1 != c2) {
                if (rank[c1 - 'a'] > rank[c2 - 'a']) return false; // out of order
                decided = true;
                break;
            }
            i++;
            j++;
        }

        /** NOTE !!!
         *  no differing char -> one word is a PREFIX of the other
         *  -> the longer one must NOT come first ("apple" before "app" is invalid)
         */
        if (!decided && w1.length() > w2.length()) return false;
    }
    return true;
}
```

```python
# python
# LC 953 - Verifying an Alien Dictionary
# time = O(total chars), space = O(1)
class Solution(object):
    def isAlienSorted(self, words, order):
        rank = {c: i for i, c in enumerate(order)}

        for w1, w2 in zip(words, words[1:]):
            i, j = 0, 0
            decided = False

            while i < len(w1) and j < len(w2):
                if w1[i] != w2[j]:
                    if rank[w1[i]] > rank[w2[j]]:
                        return False
                    decided = True
                    break
                i += 1
                j += 1

            # prefix rule: longer word must not come first
            if not decided and len(w1) > len(w2):
                return False

        return True
```

#### Two-strings-in-lockstep family

| Problem | LC# | What each pointer consumes | Tie / exhaustion rule |
|---------|-----|----------------------------|------------------------|
| Compare Version Numbers | 165 | one `.`-separated integer | missing side = `0` |
| Verifying an Alien Dictionary | 953 | one character (custom rank) | shorter must be the prefix |
| Longest Common Prefix | 14 | one character across ALL strings | stop at first mismatch / shortest word |
| Backspace String Compare | 844 | one *effective* char (scanning backwards) | both must exhaust together |
| Merge Sorted Array | 88 | one element from each array | flush the leftover side |
| Interval List Intersections | 986 | one interval from each list | advance the list that ends first |

---


## Pointers over Intervals, Matrices and Partitions

> Templates: [Template 5 — Merge Two Sorted Arrays](./2_pointers.md#template-5-merge-two-sorted-arrays-fill-from-the-back-lc-88) and [Template 6 — Three-Way Partition](./2_pointers.md#template-6-three-way-partition-dutch-national-flag-lc-75).

### Interval List Intersections — LC 986 ⭐⭐⭐⭐

#### Core Idea

Two sorted, pairwise-disjoint interval lists. One pointer per list (`i`, `j`), each
pointing at the "currently active" interval. At every step we ask **two** questions:

1. **Do `firstList[i]` and `secondList[j]` overlap?**
   -> intersection is `[max(s1, s2), min(e1, e2)]`, valid iff `max(start) <= min(end)`

2. **Which pointer moves?**
   -> **ALWAYS advance the interval that ENDS FIRST** (the critical trick)

**Why "ends first" is correct**: the interval with the smaller end can never reach any
*later* interval in the other list (those all start after the current one, since the
lists are sorted & disjoint). So it is fully consumed — discarding it loses nothing.

**Why we DON'T need prev-interval bookkeeping** (unlike LC 56 / LC 57):
the interval that ends *later* **stays put**, so it is automatically re-compared against
the next interval of the other list. One interval can therefore produce *multiple*
intersections without us ever looking backward.

```text
firstList  = [[13,23],[24,25]]
secondList = [[15,24],[25,26]]
-> [[15,23],[24,24],[25,25]]
             ^^^^^^^  [15,24] survives after matching [13,23], so it also hits [24,25]
```

**Overlap test — two equivalent forms**:

```python
# form A: direct
if max(s1, s2) <= min(e1, e2): ...

# form B: negate the ONLY 2 non-overlap cases
#   case 1)  |---|            case 2)        |----|
#                 |----|            |---|
if not (e1 < s2 or s1 > e2): ...
```

**Complexity**: time `O(m + n)` — each pointer moves forward only; space `O(1)` extra
(output not counted). Note we can never "skip" a valid intersection: both pointers
together advance exactly `m + n` times.

#### Visual Trace

```text
firstList  = [[0,2],[5,10],[13,23],[24,25]]
secondList = [[1,5],[8,12],[15,24],[25,26]]

i j  first[i]  second[j]  [max(s), min(e)]  emit?      move (ends first)
------------------------------------------------------------------------
0 0  [0,2]     [1,5]      [1, 2]            [1,2]      e1=2  < e2=5   -> i++
1 0  [5,10]    [1,5]      [5, 5]            [5,5]      e2=5  < e1=10  -> j++
1 1  [5,10]    [8,12]     [8, 10]           [8,10]     e1=10 < e2=12  -> i++
2 1  [13,23]   [8,12]     [13, 12]          x (13>12)  e2=12 < e1=23  -> j++
2 2  [13,23]   [15,24]    [15, 23]          [15,23]    e1=23 < e2=24  -> i++
3 2  [24,25]   [15,24]    [24, 24]          [24,24]    e2=24 < e1=25  -> j++
3 3  [24,25]   [25,26]    [25, 25]          [25,25]    e1=25 < e2=26  -> i++ -> i == m, stop

ans = [[1,2],[5,5],[8,10],[15,23],[24,24],[25,25]]
```

#### Pattern (Python)

```python
# python
# LC 986 - Interval List Intersections
# IDEA: 2 pointers over 2 sorted interval lists
# time = O(m + n), space = O(1) (excluding output)
class Solution(object):
    def intervalIntersection(self, firstList, secondList):
        # edge case: intersection with an empty list is always []
        if not firstList or not secondList:
            return []

        ans = []
        i, j = 0, 0
        len_f, len_s = len(firstList), len(secondList)

        # NOTE !!! loop while BOTH lists still have intervals
        while i < len_f and j < len_s:
            s1, e1 = firstList[i]
            s2, e2 = secondList[j]

            # 1) overlap ? -> [max(start), min(end)]
            start, end = max(s1, s2), min(e1, e2)
            if start <= end:               # or: if not (e1 < s2 or s1 > e2)
                ans.append([start, end])

            # 2) NOTE !!! CRITICAL: move ONLY the pointer that ENDS FIRST
            if e1 < e2:
                i += 1
            else:
                j += 1

        return ans
```

#### Pattern (Java)

```java
// java
// LC 986 - Interval List Intersections
// IDEA: 2 pointers over 2 sorted interval lists
// time = O(m + n), space = O(1) (excluding output)
public int[][] intervalIntersection(int[][] firstList, int[][] secondList) {
    if (firstList.length == 0 || secondList.length == 0)
        return new int[0][0];

    List<int[]> ans = new ArrayList<>();
    int i = 0, j = 0;

    while (i < firstList.length && j < secondList.length) {
        int startMax = Math.max(firstList[i][0], secondList[j][0]);
        int endMin   = Math.min(firstList[i][1], secondList[j][1]);

        // 1) overlap ?
        if (startMax <= endMin) {
            ans.add(new int[]{startMax, endMin});
        }

        // 2) advance the interval that ends first
        if (firstList[i][1] < secondList[j][1]) i++;
        else j++;
    }

    return ans.toArray(new int[ans.size()][2]);
}
```

> **Tie handling** (`e1 == e2`): both intervals are fully consumed, so two variants are
> both correct:
> - `if/else` (above) — moves one pointer; the next iteration compares the stale interval
>   against an interval that starts *after* `e1`, finds no overlap, and moves on. Costs one
>   wasted iteration, emits nothing extra.
> - `if (endMin == e1) i++; if (endMin == e2) j++;` (verbose version below) — moves **both**,
>   skipping that wasted step.
>
> The only wrong choice is advancing **neither** -> infinite loop.

#### Common Pitfalls

| Pitfall | Why it breaks | Fix |
|---------|---------------|-----|
| Advancing the interval that **starts** first | Discards an interval that may still intersect later ones | Advance by **end**, not start |
| Advancing both pointers unconditionally | Skips valid intersections (one interval can match many) | Only move the one that ends first |
| Moving neither on `e1 == e2` | Infinite loop | Move at least one pointer every iteration |
| Using `start < end` for the overlap test | Drops single-point intersections like `[5,5]` | Closed intervals -> `start <= end` |
| `while (i < m || j < n)` | Out-of-bounds; no pair left to intersect | `&&` — stop when either list is exhausted |
| Returning `firstList` when the other list is empty | Intersection with `[]` is `[]`, not the non-empty list | Early-return `[]` |

#### Similar Problems

| Problem | LC# | Key Difference |
|---------|-----|----------------|
| **Interval List Intersections** | **986** | **2 lists, 2 pointers; advance the earlier `end`** |
| Insert Interval | 57 | 1 list + 1 new interval; 3-phase pointer scan |
| Merge Intervals | 56 | 1 list; sort, then extend a running `end` (union, not intersection) |
| Non-overlapping Intervals | 435 | Greedy by `end` — same "earliest end wins" intuition |
| Meeting Rooms II | 253 | Sorted starts/ends as two pointers -> max concurrent overlap |
| Employee Free Time | 759 | Merge all intervals, then emit the **gaps** (complement) |
| Merge Sorted Array | 88 | Same 2-pointer merge, but on scalars instead of intervals |
| Intersection of Two Arrays II | 350 | Degenerate case: each "interval" is a single point |
| My Calendar I | 729 | Same overlap test `max(s) < min(e)`, on insert |
| Range Module | 715 | Add/remove/query ranges — interval intersection + merge combined |

#### Verbose reference version (with inline notes)

```java
// java
// LC 986
    public int[][] intervalIntersection_1(int[][] firstList, int[][] secondList) {
        if (firstList.length == 0 || secondList.length == 0)
            return new int[0][0];
    /**
     *  NOTE !!!!
     *   - i and j are pointers used to iterate through
     *      `firstList` and `secondList` respectively.
     *
     *   - `startMax` and `endMin` are used to compute
     *     the `intersection` of the current intervals
     *     from firstList and secondList.
     *
     *   - ans is a list to store the resulting intersection intervals.
     */
        int i = 0;
        int j = 0;
        int startMax = 0, endMin = 0;
        List<int[]> ans = new ArrayList<>();

    /**
     *
     *   - The loop continues as long as
     *      there are intervals remaining in `BOTH lists`.
     *
     *   - `startMax` is the maximum of the `START points` of the two
     *     intervals (firstList[i] and secondList[j]).
     *       -> This ensures the intersection starts no earlier than both intervals.
     *
     *   - `endMin` is the minimum of the `END points` of the two intervals.
     *
     *   - This ensures the intersection ends no later than the earlier of
     *     the two intervals.
     *
     */
    while (i < firstList.length && j < secondList.length) {
      startMax = Math.max(firstList[i][0], secondList[j][0]);
      endMin = Math.min(firstList[i][1], secondList[j][1]);

      // you have end greater than start and you already know that this interval is
      // surrounded with startMin and endMax so this must be the intersection
      /**
       *
       *  - If endMin >= startMax, it means there is an intersection between the two intervals.
       *    ->  Add the intersection [startMax, endMin] to the result list.
       */
      if (endMin >= startMax) {
        ans.add(new int[] {startMax, endMin});
      }

      // the interval with min end has been covered completely and have no chance to
      // intersect with any other interval so move that list's pointer
      /**
       * - Since the intervals are sorted and disjoint:
       *    - If the interval from firstList ends first (or at the same time), increment i.
       *    - If the interval from secondList ends first (or at the same time), increment j.
       *    -> This ensures that the interval which has been fully processed is skipped, moving to the next potential candidate for intersection.
       *
       */
      if (endMin == firstList[i][1]) i++;
      if (endMin == secondList[j][1]) j++;
        }

        return ans.toArray(new int[ans.size()][2]);
    }
```


### Insert Interval — LC 57

> **Variation of 2-12 (LC 986)**: instead of two pointers over *two lists*, a single
> pointer `i` sweeps one sorted list in **three phases** against ONE new interval.

#### Core Idea

`intervals` is sorted and non-overlapping. One forward pointer `i`, three phases — no
re-sorting needed:

| Phase | While condition | Action |
|-------|-----------------|--------|
| 1. **Before** | `intervals[i][1] < newInterval[0]` | copy as-is (ends before new starts) |
| 2. **Overlap** | `intervals[i][0] <= e` | merge: `s = min(s, start)`, `e = max(e, end)` |
| 3. **After** | remaining | copy as-is |

The merged interval `[s, e]` is emitted exactly once, between phase 2 and phase 3.

```text
intervals = [[1,2],[3,5],[6,7],[8,10],[12,16]], new = [4,8]

phase 1: [1,2] ends at 2 < 4          → copy       res = [[1,2]]
phase 2: [3,5] starts 3 <= 8          → s=3, e=8
         [6,7] starts 6 <= 8          → s=3, e=8
         [8,10] starts 8 <= 8         → s=3, e=10
         [12,16] starts 12 > 10       → stop
         emit [3,10]                  res = [[1,2],[3,10]]
phase 3: [12,16]                      → copy       res = [[1,2],[3,10],[12,16]]
```

```java
// java
// LC 57 - Insert Interval
// IDEA: single forward pointer, 3 phases: before / merge-overlap / after
// time = O(N), space = O(N) for output
public int[][] insert(int[][] intervals, int[] newInterval) {
    List<int[]> res = new ArrayList<>();
    int i = 0, n = intervals.length;

    // Phase 1: everything strictly BEFORE the new interval
    while (i < n && intervals[i][1] < newInterval[0]) {
        res.add(intervals[i]);
        i++;
    }

    // Phase 2: absorb every interval that OVERLAPS (start <= running end)
    int s = newInterval[0], e = newInterval[1];
    while (i < n && intervals[i][0] <= e) {
        s = Math.min(s, intervals[i][0]);
        e = Math.max(e, intervals[i][1]);
        i++;
    }
    res.add(new int[] { s, e });

    // Phase 3: everything strictly AFTER
    while (i < n) {
        res.add(intervals[i]);
        i++;
    }

    return res.toArray(new int[res.size()][]);
}
```

```python
# python
# LC 57 - Insert Interval
# IDEA: single forward pointer, 3 phases: before / merge-overlap / after
# time = O(N), space = O(N) for output
class Solution(object):
    def insert(self, intervals, newInterval):
        res = []
        i, n = 0, len(intervals)

        # Phase 1: before
        while i < n and intervals[i][1] < newInterval[0]:
            res.append(intervals[i])
            i += 1

        # Phase 2: merge all overlapping
        s, e = newInterval[0], newInterval[1]
        while i < n and intervals[i][0] <= e:
            s = min(s, intervals[i][0])
            e = max(e, intervals[i][1])
            i += 1
        res.append([s, e])

        # Phase 3: after
        while i < n:
            res.append(intervals[i])
            i += 1

        return res
```

> **Pitfall**: phase 1 uses `end < newStart` (strict) while phase 2 uses `start <= e`
> (inclusive) — touching intervals like `[1,3]` and `[3,5]` must MERGE, not stay apart.

#### Interval pointer family

| Problem | LC# | Pointer setup |
|---------|-----|---------------|
| Insert Interval | 57 | one list + one new interval → 3 phases |
| Merge Intervals | 56 | sort, then one pointer extending a running `end` |
| Interval List Intersections | 986 | two pointers over two sorted lists |
| Partition Labels | 763 | implicit intervals from last-occurrence table |

---

### Partition Labels — LC 763 ⭐⭐⭐⭐⭐

#### Core Idea

**Two pointers `start` / `end` where `end` is a moving "commitment" boundary.**

Cut a string into as many pieces as possible so that **each letter appears in at most one piece**.

1. **Pre-pass**: record `last[c]` = the last index where character `c` appears.
2. **Scan pass**: keep a window `[start, end]`. For every index `i`, the current piece is
   *forced* to extend at least to `last[s[i]]` → `end = max(end, last[s[i]])`.
3. When `i == end`, no character inside `[start, end]` reaches beyond `end` → **safe to cut**.
   Emit `end - start + 1`, then `start = i + 1`.

```text
Key invariant:
  end = the furthest index that ANY character seen since `start` still needs.
  i < end   → cannot cut yet (some letter still appears later)
  i == end  → the window is "closed" → cut here
```

**Why greedy is optimal**: cutting at the *first* index where `i == end` gives the shortest
possible valid piece, which leaves the maximum room for the remaining pieces.

---

#### Visual Trace

```text
s = "ababcbacadefegdehijhklij"
     0123456789...

last: a→8, b→5, c→7, d→14, e→15, f→11, g→13, h→19, i→22, j→23, k→20, l→21

i=0  'a' → end = max(0, 8) = 8
i=1  'b' → end = max(8, 5) = 8
i=2  'a' → end = 8
...
i=8  'a' → end = 8,  i == end  ✅ CUT → len = 8 - 0 + 1 = 9   ("ababcbaca")
                                       start = 9

i=9  'd' → end = 14
i=10 'e' → end = max(14, 15) = 15
i=11 'f' → end = 15
...
i=15 'e' → end = 15, i == end  ✅ CUT → len = 15 - 9 + 1 = 7  ("defegde")
                                       start = 16

i=16 'h' → end = 19
i=17 'i' → end = 22
...
i=23 'j' → end = 23, i == end  ✅ CUT → len = 23 - 16 + 1 = 8 ("hijhklij")

Result: [9, 7, 8]
```

---

#### Pattern (Java)

```java
// java
// LC 763 - Partition Labels
// IDEA: pre-compute each char's LAST index; scan with start/end, cut when i == end
// time = O(N), space = O(1)   (last[] is fixed size 26)
public List<Integer> partitionLabels(String s) {
    // Step 1: last occurrence index of every character
    int[] last = new int[26];
    for (int i = 0; i < s.length(); i++) {
        last[s.charAt(i) - 'a'] = i;
    }

    List<Integer> res = new ArrayList<>();
    int start = 0; // left boundary of current piece
    int end = 0;   // furthest index the current piece MUST reach

    for (int i = 0; i < s.length(); i++) {
        /** NOTE !!!
         *  the current piece is forced to cover this char's last occurrence
         */
        end = Math.max(end, last[s.charAt(i) - 'a']);

        // NOTE !!! i == end -> nothing inside reaches further -> safe cut
        if (i == end) {
            res.add(end - start + 1);
            start = i + 1;
        }
    }
    return res;
}
```

#### Pattern (Python)

```python
# python
# LC 763 - Partition Labels
# IDEA: pre-compute each char's LAST index; scan with start/end, cut when i == end
# time = O(N), space = O(1)   (at most 26 keys)
class Solution(object):
    def partitionLabels(self, s):
        # dict comprehension keeps the LAST index for each char
        last = {c: i for i, c in enumerate(s)}

        res = []
        start = end = 0

        for i, c in enumerate(s):
            # extend the piece to cover this char's last occurrence
            end = max(end, last[c])

            # NOTE !!! window closed -> cut
            if i == end:
                res.append(end - start + 1)
                start = i + 1

        return res
```

#### Comparison with the sliding-window family

| Aspect | Sliding Window (LC 3, 209) | Partition Labels (LC 763) |
|--------|----------------------------|---------------------------|
| **Left pointer** | shrinks on violation | only jumps AFTER a cut (`start = i + 1`) |
| **Right pointer** | scans one step per loop | `end` is a *max* over required reaches, not a scanner |
| **Cut condition** | window validity predicate | `i == end` (no pending char) |
| **Pre-pass needed** | no | yes — last-occurrence table |

#### Similar Problems

| Problem | LC# | Key Difference |
|---------|-----|----------------|
| Partition Labels | 763 | Each letter in exactly one piece — `i == end` cut |
| Merge Intervals | 56 | Same "extend end, close when gap" idea, on intervals |
| Jump Game II | 45 | `end` = current jump boundary; count jumps when `i == end` |
| Interval List Intersections | 986 | Two pointers over two interval lists |
| DI String Match | 942 | Greedy pointer consumption |

---


### Search a 2D Matrix II — LC 240 ⭐⭐⭐⭐

#### Core Idea

**Two pointers on a 2D grid: one row pointer + one column pointer, both monotone.**

Matrix is sorted **left→right in every row** and **top→bottom in every column**.
Start from a corner where the two sort directions *disagree* — the **top-right** corner:

- `matrix[r][c]` is the **largest** in its row and the **smallest** in its column
- `cur > target` → the whole column `c` below is too big → `c--` (drop a column)
- `cur < target` → the whole row `r` to the left is too small → `r++` (drop a row)
- `cur == target` → found

Each step eliminates an entire row or column → at most `m + n` steps.

```text
Why NOT the top-left corner?
  top-left is the minimum: both "go right" and "go down" increase the value
  → the comparison gives no information about which direction to drop.
  A valid start corner must be a "saddle": max in one direction, min in the other.
  → top-right (this template) or bottom-left (mirror: cur > target -> r--, else c++).
```

---

#### Visual Trace

```text
matrix = [[ 1, 4, 7,11,15],
          [ 2, 5, 8,12,19],
          [ 3, 6, 9,16,22],
          [10,13,14,17,24],
          [18,21,23,26,30]]
target = 5

r=0,c=4: 15 > 5  → c-- (column of 15,19,22,24,30 all too big)
r=0,c=3: 11 > 5  → c--
r=0,c=2:  7 > 5  → c--
r=0,c=1:  4 < 5  → r++ (row 0 left of col1 is all <= 4)
r=1,c=1:  5 == 5 → FOUND ✅

Path is a staircase: only left and down moves, never backtracks.
```

---

#### Pattern (Java)

```java
// java
// LC 240 - Search a 2D Matrix II
// IDEA: start at TOP-RIGHT; too big -> drop column (c--), too small -> drop row (r++)
// time = O(M + N), space = O(1)
public boolean searchMatrix(int[][] matrix, int target) {
    if (matrix == null || matrix.length == 0 || matrix[0].length == 0) {
        return false;
    }

    /** NOTE !!!  start from the TOP-RIGHT corner (the "saddle" point) */
    int r = 0;
    int c = matrix[0].length - 1;

    while (r < matrix.length && c >= 0) {
        int cur = matrix[r][c];

        if (cur == target) {
            return true;
        } else if (cur > target) {
            c--; // whole column below is >= cur > target
        } else {
            r++; // whole row left is <= cur < target
        }
    }
    return false;
}
```

#### Pattern (Python)

```python
# python
# LC 240 - Search a 2D Matrix II
# IDEA: start at TOP-RIGHT; too big -> c -= 1, too small -> r += 1
# time = O(M + N), space = O(1)
class Solution(object):
    def searchMatrix(self, matrix, target):
        if not matrix or not matrix[0]:
            return False

        # NOTE !!! top-right corner
        r, c = 0, len(matrix[0]) - 1

        while r < len(matrix) and c >= 0:
            cur = matrix[r][c]
            if cur == target:
                return True
            elif cur > target:
                c -= 1   # drop this column
            else:
                r += 1   # drop this row
        return False
```

#### Corner-choice table

| Start corner | `cur > target` | `cur < target` | Valid? |
|--------------|----------------|----------------|--------|
| **Top-right** | `c--` | `r++` | ✅ (this template) |
| **Bottom-left** | `r--` | `c++` | ✅ (mirror) |
| Top-left (min) | — | — | ❌ both directions increase |
| Bottom-right (max) | — | — | ❌ both directions decrease |

#### Similar Problems

| Problem | LC# | Key Difference |
|---------|-----|----------------|
| Search a 2D Matrix II | 240 | Rows AND columns sorted → staircase O(M+N) |
| Sort Colors | 75 | Pointers shrink a range instead of a grid |
| Container With Most Water | 11 | Same "drop the provably useless side" greedy |
| Two Sum II | 167 | 1D version: `l++` / `r--` on a sorted array |

---


### Sum of Subarray Ranges — LC 2104
```python
# LC 2104. Sum of Subarray Ranges
# V0
# IDEA : BRUTE FORCE
class Solution:
    def subArrayRanges(self, nums):
        res = 0
        for i in range(len(nums)):
            curMin = float("inf")
            curMax = -float("inf")
            for j in range(i, len(nums)):
                curMin = min(curMin, nums[j])
                curMax = max(curMax, nums[j])
                res += curMax - curMin
        return res

# V0'
# IDEA : INCREASING STACK
class Solution:
    def subArrayRanges(self, A0):
        res = 0
        inf = float('inf')
        A = [-inf] + A0 + [-inf]
        s = []
        for i, x in enumerate(A):
            while s and A[s[-1]] > x:
                j = s.pop()
                k = s[-1]
                res -= A[j] * (i - j) * (j - k)
            s.append(i)
            
        A = [inf] + A0 + [inf]
        s = []
        for i, x in enumerate(A):
            while s and A[s[-1]] < x:
                j = s.pop()
                k = s[-1]
                res += A[j] * (i - j) * (j - k)
            s.append(i)
        return res
```

