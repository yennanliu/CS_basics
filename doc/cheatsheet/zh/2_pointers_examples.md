# 雙指標 — 實例解析

> **範圍** — 雙指標的 LeetCode 實作題庫，每題每種語言各一份標準解，依它所套用的模板分組；概念、指標種類與模板本身留在雙指標主文件裡。
> **另見**：[2_pointers.md](./2_pointers.md) — 概念，以及每個範例所套用的標準模板；[2_pointers_quickselect.md](./2_pointers_quickselect.md) — 第 k 個元素的選擇，從同一份文件拆出來；[sliding_window.md](./sliding_window.md) — 由條件驅動的視窗；[n_sum.md](./n_sum.md) — 深入 k-sum 家族；[palindrome.md](./palindrome.md) — 深入回文家族。

## LeetCode 題目清單

- [Two Pointers](https://leetcode.com/problem-list/two-pointers/)
- [Array](https://leetcode.com/problem-list/array/)
- [String](https://leetcode.com/problem-list/string/)

## 總覽

一題一節。每題只出現一次，歸檔在它所套用的 [2_pointers.md](./2_pointers.md) 模板底下 — 先讀模板，再看範例。

### 題目索引

| 分組 | 題目 |
|---|---|
| [原地改寫（快慢指標）](#in-place-rewriting-fastslow) | LC 80、LC 283 |
| [向內收斂的雙向指標](#converging-bidirectional-pointers) | LC 15、LC 16、LC 31、LC 42、LC 128、LC 344、LC 942 |
| [從中心往外擴](#expand-from-centre) | LC 5、LC 214、LC 647、LC 680、LC 845 |
| [子序列與雙字串比對](#subsequence--two-string-matching) | LC 161、LC 165、LC 271、LC 524、LC 809、LC 953、LC 1023 |
| [在區間、矩陣與分割上的指標](#pointers-over-intervals-matrices-and-partitions) | LC 57、LC 240、LC 763、LC 986、LC 2104 |

## 原地改寫（快慢指標）

> 模板：[模板 1 — 快慢指標的讀寫壓縮](./2_pointers.md#template-1-fastslow-read-write-compaction--lc-26-lc-27-)。

### Remove Duplicates from Sorted Array II — LC 80

#### 核心想法

**「跟往回兩格的位置比」這個技巧：**
- 每個元素**最多留兩份** → 只在它跟 `nums[slow - 2]` 不同時才保留
- 因為陣列已排序，若 `nums[fast] == nums[slow - 2]`，寫進去就會產生第 3 個連續重複 → 跳過
- `slow` 和 `fast` 都從索引 2 開始（前兩個元素一定合法）

```text
Key condition: nums[fast] != nums[slow - 2]
  → write nums[fast] to nums[slow], slow++
  
Pointer initialization:
  slow = 2  (write pointer, first 2 slots are always valid)
  fast = 2  (read pointer, scans from index 2 onward)
```

**為什麼是 `slow - 2` 而不是 `slow - 1`？**
- `slow - 1` 只有在「最後寫入的兩個值相同」時，才擋得住第 3 份以上的重複
- `slow - 2` 直接檢查往回兩格的位置是不是已經放了同樣的值 — 保證最多只有 2 份

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

#### 一般化模式：最多允許 K 份重複

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

#### LC 26 vs LC 80 比較

| 面向 | LC 26（最多 1 份） | LC 80（最多 2 份） |
|--------|-------------------|-------------------|
| **條件** | `nums[fast] != nums[slow - 1]` | `nums[fast] != nums[slow - 2]` |
| **初始化** | `slow = 1, fast = 1` | `slow = 2, fast = 2` |
| **回傳** | `slow` | `slow` |
| **一般化** | `k = 1` | `k = 2` |

#### 相似題目

| 題目 | LC# | 關鍵差異 |
|---------|-----|----------------|
| Remove Duplicates I | 26 | 最多 1 份 — 比 `nums[slow-1]` |
| Remove Duplicates II | 80 | 最多 2 份 — 比 `nums[slow-2]` |
| Remove Element | 27 | 移除所有指定值 |
| Move Zeroes | 283 | 保留 0，但搬到尾端 |

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

**相似題目：**
- LC 283 Move Zeroes（本模式）
- LC 27 Remove Element（覆寫版）
- LC 905 Sort Array By Parity（把偶數搬到前面）
- LC 922 Sort Array By Parity II（奇偶交錯定位）
- LC 2460 Apply Operations to an Array
- LC 1089 Duplicate Zeros（陣列會膨脹的版本）


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

## 向內收斂的雙向指標

> 模板：[模板 2 — 向內收斂的雙向指標](./2_pointers.md#template-2-converging-bidirectional-pointers-)。

### Next Permutation — LC 31

#### 核心想法

**找樞紐 → 找後繼者 → 交換 → 反轉後綴：**

1. **找樞紐** — 由右往左掃，找出第一個滿足 `nums[i] < nums[i+1]` 的索引 `i`。依定義，後綴 `nums[i+1:]` 必定完全遞減。若找不到這樣的 `i`，整個陣列就是遞減 → 反轉後回傳。
2. **找後繼者** — 由右往左掃，找出第一個滿足 `nums[j] > nums[i]` 的索引 `j`。這是後綴中大於樞紐的最小值。
3. **交換** `nums[i]` 和 `nums[j]`。交換後後綴仍然是遞減的。
4. **反轉後綴** `nums[i+1:]` — 遞減變遞增，得到最小的尾巴。

```text
Key invariant:
  suffix after pivot is ALWAYS descending when we find the pivot.
  After the swap it's still descending (we swapped the smallest-greater element in).
  Reversing descending → ascending gives the smallest suffix.
```

**為什麼這樣是對的：**
- 樞紐是最右邊那個「還能把數字變大」的位置。
- 挑最小的後繼者，保證位置 `i` 的增幅最小。
- 反轉後綴，保證尾巴盡可能小。

**一句話口訣（3 個動作）：**
> 1. **找出第一個還能變大的數字**（樞紐）。
> 2. **把它跟右邊比它大的數字中最小的那個交換**（後繼者）。
> 3. **反轉它後面的全部**，讓結果盡可能小。

---

#### 視覺化追蹤

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

#### 模式（Python）

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

#### 模式（Java）

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

#### 演算法步驟總結

| 步驟 | 動作 | 條件 | 效果 |
|------|--------|-----------|--------|
| 找樞紐 | 由右往左掃 | 停在第一個 `nums[i] < nums[i+1]` | 標出最左邊可以變大的位置 |
| 找不到樞紐 | `i == -1` | 整個陣列遞減 | 反轉整個陣列（繞回第一個排列） |
| 找後繼者 | 從尾端由右往左掃 | 第一個 `nums[j] > nums[i]` | 大於樞紐的最小值 |
| 交換 | `nums[i], nums[j]` | — | 位置 `i` 的增幅最小 |
| 反轉後綴 | `nums[i+1:]` | 一律執行 | 遞減 → 遞增 = 最小的尾巴 |

#### 相似題目

| 題目 | LC# | 關鍵模式 |
|---------|-----|-------------|
| Next Permutation | 31 | 樞紐 + 後繼者 + 反轉後綴 |
| Previous Permutation with One Swap | 1053 | 往左找第一組遞減對，跟最右邊較小的值交換 |
| Next Greater Element III | 556 | 同一套演算法套在整數的各位數上（要檢查溢位） |
| Permutation Sequence | 60 | 用階乘進位制直接建出第 K 個排列 |
| Permutations | 46 | 產生所有排列（回溯） |
| Permutations II | 47 | 含重複元素的所有排列（回溯 + 去重） |
| Find the Next Palindrome | 3348 | 排列風格的數位操作 |


#### 另一種做法（Python） — 正向掃描找樞紐

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

#### 核心想法

從一個 `"I"`／`"D"` 字串重建出 `[0, n]` 的排列。在**還沒用掉的值域**上放兩個指標：

- `low = 0`（未使用的最小值）、`high = n`（未使用的最大值）
- 遇到 `"I"`（下一個必須**更大**）→ 接上 `low`，然後 `low++`
- 遇到 `"D"`（下一個必須**更小**）→ 接上 `high`，然後 `high--`
- 迴圈結束後 `low == high` → 接上最後剩下的那個值

**為什麼一定合法：** 對 `"I"` 挑 `low`，保證接下來不管是什麼都比它大（剩下的值全都 `> low`）；對 `"D"` 挑 `high`，保證接下來不管是什麼都比它小。我們永遠不會「用掉」某個之後需要的值，所以任何貪婪選擇都會產出一組合法答案。

```text
Pointer roles:
  low  — smallest value not yet placed (consumed on "I")
  high — largest value not yet placed  (consumed on "D")

Invariant: after k chars processed, exactly (n+1) - k values remain,
           and they are the contiguous range [low, high].
           The final leftover (low == high) fills the last slot.
```

---

#### 視覺化追蹤

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

#### 模式（Python）

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

#### 模式（Java）

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

#### 相似題目

| 題目 | LC# | 關鍵模式 |
|---------|-----|-------------|
| DI String Match | 942 | 貪婪：`"I"`→low、`"D"`→high，向內收斂 |
| Next Permutation | 31 | 樞紐 + 後繼者 + 反轉後綴 |
| Valid Permutations for DI Sequence | 903 | 用 DP 計數（而非建構）DI 排列 |
| Score After Flipping Matrix | 861 | 逐位置取最佳選擇的貪婪 |


### 3Sum — LC 15

**模式：固定第一個元素的雙指標**
- 固定第一個元素，剩下兩個用雙指標
- 跳過相同的值來避免重複
- 先把陣列排序

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

**相似題目：**
- LC 15 3Sum（本模式）
- LC 16 3Sum Closest
- LC 18 4Sum
- LC 259 3Sum Smaller
- LC 1 Two Sum


### 3Sum Closest — LC 16

#### 核心想法

**排序 + 固定一個 + 雙指標夾擠：**
- 先排序，讓雙指標的移動方向是確定的
- 把第一個元素固定在索引 `i`（外層迴圈 `i = 0..n-3`）
- 對剩下的子陣列設 `l = i+1`、`r = n-1`，往內夾
- 每一步算 `sum = nums[i] + nums[l] + nums[r]`，當 `|sum - target| < |closest - target|` 時更新 `closest`
- 剛好相等 → 立刻回傳（不可能更好）
- `sum > target` → `r--`（把和變小，需要更小的右值）
- `sum < target` → `l++`（把和變大，需要更大的左值）

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

#### 與 3Sum（LC 15）的模式比較

| 面向 | 3Sum（LC 15） | 3Sum Closest（LC 16） |
|--------|-------------|----------------------|
| **目標** | 所有和為 0 的三元組 | 單一個最接近 `target` 的三元組 |
| **要追蹤的東西** | 結果清單 | `closest` 這個純量 |
| **剛好相等時** | 記錄下來並跳過重複 | 立刻回傳 |
| **是否要跳過重複** | 必須（避免重複三元組） | 可省略（題目保證答案唯一） |
| **回傳** | `List<List<Integer>>` | `int` |

#### 相似題目

| 題目 | LC# | 關鍵差異 |
|---------|-----|----------------|
| 3Sum | 15 | 和必須剛好等於 0；蒐集所有三元組 |
| **3Sum Closest** | **16** | **最接近任意 target 的和** |
| 3Sum Smaller | 259 | 計算和小於 target 的三元組數量 |
| 4Sum | 18 | 四個元素；多加一層固定的外迴圈 |
| Two Sum II | 167 | 兩個元素，已排序的陣列 |
| Two Sum（最接近版） | — | 本模式的雙指標變形 |


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

**模式：用雙指標做原地反轉**

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

**相似題目：**
- LC 344 Reverse String
- LC 345 Reverse Vowels of a String
- LC 541 Reverse String II
- LC 186 Reverse Words in a String II
- LC 151 Reverse Words in a String


## 從中心往外擴

> 模板：[模板 3 — 從中心往外擴](./2_pointers.md#template-3-expand-from-centre--lc-5-lc-647-)。

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

**模式：帶不匹配處理的雙指標**
- 從兩端往內檢查是否為回文
- 第一次遇到不匹配時，試兩種可能：
  1. 跳過左邊字元（檢查 `s[l+1...r]`）
  2. 跳過右邊字元（檢查 `s[l...r-1]`）
- 只要**任一**成立就回傳 true
- 用輔助函式檢查某個區間是不是回文

**關鍵洞見：**
- 不要真的刪掉字元再建新字串（那要 O(N) 空間）
- 改成用指標原地檢查子字串（O(1) 空間）

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

**常見錯誤：**
- ❌ 建立新字串（O(N) 空間與時間）
- ❌ 只試著跳過其中一邊
- ✅ 用帶指標的輔助函式（O(1) 空間）
- ✅ **兩種**跳過方式都要試

**相似題目：**
- LC 680 Valid Palindrome II（本模式）
- LC 125 Valid Palindrome
- LC 1216 Valid Palindrome III（允許刪 k 個字元 — DP）
- LC 234 Palindrome Linked List


### Longest Mountain in Array — LC 845

**核心想法：**

先找出合法的**山頂**（兩側鄰居都嚴格更小的區域最大值），再從山頂**往左右擴展**，找出整座山的山腳。這跟回文的「從中心往外擴」不同：這裡是先驗證山頂，再沿著嚴格單調的斜坡往外走。

**關鍵最佳化 — `i = right` 跳躍：**

處理完一整座山之後，把 `i` 直接跳到 `right`（右側山腳）。少了這一步，外層迴圈會把下坡上的每個索引再檢查一遍 — 變成 O(N²)。加上之後，沒有任何索引會被重訪，所有山加起來的總工作量是 O(N)。

```text
Without skip: outer loop backtracks over already-visited slope indices → O(N²)
With i = right: outer loop only ever moves forward → amortized O(N)
```

**模式（Java — 找山頂 + 往左右擴展）：**

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

**手動追蹤 — `arr = [2,1,4,7,3,2,5]`：**

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

**另一種做法 — `while` 迴圈（顯式追蹤山腳，V1 模式）：**

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

**另一種做法 — 預先算出兩個陣列 `up[]`／`down[]`（前綴／後綴斜坡）：**

與其從每個山頂往外擴，不如先在每個索引上算好：從左邊過來的嚴格遞增段有多長、往右邊延伸的嚴格遞減段有多長。任何**兩者都非零**的索引就是山頂，它的山長就是 `up[i] + down[i] + 1`。這用 O(N) 空間換掉 O(1) 空間，但通常是最好理解的一種。

- `up[i]`   = **結束**在 `i` 的遞增段長度（由左往右建）
- `down[i]` = **從** `i` 開始的遞減段長度（由右往左建）
- 合法山頂 ⇔ `up[i] > 0 && down[i] > 0`

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

**幾種做法的比較：**

| 做法 | 核心指標 | 跳躍技巧 | 什麼時候用 |
|----------|-------------|------------|-------------|
| 山頂 + 擴展（V0） | `i` 掃山頂；`left`／`right` 往外擴 | 每座山處理完後 `i = right` | 結構最清楚 |
| 山腳 + 爬坡（V1） | `base` 追蹤山的起點 | `base = max(end, base+1)` | 單趟掃描，不用往回看 |
| 兩個陣列 `up[]`／`down[]`（V2） | 每個索引預先算好的斜坡長度 | 無 — 直接在山頂合併 | 最直觀；O(N) 空間 |

**合法山的不變條件：**
1. 山頂不能在索引 0 或 n-1
2. 左邊至少要有一個嚴格遞增的元素
3. 右邊至少要有一個嚴格遞減的元素
4. 不能有平坦段（兩側斜坡都必須是嚴格不等式）

**相似的 LC 題目：**

| 題目 | LC# | 關鍵模式 |
|---------|-----|-------------|
| Longest Mountain in Array | 845 | 找山頂 → 往左右擴 → 跳到右側山腳 |
| Valid Mountain Array | 941 | 單趟掃描：先上再下，驗證有走完全程 |
| Peak Index in Mountain Array | 852 | 在保證是山的陣列上二分搜尋山頂 |
| Find Peak Element | 162 | 二分搜尋：永遠往較高的鄰居移動 |
| Trapping Rain Water | 42 | 左右擴展 + 追蹤高度 |
| Longest Palindromic Substring | 5 | 從中心往外擴（本模式的對稱版） |
| Count of Subarrays with Score less than K | 2302 | 斜坡擴展 + 追蹤總和 |

---

### Shortest Palindrome — LC 214

**模式：從右往左掃，用左指標找出最長的回文前綴**

**核心想法：**
- 找出 `s` 中已經是回文的最長前綴
- 把剩下的後綴反轉後接到 `s` 前面
- 用兩個指標：`j` 錨在 0（左），`i` 由右往左掃
- 當 `s[i] == s[j]` 時把 `j` 往前推 — 掃完之後 `s[0..j-1]` 就是配對到的前綴
- 若 `j < n`，對 `s[0..j]` 遞迴，再把非回文的後綴夾在兩側

**手動追蹤 — `s = "aacecaaa"`：**
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

**關鍵洞見 — 為什麼 `j` 追得到那個前綴？**
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

**暴力版本（大輸入會 TLE）：**
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

**指標移動方式比較：**

| 做法 | 左指標 `j` | 右指標 `i` | `j` 什麼時候前進 |
|----------|-----------------|-------------------|-------------------|
| 由右往左掃 | 錨在 0，往右移 | 從 n-1 掃到 0 | `s[i] == s[j]` |
| 暴力 isPalindrome | 從兩端往內擴 | 從 n-1 開始遞減 | 一律（字元相符時） |
| KMP | 不適用 — 改用前綴表 | 不適用 | 不適用 |

**相似題目：**
- LC 214 Shortest Palindrome（本模式）
- LC 5 Longest Palindromic Substring（從中心往外擴）
- LC 647 Palindromic Substrings（從中心往外擴）
- LC 680 Valid Palindrome II（跳過一個字元）
- LC 516 Longest Palindromic Subsequence（DP）
- LC 132 Palindrome Partitioning II（DP + 回文檢查）
- LC 336 Palindrome Pairs（雜湊表 + 回文前綴／後綴）


## 子序列與雙字串比對

> 模板：[模板 4 — 子序列比對](./2_pointers.md#template-4-subsequence-matching-one-pointer-always-moves--lc-392-)。

### Longest Word in Dictionary through Deleting — LC 524 ⭐⭐⭐⭐

**核心想法**

> LC 524 就是**對字典裡每個單字各跑一次 LC 392（Is Subsequence）**，外面再包一層「保留目前為止最好的合法單字」。雙指標的子序列檢查完全相同 — 唯一新增的是挑贏家時的**同分規則**。

給定 `s` 和一個 `dictionary`，回傳其中最長、且是 `s` 的**子序列**的單字。長度相同時回傳**字典序最小**的那個。

對每個 `word`：
1. **子序列檢查**（同 LC 392）：用指標 `i` 掃 `s`，只有相符時才把 `word` 的指標 `j` 往前推。掃完後 `j == len(word)` ⇒ `word` 是子序列。
2. **候選挑選**：如果 `word` 在 `(長度, 字典序)` 上勝過目前最佳解，就換成它。

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

**模式 — 雙指標 + 字串比較（`word < res`）**

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

> **關於 `word < res` 的說明**：Python 的字串比較本來就是**字典序**（辭典順序） — `"apple" < "apply"` 是 `True`。這一行就取代了逐字元比較。見
> [python_trick.md](https://github.com/yennanliu/CS_basics/blob/master/doc/cheatsheet/python_trick.md)。

**另一種做法 — 先排序，回傳第一個相符的（不用寫同分邏輯）**

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

**處理「最長 + 字典序最小」需求的兩種方式**

| 做法 | 怎麼決定贏家 | 取捨 |
|----------|--------------------------|-----------|
| **邊掃邊比**（`V0`） | 用 `(len(word) > len(res))` 和 `word < res` 維護最佳解 | 不用排序 — 額外空間 `O(1)`，但同分邏輯寫在迴圈裡 |
| **先排序 + 取第一個**（`V0-1`） | `sort(key=lambda x: (-len(x), x))`，回傳第一個子序列 | 迴圈乾淨，但前面要付 `O(d log d)` 的排序 |

> **陷阱**：**沒排序**就對第一個找到的子序列 `return word` 是錯的 — 後面的單字可能更長，或（長度相同時）字典序更小。要嘛先排序（V0-1），要嘛跟目前最佳解比較（V0），絕不能兩者混用。

**相似題目**

| 題目 | LC# | 跟 LC 524 的關係 |
|---------|-----|--------------------|
| Is Subsequence | 392 | 就是裡面那層檢查 — 某字串是不是另一個的子序列 |
| Longest Word through Deleting | **524** | 每個單字跑一次 LC 392 + 最長／字典序同分規則 |
| Number of Matching Subsequences | 792 | 數有幾個單字是子序列（規模大時用下一個字元分桶） |
| Shortest Way to Form String | 1055 | 反覆做貪婪子序列比對 |
| Append Characters to Make Subsequence | 2486 | 單趟子序列掃描，數剩下的字元 |

### One Edit Distance — LC 161

**核心想法：**
判斷兩個字串是不是剛好差一次編輯（插入、刪除或取代）。

幾個關鍵觀察：
1. 若 `|len(s) - len(t)| > 1` → 不可能，回傳 false
2. 若 `s == t` → 零次編輯，回傳 false
3. 一律讓 `s` 是較短的那個（需要就交換）
4. 由左往右掃：在**第一個不匹配處**試那唯一可能的操作，再用 `substring.equals()` 在 O(1) 內驗證剩下的部分

**第一個不匹配處的三種情況：**

| 長度關係 | 操作 | 檢查 |
|---------|-----------|-------|
| `len(s) == len(t)` | 取代 `s[i]` | `s[i+1..] == t[i+1..]` |
| `len(s) < len(t)` | 往 s 插入（跳過 t[i]） | `s[i..] == t[i+1..]` |
| `len(s) > len(t)` | 從 s 刪除（跳過 s[i]） | `s[i+1..] == t[i..]` |

迴圈跑完都沒有不匹配時：只有在 `len(t) == len(s) + 1`（尾端插入一個）時才合法。

**模式（Java）：**
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

**為什麼用 `substring` 比較，而不是繼續跑迴圈？**
一旦找到第一個不匹配，合法的修補動作就只剩一種。用 `substring.equals()` 檢查後綴，可以在 O(n) 內解決，不必額外維護旗標或指標簿記。

**指標移動總結：**
```text
Both i and j advance together while chars match.
At FIRST mismatch:
  - Same length  → advance both (replace): check suffix
  - Diff length  → advance j only (insert): check suffix
No second chance — any further mismatch = false.
```

**相似的 LC 題目：**
| 題目 | LC# | 關鍵差異 |
|---------|-----|----------------|
| One Edit Distance | 161 | 剛好 1 次編輯（插入／刪除／取代） |
| Edit Distance | 72 | 最少編輯次數（DP） |
| Is Subsequence | 392 | 只能刪除，次數不限 |
| Longest Common Subsequence | 1143 | 最多共同字元（DP） |
| Valid Palindrome II | 680 | 最多刪 1 個字元湊成回文 |

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

**跟標準子序列的關鍵差異：**

| 面向 | 子序列（LC 392） | CamelCase Matching（LC 1023） |
|--------|---------------------|------------------------------|
| **樣式** | 任意子序列 | 帶型別限制的子序列 |
| **未配對的字元** | 忽略 | **必須**是小寫 |
| **未配對的大寫字元** | 忽略 | **立刻失敗** |
| **使用場景** | 一般比對 | 識別字／名稱比對 |

**圖解：**

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

**指標移動規則：**

1. **i（掃 query 的探路指標）：**
   - **每一步**都往前走
   - 掃過 query 的每個字元
   - 永不倒退

2. **j（追 pattern 進度的指標）：**
   - **只有**在找到相符字元時才移動
   - 若 `j == pattern.length()`，代表 pattern 的字元都找齊了

3. **安全檢查：**
   - 未配對的大寫字元 → **立刻回傳 false**
   - 未配對的小寫字元 → **繼續**（這是允許的插入）

**經典題目：**
- LC 1023 Camelcase Matching（本模式）
- LC 392 Is Subsequence（簡化版）
- LC 524 Longest Word in Dictionary through Deleting
- LC 792 Number of Matching Subsequences

### Expressive Words — LC 809

#### 核心想法

**逐組**比較兩個字串，一組就是同一個字元的最長連續段。對每一組對齊的字元組：
1. 字元必須相同
2. 來源字串的組長 `cntS` 必須**≥** query 的組長 `cntW`（只能擴張，不能縮短）
3. 若兩者不等（`cntS != cntW`），`cntS` 必須**≥ 3** — 否則來源不可能是從 query「拉長」而來

```text
Key invariant:
  cntS < cntW          → impossible (word has more chars than s)
  cntS != cntW && cntS < 3  → impossible (s has too few to be an extension)
  otherwise            → valid group match
```

兩個指標必須同時走到各自字串的結尾。

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

#### 每組的判斷表

| `cntS` vs `cntW` | `cntS >= 3`？ | 結果 |
|-----------------|-------------|--------|
| `cntS == cntW` | — | 合法（完全相符） |
| `cntS < cntW` | — | **不合法**（s 比較短） |
| `cntS > cntW` | 是（>= 3） | 合法（有被拉長） |
| `cntS > cntW` | 否（< 3） | **不合法**（小組不能被拉長） |

#### 相似題目

| 題目 | LC# | 關鍵差異 |
|---------|-----|----------------|
| Expressive Words | 809 | 多個單字：數出可拉長的單字數 |
| String Compression | 443 | 原地把每組編碼成 `char + count` |
| Count and Say | 38 | 讀出各組來產生下一個序列 |
| Consecutive Characters | 1446 | 找最長的單一字元連續段 |
| Run-Length Encoding | — | 字元組的編碼／解碼 |


### Encode and Decode Strings — LC 271

**模式：先解析長度標頭，再把 `i` 依宣告的長度往前跳**

核心想法：把每個字串編碼成 `len(s) + "#" + s`。解碼時用兩個指標（`i`、`j`）：
- 每一輪 `i` 都指在**長度標頭的開頭**
- `j` 從 `i` 往前掃到 `"#"` 為止，得到單字長度
- 取出單字後，`i` 直接跳到 `j + 1 + length`（下一個標頭）

這跟一般的雙指標不同，因為跳躍距離是**可變的**，而且**編碼在字串本身裡** — 沒有固定的視窗大小。

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

**手動追蹤 — `strs = ["Hello", "World"]`：**

```text
encode → "5#Hello5#World"

decode:
  i=0: j scans → s[1]="#", length=5, word="Hello", i=7
  i=7: j scans → s[8]="#", length=5, word="World", i=14
  i=14: loop ends

result: ["Hello", "World"]
```

**為什麼用 `#` 當分隔符是安全的：**
- 長度標頭精確告訴你要讀幾個位元組 — 所以就算單字本身含有 `#`，`j+1+length` 也會正確跳過它
- 唯一有意義的 `#` 是 `i` 之後的**第一個**（由 `j` 掃出來的那個）

**另一種寫法：`str.find("#", i)` — 一樣是 O(N)，但稍微乾淨一點：**
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

**相似題目：**
- LC 271 Encode and Decode Strings（本模式）
- LC 297 Serialize and Deserialize Binary Tree（樹節點的變長編碼）
- LC 449 Serialize and Deserialize BST

---


### Compare Version Numbers — LC 165 ⭐⭐⭐⭐

#### 核心想法

**兩個獨立的指標走在兩個不同字串上，每一輪各消耗一個「區塊」。**

這跟同一個陣列上的 `l`／`r` 不一樣：這裡 `i` 走 `version1`、`j` 走 `version2`，每一次迴圈各從一邊解析出**一個版號段**。

這個模板順手解掉兩個陷阱：
1. **長度不同** — `"1.0"` vs `"1.0.0.0"`。迴圈條件是 `i < n1 **||** j < n2`（OR，不是 AND）。已耗盡的那一邊就直接給 `0` → **隱式補零**。
2. **前導零** — `"1.01"` vs `"1.001"`。用 `a = a * 10 + digit` 累積數值，兩邊都會變成 `1` → 完全不用做字串比較。

```text
Per round:
  parse int a from version1 until '.' or end
  parse int b from version2 until '.' or end
  a != b  -> return -1 / 1 immediately
  a == b  -> skip the '.' on both sides (i++, j++) and continue
Loop ends with all chunks equal -> return 0
```

---

#### 模式（Java）

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

#### 模式（Python）

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

> **Python 捷徑**：`v1 = list(map(int, version1.split(".")))` 之後補零 — 比較短，但要多 O(N) 空間。雙指標版才是面試官通常想聽到的 O(1) 空間解。

#### 變形 — Verifying an Alien Dictionary（LC 953）

> **轉折**：一樣是「兩個字串同步往前走」的掃描，但比較改用**自訂字母順序**，而且迴圈要跑過每一組*相鄰*的單字。「較短的字串必須是前綴」這條規則，取代了隱式補零那條。

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

#### 雙字串同步走的家族

| 題目 | LC# | 每個指標消耗什麼 | 同分／耗盡規則 |
|---------|-----|----------------------------|------------------------|
| Compare Version Numbers | 165 | 一個以 `.` 分隔的整數 | 缺的那邊當 `0` |
| Verifying an Alien Dictionary | 953 | 一個字元（自訂順序） | 較短的必須是前綴 |
| Longest Common Prefix | 14 | 所有字串上的同一個字元 | 遇到第一個不匹配／最短的字就停 |
| Backspace String Compare | 844 | 一個*有效*字元（由後往前掃） | 兩邊必須同時耗盡 |
| Merge Sorted Array | 88 | 各陣列一個元素 | 把剩下那邊倒完 |
| Interval List Intersections | 986 | 各清單一個區間 | 推進先結束的那個清單 |

---


## 在區間、矩陣與分割上的指標

> 模板：[模板 5 — 合併兩個有序陣列](./2_pointers.md#template-5-merge-two-sorted-arrays-fill-from-the-back--lc-88-) 與 [模板 6 — 三路分割](./2_pointers.md#template-6-three-way-partition-dutch-national-flag--lc-75-)。

### Interval List Intersections — LC 986 ⭐⭐⭐⭐

#### 核心想法

兩份已排序、彼此不重疊的區間清單。每份清單一個指標（`i`、`j`），各指著「當前作用中的區間」。每一步問**兩個**問題：

1. **`firstList[i]` 和 `secondList[j]` 有重疊嗎？**
   -> 交集是 `[max(s1, s2), min(e1, e2)]`，當 `max(start) <= min(end)` 時才合法

2. **該移動哪個指標？**
   -> **永遠推進先結束的那個區間**（關鍵技巧）

**為什麼「先結束的」是對的**：結束較早的那個區間，不可能碰到另一份清單裡任何*更後面*的區間（那些區間都在目前這個之後才開始，因為清單已排序且不重疊）。所以它已經被用完了 — 丟掉它不會損失任何東西。

**為什麼不需要記錄前一個區間**（不像 LC 56／LC 57）：結束較晚的那個區間**留在原地**，所以它會自動跟另一份清單的下一個區間再比一次。因此同一個區間可以產生*多個*交集，而我們完全不用回頭看。

```text
firstList  = [[13,23],[24,25]]
secondList = [[15,24],[25,26]]
-> [[15,23],[24,24],[25,25]]
             ^^^^^^^  [15,24] survives after matching [13,23], so it also hits [24,25]
```

**重疊判定 — 兩種等價寫法**：

```python
# form A: direct
if max(s1, s2) <= min(e1, e2): ...

# form B: negate the ONLY 2 non-overlap cases
#   case 1)  |---|            case 2)        |----|
#                 |----|            |---|
if not (e1 < s2 or s1 > e2): ...
```

**複雜度**：時間 `O(m + n)` — 兩個指標都只往前走；額外空間 `O(1)`（輸出不計）。注意我們絕不會「漏掉」任何合法交集：兩個指標合起來剛好前進 `m + n` 次。

#### 視覺化追蹤

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

#### 模式（Python）

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

#### 模式（Java）

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

> **同分處理**（`e1 == e2`）：兩個區間都已用完，所以下面兩種寫法都對：
> - `if/else`（上面那份） — 只移動一個指標；下一輪會拿那個已經沒用的區間，去跟一個在 `e1` *之後*才開始的區間比，發現沒有重疊就往下走。代價是浪費一輪迭代，但不會多輸出東西。
> - `if (endMin == e1) i++; if (endMin == e2) j++;`（下面的詳解版） — **兩個都移動**，省掉那一步浪費。
>
> 唯一錯誤的選擇是**兩個都不動** -> 無窮迴圈。

#### 常見陷阱

| 陷阱 | 為什麼會壞 | 修法 |
|---------|---------------|-----|
| 推進**開始**比較早的那個區間 | 丟掉了一個之後可能還會有交集的區間 | 依**結束**推進，不是開始 |
| 無條件同時推進兩個指標 | 會漏掉合法交集（一個區間可能對到很多個） | 只移動先結束的那個 |
| `e1 == e2` 時兩個都不動 | 無窮迴圈 | 每一輪至少要移動一個指標 |
| 重疊判定用 `start < end` | 會漏掉像 `[5,5]` 這種單點交集 | 閉區間 -> 用 `start <= end` |
| `while (i < m || j < n)` | 會越界；已經沒有可配對的區間了 | 用 `&&` — 任一清單耗盡就停 |
| 另一份清單為空時回傳 `firstList` | 跟 `[]` 的交集是 `[]`，不是那個非空清單 | 提早回傳 `[]` |

#### 相似題目

| 題目 | LC# | 關鍵差異 |
|---------|-----|----------------|
| **Interval List Intersections** | **986** | **2 份清單、2 個指標；推進較早的 `end`** |
| Insert Interval | 57 | 1 份清單 + 1 個新區間；三階段指標掃描 |
| Merge Intervals | 56 | 1 份清單；先排序，再延伸一個滾動的 `end`（聯集，不是交集） |
| Non-overlapping Intervals | 435 | 依 `end` 貪婪 — 一樣是「最早結束者勝」的直覺 |
| Meeting Rooms II | 253 | 把排序後的 starts／ends 當兩個指標 -> 最大同時重疊數 |
| Employee Free Time | 759 | 先合併所有區間，再輸出**空隙**（補集） |
| Merge Sorted Array | 88 | 同樣的雙指標合併，只是對象是純量而不是區間 |
| Intersection of Two Arrays II | 350 | 退化情形：每個「區間」都是單一個點 |
| My Calendar I | 729 | 插入時做同樣的重疊判定 `max(s) < min(e)` |
| Range Module | 715 | 新增／移除／查詢範圍 — 區間交集與合併的綜合體 |

#### 詳解版參考實作（含逐行註解）

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

> **2-12（LC 986）的變形**：不是用兩個指標跑*兩份清單*，而是用單一個指標 `i` 對著一個新區間，分**三階段**掃過一份已排序的清單。

#### 核心想法

`intervals` 已排序且互不重疊。一個往前走的指標 `i`，三個階段 — 完全不用重新排序：

| 階段 | while 條件 | 動作 |
|-------|-----------------|--------|
| 1. **之前** | `intervals[i][1] < newInterval[0]` | 原樣複製（在新區間開始前就結束了） |
| 2. **重疊** | `intervals[i][0] <= e` | 合併：`s = min(s, start)`、`e = max(e, end)` |
| 3. **之後** | 其餘 | 原樣複製 |

合併後的區間 `[s, e]` 在階段 2 與階段 3 之間，剛好輸出一次。

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

> **陷阱**：階段 1 用 `end < newStart`（嚴格），階段 2 用 `start <= e`（含等於） — 像 `[1,3]` 和 `[3,5]` 這種相接的區間必須**合併**，不能各自分開。

#### 區間指標家族

| 題目 | LC# | 指標配置 |
|---------|-----|---------------|
| Insert Interval | 57 | 一份清單 + 一個新區間 → 三階段 |
| Merge Intervals | 56 | 先排序，再用一個指標延伸滾動的 `end` |
| Interval List Intersections | 986 | 兩個指標跑兩份已排序清單 |
| Partition Labels | 763 | 由最後出現位置表推導出的隱式區間 |

---

### Partition Labels — LC 763 ⭐⭐⭐⭐⭐

#### 核心想法

**兩個指標 `start`／`end`，其中 `end` 是一條會往後移動的「承諾」邊界。**

把字串切成盡可能多段，讓**每個字母只出現在其中一段**。

1. **預處理**：記錄 `last[c]` = 字元 `c` 最後出現的索引。
2. **掃描**：維護視窗 `[start, end]`。對每個索引 `i`，當前這段被*強迫*至少延伸到 `last[s[i]]` → `end = max(end, last[s[i]])`。
3. 當 `i == end` 時，`[start, end]` 裡沒有任何字元會延伸超過 `end` → **可以安全切開**。輸出 `end - start + 1`，然後 `start = i + 1`。

```text
Key invariant:
  end = the furthest index that ANY character seen since `start` still needs.
  i < end   → cannot cut yet (some letter still appears later)
  i == end  → the window is "closed" → cut here
```

**為什麼貪婪是最佳的**：在*第一個*滿足 `i == end` 的索引切開，會得到最短的合法段落，也就替後面的段落留下最大的空間。

---

#### 視覺化追蹤

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

#### 模式（Java）

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

#### 模式（Python）

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

#### 跟滑動視窗家族的比較

| 面向 | 滑動視窗（LC 3、209） | Partition Labels（LC 763） |
|--------|----------------------------|---------------------------|
| **左指標** | 違反條件時往內縮 | 只有在切開**之後**才跳（`start = i + 1`） |
| **右指標** | 每輪掃一格 | `end` 是所有必要覆蓋範圍的*最大值*，不是掃描指標 |
| **切開條件** | 視窗合法性的判定式 | `i == end`（沒有待處理的字元） |
| **需要預處理嗎** | 不用 | 要 — 最後出現位置表 |

#### 相似題目

| 題目 | LC# | 關鍵差異 |
|---------|-----|----------------|
| Partition Labels | 763 | 每個字母只落在一段 — `i == end` 就切 |
| Merge Intervals | 56 | 同樣是「延伸 end、出現空隙就收尾」，只是對象是區間 |
| Jump Game II | 45 | `end` = 當前這一跳的邊界；`i == end` 時跳數 +1 |
| Interval List Intersections | 986 | 兩個指標跑兩份區間清單 |
| DI String Match | 942 | 貪婪地消耗指標 |

---


### Search a 2D Matrix II — LC 240 ⭐⭐⭐⭐

#### 核心想法

**二維格子上的雙指標：一個列指標 + 一個行指標，兩者都單調。**

矩陣的**每一列由左到右遞增**、**每一行由上到下遞增**。要從兩個排序方向*互相矛盾*的角落出發 — 也就是**右上角**：

- `matrix[r][c]` 是它那一列的**最大值**、它那一行的**最小值**
- `cur > target` → 底下整個 `c` 行都太大 → `c--`（砍掉一行）
- `cur < target` → 左邊整個 `r` 列都太小 → `r++`（砍掉一列）
- `cur == target` → 找到了

每一步都消掉一整列或一整行 → 最多 `m + n` 步。

```text
Why NOT the top-left corner?
  top-left is the minimum: both "go right" and "go down" increase the value
  → the comparison gives no information about which direction to drop.
  A valid start corner must be a "saddle": max in one direction, min in the other.
  → top-right (this template) or bottom-left (mirror: cur > target -> r--, else c++).
```

---

#### 視覺化追蹤

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

#### 模式（Java）

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

#### 模式（Python）

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

#### 起始角落的選擇

| 起始角落 | `cur > target` | `cur < target` | 可行？ |
|--------------|----------------|----------------|--------|
| **右上** | `c--` | `r++` | ✅（本模板） |
| **左下** | `r--` | `c++` | ✅（鏡像版） |
| 左上（最小值） | — | — | ❌ 兩個方向都遞增 |
| 右下（最大值） | — | — | ❌ 兩個方向都遞減 |

#### 相似題目

| 題目 | LC# | 關鍵差異 |
|---------|-----|----------------|
| Search a 2D Matrix II | 240 | 列和行都排序 → 階梯式 O(M+N) |
| Sort Colors | 75 | 指標縮的是一個區間，不是格子 |
| Container With Most Water | 11 | 同樣是「丟掉可證明無用的那一邊」的貪婪 |
| Two Sum II | 167 | 一維版本：在已排序陣列上 `l++`／`r--` |

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
