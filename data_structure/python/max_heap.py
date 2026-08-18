#---------------------------------------------------------------
# MAX HEAP -- the heapq negation trick
#---------------------------------------------------------------
#
# Scope: the 10-line max-heap you should actually write in an
#        interview. See MaxHeap.py for the from-scratch array version
#        and heap.py for the concepts.
#
# Python's `heapq` is a MIN-heap and there is no max-heap flag. The
# standard workaround is to store NEGATED values, which reverses the
# ordering, and negate again on the way out:
#
#     push 3, 9, 5   ->  heap holds  -9, -5, -3
#     min of that is -9  ->  negate  ->  9, the true maximum
#
# Only the sign is flipped, so every heapq cost carries over.
#
# NOTE this works for numbers. For objects, push a tuple whose first
# element is a negated sort key, e.g. `(-count, word)`.
#
# Time  : push O(log N), pop O(log N), top O(1)
# Space : O(N)
#
# References:
#   - https://docs.python.org/3/library/heapq.html


import heapq


class MaxHeap:
    """Max-heap wrapper around heapq, storing values negated."""

    def __init__(self, values=()):
        self.heap = [-v for v in values]
        heapq.heapify(self.heap)          # O(N), cheaper than N pushes

    def __len__(self):
        return len(self.heap)

    def is_empty(self):
        return not self.heap

    def push(self, value):
        heapq.heappush(self.heap, -value)

    def pop(self):
        """Remove and return the LARGEST value."""
        if self.is_empty():
            raise IndexError("pop from an empty heap")
        return -heapq.heappop(self.heap)

    def top(self):
        """The largest value, without removing it."""
        if self.is_empty():
            raise IndexError("top of an empty heap")
        return -self.heap[0]


if __name__ == "__main__":
    heap = MaxHeap()
    assert heap.is_empty()

    for value in [3, 9, 5, 1]:
        heap.push(value)
    assert len(heap) == 4
    assert heap.top() == 9                  # top does not remove
    assert len(heap) == 4

    assert [heap.pop() for _ in range(4)] == [9, 5, 3, 1]
    assert heap.is_empty()

    # heapify an existing list in one shot
    heap = MaxHeap([4, 8, 2])
    assert heap.pop() == 8

    # negatives are handled fine -- only the sign of the KEY is flipped
    heap = MaxHeap([-5, -1, -9])
    assert heap.pop() == -1

    # for objects, negate the sort key inside a tuple
    counts = MaxHeap()
    for count, word in [(2, "b"), (5, "a"), (1, "c")]:
        heapq.heappush(counts.heap, (-count, word))
    assert heapq.heappop(counts.heap) == (-5, "a")

    try:
        MaxHeap().pop()
        raise AssertionError("expected IndexError")
    except IndexError:
        pass

    print("Success.")
