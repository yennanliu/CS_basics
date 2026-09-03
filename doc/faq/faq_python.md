# Python FAQ

> **Scope** — the Python language itself: object model, mutability, functions and decorators, iterators, OOP, memory management, typing, packaging and the gotchas interviewers actually probe.
> **See also**: [`faq_python_concurrency.md`](./faq_python_concurrency.md) — the GIL,
> threads vs processes, and `asyncio`.

Answers target **Python 3.10+**. Where a version matters it is called out.

---

## 1) The Object Model ⭐⭐⭐⭐⭐

### Names are bindings, not boxes

A Python variable is a **name bound to an object**, not a memory box holding a value.
Assignment rebinds the name; it never copies the object.

```python
# python
a = [1, 2, 3]
b = a            # b and a name the SAME list
b.append(4)
print(a)         # [1, 2, 3, 4]  <- mutated through the other name

b = [9]          # rebinding b does NOT touch a
print(a)         # [1, 2, 3, 4]
```

This is why "is Python pass-by-value or pass-by-reference?" has one correct answer:
**pass-by-object-reference** (also called pass-by-assignment). The function receives a
new name bound to the caller's object — mutating it is visible to the caller,
rebinding it is not.

```python
# python
def mutate(xs): xs.append(1)     # caller sees this
def rebind(xs): xs = [1]         # caller does NOT see this
```

### `is` vs `==`

| Operator | Asks | Backed by |
|----------|------|-----------|
| `==` | Same **value**? | `__eq__` |
| `is` | Same **object** (same `id()`)? | identity |

Use `is` only for singletons: `x is None`, `x is True`, `x is NotImplemented`.

```python
# python
a, b = 256, 256
a is b            # True  — CPython caches small ints (-5..256)
a, b = 257, 257
a is b            # often False — an implementation detail, never rely on it
```

> **Gotcha**: `if x == None` works but is wrong style; `is None` is faster and cannot be
> fooled by a custom `__eq__`.

### Mutable vs immutable

| Immutable | Mutable |
|-----------|---------|
| `int`, `float`, `bool`, `str`, `bytes`, `tuple`, `frozenset`, `range` | `list`, `dict`, `set`, `bytearray`, most user classes |

**Only hashable objects can be `dict` keys or `set` members**, because hashing requires
the value never to change. A `tuple` containing a `list` is itself unhashable.

### Truthiness

`if x:` calls `__bool__`, falling back to `__len__`. Falsy: `False`, `None`, `0`, `0.0`,
`""`, `[]`, `{}`, `set()`, `()`. Everything else is truthy.

```python
# python
if items:              # good — works for list, dict, str, ...
if len(items) > 0:     # noisier, same meaning
if items is not None:  # DIFFERENT: an empty list is not None
```

---

## 2) Mutability Gotchas ⭐⭐⭐⭐⭐

### The mutable default argument

The default is evaluated **once at function-definition time**, so every call shares it.

```python
# python
def bad(item, bucket=[]):     # ❌ the SAME list on every call
    bucket.append(item)
    return bucket

bad(1)   # [1]
bad(2)   # [1, 2]  <- surprise

def good(item, bucket=None):  # ✅ the idiom
    if bucket is None:
        bucket = []
    bucket.append(item)
    return bucket
```

### Shallow vs deep copy

```python
# python
import copy

original = [[1, 2], [3, 4]]
shallow  = copy.copy(original)      # or original[:] / list(original)
deep     = copy.deepcopy(original)

shallow[0].append(99)
original     # [[1, 2, 99], [3, 4]]  <- inner lists are SHARED
deep[0].append(0)
original     # unchanged
```

`deepcopy` handles cycles but is slow; prefer restructuring so you don't need it.

### Repeating a mutable

```python
# python
grid = [[0] * 3] * 3      # ❌ three references to ONE row
grid[0][0] = 1            # [[1,0,0], [1,0,0], [1,0,0]]

grid = [[0] * 3 for _ in range(3)]   # ✅ three distinct rows
```

`[0] * 3` is fine because `int` is immutable — the aliasing only bites for mutables.

### Mutable class attributes

```python
# python
class Cart:
    items = []            # ❌ shared by every instance

class Cart:
    def __init__(self):
        self.items = []   # ✅ per instance
```

---

## 3) Functions ⭐⭐⭐⭐

### Parameters

```python
# python
def f(pos_only, /, standard, *args, kw_only, **kwargs):
    ...
```

- Everything before `/` is **positional-only** (3.8+).
- Everything after `*` or `*args` is **keyword-only**.
- `*args` collects extra positionals into a tuple; `**kwargs` extra keywords into a dict.
- At the call site `*` and `**` **unpack**: `f(*seq, **mapping)`.

### Closures and late binding

A closure captures the **variable**, not its value at capture time.

```python
# python
fs = [lambda: i for i in range(3)]
[f() for f in fs]                       # [2, 2, 2]  <- all see the final i

fs = [lambda i=i: i for i in range(3)]  # bind now via a default
[f() for f in fs]                       # [0, 1, 2]
```

`nonlocal` rebinds a name in the enclosing function scope; `global` at module scope.

### Scope: LEGB

Name lookup walks **L**ocal → **E**nclosing → **G**lobal → **B**uiltins. Assigning to a
name anywhere in a function makes it local *for the whole function*:

```python
# python
count = 0
def bump():
    count += 1        # UnboundLocalError — `count` is local because we assign to it
def bump_ok():
    global count
    count += 1
```

### Decorators

A decorator takes a function and returns a replacement. `@deco` is exactly
`f = deco(f)`.

```python
# python
import functools, time

def timed(fn):
    @functools.wraps(fn)              # keep __name__, __doc__, signature
    def wrapper(*args, **kwargs):
        start = time.perf_counter()
        try:
            return fn(*args, **kwargs)
        finally:
            print(f"{fn.__name__} took {time.perf_counter() - start:.3f}s")
    return wrapper

@timed
def work(n): return sum(range(n))
```

**Always use `functools.wraps`** — without it the wrapped function loses its name,
docstring and signature, which breaks logging, `help()`, and frameworks that introspect
handlers.

A decorator **with arguments** is one more layer — a function returning a decorator:

```python
# python
def retry(times):                      # takes the argument
    def decorator(fn):                 # takes the function
        @functools.wraps(fn)
        def wrapper(*a, **kw):
            for attempt in range(times):
                try:
                    return fn(*a, **kw)
                except Exception:
                    if attempt == times - 1:
                        raise
        return wrapper
    return decorator

@retry(times=3)
def flaky(): ...
```

Useful built-in decorators: `@functools.lru_cache` / `@functools.cache` (memoisation),
`@property`, `@staticmethod`, `@classmethod`, `@dataclasses.dataclass`,
`@functools.singledispatch`, `@contextlib.contextmanager`.

---

## 4) Iterators & Generators ⭐⭐⭐⭐⭐

### The protocols

- **Iterable**: has `__iter__()` returning an iterator (`list`, `dict`, `str`, a file…).
- **Iterator**: has `__next__()` and returns itself from `__iter__()`. Raises
  `StopIteration` when exhausted. **An iterator is consumed once.**

```python
# python
it = iter([1, 2, 3])
next(it)          # 1
list(it)          # [2, 3]
list(it)          # []   <- already exhausted
```

This is what "why can I only read `open(f)` once?" comes down to: a file object *is* an
iterator over its lines, and its position is state. Call `f.seek(0)` to rewind, or read
into a list if you need multiple passes.

### Generators

A function with `yield` returns a generator: it computes values **lazily**, one at a
time, and keeps its local state between `next()` calls.

```python
# python
def powers_of_two(limit):
    value = 1
    while value < limit:
        yield value            # suspend here, resume on next()
        value *= 2

list(powers_of_two(20))        # [1, 2, 4, 8, 16]
```

| | List | Generator |
|---|------|-----------|
| Memory | All elements held | One element at a time |
| `len()` | Yes | No |
| Re-iterate | Yes | No — exhausted after one pass |
| Built by | `[x for x in it]` | `(x for x in it)`, or `yield` |
| Use when | You need indexing / multiple passes | Large or infinite streams, pipelines |

```python
# python
import itertools

# A pipeline that never materialises the whole file
lines   = (line.rstrip() for line in open("big.log"))
errors  = (l for l in lines if "ERROR" in l)
first10 = itertools.islice(errors, 10)
```

`yield from sub_generator()` delegates to another generator (and forwards `send`/throw).

### Generators as coroutines

`gen.send(value)` resumes a generator, making `yield` evaluate to `value` — the mechanism
`asyncio` was originally built on. See
[`faq_python_concurrency.md`](./faq_python_concurrency.md).

---

## 5) Comprehensions ⭐⭐⭐⭐

```python
# python
new   = [expr(i) for i in old if keep(i)]        # list
uniq  = {expr(i) for i in old}                   # set
index = {k: v for k, v in pairs}                 # dict
lazy  = (expr(i) for i in old)                   # generator
flat  = [c for row in grid for c in row]         # nested: outer loop first
```

- Comprehensions have **their own scope** (Python 3): the loop variable does not leak.
- They are faster than an explicit `append` loop (no attribute lookup per item), but a
  comprehension whose only purpose is a side effect should be a plain `for` loop.
- The **walrus operator** (`:=`, 3.8+) lets you reuse a computed value:
  `[y for x in data if (y := f(x)) is not None]`.

---

## 6) OOP in Python ⭐⭐⭐⭐

### Methods

```python
# python
class Circle:
    tau = 6.283185                       # class attribute (shared)

    def __init__(self, r):
        self.r = r                       # instance attribute

    def area(self):                      # instance method — gets self
        return self.tau / 2 * self.r ** 2

    @classmethod
    def unit(cls):                       # gets the class — alternative constructor
        return cls(1)

    @staticmethod
    def describe():                      # plain function in the class namespace
        return "a round thing"

    @property
    def diameter(self):                  # computed attribute, no parentheses
        return 2 * self.r

    @diameter.setter
    def diameter(self, d):
        self.r = d / 2
```

Use `@property` to turn an attribute into a computed one **without changing callers** —
which is why Python has no getter/setter boilerplate.

### Dunder methods worth knowing

| Method | Powers |
|--------|--------|
| `__repr__` | `repr(x)`, the debugger/REPL — unambiguous, for developers |
| `__str__` | `str(x)`, `print` — readable, for users (falls back to `__repr__`) |
| `__eq__` + `__hash__` | `==`, `set`/`dict` membership — define **together** |
| `__len__`, `__getitem__`, `__contains__` | `len()`, `x[i]`, `in` |
| `__iter__`, `__next__` | `for` loops |
| `__enter__`, `__exit__` | `with` blocks |
| `__call__` | making an instance callable |
| `__lt__` (+ `functools.total_ordering`) | sorting, `heapq` |

> Defining `__eq__` sets `__hash__` to `None` (unhashable) unless you define `__hash__`
> too — the same contract as Java's `equals`/`hashCode`.

### `__init__` vs `__new__`

`__new__` **creates** the instance (rarely overridden — singletons, immutable subclasses);
`__init__` **initialises** the already-created instance.

### Inheritance, MRO and `super()`

Python allows multiple inheritance; the **MRO** (method resolution order, C3
linearisation) decides which implementation wins. `super()` follows the MRO, not
"the parent class".

```python
# python
class A: ...
class B(A): ...
class C(A): ...
class D(B, C): ...
D.__mro__     # D -> B -> C -> A -> object
```

Cooperative multiple inheritance works only if **every** class in the chain calls
`super().__init__(...)`.

### Duck typing, ABCs and Protocols

Python dispatches on behaviour, not declared types ("if it quacks…"). To make a contract
explicit:

```python
# python
from abc import ABC, abstractmethod

class Repository(ABC):                 # nominal: subclasses must inherit
    @abstractmethod
    def get(self, key): ...

from typing import Protocol

class Closeable(Protocol):             # structural: anything with close() matches
    def close(self) -> None: ...
```

### Data-carrying classes

```python
# python
from dataclasses import dataclass, field

@dataclass(frozen=True, slots=True)    # frozen -> immutable + hashable; slots -> smaller
class Point:
    x: int
    y: int
    tags: list[str] = field(default_factory=list)   # never `= []`
```

`@dataclass` generates `__init__`, `__repr__`, `__eq__` (and ordering with
`order=True`). Alternatives: `NamedTuple` (immutable, tuple-like, lightweight),
`enum.Enum` (a closed set of constants), `TypedDict` (a dict with a fixed shape).

### `__slots__`

Declaring `__slots__ = ("x", "y")` removes the per-instance `__dict__`: less memory and
faster attribute access, at the cost of no dynamic attributes. Worth it for millions of
small objects.

---

## 7) Context Managers ⭐⭐⭐

`with` guarantees cleanup even on exception — Python's answer to try-with-resources.

```python
# python
with open("data.txt") as f:      # __exit__ closes f, exception or not
    process(f)

from contextlib import contextmanager

@contextmanager
def timing(label):
    start = time.perf_counter()
    try:
        yield                    # the body of the `with` runs here
    finally:
        print(label, time.perf_counter() - start)
```

`__exit__(exc_type, exc, tb)` returning `True` **swallows** the exception — return
`False`/`None` unless suppression is the point. `contextlib.suppress`, `ExitStack` and
`closing` cover the common cases.

---

## 8) Errors & Exceptions ⭐⭐⭐⭐

### EAFP over LBYL

Python prefers "easier to ask forgiveness than permission" — try it and handle failure —
over "look before you leap", which is racy and slower on the happy path.

```python
# python
try:                       # ✅ EAFP
    value = cache[key]
except KeyError:
    value = compute(key)

if key in cache:           # LBYL — two lookups, and racy under concurrency
    value = cache[key]
```

### Full statement

```python
# python
try:
    risky()
except (ValueError, TypeError) as e:   # narrow, grouped
    log.warning("bad input: %s", e)
    raise BusinessError("...") from e  # chain: keeps the original traceback
except Exception:
    raise                              # re-raise unchanged
else:
    commit()                           # runs only if NO exception
finally:
    release()                          # always runs
```

### Rules that show up in review

- **Never `except:` bare** — it catches `KeyboardInterrupt` and `SystemExit` too. Use
  `except Exception:` at worst.
- Catch the **narrowest** exception you can actually handle.
- `raise X from e` preserves the cause; a bare `raise X` inside `except` still chains
  implicitly, but the explicit form documents intent.
- A `return` inside `finally` **swallows** a propagating exception — don't.
- Custom exceptions: subclass `Exception` (not `BaseException`), with one base class per
  package so callers can catch the whole family.

`ExceptionGroup` and `except*` (3.11+) handle several exceptions raised concurrently —
the shape `asyncio.TaskGroup` raises.

---

## 9) Memory Management & GC ⭐⭐⭐⭐

CPython uses **reference counting** plus a **cycle collector**:

1. Every object has a refcount; it is freed **immediately** when the count hits zero —
   which is why CPython's memory use is predictable and `with` blocks feel prompt.
2. Reference cycles (`a.b = b; b.a = a`) never reach zero, so a **generational GC**
   (three generations, scanning young objects most often) finds and frees them.

```python
# python
import sys, gc
sys.getrefcount(obj)     # +1 for the temporary argument reference
gc.collect()             # force a cycle collection
gc.get_referrers(obj)    # who is keeping this alive?
```

**Common leaks** — objects that stay reachable:

- module-level caches and lists that only grow (use `functools.lru_cache(maxsize=…)`);
- registering callbacks/observers and never unregistering (use `weakref`);
- exception tracebacks stored long-term (they hold every frame's locals);
- `__del__` on objects in a cycle used to make them uncollectable (fixed since 3.4, but
  `__del__` is still unpredictable — prefer context managers).

Other notes: CPython allocates small objects from **arenas/pools** (`pymalloc`) and may
not return freed memory to the OS; identifier-like strings are interned so identical
literals share one object; `sys.intern` can help hash-heavy workloads.

---

## 10) Type Hints ⭐⭐⭐

Hints are **not enforced at runtime** — they are for readers, IDEs and checkers
(`mypy`, `pyright`).

```python
# python
from typing import Iterable, TypeVar

def head(xs: list[int]) -> int | None:        # `Optional[int]` before 3.10
    return xs[0] if xs else None

T = TypeVar("T")
def first(xs: Iterable[T]) -> T | None: ...   # generic, preserves the element type
```

- Prefer built-in generics (`list[str]`, `dict[str, int]`) over `typing.List` (3.9+).
- Annotate **public** function signatures and dataclasses; skip obvious locals.
- `from __future__ import annotations` makes annotations lazy strings, which fixes
  forward references and circular-import pain.
- `Any` disables checking — treat it as a TODO.

---

## 11) The Standard-Library Toolbelt ⭐⭐⭐⭐

The modules that turn a 20-line answer into a 5-line one (and show up constantly in
coding rounds):

| Module | Reach for it when |
|--------|-------------------|
| `collections` | `defaultdict` (auto-initialised buckets), `Counter` (frequencies + `most_common`), `deque` (**O(1)** appends/pops at both ends — the right BFS queue), `OrderedDict` (`move_to_end`, LRU) |
| `heapq` | Top-K, priority queues. Min-heap only — push `-x` or `(-key, item)` for a max-heap |
| `bisect` | Binary search on a sorted list: `bisect_left`, `insort` |
| `itertools` | `product`, `permutations`, `combinations`, `groupby` (needs sorted input!), `accumulate`, `islice`, `chain`, `pairwise` |
| `functools` | `cache`/`lru_cache` (memoise a DP in one line), `reduce`, `partial`, `cmp_to_key` |
| `dataclasses`, `enum`, `typing` | Modelling |
| `pathlib`, `json`, `csv`, `datetime`, `re`, `logging` | Everyday plumbing |

```python
# python
from collections import defaultdict

richer = [[1, 0], [2, 1], [3, 1], [3, 7], [4, 3], [5, 3], [6, 3]]
richer_than = defaultdict(list)
for person, other in richer:
    richer_than[person].append(other)
# defaultdict(list, {1: [0], 2: [1], 3: [1, 7], 4: [3], 5: [3], 6: [3]})
```

### Sorting

```python
# python
people.sort(key=lambda p: (-p.score, p.name))   # score desc, then name asc
sorted(words, key=len)                          # returns a new list
```

Python's sort is **Timsort**: `O(n log n)`, **stable** — so you can sort by a secondary
key first, then by the primary key, and ties keep the earlier order.

---

## 12) Strings, Bytes and Formatting ⭐⭐⭐

- `str` is a sequence of **Unicode code points**; `bytes` is raw octets. Convert
  explicitly: `s.encode("utf-8")` / `b.decode("utf-8")`. Files opened in text mode decode
  for you; `"rb"` does not.
- Strings are immutable, so `s += x` in a loop is **O(n²)**. Build a list and
  `"".join(parts)`.
- f-strings are the default (`f"{name!r} scored {score:.2f}"`); `f"{x=}"` (3.8+) prints
  `x=value` and is the fastest debugging tool in the language.
- `str.find` returns `-1` when absent; `str.index` raises. Both scan left to right.

```python
# python
def find_all(haystack: str, needle: str) -> list[int]:
    """Every start index where `needle` occurs, overlaps included."""
    out, i = [], haystack.find(needle)
    while i != -1:
        out.append(i)
        i = haystack.find(needle, i + 1)   # +1, not +len(needle) -> allows overlap
    return out

find_all("99023430990999", "99")           # [0, 8, 11, 12]
```

---

## 13) Modules, Imports & Packaging ⭐⭐⭐

- A module runs **once** on first import and is cached in `sys.modules`.
- `if __name__ == "__main__":` guards code that should run only when the file is executed
  directly — **required** for `multiprocessing` on macOS/Windows.
- Prefer **absolute imports** (`from myapp.db import conn`). Circular imports usually mean
  a layering problem; the local fix is to import inside the function.
- Environments: `python -m venv .venv` for the standard tool; `uv` / `poetry` /
  `pip-tools` add lockfiles. Pin dependencies for applications, keep ranges for libraries.
- `pyproject.toml` is the modern packaging manifest (`setup.py` is legacy);
  `pip install -e .` installs a local project in editable mode.

---

## 14) Testing ⭐⭐⭐

```python
# python
import pytest

@pytest.fixture
def repo():
    r = Repo(":memory:")
    yield r              # setup / teardown around the yield
    r.close()

@pytest.mark.parametrize("value,expected", [(0, "zero"), (1, "one")])
def test_naming(value, expected):
    assert name(value) == expected

def test_rejects_negative(repo):
    with pytest.raises(ValueError, match="negative"):
        repo.add(-1)
```

Patch where an object is **looked up**, not where it is defined:
`mock.patch("myapp.service.requests.get")`, not `mock.patch("requests.get")`.

---

## 15) Performance ⭐⭐⭐

Measure first: `timeit` for micro-benchmarks, `cProfile` for a program, `tracemalloc` for
memory.

| Cost | Fix |
|------|-----|
| `x in list` is `O(n)` | Use a `set`/`dict` — `O(1)` average |
| `list.insert(0, x)` / `pop(0)` is `O(n)` | `collections.deque` |
| String `+=` in a loop is `O(n²)` | `"".join(parts)` |
| Per-item Python-level work | Push the loop into C: built-ins, comprehensions, `map`, NumPy/pandas |
| Repeated pure calls | `functools.cache` |
| Attribute/global lookups in a hot loop | Bind to a local first (`append = out.append`) |
| Many small objects | `__slots__`, or arrays/NumPy |

CPU-bound code does not scale with threads because of the GIL — see
[`faq_python_concurrency.md`](./faq_python_concurrency.md).

---

## 16) Version Notes

| Version | Worth knowing |
|---------|---------------|
| 3.6 | f-strings, ordered `dict` (as an implementation detail) |
| 3.7 | `dict` insertion order **guaranteed**, dataclasses, `breakpoint()` |
| 3.8 | Walrus `:=`, positional-only `/`, `f"{x=}"`, `functools.cached_property` |
| 3.9 | Built-in generics `list[int]`, dict merge with `\|` |
| 3.10 | `match` statement, `X \| Y` unions, better error messages |
| 3.11 | Large speedups, `ExceptionGroup`/`except*`, `asyncio.TaskGroup`, `tomllib` |
| 3.12 | Cleaner f-strings, `type` alias syntax, per-interpreter GIL groundwork |
| 3.13 | Experimental free-threaded (no-GIL) build, JIT groundwork |

Python 2 is dead; if you meet `xrange`, `print` as a statement, or `dict.iteritems()`,
you are reading Python 2 code — the Python 3 equivalents are `range` (already lazy),
`print()` and `dict.items()`.

---

## 17) Quickfire Interview Q&A

**Q: `list` vs `tuple`?**
Mutable vs immutable. Tuples are hashable (usable as dict keys), slightly smaller/faster,
and signal "a fixed record"; lists signal "a homogeneous collection that changes".

**Q: How is `dict` implemented?**
An open-addressing hash table with a compact index array (3.6+), which is why iteration
order matches insertion order. Average `O(1)` lookup, `O(n)` worst case; it resizes when
about two-thirds full.

**Q: `range` — does it build a list?**
No. `range` is a lazy sequence object: `O(1)` memory, and it supports `len`, indexing and
`in` (the last in `O(1)` because it is an arithmetic check).

**Q: What does `if __name__ == "__main__"` do?**
`__name__` is `"__main__"` only when the file is run as a script, so the guarded code is
skipped when the module is imported.

**Q: `staticmethod` vs `classmethod`?**
`classmethod` receives the class (`cls`) — use it for alternative constructors and
anything that must respect subclassing. `staticmethod` receives nothing — it is a plain
function grouped in the class namespace.

**Q: Shallow vs deep copy?** See §2.

**Q: What is monkey patching?**
Replacing an attribute of a module/class at runtime. Legitimate in tests (`mock.patch`);
in production it makes behaviour untraceable.

**Q: `@lru_cache` — what breaks?**
Arguments must be hashable, the cached function must be pure, and an unbounded cache
(`maxsize=None`) on an instance method keeps every `self` alive — a classic leak.

**Q: Why is `0.1 + 0.2 != 0.3`?**
Binary floating point (IEEE 754) cannot represent those decimals exactly. Compare with
`math.isclose`, and use `decimal.Decimal` for money.

**Q: What is the GIL?**
See [`faq_python_concurrency.md`](./faq_python_concurrency.md) §1.

---

## 18) Recap Checklist

```text
[ ] Names bind objects; pass-by-object-reference; mutate vs rebind
[ ] is vs ==, and why `is None` is the only safe use
[ ] Mutable default arg, [[0]*3]*3, shallow vs deep copy
[ ] Closure late binding and the LEGB rule
[ ] Write a decorator with functools.wraps, and one that takes arguments
[ ] Iterable vs iterator; generator memory/laziness trade-off
[ ] __repr__ vs __str__; __eq__ with __hash__; MRO and cooperative super()
[ ] dataclass / NamedTuple / Enum: which and why
[ ] Context manager both ways (class and @contextmanager)
[ ] EAFP, exception chaining, never bare except
[ ] Refcounting + cycle GC; where leaks actually come from
[ ] collections / heapq / bisect / itertools / functools by heart
[ ] Timsort is stable; sort keys and tuple keys
[ ] Big-O of list vs set vs deque operations
```

---

## References

- [Python docs — the data model](https://docs.python.org/3/reference/datamodel.html)
- [Python docs — the standard library](https://docs.python.org/3/library/)
- [`faq_python_concurrency.md`](./faq_python_concurrency.md) — GIL, threads, processes, asyncio
- [`faq_software_runtime.md`](./faq_software_runtime.md) — processes, memory layout, JIT vs interpretation
