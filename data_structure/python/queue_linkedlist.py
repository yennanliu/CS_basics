#---------------------------------------------------------------
# QUEUE - linked list implementation
#---------------------------------------------------------------
#
# Scope: the POINTER-based queue (head/tail node references).
#        See queue_array.py for the array-backed version and the
#        trade-off between the two.
#        (Named queue_linkedlist.py, not queue.py, so it does not
#        shadow Python's standard library `queue` module.)
#
# FIFO (First In, First Out): the first item enqueued is the first
# one dequeued.
#
#     dequeue <--- [head] 1 -> 2 -> 3 [tail] <--- enqueue
#
# Keeping a `tail` pointer is what makes enqueue O(1): without it we
# would have to walk the whole list to find the end.
#
# Time  : enqueue / dequeue / peek / is_empty -> O(1)
#         size (as maintained counter)        -> O(1)
# Space : O(N)
#
# References:
#   - http://zhaochj.github.io/2016/05/15/2016-05-15-数据结构-单端队列/


class Node:
    """A single link: a value plus a pointer to the next node."""

    def __init__(self, value):
        self.value = value
        self.next = None


class Queue:
    """FIFO queue built from singly-linked nodes."""

    def __init__(self):
        self.head = None         # dequeue from here
        self.tail = None         # enqueue to here
        self._size = 0

    def __len__(self):
        return self._size

    def __str__(self):
        values, node = [], self.head
        while node:
            values.append(str(node.value))
            node = node.next
        return " -> ".join(values)

    def is_empty(self):
        return self.head is None

    def enqueue(self, value):
        """Append to the TAIL."""
        node = Node(value)
        if self.tail is None:            # empty queue: head and tail are the same node
            self.head = self.tail = node
        else:
            self.tail.next = node
            self.tail = node
        self._size += 1

    def dequeue(self):
        """Remove and return the value at the HEAD."""
        if self.is_empty():
            raise IndexError("dequeue from an empty queue")
        node = self.head
        self.head = node.next
        if self.head is None:            # queue just became empty -> drop the tail too
            self.tail = None
        self._size -= 1
        return node.value

    def peek(self):
        """Return the head value WITHOUT removing it."""
        if self.is_empty():
            raise IndexError("peek at an empty queue")
        return self.head.value

    def size(self):
        return self._size


if __name__ == "__main__":
    q = Queue()
    assert q.is_empty()

    for i in range(3):
        q.enqueue(i)
    assert str(q) == "0 -> 1 -> 2"
    assert q.size() == 3

    # first in, first out
    assert q.peek() == 0
    assert q.dequeue() == 0
    assert q.dequeue() == 1
    assert q.dequeue() == 2
    assert q.is_empty()
    assert q.tail is None            # tail is reset, so enqueue still works

    q.enqueue(9)
    assert q.peek() == 9

    print("Success.")
