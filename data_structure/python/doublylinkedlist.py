#---------------------------------------------------------------
# DOUBLY LINKED LIST
#---------------------------------------------------------------
#
# Scope: the DOUBLY linked list (each node knows both neighbours).
#        See linkedList.py for the singly linked version.
#
#     None <- [1] <-> [2] <-> [3] -> None
#              ^                ^
#             head             tail
#
# The extra `prev` pointer costs one reference per node and buys:
#   - backwards traversal
#   - O(1) removal of a node you already hold (no need to walk the
#     list to find its predecessor)
# That second property is exactly why an LRU cache is a hash map
# plus a doubly linked list -- see algorithm/python/lru_cache.py.
#
# Time  : prepend / append          O(1)
#         remove_node(node)         O(1)
#         get / insert / remove by index   O(N)
# Space : O(N)
#
# References:
#   - https://www.geeksforgeeks.org/doubly-linked-list/


class Node:
    """A single link, pointing at BOTH neighbours."""

    def __init__(self, value):
        self.value = value
        self.prev = None
        self.next = None


class DoublyLinkedList:
    """Doubly linked list with head and tail pointers."""

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
        return " <-> ".join(str(v) for v in self)

    def to_list(self):
        return list(self)

    def to_list_reversed(self):
        """Walk backwards from the tail -- impossible in a singly linked list."""
        values, node = [], self.tail
        while node:
            values.append(node.value)
            node = node.prev
        return values

    def is_empty(self):
        return self.head is None

    def prepend(self, value):
        """Insert at the FRONT."""
        node = Node(value)
        if self.head is None:
            self.head = self.tail = node
        else:
            node.next = self.head
            self.head.prev = node
            self.head = node
        self._size += 1
        return node

    def append(self, value):
        """Insert at the END."""
        node = Node(value)
        if self.tail is None:
            self.head = self.tail = node
        else:
            node.prev = self.tail
            self.tail.next = node
            self.tail = node
        self._size += 1
        return node

    def _node_at(self, index):
        node = self.head
        for _ in range(index):
            node = node.next
        return node

    def get(self, index):
        if not 0 <= index < self._size:
            raise IndexError("index out of range: {}".format(index))
        return self._node_at(index).value

    def insert(self, index, value):
        """Insert `value` so that it ends up AT `index`.

        Four pointers have to be rewired -- draw it before you code it:

            leader <-> follower          becomes
            leader <-> new <-> follower
        """
        if not 0 <= index <= self._size:
            raise IndexError("index out of range: {}".format(index))
        if index == 0:
            return self.prepend(value)
        if index == self._size:
            return self.append(value)

        node = Node(value)
        leader = self._node_at(index - 1)
        follower = leader.next

        node.prev = leader
        node.next = follower
        leader.next = node
        follower.prev = node
        self._size += 1
        return node

    def remove_node(self, node):
        """Unlink a node we already hold -- O(1), no traversal needed."""
        if node.prev:
            node.prev.next = node.next
        else:                                  # node was the head
            self.head = node.next
        if node.next:
            node.next.prev = node.prev
        else:                                  # node was the tail
            self.tail = node.prev
        node.prev = node.next = None
        self._size -= 1
        return node.value

    def remove(self, index):
        """Remove and return the value at `index`."""
        if not 0 <= index < self._size:
            raise IndexError("index out of range: {}".format(index))
        return self.remove_node(self._node_at(index))


if __name__ == "__main__":
    dll = DoublyLinkedList([1, 7, 6])
    assert str(dll) == "1 <-> 7 <-> 6"

    dll.append(4)
    dll.prepend(0)
    assert dll.to_list() == [0, 1, 7, 6, 4]

    # the same list read from the tail backwards
    assert dll.to_list_reversed() == [4, 6, 7, 1, 0]

    dll.insert(3, 8)
    assert dll.to_list() == [0, 1, 7, 8, 6, 4]
    assert dll.to_list_reversed() == [4, 6, 8, 7, 1, 0]

    assert dll.get(3) == 8
    assert dll.remove(3) == 8
    assert dll.to_list() == [0, 1, 7, 6, 4]

    # O(1) removal of a node we kept a reference to
    node = dll.insert(2, 99)
    assert dll.to_list() == [0, 1, 99, 7, 6, 4]
    dll.remove_node(node)
    assert dll.to_list() == [0, 1, 7, 6, 4]

    # removing the ends keeps head/tail consistent in both directions
    dll.remove(0)
    dll.remove(len(dll) - 1)
    assert dll.to_list() == [1, 7, 6]
    assert dll.to_list_reversed() == [6, 7, 1]

    single = DoublyLinkedList([1])
    single.remove(0)
    assert single.is_empty() and single.head is None and single.tail is None

    print("Success.")
