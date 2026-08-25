# Binary Search on the Answer

> **Scope** — Binary searching a *range of candidate answers* against a monotone feasibility predicate — the `canFinish` / `isValid` framing, the minimise-maximum vs maximise-minimum decision, the `[max(nums), sum(nums)]` boundary recipe, and counting predicates over a value domain.
> **See also** — *parent sheet*: [binary_search.md](./binary_search.md) — loop invariants, the boundary (lower/upper bound) templates, rotated arrays and 2D search; [binary_search_examples.md](./binary_search_examples.md) — the worked-problem archive for the index-space templates.
> *Neighbouring sheets*: [greedy.md](./greedy.md) — the greedy scan most of these predicates are built from; [bfs.md](./bfs.md) — the traversal used as a predicate in LC 1631 / LC 778; [heap.md](./heap.md) — the k-th-element alternative to value-domain counting.

## LeetCode Problem Lists

- [Binary Search](https://leetcode.com/problem-list/binary-search/)
- [Binary Search on Answer (tag)](https://leetcode.com/tag/binary-search/)

## Overview
**Critical Pattern** - One of the most important and frequently tested binary search applications in FAANG interviews.

### Concept

Instead of searching for a value IN an array, we binary search on a **range of possible answers** and use a validation function to check feasibility.

**When to Use:**
- "Find minimum/maximum value that satisfies..."
- "What's the smallest/largest X such that..."
- "Can we achieve X? What's the optimal X?"
- Problem has **monotonic** property: if X works, then X+1 (or X-1) also works

**Key Recognition Keywords:**
- "Minimize the maximum..."
- "Maximize the minimum..."
- "Find the smallest capacity/speed/divisor..."
- "Can you split/allocate/distribute..."

**Common Problem Patterns:**
- LC 410: Split Array Largest Sum
- LC 1011: Capacity To Ship Packages Within D Days
- LC 875: Koko Eating Bananas
- LC 1283: Find the Smallest Divisor
- LC 1482: Minimum Number of Days to Make m Bouquets
- LC 2226: Maximum Candies Allocated to K Children

---

## Problem Categories

**Pattern 1: Minimize Maximum**
- Goal: Find smallest X where some maximum value ≤ X
- Update: `if valid: right = mid` (try smaller)
- Examples: LC 410, 1011, 1482, **2616**
- Key: Sort first (if applicable) + greedy validation
- **Why BS works**: Monotonic property — larger X is always easier to satisfy

**Pattern 2: Maximize Minimum**
- Goal: Find largest X where some minimum value ≥ X
- Update: `if valid: left = mid + 1` (try larger), use `mid = (l + r + 1) / 2`
- Examples: **LC 1231** (Divide Chocolate), LC 1552, LC 2064, LC 2226
- Key: Greedy validation — can we make enough pieces/groups with each ≥ target?
- **Why BS works**: Monotonic property — smaller X is always easier to satisfy
- **Counterpart to LC 410**: LC 1231 is "maximize min sweetness" vs LC 410 "minimize max sum"

**Pattern 3: Count-Based Validation**
- Check: "Can we do it in at most K groups/days/operations?"
- Greedy approach: Try to fit as much as possible in each group
- Examples: LC 410 (subarrays), LC 1011 (days)

**Pattern 4: Sum-Based Validation**
- Check: "Is the sum/total within bounds?"
- Accumulate values and check threshold
- Examples: LC 1283 (division sum), LC 875 (hours)

---

## Templates & Algorithms

### Monotonic Predicate — the Conceptual Foundation
The real power of binary search: if you can define a predicate `P(x)` such that all `x` satisfying `P` form a contiguous range, binary search finds the boundary in O(log n).

```text
P(x) = False, False, ..., False, True, True, ..., True
                                 ^
                          find this boundary
```

Examples of monotonic predicates:
- `canFinish(speed)` — can Koko eat all bananas in H hours at speed ≥ k? (LC 875)
- `canShip(capacity)` — can we ship all packages in D days with capacity ≥ c? (LC 1011)
- `isEnough(mid)` — can we find k pairs with sum ≤ mid? (LC 719)

**Template for "find minimum x satisfying P":**
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

**Template for "find maximum x satisfying P":**
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

### The Unified Template

**Structure:**
1. **Define search space**: [min_possible, max_possible]
2. **Binary search** on this range
3. **Validation function**: Check if current value satisfies constraints
4. **Update boundaries** based on minimization/maximization goal

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

### Decision Matrix: Minimize vs Maximize ⭐⭐⭐⭐⭐

| Goal | Example Problems | Valid Condition | Update Rule | Final Answer |
|------|------------------|----------------|-------------|--------------|
| **Minimize maximum** | LC 410, 1011, 2616 | If mid works | `right = mid` (try smaller) | `left` (smallest valid) |
| **Maximize minimum** | LC 1231, 1552, 2226 | If mid works | `left = mid + 1` (try larger) | `left - 1` or `ans` variable |

**Mnemonic:**
- **Minimize**: When valid, go **left** (smaller values) → Find smallest working value
- **Maximize**: When valid, go **right** (larger values) → Find largest working value

**Critical Template Difference:**

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

> **Why `+1` in the Maximize template?**
> `// +1 avoids infinite loop: when left+1==right, (left+right)/2 == left, so right never moves`
> Without `+1`, when `left` and `right` are adjacent (`left + 1 == right`), `mid` computes to `left`.
> If `isValid(mid)` is true, we set `left = mid = left` — no progress, infinite loop.
> Adding `+1` biases `mid` upward so `mid == right`, guaranteeing the loop always shrinks.

---

### Search Boundary Pattern: `left = max(nums)`, `right = sum(nums)` ⭐⭐⭐⭐⭐

This is the **canonical search space setup** for "Binary Search on Answer" problems
that ask you to split/partition/allocate elements from an array.

#### Why `left = max(nums)`?

Any valid answer (max subarray sum, capacity, speed, etc.) must be **at least** as
large as the biggest single element — because that element must appear in *some*
group by itself in the worst case.

```text
nums = [7, 2, 5, 10, 8]
             ^  ^^
        max = 10  ← no matter how you split, some subarray contains 10 alone
                     → answer cannot be smaller than 10
left = max(nums) = 10
```

#### Why `right = sum(nums)`?

If you put **all** elements in one group, the sum is `sum(nums)`. That always works —
it's the trivially valid upper bound. The answer can never exceed this.

```text
nums = [7, 2, 5, 10, 8]  →  sum = 32
If k=1: one subarray containing everything, max sum = 32 ✓
right = sum(nums) = 32
```

#### Visual: The Answer Lives Inside `[max, sum]`

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

#### The Code Pattern (reusable across problems)

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

#### Similar Problems Using the Same `[max, sum]` Boundary

| LC # | Problem | What's minimized | `left` | `right` |
|------|---------|-----------------|--------|---------|
| **410** | Split Array Largest Sum | max subarray sum | `max(nums)` | `sum(nums)` |
| **1011** | Capacity To Ship Packages | ship capacity | `max(weights)` | `sum(weights)` |
| **1482** | Min Days to Make m Bouquets | days | `1` | `max(bloomDay)` |
| **875** | Koko Eating Bananas | eating speed | `1` | `max(piles)` |
| **1283** | Find the Smallest Divisor | divisor | `1` | `max(nums)` |
| **2064** | Minimized Maximum of Products Distributed | max per store | `1` | `max(quantities)` |

> **Tip:** Whenever the problem says "split/ship/distribute array elements into K groups,
> minimize the maximum", reach for `left = max(nums)`, `right = sum(nums)`.

---

### Why Binary Search Works for "Minimize the Maximum" ⭐⭐⭐⭐⭐

This is the **theoretical foundation** that makes binary search applicable to optimization problems.

#### The Monotonic Property (Key Insight)

Binary search requires a **monotonic** (sorted) property. For "Minimize the Maximum" problems, this property exists in the **feasibility function**:

```text
If we can achieve the goal with maximum value = X,
then we can ALWAYS achieve it with maximum value = X + 1 (or any larger value).
```

This creates a **monotonic feasibility curve**:

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

#### Why This Enables Binary Search

**Standard binary search** finds a target in a sorted array.
**Binary search on answer** finds the boundary in a sorted feasibility function.

| Concept | Standard Binary Search | Binary Search on Answer |
|---------|----------------------|------------------------|
| **Search space** | Sorted array of values | Range of possible answers |
| **Monotonic property** | Values are sorted | Feasibility is monotonic |
| **Goal** | Find exact target | Find boundary (first ✓) |
| **Check** | `nums[mid] == target?` | `isValid(mid)?` |

#### Mathematical Proof

**Theorem:** If `isValid(x)` has the monotonic property:
- `isValid(x) = true` ⟹ `isValid(x + 1) = true`

Then binary search correctly finds the minimum valid `x`.

**Proof:**
1. The answer space `[left, right]` can be partitioned into:
   - `[left, answer-1]`: all invalid
   - `[answer, right]`: all valid
2. Binary search finds this partition point in O(log n) time
3. Each iteration halves the search space while preserving the invariant

#### Visual Example — the Feasibility Curve

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

#### Why Not Just Iterate? (Complexity Analysis)

| Approach | Time Complexity | Explanation |
|----------|----------------|-------------|
| **Linear search** | O(range × n) | Check every possible answer |
| **Binary search** | O(log(range) × n) | Halve search space each time |

For LC 2616: range = 10⁹, n = 10⁵
- Linear: 10⁹ × 10⁵ = 10¹⁴ operations ❌ TLE
- Binary: log(10⁹) × 10⁵ ≈ 30 × 10⁵ = 3×10⁶ operations ✓

#### The Three Requirements for Binary Search on Answer

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

#### Common "Minimize Maximum" Problem Structure

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

### Template Variations

**Variation 1: Closed Interval [left, right]**
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

**Variation 2: Half-Open Interval [left, right)**
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

## LC Examples

### Split Array Largest Sum — LC 410 ⭐⭐⭐⭐⭐

**Problem:** Split array into m subarrays, minimize the largest sum among subarrays.

**Insight:** Binary search on possible "largest sum" values. For each mid, check if we can split array into ≤ m subarrays with each sum ≤ mid.

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

**Step-by-Step Trace:** `nums = [7,2,5,10,8], k = 2`

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

**Problem:** Ship packages within D days. Find minimum capacity needed.

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

**Approach**: Binary search on capacity + greedy validation
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
**Problem:** Koko must eat all bananas within h hours. Find minimum eating speed.

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

> Binary search on the eating speed; check if all bananas can be eaten in H hours.

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
**Problem:** Find smallest divisor such that sum(ceil(num/divisor)) ≤ threshold.

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
**Pattern**: `while (l < r - 1)` - Complex validation with helper function
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

> Binary search on the number of days; check if m bouquets of k adjacent flowers can be made.

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

### Divide Chocolate — LC 1231 (Maximize Minimum) ⭐⭐⭐⭐⭐

**Problem:** Divide chocolate bar into K+1 pieces (sharing with K friends). You eat the piece with minimum sweetness. Maximize that minimum sweetness.

**Key Insight:** This is a **"Maximize Minimum"** problem - the counterpart to "Minimize Maximum":
1. **Binary search** on the "minimum sweetness" you can get
2. **Greedy validation**: Can we cut into ≥ K+1 pieces where each piece has sweetness ≥ mid?
3. If valid → try larger minimum (go right)
4. If invalid → need smaller target (go left)

**Why Greedy Works:**
- Greedily accumulate sweetness until reaching target, then cut
- This maximizes the number of valid pieces for a given target
- Monotonic property: if minSweetness X works, X-1 also works (easier to split)

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

**Alternative Template (while l < r):**

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

**Step-by-Step Trace:** `sweetness = [1,2,3,4,5,6,7,8,9], k = 5`

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

**Similar Problems (Maximize Minimum Pattern):**

| Problem | Description | Validation Logic |
|---------|-------------|------------------|
| **LC 1231** | Divide Chocolate | Can split into K+1 pieces with each ≥ target |
| LC 1552 | Magnetic Force | Can place m balls with min distance ≥ target |
| LC 2226 | Maximum Candies | Can give k children candies with each ≥ target |
| LC 2064 | Minimized Maximum Products | Distribute products to stores |

---

### Minimize the Maximum Difference of Pairs — LC 2616 ⭐⭐⭐⭐⭐

**Problem:** Find p pairs of indices such that the maximum difference amongst all pairs is minimized. Each index can only be used once.

**Key Insight:** This is a classic "Minimize the Maximum" problem:
1. **Sort** the array so closest numbers are adjacent
2. **Binary search** on the "maximum difference" value
3. **Greedy check**: Can we find ≥ p pairs where each pair's diff ≤ mid?

**Why Greedy Works (but PQ Doesn't):**
- After sorting, adjacent pairs give minimum differences
- Greedy pairing rule: `if (nums[i+1] - nums[i] <= maxDiff) → take pair, skip i+1`
- This guarantees **maximum number of pairs** for that maxDiff
- PQ approach fails because it's a **matching optimization problem** where local greedy selection doesn't guarantee optimality
- Binary search has **monotonic property**: if maxDiff X works, any larger diff also works

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

**Step-by-Step Trace:** `nums = [10,1,2,7,1,3], p = 2`

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

**Why PQ Approach Fails - Counterexample:**

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

### Path With Minimum Effort — LC 1631, a Graph Predicate ⭐⭐⭐⭐

> Same "minimize the maximum" skeleton as the unified template above, but the feasibility check is a **BFS/DFS reachability test** instead of an `O(n)` scan.

`canReach(limit)` = "can I walk from top-left to bottom-right using only steps whose cost `<= limit`?" — monotone in `limit` (a bigger limit only unlocks more edges), which is exactly what binary search needs.

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

**Sibling problems (identical skeleton, different edge rule)**

| LC | Problem | `canReach(limit)` means |
|----|---------|-------------------------|
| **1631** | Path With Minimum Effort | every step's absolute height difference is `<= limit` |
| 778 | Swim in Rising Water | every visited **cell value** is `<= limit` (the time `t`) |

> Alternative for both: **Dijkstra with a min-max ("bottleneck") relaxation** `nd = max(d, cost)`, or **union-find** processing cells in sorted order. Binary search + BFS is the easiest to derive under time pressure; say the Dijkstra variant out loud as the `O(mn log mn)` improvement.

---

### Kth Smallest Element in a Sorted Matrix — LC 378 ⭐⭐⭐⭐⭐

> Given an `n x n` matrix where each **row and column** is sorted ascending, return the `k`-th smallest element. Requires memory better than `O(n²)` (so we can't just flatten & sort).

#### 1. Core Idea

**Binary search on the VALUE RANGE, not on indices.**

Because rows/columns are sorted, the answer lies in `[matrix[0][0], matrix[n-1][n-1]]`. We binary search over this **value space** and, for each candidate `mid`, count how many matrix elements are `<= mid`.

- The function `count(mid) = #elements <= mid` is **monotonically non-decreasing** in `mid` → this monotonicity is what enables binary search.
- We want the **smallest value** `x` such that `count(x) >= k`. That `x` is guaranteed to be an actual element in the matrix (the k-th smallest).

```text
if count(mid) < k  → answer is bigger  → left  = mid + 1
else               → mid might be it   → right = mid   (keep left half, include mid)
```

Loop `while left < right`, converge to a single value, return `left`.

#### 2. Pattern

**"Binary search on answer + count check"** — same family as Koko (LC 875), Split Array (LC 410).
The twist: the **count step** exploits the sorted-matrix structure to run in `O(n)` (staircase walk) instead of `O(n²)`.

**Count `<= target` in O(n) — staircase from bottom-left:**

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

**Full solution:**

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

**Key points to remember:**
- Search space is **values** (`matrix[0][0] .. matrix[n-1][n-1]`), not indices.
- Use `while left < right` + `right = mid` (left-boundary style) so we converge on the first value with `count >= k`.
- `left` always ends on a real matrix element — no need to snap it back.
- Alternative count: per-row `bisect_right` gives `O(n log n)`; the staircase is `O(n)`.
- Alternative overall approach: **min-heap** of `(val, r, c)`, pop `k` times → `O(k log n)` time, `O(n)` space (worse memory, simpler to reason about).

#### 3. Similar LC Problems

| LC | Problem | Relation |
|----|---------|----------|
| **378** | Kth Smallest Element in a Sorted Matrix | This problem — BS on value + count |
| **668** | Kth Smallest Number in Multiplication Table | BS on value; `count(x)=Σ min(x//i, n)` |
| **719** | Find K-th Smallest Pair Distance | BS on distance value + two-pointer count |
| **786** | K-th Smallest Prime Fraction | BS on fraction value + count |
| **373** | Find K Pairs with Smallest Sums | Heap variant (same k-smallest idea) |
| **240** | Search a 2D Matrix II | Same staircase walk (search, not count) |
| **875** | Koko Eating Bananas | Same "BS on answer + count/feasibility" template |
| **410** | Split Array Largest Sum | Same "BS on answer" template |
| **4**   | Median of Two Sorted Arrays | k-th smallest via binary search partition |

### Find the Duplicate Number — LC 287, Searching a Value Domain ⭐⭐⭐⭐⭐

> `n + 1` integers, each in `[1, n]`. Exactly one number repeats. Find it **without modifying the array** and in `O(1)` extra space.

#### Core Idea — the index space is useless, the value space is not

The array is **not sorted**, so binary searching indices is meaningless. But the **values** live in a known range `[1, n]`, and the counting function

```text
count(v) = #{ x in nums : x <= v }
```

is **monotonically non-decreasing** in `v`. By pigeonhole:

```text
count(v) >  v   ->  a duplicate lives in [1, v]     -> hi = v
count(v) <= v   ->  the duplicate is above v        -> lo = v + 1
```

Converge with the standard "first `True`" template → `lo` is the duplicate.

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

**Interview notes**
- Mention the **`O(n)` Floyd cycle-detection** alternative (treat `i -> nums[i]` as a linked list, find the cycle entrance) — the binary search is the one interviewers accept when you can't recall Floyd, and it is easier to prove.
- Same skeleton as the LC 378 section above (kth smallest in a sorted matrix) and LC 719 — only the `count()` implementation changes. See also `matrix.md` for the matrix-flavoured version.

**Variation — derived monotone predicate (LC 1539 Kth Missing Positive Number)**
The twist: instead of counting values, derive a monotone quantity **from the index**. In a strictly increasing positive array, the number of missing positives before index `i` is `arr[i] - (i + 1)` — non-decreasing in `i`. Find the first index where it reaches `k`:

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

## Problems by Pattern

| Problem | Difficulty | Pattern | Key Insight |
|---------|------------|---------|-------------|
| LC 69 | Easy | Integer square root | Search on [0, x] |
| LC 875 | Medium | Minimize speed | Eating bananas, greedy validation |
| LC 1011 | Medium | Minimize capacity | Ship packages, similar to LC 410 |
| **LC 1231** | **Hard** | **Maximize minimum** | **Divide chocolate, greedy cut when sum ≥ target** |
| LC 1283 | Medium | Minimize divisor | Ceiling division, sum constraint |
| LC 1482 | Medium | Minimize days | Make bouquets, range validation |
| LC 1552 | Medium | Maximize minimum | Magnetic force, aggressive cows |
| LC 2226 | Medium | Maximize candies | Per-child allocation |
| **LC 2616** | **Medium** | **Minimize max diff** | **Sort + greedy pairing, skip used** |
| LC 410 | Hard | Minimize maximum | Split array, subarray sum |
| LC 2064 | Medium | Minimize max | Distribute products to stores |

**Practice Progression:**
1. Start with LC 875 (clearest example of minimize pattern)
2. Then LC 1011 (similar to 410 but easier)
3. Master LC 410 (classic minimize maximum, frequently asked)
4. Try LC 1231 (maximize minimum - counterpart to LC 410)
5. Try LC 2616 (minimize max with pairing constraint)
6. Explore LC 1283, 1482 (variations)
7. Challenge: LC 1552, 2064, 2226 (maximize minimum pattern)

---

## Pattern Selection Strategy

**How to Recognize:**
1. Problem asks for "minimum/maximum" value
2. You can easily check "is X valid?" but not "what is the optimal X?"
3. Answer has monotonic property (if X works, X+1 or X-1 also works)

**Common Mistakes:**
1. **Wrong search space bounds**
   - Too narrow: Missing the optimal answer
   - Solution: Carefully analyze minimum (e.g., max element) and maximum (e.g., sum)

2. **Off-by-one in validation**
   ```java
   // ❌ WRONG: Using < instead of <=
   if (currentSum + num < maxSum) {...}

   // ✅ CORRECT: Must allow equality
   if (currentSum + num <= maxSum) {...}
   ```

3. **Wrong boundary update**
   - Minimize: `right = mid` (not `mid - 1`)
   - Maximize: `left = mid + 1`

4. **Inefficient validation**
   - Add early termination in validation function
   - Use greedy approach for O(n) validation

**Talking Points:**
- "This is a binary search on answer space problem"
- "I'll binary search on [min, max] and use a helper to validate"
- "The answer has monotonic property: if X works, larger X also works"
- "Time complexity: O(n × log(range)) where n is validation cost"

---

## Summary

- **Three preconditions** — a bounded answer range, a *monotone* feasibility predicate, and a validation you can run in `O(n)` / `O(n log n)`. If any one is missing, this is not the pattern.
- **Two shapes only** — minimise the maximum (`if valid: right = mid`, return `left`) and maximise the minimum (`if valid: left = mid`, `mid = left + (right - left + 1) / 2`, return `left`). Everything else is the predicate.
- **Boundary recipe** — "split / ship / distribute array elements into K groups and minimise the maximum" ⇒ `left = max(nums)`, `right = sum(nums)`. "Rate / divisor / days" ⇒ `left = 1`, `right = max(nums)`.
- **Same skeleton, different predicate** — a greedy scan (LC 410, 1011, 875, 1482), a count over a value domain (LC 378, 287, 1539), or a graph walk (LC 1631, LC 778).
- Say it out loud in the interview: *"the answer is monotone in feasibility, so I'll binary search the answer space and write `isValid` as the helper."*
