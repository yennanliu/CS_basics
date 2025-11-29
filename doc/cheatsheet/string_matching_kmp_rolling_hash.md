# String Matching (KMP, Rolling Hash)

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
