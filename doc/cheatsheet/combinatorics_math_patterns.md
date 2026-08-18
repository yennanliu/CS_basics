# Combinatorics & Math Patterns

> **Scope** — Counting and number theory as they appear in interviews — modular arithmetic, GCD/LCM, sieves, nCr, reservoir sampling, and basic geometry.
> **See also**: [math.md](./math.md) — general numeric manipulation; [dp.md](./dp.md) — counting via recurrence instead of formula; [bit_manipulation.md](./bit_manipulation.md) — bit-level number theory.

## LeetCode Problem Lists

- [Combinatorics](https://leetcode.com/problem-list/combinatorics/)
- [Math](https://leetcode.com/problem-list/math/)
- [Number Theory](https://leetcode.com/problem-list/number-theory/)

## Overview
Google interviews frequently test math/combinatorics reasoning — more than other FAANGs. This covers number theory, counting, geometry, and probability patterns common in coding interviews.

### Key Properties
- **When to Use**: Problem involves counting arrangements, modular arithmetic, GCD/LCM, prime numbers, or geometric calculations
- **Google Signal**: Can you derive a formula instead of brute-forcing?

## Pattern 1: Modular Arithmetic

### Basics
```text
(a + b) % m = ((a % m) + (b % m)) % m
(a * b) % m = ((a % m) * (b % m)) % m
(a - b) % m = ((a % m) - (b % m) + m) % m   ← add m to avoid negative

Division: (a / b) % m = (a * b^(-1)) % m  where b^(-1) = modular inverse
```

### Modular Exponentiation (Fast Power) — LC 50
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

### Modular Inverse (when mod is prime)
```text
b^(-1) mod p = b^(p-2) mod p    (Fermat's little theorem)
```

**Classic LC:** LC 1808 (Maximize Number of Nice Divisors), LC 372 (Super Pow)

### Variation: Prefix Sums Modulo K — LC 523
*Twist: apply `(a - b) % m` backwards — two prefix sums with the same remainder bracket a subarray divisible by `k`.*

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

**Pigeonhole corollary**: only `k` remainders exist, so any `k+1` prefixes force a repeat — once `N >= k` some **non-empty** subarray is divisible by `k`. But the repeat can be at *adjacent* prefixes, i.e. length 1, which LC 523 rejects (`[1, 0]`, `k = 2` has `N >= k` yet no valid answer). For a guaranteed length `>= 2` subarray you need `N >= 2k` — apply pigeonhole to the even-indexed prefixes `p0, p2, p4, ...` only, so any repeat is already `>= 2` apart. (`2k` is tight — `N = 2k - 1` is not enough: `[0, 1, 0]`, `k = 2`.)

### Variation: Pigeonhole → Finite State Space Must Cycle — LC 957
*Twist: same pigeonhole argument on **whole states** instead of remainders — if the state space is finite and the step function is deterministic, the walk must cycle, so reduce a huge `n` modulo the period.*

**Recipe (reusable for any "apply f() n times, n up to 1e9" problem):**
```text
1. hash each visited state -> the day it first appeared
2. on a repeat at day `d` with first sighting `f`:  period = d - f
3. remaining = (n - d) % period      ← burn off whole cycles
4. step `remaining` more times and return
```
Why it terminates for LC 957: after day 1 both ends are always `0`, so only the 6 inner cells vary → at most `2^6 = 64` reachable states.

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

> The same "hash state → detect period → mod out the cycles" skeleton solves any
> bounded-state simulation with an absurdly large step count. If you only need to
> *detect* a cycle (not its offset), Floyd's tortoise/hare does it in O(1) space.

## Pattern 2: GCD / LCM

```java
int gcd(int a, int b) { return b == 0 ? a : gcd(b, a % b); }
long lcm(long a, long b) { return a / gcd(a, b) * b; }  // divide first to avoid overflow
```

```python
from math import gcd
lcm = a * b // gcd(a, b)
# Python 3.9+: math.lcm(a, b)
```

**Classic LC:**
- LC 1071 GCD of Strings — O(N+M)
- LC 878 Nth Magical Number — LCM + binary search
- LC 2344 Minimum Deletions to Make Array Divisible — GCD of target array

## Pattern 3: Prime Numbers & Sieve

### Sieve of Eratosthenes — LC 204
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

### Prime Factorization
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

**Classic LC:** LC 204 (Count Primes), LC 952 (Largest Component by Common Factor)

### Legendre's Formula — exponent of a prime inside `n!` — LC 172 ⭐⭐⭐⭐
**Key Idea**: you can read off the power of a prime `p` in `n!` **without ever building `n!`**.

**Derivation**: among `1..n`, exactly `⌊n/p⌋` numbers contribute at least one factor `p`,
`⌊n/p²⌋` of them contribute a *second* one, and so on. Summing the layers:

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

**Why it matters beyond LC 172**: `legendre()` is the tool for *any* "divisibility of a
factorial / binomial" question — e.g. how many times `p` divides `C(n, r)` is
`e_p(n!) - e_p(r!) - e_p((n-r)!)`, and the largest `p^k` dividing `n!` is `p^legendre(n,p)`.

## Pattern 4: Combinations & Counting

### nCr with Pascal's Triangle
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

### nCr with Modular Inverse (for large N)
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

### Catalan Numbers — LC 96
```text
C(n) = C(2n, n) / (n+1) = (2n)! / ((n+1)! * n!)

C(0)=1, C(1)=1, C(2)=2, C(3)=5, C(4)=14, C(5)=42

Applications:
  - Number of valid parentheses sequences of length 2n
  - Number of unique BSTs with n nodes
  - Number of ways to triangulate a polygon with n+2 sides
```

**Classic LC:** LC 96 (Unique BSTs), LC 22 (Generate Parentheses count)

### Lattice Paths in a Grid — LC 62 ⭐⭐⭐⭐⭐
**Key Idea**: a monotone grid path is nothing but a **word made of D's and R's**. Choosing
the path = choosing *which* steps are the D's. That turns an O(m·n) DP into one binomial.

```text
m x n grid, moves right/down only:
  total steps  = (m-1) downs + (n-1) rights = m+n-2
  a path       <=> which (m-1) of those m+n-2 slots are "down"

  paths = C(m+n-2, m-1) = C(m+n-2, n-1)

  3 x 7 grid -> C(8, 2) = 28
```

**Multiplicative form** (never build factorials — they overflow long fast):
`C(N, r) = Π_{i=1..r} (N-r+i) / i`, and the partial product after step `i` is exactly
`C(N-r+i, i)`, an integer — so plain integer division stays exact at every step.

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

**Generalizations worth naming in an interview:**

| Constraint added | Answer |
|---|---|
| Plain `m x n`, right/down only | `C(m+n-2, m-1)` |
| Must pass through a checkpoint `p` | `paths(start→p) * paths(p→end)` |
| Obstacles / blocked cells | closed form breaks → fall back to O(m·n) DP |
| Path must stay weakly below the diagonal | **Catalan** `C(n)` (see above) — the ballot problem |

### Factorial Number System (Permutation Ranking / Unranking) — LC 60 ⭐⭐⭐⭐
**Key Idea**: the `n!` permutations in lexicographic order are numbered `0 .. n!-1`. Fixing
the first element freezes a contiguous block of `(n-1)!` of them, so the rank's **digits in
factorial base** name the choices directly — no enumeration, no backtracking.

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

**The inverse direction (ranking)** — asked as the follow-up "given a permutation, what is
its index?". Digit `d_i` = how many *unused* values smaller than `perm[i]` remain:

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

> `rankOf` counts inversions to the right — replace the inner loop with a BIT / Fenwick tree
> for O(N log N) when `n` is large.

### Counting by Contribution ⭐⭐⭐⭐⭐ — LC 477
**Key Idea**: when a sum runs over **pairs / subarrays / subsets**, don't iterate the
structures — flip the loops and ask *"how many structures does each atom appear in?"*
That converts an O(N²) (or worse) sum into O(N) × (cheap per-atom count).

```text
   sum over structures  ==  sum over atoms of  (value) * (# structures containing it)

Two counts worth memorising:
  - pairs across a binary split : ones * (n - ones)
  - subarrays containing index i: (i + 1) * (n - i)
```

For LC 477 the bits are independent (Hamming distance adds per bit), so fix a bit position:
the pairs that differ there are exactly `ones × (n - ones)`.

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

**Recognising it**: the prompt says "sum over **all pairs** / **all subarrays**" and `N` is
big enough that the literal double loop is TLE. Independence (bits, digits, indices) is what
lets you split the total into per-atom pieces.

## Pattern 5: Reservoir Sampling & Random

### Reservoir Sampling (K=1) — LC 382
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

**Classic LC:** LC 382 (Linked List Random Node), LC 398 (Random Pick Index)

### Fisher-Yates Shuffle — LC 384
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

**Classic LC:** LC 384 (Shuffle an Array)

### Weighted Random / Binary Search on Prefix Sum — LC 528
**Classic LC:** LC 528 (Random Pick with Weight) — O(log N) per pick

## Pattern 6: Geometry / Computational Geometry

### Cross Product (Orientation Test)
```java
// Returns > 0 if counter-clockwise, < 0 if clockwise, 0 if collinear
long cross(int[] O, int[] A, int[] B) {
    return (long)(A[0] - O[0]) * (B[1] - O[1])
         - (long)(A[1] - O[1]) * (B[0] - O[0]);
}
```

### Convex Hull (Andrew's Monotone Chain) — LC 587
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

### Distance & Line Formulas
```text
Euclidean distance: sqrt((x2-x1)² + (y2-y1)²)
Manhattan distance: |x2-x1| + |y2-y1|

Tip: Avoid sqrt when possible — compare squared distances instead.
Tip: Use long to avoid integer overflow in distance calculations.
```

**Classic LC:**
- LC 149 Max Points on a Line — GCD for slope representation, O(N²)
- LC 587 Erect the Fence — Convex Hull
- LC 973 K Closest Points to Origin — O(N) quickselect or O(N log K) heap
- LC 1232 Check if Straight Line — Cross product

### Variation: Inclusion–Exclusion on Rectangles — LC 836 / LC 223
*Twist: both problems reduce to the **same 1-D primitive** — overlap of two intervals —
applied once per axis. Overlap test = "both axes overlap"; union area = inclusion–exclusion.*

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

> `max(0, ...)` is what makes it safe: a **negative** raw overlap must be clamped, otherwise
> disjoint rectangles "subtract" a phantom positive intersection and inflate the union.

### Variation: Count Rectangles by Diagonal Pairs — LC 939
*Twist: an axis-aligned rectangle is uniquely pinned by **one diagonal**. Enumerate the
`C(N,2)` point pairs instead of the `C(N,4)` quadruples, and hash-check the two corners.*

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

> Each rectangle is found **twice** (once per diagonal) — harmless for a `min`, but remember
> to halve the count if the question asks *how many* rectangles instead.

## Pattern 7: Bit Counting & Number Theory

### Count Divisors
```text
Number of divisors of n = product of (e_i + 1) for each prime factor p_i^e_i
Example: 12 = 2² × 3¹ → (2+1)(1+1) = 6 divisors
```

### Euler's Totient
```text
φ(n) = count of integers in [1, n] coprime to n
φ(p) = p - 1  for prime p
φ(p^k) = p^k - p^(k-1)
```

### Sum of Digits / Digital Root — LC 258
```text
Digital root of n = 1 + (n-1) % 9  (for n > 0)
```

**Classic LC:** LC 258 (Add Digits), LC 1922 (Count Good Numbers)

### Named Theorems Worth Recognising (one-liners)
These need no template — knowing the theorem *is* the solution.

| LC | Theorem / trick | Payoff |
|----|-----------------|--------|
| 279 Perfect Squares | **Lagrange's four-square** ⇒ answer ∈ {1,2,3,4}; it is `4` iff `n = 4^a(8b+7)` (Legendre's three-square), `1` iff `n` is a perfect square, else test `2` | O(√N) instead of O(N√N) DP |
| 633 Sum of Square Numbers | **Fermat's two-square**: `c = a²+b²` iff every prime `≡ 3 (mod 4)` has even exponent; in practice just two-pointer `a=0, b=⌊√c⌋` | O(√C), O(1) space |
| 89 Gray Code | **Reflected binary code**: `g(i) = i ^ (i >> 1)` — consecutive codes differ in one bit by construction | O(2^n) output, no backtracking |
| 202 Happy Number | Digit-square map has a **finite** reachable set ⇒ must cycle (same pigeonhole as LC 957 above); Floyd's tortoise/hare detects it in O(1) space | O(log N) space → O(1) |

### Cross-references (covered in sibling cheatsheets — not duplicated here)
- **Digit / carry / base arithmetic** — LC 2, 43, 66, 67, 415, 171 → [`add_x_sum.md`](add_x_sum.md)
- **Fast power, GCD/LCM, integer sqrt, base conversion, randomized sampling, telescoping sums** — LC 50, 69, 149, 204, 384, 396, 504, 528, 762 → [`math.md`](math.md)

## LC Example

| # | Problem | Pattern | Time | Space |
|---|---------|---------|------|-------|
| 204 | Count Primes | Sieve | O(N log log N) | O(N) |
| 96 | Unique BSTs | Catalan | O(N) | O(N) |
| 382 | Linked List Random Node | Reservoir sampling | O(N) | O(1) |
| 528 | Random Pick with Weight | Prefix sum + BS | O(N) build, O(log N) pick | O(N) |
| 149 | Max Points on a Line | GCD slope | O(N²) | O(N) |
| 587 | Erect the Fence | Convex Hull | O(N log N) | O(N) |
| 878 | Nth Magical Number | LCM + binary search | O(log(N·max(A,B))) | O(1) |
| 952 | Largest Component | Sieve + Union-Find | O(N√M) | O(M) |
| 1071 | GCD of Strings | GCD | O(N+M) | O(1) |
| 384 | Shuffle an Array | Fisher-Yates | O(N) | O(1) |
| 372 | Super Pow | Mod exponentiation | O(N) | O(1) |
| 1808 | Max Nice Divisors | Mod exp + math | O(log N) | O(1) |
| 62 | Unique Paths | Lattice paths → `C(m+n-2, m-1)` | O(min(m,n)) | O(1) |
| 60 | Permutation Sequence | Factorial number system (unranking) | O(N²) | O(N) |
| 172 | Factorial Trailing Zeroes | Legendre's formula | O(log₅ N) | O(1) |
| 477 | Total Hamming Distance | Counting by contribution (per bit) | O(32·N) | O(1) |
| 523 | Continuous Subarray Sum | Prefix sums mod K | O(N) | O(min(N,K)) |
| 957 | Prison Cells After N Days | Pigeonhole → cycle period | O(64·8) | O(64) |
| 223 | Rectangle Area | Inclusion–exclusion | O(1) | O(1) |
| 836 | Rectangle Overlap | Per-axis interval overlap | O(1) | O(1) |
| 939 | Minimum Area Rectangle | Diagonal pairs + hash set | O(N²) | O(N) |
