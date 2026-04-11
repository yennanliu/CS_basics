# Combinatorics & Math Patterns

## Overview
Google interviews frequently test math/combinatorics reasoning — more than other FAANGs. This covers number theory, counting, geometry, and probability patterns common in coding interviews.

### Key Properties
- **When to Use**: Problem involves counting arrangements, modular arithmetic, GCD/LCM, prime numbers, or geometric calculations
- **Google Signal**: Can you derive a formula instead of brute-forcing?

## Pattern 1: Modular Arithmetic

### Basics
```
(a + b) % m = ((a % m) + (b % m)) % m
(a * b) % m = ((a % m) * (b % m)) % m
(a - b) % m = ((a % m) - (b % m) + m) % m   ← add m to avoid negative

Division: (a / b) % m = (a * b^(-1)) % m  where b^(-1) = modular inverse
```

### Modular Exponentiation (Fast Power)
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
```
b^(-1) mod p = b^(p-2) mod p    (Fermat's little theorem)
```

**Classic LC:** LC 1808 (Maximize Number of Nice Divisors), LC 372 (Super Pow)

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

### Sieve of Eratosthenes
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

### Catalan Numbers
```
C(n) = C(2n, n) / (n+1) = (2n)! / ((n+1)! * n!)

C(0)=1, C(1)=1, C(2)=2, C(3)=5, C(4)=14, C(5)=42

Applications:
  - Number of valid parentheses sequences of length 2n
  - Number of unique BSTs with n nodes
  - Number of ways to triangulate a polygon with n+2 sides
```

**Classic LC:** LC 96 (Unique BSTs), LC 22 (Generate Parentheses count)

## Pattern 5: Reservoir Sampling & Random

### Reservoir Sampling (K=1)
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

### Fisher-Yates Shuffle
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

### Weighted Random / Binary Search on Prefix Sum
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

### Convex Hull (Andrew's Monotone Chain)
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
```
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

## Pattern 7: Bit Counting & Number Theory

### Count Divisors
```
Number of divisors of n = product of (e_i + 1) for each prime factor p_i^e_i
Example: 12 = 2² × 3¹ → (2+1)(1+1) = 6 divisors
```

### Euler's Totient
```
φ(n) = count of integers in [1, n] coprime to n
φ(p) = p - 1  for prime p
φ(p^k) = p^k - p^(k-1)
```

### Sum of Digits / Digital Root
```
Digital root of n = 1 + (n-1) % 9  (for n > 0)
```

**Classic LC:** LC 258 (Add Digits), LC 1922 (Count Good Numbers)

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
