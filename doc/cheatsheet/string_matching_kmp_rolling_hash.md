# String Matching (KMP, Rolling Hash)

> **Scope** — Substring search only — KMP's failure function, Rabin-Karp rolling hash, and the built-in-`indexOf`-vs-KMP-vs-hash decision.
> **See also**: [string.md](./string.md) — general string handling; [advanced_string_algorithms.md](./advanced_string_algorithms.md) — Z-algorithm and suffix structures; [hashing.md](./hashing.md) — hash design and collisions.

## LeetCode Problem Lists

- [String Matching](https://leetcode.com/problem-list/string-matching/)
- [Rolling Hash](https://leetcode.com/problem-list/rolling-hash/)
- [Hash Function](https://leetcode.com/problem-list/hash-function/)

## 0) Concept

### 0-1) Types

**String Matching** refers to finding occurrences of a pattern within a text efficiently. The two most important algorithms are:

1. **KMP (Knuth-Morris-Pratt)**: Uses a failure function to avoid redundant comparisons
2. **Rolling Hash (Rabin-Karp)**: Uses polynomial hashing to compare substrings in O(1) time

### 0-2) Pattern

Both algorithms optimize naive string matching from O(nm) to O(n+m) or O(n) where:
- n = length of text
- m = length of pattern

**Key Characteristics:**
- **KMP**: Deterministic, no hash collisions, O(n+m) time, O(m) space
- **Rolling Hash**: Probabilistic, may have collisions, O(n) average time, O(1) space
- **When to Use KMP**: Single pattern search, guaranteed correctness
- **When to Use Rolling Hash**: Multiple patterns, substring comparisons, duplicate detection

## 1) General form

### 1-1) Basic OP

#### KMP Algorithm Components

1. **Failure Function (LPS Array)**
   - LPS = Longest Proper Prefix which is also Suffix
   - Helps skip unnecessary comparisons
   - Time: O(m), Space: O(m)

2. **Pattern Matching**
   - Uses LPS array to avoid backtracking in text
   - Time: O(n), Space: O(1)

#### Rolling Hash Components

1. **Hash Calculation**
   - Polynomial hash: hash = (c₁ × base^(m-1) + c₂ × base^(m-2) + ... + cₘ) % mod
   - Base: typically 256 or prime number
   - Mod: large prime to reduce collisions

2. **Rolling Window**
   - Remove leftmost character: hash = (hash - c₀ × base^(m-1)) % mod
   - Add rightmost character: hash = (hash × base + cₘ) % mod

### 1-2) Templates

#### KMP Template

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

#### Rolling Hash Template

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

### 1-3) Double Hashing (Collision Reduction)

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

### 1-4) Repeat Text Until It Contains the Pattern — LC 686 ⭐⭐⭐⭐

**Key Idea**: "how many copies of `a` until `b` is a substring?" looks unbounded, but it is **two candidates only**. Any occurrence of `b` inside `aaaa...` can be shifted so it starts inside the *first* copy of `a`; from there it spans at most `ceil(|b| / |a|)` copies, plus one extra copy for the tail that hangs over the boundary. So test `k = ceil(|b|/|a|)` and `k + 1` — if neither repeated string contains `b`, the answer is `-1`.

**When to use**: any "repeat / concatenate a block until it contains X" question. The bound-the-candidates argument is the whole interview; the containment check is just KMP (reuse `1-2`).

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

**Gotchas**
- Do **not** loop `while len(text) < some_bound`: state the two-candidate bound explicitly, it is what the interviewer is checking.
- `count` can be 1 when `|b| <= |a|` (e.g. `a="aa", b="a"` → 1). The `ceil` handles it; don't special-case.

---

### 1-5) Fixed-Length Window Duplicate Detection (integer rolling hash) — LC 187 ⭐⭐⭐⭐

**Key Idea**: when the alphabet is tiny and the window length is fixed, the window **is** an integer — no modulus needed. With 4 DNA letters, 2 bits each, a 10-char window is exactly 20 bits: roll with `h = ((h << 2) | code[c]) & mask`. That is a *collision-free* rolling hash, so no verification step is required. Fall back to the polynomial hash mod p (template `1-2`) only when the window does not fit in a machine word.

**When to use**: "find/count all repeated substrings of fixed length k", sliding-window de-duplication, any fixed-width window over a small alphabet.

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

**Variation — LC 1461 (Check If a String Contains All Binary Codes of Size K)**: same rolling bitmask, 1 bit per character instead of 2, and the question becomes *counting distinct windows*: the answer is `True` iff we saw all `2^k` of them.

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

### 1-6) Binary Search on Length + Rolling Hash over **Arrays** — LC 718 ⭐⭐⭐⭐

**Key Idea**: the predicate *"a common block of length L exists"* is **monotone** — if one of length `L` exists, so does one of length `L-1` (take a prefix). That licenses binary search on `L`, and each `check(L)` is one O(n+m) hash pass: hash every length-`L` window of the first sequence into a set, then stream the second sequence's windows and test membership. Nothing here is string-specific — hash the raw integers, so this generalizes template `2-3` (LC 1044) from characters to arrays.

**When to use**: "longest common / repeated block of length L" where a direct DP is O(n·m) and you need O((n+m)·log). Also the reflex when the input is an int array rather than a string.

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

**Gotchas**
- Use the **upper mid** `(lo + hi + 1) // 2` with `lo = mid` / `hi = mid - 1`, otherwise the loop hangs when `hi == lo + 1`.
- A single 32-bit-ish modulus will collide across ~10⁵ windows (birthday bound). Use mod `2^61 - 1`, or double hashing (`1-3`), or verify the candidate by direct comparison.
- The classic DP `dp[i][j] = dp[i+1][j+1] + 1` is O(n·m) time / O(m) space and is easier to write — lead with it, then offer this when asked to beat O(n·m).

**Variation — LC 1923 (Longest Common Subpath)**: same binary-search-on-length skeleton, but `check(L)` must hold across **all** paths: hash the length-`L` windows of the first path into a set, then repeatedly intersect with the next path's window hashes; the answer is `L` if the intersection survives non-empty.

---

### 1-7) Serialize + KMP for Structural (Tree) Matching — LC 572 ⭐⭐⭐

**Key Idea**: "is B a subtree of A?" becomes "is `serialize(B)` a **substring** of `serialize(A)`?" once the serialization is *unambiguous*. Two markers do it: `#` for every null child (so shape is encoded) and a `^` before every value (so `^2` can never match inside `^12`). Then one KMP pass gives O(n+m) instead of the naive O(n·m) "compare-at-every-node".

**When to use**: subtree / sub-structure containment, deduplicating subtrees, any tree question that turns into a linear-pattern question after serialization.

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

**Gotchas**
- Skipping the null markers breaks it: `[1,2]` and `[1,null,2]` serialize the same.
- Skipping the value prefix breaks it: pattern `^2##` would otherwise match inside `^12##`, and negative values (`-2`) collide with the minus sign.
- A rolling hash of the serialized strings works too, but then you must verify a hit — KMP is exact and just as fast here.

## 2) LC Example

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

### 2-7) Implement strStr() Variants

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

## 3) Common Applications

### 3-1) Pattern Matching Problems
- Find first/all occurrences
- Multiple pattern search
- Substring with wildcard matching

### 3-2) String Period Detection
- Repeated substring pattern
- String rotation
- Cyclic string comparison

### 3-3) Palindrome Problems
- Shortest palindrome
- Palindromic prefix/suffix

### 3-4) Duplicate Detection
- Longest duplicate substring
- Distinct substrings
- Echo substrings

## 4) Algorithm Comparison

| Algorithm | Time | Space | Pros | Cons |
|-----------|------|-------|------|------|
| **KMP** | O(n+m) | O(m) | Deterministic, no collisions | More complex implementation |
| **Rolling Hash** | O(n) avg | O(1) | Simple, multiple patterns | Hash collisions possible |
| **Naive** | O(nm) | O(1) | Very simple | Too slow for large inputs |

## 5) Tips & Tricks

### 5-1) When to Use KMP
- Single pattern matching
- Need guaranteed correctness
- Pattern has repeating structure
- LC 28, 214, 459, 1392

### 5-2) When to Use Rolling Hash
- Multiple pattern search
- Substring comparison
- Duplicate detection
- LC 1044, 1316, 718, 1062

### 5-3) Common Patterns
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

## 6) Summary

### Key Takeaways
1. **KMP** is optimal for single pattern matching with O(n+m) time
2. **Rolling Hash** excels at substring comparison in O(1) amortized time
3. Use **double hashing** to minimize collision probability
4. **Binary search + hash** is powerful for optimization problems
5. LPS array reveals string periodicity and structure

### Common LeetCode Problems
| Problem | Number | Algorithm | Difficulty |
|---------|--------|-----------|------------|
| Implement strStr() | 28 | KMP/Hash | Easy |
| Shortest Palindrome | 214 | KMP | Hard |
| Repeated Substring Pattern | 459 | KMP | Easy |
| Rotate String | 796 | String | Easy |
| Longest Duplicate Substring | 1044 | Hash+BS | Hard |
| Distinct Echo Substrings | 1316 | Hash | Hard |
| Longest Happy Prefix | 1392 | KMP | Hard |
| Maximum Repeating Substring | 1668 | String | Easy |

### Interview Tips
1. Always clarify if overlapping matches are allowed
2. Consider string length constraints (KMP vs naive)
3. For hash, explain collision handling strategy
4. Time complexity: O(n+m) for KMP, O(n) for hash
5. Space complexity: O(m) for KMP, O(1) for basic hash

## 7) Decision Guide: built-in `indexOf` vs KMP vs Rolling Hash

Most "string matching" problems do **not** need KMP. Pick by what the problem forces you to do:

| Situation | Use | Why |
|-----------|-----|-----|
| One pattern, `n·m` fits the constraints (`n, m <= ~10³`) | built-in `indexOf` / `in` / two pointers | Shortest correct code; say the worst case out loud |
| One pattern, huge text, need a **guaranteed** linear bound | **KMP** (`1-2`) | Deterministic O(n+m), no collisions |
| You need prefix==suffix / periodicity / "extend into a palindrome" | **KMP failure array only** (no search) | The LPS array *is* the answer (LC 459, 1392, 214) |
| Many patterns, or comparing arbitrary substring pairs in O(1) | **Rolling hash** (`1-2`, `1-3`) | One preprocessing pass answers any `(l, r)` query |
| "Longest X of length L" with a monotone predicate | **Binary search on L + rolling hash** (`1-6`, `2-3`) | Turns O(n²) scanning into O(n log n) |
| Fixed small window over a small alphabet | **Bit-packed rolling window** (`1-5`) | Exact, collision-free, no modulus |
| Matching a *structure* (tree/folder) rather than text | **Serialize + KMP** (`1-7`) | Containment becomes substring search |

**Practical note**: Java's `String.indexOf` is naive O(n·m) in the worst case; CPython's `in` / `str.find` uses a two-way algorithm that is linear in the worst case. Either way, state the bound you are relying on instead of leaning on the library.

### 7-1) Problems where naive matching is the right answer
These are tagged *string-matching* but the constraints are tiny — reach for the library, and spend the time on the loop structure instead:

- **LC 1408 - String Matching in an Array**: return words that are substrings of another word. `O(n²·L)` double loop with `contains` is intended.
- **LC 1455 - Check If a Word Occurs As a Prefix of Any Word in a Sentence**: `split(" ")` + `startsWith`; 1-indexed answer, `-1` if none.
- **LC 1023 - Camelcase Matching**: **subsequence**, not substring — greedy two pointers, and every unmatched query char must be lowercase. Good reminder that "matching" ≠ "substring".

### 7-2) Advanced applications (know the idea, code only if asked)
- **LC 1147 - Longest Chunked Palindrome Decomposition**: greedy two pointers from both ends; cut as soon as prefix chunk == suffix chunk (compare with rolling hash for O(n), direct compare is fine in practice). Greedy is optimal — the shortest possible matching chunk never hurts.
- **LC 1923 - Longest Common Subpath**: binary search on length + rolling hash, intersecting window-hash sets across *all* paths (see the variation under `1-6`).
- **LC 1948 - Delete Duplicate Folders in System**: the `1-7` idea one level up — canonically serialize each subtree of the folder trie, count identical serializations, delete every subtree whose serialization appears more than once.
- **LC 1397 - Find All Good Strings**: digit DP over the KMP **automaton** of `evil` — the DP state is (position, tight-low, tight-high, LPS-matched-length), and the failure array gives the next matched length for each candidate character. The rare case where you build the full automaton, not just the LPS array.

### 7-3) Cross-references (covered in sibling cheatsheets)
- **LC 214 - Shortest Palindrome** → `palindrome.md` Template 8 has the full KMP-prefix-function derivation (`s + "#" + reverse(s)`); section `2-4` above is the condensed version.
- **LC 336 - Palindrome Pairs** → `palindrome.md` Template 9 (HashMap split on prefix/suffix) and `trie.md`. Tagged *hash-function* but it is a hash-map/trie lookup problem, not rolling hash.
