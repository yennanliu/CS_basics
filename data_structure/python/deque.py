#---------------------------------------------------------------
# DEQUE (double-ended queue) -- ring buffer implementation
#---------------------------------------------------------------
#
# Scope: a queue that is O(1) at BOTH ends, built from scratch on a
#        circular array. See queue_array.py for the plain FIFO queue
#        (and why list.pop(0) is O(N)), stack.py for LIFO.
#        In real code use `collections.deque` -- this file shows what
#        it has to solve.
#
#     append_left -->  [ 1, 2, 3 ]  <-- append
#     pop_left    <--               --> pop
#
# A deque is BOTH a stack and a queue:
#     stack : append + pop
#     queue : append + pop_left
#
# THE RING BUFFER
#   Store the items in a fixed array and remember where the front is
#   (`head`) and how many there are (`size`). Nothing ever shifts:
#   removing the front just moves `head` one slot to the right, and
#   every index WRAPS AROUND with `% capacity`.
#
#       slots :  [ 30,  _ ,  _ , 10, 20 ]      capacity 5, size 3
#                             head ^
#       logical order: 10, 20, 30     (20 is at slot 4, 30 wrapped to slot 0)
#
#       item i lives at slot  (head + i) % capacity
#
#   When the array is full it is copied into one twice as large --
#   O(N) once, but rare enough that each append is O(1) AMORTISED.
#
# Time  : append / append_left / pop / pop_left / peeks / [i]  O(1)
#         (the two appends are amortised, because of the resize)
# Space : O(N)
#
# Used by: BFS queues, sliding-window maximum (LC 239), LC 641 Design
#          Circular Deque.
#
# References:
#   - https://docs.python.org/3/library/collections.html#collections.deque


class Deque:
    """Double-ended queue on a circular array that doubles when full."""

    def __init__(self, values=(), capacity=4):
        self.slots = [None] * max(1, capacity)
        self.head = 0                # slot of the FRONT item
        self.size = 0
        for value in values:
            self.append(value)

    def __len__(self):
        return self.size

    def __iter__(self):
        """Front -> back."""
        for i in range(self.size):
            yield self.slots[self._slot(i)]

    def __str__(self):
        return " ".join(str(value) for value in self)

    def __getitem__(self, i):
        """dq[i] -- the i-th item from the front, in O(1)."""
        if not 0 <= i < self.size:
            raise IndexError("index out of range: {}".format(i))
        return self.slots[self._slot(i)]

    def is_empty(self):
        return self.size == 0

    def _slot(self, i):
        """Array slot of the i-th item from the front. `%` is the wrap-around."""
        return (self.head + i) % len(self.slots)

    def _grow_if_full(self):
        """Full -> copy into an array twice the size, front at slot 0 again."""
        if self.size < len(self.slots):
            return
        bigger = [None] * (2 * len(self.slots))
        for i in range(self.size):
            bigger[i] = self.slots[self._slot(i)]    # un-wrap while copying
        self.slots = bigger
        self.head = 0

    #--- back ------------------------------------------------------
    def append(self, value):
        """Add at the BACK: the first free slot after the last item."""
        self._grow_if_full()
        self.slots[self._slot(self.size)] = value
        self.size += 1

    def pop(self):
        """Remove and return the BACK item."""
        if self.is_empty():
            raise IndexError("pop from an empty deque")
        slot = self._slot(self.size - 1)
        value = self.slots[slot]
        self.slots[slot] = None      # drop the reference so it can be freed
        self.size -= 1
        return value

    def peek(self):
        """Return the BACK item without removing it."""
        if self.is_empty():
            raise IndexError("peek at an empty deque")
        return self.slots[self._slot(self.size - 1)]

    #--- front -----------------------------------------------------
    def append_left(self, value):
        """Add at the FRONT: step `head` one slot LEFT, wrapping below 0."""
        self._grow_if_full()
        self.head = (self.head - 1) % len(self.slots)   # -1 % 5 == 4 in Python
        self.slots[self.head] = value
        self.size += 1

    def pop_left(self):
        """Remove and return the FRONT item: step `head` one slot RIGHT."""
        if self.is_empty():
            raise IndexError("pop from an empty deque")
        value = self.slots[self.head]
        self.slots[self.head] = None
        self.head = (self.head + 1) % len(self.slots)
        self.size -= 1
        return value

    def peek_left(self):
        """Return the FRONT item without removing it."""
        if self.is_empty():
            raise IndexError("peek at an empty deque")
        return self.slots[self.head]


if __name__ == "__main__":
    dq = Deque()
    assert dq.is_empty()

    dq.append(2)
    dq.append(3)
    dq.append_left(1)
    assert str(dq) == "1 2 3"
    assert len(dq) == 3
    assert dq.peek_left() == 1 and dq.peek() == 3
    assert dq[0] == 1 and dq[2] == 3

    # as a QUEUE: in at the back, out at the front
    assert dq.pop_left() == 1
    # as a STACK: in at the back, out at the back
    assert dq.pop() == 3
    assert list(dq) == [2]

    # the wrap-around from the header: capacity 5, front at slot 3
    ring = Deque(capacity=5)
    for value in [0, 0, 0, 10, 20]:
        ring.append(value)
    for _ in range(3):
        ring.pop_left()                  # head moves to slot 3, nothing shifts
    ring.append(30)                      # the back wraps round to slot 0
    assert ring.slots == [30, None, None, 10, 20]
    assert ring.head == 3 and list(ring) == [10, 20, 30]

    # growing un-wraps the items into a bigger array
    ring.append(40)
    ring.append(50)
    ring.append(60)                      # 6 items > capacity 5 -> doubled
    assert len(ring.slots) == 10 and ring.head == 0
    assert list(ring) == [10, 20, 30, 40, 50, 60]

    # same answers as the standard library, over a mixed workload
    from collections import deque

    ours = Deque(capacity=1)
    theirs = deque()
    for step in range(200):
        if step % 5 == 0:
            ours.append_left(step)
            theirs.appendleft(step)
        elif step % 5 in (1, 2):
            ours.append(step)
            theirs.append(step)
        elif step % 5 == 3:
            assert ours.pop_left() == theirs.popleft()
        else:
            assert ours.pop() == theirs.pop()
        assert list(ours) == list(theirs)

    for bad_call in (Deque().pop, Deque().pop_left, Deque().peek, Deque().peek_left):
        try:
            bad_call()
            raise AssertionError("expected IndexError")
        except IndexError:
            pass

    print("Success.")
