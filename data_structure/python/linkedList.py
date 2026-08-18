#---------------------------------------------------------------
# LINKED LIST (singly linked)
#---------------------------------------------------------------
#
# Scope: the SINGLY linked list (each node points forward only).
#        See doublylinkedlist.py for the prev+next variant.
#
#     head
#      |
#      v
#     [1] -> [2] -> [3] -> None
#
# Unlike an array, nodes are NOT contiguous in memory, so there is no
# index arithmetic: reaching position i means walking i links. In
# exchange, inserting/removing costs O(1) once you hold the node
# before the target -- no shifting.
#
# Time  : prepend            O(1)
#         append             O(1)   (a `tail` pointer is kept)
#         get / insert / remove by index   O(N)  (walk to the node)
#         search             O(N)
#         reverse            O(N)
# Space : O(N)
#
# References:
#   - https://www.geeksforgeeks.org/python-program-for-reverse-a-linked-list/


class Node:
    """A single link: a value plus a pointer to the next node."""

    def __init__(self, value, next=None):
        self.value = value
        self.next = next


class LinkedList:
    """Singly linked list with head and tail pointers."""

    def __init__(self, values=()):
        self.head = None
        self.tail = None
        self._size = 0
        for value in values:
            self.append(value)

    def __len__(self):
        return self._size

    def __iter__(self):
        node = self.head
        while node:
            yield node.value
            node = node.next

    def __str__(self):
        return " -> ".join(str(v) for v in self)

    def to_list(self):
        return list(self)

    def is_empty(self):
        return self.head is None

    def prepend(self, value):
        """Insert at the FRONT.  before: 1->2   after: 0->1->2"""
        node = Node(value, next=self.head)
        self.head = node
        if self.tail is None:            # first node ever -> it is also the tail
            self.tail = node
        self._size += 1

    def append(self, value):
        """Insert at the END.  before: 1->2   after: 1->2->3"""
        node = Node(value)
        if self.tail is None:
            self.head = self.tail = node
        else:
            self.tail.next = node
            self.tail = node
        self._size += 1

    def _node_at(self, index):
        """Return the node at `index`. Caller must validate the index."""
        node = self.head
        for _ in range(index):
            node = node.next
        return node

    def get(self, index):
        """Return the value at `index`."""
        if not 0 <= index < self._size:
            raise IndexError("index out of range: {}".format(index))
        return self._node_at(index).value

    def insert(self, index, value):
        """Insert `value` so that it ends up AT `index`.

            before : 1 -> 2 -> 3     insert(1, 9)
            after  : 1 -> 9 -> 2 -> 3
        """
        if not 0 <= index <= self._size:      # == size is allowed: that is an append
            raise IndexError("index out of range: {}".format(index))
        if index == 0:
            return self.prepend(value)
        if index == self._size:
            return self.append(value)
        prev = self._node_at(index - 1)       # the node BEFORE the target slot
        prev.next = Node(value, next=prev.next)
        self._size += 1

    def remove(self, index):
        """Remove and return the value at `index`.

            before : 1 -> 2 -> 3     remove(1)
            after  : 1 -> 3
        """
        if not 0 <= index < self._size:
            raise IndexError("index out of range: {}".format(index))
        if index == 0:
            removed = self.head
            self.head = removed.next
            if self.head is None:             # list became empty
                self.tail = None
        else:
            prev = self._node_at(index - 1)
            removed = prev.next
            prev.next = removed.next
            if removed is self.tail:          # removed the last node
                self.tail = prev
        self._size -= 1
        return removed.value

    def search(self, value):
        """Return the index of the first node holding `value`, else -1."""
        for i, v in enumerate(self):
            if v == value:
                return i
        return -1

    def reverse(self):
        """Reverse in place by flipping every `next` pointer.

            before : 1 -> 2 -> 3
            after  : 3 -> 2 -> 1

        `prev` trails one node behind `current`; `nxt` is saved first
        because rewriting current.next destroys the way forward.
        """
        prev, current = None, self.head
        self.head, self.tail = self.tail, self.head
        while current:
            nxt = current.next
            current.next = prev
            prev = current
            current = nxt


if __name__ == "__main__":
    ll = LinkedList([1, 2, 3])
    assert str(ll) == "1 -> 2 -> 3"
    assert len(ll) == 3

    ll.prepend(0)
    ll.append(4)
    assert ll.to_list() == [0, 1, 2, 3, 4]

    ll.insert(2, 99)
    assert ll.to_list() == [0, 1, 99, 2, 3, 4]

    assert ll.get(2) == 99
    assert ll.search(99) == 2
    assert ll.search(-1) == -1

    assert ll.remove(2) == 99
    assert ll.to_list() == [0, 1, 2, 3, 4]

    assert ll.remove(4) == 4              # removing the tail keeps tail correct
    assert ll.tail.value == 3
    ll.append(4)
    assert ll.to_list() == [0, 1, 2, 3, 4]

    ll.reverse()
    assert ll.to_list() == [4, 3, 2, 1, 0]
    assert ll.head.value == 4 and ll.tail.value == 0

    # emptying the list resets both pointers
    empty = LinkedList([1])
    empty.remove(0)
    assert empty.is_empty() and empty.tail is None

    print("Success.")
