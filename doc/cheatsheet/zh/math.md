# 數學

> **範圍** — 面試中的數值操作 — 取位數、避免溢位的算術、進位轉換、開根號與次方。公式層級的計數則在隔壁那份。
> **另見**：[combinatorics_math_patterns.md](./combinatorics_math_patterns.md) — 計數、質數(prime)、模運算、幾何；[bit_manipulation.md](./bit_manipulation.md) — 位元層級的視角；[add_x_sum.md](./add_x_sum.md) — 各種輸入形態下的加法。

## LeetCode 題目清單

- [Math](https://leetcode.com/problem-list/math/)
- [Number Theory](https://leetcode.com/problem-list/number-theory/)

## 1) 通用形式

### 1-1) 基本操作

#### 1-1-0) 把 `10 based interger to N based`
- 轉成 4 進位
- 轉成 7 進位 ..

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

#### 1-1-0') 把 `N based integer to 10 based`
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

#### 1-1-1) 判斷質數(prime)
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

#### 1-1-2) 計算質數(prime)個數
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

#### 1-1-3) 只保留餘數以避免溢位（Repunit／模運算）

**核心想法：**
當你一位一位地把數字建出來（例如 1 → 11 → 111 → ...），這個數會呈指數成長並讓 `int`/`long` 溢位。
與其存整個數，**每一步只保留對 k 取模的餘數**。

之所以可行，是因為：
```text
(a * 10 + 1) % k  ==  ((a % k) * 10 + 1) % k
```
所以加上新的一位之後的餘數，永遠可以只用*前一個*餘數算出來。

**模式：**
```java
int remainder = 0;
for (int len = 1; len <= k; len++) {
    remainder = (remainder * 10 + 1) % k;  // build next repunit mod k
    if (remainder == 0) return len;         // found divisible repunit
}
return -1;
```

**關鍵洞察 — 鴿籠原理：**
可能的餘數只有 `k` 種（0 到 k-1）。經過 `k` 步之後，如果沒有任何餘數是 0，就一定有餘數重複出現 → 進入循環且無解 → 回傳 -1。所以我們只需要迴圈跑到 `k` 次為止。

**快速檢查 — 提早退出：**
一個只由 1 組成的數，末位永遠不會是 0、2、4、5、6 或 8，所以它永遠不可能被 2 或 5 整除：
```java
if (k % 2 == 0 || k % 5 == 0) return -1;
```

**用到這個餘數技巧的相似 LC 題目：**
| 題目 | 模式 |
|---------|---------|
| LC 1015 - Smallest Integer Divisible by K | repunit 對 k 取模 |
| LC 29  - Divide Two Integers | 用位元運算避免溢位 |
| LC 166 - Fraction to Recurring Decimal | 用餘數雜湊表偵測循環 |
| LC 523 - Continuous Subarray Sum | 前綴和對 k 取模 |
| LC 974 - Subarray Sums Divisible by K | 前綴和對 k 取模 |

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

#### 1-1-4) 判斷 4 個點是否構成正方形（兩兩距離技巧）

**核心想法：**
給定順序任意的 4 個點，直接檢查角度／斜率會很麻煩（除以零、浮點誤差）。改成計算這 4 個點之間的**6 個兩兩平方距離**，再把它們當成一個多重集合來推理。

**為什麼是 6 個距離？**
4 個點 → C(4,2) = 6 組配對。對正方形來說：

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

所以把這 6 個平方距離排序之後，一定會得到**4 個相等的（較小）值 + 2 個相等的（較大）值**。

**模式 — 合法的正方形具備：**
- 邊長 > 0（沒有重疊的點）
- 4 條相等的邊
- 2 條相等的對角線
- 對角線 > 邊長

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

**另一種（集合式）寫法：**
既然合法的正方形恰好只有 2 個相異的距離值（邊長²、對角線²），而且真正的正方形對角線永遠 > 邊長，你可以直接檢查 `len(set(distances)) == 2`（再加上 `0 not in distances` 這個防止重疊點的守衛）：

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

#### 1-1-5) 加權旋轉和 — 疊縮遞迴式（避開 O(n²) 暴力法）

**模式：**
對每個旋轉 `k` 都重新計算 `F(k) = sum(i * arr_k[i])`，每次要 O(n) → 總共 O(n²)。改成推導出從 `F(k-1)` 到 `F(k)` 的 **O(1) 轉移**，就能在 O(n) 內滑過全部 `n` 個旋轉。

**核心想法：**
手動寫出幾個旋轉並逐項比較（`nums = [A, B, C, D]`，`n = 4`）：

```text
F(0) = 0*A + 1*B + 2*C + 3*D
F(1) = 0*D + 1*A + 2*B + 3*C
F(2) = 0*C + 1*D + 2*A + 3*B
F(3) = 0*B + 1*C + 2*D + 3*A
```

每次旋轉時，每個元素的權重都會加 1 — **除了**剛從尾端繞到前端的那個元素，它的權重會從 `n-1` 掉到 `0`。所以：

```text
sum = A + B + C + D                 # total sum, weight-independent

F(1) = F(0) + sum - 4*D             # D's weight: 3 -> 0, i.e. -4*D; everything else: +1 each -> +sum
F(2) = F(1) + sum - 4*C
F(3) = F(2) + sum - 4*B
```

推廣之後，對 `F(k)` 而言繞到位置 0 的元素是 `nums[n-k]`，得到遞迴式：

```text
F(k) = F(k-1) + sum(nums) - n * nums[n-k]
```

這和前綴和是同一套「**維護一個滾動的彙總值，而不是從頭重算**」的哲學 — 只是套用在*加權*和上，而不是單純的和。

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

**相似 LC 題目（同樣的「狀態之間 O(1) 轉移」想法）：**
| 題目 | 模式 |
|---------|---------|
| LC 396 - Rotate Function | 疊縮的加權和遞迴式：`F(k) = F(k-1) + sum - n*nums[n-k]` |
| LC 238 - Product of Array Except Self | 滾動的前綴／後綴乘積，而非每個索引重算 |
| LC 303 - Range Sum Query - Immutable | 預先算好前綴和，而非每次查詢重算 |
| LC 189 - Rotate Array | 真的做實體旋轉（反轉技巧）— 對比：沒有彙總公式，只是重新排列元素 |

#### 1-1-6) 貪婪鋸齒建構 — 用閉合式 O(1) 取代模擬

**題型形態（LC 3993 - Maximum Value of an Alternating Sequence）：**
給定 `n`、`s`、`m` — 建構一個長度為 `n` 的交錯（鋸齒）序列，滿足 `seq[0] = s` 且 `|seq[i] - seq[i-1]| <= m`。回傳任一合法序列中可能出現的**最大元素**。

**核心想法：**
不要在序列上搜尋／做動態規劃。只要問：*多加一個波峰要付出多少代價？* 然後答案就是一條公式。

三個貪婪觀察就能把一切釘死：

1. **一開始一定要往上走。** 以往下走開頭（`seq[0] > seq[1]`）會浪費第一步，之後每個波峰都更低。所以採用 `seq[0] < seq[1] > seq[2] < ...` 這種形狀。
2. **每個向上的步伐都該是 `+m`** — 允許的最大跳幅。
3. **每個向下的步伐都該是 `-1`** — *最小*的合法下降幅度，因為不等式是**嚴格**的（`>`），所以最小的下降量是 1。掉得更多只會把後面所有波峰都拉低。

所以這個序列是一條爬升 `m`、只回吐 `1` 的鋸齒：

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

**計算步數：**
- 波峰落在奇數索引 `1, 3, 5, ...` → 向上步數 `k = n // 2`
- `k` 個波峰之間有 `k - 1` 個向下步伐，每個恰好花掉 `1`

```text
max = s + k*m - (k - 1)
    = s + k*(m - 1) + 1        , where k = n // 2
```

**邊界情況：** `n == 1` → 長度為 1 的序列依定義就是交錯的，而且它必須等於 `s`。（公式會給出 `s + 1`，那是錯的，所以要明確加守衛。）

**模式：**
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

**驗算：**

| n | s | m | k = n//2 | s + k*(m-1) + 1 | 序列 |
|---|---|---|----------|-----------------|----------|
| 1 | 3 | 5 | — | `3`（邊界情況） | `[3]` |
| 2 | 4 | 3 | 1 | `4 + 1*2 + 1 = 7` | `[4, 7]` |
| 4 | 3 | 5 | 2 | `3 + 2*4 + 1 = 12` | `[3, 8, 7, 12]` |
| 5 | 3 | 5 | 2 | `3 + 2*4 + 1 = 12` | `[3, 8, 7, 12, 11]` |
| 4 | 3 | 1 | 2 | `3 + 2*0 + 1 = 4` | `[3, 4, 3, 4]` |

注意 `n = 4` 和 `n = 5` 得到相同的答案 — 尾端多出來的偶數步只能從最後一個波峰*往下*走，所以永遠不會抬高最大值。

**關鍵心得（可遷移）：**
- **「嚴格不等式」⇒ 最小步伐是 1，不是 0。** 那個 `-1` 就是 `(m - 1)` 的由來。
- 當限制只落在*相鄰*配對上時，最佳解通常是一個**貪婪的重複單元**；先數有幾個單元塞得下，再乘起來。
- 像 `1 <= n, s <= 1e9` 這種限制，是在強烈暗示預期解法是 **O(1) 數學**而不是模擬 — 而且你會需要 64 位元整數。

**相似 LC 題目：**
| 題目 | 模式 |
|---------|---------|
| LC 3993 - Maximum Value of an Alternating Sequence | 貪婪鋸齒（`+m` / `-1`）→ 閉合式 `s + (n//2)*(m-1) + 1` |
| LC 376 - Wiggle Subsequence | 一樣上下交錯，但是要*挑選*元素（貪婪計算方向翻轉次數） |
| LC 280 - Wiggle Sort | 原地交換違規的相鄰元素，建構出交錯陣列 |
| LC 324 - Wiggle Sort II | 嚴格交錯 + 不允許相鄰相等 → 先排序(sorting)再交錯兩半 |
| LC 1846 - Maximum Element After Decreasing and Rearranging | 在 `|adjacent diff| <= 1` 下最大化最後一個元素 → 貪婪取 `prev + 1` |
| LC 1936 - Add Minimum Number of Rungs | 相鄰間距上限為 `dist` → 每個間距 `ceil(gap/dist) - 1`（用算的，不是模擬） |
| LC 453 - Minimum Moves to Equal Array Elements | 把「增加 n-1 個」重新想成「減少 1 個」→ `sum - n*min`，O(1) 數學 |
| LC 462 - Minimum Moves to Equal Array Elements II | 移向中位數；用閉合式算成本，而不是逐一嘗試目標值 |

#### 1-1-7) 把 `n` 盡量平均拆成 `k` 份 → **最大乘積**（`divmod` 模式）⭐⭐⭐⭐⭐

**題型形態（LC 343 - Integer Break）：**
把 `n` 拆成 `k >= 2` 個正整數的和，並最大化它們的乘積。

##### 核心想法

兩個彼此獨立的問題 — 分開解：

1. **在 `k` 固定的前提下，每份該多大？** → **盡量平均**（`divmod`）
2. **哪個 `k` 最好？** → 全部試一遍（`O(n²)`），或用數學（`O(log n)`，見下）

**為什麼「盡量平均」是最佳解（交換論證）：**

在總和固定、份數固定的情況下，所有份數相等時乘積最大（**算幾不等式 AM–GM**）。但用*整數*沒辦法永遠完全相等，所以各份之間最多只能相差 **1**。

```text
Proof sketch — suppose two parts differ by >= 2, i.e.  a >= b + 2
Replace (a, b) with (a-1, b+1)  -> sum is unchanged

  (a-1)(b+1) = ab + a - b - 1
             >= ab + 1          , since a - b >= 2

  -> product STRICTLY increases
  -> keep rebalancing until every pair differs by <= 1
```

**所以整個拆法完全由 `divmod` 決定：**

```text
q, r = divmod(n, k)          # q = n // k  (商數), r = n % k  (餘數)

  ->  r     parts have value (q + 1)      <-- the "leftover" r units are spread
  ->  (k-r) parts have value  q               one-by-one over r parts

Sanity check the sum:
  r*(q+1) + (k-r)*q  =  r*q + r + k*q - r*q  =  k*q + r  =  n   ✅

Max product for that k:
  P(n, k) = (q + 1)^r  *  q^(k - r)
```

##### 圖解範例

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

對 `n = 10` 把 `k` 掃過一遍：

| k | `divmod(10, k)` = (q, r) | 各份 | 乘積 |
|---|--------------------------|-------|---------|
| 2 | (5, 0) | `[5, 5]` | 25 |
| 3 | (3, 1) | `[4, 3, 3]` | **36** ⭐ |
| 4 | (2, 2) | `[3, 3, 2, 2]` | 36 ⭐ |
| 5 | (2, 0) | `[2, 2, 2, 2, 2]` | 32 |
| 6 | (1, 4) | `[2, 2, 2, 2, 1, 1]` | 16 |
| 7 | (1, 3) | `[2, 2, 2, 1, 1, 1, 1]` | 8 |

→ 答案 = 36。注意乘積是**先升後降** — 每份接近 3 時是甜蜜點（見下面的「為什麼是 3？」）。

##### 模式 — 可重複使用的 `divmod` 輔助函式

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

##### 套用 — LC 343 Integer Break

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

> **關於迴圈上界的註記：** `range(2, n + 1)` 是安全／完整的範圍（`k = n` → 全部都是 1）。
> `range(2, n)` 在這題也會過，因為全 1 的乘積是 `1`，永遠贏不過
> `max_product` 的初始值 `1`。但 `k <= n` 才是正確的通用上界 —
> `k > n` 不可能發生，因為每一份都必須 `>= 1`。

##### 為什麼答案是「盡量多的 **3**」— O(log n) 捷徑

上面的 `divmod` 掃描告訴你在給定 `k` 時*怎麼*拆；一點微積分則告訴你*哪種份數大小*會贏，於是整個迴圈都能省掉。

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

**小心處理餘數** — 千萬別留下一個孤零零的 `1`：

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

##### 解法比較

| 做法 | 時間 | 空間 | 備註 |
|----------|------|-------|------|
| 對所有 `k` 暴力 `divmod` | `O(n²)` | `O(1)` | **最直覺**；就是上面的 `get_product` 模式 |
| DP `dp[x] = max(3*dp[x-3], 2*dp[x-2])` | `O(n)` | `O(n)` | 安全的後備方案；注意 `dp[3] = 3`（不是 2） |
| 數學（盡量多的 3） | `O(log n)` | `O(1)` | 需要 `n % 3 == 1 → 改用 4` 這個洞察 |

##### 常見陷阱

| 陷阱 | 為什麼會壞 | 修正 |
|---------|---------------|-----|
| 對 `n = 2, 3` 直接回傳 `n` | `k >= 2` 是**強制**的 — 你一定得拆 | `if n < 4: return n - 1` |
| `n % 3 == 1` → `3^(n//3) * 1` | 乘以 `1` 等於浪費一個單位 | 借回一個 3：`3^(n//3 - 1) * 4` |
| DP 裡寫 `dp[3] = 2` | 在 `dp` 內部，`3` 可以當成**完整**的因數保留 | `dp[3] = 3`（只有最終答案才被強制要拆） |
| 拆得不平均（`[8,1,1]`） | 違反 AM–GM | `divmod` → `r` 份 `q+1`、`k-r` 份 `q` |
| Java `int` 溢位 | 對較大的 `n`，`3^19` 已經超出 `int` | 用 `long` / Python 的大整數 |

##### 相似題目

| 題目 | LC# | 關鍵差異 |
|---------|-----|----------------|
| **Integer Break** | **343** | **整數分拆的最大乘積 — 基礎模式** |
| Maximize Number of Nice Divisors | 1808 | 同樣的「盡量多的 3」技巧，但答案要 `mod 1e9+7` → 需要快速冪 |
| Maximum Product After K Increments | 2233 | 最大乘積 ⇒ 讓各值**盡量平均** — 永遠對當前最小值做遞增（堆積(heap)） |
| Minimum Moves to Equal Array Elements II | 462 | 朝**中位數**拉平；用成本公式取代模擬 |
| Minimize Maximum of Array | 2439 | 把前綴平均攤開 → `ceil(prefixSum / count)` |
| Split Array Largest Sum | 410 | 拆成 `k` 段並最小化最大段和 — 二分搜尋（每段大小不自由） |
| Capacity To Ship Packages Within D Days | 1011 | 同樣是「在限制下平均拆分」的形態，用二分搜尋解 |
| Maximum Candies Allocated to K Children | 2226 | 對每份大小二分搜尋；`divmod` 用來數塞得下幾份 |
| Divide Array in Sets of K Consecutive Numbers | 1296 | 一樣是平均分組，但依數值相鄰性（貪婪 + 計數器） |
| Fair Distribution of Cookies | 2305 | 物品*固定*的平均分配 → 回溯（用不了 `divmod`） |
| Distribute Candies Among Children | 2929 | 是在數拆法數量，不是最大化乘積（組合數學／排容原理） |

**關鍵心得（可遷移）：**
- **總和固定 + 份數固定 → 最大化乘積 ⇒ 讓各份相等。** 用整數就是 `divmod`：`r` 份 `q+1`、`k-r` 份 `q`。
- 交換論證（`a >= b+2` ⇒ 重新平衡必定更好）是面試中*證明*「平均最佳」的標準做法。
- 反向也成立：在總和固定下要**最小化**乘積／平方和，就讓各份盡量**不平均**；要**最小化最大值**，就讓它們盡量平均。
- 當只有*相鄰*結構重要時，找閉合式（`O(1)`/`O(log n)`）而不是硬跑迴圈 — 和 [1-1-6](#1-1-6-greedy-zigzag-construction--closed-form-o1-instead-of-simulation) 是同一套哲學。

#### 1-1-8) 快速冪（二進位取冪）⭐⭐⭐⭐⭐

**模式：** 用「底數平方、指數折半」的方式，在 `O(log n)` 而不是 `O(n)` 內算出 `x^n`。

**核心想法 — 把指數看成二進位：**

```text
x^n  =  (x^2)^(n/2)              , n even
     =  x * (x^2)^((n-1)/2)      , n odd

-> equivalently: n = sum of powers of 2 (its binary bits)
   x^13 = x^(1101b) = x^8 * x^4 * x^1
```

所以維護一個滾動的 `res`，每當 `n` 當前的位元是 1，就把當前的平方值乘進 `res`：

```text
x = 2, n = 13 (1101b)

step | bit | x (running square) | res
-----|-----|--------------------|------------
  0  |  1  | 2                  | 1 * 2   = 2       <- x^1
  1  |  0  | 4                  | 2                 (skip)
  2  |  1  | 16                 | 2 * 16  = 32      <- x^1 * x^4
  3  |  1  | 256                | 32 * 256 = 8192   <- x^1 * x^4 * x^8

2^13 = 8192  ✅
```

```java
// java
// LC 50 - Pow(x, n)
// IDEA: FAST POWER (BINARY EXPONENTIATION) - square the base, halve the exponent
// time = O(log n), space = O(1)
public double myPow(double x, int n) {
    /** NOTE !!! promote to long FIRST
     *  -n overflows when n == Integer.MIN_VALUE (-2^31 has no positive int)
     */
    long N = n;
    if (N < 0) {
        x = 1 / x;
        N = -N;
    }

    double res = 1.0;
    while (N > 0) {
        // if current bit is 1 -> accumulate the current square
        if ((N & 1) == 1) {
            res *= x;
        }
        x *= x;      // x -> x^2 -> x^4 -> x^8 ...
        N >>= 1;
    }
    return res;
}
```

```python
# python
# LC 50. Pow(x, n)
# IDEA: FAST POWER (BINARY EXPONENTIATION)
# time = O(log n), space = O(1)
class Solution(object):
    def myPow(self, x, n):
        # negative exponent -> invert the base
        if n < 0:
            x, n = 1 / x, -n

        res = 1.0
        while n:
            ### NOTE !!! if current bit is 1, accumulate the current square
            if n & 1:
                res *= x
            x *= x        # x -> x^2 -> x^4 -> x^8 ...
            n >>= 1
        return res
```

##### 變形 — **模**快速冪（`x^n mod m`）

變化點：在模數下相乘，讓中間值永遠不會溢位。這正是 LC 1808（在 [1-1-7](#1-1-7-split-n-into-k-parts-as-evenly-as-possible--max-product-divmod-pattern-) 提到過）需要的輔助函式，它也是用費馬小定理求模反元素的基礎：質數 `m` 時 `inv(a) = a^(m-2) mod m`。

```java
// java
// GENERAL PATTERN: (base ^ exp) % mod
// DOMAIN: exp >= 0, mod > 0, mod < ~3e9 (so `res * base` fits in a long).
//         Negative base -> use Math.floorMod(base, mod); negative exp needs a modular inverse.
// time = O(log exp), space = O(1)
long powMod(long base, long exp, long mod) {
    long res = 1 % mod;      // NOTE: `1 % mod`, not `1` -> handles mod == 1
    base %= mod;
    while (exp > 0) {
        if ((exp & 1) == 1) {
            res = res * base % mod;   // use long, `int * int` overflows
        }
        base = base * base % mod;
        exp >>= 1;
    }
    return res;
}
```

```python
# python
# GENERAL PATTERN: (base ** exp) % mod
# DOMAIN: exp >= 0, mod > 0. A negative `exp` never terminates here
#         (`exp >>= 1` on a negative int floors to -1 forever) -> use a modular inverse instead.
# time = O(log exp), space = O(1)
def pow_mod(base, exp, mod):
    res = 1 % mod
    base %= mod
    while exp:
        if exp & 1:
            res = res * base % mod
        base = base * base % mod
        exp >>= 1
    return res

# python builtin does exactly this:
#   pow(base, exp, mod)
```

**常見陷阱：**

| 陷阱 | 為什麼會壞 | 修正 |
|---------|---------------|-----|
| 對 `int` 做 `n = -n` | `Integer.MIN_VALUE` 沒有對應的正 `int` | 取負**之前**先轉成 `long` |
| 遞迴時沒有把 `half` 記下來 | `myPow(x, n/2) * myPow(x, n/2)` 會重算 → `O(n)` | `half` 只算**一次**，再做 `half * half` |
| 在模運算下用 `int` 做 `res * base` | 在 `%` 執行前就溢位了 | 全程都用 `long` |
| `n < 0` 時忘了 `x = 1/x` | 會回傳 `x^abs(n)` | 一開始就先把底數取倒數 |

#### 1-1-9) 最大公因數(GCD) / 最小公倍數(LCM) — 輾轉相除法 ⭐⭐⭐⭐

**模式：** `gcd(a, b) = gcd(b, a % b)`，終止於 `gcd(a, 0) = a`。時間為 `O(log(min(a,b)))`。

**為什麼可行：** `a` 和 `b` 的任何公因數也會整除 `a - k*b`，所以把 `a` 換成 `a % b` 之後，公因數的集合不變。每一步至少會把較大的值砍半 → 對數時間。

```text
gcd(48, 18)
  -> gcd(18, 48 % 18 = 12)
  -> gcd(12, 18 % 12 = 6)
  -> gcd(6,  12 % 6  = 0)
  -> 6

lcm(a, b) = a * b / gcd(a, b)
          = a / gcd(a, b) * b        <-- divide FIRST to avoid overflow
```

```java
// java
// GENERAL PATTERN: Euclid gcd / lcm
// time = O(log(min(a,b))), space = O(1)
int gcd(int a, int b) {
    a = Math.abs(a);
    b = Math.abs(b);
    while (b != 0) {
        int t = a % b;
        a = b;
        b = t;
    }
    return a;          // NOTE: gcd(x, 0) == x, gcd(0, 0) == 0
}                      // (caveat: Math.abs(Integer.MIN_VALUE) stays negative -> use long if that's reachable)

long lcm(int a, int b) {
    // NOTE !!! lcm with 0 is 0 -> guard first, else gcd(0, 0) == 0 divides by zero
    if (a == 0 || b == 0) {
        return 0;
    }
    // NOTE !!! divide BEFORE multiplying, else a*b can overflow
    return (long) Math.abs(a) / gcd(a, b) * Math.abs(b);
}
```

```python
# python
# GENERAL PATTERN: Euclid gcd / lcm
# time = O(log(min(a,b))), space = O(1)
def my_gcd(a, b):
    a, b = abs(a), abs(b)
    while b:
        a, b = b, a % b
    return a

def my_lcm(a, b):
    ### NOTE !!! lcm with 0 is 0 -> guard first, else my_gcd(0, 0) == 0 divides by zero
    if a == 0 or b == 0:
        return 0
    return abs(a) // my_gcd(a, b) * abs(b)

# python builtin:
#   from math import gcd, lcm      # lcm needs python 3.9+
```

##### 套用 — LC 149 Max Points on a Line

**關鍵想法：** 絕對不要用浮點數比較斜率（`dy/dx` 在垂直線會炸掉，而且會失去精度）。改用**約分後的方向向量** `(dx/g, dy/g)`，其中 `g = gcd(dx, dy)`，再**正規化正負號**，讓 `(1, 2)` 和 `(-1, -2)` 雜湊到同一條線。

```java
// java
// LC 149 - Max Points on a Line
// IDEA: GCD-REDUCED SLOPE AS HASH KEY (no float division)
// time = O(n^2), space = O(n)
public int maxPoints(int[][] points) {
    int n = points.length;
    if (n <= 2) {
        return n;
    }

    int best = 1;
    for (int i = 0; i < n; i++) {
        // slope -> how many points share it with points[i]
        Map<String, Integer> cnt = new HashMap<>();
        for (int j = i + 1; j < n; j++) {
            int dx = points[j][0] - points[i][0];
            int dy = points[j][1] - points[i][1];

            /** NOTE !!! reduce by gcd -> canonical direction vector */
            int g = gcd(dx, dy);
            if (g != 0) {
                dx /= g;
                dy /= g;
            }

            /** NOTE !!! normalize sign, so (1,2) and (-1,-2) are the SAME line */
            if (dx < 0 || (dx == 0 && dy < 0)) {
                dx = -dx;
                dy = -dy;
            }

            int c = cnt.merge(dx + "/" + dy, 1, Integer::sum);
            best = Math.max(best, c + 1);   // +1 -> include points[i] itself
        }
    }
    return best;
}
```

```python
# python
# LC 149. Max Points on a Line
# IDEA: GCD-REDUCED SLOPE AS HASH KEY (no float division)
# time = O(n^2), space = O(n)
from math import gcd

class Solution(object):
    def maxPoints(self, points):
        n = len(points)
        if n <= 2:
            return n

        best = 1
        for i in range(n):
            cnt = {}
            x1, y1 = points[i]
            for j in range(i + 1, n):
                dx = points[j][0] - x1
                dy = points[j][1] - y1

                ### NOTE !!! reduce by gcd -> canonical direction
                g = gcd(abs(dx), abs(dy))
                if g:
                    dx //= g
                    dy //= g

                ### NOTE !!! normalize sign
                if dx < 0 or (dx == 0 and dy < 0):
                    dx, dy = -dx, -dy

                cnt[(dx, dy)] = cnt.get((dx, dy), 0) + 1
                best = max(best, cnt[(dx, dy)] + 1)   # +1 -> points[i] itself
        return best
```

##### 變形 — LC 365 Water and Jug Problem（**貝祖等式**）

變化點：與其在狀態上做 BFS，不如注意到每個可達的水量都是 `a*x + b*y`（`a, b` 為整數），而貝祖定理說這個集合恰好就是 `gcd(x, y)` 的倍數。於是整題塌縮成一次 `gcd` 檢查 — `O(log(min(x,y)))` 而不是 BFS。

```java
// java
// LC 365 - Water and Jug Problem
// IDEA: BEZOUT -> reachable amounts == multiples of gcd(x, y)
// time = O(log(min(x,y))), space = O(1)
public boolean canMeasureWater(int x, int y, int target) {
    if (target == 0) {
        return true;                       // NOTE: guard first, gcd(0,0) == 0
    }
    if ((long) x + y < target) {
        return false;                      // can't hold more than both jugs
    }
    return target % gcd(x, y) == 0;
}
```

```python
# python
# LC 365. Water and Jug Problem
# IDEA: BEZOUT -> reachable amounts == multiples of gcd(x, y)
# time = O(log(min(x,y))), space = O(1)
from math import gcd

class Solution(object):
    def canMeasureWater(self, x, y, target):
        if target == 0:
            return True                    # guard first (gcd(0,0) == 0)
        if x + y < target:
            return False
        return target % gcd(x, y) == 0
```

**GCD 會出現在哪些地方：**

| 題目 | gcd 的用途 |
|---------|-----------|
| LC 149 - Max Points on a Line | 把 `(dx, dy)` 約分成標準化的斜率鍵值 |
| LC 365 - Water and Jug Problem | 貝祖：可達 ⟺ 是 `gcd(x, y)` 的倍數 |
| LC 1015 - Smallest Integer Divisible by K | 見 [1-1-3](#1-1-3-keep-remainder-to-avoid-overflow-repunit--modular-arithmetic) — 那個 `k % 2 / k % 5` 守衛其實就是 `gcd(k, 10) > 1` 的論證 |

#### 1-1-10) 整數平方根 — 二分搜尋與牛頓法 ⭐⭐⭐⭐

**模式：** `isqrt(x)` = 使 `r*r <= x` 成立的最大 `r`。兩套標準模板。

**模板 A — 對答案做二分搜尋**（可遷移的那一套：適用任何單調的判定條件，例如立方根、「最小的除數」、容量類題目）：

```java
// java
// LC 69 - Sqrt(x)
// IDEA: BINARY SEARCH ON ANSWER, keep last feasible mid
// time = O(log x), space = O(1)
public int mySqrt(int x) {
    if (x < 2) {
        return x;                    // 0 -> 0, 1 -> 1
    }
    int lo = 1, hi = x / 2, ans = 1; // NOTE: for x >= 2, isqrt(x) <= x/2
    while (lo <= hi) {
        int mid = lo + (hi - lo) / 2;
        /** NOTE !!! cast to long -> mid * mid overflows int */
        long sq = (long) mid * mid;
        if (sq == x) {
            return mid;
        } else if (sq < x) {
            ans = mid;               // feasible -> remember, then push right
            lo = mid + 1;
        } else {
            hi = mid - 1;
        }
    }
    return ans;
}
```

```python
# python
# LC 69. Sqrt(x)
# IDEA: BINARY SEARCH ON ANSWER, keep last feasible mid
# time = O(log x), space = O(1)
class Solution(object):
    def mySqrt(self, x):
        if x < 2:
            return x
        lo, hi, ans = 1, x // 2, 1
        while lo <= hi:
            mid = lo + (hi - lo) // 2
            sq = mid * mid
            if sq == x:
                return mid
            elif sq < x:
                ans = mid            # feasible -> remember it
                lo = mid + 1
            else:
                hi = mid - 1
        return ans
```

**模板 B — 牛頓法**（二次收斂，約 5 行；面試中的「一行解」）：

```text
r_next = (r + x / r) / 2      # integer division is fine, it converges DOWN to isqrt(x)
loop while r * r > x
```

```java
// java
// LC 69 - Sqrt(x)  (Newton)
// time = O(log x), space = O(1)
public int mySqrtNewton(int x) {
    long r = x;                     // NOTE: long, r*r overflows int
    while (r * r > x) {
        r = (r + x / r) / 2;
    }
    return (int) r;                 // x == 0 -> loop never runs -> 0
}
```

```python
# python
# LC 69. Sqrt(x)  (Newton)
# time = O(log x), space = O(1)
class Solution(object):
    def mySqrt(self, x):
        r = x
        while r * r > x:
            r = (r + x // r) // 2
        return r
```

##### 變形 — LC 367 Valid Perfect Square

變化點：同一個牛頓迴圈，但答案是*相等性*檢查而不是取下界值。

```java
// java
// LC 367 - Valid Perfect Square
// time = O(log num), space = O(1)
public boolean isPerfectSquare(int num) {
    long r = num;
    while (r * r > num) {
        r = (r + num / r) / 2;
    }
    return r * r == num;            // only difference vs LC 69
}
```

```python
# python
# LC 367. Valid Perfect Square
# time = O(log num), space = O(1)
class Solution(object):
    def isPerfectSquare(self, num):
        r = num
        while r * r > num:
            r = (r + num // r) // 2
        return r * r == num
```

##### 變形 — LC 633 Sum of Square Numbers

變化點：在 `[0, isqrt(c)]` 上用雙指標 — 上界往下縮、下界往上長，`O(sqrt(c))`。

```python
# python
# LC 633. Sum of Square Numbers
# IDEA: TWO POINTERS on [0, isqrt(c)]
# time = O(sqrt(c)), space = O(1)
from math import isqrt

class Solution(object):
    def judgeSquareSum(self, c):
        a, b = 0, isqrt(c)
        while a <= b:
            cur = a * a + b * b
            if cur == c:
                return True
            elif cur < c:
                a += 1               # need bigger -> raise the low end
            else:
                b -= 1               # too big -> lower the high end
        return False
```

```java
// java
// LC 633 - Sum of Square Numbers
// time = O(sqrt(c)), space = O(1)
public boolean judgeSquareSum(int c) {
    long a = 0, b = (long) Math.sqrt(c);
    while (a <= b) {
        long cur = a * a + b * b;
        if (cur == c) {
            return true;
        } else if (cur < c) {
            a++;
        } else {
            b--;
        }
    }
    return false;
}
```

#### 1-1-11) 隨機取樣模板 ⭐⭐⭐⭐⭐

四套模板幾乎涵蓋所有「回傳一個隨機的 ...」面試題。共通的技巧是：**從*陣列*裡均勻取樣，並讓陣列保持緊密無空洞**。

| 目標 | 模板 | LC |
|------|----------|-----|
| 均勻隨機排列 | Fisher–Yates（由後往前交換） | 384 |
| 依**權重**取隨機索引 | 前綴和 + 二分搜尋 | 528 |
| 取某個值的隨機索引，**串流／O(1) 空間** | 蓄水池取樣 | 398 |
| `insert` / `remove` / `getRandom` 全部 `O(1)` | 陣列 + `val -> index` 對照表，與最後一個交換 | 380 |

##### A) Fisher–Yates 洗牌 — LC 384

**關鍵想法：** 從尾端往前走；對每個 `i`，把 `a[i]` 和隨機的 `a[j]` 交換，其中 `j ∈ [0, i]`（**含端點**）。每種排列的機率恰好都是 `1/n!`。

```java
// java
// LC 384 - Shuffle an Array
// IDEA: FISHER-YATES (swap a[i] with a random j in [0, i])
// time = O(n) per shuffle, space = O(n)
class Solution {
    private final int[] original;
    private final Random rand = new Random();

    public Solution(int[] nums) {
        this.original = nums.clone();
    }

    public int[] reset() {
        return original.clone();
    }

    public int[] shuffle() {
        int[] a = original.clone();
        for (int i = a.length - 1; i > 0; i--) {
            /** NOTE !!! nextInt(i + 1) -> j in [0, i] INCLUSIVE
             *  using nextInt(n) here (a fixed bound) makes it NON-uniform
             */
            int j = rand.nextInt(i + 1);
            int t = a[i];
            a[i] = a[j];
            a[j] = t;
        }
        return a;
    }
}
```

```python
# python
# LC 384. Shuffle an Array
# IDEA: FISHER-YATES
# time = O(n) per shuffle, space = O(n)
import random

class Solution(object):
    def __init__(self, nums):
        self.original = nums[:]

    def reset(self):
        return self.original[:]

    def shuffle(self):
        arr = self.original[:]
        for i in range(len(arr) - 1, 0, -1):
            ### NOTE !!! randint is INCLUSIVE on both ends -> j in [0, i]
            j = random.randint(0, i)
            arr[i], arr[j] = arr[j], arr[i]
        return arr
```

##### B) 加權隨機取樣 — LC 528

**關鍵想法：** 把權重轉成**前綴和**，然後往 `[1, total]` 射一支飛鏢，再二分搜尋**最左邊 >= 目標值的前綴**。權重 `w[i]` 恰好佔掉 `total` 個格子中的 `w[i]` 個。

```text
w       = [1, 3, 2]
prefix  = [1, 4, 6]      total = 6

target: 1        -> idx 0   (prob 1/6)
        2,3,4    -> idx 1   (prob 3/6)
        5,6      -> idx 2   (prob 2/6)
```

```java
// java
// LC 528 - Random Pick with Weight
// IDEA: PREFIX SUM + BINARY SEARCH (leftmost prefix >= target)
// time = O(n) build / O(log n) per pick, space = O(n)
class Solution {
    private final int[] prefix;
    private final int total;
    private final Random rand = new Random();

    public Solution(int[] w) {
        prefix = new int[w.length];
        int s = 0;
        for (int i = 0; i < w.length; i++) {
            s += w[i];
            prefix[i] = s;
        }
        total = s;
    }

    public int pickIndex() {
        /** NOTE !!! target in [1, total] (nextInt is exclusive on the bound) */
        int target = rand.nextInt(total) + 1;

        int lo = 0, hi = prefix.length - 1;
        while (lo < hi) {               // lower-bound style: NO `lo <= hi`
            int mid = lo + (hi - lo) / 2;
            if (prefix[mid] < target) {
                lo = mid + 1;
            } else {
                hi = mid;               // NOTE: hi = mid (not mid - 1)
            }
        }
        return lo;
    }
}
```

```python
# python
# LC 528. Random Pick with Weight
# IDEA: PREFIX SUM + BINARY SEARCH (leftmost prefix >= target)
# time = O(n) build / O(log n) per pick, space = O(n)
import random

class Solution(object):
    def __init__(self, w):
        self.prefix = []
        s = 0
        for x in w:
            s += x
            self.prefix.append(s)
        self.total = s

    def pickIndex(self):
        target = random.randint(1, self.total)   # inclusive [1, total]
        lo, hi = 0, len(self.prefix) - 1
        while lo < hi:
            mid = lo + (hi - lo) // 2
            if self.prefix[mid] < target:
                lo = mid + 1
            else:
                hi = mid
        return lo

# builtin shortcut (still O(log n)): bisect.bisect_left(self.prefix, target)
```

##### C) 蓄水池取樣 — LC 398

**關鍵想法：** 要從**長度未知的串流**中以 **`O(1)` 空間**均勻挑出 1 個元素：當你看到第 `k` 個符合條件的元素時，以 `1/k` 的機率保留它。

```text
Proof for k = 3 items:
  P(keep #3) = 1/3
  P(keep #2) = 1/2 * (1 - 1/3) = 1/2 * 2/3 = 1/3
  P(keep #1) = 1   * (1 - 1/2) * (1 - 1/3) = 1/2 * 2/3 = 1/3   ✅ uniform
```

```java
// java
// LC 398 - Random Pick Index
// IDEA: RESERVOIR SAMPLING (keep the k-th match with prob 1/k)
// time = O(n) per pick, space = O(1) extra
class Solution {
    private final int[] nums;
    private final Random rand = new Random();

    public Solution(int[] nums) {
        this.nums = nums;
    }

    public int pick(int target) {
        int count = 0, res = -1;
        for (int i = 0; i < nums.length; i++) {
            if (nums[i] != target) {
                continue;
            }
            count++;
            /** NOTE !!! nextInt(count) == 0 happens with prob 1/count */
            if (rand.nextInt(count) == 0) {
                res = i;
            }
        }
        return res;
    }
}
```

```python
# python
# LC 398. Random Pick Index
# IDEA: RESERVOIR SAMPLING (keep the k-th match with prob 1/k)
# time = O(n) per pick, space = O(1) extra
import random

class Solution(object):
    def __init__(self, nums):
        self.nums = nums

    def pick(self, target):
        count = 0
        res = -1
        for i, v in enumerate(self.nums):
            if v != target:
                continue
            count += 1
            ### NOTE !!! keep index i with probability 1 / count
            if random.randint(1, count) == 1:
                res = i
        return res
```

> **取捨：** 用 `value -> [indices]` 雜湊表可以讓 `pick` 變成 `O(1)`，但要付出 `O(n)` 記憶體。
> 當面試官補上*「陣列非常大／這是一個串流」*時，蓄水池取樣才是他想要的答案。

##### D) `insert` / `remove` / `getRandom` 都做到 O(1) — LC 380

**關鍵想法：** `getRandom` 需要一個**緊密的陣列**（索引 → 值）；`remove` 需要一個**對照表**（值 → 索引）。要在 `O(1)` 內刪除又不留下空洞，就**把被刪的元素和最後一個交換，再 pop 掉**。

```text
arr = [a, b, c, d]      idx = {a:0, b:1, c:2, d:3}
remove(b):
  1) move last (d) into b's slot ->  arr = [a, d, c, d]  idx[d] = 1
  2) pop the tail               ->  arr = [a, d, c]
  3) drop b from the map        ->  idx = {a:0, d:1, c:2}   ✅ still dense
```

```java
// java
// LC 380 - Insert Delete GetRandom O(1)
// IDEA: ARRAY (dense) + MAP (val -> index), delete by SWAP-WITH-LAST
// time = O(1) all ops, space = O(n)
class RandomizedSet {
    private final List<Integer> arr = new ArrayList<>();
    private final Map<Integer, Integer> idx = new HashMap<>();   // val -> position
    private final Random rand = new Random();

    public boolean insert(int val) {
        if (idx.containsKey(val)) {
            return false;
        }
        idx.put(val, arr.size());
        arr.add(val);
        return true;
    }

    public boolean remove(int val) {
        Integer i = idx.get(val);
        if (i == null) {
            return false;
        }
        /** NOTE !!! move the LAST element into the hole, then pop the tail */
        int last = arr.get(arr.size() - 1);
        arr.set(i, last);
        idx.put(last, i);
        arr.remove(arr.size() - 1);   // remove by INDEX -> O(1)
        idx.remove(val);              // NOTE: remove val AFTER the put above
        return true;
    }

    public int getRandom() {
        return arr.get(rand.nextInt(arr.size()));
    }
}
```

```python
# python
# LC 380. Insert Delete GetRandom O(1)
# IDEA: ARRAY (dense) + MAP (val -> index), delete by SWAP-WITH-LAST
# time = O(1) all ops, space = O(n)
import random

class RandomizedSet(object):
    def __init__(self):
        self.arr = []
        self.idx = {}                 # val -> position in arr

    def insert(self, val):
        if val in self.idx:
            return False
        self.idx[val] = len(self.arr)
        self.arr.append(val)
        return True

    def remove(self, val):
        if val not in self.idx:
            return False
        ### NOTE !!! overwrite the hole with the last element, then pop
        i = self.idx[val]
        last = self.arr[-1]
        self.arr[i] = last
        self.idx[last] = i
        self.arr.pop()
        del self.idx[val]             # NOTE: delete AFTER re-pointing `last`
        return True

    def getRandom(self):
        return self.arr[random.randint(0, len(self.arr) - 1)]
```

**常見陷阱：**

| 陷阱 | 為什麼會壞 | 修正 |
|---------|---------------|-----|
| Fisher–Yates 用 `rand.nextInt(n)`（固定上界） | 有偏差 — 不是所有 `n!` 種排列都等機率 | 上界必須遞減：`nextInt(i + 1)` |
| LC 528 飛鏢射在 `[0, total)` 卻用 `<=` 比較 | 差一錯誤 → 權重為零的項目也可能被選到 | 取 `[1, total]` + 最左邊 `prefix >= target` |
| LC 528 的二分搜尋寫成 `lo <= hi` | 那是精確比對的搜尋；這裡一般不會有某個前綴剛好等於目標值 | 用 lower-bound 形式：`while lo < hi`、`hi = mid` |
| LC 380 在 `self.idx[last] = i` **之前**就 `del self.idx[val]` | 當 `val == last` 時，你會刪掉剛剛才寫入的那筆 | 先重新指向 `last`，最後才刪 `val` |
| Java 裡 LC 380 用 `list.remove(value)` | 那是依值刪除 → `O(n)`，而且對 `Integer` 語意也不對 | 用 `arr.remove(arr.size() - 1)`（依索引） |

#### 1-1-12) 速查 — 其他高頻的數學類 LC

一些不需要完整模板的小模式，外加指向姊妹作弊表的連結（避免在這裡重複）。

| 題目 | 一句話模式 | 另見 |
|---------|------------------|----------|
| LC 9 - Palindrome Number | 只反轉**一半**的位數（`while x > rev`），再比較 `x == rev \|\| x == rev/10`；不用字串、不會溢位 | — |
| LC 172 - Factorial Trailing Zeroes | 零的個數 = 因數 **5** 的個數（2 多得是）：`while n: n //= 5; res += n`（勒讓德公式） | [1-1-1](#1-1-1-check-prime-number) |
| LC 202 - Happy Number | 反覆取各位數平方和直到 1 或重複 → **循環偵測**（雜湊集合，或 Floyd 快慢指標） | — |
| LC 12 - Integer to Roman | 對遞減的數值／符號表做貪婪（含 `900/400/90/40/9/4`） | [2-3-1](#2-3-1-integer-to-roman--lc-12) |
| LC 171 - Excel Sheet Column Number | 單純的 26 進位 Horner 法：`res = res*26 + (ch - 'A' + 1)` | [2-1-1](#2-1-1-excel-sheet-column-number--lc-171) |
| LC 2 / 66 / 67 / 415 / 43 / 7 | 逐位帶進位的加法／乘法、反轉整數 | `add_x_sum.md` |
| LC 62 / 60 | `C(n, k)` 路徑數、階乘進位制 | `combinatorics_math_patterns.md` |
| LC 89 / 477 | 格雷碼（`i ^ (i >> 1)`）、依位元欄位算漢明距離 | `bit_manipulation.md` |

## 2) LC 範例

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

### 2-1-1) Excel Sheet Column Number — LC 171

> **[2-1](#2-1-excel-sheet-column-title--lc-168) 的變形：** *反*方向（標題 → 數字）。這是簡單的那個方向 — **沒有 `n-1` / `r == 0` 的修正**，因為你根本不用做除法。就是單純的 26 進位 Horner 累加，只是位數是 `A..Z = 1..26` 而不是 `0..25`。

```java
// java
// LC 171 - Excel Sheet Column Number
// IDEA: BASE-26 HORNER (A..Z == 1..26, i.e. "bijective base 26")
// time = O(n), space = O(1)
public int titleToNumber(String columnTitle) {
    int res = 0;
    for (char ch : columnTitle.toCharArray()) {
        /** NOTE !!! `+ 1` -> 'A' is 1 (NOT 0); that is why 168 needs the -1 fixup */
        res = res * 26 + (ch - 'A' + 1);
    }
    return res;
}
```

```python
# python
# LC 171. Excel Sheet Column Number
# IDEA: BASE-26 HORNER (A..Z == 1..26)
# time = O(n), space = O(1)
class Solution(object):
    def titleToNumber(self, columnTitle):
        res = 0
        for ch in columnTitle:
            ### NOTE !!! 'A' maps to 1, not 0
            res = res * 26 + (ord(ch) - ord('A') + 1)
        return res

# "A"  -> 1
# "AB" -> 1*26 + 2  = 28
# "ZY" -> 26*26 + 25 = 701
```

**為什麼 168 比 171 難 — 「雙射的 26 進位」沒有數字 `0`：**

```text
encode (168):  n -> title    needs  (n-1) % 26  and  n = (n-1) // 26
                             because a "digit" of 26 (Z) must borrow from the next place

decode (171):  title -> n    no fixup: res = res*26 + d,  d in [1, 26]
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

### 2-3-1) Integer to Roman — LC 12

> **[2-3](#2-3-roman-to-integer--lc-13) 的變形：** *反*方向（數字 → 羅馬數字）。能消掉所有特例的技巧是：把六種**減法**形式（`900 CM`、`400 CD`、`90 XC`、`40 XL`、`9 IX`、`4 IV`）**直接放進數值表裡**。之後就只是單純的遞減貪婪 — 不需要為「這是 4 還是 9」寫任何 `if`。

```java
// java
// LC 12 - Integer to Roman
// IDEA: GREEDY over a DESCENDING value table that already contains 900/400/90/40/9/4
// time = O(1) (num <= 3999), space = O(1)
public String intToRoman(int num) {
    /** NOTE !!! subtractive pairs are baked into the table -> no special cases */
    int[] vals    = {1000, 900, 500, 400, 100, 90, 50, 40, 10, 9, 5, 4, 1};
    String[] syms = {"M", "CM", "D", "CD", "C", "XC", "L", "XL", "X", "IX", "V", "IV", "I"};

    StringBuilder sb = new StringBuilder();
    for (int i = 0; i < vals.length && num > 0; i++) {
        // take the biggest symbol that still fits, as many times as possible
        while (num >= vals[i]) {
            num -= vals[i];
            sb.append(syms[i]);
        }
    }
    return sb.toString();
}
```

```python
# python
# LC 12. Integer to Roman
# IDEA: GREEDY over a DESCENDING value table (subtractive forms included)
# time = O(1) (num <= 3999), space = O(1)
class Solution(object):
    def intToRoman(self, num):
        vals = [1000, 900, 500, 400, 100, 90, 50, 40, 10, 9, 5, 4, 1]
        syms = ["M", "CM", "D", "CD", "C", "XC", "L", "XL", "X", "IX", "V", "IV", "I"]

        res = []
        for v, s in zip(vals, syms):
            if num == 0:
                break
            ### NOTE !!! divmod gives "how many of this symbol" in one step
            cnt, num = divmod(num, v)
            res.append(s * cnt)
        return "".join(res)

# 3749 -> "MMMDCCXLIX"
# 1994 -> "MCMXCIV"
#    4 -> "IV"
```

**LC 12 vs LC 13（編碼 vs 解碼）：**

| | LC 12（int → roman） | LC 13（roman → int） |
|---|---|---|
| 方向 | 編碼 | 解碼 |
| 核心技巧 | 對**含**減法配對的數值表做遞減貪婪 | 由右往左掃描，`curr < prev` 時做減法 |
| 處理 `IV`/`IX`/... | 直接內建在表格裡 | 由 `curr < prev` 的比較偵測 |
| 迴圈 | 貪婪 `while num >= vals[i]` | 單次掃描，帶著 `prev` |
