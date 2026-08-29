# 位元遮罩 DP（狀態壓縮）

> **範圍** — 狀態是「一個用整數編碼的子集合」的 DP：遮罩運算、子遮罩列舉、TSP 與指派問題的模板，以及 n <= 20 這條規模上限。
> **另見**：[dp.md](./dp.md) — 精簡版的位元遮罩模板，以及它在各 DP 模式中的定位；[bit_manipulation.md](./bit_manipulation.md) — 純粹的位元技巧，不談 DP。

## LeetCode 題目清單

- [Dynamic Programming](https://leetcode.com/problem-list/dynamic-programming/)

## 概觀

### 關鍵性質

- **複雜度**：標準的子集合 DP 是 `O(2^n * n)` 時間、`O(2^n)` 空間 — 所以 `n <= 20` 是實務上的
  天花板（`2^20` ≈ 1e6）。子遮罩列舉的成本是 `O(3^n)`。
- **核心想法**：DP 的狀態是一個**集合**，而 `n` 個元素的集合就只是一個 `n` 位元的整數，
  所以整張 memo 表就是一個以該整數為索引的一維陣列。
- **什麼時候用**：題目要記錄「我已經用過／走過哪些元素」、`n` 很小，而且暴力解會是一個排列搜尋。

### 參考資料

- [dp.md](./dp.md) — 一個螢幕就講完的位元遮罩模板
- [bit_manipulation.md](./bit_manipulation.md) — 單獨看的位元技巧

## 模板與演算法

### 狀態壓縮模式

**什麼時候該用位元遮罩 DP**：
- 狀態空間很小（≤ 20 個元素）
- 需要記錄哪些元素被選了／走過了
- 排列／組合類的問題
- 旅行推銷員問題的各種變形

**常見的位元遮罩運算**：
```python
# python
# IDEA: the bit vocabulary every bitmask DP is written in
# Check if i-th bit is set
if mask & (1 << i):
    pass

# Set i-th bit
new_mask = mask | (1 << i)

# Unset i-th bit
new_mask = mask & ~(1 << i)

# Iterate through all submasks
submask = mask
while submask:
    # Process submask
    submask = (submask - 1) & mask
```

**Java 的位元遮罩運算**：
```java
// java
// IDEA: the same bit vocabulary in Java
// Check if i-th bit is set
if ((mask & (1 << i)) != 0) {
    // i-th item is included
}

// Set i-th bit
int newMask = mask | (1 << i);

// Unset i-th bit
int newMask = mask & ~(1 << i);

// Toggle i-th bit
int newMask = mask ^ (1 << i);

// Count number of set bits
int count = Integer.bitCount(mask);

// Get lowest set bit
int lowestBit = mask & (-mask);

// Iterate through all subsets
for (int mask = 0; mask < (1 << n); mask++) {
    // Process mask
}

// Iterate through all submasks of mask
for (int submask = mask; submask > 0; submask = (submask - 1) & mask) {
    // Process submask
}
```

---

#### **模式 1：走遍所有節點（TSP 變形）**

**題型**：找出恰好走過所有節點一次的最短路徑

**狀態定義**：`dp[mask][i]` = 走完 `mask` 中所有節點、且停在節點 `i` 的最小成本

**轉移**：對每個還沒走過的節點 `j`，試著從目前的節點 `i` 走過去

**時間複雜度**：O(2^n × n²)
**空間複雜度**：O(2^n × n)

**範例**：LC 847 - Shortest Path Visiting All Nodes

```java
// java
// LC 847 - Shortest Path Visiting All Nodes
// IDEA: BFS over (mask, node); dp[mask][node] = fewest edges to stand on `node`
//       having visited exactly the set `mask`
// time = O(2^n * n^2), space = O(2^n * n)
public int shortestPathLength(int[][] graph) {
    int n = graph.length;
    int[][] dp = new int[1 << n][n];
    // NOTE !!! every state must start at +inf, not 0 — otherwise
    // `dp[nextMask][next] > dist + 1` is false for unvisited states and the BFS never expands
    for (int[] row : dp) {
        Arrays.fill(row, Integer.MAX_VALUE);
    }
    Queue<int[]> queue = new LinkedList<>();

    // Initialize: start from any node
    for (int i = 0; i < n; i++) {
        dp[1 << i][i] = 0;
        queue.offer(new int[]{1 << i, i});
    }

    int target = (1 << n) - 1;

    while (!queue.isEmpty()) {
        int[] curr = queue.poll();
        int mask = curr[0], node = curr[1];
        int dist = dp[mask][node];

        if (mask == target) {
            return dist;
        }

        for (int next : graph[node]) {
            int nextMask = mask | (1 << next);
            if (dp[nextMask][next] > dist + 1) {
                dp[nextMask][next] = dist + 1;
                queue.offer(new int[]{nextMask, next});
            }
        }
    }

    return -1;
}
```

---

#### **模式 2：指派問題**

**題型**：把 n 件工作指派給 n 個工人，讓總成本最小／最大

**狀態定義**：`dp[w][mask]` = 在**恰好 `w` 個工人**接下 `mask` 這些工作時的最小完工時間

**轉移**：讓工人 `w` 接下 `mask` 的某個子遮罩；剩下的交給工人 `1..w-1`

**時間複雜度**：O(k × 3^n) — 對所有遮罩跑子遮罩迴圈是 3^n，每個工人各跑一次
**空間複雜度**：O(k × 2^n)

> **工人數必須是狀態的一部分。** 只用 `dp[mask]` 去最小化 `max(dp[mask ^ sub], sum(sub))`，
> 等於是把 `mask` 拆成*不限數量*的組別，那算出來的是「工人愛用幾個就用幾個時的最小完工時間」，
> 而不是「恰好用 `k` 個」。

**範例**：LC 1723 - Find Minimum Time to Finish All Jobs

```java
// java
// LC 1723 - Find Minimum Time to Finish All Jobs
// IDEA: dp[w][mask] = min makespan after w workers have taken exactly the jobs in mask
// time = O(k * 3^n), space = O(k * 2^n)
public int minimumTimeRequired(int[] jobs, int k) {
    int n = jobs.length, full = (1 << n) - 1;

    // Precompute sum for each subset (lowest-set-bit recurrence, O(2^n))
    int[] subsetSum = new int[1 << n];
    for (int mask = 1; mask <= full; mask++) {
        int lowBit = mask & -mask;
        subsetSum[mask] = subsetSum[mask ^ lowBit] + jobs[Integer.numberOfTrailingZeros(lowBit)];
    }

    int[][] dp = new int[k + 1][1 << n];
    for (int[] row : dp) {
        Arrays.fill(row, Integer.MAX_VALUE);
    }
    dp[0][0] = 0;                       // 0 workers can only cover the empty job set

    for (int w = 1; w <= k; w++) {
        for (int mask = 0; mask <= full; mask++) {
            // NOTE !!! `sub` is worker w's share; `mask ^ sub` goes to workers 1..w-1
            for (int sub = mask; ; sub = (sub - 1) & mask) {
                int prev = dp[w - 1][mask ^ sub];
                if (prev != Integer.MAX_VALUE) {
                    dp[w][mask] = Math.min(dp[w][mask], Math.max(prev, subsetSum[sub]));
                }
                if (sub == 0) break;    // must run sub == 0 too (worker w idles), then stop
            }
        }
    }

    return dp[k][full];
}
```

---

#### **模式 3：帶限制的子集合選取**

**題型**：選出滿足特定限制的子集合

**狀態定義**：`dp[mask]` = 達成 `mask` 所代表狀態的方法數／最小成本

**轉移**：對每個元素，依照目前的遮罩決定要不要納入

**時間複雜度**：O(2^n × n)，或用子遮罩列舉時是 O(3^n)
**空間複雜度**：O(2^n)

**範例**：LC 691 - Stickers to Spell Word

```java
// java
// LC 691 - Stickers to Spell Word
// IDEA: dp[mask] = fewest stickers to cover the letters in `mask`
// time = O(2^n * stickers * n), space = O(2^n)
public int minStickers(String[] stickers, String target) {
    int n = target.length();
    int[] dp = new int[1 << n];
    Arrays.fill(dp, -1);
    dp[0] = 0;

    for (int mask = 0; mask < (1 << n); mask++) {
        if (dp[mask] == -1) continue;

        for (String sticker : stickers) {
            int newMask = mask;
            int[] counts = new int[26];

            for (char c : sticker.toCharArray()) {
                counts[c - 'a']++;
            }

            for (int i = 0; i < n; i++) {
                if ((mask & (1 << i)) == 0) {
                    char c = target.charAt(i);
                    if (counts[c - 'a'] > 0) {
                        counts[c - 'a']--;
                        newMask |= (1 << i);
                    }
                }
            }

            if (dp[newMask] == -1 || dp[newMask] > dp[mask] + 1) {
                dp[newMask] = dp[mask] + 1;
            }
        }
    }

    return dp[(1 << n) - 1];
}
```

---

#### **模式 4：分割成 K 個子集合**

**題型**：把 n 個元素在限制下分成 k 組

**狀態定義**：`dp[mask]` = `mask` 裡的元素能不能剛好切成幾個完整的組

**轉移**：試著從目前的狀態湊出一個完整的組

**時間複雜度**：O(2^n × n)
**空間複雜度**：O(2^n)

**範例**：LC 698 - Partition to K Equal Sum Subsets

```java
// java
// LC 698 - Partition to K Equal Sum Subsets
// IDEA: fill one bucket at a time; dp[mask] tracks the running remainder
// time = O(2^n * n), space = O(2^n)
public boolean canPartitionKSubsets(int[] nums, int k) {
    int sum = 0;
    for (int num : nums) sum += num;

    if (sum % k != 0) return false;

    int target = sum / k;
    int n = nums.length;
    boolean[] dp = new boolean[1 << n];
    int[] total = new int[1 << n];
    dp[0] = true;

    for (int mask = 0; mask < (1 << n); mask++) {
        if (!dp[mask]) continue;

        for (int i = 0; i < n; i++) {
            if ((mask & (1 << i)) != 0) continue;

            int newMask = mask | (1 << i);

            if (total[mask] % target + nums[i] <= target) {
                dp[newMask] = true;
                total[newMask] = total[mask] + nums[i];
            }
        }
    }

    return dp[(1 << n) - 1];
}
```

---

#### **位元遮罩 DP 常見模式總表**

| 模式 | 狀態定義 | 轉移 | 例題 |
|---------|-----------------|------------|------------------|
| **走遍所有節點** | dp[mask][i] = 走完 mask、停在 i 的成本 | 試下一個沒走過的節點 | LC 847, LC 943 |
| **指派** | dp[mask] = 指派完 mask 中工作的成本 | 把下一件工作派給工人 | LC 1723, LC 1986 |
| **子集合選取** | dp[mask] = 子集合 mask 的方法數／成本 | 納入／不納入下一個元素 | LC 691, LC 1434 |
| **分割** | dp[mask] = mask 能否分成完整的組 | 湊出完整的組 | LC 698, LC 1681 |
| **輪廓線 DP** | dp[i][mask] = 第 i 列、欄位狀態為 mask | 一列一列處理 | 鋪磚類問題 |

---

#### **進階技巧**

**1. 預先算好子集合的性質**：
```java
// java
// IDEA: subset-sum precompute via the lowest set bit
// time = O(2^n), space = O(2^n)
// Precompute sum for all subsets - O(2^n × n)
int[] subsetSum = new int[1 << n];
for (int mask = 0; mask < (1 << n); mask++) {
    for (int i = 0; i < n; i++) {
        if ((mask & (1 << i)) != 0) {
            subsetSum[mask] += arr[i];
        }
    }
}
```

**2. 子遮罩列舉 — O(3^n)**：
```java
// java
// IDEA: submask enumeration — the `(sub - 1) & mask` idiom
// time = O(3^n), space = O(1)
// For each mask, iterate through all its submasks
for (int mask = 0; mask < (1 << n); mask++) {
    for (int submask = mask; submask > 0; submask = (submask - 1) & mask) {
        // dp[mask] can be computed from dp[submask] and dp[mask ^ submask]
        dp[mask] = Math.min(dp[mask], dp[submask] + dp[mask ^ submask]);
    }
}
```

**3. SOS（Sum Over Subsets）DP — O(2^n × n)**：
```java
// java
// IDEA: SOS (sum over subsets) DP — n passes instead of 3^n
// time = O(2^n * n), space = O(2^n)
// For each mask, sum values of all its submasks
int[] dp = new int[1 << n];
// ... initialize dp ...

for (int i = 0; i < n; i++) {
    for (int mask = 0; mask < (1 << n); mask++) {
        if ((mask & (1 << i)) != 0) {
            dp[mask] += dp[mask ^ (1 << i)];
        }
    }
}
```

---

#### **複雜度分析**

| 技巧 | 時間複雜度 | 空間複雜度 | 適用情境 |
|-----------|----------------|------------------|----------|
| **基本位元遮罩** | O(2^n × n) | O(2^n) | 走遍全部、指派 |
| **子遮罩列舉** | O(3^n) | O(2^n) | 分割、子集合和 |
| **SOS DP** | O(2^n × n) | O(2^n) | 子集合求和 |
| **輪廓線 DP** | O(2^m × n) | O(2^m) | 網格鋪磚（m = 寬度） |

**可行規模的界線**：
- n ≤ 15：非常安全，約 32K 個狀態
- n ≤ 20：可行，約 1M 個狀態
- n ≤ 24：很吃緊，約 16M 個狀態（小心 TLE）
- n > 24：通常已經超出位元遮罩 DP 能處理的範圍

---

#### **面試提示**

1. **認出狀態壓縮**：
   - 關鍵字：「走遍全部」、「指派」、「分成 k 組」
   - 限制條件：n ≤ 20
   - 需要記錄子集合／走過的元素

2. **選對狀態**：
   - TSP 型：`dp[mask][last_node]`
   - 指派型：`dp[mask]`（隱含地派給第 k 個工人）
   - 分割型：`dp[mask]` 搭配取模檢查

3. **最佳化**：
   - 預先算好子集合的性質
   - 最短路徑類的問題改用 BFS
   - 子集合求和的查詢考慮用 SOS DP

4. **常見錯誤**：
   - 忘了初始化 `dp[0]`
   - 子遮罩迭代寫錯：要用 `(submask - 1) & mask`
   - 用某個位元前沒先檢查它是不是 1
   - `1 << n` 讓 int 溢位（n ≥ 31 要用 `1L << n`）

---

## 總結

| 步驟 | 要寫什麼 |
|------|---------------|
| **1. 檢查規模** | `n <= 20` 嗎？不是的話，位元遮罩 DP 就是選錯工具了。 |
| **2. 狀態** | `dp[mask]`（組別／工人數會影響答案時，再加第二個維度） |
| **3. 迭代順序** | `mask` 由小到大 — `mask` 的每個子遮罩數值上都比它小，所以一定已經算好了 |
| **4. 轉移** | 不是「加一個元素」（`O(2^n * n)`），就是「切出一個子遮罩」（`O(3^n)`） |
| **5. 答案** | `dp[(1 << n) - 1]` — 也就是全集 |

**真正會發生的三個 bug**

1. 遞迴式是用 `<` / `>` 來鬆弛，卻讓表格停在預設的 0 — 要先把它初始化成 `±infinity`。
2. 空的子遮罩其實是合法選擇時，卻寫成 `for (sub = mask; sub > 0; sub = (sub-1) & mask)` —
   這樣會整個跳過它。
3. `n >= 31` 時 `1 << n` 在 `int` 上溢位 — 要用 `1L << n`。
