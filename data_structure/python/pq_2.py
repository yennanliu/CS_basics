#---------------------------------------------------------------
# PRIORITY QUEUE (2) -- stable ordering and updatable priorities
#---------------------------------------------------------------
#
# Scope: the two problems a bare `heapq` does not solve, and the
#        standard fixes. See pq_1.py for the stdlib basics, pq_3.py
#        for the naive list version.
#
# PROBLEM 1 -- TIES
#   Pushing (priority, value) breaks when two priorities are equal:
#   Python moves on to compare the VALUES, which either reorders
#   equal-priority items arbitrarily or raises TypeError on objects
#   that are not comparable at all.
#
#   FIX: push (priority, counter, value) with a monotonically
#   increasing counter. The counter is unique, so it settles every
#   tie before the value is ever looked at -- and because it grows
#   with insertion order, equal priorities come out FIFO. That makes
#   the queue STABLE.
#
# PROBLEM 2 -- CHANGING A PRIORITY
#   heapq has no "decrease-key". Dijkstra and A* need one.
#
#   FIX (used here): keep a dict value -> entry, mutate the entry's
#   priority, and re-heapify -- simple, O(N) per update.
#   FIX (used in practice): push a duplicate entry with the new
#   priority and skip stale entries on pop -- O(log N), at the cost
#   of a larger heap. See algorithm/python/dijkstra.py.
#
# NOTE this class stores HIGHEST priority first, by negating the
# priority on the way in. That is the opposite of pq_1.py.
#
# Time  : enqueue / dequeue  O(log N),  reprioritise O(N)
# Space : O(N)
#
# References:
#   - https://realpython.com/queue-in-python/


from dataclasses import dataclass, field
from heapq import heapify, heappop, heappush
from itertools import count
from typing import Any


class PriorityQueue:
    """Max-priority queue; ties are broken FIFO by insertion order."""

    def __init__(self):
        self.elements = []
        self.counter = count()          # 0, 1, 2, ... never repeats

    def __len__(self):
        return len(self.elements)

    def __iter__(self):
        """Draining iteration: highest priority first."""
        while self.elements:
            yield self.dequeue()

    def is_empty(self):
        return not self.elements

    def enqueue(self, priority, value):
        # -priority  -> a min-heap now pops the HIGHEST priority
        # next(...)  -> unique, so `value` is never compared
        heappush(self.elements, (-priority, next(self.counter), value))

    def dequeue(self):
        if self.is_empty():
            raise IndexError("dequeue from an empty priority queue")
        return heappop(self.elements)[-1]

    def peek(self):
        if self.is_empty():
            raise IndexError("peek at an empty priority queue")
        return self.elements[0][-1]


@dataclass(order=True)
class Element:
    """Heap entry. `order=True` compares priority, then count -- never value."""
    priority: float
    count: int
    value: Any = field(compare=False)


class MutableMinHeap:
    """Min-priority queue whose priorities can be changed after insertion."""

    def __init__(self):
        self.elements = []
        self.by_value = {}
        self.counter = count()

    def __len__(self):
        return len(self.elements)

    def is_empty(self):
        return not self.elements

    def __setitem__(self, value, priority):
        """`heap[value] = priority` -- inserts, or reprioritises in place."""
        if value in self.by_value:
            self.by_value[value].priority = priority
            heapify(self.elements)      # O(N): the mutated entry may sit anywhere
        else:
            element = Element(priority, next(self.counter), value)
            self.by_value[value] = element
            heappush(self.elements, element)

    def __getitem__(self, value):
        return self.by_value[value].priority

    def __contains__(self, value):
        return value in self.by_value

    def dequeue(self):
        """Remove and return the value with the LOWEST priority."""
        if self.is_empty():
            raise IndexError("dequeue from an empty heap")
        element = heappop(self.elements)
        del self.by_value[element.value]
        return element.value


if __name__ == "__main__":
    #--- highest priority first ----------------------------------
    pq = PriorityQueue()
    pq.enqueue(1, "update docs")
    pq.enqueue(3, "fix outage")          # highest number -> served first
    pq.enqueue(2, "write code")

    assert len(pq) == 3
    assert pq.peek() == "fix outage"
    assert list(pq) == ["fix outage", "write code", "update docs"]
    assert pq.is_empty()

    #--- ties come out FIFO, and never compare the payload -------
    stable = PriorityQueue()
    for name in ["first", "second", "third"]:
        stable.enqueue(1, name)          # all the same priority
    assert list(stable) == ["first", "second", "third"]

    objects = PriorityQueue()
    objects.enqueue(1, {"task": "b"})
    objects.enqueue(1, {"task": "a"})    # dicts are not comparable -- fine here
    assert objects.dequeue() == {"task": "b"}

    try:
        PriorityQueue().dequeue()
        raise AssertionError("expected IndexError")
    except IndexError:
        pass

    #--- reprioritising ------------------------------------------
    heap = MutableMinHeap()
    heap["a"] = 3
    heap["b"] = 1
    heap["c"] = 2
    assert "a" in heap and heap["a"] == 3

    heap["a"] = 0                        # promote "a" to the front
    assert heap["a"] == 0
    assert heap.dequeue() == "a"
    assert heap.dequeue() == "b"
    assert heap.dequeue() == "c"
    assert heap.is_empty()
    assert "a" not in heap

    print("Success.")
