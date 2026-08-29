# 位元運算

> **範圍** — 位元層級的操作，以及建立在其上的各種技巧：遮罩、XOR 恆等式、最低位 1、子集列舉，還有 bitmask DP。
> **另見**：[bit_manipulation_examples.md](./bit_manipulation_examples.md) — 支撐這些技巧的十四道詳解題；[math.md](./math.md) — 不靠位元的數值操作；[combinatorics_math_patterns.md](./combinatorics_math_patterns.md) — 計數；[dp.md](./dp.md) — bitmask DP 所屬的完整 DP 目錄。

## LeetCode 題目清單

- [Bit Manipulation](https://leetcode.com/problem-list/bit-manipulation/)
- [Bitmask](https://leetcode.com/problem-list/bitmask/)

## 總覽

位元運算直接操作整數的**二進位表示**。因為每個運算都只是一道 CPU 指令，位元技巧能把許多
`O(n)` 的掃描變成 `O(1)` 的算術，也能讓一個小整數當成最多 32/64 個旗標的緊湊**集合**
（bitmask）。

### 關鍵性質
- **時間複雜度**：每個位元運算 `O(1)`；整個字組的掃描是 `O(number of bits)` ≈ `O(32)`
- **空間複雜度**：`O(1)` — 一個遮罩重複使用同一個整數，取代陣列或集合
- **核心想法**：用 `&` `|` `^` `~` `<<` `>>` 讀取／翻轉個別位元；XOR 會讓成對的值互相抵消（`a ^ a = 0`）
- **什麼時候用**：配對／抵消類的題目、算 1 的個數、子集列舉（bitmask）、判斷 2 的次方、不用 `+` 做加法、把旗標打包進一個數字

### 速查表 — 一定要背起來的技巧 ⭐⭐⭐⭐⭐

| 目標 | 表達式 |
| ---- | ---------- |
| 測試第 `i` 位是不是 1 | `(x >> i) & 1` |
| 把第 `i` 位設為 1 | `x \| (1 << i)` |
| 把第 `i` 位清成 0 | `x & ~(1 << i)` |
| 翻轉第 `i` 位 | `x ^ (1 << i)` |
| 取出最低位的 1 | `x & -x` |
| 清掉最低位的 1 | `x & (x - 1)` |
| 是不是 2 的次方？ | `x > 0 && (x & (x - 1)) == 0` |
| 是不是偶數？ | `(x & 1) == 0` |
| XOR 自我抵消 | `a ^ a = 0`, `a ^ 0 = a` |

### 參考資料
- [LeetCode — Bit Manipulation card](https://leetcode.com/explore/learn/card/bit-manipulation/)
- [bit_manipulation.md](https://github.com/yennanliu/CS_basics/blob/master/doc/bit_manipulation.md)
- [leetcode-easy-bitwise-xor-summary](https://steveyang.blog/2022/07/02/leetcode-easy-bitwise-xor-summary/)

## 0) 概念
- 進位制
    - [Ref](https://leetcode.com/explore/learn/card/bit-manipulation/669/bit-manipulation-concepts/4494/)
    - 一個 X 進位數字的實際數值，由每一位的數字和它的位置共同決定。
    - 例子：
        - 123.45（10 進位）= 1 * 10^2 + 2 * 10^1 + 3 * 10^0 + 4 * 10^(-1) + 5 * 10^(-2)
        - 720.5（8 進位）= 7 * 8^2 + 2 * 8^1 + 0 * 8^0 + 5 * 8^(-1)
    - 在電腦科學裡最常用的是二進位，只有兩個數字：0 和 1。八進位（base-8）和十六進位（base 16）也很常用。八進位有八個數字：0、1、2、3、4、5、6、7。

- [bit VS byte VS char](http://web.ntnu.edu.tw/~algo/Bit.html)
    - 基礎
        - bit：二進位數字（以 2 為底：0, 1）
        - 十六進位數字：以 16 為底：0123456789abcdef（大小寫視為相同）
    - byte：8 bytes（字節）
    - char：16 bytes（字符）
    - 參考：
        - [java example](https://github.com/yennanliu/JavaHelloWorld/blob/main/src/main/java/Advances/IOFlow/demo1.java#L25)
- 參考
    - [bit_manipulation.md](https://github.com/yennanliu/CS_basics/blob/master/doc/bit_manipulation.md)
    - [python-operators.html](https://www.runoob.com/python/python-operators.html)
    - [leetcode-easy-bitwise-xor-summary](https://steveyang.blog/2022/07/02/leetcode-easy-bitwise-xor-summary/)

<p align="center"><img src="../pic/bit_basic1.png"></p>
<p align="center"><img src="../pic/bit_basic2.png"></p>

## 1) 核心運算

### 1-1) 六個運算子

| 運算子 | 名稱 | 規則 | 範例（4-bit） |
| -- | ---- | ---- | --------------- |
| `&`  | AND | **兩邊都是** 1 才是 1 | `0110 & 1010 = 0010` |
| `\|`  | OR  | **任一邊是** 1 就是 1 | `0110 \| 1010 = 1110` |
| `^`  | XOR | 兩邊**不同**才是 1 | `0110 ^ 1010 = 1100` |
| `~`  | NOT | 每個位元都翻轉（`~x = -x - 1`） | `~0110 = ...1001` |
| `<<` | 左移 | 尾端補 `n` 個 0 → `x * 2^n` | `0011 << 1 = 0110` |
| `>>` | 右移 | 丟掉低位的 `n` 個位元 → `x // 2^n` | `0110 >> 1 = 0011` |

> **XOR 恆等式**（許多 LC 題目的核心）：`a ^ a = 0`、`a ^ 0 = a`，
> XOR 具**交換律與結合律** → 把整份清單 XOR 起來，出現偶數次的值全部抵消，
> 只剩下出現奇數次的那一個。

### 1-2) 單一位元的技巧（附程式碼）

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

### 1-3) 計算 1 的個數（population count）

**核心想法**：`x & (x - 1)` 會清掉最低位的 1，所以迴圈**每個 1 只跑一次**
（Brian Kernighan 演算法）→ 是 `O(popcount)` 而不是 `O(32)`。

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

**視覺追蹤** — `count_bits(12)`，`12 = 1100`：

```text
x = 1100   x & (x-1) = 1100 & 1011 = 1000   count = 1
x = 1000   x & (x-1) = 1000 & 0111 = 0000   count = 2
x = 0000   stop                              → 2 set bits
```

### 1-4) 沿著位元「欄」做計數 — LC 461 / LC 477

**模式**：不要對數字兩兩配對跑迴圈，改成對 **32 個位元位置**跑迴圈，問每一欄各貢獻多少。
這能把很多看起來像 `O(n^2)` 的問題壓成 `O(32n)`。

**核心想法**：在位元位置 `i`，若有 `ones` 個數字在該位是 1、`n - ones` 個是 0，
那麼在這一位上**剛好有 `ones * (n - ones)` 對數字不同**。把 32 個位置加總即可。

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

**為什麼可行** — `[4, 14, 2]` = `00100, 01110, 00010`：

```text
bit column :  0     1     2     3
ones       :  0     2     2     1        n = 3
zeros      :  3     1     1     2
pairs      : 0*3   2*1   2*1   1*2  ->  0 + 2 + 2 + 2 = 6
```


### 1-5) 把 bitmask 當成**字元集合** — LC 318 ⭐⭐⭐⭐

**模式**：小寫字母的集合只需要 **26 個位元**，所以整個單字可以壓成一個 `int`。
接著所有集合問題都變成單一指令：

| 集合問題 | 位元表達式 |
| ------------ | -------------- |
| 兩個單字有共同字母嗎？ | `(maskA & maskB) != 0` |
| 兩者互斥嗎？ | `(maskA & maskB) == 0` |
| 字母的聯集 | `maskA \| maskB` |
| 有幾個相異字母？ | `Integer.bitCount(mask)` / `bin(mask).count("1")` |
| 單字裡有重複字母嗎？ | 建構過程中檢查：`(mask & bit) != 0` |

這把每一對都要花 `O(len)` 的字串比較，換成 `O(1)` 的 AND。

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

#### **變形 A — 逐步累積互斥遮罩的聯集（LC 1239）**

*變化點*：不是只挑**兩個**互斥的單字，而是貪婪地把**所有**可達的聯集都長出來。
維護一份可達遮罩清單；一個單字只有在 `cur & m == 0` 時才能加進某個遮罩。

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

#### **變形 B — 把固定寬度的符號打包成滾動的 int key（LC 187）**

*變化點*：字母表只有 4 個符號（`A C G T`），所以每個字元只需要 **2 個位元**，
10 個字元的視窗就是一個 20 位元的整數。用 `hash = ((hash << 2) | code) & mask` 滑動視窗
— 這是 `O(1)` 的滾動 key，不必每一步都去雜湊一段 10 字元的子字串。

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

> **更多字母遮罩的練習**（同樣的 26 位元編碼，沒有新技術）：
> LC 1255（Maximum Score Words Formed by Letters）、LC 2135（Count Words Obtained After
> Adding a Letter）、LC 1684（Count the Number of Consistent Strings — `word & ~allowed == 0`）。


## 3) Bitmask DP

**bitmask** 讓一個整數代表**一組已走訪／已選取的項目**（第 `i` 位是 1 ⇔ 項目 `i` 在集合裡）。
當 DP 狀態需要記錄「我用掉了 ≤ 約 20 個項目中的哪個子集」時，遮罩**本身就是**狀態 —
這讓指數級的子集問題能在 `O(2^n · n)` 內跑完。

### 3-1) 子集列舉（LC 78 回顧）

讓 `mask` 從 `0` 跑到 `2^n − 1`，就會**不重不漏**地走過每一個子集；用位元測試挑出成員
（見 [2-12](./bit_manipulation_examples.md#12-subsets--lc-78--the-bitmask-enumeration-)）。幾個好用的遮罩慣用寫法：

```python
# python
mask & (1 << i)          # is item i in the subset?
mask | (1 << i)          # add item i
mask & ~(1 << i)         # remove item i
bin(mask).count("1")     # size of the subset
sub = (sub - 1) & mask   # enumerate all SUB-masks of `mask` (classic trick)
```

### 3-2) TSP 型的 bitmask DP（Held–Karp）

**旅行推銷員**家族是 bitmask DP 的代表題：`dp[mask][i]` = 一條**恰好走訪 `mask` 中所有城市**
且目前停在城市 `i` 的路徑的最小成本。

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

> **什麼時候該想到 bitmask DP**：`n` 很小（≤ 約 20，讓 `2^n` 還算得動），而且狀態是
> 「我用過／走訪過哪個子集」。相關 LC：847（Shortest Path Visiting All Nodes）、
> 1349（Maximum Students Taking Exam）、691（Stickers to Spell Word）、526（Beautiful Arrangement）。

### 3-3) 「一次填滿一個桶」的 bitmask DP — LC 698 ⭐⭐⭐⭐⭐

**模式**：切成 `k` 個相等群組的題目，看起來需要 `k` 層巢狀搜尋。
訣竅是**不要再去追蹤你正在填哪一個桶**，只追蹤：

```text
state : dp[mask] = how full the CURRENT bucket is, given `mask` items are already placed
        (-1 = mask unreachable)
key   : sum(mask) is fixed by the mask, so the bucket index is implied —
        every time the running bucket hits `target` it wraps to 0 and a new bucket starts
trans : dp[mask | (1<<i)] = (dp[mask] + nums[i]) % target,  allowed iff dp[mask] + nums[i] <= target
answer: dp[FULL] == 0   (all items used AND the last bucket closed exactly)
time  : O(2^n · n)      space : O(2^n)
```

**核心想法**：`% target` 正是讓「開始下一個桶」這個轉移完全免費的關鍵 — 不需要為桶的計數器多開一個狀態維度。

**真正有用的剪枝**：把 `nums` 由小到大排序，然後一旦 `dp[mask] + nums[i] > target` 就
`break`（不是 `continue`）— 後面的每個元素都更大，一樣會失敗。

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

**視覺追蹤** — `nums = [1,2,2,3]`（已排序），`k = 2`，`target = 4`。
第 `i` 位 = `nums[i]` 已被使用；這裡只畫出成功的那條路徑（迴圈其實也會填其他遮罩）：

```text
mask 0000  dp=0    place nums[0]=1 -> dp[0001] = 1
mask 0001  dp=1    place nums[3]=3 -> dp[1001] = (1+3) % 4 = 0   (bucket closed!)
mask 0011  dp=3    place nums[2]=2 -> 3+2 = 5 > 4 -> break        (dead branch)
mask 1001  dp=0    place nums[1]=2 -> dp[1011] = 2
mask 1011  dp=2    place nums[2]=2 -> dp[1111] = (2+2) % 4 = 0
                                     dp[FULL] == 0 -> TRUE  ([1,3] and [2,2])
```

#### **變形 — 同一個模板，`k` 寫死（LC 473）**

*變化點*：LC 473（Matchsticks to Square）**就是** `k = 4` 的 LC 698，其他完全一樣。

#### **變形 — 遮罩當成遊戲狀態，而不是 DP 表（LC 464）**

*變化點*：在 LC 464（Can I Win）裡，遮罩代表「`1..maxChoosable` 中哪些數字已被拿走」，
遞迴是 minimax 而不是求成本：`win(mask)` 為 `true` 的條件是**存在**某個沒用過的 `i`，
它要嘛立刻達到總和，要嘛讓對手落入必敗狀態 `!win(mask | (1 << (i-1)))`。
只用 `mask` 做記憶化 — 剩下的總和已經被它隱含決定了。
先用 `maxChoosable * (maxChoosable + 1) / 2 < desiredTotal` 剪枝 → 誰都贏不了。

> **更多 bitmask DP 練習**：LC 1125（Smallest Sufficient Team — 集合覆蓋，`dp[skillMask]`）、
> LC 980（Unique Paths III — 已走訪格子的遮罩）、LC 864（Shortest Path to Get All Keys —
> BFS 狀態 = `(cell, keyMask)`）、LC 1494（Parallel Courses II — 用 `sub = (sub - 1) & mask`
> 列舉目前可修課程集合的**子遮罩**）。

> **不是 bitmask DP，但和位元相鄰**：LC 421（Maximum XOR of Two Numbers in an Array）和
> LC 1707（Maximum XOR With an Element From Array）是用**二元／XOR 字典樹（Trie）**解的 —
> 請看 `trie.md`，這裡不重複。

## 範例詳解

十四道題目放在 **[bit_manipulation_examples.md](./bit_manipulation_examples.md)**，
依照它們倚賴位元運算子的哪個性質分組：

| 分組 | 用到的性質 | 題目 |
|---|---|---|
| [XOR — 抵消成對元素](./bit_manipulation_examples.md#xor--cancelling-pairs) | `x ^ x == 0`，所以任何成對的東西都會消失 | LC 136, 137, 260, 268 |
| [計數與轉換位元](./bit_manipulation_examples.md#counting--transforming-bits) | `x & (x-1)` 清掉最低位的 1 | LC 191, 338, 190, 231 |
| [不用算術做算術](./bit_manipulation_examples.md#arithmetic-without-arithmetic) | XOR 是不帶進位的加法；AND 找出進位 | LC 371, 67, 29 |
| [列舉與建構](./bit_manipulation_examples.md#enumerating-and-constructing-with-bits) | 一個整數*就是*一個子集，往上數就能走遍所有子集 | LC 78, 89, 201 |
