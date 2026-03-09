# Kadane's Algorithm

- **Core idea**: Find maximum sum/product of contiguous subarray in O(n) time using dynamic programming
- **When to use it**: Maximum subarray sum, optimization problems on arrays, product variations
- **Key LeetCode problems**: LC 53, LC 152, LC 918, LC 1186, LC 121, LC 134, LC 122
- **Data structures**: Array, variables to track current/global max
- **Typical states**: Current maximum ending here vs global maximum so far

**Time Complexity:** O(n) - single pass
**Space Complexity:** O(1) - only need few variables

## 0) Concept

### 0-0) Core Principle

Kadane's algorithm is an elegant method for calculating the maximum sum subarray ending at a given position in an array, all in a single pass.

**Key Decision at Each Step:**
- `current_max = max(nums[i], current_max + nums[i])`
  - `nums[i]`: Start new subarray from current element
  - `current_max + nums[i]`: Extend existing subarray

**Important Insight:**
- At each position, decide: "Should I extend the current subarray or start fresh?"
- Current sum < 0 means starting fresh is better
- Global maximum tracks the best subarray seen so far

### 0-1) Types

1. **Maximum Subarray Sum** - Classic Kadane's algorithm (LC 53)
2. **Maximum Product Subarray** - Modified for multiplication (LC 152)
3. **Circular Array Maximum** - Handle wraparound cases (LC 918)
4. **Maximum with At Most One Deletion** - Allow removing one element (LC 1186)
5. **2D Kadane's** - Extended to matrices for maximum rectangle sum
6. **Stock Trading** - Maximum difference/profit subarray (LC 121)

### 0-2) Algorithm Pattern / Template

**Basic Kadane's Algorithm (Sum):**

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

**Important Edge Cases:**
- All negative numbers → return the maximum single element
- Empty array → handle based on problem requirements
- Single element → return that element

---

## 1) Pattern-Specific Implementations

### 1-1) Classic Maximum Subarray Sum (LC 53)

**Problem:** Find the contiguous subarray with the largest sum.

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

### 1-2) Maximum Subarray Sum with Index Tracking

**Variant:** Return not just the sum, but also start/end indices of the maximum subarray.

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

### 1-3) Maximum Product Subarray (LC 152)

**Key Insight:** Track both maximum and minimum products because negative × negative = positive.

**Algorithm:**
- Each step considers 3 choices:
  1. Start new subarray at i → just take nums[i]
  2. Extend previous max product → nums[i] × maxProd
  3. Extend previous min product → nums[i] × minProd

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

**Step-by-Step Example:** `nums = [2, 3, -2, 4]`

```
Index | nums[i] | maxProd              | minProd              | result
----------------------------------------------------------------------
  0   |    2    |     2                |     2                |   2
  1   |    3    | max(3,6,6)=6        | min(3,6,6)=3         |   6
  2   |   -2    | max(-2,-12,-6)=-2   | min(-2,-12,-6)=-12   |   6
  3   |    4    | max(4,-8,-48)=4     | min(4,-8,-48)=-48    |   6
```

**Final Answer:** 6 (subarray [2,3] has product 6)

---

### 1-4) Circular Maximum Subarray (LC 918)

**Key Insight:** Maximum can occur in two scenarios:
1. **Normal**: Maximum subarray doesn't wrap around
2. **Circular**: Maximum subarray wraps around edges

**Circular Maximum = Total Sum - Minimum Subarray**

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

**Why it works:**
- If max subarray wraps around, removing the middle part (minimum subarray) leaves the maximum
- `Total Sum - Min Subarray = Max Circular Subarray`

---

### 1-5) Maximum Subarray Sum with One Deletion (LC 1186)

**Problem:** You can delete at most one element to maximize subarray sum.

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

## 2) Related LeetCode Problems

### Kadane's Algorithm Direct Applications

| Problem | Difficulty | Pattern | Key Insight |
|---------|------------|---------|-------------|
| LC 53 | Easy | Maximum Subarray | Classic Kadane's |
| LC 152 | Medium | Maximum Product | Track max & min |
| LC 918 | Medium | Circular Subarray | Total - minimum |
| LC 1186 | Medium | With One Deletion | Two states DP |
| LC 121 | Easy | Stock Trading | Max difference |
| LC 122 | Medium | Stock Trading II | Sum all gains |
| LC 134 | Medium | Gas Station | Circular + greedy |
| LC 1191 | Medium | K-Concatenation | Repeated arrays |

### Related Patterns

- **LC 325** - Maximum Size Subarray Sum Equals k (Prefix Sum + HashMap)
- **LC 560** - Subarray Sum Equals K (Prefix Sum + HashMap)
- **LC 862** - Shortest Subarray with Sum at Least K (Deque + Prefix Sum)
- **LC 1004** - Max Consecutive Ones III (Sliding Window)

---

## 3) Common Mistakes & Edge Cases

### 🚫 Common Mistakes

1. **Forgetting Edge Cases**
   ```java
   // ❌ WRONG: Not handling empty arrays
   if (nums.length == 0) return 0;

   // ✅ CORRECT: Check for null and empty
   if (nums == null || nums.length == 0) return 0;
   ```

2. **All Negative Arrays**
   ```python
   # ❌ WRONG: Returning 0 for all negative
   if max_sum < 0:
       return 0

   # ✅ CORRECT: Return maximum element
   # Kadane's handles this naturally by initializing with nums[0]
   ```

3. **Product Problems: Not Tracking Minimum**
   ```java
   // ❌ WRONG: Only tracking max product
   maxProd = Math.max(nums[i], maxProd * nums[i]);

   // ✅ CORRECT: Track both max and min
   maxProd = Math.max(nums[i], Math.max(maxProd * nums[i], minProd * nums[i]));
   ```

4. **Index Tracking: Off-by-One Errors**
   ```python
   # ❌ WRONG: Starting loop from 0 when initialized with nums[0]
   for i in range(len(nums)):  # Recounts nums[0]

   # ✅ CORRECT: Start from 1
   for i in range(1, len(nums)):
   ```

5. **Circular Arrays: Missing Edge Case**
   ```java
   // ❌ WRONG: Not checking if all numbers are negative
   return Math.max(maxSum, totalSum - minSum);

   // ✅ CORRECT: Handle case where minSum = totalSum
   if (totalSum == minSum) return maxSum;
   return Math.max(maxSum, totalSum - minSum);
   ```

### ⚠️ Edge Cases

1. **Empty Array**: Return 0 or throw exception
2. **Single Element**: Return that element
3. **All Negative**: Return maximum element (least negative)
4. **All Positive**: Return sum of entire array
5. **Contains Zero**: Resets product calculations
6. **Circular All Negative**: Standard Kadane's result only

---

## 4) Interview Tips & Complexity Analysis

### 💡 Interview Strategy

#### Recognition Patterns:
- "Maximum sum subarray" → Classic Kadane's
- "Maximum product subarray" → Modified Kadane's with min tracking
- "Circular array" → Consider normal + circular cases
- "With deletion/skip" → Track multiple states
- "Stock profit" → Kadane's variant

#### Problem-Solving Framework:

```
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

### 📊 Complexity Analysis

| Variant | Time | Space | Key Operation |
|---------|------|-------|---------------|
| Standard Kadane's | O(n) | O(1) | max(cur, cur+next) |
| Product Kadane's | O(n) | O(1) | Track max & min |
| Circular | O(n) | O(1) | Two passes |
| With Deletion | O(n) | O(1) | Two state DP |
| 2D Kadane's | O(n²m) | O(m) | Row compression |

### 🎯 Interview Talking Points

1. **Why Kadane's Works:**
   - "At each position, we choose whether to extend the current subarray or start fresh"
   - "Negative sum means starting fresh is better"
   - "This is optimal because we consider all possibilities in O(n) time"

2. **Product Variant Intuition:**
   - "Need to track both max and min because negative × negative = positive"
   - "A small negative number can become large when multiplied by another negative"
   - "This handles the non-linear behavior of multiplication"

3. **Circular Array Strategy:**
   - "Maximum is either in the middle (normal) or wraps around (circular)"
   - "Circular maximum = Total sum - Minimum subarray"
   - "We compute both and return the maximum"

4. **Space Optimization:**
   - "Don't need DP array, just track two variables: current_max and global_max"
   - "This reduces space from O(n) to O(1)"

### 🔧 Optimization Techniques

1. **Reset Strategy**: When current_sum < 0, reset to current element
2. **Space Optimization**: Only O(1) space needed, no DP array
3. **Early Termination**: If all positive, return sum of entire array
4. **Product Optimizations**:
   - Track zeros separately for special handling
   - Count negative numbers for parity checking

### 📚 Related Patterns

- **Sliding Window**: Fixed-size subarray maximum
- **Prefix Sum**: Range sum queries
- **Dynamic Programming**: General subarray optimization
- **Divide and Conquer**: Alternative O(n log n) approach for max subarray

---

## 5) References

- [Wikipedia: Maximum Subarray Problem](https://zh.wikipedia.org/zh-tw/%E6%9C%80%E5%A4%A7%E5%AD%90%E6%95%B0%E5%88%97%E9%97%AE%E9%A2%98)
- [Flydean: Kadane's Algorithm](https://www.flydean.com/interview/arithmetic/arithmetic-Kadane/)
- [LeetCode: Dynamic Programming Patterns](https://leetcode.com/discuss/study-guide/1308617/Dynamic-Programming-Patterns)

---

## Summary

**Core Principles:**
- ✅ Single pass O(n) algorithm for subarray optimization
- ✅ Key decision: extend current subarray OR start fresh
- ✅ Track current_max (local) and global_max
- ✅ For products: track both max and min

**When to Use:**
- Maximum/minimum sum/product of contiguous subarray
- Stock trading profit maximization
- Optimization problems with contiguous elements
- Problems requiring O(n) time with O(1) space

**Key Variants:**
1. **Sum** - Classic Kadane's
2. **Product** - Track max and min
3. **Circular** - Consider wraparound
4. **With Deletion** - Multiple state DP

**Interview Focus:**
- Understand the "extend vs start fresh" decision
- Handle edge cases (all negative, empty, single element)
- Know product variant requires min tracking
- Master circular array approach (total - minimum)

## LC Examples

### 2-1) Maximum Subarray (LC 53) — Kadane's Algorithm
> Track local max ending at each index; reset when local max drops below current element.

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

### 2-2) Maximum Product Subarray (LC 152) — Track Min & Max
> Track both max and min product (min can become max when multiplied by negative).

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

### 2-3) Maximum Sum Circular Subarray (LC 918) — Kadane + Total Sum Trick
> Max circular subarray = max(normal max subarray, total sum - min subarray).

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

### 2-4) Best Time to Buy and Sell Stock (LC 121) — Kadane Variant
> Track running minimum price; max profit = current price − running minimum.

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

### 2-5) Maximum Subarray Sum with One Deletion (LC 1186)
> dp0[i] = max sum ending at i (no deletion); dp1[i] = max sum with one deletion used.

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

### 2-6) Longest Turbulent Subarray (LC 978) — Kadane Variant
> Track lengths of increasing and decreasing alternating windows; reset on equality.

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

### 2-7) Gas Station (LC 134) — Greedy / Kadane on Circular
> If total gas >= total cost, a solution exists; start from the first surplus reset point.

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

### 2-8) Maximum Length of Subarray with Positive Product (LC 1567)
> Track lengths with positive and negative product separately; swap on negative number.

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

### 2-9) Maximum Score of Spliced Array (LC 2321) — Kadane on Difference
> Max gain from swapping subarray = max subarray sum of (nums2[i] - nums1[i]).

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

### 2-10) K-Concatenation Maximum Sum (LC 1191) — Kadane + Math
> For k >= 2: answer = maxSubarray(2 copies) + max(0, totalSum) × (k − 2).

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

### 2-11) Find Maximum Sum of Almost Unique Subarray (LC 2841) — Sliding Window Kadane
> Fixed-size window of length k; count distinct elements using HashMap; maximize sum.

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