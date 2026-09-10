# Bit Manipulation — Worked Examples

> **Scope** — The worked-solution archive behind [bit_manipulation.md](./bit_manipulation.md): nineteen problems grouped by which property of the bit operators they lean on — XOR cancelling pairs, clearing the lowest set bit, carry-free arithmetic, an integer standing in for a subset, or a hand-built mask editing a bit field.
> **See also**: [bit_manipulation.md](./bit_manipulation.md) — the parent sheet: the operators, the single-bit tricks, counting over bit columns, the bitmask-as-character-set technique, and bitmask DP; [dp_bitmask.md](./dp_bitmask.md) — subset DP in its own right; [math.md](./math.md) — the arithmetic these problems avoid using.

## LeetCode Problem Lists

- [Bit Manipulation](https://leetcode.com/problem-list/bit-manipulation/)

## Overview

This is the long tail of [bit_manipulation.md](./bit_manipulation.md), which was 88% example
tail — the highest share in Tier 3 after `binary_indexed_tree`. The parent keeps the operators
and the techniques; this file keeps the problems that *apply* them.

### Key Properties
- **Complexity**: O(1) per number or O(32n) over an array, unless a solution says otherwise — which is the reason to reach for bits at all
- **Core Idea**: five properties do almost all the work, and the groups below are those five
- **When to Use**: when the constraint is O(1) space, no arithmetic operators, or a set small enough to fit in an `int`


## XOR — Cancelling Pairs

### 1) Single Number — LC 136 ⭐⭐⭐⭐⭐


**Key Idea**: XOR every element. Pairs cancel (`a ^ a = 0`), leaving the lone number.

```python
# python
# LC 136 Single Number
# IDEA: XOR all -> duplicates cancel, single element remains
class Solution(object):
    def singleNumber(self, nums):
        res = 0
        for n in nums:
            res ^= n        # a ^ a = 0, a ^ 0 = a
        return res
```

```java
// java
// LC 136 Single Number
// time = O(N), space = O(1)
class Solution {
    public int singleNumber(int[] nums) {
        int res = 0;
        for (int n : nums) res ^= n;   // pairs cancel, single survives
        return res;
    }
}
```

### 2) Single Number II — LC 137 — counting bits mod 3


Every element appears **3 times** except one. Plain XOR fails (it only cancels pairs). Use
**bit-counting mod 3**: for each of the 32 bit positions, count the 1s across all numbers;
`count % 3` is the bit of the unique number.

```python
# python
# LC 137 Single Number II
# IDEA: for each bit position, sum of set bits % 3 = that bit of the answer
class Solution(object):
    def singleNumber(self, nums):
        res = 0
        for i in range(32):
            bit_sum = 0
            for n in nums:
                bit_sum += (n >> i) & 1        # count 1s at position i
            bit = bit_sum % 3                  # 0 or 1 -> the unique number's bit
            if bit:
                res |= (1 << i)
        # handle negative numbers (Python ints are unbounded)
        if res >= 2**31:
            res -= 2**32
        return res
```

```java
// java
// LC 137 Single Number II
// IDEA: count set bits per position mod 3
// time = O(32 * N), space = O(1)
class Solution {
    public int singleNumber(int[] nums) {
        int res = 0;
        for (int i = 0; i < 32; i++) {
            int bitSum = 0;
            for (int n : nums) bitSum += (n >> i) & 1;
            if (bitSum % 3 != 0) res |= (1 << i);   // set bit i in answer
        }
        return res;
    }
}
```

### 3) Single Number III — LC 260 — split by the lowest differing bit


Two numbers appear once, the rest in pairs. XOR of all = `a ^ b` (the two singles). Isolate
**any** differing bit with `xor & -xor`, then split all numbers into two groups by that bit
— each single lands in its own group and XOR within a group recovers it.

```python
# python
# LC 260 Single Number III
# IDEA: XOR all -> a^b; pick a set bit to split nums into 2 groups; XOR each group
class Solution(object):
    def singleNumber(self, nums):
        xor = 0
        for n in nums:
            xor ^= n                 # xor = a ^ b
        diff = xor & (-xor)          # lowest set bit where a, b differ
        a = 0
        for n in nums:
            if n & diff:             # group with that bit set
                a ^= n
        b = xor ^ a
        return [a, b]
```

```java
// java
// LC 260 Single Number III
// time = O(N), space = O(1)
class Solution {
    public int[] singleNumber(int[] nums) {
        int xor = 0;
        for (int n : nums) xor ^= n;      // a ^ b
        int diff = xor & (-xor);          // isolate a differing bit
        int a = 0;
        for (int n : nums) {
            if ((n & diff) != 0) a ^= n;  // group split by that bit
        }
        return new int[]{a, xor ^ a};
    }
}
```

### 4) Missing Number — LC 268


`nums` holds `n` distinct values from `[0, n]` with one missing. XOR all indices `0..n` with
all values — every present number cancels with its index, leaving the missing one.

```python
# python
# LC 268 Missing Number
# IDEA: XOR indices 0..n with all values -> matches cancel, missing survives
class Solution(object):
    def missingNumber(self, nums):
        res = len(nums)              # start with index n (loop below only reaches n-1)
        for i, n in enumerate(nums):
            res ^= i ^ n
        return res
```

```java
// java
// LC 268 Missing Number
// time = O(N), space = O(1)
class Solution {
    public int missingNumber(int[] nums) {
        int res = nums.length;       // include index n
        for (int i = 0; i < nums.length; i++) {
            res ^= i ^ nums[i];      // cancel each index with its value
        }
        return res;
    }
}
```

## Counting & Transforming Bits

### 5) Number of 1 Bits — LC 191


**Key Idea**: `n & (n - 1)` clears the lowest set bit — loop runs once per set bit.

```python
# python
# LC 191 Number of 1 Bits (Hamming weight)
class Solution(object):
    def hammingWeight(self, n):
        count = 0
        while n:
            n &= (n - 1)       # drop lowest set bit
            count += 1
        return count
```

```java
// java
// LC 191 Number of 1 Bits
// time = O(popcount), space = O(1)
public class Solution {
    public int hammingWeight(int n) {
        int count = 0;
        while (n != 0) {
            n &= (n - 1);      // clear lowest set bit
            count++;
        }
        return count;
    }
}
```

### 6) Counting Bits — LC 338 — DP on `i >> 1`


Return `popcount(i)` for every `i` in `[0, n]`. **DP over bits**: `dp[i] = dp[i >> 1] + (i & 1)`
— `i` has the same set bits as `i/2` plus its own lowest bit. `O(n)` overall.

```python
# python
# LC 338 Counting Bits
# IDEA: dp[i] = dp[i >> 1] + (i & 1)
class Solution(object):
    def countBits(self, n):
        dp = [0] * (n + 1)
        for i in range(1, n + 1):
            dp[i] = dp[i >> 1] + (i & 1)   # bits of i/2, plus i's lowest bit
        return dp
```

```java
// java
// LC 338 Counting Bits
// time = O(N), space = O(N)
class Solution {
    public int[] countBits(int n) {
        int[] dp = new int[n + 1];
        for (int i = 1; i <= n; i++) {
            dp[i] = dp[i >> 1] + (i & 1);
        }
        return dp;
    }
}
```

### 7) Reverse Bits — LC 190

```python
# 190. Reverse Bits
# V0
class Solution:
    def reverseBits(self, n):
        s = bin(n)[2:]
        s = "0"*(32 - len(s)) + s
        t = s[::-1]
        return int(t,2)

# V0'
# DEMO
# n = 10100101000001111010011100
# n =       10100101000001111010011100
class Solution:
    def reverseBits(self, n):
        n = bin(n)[2:]         # convert to binary, and remove the usual 0b prefix
        print ("n = " + str(n))
        n = '%32s' % n         # print number into a pre-formatted string with space-padding
        print ("n = " + str(n))
        n = n.replace(' ','0') # Convert the useful space-padding into zeros
        # Now we have a  proper binary representation, so we can make the final transformation
        return int(n[::-1],2)

# V0'' 
class Solution(object):
    def reverseBits(self, n):
        #b = bin(n)[:1:-1]
        b = bin(n)[2:][::-1]
        return int(b + '0'*(32-len(b)), 2)
```

```java
// java
// LC 190 Reverse Bits
// IDEA: pull off the lowest bit of n, push it onto res from the left, 32 times
public class Solution {
    public int reverseBits(int n) {
        int res = 0;
        for (int i = 0; i < 32; i++) {
            res <<= 1;               // make room for the next bit
            res |= (n & 1);          // copy n's lowest bit into res
            n >>>= 1;                // unsigned shift — must NOT sign-extend (LC 190 treats n as unsigned 32-bit)
        }
        return res;
    }
}
```

### 8) Power of Two — LC 231 — `n & (n - 1) == 0`

```python
# LC 231. Power of Two
# NOTE : there is also brute force approach
# V0'
# IDEA : BIT OP
# IDEA : Bitwise operators : Turn off the Rightmost 1-bit
# https://leetcode.com/problems/power-of-two/solution/
class Solution(object):
    def isPowerOfTwo(self, n):
        if n == 0:
            return False
        return n & (n - 1) == 0
```

```java
// java
// LC 231 Power of Two
// IDEA: a power of two has exactly ONE set bit -> x & (x-1) removes it, leaving 0
class Solution {
    public boolean isPowerOfTwo(int n) {
        // n > 0 rules out 0 and negatives (which have set sign bit)
        return n > 0 && (n & (n - 1)) == 0;
    }
}
```

## Arithmetic Without Arithmetic

### 9) Sum of Two Integers — LC 371 — carry via AND, sum via XOR ⭐⭐⭐⭐

```python
# 371. Sum of Two Integers
# V0'
# https://blog.csdn.net/fuxuemingzhu/article/details/79379939
#########
# XOR op:
#########
# https://stackoverflow.com/questions/14526584/what-does-the-xor-operator-do
# XOR is a binary operation, it stands for "exclusive or", that is to say the resulting bit evaluates to one if only exactly one of the bits is set.
# -> XOR : RETURN 1 if only one "1", return 0 else 
# -> XOR extra : Exclusive or or exclusive disjunction is a logical operation that is true if and only if its arguments differ. It is symbolized by the prefix operator J and by the infix operators XOR, EOR, EXOR, ⊻, ⩒, ⩛, ⊕, ↮, and ≢. Wikipedia
# a | b | a ^ b
# --|---|------
# 0 | 0 | 0
# 0 | 1 | 1
# 1 | 0 | 1
# 1 | 1 | 0
# This operation is performed between every two corresponding bits of a number.
# Example: 7 ^ 10
# In binary: 0111 ^ 1010
#   0111
# ^ 1010
# ======
#   1101 = 13
class Solution(object):
    def getSum(self, a, b):
        """
        :type a: int
        :type b: int
        :rtype: int
        """
        # 32 bits integer max
        MAX = 2**31-1  #0x7FFFFFFF
        # 32 bits interger min
        MIN = 2**31    #0x80000000
        # mask to get last 32 bits
        mask = 2**32-1 #0xFFFFFFFF
        while b != 0:
            # ^ get different bits and & gets double 1s, << moves carry
            a, b = (a ^ b) & mask, ((a & b) << 1) & mask
        # if a is negative, get a's 32 bits complement positive first
        # then get 32-bit positive's Python complement negative
        return a if a <= MAX else ~(a ^ mask)

# V0''
# https://blog.csdn.net/fuxuemingzhu/article/details/79379939
class Solution():
    def getSum(self, a, b):
        MAX = 2**31-1  #0x7fffffff
        MIN = 2**31    #0x80000000
        mask = 2**32-1 #0xFFFFFFFF
        while b != 0:
            a, b = (a ^ b) & mask, ((a & b) << 1)
        return a if a <= MAX else ~(a ^ mask)
```

```java
// java
// LC 371 Sum of Two Integers
// IDEA: a ^ b = sum without carry; (a & b) << 1 = the carry; loop until no carry left
class Solution {
    public int getSum(int a, int b) {
        while (b != 0) {
            int carry = (a & b) << 1;   // positions where both are 1 carry left
            a = a ^ b;                  // add bits with no carry
            b = carry;                  // fold the carry back in next round
        }
        return a;
    }
}
```

### 10) Add Binary — LC 67

```python
# LC 67. Add Binary
# V0
# IDEA : Bit-by-Bit Computation
class Solution:
    def addBinary(self, a, b):
        n = max(len(a), len(b))
        """
        NOTE : zfill syntax
            -> fill n-1 "0" to a string at beginning

            example :
                In [10]: x = '1'

                In [11]: x.zfill(2)
                Out[11]: '01'

                In [12]: x.zfill(3)
                Out[12]: '001'

                In [13]: x.zfill(4)
                Out[13]: '0001'

                In [14]: x.zfill(10)
                Out[14]: '0000000001'
        """
        a, b = a.zfill(n), b.zfill(n)
        
        carry = 0
        answer = []
        for i in range(n - 1, -1, -1):
            if a[i] == '1':
                carry += 1
            if b[i] == '1':
                carry += 1
                
            if carry % 2 == 1:
                answer.append('1')
            else:
                answer.append('0')
            
            carry //= 2
        
        if carry == 1:
            answer.append('1')
        answer.reverse()
        
        return ''.join(answer)

# V0'
# IDEA : py default
class Solution:
    def addBinary(self, a, b) -> str:
        return '{0:b}'.format(int(a, 2) + int(b, 2))

# V0''
# IDEA : Bit Manipulation
class Solution:
    def addBinary(self, a, b) -> str:
        x, y = int(a, 2), int(b, 2)
        while y:
            answer = x ^ y
            carry = (x & y) << 1
            x, y = answer, carry
        return bin(x)[2:]
```

```java
// java
// LC 67 Add Binary
// IDEA: bit-by-bit addition from the right, carrying over
class Solution {
    public String addBinary(String a, String b) {
        StringBuilder sb = new StringBuilder();
        int i = a.length() - 1, j = b.length() - 1, carry = 0;
        while (i >= 0 || j >= 0 || carry != 0) {
            int sum = carry;
            if (i >= 0) sum += a.charAt(i--) - '0';
            if (j >= 0) sum += b.charAt(j--) - '0';
            sb.append(sum % 2);      // current bit
            carry = sum / 2;         // carry to next position
        }
        return sb.reverse().toString();
    }
}
```

### 11) Divide Two Integers — LC 29 — shift-and-subtract ⭐⭐⭐⭐⭐


**Pattern**: *shift-and-subtract long division in binary.* Subtracting `divisor` one at a time
is `O(quotient)` and TLEs. Instead, for each bit position `shift` from high to low, ask
"does `divisor << shift` still fit into what's left of the dividend?" — if yes, subtract it and
set bit `shift` of the quotient. That is exactly grade-school long division in base 2, so it
runs in **32 steps**.

**Key Idea**: test `(a >> shift) >= b` rather than `(b << shift) <= a` — the right-shift form
can never overflow.

**Overflow trap**: `Integer.MIN_VALUE / -1` = `2^31` which does not fit in an `int` → the
problem asks you to clamp it to `Integer.MAX_VALUE`. Work in `long` (Java) after taking
absolute values, since `Math.abs(Integer.MIN_VALUE)` is still negative.

```java
// java
// LC 29 - Divide Two Integers
// IDEA: binary long division — for shift = 31..0, if (divisor << shift) fits, subtract it and set bit `shift`
// time = O(32), space = O(1)
class Solution {
    public int divide(int dividend, int divisor) {
        // ONLY overflow case: -2^31 / -1 = 2^31 > Integer.MAX_VALUE
        if (dividend == Integer.MIN_VALUE && divisor == -1) return Integer.MAX_VALUE;

        boolean neg = (dividend < 0) ^ (divisor < 0);       // XOR = "signs differ"
        long a = Math.abs((long) dividend);                 // widen BEFORE abs (MIN_VALUE trap)
        long b = Math.abs((long) divisor);

        long res = 0;
        for (int shift = 31; shift >= 0; shift--) {
            if ((a >> shift) >= b) {       // safe form of: (b << shift) <= a
                a -= (b << shift);         // subtract the biggest fitting multiple
                res |= (1L << shift);      // record 2^shift copies of the divisor
            }
        }
        return neg ? (int) -res : (int) res;
    }
}
```

```python
# python
# LC 29 - Divide Two Integers
# IDEA: binary long division — for shift = 31..0, if (divisor << shift) fits, subtract it and set bit `shift`
# time = O(32), space = O(1)
class Solution(object):
    def divide(self, dividend, divisor):
        INT_MIN, INT_MAX = -2 ** 31, 2 ** 31 - 1
        neg = (dividend < 0) != (divisor < 0)      # signs differ
        a, b = abs(dividend), abs(divisor)

        res = 0
        for shift in range(31, -1, -1):
            if (a >> shift) >= b:        # does divisor * 2^shift still fit?
                a -= b << shift
                res |= 1 << shift
        res = -res if neg else res
        # python ints are unbounded -> clamp to the 32-bit signed range yourself
        return max(INT_MIN, min(INT_MAX, res))
```

**Visual trace** — `divide(10, 3)`:

```text
a = 10, b = 3
shift = 1 : b << 1 = 6  <= 10  -> a = 10 - 6 = 4,  res = 0b10 = 2
shift = 0 : b << 0 = 3  <= 4   -> a = 4  - 3 = 1,  res = 0b11 = 3
                                             remainder 1, quotient 3
```

> **Related**: LC 371 (Sum of Two Integers) does the same "arithmetic without operators" idea
> for `+` via the XOR/carry loop — see [2-5](#9-sum-of-two-integers--lc-371--carry-via-and-sum-via-xor-) and the
> XOR-carry template in `add_x_sum.md`.

## Enumerating and Constructing with Bits

### 12) Subsets — LC 78 — the bitmask enumeration ⭐⭐⭐⭐


Every subset of an `n`-element array maps to an `n`-bit number in `[0, 2^n)`: bit `i` set
⇔ include `nums[i]`. Enumerating masks is an iterative alternative to backtracking.

```python
# python
# LC 78 Subsets — bitmask enumeration
# IDEA: mask in [0, 2^n); bit i set -> take nums[i]
class Solution(object):
    def subsets(self, nums):
        n = len(nums)
        res = []
        for mask in range(1 << n):          # 2^n subsets
            subset = []
            for i in range(n):
                if mask & (1 << i):         # is bit i set?
                    subset.append(nums[i])
            res.append(subset)
        return res
```

```java
// java
// LC 78 Subsets — bitmask enumeration
// time = O(2^n * n), space = O(1) extra
class Solution {
    public List<List<Integer>> subsets(int[] nums) {
        int n = nums.length;
        List<List<Integer>> res = new ArrayList<>();
        for (int mask = 0; mask < (1 << n); mask++) {   // 2^n subsets
            List<Integer> subset = new ArrayList<>();
            for (int i = 0; i < n; i++) {
                if ((mask & (1 << i)) != 0) subset.add(nums[i]);
            }
            res.add(subset);
        }
        return res;
    }
}
```

### 13) Gray Code — LC 89

```python
# LC 89 Gray Code
# V0
# IDEA : bit op
# https://blog.csdn.net/qqxx6661/article/details/78371259
# DEMO
# i = 0 bin(i) = 0b0 bin(i >> 1) = 0b0 bin(i >> 1) ^ i  = 0b0
# i = 1 bin(i) = 0b1 bin(i >> 1) = 0b0 bin(i >> 1) ^ i  = 0b1
# i = 2 bin(i) = 0b10 bin(i >> 1) = 0b1 bin(i >> 1) ^ i  = 0b11
# i = 3 bin(i) = 0b11 bin(i >> 1) = 0b1 bin(i >> 1) ^ i  = 0b10
# i = 4 bin(i) = 0b100 bin(i >> 1) = 0b10 bin(i >> 1) ^ i  = 0b110
# i = 5 bin(i) = 0b101 bin(i >> 1) = 0b10 bin(i >> 1) ^ i  = 0b111
# i = 6 bin(i) = 0b110 bin(i >> 1) = 0b11 bin(i >> 1) ^ i  = 0b101
# i = 7 bin(i) = 0b111 bin(i >> 1) = 0b11 bin(i >> 1) ^ i  = 0b100
# i = 8 bin(i) = 0b1000 bin(i >> 1) = 0b100 bin(i >> 1) ^ i  = 0b1100
# i = 9 bin(i) = 0b1001 bin(i >> 1) = 0b100 bin(i >> 1) ^ i  = 0b1101
# i = 10 bin(i) = 0b1010 bin(i >> 1) = 0b101 bin(i >> 1) ^ i  = 0b1111
# i = 11 bin(i) = 0b1011 bin(i >> 1) = 0b101 bin(i >> 1) ^ i  = 0b1110
# i = 12 bin(i) = 0b1100 bin(i >> 1) = 0b110 bin(i >> 1) ^ i  = 0b1010
# i = 13 bin(i) = 0b1101 bin(i >> 1) = 0b110 bin(i >> 1) ^ i  = 0b1011
# i = 14 bin(i) = 0b1110 bin(i >> 1) = 0b111 bin(i >> 1) ^ i  = 0b1001
# i = 15 bin(i) = 0b1111 bin(i >> 1) = 0b111 bin(i >> 1) ^ i  = 0b1000
class Solution(object):
    def grayCode(self, n):
        res = []
        size = 2**n
        for i in range(size):
            print ("i = " + str(i) + " bin(i) = " + str(bin(i)) + " bin(i >> 1) = " + str(bin(i >> 1))  + " bin(i >> 1) ^ i  = " + str( bin((i >> 1) ^ i) )  )
            """
            NOTE : 
              step 1) we move 1 digit right in every iteration (i >> 1), for keep adding space
              step 2) we do (i >> 1) ^ i. for getting "inverse" binary code with i
              step 3) append and return the result 
            """
            res.append((i >> 1) ^ i)
        return res

# V1'
# https://ithelp.ithome.com.tw/articles/10213273
# DEMO
# In [23]: add=1

# In [24]: add = add << 1

# In [25]: add
# Out[25]: 2

# In [26]: add = add << 1

# In [27]: add
# Out[27]: 4

# In [28]: add = add << 1

# In [29]: add
# Out[29]: 8

# In [30]: add = add << 1

# In [31]: add
# Out[31]: 16

# In [32]: add = add << 1

# In [33]: add
# Out[33]: 32
#
class Solution:
    def grayCode(self, n):
        res = [0]
        add = 1
        for _ in range(n):
            for i in range(add):
                res.append(res[add - 1 - i] + add);
            add <<= 1
        return res
```

```java
// java
// LC 89 Gray Code
// IDEA: i-th gray code = i ^ (i >> 1)
class Solution {
    public List<Integer> grayCode(int n) {
        List<Integer> res = new ArrayList<>();
        int size = 1 << n;                 // 2^n codes
        for (int i = 0; i < size; i++) {
            res.add(i ^ (i >> 1));         // reflect to get the "inverse" binary code
        }
        return res;
    }
}
```

### 14) Bitwise AND of Numbers Range — LC 201 — the common prefix


AND of all numbers in `[left, right]` = their **common binary prefix** padded with zeros
(any differing low bit becomes 0 somewhere in the range). Shift both right until equal,
counting shifts, then shift the common prefix back.

```python
# python
# LC 201 Bitwise AND of Numbers Range
# IDEA: result is the common high-bit prefix of left and right
class Solution(object):
    def rangeBitwiseAnd(self, left, right):
        shift = 0
        while left < right:            # strip differing low bits
            left >>= 1
            right >>= 1
            shift += 1
        return left << shift           # restore the common prefix, low bits = 0
```

```java
// java
// LC 201 Bitwise AND of Numbers Range
// time = O(log N), space = O(1)
class Solution {
    public int rangeBitwiseAnd(int left, int right) {
        int shift = 0;
        while (left < right) {         // find common prefix
            left >>= 1;
            right >>= 1;
            shift++;
        }
        return left << shift;          // pad differing low bits with 0
    }
}
```

## Bit-Field Surgery

These five are *Cracking the Coding Interview* problems rather than LeetCode ones, and they
test the thing LC rarely does: whether you can **build a mask** instead of recalling a
trick. The recipe never changes — build a mask, clear the field, OR the new bits in.

### 15) Insert M into N between bits i and j — CtCI 5.1 ⭐⭐⭐⭐

Put all of `m` into `n` so that it occupies bits `i` through `j`, leaving every other bit of
`n` untouched. The mask you need is `1`s everywhere **except** `i..j`, built as
`(1s above j) | (1s below i)`.

```text
n = 10000000000, m = 10011, i = 2, j = 6

left  = ~0 << (j+1) = 11110000000        1s above j
right = (1 << i) - 1 = 00000000011       1s below i
mask  = left | right = 11110000011       0s exactly on bits 2..6
n & mask             = 10000000000       field cleared
m << i               = 00001001100       m moved into place
result               = 10001001100
```

```python
# python
# CtCI 5.1 - insert m into n so that m occupies bits i..j
# IDEA: clear the field with a 111..000..111 mask, then OR in m shifted to offset i
# time = O(1), space = O(1)
def update_bits(n, m, i, j):
    all_ones = ~0                        # ...11111111
    left  = all_ones << (j + 1)          # 1s above j, 0s from j down
    right = (1 << i) - 1                 # 1s below i
    mask  = left | right                 # 0s exactly on bits i..j
    return (n & mask) | (m << i)
```

```java
// java
// CtCI 5.1 - insert m into n between bits i and j
// IDEA: same three steps — build mask, clear field, drop m in at offset i
// time = O(1), space = O(1)
int updateBits(int n, int m, int i, int j) {
    int allOnes = ~0;
    int left  = (j < 31) ? (allOnes << (j + 1)) : 0;   // j == 31: << 32 is a NO-OP in Java
    int right = (1 << i) - 1;
    int mask  = left | right;
    return (n & mask) | (m << i);
}
```

> The `j == 31` guard is what the question is really checking. **Java** masks a shift count
> by 31 (JLS 15.19), so `allOnes << 32` returns `allOnes` unchanged rather than 0 — the mask
> comes out as all 1s and clears nothing. **C is worse**: a shift by the operand's width is
> *undefined behaviour*, so it may mask, may give 0, may do something else entirely. Python
> has no fixed width and no shift limit, so the guard is not needed there.

### 16) Next number with the same number of 1 bits — CtCI 5.4 ⭐⭐⭐

Brute force (increment until the popcount matches) is worth stating first, then improve it.
Let `c0` be the trailing zeros and `c1` the run of ones just above them. Setting bit
`p = c0 + c1` makes the number **larger**; clearing everything below `p` and re-inserting
the remaining `c1 - 1` ones at the very bottom makes it the **smallest** such number.

```text
n = 13948 = 11011001111100      c0 = 2 (trailing 0s), c1 = 5 (ones above), p = 7
set bit 7                        11011010111100
clear below 7                    11011010000000
add back c1-1 = 4 ones at bottom 11011010001111 = 13967
```

```python
# python
# CtCI 5.4 - the smallest number LARGER than n with the same number of 1 bits
# DOMAIN: positive 32-bit SIGNED ints, as in the book — the answer must stay under
#         2^31, so a result needing bit 31 is reported as -1 rather than returned
# IDEA: flip the rightmost non-trailing zero (position c0+c1) to grow the number,
#       then push the remaining ones as far right as possible to keep it minimal
# time = O(32), space = O(1)
def next_same_popcount(n):
    c, c0, c1 = n, 0, 0
    while c and not (c & 1):             # c0 = trailing zeros
        c0 += 1
        c >>= 1
    while c & 1:                         # c1 = the run of ones above them
        c1 += 1
        c >>= 1
    if c0 + c1 == 0:                     # n == 0: no ones to move
        return -1
    if c0 + c1 == 31:                    # the next value would need bit 31 (negative
        return -1                        # as an int32) — out of domain, so no answer

    p = c0 + c1
    n |= 1 << p                          # flip the rightmost non-trailing zero
    n &= ~((1 << p) - 1)                 # clear everything below it
    n |= (1 << (c1 - 1)) - 1             # re-insert (c1 - 1) ones at the bottom
    return n
```

The **previous** smaller number is the mirror image: count the trailing *ones* and the zeros
above them, clear the rightmost non-trailing one, and pack the ones back immediately below
it. Same three lines with the roles of 0 and 1 swapped.

### 17) Longest run of 1s after flipping one 0 — CtCI 5.3 ⭐⭐⭐⭐

Walk the bits from the right, keeping two counters: the run of 1s you are inside now, and
the run that ended at the last 0. A **single** 0 between them can be flipped to join the
two; two or more 0s cannot, so the earlier run resets.

```python
# python
# CtCI 5.3 - longest run of 1s obtainable by flipping exactly one bit to 1
# IDEA: cur = run ending here, prev = run before the last 0 (kept ONLY if that 0 is
#       isolated). prev + cur + 1 is the best merge through the current zero.
# time = O(32), space = O(1)
def flip_bit_to_win(a):
    a &= 0xFFFFFFFF                      # Python ints are unbounded — pin it to 32 bits
    if a == 0xFFFFFFFF:
        return 32                        # already all 1s
    cur = prev = 0
    best = 1                             # a lone flipped 0 always gives at least 1
    while a:
        if a & 1:
            cur += 1
        else:
            prev = cur if (a & 2) else 0 # the NEXT bit decides whether the runs can merge
            cur = 0
        best = max(best, prev + cur + 1)
        a >>= 1
    return best
```

```java
// java
// CtCI 5.3 - longest run of 1s after flipping one bit
// IDEA: identical, but the shift must be >>> — an arithmetic >> on a negative int
//       feeds in 1s forever and the loop never ends
// time = O(32), space = O(1)
int flipBitToWin(int a) {
    if (~a == 0) return Integer.SIZE;    // all 1s
    int cur = 0, prev = 0, best = 1;
    while (a != 0) {
        if ((a & 1) == 1) {
            cur++;
        } else {
            prev = ((a & 2) == 0) ? 0 : cur;
            cur = 0;
        }
        best = Math.max(best, prev + cur + 1);
        a >>>= 1;                        // logical shift, not >>
    }
    return best;
}
```

The array version of this question — *at most `k` zeros*, not exactly one — is a sliding
window instead: LC 1004 and LC 487 in [sliding_window.md](./sliding_window.md). Bits only
buy you the `O(1)` space here because the input is one 32-bit word.

### 18) Swap odd and even bits — CtCI 5.7 ⭐⭐⭐

Swap bit 0 with bit 1, bit 2 with bit 3, and so on. No loop and no temporary: mask the two
halves apart and shift each one the other way.

```text
0xAAAAAAAA = 1010...1010   the ODD-indexed bits    -> shift RIGHT by 1
0x55555555 = 0101...0101   the EVEN-indexed bits   -> shift LEFT  by 1
```

```python
# python
# CtCI 5.7 - swap every pair of adjacent bits
# IDEA: pull the odd and even bits apart with 0xA... / 0x5..., shift each toward the
#       other's slot, then OR the two halves back together
# time = O(1), space = O(1)
def swap_odd_even_bits(x):
    x &= 0xFFFFFFFF
    return (((x & 0xAAAAAAAA) >> 1) | ((x & 0x55555555) << 1)) & 0xFFFFFFFF
```

```java
// java
// CtCI 5.7 - swap every pair of adjacent bits
// IDEA: >>> is required — 0xaaaaaaaa is a NEGATIVE int, so >> would smear its sign bit
// time = O(1), space = O(1)
int swapOddEvenBits(int x) {
    return ((x & 0xaaaaaaaa) >>> 1) | ((x & 0x55555555) << 1);
}
```

The same mask-and-shift shape scales: swapping nibbles is `0xF0F0F0F0` / `0x0F0F0F0F` with
a shift of 4, and repeating it for 1, 2, 4, 8, 16 is exactly how a `O(log n)` bit-reverse
(LC 190) works.

### 19) Print a fraction in binary — CtCI 5.2 ⭐⭐⭐

Given a `double` between 0 and 1, print its binary representation, or `ERROR` if it needs
more than 32 characters. Reading a binary fraction is **doubling**: bit `k` after the point
is 1 exactly when `2x >= 1`, and the leftover `2x - 1` carries into the next bit.

```python
# python
# CtCI 5.2 - print a double in (0, 1) as binary, or ERROR beyond 32 characters
# IDEA: double the number; a result >= 1 emits a 1 bit and leaves the remainder behind,
#       a result < 1 emits a 0 bit. It terminates only for dyadic fractions (k / 2^m).
# time = O(32), space = O(32)
def print_binary(num):
    if num <= 0 or num >= 1:
        return "ERROR"
    out = ["."]
    while num > 0:
        if len(out) >= 32:
            return "ERROR"               # not representable in 32 characters
        num *= 2
        if num >= 1:
            out.append("1")
            num -= 1                      # keep only the fractional part
        else:
            out.append("0")
    return "".join(out)

# print_binary(0.625) -> ".101"    (1/2 + 1/8)
# print_binary(0.125) -> ".001"
# print_binary(0.1)   -> "ERROR"   0.1 is 0.0001100110011... forever in binary
```

That last line is the whole point of the question, and the same fact behind
`0.1 + 0.2 != 0.3` — see [python_gotchas.md](./python_gotchas.md). A fraction terminates in
binary only when its denominator is a power of two.
