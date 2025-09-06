---
layout: cheatsheet
title: "String Algorithms"
description: "Techniques for processing, searching, and manipulating text data"
category: "Algorithm"
difficulty: "Medium"
tags: ["string", "text-processing", "pattern-matching", "manipulation"]
patterns:
  - Pattern matching
  - String transformation
  - Text parsing
---

# String Algorithms & Manipulation

## Overview
**String algorithms** encompass techniques for processing, searching, and manipulating text data. These are fundamental in text processing, pattern matching, parsing, and many coding interview problems.

### Key Properties
- **Immutability**: Strings are immutable in many languages (Python, Java)
- **Time Complexity**: Often O(n) for traversal, O(n²) for naive comparisons
- **Space Complexity**: O(n) for most transformations
- **Core Techniques**: Two pointers, sliding window, hashing, pattern matching
- **When to Use**: Text processing, pattern matching, parsing, validation

### Common Operations
- **Searching**: Finding substrings, pattern matching
- **Manipulation**: Reverse, rotate, transform
- **Validation**: Palindromes, anagrams, valid formats
- **Parsing**: Split, tokenize, extract
- **Comparison**: Lexicographic ordering, edit distance  

## Problem Categories

### **Pattern 1: Two Pointers**
- **Description**: Process string from both ends or with fast/slow pointers
- **Examples**: LC 125, 344, 345, 680, 917
- **Pattern**: Start/end pointers meeting in middle

### **Pattern 2: Sliding Window**
- **Description**: Find substring with specific properties
- **Examples**: LC 3, 76, 159, 340, 424, 567
- **Pattern**: Expand window, contract when condition met

### **Pattern 3: String Matching**
- **Description**: Find pattern in text (KMP, Rabin-Karp)
- **Examples**: LC 28, 214, 459, 686, 796
- **Pattern**: Pattern preprocessing or rolling hash

### **Pattern 4: Palindrome**
- **Description**: Check or find palindromic substrings
- **Examples**: LC 5, 125, 131, 409, 516, 647
- **Pattern**: Expand from center or DP

### **Pattern 5: String Transformation**
- **Description**: Convert between string formats
- **Examples**: LC 6, 8, 12, 13, 38, 443
- **Pattern**: Parse and rebuild with rules

### **Pattern 6: String DP**
- **Description**: Dynamic programming on strings
- **Examples**: LC 10, 44, 72, 115, 583, 1143
- **Pattern**: 2D DP table for string comparison

## Templates & Algorithms

### Template Comparison Table
| Template Type | Use Case | Complexity | When to Use |
|---------------|----------|------------|-------------|
| **Two Pointers** | Palindrome, reverse | O(n) | Both ends processing |
| **Sliding Window** | Substring problems | O(n) | Continuous subarray |
| **KMP** | Pattern matching | O(n+m) | Exact pattern search |
| **Rolling Hash** | Pattern/duplicate | O(n) | Multiple pattern search |
| **Trie** | Prefix matching | O(m) | Multiple string search |
| **DP** | Edit distance | O(n²) | String comparison |

### Template 1: Two Pointers Pattern
```python
# Python - Two pointers for palindrome
def isPalindrome(s):
    left, right = 0, len(s) - 1
    
    while left < right:
        # Skip non-alphanumeric
        while left < right and not s[left].isalnum():
            left += 1
        while left < right and not s[right].isalnum():
            right -= 1
        
        if s[left].lower() != s[right].lower():
            return False
        
        left += 1
        right -= 1
    
    return True

# Reverse string/array in-place
def reverseString(s):
    left, right = 0, len(s) - 1
    
    while left < right:
        s[left], s[right] = s[right], s[left]
        left += 1
        right -= 1
    
    return s

# Valid palindrome with one deletion
def validPalindrome(s):
    def checkPalindrome(s, left, right):
        while left < right:
            if s[left] != s[right]:
                return False
            left += 1
            right -= 1
        return True
    
    left, right = 0, len(s) - 1
    
    while left < right:
        if s[left] != s[right]:
            # Try deleting either character
            return (checkPalindrome(s, left + 1, right) or 
                   checkPalindrome(s, left, right - 1))
        left += 1
        right -= 1
    
    return True
```

```java
// Java - Two pointers
public boolean isPalindrome(String s) {
    int left = 0, right = s.length() - 1;
    
    while (left < right) {
        while (left < right && !Character.isLetterOrDigit(s.charAt(left))) {
            left++;
        }
        while (left < right && !Character.isLetterOrDigit(s.charAt(right))) {
            right--;
        }
        
        if (Character.toLowerCase(s.charAt(left)) != 
            Character.toLowerCase(s.charAt(right))) {
            return false;
        }
        
        left++;
        right--;
    }
    
    return true;
}
```

### Template 2: Sliding Window Pattern
```python
# Python - Variable size sliding window
def longestSubstringKDistinct(s, k):
    if k == 0:
        return 0
    
    window = {}
    left = 0
    max_len = 0
    
    for right in range(len(s)):
        # Expand window
        window[s[right]] = window.get(s[right], 0) + 1
        
        # Contract window if needed
        while len(window) > k:
            window[s[left]] -= 1
            if window[s[left]] == 0:
                del window[s[left]]
            left += 1
        
        max_len = max(max_len, right - left + 1)
    
    return max_len

# Minimum window substring
def minWindow(s, t):
    from collections import Counter
    
    need = Counter(t)
    window = {}
    
    left = 0
    min_len = float('inf')
    min_start = 0
    formed = 0
    required = len(need)
    
    for right in range(len(s)):
        char = s[right]
        window[char] = window.get(char, 0) + 1
        
        if char in need and window[char] == need[char]:
            formed += 1
        
        while formed == required and left <= right:
            # Update result
            if right - left + 1 < min_len:
                min_len = right - left + 1
                min_start = left
            
            # Contract window
            char = s[left]
            window[char] -= 1
            if char in need and window[char] < need[char]:
                formed -= 1
            left += 1
    
    return s[min_start:min_start + min_len] if min_len != float('inf') else ""
```

```java
// Java - Sliding window
public int lengthOfLongestSubstringKDistinct(String s, int k) {
    if (k == 0) return 0;
    
    Map<Character, Integer> window = new HashMap<>();
    int left = 0;
    int maxLen = 0;
    
    for (int right = 0; right < s.length(); right++) {
        char c = s.charAt(right);
        window.put(c, window.getOrDefault(c, 0) + 1);
        
        while (window.size() > k) {
            char leftChar = s.charAt(left);
            window.put(leftChar, window.get(leftChar) - 1);
            if (window.get(leftChar) == 0) {
                window.remove(leftChar);
            }
            left++;
        }
        
        maxLen = Math.max(maxLen, right - left + 1);
    }
    
    return maxLen;
}
```

### Template 3: String Pattern Matching (KMP)
```python
# Python - KMP pattern matching
def KMP(text, pattern):
    if not pattern:
        return 0
    
    # Build LPS array
    def buildLPS(pattern):
        lps = [0] * len(pattern)
        length = 0
        i = 1
        
        while i < len(pattern):
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
    
    lps = buildLPS(pattern)
    i = j = 0
    
    while i < len(text):
        if text[i] == pattern[j]:
            i += 1
            j += 1
        
        if j == len(pattern):
            return i - j  # Pattern found
        elif i < len(text) and text[i] != pattern[j]:
            if j != 0:
                j = lps[j - 1]
            else:
                i += 1
    
    return -1  # Pattern not found

# Rolling hash (Rabin-Karp)
def rabinKarp(text, pattern):
    if len(pattern) > len(text):
        return -1
    
    base = 256
    prime = 101
    pattern_hash = 0
    text_hash = 0
    h = 1
    
    # Calculate h = base^(m-1) % prime
    for i in range(len(pattern) - 1):
        h = (h * base) % prime
    
    # Calculate initial hashes
    for i in range(len(pattern)):
        pattern_hash = (base * pattern_hash + ord(pattern[i])) % prime
        text_hash = (base * text_hash + ord(text[i])) % prime
    
    # Slide pattern over text
    for i in range(len(text) - len(pattern) + 1):
        if pattern_hash == text_hash:
            # Check characters one by one
            if text[i:i + len(pattern)] == pattern:
                return i
        
        # Calculate hash for next window
        if i < len(text) - len(pattern):
            text_hash = (base * (text_hash - ord(text[i]) * h) + 
                        ord(text[i + len(pattern)])) % prime
            if text_hash < 0:
                text_hash += prime
    
    return -1
```

### Template 4: Palindrome Patterns
```python
# Python - Expand from center
def longestPalindrome(s):
    if not s:
        return ""
    
    def expandFromCenter(left, right):
        while left >= 0 and right < len(s) and s[left] == s[right]:
            left -= 1
            right += 1
        return right - left - 1
    
    start = 0
    max_len = 0
    
    for i in range(len(s)):
        # Odd length palindrome
        len1 = expandFromCenter(i, i)
        # Even length palindrome
        len2 = expandFromCenter(i, i + 1)
        
        curr_len = max(len1, len2)
        if curr_len > max_len:
            max_len = curr_len
            start = i - (curr_len - 1) // 2
    
    return s[start:start + max_len]

# Count palindromic substrings
def countSubstrings(s):
    count = 0
    
    def expandFromCenter(left, right):
        nonlocal count
        while left >= 0 and right < len(s) and s[left] == s[right]:
            count += 1
            left -= 1
            right += 1
    
    for i in range(len(s)):
        expandFromCenter(i, i)      # Odd length
        expandFromCenter(i, i + 1)  # Even length
    
    return count
```

### Template 5: String Transformation
```python
# Python - String to integer (atoi)
def myAtoi(s):
    s = s.strip()
    if not s:
        return 0
    
    sign = 1
    idx = 0
    
    if s[0] in ['+', '-']:
        sign = -1 if s[0] == '-' else 1
        idx = 1
    
    num = 0
    while idx < len(s) and s[idx].isdigit():
        num = num * 10 + int(s[idx])
        idx += 1
    
    num *= sign
    
    # Handle overflow
    INT_MAX = 2**31 - 1
    INT_MIN = -2**31
    
    if num > INT_MAX:
        return INT_MAX
    if num < INT_MIN:
        return INT_MIN
    
    return num

# Integer to Roman
def intToRoman(num):
    values = [1000, 900, 500, 400, 100, 90, 50, 40, 10, 9, 5, 4, 1]
    symbols = ["M", "CM", "D", "CD", "C", "XC", "L", "XL", "X", "IX", "V", "IV", "I"]
    
    result = []
    for i, val in enumerate(values):
        count = num // val
        if count:
            result.append(symbols[i] * count)
            num -= val * count
    
    return ''.join(result)
```

### Template 6: String DP
```python
# Python - Edit distance
def minDistance(word1, word2):
    m, n = len(word1), len(word2)
    dp = [[0] * (n + 1) for _ in range(m + 1)]
    
    # Initialize base cases
    for i in range(m + 1):
        dp[i][0] = i
    for j in range(n + 1):
        dp[0][j] = j
    
    # Fill DP table
    for i in range(1, m + 1):
        for j in range(1, n + 1):
            if word1[i-1] == word2[j-1]:
                dp[i][j] = dp[i-1][j-1]
            else:
                dp[i][j] = 1 + min(
                    dp[i-1][j],    # Delete
                    dp[i][j-1],    # Insert
                    dp[i-1][j-1]   # Replace
                )
    
    return dp[m][n]

# Longest common subsequence
def longestCommonSubsequence(text1, text2):
    m, n = len(text1), len(text2)
    dp = [[0] * (n + 1) for _ in range(m + 1)]
    
    for i in range(1, m + 1):
        for j in range(1, n + 1):
            if text1[i-1] == text2[j-1]:
                dp[i][j] = dp[i-1][j-1] + 1
            else:
                dp[i][j] = max(dp[i-1][j], dp[i][j-1])
    
    return dp[m][n]
```

## Basic String Operations
### Python String Operations
```python
# String <-> List conversion
s = "abcd"
char_list = list(s)           # ['a', 'b', 'c', 'd']
back_to_string = ''.join(char_list)  # "abcd"

# Join with separator
words = ["hello", "world"]
sentence = " ".join(words)    # "hello world"
csv = ",".join(words)         # "hello,world"

# Reverse iteration
s = "abcd"
for i in range(len(s)-1, -1, -1):
    print(s[i])  # d, c, b, a

# String slicing
s = "abcdef"
reversed_s = s[::-1]         # "fedcba"
every_other = s[::2]          # "ace"
substring = s[1:4]            # "bcd"

# Common string methods
s = "  Hello World  "
s.strip()                     # "Hello World"
s.lower()                     # "  hello world  "
s.upper()                     # "  HELLO WORLD  "
s.replace("World", "Python")  # "  Hello Python  "
s.split()                     # ['Hello', 'World']

# Character operations
char = 'a'
ord_val = ord(char)           # 97
back_to_char = chr(97)        # 'a'
is_alpha = char.isalpha()     # True
is_digit = '5'.isdigit()      # True
```

### Java String Operations
```java
// String operations in Java
String s = "abcd";

// String to char array
char[] chars = s.toCharArray();
String backToString = new String(chars);

// StringBuilder for mutable strings
StringBuilder sb = new StringBuilder();
sb.append("Hello");
sb.append(" World");
sb.reverse();
String result = sb.toString();

// String methods
String str = "  Hello World  ";
str.trim()                    // "Hello World"
str.toLowerCase()             // "  hello world  "
str.toUpperCase()             // "  HELLO WORLD  "
str.replace("World", "Java")  // "  Hello Java  "
str.substring(2, 7)           // "Hello"
String[] words = str.split(" ");

// Character operations
char c = 'a';
int ascii = (int) c;          // 97
char backToChar = (char) 97;  // 'a'
boolean isLetter = Character.isLetter(c);
boolean isDigit = Character.isDigit('5');
```

### String Manipulation Tricks
```python
# go through elements in str AVOID index out of range error
x = '1234'

for i in range(len(x)):
    if  i == len(x)-1 or x[i] != x[i+1]:
        print (x[i])
```

```python
# string -> array

a = 1234
a_array = list(str(a))

In [12]: a_array
Out[12]: ['1', '2', '3', '4']
```

```java
// java
// split string (java)
/** NOTE !!! split string via .split("") */

 for (String x : s.split("")){
    System.out.println(x);
 }
```


#### 1-8) Group sub-string
```python
# LC 696. Count Binary Substrings
# ...
groups = [1]
for i in range(1, len(s)):
    """
    NOTE here !!!
    """
    if s[i-1] != s[i]:
        groups.append(1)
    else:
        groups[-1] += 1
ans = 0
for i in range(1, len(groups)):
    """
    NOTE here !!!
    """
    ans += mins(groups[i-1], groups[i])
# ...
```

#### 1-9) Rotate string
```python
# LC 796. Rotate String
class Solution(object):
    def rotateString(self, A, B):
        for i in range(len(A)):
            if A[i:] + A[:i] == B:
                return True
        return False
```

## Problems by Pattern

### Pattern-Based Problem Tables

#### **Two Pointers Problems**
| Problem | LC # | Key Technique | Difficulty |
|---------|------|---------------|------------|
| Valid Palindrome | 125 | Skip non-alphanumeric | Easy |
| Reverse String | 344 | Swap in-place | Easy |
| Reverse Vowels | 345 | Selective swap | Easy |
| Valid Palindrome II | 680 | One deletion allowed | Easy |
| Sort Characters | 917 | Skip special chars | Easy |
| Long Pressed Name | 925 | Character matching | Easy |
| Compare Version | 165 | Split and compare | Medium |

#### **Sliding Window Problems**
| Problem | LC # | Key Technique | Difficulty |
|---------|------|---------------|------------|
| Longest Substring Without Repeating | 3 | Variable window | Medium |
| Minimum Window Substring | 76 | Two pointers + hash | Hard |
| Longest Substring with Two Distinct | 159 | K distinct chars | Medium |
| Longest Substring with K Distinct | 340 | HashMap window | Hard |
| Max Consecutive Ones III | 1004 | At most K flips | Medium |
| Character Replacement | 424 | Character replacement | Medium |
| Permutation in String | 567 | Fixed window | Medium |
| Find All Anagrams | 438 | Fixed size window | Medium |

#### **Pattern Matching Problems**
| Problem | LC # | Key Technique | Difficulty |
|---------|------|---------------|------------|
| Implement strStr() | 28 | KMP/Rabin-Karp | Easy |
| Shortest Palindrome | 214 | KMP application | Hard |
| Repeated Substring Pattern | 459 | Pattern in s+s | Easy |
| Repeated String Match | 686 | Multiple concatenation | Medium |
| Rotate String | 796 | Check in A+A | Easy |
| Find and Replace Pattern | 890 | Pattern mapping | Medium |

#### **Palindrome Problems**
| Problem | LC # | Key Technique | Difficulty |
|---------|------|---------------|------------|
| Longest Palindromic Substring | 5 | Expand from center | Medium |
| Palindrome Partitioning | 131 | Backtracking | Medium |
| Longest Palindrome | 409 | Character counting | Easy |
| Palindromic Substrings | 647 | Expand from center | Medium |
| Longest Palindromic Subsequence | 516 | DP | Medium |
| Valid Palindrome III | 1216 | K deletions | Hard |

#### **String Transformation Problems**
| Problem | LC # | Key Technique | Difficulty |
|---------|------|---------------|------------|
| ZigZag Conversion | 6 | Pattern simulation | Medium |
| String to Integer (atoi) | 8 | Parse with rules | Medium |
| Integer to Roman | 12 | Greedy conversion | Medium |
| Roman to Integer | 13 | Mapping | Easy |
| Count and Say | 38 | Iterative generation | Medium |
| String Compression | 443 | In-place modification | Medium |
| Decode String | 394 | Stack | Medium |
| License Key Formatting | 482 | String building | Easy |
| Validate IP Address | 468 | Parse validation | Medium |

#### **String DP Problems**
| Problem | LC # | Key Technique | Difficulty |
|---------|------|---------------|------------|
| Regular Expression Matching | 10 | 2D DP | Hard |
| Wildcard Matching | 44 | 2D DP | Hard |
| Edit Distance | 72 | Classic DP | Hard |
| Distinct Subsequences | 115 | Count subsequences | Hard |
| Delete Operations | 583 | LCS variation | Medium |
| Longest Common Subsequence | 1143 | Classic DP | Medium |
| Interleaving String | 97 | 2D DP | Hard |

## Pattern Selection Strategy

```
Problem Analysis Flowchart:

1. Processing from both ends?
   ├── YES → Two Pointers
   │         ├── Palindrome check
   │         └── Reverse operations
   └── NO → Continue to 2

2. Finding substring with property?
   ├── YES → Sliding Window
   │         ├── Variable size → Expand/contract
   │         └── Fixed size → Slide window
   └── NO → Continue to 3

3. Pattern matching needed?
   ├── YES → String Matching
   │         ├── Single pattern → KMP
   │         └── Multiple patterns → Trie/Hash
   └── NO → Continue to 4

4. Palindrome related?
   ├── YES → Palindrome Techniques
   │         ├── All palindromes → Expand center
   │         └── Longest → DP or Manacher
   └── NO → Continue to 5

5. Format conversion?
   ├── YES → String Transformation
   │         ├── Parse → State machine
   │         └── Generate → Build rules
   └── NO → Continue to 6

6. Compare/align strings?
   ├── YES → Dynamic Programming
   │         ├── Edit operations → Edit distance
   │         └── Subsequence → LCS variations
   └── NO → Use appropriate combination
```

## Summary & Quick Reference

### Complexity Quick Reference
| Operation | Time | Space | Notes |
|-----------|------|-------|-------|
| Two Pointers | O(n) | O(1) | Single pass |
| Sliding Window | O(n) | O(k) | k = window elements |
| KMP Search | O(n+m) | O(m) | m = pattern length |
| Rabin-Karp | O(n) avg | O(1) | Hash collisions |
| Expand Center | O(n²) | O(1) | All palindromes |
| Edit Distance | O(mn) | O(mn) | Can optimize to O(n) |
| Trie Operations | O(m) | O(ALPHABET_SIZE * m) | m = word length |

### Template Quick Reference
| Template | Pattern | Key Code |
|----------|---------|----------|
| **Two Pointers** | Start/end meet | `while left < right` |
| **Sliding Window** | Expand/contract | `right++; while(invalid) left++` |
| **KMP** | Failure function | `lps[i] = longest prefix suffix` |
| **Palindrome** | Expand center | `expand(i,i); expand(i,i+1)` |
| **String DP** | 2D table | `dp[i][j] = relation` |
| **Rolling Hash** | Hash window | `hash = (hash * base + char) % mod` |

### Common Patterns & Tricks

#### **String Building Performance**
```python
# Python: Use list and join
result = []
for item in items:
    result.append(process(item))
return ''.join(result)
```

```java
// Java: Use StringBuilder
StringBuilder sb = new StringBuilder();
for (String item : items) {
    sb.append(process(item));
}
return sb.toString();
```

#### **Palindrome Optimization**
```python
# Check palindrome efficiently
def isPalindrome(s):
    return s == s[::-1]

# Expand from all centers
for i in range(len(s)):
    expandAroundCenter(i, i)      # Odd length
    expandAroundCenter(i, i + 1)  # Even length
```

### Problem-Solving Steps

1. **Identify Pattern Type**
   - Character-by-character?
   - Substring properties?
   - Pattern matching?
   - Transformation rules?

2. **Choose Approach**
   - In-place possible?
   - Need state tracking?
   - Multiple passes?

3. **Handle Edge Cases**
   - Empty string
   - Single character
   - All same characters
   - Special characters

### Common Mistakes & Tips

**🚫 Common Mistakes:**
- String concatenation in loop (O(n²))
- Off-by-one errors in substring
- Not handling empty strings
- Modifying immutable strings
- Character encoding issues

**✅ Best Practices:**
- Use StringBuilder/list+join
- Clarify character set (ASCII/Unicode)
- Consider case sensitivity
- Test with special characters
- Handle overflow in conversions

### Interview Tips

1. **Clarify Requirements**
   - Character set?
   - Case sensitive?
   - In-place allowed?
   - Handle special chars?

2. **Start Simple**
   - Brute force first
   - Optimize incrementally
   - Explain trade-offs

3. **Common Follow-ups**
   - Handle Unicode
   - Optimize space
   - Stream processing
   - Parallel processing

### Advanced Techniques

#### **Suffix Array/Tree**
- Multiple pattern search
- O(n log n) construction
- O(m + log n) search

#### **Manacher's Algorithm**
- All palindromes in O(n)
- Complex but optimal

#### **Z-Algorithm**
- Pattern matching O(n)
- Similar to KMP

### Related Topics
- **Arrays**: Two-pointer techniques
- **Hash Tables**: Pattern counting
- **Dynamic Programming**: String alignment
- **Tries**: Prefix matching
- **Regular Expressions**: Pattern matching

## 2) LC Example

### 2-1) Compare Version Number
- go through 2 string, keep comparing digits in eash string
```python
# 165 Compare Version Number
# V0
# IDEA : STRING + while op
class Solution(object):
    def compareVersion(self, version1, version2):
        # edge case
        if not version1 and not version2:
            return
        # split by "." as list
        v_1 = version1.split(".")
        v_2 = version2.split(".")
        # compare
        while v_1 and v_2:
            tmp1 = int(v_1.pop(0))
            tmp2 = int(v_2.pop(0))

            if tmp1 > tmp2:
                return 1
            elif tmp1 < tmp2:
                return -1
        # if v_1 remains
        if v_1:
            while v_1:
                tmp1 = int(v_1.pop(0))
                if tmp1 != 0:
                    return 1
        # if v_2 remains
        if v_2:
            while v_2:
                tmp2 = int(v_2.pop(0))
                if tmp2 != 0:
                    return -1
        return 0

# V0'
# IDEA : STRING
class Solution(object):
    def compareVersion(self, version1, version2):
        v1_split = version1.split('.')
        v2_split = version2.split('.')
        v1_len, v2_len = len(v1_split), len(v2_split)
        maxLen = max(v1_len, v2_len)
        for i in range(maxLen):
            temp1, temp2 = 0, 0
            if i < v1_len:
                temp1 = int(v1_split[i])
            if i < v2_len:
                temp2 = int(v2_split[i])
            if temp1 < temp2:
                return -1
            elif temp1 > temp2:
                return 1
        return 0
```

### 2-2) Add Two Numbers II,  Decode String
- String -> Int
```python
# 445 Add Two Numbers II
# 394 Decode String
def str_2_int(x):
    r=0
    for i in x:
        r = int(r)*10 + int(i)
        print (i, r)
    return r

def str_2_int_v2(x):
    res = 0
    for i in x:
        res = (res + int(i) % 10) * 10
    return int(res / 10)

# example 1
x="131"
r=str_2_int(x)
print (r)
# 1 1
# 3 13
# 1 131
# 131

# examle 2
In [62]: z
Out[62]: '5634'

In [63]: ans = 0

In [64]: for i in z:
    ...:     ans = 10 * ans + int(i)
    ...:

In [65]: ans
Out[65]: 5634
```

### 2-3) Count and say
```python
# LC 038 Count and say
# V0
# IDEA : ITERATION
class Solution:
    def countAndSay(self, n):
        
        val = ""
        res = "1"
        
        for _ in range(n-1):
            cnt = 1
            for j in range(len(res)-1):
                if res[j]==res[j+1]:
                    cnt+=1
                else:
                    val += str(cnt) + res[j]
                    cnt = 1
            val += str(cnt)+res[-1]
            res = val
            val = ""
        return res
```

### 2-4) Monotone Increasing Digits
```python
# LC 738 Monotone Increasing Digits
class Solution:
    def monotoneIncreasingDigits(self, N):
        s = list(str(N));
        ### NOTICE HERE 
        for i in range(len(s) - 2,-1,-1):
            # if int(s[i]) > int(s[i+1]) -> the string is not `monotone increase`
            # -> we need to find the next biggest int, 
            # -> so we need to make all right hand side digit as '9'
            # -> and minus current digit with 1  (s[i] = str(int(s[i]) - 1))
            if int(s[i]) > int(s[i+1]):
                ### NOTICE HERE 
                for j in range(i+1,len(s)):
                    s[j] = '9'
                s[i] = str(int(s[i]) - 1)
        s = "".join(s)        
        return int(s) 
```

### 2-5) Validate IP Address
```python
# LC 468. Validate IP Address
# V0
# IDEA : Divide and Conquer
class Solution:
    def validate_IPv4(self, IP):
        nums = IP.split('.')
        for x in nums:
            # Validate integer in range (0, 255):
            # 1. length of chunk is between 1 and 3
            if len(x) == 0 or len(x) > 3:
                return "Neither"
            # 2. no extra leading zeros
            # 3. only digits are allowed
            # 4. less than 255
            if x[0] == '0' and len(x) != 1 or not x.isdigit() or int(x) > 255:
                return "Neither"
        return "IPv4"
    
    def validate_IPv6(self, IP):
        nums = IP.split(':')
        hexdigits = '0123456789abcdefABCDEF'
        for x in nums:
            # Validate hexadecimal in range (0, 2**16):
            # 1. at least one and not more than 4 hexdigits in one chunk
            # 2. only hexdigits are allowed: 0-9, a-f, A-F
            if len(x) == 0 or len(x) > 4 or not all(c in hexdigits for c in x):
                return "Neither"
        return "IPv6"
        
    def validIPAddress(self, IP):
        if IP.count('.') == 3:
            return self.validate_IPv4(IP)
        elif IP.count(':') == 7:
            return self.validate_IPv6(IP)
        else:
            return "Neither"
```

### 2-6) String to Integer (atoi)
```python
# LC 008
# V0'
# IDEA : string op
class Solution(object):
    def myAtoi(self, _str):
        _str = _str.strip()
        number = 0
        flag = 1
        print ("_str = " + str(_str))
        if not _str:
            return 0
        if _str[0] == '-':
            _str = _str[1:]
            flag = -1
        elif _str[0] == '+':
            _str = _str[1:]
        for c in _str:
            #if c >= '0' and c <= '9':  # '3' > '2' -> True
            if c in [str(x) for x in range(10)]:
                """
                str(int) -> ord demo

                Example 1 :
                In [55]: for i in range(10):
                        ...: print (str(i) + " ord = " + str(ord(str(i))))
                        ...:
                                0 ord = 48
                                1 ord = 49
                                2 ord = 50
                                3 ord = 51
                                4 ord = 52
                                5 ord = 53
                                6 ord = 54
                                7 ord = 55
                                8 ord = 56
                                9 ord = 57

                Example 2 :

                            In [62]: z
                            Out[62]: '5634'

                            In [63]: ans = 0

                            In [64]: for i in z:
                                ...:     ans = 10 * ans + int(i)
                                ...:

                            In [65]: ans
                            Out[65]: 5634
                """
                #number = 10*number + ord(c) - ord('0')  # _string to integer 
                number = 10*number + int(c)  # _string to integer , above is OK as well
            else:
                break
        res = flag * number
        res = res if res <= 2**31 - 1 else 2**31 - 1    # 2**31 == 2147483648
        res = res if res >= -1 * 2**31  else -1 * 2**31   # -(1)*(2**31) == - 2147483648
        return res
```

### 2-7) License Key Formatting
```python
# LC 482. License Key Formatting
# ref : LC 725. Split Linked List in Parts

# V0
class Solution(object):
    def licenseKeyFormatting(self, S, K):
        result = []
        for i in reversed(range(len(S))):
            if S[i] == '-':
                continue
            if len(result) % (K + 1) == K:
                result += '-'
            result += S[i].upper()
        return "".join(reversed(result))

# V0'
# IDEA : string op + brute force
class Solution(object):
    def licenseKeyFormatting(self, s, k):
        # edge case
        if not s or not k:
            return s
        s = s.replace("-", "")
        s_ = ""
        for _ in s:
            if _.isalpha():
                s_ += _.upper()
            else:
                s_ += _

        s_ = list(s_)
        #print ("s_ = " + str(s_))
        s_len = len(s)
        remain = s_len % k
        #res = []
        res = ""
        tmp = ""
        # if s_len % k != 0
        while remain != 0:
            tmp += s_.pop(0)
            remain -= 1
        #res.append(tmp)
        res += (tmp + "-")
        tmp = ""
        # if s_len % k == 0
        for i in range(0, len(s_), k):
            #print (s_[i:i+k])
            #res.append(s_[i:i+k])
            res += ("".join(s_[i:i+k]) + "-")
        return res.strip("-")
```

### 2-8) Repeated String Match
```python
# LC 686. Repeated String Match
# V0
# IDEA : BRUTE FORCE
# https://leetcode.com/problems/repeated-string-match/discuss/108090/Intuitive-Python-2-liner
# -> if there is a sufficient solution, B must "inside" A
# -> Let n be the answer, 
# -> Let x be the theoretical lower bound, which is ceil(len(B)/len(A)).
# -> the value of n can br ONLY "x" or "x + 1"
# -> e.g. : in the case where len(B) is a multiple of len(A) like in A = "abcd" and B = "cdabcdab") and not more. Because if B is already in A * n, B is definitely in A * (n + 1).
# --> So all we need to check whether are:
#       -> 1) B in A * x
#         or
#       -> 2) B in A * (x+1)
# -> return -1 if above contitions are not met
class Solution(object):
    def repeatedStringMatch(self, A, B):
        sa, sb = len(A), len(B)
        x = 1
        while (x - 1) * sa <= 2 * max(sa, sb):
            if B in A * x: 
                return x
            x += 1
        return -1

# V0'
class Solution(object):
    def repeatedStringMatch(self, a, b):
        # edge case
        if not a and b:
            return -1
        if (not a and not b) or (a == b) or (b in a):
            return 1
        res = 1
        sa = len(a)
        sb = len(b)
        #while res * sa <= 3 * max(sa, sb):  # this condition is OK as well
        while (res-1) * sa <= 2 * max(sa, sb):
            a_ = res * a
            if b in a_:
                return res
            res += 1
        return -1
```

### 2-9) Count Binary Substrings
```python
# LC 696. Count Binary Substrings
# V0 
# IDEA :  Group By Character + continous sub-string
# https://leetcode.com/problems/count-binary-substrings/solution/
# https://blog.csdn.net/fuxuemingzhu/article/details/79183556
# IDEA :
#   -> for x = “0110001111”, how many continuous "0" or "1"
#   -> [1,2,3,4]
#   -> So, if we want to find # of "equal 0 and 1 sub string"
#   -> all we need to do : min(3,4) = 3. e.g. ("01", "0011", "000111")
#   -> since for every "cross" sub string (e.g. 0 then 1 or 1 then 0),
#   -> we can the "number of same continuous 0 and 1"  by min(groups[i-1], groups[i])
class Solution(object):
    def countBinarySubstrings(self, s):
        groups = [1]
        for i in range(1, len(s)):
            if s[i-1] != s[i]:
                groups.append(1)
            else:
                groups[-1] += 1

        ans = 0
        for i in range(1, len(groups)):
            ans += min(groups[i-1], groups[i])
        return ans

# V1
# IDEA :  Group By Character
# https://leetcode.com/problems/count-binary-substrings/solution/
class Solution(object):
    def countBinarySubstrings(self, s):
        groups = [1]
        for i in range(1, len(s)):
            if s[i-1] != s[i]:
                groups.append(1)
            else:
                groups[-1] += 1

        ans = 0
        for i in range(1, len(groups)):
            ans += min(groups[i-1], groups[i])
        return ans

# V1''
# IDEA :  Linear Scan
# https://leetcode.com/problems/count-binary-substrings/solution/
class Solution(object):
    def countBinarySubstrings(self, s):
        ans, prev, cur = 0, 0, 1
        for i in range(1, len(s)):
            if s[i-1] != s[i]:
                ans += min(prev, cur)
                prev, cur = cur, 1
            else:
                cur += 1

        return ans + min(prev, cur)
```

### 2-10) Roman to Integer
```python
# LC 13. Roman to Integer
# V0
class Solution(object):
    def romanToInt(self, s):
        # helper ref
        roman = {"I":1, "V":5, "X":10, "L":50, "C":100, "D":500, "M":1000}
        # NOTE : we init res as below
        res = roman[s[-1]]
        N = len(s)
        """
        2 cases:
            case 1) XY, X > Y -> res = X - Y
            case 2) XY, X < Y -> res = X + Y
        """
        for i in range(N - 2, -1, -1):
            # case 1
            if roman[s[i]] < roman[s[i + 1]]:
                res -= roman[s[i]]
            # case 2
            else:
                res += roman[s[i]]
        return res
```

### 2-11) ount Unique Characters of All Substrings of a Given String
```python
# LC 828. Count Unique Characters of All Substrings of a Given String
# V0
class Solution(object):
     def uniqueLetterString(self, S):
            index = {c: [-1, -1] for c in 'ABCDEFGHIJKLMNOPQRSTUVWXYZ'}
            res = 0
            for i, c in enumerate(S):
                k, j = index[c]
                res += (i - j) * (j - k)
                index[c] = [j, i]
            for c in index:
                k, j = index[c]
                res += (len(S) - j) * (j - k)
            return res % (10**9 + 7)

# V1
# https://leetcode.com/problems/count-unique-characters-of-all-substrings-of-a-given-string/discuss/128952/C%2B%2BJavaPython-One-pass-O(N)
# IDEA :
# Let's think about how a character can be found as a unique character.
# Think about string "XAXAXXAX" and focus on making the second "A" a unique character.
# We can take "XA(XAXX)AX" and between "()" is our substring.
# We can see here, to make the second "A" counted as a uniq character, we need to:
# insert "(" somewhere between the first and second A
# insert ")" somewhere between the second and third A
# For step 1 we have "A(XA" and "AX(A", 2 possibility.
# For step 2 we have "A)XXA", "AX)XA" and "AXX)A", 3 possibilities.
# So there are in total 2 * 3 = 6 ways to make the second A a unique character in a substring.
# In other words, there are only 6 substring, in which this A contribute 1 point as unique string.
# Instead of counting all unique characters and struggling with all possible substrings,
# we can count for every char in S, how many ways to be found as a unique char.
# We count and sum, and it will be out answer.
class Solution(object):
     def uniqueLetterString(self, S):
            index = {c: [-1, -1] for c in string.ascii_uppercase}
            res = 0
            for i, c in enumerate(S):
                k, j = index[c]
                res += (i - j) * (j - k)
                index[c] = [j, i]
            for c in index:
                k, j = index[c]
                res += (len(S) - j) * (j - k)
            return res % (10**9 + 7)
```

### 2-12) Palindromic Substrings
```python
# LC 647. Palindromic Substrings
# V0
# IDEA : BRUTE FORCE
class Solution(object):
    def countSubstrings(self, s):
        count = 0
        # NOTE: since i from 0 to len(s) - 1, so for j we need to "+1" then can get go throgh all elements in str
        for i in range(len(s)):
            # Note : for j we need to "+1"
            for j in range(i+1, len(s)+1):
                if s[i:j] == s[i:j][::-1]:
                    count += 1
        return count
```

### 2-13) Repeated Substring Pattern
```python
# LC 459. Repeated Substring Pattern
# V0
# IDEA : # only have to go through till HALF of s's length, since it's not possbile to find the SubstringPattern if len(s[:x]) > size//2
class Solution(object):
    def repeatedSubstringPattern(self, s):
        _len_s = len(s)
        i = 0
        tmp = ""
        while i < _len_s:
            if i == 0:
                multiply = 0
            if i != 0:
                multiply = _len_s // i
            if multiply * tmp == s:
                return True
            if i > _len_s // 2:
                return False
            tmp += s[i]
            i += 1
        return False
```