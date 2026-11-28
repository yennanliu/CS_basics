#---------------------------------------------------------------
# HASH SET (separate chaining, with resizing)
#---------------------------------------------------------------
#
# Scope: a set -- UNIQUE keys, no values -- built from scratch, and
#        the RESIZE step that hash_table.py leaves out on purpose.
#        See hash_table.py for the key -> value version and the
#        collision picture. In real code use Python's built-in `set`.
#
# A set answers one question fast: "have I seen this before?"
#
#     seen = HashSet()
#     seen.add(3); seen.add(3)       ->  holds 3 once
#     3 in seen                      ->  True, in O(1) on average
#
# It is a hash table that stores only the keys:
#
#     buckets[0] -> [8]
#     buckets[1] -> [1, 9]      <- 1 and 9 collide (both % 4 == 1), chained
#     buckets[2] -> []
#     buckets[3] -> [3]
#
# LOAD FACTOR AND RESIZING
#   load factor = items / buckets = the AVERAGE chain length.
#   Every operation scans one chain, so the cost is O(load factor).
#   Keep it under a constant and every operation is O(1) on average:
#   when it passes MAX_LOAD, double the bucket count and RE-INSERT
#   every key (a key's bucket depends on the bucket count, so each one
#   has to be hashed again -- this is "rehashing").
#
#   One resize is O(N), but doubling makes them rarer and rarer, so
#   N adds cost O(N) in total: O(1) AMORTISED each.
#
# Time  : add / remove / contains  O(1) average, O(N) worst
#                                  (worst = every key in one bucket)
# Space : O(N)
#
# Used by: LC 705 Design HashSet, and every "seen before?" check --
#          LC 217 Contains Duplicate, LC 128 Longest Consecutive Sequence.


class HashSet:
    """Set of hashable keys: separate chaining, doubling when it gets crowded."""

    MAX_LOAD = 0.75                  # resize once items / buckets exceeds this

    def __init__(self, keys=(), n_buckets=8):
        self.buckets = [[] for _ in range(n_buckets)]
        self._size = 0
        for key in keys:
            self.add(key)

    def __len__(self):
        return self._size

    def __iter__(self):
        """Every key, bucket by bucket. NOT insertion order, NOT sorted."""
        for bucket in self.buckets:
            for key in bucket:
                yield key

    def __contains__(self, key):
        """`key in s` -- hash to one bucket, then scan only that bucket."""
        return key in self._bucket(key)

    def _bucket(self, key):
        """The chain this key belongs to."""
        return self.buckets[hash(key) % len(self.buckets)]

    def load_factor(self):
        return self._size / len(self.buckets)

    def add(self, key):
        """Insert key. Returns False (and changes nothing) if already present."""
        bucket = self._bucket(key)
        if key in bucket:
            return False             # a set never stores a key twice
        bucket.append(key)
        self._size += 1
        if self.load_factor() > self.MAX_LOAD:
            self._resize(2 * len(self.buckets))
        return True

    def remove(self, key):
        """Delete key. Returns True if something was removed."""
        bucket = self._bucket(key)
        if key not in bucket:
            return False
        bucket.remove(key)
        self._size -= 1
        return True

    def _resize(self, n_buckets):
        """Rehash: every key moves to its bucket in a table of the new size."""
        old_buckets = self.buckets
        self.buckets = [[] for _ in range(n_buckets)]
        for bucket in old_buckets:
            for key in bucket:
                self._bucket(key).append(key)    # no duplicate check needed

    #--- set algebra ----------------------------------------------
    def union(self, other):
        """Keys in EITHER set."""
        result = HashSet(self)
        for key in other:
            result.add(key)
        return result

    def intersection(self, other):
        """Keys in BOTH sets: scan one, look each key up in the other."""
        result = HashSet()
        for key in self:
            if key in other:
                result.add(key)
        return result


if __name__ == "__main__":
    s = HashSet()
    assert s.add(3) is True
    assert s.add(3) is False             # already there -> not stored twice
    assert len(s) == 1
    assert 3 in s and 4 not in s

    assert s.remove(3) is True
    assert s.remove(3) is False
    assert len(s) == 0 and 3 not in s

    # the collision from the header: with 4 buckets, 1 and 9 share bucket 1
    crowded = HashSet(n_buckets=4)
    for key in [8, 1, 9]:
        crowded.add(key)                 # 3 / 4 = 0.75, not OVER the limit yet
    assert crowded.buckets == [[8], [1, 9], [], []]

    # a 4th key pushes the load factor to 1.0 -> doubled to 8 buckets and
    # rehashed. 5 used to share bucket 1 as well (5 % 4 == 1); with 8
    # buckets it gets its own (5 % 8 == 5), while 1 and 9 still collide --
    # a resize shortens chains on AVERAGE, it does not untangle every one
    crowded.add(5)
    assert len(crowded.buckets) == 8
    assert crowded.buckets[1] == [1, 9] and crowded.buckets[5] == [5]
    assert sorted(crowded) == [1, 5, 8, 9]

    # 1000 keys: the table keeps growing so the average chain stays short
    big = HashSet(range(1000))
    assert len(big) == 1000
    assert big.load_factor() <= HashSet.MAX_LOAD
    assert all(key in big for key in range(1000)) and 1000 not in big

    # duplicates collapse -- the classic use
    assert len(HashSet([1, 2, 2, 3, 3, 3])) == 3

    a, b = HashSet([1, 2, 3]), HashSet([2, 3, 4])
    assert sorted(a.union(b)) == [1, 2, 3, 4]
    assert sorted(a.intersection(b)) == [2, 3]

    print("Success.")
