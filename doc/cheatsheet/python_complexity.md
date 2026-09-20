# Python Operation Complexity

> **Scope** — What every Python built-in and container operation actually costs, and why: the per-call tables for the built-in functions, `list`, `str`, `dict`/`set`, `tuple`, `deque`, `heapq`, `bisect` and `int`, the CPython layout each cost follows from, and the seven idioms that quietly turn an O(n) solution into an O(n²) one.
> **See also**: [python_trick.md](./python_trick.md) — the Python idioms these costs govern; [complexity_cheatsheet.md](./complexity_cheatsheet.md) — the same Big-O lookup for data structures and classic algorithms rather than for Python's built-ins; [python_trick_stdlib.md](./python_trick_stdlib.md) — `heapq`, `bisect`, `collections` and the rest of the library in depth; [time_space_complexity.md](./time_space_complexity.md) — arguing the complexity of a whole LC solution rather than of one call; [java_trick_collections.md](./java_trick_collections.md) — the same ground in Java.

## LeetCode Problem Lists

- [Python](https://leetcode.com/problemset/all/?languageTags=python3)

## Overview

**Why this sheet exists**: nearly every "your solution is O(n²), can you do better?" moment comes
from a built-in whose cost was assumed to be O(1). `len()` really is free; `x in my_list`,
`res += ch` and `arr[1:]` are not.

`n` = size of the object being operated on, `m` = size of the other operand, `k` = size of a slice.

| I want the cost of… | Go to |
|---|---|
| `len`, `max`, `sum`, `sorted`, `reversed`, `pow`, `int`/`str` conversion | [Built-in functions](#built-in-functions) |
| indexing, `append`, `pop(0)`, `in`, slicing, `+=`, copying, sorting a list | [`list`](#list--a-dynamic-array-of-pointers) |
| concatenation, `join`, slicing, `find`, `split`, `replace`, `hash` | [`str`](#str--an-immutable-array-of-code-points) |
| lookup, insert, delete, iteration, `\|` `&` `-` `^` on a `dict` or `set` | [`dict` and `set`](#dict-and-set--open-addressing-hash-tables-) |
| `tuple`, `deque`, `heapq`, `bisect`, big-`int` arithmetic, `==` | [Other containers](#other-containers-worth-knowing) |
| the seven idioms that silently cost an order of growth | [The traps](#the-traps-that-actually-cost-interview-points-) |

## Built-in functions

| Operation | Time | Space | Why (low level) |
|---|---|---|---|
| `len(x)` | **O(1)** | O(1) | Not a walk — the count is a field in the object header (`ob_size` for list/tuple/str, `ma_used` for dict, `used` for set) |
| `max(it)` / `min(it)` | O(n) | O(1) | One linear pass holding the current best |
| `max(it, key=f)` | O(n · f) | O(1) | `f` is called **once per element** (Schwartzian transform), not once per comparison |
| `sum(it)` | O(n) | O(1) | Linear pass. ⚠️ `sum(list_of_lists, [])` is **O(n²)** — each `+` builds a new list |
| `any(it)` / `all(it)` | O(n) worst | O(1) | **Short-circuits** on the first `True` / `False` — O(1) best case |
| `sorted(it)` / `list.sort()` | O(n log n) | O(n) / O(n) | Timsort. `sorted()` also allocates the new list; `.sort()` is in-place but the merge still needs a temp buffer |
| `reversed(seq)` | **O(1)** | O(1) | Returns a *lazy iterator* — nothing is copied. Contrast `seq[::-1]`, which is O(n) time **and** O(n) space |
| `enumerate(it)` / `zip(*its)` | O(1) to create | O(1) | Lazy iterators; the O(n) is paid by the loop that drains them |
| `list(it)` / `set(it)` / `dict(it)` | O(n) | O(n) | Materialises the iterator |
| `abs()`, `divmod()`, `ord()`, `chr()` | O(1) | O(1) | Single machine-word ops for interview-sized ints |
| `pow(a, b, mod)` | O(log b) | O(1) | Square-and-multiply. ⚠️ `(a ** b) % mod` is **not** the same — it builds the full `a**b` first |
| `str(n)` / `int(s)` | O(d) | O(d) | `d` = digit count. Superlinear for huge ints (CPython 3.11+ even caps it at 4300 digits) |
| `bin(n)` / `hex(n)` | O(log n) | O(log n) | One char per bit / nibble |

## `list` — a dynamic array of pointers

A CPython list is a contiguous `PyObject **` block plus a length and a capacity. **Every element is
one pointer wide, whatever it holds** — that is why indexing is O(1) and why inserting in the middle
has to `memmove` the tail.

| Operation | Time | Space | Why (low level) |
|---|---|---|---|
| `l[i]`, `l[i] = v` | **O(1)** | O(1) | Pointer arithmetic on the block |
| `l.append(x)` | **O(1) amortized** | O(1) | Over-allocates geometrically (~1.125×), so a resize costs O(n) but happens O(log n) times |
| `l.pop()` | **O(1)** | O(1) | Drop the last pointer |
| `l.pop(0)`, `l.insert(i, x)`, `del l[0]` | **O(n)** | O(1) | `memmove` of everything after the hole. ⚠️ This is the classic BFS bug — use `collections.deque` |
| `l.remove(x)` | O(n) | O(1) | Linear scan to find it, then the shift above |
| `x in l`, `l.index(x)`, `l.count(x)` | **O(n)** | O(1) | Linear scan with `==` on each element. ⚠️ The single most common accidental O(n²) |
| `l[i:j]` (slice) | O(k) | O(k) | Copies `k` pointers into a **new** list |
| `l[::-1]` | O(n) | O(n) | New list. Use `reversed(l)` when you only iterate |
| `l1 + l2` | O(n + m) | O(n + m) | New list. ⚠️ `+=` in a loop is O(n²); `append` instead |
| `l * k` | O(n · k) | O(n · k) | ⚠️ `[[0] * n] * m` shares **one** row object — see [2D array initialization](./python_trick.md#2d-array-matrix-initialization) |
| `l[:]` / `l.copy()` / `list(l)` | O(n) | O(n) | Shallow — copies n pointers, not the objects |
| `copy.deepcopy(l)` | O(total nodes) | O(total nodes) | Walks the whole object graph and keeps a memo dict for shared refs |
| `l.sort()` | O(n log n) | O(n) | Timsort; **O(n) on already-sorted input** (it detects existing runs) |
| `l.reverse()` | O(n) | O(1) | In-place pointer swap |

```text
list = [10, 20, 30]           capacity 4, size 3

  ob_size = 3   ─────────────► len() reads this, O(1)
  ob_item ──► [ *10 | *20 | *30 | ___ ]     <- contiguous pointers
                 ^                   ^
                 l[0] is one         spare slot from the
                 offset away         geometric over-allocation
                 -> O(1)             -> append() is amortized O(1)

  l.pop(0)  ->  [ *20 | *30 | ___ | ___ ]   <- everything memmove'd left: O(n)
```

## `str` — an immutable array of code points

Strings are immutable, so **every "modification" allocates a new string and copies**. CPython stores
them compactly (PEP 393: 1, 2 or 4 bytes per char depending on the largest code point), so `s[i]` is
still O(1) indexing rather than a UTF-8 walk.

| Operation | Time | Space | Why (low level) |
|---|---|---|---|
| `len(s)`, `s[i]` | **O(1)** | O(1) | Length in the header; fixed-width code units |
| `s1 + s2` | O(n + m) | O(n + m) | Allocate a new buffer, `memcpy` both. ⚠️ `s += ch` in a loop is **O(n²)** |
| `"".join(parts)` | **O(total)** | O(total) | Two passes: sum the lengths, allocate **once**, `memcpy` each part. This is why `join` beats `+=` |
| `s[i:j]` | O(k) | O(k) | New string buffer |
| `s[::-1]` | O(n) | O(n) | New string |
| `s[:i] + ch + s[i+1:]` | O(n) | O(n) | The "replace one char" idiom rebuilds the whole string — fine once, **O(n²)** in a loop |
| `sub in s`, `s.find`, `s.index` | O(n · m) worst, ~O(n) typical | O(1) | Bloom-filtered Boyer–Moore–Horspool; CPython 3.10+ switches to two-way for long needles (O(n + m)) |
| `s.replace(a, b)`, `s.count(a)` | O(n · m) worst | O(n) | Search above, then build a new string |
| `s.split(sep)` | O(n) | O(n) | One scan; the result holds n chars across all pieces |
| `s.strip()`, `s.lstrip()` | O(n) worst | O(n) | Scans only the ends, but still returns a new string |
| `s.lower()`, `s.upper()` | O(n) | O(n) | New string |
| `sorted(s)` / `"".join(sorted(s))` | O(n log n) | O(n) | The anagram-key idiom (LC 49) |
| `s * k` | O(n · k) | O(n · k) | |
| `hash(s)` | O(n) first time, **O(1)** after | O(1) | The hash is computed over all bytes, then **cached in the string object** |

```text
res = ""
for ch in s:          #  n iterations
    res += ch         #  each one allocates a buffer of the current length
                      #  1 + 2 + 3 + ... + n  =  O(n^2) bytes copied

parts = []
for ch in s:
    parts.append(ch)  #  amortized O(1) each
res = "".join(parts)  #  ONE allocation of exactly n bytes  ->  O(n) total
```

> CPython does have an in-place `+=` optimisation (when the string's refcount is 1 it can `realloc`
> instead of copying), which sometimes hides the O(n²). **Do not rely on it** — it breaks the moment
> a second name points at the string, and it does not exist in PyPy/Jython. Say "join" in an interview.

## `dict` and `set` — open-addressing hash tables ⭐⭐⭐⭐

Both are open-addressing tables (probe the next slot on a collision, no bucket chains). A dict since
3.6 is *split*: a sparse array of indices plus a **dense** array of `(hash, key, value)` entries in
insertion order — which is where ordering guarantees and the compact memory come from.

| Operation | Time (avg) | Time (worst) | Space | Why (low level) |
|---|---|---|---|---|
| `d[k]`, `k in d`, `d.get(k)` | **O(1)** | O(n) | O(1) | Hash → slot → probe. Worst case = every key collides (adversarial input) |
| `d[k] = v`, `s.add(x)` | **O(1) amortized** | O(n) | O(1) | Resizes when the load factor is exceeded (dict at 2/3 full, set at 3/5) — a resize **rehashes everything**, O(n) |
| `del d[k]`, `s.discard(x)` | **O(1)** | O(n) | O(1) | Leaves a *dummy* marker so later probe chains do not break |
| `hash(key)` | O(size of key) | — | O(1) | ⚠️ Hashing a **string or tuple key** is O(len), not O(1) — a dict keyed on length-L strings costs O(L) per op |
| Iterating `d` / `d.items()` | O(n) | — | O(1) | Walks the dense entry array (the view itself is O(1) to create) |
| Iterating a `set` | O(capacity) | — | O(1) | Sets have **no** dense array — iteration walks empty slots too |
| `d.keys() \| other`, `set` union `a \| b` | O(n + m) | — | O(n + m) | |
| `a & b` (intersection) | **O(min(n, m))** | — | O(min) | Iterates the *smaller* set, probing the larger |
| `a - b` (difference) | O(n) | — | O(n) | |
| `a ^ b` (symmetric diff) | O(n + m) | — | O(n + m) | |
| `Counter(it)`, `collections.defaultdict` | O(n) | — | O(n) | Same table, one insert per element |
| `max(d, key=d.get)` | O(n) | — | O(1) | Full scan — there is no "max key" shortcut in a hash table |

> **The one-line version for an interview**: a hash table buys O(1) membership by spending O(n) space.
> Turning `x in nums` (list, O(n)) into `x in set(nums)` (O(1)) is what collapses most brute-force
> O(n²) solutions to O(n) — and the `set(nums)` build is itself O(n).

## Other containers worth knowing

| Type | Op | Time | Why |
|---|---|---|---|
| `tuple` | `t[i]`, `len(t)` | O(1) | Same layout as a list, minus the spare capacity — immutable, so hashable |
| `collections.deque` | `append` / `appendleft` / `pop` / `popleft` | **O(1)** | Doubly-linked list of 64-slot blocks — this is the fix for `list.pop(0)` |
| `collections.deque` | `dq[i]` in the middle | **O(n)** | Has to walk the blocks — deques are not random-access |
| `heapq` | `heappush` / `heappop` | O(log n) | Sift up/down a binary heap stored in a plain list |
| `heapq` | `heapify(l)` | **O(n)** | Bottom-up sift — not `n` pushes. `nlargest(k, it)` is O(n log k) |
| `bisect` | `bisect_left` / `bisect_right` | O(log n) | Binary search on a sorted list |
| `bisect` | `insort` | **O(n)** | O(log n) to find the spot, **O(n) to memmove** the tail — the trap |
| `int` | `a + b`, `a - b` | O(d) | Arbitrary precision: 30-bit "digits", `d` of them. O(1) for interview-sized ints |
| `int` | `a * b` | O(d²) | Schoolbook, Karatsuba above ~70 digits. Matters for factorials / big-power problems |
| any sequence | `a == b` | O(n) | Element-by-element, after an O(1) identity and length short-circuit |

## The traps that actually cost interview points ⭐⭐⭐⭐⭐

```python
# 1) membership on the wrong container  ->  O(n^2) instead of O(n)
for x in nums:
    if x in seen_list:   # ❌ O(n) each
        ...
seen = set(seen_list)    # ✅ O(n) once, then O(1) each

# 2) building a string with +=  ->  O(n^2)
res = ""
for ch in s: res += ch          # ❌
res = "".join(ch for ch in s)   # ✅

# 3) list as a queue  ->  O(n) per dequeue
q = [root]; node = q.pop(0)             # ❌ O(n) memmove every pop
from collections import deque
q = deque([root]); node = q.popleft()   # ✅ O(1)

# 4) slicing inside recursion  ->  O(n^2) time AND O(n^2) space
def f(arr):
    return f(arr[1:])            # ❌ a full copy per level
def f(arr, i):
    return f(arr, i + 1)         # ✅ pass an index

# 5) sorting inside a loop  ->  O(n^2 log n)
for w in words: key = "".join(sorted(w))   # ⚠️ fine (sorts each word once)
for i in range(n): arr.sort()              # ❌ sorts the WHOLE array n times

# 6) recomputing max/min/sum over the same window
for i in range(n): cur = max(nums[i:i+k])  # ❌ O(n*k) -> use a monotonic deque, O(n)

# 7) `in` on a dict's items vs keys
if k in d:            # ✅ O(1) — hashes the key
if k in d.keys():     # ✅ O(1) too — a view, not a list
if k in list(d):      # ❌ O(n) — materialises the keys first
```

**What to say out loud**: name the container, then the cost. *"`seen` is a set, so the membership
test is O(1) average — that makes the whole loop O(n) time and O(n) space."* Stating the container
is what makes the complexity claim checkable, and it is exactly the sentence an interviewer is
listening for.

> See also [complexity_cheatsheet.md](./complexity_cheatsheet.md) for the same lookup at the level of
> data structures and classic algorithms, [python_trick.md](./python_trick.md) for the idioms these
> costs govern, and [python_trick_stdlib.md](./python_trick_stdlib.md) for `heapq` / `bisect` /
> `collections` in depth.
