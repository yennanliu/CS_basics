# Python 陷阱與並行處理

> **範圍** — 那些會悄悄害你 submission 掛掉的 Python 行為：可變預設參數、closure 的延遲綁定、整數快取、複製語意，再加上 GIL 與 Python 的並行處理全貌。
> **另見**：[python_trick.md](./python_trick.md) — 幫得上忙而不是扯後腿的慣用寫法；[concurrency_patterns.md](./concurrency_patterns.md) — Java 這邊的並行對照；[java_trick.md](./java_trick.md) — 給正在換語言的人。

## LeetCode 題目清單

- [Concurrency](https://leetcode.com/problem-list/concurrency/)

## 總覽

面試官很少直接問「請列舉 Python 的陷阱」。這些坑通常是**藏在**一題正常的 coding 題裡冒出來的 — 你用 `[[0]]*n` 初始化 grid、你用了可變預設參數、你在 hot loop 裡 `pop(0)` — 強的候選人會當場看出來並解釋清楚。

知道這些坑代表你有**深度**：

- 你懂 Python 的**物件模型**（參考 vs 複製、可變性、interning）。
- 你懂**求值時機**（延遲綁定、generator 的惰性）。
- 你懂**執行成本**（為什麼 `list.pop(0)` 是 O(n)、為什麼會有 GIL）。

這份文件只講 Python。一般的 Python 慣用寫法（切片、複製示範、進位轉換、字典排序）請看 **[`python_trick.md`](python_trick.md)** — 這裡**不重複**那些內容，只做交叉引用。Java 的並行原語請看 **[`concurrency_patterns.md`](concurrency_patterns.md)**。

### 關鍵性質

- **核心想法**：名字是「綁定」到物件上的；賦值永遠不會複製；很多「bug」其實只是共用參考或延後求值。
- **什麼時候用**：每一場面試 — 大多數陷阱都是藏在較大解法裡的一行小錯。

### 參考資料

- [The Python Language Reference — Data model](https://docs.python.org/3/reference/datamodel.html)
- [Common Gotchas — The Hitchhiker's Guide to Python](https://docs.python-guide.org/writing/gotchas/)
- [`python_trick.md`](python_trick.md) · [`concurrency_patterns.md`](concurrency_patterns.md)

---

## 0) 概念

### 0-1) （幾乎）所有陷阱背後的心智模型

三件事就能解釋大部分 Python 的意外：

| 事實 | 後果 |
|------|-------------|
| 變數是**綁到某個物件上的名字**，不是裝著值的盒子 | 賦值（`b = a`）複製的是*參考*，不是資料 |
| 物件分成**可變**（`list`、`dict`、`set`）與**不可變**（`int`、`str`、`tuple`、`frozenset`） | 改動一個共用的可變物件，所有綁到它的名字都會看到 |
| 有些東西是**立即求值**（預設參數、list 字面值），有些是**惰性求值**（generator、closure） | 程式碼*什麼時候*跑，決定了它看到*什麼*值 |

### 0-2) 「陷阱 vs 修法」的固定格式

底下每一節都是同一個形狀：先一段 `# gotcha:` 展示坑，再用 `# fix:` / `# why:` 解釋。要背的是*原因*，不是變通寫法 — 面試官挖的就是這個。

---

## 1) 語言層面的陷阱 ⭐⭐⭐⭐⭐

### 1-1) 可變的預設參數

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

### 1-2) `is` vs `==` 與 interning

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

### 1-3) 迴圈裡 closure 的延遲綁定

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

### 1-4) 整數快取與任意精度

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

### 1-5) 淺複製 vs 深複製

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

### 1-6) 變數作用域：LEGB、`global`、`nonlocal`

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

### 1-7) 真假值與短路運算子的回傳值

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

### 1-8) 浮點數相等與 Decimal

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

### 1-9) 字典順序與安全存取

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

### 1-10) Generator vs list：惰性且只能走一次

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

### 1-11) `*args`、`**kwargs`、拆包、海象運算子

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

### 1-12) `list * n` 的別名問題 — grid 初始化的經典坑 ⭐⭐⭐⭐⭐

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

### 1-13) 字串不可變 — 為什麼 `join` 贏過 `+=`

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

### 1-14) 例外與走訪的陷阱

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

### 1-15) 整數除法 `//` 與 `%` — Python 是**向下取整**，Java 是**向零截斷** ⭐⭐⭐⭐⭐

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

### 1-16) 預設遞迴上限 — 深度 DFS 會噴 `RecursionError`

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

### 1-17) 排序：穩定性、`key=` vs `cmp_to_key`

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

### 1-18) `set` **沒有**順序保證（`dict` 有）

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

## 2) 面試用的資料結構與效能筆記

### 2-1) 成本速查表

| 結構 | 快（一般情況） | 慢／陷阱 |
|-----------|----------------|---------------|
| `list` | 索引 `O(1)`、在**尾端** append/pop `O(1)`* | `pop(0)` / `insert(0, x)` 是 **O(n)**（整串要位移） |
| `collections.deque` | **兩端**都能 `O(1)` append/pop | 隨機索引是 `O(n)` — 不適合存取中間 |
| `set` / `dict` | 查詢、插入、刪除平均 `O(1)` | 最壞 `O(n)`；不可雜湊的 key 會丟 `TypeError` |
| `str` | 索引 `O(1)` | 在迴圈裡串接是 `O(n^2)`（改用 `join`） |
| `heapq`（架在 list 上） | push/pop `O(log n)`、看最小值 `O(1)` | **只有 min-heap** |
| `bisect`（排序好的 list） | 搜尋 `O(log n)` | 插入仍然是 `O(n)`（list 位移） |

*append 是攤還 O(1)。

### 2-2) 佇列：絕對不要用 `list.pop(0)`

```python
# gotcha: using a list as a FIFO queue -> pop(0) shifts every remaining element (O(n))
from collections import deque
q = deque([1, 2, 3])
q.append(4)            # O(1) enqueue at right
q.popleft()            # O(1) dequeue from left  -> 1
# BFS on large graphs with a plain list + pop(0) can TLE; deque fixes it.
```

### 2-3) `heapq` — 只有 min-heap；要 max-heap 就取負號

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

### 2-4) `collections` 的好用工具

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

### 2-5) `bisect` 與 `functools.lru_cache`

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

### 2-6) `defaultdict` **在你「讀」的時候就會插入 key** ⭐⭐⭐⭐

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

### 2-7) 切片是**複製** — 每次切片都要 `O(k)`

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

## 3) Python 的並行處理 ⭐⭐⭐⭐⭐

這是常見的**知識盲點**，也是面試官很愛問的題目，因為答案沒那麼單純：「Python 有 thread，但它不會讓 CPU 工作變快 — 原因是這樣。」

### 3-1) GIL（Global Interpreter Lock）

**它是什麼**：CPython 用一把全域的 mutex — 也就是 GIL — 讓**同一時間只有一條 thread 在執行 Python bytecode**，即使機器有多核也一樣。它保護直譯器內部狀態（例如 reference count）不被資料競爭破壞。

**後果**：

| 工作型態 | thread 有幫助嗎？ | 為什麼 |
|----------|---------------|-----|
| **CPU-bound**（密集迴圈、數學運算、parsing） | **沒有** | thread 會在 GIL 上排隊 — 你只拿到約一顆核心的吞吐量，還多付了 context switch 成本 |
| **I/O-bound**（網路、磁碟、資料庫） | **有** | 阻塞式 I/O 期間 GIL 會**被釋放**，所以一條 thread 在等的時候其他 thread 可以跑 |

**GIL 什麼時候釋放**：阻塞式 I/O（socket／檔案讀取）、`time.sleep`，以及許多會主動放掉 GIL 的 C 擴充套件（NumPy 的重運算）。所以 NumPy 那種向量化運算可以平行化，純 Python 迴圈則不行。

**可以拿來講的點**：要在 CPython 拿到真正的 CPU 平行，就用**多個 process**（每個都有自己的直譯器和 GIL），透過 `multiprocessing` 或 `ProcessPoolExecutor`。*（註：較新的 CPython 3.12+ 加入了 per-interpreter GIL，3.13+ 也開始提供實驗性的 free-threaded／無 GIL 版本 — 提一下可以顯示你有在跟進，但預設版本仍然有 GIL。）*

### 3-2) 決策表：threading vs multiprocessing vs asyncio

| 模型 | 最適合 | 平行性 | 成本／備註 |
|-------|----------|-------------|--------------|
| `threading` | I/O-bound、併發量中等、要搭配阻塞式函式庫 | 併發，但**不是**平行（GIL） | 搶佔式；需要 lock；thread 還算便宜 |
| `multiprocessing` / `ProcessPoolExecutor` | **CPU-bound** 的工作 | **真正**平行（N 顆核心） | 記憶體各自獨立；參數／回傳值必須可 **pickle**；有 IPC 與啟動成本 |
| `asyncio` | **高併發的 I/O**（上千條連線） | 併發，單一 thread | 協作式 — 一個阻塞呼叫就會卡住全部；需要 `async` 版的函式庫 |

大原則：

- **CPU-bound → `multiprocessing`**（或丟給 C／NumPy）。
- **I/O-bound、任務數不多 → `threading`**（可以直接用一般的阻塞式程式碼）。
- **I/O-bound、任務數很多 → `asyncio`**（能便宜地擴到上千條連線）。

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

### 3-4) `concurrent.futures` 的 pool

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

### 3-5) `asyncio` — async/await 與 gather

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

### 3-6) 並行處理的面試講稿

| 名詞 | 一句話解釋 |
|------|----------------------|
| **競爭條件（race condition）** | 結果取決於 thread 的時序；兩條 thread 各做 `x += 1` 可能少算一次，因為 read-modify-write 不是原子操作。用 `Lock` 修。 |
| **死鎖（deadlock）** | 兩條 thread 各自握著對方要的 lock，就這樣互等到天荒地老。解法是用**一致的全域順序**取得 lock（或加 timeout）。 |
| **活鎖／飢餓** | thread 一直在對彼此做反應卻沒進展（活鎖），或某條 thread 永遠排不到（飢餓）。 |
| **為什麼 thread 加速不了 CPU 工作** | **GIL** 讓 Python bytecode 序列化 — 同時只有一條 thread 在跑。要 CPU 平行就用 process。 |
| **原子性** | 有些單一 bytecode 看起來是原子的，但複合語句（`+=`、`if x: x=...`）不是。永遠不要賭「這應該是原子的吧」。 |
| **`concurrent` vs `parallel`** | 併發 = 任務*交錯*進行（結構問題）；平行 = 任務*真的同時*在多核上跑。thread／asyncio 給你併發，process 才給你平行。 |
| **執行緒安全的結構** | `queue.Queue` 對 producer/consumer 是 thread-safe；一般的 `list`／`dict` 在多個 writer 同時寫時不保證安全。 |

---

## 4) Java ↔ Python 快速對照（給正在換語言的人）

| 概念 | Java | Python |
|---------|------|--------|
| 型別 | 靜態，編譯期檢查 | 動態、duck typing（type *hint* 是選配，不強制） |
| 整數範圍 | `int` 32 位元、`long` 64 位元 — 會**溢位** | `int` 是**任意精度** — 永遠不溢位 |
| 預設參數陷阱 | 無（沒有預設參數物件） | 可變預設值只求值**一次**（見 1-1 節） |
| 物件的 `==` | 比 identity（參考）；比值要用 `.equals()` | `==` 比**值**（`__eq__`）；`is` 才是 identity |
| 組字串 | 迴圈裡用 `StringBuilder` | list + `"".join()`（迴圈裡別用 `+=`） |
| 可增長陣列 | `ArrayList` | `list` |
| 雙端佇列 | `ArrayDeque` / `LinkedList` | `collections.deque`（別用 `list.pop(0)`） |
| 雜湊表／雜湊集合 | `HashMap` / `HashSet` | `dict` / `set`（3.7 起 dict 保留插入順序） |
| 優先佇列 | `PriorityQueue`（min-heap） | `heapq`（只有 min-heap；要 max 就取負號） |
| 有序 map | `TreeMap`（有序，`O(log n)`） | 標準庫沒有 sorted map — 用 `bisect` 搭 list，或第三方的 `sortedcontainers` |
| 記憶化 | 自己用 `HashMap` 當快取 | `@functools.lru_cache` 裝飾器 |
| 真正的平行 | 原生 thread 可用滿所有核心 | **process**（GIL 讓 thread 在 CPU 工作上只用得到一顆核心） |
| Lambda | `x -> x + 1`，closure 捕捉的是 *effectively final* 變數 | `lambda x: x + 1`，closure 以**參考**捕捉（延遲綁定，見 1-3 節） |
| Null | `null` | `None`（用 `is None` 比較） |
| 三元運算 | `cond ? a : b` | `a if cond else b` |
| 整數除法 | `/` 向 0 **截斷**（`-7/2 == -3`） | `//` 向 -∞ **取整**（`-7//2 == -4`）；見 1-15 節 |
| 取餘數的正負號 | 跟著**被除數**（`-7 % 2 == -1`） | 跟著**除數**（`-7 % 2 == 1`） |
| 遞迴深度 | JVM stack，約一萬層以上 | 預設 **1000** → `RecursionError`；見 1-16 節 |
| 自訂排序 | `Comparator`（三向 `compare`） | `key=`（優先）或 `functools.cmp_to_key`；兩者都穩定 |
| 子串列／子陣列 | `List.subList` 是一個**view** | 切片是**複製**，每次 `O(k)`；見 2-7 節 |

---

## 另見

- [`python_trick.md`](python_trick.md) — 切片、複製示範、進位轉換、字典排序、字串技巧。
- [`concurrency_patterns.md`](concurrency_patterns.md) — Java 並行原語與 LC 的 threading 題目。
