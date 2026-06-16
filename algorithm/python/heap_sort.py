#---------------------------------------------------------------
# HEAP SORT
#---------------------------------------------------------------
#
# Build a max-heap, then repeatedly swap the root (the max) to the end
# and re-heapify the shrinking heap.
#
# Time  : O(N log N) (all cases)
# Space : O(1)
#
# References:
#   - https://rust-algo.club/sorting/heapsort/index.html


# V0
def heapify(arr, n, i):
    """Sift down arr[i] within the heap of size n (max-heap)."""
    largest = i
    left = 2 * i + 1
    right = 2 * i + 2

    if left < n and arr[left] > arr[largest]:
        largest = left
    if right < n and arr[right] > arr[largest]:
        largest = right

    if largest != i:
        arr[i], arr[largest] = arr[largest], arr[i]
        heapify(arr, n, largest)


def heap_sort(arr):
    n = len(arr)

    # build a max-heap (start from the last non-leaf node)
    for i in range(n // 2 - 1, -1, -1):
        heapify(arr, n, i)

    # repeatedly move the current max to the end, then re-heapify
    for i in range(n - 1, 0, -1):
        arr[0], arr[i] = arr[i], arr[0]
        heapify(arr, i, 0)
    return arr


if __name__ == "__main__":
    assert heap_sort([12, 11, 13, 5, 6, 7]) == [5, 6, 7, 11, 12, 13]
    assert heap_sort([]) == []
    print("Success.")
