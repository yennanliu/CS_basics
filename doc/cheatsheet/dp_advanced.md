# Advanced DP Techniques

> **Scope** — The DP appendix — techniques a first pass should skip: game-theory / minimax DP, tree and re-rooting DP, interval and string DP deep dives, probability and step-indexed counting DP, monotonic-queue and stack-carried DP, and the long-form derivations trimmed out of the main sheet.
> **See also**: [dp.md](./dp.md) — the canonical template for every must-know family; [dp_examples.md](./dp_examples.md) — the worked-solution archive; [knapsack.md](./knapsack.md) — the knapsack family in full; [dp_string.md](./dp_string.md) — the two-sequence grid; [dp_bitmask.md](./dp_bitmask.md) — state compression; [dp_monotonic_stack.md](./dp_monotonic_stack.md) — stack-carried DP values; [monotonic_queue.md](./monotonic_queue.md) — the sliding-window maximum structure.

## LeetCode Problem Lists

- [Dynamic Programming](https://leetcode.com/problem-list/dynamic-programming/)
- [Memoization](https://leetcode.com/problem-list/memoization/)
- [Game Theory](https://leetcode.com/problem-list/game-theory/)
- [Probability and Statistics](https://leetcode.com/problem-list/probability-and-statistics/)

## Overview

Everything here is a second pass. Each section assumes the matching canonical template in
[dp.md](./dp.md) is already familiar and adds either a harder pattern, an alternative
implementation, or the derivation the main sheet only states the conclusion of.

### Key Properties
- **Complexity**: stated per section — interval deep dives are O(n³), bitmask O(2^n · n), the rest O(n) to O(n²)
- **Core Idea**: the state is the interesting part; once the state is right the transition is mechanical
- **When to Use**: a naive state gives wrong answers, or the naive complexity does not fit the constraints

## Problem Categories

| Section | Signal in the problem | LC |
|---------|----------------------|----|
| **Game theory / minimax** | two players, both play optimally | 486, 877, 1140, 1406 |
| **Tree DP / re-rooting** | "for every node, compute ..." on a tree | 834, 2581, 337, 968 |
| **Interval DP deep dives** | order of operations changes the result | 312, 1000, 546, 664 |
| **Weighted interval scheduling** | `(start, end, value)` triples, non-overlapping | 1235, 1751, 354 |
| **Partition into k blocks** | "split into d days / k subarrays" | 1335, 410 |
| **Extra state dimension** | what you may do next depends on how you got here | 403, 309 |
| **Step-indexed counting / probability** | fixed number of moves on a small state graph | 935, 688, 1269 |
| **Monotonic queue / stack DP** | `dp[i] = max(dp[j])` over a sliding window; histogram area | 1425, 84, 85 |
| **Chaining DP** | each element is one operation away from the next | 1048, 300, 329 |

## Game Theory / Minimax DP

### Category overview

- **Description**: Two-player optimal play on arrays; each player picks from either end
- **Examples**: LC 486 (Predict the Winner), LC 877 (Stone Game), LC 1140 (Stone Game II)
- **Pattern**: dp[i][j] = max relative score difference (current player minus opponent) on subarray nums[i..j]
- **Core idea**: When the current player picks, the opponent then plays optimally on the remaining subarray. Subtracting `dp[sub]` flips perspective — the opponent's best becomes your loss.
- **Recurrence**: `dp[i][j] = max(nums[i] - dp[i+1][j], nums[j] - dp[i][j-1])`
- **Base case**: `dp[i][i] = nums[i]` (only one element left, take it)
- **Answer**: `dp[0][n-1] >= 0` means the first player wins or ties

### Predict the Winner — LC 486

**Pattern**: Two players take turns picking from either end of an array, both play optimally. Determine if player 1 can win.

**Core Idea**: Define `dp[i][j]` as the **max score difference** (current player − opponent) on subarray `nums[i..j]`. When you pick `nums[i]`, the opponent then faces `dp[i+1][j]` — which is *their* best relative score. Subtracting it gives your net advantage.

**Why subtract?** After you pick, it's the opponent's turn. `dp[i+1][j]` is the opponent's max advantage on the remaining subarray. From your perspective, that advantage works against you, so you subtract it.

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

**Similar LeetCode Problems:**
| LC # | Problem | Notes |
|------|---------|-------|
| 486 | Predict the Winner | Core minimax interval DP |
| 877 | Stone Game | Same structure; always true for even-length (math proof) |
| 1140 | Stone Game II | Minimax + variable take count (suffix sum optimization) |
| 1406 | Stone Game III | 1D suffix minimax, pick 1-3 stones |
| 464 | Can I Win | Bitmask + minimax (state compression variant) |
| 294 | Flip Game II | Minimax with memoization |

---

## Tree DP and Re-rooting

### Tree DP sub-patterns

**Sub-patterns:**
1. **Bottom-Up Tree DP** (standard)
   - Post-order DFS: state at each node computed from children
   - Examples: LC 337 (House Robber III), LC 968 (Binary Tree Cameras)
2. **Re-rooting DP** (two-pass DFS)
   - Compute answer for one root, then shift root to every other node in O(N)
   - Pass 1 (Post-order): compute subtree sizes and base answer for root 0
   - Pass 2 (Pre-order): re-root from parent to child using mathematical formula
   - Examples: LC 834 (Sum of Distances in Tree), LC 2581 (Count Number of Possible Root Nodes)

### Sum of Distances in Tree — LC 834

> Two-pass DFS: compute root distances, then re-root by adjusting parent answer by ±subtree size.

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

#### Re-rooting DP Template (General)

```java
/**
 * RE-ROOTING DP TEMPLATE
 *
 * Use when: "compute some aggregate for EVERY node as root" on a tree
 *
 * Pattern:
 *   1. Post-order DFS: compute answer for one fixed root (node 0)
 *   2. Pre-order DFS: re-root from parent → child using a transition formula
 *
 * The transition formula depends on the problem:
 *   LC 834: ans[v] = ans[u] - count[v] + (n - count[v])
 *   General: ans[child] = f(ans[parent], subtree_info[child], n)
 *
 * Time:  O(N)
 * Space: O(N)
 *
 * SIMILAR PROBLEMS:
 * | Problem                                  | LC #  | Re-rooting Formula / Key Idea                    |
 * |------------------------------------------|-------|--------------------------------------------------|
 * | Sum of Distances in Tree                 | 834   | ans[v] = ans[u] - count[v] + (n - count[v])     |
 * | Count Number of Possible Root Nodes      | 2581  | Track "good" edges, adjust count when re-rooting |
 * | Minimum Edge Weight Equilibrium Queries   | 2846  | Re-root with edge frequency tracking             |
 * | Sum of Prefix Scores of Strings (on Trie)| 2416  | Similar two-pass idea on trie structure           |
 *
 * WHEN TO SUSPECT RE-ROOTING:
 * - "For every node, compute ..." on a tree
 * - Naive per-node DFS/BFS gives O(N²) → need O(N)
 * - Answer for child can be derived from parent's answer
 */
```

## Interval DP Deep Dives

### Worked trace — LC 312, `nums = [3,1,5,8]`

**Example Trace**: `nums = [3,1,5,8]`

After adding boundaries: `[1, 3, 1, 5, 8, 1]` (indices 0-5)

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


Some interval DP problems use **inclusive** boundaries where `dp[i][j]` includes elements `i` and `j`.

**Python Implementation**:
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

#### **Top-Down (Memoization) Approach**

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

**Java Top-Down**:
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

#### **Complexity Analysis**

**Time Complexity**: O(n³)
- Outer loop (length): O(n)
- Middle loop (left boundary): O(n)
- Inner loop (split point k): O(n)
- Each cell takes O(n) time to compute

**Space Complexity**: O(n²)
- 2D DP table of size `(n+2) × (n+2)`
- Can be optimized in some cases, but generally requires O(n²)

**Reference**: See `leetcode_java/src/main/java/LeetCodeJava/DynamicProgramming/BurstBalloons.java` for multiple implementation variants. The canonical LC 312 template is in [dp.md](./dp.md).

### Recognition checklist and common mistakes

#### **Pattern Recognition Checklist**

Use this interval DP pattern when:
- ✅ Problem involves processing elements in an array/sequence
- ✅ Order of operations affects the result
- ✅ Subproblems become independent after choosing an operation
- ✅ Optimal solution can be built from optimal subproblems
- ✅ Keywords: "merge", "burst", "remove", "split", "multiply"

#### **Common Mistakes to Avoid**

1. **Thinking "first" instead of "last"**:
   - ❌ "Which balloon to burst first?" → Neighbors change, dependencies unclear
   - ✅ "Which balloon to burst last?" → Subproblems are independent

2. **Wrong boundary handling**:
   - Add explicit boundaries (like `[1, ...nums..., 1]`) to simplify edge cases
   - Decide if boundaries are inclusive or exclusive

3. **Off-by-one errors**:
   - Be consistent: `dp[i][j]` means `(i, j)` exclusive or `[i, j]` inclusive
   - Adjust loop ranges accordingly

4. **Incorrect loop order**:
   - Always build from smaller intervals to larger ones
   - Length must be the outermost loop

### Backward-i + Forward-j Loop Order (Palindrome / Substring DP)


**🎯 When to Use This Pattern**

Use this when `dp[i][j]` depends on:
- `dp[i+1][j-1]` — the **inner** substring (both boundaries shrink inward)
- `dp[i+1][j]` — row **below** (i increases)
- `dp[i][j-1]` — column **left** (j decreases)

Classic problems: **LC 516 Longest Palindromic Subsequence**, **LC 5 Longest Palindromic Substring**, **LC 647 Palindromic Substrings**.

**Core Insight: Dependency Direction Determines Loop Order**

```text
dp[i][j] depends on:
    dp[i+1][j-1]   ← diagonal (i+1, j-1): both already computed
    dp[i+1][j]     ← row below: need i+1 before i  → loop i BACKWARD
    dp[i][j-1]     ← column left: need j-1 before j → loop j FORWARD
```

So: **loop `i` from `n-1` down to `0`, loop `j` from `i+1` up to `n-1`**.

**Template (Java)**:
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

**Why NOT `length`-based outer loop here?**

The length-based loop (the interval DP template in [dp.md](./dp.md)) also works, but the backward-i + forward-j approach is more intuitive when the transition naturally reads as "expand/shrink boundaries" rather than "try a split point k".

| Approach | Outer Loop | Use When |
|---|---|---|
| Length-based ([dp.md](./dp.md)) | `length: 2 → n` | Split-point `k` problems (burst balloons, matrix chain) |
| Backward-i + Forward-j (this template) | `i: n-1 → 0` | Boundary expand/shrink problems (palindrome, LCS on same string) |

**Similar LeetCode Problems**:
- **LC 516** — Longest Palindromic Subsequence (exact template above)
- **LC 5** — Longest Palindromic Substring (same loop order, boolean dp)
- **LC 647** — Palindromic Substrings (count all palindromes)
- **LC 1048** — Longest String Chain (DFS+memo or sort-by-length DP; see `LongestStringChain.java`)
- **LC 1312** — Minimum Insertion Steps to Make a String Palindrome
- **LC 730** — Count Different Palindromic Subsequences

---

### Generic interval-DP skeletons

The split-point form, with `cost(i, j)` left abstract:

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


The matrix-chain specialisation of the same skeleton:

#### **Interval DP Template**:
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

## 1-D Array Sizing & Loop Order Deep Dives

### Physical steps vs goals — LC 746


In problems involving movement (stairs, paths), the "goal" is one step **past** the last index.

**Example: LC 70 (Climbing Stairs) / LC 746 (Min Cost Climbing Stairs)**

If `cost = [10, 15, 20]` (indices 0, 1, 2):
- These are the steps you can stand on
- The "Floor" (goal) is at index **3**
- Therefore, `dp` array needs size `n + 1` to include the landing

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

#### Coin Change array sizing — LC 322 vs LC 518


**Key Insight**: For problems involving a **target value** (amount, sum, etc.), the DP array size must be `target + 1` to accommodate all values from `0` to `target` inclusive.

##### **Why `dp[amount + 1]`?**

- `dp[i]` represents the result for **amount `i`**
- To track all amounts from `0` to `amount`, we need indices `0, 1, 2, ..., amount`
- That's `amount + 1` total positions

##### **Concrete Example: `amount = 5`**

```text
We need to represent amounts: 0, 1, 2, 3, 4, 5
                         ↓    ↓   ↓   ↓   ↓   ↓
Array indices needed:    [0]  [1] [2] [3] [4] [5]

Therefore: dp array size = 6 = amount + 1
```

Java Code:
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

##### **LC 322 vs LC 518 Comparison**

| Aspect | LC 322: Coin Change | LC 518: Coin Change II |
|--------|---------------------|----------------------|
| **Goal** | Find **minimum coins** needed | Find **number of combinations** |
| **Return Type** | `int` (coin count or -1) | `int` (combination count) |
| **DP Definition** | `dp[i]` = min coins to make amount `i` | `dp[i]` = total combinations to make amount `i` |
| **DP Array Size** | `amount + 1` | `amount + 1` |
| **Base Case** | `dp[0] = 0` (0 coins for 0 amount) | `dp[0] = 1` (1 way: empty set) |
| **Loop Order** | `coin` → `amount` (both directions) | `coin` → `amount` (forward only) |
| **Transition** | `dp[i] = min(dp[i], dp[i - coin] + 1)` | `dp[i] += dp[i - coin]` |
| **Example** | `coins=[1,2,5], amount=5` → `2` (one 5) | `coins=[1,2,5], amount=5` → `4` (four ways) |

##### **Detailed Code Example: LC 518 (Coin Change II)**

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

**Trace Example**: `amount = 5, coins = [1, 2, 5]`
```text
Initial:        dp = [1, 0, 0, 0, 0, 0]
After coin 1:   dp = [1, 1, 1, 1, 1, 1]  (all amounts reachable with 1s)
After coin 2:   dp = [1, 1, 2, 2, 3, 3]  (add combinations with 2s)
After coin 5:   dp = [1, 1, 2, 2, 3, 4]  (add combination with 5)

Result: dp[5] = 4 combinations: {5}, {2+2+1}, {2+1+1+1}, {1+1+1+1+1}
```

##### **LC 322 Code Example (for comparison)**

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

#### **⭐⭐⭐⭐⭐ Loop Order: Combinations vs Permutations**

This is one of the most common interview traps in unbounded knapsack / coin change problems.

**The Rule:**

| Outer Loop | Inner Loop | Counts | Example |
|------------|-----------|--------|---------|
| `for coin in coins` | `for i in range(coin, amount+1)` | **Combinations** (order doesn't matter) | LC 518 |
| `for i in range(1, amount+1)` | `for coin in coins` | **Permutations** (order matters) | LC 377 |

**Why Coins-Outer → Combinations:**

When `coins` is the outer loop, each coin is processed completely before the next coin is introduced. This means `[1, 2]` and `[2, 1]` are never both counted — the coin-1 pass "happened first" globally, so `[2, 1]` has no way to appear as a separate path.

**Why Amount-Outer → Permutations:**

When `amount` is the outer loop, for each target `i` we ask *"what was the last coin placed?"* and try every coin. So reaching `i=3` via last-coin=2 (from `dp[1]` built with coin 1) is a separate path from last-coin=1 (from `dp[2]` built with coin 2). Every ordering is counted.

**Concrete Trace: `coins = [1, 2], amount = 3`**

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

**Code Side-by-Side:**

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

**Key Intuition — mental model:**

```text
Coins outer: "I decide to use coin-1 first, then optionally add coin-2 on top."
             → Sequence is forced: coin-1 always before coin-2 → no duplicates.

Amount outer: "To reach amount i, which coin did I place LAST?"
             → Each last-coin choice is a distinct path → all orderings counted.
```

**What about LC 322 (min coins)?**

LC 322 asks for the *minimum count*, not *how many ways* — so whether you count `[1,2]` and `[2,1]` separately doesn't matter; the minimum stays the same either way. Both loop orders are correct for LC 322.

**LeetCode Problem Map:**

| LC # | Problem | Loop Order | Reason |
|------|---------|-----------|--------|
| **518** | Coin Change II | coins outer | Count combinations |
| **377** | Combination Sum IV | amount outer | Count permutations |
| **322** | Coin Change | either | Minimize — order irrelevant |
| **39** | Combination Sum | backtracking | Combinations with arbitrary coins |
| **40** | Combination Sum II | backtracking | Combinations, each used once |

---

### Rewriting between the `n` and `n+1` styles


1. **Struggling with off-by-one errors?** Try the `n+1` approach
   - It allows index `i` to represent the i-th item
   - Keeps `dp[0]` as a "safe" dummy value for base case
   - Cleaner alignment between problem size and array index

2. **When to use which?**
   - Use `n+1` when: Problem describes "first i items", "i steps", or needs "empty" base case
   - Use `n` when: Direct element-to-index mapping makes more sense

3. **Rewriting between styles**:
   - `n` → `n+1`: Add 1 to array size, shift base cases, adjust `nums[i]` to `nums[i-1]`
   - `n+1` → `n`: Remove dummy index, handle base cases explicitly, use direct indexing

## Knapsack Deep Dives

### 2-D → 1-D derivation


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

### 2-D 0/1 knapsack (classic form)


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

### What knapsack.md covers


[**knapsack.md**](./knapsack.md) covers, in full:

- **0/1 Knapsack & Subset Sum deep dive** — the partition reduction, the `(total + target) / 2`
  transform for LC 494, boolean vs counting vs max-value variants, and the common pitfalls.
- **Combinations vs Permutations** — visual summary of the four core loop orders, code templates
  per pattern, and the pattern-selection decision tree.
- **Combinations vs Permutations vs 0/1 Knapsack** — side-by-side code, a step-by-step trace of the
  same input under both loop orders, and a when-to-use-which table.
- **`if (i - coin >= 0)` in Coin Change** — why the guard is `>=` and not `==`.

[**knapsack_01_zh.md**](./knapsack_01_zh.md) — 中文版，只聚焦 0/1 背包：state 定義、「拿或不拿」的轉移、
倒序 vs 正序的逐步 trace、max / min / count / boolean 四種變形，以及 LC 494 / 416 / 1049 / 474 的解法。

---

## Grid DP — The Other Three Implementations

### LC 64 variants


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

**Trade-off**: Modifies the input grid. Use when space is critical and mutation is acceptable.

#### **Approach 3: Space-Optimized 1D DP** (O(m) extra space)

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

**Key Insight**: `cur[i]` before update = cost of reaching `(i, j-1)` (from left). After update of `cur[i-1]` = cost of reaching `(i-1, j)` (from above). So `min(cur[i-1], cur[i])` is exactly `min(above, left)`.

#### **Approach 4: Top-Down Memoization** (Recursive)

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

**Key Insight**: Recurse from `(m-1, n-1)` down to `(0, 0)`. Base cases handle the first row/column (only one direction possible). Cache with `dp[m][n] != -1` guard.

#### **Approach Comparison** — LC 64

| Approach | Space | Modifies Input | Notes |
|----------|-------|----------------|-------|
| Top-Down Memo | O(m×n) | No | Natural recursive thinking |
| 2D DP | O(m×n) | No | Clearest iterative; easiest to reason about |
| In-place DP | O(1) | Yes ⚠️ | Best space, but destructive |
| 1D DP (1 row) | O(m) | No | Good balance of space and clarity |

### Similar grid problems


| Problem | LC # | Key Difference | Algorithm |
|---------|------|----------------|-----------|
| **Minimum Path Sum** | 64 | Sum along path, right/down only | 2D DP |
| **Unique Paths** | 62 | Count paths (not minimize sum) | 2D DP |
| **Unique Paths II** | 63 | With obstacles | 2D DP (skip obstacles) |
| **Dungeon Game** | 174 | Same grid shape, but solve bottom-right → top-left (need min HP) | 2D DP (reverse direction) |
| **Triangle** | 120 | Triangle shape, top→bottom | 1D DP (bottom-up) |
| **Minimum Falling Path Sum** | 931 | Can move diagonally ±1 | 2D DP |
| **Maximal Square** | 221 | Find largest square of 1s | 2D DP (`min` of 3 neighbors) |
| **Path With Min Effort** | 1631 | 4 directions, max-diff cost | Dijkstra |
| **Shortest Path in Grid with Obstacles** | 1293 | BFS with k obstacle eliminations | BFS + state |

### Visual trace and Python transcription — LC 64


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

**File Reference**: `leetcode_java/src/main/java/LeetCodeJava/DynamicProgramming/MinimumPathSum.java`

### Python rolling-row template

**Template for 2D DP with Space Optimization**:
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

## State Machine DP — Extended

### Sub-patterns by transaction constraint

**Sub-patterns:**
1. **2-State Machine** (Buy/Sell without cooldown)
   - States: `hold`, `cash`
   - Example: LC 122 (unlimited transactions)

2. **3-State Machine** (Buy/Sell with cooldown) ⭐
   - States: `hold`, `sold`, `rest`
   - Example: LC 309 (cooldown after sell)
   - Key: `rest` state prevents immediate buy after sell

3. **Multi-State Machine** (Limited transactions)
   - States: `buy1`, `sell1`, `buy2`, `sell2`, ...
   - Example: LC 123 (at most 2 transactions), LC 188 (at most k transactions)

### LC 309 state transition diagram


**State Transition Diagram for LC 309:**
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

**Key Insights:**
- **3 States vs 2 States**: Unlike simple stock problems (buy/sell), this needs 3 states due to cooldown
- **Cooldown Enforcement**: `rest` state ensures you can't buy immediately after selling
- **Space Optimization**: Can use O(1) space with 3 variables instead of 2D array
- **Critical Transition**: `hold = max(hold, rest - prices[i])` - can only buy after rest, not after sold

## String DP Deep Dives

### Edit Distance — pattern recognition and the three operations

#### 🎯 **Pattern Recognition** — LC 72

**When to use Edit Distance DP:**
- ✅ Converting one string to another with insert/delete/replace operations
- ✅ Finding minimum "edit distance" or "operations" between two strings
- ✅ String transformation problems (especially LeetCode medium/hard string problems)
- ✅ Two-string comparison problems where operations have costs

**Problem**: LC 72 - Edit Distance (aka Levenshtein Distance)

Given two strings `word1` and `word2`, find the **minimum number of operations** required to convert `word1` to `word2`.

Allowed operations (each counts as 1 step):
1. Insert a character
2. Delete a character
3. Replace a character

#### 💡 **Core DP Idea**

The key insight is: **When characters don't match, choose the operation that leads to the minimum cost.**

```text
At position (i, j):
  - If chars match: No cost, take solution from (i-1, j-1)
  - If they don't:
      Delete from word1:   dp[i-1][j] + 1
      Insert into word1:   dp[i][j-1] + 1
      Replace in word1:    dp[i-1][j-1] + 1
      → Take the minimum of these three
```

### Edit Distance implementation variants — LC 72


**Variant 1: Top-Down Memoization (Recursion + Cache)**
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

**Variant 2: Space-Optimized (O(n) Space)**
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

### Edit Distance visual table


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

#### Key insights — the three neighbours


1. **Three Operations Visualization**:
   ```text
   dp[i-1][j]      dp[i-1][j-1]
       ↓            ↘
   dp[i][j-1] →   dp[i][j]
   
   Delete (↓):    dp[i-1][j] + 1
   Replace (↘):   dp[i-1][j-1] + 1
   Insert (→):    dp[i][j-1] + 1
   ```

2. **Indexing Styles**:
   - **1-based indexing** (cleaner): Loop `i` from 1 to m, store in `dp[i][j]`
   - **0-based indexing** (also works): Loop `i` from 0 to m-1, store in `dp[i+1][j+1]`

3. **Complexity**:
   - **Time**: O(m × n)
   - **Space**: O(m × n) standard, O(min(m,n)) space-optimized

4. **Why we look at 3 neighbors**:
   - Characters **don't match** → pick cheapest operation
   - Characters **match** → no cost, inherit from diagonal
   - This greedy choice at each step leads to global optimum

#### **Pattern Recognition Checklist** ✅ — LC 72

Use this pattern when you see:
- "Minimum number of operations" + two strings → Edit Distance
- "Insert, Delete, Replace" operations → Edit Distance likely
- "Convert string A to string B" → Edit Distance
- "Levenshtein distance" or "edit distance" → Definitely this template
- String comparison where operations have equal cost (1)

#### **Common Mistakes** ⚠️

1. **Wrong indexing**: Forgetting that `dp[i][j]` uses `word1[i-1]` and `word2[j-1]`
   - ❌ `if (word1.charAt(i) == word2.charAt(j))`
   - ✅ `if (word1.charAt(i-1) == word2.charAt(j-1))`

2. **Incorrect base cases**: Not initializing the first row and column
   - Must set `dp[i][0] = i` and `dp[0][j] = j`

3. **Missing +1 for operations**: Forgetting to add 1 when characters don't match
   - ❌ `dp[i][j] = Math.min(...)`
   - ✅ `dp[i][j] = 1 + Math.min(...)`

4. **Wrong state definition**: Confusing which string index corresponds to which dimension
   - Be consistent: rows = word1, columns = word2

---

#### **Related String DP Problems** 📚

| LC # | Problem | Variant/Difference | Difficulty | Key Insight |
|------|---------|-------------------|------------|-------------|
| **72** | **Edit Distance** | Classic (Insert, Delete, Replace) | Medium | 3 operations, take min |
| **583** | Delete Operation for Two Strings | Only DELETE allowed | Medium | `dp[i][j] = dp[i-1][j] + 1` or `dp[i][j-1] + 1` |
| **712** | Minimum ASCII Delete Sum | Delete with ASCII cost | Medium | Track cost instead of count |
| **1143** | Longest Common Subsequence (LCS) | Maximize matches (opposite of edit) | Medium | Match: +1, Mismatch: max(left, top) |
| **1312** | Minimum Insertion Steps | Make string palindrome | Hard | Similar to LCS |
| **87** | Scramble String | Check if one string is scrambled version of another | Hard | 2D DP with partitioning |
| **115** | Distinct Subsequences | Count subsequences matching pattern | Hard | Counting variant |
| **44** | Wildcard Matching | Pattern matching with `?` and `*` | Hard | Extended string DP |
| **10** | Regular Expression Matching | DP pattern matching | Hard | Handle regex special chars |

#### **File References**:
- **Java Implementations**: `leetcode_java/src/main/java/LeetCodeJava/DynamicProgramming/EditDistance.java`
  - Multiple solution approaches (bottom-up, top-down, space-optimized)
  - Well-commented with detailed DP transition explanations
- **Related**: See the LCS template in [dp.md](./dp.md) for the comparison-maximization variant

### One Edit Distance — LC 161, the exactly-1-edit variant


#### 🎯 **Pattern Recognition** — LC 161

**This is NOT the same as Edit Distance (LC 72).**

| | LC 72 Edit Distance | LC 161 One Edit Distance |
|---|---|---|
| **Goal** | Find **minimum** edit count | Check if edit count is **exactly 1** |
| **Operations** | Insert, Delete, Replace | Same three |
| **Output** | Integer (min ops) | Boolean |
| **Approach** | Full 2D DP | 2-pointer OR 2D DP (check `== 1`) |
| **Time** | O(m × n) | O(n) two-pointer, O(m×n) DP |

#### 💡 **Core Idea — Two-Pointer (O(n), preferred)**

Instead of filling the whole DP table, scan left-to-right and at the **first mismatch**, apply the only possible repair and verify the suffix immediately:

```text
Three cases at first mismatch position i:
  len(s) == len(t) → Replace s[i]:  check s[i+1..] == t[i+1..]
  len(s) <  len(t) → Insert into s: check s[i..]   == t[i+1..]
  len(s) >  len(t) → Delete from s: check s[i+1..] == t[i..]
```

Post-loop (no mismatch): valid only if `len(t) == len(s) + 1`.

**Java (two-pointer):**
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

#### 💡 **Core Idea — DP (O(m×n), same table as LC 72)**

Run the full Edit Distance DP and return `dp[m][n] == 1`:

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

**Why the early `Math.abs > 1` guard matters**: strings that differ in length by more than 1 always have edit distance ≥ 2, so we skip the O(m×n) work entirely.

#### 📐 **Why These Three DP Transitions? (Intuition with Example)**

```text
| Operation | DP Cell Used  | Meaning                                      |
|-----------|---------------|----------------------------------------------|
| Insert    | dp[i][j-1]    | Already matched s[0..i] to t[0..j-1], then insert t[j] |
| Delete    | dp[i-1][j]    | Already matched s[0..i-1] to t[0..j], then delete s[i] |
| Replace   | dp[i-1][j-1]  | Already matched s[0..i-1] to t[0..j-1], then swap s[i]→t[j] |
```

**Concrete walkthrough: s = "ab", t = "acb"**

Build the table where `dp[i][j]` = min edits to convert `s[0..i-1]` → `t[0..j-1]`:

```text
       ""   a    c    b
  ""  [ 0][ 1][ 2][ 3]
  a   [ 1][ 0][ 1][ 2]
  b   [ 2][ 1][ 1][ 1]
```

Focus on `dp[2][3]` (convert "ab" → "acb"):

```text
s[1] = 'b',  t[2] = 'b'   → chars MATCH → dp[2][3] = dp[1][2] = 1  ✓
```

Now focus on `dp[1][2]` (convert "a" → "ac") where s[0]='a', t[1]='c' (NO match):

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

**Mental model for the three cells:**

```text
dp[i-1][j-1]  dp[i-1][j]
     ↘              ↓
dp[i][j-1]  →   dp[i][j]

  ↘ Replace      ↓ Delete (from s)
  → Insert (into s, advance t only)
```

- **`dp[i][j-1]` (left)**: t advanced one step but s didn't → we filled the gap with an **insert**
- **`dp[i-1][j]` (up)**: s advanced one step but t didn't → we removed a char from s (**delete**)
- **`dp[i-1][j-1]` (diagonal)**: both advanced → we **replaced** s[i] with t[j]

For LC 161 specifically: after filling the table, `dp[ns][nt] == 1` means exactly one of these three operations was needed.

#### **When to Use Which**

| Approach | When to Prefer |
|----------|----------------|
| Two-pointer (`substring.equals`) | Interview, O(n) time, simple to reason |
| Full DP | Already have LC 72 solution, want code reuse |

#### **Similar LC Problems**

| Problem | LC# | Relation |
|---------|-----|----------|
| Edit Distance | 72 | Generalized version (minimize ops) |
| One Edit Distance | 161 | Exactly 1 op — this pattern |
| Valid Palindrome II | 680 | At most 1 delete to form palindrome |
| Longest Common Subsequence | 1143 | Maximize matches instead of minimize ops |
| Delete Operation for Two Strings | 583 | Delete-only edit distance |

---

### What dp_string.md covers

[**dp_string.md**](./dp_string.md) covers, in full:

- **The two-string / two-sequence grid pattern** — full comparison table across LC 1143 / 72 / 115 /
  583 / 712 / 10 / 44.
- **Deep dive: prefix-based (1-indexed) indexing** — why the table is `dp[m+1][n+1]`, and the
  off-by-one bugs the 0-indexed version causes.
- **Interleaving String (LC 97)** — base cases, the 1-D space optimisation, and neighbours.
- **Valid Parenthesis String (LC 678)** — the wildcard state problem solved by DP, greedy range, and
  two-stack, side by side.

---

## Palindrome DP Variants

### Length-based outer loop — LC 647


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

### Manacher's algorithm — O(n)


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

### Similar palindrome problems


| LC # | Problem | Approach | Key Difference |
|------|---------|----------|----------------|
| **647** | Palindromic Substrings | DP or Expand | Count all palindromes |
| **5** | Longest Palindromic Substring | Expand or DP | Track max length instead of count |
| **516** | Longest Palindromic Subsequence | 2D DP (backward-i) | Subsequence (not substring), use `dp[i+1][j-1]+2` |
| **132** | Palindrome Partitioning II | DP + palindrome table | Min cuts; precompute `isPalin[i][j]` first |
| **131** | Palindrome Partitioning | Backtracking + palindrome table | All partition ways |
| **1312** | Min Insertions to Make Palindrome | 2D DP | `n - LPS(s)` |
| **680** | Valid Palindrome II | Two pointers | At most 1 delete |

---

## Rolling-Variable Deep Dive

### Candy-bar / Tribonacci walkthrough


**Pattern**: any recurrence that only looks back a **fixed** number of steps (`dp[i-1] … dp[i-k]`) needs only `k` variables, not an array. The whole difficulty moves out of the recurrence and into the *update block*: after computing `dp[i]`, the `k` variables must be shifted one slot so the next iteration sees the right window.

**Motivating problem** — *candy bar / Toblerone*: a bar is `n` pieces in a single row, you bite off 1, 2, or 3 pieces at a time. How many different ways can you eat the whole bar?

The last bite is 1, 2, or 3 pieces, and those cases are disjoint, so:

```text
dp[i] = dp[i-1] + dp[i-2] + dp[i-3]     # Tribonacci (same shape as LC 1137)

dp[0] = 1   # one way to eat nothing (the empty sequence of bites)
dp[1] = 1   # (1)
dp[2] = 2   # (1,1) (2)
dp[3] = 4   # (1,1,1) (1,2) (2,1) (3)
```

Only three previous values are ever read → three variables.

#### The update block

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

**Key Idea**: `p1, p2, p3` are not "three numbers I happen to need" — they are *named positions relative to `i`*. Write the invariant down before the loop:

```text
at the top of iteration i:   p1 = dp[i-3],  p2 = dp[i-2],  p3 = dp[i-1]
```

Every line of the update block exists to restore that invariant for `i+1`.

#### Visual trace (`n = 6`)

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

#### Update order: the classic bug ⭐⭐⭐⭐⭐

Assign **oldest → newest** (`p1` first, `p3` last). Going the other way overwrites a value you still need:

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

In Java there is no tuple assignment, so the oldest → newest order is the only option:

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

### Seeding and `k` steps back

> **Bonus — the seed-free variant.** Starting from `p1 = p2 = 0` and iterating over *every* element
> removes the `n == 1` / `n == 2` special cases entirely, because `dp[-1] = dp[-2] = 0` are honest
> seeds for this recurrence:
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
> Works because `max(0, 0 + nums[0]) = nums[0]` reproduces the seed the long version wrote by hand.
> Contrast with the Tribonacci seeding trap above: there, `dp[0] = 1` — the zero seed is *not*
> always the right one, so check it against the recurrence each time.

#### Seeding: the values must satisfy the recurrence

`dp[0]` is the usual trap. The *answer* for an empty bar is arguably `0`, but the *recurrence* needs `dp[0] = 1`, because `dp[3] = dp[2] + dp[1] + dp[0] = 2 + 1 + 1 = 4`. Use `0` only as an early-return for the caller, never as a seed:

```python
# python
if n == 0:
    return 0        # answer for the caller
p0 = 1              # seed for the recurrence — a different number on purpose
```

Rule of thumb: after seeding, hand-compute the **first** loop iteration and check it against a brute-force count. If `dp[4]` doesn't come out to `7`, the seeds are wrong, not the loop.

#### Generalizing to `k` steps back

For `dp[i] = sum(dp[i-1] … dp[i-k])`, named variables stop scaling. Two clean options:

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

The `i % k` trick removes the update block entirely — nothing is shifted, the slot for `dp[i]` simply reuses the slot of `dp[i-k]`, which is exactly the value that just fell out of the window. Same idea powers the 1D-rolling-array optimization in 2D DP (`dp[i % 2][j]`).

### Same update pattern, other problems


| Problem | Recurrence | Vars |
|---------|------------|------|
| LC 70 Climbing Stairs | `dp[i] = dp[i-1] + dp[i-2]` | 2 |
| LC 1137 N-th Tribonacci | `dp[i] = dp[i-1] + dp[i-2] + dp[i-3]` | 3 |
| Candy bar (bite 1/2/3) | `dp[i] = dp[i-1] + dp[i-2] + dp[i-3]` | 3 |
| LC 198 House Robber | `dp[i] = max(dp[i-1], dp[i-2] + nums[i])` | 2 |
| LC 746 Min Cost Climbing Stairs | `dp[i] = min(dp[i-1], dp[i-2]) + cost[i]` | 2 |
| LC 91 Decode Ways | `dp[i] = dp[i-1]·ok1 + dp[i-2]·ok2` | 2 |
| Bite any of `k` sizes | `dp[i] = sum(dp[i-c] for c in sizes)` | `max(sizes)` |

## Bitmask DP — TSP Template

### Shortest path visiting all nodes — LC 847

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

## Chaining DP — DFS + Memoization

### Longest String Chain — LC 1048


**Problem Type**: Finding the longest chain/sequence where each element is derived from the previous by a single structural operation

#### 🎯 Pattern

| Aspect | Detail |
|--------|--------|
| **Pattern** | Chaining / Sequence DP |
| **Core Operation** | Each element is exactly one step larger/different than the previous |
| **Two Approaches** | ① Bottom-up DP (sort + remove char) ② Top-down DFS + Memoization |
| **Key Data Structure** | `Map<String, Integer> dp` or `Map<String, Integer> memo` |
| **Time Complexity** | O(N × L²) where N = words, L = max word length |
| **Space Complexity** | O(N) for map |

#### 💡 Core Idea (LC 1048 - Longest String Chain)

> **Word A is a predecessor of Word B** if you can insert exactly one letter into A to get B.

**Two equivalent ways to think about it**:
- **Forward (DFS)**: From word `w`, look for all words of length+1 that are valid successors → recurse
- **Backward (Bottom-up DP)**: For word `w`, try removing each character → check if the shorter word is a known predecessor

**Key Insight — Backward approach is simpler**:
- Sort words by length (shortest first)
- For each word, remove one character at a time to generate all possible predecessors
- If a predecessor exists in the dp map, extend its chain
- This avoids the `isOneOff` comparison entirely

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

#### **Approach 1: Bottom-up DP** ⭐ (Recommended — simpler)

**State**: `dp[word]` = length of longest chain ending at `word`

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

**Why this works**: Sorting guarantees that when we process word `w`, all words shorter than `w` are already in `dp`. Removing one character generates all possible predecessors of length `|w|-1`.

#### **Approach 2: Top-down DFS + Memoization**

**State**: `memo[word]` = length of longest chain starting from `word`

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

#### **Approach Comparison** — LC 1048

| | Bottom-up DP (Approach 1) | Top-down DFS (Approach 2) |
|---|---|---|
| **Direction** | Backward: remove char to find predecessors | Forward: add char to find successors |
| **Sorting** | Required (shortest first) | Not required |
| **Helper needed** | No (substring removal is the check) | Yes (`isOneOff` two-pointer) |
| **Complexity** | O(N × L²) | O(N × L²) |
| **Simplicity** | Simpler ✅ | More verbose |

#### **Similar LeetCode Problems** 📚

| Problem | LC # | Chain Element | Operation | Pattern |
|---------|------|--------------|-----------|---------|
| **Longest String Chain** | 1048 | String | Insert 1 char | Sort + Remove char DP |
| **Longest Increasing Subsequence** | 300 | Number | Increase by any amount | Sort + 1D DP or patience sort |
| **Longest Consecutive Sequence** | 128 | Number | +1 exactly | HashSet lookup |
| **Word Ladder** | 127 | String | Change 1 char (not insert) | BFS (find shortest path) |
| **Longest Increasing Path in Matrix** | 329 | Grid cell | Move to larger neighbor | DFS + Memo on 2D grid |
| **Longest Path in Tree** | 2246 | Tree node | Parent-child edge | DFS on tree |
| **Concatenated Words** | 472 | String | One word is prefix of another | DP + word break |

**Key Distinctions**:
- LC 1048 vs LC 300: Both are "longest chain" but 1048 uses string structure; 300 uses numeric ordering
- LC 1048 vs LC 127: 1048 inserts a char (length changes); 127 replaces a char (length fixed) → BFS for shortest path
- LC 1048 vs LC 128: 1048 allows inserting anywhere; 128 requires consecutive integers

#### **Pattern Recognition Checklist** ✅ — LC 1048

Use this pattern when:
- ✅ Building chains where each element is exactly one operation away from the next
- ✅ Predecessor-successor relation is well-defined (insert char, +1 value, etc.)
- ✅ Need the longest such chain across all possible starting points
- ✅ Same element can appear in chains from multiple different predecessors → memoize

#### **Common Pitfalls** ⚠️

1. **Forgot to sort (Bottom-up DP)**: Must sort by length first so predecessors are in `dp` when successors are processed
2. **Using `contains` instead of `getOrDefault`**: Always use `dp.getOrDefault(prev, 0) + 1` — predecessor might not exist in list
3. **Generating all successors instead of predecessors (Bottom-up)**: It's simpler to remove chars (generate predecessors) than to insert chars (generate successors) — fewer strings to generate
4. **Validation complexity (Top-down DFS)**: Use two-pointer `isOneOff` in O(L) rather than brute-force O(L²) comparison

**File Reference**: `leetcode_java/src/main/java/LeetCodeJava/DynamicProgramming/LongestStringChain.java`

---

## Interval & Partition Scheduling DP

### Weighted Interval Scheduling — DP + Binary Search — LC 1235


> **Pattern**: items are *intervals* with a value; picking one forbids every interval that overlaps it. Sorting by **end time** turns "which items are still compatible?" into a **binary search** on a prefix of the DP array.

#### 🎯 Pattern Recognition — LC 1235

| Signal | Meaning |
|--------|---------|
| Input is `(start, end, value)` triples | Interval DP over *items*, not over ranges |
| "non-overlapping" / "cannot attend two at once" | Weighted interval scheduling |
| Values differ per interval | Greedy (activity selection) **fails** → must DP |
| n up to 5·10⁴ | O(n²) too slow → binary search the predecessor |

> ⚠️ Classic greedy "pick earliest finishing" only works when every interval is worth the same. With weights you must compare *take* vs *skip*.

#### 💡 Core Idea — LC 1235

```text
sort jobs by endTime
dp[i] = max profit using the first i jobs (sorted order)

take    : profit[i] + dp[p(i)]     where p(i) = # of jobs whose end <= start[i]
skip    : dp[i-1]
dp[i]   = max(take, skip)

p(i) is found by binary search over the (sorted) end times already in dp.
```

**Recurrence**: `dp[i] = max(dp[i-1], profit_i + dp[bisect_right(ends, start_i)])`

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

**Why `dp` stays sorted**: `dp[i] = max(dp[i-1], ...) >= dp[i-1]`, so the prefix maximum *is* `dp[i]` — no extra running-max needed.

#### Variation: extra "budget" dimension — LC 1751

> **Twist**: same sort + binary search, but you may attend **at most `k`** events → add a second state dimension `k`. Also note events are *inclusive day ranges*, so the predecessor must end **strictly before** the current start.

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

#### Similar LeetCode Problems 📚

| Problem | LC # | Twist |
|---------|------|-------|
| Maximum Profit in Job Scheduling | 1235 | Base pattern |
| Maximum Number of Events That Can Be Attended II | 1751 | + "at most k picks" dimension |
| Russian Doll Envelopes | 354 | Same "sort then DP on a prefix", but LIS instead of intervals |

---

### Partition Array into K Contiguous Groups — LC 1335


> **Pattern**: split an array into exactly `k` **contiguous** blocks and optimise a cost that is `sum / max` over blocks. Distinct from Interval DP (Template 3): here the split points are the decision, and the blocks must cover the array left to right.

#### 🎯 Pattern Recognition — LC 1335

- "divide into `d` days / `k` subarrays / `m` segments"
- Order of elements is fixed (no reordering, no skipping)
- Cost of a block is computable incrementally while scanning (`max`, prefix sum)

#### 💡 Core Idea — LC 1335

```text
dp[k][i] = best cost to cover jobs[i:] using exactly k blocks

dp[k][i] = min over j in [i, n-k] of ( cost(i..j) + dp[k-1][j+1] )
                                       ^ this block   ^ the rest

base: dp[0][n] = 0, dp[0][i<n] = INF   (0 blocks must consume 0 jobs)
answer: dp[d][0];  impossible when n < d
```

**Key trick**: extend `j` outward and maintain `cost(i..j)` in O(1) (running `max` here, running prefix sum for LC 410) — that keeps the transition O(n) instead of O(n²).

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

#### Variation: minimise the maximum block — LC 410

> **Twist**: identical partition skeleton, but the objective is `min over splits of (max block sum)` → the transition combines with `max` instead of `+`. (LC 410 also has the famous O(n log S) *binary-search-on-the-answer* solution; the DP below is what interviewers ask you to derive first.)

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

| Problem | LC # | Block cost | Combine |
|---------|------|-----------|---------|
| Minimum Difficulty of a Job Schedule | 1335 | `max` of block | `+` across blocks |
| Split Array Largest Sum | 410 | `sum` of block | `max` across blocks |

---

## State-Dimension DP

### An extra "last move" state dimension — LC 403


> **Pattern**: position alone is **not** a valid state — what you can do next depends on *how you got here*. Add the last transition to the state: `dp[position][lastMove]`. Whenever a naive `dp[i]` gives wrong answers because "the same cell is reachable in different ways with different futures", this is the fix.

#### 🎯 Pattern Recognition — LC 403

| Signal | Extra dimension to add |
|--------|------------------------|
| "next jump must be k-1, k or k+1" | last jump size |
| "cannot use the same direction twice" | last direction |
| "at most 2 in a row" | run length so far |
| "cooldown after selling" | last action (see the state machine template in [dp.md](./dp.md)) |

#### 💡 Core Idea (LC 403 Frog Jump)

```text
state  : (stone index i, jump size k that landed on i)
init   : (0, 0)
move   : from (i, k) you may jump k-1, k or k+1 (must be > 0)
         land on stone at position stones[i] + nk  ->  state (j, nk)
answer : any state on the last stone is reachable

dp[i] = set of jump sizes that can land ON stone i
```

Because jumps only move **forward** (`j > i`), a single left-to-right sweep is enough — no recursion needed.

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

#### Common Pitfalls ⚠️

- **Forgetting `nk > 0`** — a jump of 0 (or negative) would loop forever on the same stone.
- **Using `dp[i] = boolean`** — reachability alone loses the jump size and gives wrong answers (e.g. `[0,1,3,6,10,13,14]`).
- **Not deduplicating** — use a `Set` per stone, otherwise the state space blows up.
- Top-down `memo[(i, k)] -> boolean` with DFS is the equivalent formulation; same complexity.

---

### Step-Indexed Counting / Probability DP — LC 935


> **Pattern**: a **small state graph** (10 phone keys, an n×n board, a 1D array) plus a **fixed number of moves**. Answer = "how many ways / with what probability am I at each state after `t` steps". The DP layer is the step count, so you always roll one layer at a time.

#### 💡 Core Idea — LC 935

```text
dp[t][v] = ways (or probability) to be at state v after t steps
dp[t][v] = sum over u with edge u -> v of dp[t-1][u]

counting     -> take everything mod 1e9+7
probability  -> divide each contribution by the out-degree
```

Only `dp[t-1]` is needed → keep two arrays (`dp`, `ndp`) instead of a `steps × V` table.

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

#### Variation: probability instead of count — LC 688

> **Twist**: states are board cells, each move picks 1 of 8 directions uniformly, and moves off the board are *lost* — so the layer sums decay. Answer = sum of the final layer.

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

#### Variation: bound the reachable state space — LC 1269

> **Twist**: `arrLen` can be 10⁶ but with only `steps` moves you can never pass index `steps // 2` (you must walk back). Clamping the state space to `min(arrLen, steps // 2 + 1)` is what makes this pass.

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

#### Pattern Recognition Checklist ✅

- [ ] Fixed number of steps / rounds given as input (`n`, `k`, `steps`)
- [ ] State space is small and the transition graph is fixed
- [ ] Question asks "how many ways" (mod 1e9+7) or "with what probability"
- [ ] → roll one layer per step; O(1)-ish space in the state dimension

---

## Monotonic Queue & Stack DP

### DP with Monotonic Queue Optimization — LC 1425
When `dp[i] = max(dp[j]) + f(i)` for `j` in a sliding window `[i-k, i-1]`, use monotonic deque to reduce O(n²) → O(n).

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

### Largest Rectangle in Histogram — LC 84 (Stack DP)
Use monotonic stack to find the largest rectangle area in O(n).

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

## Pattern Selection — Extended Reference

### Decision flowchart

#### Decision Flowchart

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

### Keyword recognition index

#### Recognition Patterns by Keywords

**Linear sequence keywords** → Category 1 (Linear DP)
- "nth Fibonacci", "climbing stairs", "decode ways"
- "house robber", "non-adjacent", "skip adjacent"
- "longest increasing subsequence", "LIS"

**Grid/Matrix keywords** → Category 2 (Grid/2D DP)
- "grid", "matrix", "m x n"
- "unique paths", "number of ways"
- "minimum/maximum path sum"
- "maximal square", "largest rectangle"

**Interval/Subarray keywords** → Category 3 (Interval DP)
- "burst", "merge", "split", "partition"
- "optimal way to cut/divide"
- "minimum cost to merge"
- "palindrome partitioning"

**Tree keywords** → Category 4 (Tree DP)
- "binary tree", "tree structure"
- "each node", "children", "parent"
- "rob houses on tree", "cameras on tree"
- "for every node compute", "sum of distances" → Re-rooting DP (LC 834)

**State transition keywords** → Category 5 (State Machine DP)
- "buy and sell stock"
- "cooldown", "transaction fee"
- "at most k transactions"
- "multiple states"

**Selection with constraints** → Category 6 (Knapsack DP)
- "subset sum", "partition equal"
- "target sum", "combination sum"
- "0/1 knapsack", "unbounded knapsack"
- "coin change", "unlimited supply"

**String matching keywords** → Category 7 (String DP)
- "edit distance", "minimum operations"
- "longest common subsequence (LCS)"
- "palindrome subsequence/substring"
- "string transformation"

**Subset/Visited tracking** → Category 8 (State Compression DP)
- "visit all nodes", "shortest path visiting all"
- "assign tasks", "match workers"
- "traveling salesman problem (TSP)"
- "subset enumeration with constraints"

#### Quick Decision Examples

1. **"Find minimum path sum in a grid"**
   - Keywords: "grid", "minimum path sum"
   - Decision: Category 2 (Grid/2D DP)
   - Template: dp[i][j] = min(dp[i-1][j], dp[i][j-1]) + grid[i][j]

2. **"Count ways to make change with coins"**
   - Keywords: "coin change", "unlimited supply"
   - Decision: Category 6 (Unbounded Knapsack DP)
   - Template: dp[amount] = sum of dp[amount - coin]

3. **"Find edit distance between two strings"**
   - Keywords: "edit distance", "two strings"
   - Decision: Category 7 (String DP)
   - Template: dp[i][j] with insert/delete/replace operations

4. **"Buy and sell stock with cooldown"**
   - Keywords: "stock", "cooldown"
   - Decision: Category 5 (State Machine DP)
   - Template: 3 states (hold, sold, rest)

5. **"Shortest path visiting all nodes in graph"**
   - Keywords: "visit all nodes", "shortest path"
   - Decision: Category 8 (State Compression DP)
   - Template: dp[mask][node] with bitmask for visited nodes

6. **"Burst balloons to maximize coins"**
   - Keywords: "burst", "maximize"
   - Decision: Category 3 (Interval DP)
   - Template: dp[i][j] for interval [i, j]

7. **"Rob houses on a binary tree"**
   - Keywords: "tree", "rob", "non-adjacent"
   - Decision: Category 4 (Tree DP)
   - Template: Bottom-up DFS with two states per node

### Pro tips and pitfalls

#### Pro Tips for Pattern Selection

- **One sequence** → Linear DP (Category 1)
- **Two sequences** → Usually String DP (Category 7) or 2D DP
- **Grid movement** → Grid DP (Category 2)
- **Interval splitting** → Interval DP (Category 3) - often O(n³)
- **Tree traversal** → Tree DP (Category 4) - use DFS
- **Multiple states** → State Machine (Category 5) - draw state diagram first
- **Weight/capacity constraint** → Knapsack (Category 6)
- **String matching/transform** → String DP (Category 7)
- **Visit all/subset** → State Compression (Category 8) - use bitmask

#### Common Pitfalls

- **Interval DP**: Remember to iterate length from small to large
- **Knapsack**: 0/1 requires reverse iteration for space optimization
- **State Machine**: Draw state transition diagram before coding
- **Tree DP**: Use bottom-up DFS (postorder traversal); for "every node as root" problems, use Re-rooting DP (two-pass DFS, LC 834)
- **State Compression**: Check if n ≤ 20 (2^20 states is feasible)
- **String DP**: Define dp[i][j] carefully (length vs index)

---

### Common Recurrence Relations

#### **Sum/Count Patterns**
```python
# Fibonacci-like
dp[i] = dp[i-1] + dp[i-2]

# Include/exclude current
dp[i] = dp[i-1] + (dp[i-2] + nums[i])
```

#### **Min/Max Patterns**
```python
# Take or skip
dp[i] = max(dp[i-1], dp[i-2] + nums[i])

# Best from all previous
dp[i] = max(dp[j] + score(j, i) for j < i)
```

#### **Grid Patterns**
```python
# Path counting
dp[i][j] = dp[i-1][j] + dp[i][j-1]

# Min path
dp[i][j] = min(dp[i-1][j], dp[i][j-1]) + grid[i][j]
```

### Space Optimization Techniques

#### **Rolling Array**
```python
# From O(n²) to O(n)
# Instead of dp[i][j], use dp[2][j]
curr = [0] * n
prev = [0] * n
for i in range(m):
    curr, prev = prev, curr
    # Update curr based on prev
```

#### **State Compression**
```python
# From O(n) to O(1) for Fibonacci-like
prev2, prev1 = 0, 1
for i in range(2, n):
    curr = prev1 + prev2
    prev2, prev1 = prev1, curr
```

### State Machine DP Interview Pattern Recognition

**Quick Decision Tree:**
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

**State Machine Pattern Comparison:**

| Constraint Type | States | State Names | Buy Condition | Sell Condition | Example LC |
|----------------|--------|-------------|---------------|----------------|------------|
| None | 2 | hold, cash | `cash - price` | `hold + price` | 122 |
| **Cooldown** | **3** | **hold, sold, rest** | `rest - price` ⚠️ | `hold + price` | **309** |
| Transaction fee | 2 | hold, cash | `cash - price` | `hold + price - fee` | 714 |
| k transactions | 2k | buy1, sell1, ... | Track transaction # | Track transaction # | 123, 188 |

**⚠️ Critical Difference in Cooldown Pattern:**
- Regular: `hold = max(hold, cash - price)` - can buy anytime
- Cooldown: `hold = max(hold, rest - price)` - can only buy after rest!

**Pattern Recognition Cheat Sheet:**

| Problem Says... | Pattern | States | Key Transition |
|----------------|---------|--------|----------------|
| "Cooldown 1 day after sell" | 3-state | hold/sold/rest | Buy from `rest` only |
| "Transaction fee of k" | 2-state | hold/cash | `cash = hold + price - fee` |
| "At most 2 transactions" | 4-state | buy1/sell1/buy2/sell2 | Track transaction count |
| "At most k transactions" | 2k-state | Dynamic | Generalized k transactions |
| "Unlimited transactions" | 2-state | hold/cash | Simple buy/sell |

**Common Interview Follow-ups:**
1. "What if cooldown is k days?" → Need k+2 states
2. "What if both cooldown AND fee?" → 3 states + fee deduction
3. "Space optimize it" → Use variables instead of arrays
4. "Prove correctness" → Show state transitions enforce constraints

### State Machine DP Quick Reference
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
