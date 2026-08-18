#---------------------------------------------------------------
# QUEUE - array implementation
#---------------------------------------------------------------
#
# Scope: the ARRAY-backed queue, plus the O(N) dequeue problem it
#        creates and how collections.deque solves it.
#        See queue_linkedlist.py for the linked-list version.
#
# FIFO (First In, First Out):
#
#     dequeue <--- 1, 2, 3 <--- enqueue
#                  ^        ^
#                front     rear
#
# BEWARE: a plain Python list makes dequeue O(N), because
# `list.pop(0)` has to shift every remaining element left one slot:
#
#     x = [1, 2, 3];  x.pop(0)  ->  x = [2, 3]   (2 and 3 both moved)
#
# That is fine for learning, but in real code use
# `collections.deque`, which is O(1) at BOTH ends (see the demo below).
#
# Time  : enqueue O(1) amortised, dequeue O(N), peek/size O(1)
# Space : O(N)
#
# References:
#   - https://github.com/yennanliu/Data-Structures-using-Python/blob/master/Queue/Queue.py


class Queue:
    """FIFO queue backed by a Python list, with an optional capacity limit."""

    def __init__(self, limit=10):
        self.queue = []
        self.limit = limit

    def __len__(self):
        return len(self.queue)

    def __str__(self):
        # printed front -> rear
        return " ".join(str(i) for i in self.queue)

    def is_empty(self):
        return not self.queue

    def is_full(self):
        return len(self.queue) >= self.limit

    def enqueue(self, data):
        """Append at the REAR. Raises when the queue is full (overflow)."""
        if self.is_full():
            raise OverflowError("queue overflow (limit={})".format(self.limit))
        self.queue.append(data)

    def dequeue(self):
        """Remove and return the FRONT item. Raises when empty (underflow)."""
        if self.is_empty():
            raise IndexError("dequeue from an empty queue")
        return self.queue.pop(0)     # O(N): everything after index 0 shifts left

    def peek(self):
        """Return the front item WITHOUT removing it."""
        if self.is_empty():
            raise IndexError("peek at an empty queue")
        return self.queue[0]

    def size(self):
        return len(self.queue)


if __name__ == "__main__":
    q = Queue(limit=3)
    assert q.is_empty()

    q.enqueue(1)
    q.enqueue(2)
    q.enqueue(3)
    assert str(q) == "1 2 3"
    assert q.size() == 3

    # first in, first out
    assert q.peek() == 1
    assert q.dequeue() == 1
    assert q.dequeue() == 2
    assert str(q) == "3"

    q.enqueue(4)
    q.enqueue(5)
    try:
        q.enqueue(6)                 # limit is 3 -> overflow
        raise AssertionError("expected OverflowError")
    except OverflowError:
        pass

    q.dequeue(); q.dequeue(); q.dequeue()
    try:
        q.dequeue()                  # empty -> underflow
        raise AssertionError("expected IndexError")
    except IndexError:
        pass

    # the production-ready version: O(1) at both ends
    from collections import deque

    dq = deque()
    dq.append(1)                     # enqueue
    dq.append(2)
    assert dq.popleft() == 1         # dequeue, O(1) -- no shifting
    assert list(dq) == [2]

    print("Success.")
