# 數位 DP（在限制下數數字）

> **範圍** — 數出某個區間內有多少數字滿足「逐位」的性質：tight／started／位置這組狀態、通用的 top-down 模板，以及 `count(R) - count(L-1)` 這個區間技巧。
> **另見**：[dp.md](./dp.md) — 數位 DP 在 DP 家族裡的位置；[math.md](./math.md) — 不需要 DP、可以直接算閉式解的情況。

## LeetCode 題目清單

- [Dynamic Programming](https://leetcode.com/problem-list/dynamic-programming/)

## 總覽

### 關鍵性質

- **複雜度**：時間 `O(D * S * 10)`、空間 `O(D * S)` — `D` 是位數（64 位元的上界大約 18 位），
  `S` 是題目自帶的狀態數。
- **核心想法**：絕對不要走訪整個區間。改成**從左到右一位一位地把數字建出來**，
  並對 `(position, tight, started, extra)` 做記憶化。
- **什麼時候用**：「`[L, R]` 之間有幾個整數，它的**每一位**滿足某個性質」，而且 `R` 大到不可能用迴圈跑完。

### 三個通用狀態變數

| 狀態 | 意義 | 為什麼需要它 |
|-------|---------|---------------|
| `pos` | 現在填到第幾位 | 遞迴的索引 |
| `tight` | 前綴是否還緊貼上界的前綴？ | 是的話這一位最多只能填到 `bound[pos]`；不是的話 `0..9` 隨便填 |
| `started` | 是否已經放過非零的數字？ | 用來區分「真正的首位數字」和「補位的 0」 |

**區間技巧**：`answer(L, R) = count(R) - count(L - 1)`。

### 參考資料

- [dp.md](./dp.md) — 數位 DP 在 DP 家族裡的位置
- [math.md](./math.md) — 不需要 DP、可以直接算閉式解的情況

## 模板與演算法

### 核心概念

數位 DP 是用來數「`[L, R]` 之間有幾個數字滿足某種逐位限制」的技巧。它用 DP 一位一位把數字建出來，狀態要追蹤：
- 目前填到數字的哪一位
- 各種限制（是否緊貼上界、是否還在前導零階段等等）
- 題目自己的狀態（位數和、前一位是什麼等等）

**關鍵洞見：**
```text
Count(L, R) = Count(0, R) - Count(0, L-1)

Build numbers digit-by-digit left to right:
For each position, try all valid digits (0-9 or constrained by upper bound)
Use memoization to avoid recalculating same states
```

**常見應用：**
- 數出位數和等於 K 的數字
- 數出沒有連續重複位數的數字
- 數出每一位都相異的數字
- 數出符合特定位數樣式的數字

**時間複雜度**：O(digits × states × 10)，64 位元整數通常就是 O(18 × states × 10)
**空間複雜度**：記憶化用掉 O(digits × states)

---

#### 基本數位 DP 模板

**標準狀態變數：**
1. `pos`：目前的位數位置（0 是最左邊）
2. `tight`：目前這個數是否還被上界卡住
3. `started`：是否已經出現過非零的位數（用來處理前導零）
4. **題目自帶的狀態**：總和、計數、前一位等等

```python
# python
# IDEA: the universal (pos, tight, started, state) digit DP skeleton
# time = O(D * S * 10), space = O(D * S)
def count_numbers(n):
    """
    Count numbers from 0 to n satisfying certain constraints.

    Time: O(digits × states × 10)
    Space: O(digits × states)
    """
    if n < 0:
        return 0

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

> **顧好下界。** `count_range(0, R)` 會呼叫到 `count_numbers(-1)`，而 `str(-1)` 會讓拆位數的程式碼被 `'-'` 炸掉。所以這份文件裡每個 `count_numbers` 開頭都寫了 `if n < 0: return 0`。

---

#### 範例 1：LC 902 - Numbers At Most N Given Digit Set

**題目：** 給定數字集合 D，數出只用 D 裡的數字、且 ≤ N 的數有幾個。

```python
# python
# IDEA: only digits from the allowed set may be placed; `started` handles shorter numbers
# time = O(D * 2 * 2 * |digits|), space = O(D)
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
// java
// LC 902 - Numbers At Most N Given Digit Set
// IDEA: closed-form counting — all shorter lengths, then the same-length prefix walk
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

#### 範例 2：數出位數和等於 K 的數字

**題目：** 數出 `[1, N]` 中位數和恰好等於 K 的數字有幾個。

```python
# python
# IDEA: carry the running digit sum as the extra state
# time = O(D * k * 10), space = O(D * k)
# Count numbers with digit sum = K
def count_digit_sum_k(n, k):
    """
    Count numbers from 1 to n where digit sum = k.

    State: (pos, tight, sum)
    """
    if n < 0:
        return 0

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

#### 範例 3：LC 233 - Number of Digit One

**題目：** 數出 1 到 n 的所有整數中，數字 '1' 總共出現幾次。

```python
# python
# IDEA: count the 1s contributed at each position, not the numbers themselves
# time = O(D * D * 10), space = O(D * D)
# LC 233 - Number of Digit One
def countDigitOne(n):
    """
    Count occurrences of digit 1 in range [1, n].

    State: (pos, tight, count_of_ones)
    """
    if n < 0:
        return 0

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
// java
// LC 233 - Number of Digit One
// IDEA: per-position closed form — high/current/low decomposition
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

#### 範例 4：數出沒有連續相同位數的數字

```python
# python
# IDEA: previous digit is the extra state; `started` keeps padding zeros out of the rule
# time = O(D * 10 * 2 * 2), space = O(D * 10)
# Count numbers without consecutive same digits
def count_no_consecutive(n):
    """
    Count numbers from 1 to n with no two adjacent identical digits.

    State: (pos, tight, prev_digit)
    """
    if n < 0:
        return 0

    digits = [int(d) for d in str(n)]
    memo = {}

    # NOTE !!! `started` is not optional here. Without it, 1 is built as 0,0,1 under a
    #          3-digit bound and the two padding zeros trip the "same as previous" rule.
    def dp(pos, tight, started, prev_digit):
        if pos == len(digits):
            return 1 if started else 0      # `started` also drops the all-zeros number

        key = (pos, tight, started, prev_digit)
        if key in memo:
            return memo[key]

        limit = digits[pos] if tight else 9
        result = 0

        for digit in range(0, limit + 1):
            # Skip if same as previous digit — but only once the number has begun
            if started and digit == prev_digit:
                continue

            new_started = started or digit > 0
            result += dp(
                pos + 1,
                tight and (digit == limit),
                new_started,
                digit if new_started else -1  # padding zeros never become `prev_digit`
            )

        memo[key] = result
        return result

    return dp(0, True, False, -1)

# Example: count_no_consecutive(100) == 90
# Valid: 1..9, then 10, 12, 13, ..., 21, 23, 24, ..., 100 (exclude 11, 22, ...)
```

---

#### 經典 LeetCode 題目

| 題目 | LC# | Difficulty | 狀態變數 | 關鍵洞見 |
|---------|-----|------------|----------------|-------------|
| **Numbers At Most N Given Digit Set** | **902** | **Hard** | pos, tight | 數出合法的位數組合 |
| **Number of Digit One** | **233** | **Hard** | pos, tight, count | 數某個數字出現幾次 |
| Numbers With Repeated Digits | 1012 | Hard | pos, tight, mask | 用 bitmask 記錄用過哪些數字 |
| Count Special Integers | 2376 | Hard | pos, tight, mask | 每一位都相異 |
| Count Integers With Even Digit Sum | 2180 | Medium | pos, tight, sum | 位數和的奇偶性 |
| Count Numbers with Unique Digits | 357 | Medium | pos, mask | 排列組合計數 |

---

#### 圖解：一位一位把數字建出來

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

#### 面試提示

**1. 辨認模式：**
```text
"Count numbers in range with..."
"How many numbers from L to R satisfy..."
"Numbers where digits..."
→ Think Digit DP

Keywords: "digit sum", "consecutive digits", "distinct digits",
         "digit constraints", "count numbers"
```

**2. 常見的狀態變數：**
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

**3. 模板檢查清單：**
```python
# python
# IDEA: the shape to reproduce from memory in an interview
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

**4. 常犯的錯：**
- 忘了處理前導零（要用 `started` 旗標）
- `tight` 更新寫錯：應該是 `tight and (digit == limit)`
- 字串轉位數陣列轉錯
- 區間查詢差一：是 `count(R) - count(L-1)`

**5. 最佳化技巧：**
```python
# python
# IDEA: two small wins — prune dead states, and let lru_cache own the memo
# Pruning: Skip impossible states
for digit in range(0, limit + 1):
    if current_sum + digit > target:
        break  # Remaining digits can't help

# Use @lru_cache for cleaner code
from functools import lru_cache

@lru_cache(None)
def dp(pos, tight, state):
    ...        # the per-problem transition goes here
```

**6. 講給面試官聽的說法：**
- 「數位 DP 就是一位一位建出數字，再加上記憶化」
- 「tight 旗標用來追蹤我們是不是還被上界卡著」
- 「Count(L, R) = Count(0, R) - Count(0, L-1) 這個轉換」
- 「複雜度是 O(digits × states × 10)，非常快」

---

#### 進階：區間查詢的最佳化

> **這只是骨架，不是能跑的程式碼。** `check_valid` / `initial_state` 都是佔位用的，轉移那段留成 `pass` — 這裡只是要呈現「雙上下界數位 DP」長什麼樣子。實務上該寫的還是 `count(R) - count(L - 1)`；下面這種單趟寫法通常不值得多背這些狀態。

```text
# pseudocode — optimized range query
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

---

## 總結

```text
count_range(L, R) = count(R) - count(L - 1)        # and count(n) returns 0 for n < 0

count(n):
    digits = decimal digits of n, most significant first
    dfs(pos, tight, started, extra):
        pos == len(digits)  ->  1 if started else 0
        limit = digits[pos] if tight else 9
        sum over d in 0..limit of
            dfs(pos+1, tight and d == limit, started or d > 0, update(extra, d))
```

| 陷阱 | 症狀 | 修法 |
|------|------|------|
| 沒有 `started` 旗標 | 補位的 0 被當成真的位數（`1` 變成 `001`） | 把 `started` 一路傳下去，只有它為真時才套用位數規則 |
| 記憶化的 key 少了 `started` | 答案錯，而且會隨上界的長度而變 | key 要用 `(pos, tight, started, extra)` |
| `L == 0` 時算 `count(L - 1)` | `str(-1)` 解析到 `'-'` 直接爆掉 | 負的上界一律回傳 `0` |
| `tight` 為真時還做記憶化 | 少算 | 要嘛不要把 `tight` 狀態放進快取，要嘛把 `tight` 放進 key |
