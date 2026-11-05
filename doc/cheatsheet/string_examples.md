# String Algorithms — Worked Examples

> **Scope** — The worked string LeetCode problems that are not a template's anchor, each filed as a problem card under the [string.md](./string.md) template it reduces to; the concepts, the pattern cards and the anchor problems themselves stay in the main string sheet.
> **See also**: [string.md](./string.md) — the parent sheet, with the template each card reduces to; [string_operations.md](./string_operations.md) — the language-level string API, split out of the same file; [palindrome.md](./palindrome.md) — the palindrome family in depth; [string_matching_kmp_rolling_hash.md](./string_matching_kmp_rolling_hash.md) — substring search; [sliding_window.md](./sliding_window.md) — character-window problems; [2_pointers_examples.md](./2_pointers_examples.md) — the two-pointer problem set, home of LC 524 and 647 (LC 165 is the anchor of 2_pointers.md Template 9).

## LeetCode Problem Lists

- [String](https://leetcode.com/problem-list/string/)

## Overview

Every section below mirrors one template of [string.md](./string.md), in that sheet's order, and
every problem in it is a problem card: the key point first, then the card it reduces to and the
one thing that differs, then one canonical solution. A template with no problem here (5 to 8)
has no section; its anchor problem is worked in the core card. Read the template first, then the
card.

## Index

| Card in [string.md](./string.md) | Problems (easy → hard) |
|---|---|
| [Template 1: Two-Pointer Scan & In-Place Reversal](./string.md#template-1-two-pointer-scan--in-place-reversal--lc-125-) | [LC 917](#reverse-only-letters--lc-917) |
| [Template 2: Character Frequency & Anagram Signatures](./string.md#template-2-character-frequency--anagram-signatures--lc-242-) | [LC 1794](#count-pairs-of-equal-substrings-with-minimum-difference--lc-1794) · [LC 720](#longest-word-in-dictionary--lc-720) · [LC 828](#count-unique-characters-of-all-substrings-of-a-given-string--lc-828) |
| [Template 3: Run-Length Grouping](./string.md#template-3-run-length-grouping-consecutive-character-groups--lc-696-) | [LC 38](#count-and-say--lc-38) · [LC 809](#expressive-words--lc-809-) |
| [Template 4: Parse & Rebuild by Phases](./string.md#template-4-parse--rebuild-by-phases--lc-8-) | [LC 13](#roman-to-integer--lc-13-) · [LC 482](#license-key-formatting--lc-482) · [LC 468](#validate-ip-address--lc-468) · [LC 816](#ambiguous-coordinates--lc-816) · [LC 273](#integer-to-english-words--lc-273-) |

**Lives elsewhere** — worked in another sheet; listed here so a search for the number lands somewhere.

| LC | Problem | Home |
|---|---|---|
| 14 | Longest Common Prefix | [python_trick.md](./python_trick.md#dropping-the-last-character--s-1-shrinking-a-prefix) — prefix shrinking; or a vertical scan, column `i` across all words |
| 72 | Edit Distance | [dp.md § Template 8](./dp.md#template-8-edit-distance--lc-72-) |
| 165 | Compare Version Numbers | [2_pointers.md § Template 9](./2_pointers.md#template-9-two-sequences-in-lockstep--lc-165-) — one pointer per string, one revision per round |
| 205 · 890 | Isomorphic Strings · Find and Replace Pattern | [hash_map.md § Template 7](./hash_map.md#template-7-bijection-with-two-maps--lc-205) — two maps, `s→t` **and** `t→s` |
| 394 | Decode String | [stack_expression_parsing.md](./stack_expression_parsing.md) |
| 445 | Add Two Numbers II | [add_x_sum.md](./add_x_sum.md) |
| 459 · 686 · 796 | Repeated Substring Pattern · Repeated String Match · Rotate String | [string_matching_kmp_rolling_hash.md](./string_matching_kmp_rolling_hash.md) |
| 524 | Longest Word in Dictionary through Deleting | [2_pointers_examples.md](./2_pointers_examples.md#longest-word-in-dictionary-through-deleting--lc-524-) — the LC 392 subsequence check once per word |
| 647 | Palindromic Substrings | [2_pointers_examples.md](./2_pointers_examples.md#palindromic-substrings--lc-647-) — centre expansion; Manacher in [palindrome.md](./palindrome.md) |
| 738 | Monotone Increasing Digits | [greedy_examples.md](./greedy_examples.md) — borrow on a descent, flood the suffix with `9` |
| 953 | Verifying an Alien Dictionary | [hash_map.md § Template 6](./hash_map.md#template-6-rank-map--value-to-position--lc-953-) |
| 1143 | Longest Common Subsequence | [dp.md § Template 9](./dp.md#template-9-longest-common-subsequence--lc-1143) |

**One-liners** — no card needed; the idea fits in a row.

| LC | Problem | One-line idea | Card |
|---|---|---|---|
| 383 | Ransom Note | count the magazine, decrement per note letter, fail on a negative count | [Template 2](./string.md#template-2-character-frequency--anagram-signatures--lc-242-) |
| 819 | Most Common Word | lower-case, split on non-letters, skip the banned set, take the max count | [Template 2](./string.md#template-2-character-frequency--anagram-signatures--lc-242-) |
| 299 | Bulls and Cows | one pass: equal digits are bulls; otherwise bump two count arrays, cows = `sum(min(cntS[d], cntG[d]))` | [Template 2](./string.md#template-2-character-frequency--anagram-signatures--lc-242-) |
| 937 | Reorder Data in Log Files | `split(" ", 1)` into id and body; letter-logs sort by (body, id), digit-logs keep their order (stable sort) | [Template 6](./string.md#template-6-parse-structured-text-delimiter-split--depthstack--lc-388-) |

## Two-Pointer Scan Examples

### Reverse Only Letters — LC 917

> **Key point** — non-letters never move, so they are walls the two pointers step over; every swap
> pairs the k-th letter from the left with the k-th letter from the right, which is exactly a
> reversal of the letters alone.

**Reduces to**: [string.md § Template 1](./string.md#template-1-two-pointer-scan--in-place-reversal--lc-125-) — the LC 125 scan, swapping instead of comparing, and skipping non-letters instead of non-alphanumerics.

```python
# python
# LC 917 - Reverse Only Letters
# IDEA: left/right walk inward; a non-letter on either side is stepped over, two letters swap
# time = O(n), space = O(n) for the char list (strings are immutable)
def reverseOnlyLetters(s):
    chars = list(s)
    left, right = 0, len(chars) - 1
    while left < right:
        if not chars[left].isalpha():        # left is not a letter: it stays where it is
            left += 1
        elif not chars[right].isalpha():     # same on the right
            right -= 1
        else:                                # two letters: swap them and move both inward
            chars[left], chars[right] = chars[right], chars[left]
            left += 1
            right -= 1
    return "".join(chars)
```

```java
// java
// LC 917 - Reverse Only Letters
// IDEA: left/right walk inward; a non-letter on either side is stepped over, two letters swap
// time = O(n), space = O(n) for the char array
public String reverseOnlyLetters(String s) {
    char[] chars = s.toCharArray();
    int left = 0, right = chars.length - 1;
    while (left < right) {
        if (!Character.isLetter(chars[left])) {
            left++;
        } else if (!Character.isLetter(chars[right])) {
            right--;
        } else {
            char tmp = chars[left];
            chars[left] = chars[right];
            chars[right] = tmp;
            left++;
            right--;
        }
    }
    return new String(chars);
}
```

```text
s = "ab-cd"
[a b - c d]  left=0 right=4  both letters  swap -> [d b - c a]
[d b - c a]  left=1 right=3  both letters  swap -> [d c - b a]
             left=2 right=2  stop                -> "dc-ba"
```

**Traps**
- A swap after both inner skip loops, without re-checking `left < right`: `"7_28]"` (no letters)
  swaps two non-letters. Moving one pointer per iteration, as above, cannot.
- `isalnum` instead of `isalpha`: `"a1b"` would move the `1`. Only letters move.

**Similar** — LC 344 (reverse everything) · LC 345 (swap only vowels) · LC 151 (reverse the words)

## Character Frequency & Signature Examples

### Count Pairs of Equal Substrings With Minimum Difference — LC 1794

> **Key point** — any matching pair of substrings can be shrunk to its first character without
> increasing `j - a`, so only single characters matter; for each letter the best pair is its
> **first** index in `firstString` against its **last** index in `secondString`.

**Reduces to**: [string.md § Template 2](./string.md#template-2-character-frequency--anagram-signatures--lc-242-) — one table per string over the alphabet, but each slot holds an index (first or last occurrence) instead of a count.

```python
# python
# LC 1794 - Count Pairs of Equal Substrings With Minimum Difference
# IDEA: char -> FIRST index in firstString, char -> LAST index in secondString; minimise the gap
# time = O(m + n), space = O(1)  (at most 26 keys per map)
def countQuadruples(firstString, secondString):
    first_idx = {}                      # char -> its FIRST index in firstString
    for i in range(len(firstString)):
        ch = firstString[i]
        if ch not in first_idx:
            first_idx[ch] = i
    last_idx = {}                       # char -> its LAST index in secondString
    for a in range(len(secondString)):
        last_idx[secondString[a]] = a   # later indices overwrite earlier ones
    min_diff = float('inf')
    count = 0
    for ch in first_idx:
        if ch not in last_idx:          # a char missing from secondString forms nothing
            continue
        diff = first_idx[ch] - last_idx[ch]
        if diff < min_diff:             # a strictly smaller diff restarts the count
            min_diff = diff
            count = 1
        elif diff == min_diff:          # an equal diff is one more quadruple
            count += 1
    return count
```

```java
// java
// LC 1794 - Count Pairs of Equal Substrings With Minimum Difference
// IDEA: int[26] of last positions (+1, so 0 means absent); scan firstString once, first hits only
// time = O(m + n), space = O(1)
public int countQuadruples(String firstString, String secondString) {
    int[] last = new int[26];               // last[c] = last index of c in secondString, plus 1
    for (int a = 0; a < secondString.length(); a++) {
        last[secondString.charAt(a) - 'a'] = a + 1;
    }
    boolean[] seen = new boolean[26];       // only the FIRST occurrence in firstString counts
    int minDiff = Integer.MAX_VALUE, count = 0;
    for (int i = 0; i < firstString.length(); i++) {
        int c = firstString.charAt(i) - 'a';
        if (seen[c]) {
            continue;
        }
        seen[c] = true;
        if (last[c] == 0) {                 // 0 means c never appears in secondString
            continue;
        }
        int diff = i - (last[c] - 1);
        if (diff < minDiff) {
            minDiff = diff;
            count = 1;
        } else if (diff == minDiff) {
            count++;
        }
    }
    return count;
}
```

```text
firstString = "abcd", secondString = "bccda"
char  first i  last a  i - a
 a       0       4      -4   <- minimum
 b       1       0       1
 c       2       2       0
 d       3       3       0          -> only 'a' reaches -4 -> answer 1
```

**Traps**
- Counting every equal-diff *substring* pair: the answer counts quadruples at the minimum, and
  each letter contributes exactly one, so `("abcd", "bccda")` is 1, not more.
- Keeping the last index in `firstString` or the first in `secondString`: both make `i - a`
  larger, so the minimum is missed.

**Similar** — LC 1624 (first/last occurrence span) · LC 387 (first unique character)

### Longest Word in Dictionary — LC 720

> **Key point** — after sorting, a word's one-shorter prefix is always processed before it, so
> checking only the **immediate** prefix against the set of buildable words is enough: by
> induction every shorter prefix was checked when that prefix went in.

**Reduces to**: [string.md § Template 2](./string.md#template-2-character-frequency--anagram-signatures--lc-242-) — a hash lookup keyed on whole strings rather than on letter counts; the general form for prefix queries across many words is a trie ([trie.md](./trie.md)), which this sorted set replaces because only one prefix per word is ever asked.

```python
# python
# LC 720 - Longest Word in Dictionary
# IDEA: sort, then a word is buildable iff it has length 1 or word[:-1] is already buildable
# time = O(sum(L) log n) for the sort + O(sum(L)) for the slices, space = O(sum(L))
def longestWord(words):
    words = sorted(words)               # a prefix sorts before every word that extends it
    buildable = set()
    best = ""
    for word in words:
        if len(word) == 1 or word[:-1] in buildable:
            buildable.add(word)
            if len(word) > len(best):   # strictly longer: ties keep the earlier, smaller word
                best = word
    return best
```

```java
// java
// LC 720 - Longest Word in Dictionary
// IDEA: sort, then a word is buildable iff it has length 1 or its one-shorter prefix is buildable
// time = O(sum(L) log n), space = O(sum(L))
public String longestWord(String[] words) {
    Arrays.sort(words);
    Set<String> buildable = new HashSet<>();
    String best = "";
    for (String word : words) {
        if (word.length() == 1 || buildable.contains(word.substring(0, word.length() - 1))) {
            buildable.add(word);
            if (word.length() > best.length()) {
                best = word;
            }
        }
    }
    return best;
}
```

**Traps**
- `>=` instead of `>` when updating `best`: `["a","b"]` returns `"b"`; sorted order already
  delivers the lexicographically smaller word first, so only a strictly longer word may replace it.
- Adding a word to `buildable` before checking its prefix: `["banana"]` alone would seed a chain
  that does not start from one letter.

**Similar** — LC 648 (replace words, trie) · LC 1858 (longest word with all prefixes, trie)

### Count Unique Characters of All Substrings of a Given String — LC 828

> **Key point** — flip the sum: instead of counting the unique letters of each substring, count,
> for each position, the substrings in which that letter occurs once. That is (distance to the
> previous same letter) × (distance to the next same letter).

**Reduces to**: [array.md § Template 5: Index Contribution Counting](./array.md) — the same "each element contributes left-choices × right-choices" sum, where the span is bounded by the neighbouring occurrences of the same letter; the per-letter table of the last two positions is the Template 2 alphabet table holding indices.

```python
# python
# LC 828 - Count Unique Characters of All Substrings of a Given String
# IDEA: per letter keep its last two positions; when a third arrives, the middle one's
#       contribution (prev1 - prev2) * (i - prev1) is final
# time = O(n), space = O(1)  (26 letters)
def uniqueLetterString(s):
    last_two = {}                       # char -> [second-to-last index, last index]
    total = 0
    for i, ch in enumerate(s):
        prev2, prev1 = last_two.get(ch, [-1, -1])
        # the occurrence at prev1 is unique in substrings that start in (prev2, prev1]
        # and end in [prev1, i): (prev1 - prev2) * (i - prev1) of them
        if prev1 != -1:
            total += (prev1 - prev2) * (i - prev1)
        last_two[ch] = [prev1, i]
    n = len(s)
    for prev2, prev1 in last_two.values():    # settle each char's final occurrence
        total += (prev1 - prev2) * (n - prev1)
    return total
```

```text
s = "ABA"   (n = 3)
A at 0: prev = -1, next = 2  -> (0 - (-1)) * (2 - 0) = 2
B at 1: prev = -1, next = 3  -> (1 - (-1)) * (3 - 1) = 4
A at 2: prev =  0, next = 3  -> (2 - 0)    * (3 - 2) = 2
total = 8
```

**Traps**
- Forgetting the final pass: every letter's last occurrence is still open when the loop ends,
  so `"ABC"` returns 0 instead of 10.
- Enumerating substrings and counting with a set: O(n²) at best, and n ≤ 10⁵.

**Similar** — LC 2063 (vowels of all substrings) · LC 907 (sum of subarray minimums) · LC 2262 (total appeal of a string)

## Run-Length Grouping Examples

### Count and Say — LC 38

> **Key point** — each term is the previous one compressed into groups and spoken as
> `count + char`; the sequence never needs anything but the group walk, run n − 1 times.

**Reduces to**: [string.md § Template 3](./string.md#template-3-run-length-grouping-consecutive-character-groups--lc-696-) — the same group walk, emitting `str(count) + char` per group instead of combining adjacent group lengths.

```python
# python
# LC 38 - Count and Say
# IDEA: n - 1 times, walk the term in groups of equal digits and write count then digit
# time = O(n * L) where L is the length of the longest term, space = O(L)
def countAndSay(n):
    term = "1"
    for _ in range(n - 1):
        parts = []
        i = 0
        while i < len(term):
            j = i
            while j < len(term) and term[j] == term[i]:   # j ends the group that starts at i
                j += 1
            parts.append(str(j - i))
            parts.append(term[i])
            i = j
        term = "".join(parts)
    return term
```

**Traps**
- Building the term with `+=` inside the walk: quadratic in the term's length; append to a list
  and join once.
- Closing the last group outside the loop by hand: the `i = j` walk closes every group itself,
  including the last, so `n = 2` returns `"11"` without a special case.

### Expressive Words — LC 809 ⭐⭐⭐⭐

> **Key point** — a word matches `s` iff the two strings have the same sequence of groups and each
> pair of groups has either equal lengths, or an `s` group that is longer **and** at least 3.

**Reduces to**: [string.md § Template 3](./string.md#template-3-run-length-grouping-consecutive-character-groups--lc-696-) — groups of **two** strings walked in lockstep with one pointer each, comparing group lengths pair by pair instead of summing `min(prev, cur)`.

```python
# python
# LC 809 - Expressive Words
# IDEA: walk s and the word together one group at a time; compare each pair of group lengths
# time = O(len(words) * len(s) + sum(len(w))): each word rescans s, space = O(1)
def expressiveWords(s, words):
    def stretchy(word):
        i = j = 0
        while i < len(s) and j < len(word):
            if s[i] != word[j]:
                return False
            ch = s[i]
            run_s = 0
            while i < len(s) and s[i] == ch:       # length of this group in s
                run_s += 1
                i += 1
            run_w = 0
            while j < len(word) and word[j] == ch: # length of the same group in word
                run_w += 1
                j += 1
            if run_s < run_w:                      # stretching only grows a group
                return False
            if run_s != run_w and run_s < 3:       # a grown group must reach 3
                return False
        return i == len(s) and j == len(word)      # both used up: same group sequence

    count = 0
    for word in words:
        if stretchy(word):
            count += 1
    return count
```

```java
// java
// LC 809 - Expressive Words
// IDEA: walk s and the word together one group at a time; compare each pair of group lengths
// time = O(len(words) * len(s) + sum(len(w))): each word rescans s, space = O(1)
public int expressiveWords(String s, String[] words) {
    int count = 0;
    for (String word : words) {
        if (isStretchy(s, word)) {
            count++;
        }
    }
    return count;
}

private boolean isStretchy(String s, String word) {
    int i = 0, j = 0;
    while (i < s.length() && j < word.length()) {
        if (s.charAt(i) != word.charAt(j)) {
            return false;
        }
        char ch = s.charAt(i);
        int runS = 0;
        while (i < s.length() && s.charAt(i) == ch) {
            runS++;
            i++;
        }
        int runW = 0;
        while (j < word.length() && word.charAt(j) == ch) {
            runW++;
            j++;
        }
        if (runS < runW) {                    // stretching only grows a group
            return false;
        }
        if (runS != runW && runS < 3) {       // a grown group must reach 3
            return false;
        }
    }
    return i == s.length() && j == word.length();
}
```

```text
s = "heeellooo"
word "hello": h 1|1  e 3|1 (>= 3, ok)  l 2|2  o 3|1 (>= 3, ok)  both used up -> match
word "helo":  h 1|1  e 3|1 (ok)        l 2|1 (2 < 3, fail)                    -> no
word "hi":    h 1|1  then 'e' != 'i'                                          -> no
answer = 1
```

| `s` group vs word group | `s` group ≥ 3? | Result |
|---|---|---|
| equal | — | match |
| `s` shorter | — | no: stretching never shrinks |
| `s` longer | yes | match: the word's group was stretched |
| `s` longer | no | no: a group of 2 was never a stretch |

**Traps**
- Missing the final `i == len(s) and j == len(word)`: `s = "abcd"`, word `"abc"` matches every
  group the loop sees and leaves `d` unread.
- Allowing `run_s < run_w`: `s = "aaa"`, word `"aaaa"` is not stretchy; extension only grows.
- Treating a group of 2 as stretchable: `s = "aa"`, word `"a"` is invalid; the grown group must
  reach 3.

**Similar** — LC 443 (string compression) · LC 1446 (longest group) · LC 925 (long pressed name: the same lockstep with no ≥ 3 rule)

## Parse & Rebuild Examples

### Roman to Integer — LC 13 ⭐⭐⭐⭐

> **Key point** — read right to left: a symbol smaller than the one to its right is a subtractive
> prefix (`IV`, `CM`), so it subtracts; anything else adds. One comparison replaces the six
> special pairs.

**Reduces to**: [string.md § Template 4](./string.md#template-4-parse--rebuild-by-phases--lc-8-) — the parse direction of LC 12's value table: one index, one rule per character, with the rule decided by the neighbour already read.

```python
# python
# LC 13 - Roman to Integer
# IDEA: scan right to left; subtract a symbol smaller than the one after it, else add
# time = O(n), space = O(1)
def romanToInt(s):
    value = {"I": 1, "V": 5, "X": 10, "L": 50, "C": 100, "D": 500, "M": 1000}
    total = 0
    prev = 0                            # value of the symbol to the right
    for i in range(len(s) - 1, -1, -1):
        cur = value[s[i]]
        if cur < prev:                  # a smaller symbol before a larger one subtracts
            total -= cur
        else:
            total += cur
        prev = cur
    return total
```

```java
// java
// LC 13 - Roman to Integer
// IDEA: scan right to left; subtract a symbol smaller than the one after it, else add
// time = O(n), space = O(1)
public int romanToInt(String s) {
    Map<Character, Integer> value = new HashMap<>();
    value.put('I', 1);
    value.put('V', 5);
    value.put('X', 10);
    value.put('L', 50);
    value.put('C', 100);
    value.put('D', 500);
    value.put('M', 1000);
    int total = 0, prev = 0;
    for (int i = s.length() - 1; i >= 0; i--) {
        int cur = value.get(s.charAt(i));
        if (cur < prev) {
            total -= cur;
        } else {
            total += cur;
        }
        prev = cur;
    }
    return total;
}
```

**Traps**
- `cur <= prev` subtracts on equal symbols: `"III"` returns 1 instead of 3.
- Hard-coding the six pairs (`IV`, `IX`, `XL`, …) as two-character tokens works but is the
  answer an interviewer pushes back on; the one comparison covers all of them.

**Similar** — LC 12 (the rebuild direction, in the core card) · LC 273 (below)

### License Key Formatting — LC 482

> **Key point** — the first group is the only short one, so build from the **right**: every k
> characters, a dash; then reverse once. No group-size arithmetic on the left is needed.

**Reduces to**: [string.md § Template 4](./string.md#template-4-parse--rebuild-by-phases--lc-8-) — the rebuild direction: drop the old separators while reading, write the new ones from a fixed rule, scanning from the end because the rule is anchored there.

```python
# python
# LC 482 - License Key Formatting
# IDEA: scan right to left, skip dashes, upper-case, insert '-' each time a group of k fills
# time = O(n), space = O(n)
def licenseKeyFormatting(s, k):
    out = []                            # built right to left
    in_group = 0
    for i in range(len(s) - 1, -1, -1):
        if s[i] == '-':
            continue
        if in_group == k:               # the group on the right is full: open a new one
            out.append('-')
            in_group = 0
        out.append(s[i].upper())
        in_group += 1
    return "".join(reversed(out))
```

**Traps**
- Writing the dash *after* a character when the group fills: `"a-a-a-a-"`, k = 1 then ends in a
  leading `-`. Insert it before the next character instead, as above.
- An input of only dashes (`"---"`) must return `""`, not `"-"`.

### Validate IP Address — LC 468

> **Key point** — the separator count picks the grammar (3 dots → IPv4, 7 colons → IPv6), and then
> every field is checked against a short rule list; the first broken rule answers `"Neither"`.

**Reduces to**: [string.md § Template 4](./string.md#template-4-parse--rebuild-by-phases--lc-8-) — parse with explicit rules, but the phases are fields split on a delimiter rather than character classes walked by one index.

```python
# python
# LC 468 - Validate IP Address
# IDEA: count separators to choose IPv4 / IPv6, split, validate each field against its rules
# time = O(n), space = O(n) for the split
def validIPAddress(queryIP):
    def is_ipv4(parts):
        for part in parts:
            if not 1 <= len(part) <= 3:
                return False
            if not all('0' <= c <= '9' for c in part):
                return False
            if len(part) > 1 and part[0] == '0':   # no leading zero
                return False
            if int(part) > 255:
                return False
        return True

    def is_ipv6(parts):
        hexdigits = "0123456789abcdefABCDEF"
        for part in parts:
            if not 1 <= len(part) <= 4:
                return False
            if not all(c in hexdigits for c in part):
                return False
        return True

    if queryIP.count('.') == 3 and is_ipv4(queryIP.split('.')):
        return "IPv4"
    if queryIP.count(':') == 7 and is_ipv6(queryIP.split(':')):
        return "IPv6"
    return "Neither"
```

**Traps**
- Skipping the empty-field check: `"1.1..1"` has the right 3 dots but an empty field;
  `len(part) >= 1` rejects it.
- `"01.01.01.01"` is invalid IPv4 (leading zero), while `"0db8"` is a valid IPv6 field: the
  leading-zero rule is IPv4-only.
- `str.isdigit()` accepts `"²"`; compare against `'0'` … `'9'`.

**Similar** — LC 93 (restore IP addresses: the same field rule, enumerated)

### Ambiguous Coordinates — LC 816

> **Key point** — two independent choices: where the comma goes, and where (if anywhere) each half's
> decimal point goes. A half is valid when its whole part has no leading zero and its fraction no
> trailing zero, so the answer is a cross product of each half's valid formats.

**Reduces to**: [string.md § Template 4](./string.md#template-4-parse--rebuild-by-phases--lc-8-) — the same leading-zero / format rules as a number parse, applied to every split position instead of to one given string.

```python
# python
# LC 816 - Ambiguous Coordinates
# IDEA: for every comma position, cross the valid formats of the left and right halves
# time = O(n^4): O(n) commas x O(n) formats per half on each side x O(n) to build each string,
# space = O(n^4) for the output
def ambiguousCoordinates(s):
    def formats(part):
        out = []
        n = len(part)
        if n == 1 or part[0] != '0':            # whole number: no leading zero
            out.append(part)
        for i in range(1, n):                   # decimal: '.' after each interior position
            whole, frac = part[:i], part[i:]
            if len(whole) > 1 and whole[0] == '0':
                continue                        # "01.5": leading zero in the whole part
            if frac[-1] == '0':
                continue                        # "1.50": trailing zero in the fraction
            out.append(whole + "." + frac)
        return out

    digits = s[1:-1]
    result = []
    for i in range(1, len(digits)):             # the comma goes after digits[:i]
        for left in formats(digits[:i]):
            for right in formats(digits[i:]):
                result.append("(" + left + ", " + right + ")")
    return result
```

```text
s = "(0123)"  ->  digits = "0123"
"0"   | "123"  ->  ["0"]  x ["123", "1.23", "12.3"]  ->  3
"01"  | "23"   ->  ["0.1"] x ["23", "2.3"]           ->  2
"012" | "3"    ->  ["0.12"] x ["3"]                  ->  1
total 6
```

**Traps**
- `"0"` alone is a valid whole number, `"00"` and `"01"` are not: test `n == 1` before the
  leading-zero rule, or `"(00011)"` loses `(0, 0.011)`.
- Any fraction ending in `0` is invalid, including `"1.0"`; otherwise `"1.0"` and `"1"` both appear.

**Similar** — LC 93 (restore IP addresses) · LC 282 (expression add operators)

### Integer to English Words — LC 273 ⭐⭐⭐⭐

> **Key point** — English names a number in 3-digit chunks, each followed by its scale
> (Thousand, Million, Billion); spell 1–999 once and reuse it per chunk, and an all-zero chunk
> says nothing at all.

**Reduces to**: [string.md § Template 4](./string.md#template-4-parse--rebuild-by-phases--lc-8-) — the rebuild direction, like LC 12's greedy table: the table here is the scale list, and each chunk is spelled from the below-20 and tens tables. A recursive form that peels off the largest scale lives in [recursion.md](./recursion.md).

```python
# python
# LC 273 - Integer to English Words
# IDEA: split into 3-digit chunks low to high, spell each, tag with its scale, prepend
# time = O(1) (at most 4 chunks for a 32-bit int), space = O(1)
BELOW_20 = ["", "One", "Two", "Three", "Four", "Five", "Six", "Seven", "Eight",
            "Nine", "Ten", "Eleven", "Twelve", "Thirteen", "Fourteen", "Fifteen",
            "Sixteen", "Seventeen", "Eighteen", "Nineteen"]
TENS = ["", "", "Twenty", "Thirty", "Forty", "Fifty", "Sixty", "Seventy", "Eighty", "Ninety"]
SCALES = ["", "Thousand", "Million", "Billion"]

def numberToWords(num):
    if num == 0:
        return "Zero"

    def three(n):                       # spell 1..999 as a list of words
        if n == 0:
            return []
        if n < 20:
            return [BELOW_20[n]]
        if n < 100:
            return [TENS[n // 10]] + three(n % 10)
        return [BELOW_20[n // 100], "Hundred"] + three(n % 100)

    parts = []
    scale = 0
    while num > 0:
        chunk = num % 1000
        if chunk != 0:                  # an all-zero chunk says nothing, not even its scale
            words = three(chunk)
            if scale > 0:
                words.append(SCALES[scale])
            parts = words + parts       # chunks arrive low to high: prepend
        num //= 1000
        scale += 1
    return " ".join(parts)
```

```java
// java
// LC 273 - Integer to English Words
// IDEA: split into 3-digit chunks low to high, spell each, tag with its scale, prepend
// time = O(1) (at most 4 chunks for a 32-bit int), space = O(1)
private static final String[] BELOW_20 = {"", "One", "Two", "Three", "Four", "Five",
    "Six", "Seven", "Eight", "Nine", "Ten", "Eleven", "Twelve", "Thirteen",
    "Fourteen", "Fifteen", "Sixteen", "Seventeen", "Eighteen", "Nineteen"};
private static final String[] TENS = {"", "", "Twenty", "Thirty", "Forty", "Fifty",
    "Sixty", "Seventy", "Eighty", "Ninety"};
private static final String[] SCALES = {"", "Thousand", "Million", "Billion"};

public String numberToWords(int num) {
    if (num == 0) {
        return "Zero";
    }
    List<String> parts = new ArrayList<>();
    int scale = 0;
    while (num > 0) {
        int chunk = num % 1000;
        if (chunk != 0) {                     // an all-zero chunk says nothing
            List<String> words = three(chunk);
            if (scale > 0) {
                words.add(SCALES[scale]);
            }
            parts.addAll(0, words);           // chunks arrive low to high: prepend
        }
        num /= 1000;
        scale++;
    }
    return String.join(" ", parts);
}

private List<String> three(int n) {           // spell 1..999
    List<String> out = new ArrayList<>();
    if (n == 0) {
        return out;
    }
    if (n < 20) {
        out.add(BELOW_20[n]);
    } else if (n < 100) {
        out.add(TENS[n / 10]);
        out.addAll(three(n % 10));
    } else {
        out.add(BELOW_20[n / 100]);
        out.add("Hundred");
        out.addAll(three(n % 100));
    }
    return out;
}
```

**Traps**
- `1000010` must read `"One Million Ten"`: the middle chunk is 0, and emitting its scale gives
  `"One Million Thousand Ten"`.
- `num == 0` is the only place `"Zero"` is written; an inner zero contributes nothing.
- 10 to 19 are irregular words: handle `n < 20` before the tens branch, or 15 becomes `"Ten Five"`.
- Joining strings with `" "` and trimming invites double spaces; joining a list of words cannot
  produce one.
