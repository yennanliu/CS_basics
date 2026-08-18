#---------------------------------------------------------------
# HASH TABLE (separate chaining)
#---------------------------------------------------------------
#
# A hash table buys O(1) lookup by SPENDING memory -- the classic
# time-space tradeoff.
#
#   1) hash the key to an integer                 hash(key)
#   2) fold it into a slot index                  hash(key) % n_slots
#   3) store (key, value) in that slot
#
# Two different keys can land in the same slot (a COLLISION). This
# implementation resolves collisions by SEPARATE CHAINING: every slot
# holds a list of entries, and lookup scans that short list.
#
#     slots[0] -> [(k1, v1)]
#     slots[1] -> []
#     slots[2] -> [(k2, v2), (k7, v7)]      <- collision, chained
#
# We must still compare the full key inside the chain: an equal hash
# does NOT mean an equal key.
#
# Time  : put / get / remove -> O(1) average, O(N) worst
#                               (worst = every key hashed to one slot)
# Space : O(N)
#
# References:
#   - http://zhaochj.github.io/2016/05/16/2016-05-16-数据结构-hash/


class HashTable:
    """Fixed-slot hash table using separate chaining."""

    def __init__(self, n_slots=8, hash_func=hash):
        self.n_slots = n_slots
        self.hash_func = hash_func
        self.slots = [[] for _ in range(n_slots)]
        self._size = 0

    def __len__(self):
        return self._size

    def __str__(self):
        return str({k: v for chain in self.slots for k, v in chain})

    def _index(self, key):
        """Map a key to a slot index."""
        return self.hash_func(key) % self.n_slots

    def put(self, key, value):
        """Insert, or overwrite the value if the key is already present."""
        chain = self.slots[self._index(key)]
        for i, (existing_key, _) in enumerate(chain):
            if existing_key == key:
                chain[i] = (key, value)      # overwrite, do NOT append a duplicate
                return
        chain.append((key, value))
        self._size += 1

    def get(self, key, default=None):
        """Return the value for key, or `default` if absent."""
        for existing_key, value in self.slots[self._index(key)]:
            if existing_key == key:
                return value
        return default

    def remove(self, key):
        """Delete key. Returns True if something was removed."""
        chain = self.slots[self._index(key)]
        for i, (existing_key, _) in enumerate(chain):
            if existing_key == key:
                chain.pop(i)
                self._size -= 1
                return True
        return False

    def keys(self):
        return [k for chain in self.slots for k, _ in chain]

    # so `key in table` works
    def __contains__(self, key):
        return any(k == key for k, _ in self.slots[self._index(key)])


if __name__ == "__main__":
    table = HashTable(n_slots=4)

    table.put("a", 1)
    table.put("b", 2)
    table.put("c", 3)
    assert len(table) == 3
    assert table.get("b") == 2
    assert "b" in table

    # overwriting an existing key does not grow the table
    table.put("b", 20)
    assert table.get("b") == 20
    assert len(table) == 3

    # missing keys fall back to the default
    assert table.get("zzz") is None
    assert table.get("zzz", "not set") == "not set"

    assert table.remove("b") is True
    assert table.remove("b") is False
    assert "b" not in table
    assert len(table) == 2
    assert sorted(table.keys()) == ["a", "c"]

    # 4 slots but 20 keys -> chains get long, yet everything still resolves
    big = HashTable(n_slots=4)
    for i in range(20):
        big.put(i, i * i)
    assert all(big.get(i) == i * i for i in range(20))
    assert len(big) == 20

    print("Success.")
