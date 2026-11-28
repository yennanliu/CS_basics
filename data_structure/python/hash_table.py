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
#
# The slot count here is FIXED, so chains grow as the table fills. A
# real table (Python's dict, Java's HashMap) watches its LOAD FACTOR
# (entries / slots) and, past a threshold, allocates more slots and
# re-inserts everything -- that resize is what keeps "O(1) average" true.
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
        return str(dict(self.items()))

    #--- the two steps every operation starts with ----------------
    def _chain(self, key):
        """Step 1: hash the key and fold it into a slot; return that slot's chain."""
        index = self.hash_func(key) % self.n_slots
        return self.slots[index]

    @staticmethod
    def _position(chain, key):
        """Step 2: scan the chain for the key. Return its position, or -1.

        This compares the FULL key: two different keys can share a slot.
        """
        for i, (existing_key, _) in enumerate(chain):
            if existing_key == key:
                return i
        return -1

    #--- operations -----------------------------------------------
    def put(self, key, value):
        """Insert, or overwrite the value if the key is already present."""
        chain = self._chain(key)
        i = self._position(chain, key)
        if i >= 0:
            chain[i] = (key, value)          # overwrite, do NOT append a duplicate
        else:
            chain.append((key, value))
            self._size += 1

    def get(self, key, default=None):
        """Return the value for key, or `default` if absent."""
        chain = self._chain(key)
        i = self._position(chain, key)
        if i < 0:
            return default
        return chain[i][1]                   # the entry is (key, value)

    def remove(self, key):
        """Delete key. Returns True if something was removed."""
        chain = self._chain(key)
        i = self._position(chain, key)
        if i < 0:
            return False
        chain.pop(i)
        self._size -= 1
        return True

    def __contains__(self, key):
        """So that `key in table` works."""
        return self._position(self._chain(key), key) >= 0

    def items(self):
        """Every (key, value) pair, slot by slot -- O(n_slots + N)."""
        pairs = []
        for chain in self.slots:
            for pair in chain:
                pairs.append(pair)
        return pairs

    def keys(self):
        return [key for key, _ in self.items()]


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

    # a value of None is still a stored key: `in` and get() must not confuse them
    table.put("nothing", None)
    assert "nothing" in table and table.get("nothing", "default") is None

    # worst case made visible: a hash that sends EVERY key to slot 0
    crowded = HashTable(n_slots=4, hash_func=lambda key: 0)
    for i in range(5):
        crowded.put(i, i)
    assert len(crowded.slots[0]) == 5 and crowded.get(4) == 4

    print("Success.")
