# Python Tricks & Idioms

> **Scope** — The Python language idioms that come up while solving problems — copying, string handling, sort keys, integer arithmetic, comprehensions and scope — grouped by what you are trying to do. The standard library and the index arithmetic have their own sheets.
> **See also**: [python_trick_stdlib.md](./python_trick_stdlib.md) — `heapq`, `bisect`, `SortedDict`, `collections`, `itertools`, `functools`; [python_trick_indexing.md](./python_trick_indexing.md) — insertion, slicing and the off-by-one arithmetic behind them; [python_gotchas.md](./python_gotchas.md) — the behaviours that are surprising rather than merely useful; [java_trick.md](./java_trick.md) — the same ground in Java.

## LeetCode Problem Lists

- [Python](https://leetcode.com/problemset/all/?languageTags=python3)

## Overview

This sheet used to be 3,672 lines under a single `## 1) Examples` heading, with 68 entries
numbered `0-1)`, `1-11''')` and `1-27-3)` in no particular order. The numbers are gone; find
things by what they do.

| I want to… | Go to |
|---|---|
| copy a list or dict without the copy being an alias | [Copying & References](#copying--references) |
| slice, pad, strip, split or rebuild a string | [Strings](#strings) |
| sort by something other than the natural order | [Sorting & Comparison](#sorting--comparison) |
| divide, round, take a remainder, or avoid overflow surprises | [Numbers & Math](#numbers--math) |
| loop over two things at once, or build a list in one line | [Iteration, Comprehensions & Functional Tools](#iteration-comprehensions--functional-tools) |
| count things, or use a default instead of a `KeyError` | [Dicts & Sets](#dicts--sets), or `Counter` / `defaultdict` in [python_trick_stdlib.md](./python_trick_stdlib.md) |
| write to a variable from inside a nested function | [Structure, Scope & Return Values](#structure-scope--return-values) |
| use a heap, a binary search, an ordered map, or `itertools` | [python_trick_stdlib.md](./python_trick_stdlib.md) |
| insert into a list, slice a subarray, or get an off-by-one right | [python_trick_indexing.md](./python_trick_indexing.md) |


## Copying & References

### Assignment vs shallow copy vs deep copy

- https://www.runoob.com/w3cnote/python-understanding-dict-copy-shallow-or-deep.html
- https://iter01.com/578999.html
- Type of copy : deep copy, shallow copy, reference copy
```python
# LC 138

#-------------------------------------------------
# CASE 1) assignment : point to the same instance
#-------------------------------------------------

In [112]: z = [1,2,3]

In [113]: x = y = z

In [114]: x
Out[114]: [1, 2, 3]

In [115]: y
Out[115]: [1, 2, 3]

In [116]: z
Out[116]: [1, 2, 3]

In [117]: z.append(4)

In [118]: z
Out[118]: [1, 2, 3, 4]

In [119]: x
Out[119]: [1, 2, 3, 4]

In [120]: y
Out[120]: [1, 2, 3, 4]

In [121]: z
Out[121]: [1, 2, 3, 4]


#-------------------------------------------------
# CASE 2) shallow copy : copy "parent" instance, but NOT sub instance
#-------------------------------------------------

# https://docs.python.org/zh-tw/3/tutorial/datastructures.html
# form 1
a.copy()

# form 2
a[:]

# demo
In [90]: x = [1,2,3]

In [91]: y = x[:]

In [92]: y
Out[92]: [1, 2, 3]

In [93]: x.append(4)

In [94]: x
Out[94]: [1, 2, 3, 4]

In [96]: y
Out[96]: [1, 2, 3]


# LC 77 Combinations
class Solution(object):
    def combine(self, n, k):
        result = []
        
        def dfs(current, start):
            if(len(current) == k):
                result.append(current[:])
                return
            
            for i in range(start, n + 1):
                current.append(i)
                dfs(current, i + 1)
                current.pop()
            
        dfs([], 1)
        return result

#-------------------------------------------------
# CASE 3) deep copy : copy "parent" instance, AND sub instance
#-------------------------------------------------

import copy

In [25]: import copy
    ...:
    ...: x = [1,2,3]
    ...: z =  copy.deepcopy(x)
    ...:
    ...: x
Out[25]: [1, 2, 3]

In [26]:

In [26]: z
Out[26]: [1, 2, 3]

In [27]: x.append(4)

In [28]: x
Out[28]: [1, 2, 3, 4]

In [29]: z
Out[29]: [1, 2, 3]

In [31]: z.append(5)

# NOTE : x, z NOT affect on each other

In [32]: z
Out[32]: [1, 2, 3, 5]

In [33]: x
Out[33]: [1, 2, 3, 4]
```

### Which copy gives an INDEPENDENT object


**The question:** how do we make a copy so that mutating the *original* does NOT
affect the *copy* (and vice versa)?

```python
# ── The idiom you'll see everywhere (shallow copy of a list) ──
# NOTE !!! how we make copy in py
intervals_cache = intervals[:]        # copy 1) slice
intervals_cache = intervals.copy()    # copy 2) .copy()  (same effect)
intervals_cache = list(intervals)     # copy 3) list()   (same effect)
```

**Key distinction — it depends on whether the list is FLAT or NESTED:**

```python
#----------------------------------------------------------
# CASE A) FLAT list (ints, strings, …) → shallow copy IS enough
#----------------------------------------------------------
original = [1, 2, 3]
copy_    = original[:]         # independent copy

original.append(4)
original[0] = 99
print(original)   # [99, 2, 3, 4]
print(copy_)      # [1, 2, 3]     ← NOT affected  ✅

#----------------------------------------------------------
# CASE B) NESTED list (list of lists / objects) → shallow copy is NOT enough
#----------------------------------------------------------
original = [[1, 2], [3, 4]]
shallow  = original[:]        # copies OUTER list only; inner lists are SHARED

original.append([5, 6])       # outer-level change → safe
print(shallow)                # [[1, 2], [3, 4]]   ← NOT affected  ✅

original[0][0] = 99           # inner-level change → LEAKS through!
print(shallow)                # [[99, 2], [3, 4]]  ← AFFECTED  ❌

# → to be FULLY independent, use deepcopy:
import copy
deep = copy.deepcopy(original)
original[0][0] = -1
print(deep)                   # inner list unchanged  ✅
```

**Decision table — "I want a copy the original CANNOT affect"**

| Data shape | Use | Independent? |
|------------|-----|--------------|
| `x = y`  (assignment) | — | ❌ same object, all changes leak |
| Flat list `[1,2,3]` | `x[:]` / `x.copy()` / `list(x)` | ✅ fully independent |
| Nested list `[[..],[..]]` | `x[:]` (shallow) | ⚠️ outer only — inner leaks |
| Nested list / dict / objects | `copy.deepcopy(x)` | ✅ fully independent |
| Dict (flat values) | `d.copy()` / `dict(d)` / `{**d}` | ✅ (values shared if mutable) |

**Rule of thumb**
- `[:]`, `.copy()`, `list()` → **shallow**: safe only when elements are *immutable*
  (int, str, tuple) OR you only mutate the top level.
- `copy.deepcopy()` → **deep**: safe for any nesting, but slower — use only when you
  actually mutate nested elements.

> Classic backtracking use (see LC 77 above): `result.append(current[:])` snapshots the
> *current* path so later `current.pop()` / `current.append()` don't corrupt the saved
> result — works because path elements are immutable ints.

## Strings

### Sorting the characters of a string

```python
def _sort(x):
    _x = list(x)
    _x.sort()
    return "".join(_x)

x = "bca"
print (x)
x_ = _sort(x)
print (x_)
```

### Replacing a character by index (slice + concat)


**Key point: Python strings are IMMUTABLE** — you CANNOT do `s[i] = ch`
(that raises `TypeError: 'str' object does not support item assignment`).
To "change the char at index `i`", rebuild a **new** string by slicing around `i`.

```python
# ── The idiom ──
#   s[:i]      -> everything BEFORE index i   (i excluded)
#   ch         -> the new character to place at index i
#   s[i+1:]    -> everything AFTER index i    (i excluded, i.e. drop old s[i])
new_s = s[:i] + ch + s[i+1:]
```

```python
# demo
In [1]: s = "AACCGGTT"

In [2]: s[:3] + "X" + s[3+1:]     # replace index 3 ('C') with 'X'
Out[2]: 'AACXGGTT'

# original is untouched (immutable) — a NEW string is returned
In [3]: s
Out[3]: 'AACCGGTT'
```

**Why the `i+1`?** `s[i+1:]` starts *after* `i`, so the old char `s[i]` is
dropped and replaced by `ch`. Using `s[i:]` instead would keep the old char
(insert rather than replace):

```python
s = "abc"
s[:1] + "X" + s[1+1:]   # 'aXc'  ← REPLACE index 1  (skip old 'b')
s[:1] + "X" + s[1:]     # 'aXbc' ← INSERT before index 1 (old 'b' kept)
```

**Classic LC use — LC 433 Minimum Genetic Mutation** (BFS, mutate one gene char at a time):

```python
# for each position i, try each candidate char ch
for i in range(len(cur_gene)):
    for ch in "ACGT":
        if ch == cur_gene[i]:
            continue
        # build the neighbor gene with position i mutated to ch
        new_gene = cur_gene[:i] + ch + cur_gene[i+1:]
        ...
```

> **Alternatives**: if you mutate many positions, convert to a `list` first
> (`arr = list(s); arr[i] = ch; s = "".join(arr)`) — lists ARE mutable, so
> in-place index assignment works and avoids repeated string rebuilds.
> For a single edit, the slice idiom above is the cleanest.

### `split`

```python
# python
# syntax : split(separator, number_of_split_result) 
# example 1
# In [17]: x = 'dig1 8 1 5 1'

# In [18]: x
# Out[18]: 'dig1 8 1 5 1'

# In [19]: x.split(" ")
# Out[19]: ['dig1', '8', '1', '5', '1']

# In [20]: x.split(" ", 1)
# Out[20]: ['dig1', '8 1 5 1']

# In [21]: x.split(" ", 2)
# Out[21]: ['dig1', '8', '1 5 1']

# In [22]: x.split(" ", 3)
# Out[22]: ['dig1', '8', '1', '5 1']

# In [23]: x.split(" ", 4)
# Out[23]: ['dig1', '8', '1', '5', '1']

# In [24]: x.split(" ", 100)
# Out[24]: ['dig1', '8', '1', '5', '1']

# example 2
# LC 937 Reorder Data in Log Files
class Solution:
    def reorderLogFiles(self, logs):
        def f(log):
            id_, rest = log.split(" ", 1)
            return (0, rest, id_) if rest[0].isalpha() else (1,)

        logs.sort(key = lambda x : f(x))
        return logs #sorted(logs, key = f)
```


### Zero-padding a string

```python
# LC 67. Add Binary
#NOTE : zfill syntax
#    -> fill n-1 "0" to a string at beginning

#example :
In [10]: x = '1'

In [11]: x.zfill(2)
Out[11]: '01'

In [12]: x.zfill(3)
Out[12]: '001'

In [13]: x.zfill(4)
Out[13]: '0001'

In [14]: x.zfill(10)
Out[14]: '0000000001'
```

### `lstrip` / `rstrip` / `strip`


```python
# python
# syntax : s.lstrip(chars) / s.rstrip(chars) / s.strip(chars)
#   - lstrip : remove matching chars from the LEFT  (leading)
#   - rstrip : remove matching chars from the RIGHT (trailing)
#   - strip  : remove matching chars from BOTH ends
#   - `chars` is a SET of characters to remove (NOT a substring/prefix!)
#   - no arg -> strips whitespace (space, \t, \n, \r ...)
#   - it does NOT touch chars in the MIDDLE, and returns a NEW string
```

```python
#----------------------------
# example 1 : strip leading zeros (lstrip)
#----------------------------
In [1]: x = "0000123"
In [2]: x.lstrip("0")
Out[2]: '123'

# no leading zeros -> unchanged (safe, no error)
In [3]: y = "123"
In [4]: y.lstrip("0")
Out[4]: '123'

# all chars match -> empty string (WATCH OUT!)
In [5]: "0000".lstrip("0")
Out[5]: ''

#----------------------------
# example 2 : rstrip (trailing)
#----------------------------
In [6]: "12300".rstrip("0")
Out[6]: '123'

In [7]: "hello!!!".rstrip("!")
Out[7]: 'hello'

# common: drop trailing newline / whitespace when reading input
In [8]: "  line\n".rstrip()
Out[8]: '  line'

#----------------------------
# example 3 : strip (both ends) + whitespace default
#----------------------------
In [9]: "  hi  ".strip()
Out[9]: 'hi'

In [10]: "xxhixx".strip("x")
Out[10]: 'hi'

#----------------------------
# example 4 : GOTCHA — `chars` is a char SET, not a prefix string
#----------------------------
# removes ANY leading char that is 'a','b', or 'c' (in any order),
# NOT the literal prefix "abc"
In [11]: "cabbage".lstrip("abc")
Out[11]: 'ge'          # 'c','a','b','b','a' all stripped, stops at 'g'

# to remove a real PREFIX/SUFFIX (py3.9+), use removeprefix / removesuffix:
In [12]: "test.py".removesuffix(".py")
Out[12]: 'test'
In [13]: "img_001".removeprefix("img_")
Out[13]: '001'
```

**Common LC use — strip leading zeros after building a number**

```python
# LC 402. Remove K Digits
# after building the result digits in a stack, the front may have leading zeros
# e.g. stack -> "0200"  (must return "200", and "" must become "0")

res = "".join(stack).lstrip('0')   # "0200" -> "200",  "0000" -> ""
return res if res else "0"         # handle the all-zero / empty case
```

> **Rule of thumb**: `lstrip('0')` is the idiomatic way to normalize a numeric string
> (drop leading zeros) — but always guard the **empty-string** result (`res or "0"`),
> since `"0"`/`"0000"` strip down to `""`. See also [`zfill`](#zero-padding-a-string)
> for the inverse (padding zeros).

### `ord()`, `chr()`, `isalpha()`, `isdigit()`

```python
#-------------------------------
# ord() : char -> ASCII int
# chr() : ASCII int -> char
#-------------------------------
print(ord('a'))   # 97
print(ord('A'))   # 65
print(ord('0'))   # 48

print(chr(97))    # 'a'
print(chr(65))    # 'A'

# Common pattern: normalize letter to 0-25 index
ch = 'c'
idx = ord(ch) - ord('a')   # 2

# Shift a character by n positions
def shift(ch, n):
    return chr((ord(ch) - ord('a') + n) % 26 + ord('a'))

#-------------------------------
# String check methods
#-------------------------------
"abc".isalpha()     # True  — all letters
"abc123".isalpha()  # False
"123".isdigit()     # True  — all digits
"abc123".isalnum()  # True  — letters + digits
"  ".isspace()      # True
"ABC".isupper()     # True
"abc".islower()     # True

# LC 125 Valid Palindrome
def isPalindrome(s):
    filtered = [c.lower() for c in s if c.isalnum()]
    return filtered == filtered[::-1]

# NOTE: lower() has NO effect on digits — safe to call on any alphanumeric char
# >>> "0".lower()
# '0'
# >>> "A".lower()
# 'a'
# So the one-liner below works for both letters and numbers:
fixed_s = ''.join(c.lower() for c in s if c.isalnum())
```

### String methods cheatsheet

```python
s = "  Hello, World!  "

# Strip whitespace (or specific chars)
s.strip()          # "Hello, World!"
s.lstrip()         # "Hello, World!  "
s.rstrip()         # "  Hello, World!"
s.strip("!")       # "  Hello, World!  " (only strips specified chars from ends)

# Case
s.lower()          # "  hello, world!  "
s.upper()          # "  HELLO, WORLD!  "
s.title()          # "  Hello, World!  "
s.swapcase()       # "  hELLO, wORLD!  "

# Search
s.find("World")    # 9  (-1 if not found)
s.index("World")   # 9  (raises ValueError if not found)
s.count("l")       # 3
s.startswith("  H")  # True
s.endswith("!  ")    # True

# Replace and join
s.replace("World", "Python")   # "  Hello, Python!  "
", ".join(["a", "b", "c"])     # "a, b, c"
"a,b,c".split(",")             # ['a', 'b', 'c']

# Reverse a string
rev = s[::-1]

# String multiplication
"ab" * 3   # "ababab"
"-" * 10   # "----------"

# Check if string is a palindrome
def is_palindrome(s):
    return s == s[::-1]
```

### Pulling the numeric characters out of a string

```python
# LC 008
s = '4193 with words'
res = re.search('(^[\+\-]?\d+)', s).group()
print (res)
```

### Looping a string in reverse


**Key difference: stop value is EXCLUSIVE in `range()`**

```python
x = "332"   # indices: 0, 1, 2

# ── Form 1: range(len(x)-1, -1, -1)  → stop = -1 (exclusive) → covers 2, 1, 0 (ALL indices)
for i in range(len(x)-1, -1, -1):
    print(i)
# 2
# 1
# 0   ← index 0 IS included

# ── Form 2: range(len(x)-1, 0, -1)   → stop = 0 (exclusive) → covers 2, 1 (MISSES index 0)
for i in range(len(x)-1, 0, -1):
    print(i)
# 2
# 1   ← index 0 is NOT included
```

| Form | Stop value | Indices visited | Includes index 0? |
|------|-----------|-----------------|-------------------|
| `range(len(x)-1, -1, -1)` | `-1` (exclusive) | `len-1 … 0` | **Yes** |
| `range(len(x)-1,  0, -1)` | ` 0` (exclusive) | `len-1 … 1` | **No** |

**Rule of thumb:** to loop ALL indices in reverse, always use `-1` as the stop value.

```python
# Equivalent ways to iterate a string/array in reverse (all indices)
x = "abc"

# Form A: range
for i in range(len(x) - 1, -1, -1):
    print(x[i])

# Form B: reversed() — cleaner, no index needed
for ch in reversed(x):
    print(ch)

# Form C: slice — creates a reversed copy
for ch in x[::-1]:
    print(ch)
```

**When you DO want to skip index 0** (e.g. comparing `x[i]` with `x[i-1]`):
```python
# Safe to start from index 1 in forward loops, or stop before 0 in reverse loops
for i in range(len(x) - 1, 0, -1):   # compares x[i] vs x[i-1]; never i-1 = -1
    if x[i] == x[i - 1]:
        print(f"duplicate at {i}")
```

## Sorting & Comparison

### `sort` with a `lambda` key

```python
# example 1
# LC 973. K Closest Points to Origin
# IDEA : sort + lambda
class Solution(object):
    def kClosest(self, points, K):
        points.sort(key = lambda x : x[0]**2 +  x[1]**2)
        return points[:K]


# example 2
In [28]: def my_func(x):
    ...:     return x**2
    ...:
    ...: x = [-4,-5,0,1,2,5]
    ...: x.sort(key=lambda x: my_func(x))
    ...: print (x)
[0, 1, 2, -4, -5, 5]
```

```python
# LC 937
# https://leetcode.com/problems/reorder-data-in-log-files/solution/
def my_func(input):
    # do sth
    if condition:
        return key1, key2, key3....
    else:
        return key4, key5, key6....

my_array=["a1 9 2 3 1","g1 act car","zo4 4 7","ab1 off key dog","a8 act zoo"]
my_array.sort(key=lambda x : my_func(x))
```

### Descending: `key=lambda x: -x[0]` vs `reverse=True` vs `[::-1]`


Three ways to sort descending — each has a distinct use case.

```python
# ── Context: LC 853 Car Fleet ──
# We have pos_speed = [[pos, speed, time], ...] and want to sort by position DESC.

# ── Form 1: negate the key  (in-place, fine-grained control) ──
pos_speed.sort(key=lambda x: -x[0])
# Use when:
#   • You need MIXED direction: primary DESC, secondary ASC
#     e.g. sort(key=lambda x: (-x[0], x[1]))  ← impossible with reverse=True alone
#   • Works for int and float keys

# ── Form 2: reverse=True  (cleaner for single-direction reversal) ──
pos_speed.sort(key=lambda x: x[0], reverse=True)
sorted_cars = sorted(cars, reverse=True)   # creates a NEW list
# Use when:
#   • ALL keys go the same direction (all DESC)
#   • More readable for simple cases
#   • sorted() is preferred over sort() when you need to keep the original

# ── Form 3: sort ASC then reverse/slice  (separate steps) ──
times = [(target - pos) / spe for pos, spe in sorted(cars)]   # ASC sort
for time in times[::-1]:        # iterate in reverse  — does NOT mutate list
    ...
# -- or --
for time in reversed(times):    # same effect, no extra list copy
    ...
# Use when:
#   • You want to keep the sorted-ASC list around for other uses
#   • reversed() is O(1) memory; [::-1] creates a new list copy

# ── Quick comparison ──
# Method              | In-place? | New list? | Mixed direction? | Readability
# -x[0]               |   yes     |    no     |      YES         |  moderate
# reverse=True        |   yes     |    no     |      no          |  high
# sorted(reverse=True)|   no      |    YES    |      no          |  high
# sort ASC + reversed |   yes     |    no     |      no          |  moderate

# ── Multi-key mixed direction example (only negation works here) ──
# Sort by position DESC, then by speed ASC as tiebreaker:
data.sort(key=lambda x: (-x[0], x[1]))
```

### Multi-key tuple sort: `key=lambda x: (x[0], x[1])` ⭐⭐⭐⭐⭐

**Key Idea**: return a **tuple** from `key`. Python compares tuples **left → right**, short-circuiting on the first unequal element. So `(-x[0], x[1])` means *"primary: x[0] DESC, tie-break: x[1] ASC"*.

**Quick Decision Table**

| Goal | Pattern |
|------|---------|
| key1 ASC | `key = lambda x : x[0]` |
| key1 DESC | `key = lambda x : -x[0]` **(numeric only)** or `reverse = True` |
| key1 ASC, key2 ASC | `key = lambda x : (x[0], x[1])` |
| key1 DESC, key2 DESC | `key = lambda x : (x[0], x[1]), reverse = True` |
| key1 DESC, key2 ASC | `key = lambda x : (-x[0], x[1])` **(key1 numeric)** |
| key1 ASC, key2 DESC | `key = lambda x : (x[0], -x[1])` **(key2 numeric)** |
| mixed dir, **non-numeric** key | **2 stable sorts** — sort by the *last* key first (see below) |
| pairwise custom rule | `functools.cmp_to_key(my_cmp)` |

```python
# python
y = [[7,0], [4,4], [7,1], [5,0], [6,1], [5,2]]
print (y)
# sort by x[0] DESC, then x[1] ASC
y.sort(key = lambda x : (-x[0], x[1]))
print (y)
# [[7, 0], [4, 4], [7, 1], [5, 0], [6, 1], [5, 2]]
# [[7, 0], [7, 1], [6, 1], [5, 0], [5, 2], [4, 4]]
```

**Visual Trace** — how the tuple key orders the above:

```text
elem     | key = (-x[0], x[1])
---------------------------------
[7,0]    | (-7, 0)
[7,1]    | (-7, 1)
[6,1]    | (-6, 1)
[5,0]    | (-5, 0)
[5,2]    | (-5, 2)
[4,4]    | (-4, 4)

sort keys ASC  ->  (-7,0) < (-7,1) < (-6,1) < (-5,0) < (-5,2) < (-4,4)
                    ^^^^^^^^^^^^^^ 1st slot tied (-7) -> compare 2nd slot (0 < 1)
=> [[7,0], [7,1], [6,1], [5,0], [5,2], [4,4]]
```

#### **`reverse = True` vs negating the key**

```python
# python
arr = [[1,'b'], [1,'a'], [2,'a']]

# (a) reverse=True flips the WHOLE ordering (every key), not just the 1st
sorted(arr, key = lambda x : (x[0], x[1]), reverse = True)
# [[2,'a'], [1,'b'], [1,'a']]   <-- x[0] DESC *and* x[1] DESC

# (b) negation flips only the negated slot
sorted(arr, key = lambda x : (-x[0], x[1]))
# [[2,'a'], [1,'a'], [1,'b']]   <-- x[0] DESC, x[1] ASC

# NOTE !!! `-` only works on numbers
sorted(arr, key = lambda x : (-x[0], -x[1]))   # ❌ TypeError : bad operand type for unary -: 'str'
```

#### **Mixed direction with a NON-numeric key → 2 stable sorts**

Timsort is **stable**, so you can chain sorts. Rule: **sort by the LAST (least significant) key first.**

```python
# python
# want : len(s) ASC, then s DESC  (can't do -s)
words = ["bb", "a", "ab", "c", "ba"]

words.sort(reverse = True)          # step 1 : least significant key (s DESC)
words.sort(key = len)               # step 2 : most  significant key (len ASC) - stable
print (words)
# ['c', 'a', 'bb', 'ba', 'ab']
#  ^^^^^^^^ len 1, DESC        ^^^^^^^^^^^^^^ len 2, DESC
```

> ⚠️ Do **NOT** reverse the order of the two passes — sorting by `len` first then by `s` would throw the `len` grouping away.

#### **`functools.cmp_to_key` — when no key function exists**

Use when ordering depends on **comparing two elements together** (no per-element value can express it).

```python
# python
# LC 179 Largest Number : concat digits to form the biggest number
# rule : a before b  iff  a+b > b+a  -> not expressible as a single key
import functools

class Solution(object):
    def largestNumber(self, nums):
        # time = O(n log n * k), space = O(n)   (k = digit length)
        def cmp(a, b):
            if a + b > b + a:
                return -1      # a comes FIRST
            elif a + b < b + a:
                return 1       # b comes FIRST
            return 0

        strs = [str(x) for x in nums]
        strs.sort(key = functools.cmp_to_key(cmp))
        res = "".join(strs)
        return "0" if res[0] == "0" else res

# nums = [3,30,34,5,9] -> "9534330"
```

#### **Other handy sort keys**

```python
# python
# 1) sort dict by VALUE desc, then KEY asc  (LC 692 Top K Frequent Words)
from collections import Counter
cnt = Counter(["i","love","leetcode","i","love","coding"])
res = sorted(cnt.keys(), key = lambda w : (-cnt[w], w))
# ['i', 'love', 'coding', 'leetcode']

# 2) sort by "distance" (LC 973 K Closest Points to Origin)
points = [[1,3],[-2,2],[5,8],[0,1]]
points.sort(key = lambda p : p[0]**2 + p[1]**2)
# [[0,1], [-2,2], [1,3], [5,8]]

# 3) sort intervals by start (LC 56 / 57 / 253 / 435)
intervals = [[1,3],[8,10],[2,6],[15,18]]
intervals.sort(key = lambda x : x[0])
# [[1,3], [2,6], [8,10], [15,18]]

# 4) sort by end   (LC 435 Non-overlapping Intervals - greedy)
intervals.sort(key = lambda x : x[1])

# 5) sort chars of a string as the canonical key (LC 49 Group Anagrams)
"".join(sorted("tea"))    # 'aet'

# 6) sort with index preserved (need original position afterwards)
nums = [5,2,8]
idx_sorted = sorted(range(len(nums)), key = lambda i : nums[i])
# [1, 0, 2]   <-- indices in value order

# 7) sort intervals by BOTH ends (LC 56 Merge Intervals, LC 252 / 253 Meeting Rooms)
intervals.sort(key = lambda x : (x[0], x[1]))

# 8) primary ASC, secondary DESC -- negate only the secondary slot
events.sort(key = lambda x : (x[0], -x[1]))

# 9) sort a list of dicts by two fields
people = [{"age": 30, "name": "bob"}, {"age": 30, "name": "amy"}]
people.sort(key = lambda p : (p["age"], p["name"]))

# 10) three keys, same rule -- tuples compare left to right for any length
tasks.sort(key = lambda t : (t[0], t[1], t[2]))

# 11) sorted() returns a NEW list; .sort() mutates in place and returns None
sorted_events = sorted(event_list, key = lambda x : (x[0], x[1]))
```

**Note on Java equivalent:**

```java
// java
int[][] people = {{7,0},{4,4},{7,1},{5,0},{6,1},{5,2}};

// V1 : explicit comparator - x[0] DESC, tie-break x[1] ASC
Arrays.sort(people, (a, b) -> {
    if (a[0] != b[0]) {
        return b[0] - a[0];   // NOTE !!! b - a  => DESC
    }
    return a[1] - b[1];       //          a - b  => ASC
});

// V2 : Comparator chaining (more readable, Java 8+)
Arrays.sort(people,
        Comparator.<int[]>comparingInt(a -> a[0]).reversed()   // key1 DESC
                  .thenComparingInt(a -> a[1]));               // key2 ASC

// List version
List<int[]> list = new ArrayList<>();
list.sort(Comparator.<int[]>comparingInt(a -> a[0]).reversed()
                    .thenComparingInt(a -> a[1]));

// ⚠️ `b[0] - a[0]` can OVERFLOW for large/negative values
//    -> use Integer.compare(b[0], a[0]) instead
Arrays.sort(people, (a, b) -> Integer.compare(b[0], a[0]));

// ⚠️ `.reversed()` after `.thenComparing(...)` reverses the WHOLE chain
//    (same trap as python's reverse=True)
Comparator.comparingInt((int[] a) -> a[0])
          .thenComparingInt(a -> a[1])
          .reversed();          // <-- key1 DESC *and* key2 DESC
```

**🚫 Common Mistakes:**

```python
# 1) Expecting reverse=True to flip only the primary key
arr.sort(key = lambda x : (x[0], x[1]), reverse = True)   # ❌ flips BOTH keys
arr.sort(key = lambda x : (-x[0], x[1]))                  # ✅ only key1 DESC

# 2) Negating a string / tuple
arr.sort(key = lambda x : (-x[0], -x[1]))   # ❌ TypeError if x[1] is str
# ✅ use 2 stable sorts, or cmp_to_key

# 3) Comparing mixed types inside the key
sorted([1, "a"])            # ❌ TypeError : '<' not supported between 'str' and 'int'

# 4) Multi-pass sorts in the WRONG order
words.sort(key = len); words.sort(reverse = True)   # ❌ len grouping destroyed
words.sort(reverse = True); words.sort(key = len)   # ✅ least significant key FIRST

# 5) Forgetting the sort is what makes the greedy/2-pointer step valid
#    -> for interval problems, ALWAYS state which key you sorted on
```

**💡 Interview Tips:**

- Say it out loud as *"sort by A descending, break ties by B ascending"* → then write the tuple key.
- **Complexity**: `O(n log n)` comparisons; each tuple-key build is `O(k)` for k keys → `O(n log n * k)`.
- Both `list.sort()` and `sorted()` are **stable** — this is what makes the multi-pass trick and `LC 406`-style insert work.
- If you can't express the rule as a per-element key, that's the signal for `cmp_to_key` (py) / a custom `Comparator` (java).

**Related LeetCode Problems:**

| Problem | LC# | Sort key |
|---------|-----|----------|
| **Queue Reconstruction by Height** | **406** | `(-h, k)` — tall first, then k ASC |
| Largest Number | 179 | `cmp_to_key(a+b vs b+a)` |
| Top K Frequent Words | 692 | `(-count, word)` |
| K Closest Points to Origin | 973 | `x² + y²` |
| Merge Intervals | 56 | `start` ASC |
| Meeting Rooms II | 253 | `start` ASC (+ min-heap on end) |
| Non-overlapping Intervals | 435 | `end` ASC (greedy) |
| Group Anagrams | 49 | `"".join(sorted(word))` |
| Custom Sort String | 791 | `order.index(ch)` |
| Sort Array By Parity | 905 | `x % 2` |
| Relative Sort Array | 1122 | `(rank.get(x, len), x)` |
| Car Fleet | 853 | `position` DESC (+ stack) |
| Boats to Save People | 881 | `weight` ASC (+ 2 pointers) |

**Summary:**
- ✅ Tuple key = multi-key sort, compared **left → right**
- ✅ `-key` flips one slot (**numeric only**); `reverse=True` flips **all** slots
- ✅ Mixed direction on non-numeric → **2 stable sorts, least significant key first**
- ✅ No per-element key expressible → `functools.cmp_to_key` / java `Comparator`
- ✅ Java: `Integer.compare(b, a)` over `b - a` to dodge overflow; `.reversed()` applies to the whole chain

### Conditional tuple keys via a named `key` function


When the sort key depends on a **condition** (group A vs group B, valid vs invalid,
etc.), a one-line lambda gets unreadable. Write a **named `key` function that returns
a tuple** — the tuple is still compared element-by-element (left → right), so the
first field becomes the primary sort, the next the tiebreaker, and so on.

**Pattern: leading "group tag" + per-group ordering**

```python
# Return a tuple of sort keys; the FIRST element groups items,
# the rest order items WITHIN each group.
def compare(item):
    if condition:
        return (0, item.value, item.name)   # group 0 first; ASC by value, then name
    else:
        return (1, -item.priority, item.id)  # group 1 next; DESC by priority, ASC by id

items.sort(key=compare)        # in-place
# items = sorted(items, key=compare)   # or build a new list
```

**Why the leading `0` / `1`?** It is a **group tag** — every group-0 item sorts before
every group-1 item (because tuple comparison checks the first element first). The
remaining tuple fields only matter *within* the same group, so each group can use its
own ordering rules (ASC, DESC via negation, different fields entirely).

**Key rules**
- All branches must return a tuple of the **same length** with **comparable types**
  position-by-position (don't mix `str` and `int` in the same slot).
- Negate a numeric field (`-item.priority`) to sort that field DESC while keeping the
  rest ASC — same trick as section [1-11'].
- The `key` function is called **once per element** (Schwartzian transform), so it's
  efficient even with heavier logic inside.

**Classic LC use — LC 937 Reorder Data in Log Files** (letter-logs grouped before
digit-logs, letter-logs sorted by content then id):

```python
class Solution:
    def reorderLogFiles(self, logs):
        def compare(log):
            id_, rest = log.split(" ", 1)
            if rest[0].isalpha():
                return (0, rest, id_)   # letter-logs: group 0, by content, then id
            else:
                return (1,)             # digit-logs: group 1, keep original order

        return sorted(logs, key=compare)
```

> **Rule of thumb**: reach for a named tuple-returning `key` function the moment the
> ordering has *branches* — it reads far better than cramming `if/else` into a lambda.

### Sorting a dict by frequency

```python
# LC 451 Sort Characters By Frequency
# V1
import collections
class Solution(object):
    def frequencySort(self, s):
        d = collections.Counter(s)
        d_dict = dict(d)
        res = []
        for x in sorted(d_dict, key=lambda k : -d_dict[k]):
            res.append(x)
        return res

x= [1, 2, 3, 1, 2, 1, 2, 1]
s = Solution()
r = s.frequencySort(x)

# V2
# https://stackoverflow.com/questions/613183/how-do-i-sort-a-dictionary-by-value
import collections
class Solution(object):
    def frequencySort(self, s):
        d = collections.Counter(s)
        d_dict = dict(d)
        res = []
        #for x in sorted(d_dict.items(), key=lambda items: -items[1]):
        for _ in sorted(d_dict.items(), key=lambda x: -x[1]):
            res.append(_)
        return res

x= [1, 2, 3, 1, 2, 1, 2, 1]
s = Solution()
r = s.frequencySort(x)
```

### `min()` / `max()` with `key`

```python
# key= works exactly like sort's key= parameter
nums = [-3, -1, 2, 4]
print(max(nums, key=abs))   # -3  (largest absolute value)
print(min(nums, key=abs))   # -1  (smallest absolute value)

# With iterable of tuples
points = [(1, 5), (3, 2), (2, 8)]
print(max(points, key=lambda p: p[1]))  # (2, 8)

# min/max with default (avoids error on empty iterable)
print(min([], default=0))   # 0

# clamp a value between lo and hi
val = max(lo, min(val, hi))
```

### `sorted()`, `reversed()`, `sum()`, `abs()`

```python
# sorted() returns a NEW list; list.sort() is in-place
nums = [3, 1, 4, 1, 5]
print(sorted(nums))            # [1, 1, 3, 4, 5]  — nums unchanged
print(sorted(nums, reverse=True))  # [5, 4, 3, 1, 1]

# Sort list of tuples: primary key asc, secondary key desc
pairs = [(1, 3), (2, 1), (1, 5)]
pairs.sort(key=lambda x: (x[0], -x[1]))
# [(1, 5), (1, 3), (2, 1)]

# reversed() returns an iterator
for x in reversed([1, 2, 3]):
    print(x)  # 3 2 1

list(reversed([1,2,3]))  # [3, 2, 1]

# sum() with start
sum([1, 2, 3], 10)   # 16

# sum of 2D list
matrix = [[1,2],[3,4]]
total = sum(sum(row) for row in matrix)  # 10

# abs()
abs(-5)    # 5
abs(3+4j)  # 5.0  (complex magnitude)
```

## Numbers & Math

### Quotient and remainder together — `divmod`

```python
In [1]: x,y = divmod(100, 3)

In [2]: x
Out[2]: 33

In [3]: y
Out[3]: 1
```

### Remainder when divided by a number

```python
#-----------------
# V1 : %=
#-----------------

In [7]: x = 100

In [8]: x %= 60

In [9]: x
Out[9]: 40

In [10]: y = 120

In [11]: y %= 60

In [12]: y
Out[12]: 0

#-----------------
# V2 : divmod
#-----------------
In [13]: a = 100

In [14]: q, r = divmod(a, 60)

In [15]: q
Out[15]: 1

In [16]: r
Out[16]: 40

In [17]: b = 120

In [18]: q2, r2 = divmod(b, 60)

In [19]: q2
Out[19]: 2

In [20]: r2
Out[20]: 0
```

```python
# LC 1010
# V0
# IDEA : dict
class Solution(object):
    def numPairsDivisibleBy60(self, time):
        rem = {}
        pairs = 0
        for t in time:
            #print ("rem = " + str(rem))
            t %= 60
            if (60 - t) % 60 in rem:
                pairs += rem[(60 - t) % 60]
            if t not in rem:
                rem[t] = 1
            else:
                rem[t] += 1
        return pairs
```

### `math.ceil`

```python
# https://www.runoob.com/python/func-number-ceil.html
# https://www.runoob.com/python/func-number-ceil.html

"""
The method ceil(x) in Python returns a ceiling value of x 
-> i.e., the SMALLEST integer GREATER than or EQUAL to x.
"""
In [9]:
   ...: import math
   ...:
   ...: # prints the ceil using ceil() method
   ...: print ("math.ceil(-23.11) : ", math.ceil(-23.11))
   ...: print ("math.ceil(300.16) : ", math.ceil(300.16))
   ...: print ("math.ceil(300.72) : ", math.ceil(300.72))
math.ceil(-23.11) :  -23
math.ceil(300.16) :  301
math.ceil(300.72) :  301


# LC 875. Koko Eating Bananas
#...
# Iterate over the piles and calculate hour_spent.
# We increase the hour_spent by ceil(pile / middle)
for pile in piles:
    # python ceil : https://www.runoob.com/python/func-number-ceil.html
    hour_spent += math.ceil(pile / middle)
# Check if middle is a workable speed, and cut the search space by half.
if hour_spent <= h:
    right = middle
else:
    left = middle + 1
#...
```

### `math.floor`

```python
# https://www.geeksforgeeks.org/floor-ceil-function-python/

"""
floor() method in Python returns the floor of x 
-> i.e., the LARGEST integer NOT GREATER than x. 
"""

# This will import math module
import math   
  
In [8]: import math
   ...:
   ...: # prints the ceil using floor() method
   ...: print ("math.floor(-23.11) : ", math.floor(-23.11))
   ...: print ("math.floor(300.16) : ", math.floor(300.16))
   ...: print ("math.floor(300.72) : ", math.floor(300.72))
math.floor(-23.11) :  -24
math.floor(300.16) :  300
math.floor(300.72) :  300
```

### `pow(x, n, mod)` — fast modular exponentiation

```python
# Built-in 3-arg pow is O(log n), much faster than (x**n) % mod
MOD = 10**9 + 7

print(pow(2, 10, MOD))    # 1024
print(pow(2, 100, MOD))   # 976371285  (computed efficiently)

# Modular inverse (when mod is prime): pow(a, mod-2, mod)
inv = pow(3, MOD - 2, MOD)   # modular inverse of 3

# LC 50 Pow(x, n) — manual fast power
def myPow(x, n):
    if n < 0:
        x, n = 1 / x, -n
    res = 1
    while n:
        if n % 2 == 1:
            res *= x
        x *= x
        n //= 2
    return res
```

### Integer division `//` and bit operations

```python
#-------------------------------
# Integer division (floor division)
#-------------------------------
7 // 2    # 3
-7 // 2   # -4  (rounds toward -inf, NOT toward 0!)
int(-7/2) # -3  (truncation toward 0)

# Safe mid-point (avoids overflow in other languages)
lo, hi = 0, 100
mid = (lo + hi) // 2

#-------------------------------
# Bit operations (common in LC)
#-------------------------------
# AND, OR, XOR, NOT
5 & 3    # 1   (101 & 011 = 001)
5 | 3    # 7   (101 | 011 = 111)
5 ^ 3    # 6   (101 ^ 011 = 110)
~5       # -6  (bitwise NOT: ~x = -(x+1))

# Shift
5 << 1   # 10  (multiply by 2)
5 >> 1   # 2   (floor divide by 2)

# Check/set/clear bit i
x = 13          # 1101
x & (1 << i)    # check bit i (non-zero if set)
x | (1 << i)    # set bit i
x & ~(1 << i)   # clear bit i
x ^ (1 << i)    # flip bit i

# Count set bits
bin(13).count('1')   # 3
# or: use Brian Kernighan
def count_bits(n):
    count = 0
    while n:
        n &= n - 1   # clear lowest set bit
        count += 1
    return count

# x & (x-1) removes lowest set bit — useful for power-of-2 check
def is_power_of_two(n):
    return n > 0 and (n & (n - 1)) == 0
```

### Converting an N-based integer to base 10

```python
# https://github.com/yennanliu/CS_basics/blob/master/doc/cheatsheet/math.md

# How does int(x[,base]) work?
# -> https://stackoverflow.com/questions/33664451/how-does-intx-base-work
# -> int(string, base) accepts an arbitrary base. You are probably familiar with binary and hexadecimal, and perhaps octal; these are just ways of noting an integer number in different bases:
# exmaple :
# In [76]: int('10',2)      # transform '10' from 2 based to 10 based                                                  
# Out[76]: 2
#
# In [77]: int('11',2)      # # transform '11' from 2 based to 10 based                                                 
# Out[77]: 3
#
# In [78]: int('100',2)     # # transform '100' from 2 based to 10 based                                                 
# Out[78]: 4

# LC 089
```

### Infinity and boundary values

```python
# Use float('inf') / float('-inf') instead of sys.maxsize for clarity
INF = float('inf')
NEG_INF = float('-inf')

# Works with min/max comparisons
min_val = float('inf')
for x in [3, 1, 4, 1, 5]:
    min_val = min(min_val, x)
print(min_val)  # 1

# Common in DP initialization
dp = [[float('inf')] * n for _ in range(m)]

# Python int has no overflow — safe to use large numbers
# But float('inf') is cleaner for "unbounded" semantics
```

## Iteration, Comprehensions & Functional Tools

### `all()`

- Will return Boolean (true or false) per condition for ALL elements in a list
```python
# example 1
In [36]: a = "000"

In [37]: all( i == "0" for i in a )
Out[37]: True

# example 2
In [38]: b = "abc123"

In [39]: all ( i == "a" for i in b )
Out[39]: False

# LC 763. Partition Labels
class Solution(object):
    def partitionLabels(self, s):
        d = {val:idx for idx, val in enumerate(list(s))}
        #print (d)
        res = []
        tmp = set()
        for idx, val in enumerate(s):
            """
            NOTE : below condition
            """
            if idx == d[val] and all(idx >= d[t] for t in tmp):
                res.append(idx+1)
            else:
                tmp.add(val)
        _res = [res[0]] + [ res[i] - res[i-1] for i in range(1, len(res)) ]
        return _res
```

### `any()`

```python
# Returns True if ANY element in iterable is True (short-circuits)
In [1]: any([False, False, True])
Out[1]: True

In [2]: any([False, False, False])
Out[2]: False

In [3]: any(x > 3 for x in [1, 2, 5])
Out[3]: True

# Complement to all():
# all() -> every element must be True
# any() -> at least one element must be True
```

### `not` logic

```python
#----------------------------
# can be either None, [], ""
#----------------------------
In [32]: x = None

In [34]: not x
Out[34]: True

In [35]: y = []

In [36]: not y
Out[36]: True

In [37]: z = ""

In [38]: not z
Out[38]: True
```

### `enumerate()`

```python
# Returns (index, value) pairs — avoids manual index tracking
fruits = ['apple', 'banana', 'cherry']

for i, v in enumerate(fruits):
    print(i, v)
# 0 apple
# 1 banana
# 2 cherry

# start parameter
for i, v in enumerate(fruits, start=1):
    print(i, v)
# 1 apple  2 banana  3 cherry

# Build index map (very common in LC)
s = "abcba"
idx_map = {v: i for i, v in enumerate(s)}
print(idx_map)  # {'a': 4, 'b': 3, 'c': 2}  (last occurrence)
```

### `zip()`

```python
# python
In [1]: for x, y in zip([-1, 1, 0, 0], [0, 0, -1, 1]):
   ...:     print (x, y)
   ...:
-1 0
1 0
0 -1
0 1

In [2]: for x, y, z in zip([-1, 1, 0, 0], [0, 0, -1, 1], [0,0,0,0]):
   ...:     print (x,y,z)
   ...:
-1 0 0
1 0 0
0 -1 0
0 1 0

In [3]: for x, y, z, u in zip([-1, 1, 0, 0], [0, 0, -1, 1], [0,0,0,0], [9,9,9,9]):
   ...:     print (x,y,z,u)
   ...:
-1 0 0 9
1 0 0 9
0 -1 0 9
0 1 0 9
```

### Looping a dict

```python
d = {'a':1, 'b':2, 'c': 3}
# loop over key, value
for k, v in d.items():
    print (k, v)

# loop over key
for k in d.keys():
    print (k)

# loop over value
for v in d.values():
    print (v)
```

### Starred (`*`) expressions

```python
# Extended Iterable Unpacking

# https://www.python.org/dev/peps/pep-3132/
# http://swaywang.blogspot.com/2012/01/pythonstarred-expression.html

# example 1
In [38]: a, *b, c = range(5)

In [39]: a
Out[39]: 0

In [40]: b
Out[40]: [1, 2, 3]

In [41]: c
Out[41]: 4

# example 2
In [43]: for a, *b in [(1, 2, 3), (4, 5, 6, 7)]:
    ...:     print ("a = " + str(a) + " b = " + str(b))
    ...:
a = 1 b = [2, 3]
a = 4 b = [5, 6, 7]

# example 3
In [44]: first, *rest = [1, 2, 3, 4, 5]

In [45]: first
Out[45]: 1

In [46]: rest
Out[46]: [2, 3, 4, 5]

# example 4
In [47]: *directories, executable = "/usr/local/bin/vim".split("/")
    ...: print (directories)
    ...: print (executable)
['', 'usr', 'local', 'bin']
vim

# example 5
args = [1,3]
print (range(*args))
```

### `filter()`

```python
# https://www.runoob.com/python/python-func-filter.html

#-----------------------------------------------
# syntax : filter(<filter_func>, <iterable>)
#-----------------------------------------------

# note !!! : in py 3, it will return iterable instance; while in py 2, it will return a list directly

#----------------------------
# example 1
#----------------------------
In [13]: def is_odd(n):
    ...:     return n % 2 == 1
    ...:
    ...: newlist = filter(is_odd, [1, 2, 3, 4, 5, 6, 7, 8, 9, 10])
    ...: print(newlist)
<filter object at 0x7fc71c3dced0>

In [14]:

In [14]: list(newlist)
Out[14]: [1, 3, 5, 7, 9]


#----------------------------
# example 2
#----------------------------
In [15]: import math
    ...: def is_sqr(x):
    ...:     return math.sqrt(x) % 1 == 0
    ...:
    ...: newlist = filter(is_sqr, range(1, 101))
    ...: print(newlist)
<filter object at 0x7fc71bb10450>

In [16]:

In [16]: list(newlist)
Out[16]: [1, 4, 9, 16, 25, 36, 49, 64, 81, 100]
```

### List comprehensions

```python
#----------------------------
# example 1
#----------------------------
# https://stackoverflow.com/questions/4260280/if-else-in-a-list-comprehension

In [8]: [ x for x in range(5) ]
Out[8]: [0, 1, 2, 3, 4]

# NOTE this !!!!
In [9]: [ x if x % 2 == 0 else -1 for x in range(5) ]
   ...:
   ...:
Out[9]: [0, -1, 2, -1, 4]

In [10]: def my_func(x):
    ...:     if x % 2 ==0:
    ...:         return True
    ...:     return False
    ...:
    ...: [ x if my_func(x) else 999  for x in range(5)]
Out[10]: [0, 999, 2, 999, 4]
```

### `map()` and generator expressions

```python
# map(func, iterable) — lazy, returns iterator
nums = ["1", "2", "3"]
ints = list(map(int, nums))    # [1, 2, 3]

# map with lambda
doubled = list(map(lambda x: x * 2, [1, 2, 3]))  # [2, 4, 6]

# Generator expression (lazy list comprehension) — memory efficient
gen = (x**2 for x in range(1000000))   # nothing computed yet
total = sum(x**2 for x in range(1000000))  # computed on the fly

# Prefer generator expression over list comprehension inside sum/any/all/max/min
max_val = max(abs(x) for x in nums)
has_neg = any(x < 0 for x in nums)
```

### Ternary (conditional) expressions

```python
# syntax: <value_if_true> if <condition> else <value_if_false>
x = 5
result = "even" if x % 2 == 0 else "odd"   # "odd"

# Nested ternary (keep shallow — hard to read beyond two levels)
sign = "positive" if x > 0 else ("zero" if x == 0 else "negative")

# Common LC use
ans = left if left else right          # return whichever is not None
val = node.val if node else 0
```

## Dicts & Sets

### Dict `get()`, `setdefault()`, comprehension

```python
d = {'a': 1, 'b': 2}

# get(key, default) — safe access
d.get('c', 0)     # 0  (no KeyError)
d.get('a', 0)     # 1

# setdefault(key, default) — insert if missing, return value
d.setdefault('c', []).append(3)   # d['c'] = [3]
d.setdefault('c', []).append(4)   # d['c'] = [3, 4]

# dict comprehension
squares = {x: x**2 for x in range(5)}
# {0: 0, 1: 1, 2: 4, 3: 9, 4: 16}

# invert a dict (assuming unique values)
inv = {v: k for k, v in squares.items()}

# filter dict
evens = {k: v for k, v in squares.items() if v % 2 == 0}
```

### Set operations

```python
a = {1, 2, 3, 4}
b = {3, 4, 5, 6}

# Basic ops
a | b    # union        {1, 2, 3, 4, 5, 6}
a & b    # intersection {3, 4}
a - b    # difference   {1, 2}
a ^ b    # symmetric diff {1, 2, 5, 6}

# Membership: O(1) average
3 in a   # True

# Mutation
a.add(5)
a.discard(99)   # no error if missing (vs remove() which raises KeyError)
a.remove(1)     # raises KeyError if missing

# NOTE: can directly remove a specific element from a set by value (not index)
# Common in sliding window problems (LC 3)
seen = set()
seen.add('a')
seen.remove('a')   # removes 'a' directly — no index needed

# Set comprehension
squares = {x**2 for x in range(5)}   # {0, 1, 4, 9, 16}

# Freeze (hashable, usable as dict key)
fs = frozenset([1, 2, 3])
```

### Testing "either element exists" with `or`

```python
In [8]: def test(l1, l2):
   ...:     if l1 or l2:
   ...:         return l1 or l2
   ...:
   ...: res = test("l1", None)
   ...: print (res)
   ...:
   ...: res2 = test(None, "l2")
   ...: print (res2)
l1
l2
```

## Structure, Scope & Return Values

### 2D array (matrix) initialization

```python
#-------------------------------------------------
# CORRECT way — use list comprehension (independent rows)
#-------------------------------------------------
m, n = 3, 4
grid = [[0] * n for _ in range(m)]
grid[0][0] = 1
# Only grid[0][0] is changed

#-------------------------------------------------
# WRONG way — all rows share the same list!
#-------------------------------------------------
bad = [[0] * n] * m
bad[0][0] = 1
# ALL rows become [1, 0, 0, 0]  — common bug!

#-------------------------------------------------
# Common DP patterns
#-------------------------------------------------
# 1D DP
dp = [0] * (n + 1)

# 2D DP (m rows, n cols, filled with False)
dp = [[False] * (n + 1) for _ in range(m + 1)]

# Fill with infinity
dp = [[float('inf')] * n for _ in range(m)]
```

### `nonlocal` and `global` in nested functions

```python
# nonlocal: modify a variable in the ENCLOSING (not global) scope
def outer():
    count = 0
    def inner():
        nonlocal count
        count += 1
    inner()
    inner()
    print(count)  # 2

# Without nonlocal, count += 1 raises UnboundLocalError

# global: modify a module-level variable inside a function
total = 0
def add(x):
    global total
    total += x

# Common LC pattern: DFS with mutable result
def maxDepth(root):
    res = [0]
    def dfs(node, depth):
        if not node:
            return
        res[0] = max(res[0], depth)  # list trick avoids nonlocal
        dfs(node.left, depth + 1)
        dfs(node.right, depth + 1)
    dfs(root, 1)
    return res[0]
```

### `isinstance()` and type checking

```python
isinstance(3, int)        # True
isinstance(3.0, float)    # True
isinstance("hi", str)     # True
isinstance([], list)      # True
isinstance({}, dict)      # True

# Check multiple types at once
isinstance(3, (int, float))   # True

# type() for exact type (no inheritance)
type(3) == int    # True
type(True) == int # False  (bool is subclass of int, but type() is exact)
isinstance(True, int)  # True  (True IS an int!)
```

### Carrying multiple return values in a small class


When a DFS / recursion needs to **return several values at once** (e.g. height + size + a flag), the Java idiom is a small `private static class SubtreeInfo`. In Python the closest equivalents are a `@dataclass`, a plain class, or a `NamedTuple`.

```java
// java — the pattern we want to port
private static class SubtreeInfo {
    int height;
    int size;
    boolean isPerfect;
}
```

#### **Option 1: `@dataclass` (recommended)**

`@dataclass` auto-generates `__init__`, `__repr__`, `__eq__` — least boilerplate, most readable.

```python
# python
from dataclasses import dataclass

@dataclass
class SubtreeInfo:
    height: int
    size: int
    is_perfect: bool


def dfs(node):
    # returns SubtreeInfo carrying 3 values up the recursion
    if node is None:
        return SubtreeInfo(0, 0, True)

    left = dfs(node.left)
    right = dfs(node.right)

    is_perfect = (
        left.is_perfect
        and right.is_perfect
        and left.height == right.height
    )

    size = left.size + right.size + 1
    height = max(left.height, right.height) + 1

    if is_perfect:
        perfect_sizes.append(size)

    return SubtreeInfo(height, size, is_perfect)

# usage
info = dfs(root)
print(info.height, info.size, info.is_perfect)
```

#### **Option 2: Traditional class (no imports)**

```python
# python
class SubtreeInfo:
    def __init__(self, height, size, is_perfect):
        self.height = height
        self.size = size
        self.is_perfect = is_perfect

# usage is identical
info = dfs(root)
print(info.height)
```

#### **Option 3: `NamedTuple` (lightweight + immutable)**

Use when the bundle should be **read-only** (also unpackable like a tuple).

```python
# python
from typing import NamedTuple

class SubtreeInfo(NamedTuple):
    height: int
    size: int
    is_perfect: bool

info = dfs(root)
print(info.height)          # attribute access
h, s, p = info              # tuple unpacking also works
```

#### **Quick comparison**

| Option | Boilerplate | Mutable? | Best for |
|--------|-------------|----------|----------|
| `@dataclass`   | low  | yes (`frozen=True` for immutable) | **default choice** — clean & readable |
| plain class    | high | yes  | no imports allowed / very old Python |
| `NamedTuple`   | low  | **no** | immutable bundle, also tuple-unpackable |

> **Quick & dirty alternative**: for one-off DFS you can just `return (height, size, is_perfect)` and unpack — but a named class/`NamedTuple` is far more readable once you have 3+ fields. For LeetCode-style solutions, `@dataclass` is usually the cleanest replacement for a Java `private static class`.

> **Rule of thumb:** if you *mutate* a shared container (`append`/`add`), you must undo it (`pop`/`remove`). If you create a *new* object each call (string concat, `tmp + [x]`, tuple), the copy IS the backtrack — there's nothing to undo. See also [0-2) assignment vs shallow/deep copy](#assignment-vs-shallow-copy-vs-deep-copy).
### `eval()`

```python
# https://www.runoob.com/python/python-func-eval.html
# https://www.programiz.com/python-programming/methods/built-in/eval
# The eval() method parses the expression passed to this method and runs python expression (code) within the program.

# LC 640
# LC 150

# syntax : eval(expression[, globals[, locals]])

# eample
In [51]: x = 7
    ...: eval('3 * x')
    ...:
Out[51]: 21

In [52]: eval ('2 + 2')
    ...:
Out[52]: 4

In [53]: n = 81
    ...: eval('n + 4')
Out[53]: 85
```

### Swapping for a longer array

```python
if len(l1) < len(l2):
   l1, l2 = l2, l1
```

### DFS path: `str` (immutable, no backtrack) vs `list` (mutable, needs backtrack)


When carrying a `path` down a DFS/backtracking recursion, **the data type decides whether you must undo (backtrack)**:

```text
- if path is `str` type, we DON'T need to undo (backtrack)
     -> since it's IMMUTABLE.

- if path is `array` ([]) type, we NEED to undo (backtrack)
     -> since it's MUTABLE.
```

**Why?**
- A **`str`** is immutable: `path + "->" + str(node.val)` creates a **brand-new string** every call. The parent's `path` is never touched, so each branch automatically gets its own independent copy — nothing to undo.
- A **`list`** is mutable: `path.append(...)` modifies the **same shared object** across all recursive calls. After exploring a branch you must `path.pop()` to restore state for the sibling branch — otherwise leftovers leak across branches.

```python
# LC 257 - Binary Tree Paths
# https://github.com/yennanliu/CS_basics/blob/master/leetcode_python/Depth-First-Search/binary-tree-paths.py

#-------------------------------------------------
# CASE 1) path as ARRAY (mutable) -> MUST backtrack (path.pop())
#-------------------------------------------------
class Solution(object):
    def binaryTreePaths(self, root):
        self.res = []
        self.helper(root, [])      # use array ([]) as tmp cache
        return self.res

    def helper(self, root, path):
        if not root:
            return
        path.append(str(root.val))            # mutate shared list
        # leaf node
        if not root.left and not root.right:
            self.res.append("->".join(path))
        else:
            self.helper(root.left, path)
            self.helper(root.right, path)
        # NOTE !!! backtrack at the final stage (undo the append)
        path.pop()

#-------------------------------------------------
# CASE 2) path as STRING (immutable) -> NO backtrack needed
#-------------------------------------------------
class Solution(object):
    def binaryTreePaths(self, root):
        res = []
        def dfs(node, cur):
            if not node:
                return
            cur = str(node.val) if cur == "" else cur + "->" + str(node.val)  # new string each call
            if not node.left and not node.right:
                res.append(cur)
                return
            # NOTE !!! no path.pop() — each branch got its own string copy
            dfs(node.left, cur)
            dfs(node.right, cur)
        dfs(root, "")
        return res
```

**Same idea, other immutable carriers** — tuples and "pass a new list" also skip the explicit pop, because they hand each child a fresh object instead of sharing one:

```python
# trick: build a NEW list per call (tmp + [x]) instead of append+pop
def dfs(r, tmp):
    if not r.left and not r.right:
        ans.append("->".join(tmp))
    if r.left:
        dfs(r.left, tmp + [str(r.left.val)])    # tmp + [..] -> new list, no pop
    if r.right:
        dfs(r.right, tmp + [str(r.right.val)])
```

**`int` accumulators (`cur_sum`) follow the SAME immutable rule — NO backtrack** ⭐

A very common confusion: in a DFS that carries **both** a running sum (`cur_sum`, an
`int`) **and** a path list (`cache`), why do we `cache.pop()` but never "un-add"
`cur_sum`? Because **integers are immutable** — `cur_sum += root.val` does NOT mutate
the parent's integer in place; it **rebinds** the *local* `cur_sum` to a brand-new int
object. When the child frame is destroyed, the parent's `cur_sum` is untouched.

| Variable | How Python passes it | Need backtrack? | Why |
|----------|----------------------|-----------------|-----|
| **`cur_sum`** (`int`) | **by value** (immutable copy) | **❌ No** | `+= val` makes a NEW int bound to the local name; the parent's value is never overwritten, so it auto-restores when the child frame ends. |
| **`cache`** (`list`) | **by reference** (one shared object) | **✅ Yes** | ONE list instance is shared across the whole recursion tree. A child's `append` is seen by the parent, so we MUST `pop()` to clean up. |

```python
# LC 113 - Path Sum II
# https://github.com/yennanliu/CS_basics/blob/master/leetcode_python/Depth-First-Search/path-sum-ii.py
class Solution(object):
    def pathSum(self, root, targetSum):
        self.res = []
        if not root:
            return self.res
        self.helper(root, targetSum, 0, [])
        return self.res

    def helper(self, root, targetSum, cur_sum, cache):
        if not root:
            return

        cur_sum += root.val        # int  -> rebinds LOCAL name to a new int (immutable)
        cache.append(root.val)     # list -> mutates the ONE shared list

        if not root.left and not root.right and cur_sum == targetSum:
            self.res.append(cache[:])    # snapshot — cache[:] copies, else later pops corrupt it

        self.helper(root.left,  targetSum, cur_sum, cache)
        self.helper(root.right, targetSum, cur_sum, cache)

        cache.pop()                # MUST backtrack the list ...
        # NOTE: NO `cur_sum -= root.val` — the int never changed for the parent
```

**Memory walk-through** — parent at `cur_sum = 5`, `cache = [5]`, step into a child of value `3`:

| | Going DOWN into child | Coming back UP to parent |
|---|---|---|
| **`cache` (list)** | `cache.append(3)` → `[5, 3]` (same object) | without `pop()` it stays `[5, 3]` → **parent corrupted → backtrack required** |
| **`cur_sum` (int)** | `cur_sum + 3` → `8` (new int, local) | child frame destroyed → parent's `cur_sum` still `5` → **no backtrack needed** |

| `path` / accumulator type | Mutable? | New object per call? | Need backtrack (`pop`)? |
|-------------|----------|----------------------|-------------------------|
| `int` (`cur_sum`) | No | Yes (`n + x` rebinds) | **No**                  |
| `str`       | No       | Yes (`s + x`)        | **No**                  |
| `tuple`     | No       | Yes (`t + (x,)`)     | **No**                  |
| `list` + `tmp + [x]` | No (rebound) | Yes | **No** |
| `list` + `append` | **Yes** | No (shared)     | **Yes — `path.pop()`**  |

### Worked pair — LC 445 Add Two Numbers II and LC 394 Decode String

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
