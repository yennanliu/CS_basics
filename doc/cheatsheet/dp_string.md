# String DP (Two-Sequence Grid Patterns)

> **Scope** — DP over one or two strings: the `dp[m+1][n+1]` two-sequence grid, prefix-based (1-indexed) table design, and the worked patterns for LCS, edit distance, interleaving and wildcard/parenthesis matching.
> **See also**: [dp.md](./dp.md) — the Edit Distance and LCS templates themselves; [palindrome.md](./palindrome.md) — palindrome-specific DP; [string_matching_kmp_rolling_hash.md](./string_matching_kmp_rolling_hash.md) — non-DP substring search.

## LeetCode Problem Lists

- [Dynamic Programming](https://leetcode.com/problem-list/dynamic-programming/)

## Overview

### Key Properties

- **Complexity**: `O(m * n)` time and space for the full table; `O(min(m, n))` space once rolled
  down to two rows.
- **Core Idea**: `dp[i][j]` = the answer for **prefix `s1[:i]` and prefix `s2[:j]`**. Every problem
  in this family is the same grid — only the transition on match / mismatch changes.
- **When to Use**: two strings (or a string and a pattern) compared position by position, asking for
  a longest / minimum-cost / count / yes-no answer.

### Problem Categories

| Category | Question | LC |
|----------|----------|----|
| **Longest common** | how much do the two share? | 1143, 583, 712 |
| **Transform cost** | cheapest edit script from `s1` to `s2`? | 72, 161 |
| **Counting paths** | how many ways does `s2` appear inside `s1`? | 115 |
| **Feasibility** | can these two interleave / match this pattern? | 97, 10, 44 |
| **Single-string wildcard** | is this bracket string valid with `*`? | 678 |

### References

- [dp.md](./dp.md) — the Edit Distance (LC 72) and LCS (LC 1143) templates
- [palindrome.md](./palindrome.md) — the single-string palindrome DP family

## Templates & Algorithms

### The Two-String Grid

#### **The "Two-String / Two-Sequence Grid" Pattern** 🧩

This is one of the most important DP patterns for string problems. Once you recognize this pattern, a whole class of problems becomes much easier to solve.

**Core Structure:**
- Create a 2D array `dp[m+1][n+1]` where:
  - **Rows (i)**: Represent the prefix of String A (first i characters)
  - **Columns (j)**: Represent the prefix of String B (first j characters)
  - **Cell `dp[i][j]`**: Stores the answer for those two specific prefixes

**Grid Movements (How to Choose the Move):**

Think of the grid as a game where you move from `(0,0)` to `(m,n)`:

1. **Diagonal Move (`dp[i-1][j-1]`)**: You "use" or "match" a character from **both** strings simultaneously
2. **Vertical Move (`dp[i-1][j]`)**: You "skip" or "delete" a character from String A
3. **Horizontal Move (`dp[i][j-1]`)**: You "skip" or "insert" a character from String B

**Pattern Comparison Table:**

| Problem | Goal | Match Logic (`s1[i-1] == s2[j-1]`) | Mismatch Logic | Key Insight |
|---------|------|-----------------------------------|----------------|-------------|
| **LC 1143: LCS** | Longest common length | `1 + dp[i-1][j-1]` (Diagonal + 1) | `max(dp[i-1][j], dp[i][j-1])` | Take diagonal when match, else max of skip either string |
| **LC 97: Interleaving String** | Can s3 interleave s1+s2? | `dp[i-1][j] \|\| dp[i][j-1]` | `false` | Check if we can form by taking from either string |
| **LC 115: Distinct Subsequences** | Count occurrences | `dp[i-1][j-1] + dp[i-1][j]` | `dp[i-1][j]` | Can either use match or skip s char |
| **LC 72: Edit Distance** | Min edits to match | `dp[i-1][j-1]` (No cost) | `1 + min(top, left, diagonal)` | No operation needed if match, else try all 3 operations |
| **LC 583: Delete Operation** | Min deletions to make equal | `dp[i-1][j-1]` | `1 + min(dp[i-1][j], dp[i][j-1])` | Delete from either string |
| **LC 712: Min ASCII Delete Sum** | Min ASCII sum to make equal | `dp[i-1][j-1]` | `min(dp[i-1][j] + s1[i], dp[i][j-1] + s2[j])` | Track ASCII costs |

**The "Empty String" Base Case Pattern** 💡

This is **THE MOST IMPORTANT** pattern in Two-String DP:

* `dp[0][0]`: State where both strings are empty (usually `0` or `true`)
* First row `dp[0][j]`: String A is empty, only String B has characters
* First column `dp[i][0]`: String B is empty, only String A has characters

**Why `m+1` and `n+1`?**
- The `+1` gives us room for the "empty string" base case
- Without this, transitions like `dp[i-1][j]` would crash on the first character
- `dp[i][j]` represents using the first `i` characters of string 1 and first `j` characters of string 2

**Universal Template:**
```java
// java
// IDEA: the shared skeleton of every two-sequence grid DP
// time = O(m * n), space = O(m * n)
public int stringDP(String s1, String s2) {
    int m = s1.length(), n = s2.length();
    int[][] dp = new int[m + 1][n + 1];

    // Step 1: Initialize base cases (empty string states)
    dp[0][0] = 0; // Both strings empty

    // Initialize first row (s1 is empty)
    for (int j = 1; j <= n; j++) {
        dp[0][j] = initValueForEmptyS1(j);
    }

    // Initialize first column (s2 is empty)
    for (int i = 1; i <= m; i++) {
        dp[i][0] = initValueForEmptyS2(i);
    }

    // Step 2: Fill the DP table
    for (int i = 1; i <= m; i++) {
        for (int j = 1; j <= n; j++) {
            // NOTE: Use i-1 and j-1 to access string characters
            if (s1.charAt(i-1) == s2.charAt(j-1)) {
                // Characters match
                dp[i][j] = transitionOnMatch(dp, i, j);
            } else {
                // Characters don't match
                dp[i][j] = transitionOnMismatch(dp, i, j);
            }
        }
    }

    return dp[m][n];
}
```

**Space Optimization Secret** ⚡

In every "Two-String" problem, you only ever look at:
- The **current row** (`dp[i][j]`)
- The **row above** (`dp[i-1][j]`)
- The **diagonal** (`dp[i-1][j-1]`)

This means you can **always reduce space from O(m×n) to O(n)** by using:
1. A 1D array for the previous row
2. A variable to store the diagonal value
3. Rolling updates as you process each row

**Example Space-Optimized LCS:**
```java
// java
// LC 1143 - Longest Common Subsequence
// IDEA: diagonal + 1 on a match, else the better of dropping one character
// time = O(m * n), space = O(m * n)
public int longestCommonSubsequence(String s1, String s2) {
    int m = s1.length(), n = s2.length();
    int[] prev = new int[n + 1];

    for (int i = 1; i <= m; i++) {
        int[] curr = new int[n + 1];
        for (int j = 1; j <= n; j++) {
            if (s1.charAt(i-1) == s2.charAt(j-1)) {
                curr[j] = prev[j-1] + 1; // Diagonal
            } else {
                curr[j] = Math.max(prev[j], curr[j-1]); // Top or left
            }
        }
        prev = curr; // Roll forward
    }

    return prev[n];
}
```

---

### **Deep Dive: The Prefix-Based Indexing Pattern (LCS & Variants)** 🔍

This subsection focuses on understanding the **1-indexed DP table** concept that's critical for getting string DP right.

#### **Why 1-Indexed DP Table?**

When building a 2D DP table for string problems, we use `dp[m+1][n+1]` instead of `dp[m][n]`. This might seem like off-by-one overhead, but it's actually elegant:

**The Key Insight**: `dp[i][j]` represents the **answer for a prefix of length i from string1 and prefix of length j from string2**.

```text
✅ CORRECT: 1-indexed approach
   dp[i][j] = answer for string1.substring(0, i) and string2.substring(0, j)
   - Row index i ∈ [0, m] where m = string1.length()
   - Col index j ∈ [0, n] where n = string2.length()
   - Character comparison: string1.charAt(i-1) vs string2.charAt(j-1)

❌ WRONG: 0-indexed approach (will cause boundary issues)
   - No room for "empty string" base case
   - First iteration accesses negative indices
```

#### **The Prefix Concept: Why dp[i-1] and dp[j-1] for Characters**

```text
Example: string1 = "abcde", string2 = "ace"

When i=3, j=2 (processing prefixes "abc" and "ac"):
  - DP state represents: LCS("abc", "ac")
  - We compare: string1.charAt(3-1) = 'c' with string2.charAt(2-1) = 'c'
  - The characters at POSITION (i-1) and (j-1) are what define the LAST character of each prefix

Index Mapping:
  i=0: prefix length 0 (empty string)
  i=1: prefix length 1 (first 1 char) → access string1[0]
  i=2: prefix length 2 (first 2 chars) → access string1[1]
  i=3: prefix length 3 (first 3 chars) → access string1[2]
  ...
  Therefore: when at dp[i][j], compare string1[i-1] with string2[j-1]
```

#### **The Three-Way Transition Logic (Using LCS as Example)**

```java
// java
// IDEA: the match / mismatch branch, isolated
// Pattern: Two cases only
if (string1.charAt(i - 1) == string2.charAt(j - 1)) {
    // CASE 1: Characters match → extend previous best result
    // The matching characters contribute +1 to the LCS length
    dp[i][j] = 1 + dp[i - 1][j - 1];  // Diagonal: both strings move forward
} else {
    // CASE 2: Characters don't match → take best of skipping either string
    // We have two choices:
    //   Option A: Skip current char from string1 → dp[i-1][j]
    //   Option B: Skip current char from string2 → dp[i][j-1]
    // Take whichever gives the better result
    dp[i][j] = Math.max(dp[i - 1][j],    // Skip from string1
                       dp[i][j - 1]);    // Skip from string2
}
```

**Why This Works:**
- **Diagonal (dp[i-1][j-1])**: When characters match, we're "using" both characters to build our answer. We take the best result from the shorter prefixes and add 1.
- **Vertical (dp[i-1][j])**: When characters don't match, we skip the current character from string1 and see if we can still find a good LCS with string2.
- **Horizontal (dp[i][j-1])**: Alternatively, skip from string2 and see if we can find a good LCS with string1.

#### **Complete LCS Example with Grid**

```text
string1 = "abcde"
string2 = "ace"

DP Grid (showing values):
        ""  a   c   e
    ""  0   0   0   0
    a   0   1   1   1
    b   0   1   1   1
    c   0   1   2   2
    d   0   1   2   2
    e   0   1   2   3

How to read:
  dp[4][2] = 2 means LCS("abcd", "ac") has length 2
  dp[5][3] = 3 means LCS("abcde", "ace") has length 3 ✓

Key moments:
  - dp[1][1]: Compare 'a' with 'a' → match! → dp[0][0] + 1 = 1
  - dp[2][1]: Compare 'b' with 'a' → no match → max(dp[1][1], dp[2][0]) = 1
  - dp[3][2]: Compare 'c' with 'c' → match! → dp[2][1] + 1 = 2
  - dp[5][3]: Compare 'e' with 'e' → match! → dp[4][2] + 1 = 3
```

#### **Java Implementation Pattern**

```java
// java
// LC 1143 - Longest Common Subsequence
// IDEA: same recurrence, written as the canonical 1-indexed table
// time = O(m * n), space = O(m * n)
public int longestCommonSubsequence(String text1, String text2) {
    int m = text1.length();
    int n = text2.length();

    // Create (m+1) × (n+1) table to handle "empty string" base case
    int[][] dp = new int[m + 1][n + 1];
    // dp[0][j] and dp[i][0] are already 0 (empty string has LCS of 0)

    // Loop uses 1-based indices to represent prefix lengths
    for (int i = 1; i <= m; i++) {
        for (int j = 1; j <= n; j++) {
            // i represents prefix length in text1
            // j represents prefix length in text2
            // Compare character at position i-1 and j-1 (0-indexed)
            if (text1.charAt(i - 1) == text2.charAt(j - 1)) {
                // Characters match: extend the best previous result
                dp[i][j] = 1 + dp[i - 1][j - 1];
            } else {
                // Characters don't match: choose best by skipping either string
                dp[i][j] = Math.max(dp[i - 1][j],    // Skip from text1
                                   dp[i][j - 1]);    // Skip from text2
            }
        }
    }

    return dp[m][n];  // Answer for full strings
}
```

#### **When to Use This Pattern** 📋

Use the **1-indexed prefix-based 2D DP** when:

| Condition | Example Problems |
|-----------|------------------|
| Two strings/sequences as input | LC 1143 (LCS), LC 72 (Edit Distance) |
| Answer depends on comparing prefixes character-by-character | LC 583 (Delete Ops), LC 712 (Min ASCII Delete) |
| Three-way transitions (match/skip1/skip2) or two-way transitions | LC 1143, 97, 115 |
| Need to handle "empty string" as base case | All Two-String DP problems |

#### **Similar LeetCode Problems Using This Pattern**

| Problem | Goal | Match Case | Mismatch Case | Complexity |
|---------|------|-----------|----------------|-----------|
| **LC 1143: LCS** | Length of longest common subsequence | `1 + dp[i-1][j-1]` | `max(dp[i-1][j], dp[i][j-1])` | O(m×n) |
| **LC 72: Edit Distance** | Min operations to transform | `dp[i-1][j-1]` (no cost) | `1 + min(dp[i-1][j], dp[i][j-1], dp[i-1][j-1])` | O(m×n) |
| **LC 583: Delete Operation** | Min deletions to make equal | `dp[i-1][j-1]` | `1 + min(dp[i-1][j], dp[i][j-1])` | O(m×n) |
| **LC 97: Interleaving String** | Can s3 be interleaved from s1+s2? | `dp[i-1][j] \|\| dp[i][j-1]` | `false` | O(m×n) |
| **LC 115: Distinct Subsequences** | Count occurrences of target `s2` as a subsequence of source `s1` | `dp[i-1][j-1] + dp[i-1][j]` | `dp[i-1][j]` | O(m×n) |
| **LC 712: Min ASCII Delete Sum** | Min cost to make strings equal | `dp[i-1][j-1]` | `min(dp[i-1][j] + cost1, dp[i][j-1] + cost2)` | O(m×n) |

#### **Common Pitfalls** ⚠️

1. **Using 0-indexed DP directly** → Causes negative index access, no room for empty string
2. **Comparing `string[i]` instead of `string[i-1]`** → Off-by-one error in character comparison
3. **Forgetting to initialize first row/column** → Some problems need special initialization
4. **Wrong transition for mismatch case** → Must match your specific problem's logic

---

**Quick Recognition Checklist** ✅

Use "Two-String Grid" pattern when you see:
- [ ] Two strings/sequences as input
- [ ] Need to compare characters from both strings
- [ ] Answer depends on prefixes (first i chars of s1, first j chars of s2)
- [ ] Keywords: "common", "matching", "transform", "interleaving", "subsequence"

**Common Problems:**
- LC 1143 (LCS) - Find longest common subsequence
- LC 72 (Edit Distance) - Minimum edits to transform
- LC 97 (Interleaving String) - Can s3 be formed by interleaving?
- LC 115 (Distinct Subsequences) - Count occurrences
- LC 583 (Delete Operation) - Min deletions to make equal
- LC 712 (Min ASCII Delete Sum) - Min ASCII cost to make equal
- LC 10 (Regular Expression Matching) - Pattern matching with * and .
- LC 44 (Wildcard Matching) - Pattern matching with * and ?

---

### **Classic String DP Patterns (Detailed)**

| Problem Type | Pattern | Complexity | Notes |
|--------------|---------|------------|-------|
| **Edit Distance** | dp[i][j] = operations to transform s1[:i] to s2[:j] | O(m×n) | Insert/Delete/Replace |
| **LCS** | dp[i][j] = LCS length of s1[:i] and s2[:j] | O(m×n) | Two sequences; diagonal on match |
| **LIS** | dp[i] = longest increasing subsequence ending at i | O(n²) | **One** sequence — not this grid; O(n log n) with patience sorting |
| **Palindrome** | dp[i][j] = is s[i:j+1] palindrome | O(n²) | Expand around centers |
| **Word Break** | dp[i] = can break s[:i] | O(n³) | Check all possible breaks |

## LC Examples

### **Interleaving String Pattern (LC 97)** 🧩

**Pattern**: Two-String Grid DP (Boolean)

**Core Idea**: Given three strings `s1`, `s2`, `s3`, determine if `s3` is formed by interleaving `s1` and `s2` while preserving relative order. Think of it as finding a path from `(0,0)` to `(m,n)` in a 2D grid where moving **down** takes a char from `s1` and moving **right** takes a char from `s2`.

**DP Definition**:
- `dp[i][j]` = can `s1[0..i-1]` and `s2[0..j-1]` form `s3[0..i+j-1]`?

**Key Formula**:
```text
dp[i][j] = (dp[i-1][j] && s1[i-1] == s3[i+j-1])   // take from s1
         || (dp[i][j-1] && s2[j-1] == s3[i+j-1])   // take from s2
```

**Base Cases**:
- `dp[0][0] = true` (empty + empty = empty)
- First column: `dp[i][0] = dp[i-1][0] && s1[i-1] == s3[i-1]` (only s1 contributes)
- First row: `dp[0][j] = dp[0][j-1] && s2[j-1] == s3[j-1]` (only s2 contributes)

**Early Exit**: If `len(s1) + len(s2) != len(s3)`, return `false`.

**Space Optimization**: Can reduce to 1D `dp[n+1]` array since each row only depends on current and previous row.

| Approach | Time | Space |
|----------|------|-------|
| 2D DP | O(m×n) | O(m×n) |
| 1D DP (space optimized) | O(m×n) | O(min(m,n)) |
| Top-down memoization | O(m×n) | O(m×n) |
| Brute force recursion | O(2^(m+n)) | O(m+n) |

**Similar LeetCode Problems**:

| Problem | Similarity | Key Difference |
|---------|-----------|----------------|
| LC 1143 (LCS) | Two-string grid, prefix comparison | Maximizes length instead of boolean check |
| LC 72 (Edit Distance) | Two-string grid, 3 transitions | Minimizes cost instead of boolean feasibility |
| LC 115 (Distinct Subsequences) | Two-string grid, counting paths | Counts number of ways instead of yes/no |
| LC 583 (Delete Operation for Two Strings) | Two-string grid | Minimizes deletions |
| LC 44 (Wildcard Matching) | Two-string boolean DP grid | Pattern matching with `*` and `?` |
| LC 10 (Regular Expression Matching) | Two-string boolean DP grid | Pattern matching with `*` and `.` |

**Reference**: See `leetcode_java/src/main/java/LeetCodeJava/DynamicProgramming/InterleavingString.java` for 2D DP, 1D DP, top-down, and bottom-up implementations.

---

### **Valid Parenthesis String Pattern (LC 678)** 🌟

**Problem**: Given a string containing '(', ')' and '*', where '*' can be treated as '(', ')' or empty string, determine if the string is valid.

This problem demonstrates **multiple DP paradigms** and is excellent for understanding:
- State tracking with wildcards
- Greedy vs DP trade-offs
- Interval DP patterns
- Space optimization techniques

#### **Approach 1: Greedy (Min/Max Balance Tracking)** ⚡ OPTIMAL

**Time**: O(n) | **Space**: O(1)

**Key Insight**: Track the **range** of possible unmatched open parentheses at each position.

```java
// java
// LC 678 - Valid Parenthesis String
// IDEA: greedy — carry the min/max possible open count instead of a DP table
// time = O(n), space = O(1)
public boolean checkValidString(String s) {
    int minParenCnt = 0; // minimum possible unmatched '('
    int maxParenCnt = 0; // maximum possible unmatched '('

    for (char c : s.toCharArray()) {
        if (c == '(') {
            minParenCnt++;
            maxParenCnt++;
        } else if (c == ')') {
            minParenCnt--;
            maxParenCnt--;
        } else { // '*' - wildcard
            minParenCnt--; // treat '*' as ')'
            maxParenCnt++; // treat '*' as '('
        }

        // If maxParenCnt < 0: too many unmatched ')'
        if (maxParenCnt < 0) return false;

        // If minParenCnt < 0: reset to 0 (can use '*' as empty)
        if (minParenCnt < 0) minParenCnt = 0;
    }

    // Valid if we can have 0 unmatched '('
    return minParenCnt == 0;
}
```

**Why this works**:
- `maxParenCnt < 0` → impossible to balance (too many ')')
- `minParenCnt = 0` → can reset negative balance using '*' as empty
- Final `minParenCnt == 0` → at least one valid way to match all

---

#### **Approach 2: 2D DP (Position × Open Count)** 📊

**Time**: O(n²) | **Space**: O(n²)

**DP Definition**:
- `dp[i][j]`: Can we have exactly `j` unmatched '(' after processing first `i` characters?

```java
// java
// LC 678 - Valid Parenthesis String
// IDEA: dp[i][open] = can s[i:] close with `open` brackets already outstanding
// time = O(n^2), space = O(n^2)
public boolean checkValidString(String s) {
    int n = s.length();
    boolean[][] dp = new boolean[n + 1][n + 1];
    dp[0][0] = true; // empty string, 0 open parens

    for (int i = 1; i <= n; i++) {
        char c = s.charAt(i - 1);
        for (int j = 0; j <= n; j++) {
            if (c == '(') {
                // Add one open paren
                if (j > 0) dp[i][j] = dp[i - 1][j - 1];
            } else if (c == ')') {
                // Close one open paren
                if (j < n) dp[i][j] = dp[i - 1][j + 1];
            } else { // '*'
                // Option 1: treat '*' as empty
                dp[i][j] = dp[i - 1][j];
                // Option 2: treat '*' as '('
                if (j > 0) dp[i][j] |= dp[i - 1][j - 1];
                // Option 3: treat '*' as ')'
                if (j < n) dp[i][j] |= dp[i - 1][j + 1];
            }
        }
    }

    return dp[n][0]; // n chars processed, 0 open parens
}
```

**State Transitions**:
- `'('`: `dp[i][j] = dp[i-1][j-1]` (increase open count)
- `')'`: `dp[i][j] = dp[i-1][j+1]` (decrease open count)
- `'*'`: `dp[i][j] = dp[i-1][j] || dp[i-1][j-1] || dp[i-1][j+1]` (try all 3)

---

#### **Approach 3: Interval DP (Range Validity)** 🎯

**Time**: O(n³) | **Space**: O(n²)

**DP Definition**:
- `dp[i][j]`: Is substring `s[i..j]` valid?

```java
// java
// LC 678 - Valid Parenthesis String
// IDEA: interval DP — dp[i][j] = is s[i..j] a valid string on its own
// time = O(n^3), space = O(n^2)
public boolean checkValidString(String s) {
    int n = s.length();
    if (n == 0) return true;

    boolean[][] dp = new boolean[n][n];

    // Base case: single character valid only if '*'
    for (int i = 0; i < n; i++) {
        if (s.charAt(i) == '*') dp[i][i] = true;
    }

    // Fill table for increasing lengths
    for (int len = 2; len <= n; len++) {
        for (int i = 0; i <= n - len; i++) {
            int j = i + len - 1;

            // Option A: s[i] and s[j] form a matching pair
            if ((s.charAt(i) == '(' || s.charAt(i) == '*') &&
                (s.charAt(j) == ')' || s.charAt(j) == '*')) {
                if (len == 2 || dp[i + 1][j - 1]) {
                    dp[i][j] = true;
                }
            }

            // Option B: Split at some point k
            if (!dp[i][j]) {
                for (int k = i; k < j; k++) {
                    if (dp[i][k] && dp[k + 1][j]) {
                        dp[i][j] = true;
                        break;
                    }
                }
            }
        }
    }

    return dp[0][n - 1];
}
```

**Key Pattern**: This is classic **Interval DP** similar to:
- LC 312 (Burst Balloons)
- LC 1039 (Minimum Score Triangulation)
- LC 1547 (Minimum Cost to Cut a Stick)

---

#### **Approach 4: Top-Down DP (Recursion + Memoization)** 🔄

**Time**: O(n²) | **Space**: O(n²) + recursion stack

```java
// java
// LC 678 - Valid Parenthesis String
// IDEA: top-down recursion on (index, open) with memoisation
// time = O(n^2), space = O(n^2)
public boolean checkValidString(String s) {
    int n = s.length();
    Boolean[][] memo = new Boolean[n + 1][n + 1];
    return dfs(0, 0, s, memo);
}

private boolean dfs(int i, int open, String s, Boolean[][] memo) {
    // Too many closing parens
    if (open < 0) return false;

    // End of string: valid if all matched
    if (i == s.length()) return open == 0;

    // Memoization
    if (memo[i][open] != null) return memo[i][open];

    boolean result;
    if (s.charAt(i) == '(') {
        result = dfs(i + 1, open + 1, s, memo);
    } else if (s.charAt(i) == ')') {
        result = dfs(i + 1, open - 1, s, memo);
    } else { // '*'
        result = dfs(i + 1, open, s, memo) ||        // empty
                 dfs(i + 1, open + 1, s, memo) ||    // '('
                 dfs(i + 1, open - 1, s, memo);      // ')'
    }

    memo[i][open] = result;
    return result;
}
```

---

#### **Approach 5: Bottom-Up DP** 📈

**Time**: O(n²) | **Space**: O(n²)

```java
// java
// LC 678 - Valid Parenthesis String
// IDEA: same states as approach 4, filled bottom-up
// time = O(n^2), space = O(n^2)
public boolean checkValidString(String s) {
    int n = s.length();
    boolean[][] dp = new boolean[n + 1][n + 1];
    dp[n][0] = true; // base: end with 0 open parens

    for (int i = n - 1; i >= 0; i--) {
        for (int open = 0; open < n; open++) {
            boolean res = false;
            if (s.charAt(i) == '*') {
                res |= dp[i + 1][open + 1];           // treat as '('
                if (open > 0) res |= dp[i + 1][open - 1]; // treat as ')'
                res |= dp[i + 1][open];                // treat as empty
            } else {
                if (s.charAt(i) == '(') {
                    res |= dp[i + 1][open + 1];
                } else if (open > 0) {
                    res |= dp[i + 1][open - 1];
                }
            }
            dp[i][open] = res;
        }
    }
    return dp[0][0];
}
```

---

#### **Approach 6: Space-Optimized DP** ⚡

**Time**: O(n²) | **Space**: O(n)

```java
// java
// LC 678 - Valid Parenthesis String
// IDEA: bottom-up rolled down to one row over `open`
// time = O(n^2), space = O(n)
public boolean checkValidString(String s) {
    int n = s.length();
    boolean[] dp = new boolean[n + 1];
    dp[0] = true;

    for (int i = n - 1; i >= 0; i--) {
        boolean[] newDp = new boolean[n + 1];
        for (int open = 0; open < n; open++) {
            if (s.charAt(i) == '*') {
                newDp[open] = dp[open + 1] ||
                              (open > 0 && dp[open - 1]) ||
                              dp[open];
            } else if (s.charAt(i) == '(') {
                newDp[open] = dp[open + 1];
            } else if (open > 0) {
                newDp[open] = dp[open - 1];
            }
        }
        dp = newDp;
    }
    return dp[0];
}
```

**Space Optimization Technique**: Rolling array - only keep current and previous row.

---

#### **Approach 7: Stack-Based (Two Stacks)** 📚

**Time**: O(n) | **Space**: O(n)

```java
// java
// LC 678 - Valid Parenthesis String
// IDEA: two stacks — one of '(' indices, one of '*' indices; match leftovers by position
// time = O(n), space = O(n)
public boolean checkValidString(String s) {
    Stack<Integer> leftStack = new Stack<>();  // indices of '('
    Stack<Integer> starStack = new Stack<>();  // indices of '*'

    // First pass: match ')' with '(' or '*'
    for (int i = 0; i < s.length(); i++) {
        char ch = s.charAt(i);
        if (ch == '(') {
            leftStack.push(i);
        } else if (ch == '*') {
            starStack.push(i);
        } else { // ')'
            if (!leftStack.isEmpty()) {
                leftStack.pop();
            } else if (!starStack.isEmpty()) {
                starStack.pop();
            } else {
                return false; // unmatched ')'
            }
        }
    }

    // Second pass: match remaining '(' with '*'
    while (!leftStack.isEmpty() && !starStack.isEmpty()) {
        // '*' must come after '(' to be valid
        if (leftStack.pop() > starStack.pop()) {
            return false;
        }
    }

    return leftStack.isEmpty();
}
```

**Key Insight**: Store **indices** to ensure '*' comes after '(' when used as ')'.

---

#### **Pattern Comparison Summary**

| Approach | Time | Space | Best For | Trade-offs |
|----------|------|-------|----------|------------|
| **Greedy (min/max)** | O(n) | O(1) | Production code | Hardest to understand initially |
| **2D DP (pos × count)** | O(n²) | O(n²) | Learning state transitions | Space-heavy but intuitive |
| **Interval DP** | O(n³) | O(n²) | Understanding range problems | Slowest but shows interval pattern |
| **Top-Down DP** | O(n²) | O(n²) | Natural recursion thinkers | Stack overhead |
| **Bottom-Up DP** | O(n²) | O(n²) | Avoiding recursion | Requires reverse thinking |
| **Space-Optimized** | O(n²) | O(n) | Memory-constrained | More complex implementation |
| **Stack-Based** | O(n) | O(n) | Index-tracking insight | Two-pass algorithm |

#### **Key Takeaways** 💡

1. **Greedy is optimal** for this problem - recognizing when greedy works is crucial
2. **Wildcard handling**: Always consider all possibilities ('(', ')', empty)
3. **Balance tracking**: Many paren problems reduce to tracking open count
4. **Index matters**: When wildcards can be different things, position matters (stack approach)
5. **Multiple paradigms**: Same problem solvable with interval DP, state DP, greedy, and stacks

#### **Related Problems**
- LC 20 (Valid Parentheses) - simpler version without '*'
- LC 32 (Longest Valid Parentheses) - find longest valid substring
- LC 301 (Remove Invalid Parentheses) - remove minimum to make valid
- LC 921 (Minimum Add to Make Parentheses Valid) - min additions needed

**Reference**: `leetcode_java/src/main/java/LeetCodeJava/String/ValidParenthesisString.java`

---

## Summary

| Question the problem asks | dp[i][j] holds | On match | On mismatch |
|---|---|---|---|
| longest shared run | LCS length | `1 + dp[i-1][j-1]` | `max(dp[i-1][j], dp[i][j-1])` |
| cheapest rewrite | edit cost | `dp[i-1][j-1]` | `1 + min(3 neighbours)` |
| how many embeddings | count of ways | `dp[i-1][j-1] + dp[i-1][j]` | `dp[i-1][j]` |
| is it possible at all | boolean | OR of the legal moves | `False` |

**The three rules that prevent most bugs**

1. Size the table `dp[m+1][n+1]` and read characters as `s1[i-1]` / `s2[j-1]` — row/column `0` is
   the empty prefix, which is what makes the base cases writable.
2. Fill row `0` and column `0` *before* the main loops; they encode "match against nothing".
3. The answer is `dp[m][n]`, never `dp[m-1][n-1]`.
