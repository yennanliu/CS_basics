# 對答案做二分搜尋

> **範圍** — 針對一個*候選答案的範圍*，搭配單調的可行性判定式做二分搜尋 — `canFinish` / `isValid` 的寫法、「最小化最大值 vs 最大化最小值」的取捨、`[max(nums), sum(nums)]` 邊界配方，以及在值域上做計數的判定式。
> **另見** — *母文件*：[binary_search.md](./binary_search.md) — 迴圈不變式、邊界（lower/upper bound）模板、旋轉陣列與二維搜尋；[binary_search_examples.md](./binary_search_examples.md) — 索引空間模板的題解存放處。
> *鄰近文件*：[greedy.md](./greedy.md) — 這裡多數判定式都是由貪婪掃描組成的；[bfs.md](./bfs.md) — LC 1631 / LC 778 拿來當判定式的走訪；[heap.md](./heap.md) — 值域計數之外，找第 k 小元素的另一條路。

## LeetCode 題目清單

- [Binary Search](https://leetcode.com/problem-list/binary-search/)
- [Binary Search on Answer (tag)](https://leetcode.com/tag/binary-search/)

## 總覽
**關鍵模式** — FAANG 面試中最重要、也最常考的二分搜尋應用之一。

### 概念

我們不是在陣列裡找某個值，而是對**所有可能答案構成的範圍**做二分搜尋，再用一個驗證函式檢查該答案是否可行。

**什麼時候用：**
- 「找出滿足……的最小／最大值」
- 「使得……成立的最小／最大 X 是多少？」
- 「我們能達成 X 嗎？最佳的 X 是多少？」
- 題目具有**單調性**：如果 X 可行，那麼 X+1（或 X-1）也一定可行

**辨識關鍵字：**
- 「最小化最大值……」
- 「最大化最小值……」
- 「找出最小的容量／速度／除數……」
- 「能不能切分／分配／發放……」

**常見題型：**
- LC 410: Split Array Largest Sum
- LC 1011: Capacity To Ship Packages Within D Days
- LC 875: Koko Eating Bananas
- LC 1283: Find the Smallest Divisor
- LC 1482: Minimum Number of Days to Make m Bouquets
- LC 2226: Maximum Candies Allocated to K Children

---

## 題型分類

**模式 1：最小化最大值**
- 目標：找出讓某個最大值 ≤ X 的最小 X
- 更新：`if valid: right = mid`（試更小的）
- 例題：LC 410、1011、1482、**2616**
- 關鍵：先排序（如果適用）＋ 貪婪驗證
- **為什麼二分搜尋成立**：單調性 — X 越大越容易滿足

**模式 2：最大化最小值**
- 目標：找出讓某個最小值 ≥ X 的最大 X
- 更新：`if valid: left = mid + 1`（試更大的），並用 `mid = (l + r + 1) / 2`
- 例題：**LC 1231**（Divide Chocolate）、LC 1552、LC 2064、LC 2226
- 關鍵：貪婪驗證 — 我們能不能切出足夠多、每份都 ≥ target 的片段／群組？
- **為什麼二分搜尋成立**：單調性 — X 越小越容易滿足
- **LC 410 的對偶**：LC 1231 是「最大化最小甜度」，LC 410 是「最小化最大總和」

**模式 3：以計數做驗證**
- 檢查：「我們能在最多 K 組／天／次操作內完成嗎？」
- 貪婪做法：每一組都盡可能塞滿
- 例題：LC 410（子陣列）、LC 1011（天數）

**模式 4：以總和做驗證**
- 檢查：「總和是否在界限之內？」
- 累加數值並檢查門檻
- 例題：LC 1283（除法總和）、LC 875（小時數）

---

## 模板與演算法

### 單調判定式 — 觀念基礎
二分搜尋真正的威力：只要你能定義一個判定式 `P(x)`，使得所有滿足 `P` 的 `x` 形成一段連續區間，二分搜尋就能在 O(log n) 內找到那個邊界。

```text
P(x) = False, False, ..., False, True, True, ..., True
                                 ^
                          find this boundary
```

單調判定式的例子：
- `canFinish(speed)` — Koko 以 ≥ k 的速度能否在 H 小時內吃完所有香蕉？（LC 875）
- `canShip(capacity)` — 用 ≥ c 的容量能否在 D 天內運完所有包裹？（LC 1011）
- `isEnough(mid)` — 我們能否找到 k 組總和 ≤ mid 的數對？（LC 719）

**「找出滿足 P 的最小 x」模板：**
```python
def binary_search_on_answer(lo, hi):
    while lo < hi:
        mid = (lo + hi) // 2
        if predicate(mid):    # P(mid) is True → answer is ≤ mid
            hi = mid
        else:
            lo = mid + 1
    return lo   # lo == hi == first True
```

**「找出滿足 P 的最大 x」模板：**
```python
def binary_search_max(lo, hi):
    while lo < hi:
        mid = (lo + hi + 1) // 2   # +1 to avoid infinite loop
        if predicate(mid):
            lo = mid
        else:
            hi = mid - 1
    return lo
```

### 統一模板

**結構：**
1. **定義搜尋空間**：[min_possible, max_possible]
2. 在這個範圍上做**二分搜尋**
3. **驗證函式**：檢查目前的值是否滿足限制
4. 依據要最小化或最大化來**更新邊界**

```java
// Unified Template for Binary Search on Answer Space
public int binarySearchOnAnswer(int[] arr, int target) {
    // Step 1: Define search space boundaries
    int left = 1;              // Minimum possible answer
    int right = Integer.MAX_VALUE;  // Maximum possible answer (or sum, max element, etc.)

    // Step 2: Binary search on the answer space
    while (left < right) {  // or left <= right depending on problem
        int mid = left + (right - left) / 2;

        // Step 3: Check if 'mid' is a valid answer using validation function
        if (isValid(arr, mid, target)) {
            // If minimizing: valid answer found, try smaller
            right = mid;

            // If maximizing: valid answer found, try larger
            // left = mid + 1;
        } else {
            // If minimizing: mid is too small, try larger
            left = mid + 1;

            // If maximizing: mid is too large, try smaller
            // right = mid - 1;
        }
    }

    return left;  // or right, they converge to the same value
}

// Step 4: Validation function - checks if 'value' satisfies constraints
private boolean isValid(int[] arr, int value, int target) {
    // Problem-specific logic to check feasibility
    // Example: Can we split array into at most K subarrays with max sum <= value?
    // Returns true if 'value' is valid, false otherwise
    return true;  // placeholder
}
```

---

### 決策表：最小化 vs 最大化 ⭐⭐⭐⭐⭐

| 目標 | 例題 | 可行條件 | 更新規則 | 最終答案 |
|------|------------------|----------------|-------------|--------------|
| **最小化最大值** | LC 410、1011、2616 | 若 mid 可行 | `right = mid`（試更小） | `left`（最小可行值） |
| **最大化最小值** | LC 1231、1552、2226 | 若 mid 可行 | `left = mid + 1`（試更大） | `left - 1` 或 `ans` 變數 |

**口訣：**
- **最小化**：可行時往**左**走（更小的值）→ 找到最小的可行值
- **最大化**：可行時往**右**走（更大的值）→ 找到最大的可行值

**模板上的關鍵差異：**

```java
// MINIMIZE pattern (LC 410, 1011)
while (left < right) {
    int mid = left + (right - left) / 2;  // Standard mid
    if (isValid(mid)) right = mid;        // Try smaller
    else left = mid + 1;
}
return left;

// MAXIMIZE pattern (LC 1231, 1552)
while (left < right) {
    int mid = left + (right - left + 1) / 2;  // CRITICAL: +1 to avoid infinite loop!
    if (isValid(mid)) left = mid;             // Try larger
    else right = mid - 1;
}
return left;
```

> **為什麼「最大化」模板要 `+1`？**
> `// +1 avoids infinite loop: when left+1==right, (left+right)/2 == left, so right never moves`
> 少了 `+1`，當 `left` 與 `right` 相鄰（`left + 1 == right`）時，`mid` 會算成 `left`。
> 若 `isValid(mid)` 為真，我們設 `left = mid = left` — 沒有前進，無窮迴圈。
> 加上 `+1` 讓 `mid` 往上偏，使 `mid == right`，保證每一輪區間都會縮小。

---

### 搜尋邊界模式：`left = max(nums)`、`right = sum(nums)` ⭐⭐⭐⭐⭐

這是「對答案做二分搜尋」類題目裡最**經典的搜尋空間設定**，
適用於所有要你把陣列元素切分／分割／分配的題目。

#### 為什麼是 `left = max(nums)`？

任何合法答案（最大子陣列和、容量、速度等）都必須**至少**和最大的單一元素一樣大 —
因為最壞情況下，那個元素得自己獨佔*某一*組。

```text
nums = [7, 2, 5, 10, 8]
             ^  ^^
        max = 10  ← no matter how you split, some subarray contains 10 alone
                     → answer cannot be smaller than 10
left = max(nums) = 10
```

#### 為什麼是 `right = sum(nums)`？

如果你把**所有**元素放進同一組，總和就是 `sum(nums)`。這一定可行 —
它是明顯成立的上界，答案永遠不會超過它。

```text
nums = [7, 2, 5, 10, 8]  →  sum = 32
If k=1: one subarray containing everything, max sum = 32 ✓
right = sum(nums) = 32
```

#### 圖解：答案就落在 `[max, sum]` 之內

```text
Answer space for nums=[7,2,5,10,8], k=2:

  10    12    14    16    18    20    22   ...   32
  |-----|-----|-----|-----|-----|-----|---------|
  left=max                                right=sum

  Can split into ≤2 subarrays with max sum ≤ mid?

  mid=10: [7,2,5] ok? sum=14 > 10 ✗  →  impossible
  mid=18: [7,2,5]=14 ✓, [10,8]=18 ✓  →  2 subarrays ✓
  mid=15: [7,2,5]=14 ✓, [10,8]=18 > 15 ✗  →  need 3 subarrays ✗
  mid=16: [7,2,5]=14 ✓, [10,8]=18 > 16 ✗  →  need 3 ✗
  mid=17: same ✗
  mid=18: ✓  ← answer = 18

  Feasibility:  ✗ ✗ ✗ ✗ ✗ ✗ ✗ ✗ ✓ ✓ ✓ ... ✓
                |<--  infeasible -->|<-- feasible -->|
                                   ^
                                answer = leftmost ✓
```

#### 程式碼模式（可跨題重複使用）

```java
int left = 0, right = 0;
for (int x : nums) {
    left = Math.max(left, x);  // lower bound: must hold the largest element
    right += x;                // upper bound: put everything in one group
}
// Now binary search on [left, right]
while (left < right) {
    int mid = left + (right - left) / 2;
    if (isValid(nums, k, mid)) {
        right = mid;      // valid → try smaller (minimize)
    } else {
        left = mid + 1;   // invalid → need larger
    }
}
return left;
```

#### 使用同一套 `[max, sum]` 邊界的相似題目

| LC # | 題目 | 最小化的對象 | `left` | `right` |
|------|---------|-----------------|--------|---------|
| **410** | Split Array Largest Sum | 最大子陣列和 | `max(nums)` | `sum(nums)` |
| **1011** | Capacity To Ship Packages | 船的容量 | `max(weights)` | `sum(weights)` |
| **1482** | Min Days to Make m Bouquets | 天數 | `1` | `max(bloomDay)` |
| **875** | Koko Eating Bananas | 進食速度 | `1` | `max(piles)` |
| **1283** | Find the Smallest Divisor | 除數 | `1` | `max(nums)` |
| **2064** | Minimized Maximum of Products Distributed | 每家店的上限 | `1` | `max(quantities)` |

> **提示：** 只要題目說「把陣列元素切分／裝運／分配成 K 組，
> 並最小化其中的最大值」，就直接用 `left = max(nums)`、`right = sum(nums)`。

---

### 為什麼二分搜尋能解「最小化最大值」 ⭐⭐⭐⭐⭐

這是讓二分搜尋能套用到最佳化問題的**理論基礎**。

#### 單調性（關鍵洞察）

二分搜尋需要**單調**（已排序）的性質。在「最小化最大值」題目裡，這個性質存在於**可行性函式**中：

```text
If we can achieve the goal with maximum value = X,
then we can ALWAYS achieve it with maximum value = X + 1 (or any larger value).
```

這會形成一條**單調的可行性曲線**：

```text
Answer Space:  0   1   2   3   4   5   6   7   8   9   ...
               |---|---|---|---|---|---|---|---|---|---|
Feasible?      ✗   ✗   ✗   ✗   ✓   ✓   ✓   ✓   ✓   ✓   ...
                           ↑
                    Decision Boundary (Answer = 4)

The feasibility function is monotonic:
- All values LEFT of boundary: INFEASIBLE (✗)
- All values RIGHT of boundary: FEASIBLE (✓)
- We want to find the LEFTMOST ✓ (minimum feasible value)
```

#### 為什麼這讓二分搜尋可行

**標準二分搜尋**在已排序陣列中找目標值。
**對答案做二分搜尋**則是在已排序的可行性函式中找邊界。

| 概念 | 標準二分搜尋 | 對答案做二分搜尋 |
|---------|----------------------|------------------------|
| **搜尋空間** | 已排序的數值陣列 | 所有可能答案的範圍 |
| **單調性** | 值是排序好的 | 可行性是單調的 |
| **目標** | 找到確切的目標值 | 找到邊界（第一個 ✓） |
| **檢查** | `nums[mid] == target?` | `isValid(mid)?` |

#### 數學證明

**定理：** 若 `isValid(x)` 具備單調性：
- `isValid(x) = true` ⟹ `isValid(x + 1) = true`

則二分搜尋能正確找出最小的可行 `x`。

**證明：**
1. 答案空間 `[left, right]` 可以切成兩段：
   - `[left, answer-1]`：全部不可行
   - `[answer, right]`：全部可行
2. 二分搜尋能在 O(log n) 時間內找到這個切分點
3. 每一輪都把搜尋空間減半，同時維持不變式

#### 圖解範例 — 可行性曲線

```text
Problem: Find p=2 pairs with minimum maximum difference
Array after sorting: [1, 1, 2, 3, 7, 10]

Answer space (max diff): 0  1  2  3  4  5  6  7  8  9
Can form 2 pairs?        ✗  ✓  ✓  ✓  ✓  ✓  ✓  ✓  ✓  ✓
                            ↑
                     Answer = 1 (minimum max diff)

Why monotonic?
- If max_diff = 1 works: pairs (1,1)=0, (2,3)=1 → both ≤ 1 ✓
- If max_diff = 2 works: same pairs still work, more options available ✓
- If max_diff = 0 fails: only (1,1)=0 works, can't form 2 pairs ✗

Larger max_diff → More pairs possible → Easier to satisfy constraint
```

#### 為什麼不直接暴力列舉？（複雜度分析）

| 做法 | 時間複雜度 | 說明 |
|----------|----------------|-------------|
| **線性搜尋** | O(range × n) | 檢查每一個可能的答案 |
| **二分搜尋** | O(log(range) × n) | 每次把搜尋空間減半 |

以 LC 2616 為例：range = 10⁹、n = 10⁵
- 線性：10⁹ × 10⁵ = 10¹⁴ 次運算 ❌ TLE
- 二分：log(10⁹) × 10⁵ ≈ 30 × 10⁵ = 3×10⁶ 次運算 ✓

#### 對答案做二分搜尋的三項前提

```text
✅ Requirement 1: BOUNDED answer space
   - Must have clear [min, max] range
   - Example: [0, max_element - min_element]

✅ Requirement 2: MONOTONIC feasibility
   - If X works, X+1 must also work (for minimize)
   - If X works, X-1 must also work (for maximize)

✅ Requirement 3: EFFICIENT validation
   - Can check if answer X is valid in O(n) or O(n log n)
   - Usually uses greedy approach
```

#### 「最小化最大值」題目的常見結構

```java
public int minimizeMaximum(int[] arr, int constraint) {
    // Step 1: Define bounded search space
    int left = minPossibleAnswer;   // Often 0 or min(arr)
    int right = maxPossibleAnswer;  // Often sum(arr) or max(arr)

    // Step 2: Binary search using monotonic property
    while (left < right) {
        int mid = left + (right - left) / 2;

        // Step 3: Check feasibility (must be O(n) or O(n log n))
        if (isValid(arr, constraint, mid)) {
            right = mid;      // Valid → try smaller (minimize)
        } else {
            left = mid + 1;   // Invalid → need larger
        }
    }

    return left;  // Leftmost valid answer
}

// Validation function - the KEY to correctness
// Must return true for all values >= optimal answer
private boolean isValid(int[] arr, int constraint, int maxAllowed) {
    // Greedy check: can we satisfy constraint with this maxAllowed?
    // This is problem-specific
}
```

---

### 模板變體

**變體 1：閉區間 [left, right]**
```java
while (left <= right) {
    int mid = left + (right - left) / 2;
    if (isValid(mid)) {
        result = mid;  // Store potential answer
        right = mid - 1;  // Try to minimize
    } else {
        left = mid + 1;
    }
}
return result;
```

**變體 2：半開區間 [left, right)**
```java
while (left < right) {
    int mid = left + (right - left) / 2;
    if (isValid(mid)) {
        right = mid;  // Keep mid in range
    } else {
        left = mid + 1;
    }
}
return left;  // left == right
```

---

## LC 範例

### Split Array Largest Sum — LC 410 ⭐⭐⭐⭐⭐

**題目：** 把陣列切成 m 個子陣列，最小化所有子陣列中最大的那個總和。

**洞察：** 對可能的「最大總和」做二分搜尋。對每個 mid，檢查能不能把陣列切成 ≤ m 個、每段總和都 ≤ mid 的子陣列。

```java
// LC 410 - Split Array Largest Sum
class Solution {
    /**
     * time = O(N × log(sum))
     * space = O(1)
     *
     * Approach: Binary search on answer space [max_element, total_sum]
     */
    public int splitArray(int[] nums, int k) {
        // Step 1: Define search space
        int left = 0;   // Minimum: largest single element
        int right = 0;  // Maximum: sum of all elements

        for (int num : nums) {
            left = Math.max(left, num);  // Must fit largest element
            right += num;                // Upper bound is total sum
        }

        // Step 2: Binary search on possible "largest subarray sum"
        while (left < right) {
            int mid = left + (right - left) / 2;

            // Step 3: Check if we can split into ≤ k subarrays with max sum = mid
            if (canSplit(nums, k, mid)) {
                // Valid! Try smaller max sum (minimize)
                right = mid;
            } else {
                // Can't split with this sum, need larger max sum
                left = mid + 1;
            }
        }

        return left;  // Smallest valid maximum subarray sum
    }

    // Validation: Can we split array into at most k subarrays with max sum <= maxSum?
    private boolean canSplit(int[] nums, int k, int maxSum) {
        int subarrayCount = 1;  // Start with 1 subarray
        int currentSum = 0;

        for (int num : nums) {
            // Try to add num to current subarray
            if (currentSum + num <= maxSum) {
                currentSum += num;
            } else {
                // Start new subarray
                subarrayCount++;
                currentSum = num;

                // Early termination: too many subarrays needed
                if (subarrayCount > k) {
                    return false;
                }
            }
        }

        return true;  // Successfully split into ≤ k subarrays
    }
}
```

```python
# Python - LC 410
def splitArray(nums, k):
    """
    Time: O(n × log(sum))
    Space: O(1)
    """
    def can_split(max_sum):
        """Check if we can split into <= k subarrays with max sum <= max_sum"""
        subarray_count = 1
        current_sum = 0

        for num in nums:
            if current_sum + num <= max_sum:
                current_sum += num
            else:
                subarray_count += 1
                current_sum = num
                if subarray_count > k:
                    return False

        return True

    # Binary search on answer space
    left = max(nums)   # Min: largest element
    right = sum(nums)  # Max: total sum

    while left < right:
        mid = left + (right - left) // 2

        if can_split(mid):
            right = mid  # Try smaller (minimize)
        else:
            left = mid + 1

    return left
```

**逐步追蹤：** `nums = [7,2,5,10,8], k = 2`

```text
Search space: [10, 32]  (max element to sum)

Iteration 1: mid = 21
  Can split into [[7,2,5], [10,8]]? Sum = [14, 18] ≤ 21 ✓
  Valid! Try smaller: right = 21

Iteration 2: mid = 15
  Can split [[7,2,5], [10,8]]? Sum = [14, 18] ≤ 15 ✗ (18 > 15)
  Invalid! Need larger: left = 16

Iteration 3: mid = 18
  Can split [[7,2], [5,10], [8]]? Need 3 subarrays ✗ (k=2)
  Can split [[7,2,5], [10,8]]? Sum = [14, 18] ≤ 18 ✓
  Valid! Try smaller: right = 18

left = 16, right = 18
Iteration 4: mid = 17
  Can split? Need to check...

Final: left = 18 (minimum largest sum)
```

---

### Capacity To Ship Packages Within D Days — LC 1011

**題目：** 在 D 天內把包裹運完，求所需的最小容量。

```java
// LC 1011 - Capacity To Ship Packages Within D Days
class Solution {
    /**
     * time = O(N × log(sum))
     * space = O(1)
     */
    public int shipWithinDays(int[] weights, int days) {
        // Search space: [max_weight, sum_of_weights]
        int left = 0, right = 0;

        for (int weight : weights) {
            left = Math.max(left, weight);  // Must hold largest package
            right += weight;                // Upper bound
        }

        while (left < right) {
            int mid = left + (right - left) / 2;

            // Can we ship all packages within D days with capacity = mid?
            if (canShip(weights, days, mid)) {
                right = mid;  // Try smaller capacity (minimize)
            } else {
                left = mid + 1;
            }
        }

        return left;
    }

    // Check if we can ship within D days with given capacity
    private boolean canShip(int[] weights, int days, int capacity) {
        int daysNeeded = 1;
        int currentLoad = 0;

        for (int weight : weights) {
            if (currentLoad + weight <= capacity) {
                currentLoad += weight;
            } else {
                daysNeeded++;
                currentLoad = weight;

                if (daysNeeded > days) {
                    return false;
                }
            }
        }

        return true;
    }
}
```

**做法**：對容量做二分搜尋 ＋ 貪婪驗證
```python
# LC 1011
# V1
# IDEA : BINARY SEARCH
# https://leetcode.com/problems/capacity-to-ship-packages-within-d-days/discuss/390359/Simple-Python-Binary-Search
# V0
# IDEA : BINARY SEARCH
class Solution(object):
     def shipWithinDays(self, weights, D):
            """
            NOTE !!!
                -> for this help func,
                -> we ONLY need to check weights can split by offered max_wgt
                -> so the return val is boolean (True or False)
            """
            # help func
            def cannot_split(weights, D, max_wgt):
                s = 0
                days = 1
                for w in weights:
                    s += w
                    if s > max_wgt:
                        s = w
                        days += 1
                return days > D

            """
            NOTE this !!!
                -> for l, we use max(weights)
                -> for r, we use sum(weights)
            """
            l = max(weights)
            r = sum(weights)
            while l <= r:
                mid = l + (r - l) // 2
                if cannot_split(weights, D, mid):
                    l = mid + 1
                else:
                    r = mid - 1
            return l
```

---

### Koko Eating Bananas — LC 875
**題目：** Koko 必須在 h 小時內吃完所有香蕉，求最小的進食速度。

```python
# LC 875 - Koko Eating Bananas
def minEatingSpeed(piles, h):
    """
    Time: O(n × log(max_pile))
    Space: O(1)
    """
    import math

    def can_finish(speed):
        """Check if Koko can finish with this speed"""
        hours_needed = sum(math.ceil(pile / speed) for pile in piles)
        return hours_needed <= h

    # Binary search on speed [1, max(piles)]
    left, right = 1, max(piles)

    while left < right:
        mid = left + (right - left) // 2

        if can_finish(mid):
            right = mid  # Try slower speed (minimize)
        else:
            left = mid + 1  # Need faster speed

    return left
```

> 對進食速度做二分搜尋；檢查是否能在 H 小時內吃完所有香蕉。

```java
// LC 875 - Koko Eating Bananas
// IDEA: Binary search on answer space [1, max(piles)]
// time = O(N log M), space = O(1)  M = max pile size
public int minEatingSpeed(int[] piles, int h) {
    int l = 1, r = Arrays.stream(piles).max().getAsInt();
    while (l < r) {
        int mid = (l + r) / 2;
        if (canFinish(piles, mid, h)) r = mid;
        else l = mid + 1;
    }
    return l;
}
private boolean canFinish(int[] piles, int speed, int h) {
    int hours = 0;
    for (int pile : piles) hours += (pile + speed - 1) / speed;
    return hours <= h;
}
```

---

### Find the Smallest Divisor — LC 1283
**題目：** 找出最小的除數，使得 sum(ceil(num/divisor)) ≤ threshold。

```java
// LC 1283 - Find the Smallest Divisor
class Solution {
    /**
     * time = O(N × log(max_num))
     * space = O(1)
     */
    public int smallestDivisor(int[] nums, int threshold) {
        int left = 1;
        int right = 0;

        for (int num : nums) {
            right = Math.max(right, num);
        }

        while (left < right) {
            int mid = left + (right - left) / 2;

            if (getDivisionSum(nums, mid) <= threshold) {
                right = mid;  // Valid, try smaller divisor (minimize)
            } else {
                left = mid + 1;  // Sum too large, need larger divisor
            }
        }

        return left;
    }

    private int getDivisionSum(int[] nums, int divisor) {
        int sum = 0;
        for (int num : nums) {
            sum += (num + divisor - 1) / divisor;  // Ceiling division
        }
        return sum;
    }
}
```
---

### Minimum Number of Days to Make m Bouquets — LC 1482
**模式**：`while (l < r - 1)` — 搭配輔助函式的複雜驗證
```python
# LC 1482 Minimum Number of Days to Make m Bouquets
class Solution(object):
    def minDays(self, bloomDay, m, k):
        if m * k > len(bloomDay):
            return -1
        
        def canMakeBouquets(days):
            bouquets = consecutive = 0
            for bloom in bloomDay:
                if bloom <= days:
                    consecutive += 1
                    if consecutive == k:
                        bouquets += 1
                        consecutive = 0
                else:
                    consecutive = 0
            return bouquets >= m
        
        l, r = min(bloomDay), max(bloomDay)
        
        while l < r:
            mid = l + (r - l) // 2
            if canMakeBouquets(mid):
                r = mid
            else:
                l = mid + 1
        
        return l
```

> 對天數做二分搜尋；檢查能否做出 m 束、每束 k 朵相鄰花的花束。

```java
// LC 1482 - Minimum Number of Days to Make m Bouquets
// IDEA: Binary search on days [min, max]; check feasibility
// time = O(N log D), space = O(1)
public int minDays(int[] bloomDay, int m, int k) {
    if ((long) m * k > bloomDay.length) return -1;
    int l = 1, r = 0;
    for (int d : bloomDay) r = Math.max(r, d);
    while (l < r) {
        int mid = (l + r) / 2;
        if (canMake(bloomDay, m, k, mid)) r = mid;
        else l = mid + 1;
    }
    return l;
}
private boolean canMake(int[] bloomDay, int m, int k, int day) {
    int bouquets = 0, consecutive = 0;
    for (int d : bloomDay) {
        if (d <= day) { if (++consecutive == k) { bouquets++; consecutive = 0; } }
        else consecutive = 0;
    }
    return bouquets >= m;
}
```

---

### Divide Chocolate — LC 1231（最大化最小值） ⭐⭐⭐⭐⭐

**題目：** 把巧克力棒分成 K+1 塊（分給 K 位朋友）。你會拿到甜度最小的那一塊，請最大化那個最小甜度。

**關鍵洞察：** 這是**「最大化最小值」**題型 — 「最小化最大值」的對偶：
1. 對你能拿到的「最小甜度」做**二分搜尋**
2. **貪婪驗證**：能否切成 ≥ K+1 塊，且每塊甜度都 ≥ mid？
3. 若可行 → 試更大的最小值（往右）
4. 若不可行 → 目標得再小一點（往左）

**為什麼貪婪成立：**
- 貪婪地累加甜度，一到達目標就切一刀
- 這會讓給定目標下的合法塊數達到最大
- 單調性：若最小甜度 X 可行，X-1 也一定可行（更容易切）

```java
// LC 1231 - Divide Chocolate
class Solution {
    /**
     * time = O(N × log(sum))
     * space = O(1)
     *
     * Approach: Binary search on answer space [1, sum/totalPeople]
     * Pattern: MAXIMIZE MINIMUM
     */
    public int maximizeSweetness(int[] sweetness, int k) {
        int totalPeople = k + 1;  // K friends + yourself

        // Step 1: Define search space
        int left = 1;  // Minimum possible sweetness
        int right = 0;
        for (int s : sweetness) right += s;
        right /= totalPeople;  // Upper bound: average sweetness

        int ans = 0;

        // Step 2: Binary search on "minimum sweetness you can get"
        while (left <= right) {
            int mid = left + (right - left) / 2;

            // Step 3: Check if we can make at least totalPeople pieces
            // where each piece has at least 'mid' sweetness
            if (canSplit(sweetness, totalPeople, mid)) {
                ans = mid;        // Valid! This could be our answer
                left = mid + 1;   // Try larger minimum (MAXIMIZE)
            } else {
                right = mid - 1;  // Can't split, need smaller target
            }
        }

        return ans;
    }

    // Validation: Can we make at least 'totalPeople' pieces with each >= minTarget?
    private boolean canSplit(int[] sweetness, int totalPeople, int minTarget) {
        int currentSweetness = 0;
        int pieces = 0;

        for (int s : sweetness) {
            currentSweetness += s;
            // When current piece reaches target, cut it
            if (currentSweetness >= minTarget) {
                pieces++;
                currentSweetness = 0;
            }
        }

        return pieces >= totalPeople;  // Can we make enough pieces?
    }
}
```

**另一種模板（while l < r）：**

```java
public int maximizeSweetness(int[] sweetness, int k) {
    int left = 1;
    int right = 0;
    for (int s : sweetness) right += s;

    // Binary search with half-open interval
    while (left < right) {
        // CRITICAL: Use (l + r + 1) / 2 for maximize problems to avoid infinite loop
        int mid = left + (right - left + 1) / 2;

        if (canSplit(sweetness, k + 1, mid)) {
            left = mid;       // Valid → try larger (maximize)
        } else {
            right = mid - 1;  // Invalid → reduce target
        }
    }

    return left;
}
```

**逐步追蹤：** `sweetness = [1,2,3,4,5,6,7,8,9], k = 5`

```text
Total people = 6, Sum = 45
Search space: [1, 7]  (1 to 45/6)

Iteration 1: mid = 4
  Pieces with sweetness >= 4:
  [1,2,3]=6 ✓, [4]=4 ✓, [5]=5 ✓, [6]=6 ✓, [7]=7 ✓, [8]=8 ✓, [9]=9 ✓
  Actually: [1,2,3]=6, [4,5]=9... need to re-trace

  Greedy: 1+2+3=6≥4 ✓, 4≥4 ✓, 5≥4 ✓, 6≥4 ✓, 7≥4 ✓, 8≥4 ✓
  Pieces = 6 ≥ 6 ✓
  Valid! Try larger: left = 5

Iteration 2: mid = 6
  Greedy: 1+2+3=6≥6 ✓, 4+5=9≥6 ✓, 6≥6 ✓, 7≥6 ✓, 8≥6 ✓, 9≥6 ✓
  Pieces = 6 ≥ 6 ✓
  Valid! Try larger: left = 7

Iteration 3: mid = 7
  Greedy: 1+2+3+4=10≥7 ✓, 5+6=11≥7 ✓, 7≥7 ✓, 8≥7 ✓, 9≥7 ✓
  Pieces = 5 < 6 ✗
  Invalid! Reduce: right = 6

Final: left = 7 > right = 6, return ans = 6
```

**相似題目（最大化最小值模式）：**

| 題目 | 說明 | 驗證邏輯 |
|---------|-------------|------------------|
| **LC 1231** | Divide Chocolate | 能否切成 K+1 塊且每塊 ≥ target |
| LC 1552 | Magnetic Force | 能否放下 m 顆球且最小間距 ≥ target |
| LC 2226 | Maximum Candies | 能否讓 k 個小孩每人分到 ≥ target |
| LC 2064 | Minimized Maximum Products | 把商品分配到各家店 |

---

### Minimize the Maximum Difference of Pairs — LC 2616 ⭐⭐⭐⭐⭐

**題目：** 找出 p 組索引配對，使所有配對中的最大差值最小。每個索引只能用一次。

**關鍵洞察：** 這是經典的「最小化最大值」題型：
1. 先**排序**陣列，讓相近的數字彼此相鄰
2. 對「最大差值」做**二分搜尋**
3. **貪婪檢查**：能否找到 ≥ p 組、每組差值都 ≤ mid 的配對？

**為什麼貪婪成立（而優先佇列不行）：**
- 排序後，相鄰的兩數會給出最小的差值
- 貪婪配對規則：`if (nums[i+1] - nums[i] <= maxDiff) → take pair, skip i+1`
- 這能保證在該 maxDiff 下取得**最多的配對數**
- 優先佇列做法會失敗，因為這是一個**配對最佳化問題**，局部貪婪選取並不保證全域最佳
- 二分搜尋具備**單調性**：若 maxDiff 為 X 可行，任何更大的差值也可行

```java
// LC 2616 - Minimize the Maximum Difference of Pairs
class Solution {
    /**
     * time = O(N log N + N log(max-min))
     * space = O(1)
     *
     * Approach:
     * 1. Sort array → adjacent elements have minimum differences
     * 2. Binary search on answer space [0, max_diff]
     * 3. Greedy validation: count pairs with diff <= mid
     */
    public int minimizeMax(int[] nums, int p) {
        if (p == 0) return 0;

        // Step 1: Sort to make closest numbers adjacent
        Arrays.sort(nums);

        int n = nums.length;
        // Search space: [0, max_difference]
        int left = 0;
        int right = nums[n - 1] - nums[0];

        // Step 2: Binary search on possible "maximum difference"
        while (left < right) {
            int mid = left + (right - left) / 2;

            // Step 3: Check if we can form at least p pairs with diff <= mid
            if (canFormPairs(nums, p, mid)) {
                right = mid;  // Valid! Try smaller max diff (minimize)
            } else {
                left = mid + 1;  // Can't form enough pairs, need larger diff
            }
        }

        return left;
    }

    // Greedy validation: count maximum pairs with diff <= maxDiff
    private boolean canFormPairs(int[] nums, int p, int maxDiff) {
        int count = 0;

        for (int i = 0; i < nums.length - 1; i++) {
            // If adjacent pair fits constraint, take it!
            if (nums[i + 1] - nums[i] <= maxDiff) {
                count++;
                i++;  // CRITICAL: Skip next index (element can only be in one pair)
            }
            if (count >= p) return true;  // Early termination
        }

        return count >= p;
    }
}
```

**逐步追蹤：** `nums = [10,1,2,7,1,3], p = 2`

```text
After sorting: [1, 1, 2, 3, 7, 10]
Adjacent diffs: [0, 1, 1, 4, 3]

Search space: [0, 9]  (min diff to max diff)

Iteration 1: mid = 4
  Pairs with diff ≤ 4: (1,1)=0 ✓, skip, (2,3)=1 ✓ → 2 pairs
  Valid! Try smaller: right = 4

Iteration 2: mid = 2
  Pairs with diff ≤ 2: (1,1)=0 ✓, skip, (2,3)=1 ✓ → 2 pairs
  Valid! Try smaller: right = 2

Iteration 3: mid = 1
  Pairs with diff ≤ 1: (1,1)=0 ✓, skip, (2,3)=1 ✓ → 2 pairs
  Valid! Try smaller: right = 1

Final: left = 1 (minimum maximum difference)
```

**優先佇列做法為什麼會失敗 — 反例：**

```text
nums = [1, 3, 4, 6, 7, 20], p = 2
Sorted diffs: (3,4)=1, (6,7)=1, (1,3)=2, (4,6)=2, (7,20)=13

PQ picks smallest first:
  1. (3,4)=1 → use 3,4
  2. (6,7)=1 → use 6,7
  Result: max = 1 ✓ (happens to be correct here)

But in general, PQ may pick overlapping or suboptimal pairs.
Binary search guarantees correctness via monotonic property.
```

---

### Path With Minimum Effort — LC 1631，判定式是一張圖 ⭐⭐⭐⭐

> 和上面統一模板一樣的「最小化最大值」骨架，只是可行性檢查換成了 **BFS/DFS 的可達性測試**，而不是 `O(n)` 掃描。

`canReach(limit)` = 「我能不能只走成本 `<= limit` 的步伐，從左上走到右下？」— 對 `limit` 是單調的（限制放寬只會解鎖更多邊），這正是二分搜尋要的性質。

```java
// java
// LC 1631 - Path With Minimum Effort
// IDEA: binary search the answer (max allowed |height diff|); feasibility = BFS reachability
// time = O(m*n*log(maxH-minH)), space = O(m*n)
public int minimumEffortPath(int[][] heights) {
    int mx = Integer.MIN_VALUE, mn = Integer.MAX_VALUE;
    for (int[] row : heights) for (int v : row) { mx = Math.max(mx, v); mn = Math.min(mn, v); }
    int lo = 0, hi = mx - mn;                 // effort 0 is possible (flat grid)
    while (lo < hi) {
        int mid = lo + (hi - lo) / 2;
        if (canReach(heights, mid)) hi = mid; // mid works -> try smaller
        else lo = mid + 1;
    }
    return lo;
}

private boolean canReach(int[][] h, int limit) {
    int m = h.length, n = h[0].length;
    boolean[][] seen = new boolean[m][n];
    int[][] dirs = {{1,0},{-1,0},{0,1},{0,-1}};
    Deque<int[]> q = new ArrayDeque<>();
    q.offer(new int[]{0, 0});
    seen[0][0] = true;
    while (!q.isEmpty()) {
        int[] cur = q.poll();
        if (cur[0] == m - 1 && cur[1] == n - 1) return true;
        for (int[] d : dirs) {
            int nr = cur[0] + d[0], nc = cur[1] + d[1];
            if (nr >= 0 && nr < m && nc >= 0 && nc < n && !seen[nr][nc]
                    && Math.abs(h[nr][nc] - h[cur[0]][cur[1]]) <= limit) {
                seen[nr][nc] = true;
                q.offer(new int[]{nr, nc});
            }
        }
    }
    return false;
}
```

```python
# python
# LC 1631 - Path With Minimum Effort
# IDEA: binary search on the answer + BFS feasibility check (monotone in `limit`)
# time = O(m*n*log(maxH-minH)), space = O(m*n)
from collections import deque

class Solution:
    def minimumEffortPath(self, heights):
        m, n = len(heights), len(heights[0])

        def can_reach(limit):
            seen = [[False] * n for _ in range(m)]
            seen[0][0] = True
            q = deque([(0, 0)])
            while q:
                r, c = q.popleft()
                if r == m - 1 and c == n - 1:
                    return True
                for nr, nc in ((r + 1, c), (r - 1, c), (r, c + 1), (r, c - 1)):
                    if 0 <= nr < m and 0 <= nc < n and not seen[nr][nc] \
                            and abs(heights[nr][nc] - heights[r][c]) <= limit:
                        seen[nr][nc] = True
                        q.append((nr, nc))
            return False

        lo, hi = 0, max(map(max, heights)) - min(map(min, heights))
        while lo < hi:
            mid = (lo + hi) // 2
            if can_reach(mid):
                hi = mid
            else:
                lo = mid + 1
        return lo
```

**姊妹題（骨架相同，只差在邊的規則）**

| LC | 題目 | `canReach(limit)` 的意思 |
|----|---------|-------------------------|
| **1631** | Path With Minimum Effort | 每一步的高度差絕對值都 `<= limit` |
| 778 | Swim in Rising Water | 每個走過的**格子值**都 `<= limit`（即時間 `t`） |

> 兩題的替代解法：**用 min-max（瓶頸）鬆弛的 Dijkstra** `nd = max(d, cost)`，或**併查集**依排序順序逐格加入。時間壓力下二分搜尋 + BFS 最容易推導出來；面試時記得口頭補上 Dijkstra 版本，說明它是 `O(mn log mn)` 的改良。

---

### Kth Smallest Element in a Sorted Matrix — LC 378 ⭐⭐⭐⭐⭐

> 給定一個 `n x n` 矩陣，每一**列與每一行**都遞增排序，回傳第 `k` 小的元素。要求記憶體優於 `O(n²)`（所以不能直接攤平後排序）。

#### 1. 核心想法

**對值域做二分搜尋，而不是對索引。**

因為列／行都排序過，答案必定落在 `[matrix[0][0], matrix[n-1][n-1]]`。我們在這個**值空間**上做二分搜尋，對每個候選 `mid`，數出矩陣中有多少元素 `<= mid`。

- 函式 `count(mid) = #elements <= mid` 對 `mid` 是**單調非遞減**的 → 這個單調性正是二分搜尋能成立的原因。
- 我們要找的是使 `count(x) >= k` 的**最小值** `x`。這個 `x` 保證是矩陣中真實存在的元素（也就是第 k 小的那個）。

```text
if count(mid) < k  → answer is bigger  → left  = mid + 1
else               → mid might be it   → right = mid   (keep left half, include mid)
```

用 `while left < right` 迴圈收斂到單一值，回傳 `left`。

#### 2. 模式

**「對答案做二分搜尋 ＋ 計數檢查」** — 和 Koko（LC 875）、Split Array（LC 410）同一家族。
差別在於：**計數這一步**利用了排序矩陣的結構，用 `O(n)`（階梯走法）而非 `O(n²)` 完成。

**在 O(n) 內數出 `<= target` 的元素 — 從左下角走階梯：**

```python
def countLessEqual(matrix, target):
    n = len(matrix)
    row, col = n - 1, 0          # start bottom-left corner
    count = 0
    while row >= 0 and col < n:
        if matrix[row][col] <= target:
            count += row + 1     # whole column above is also <= target
            col += 1             # move right
        else:
            row -= 1             # move up
    return count
```

**完整解法：**

```python
# LC 378 - Kth Smallest Element in a Sorted Matrix
# IDEA: binary search on value range + O(n) count of elements <= mid
# time = O(n * log(max - min)), space = O(1)
class Solution:
    def kthSmallest(self, matrix, k):
        n = len(matrix)
        left, right = matrix[0][0], matrix[n - 1][n - 1]

        while left < right:
            mid = left + (right - left) // 2
            if self.countLessEqual(matrix, mid) < k:
                left = mid + 1          # too few <= mid, go higher
            else:
                right = mid             # enough, mid may be the answer
        return left                     # left == right == k-th smallest

    def countLessEqual(self, matrix, target):
        n = len(matrix)
        row, col = n - 1, 0
        count = 0
        while row >= 0 and col < n:
            if matrix[row][col] <= target:
                count += row + 1
                col += 1
            else:
                row -= 1
        return count
```

**要記住的重點：**
- 搜尋空間是**值**（`matrix[0][0] .. matrix[n-1][n-1]`），不是索引。
- 用 `while left < right` ＋ `right = mid`（左邊界寫法），才能收斂到第一個 `count >= k` 的值。
- `left` 最後一定會停在真實存在的矩陣元素上 — 不需要再校正回去。
- 另一種計數方式：對每列做 `bisect_right`，得到 `O(n log n)`；階梯走法是 `O(n)`。
- 整體的另一種做法：用 `(val, r, c)` 的**最小堆積**，pop `k` 次 → `O(k log n)` 時間、`O(n)` 空間（記憶體較差，但推理較簡單）。

#### 3. 相似的 LC 題目

| LC | 題目 | 關聯 |
|----|---------|----------|
| **378** | Kth Smallest Element in a Sorted Matrix | 本題 — 對值二分 ＋ 計數 |
| **668** | Kth Smallest Number in Multiplication Table | 對值二分；`count(x)=Σ min(x//i, n)` |
| **719** | Find K-th Smallest Pair Distance | 對距離值二分 ＋ 雙指標計數 |
| **786** | K-th Smallest Prime Fraction | 對分數值二分 ＋ 計數 |
| **373** | Find K Pairs with Smallest Sums | 堆積版本（同樣是第 k 小的想法） |
| **240** | Search a 2D Matrix II | 同一套階梯走法（是搜尋，不是計數） |
| **875** | Koko Eating Bananas | 同一套「對答案二分 ＋ 計數／可行性」模板 |
| **410** | Split Array Largest Sum | 同一套「對答案二分」模板 |
| **4**   | Median of Two Sorted Arrays | 用二分搜尋切分找第 k 小 |

### Find the Duplicate Number — LC 287，在值域上搜尋 ⭐⭐⭐⭐⭐

> `n + 1` 個整數，每個都落在 `[1, n]`。恰有一個數字重複。要求**不能修改陣列**且只用 `O(1)` 額外空間找出它。

#### 核心想法 — 索引空間沒有用，值空間才有

陣列**沒有排序**，所以對索引做二分搜尋毫無意義。但**值**落在已知範圍 `[1, n]` 內，而計數函式

```text
count(v) = #{ x in nums : x <= v }
```

對 `v` 是**單調非遞減**的。由鴿籠原理：

```text
count(v) >  v   ->  a duplicate lives in [1, v]     -> hi = v
count(v) <= v   ->  the duplicate is above v        -> lo = v + 1
```

用標準的「第一個 `True`」模板收斂 → `lo` 就是那個重複的數。

```java
// java
// LC 287 - Find the Duplicate Number
// IDEA: binary search the VALUE range [1, n]; count(mid) > mid (pigeonhole) => duplicate is <= mid
// time = O(n log n), space = O(1)
public int findDuplicate(int[] nums) {
    int lo = 1, hi = nums.length - 1;          // value range [1, n]
    while (lo < hi) {
        int mid = lo + (hi - lo) / 2;
        int cnt = 0;
        for (int x : nums) if (x <= mid) cnt++;
        if (cnt > mid) hi = mid;               // too many small values -> dup in [lo, mid]
        else lo = mid + 1;                     // dup is strictly above mid
    }
    return lo;
}
```

```python
# python
# LC 287 - Find the Duplicate Number
# IDEA: binary search on the value domain [1, n] + pigeonhole count
# time = O(n log n), space = O(1)
class Solution:
    def findDuplicate(self, nums):
        lo, hi = 1, len(nums) - 1
        while lo < hi:
            mid = (lo + hi) // 2
            if sum(1 for x in nums if x <= mid) > mid:
                hi = mid                # duplicate is in [lo, mid]
            else:
                lo = mid + 1            # duplicate is above mid
        return lo
```

**面試筆記**
- 記得提到 `O(n)` 的 **Floyd 循環偵測**替代解（把 `i -> nums[i]` 看成鏈結串列，找出環的入口）— 二分搜尋是你想不起 Floyd 時面試官也會接受的解法，而且比較好證明。
- 骨架和上面 LC 378（排序矩陣第 k 小）以及 LC 719 完全一樣 — 只有 `count()` 的實作不同。矩陣版本另見 `matrix.md`。

**變體 — 從索引推導出單調判定式（LC 1539 Kth Missing Positive Number）**
這裡的巧思是：不去數值，而是**從索引**推導出一個單調量。在嚴格遞增的正整數陣列中，索引 `i` 之前缺少的正整數個數是 `arr[i] - (i + 1)` — 對 `i` 單調非遞減。找出第一個讓它達到 `k` 的索引：

```java
// java
// LC 1539 - Kth Missing Positive Number
// IDEA: missing(i) = arr[i] - (i+1) is monotone -> lower bound on "missing(i) >= k"
// time = O(log n), space = O(1)
public int findKthPositive(int[] arr, int k) {
    int lo = 0, hi = arr.length;               // note hi = n (answer may be past the end)
    while (lo < hi) {
        int mid = lo + (hi - lo) / 2;
        if (arr[mid] - (mid + 1) < k) lo = mid + 1;
        else hi = mid;
    }
    return lo + k;                             // lo numbers present before the answer
}
```

```python
# python
# LC 1539 - Kth Missing Positive Number
# IDEA: binary search the first index whose "missing count so far" >= k
# time = O(log n), space = O(1)
class Solution:
    def findKthPositive(self, arr, k):
        lo, hi = 0, len(arr)          # hi = n, the answer can fall past the last element
        while lo < hi:
            mid = (lo + hi) // 2
            if arr[mid] - (mid + 1) < k:
                lo = mid + 1
            else:
                hi = mid
        return lo + k
```

---

## 依模式分類的題目

| 題目 | 難度 | 模式 | 關鍵洞察 |
|---------|------------|---------|-------------|
| LC 69 | Easy | 整數平方根 | 在 [0, x] 上搜尋 |
| LC 875 | Medium | 最小化速度 | 吃香蕉，貪婪驗證 |
| LC 1011 | Medium | 最小化容量 | 運送包裹，和 LC 410 類似 |
| **LC 1231** | **Hard** | **最大化最小值** | **切巧克力，累加到 ≥ target 就切** |
| LC 1283 | Medium | 最小化除數 | 向上取整除法，總和限制 |
| LC 1482 | Medium | 最小化天數 | 做花束，區間驗證 |
| LC 1552 | Medium | 最大化最小值 | 磁力，經典的「牛舍分配」 |
| LC 2226 | Medium | 最大化糖果數 | 每個小孩的分配量 |
| **LC 2616** | **Medium** | **最小化最大差值** | **排序 ＋ 貪婪配對，跳過用過的** |
| LC 410 | Hard | 最小化最大值 | 切分陣列，子陣列和 |
| LC 2064 | Medium | 最小化最大值 | 把商品分配到各家店 |

**練習順序：**
1. 從 LC 875 開始（最小化模式最清楚的範例）
2. 接著 LC 1011（和 410 類似但更簡單）
3. 精熟 LC 410（經典的最小化最大值，常考）
4. 試 LC 1231（最大化最小值 — LC 410 的對偶）
5. 試 LC 2616（帶配對限制的最小化最大值）
6. 再看 LC 1283、1482（各種變體）
7. 挑戰：LC 1552、2064、2226（最大化最小值模式）

---

## 模式選擇策略

**如何辨識：**
1. 題目要求某個「最小／最大」值
2. 你可以輕鬆檢查「X 可行嗎？」，卻很難直接算出「最佳的 X 是多少？」
3. 答案具有單調性（若 X 可行，X+1 或 X-1 也可行）

**常見錯誤：**
1. **搜尋空間邊界錯誤**
   - 範圍太窄：會漏掉最佳答案
   - 解法：仔細分析下界（例如最大元素）與上界（例如總和）

2. **驗證時差一（off-by-one）**
   ```java
   // ❌ WRONG: Using < instead of <=
   if (currentSum + num < maxSum) {...}

   // ✅ CORRECT: Must allow equality
   if (currentSum + num <= maxSum) {...}
   ```

3. **邊界更新寫錯**
   - 最小化：`right = mid`（不是 `mid - 1`）
   - 最大化：`left = mid + 1`

4. **驗證函式效率不佳**
   - 在驗證函式裡加上提早結束
   - 用貪婪做法讓驗證維持在 O(n)

**面試時可以說的話：**
- 「這是一題對答案空間做二分搜尋的問題」
- 「我會在 [min, max] 上做二分搜尋，並寫一個輔助函式來驗證」
- 「答案具有單調性：若 X 可行，更大的 X 也一定可行」
- 「時間複雜度：O(n × log(range))，其中 n 是驗證的成本」

---

## 總結

- **三項前提** — 有界的答案範圍、*單調*的可行性判定式，以及能在 `O(n)` / `O(n log n)` 內跑完的驗證。少了任何一項，這題就不是這個模式。
- **只有兩種形狀** — 最小化最大值（`if valid: right = mid`，回傳 `left`）與最大化最小值（`if valid: left = mid`、`mid = left + (right - left + 1) / 2`，回傳 `left`）。其餘的差別全在判定式。
- **邊界配方** — 「把陣列元素切分／裝運／分配成 K 組並最小化最大值」⇒ `left = max(nums)`、`right = sum(nums)`。「速率／除數／天數」⇒ `left = 1`、`right = max(nums)`。
- **同一套骨架，不同的判定式** — 貪婪掃描（LC 410、1011、875、1482）、在值域上計數（LC 378、287、1539），或走一趟圖（LC 1631、LC 778）。
- 面試時把它講出來：*「答案在可行性上是單調的，所以我會對答案空間做二分搜尋，並把 `isValid` 寫成輔助函式。」*
