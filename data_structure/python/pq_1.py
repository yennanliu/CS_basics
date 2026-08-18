#---------------------------------------------------------------
# PRIORITY QUEUE (1) -- the standard library options
#---------------------------------------------------------------
#
# Scope: the two batteries-included ways to get a priority queue in
#        Python, and when each is right. See pq_2.py for a class with
#        stable tie-breaking and updatable priorities, pq_3.py for the
#        naive list version that shows why heaps are worth it.
#
# A priority queue is a queue whose dequeue order is decided by a
# PRIORITY rather than by arrival time. Both options below are backed
# by a binary min-heap, so LOWEST priority number comes out first.
#
#   heapq                a set of functions operating on a plain list
#                        - fastest, no locking
#                        - what you want in an interview or single
#                          threaded code
#
#   queue.PriorityQueue  a class wrapping heapq with a mutex
#                        - safe to share across THREADS
#                        - blocking get()/put(), so it doubles as a
#                          producer/consumer channel
#                        - slower; pointless if you have one thread
#
# NOTE items must be comparable. Push `(priority, payload)` tuples and
# Python compares the priority first -- but if two priorities tie it
# goes on to compare the payloads, which blows up on non-comparable
# objects. pq_2.py shows the counter trick that fixes that.
#
# Time  : push / pop  O(log N),  peek O(1)
# Space : O(N)
#
# References:
#   - https://docs.python.org/3/library/heapq.html
#   - https://docs.python.org/3/library/queue.html


import heapq
import queue


class HeapqPriorityQueue:
    """Priority queue built directly on heapq (single-threaded)."""

    def __init__(self):
        self.items = []

    def __len__(self):
        return len(self.items)

    def is_empty(self):
        return not self.items

    def enqueue(self, item, priority):
        heapq.heappush(self.items, (priority, item))

    def dequeue(self):
        """Return the item with the LOWEST priority number."""
        if self.is_empty():
            raise IndexError("dequeue from an empty priority queue")
        return heapq.heappop(self.items)[1]

    def peek(self):
        if self.is_empty():
            raise IndexError("peek at an empty priority queue")
        return self.items[0][1]


class ThreadSafePriorityQueue:
    """Priority queue built on queue.PriorityQueue (locked, thread-safe)."""

    def __init__(self):
        self.items = queue.PriorityQueue()

    def __len__(self):
        return self.items.qsize()

    def is_empty(self):
        return self.items.empty()

    def enqueue(self, item, priority):
        self.items.put((priority, item))

    def dequeue(self):
        """Return the item with the LOWEST priority number.

        NOTE the `get_nowait()` rather than a `is_empty()` check followed
        by `get()`. Those two calls are not atomic: another consumer can
        take the last item in between, and `get()` BLOCKS by default, so
        this thread would wait forever on a queue it just saw as
        non-empty. Checking and taking in one locked operation is the
        whole reason to reach for queue.PriorityQueue in the first place.
        """
        try:
            return self.items.get_nowait()[1]
        except queue.Empty:
            raise IndexError("dequeue from an empty priority queue")


if __name__ == "__main__":
    #--- heapq version -------------------------------------------
    pq = HeapqPriorityQueue()
    pq.enqueue("write code", 2)
    pq.enqueue("fix outage", 1)          # urgent -> smallest number
    pq.enqueue("update docs", 3)

    assert len(pq) == 3
    assert pq.peek() == "fix outage"     # peek does not remove
    assert pq.dequeue() == "fix outage"
    assert pq.dequeue() == "write code"
    assert pq.dequeue() == "update docs"
    assert pq.is_empty()

    try:
        pq.dequeue()
        raise AssertionError("expected IndexError")
    except IndexError:
        pass

    #--- queue.PriorityQueue version -----------------------------
    tpq = ThreadSafePriorityQueue()
    tpq.enqueue("write code", 2)
    tpq.enqueue("fix outage", 1)
    assert tpq.dequeue() == "fix outage"
    assert tpq.dequeue() == "write code"
    assert tpq.is_empty()

    # losing the race must raise, not block forever: two consumers, one item
    import threading

    contended = ThreadSafePriorityQueue()
    contended.enqueue("only-item", 1)
    outcomes = []

    def consume():
        try:
            outcomes.append(contended.dequeue())
        except IndexError:
            outcomes.append("empty")

    threads = [threading.Thread(target=consume) for _ in range(2)]
    for thread in threads:
        thread.start()
    for thread in threads:
        thread.join(timeout=5)
        assert not thread.is_alive(), "dequeue blocked on an empty queue"
    assert sorted(outcomes) == ["empty", "only-item"]

    #--- the tie-breaking trap -----------------------------------
    # equal priorities make Python compare the payloads too
    ok = HeapqPriorityQueue()
    ok.enqueue("b", 1)
    ok.enqueue("a", 1)
    assert ok.dequeue() == "a"           # strings compare fine

    broken = HeapqPriorityQueue()
    broken.enqueue({"task": "b"}, 1)
    try:
        broken.enqueue({"task": "a"}, 1)  # dicts do not -> TypeError
        raise AssertionError("expected TypeError")
    except TypeError:
        pass                              # pq_2.py shows the fix

    print("Success.")
