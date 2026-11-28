#---------------------------------------------------------------
# HEAP SORT
#---------------------------------------------------------------
#
# Scope: sorting an array IN PLACE with a heap. See
#        data_structure/python/heap.py for what a heap is, and
#        data_structure/python/MaxHeap.py for push / pop.
#
# A MAX-heap keeps its largest value at the root (index 0). So:
#
#   1) BUILD: rearrange the array into a max-heap
#   2) SORT:  swap the root (the max) with the last element -- the
#             max is now in its final place. Shrink the heap by one
#             and sift the new root down. Repeat.
#
#     build     [9, 6, 8, 2, 5]            9 is the root
#     swap      [5, 6, 8, 2 | 9]           9 is home; heap is the first 4
#     sift down [8, 6, 5, 2 | 9]
#     swap      [2, 6, 5 | 8, 9]
#     sift down [6, 2, 5 | 8, 9]
#     swap      [5, 2 | 6, 8, 9]
#     swap      [2 | 5, 6, 8, 9]           sorted
#
# The heap lives in the same array, with no pointers:
#     left child of i : 2i + 1       right child of i : 2i + 2
#
# The sorted part grows from the BACK, which is why a max-heap is used
# to sort ascending.
#
# Time  : O(N log N) in EVERY case -- build is O(N), then N sift-downs
#         of O(log N) each. No bad input, unlike quick sort.
# Space : O(1) -- in place, unlike merge sort
#
# NOT stable. In practice slower than quick sort (it jumps around the
# array, which is bad for the CPU cache), so it is the fallback that
# guarantees O(N log N), not the first choice.
#
# References:
#   - https://rust-algo.club/sorting/heapsort/index.html


# V0
def heapify(arr, n, i):
    """Sift arr[i] DOWN until the subtree under i is a max-heap again.

    Only arr[0..n-1] is part of the heap; anything from n onwards is the
    already-sorted tail and must not be touched.
    """
    while True:
        left = 2 * i + 1
        right = 2 * i + 2

        # which is largest: the node, or one of its children?
        largest = i
        if left < n and arr[left] > arr[largest]:
            largest = left
        if right < n and arr[right] > arr[largest]:
            largest = right

        if largest == i:             # the node beats both children: done
            return
        arr[i], arr[largest] = arr[largest], arr[i]
        i = largest                  # follow the value down one level


def build_max_heap(arr):
    """Rearrange arr into a max-heap, in O(N).

    Start at the LAST node that has a child (index n // 2 - 1) and work
    back to the root: by the time a node is sifted down, both of its
    subtrees are already heaps. The leaves need no work at all.
    """
    n = len(arr)
    for i in range(n // 2 - 1, -1, -1):
        heapify(arr, n, i)


def heap_sort(arr):
    """Sort `arr` in place (ascending) and return it."""
    build_max_heap(arr)

    # arr[0..end] is the heap, arr[end+1..] is sorted and final
    for end in range(len(arr) - 1, 0, -1):
        arr[0], arr[end] = arr[end], arr[0]      # the max goes to its final slot
        heapify(arr, end, 0)                     # the heap is now one shorter
    return arr


def is_max_heap(arr):
    """True if every parent is >= both of its children."""
    for i in range(1, len(arr)):
        parent = (i - 1) // 2
        if arr[parent] < arr[i]:
            return False
    return True


if __name__ == "__main__":
    assert heap_sort([9, 6, 8, 2, 5]) == [2, 5, 6, 8, 9]
    assert heap_sort([12, 11, 13, 5, 6, 7]) == [5, 6, 7, 11, 12, 13]
    assert heap_sort([]) == [] and heap_sort([7]) == [7]

    # step 1 on its own: a heap is NOT sorted, it only has the max on top
    heap = [2, 5, 6, 8, 9]
    build_max_heap(heap)
    assert heap[0] == 9 and is_max_heap(heap)
    assert heap != sorted(heap, reverse=True)

    # 5000 sorted values: no recursion, and no bad case
    ordered = list(range(5000))
    assert heap_sort(list(ordered)) == ordered
    assert heap_sort(ordered[::-1]) == ordered

    # agree with the built-in sort on random input
    import random

    random.seed(1)
    for _ in range(200):
        data = [random.randint(-9, 9) for _ in range(random.randint(0, 30))]
        assert heap_sort(list(data)) == sorted(data)

    print("Success.")
