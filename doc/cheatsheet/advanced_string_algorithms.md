# Advanced String Algorithms

> **Scope** — Heavier string machinery — suffix structures, Z-algorithm, Manacher, and string-DP — the parts too specialised for the main string doc.
> **See also**: [string.md](./string.md) — the everyday string catalogue and templates; [string_matching_kmp_rolling_hash.md](./string_matching_kmp_rolling_hash.md) — substring search specifically; [palindrome.md](./palindrome.md) — the palindrome family; [trie.md](./trie.md) — prefix structures.

## LeetCode Problem Lists

- [String](https://leetcode.com/problem-list/string/)
- [String Matching](https://leetcode.com/problem-list/string-matching/)
- [Rolling Hash](https://leetcode.com/problem-list/rolling-hash/)
- [Suffix Array](https://leetcode.com/problem-list/suffix-array/)

## Overview
**Advanced String Algorithms** encompass sophisticated techniques for string processing beyond basic operations. These algorithms provide optimal solutions for pattern matching, palindrome detection, and complex string manipulations with theoretical guarantees.

### Key Properties
- **Time Complexity**: Often O(n) or O(n + m) for optimal algorithms
- **Space Complexity**: O(n) for preprocessing structures
- **Core Idea**: Preprocess strings to enable fast queries and pattern matching
- **When to Use**: Complex string patterns, multiple queries, optimization needed
- **Key Algorithms**: KMP, Manacher's, Z-Algorithm, Rolling Hash, Suffix Arrays

### Core Characteristics
- **Preprocessing**: Build auxiliary structures for fast operations
- **Pattern Recognition**: Identify repeating structures and patterns
- **Linear Time**: Achieve optimal time complexity through clever techniques
- **Multiple Queries**: Efficient for repeated operations on same string
- **Theoretical Foundation**: Based on deep string theory and automata

## Problem Categories

### **Category 1: Pattern Matching**
- **Description**: Find occurrences of pattern in text efficiently
- **Examples**: LC 28 (Find Index of First Occurrence), LC 459 (Repeated Substring Pattern)
- **Pattern**: KMP, Z-Algorithm, Rolling Hash for O(n + m) solutions

### **Category 2: Palindrome Problems**
- **Description**: Find all palindromes or longest palindromic substrings
- **Examples**: LC 5 (Longest Palindromic Substring), LC 647 (Palindromic Substrings)
- **Pattern**: Manacher's Algorithm for O(n) palindrome detection

### **Category 3: String Periodicity**
- **Description**: Detect repeating patterns and string periods
- **Examples**: LC 459 (Repeated Substring Pattern), LC 1316 (Distinct Echo Substrings)
- **Pattern**: Period detection using failure function or Z-array

### **Category 4: Suffix-Based Problems**
- **Description**: Problems involving string suffixes and lexicographic ordering
- **Examples**: LC 1044 (Longest Duplicate Substring), LC 1316 (Distinct Echo Substrings)
- **Pattern**: Suffix arrays, longest common prefix, rolling hash

## Templates & Algorithms

### Template Comparison Table
| Algorithm | Use Case | Time Complexity | Space Complexity | When to Use |
|-----------|----------|-----------------|------------------|-------------|
| **KMP** | Pattern matching | O(n + m) | O(m) | Single pattern search |
| **Manacher's** | All palindromes | O(n) | O(n) | Palindrome problems |
| **Z-Algorithm** | String matching | O(n) | O(n) | Pattern matching variants |
| **Rolling Hash** | Substring comparison | O(n) | O(1) | Multiple pattern search |
| **DFA / State Machine** | Format validation / tokenizing | O(n) | O(1) | Messy `if/else` parsing rules (LC 65) |

### Template 1: KMP (Knuth-Morris-Pratt) Algorithm — LC 28
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

### Template 2: Manacher's Algorithm — LC 5
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

### Template 3: Z-Algorithm — LC 459
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

### Template 4: Advanced Rolling Hash — LC 1044
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

### Template 5: Suffix Array Construction — LC 1044
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

### Template 6: DFA / State Machine (String Validation) — LC 65 ⭐⭐⭐⭐

> **Key Idea**: when the rules of a string format are a tangle of "this is allowed only after that",
> stop writing nested `if`s and write the **transition table** instead. Classify each char into a small
> set of **character classes**, then move a single `state` integer through a hand-built DFA.
> One pass, O(1) memory, and every rule lives in one readable table.
>
> **When to reach for it**: format validation / tokenizing (LC 65 Valid Number), where an ad-hoc
> `if/else` solve is where most candidates lose the interview on edge cases (`"."`, `"4e+"`, `"3."`, `".9"`).
> This is the same automaton idea that underlies KMP (the LPS array *is* a matching automaton) —
> here the automaton is written by hand instead of derived from a pattern.

**Recipe**
1. Enumerate character classes (here: `digit`, `sign`, `dot`, `exp`) — anything else is an instant reject.
2. Enumerate states, one per "what have I legally seen so far".
3. Fill the table; `-1` = dead state.
4. Mark the **accepting** states; a string is valid iff it ends in one.

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

**Why states 2/4/7 and nothing else** — the three ways a valid number can end:

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

**Interview tip**: draw the state diagram on the whiteboard *first*, then transcribe it. The table
makes the solution self-reviewing — an interviewer can check each rule by reading one cell.

## Problems by Pattern

### **Pattern Matching Problems**
| Problem | LC # | Best Algorithm | Time Complexity | Difficulty |
|---------|------|----------------|-----------------|------------|
| Find Index of First Occurrence | 28 | KMP | O(n + m) | Medium |
| Repeated Substring Pattern | 459 | KMP/Z-Algorithm | O(n) | Easy |
| Shortest Palindrome | 214 | KMP + Reverse | O(n) | Hard |

### **Palindrome Problems**
| Problem | LC # | Best Algorithm | Time Complexity | Difficulty |
|---------|------|----------------|-----------------|------------|
| Longest Palindromic Substring | 5 | Manacher's | O(n) | Medium |
| Palindromic Substrings | 647 | Manacher's | O(n) | Medium |
| Shortest Palindrome | 214 | Manacher's/KMP | O(n) | Hard |

### **Advanced String Problems**
| Problem | LC # | Best Algorithm | Time Complexity | Difficulty |
|---------|------|----------------|-----------------|------------|
| Longest Duplicate Substring | 1044 | Rolling Hash + Binary Search | O(n log n) | Hard |
| Distinct Echo Substrings | 1316 | Rolling Hash | O(n²) | Hard |
| Find All Anagrams | 438 | Rolling Hash | O(n) | Medium |

## LC Examples

### 2-1) Find Index of First Occurrence (LC 28) — KMP
> Build LPS (failure) array for pattern; skip redundant comparisons on mismatch.

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

#### Variation: LC 686 Repeated String Match — *the twist is bounding the haystack, not the search*

> Same KMP search as LC 28; the only new idea is proving how long the haystack has to get.
> If `b` fits inside repeated `a` at all, it fits inside `ceil(|b|/|a|)` copies (enough length) or
> `ceil(|b|/|a|) + 1` copies (one extra copy to cover a match that starts mid-copy). Beyond that,
> a further copy adds no new alignment — so **two candidates, then `-1`**.
> (`string.md` shows the plain concatenation solve; this is the O(N+M) matching version.)

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

**Common mistake**: looping `while len(a*k) < len(b) + 2*len(a)` style guesses, or repeating until some
arbitrary cap. State the bound explicitly — the interviewer is testing that argument, not the search.

### 2-2) Longest Palindromic Substring (LC 5) — Expand Around Center
> Try every center (odd and even length); expand while characters match.

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

### 2-3) Longest Duplicate Substring (LC 1044) — Binary Search + Rolling Hash
> Binary search on length; use Rabin-Karp rolling hash to check if duplicate of that length exists.

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

#### Variation: LC 718 Maximum Length of Repeated Subarray — *same binary-search + hash, but across **two** sequences and over ints, not chars*

> LC 1044 hunts a duplicate inside one string; LC 718 hunts a common block across two arrays.
> The monotonic predicate is identical ("if a common block of length `L` exists, one of length `L-1`
> does too"), so binary-search `L` and hash every window of both arrays: hash all windows of `nums1`
> into a set, then stream `nums2` and look for a hit. Values are ints, so feed them straight into the
> rolling hash — no `ord()` needed.
>
> The expected interview answer here is the O(N·M) LCS-style DP (see `dp_pattern.md`). Bring this up
> as the follow-up when the interviewer pushes past O(N·M) — it is O((N+M)·log min(N,M)).

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

**Two traps**: (1) use the **upper mid** `(lo + hi + 1) // 2` with the `lo = mid` / `hi = mid - 1`
update, otherwise the search never terminates; (2) a single 32-bit-ish modulus invites collisions —
say out loud that you would verify a hash hit by comparing the actual windows, or switch to the
`DoubleHash` class below.

## Advanced Techniques

### Multiple Pattern Matching
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

### String Hashing Optimizations
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

## Performance Optimization Tips

### Algorithm Selection Guide
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

### Is the Advanced Algorithm Actually Worth It? ⭐⭐⭐⭐⭐

Interviewers rarely *want* Manacher or a suffix array. They want the straightforward solution,
correct and clean, plus the sentence "and here is how I would beat it if the constraints demanded".
Lead with the expected answer; name the advanced one as the follow-up.

| Problem | What the interviewer expects | Advanced option | Worth switching? |
|---------|------------------------------|-----------------|------------------|
| LC 28 Find First Occurrence | Sliding compare, O(N·M) | **KMP**, O(N+M) | **Yes** — this problem *is* the KMP question; the naive solve reads as "didn't know it" |
| LC 5 Longest Palindromic Substring | Expand around center, O(N²) | **Manacher**, O(N) | **No** — code Manacher only if asked for O(N). Center-expansion is the accepted answer (see `palindrome.md`) |
| LC 647 Palindromic Substrings | Expand around center, O(N²) | Manacher, O(N) | **No** — same reasoning |
| LC 686 Repeated String Match | `b in a*k` with the bound argument | KMP search | **Rarely** — the *bound proof* is the point, not the matcher |
| LC 718 Max Length of Repeated Subarray | LCS-style DP, O(N·M) | Binary search + rolling hash, O((N+M)·log N) | **Only on follow-up** — DP first, then offer this |
| LC 1044 Longest Duplicate Substring | (no easy answer — O(N²) TLEs) | **Binary search + Rabin-Karp** | **Yes** — the advanced solve is the only passing one |
| LC 1316 Distinct Echo Substrings | — | Rolling hash | **Yes** — hashing is the intended tool |
| LC 214 Shortest Palindrome | Reverse + prefix check, O(N²) | KMP prefix function on `s + '#' + rev(s)` | **Yes if asked for O(N)** — see `palindrome.md` |
| LC 10 / LC 44 Regex & Wildcard Matching | **2D DP** — no string automaton needed | Building an NFA/DFA | **No** — DP is the answer; see `dp_pattern.md`, `recursion_to_dp.md` |
| LC 65 Valid Number | Ad-hoc flag juggling | **DFA table** (Template 6) | **Yes** — the table is *shorter* and provably covers the edge cases |

**Rule of thumb**: reach for an advanced structure when (a) the naive bound actually TLEs against the
stated constraints, (b) the same string is queried many times so preprocessing amortizes, or (c) the
advanced version is genuinely *simpler* to get right (LC 65's DFA, LC 28's KMP). Otherwise it is a
liability — more code, more bugs, less time to talk.

### Where the Neighbouring Patterns Live

These belong to sibling cheatsheets — cross-referenced here so the advanced-string page stays about
suffix structures, automata, hashing, and linear-time matching:

- **LC 336 Palindrome Pairs** — reversed-word trie / hashmap split → `trie.md`, `palindrome.md`
- **LC 208 / 211 / 212 / 472 / 648** (Trie, Add & Search Words, Word Search II, Concatenated Words, Replace Words) → `trie.md`
- **LC 10 / 44** (Regular Expression & Wildcard Matching) — two-string boolean DP grid → `dp_pattern.md`, `recursion_to_dp.md`
- **LC 3 / 438** (Longest Substring Without Repeating Characters, Find All Anagrams) — window + counts, no hashing needed → `sliding_window.md`, `string.md`
- **LC 8 / 12 / 13 / 43 / 68 / 273 / 443** (parsing, formatting, big-number strings) — ordinary string manipulation → `string.md`
- **LC 20 / 224 / 227 / 394 / 1249** (parenthesis & expression parsing) — stack machines, not string algorithms → `stack.md`, `string.md`

## Summary & Quick Reference

### Algorithm Complexity Comparison

| Algorithm | Time Complexity | Space Complexity | Best Use Case |
|-----------|-----------------|------------------|---------------|
| KMP | O(n + m) | O(m) | Single pattern search |
| Manacher's | O(n) | O(n) | All palindromes |
| Z-Algorithm | O(n) | O(n) | Pattern matching variants |
| Rolling Hash | O(n) | O(1) amortized | Multiple queries |
| Suffix Array | O(n log n) | O(n) | Complex string operations |

### Common Mistakes & Tips

**🚫 Common Mistakes:**
- Not handling empty strings or patterns
- Off-by-one errors in index calculations
- Hash collision issues with single hash
- Incorrect failure function computation

**✅ Best Practices:**
- Always validate input strings
- Use double hashing for collision resistance
- Preprocess strings when multiple queries expected
- Choose algorithm based on problem constraints
- Test with edge cases and long strings

### Interview Tips
1. **Identify the core requirement**: Pattern matching, palindromes, or queries
2. **Choose optimal algorithm**: Based on time/space constraints
3. **Handle edge cases**: Empty strings, single characters
4. **Consider preprocessing**: For multiple queries
5. **Implement carefully**: Index management is critical
6. **Test thoroughly**: With various string lengths and patterns

This comprehensive advanced string algorithms cheatsheet covers the most sophisticated techniques for optimal string processing and pattern matching.