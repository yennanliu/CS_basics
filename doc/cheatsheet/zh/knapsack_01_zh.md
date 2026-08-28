# 0/1 背包 DP（0/1 Knapsack 中文詳解）

> **範圍** — 用中文把 0/1 背包從「物品重量價值」一路講到 LC 494／416 的解題流程，重點放在 狀態怎麼定義、「拿或不拿」的轉移、以及為什麼內層迴圈一定要倒序；完全背包（Unbounded）與排列／組合的迴圈順序規則不在這裡展開。
> **另見**：[knapsack.md](./knapsack.md) — 同一家族的英文完整版，含 unbounded / bounded、combinations vs permutations；[dp.md](./dp.md) — DP 主索引與其他 DP pattern；[recursion_to_dp.md](./recursion_to_dp.md) — 從遞迴一步步改寫成 DP。

## LeetCode 題目清單

- [Dynamic Programming](https://leetcode.com/problem-list/dynamic-programming/)
- [0/1 Knapsack](https://leetcode.com/list/9zsdxlj0/)

---

## 0) 概念

### 0-1) 一分鐘看懂「背包」在做什麼

假設你有 4 個物品，和一個容量 7 的背包：

```text
物品：   A   B   C   D
重量：   2   3   4   5
價值：   3   4   5   6

背包容量 = 7
```

每個物品身上帶兩個資訊：

```text
weight = 拿它要花掉多少容量
value  = 拿了可以得到多少價值
```

要回答的問題是：

> 在**總重量不超過 7** 的前提下，最多可以拿到多少 value？

把幾種組合列出來看：

```text
A + B    weight = 2 + 3 = 5    value = 3 + 4 = 7
A + C    weight = 2 + 4 = 6    value = 3 + 5 = 8
B + C    weight = 3 + 4 = 7    value = 4 + 5 = 9   ← 最好
A + D    weight = 2 + 5 = 7    value = 3 + 6 = 9   ← 同樣是 9
```

所以答案是 `9`。

注意這裡的難處：**不是挑最貴的、也不是挑最輕的**。D 的 value 最大（6），但它吃掉 5 個容量，剩下 2 只能再塞 A；貪婪（greedy）在這個問題上是錯的，所以要用 DP。

### 0-2) 為什麼叫「0/1」？

因為每個物品只有兩種狀態：

```text
0 → 不拿
1 → 拿
```

而且最重要的一條規則是：

> **每個物品最多只能拿一次。**

所以下面這種選法是**不允許**的：

```text
A A A B      ✗ A 被拿了 3 次
```

換句話說：

> 0/1 背包 = **對每個 item 做一次「拿 / 不拿」的決策**。

這句「每個只能用一次」不只是題目描述——它會**直接決定 DP 的迴圈要怎麼寫**（見下面 **1-1** 節）。

### 0-3) 三個識別信號：什麼時候該想到 0/1 背包？⭐⭐⭐⭐⭐

題目同時出現這 3 點，幾乎就是 0/1 背包：

| # | 特徵 | 說明 |
|---|------|------|
| **1** | 元素**不可重複使用** | 每個物品／數字只有一個，只能選 (1) 或不選 (0)。對比：完全背包的物品可以無限次選（例如無限供應的硬幣） |
| **2** | 有明確的**容量 / 目標上限** | 背包最大容量 `W`，或子集目標和 `target`——要在不超過它的前提下做選擇 |
| **3** | 求的是**子集**相關的答案 | ① 最值（最大 value）② 存在性（能不能剛好湊到）③ 方案數（有幾種湊法） |

**實務上的快速判斷** —— 題目裡同時有：

1. 一堆物品
2. 每個物品有一個 **cost / weight**
3. 每個物品有一個 **value / reward**（或 value 就是 weight 本身）
4. 每個物品**最多只能選一次**
5. 有一個總容量 / 預算上限

→ 很可能就是 0/1 背包。

> **別被「背包」這個詞騙到。** 0/1 背包經常被包裝成完全不像背包的樣子：預算分配、選課、專案挑選、投資組合、只能做一次的任務排程……只要本質是「**選或不選 + 有容量限制**」就是它。
> LC 416（分割等和子集）、LC 494（加 +/- 號）、LC 474（限制 0 與 1 的個數）都是被包裝過的 0/1 背包。

### 0-4) DP 最重要的一步：先定義狀態

學背包最關鍵的一步，不是背程式碼，是把 `dp` 的意義講清楚。定義：

```text
dp[j] = 背包容量為 j 時，可以得到的最大 value
```

用 0-1 的例子（A=(2,3), B=(3,4), C=(4,5), D=(5,6)，容量 7），最後的 dp 長這樣：

```text
j       0   1   2   3   4   5   6   7
dp[j]   0   0   3   4   5   7   8   9
                ↑           ↑       ↑
                只拿 A      A+B     B+C
```

兩個很容易搞混的點：

- `dp[j]` **不是**「用了幾個 item」，也不是「拿到第 j 個物品」。
- `dp[j]` 是：**容量剛好給你 j 的時候，最好的答案是多少**。

> 一句話記住：**`dp` 的索引是「資源」，`dp` 的值是「答案」。**

### 0-5) 核心轉移：拿 or 不拿 ⭐⭐⭐⭐⭐

假設現在手上這個物品是：

```text
weight = 3
value  = 4
```

對容量 `j` 來說，只有兩個選擇：

**選擇 1：不拿。** 答案就維持原本的 `dp[j]`（也就是「還沒考慮這個物品時」的最佳值）。

**選擇 2：拿。** 拿了要先付出 3 的容量：

```text
剩下的容量       = j - 3
剩下容量的最佳答案 = dp[j - 3]
加上這個物品      = dp[j - 3] + 4
```

兩者取較好的那個，就是 0/1 背包**唯一需要記住的轉移式**：

```text
dp[j] = max( dp[j],  dp[j - weight] + value )
         └─不拿─┘   └────── 拿 ──────┘
```

畫成圖：

```text
                  容量 j
                    │
          ┌─────────┴─────────┐
       不拿                   拿
          │                    │
       dp[j]          dp[j - weight] + value
          └─────────┬─────────┘
                  max
                    ↓
                 dp[j]（更新後）
```

### 0-6) 標準模板（1-D 滾動陣列）⭐⭐⭐⭐⭐

```python
# python — 0/1 背包標準模板（求最大 value）
# IDEA: 外層跑物品，內層容量「由大到小」，確保每個物品只被用一次
# time = O(n * W), space = O(W)
def knapsack_01(items, capacity):
    dp = [0] * (capacity + 1)                    # dp[j] = 容量 j 的最大 value

    for weight, value in items:                  # ① 外層：一個一個物品處理
        for j in range(capacity, weight - 1, -1): # ② 內層：容量倒序！
            dp[j] = max(dp[j], dp[j - weight] + value)   # ③ 拿 or 不拿

    return dp[capacity]
```

```java
// java — 0/1 背包標準模板（求最大 value）
// IDEA: 同上；j 從 W 遞減到 weight，讀到的 dp[j-weight] 必定是「上一輪」的值
// time = O(n * W), space = O(W)
public int knapsack01(int[] weights, int[] values, int W) {
    int[] dp = new int[W + 1];

    for (int i = 0; i < weights.length; i++) {                 // ① 物品
        for (int j = W; j >= weights[i]; j--) {                // ② 容量倒序
            dp[j] = Math.max(dp[j], dp[j - weights[i]] + values[i]);  // ③ 拿 or 不拿
        }
    }
    return dp[W];
}
```

先把這個骨架記牢，剩下都是變形：

```text
for item:            ← 外層一定是物品
    for capacity:    ← 內層是容量，0/1 一律倒序
        拿 or 不拿    ← 依題目換成 max / min / +=
```

---

## 1) 通用形式

### 1-1) 為什麼內層迴圈一定要倒序？⭐⭐⭐⭐⭐

這是 0/1 背包**最容易寫錯、也最常被追問**的地方。

用一個極簡例子：

```text
capacity = 5
只有一個物品： weight = 2, value = 10
```

正確答案顯然是 `10`（這個物品只有一個，最多拿一次）。

**倒序寫法**：

```python
for j in range(5, 1, -1):    # 5 → 4 → 3 → 2
    dp[j] = max(dp[j], dp[j - 2] + 10)
```

逐步追蹤（初始全 0）：

```text
起始:            dp = [0, 0, 0, 0, 0, 0]
                        0  1  2  3  4  5

j=5:  dp[5] = max(dp[5], dp[3] + 10) = 10     dp[3] 還是 0（本輪還沒動過）✓
j=4:  dp[4] = max(dp[4], dp[2] + 10) = 10     dp[2] 還是 0 ✓
j=3:  dp[3] = max(dp[3], dp[1] + 10) = 10     dp[1] 還是 0 ✓
j=2:  dp[2] = max(dp[2], dp[0] + 10) = 10     dp[0] = 0   ✓

結果:            dp = [0, 0, 10, 10, 10, 10]      → 答案 10 ✓
```

**關鍵**：`j` 由大到小走的時候，`dp[j - weight]` 的索引比 `j` 小，而比 `j` 小的位置**這一輪還沒被更新過**——讀到的是「還沒放進這個物品」的舊值。所以這個物品最多被算一次。

### 1-2) 如果改成正序會怎樣？（同一個例子的 trace）

```python
for j in range(2, 6):        # 2 → 3 → 4 → 5   ← 錯誤寫法（對 0/1 而言）
    dp[j] = max(dp[j], dp[j - 2] + 10)
```

```text
起始:            dp = [0, 0, 0, 0, 0, 0]

j=2:  dp[2] = max(dp[2], dp[0] + 10) = 10     ← 用了 1 次物品
j=3:  dp[3] = max(dp[3], dp[1] + 10) = 10
j=4:  dp[4] = max(dp[4], dp[2] + 10) = 20     ← dp[2] 已含這個物品 → 用了 2 次！✗
j=5:  dp[5] = max(dp[5], dp[3] + 10) = 20     ← 又一次疊加

結果:            dp = [0, 0, 10, 10, 20, 20]      → 答案 20 ✗（物品被拿了兩次）
```

正序時 `dp[j - weight]` 已經被**本輪**更新過，等於允許「同一個物品再拿一次」——這正好就是**完全背包（Unbounded Knapsack）**要的行為。

所以這條規則值得直接背下來：

```text
倒序 = 禁止重複使用目前這個 item   → 0/1 背包
正序 = 允許重複使用目前這個 item   → 完全背包
```

| | **0/1 背包** | **完全背包（Unbounded）** |
|---|---|---|
| 每個物品可用幾次 | 最多 1 次 | 無限次 |
| 內層容量方向 | **倒序**（`W → weight`） | **正序**（`weight → W`） |
| `dp[j-weight]` 讀到的是 | 上一輪的舊值 | 本輪的新值 |
| 代表題 | LC 416, 494, 1049, 474 | LC 322, 518, 279 |

```python
# 0/1：倒序
for weight, value in items:
    for j in range(capacity, weight - 1, -1):
        ...

# Unbounded：正序
for weight, value in items:
    for j in range(weight, capacity + 1):
        ...
```

> 兩份程式碼的差別只有內層 `range` 的方向——但語意完全不同。看到 0/1 背包時，先確認自己寫的是 `range(capacity, weight - 1, -1)`。

### 1-3) 從 2-D 原始版本理解 1-D 是怎麼壓出來的

1-D 版本雖然短，但一開始不好懂。原始的 2-D 定義更直白：

```text
dp[i][j] = 只考慮前 i 個物品、容量為 j 時的最大 value
```

```python
# python — 2-D 0/1 背包（教學用，理解 1-D 的來源）
# time = O(n * W), space = O(n * W)
def knapsack_01_2d(weights, values, capacity):
    n = len(weights)
    dp = [[0] * (capacity + 1) for _ in range(n + 1)]

    for i in range(1, n + 1):
        for j in range(capacity + 1):
            dp[i][j] = dp[i - 1][j]                          # 不拿第 i 個
            if weights[i - 1] <= j:                          # 容量夠才能拿
                dp[i][j] = max(dp[i][j],
                               dp[i - 1][j - weights[i - 1]] + values[i - 1])  # 拿
    return dp[n][capacity]
```

看轉移式右邊：`dp[i][j]` 只依賴 **`dp[i-1][...]`**，也就是「上一列」。所以只要保留一列就好——這就是 1-D 版本。

而 1-D 版本會出問題的地方正是：一列同時扮演「上一列」和「這一列」。

```text
2-D:   dp[i][j] = max( dp[i-1][j],  dp[i-1][j-w] + v )
                            ↑              ↑
                       都必須是「上一列」的值

1-D:   dp[j]    = max( dp[j],      dp[j-w] + v )
                            ↑              ↑
                     還沒更新 = 舊的    必須也是舊的 → 所以 j 要倒序
```

> **一句話**：倒序不是什麼小技巧，它是「1-D 陣列在模擬 2-D 的上一列」的必要條件。

### 1-4) 背包不只有「最大價值」：max / min / count / boolean ⭐⭐⭐⭐

刷 LC 時最需要注意的一點：骨架一樣，但 `dp` 的意義和轉移會換。

| 問法 | `dp[j]` 的意義 | 轉移 | 初始化 | 代表題 |
|------|---------------|------|--------|--------|
| **最大值** | 容量 j 的最大 value | `dp[j] = max(dp[j], dp[j-w] + v)` | 全 0 | 經典背包, LC 1049 |
| **最小值** | 湊出 j 的最少個數 | `dp[j] = min(dp[j], dp[j-w] + 1)` | `dp[0]=0`，其餘 `inf` | LC 322, 279 |
| **方案數** | 湊出 j 有幾種方法 | `dp[j] += dp[j-w]` | `dp[0]=1`，其餘 0 | LC 494, 518 |
| **存在性** | 能不能湊出 j | `dp[j] = dp[j] or dp[j-w]` | `dp[0]=True`，其餘 False | LC 416, 2915 |

```text
                Knapsack 骨架
                     │
      ┌──────────┬───┴────┬──────────┐
      ↓          ↓        ↓          ↓
     max        min     count     boolean
   （價值）   （個數）  （方法數）  （可行性）
```

四種寫法並排看：

```python
# python — 同一個骨架的四種轉移
for x in items:
    for j in range(target, x - 1, -1):     # 0/1 → 一律倒序
        dp[j] = max(dp[j], dp[j - x] + value)   # A. 最大值
        dp[j] = min(dp[j], dp[j - x] + 1)       # B. 最少個數
        dp[j] += dp[j - x]                      # C. 方案數
        dp[j] = dp[j] or dp[j - x]              # D. 存在性
```

> 注意上面四行是**四題各用一行**，不是同時寫在一起。

### 1-5) 三種背包的完整對照

| 類型 | 每個物品可用幾次 | 容量方向 | 做法 | 代表題 |
|------|----------------:|----------|------|--------|
| **0/1 背包** | 最多 1 次 | **倒序** | 標準模板 | LC 416, 494, 1049, 474 |
| **完全背包（Unbounded）** | 無限次 | **正序** | 內層改正序即可 | LC 322, 518, 279 |
| **有界背包（Bounded）** | 最多 `k` 次 | 倒序 | 把每個物品用二進位拆成 `1,2,4,…` 份，再當成 0/1 做 | LC 2585, 1774 |

一個很好用的判斷：

> - **「每個東西只能選一次」→ 0/1（倒序）**
> - **「每個東西可以一直選」→ 完全背包（正序）**
> - **「每個東西最多 k 次」→ 二進位拆分後套 0/1**

### 1-6) 建議的四步解題流程 ⭐⭐⭐⭐⭐

看到題目**先不要急著寫程式碼**，先跑完這 4 步：

```text
Step 1 ── 每個 item 能用幾次？
            最多一次   → 0/1（內層倒序）
            無限次     → 完全背包（內層正序）
            最多 k 次  → 二進位拆分 → 0/1

Step 2 ── 題目要算什麼？
            最大值 → max      最小值 → min
            方法數 → +=       可不可行 → or

Step 3 ── State 是什麼？
            dp[j] = 「用已處理過的 items，湊出 capacity / sum = j 的答案」
            → 順手把初始值定好（見 1-4 的初始化欄）

Step 4 ── 寫「拿 or 不拿」
            不拿：dp[j]
            拿  ：dp[j - weight] (+ value / + 1 / 直接相加)
            用 Step 2 選的運算子把兩者合起來
```

---

## 2) LC 範例

### 2-1) LC 494 Target Sum — 方案數版的 0/1 背包 ⭐⭐⭐⭐⭐

```text
nums = [1, 1, 1, 1, 1]，target = 3
每個數字前面加 + 或 -，問有幾種加法組合的結果等於 3？
```

**第一步是數學轉換**，把「加正負號」變成「選子集」。設所有被標成 `+` 的數字之和為 `P`，被標成 `-` 的為 `N`：

```text
P + N = sum(nums)        （全部數字加起來）
P - N = target           （題目要求）
──────────────────────
2P    = sum + target
 P    = (sum + target) / 2
```

所以問題變成：

> **有幾個子集，它們的和剛好等於 `(sum + target) / 2`？**

代入例子：

```text
subset_sum = (5 + 3) / 2 = 4
→ 從 [1,1,1,1,1] 中選幾個，使和 = 4 有幾種方法？（答案 5）
```

每個數字只能用一次 → **0/1 背包**；問的是方法數 → **count 版本**。

```python
# python
# LC 494 - Target Sum
# IDEA: (sum + target) / 2 轉成 subset sum，再用 0/1 背包數方案數
# time = O(n * subset_sum), space = O(subset_sum)
def findTargetSumWays(nums, target):
    total = sum(nums)
    if abs(target) > total or (total + target) % 2 != 0:
        return 0                                  # 湊不出來（奇偶不合 / 超出範圍）

    subset_sum = (total + target) // 2
    dp = [0] * (subset_sum + 1)
    dp[0] = 1                                     # base case：什麼都不選，湊出 0 有 1 種

    for num in nums:
        for s in range(subset_sum, num - 1, -1):  # 倒序 → 每個 num 只用一次
            dp[s] += dp[s - num]                  # 方案數用 += ，不是 max()

    return dp[subset_sum]
```

```java
// java
// LC 494 - Target Sum
// IDEA: 同上；注意 (total + target) 必須是偶數且 |target| <= total
// time = O(n * subsetSum), space = O(subsetSum)
public int findTargetSumWays(int[] nums, int target) {
    int total = 0;
    for (int n : nums) total += n;
    if (Math.abs(target) > total || (total + target) % 2 != 0) return 0;

    int subsetSum = (total + target) / 2;
    int[] dp = new int[subsetSum + 1];
    dp[0] = 1;

    for (int num : nums) {
        for (int s = subsetSum; s >= num; s--) {   // 倒序
            dp[s] += dp[s - num];
        }
    }
    return dp[subsetSum];
}
```

這裡的 `dp[s]` **不是**最大 value，而是：

> **湊出 sum = s 有幾種方法**

所以用 `+=` 而不是 `max()`。

#### `dp[0] = 1` 到底是什麼意思？

這個初始化不是隨便填的，它是 base case。用 `nums = [2]` 看：

```text
dp[0] = 1        意思是「湊出 sum = 0 有 1 種方法」→ 就是「什麼都不選」

處理 num = 2：
dp[2] += dp[0]   →  dp[2] = 1     意思是「湊出 2 有 1 種方法」→ 就是 [2]
```

如果把 `dp[0]` 設成 0，那 `dp[2] += dp[0]` 會得到 0，整條鏈全部變 0——**所有方案都是從「空集合」長出來的**，所以空集合這一種必須先算進去。

```text
dp[0] = 1  ⟺  空集合的和是 0，而且「什麼都不選」是一種合法方案
```

### 2-2) LC 416 Partition Equal Subset Sum — 存在性版本 ⭐⭐⭐⭐⭐

> 能不能把陣列分成兩個和相等的子集？

和相等 ⟺ 每邊都是 `sum / 2` ⟺ **能不能選出子集使其和 = `sum / 2`**。這裡 weight 和 value 是同一個數字。

```python
# python
# LC 416 - Partition Equal Subset Sum
# IDEA: 目標 = sum/2 的 subset sum 存在性，0/1 背包 boolean 版
# time = O(n * sum/2), space = O(sum/2)
def canPartition(nums):
    total = sum(nums)
    if total % 2 != 0:
        return False                              # 奇數不可能平分

    target = total // 2
    dp = [False] * (target + 1)
    dp[0] = True                                  # 空集合湊出 0

    for num in nums:
        for j in range(target, num - 1, -1):      # 倒序
            dp[j] = dp[j] or dp[j - num]

    return dp[target]
```

```java
// java
// LC 416 - Partition Equal Subset Sum
// IDEA: 同上；可在內層提早 return 命中 target 的情況
// time = O(n * sum/2), space = O(sum/2)
public boolean canPartition(int[] nums) {
    int total = 0;
    for (int n : nums) total += n;
    if (total % 2 != 0) return false;

    int target = total / 2;
    boolean[] dp = new boolean[target + 1];
    dp[0] = true;

    for (int num : nums) {
        for (int j = target; j >= num; j--) {      // 倒序
            dp[j] = dp[j] || dp[j - num];
        }
    }
    return dp[target];
}
```

以 `nums = [1, 5, 11, 5]`（total = 22, target = 11）追蹤 `dp`：

```text
初始         dp = T F F F F F F F F F F F      (index 0..11)
放入 1       dp = T T F F F F F F F F F F
放入 5       dp = T T F F F T T F F F F F      （5, 6=1+5）
放入 11      dp = T T F F F T T F F F F T      （11）
放入 5       dp = T T F F F T T F F F T T      （10=5+5, 11 已成立）
                                            ↑
                                        dp[11] = True → 可以平分
```

### 2-3) LC 1049 Last Stone Weight II — 最大值版本 ⭐⭐⭐⭐

> 每次挑兩顆石頭相撞，剩下差值，問最後剩下的最小重量。

每顆石頭最後其實只是被分成「+ 組」和「− 組」，答案 = `|sumA - sumB|`。要讓差最小，就要讓其中一組**盡量接近但不超過 `total / 2`**——這就是「容量 `total/2` 的最大 subset sum」。

```python
# python
# LC 1049 - Last Stone Weight II
# IDEA: 分兩堆使差最小 → 求 <= total//2 的最大 subset sum
# time = O(n * total), space = O(total)
def lastStoneWeightII(stones):
    total = sum(stones)
    target = total // 2
    dp = [0] * (target + 1)                       # dp[j] = 容量 j 內能裝的最大和

    for stone in stones:
        for j in range(target, stone - 1, -1):    # 倒序
            dp[j] = max(dp[j], dp[j - stone] + stone)

    return total - 2 * dp[target]                 # (total - best) - best
```

### 2-4) LC 474 Ones and Zeroes — 二維容量

有些題的「容量」不只一個維度。LC 474 同時限制 0 的個數 `m` 和 1 的個數 `n`，那就開兩維、**兩維都倒序**：

```python
# python
# LC 474 - Ones and Zeroes
# IDEA: 容量是 (m 個 0, n 個 1) 的二維 0/1 背包，value = 字串數 1
# time = O(len(strs) * m * n), space = O(m * n)
def findMaxForm(strs, m, n):
    dp = [[0] * (n + 1) for _ in range(m + 1)]

    for s in strs:
        zeros, ones = s.count('0'), s.count('1')
        for i in range(m, zeros - 1, -1):         # 兩個維度都要倒序
            for j in range(n, ones - 1, -1):
                dp[i][j] = max(dp[i][j], dp[i - zeros][j - ones] + 1)

    return dp[m][n]
```

> 規則不變：**每個維度都是容量**，所以每一層容量迴圈都要倒序。

### 2-5) 相似題整理

| LC # | 題目 | 變形 | 關鍵轉換 |
|------|------|------|----------|
| **416** | Partition Equal Subset Sum | boolean | `target = sum / 2`，問能否湊到 |
| **494** | Target Sum | count | `subset_sum = (sum + target) / 2` |
| **1049** | Last Stone Weight II | max | 求 `<= sum/2` 的最大 subset sum |
| **474** | Ones and Zeroes | 二維容量 | 容量 = (0 的個數, 1 的個數) |
| **879** | Profitable Schemes | 二維 + count | 容量 = (人數, 利潤下限) |
| **2915** | Longest Subsequence That Sums to Target | max 長度 | `dp[j]` 換成「湊出 j 的最長長度」 |
| **322** | Coin Change | **完全背包** min | 硬幣無限 → 內層改**正序** |
| **518** | Coin Change II | **完全背包** count | 硬幣無限 → 內層改**正序** |

> 322 / 518 放在這裡是為了對照：它們**不是** 0/1 背包。判斷點只有一個——硬幣可以重複使用，所以內層正序。完整討論見 [knapsack.md](./knapsack.md)。

---

## 3) 常見錯誤與邊界情況

### 🚫 常見錯誤

| # | 錯誤 | 症狀 | 修正 |
|---|------|------|------|
| 1 | 內層容量寫成**正序** | 答案偏大（物品被重複使用） | `range(capacity, weight - 1, -1)` |
| 2 | **外層寫容量、內層寫物品** | 1-D 滾動陣列語意壞掉 | 0/1 背包外層一定是物品 |
| 3 | 方案數題忘了 `dp[0] = 1` | 全部回傳 0 | 空集合是一種合法方案 |
| 4 | min 題忘了初始化成 `inf` | 答案變成 0 | `dp[0] = 0`，其餘 `float('inf')` |
| 5 | 陣列開 `capacity` 而不是 `capacity + 1` | index out of range | 要包含 `dp[0]`，長度是 `capacity + 1` |
| 6 | LC 494 忘檢查 `(sum + target)` 的奇偶 | 除出非整數 / 答案錯 | `if (total + target) % 2: return 0` |
| 7 | LC 494 忘檢查 `abs(target) > total` | 負索引／錯答案 | 先擋掉不可能的 target |
| 8 | 二維容量只倒序一個維度 | 答案偏大 | **每個**容量維度都要倒序 |

### ⚠️ 邊界情況

```text
capacity = 0            → 答案 0（max 版）／dp[0] 的初始值（count / boolean 版）
物品 weight > capacity  → 永遠放不進去，內層 range 自然為空，不需特判
sum 是奇數（LC 416）     → 直接 return False
空陣列                  → max 版回 0；count 版回 dp[0]
weight = 0 的物品        → 倒序 range 會退化，需個別處理（LC 一般不會出現）
負數 value / weight     → 標準背包不支援，需另外轉換
```

---

## 4) 一頁總結：模板地圖

真正要掌握的不是背 20 題解法，而是這張圖：

```text
             每個 item
                 ↓
          ┌─────────────┐
          │  拿 / 不拿   │
          └─────────────┘
                 ↓
              dp[j]
                 ↓
      ┌──────────┼──────────┬──────────┐
      ↓          ↓          ↓          ↓
     max        min       count     boolean
```

加上另一個關鍵：

```text
0/1（每個只能用一次）  → 內層容量 倒序
完全背包（可無限用）    → 內層容量 正序
```

三個最常用的模板，建議分開記：

```python
# python
# ① 0/1 背包 — 最大值
dp = [0] * (capacity + 1)
for weight, value in items:
    for j in range(capacity, weight - 1, -1):
        dp[j] = max(dp[j], dp[j - weight] + value)

# ② 0/1 背包 — 方案數（LC 494）
dp = [0] * (target + 1)
dp[0] = 1
for num in nums:
    for s in range(target, num - 1, -1):          # 倒序
        dp[s] += dp[s - num]

# ③ 完全背包 — 方案數（LC 518）
dp = [0] * (target + 1)
dp[0] = 1
for num in nums:
    for s in range(num, target + 1):              # 正序
        dp[s] += dp[s - num]
```

| 面試要能立刻說出的 | 答案 |
|---|---|
| 時間複雜度 | `O(n * W)`，n = 物品數，W = 容量／目標 |
| 空間複雜度 | 1-D 滾動陣列 `O(W)`（2-D 版是 `O(n * W)`） |
| 為什麼倒序 | 1-D 陣列在模擬 2-D 的「上一列」，倒序才能讀到還沒被本輪更新的舊值 |
| 為什麼不能貪婪 | 高 value 的物品可能吃掉太多容量，局部最優 ≠ 全局最優 |

> **接下來讀什麼**：完全背包、有界背包、以及「組合 vs 排列」的迴圈順序規則（LC 518 vs LC 377）在
> [**knapsack.md**](./knapsack.md)；其他 DP pattern 在 [**dp.md**](./dp.md)。
