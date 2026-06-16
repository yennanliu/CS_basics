#---------------------------------------------------------------
# LRU CACHE  (LC 146)
#---------------------------------------------------------------
#
# Least Recently Used cache. On capacity overflow, evict the least
# recently used key. V1 supports get/put in O(1).


# V0 : array + hashmap (simple, but get/put are O(N) due to list.remove)
class LRUCacheArray:
    def __init__(self, capacity):
        self.capacity = capacity
        self._order = []          # keys, least-recently-used first
        self._lookup = {}         # key -> value

    def get(self, key):
        if key not in self._lookup:
            return -1
        self._order.remove(key)
        self._order.append(key)
        return self._lookup[key]

    def put(self, key, value):
        if key in self._lookup:
            self._lookup[key] = value
            self._order.remove(key)
            self._order.append(key)
            return
        if len(self._order) == self.capacity:
            evicted = self._order.pop(0)
            del self._lookup[evicted]
        self._order.append(key)
        self._lookup[key] = value


# V1 : OrderedDict (get/put in O(1)) -- recommended
# An OrderedDict combines a hashmap and a doubly linked list, which is
# exactly what an LRU cache needs.
from collections import OrderedDict


class LRUCache(OrderedDict):
    def __init__(self, capacity):
        super().__init__()
        self.capacity = capacity

    def get(self, key):
        if key not in self:
            return -1
        self.move_to_end(key)
        return self[key]

    def put(self, key, value):
        if key in self:
            self.move_to_end(key)
        self[key] = value
        if len(self) > self.capacity:
            self.popitem(last=False)      # evict least recently used


if __name__ == "__main__":
    cache = LRUCache(2)
    cache.put(1, 1)
    cache.put(2, 2)
    assert cache.get(1) == 1
    cache.put(3, 3)                       # evicts key 2
    assert cache.get(2) == -1
    print("Success.")
