# 字串比對（KMP、Rolling Hash）

> **範圍** — 只談子字串搜尋 — KMP 的失敗函數、Rabin-Karp rolling hash，以及「內建 `indexOf` vs KMP vs hash」該怎麼選。
> **另見**：[string.md](./string.md) — 一般的字串處理；[advanced_string_algorithms.md](./advanced_string_algorithms.md) — Z-algorithm 與後綴結構；[hashing.md](./hashing.md) — 雜湊的設計與碰撞。

## LeetCode 題目清單

- [String Matching](https://leetcode.com/problem-list/string-matching/)
- [Rolling Hash](https://leetcode.com/problem-list/rolling-hash/)
- [Hash Function](https://leetcode.com/problem-list/hash-function/)

## 0) 概念

### 0-1) 分類

**字串比對**指的是有效率地在文字中找出某個 pattern 出現的位置。最重要的兩個演算法是：

1. **KMP（Knuth-Morris-Pratt）**：用失敗函數避開重複的比較
2. **Rolling Hash（Rabin-Karp）**：用多項式雜湊，在 O(1) 時間內比較子字串

### 0-2) 模式

兩個演算法都把樸素比對的 O(nm) 壓到 O(n+m) 或 O(n)，其中：
- n = 文字長度
- m = pattern 長度

**關鍵特性：**
- **KMP**：確定性，沒有雜湊碰撞，O(n+m) 時間、O(m) 空間
- **Rolling Hash**：機率性，可能碰撞，平均 O(n) 時間、O(1) 空間
- **什麼時候用 KMP**：單一 pattern 搜尋、需要保證正確
- **什麼時候用 Rolling Hash**：多個 pattern、子字串比較、找重複

## 1) 通用形式

### 1-1) 基本操作

#### KMP 演算法的組成

1. **失敗函數（LPS 陣列）**
   - LPS = 最長的「既是真前綴又是後綴」的長度
   - 幫你跳過不必要的比較
   - Time: O(m)，Space: O(m)

2. **Pattern 比對**
   - 用 LPS 陣列避免在文字上回退
   - Time: O(n)，Space: O(1)

#### Rolling Hash 的組成

1. **雜湊值計算**
   - 多項式雜湊：hash = (c₁ × base^(m-1) + c₂ × base^(m-2) + ... + cₘ) % mod
   - Base：通常取 256 或某個質數
   - Mod：取大質數以降低碰撞

2. **滾動視窗**
   - 移掉最左邊的字元：hash = (hash - c₀ × base^(m-1)) % mod
   - 加入最右邊的字元：hash = (hash × base + cₘ) % mod

### 1-2) 模板

#### KMP 模板

```python
def kmp_search(text, pattern):
    """Find all occurrences of pattern in text using KMP"""

    def build_lps(pattern):
        """Build Longest Prefix Suffix array"""
        m = len(pattern)
        lps = [0] * m
        length = 0  # length of previous longest prefix suffix
        i = 1

        while i < m:
            if pattern[i] == pattern[length]:
                length += 1
                lps[i] = length
                i += 1
            else:
                if length != 0:
                    # Don't increment i, try with shorter prefix
                    length = lps[length - 1]
                else:
                    lps[i] = 0
                    i += 1
        return lps

    if not pattern:
        return [0]

    n, m = len(text), len(pattern)
    lps = build_lps(pattern)
    matches = []

    i = j = 0  # i for text, j for pattern
    while i < n:
        if text[i] == pattern[j]:
            i += 1
            j += 1

        if j == m:
            # Found pattern at index i-j
            matches.append(i - j)
            j = lps[j - 1]
        elif i < n and text[i] != pattern[j]:
            if j != 0:
                j = lps[j - 1]
            else:
                i += 1

    return matches
```

```java
// Java KMP Implementation
public class KMP {
    public List<Integer> search(String text, String pattern) {
        List<Integer> matches = new ArrayList<>();
        if (pattern.isEmpty()) return matches;

        int[] lps = buildLPS(pattern);
        int n = text.length(), m = pattern.length();
        int i = 0, j = 0;

        while (i < n) {
            if (text.charAt(i) == pattern.charAt(j)) {
                i++;
                j++;
            }

            if (j == m) {
                matches.add(i - j);
                j = lps[j - 1];
            } else if (i < n && text.charAt(i) != pattern.charAt(j)) {
                if (j != 0) {
                    j = lps[j - 1];
                } else {
                    i++;
                }
            }
        }
        return matches;
    }

    private int[] buildLPS(String pattern) {
        int m = pattern.length();
        int[] lps = new int[m];
        int length = 0;
        int i = 1;

        while (i < m) {
            if (pattern.charAt(i) == pattern.charAt(length)) {
                length++;
                lps[i] = length;
                i++;
            } else {
                if (length != 0) {
                    length = lps[length - 1];
                } else {
                    lps[i] = 0;
                    i++;
                }
            }
        }
        return lps;
    }
}
```

#### Rolling Hash 模板

```python
class RollingHash:
    """Rolling hash for efficient substring matching"""

    def __init__(self, s, base=256, mod=10**9 + 7):
        self.s = s
        self.n = len(s)
        self.base = base
        self.mod = mod

        # Precompute hash values for all prefixes
        self.hash_values = [0] * (self.n + 1)
        self.base_powers = [1] * (self.n + 1)

        for i in range(self.n):
            self.hash_values[i + 1] = (self.hash_values[i] * base + ord(s[i])) % mod
            self.base_powers[i + 1] = (self.base_powers[i] * base) % mod

    def get_hash(self, left, right):
        """Get hash of substring s[left:right+1] in O(1)"""
        length = right - left + 1
        result = (self.hash_values[right + 1] -
                 self.hash_values[left] * self.base_powers[length]) % self.mod
        return result if result >= 0 else result + self.mod

    def compare_substrings(self, l1, r1, l2, r2):
        """Compare two substrings in O(1) using hash"""
        return (r1 - l1 == r2 - l2 and
                self.get_hash(l1, r1) == self.get_hash(l2, r2))

def rabin_karp_search(text, pattern):
    """Find all occurrences of pattern using Rabin-Karp"""
    if not pattern or len(pattern) > len(text):
        return []

    base = 256
    mod = 10**9 + 7
    n, m = len(text), len(pattern)

    # Calculate base^(m-1) % mod
    base_power = pow(base, m - 1, mod)

    # Calculate initial hashes
    pattern_hash = 0
    text_hash = 0
    for i in range(m):
        pattern_hash = (pattern_hash * base + ord(pattern[i])) % mod
        text_hash = (text_hash * base + ord(text[i])) % mod

    matches = []

    # Slide pattern over text
    for i in range(n - m + 1):
        # If hash matches, verify character by character
        if pattern_hash == text_hash:
            if text[i:i+m] == pattern:
                matches.append(i)

        # Calculate hash for next window
        if i < n - m:
            text_hash = (text_hash - ord(text[i]) * base_power) % mod
            text_hash = (text_hash * base + ord(text[i + m])) % mod
            if text_hash < 0:
                text_hash += mod

    return matches
```

```java
// Java Rolling Hash Implementation
public class RollingHash {
    private static final long BASE = 256;
    private static final long MOD = 1_000_000_007;

    public List<Integer> search(String text, String pattern) {
        List<Integer> matches = new ArrayList<>();
        int n = text.length(), m = pattern.length();

        if (m > n) return matches;

        long basePower = 1;
        for (int i = 0; i < m - 1; i++) {
            basePower = (basePower * BASE) % MOD;
        }

        long patternHash = 0, textHash = 0;

        // Calculate initial hashes
        for (int i = 0; i < m; i++) {
            patternHash = (patternHash * BASE + pattern.charAt(i)) % MOD;
            textHash = (textHash * BASE + text.charAt(i)) % MOD;
        }

        // Slide pattern over text
        for (int i = 0; i <= n - m; i++) {
            if (patternHash == textHash) {
                // Verify character by character
                if (text.substring(i, i + m).equals(pattern)) {
                    matches.add(i);
                }
            }

            if (i < n - m) {
                textHash = (textHash - text.charAt(i) * basePower % MOD + MOD) % MOD;
                textHash = (textHash * BASE + text.charAt(i + m)) % MOD;
            }
        }

        return matches;
    }
}
```

### 1-3) 雙重雜湊（降低碰撞）

```python
class DoubleHash:
    """Double hashing to minimize collision probability"""

    def __init__(self, s):
        self.s = s
        self.n = len(s)

        # Two different bases and moduli
        self.base1, self.mod1 = 257, 10**9 + 7
        self.base2, self.mod2 = 263, 10**9 + 9

        # Precompute for both hash functions
        self.hash1 = [0] * (self.n + 1)
        self.hash2 = [0] * (self.n + 1)
        self.pow1 = [1] * (self.n + 1)
        self.pow2 = [1] * (self.n + 1)

        for i in range(self.n):
            self.hash1[i + 1] = (self.hash1[i] * self.base1 + ord(s[i])) % self.mod1
            self.hash2[i + 1] = (self.hash2[i] * self.base2 + ord(s[i])) % self.mod2
            self.pow1[i + 1] = (self.pow1[i] * self.base1) % self.mod1
            self.pow2[i + 1] = (self.pow2[i] * self.base2) % self.mod2

    def get_hash(self, left, right):
        """Get double hash tuple for substring"""
        length = right - left + 1
        h1 = (self.hash1[right + 1] - self.hash1[left] * self.pow1[length]) % self.mod1
        h2 = (self.hash2[right + 1] - self.hash2[left] * self.pow2[length]) % self.mod2
        return (h1 if h1 >= 0 else h1 + self.mod1,
                h2 if h2 >= 0 else h2 + self.mod2)
```

### 1-4) 重複文字直到包含 pattern — LC 686 ⭐⭐⭐⭐

**核心想法**：「`a` 要重複幾份，`b` 才會是它的子字串？」看起來沒有上界，其實**只有兩個候選**。`b` 在 `aaaa...` 裡的任何一次出現，都可以平移到從*第一份* `a` 內部開始；從那裡算起最多橫跨 `ceil(|b| / |a|)` 份，再加一份給跨越邊界的尾巴。所以只要測 `k = ceil(|b|/|a|)` 和 `k + 1` — 兩個都不含 `b` 的話，答案就是 `-1`。

**什麼時候用**：任何「把某個區塊重複／串接到包含 X 為止」的問題。整場面試的重點就是那個「候選只有兩個」的論證；至於包含與否的檢查，用 KMP 就好（沿用 `1-2`）。

```java
// java
// LC 686 - Repeated String Match
// IDEA: only k = ceil(|b|/|a|) and k+1 can work -> build those two texts and KMP-search b in each
// time = O(n + m), space = O(n + m)   // n = |a|, m = |b|
public int repeatedStringMatch(String a, String b) {
    int count = (b.length() + a.length() - 1) / a.length();   // ceil(|b| / |a|)
    StringBuilder sb = new StringBuilder();
    for (int i = 0; i < count; i++) sb.append(a);
    if (kmpContains(sb.toString(), b)) return count;
    sb.append(a);                                             // one extra copy for the overhang
    if (kmpContains(sb.toString(), b)) return count + 1;
    return -1;
}

// KMP containment test (buildLPS from template 1-2)
private boolean kmpContains(String text, String pat) {
    if (pat.isEmpty()) return true;
    int[] lps = buildLPS(pat);
    int j = 0;
    for (int i = 0; i < text.length(); i++) {
        while (j > 0 && text.charAt(i) != pat.charAt(j)) j = lps[j - 1];
        if (text.charAt(i) == pat.charAt(j)) {
            j++;
            if (j == pat.length()) return true;
        }
    }
    return false;
}
```

```python
# python
# LC 686 - Repeated String Match
# IDEA: only k = ceil(|b|/|a|) and k+1 can work -> build those two texts and KMP-search b in each
# time = O(n + m), space = O(n + m)   # n = |a|, m = |b|
def repeatedStringMatch(a, b):
    count = -(-len(b) // len(a))            # ceil(|b| / |a|)
    for k in (count, count + 1):            # k+1 covers the overhang past the boundary
        if kmp_contains(a * k, b):          # `b in a * k` is the same test via built-in
            return k
    return -1

def kmp_contains(text, pat):                # build_lps from template 1-2
    if not pat:
        return True
    lps = build_lps(pat)
    j = 0
    for ch in text:
        while j > 0 and ch != pat[j]:
            j = lps[j - 1]
        if ch == pat[j]:
            j += 1
            if j == len(pat):
                return True
    return False
```

**容易踩到的坑**
- **不要**寫成 `while len(text) < some_bound` 的迴圈：把那個「只有兩個候選」的界線明講出來，那才是面試官在看的。
- 當 `|b| <= |a|` 時 `count` 可以是 1（例如 `a="aa", b="a"` → 1）。`ceil` 已經處理掉了，不用另外開特例。

---

### 1-5) 固定長度視窗的重複偵測（整數 rolling hash） — LC 187 ⭐⭐⭐⭐

**核心想法**：字母表很小、視窗長度又固定時，視窗**本身就是**一個整數 — 根本不用取模。DNA 只有 4 種字母、每個 2 個位元，10 個字元的視窗剛好 20 位元：用 `h = ((h << 2) | code[c]) & mask` 滾動即可。這是*完全不會碰撞*的 rolling hash，所以不需要再驗證一次。只有在視窗塞不進一個機器字時，才退回去用模 p 的多項式雜湊（模板 `1-2`）。

**什麼時候用**：「找出／數出所有長度為 k 的重複子字串」、滑動視窗去重，以及任何在小字母表上做固定寬度視窗的題目。

```java
// java
// LC 187 - Repeated DNA Sequences
// IDEA: 2 bits per base -> a 10-char window is a 20-bit int; roll it and record windows seen twice
// time = O(N), space = O(N)
public List<String> findRepeatedDnaSequences(String s) {
    List<String> res = new ArrayList<>();
    if (s.length() <= 10) return res;
    int[] code = new int[128];
    code['A'] = 0; code['C'] = 1; code['G'] = 2; code['T'] = 3;
    int mask = (1 << 20) - 1, h = 0;
    Set<Integer> seen = new HashSet<>(), reported = new HashSet<>();
    for (int i = 0; i < s.length(); i++) {
        h = ((h << 2) | code[s.charAt(i)]) & mask;      // push 2 bits in, drop the top 2 bits
        if (i >= 9) {                                   // first full window ends at index 9
            // seen.add returns false if already present; reported.add keeps the output distinct
            if (!seen.add(h) && reported.add(h)) res.add(s.substring(i - 9, i + 1));
        }
    }
    return res;
}
```

```python
# python
# LC 187 - Repeated DNA Sequences
# IDEA: 2 bits per base -> a 10-char window is a 20-bit int; roll it and record windows seen twice
# time = O(N), space = O(N)
def findRepeatedDnaSequences(s):
    if len(s) <= 10:
        return []
    code = {'A': 0, 'C': 1, 'G': 2, 'T': 3}
    mask = (1 << 20) - 1                       # keep only the last 10 bases
    h = 0
    seen, res = set(), set()
    for i, ch in enumerate(s):
        h = ((h << 2) | code[ch]) & mask
        if i >= 9:                             # first full window ends at index 9
            if h in seen:
                res.add(s[i - 9:i + 1])
            else:
                seen.add(h)
    return list(res)
```

**變形 — LC 1461（Check If a String Contains All Binary Codes of Size K）**：一樣的滾動位元遮罩，只是每個字元 1 個位元而不是 2 個，問題也變成*數出相異視窗的個數*：看到全部 `2^k` 個時答案才是 `True`。

```python
# python
# LC 1461 - Check If a String Contains All Binary Codes of Size K
# IDEA: roll a k-bit window mask; every code of length k must appear -> distinct window count == 2^k
# time = O(N), space = O(2^k)
def hasAllCodes(s, k):
    if len(s) < k:
        return False
    need = 1 << k
    mask = need - 1
    h, seen = 0, set()
    for i, ch in enumerate(s):
        h = ((h << 1) | (1 if ch == '1' else 0)) & mask
        if i >= k - 1:
            seen.add(h)
    return len(seen) == need
```

```java
// java
// LC 1461 - Check If a String Contains All Binary Codes of Size K
// IDEA: roll a k-bit window mask; every code of length k must appear -> distinct window count == 2^k
// time = O(N), space = O(2^k)
public boolean hasAllCodes(String s, int k) {
    if (s.length() < k) return false;                 // also prunes when s is shorter than 2^k + k - 1
    int need = 1 << k, mask = need - 1, h = 0;
    Set<Integer> seen = new HashSet<>();
    for (int i = 0; i < s.length(); i++) {
        h = ((h << 1) | (s.charAt(i) - '0')) & mask;
        if (i >= k - 1) seen.add(h);
    }
    return seen.size() == need;
}
```

---

### 1-6) 對長度做二分搜尋 + 在**陣列**上做 rolling hash — LC 718 ⭐⭐⭐⭐

**核心想法**：*「存在長度為 L 的共同區塊」*這個判定是**單調的** — 長度 `L` 的存在，長度 `L-1` 的一定也存在（取前綴即可）。這就讓我們可以對 `L` 做二分搜尋，而每次 `check(L)` 只要一趟 O(n+m) 的雜湊：把第一個序列所有長度為 `L` 的視窗雜湊進一個集合，再串流掃過第二個序列的視窗，看有沒有落在集合裡。這裡沒有任何跟字串綁死的東西 — 直接對原始整數做雜湊，所以這等於把模板 `2-3`（LC 1044）從字元推廣到陣列。

**什麼時候用**：「最長的共同／重複區塊長度為 L」這類問題，直接 DP 是 O(n·m)，而你需要 O((n+m)·log)。輸入是整數陣列而不是字串時，這也該是你的反射動作。

```java
// java
// LC 718 - Maximum Length of Repeated Subarray
// IDEA: binary search the length; check(L) = do a length-L window hash of A and of B intersect?
// time = O((n + m) * log(min(n, m))), space = O(n + m)
static final long MOD = 1_000_000_007L;
long BASE;

public int findLength(int[] nums1, int[] nums2) {
    BASE = 131 + new Random().nextInt(1000);          // randomize -> anti hash-attack
    int lo = 0, hi = Math.min(nums1.length, nums2.length);
    while (lo < hi) {
        int mid = lo + (hi - lo + 1) / 2;             // upper-mid: lo is the last known-good length
        if (exists(nums1, nums2, mid)) lo = mid;
        else hi = mid - 1;
    }
    return lo;
}

private boolean exists(int[] a, int[] b, int L) {
    if (L == 0) return true;
    if (L > a.length || L > b.length) return false;
    Set<Long> seen = windowHashes(a, L);
    for (long h : windowHashes(b, L)) if (seen.contains(h)) return true;
    return false;
}

private Set<Long> windowHashes(int[] arr, int L) {
    long p = 1;
    for (int i = 0; i < L; i++) p = p * BASE % MOD;   // BASE^L
    long h = 0;
    for (int i = 0; i < L; i++) h = (h * BASE + arr[i]) % MOD;
    Set<Long> out = new HashSet<>();
    out.add(h);
    for (int i = L; i < arr.length; i++) {           // roll: push arr[i], pop arr[i-L]
        h = ((h * BASE + arr[i] - p * arr[i - L]) % MOD + MOD) % MOD;
        out.add(h);
    }
    return out;
}
```

```python
# python
# LC 718 - Maximum Length of Repeated Subarray
# IDEA: binary search the length; check(L) = do a length-L window hash of A and of B intersect?
# time = O((n + m) * log(min(n, m))), space = O(n + m)
import random

def findLength(nums1, nums2):
    MOD = (1 << 61) - 1                       # Mersenne prime -> very low collision odds
    base = random.randrange(256, 1 << 20)     # randomize -> anti hash-attack

    def window_hashes(arr, L):
        p = pow(base, L, MOD)                 # base^L
        h = 0
        for i in range(L):
            h = (h * base + arr[i]) % MOD
        out = {h}
        for i in range(L, len(arr)):          # roll: push arr[i], pop arr[i-L]
            h = (h * base + arr[i] - p * arr[i - L]) % MOD
            out.add(h)
        return out

    def exists(L):
        if L == 0:
            return True
        if L > len(nums1) or L > len(nums2):
            return False
        seen = window_hashes(nums1, L)
        return any(h in seen for h in window_hashes(nums2, L))

    lo, hi = 0, min(len(nums1), len(nums2))
    while lo < hi:
        mid = (lo + hi + 1) // 2              # upper-mid: lo is the last known-good length
        if exists(mid):
            lo = mid
        else:
            hi = mid - 1
    return lo
```

**容易踩到的坑**
- 要用**上取中點** `(lo + hi + 1) // 2` 搭配 `lo = mid` / `hi = mid - 1`，否則 `hi == lo + 1` 時迴圈會卡死。
- 單一個 32 位元左右的模數，在約 10⁵ 個視窗上必然會碰撞（生日界線）。要用模 `2^61 - 1`、雙重雜湊（`1-3`），或是對候選答案直接比對驗證。
- 經典 DP `dp[i][j] = dp[i+1][j+1] + 1` 是 O(n·m) 時間／O(m) 空間，而且好寫得多 — 先講它，等面試官要求打敗 O(n·m) 時再拿這招出來。

**變形 — LC 1923（Longest Common Subpath）**：同樣是「對長度二分搜尋」的骨架，但 `check(L)` 必須對**所有**路徑都成立：先把第一條路徑長度為 `L` 的視窗雜湊進集合，再逐一跟下一條路徑的視窗雜湊取交集；交集最後還非空，答案就是 `L`。

---

### 1-7) 序列化 + KMP 做結構（樹）比對 — LC 572 ⭐⭐⭐

**核心想法**：只要序列化是*沒有歧義*的，「B 是不是 A 的子樹？」就變成「`serialize(B)` 是不是 `serialize(A)` 的**子字串**？」。兩個標記就夠了：每個 null 子節點放 `#`（把形狀編碼進去），每個數值前面放 `^`（這樣 `^2` 就不可能配到 `^12` 裡面）。接著一趟 KMP 就是 O(n+m)，而不是樸素的「每個節點都比一次」的 O(n·m)。

**什麼時候用**：子樹／子結構的包含關係、子樹去重，以及任何序列化之後就變成線性 pattern 問題的樹題。

```java
// java
// LC 572 - Subtree of Another Tree
// IDEA: preorder-serialize with '#' for null and '^' before each value, then subtree <=> substring (KMP)
// time = O(n + m), space = O(n + m)
public boolean isSubtree(TreeNode root, TreeNode subRoot) {
    StringBuilder s = new StringBuilder(), t = new StringBuilder();
    serialize(root, s);
    serialize(subRoot, t);
    return kmpContains(s.toString(), t.toString());   // helper from template 1-4
}

private void serialize(TreeNode node, StringBuilder sb) {
    if (node == null) { sb.append('#'); return; }     // '#' encodes shape -> no ambiguity
    sb.append('^').append(node.val);                  // '^' blocks "^2" matching inside "^12"
    serialize(node.left, sb);
    serialize(node.right, sb);
}
```

```python
# python
# LC 572 - Subtree of Another Tree
# IDEA: preorder-serialize with '#' for null and '^' before each value, then subtree <=> substring (KMP)
# time = O(n + m), space = O(n + m)
def isSubtree(root, subRoot):
    def serialize(node, out):
        if not node:
            out.append("#")                  # '#' encodes shape -> no ambiguity
            return
        out.append("^" + str(node.val))      # '^' blocks "^2" matching inside "^12"
        serialize(node.left, out)
        serialize(node.right, out)

    s, t = [], []
    serialize(root, s)
    serialize(subRoot, t)
    return kmp_contains("".join(s), "".join(t))   # helper from template 1-4
```

**容易踩到的坑**
- 少了 null 標記就壞掉：`[1,2]` 和 `[1,null,2]` 會序列化成同一個字串。
- 少了數值前綴也會壞掉：pattern `^2##` 會配進 `^12##` 裡面，而負值（`-2`）也會跟減號撞在一起。
- 對序列化後的字串做 rolling hash 也可以，但那樣命中之後還得驗證一次 — KMP 是精確的，在這裡又一樣快。

## 2) LC 範例

### 2-1) Find the Index of the First Occurrence in a String (LC 28)

```python
# KMP Solution
def strStr(haystack, needle):
    """LC 28: Implement strStr() using KMP"""
    if not needle:
        return 0

    def build_lps(pattern):
        m = len(pattern)
        lps = [0] * m
        length = 0
        i = 1

        while i < m:
            if pattern[i] == pattern[length]:
                length += 1
                lps[i] = length
                i += 1
            else:
                if length != 0:
                    length = lps[length - 1]
                else:
                    lps[i] = 0
                    i += 1
        return lps

    n, m = len(haystack), len(needle)
    lps = build_lps(needle)

    i = j = 0
    while i < n:
        if haystack[i] == needle[j]:
            i += 1
            j += 1

        if j == m:
            return i - j
        elif i < n and haystack[i] != needle[j]:
            if j != 0:
                j = lps[j - 1]
            else:
                i += 1

    return -1

# Rolling Hash Solution
def strStr_hash(haystack, needle):
    """LC 28: Using rolling hash"""
    if not needle:
        return 0
    if len(needle) > len(haystack):
        return -1

    base = 256
    mod = 10**9 + 7
    n, m = len(haystack), len(needle)

    base_power = pow(base, m - 1, mod)

    needle_hash = 0
    hay_hash = 0

    for i in range(m):
        needle_hash = (needle_hash * base + ord(needle[i])) % mod
        hay_hash = (hay_hash * base + ord(haystack[i])) % mod

    for i in range(n - m + 1):
        if needle_hash == hay_hash:
            if haystack[i:i+m] == needle:
                return i

        if i < n - m:
            hay_hash = (hay_hash - ord(haystack[i]) * base_power) % mod
            hay_hash = (hay_hash * base + ord(haystack[i + m])) % mod
            if hay_hash < 0:
                hay_hash += mod

    return -1
```

### 2-2) Repeated Substring Pattern (LC 459)

```python
def repeatedSubstringPattern(s):
    """LC 459: Check if string is made of repeated pattern"""

    # Method 1: KMP-based
    def kmp_solution(s):
        n = len(s)
        lps = [0] * n
        length = 0
        i = 1

        while i < n:
            if s[i] == s[length]:
                length += 1
                lps[i] = length
                i += 1
            else:
                if length != 0:
                    length = lps[length - 1]
                else:
                    lps[i] = 0
                    i += 1

        # If lps[n-1] != 0 and n % (n - lps[n-1]) == 0
        # then string has repeating pattern
        return lps[n - 1] != 0 and n % (n - lps[n - 1]) == 0

    # Method 2: String rotation trick
    def rotation_solution(s):
        # If s is repeated pattern, it will appear in (s+s)[1:-1]
        return s in (s + s)[1:-1]

    return kmp_solution(s)
```

### 2-3) Longest Duplicate Substring (LC 1044)

```python
def longestDupSubstring(s):
    """LC 1044: Find longest duplicate substring using binary search + rolling hash"""

    def has_duplicate(length):
        """Check if there's duplicate substring of given length"""
        base = 256
        mod = 2**63 - 1

        # Calculate base^length
        base_power = pow(base, length, mod)

        # Initial hash
        current_hash = 0
        for i in range(length):
            current_hash = (current_hash * base + ord(s[i])) % mod

        seen = {current_hash}

        # Rolling hash
        for i in range(length, len(s)):
            # Remove leftmost character
            current_hash = (current_hash - ord(s[i - length]) * base_power) % mod
            # Add rightmost character
            current_hash = (current_hash * base + ord(s[i])) % mod

            if current_hash in seen:
                return i - length + 1
            seen.add(current_hash)

        return -1

    # Binary search on length
    left, right = 0, len(s) - 1
    result_start = -1

    while left <= right:
        mid = (left + right) // 2
        start_pos = has_duplicate(mid)

        if start_pos != -1:
            result_start = start_pos
            left = mid + 1
        else:
            right = mid - 1

    return s[result_start:result_start + right] if result_start != -1 else ""
```

### 2-4) Shortest Palindrome (LC 214)

```python
def shortestPalindrome(s):
    """LC 214: Find shortest palindrome by adding chars to front"""

    # KMP approach: Find longest palindromic prefix
    def kmp_solution(s):
        # Create pattern: s + "#" + reverse(s)
        # Find longest prefix of s that matches suffix of reverse(s)
        rev = s[::-1]
        new_s = s + "#" + rev

        # Build LPS array
        n = len(new_s)
        lps = [0] * n
        length = 0
        i = 1

        while i < n:
            if new_s[i] == new_s[length]:
                length += 1
                lps[i] = length
                i += 1
            else:
                if length != 0:
                    length = lps[length - 1]
                else:
                    lps[i] = 0
                    i += 1

        # lps[-1] gives length of longest palindromic prefix
        palindrome_len = lps[-1]

        # Add reverse of remaining suffix to front
        return rev[:len(s) - palindrome_len] + s

    return kmp_solution(s)
```

### 2-5) Distinct Echo Substrings (LC 1316)

```python
def distinctEchoSubstrings(text):
    """LC 1316: Count distinct echo substrings (repeated twice consecutively)"""

    rh = RollingHash(text)
    seen = set()
    count = 0

    # Try all possible lengths (must be even)
    for length in range(2, len(text) + 1, 2):
        half = length // 2

        for start in range(len(text) - length + 1):
            mid = start + half
            end = start + length - 1

            # Compare first half with second half
            if rh.compare_substrings(start, mid - 1, mid, end):
                substring_hash = rh.get_hash(start, end)
                if substring_hash not in seen:
                    seen.add(substring_hash)
                    count += 1

    return count
```

### 2-6) Rotate String (LC 796)

```python
def rotateString(s, goal):
    """LC 796: Check if goal is rotation of s"""

    # If goal is rotation of s, it will be in s+s
    if len(s) != len(goal):
        return False

    # Method 1: Simple check
    return goal in (s + s)

    # Method 2: KMP approach
    def kmp_approach():
        if len(s) != len(goal):
            return False

        # Search for goal in s+s
        matches = kmp_search(s + s, goal)
        return len(matches) > 0

    return kmp_approach()
```

### 2-7) strStr() 的各種實作變形

```python
# Count all occurrences
def count_occurrences(text, pattern):
    """Count all occurrences of pattern in text"""
    matches = kmp_search(text, pattern)
    return len(matches)

# Find all overlapping occurrences
def find_all_overlapping(text, pattern):
    """Find all overlapping occurrences (including overlaps)"""
    n, m = len(text), len(pattern)
    lps = build_lps(pattern)
    matches = []

    i = j = 0
    while i < n:
        if text[i] == pattern[j]:
            i += 1
            j += 1

        if j == m:
            matches.append(i - j)
            j = lps[j - 1]  # Continue to find overlapping matches
        elif i < n and text[i] != pattern[j]:
            if j != 0:
                j = lps[j - 1]
            else:
                i += 1

    return matches

# Multiple pattern matching
def multiple_pattern_search(text, patterns):
    """Search for multiple patterns in text"""
    results = {}
    for pattern in patterns:
        results[pattern] = kmp_search(text, pattern)
    return results
```

### 2-8) Longest Happy Prefix (LC 1392)

```python
def longestPrefix(s):
    """LC 1392: Find longest happy prefix (prefix == suffix)"""

    n = len(s)
    lps = [0] * n
    length = 0
    i = 1

    while i < n:
        if s[i] == s[length]:
            length += 1
            lps[i] = length
            i += 1
        else:
            if length != 0:
                length = lps[length - 1]
            else:
                lps[i] = 0
                i += 1

    # lps[-1] gives length of longest prefix which is also suffix
    return s[:lps[-1]]
```

## 3) 常見應用

### 3-1) Pattern 比對類問題
- 找第一次／所有出現位置
- 多 pattern 搜尋
- 帶萬用字元的子字串比對

### 3-2) 字串週期偵測
- 重複子字串構成的 pattern
- 字串旋轉
- 環狀字串比較

### 3-3) 回文問題
- 最短回文
- 回文前綴／後綴

### 3-4) 重複偵測
- 最長重複子字串
- 相異子字串
- 疊字（echo）子字串

## 4) 演算法比較

| 演算法 | 時間 | 空間 | 優點 | 缺點 |
|-----------|------|-------|------|------|
| **KMP** | O(n+m) | O(m) | 確定性，不會碰撞 | 實作比較複雜 |
| **Rolling Hash** | O(n) 平均 | O(1) | 簡單，可處理多 pattern | 可能有雜湊碰撞 |
| **樸素比對** | O(nm) | O(1) | 非常簡單 | 輸入一大就太慢 |

## 5) 技巧與訣竅

### 5-1) 什麼時候用 KMP
- 單一 pattern 比對
- 需要保證正確性
- Pattern 本身有重複結構
- LC 28, 214, 459, 1392

### 5-2) 什麼時候用 Rolling Hash
- 多 pattern 搜尋
- 子字串比較
- 重複偵測
- LC 1044, 1316, 718, 1062

### 5-3) 常見寫法
```python
# Pattern 1: String in (s+s) for rotation
def is_rotation(s1, s2):
    return len(s1) == len(s2) and s2 in (s1 + s1)

# Pattern 2: LPS for periodicity
def has_period(s):
    lps = build_lps(s)
    n = len(s)
    return lps[n-1] != 0 and n % (n - lps[n-1]) == 0

# Pattern 3: Binary search + hash for optimization
def find_longest(s, condition):
    left, right = 0, len(s)
    result = -1

    while left <= right:
        mid = (left + right) // 2
        if check_with_hash(s, mid, condition):
            result = mid
            left = mid + 1
        else:
            right = mid - 1

    return result
```

## 6) 總結

### 重點整理
1. **KMP** 是單一 pattern 比對的最佳解，O(n+m) 時間
2. **Rolling Hash** 擅長把子字串比較壓到攤還 O(1)
3. 用**雙重雜湊**把碰撞機率壓到最低
4. **二分搜尋 + 雜湊**在最佳化類問題上很強大
5. LPS 陣列會揭露字串的週期性與結構

### 常見 LeetCode 題目
| 題目 | 編號 | 演算法 | 難度 |
|---------|--------|-----------|------------|
| Implement strStr() | 28 | KMP/Hash | Easy |
| Shortest Palindrome | 214 | KMP | Hard |
| Repeated Substring Pattern | 459 | KMP | Easy |
| Rotate String | 796 | String | Easy |
| Longest Duplicate Substring | 1044 | Hash+BS | Hard |
| Distinct Echo Substrings | 1316 | Hash | Hard |
| Longest Happy Prefix | 1392 | KMP | Hard |
| Maximum Repeating Substring | 1668 | String | Easy |

### 面試提示
1. 一定要先問清楚允不允許重疊的比對結果
2. 考慮字串長度的限制（決定 KMP 還是樸素比對）
3. 用雜湊的話，要說明碰撞的處理策略
4. 時間複雜度：KMP 是 O(n+m)，雜湊是 O(n)
5. 空間複雜度：KMP 是 O(m)，基本雜湊是 O(1)

## 7) 決策指南：內建 `indexOf` vs KMP vs Rolling Hash

大多數「字串比對」的題目其實**不需要** KMP。照題目逼你做的事來選：

| 情境 | 用什麼 | 為什麼 |
|-----------|-----|-----|
| 單一 pattern，`n·m` 在限制內（`n, m <= ~10³`） | 內建 `indexOf` / `in` / 雙指標 | 最短的正確寫法；把最壞情況講出來就好 |
| 單一 pattern，文字很大，需要**保證**線性 | **KMP**（`1-2`） | 確定性的 O(n+m)，不會碰撞 |
| 你要的是「前綴==後綴」／週期性／「補成回文」 | **只要 KMP 的失敗陣列**（不用搜尋） | LPS 陣列*本身就是*答案（LC 459, 1392, 214） |
| 多個 pattern，或要 O(1) 比較任意兩段子字串 | **Rolling hash**（`1-2`、`1-3`） | 一次前處理就能回答任何 `(l, r)` 查詢 |
| 帶單調判定的「長度為 L 的最長 X」 | **對 L 二分搜尋 + rolling hash**（`1-6`、`2-3`） | 把 O(n²) 掃描變成 O(n log n) |
| 小字母表上的固定小視窗 | **位元打包的滾動視窗**（`1-5`） | 精確、無碰撞、不用取模 |
| 比對的是*結構*（樹／資料夾）而不是文字 | **序列化 + KMP**（`1-7`） | 包含關係變成子字串搜尋 |

**實務備註**：Java 的 `String.indexOf` 在最壞情況下是樸素的 O(n·m)；CPython 的 `in` / `str.find` 用的是 two-way 演算法，最壞情況是線性。不管哪一種，都要把你依賴的複雜度界線講出來，而不是把責任丟給函式庫。

### 7-1) 樸素比對就是正解的題目
這些題掛著 *string-matching* 的標籤，但限制小到不行 — 直接用函式庫，把時間花在迴圈結構上：

- **LC 1408 - String Matching in an Array**：回傳是其他單字之子字串的那些單字。用 `contains` 跑 `O(n²·L)` 的雙層迴圈就是出題者的意圖。
- **LC 1455 - Check If a Word Occurs As a Prefix of Any Word in a Sentence**：`split(" ")` + `startsWith`；答案從 1 開始編號，沒有的話回 `-1`。
- **LC 1023 - Camelcase Matching**：這是**子序列**，不是子字串 — 貪婪雙指標，而且每個沒配到的 query 字元都必須是小寫。很好的提醒：「matching」≠「substring」。

### 7-2) 進階應用（知道想法就好，被問到再寫）
- **LC 1147 - Longest Chunked Palindrome Decomposition**：從兩端往中間的貪婪雙指標；前綴區塊 == 後綴區塊時就馬上切（用 rolling hash 比較可以做到 O(n)，實務上直接比對也夠用）。貪婪是最佳的 — 取最短的可配對區塊絕不會吃虧。
- **LC 1923 - Longest Common Subpath**：對長度二分搜尋 + rolling hash，把*所有*路徑的視窗雜湊集合取交集（見 `1-6` 底下的變形）。
- **LC 1948 - Delete Duplicate Folders in System**：把 `1-7` 的想法往上提一層 — 對資料夾 trie 的每棵子樹做標準序列化，數出相同序列化的次數，然後刪掉所有序列化出現超過一次的子樹。
- **LC 1397 - Find All Good Strings**：在 `evil` 的 KMP **自動機**上跑數位 DP — DP 狀態是（位置、下界是否貼齊、上界是否貼齊、LPS 已配對長度），失敗陣列則給出每個候選字元下的下一個配對長度。這是少數真的要建出完整自動機、而不只是 LPS 陣列的情況。

### 7-3) 交叉參照（在兄弟表單裡有完整說明）
- **LC 214 - Shortest Palindrome** → `palindrome.md` 的模板 8 有完整的 KMP 前綴函數推導（`s + "#" + reverse(s)`）；上面的 `2-4` 是濃縮版。
- **LC 336 - Palindrome Pairs** → `palindrome.md` 的模板 9（用 HashMap 對前綴／後綴切分）以及 `trie.md`。它掛著 *hash-function* 標籤，但本質是雜湊表／字典樹（Trie）的查表問題，不是 rolling hash。
