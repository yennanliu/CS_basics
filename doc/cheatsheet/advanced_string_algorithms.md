# Advanced String Algorithms

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

### Template 1: KMP (Knuth-Morris-Pratt) Algorithm
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

### Template 2: Manacher's Algorithm
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

### Template 3: Z-Algorithm
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

### Template 4: Advanced Rolling Hash
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

### Template 5: Suffix Array Construction
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

### 1. Find Index of First Occurrence (LC 28)
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

### 2. Longest Palindromic Substring (LC 5)
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

### 3. Longest Duplicate Substring (LC 1044)
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