#---------------------------------------------------------------
# LFU CACHE  (LC 460)
#---------------------------------------------------------------
#
# Least Frequently Used cache. Evict the least-frequently-used key,
# breaking ties by least-recently-used. get/put run in O(1).
#
# Idea: track each key's use count, and keep an OrderedDict of keys per
# count so we can evict the LRU among the lowest-count keys.


from collections import OrderedDict


class Node:
    def __init__(self, key, val, count):
        self.key = key
        self.val = val
        self.count = count


class LFUCache:
    def __init__(self, capacity):
        self.capacity = capacity
        self.key_node = {}            # key   -> Node
        self.count_node = {}          # count -> OrderedDict(key -> Node)
        self.min_count = None         # current lowest count

    def get(self, key):
        if key not in self.key_node:
            return -1
        node = self.key_node[key]

        # move the node from its current count bucket up to count + 1
        del self.count_node[node.count][key]
        if not self.count_node[node.count]:
            del self.count_node[node.count]
            if self.min_count == node.count:
                self.min_count += 1
        node.count += 1
        self.count_node.setdefault(node.count, OrderedDict())[key] = node
        return node.val

    def put(self, key, value):
        if self.capacity == 0:
            return
        if key in self.key_node:
            self.key_node[key].val = value
            self.get(key)                 # bump its count
            return
        if len(self.key_node) == self.capacity:
            # evict least-frequently-used, least-recently-used key
            evicted_key, _ = self.count_node[self.min_count].popitem(last=False)
            del self.key_node[evicted_key]
        node = Node(key, value, 1)
        self.key_node[key] = node
        self.count_node.setdefault(1, OrderedDict())[key] = node
        self.min_count = 1


if __name__ == "__main__":
    cache = LFUCache(2)
    cache.put(1, 1)
    cache.put(2, 2)
    assert cache.get(1) == 1
    cache.put(3, 3)                       # evicts key 2 (least frequently used)
    assert cache.get(2) == -1
    assert cache.get(3) == 3
    print("Success.")
