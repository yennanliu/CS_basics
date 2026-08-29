# 進階字串演算法

> **範圍** — 比較重的字串工具：後綴結構、Z-algorithm、Manacher，以及字串 DP — 這些對主字串文件來說太專門的部分。
> **另見**：[string.md](./string.md) — 日常字串的題目目錄與模板；[string_matching_kmp_rolling_hash.md](./string_matching_kmp_rolling_hash.md) — 專講子字串搜尋；[palindrome.md](./palindrome.md) — 回文題家族；[trie.md](./trie.md) — 前綴結構。

## LeetCode 題目清單

- [String](https://leetcode.com/problem-list/string/)
- [String Matching](https://leetcode.com/problem-list/string-matching/)
- [Rolling Hash](https://leetcode.com/problem-list/rolling-hash/)
- [Suffix Array](https://leetcode.com/problem-list/suffix-array/)

## 總覽
**進階字串演算法**指的是基本操作之外、比較講究的字串處理技巧。這些演算法在模式比對、回文偵測和複雜字串操作上，能給出有理論保證的最佳解。

### 關鍵性質
- **時間複雜度**：最佳演算法通常是 O(n) 或 O(n + m)
- **空間複雜度**：預處理結構需要 O(n)
- **核心想法**：先把字串預處理過，之後查詢和比對就能很快
- **什麼時候用**：字串模式複雜、要查很多次、需要最佳化
- **主要演算法**：KMP、Manacher、Z-Algorithm、Rolling Hash、後綴陣列

### 共同特徵
- **預處理**：先建輔助結構，換取後續操作的速度
- **辨識模式**：找出重複出現的結構與週期
- **線性時間**：靠一些巧妙的技巧壓到最佳複雜度
- **多次查詢**：同一個字串反覆操作時特別划算
- **理論基礎**：建立在字串理論與自動機之上

## 題型分類

### **分類 1：模式比對**
- **描述**：在文字中高效率地找出模式出現的位置
- **例題**：LC 28（Find Index of First Occurrence）、LC 459（Repeated Substring Pattern）
- **模式**：用 KMP、Z-Algorithm、Rolling Hash 做到 O(n + m)

### **分類 2：回文問題**
- **描述**：找出所有回文，或最長的回文子字串
- **例題**：LC 5（Longest Palindromic Substring）、LC 647（Palindromic Substrings）
- **模式**：用 Manacher 演算法做到 O(n) 的回文偵測

### **分類 3：字串週期性**
- **描述**：偵測重複模式與字串週期
- **例題**：LC 459（Repeated Substring Pattern）、LC 1316（Distinct Echo Substrings）
- **模式**：用 failure function 或 Z-array 做週期偵測

### **分類 4：後綴類問題**
- **描述**：牽涉到字串後綴與字典序的問題
- **例題**：LC 1044（Longest Duplicate Substring）、LC 1316（Distinct Echo Substrings）
- **模式**：後綴陣列、最長共同前綴、rolling hash

## 模板與演算法

### 模板比較表
| 演算法 | 用途 | 時間複雜度 | 空間複雜度 | 什麼時候用 |
|-----------|----------|-----------------|------------------|-------------|
| **KMP** | 模式比對 | O(n + m) | O(m) | 單一模式搜尋 |
| **Manacher** | 所有回文 | O(n) | O(n) | 回文類問題 |
| **Z-Algorithm** | 字串比對 | O(n) | O(n) | 模式比對的各種變形 |
| **Rolling Hash** | 子字串比較 | O(n) | O(1) | 多模式搜尋 |
| **DFA／狀態機** | 格式驗證／斷詞 | O(n) | O(1) | 規則亂成一團的 `if/else` 解析（LC 65） |

### 模板 1：KMP（Knuth-Morris-Pratt）演算法 — LC 28
```python
def kmp_search(text, pattern):
    """KMP algorithm for pattern matching"""

    def compute_failure_function(pattern):
        """Compute failure function (partial match table)"""
        m = len(pattern)
        failure = [0] * m
        j = 0

        for i in range(1, m):
            while j > 0 and pattern[i] != pattern[j]:
                j = failure[j - 1]

            if pattern[i] == pattern[j]:
                j += 1

            failure[i] = j

        return failure

    if not pattern:
        return 0

    n, m = len(text), len(pattern)
    failure = compute_failure_function(pattern)
    matches = []

    j = 0  # Index for pattern
    for i in range(n):  # Index for text
        while j > 0 and text[i] != pattern[j]:
            j = failure[j - 1]

        if text[i] == pattern[j]:
            j += 1

        if j == m:
            matches.append(i - m + 1)
            j = failure[j - 1]

    return matches

def kmp_pattern_matching_template():
    """Template for various KMP applications"""

    def find_first_occurrence(text, pattern):
        """Find first occurrence of pattern in text"""
        matches = kmp_search(text, pattern)
        return matches[0] if matches else -1

    def count_occurrences(text, pattern):
        """Count all occurrences of pattern"""
        return len(kmp_search(text, pattern))

    def is_substring_pattern(text, pattern):
        """Check if pattern exists in text"""
        return len(kmp_search(text, pattern)) > 0

    def repeated_string_pattern(s):
        """Check if string is made of repeated pattern (LC 459)"""
        n = len(s)
        failure = compute_failure_function(s + s)

        # Check if s is a rotation of itself in s+s
        return failure[-1] != 0 and n % (n - failure[-1]) == 0

    return {
        'find_first': find_first_occurrence,
        'count': count_occurrences,
        'exists': is_substring_pattern,
        'repeated': repeated_string_pattern
    }
```

### 模板 2：Manacher 演算法 — LC 5
```python
def manacher_algorithm(s):
    """Manacher's algorithm to find all palindromes in O(n)"""

    def preprocess(s):
        """Preprocess string to handle even-length palindromes"""
        # Transform "abba" -> "^#a#b#b#a#$"
        result = "^"
        for c in s:
            result += "#" + c
        result += "#$"
        return result

    processed = preprocess(s)
    n = len(processed)
    p = [0] * n  # p[i] = radius of palindrome centered at i
    center = right = 0

    for i in range(1, n - 1):
        # Mirror of i with respect to center
        mirror = 2 * center - i

        # If i is within the right boundary, use previously computed values
        if i < right:
            p[i] = min(right - i, p[mirror])

        # Try to expand palindrome centered at i
        while processed[i + p[i] + 1] == processed[i - p[i] - 1]:
            p[i] += 1

        # If palindrome centered at i extends past right, adjust center and right
        if i + p[i] > right:
            center, right = i, i + p[i]

    return p, processed

def manacher_applications():
    """Various applications of Manacher's algorithm"""

    def longest_palindromic_substring(s):
        """Find longest palindromic substring (LC 5)"""
        if not s:
            return ""

        p, processed = manacher_algorithm(s)
        max_len = 0
        center_index = 0

        for i in range(1, len(p) - 1):
            if p[i] > max_len:
                max_len = p[i]
                center_index = i

        # Convert back to original string coordinates
        start = (center_index - max_len) // 2
        return s[start:start + max_len]

    def count_palindromic_substrings(s):
        """Count all palindromic substrings (LC 647)"""
        if not s:
            return 0

        p, processed = manacher_algorithm(s)
        count = 0

        for i in range(1, len(p) - 1):
            # Each palindrome of radius r contributes (r+1)/2 palindromes
            count += (p[i] + 1) // 2

        return count

    def is_palindrome_range(s, left, right):
        """Check if substring s[left:right+1] is palindrome"""
        p, processed = manacher_algorithm(s)

        # Convert to processed string coordinates
        center = left + right + 2
        radius = right - left

        return p[center] >= radius

    def all_palindromic_substrings(s):
        """Get all palindromic substrings with their positions"""
        p, processed = manacher_algorithm(s)
        palindromes = []

        for i in range(1, len(p) - 1):
            for r in range(p[i] + 1):
                # Convert back to original coordinates
                start = (i - r - 1) // 2
                end = (i + r - 1) // 2
                if start <= end:
                    palindromes.append((start, end, s[start:end + 1]))

        return palindromes

    return {
        'longest': longest_palindromic_substring,
        'count': count_palindromic_substrings,
        'is_palindrome': is_palindrome_range,
        'all_palindromes': all_palindromic_substrings
    }
```

### 模板 3：Z-Algorithm — LC 459
```python
def z_algorithm(s):
    """Z-algorithm: compute Z array where Z[i] = length of longest substring
    starting from s[i] which is also a prefix of s"""

    n = len(s)
    z = [0] * n
    left = right = 0

    for i in range(1, n):
        if i <= right:
            # Use previously computed values
            z[i] = min(right - i + 1, z[i - left])

        # Try to extend match
        while i + z[i] < n and s[z[i]] == s[i + z[i]]:
            z[i] += 1

        # Update window if we extended past right boundary
        if i + z[i] - 1 > right:
            left, right = i, i + z[i] - 1

    return z

def z_algorithm_applications():
    """Applications of Z-algorithm"""

    def pattern_search_z(text, pattern):
        """Pattern searching using Z-algorithm"""
        combined = pattern + "$" + text
        z = z_algorithm(combined)
        pattern_len = len(pattern)
        matches = []

        for i in range(pattern_len + 1, len(combined)):
            if z[i] == pattern_len:
                matches.append(i - pattern_len - 1)

        return matches

    def find_all_occurrences(s, pattern):
        """Find all occurrences of pattern in string"""
        return pattern_search_z(s, pattern)

    def longest_prefix_suffix(s):
        """Find longest prefix which is also suffix"""
        z = z_algorithm(s)
        n = len(s)

        for i in range(n - 1, 0, -1):
            if z[i] == n - i:
                return s[:n - i]

        return ""

    def period_detection(s):
        """Find the period of string using Z-algorithm"""
        z = z_algorithm(s)
        n = len(s)

        for period in range(1, n):
            if period + z[period] == n:
                return period

        return n  # String has no period shorter than itself

    return {
        'search': pattern_search_z,
        'find_all': find_all_occurrences,
        'prefix_suffix': longest_prefix_suffix,
        'period': period_detection
    }
```

### 模板 4：進階 Rolling Hash — LC 1044
```python
class RollingHash:
    """Advanced rolling hash for string problems"""

    def __init__(self, s, base=256, mod=10**9 + 7):
        self.s = s
        self.n = len(s)
        self.base = base
        self.mod = mod

        # Precompute hash values and powers
        self.hash_values = [0] * (self.n + 1)
        self.base_powers = [1] * (self.n + 1)

        for i in range(self.n):
            self.hash_values[i + 1] = (self.hash_values[i] * base + ord(s[i])) % mod
            self.base_powers[i + 1] = (self.base_powers[i] * base) % mod

    def get_hash(self, left, right):
        """Get hash of substring s[left:right+1]"""
        length = right - left + 1
        result = (self.hash_values[right + 1] -
                 self.hash_values[left] * self.base_powers[length]) % self.mod
        return result if result >= 0 else result + self.mod

    def compare_substrings(self, l1, r1, l2, r2):
        """Compare two substrings using hash values"""
        return (r1 - l1 == r2 - l2 and
                self.get_hash(l1, r1) == self.get_hash(l2, r2))

    def longest_duplicate_substring(self):
        """Find longest duplicate substring (LC 1044)"""
        def has_duplicate_of_length(length):
            seen_hashes = set()
            for i in range(self.n - length + 1):
                substring_hash = self.get_hash(i, i + length - 1)
                if substring_hash in seen_hashes:
                    return i
                seen_hashes.add(substring_hash)
            return -1

        # Binary search on length
        left, right = 0, self.n - 1
        result_start = -1

        while left <= right:
            mid = (left + right) // 2
            start_pos = has_duplicate_of_length(mid)

            if start_pos != -1:
                result_start = start_pos
                left = mid + 1
            else:
                right = mid - 1

        return self.s[result_start:result_start + right] if result_start != -1 else ""

    def distinct_echo_substrings(self):
        """Count distinct echo substrings (LC 1316)"""
        seen = set()
        count = 0

        for i in range(self.n):
            for j in range(i + 1, self.n, 2):  # Only even lengths
                mid = (i + j) // 2
                if self.compare_substrings(i, mid, mid + 1, j):
                    substring_hash = self.get_hash(i, j)
                    if substring_hash not in seen:
                        seen.add(substring_hash)
                        count += 1

        return count
```

### 模板 5：後綴陣列建構 — LC 1044
```python
def suffix_array_construction(s):
    """Construct suffix array using counting sort (O(n log n))"""

    def counting_sort(arr, key_func, max_val):
        """Stable counting sort"""
        count = [0] * (max_val + 1)
        for item in arr:
            count[key_func(item)] += 1

        for i in range(1, len(count)):
            count[i] += count[i - 1]

        result = [0] * len(arr)
        for i in range(len(arr) - 1, -1, -1):
            key = key_func(arr[i])
            count[key] -= 1
            result[count[key]] = arr[i]

        return result

    n = len(s)
    if n == 0:
        return []

    # Initial ranking based on first character
    suffixes = list(range(n))
    rank = [ord(c) for c in s]

    k = 1
    while k < n:
        # Sort by (rank[i], rank[i + k])
        def sort_key(i):
            return (rank[i], rank[i + k] if i + k < n else -1)

        # Find maximum rank for counting sort
        max_rank = max(rank) + 1

        # Sort by second key first, then by first key
        suffixes = counting_sort(suffixes, lambda i: rank[i + k] if i + k < n else -1, max_rank)
        suffixes = counting_sort(suffixes, lambda i: rank[i], max_rank)

        # Update ranks
        new_rank = [0] * n
        for i in range(1, n):
            if (rank[suffixes[i]], rank[suffixes[i] + k] if suffixes[i] + k < n else -1) == \
               (rank[suffixes[i - 1]], rank[suffixes[i - 1] + k] if suffixes[i - 1] + k < n else -1):
                new_rank[suffixes[i]] = new_rank[suffixes[i - 1]]
            else:
                new_rank[suffixes[i]] = i

        rank = new_rank
        k *= 2

    return suffixes

def lcp_array(s, suffix_array):
    """Compute LCP (Longest Common Prefix) array"""
    n = len(s)
    if n == 0:
        return []

    # Inverse of suffix array
    rank = [0] * n
    for i in range(n):
        rank[suffix_array[i]] = i

    lcp = [0] * (n - 1)
    h = 0

    for i in range(n):
        if rank[i] > 0:
            j = suffix_array[rank[i] - 1]
            while i + h < n and j + h < n and s[i + h] == s[j + h]:
                h += 1
            lcp[rank[i] - 1] = h
            if h > 0:
                h -= 1

    return lcp
```

### 模板 6：DFA／狀態機（字串驗證） — LC 65 ⭐⭐⭐⭐

> **關鍵想法**：當一個字串格式的規則全是「這個只能接在那個後面」這種糾纏在一起的條件時，
> 別再寫巢狀 `if` 了，改寫**轉移表**。先把每個字元歸到少數幾個**字元類別**，
> 再讓一個 `state` 整數在手刻的 DFA 上跑。
> 一趟掃完、O(1) 記憶體，而且所有規則都集中在一張看得懂的表裡。
>
> **什麼時候該拿出來用**：格式驗證／斷詞（LC 65 Valid Number）這種題，
> 臨時拼湊的 `if/else` 解法正是多數人在邊界情況上翻船的地方（`"."`、`"4e+"`、`"3."`、`".9"`）。
> 這跟 KMP 底下的自動機是同一個概念（LPS 陣列**本身**就是一台比對自動機）—
> 差別只在這裡的自動機是手寫的，不是從模式推導出來的。

**做法**
1. 列出字元類別（這裡是 `digit`、`sign`、`dot`、`exp`）— 其他字元一律直接拒絕。
2. 列出狀態，每個狀態代表一種「我目前合法看過的東西」。
3. 填表；`-1` 代表死狀態。
4. 標出**接受狀態**；字串合法的充要條件是它停在其中之一。

```java
// java
// LC 65 - Valid Number
// IDEA: hand-built DFA — classify char, follow transition table, accept only in a terminal state
// time = O(N), space = O(1)   (table is a fixed 8x4 constant)
public class ValidNumber {
    // rows = states, cols = char class {0:digit, 1:sign, 2:dot, 3:exp}, -1 = dead
    private static final int[][] DFA = {
        //  d   s   .   e
        {   2,  1,  3, -1 },  // 0 start
        {   2, -1,  3, -1 },  // 1 after leading sign
        {   2, -1,  4,  5 },  // 2 integer digits          (ACCEPT)
        {   4, -1, -1, -1 },  // 3 dot with no digit yet
        {   4, -1, -1,  5 },  // 4 fraction digits         (ACCEPT)
        {   7,  6, -1, -1 },  // 5 just saw 'e' / 'E'
        {   7, -1, -1, -1 },  // 6 sign after 'e'
        {   7, -1, -1, -1 },  // 7 exponent digits         (ACCEPT)
    };

    public boolean isNumber(String s) {
        int state = 0;
        for (char c : s.toCharArray()) {
            int cls;
            if (c >= '0' && c <= '9') cls = 0;
            else if (c == '+' || c == '-') cls = 1;
            else if (c == '.') cls = 2;
            else if (c == 'e' || c == 'E') cls = 3;
            else return false;                 // illegal character
            state = DFA[state][cls];
            if (state == -1) return false;     // illegal transition
        }
        return state == 2 || state == 4 || state == 7;   // accepting states
    }
}
```

```python
# python
# LC 65 - Valid Number
# IDEA: hand-built DFA — classify char, follow transition table, accept only in a terminal state
# time = O(N), space = O(1)
DFA = [
    #  d   s   .   e
    [  2,  1,  3, -1],   # 0 start
    [  2, -1,  3, -1],   # 1 after leading sign
    [  2, -1,  4,  5],   # 2 integer digits          (ACCEPT)
    [  4, -1, -1, -1],   # 3 dot with no digit yet
    [  4, -1, -1,  5],   # 4 fraction digits         (ACCEPT)
    [  7,  6, -1, -1],   # 5 just saw 'e' / 'E'
    [  7, -1, -1, -1],   # 6 sign after 'e'
    [  7, -1, -1, -1],   # 7 exponent digits         (ACCEPT)
]

def isNumber(s):
    state = 0
    for c in s:
        if c.isdigit():
            cls = 0
        elif c in "+-":
            cls = 1
        elif c == ".":
            cls = 2
        elif c in "eE":
            cls = 3
        else:
            return False              # illegal character
        state = DFA[state][cls]
        if state == -1:               # illegal transition
            return False
    return state in (2, 4, 7)         # accepting states
```

**為什麼是狀態 2/4/7、而不是別的** — 一個合法數字只有三種結尾方式：

```text
"53"      0 -d-> 2                     accept (integer)
"53.5"    0 -d-> 2 -.-> 4 -d-> 4       accept (fraction)
"3."      0 -d-> 2 -.-> 4              accept ("digits then dot" is legal)
".9"      0 -.-> 3 -d-> 4              accept (dot first needs a digit after)
"."       0 -.-> 3                     REJECT (state 3 is not accepting)
"53e-9"   0 -d-> 2 -e-> 5 -s-> 6 -d-> 7  accept
"4e+"     0 -d-> 2 -e-> 5 -s-> 6       REJECT (exponent needs a digit)
"99e2.5"  ... state 7 -.-> -1          REJECT (no dot in the exponent)
```

**面試小技巧**：先在白板上把狀態圖畫出來，再抄成表。這張表讓解法可以自己被檢查 — 面試官讀一格就能驗證一條規則。

## 依模式分類的題目

### **模式比對類**
| 題目 | LC # | 最佳演算法 | 時間複雜度 | 難度 |
|---------|------|----------------|-----------------|------------|
| Find Index of First Occurrence | 28 | KMP | O(n + m) | Medium |
| Repeated Substring Pattern | 459 | KMP/Z-Algorithm | O(n) | Easy |
| Shortest Palindrome | 214 | KMP + Reverse | O(n) | Hard |

### **回文類**
| 題目 | LC # | 最佳演算法 | 時間複雜度 | 難度 |
|---------|------|----------------|-----------------|------------|
| Longest Palindromic Substring | 5 | Manacher | O(n) | Medium |
| Palindromic Substrings | 647 | Manacher | O(n) | Medium |
| Shortest Palindrome | 214 | Manacher/KMP | O(n) | Hard |

### **進階字串類**
| 題目 | LC # | 最佳演算法 | 時間複雜度 | 難度 |
|---------|------|----------------|-----------------|------------|
| Longest Duplicate Substring | 1044 | Rolling Hash + 二分搜尋 | O(n log n) | Hard |
| Distinct Echo Substrings | 1316 | Rolling Hash | O(n²) | Hard |
| Find All Anagrams | 438 | Rolling Hash | O(n) | Medium |

## LC 範例

### 2-1) Find Index of First Occurrence（LC 28） — KMP
> 為模式建 LPS（failure）陣列；比對失敗時直接跳過那些不必要的比較。

```java
// LC 28 - Find the Index of the First Occurrence in a String
// IDEA: KMP — build LPS array; on mismatch jump to lps[j-1] instead of restarting
// time = O(N+M), space = O(M)
public int strStr(String haystack, String needle) {
    int n = haystack.length(), m = needle.length();
    int[] lps = new int[m];
    for (int i = 1, len = 0; i < m; ) {
        if (needle.charAt(i) == needle.charAt(len)) lps[i++] = ++len;
        else if (len > 0) len = lps[len - 1];
        else lps[i++] = 0;
    }
    for (int i = 0, j = 0; i < n; ) {
        if (haystack.charAt(i) == needle.charAt(j)) { i++; j++; }
        if (j == m) return i - j;
        else if (i < n && haystack.charAt(i) != needle.charAt(j))
            j = j > 0 ? lps[j - 1] : 0;
        if (j == 0 && i < n && haystack.charAt(i) != needle.charAt(j)) i++;
    }
    return -1;
}
```

```python
def strStr(haystack, needle):
    """KMP implementation for string search"""
    if not needle:
        return 0

    def compute_lps(pattern):
        """Compute Longest Prefix Suffix array"""
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
    lps = compute_lps(needle)

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
```

#### 變形：LC 686 Repeated String Match — *難的是界定 haystack 要多長，不是搜尋本身*

> 搜尋部分跟 LC 28 的 KMP 一模一樣；唯一的新東西是證明 haystack 到底要接多長。
> 如果 `b` 塞得進重複的 `a`，那它一定塞得進 `ceil(|b|/|a|)` 份（長度夠了）或
> `ceil(|b|/|a|) + 1` 份（多一份是為了涵蓋從某一份中間開始的比對）。再多接就沒有新的對齊方式了 —
> 所以**只有兩個候選，都不行就回 `-1`**。
>（`string.md` 給的是單純字串串接的解法；這裡是 O(N+M) 的比對版本。）

```java
// java
// LC 686 - Repeated String Match
// IDEA: repeat `a` to ceil(|b|/|a|) copies, then +1 more; KMP-search `b` in each. Nothing longer can help.
// time = O(N + M), space = O(N + M)   (N=|a|, M=|b|; built haystack is O(N+M) long)
public int repeatedStringMatch(String a, String b) {
    int reps = (b.length() + a.length() - 1) / a.length();   // ceil
    StringBuilder sb = new StringBuilder();
    for (int i = 0; i < reps; i++) sb.append(a);
    if (kmpContains(sb.toString(), b)) return reps;
    sb.append(a);                                            // one extra copy for a mid-copy start
    if (kmpContains(sb.toString(), b)) return reps + 1;
    return -1;
}

private boolean kmpContains(String text, String pat) {
    int m = pat.length();
    int[] lps = new int[m];
    for (int i = 1, len = 0; i < m; ) {
        if (pat.charAt(i) == pat.charAt(len)) lps[i++] = ++len;
        else if (len > 0) len = lps[len - 1];
        else lps[i++] = 0;
    }
    for (int i = 0, j = 0; i < text.length(); i++) {
        while (j > 0 && text.charAt(i) != pat.charAt(j)) j = lps[j - 1];
        if (text.charAt(i) == pat.charAt(j)) j++;
        if (j == m) return true;
    }
    return false;
}
```

```python
# python
# LC 686 - Repeated String Match
# IDEA: only two candidate repeat counts, ceil(|b|/|a|) and one more; substring-search each
# time = O(N + M), space = O(N + M)
def repeatedStringMatch(a, b):
    reps = -(-len(b) // len(a))          # ceil division
    for k in (reps, reps + 1):
        if b in a * k:                   # CPython's `in` is linear-time; or reuse kmp_search() above
            return k
    return -1
```

**常見錯誤**：寫成 `while len(a*k) < len(b) + 2*len(a)` 這種用猜的迴圈，或一路重複到某個隨便訂的上限。把界限明確講出來 — 面試官考的是這段論證，不是搜尋。

### 2-2) Longest Palindromic Substring（LC 5） — 中心擴展
> 試過每一個中心（奇數長度與偶數長度）；只要字元相同就往外擴。

```java
// LC 5 - Longest Palindromic Substring
// IDEA: Expand around center for each of 2N-1 centers; track longest
// time = O(N^2), space = O(1)
public String longestPalindrome(String s) {
    int start = 0, maxLen = 1;
    for (int i = 0; i < s.length(); i++) {
        for (int d = 0; d <= 1; d++) {  // d=0: odd length, d=1: even length
            int l = i, r = i + d;
            while (l >= 0 && r < s.length() && s.charAt(l) == s.charAt(r)) { l--; r++; }
            if (r - l - 1 > maxLen) { maxLen = r - l - 1; start = l + 1; }
        }
    }
    return s.substring(start, start + maxLen);
}
```

```python
def longestPalindrome(s):
    """Manacher's algorithm for longest palindromic substring"""
    if not s:
        return ""

    # Preprocess string
    processed = "^#" + "#".join(s) + "#$"
    n = len(processed)
    p = [0] * n
    center = right = 0

    for i in range(1, n - 1):
        mirror = 2 * center - i

        if i < right:
            p[i] = min(right - i, p[mirror])

        # Try to expand
        while processed[i + p[i] + 1] == processed[i - p[i] - 1]:
            p[i] += 1

        # Update center and right boundary
        if i + p[i] > right:
            center, right = i, i + p[i]

    # Find longest palindrome
    max_len = 0
    center_index = 0
    for i in range(1, n - 1):
        if p[i] > max_len:
            max_len = p[i]
            center_index = i

    start = (center_index - max_len) // 2
    return s[start:start + max_len]
```

### 2-3) Longest Duplicate Substring（LC 1044） — 二分搜尋 + Rolling Hash
> 對長度做二分搜尋；用 Rabin-Karp rolling hash 檢查該長度的重複子字串是否存在。

```java
// LC 1044 - Longest Duplicate Substring
// IDEA: Binary search on length L; Rabin-Karp rolling hash checks duplicate in O(N)
// time = O(N log N), space = O(N)
public String longestDupSubstring(String s) {
    int lo = 1, hi = s.length() - 1;
    String ans = "";
    while (lo <= hi) {
        int mid = (lo + hi) / 2;
        String dup = findDuplicate(s, mid);
        if (dup != null) { ans = dup; lo = mid + 1; }
        else hi = mid - 1;
    }
    return ans;
}
private String findDuplicate(String s, int len) {
    long MOD = (1L << 61) - 1, BASE = 31;
    long power = 1;
    for (int i = 0; i < len; i++) power = power * BASE % MOD;
    long hash = 0;
    for (int i = 0; i < len; i++) hash = (hash * BASE + s.charAt(i)) % MOD;
    Map<Long, List<Integer>> seen = new HashMap<>();
    seen.computeIfAbsent(hash, k -> new ArrayList<>()).add(0);
    for (int i = len; i < s.length(); i++) {
        hash = (hash * BASE - s.charAt(i - len) * power % MOD + s.charAt(i) + MOD) % MOD;
        int start = i - len + 1;
        if (seen.containsKey(hash)) {
            String sub = s.substring(start, start + len);
            for (int prev : seen.get(hash))
                if (s.substring(prev, prev + len).equals(sub)) return sub;
        }
        seen.computeIfAbsent(hash, k -> new ArrayList<>()).add(start);
    }
    return null;
}
```

```python
def longestDupSubstring(s):
    """Rolling hash with binary search"""

    def has_duplicate(length):
        base = 256
        mod = 2**63 - 1
        base_power = pow(base, length, mod)

        current_hash = 0
        for i in range(length):
            current_hash = (current_hash * base + ord(s[i])) % mod

        seen = {current_hash}

        for i in range(length, len(s)):
            # Remove leftmost character and add rightmost
            current_hash = (current_hash - ord(s[i - length]) * base_power) % mod
            current_hash = (current_hash * base + ord(s[i])) % mod

            if current_hash in seen:
                return i - length + 1
            seen.add(current_hash)

        return -1

    left, right = 0, len(s) - 1
    result_start = 0

    while left <= right:
        mid = (left + right) // 2
        start_pos = has_duplicate(mid)

        if start_pos != -1:
            result_start = start_pos
            left = mid + 1
        else:
            right = mid - 1

    return s[result_start:result_start + right] if right > 0 else ""
```

#### 變形：LC 718 Maximum Length of Repeated Subarray — *一樣是二分搜尋 + hash，但跨**兩個**序列，而且處理的是整數不是字元*

> LC 1044 是在一個字串裡找重複；LC 718 是在兩個陣列之間找共同的區塊。
> 單調的判定條件完全一樣（「若存在長度 `L` 的共同區塊，那長度 `L-1` 的也存在」），
> 所以對 `L` 做二分搜尋，並把兩個陣列的每個視窗都 hash 起來：先把 `nums1` 的所有視窗雜湊值丟進集合，
> 再掃 `nums2` 看有沒有命中。值本來就是整數，直接餵進 rolling hash 即可 — 不需要 `ord()`。
>
> 這題面試上預期的答案是 O(N·M) 的 LCS 型 DP（見 `dp_pattern.md`）。當面試官追問能不能比 O(N·M) 更快時，
> 再把這個當成 follow-up 端出來 — 它是 O((N+M)·log min(N,M))。

```java
// java
// LC 718 - Maximum Length of Repeated Subarray
// IDEA: binary search the answer length L; rolling-hash every length-L window of both arrays and intersect
// time = O((N+M) log(min(N,M))), space = O(N)
public int findLength(int[] nums1, int[] nums2) {
    int lo = 0, hi = Math.min(nums1.length, nums2.length);
    while (lo < hi) {
        int mid = (lo + hi + 1) / 2;          // upper-mid: we shrink hi, so avoid infinite loop
        if (hasCommon(nums1, nums2, mid)) lo = mid;
        else hi = mid - 1;
    }
    return lo;
}

private boolean hasCommon(int[] a, int[] b, int len) {
    long MOD = 1_000_000_007L, BASE = 1_000_003L, power = 1;
    for (int i = 0; i < len; i++) power = power * BASE % MOD;

    Set<Long> seen = new HashSet<>();
    long h = 0;
    for (int i = 0; i < a.length; i++) {
        h = (h * BASE + a[i]) % MOD;                                  // push right
        if (i >= len) h = (h - a[i - len] * power % MOD + MOD) % MOD; // pop left
        if (i >= len - 1) seen.add(h);
    }
    h = 0;
    for (int i = 0; i < b.length; i++) {
        h = (h * BASE + b[i]) % MOD;
        if (i >= len) h = (h - b[i - len] * power % MOD + MOD) % MOD;
        if (i >= len - 1 && seen.contains(h)) return true;
    }
    return false;
}
```

```python
# python
# LC 718 - Maximum Length of Repeated Subarray
# IDEA: binary search the answer length L; rolling-hash every length-L window of both arrays and intersect
# time = O((N+M) log(min(N,M))), space = O(N)
def findLength(nums1, nums2):
    MOD, BASE = (1 << 61) - 1, 1000003

    def window_hashes(arr, L):
        power = pow(BASE, L, MOD)
        out, h = set(), 0
        for i, v in enumerate(arr):
            h = (h * BASE + v) % MOD                 # push right
            if i >= L:
                h = (h - arr[i - L] * power) % MOD   # pop left
            if i >= L - 1:
                out.add(h)
        return out

    def has_common(L):
        seen = window_hashes(nums1, L)
        return any(h in seen for h in window_hashes(nums2, L))

    lo, hi = 0, min(len(nums1), len(nums2))
    while lo < hi:
        mid = (lo + hi + 1) // 2
        if has_common(mid):
            lo = mid
        else:
            hi = mid - 1
    return lo
```

**兩個陷阱**：(1) 要用**上中點** `(lo + hi + 1) // 2` 搭配 `lo = mid` / `hi = mid - 1` 的更新方式，
否則搜尋不會終止；(2) 只用一個 32-bit 左右的模數很容易碰撞 —
記得說出來你會在雜湊命中時比對實際視窗來驗證，或是改用下面的 `DoubleHash` 類別。

## 進階技巧

### 多模式比對
```python
class AhoCorasick:
    """Aho-Corasick algorithm for multiple pattern matching"""

    def __init__(self):
        self.trie = {}
        self.failure = {}
        self.output = {}

    def add_pattern(self, pattern, pattern_id):
        """Add pattern to trie"""
        node = self.trie
        for char in pattern:
            if char not in node:
                node[char] = {}
            node = node[char]
        node['$'] = pattern_id

    def build_failure_function(self):
        """Build failure function for Aho-Corasick"""
        from collections import deque

        queue = deque()
        self.failure[id(self.trie)] = self.trie

        # Initialize first level
        for char, child in self.trie.items():
            if char != '$':
                self.failure[id(child)] = self.trie
                queue.append((child, char))

        # Build failure links using BFS
        while queue:
            current, char = queue.popleft()
            current_id = id(current)

            for next_char, next_node in current.items():
                if next_char == '$':
                    continue

                queue.append((next_node, next_char))

                # Find failure link
                failure_node = self.failure[current_id]
                while failure_node != self.trie and next_char not in failure_node:
                    failure_node = self.failure[id(failure_node)]

                if next_char in failure_node:
                    self.failure[id(next_node)] = failure_node[next_char]
                else:
                    self.failure[id(next_node)] = self.trie

    def search_all(self, text):
        """Find all pattern occurrences in text"""
        current = self.trie
        results = []

        for i, char in enumerate(text):
            # Follow failure links
            while current != self.trie and char not in current:
                current = self.failure[id(current)]

            if char in current:
                current = current[char]

                # Check for matches
                temp = current
                while temp != self.trie:
                    if '$' in temp:
                        pattern_id = temp['$']
                        results.append((i, pattern_id))
                    temp = self.failure[id(temp)]

        return results
```

### 字串雜湊的最佳化
```python
class DoubleHash:
    """Double hashing to reduce collision probability"""

    def __init__(self, s):
        self.s = s
        self.n = len(s)

        # Two different bases and moduli
        self.base1, self.mod1 = 257, 10**9 + 7
        self.base2, self.mod2 = 263, 10**9 + 9

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
        """Get double hash of substring"""
        length = right - left + 1
        h1 = (self.hash1[right + 1] - self.hash1[left] * self.pow1[length]) % self.mod1
        h2 = (self.hash2[right + 1] - self.hash2[left] * self.pow2[length]) % self.mod2
        return (h1, h2)
```

## 效能最佳化建議

### 演算法選擇指南
```python
def choose_string_algorithm(problem_characteristics):
    """Guide for choosing optimal string algorithm"""

    if problem_characteristics['type'] == 'pattern_matching':
        if problem_characteristics['single_pattern']:
            return "KMP or Z-Algorithm"
        else:
            return "Aho-Corasick"

    elif problem_characteristics['type'] == 'palindromes':
        if problem_characteristics['all_palindromes']:
            return "Manacher's Algorithm"
        else:
            return "Expand around centers"

    elif problem_characteristics['type'] == 'substring_queries':
        if problem_characteristics['many_queries']:
            return "Rolling Hash or Suffix Array"
        else:
            return "Simple comparison"

    elif problem_characteristics['type'] == 'string_matching':
        if problem_characteristics['approximate']:
            return "Edit distance DP"
        else:
            return "KMP or Rolling Hash"
```

### 這個進階演算法真的划算嗎？ ⭐⭐⭐⭐⭐

面試官其實很少*想*看到 Manacher 或後綴陣列。他們要的是直觀的解法、寫對寫乾淨，
再加一句「如果限制真的逼我，我會這樣把它壓下來」。先給預期的答案，再把進階版當 follow-up 提出來。

| 題目 | 面試官預期的解法 | 進階選項 | 值得換嗎？ |
|---------|------------------------------|-----------------|------------------|
| LC 28 Find First Occurrence | 滑動比對，O(N·M) | **KMP**，O(N+M) | **值得** — 這題**就是**在考 KMP；用暴力解會被讀成「不會」 |
| LC 5 Longest Palindromic Substring | 中心擴展，O(N²) | **Manacher**，O(N) | **不值得** — 只有被要求 O(N) 才寫 Manacher。中心擴展就是被接受的答案（見 `palindrome.md`） |
| LC 647 Palindromic Substrings | 中心擴展，O(N²) | Manacher，O(N) | **不值得** — 理由同上 |
| LC 686 Repeated String Match | `b in a*k` 加上界限論證 | KMP 搜尋 | **很少需要** — 重點是**界限的證明**，不是比對器 |
| LC 718 Max Length of Repeated Subarray | LCS 型 DP，O(N·M) | 二分搜尋 + rolling hash，O((N+M)·log N) | **只在 follow-up 時** — 先 DP，再提這個 |
| LC 1044 Longest Duplicate Substring | （沒有簡單解 — O(N²) 會 TLE） | **二分搜尋 + Rabin-Karp** | **值得** — 進階解是唯一過得了的解 |
| LC 1316 Distinct Echo Substrings | — | Rolling hash | **值得** — 雜湊本來就是這題預設的工具 |
| LC 214 Shortest Palindrome | 反轉 + 前綴檢查，O(N²) | 在 `s + '#' + rev(s)` 上跑 KMP 的 prefix function | **被要求 O(N) 時值得** — 見 `palindrome.md` |
| LC 10 / LC 44 Regex & Wildcard Matching | **二維 DP** — 不需要字串自動機 | 自己建 NFA/DFA | **不值得** — DP 就是答案；見 `dp_pattern.md`、`recursion_to_dp.md` |
| LC 65 Valid Number | 東拼西湊的 flag 大亂鬥 | **DFA 表**（模板 6） | **值得** — 表格版**更短**，而且可以證明它涵蓋了所有邊界情況 |

**經驗法則**：在下列情況才動用進階結構 — (a) 暴力解的複雜度在題目給的限制下真的會 TLE、
(b) 同一個字串要被查很多次，預處理攤提得下來，或 (c) 進階版其實**更容易**寫對（LC 65 的 DFA、LC 28 的 KMP）。
其他時候它就是個包袱 — 更多程式碼、更多 bug、更少時間講話。

### 鄰近的模式各自住在哪裡

下面這些屬於其他 cheatsheet，在這裡只做交叉引用，讓這頁專心處理後綴結構、自動機、雜湊和線性時間比對：

- **LC 336 Palindrome Pairs** — 反轉單字的 trie／雜湊表拆分 → `trie.md`、`palindrome.md`
- **LC 208 / 211 / 212 / 472 / 648**（Trie、Add & Search Words、Word Search II、Concatenated Words、Replace Words）→ `trie.md`
- **LC 10 / 44**（Regular Expression & Wildcard Matching） — 兩個字串的布林 DP 表格 → `dp_pattern.md`、`recursion_to_dp.md`
- **LC 3 / 438**（Longest Substring Without Repeating Characters、Find All Anagrams） — 視窗 + 計數，不需要雜湊 → `sliding_window.md`、`string.md`
- **LC 8 / 12 / 13 / 43 / 68 / 273 / 443**（解析、格式化、大數字串） — 一般的字串操作 → `string.md`
- **LC 20 / 224 / 227 / 394 / 1249**（括號與運算式解析） — 堆疊機，不是字串演算法 → `stack.md`、`string.md`

## 總結與速查

### 演算法複雜度比較

| 演算法 | 時間複雜度 | 空間複雜度 | 最適合的場景 |
|-----------|-----------------|------------------|---------------|
| KMP | O(n + m) | O(m) | 單一模式搜尋 |
| Manacher | O(n) | O(n) | 所有回文 |
| Z-Algorithm | O(n) | O(n) | 模式比對的各種變形 |
| Rolling Hash | O(n) | O(1) 攤提 | 多次查詢 |
| 後綴陣列 | O(n log n) | O(n) | 複雜的字串操作 |

### 常見錯誤與建議

**🚫 常見錯誤：**
- 沒處理空字串或空模式
- 索引計算的差一錯誤
- 只用單一雜湊，碰撞問題沒解決
- failure function 算錯

**✅ 最佳實務：**
- 一定要先驗證輸入字串
- 用雙雜湊來抵抗碰撞
- 預期會多次查詢時就先預處理
- 依題目限制挑演算法
- 用邊界情況和長字串測過

### 面試建議
1. **先辨認核心需求**：是模式比對、回文，還是多次查詢
2. **挑對演算法**：依時間／空間限制決定
3. **處理邊界情況**：空字串、單一字元
4. **考慮預處理**：查詢次數多的時候
5. **實作要小心**：索引管理是關鍵
6. **測試要徹底**：各種長度與模式的字串都試過

這份進階字串演算法 cheatsheet 收錄了字串處理與模式比對上最講究的那些技巧。
