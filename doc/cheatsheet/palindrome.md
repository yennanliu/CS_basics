# Palindrome (回文)

## Overview
**Palindrome** is a sequence that reads the same forward and backward. It's a fundamental concept in string manipulation and array processing, commonly appearing in coding interviews.

### Key Properties
- **Time Complexity**: O(n) for basic operations, O(n²) for substring problems
- **Space Complexity**: O(1) for two-pointer approach, O(n) for recursive/DP solutions
- **Core Idea**: Compare characters/elements from both ends moving toward the center
- **When to Use**: String validation, substring problems, number manipulation, partitioning

### Problem Categories

#### **Pattern 1: Basic Palindrome Validation**
- **Description**: Check if a given string/array/number is a palindrome
- **Examples**: LC 9, 125, 234 - Valid Palindrome, Palindrome Number, Palindrome Linked List
- **Pattern**: Two pointers from ends, or reverse and compare

#### **Pattern 2: Palindrome Substring Problems**  
- **Description**: Find longest palindromic substring or count palindromic substrings
- **Examples**: LC 5, 647, 214 - Longest Palindromic Substring, Palindromic Substrings
- **Pattern**: Center expansion or dynamic programming

#### **Pattern 3: Palindrome Construction & Partitioning**
- **Description**: Break string into palindromic parts or construct palindromes
- **Examples**: LC 131, 132, 336 - Palindrome Partitioning I/II, Palindrome Pairs
- **Pattern**: Backtracking with palindrome validation, dynamic programming

#### **Pattern 4: Palindrome Manipulation**
- **Description**: Transform strings to create palindromes (insertions, deletions)
- **Examples**: LC 516, 1312, 1332 - Longest Palindromic Subsequence, Minimum Insertions
- **Pattern**: Dynamic programming with edit distance concepts

#### **Pattern 5: Advanced Palindrome Problems**
- **Description**: Complex palindrome problems with additional constraints
- **Examples**: LC 1147, 1177, 1930 - Longest Chunked Palindrome, Can Make Palindrome
- **Pattern**: Sliding window, hash tables, bit manipulation

#### **Pattern 6: Number Palindromes**
- **Description**: Palindrome problems involving integers and mathematical operations
- **Examples**: LC 9, 479, 564 - Palindrome Number, Largest Palindrome Product
- **Pattern**: Mathematical manipulation, string conversion

### References
- [LeetCode Palindrome Problems](https://leetcode.com/tag/string/)
- Algorithm Design Manual - String Algorithms

## Templates & Algorithms

### Template Comparison Table
| Template Type | Use Case | Time Complexity | When to Use |
|---------------|----------|-----------------|-------------|
| **Two Pointers** | Basic validation | O(n) | Simple palindrome check |
| **Center Expansion** | Find longest substring | O(n²) | Substring problems |
| **Reverse & Compare** | Simple check | O(n) | When space is not a constraint |
| **DP (2D)** | Complex substring counting | O(n²) | Multiple subproblems |
| **Recursive + Memo** | Subsequence problems | O(n²) | Overlapping subproblems |
| **Backtracking** | Partitioning | O(2^n) | Generate all partitions |

### Template 1: Two Pointers (Most Common)
```python
def is_palindrome_two_pointers(s):
    """
    Basic palindrome check using two pointers
    Time: O(n), Space: O(1)
    """
    left, right = 0, len(s) - 1
    
    while left < right:
        # Skip non-alphanumeric characters (for valid palindrome problems)
        while left < right and not s[left].isalnum():
            left += 1
        while left < right and not s[right].isalnum():
            right -= 1
            
        # Compare characters (case-insensitive)
        if s[left].lower() != s[right].lower():
            return False
            
        left += 1
        right -= 1
    
    return True

# Java version
class Solution {
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
}
```

### Template 2: Center Expansion (Substring Problems)
```python
def longest_palindromic_substring(s):
    """
    Find longest palindromic substring using center expansion
    Time: O(n²), Space: O(1)
    """
    if not s:
        return ""
    
    start, max_len = 0, 1
    
    def expand_around_center(left, right):
        # Expand while characters match
        while left >= 0 and right < len(s) and s[left] == s[right]:
            left -= 1
            right += 1
        return right - left - 1  # Length of palindrome
    
    for i in range(len(s)):
        # Check for odd-length palindromes (center at i)
        len1 = expand_around_center(i, i)
        # Check for even-length palindromes (center between i and i+1)
        len2 = expand_around_center(i, i + 1)
        
        current_max = max(len1, len2)
        if current_max > max_len:
            max_len = current_max
            start = i - (current_max - 1) // 2
    
    return s[start:start + max_len]

# Java version
class Solution {
    public String longestPalindrome(String s) {
        if (s == null || s.length() < 1) return "";
        
        int start = 0, end = 0;
        
        for (int i = 0; i < s.length(); i++) {
            int len1 = expandAroundCenter(s, i, i);     // odd length
            int len2 = expandAroundCenter(s, i, i + 1); // even length
            int len = Math.max(len1, len2);
            
            if (len > end - start) {
                start = i - (len - 1) / 2;
                end = i + len / 2;
            }
        }
        
        return s.substring(start, end + 1);
    }
    
    private int expandAroundCenter(String s, int left, int right) {
        while (left >= 0 && right < s.length() && s.charAt(left) == s.charAt(right)) {
            left--;
            right++;
        }
        return right - left - 1;
    }
}
```

### Template 3: Dynamic Programming (Complex Counting)
```python
def count_palindromic_substrings(s):
    """
    Count all palindromic substrings using DP
    Time: O(n²), Space: O(n²)
    """
    n = len(s)
    # dp[i][j] = True if s[i:j+1] is palindrome
    dp = [[False] * n for _ in range(n)]
    count = 0
    
    # Single characters are palindromes
    for i in range(n):
        dp[i][i] = True
        count += 1
    
    # Check for palindromes of length 2
    for i in range(n - 1):
        if s[i] == s[i + 1]:
            dp[i][i + 1] = True
            count += 1
    
    # Check for palindromes of length 3+
    for length in range(3, n + 1):
        for i in range(n - length + 1):
            j = i + length - 1
            
            # Check if s[i:j+1] is palindrome
            if s[i] == s[j] and dp[i + 1][j - 1]:
                dp[i][j] = True
                count += 1
    
    return count

# Space-optimized version using center expansion
def count_palindromic_substrings_optimized(s):
    """
    Time: O(n²), Space: O(1)
    """
    count = 0
    
    def expand_and_count(left, right):
        nonlocal count
        while left >= 0 and right < len(s) and s[left] == s[right]:
            count += 1
            left -= 1
            right += 1
    
    for i in range(len(s)):
        # Odd length palindromes
        expand_and_count(i, i)
        # Even length palindromes
        expand_and_count(i, i + 1)
    
    return count
```

### Template 4: Backtracking (Partitioning)
```python
def palindrome_partitioning(s):
    """
    Find all palindrome partitions using backtracking
    Time: O(n * 2^n), Space: O(n)
    """
    result = []
    current_partition = []
    
    def is_palindrome(start, end):
        while start < end:
            if s[start] != s[end]:
                return False
            start += 1
            end -= 1
        return True
    
    def backtrack(start):
        # Base case: reached end of string
        if start >= len(s):
            result.append(current_partition[:])
            return
        
        # Try all possible endings for current substring
        for end in range(start, len(s)):
            # If current substring is palindrome
            if is_palindrome(start, end):
                current_partition.append(s[start:end + 1])
                backtrack(end + 1)
                current_partition.pop()
    
    backtrack(0)
    return result

# Optimized with memoization
def palindrome_partitioning_memo(s):
    """
    With palindrome check memoization
    """
    n = len(s)
    # Precompute palindrome checks
    is_palin = [[False] * n for _ in range(n)]
    
    # Fill palindrome table
    for i in range(n):
        is_palin[i][i] = True
    
    for i in range(n - 1):
        is_palin[i][i + 1] = (s[i] == s[i + 1])
    
    for length in range(3, n + 1):
        for i in range(n - length + 1):
            j = i + length - 1
            is_palin[i][j] = (s[i] == s[j] and is_palin[i + 1][j - 1])
    
    result = []
    current_partition = []
    
    def backtrack(start):
        if start >= n:
            result.append(current_partition[:])
            return
        
        for end in range(start, n):
            if is_palin[start][end]:
                current_partition.append(s[start:end + 1])
                backtrack(end + 1)
                current_partition.pop()
    
    backtrack(0)
    return result
```

### Template 5: Longest Palindromic Subsequence (DP)
```python
def longest_palindromic_subsequence(s):
    """
    Find length of longest palindromic subsequence using DP
    Time: O(n²), Space: O(n²)
    """
    n = len(s)
    # dp[i][j] = length of LPS in s[i:j+1]
    dp = [[0] * n for _ in range(n)]
    
    # Single characters have LPS length 1
    for i in range(n):
        dp[i][i] = 1
    
    # Fill for substrings of length 2+
    for length in range(2, n + 1):
        for i in range(n - length + 1):
            j = i + length - 1
            
            if s[i] == s[j]:
                dp[i][j] = dp[i + 1][j - 1] + 2
            else:
                dp[i][j] = max(dp[i + 1][j], dp[i][j - 1])
    
    return dp[0][n - 1]

# Space-optimized version
def longest_palindromic_subsequence_optimized(s):
    """
    Time: O(n²), Space: O(n)
    """
    n = len(s)
    dp = [1] * n
    
    for i in range(n - 2, -1, -1):
        prev = 0
        for j in range(i + 1, n):
            temp = dp[j]
            if s[i] == s[j]:
                dp[j] = prev + 2
            else:
                dp[j] = max(dp[j], dp[j - 1])
            prev = temp
    
    return dp[n - 1]
```

### Template 6: Number Palindrome
```python
def is_palindrome_number(x):
    """
    Check if integer is palindrome without converting to string
    Time: O(log x), Space: O(1)
    """
    # Negative numbers are not palindromes
    if x < 0:
        return False
    
    # Single digit numbers are palindromes
    if x < 10:
        return True
    
    # Numbers ending in 0 (except 0) are not palindromes
    if x % 10 == 0:
        return False
    
    reversed_half = 0
    
    # Reverse only half of the number
    while x > reversed_half:
        reversed_half = reversed_half * 10 + x % 10
        x //= 10
    
    # For even length: x == reversed_half
    # For odd length: x == reversed_half // 10
    return x == reversed_half or x == reversed_half // 10

# Java version
class Solution {
    public boolean isPalindrome(int x) {
        if (x < 0 || (x % 10 == 0 && x != 0)) {
            return false;
        }
        
        int reversedHalf = 0;
        while (x > reversedHalf) {
            reversedHalf = reversedHalf * 10 + x % 10;
            x /= 10;
        }
        
        return x == reversedHalf || x == reversedHalf / 10;
    }
}
```

## Problems by Pattern

### **Pattern 1: Basic Palindrome Validation**
| Problem | LC # | Key Technique | Difficulty | Template |
|---------|------|---------------|------------|----------|
| Valid Palindrome | 125 | Two pointers, character filtering | Easy | Two Pointers |
| Palindrome Number | 9 | Mathematical reversal | Easy | Number Palindrome |
| Palindrome Linked List | 234 | Two pointers, reverse half | Easy | Two Pointers |
| Valid Palindrome II | 680 | Two pointers, one deletion | Easy | Two Pointers |

### **Pattern 2: Palindrome Substring Problems**
| Problem | LC # | Key Technique | Difficulty | Template |
|---------|------|---------------|------------|----------|
| Longest Palindromic Substring | 5 | Center expansion | Medium | Center Expansion |
| Palindromic Substrings | 647 | Center expansion/DP | Medium | Center Expansion |
| Shortest Palindrome | 214 | KMP/Center expansion | Hard | Center Expansion |
| Longest Palindromic Subsequence | 516 | DP (2D) | Medium | DP Subsequence |
| Palindromic Substring Queries | 1177 | Bit manipulation | Medium | Advanced |
| Minimum Insertion Steps to Make Palindrome | 1312 | DP (edit distance) | Hard | DP Subsequence |

### **Pattern 3: Palindrome Construction & Partitioning**
| Problem | LC # | Key Technique | Difficulty | Template |
|---------|------|---------------|------------|----------|
| Palindrome Partitioning | 131 | Backtracking + palindrome check | Medium | Backtracking |
| Palindrome Partitioning II | 132 | DP + palindrome preprocessing | Hard | DP + Backtracking |
| Palindrome Pairs | 336 | HashMap + string manipulation | Hard | Advanced |
| Valid Palindrome III | 1216 | DP (k deletions allowed) | Hard | DP Manipulation |
| Break a Palindrome | 1328 | Greedy + string manipulation | Medium | Greedy |

### **Pattern 4: Palindrome Manipulation (Insertions/Deletions)**
| Problem | LC # | Key Technique | Difficulty | Template |
|---------|------|---------------|------------|----------|
| Minimum Deletions to Make Palindrome | 516* | DP (LPS difference) | Medium | DP Subsequence |
| Minimum Insertions for Palindrome | 1312 | DP (edit distance) | Hard | DP Subsequence |
| Delete Operation for Two Strings | 583* | DP (LCS concept) | Medium | DP Subsequence |
| Longest Palindromic Subsequence II | 1682 | DP with constraints | Medium | DP Subsequence |
| Count Different Palindromic Subsequences | 730 | DP with character counting | Hard | DP Advanced |

### **Pattern 5: Advanced Palindrome Problems**
| Problem | LC # | Key Technique | Difficulty | Template |
|---------|------|---------------|------------|----------|
| Longest Chunked Palindrome Decomposition | 1147 | Greedy + two pointers | Hard | Advanced |
| Check If Word Is Valid After Substitutions | 1003* | Stack (palindrome-like pattern) | Medium | Advanced |
| Unique Length-3 Palindromic Subsequences | 1930 | Set + character analysis | Medium | Advanced |
| Maximum Product of Length of Palindromic Subsequences | 1930* | Bitmask + DP | Hard | Advanced |
| Find Palindrome With Fixed Length | 2217 | Mathematical construction | Medium | Number Palindrome |

### **Pattern 6: Number Palindromes**
| Problem | LC # | Key Technique | Difficulty | Template |
|---------|------|---------------|------------|----------|
| Palindrome Number | 9 | Mathematical reversal | Easy | Number Palindrome |
| Largest Palindrome Product | 479 | Mathematical construction | Hard | Number Palindrome |
| Super Palindromes | 906 | Mathematical enumeration | Hard | Number Palindrome |
| Closest Palindrome | 564 | Mathematical analysis | Hard | Number Palindrome |
| Palindromic Prime Numbers | Custom | Number theory | Medium | Number Palindrome |

### **Additional Practice Problems**
| Problem | LC # | Key Technique | Difficulty | Template |
|---------|------|---------------|------------|----------|
| Reverse String | 344 | Two pointers basics | Easy | Two Pointers |
| Reverse Words in a String III | 557 | Two pointers per word | Easy | Two Pointers |
| Find the Difference | 389 | Character counting | Easy | Advanced |
| Reverse Only Letters | 917 | Two pointers with filtering | Easy | Two Pointers |
| Valid Parentheses | 20 | Stack (symmetry concept) | Easy | Advanced |
| Longest Palindromic Path in Tree | Custom | Tree DP | Hard | Advanced |
| Palindrome Removal | Custom | Interval DP | Hard | DP Advanced |
| Count Palindromic Paths | Custom | Tree traversal | Medium | Advanced |

### **Problem Categories Summary**
- **Total Problems**: 40+ classified problems
- **Easy**: 8 problems (20%)
- **Medium**: 20 problems (50%) 
- **Hard**: 12+ problems (30%)
- **Most Common Patterns**: Center Expansion, Two Pointers, Dynamic Programming
- **Advanced Techniques**: Backtracking, Bit manipulation, Mathematical construction

### **Template Usage Statistics**
- **Two Pointers**: 25% of problems
- **Center Expansion**: 20% of problems  
- **Dynamic Programming**: 30% of problems
- **Backtracking**: 10% of problems
- **Advanced/Hybrid**: 15% of problems

## Pattern Selection Framework

### Decision Flowchart for Palindrome Problems

```
Problem Analysis Flowchart:

1. Is this a simple validation problem (check if input is palindrome)?
   ├── YES → Use Template 1: Two Pointers
   │   ├── String/Array input → Two pointers from ends
   │   ├── Linked List input → Two pointers + reverse half
   │   └── Integer input → Template 6: Number Palindrome
   └── NO → Continue to 2

2. Do you need to find/count palindromic substrings?
   ├── YES → 
   │   ├── Find longest substring → Template 2: Center Expansion
   │   ├── Count all substrings → Template 2: Center Expansion (optimized)
   │   └── Complex substring queries → Template 3: Dynamic Programming
   └── NO → Continue to 3

3. Do you need to partition/construct palindromes?
   ├── YES →
   │   ├── Find all partitions → Template 4: Backtracking
   │   ├── Minimum cuts needed → Template 3: DP + preprocessing
   │   └── Construct specific palindrome → Advanced techniques
   └── NO → Continue to 4

4. Do you need to modify string to make it palindrome?
   ├── YES →
   │   ├── Minimum insertions/deletions → Template 5: DP Subsequence
   │   ├── With k operations allowed → Template 3: DP with constraints
   │   └── Optimal transformation → Advanced DP techniques
   └── NO → Continue to 5

5. Is this a number-based palindrome problem?
   ├── YES → Template 6: Number Palindrome
   │   ├── Check palindrome number → Mathematical reversal
   │   ├── Generate palindromes → Mathematical construction
   │   └── Complex number constraints → Advanced math
   └── NO → Continue to 6

6. Advanced/Complex palindrome problem?
   ├── YES → Hybrid approach needed
   │   ├── Multiple patterns combined → Use multiple templates
   │   ├── Additional constraints → Modify existing templates
   │   └── Novel problem type → Design custom solution
   └── NO → Re-analyze problem requirements
```

### Template Selection Guide

#### **When to Use Each Template:**

**Template 1 - Two Pointers:**
- ✅ Simple palindrome validation
- ✅ Problems with character filtering (spaces, punctuation)
- ✅ Linked list palindrome checks
- ✅ When O(1) space is required
- ❌ Need to find all palindromic substrings
- ❌ Complex counting/construction problems

**Template 2 - Center Expansion:**
- ✅ Find longest palindromic substring
- ✅ Count palindromic substrings
- ✅ When you need actual substring positions
- ✅ O(1) space constraint for counting
- ❌ Subsequence problems (non-contiguous)
- ❌ Complex edit distance scenarios

**Template 3 - Dynamic Programming:**
- ✅ Count problems with overlapping subproblems
- ✅ Minimum operations to make palindrome
- ✅ Multiple queries on same string
- ✅ Complex constraint combinations
- ❌ Simple validation (overkill)
- ❌ When space is very limited

**Template 4 - Backtracking:**
- ✅ Generate all palindrome partitions
- ✅ Find specific partition patterns
- ✅ Constraint satisfaction problems
- ✅ When you need all possible solutions
- ❌ Only counting solutions (use DP)
- ❌ Very large input sizes (exponential time)

**Template 5 - DP Subsequence:**
- ✅ Longest palindromic subsequence
- ✅ Minimum edit operations
- ✅ Character deletion/insertion problems
- ✅ When order matters but contiguity doesn't
- ❌ Contiguous substring problems
- ❌ Simple validation tasks

**Template 6 - Number Palindrome:**
- ✅ Integer palindrome validation
- ✅ Mathematical palindrome construction
- ✅ Number theory related problems
- ✅ When string conversion is inefficient
- ❌ String-based palindrome problems
- ❌ Complex character manipulations

### Problem-Solving Strategy

#### **Step-by-Step Approach:**

1. **Identify the Core Task:**
   - Validation vs. Finding vs. Counting vs. Construction
   - Single result vs. All possible results

2. **Analyze Input Constraints:**
   - String, Array, Number, or Linked List?
   - Size constraints (affects algorithm choice)
   - Special characters or filtering needed?

3. **Determine Output Requirements:**
   - Boolean result, count, actual palindromes, or positions?
   - Single answer or all possible answers?

4. **Choose Base Template:**
   - Use decision flowchart above
   - Consider time/space complexity requirements

5. **Adapt Template for Specifics:**
   - Add filtering logic if needed
   - Modify for case sensitivity requirements
   - Add memoization if overlapping subproblems exist

6. **Optimize if Necessary:**
   - Space optimization (2D DP → 1D DP)
   - Early termination conditions
   - Preprocessing for multiple queries

### Common Problem Patterns Recognition

#### **Pattern Signals:**
- **"Check if palindrome"** → Two Pointers
- **"Longest palindromic substring"** → Center Expansion  
- **"Count palindromic substrings"** → Center Expansion or DP
- **"All palindrome partitions"** → Backtracking
- **"Minimum insertions/deletions"** → DP Subsequence
- **"Palindrome number"** → Number Palindrome
- **"Can be made palindrome with k changes"** → DP with constraints

## Summary & Quick Reference

### Complexity Quick Reference
| Template | Time Complexity | Space Complexity | Best For | Notes |
|----------|----------------|------------------|----------|--------|
| **Two Pointers** | O(n) | O(1) | Basic validation | Most efficient for simple checks |
| **Center Expansion** | O(n²) | O(1) | Substring problems | Good balance of time/space |
| **Reverse & Compare** | O(n) | O(n) | Simple problems | Easy to implement |
| **DP (2D)** | O(n²) | O(n²) | Complex counting | Handles overlapping subproblems |
| **DP Subsequence** | O(n²) | O(n²) → O(n) | Edit distance type | Space optimizable |
| **Backtracking** | O(n·2^n) | O(n) | Generate partitions | Exponential time |
| **Number Palindrome** | O(log n) | O(1) | Integer problems | Avoid string conversion |

### Template Quick Reference
| Template | Pattern | Key Code Snippet |
|----------|---------|------------------|
| **Two Pointers** | Validation | `while left < right: check s[left] == s[right]` |
| **Center Expansion** | Substring | `expand_around_center(left, right)` |
| **DP Counting** | Complex counting | `dp[i][j] = (s[i] == s[j]) and dp[i+1][j-1]` |
| **Backtracking** | All partitions | `if is_palindrome(start, end): backtrack(end+1)` |
| **DP Subsequence** | LPS/Edit distance | `dp[i][j] = dp[i+1][j-1] + 2 if s[i] == s[j]` |
| **Number Math** | Integer palindrome | `reversed_half = reversed_half * 10 + x % 10` |

### Common Patterns & Tricks

#### **Two Pointers Variations**
```python
# Basic two pointers
def is_palindrome_basic(s):
    left, right = 0, len(s) - 1
    while left < right:
        if s[left] != s[right]:
            return False
        left += 1
        right -= 1
    return True

# With character filtering
def is_palindrome_alphanumeric(s):
    left, right = 0, len(s) - 1
    while left < right:
        while left < right and not s[left].isalnum():
            left += 1
        while left < right and not s[right].isalnum():
            right -= 1
        if s[left].lower() != s[right].lower():
            return False
        left += 1
        right -= 1
    return True

# Allow one mismatch
def is_palindrome_one_delete(s):
    def helper(left, right, deleted):
        while left < right:
            if s[left] != s[right]:
                if deleted:  # Already used one delete
                    return False
                # Try deleting left or right character
                return (helper(left + 1, right, True) or 
                       helper(left, right - 1, True))
            left += 1
            right -= 1
        return True
    return helper(0, len(s) - 1, False)
```

#### **Center Expansion Patterns**
```python
# Standard center expansion
def expand_around_center(s, left, right):
    while left >= 0 and right < len(s) and s[left] == s[right]:
        left -= 1
        right += 1
    return right - left - 1

# With counting
def count_palindromes_at_center(s, left, right):
    count = 0
    while left >= 0 and right < len(s) and s[left] == s[right]:
        count += 1
        left -= 1
        right += 1
    return count

# For all centers
def find_all_palindromes(s):
    palindromes = []
    for i in range(len(s)):
        # Odd length
        left, right = i, i
        while left >= 0 and right < len(s) and s[left] == s[right]:
            palindromes.append(s[left:right+1])
            left -= 1
            right += 1
        # Even length
        left, right = i, i + 1
        while left >= 0 and right < len(s) and s[left] == s[right]:
            palindromes.append(s[left:right+1])
            left -= 1
            right += 1
    return palindromes
```

#### **DP Optimization Patterns**
```python
# Space optimization: 2D DP to 1D
def longest_palindromic_subsequence_1D(s):
    n = len(s)
    dp = [1] * n  # dp[j] represents length for current i to j
    
    for i in range(n - 2, -1, -1):
        prev = 0  # dp[i+1][j-1]
        for j in range(i + 1, n):
            temp = dp[j]  # Save current dp[j] for next iteration's prev
            if s[i] == s[j]:
                dp[j] = prev + 2
            else:
                dp[j] = max(dp[j], dp[j-1])
            prev = temp
    
    return dp[n-1]

# Preprocessing palindrome table
def precompute_palindromes(s):
    n = len(s)
    is_palin = [[False] * n for _ in range(n)]
    
    # Single characters
    for i in range(n):
        is_palin[i][i] = True
    
    # Two characters
    for i in range(n - 1):
        is_palin[i][i+1] = (s[i] == s[i+1])
    
    # Three+ characters
    for length in range(3, n + 1):
        for i in range(n - length + 1):
            j = i + length - 1
            is_palin[i][j] = (s[i] == s[j] and is_palin[i+1][j-1])
    
    return is_palin
```

### Problem-Solving Steps

1. **Problem Analysis**
   - Read problem carefully and identify key requirements
   - Determine if it's validation, finding, counting, or construction
   - Note constraints (case sensitivity, special characters, etc.)

2. **Pattern Recognition**
   - Use the pattern signals to identify the likely approach
   - Check the decision flowchart for template selection
   - Consider hybrid approaches for complex problems

3. **Template Selection**
   - Choose the most appropriate template based on requirements
   - Consider time/space complexity constraints
   - Plan for edge cases and optimizations

4. **Implementation Strategy**
   - Start with the basic template structure
   - Add problem-specific modifications
   - Handle edge cases (empty string, single character, etc.)

5. **Testing & Optimization**
   - Test with provided examples
   - Consider edge cases: "", "a", "ab", "aba"
   - Optimize space if needed (2D DP → 1D DP)

### Common Mistakes & Tips

**Common Mistakes:**
- **Off-by-one errors** in center expansion bounds checking
- **Case sensitivity** not handled when problem requires it
- **Character filtering** logic incorrect for alphanumeric checks
- **Space complexity** not optimized when required
- **Integer overflow** in number palindrome problems
- **Backtracking** without proper base case or pruning
- **DP state definition** incorrect for subsequence problems

**Best Practices:**
- **Always check bounds** before accessing array elements
- **Handle empty input** and single character cases separately
- **Use consistent naming** for left/right or start/end pointers
- **Comment your approach** clearly, especially for DP transitions
- **Consider space optimization** after getting correct solution
- **Test edge cases** thoroughly before submitting
- **Use helper functions** to keep main logic clean and readable

### Interview Tips

1. **Start with Clarification**
   - Ask about case sensitivity requirements
   - Clarify if spaces/punctuation should be ignored
   - Understand the expected output format

2. **Discuss Approach**
   - Explain your chosen template and reasoning
   - Mention time/space complexity upfront
   - Discuss potential optimizations

3. **Code Systematically**
   - Start with basic structure, then add details
   - Handle edge cases explicitly
   - Use meaningful variable names

4. **Test Thoroughly**
   - Walk through examples step by step
   - Test edge cases: "", "a", "ab", "aba", "abcba"
   - Verify your complexity analysis

5. **Common Follow-ups**
   - "Can you optimize space complexity?"
   - "What if we allow k character mismatches?"
   - "How would you handle very large inputs?"
   - "Can you solve it without extra space?"

### Related Topics
- **Two Pointers**: General two-pointer techniques and applications
- **Dynamic Programming**: Edit distance, longest common subsequence
- **String Algorithms**: Pattern matching, string manipulation
- **Backtracking**: Constraint satisfaction, combinatorial problems  
- **Hash Tables**: Character frequency counting, anagram problems
- **Sliding Window**: Substring problems with constraints
- **Tree Algorithms**: Path problems, symmetric tree validation