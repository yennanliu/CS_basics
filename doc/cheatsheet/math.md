# Math

## LeetCode Problem Lists

- [Math](https://leetcode.com/problem-list/math/)
- [Number Theory](https://leetcode.com/problem-list/number-theory/)

## 1) General form

### 1-1) Basic OP

#### 1-1-0) transform `10 based interger to N based`
- to 4 base
- to 7 base ..

<p align="center"><img src="../pic/convert_int_n_base.png"></p>
```python
# python
# 504. Base 7

# V0
# IDEA : MATH : 10 based -> 7 based
"""
### NOTE :
    1) for negative num : transform to positive int first, do 10 based -> 7 based op
                          then add "-" at beginning
    2) for positive num : do 10 based -> 7 based op
                        -> keep checking if num % N == 0
                        -> if not == 0, keep do  num = num % N, and append cur result to res
                        -> reverse res
                        -> make array to string
                        -> return result
    3) example:
        100 (10 based) -> "202" (7 based)

        tmp = 100
        a, b = divmod(tmp, 7)  -> a = 14, b = 2,  tmp  = 14
        a, b = divmod(tmp, 7)  -> a= 2, b = 0, tmp = 2
        a, b = divmod(tmp, 7)  -> a = 0, b = 2

        -> so res = [2,0,2]
"""

# V1
class Solution(object):
    def convertToBase7(self, num):
        # edge case
        if num == 0:
            return '0'
        tmp = abs(num)
        res = []
        while tmp > 0:
            a, b = divmod(tmp, 7)
            res.append(str(b))
            tmp = a
        res = res[::-1]
        _res = "".join(res)
        if num > 0:
            return _res
        else:
            return "-" + _res

# test
#----------------
# exmaple :
# 7 base
# 20 -> 26
# -100 -> -202
#----------------
num = 20   # 26
num = -100 # -202
s = Solution()
r = s.convertToBase7(num)
print (r)

# V2
# https://www.itread01.com/content/1544603062.html
# https://kknews.cc/code/jlv38qp.html
class Solution(object):
    def convertToBase7(self, num):
        # edge case
        if num == 0:
            return '0'
        tmp = abs(num)
        res = []
        while tmp:
            i = tmp % 7
            res.append(str(i))
            tmp = tmp // 7
        res = res[::-1]
        _res = "".join(res)
        if num > 0:
            return _res
        else:
            return "-" + _res 
```

#### 1-1-0') transform `N based integer to 10 based`
```python
# V1
# LC 1022. Sum of Root To Leaf Binary Numbers
def convertToBaseN(num, n):
    return int(str(num), n)

In [34]: int("100",7)
Out[34]: 49

In [35]: int("14",7)
Out[35]: 11

In [36]: int("66",7)
Out[36]: 48
```

#### 1-1-1) check prime number
```python
# LC 762 Prime Number of Set Bits in Binary Representation
def check_prime(x):
    if x <= 1:
        return False
    for i in range(2, int(x**(0.5)+1)):
        if x % i == 0:
            return False
    return True
```

#### 1-1-2) count prime number
```python
# LC 204 Count Primes
# V0
# IDEA : set
# https://leetcode.com/problems/count-primes/discuss/1343795/python%3A-sieve-of-eretosthenes
# prime(x) : number of prime in [0, x]
# prime(0) = 0
# prime(1) = 0
# prime(2) = 0
# prime(3) = 1
# prime(4) = 2
# prime(5) = 3
class Solution:
    def countPrimes(self, n):
        # using sieve of eretosthenes algorithm
        if n < 2: return 0
        nonprimes = set()
        for i in range(2, round(n**(1/2))+1):
            if i not in nonprimes:
                for j in range(i*i, n, i):
                    nonprimes.add(j)
        return n - len(nonprimes) - 2  # remove prime(1), prime(2)
```

```java
// java
// algorithm book (labu) p.362
// V1
int countPrimes(int n){
    boolean[] isPrime = new boolean[n];

    // init array to true
    Arrays.fill(isPrime, true);

    // prime number start from 2
    for (int i = 2; i < n; i++){
        if (isPrime[i]){
            // if i is prime, then i's multiple is NOT prime
            for (int j = 2 * i; j < n; j += i){
                isPrime[j] = false;
            }
        }
    }

    int count = 0;
    for (int i = 2; i < n; i++){
        if (isPrime[i]){
            count ++;
        }
    }
    return count;
}
```

```java
// java
// algorithm book (labu) p.363
// V1' (optimization)
int countPrimes(int n){
    boolean[] isPrime = new boolean[n];

    // init array to true
    Arrays.fill(isPrime, true);

    // prime number start from 2
    for (int i = 2; i * i  < n; i++){
        if (isPrime[i]){
            // if i is prime, then i's multiple is NOT prime
            /** optimize here :  make j start from i * i, instead of 2 * i */
            // (the only difference between V1 and V1')
            for (int j = i * i; j < n; j += i){
                isPrime[j] = false;
            }
        }
    }

    int count = 0;
    for (int i = 2; i < n; i++){
        if (isPrime[i]){
            count ++;
        }
    }
    return count;
}
```

#### 1-1-3) Keep Remainder to Avoid Overflow (Repunit / Modular Arithmetic)

**Core Idea:**
When building a number digit-by-digit (e.g. 1 → 11 → 111 → ...), the number grows exponentially and overflows `int`/`long`.
Instead of storing the full number, **only keep the remainder mod k** at each step.

This works because:
```text
(a * 10 + 1) % k  ==  ((a % k) * 10 + 1) % k
```
So the remainder after adding a new digit can always be computed from the *previous* remainder alone.

**Pattern:**
```java
int remainder = 0;
for (int len = 1; len <= k; len++) {
    remainder = (remainder * 10 + 1) % k;  // build next repunit mod k
    if (remainder == 0) return len;         // found divisible repunit
}
return -1;
```

**Key insight — Pigeonhole Principle:**
There are only `k` possible remainders (0 to k-1). After `k` steps, if no remainder is 0, a remainder must repeat → cycle with no solution → return -1. So we only need to loop up to `k` times.

**Quick check — early exit:**
A number made only of 1s never ends in 0, 2, 4, 5, 6, or 8, so it can never be divisible by 2 or 5:
```java
if (k % 2 == 0 || k % 5 == 0) return -1;
```

**Similar LC problems using this remainder trick:**
| Problem | Pattern |
|---------|---------|
| LC 1015 - Smallest Integer Divisible by K | repunit mod k |
| LC 29  - Divide Two Integers | avoid overflow with bit ops |
| LC 166 - Fraction to Recurring Decimal | detect cycle via remainder map |
| LC 523 - Continuous Subarray Sum | prefix sum mod k |
| LC 974 - Subarray Sums Divisible by K | prefix sum mod k |

```java
// LC 1015 full solution
public int smallestRepunitDivByK(int k) {
    if (k % 2 == 0 || k % 5 == 0) return -1;
    int remainder = 0;
    for (int len = 1; len <= k; len++) {
        remainder = (remainder * 10 + 1) % k;
        if (remainder == 0) return len;
    }
    return -1;
}
```

#### 1-1-4) Check if 4 points form a valid square (Pairwise Distance Trick)

**Core Idea:**
Given 4 points in any order, checking angles/slopes directly is messy (division by zero, floating point). Instead, compute the **6 pairwise squared distances** among the 4 points and reason about them as a multiset.

**Why 6 distances?**
4 points → C(4,2) = 6 pairs. For a square:

```text
A ----- B
|       |
|       |
D ----- C
```

```text
AB = side
BC = side
CD = side
DA = side
AC = diagonal
BD = diagonal
```

So after sorting the 6 squared distances, you always get **4 equal (smaller) values + 2 equal (larger) values**.

**Pattern — a valid square has:**
- side > 0 (no overlapping points)
- 4 equal sides
- 2 equal diagonals
- diagonal > side

```python
# LC 593. Valid Square
class Solution(object):
    def validSquare(self, p1, p2, p3, p4):
        points = [p1, p2, p3, p4]
        dists = []

        for i in range(4):
            for j in range(i + 1, 4):
                dists.append(self.get_len(
                    points[i][0], points[i][1],
                    points[j][0], points[j][1]
                ))

        dists.sort()

        return (
            dists[0] > 0 and                 # no overlapping points
            dists[0] == dists[1] ==
            dists[2] == dists[3] and         # four equal sides
            dists[4] == dists[5] and         # two equal diagonals
            dists[4] > dists[0]              # diagonal longer than side
        )

    def get_len(self, x1, y1, x2, y2):
        # use squared distance -> avoid float/sqrt precision issues
        return (x1 - x2) ** 2 + (y1 - y2) ** 2
```

**Alternative (set-based) form:**
Since a valid square has exactly 2 distinct distance values (side², diagonal²), and diagonal is always > side for a real square, you can just check `len(set(distances)) == 2` (plus the `0 not in distances` guard for overlapping points):

```python
class Solution(object):
    def validSquare(self, p1, p2, p3, p4):
        def dist(a, b):
            return (a[0] - b[0]) ** 2 + (a[1] - b[1]) ** 2

        points = [p1, p2, p3, p4]
        lookup = set(
            dist(points[i], points[j])
            for i in range(4) for j in range(i + 1, 4)
        )
        return 0 not in lookup and len(lookup) == 2
```

#### 1-1-5) Weighted Rotation Sum — Telescoping Recurrence (avoid O(n²) brute force)

**Pattern:**
Brute-force recomputing `F(k) = sum(i * arr_k[i])` for every rotation `k` costs O(n) per rotation → O(n²) total. Instead, derive an **O(1) transition** from `F(k-1)` to `F(k)` and slide through all `n` rotations in O(n).

**Core Idea:**
Write out a few rotations by hand and compare term by term (`nums = [A, B, C, D]`, `n = 4`):

```text
F(0) = 0*A + 1*B + 2*C + 3*D
F(1) = 0*D + 1*A + 2*B + 3*C
F(2) = 0*C + 1*D + 2*A + 3*B
F(3) = 0*B + 1*C + 2*D + 3*A
```

Every element's weight goes up by 1 when you rotate — **except** the element that just wrapped from the back to the front, whose weight drops from `n-1` down to `0`. So:

```text
sum = A + B + C + D                 # total sum, weight-independent

F(1) = F(0) + sum - 4*D             # D's weight: 3 -> 0, i.e. -4*D; everything else: +1 each -> +sum
F(2) = F(1) + sum - 4*C
F(3) = F(2) + sum - 4*B
```

Generalizing, the element that wraps into position 0 for `F(k)` is `nums[n-k]`, giving the recurrence:

```text
F(k) = F(k-1) + sum(nums) - n * nums[n-k]
```

This is the same "**maintain a running aggregate instead of recomputing from scratch**" philosophy as prefix sums — just applied to a *weighted* sum instead of a plain sum.

```python
# LC 396. Rotate Function
# time = O(n), space = O(1)
class Solution(object):
    def maxRotateFunction(self, nums):
        size = len(nums)
        total = sum(nums)
        f = sum(i * x for i, x in enumerate(nums))  # F(0)

        ans = f
        for i in range(size - 1, 0, -1):
            # nums[i] is the element wrapping to front: weight n-1 -> 0
            f += total - size * nums[i]
            ans = max(ans, f)
        return ans
```

**Similar LC problems (same "O(1) transition between states" idea):**
| Problem | Pattern |
|---------|---------|
| LC 396 - Rotate Function | telescoping weighted-sum recurrence: `F(k) = F(k-1) + sum - n*nums[n-k]` |
| LC 238 - Product of Array Except Self | running prefix/suffix product instead of recomputing per index |
| LC 303 - Range Sum Query - Immutable | precomputed prefix sum instead of recomputing per query |
| LC 189 - Rotate Array | actual physical rotation (reverse trick) — contrast: no aggregate formula, just rearranges elements |

#### 1-1-6) Greedy Zigzag Construction — closed-form O(1) instead of simulation

**Problem shape (LC 3993 - Maximum Value of an Alternating Sequence):**
Given `n`, `s`, `m` — build a length-`n` alternating (zigzag) sequence with `seq[0] = s` and `|seq[i] - seq[i-1]| <= m`. Return the **maximum element** that can appear in any valid sequence.

**Core Idea:**
Don't search / DP over sequences. Ask only: *what does one extra peak cost?* Then the answer is a formula.

Three greedy observations pin everything down:

1. **Always start by going up.** Starting with a down-step (`seq[0] > seq[1]`) wastes the first move and every later peak is lower. So use the `seq[0] < seq[1] > seq[2] < ...` shape.
2. **Every up-step should be `+m`** — the largest jump allowed.
3. **Every down-step should be `-1`** — the *smallest* legal drop, because the inequality is **strict** (`>`), so the minimum decrease is 1. Dropping more just lowers all following peaks.

So the sequence is a sawtooth that climbs `m` and gives back only `1`:

```text
n = 6, s = 3, m = 5

              s+2m-1=12          s+3m-2=16
                 /\                 /\
      s+m=8    /    \             /
        /\    /      \           /
       /   \ /   s+2m-2=11      /
      /   s+m-1=7              /
   s=3

seq = [3, 8, 7, 12, 11, 16]
       ^  +5 -1  +5  -1  +5
```

**Counting the moves:**
- Peaks sit at odd indices `1, 3, 5, ...` → number of up-steps `k = n // 2`
- Between `k` peaks there are `k - 1` down-steps, each costing exactly `1`

```text
max = s + k*m - (k - 1)
    = s + k*(m - 1) + 1        , where k = n // 2
```

**Edge case:** `n == 1` → a length-1 sequence is alternating by definition, and it must equal `s`. (The formula would give `s + 1`, which is wrong, so guard it explicitly.)

**Pattern:**
```python
# python
# LC 3993. Maximum Value of an Alternating Sequence
# time = O(1), space = O(1)
class Solution(object):
    def maximumValue(self, n, s, m):
        # IDEA: GREEDY ZIGZAG -> CLOSED FORM
        # go up by m, come down by only 1 (strict inequality), repeat

        # edge case: length-1 sequence is just [s]
        if n == 1:
            return s

        k = n // 2                 # number of "up" moves == number of peaks
        return s + k * (m - 1) + 1  # == s + k*m - (k-1)
```

```java
// java
// LC 3993 - Maximum Value of an Alternating Sequence
// time = O(1), space = O(1)
public long maximumValue(int n, int s, int m) {
    // IDEA: GREEDY ZIGZAG -> CLOSED FORM
    if (n == 1) {
        return s;
    }
    long k = n / 2;                    // peaks == up moves
    return s + k * (m - 1) + 1;        // use long: n, s up to 1e9
}
```

**Sanity check:**

| n | s | m | k = n//2 | s + k*(m-1) + 1 | sequence |
|---|---|---|----------|-----------------|----------|
| 1 | 3 | 5 | — | `3` (edge case) | `[3]` |
| 2 | 4 | 3 | 1 | `4 + 1*2 + 1 = 7` | `[4, 7]` |
| 4 | 3 | 5 | 2 | `3 + 2*4 + 1 = 12` | `[3, 8, 7, 12]` |
| 5 | 3 | 5 | 2 | `3 + 2*4 + 1 = 12` | `[3, 8, 7, 12, 11]` |
| 4 | 3 | 1 | 2 | `3 + 2*0 + 1 = 4` | `[3, 4, 3, 4]` |

Note `n = 4` and `n = 5` give the same answer — an even trailing step can only go *down* from the last peak, so it never raises the max.

**Key takeaways (transferable):**
- **"Strict inequality" ⇒ the minimal step is 1, not 0.** That `-1` is where the `(m - 1)` comes from.
- When constraints are only on *adjacent* pairs, the optimum is usually a **greedy repeating unit**; count how many units fit, then multiply.
- Constraints like `1 <= n, s <= 1e9` are a strong hint that the intended answer is **O(1) math**, not simulation — and that you need 64-bit ints.

**Similar LC problems:**
| Problem | Pattern |
|---------|---------|
| LC 3993 - Maximum Value of an Alternating Sequence | greedy zigzag (`+m` / `-1`) → closed form `s + (n//2)*(m-1) + 1` |
| LC 376 - Wiggle Subsequence | alternating up/down, but *pick* elements (greedy count of direction flips) |
| LC 280 - Wiggle Sort | construct an alternating array in-place by swapping adjacent violators |
| LC 324 - Wiggle Sort II | strict alternating + no equal neighbors → sort then interleave halves |
| LC 1846 - Maximum Element After Decreasing and Rearranging | maximize final element under `|adjacent diff| <= 1` → greedy `prev + 1` |
| LC 1936 - Add Minimum Number of Rungs | adjacent gap capped at `dist` → `ceil(gap/dist) - 1` per gap (counting, not simulation) |
| LC 453 - Minimum Moves to Equal Array Elements | reframe "increment n-1" as "decrement 1" → `sum - n*min`, O(1) math |
| LC 462 - Minimum Moves to Equal Array Elements II | move to median; closed-form cost instead of trying each target |

#### 1-1-7) Split `n` into `k` parts as evenly as possible → **max product** (`divmod` pattern) ⭐⭐⭐⭐⭐

**Problem shape (LC 343 - Integer Break):**
Break `n` into a sum of `k >= 2` positive integers and maximize their product.

##### Core Idea

Two independent questions — solve them separately:

1. **Given a FIXED `k`, how should the parts be sized?** → **as evenly as possible** (`divmod`)
2. **Which `k` is best?** → try them all (`O(n²)`), or use math (`O(log n)`, see below)

**Why "as evenly as possible" is optimal (exchange argument):**

For a fixed sum and fixed count, the product is maximized when all parts are equal (**AM–GM inequality**). With *integers* you can't always be exactly equal, so parts must differ by **at most 1**.

```text
Proof sketch — suppose two parts differ by >= 2, i.e.  a >= b + 2
Replace (a, b) with (a-1, b+1)  -> sum is unchanged

  (a-1)(b+1) = ab + a - b - 1
             >= ab + 1          , since a - b >= 2

  -> product STRICTLY increases
  -> keep rebalancing until every pair differs by <= 1
```

**So the split is fully determined by `divmod`:**

```text
q, r = divmod(n, k)          # q = n // k  (商數), r = n % k  (餘數)

  ->  r     parts have value (q + 1)      <-- the "leftover" r units are spread
  ->  (k-r) parts have value  q               one-by-one over r parts

Sanity check the sum:
  r*(q+1) + (k-r)*q  =  r*q + r + k*q - r*q  =  k*q + r  =  n   ✅

Max product for that k:
  P(n, k) = (q + 1)^r  *  q^(k - r)
```

##### Visual Example

```text
n = 10, k = 3
  q, r = divmod(10, 3) = (3, 1)
  -> 1 part  of q+1 = 4
  -> 2 parts of q   = 3
  -> [4, 3, 3]   sum = 10 ✅   product = 4*3*3 = 36

Compare with UNEVEN splits of the same n and k:
  [8, 1, 1] -> 8      [6, 2, 2] -> 24     [5, 4, 1] -> 20
  [4, 3, 3] -> 36  <-- max, and it is the "even" one ✅
```

Full sweep of `k` for `n = 10`:

| k | `divmod(10, k)` = (q, r) | parts | product |
|---|--------------------------|-------|---------|
| 2 | (5, 0) | `[5, 5]` | 25 |
| 3 | (3, 1) | `[4, 3, 3]` | **36** ⭐ |
| 4 | (2, 2) | `[3, 3, 2, 2]` | 36 ⭐ |
| 5 | (2, 0) | `[2, 2, 2, 2, 2]` | 32 |
| 6 | (1, 4) | `[2, 2, 2, 2, 1, 1]` | 16 |
| 7 | (1, 3) | `[2, 2, 2, 1, 1, 1, 1]` | 8 |

→ answer = 36. Notice the product **rises then falls** — parts near 3 are the sweet spot (see "Why 3?" below).

##### Pattern — the reusable `divmod` helper

```python
# python
# GENERAL PATTERN: split n into k parts as evenly as possible
def get_product(n, k):
    """
    Split n into k parts as evenly as possible,
    return their (maximum) product.
    """
    ### NOTE !!! get q (商數), r (餘數) via divmod
    q = n // k
    r = n % k

    ### NOTE !!!
    #   r      parts have value q + 1   (the remainder is spread 1 unit each)
    #   k - r  parts have value q
    product = 1

    ### NOTE !!! build the product from q, r
    for _ in range(r):
        product *= (q + 1)

    for _ in range(k - r):
        product *= q

    return product


# one-liner form (same thing, via pow)
def get_product(n, k):
    q, r = divmod(n, k)
    return (q + 1) ** r * q ** (k - r)


# the parts themselves (when you need the list, not the product)
def split_evenly(n, k):
    q, r = divmod(n, k)
    return [q + 1] * r + [q] * (k - r)
```

```java
// java
// GENERAL PATTERN: split n into k parts as evenly as possible
long getProduct(int n, int k) {
    int q = n / k;          // 商數
    int r = n % k;          // 餘數

    long product = 1;
    for (int i = 0; i < r; i++)     product *= (q + 1);   // r parts of q+1
    for (int i = 0; i < k - r; i++) product *= q;         // k-r parts of q
    return product;
}
```

##### Applying it — LC 343 Integer Break

```python
# python
# LC 343. Integer Break
# IDEA: MATH — for each k, split evenly (divmod); take max over all k
# time = O(n^2), space = O(1)
class Solution(object):
    def integerBreak(self, n):
        # edge case: k >= 2 is FORCED, so 2 must become 1 + 1
        if n == 2:
            return 1

        max_product = 1

        # try every possible number of parts
        for k in range(2, n + 1):
            max_product = max(max_product, self.get_product(n, k))

        return max_product

    def get_product(self, n, k):
        q, r = n // k, n % k
        # r parts of (q+1),  k-r parts of q
        product = 1
        for _ in range(r):
            product *= (q + 1)
        for _ in range(k - r):
            product *= q
        return product
```

> **NOTE on the loop bound:** `range(2, n + 1)` is the safe/complete range (`k = n` → all 1s).
> `range(2, n)` also passes here because the all-1s product is `1`, which never beats
> `max_product`'s initial value of `1`. But `k <= n` is the correct general bound —
> `k > n` is impossible since every part must be `>= 1`.

##### Why the answer is "as many **3**s as possible" — the O(log n) shortcut

The `divmod` sweep above tells you *how* to split for a given `k`; a bit of calculus tells you *which part size* wins, so you can skip the loop entirely.

```text
Splitting n into parts of size x gives n/x parts -> product = x^(n/x)
Maximize  f(x) = x^(1/x)   ->   maximum at x = e ≈ 2.718

Integer candidates around e:
  2^(1/2) ≈ 1.414
  3^(1/3) ≈ 1.442   <-- WINNER
  4^(1/4) ≈ 1.414   (== 2, since 4 = 2+2 -> 2*2 = 4)

Also, by direct comparison:
  x >= 5  ->  3 * (x - 3) > x     -> ALWAYS split a part of size >= 5
  x == 4  ->  2 * 2 == 4          -> splitting 4 is neutral
  part == 1 is pure waste (multiplying by 1 does nothing)

=> every part in the optimum is a 3 or a 2, and we prefer 3s.
```

**Careful with the remainder** — never leave a `1` hanging:

```text
n % 3 == 0  ->  3^(n/3)                    e.g. 9  = 3+3+3      -> 27
n % 3 == 2  ->  3^(n/3) * 2                e.g. 11 = 3+3+3+2    -> 54
n % 3 == 1  ->  3^(n/3 - 1) * 4            e.g. 10 = 3+3+4      -> 36
                ^^^^^^^^^^^^^^^ borrow one 3 back and use 2+2,
                because 3*1 = 3  <  2*2 = 4
```

```python
# python
# LC 343 - O(log n) math solution
# time = O(log n) (pow), space = O(1)
class Solution(object):
    def integerBreak(self, n):
        # n = 2 -> 1,  n = 3 -> 2  (forced to split, so we lose)
        if n < 4:
            return n - 1

        if n % 3 == 0:
            return 3 ** (n // 3)
        elif n % 3 == 2:
            return 3 ** (n // 3) * 2
        else:                      # n % 3 == 1 -> use 2*2 instead of 3*1
            return 3 ** (n // 3 - 1) * 4
```

```python
# python
# LC 343 - O(n) DP solution (safest if you can't recall the math)
# dp[x] = max product of breaking x  (x itself allowed as a "part" for x >= 4)
# time = O(n), space = O(n)
class Solution(object):
    def integerBreak(self, n):
        if n <= 3:
            return n - 1
        dp = [0] * (n + 1)
        dp[2], dp[3] = 2, 3        # NOTE: 3 (not 2), because 3 can stay whole here
        for x in range(4, n + 1):
            dp[x] = max(3 * dp[x - 3], 2 * dp[x - 2])
        return dp[n]
```

##### Solution Comparison

| Approach | Time | Space | Note |
|----------|------|-------|------|
| Brute force `divmod` over all `k` | `O(n²)` | `O(1)` | **Most intuitive**; the `get_product` pattern above |
| DP `dp[x] = max(3*dp[x-3], 2*dp[x-2])` | `O(n)` | `O(n)` | Safe fallback; note `dp[3] = 3` (not 2) |
| Math (as many 3s as possible) | `O(log n)` | `O(1)` | Needs the `n % 3 == 1 → use 4` insight |

##### Common Pitfalls

| Pitfall | Why it breaks | Fix |
|---------|---------------|-----|
| Returning `n` for `n = 2, 3` | `k >= 2` is **mandatory** — you must split | `if n < 4: return n - 1` |
| `n % 3 == 1` → `3^(n//3) * 1` | Multiplying by `1` wastes a unit | Borrow a 3 back: `3^(n//3 - 1) * 4` |
| `dp[3] = 2` in the DP | Inside `dp`, a `3` may stay **whole** as a factor | `dp[3] = 3` (only the final answer is forced to split) |
| Splitting unevenly (`[8,1,1]`) | Violates AM–GM | `divmod` → `r` parts of `q+1`, `k-r` of `q` |
| Java `int` overflow | `3^19` already exceeds `int` for larger `n` | Use `long` / Python big ints |

##### Similar Problems

| Problem | LC# | Key Difference |
|---------|-----|----------------|
| **Integer Break** | **343** | **Max product of an integer partition — the base pattern** |
| Maximize Number of Nice Divisors | 1808 | Same "as many 3s as possible" trick, but answer is `mod 1e9+7` → needs fast pow |
| Maximum Product After K Increments | 2233 | Max product ⇒ keep values **even** — always increment the current min (heap) |
| Minimum Moves to Equal Array Elements II | 462 | Even-out toward the **median**; cost formula instead of simulation |
| Minimize Maximum of Array | 2439 | Spread a prefix evenly → `ceil(prefixSum / count)` |
| Split Array Largest Sum | 410 | Split into `k` parts minimizing the max sum — binary search (parts not free-sized) |
| Capacity To Ship Packages Within D Days | 1011 | Same even-split-under-constraint shape, solved by binary search |
| Maximum Candies Allocated to K Children | 2226 | Binary search on part size; `divmod` counts how many parts fit |
| Divide Array in Sets of K Consecutive Numbers | 1296 | Even grouping, but by value adjacency (greedy + counter) |
| Fair Distribution of Cookies | 2305 | Even split with *fixed* items → backtracking (can't use `divmod`) |
| Distribute Candies Among Children | 2929 | Counting splits, not maximizing product (combinatorics / inclusion–exclusion) |

**Key takeaways (transferable):**
- **Fixed sum + fixed count → maximize product ⇒ make parts equal.** With integers that means `divmod`: `r` parts of `q+1`, `k-r` parts of `q`.
- The exchange argument (`a >= b+2` ⇒ rebalancing strictly improves) is the standard way to *prove* "even is optimal" in an interview.
- The mirror image also holds: to **minimize** a product / sum-of-squares for a fixed sum, make parts as **unequal** as possible; to **minimize the max**, make them as even as possible.
- When only *adjacent* structure matters, look for a closed form (`O(1)`/`O(log n)`) instead of looping — same philosophy as [1-1-6](#1-1-6-greedy-zigzag-construction--closed-form-o1-instead-of-simulation).

## 2) LC Example

### 2-1) Excel Sheet Column Title — LC 168

```python
# 168 Excel Sheet Column Title
# https://leetcode.com/problems/excel-sheet-column-title/discuss/205987/Python-Solution-with-explanation

# V0
# https://www.jianshu.com/p/591d3a2ab45d
class Solution(object):
    def convertToTitle(self, n):
        tar = "ABCDEFGHIJKLMNOPQRSTUVWXYZ"
        res = ""
        while n > 0:
            # why (n-1) ? : idx start from 0
            m = (n-1) % 26
            #result += tar[m]
            res = (tar[m] + res)
            if m == 0:
                # why n=n+1 ? : since there is no 0 residual (m = (n-1) % 26), so we need to "pass" this case
                n = n + 1
            n = (n-1) // 26
        return res
# V0'
class Solution:
    def convertToTitle(self, n):
        """
        :type n: int
        :rtype: str
        """
        d='0ABCDEFGHIJKLMNOPQRSTUVWXYZ'
        res=''
        if n<=26:
            return d[n]
        else:
            while n > 0:
                n,r=divmod(n,26)
                # This is the catcha on this problem where when r==0 as a result of n%26. eg, n=52//26=2, r=52%26=0. 
                #To get 'AZ' as known for 52, n-=1 and r+=26. Same goes to 702.
                if r == 0:
                    n-=1
                    r+=26
                res = d[r] + res
        return res
```

### 2-2) Solve the Equation — LC 640

```python
# LC 640. Solve the Equation
# V0
# IDEA : replace + eval + math
# https://leetcode.com/problems/solve-the-equation/discuss/105362/Simple-2-liner-(and-more)
# eval : The eval() method parses the expression passed to this method and runs python expression (code) within the program.
# -> https://www.runoob.com/python/python-func-eval.html
class Solution(object):
    def solveEquation(self, equation):
        tmp = equation.replace('x', 'j').replace('=', '-(')
        z = eval(tmp + ")" , {'j':1j})
        # print ("equation = " + str(equation))
        # print ("tmp = " + str(tmp))
        # print ("z = " + str(z))
        a, x = z.real, -z.imag
        return 'x=%d' % (a / x) if x else 'No solution' if a else 'Infinite solutions'
```

### 2-3) Roman to Integer — LC 13

```java
// java
// LC 13

// V0-1
// IDEA: MAP + STR OP (fixed by gpt)
public int romanToInt_0_1(String s) {
    if (s == null || s.isEmpty()) {
        return 0;
    }

    Map<Character, Integer> map = new HashMap<>();
    map.put('I', 1);
    map.put('V', 5);
    map.put('X', 10);
    map.put('L', 50);
    map.put('C', 100);
    map.put('D', 500);
    map.put('M', 1000);

    int total = 0;
    int prev = 0;

    /**
     * NOTE !!!
     *
     * loop reversely (from  idx = s.len() - 1)
     */
    for (int i = s.length() - 1; i >= 0; i--) {
        int curr = map.get(s.charAt(i));
        if (curr < prev) {
            total -= curr;
        } else {
            total += curr;
        }
        /**
         * NOTE !!!
         *
         *  set `prev` as curr
         */
        prev = curr;
    }

    return total;
}
```