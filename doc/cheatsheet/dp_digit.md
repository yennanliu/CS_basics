# Digit DP (Counting Numbers Under Constraints)

> **Scope** — Counting how many numbers in a range satisfy a digit-level property: the tight/started/position state, the universal top-down template, and the `count(R) - count(L-1)` range trick.
> **See also**: [dp.md](./dp.md) — where digit DP sits among the DP patterns; [math.md](./math.md) — closed-form counting when no DP is needed.

## LeetCode Problem Lists

- [Dynamic Programming](https://leetcode.com/problem-list/dynamic-programming/)

## Overview

### Key Properties

- **Complexity**: `O(D * S * 10)` time, `O(D * S)` space — `D` = number of digits (~18 for a 64-bit
  bound), `S` = the number of problem-specific states.
- **Core Idea**: never iterate the range. Build the number **digit by digit, left to right**, and
  memoise on `(position, tight, started, extra)`.
- **When to Use**: "how many integers in `[L, R]` have some property of their **digits**", where `R`
  is far too large to loop over.

### The three universal state variables

| State | Meaning | Why it exists |
|-------|---------|---------------|
| `pos` | which digit position is being filled | the recursion index |
| `tight` | is the prefix still equal to the bound's prefix? | if yes this digit is capped at `bound[pos]`; if no, `0..9` is free |
| `started` | has a non-zero digit been placed yet? | separates a genuine leading digit from padding zeros |

**Range trick**: `answer(L, R) = count(R) - count(L - 1)`.

### References

- [dp.md](./dp.md) — where digit DP sits among the DP patterns
- [math.md](./math.md) — closed-form counting when no DP is needed

## Core Concept

Digit DP is a technique for counting numbers in range [L, R] that satisfy certain digit-based constraints. It builds numbers digit-by-digit using DP with states tracking:
- Current position in number
- Constraints (tight bound, leading zeros, etc.)
- Problem-specific state (sum of digits, previous digit, etc.)

**Key Insight:**
```text
Count(L, R) = Count(0, R) - Count(0, L-1)

Build numbers digit-by-digit left to right:
For each position, try all valid digits (0-9 or constrained by upper bound)
Use memoization to avoid recalculating same states
```

**Common Use Cases:**
- Count numbers with digit sum = K
- Count numbers with no repeated consecutive digits
- Count numbers with all distinct digits
- Count numbers with specific digit patterns

**Time Complexity**: O(digits × states × 10) typically O(18 × states × 10) for 64-bit integers
**Space Complexity**: O(digits × states) for memoization

---

### Basic Digit DP Template

**Standard State Variables:**
1. `pos`: Current digit position (0 = leftmost)
2. `tight`: Whether current number is still bounded by upper limit
3. `started`: Whether non-zero digits have appeared (for handling leading zeros)
4. **Problem-specific state**: Sum, count, previous digit, etc.

```python
# Universal Digit DP Template
def count_numbers(n):
    """
    Count numbers from 0 to n satisfying certain constraints.

    Time: O(digits × states × 10)
    Space: O(digits × states)
    """
    digits = [int(d) for d in str(n)]
    memo = {}

    def dp(pos, tight, started, state):
        """
        pos: current position (0-indexed from left)
        tight: if True, current digit is bounded by digits[pos]
        started: if True, we've placed a non-zero digit
        state: problem-specific state (sum, count, etc.)
        """
        # Base case: processed all digits
        if pos == len(digits):
            return 1 if check_valid(state) else 0

        # Check memo
        if (pos, tight, started, state) in memo:
            return memo[(pos, tight, started, state)]

        # Determine max digit we can place
        limit = digits[pos] if tight else 9

        result = 0
        for digit in range(0, limit + 1):
            # Handle leading zeros
            new_started = started or (digit > 0)

            # Update problem-specific state
            new_state = update_state(state, digit, new_started)

            # Recursively count
            result += dp(
                pos + 1,
                tight and (digit == limit),
                new_started,
                new_state
            )

        memo[(pos, tight, started, state)] = result
        return result

    return dp(0, True, False, initial_state)

def count_range(L, R):
    """Count numbers in range [L, R]."""
    return count_numbers(R) - count_numbers(L - 1)
```

---

### Example 1: LC 902 - Numbers At Most N Given Digit Set

**Problem:** Given digit set D, count numbers ≤ N using only digits from D.

```python
# LC 902 - Numbers At Most N Given Digit Set
def atMostNGivenDigitSet(digits, n):
    """
    Count numbers using only digits from set, at most n.

    Time: O(log n × |D|) where D is digit set
    Space: O(log n)
    """
    str_n = str(n)
    n_digits = len(str_n)
    digit_set = set(digits)

    # Count numbers with fewer digits (always valid)
    count = sum(len(digits) ** i for i in range(1, n_digits))

    # DP for numbers with exactly n_digits digits
    @lru_cache(None)
    def dp(pos, tight):
        # Base case: formed complete number
        if pos == n_digits:
            return 1

        # Determine max digit
        limit = int(str_n[pos]) if tight else 9

        result = 0
        for d in digits:
            digit_val = int(d)
            if digit_val > limit:
                break  # Can't use this digit

            # Continue building
            result += dp(pos + 1, tight and (digit_val == limit))

        return result

    return count + dp(0, True)
```

```java
// Java - LC 902
/**
 * time = O(log N × |D|)
 * space = O(log N)
 */
class Solution {
    private String strN;
    private String[] digits;
    private Map<String, Integer> memo;

    public int atMostNGivenDigitSet(String[] digits, int n) {
        this.strN = String.valueOf(n);
        this.digits = digits;
        this.memo = new HashMap<>();

        int nDigits = strN.length();
        int count = 0;

        // Count numbers with fewer digits
        int base = digits.length;
        for (int i = 1; i < nDigits; i++) {
            count += Math.pow(base, i);
        }

        // Add numbers with exactly nDigits digits
        count += dp(0, true);

        return count;
    }

    private int dp(int pos, boolean tight) {
        if (pos == strN.length()) {
            return 1;
        }

        String key = pos + "," + tight;
        if (memo.containsKey(key)) {
            return memo.get(key);
        }

        int limit = tight ? strN.charAt(pos) - '0' : 9;
        int result = 0;

        for (String d : digits) {
            int digit = Integer.parseInt(d);
            if (digit > limit) break;

            result += dp(pos + 1, tight && (digit == limit));
        }

        memo.put(key, result);
        return result;
    }
}
```

---

### Example 2: Count Numbers with Digit Sum = K

**Problem:** Count numbers in [1, N] where sum of digits equals K.

```python
# Count numbers with digit sum = K
def count_digit_sum_k(n, k):
    """
    Count numbers from 1 to n where digit sum = k.

    State: (pos, tight, sum)
    """
    digits = [int(d) for d in str(n)]
    memo = {}

    def dp(pos, tight, current_sum):
        # Base case
        if pos == len(digits):
            return 1 if current_sum == k else 0

        # Memo check
        if (pos, tight, current_sum) in memo:
            return memo[(pos, tight, current_sum)]

        # Determine limit
        limit = digits[pos] if tight else 9

        result = 0
        for digit in range(0, limit + 1):
            # Pruning: skip if sum will exceed k
            if current_sum + digit > k:
                break

            result += dp(
                pos + 1,
                tight and (digit == limit),
                current_sum + digit
            )

        memo[(pos, tight, current_sum)] = result
        return result

    # Subtract 1 to exclude 0
    return dp(0, True, 0) - 1

# Example: count_digit_sum_k(100, 5)
# Numbers: 5, 14, 23, 32, 41, 50 → 6 numbers
```

---

### Example 3: LC 233 - Number of Digit One

**Problem:** Count total number of digit '1' appearing in all integers from 1 to n.

```python
# LC 233 - Number of Digit One
def countDigitOne(n):
    """
    Count occurrences of digit 1 in range [1, n].

    State: (pos, tight, count_of_ones)
    """
    digits = [int(d) for d in str(n)]
    memo = {}

    def dp(pos, tight, started, count_ones):
        if pos == len(digits):
            return count_ones

        if (pos, tight, started, count_ones) in memo:
            return memo[(pos, tight, started, count_ones)]

        limit = digits[pos] if tight else 9
        result = 0

        for digit in range(0, limit + 1):
            new_started = started or (digit > 0)

            # Count this digit if it's 1 and we've started
            new_count = count_ones + (1 if digit == 1 and new_started else 0)

            result += dp(
                pos + 1,
                tight and (digit == limit),
                new_started,
                new_count
            )

        memo[(pos, tight, started, count_ones)] = result
        return result

    return dp(0, True, False, 0)
```

```java
// Java - LC 233
/**
 * time = O(log N × log N)
 * space = O(log N × log N)
 */
class Solution {
    private int[] digits;
    private Map<String, Integer> memo;

    public int countDigitOne(int n) {
        String strN = String.valueOf(n);
        digits = new int[strN.length()];
        for (int i = 0; i < strN.length(); i++) {
            digits[i] = strN.charAt(i) - '0';
        }

        memo = new HashMap<>();
        return dp(0, true, false, 0);
    }

    private int dp(int pos, boolean tight, boolean started, int countOnes) {
        if (pos == digits.length) {
            return countOnes;
        }

        String key = pos + "," + tight + "," + started + "," + countOnes;
        if (memo.containsKey(key)) {
            return memo.get(key);
        }

        int limit = tight ? digits[pos] : 9;
        int result = 0;

        for (int digit = 0; digit <= limit; digit++) {
            boolean newStarted = started || (digit > 0);
            int newCount = countOnes + ((digit == 1 && newStarted) ? 1 : 0);

            result += dp(
                pos + 1,
                tight && (digit == limit),
                newStarted,
                newCount
            );
        }

        memo.put(key, result);
        return result;
    }
}
```

---

### Example 4: Count Numbers with No Consecutive Same Digits

```python
# Count numbers without consecutive same digits
def count_no_consecutive(n):
    """
    Count numbers from 1 to n with no two adjacent identical digits.

    State: (pos, tight, prev_digit)
    """
    digits = [int(d) for d in str(n)]
    memo = {}

    def dp(pos, tight, prev_digit):
        if pos == len(digits):
            return 1

        if (pos, tight, prev_digit) in memo:
            return memo[(pos, tight, prev_digit)]

        limit = digits[pos] if tight else 9
        result = 0

        for digit in range(0, limit + 1):
            # Skip if same as previous digit
            if digit == prev_digit:
                continue

            result += dp(
                pos + 1,
                tight and (digit == limit),
                digit
            )

        memo[(pos, tight, prev_digit)] = result
        return result

    return dp(0, True, -1) - 1  # -1 to exclude 0

# Example: count_no_consecutive(100)
# Valid: 10, 12, 13, 14, ..., 21, 23, 24, ... (exclude 11, 22, ...)
```

---

### Classic LeetCode Problems

| Problem | LC# | Difficulty | State Variables | Key Insight |
|---------|-----|------------|----------------|-------------|
| **Numbers At Most N Given Digit Set** | **902** | **Hard** | pos, tight | Count valid digit combinations |
| **Number of Digit One** | **233** | **Hard** | pos, tight, count | Count digit occurrences |
| Numbers With Repeated Digits | 1012 | Hard | pos, tight, mask | Track used digits with bitmask |
| Count Special Integers | 2376 | Hard | pos, tight, mask | All distinct digits |
| Count Integers With Even Digit Sum | 2180 | Medium | pos, tight, sum | Digit sum parity |
| Count Numbers with Unique Digits | 357 | Medium | pos, mask | Permutation counting |

---

### Visual Example: Building Numbers Digit-by-Digit

```text
Problem: Count numbers ≤ 523 with digit sum = 10

Digits of 523: [5, 2, 3]

Decision Tree (simplified):

Position 0: Can use 0-5
├─ Use 0: sum=0, tight=False → dp(1, False, 0)
│  ├─ Next positions have limit=9
│  └─ Count all with sum=10
│
├─ Use 1: sum=1, tight=False → dp(1, False, 1)
│  └─ Count numbers 1XX with digit sum = 10
│
├─ Use 2: sum=2, tight=False → dp(1, False, 2)
│  └─ Count numbers 2XX with digit sum = 10
│
├─ Use 3: sum=3, tight=False → dp(1, False, 3)
│  └─ Count numbers 3XX with digit sum = 10
│
├─ Use 4: sum=4, tight=False → dp(1, False, 4)
│  └─ Count numbers 4XX with digit sum = 10
│
└─ Use 5: sum=5, tight=True → dp(1, True, 5)
   ├─ Position 1: Can use 0-2 (tight bound)
   │  ├─ Use 0: sum=5, tight=False
   │  ├─ Use 1: sum=6, tight=False
   │  └─ Use 2: sum=7, tight=True
   │     └─ Position 2: Can use 0-3 (tight)
   │        ├─ Use 3: sum=10 ✓ (523 included!)
   │        └─ ...

Valid numbers: 109, 118, 127, ..., 505, 514, 523
```

---

### Interview Tips

**1. Recognition Patterns:**
```text
"Count numbers in range with..."
"How many numbers from L to R satisfy..."
"Numbers where digits..."
→ Think Digit DP

Keywords: "digit sum", "consecutive digits", "distinct digits",
         "digit constraints", "count numbers"
```

**2. Common State Variables:**
```text
Always needed:
- pos: current digit position
- tight: bounded by upper limit

Often needed:
- started: handle leading zeros
- prev_digit: for consecutive/adjacent constraints
- sum: for digit sum problems
- mask: for tracking which digits used (bitmask)
```

**3. Template Checklist:**
```python
def digit_dp(n):
    digits = [int(d) for d in str(n)]
    memo = {}

    def dp(pos, tight, started, state):
        # 1. Base case
        if pos == len(digits):
            return check_condition(state)

        # 2. Memoization
        if (pos, tight, started, state) in memo:
            return memo[(pos, tight, started, state)]

        # 3. Determine limit
        limit = digits[pos] if tight else 9

        # 4. Try all valid digits
        result = 0
        for digit in range(0, limit + 1):
            new_started = started or (digit > 0)
            new_state = update_state(state, digit, new_started)

            result += dp(
                pos + 1,
                tight and (digit == limit),
                new_started,
                new_state
            )

        # 5. Save and return
        memo[(pos, tight, started, state)] = result
        return result

    return dp(0, True, False, initial_state)
```

**4. Common Mistakes:**
- Forgetting to handle leading zeros (use `started` flag)
- Wrong tight update: should be `tight and (digit == limit)`
- Not converting string to digit array correctly
- Off-by-one in range queries: `count(R) - count(L-1)`

**5. Optimization Tips:**
```python
# Pruning: Skip impossible states
for digit in range(0, limit + 1):
    if current_sum + digit > target:
        break  # Remaining digits can't help

# Use @lru_cache for cleaner code
from functools import lru_cache

@lru_cache(None)
def dp(pos, tight, state):
    # ...
```

**6. Talking Points:**
- "Digit DP builds numbers digit-by-digit with memoization"
- "tight flag tracks whether we're bounded by upper limit"
- "Count(L, R) = Count(0, R) - Count(0, L-1) transformation"
- "Complexity is O(digits × states × 10), very efficient"

---

### Advanced: Range Query Optimization

```python
# Optimized range query
def count_range_optimized(L, R):
    """
    Handle range queries efficiently.

    Instead of count(R) - count(L-1), we can process both ends
    simultaneously to avoid redundant computation.
    """
    def count(n, lower_bound=None):
        digits = [int(d) for d in str(n)]
        memo = {}

        def dp(pos, tight_upper, tight_lower, state):
            # tight_upper: bounded by n
            # tight_lower: bounded by lower_bound (if exists)
            if pos == len(digits):
                return check_valid(state)

            # ... implementation with both bounds
            pass

        return dp(0, True, lower_bound is not None, initial_state)

    # Single call handles both L and R
    return count(R, L)
```
