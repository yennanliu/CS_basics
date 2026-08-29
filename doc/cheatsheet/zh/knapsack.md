# 背包 DP（0/1、完全背包、Coin Change）

> **範圍** — 完整的背包問題家族：0/1 vs 完全背包（unbounded）vs 有界背包（bounded）、子集合和（subset-sum）的化簡、為什麼 0/1 的內層迴圈要倒序，以及區分組合與排列的迴圈順序規則。
> **另見**：[dp.md](./dp.md) — 一頁式的背包模板與其餘 DP 模式；[knapsack_01_zh.md](./knapsack_01_zh.md) — 0/1 背包的中文詳解 — 只講 0/1 情況的中文逐步解說；[combinatorics_math_patterns.md](./combinatorics_math_patterns.md) — 不用 DP 的計數方法。

## LeetCode 題目清單

- [Dynamic Programming](https://leetcode.com/problem-list/dynamic-programming/)
- [0/1 Knapsack](https://leetcode.com/list/9zsdxlj0/)

## 總覽

### 關鍵性質

- **複雜度**：時間 `O(n * W)`；壓成一維後空間 `O(W)` — `n` 個物品、容量／目標為 `W`。
- **核心想法**：每個物品都是一個**拿 / 不拿**的決定，而區分各種變形的那個 DP 維度是
  **容量軸** — 也就是內層迴圈讀到的值，是否已經包含了當前這個物品。
- **使用時機**：有一組固定的物品、每個帶有成本，有一個硬性的容量／目標，
  然後要對子集合問「最大值 / 可行性 / 有幾種方法」。

### 一張決定一切的表

| 變形 | 重複使用 | 外層迴圈 | 內層迴圈 | LC |
|---------|-------|-----------|------------|----|
| **0/1** | 每個物品最多 1 次 | 物品 | 容量，**倒序** | 416, 494, 1049, 474 |
| **完全背包 — 組合** | 無限次，順序**不**重要 | 物品 | amount，正序 | 518 |
| **完全背包 — 排列** | 無限次，順序**很**重要 | amount | 物品 | 377 |
| **完全背包 — 最小 / 最大** | 無限次，順序無關 | 皆可 | 皆可 | 322, 279, 1449 |
| **有界背包** | 每個物品最多 `k` 次 | 物品（二進位拆分成多份 0/1 副本） | 容量，倒序 | 2585, 1774 |

### 參考資料

- [dp.md](./dp.md) — 精簡版背包模板與其餘 DP 模式家族
- [knapsack_01_zh.md](./knapsack_01_zh.md) — 0/1 背包中文詳解：state 定義、倒序 trace、LC 494/416 解題流程
- [Knapsack problem — Wikipedia](https://en.wikipedia.org/wiki/Knapsack_problem)

## 題型分類

| 分類 | 它回答什麼問題 | 答案型別 | LC |
|----------|--------------------|-------------|----|
| **子集合可行性** | 有*某個*子集合剛好湊到這個和嗎？ | boolean | 416, 1049, 2915 |
| **子集合計數** | 有幾個子集合能湊到？ | int（方法數） | 494, 518 |
| **容量上限下的最大價值** | 塞得進容量的最大價值是多少？ | int（max） | 經典 0/1、474、879 |
| **湊到目標的最少物品數** | 湊出這個金額最少要幾枚硬幣 / 幾個平方數？ | int（min）或 -1 | 322, 279 |
| **有序 vs 無序計數** | `1+2` 和 `2+1` 算同一種嗎？ | 決定迴圈的巢狀順序 | 518 vs 377 |

## 模板與演算法

### 迴圈順序：組合 vs 排列

**🔑 關鍵洞見**：在完全背包類的題目（例如 Coin Change）中，**巢狀迴圈的順序**決定了你數的是**組合**還是**排列**。

---

#### **🎯 終極速查表：什麼時候用哪個模式**

| 當題目說…… | 該用的模式 | 迴圈順序 | 方向 | DP 轉移式 | 範例 LC |
|---------------------|----------------|------------|-----------|---------------|------------|
| 「數方法數」+ 順序不重要 | **組合** | 物品 → 目標 | 正序 | `dp[i] += dp[i-item]` | **518** |
| 「數方法數」+ 順序重要 | **排列** | 目標 → 物品 | 正序 | `dp[i] += dp[i-item]` | **377** |
| 「每個物品只能用一次」+ 求最大／最小 | **0/1 背包** | 物品 → 容量 | **倒序** | `dp[w] = max(dp[w], ...)` | **416** |
| 「物品可無限使用」+ 求最大／最小 | **完全背包** | 物品 → 容量 | 正序 | `dp[i] = min(dp[i], ...)` | **322** |

**⚡ 快速辨識：**
- 看到「different sequences」或「different orderings」→ **排列**（目標在外層）
- 看到「number of combinations」或「unique ways」→ **組合**（物品在外層）
- 看到「each element at most once」→ **0/1 背包**（倒序）
- 看到「minimum coins」或「fewest items」→ **完全背包**（正序）

---

#### **📊 視覺總覽：四個核心模式**

```text
┌─────────────────────────────────────────────────────────────────────────┐
│                     DP KNAPSACK PATTERN MATRIX                          │
└─────────────────────────────────────────────────────────────────────────┘

                          COUNT WAYS              FIND MIN/MAX
                    ┌──────────────────┬──────────────────────────┐
                    │                  │                          │
ORDER MATTERS?      │  PERMUTATIONS    │   Not typically used     │
(Yes)              │  LC 377          │   (Use Permutations      │
                    │  Target→Item     │    for counting)         │
                    │  Forward         │                          │
                    ├──────────────────┼──────────────────────────┤
                    │                  │                          │
ORDER DOESN'T       │  COMBINATIONS    │   UNBOUNDED KNAPSACK     │
MATTER              │  LC 518          │   LC 322                 │
(No)                │  Item→Target     │   Item→Capacity          │
                    │  Forward         │   Forward                │
                    ├──────────────────┼──────────────────────────┤
                    │                  │                          │
USE EACH ONCE       │  Not typical     │   0/1 KNAPSACK           │
(Constraint)        │  (Can adapt      │   LC 416                 │
                    │   0/1 pattern)   │   Item→Capacity          │
                    │                  │   BACKWARD ⚠️            │
                    └──────────────────┴──────────────────────────┘

Legend:
  Item→Target     = Outer loop: items,    Inner loop: target
  Target→Item     = Outer loop: target,   Inner loop: items
  Forward         = Inner loop: i to target (allows reuse)
  BACKWARD ⚠️     = Inner loop: target to i (prevents reuse)
```

**🎯 決策流程：**
```text
Start
  │
  ├─ Question asks "count ways"?
  │   │
  │   ├─ YES → Order matters?
  │   │         ├─ YES → Permutations (Target→Item) [LC 377]
  │   │         └─ NO  → Combinations (Item→Target) [LC 518]
  │   │
  │   └─ NO  → Question asks "min/max"?
  │             │
  │             ├─ Each item once?
  │             │   ├─ YES → 0/1 Knapsack (BACKWARD) [LC 416]
  │             │   └─ NO  → Unbounded (FORWARD) [LC 322]
  │             │
  │             └─ Unknown → Check problem constraints
```

---

#### **📋 主模式表：依題型分類的 DP 轉移式**

| 模式類型 | 迴圈順序 | DP 轉移式 | 它數的是什麼 | 心智模型 | 範例 | 結果 |
|--------------|------------|---------------|----------------|--------------|---------|--------|
| **組合**<br>（順序不重要） | **物品 → 目標**<br><br>`for item in items:`<br>&nbsp;&nbsp;`for i in range(item, target+1):` | `dp[i] += dp[i - item]` | 不重複的集合<br>[1,2] = [2,1] | 「先處理完物品 1 的所有用法，再處理物品 2 的所有用法」<br><br>強制產生標準順序 | LC 518<br>coins=[1,2]<br>amount=3 | **2 種**<br>{1,1,1}<br>{1,2} |
| **排列**<br>（順序重要） | **目標 → 物品**<br><br>`for i in range(1, target+1):`<br>&nbsp;&nbsp;`for item in items:` | `dp[i] += dp[i - item]` | 不同的排列順序<br>[1,2] ≠ [2,1] | 「對每個目標值，試著讓每個物品當『最後一個』」<br><br>允許任意順序 | LC 377<br>nums=[1,2]<br>target=3 | **3 種**<br>{1,1,1}<br>{1,2}<br>{2,1} |
| **0/1 背包**<br>（每個只用一次） | **物品 → 容量**<br>（倒序）<br><br>`for item in items:`<br>&nbsp;&nbsp;`for w in range(W, weight-1, -1):` | `dp[w] = max(dp[w],`<br>`dp[w-weight[i]] + value[i])` | 有限制條件下的最大／最小值<br>每個物品最多用 1 次 | 「必須倒序迭代，才不會在同一輪裡把同一個物品用了兩次」 | LC 416<br>Partition<br>Subset | True/False<br>或最大價值 |
| **完全背包**<br>（可無限使用） | **物品 → 容量**<br>（正序）<br><br>`for item in items:`<br>&nbsp;&nbsp;`for w in range(weight, W+1):` | `dp[w] = max(dp[w],`<br>`dp[w-weight[i]] + value[i])` | 無次數限制下的最大／最小值<br>每個物品可無限使用 | 「正序迭代 — 同一輪裡就能用到已更新的值」 | LC 322<br>Coin Change<br>（最少硬幣數） | 最小數量<br>或 -1 |

---

#### **💻 各模式的程式碼模板**

```java
// java
// IDEA: the four knapsack loop orders side by side — each differs only in nesting/direction
// time = O(n * W), space = O(W)
// ============================================
// PATTERN 1: COMBINATIONS (Item → Target)
// ============================================
// LC 518: Coin Change II
public int countCombinations(int target, int[] items) {
    int[] dp = new int[target + 1];
    dp[0] = 1;  // Base: one way to make 0

    // OUTER: Items/Coins
    for (int item : items) {
        // INNER: Target/Amount (forward)
        for (int i = item; i <= target; i++) {
            dp[i] += dp[i - item];  // ← Same transition
        }
    }
    return dp[target];
}

// ============================================
// PATTERN 2: PERMUTATIONS (Target → Item)
// ============================================
// LC 377: Combination Sum IV
public int countPermutations(int target, int[] items) {
    int[] dp = new int[target + 1];
    dp[0] = 1;  // Base: one way to make 0

    // OUTER: Target/Amount
    for (int i = 1; i <= target; i++) {
        // INNER: Items/Coins
        for (int item : items) {
            if (i >= item) {
                dp[i] += dp[i - item];  // ← Same transition
            }
        }
    }
    return dp[target];
}

// ============================================
// PATTERN 3: 0/1 KNAPSACK (Item → Capacity BACKWARDS)
// ============================================
// LC 416: Partition Equal Subset Sum
public boolean canPartition(int[] nums, int target) {
    boolean[] dp = new boolean[target + 1];
    dp[0] = true;  // Base: can make 0

    // OUTER: Items
    for (int num : nums) {
        // INNER: Capacity (BACKWARDS to prevent reuse)
        for (int w = target; w >= num; w--) {
            dp[w] = dp[w] || dp[w - num];  // ← Different transition (OR)
        }
    }
    return dp[target];
}

// ============================================
// PATTERN 4: UNBOUNDED KNAPSACK (Item → Capacity FORWARDS)
// ============================================
// LC 322: Coin Change (minimum coins)
public int minCoins(int target, int[] coins) {
    int[] dp = new int[target + 1];
    Arrays.fill(dp, target + 1);  // Infinity
    dp[0] = 0;  // Base: 0 coins for 0 amount

    // OUTER: Items/Coins
    for (int coin : coins) {
        // INNER: Target (FORWARDS allows reuse)
        for (int i = coin; i <= target; i++) {
            dp[i] = Math.min(dp[i], dp[i - coin] + 1);  // ← Different transition (MIN)
        }
    }
    return dp[target] > target ? -1 : dp[target];
}
```

**🔑 重點觀察：**
1. **DP 轉移式相同（`dp[i] += dp[i - item]`）** 的有：
   - 組合（物品 → 目標）
   - 排列（目標 → 物品）
   - **唯一差別**：迴圈順序！

2. **DP 轉移式不同**的有：
   - 0/1 背包：`dp[w] = dp[w] || dp[w - num]`（布林 OR 或取 MAX）
   - 完全背包：`dp[i] = min(dp[i], dp[i - coin] + 1)`（取 MIN／MAX）

3. 背包的**方向很關鍵**：
   - 倒序 → 阻止重複使用（0/1）
   - 正序 → 允許重複使用（完全背包）

---

#### **🎯 模式選擇決策樹**

```text
Question: What does the problem ask for?

├─ "Count number of ways/combinations to reach target"
│  ├─ Order matters? (e.g., [1,2] ≠ [2,1])
│  │  ├─ YES → Use PERMUTATIONS pattern (Target → Item)
│  │  │         Example: LC 377 Combination Sum IV
│  │  └─ NO  → Use COMBINATIONS pattern (Item → Target)
│  │            Example: LC 518 Coin Change II
│  │
│  └─ Can reuse items?
│     ├─ YES → Unbounded, iterate forwards
│     └─ NO  → 0/1 Knapsack, iterate backwards
│
└─ "Find minimum/maximum value"
   ├─ Can reuse items?
   │  ├─ YES → Unbounded Knapsack (forwards)
   │  │         Example: LC 322 Coin Change (min coins)
   │  └─ NO  → 0/1 Knapsack (backwards)
   │            Example: LC 416 Partition Equal Subset Sum
   │
   └─ Always use (Item → Capacity) order
```

---

### **深入探討：0/1 背包與子集合和模式** 🎒

這個模式非常基礎，而且經常以各種偽裝的形式出現。Last Stone Weight II 就是一個很好的例子，示範怎麼看出一題其實骨子裡是子集合和問題。

#### **什麼時候用這個模式**

看到下列訊號就用 **0/1 背包 / 子集合和**：

| 訊號 | 代表什麼 | 範例 |
|-----------|--------------|---------|
| 「Partition」或「split into two groups」 | 把物品分成幾個子集合 | LC 1049 (Last Stone Weight II) |
| 「Maximize/minimize the difference」 | 找出最佳的分割方式 | LC 1049, 494 |
| 「Can you achieve sum X?」 | 檢查某個特定和是否湊得出來 | LC 416 (Equal Subset Partition) |
| 「Each item used at most once」 | 0/1 限制（不是無限次） | 以上皆是 |
| 「Minimize difference between groups」 | 分成兩組且要盡量平衡 | LC 1049 |

**關鍵辨識**：看到「partition」或「divide into two groups」→ 想到 **0/1 背包**。

#### **核心想法：數學上的轉換** 🧮

**問題**：把陣列分成兩組，並讓兩組的差最小。

```text
Given: stones = [2, 7, 4, 1, 8, 1]
Total sum = 23

Goal: Split into two groups with min |sum1 - sum2|

Mathematical insight:
  Let sum1 = S (sum of group 1)
  Then sum2 = total - S (sum of group 2)
  
  Difference = |sum1 - sum2| = |S - (total - S)| = |2S - total|
  
  To minimize this: Maximize S such that S ≤ total/2
  
  Result = total - 2*S (where S is the largest achievable sum ≤ total/2)
```

**為什麼這樣成立**：
- 找出不超過 `total / 2` 的最大子集合和
- 這就給出了最平衡的分割方式
- 剩下那一組的和是 `total - S`
- 兩者的差 = `(total - S) - S = total - 2*S`

#### **模式：兩種變形**

**變形 1：布林 DP（這個和湊得出來嗎？）**

```java
// java
// LC 1049 - Last Stone Weight II
// IDEA: variant 1 — boolean subset sum; can we reach exactly `sum`?
// time = O(n * total), space = O(total)
public int lastStoneWeightII(int[] stones) {
    int total = 0;
    for (int stone : stones) {
        total += stone;
    }

    int target = total / 2;
    
    // dp[j] = can we achieve sum j?
    boolean[] dp = new boolean[target + 1];
    dp[0] = true;  // Base: always can make sum 0 (choose nothing)

    // For each stone
    for (int stone : stones) {
        // Iterate BACKWARDS to prevent using same stone twice
        for (int j = target; j >= stone; j--) {
            dp[j] = dp[j] || dp[j - stone];  // Can achieve j if:
                                              // (already could) OR (could make j-stone and add this stone)
        }
    }

    // Find largest achievable sum ≤ target
    for (int j = target; j >= 0; j--) {
        if (dp[j]) {
            return total - 2 * j;
        }
    }

    return 0;
}
```

**變形 2：整數 DP（可達成的最大值）**

```java
// java
// LC 1049 - Last Stone Weight II
// IDEA: variant 2 — maximise the achievable subset sum <= total/2, answer = total - 2*best
// time = O(n * total), space = O(total)
public int lastStoneWeightII(int[] stones) {
    int total = 0;
    for (int stone : stones) {
        total += stone;
    }

    int target = total / 2;
    
    // dp[j] = maximum sum we can achieve ≤ j
    int[] dp = new int[target + 1];
    dp[0] = 0;  // Base: can make sum 0

    // For each stone
    for (int stone : stones) {
        // Iterate BACKWARDS to prevent reuse
        for (int j = target; j >= stone; j--) {
            // Either skip this stone (dp[j])
            // Or include it and add to best we could do with j-stone (dp[j-stone] + stone)
            dp[j] = Math.max(dp[j], dp[j - stone] + stone);
        }
    }

    return total - 2 * dp[target];
}
```

#### **為什麼要倒序迭代？（最關鍵的細節）**

```text
❌ WRONG: Forward iteration (causes reuse)
for (int j = stone; j <= target; j++) {
    dp[j] = dp[j] || dp[j - stone];
}
Problem: When we update dp[j], we're using the NEW value of dp[j-stone]
         which might have already been updated by the same stone in this iteration.
         This allows using the same stone multiple times!

Example with stone=3, target=9:
  j=3: dp[3] = dp[0] = true ✓
  j=6: dp[6] = dp[3] = true ✓ BUT dp[3] was just updated by the same stone!
  j=9: dp[9] = dp[6] = true ✓ Again, using same stone multiple times!

✅ CORRECT: Backward iteration (prevents reuse)
for (int j = target; j >= stone; j--) {
    dp[j] = dp[j] || dp[j - stone];
}
Reason: We process from right to left, so dp[j-stone] is always from the PREVIOUS iteration
        (before this stone was considered). So we use each stone only once.

Example with stone=3, target=9:
  j=9: dp[9] = dp[6] (old value from previous stone) ✓
  j=6: dp[6] = dp[3] (old value from previous stone) ✓
  j=3: dp[3] = dp[0] (old value from previous stone) ✓
```

#### **完整範例：Last Stone Weight II**

```text
stones = [2, 7, 4, 1, 8, 1]
total = 23
target = 23 / 2 = 11

Initial: dp = [T, F, F, F, F, F, F, F, F, F, F, F]

After stone 2:
  dp[2] = T (can make sum 2)
  dp = [T, F, T, F, F, F, F, F, F, F, F, F]

After stone 7:
  dp[9] = T (can make 2+7)
  dp[7] = T
  dp[2] = T (unchanged)
  dp = [T, F, T, F, F, F, F, T, F, T, F, F]

After stone 4:
  dp[11] = T (can make 7+4)
  dp[9] = T (unchanged)
  dp[6] = T (can make 2+4)
  dp[4] = T
  dp = [T, F, T, F, T, F, T, T, F, T, F, T]

... continue for remaining stones ...

Final: Find largest j ≤ 11 where dp[j] = T
       Result = 23 - 2 * j
```

#### **相似的 LeetCode 題目** 📚

| 題目 | 目標 | 轉換方式 | 複雜度 |
|---------|------|-----------------|-----------|
| **LC 1049: Last Stone II** | 最後一顆石頭的最小重量 | 分成兩組，讓差最小 | O(n × sum/2) |
| **LC 416: Partition Equal Subset** | 能否分成兩組和相等？ | 能否湊出 sum = total/2？ | O(n × sum/2) |
| **LC 494: Target Sum** | 數出湊到 target 的方法數 | 視為：正號組和為 sum1、負號組和為 sum2，解 sum1 - sum2 = target | O(n × sum) |
| **LC 879: Profitable Schemes** | 數出合法的獲利方案數 | 對 (人數, 利潤) 做 DP | O(n × k × p) |

**轉換範例**：

**LC 416 (Partition Equal Subset)**：
```text
Question: Can we partition into two equal subsets?
Answer: Can we achieve sum = total/2?
DP: boolean[] dp where dp[j] = can we make sum j?
Return: dp[total/2]
```

**LC 494 (Target Sum)**：
```text
Question: Assign +/- to reach target T
Transformation: Let sum1 = sum of items with +
                Let sum2 = sum of items with -
                sum1 - sum2 = T
                sum1 + sum2 = total (all items)
                
                Solving: sum1 = (total + T) / 2

Feasibility first (both are required before the DP runs):
    abs(T) > total          -> 0 ways: even all-plus or all-minus cannot reach T
    (total + T) is odd      -> 0 ways: sum1 would not be an integer

So: This is 0/1 knapsack! Find count of subsets with sum = (total + T) / 2
DP: int[] dp where dp[j] = count of ways to make sum j
Return: dp[(total + T) / 2]
```

```java
// java
// LC 494 - Target Sum
// IDEA: reduce "assign +/-" to "count subsets summing to (total + T) / 2", then 0/1 knapsack
// time = O(n * target), space = O(target)
public int findTargetSumWays(int[] nums, int target) {
    int total = 0;
    for (int x : nums) total += x;

    // NOTE !!! guard before the division — otherwise `sub` is negative or non-integral
    if (Math.abs(target) > total || ((total + target) % 2) != 0) return 0;

    int sub = (total + target) / 2;
    int[] dp = new int[sub + 1];
    dp[0] = 1;                                  // one way to make 0: pick nothing
    for (int num : nums) {
        for (int j = sub; j >= num; j--) {      // backward -> each num used at most once
            dp[j] += dp[j - num];
        }
    }
    return dp[sub];
}
```

#### **常見陷阱** ⚠️

1. **正序迭代而不是倒序**
   - 會允許同一個物品被重複使用多次
   - 0/1 背包一定要倒序迭代

2. **DP 轉移式寫錯**
   - 布林版：`dp[j] = dp[j] || dp[j - weight]`
   - 整數和版：`dp[j] = Math.max(dp[j], dp[j - weight] + weight)`
   - 計數方法數版：`dp[j] += dp[j - weight]`
   - 千萬別搞混！

3. **沒認出「partition」這個模式**
   - 「兩組之間的差」→ 想到分割
   - 「分成兩隊」→ 想到分割
   - 「切分陣列」→ 想到分割

4. **總和造成整數溢位**
   - 總和很大時要小心溢位
   - 必要時考慮改用 long

---

#### **⚡ 速查：迴圈順序 → 題型**

| 外層迴圈 | 內層迴圈 | 模式名稱 | 使用時機 | 題目 |
|------------|------------|--------------|----------|----------|
| **物品／硬幣** | **目標／金額** | 組合 | 數不重複的集合（順序不重要） | LC 518 |
| **目標／金額** | **物品／硬幣** | 排列 | 數序列（順序重要） | LC 377 |
| **物品**（倒序） | **容量** | 0/1 背包 | 每個物品只用一次，求最大／最小 | LC 416, 494 |
| **物品**（正序） | **容量** | 完全背包 | 物品可無限使用，求最大／最小 | LC 322 |

---

#### **快速比較表**

| 面向 | 組合 (LC 518) | 排列 (LC 377) |
|--------|----------------------|----------------------|
| **迴圈順序** | 硬幣 → 金額 | 金額 → 硬幣 |
| **順序重要嗎？** | ❌ 不重要：[1,2] = [2,1] | ✅ 重要：[1,2] ≠ [2,1] |
| **題目類型** | Coin Change II | Combination Sum IV |
| **外層迴圈** | `for (int coin : coins)` | `for (int i = 1; i <= target; i++)` |
| **內層迴圈** | `for (int i = coin; i <= amount; i++)` | `for (int num : nums)` |
| **範例** | amount=3, coins=[1,2] → 2 種 | target=3, nums=[1,2] → 3 種 |

---

#### **模式 1：組合（外層：硬幣，內層：金額）**
```java
// java
// IDEA: coins outer, amount inner -> each coin is offered once, so sets are counted
// time = O(n * amount), space = O(amount)
// LC 518: Coin Change II - Count combinations
// Example: [1,2] and [2,1] are the SAME combination
public int change(int amount, int[] coins) {
    int[] dp = new int[amount + 1];
    dp[0] = 1; // Base case: 1 way to make amount 0

    // OUTER LOOP: Iterate through each coin
    // This ensures we process all uses of one coin before moving to the next,
    // which prevents duplicate combinations like [1,2] and [2,1].
    for (int coin : coins) {
        // INNER LOOP: Update dp table for all amounts reachable by this coin
        for (int i = coin; i <= amount; i++) {
            // Number of ways to make amount 'i' is:
            // (Current ways) + (Ways to make 'i - coin')
            dp[i] += dp[i - coin];
        }
    }

    return dp[amount];
}
```

**為什麼這樣成立**：
- 一次只處理一種硬幣（例如先處理所有的 1，再處理所有的 2，接著所有的 5）
- 當你開始用硬幣 `2` 時，硬幣 `1` 的所有計算都已經做完了
- 不可能在 `2` 之後再放一個 `1`，因此強制產生非遞減的順序
- 結果：只會數到**組合**（順序不重要）

**範例追蹤**：`coins = [1,2], amount = 3`
```text
After coin 1: dp = [1, 1, 1, 1]  // {}, {1}, {1,1}, {1,1,1}
After coin 2: dp = [1, 1, 2, 2]  // + {2}, {1,2}
Result: 2 combinations → {1,1,1}, {1,2}
```

#### **模式 2：排列（外層：金額，內層：硬幣）**
```java
// java
// IDEA: amount outer, coins inner -> every coin is retried at every amount, so orderings count
// time = O(n * target), space = O(target)
// LC 377: Combination Sum IV - Count permutations
// Example: [1,2] and [2,1] are DIFFERENT permutations
public int combinationSum4(int[] nums, int target) {
    int[] dp = new int[target + 1];
    dp[0] = 1;

    // OUTER LOOP: Iterate through each amount
    // For each amount, try all coins to see which was "last added"
    for (int i = 1; i <= target; i++) {
        // INNER LOOP: Try each coin for current amount
        for (int num : nums) {
            if (i >= num) {
                dp[i] += dp[i - num];
            }
        }
    }

    return dp[target];
}
```

**為什麼這樣數的是排列**：
- 對每個金額問：「我加進去的**最後一枚硬幣**是哪一枚？」
- 每一枚硬幣在每一步都可以當那個「最後一枚」
- 結果：**排列**（順序重要）

**範例追蹤**：`nums = [1,2], target = 3`
```text
dp[1]: Use 1 → [1] (1 way)
dp[2]: Use 1 → [1,1], Use 2 → [2] (2 ways)
dp[3]: From dp[2] add 1 → [1,1,1], [2,1]
       From dp[1] add 2 → [1,2]
Result: 3 permutations → {1,1,1}, {1,2}, {2,1}
```

#### **比較表**

| 迴圈順序 | 結果型態 | 題目範例 | 使用情境 |
|------------|-------------|-----------------|----------|
| **外層：硬幣**<br>內層：金額 | **組合**<br>（順序不重要） | LC 518 Coin Change II | 數不重複的硬幣組合 |
| **外層：金額**<br>內層：硬幣 | **排列**<br>（順序重要） | LC 377 Combination Sum IV | 數不同的排列順序 |

#### **🔥 並排程式碼比較**

**LC 518: Coin Change II（組合）**
```java
// java
// LC 518 - Coin Change II
// IDEA: combinations — coins outer
// time = O(n * amount), space = O(amount)
public int change(int amount, int[] coins) {
    int[] dp = new int[amount + 1];
    dp[0] = 1; // Base: 1 way to make 0

    // CRITICAL: Coin outer loop = COMBINATIONS
    for (int coin : coins) {              // ← Process coins one by one
        for (int i = coin; i <= amount; i++) {  // ← Update all amounts for this coin
            dp[i] += dp[i - coin];
        }
    }
    return dp[amount];
}

// Example: amount=3, coins=[1,2]
// Result: 2 combinations
// {1,1,1}, {1,2}  (Note: [1,2] and [2,1] counted as same)
```

**LC 377: Combination Sum IV（排列）**
```java
// java
// LC 377 - Combination Sum IV
// IDEA: permutations — amount outer
// time = O(n * target), space = O(target)
public int combinationSum4(int[] nums, int target) {
    int[] dp = new int[target + 1];
    dp[0] = 1; // Base: 1 way to make 0

    // CRITICAL: Amount outer loop = PERMUTATIONS
    for (int i = 1; i <= target; i++) {   // ← Process each amount
        for (int num : nums) {            // ← Try every number for this amount
            if (i >= num) {
                dp[i] += dp[i - num];
            }
        }
    }
    return dp[target];
}

// Example: target=3, nums=[1,2]
// Result: 3 permutations
// {1,1,1}, {1,2}, {2,1}  (Note: [1,2] and [2,1] are different)
```

#### **🔍 詳細追蹤比較：為什麼迴圈順序有差**

**範例：nums/coins = [1, 2]、target/amount = 3**

**LC 518（組合 — 硬幣在外層）：**
```text
Initialize: dp = [1, 0, 0, 0]

Process coin 1:
  i=1: dp[1] += dp[0] = 1    → [1, 1, 0, 0]  // ways: {1}
  i=2: dp[2] += dp[1] = 1    → [1, 1, 1, 0]  // ways: {1,1}
  i=3: dp[3] += dp[2] = 1    → [1, 1, 1, 1]  // ways: {1,1,1}

Process coin 2:
  i=2: dp[2] += dp[0] = 1+1=2 → [1, 1, 2, 1]  // ways: {1,1}, {2}
  i=3: dp[3] += dp[1] = 1+1=2 → [1, 1, 2, 2]  // ways: {1,1,1}, {1,2}
                                              // Note: Can't get {2,1} because
                                              // all coin-1 uses are done before coin-2

Final: dp[3] = 2  ✅ Only {1,1,1} and {1,2}
```

**LC 377（排列 — 金額在外層）：**
```text
Initialize: dp = [1, 0, 0, 0]

i=1 (building sum 1):
  Try 1: dp[1] += dp[0] = 1   → [1, 1, 0, 0]  // ways: {1}
  Try 2: skip (2 > 1)

i=2 (building sum 2):
  Try 1: dp[2] += dp[1] = 1   → [1, 1, 1, 0]  // {1} + 1 = {1,1}
  Try 2: dp[2] += dp[0] = 1+1=2 → [1, 1, 2, 0]  // {} + 2 = {2}

i=3 (building sum 3):
  Try 1: dp[3] += dp[2] = 2   → [1, 1, 2, 2]  // {1,1} + 1 = {1,1,1}
                                              // {2} + 1 = {2,1}  ✅
  Try 2: dp[3] += dp[1] = 2+1=3 → [1, 1, 2, 3]  // {1} + 2 = {1,2}  ✅

Final: dp[3] = 3  ✅ All three: {1,1,1}, {1,2}, {2,1}
```

**關鍵洞見：**
- **LC 518（硬幣在外層）**：處理完硬幣 1 之後就再也不回頭。這強制產生一個標準順序（所有的 1 都排在所有的 2 前面），因此不會同時數到 {1,2} 和 {2,1}。
- **LC 377（金額在外層）**：對每個和問「加進去的**最後**一個數字是誰？」每個數字都可以當「最後一個」，所以 {1,2} 和 {2,1} 都會被算到。

---

#### **什麼時候用哪一種**

**用組合（硬幣 → 金額）**的時機：
- 題目問「有幾種方法」但不考慮順序
- [1,2,5] 和 [2,1,5] 應該只算一次
- 關鍵字：「combinations」、「unique sets」

**用排列（金額 → 硬幣）**的時機：
- 題目問的是不同的序列／順序
- [1,2] 和 [2,1] 應該分開計算
- 關鍵字：「permutations」、「different orderings」、「sequences」

#### **完整 Java 範例：LC 518 Coin Change II**
```java
// java
// LC 518 - Coin Change II
// IDEA: count the ways to form each amount; coins outer keeps `{1,2}` and `{2,1}` as one
// time = O(n * amount), space = O(amount)
public int change(int amount, int[] coins) {
    // dp[i] = total number of combinations that make up amount i
    int[] dp = new int[amount + 1];

    // Base case: There is exactly 1 way to make 0 amount (empty set)
    dp[0] = 1;

    // CRITICAL: Coin outer loop = COMBINATIONS
    for (int coin : coins) {
        for (int i = coin; i <= amount; i++) {
            dp[i] += dp[i - coin];
        }
    }

    return dp[amount];
}
```

**測試案例**：
```text
Input: amount = 5, coins = [1,2,5]
Output: 4
Combinations: {5}, {2,2,1}, {2,1,1,1}, {1,1,1,1,1}

Input: amount = 3, coins = [2]
Output: 0
Explanation: Cannot make 3 with only coins of 2
```

#### **📚 題目對照**

| 題目 | LC # | 迴圈順序 | 它數的是什麼 | 檔案位置 |
|---------|------|------------|----------------|----------------|
| **Coin Change II** | 518 | 硬幣 → 金額 | 組合（順序不重要） | `leetcode_java/.../CoinChange2.java` |
| **Combination Sum IV** | 377 | 金額 → 硬幣 | 排列（順序重要） | `leetcode_java/.../CombinationSumIV.java` |

**💡 記憶小技巧：**
- **「Coin first」= Combinations**（兩個都是 C 開頭）
- **「Amount first」= Arrangements／Permutations**（兩個都是 A 開頭）

---

#### **📝 最終總結：完整模式比較**

| 面向 | LC 518: Coin Change II<br>（組合） | LC 377: Combination Sum IV<br>（排列） |
|--------|------------------------------------------|---------------------------------------------|
| **它數的是什麼** | 不重複的集合（順序不重要） | 不同的序列（順序重要） |
| **範例** | [1,2] = [2,1]（相同） | [1,2] ≠ [2,1]（不同） |
| **外層迴圈** | `for (int coin : coins)` | `for (int i = 1; i <= target; i++)` |
| **內層迴圈** | `for (int i = coin; i <= amount; i++)` | `for (int num : nums)` |
| **DP 轉移式** | `dp[i] += dp[i - coin]` | `dp[i] += dp[i - num]` |
| **base case** | `dp[0] = 1` | `dp[0] = 1` |
| **nums=[1,2], target=3<br>時的結果** | **2** 種組合：<br>{1,1,1}, {1,2} | **3** 種排列：<br>{1,1,1}, {1,2}, {2,1} |
| **為什麼成立** | 先把硬幣 1 完全處理完再處理硬幣 2，強制產生標準順序 → 不會出現 {2,1} | 對每個和都讓每個數字當「最後一個」→ 允許所有順序 |
| **檔案位置** | `CoinChange2.java` | `CombinationSumIV.java` |

**🔥 唯一的差別：**
```java
// java
// IDEA: the two nestings printed together — the only difference is which loop is outer
// time = O(n * amount), space = O(amount)
// LC 518: Combinations
for (int coin : coins)              // ← ITEM OUTER
    for (int i = coin; i <= amount; i++)

// LC 377: Permutations
for (int i = 1; i <= target; i++)   // ← TARGET OUTER
    for (int num : nums)
```

**兩者用的是完全相同的轉移式：`dp[i] += dp[i - item]`**

---

### 為什麼守衛條件是 `if (i - coin >= 0)` 而不是 `if (i == coin)`

**🔑 問題**：為什麼要用 `if (i >= coin)` 而不是 `if (i == coin)`？

這是理解動態規劃如何在既有**子問題**答案上疊加的基礎概念。

#### **簡短的答案**
- `i == coin` 只檢查**單一一枚硬幣**是否剛好等於金額
- `i >= coin` 檢查的是這枚硬幣能不能**搭配**之前算出的某個和一起湊到這個金額

#### **`i - coin >= 0` 背後的邏輯**

計算 `dp[i]` 時，我們不是只在找一枚剛好等於 `i` 的硬幣，而是在找一枚硬幣 `coin`，使得 `i` 減掉它之後剩下的餘額是我們**已經知道怎麼解**的。

- **`i`**：現在想湊出的總金額
- **`coin`**：剛拿起來的那枚硬幣的面額
- **`i - coin`**：「餘額」，也就是還剩下要湊的金額

只要 `i - coin >= 0`，這枚硬幣就放得下，而餘額是一個我們**已經算過**的子問題 — 因為表格是從 `0` 一路填到 `amount`。`== 0` 這個情況並沒有被特判：它讀的是 `dp[0]`，而 base case 早就把它設好了。這正是守衛條件用 `>=` 而不是 `>` 的原因。

**DP 會回頭去看 `dp[i - coin]`**，直接重用那個答案！

#### **一個具體的例子**

假設 `coins = [2]`，我們要算 `dp[4]`（怎麼湊出 4 分錢）。

1. 試硬幣 `coin = 2`
2. `i - coin` 是 `4 - 2 = 2`
3. 因為 `2 > 0`，我們不會停下來，而是去看 `dp[2]`
4. `dp[2] = 1` 已經算過了（用一枚 2 分硬幣湊出 2 分）
5. 所以 `dp[4] = dp[2] + 1 = 2`

**如果只用 `if (i - coin == 0)`：**
- 我們永遠只會發現 `dp[2] = 1`
- 算到 `dp[4]` 時，條件 `4 - 2 == 0` 會是 **false**
- 就會錯誤地下結論說 4 分錢湊不出來！

#### **三種情況**

檢查 `i - coin` 時：

| `i - coin` 的結果 | 意義 | 動作 |
| --- | --- | --- |
| **負數**（`< 0`） | 這枚硬幣對這個金額來說太大了 | 跳過這枚硬幣 |
| **零**（`== 0`） | 這一枚硬幣剛好等於金額 | `dp[i] = 1` |
| **正數**（`> 0`） | 這枚硬幣放得下，還要再看「餘額」 | `dp[i] = dp[remainder] + 1` |

後兩列其實是**同一行程式碼** — `dp[i] = dp[i - coin] + 1` — 因為 `dp[0]` 已經被
初始化成 `0`。這就是為什麼一個守衛條件 `i - coin >= 0` 就同時涵蓋了兩者。

#### **💡 關鍵洞見**

`if (i >= coin)` 這個條件同時涵蓋了「硬幣剛好對上金額」**以及**「硬幣只是更大拼圖的其中一塊」這兩種情況。

#### **完整範例與追蹤**

**輸入**：`coins = [1,2,5], amount = 11`

**設定**：
- **DP 陣列**：`int[12]`（索引 0 到 11）
- **初始化**：`dp[0] = 0`，其餘全設為 `12`（我們的「無窮大」）

**逐步追蹤**：

**金額 1 到 4**：
- **`i=1` 時**：只有硬幣 `1` 放得下（`1 >= 1`）。`dp[1] = dp[0] + 1 = 1`
- **`i=2` 時**：
  - 硬幣 `1`：`dp[2] = dp[1] + 1 = 2`
  - 硬幣 `2`：`dp[2] = dp[0] + 1 = 1`（勝出：最小值是 1）
- **`i=3` 時**：
  - 硬幣 `1`：`dp[3] = dp[2] + 1 = 2`
  - 硬幣 `2`：`dp[3] = dp[1] + 1 = 2`
  - `dp[3] = 2`（例如 `2+1` 或 `1+1+1`）
- **`i=4` 時**：
  - 硬幣 `1`：`dp[4] = dp[3] + 1 = 3`
  - 硬幣 `2`：`dp[4] = dp[2] + 1 = 2`
  - `dp[4] = 2`（例如 `2+2`）

**金額 5（第一次大跳躍）**：
- 硬幣 `1`：`dp[5] = dp[4] + 1 = 3`
- 硬幣 `2`：`dp[5] = dp[3] + 1 = 3`
- **硬幣 `5`**：`dp[5] = dp[0] + 1 = 1`
- **結果**：`dp[5] = 1`（剛好對上）

**金額 10**：
- 硬幣 `1`：`dp[10] = dp[9] + 1 = 4`
- 硬幣 `2`：`dp[10] = dp[8] + 1 = 4`
- **硬幣 `5`**：`dp[10] = dp[5] + 1 = 2`
- **結果**：`dp[10] = 2`（代表 `5+5`）

**最終目標：金額 11**：
1. **試硬幣 `1`**：
   - 餘額：`11 - 1 = 10`
   - 查 `dp[10]`：是 `2`
   - 計算：`dp[11] = dp[10] + 1 = 3`

2. **試硬幣 `2`**：
   - 餘額：`11 - 2 = 9`
   - 查 `dp[9]`：是 `3`（例如 `5+2+2`）
   - 計算：`dp[11] = dp[9] + 1 = 4`

3. **試硬幣 `5`**：
   - 餘額：`11 - 5 = 6`
   - 查 `dp[6]`：是 `2`（例如 `5+1`）
   - 計算：`dp[11] = dp[6] + 1 = 3`

**最後比較**：`dp[11] = min(3, 4, 3) = 3`

#### **為什麼餘額 `i - coin > 0` 這件事行得通**

計算 **11** 的時候，演算法完全不需要「重新解」怎麼湊出 10 或 6，只要查表就好：
- 「喔，我知道湊出 **10** 的最佳方式是 **2** 枚硬幣（`5+5`）」
- 「再加上我手上這枚 **1**，就用 **3** 枚硬幣湊出了 **11**（`5+5+1`）」

#### **總結表（精簡版）**

| i | 0 | 1 | 2 | 3 | 4 | 5 | 6 | 7 | 8 | 9 | 10 | **11** |
| --- | --- | --- | --- | --- | --- | --- | --- | --- | --- | --- | --- | --- |
| **dp[i]** | 0 | 1 | 1 | 2 | 2 | 1 | 2 | 2 | 3 | 3 | 2 | **3** |

#### **DP 的程式碼模式**

```java
// java
// LC 322 - Coin Change
// IDEA: min coins per amount; order is irrelevant so either nesting works
// time = O(n * amount), space = O(amount)
public int coinChange(int[] coins, int amount) {
    if (amount == 0) return 0;

    // dp[i] = min coins to make amount i
    int[] dp = new int[amount + 1];

    // Initialize with "Infinity" (amount + 1 is safe)
    Arrays.fill(dp, amount + 1);

    // Base case: 0 coins needed for 0 amount
    dp[0] = 0;

    // Iterate through every amount from 1 to amount
    for (int i = 1; i <= amount; i++) {
        // For each amount, try every coin
        for (int coin : coins) {
            // CRITICAL CONDITION: Check if coin fits
            if (i >= coin) {
                // DP equation: Min of (current value) OR
                // (1 coin + coins needed for remainder)
                dp[i] = Math.min(dp[i], dp[i - coin] + 1);
            }
        }
    }

    // If value is still "Infinity", we couldn't reach it
    return dp[amount] > amount ? -1 : dp[amount];
}
```

**參考**：詳細實作見 `leetcode_java/src/main/java/LeetCodeJava/DynamicProgramming/CoinChange.java:356-408`。

---

## 模式選擇策略

```text
Is each item reusable?
│
├─ NO  ──► 0/1 Knapsack
│          for item in items:
│              for w in range(W, weight-1, -1):     # BACKWARD
│          └─ asks "can we hit the sum?"  -> boolean dp
│          └─ asks "how many ways?"       -> dp[j] += dp[j-w]
│          └─ asks "best value?"          -> dp[j] = max(dp[j], dp[j-w]+v)
│
└─ YES ──► Does order matter?
           │
           ├─ NO  (combinations, {1,2} == {2,1})  ──► items outer, amount inner  [518]
           ├─ YES (permutations, {1,2} != {2,1})  ──► amount outer, items inner  [377]
           └─ Min/max only (order irrelevant)     ──► either nesting             [322, 279]
```

## 總結

| 每一列只記住一件事的話 | ……就記這個 |
|---|---|
| **0/1 vs 完全背包** | *內層迴圈的方向*：倒序阻止重複使用，正序允許重複使用 |
| **組合 vs 排列** | *迴圈的巢狀順序*：物品在外層數的是集合，金額在外層數的是序列 |
| **分割類題目** | 「切成相等的兩半」⇒ 對 `total / 2` 做子集合和 |
| **LC 494 Target Sum** | `sum1 = (total + T) / 2`，但要先檢查 `abs(T) <= total` 且 `(total + T)` 為偶數 |
| **守衛條件** | 是 `i - coin >= 0`，不是 `> 0` — `== 0` 的情況讀到的是已初始化的 `dp[0]` |
| **有界背包** | 把每個物品二進位拆分成 `1, 2, 4, …` 份副本，再跑一般的 0/1 |
