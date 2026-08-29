# 把 `xxx` 加起來

> **範圍** — 逐位數相加，涵蓋面試官會丟給你的四種輸入形式：字串、整數、陣列、鏈結串列。
> **另見**：[math.md](./math.md) — 更廣泛的數值操作；[bit_manipulation.md](./bit_manipulation.md) — 不用 `+` 做加法；[linked_list.md](./linked_list.md) — 串列節點版本（LC 2、445）；[string.md](./string.md) — 字串組裝。

- https://leetcode.com/problems/add-strings/solution/

`Facebook` 面試官很愛這題，而且主要會出四種變形。演算法怎麼選，取決於輸入格式：

1. 字串（也就是本題）。用小學那套逐位數相加。注意：在字串不可變的語言（例如 Java 和 Python）裡，做不到常數空間。兩個例子：
    - LC 067 : Add Binary：兩個二進位字串相加。
    - LC 415 : Add Strings：兩個以字串表示的非負整數相加，不能直接轉成整數。

2. 整數。面試官通常會要求你不用 + 和 - 運算子做加法。這時用位元運算。例子：
    - LC 371 : Sum of Two Integers：不用 + 和 - 把兩個整數相加。

3. 陣列。一樣是課本上的加法。例子：
    - LC 989 : Add to Array Form of Integer。

4. 鏈結串列。哨兵頭節點 + 課本加法。幾個例子：
    - LC 66 : Plus One。
    - LC 002 : Add Two Numbers。
    - LC 445 : Add Two Numbers II。

## LeetCode 題目清單

- [Math](https://leetcode.com/problem-list/math/)
- [String](https://leetcode.com/problem-list/string/)
- [Bit Manipulation](https://leetcode.com/problem-list/bit-manipulation/)

## 2) LC 範例

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

## 3) 更多模板

### 快速決策表

| 目標 | 模板 | LC |
|------|----------|-----|
| **不用** `+` / `-` 把兩數相加 | XOR（和）+ AND<<1（進位）迴圈 | 371 |
| 兩個數字字串**相乘** | 位數格子 + `i+j` / `i+j+1` 索引規則 | 43 |
| 在**任意進位 k** 下相加／解析 | 同一套進位迴圈，把 `10` 換成 `k` | 67 (k=2), 415 (k=10) |
| **解析** *雙射*進位制（沒有 `0` 這個位數） | `res = res*k + digit`，digit 從 1 開始算 | 171 (bijective k=26) |
| **安全地**逐位數組出一個整數 | `res = res*10 + d`，加上乘之前的溢位防護 | 7 |

### 3-1) 不用 `+` / `-` 相加兩整數 — LC 371 ⭐⭐⭐⭐⭐

**核心想法**：把二進位加法拆成兩個互不相干的部分。
- `a ^ b` = **忽略**所有進位之後的和
- `(a & b) << 1` = **只有**進位的部分（兩個位元都是 1 才產生進位，而且會落在左邊一位）

一直迴圈到進位變成 0 為止。這就是課本的進位流程，只是所有位元同時平行做完。

**視覺追蹤**（`a = 3 (011)`、`b = 5 (101)`）：

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

> **變形 — 減法**：`a - b` 就是 `getSum(a, ~b + 1)`（二補數取負）。

### 3-2) Multiply Strings — LC 43 ⭐⭐⭐⭐

LC 415（Add Strings）很自然的下一題：一樣不准轉成大整數，但改成乘法。

**核心想法**：`num1[i] * num2[j]` 一定落在結果陣列的**剛好兩個**格子上：
- `pos[i + j + 1]` -> 個位數
- `pos[i + j]`     -> 進位

所以結果長度最多是 `m + n`。邊累加進 `pos` 邊做正規化，最後把開頭的 0 去掉。

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

### 3-3) 在任意進位 k 下相加／解析

LC 67（2 進位）和 LC 415（10 進位）的迴圈其實是**同一套**模板 — 差別只在取模的數字。寫成通用版本，之後都能重用：

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

#### **實作範例：LC 171 — Excel Sheet Column Number（*雙射* 26 進位）**

同一個想法的**反方向**，而且**不是**單純的 k 進位進位迴圈：從前往後折疊每個位數，`res = res * base + digit`。
麻煩的地方：Excel 是**從 1 開始編號**的（`A = 1 ... Z = 26`），所以沒有 `0` 這個位數 — 這叫做*雙射* 26 進位。

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

> **變形 — LC 7 Reverse Integer**：一樣是 `res = res * 10 + digit` 的累加，但結果必須留在 32 位元範圍內，所以要在**乘之前**檢查（在 Java 裡溢位發生後就抓不到了）。

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

## 4) 其他相關 LC

- LC 8 : String to Integer (atoi) — 一樣是 `res = res * 10 + digit` 的累加，另外加上正負號解析／略過空白／溢位時**夾住邊界值**（而不是回傳 0）。
