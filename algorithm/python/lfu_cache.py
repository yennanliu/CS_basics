#---------------------------------------------------------------
# LFU CACHE  (LC 460)
#---------------------------------------------------------------
#
# Scope: the frequency-based cache. See lru_cache.py for the
#        recency-based one, which this builds on.
#
# A cache with a fixed capacity. When it is full and a new key
# arrives, the key that was used the FEWEST TIMES is thrown out. If
# several keys tie for fewest, the least recently used of them goes.
#
#     capacity 2
#     put(1, a)     1 used 1x
#     put(2, b)     2 used 1x
#     get(1)        1 used 2x
#     put(3, c)     full -> evict 2 (1x), although 1 is the OLDER key
#     get(3)        3 used 2x
#     put(4, d)     full -> 1 and 3 tie at 2x -> evict 1, used longer ago
#
# LRU vs LFU: LRU forgets a key that was popular an hour ago but idle
# since; LFU keeps it. LFU's weakness is the opposite -- a key that
# was hot long ago can sit in the cache forever.
#
# HOW get AND put STAY O(1)
#   Three pieces of state:
#
#     key_node     key   -> node (value + use count)       "find it"
#     count_node   count -> OrderedDict of the keys with   "who has this
#                           that count, oldest first        count, in
#                                                           recency order"
#     min_count    the smallest count currently present    "where to evict"
#
#     count_node:   1 -> {2: b}            <- min_count = 1: evict from here
#                   2 -> {1: a}
#
#   Using a key moves it from bucket `count` to bucket `count + 1`.
#   Evicting takes the OLDEST key of bucket `min_count`. Each bucket is
#   a small LRU list of its own, which is what breaks ties.
#
#   min_count never needs a search: it goes up by one only when its
#   bucket empties because of a use (the key just moved to the next
#   bucket), and it resets to 1 whenever a new key is inserted.
#
# Time  : get O(1), put O(1)
# Space : O(capacity)


from collections import OrderedDict


class Node:
    """One cache entry: its value and how many times it has been used."""

    def __init__(self, key, val, count):
        self.key = key
        self.val = val
        self.count = count


class LFUCache:
    def __init__(self, capacity):
        self.capacity = capacity
        self.key_node = {}            # key   -> Node
        self.count_node = {}          # count -> OrderedDict(key -> Node), oldest first
        self.min_count = None         # the lowest use count in the cache

    def _touch(self, node):
        """Record one more use: move the node from its bucket to the next one up."""
        old_count = node.count
        bucket = self.count_node[old_count]
        del bucket[node.key]
        if not bucket:                           # that bucket is now empty
            del self.count_node[old_count]
            if self.min_count == old_count:
                self.min_count += 1              # the node itself is moving to count + 1

        node.count += 1
        if node.count not in self.count_node:
            self.count_node[node.count] = OrderedDict()
        self.count_node[node.count][node.key] = node     # newest in its bucket

    def get(self, key):
        """Return the value for key (counting it as a use), or -1."""
        if key not in self.key_node:
            return -1
        node = self.key_node[key]
        self._touch(node)
        return node.val

    def put(self, key, value):
        """Insert or update key, evicting the least frequently used if full."""
        if self.capacity <= 0:
            return
        if key in self.key_node:                 # an update is also a use
            node = self.key_node[key]
            node.val = value
            self._touch(node)
            return

        if len(self.key_node) == self.capacity:
            # the lowest-count bucket, and within it the OLDEST key
            bucket = self.count_node[self.min_count]
            evicted_key, _ = bucket.popitem(last=False)
            if not bucket:
                del self.count_node[self.min_count]
            del self.key_node[evicted_key]

        node = Node(key, value, 1)
        self.key_node[key] = node
        if 1 not in self.count_node:
            self.count_node[1] = OrderedDict()
        self.count_node[1][key] = node
        self.min_count = 1                       # a brand-new key has the lowest count

    def counts(self):
        """{key: use count} -- for testing / debugging."""
        return {key: node.count for key, node in self.key_node.items()}


if __name__ == "__main__":
    # the trace from the header
    cache = LFUCache(2)
    cache.put(1, "a")
    cache.put(2, "b")
    assert cache.get(1) == "a"
    assert cache.counts() == {1: 2, 2: 1}
    cache.put(3, "c")                            # evicts 2: fewest uses
    assert cache.get(2) == -1
    assert cache.get(3) == "c"
    assert cache.counts() == {1: 2, 3: 2}
    cache.put(4, "d")                            # tie at 2 uses -> 1 was used longer ago
    assert cache.get(1) == -1
    assert cache.get(3) == "c" and cache.get(4) == "d"

    # LC 460's example
    lfu = LFUCache(2)
    lfu.put(1, 1)
    lfu.put(2, 2)
    assert lfu.get(1) == 1
    lfu.put(3, 3)                                # evicts key 2
    assert lfu.get(2) == -1
    assert lfu.get(3) == 3
    lfu.put(4, 4)                                # evicts key 1
    assert lfu.get(1) == -1
    assert lfu.get(3) == 3
    assert lfu.get(4) == 4

    # unlike LRU: the OLD but popular key survives, the newer one goes
    popular = LFUCache(2)
    popular.put("old", 1)
    popular.get("old")
    popular.get("old")
    popular.put("new", 2)
    popular.put("newer", 3)                      # evicts "new" (1 use), not "old" (3)
    assert popular.get("old") == 1 and popular.get("new") == -1

    # an update counts as a use and does not grow the cache
    update = LFUCache(2)
    update.put("k", 1)
    update.put("k", 2)
    assert update.get("k") == 2 and update.counts() == {"k": 3}

    # emptied buckets are removed, so min_count always names a real bucket
    assert all(len(bucket) > 0 for bucket in update.count_node.values())

    zero = LFUCache(0)
    zero.put("x", 1)
    assert zero.get("x") == -1

    # compare with a brute-force model: scan for (lowest count, oldest use)
    import random

    random.seed(1)
    fast = LFUCache(3)
    values, uses, last_used = {}, {}, {}
    for clock in range(3000):
        key = random.randrange(7)
        if random.random() < 0.5:
            expected = values.get(key, -1)
            assert fast.get(key) == expected
            if key in values:
                uses[key] += 1
                last_used[key] = clock
        else:
            value = random.randrange(100)
            fast.put(key, value)
            if key not in values and len(values) == 3:
                victim = min(values, key=lambda k: (uses[k], last_used[k]))
                del values[victim], uses[victim], last_used[victim]
            uses[key] = uses.get(key, 0) + 1
            values[key] = value
            last_used[key] = clock
        assert fast.counts() == uses

    print("Success.")
