#---------------------------------------------------------------
# LRU CACHE  (LC 146)
#---------------------------------------------------------------
#
# A cache with a fixed capacity. When it is full and a new key
# arrives, the key that was used LEAST RECENTLY is thrown out.
# "Used" means read (get) OR written (put).
#
#     capacity 2
#     put(1, a)     [1]              most recent on the right
#     put(2, b)     [1, 2]
#     get(1)        [2, 1]           reading 1 makes it the most recent
#     put(3, c)     [1, 3]           full -> evict 2, the least recent
#     get(2)        -1               gone
#
# Both operations have to be O(1), which needs TWO structures working
# together -- neither one can do it alone:
#
#     need                              structure
#     --------------------------------  ---------------------------
#     find a key instantly              hash map  key -> node
#     know the order of use, and move   doubly linked list, least
#     any entry to the "most recent"    recent at the head, most
#     end instantly                     recent at the tail
#
#     map:   1 --+      3 --+
#                v          v
#     head <-> [1:a] <-> [3:c] <-> tail
#     (least recent)      (most recent)
#
# The list must be DOUBLY linked: removing a node from the middle in
# O(1) needs its previous node, and only a `prev` pointer gives that
# without a walk. (See data_structure/python/doublylinkedlist.py.)
#
# Time  : get O(1), put O(1)      (V0 / V1)
# Space : O(capacity)
#
# In real Python code: functools.lru_cache for memoising a function,
# or V1 below (OrderedDict) for a cache object.


from collections import OrderedDict


# V0 : hash map + doubly linked list, from scratch -- what an
# interviewer wants to see
class Node:
    """One cache entry, linked to its neighbours in recency order."""

    def __init__(self, key=None, value=None):
        self.key = key               # kept so that eviction can delete the map entry
        self.value = value
        self.prev = None
        self.next = None


class LRUCache:
    """LRU cache with O(1) get and put."""

    def __init__(self, capacity):
        self.capacity = capacity
        self.nodes = {}              # key -> Node

        # Two DUMMY nodes that are never removed. With them, every real
        # node always has a neighbour on both sides, so linking and
        # unlinking need no "is this the first / last node?" cases.
        self.head = Node()           # head.next is the LEAST recently used
        self.tail = Node()           # tail.prev is the MOST recently used
        self.head.next = self.tail
        self.tail.prev = self.head

    def _unlink(self, node):
        """Take a node out of the list: its two neighbours skip over it."""
        node.prev.next = node.next
        node.next.prev = node.prev

    def _append(self, node):
        """Put a node just before the tail: the most-recently-used position."""
        last = self.tail.prev
        last.next = node
        node.prev = last
        node.next = self.tail
        self.tail.prev = node

    def get(self, key):
        """Return the value for key (and mark it most recent), or -1."""
        if key not in self.nodes:
            return -1
        node = self.nodes[key]
        self._unlink(node)           # reading counts as a use:
        self._append(node)           # move it to the most-recent end
        return node.value

    def put(self, key, value):
        """Insert or update key, evicting the least recently used if full."""
        if self.capacity <= 0:
            return
        if key in self.nodes:        # update in place, and mark it most recent
            node = self.nodes[key]
            node.value = value
            self._unlink(node)
            self._append(node)
            return

        if len(self.nodes) == self.capacity:
            oldest = self.head.next  # least recently used
            self._unlink(oldest)
            del self.nodes[oldest.key]

        node = Node(key, value)
        self.nodes[key] = node
        self._append(node)

    def keys(self):
        """Keys from least to most recently used (for testing / debugging)."""
        keys = []
        node = self.head.next
        while node is not self.tail:
            keys.append(node.key)
            node = node.next
        return keys


# V1 : OrderedDict -- the short version for real code. An OrderedDict
# IS a hash map threaded with a doubly linked list, so it does both
# jobs: move_to_end() is "mark most recent", popitem(last=False) is
# "evict the least recent".
class LRUCacheOrderedDict:
    def __init__(self, capacity):
        self.capacity = capacity
        self.items = OrderedDict()   # least recently used first

    def get(self, key):
        if key not in self.items:
            return -1
        self.items.move_to_end(key)
        return self.items[key]

    def put(self, key, value):
        if self.capacity <= 0:
            return
        if key in self.items:
            self.items.move_to_end(key)
        self.items[key] = value
        if len(self.items) > self.capacity:
            self.items.popitem(last=False)       # evict the least recently used

    def keys(self):
        return list(self.items)


# V2 : list + dict -- the first thing most people write. Correct, but
# list.remove() and list.pop(0) both walk the list, so get and put are
# O(N). It is here to show what the linked list is FOR.
class LRUCacheArray:
    def __init__(self, capacity):
        self.capacity = capacity
        self._order = []             # keys, least recently used first
        self._lookup = {}            # key -> value

    def get(self, key):
        if key not in self._lookup:
            return -1
        self._order.remove(key)      # O(N): has to find the key in the list
        self._order.append(key)
        return self._lookup[key]

    def put(self, key, value):
        if self.capacity <= 0:
            return
        if key in self._lookup:
            self._lookup[key] = value
            self._order.remove(key)
            self._order.append(key)
            return
        if len(self._order) == self.capacity:
            evicted = self._order.pop(0)         # O(N): everything shifts left
            del self._lookup[evicted]
        self._order.append(key)
        self._lookup[key] = value

    def keys(self):
        return list(self._order)


if __name__ == "__main__":
    for cls in (LRUCache, LRUCacheOrderedDict, LRUCacheArray):
        # the trace from the header
        cache = cls(2)
        cache.put(1, "a")
        cache.put(2, "b")
        assert cache.keys() == [1, 2]
        assert cache.get(1) == "a"
        assert cache.keys() == [2, 1]            # the read moved 1 to the recent end
        cache.put(3, "c")                        # full -> evicts 2, not 1
        assert cache.get(2) == -1
        assert cache.keys() == [1, 3]

        # updating an existing key changes its value AND its recency, not the size
        cache.put(1, "A")
        assert cache.keys() == [3, 1] and cache.get(1) == "A"
        cache.put(4, "d")                        # evicts 3
        assert cache.get(3) == -1 and cache.keys() == [1, 4]

        # LC 146's example
        lc = cls(2)
        lc.put(1, 1)
        lc.put(2, 2)
        assert lc.get(1) == 1
        lc.put(3, 3)
        assert lc.get(2) == -1
        lc.put(4, 4)
        assert lc.get(1) == -1 and lc.get(3) == 3 and lc.get(4) == 4

        # capacity 1 keeps only the newest key; capacity 0 keeps nothing
        one = cls(1)
        one.put("x", 1)
        one.put("y", 2)
        assert one.get("x") == -1 and one.get("y") == 2
        zero = cls(0)
        zero.put("x", 1)
        assert zero.get("x") == -1

    # all three stay identical over a long random workload
    import random

    random.seed(1)
    caches = [LRUCache(4), LRUCacheOrderedDict(4), LRUCacheArray(4)]
    for _ in range(2000):
        key = random.randrange(8)
        if random.random() < 0.5:
            results = [cache.get(key) for cache in caches]
            assert results[0] == results[1] == results[2]
        else:
            value = random.randrange(100)
            for cache in caches:
                cache.put(key, value)
        assert caches[0].keys() == caches[1].keys() == caches[2].keys()

    print("Success.")
