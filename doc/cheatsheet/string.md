# String Algorithms & Manipulation

> **Scope** — The everyday string catalogue — character-level two-pointer scans, frequency and anagram signatures, run-length grouping, tokenising, parsing and in-place rewriting — while the worked-solution archive, the language-level string API, palindromes, substring search and two-sequence DP each live in their own sheet.
> **See also** — *split out of this file*: [string_examples.md](./string_examples.md) — the worked LC solution archive; [string_operations.md](./string_operations.md) — the Python/Java string API, `StringBuilder`, char arithmetic and case/Unicode gotchas.
> *Neighbouring sheets*: [palindrome.md](./palindrome.md) — the palindrome family, centre expansion through Manacher; [string_matching_kmp_rolling_hash.md](./string_matching_kmp_rolling_hash.md) — substring search (KMP, Rabin-Karp); [advanced_string_algorithms.md](./advanced_string_algorithms.md) — Z-algorithm, suffix arrays, DFA validation; [dp_string.md](./dp_string.md) — the two-sequence grid family; [sliding_window.md](./sliding_window.md) — condition-driven character windows; [hashing.md](./hashing.md) — frequency maps and canonical keys; [trie.md](./trie.md) — prefix structures.

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
| Template | Use Case | Complexity | Where the code is |
|---|---|---|---|
| **Two-Pointer Scan / Reverse** | Compare or swap from both ends | O(n) | Template 1 |
| **Char Frequency / Anagram Signature** | "same characters?", "group by characters" | O(n) | Template 2 |
| **Run-Length Grouping** | Answer depends on runs of equal chars | O(n) | Template 3 |
| **Parse & Rebuild** | `atoi`, Roman numerals, format rules | O(n) | Template 4 |
| **Greedy Pack + Distribute** | Word wrap, column formatting | O(total chars) | Template 5 |
| **Split + Depth / Token Stack** | Paths, indented trees, logs | O(n) | Template 6 |
| **Mark-then-Rebuild `char[]`** | Delete characters found by index | O(n) | Template 7 |
| **Partition by Last Occurrence** | Maximum number of independent chunks | O(n) | Template 8 |
| **Substring Search** (KMP, Rabin-Karp, Z) | Exact pattern search | O(n+m) | [string_matching_kmp_rolling_hash.md](./string_matching_kmp_rolling_hash.md) |
| **Sliding Window over chars** | Longest / shortest substring with a property | O(n) | [sliding_window.md](./sliding_window.md) |
| **Palindrome** (centre expansion, Manacher) | Palindromic substrings | O(n²) / O(n) | [palindrome.md](./palindrome.md) |
| **Two-Sequence DP** | Edit distance, LCS | O(mn) | [dp_string.md](./dp_string.md) |
| **Trie** | Prefix matching across many words | O(m) | [trie.md](./trie.md) |

### Template 1: Two-Pointer Scan & In-Place Reversal — LC 125, LC 344 ⭐⭐⭐⭐⭐
> For the generic two-pointer pattern (fast/slow, left/right on arrays) see [2_pointers.md](./2_pointers.md); for the one-deletion variant (LC 680) and centre expansion see [palindrome.md](./palindrome.md). What stays here is the character-level scan-and-swap.

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

### Template 2: Character Frequency & Anagram Signatures — LC 242, LC 438, LC 49 ⭐⭐⭐⭐⭐

**Pattern**: two strings are anagrams iff their **character multisets** are equal. That single
signature idea appears three ways — compare the multiset directly, **roll** it across a
fixed-size window, or use it as a **hash key** to group.

**Key Idea**: the signature is `Counter(s)` (or `int[26]`); a sorted-character tuple is the same
signature in a hashable form.

> Assumes `from collections import Counter`. Depth lives elsewhere: [hashing.md](./hashing.md) owns
> LC 242 and LC 49 with the Java implementations, and [sliding_window.md](./sliding_window.md) owns
> the LC 438 / LC 567 fixed-window mechanics.

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

**Gotchas**
- ⚠️ `Counter(a) == Counter(b)` is O(n) but allocates; an `int[26]` diff counter is the O(1)-space form.
- ⚠️ For the rolling window, **delete the zero entries** — `Counter` keeps `0` values and then never compares equal.
- ⚠️ `sorted(s)` is a `list` and unhashable; the key must be `tuple(sorted(s))` or `"".join(sorted(s))`.

### Template 3: Run-Length Grouping (Consecutive Character Groups) — LC 696 ⭐⭐⭐⭐

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

### Template 4: String Transformation — Parse & Rebuild — LC 8, LC 12 ⭐⭐⭐⭐
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

### Template 5: Greedy Line Packing + Space Distribution (Text Wrapping) — LC 68 ⭐⭐⭐⭐⭐

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

> The same "collect pieces into a list, join once" discipline applied to 3-digit chunks is
> LC 273 Integer to English Words — see [string_examples.md](./string_examples.md).

### Template 6: Parse Structured Text (Delimiter Split + Depth/Stack) — LC 388 ⭐⭐⭐⭐

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

#### Variation 6.1: Token Stack — LC 71 Simplify Path

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

### Template 7: In-place Char Array — Mark then Rebuild — LC 1249 ⭐⭐⭐⭐⭐

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

### Template 8: Greedy Partition by Last Occurrence — LC 763 ⭐⭐⭐⭐

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

### Where the Heavy Algorithms Live ⭐⭐⭐⭐

Four string families are big enough to own their own sheet. This sheet does **not** re-derive
them; pick the row, go to the sheet.

**Substring search — which algorithm?**

| Situation | Use | Sheet |
|---|---|---|
| One search, `n·m` fits the limits | the built-in — `s.find(p)` / `s.indexOf(p)` | — |
| One pattern, adversarial input, need O(n+m) | **KMP** failure function | [string_matching_kmp_rolling_hash.md](./string_matching_kmp_rolling_hash.md) |
| Many patterns, or "does any length-`k` window repeat?" | **rolling hash** (Rabin-Karp), double-hash to kill collisions | [string_matching_kmp_rolling_hash.md](./string_matching_kmp_rolling_hash.md) |
| Longest prefix that is also a suffix; periodicity | **KMP failure array** or **Z-array** | [advanced_string_algorithms.md](./advanced_string_algorithms.md) |
| Binary search the answer length + hash the windows | rolling hash on top of binary search (LC 1044, 718) | [string_matching_kmp_rolling_hash.md](./string_matching_kmp_rolling_hash.md) |
| Repeated substrings, suffix ranking | **suffix array / automaton** | [advanced_string_algorithms.md](./advanced_string_algorithms.md) |
| Validate a numeric / formatted token | hand-built **DFA** (LC 65) | [advanced_string_algorithms.md](./advanced_string_algorithms.md) |

**The other three families**

| Family | Signals | Sheet |
|---|---|---|
| **Palindromes** | "palindromic substring/subsequence", "make it a palindrome", one-deletion checks | [palindrome.md](./palindrome.md) — centre expansion, interval DP, Manacher (LC 5), KMP prefix trick (LC 214), palindrome pairs (LC 336) |
| **Character windows** | "longest/shortest substring such that…", "at most k distinct", "contains all of t" | [sliding_window.md](./sliding_window.md) — LC 3, 76, 159, 340, 424, 438, 567, 1004 |
| **Two-sequence DP** | comparing / aligning **two** strings, "minimum operations", "longest common …" | [dp_string.md](./dp_string.md) and [dp.md](./dp.md) — LC 72, 97, 115, 583, 712, 1143 |

## String API Essentials

The full API tour — slicing, `split`/`join`, `StringBuilder`, char arithmetic, case and Unicode
traps — moved to [string_operations.md](./string_operations.md). This is the subset worth
recalling without opening it.

| Task | Python | Java |
|---|---|---|
| String → chars | `list(s)` | `s.toCharArray()` |
| Chars → string | `"".join(chars)` | `new String(chars)` |
| Reverse | `s[::-1]` | `new StringBuilder(s).reverse().toString()` |
| Substring | `s[i:j]` | `s.substring(i, j)` |
| Split on whitespace / delimiter | `s.split()` / `s.split(",")` | `s.trim().split("\\s+")` / `s.split(",")` |
| Join with separator | `",".join(parts)` | `String.join(",", parts)` |
| Build incrementally | `parts.append(x)` then `"".join(parts)` | `StringBuilder.append(x)` then `.toString()` |
| Char → code / code → char | `ord(c)` / `chr(n)` | `(int) c` / `(char) n` |
| Index into the 26 letters | `ord(c) - ord('a')` | `c - 'a'` |
| Is letter / digit / alnum | `c.isalpha()` / `c.isdigit()` / `c.isalnum()` | `Character.isLetter(c)` / `isDigit(c)` / `isLetterOrDigit(c)` |
| Case fold | `c.lower()` | `Character.toLowerCase(c)` |
| Frequency table | `Counter(s)` | `int[26]` or `HashMap<Character,Integer>` |

- ⚠️ **Never concatenate in a loop** — `s += x` is O(n²) in both languages. Collect and join.
- ⚠️ Java `String.split` takes a **regex**: `split(".")` splits on every character; use `split("\\.")`.
- ⚠️ `s.split(",")` drops trailing empty fields in Java; pass `-1` as the limit to keep them.

## Summary & Quick Reference

### Problem → Template Decision Table ⭐⭐⭐⭐⭐

Read the **Signal** column; it is the phrase in the problem statement that fixes the approach.

| Signal in the problem | Approach | Where | Problems |
|---|---|---|---|
| Compare or swap from **both ends**; reverse in place | two pointers, `while left < right` | Template 1 | 125, 344, 345, 541, 917, 925, 151 |
| "**anagram**", "permutation of", "group by same letters" | char-frequency signature | Template 2 | 242, 438, 49, 567, 451 |
| Answer depends on **runs** of identical characters | group-length array (or streaming `prev`/`cur`) | Template 3 | 696, 38, 443, 485, 487, 830, 1004, 1446, 1768, 809 |
| Convert **between formats** — digits, Roman, zigzag | parse with explicit rules, rebuild | Template 4 | 6, 8, 12, 13, 273, 482, 468 |
| **Fixed line width**, padding, column layout | greedy pack, then distribute leftovers left-heavy | Template 5 | 68, 273 |
| Input is a **serialized structure** — path, log, indented tree | split on delimiter + depth map or token stack | Template 6 | 388, 71, 937, 1071 |
| "**remove** the minimum characters so that …" | `char[]` + index stack, mark then rebuild | Template 7 | 1249, 20, 32, 921 |
| "**maximum number of parts**" such that a property stays local | last-occurrence sweep, cut when `i == end` | Template 8 | 763, 56 |
| Build words **character by character** from a dictionary | sort + set, check only the immediate prefix | [string_examples.md](./string_examples.md) | 720, 648, 745 |
| "find this pattern in that text **efficiently**" | KMP, rolling hash or Z-array | [string_matching_kmp_rolling_hash.md](./string_matching_kmp_rolling_hash.md) | 28, 459, 686, 796, 1044, 1392 |
| "**longest / shortest substring** such that …" | sliding window | [sliding_window.md](./sliding_window.md) | 3, 76, 159, 340, 424, 1004 |
| Anything **palindromic** | centre expansion, interval DP, Manacher, KMP prefix | [palindrome.md](./palindrome.md) | 5, 9, 125, 131, 132, 214, 409, 516, 647, 680, 1216, 1312 |
| Comparing / aligning **two** strings | two-sequence grid DP | [dp_string.md](./dp_string.md) | 10, 44, 72, 97, 115, 583, 712, 1143 |
| **Many words** sharing prefixes | trie | [trie.md](./trie.md) | 208, 211, 212, 648, 745 |
| "is one a **rotation** of the other" | `goal in (s + s)` after a length check | [string_examples.md](./string_examples.md) | 796 |
| Custom alphabet / **comparator** ordering | rank map `int[26]`, compare adjacent pairs | [string_examples.md](./string_examples.md) | 953, 269, 937 |
| Enumerate **split positions** and validate each piece | nested enumeration + per-piece validity rules | [string_examples.md](./string_examples.md) | 816, 93, 282, 468 |

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

### Common Tricks

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

> The `StringBuilder`-only spelling of this same Java scan was dropped — same stack semantics, no new idea.

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

### Common Mistakes & Interview Tips

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

**🎤 Interview Tips:**

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

- **Edge cases to state out loud**: empty string, single character, all characters identical, non-alphanumeric characters, mixed case, overflow on numeric conversion.

### Where the Rest Lives

| Sheet | What it holds |
|---|---|
| [string_examples.md](./string_examples.md) | The worked LC archive — one canonical solution per problem per language, for every problem this sheet's templates do not already solve. |
| [string_operations.md](./string_operations.md) | The language-level API: Python slicing and methods, Java `String`/`StringBuilder`, character classification, and the build-performance rules. |
