# Python Gotchas & Concurrency

> **Scope** — Python behaviours that silently cost you a passing submission — mutable defaults, closure late-binding, integer caching, copy semantics — plus the GIL and Python's concurrency story.
> **See also**: [python_trick.md](./python_trick.md) — idioms that help rather than hurt; [concurrency_patterns.md](./concurrency_patterns.md) — the Java concurrency counterpart; [java_trick.md](./java_trick.md) — for candidates switching languages.

## LeetCode Problem Lists

- [Concurrency](https://leetcode.com/problem-list/concurrency/)

## Overview

Interviewers rarely ask *"list Python gotchas"* directly. Instead, these traps surface **inside** a normal coding problem — you initialize a grid with `[[0]]*n`, you use a mutable default argument, you `pop(0)` in a hot loop — and a strong candidate spots and explains them on the fly.

Knowing them signals **depth**:

- You understand Python's **object model** (references vs. copies, mutability, interning).
- You understand **evaluation timing** (late binding, lazy generators).
- You understand **runtime cost** (why `list.pop(0)` is O(n), why the GIL exists).

This cheatsheet is Python-only. For general Python idioms (slicing, copying demos, base conversion, sorting dicts) see **[`python_trick.md`](python_trick.md)** — this doc does **not** duplicate that content, it cross-references it. For Java concurrency primitives see **[`concurrency_patterns.md`](concurrency_patterns.md)**.

### Key Properties

- **Core Idea**: names are *bindings* to objects; assignment never copies; many "bugs" are shared references or deferred evaluation.
- **When to Use**: every interview — most gotchas appear as one-line mistakes inside a larger solution.

### References

- [The Python Language Reference — Data model](https://docs.python.org/3/reference/datamodel.html)
- [Common Gotchas — The Hitchhiker's Guide to Python](https://docs.python-guide.org/writing/gotchas/)
- [`python_trick.md`](python_trick.md) · [`concurrency_patterns.md`](concurrency_patterns.md)

---

## 0) Concept

### 0-1) The mental model behind (almost) every gotcha

Three facts explain most Python surprises:

| Fact | Consequence |
|------|-------------|
| A variable is a **name bound to an object**, not a box holding a value | Assignment (`b = a`) copies the *reference*, not the data |
| Objects are **mutable** (`list`, `dict`, `set`) or **immutable** (`int`, `str`, `tuple`, `frozenset`) | Mutating a shared mutable object is visible through every name bound to it |
| Some things are evaluated **eagerly** (default args, list literals), others **lazily** (generators, closures) | *When* code runs decides *what* value it sees |

### 0-2) Gotcha vs. fix pattern

Every section below follows the same shape: a `# gotcha:` block showing the trap, then the `# fix:` / `# why:` explanation. Memorize the *reason*, not just the workaround — that is what interviewers probe.

---

## 1) Language gotchas ⭐⭐⭐⭐⭐

### 1-1) Mutable default arguments

```python
# gotcha: the default list is created ONCE, at def-time, and REUSED across calls
def append_bad(x, acc=[]):
    acc.append(x)
    return acc

append_bad(1)          # [1]
append_bad(2)          # [1, 2]  <-- surprise! same list persists
append_bad(3)          # [1, 2, 3]

# why: default values are evaluated once when the function object is created,
#      not on each call. A mutable default becomes shared state.

# fix: use None as the sentinel and build a fresh object inside the body
def append_good(x, acc=None):
    if acc is None:
        acc = []
    acc.append(x)
    return acc

append_good(1)         # [1]
append_good(2)         # [2]  <-- fresh list every call
```

### 1-2) `is` vs `==` and interning

```python
# == compares VALUE (calls __eq__).  is compares IDENTITY (same object in memory).

a = [1, 2]
b = [1, 2]
a == b                 # True  -> equal contents
a is b                 # False -> different objects

# gotcha: interning makes `is` "accidentally" work for small ints / short strings
x = 256
y = 256
x is y                 # True  -> CPython caches -5..256

x = 257
y = 257
x is y                 # False (usually) -> outside the cache range

# strings: identifier-like literals are often interned by the compiler
"hi" is "hi"           # True (compile-time interning) - do NOT rely on this
s = "".join(["h", "i"])
s is "hi"              # False -> built at runtime, not interned

# rule: use `is` ONLY for singletons -> `is None`, `is True`, `is False`.
#       use `==` for value comparison. NEVER use `is` to compare numbers/strings.
```

### 1-3) Late-binding closures in loops

```python
# gotcha: closures capture the VARIABLE, not its value at creation time
fns = [lambda: i for i in range(3)]
[f() for f in fns]     # [2, 2, 2]  <-- all see the final i == 2

# why: each lambda looks up `i` when CALLED. By then the loop has finished (i == 2).

# fix A: bind the current value as a default argument (evaluated at def-time)
fns = [lambda i=i: i for i in range(3)]
[f() for f in fns]     # [0, 1, 2]

# fix B: use a factory that creates a new scope per value
def make(i):
    return lambda: i
fns = [make(i) for i in range(3)]
[f() for f in fns]     # [0, 1, 2]
```

### 1-4) Integer caching & arbitrary precision

```python
# gotcha: -5..256 are pre-cached singletons, so `is` "works" — by accident
(-5) is (-5)           # True
256 is 256             # True
257 is 257             # False (fresh object)  -> again: never compare ints with `is`

# feature (not a bug): Python ints are ARBITRARY precision — no overflow
2 ** 200               # 1606938044258990275541962092341162602522202993782792835301376
import math
math.factorial(100)    # a 158-digit int, exact, no wraparound

# why it matters in interviews: problems that overflow int64 in Java/C++
# (big factorials, huge products, hashing) "just work" in Python. Mention this
# as a language advantage — but note the trade-off: big ints are slower (O(n) in
# the number of digits) than fixed-width machine ints.
```

### 1-5) Shallow vs deep copy

```python
import copy

# gotcha: copy() / [:] / list() copy only the OUTER container; inner objects are shared
grid = [[0, 0], [0, 0]]
shallow = copy.copy(grid)     # or grid[:] or list(grid)
shallow[0][0] = 9
grid                          # [[9, 0], [0, 0]]  <-- original mutated!

# fix: deepcopy recursively copies nested objects
deep = copy.deepcopy(grid)
deep[0][0] = 5
grid                          # unchanged by the deep copy

# why: shallow copy duplicates the parent list but its elements are the SAME
#      inner list objects. Only deepcopy walks the whole tree.
# see python_trick.md "assignment VS shallow copy VS deep copy" for a full trace.
```

### 1-6) Variable scope: LEGB, `global`, `nonlocal`

```python
# Name resolution order: Local -> Enclosing -> Global -> Built-in  (LEGB)

x = "global"
def outer():
    x = "enclosing"
    def inner():
        # reads find "enclosing" via LEGB
        return x
    return inner()

# gotcha: assigning to a name makes it LOCAL for the whole function -> UnboundLocalError
count = 0
def bump_bad():
    count += 1            # UnboundLocalError: `count` treated as local because it's assigned
# fix:
def bump_ok():
    global count
    count += 1

# nonlocal: rebind a name in the nearest ENCLOSING function scope (not global)
def counter():
    n = 0
    def step():
        nonlocal n
        n += 1
        return n
    return step

# gotcha: comprehensions have their OWN scope (Py3), so the loop var does NOT leak
[i for i in range(3)]
# print(i)  -> NameError in Py3 (in a comprehension). BUT a plain `for` loop DOES leak:
for j in range(3):
    pass
j                        # 2  <-- j survives after the loop
```

### 1-7) Truthiness & short-circuit operands

```python
# Falsy values: 0, 0.0, "", [], {}, set(), None, False
# Everything else is truthy.

# gotcha: `and`/`or` return an OPERAND, not a bool
"a" or "b"             # "a"  (first truthy)
"" or "b"              # "b"  (first is falsy -> return second)
"a" and "b"            # "b"  (both truthy -> return last)
0 and "b"              # 0    (first falsy -> short-circuits, returns it)

# idiom: default a possibly-empty value
name = user_input or "anonymous"     # "anonymous" when user_input is falsy

# gotcha: this idiom treats 0 / "" / [] as "missing" too!
qty = given_qty or 10                # BUG if given_qty == 0 -> becomes 10
# fix: be explicit about None
qty = given_qty if given_qty is not None else 10
```

### 1-8) Float equality & Decimal

```python
# gotcha: binary floating point cannot represent 0.1 exactly
0.1 + 0.2 == 0.3       # False !
0.1 + 0.2              # 0.30000000000000004

# fix A: compare with a tolerance
import math
math.isclose(0.1 + 0.2, 0.3)         # True (relative + absolute tolerance)

# fix B: use Decimal for exact base-10 arithmetic (money!)
from decimal import Decimal
Decimal("0.1") + Decimal("0.2") == Decimal("0.3")   # True
# note: pass STRINGS to Decimal. Decimal(0.1) inherits the float's imprecision.

# fix C: use Fraction for exact rationals
from fractions import Fraction
Fraction(1, 3) + Fraction(1, 3) + Fraction(1, 3)     # Fraction(1, 1)
```

### 1-9) Dict ordering & safe access

```python
# Since Python 3.7 dicts preserve INSERTION order (a language guarantee, not luck).
d = {}
d["b"] = 1; d["a"] = 2; d["c"] = 3
list(d)                # ['b', 'a', 'c']  -> insertion order, NOT sorted

# gotcha: d[key] raises KeyError on a missing key
# d["missing"]         # KeyError

# fix: get() returns a default (None by default) instead of raising
d.get("missing")       # None
d.get("missing", 0)    # 0

# gotcha: get() does NOT insert. setdefault() reads-or-inserts in one shot.
counts = {}
counts.setdefault("x", 0)
counts["x"] += 1       # {'x': 1}
# for counting, collections.Counter / defaultdict(int) are cleaner (section 3).
```

### 1-10) Generators vs lists: lazy & one-shot

```python
# A list is materialized eagerly; a generator yields lazily, one item at a time.
squares_list = [x*x for x in range(1_000_000)]   # builds 1M ints in memory NOW
squares_gen  = (x*x for x in range(1_000_000))   # builds NOTHING yet

# gotcha: a generator is EXHAUSTED after one pass — you cannot reuse it
gen = (x for x in range(3))
list(gen)              # [0, 1, 2]
list(gen)              # []  <-- already consumed!

# gotcha: len() / indexing do NOT work on generators
# len(gen)             # TypeError
# gen[0]               # TypeError

# why it matters: generators give O(1) memory for streaming/pipelines, but if you
# need multiple passes or random access, materialize once with list(...).
```

### 1-11) `*args`, `**kwargs`, unpacking, walrus

```python
# *args  -> extra positional args as a tuple.  **kwargs -> extra keyword args as a dict.
def f(*args, **kwargs):
    return args, kwargs
f(1, 2, a=3)           # ((1, 2), {'a': 3})

# unpacking in calls and assignments
nums = [1, 2, 3]
print(*nums)           # 1 2 3          (spread into positional args)
a, *rest = [1, 2, 3, 4]                # a=1, rest=[2, 3, 4]
merged = {**{"x": 1}, **{"y": 2}}      # {'x': 1, 'y': 2}

# walrus := assigns AND returns a value inside an expression (Py3.8+)
# gotcha without it: you compute + call twice, or add a pre-loop read
while (line := input_stream.readline()):
    process(line)
# handy in comprehensions: keep only expensive results that pass a test
# results = [y for x in data if (y := f(x)) > 0]
```

### 1-12) `list * n` aliasing — the grid-init trap ⭐⭐⭐⭐⭐

```python
# gotcha: `*` on a list of mutable objects duplicates the REFERENCE, not the object
row = [0] * 3          # FINE: ints are immutable -> [0, 0, 0]
grid = [[0] * 3] * 2   # TRAP: 2 references to the SAME inner list
grid[0][0] = 9
grid                   # [[9, 0, 0], [9, 0, 0]]  <-- both rows changed!

# why: [[0]*3] * 2 makes the inner list once, then stores it twice.
#      Mutating one "row" mutates the shared object.

# fix: build an independent inner list per row with a comprehension
grid = [[0] * 3 for _ in range(2)]
grid[0][0] = 9
grid                   # [[9, 0, 0], [0, 0, 0]]  <-- rows are independent

# This is THE most common LeetCode bug: DP tables, visited grids, adjacency
# matrices. Always use the comprehension form for 2D+ structures.
```

### 1-13) String immutability — why `join` beats `+=`

```python
# Strings are immutable: every += builds a brand-new string and copies everything.

# gotcha: O(n^2) — each concat copies the whole accumulated string
s = ""
for ch in chars:
    s += ch            # allocates & copies a growing string each iteration

# fix: collect in a list (O(1) amortized append), join ONCE at the end -> O(n)
parts = []
for ch in chars:
    parts.append(ch)
s = "".join(parts)

# same reason string[i] = 'x' is illegal — build a new string instead.
# see python_trick.md "update / replace a char in a string BY INDEX".
```

### 1-14) Exception & iteration gotchas

```python
# gotcha: bare `except:` swallows EVERYTHING, including KeyboardInterrupt / SystemExit
try:
    risky()
except:                # too broad — hides bugs, can't Ctrl-C out
    pass
# fix: catch the narrowest exception you expect
try:
    risky()
except (ValueError, KeyError) as e:
    handle(e)

# gotcha: mutating a container WHILE iterating it corrupts the traversal
nums = [1, 2, 3, 4]
for x in nums:
    if x % 2 == 0:
        nums.remove(x)     # skips elements / unpredictable -> [1, 3] here, by luck
# fix: iterate a copy, or build a new list
nums = [x for x in nums if x % 2]       # keep odds, no in-place mutation
# same trap with dicts: "RuntimeError: dict changed size during iteration"
for k in list(d):          # iterate a snapshot of the keys
    if should_drop(k):
        del d[k]
```

### 1-15) Integer division `//` and `%` — Python **floors**, Java **truncates** ⭐⭐⭐⭐⭐

```python
# `//` rounds toward NEGATIVE INFINITY. Java / C++ `/` rounds toward ZERO.
7 // 2                 #  3    same in both
-7 // 2                # -4    Java: -7 / 2 == -3   <-- DIFFERENT
int(-7 / 2)            # -3    int() truncates toward zero -> the Java behaviour
import math
math.trunc(-7 / 2)     # -3    explicit truncation

# the sign of `%` follows the DIVISOR in Python, the DIVIDEND in Java
-7 % 2                 #  1    Java: -7 % 2 == -1
7 % -2                 # -1    Java:  7 % -2 ==  1
math.fmod(-7, 2)       # -1.0  C/Java remainder semantics, if you truly need them
divmod(-7, 2)          # (-4, 1)  -> (a // b, a % b) in one call

# gotcha: digit extraction on a NEGATIVE number silently produces garbage
n = -123
digits = []
while n:
    digits.append(n % 10)   # -123 % 10 == 7   (not -3!)
    n //= 10                # -123 // 10 == -13 -> drifts to -1, never hits 0
# digits -> [7, 7, 8, ...]  and the loop does not terminate the way you expect

# fix: strip the sign first, re-apply at the end (LC 7 Reverse Integer, LC 8, LC 12)
n = -123
sign = -1 if n < 0 else 1
n = abs(n)
digits = []
while n:
    digits.append(n % 10)
    n //= 10
digits                 # [3, 2, 1]  -> then rebuild and multiply by `sign`

# gotcha: `/` ALWAYS returns a float — never use it for an index
arr = [10, 20, 30, 40]
lo, hi = 0, 3
# arr[(lo + hi) / 2]   # TypeError: list indices must be integers or slices, not float
arr[(lo + hi) // 2]    # 20  -> binary-search mid MUST use //

# and float has only 53 bits of mantissa, so `/` silently loses big ints
(10**18 + 1) / 1 == float(10**18)     # True  <-- the +1 vanished

# ceiling division WITHOUT floats (no math.ceil, no precision loss)
-(-7 // 2)             # 4     works for any sign
(7 + 2 - 1) // 2       # 4     the classic (a + b - 1) // b, for a, b > 0

# gotcha: round() is BANKER'S rounding — ties go to the nearest EVEN, not up
round(0.5)             # 0  <-- not 1
round(1.5)             # 2
round(2.5)             # 2  <-- not 3
# fix: math.floor(x + 0.5) for half-up, or Decimal with an explicit rounding mode.
```

### 1-16) Default recursion limit — deep DFS raises `RecursionError`

```python
import sys
sys.getrecursionlimit()      # 1000 in CPython — and your frames are NOT the only ones

def depth(n):
    return 0 if n == 0 else 1 + depth(n - 1)

# depth(10000)               # RecursionError: maximum recursion depth exceeded

# where it bites on LeetCode (constraints routinely exceed 1000):
#  - linked list up to 5*10^4 nodes  -> recursive reverse / merge blows up (LC 206, LC 21)
#  - grid 300x300 = 90_000 cells     -> flood-fill DFS blows up (LC 200, LC 130, LC 695)
#  - a SKEWED tree of 10^5 nodes     -> recursion depth == n (LC 104, LC 124)

# fix A (preferred, and what the interviewer wants to see): go iterative with an
# explicit stack — same algorithm, heap memory instead of the C call stack.
def dfs_iter(grid, sr, sc):
    stack = [(sr, sc)]
    while stack:
        r, c = stack.pop()
        ...                  # push neighbours instead of recursing

# fix B (quick escape hatch): raise the limit
sys.setrecursionlimit(10**6)
depth(10000)                 # 10000  -> now fine
# CAVEAT: setrecursionlimit only moves Python's SAFETY COUNTER; it does not grow the
# C stack. Set it absurdly high and you get a hard SEGFAULT instead of a clean
# RecursionError. If you need a very deep recursion, run it on a thread created with
# threading.stack_size(64 * 1024 * 1024) — or just write the iterative version.
```

### 1-17) Sorting: stability, `key=` vs `cmp_to_key`

```python
# Python's sort (Timsort) is STABLE: records with equal keys keep their input order.
people = [("bob", 2), ("amy", 1), ("cat", 2), ("dan", 1)]
sorted(people, key=lambda p: p[1])
# [('amy', 1), ('dan', 1), ('bob', 2), ('cat', 2)]  -> amy before dan, bob before cat

# gotcha: reverse=True is NOT the same as negating the key. It reverses the ORDER
# of the keys but PRESERVES the original order inside each tie group.
sorted(people, key=lambda p: p[1], reverse=True)
# [('bob', 2), ('cat', 2), ('amy', 1), ('dan', 1)]  -> ties still in INPUT order
# stability is exactly what makes multi-pass sorting (and radix sort) correct:
# sort by the MINOR key first, then by the MAJOR key.

# gotcha: the "negate the key" trick for a descending sub-key only works for NUMBERS
words = ["bb", "a", "ccc", "dd"]
# sorted(words, key=lambda w: (len(w), -w))
#   TypeError: bad operand type for unary -: 'str'

# fix A: two stable passes — minor key first, major key second
tmp = sorted(words, reverse=True)      # minor: alphabetical DESC
sorted(tmp, key=len)                   # major: length ASC
# ['a', 'dd', 'bb', 'ccc']

# fix B: functools.cmp_to_key wraps a real 3-way comparator (LC 179 Largest Number)
from functools import cmp_to_key
def cmp(a, b):                         # <0 -> a first, >0 -> b first, 0 -> tie
    if len(a) != len(b):
        return len(a) - len(b)         # length ASC
    return -1 if a > b else (1 if a < b else 0)     # alphabetical DESC

sorted(words, key=cmp_to_key(cmp))     # ['a', 'dd', 'bb', 'ccc']
# note: cmp_to_key costs a Python-level call per COMPARISON (O(n log n) calls), while
# key= is computed once per ELEMENT (O(n) calls). Prefer key= whenever it can express
# the ordering; reach for cmp_to_key only for genuinely pairwise rules.

# gotcha: Python 3 refuses to order unrelated types (Python 2 allowed it)
# sorted([1, "a"])     # TypeError: '<' not supported between instances of 'str' and 'int'
sorted([1, "a"], key=str)              # [1, 'a']  -> project onto a common key type

# gotcha: list.sort() sorts IN PLACE and returns None
x = [3, 1, 2].sort()                   # None  <-- the classic "why is my list None?"
lst = [3, 1, 2]
sorted(lst)                            # [1, 2, 3]  -> new list; lst still [3, 1, 2]
```

### 1-18) `set` has **no** order guarantee (unlike `dict`)

```python
# dict preserves INSERTION order since 3.7 (section 1-9). set NEVER has, and never will.
s = set()
for v in ["b", "a", "c"]:
    s.add(v)
s                      # {'c', 'b', 'a'}  -> neither insertion order nor sorted

{8, 1, 4, 3}           # {8, 1, 3, 4}  -> looks "almost sorted" because hash(int) == int,
                       #    i.e. the hash-table slot IS the value. Pure coincidence.

# gotcha: for STRINGS the layout also changes BETWEEN PROCESSES — CPython randomizes
# the string hash seed (PYTHONHASHSEED) as a DoS defence:
#   run 1: ['cherry', 'apple', 'banana', 'fig', 'date']
#   run 2: ['cherry', 'banana', 'fig', 'date', 'apple']
#   run 3: ['apple', 'date', 'banana', 'cherry', 'fig']
# -> a solution that "passes locally" can fail the judge, non-deterministically.

# fix: never let a set's iteration order reach the answer
sorted(s)              # ['a', 'b', 'c']  -> deterministic
# if you need dedupe + insertion order, use a dict as an ordered set:
list(dict.fromkeys(["b", "a", "b", "c"]))     # ['b', 'a', 'c']
```

---

## 2) Data-structure & performance notes for interviews

### 2-1) Cost cheat table

| Structure | Fast (typical) | Slow / gotcha |
|-----------|----------------|---------------|
| `list` | index `O(1)`, append/pop **end** `O(1)`* | `pop(0)` / `insert(0, x)` are **O(n)** (shift all) |
| `collections.deque` | append/pop **both ends** `O(1)` | random index is `O(n)` — not for middle access |
| `set` / `dict` | membership, insert, delete avg `O(1)` | worst case `O(n)`; unhashable keys raise `TypeError` |
| `str` | index `O(1)` | concat in a loop `O(n^2)` (use `join`) |
| `heapq` (on a list) | push/pop `O(log n)`, peek min `O(1)` | **min-heap only** |
| `bisect` (sorted list) | search `O(log n)` | insert is still `O(n)` (list shift) |

*append is amortized O(1).

### 2-2) Queue: never `list.pop(0)`

```python
# gotcha: using a list as a FIFO queue -> pop(0) shifts every remaining element (O(n))
from collections import deque
q = deque([1, 2, 3])
q.append(4)            # O(1) enqueue at right
q.popleft()            # O(1) dequeue from left  -> 1
# BFS on large graphs with a plain list + pop(0) can TLE; deque fixes it.
```

### 2-3) `heapq` — min-heap only; negate for max-heap

```python
import heapq
h = []
heapq.heappush(h, 3)
heapq.heappush(h, 1)
heapq.heappush(h, 2)
heapq.heappop(h)       # 1  -> smallest first (MIN-heap)

# gotcha: there is no max-heap. Trick: push negatives, negate on the way out.
maxh = []
for v in [3, 1, 2]:
    heapq.heappush(maxh, -v)
-heapq.heappop(maxh)   # 3  -> the largest

# heapify an existing list in O(n); store tuples (priority, item) for a PQ.
data = [5, 1, 4]
heapq.heapify(data)    # in place, O(n)
heapq.nlargest(2, [5, 1, 4, 2])   # [5, 4]  -> handy one-liners
```

### 2-4) `collections` power tools

```python
from collections import Counter, defaultdict, deque, OrderedDict

Counter("banana")               # Counter({'a': 3, 'n': 2, 'b': 1})
Counter("banana").most_common(2)   # [('a', 3), ('n', 2)]

g = defaultdict(list)           # missing key auto-creates [] -> great for adjacency lists
g[0].append(1)                  # no KeyError, no setdefault boilerplate

# defaultdict(int) for counting; deque for O(1) both-ends queue/stack
freq = defaultdict(int)
for ch in "aab":
    freq[ch] += 1               # {'a': 2, 'b': 1}

# OrderedDict: mostly superseded by ordered dict (3.7+), BUT still useful:
#  - move_to_end(key) + popitem(last=False) -> classic LRU cache implementation
#  - equality is order-SENSITIVE (a plain dict's == ignores order)
```

### 2-5) `bisect` & `functools.lru_cache`

```python
import bisect
arr = [1, 3, 5, 7]
bisect.bisect_left(arr, 5)      # 2  -> leftmost insertion index (O(log n) search)
bisect.insort(arr, 4)           # keeps arr sorted -> [1, 3, 4, 5, 7] (insert is O(n))

from functools import lru_cache
@lru_cache(maxsize=None)        # memoize -> turns exponential recursion into linear
def fib(n):
    return n if n < 2 else fib(n - 1) + fib(n - 2)
# gotcha: arguments must be HASHABLE (no list/dict args). Use tuples.
# Python 3.9+: functools.cache is a shorthand for lru_cache(maxsize=None).
```

### 2-6) `defaultdict` **inserts a key when you READ it** ⭐⭐⭐⭐

```python
from collections import defaultdict

g = defaultdict(list)
g[1].append(2)
len(g)                 # 1

# gotcha: ANY d[k] on a missing key CREATES it with the factory's default value.
if g[3]:               # an innocent-looking "does node 3 have neighbours?" probe
    pass
dict(g)                # {1: [2], 3: []}   <-- 3 now EXISTS
len(g)                 # 2   -> silently breaks "count the nodes / distinct keys"
3 in g                 # True (we just inserted it ourselves)

# fix: probe WITHOUT inserting
g2 = defaultdict(list)
g2[1].append(2)
3 in g2                # False  -> membership test never inserts
g2.get(3)              # None   -> .get() never inserts
dict(g2)               # {1: [2]}  -> untouched

# gotcha: a missing-key read WHILE iterating mutates the dict mid-loop
# for k in g2:
#     _ = g2[k + 1]    # RuntimeError: dictionary changed size during iteration
for k in list(g2):     # fix: iterate a snapshot of the keys
    _ = g2.get(k + 1)

# where it bites on LC: graph problems that do `for nb in graph[node]` on a LEAF /
# sink node — the leaf gets silently added to `graph`, so a later len(graph) or
# `for n in graph` node count is wrong (LC 207, LC 210, LC 332, LC 1136).
```

### 2-7) Slicing **copies** — every slice is `O(k)`

```python
# gotcha: a Python slice is a COPY, not a view (unlike Java subList or a NumPy view).
# One slice is O(k); a slice inside a loop is O(n^2).

def count_slice(s):
    n = 0
    while s:
        n += 1
        s = s[1:]      # copies len(s)-1 chars EVERY iteration -> O(n^2) total
    return n

def count_index(s):
    n, i = 0, 0
    while i < len(s):
        n += 1
        i += 1         # O(1) per step -> O(n) total
    return n

# measured (CPython 3.14): doubling n roughly QUADRUPLES the slicing version
#   n = 20000   s[1:] loop 0.0025s   index loop 0.0006s
#   n = 40000   s[1:] loop 0.0088s   index loop 0.0011s
#   n = 80000   s[1:] loop 0.0463s   index loop 0.0022s

# same trap in RECURSION — passing a shrinking slice instead of an index:
#   rec(a[1:])  -> n=2000: 0.0057s   n=4000: 0.0228s   n=8000: 0.1110s   (quadratic)
#   rec(a, i+1) -> n=2000: 0.0001s   n=4000: 0.0002s   n=8000: 0.0004s   (linear)
# fix: pass (lo, hi) INDICES into the original list — divide & conquer, backtracking,
#      merge sort, and "build tree from preorder/inorder" (LC 105, LC 108) all rely on this.

# also remember: a slice is a SHALLOW copy (see section 1-5)
n = [[0], [1]]
m = n[:]
m[0][0] = 99
n                      # [[99], [1]]  <-- inner lists are still shared
```

---

## 3) Concurrency in Python ⭐⭐⭐⭐⭐

This is a common **knowledge gap** and a favorite interview topic because the answer is nuanced: "Python has threads, but they don't make CPU work faster — here's why."

### 3-1) The GIL (Global Interpreter Lock)

**What it is**: CPython uses a single global mutex — the GIL — so that **only one thread executes Python bytecode at a time**, even on a multi-core machine. It protects interpreter internals (like reference counts) from data races.

**Consequence**:

| Workload | Threads help? | Why |
|----------|---------------|-----|
| **CPU-bound** (tight loops, math, parsing) | **No** | Threads serialize on the GIL — you get ~1 core of throughput plus context-switch overhead |
| **I/O-bound** (network, disk, DB) | **Yes** | The GIL is **released** during blocking I/O, so other threads run while one waits |

**When the GIL releases**: during blocking I/O (socket/file reads), `time.sleep`, and inside many C extensions (NumPy heavy ops) that explicitly drop it. So NumPy-style vectorized work can parallelize even though pure-Python loops cannot.

**Talking point**: to get true CPU parallelism in CPython, use **multiple processes** (each has its own interpreter + GIL) via `multiprocessing` or `ProcessPoolExecutor`. *(Note: recent CPython, 3.12+, adds per-interpreter GILs and 3.13+ ships an experimental free-threaded / no-GIL build — mention it to show currency, but the default build still has the GIL.)*

### 3-2) Decision table: threading vs multiprocessing vs asyncio

| Model | Best for | Parallelism | Cost / notes |
|-------|----------|-------------|--------------|
| `threading` | I/O-bound, moderate concurrency, blocking libraries | Concurrent, **not** parallel (GIL) | Pre-emptive; needs locks; cheap-ish threads |
| `multiprocessing` / `ProcessPoolExecutor` | **CPU-bound** work | **True** parallel (N cores) | Separate memory; args/results must be **picklable**; IPC + startup overhead |
| `asyncio` | **High-concurrency I/O** (thousands of sockets) | Concurrent, single thread | Cooperative — a blocking call stalls everything; needs `async` libraries |

Rule of thumb:

- **CPU-bound → `multiprocessing`** (or offload to C/NumPy).
- **I/O-bound, few tasks → `threading`** (works with ordinary blocking code).
- **I/O-bound, many tasks → `asyncio`** (scales to thousands of connections cheaply).

### 3-3) `threading.Thread` + `Lock`

```python
import threading

counter = 0
lock = threading.Lock()

def worker(n):
    global counter
    for _ in range(n):
        # gotcha: counter += 1 is NOT atomic (read, add, write) -> lost updates
        with lock:                 # fix: serialize the read-modify-write
            counter += 1

threads = [threading.Thread(target=worker, args=(100_000,)) for _ in range(4)]
for t in threads: t.start()
for t in threads: t.join()         # wait for all to finish
print(counter)                     # 400000 WITH the lock; a smaller garbage value without
# note: this is I/O-free CPU work, so the GIL means NO speedup vs 1 thread — the
# point here is CORRECTNESS (the race), not performance.
```

### 3-4) `concurrent.futures` pools

```python
from concurrent.futures import ThreadPoolExecutor, ProcessPoolExecutor

# I/O-bound -> threads. map() preserves input order; results stream as they're ready.
def fetch(url):
    ...                            # e.g. a blocking HTTP GET
    return len(url)

with ThreadPoolExecutor(max_workers=8) as pool:
    sizes = list(pool.map(fetch, urls))

# CPU-bound -> processes for TRUE parallelism across cores
def heavy(n):
    return sum(i * i for i in range(n))

with ProcessPoolExecutor() as pool:      # defaults to os.cpu_count() workers
    results = list(pool.map(heavy, [10**6] * 8))
# gotcha: the target function and its args must be picklable (top-level funcs,
# no lambdas / local closures) or ProcessPoolExecutor raises at submit time.
```

### 3-5) `asyncio` — async/await + gather

```python
import asyncio

async def fetch(name, delay):
    # `await` yields control to the event loop while "I/O" is pending
    await asyncio.sleep(delay)     # stand-in for a non-blocking network call
    return f"{name} done"

async def main():
    # gather runs coroutines CONCURRENTLY on one thread; total ~= max(delays), not sum
    results = await asyncio.gather(
        fetch("a", 1),
        fetch("b", 2),
        fetch("c", 1),
    )
    return results

asyncio.run(main())                # ~2s total, not 4s

# gotcha: a BLOCKING call (time.sleep, requests.get, heavy CPU) inside a coroutine
# freezes the WHOLE event loop — every task stalls. Use async libraries, or push
# blocking/CPU work to a thread/process pool via loop.run_in_executor / asyncio.to_thread.
```

### 3-6) Concurrency interview talking points

| Term | One-line explanation |
|------|----------------------|
| **Race condition** | Result depends on thread timing; `x += 1` from two threads can lose an update because read-modify-write isn't atomic. Fix with a `Lock`. |
| **Deadlock** | Two threads each hold a lock the other needs and both wait forever. Avoid by acquiring locks in a **consistent global order** (or use timeouts). |
| **Livelock / starvation** | Threads keep reacting to each other (livelock) or one never gets scheduled (starvation). |
| **Why threads don't speed up CPU work** | The **GIL** serializes Python bytecode — only one thread runs at a time. Use processes for CPU parallelism. |
| **Atomicity** | Some single bytecode ops look atomic, but compound statements (`+=`, `if x: x=...`) are not. Never rely on "it's probably atomic." |
| **`concurrent` vs `parallel`** | Concurrent = tasks *interleave* (structure); parallel = tasks *literally run at once* on multiple cores. Threads/asyncio give concurrency; processes give parallelism. |
| **Thread-safe structures** | `queue.Queue` is thread-safe for producer/consumer; a plain `list`/`dict` is not guaranteed safe under concurrent writers. |

---

## 4) Java ↔ Python quick contrast (for candidates switching languages)

| Concept | Java | Python |
|---------|------|--------|
| Typing | Static, compile-time checked | Dynamic, duck-typed (optional type *hints*, not enforced) |
| Integer range | `int` 32-bit, `long` 64-bit — **overflows** | `int` is **arbitrary precision** — never overflows |
| Default arg trap | N/A (no default-arg objects) | Mutable default evaluated **once** (section 1-1) |
| `==` on objects | Identity (reference); use `.equals()` for value | `==` is **value** (`__eq__`); `is` is identity |
| String building | `StringBuilder` for loops | list + `"".join()` (avoid `+=` in loops) |
| Growable array | `ArrayList` | `list` |
| Double-ended queue | `ArrayDeque` / `LinkedList` | `collections.deque` (never `list.pop(0)`) |
| Hash map / set | `HashMap` / `HashSet` | `dict` / `set` (dict is insertion-ordered since 3.7) |
| Priority queue | `PriorityQueue` (min-heap) | `heapq` (min-heap only; negate for max) |
| Ordered map | `TreeMap` (sorted, `O(log n)`) | no stdlib sorted map — use `bisect` on a list, or `sortedcontainers` (3rd-party) |
| Memoization | manual `HashMap` cache | `@functools.lru_cache` decorator |
| True parallelism | native threads on all cores | **processes** (GIL limits threads to one core for CPU work) |
| Lambdas | `x -> x + 1`, closures capture *effectively final* vars | `lambda x: x + 1`, closures capture by **reference** (late binding, section 1-3) |
| Null | `null` | `None` (compare with `is None`) |
| Ternary | `cond ? a : b` | `a if cond else b` |
| Integer division | `/` **truncates** toward 0 (`-7/2 == -3`) | `//` **floors** toward -∞ (`-7//2 == -4`); section 1-15 |
| Modulo sign | follows the **dividend** (`-7 % 2 == -1`) | follows the **divisor** (`-7 % 2 == 1`) |
| Recursion depth | JVM stack, ~10k+ frames | **1000** by default → `RecursionError`; section 1-16 |
| Custom sort order | `Comparator` (3-way `compare`) | `key=` (preferred) or `functools.cmp_to_key`; both stable |
| Sublist / subarray | `List.subList` is a **view** | slicing is a **copy**, `O(k)` each time; section 2-7 |

---

## See also

- [`python_trick.md`](python_trick.md) — slicing, copy demos, base conversion, dict sorting, string tricks.
- [`concurrency_patterns.md`](concurrency_patterns.md) — Java concurrency primitives & LC threading problems.
