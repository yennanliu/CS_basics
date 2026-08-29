# 組合數學與數學模式

> **範圍** — 面試會考到的計數與數論：模運算、GCD／LCM、篩法、nCr、蓄水池抽樣，以及基本幾何。
> **另見**：[math.md](./math.md) — 一般的數值操作；[dp.md](./dp.md) — 用遞推而非公式來計數；[bit_manipulation.md](./bit_manipulation.md) — 位元層級的數論。

## LeetCode 題目清單

- [Combinatorics](https://leetcode.com/problem-list/combinatorics/)
- [Math](https://leetcode.com/problem-list/math/)
- [Number Theory](https://leetcode.com/problem-list/number-theory/)

## 總覽
Google 面試很常考數學／組合推理，比其他 FAANG 都多。這份文件涵蓋在程式面試中常見的數論、計數、幾何與機率模式。

### 關鍵性質
- **什麼時候用**：題目牽涉到排列組合計數、模運算、GCD／LCM、質數，或幾何計算
- **Google 想看的訊號**：你能不能推出公式，而不是硬爆搜？

## 模式 1：模運算

### 基本規則
```text
(a + b) % m = ((a % m) + (b % m)) % m
(a * b) % m = ((a % m) * (b % m)) % m
(a - b) % m = ((a % m) - (b % m) + m) % m   ← add m to avoid negative

Division: (a / b) % m = (a * b^(-1)) % m  where b^(-1) = modular inverse
```

### 模冪運算（快速冪） — LC 50
```java
// Time: O(log exp), Space: O(1)
long modPow(long base, long exp, long mod) {
    long result = 1;
    base %= mod;
    while (exp > 0) {
        if ((exp & 1) == 1) result = result * base % mod;
        exp >>= 1;
        base = base * base % mod;
    }
    return result;
}
```

```python
# Python has built-in: pow(base, exp, mod)
```

### 模反元素（模數為質數時）
```text
b^(-1) mod p = b^(p-2) mod p    (Fermat's little theorem)
```

**經典 LC：** LC 1808（Maximize Number of Nice Divisors）、LC 372（Super Pow）

### 變形：前綴和模 K — LC 523
*轉折：把 `(a - b) % m` 反過來用 — 兩個餘數相同的前綴和，中間夾出來的子陣列就能被 `k` 整除。*

```java
// java
// LC 523 - Continuous Subarray Sum
// IDEA: sum(i..j) % k == 0  <=>  prefix[j] % k == prefix[i-1] % k
//       so store the FIRST index of each remainder and check the gap is >= 2.
// time = O(N), space = O(min(N, K))
public boolean checkSubarraySum(int[] nums, int k) {
    Map<Integer, Integer> firstIdx = new HashMap<>();
    firstIdx.put(0, -1);                     // empty prefix
    int run = 0;
    for (int i = 0; i < nums.length; i++) {
        run = (run + nums[i]) % k;
        Integer j = firstIdx.get(run);
        if (j != null) {
            if (i - j >= 2) return true;     // need length >= 2
        } else {
            firstIdx.put(run, i);            // keep the EARLIEST index only
        }
    }
    return false;
}
```

```python
# python
# LC 523 - Continuous Subarray Sum
# IDEA: sum(i..j) % k == 0  <=>  prefix[j] % k == prefix[i-1] % k
#       so store the FIRST index of each remainder and check the gap is >= 2.
# time = O(N), space = O(min(N, K))
def checkSubarraySum(nums, k):
    first = {0: -1}
    run = 0
    for i, v in enumerate(nums):
        run = (run + v) % k
        if run in first:
            if i - first[run] >= 2:
                return True
        else:
            first[run] = i
    return False
```

**鴿籠原理的推論**：餘數只有 `k` 種，所以任意 `k+1` 個前綴必定重複一次 — 只要 `N >= k`，就一定存在某個**非空**子陣列可被 `k` 整除。但重複可能出現在*相鄰*的兩個前綴，也就是長度 1，而 LC 523 不接受（`[1, 0]`、`k = 2` 滿足 `N >= k`，卻沒有合法答案）。要保證長度 `>= 2`，需要 `N >= 2k` — 只對偶數索引的前綴 `p0, p2, p4, ...` 套鴿籠原理，這樣任何重複本身就已經相距 `>= 2`。（`2k` 是緊的 — `N = 2k - 1` 不夠：`[0, 1, 0]`、`k = 2`。）

### 變形：鴿籠原理 → 有限狀態空間必定進入循環 — LC 957
*轉折：把同一套鴿籠論證從餘數換成**整個狀態** — 只要狀態空間有限、轉移函數是確定性的，這條路徑就一定會循環，於是可以把巨大的 `n` 對週期取模。*

**做法（任何「把 f() 套用 n 次，n 大到 1e9」的題目都能重用）：**
```text
1. hash each visited state -> the day it first appeared
2. on a repeat at day `d` with first sighting `f`:  period = d - f
3. remaining = (n - d) % period      ← burn off whole cycles
4. step `remaining` more times and return
```
LC 957 為什麼會終止：第 1 天之後兩端永遠是 `0`，只有中間 6 格會變 → 可達狀態最多 `2^6 = 64` 個。

```java
// java
// LC 957 - Prison Cells After N Days
// IDEA: PIGEONHOLE — <=64 reachable states, so the day-sequence is eventually
//       periodic. Find the period by hashing states, then n %= period.
// time = O(S * 8) with S = distinct states (<= 64), space = O(S)
public int[] prisonAfterNDays(int[] cells, int n) {
    Map<String, Integer> seen = new HashMap<>();   // state -> day first seen
    int day = 0;
    while (day < n) {
        String key = Arrays.toString(cells);
        Integer first = seen.get(key);
        if (first != null) {                       // cycle detected
            int period = day - first;
            int remain = (n - day) % period;
            for (int i = 0; i < remain; i++) cells = nextDay(cells);
            return cells;
        }
        seen.put(key, day);
        cells = nextDay(cells);
        day++;
    }
    return cells;
}

private int[] nextDay(int[] c) {
    int[] out = new int[8];                        // ends stay 0 by construction
    for (int i = 1; i < 7; i++) out[i] = (c[i - 1] == c[i + 1]) ? 1 : 0;
    return out;
}
```

```python
# python
# LC 957 - Prison Cells After N Days
# time = O(S * 8), space = O(S)
def prisonAfterNDays(cells, n):
    seen, day = {}, 0
    while day < n:
        key = tuple(cells)
        if key in seen:                            # cycle detected
            period = day - seen[key]
            for _ in range((n - day) % period):
                cells = next_day(cells)
            return cells
        seen[key] = day
        cells = next_day(cells)
        day += 1
    return cells

def next_day(c):
    return [0] + [1 if c[i - 1] == c[i + 1] else 0 for i in range(1, 7)] + [0]
```

> 同一套「把狀態雜湊起來 → 偵測週期 → 把循環數取模掉」的骨架，可以解掉任何
> 狀態有界、但步數大到荒謬的模擬題。如果你只需要*偵測*循環（不需要知道起始偏移），
> Floyd 的龜兔賽跑用 O(1) 空間就能做到。

## 模式 2：GCD／LCM

```java
int gcd(int a, int b) { return b == 0 ? a : gcd(b, a % b); }
long lcm(long a, long b) { return a / gcd(a, b) * b; }  // divide first to avoid overflow
```

```python
from math import gcd
lcm = a * b // gcd(a, b)
# Python 3.9+: math.lcm(a, b)
```

**經典 LC：**
- LC 1071 GCD of Strings — O(N+M)
- LC 878 Nth Magical Number — LCM + 二分搜尋
- LC 2344 Minimum Deletions to Make Array Divisible — 目標陣列的 GCD

## 模式 3：質數與篩法

### 埃拉托斯特尼篩法 — LC 204
```java
// Time: O(N log log N), Space: O(N)
boolean[] sieve(int n) {
    boolean[] isPrime = new boolean[n + 1];
    Arrays.fill(isPrime, true);
    isPrime[0] = isPrime[1] = false;
    for (int i = 2; i * i <= n; i++) {
        if (isPrime[i]) {
            for (int j = i * i; j <= n; j += i) {
                isPrime[j] = false;
            }
        }
    }
    return isPrime;
}
```

### 質因數分解
```java
// Time: O(√N)
List<int[]> primeFactors(int n) {
    List<int[]> factors = new ArrayList<>();
    for (int i = 2; i * i <= n; i++) {
        int count = 0;
        while (n % i == 0) { n /= i; count++; }
        if (count > 0) factors.add(new int[]{i, count});
    }
    if (n > 1) factors.add(new int[]{n, 1});
    return factors;
}
```

**經典 LC：** LC 204（Count Primes）、LC 952（Largest Component by Common Factor）

### Legendre 公式 — `n!` 中某個質數的指數 — LC 172 ⭐⭐⭐⭐
**核心想法**：不必真的算出 `n!`，就能直接讀出質數 `p` 在 `n!` 中的次方。

**推導**：在 `1..n` 之中，剛好有 `⌊n/p⌋` 個數至少貢獻一個因數 `p`，其中 `⌊n/p²⌋` 個再貢獻*第二個*，依此類推。把每一層加起來：

```text
e_p(n!) = ⌊n/p⌋ + ⌊n/p²⌋ + ⌊n/p³⌋ + ...     (stops once p^k > n)

Example  e_5(30!) = ⌊30/5⌋ + ⌊30/25⌋ = 6 + 1 = 7

Trailing zeros of n! = min(e_2, e_5) = e_5
  → 2s are strictly more plentiful than 5s, so the 5s are always the bottleneck.
```

```java
// java
// LC 172 - Factorial Trailing Zeroes
// IDEA: LEGENDRE'S FORMULA — e_p(n!) = sum_{k>=1} floor(n / p^k).
//       A trailing zero needs one 2 and one 5; 5s are scarcer, so count them.
// time = O(log_p N), space = O(1)
public int trailingZeroes(int n) {
    return legendre(n, 5);
}

// reusable helper: exponent of prime p in n!
int legendre(int n, int p) {
    int e = 0;
    for (long pk = p; pk <= n; pk *= p) e += (int) (n / pk);   // long pk: p^k can overflow int
    return e;
}
```

```python
# python
# LC 172 - Factorial Trailing Zeroes
# time = O(log_p N), space = O(1)
def legendre(n, p):
    e, pk = 0, p
    while pk <= n:
        e += n // pk
        pk *= p
    return e

def trailingZeroes(n):
    return legendre(n, 5)
```

**它在 LC 172 之外的價值**：`legendre()` 是處理*任何*「階乘／二項式的整除性」問題的工具 — 例如 `p` 能整除 `C(n, r)` 幾次，就是 `e_p(n!) - e_p(r!) - e_p((n-r)!)`；而能整除 `n!` 的最大 `p^k` 就是 `p^legendre(n,p)`。

## 模式 4：組合與計數

### 用 Pascal 三角求 nCr
```java
// Time: O(N·K), Space: O(N·K)
long[][] pascal(int n) {
    long[][] C = new long[n + 1][n + 1];
    for (int i = 0; i <= n; i++) {
        C[i][0] = 1;
        for (int j = 1; j <= i; j++) {
            C[i][j] = C[i-1][j-1] + C[i-1][j];  // add % MOD if needed
        }
    }
    return C;
}
```

### 用模反元素求 nCr（N 很大時）
```python
MOD = 10**9 + 7

def nCr(n, r):
    if r > n: return 0
    num = den = 1
    for i in range(r):
        num = num * (n - i) % MOD
        den = den * (i + 1) % MOD
    return num * pow(den, MOD - 2, MOD) % MOD
```

### Catalan 數 — LC 96
```text
C(n) = C(2n, n) / (n+1) = (2n)! / ((n+1)! * n!)

C(0)=1, C(1)=1, C(2)=2, C(3)=5, C(4)=14, C(5)=42

Applications:
  - Number of valid parentheses sequences of length 2n
  - Number of unique BSTs with n nodes
  - Number of ways to triangulate a polygon with n+2 sides
```

**經典 LC：** LC 96（Unique BSTs）、LC 22（Generate Parentheses 的計數版）

### 格子圖上的路徑 — LC 62 ⭐⭐⭐⭐⭐
**核心想法**：格子圖上的單調路徑，其實就是**一個由 D 與 R 組成的字串**。選路徑等於選*哪些*步是 D。這就把 O(m·n) 的 DP 變成一個二項式係數。

```text
m x n grid, moves right/down only:
  total steps  = (m-1) downs + (n-1) rights = m+n-2
  a path       <=> which (m-1) of those m+n-2 slots are "down"

  paths = C(m+n-2, m-1) = C(m+n-2, n-1)

  3 x 7 grid -> C(8, 2) = 28
```

**乘法形式**（千萬別真的去算階乘，long 很快就爆掉）：
`C(N, r) = Π_{i=1..r} (N-r+i) / i`，而第 `i` 步之後的部分乘積剛好是 `C(N-r+i, i)`，是整數 — 所以每一步用整數除法都不會失準。

```java
// java
// LC 62 - Unique Paths
// IDEA: COMBINATORICS — every path is a permutation of (m-1) D's and (n-1) R's,
//       so answer = C(m+n-2, m-1). Multiply/divide alternately to stay in range.
// time = O(min(m,n)), space = O(1)
public int uniquePaths(int m, int n) {
    int total = m + n - 2;
    int r = Math.min(m - 1, n - 1);        // pick the smaller half -> fewer iterations
    long res = 1;
    for (int i = 1; i <= r; i++) {
        res = res * (total - r + i) / i;   // exact: res == C(total-r+i, i) each step
    }
    return (int) res;
}
```

```python
# python
# LC 62 - Unique Paths
# time = O(min(m,n)), space = O(1)
from math import comb

def uniquePaths(m, n):
    return comb(m + n - 2, m - 1)

# manual version (no math.comb) — same alternate multiply/divide trick
def uniquePathsManual(m, n):
    total, r = m + n - 2, min(m - 1, n - 1)
    res = 1
    for i in range(1, r + 1):
        res = res * (total - r + i) // i
    return res
```

**面試時值得順口說出的推廣：**

| 額外限制 | 答案 |
|---|---|
| 單純 `m x n`，只能往右／往下 | `C(m+n-2, m-1)` |
| 必須經過某個檢查點 `p` | `paths(start→p) * paths(p→end)` |
| 有障礙物／被封鎖的格子 | 封閉解失效 → 退回 O(m·n) DP |
| 路徑必須維持在對角線下方（含對角線） | **Catalan** `C(n)`（見上）— 也就是投票問題 |

### 階乘進位制（排列的編號與還原） — LC 60 ⭐⭐⭐⭐
**核心想法**：`n!` 種排列按字典序編號為 `0 .. n!-1`。固定第一個元素，就框住連續的 `(n-1)!` 個排列，所以把編號寫成**階乘進位制的每一位數**，就直接對應每一步的選擇 — 不用列舉，也不用回溯。

```text
rank = d_{n-1}*(n-1)! + d_{n-2}*(n-2)! + ... + d_1*1! + d_0*0!,   0 <= d_i <= i

n=4, k=9  ->  rank 8 (0-indexed), pool [1,2,3,4]
  8 / 3! = 1 rem 2   -> take pool[1] = 2   pool -> [1,3,4]
  2 / 2! = 1 rem 0   -> take pool[1] = 3   pool -> [1,4]
  0 / 1! = 0 rem 0   -> take pool[0] = 1   pool -> [4]
  0 / 0! = 0         -> take pool[0] = 4
  => "2314"
```

```java
// java
// LC 60 - Permutation Sequence
// IDEA: UNRANKING via the factorial number system. Fixing the i-th element
//       skips blocks of (i-1)! permutations => digit = k / (i-1)!, k %= (i-1)!.
// time = O(N^2)  (List.remove is O(N)), space = O(N)
public String getPermutation(int n, int k) {
    int[] fact = new int[n + 1];
    fact[0] = 1;
    for (int i = 1; i <= n; i++) fact[i] = fact[i - 1] * i;   // n <= 9, fits int

    List<Integer> pool = new ArrayList<>();
    for (int i = 1; i <= n; i++) pool.add(i);

    k--;                                       // convert to 0-indexed rank
    StringBuilder sb = new StringBuilder();
    for (int i = n; i >= 1; i--) {
        int idx = k / fact[i - 1];
        k %= fact[i - 1];
        sb.append(pool.remove(idx));           // remove keeps `pool` sorted
    }
    return sb.toString();
}
```

```python
# python
# LC 60 - Permutation Sequence
# time = O(N^2), space = O(N)
from math import factorial

def getPermutation(n, k):
    pool = list(range(1, n + 1))
    k -= 1                                     # 0-indexed rank
    out = []
    for i in range(n, 0, -1):
        idx, k = divmod(k, factorial(i - 1))
        out.append(pool.pop(idx))
    return "".join(map(str, out))
```

**反方向（求編號）** — 常見的追問是「給你一個排列，它的索引是多少？」。第 `d_i` 位 = 還沒用過、且比 `perm[i]` 小的值有幾個：

```java
// java
// Inverse of LC 60: 0-indexed lexicographic rank of a permutation
// time = O(N^2) brute (use a BIT for O(N log N)), space = O(1)
long rankOf(int[] perm) {
    int n = perm.length;
    long rank = 0, f = 1;
    for (int i = n - 1; i >= 0; i--) {
        int smaller = 0;
        for (int j = i + 1; j < n; j++) if (perm[j] < perm[i]) smaller++;
        rank += smaller * f;                   // f == (n-1-i)! at this point
        f *= (n - i);
    }
    return rank;
}
```

```python
# python
# Inverse of LC 60: 0-indexed lexicographic rank of a permutation
# time = O(N^2), space = O(1)
def rank_of(perm):
    n = len(perm)
    rank, f = 0, 1
    for i in range(n - 1, -1, -1):
        smaller = sum(1 for j in range(i + 1, n) if perm[j] < perm[i])
        rank += smaller * f
        f *= (n - i)
    return rank
```

> `rankOf` 其實是在數右邊的逆序對 — `n` 很大時把內層迴圈換成樹狀陣列（Fenwick），可做到 O(N log N)。

### 依貢獻度計數 ⭐⭐⭐⭐⭐ — LC 477
**核心想法**：當一個總和是跑遍**所有配對／子陣列／子集合**時，不要去走訪這些結構 — 把迴圈翻過來問：*「每個最小單位分別出現在幾個結構裡？」*
這會把 O(N²)（甚至更糟）的總和，變成 O(N) × （每個單位一次便宜的計數）。

```text
   sum over structures  ==  sum over atoms of  (value) * (# structures containing it)

Two counts worth memorising:
  - pairs across a binary split : ones * (n - ones)
  - subarrays containing index i: (i + 1) * (n - i)
```

以 LC 477 來說，各個位元互相獨立（Hamming 距離是逐位元相加），所以固定一個位元位置：在該位不同的配對數，剛好是 `ones × (n - ones)`。

```java
// java
// LC 477 - Total Hamming Distance
// IDEA: COUNT BY CONTRIBUTION. Skip the C(n,2) pairs; ask what each BIT adds.
//       At a bit with `ones` set and `n-ones` clear, exactly ones*(n-ones) pairs differ.
// time = O(32*N), space = O(1)
public int totalHammingDistance(int[] nums) {
    int n = nums.length, total = 0;
    for (int b = 0; b < 32; b++) {
        int ones = 0;
        for (int x : nums) ones += (x >> b) & 1;
        total += ones * (n - ones);        // pairs differing at bit b
    }
    return total;
}
```

```python
# python
# LC 477 - Total Hamming Distance
# time = O(32*N), space = O(1)
def totalHammingDistance(nums):
    n, total = len(nums), 0
    for b in range(32):
        ones = sum((x >> b) & 1 for x in nums)
        total += ones * (n - ones)
    return total
```

**怎麼認出來**：題目說「對**所有配對**／**所有子陣列**求和」，而 `N` 大到直接雙層迴圈會 TLE。能把總和拆成每個單位的貢獻，靠的就是獨立性（位元、位數、索引）。

## 模式 5：蓄水池抽樣與隨機

### 蓄水池抽樣（K=1） — LC 382
```java
// Select 1 random element from stream of unknown length
// Each element has 1/n probability of being chosen
// Time: O(N), Space: O(1)
Random rand = new Random();
int result = 0;
int count = 0;
for (int val : stream) {
    count++;
    if (rand.nextInt(count) == 0) {
        result = val;
    }
}
```

**經典 LC：** LC 382（Linked List Random Node）、LC 398（Random Pick Index）

### Fisher-Yates 洗牌 — LC 384
```java
// Time: O(N), Space: O(1) extra
void shuffle(int[] arr) {
    Random rand = new Random();
    for (int i = arr.length - 1; i > 0; i--) {
        int j = rand.nextInt(i + 1);
        int tmp = arr[i]; arr[i] = arr[j]; arr[j] = tmp;
    }
}
```

**經典 LC：** LC 384（Shuffle an Array）

### 加權隨機／在前綴和上二分搜尋 — LC 528
**經典 LC：** LC 528（Random Pick with Weight） — 每次抽取 O(log N)

## 模式 6：幾何／計算幾何

### 外積（方向判斷）
```java
// Returns > 0 if counter-clockwise, < 0 if clockwise, 0 if collinear
long cross(int[] O, int[] A, int[] B) {
    return (long)(A[0] - O[0]) * (B[1] - O[1])
         - (long)(A[1] - O[1]) * (B[0] - O[0]);
}
```

### 凸包（Andrew 單調鏈） — LC 587
```python
# Time: O(N log N), Space: O(N)
def convex_hull(points):
    points.sort()
    lower = []
    for p in points:
        while len(lower) >= 2 and cross(lower[-2], lower[-1], p) <= 0:
            lower.pop()
        lower.append(p)
    upper = []
    for p in reversed(points):
        while len(upper) >= 2 and cross(upper[-2], upper[-1], p) <= 0:
            upper.pop()
        upper.append(p)
    return lower[:-1] + upper[:-1]

def cross(O, A, B):
    return (A[0]-O[0])*(B[1]-O[1]) - (A[1]-O[1])*(B[0]-O[0])
```

### 距離與直線公式
```text
Euclidean distance: sqrt((x2-x1)² + (y2-y1)²)
Manhattan distance: |x2-x1| + |y2-y1|

Tip: Avoid sqrt when possible — compare squared distances instead.
Tip: Use long to avoid integer overflow in distance calculations.
```

**經典 LC：**
- LC 149 Max Points on a Line — 用 GCD 表示斜率，O(N²)
- LC 587 Erect the Fence — 凸包
- LC 973 K Closest Points to Origin — O(N) quickselect 或 O(N log K) 堆積
- LC 1232 Check if Straight Line — 外積

### 變形：矩形上的排容原理 — LC 836／LC 223
*轉折：這兩題都可以化約成**同一個一維基本操作** — 兩個區間的重疊 — 每個軸各做一次。
重疊判定＝「兩個軸都重疊」；聯集面積＝排容原理。*

```text
Inclusion-Exclusion:  |A ∪ B| = |A| + |B| - |A ∩ B|
      (3 sets)        |A∪B∪C| = Σ|A| - Σ|A∩B| + |A∩B∩C|      ← signs alternate

1-D primitive:   overlap(lo1,hi1, lo2,hi2) = max(0, min(hi1,hi2) - max(lo1,lo2))
2-D:             axis-aligned rectangles are a PRODUCT of two independent intervals
                 => area(A ∩ B) = overlapX * overlapY
```

```java
// java
// LC 836 - Rectangle Overlap  /  LC 223 - Rectangle Area
// IDEA: one shared 1-D helper. Overlap <=> positive overlap on BOTH axes.
//       Union area <=> inclusion-exclusion: |A| + |B| - |A ∩ B|.
// time = O(1), space = O(1)
long overlapLen(int lo1, int hi1, int lo2, int hi2) {
    return Math.max(0, Math.min(hi1, hi2) - Math.max(lo1, lo2));
}

public boolean isRectangleOverlap(int[] a, int[] b) {
    return overlapLen(a[0], a[2], b[0], b[2]) > 0      // x axis
        && overlapLen(a[1], a[3], b[1], b[3]) > 0;     // y axis
}

public int computeArea(int ax1, int ay1, int ax2, int ay2,
                       int bx1, int by1, int bx2, int by2) {
    long areaA = (long)(ax2 - ax1) * (ay2 - ay1);
    long areaB = (long)(bx2 - bx1) * (by2 - by1);
    long inter = overlapLen(ax1, ax2, bx1, bx2) * overlapLen(ay1, ay2, by1, by2);
    return (int)(areaA + areaB - inter);               // long math: coords reach 1e4
}
```

```python
# python
# LC 836 - Rectangle Overlap  /  LC 223 - Rectangle Area
# time = O(1), space = O(1)
def overlap_len(lo1, hi1, lo2, hi2):
    return max(0, min(hi1, hi2) - max(lo1, lo2))

def isRectangleOverlap(a, b):
    return (overlap_len(a[0], a[2], b[0], b[2]) > 0
            and overlap_len(a[1], a[3], b[1], b[3]) > 0)

def computeArea(ax1, ay1, ax2, ay2, bx1, by1, bx2, by2):
    areaA = (ax2 - ax1) * (ay2 - ay1)
    areaB = (bx2 - bx1) * (by2 - by1)
    inter = overlap_len(ax1, ax2, bx1, bx2) * overlap_len(ay1, ay2, by1, by2)
    return areaA + areaB - inter
```

> `max(0, ...)` 才是安全的關鍵：**負的**原始重疊量必須夾成 0，否則兩個不相交的矩形會「扣掉」一塊不存在的正交集面積，把聯集撐大。

### 變形：用對角線配對數矩形 — LC 939
*轉折：一個軸對齊矩形，被**一條對角線**唯一決定。所以列舉 `C(N,2)` 組點對，而不是 `C(N,4)` 組四元組，再用雜湊檢查另外兩個角。*

```java
// java
// LC 939 - Minimum Area Rectangle
// IDEA: pick 2 points as a DIAGONAL (needs different x AND different y), then the
//       other two corners are forced: (x1,y2) and (x2,y1). Hash-set lookup, O(1).
// time = O(N^2), space = O(N)
public int minAreaRect(int[][] points) {
    Set<Long> seen = new HashSet<>();
    for (int[] p : points) seen.add(key(p[0], p[1]));

    long best = Long.MAX_VALUE;
    for (int i = 0; i < points.length; i++) {
        for (int j = i + 1; j < points.length; j++) {
            int x1 = points[i][0], y1 = points[i][1];
            int x2 = points[j][0], y2 = points[j][1];
            if (x1 == x2 || y1 == y2) continue;          // same row/col -> not a diagonal
            if (seen.contains(key(x1, y2)) && seen.contains(key(x2, y1))) {
                best = Math.min(best, (long) Math.abs(x1 - x2) * Math.abs(y1 - y2));
            }
        }
    }
    return best == Long.MAX_VALUE ? 0 : (int) best;
}

private long key(int x, int y) { return (long) x * 40001 + y; }   // coords <= 4*10^4
```

```python
# python
# LC 939 - Minimum Area Rectangle
# time = O(N^2), space = O(N)
def minAreaRect(points):
    seen = {(x, y) for x, y in points}
    best = float("inf")
    for i in range(len(points)):
        x1, y1 = points[i]
        for j in range(i + 1, len(points)):
            x2, y2 = points[j]
            if x1 == x2 or y1 == y2:                     # not a diagonal
                continue
            if (x1, y2) in seen and (x2, y1) in seen:
                best = min(best, abs(x1 - x2) * abs(y1 - y2))
    return 0 if best == float("inf") else best
```

> 每個矩形會被找到**兩次**（兩條對角線各一次）— 對取 `min` 無害，但如果題目問的是矩形*數量*，記得除以 2。

## 模式 7：位元計數與數論

### 計算因數個數
```text
Number of divisors of n = product of (e_i + 1) for each prime factor p_i^e_i
Example: 12 = 2² × 3¹ → (2+1)(1+1) = 6 divisors
```

### 歐拉函數
```text
φ(n) = count of integers in [1, n] coprime to n
φ(p) = p - 1  for prime p
φ(p^k) = p^k - p^(k-1)
```

### 數位和／數位根 — LC 258
```text
Digital root of n = 1 + (n-1) % 9  (for n > 0)
```

**經典 LC：** LC 258（Add Digits）、LC 1922（Count Good Numbers）

### 值得認得的具名定理（一句話版）
這些不需要模板 — 知道定理本身就等於解出來了。

| LC | 定理／技巧 | 好處 |
|----|-----------------|--------|
| 279 Perfect Squares | **Lagrange 四平方和** ⇒ 答案 ∈ {1,2,3,4}；當 `n = 4^a(8b+7)` 時是 `4`（Legendre 三平方和定理），`n` 是完全平方數時是 `1`，其餘再測 `2` | O(√N)，取代 O(N√N) 的 DP |
| 633 Sum of Square Numbers | **Fermat 二平方和**：`c = a²+b²` 的充要條件是每個 `≡ 3 (mod 4)` 的質因數指數皆為偶數；實作上直接用雙指標 `a=0, b=⌊√c⌋` | O(√C)、O(1) 空間 |
| 89 Gray Code | **反射二進位碼**：`g(i) = i ^ (i >> 1)` — 由構造本身保證相鄰兩碼只差一個位元 | 輸出 O(2^n)，不需要回溯 |
| 202 Happy Number | 數位平方和的映射，可達集合是**有限的** ⇒ 必定循環（跟前面 LC 957 是同一套鴿籠論證）；用 Floyd 龜兔賽跑可用 O(1) 空間偵測 | O(log N) 空間 → O(1) |

### 交叉參考（收在姊妹文件裡，這裡不重複）
- **數位／進位／進制運算** — LC 2、43、66、67、415、171 → [`add_x_sum.md`](add_x_sum.md)
- **快速冪、GCD／LCM、整數開根號、進制轉換、隨機抽樣、伸縮級數** — LC 50、69、149、204、384、396、504、528、762 → [`math.md`](math.md)

## LC 範例

| # | 題目 | 模式 | 時間 | 空間 |
|---|---------|---------|------|-------|
| 204 | Count Primes | 篩法 | O(N log log N) | O(N) |
| 96 | Unique BSTs | Catalan | O(N) | O(N) |
| 382 | Linked List Random Node | 蓄水池抽樣 | O(N) | O(1) |
| 528 | Random Pick with Weight | 前綴和 + 二分搜尋 | 建表 O(N)，抽取 O(log N) | O(N) |
| 149 | Max Points on a Line | GCD 斜率 | O(N²) | O(N) |
| 587 | Erect the Fence | 凸包 | O(N log N) | O(N) |
| 878 | Nth Magical Number | LCM + 二分搜尋 | O(log(N·max(A,B))) | O(1) |
| 952 | Largest Component | 篩法 + 併查集 | O(N√M) | O(M) |
| 1071 | GCD of Strings | GCD | O(N+M) | O(1) |
| 384 | Shuffle an Array | Fisher-Yates | O(N) | O(1) |
| 372 | Super Pow | 模冪運算 | O(N) | O(1) |
| 1808 | Max Nice Divisors | 模冪 + 數學 | O(log N) | O(1) |
| 62 | Unique Paths | 格子路徑 → `C(m+n-2, m-1)` | O(min(m,n)) | O(1) |
| 60 | Permutation Sequence | 階乘進位制（還原編號） | O(N²) | O(N) |
| 172 | Factorial Trailing Zeroes | Legendre 公式 | O(log₅ N) | O(1) |
| 477 | Total Hamming Distance | 依貢獻度計數（逐位元） | O(32·N) | O(1) |
| 523 | Continuous Subarray Sum | 前綴和模 K | O(N) | O(min(N,K)) |
| 957 | Prison Cells After N Days | 鴿籠原理 → 循環週期 | O(64·8) | O(64) |
| 223 | Rectangle Area | 排容原理 | O(1) | O(1) |
| 836 | Rectangle Overlap | 逐軸區間重疊 | O(1) | O(1) |
| 939 | Minimum Area Rectangle | 對角線配對 + 雜湊集合 | O(N²) | O(N) |
