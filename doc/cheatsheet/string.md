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

## Recognition

Read the statement for the signal; the constraint cue comes from the ruler in [complexity_cheatsheet.md](./complexity_cheatsheet.md). Rows that route out to another sheet are correct answers, not gaps.

| Signal in the statement | Constraint cue | Reach for | Not this if |
|---|---|---|---|
| "is it a palindrome, ignoring non-alphanumerics", "reverse in place" | n ≤ 2·10⁵, O(1) extra space asked | [Template 1](#template-1-two-pointer-scan--in-place-reversal--lc-125-) two-pointer scan | one deletion allowed, or the longest palindromic substring → [palindrome.md](./palindrome.md) |
| "anagram", "permutation of", "group words with the same letters" | lowercase English letters → `int[26]` | [Template 2](#template-2-character-frequency--anagram-signatures--lc-242-) frequency signature | "longest substring with at most k distinct" → a variable window, [sliding_window.md](./sliding_window.md) |
| "consecutive 0s and 1s", "groups of the same character", "compress" | n ≤ 10⁵ → no O(n²) substring enumeration | [Template 3](#template-3-run-length-grouping-consecutive-character-groups--lc-696-) run-length groups | flip at most k zeros → a window over the raw string (LC 1004) |
| "convert the string to an integer", Roman numerals, a rule list read top to bottom | 32-bit overflow named in the statement | [Template 4](#template-4-parse--rebuild-by-phases--lc-8-) parse by phases | a full numeric grammar with exponents (LC 65) → a DFA, [advanced_string_algorithms.md](./advanced_string_algorithms.md) |
| "each line has exactly maxWidth characters", "fully justified" | total characters ≤ 10⁴ | [Template 5](#template-5-greedy-line-packing--space-distribution-text-wrapping--lc-68-) greedy pack + distribute | minimise the raggedness cost of the lines → DP, not greedy |
| "absolute path", `\n` and `\t` in the input, "simplify the path" | | [Template 6](#template-6-parse-structured-text-delimiter-split--depthstack--lc-388-) split + depth map / token stack | arithmetic expressions with precedence → [stack_expression_parsing.md](./stack_expression_parsing.md) |
| "remove the minimum number of characters so the result is valid" | n ≤ 10⁵ → no repeated `substring` | [Template 7](#template-7-in-place-char-array--mark-then-rebuild--lc-1249-) mark then rebuild | only "is it valid?" → LC 20, a plain stack |
| "partition into as many parts as possible", "each letter in at most one part" | | [Template 8](#template-8-greedy-partition-by-last-occurrence--lc-763-) last-occurrence sweep | the pieces are given as intervals → merge intervals (LC 56) |
| "build every word one character at a time from other words in the list" | | sort + set, check the immediate prefix → [string_examples.md](./string_examples.md) (LC 720) | prefix queries across many words → [trie.md](./trie.md) |
| "find the pattern in the text", "repeated substring" | n·m too large → O(n + m) | [string_matching_kmp_rolling_hash.md](./string_matching_kmp_rolling_hash.md) | one search where n·m fits → the built-in `find` / `indexOf` |
| "longest / shortest substring such that …" | n ≤ 10⁵ | [sliding_window.md](./sliding_window.md) | |
| compare or align **two** strings, "minimum operations", "longest common" | m·n ≤ 10⁶ | [dp_string.md](./dp_string.md) | |

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

### Template 1: Two-Pointer Scan & In-Place Reversal — LC 125 ⭐⭐⭐⭐⭐

> For the generic two-pointer pattern (fast/slow, left/right on arrays) see [2_pointers.md](./2_pointers.md); for the one-deletion variant (LC 680) and centre expansion see [palindrome.md](./palindrome.md). What stays here is the character-level scan-and-swap.

**Recognise** — "is it a palindrome, considering only alphanumeric characters", "reverse the
string / the vowels / only the letters **in place**". n ≤ 2·10⁵ and O(1) extra space → no
cleaned copy, walk the original from both ends.

**Key point** — everything outside `s[left..right]` has already been matched against its
mirror, so the first mismatch inside the window is a proof of "not a palindrome", and
`left >= right` is a proof that it is.

```python
# python
# LC 125 - Valid Palindrome
# IDEA: left/right walk inward, each skipping non-alphanumerics; compare case-folded
# time = O(n), space = O(1)
def isPalindrome(s):
    left, right = 0, len(s) - 1
    while left < right:
        while left < right and not s[left].isalnum():     # skip punctuation on the left
            left += 1
        while left < right and not s[right].isalnum():    # ... and on the right
            right -= 1
        if s[left].lower() != s[right].lower():
            return False
        left += 1
        right -= 1
    return True

# LC 344 - Reverse String: the same two pointers, swapping instead of comparing
def reverseString(s):
    left, right = 0, len(s) - 1
    while left < right:
        s[left], s[right] = s[right], s[left]
        left += 1
        right -= 1
```

```java
// java
// LC 125 - Valid Palindrome
// IDEA: left/right walk inward, each skipping non-alphanumerics; compare case-folded
// time = O(n), space = O(1)
public boolean isPalindrome(String s) {
    int left = 0, right = s.length() - 1;
    while (left < right) {
        while (left < right && !Character.isLetterOrDigit(s.charAt(left))) {
            left++;
        }
        while (left < right && !Character.isLetterOrDigit(s.charAt(right))) {
            right--;
        }
        if (Character.toLowerCase(s.charAt(left)) != Character.toLowerCase(s.charAt(right))) {
            return false;
        }
        left++;
        right--;
    }
    return true;
}
```

**Complexity**
- **Time O(n)**: the two skip loops look nested, but `left` only moves right and `right` only
  moves left, so all three loops together take at most n steps.
- **Space O(1)**: the scan reads `s` in place; there is no `cleaned` copy.

**Traps**
- Skipping with `isalpha` instead of `isalnum`: `"0P"` skips the `0`, the pointers meet, and it
  returns `True`. Digits count.
- Dropping `left < right` from the skip loops: `".,"` runs `left` past the end and raises
  `IndexError`.
- `cleaned == cleaned[::-1]` is correct but O(n) space; the interviewer's follow-up is "now
  without the copy".

**Not this if** — "at most one deletion" (LC 680) or "longest palindromic substring" (LC 5):
those branch or expand from a centre → [palindrome.md](./palindrome.md).

**Drill** — LC 125 ⭐ · LC 344 · LC 345 *(swap only vowels)* · LC 917 *(swap only letters)* ·
LC 151 *(reverse the whole string, then each word)*

### Template 2: Character Frequency & Anagram Signatures — LC 242 ⭐⭐⭐⭐⭐

> Depth lives elsewhere: [hashing.md](./hashing.md) owns LC 242 and LC 49 as worked examples, and
> [sliding_window.md](./sliding_window.md) owns the LC 438 / LC 567 fixed-window mechanics.

**Recognise** — "anagram", "permutation of", "same characters", "group the words that use the
same letters". "Lowercase English letters" in the constraints → an `int[26]` instead of a map.

**Key point** — two strings are anagrams iff their **character multisets** are equal. The
multiset is the signature: compare it directly (LC 242), **roll** it across a window of fixed
length (LC 438), or use a hashable spelling of it, the sorted characters, as a **key** (LC 49).

```python
# python
# LC 242 - Valid Anagram
# IDEA: count s up and t down in one int[26]; anagrams iff every count returns to 0
# time = O(n), space = O(1)  (26 letters)
from collections import Counter, defaultdict

def isAnagram(s, t):
    if len(s) != len(t):
        return False
    count = [0] * 26
    for a, b in zip(s, t):
        count[ord(a) - ord('a')] += 1
        count[ord(b) - ord('a')] -= 1
    for c in count:
        if c != 0:
            return False
    return True

# LC 49 - Group Anagrams: the same signature, as a hash key
# time = O(n * k log k), k = longest word
def groupAnagrams(strs):
    groups = defaultdict(list)
    for word in strs:
        key = "".join(sorted(word))      # a list is unhashable; the joined string is not
        groups[key].append(word)
    return list(groups.values())

# LC 438 - Find All Anagrams in a String: the same signature, rolled across a window of len(p)
# time = O(n), space = O(1)  (26 letters)
def findAnagrams(s, p):
    need = Counter(p)
    window = Counter()
    result = []
    for i, c in enumerate(s):
        window[c] += 1
        if i >= len(p):                  # the window is one too long: drop its first char
            out = s[i - len(p)]
            window[out] -= 1
            if window[out] == 0:
                del window[out]
        if window == need:
            result.append(i - len(p) + 1)
    return result
```

```java
// java
// LC 242 - Valid Anagram
// IDEA: count s up and t down in one int[26]; anagrams iff every count returns to 0
// time = O(n), space = O(1)
public boolean isAnagram(String s, String t) {
    if (s.length() != t.length()) {
        return false;
    }
    int[] count = new int[26];
    for (int i = 0; i < s.length(); i++) {
        count[s.charAt(i) - 'a']++;
        count[t.charAt(i) - 'a']--;
    }
    for (int c : count) {
        if (c != 0) {
            return false;
        }
    }
    return true;
}

// LC 49 - Group Anagrams: the sorted characters are the key
public List<List<String>> groupAnagrams(String[] strs) {
    Map<String, List<String>> groups = new HashMap<>();
    for (String word : strs) {
        char[] chars = word.toCharArray();
        Arrays.sort(chars);
        String key = new String(chars);
        groups.computeIfAbsent(key, k -> new ArrayList<>()).add(word);
    }
    return new ArrayList<>(groups.values());
}
```

**Complexity**
- **Time O(n)** for LC 242: one pass over both strings, then a fixed 26-slot check. LC 49 is
  O(n · k log k): the `sorted(word)` per word sets it; a 26-count tuple key makes it O(n · k).
- **Space O(1)** for LC 242: `count` has 26 slots whatever n is. LC 49 stores every word once,
  O(n · k).

**Traps**
- Skipping the length check: `isAnagram("a", "ab")` — `zip` stops at the shorter string and
  returns `True`.
- `groups[sorted(word)]` raises `TypeError: unhashable type: 'list'`. Use `"".join(sorted(word))`
  or `tuple(...)`.
- LC 438's rolling window: delete a count when it reaches 0. Before Python 3.10 a `Counter` with
  a `0` entry did not compare equal to one without it, so `"cbaebabacd"`, `"abc"` lost the match
  at 6 (the dropped `e` stays as `e: 0`); deleting is correct on every version.
- `int[26]` assumes lowercase letters. The follow-up "what about Unicode?" wants a `HashMap`.

**Not this if** — "longest substring with at most k distinct characters": the window length is
not fixed, so it is a variable window → [sliding_window.md](./sliding_window.md).

**Drill** — LC 242 ⭐ · LC 383 *(one-sided: magazine covers note)* · LC 49 ⭐ · LC 438 *(rolled
signature)* · LC 567

### Template 3: Run-Length Grouping (Consecutive Character Groups) — LC 696 ⭐⭐⭐⭐

**Recognise** — "consecutive 0s and 1s", "groups of the same character", "compress the string",
"longest run". n ≤ 10⁵ rules out enumerating substrings in O(n²); compress first and work on the
group-length array.

**Key point** — every valid LC 696 substring is `0…01…1` or `1…10…0`, so it straddles **exactly
one boundary** between two adjacent groups. A boundary between groups of lengths `a` and `b`
yields exactly `min(a, b)` of them: take `k` characters each side for `k = 1 … min(a, b)`; any
larger `k` spills into a third group.

```python
# python
# LC 696 - Count Binary Substrings
# IDEA: compress s into consecutive-group lengths; each adjacent pair adds min(prev, cur)
# time = O(n), space = O(n)
def group_lengths(s):
    groups = [1]
    for i in range(1, len(s)):
        if s[i - 1] != s[i]:      # boundary: start a new group
            groups.append(1)
        else:                     # same char: extend the current group
            groups[-1] += 1
    return groups

def countBinarySubstrings(s):
    groups = group_lengths(s)
    ans = 0
    for i in range(1, len(groups)):
        ans += min(groups[i - 1], groups[i])
    return ans
```

The streaming form keeps only the last two groups, which is the O(1)-space answer to the
follow-up:

```python
# python
# LC 696 - Count Binary Substrings, streaming
# IDEA: never build the group array; keep prev (last finished group) and cur (open group)
# time = O(n), space = O(1)
def countBinarySubstrings(s):
    ans, prev, cur = 0, 0, 1
    for i in range(1, len(s)):
        if s[i - 1] != s[i]:
            ans += min(prev, cur)   # close off the boundary
            prev, cur = cur, 1      # the open group becomes prev; restart cur
        else:
            cur += 1
    return ans + min(prev, cur)     # the loop never closes the last pair
```

```java
// java
// LC 696 - Count Binary Substrings
// IDEA: streaming run-length groups; each boundary adds min(prev, cur)
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
    return ans + Math.min(prev, cur);   // flush the final pair
}
```

**Complexity**
- **Time O(n)**: one `for i in range(1, len(s))` pass; each character extends a group or opens
  one.
- **Space O(n)** for `groups` (one slot per run, n in the worst case `"0101…"`); **O(1)** for the
  streaming form, which keeps two integers.

**Dry run**

```text
s = "001110011"
groups:        00 | 111 | 00 | 11   ->  [2, 3, 2, 2]
min(2, 3) = 2  "01", "0011"
min(3, 2) = 2  "10", "1100"
min(2, 2) = 2  "01", "0011"
total     = 6
```

**Traps**
- Forgetting the flush after the loop: `"01"` returns 0 instead of 1, because the loop settles a
  pair only when it sees the *next* boundary.
- Starting with `prev = 1`: `"01"` returns 2. With `prev = 0` the first boundary adds
  `min(0, cur) = 0`, as it must.
- Filtering out groups of length 1: `"010"` has two valid substrings, both built from length-1
  groups.

**Not this if** — "flip at most k zeros to get the longest run of ones" (LC 1004): the answer
spans several groups, so slide a window over the raw string → [sliding_window.md](./sliding_window.md).

**Drill** — LC 696 ⭐ · LC 1446 *(max group length)* · LC 830 *(report groups ≥ 3)* · LC 443
*(write `char + count` in place)* · LC 38 *(emit `count + char` per group, n times)*

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

### Template 4: Parse & Rebuild by Phases — LC 8 ⭐⭐⭐⭐

**Recognise** — "convert the string to a 32-bit integer", "integer to Roman / Roman to integer",
"zigzag", "encode a list of strings to one string": the statement is a **rule list** read top to
bottom, and it names the overflow behaviour.

**Key point** — one index walks through fixed phases (spaces → at most one sign → digits); each
phase consumes only its own characters, so the first character no phase accepts ends the parse.
The value is clamped **while** it is being built, so it never leaves the 32-bit range.

```python
# python
# LC 8 - String to Integer (atoi)
# IDEA: one index, three phases (leading spaces, one sign, digits); clamp as each digit lands
# time = O(n), space = O(1)
def myAtoi(s):
    INT_MAX, INT_MIN = 2**31 - 1, -2**31
    i, n = 0, len(s)
    while i < n and s[i] == ' ':           # phase 1: leading spaces only
        i += 1
    sign = 1
    if i < n and s[i] in '+-':             # phase 2: at most one sign
        if s[i] == '-':
            sign = -1
        i += 1
    num = 0
    while i < n and '0' <= s[i] <= '9':    # phase 3: digits until the first non-digit
        num = num * 10 + (ord(s[i]) - ord('0'))
        if sign * num > INT_MAX:
            return INT_MAX
        if sign * num < INT_MIN:
            return INT_MIN
        i += 1
    return sign * num

# LC 12 - Integer to Roman: the rebuild direction — greedy over the value/symbol table,
# where the subtractive pairs (900 "CM", 4 "IV") are just more rows
def intToRoman(num):
    values = [1000, 900, 500, 400, 100, 90, 50, 40, 10, 9, 5, 4, 1]
    symbols = ["M", "CM", "D", "CD", "C", "XC", "L", "XL", "X", "IX", "V", "IV", "I"]
    result = []
    for value, symbol in zip(values, symbols):
        while num >= value:
            result.append(symbol)
            num -= value
    return "".join(result)
```

```java
// java
// LC 8 - String to Integer (atoi)
// IDEA: one index, three phases; test for overflow BEFORE num * 10 + digit can wrap
// time = O(n), space = O(1)
public int myAtoi(String s) {
    int i = 0, n = s.length();
    while (i < n && s.charAt(i) == ' ') {
        i++;
    }
    int sign = 1;
    if (i < n && (s.charAt(i) == '+' || s.charAt(i) == '-')) {
        if (s.charAt(i) == '-') {
            sign = -1;
        }
        i++;
    }
    int num = 0;
    while (i < n && s.charAt(i) >= '0' && s.charAt(i) <= '9') {   // ASCII digits only
        int digit = s.charAt(i) - '0';
        if (num > (Integer.MAX_VALUE - digit) / 10) {   // num * 10 + digit would overflow
            return sign == 1 ? Integer.MAX_VALUE : Integer.MIN_VALUE;
        }
        num = num * 10 + digit;
        i++;
    }
    return sign * num;
}
```

**Complexity**
- **Time O(n)**: the index `i` only moves forward across the three phase loops.
- **Space O(1)**: the number is accumulated in place; nothing is sliced. (LC 12's output is
  O(1) too: at most 15 symbols for num ≤ 3999.)

**Traps**
- `"+-12"` returns 0: after one sign, `-` is a non-digit and ends phase 3. Accepting a second
  sign is the classic bug.
- Java `num * 10 + digit` wraps silently: `"2147483648"` becomes negative unless the overflow test
  runs **before** the multiply. `"-91283472332"` must clamp to `-2147483648`.
- `s.strip()` also eats trailing spaces and `"\t"`; the statement says leading `' '` only. Harmless
  on LeetCode's tests, wrong as a reading of the rules.
- `str.isdigit()` is `True` for `"²"`; compare against `'0'` … `'9'`.

**Not this if** — a full numeric grammar with signs, a decimal point and an exponent (LC 65
Valid Number): too many phases to hand-code → a DFA in
[advanced_string_algorithms.md](./advanced_string_algorithms.md).

**Drill** — LC 8 ⭐ · LC 13 *(Roman → int: subtract when a smaller symbol precedes a larger)* ·
LC 12 · LC 6 *(zigzag: one row index bouncing)* · LC 271 *(length-prefix encoding: parse a
length, then that many characters)*

### Template 5: Greedy Line Packing + Space Distribution (Text Wrapping) — LC 68 ⭐⭐⭐⭐⭐

**Recognise** — "each line has exactly `maxWidth` characters", "fully justified", "extra spaces
are distributed as evenly as possible, the left slots getting more". Every word-wrap or
column-formatting problem is two phases: pack a line, then spread its leftover spaces.

**Key point** — while packing, the width needed for `words[i..j]` is `sum(len) + (j - i)`: the
gap count is exactly `j - i`, so the fit test is `lineLen + len(words[j]) + (j - i) <= maxWidth`.
When distributing, every gap gets `base = spaces // slots` and the **first `spaces % slots` gaps
get one more** (left-heavy).

```python
# python
# LC 68 - Text Justification
# IDEA: 2 phases per line -> (1) greedy pack words, (2) distribute leftover spaces
# time = O(total chars), space = O(total chars) for the output
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
                gap = base + (1 if k - i < extra else 0)
                parts.append(" " * gap)
            parts.append(words[j - 1])
            line = "".join(parts)
        res.append(line)
        i = j                             # the next line starts where this one stopped
    return res
```

```java
// java
// LC 68 - Text Justification
// IDEA: 2 phases per line -> (1) greedy pack words, (2) distribute leftover spaces
// time = O(total chars), space = O(total chars) for the output
public List<String> fullJustify(String[] words, int maxWidth) {
    List<String> res = new ArrayList<>();
    int i = 0, n = words.length;
    while (i < n) {
        // (1) GREEDY PACK: (j - i) is the number of gaps if words[j] is also taken
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
                if (k > i) {
                    sb.append(' ');
                }
                sb.append(words[k]);
            }
            while (sb.length() < maxWidth) {
                sb.append(' ');
            }
        } else {
            // (2b) FULL JUSTIFY: base = spaces / slots, the first (spaces % slots) gaps get +1
            int base = spaces / slots, extra = spaces % slots;
            for (int k = i; k < j; k++) {
                sb.append(words[k]);
                if (k < j - 1) {
                    int pad = base + (k - i < extra ? 1 : 0);
                    for (int p = 0; p < pad; p++) {
                        sb.append(' ');
                    }
                }
            }
        }
        res.add(sb.toString());
        i = j;   // the next line starts where this one stopped
    }
    return res;
}
```

**Complexity**
- **Time O(total chars)**: each word is packed once (the inner `while j` never revisits a word,
  because `i = j` moves past it) and each output character is written once.
- **Space O(total chars)**: the output itself; the working state is a few integers.

**Traps**
- Fully justifying the last line: `["What","must","be","acknowledgment","shall","be"]`, width 16,
  must end `"shall be        "`, left-justified and padded, not `"shall          be"`.
- The single-word line: `"acknowledgment"` alone has `slots == 0`, and `spaces / slots` divides by
  zero unless that branch comes first.
- Right-heavy distribution: `"example  of text"` (width 16) has 3 spaces over 2 gaps; the extra
  space goes in the **first** gap.
- A left-justified line not padded to `maxWidth` fails the exact-length check.

**Not this if** — "minimise the total raggedness" of the lines: greedy packing is not optimal for
a squared-slack cost → DP over break points.

**Drill** — LC 68 ⭐ · LC 1592 *(one line: spread spaces evenly, the rest at the end)* · LC 273
*(collect pieces into a list, join once — chunked by 3 digits; see
[string_examples.md](./string_examples.md))*

### Template 6: Parse Structured Text (Delimiter Split + Depth/Stack) — LC 388 ⭐⭐⭐⭐

**Recognise** — the input is a **serialized structure**: "absolute path", `\n` and `\t` in the
input, "simplify the Unix path", log lines with fields. Split on the delimiter and keep the
current context, never re-scan the string.

**Key point** — carry **lengths or tokens**, never substrings. `depth_len[d]` is the length of
the path prefix at depth `d` on the current branch, so a file at depth `d` costs
`depth_len[d] + len(name)` in O(1), and overwriting `depth_len[d + 1]` discards a finished branch.

```python
# python
# LC 388 - Longest Absolute File Path
# IDEA: split on '\n'; leading '\t' count = depth; depth_len[d] = prefix length at depth d
# time = O(n), space = O(max depth)
def lengthLongestPath(inp):
    best = 0
    depth_len = {0: 0}                       # root has an empty prefix
    for line in inp.split("\n"):
        name = line.lstrip("\t")
        depth = len(line) - len(name)        # depth = number of leading tabs
        if "." in name:
            best = max(best, depth_len[depth] + len(name))         # a file is a leaf
        else:
            depth_len[depth + 1] = depth_len[depth] + len(name) + 1  # +1 for '/'
    return best
```

```java
// java
// LC 388 - Longest Absolute File Path
// IDEA: split on '\n'; leading '\t' count = depth; depthLen[d] = prefix length at depth d
// time = O(n), space = O(max depth)
public int lengthLongestPath(String input) {
    int best = 0;
    Map<Integer, Integer> depthLen = new HashMap<>();
    depthLen.put(0, 0);                       // root has an empty prefix
    for (String line : input.split("\n")) {
        int depth = 0;                        // number of leading '\t', not the indent width
        while (depth < line.length() && line.charAt(depth) == '\t') {
            depth++;
        }
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

**Complexity**
- **Time O(n)**: `split` and the tab count each touch every character once; every map access is
  O(1).
- **Space O(max depth)** for `depth_len`, plus the O(n) list `split` returns.

**Traps**
- Counting indent width: `'\t'` is **one** character, so `"dir\n\tfile.txt"` has depth 1, not 4.
- No file at all: `"a"` returns 0, not the longest directory path.
- A trailing `/` on the file: the `+ 1` belongs to directories only; `"dir\n\tfile.ext"` is 12
  (`dir/file.ext`), not 13.

**Drill** — LC 388 ⭐ · LC 1598 *(depth counter only)* · LC 71 *(token stack, below)* · LC 937
*(split each log into id + body, then sort)*

#### Variation 6.1: Token Stack — LC 71 Simplify Path

> **Key point** — same split-then-context shape, but the context is a stack of **tokens**: after
> each token the stack is exactly the canonical path so far, so `..` pops and every other name
> pushes.

**Reduces to**: [Template 6](#template-6-parse-structured-text-delimiter-split--depthstack--lc-388-) — the depth map becomes a token stack.

```python
# python
# LC 71 - Simplify Path
# IDEA: split on '/', ignore "" and ".", ".." pops, everything else pushes
# time = O(n), space = O(n)
def simplifyPath(path):
    stack = []
    for tok in path.split("/"):
        if tok == "" or tok == ".":          # "//" and "/./" are no-ops
            continue
        if tok == "..":
            if stack:                        # ".." at the root stays at the root
                stack.pop()
        else:
            stack.append(tok)
    return "/" + "/".join(stack)
```

```java
// java
// LC 71 - Simplify Path
// IDEA: split on '/', ignore "" and ".", ".." pops, everything else pushes
// time = O(n), space = O(n)
public String simplifyPath(String path) {
    Deque<String> stack = new ArrayDeque<>();
    for (String tok : path.split("/")) {
        if (tok.isEmpty() || tok.equals(".")) {
            continue;                                // "//" and "/./" are no-ops
        }
        if (tok.equals("..")) {
            if (!stack.isEmpty()) {
                stack.pollLast();                    // ".." at the root stays at the root
            }
        } else {
            stack.offerLast(tok);
        }
    }
    StringBuilder sb = new StringBuilder();
    for (String dir : stack) {
        sb.append('/').append(dir);
    }
    return sb.length() == 0 ? "/" : sb.toString();
}
```

- ⚠️ `"..."` / `"....."` are **valid directory names** — only exactly `".."` pops.
- ⚠️ The result always starts with `/` and never ends with one (except the bare root `"/"`).

### Template 7: In-place Char Array — Mark then Rebuild — LC 1249 ⭐⭐⭐⭐⭐

**Recognise** — "remove the minimum number of characters so that the result is valid", "delete
the characters at these positions". The offenders are found by **index**, and n ≤ 10⁵ rules out
deleting with `substring` inside the loop.

**Key point** — the stack holds **indices of `'('`, not characters**, so after the scan it holds
exactly the unmatched `'('` positions; every unmatched `')'` was marked the moment it arrived. Two
passes: mark, then rebuild once.

```python
# python
# LC 1249 - Minimum Remove to Make Valid Parentheses
# IDEA: stack of '(' INDICES; unmatched ')' blanked on sight, unmatched '(' left on the stack
# time = O(n), space = O(n)
def minRemoveToMakeValid(s):
    arr = list(s)
    stack = []
    for i, c in enumerate(arr):
        if c == "(":
            stack.append(i)          # push the index, not the char
        elif c == ")":
            if stack:
                stack.pop()          # matched pair
            else:
                arr[i] = ""          # ')' with no partner -> blank it out
    for i in stack:                  # leftovers = unmatched '(' positions
        arr[i] = ""
    return "".join(arr)
```

```java
// java
// LC 1249 - Minimum Remove to Make Valid Parentheses
// IDEA: stack of '(' INDICES; unmatched ')' marked on sight, unmatched '(' left on the stack
// time = O(n), space = O(n)
public String minRemoveToMakeValid(String s) {
    char[] arr = s.toCharArray();
    Deque<Integer> stack = new ArrayDeque<>();
    for (int i = 0; i < arr.length; i++) {
        if (arr[i] == '(') {
            stack.push(i);                   // push the index, not the char
        } else if (arr[i] == ')') {
            if (stack.isEmpty()) {
                arr[i] = '*';                // ')' with no partner -> mark for deletion
            } else {
                stack.pop();                 // matched pair
            }
        }
    }
    while (!stack.isEmpty()) {               // leftovers = unmatched '(' positions
        arr[stack.pop()] = '*';
    }
    StringBuilder sb = new StringBuilder();
    for (char c : arr) {
        if (c != '*') {
            sb.append(c);
        }
    }
    return sb.toString();
}
```

**Complexity**
- **Time O(n)**: one marking pass, one flush of the stack (at most n indices), one rebuild pass.
- **Space O(n)**: the `char[]` copy and the index stack, which holds every index for `"(((("`.

**Traps**
- Skipping the second flush: `"))(("` returns `"(("` instead of `""` — the unmatched `'('` are
  only known once the scan ends.
- A sentinel that can occur in the input: `'*'` is safe for LC 1249 (letters and parentheses
  only); in Python the empty string is safest, because `"".join` skips it.
- Deleting with `s = s[:i] + s[i+1:]` inside the loop is O(n²) and shifts every later index, so
  the stored positions go stale.

**Not this if** — only "is it valid?" (LC 20) needs a boolean, not the indices; "longest valid
substring" (LC 32) reuses the index stack to measure `i - stack[-1]`.

**Drill** — LC 1249 ⭐ · LC 20 *(validity only)* · LC 921 *(count the unmatched, no rebuild)* ·
LC 32 *(index stack measures lengths)*

### Template 8: Greedy Partition by Last Occurrence — LC 763 ⭐⭐⭐⭐

**Recognise** — "partition the string into as many parts as possible so that each letter appears
in at most one part": the maximum number of pieces such that a property stays local to a piece.

**Key point** — the current chunk cannot end before `max(last[c])` over every `c` seen in it. When
`i == end`, nothing inside the chunk reaches further right, so cutting here is safe, and cutting
at the first safe point maximises the count.

```python
# python
# LC 763 - Partition Labels
# IDEA: last[c] = final index of c; extend `end` while scanning, cut when i reaches it
# time = O(n), space = O(1)  (26 letters)
def partitionLabels(s):
    last = {}
    for i, c in enumerate(s):      # pass 1: the last index of every character
        last[c] = i
    res = []
    start = end = 0
    for i, c in enumerate(s):      # pass 2: stretch the chunk, cut when it closes
        end = max(end, last[c])
        if i == end:
            res.append(end - start + 1)
            start = i + 1
    return res
```

```java
// java
// LC 763 - Partition Labels
// IDEA: last[c] = final index of c; extend `end` while scanning, cut when i reaches it
// time = O(n), space = O(1)  (26 letters)
public List<Integer> partitionLabels(String s) {
    int[] last = new int[26];
    for (int i = 0; i < s.length(); i++) {
        last[s.charAt(i) - 'a'] = i;         // last occurrence
    }
    List<Integer> res = new ArrayList<>();
    int start = 0, end = 0;
    for (int i = 0; i < s.length(); i++) {
        end = Math.max(end, last[s.charAt(i) - 'a']);   // stretch to cover this char
        if (i == end) {                      // nothing inside reaches past i -> safe to cut
            res.add(end - start + 1);
            start = i + 1;
        }
    }
    return res;
}
```

**Complexity**
- **Time O(n)**: two independent passes over `s`.
- **Space O(1)**: `last` has at most 26 entries.

**Traps**
- Cutting on `i == last[s[i]]` instead of `i == end`: `"abab"` cuts after the second `a` and
  returns `[3, 1]`; the right answer is `[4]`, because `b` pushed `end` to 3.
- Building `last` in the same pass as the cuts: a cut cannot see a later occurrence, so the
  separate first pass is required.

**Not this if** — the pieces are given as intervals rather than derived from a string: the same
sweep is merge intervals (LC 56), `[first[c], last[c]]` merged.

**Drill** — LC 763 ⭐ · LC 2405 *(cut when a character repeats)* · LC 56 *(the interval form)*

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

## Drill Path

| Stage | Problems (easy → hard) | What it adds |
|---|---|---|
| **Core** ⭐ | LC 125 · LC 242 · LC 49 · LC 271 | the two-pointer scan, the frequency signature as a check and as a key, a length-prefix parse |
| **Practice** | LC 344 · LC 696 · LC 8 · LC 763 · LC 388 · LC 1249 | one per remaining template: reversal, run-length groups, phased parsing, last-occurrence cuts, depth map, mark then rebuild |
| **Stretch** | LC 443 · LC 71 · LC 68 | in-place compression, the token stack, greedy packing with left-heavy spaces |

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
| "is one a **rotation** of the other" | `goal in (s + s)` after a length check | [string_matching_kmp_rolling_hash.md](./string_matching_kmp_rolling_hash.md) | 796 |
| Custom alphabet / **comparator** ordering | rank map `int[26]`, compare adjacent pairs | [hash_map.md § Template 6](./hash_map.md#template-6-rank-map--value-to-position--lc-953-) | 953, 269, 937 |
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

```python
# python
# LC 1544 - Make The String Great
# IDEA: stack; a char that is the top's other-case twin (|diff| == 32) cancels it, else push
# time = O(n), space = O(n)
def makeGood(s):
    stack = []
    for ch in s:
        if stack and abs(ord(stack[-1]) - ord(ch)) == 32:   # same letter, other case
            stack.pop()                                      # the pair cancels
        else:
            stack.append(ch)
    return "".join(stack)
```

```java
// java
// LC 1544 - Make The String Great
// IDEA: stack; a char that is the top's other-case twin (|diff| == 32) cancels it, else push
// time = O(n), space = O(n)
public String makeGood(String s) {
    Deque<Character> stack = new ArrayDeque<>();
    for (char ch : s.toCharArray()) {
        if (!stack.isEmpty() && Math.abs(stack.peek() - ch) == 32) {
            stack.pop();                     // same letter, other case: the pair cancels
        } else {
            stack.push(ch);
        }
    }
    StringBuilder sb = new StringBuilder();
    while (!stack.isEmpty()) {
        sb.append(stack.pollLast());         // bottom of the stack is the front of the answer
    }
    return sb.toString();
}
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
