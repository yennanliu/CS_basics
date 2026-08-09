# Iterator

## LeetCode Problem Lists

- [Iterator](https://leetcode.com/problem-list/iterator/)
- [Design](https://leetcode.com/problem-list/design/)

## 0) Concept

An **iterator** exposes sequential access to a collection via `hasNext()` / `next()`
without revealing the underlying structure. Interview variants usually ask you to
**wrap** an existing iterator (or nested structure) and add a capability — peek,
flatten, merge — while keeping `next()` at `O(1)` amortized.

### 0-1) Types

- **Peeking** — look at the next element without consuming it (cache one element ahead)
- **Flattening** — iterate a nested / 2D structure as if it were flat (use a stack)
- **Merging** — interleave multiple iterators (use a heap / queue)

### 0-2) Pattern

- **Lazy vs eager** — compute / fetch the next element only when asked; saves memory on large streams
- **Cache-ahead** — pre-fetch one element into a buffer to support `peek()`
- **Stack of iterators** — flatten nested lists by pushing sub-lists on demand

## 1) General form

### 1-1) Basic OP

```python
# python
class MyIterator:
    def __init__(self, data):
        self.data = data
        self.idx = 0

    def hasNext(self):
        return self.idx < len(self.data)

    def next(self):
        val = self.data[self.idx]
        self.idx += 1
        return val
```

### 1-2) The `hasNext()` / `next()` contract ⭐⭐⭐⭐⭐

The single most common way to fail an iterator interview is a `hasNext()` that
**consumes**. The contract every wrapper must honour:

1. **`hasNext()` is idempotent** — calling it 0, 1 or 100 times before `next()` must
   give the exact same sequence from `next()`.
2. **`hasNext()` may mutate internal state, never observable state** — it is allowed to
   lazily advance a stack / fetch from the source in order to *decide*, but the element
   it uncovers must still be handed out by the following `next()`.
3. **`next()` is only defined when `hasNext()` is true** — throw (`NoSuchElementException`)
   or return a documented sentinel; never silently return garbage.
4. **`next()` is amortized O(1)** — a single call may do O(k) work, but a full pass over
   `n` elements must stay O(n).

There are exactly two legal places to put the "advance" work. Pick one and be consistent —
mixing them is where double-consumption bugs come from.

| Idiom | Where the work happens | `hasNext()` | Best for |
|-------|------------------------|-------------|----------|
| **Buffer-one-ahead** (cache-ahead) | constructor + `next()` | just tests the buffer flag | wrapping another iterator, `peek()` support (LC 284) |
| **Normalize-in-`hasNext()`** | `hasNext()` | does the lazy unwrapping, must be re-entrant | nested / tree structures where "is there a next" needs digging (LC 341, LC 173) |

```java
// java
// IDEA: idiom A - buffer one element ahead; hasNext() is a pure test.
//       Use an explicit boolean flag, NOT a null sentinel, so a stream that
//       legitimately contains null still works.
// time = O(1) amortized per next(), space = O(1) extra
class BufferedIterator<T> {
    private final Iterator<T> src;
    private T buffer;
    private boolean hasBuffered;

    BufferedIterator(Iterator<T> src) {
        this.src = src;
        advance();                       // prime once
    }

    private void advance() {             // the ONLY place that pulls from src
        hasBuffered = src.hasNext();
        buffer = hasBuffered ? src.next() : null;
    }

    public boolean hasNext() { return hasBuffered; }   // pure, idempotent

    public T peek() {                                  // non-destructive
        if (!hasBuffered) throw new NoSuchElementException();
        return buffer;
    }

    public T next() {
        if (!hasBuffered) throw new NoSuchElementException();
        T ret = buffer;
        advance();
        return ret;
    }
}
```

```python
# python
# IDEA: idiom B - do the lazy work inside hasNext(); it must be safe to call twice.
#       hasNext() only UNCOVERS the next element, next() is what removes it.
# time = O(1) amortized per next(), space = O(depth) for the pending stack
class LazyIterator:
    def __init__(self, items):
        self.stack = list(reversed(items))

    def hasNext(self):
        # normalize: pop away anything that is not a yieldable element.
        # re-entrant -> calling it again just re-checks an already-clean top
        while self.stack and not self._is_leaf(self.stack[-1]):
            top = self.stack.pop()
            self.stack.extend(reversed(self._children(top)))
        return len(self.stack) > 0

    def next(self):
        if not self.hasNext():
            raise StopIteration
        return self.stack.pop()          # ONLY next() consumes

    def _is_leaf(self, x):
        return not isinstance(x, list)

    def _children(self, x):
        return x
```

**Interviewer follow-ups to be ready for**

- *"Support `remove()`"* — you must remember the element `next()` just returned and
  guard against `remove()` before any `next()` / two `remove()`s in a row. Keep a
  `lastReturned` field plus a `canRemove` boolean; `next()` sets it, `remove()` clears it.
  Under the buffer-one-ahead idiom this is genuinely hard: the source iterator has
  *already* moved past the element you want to remove, so `remove()` would delete the
  wrong item — this is exactly why `java.util` iterators do not pre-fetch.
- *"What if the source is infinite / a huge file?"* — the lazy idioms above already work;
  emphasise that no version materialises the full sequence.
- *"Is `next()` really O(1)?"* — see the amortized argument in **2-5**.

## 2) LC Example

### 2-1) Peeking Iterator — LC 284

```python
# python
# LC 284. Peeking Iterator
# IDEA: wrap the given iterator and cache the next value so peek() is non-destructive
class PeekingIterator:
    def __init__(self, iterator):
        self.it = iterator
        self.buffer = self.it.next() if self.it.hasNext() else None

    def peek(self):
        return self.buffer

    def next(self):
        ret = self.buffer
        self.buffer = self.it.next() if self.it.hasNext() else None
        return ret

    def hasNext(self):
        return self.buffer is not None
```

### 2-2) Flatten Nested List Iterator — LC 341

```python
# python
# LC 341. Flatten Nested List Iterator
# IDEA: keep a stack of NestedInteger; lazily unwrap lists in hasNext()
class NestedIterator:
    def __init__(self, nestedList):
        # push in reverse so the first element ends up on top of the stack
        self.stack = nestedList[::-1]

    def next(self):
        return self.stack.pop().getInteger()

    def hasNext(self):
        while self.stack:
            top = self.stack[-1]
            if top.isInteger():
                return True
            self.stack.pop()
            self.stack.extend(top.getList()[::-1])
        return False
```

> **Related — explicit-stack iteration over a tree.** LC 173 (Binary Search Tree Iterator)
> is the same "push the frontier, expand lazily" shape applied to in-order traversal
> (push the left spine, pop, then push the right child's left spine). Full templates live
> in [`stack.md`](stack.md) §2-19 and [`bst.md`](bst.md) §2-5 — the contract rules in
> **1-2** apply to it unchanged.

### 2-3) Run-Length / bulk-skip Iterator — LC 900 ⭐⭐⭐⭐

**Pattern**: the source is **compressed** (`[count, value, count, value, ...]`) and `next(n)`
asks to consume `n` elements at once. Never expand the runs — `count` can be `10^9`.
**Key Idea**: keep a cursor at the current run plus how much of it is already `used`, then
**subtract whole runs** until `n` fits inside the current one.

```java
// java
// LC 900 - RLE Iterator
// IDEA: cursor (i) on the current run + how many of it are used.
//       While n overflows the remaining part of the run, consume the remainder
//       and jump to the next run. Exhausted -> -1.
// time = O(1) amortized per next() (each run is skipped at most once,
//        so a whole run of calls is O(len(encoding))), space = O(1)
class RLEIterator {
    private final int[] encoding;
    private int i = 0;        // index of the current COUNT
    private long used = 0;    // how many of encoding[i] already consumed

    public RLEIterator(int[] encoding) { this.encoding = encoding; }

    public int next(int n) {
        long k = n;           // long: count and n are both up to 1e9 -> int overflows
        while (i < encoding.length && used + k > encoding[i]) {
            k -= encoding[i] - used;   // eat what is left of this run
            used = 0;
            i += 2;                    // move to the next (count, value) pair
        }
        if (i >= encoding.length) return -1;   // stream exhausted
        used += k;
        return encoding[i + 1];
    }
}
```

```python
# python
# LC 900 - RLE Iterator
# IDEA: same cursor + used counter; Python ints don't overflow so no cast needed
# time = O(1) amortized per next(), space = O(1)
class RLEIterator:
    def __init__(self, encoding):
        self.enc = encoding
        self.i = 0        # index of the current count
        self.used = 0     # consumed portion of enc[i]

    def next(self, n):
        while self.i < len(self.enc) and self.used + n > self.enc[self.i]:
            n -= self.enc[self.i] - self.used
            self.used = 0
            self.i += 2
        if self.i >= len(self.enc):
            return -1
        self.used += n
        return self.enc[self.i + 1]
```

**Traps**

- **Zero-length runs** (`[0, 9]`) must be skipped — the `while` handles them for free
  because `used + n > 0` for any `n >= 1`.
- **Overflow in Java**: `used + n` with both near `10^9` exceeds `int`. Use `long`.
- Once exhausted, every later `next()` must keep returning `-1` (the `i >= length`
  guard is sticky since `i` never decreases).

### 2-4) Iterator over a generated sequence — LC 1286 ⭐⭐⭐

**Pattern**: there is no underlying collection at all — the elements are **combinatorially
generated**. The naive solution precomputes all `C(n, k)` combinations in the constructor
(`O(C(n,k) * k)` memory); the iterator version stores only the *current* state and advances
it in `O(k)`.

**Key Idea**: hold the `k` chosen indices as an **odometer**. To advance, find the rightmost
index that is not yet at its maximum (`idx[i] == i + n - k`), bump it, and reset everything
to its right to be consecutive.

```java
// java
// LC 1286 - Iterator for Combination
// IDEA: keep only the k current indices; advance like an odometer.
//       characters is already sorted, so index order == lexicographic order.
// time = O(k) per next(), O(1) hasNext(), space = O(k)  (NOT O(C(n,k)))
class CombinationIterator {
    private final char[] s;
    private final int k;
    private final int[] idx;      // current combination as indices into s
    private boolean done = false;

    public CombinationIterator(String characters, int combinationLength) {
        this.s = characters.toCharArray();
        this.k = combinationLength;
        this.idx = new int[k];
        for (int i = 0; i < k; i++) idx[i] = i;   // first combination = 0,1,...,k-1
    }

    public boolean hasNext() { return !done; }    // pure, idempotent

    public String next() {
        StringBuilder sb = new StringBuilder();
        for (int i : idx) sb.append(s[i]);        // emit CURRENT, then advance

        int i = k - 1;
        while (i >= 0 && idx[i] == i + s.length - k) i--;   // idx[i] maxed out?
        if (i < 0) {
            done = true;                          // last combination just emitted
        } else {
            idx[i]++;
            for (int j = i + 1; j < k; j++) idx[j] = idx[j - 1] + 1;  // reset the tail
        }
        return sb.toString();
    }
}
```

```python
# python
# LC 1286 - Iterator for Combination
# IDEA: odometer over the k chosen indices; emit current, then step forward
# time = O(k) per next(), O(1) hasNext(), space = O(k)
class CombinationIterator:
    def __init__(self, characters, combinationLength):
        self.s = characters
        self.k = combinationLength
        self.idx = list(range(self.k))
        self.done = False

    def hasNext(self):
        return not self.done

    def next(self):
        res = "".join(self.s[i] for i in self.idx)   # emit current

        i = self.k - 1
        while i >= 0 and self.idx[i] == i + len(self.s) - self.k:
            i -= 1
        if i < 0:
            self.done = True
        else:
            self.idx[i] += 1
            for j in range(i + 1, self.k):
                self.idx[j] = self.idx[j - 1] + 1
        return res
```

**Variation — bitmask counter** (same LC 1286): since `n <= 15`, walk `mask` from
`(1 << n) - 1` down to `0` and keep only masks with `popcount == k`; bit `n-1-i` set means
`s[i]` is chosen, so descending masks come out in lexicographic order. `O(1)` state, but
`next()` is no longer `O(k)` worst case since it may scan past many rejected masks.

**Contrast with backtracking**: a normal `combine()` recursion produces the same sequence,
but recursion cannot be paused. The odometer *is* that recursion with its call stack
flattened into `idx[]` — which is exactly what "make it an iterator" means.

### 2-5) Amortized O(1) `next()` — lazy transfer — LC 232 ⭐⭐⭐⭐

**Pattern**: consuming in an order the storage doesn't support. Rather than paying on every
operation, **defer the reordering and do it in one batch, only when the output side runs dry**.
This is the canonical proof obligation behind rule 4 in **1-2**.

**Key Idea**: two stacks. `in` takes pushes; `out` serves reads. Refill `out` from `in`
**only when `out` is empty** — the `if` is what makes it amortized O(1); transferring
eagerly on every call would be O(n) per operation.

```java
// java
// LC 232 - Implement Queue using Stacks
// IDEA: in-stack absorbs writes, out-stack serves reads; flip only when out is empty
// time = O(1) amortized per op (worst case O(n) for one pop), space = O(n)
class MyQueue {
    private final Deque<Integer> in = new ArrayDeque<>();
    private final Deque<Integer> out = new ArrayDeque<>();

    public void push(int x) { in.push(x); }

    public int pop()  { shift(); return out.pop(); }
    public int peek() { shift(); return out.peek(); }   // the "hasNext must not consume" analogue

    public boolean empty() { return in.isEmpty() && out.isEmpty(); }

    private void shift() {
        if (out.isEmpty()) {            // the guard IS the amortization
            while (!in.isEmpty()) out.push(in.pop());
        }
    }
}
```

```python
# python
# LC 232 - Implement Queue using Stacks
# time = O(1) amortized per op, space = O(n)
class MyQueue:
    def __init__(self):
        self.in_st = []
        self.out_st = []

    def push(self, x):
        self.in_st.append(x)

    def _shift(self):
        if not self.out_st:                  # only when drained
            while self.in_st:
                self.out_st.append(self.in_st.pop())

    def pop(self):
        self._shift()
        return self.out_st.pop()

    def peek(self):
        self._shift()
        return self.out_st[-1]

    def empty(self):
        return not self.in_st and not self.out_st
```

**The amortized argument (say this out loud in the interview)**

> Each element is pushed to `in` exactly once, moved to `out` exactly once, and popped from
> `out` exactly once — **3 stack operations over its entire lifetime, regardless of the call
> pattern**. So `n` operations cost `O(n)` total, i.e. `O(1)` amortized. A *single* `pop()`
> can still be `O(n)`; amortized is not worst-case, and interviewers do probe that difference.

Accounting/potential view: charge each `push` 3 units — 1 to do the push, 2 banked to pay for
the future move-and-pop. The bank is never negative, so the amortized cost is a constant.

The same reasoning licenses the lazy stack in **2-2** / LC 341: each `NestedInteger` is pushed
and popped at most once, so a full traversal is `O(total nodes)` even though one `hasNext()`
call may unwrap a deeply nested chain.
