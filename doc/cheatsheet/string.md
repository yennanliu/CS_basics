# String Algorithms & Manipulation

> **Scope** — The everyday string catalogue — parsing, building, anagram/frequency comparison, in-place rewriting, and the window/pointer patterns as applied to characters.
> **See also**: [advanced_string_algorithms.md](./advanced_string_algorithms.md) — suffix structures, Z-algorithm, string DP; [string_matching_kmp_rolling_hash.md](./string_matching_kmp_rolling_hash.md) — substring search; [palindrome.md](./palindrome.md); [trie.md](./trie.md) — prefix structures.

## LeetCode Problem Lists

- [String](https://leetcode.com/problem-list/string/)

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

### **Pattern 7: Incremental Prefix Validation**
- **Description**: Validate words can be built character-by-character from prefixes
- **Examples**: LC 720
- **Pattern**: Sort words + HashSet to track buildable words + check immediate prefix
- **Key Trick**: Only need to check if `word.substring(0, word.length() - 1)` exists

### **Pattern 8: Run-Length Grouping (Consecutive Character Groups)** ⭐⭐⭐⭐
- **Description**: Compress string into **groups of consecutive identical chars**, then solve on the group-length array
- **Examples**: LC 696, 38, 443, 1446, 485, 1004, 1759
- **Pattern**: `s` → `[len(g1), len(g2), ...]` → answer computed from adjacent group lengths
- **Key Trick**: For LC 696, each **adjacent group pair** contributes `min(g[i-1], g[i])` valid substrings

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
| **Prefix Validation** | Word building validation | O(n log n) | Check all prefixes exist |
| **Run-Length Grouping** | Consecutive char groups | O(n) | Answer depends on run lengths |

### Template 1: Two Pointers Pattern
> For the generic two-pointers pattern (fast/slow, left/right on arrays), see the two-pointers cheatsheet. This section focuses on palindrome-specific two-pointer usage.

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
                text_hash = (text_hash % prime + prime) % prime  # ensure positive remainder after modulo
    
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

---

#### Template 4.1: Manacher's Algorithm (O(n) Palindrome Detection)

**Core Concept:**
Manacher's algorithm finds the longest palindromic substring in **linear time O(n)** by avoiding redundant comparisons using previously computed palindrome information.

**Key Insight:**
- Standard expand-from-center: O(n²) worst case
- Manacher's: O(n) by reusing palindrome boundaries
- Uses concept of "mirror" positions across palindrome center

**Why It's Faster:**
```text
Standard approach: Check each center independently → O(n²)
Manacher's: Use already-found palindromes to skip comparisons → O(n)

Example: If we know s[5..15] is palindrome with center at 10:
When checking position 12, we can use info from mirror position 8!
```

**Algorithm Overview:**
1. Transform string to handle even/odd lengths uniformly: "aba" → "#a#b#a#"
2. For each position, maintain:
   - `P[i]`: radius of palindrome centered at i
   - `C`: center of rightmost palindrome found
   - `R`: right boundary of rightmost palindrome
3. Use mirror property to initialize `P[i]` when `i < R`
4. Expand from `i` only when necessary

---

##### Python Implementation

```python
# Manacher's Algorithm - Longest Palindromic Substring
def longestPalindrome_manacher(s):
    """
    Find longest palindromic substring using Manacher's algorithm.

    Time: O(n) - each position expanded at most once
    Space: O(n) - for transformed string and P array

    Returns: longest palindromic substring
    """
    if not s:
        return ""

    # Step 1: Transform string to handle even/odd uniformly
    # "aba" → "#a#b#a#"
    # "abba" → "#a#b#b#a#"
    T = '#'.join('^{}$'.format(s))
    n = len(T)
    P = [0] * n  # P[i] = radius of palindrome centered at i
    C = R = 0    # Center and right boundary of current rightmost palindrome

    # Step 2: Fill P array using mirror property
    for i in range(1, n - 1):
        # Mirror of i across center C
        mirror = 2 * C - i

        # Initialize P[i] using mirror when i < R
        if i < R:
            P[i] = min(R - i, P[mirror])

        # Try to expand palindrome centered at i
        try:
            while T[i + P[i] + 1] == T[i - P[i] - 1]:
                P[i] += 1
        except IndexError:
            pass

        # Update center and right boundary if palindrome extends beyond R
        if i + P[i] > R:
            C, R = i, i + P[i]

    # Step 3: Find longest palindrome
    max_len, center_idx = max((length, idx) for idx, length in enumerate(P))

    # Step 4: Extract substring from original string
    start = (center_idx - max_len) // 2
    return s[start:start + max_len]

# Alternative: Find all palindrome radii
def manacher_array(s):
    """
    Compute palindrome radius array using Manacher's algorithm.

    Returns: P array where P[i] = radius at position i in transformed string
    """
    T = '#'.join('^{}$'.format(s))
    n = len(T)
    P = [0] * n
    C = R = 0

    for i in range(1, n - 1):
        mirror = 2 * C - i

        if i < R:
            P[i] = min(R - i, P[mirror])

        # Expand palindrome
        try:
            while T[i + P[i] + 1] == T[i - P[i] - 1]:
                P[i] += 1
        except IndexError:
            pass

        # Update C and R
        if i + P[i] > R:
            C, R = i, i + P[i]

    return P

# Count all palindromic substrings - O(n)
def countSubstrings_manacher(s):
    """
    Count all palindromic substrings in O(n) time.

    Each P[i] represents a palindrome of radius P[i]
    Number of palindromes centered at i = (P[i] + 1) // 2
    """
    P = manacher_array(s)

    # Each radius P[i] contributes (P[i] + 1) // 2 palindromes
    # Example: radius 3 in "#a#b#a#" → palindromes: "a", "aba"
    return sum((p + 1) // 2 for p in P)
```

##### Java Implementation

```java
// LC 5 - Longest Palindromic Substring (Manacher's)
/**
 * time = O(N)
 * space = O(N)
 */
class Solution {
    public String longestPalindrome(String s) {
        if (s == null || s.length() == 0) return "";

        // Transform string: "aba" → "^#a#b#a#$"
        StringBuilder T = new StringBuilder("^");
        for (char c : s.toCharArray()) {
            T.append("#").append(c);
        }
        T.append("#$");

        int n = T.length();
        int[] P = new int[n];  // Palindrome radius array
        int C = 0, R = 0;      // Center and right boundary

        // Compute palindrome radii
        for (int i = 1; i < n - 1; i++) {
            int mirror = 2 * C - i;

            // Initialize P[i] using mirror
            if (i < R) {
                P[i] = Math.min(R - i, P[mirror]);
            }

            // Try to expand palindrome at i
            while (T.charAt(i + P[i] + 1) == T.charAt(i - P[i] - 1)) {
                P[i]++;
            }

            // Update center and right boundary
            if (i + P[i] > R) {
                C = i;
                R = i + P[i];
            }
        }

        // Find longest palindrome
        int maxLen = 0;
        int centerIndex = 0;
        for (int i = 1; i < n - 1; i++) {
            if (P[i] > maxLen) {
                maxLen = P[i];
                centerIndex = i;
            }
        }

        // Extract substring from original string
        int start = (centerIndex - maxLen) / 2;
        return s.substring(start, start + maxLen);
    }
}
```

---

##### Visual Example: Step-by-Step Walkthrough

```text
Input: s = "babcbabcbaccba"

Step 1: Transform string
s = "babcbabcbaccba"
T = "^#b#a#b#c#b#a#b#c#b#a#c#c#b#a#$"
     0 1 2 3 4 5 6 7 8 9...

Step 2: Compute P array (palindrome radii)

i=1 (T[1]='#'):
  mirror=2*0-1=-1 (outside)
  Expand: '#b#' → P[1]=1
  Update: C=1, R=2

i=2 (T[2]='b'):
  mirror=2*1-2=0
  i<R (2<2 False), P[2]=0
  Expand: 'b' → no match → P[2]=0

i=3 (T[3]='#'):
  mirror=2*1-3=-1
  Expand: '#b#a#b#' → P[3]=3
  Update: C=3, R=6

i=7 (T[7]='c'):
  mirror=2*3-7=-1
  Expand: 'c' → P[7]=0

i=9 (T[9]='#'):
  Inside previous palindrome
  mirror=2*3-9=-3
  Can use mirror info!
  P[9] = min(R-9, P[mirror])
       = min(6-9, P[-3]) → initialize, then expand

... continue for all positions

Final P array:
Position: 0  1  2  3  4  5  6  7  8  9 10 11 12 ...
T:        ^  #  b  #  a  #  b  #  c  #  b  #  a  ...
P:        0  1  0  3  0  1  0  7  0  1  0  3  0  ...

Maximum: P[7]=7 (center at position 7)
Longest palindrome: "babcbab" (length 7)
```

---

##### Mirror Property Explanation

```text
Key Concept: If we know palindrome boundaries, we can use symmetry!

Example:
    Center C=10, Right boundary R=15
    Current position i=13
    Mirror position i'=7 (mirror of 13 across 10)

    Left Boundary      Center      Right Boundary
          |              |              |
    ...  7 ...          10 ...         13 ... 15
         i'                            i      R

If P[i']=2 (palindrome of radius 2 at position 7):
Then P[i] >= min(R-i, P[i']) = min(15-13, 2) = min(2, 2) = 2

Why? Because everything inside the large palindrome is mirrored!

We still need to try expanding beyond this initial value,
but we skip redundant comparisons within the mirror region.
```

---

##### Complexity Analysis

**Time Complexity: O(n)**
```text
Each character is visited at most twice:
1. Once when updating C and R (moving R forward)
2. Once when checking as center i

Key insight: R only moves forward, never backward
Total expansions across all positions ≤ n
```

**Space Complexity: O(n)**
```text
- Transformed string T: O(n)
- Palindrome array P: O(n)
- Total: O(n)
```

**Comparison:**

| Approach | Time | Space | When to Use |
|----------|------|-------|-------------|
| **Expand from Center** | **O(n²)** | **O(1)** | Simple, short code, small strings |
| **Manacher's Algorithm** | **O(n)** | **O(n)** | Large strings, optimal complexity required |
| **DP (2D array)** | O(n²) | O(n²) | Need all palindrome info |

---

##### Classic LeetCode Problems

| Problem | LC# | Difficulty | Manacher's Benefit | Standard Approach |
|---------|-----|------------|-------------------|-------------------|
| **Longest Palindromic Substring** | **5** | **Medium** | **O(n) vs O(n²)** | Expand from center |
| Palindromic Substrings | 647 | Medium | O(n) counting | O(n²) expand |
| Shortest Palindrome | 214 | Hard | O(n) prefix check | O(n²) KMP |
| Longest Palindromic Subsequence | 516 | Medium | Not applicable | DP O(n²) |
| Palindrome Partitioning | 131 | Medium | O(n) palindrome check | O(n²) precompute |

---

##### Interview Tips

**1. Recognition:**
```text
Follow-up: "Can you do better than O(n²)?"
→ Think Manacher's algorithm for palindrome problems

Interviewer: "What's the optimal time complexity?"
→ O(n) with Manacher's for longest palindrome
```

**2. When to Use:**
```text
✅ Large input strings (n > 10,000)
✅ Follow-up asks for O(n) solution
✅ Need to find longest palindrome
✅ Want to show advanced knowledge

❌ Short strings (expand from center is simpler)
❌ Need all palindromic substrings (Manacher's doesn't help much)
❌ Interview time is limited (complex implementation)
```

**3. Common Mistakes:**
- Off-by-one errors in mirror calculation: `mirror = 2*C - i`
- Forgetting boundary markers '^' and '$' to avoid index checks
- Wrong extraction from transformed string back to original
- Not updating C and R correctly

**4. Simplified Implementation:**
```python
# Minimal Manacher's (easier to code in interview)
def longestPalindrome(s):
    T = '#'.join('^{}$'.format(s))
    n = len(T)
    P, C, R = [0]*n, 0, 0

    for i in range(1, n-1):
        if i < R:
            P[i] = min(R-i, P[2*C-i])

        while T[i+P[i]+1] == T[i-P[i]-1]:
            P[i] += 1

        if i + P[i] > R:
            C, R = i, i + P[i]

    max_len, idx = max((l, i) for i, l in enumerate(P))
    return s[(idx-max_len)//2:(idx+max_len)//2]  # verify: in transformed string T, original string index = T_index // 2
```

**5. Talking Points:**
- "Manacher's uses mirror symmetry to avoid redundant expansions"
- "Each position expanded at most once → amortized O(n)"
- "Transform string to handle even/odd lengths uniformly"
- "Trade-off: O(n) time for O(n) space and implementation complexity"

**6. Alternative for Interviews:**
```text
If time is limited, mention Manacher's exists but implement O(n²):
"The optimal O(n) solution uses Manacher's algorithm, but
I'll implement the O(n²) expand-from-center approach which
is more straightforward and works well for most cases."

Then demonstrate understanding by explaining Manacher's concept.
```

---

##### Advanced: Why Manacher's Works (Proof Sketch)

**Claim**: Total number of character comparisons is O(n).

**Proof**:
```text
Let R be the right boundary of the rightmost palindrome found.

Key observations:
1. R never decreases (only moves forward or stays same)
2. R can increase by at most n total across all iterations
3. Each expansion at position i increases R by at most the expansion amount

For position i:
- If i >= R: We expand from scratch
  → These expansions increase R

- If i < R: We use P[mirror] to skip known palindrome
  → We only expand beyond R
  → These expansions also increase R

Since R increases from 0 to at most n, and each character
comparison increases R by 1, total comparisons ≤ n.

Therefore, time complexity is O(n). □
```

---

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

### Template 7: Incremental Prefix Validation
```python
# Python - LC 720 Longest Word in Dictionary
def longestWord(words):
    """
    Pattern: Build words incrementally by validating immediate prefix

    Key Insight:
      - A word is valid if ALL its prefixes exist in dictionary
      - Instead of checking all prefixes, we only check the immediate prefix
      - This works because we process words in sorted order (shorter first)
      - If "worl" is valid, then "wor", "wo", "w" must already be valid

    Example:
      words = ["w","wo","wor","worl","world"]

      After sorting: ["w","wo","wor","worl","world"]

      Process:
        "w"     -> len==1, add to built, result="w"
        "wo"    -> "w" in built ✓, add "wo", result="wo"
        "wor"   -> "wo" in built ✓, add "wor", result="wor"
        "worl"  -> "wor" in built ✓, add "worl", result="worl"
        "world" -> "worl" in built ✓, add "world", result="world"

    Time: O(n log n) for sorting + O(n*m) for processing (m = avg word length)
    Space: O(n*m) for storing all words in set
    """
    if not words:
        return ""

    # words.sort() works here because shorter words sort before longer ones lexicographically
    # Sort lexicographically (automatically handles tie-breaking)
    words.sort()

    built = set()
    result = ""

    for word in words:
        # Word is valid if:
        # 1. Single character (base case), OR
        # 2. Its immediate prefix exists in built set
        if len(word) == 1 or word[:-1] in built:
            built.add(word)

            # Update result if current word is longer
            # (sorting ensures alphabetical order for ties)
            if len(word) > len(result):
                result = word

    return result

# Alternative with explicit substring
def longestWord_v2(words):
    words.sort()
    built = set()
    result = ""

    for word in words:
        # Check immediate prefix: word.substring(0, word.length() - 1)
        if len(word) == 1 or word[:len(word)-1] in built:
            built.add(word)
            if len(word) > len(result):
                result = word

    return result
```

```java
// Java - LC 720 Longest Word in Dictionary
public String longestWord(String[] words) {
    /**
     * Pattern: Incremental Prefix Validation
     *
     * Core Trick:
     *   word.substring(0, word.length() - 1)
     *
     *   Only check if the IMMEDIATE prefix exists (not all prefixes)
     *   This works because sorting guarantees shorter words are processed first
     *
     * Why Sorting is Critical:
     *   Arrays.sort(words) ensures:
     *   1. Shorter words come before longer words (alphabetically)
     *   2. When we reach "world", "worl" has already been validated
     *   3. If "worl" wasn't valid, it wouldn't be in builtWords
     *
     * Example:
     *   Input: ["a","banana","app","appl","ap","apply","apple"]
     *   After sort: ["a","ap","app","appl","apple","apply","banana"]
     *
     *   Process:
     *     "a"      -> len==1, add ✓
     *     "ap"     -> "a" exists ✓, add ✓
     *     "app"    -> "ap" exists ✓, add ✓
     *     "appl"   -> "app" exists ✓, add ✓
     *     "apple"  -> "appl" exists ✓, add ✓
     *     "apply"  -> "appl" exists ✓, add ✓
     *     "banana" -> "banan" NOT exists ✗, skip
     *
     *   Result: "apple" (longer and lexicographically smaller than "apply")
     *
     * time = O(N log N) for sorting + O(N*M) for processing
     * space = O(N*M) for HashSet storage
     */
    if (words == null || words.length == 0) {
        return "";
    }

    // Sort lexicographically (handles both length and alphabetical order)
    Arrays.sort(words);

    Set<String> built = new HashSet<>();
    String result = "";

    for (String word : words) {
        // Word is valid if:
        // 1. Length == 1 (base case: single char always buildable), OR
        // 2. Its prefix (all chars except last) exists in built set

        /** NOTE !!! KEY TRICK
         *
         * word.substring(0, word.length() - 1)
         *
         * Get the immediate prefix (remove last character)
         *
         * Why not check ALL prefixes?
         *   - We could do:
         *     for (int i = 1; i < word.length(); i++) {
         *         if (!built.contains(word.substring(0, i))) return false;
         *     }
         *
         *   - But that's unnecessary because:
         *     If "worl" is valid, then "wor", "wo", "w" must already be valid
         *     (due to incremental building from sorted order)
         *
         * Inductive Logic:
         *   If immediate prefix exists AND is valid,
         *   Then all shorter prefixes must also exist (by induction)
         */
        if (word.length() == 1 || built.contains(word.substring(0, word.length() - 1))) {
            built.add(word);

            // Update result if current word is longer
            // (sorting ensures lexicographical order is maintained)
            if (word.length() > result.length()) {
                result = word;
            }
        }
    }

    return result;
}
```

**Key Insights:**

1. **Why Only Check Immediate Prefix:**
   - Sorting ensures shorter words are processed first
   - If "worl" is valid, all its prefixes ("wor", "wo", "w") must already be valid
   - This is **inductive reasoning**: checking immediate prefix is sufficient

2. **Why Sorting Works:**
   ```
   Before: ["world","worl","wor","wo","w"]
   After:  ["w","wo","wor","worl","world"]

   When processing "world":
     - "worl" has already been processed
     - If "worl" is in built, all shorter prefixes are guaranteed valid
   ```

3. **Complexity Breakdown:**
   - Sorting: O(N log N)
   - Processing: O(N * M) where M = average word length
   - Space: O(N * M) for HashSet
   - Overall: O(N log N + N*M)

4. **Similar Problems:**
   - LC 720 Longest Word in Dictionary (this pattern)
   - LC 745 Prefix and Suffix Search (Trie variation)
   - LC 648 Replace Words (Trie + prefix matching)

### Template 8: Greedy Line Packing + Space Distribution (Text Wrapping) ⭐⭐⭐⭐⭐

**Pattern**: pack as many words as fit on a line (greedy), then *spread* the leftover
spaces over the gaps. Every word-wrap / column-formatting problem is these 2 phases.

**Key Idea**: while packing, the width needed for `words[i..j]` is
`sum(len) + (number of gaps)` — the gap count is exactly `j - i`, so the fit test is
`lineLen + len(words[j]) + (j - i) <= maxWidth`. When distributing, `base = spaces / slots`
and the **first `spaces % slots` gaps get one extra space** (left-heavy rule).

```java
// java
// LC 68 - Text Justification
// time = O(total chars), space = O(total chars) for the output
// IDEA: 2 phases per line -> (1) greedy pack words, (2) distribute leftover spaces
public List<String> fullJustify(String[] words, int maxWidth) {
    List<String> res = new ArrayList<>();
    int i = 0, n = words.length;
    while (i < n) {
        /** NOTE !!! (1) GREEDY PACK: widest [i, j) that fits with >= 1 space per gap
         *  (j - i) is the number of gaps if we also take words[j] */
        int j = i, lineLen = 0;
        while (j < n && lineLen + words[j].length() + (j - i) <= maxWidth) {
            lineLen += words[j].length();
            j++;
        }
        int slots = j - i - 1;             // gaps between the packed words
        int spaces = maxWidth - lineLen;   // spaces to spread over those gaps

        StringBuilder sb = new StringBuilder();
        if (j == n || slots == 0) {
            // (2a) LAST LINE or SINGLE WORD -> left justify, pad the right
            for (int k = i; k < j; k++) {
                if (k > i) sb.append(' ');
                sb.append(words[k]);
            }
            while (sb.length() < maxWidth) sb.append(' ');
        } else {
            /** NOTE !!! (2b) FULL JUSTIFY
             *  base = spaces / slots, and the FIRST (spaces % slots) gaps get +1 */
            int base = spaces / slots, extra = spaces % slots;
            for (int k = i; k < j; k++) {
                sb.append(words[k]);
                if (k < j - 1) {
                    int pad = base + (k - i < extra ? 1 : 0);
                    for (int p = 0; p < pad; p++) sb.append(' ');
                }
            }
        }
        res.add(sb.toString());
        i = j;   // NOTE !!! next line starts where this one stopped
    }
    return res;
}
```

```python
# python
# LC 68 - Text Justification
# time = O(total chars), space = O(total chars) for the output
# IDEA: 2 phases per line -> (1) greedy pack words, (2) distribute leftover spaces
def fullJustify(words, maxWidth):
    res, i, n = [], 0, len(words)
    while i < n:
        # (1) GREEDY PACK: widest [i, j) that fits with >= 1 space per gap
        j, lineLen = i, 0
        while j < n and lineLen + len(words[j]) + (j - i) <= maxWidth:
            lineLen += len(words[j])
            j += 1
        slots = j - i - 1                 # gaps between the packed words
        spaces = maxWidth - lineLen       # spaces to spread over those gaps

        if j == n or slots == 0:
            # (2a) LAST LINE or SINGLE WORD -> left justify, pad the right
            line = " ".join(words[i:j])
            line += " " * (maxWidth - len(line))
        else:
            # (2b) FULL JUSTIFY: first (spaces % slots) gaps get one extra space
            base, extra = divmod(spaces, slots)
            parts = []
            for k in range(i, j - 1):
                parts.append(words[k])
                parts.append(" " * (base + (1 if k - i < extra else 0)))
            parts.append(words[j - 1])
            line = "".join(parts)
        res.append(line)
        i = j
    return res
```

**Gotchas**
- ⚠️ **The last line is left-justified**, not fully justified — same for a line holding a single word.
- ⚠️ Leftover spaces go **left-heavy**: gap `k` gets `base + 1` while `k < spaces % slots`.
- ⚠️ Every emitted line must be **exactly** `maxWidth` chars — pad the left-justified cases.
- ⚠️ `slots == 0` means division by zero if you forget the single-word branch.

#### Variation 8.1: Chunk-and-Join Formatting — LC 273 Integer to English Words

*Twist*: same "build pieces into a list, join once at the end" discipline, but the chunking rule
is **groups of 3 digits** instead of "as many words as fit". Building a `List<String>` and
joining beats `StringBuilder` + `trim()` because it makes double-space bugs impossible.

```java
// java
// LC 273 - Integer to English Words
// time = O(1) (num <= 2^31-1 -> at most 4 chunks), space = O(1)
// IDEA: split number into 3-digit chunks, spell each chunk, tag it with Thousand/Million/Billion
private static final String[] BELOW_20 = {"", "One", "Two", "Three", "Four", "Five",
    "Six", "Seven", "Eight", "Nine", "Ten", "Eleven", "Twelve", "Thirteen",
    "Fourteen", "Fifteen", "Sixteen", "Seventeen", "Eighteen", "Nineteen"};
private static final String[] TENS = {"", "", "Twenty", "Thirty", "Forty", "Fifty",
    "Sixty", "Seventy", "Eighty", "Ninety"};
private static final String[] THOUSANDS = {"", "Thousand", "Million", "Billion"};

public String numberToWords(int num) {
    if (num == 0) return "Zero";   // NOTE !!! only place "Zero" is ever emitted
    List<String> parts = new ArrayList<>();
    int i = 0;
    while (num > 0) {
        if (num % 1000 != 0) {          // NOTE !!! skip all-zero chunks (1,000,010 -> "One Million Ten")
            List<String> chunk = three(num % 1000);
            if (i > 0) chunk.add(THOUSANDS[i]);
            parts.addAll(0, chunk);     // prepend: we scan chunks low -> high
        }
        num /= 1000;
        i++;
    }
    return String.join(" ", parts);
}

// spell 1..999
private List<String> three(int n) {
    List<String> out = new ArrayList<>();
    if (n == 0) return out;
    if (n < 20) { out.add(BELOW_20[n]); return out; }
    if (n < 100) { out.add(TENS[n / 10]); out.addAll(three(n % 10)); return out; }
    out.add(BELOW_20[n / 100]);
    out.add("Hundred");
    out.addAll(three(n % 100));
    return out;
}
```

```python
# python
# LC 273 - Integer to English Words
# time = O(1), space = O(1)
# IDEA: split number into 3-digit chunks, spell each chunk, tag with Thousand/Million/Billion
BELOW_20 = ["", "One", "Two", "Three", "Four", "Five", "Six", "Seven", "Eight",
            "Nine", "Ten", "Eleven", "Twelve", "Thirteen", "Fourteen", "Fifteen",
            "Sixteen", "Seventeen", "Eighteen", "Nineteen"]
TENS = ["", "", "Twenty", "Thirty", "Forty", "Fifty", "Sixty", "Seventy", "Eighty", "Ninety"]
THOUSANDS = ["", "Thousand", "Million", "Billion"]

def numberToWords(num):
    if num == 0:
        return "Zero"

    def three(n):           # spell 1..999 as a list of words
        if n == 0:
            return []
        if n < 20:
            return [BELOW_20[n]]
        if n < 100:
            return [TENS[n // 10]] + three(n % 10)
        return [BELOW_20[n // 100], "Hundred"] + three(n % 100)

    parts, i = [], 0
    while num:
        if num % 1000:      # NOTE !!! skip all-zero chunks
            parts = three(num % 1000) + ([THOUSANDS[i]] if i else []) + parts
        num //= 1000
        i += 1
    return " ".join(parts)
```

- ⚠️ `num == 0` is the only "Zero"; an inner zero chunk must emit **nothing**.
- ⚠️ 10..19 are irregular words — handle `n < 20` **before** the tens branch.

---

### Template 9: Parse Structured Text (Delimiter Split + Depth/Stack) ⭐⭐⭐⭐

**Pattern**: input is a *serialized structure* (paths, logs, indented trees). Split on the
delimiter, then keep a **stack (or depth → prefix-length map)** describing the current context
instead of re-scanning the string.

**Key Idea**: never carry substrings around — carry **lengths / tokens**. `depthLen[d]` = length
of the path prefix at depth `d`, so a file at depth `d` costs `depthLen[d] + len(name)` in O(1).

```java
// java
// LC 388 - Longest Absolute File Path
// time = O(n), space = O(max depth)
// IDEA: split on '\n'; leading '\t' count = depth; depthLen[d] = prefix length at depth d
public int lengthLongestPath(String input) {
    int best = 0;
    Map<Integer, Integer> depthLen = new HashMap<>();
    depthLen.put(0, 0);                       // root has empty prefix
    for (String line : input.split("\n")) {
        /** NOTE !!! depth == number of leading '\t' (tabs), NOT the indent width */
        int depth = 0;
        while (depth < line.length() && line.charAt(depth) == '\t') depth++;
        String name = line.substring(depth);
        if (name.indexOf('.') >= 0) {
            // a FILE: it is a leaf -> only measure, never push
            best = Math.max(best, depthLen.get(depth) + name.length());
        } else {
            // a DIRECTORY: children live at depth+1, +1 for the '/' separator
            depthLen.put(depth + 1, depthLen.get(depth) + name.length() + 1);
        }
    }
    return best;
}
```

```python
# python
# LC 388 - Longest Absolute File Path
# time = O(n), space = O(max depth)
# IDEA: split on '\n'; leading '\t' count = depth; depth_len[d] = prefix length at depth d
def lengthLongestPath(inp):
    best = 0
    depth_len = {0: 0}                       # root has empty prefix
    for line in inp.split("\n"):
        name = line.lstrip("\t")
        depth = len(line) - len(name)        # NOTE !!! depth = number of leading tabs
        if "." in name:
            best = max(best, depth_len[depth] + len(name))   # file = leaf
        else:
            depth_len[depth + 1] = depth_len[depth] + len(name) + 1   # +1 for '/'
    return best
```

**Gotchas**
- ⚠️ `'\t'` is **one** character — do not count 4 spaces of indent.
- ⚠️ Return `0` when there is no file (`"a"` → `0`), not the longest directory path.
- ⚠️ Overwriting `depthLen[depth+1]` each time is correct: only the *current* branch matters.
- ⚠️ The `+1` per directory is the `'/'` separator; the file itself gets no trailing slash.

#### Variation 9.1: Token Stack — LC 71 Simplify Path

*Twist*: same split-then-stack shape, but the stack holds **tokens** and `..` pops instead of pushes.

```java
// java
// LC 71 - Simplify Path
// time = O(n), space = O(n)
// IDEA: split on '/', ignore "" and ".", ".." pops, everything else pushes
public String simplifyPath(String path) {
    Deque<String> stack = new ArrayDeque<>();
    for (String tok : path.split("/")) {
        if (tok.isEmpty() || tok.equals(".")) continue;   // "//" and "/./" are no-ops
        if (tok.equals("..")) {
            if (!stack.isEmpty()) stack.pollLast();       // NOTE !!! popping empty root is a no-op
        } else {
            stack.offerLast(tok);
        }
    }
    StringBuilder sb = new StringBuilder();
    for (String d : stack) sb.append('/').append(d);
    return sb.length() == 0 ? "/" : sb.toString();
}
```

```python
# python
# LC 71 - Simplify Path
# time = O(n), space = O(n)
# IDEA: split on '/', ignore "" and ".", ".." pops, everything else pushes
def simplifyPath(path):
    stack = []
    for tok in path.split("/"):
        if tok in ("", "."):
            continue
        if tok == "..":
            if stack:
                stack.pop()
        else:
            stack.append(tok)
    return "/" + "/".join(stack)
```

- ⚠️ `"..."` / `"....."` are **valid directory names** — only exactly `".."` pops.
- ⚠️ The result always starts with `/` and never ends with one (except the bare root `"/"`).

---

### Template 10: In-place Char Array — Mark then Rebuild ⭐⭐⭐⭐⭐

**Pattern**: when a "delete some characters" problem needs the *indices* of the offenders,
convert to a `char[]`, **mark** removals with a sentinel in pass 1, and **rebuild** in pass 2.
This avoids O(n²) repeated `substring`/string concatenation.

**Key Idea**: the stack holds **indices, not characters**, so the leftovers on the stack after
the scan are exactly the positions still to delete.

```java
// java
// LC 1249 - Minimum Remove to Make Valid Parentheses
// time = O(n), space = O(n)
// IDEA: stack of '(' INDICES; unmatched ')' marked on sight, unmatched '(' left on the stack
public String minRemoveToMakeValid(String s) {
    char[] arr = s.toCharArray();
    Deque<Integer> stack = new ArrayDeque<>();
    for (int i = 0; i < arr.length; i++) {
        if (arr[i] == '(') {
            stack.push(i);                    // NOTE !!! push the INDEX
        } else if (arr[i] == ')') {
            if (stack.isEmpty()) arr[i] = '*';   // ')' with no partner -> mark for deletion
            else stack.pop();                    // matched pair
        }
        // letters are untouched
    }
    /** NOTE !!! whatever is still on the stack are unmatched '(' positions */
    while (!stack.isEmpty()) arr[stack.pop()] = '*';

    StringBuilder sb = new StringBuilder();
    for (char c : arr) if (c != '*') sb.append(c);
    return sb.toString();
}
```

```python
# python
# LC 1249 - Minimum Remove to Make Valid Parentheses
# time = O(n), space = O(n)
# IDEA: stack of '(' INDICES; unmatched ')' blanked on sight, unmatched '(' left on the stack
def minRemoveToMakeValid(s):
    arr = list(s)
    stack = []
    for i, c in enumerate(arr):
        if c == "(":
            stack.append(i)          # NOTE !!! push the INDEX
        elif c == ")":
            if stack:
                stack.pop()          # matched pair
            else:
                arr[i] = ""          # ')' with no partner -> blank it out
    for i in stack:                  # NOTE !!! leftovers = unmatched '(' positions
        arr[i] = ""
    return "".join(arr)
```

**Gotchas**
- ⚠️ Pick a sentinel that **cannot appear in the input** (here `'*'`; in Python the empty string
  works because `"".join` skips it).
- ⚠️ Don't forget the **second flush** — the unmatched `'('` still sitting on the stack.
- ⚠️ Deleting by `substring` inside the loop turns this into O(n²) and shifts every later index.

**Related**: LC 20 Valid Parentheses is the same scan but only needs a *boolean* (stack empty at
the end); LC 32 Longest Valid Parentheses reuses the index stack to measure `i - stack.peek()`.

---

### Template 11: Greedy Partition by Last Occurrence ⭐⭐⭐⭐

**Pattern**: cut a string into the **maximum number of pieces** such that a property stays local
(e.g. each letter appears in only one piece). Precompute the last index of every char, then
sweep while stretching the current cut point.

**Key Idea**: the current chunk cannot end before `max(last[c])` over all `c` seen so far.
When `i == end`, nothing inside can reach further right → **cut here**.

```java
// java
// LC 763 - Partition Labels
// time = O(n), space = O(1)  (26 letters)
// IDEA: last[c] = final index of c; extend `end` while scanning, cut when i reaches it
public List<Integer> partitionLabels(String s) {
    int[] last = new int[26];
    for (int i = 0; i < s.length(); i++) last[s.charAt(i) - 'a'] = i;  // last occurrence

    List<Integer> res = new ArrayList<>();
    int start = 0, end = 0;
    for (int i = 0; i < s.length(); i++) {
        /** NOTE !!! the chunk must stretch to cover this char's last occurrence */
        end = Math.max(end, last[s.charAt(i) - 'a']);
        if (i == end) {                 // nothing inside reaches past i -> safe to cut
            res.add(end - start + 1);
            start = i + 1;
        }
    }
    return res;
}
```

```python
# python
# LC 763 - Partition Labels
# time = O(n), space = O(1)  (26 letters)
# IDEA: last[c] = final index of c; extend `end` while scanning, cut when i reaches it
def partitionLabels(s):
    last = {c: i for i, c in enumerate(s)}   # dict comp keeps the LAST index
    res, start, end = [], 0, 0
    for i, c in enumerate(s):
        end = max(end, last[c])              # stretch the chunk
        if i == end:                         # safe cut point
            res.append(end - start + 1)
            start = i + 1
    return res
```

**Gotchas**
- ⚠️ Build `last` in a **separate first pass**; you cannot know the future while cutting.
- ⚠️ Cut on `i == end`, **not** `i == last[s[i]]` (a later char may have pushed `end` right).
- ⚠️ This is a greedy *interval-merge* in disguise: `[first[c], last[c]]` intervals merged.

---

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


#### 1-8) Group sub-string (Run-Length Grouping) ⭐⭐⭐⭐

**Core Idea**

Instead of enumerating substrings (O(n²)), **compress the string into consecutive groups** and
solve the problem on the (much smaller) group-length array.

```text
s = "001110011"

groups:
  00   -> 2
  111  -> 3
  00   -> 2
  11   -> 2

group lengths:  [2, 3, 2, 2]
```

For **LC 696 (Count Binary Substrings)**, every valid substring must be `0…01…1` or `1…10…0`,
i.e. it must **straddle exactly one boundary between two adjacent groups**.
A boundary between groups of length `a` and `b` yields exactly `min(a, b)` valid substrings:

```text
adjacent pairs:
  min(2, 3) = 2      # "01", "0011"
  min(3, 2) = 2      # "10", "1100"
  min(2, 2) = 2      # "01", "0011"
--------------------
  total     = 6
```

> **Why `min(a, b)`?** You can pick a matching count `k = 1, 2, ..., min(a, b)`
> and take `k` chars left of the boundary + `k` chars right of it. Any `k > min(a, b)`
> would spill into a third group and break the "grouped consecutively" rule.

**Template — build the group array (O(n) time, O(n) space)**

```python
# python
# IDEA: compress s into consecutive-group lengths, then work on that array
def group_lengths(s):
    groups = [1]
    for i in range(1, len(s)):
        # NOTE !!! boundary -> start a new group
        if s[i-1] != s[i]:
            groups.append(1)
        # same char -> extend current group
        else:
            groups[-1] += 1
    return groups

# LC 696 - Count Binary Substrings
# time = O(n), space = O(n)
def countBinarySubstrings(s):
    groups = group_lengths(s)
    ans = 0
    for i in range(1, len(groups)):
        # NOTE !!! each adjacent pair contributes min(prev, cur)
        ans += min(groups[i-1], groups[i])
    return ans

# one-liner with itertools.groupby
import itertools
def countBinarySubstrings_v2(s):
    groups = [len(list(v)) for _, v in itertools.groupby(s)]
    return sum(min(a, b) for a, b in zip(groups, groups[1:]))
```

**Template — streaming / O(1) space (only `prev` + `cur` group needed)**

```python
# python
# IDEA: we never need the whole group array, only the 2 latest groups
# time = O(n), space = O(1)
def countBinarySubstrings(s):
    ans, prev, cur = 0, 0, 1
    for i in range(1, len(s)):
        if s[i-1] != s[i]:
            ans += min(prev, cur)   # close off the boundary
            prev, cur = cur, 1      # NOTE !!! cur becomes prev, restart cur
        else:
            cur += 1
    # NOTE !!! don't forget the LAST pair (loop never closes it)
    return ans + min(prev, cur)
```

```java
// java
// LC 696 - Count Binary Substrings
// time = O(n), space = O(1)
public int countBinarySubstrings(String s) {
    int ans = 0, prev = 0, cur = 1;
    for (int i = 1; i < s.length(); i++) {
        if (s.charAt(i) != s.charAt(i - 1)) {
            ans += Math.min(prev, cur);
            prev = cur;
            cur = 1;
        } else {
            cur++;
        }
    }
    /** NOTE !!! flush the final group pair after the loop */
    return ans + Math.min(prev, cur);
}
```

**Gotchas**
- ⚠️ **Flush the last group.** The loop only settles a group when it sees a boundary, so the
  final group is never paired — always add `min(prev, cur)` after the loop.
- ⚠️ **A group of length 1 is still a valid group** — don't filter out `len == 1`.
- ⚠️ Init `prev = 0` (not 1) so the very first boundary contributes `min(0, cur) = 0`.
- ⚠️ Loop from `i = 1`, comparing `s[i]` vs `s[i-1]`, to avoid index-out-of-range.

**Similar Problems (Run-Length Grouping)**

| Problem | LC # | What you do with the group lengths | Difficulty |
|---------|------|------------------------------------|------------|
| Count Binary Substrings | 696 | Sum `min(g[i-1], g[i])` over adjacent pairs | Easy |
| Count and Say | 38 | Emit `count + char` per group, iterate n times | Medium |
| String Compression | 443 | Write `char + count` in-place | Medium |
| Consecutive Characters | 1446 | `max(group lengths)` | Easy |
| Max Consecutive Ones | 485 | `max` length of the `1` groups | Easy |
| Max Consecutive Ones III | 1004 | Sliding window over groups (flip ≤ k zeros) | Medium |
| Max Consecutive Ones II | 487 | Merge two `1` groups across a single `0` group | Medium |
| Longest Repeating Char Replacement | 424 | Window + max-freq (group idea generalized) | Medium |
| Positions of Large Groups | 830 | Report groups with length ≥ 3 | Easy |
| Find Longest Awesome Substring | 1542 | Bitmask parity (grouping variant) | Hard |
| Merge Strings Alternately | 1768 | Two-pointer over runs | Easy |

**Variation: compare TWO strings group-by-group — LC 809 Expressive Words**

*Twist*: instead of scanning one string's groups, build `(char, count)` groups for **both**
strings and zip them. Two strings match iff the group *sequences* line up and each source group
is either the same size or "stretchable" (`>= 3`).

```python
# python
# LC 809 - Expressive Words
# time = O(n + sum(len(w))), space = O(n)
# IDEA: reduce both strings to (char, run-length) groups, then compare group-by-group
def groups(x):
    res, i = [], 0
    while i < len(x):
        j = i
        while j < len(x) and x[j] == x[i]:
            j += 1
        res.append((x[i], j - i))
        i = j
    return res

def expressiveWords(s, words):
    gs = groups(s)
    cnt = 0
    for w in words:
        gw = groups(w)
        # NOTE !!! group COUNT must match first ("abc" vs "abbc" can never match)
        if len(gw) != len(gs):
            continue
        # each pair: same char AND (equal length OR s-group is stretchable: >= 3 and longer)
        if all(c1 == c2 and (n1 == n2 or (n1 >= 3 and n1 > n2))
               for (c1, n1), (c2, n2) in zip(gs, gw)):
            cnt += 1
    return cnt
```

```java
// java
// LC 809 - Expressive Words
// time = O(n + sum(len(w))), space = O(n)
// IDEA: reduce both strings to (char, run-length) groups, then compare group-by-group
public int expressiveWords(String s, String[] words) {
    List<int[]> gs = groups(s);
    int cnt = 0;
    for (String w : words) {
        List<int[]> gw = groups(w);
        if (gw.size() != gs.size()) continue;   // NOTE !!! group count must match
        boolean ok = true;
        for (int i = 0; i < gs.size(); i++) {
            int[] a = gs.get(i), b = gw.get(i);
            if (a[0] != b[0]) { ok = false; break; }
            /** NOTE !!! equal counts are fine; otherwise s-group must be >= 3 AND longer */
            if (a[1] != b[1] && (a[1] < 3 || a[1] < b[1])) { ok = false; break; }
        }
        if (ok) cnt++;
    }
    return cnt;
}

private List<int[]> groups(String x) {
    List<int[]> res = new ArrayList<>();
    int i = 0;
    while (i < x.length()) {
        int j = i;
        while (j < x.length() && x.charAt(j) == x.charAt(i)) j++;
        res.add(new int[]{x.charAt(i), j - i});
        i = j;
    }
    return res;
}
```

- ⚠️ Extension only **grows** groups: `"aaa"` cannot match `"aaaa"` (need `n1 > n2`).
- ⚠️ A group of size 2 can never be stretched (`"aa"` from `"a"` is invalid) — the `>= 3` rule.

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
| Reverse Only Letters | 917 | Skip special chars | Easy |
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
> For LC examples comparing expand-from-center vs Manacher's, see the Classic LeetCode Problems table in Template 4.1 above.

| Problem | LC # | Key Technique | Difficulty |
|---------|------|---------------|------------|
| Longest Palindromic Substring | 5 | Expand from center or Manacher's O(n) | Medium |
| Palindrome Partitioning | 131 | Backtracking | Medium |
| Longest Palindrome | 409 | Character counting | Easy |
| Palindromic Substrings | 647 | Expand from center or Manacher's O(n) | Medium |
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

#### **Incremental Prefix Validation Problems**
| Problem | LC # | Key Technique | Difficulty |
|---------|------|---------------|------------|
| Longest Word in Dictionary | 720 | Sort + immediate prefix check | Medium |
| Implement Trie (Prefix Tree) | 208 | Trie data structure | Medium |
| Replace Words | 648 | Trie + prefix matching | Medium |
| Word Search II | 212 | Trie + DFS | Hard |

**Pattern Recognition:**
- Need to validate if all prefixes of a word exist
- Multiple words share common prefixes
- Building words character-by-character
- Dictionary-based word validation

**Key Trick:**
```java
// Instead of checking ALL prefixes (O(M²) per word):
for (int i = 1; i < word.length(); i++) {
    if (!dict.contains(word.substring(0, i))) return false;
}

// Only check IMMEDIATE prefix (O(M) per word):
if (word.length() == 1 || dict.contains(word.substring(0, word.length() - 1))) {
    // Valid!
}
```

## Pattern Selection Strategy

```text
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
   └── NO → Continue to 7

7. Validate word building from prefixes?
   ├── YES → Incremental Prefix Validation
   │         ├── Sort + HashSet → O(N log N)
   │         └── Check immediate prefix only
   └── NO → Continue to 8

8. Answer depends on runs of consecutive identical chars?
   ├── YES → Run-Length Grouping
   │         ├── Build group-length array → work on adjacent pairs (LC 696)
   │         └── Streaming prev/cur → O(1) space
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
| **Run-Length Grouping** | Consecutive groups | `if s[i]!=s[i-1]: ans+=min(prev,cur); prev,cur=cur,1` |

### Common Patterns & Tricks

#### **ASCII Case Difference Trick (|char1 - char2| == 32)**

A powerful trick for detecting **same letter but different case** (e.g., `'a'` vs `'A'`):

```text
Math.abs('a' - 'A') == 32   // true
Math.abs('z' - 'Z') == 32   // true
Math.abs('a' - 'B') == 33   // false (different letters)
```

**Why 32?** In ASCII, lowercase letters start at 97 (`'a'`) and uppercase at 65 (`'A'`). The difference is always exactly 32 for the same letter.

**Classic Use Case: LC 1544 - Make The String Great (Stack)**
> Remove adjacent pairs where same letter but different case until no such pair remains.

```java
// Java - Stack approach using |char1 - char2| == 32
public String makeGood(String s) {
    Stack<Character> stack = new Stack<>();

    for (char curr : s.toCharArray()) {
        if (!stack.isEmpty()) {
            char prev = stack.peek();

            /** NOTE !!
             *  core idea:
             *   The "Great" Condition: A pair is bad if |char1 - char2| == 32.
             *   Math.abs('a' - 'A') == 32.
             *   This checks if they are the same letter but different case.
             */
            if (Math.abs(curr - prev) == 32) {
                stack.pop(); // They cancel out — remove the pair
                continue;    // Move to next character
            }
        }
        stack.push(curr);
    }

    StringBuilder sb = new StringBuilder();
    for (char c : stack) {
        sb.append(c);
    }
    return sb.toString();
}
```

```java
// Java - StringBuilder variant (more concise)
public String makeGood(String s) {
    StringBuilder sb = new StringBuilder();
    for (char c : s.toCharArray()) {
        int len = sb.length();
        if (len > 0 && Math.abs(sb.charAt(len - 1) - c) == 32) {
            sb.deleteCharAt(len - 1); // Remove last char (cancel pair)
        } else {
            sb.append(c);
        }
    }
    return sb.toString();
}
```

```python
# Python equivalent
def makeGood(s: str) -> str:
    stack = []
    for c in s:
        if stack and abs(ord(stack[-1]) - ord(c)) == 32:
            stack.pop()  # Cancel the pair
        else:
            stack.append(c)
    return ''.join(stack)
```

**Key Insight:** This trick generalizes to any problem requiring adjacent pair cancellation where pairs are defined by ASCII distance. Combine with a Stack for O(N) time, O(N) space.

| Check | Meaning | Example |
|-------|---------|---------|
| `Math.abs(a - b) == 32` | Same letter, different case | `'a'` and `'A'` |
| `Character.toLowerCase(a) == Character.toLowerCase(b)` | Same letter (any case) | `'a'` and `'A'` |
| `a == b` | Exact same character | `'a'` and `'a'` |

---

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

### 2-1) Compare Version Number — LC 165
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

### 2-3) Count and say — LC 38
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

### 2-4) Monotone Increasing Digits — LC 738
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

### 2-5) Validate IP Address — LC 468
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

### 2-6) String to Integer (atoi) — LC 8
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

### 2-7) License Key Formatting — LC 482
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

### 2-8) Repeated String Match — LC 686
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

### 2-9) Count Binary Substrings — LC 696
> Pattern: **Run-Length Grouping** — see [1-8) Group sub-string](#1-8-group-sub-string-run-length-grouping-) for the full template, gotchas, and similar-problem table.

**Core idea**: compress `s` into consecutive-group lengths, then every valid substring straddles
exactly one boundary — an adjacent pair `(a, b)` contributes `min(a, b)`.

```text
s = "001110011"  ->  groups = [2, 3, 2, 2]
ans = min(2,3) + min(3,2) + min(2,2) = 2 + 2 + 2 = 6
```

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
# (same as V0 above — Group By Character + continuous sub-string)

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

### 2-10) Roman to Integer — LC 13
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

### 2-11) Count Unique Characters of All Substrings of a Given String — LC 828
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

### 2-12) Palindromic Substrings — LC 647
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

### 2-13) Repeated Substring Pattern — LC 459
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

### 2-14) Reverse Only Letters — LC 917

**Pattern: Selective Character Reversal**
- Reverse only alphabetic characters
- Keep non-alphabetic characters in original positions
- Two approaches: Two Pointers or Stack

#### Approach 1: Two Pointers (Optimal)
```java
// java
// LC 917. Reverse Only Letters
/**
 * Pattern: Two pointers with selective swap
 *
 * Key Technique:
 *   - Use Character.isLetter() to check if char is alphabetic
 *   - Skip non-letters on both sides
 *   - Swap only when both pointers point to letters
 *
 * Example:
 *   s = "ab-cd"
 *
 *   [a,b,-,c,d]    l=0, r=4, both letters, swap
 *    l       r     -> [d,b,-,c,a]
 *
 *   [d,b,-,c,a]    l=1, r=3, both letters, swap
 *      l   r       -> [d,c,-,b,a]
 *
 *   [d,c,-,b,a]    l=2, r=2, l >= r, done!
 *        lr
 *
 * Example 2:
 *   s = "a-bC-dEf-ghIj"
 *
 *   [a,-,b,C,-,d,E,f,-,g,h,I,j]
 *    l                       r    both letters, swap
 *   -> [j,-,b,C,-,d,E,f,-,g,h,I,a]
 *
 *   [j,-,b,C,-,d,E,f,-,g,h,I,a]
 *        l                   r    both letters, swap
 *   -> [j,-,I,C,-,d,E,f,-,g,h,b,a]
 *   ... continue ...
 *
 * Time: O(N), Space: O(N) for char array
 */
public String reverseOnlyLetters(String s) {
    // Convert to char array for easy swapping
    char[] arr = s.toCharArray();
    int l = 0;
    int r = s.length() - 1;

    while (l < r) {
        /** NOTE !!!
         *
         *  Character.isLetter() - Key method to check if char is alphabetic
         *
         *  IMPORTANT: Check both conditions:
         *    1. l < r (pointers haven't crossed)
         *    2. !Character.isLetter(arr[l]) (current char is not letter)
         */
        // Move left pointer until it hits a letter
        while (l < r && !Character.isLetter(arr[l])) {
            l++;
        }

        // Move right pointer until it hits a letter
        while (l < r && !Character.isLetter(arr[r])) {
            r--;
        }

        // Swap the letters
        char tmp = arr[l];
        arr[l] = arr[r];
        arr[r] = tmp;

        // Move pointers inward
        l++;
        r--;
    }

    return new String(arr);
}
```

**Character Validation Methods:**
```java
// java
// Key methods for character checking

char x = 'a';

// Check if alphabetic letter (a-z, A-Z)
Character.isLetter(x);         // true

// Check if digit (0-9)
Character.isDigit('5');        // true

// Check if letter or digit
Character.isLetterOrDigit(x);  // true

// Check if whitespace
Character.isWhitespace(' ');   // true

// Case conversion
Character.toLowerCase('A');    // 'a'
Character.toUpperCase('b');    // 'B'
```

```python
# python
# Character checking methods

char = 'a'

# Check if alphabetic
char.isalpha()      # True

# Check if digit
'5'.isdigit()       # True

# Check if alphanumeric
char.isalnum()      # True

# Check if whitespace
' '.isspace()       # True

# Case conversion
char.upper()        # 'A'
char.lower()        # 'a'
```

#### Approach 2: Stack (FILO)
```java
// java
// LC 917. Reverse Only Letters
/**  IDEA: Stack-based reversal (FILO - First In Last Out)
 *
 *  Steps:
 *   1. First pass: Loop over string, save only LETTERS in stack
 *   2. Second pass: Loop over string again
 *      - For NON-letters: append in original order
 *      - For letters: pop from stack (reverse order due to FILO)
 *
 * Example:
 *   s = "ab-cd"
 *
 *   First pass: Stack = [a, b, c, d]  (top -> d)
 *
 *   Second pass:
 *     i=0, 'a' is letter  -> pop 'd' -> result = "d"
 *     i=1, 'b' is letter  -> pop 'c' -> result = "dc"
 *     i=2, '-' NOT letter -> append '-' -> result = "dc-"
 *     i=3, 'c' is letter  -> pop 'b' -> result = "dc-b"
 *     i=4, 'd' is letter  -> pop 'a' -> result = "dc-ba"
 *
 * Time: O(N), Space: O(N) for stack
 */
public String reverseOnlyLetters(String s) {
    // NOTE !!! Stack: FILO (First In, Last Out)
    Stack<Character> letters = new Stack<>();

    // First pass: Save all letters in stack
    for (char c : s.toCharArray()) {
        if (Character.isLetter(c)) {
            letters.push(c);
        }
    }

    StringBuilder ans = new StringBuilder();

    // Second pass: Build result
    for (char c : s.toCharArray()) {
        if (Character.isLetter(c)) {
            // For letters: pop from stack (reversed order)
            ans.append(letters.pop());
        } else {
            // For non-letters: keep original position
            ans.append(c);
        }
    }

    return ans.toString();
}
```

**Stack Pattern Visualization:**
```text
Input: "Test1ng-Leet=code-Q!"

Step 1: Build Stack (push letters only)
Stack building:
  T -> [T]
  e -> [T, e]
  s -> [T, e, s]
  t -> [T, e, s, t]
  (skip '1')
  n -> [T, e, s, t, n]
  g -> [T, e, s, t, n, g]
  (skip '-')
  L -> [T, e, s, t, n, g, L]
  ... continue ...

Final Stack (bottom to top):
  [T, e, s, t, n, g, L, e, e, t, c, o, d, e, Q]
   ^                                          ^
   bottom                                    top

Step 2: Build Result (pop letters, keep non-letters)
  Position 0: 'T' is letter -> pop 'Q' -> result = "Q"
  Position 1: 'e' is letter -> pop 'e' -> result = "Qe"
  Position 2: 's' is letter -> pop 'd' -> result = "Qed"
  Position 3: 't' is letter -> pop 'o' -> result = "Qedo"
  Position 4: '1' NOT letter -> append '1' -> result = "Qedo1"
  Position 5: 'n' is letter -> pop 'c' -> result = "Qedo1c"
  Position 6: 'g' is letter -> pop 't' -> result = "Qedo1ct"
  Position 7: '-' NOT letter -> append '-' -> result = "Qedo1ct-"
  ... continue ...

Final: "Qedo1ct-eeLg=ntse-T!"
```

**Comparison:**
| Approach | Time | Space | When to Use |
|----------|------|-------|-------------|
| Two Pointers | O(N) | O(N) | In-place modification, optimal |
| Stack | O(N) | O(N) | Need to preserve original, clearer logic |

**Similar Problems:**
- LC 917 Reverse Only Letters (this pattern)
- LC 345 Reverse Vowels of a String (selective reversal)
- LC 344 Reverse String (full reversal)
- LC 541 Reverse String II (selective ranges)
- LC 151 Reverse Words in a String (word-level reversal)

### 2-15) Verifying an Alien Dictionary — LC 953

**Pattern: Custom Lexicographic Order Comparison**
- Map each character to its rank in the alien order
- Compare adjacent words character by character
- Handle prefix case: shorter word must come first

#### Approach: Array Mapping + Adjacent Word Comparison
```java
// java
// LC 953. Verifying an Alien Dictionary
/**
 * Pattern: Custom order mapping + pairwise comparison
 *
 * Key Technique:
 *   - Use int[26] array to map each character to its alien rank (O(1) lookup)
 *   - Compare adjacent word pairs only (if each pair is sorted, whole list is sorted)
 *   - On first differing character, compare their ranks to determine order
 *   - If one word is a prefix of the other, shorter word must come first
 *
 * Example:
 *   words = ["hello","leetcode"], order = "hlabcdefgijkmnopqrstuvwxyz"
 *
 *   Alien rank mapping:
 *     h->0, l->1, a->2, b->3, c->4, ...
 *
 *   Compare "hello" vs "leetcode":
 *     h(rank=0) vs l(rank=1) -> 0 < 1 -> sorted!
 *
 * Example 2:
 *   words = ["apple","app"], order = "abcdefghijklmnopqrstuvwxyz"
 *
 *   Compare "apple" vs "app":
 *     a==a, p==p, p==p -> all equal up to minLen
 *     len("apple")=5 > len("app")=3 -> NOT sorted!
 *     (longer word cannot come before its prefix)
 *
 * Time: O(M) where M = total characters across all words
 * Space: O(1) - fixed size array of 26
 */
public boolean isAlienSorted(String[] words, String order) {
    // 1. Map each character to its alien rank for O(1) lookup
    int[] alienOrder = new int[26];
    for (int i = 0; i < order.length(); i++) {
        alienOrder[order.charAt(i) - 'a'] = i;
    }

    // 2. Compare adjacent words
    for (int i = 0; i < words.length - 1; i++) {
        if (!isSorted(words[i], words[i + 1], alienOrder)) {
            return false;
        }
    }

    return true;
}

private boolean isSorted(String w1, String w2, int[] alienOrder) {
    int len1 = w1.length();
    int len2 = w2.length();
    int minLen = Math.min(len1, len2);

    for (int i = 0; i < minLen; i++) {
        char c1 = w1.charAt(i);
        char c2 = w2.charAt(i);

        if (c1 != c2) {
            // If characters differ, the first one must have a smaller rank
            return alienOrder[c1 - 'a'] < alienOrder[c2 - 'a'];
        }
    }

    // If we reach here, one word is a prefix of the other.
    // "apple" is NOT allowed to come before "app".
    // The shorter word must come first.
    return len1 <= len2;
}
```

**Key Insights:**
```text
Why int[26] array instead of HashMap?
  - Characters are lowercase English letters only (a-z)
  - alienOrder[ch - 'a'] = rank  ->  O(1) lookup, no boxing overhead
  - Classic trick: char - 'a' maps 'a'->0, 'b'->1, ..., 'z'->25

Why compare only adjacent pairs?
  - If words[0] <= words[1] and words[1] <= words[2], then words[0] <= words[2]
  - Transitivity means we only need N-1 comparisons

Why return len1 <= len2 at the end?
  - If all characters match up to minLen, the shorter word must come first
  - "app" < "apple" in any lexicographic order
  - "apple" before "app" is INVALID (Example 3 in problem)
```

**Similar Problems:**
- LC 953 Verifying an Alien Dictionary (this pattern)
- LC 269 Alien Dictionary (topological sort, harder)
- LC 242 Valid Anagram (character frequency mapping)

### 2-16) Longest Word in Dictionary through Deleting — LC 524

**Pattern: Subsequence Check + Best Candidate Tracking**
- Check if a dictionary word can be formed by deleting characters from `s` (i.e., is a subsequence of `s`)
- Track the best result: longest length wins, ties broken by lexicographic order
- Two-pointer subsequence check is the core technique

#### Approach 1: Iterate + Subsequence Check + In-place Best Tracking (Optimal)
```java
// java
// LC 524. Longest Word in Dictionary through Deleting
/**
 * Pattern: Two-pointer subsequence check + greedy best tracking
 *
 * Core Idea:
 *   - For each word in dictionary, check if it's a subsequence of s
 *   - Subsequence check: two pointers, one on s and one on word
 *     - If chars match, advance both pointers
 *     - If not, only advance s pointer (skip/delete char from s)
 *     - Word is subsequence if its pointer reaches the end
 *   - Track best candidate: longer length wins, same length -> smaller lexicographic order
 *
 * Example:
 *   s = "abpcplea", dictionary = ["ale","apple","monkey","plea"]
 *
 *   Check "ale":    a-b-p-c-p-l-e-a
 *                   ^         ^ ^       -> match a,l,e -> subsequence ✅ (len=3)
 *   Check "apple":  a-b-p-c-p-l-e-a
 *                   ^ ^ ^   ^ ^         -> match a,p,p,l,e -> subsequence ✅ (len=5)
 *   Check "monkey": no 'm' early enough -> ❌
 *   Check "plea":   a-b-p-c-p-l-e-a
 *                       ^     ^ ^ ^     -> match p,l,e,a -> subsequence ✅ (len=4)
 *
 *   Best: "apple" (longest at len=5)
 *
 * Time: O(N * M) where N = dictionary size, M = length of s
 * Space: O(1) extra (just pointers and result string)
 */
public String findLongestWord(String s, List<String> dictionary) {
    String res = "";

    for (String word : dictionary) {
        // 1. Check if word is a subsequence of s
        if (isSubsequence(s, word)) {
            // 2. Update best: longer wins, ties broken by lexicographic order
            /**
             * NOTE !!!
             *
             *  word.compareTo(res) < 0 means word is lexicographically SMALLER
             *  We want the smallest lexicographic order among same-length candidates
             */
            if (word.length() > res.length() ||
                    (word.length() == res.length() && word.compareTo(res) < 0)) {
                res = word;
            }
        }
    }
    return res;
}

/**
 * Two-pointer subsequence check
 *
 *  s = source string (we "delete" chars from this)
 *  target = dictionary word (check if this is a subsequence of s)
 *
 *  i moves through s (always advances)
 *  j moves through target (advances only on match)
 */
private boolean isSubsequence(String s, String target) {
    int i = 0, j = 0;
    while (i < s.length() && j < target.length()) {
        if (s.charAt(i) == target.charAt(j)) {
            j++; // Match found, advance target pointer
        }
        i++; // Always advance source pointer
    }
    // If j reached end, all chars of target were found in order
    return j == target.length();
}
```

#### Approach 2: Sort First + Return First Match
```java
// java
// LC 524. Longest Word in Dictionary through Deleting
/**
 * Pattern: Pre-sort dictionary by (length DESC, lexicographic ASC),
 *          then return the first subsequence match
 *
 * Key Trick:
 *   - Sort so that longest words come first
 *   - Among same-length words, lexicographically smaller comes first
 *   - First valid subsequence match IS the answer (no need to track best)
 *
 * Time: O(N log N * K + N * M)  where K = avg word length (for sort comparisons)
 * Space: O(log N) for sorting
 */
public String findLongestWord_sort(String s, List<String> d) {
    // Sort: longer first, then lexicographic order for ties
    Collections.sort(d, (s1, s2) ->
        s2.length() != s1.length() ? s2.length() - s1.length() : s1.compareTo(s2)
    );

    for (String str : d) {
        if (isSubsequence(s, str))
            return str;  // First match is guaranteed to be the best
    }
    return "";
}
```

**Key Insights:**
```text
Two-pointer subsequence check:
  - i (source pointer) ALWAYS advances
  - j (target pointer) advances ONLY on character match
  - If j == target.length() at the end, target is a subsequence
  - This is the same pattern as LC 392 (Is Subsequence)

Best candidate selection (without sorting):
  - word.length() > res.length()  ->  longer is always better
  - word.compareTo(res) < 0       ->  lexicographically smaller wins ties
  - Combined: no need to sort the dictionary at all

Sorting approach trade-off:
  - Pro: simpler logic (return first match)
  - Con: O(N log N) sorting overhead
  - Approach 1 (no sort) is generally preferred
```

**Similar Problems:**
- LC 524 Longest Word in Dictionary through Deleting (this pattern)
- LC 392 Is Subsequence (core two-pointer subsequence check)
- LC 720 Longest Word in Dictionary (prefix-based, different pattern)
- LC 1055 Shortest Way to Form String (subsequence with multiple passes)

### 2-17) Count Pairs of Equal Substrings With Minimum Difference — LC 1794

**Pattern: First/Last Character Occurrence + Minimum Difference Counting**
- LC 1794. Count Pairs of Equal Substrings With Minimum Difference (Medium)

#### Core Idea
```text
Non-obvious key insight: optimal quadruples ALWAYS use single-character substrings.

Why? For quadruple (i, j, a, b) minimizing j - a:
  - Extending in firstString (j > i) increases j → diff gets larger
  - Extending in secondString (b > a) decreases a → diff also gets larger
  - Therefore i == j, a == b is always optimal → single characters only

For each character c shared by both strings:
  - FIRST occurrence in firstString  → smallest i, minimizes diff
  - LAST  occurrence in secondString → largest  a, minimizes diff
  - diff = i - a; track minimum and count characters achieving it
```

#### Java Implementation (O(n + m))
```java
// LC 1794 - Count Pairs of Equal Substrings With Minimum Difference
/**
 * Time: O(n + m)  Space: O(1) — fixed 26-char arrays
 *
 * Trick: last[c] = j + 1  so 0 means "not present in secondString"
 */
public int countQuadruples(String firstString, String secondString) {
    int[] last = new int[26];

    // Record LAST occurrence of each char in secondString (+1 offset)
    for (int j = 0; j < secondString.length(); j++) {
        last[secondString.charAt(j) - 'a'] = j + 1;
    }

    int minDiff = Integer.MAX_VALUE;
    int count = 0;
    boolean[] visited = new boolean[26]; // only use FIRST occurrence in firstString

    for (int i = 0; i < firstString.length(); i++) {
        int charIdx = firstString.charAt(i) - 'a';
        if (visited[charIdx]) continue;
        visited[charIdx] = true;

        int j = last[charIdx];
        if (j > 0) { // character exists in secondString
            int diff = i - j; // j stored as actual_index + 1

            if (diff < minDiff) {
                minDiff = diff;
                count = 1;
            } else if (diff == minDiff) {
                count++;
            }
        }
    }

    return count;
}
```

**Key Tricks:**
```text
+1 offset for "not found" sentinel:
  last[c] = 0  → character never appeared in secondString
  last[c] = k  → character last appeared at index k-1

Why FIRST in firstString + LAST in secondString:
  - Later occurrence of c in firstString → larger i → larger diff (bad)
  - Earlier occurrence of c in secondString → smaller a → larger diff (bad)
  - First + Last gives the tightest (minimum) i - a for each character
```

**Similar Problems:**
- LC 1624 Largest Substring Between Two Equal Characters (first/last occurrence span)
- LC 387 First Unique Character in a String (first occurrence tracking)
- LC 1 Two Sum (hash map for O(1) pairing/lookup)
- LC 242 Valid Anagram (character frequency array)
- LC 567 Permutation in String (character position mapping + sliding window)

---

### 2-18) Ambiguous Coordinates — LC 816

**Pattern: Enumerate Splits + Generate Valid Number Formats**
- LC 816. Ambiguous Coordinates (Medium)
- Given digits like `"(123)"`, restore all possible `"(x, y)"` coordinates by inserting a comma and (optionally) decimal points.

#### Core Idea
```text
2 nested decisions:
  1) WHERE to split the digit string into left / right (the comma position)
  2) HOW to format each half as a valid number (integer or decimal)

For each split position i (1 <= i < len):
  left  = digits[:i]
  right = digits[i:]
  -> enumerate valid formats of left  x valid formats of right
  -> combine into "(left, right)"

A half can be either:
  (A) a whole integer (no decimal point)
  (B) a decimal: insert '.' at every interior position
```

#### Validity Rules (the tricky part)
```text
Whole integer  s:
  - valid only if s == single digit, OR s does NOT start with '0'
  - "0" ok, "10" ok, "01" / "00" invalid

Decimal  int_part . dec_part:
  - int_part: no leading zero unless it is exactly "0"
      -> "0.5" ok, "05.1" invalid
  - dec_part: cannot end with '0' (no trailing zero)
      -> "1.5" ok, "1.50" invalid, "1.0" invalid
```

#### Python (V0 — explicit helper)
```python
# LC 816 - Ambiguous Coordinates
class Solution(object):
    def ambiguousCoordinates(self, s):
        # time  = O(n^4) : O(n) splits * O(n) decimal pos * O(n) string build
        # space = O(n^2) for results
        digits = s[1:-1]            # strip outer parentheses
        res = []
        for i in range(1, len(digits)):
            lefts  = self.get_valid_formats(digits[:i])
            rights = self.get_valid_formats(digits[i:])
            for l in lefts:
                for r in rights:
                    res.append("({}, {})".format(l, r))
        return res

    def get_valid_formats(self, sub):
        ans = []
        n = len(sub)
        # (A) whole integer: no leading zero unless single char
        if n == 1 or not sub.startswith('0'):
            ans.append(sub)
        # (B) decimal: insert '.' at every interior position
        for i in range(1, n):
            int_part, dec_part = sub[:i], sub[i:]
            if len(int_part) > 1 and int_part.startswith('0'):
                continue            # leading zero in integer part
            if dec_part.endswith('0'):
                continue            # trailing zero in decimal part
            ans.append(int_part + "." + dec_part)
        return ans
```

#### Python (concise — generator)
```python
class Solution(object):
    def ambiguousCoordinates(self, s):
        digits = s[1:-1]
        res = []

        def generate(part):
            n = len(part)
            if n == 1 or part[0] != '0':   # whole integer
                yield part
            for i in range(1, n):          # decimal versions
                left, right = part[:i], part[i:]
                if len(left) > 1 and left[0] == '0':
                    continue
                if right[-1] == '0':
                    continue
                yield left + "." + right

        for i in range(1, len(digits)):
            for l in generate(digits[:i]):
                for r in generate(digits[i:]):
                    res.append("(" + l + ", " + r + ")")
        return res
```

**Worked Example:**
```text
s = "(0123)"  ->  digits = "0123"

split "0" | "123":
  "0" valid formats     -> ["0"]
  "123" valid formats   -> ["123", "1.23", "12.3"]
  -> (0, 123), (0, 1.23), (0, 12.3)

split "01" | "23":
  "01" -> invalid as integer (leading zero), "0.1" ok
  "23" -> ["23", "2.3"]
  -> (0.1, 23), (0.1, 2.3)

split "012" | "3":
  "012" -> "0.12" ok (only)
  "3"   -> ["3"]
  -> (0.12, 3)

Final: 6 coordinates
```

**Key Tricks:**
```text
- "0" alone is always a valid integer; "00", "01" never are.
- Decimal: a digit sequence is invalid if it ends in '0' (else two
  representations collide, e.g. "1.50" == "1.5").
- Two independent halves -> cross-product (left choices x right choices).
```

**Similar Problems:**
- LC 93 Restore IP Addresses (enumerate split positions + segment validity)
- LC 468 Validate IP Address (per-segment leading-zero / range rules)
- LC 282 Expression Add Operators (insert operators between digits)

---

## Advanced String Algorithms — Z-Algorithm, KMP Applications, String DP

### Z-Algorithm — O(n) Pattern Matching (Alternative to KMP)
Build Z-array where `Z[i]` = length of the longest substring starting at `i` that matches a prefix of the string.

```python
def z_function(s):
    n = len(s)
    z = [0] * n
    z[0] = n
    l, r = 0, 0
    for i in range(1, n):
        if i < r:
            z[i] = min(r - i, z[i - l])
        while i + z[i] < n and s[z[i]] == s[i + z[i]]:
            z[i] += 1
        if i + z[i] > r:
            l, r = i, i + z[i]
    return z

def z_search(text, pattern):
    """Find all occurrences of pattern in text."""
    s = pattern + '#' + text   # '#' acts as separator
    z = z_function(s)
    m = len(pattern)
    return [i - m - 1 for i in range(m + 1, len(s)) if z[i] == m]

# KMP vs Z-Algorithm:
# Both O(n+m). KMP uses failure function; Z-algorithm is simpler to implement.
# In interviews: pick whichever you can code correctly under pressure.
```

### Shortest Palindrome — LC 214 (KMP Application)
Find the shortest palindrome by prepending characters to the front of `s`.

```python
def shortestPalindrome(s):
    # Key insight: find longest palindromic prefix using KMP
    # Build string: s + '#' + reverse(s)
    # The KMP failure value at the end = length of longest palindromic prefix
    t = s + '#' + s[::-1]
    fail = [0] * len(t)
    j = 0
    for i in range(1, len(t)):
        while j > 0 and t[i] != t[j]:
            j = fail[j-1]
        if t[i] == t[j]:
            j += 1
        fail[i] = j
    # fail[-1] = length of longest palindromic prefix
    return s[fail[-1]:][::-1] + s
```

### Anagram / Permutation Detection — Canonical Patterns

```python
# LC 242 Valid Anagram
def isAnagram(s, t):
    return Counter(s) == Counter(t)

# LC 438 Find All Anagrams in String (Sliding Window)
def findAnagrams(s, p):
    need = Counter(p)
    window = Counter()
    result = []
    for i, c in enumerate(s):
        window[c] += 1
        if i >= len(p):
            left = s[i - len(p)]
            window[left] -= 1
            if window[left] == 0: del window[left]
        if window == need:
            result.append(i - len(p) + 1)
    return result

# LC 49 Group Anagrams (sorted key)
def groupAnagrams(strs):
    from collections import defaultdict
    groups = defaultdict(list)
    for s in strs:
        groups[tuple(sorted(s))].append(s)
    return list(groups.values())
```

### String DP Patterns

```python
# LC 72 Edit Distance — classic 2D DP
def minDistance(word1, word2):
    m, n = len(word1), len(word2)
    dp = list(range(n + 1))
    for i in range(1, m + 1):
        prev = dp[:]
        dp[0] = i
        for j in range(1, n + 1):
            if word1[i-1] == word2[j-1]:
                dp[j] = prev[j-1]
            else:
                dp[j] = 1 + min(prev[j], dp[j-1], prev[j-1])
    return dp[n]

# LC 1143 Longest Common Subsequence
def longestCommonSubsequence(text1, text2):
    m, n = len(text1), len(text2)
    dp = [0] * (n + 1)
    for c in text1:
        prev, dp = dp[:], [0] * (n + 1)
        for j, d in enumerate(text2):
            dp[j+1] = prev[j] + 1 if c == d else max(prev[j+1], dp[j])
    return dp[n]
```

### Interview tips — strings
| Signal | Pattern |
|--------|---------|
| "find pattern in text efficiently" | KMP or Z-algorithm |
| "find all anagrams/permutations" | Sliding window + Counter |
| "group by same characters" | Sort chars as key (Group Anagrams) |
| "longest palindromic substring" | Expand from center or Manacher |
| "shortest palindrome by prepending" | KMP on `s + '#' + reverse(s)` |
| "edit distance, LCS" | 2D DP → space-optimize to 1D |
| "repeated substrings" | Rolling hash or suffix array |
| "is rotation of another string" | `s in (t+t)` |

## Additional High-Frequency String Problems (Reference)

No new template — each is a one-line idea, but they show up constantly.

| Problem | LC # | One-line idea | Difficulty |
|---------|------|---------------|------------|
| Longest Common Prefix | 14 | Vertical scan: compare column `i` across all words, stop at first mismatch | Easy |
| Isomorphic Strings | 205 | Two maps (`s→t` **and** `t→s`) — a single map wrongly accepts `"ab" → "aa"` | Easy |
| Ransom Note | 383 | Char-count of magazine, decrement per note char, fail on negative | Easy |
| Most Common Word | 819 | Lowercase + split on non-letters, skip banned set, take max count | Easy |
| Reorder Data in Log Files | 937 | `split(" ", 2)` → id + body; custom comparator: letter-logs by (body, id), digit-logs keep original order (stable sort) | Medium |
| Bulls and Cows | 299 | One pass: equal chars → bulls; else bump two count arrays, cows = `sum(min(cntS[d], cntG[d]))` | Medium |