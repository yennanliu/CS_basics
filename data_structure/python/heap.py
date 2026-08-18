#---------------------------------------------------------------
# HEAP -- concepts, and Python's built-in heapq
#---------------------------------------------------------------
#
# Scope: what a binary heap IS, and how to use the standard library
#        (`heapq`) -- which is what you should reach for in real code
#        and in interviews. For the from-scratch array implementation
#        with sift-up/sift-down written out, see MinHeap.py / MaxHeap.py.
#
# A binary heap is a COMPLETE binary tree obeying the heap property:
#
#     MIN-heap: every parent <= both of its children  (root = smallest)
#     MAX-heap: every parent >= both of its children  (root = largest)
#
#             1                  index   0  1  2  3  4
#           /   \                value   1  3  6  5  9
#          3     6
#         / \                    left  child of i : 2i + 1
#        5   9                   right child of i : 2i + 2
#                                parent      of i : (i-1) // 2
#
# Because the tree is complete it needs no pointers -- it lives in a
# flat array, and the index arithmetic above IS the tree.
#
# NOTE the heap property is only a PARENT/CHILD rule. Siblings are
# unordered, so a heap is NOT a sorted array; it only guarantees a
# cheap minimum (or maximum) at the root.
#
# NOTE Python's heapq is a MIN-heap. For a max-heap, push negated
# values (see max_heap.py).
#
# Time  : peek        O(1)
#         push / pop  O(log N)
#         heapify a whole list  O(N)   -- not O(N log N)
#         nlargest / nsmallest  O(N log k)
# Space : O(N)
#
# References:
#   - https://docs.python.org/3/library/heapq.html


import heapq


def heap_sort(nums):
    """Sort by draining a heap. See algorithm/python/heap_sort.py for the
    in-place O(1)-extra-space version."""
    heap = list(nums)
    heapq.heapify(heap)                       # O(N), not N pushes
    return [heapq.heappop(heap) for _ in range(len(heap))]


def k_smallest(nums, k):
    """The k smallest values, in O(N log k) time and O(k) space.

    The trick: keep a MAX-heap of size k (negated, since heapq is a
    min-heap). Whatever sits on top is the worst of the current best k,
    so it is the one to evict.
    """
    heap = []
    for num in nums:
        heapq.heappush(heap, -num)
        if len(heap) > k:
            heapq.heappop(heap)               # drop the largest so far
    return sorted(-x for x in heap)


def merge_sorted_lists(*lists):
    """Merge k sorted lists in O(N log k) -- the LC 23 pattern."""
    return list(heapq.merge(*lists))


if __name__ == "__main__":
    #--- the four operations ------------------------------------
    heap = []
    for value in [9, 5, 6, 2, 3]:
        heapq.heappush(heap, value)

    assert heap[0] == 2                       # peek: the root is the minimum
    assert heapq.heappop(heap) == 2           # pop always yields the smallest
    assert heapq.heappop(heap) == 3
    heapq.heappush(heap, 1)
    assert heapq.heappop(heap) == 1

    #--- heapify: O(N), and it mutates the list in place ---------
    nums = [9, 5, 6, 2, 3]
    heapq.heapify(nums)
    assert nums[0] == 2                       # root is the min...
    assert sorted(nums) == [2, 3, 5, 6, 9]    # ...but the rest is NOT sorted

    assert heap_sort([9, 5, 6, 2, 3]) == [2, 3, 5, 6, 9]

    #--- max-heap by negation ------------------------------------
    max_heap = []
    for value in [9, 5, 6, 2, 3]:
        heapq.heappush(max_heap, -value)
    assert -heapq.heappop(max_heap) == 9      # largest first

    #--- tuples: heapq orders by the FIRST element ---------------
    tasks = []
    heapq.heappush(tasks, (2, "write"))
    heapq.heappush(tasks, (1, "read"))
    heapq.heappush(tasks, (3, "ship"))
    assert heapq.heappop(tasks) == (1, "read")

    #--- top-k ---------------------------------------------------
    assert k_smallest([9, 5, 6, 2, 3, 8, 1], 3) == [1, 2, 3]
    assert heapq.nsmallest(3, [9, 5, 6, 2, 3, 8, 1]) == [1, 2, 3]
    assert heapq.nlargest(3, [9, 5, 6, 2, 3, 8, 1]) == [9, 8, 6]

    assert merge_sorted_lists([1, 4, 7], [2, 5], [3, 6]) == [1, 2, 3, 4, 5, 6, 7]

    print("Success.")
