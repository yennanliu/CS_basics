#---------------------------------------------------------------
# QUICK SORT V1
#---------------------------------------------------------------

# V0
# https://www.bilibili.com/video/BV1at411T75o/
# https://hackmd.io/@Aquamay/H1nxBOLcO/https%3A%2F%2Fhackmd.io%2F%40Aquamay%2FB1SPnfRq_
# https://www.baeldung.com/cs/quicksort-time-complexity-worst-case
#
# Steps
# Step 1) find pivot (can be any idx, we choose idx 0 for simplicity)
# Step 2) move element < pivot to left sub array, move element > pivot to right sub array
# Step 3) repeat above to all sub array
def quick_sort(sorting: list, left: int, right: int) -> list:
    if right <= left:
        return
    a = i = left
    b = right
    pivot = sorting[left]
    while i <= b:
        if sorting[i] < pivot:
            sorting[a], sorting[i] = sorting[i], sorting[a]
            a += 1
            i += 1
        elif sorting[i] > pivot:
            sorting[b], sorting[i] = sorting[i], sorting[b]
            b -= 1
        else:
            i += 1
    quick_sort(sorting, left, a - 1)
    quick_sort(sorting, b + 1, right)

    return sorting


# V1
# steps
# 0) get pivot (last element from original array)
# 1) init big, small sub array
# 2) loop over element 
#      ->  put "< pivot" elements to small sub array, 
#      ->  put "> pivot" elements to small big array
# 3) run same algorithm on sub array, big array
# 4) return result
def quick_sort(arr):
    # edge case
    if len(arr) < 2:
        return arr
    # use last element as first pivot
    pivot = arr.pop(-1)
    # init small, big array
    small = []
    big = []
    for i in arr:
        if i > pivot:
            big.append(i)
        else:
            small.append(i)
    # recursive do quick_sort
    return quick_sort(small) + [pivot] + quick_sort(big)


# V1
# https://github.com/yennanliu/Python/blob/master/sorts/quick_sort.py
from __future__ import annotations

def quick_sort(collection: list) -> list:
    """
    A pure Python implementation of quick sort algorithm
    :param collection: a mutable collection of comparable items
    :return: the same collection ordered by ascending
    Examples:
    >>> quick_sort([0, 5, 3, 2, 2])
    [0, 2, 2, 3, 5]
    >>> quick_sort([])
    []
    >>> quick_sort([-2, 5, 0, -45])
    [-45, -2, 0, 5]
    """
    if len(collection) < 2:
        return collection
    pivot = collection.pop()  # Use the last element as the first pivot
    greater: list[int] = []  # All elements greater than pivot
    lesser: list[int] = []  # All elements less than or equal to pivot
    for element in collection:
        (greater if element > pivot else lesser).append(element)
    return quick_sort(lesser) + [pivot] + quick_sort(greater)


# if __name__ == "__main__":
#     user_input = input("Enter numbers separated by a comma:\n").strip()
#     unsorted = [int(item) for item in user_input.split(",")]
#     print(quick_sort(unsorted))

# V1'
# https://github.com/yennanliu/Python/blob/master/sorts/recursive_quick_sort.py
# IDEA : recursive quick sort
def quick_sort(data: list) -> list:
    """
    >>> for data in ([2, 1, 0], [2.2, 1.1, 0], "quick_sort"):
    ...     quick_sort(data) == sorted(data)
    True
    True
    True
    """
    if len(data) <= 1:
        return data
    else:
        return (
            quick_sort([e for e in data[1:] if e <= data[0]])
            + [data[0]]
            + quick_sort([e for e in data[1:] if e > data[0]])
        )


# V1''
# https://github.com/yennanliu/Python/blob/master/sorts/quick_sort_3_partition.py
# IDEA : quick sort partition
def quick_sort_3partition(sorting: list, left: int, right: int) -> None:
    if right <= left:
        return
    a = i = left
    b = right
    pivot = sorting[left]
    while i <= b:
        if sorting[i] < pivot:
            sorting[a], sorting[i] = sorting[i], sorting[a]
            a += 1
            i += 1
        elif sorting[i] > pivot:
            sorting[b], sorting[i] = sorting[i], sorting[b]
            b -= 1
        else:
            i += 1
    quick_sort_3partition(sorting, left, a - 1)
    quick_sort_3partition(sorting, b + 1, right)


def quick_sort_lomuto_partition(sorting: list, left: int, right: int) -> None:
    """
    A pure Python implementation of quick sort algorithm(in-place)
    with Lomuto partition scheme:
    https://en.wikipedia.org/wiki/Quicksort#Lomuto_partition_scheme
    :param sorting: sort list
    :param left: left endpoint of sorting
    :param right: right endpoint of sorting
    :return: None
    Examples:
    >>> nums1 = [0, 5, 3, 1, 2]
    >>> quick_sort_lomuto_partition(nums1, 0, 4)
    >>> nums1
    [0, 1, 2, 3, 5]
    >>> nums2 = []
    >>> quick_sort_lomuto_partition(nums2, 0, 0)
    >>> nums2
    []
    >>> nums3 = [-2, 5, 0, -4]
    >>> quick_sort_lomuto_partition(nums3, 0, 3)
    >>> nums3
    [-4, -2, 0, 5]
    """
    if left < right:
        pivot_index = lomuto_partition(sorting, left, right)
        quick_sort_lomuto_partition(sorting, left, pivot_index - 1)
        quick_sort_lomuto_partition(sorting, pivot_index + 1, right)


def lomuto_partition(sorting: list, left: int, right: int) -> int:
    """
    Example:
    >>> lomuto_partition([1,5,7,6], 0, 3)
    2
    """
    pivot = sorting[right]
    store_index = left
    for i in range(left, right):
        if sorting[i] < pivot:
            sorting[store_index], sorting[i] = sorting[i], sorting[store_index]
            store_index += 1
    sorting[right], sorting[store_index] = sorting[store_index], sorting[right]
    return store_index


def three_way_radix_quicksort(sorting: list) -> list:
    """
    Three-way radix quicksort:
    https://en.wikipedia.org/wiki/Quicksort#Three-way_radix_quicksort
    First divide the list into three parts.
    Then recursively sort the "less than" and "greater than" partitions.
    >>> three_way_radix_quicksort([])
    []
    >>> three_way_radix_quicksort([1])
    [1]
    >>> three_way_radix_quicksort([-5, -2, 1, -2, 0, 1])
    [-5, -2, -2, 0, 1, 1]
    >>> three_way_radix_quicksort([1, 2, 5, 1, 2, 0, 0, 5, 2, -1])
    [-1, 0, 0, 1, 1, 2, 2, 2, 5, 5]
    """
    if len(sorting) <= 1:
        return sorting
    return (
        three_way_radix_quicksort([i for i in sorting if i < sorting[0]])
        + [i for i in sorting if i == sorting[0]]
        + three_way_radix_quicksort([i for i in sorting if i > sorting[0]])
    )
