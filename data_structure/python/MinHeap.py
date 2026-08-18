#---------------------------------------------------------------
# MIN HEAP -- array implementation from scratch
#---------------------------------------------------------------
#
# Scope: sift-up / sift-down written out by hand, on a 1-indexed
#        array. See heap.py for the concepts and the built-in heapq
#        you would actually use, MaxHeap.py for the mirror image.
#
# The heap is a COMPLETE binary tree, so it needs no node objects --
# the array index IS the tree structure. Storing the root at index 1
# (leaving slot 0 unused) makes the arithmetic clean:
#
#     parent of i : i // 2          index   1  2  3  4  5
#     left   of i : i * 2           value   1  3  6  5  9
#     right  of i : i * 2 + 1
#
#             1
#           /   \             MIN-heap property:
#          3     6            every parent <= both children
#         / \                 (siblings are NOT ordered)
#        5   9
#
# THE TWO MOVES
#   add():  put the new value in the first free slot, then SIFT UP --
#           swap it with its parent while it is smaller.
#   pop():  take the root, move the LAST element into the root, then
#           SIFT DOWN -- swap it with its smaller child while it is
#           bigger than one of them.
# Both walk one root-to-leaf path, so both are O(log N).
#
# Time  : peek O(1), add O(log N), pop O(log N)
# Space : O(N)
#
# References:
#   - https://leetcode.com/explore/learn/card/heap/643/heap/4017/


class MinHeap:
    """Fixed-capacity min-heap over a 1-indexed array."""

    def __init__(self, capacity):
        self.capacity = capacity
        # slot 0 is deliberately unused so parent/child arithmetic stays simple
        self.heap = [0] * (capacity + 1)
        self.size = 0

    def __len__(self):
        return self.size

    def __str__(self):
        return str(self.heap[1:self.size + 1])

    def is_empty(self):
        return self.size == 0

    def peek(self):
        """The minimum, in O(1). It is always at the root."""
        if self.is_empty():
            raise IndexError("peek at an empty heap")
        return self.heap[1]

    def add(self, element):
        """Append at the end, then bubble the value UP to its place."""
        if self.size >= self.capacity:
            raise OverflowError("heap is full (capacity={})".format(self.capacity))

        self.size += 1
        self.heap[self.size] = element

        index = self.size
        while index > 1 and self.heap[index] < self.heap[index // 2]:
            parent = index // 2
            self.heap[index], self.heap[parent] = self.heap[parent], self.heap[index]
            index = parent

    def pop(self):
        """Remove and return the minimum, then bubble the new root DOWN."""
        if self.is_empty():
            raise IndexError("pop from an empty heap")

        smallest = self.heap[1]
        self.heap[1] = self.heap[self.size]      # last element takes the root
        self.size -= 1

        index = 1
        while index * 2 <= self.size:            # while the node has a left child
            left = index * 2
            right = left + 1
            # NOTE the `right <= self.size` guard: the right child may not
            # exist, and without this we would read a stale slot past the end
            child = right if right <= self.size and self.heap[right] < self.heap[left] else left
            if self.heap[index] <= self.heap[child]:
                break                            # heap property restored
            self.heap[index], self.heap[child] = self.heap[child], self.heap[index]
            index = child

        return smallest


if __name__ == "__main__":
    heap = MinHeap(5)
    assert heap.is_empty()

    heap.add(3)
    heap.add(1)
    heap.add(2)
    assert str(heap) == "[1, 3, 2]"          # array form; the tree is 1 -> (3, 2)
    assert len(heap) == 3

    assert heap.peek() == 1                  # peek does not remove
    assert len(heap) == 3

    assert heap.pop() == 1
    assert heap.pop() == 2
    assert heap.pop() == 3
    assert heap.is_empty()

    heap.add(4)
    heap.add(5)
    assert str(heap) == "[4, 5]"

    # a node with only a LEFT child must not read past the end
    odd = MinHeap(5)
    for value in [5, 4, 3]:
        odd.add(value)
    assert [odd.pop(), odd.pop(), odd.pop()] == [3, 4, 5]

    # draining always yields sorted output
    heap = MinHeap(8)
    for value in [8, 3, 5, 1, 7, 2]:
        heap.add(value)
    assert [heap.pop() for _ in range(6)] == [1, 2, 3, 5, 7, 8]

    full = MinHeap(1)
    full.add(1)
    try:
        full.add(2)
        raise AssertionError("expected OverflowError")
    except OverflowError:
        pass

    try:
        MinHeap(1).pop()
        raise AssertionError("expected IndexError")
    except IndexError:
        pass

    print("Success.")
