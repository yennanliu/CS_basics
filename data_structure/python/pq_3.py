#---------------------------------------------------------------
# PRIORITY QUEUE (3) -- the naive unsorted-list version
#---------------------------------------------------------------
#
# Scope: the O(N)-dequeue implementation, kept as the BASELINE that
#        explains why a heap is worth the extra code. See pq_1.py for
#        the stdlib heap versions and pq_2.py for stable ordering and
#        updatable priorities.
#
# Store items in an unsorted list. Insert is trivially O(1); the cost
# moves entirely into dequeue, which must scan the whole list to find
# the maximum:
#
#     [12, 1, 14, 7]   ->  scan all 4  ->  pop 14
#     [12, 1, 7]       ->  scan all 3  ->  pop 12
#
# THE THREE DESIGNS, side by side:
#
#   backing store      insert      pop-max     when it wins
#   ---------------    --------    --------    ---------------------
#   unsorted list      O(1)        O(N)        few pops, many inserts
#   sorted list        O(N)        O(1)        many pops, few inserts
#   binary heap        O(log N)    O(log N)    balanced -- the default
#
# Draining N items costs O(N^2) here versus O(N log N) with a heap,
# which is the whole reason heapq exists.
#
# Time  : insert O(1), delete O(N), peek O(N)
# Space : O(N)
#
# References:
#   - https://www.geeksforgeeks.org/priority-queue-in-python/


class PriorityQueue:
    """Max-priority queue backed by an unsorted list."""

    def __init__(self, items=()):
        self.queue = list(items)

    def __len__(self):
        return len(self.queue)

    def __str__(self):
        return " ".join(str(i) for i in self.queue)

    def is_empty(self):
        return not self.queue

    def insert(self, data):
        """O(1) -- just append, order does not matter."""
        self.queue.append(data)

    def _max_index(self):
        """Scan for the position of the largest item. This is the O(N) part."""
        best = 0
        for i in range(1, len(self.queue)):
            if self.queue[i] > self.queue[best]:
                best = i
        return best

    def delete(self):
        """Remove and return the LARGEST item."""
        if self.is_empty():
            raise IndexError("delete from an empty priority queue")
        return self.queue.pop(self._max_index())

    def peek(self):
        """Return the largest item WITHOUT removing it."""
        if self.is_empty():
            raise IndexError("peek at an empty priority queue")
        return self.queue[self._max_index()]


if __name__ == "__main__":
    pq = PriorityQueue()
    assert pq.is_empty()

    for value in [12, 1, 14, 7]:
        pq.insert(value)
    assert str(pq) == "12 1 14 7"        # insertion order -- the list is UNSORTED
    assert len(pq) == 4

    assert pq.peek() == 14               # peek does not remove
    assert len(pq) == 4

    # draining yields descending order, one O(N) scan at a time
    assert [pq.delete() for _ in range(4)] == [14, 12, 7, 1]
    assert pq.is_empty()

    try:
        pq.delete()
        raise AssertionError("expected IndexError")
    except IndexError:
        pass

    # tuples work too: Python compares element by element
    tasks = PriorityQueue([(2, "write"), (1, "read"), (3, "ship")])
    assert tasks.delete() == (3, "ship")

    print("Success.")
