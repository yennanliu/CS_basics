# String Operations & Language APIs

> **Scope** — The language mechanics of strings: Python slicing and methods, Java `String` and `StringBuilder`, character classification and case conversion, char arithmetic, the split/join traps and the build-performance rules — not the algorithms that use them.
> **See also**: [string.md](./string.md) — the parent sheet, holding the string pattern catalogue and its templates; [string_examples.md](./string_examples.md) — the worked LC solution archive split out of the same file; [python_trick.md](./python_trick.md) and [java_trick.md](./java_trick.md) — the general language-idiom sheets; [Collection.md](./Collection.md) — choosing among Java's collections.

## LeetCode Problem Lists

- [String](https://leetcode.com/problem-list/string/)

## Overview

Nothing here is an algorithm. It is the layer underneath: the calls you reach for without
thinking, plus the handful that quietly cost you a submission — `split` taking a regex in Java,
`+=` in a loop being quadratic, `Counter` keeping zero entries, `isalpha()` being true for
non-ASCII letters.

### Key Properties
- **Immutability**: `String` is immutable in both Python and Java — every "modification" allocates
- **Cost model**: concatenation is O(len); doing it in a loop is O(n²). Collect into a list / `StringBuilder` and join once
- **Char arithmetic**: `ord(c) - ord('a')` in Python, `c - 'a'` in Java, is the standard index into a 26-slot table
- **When to read this**: before an interview, and whenever a "simple" string call behaved unexpectedly

## Python String Operations
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

## Java String Operations
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

## Character Classification & Case

The predicates and case conversions that the two-pointer and parsing templates in
[string.md](./string.md) lean on.

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

- ⚠️ Python's `isalpha()` / `isdigit()` are **Unicode**-aware: `'²'.isdigit()` is `True` and
  `'é'.isalpha()` is `True`. When a problem says "lowercase English letters", test with
  `'a' <= c <= 'z'` if you need to be exact.
- ⚠️ Java's `Character.isLetter` is likewise Unicode-aware; `Character.isLetterOrDigit` is the
  one the LC 125 style "skip non-alphanumeric" scans want.
- ⚠️ Case folding is **not** always a round trip — `toLowerCase` on some locales changes length.
  Interview inputs are ASCII, but say so out loud rather than assuming it.

## String Manipulation Tricks
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

## String Building Performance
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

- ⚠️ `s += x` inside a loop is O(n²) in **both** languages. CPython sometimes optimises the
  refcount-1 case, but never rely on it.
- ⚠️ `StringBuilder` is not thread-safe (that is `StringBuffer`) — irrelevant in interviews,
  worth knowing as a follow-up answer.
- ⚠️ Pre-size when you know the answer's length: `new StringBuilder(n)` avoids the doubling copies.

## Summary & Quick Reference

| Task | Python | Java |
|---|---|---|
| String → chars | `list(s)` | `s.toCharArray()` |
| Chars → string | `"".join(chars)` | `new String(chars)` |
| Reverse | `s[::-1]` | `new StringBuilder(s).reverse().toString()` |
| Substring | `s[i:j]` | `s.substring(i, j)` |
| Every other char | `s[::2]` | manual loop with `i += 2` |
| Split on whitespace | `s.split()` | `s.trim().split("\\s+")` |
| Split on a literal dot | `s.split(".")` | `s.split("\\.")` — the argument is a **regex** |
| Split keeping empty tails | `s.split(",")` | `s.split(",", -1)` |
| Join with separator | `",".join(parts)` | `String.join(",", parts)` |
| Strip whitespace | `s.strip()` | `s.trim()` / `s.strip()` (Java 11+) |
| Case fold | `s.lower()` / `s.upper()` | `s.toLowerCase()` / `s.toUpperCase()` |
| Replace | `s.replace(a, b)` | `s.replace(a, b)` |
| Find substring | `s.find(p)` (`-1` if absent) | `s.indexOf(p)` (`-1` if absent) |
| Char → code / code → char | `ord(c)` / `chr(n)` | `(int) c` / `(char) n` |
| Index into the 26 letters | `ord(c) - ord('a')` | `c - 'a'` |
| Is letter / digit / alnum | `c.isalpha()` / `c.isdigit()` / `c.isalnum()` | `Character.isLetter(c)` / `isDigit(c)` / `isLetterOrDigit(c)` |
| Frequency table | `Counter(s)` | `int[26]` or `HashMap<Character,Integer>` |
| Build incrementally | list `append` then `join` | `StringBuilder.append` then `toString` |

### Gotchas worth memorising
1. **Java `split` is regex-based.** `"a.b".split(".")` returns an array of empty strings.
2. **Java `split` drops trailing empties.** `"a,,".split(",")` has length 1; pass `-1` to keep them.
3. **Python slicing never throws.** `s[5:99]` on a 3-char string is `""`, not an error — convenient, and a silent-bug source.
4. **`Counter` keeps zeros.** After decrementing to `0`, `del` the key or the `==` comparison against a fresh `Counter` fails.
5. **Never concatenate in a loop.** Collect and join.
