# Add `xxx` to Sum

> **Scope** — Digit-by-digit addition across the four input shapes an interviewer will hand you: strings, integers, arrays, and linked lists.
> **See also**: [math.md](./math.md) — wider numeric manipulation; [bit_manipulation.md](./bit_manipulation.md) — adding without `+`; [linked_list.md](./linked_list.md) — the list-node variant (LC 2, 445); [string.md](./string.md) — string building.

- https://leetcode.com/problems/add-strings/solution/

`Facebook` interviewers like this question and propose it in four main variations. The choice of algorithm should be based on the input format:

1. Strings (the current problem). Use schoolbook digit-by-digit addition. Note, that to fit into constant space is not possible for languages with immutable strings, for example, for Java and Python. Here are two examples:
    - LC 067 : Add Binary: sum two binary strings.
    - LC 415 : Add Strings: sum two non-negative numbers in a string representation without converting them to integers directly.

2. Integers. Usually, the interviewer would ask you to implement a sum without using + and - operators. Use bit manipulation approach. Here is an example:
    - LC 371 : Sum of Two Integers: Sum two integers without using + and - operators.

3. Arrays. The same textbook addition. Here is an example:
    - LC 989 : Add to Array Form of Integer.

4. Linked Lists. Sentinel Head + Textbook Addition. Here are some examples:
    - LC 66 : Plus One.
    - LC 002 : Add Two Numbers.
    - LC 445 : Add Two Numbers II.

## LeetCode Problem Lists

- [Math](https://leetcode.com/problem-list/math/)
- [String](https://leetcode.com/problem-list/string/)
- [Bit Manipulation](https://leetcode.com/problem-list/bit-manipulation/)

## 2) LC Example

### 2-1) Add Binary — LC 67
```python
# LC 067 Add Binary: sum two binary strings.
# V0
# IDEA : STRING + BINARY
class Solution(object):
    def addBinary(self, a, b):

        _len = max(len(a), len(b))
        if len(a) < _len:
            a = (_len - len(a)) * '0' + a
        if len(b) < _len:
            b = (_len - len(b)) * '0' + b

        plus = 0
        result = ""

        # INVERSE LOOPING THE a, b
        for i in range(len(a))[::-1]:
            tmp = int(a[i]) + int(b[i]) + plus
            if tmp > 1:
                tmp -= 2
                plus = 1
            else:
                plus = 0

            result += str(tmp)

        if plus == 1:
            return '1' + result[::-1]  ### NOTE WE NEED TO REVERSE IT!
        else: 
            return result[::-1] ### NOTE WE NEED TO REVERSE IT!

# V0'
class Solution(object):
    def addBinary(self, a, b):
        """
        :type a: str
        :type b: str
        :rtype: str
        """
        res = ''
        i, j, plus = len(a)-1, len(b)-1, 0
        while i>=0 or j>=0 or plus==1:
            plus += int(a[i]) if i>= 0 else 0
            plus += int(b[j]) if j>= 0 else 0
            res = str(plus % 2) + res
            i, j, plus = i-1, j-1, plus//2  # since max of "plus" is 3 in bimary case, so "plus//2" works here
        return res
```

### 2-2) Add Strings — LC 415

```java
// java
// LC 415
// V0
// IDEA: string op (fixed by gpt)
public String addStrings(String num1, String num2) {
    if (num1 == null || num2 == null) {
        if (num1 == null) {
            return num2;
        }
        return num1;
    }
    if (num1.equals("0") && num2.equals("0")) {
        return "0";
    }

    StringBuilder sb = new StringBuilder();

    int plus = 0;
    int idx_1 = num1.length() - 1;
    int idx_2 = num2.length() - 1;

    /** NOTE !!!
     *
     *  1. while loop
     *  2. idx_1 >= 0 or idx_2 >= 0
     */
    while (idx_1 >= 0 || idx_2 >= 0) {

        int v1 = 0;
        int v2 = 0;

        int new_val = 0;

        /** NOTE !!!
         *
         *  if idx_1 >= 0, then get val from it
         */
        if (idx_1 >= 0) {
            v1 = Integer.parseInt(String.valueOf(num1.charAt(idx_1)));
            idx_1 -= 1;
        }

        /** NOTE !!!
         *
         *  if idx_1 >= 0, then get val from it
         */
        if (idx_2 >= 0) {
            v2 = Integer.parseInt(String.valueOf(num2.charAt(idx_2)));
            idx_2 -= 1;
        }

        new_val = (new_val + v1 + v2 + plus);

        /** NOTE !!!
         *
         *  if new_vla > 9,
         *  we should `subtract 10` (instead of 9)
         */
        if (new_val > 9) {
            plus = 1;
            new_val -= 10;
        } else {
            plus = 0;
        }

        sb.append(new_val);
    }

    /** NOTE !!!
     *
     *  need to add the `remaining plus` to res
     *  if there is it
     */
    if (plus > 0) {
        sb.append(plus);
    }

    // reverse
    return sb.reverse().toString();
}
```


```python
# LC 415. Add Strings
# V0
# IDEA : string + math
class Solution(object):
    def addStrings(self, num1, num2):
        result = []
        # note : we init carry as 0
        carry = 0
        num1 = list(num1)
        num2 = list(num2)
        # while there is still non-add digit in num1, and num2; or there is non-zero carry 
        while num1 or num2 or carry:
            digit = carry
            if num1:
                tmp1 = num1.pop(-1)
                digit += int(tmp1)
            if num2:
                tmp2 = num2.pop(-1)
                digit += int(tmp2)
            """
            if digit > 9 -> we need to "carry" 1 to next digit -> carry = 1
            else -> carry = 0
            """
            if digit > 9:
                carry = 1
            else:
                carry = 0
            # NOTE !!! we get "remain" by 10 via below code
            result.append(str(digit % 10))
        return ''.join(result[::-1]) 
```

### 2-3) Sum of Two Integers — LC 371
```python
# LC 371 : Sum of Two Integers
# V0
# https://leetcode.com/problems/sum-of-two-integers/discuss/1214257/Python-1-line%3A-91-faster
class Solution:
    def getSum(self, a, b):
        tmp = math.exp(a) * math.exp(b)
        r = int(math.log(tmp))
        return r
```

### 2-4) Add to Array Form of Integer — LC 989
```python
# LC 989 Add to Array Form of Integer
# V0
# IDEA : array op
class Solution:
    def addToArrayForm(self, num, k):
        s = ""
        for i in num:
            s += str(i)       
        answer = int(s) + k
        return  list("".join(str(answer)))
```

### 2-5) Plus One — LC 66
```python
# LC 66 Plus One
# V0
# NOTE : Notice the index of inverse loop  : range(len(a)-1, -1, -1)
# a = ['a', 'b', 'c']
# for i in range(len(a)-1, -1, -1):
#     print (a[i])
# c
# b
# a
class Solution(object):
    def plusOne(self, digits):
        """
        :type digits: List[int]
        :rtype: List[int]
        """
        plus = 1
        for i in range(len(digits)-1, -1, -1):
            if digits[i] + plus > 9:
                digits[i] = 0
                plus = 1
            else:
                digits[i] = digits[i] + plus
                plus = 0
        if plus == 1:
            digits.insert(0, 1)
        return digits
```

### 2-6) Add Two Numbers — LC 2
```python
# LC 002 Add Two Numbers
# V0
class Solution(object):
    def addTwoNumbers(self, l1, l2):
        """
        NOTE :
         1. we init linkedlist via ListNode()
         2. we NEED make extra head refer same linkedlist, since we need to return beginning of linkedlust of this func, while res will meet "tail" at the end of while loop
        """
        head = res = ListNode()
        plus = 0
        tmp = 0
        while l1 or l2:
            tmp += plus
            plus = 0
            if l1:
                tmp += l1.val
                l1 = l1.next
            if l2:
                tmp += l2.val
                l2 = l2.next
            if tmp > 9:
                tmp -= 10
                plus = 1

            res.next = ListNode(tmp)
            res = res.next
            tmp = 0
        ### NOTE : need to deal with case : l1, l2 are completed, but still "remaining" plus
        if plus != 0:
            res.next = ListNode(plus)
            res = res.next
        #print ("res = " + str(res))
        #print ("head = " + str(head))
        return head.next
```

### 2-7) Add Two Numbers II — LC 445
```python
# LC 445 Add Two Numbers II
# V0
# IDEA : string + linked list
# DEMO
# input :
# [7,2,4,3]
# [5,6,4]
# intermedia output : 
# l1_num = 7243
# l2_num = 564
class Solution:
    def addTwoNumbers(self, l1, l2):
        if not l1 and not l2:
            return None

        l1_num = 0
        while l1:
            l1_num = l1_num * 10 + l1.val
            l1 = l1.next

        l2_num = 0
        while l2:
            l2_num = l2_num * 10 + l2.val
            l2 = l2.next

        print ("l1_num = " + str(l1_num))
        print ("l2_num = " + str(l2_num))


        ### NOTE : trick here :
        #    -> get int format of 2 linked list first (l1, l2)
        #    -> then sum them (l1_num + l2_num)
        lsum = l1_num + l2_num

        head = ListNode(None)
        cur = head
        ### NOTE : go thrpigh the linked list int sum, append each digit to ListNode and return it
        for istr in str(lsum):
            cur.next = ListNode(int(istr))
            cur = cur.next
        # NOTE : need to return head (but not cur, since cur already meet the end of ListNode)
        return head.next
```

## 3) More Templates

### Quick Decision Table

| Goal | Template | LC |
|------|----------|-----|
| Add 2 numbers **without** `+` / `-` | XOR (sum) + AND<<1 (carry) loop | 371 |
| **Multiply** 2 number strings | digit grid + `i+j` / `i+j+1` index rule | 43 |
| Add / parse in an **arbitrary base k** | same carry loop, replace `10` with `k` | 67 (k=2), 415 (k=10) |
| **Parse** a *bijective* base (no `0` digit) | `res = res*k + digit`, digit is 1-indexed | 171 (bijective k=26) |
| Build an int digit-by-digit **safely** | `res = res*10 + d` + pre-multiply overflow guard | 7 |

### 3-1) Add Two Integers Without `+` / `-` — LC 371 ⭐⭐⭐⭐⭐

**Key Idea**: split binary addition into two independent parts.
- `a ^ b` = the sum **ignoring** all carries
- `(a & b) << 1` = the carries **only** (a carry is produced where both bits are 1, and it lands one position left)

Loop until the carry becomes 0. This is exactly the schoolbook carry loop, done on all bits in parallel.

**Visual Trace** (`a = 3 (011)`, `b = 5 (101)`):

```text
a=011 b=101 -> sum(^)=110  carry(&<<1)=010
a=110 b=010 -> sum(^)=100  carry(&<<1)=100
a=100 b=100 -> sum(^)=000  carry(&<<1)=1000
a=000 b=1000-> sum(^)=1000 carry=0  -> stop, ans = 8
```

```java
// java
// LC 371 - Sum of Two Integers
// IDEA: bit manipulation, XOR = carry-less sum, (AND << 1) = carry
public int getSum(int a, int b) {
    // time = O(1) (<= 32 iterations), space = O(1)
    while (b != 0) {
        int carry = (a & b) << 1; // where both bits are 1 -> carry to the left
        a = a ^ b;                // sum without carry
        b = carry;                // keep adding the carry back in
    }
    return a;
}
```

```python
# python
# LC 371 - Sum of Two Integers
# IDEA: same bit trick, but python ints are UNBOUNDED
#       -> must mask to 32 bits, then re-interpret as a signed int at the end
class Solution:
    def getSum(self, a, b):
        # time = O(1) (<= 32 iterations), space = O(1)
        MASK = 0xFFFFFFFF
        MAX_INT = 0x7FFFFFFF
        a &= MASK
        b &= MASK
        while b:
            # NOTE !!! mask on EVERY step, otherwise the carry grows forever
            a, b = (a ^ b) & MASK, ((a & b) << 1) & MASK
        # if a is over MAX_INT it is a negative number in 2's complement
        return a if a <= MAX_INT else ~(a ^ MASK)
```

> **Variation — subtraction**: `a - b` is `getSum(a, ~b + 1)` (two's complement negation).

### 3-2) Multiply Strings — LC 43 ⭐⭐⭐⭐

Natural follow-up to LC 415 (Add Strings): same "no big-int conversion" constraint, but multiplication.

**Key Idea**: `num1[i] * num2[j]` always lands on **exactly two** slots of the result array:
- `pos[i + j + 1]` -> the ones digit
- `pos[i + j]`     -> the carry digit

So result length is at most `m + n`. Accumulate into `pos`, normalizing as you go, then strip leading zeros.

```text
    1 2 3        m = 3, n = 3 -> pos length 6
  x 4 5 6        num1[1]='2' (i=1), num2[2]='6' (j=2)
  -------        2*6 = 12 -> pos[i+j+1] = pos[4] += 2
   56088                     pos[i+j]   = pos[3] += 1
```

```java
// java
// LC 43 - Multiply Strings
// IDEA: schoolbook multiplication into an int[m+n] grid, index rule i+j / i+j+1
public String multiply(String num1, String num2) {
    // time = O(m * n), space = O(m + n)
    if (num1.equals("0") || num2.equals("0")) {
        return "0";
    }
    int m = num1.length(), n = num2.length();
    int[] pos = new int[m + n];

    for (int i = m - 1; i >= 0; i--) {
        for (int j = n - 1; j >= 0; j--) {
            int mul = (num1.charAt(i) - '0') * (num2.charAt(j) - '0');
            int p1 = i + j, p2 = i + j + 1;
            /** NOTE !!! add the CURRENT value at p2 first, then split */
            int sum = mul + pos[p2];
            pos[p2] = sum % 10;
            pos[p1] += sum / 10; // carry (accumulates, normalized on a later visit)
        }
    }

    StringBuilder sb = new StringBuilder();
    for (int v : pos) {
        // NOTE !!! skip leading zeros only
        if (!(sb.length() == 0 && v == 0)) {
            sb.append(v);
        }
    }
    return sb.length() == 0 ? "0" : sb.toString();
}
```

```python
# python
# LC 43 - Multiply Strings
# IDEA: same digit grid, pos[i+j+1] = ones digit, pos[i+j] = carry
class Solution:
    def multiply(self, num1, num2):
        # time = O(m * n), space = O(m + n)
        if num1 == "0" or num2 == "0":
            return "0"
        m, n = len(num1), len(num2)
        pos = [0] * (m + n)
        for i in range(m - 1, -1, -1):
            for j in range(n - 1, -1, -1):
                mul = int(num1[i]) * int(num2[j])
                p1, p2 = i + j, i + j + 1
                total = mul + pos[p2]
                pos[p2] = total % 10
                pos[p1] += total // 10
        res = "".join(map(str, pos)).lstrip("0")
        return res if res else "0"
```

### 3-3) Add / Parse in an Arbitrary Base k

The LC 67 (base 2) and LC 415 (base 10) loops are the **same** template — only the modulus changes. Generalize once and reuse:

```java
// java
// IDEA: one carry loop for any base k (digits '0'-'9' then 'a'-'z')
private static final String DIGITS = "0123456789abcdefghijklmnopqrstuvwxyz";

public String addInBase(String a, String b, int base) {
    // time = O(max(m, n)), space = O(max(m, n))
    StringBuilder sb = new StringBuilder();
    int i = a.length() - 1, j = b.length() - 1, carry = 0;
    /** NOTE !!! the `|| carry != 0` term handles the final rollover (e.g. "99" + "1") */
    while (i >= 0 || j >= 0 || carry != 0) {
        int sum = carry;
        if (i >= 0) sum += DIGITS.indexOf(a.charAt(i--));
        if (j >= 0) sum += DIGITS.indexOf(b.charAt(j--));
        sb.append(DIGITS.charAt(sum % base)); // digit
        carry = sum / base;                   // carry
    }
    return sb.reverse().toString();
}
// addInBase("1010", "1011", 2) = "10101"
// addInBase("ff", "1", 16)     = "100"
```

```python
# python
# IDEA: one carry loop for any base k
DIGITS = "0123456789abcdefghijklmnopqrstuvwxyz"

def addInBase(a, b, base):
    # time = O(max(m, n)), space = O(max(m, n))
    res = []
    i, j, carry = len(a) - 1, len(b) - 1, 0
    while i >= 0 or j >= 0 or carry:
        s = carry
        if i >= 0:
            s += DIGITS.index(a[i]); i -= 1
        if j >= 0:
            s += DIGITS.index(b[j]); j -= 1
        res.append(DIGITS[s % base])
        carry = s // base
    return "".join(reversed(res))
```

#### **Worked example: LC 171 — Excel Sheet Column Number (*bijective* base 26)**

The **inverse** direction of the same idea, and **not** a plain base-`k` carry loop: fold digits front-to-back with `res = res * base + digit`.
Twist: Excel is **1-indexed** (`A = 1 ... Z = 26`), so there is no `0` digit — this is *bijective* base-26.

```java
// java
// LC 171 - Excel Sheet Column Number
// IDEA: positional accumulation in base 26, digit = c - 'A' + 1 (1-indexed !!)
public int titleToNumber(String columnTitle) {
    // time = O(n), space = O(1)
    int res = 0;
    for (char c : columnTitle.toCharArray()) {
        res = res * 26 + (c - 'A' + 1);
    }
    return res;
}
// "A" -> 1, "AB" -> 28, "ZY" -> 701
```

```python
# python
# LC 171 - Excel Sheet Column Number
class Solution:
    def titleToNumber(self, columnTitle):
        # time = O(n), space = O(1)
        res = 0
        for c in columnTitle:
            res = res * 26 + (ord(c) - ord('A') + 1)
        return res
```

> **Variation — LC 7 Reverse Integer**: same `res = res * 10 + digit` accumulation, but the result must stay in 32-bit range, so check **before** multiplying (you cannot detect the overflow after it happens in Java).

```java
// java
// LC 7 - Reverse Integer
// IDEA: pop digit with %10, push with *10 + d, guard the overflow BEFORE pushing
public int reverse(int x) {
    // time = O(log x), space = O(1)
    int res = 0;
    while (x != 0) {
        int digit = x % 10; // keeps the sign in java
        x /= 10;
        /** NOTE !!! check overflow BEFORE res = res * 10 + digit */
        if (res > Integer.MAX_VALUE / 10 || (res == Integer.MAX_VALUE / 10 && digit > 7)) return 0;
        if (res < Integer.MIN_VALUE / 10 || (res == Integer.MIN_VALUE / 10 && digit < -8)) return 0;
        res = res * 10 + digit;
    }
    return res;
}
```

```python
# python
# LC 7 - Reverse Integer
# IDEA: python ints don't overflow -> just range-check at the END
class Solution:
    def reverse(self, x):
        # time = O(log x), space = O(1)
        sign = -1 if x < 0 else 1
        x = abs(x)
        res = 0
        while x:
            res = res * 10 + x % 10
            x //= 10
        res *= sign
        return res if -2 ** 31 <= res <= 2 ** 31 - 1 else 0
```

## 4) Other related LC

- LC 8 : String to Integer (atoi) — same `res = res * 10 + digit` accumulation, plus sign parsing / whitespace skipping / **clamping** (instead of returning 0) on overflow.