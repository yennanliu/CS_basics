# DP 進階技巧

> **範圍** — DP 的附錄——第一輪該跳過的技巧：賽局理論／minimax DP、樹形與換根 DP、區間與字串 DP 的深入探討、機率與按步數計數的 DP、單調佇列與單調堆疊承載的 DP，以及從主表裁掉的長篇推導。
> **另見**：[dp.md](./dp.md) — 每個必會家族的標準模板；[dp_examples.md](./dp_examples.md) — 題解倉庫；[knapsack.md](./knapsack.md) — 完整的背包家族；[dp_string.md](./dp_string.md) — 雙序列格子；[dp_bitmask.md](./dp_bitmask.md) — 狀態壓縮；[dp_monotonic_stack.md](./dp_monotonic_stack.md) — 用堆疊承載 DP 值；[monotonic_queue.md](./monotonic_queue.md) — 滑動視窗最大值的結構。

## LeetCode 題目清單

- [Dynamic Programming](https://leetcode.com/problem-list/dynamic-programming/)
- [Memoization](https://leetcode.com/problem-list/memoization/)
- [Game Theory](https://leetcode.com/problem-list/game-theory/)
- [Probability and Statistics](https://leetcode.com/problem-list/probability-and-statistics/)

## 概覽

這裡的東西全都屬於第二輪。每一節都假設你已經熟悉 [dp.md](./dp.md) 裡對應的標準模板，然後補上更難的模式、另一種實作，或是主表只給了結論、沒給推導的那段推導。

### 關鍵性質
- **複雜度**：每節各自標示——區間深入探討是 O(n³)，bitmask 是 O(2^n · n)，其餘落在 O(n) 到 O(n²)
- **核心想法**：有趣的是狀態；狀態定對了，轉移就只是機械動作
- **什麼時候用**：直覺的狀態會給出錯的答案，或直覺解法的複雜度塞不進題目的限制

## 題型分類

| 章節 | 題目裡的訊號 | LC |
|---------|----------------------|----|
| **賽局理論／minimax** | 兩個玩家，雙方都最佳決策 | 486, 877, 1140, 1406 |
| **樹形 DP／換根** | 在樹上「對每個節點都算出 ……」 | 834, 2581, 337, 968 |
| **區間 DP 深入** | 操作的順序會改變結果 | 312, 1000, 546, 664 |
| **加權區間排程** | `(start, end, value)` 三元組，且不能重疊 | 1235, 1751, 354 |
| **切成 k 個區塊** | 「分成 d 天／k 個子陣列」 | 1335, 410 |
| **多一個狀態維度** | 下一步能做什麼，取決於你是怎麼走到這裡的 | 403, 309 |
| **按步數計數／機率** | 在很小的狀態圖上走固定步數 | 935, 688, 1269 |
| **單調佇列／堆疊 DP** | `dp[i] = max(dp[j])`，j 落在滑動視窗內；長條圖面積 | 1425, 84, 85 |
| **鏈式 DP** | 每個元素跟下一個只差一次操作 | 1048, 300, 329 |

## 賽局理論／Minimax DP

### 分類概覽

- **說明**：陣列上的雙人最佳對局；每位玩家只能從頭尾兩端取
- **範例**：LC 486（Predict the Winner）、LC 877（Stone Game）、LC 1140（Stone Game II）
- **模式**：dp[i][j] = 在子陣列 nums[i..j] 上的最大相對分差（當前玩家減對手）
- **核心想法**：當前玩家取完之後，對手會在剩下的子陣列上做最佳決策。減掉 `dp[sub]` 等於把視角翻面——對手拿到的最好結果，就是你的損失。
- **遞迴式**：`dp[i][j] = max(nums[i] - dp[i+1][j], nums[j] - dp[i][j-1])`
- **base case**：`dp[i][i] = nums[i]`（只剩一個元素，直接拿）
- **答案**：`dp[0][n-1] >= 0` 代表先手贏或平手

### Predict the Winner — LC 486

**模式**：兩個玩家輪流從陣列頭尾取數，雙方都最佳決策。判斷玩家 1 能不能贏。

**核心想法**：定義 `dp[i][j]` 為在子陣列 `nums[i..j]` 上的**最大分差**（當前玩家 − 對手）。當你取走 `nums[i]`，對手面對的是 `dp[i+1][j]`——那是*他的*最佳相對分數。減掉它，剩下的就是你的淨優勢。

**為什麼是減？** 你取完之後輪到對手。`dp[i+1][j]` 是對手在剩下子陣列上的最大優勢。從你的角度看，那個優勢是對你不利的，所以要減掉。

```java
// LC 486. Predict the Winner — 2D DP
public boolean predictTheWinner(int[] nums) {
    int n = nums.length;
    int[][] dp = new int[n][n];

    // Base case: single element → take it
    for (int i = 0; i < n; i++) {
        dp[i][i] = nums[i];
    }

    // Fill by increasing subarray length
    for (int len = 2; len <= n; len++) {
        for (int i = 0; i <= n - len; i++) {
            int j = i + len - 1;
            dp[i][j] = Math.max(
                nums[i] - dp[i + 1][j],   // pick left
                nums[j] - dp[i][j - 1]    // pick right
            );
        }
    }

    return dp[0][n - 1] >= 0;  // player 1 wins or ties
}
```

**相關 LeetCode 題目：**
| LC # | 題目 | 備註 |
|------|---------|-------|
| 486 | Predict the Winner | 核心的 minimax 區間 DP |
| 877 | Stone Game | 同一個結構；偶數長度時恆為 true（可用數學證明） |
| 1140 | Stone Game II | Minimax + 可變的取用數量（用後綴和最佳化） |
| 1406 | Stone Game III | 一維後綴 minimax，一次取 1-3 顆石頭 |
| 464 | Can I Win | Bitmask + minimax（狀態壓縮變形） |
| 294 | Flip Game II | 帶記憶化的 minimax |

---

## 樹形 DP 與換根

### 樹形 DP 的子模式

**子模式：**
1. **由下而上的樹形 DP**（標準做法）
   - 後序 DFS：每個節點的狀態由子節點算出來
   - 範例：LC 337（House Robber III）、LC 968（Binary Tree Cameras）
2. **換根 DP**（兩趟 DFS）
   - 先算出某一個根的答案，再用 O(N) 把根換到其他每個節點
   - 第一趟（後序）：算出各子樹大小，以及節點 0 當根時的答案
   - 第二趟（前序）：用數學公式把根從父節點換到子節點
   - 範例：LC 834（Sum of Distances in Tree）、LC 2581（Count Number of Possible Root Nodes）

### Sum of Distances in Tree — LC 834

> 兩趟 DFS：先算出以根為起點的距離，再用 ±子樹大小 調整父節點的答案來換根。

```java
// java
// LC 834
// Reference: leetcode_java/src/main/java/LeetCodeJava/Tree/SumOfDistancesInTree.java

/**
 * Problem: Given an undirected tree with n nodes, return an array where
 * answer[i] = sum of distances between node i and all other nodes.
 *
 * Example:
 * Input: n = 6, edges = [[0,1],[0,2],[2,3],[2,4],[2,5]]
 * Output: [8,12,6,10,10,10]
 *
 * WHY RE-ROOTING DP?
 *
 * A naive BFS/DFS from every node → O(N²), TLE for N = 3×10⁴.
 * Re-rooting DP solves it in O(N) with two DFS passes.
 *
 * KEY INSIGHT (Re-rooting Formula):
 *
 *   When moving root from node u to its child v:
 *     - count[v] nodes get 1 unit CLOSER  (they are in v's subtree)
 *     - (N - count[v]) nodes get 1 unit FARTHER (they are outside v's subtree)
 *
 *   Therefore:
 *     ans[v] = ans[u] - count[v] + (N - count[v])
 *
 * ALGORITHM:
 *
 *   Pass 1 — Post-order DFS (bottom-up):
 *     For each node u, compute:
 *       count[u] = size of u's subtree (including u)
 *       ans[u]   = sum of distances from u to all nodes in u's subtree
 *     After this pass, ans[root] is correct (total distance from root to all nodes).
 *
 *   Pass 2 — Pre-order DFS (top-down, re-root):
 *     For each edge u→v, compute ans[v] from ans[u] using the formula above.
 *     This propagates the correct answer to every node.
 *
 * Time:  O(N) — two DFS passes
 * Space: O(N) — adjacency list + count[] + ans[]
 */

// V0-1: Re-rooting DP (clean implementation)
int[] ans;
int[] count;
List<Set<Integer>> adj;
int n;

public int[] sumOfDistancesInTree(int n, int[][] edges) {
    this.n = n;
    ans = new int[n];
    count = new int[n];
    adj = new ArrayList<>();

    for (int i = 0; i < n; i++)
        adj.add(new HashSet<>());
    for (int[] e : edges) {
        adj.get(e[0]).add(e[1]);
        adj.get(e[1]).add(e[0]);
    }

    // Pass 1: Post-order DFS → compute count[] and ans[0]
    dfs1(0, -1);

    // Pass 2: Pre-order DFS → re-root to compute all ans[i]
    dfs2(0, -1);

    return ans;
}

// Post-order: count subtree sizes, accumulate distances for root
private void dfs1(int u, int parent) {
    count[u] = 1;
    for (int v : adj.get(u)) {
        if (v == parent) continue;
        dfs1(v, u);
        count[u] += count[v];
        // Distance from u to all nodes in v's subtree
        // = (dist from v to its subtree) + (number of nodes in v's subtree)
        ans[u] += ans[v] + count[v];
    }
}

// Pre-order: shift root from parent u to child v
private void dfs2(int u, int parent) {
    for (int v : adj.get(u)) {
        if (v == parent) continue;
        // Re-rooting formula:
        // count[v] nodes get closer, (n - count[v]) nodes get farther
        ans[v] = ans[u] - count[v] + (n - count[v]);
        dfs2(v, u);
    }
}

/**
 * STEP-BY-STEP EXAMPLE:
 *
 *        0
 *       / \
 *      1   2
 *         /|\
 *        3  4  5
 *
 * n = 6, edges = [[0,1],[0,2],[2,3],[2,4],[2,5]]
 *
 * Pass 1 (Post-order, root=0):
 *   count = [6, 1, 4, 1, 1, 1]
 *   ans   = [8, 0, 3, 0, 0, 0]   ← only ans[0]=8 is the full answer
 *
 *   ans[2] = ans[3]+count[3] + ans[4]+count[4] + ans[5]+count[5]
 *          = 0+1 + 0+1 + 0+1 = 3
 *   ans[0] = ans[1]+count[1] + ans[2]+count[2]
 *          = 0+1 + 3+4 = 8 ✓ (matches expected output)
 *
 * Pass 2 (Pre-order, re-root):
 *   ans[1] = ans[0] - count[1] + (6 - count[1]) = 8 - 1 + 5 = 12 ✓
 *   ans[2] = ans[0] - count[2] + (6 - count[2]) = 8 - 4 + 2 = 6  ✓
 *   ans[3] = ans[2] - count[3] + (6 - count[3]) = 6 - 1 + 5 = 10 ✓
 *   ans[4] = ans[2] - count[4] + (6 - count[4]) = 6 - 1 + 5 = 10 ✓
 *   ans[5] = ans[2] - count[5] + (6 - count[5]) = 6 - 1 + 5 = 10 ✓
 *
 * Final: [8, 12, 6, 10, 10, 10] ✓
 */
```

```python
# python
# LC 834 Sum of Distances in Tree
# Re-rooting DP template

# V0
# IDEA: Re-rooting DP (two-pass DFS)
class Solution:
    def sumOfDistancesInTree(self, n, edges):
        adj = [[] for _ in range(n)]
        for u, v in edges:
            adj[u].append(v)
            adj[v].append(u)

        count = [1] * n  # subtree size
        ans = [0] * n

        # Pass 1: Post-order DFS (iterative to avoid recursion limit)
        # Compute count[] and ans[0]
        order = []
        visited = [False] * n
        parent = [-1] * n
        stack = [0]
        visited[0] = True
        while stack:
            u = stack.pop()
            order.append(u)
            for v in adj[u]:
                if not visited[v]:
                    visited[v] = True
                    parent[v] = u
                    stack.append(v)

        # Process in reverse order (post-order)
        for u in reversed(order):
            for v in adj[u]:
                if v == parent[u]:
                    continue
                count[u] += count[v]
                ans[u] += ans[v] + count[v]

        # Pass 2: Pre-order DFS (re-root)
        for u in order:
            for v in adj[u]:
                if v == parent[u]:
                    continue
                ans[v] = ans[u] - count[v] + (n - count[v])

        return ans
```

#### 換根 DP —— 什麼時候用、怎麼用

**什麼時候用**：題目要求在樹上「對**每個**節點當根，算出某個彙總值」，而每個節點各跑一次 DFS/BFS 會是 O(N²)。

**兩趟的內容**：

1. **後序 DFS** —— 算出固定某個根（節點 `0`）的答案，往上回傳的路上順便收集每棵子樹的大小與貢獻。
2. **前序 DFS** —— 從父節點換根到子節點，用一條轉移公式由父節點的答案推出子節點的答案。

```text
ans[child] = f(ans[parent], subtree_info[child], n)

LC 834:  ans[v] = ans[u] - count[v] + (n - count[v])
         moving the root across edge u->v pulls count[v] nodes one step
         closer and pushes the other (n - count[v]) one step further
```

`O(N)` 時間、`O(N)` 空間——兩趟線性掃描取代了 N 次獨立走訪。

| 題目 | LC # | 換根公式／關鍵想法 |
|---------|------|-------------------------------|
| Sum of Distances in Tree | 834 | `ans[v] = ans[u] - count[v] + (n - count[v])` |
| Count Number of Possible Root Nodes | 2581 | 追蹤「好邊」的數量，換根時調整計數 |
| Minimum Edge Weight Equilibrium Queries | 2846 | 換根時同時追蹤邊的頻率 |
| Sum of Prefix Scores of Strings | 2416 | 同樣的兩趟做法，只是跑在字典樹上 |

**什麼時候該懷疑是換根**：題目在樹上說「對每個節點，算出 ……」；每個節點各走一次是 O(N²)；而且子節點的答案可以從父節點推出來。

## 區間 DP 深入探討

### 完整追蹤 — LC 312，`nums = [3,1,5,8]`

**追蹤範例**：`nums = [3,1,5,8]`

補上邊界之後：`[1, 3, 1, 5, 8, 1]`（索引 0-5）

```text
Building dp[0][5] (entire interval):
  Try k=1 (value 3) as LAST:
    coins = balloons[0] * balloons[1] * balloons[5] = 1 * 3 * 1 = 3
    total = 3 + dp[0][1] + dp[1][5]

  Try k=2 (value 1) as LAST:
    coins = balloons[0] * balloons[2] * balloons[5] = 1 * 1 * 1 = 1
    total = 1 + dp[0][2] + dp[2][5]

  Try k=3 (value 5) as LAST:
    coins = balloons[0] * balloons[3] * balloons[5] = 1 * 5 * 1 = 5
    total = 5 + dp[0][3] + dp[3][5]

  Try k=4 (value 8) as LAST:
    coins = balloons[0] * balloons[4] * balloons[5] = 1 * 8 * 1 = 8
    total = 8 + dp[0][4] + dp[4][5]

Result: dp[0][5] = 167
```


有些區間 DP 題用的是**閉區間**邊界，也就是 `dp[i][j]` 含 `i` 與 `j` 兩個元素。

**Python 實作**：
```python
def maxCoins_inclusive(nums):
    """Alternative: dp[i][j] includes balloons i through j"""
    n = len(nums)
    balloons = [1] + nums + [1]
    dp = [[0] * (n + 2) for _ in range(n + 2)]

    # Iterate through window lengths (len) from 1 to n
    for length in range(1, n + 1):
        for left in range(1, n - length + 2):
            right = left + length - 1

            # Try every balloon k in [left, right] as LAST to burst
            for k in range(left, right + 1):
                coins = (dp[left][k - 1] + dp[k + 1][right] +
                        balloons[left - 1] * balloons[k] * balloons[right + 1])
                dp[left][right] = max(dp[left][right], coins)

    return dp[1][n]
```

#### **由上而下（記憶化）做法**

```python
def maxCoins_topdown(nums):
    """Top-down with memoization"""
    balloons = [1] + nums + [1]
    memo = {}

    def dp(left, right):
        """Max coins from bursting balloons between left and right (exclusive)"""
        if left + 1 == right:  # No balloons between left and right
            return 0

        if (left, right) in memo:
            return memo[(left, right)]

        max_coins = 0
        # Try each balloon k as the last to burst
        for k in range(left + 1, right):
            coins = (balloons[left] * balloons[k] * balloons[right] +
                    dp(left, k) + dp(k, right))
            max_coins = max(max_coins, coins)

        memo[(left, right)] = max_coins
        return max_coins

    return dp(0, len(balloons) - 1)
```

**Java 由上而下**：
```java
public int maxCoins(int[] nums) {
    int n = nums.length;
    int[] balloons = new int[n + 2];
    balloons[0] = balloons[n + 1] = 1;
    for (int i = 0; i < n; i++) {
        balloons[i + 1] = nums[i];
    }

    int[][] dp = new int[n + 2][n + 2];
    for (int i = 0; i <= n; i++) {
        for (int j = 0; j <= n; j++) {
            dp[i][j] = -1;  // -1 means not computed yet
        }
    }

    return burst(balloons, 0, n + 1, dp);
}

private int burst(int[] balloons, int left, int right, int[][] dp) {
    if (left + 1 == right) return 0;  // No balloons between left and right

    if (dp[left][right] != -1) {
        return dp[left][right];
    }

    int maxCoins = 0;
    for (int k = left + 1; k < right; k++) {
        int coins = balloons[left] * balloons[k] * balloons[right];
        coins += burst(balloons, left, k, dp) + burst(balloons, k, right, dp);
        maxCoins = Math.max(maxCoins, coins);
    }

    dp[left][right] = maxCoins;
    return maxCoins;
}
```

#### **複雜度分析**

**時間複雜度**：O(n³)
- 外層迴圈（長度）：O(n)
- 中層迴圈（左邊界）：O(n)
- 內層迴圈（切點 k）：O(n)
- 每一格要花 O(n) 才算得出來

**空間複雜度**：O(n²)
- 大小為 `(n+2) × (n+2)` 的二維 DP 表
- 某些情況可以再壓，但一般來說就是要 O(n²)

**參考**：多種實作變形見 `leetcode_java/src/main/java/LeetCodeJava/DynamicProgramming/BurstBalloons.java`。LC 312 的標準模板在 [dp.md](./dp.md)。

### 辨識清單與常見錯誤

#### **模式辨識清單**

出現以下狀況就用區間 DP：
- ✅ 題目要處理陣列／序列裡的元素
- ✅ 操作的順序會影響結果
- ✅ 選定一個操作之後，子問題彼此獨立
- ✅ 最佳解可以由子問題的最佳解拼出來
- ✅ 關鍵字：「merge」、「burst」、「remove」、「split」、「multiply」

#### **要避開的常見錯誤**

1. **想成「第一個」而不是「最後一個」**：
   - ❌ 「先戳哪顆氣球？」→ 鄰居會變，相依關係說不清
   - ✅ 「最後戳哪顆氣球？」→ 子問題彼此獨立

2. **邊界處理錯誤**：
   - 明確補上邊界（像 `[1, ...nums..., 1]`）可以簡化邊界情況
   - 先決定邊界是閉的還是開的

3. **差一錯誤**：
   - 要一致：`dp[i][j]` 到底是開區間 `(i, j)` 還是閉區間 `[i, j]`
   - 迴圈範圍要跟著調

4. **迴圈順序錯誤**：
   - 一律從小區間往大區間建
   - 長度必須是最外層迴圈

### 「i 倒著跑 + j 正著跑」的迴圈順序（回文／子字串 DP）


**🎯 什麼時候用這個模式**

當 `dp[i][j]` 依賴以下這些格子時就用它：
- `dp[i+1][j-1]` — **內縮**的子字串（兩個邊界一起往內縮）
- `dp[i+1][j]` — **下面**那一列（i 變大）
- `dp[i][j-1]` — **左邊**那一行（j 變小）

經典題：**LC 516 Longest Palindromic Subsequence**、**LC 5 Longest Palindromic Substring**、**LC 647 Palindromic Substrings**。

**核心洞見：相依方向決定迴圈順序**

```text
dp[i][j] depends on:
    dp[i+1][j-1]   ← diagonal (i+1, j-1): both already computed
    dp[i+1][j]     ← row below: need i+1 before i  → loop i BACKWARD
    dp[i][j-1]     ← column left: need j-1 before j → loop j FORWARD
```

所以：**`i` 從 `n-1` 倒著跑到 `0`，`j` 從 `i+1` 正著跑到 `n-1`**。

**模板（Java）**：
```java
int n = s.length();
int[][] dp = new int[n][n];

// Base case: single characters
for (int i = 0; i < n; i++) dp[i][i] = 1;

// i backwards (so dp[i+1][...] is already filled)
for (int i = n - 1; i >= 0; i--) {
    // j forwards (so dp[...][j-1] is already filled)
    for (int j = i + 1; j < n; j++) {
        if (s.charAt(i) == s.charAt(j)) {
            dp[i][j] = dp[i + 1][j - 1] + 2;   // expand palindrome
        } else {
            dp[i][j] = Math.max(dp[i + 1][j], dp[i][j - 1]);  // skip i or j
        }
    }
}
return dp[0][n - 1];
```

**為什麼這裡不用以 `length` 為外層迴圈？**

以長度為外層的寫法（[dp.md](./dp.md) 裡的區間 DP 模板）也可以，但當轉移自然地讀成「邊界往外擴／往內縮」而不是「試每個切點 k」時，「i 倒著 + j 正著」比較直覺。

| 做法 | 外層迴圈 | 什麼時候用 |
|---|---|---|
| 以長度為外層（[dp.md](./dp.md)） | `length: 2 → n` | 有切點 `k` 的題（burst balloons、矩陣連乘） |
| i 倒著 + j 正著（本模板） | `i: n-1 → 0` | 邊界擴／縮的題（回文、同一字串上的 LCS） |

**相關 LeetCode 題目**：
- **LC 516** — Longest Palindromic Subsequence（正好就是上面的模板）
- **LC 5** — Longest Palindromic Substring（同樣的迴圈順序，dp 是布林）
- **LC 647** — Palindromic Substrings（數出所有回文）
- **LC 1048** — Longest String Chain（DFS+memo 或依長度排序後 DP；見 `LongestStringChain.java`）
- **LC 1312** — Minimum Insertion Steps to Make a String Palindrome
- **LC 730** — Count Different Palindromic Subsequences

---

### 通用的區間 DP 骨架

切點形式，`cost(i, j)` 留成抽象的：

```python
def interval_dp(arr):
    """DP for interval/subarray problems"""
    n = len(arr)
    # dp[i][j] = optimal value for interval [i, j]
    dp = [[0] * n for _ in range(n)]

    # Base case: single elements
    for i in range(n):
        dp[i][i] = arr[i]

    # Iterate by interval length
    for length in range(2, n + 1):
        for i in range(n - length + 1):
            j = i + length - 1
            # Try all split points
            for k in range(i, j):
                dp[i][j] = max(dp[i][j],
                              dp[i][k] + dp[k+1][j] + cost(i, j))

    return dp[0][n-1]
```


同一副骨架在矩陣連乘上的特化版：

#### **區間 DP 模板**：
```python
def interval_dp(arr):
    """Matrix chain multiplication style"""
    n = len(arr)
    dp = [[0] * n for _ in range(n)]

    # Length of interval
    for length in range(2, n + 1):
        for i in range(n - length + 1):
            j = i + length - 1
            dp[i][j] = float('inf')

            # Try all possible split points
            for k in range(i, j):
                cost = dp[i][k] + dp[k+1][j] + arr[i] * arr[k+1] * arr[j+1]
                dp[i][j] = min(dp[i][j], cost)

    return dp[0][n-2] if n > 1 else 0
```

---

## 一維陣列大小與迴圈順序深入探討

### 實體的階梯 vs 目標 — LC 746


在跟移動有關的題目（爬樓梯、走路徑）裡，「目標」是最後一個索引再往**後**一格。

**範例：LC 70（Climbing Stairs）／LC 746（Min Cost Climbing Stairs）**

若 `cost = [10, 15, 20]`（索引 0、1、2）：
- 這些是你可以踩的階梯
- 「地板」（目標）在索引 **3**
- 所以 `dp` 陣列需要 `n + 1` 的大小，才裝得下落地點

```python
# LC 746: Min Cost Climbing Stairs
def minCostClimbingStairs(cost):
    n = len(cost)
    dp = [0] * (n + 1)  # Need n+1 for the "top floor"

    # You can start from step 0 or step 1
    dp[0] = 0
    dp[1] = 0

    for i in range(2, n + 1):  # Loop to n+1
        # You can arrive from i-1 or i-2
        dp[i] = min(dp[i-1] + cost[i-1], dp[i-2] + cost[i-2])

    return dp[n]  # The top floor is at position n
```

#### Coin Change 的陣列大小 — LC 322 vs LC 518


**關鍵洞見**：只要題目有一個**目標值**（amount、sum 之類），DP 陣列大小就必須是 `target + 1`，才容納得下 `0` 到 `target` 這些值。

##### **為什麼是 `dp[amount + 1]`？**

- `dp[i]` 代表**金額 `i`** 的結果
- 要記錄 `0` 到 `amount` 的所有金額，就需要索引 `0, 1, 2, ..., amount`
- 總共是 `amount + 1` 個位置

##### **具體例子：`amount = 5`**

```text
We need to represent amounts: 0, 1, 2, 3, 4, 5
                         ↓    ↓   ↓   ↓   ↓   ↓
Array indices needed:    [0]  [1] [2] [3] [4] [5]

Therefore: dp array size = 6 = amount + 1
```

Java 程式碼：
```java
int amount = 5;
int[] dp = new int[amount + 1];  // size = 6, indices 0-5

// Now we can store results for each amount:
dp[0] = ...  // result for amount 0
dp[1] = ...  // result for amount 1
dp[2] = ...  // result for amount 2
dp[3] = ...  // result for amount 3
dp[4] = ...  // result for amount 4
dp[5] = ...  // result for amount 5
```

##### **LC 322 vs LC 518 比較**

| 面向 | LC 322: Coin Change | LC 518: Coin Change II |
|--------|---------------------|----------------------|
| **目標** | 求**最少硬幣數** | 求**組合數** |
| **回傳型別** | `int`（硬幣數或 -1） | `int`（組合數） |
| **DP 定義** | `dp[i]` = 湊出金額 `i` 的最少硬幣數 | `dp[i]` = 湊出金額 `i` 的組合總數 |
| **DP 陣列大小** | `amount + 1` | `amount + 1` |
| **base case** | `dp[0] = 0`（0 元用 0 枚） | `dp[0] = 1`（1 種：空集合） |
| **迴圈順序** | `coin` → `amount`（正倒序皆可） | `coin` → `amount`（只能正序） |
| **轉移** | `dp[i] = min(dp[i], dp[i - coin] + 1)` | `dp[i] += dp[i - coin]` |
| **例子** | `coins=[1,2,5], amount=5` → `2`（一枚 5） | `coins=[1,2,5], amount=5` → `4`（四種） |

##### **詳細程式碼範例：LC 518（Coin Change II）**

```java
public int change(int amount, int[] coins) {
    // dp[i] = total number of COMBINATIONS that make up amount i
    // Index corresponds to the amount value
    
    // Example: if amount = 5
    // We need: dp[0], dp[1], dp[2], dp[3], dp[4], dp[5]
    // Therefore: dp array size = 5 + 1 = 6
    
    int[] dp = new int[amount + 1];  // Size = amount + 1
    
    // Base case: There is exactly 1 way to make amount 0 (empty set)
    dp[0] = 1;
    
    // OUTER LOOP: Iterate through each coin
    // This ensures combinations (not permutations)
    for (int coin : coins) {
        // INNER LOOP: Update dp for all reachable amounts
        for (int i = coin; i <= amount; i++) {
            // Accumulate combinations:
            // Ways to make i = current ways + ways to make (i - coin)
            dp[i] += dp[i - coin];
        }
    }
    
    return dp[amount];  // Answer is at index = amount
}
```

**追蹤範例**：`amount = 5, coins = [1, 2, 5]`
```text
Initial:        dp = [1, 0, 0, 0, 0, 0]
After coin 1:   dp = [1, 1, 1, 1, 1, 1]  (all amounts reachable with 1s)
After coin 2:   dp = [1, 1, 2, 2, 3, 3]  (add combinations with 2s)
After coin 5:   dp = [1, 1, 2, 2, 3, 4]  (add combination with 5)

Result: dp[5] = 4 combinations: {5}, {2+2+1}, {2+1+1+1}, {1+1+1+1+1}
```

##### **LC 322 程式碼範例（拿來對照）**

```java
public int coinChange(int[] coins, int amount) {
    // dp[i] = minimum coins needed to make amount i
    // Same sizing: dp array size = amount + 1
    
    int[] dp = new int[amount + 1];
    
    // Initialize all to "infinity" except dp[0]
    Arrays.fill(dp, amount + 1);  // Use amount+1 as infinity
    dp[0] = 0;  // Base case: 0 coins needed for amount 0
    
    // OUTER LOOP: For each amount (can be any order)
    for (int i = 1; i <= amount; i++) {
        // INNER LOOP: Try each coin
        for (int coin : coins) {
            if (i >= coin) {
                dp[i] = Math.min(dp[i], dp[i - coin] + 1);
            }
        }
    }
    
    return dp[amount] == amount + 1 ? -1 : dp[amount];
}
```

---

#### **⭐⭐⭐⭐⭐ 迴圈順序：組合 vs 排列**

這是無限背包／換零錢類題目裡最常見的面試陷阱之一。

**規則：**

| 外層迴圈 | 內層迴圈 | 數到的是 | 例子 |
|------------|-----------|--------|---------|
| `for coin in coins` | `for i in range(coin, amount+1)` | **組合**（順序無關） | LC 518 |
| `for i in range(1, amount+1)` | `for coin in coins` | **排列**（順序有關） | LC 377 |

**為什麼硬幣在外層 → 組合：**

當 `coins` 在外層時，每一枚硬幣會被完整處理完，下一枚才登場。這代表 `[1, 2]` 跟 `[2, 1]` 不可能同時被數到——硬幣 1 那一輪在全域上「已經發生過」，所以 `[2, 1]` 沒有機會另外成為一條路徑。

**為什麼金額在外層 → 排列：**

當 `amount` 在外層時，對每個目標 `i` 我們問的是*「最後放進去的是哪一枚？」*，然後每種硬幣都試。所以「經由最後一枚是 2 抵達 i=3」（從用硬幣 1 建出來的 `dp[1]`）跟「最後一枚是 1」（從用硬幣 2 建出來的 `dp[2]`）是兩條不同的路徑。每一種順序都被數進去了。

**具體追蹤：`coins = [1, 2], amount = 3`**

```text
# Combinations (coins outer):
Initial:           dp = [1, 0, 0, 0]
After coin=1:      dp = [1, 1, 1, 1]   ← all amounts built from 1s only
After coin=2:      dp = [1, 1, 2, 2]   ← can now use 2s on top of 1s

→ dp[3] = 2 : {1,1,1}, {1,2}           ← {2,1} NOT separately counted ✓


# Permutations (amount outer):
Initial:           dp = [1, 0, 0, 0]
i=1: coin=1 → dp[1]+=dp[0]=1          dp = [1, 1, 0, 0]
i=2: coin=1 → dp[2]+=dp[1]=1
     coin=2 → dp[2]+=dp[0]=1          dp = [1, 1, 2, 0]
i=3: coin=1 → dp[3]+=dp[2]=2
     coin=2 → dp[3]+=dp[1]=1          dp = [1, 1, 2, 3]

→ dp[3] = 3 : {1,1,1}, {1,2}, {2,1}   ← {2,1} counted separately ✓
```

**程式碼並排：**

```python
# python — Combinations (LC 518): coins outer
def change(amount, coins):
    dp = [0] * (amount + 1)
    dp[0] = 1
    for coin in coins:                    # coin fixed first → combinations
        for i in range(coin, amount + 1):
            dp[i] += dp[i - coin]
    return dp[amount]

# python — Permutations (LC 377): amount outer
def combinationSum4(nums, target):
    dp = [0] * (target + 1)
    dp[0] = 1
    for i in range(1, target + 1):        # amount first → permutations
        for num in nums:
            if i >= num:
                dp[i] += dp[i - num]
    return dp[target]
```

**關鍵直覺——心智模型：**

```text
Coins outer: "I decide to use coin-1 first, then optionally add coin-2 on top."
             → Sequence is forced: coin-1 always before coin-2 → no duplicates.

Amount outer: "To reach amount i, which coin did I place LAST?"
             → Each last-coin choice is a distinct path → all orderings counted.
```

**那 LC 322（最少硬幣數）呢？**

LC 322 問的是*最少數量*，不是*有幾種湊法*——所以 `[1,2]` 跟 `[2,1]` 有沒有分開算根本無所謂；最小值兩種寫法都一樣。LC 322 兩種迴圈順序都對。

**LeetCode 題目對照：**

| LC # | 題目 | 迴圈順序 | 原因 |
|------|---------|-----------|------|
| **518** | Coin Change II | 硬幣在外 | 數組合 |
| **377** | Combination Sum IV | 金額在外 | 數排列 |
| **322** | Coin Change | 都可以 | 求最小值——順序無關 |
| **39** | Combination Sum | 回溯 | 硬幣任意的組合 |
| **40** | Combination Sum II | 回溯 | 組合，每個只能用一次 |

---

### 在 `n` 與 `n+1` 兩種寫法之間改寫


1. **老是被差一錯誤卡住？** 試試 `n+1` 的寫法
   - 它讓索引 `i` 直接代表第 i 個物品
   - 把 `dp[0]` 留成 base case 的「安全」佔位值
   - 題目大小跟陣列索引對得更整齊

2. **什麼時候用哪一種？**
   - 用 `n+1`：題目講「前 i 個物品」、「i 步」，或需要一個「空」的 base case
   - 用 `n`：元素直接對到索引比較說得通時

3. **兩種寫法之間怎麼改**：
   - `n` → `n+1`：陣列大小加 1，base case 位移，`nums[i]` 改成 `nums[i-1]`
   - `n+1` → `n`：拿掉佔位索引，base case 明寫出來，改用直接索引

## 背包深入探討

### 二維 → 一維的推導


```text
2-D (classic):
  dp[i][w] = max value using first i items with capacity w

  dp[i][w] = dp[i-1][w]                              # skip item i
  if w >= weight[i]:
      dp[i][w] = max(dp[i][w], dp[i-1][w-weight[i]] + value[i])   # take item i

1-D (space-optimized):
  Observe that dp[i][...] depends ONLY on dp[i-1][...].
  Collapse to one array, iterate w BACKWARD so dp[w-weight[i]]
  still holds dp[i-1][w-weight[i]] when we need it.

  dp[w] = max(dp[w], dp[w - weight[i]] + value[i])
  (backward w: W → weight[i])
```

---

### 何時使用 0/1 背包 DP？（中文速記）


> **完整中文版** —— 這一節是速記；從「物品重量價值」講到 LC 494 / 416 的逐步 trace、`dp[0] = 1` 的意義、
> 以及四步解題流程，見 [**knapsack_01_zh.md**](./knapsack_01_zh.md)。

**三個核心識別信號** —— 題目同時出現下面 3 點，幾乎可以確定是 0/1 背包：

| # | 特徵 | 說明 |
|---|------|------|
| **1** | 元素「**不可重複使用**」（0 或 1 的決策） | 每個物品／數字只有一個，只能 **選 (1)** 或 **不選 (0)**。對比：完全背包（Unbounded）的物品可以無限次重複選（例如無限供應的硬幣） |
| **2** | 存在明確的「**容量 / 目標限制**」 | 題目給了一個上限或目標值（背包最大容量 $W$，或子集目標和 Target），需要在不超過它的前提下做選擇與組合 |
| **3** | 求解目標是「**子集組合**」相關 | ① **最值**：容量內能裝入的最大價值？② **存在性**：能不能選出子集使和剛好等於目標？③ **方案數**：和等於目標的選法有幾種？ |

**實務上的快速判斷** —— 題目裡同時有：

1. 很多物品
2. 每個物品有一個 **cost / weight**
3. 每個物品有一個 **value / reward**
4. 每個物品 **最多只能選一次**
5. 有一個總容量 / 預算上限

→ 很可能就是 **0/1 Knapsack**。

> 例：「有 5 個物品，每個有重量和價值，背包最多裝重量 10，問最大價值是多少？」—— 典型 0/1 Knapsack。

**為什麼叫 0/1？** 每個 item 只有兩種選擇：`0` 不拿、`1` 拿，而且**不能拿第二次**。

#### 0/1 背包速記：DP 定義、方向與變形

##### 最常見的 DP 定義

`dp[j]` = 容量最多為 `j` 時，可以得到的最大 value。對每個 item `(weight, value)`：

```text
for each item:
    for j = W down to weight:          # ※ j 要倒著跑
        dp[j] = max(dp[j], dp[j - weight] + value)
```

**關鍵是 `j` 要倒著跑**。倒序的原因：你不希望同一個 item 在這一輪被重複使用——倒序時 `dp[j - weight]` 讀到的是 **上一輪（還沒用過這個 item）** 的值；正序時 `dp[j - weight]` 已經被本輪更新過，等於允許同一個 item 被選多次。詳細推導見 [dp.md](./dp.md) 的 **💡 Why Must the Inner Loop Go Backward?**（含 `nums = [3], target = 6` 的逐步 trace）。

##### 跟其他 Knapsack 的區別

| 類型 | 每個物品可以用幾次 | DP 容量方向 | 代表題 |
|------|------------------:|-------------|--------|
| **0/1 Knapsack** | 最多 1 次 | **由大到小（倒序）** | LC 416, 494, 1049 |
| **Unbounded Knapsack** | 無限次 | **由小到大（正序）** | LC 322, 518 |
| **Bounded Knapsack** | 最多 `k` 次 | 需要額外處理（二進制拆分成多個 0/1 物品，或單調隊列優化） | LC 2585, 1774 |

一個很好用的判斷：

> - **「每個東西只能選一次」→ 想 0/1 Knapsack（倒序）**
> - **「每個東西可以一直選」→ 想 Unbounded Knapsack（正序）**

##### 別被「背包」這個詞騙到

0/1 Knapsack 不一定真的長得像「背包」。像是 **預算分配、選課、專案選擇、投資組合、最多只能做一次的任務選擇**，只要本質是「**選或不選 + 有容量限制**」，都可能套這個 DP。LC 416（分割等和子集）、LC 494（添加 +/- 號）、LC 474（限 0 與 1 的個數）都是被包裝過的 0/1 背包。

---

### 二維 0/1 背包（經典寫法）


```python
def knapsack_01(weights, values, capacity):
    # time = O(n * W), space = O(n * W)
    n = len(weights)
    dp = [[0] * (capacity + 1) for _ in range(n + 1)]
    for i in range(1, n + 1):
        for w in range(capacity + 1):
            dp[i][w] = dp[i-1][w]                                    # skip
            if weights[i-1] <= w:
                dp[i][w] = max(dp[i][w],
                               dp[i-1][w - weights[i-1]] + values[i-1])  # take
    return dp[n][capacity]
```

### knapsack.md 涵蓋了什麼


[**knapsack.md**](./knapsack.md) 完整涵蓋：

- **0/1 背包與子集和深入** —— 分割問題的化簡、LC 494 的 `(total + target) / 2`
  變換、布林／計數／最大價值三種變形，以及常見陷阱。
- **組合 vs 排列** —— 四種核心迴圈順序的圖解總結、各模式的程式碼模板，以及選模式的決策樹。
- **組合 vs 排列 vs 0/1 背包** —— 程式碼並排、同一份輸入在兩種迴圈順序下的逐步追蹤，以及一張「該用哪個」對照表。
- **Coin Change 裡的 `if (i - coin >= 0)`** —— 為什麼守衛是 `>=` 而不是 `==`。

[**knapsack_01_zh.md**](./knapsack_01_zh.md) — 中文版，只聚焦 0/1 背包：state 定義、「拿或不拿」的轉移、
倒序 vs 正序的逐步 trace、max / min / count / boolean 四種變形，以及 LC 494 / 416 / 1049 / 474 的解法。

---

## 格子 DP —— 另外三種實作

### LC 64 的各種變形


```java
public int minPathSum(int[][] grid) {
    int m = grid.length, n = grid[0].length;

    // First column prefix sum
    for (int i = 1; i < m; i++)
        grid[i][0] += grid[i - 1][0];

    // First row prefix sum
    for (int j = 1; j < n; j++)
        grid[0][j] += grid[0][j - 1];

    // Fill rest in-place
    for (int i = 1; i < m; i++)
        for (int j = 1; j < n; j++)
            grid[i][j] += Math.min(grid[i - 1][j], grid[i][j - 1]);

    return grid[m - 1][n - 1];
}
```

**取捨**：會改動輸入的 grid。當空間很吃緊、而且允許改動輸入時才用。

#### **做法 3：壓成一維的空間最佳化版**（額外 O(m) 空間）

```java
public int minPathSum(int[][] grid) {
    int m = grid.length, n = grid[0].length;

    // cur[i] = min cost to reach current column at row i
    int[] cur = new int[m];
    cur[0] = grid[0][0];

    // Initialize first column
    for (int i = 1; i < m; i++)
        cur[i] = cur[i - 1] + grid[i][0];

    // Process column by column
    for (int j = 1; j < n; j++) {
        cur[0] += grid[0][j];  // First row: only from left
        for (int i = 1; i < m; i++)
            cur[i] = Math.min(cur[i - 1], cur[i]) + grid[i][j];
            //                ↑ from above    ↑ current row, previous column
    }

    return cur[m - 1];
}
```

**關鍵洞見**：`cur[i]` 在更新前 = 走到 `(i, j-1)` 的成本（從左邊來）。`cur[i-1]` 更新後 = 走到 `(i-1, j)` 的成本（從上面來）。所以 `min(cur[i-1], cur[i])` 剛好就是 `min(上, 左)`。

#### **做法 4：由上而下的記憶化**（遞迴）

```java
public int minPathSum(int[][] grid) {
    int m = grid.length - 1;
    int n = grid[0].length - 1;
    int[][] dp = new int[m + 1][n + 1];
    for (int[] row : dp)
        Arrays.fill(row, -1);
    return helper(grid, m, n, dp);
}

// helper(m, n) = min path sum from (0,0) to (m,n)
private int helper(int[][] grid, int m, int n, int[][] dp) {
    if (m == 0 && n == 0) return grid[0][0];
    if (m == 0) {
        dp[m][n] = grid[m][n] + helper(grid, m, n - 1, dp);
        return dp[m][n];
    }
    if (n == 0) {
        dp[m][n] = grid[m][n] + helper(grid, m - 1, n, dp);
        return dp[m][n];
    }
    if (dp[m][n] != -1) return dp[m][n];
    // DP equation: min(come from left, come from above) + current cell
    dp[m][n] = grid[m][n] + Math.min(helper(grid, m, n - 1, dp), helper(grid, m - 1, n, dp));
    return dp[m][n];
}
```

**關鍵洞見**：從 `(m-1, n-1)` 往 `(0, 0)` 遞迴。base case 處理第一列／第一行（只有一個方向可走）。用 `dp[m][n] != -1` 當守衛做快取。

#### **做法比較** — LC 64

| 做法 | 空間 | 會改動輸入 | 備註 |
|----------|-------|----------------|-------|
| 由上而下記憶化 | O(m×n) | 否 | 最貼近遞迴的思考方式 |
| 二維 DP | O(m×n) | 否 | 迭代寫法裡最清楚的；最好推理 |
| 原地 DP | O(1) | 是 ⚠️ | 空間最省，但有破壞性 |
| 一維 DP（一列） | O(m) | 否 | 空間與清晰度平衡得不錯 |

### 相似的格子題


| 題目 | LC # | 關鍵差異 | 演算法 |
|---------|------|----------------|-----------|
| **Minimum Path Sum** | 64 | 沿路徑求和，只能往右／往下 | 二維 DP |
| **Unique Paths** | 62 | 數路徑數（不是求最小和） | 二維 DP |
| **Unique Paths II** | 63 | 有障礙物 | 二維 DP（跳過障礙物） |
| **Dungeon Game** | 174 | 格子形狀相同，但要從右下往左上解（要求最低 HP） | 二維 DP（反方向） |
| **Triangle** | 120 | 三角形形狀，由上往下 | 一維 DP（由下往上） |
| **Minimum Falling Path Sum** | 931 | 可以斜走 ±1 | 二維 DP |
| **Maximal Square** | 221 | 找最大的全 1 正方形 | 二維 DP（取三個鄰居的 `min`） |
| **Path With Min Effort** | 1631 | 四方向，成本是最大差值 | Dijkstra |
| **Shortest Path in Grid with Obstacles** | 1293 | BFS，可消除 k 個障礙物 | BFS + 狀態 |

### 圖解追蹤與 Python 對照版 — LC 64


```text
grid = [[1,3,1],
        [1,5,1],
        [4,2,1]]

After DP:
dp = [[1, 4, 5],
      [2, 7, 6],
      [6, 8, 7]]

Path: 1→3→1→1→1 = 7
```

```python
# Python equivalent
def grid_dp(grid):
    if not grid or not grid[0]:
        return 0
    m, n = len(grid), len(grid[0])
    dp = [[0] * n for _ in range(m)]
    dp[0][0] = grid[0][0]
    for i in range(1, m):
        dp[i][0] = dp[i-1][0] + grid[i][0]
    for j in range(1, n):
        dp[0][j] = dp[0][j-1] + grid[0][j]
    for i in range(1, m):
        for j in range(1, n):
            dp[i][j] = min(dp[i-1][j], dp[i][j-1]) + grid[i][j]
    return dp[m-1][n-1]
```

**檔案參考**：`leetcode_java/src/main/java/LeetCodeJava/DynamicProgramming/MinimumPathSum.java`

### Python 滾動列模板

**二維 DP 的空間最佳化模板**：
```python
def grid_dp_optimized(grid):
    """Space-optimized 2D DP"""
    if not grid or not grid[0]:
        return 0

    m, n = len(grid), len(grid[0])
    # Only need previous row
    prev = [0] * n
    prev[0] = grid[0][0]

    # Initialize first row
    for j in range(1, n):
        prev[j] = prev[j-1] + grid[0][j]

    # Process remaining rows
    for i in range(1, m):
        curr = [0] * n
        curr[0] = prev[0] + grid[i][0]

        for j in range(1, n):
            curr[j] = min(prev[j], curr[j-1]) + grid[i][j]

        prev = curr

    return prev[n-1]
```

## 狀態機 DP —— 延伸

### 依交易限制分出的子模式

**子模式：**
1. **雙狀態機**（買賣，無冷卻期）
   - 狀態：`hold`、`cash`
   - 範例：LC 122（交易次數不限）

2. **三狀態機**（買賣，有冷卻期）
   - 狀態：`hold`、`sold`、`rest`
   - 範例：LC 309（賣出後要冷卻）
   - 關鍵：`rest` 狀態擋住「賣完立刻買」

3. **多狀態機**（交易次數有限）
   - 狀態：`buy1`、`sell1`、`buy2`、`sell2`、……
   - 範例：LC 123（最多 2 次交易）、LC 188（最多 k 次交易）

### LC 309 的狀態轉移圖


**LC 309 的狀態轉移圖：**
```text
    ┌─────────────────────────────────────────┐
    │         State Machine Flow              │
    └─────────────────────────────────────────┘

         buy            sell          cooldown
    REST ────→ HOLD ────→ SOLD ─────→ REST
     ↑                                   │
     └───────────────────────────────────┘

    Transitions:
    • REST → HOLD: Buy stock (rest - price)
    • HOLD → SOLD: Sell stock (hold + price)
    • SOLD → REST: Cooldown (no transaction)
    • REST → REST: Do nothing (rest)
    • HOLD → HOLD: Keep holding (hold)
```

**關鍵洞見：**
- **三狀態 vs 兩狀態**：跟單純的股票題（買／賣）不同，冷卻期逼得你需要第三個狀態
- **冷卻期的落實**：`rest` 狀態確保你不能賣完馬上買
- **空間最佳化**：用 3 個變數就能做到 O(1) 空間，不必開二維陣列
- **關鍵轉移**：`hold = max(hold, rest - prices[i])` —— 只能從 rest 買進，不能從 sold 買進

## 字串 DP 深入探討

### 編輯距離 —— 模式辨識與三種操作

#### 🎯 **模式辨識** — LC 72

**什麼時候用編輯距離 DP：**
- ✅ 用插入／刪除／取代把一個字串變成另一個
- ✅ 求兩個字串之間的最小「編輯距離」或「操作次數」
- ✅ 字串變換類的題目（尤其是 LeetCode 的 medium/hard 字串題）
- ✅ 兩字串比較、且各種操作各有成本的題目

**題目**：LC 72 - Edit Distance（又稱 Levenshtein 距離）

給兩個字串 `word1` 與 `word2`，求把 `word1` 變成 `word2` 所需的**最少操作次數**。

允許的操作（每次算 1 步）：
1. 插入一個字元
2. 刪除一個字元
3. 取代一個字元

#### 💡 **核心 DP 想法**

關鍵洞見是：**字元不相符時，選成本最小的那個操作。**

```text
At position (i, j):
  - If chars match: No cost, take solution from (i-1, j-1)
  - If they don't:
      Delete from word1:   dp[i-1][j] + 1
      Insert into word1:   dp[i][j-1] + 1
      Replace in word1:    dp[i-1][j-1] + 1
      → Take the minimum of these three
```

### 編輯距離的實作變形 — LC 72


**變形 1：由上而下記憶化（遞迴 + 快取）**
```java
private int[][] memo;

public int minDistance(String word1, String word2) {
    int m = word1.length();
    int n = word2.length();
    memo = new int[m][n];
    for (int i = 0; i < m; i++) {
        for (int j = 0; j < n; j++) {
            memo[i][j] = -1;
        }
    }
    return dfs(0, 0, word1, word2, m, n);
}

private int dfs(int i, int j, String word1, String word2, int m, int n) {
    // Base cases
    if (i == m) return n - j;
    if (j == n) return m - i;
    
    // Check memo
    if (memo[i][j] != -1) return memo[i][j];

    int res;
    if (word1.charAt(i) == word2.charAt(j)) {
        res = dfs(i + 1, j + 1, word1, word2, m, n);
    } else {
        res = 1 + Math.min(
            dfs(i + 1, j, word1, word2, m, n),      // Delete
            Math.min(
                dfs(i, j + 1, word1, word2, m, n),  // Insert
                dfs(i + 1, j + 1, word1, word2, m, n) // Replace
            )
        );
    }

    memo[i][j] = res;
    return res;
}
```

**變形 2：空間最佳化（O(n) 空間）**
```java
public int minDistance(String word1, String word2) {
    int m = word1.length();
    int n = word2.length();
    
    // Use 1D array instead of 2D (only need previous row)
    int[] prev = new int[n + 1];
    for (int j = 0; j <= n; j++) {
        prev[j] = j;
    }

    for (int i = 1; i <= m; i++) {
        int[] curr = new int[n + 1];
        curr[0] = i;
        
        for (int j = 1; j <= n; j++) {
            if (word1.charAt(i - 1) == word2.charAt(j - 1)) {
                curr[j] = prev[j - 1];
            } else {
                curr[j] = 1 + Math.min(
                    prev[j],        // Delete
                    Math.min(
                        curr[j - 1],    // Insert
                        prev[j - 1]     // Replace
                    )
                );
            }
        }
        
        prev = curr;
    }

    return prev[n];
}
```

### 編輯距離的視覺化表格


```text
Input: word1 = "horse", word2 = "ros"

       ""  r   o   s
    "" 0   1   2   3
    h  1   1   2   3
    o  2   2   1   2
    r  3   2   2   2
    s  4   3   3   2
    e  5   4   4   3

Result: dp[5][3] = 3 operations
Explanation: 
  - Replace 'h' → 'r': "rorse"
  - Delete 'r': "rose"
  - Delete 'e': "ros"
```

#### 關鍵洞見 —— 那三個鄰居


1. **三種操作的圖解**：
   ```text
   dp[i-1][j]      dp[i-1][j-1]
       ↓            ↘
   dp[i][j-1] →   dp[i][j]
   
   Delete (↓):    dp[i-1][j] + 1
   Replace (↘):   dp[i-1][j-1] + 1
   Insert (→):    dp[i][j-1] + 1
   ```

2. **索引風格**：
   - **1-based 索引**（比較乾淨）：`i` 從 1 跑到 m，存進 `dp[i][j]`
   - **0-based 索引**（也可以）：`i` 從 0 跑到 m-1，存進 `dp[i+1][j+1]`

3. **複雜度**：
   - **時間**：O(m × n)
   - **空間**：標準是 O(m × n)，空間最佳化後是 O(min(m,n))

4. **為什麼要看三個鄰居**：
   - 字元**不相符** → 挑最便宜的操作
   - 字元**相符** → 不花成本，直接繼承左上角
   - 每一步的這個貪婪選擇，會導向全域最佳解

#### **模式辨識清單** ✅ — LC 72

看到這些就用這個模式：
- 「最少操作次數」+ 兩個字串 → 編輯距離
- 「插入、刪除、取代」操作 → 大概率是編輯距離
- 「把字串 A 變成字串 B」 → 編輯距離
- 「Levenshtein 距離」或「edit distance」 → 一定是這個模板
- 兩字串比較、且各操作成本相同（都是 1）

#### **常見錯誤** ⚠️

1. **索引錯誤**：忘記 `dp[i][j]` 對應的是 `word1[i-1]` 與 `word2[j-1]`
   - ❌ `if (word1.charAt(i) == word2.charAt(j))`
   - ✅ `if (word1.charAt(i-1) == word2.charAt(j-1))`

2. **base case 錯誤**：沒有初始化第一列與第一行
   - 必須設 `dp[i][0] = i` 與 `dp[0][j] = j`

3. **漏掉操作的 +1**：字元不相符時忘了加 1
   - ❌ `dp[i][j] = Math.min(...)`
   - ✅ `dp[i][j] = 1 + Math.min(...)`

4. **狀態定義錯誤**：搞混哪個字串對應哪個維度
   - 要一致：列 = word1，行 = word2

---

#### **相關的字串 DP 題目** 📚

| LC # | 題目 | 變形／差異 | 難度 | 關鍵洞見 |
|------|---------|-------------------|------------|-------------|
| **72** | **Edit Distance** | 經典（插入、刪除、取代） | Medium | 三種操作取最小 |
| **583** | Delete Operation for Two Strings | 只准刪除 | Medium | `dp[i][j] = dp[i-1][j] + 1` 或 `dp[i][j-1] + 1` |
| **712** | Minimum ASCII Delete Sum | 刪除的成本是 ASCII 值 | Medium | 追成本而不是追次數 |
| **1143** | Longest Common Subsequence (LCS) | 最大化相符數（跟編輯相反） | Medium | 相符：+1；不相符：max(左, 上) |
| **1312** | Minimum Insertion Steps | 把字串變成回文 | Hard | 跟 LCS 很像 |
| **87** | Scramble String | 判斷一個字串是不是另一個的打亂版 | Hard | 帶切分的二維 DP |
| **115** | Distinct Subsequences | 數符合樣式的子序列個數 | Hard | 計數型變形 |
| **44** | Wildcard Matching | 帶 `?` 與 `*` 的樣式比對 | Hard | 延伸的字串 DP |
| **10** | Regular Expression Matching | DP 做樣式比對 | Hard | 要處理正規表示式的特殊字元 |

#### **檔案參考**：
- **Java 實作**：`leetcode_java/src/main/java/LeetCodeJava/DynamicProgramming/EditDistance.java`
  - 多種解法（由下而上、由上而下、空間最佳化）
  - 註解完整，DP 轉移解釋得很細
- **相關**：想看「比較並最大化」的變形，見 [dp.md](./dp.md) 裡的 LCS 模板

### One Edit Distance — LC 161，「剛好一次編輯」的變形


#### 🎯 **模式辨識** — LC 161

**這跟 Edit Distance（LC 72）不是同一回事。**

| | LC 72 Edit Distance | LC 161 One Edit Distance |
|---|---|---|
| **目標** | 求**最少**編輯次數 | 判斷編輯次數是不是**剛好 1** |
| **操作** | 插入、刪除、取代 | 同樣三種 |
| **輸出** | 整數（最少操作數） | 布林 |
| **做法** | 完整二維 DP | 雙指標，或二維 DP（檢查 `== 1`） |
| **時間** | O(m × n) | 雙指標 O(n)，DP O(m×n) |

#### 💡 **核心想法 —— 雙指標（O(n)，首選）**

不必填整張 DP 表，改成從左到右掃，在**第一個不相符**的位置套用唯一可行的修補，然後立刻驗證後綴：

```text
Three cases at first mismatch position i:
  len(s) == len(t) → Replace s[i]:  check s[i+1..] == t[i+1..]
  len(s) <  len(t) → Insert into s: check s[i..]   == t[i+1..]
  len(s) >  len(t) → Delete from s: check s[i+1..] == t[i..]
```

掃完都沒有不相符時：只有 `len(t) == len(s) + 1` 才算數。

**Java（雙指標）：**
```java
// LC 161 - One Edit Distance  O(n) time, O(1) space
public boolean isOneEditDistance(String s, String t) {
    int ns = s.length(), nt = t.length();
    if (ns > nt) return isOneEditDistance(t, s); // ensure s is shorter
    if (nt - ns > 1) return false;               // gap > 1 → impossible

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
    // No mismatch in s — valid only if t has one trailing extra char
    return ns + 1 == nt;
}
```

#### 💡 **核心想法 —— DP（O(m×n)，跟 LC 72 同一張表）**

跑完整的編輯距離 DP，然後回傳 `dp[m][n] == 1`：

```java
// LC 161 - DP approach
public boolean isOneEditDistance(String s, String t) {
    int ns = s.length(), nt = t.length();
    if (Math.abs(ns - nt) > 1) return false;

    int[][] dp = new int[ns + 1][nt + 1];
    for (int i = 0; i <= ns; i++) dp[i][0] = i;
    for (int j = 0; j <= nt; j++) dp[0][j] = j;

    for (int i = 1; i <= ns; i++) {
        for (int j = 1; j <= nt; j++) {
            if (s.charAt(i - 1) == t.charAt(j - 1)) {
                dp[i][j] = dp[i - 1][j - 1];
            } else {
                dp[i][j] = 1 + Math.min(dp[i - 1][j - 1],
                               Math.min(dp[i - 1][j], dp[i][j - 1]));
            }
        }
    }
    return dp[ns][nt] == 1;
}
```

**為什麼一開始的 `Math.abs > 1` 守衛很重要**：長度差超過 1 的兩個字串，編輯距離一定 ≥ 2，所以整段 O(m×n) 的工可以直接省掉。

#### 📐 **為什麼是這三條 DP 轉移？（用例子建立直覺）**

```text
| Operation | DP Cell Used  | Meaning                                      |
|-----------|---------------|----------------------------------------------|
| Insert    | dp[i][j-1]    | Already matched s[0..i] to t[0..j-1], then insert t[j] |
| Delete    | dp[i-1][j]    | Already matched s[0..i-1] to t[0..j], then delete s[i] |
| Replace   | dp[i-1][j-1]  | Already matched s[0..i-1] to t[0..j-1], then swap s[i]→t[j] |
```

**具體走一遍：s = "ab"、t = "acb"**

建一張表，`dp[i][j]` = 把 `s[0..i-1]` 變成 `t[0..j-1]` 的最少編輯數：

```text
       ""   a    c    b
  ""  [ 0][ 1][ 2][ 3]
  a   [ 1][ 0][ 1][ 2]
  b   [ 2][ 1][ 1][ 1]
```

聚焦在 `dp[2][3]`（把 "ab" 變成 "acb"）：

```text
s[1] = 'b',  t[2] = 'b'   → chars MATCH → dp[2][3] = dp[1][2] = 1  ✓
```

再聚焦在 `dp[1][2]`（把 "a" 變成 "ac"），此時 s[0]='a'、t[1]='c'（**不**相符）：

```text
Option 1 — INSERT 'c' into s after "a":
    We already know it takes dp[1][1] = 0 ops to match "a"→"a",
    then we insert 'c' → dp[1][1] + 1 = 1
    → uses dp[i][j-1]  (same row, one column back = t is one char shorter)

Option 2 — DELETE s[0]='a' from s:
    We already know it takes dp[0][2] = 2 ops to match ""→"ac",
    then we delete 'a' → dp[0][2] + 1 = 3
    → uses dp[i-1][j]  (one row up = s is one char shorter)

Option 3 — REPLACE s[0]='a' with t[1]='c':
    We already know it takes dp[0][1] = 1 op to match ""→"a",
    then swap 'a'→'c' → dp[0][1] + 1 = 2
    → uses dp[i-1][j-1]  (diagonal = both strings one char shorter)

→ dp[1][2] = min(1, 3, 2) = 1
```

**三個格子的心智模型：**

```text
dp[i-1][j-1]  dp[i-1][j]
     ↘              ↓
dp[i][j-1]  →   dp[i][j]

  ↘ Replace      ↓ Delete (from s)
  → Insert (into s, advance t only)
```

- **`dp[i][j-1]`（左）**：t 往前走一步、s 沒有 → 用一次**插入**補上這個缺口
- **`dp[i-1][j]`（上）**：s 往前走一步、t 沒有 → 從 s 拿掉一個字元（**刪除**）
- **`dp[i-1][j-1]`（斜對角）**：兩邊都往前走 → 把 s[i] **替換**成 t[j]

回到 LC 161：表格填完後，`dp[ns][nt] == 1` 就代表剛好只需要這三種操作中的一次。

#### **該用哪一種**

| 解法 | 什麼時候選它 |
|----------|----------------|
| 雙指標（`substring.equals`） | 面試場合，O(n) 時間，好講清楚 |
| 完整 DP | 已經寫過 LC 72，想直接沿用程式碼 |

#### **相似 LC 題目**

| 題目 | LC# | 關聯 |
|---------|-----|----------|
| Edit Distance | 72 | 一般化版本（最小化操作數） |
| One Edit Distance | 161 | 剛好 1 次操作 —— 就是這個模式 |
| Valid Palindrome II | 680 | 最多刪 1 個字元湊出回文 |
| Longest Common Subsequence | 1143 | 改成最大化相符數，而非最小化操作數 |
| Delete Operation for Two Strings | 583 | 只能刪除的編輯距離 |

---

### dp_string.md 涵蓋什麼

[**dp_string.md**](./dp_string.md) 完整涵蓋以下內容：

- **雙字串／雙序列的網格模式** —— 橫跨 LC 1143 / 72 / 115 /
  583 / 712 / 10 / 44 的完整對照表。
- **深入：前綴式（1-indexed）索引** —— 為什麼表格是 `dp[m+1][n+1]`，以及 0-indexed
  版本會帶來哪些差一位的錯誤。
- **Interleaving String（LC 97）** —— base case、一維空間優化，以及相鄰題型。
- **Valid Parenthesis String（LC 678）** —— 通配字元的狀態問題，用 DP、貪婪區間、
  雙堆疊三種解法並排比較。

---

## 回文 DP 變化題

### 以長度當外層迴圈 —— LC 647


```python
# IDEA: build dp table by increasing substring length
# dp[i][j] = True if s[i:j+1] is palindrome
def countSubstrings_dp_length(s):
    # time = O(n^2), space = O(n^2)
    n = len(s)
    dp = [[False] * n for _ in range(n)]
    count = 0

    for length in range(1, n + 1):          # outer: substring length
        for i in range(n - length + 1):     # inner: start index
            j = i + length - 1              # end index

            if length == 1:
                dp[i][j] = True
            elif length == 2:
                dp[i][j] = (s[i] == s[j])
            else:
                dp[i][j] = (s[i] == s[j] and dp[i+1][j-1])

            if dp[i][j]:
                count += 1

    return count
```

### Manacher 演算法 —— O(n)


```python
# IDEA: reuse previously computed palindrome radii to skip redundant checks
def countSubstrings_manacher(s):
    # time = O(n), space = O(n)
    def manacher(s):
        # transform: "abc" → "^#a#b#c#$"
        t = '^#' + '#'.join(s) + '#$'
        P = [0] * len(t)
        C = R = 0
        for i in range(1, len(t) - 1):
            mirror = 2 * C - i
            if R > i:
                P[i] = min(R - i, P[mirror])
            while t[i + 1 + P[i]] == t[i - 1 - P[i]]:
                P[i] += 1
            if i + P[i] > R:
                C, R = i, i + P[i]
        return P

    return sum((r + 1) // 2 for r in manacher(s))
```

---

### 相似的回文題目


| LC # | 題目 | 解法 | 關鍵差異 |
|------|---------|----------|----------------|
| **647** | Palindromic Substrings | DP 或中心擴展 | 計算所有回文的數量 |
| **5** | Longest Palindromic Substring | 中心擴展或 DP | 追蹤最大長度而非數量 |
| **516** | Longest Palindromic Subsequence | 二維 DP（i 倒著跑） | 子序列（不是子字串），用 `dp[i+1][j-1]+2` |
| **132** | Palindrome Partitioning II | DP + 回文表 | 最少切幾刀；先預算 `isPalin[i][j]` |
| **131** | Palindrome Partitioning | 回溯 + 回文表 | 列出所有切法 |
| **1312** | Min Insertions to Make Palindrome | 二維 DP | `n - LPS(s)` |
| **680** | Valid Palindrome II | 雙指標 | 最多刪 1 個字元 |

---

## 滾動變數深入解析

### 巧克力棒／Tribonacci 完整走一遍


**模式**：任何只回看**固定**步數（`dp[i-1] … dp[i-k]`）的遞迴式，只需要 `k` 個變數，不需要陣列。難度整個從遞迴式移到了*更新區塊*：算完 `dp[i]` 之後，這 `k` 個變數必須整體往前挪一格，下一輪才會看到正確的視窗。

**引子題** —— *巧克力棒／三角巧克力*：一條棒子是排成一列的 `n` 塊，每次咬掉 1、2 或 3 塊。把整條吃完共有幾種不同吃法？

最後一口是 1、2 或 3 塊，而且這三種情況互斥，所以：

```text
dp[i] = dp[i-1] + dp[i-2] + dp[i-3]     # Tribonacci (same shape as LC 1137)

dp[0] = 1   # one way to eat nothing (the empty sequence of bites)
dp[1] = 1   # (1)
dp[2] = 2   # (1,1) (2)
dp[3] = 4   # (1,1,1) (1,2) (2,1) (3)
```

全程只會讀到前三個值 → 三個變數就夠。

#### 更新區塊

```python
# python
# IDEA: keep a sliding window of the last 3 dp values instead of a dp array
# time = O(n), space = O(1)
def get_ways(n):
    # 1. Base / edge cases
    if n == 0:
        return 0          # "no way to eat a bar that doesn't exist"
    if n <= 2:
        return n          # dp[1] = 1, dp[2] = 2
    if n == 3:
        return 4

    # 2. Seed: p1, p2, p3 = dp[i-3], dp[i-2], dp[i-1] for the first i we compute (i = 4)
    p1, p2, p3 = 1, 2, 4  # dp[1], dp[2], dp[3]

    # 3. Transition + update
    for i in range(4, n + 1):
        dp = p1 + p2 + p3   # dp[i] = dp[i-3] + dp[i-2] + dp[i-1]
        # update: slide the window right by one
        p1 = p2             # new dp[i-3] is the old dp[i-2]
        p2 = p3             # new dp[i-2] is the old dp[i-1]
        p3 = dp             # new dp[i-1] is the dp[i] we just computed

    return dp
```

**核心想法**：`p1, p2, p3` 不是「剛好需要的三個數字」，而是*相對於 `i` 的具名位置*。進迴圈前先把不變式寫下來：

```text
at the top of iteration i:   p1 = dp[i-3],  p2 = dp[i-2],  p3 = dp[i-1]
```

更新區塊的每一行，都是為了讓這個不變式在 `i+1` 時仍然成立。

#### 視覺化追蹤（`n = 6`）

```text
i        p1      p2      p3      dp = p1+p2+p3
----------------------------------------------
(seed)  dp1=1   dp2=2   dp3=4        -
4         1       2       4        7      <- dp[4] = 1+2+4
  update: p1<-2, p2<-4, p3<-7
5         2       4       7       13      <- dp[5] = 2+4+7
  update: p1<-4, p2<-7, p3<-13
6         4       7      13       24      <- dp[6] = 4+7+13

answer = 24

window slides right one slot per iteration:
dp:  [1] [2] [4]  7   13   24
      ^p1 ^p2 ^p3            i=4
          ^p1 ^p2 ^p3        i=5
              ^p1 ^p2 ^p3    i=6
```

#### 更新順序：經典的坑 ⭐⭐⭐⭐⭐

賦值順序要**從最舊到最新**（`p1` 先、`p3` 最後）。反過來寫會把還要用的值蓋掉：

```python
# python
# ❌ WRONG — p3 is clobbered before p2 reads it
p3 = dp     # p3 destroyed
p2 = p3     # p2 gets dp[i], not dp[i-1]
p1 = p2     # p1 gets dp[i] too — all three collapse to the same value

# ✅ RIGHT — each read happens before its target is overwritten
p1 = p2
p2 = p3
p3 = dp

# ✅ ALSO RIGHT — tuple assignment evaluates the whole RHS first, so order is irrelevant
p1, p2, p3 = p2, p3, dp
```

Java 沒有 tuple 賦值，所以由舊到新是唯一可行的順序：

```java
// java
// IDEA: same 3-variable rolling window; shift oldest -> newest
// time = O(n), space = O(1)
public int getWays(int n) {
    if (n == 0) return 0;
    if (n <= 2) return n;
    if (n == 3) return 4;

    int p1 = 1, p2 = 2, p3 = 4;   // dp[1], dp[2], dp[3]
    int dp = p3;
    for (int i = 4; i <= n; i++) {
        dp = p1 + p2 + p3;
        p1 = p2;                  // must go oldest -> newest
        p2 = p3;
        p3 = dp;
    }
    return dp;
}
```

### 種子值與回看 `k` 步

> **加碼 —— 免種子的寫法。** 從 `p1 = p2 = 0` 開始，對*每一個*元素都跑一輪，就能完全拿掉
> `n == 1` / `n == 2` 的特例，因為對這條遞迴式而言 `dp[-1] = dp[-2] = 0` 是誠實的種子值：
>
> ```python
> # python
> # LC 198 - House Robber (no edge cases)
> # time = O(n), space = O(1)
> def rob(nums):
>     p1, p2 = 0, 0                   # dp[i-2], dp[i-1]
>     for num in nums:
>         p1, p2 = p2, max(p2, p1 + num)
>     return p2
> ```
>
> 之所以成立，是因為 `max(0, 0 + nums[0]) = nums[0]` 正好重現了長版本手寫的那個種子值。
> 對照上面 Tribonacci 的種子陷阱：那裡 `dp[0] = 1` —— 零*不是*每次都對，
> 所以每次都要拿遞迴式驗一下。

#### 種子值：這些值必須滿足遞迴式

`dp[0]` 是最常見的坑。空棒子的*答案*大概是 `0`，但*遞迴式*需要 `dp[0] = 1`，因為 `dp[3] = dp[2] + dp[1] + dp[0] = 2 + 1 + 1 = 4`。`0` 只能拿來當對外的提前返回值，絕不能當種子：

```python
# python
if n == 0:
    return 0        # answer for the caller
p0 = 1              # seed for the recurrence — a different number on purpose
```

經驗法則：種子設好之後，用手算第**一**輪迴圈，再跟暴力枚舉的結果對一下。如果 `dp[4]` 算不出 `7`，錯的是種子，不是迴圈。

#### 推廣到回看 `k` 步

當 `dp[i] = sum(dp[i-1] … dp[i-k])`，具名變數就撐不住了。兩種乾淨的做法：

```python
# python
# IDEA: k-step rolling window with a deque — O(k) space instead of O(n)
#       returns the recurrence value, so ways_k(0, k) == dp[0] == 1
# time = O(n), space = O(k)
from collections import deque

def ways_k(n, k):
    window = deque([1])            # dp[0] = 1
    total = 1                      # running sum of the window
    for i in range(1, n + 1):
        cur = total
        window.append(cur)
        total += cur
        if len(window) > k:        # drop dp[i-k], it's out of range now
            total -= window.popleft()
    return window[-1]
```

```python
# python
# IDEA: circular buffer — dp[i] lives at index i % k, no shifting at all
# time = O(n * k), space = O(k)
def ways_k_mod(n, k):
    dp = [0] * k
    dp[0] = 1                      # dp[0] = 1
    for i in range(1, n + 1):
        dp[i % k] = sum(dp[(i - j) % k] for j in range(1, min(k, i) + 1))
    return dp[n % k]
```

`i % k` 這招把更新區塊整個消掉了 —— 什麼都不用挪，`dp[i]` 直接重用 `dp[i-k]` 的槽位，而那個值正好是剛滑出視窗的那一個。二維 DP 的一維滾動陣列優化（`dp[i % 2][j]`）用的是同一個道理。

### 同樣的更新模式，其他題目


| 題目 | 遞迴式 | 變數數 |
|---------|------------|------|
| LC 70 Climbing Stairs | `dp[i] = dp[i-1] + dp[i-2]` | 2 |
| LC 1137 N-th Tribonacci | `dp[i] = dp[i-1] + dp[i-2] + dp[i-3]` | 3 |
| 巧克力棒（一次咬 1/2/3 塊） | `dp[i] = dp[i-1] + dp[i-2] + dp[i-3]` | 3 |
| LC 198 House Robber | `dp[i] = max(dp[i-1], dp[i-2] + nums[i])` | 2 |
| LC 746 Min Cost Climbing Stairs | `dp[i] = min(dp[i-1], dp[i-2]) + cost[i]` | 2 |
| LC 91 Decode Ways | `dp[i] = dp[i-1]·ok1 + dp[i-2]·ok2` | 2 |
| 每口可咬 `k` 種大小 | `dp[i] = sum(dp[i-c] for c in sizes)` | `max(sizes)` |

## 位元遮罩 DP —— TSP 模板

### 走訪所有節點的最短路徑 —— LC 847

```python
def state_compression_dp(graph):
    """Traveling Salesman Problem using bitmask DP"""
    n = len(graph)
    # dp[mask][i] = min cost to visit all cities in mask, ending at city i
    dp = [[float('inf')] * n for _ in range(1 << n)]
    dp[1][0] = 0  # Start at city 0

    for mask in range(1 << n):
        for u in range(n):
            if not (mask & (1 << u)):
                continue

            for v in range(n):
                if mask & (1 << v):
                    continue

                new_mask = mask | (1 << v)
                dp[new_mask][v] = min(dp[new_mask][v],
                                    dp[mask][u] + graph[u][v])

    # Return to starting city
    return min(dp[(1 << n) - 1][i] + graph[i][0] for i in range(1, n))
```

## 串鏈 DP —— DFS + 記憶化

### Longest String Chain —— LC 1048


**題型**：找出最長的鏈／序列，其中每個元素都是由前一個元素做一次結構性操作得到的

#### 🎯 模式

| 面向 | 說明 |
|--------|--------|
| **模式** | 串鏈／序列 DP |
| **核心操作** | 每個元素都剛好比前一個大／不同一步 |
| **兩種解法** | ① 由下而上 DP（排序 + 刪字元）② 由上而下 DFS + 記憶化 |
| **關鍵資料結構** | `Map<String, Integer> dp` 或 `Map<String, Integer> memo` |
| **時間複雜度** | O(N × L²)，N = 單字數，L = 最長單字長度 |
| **空間複雜度** | O(N)，用於 map |

#### 💡 核心想法（LC 1048 - Longest String Chain）

> **單字 A 是單字 B 的前驅**，若在 A 中插入剛好一個字母就能得到 B。

**兩種等價的思考方向**：
- **正向（DFS）**：從單字 `w` 出發，找出所有長度加一且合法的後繼 → 遞迴下去
- **反向（由下而上 DP）**：對單字 `w`，逐一嘗試刪掉一個字元 → 檢查較短的那個字串是不是已知的前驅

**關鍵洞察 —— 反向做法更簡單**：
- 依長度排序單字（短的在前）
- 對每個單字，一次刪掉一個字元，產生所有可能的前驅
- 若某個前驅存在於 dp map 中，就延長它的鏈
- 這樣完全不需要寫 `isOneOff` 比較

```text
words = ["a","b","ba","bca","bda","bdca"]
Sorted: ["a","b","ba","bca","bda","bdca"]

dp["a"]    = 1  (no predecessors)
dp["b"]    = 1  (no predecessors)
dp["ba"]   = 2  (remove 'b' → "a" exists, remove 'a' → "b" exists → max(dp["a"], dp["b"]) + 1 = 2)
dp["bca"]  = 3  (remove 'c' → "ba" exists → dp["ba"] + 1 = 3)
dp["bda"]  = 3  (remove 'd' → "ba" exists → dp["ba"] + 1 = 3)
dp["bdca"] = 4  (remove 'c' → "bda" exists → dp["bda"] + 1 = 4)

Answer: 4
```

#### **解法 1：由下而上 DP** ⭐（推薦 —— 比較簡單）

**狀態**：`dp[word]` = 以 `word` 結尾的最長鏈長度

```java
public int longestStrChain(String[] words) {
    // Step 1: Sort by word length (process predecessors before successors)
    Arrays.sort(words, (a, b) -> a.length() - b.length());

    // Step 2: dp[word] = longest chain ending at this word
    Map<String, Integer> dp = new HashMap<>();
    int maxChain = 1;

    for (String word : words) {
        int best = 1;

        // Step 3: Try removing each character to generate all predecessors
        for (int i = 0; i < word.length(); i++) {
            String prev = word.substring(0, i) + word.substring(i + 1);
            // If predecessor exists, extend its chain
            best = Math.max(best, dp.getOrDefault(prev, 0) + 1);
        }

        dp.put(word, best);
        maxChain = Math.max(maxChain, best);
    }

    return maxChain;
}
```

**為什麼可行**：排序保證處理到單字 `w` 時，所有比 `w` 短的單字都已經在 `dp` 裡了。刪掉一個字元就能產生所有長度為 `|w|-1` 的可能前驅。

#### **解法 2：由上而下 DFS + 記憶化**

**狀態**：`memo[word]` = 從 `word` 出發的最長鏈長度

```java
private Map<Integer, List<String>> wordLengthMap;
private Map<String, Integer> memo;

public int longestStrChain(String[] words) {
    // Group words by length for O(1) lookup of next-length candidates
    wordLengthMap = new HashMap<>();
    for (String word : words) {
        wordLengthMap.putIfAbsent(word.length(), new ArrayList<>());
        wordLengthMap.get(word.length()).add(word);
    }

    int maxPath = 1;
    memo = new HashMap<>();
    for (String word : words)
        maxPath = Math.max(maxPath, dfs(word));

    return maxPath;
}

private int dfs(String word) {
    // Base case: no words of next length exist
    if (!wordLengthMap.containsKey(word.length() + 1)) return 1;
    if (memo.containsKey(word)) return memo.get(word);

    int maxPath = 0;
    // Try all words of length+1 as potential successors
    for (String nextWord : wordLengthMap.get(word.length() + 1)) {
        if (isOneOff(word, nextWord))
            maxPath = Math.max(maxPath, dfs(nextWord));
    }

    memo.put(word, maxPath + 1);
    return memo.get(word);
}

// Two-pointer: returns true if b has exactly one more char than a
private boolean isOneOff(String a, String b) {
    int count = 0;
    for (int i = 0, j = 0; i < b.length() && j < a.length() && count <= 1; i++) {
        if (a.charAt(j) != b.charAt(i)) count++;
        else j++;
    }
    return count <= 1;
}
```

#### **兩種解法比較** —— LC 1048

| | 由下而上 DP（解法 1） | 由上而下 DFS（解法 2） |
|---|---|---|
| **方向** | 反向：刪字元找前驅 | 正向：加字元找後繼 |
| **是否要排序** | 需要（短的在前） | 不需要 |
| **是否要輔助函式** | 不用（取子字串本身就是檢查） | 要（`isOneOff` 雙指標） |
| **複雜度** | O(N × L²) | O(N × L²) |
| **簡潔度** | 比較簡單 ✅ | 比較囉嗦 |

#### **相似 LeetCode 題目** 📚

| 題目 | LC # | 鏈的元素 | 操作 | 模式 |
|---------|------|--------------|-----------|---------|
| **Longest String Chain** | 1048 | 字串 | 插入 1 個字元 | 排序 + 刪字元 DP |
| **Longest Increasing Subsequence** | 300 | 數字 | 增加任意量 | 排序 + 一維 DP 或 patience sort |
| **Longest Consecutive Sequence** | 128 | 數字 | 剛好 +1 | 雜湊集合查表 |
| **Word Ladder** | 127 | 字串 | 改 1 個字元（非插入） | BFS（找最短路徑） |
| **Longest Increasing Path in Matrix** | 329 | 格子 | 移到更大的鄰居 | 二維網格上的 DFS + 記憶化 |
| **Longest Path in Tree** | 2246 | 樹節點 | 父子邊 | 樹上 DFS |
| **Concatenated Words** | 472 | 字串 | 一個字是另一個的前綴 | DP + word break |

**關鍵區別**：
- LC 1048 vs LC 300：兩者都是「最長鏈」，但 1048 靠字串結構，300 靠數值大小
- LC 1048 vs LC 127：1048 插入字元（長度會變），127 替換字元（長度固定）→ 用 BFS 找最短路徑
- LC 1048 vs LC 128：1048 可以插在任何位置，128 要求整數連續

#### **模式辨識檢查表** ✅ —— LC 1048

出現以下情況就用這個模式：
- ✅ 要建一條鏈，鏈上每個元素跟下一個剛好差一次操作
- ✅ 前驅／後繼關係定義明確（插入字元、值 +1 等等）
- ✅ 要在所有可能的起點中找出最長的那條鏈
- ✅ 同一個元素可能出現在來自多個不同前驅的鏈裡 → 記憶化

#### **容易踩到的坑** ⚠️

1. **忘記排序（由下而上 DP）**：一定要先依長度排序，處理後繼時前驅才會在 `dp` 裡
2. **用 `contains` 而不是 `getOrDefault`**：一律寫 `dp.getOrDefault(prev, 0) + 1` —— 前驅不一定在清單中
3. **產生後繼而非前驅（由下而上）**：刪字元（產生前驅）比插字元（產生後繼）簡單，要產生的字串也少很多
4. **驗證的複雜度（由上而下 DFS）**：用 O(L) 的雙指標 `isOneOff`，不要用 O(L²) 的暴力比對

**檔案參考**：`leetcode_java/src/main/java/LeetCodeJava/DynamicProgramming/LongestStringChain.java`

---

## 區間與分割排程 DP

### 加權區間排程 —— DP + 二分搜尋 —— LC 1235


> **模式**：物件是帶有價值的*區間*；選了其中一個，就不能再選任何與它重疊的區間。依**結束時間**排序後，「還有哪些物件相容？」就變成在 DP 陣列的前綴上做**二分搜尋**。

#### 🎯 模式辨識 —— LC 1235

| 訊號 | 意義 |
|--------|---------|
| 輸入是 `(start, end, value)` 三元組 | 這是對*物件*而非對區間範圍做的區間 DP |
| 「不重疊」／「同時只能參加一場」 | 加權區間排程 |
| 每個區間的價值不同 | 貪婪（活動選擇）**會錯** → 必須用 DP |
| n 高達 5·10⁴ | O(n²) 太慢 → 用二分搜尋找前驅 |

> ⚠️ 經典的貪婪「挑最早結束的」只有在每個區間價值相同時才成立。有權重時就必須比較*選*與*不選*。

#### 💡 核心想法 —— LC 1235

```text
sort jobs by endTime
dp[i] = max profit using the first i jobs (sorted order)

take    : profit[i] + dp[p(i)]     where p(i) = # of jobs whose end <= start[i]
skip    : dp[i-1]
dp[i]   = max(take, skip)

p(i) is found by binary search over the (sorted) end times already in dp.
```

**遞迴式**：`dp[i] = max(dp[i-1], profit_i + dp[bisect_right(ends, start_i)])`

```java
// java
// LC 1235 - Maximum Profit in Job Scheduling
// IDEA: sort by end time; dp[i] = best profit among first i jobs;
//       binary search the last job that finishes at or before job i's start.
// time = O(n log n), space = O(n)
public int jobScheduling(int[] startTime, int[] endTime, int[] profit) {
    int n = startTime.length;
    int[][] jobs = new int[n][3];
    for (int i = 0; i < n; i++) {
        jobs[i] = new int[]{endTime[i], startTime[i], profit[i]};
    }
    Arrays.sort(jobs, (a, b) -> Integer.compare(a[0], b[0])); // by end time

    int[] ends = new int[n + 1];   // ends[0] = 0  (sentinel "no job")
    int[] dp = new int[n + 1];     // dp[0]   = 0

    for (int i = 1; i <= n; i++) {
        int s = jobs[i - 1][1], p = jobs[i - 1][2];

        /** NOTE !!! binary search on ends[0..i-1] :
         *  largest idx with ends[idx] <= s  ->  dp[idx] is the compatible prefix
         */
        int lo = 0, hi = i - 1, idx = 0;
        while (lo <= hi) {
            int mid = lo + (hi - lo) / 2;
            if (ends[mid] <= s) { idx = mid; lo = mid + 1; }
            else { hi = mid - 1; }
        }

        dp[i] = Math.max(dp[i - 1], dp[idx] + p);  // skip vs take
        ends[i] = jobs[i - 1][0];
    }
    return dp[n];
}
```

```python
# python
# LC 1235 - Maximum Profit in Job Scheduling
# IDEA: sort by end time, dp is non-decreasing, so bisect over the end-time list
# time = O(n log n), space = O(n)
import bisect

def jobScheduling(startTime, endTime, profit):
    jobs = sorted(zip(endTime, startTime, profit))   # sort by end time
    ends = [0]      # sentinel: "no job taken"
    dp = [0]

    for e, s, p in jobs:
        # last job whose end <= s  (compatible predecessor)
        i = bisect.bisect_right(ends, s) - 1
        dp.append(max(dp[-1], dp[i] + p))            # skip vs take
        ends.append(e)

    return dp[-1]
```

**為什麼 `dp` 一定遞增**：`dp[i] = max(dp[i-1], ...) >= dp[i-1]`，所以前綴最大值*就是* `dp[i]`，不必額外維護 running max。

#### 變化題：多一個「預算」維度 —— LC 1751

> **變化點**：一樣是排序 + 二分搜尋，但**最多只能參加 `k`** 場 → 加上第二個狀態維度 `k`。另外注意這題的活動是*閉區間的天數範圍*，所以前驅必須在當前起點**之前嚴格結束**。

```python
# python
# LC 1751 - Maximum Number of Events That Can Be Attended II
# IDEA: LC 1235 + a "how many picks left" dimension; dp[i][t] = best value using
#       first i events (sorted by end) while attending at most t of them.
# time = O(n * k * log n), space = O(n * k)
import bisect

def maxValue(events, k):
    events.sort(key=lambda e: e[1])                   # by end day
    n = len(events)
    ends = [0] + [e[1] for e in events]
    dp = [[0] * (k + 1) for _ in range(n + 1)]

    for i in range(1, n + 1):
        s, e, v = events[i - 1]
        # last event ending strictly before s (days are inclusive)
        j = bisect.bisect_right(ends, s - 1, 0, i) - 1
        for t in range(1, k + 1):
            dp[i][t] = max(dp[i - 1][t],              # skip event i
                           dp[j][t - 1] + v)          # take event i
    return dp[n][k]
```

#### 相似 LeetCode 題目 📚

| 題目 | LC # | 變化點 |
|---------|------|-------|
| Maximum Profit in Job Scheduling | 1235 | 基礎模式 |
| Maximum Number of Events That Can Be Attended II | 1751 | 多一個「最多挑 k 個」的維度 |
| Russian Doll Envelopes | 354 | 同樣是「先排序再對前綴做 DP」，但底層是 LIS 而非區間 |

---

### 把陣列切成 K 段連續區塊 —— LC 1335


> **模式**：把陣列切成剛好 `k` 段**連續**區塊，並最佳化一個以區塊 `sum / max` 計算的成本。這跟區間 DP（模板 3）不同：這裡的決策是切點，而且區塊必須由左到右鋪滿整個陣列。

#### 🎯 模式辨識 —— LC 1335

- 「分成 `d` 天／`k` 個子陣列／`m` 段」
- 元素順序固定（不能重排、不能跳過）
- 區塊成本可以邊掃邊增量計算（`max`、前綴和）

#### 💡 核心想法 —— LC 1335

```text
dp[k][i] = best cost to cover jobs[i:] using exactly k blocks

dp[k][i] = min over j in [i, n-k] of ( cost(i..j) + dp[k-1][j+1] )
                                       ^ this block   ^ the rest

base: dp[0][n] = 0, dp[0][i<n] = INF   (0 blocks must consume 0 jobs)
answer: dp[d][0];  impossible when n < d
```

**關鍵技巧**：把 `j` 往外擴，同時用 O(1) 維護 `cost(i..j)`（這題是 running `max`，LC 410 是 running 前綴和）—— 這樣轉移就是 O(n) 而不是 O(n²)。

```java
// java
// LC 1335 - Minimum Difficulty of a Job Schedule
// IDEA: dp[day][i] = min total difficulty to finish jobs[i:] in `day` days;
//       inner loop grows the current day's block, tracking its running max.
// time = O(d * n^2), space = O(n)  (rolling over the day dimension)
public int minDifficulty(int[] jobDifficulty, int d) {
    int n = jobDifficulty.length;
    if (n < d) return -1;                 // not enough jobs: each day needs >= 1

    final int INF = Integer.MAX_VALUE / 2;
    int[] dp = new int[n + 1];
    Arrays.fill(dp, INF);
    dp[n] = 0;                            // 0 jobs left with 0 days left

    for (int day = 1; day <= d; day++) {
        int[] ndp = new int[n + 1];
        Arrays.fill(ndp, INF);
        // i can start at most at n-day, leaving >= 1 job per remaining day
        for (int i = 0; i <= n - day; i++) {
            int mx = 0;
            for (int j = i; j <= n - day; j++) {   // today handles jobs[i..j]
                mx = Math.max(mx, jobDifficulty[j]);
                ndp[i] = Math.min(ndp[i], mx + dp[j + 1]);
            }
        }
        dp = ndp;
    }
    return dp[0];
}
```

```python
# python
# LC 1335 - Minimum Difficulty of a Job Schedule
# IDEA: same recurrence, rolling 1D array over the "day" dimension
# time = O(d * n^2), space = O(n)
def minDifficulty(jobDifficulty, d):
    n = len(jobDifficulty)
    if n < d:
        return -1

    INF = float('inf')
    dp = [INF] * (n + 1)
    dp[n] = 0                                   # 0 jobs left, 0 days left

    for day in range(1, d + 1):
        ndp = [INF] * (n + 1)
        for i in range(n - day + 1):            # start of today's block
            mx = 0
            for j in range(i, n - day + 1):     # end of today's block
                mx = max(mx, jobDifficulty[j])
                if dp[j + 1] < INF:
                    ndp[i] = min(ndp[i], mx + dp[j + 1])
        dp = ndp

    return dp[0]
```

#### 變化題：最小化最大的區塊 —— LC 410

> **變化點**：分割的骨架完全一樣，但目標變成 `min over splits of (max block sum)` → 轉移時用 `max` 合併而不是 `+`。（LC 410 還有著名的 O(n log S) *對答案二分搜尋*解法；不過面試官通常先要你推導出下面這個 DP。）

```python
# python
# LC 410 - Split Array Largest Sum
# IDEA: dp[t][i] = min possible "largest subarray sum" when splitting nums[:i] into t parts
# time = O(k * n^2), space = O(k * n)
def splitArray(nums, k):
    n = len(nums)
    pre = [0] * (n + 1)
    for i, v in enumerate(nums):
        pre[i + 1] = pre[i] + v

    INF = float('inf')
    dp = [[INF] * (n + 1) for _ in range(k + 1)]
    dp[0][0] = 0

    for t in range(1, k + 1):
        for i in range(1, n + 1):
            for j in range(t - 1, i):           # last part = nums[j:i]
                dp[t][i] = min(dp[t][i], max(dp[t - 1][j], pre[i] - pre[j]))

    return dp[k][n]
```

| 題目 | LC # | 區塊成本 | 跨區塊合併 |
|---------|------|-----------|---------|
| Minimum Difficulty of a Job Schedule | 1335 | 區塊的 `max` | 用 `+` |
| Split Array Largest Sum | 410 | 區塊的 `sum` | 用 `max` |

---

## 狀態維度 DP

### 多加一個「上一步」狀態維度 —— LC 403


> **模式**：只有位置**不足以**構成狀態 —— 接下來能做什麼，取決於*你是怎麼走到這裡的*。把上一次的轉移加進狀態：`dp[position][lastMove]`。只要天真的 `dp[i]` 因為「同一格能用不同方式抵達、而後續發展不同」而算錯，這就是解法。

#### 🎯 模式辨識 —— LC 403

| 訊號 | 該加的額外維度 |
|--------|------------------------|
| 「下一跳必須是 k-1、k 或 k+1」 | 上一跳的距離 |
| 「同一個方向不能用兩次」 | 上一次的方向 |
| 「最多連續 2 次」 | 目前連續長度 |
| 「賣出後有冷卻期」 | 上一次的動作（見 [dp.md](./dp.md) 的狀態機模板） |

#### 💡 核心想法（LC 403 Frog Jump）

```text
state  : (stone index i, jump size k that landed on i)
init   : (0, 0)
move   : from (i, k) you may jump k-1, k or k+1 (must be > 0)
         land on stone at position stones[i] + nk  ->  state (j, nk)
answer : any state on the last stone is reachable

dp[i] = set of jump sizes that can land ON stone i
```

因為跳躍只會**往前**（`j > i`），由左到右掃一遍就夠了 —— 不需要遞迴。

```java
// java
// LC 403 - Frog Jump
// IDEA: dp.get(i) = set of jump sizes that can land on stone i;
//       push forward to stones[i] + (k-1 | k | k+1) via a position -> index map.
// time = O(n^2), space = O(n^2)
public boolean canCross(int[] stones) {
    int n = stones.length;
    Map<Integer, Integer> pos = new HashMap<>();   // stone position -> index
    for (int i = 0; i < n; i++) pos.put(stones[i], i);

    List<Set<Integer>> dp = new ArrayList<>();
    for (int i = 0; i < n; i++) dp.add(new HashSet<>());
    dp.get(0).add(0);                              // start: landed with jump 0

    for (int i = 0; i < n; i++) {
        for (int k : dp.get(i)) {
            for (int nk = k - 1; nk <= k + 1; nk++) {
                if (nk <= 0) continue;             // jump must move forward
                Integer j = pos.get(stones[i] + nk);
                /** NOTE !!! j > i guarantees we only write to FUTURE stones,
                 *  so mutating dp while scanning is safe. */
                if (j != null && j > i) dp.get(j).add(nk);
            }
        }
    }
    return !dp.get(n - 1).isEmpty();
}
```

```python
# python
# LC 403 - Frog Jump
# IDEA: dp[i] = set of jump sizes that can land on stone i (forward propagation)
# time = O(n^2), space = O(n^2)
def canCross(stones):
    n = len(stones)
    pos = {s: i for i, s in enumerate(stones)}     # position -> index
    dp = [set() for _ in range(n)]
    dp[0].add(0)                                   # start with jump size 0

    for i in range(n):
        for k in dp[i]:
            for nk in (k - 1, k, k + 1):
                if nk <= 0:
                    continue
                j = pos.get(stones[i] + nk)
                if j is not None and j > i:        # only forward
                    dp[j].add(nk)

    return len(dp[n - 1]) > 0
```

#### 容易踩到的坑 ⚠️

- **忘記 `nk > 0`** —— 跳 0 步（或負數）會在同一顆石頭上無限繞下去。
- **把 `dp[i]` 寫成 boolean** —— 只記可達性會丟掉跳躍距離，答案就錯了（例如 `[0,1,3,6,10,13,14]`）。
- **沒去重** —— 每顆石頭用一個 `Set`，否則狀態空間會爆掉。
- 由上而下的 `memo[(i, k)] -> boolean` 配 DFS 是等價寫法，複雜度相同。

---

### 以步數為層的計數／機率 DP —— LC 935


> **模式**：一張**小的狀態圖**（10 個電話鍵、n×n 棋盤、一維陣列）加上**固定的移動次數**。答案是「走了 `t` 步之後，落在各狀態的方法數／機率是多少」。DP 的層就是步數，所以每次只滾一層。

#### 💡 核心想法 —— LC 935

```text
dp[t][v] = ways (or probability) to be at state v after t steps
dp[t][v] = sum over u with edge u -> v of dp[t-1][u]

counting     -> take everything mod 1e9+7
probability  -> divide each contribution by the out-degree
```

只會用到 `dp[t-1]` → 用兩個陣列（`dp`、`ndp`）取代 `steps × V` 的表。

```java
// java
// LC 935 - Knight Dialer
// IDEA: dp[d] = # of distinct numbers of current length ending on digit d;
//       one layer per additional dialed digit.
// time = O(n * 10 * 3) = O(n), space = O(10) = O(1)
public int knightDialer(int n) {
    final int MOD = 1_000_000_007;
    // knight moves on the phone pad (5 is unreachable)
    int[][] moves = {{4,6},{6,8},{7,9},{4,8},{0,3,9},{},{0,1,7},{2,6},{1,3},{2,4}};

    long[] dp = new long[10];
    Arrays.fill(dp, 1);                 // length-1 numbers: each digit once

    for (int step = 1; step < n; step++) {
        long[] ndp = new long[10];
        for (int d = 0; d < 10; d++) {
            for (int nxt : moves[d]) {
                ndp[nxt] = (ndp[nxt] + dp[d]) % MOD;
            }
        }
        dp = ndp;                       // roll the layer
    }

    long res = 0;
    for (long v : dp) res = (res + v) % MOD;
    return (int) res;
}
```

```python
# python
# LC 935 - Knight Dialer
# IDEA: same layer-rolling; MOVES is the knight-move adjacency of the keypad
# time = O(n), space = O(1)
MOD = 10 ** 9 + 7
MOVES = {0: [4, 6], 1: [6, 8], 2: [7, 9], 3: [4, 8], 4: [0, 3, 9],
         5: [],     6: [0, 1, 7], 7: [2, 6], 8: [1, 3], 9: [2, 4]}

def knightDialer(n):
    dp = [1] * 10                       # numbers of length 1
    for _ in range(n - 1):
        ndp = [0] * 10
        for d in range(10):
            for nxt in MOVES[d]:
                ndp[nxt] = (ndp[nxt] + dp[d]) % MOD
        dp = ndp
    return sum(dp) % MOD
```

#### 變化題：機率而非計數 —— LC 688

> **變化點**：狀態是棋盤格，每步從 8 個方向均勻挑一個，走出棋盤就*算輸* —— 所以每層的總和會衰減。答案是最後一層的總和。

```python
# python
# LC 688 - Knight Probability in Chessboard
# IDEA: dp[r][c] = probability of standing on (r,c) after t moves; spread /8 each step
# time = O(k * n^2 * 8), space = O(n^2)
def knightProbability(n, k, row, column):
    dirs = [(1,2),(2,1),(-1,2),(-2,1),(1,-2),(2,-1),(-1,-2),(-2,-1)]
    dp = [[0.0] * n for _ in range(n)]
    dp[row][column] = 1.0

    for _ in range(k):
        ndp = [[0.0] * n for _ in range(n)]
        for r in range(n):
            for c in range(n):
                if dp[r][c] == 0.0:
                    continue
                for dr, dc in dirs:
                    nr, nc = r + dr, c + dc
                    if 0 <= nr < n and 0 <= nc < n:   # off-board = fell off
                        ndp[nr][nc] += dp[r][c] / 8.0
        dp = ndp

    return sum(map(sum, dp))
```

#### 變化題：把可達狀態空間夾住 —— LC 1269

> **變化點**：`arrLen` 可以到 10⁶，但只有 `steps` 步的話，你永遠走不過索引 `steps // 2`（還得走回來）。把狀態空間夾成 `min(arrLen, steps // 2 + 1)` 才過得了。

```python
# python
# LC 1269 - Number of Ways to Stay in the Same Place After Some Steps
# IDEA: dp[i] = ways to be at index i; each step move left / stay / right
# time = O(steps * min(arrLen, steps/2)), space = O(min(arrLen, steps/2))
def numWays(steps, arrLen):
    MOD = 10 ** 9 + 7
    m = min(arrLen, steps // 2 + 1)     # unreachable indices pruned away
    dp = [0] * m
    dp[0] = 1

    for _ in range(steps):
        ndp = [0] * m
        for i in range(m):
            if dp[i]:
                for j in (i - 1, i, i + 1):
                    if 0 <= j < m:
                        ndp[j] = (ndp[j] + dp[i]) % MOD
        dp = ndp

    return dp[0]                        # must end back at index 0
```

#### 模式辨識檢查表 ✅

- [ ] 輸入給定固定的步數／回合數（`n`、`k`、`steps`）
- [ ] 狀態空間很小，轉移圖是固定的
- [ ] 題目問「有幾種走法」（mod 1e9+7）或「機率是多少」
- [ ] → 每步滾一層；狀態維度上大致是 O(1) 空間

---

## 單調佇列與單調堆疊 DP

### 用單調佇列優化 DP —— LC 1425
當 `dp[i] = max(dp[j]) + f(i)`、且 `j` 落在滑動視窗 `[i-k, i-1]` 內時，用單調雙端佇列把 O(n²) 降到 O(n)。

```python
from collections import deque

# LC 1425 Constrained Subsequence Sum
def constrainedSubsetSum(nums, k):
    n = len(nums)
    dp = nums[:]       # dp[i] = max sum of subsequence ending at i
    dq = deque()       # decreasing deque of indices by dp value

    for i in range(n):
        # Best previous dp in window [i-k, i-1]
        if dq and dp[dq[0]] > 0:
            dp[i] = max(dp[i], dp[dq[0]] + nums[i])
        # Maintain decreasing order
        while dq and dp[dq[-1]] <= dp[i]:
            dq.pop()
        dq.append(i)
        # Remove elements outside window
        if dq[0] == i - k:
            dq.popleft()

    return max(dp)
```

### Largest Rectangle in Histogram —— LC 84（堆疊 DP）
用單調堆疊在 O(n) 時間內找出最大矩形面積。

```python
def largestRectangleArea(heights):
    stack = []   # increasing stack of indices
    max_area = 0
    heights.append(0)   # sentinel to flush stack

    for i, h in enumerate(heights):
        while stack and heights[stack[-1]] >= h:
            height = heights[stack.pop()]
            width = i if not stack else i - stack[-1] - 1
            max_area = max(max_area, height * width)
        stack.append(i)
    return max_area

# LC 85 Maximal Rectangle in Matrix — apply LC 84 row by row
def maximalRectangle(matrix):
    if not matrix: return 0
    n = len(matrix[0])
    heights = [0] * n
    ans = 0
    for row in matrix:
        heights = [heights[j] + int(row[j]) if row[j] != '0' else 0 for j in range(n)]
        ans = max(ans, largestRectangleArea(heights[:]))
    return ans
```

## 模式選擇 —— 延伸參考

### 決策流程圖

#### 決策流程圖

```text
START: What type of problem are you solving?
│
├─ Working with a SINGLE SEQUENCE/ARRAY?
│  │
│  ├─ Linear dependencies (dp[i] from dp[i-1]) → Category 1 (Linear DP)
│  │                                               Examples: LC 70, 198, 300
│  │
│  └─ Selection with constraints → Category 6 (Knapsack DP)
│                                    Examples: LC 416, 494, 518
│
├─ Working with a 2D GRID/MATRIX?
│  │
│  └─ Path counting, min/max path → Category 2 (Grid/2D DP)
│                                     Examples: LC 62, 64, 221
│
├─ Working with INTERVALS/SUBARRAYS?
│  │
│  └─ Optimal split, merge, or partition → Category 3 (Interval DP)
│                                           Examples: LC 312, 1000, 516
│
├─ Working with TREE structures?
│  │
│  ├─ State at node depends on children → Category 4 (Tree DP)
│  │                                       Examples: LC 337, 968, 124
│  │
│  └─ "For every node, compute ..." → Re-rooting DP (two-pass DFS)
│                                       Examples: LC 834, 2581
│
├─ Working with STRINGS?
│  │
│  ├─ Two strings (matching/alignment) → Category 7 (String DP)
│  │                                      Examples: LC 72, 1143, 583
│  │
│  └─ Single string (palindrome, split) → Also Category 7
│                                          Examples: LC 5, 131, 647
│
├─ Problem has MULTIPLE STATES with transitions?
│  │
│  └─ Stock trading, state machines → Category 5 (State Machine DP)
│                                      Examples: LC 122, 309, 714
│
└─ Need to track SUBSET/VISITED items efficiently?
   │
   └─ Use bitmask to compress state → Category 8 (State Compression DP)
                                       Examples: LC 691, 847, 1723
```

### 關鍵字辨識索引

#### 依關鍵字辨識模式

**線性序列關鍵字** → 類別 1（線性 DP）
- 「nth Fibonacci」、「climbing stairs」、「decode ways」
- 「house robber」、「non-adjacent」、「skip adjacent」
- 「longest increasing subsequence」、「LIS」

**網格／矩陣關鍵字** → 類別 2（網格／二維 DP）
- 「grid」、「matrix」、「m x n」
- 「unique paths」、「number of ways」
- 「minimum/maximum path sum」
- 「maximal square」、「largest rectangle」

**區間／子陣列關鍵字** → 類別 3（區間 DP）
- 「burst」、「merge」、「split」、「partition」
- 「optimal way to cut/divide」
- 「minimum cost to merge」
- 「palindrome partitioning」

**樹的關鍵字** → 類別 4（樹 DP）
- 「binary tree」、「tree structure」
- 「each node」、「children」、「parent」
- 「rob houses on tree」、「cameras on tree」
- 「for every node compute」、「sum of distances」→ 換根 DP（LC 834）

**狀態轉移關鍵字** → 類別 5（狀態機 DP）
- 「buy and sell stock」
- 「cooldown」、「transaction fee」
- 「at most k transactions」
- 「multiple states」

**帶限制的選取** → 類別 6（背包 DP）
- 「subset sum」、「partition equal」
- 「target sum」、「combination sum」
- 「0/1 knapsack」、「unbounded knapsack」
- 「coin change」、「unlimited supply」

**字串比對關鍵字** → 類別 7（字串 DP）
- 「edit distance」、「minimum operations」
- 「longest common subsequence (LCS)」
- 「palindrome subsequence/substring」
- 「string transformation」

**子集合／已走訪追蹤** → 類別 8（狀態壓縮 DP）
- 「visit all nodes」、「shortest path visiting all」
- 「assign tasks」、「match workers」
- 「traveling salesman problem (TSP)」
- 「subset enumeration with constraints」

#### 快速判斷範例

1. **「找出網格中的最小路徑和」**
   - 關鍵字：「grid」、「minimum path sum」
   - 判斷：類別 2（網格／二維 DP）
   - 模板：dp[i][j] = min(dp[i-1][j], dp[i][j-1]) + grid[i][j]

2. **「用硬幣湊出金額有幾種方法」**
   - 關鍵字：「coin change」、「unlimited supply」
   - 判斷：類別 6（完全背包 DP）
   - 模板：dp[amount] = sum of dp[amount - coin]

3. **「求兩個字串的編輯距離」**
   - 關鍵字：「edit distance」、「two strings」
   - 判斷：類別 7（字串 DP）
   - 模板：dp[i][j]，含插入／刪除／替換三種操作

4. **「有冷卻期的股票買賣」**
   - 關鍵字：「stock」、「cooldown」
   - 判斷：類別 5（狀態機 DP）
   - 模板：3 個狀態（hold、sold、rest）

5. **「走訪圖中所有節點的最短路徑」**
   - 關鍵字：「visit all nodes」、「shortest path」
   - 判斷：類別 8（狀態壓縮 DP）
   - 模板：dp[mask][node]，用位元遮罩記錄走訪狀態

6. **「戳氣球讓硬幣最大化」**
   - 關鍵字：「burst」、「maximize」
   - 判斷：類別 3（區間 DP）
   - 模板：對區間 [i, j] 的 dp[i][j]

7. **「在二元樹上打家劫舍」**
   - 關鍵字：「tree」、「rob」、「non-adjacent」
   - 判斷：類別 4（樹 DP）
   - 模板：由下而上 DFS，每個節點兩個狀態

### 實用訣竅與陷阱

#### 選模式的實用訣竅

- **單一序列** → 線性 DP（類別 1）
- **兩個序列** → 通常是字串 DP（類別 7）或二維 DP
- **在網格上移動** → 網格 DP（類別 2）
- **切分區間** → 區間 DP（類別 3），常見複雜度 O(n³)
- **走訪樹** → 樹 DP（類別 4），用 DFS
- **多個狀態** → 狀態機（類別 5），先畫狀態轉移圖
- **重量／容量限制** → 背包（類別 6）
- **字串比對／轉換** → 字串 DP（類別 7）
- **走訪全部／子集合** → 狀態壓縮（類別 8），用位元遮罩

#### 容易踩到的坑

- **區間 DP**：記得長度要由小到大迭代
- **背包**：0/1 背包做空間優化時必須反向迭代
- **狀態機**：動手寫程式前先畫狀態轉移圖
- **樹 DP**：用由下而上的 DFS（後序走訪）；遇到「每個節點都當一次根」的題目，用換根 DP（兩趟 DFS，LC 834）
- **狀態壓縮**：先確認 n ≤ 20（2^20 個狀態才跑得動）
- **字串 DP**：小心定義 dp[i][j]（是長度還是索引）

---

### 常見遞迴關係

#### **總和／計數類**
```python
# Fibonacci-like
dp[i] = dp[i-1] + dp[i-2]

# Include/exclude current
dp[i] = dp[i-1] + (dp[i-2] + nums[i])
```

#### **最小／最大類**
```text
# Take or skip
dp[i] = max(dp[i-1], dp[i-2] + nums[i])

# Best from all previous
dp[i] = max(dp[j] + score(j, i) for j < i)
```

#### **網格類**
```python
# Path counting
dp[i][j] = dp[i-1][j] + dp[i][j-1]

# Min path
dp[i][j] = min(dp[i-1][j], dp[i][j-1]) + grid[i][j]
```

### 空間優化技巧

#### **滾動陣列**
```python
# From O(n²) to O(n)
# Instead of dp[i][j], use dp[2][j]
curr = [0] * n
prev = [0] * n
for i in range(m):
    curr, prev = prev, curr
    # Update curr based on prev
```

#### **狀態壓縮**
```python
# From O(n) to O(1) for Fibonacci-like
prev2, prev1 = 0, 1
for i in range(2, n):
    curr = prev1 + prev2
    prev2, prev1 = prev1, curr
```

### 狀態機 DP 的面試模式辨識

**快速決策樹：**
```text
Stock/Transaction Problem?
├─ NO → Check other DP patterns
└─ YES → Continue below

Are there any constraints on transactions?
├─ NO constraints (unlimited) → 2 states (hold/cash) [LC 122]
├─ Cooldown period → 3 states (hold/sold/rest) [LC 309]
├─ Transaction fee → 2 states + fee deduction [LC 714]
├─ Limited k transactions → 2k states [LC 123, LC 188]
└─ Buy only once → Kadane's algorithm [LC 121]
```

**狀態機模式對照：**

| 限制型態 | 狀態數 | 狀態名稱 | 買進條件 | 賣出條件 | 範例 LC |
|----------------|--------|-------------|---------------|----------------|------------|
| 無 | 2 | hold, cash | `cash - price` | `hold + price` | 122 |
| **冷卻期** | **3** | **hold, sold, rest** | `rest - price` ⚠️ | `hold + price` | **309** |
| 交易手續費 | 2 | hold, cash | `cash - price` | `hold + price - fee` | 714 |
| k 次交易 | 2k | buy1, sell1, ... | 追蹤交易次數 | 追蹤交易次數 | 123, 188 |

**⚠️ 冷卻期模式的關鍵差異：**
- 一般：`hold = max(hold, cash - price)` —— 隨時可以買
- 冷卻期：`hold = max(hold, rest - price)` —— 只能從 rest 狀態買！

**模式辨識速查表：**

| 題目說… | 模式 | 狀態 | 關鍵轉移 |
|----------------|---------|--------|----------------|
| 「賣出後冷卻 1 天」 | 3 狀態 | hold/sold/rest | 只能從 `rest` 買進 |
| 「每筆交易手續費 k」 | 2 狀態 | hold/cash | `cash = hold + price - fee` |
| 「最多 2 次交易」 | 4 狀態 | buy1/sell1/buy2/sell2 | 追蹤交易次數 |
| 「最多 k 次交易」 | 2k 狀態 | 動態 | k 次交易的一般化 |
| 「交易次數不限」 | 2 狀態 | hold/cash | 單純買進賣出 |

**面試常見追問：**
1. 「冷卻期改成 k 天呢？」→ 需要 k+2 個狀態
2. 「冷卻期跟手續費同時有呢？」→ 3 個狀態再扣手續費
3. 「做空間優化」→ 用變數取代陣列
4. 「證明正確性」→ 說明狀態轉移如何強制滿足限制

### 狀態機 DP 速查
```text
Stock Problems State Transitions:
  held    = max(held,      rest - price)    # buy: rest → held
  sold    = held + price                    # sell: held → sold
  rest    = max(rest,      sold)            # cooldown: sold → rest

Variants:
  - No cooldown (LC 122):   held = max(held, rest - price); rest = max(rest, sold); sold = held_prev + price
  - With cooldown (LC 309): above with sold → rest (not directly back to held)
  - At most k tx (LC 188):  held[k] = max(held[k], rest[k-1] - price); sold[k] = held[k] + price
  - With fee (LC 714):      held = max(held, rest - price); rest = max(rest, held + price - fee)
```
