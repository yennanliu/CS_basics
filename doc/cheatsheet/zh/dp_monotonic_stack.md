# 單調堆疊 + DP

> **範圍** — 單調堆疊上還掛著一個 DP 值的題型：「這個元素能撐過幾輪」、直方圖最大矩形那類面積 DP，以及最大正方形／計算正方形數量的網格遞迴式。
> **另見**：[monotonic_stack.md](./monotonic_stack.md) — 純粹處理 next greater／previous smaller 查詢的單調堆疊技巧，上面沒有 DP；[dp.md](./dp.md) — 其餘的 DP 模式。

## LeetCode 題目清單

- [Dynamic Programming](https://leetcode.com/problem-list/dynamic-programming/)

## 總覽

### 關鍵性質

- **複雜度**：`O(n)` 時間、`O(n)` 空間 —— 每個索引最多 push 一次、pop 一次，DP 值就搭在堆疊那筆資料上一起走。
- **核心想法**：暴力解是*一輪一輪模擬*刪除，`O(n^2)`。單調堆疊把整個模擬壓扁：當一個較大的元素 pop 掉一串較小的元素時，它繼承的 DP 值是被 pop 那條鏈的**最大值**，而不是重新從頭數。
- **什麼時候用**：「要幾輪陣列才會停止變化」、「最大矩形／正方形」，或任何「每個元素的答案取決於最近的較大／較小鄰居」的題目。

### 題型分類

| 題型 | 問的問題 | LC |
|----------|----------|----|
| **存活輪數** | 元素 `i` 要幾趟才會被刪掉？ | 2289 |
| **直方圖面積** | 由最近的較小柱子夾出來的最大矩形 | 84, 85 |
| **網格正方形** | 全是 1 的正方形子矩陣，最大的／總數 | 221, 1277 |
| **單趟計數型 DP** | 掃一趟找出最便宜的切割點 | 926 |

### 參考資料

- [monotonic_stack.md](./monotonic_stack.md) — 純單調堆疊技巧，上面沒疊 DP
- [dp.md](./dp.md) — 其餘的 DP 模式家族

## 模板與演算法

### 模式總覽

**什麼時候用**：每個元素的答案取決於它被左邊（或右邊）某個較大元素壓制／移除之前能「活多久」。最明顯的訊號是**一輪一輪把元素刪掉的模擬** —— 暴力解每一步都是 O(N²)；堆疊 + DP 把整個過程壓成 O(N)。

**核心想法**：
- 維護一個索引的**單調遞減堆疊**。
- `dp[i]` = 元素 `i` 在被移除前撐過的輪數（永遠不會被移除就是 0）。
- 當新元素 `nums[i]` 把較小的元素 pop 出堆疊時，那些較小元素就會被移除。關鍵洞見是：如果 `nums[i]` 必須先等某條先前被 pop 掉的鏈清乾淨，`dp[i]` 要繼承目前看過的**最大**等待時間。

**轉移式（由左往右掃）**：
```text
currentSteps = 0
while stack not empty AND nums[i] >= nums[stack.top()]:
    currentSteps = max(currentSteps, dp[stack.pop()])

if stack not empty:          // a larger element still blocks nums[i]
    dp[i] = currentSteps + 1
else:                        // nums[i] is a new global maximum — never removed
    dp[i] = 0

answer = max(dp[i]) for all i
```

**轉移式（由右往左掃 —— 另一種寫法）**：
```text
for i from n-1 down to 0:
    maxSteps = 0
    while stack not empty AND nums[i] > nums[stack.top()]:
        maxSteps = max(maxSteps + 1, dp[stack.pop()])
    dp[i] = maxSteps
    res = max(res, dp[i])
    stack.push(i)
```

---

### 模板：LC 2289 —— Steps to Make Array Non-Decreasing

**題目**：每一步都會移除所有滿足 `nums[i-1] > nums[i]` 的元素 `nums[i]`。回傳陣列變成非遞減所需的步數。

**為什麼單調堆疊 + DP 有效**：
- 每個元素最終都會被它左邊第一個比它大的元素吃掉。
- `nums[i]` 被吃掉所需的步數，等於 1 加上「`nums[i]` 和它的『兇手』之間那些較小元素」所需步數的最大值。
- 單調堆疊記的正好就是「當下的兇手是誰」。

**Java —— 由左往右（正向掃描）**：
```java
// java
// LC 2289 - Steps to Make Array Non-decreasing
// IDEA: forward scan; a popped chain hands its max survival time to the element that ate it
// time = O(n), space = O(n)
public int totalSteps(int[] nums) {
    int n = nums.length, maxSteps = 0;
    int[] dp = new int[n];
    Stack<Integer> stack = new Stack<>();   // monotonic decreasing (by value)

    for (int i = 0; i < n; i++) {
        int currentSteps = 0;

        // Pop elements that nums[i] will outlive (nums[i] >= them)
        while (!stack.isEmpty() && nums[i] >= nums[stack.peek()]) {
            currentSteps = Math.max(currentSteps, dp[stack.pop()]);
        }

        if (!stack.isEmpty()) {
            // A larger element still exists to the left → nums[i] will be removed
            dp[i] = currentSteps + 1;
            maxSteps = Math.max(maxSteps, dp[i]);
        }
        // else dp[i] = 0 (never removed)

        stack.push(i);
    }
    return maxSteps;
}
```

**Java —— 由右往左（反向掃描）**：
```java
// java
// LC 2289 - Steps to Make Array Non-decreasing
// IDEA: same recurrence scanned right-to-left; dp[i] = rounds before nums[i] disappears
// time = O(n), space = O(n)
public int totalSteps(int[] nums) {
    int n = nums.length, res = 0;
    int[] dp = new int[n];
    Stack<Integer> stack = new Stack<>();

    for (int i = n - 1; i >= 0; i--) {
        int maxSteps = 0;
        while (!stack.isEmpty() && nums[i] > nums[stack.peek()]) {
            maxSteps = Math.max(maxSteps + 1, dp[stack.pop()]);
        }
        dp[i] = maxSteps;
        res = Math.max(res, dp[i]);
        stack.push(i);
    }
    return res;
}
```

**手動追蹤：`nums = [10, 1, 2, 7, 1, 3]`**（正向掃描）

| i | nums[i] | pop 掉誰 | currentSteps | dp[i] | 堆疊（索引） |
|---|---------|------|-------------|-------|-----------------|
| 0 | 10 | — | 0 | 0 | [0] |
| 1 | 1 | 沒有（1 < 10） | 0 | **1** | [0,1] |
| 2 | 2 | pop 1（2≥1），dp[1]=1 | 1 | **2** | [0,2] |
| 3 | 7 | pop 2（7≥2），dp[2]=2 | 2 | **3** | [0,3] |
| 4 | 1 | 沒有（1 < 7） | 0 | **1** | [0,3,4] |
| 5 | 3 | pop 4（3≥1），dp[4]=1 | 1 | **2** | [0,3,5] |

答案 = **3**。

為什麼 `dp[3] = 3`？元素 `7` 得等：第 1 步先移除 `1`，第 2 步移除 `2`，到第 3 步 `10` 才吃得到 `7`。

---

### 關鍵洞見

1. **`Math.max(currentSteps, dp[stack.pop()])`** —— 當 `nums[i]` pop 掉多個元素時，它繼承的是自己必須等的那條*最長*的移除鏈，而不是最後一條。
2. **`dp[i] = 0`** 發生在堆疊為空時 —— 此時 `nums[i]` 是新的全域最大值，永遠不會被移除。
3. **堆疊不變量**（依值單調遞減）保證了還留在堆疊上的每個元素，左邊都有一個更大的元素在等著它。

---

### **最大正方形／計算正方形數量模式（LC 1277, LC 221）** 🟦

#### 🎯 模式 —— 最大正方形

| 面向 | 細節 |
|--------|--------|
| **分類** | 二維網格 DP —— 由右下角往外長 |
| **狀態** | `dp[i][j]` = 以 `(i, j)` 為右下角、**全部都是 1 的最大正方形**的邊長 |
| **轉移式** | `dp[i][j] = min(dp[i-1][j], dp[i][j-1], dp[i-1][j-1]) + 1` |
| **base case** | 第一列或第一行：`dp[i][j] = matrix[i][j]`（最多 1×1） |
| **答案（LC 1277）** | 所有 `dp[i][j]` 的總和 —— 每個值代表有幾個正方形以此為終點 |
| **答案（LC 221）** | `max(dp[i][j])²` —— 最大正方形的面積 |
| **時間** | O(m × n) |
| **空間** | 標準寫法 O(m × n)，空間最佳化後 O(n) |

#### 💡 核心想法 —— 最大正方形

**那個「魔術」轉移式**：`dp[i][j] = min(top, left, top-left) + 1`

> 如果三個鄰居都撐得起邊長為 `k` 的正方形，那 `(i, j)` 就可以當邊長 `k+1` 正方形的右下角。三者的**最小值**決定了瓶頸。

**為什麼 `dp[i][j]` 同時也是「以 `(i, j)` 結尾的正方形個數」**：
- `dp[i][j] = 3` 的格子，可以是 1×1、2×2、3×3 三種正方形的右下角
- 所以它對總數貢獻 **3**
- 把所有 `dp[i][j]` 加起來 = 所有正方形的總數（LC 1277）

```text
Matrix:       dp values:     Contribution:
0 1 1 1       0 1 1 1        0+1+1+1 = 3   (row 0)
1 1 1 1  →    1 1 2 2   →   1+1+2+2 = 6   (row 1)
0 1 1 1       0 1 2 3        0+1+2+3 = 6   (row 2)
                                  Total = 15 ✓
```

#### **Java 實作（由下而上的二維 DP）**

```java
// java
// IDEA: the square recurrence, isolated
// LC 1277: Count Square Submatrices with All Ones
public int countSquares(int[][] matrix) {
    int rows = matrix.length, cols = matrix[0].length;
    int[][] dp = new int[rows][cols];
    int result = 0;

    for (int i = 0; i < rows; i++) {
        for (int j = 0; j < cols; j++) {
            if (matrix[i][j] == 1) {
                if (i == 0 || j == 0) {
                    dp[i][j] = 1;  // first row/col: only 1×1 possible
                } else {
                    dp[i][j] = Math.min(
                        Math.min(dp[i-1][j], dp[i][j-1]),
                        dp[i-1][j-1]
                    ) + 1;
                }
                result += dp[i][j];  // dp[i][j] = count of squares ending here
            }
        }
    }
    return result;
}
```

**改用 (n+1) × (m+1) 的尺寸（省掉第一列／第一行的特例）**：
```java
// java
// LC 1277 - Count Square Submatrices with All Ones
// IDEA: dp[i][j] = side of the largest all-1 square ending at (i,j); summing dp counts them all
// time = O(m * n), space = O(m * n)
public int countSquares(int[][] matrix) {
    int row = matrix.length, col = matrix[0].length;
    int[][] dp = new int[row + 1][col + 1];  // +1 removes boundary check
    int ans = 0;
    for (int i = 0; i < row; i++) {
        for (int j = 0; j < col; j++) {
            if (matrix[i][j] == 1) {
                dp[i+1][j+1] = Math.min(
                    Math.min(dp[i][j+1], dp[i+1][j]),
                    dp[i][j]
                ) + 1;
                ans += dp[i+1][j+1];
            }
        }
    }
    return ans;
}
```

#### **空間最佳化（O(n) 的一維 DP）**

```java
// java
// LC 1277 - Count Square Submatrices with All Ones
// IDEA: same recurrence rolled onto one row; keep the old up-left value in a temp
// time = O(m * n), space = O(n)
public int countSquares(int[][] matrix) {
    int row = matrix.length, col = matrix[0].length, result = 0, prev = 0;
    int[] dp = new int[col + 1];

    for (int i = 1; i <= row; i++) {
        for (int j = 1; j <= col; j++) {
            int temp = dp[j];
            if (matrix[i-1][j-1] == 1) {
                dp[j] = 1 + Math.min(prev, Math.min(dp[j-1], dp[j]));
                result += dp[j];
            } else {
                dp[j] = 0;
            }
            prev = temp;
        }
    }
    return result;
}
```

#### **LC 1277 與 LC 221 的比較**

| 面向 | LC 1277：計算正方形數量 | LC 221：最大正方形 |
|--------|------------------------|------------------------|
| **目標** | 數出所有尺寸的所有正方形 | 找出最大的正方形 |
| **DP 轉移式** | 相同：`min(top, left, diagonal) + 1` | 相同：`min(top, left, diagonal) + 1` |
| **答案** | `sum(dp[i][j])` | `max(dp[i][j])²` |
| **關鍵洞見** | `dp[i][j]` 是以此結尾的正方形個數 | `dp[i][j]` 是邊長 |
| **難度** | Medium | Medium |

#### **為什麼是 `min` 而不是 `max`？**

```text
Consider:    dp[i-1][j] = 3   →  top supports 3×3
             dp[i][j-1] = 1   →  left supports 1×1 only
             dp[i-1][j-1] = 2  →  diagonal supports 2×2

Even though top supports 3×3, the LEFT neighbor only supports 1×1.
If you tried to make a 2×2 square ending at (i,j), the cell one
column left would need to support a 2×2 — but it only supports 1×1.
So the bottleneck is min(3, 1, 2) = 1 → dp[i][j] = 2.
```

`min` 保證正方形的三隻「手臂」同時成立。

#### **相似的 LeetCode 題目（最大正方形）** 📚

| 題目 | LC # | 關鍵差異 | 演算法 |
|---------|------|----------------|-----------|
| **Count Square Submatrices** | 1277 | 數出所有正方形（把 dp 加總） | 二維 DP（三個鄰居取 min） |
| **Maximal Square** | 221 | 找最大的正方形（dp 取 max） | 二維 DP（三個鄰居取 min） |
| **Maximal Rectangle** | 85 | 任意由 1 組成的矩形，不限正方形 | 直方圖 + 堆疊（逐列處理） |
| **Count Submatrices with All Ones** | 1504 | 所有矩形，不限正方形 | 逐列壓縮 + 前綴和 |
| **Largest Plus Sign** | 764 | 十字形而非正方形 | 四個方向各做一次 DP |
| **Minimum Path Sum** | 64 | 最小成本路徑（不是全 1 的形狀） | 二維 DP（兩個鄰居取 min） |

#### **模式辨識檢查表（最大正方形）** ✅

以下情況適用這個模式：
- ✅ 網格只由 0 和 1 組成
- ✅ 題目問的是全 1 的**正方形**（不是矩形）
- ✅ 要在 0/1 矩陣裡數正方形或找最大的正方形
- ✅ 關鍵字：「square submatrix」、「all ones」、「count squares」

**檔案參考**：`leetcode_java/src/main/java/LeetCodeJava/DynamicProgramming/CountSquareSubmatricesWithAllOnes.java`

---

### 模板：單趟 DP —— Flip String to Monotone Increasing（LC 926）

#### 🎯 模式 —— 翻轉字串

| 面向 | 細節 |
|--------|--------|
| **模式** | 單趟一維 DP，搭配兩個滾動的狀態變數 |
| **狀態** | `flips` = 讓前綴變單調所需的最少翻轉次數；`ones` = 目前為止看到的 `'1'` 個數 |
| **轉移式** | 只有遇到 `'0'` 時才觸發（跟前面的 `'1'` 衝突） |
| **時間** | O(n) |
| **空間** | O(1) |

#### 💡 核心想法 —— 翻轉字串

> 單調遞增的二進位字串長得像 `000...111`。  
> 想像你由左往右掃，同時維護一個看不見的**切割點**：左邊全部必須是 `0`，右邊全部必須是 `1`。

看到 `'1'` 時只要計數（`ones++`）—— 它還不會逼你翻轉。  
在若干個 `'1'` **之後**看到 `'0'`，就產生衝突。有兩種選擇：

1. **把這個 `'0'` 翻成 `'1'`**：成本 `flips + 1`（保留先前所有決定，再多付 1）
2. **把先前所有 `'1'` 翻成 `'0'`**：成本 `ones`（重設整段前綴，撤銷之前所有的 `'1'`）

取比較便宜的那個：`flips = min(flips + 1, ones)`

**關鍵洞見**：`ones` 就是「反悔成本」—— 如果要回頭把目前看過的全部翻成 `0`，得付多少。

#### **核心程式碼（Java）**

```java
// java
// LC 926 - Flip String to Monotone Increasing
// IDEA: one pass — either flip this 1 to 0, or flip every 1 seen so far
// time = O(n), space = O(1)
// LC 926 — O(n) time, O(1) space
public int minFlipsMonoIncr(String s) {
    int flips = 0;   // min flips to make prefix monotone
    int ones = 0;    // count of '1's seen so far

    for (char c : s.toCharArray()) {
        if (c == '1') {
            ones++;              // potential future cost if we later want all-0 prefix
        } else {                 // c == '0' — conflict with prior '1's
            // choice 1: flip this '0' → '1'  : cost = flips + 1
            // choice 2: flip all prior '1'→'0': cost = ones
            flips = Math.min(flips + 1, ones);
        }
    }

    return flips;
}
```

#### **手動追蹤：`s = "00110"`**

| i | 字元 | ones | flips（之前） | 轉移 | flips（之後） |
|---|------|------|----------------|-----------|---------------|
| 0 | `'0'` | 0 | 0 | min(0+1, 0)=0 | **0** |
| 1 | `'0'` | 0 | 0 | min(0+1, 0)=0 | **0** |
| 2 | `'1'` | 1 | 0 | ones++ | **0** |
| 3 | `'1'` | 2 | 0 | ones++ | **0** |
| 4 | `'0'` | 2 | 0 | min(0+1, 2)=1 | **1** |

結果：`1` ✅（把最後一個 `'0'` 翻成 `'1'`：`"00111"`）

#### **手動追蹤：`s = "00011000"`**

| i | 字元 | ones | flips |
|---|------|------|-------|
| 0-2 | `'0'` | 0 | 0 |
| 3-4 | `'1'` | 2 | 0 |
| 5 | `'0'` | 2 | min(0+1,2)=1 |
| 6 | `'0'` | 2 | min(1+1,2)=2 |
| 7 | `'0'` | 2 | min(2+1,2)=2 |

結果：`2` ✅（把那兩個 `'1'` 翻成 `'0'`：`"00000000"`）

#### **另一種寫法：兩趟前綴和**

```java
// java
// LC 926 - Flip String to Monotone Increasing
// IDEA: prefix-sum variant — try every split point between the 0-block and the 1-block
// time = O(n), space = O(1)
// Count total zeroes first, then scan for the best "split point"
public int minFlipsMonoIncr(String s) {
    int zeroes = 0, ones = 0;
    for (char c : s.toCharArray()) if (c == '0') zeroes++;

    int output = zeroes;  // worst case: flip all '0' → '1'
    for (char c : s.toCharArray()) {
        if (c == '0') zeroes--;      // this '0' is now on the right → must flip
        else          ones++;        // this '1' is on the left → must flip
        output = Math.min(output, zeroes + ones);
    }
    return output;
}
```

兩種寫法都是 O(n)／O(1)。面試時單趟版本比較漂亮。

#### **相似的 LeetCode 題目（翻轉字串）** 📚

| 題目 | LC # | 相似之處 | 關鍵變數 |
|---------|------|-----------|--------------|
| **Flip String to Monotone Increasing** | 926 | 完全同一個模式 | `flips`, `ones` |
| **Minimum Number of Flips to Make Binary String Alternating** | 1888 | 翻成交替樣式 | 滑動視窗 + 奇偶計數 |
| **Make Array Non-decreasing / Non-increasing** | — | 同樣的「切割點」想法 | 前綴／後綴的極值 |
| **Partition Array into Disjoint Intervals** | 915 | 左半最大值 ≤ 右半最小值 | 滾動的 max／min |
| **Maximum Subarray**（Kadane） | 53 | 滾動狀態：延續還是重開 | `maxEndingHere` |
| **Best Time to Buy and Sell Stock** | 121 | 滾動最小值（買入價） | `minPrice`, `maxProfit` |
| **Count Binary Substrings** | 696 | 掃描 0/1 的連續區段 | `prev`, `cur` 兩組計數 |

#### **模式辨識檢查表（翻轉字串）** ✅

以下情況適用這個模式：
- ✅ 要把二進位字串變成某個目標形狀（`000...111`、`010101...` 等）
- ✅ 每個位置都有**兩個選擇**，而成本取決於先前的決定
- ✅ 只需要幾個滾動計數器而不是完整歷史，所以做得到 O(1) 空間
- ✅ 關鍵字：「minimum flips」、「monotone」、「non-decreasing binary」、「partition into prefix/suffix」

**檔案參考**：`leetcode_java/src/main/java/LeetCodeJava/DynamicProgramming/FlipStringToMonotoneIncreasing.java`

## 依模式分類的題目

### 相似的 LeetCode 題目

| LC # | 題目 | 堆疊 + DP 追蹤的是什麼 | 難度 |
|------|---------|--------------------------|------------|
| **2289** | Steps to Make Array Non-Decreasing | 元素被移除前的輪數 | Medium |
| **84** | Largest Rectangle in Histogram | 前一個較小柱子的索引 | Hard |
| **85** | Maximal Rectangle | 逐列的直方圖（用到 LC 84） | Hard |
| **907** | Sum of Subarray Minimums | 每個最小元素的貢獻 | Medium |
| **1856** | Maximum Subarray Min-Product | 用單調堆疊求最大乘積 | Medium |
| **739** | Daily Temperatures | 距離下一個更暖的日子還有幾天 | Medium |
| **901** | Online Stock Span | 距離上一個更高價格過了幾天 | Medium |
| **456** | 132 Pattern | 追蹤前綴最小值 + 單調堆疊 | Medium |
| **2866** | Beautiful Towers II | 左右兩側的最大高度貢獻 | Medium |

**模式辨識檢查表**：
- ✅ 題目牽涉到一輪一輪移除／吞掉元素
- ✅ 每個元素都被第一個較大／較小的鄰居壓制
- ✅ 問「要幾步／幾輪」元素才會被消滅
- ✅ 暴力模擬會是 O(N²)，需要 O(N)
- ✅ 答案是各元素成本的最大值

**常見陷阱**：
- while 條件用 `>` 還是 `>=`，決定了相等的元素會不會互相吃掉 —— 要跟題目的移除規則對得剛剛好。
- 正向掃描時，堆疊為空的情況 `dp[i] = 0`（不用特別賦值）；忘了這件事，全域最大值的 dp 就會錯。
- 別把左掃（`>=`）和右掃（`>`）搞混 —— 它們編碼的是不同的「誰吃誰」語意。

---

## 總結

| 形狀 | 怎麼認出來 | 核心那一行 |
|-------|-----------------|-----------|
| **存活輪數** | 「重複直到陣列不再變化」 | pop 的過程中 `cur = max(cur + 1, dp[popped])` |
| **直方圖面積** | 「最大矩形／柱狀圖下的面積」 | pop 之後寬度 = `i - stack.peek() - 1` |
| **網格中的正方形** | 「最大的全 1 正方形」 | `dp[i][j] = 1 + min(up, left, up-left)` |
| **單趟計數** | 「最便宜的單一切割點」 | 追蹤 `onesSoFar`，並用 `flips = min(flips + 1, onesSoFar)` |

**存活輪數的遞迴式為什麼用 `max` 而不是 `+1`**：一個高的元素必須等它吞掉的*每一條*鏈都跑完，所以它繼承的是其中最慢的那條 —— 不是總和，也不是重新計數。
