# 雙指標

> **範圍** — 陣列與字串上的雙指標家族：兩端向內收斂、快慢指標、從中心擴張、讀寫分離的原地重寫，每一種各給一個標準模板；依條件伸縮的視窗題不在這裡。
> **另見** — *從這份文件拆出去的深入內容*：[2_pointers_examples.md](./2_pointers_examples.md) — LC 題目實作集，每題一個標準解；[2_pointers_quickselect.md](./2_pointers_quickselect.md) — QuickSelect／以 partition 為主的第 K 個元素選取，它算是選取演算法，而不是雙指標掃描。
> *相鄰主題*：[sliding_window.md](./sliding_window.md) — 由條件驅動的變動大小視窗；[2_pointers_linkedlist.md](./2_pointers_linkedlist.md) — 同樣的想法，但作用在節點而非索引上；[n_sum.md](./n_sum.md) — 有序陣列上的 k-sum 特化；[palindrome.md](./palindrome.md) — 從中心擴張的完整討論。

## LeetCode 題目清單

- [Two Pointers](https://leetcode.com/problem-list/two-pointers/)
- [Sliding Window](https://leetcode.com/problem-list/sliding-window/)
- [Array](https://leetcode.com/problem-list/array/)

## 總覽

**雙指標**在同一個序列上放兩個索引（或兩個序列各放一個），依照某條規則推進它們，用一趟線性掃描取代巢狀迴圈。

### 關鍵性質
- **複雜度**：單趟掃描是 O(N)；需要先排序的話是 O(N log N)；對每個索引都做中心擴張則是 O(N^2)。額外空間 O(1) — 指標就是全部的狀態
- **核心想法**：每次迭代至少推進一個指標，而且從不回頭走同一個索引，所以總工作量被指標移動次數限制住
- **什麼時候用**：輸入有序、原地重寫、回文、合併兩個有序序列、子序列檢查、環偵測
- **關鍵技巧**：快慢指標（同向）、左右指標（收斂）、從中心擴張、一個指標永遠前進

### 參考資料
- [fucking-algorithm : 2 pointers](https://labuladong.online/algo/essential-technique/array-two-pointers-summary/#%E5%8E%9F%E5%9C%B0%E4%BF%AE%E6%94%B9)

## 題型分類

### 指標的種類

- 指標種類

    - `快慢指標`
        - 快、慢指標從`同一個起點`出發

    - `左右指標`
        - 左、右指標分別從 `idx = 0, idx = len(n) - 1` 出發
        - 通常設成
            - 左指標 = 0
            - 右指標 = len(nums)
        - [binary search](https://github.com/yennanliu/CS_basics/blob/master/doc/cheatsheet/binary_search.md)
        - 陣列反轉
        - [2 sum](https://github.com/yennanliu/CS_basics/blob/master/doc/cheatsheet/n_sum.md)
        - [sliding window](https://github.com/yennanliu/CS_basics/blob/master/doc/cheatsheet/sliding_window.md)

- 從中心`擴張`（並處理`奇數、偶數`兩種情況）
    - LC 680
    - LC 647
    - LC 005

- 合併有序陣列
    - LC 88

- 把所有 1 聚在一起的最少交換次數
    - LC 1151（見 [sliding_window.md](https://github.com/yennanliu/CS_basics/blob/master/doc/cheatsheet/sliding_window.md)）

- 救生艇
    - LC 881

- `排序 + 固定一個 + 雙指標`（最接近／較小的和）
    - 固定 `i`，把 `l`/`r` 往內夾；用 `|sum - target|` 追蹤最接近的答案
    - LC 16（3Sum Closest）
    - LC 259（3Sum Smaller）

- `先移動右指標`，再依條件移動左指標
    - LC 567
    - LC 209（見 `sliding window cheatsheet`）

- 帶字元型態限制的`子序列比對`
    - 一個指標永遠前進，另一個看條件才動
    - 對不匹配的字元要額外驗證
    - LC 392（Is Subsequence）
    - LC 1023（Camelcase Matching — 帶大小寫限制）

- `逐段比對兩個字串`
    - 兩個指標都是一次前進一整段（同一字元的連續區塊），不是一次一個字元
    - 每組對齊的段落都要驗證：數量不同時，長度必須足以延伸（>= 3）
    - LC 809（Expressive Words）

- `找樞紐 + 找後繼 + 反轉後綴`（下一個排列）
    - 從右往左掃，找到第一個遞增的相鄰對（樞紐），再找剛好比它大的最小後繼
    - 交換樞紐與後繼，接著把遞減的後綴反轉 → 變成遞增
    - LC 31（Next Permutation）、LC 556（Next Greater Element III）

- `用收斂的 low/high 指標建出排列`（貪婪）
    - 看到一種訊號就取用最小的可用值，另一種就取最大的
    - `low`/`high` 在 `[0, n]` 範圍上往內走；最後活下來的那個填最後一格
    - LC 942（DI String Match）

- `最後出現位置 + 右邊界擴張`（貪婪分段）
    - 先算出每個字元的最後索引；`i == end` 時就切一刀
    - LC 763（Partition Labels）

- `有序二維矩陣上的階梯指標`
    - 從`右上角`的鞍點出發：太大 → `col--`，太小 → `row++`
    - LC 240（Search a 2D Matrix II）

- `跨兩個字串的雙指標（同步／逐段）`
    - 每一輪，兩個指標各從自己的字串取用一段
    - 先耗盡的那一邊 → 隱含補 `0`（LC 165）或套前綴規則（LC 953）
    - LC 165（Compare Version Numbers）、LC 953（Verifying an Alien Dictionary）

- `在有序區間上的三階段指標掃描`
    - 在前面的 → 合併重疊的 → 在後面的
    - LC 57（Insert Interval）

- 演算法
    - 二分搜尋
    - 滑動視窗
    - for 迴圈 + 「從中心往`左`、`右`擴張」

- 資料結構
    - 陣列
    - 鏈結串列

### 模式 → 模板 → 題目

| 題目裡的訊號 | 模板 | 實作在 |
|---|---|---|
| 原地移除／去重，順序有意義 | [模板 1](#template-1-fastslow-read-write-compaction--lc-26-lc-27-) | LC 26, LC 27, LC 80, LC 283 |
| 有序陣列，找一組配對／把範圍往內夾 | [模板 2](#template-2-converging-bidirectional-pointers-) | LC 11, LC 125, LC 167, LC 344 |
| 計算／尋找回文子字串 | [模板 3](#template-3-expand-from-centre--lc-5-lc-647-) | LC 5, LC 647, LC 680 |
| 「A 是不是 B 的子序列」、樣式比對 | [模板 4](#template-4-subsequence-matching-one-pointer-always-moves--lc-392-) | LC 392, LC 524, LC 1023, LC 809 |
| 原地合併兩個有序序列 | [模板 5](#template-5-merge-two-sorted-arrays-fill-from-the-back--lc-88-) | LC 88, LC 986, LC 977 |
| 重排成 3 組／依值分割 | [模板 6](#template-6-three-way-partition-dutch-national-flag--lc-75-) | LC 75, LC 905, LC 86 |
| 鏈結串列有沒有環、找重複數字 | [模板 7](#template-7-fastslow-cycle-detection--lc-141-lc-142-) | LC 141, LC 142, LC 287 |
| 視窗依一個條件變大、依另一個條件縮小 | [模板 8](#template-8-advance-right-then-advance-left-condition-driven) | LC 209, LC 567（見 [sliding_window.md](./sliding_window.md)） |
| 第 k 大／最接近的 k 個（用選取，不要排序） | [2_pointers_quickselect.md](./2_pointers_quickselect.md) | LC 215, LC 973 |

其餘的長尾題目實作，都放在 [2_pointers_examples.md](./2_pointers_examples.md)。

## 模板與演算法

### 模板 1：快慢指標的讀寫壓縮 — LC 26, LC 27 ⭐⭐⭐⭐⭐

#### 核心想法

**慢快（寫讀）模式：**
- `slow` = 「寫」指標 — 指向最後一個已確認的相異元素位置
- `fast` = 「讀」指標 — 掃過陣列找新的相異值
- 當 `nums[fast] != nums[slow]`：找到新的相異值
  1. 先推進 `slow`（空出下一個要寫的位置）
  2. 把 `nums[fast]` 寫入（或交換到）`nums[slow]`
- 回傳 `slow + 1` 當作相異元素的個數

**關鍵不變量：** `nums[0..slow]` 永遠是排好序且互不重複的元素。

**兩種寫法：**
- **覆寫**（`nums[slow] = nums[fast]`）：比較乾淨，推薦 — 陣列本來就有序，我們只需要把相異值往前搬
- **交換**（`swap(nums[slow], nums[fast])`）：也正確，但對有序陣列來說沒必要；只有在原值還要留著用時才需要

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


#### 允許最多 K 份重複

把比較條件推廣成 `nums[fast] != nums[slow - k]`，兩個指標都從 `k` 開始：`k = 1` 就是 LC 26，`k = 2` 就是 LC 80。實作在 [2_pointers_examples.md](./2_pointers_examples.md)。

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

#### Remove Duplicates from Sorted Array（Python）— LC 26

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

#### 模式總結

| 步驟 | 動作 | 為什麼 |
|------|--------|-----|
| `nums[fast] == nums[slow]` | 只推進 `fast` | 是重複值 — 跳過 |
| `nums[fast] != nums[slow]` | `slow++`，然後寫入／交換 | 找到新的相異值 — 佔下一格 |
| 回傳 | `slow + 1` | `slow` 是索引，長度 = 索引 + 1 |

**為什麼用覆寫而不是交換？**
- 陣列有序 → 值只會往左搬，不會往右
- 被覆蓋掉的值不需要保留（那些是已經看過的重複值）
- `nums[slow] = nums[fast]` 是 O(1)，而且更單純

#### 相似題目

| 題目 | LC# | 差別 | 關鍵技巧 |
|---------|-----|------------|-----------|
| Remove Duplicates from Sorted Array | 26 | 每個值只留一份 | 不同時做 `nums[slow] = nums[fast]` |
| Remove Duplicates from Sorted Array II | 80 | 每個值**最多留兩份** | 拿 `nums[fast]` 跟 `nums[slow-1]`（往回兩格）比 |
| Remove Element | 27 | 移除所有等於 `val` 的元素 | `nums[fast] != val` 時才寫 |
| Move Zeroes | 283 | 把 0 移到最後，保持相對順序 | `nums[fast] != 0` 時交換 |
| Remove Duplicates from Sorted List | 83 | LC 26 的鏈結串列版 | 遇到重複就 `node.next = node.next.next` |
| Remove Duplicates from Sorted List II | 82 | 把有重複值的節點**全部**刪掉 | 多一個哨兵節點 + 整組重複跳過 |

### 模板 2：兩端向內收斂的雙指標 ⭐⭐⭐⭐⭐

兩個指標從兩端出發，**朝彼此**走。每一步先評估配對 `(l, r)`，再把不可能讓答案更好的那一側丟掉，所以任何一組配對都不會被檢查兩次。

```text
l = 0, r = n - 1
while l < r:
    evaluate the pair (l, r)
    move the pointer that cannot improve the answer
      -> the shorter wall (LC 11), the non-alphanumeric char (LC 125),
         the side whose sum is too small / too large (LC 167)
```

#### Container With Most Water — LC 11
從最寬的視窗開始，縮掉比較矮的那一側來把面積最大化。

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

**為什麼要移動比較矮的那一側？** 移動比較高的那側只會讓寬度變小，卻無法提高「最小高度」這個瓶頸 — 不可能有收穫。

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

> 允許刪一個字元的版本（LC 680）是同一趟掃描，只是在第一次不匹配時分岔 — 見 [2_pointers_examples.md](./2_pointers_examples.md)。

#### Remove Element 的雙向版本 — LC 27

**模式：左右指標，從兩端往內縮**

跟快慢指標（模板 1）的關鍵差別：
- 快慢指標依序覆寫 → **保持相對順序**
- 雙向版是把 `nums[l]` 換成 `nums[r]` → **不保持順序**，但寫入次數可能較少（`val` 很罕見時划算）

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

**比較：快慢 vs 雙向**

| 面向 | 快慢（模板 1） | 雙向（本節） |
|--------|-------------------|----------------------|
| **順序** | 保持相對順序 | **不**保持順序 |
| **寫入次數** | 每個有效元素寫一次 | val 罕見時寫得更少 |
| **迴圈寫法** | `for` 迴圈（fast 一定前進） | `while (l <= r)` |
| **什麼時候用** | 順序有意義時 | 順序無所謂、想少寫幾次時 |

**相似題目：**
- LC 27 Remove Element（本模式）
- LC 905 Sort Array By Parity — 偶數往左、奇數往右（同樣是雙向往內縮的想法）
- LC 75 Sort Colors（Dutch National Flag）— 三向雙指標分割
- LC 283 Move Zeroes — 順序有意義，改用快慢指標
- LC 26 Remove Duplicates from Sorted Array — 順序有意義，改用快慢指標
- LC 80 Remove Duplicates from Sorted Array II — 順序有意義，改用快慢指標


### 模板 3：從中心擴張 — LC 5, LC 647 ⭐⭐⭐⭐

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


> LC 5 與 LC 647 的完整解法在 [2_pointers_examples.md](./2_pointers_examples.md)；回文題型的完整討論在 [palindrome.md](./palindrome.md)。

### 模板 4：子序列比對，一個指標永遠前進 — LC 392 ⭐⭐⭐⭐

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

**經典題目：**
- LC 392 Is Subsequence
- LC 524 Longest Word in Dictionary through Deleting
- LC 792 Number of Matching Subsequences


### 模板 5：合併兩個有序陣列，從後面往前填 — LC 88 ⭐⭐⭐⭐⭐

#### 核心想法

**往回合併（右 → 左），不要往前。**

- `nums1` 剛好有 `m + n` 格：`m` 個有效元素 + `n` 個空的尾端格子。
- **往前**合併（先放最小的）會**蓋掉** `nums1` 裡還沒讀到的元素 → 需要額外緩衝區（`O(m+n)` 空間）。
- **往回**合併（先放最大的）寫入的是**空的尾端**，那裡永遠在讀指標之後或同位置 → **真正原地，`O(1)` 空間**。

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

**迴圈條件的小技巧 — `while p2 >= 0`（不是 `p1 >= 0 and p2 >= 0`）：**

- 若 `nums2` 先耗盡 → `nums1` 剩下的部分**本來就在正確位置**，什麼都不用做。✅
- 若 `nums1` 先耗盡（`p1 < 0`）→ `nums2` 剩下的元素**還是必須**複製過去。
- 所以只用 `p2` 當條件，兩邊的尾巴都自動處理好 — **不需要另外補一段複製**。
- 另一種寫法（`while p1 >= 0 and p2 >= 0`）就得在最後補一句 `nums1[:p2+1] = nums2[:p2+1]` 把 `nums2` 剩下的倒進去。

#### 視覺化推演

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

#### 模式（Python）

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
#### 模式（Java）

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

#### 常見陷阱

| 陷阱 | 為什麼會壞 | 怎麼修 |
|---------|---------------|-----|
| **左 → 右**合併 | 蓋掉 `nums1` 還沒讀的元素 | 改成右 → 左合併 |
| 用 `while (p1 >= 0 && p2 >= 0)` 卻沒補收尾 | `nums2` 剩下的元素永遠沒被複製 | 只用 `p2` 當條件，**或**補上 `nums1[:p2+1] = nums2[:p2+1]` |
| 沒先檢查 `p1 >= 0` 就讀 `nums1[p1]` | `nums1` 先耗盡時索引錯誤 | 用短路：`p1 >= 0 && nums1[p1] > nums2[p2]` |
| 用 `nums1 = sorted(nums1 + nums2)` | 只是重新綁定區域變數，**沒有**原地修改 | 用切片賦值或往回合併 |
| `p` 從 `m - 1` 開始 | 寫入索引錯了（`nums1` 的大小是 `m + n`） | 改成 `p = m + n - 1` |

#### 相似題目

| 題目 | LC# | 關鍵差別 |
|---------|-----|----------------|
| **Merge Sorted Array** | **88** | **原地寫進 `nums1` 的尾端；往回合併** |
| Merge Two Sorted Lists | 21 | 鏈結串列；用 dummy head 往前合併 |
| Merge k Sorted Lists | 23 | k 條串列；用堆積或分治法 |
| Squares of a Sorted Array | 977 | 結果往回填（最大的平方在兩端） |
| Sorted Merge／合併排序的合併步驟 | — | 就是合併排序 combine 階段的同一段程式 |
| Intersection of Two Arrays II | 350 | 在兩個有序陣列上跑雙指標，留下共同元素 |
| Interval List Intersections | 986 | 在兩串有序區間上跑雙指標 |
| Find Median of Two Sorted Arrays | 4 | 概念上是合併，但要做 O(log(m+n)) 的二分搜尋 |
| Move Zeroes | 283 | 原地寫指標（這題往前寫是安全的） |


### 模板 6：三向分割，Dutch National Flag — LC 75 ⭐⭐⭐⭐
用三個指標，在 O(n) 時間、O(1) 空間內把陣列分成三組。

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

不變量：`nums[0..lo-1]=0`、`nums[lo..mid-1]=1`、`nums[mid..hi]=unknown`、`nums[hi+1..n-1]=2`。

**模式：用雙指標做三向分割**
- 用三個指標：left（放 0）、mid（當前）、right（放 2）
- 把陣列切成三段
- 一趟掃描就解決

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

**相似題目：**
- LC 75 Sort Colors（本模式）
- LC 26 Remove Duplicates from Sorted Array
- LC 80 Remove Duplicates from Sorted Array II
- LC 283 Move Zeroes


### 模板 7：快慢指標的環偵測 — LC 141, LC 142 ⭐⭐⭐⭐
快指標一次走 2 步，慢指標走 1 步。只要有環，它們一定會在環裡相遇。

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

> 節點版的雙指標家族（中間節點、倒數第 n 個、重排、回文串列）在 [2_pointers_linkedlist.md](./2_pointers_linkedlist.md)。

### 模板 8：先推右指標，再推左指標（由條件驅動）

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


> 這就是滑動視窗的骨架；視窗家族（LC 3、LC 76、LC 209、LC 567）由 [sliding_window.md](./sliding_window.md) 負責。

### 基本操作：反轉陣列
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

## 總結與速查

### 模式選擇表

| 模式 | 什麼時候用 | 範例題目 |
|---------|-------------|------------------|
| **反向對走** | 有序陣列、回文檢查 | LC 167, LC 344, LC 125 |
| **同向（快慢）** | 去重、環偵測 | LC 26, LC 27, LC 142 |
| **滑動視窗** | 子陣列／子字串題 | LC 3, LC 76, LC 209 |
| **合併兩串** | 合併有序陣列／串列 | LC 88, LC 21 |
| **分割** | 重排元素 | LC 75, LC 86 |
| **可刪字元的回文** | 允許 k 次修改 | LC 680, LC 1216 |
| **固定一個 + 雙指標（精確）** | 和 == target；蒐集所有解 | LC 15, LC 18 |
| **固定一個 + 雙指標（最接近）** | 和最接近 target | LC 16, LC 259 |
| **子序列比對** | 判斷一個字串是不是另一個的子序列 | LC 392, LC 524, LC 792 |
| **帶限制的樣式比對** | 子序列 + 字元型態驗證 | LC 1023 |
| **最長回文前綴** | 找最長回文前綴，把反轉後的後綴接到前面 | LC 214, LC 336 |
| **長度前綴（編碼／解碼）** | 解析 `len#word` 區塊；`i` 依宣告的長度跳 | LC 271, LC 297 |
| **收斂的 low/high（建排列）** | 貪婪：看訊號取用最小／最大的可用值 | LC 942 |
| **最後出現位置 + 擴張右界** | 貪婪分段；`i == end` 時切一刀 | LC 763 |
| **階梯法（有序二維矩陣）** | 在列與行都有序的格子上搜尋 | LC 240 |
| **兩個字串同步走** | 逐段比較／解析兩個序列 | LC 165, LC 953, LC 14 |
| **三階段區間掃描** | 把一個區間插入／合併進有序清單 | LC 57, LC 56 |

### 依難度分類的經典題

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
- LC 14 Longest Common Prefix（同步掃過所有字串的同一個字元位置）

#### Medium

- LC 3 Longest Substring Without Repeating Characters（滑動視窗）
- LC 5 Longest Palindromic Substring
- LC 11 Container With Most Water
- LC 15 3Sum
- LC 16 3Sum Closest
- LC 18 4Sum
- LC 75 Sort Colors（Dutch National Flag）
- LC 80 Remove Duplicates from Sorted Array II
- LC 86 Partition List
- LC 88 Merge Sorted Array
- LC 142 Linked List Cycle II
- LC 165 Compare Version Numbers
- LC 167 Two Sum II - Input Array Is Sorted
- LC 240 Search a 2D Matrix II（階梯式雙指標）
- LC 57 Insert Interval（三階段指標掃描）
- LC 763 Partition Labels
- LC 209 Minimum Size Subarray Sum（滑動視窗）
- LC 287 Find the Duplicate Number
- LC 567 Permutation in String（滑動視窗）
- LC 647 Palindromic Substrings
- LC 713 Subarray Product Less Than K
- LC 881 Boats to Save People
- LC 986 Interval List Intersections
- LC 1023 Camelcase Matching

#### Hard

- LC 42 Trapping Rain Water
- LC 76 Minimum Window Substring（滑動視窗）
- LC 214 Shortest Palindrome
- LC 828 Count Unique Characters of All Substrings

### 面試提示

| 訊號 | 模式 |
|--------|---------|
| 「排序 + 找一組配對」 | 排完序後用左右指標 |
| 「原地移除／去重」 | 慢快寫指標 |
| 「鏈結串列有沒有環」 | 龜兔賽跑 |
| 「分成 3 組」 | Dutch national flag |
| 「原地合併有序序列」 | 從後往前填 |
| 「回文檢查」 | 從兩端往內走的指標 |
| 「最大面積／容器」 | 縮掉比較矮的那一側 |

### 相關文件

- [binary_search.md](./binary_search.md) — 左右指標，但每次把範圍砍半而不是掃過去
- [sliding_window.md](./sliding_window.md) — 由條件驅動的視窗
- [n_sum.md](./n_sum.md) — 固定一個元素 + 收斂指標（LC 15、LC 16、LC 18）
- [2_pointers_examples.md](./2_pointers_examples.md) — LC 題目實作集
- [2_pointers_quickselect.md](./2_pointers_quickselect.md) — 用 partition 找第 K 個元素
