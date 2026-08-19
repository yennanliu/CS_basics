# Bitmask DP (State Compression)

> **Scope** — DP where the state is a subset encoded in an integer: mask operations, submask enumeration, TSP and assignment templates, and the n <= 20 sizing rule.
> **See also**: [dp.md](./dp.md) — the short bitmask template and where it sits among the DP patterns; [bit_manipulation.md](./bit_manipulation.md) — the bit tricks themselves, without DP.

## LeetCode Problem Lists

- [Dynamic Programming](https://leetcode.com/problem-list/dynamic-programming/)

## Overview

### Key Properties

- **Complexity**: `O(2^n * n)` time, `O(2^n)` space for the standard subset DP — so `n <= 20` is the
  practical ceiling (`2^20` ≈ 1e6). Submask enumeration costs `O(3^n)`.
- **Core Idea**: the DP state is a **set**, and a set of `n` items is just an `n`-bit integer, so the
  whole memo table is a flat array indexed by that integer.
- **When to Use**: the problem tracks "which items have I already used / visited", `n` is small, and
  the naive answer is a permutation search.

### References

- [dp.md](./dp.md) — the one-screen bitmask template
- [bit_manipulation.md](./bit_manipulation.md) — the bit tricks on their own

## State Compression Patterns

**When to Use Bitmask DP**:
- Small state space (≤ 20 items)
- Need to track which items are selected/visited
- Permutation/combination problems
- Traveling salesman variants

**Common Bitmask Operations**:
```python
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

**Java Bitmask Operations**:
```java
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

### **Pattern 1: Visit All Nodes (TSP Variant)**

**Problem Type**: Find shortest path visiting all nodes exactly once

**State Definition**: `dp[mask][i]` = minimum cost to visit all nodes in `mask`, ending at node `i`

**Transition**: For each unvisited node `j`, try visiting it from current node `i`

**Time Complexity**: O(2^n × n²)
**Space Complexity**: O(2^n × n)

**Example**: LC 847 - Shortest Path Visiting All Nodes

```java
// Java Implementation
public int shortestPathLength(int[][] graph) {
    int n = graph.length;
    int[][] dp = new int[1 << n][n];
    Queue<int[]> queue = new LinkedList<>();

    // Initialize: start from any node
    for (int i = 0; i < n; i++) {
        Arrays.fill(dp[1 << i], Integer.MAX_VALUE);
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

### **Pattern 2: Assignment Problems**

**Problem Type**: Assign n tasks to n workers, minimize/maximize total cost

**State Definition**: `dp[mask]` = min/max cost to assign tasks in `mask` to first k workers (where k = number of bits set in mask)

**Transition**: For each worker, try assigning an unassigned task

**Time Complexity**: O(2^n × n)
**Space Complexity**: O(2^n)

**Example**: LC 1723 - Find Minimum Time to Finish All Jobs

```java
// Java Implementation
public int minimumTimeRequired(int[] jobs, int k) {
    int n = jobs.length;
    int[] dp = new int[1 << n];
    int[] subsetSum = new int[1 << n];

    // Precompute sum for each subset
    for (int mask = 0; mask < (1 << n); mask++) {
        for (int i = 0; i < n; i++) {
            if ((mask & (1 << i)) != 0) {
                subsetSum[mask] += jobs[i];
            }
        }
    }

    // dp[mask] = min time to finish jobs in mask
    Arrays.fill(dp, Integer.MAX_VALUE);
    dp[0] = 0;

    for (int mask = 0; mask < (1 << n); mask++) {
        // Try all submasks
        for (int submask = mask; submask > 0; submask = (submask - 1) & mask) {
            dp[mask] = Math.min(dp[mask],
                               Math.max(dp[mask ^ submask], subsetSum[submask]));
        }
    }

    return dp[(1 << n) - 1];
}
```

---

### **Pattern 3: Subset Selection with Constraints**

**Problem Type**: Select subsets satisfying specific constraints

**State Definition**: `dp[mask]` = number of ways / min cost to achieve state represented by `mask`

**Transition**: For each item, decide whether to include it based on current mask

**Time Complexity**: O(2^n × n) or O(3^n) for submask iteration
**Space Complexity**: O(2^n)

**Example**: LC 691 - Stickers to Spell Word

```java
// Java Implementation - Subset DP
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

### **Pattern 4: Partition into K Subsets**

**Problem Type**: Partition n items into k groups with constraints

**State Definition**: `dp[mask]` = true if items in `mask` can be partitioned into complete groups

**Transition**: Try forming complete groups from current state

**Time Complexity**: O(2^n × n)
**Space Complexity**: O(2^n)

**Example**: LC 698 - Partition to K Equal Sum Subsets

```java
// Java Implementation
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

### **Bitmask DP Common Patterns Summary**

| Pattern | State Definition | Transition | Example Problems |
|---------|-----------------|------------|------------------|
| **Visit All Nodes** | dp[mask][i] = cost to visit mask, end at i | Try next unvisited node | LC 847, LC 943 |
| **Assignment** | dp[mask] = cost to assign tasks in mask | Assign next task to worker | LC 1723, LC 1986 |
| **Subset Selection** | dp[mask] = ways/cost for subset mask | Include/exclude next item | LC 691, LC 1434 |
| **Partition** | dp[mask] = can partition mask into groups | Form complete groups | LC 698, LC 1681 |
| **Profile DP** | dp[i][mask] = state at row i with column mask | Process row by row | Tiling problems |

---

### **Advanced Techniques**

**1. Precomputing Subset Properties**:
```java
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

**2. Submask Enumeration - O(3^n)**:
```java
// For each mask, iterate through all its submasks
for (int mask = 0; mask < (1 << n); mask++) {
    for (int submask = mask; submask > 0; submask = (submask - 1) & mask) {
        // dp[mask] can be computed from dp[submask] and dp[mask ^ submask]
        dp[mask] = Math.min(dp[mask], dp[submask] + dp[mask ^ submask]);
    }
}
```

**3. SOS (Sum Over Subsets) DP - O(2^n × n)**:
```java
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

### **Complexity Analysis**

| Technique | Time Complexity | Space Complexity | Use Case |
|-----------|----------------|------------------|----------|
| **Basic Bitmask** | O(2^n × n) | O(2^n) | Visit all, assignment |
| **Submask Enumeration** | O(3^n) | O(2^n) | Partition, subset sum |
| **SOS DP** | O(2^n × n) | O(2^n) | Sum over subsets |
| **Profile DP** | O(2^m × n) | O(2^m) | Grid tiling (m = width) |

**Feasibility Limits**:
- n ≤ 15: Very safe, ~32K states
- n ≤ 20: Feasible, ~1M states
- n ≤ 24: Tight, ~16M states (watch TLE)
- n > 24: Usually too large for bitmask DP

---

### **Interview Tips**

1. **Recognize State Compression**:
   - Keywords: "visit all", "assign", "partition into k groups"
   - Constraints: n ≤ 20
   - Need to track subsets/visited items

2. **Choose Right State**:
   - TSP-style: `dp[mask][last_node]`
   - Assignment: `dp[mask]` (implicitly assign to worker k)
   - Partition: `dp[mask]` with modulo check

3. **Optimize**:
   - Precompute subset properties
   - Use BFS for shortest path problems
   - Consider SOS DP for subset sum queries

4. **Common Mistakes**:
   - Forgetting to initialize `dp[0]`
   - Wrong submask iteration: use `(submask - 1) & mask`
   - Not checking if bit is set before using it
   - Integer overflow with `1 << n` (use `1L << n` for n ≥ 31)
