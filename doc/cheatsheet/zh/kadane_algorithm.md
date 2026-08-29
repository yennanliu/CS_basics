# Kadane's Algorithm（卡登演算法）

> **範圍** — 深入最大子陣列這個題型家族：Kadane 本體與它的各種變形（乘積、環狀、可刪除一個元素、二維）。
> **另見**：[dp_pattern.md](./dp_pattern.md) — 把 Kadane 濃縮成一頁模板（§1）；[dp.md](./dp.md) — 更完整的 DP 目錄；[stock_trading.md](./stock_trading.md) — 同一副骨架，換成買賣股票的故事來講。

- **核心想法**：用動態規劃在 O(n) 時間內，求出連續子陣列的最大和／最大乘積
- **什麼時候用**：最大子陣列和、陣列上的最佳化問題、乘積類的變形
- **代表性 LeetCode 題目**：LC 53、LC 152、LC 918、LC 1186、LC 121、LC 134、LC 122
- **資料結構**：陣列，加上追蹤區域／全域最大值的幾個變數
- **典型狀態**：以當前位置結尾的最大值 vs 目前為止的全域最大值

**時間複雜度：** O(n) — 單趟掃描
**空間複雜度：** O(1) — 只需要幾個變數

## LeetCode 題目清單

- [Dynamic Programming](https://leetcode.com/problem-list/dynamic-programming/)
- [Array](https://leetcode.com/problem-list/array/)

## 0) 概念

### 0-0) 核心原理

Kadane 演算法是一個很優雅的方法：只掃一趟，就能算出陣列中「以每個位置結尾」的最大子陣列和。

**每一步要做的決定：**
- `current_max = max(nums[i], current_max + nums[i])`
  - `nums[i]`：從當前元素重新開一個子陣列
  - `current_max + nums[i]`：延續既有的子陣列

**關鍵洞見：**
- 在每個位置只問一件事：「該延續目前的子陣列，還是重新開始？」
- 目前的和小於 0，就代表重新開始比較划算
- 全域最大值負責記住「目前為止看過最好的子陣列」

### 0-1) 題型分類

1. **最大子陣列和** — 經典 Kadane（LC 53）
2. **最大乘積子陣列** — 改寫成乘法版（LC 152）
3. **環狀陣列最大值** — 要處理繞回頭的情況（LC 918）
4. **最多刪一個元素的最大值** — 允許移除一個元素（LC 1186）
5. **二維 Kadane** — 延伸到矩陣，求最大矩形和
6. **股票交易** — 最大差值／利潤子陣列（LC 121）

### 0-2) 演算法模式／模板

**基本 Kadane 演算法（求和）：**

```python
# Python
def kadane(nums):
    if not nums:
        return 0

    current_max = global_max = nums[0]

    for i in range(1, len(nums)):
        # At each position: extend current subarray OR start new subarray
        current_max = max(nums[i], current_max + nums[i])
        global_max = max(global_max, current_max)

    return global_max
```

```java
// Java
public int kadane(int[] nums) {
    if (nums == null || nums.length == 0) {
        return 0;
    }

    int currentMax = nums[0];
    int globalMax = nums[0];

    // Start from index 1 since we initialized with nums[0]
    for (int i = 1; i < nums.length; i++) {
        currentMax = Math.max(nums[i], currentMax + nums[i]);
        globalMax = Math.max(globalMax, currentMax);
    }

    return globalMax;
}
```

**需要留意的邊界情況：**
- 全部都是負數 → 回傳最大的那個單一元素
- 空陣列 → 依題目要求決定怎麼處理
- 只有一個元素 → 直接回傳該元素

---

## 1) 各模式的實作

### 1-1) 經典最大子陣列和（LC 53）

**題目：** 找出總和最大的連續子陣列。

```python
# Python
def maxSubArray(nums):
    """
    Time: O(n)
    Space: O(1)
    """
    current_sum = max_sum = nums[0]

    for i in range(1, len(nums)):
        current_sum = max(nums[i], current_sum + nums[i])
        max_sum = max(max_sum, current_sum)

    return max_sum
```

```java
// Java
// LC 53 - Maximum Subarray
public int maxSubArray(int[] nums) {
    if (nums == null || nums.length == 0) {
        return 0;
    }

    /**
     * time = O(N)
     * space = O(1)
     */
    int currentSum = nums[0];
    int maxSum = nums[0];

    for (int i = 1; i < nums.length; i++) {
        // Choose: start new subarray or extend current
        currentSum = Math.max(nums[i], currentSum + nums[i]);
        maxSum = Math.max(maxSum, currentSum);
    }

    return maxSum;
}
```

---

### 1-2) 最大子陣列和 + 記錄索引

**變形：** 不只回傳總和，還要回傳最大子陣列的起訖索引。

```python
# Python
def maxSubArrayWithIndices(nums):
    """
    Time: O(n)
    Space: O(1)
    Returns: (max_sum, start_index, end_index)
    """
    current_sum = max_sum = nums[0]
    start = end = temp_start = 0

    for i in range(1, len(nums)):
        if current_sum < 0:
            current_sum = nums[i]
            temp_start = i
        else:
            current_sum += nums[i]

        if current_sum > max_sum:
            max_sum = current_sum
            start = temp_start
            end = i

    return max_sum, start, end
```

```java
// Java
public class SubarrayResult {
    int sum;
    int start;
    int end;

    SubarrayResult(int sum, int start, int end) {
        this.sum = sum;
        this.start = start;
        this.end = end;
    }
}

public SubarrayResult maxSubArrayWithIndices(int[] nums) {
    int currentSum = nums[0];
    int maxSum = nums[0];
    int start = 0, end = 0, tempStart = 0;

    for (int i = 1; i < nums.length; i++) {
        if (currentSum < 0) {
            currentSum = nums[i];
            tempStart = i;
        } else {
            currentSum += nums[i];
        }

        if (currentSum > maxSum) {
            maxSum = currentSum;
            start = tempStart;
            end = i;
        }
    }

    return new SubarrayResult(maxSum, start, end);
}
```

---

### 1-3) 最大乘積子陣列（LC 152）

**關鍵洞見：** 最大值和最小值都要追蹤，因為負 × 負 = 正。

**演算法：**
- 每一步有三個選擇：
  1. 從 i 重開一個子陣列 → 只取 nums[i]
  2. 延續前一個最大乘積 → nums[i] × maxProd
  3. 延續前一個最小乘積 → nums[i] × minProd

```python
# Python
def maxProduct(nums):
    """
    Time: O(n)
    Space: O(1)
    """
    if not nums:
        return 0

    max_prod = min_prod = result = nums[0]

    for i in range(1, len(nums)):
        if nums[i] < 0:
            # Swap max and min when multiplying by negative
            max_prod, min_prod = min_prod, max_prod

        max_prod = max(nums[i], max_prod * nums[i])
        min_prod = min(nums[i], min_prod * nums[i])

        result = max(result, max_prod)

    return result
```

```java
// Java
// LC 152 - Maximum Product Subarray
public int maxProduct(int[] nums) {
    if (nums == null || nums.length == 0) {
        return 0;
    }

    /**
     * time = O(N)
     * space = O(1)
     *
     * Key: Track both max and min products
     * - maxProd: maximum product ending at current position
     * - minProd: minimum product ending at current position
     *            (needed because negative × negative = positive)
     */
    int maxProd = nums[0];
    int minProd = nums[0];
    int result = nums[0];

    for (int i = 1; i < nums.length; i++) {
        // Cache maxProd before updating (needed for minProd calculation)
        int temp = maxProd;

        // Update maxProd: choose from 3 options
        maxProd = Math.max(nums[i],
                    Math.max(nums[i] * maxProd, nums[i] * minProd));

        // Update minProd: choose from 3 options
        minProd = Math.min(nums[i],
                    Math.min(nums[i] * temp, nums[i] * minProd));

        // Update global result
        result = Math.max(result, maxProd);
    }

    return result;
}
```

**逐步範例：** `nums = [2, 3, -2, 4]`

```text
Index | nums[i] | maxProd              | minProd              | result
----------------------------------------------------------------------
  0   |    2    |     2                |     2                |   2
  1   |    3    | max(3,6,6)=6        | min(3,6,6)=3         |   6
  2   |   -2    | max(-2,-12,-6)=-2   | min(-2,-12,-6)=-12   |   6
  3   |    4    | max(4,-8,-48)=4     | min(4,-8,-48)=-48    |   6
```

**最終答案：** 6（子陣列 [2,3] 的乘積是 6）

---

### 1-4) 環狀最大子陣列（LC 918）

**關鍵洞見：** 最大值只可能出現在兩種情況：
1. **一般情況**：最大子陣列沒有繞回頭
2. **環狀情況**：最大子陣列跨越了頭尾邊界

**環狀最大值 = 總和 − 最小子陣列**

```python
# Python
def maxSubarraySumCircular(nums):
    """
    Time: O(n)
    Space: O(1)
    """
    def kadane(arr):
        current = maximum = arr[0]
        for i in range(1, len(arr)):
            current = max(arr[i], current + arr[i])
            maximum = max(maximum, current)
        return maximum

    # Case 1: Maximum subarray is normal (non-circular)
    max_normal = kadane(nums)

    # Case 2: Maximum subarray is circular
    # Circular max = Total sum - minimum subarray
    total_sum = sum(nums)

    # Find minimum subarray (negate array and find max)
    negated = [-x for x in nums]
    max_negated = kadane(negated)
    min_subarray = -max_negated

    max_circular = total_sum - min_subarray

    # Edge case: if all numbers are negative
    if max_circular == 0:
        return max_normal

    return max(max_normal, max_circular)
```

```java
// Java
// LC 918 - Maximum Sum Circular Subarray
public int maxSubarraySumCircular(int[] nums) {
    /**
     * time = O(N)
     * space = O(1)
     */
    int curMax = 0;
    int curMin = 0;
    int maxSum = nums[0];
    int minSum = nums[0];
    int totalSum = 0;

    for (int num : nums) {
        // Normal Kadane's for maximum
        curMax = Math.max(curMax, 0) + num;
        maxSum = Math.max(maxSum, curMax);

        // Kadane's for minimum (find minimum subarray)
        curMin = Math.min(curMin, 0) + num;
        minSum = Math.min(minSum, curMin);

        totalSum += num;
    }

    // Edge case: all negative numbers
    if (totalSum == minSum) {
        return maxSum;
    }

    // Return max of normal case or circular case
    return Math.max(maxSum, totalSum - minSum);
}
```

**為什麼成立：**
- 如果最大子陣列繞了回頭，那把中間那段（最小子陣列）挖掉，剩下的就是最大值
- `總和 − 最小子陣列 = 最大環狀子陣列`

---

### 1-5) 最多刪一個元素的最大子陣列和（LC 1186）

**題目：** 最多可以刪掉一個元素，讓子陣列和最大。

```python
# Python
def maximumSum(arr):
    """
    Time: O(n)
    Space: O(1)

    Track two states:
    - no_delete: max sum without any deletion
    - one_delete: max sum with exactly one deletion
    """
    n = len(arr)
    no_delete = arr[0]
    one_delete = 0
    result = arr[0]

    for i in range(1, n):
        # With one deletion: either delete current or extend previous deletion
        one_delete = max(one_delete + arr[i], no_delete)

        # Without deletion: standard Kadane's
        no_delete = max(arr[i], no_delete + arr[i])

        result = max(result, max(no_delete, one_delete))

    return result
```

```java
// Java
// LC 1186 - Maximum Subarray Sum with One Deletion
public int maximumSum(int[] arr) {
    /**
     * time = O(N)
     * space = O(1)
     */
    int n = arr.length;
    int noDelete = arr[0];
    int oneDelete = 0;
    int result = arr[0];

    for (int i = 1; i < n; i++) {
        // With one deletion: max of (extend with deletion, delete current)
        oneDelete = Math.max(oneDelete + arr[i], noDelete);

        // Without deletion: standard Kadane's
        noDelete = Math.max(arr[i], noDelete + arr[i]);

        result = Math.max(result, Math.max(noDelete, oneDelete));
    }

    return result;
}
```

---

### 1-6) 雙狀態機 Kadane（LC 714）

**模式：** 當每個元素可以處在兩種 *模式* 之一時，就**平行跑兩個 Kadane 累加器**，並讓它們互相餵資料。這是 LC 121/122 的一般化：不再只留一個 `localMax`，而是每個狀態各留一個目前最佳值。

**遞迴式**（`cash` = 手上沒股票時的最佳利潤，`hold` = 手上持股時的最佳利潤）：

```text
cash[i] = max(cash[i-1], hold[i-1] + prices[i] - fee)   // sell today (fee charged on sell)
hold[i] = max(hold[i-1], cash[i]   - prices[i])         // buy today
answer  = cash[n-1]                                     // never end holding a stock
```

**關鍵想法：** 這兩個狀態就是把「延續 vs 重開」的決定依模式拆成兩份 — `hold` 從目前看過最好的 `cash` 重新買進，`cash` 則把目前最好的 `hold` 結清。

```java
// java
// LC 714 - Best Time to Buy and Sell Stock with Transaction Fee
// IDEA: two-state Kadane — cash (no stock) vs hold (holding stock)
// time = O(N), space = O(1)
public int maxProfit(int[] prices, int fee) {
    int cash = 0;               // best profit while holding nothing
    int hold = -prices[0];      // best profit while holding a stock

    for (int i = 1; i < prices.length; i++) {
        // sell today (pay fee once, on sell) or keep resting
        cash = Math.max(cash, hold + prices[i] - fee);
        // buy today (from cash state) or keep holding
        hold = Math.max(hold, cash - prices[i]);
    }
    return cash;
}
```

```python
# python
# LC 714 - Best Time to Buy and Sell Stock with Transaction Fee
# IDEA: two-state Kadane -- cash (no stock) vs hold (holding stock)
# time = O(N), space = O(1)
def maxProfit(prices, fee):
    cash = 0              # best profit while holding nothing
    hold = -prices[0]     # best profit while holding a stock

    for i in range(1, len(prices)):
        cash = max(cash, hold + prices[i] - fee)  # sell (fee paid on sell)
        hold = max(hold, cash - prices[i])        # buy

    return cash
```

**為什麼在更新 `hold` 時可以安心沿用 `cash`：** 同一天買進又賣出淨賺 `-fee`，永遠不會讓答案變好，所以「用舊的 `cash` 還是新的 `cash`」這個差別不會影響結果。

**同一副骨架，不同狀態：**
- LC 121（只能交易一次）→ `hold = max(hold, -prices[i])`（不能把賺到的利潤再投入）
- LC 122（無限次、無手續費）→ 把 `- fee` 拿掉
- LC 714（無限次 + 手續費）→ 就是上面那段程式碼

---

### 1-7) 二維 Kadane／格子上的「以此結尾的最佳解」（LC 221）

**兩種截然不同的二維延伸 — 千萬別混在一起：**

| 目標 | 技巧 | 複雜度 |
|------|-----------|------------|
| 最大**和矩形** | 固定上下兩列 → 把欄壓縮成一維 → 跑一維 Kadane | O(rows² × cols) |
| 最大**全 1 正方形**（LC 221） | `dp[i][j]` = *以* (i,j) *結尾* 的最佳正方形；旁邊另外追蹤全域最大值 | O(M × N) |

**(a) 通用二維 Kadane — 最大和矩形（列壓縮）**

```java
// java
// IDEA: fix top/bottom row pair, compress columns into a 1-D array, run 1-D Kadane
// time = O(rows^2 * cols), space = O(cols)
public int maxSumRectangle(int[][] mat) {
    int rows = mat.length, cols = mat[0].length;
    int best = Integer.MIN_VALUE;

    for (int top = 0; top < rows; top++) {
        int[] colSum = new int[cols];               // reset for each new top row
        for (int bottom = top; bottom < rows; bottom++) {
            for (int c = 0; c < cols; c++) {
                colSum[c] += mat[bottom][c];        // rows [top..bottom] collapsed to 1-D
            }
            best = Math.max(best, kadane1D(colSum));
        }
    }
    return best;
}

private int kadane1D(int[] arr) {
    int cur = arr[0], best = arr[0];
    for (int i = 1; i < arr.length; i++) {
        cur = Math.max(arr[i], cur + arr[i]);
        best = Math.max(best, cur);
    }
    return best;
}
```

```python
# python
# IDEA: fix top/bottom row pair, compress columns to 1-D, run Kadane
# time = O(rows^2 * cols), space = O(cols)
def max_sum_rectangle(mat):
    rows, cols = len(mat), len(mat[0])
    best = float('-inf')

    for top in range(rows):
        col_sum = [0] * cols
        for bottom in range(top, rows):
            for c in range(cols):
                col_sum[c] += mat[bottom][c]
            cur = gmax = col_sum[0]
            for c in range(1, cols):
                cur = max(col_sum[c], cur + col_sum[c])
                gmax = max(gmax, cur)
            best = max(best, gmax)

    return best
```

**(b) LC 221 最大正方形 — 把「以此結尾 + 全域最大值」搬到二維**

**遞迴式：** 當 `matrix[i][j] == '1'` 時 `dp[i][j] = min(dp[i-1][j], dp[i][j-1], dp[i-1][j-1]) + 1`，否則為 `0`。

形狀和 Kadane 一模一樣：每個格子有一個*區域*最佳解，旁邊掛一個*全域*最大值，而當連續段被打斷時（碰到 `'0'`）就硬性**歸零** — 這就是「重新開始」的二維版本。

```java
// java
// LC 221 - Maximal Square
// IDEA: 2-D "best ending here" — dp[i][j] = side of largest square whose bottom-right is (i,j)
// time = O(M*N), space = O(N)  (rolling 1-D row)
public int maximalSquare(char[][] matrix) {
    int m = matrix.length, n = matrix[0].length;
    int[] dp = new int[n + 1];      // dp[j] holds previous row's value at column j-1
    int best = 0;

    for (int i = 0; i < m; i++) {
        int prevDiag = 0;           // dp[i-1][j-1]
        for (int j = 1; j <= n; j++) {
            int temp = dp[j];       // save dp[i-1][j] before overwrite
            if (matrix[i][j - 1] == '1') {
                dp[j] = Math.min(Math.min(dp[j], dp[j - 1]), prevDiag) + 1;
                best = Math.max(best, dp[j]);
            } else {
                dp[j] = 0;          // run broken -> restart (Kadane's "start fresh")
            }
            prevDiag = temp;
        }
    }
    return best * best;             // question asks for AREA, not side length
}
```

```python
# python
# LC 221 - Maximal Square
# IDEA: 2-D "best ending here" -- dp[j] = side of largest square with bottom-right at (i, j)
# time = O(M*N), space = O(N)
def maximalSquare(matrix):
    m, n = len(matrix), len(matrix[0])
    dp = [0] * (n + 1)
    best = 0

    for i in range(m):
        prev_diag = 0                     # dp[i-1][j-1]
        for j in range(1, n + 1):
            temp = dp[j]                  # dp[i-1][j]
            if matrix[i][j - 1] == '1':
                dp[j] = min(dp[j], dp[j - 1], prev_diag) + 1
                best = max(best, dp[j])
            else:
                dp[j] = 0                 # start fresh
            prev_diag = temp

    return best * best                    # AREA
```

**容易踩到的坑：**
- 要回傳 `side * side`（面積），不是邊長。
- 取三個鄰居的 `min` 才逼得出*正方形*；改成 `max` 或只看兩個鄰居，就是最經典的那個 bug。
- 要在覆寫 `dp[j]` *之前* 先把 `prevDiag` 存起來，否則對角線會讀到當前這一列的值。

---

## 2) 相關 LeetCode 題目

### Kadane 演算法的直接應用

| 題目 | 難度 | 模式 | 關鍵洞見 |
|---------|------------|---------|-------------|
| LC 53 | Easy | 最大子陣列 | 經典 Kadane |
| LC 152 | Medium | 最大乘積 | 同時追蹤最大與最小 |
| LC 918 | Medium | 環狀子陣列 | 總和 − 最小值 |
| LC 1186 | Medium | 可刪一個元素 | 雙狀態 DP |
| LC 121 | Easy | 股票交易 | 最大差值 |
| LC 122 | Medium | 股票交易 II | 把所有漲幅加總 |
| LC 134 | Medium | 加油站 | 環狀 + 貪婪 |
| LC 1191 | Medium | K 次串接 | 重複陣列 |
| LC 714 | Medium | 股票交易 + 手續費 | 雙狀態機（cash / hold） |
| LC 221 | Medium | 最大正方形（二維） | 格子上的「以此結尾」+ 全域最大值 |

### 相關模式

- **LC 325** — 和為 k 的最長子陣列（前綴和 + HashMap）
- **LC 560** — 和為 K 的子陣列（前綴和 + HashMap）
- **LC 862** — 和至少為 K 的最短子陣列（單調佇列 + 前綴和）
- **LC 1004** — 最大連續 1 的個數 III（滑動視窗）
- **LC 238** — 除自身以外陣列的乘積（前綴／後綴乘積 — 也是滾動累加器，但*沒有*「延續 vs 重開」的決定，所以它不算 Kadane）
- **LC 42** — 接雨水（前綴／後綴滾動最大值 — 滾動極值的表親；它同時保留*兩個*方向，而不是單一區域最佳解）

---

## 3) 常見錯誤與邊界情況

### 🚫 常見錯誤

1. **忘了處理邊界情況**
   ```java
   // ❌ WRONG: Not handling empty arrays
   if (nums.length == 0) return 0;

   // ✅ CORRECT: Check for null and empty
   if (nums == null || nums.length == 0) return 0;
   ```

2. **全負數陣列**
   ```python
   # ❌ WRONG: Returning 0 for all negative
   if max_sum < 0:
       return 0

   # ✅ CORRECT: Return maximum element
   # Kadane's handles this naturally by initializing with nums[0]
   ```

3. **乘積題：沒有追蹤最小值**
   ```java
   // ❌ WRONG: Only tracking max product
   maxProd = Math.max(nums[i], maxProd * nums[i]);

   // ✅ CORRECT: Track both max and min
   maxProd = Math.max(nums[i], Math.max(maxProd * nums[i], minProd * nums[i]));
   ```

4. **記錄索引：差一錯誤**
   ```python
   # ❌ WRONG: Starting loop from 0 when initialized with nums[0]
   for i in range(len(nums)):  # Recounts nums[0]

   # ✅ CORRECT: Start from 1
   for i in range(1, len(nums)):
   ```

5. **環狀陣列：漏掉邊界情況**
   ```java
   // ❌ WRONG: Not checking if all numbers are negative
   return Math.max(maxSum, totalSum - minSum);

   // ✅ CORRECT: Handle case where minSum = totalSum
   if (totalSum == minSum) return maxSum;
   return Math.max(maxSum, totalSum - minSum);
   ```

### ⚠️ 邊界情況

1. **空陣列**：回傳 0 或丟出例外
2. **單一元素**：回傳該元素
3. **全部為負**：回傳最大的元素（負得最少的那個）
4. **全部為正**：回傳整個陣列的總和
5. **含有 0**：會把乘積的計算重置
6. **環狀且全負**：只能採用標準 Kadane 的結果

---

## 4) 面試技巧與複雜度分析

### 💡 面試策略

#### 辨識訊號：
- 「最大子陣列和」→ 經典 Kadane
- 「最大乘積子陣列」→ 改寫版 Kadane，要追蹤最小值
- 「環狀陣列」→ 一般情況與環狀情況都要考慮
- 「可刪除／可跳過」→ 追蹤多個狀態
- 「股票利潤」→ Kadane 的變形

#### 解題框架：

```text
1. Identify the optimization metric:
   ├─ Sum → Standard Kadane's
   ├─ Product → Track max and min
   ├─ With constraints → Multiple state tracking
   └─ Circular → Consider wraparound

2. Choose the pattern:
   ├─ Single pass? → O(n) Kadane's
   ├─ Need indices? → Track start/end
   ├─ Need actual subarray? → Store elements
   └─ Multiple subarrays? → Modified approach

3. Handle edge cases:
   ├─ All negative → Maximum element
   ├─ Empty array → Return 0 or error
   ├─ Single element → Return element
   └─ Zeros → Reset logic for product
```

### 📊 複雜度分析

| 變形 | 時間 | 空間 | 關鍵操作 |
|---------|------|-------|---------------|
| 標準 Kadane | O(n) | O(1) | max(cur, cur+next) |
| 乘積版 Kadane | O(n) | O(1) | 同時追蹤最大與最小 |
| 環狀 | O(n) | O(1) | 掃兩趟 |
| 可刪一個元素 | O(n) | O(1) | 雙狀態 DP |
| 二維 Kadane | O(n²m) | O(m) | 列壓縮 |

### 🎯 面試時可以講的重點

1. **Kadane 為什麼會對：**
   - 「在每個位置，我們只是在決定要延續當前的子陣列，還是重新開始」
   - 「和變成負的，就代表重新開始比較好」
   - 「這是最佳解，因為我們在 O(n) 時間內已經考慮過所有可能」

2. **乘積版的直覺：**
   - 「最大和最小都要追蹤，因為負 × 負 = 正」
   - 「一個很小的負數，乘上另一個負數之後可能變得很大」
   - 「這就是在處理乘法的非線性行為」

3. **環狀陣列的策略：**
   - 「最大值要嘛落在中間（一般情況），要嘛繞過頭尾（環狀情況）」
   - 「環狀最大值 = 總和 − 最小子陣列」
   - 「兩個都算，回傳比較大的那個」

4. **空間最佳化：**
   - 「不需要 DP 陣列，只要追蹤兩個變數：current_max 和 global_max」
   - 「空間從 O(n) 降到 O(1)」

### 🔧 最佳化技巧

1. **重置策略**：當 current_sum < 0 時，重置成當前元素
2. **空間最佳化**：只需要 O(1) 空間，不必開 DP 陣列
3. **提早結束**：若全部為正，直接回傳整個陣列的總和
4. **乘積題的最佳化**：
   - 把 0 另外處理
   - 數一下負數的個數，用奇偶性判斷

### 📚 相關模式

- **滑動視窗**：固定長度子陣列的最大值
- **前綴和**：區間和查詢
- **動態規劃**：一般化的子陣列最佳化
- **分治法**：最大子陣列的另一種 O(n log n) 解法

---

## 5) 參考資料

- [Wikipedia: Maximum Subarray Problem](https://zh.wikipedia.org/zh-tw/%E6%9C%80%E5%A4%A7%E5%AD%90%E6%95%B0%E5%88%97%E9%97%AE%E9%A2%98)
- [Flydean: Kadane's Algorithm](https://www.flydean.com/interview/arithmetic/arithmetic-Kadane/)
- [LeetCode: Dynamic Programming Patterns](https://leetcode.com/discuss/study-guide/1308617/Dynamic-Programming-Patterns)

---

## 總結

**核心原則：**
- ✅ 單趟 O(n) 掃描，解決子陣列最佳化問題
- ✅ 關鍵決定：延續當前子陣列，或重新開始
- ✅ 追蹤 current_max（區域）與 global_max（全域）
- ✅ 遇到乘積：最大與最小都要追蹤

**什麼時候用：**
- 連續子陣列的最大／最小和或乘積
- 股票交易的利潤最大化
- 元素必須連續的最佳化問題
- 要求 O(n) 時間、O(1) 空間的問題

**主要變形：**
1. **求和** — 經典 Kadane
2. **乘積** — 同時追蹤最大與最小
3. **環狀** — 要考慮繞回頭
4. **可刪除** — 多狀態 DP

**面試重點：**
- 弄懂「延續 vs 重新開始」這個決定
- 處理好邊界情況（全負、空陣列、單一元素）
- 知道乘積版一定要追蹤最小值
- 熟練環狀陣列的做法（總和 − 最小值）

## 6) LC 範例

### 6-1) 最大子陣列（LC 53）— Kadane 演算法
> 追蹤以每個索引結尾的區域最大值；當區域最大值掉到比當前元素還小時就重置。

```java
// LC 53 - Maximum Subarray
// IDEA: Kadane — localMax = max(nums[i], nums[i] + localMax)
// time = O(N), space = O(1)
public int maxSubArray(int[] nums) {
    int localMax = nums[0], globalMax = nums[0];
    for (int i = 1; i < nums.length; i++) {
        localMax = Math.max(nums[i], nums[i] + localMax);
        globalMax = Math.max(globalMax, localMax);
    }
    return globalMax;
}
```

### 6-2) 最大乘積子陣列（LC 152）— 同時追蹤最小與最大
> 最大與最小乘積都要追蹤（最小值乘上負數之後可能翻成最大值）。

```java
// LC 152 - Maximum Product Subarray
// IDEA: Kadane variant — track both curMax and curMin for sign flips
// time = O(N), space = O(1)
public int maxProduct(int[] nums) {
    int curMax = nums[0], curMin = nums[0], globalMax = nums[0];
    for (int i = 1; i < nums.length; i++) {
        int temp = curMax;
        curMax = Math.max(nums[i], Math.max(curMax * nums[i], curMin * nums[i]));
        curMin = Math.min(nums[i], Math.min(temp * nums[i], curMin * nums[i]));
        globalMax = Math.max(globalMax, curMax);
    }
    return globalMax;
}
```

### 6-3) 環狀子陣列的最大和（LC 918）— Kadane + 總和技巧
> 最大環狀子陣列 = max(一般最大子陣列, 總和 − 最小子陣列)。

```java
// LC 918 - Maximum Sum Circular Subarray
// IDEA: max circular = max(kadane result, total - minSubarray)
// time = O(N), space = O(1)
public int maxSubarraySumCircular(int[] nums) {
    int totalSum = 0, curMax = 0, curMin = 0, maxSum = nums[0], minSum = nums[0];
    for (int num : nums) {
        curMax = Math.max(curMax + num, num);
        maxSum = Math.max(maxSum, curMax);
        curMin = Math.min(curMin + num, num);
        minSum = Math.min(minSum, curMin);
        totalSum += num;
    }
    // if all negative, maxSum is the answer (totalSum - minSum = 0 is invalid)
    return maxSum > 0 ? Math.max(maxSum, totalSum - minSum) : maxSum;
}
```

### 6-4) 買賣股票的最佳時機（LC 121）— Kadane 變形
> 追蹤到目前為止的最低價；最大利潤 = 當前價格 − 滾動最低價。

```java
// LC 121 - Best Time to Buy and Sell Stock
// IDEA: Kadane variant — running minimum, update max profit each step
// time = O(N), space = O(1)
public int maxProfit(int[] prices) {
    int minPrice = Integer.MAX_VALUE, maxProfit = 0;
    for (int price : prices) {
        minPrice = Math.min(minPrice, price);
        maxProfit = Math.max(maxProfit, price - minPrice);
    }
    return maxProfit;
}
```

### 6-5) 最多刪一個元素的最大子陣列和（LC 1186）— 兩個 DP 狀態
> dp0[i] = 以 i 結尾且未刪除的最大和；dp1[i] = 已用掉一次刪除的最大和。

```java
// LC 1186 - Maximum Subarray Sum with One Deletion
// IDEA: Two DP states — dp0 (no deletion), dp1 (one deletion used)
// time = O(N), space = O(1)
public int maximumSum(int[] arr) {
    int dp0 = arr[0], dp1 = 0, ans = arr[0];
    for (int i = 1; i < arr.length; i++) {
        dp1 = Math.max(dp0, dp1 + arr[i]);  // delete arr[i] or extend with deletion
        dp0 = Math.max(arr[i], dp0 + arr[i]);
        ans = Math.max(ans, Math.max(dp0, dp1));
    }
    return ans;
}
```

### 6-6) 最長湍流子陣列（LC 978）— Kadane 變形
> 分別追蹤上升與下降交替視窗的長度；相等時歸零重來。

```java
// LC 978 - Longest Turbulent Subarray
// IDEA: Kadane variant — track alternating inc/dec window lengths
// time = O(N), space = O(1)
public int maxTurbulenceSize(int[] arr) {
    int inc = 1, dec = 1, ans = 1;
    for (int i = 1; i < arr.length; i++) {
        if (arr[i] > arr[i-1])      { inc = dec + 1; dec = 1; }
        else if (arr[i] < arr[i-1]) { dec = inc + 1; inc = 1; }
        else                         { inc = 1; dec = 1; }
        ans = Math.max(ans, Math.max(inc, dec));
    }
    return ans;
}
```

### 6-7) 加油站（LC 134）— 環狀上的貪婪／Kadane
> 若總油量 >= 總消耗，解一定存在；從第一個由虧轉盈的重置點出發。

```java
// LC 134 - Gas Station
// IDEA: Greedy — if total surplus >= 0, start from where tank went negative
// time = O(N), space = O(1)
public int canCompleteCircuit(int[] gas, int[] cost) {
    int total = 0, curr = 0, start = 0;
    for (int i = 0; i < gas.length; i++) {
        int diff = gas[i] - cost[i];
        total += diff;
        curr  += diff;
        if (curr < 0) { start = i + 1; curr = 0; }
    }
    return total >= 0 ? start : -1;
}
```

### 6-8) 乘積為正數的最長子陣列長度（LC 1567）— 分別追蹤正／負乘積長度
> 分開追蹤乘積為正與為負的長度；碰到負數時兩者互換。

```java
// LC 1567 - Maximum Length of Subarray with Positive Product
// IDEA: pos = length with positive product, neg = with negative product; swap on negatives
// time = O(N), space = O(1)
public int getMaxLen(int[] nums) {
    int pos = 0, neg = 0, ans = 0;
    for (int num : nums) {
        if (num == 0) { pos = 0; neg = 0; }
        else if (num > 0) { pos++; neg = neg > 0 ? neg + 1 : 0; }
        else { int tmp = pos; pos = neg > 0 ? neg + 1 : 0; neg = tmp + 1; }
        ans = Math.max(ans, pos);
    }
    return ans;
}
```

### 6-9) 拼接陣列的最大分數（LC 2321）— 對差值跑 Kadane
> 交換某段子陣列能拿到的最大增益 = (nums2[i] - nums1[i]) 的最大子陣列和。

```java
// LC 2321 - Maximum Score of Spliced Array
// IDEA: Kadane on difference arrays — gain from swapping a subarray
// time = O(N), space = O(1)
public int[] maximumsSplicedArray(int[] nums1, int[] nums2) {
    int sum1 = 0, sum2 = 0;
    for (int i = 0; i < nums1.length; i++) { sum1 += nums1[i]; sum2 += nums2[i]; }
    return new int[]{ sum1 + maxGain(nums2, nums1), sum2 + maxGain(nums1, nums2) };
}
private int maxGain(int[] a, int[] b) {  // max(b[i]-a[i]) subarray sum
    int curr = 0, best = 0;
    for (int i = 0; i < a.length; i++) {
        curr = Math.max(0, curr + b[i] - a[i]);
        best = Math.max(best, curr);
    }
    return best;
}
```

### 6-10) K 次串接的最大和（LC 1191）— Kadane + 數學
> 當 k >= 2：答案 = maxSubarray(兩份複製) + max(0, 總和) × (k − 2)。

```java
// LC 1191 - K-Concatenation Maximum Sum
// IDEA: Kadane on 1 or 2 copies; if total positive, add total*(k-2)
// time = O(N), space = O(1)
public int kConcatenationMaxSum(int[] arr, int k) {
    int MOD = 1_000_000_007;
    long total = 0;
    for (int x : arr) total += x;
    long base = kadane(arr, Math.min(k, 2));
    long ans = base + (k > 2 && total > 0 ? total % MOD * (k - 2) % MOD : 0);
    return (int)(ans % MOD);
}
private long kadane(int[] arr, int repeat) {
    long curr = 0, best = 0;
    for (int t = 0; t < repeat; t++)
        for (int x : arr) { curr = Math.max(x, curr + x); best = Math.max(best, curr); }
    return best;
}
```

### 6-11) 幾乎唯一子陣列的最大和（LC 2841）— 滑動視窗版 Kadane
> 長度固定為 k 的視窗；用 HashMap 數相異元素個數；讓和最大。

```java
// LC 2841 - Almost Unique Subarray (fixed window Kadane variant)
// IDEA: Sliding window — maintain sum and frequency map for window of size k
// time = O(N), space = O(k)
public long maxSum(List<Integer> nums, int m, int k) {
    Map<Integer, Integer> freq = new HashMap<>();
    long windowSum = 0, ans = 0;
    int n = nums.size();
    for (int i = 0; i < n; i++) {
        freq.merge(nums.get(i), 1, Integer::sum);
        windowSum += nums.get(i);
        if (i >= k) {
            int out = nums.get(i - k);
            windowSum -= out;
            freq.merge(out, -1, Integer::sum);
            if (freq.get(out) == 0) freq.remove(out);
        }
        if (i >= k - 1 && freq.size() >= m) ans = Math.max(ans, windowSum);
    }
    return ans;
}
```
