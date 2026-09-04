# Bit Manipulation

> **Scope** — How integers are represented in bits (two's complement, fixed width, shifts) and the operations and tricks built on that — masks, XOR identities, lowest set bit, subset enumeration, and bitmask DP.
> **See also**: [bit_manipulation_examples.md](./bit_manipulation_examples.md) — the fourteen worked problems behind these techniques; [math.md](./math.md) — numeric manipulation without bits; [combinatorics_math_patterns.md](./combinatorics_math_patterns.md) — counting; [dp.md](./dp.md) — the wider DP catalogue that bitmask DP belongs to.

## LeetCode Problem Lists

- [Bit Manipulation](https://leetcode.com/problem-list/bit-manipulation/)
- [Bitmask](https://leetcode.com/problem-list/bitmask/)

## Overview

Bit manipulation operates directly on the **binary representation** of integers. Because
each operation is a single CPU instruction, bitwise tricks turn many `O(n)` scans into
`O(1)` arithmetic, and let a small integer act as a compact **set** (bitmask) of up to
32/64 flags.

### Key Properties
- **Time Complexity**: `O(1)` per bit op; `O(number of bits)` ≈ `O(32)` for whole-word scans
- **Space Complexity**: `O(1)` — a mask reuses one integer instead of an array/set
- **Core Idea**: read/flip individual bits with `&` `|` `^` `~` `<<` `>>`; XOR cancels pairs (`a ^ a = 0`)
- **When to Use**: pairing/cancellation problems, counting set bits, subset enumeration
  (bitmask), power-of-two checks, adding without `+`, packing flags into one number

### Quick Reference — the tricks you must memorize ⭐⭐⭐⭐⭐

| Goal | Expression |
| ---- | ---------- |
| Test if `i`-th bit is set | `(x >> i) & 1` |
| Set `i`-th bit | `x \| (1 << i)` |
| Clear `i`-th bit | `x & ~(1 << i)` |
| Toggle `i`-th bit | `x ^ (1 << i)` |
| Lowest set bit (isolate) | `x & -x` |
| Clear lowest set bit | `x & (x - 1)` |
| Is power of two? | `x > 0 && (x & (x - 1)) == 0` |
| Is even? | `(x & 1) == 0` |
| XOR self-cancel | `a ^ a = 0`, `a ^ 0 = a` |

### References
- [LeetCode — Bit Manipulation card](https://leetcode.com/explore/learn/card/bit-manipulation/)
- [bit_manipulation.md](https://github.com/yennanliu/CS_basics/blob/master/doc/bit_manipulation.md)
- [leetcode-easy-bitwise-xor-summary](https://steveyang.blog/2022/07/02/leetcode-easy-bitwise-xor-summary/)
- [bit VS byte VS char](http://web.ntnu.edu.tw/~algo/Bit.html) — bit widths, with Java examples
- [Python operators](https://www.runoob.com/python/python-operators.html) — precedence table

## 0) CS Foundations ⭐⭐⭐⭐⭐

Almost every "clever" bit trick is a direct consequence of **how a machine stores an
integer**. Five facts cover it — learn these and the tricks below stop needing memorisation.

| # | Fact | What it explains |
| - | ---- | ---------------- |
| 1 | A number is digits × **place values**; binary's place values are powers of two | reading and writing binary/hex by hand |
| 2 | An `int` is a **fixed-width 32-bit box** that wraps around | overflow, `MIN_VALUE`, why masks exist |
| 3 | Negatives are stored in **two's complement** | `~x = -x-1`, `x & -x`, `>>` on negatives |
| 4 | Shifting **is** multiplying/dividing by powers of two | `<<`, `>>`, `>>>` and their edge cases |
| 5 | The bits of an int **are** a subset of 32 items | bitmask, subset enumeration, bitmask DP |

### 0-1) Place values, binary, and hex

The value of a base-`X` number is decided by each digit **and its position**
([ref](https://leetcode.com/explore/learn/card/bit-manipulation/669/bit-manipulation-concepts/4494/)):

```text
123.45 (base 10) = 1*10^2 + 2*10^1 + 3*10^0 + 4*10^-1 + 5*10^-2
 720.5 (base 8)  = 7*8^2  + 2*8^1  + 0*8^0  + 5*8^-1
  1011 (base 2)  = 1*2^3  + 0*2^2  + 1*2^1  + 1*2^0    = 8 + 0 + 2 + 1 = 11
```

To read binary, **add the place values that carry a 1**. To write it, halve repeatedly and
record the remainders bottom-up. That is the entire conversion.

**Hex is binary in groups of four.** One hex digit = 4 bits (a *nibble*), so a 32-bit `int`
is exactly 8 hex digits — which is why masks are written that way:

```text
1011 1101  ->  B    D  ->  0xBD  =  11*16 + 13  =  189
                           each nibble maps to one hex digit — no arithmetic needed

0xFFFFFFFF = 32 ones       0x7FFFFFFF = 31 ones = Integer.MAX_VALUE
```

Powers of two are worth knowing cold — they appear both as constraints and as masks:

| `n`   | 4  | 8   | 10   | 16    | 20        | 31           | 32           |
| ----- | -- | --- | ---- | ----- | --------- | ------------ | ------------ |
| `2^n` | 16 | 256 | 1024 | 65536 | ~1 million | 2 147 483 648 | 4 294 967 296 |

<p align="center"><img src="../pic/bit_basic1.png"></p>
<p align="center"><img src="../pic/bit_basic2.png"></p>

### 0-2) Fixed width — an `int` is a 32-bit box

| Java type | Width | Range |
| --------- | ----- | ----- |
| `byte`    | 8 **bits** | `-128 … 127` |
| `char`    | 16 **bits** | `0 … 65535` (unsigned) |
| `int`     | 32 bits | `-2^31 … 2^31 - 1` |
| `long`    | 64 bits | `-2^63 … 2^63 - 1` |

- Bit `i` carries place value `2^i`. In an `int`, **bit 31 is the sign bit**, so the usable
  magnitude is 31 bits — this is why so many solutions loop `for i in 0..31`.
- Anything that leaves the box **wraps silently**: `Integer.MAX_VALUE + 1 == Integer.MIN_VALUE`.
  A problem statement saying "assume the result fits in a 32-bit integer" is telling you this
  is the edge case being tested.
- `1 << 31` is already **negative**. Write `1L << 31` when you want the *value* `2^31`.

### 0-3) Two's complement — how negatives are stored ⭐⭐⭐⭐⭐

**The rule**: `-x` is stored as `~x + 1` — *flip every bit, then add one*. Equivalently,
the pattern for `-x` is the unsigned value `2^32 - x`.

```text
(traces are 8-bit for space; a real int is the same picture, 32 wide)

 5  = 0000 0101
~5  = 1111 1010     flip every bit
-5  = 1111 1011     ... + 1

check:  5 + (-5) = 1 0000 0000  ->  the carry falls out of the box, leaving 0 ✓
```

Why this representation and not a sign bit + magnitude? Because **one adder handles both
signs** — `a + b` is the same circuit whether the operands are positive or negative, and
there is only one zero.

Three facts drop straight out of it:

| Fact | Where it shows up |
| ---- | ----------------- |
| `~x == -x - 1` | rewriting `~` when a language has no unsigned type |
| top bit set ⇔ negative | the `for i in 0..31` bit-column loops |
| `x >> 31` is `0` (non-negative) or `-1` (negative) | branchless `abs`, sign extraction |

**The asymmetry that bites**: the range holds one more negative than positive, so
`Integer.MIN_VALUE` has **no positive twin** — `-Integer.MIN_VALUE` and
`Math.abs(Integer.MIN_VALUE)` are both still `Integer.MIN_VALUE`. That single value is the
hidden test case in LC 29 (Divide Two Integers); handle it before you negate anything.

### 0-4) Why `x & (x - 1)` and `x & -x` work

Both fall out of two's complement. Derive them once and you never have to memorise which
is which:

```text
x       = 0101 1000        lowest set bit is bit 3
x - 1   = 0101 0111        borrowing flips that 1 to 0, and every 0 below it to 1
-x      = 1010 1000        = ~x + 1

               above bit 3     at bit 3    below bit 3
  x vs x-1  :  identical       1 vs 0      complementary
  x vs -x   :  complementary   1 vs 1      both 0

x & (x-1) = 0101 0000      above survives; bit 3 and everything under it AND to 0
x & (-x)  = 0000 1000      above ANDs to 0; only bit 3 survives
```

So `x & (x - 1)` means **"drop the lowest 1"** (loop it → Brian Kernighan's popcount, [§1-3](#1-3-counting-set-bits-population-count))
and `x & -x` means **"keep only the lowest 1"** (also the step rule of a Fenwick tree — see
[binary_indexed_tree.md](./binary_indexed_tree.md)).

### 0-5) Shifts: left, arithmetic right, and logical right

| Op | Name | Shifts in | Effect |
| -- | ---- | --------- | ------ |
| `x << n`  | left shift | zeros on the right | `x * 2^n`; bits pushed off the top are **lost** |
| `x >> n`  | **arithmetic** right shift | copies of the **sign bit** | `floor(x / 2^n)` |
| `x >>> n` | **logical** right shift (Java only) | zeros | treats the pattern as unsigned |

```text
-8 >> 1  = -4            1111 1000 -> 1111 1100    sign preserved
-8 >>> 1 = 2147483644    1111 1000 -> 0111 1100    sign bit treated as just another bit
```

**When you need `>>>`**: any loop that walks all 32 bits of a possibly-negative `int` —
LC 190 (Reverse Bits), LC 191 (Number of 1 Bits), LC 338. With `>>`, a negative number
shifts in 1s forever and `while (x != 0)` never terminates.

Two more rules that surprise people, both verified above:

- **Java masks the shift count to 5 bits**: `1 << 32` is `1 << 0`, i.e. `1` — **not** `0`.
  Shift a `long` (6-bit count), or split the shift.
- **`+` binds tighter than `<<`**: `x << 1 + 2` is `x << 3`. See [§0-7](#0-7-precedence--parenthesise-everything-).

### 0-6) Python is not Java here ⭐⭐⭐⭐⭐

Python's ints are **arbitrary precision** and behave as if they had infinitely many sign
bits. There is no box, so there is nothing to overflow — and no `>>>`, because there is no
top bit to stop at.

| | Java (`int`, 32-bit) | Python (unbounded) |
| --- | --- | --- |
| `1 << 31` | `-2147483648` (it hit the sign bit) | `2147483648` |
| `Integer.MAX_VALUE + 1` | wraps to `MIN_VALUE` | just keeps growing |
| `-1 >> 100` | `-1` | `-1` (infinite sign bits) |
| logical right shift | `x >>> n` | **none** — mask by hand |
| `~5` | `-6` | `-6` (same) |

So a Python loop that relies on 32-bit wrap-around has to **simulate the box**:

```python
# python
MASK    = 0xFFFFFFFF        # keep only the low 32 bits
INT_MAX = 0x7FFFFFFF        # 2^31 - 1

def to_signed(x):
    """read a masked 32-bit pattern back as a signed Python int"""
    return x if x <= INT_MAX else ~(x ^ MASK)
```

This is the whole reason LC 371 (Sum of Two Integers) looks so much worse in Python than in
Java: the carry loop is the same three lines, but every step needs `& MASK` and the result
needs `to_signed`.

> **Rule of thumb**: in Python, iterate `for i in range(32): (x >> i) & 1` rather than
> `while x:` whenever `x` can be negative — the `while` never ends.

### 0-7) Precedence — parenthesise everything ⭐⭐⭐⭐

Tightest to loosest — this chain is the same in C, Java and Python:

```text
~   ->   * / %   ->   + -   ->   << >>   ->   &   ->   ^   ->   |
```

Two things go wrong with it:

```text
x << 1 + 2        parses as  x << (1 + 2)     -> x << 3, not (x << 1) + 2
a ^ b + 1         parses as  a ^ (b + 1)
```

**Comparison is the one place the languages disagree**: C and Java slot `==` *between*
`>>` and `&`, Python puts it *below* `|`. So `x & 1 == 0` means three different things:

| Language | `x & 1 == 0` | Outcome |
| -------- | ------------ | ------- |
| C / C++  | `x & (1 == 0)` → `x & 0` | **silently always 0** |
| Java     | `x & (1 == 0)` → `int & boolean` | compile error (`bad operand types`) |
| Python   | `(x & 1) == 0` | correct — comparison binds *looser* than `&` here |

Never rely on which one you are in. **Write `(x & 1) == 0`.**

### 0-8) A bitmask *is* a set

The final foundation: subsets of `n` items are in **one-to-one correspondence** with the
integers `0 … 2^n - 1`. Once you see that, "bitmask" needs no further explanation — every
set operation is one instruction.

| Set language | Bit language |
| ------------ | ------------ |
| `S = {}` / `S = {0..n-1}` | `0` / `(1 << n) - 1` |
| `i ∈ S` | `(mask >> i) & 1` |
| `S ∪ {i}` / `S \ {i}` / toggle `i` | `mask \| (1<<i)` / `mask & ~(1<<i)` / `mask ^ (1<<i)` |
| `A ∪ B` / `A ∩ B` / `A \ B` | `a \| b` / `a & b` / `a & ~b` |
| `A ⊆ B` | `(a & b) == a` |
| `A ∩ B = ∅` | `(a & b) == 0` |
| `\|S\|` | `Integer.bitCount(mask)` / `bin(mask).count("1")` |
| complement within `n` items | `mask ^ ((1 << n) - 1)` |

**Two counting facts** tell you whether a bitmask solution fits the constraints:

- there are `2^n` subsets, so `n ≤ ~20` for an `O(2^n · n)` DP (`2^20 ≈ 10^6`);
- summed over *every* mask, the number of **sub**masks is `3^n`, not `4^n` — which is what
  makes the `sub = (sub - 1) & mask` loop in [§2-1](#2-1-subset-enumeration-lc-78-recap) affordable.

### 0-9) The library helpers — value vs index ⭐⭐⭐

Every language ships these, and reaching for the wrong one is a **silent** bug rather than a
compile error. The split that causes it: some helpers return a **value** (a pattern with one
bit set), others return an **index** (`0…31`). They are never interchangeable.

```text
x = 0b101000  (= 40)

Integer.highestOneBit(x)             = 0b100000 = 32     <- a VALUE
Integer.lowestOneBit(x)              = 0b001000 = 8      <- a VALUE  (exactly x & -x)
Integer.numberOfTrailingZeros(x)     = 3                 <- an INDEX
31 - Integer.numberOfLeadingZeros(x) = 5                 <- an INDEX
```

| Goal | Java | Python |
| ---- | ---- | ------ |
| popcount | `Integer.bitCount(x)` | `x.bit_count()` (3.10+), else `bin(x).count("1")` |
| lowest set bit — **value** | `Integer.lowestOneBit(x)` | `x & -x` |
| lowest set bit — **index** | `Integer.numberOfTrailingZeros(x)` | `(x & -x).bit_length() - 1` |
| highest set bit — **value** | `Integer.highestOneBit(x)` | `1 << (x.bit_length() - 1)` |
| highest set bit — **index** = `floor(log2 x)` | `31 - Integer.numberOfLeadingZeros(x)` | `x.bit_length() - 1` |
| reverse all 32 bits | `Integer.reverse(x)` | none — loop it (LC 190) |
| show the bits | `Integer.toBinaryString(x)` | `bin(x)`, or `format(x, "032b")` |
| parse binary | `Integer.parseInt(s, 2)` | `int(s, 2)` |

**The zero cases split the same way**, and this is the part that bites: the *value* helpers
return `0`, but the *index* helpers return **32**, not `-1`.

```text
Integer.highestOneBit(0)         = 0
Integer.lowestOneBit(0)          = 0
Integer.numberOfTrailingZeros(0) = 32      <- not -1
Integer.numberOfLeadingZeros(0)  = 32      <- not -1
```

So `1 << Integer.numberOfTrailingZeros(0)` is `1 << 32`, which Java masks back to **`1`**
([§0-5](#0-5-shifts-left-arithmetic-right-and-logical-right)) — a wrong answer, not a crash.
**Guard `x == 0` before any index helper.**

**Python's log2 is `bit_length()`, not `math.log2`.** Floats carry 53 bits of mantissa, so
the moment an int needs more precision than that, rounding hands you an off-by-one:

```python
# python
# IDEA: floor(log2 x) == x.bit_length() - 1, exactly, for every x > 0
x = (1 << 53) - 1
x.bit_length() - 1        # 52  <- correct
int(math.log2(x))         # 53  <- WRONG: log2 rounded up to 53.0

x = (1 << 64) - 1
x.bit_length() - 1        # 63  <- correct
int(math.log2(x))         # 64  <- WRONG
```

**Know the manual version too.** "Count the set bits *without* `Integer.bitCount`" is not a
trick question — it is the whole of LC 191. The `x &= (x - 1)` loop is in
[§1-3](#1-3-counting-set-bits-population-count); say the library call out loud, then write
the loop.

### 0-10) XOR prefix, and the `0..n` closed form ⭐⭐⭐⭐

XOR is **addition with the carries thrown away** ([§0-4](#0-4-why-x--x---1-and-x---x-work)),
so every prefix-sum technique has an XOR twin — and the XOR one is *simpler*, because XOR is
its own inverse and there is no subtraction step.

#### **Range XOR by prefix**

```text
pre[0]   = 0
pre[i+1] = pre[i] ^ a[i]

a[l] ^ a[l+1] ^ ... ^ a[r]  =  pre[r+1] ^ pre[l]
```

**Why `^` and not `-`**: every element before `l` appears **twice** in `pre[r+1] ^ pre[l]`,
once from each side, so it cancels itself. With sums you must subtract; with XOR the same
operator undoes itself.

```python
# python
# IDEA: pre[i+1] = XOR of a[0..i]; any range XOR is then one operation
# time = O(n) build + O(1) per query, space = O(n)
def build(a):
    pre = [0] * (len(a) + 1)
    for i, v in enumerate(a):
        pre[i + 1] = pre[i] ^ v
    return pre

def range_xor(pre, l, r):        # inclusive [l, r]
    return pre[r + 1] ^ pre[l]
```

#### **XOR of `0..n` in O(1)**

Every **aligned block of four** cancels itself, which collapses the whole prefix to a lookup
on `n % 4`:

```text
4k   = ...00
4k+1 = ...01     (4k) ^ (4k+1)   = 1   <- differ only in bit 0
4k+2 = ...10
4k+3 = ...11     (4k+2) ^ (4k+3) = 1   <- differ only in bit 0

                 1 ^ 1 = 0             <- so each aligned quadruple vanishes
```

Only the tail after the last complete block survives:

| `n % 4` | `0 ^ 1 ^ … ^ n` |
| ------- | --------------- |
| `0` | `n` |
| `1` | `1` |
| `2` | `n + 1` |
| `3` | `0` |

```python
# python
# IDEA: aligned blocks of 4 cancel; only n % 4 decides what is left
# time = O(1), space = O(1)
def xor_to(n):                   # XOR of 0..n  (identical to 1..n, since 0 changes nothing)
    return [n, 1, n + 1, 0][n % 4]

def xor_range(l, r):             # XOR of l..r
    return xor_to(r) ^ xor_to(l - 1)
```

`xor_to` covers `1..n` as well — XOR-ing in `0` changes nothing
([§0-8](#0-8-a-bitmask-is-a-set) has the identity: `a ^ 0 = a`).

**Where it shows up**: any "XOR of a range" query, and as the O(1) replacement for the
`x1 = 1 ^ 2 ^ … ^ n` loop in LC 268 (Missing Number). LC 2683 (Neighboring Bitwise XOR)
is the prefix idea run backwards. *LC 1310 — XOR Queries of a Subarray* is the canonical
prefix-XOR drill; it has no solution in this repo yet.

### 0-11) Branchless idioms ⭐⭐⭐

One expression does most of the work: **`x >> 31` is `0` for a non-negative `int` and `-1`
(all ones) for a negative one** ([§0-3](#0-3-twos-complement--how-negatives-are-stored-)).
An all-ones mask ANDs to *keep*, an all-zeros mask ANDs to *drop* — that is how a branch
becomes arithmetic.

| Goal | Expression | Why it works |
| ---- | ---------- | ------------ |
| do `a` and `b` have **opposite signs**? | `(a ^ b) < 0` | XOR's sign bit is 1 exactly when the two sign bits differ — and no overflow, unlike `a * b < 0` |
| `abs(x)` | `(x ^ (x >> 31)) - (x >> 31)` | `x ≥ 0` → `(x ^ 0) - 0 = x`; `x < 0` → `(x ^ -1) - (-1) = ~x + 1 = -x` |
| sign of `x` as `0` / `-1` | `x >> 31` | the sign bit smeared across all 32 |
| is `x` a multiple of `2^k`? | `(x & ((1 << k) - 1)) == 0` | the low `k` bits are the remainder |
| round `x` down to a multiple of `2^k` | `x & ~((1 << k) - 1)` | clear the remainder bits |

**`abs` inherits the `MIN_VALUE` trap, exactly like `Math.abs`**: both return
`Integer.MIN_VALUE` unchanged, because it has no positive twin
([§0-3](#0-3-twos-complement--how-negatives-are-stored-)). Branchless does not rescue you here.

#### **The one to never ship — XOR swap**

```java
// java
// IDEA: swap without a temporary. Correct ONLY when the two operands are distinct.
a ^= b;  b ^= a;  a ^= b;
```

It is the classic "look, no temp variable" answer, and it **destroys the value when the two
operands alias**:

```text
swap(arr, i, j) with i == j, arr[i] = 7

arr[i] ^= arr[j]   ->  7 ^ 7 = 0     both names point at the same slot
arr[j] ^= arr[i]   ->  0 ^ 0 = 0
arr[i] ^= arr[j]   ->  0 ^ 0 = 0     the 7 is gone
```

Any partition step that can call `swap(i, i)` — several standard quicksort and Dutch-flag
partitions do — is silently zeroed by it. **Use a temporary.** The extra variable was never
the bottleneck, and no interviewer has ever awarded a point for removing it.

### 0-12) Derive it yourself

The point of §0 is that these are **consequences**, not vocabulary. Cover the right-hand
column and rebuild each one from two's complement and place values; if you can do that, you
can reconstruct any trick in this file after forgetting it.

| Expression | What it does, and where it comes from |
| ---------- | ------------------------------------- |
| `x & (x - 1)` | **clears the lowest set bit** — `x-1` borrows through the trailing zeros, so at and below that bit the two disagree |
| `x & -x` | **isolates the lowest set bit** — `-x = ~x + 1`, so above that bit the two are complementary |
| `x & (x + 1)` | **clears the trailing ones** — the mirror of `x & (x - 1)` |
| `x \| (x + 1)` | **sets the lowest clear bit** — carry propagates up to the first `0` |
| `x ^ (x >> 1)` | the **Gray code** of `x` — adjacent codes differ in one bit (LC 89) |
| `x & (x >> 1)` | non-zero ⇔ `x` has **two adjacent 1s** |
| `(x >> i) & 1` | reads bit `i` — shift it down to position 0, mask off the rest |
| `x ^ ((1 << n) - 1)` | **complement within `n` bits** — XOR against a field of ones flips each |
| `(x & (x - 1)) == 0` | `x` is a **power of two** *or zero* — one set bit at most; add `x > 0` |
| `x >> 31` | `0` or `-1` — the sign bit smeared to full width |

Two rules for using this table honestly: **say what the expression does before you check**,
and **name the input that would break it**. `(x & (x - 1)) == 0` accepting `0` is the single
most common answer people get half-right.

## 1) Core Operations

### 1-1) The 6 operators

| Op | Name | Rule | Example (4-bit) |
| -- | ---- | ---- | --------------- |
| `&`  | AND | 1 only if **both** bits 1 | `0110 & 1010 = 0010` |
| `\|`  | OR  | 1 if **either** bit 1 | `0110 \| 1010 = 1110` |
| `^`  | XOR | 1 if bits **differ** | `0110 ^ 1010 = 1100` |
| `~`  | NOT | flip every bit (`~x = -x - 1`) | `~0110 = ...1001` |
| `<<` | left shift | append `n` zeros → `x * 2^n` | `0011 << 1 = 0110` |
| `>>` | right shift | drop `n` low bits → `x // 2^n` | `0110 >> 1 = 0011` |

> **XOR identities** (the heart of many LC problems): `a ^ a = 0`, `a ^ 0 = a`,
> XOR is **commutative & associative** → XOR-ing a whole list cancels every value that
> appears an even number of times, leaving only the odd-count one.

### 1-2) Single-bit tricks (with code)

```java
// java
int  testBit(int x, int i)  { return (x >> i) & 1; }   // 1 if bit i is set, else 0
int  setBit(int x, int i)   { return x | (1 << i); }   // force bit i to 1
int  clearBit(int x, int i) { return x & ~(1 << i); }  // force bit i to 0
int  toggleBit(int x, int i){ return x ^ (1 << i); }   // flip bit i
int  lowestSetBit(int x)    { return x & -x; }         // isolate lowest 1-bit
int  clearLowestBit(int x)  { return x & (x - 1); }    // turn OFF lowest 1-bit
```

```python
# python
def test_bit(x, i):    return (x >> i) & 1     # 1 if bit i is set, else 0
def set_bit(x, i):     return x | (1 << i)     # force bit i to 1
def clear_bit(x, i):   return x & ~(1 << i)    # force bit i to 0
def toggle_bit(x, i):  return x ^ (1 << i)     # flip bit i
def lowest_set_bit(x): return x & -x           # isolate lowest 1-bit
def clear_lowest(x):   return x & (x - 1)      # turn OFF lowest 1-bit
```

### 1-3) Counting set bits (population count)

**Key Idea**: `x & (x - 1)` clears the lowest set bit (why: [§0-4](#0-4-why-x--x---1-and-x---x-work)), so the loop
runs **once per 1-bit** (Brian Kernighan's algorithm) → `O(popcount)` instead of `O(32)`.

```java
// java
// IDEA: each `x &= (x - 1)` removes exactly one set bit
public int countBits(int x) {
    int count = 0;
    while (x != 0) {
        x &= (x - 1);   // clear lowest set bit
        count++;
    }
    return count;
    // built-in: Integer.bitCount(x)
}
```

```python
# python
# IDEA: each `x &= (x - 1)` removes exactly one set bit
def count_bits(x):
    count = 0
    while x:
        x &= (x - 1)    # clear lowest set bit
        count += 1
    return count
    # built-in: bin(x).count("1")
```

**Visual trace** — `count_bits(12)`, `12 = 1100`:

```text
x = 1100   x & (x-1) = 1100 & 1011 = 1000   count = 1
x = 1000   x & (x-1) = 1000 & 0111 = 0000   count = 2
x = 0000   stop                              → 2 set bits
```

### 1-4) Counting over bit COLUMNS — LC 461 / LC 477

**Pattern**: instead of looping over pairs of numbers, loop over the **32 bit positions** and
ask what each column contributes. This turns many `O(n^2)`-looking problems into `O(32n)`.

**Key Idea**: at bit position `i`, if `ones` numbers have that bit set and `n - ones` do not,
then exactly `ones * (n - ones)` **pairs differ** at that bit. Sum over all 32 positions.

```java
// java
// LC 461 - Hamming Distance (the 2-number base case)
// IDEA: differing bits of x and y are exactly the set bits of x ^ y
// time = O(popcount), space = O(1)
class Solution {
    public int hammingDistance(int x, int y) {
        int diff = x ^ y, count = 0;
        while (diff != 0) {
            diff &= (diff - 1);   // clear lowest set bit
            count++;
        }
        return count;             // built-in: Integer.bitCount(x ^ y)
    }
}

// LC 477 - Total Hamming Distance (all pairs)
// IDEA: per bit column, ones * (n - ones) pairs differ there
// time = O(32 * N), space = O(1)
class Solution2 {
    public int totalHammingDistance(int[] nums) {
        int n = nums.length, total = 0;
        for (int i = 0; i < 32; i++) {
            int ones = 0;
            for (int x : nums) ones += (x >> i) & 1;   // count 1s in column i
            total += ones * (n - ones);                // each 1 pairs with each 0
        }
        return total;
    }
}
```

```python
# python
# LC 461 - Hamming Distance
# time = O(popcount), space = O(1)
class Solution(object):
    def hammingDistance(self, x, y):
        diff, count = x ^ y, 0
        while diff:
            diff &= diff - 1          # clear lowest set bit
            count += 1
        return count                  # built-in: bin(x ^ y).count("1")


# LC 477 - Total Hamming Distance
# IDEA: per bit column, ones * (n - ones) pairs differ there
# time = O(32 * N), space = O(1)
class Solution2(object):
    def totalHammingDistance(self, nums):
        n, total = len(nums), 0
        for i in range(32):
            ones = sum((x >> i) & 1 for x in nums)   # count 1s in column i
            total += ones * (n - ones)
        return total
```

**Why it works** — `[4, 14, 2]` = `00100, 01110, 00010`:

```text
bit column :  0     1     2     3
ones       :  0     2     2     1        n = 3
zeros      :  3     1     1     2
pairs      : 0*3   2*1   2*1   1*2  ->  0 + 2 + 2 + 2 = 6
```


### 1-5) A bitmask as a CHARACTER SET — LC 318 ⭐⭐⭐⭐

**Pattern**: a lowercase-letter set fits in **26 bits**, so a whole word becomes ONE `int`.
Then set questions become single instructions:

| Set question | Bit expression |
| ------------ | -------------- |
| do two words share a letter? | `(maskA & maskB) != 0` |
| are they disjoint? | `(maskA & maskB) == 0` |
| union of the letters | `maskA \| maskB` |
| how many distinct letters? | `Integer.bitCount(mask)` / `bin(mask).count("1")` |
| does the word repeat a letter? | while building: `(mask & bit) != 0` |

This replaces a per-pair `O(len)` string comparison with an `O(1)` AND.

```java
// java
// LC 318 - Maximum Product of Word Lengths
// IDEA: encode each word's letters as a 26-bit mask; two words share no letter iff (mA & mB) == 0
// time = O(N * L + N^2), space = O(N)
class Solution {
    public int maxProduct(String[] words) {
        int n = words.length;
        int[] mask = new int[n];
        for (int i = 0; i < n; i++) {
            for (char c : words[i].toCharArray()) {
                mask[i] |= 1 << (c - 'a');       // add letter c to the set
            }
        }
        int best = 0;
        for (int i = 0; i < n; i++) {
            for (int j = i + 1; j < n; j++) {
                if ((mask[i] & mask[j]) == 0) {  // disjoint letter sets
                    best = Math.max(best, words[i].length() * words[j].length());
                }
            }
        }
        return best;
    }
}
```

```python
# python
# LC 318 - Maximum Product of Word Lengths
# IDEA: encode each word's letters as a 26-bit mask; disjoint iff (mA & mB) == 0
# time = O(N * L + N^2), space = O(N)
class Solution(object):
    def maxProduct(self, words):
        masks = []
        for w in words:
            m = 0
            for c in w:
                m |= 1 << (ord(c) - ord('a'))    # add letter c to the set
            masks.append(m)

        best = 0
        for i in range(len(words)):
            for j in range(i + 1, len(words)):
                if masks[i] & masks[j] == 0:     # no shared letter
                    best = max(best, len(words[i]) * len(words[j]))
        return best
```

#### **Variation A — build up a union of disjoint masks (LC 1239)**

*Twist*: instead of picking only **two** disjoint words, greedily grow **every** reachable
union. Keep a list of achievable masks; a word can join a mask only if `cur & m == 0`.

```java
// java
// LC 1239 - Maximum Length of a Concatenated String with Unique Characters
// IDEA: keep every reachable "union of disjoint words" mask; answer = max popcount
// time = O(2^N * 26), space = O(2^N)
class Solution {
    public int maxLength(List<String> arr) {
        List<Integer> masks = new ArrayList<>();
        masks.add(0);                                  // empty selection
        int best = 0;
        for (String s : arr) {
            int m = 0;
            boolean dup = false;
            for (char c : s.toCharArray()) {
                int bit = 1 << (c - 'a');
                if ((m & bit) != 0) { dup = true; break; }   // word itself repeats a letter
                m |= bit;
            }
            if (dup) continue;
            // iterate BACKWARDS over the snapshot so newly added masks aren't reused this round
            for (int i = masks.size() - 1; i >= 0; i--) {
                int cur = masks.get(i);
                if ((cur & m) != 0) continue;          // overlap -> can't concatenate
                masks.add(cur | m);
                best = Math.max(best, Integer.bitCount(cur | m));
            }
        }
        return best;
    }
}
```

```python
# python
# LC 1239 - Maximum Length of a Concatenated String with Unique Characters
# IDEA: keep every reachable "union of disjoint words" mask; answer = max popcount
# time = O(2^N * 26), space = O(2^N)
class Solution(object):
    def maxLength(self, arr):
        masks, best = [0], 0                 # 0 = empty selection
        for s in arr:
            m, dup = 0, False
            for c in s:
                bit = 1 << (ord(c) - ord('a'))
                if m & bit:                  # word itself repeats a letter
                    dup = True
                    break
                m |= bit
            if dup:
                continue
            for cur in list(masks):          # snapshot, so this word is used at most once
                if cur & m:                  # overlap -> can't concatenate
                    continue
                masks.append(cur | m)
                best = max(best, bin(cur | m).count("1"))
        return best
```

#### **Variation B — pack fixed-width symbols into a rolling int key (LC 187)**

*Twist*: the alphabet is only 4 symbols (`A C G T`), so each char needs **2 bits** and a
10-char window is a 20-bit integer. Slide the window with `hash = ((hash << 2) | code) & mask`
— an `O(1)` rolling key instead of hashing a 10-char substring every step.

```java
// java
// LC 187 - Repeated DNA Sequences
// IDEA: 2 bits per base -> a 10-char window is one 20-bit int; roll it with shift + mask
// time = O(N), space = O(N)
class Solution {
    public List<String> findRepeatedDnaSequences(String s) {
        int L = 10, n = s.length();
        List<String> res = new ArrayList<>();
        if (n <= L) return res;

        int mask = (1 << (2 * L)) - 1;          // keep only the low 20 bits
        int hash = 0;
        Map<Integer, Integer> seen = new HashMap<>();
        for (int i = 0; i < n; i++) {
            hash = ((hash << 2) | "ACGT".indexOf(s.charAt(i))) & mask;  // push new base, drop oldest
            if (i >= L - 1) {
                int c = seen.getOrDefault(hash, 0) + 1;
                seen.put(hash, c);
                if (c == 2) res.add(s.substring(i - L + 1, i + 1));     // report once
            }
        }
        return res;
    }
}
```

```python
# python
# LC 187 - Repeated DNA Sequences
# IDEA: 2 bits per base -> a 10-char window is one 20-bit int; roll it with shift + mask
# time = O(N), space = O(N)
class Solution(object):
    def findRepeatedDnaSequences(self, s):
        L, n = 10, len(s)
        if n <= L:
            return []
        code = {'A': 0, 'C': 1, 'G': 2, 'T': 3}
        mask = (1 << (2 * L)) - 1               # keep only the low 20 bits
        h, seen, res = 0, {}, []
        for i, c in enumerate(s):
            h = ((h << 2) | code[c]) & mask     # push new base, drop the oldest
            if i >= L - 1:
                seen[h] = seen.get(h, 0) + 1
                if seen[h] == 2:                # report each repeat exactly once
                    res.append(s[i - L + 1:i + 1])
        return res
```

> **More letter-mask practice** (same 26-bit encoding, no new technique):
> LC 1255 (Maximum Score Words Formed by Letters), LC 2135 (Count Words Obtained After
> Adding a Letter), LC 1684 (Count the Number of Consistent Strings — `word & ~allowed == 0`).


## 2) Bitmask DP

A **bitmask** lets an integer represent a **set of visited/chosen items** (bit `i` set ⇔ item
`i` in the set). When a DP state needs "which subset of ≤ ~20 items have I used", the mask
**is** the state — enabling exponential subset problems to run in `O(2^n · n)`.

### 2-1) Subset enumeration (LC 78 recap)

Iterating `mask` from `0` to `2^n − 1` visits **every** subset exactly once; bit tests pick
members (see [§12 of the worked examples](./bit_manipulation_examples.md#12-subsets--lc-78--the-bitmask-enumeration-)). Handy mask idioms:

```python
# python
mask & (1 << i)          # is item i in the subset?
mask | (1 << i)          # add item i
mask & ~(1 << i)         # remove item i
bin(mask).count("1")     # size of the subset
sub = (sub - 1) & mask   # enumerate all SUB-masks of `mask` (classic trick)
```

### 2-2) TSP-style bitmask DP (Held–Karp)

The **Travelling Salesman** family is the canonical bitmask DP: `dp[mask][i]` = min cost of a
path that has **visited exactly the cities in `mask`** and currently sits at city `i`.

```text
state : dp[mask][i]      mask = set of visited cities, i = current city
trans : dp[mask | (1<<j)][j] = min( dp[mask][i] + dist[i][j] )   for j not in mask
answer: min over i of dp[FULL][i] (+ dist[i][start] for a cycle)
time  : O(2^n · n^2)     space : O(2^n · n)
```

```java
// java
// Held–Karp TSP skeleton: dp[mask][i] = min cost visiting `mask`, ending at city i
int tsp(int[][] dist) {
    int n = dist.length, FULL = (1 << n) - 1;
    int[][] dp = new int[1 << n][n];
    for (int[] row : dp) Arrays.fill(row, Integer.MAX_VALUE / 2);
    dp[1][0] = 0;                             // start at city 0, only it visited
    for (int mask = 1; mask <= FULL; mask++) {
        for (int i = 0; i < n; i++) {
            if ((mask & (1 << i)) == 0) continue;         // i must be in mask
            for (int j = 0; j < n; j++) {
                if ((mask & (1 << j)) != 0) continue;     // j must NOT be visited yet
                int next = mask | (1 << j);
                dp[next][j] = Math.min(dp[next][j], dp[mask][i] + dist[i][j]);
            }
        }
    }
    int ans = Integer.MAX_VALUE;
    for (int i = 0; i < n; i++) ans = Math.min(ans, dp[FULL][i] + dist[i][0]); // close cycle
    return ans;
}
```

> **When to reach for bitmask DP**: `n` is small (≤ ~20 so `2^n` is tractable) and the state
> is "which subset have I used/visited". Related LC: 847 (Shortest Path Visiting All Nodes),
> 1349 (Maximum Students Taking Exam), 691 (Stickers to Spell Word), 526 (Beautiful Arrangement).

### 2-3) "Fill buckets one at a time" bitmask DP — LC 698 ⭐⭐⭐⭐⭐

**Pattern**: partition-into-`k`-equal-groups problems look like they need `k` nested searches.
The trick is to **stop tracking which bucket** you are filling and only track:

```text
state : dp[mask] = how full the CURRENT bucket is, given `mask` items are already placed
        (-1 = mask unreachable)
key   : sum(mask) is fixed by the mask, so the bucket index is implied —
        every time the running bucket hits `target` it wraps to 0 and a new bucket starts
trans : dp[mask | (1<<i)] = (dp[mask] + nums[i]) % target,  allowed iff dp[mask] + nums[i] <= target
answer: dp[FULL] == 0   (all items used AND the last bucket closed exactly)
time  : O(2^n · n)      space : O(2^n)
```

**Key Idea**: `% target` is what makes the "start the next bucket" transition free — no extra
state dimension for the bucket counter.

**Pruning that matters**: sort `nums` ascending, then `break` (not `continue`) as soon as
`dp[mask] + nums[i] > target` — every later item is larger and also fails.

```java
// java
// LC 698 - Partition to K Equal Sum Subsets
// IDEA: dp[mask] = fill level of the current bucket; % target rolls over to the next bucket
// time = O(2^n * n), space = O(2^n)
class Solution {
    public boolean canPartitionKSubsets(int[] nums, int k) {
        int sum = 0;
        for (int x : nums) sum += x;
        if (sum % k != 0) return false;                 // can't split evenly
        int target = sum / k, n = nums.length;

        Arrays.sort(nums);                              // ascending -> enables the `break` prune
        if (nums[n - 1] > target) return false;         // one item already overflows a bucket

        int FULL = (1 << n) - 1;
        int[] dp = new int[1 << n];
        Arrays.fill(dp, -1);                            // -1 = state not reachable
        dp[0] = 0;                                      // nothing placed, empty bucket

        for (int mask = 0; mask <= FULL; mask++) {
            if (dp[mask] < 0) continue;                 // unreachable
            for (int i = 0; i < n; i++) {
                if ((mask & (1 << i)) != 0) continue;   // item i already used
                if (dp[mask] + nums[i] > target) break; // sorted -> all later items fail too
                int next = mask | (1 << i);
                if (dp[next] < 0) {
                    dp[next] = (dp[mask] + nums[i]) % target;  // == 0 -> bucket closed, start next
                }
            }
        }
        return dp[FULL] == 0;   // every item used and the final bucket landed exactly on target
    }
}
```

```python
# python
# LC 698 - Partition to K Equal Sum Subsets
# IDEA: dp[mask] = fill level of the current bucket; % target rolls over to the next bucket
# time = O(2^n * n), space = O(2^n)
class Solution(object):
    def canPartitionKSubsets(self, nums, k):
        total = sum(nums)
        if total % k:                       # can't split evenly
            return False
        target, n = total // k, len(nums)

        nums.sort()                         # ascending -> enables the `break` prune
        if nums[-1] > target:               # one item already overflows a bucket
            return False

        FULL = (1 << n) - 1
        dp = [-1] * (1 << n)                # -1 = state not reachable
        dp[0] = 0                           # nothing placed, empty bucket

        for mask in range(FULL + 1):
            if dp[mask] < 0:
                continue
            for i in range(n):
                if mask & (1 << i):         # item i already used
                    continue
                if dp[mask] + nums[i] > target:
                    break                   # sorted -> all later items fail too
                nxt = mask | (1 << i)
                if dp[nxt] < 0:
                    dp[nxt] = (dp[mask] + nums[i]) % target   # 0 -> bucket closed
        return dp[FULL] == 0
```

**Visual trace** — `nums = [1,2,2,3]` (sorted), `k = 2`, `target = 4`.
Bit `i` = `nums[i]` used; only the winning path is shown (the loop also fills the other masks):

```text
mask 0000  dp=0    place nums[0]=1 -> dp[0001] = 1
mask 0001  dp=1    place nums[3]=3 -> dp[1001] = (1+3) % 4 = 0   (bucket closed!)
mask 0011  dp=3    place nums[2]=2 -> 3+2 = 5 > 4 -> break        (dead branch)
mask 1001  dp=0    place nums[1]=2 -> dp[1011] = 2
mask 1011  dp=2    place nums[2]=2 -> dp[1111] = (2+2) % 4 = 0
                                     dp[FULL] == 0 -> TRUE  ([1,3] and [2,2])
```

#### **Variation — same template, `k` hard-coded (LC 473)**

*Twist*: LC 473 (Matchsticks to Square) **is** LC 698 with `k = 4`; nothing else changes.

#### **Variation — mask as a GAME state instead of a DP table (LC 464)**

*Twist*: for LC 464 (Can I Win) the mask is "which of the `1..maxChoosable` numbers are already
taken", and the recursion is minimax rather than a cost: `win(mask)` is `true` if **any** unused
`i` either reaches the total immediately or leaves the opponent in a losing state
`!win(mask | (1 << (i-1)))`. Memoize on `mask` alone — the remaining total is implied by it.
Prune first with `maxChoosable * (maxChoosable + 1) / 2 < desiredTotal` → nobody can win.

> **More bitmask-DP practice**: LC 1125 (Smallest Sufficient Team — set cover, `dp[skillMask]`),
> LC 980 (Unique Paths III — mask of visited cells), LC 864 (Shortest Path to Get All Keys —
> BFS state = `(cell, keyMask)`), LC 1494 (Parallel Courses II — iterate **submasks** of the
> currently-available course set with `sub = (sub - 1) & mask`).

> **Not bitmask DP, but bit-adjacent**: LC 421 (Maximum XOR of Two Numbers in an Array) and
> LC 1707 (Maximum XOR With an Element From Array) are solved with a **binary/XOR trie** —
> see `trie.md` rather than duplicating it here.

## Worked Examples

Fourteen problems live in **[bit_manipulation_examples.md](./bit_manipulation_examples.md)**,
grouped by which property of the bit operators they lean on:

| Group | The property | Problems |
|---|---|---|
| [XOR — cancelling pairs](./bit_manipulation_examples.md#xor--cancelling-pairs) | `x ^ x == 0`, so anything paired disappears | LC 136, 137, 260, 268 |
| [Counting & transforming bits](./bit_manipulation_examples.md#counting--transforming-bits) | `x & (x-1)` clears the lowest set bit | LC 191, 338, 190, 231 |
| [Arithmetic without arithmetic](./bit_manipulation_examples.md#arithmetic-without-arithmetic) | XOR is addition without carry; AND finds the carry | LC 371, 67, 29 |
| [Enumerating & constructing](./bit_manipulation_examples.md#enumerating-and-constructing-with-bits) | an integer *is* a subset, and counting up visits every one | LC 78, 89, 201 |

## Summary

### Pick the technique from the question

| The problem says… | Reach for | Section |
| ----------------- | --------- | ------- |
| "every element appears twice except one" | XOR the whole array | [XOR — cancelling pairs](./bit_manipulation_examples.md#xor--cancelling-pairs) |
| "count the 1 bits" / "for every `i` in `0..n`" | `x & (x-1)` loop, or DP on `i >> 1` | [§1-3](#1-3-counting-set-bits-population-count) |
| "sum over all pairs" of a bit property | loop the 32 **bit columns**, not the pairs | [§1-4](#1-4-counting-over-bit-columns--lc-461--lc-477) |
| lowercase words, "share a letter" / "unique characters" | a 26-bit letter mask | [§1-5](#1-5-a-bitmask-as-a-character-set--lc-318-) |
| a small fixed alphabet + a sliding window | pack `k` bits per symbol, roll with `<<` and a mask | [Variation B](#variation-b--pack-fixed-width-symbols-into-a-rolling-int-key-lc-187) |
| "choose a subset", `n ≤ ~20` | `dp[mask]`, bitmask DP | [§2](#2-bitmask-dp) |
| "partition into `k` equal groups" | `dp[mask]` = fill level, `% target` | [§2-3](#2-3-fill-buckets-one-at-a-time-bitmask-dp--lc-698-) |
| "XOR of a subarray" / "XOR of `1..n`", asked repeatedly | XOR prefix array, or the `n % 4` closed form | [§0-10](#0-10-xor-prefix-and-the-0n-closed-form-) |
| "add / divide without `+` or `/`" | XOR = sum, AND = carry | [Arithmetic without arithmetic](./bit_manipulation_examples.md#arithmetic-without-arithmetic) |
| "maximum XOR of two numbers" | binary trie — see [trie.md](./trie.md) | — |

### The five bugs that fail a bit-manipulation submission

1. **`while (x != 0) x >>= 1` on a negative `int`** — arithmetic shift feeds in 1s forever.
   Use `>>>` in Java, or `for i in range(32)` in Python. ([§0-5](#0-5-shifts-left-arithmetic-right-and-logical-right), [§0-6](#0-6-python-is-not-java-here-))
2. **Missing parentheses** — write `(x & 1) == 0`, never `x & 1 == 0`. ([§0-7](#0-7-precedence--parenthesise-everything-))
3. **`Integer.MIN_VALUE` has no positive twin** — `Math.abs` and unary `-` both return it
   unchanged, so negate-then-divide silently breaks. ([§0-3](#0-3-twos-complement--how-negatives-are-stored-))
4. **`1 << i` overflows at `i >= 31`** — use `1L << i` (and remember Java masks the shift
   count, so `1 << 32 == 1`). ([§0-2](#0-2-fixed-width--an-int-is-a-32-bit-box), [§0-5](#0-5-shifts-left-arithmetic-right-and-logical-right))
5. **Porting a 32-bit loop to Python unchanged** — Python never overflows, so every step
   needs `& 0xFFFFFFFF` and the result needs converting back. ([§0-6](#0-6-python-is-not-java-here-))
