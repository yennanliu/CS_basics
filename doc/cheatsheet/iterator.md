# Iterator

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
