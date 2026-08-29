# Prefix Sum (前綴和)

> **範圍** — 前綴和／累積和 — 子陣列和、二維前綴和、前綴和搭配雜湊表做計數。
> **另見**：[prefix_sum_advanced.md](./prefix_sum_advanced.md) — 模板 9–13，也就是那些要借用其他資料結構的；[prefix_sum_examples.md](./prefix_sum_examples.md) — 模板沒有直接解掉的實作題；[difference_array.md](./difference_array.md) — 區間*更新*而非區間查詢；[binary_indexed_tree.md](./binary_indexed_tree.md) — 陣列本身也會變動時；[kadane_algorithm.md](./kadane_algorithm.md) — 不靠前綴和求最大子陣列。

<p align="center"><img src="../pic/prefix_sum.png"></p>

## LeetCode 題目清單

- [Prefix Sum](https://leetcode.com/problem-list/prefix-sum/)

## 總覽

**前綴和**是一種預處理技巧：先花 O(n) 前處理，之後任何子陣列的和都能在 O(1) 時間算出來。核心想法就是預先算好「從陣列開頭累加到每個位置」的和。

### 關鍵性質
- **時間複雜度**：
  - 前處理：O(n)
  - 查詢子陣列和：O(1)
  - 整體：O(n) 前處理 + 每次查詢 O(1)
- **空間複雜度**：O(n)，用來存前綴和
- **核心想法**：`prefixSum[i] = nums[0] + nums[1] + ... + nums[i-1]`
- **子陣列和**：`sum(i, j) = prefixSum[j+1] - prefixSum[i]`
- **什麼時候用**：
  - 有多次區間求和查詢
  - 帶條件的子陣列問題
  - 搭配 HashMap 把 O(n²) 降成 O(n)
  - 二維區間求和查詢

### 參考資料
- [Fucking Algorithm - Prefix Sum](https://labuladong.github.io/algo/2/19/22/)
- [LeetCode Prefix Sum Problems](https://leetcode.com/tag/prefix-sum/)
- [LeetCode Problem Set Discussion](https://leetcode.com/discuss/general-discussion/563022/prefix-sum-problems)
- [Hash Map Cheatsheet](https://github.com/yennanliu/CS_basics/blob/master/doc/cheatsheet/hash_map.md)

## 題型分類

### **模式 1：基本區間求和** — LC 303
- **說明**：求任意區間 [i, j] 內元素的總和
- **例子**：LC 303 - Range Sum Query、LC 304 - Range Sum Query 2D
- **模式**：直接套前綴和公式
- **關鍵洞見**：`sum[i:j] = prefixSum[j+1] - prefixSum[i]`

### **模式 2：子陣列和等於目標值** — LC 560
- **說明**：找出／計數總和等於目標值的子陣列
- **例子**：LC 560 - Subarray Sum Equals K、LC 325 - Maximum Size Subarray Sum Equals k
- **模式**：用 HashMap 存前綴和，然後檢查 `(current_sum - target)` 在不在裡面
- **關鍵洞見**：若 `prefixSum[j] - prefixSum[i] = k`，則 `prefixSum[i] = prefixSum[j] - k`

### **模式 3：帶整除／取餘的子陣列** — LC 523
- **說明**：牽涉到整除、餘數或取模運算的題目
- **例子**：LC 523 - Continuous Subarray Sum、LC 974 - Subarray Sums Divisible by K
- **模式**：HashMap 裡存的是餘數，不是實際的和
- **關鍵洞見**：若 `(prefixSum[j] - prefixSum[i]) % k = 0`，則 `prefixSum[j] % k = prefixSum[i] % k`

### **模式 4：區間加值／差分陣列** — LC 370
- **說明**：高效率地對陣列套用區間更新
- **例子**：LC 370 - Range Addition、LC 1094 - Car Pooling
- **模式**：差分陣列技巧搭配前綴和
- **關鍵洞見**：在起點加、在 end+1 減，最後再算一次前綴和

### **模式 5：二維前綴和** — LC 304
- **說明**：求二維矩陣中任意矩形區域的總和
- **例子**：LC 304 - Range Sum Query 2D、LC 1314 - Matrix Block Sum
- **模式**：建二維前綴和矩陣，用排容原理
- **關鍵洞見**：`sum = total - left - top + topleft`

### **模式 6：先轉換再計數** — LC 1248
- **說明**：先把陣列元素轉換過，再用前綴和來計數
- **例子**：LC 1248 - Count Nice Subarrays、LC 926 - Flip String to Monotone
- **模式**：把元素轉成 0/1，再套帶條件的前綴和
- **關鍵洞見**：把題目轉化成更單純的前綴和問題

### **模式 7：距離總和（左右拆分）** — LC 2615
- **說明**：高效率地算出索引之間絕對差值的總和
- **例子**：LC 2615 - Sum of Distances、LC 2121 - Intervals Between Identical Elements、LC 1685 - Sum of Absolute Differences
- **模式**：拆成左右兩半，套 `count * value - sum` 這條公式
- **關鍵洞見**：對索引 `i`，距離 = `(i * countLeft - sumLeft) + (sumRight - i * countRight)`

### **模式 8：前綴最大值（貪婪分塊／分割）** — LC 769
- **說明**：一路追蹤陣列的累積最大值。當 `maxSoFar == i` 時，前綴 `[0..i]` 剛好裝著 `{0, 1, ..., i}` 這些元素，可以獨立成一個排序區塊。
- **例子**：LC 769 - Max Chunks To Make Sorted、LC 768 - Max Chunks To Make Sorted II
- **模式**：單趟掃描搭配一個 `maxSoFar` 變數；每當 `maxSoFar == currentIndex` 就把區塊數加一
- **關鍵洞見**：因為陣列是 `[0, n-1]` 的一個排列，所以只要目前看過的最大值等於當前索引，位置 `0..i` 需要的所有值就一定已經在 `arr[0..i]` 裡

## 0) 概念

### 前綴和陣列怎麼建（核心）

整套技巧都建立在**一行核心程式**上。把它背下來，其他都是推論：

```python
for i in range(len(cnt)):
    prefix[i + 1] = prefix[i] + cnt[i]
```

**一步一步來：**

```python
cnt = [1, 0, 1, 1, 1]

# Step 1: allocate size n+1, fill with 0
#   the leading prefix[0] = 0 is the "empty sum" sentinel
#   -> lets sum(0, r) work without a special case
prefix = [0] * (len(cnt) + 1)
# prefix = [0, 0, 0, 0, 0, 0]

# Step 2: each prefix[i+1] = running total up to (and including) cnt[i]
for i in range(len(cnt)):
    prefix[i + 1] = prefix[i] + cnt[i]

# prefix = [0, 1, 1, 2, 3, 4]
```

**追蹤（為什麼索引是 `i + 1` 而不是 `i`）：**

```text
cnt:        [ 1,  0,  1,  1,  1 ]
index i:      0   1   2   3   4

prefix[0] = 0                 ← sentinel (empty prefix)
prefix[1] = prefix[0] + cnt[0] = 0 + 1 = 1
prefix[2] = prefix[1] + cnt[1] = 1 + 0 = 1
prefix[3] = prefix[2] + cnt[2] = 1 + 1 = 2
prefix[4] = prefix[3] + cnt[3] = 2 + 1 = 3
prefix[5] = prefix[4] + cnt[4] = 3 + 1 = 4

prefix = [0, 1, 1, 2, 3, 4]
          ↑                 ↑
       empty sum        sum of ALL cnt
```

> **重點：** `prefix` 比 `cnt` 多一個元素。`prefix[i+1]` 回答的是
> 「前 `i+1` 個元素的和」= `cnt[0] + ... + cnt[i]`。

**一行版寫法**（`itertools.accumulate` 前面補一個 0）：

```python
from itertools import accumulate
prefix = list(accumulate(cnt, initial=0))   # [0, 1, 1, 2, 3, 4]
```

### 為什麼 `sum(l, r) = prefix[r+1] - prefix[l]`

<p align="center"><img src="../pic/prefix_sum_2.png"></p>

```text
Given nums:    [ a,  b,  c,  d,  e ]
Index:           0   1   2   3   4

Build prefix array (size n+1, prefix[0] = 0):

prefix[0] = 0
prefix[1] = a
prefix[2] = a + b
prefix[3] = a + b + c
prefix[4] = a + b + c + d
prefix[5] = a + b + c + d + e

Visual:

prefix:  0 |  a  | a+b | a+b+c | a+b+c+d | a+b+c+d+e |
index:   0    1      2      3        4          5

To get sum(l=1, r=3) = nums[1] + nums[2] + nums[3] = b + c + d:

prefix[r+1] = prefix[4] = a + b + c + d
prefix[l]   = prefix[1] = a
                           ─────────────
prefix[4] - prefix[1]   =     b + c + d  ✓

Visually (what gets cancelled out):

prefix[4]:  [ a | b | c | d ]
prefix[1]:  [ a ]
            ─────────────────
difference:     [ b | c | d ]   ← this is sum(1, 3)
```

**為什麼大小是 `n+1`？** 多出來的 `prefix[0] = 0` 專門處理 `l = 0` 這個邊界情況：
```text
sum(0, 2) = prefix[3] - prefix[0]
          = (a + b + c) - 0
          = a + b + c  ✓
```
沒有它的話，就得特別寫 `if (left == 0)` 的判斷（見 LC 303 的 V0）。

### 具體範例 — LC 303

```text
nums = [-2, 0, 3, -5, 2, -1]

Step 1: Build prefix array
prefix = [0, -2, -2, 1, -4, -2, -3]
              ↑    ↑  ↑   ↑   ↑   ↑
              -2  -2+0 ...        sum of all

Step 2: Query
sumRange(0, 2) = prefix[3] - prefix[0] = 1 - 0 = 1       ✓  (-2+0+3)
sumRange(2, 5) = prefix[6] - prefix[2] = -3 - (-2) = -1  ✓  (3-5+2-1)
sumRange(0, 5) = prefix[6] - prefix[0] = -3 - 0 = -3     ✓  (-2+0+3-5+2-1)
```

### 兩種寫法比較

| 寫法 | prefix 大小 | 建表 | 查詢 `sum(l, r)` | 邊界情況 |
|-------|-------------|-------|--------------------|-----------|
| **大小 `n+1`**（推薦） | `n + 1` | `prefix[i+1] = prefix[i] + nums[i]` | `prefix[r+1] - prefix[l]` | 不需要特例 |
| **大小 `n`** | `n` | `prefix[i] = prefix[i-1] + nums[i]` | `prefix[r] - (l > 0 ? prefix[l-1] : 0)` | 需要 `if (l == 0)` 判斷 |

## 模板與演算法

### 模板比較表

| 模板類型 | 適用情境 | 關鍵資料結構 | 什麼時候用 |
|---------------|----------|-------------------|-------------|
| **基本前綴和** | 區間求和查詢 | 陣列 | 需要多次算區間和 |
| **HashMap + 前綴和** | 找目標和的子陣列 | HashMap | 找出／計數特定和的子陣列 |
| **取模前綴和** | 整除類問題 | 存餘數的 HashMap | 子陣列和可被 k 整除 |
| **差分陣列** | 區間更新 | 標記起訖點的陣列 | 多次區間加值 |
| **二維前綴和** | 矩形求和查詢 | 二維矩陣 | 二維區間求和 |
| **距離總和** | 絕對差值的總和 | HashMap + 前綴和 | 相同元素之間 |i-j| 的總和 |

### 通用模板

```python
def prefix_sum_solve(nums, target):
    """
    Universal prefix sum template for most problems
    """
    # Step 1: Initialize prefix sum and result
    prefix_sum = 0
    result = 0
    
    # Step 2: HashMap for storing prefix sums (if needed)
    prefix_map = {0: 1}  # Handle subarrays starting from index 0
    
    # Step 3: Iterate through array
    for num in nums:
        # Update prefix sum
        prefix_sum += num
        
        # Check condition based on problem type
        if prefix_sum - target in prefix_map:
            result += prefix_map[prefix_sum - target]
        
        # Update map
        prefix_map[prefix_sum] = prefix_map.get(prefix_sum, 0) + 1
    
    return result
```

### 模板 1：基本前綴和（區間查詢） — LC 303

```python
class PrefixSum:
    def __init__(self, nums):
        """Build prefix sum array for range queries"""
        self.prefix = [0] * (len(nums) + 1)
        for i in range(len(nums)):
            self.prefix[i + 1] = self.prefix[i] + nums[i]
    
    def range_sum(self, left, right):
        """Get sum of elements from index left to right (inclusive)"""
        return self.prefix[right + 1] - self.prefix[left]
```

```java
// Java implementation
class PrefixSum {
    private int[] prefix;
    
    public PrefixSum(int[] nums) {
        prefix = new int[nums.length + 1];
        for (int i = 0; i < nums.length; i++) {
            prefix[i + 1] = prefix[i] + nums[i];
        }
    }
    
    public int rangeSum(int left, int right) {
        return prefix[right + 1] - prefix[left];
    }
}
```

### 模板 2：HashMap + 前綴和（子陣列目標和） — LC 560

```python
def subarray_sum_equals_k(nums, k):
    """Count subarrays with sum equal to k"""
    count = 0
    prefix_sum = 0
    prefix_map = {0: 1}  # Important: initialize with {0: 1}
    
    for num in nums:
        prefix_sum += num
        
        # Check if (prefix_sum - k) exists
        if prefix_sum - k in prefix_map:
            count += prefix_map[prefix_sum - k]
        
        # Update map
        prefix_map[prefix_sum] = prefix_map.get(prefix_sum, 0) + 1
    
    return count
```

```java
// Java implementation
public int subarraySum(int[] nums, int k) {
    int count = 0, prefixSum = 0;
    Map<Integer, Integer> map = new HashMap<>();
    map.put(0, 1);  // Handle subarrays starting from index 0
    
    for (int num : nums) {
        prefixSum += num;
        
        if (map.containsKey(prefixSum - k)) {
            count += map.get(prefixSum - k);
        }
        
        map.put(prefixSum, map.getOrDefault(prefixSum, 0) + 1);
    }
    
    return count;
}
```

> **為什麼 map 存的是次數而不是索引** — 同一個前綴和可能在很多位置出現，
> 而每一個位置都能跟現在這裡構成一個合法的子陣列。存最新索引只會算到其中一個；
> 存「這個和出現過幾次」才會全部算到，所以更新寫成 `map[sum] += 1`，
> 而讀取寫成 `count += map[sum - k]`。

### 模板 3：取模前綴和（整除類問題） — LC 974

**核心數學洞見：**
```text
Let prefix[i] = sum of nums[0..i]

A subarray sum nums[j+1..i] is divisible by k:
  (prefix[i] - prefix[j]) % k == 0

This implies:
  prefix[i] % k == prefix[j] % k

So if we see the SAME remainder again at index i vs a previous index j,
the subarray nums[j+1..i] has sum divisible by k.

map stores: { remainder -> earliest index }

If the current remainder already exists in the map
AND the distance (i - map[remainder]) >= 2, we found a valid subarray.
```

```python
def subarray_divisible_by_k(nums, k):
    """Count subarrays with sum divisible by k"""
    count = 0
    prefix_sum = 0
    remainder_map = {0: 1}  # remainder -> count
    
    for num in nums:
        prefix_sum += num
        remainder = prefix_sum % k
        
        # Handle negative remainders
        if remainder < 0:
            remainder += k
        
        if remainder in remainder_map:
            count += remainder_map[remainder]
        
        remainder_map[remainder] = remainder_map.get(remainder, 0) + 1
    
    return count
```

### 模板 4：差分陣列（區間更新） — LC 370

```python
def range_addition(length, updates):
    """Apply multiple range additions efficiently"""
    # Step 1: Create difference array
    diff = [0] * (length + 1)
    
    # Step 2: Apply range updates to difference array
    for start, end, val in updates:
        diff[start] += val
        diff[end + 1] -= val
    
    # Step 3: Compute prefix sum to get final result
    result = []
    current_sum = 0
    for i in range(length):
        current_sum += diff[i]
        result.append(current_sum)
    
    return result
```

### 模板 5：二維前綴和 — LC 304

```python
class NumMatrix:
    def __init__(self, matrix):
        """Build 2D prefix sum matrix"""
        if not matrix or not matrix[0]:
            return
        
        m, n = len(matrix), len(matrix[0])
        self.prefix = [[0] * (n + 1) for _ in range(m + 1)]
        
        for i in range(1, m + 1):
            for j in range(1, n + 1):
                self.prefix[i][j] = (matrix[i-1][j-1] + 
                                   self.prefix[i-1][j] + 
                                   self.prefix[i][j-1] - 
                                   self.prefix[i-1][j-1])
    
    def sumRegion(self, row1, col1, row2, col2):
        """Calculate sum of rectangle from (row1,col1) to (row2,col2)"""
        return (self.prefix[row2+1][col2+1] - 
                self.prefix[row1][col2+1] - 
                self.prefix[row2+1][col1] + 
                self.prefix[row1][col1])
```

### 模板 6：先轉換再計數 — LC 1248

```python
def count_nice_subarrays(nums, k):
    """Count subarrays with exactly k odd numbers"""
    # Transform: odd -> 1, even -> 0
    transformed = [1 if x % 2 == 1 else 0 for x in nums]

    # Now it's subarray sum equals k problem
    count = 0
    prefix_sum = 0
    prefix_map = {0: 1}

    for val in transformed:
        prefix_sum += val

        if prefix_sum - k in prefix_map:
            count += prefix_map[prefix_sum - k]

        prefix_map[prefix_sum] = prefix_map.get(prefix_sum, 0) + 1

    return count
```

> **你不一定要真的把轉換後的陣列建出來。** 上面的 `transformed` 是為了讓
> 「奇數 → 1、偶數 → 0」這一步看得見，但在時間壓力下該寫的形式，是在維護前綴和的
> 那個迴圈裡直接測 `x % 2` — 這個轉換是一個判斷式，不是一趟掃描。

### 模板 7：距離總和（左右拆分） — LC 2615

這個模式能高效率算出索引之間絕對差值 `|i - j|` 的總和。

#### 核心想法
給一串排序好的索引 `[i0, i1, i2, ..., ik]`，要算 `ij` 到其他所有索引的距離總和：

```text
Instead of: |ij - i0| + |ij - i1| + ... + |ij - ik|  (O(n) per element)

Split into:
  - Left part:  (ij - i0) + (ij - i1) + ... = ij * countLeft - sumLeft
  - Right part: (ij+1 - ij) + (ij+2 - ij) + ... = sumRight - ij * countRight

Total: (ij * countLeft - sumLeft) + (sumRight - ij * countRight)
```

#### 圖解
```text
Indices with same value: [2, 5, 8, 12]
                          ↑  ↑  ↑   ↑
For index 8 (position 2 in list):

  Left indices: [2, 5]
    countLeft = 2
    sumLeft = 2 + 5 = 7
    distanceLeft = 8 * 2 - 7 = 9  → |8-2| + |8-5| = 6 + 3 = 9 ✓

  Right indices: [12]
    countRight = 1
    sumRight = 12
    distanceRight = 12 - 8 * 1 = 4  → |12-8| = 4 ✓

  Total distance for index 8: 9 + 4 = 13
```

#### Python 模板
```python
def sum_of_distances(nums):
    """
    LC 2615: Calculate sum of |i - j| for all j where nums[j] == nums[i]
    Time: O(n), Space: O(n)
    """
    from collections import defaultdict

    n = len(nums)
    result = [0] * n

    # Step 1: Group indices by value
    index_map = defaultdict(list)
    for i, num in enumerate(nums):
        index_map[num].append(i)

    # Step 2: For each group, calculate distances using prefix sum
    for indices in index_map.values():
        m = len(indices)
        if m == 1:
            continue  # Single element has distance 0

        # Build prefix sum of indices
        prefix = [0] * m
        prefix[0] = indices[0]
        for i in range(1, m):
            prefix[i] = prefix[i - 1] + indices[i]

        total_sum = prefix[m - 1]

        # Calculate distance for each index in group
        for i in range(m):
            idx = indices[i]

            # Left part: idx * countLeft - sumLeft
            count_left = i
            sum_left = prefix[i - 1] if i > 0 else 0
            left_dist = idx * count_left - sum_left

            # Right part: sumRight - idx * countRight
            count_right = m - i - 1
            sum_right = total_sum - prefix[i]
            right_dist = sum_right - idx * count_right

            result[idx] = left_dist + right_dist

    return result
```

#### Java 模板
```java
// LC 2615 - Sum of Distances
public long[] distance(int[] nums) {
    int n = nums.length;
    long[] res = new long[n];
    Map<Integer, List<Integer>> map = new HashMap<>();

    // Step 1: Group indices by value
    for (int i = 0; i < n; i++) {
        map.computeIfAbsent(nums[i], k -> new ArrayList<>()).add(i);
    }

    // Step 2: Calculate distances using prefix sum
    for (List<Integer> indices : map.values()) {
        int m = indices.size();
        if (m == 1) continue;

        // Build prefix sum
        long[] prefix = new long[m];
        prefix[0] = indices.get(0);
        for (int i = 1; i < m; i++) {
            prefix[i] = prefix[i - 1] + indices.get(i);
        }

        // Calculate distance for each index
        for (int i = 0; i < m; i++) {
            int idx = indices.get(i);

            // Left: idx * countLeft - sumLeft
            long left = (long) idx * i - (i == 0 ? 0 : prefix[i - 1]);

            // Right: sumRight - idx * countRight
            long right = (prefix[m - 1] - prefix[i]) - (long) idx * (m - i - 1);

            res[idx] = left + right;
        }
    }

    return res;
}
```

### 模板 8：前綴最大值（貪婪分塊／分割） — LC 769

**核心想法：** 對於 `[0, n-1]` 的一個排列，前綴 `arr[0..i]` 能獨立成一個排序區塊，當且僅當 `max(arr[0..i]) == i`。用一個 `maxSoFar` 變數就能追蹤這件事。

```java
// Java — LC 769 Max Chunks To Make Sorted
// Time: O(n)  Space: O(1)
public int maxChunksToSorted(int[] arr) {
    int chunks = 0, maxSoFar = 0;
    for (int i = 0; i < arr.length; i++) {
        maxSoFar = Math.max(maxSoFar, arr[i]);
        if (maxSoFar == i) chunks++;   // all values 0..i are present in arr[0..i]
    }
    return chunks;
}
```

```python
# Python — LC 769
def maxChunksToSorted(arr):
    chunks = max_so_far = 0
    for i, val in enumerate(arr):
        max_so_far = max(max_so_far, val)
        if max_so_far == i:
            chunks += 1
    return chunks
```

**等價的前綴和寫法**（同樣是 O(n)/O(1)）：
```java
// prefixSum of arr == prefixSum of sorted arr  →  same multiset in [0..i]
int chunks = 0, prefixSum = 0, sortedPrefixSum = 0;
for (int i = 0; i < arr.length; i++) {
    prefixSum += arr[i];
    sortedPrefixSum += i;           // sorted array is [0,1,2,...,n-1]
    if (prefixSum == sortedPrefixSum) chunks++;
}
```

**什麼時候該升級成 PrefixMax + SuffixMin（LC 768，一般陣列）：**
```java
// If values are NOT a permutation, use:
// max(arr[0..i-1]) < min(arr[i..n-1])  →  valid cut point
int[] prefixMax = arr.clone(), suffixMin = arr.clone();
for (int i = 1; i < n; i++) prefixMax[i] = Math.max(prefixMax[i-1], prefixMax[i]);
for (int i = n-2; i >= 0; i--) suffixMin[i] = Math.min(suffixMin[i+1], suffixMin[i]);
int chunks = 0;
for (int i = 0; i < n; i++)
    if (i == 0 || suffixMin[i] > prefixMax[i-1]) chunks++;
```

#### 替代作法：邊走邊累加（不建前綴陣列）
```python
def sum_of_distances_optimized(nums):
    """Space-optimized version using running sums"""
    from collections import defaultdict

    n = len(nums)
    result = [0] * n
    index_map = defaultdict(list)

    for i, num in enumerate(nums):
        index_map[num].append(i)

    for indices in index_map.values():
        m = len(indices)
        if m == 1:
            continue

        # Calculate total sum once
        total_sum = sum(indices)

        prefix_sum = 0
        for i, idx in enumerate(indices):
            # Left: idx * i - prefix_sum
            # Right: (total_sum - prefix_sum - idx) - idx * (m - i - 1)
            left_dist = idx * i - prefix_sum
            right_dist = (total_sum - prefix_sum - idx) - idx * (m - i - 1)

            result[idx] = left_dist + right_dist
            prefix_sum += idx

    return result
```

#### 公式整理
| 部分 | 公式 | 意義 |
|-----------|---------|---------|
| **左側距離** | `idx * countLeft - sumLeft` | `(idx - smaller_idx)` 的總和 |
| **右側距離** | `sumRight - idx * countRight` | `(larger_idx - idx)` 的總和 |
| **總距離** | `leftDist + rightDist` | 所有 `\|idx - other_idx\|` 的總和 |

> **為什麼 LC 769 光比總和就夠。** 因為值是 `0..n-1` 的一個排列，所以 `arr` 的某段前綴
> 只有在裝著跟排序後陣列同長度前綴「同一組值」（順序可以不同）時，兩者的和才會相等。
> 而那正是「這段前綴自成一個區塊」的條件 — 所以這個和的檢查根本不需要排序。


## 進階模板

模板 **9–13** 搬到 **[prefix_sum_advanced.md](./prefix_sum_advanced.md)** 了。它們是那些
已經不只是「建個陣列、相減兩項」，而是開始借用其他資料結構的模板：

| # | 模板 | 借來的想法 | LC |
|---|---|---|---|
| 9 | [補集技巧 — 總和 − 中間視窗](./prefix_sum_advanced.md#template-9-complement-trick--total--middle-window---lc-1423) ⭐⭐⭐⭐⭐ | 頭尾繞回來的選法，等於一段要*排除*的連續視窗 | 1423 |
| 10 | [前綴和 + 單調雙端佇列](./prefix_sum_advanced.md#template-10-prefix-sum--monotonic-deque-shortest-subarray-allows-negatives---lc-862) | 用雙端佇列，因為負數會讓雙指標視窗失效 | 862 |
| 11 | [列對壓縮](./prefix_sum_advanced.md#template-11-row-pair-compression--collapse-2d-into-1d-prefix-sum---lc-363) ⭐⭐⭐⭐ | 固定一對列，把二維壓成一維 | 363, 1074 |
| 12 | [前綴 XOR](./prefix_sum_advanced.md#template-12-prefix-xor---lc-1310) ⭐⭐⭐⭐ | XOR 的反運算是自己，所以同一條相減恆等式仍然成立 | 1310 |
| 13 | [用 HashMap 做稀疏差分陣列](./prefix_sum_advanced.md#template-13-sparse-difference-array-via-hashmap-line-sweep---lc-2021) ⭐⭐⭐⭐⭐ | 座標範圍太大時，用雜湊表取代陣列 | 2021 |


## 依模式分類的題目

### 按模式分類的題目清單

#### **模式 1：基本區間求和**
| 題目 | LC # | 關鍵技巧 | 難度 | 模板 |
|---------|------|---------------|------------|----------|
| Range Sum Query - Immutable | 303 | 基本前綴和陣列 | Easy | 模板 1 |
| Range Sum Query 2D - Immutable | 304 | 二維前綴和 | Medium | 模板 5 |
| Product of Array Except Self | 238 | 左右前綴乘積 | Medium | 模板 1 改寫 |
| Running Sum of 1d Array | 1480 | 直接前綴和 | Easy | 模板 1 |
| Find Pivot Index | 724 | 左邊和 vs 右邊和 | Easy | 模板 1 |

#### **模式 2：子陣列和等於目標值**
| 題目 | LC # | 關鍵技巧 | 難度 | 模板 |
|---------|------|---------------|------------|----------|
| Subarray Sum Equals K | 560 | HashMap + 前綴和 | Medium | 模板 2 |
| Maximum Size Subarray Sum Equals k | 325 | HashMap 存索引 | Medium | 模板 2 |
| Subarray Sum Equals K II | 713 | 乘積版本 | Medium | 模板 2 改寫 |
| Binary Subarrays With Sum | 930 | 轉換成求和等於目標 | Medium | 模板 6 |
| Number of Subarrays with Bounded Maximum | 795 | 區間求和技巧 | Medium | 模板 2 |
| Longest Well-Performing Interval | 1124 | 首次出現 map + 分數 ±1 技巧 | Medium | 模板 2 變形 |

#### **模式 3：帶整除／取餘的子陣列**
| 題目 | LC # | 關鍵技巧 | 難度 | 模板 |
|---------|------|---------------|------------|----------|
| Subarray Sums Divisible by K | 974 | 取模前綴和 | Medium | 模板 3 |
| Continuous Subarray Sum | 523 | 取模再檢查長度 | Medium | 模板 3 |
| Make Sum Divisible by P | 1590 | 進階取模技巧 | Medium | 模板 3 |
| Check If Array Pairs Are Divisible by k | 1497 | 統計餘數頻率 | Medium | 模板 3 改寫 |

#### **模式 4：區間加值／更新**
| 題目 | LC # | 關鍵技巧 | 難度 | 模板 |
|---------|------|---------------|------------|----------|
| Range Addition | 370 | 差分陣列 | Medium | 模板 4 |
| Car Pooling | 1094 | 時間軸模擬 | Medium | 模板 4 |
| Corporate Flight Bookings | 1109 | 區間更新 | Medium | 模板 4 |
| Maximum Population Year | 1854 | 事件處理 | Easy | 模板 4 |
| Meeting Rooms II | 253 | 重疊計數 | Medium | 模板 4 |
| Brightest Position on Street | 2021 | 稀疏差分陣列（HashMap） | Medium | 模板 13 |
| Describe the Painting | 1943 | 稀疏差分陣列（HashMap） | Medium | 模板 13 |

#### **模式 5：二維矩陣**
| 題目 | LC # | 關鍵技巧 | 難度 | 模板 |
|---------|------|---------------|------------|----------|
| Range Sum Query 2D | 304 | 二維前綴和 | Medium | 模板 5 |
| Matrix Block Sum | 1314 | 二維區間查詢 | Medium | 模板 5 |
| Number of Submatrices That Sum to Target | 1074 | 二維 + HashMap | Hard | 模板 5 + 2 |
| Maximum Side Length Square | 1292 | 二分搜尋 + 二維前綴和 | Medium | 模板 5 |

#### **模式 6：先轉換再計數**
| 題目 | LC # | 關鍵技巧 | 難度 | 模板 |
|---------|------|---------------|------------|----------|
| Count Number of Nice Subarrays | 1248 | 轉成奇偶 | Medium | 模板 6 |
| Flip String to Monotone Increasing | 926 | 轉成 0/1 再計數 | Medium | 模板 6 |
| Max Chunks To Make Sorted | 769 | 比較總和 | Medium | 模板 6 |
| Longest Arithmetic Subsequence | 1027 | 轉成差值 | Medium | 模板 6 |

#### **模式 7：距離總和**
| 題目 | LC # | 關鍵技巧 | 難度 | 模板 |
|---------|------|---------------|------------|----------|
| Sum of Distances | 2615 | 分組 + 左右拆分 | Medium | 模板 7 |
| Intervals Between Identical Elements | 2121 | 同一個模式，改算間隔 | Medium | 模板 7 |
| Sum of Absolute Differences in a Sorted Array | 1685 | 有序陣列的變形 | Medium | 模板 7 |
| Sum of Distances in Tree | 834 | 樹上的版本（DFS + 換根） | Hard | 模板 7 + DFS |
| Minimum Total Distance Traveled | 2463 | DP + 距離計算 | Hard | 模板 7 + DP |

#### **模式 8：前綴最大值**
| 題目 | LC # | 關鍵技巧 | 難度 | 模板 |
|---------|------|---------------|------------|----------|
| Max Chunks To Make Sorted | 769 | 前綴最大值 == 索引 | Medium | 模板 8 |
| Max Chunks To Make Sorted II | 768 | PrefixMax + SuffixMin 陣列 | Hard | 模板 8 |
| Find the Longest Turbulent Subarray | 978 | 邊走邊追蹤狀態 | Medium | 模板 8 改寫 |

#### **進階／混合模式**
| 題目 | LC # | 關鍵技巧 | 難度 | 模板 |
|---------|------|---------------|------------|----------|
| Maximum Sum of Two Non-Overlapping Subarrays | 1031 | 多個前綴陣列 | Medium | 模板 1 + DP |
| Subarrays with K Different Integers | 992 | 「最多 K 個」技巧 | Hard | 模板 2 |
| Minimum Window Subsequence | 727 | 滑動視窗 + 前綴和 | Hard | 模板 2 + SW |
| Split Array With Same Average | 805 | 子集合和問題 | Hard | 模板 2 |
| Largest Rectangle in Histogram | 84 | 堆疊 + 前綴和 | Hard | 模板 1 + 堆疊 |

### 補充練習題

#### **Easy（打底）**
| 題目 | LC # | 重點 | 模板 |
|---------|------|------------|----------|
| Two Sum | 1 | HashMap 基本功 | 模板 2 改寫 |
| Contains Duplicate II | 219 | 滑動視窗 + map | 模板 2 |
| Maximum Average Subarray I | 643 | 固定長度子陣列 | 模板 1 |
| Degree of an Array | 697 | 元素頻率 | 模板 2 |

#### **Medium（核心模式）**
| 題目 | LC # | 重點 | 模板 |
|---------|------|------------|----------|
| Contiguous Array | 525 | 平衡 0 和 1 | 模板 6 |
| Shortest Unsorted Continuous Subarray | 581 | 陣列分析 | 模板 1 |
| Random Pick with Weight | 528 | 帶權重的隨機選取 | 模板 1 |
| Path Sum III | 437 | 樹 + 前綴和 | 模板 2 |

#### **Hard（進階技巧）**
| 題目 | LC # | 重點 | 模板 |
|---------|------|------------|----------|
| Count of Range Sum | 327 | 合併排序 + 前綴和 | 進階 |
| Reverse Pairs | 493 | 合併排序技巧 | 進階 |
| Create Maximum Number | 321 | 貪婪 + 前綴 | 進階 |
| Count Different Palindromic Subsequences | 730 | DP + 前綴 | 進階 |

## 模式選擇策略

### 前綴和題目的決策框架

```text
Problem Analysis Flowchart:

1. Need multiple range sum queries?
   ├── YES → Use Template 1 (Basic Prefix Sum)
   └── NO → Continue to 2

2. Looking for subarrays with specific sum/count?
   ├── YES → Continue to 2a
   └── NO → Continue to 3
   
   2a. Exact sum target?
       ├── YES → Use Template 2 (HashMap + Prefix Sum)
       └── NO → Continue to 2b
   
   2b. Divisibility or modulo involved?
       ├── YES → Use Template 3 (Modulo Prefix Sum)
       └── NO → Continue to 2c
   
   2c. Count odd/even or binary transformation?
       ├── YES → Use Template 6 (Transform and Count)
       └── NO → Use Template 2

3. Multiple range updates needed?
   ├── YES → Use Template 4 (Difference Array)
   └── NO → Continue to 4

4. 2D matrix operations?
   ├── YES → Use Template 5 (2D Prefix Sum)
   └── NO → Continue to 5

5. Special cases:
   ├── Product instead of sum → Modified Template 1
   ├── Tree path sums → Template 2 + Tree traversal
   ├── Sliding window + prefix → Combine templates
   └── Advanced merge/sort → Custom approach
```

### 模板選擇指南

| 題目關鍵字 | 建議模板 | 例題 |
|------------------|---------------------|------------------|
| 「range sum」、「query」 | 模板 1 | LC 303, 304 |
| 「subarray sum equals」、「count subarrays」 | 模板 2 | LC 560, 325 |
| 「divisible by」、「remainder」、「modulo」 | 模板 3 | LC 974, 523 |
| 「range addition」、「updates」、「intervals」 | 模板 4 | LC 370, 1094 |
| 「2D」、「matrix」、「rectangle」 | 模板 5 | LC 304, 1314 |
| 「odd numbers」、「binary」、「transform」 | 模板 6 | LC 1248, 926 |
| 「sum of distances」、「absolute differences」、「identical elements」 | 模板 7 | LC 2615, 2121, 1685 |
| 「max chunks」、「partition to sort」、「split into sorted segments」 | 模板 8 | LC 769, 768 |
| 「take from both ends」、「remove from left or right」 | 模板 9 | LC 1423, 1658 |
| 「shortest subarray with sum ≥ K」**且允許負數** | 模板 10 | LC 862（對比 LC 209 的視窗解） |
| 「submatrix sum ≤ k」、「count submatrices」、「rectangle + condition」 | 模板 11 | LC 363, 1074 |
| 「XOR of subarray」、「even count of every letter」、「parity」 | 模板 12 | LC 1310, 1915, 1738 |

> 模板 **9–13** 的完整內容寫在 [prefix_sum_advanced.md](./prefix_sum_advanced.md)。

### 怎麼認出各個模板

#### **認出該用模板 1：**
- 題目提到：「range sum query」、「immutable array」、「multiple queries」
- 輸入：陣列 + 多組 (left, right) 查詢
- 輸出：區間 [left, right] 內元素的總和

#### **認出該用模板 2：**
- 題目提到：「subarray sum equals K」、「count subarrays」、「target sum」
- 關鍵洞見：要找出滿足 `prefixSum[j] - prefixSum[i] = target` 的 (i, j) 配對
- HashMap 存的是：`{prefixSum: count}` 或 `{prefixSum: index}`

#### **認出該用模板 3：**
- 題目提到：「divisible by K」、「remainder」、「modulo」、「continuous sum」
- 關鍵洞見：`(prefixSum[j] - prefixSum[i]) % k = 0` 代表兩者餘數相同
- HashMap 存的是：`{remainder: count}` 或 `{remainder: index}`

#### **認出該用模板 4：**
- 題目提到：「range updates」、「add value to range」、「difference array」
- 有多次這種操作：「對索引 [start, end] 加上 val」
- 關鍵洞見：先標記起訖點，最後再算前綴和
- **如果座標範圍很大或會是負的 → 改用模板 13（HashMap）**

#### **認出該用模板 5：**
- 題目提到：「2D matrix」、「rectangle sum」、「submatrix」
- 需要求 (r1,c1) 到 (r2,c2) 這塊矩形的總和
- 公式：`total - left - top + topleft`

#### **認出該用模板 6：**
- 題目提到：「count odd/even」、「binary conditions」、「transform array」
- 先轉換陣列（例如奇數→1、偶數→0），再套前綴和
- 會化簡成更單純的前綴和問題

#### **認出該用模板 7：**
- 題目提到：「sum of distances」、「absolute differences」、「identical elements」
- 需要對相同值的元素算出 `sum of |i - j|`
- 關鍵洞見：拆成左右兩半，套 `count * value - sum` 公式
- HashMap 存的是：`{value: [索引清單]}`
- 時間複雜度從 O(n²) 降到 O(n)

#### **認出該用模板 8：**
- 題目提到：「max chunks」、「切成幾段讓每段能各自排序」、「split to sort」
- 輸入陣列是 `[0, n-1]` 的一個排列（或可以用前綴／後綴陣列推廣）
- 關鍵洞見：`maxSoFar == i` 代表前綴 `[0..i]` 已經是一組完整、自成一體、可以直接排序的集合
- 等價的檢查：`arr[0..i]` 的前綴和等於排序後陣列 `[0..i]` 的前綴和


## 實作範例

七題實作放在 **[prefix_sum_examples.md](./prefix_sum_examples.md)** — 都是上面的模板沒有從頭到尾解掉的：

| 分組 | 題目 |
|---|---|
| [用 HashMap 求子陣列和](./prefix_sum_examples.md#subarray-sums-with-a-hashmap) | LC 325, 523, 1124, 926 |
| [固定視窗與成對視窗](./prefix_sum_examples.md#fixed-and-paired-windows) | LC 1031 |
| [二維前綴和](./prefix_sum_examples.md#2d-prefix-sums) | LC 1292 |
| [區間更新](./prefix_sum_examples.md#range-updates) | LC 1094 |

另外五題以前有自己的範例章節，現在沒有了：LC 370、560、769、1248 和 2615 都已經被
點名它們的那個模板解掉，多出來的第二份實作並沒有補上模板缺的東西。那些副本*真正*
有價值的部分 — 為什麼 map 存次數而不是索引、為什麼 LC 769 光比和就夠、以及為什麼
轉換不必真的建出陣列 — 已經以註解的形式併進模板裡。


## 總結與速查

### 複雜度速查

| 操作 | 時間 | 空間 | 備註 |
|-----------|------|-------|--------|
| 建前綴和陣列 | O(n) | O(n) | 一次性前處理 |
| 區間求和查詢 | O(1) | O(1) | 前處理完之後 |
| 用 HashMap 求子陣列和 | O(n) | O(n) | 平均情況，最壞 O(n²) |
| 建二維前綴和 | O(mn) | O(mn) | m×n 的矩陣 |
| 二維區間查詢 | O(1) | O(1) | 前處理完之後 |
| 差分陣列更新 | O(k) | O(n) | k 次更新，陣列大小 n |

### 模板速查

| 模板 | 模式 | 關鍵程式片段 |
|----------|---------|------------------|
| **模板 1** | 基本區間求和 | `prefix[i+1] = prefix[i] + nums[i]` |
| **模板 2** | HashMap + 目標值 | `if prefix_sum - k in map: count += map[prefix_sum - k]` |
| **模板 3** | 取模／整除 | `remainder = prefix_sum % k; if remainder in map...` |
| **模板 4** | 區間更新 | `diff[start] += val; diff[end+1] -= val` |
| **模板 5** | 二維矩陣 | `prefix[i][j] = val + left + top - topleft` |
| **模板 6** | 轉換後計數 | `先轉換陣列，再套前綴和` |
| **模板 7** | 距離總和 | `left = idx * countLeft - sumLeft; right = sumRight - idx * countRight` |
| **模板 8** | 前綴最大值 | `maxSoFar = max(maxSoFar, arr[i]); if (maxSoFar == i) chunks++` |
| **模板 9** | 補集（取兩端） | `ans = total - min(window of length n-k)` |
| **模板 10** | 單調雙端佇列（有負數） | `while p[i]-p[dq[0]]>=k: ans=min(ans,i-dq.popleft())` |
| **模板 11** | 列對壓縮 | `for top: for bot: colSum[c]+=mat[bot][c]` → 再用一維解法 |
| **模板 12** | 前綴 XOR | `p[i+1] = p[i] ^ a[i]; xor(l,r) = p[r+1] ^ p[l]` |
| **模板 13** | 稀疏差分（HashMap） | `d[start]+=v; d[end+1]-=v; for k in sorted(d): cur+=d[k]` |

> 模板 **9–13** 的完整內容寫在 [prefix_sum_advanced.md](./prefix_sum_advanced.md)。

### 核心數學洞見

#### **前綴和公式**
```python
# For 1D array: sum of subarray [i, j] (inclusive)
subarray_sum = prefix[j + 1] - prefix[i]

# For 2D matrix: sum of rectangle from (r1,c1) to (r2,c2)
rectangle_sum = prefix[r2+1][c2+1] - prefix[r1][c2+1] - prefix[r2+1][c1] + prefix[r1][c1]
```

#### **HashMap 的關鍵洞見**
```python
# If prefix_sum[j] - prefix_sum[i] = k
# Then prefix_sum[i] = prefix_sum[j] - k
# So check if (current_prefix_sum - k) exists in map

# For divisibility: if (sum[j] - sum[i]) % k = 0
# Then sum[j] % k = sum[i] % k
# So check if (current_sum % k) exists in remainder map
```

### 常見模式與技巧

#### **模式 1：Two Sum 的延伸**
```python
# Convert "find subarray with sum = k" to "find two prefix sums with diff = k"
def subarray_sum_equals_k(nums, k):
    prefix_sum = 0
    count = 0
    prefix_map = {0: 1}  # Critical: handle subarrays from index 0
    
    for num in nums:
        prefix_sum += num
        count += prefix_map.get(prefix_sum - k, 0)
        prefix_map[prefix_sum] = prefix_map.get(prefix_sum, 0) + 1
    
    return count
```

#### **模式 2：差分陣列的魔法**
```python
# Apply multiple range updates [start, end, val] efficiently
def range_addition(length, updates):
    diff = [0] * (length + 1)  # Extra space for end+1 indexing
    
    for start, end, val in updates:
        diff[start] += val      # Mark start
        diff[end + 1] -= val    # Mark end+1 (undo effect)
    
    # Convert difference array to result using prefix sum
    result = []
    current = 0
    for i in range(length):
        current += diff[i]      # This is prefix sum computation!
        result.append(current)
    
    return result
```

#### **模式 3：先轉換再求和**
```python
# Many problems can be reduced to simpler prefix sum problems
def count_nice_subarrays(nums, k):
    # Transform: odd numbers → 1, even numbers → 0
    # Problem becomes: count subarrays with sum = k
    binary_array = [1 if x % 2 == 1 else 0 for x in nums]
    return subarray_sum_equals_k(binary_array, k)
```

### 解題步驟

1. **先認出模式**
   - 仔細讀題，抓關鍵字（range、subarray、sum、count 等等）
   - 確認是多次查詢還是單趟掃描
   - 找找有沒有數學關係（整除、取模等等）

2. **挑對模板**
   - 用決策流程圖選出合適的模板
   - 考慮時間／空間複雜度的要求
   - 確認套前綴和之前需不需要先做轉換

3. **處理邊界情況**
   - 空陣列或只有一個元素
   - 負數（取模運算時尤其要小心）
   - 大數相加時的整數溢位
   - 值為 0 的元素對整除判斷的影響

4. **最佳化實作**
   - HashMap 要先放基底情況（通常是 `{0: 1}`）
   - 取模時處理負餘數
   - 能寫成單趟就寫成單趟
   - 只需要次數時，考慮省掉空間

### 常見錯誤與提示

**🚫 常見錯誤：**
- **忘了基底情況**：子陣列題沒有先把 HashMap 初始化成 `{0: 1}`
- **差一錯誤**：前綴和陣列的索引算錯
- **負餘數**：取模時沒處理 `remainder < 0`
- **HashMap 的時機**：先寫進 map 還是先檢查條件，順序搞反
- **二維索引**：二維前綴和裡列跟行搞混
- **區間更新**：差分陣列忘了在 `end+1` 減回去

**✅ 最佳實務：**
- 前綴和陣列**一律**開 `n+1` 大小，用 1-based 索引
- 子陣列題**一律**先在 HashMap 放 `{0: 1}`，處理掉邊界情況
- **再三確認**順序：先檢查條件，再更新 HashMap
- **處理負數**：取模用 `remainder = (remainder % k + k) % k`
- **檢查邊界**：用到 `end+1` 索引時記得檢查陣列範圍
- **測邊界情況**：空陣列、單一元素、全負數

### 面試提示

1. **模式辨識**
   - 看到「subarray sum equals K」→ 直接 HashMap + 前綴和
   - 看到「range queries」→ 基本前綴和陣列
   - 看到「divisible by K」→ HashMap 搭配取模技巧
   - 看到「多次區間更新」→ 差分陣列

2. **表達策略**
   - 把數學洞見講出來：「我們是在找兩個前綴和的配對」
   - 畫例子示範前綴和怎麼運作
   - 主動提複雜度的改善：「這把 O(n²) 降到 O(n)」
   - 討論時間與空間的取捨

3. **實作技巧**
   - 先寫暴力解確認自己理解對了
   - 再用合適的前綴和模板去最佳化
   - 解釋為什麼 HashMap 的初始化很重要
   - 拿一個小例子一步一步走過去

4. **常見追問**
   - 討論變形：「如果要的是最大長度而不是數量呢？」
   - 說明推廣到二維：「在矩陣上該怎麼做？」
   - 考慮限制條件：「如果數字非常大呢？」（溢位）

### 相關主題

- **HashMap／雜湊表**：大多數進階前綴和題目的必需品
- **滑動視窗**：可以跟前綴和結合起來最佳化
- **Two Sum**：很多前綴和題目其實是 two sum 的延伸
- **動態規劃**：前綴和常拿來當 DP 的最佳化手段
- **二分搜尋**：可以跟前綴和結合做區間查詢
- **線段樹**：需要邊更新邊查詢區間和時的替代方案
- **單調堆疊**：有時會跟前綴和一起用來最佳化

### 進階延伸

- **稀疏陣列**：座標壓縮搭配前綴和
- **線上查詢**：需要更新 + 查詢時改用線段樹或樹狀陣列
- **二維區間更新**：二維差分陣列搭配二維前綴和
- **帶權前綴和**：處理各元素權重不同的情況
- **環狀陣列**：改寫模板來處理繞回頭的情況

這份 cheatsheet 涵蓋了所有主要的前綴和模式，並提供一套有系統的方法，讓你能高效率地解掉 40 多題 LeetCode。
