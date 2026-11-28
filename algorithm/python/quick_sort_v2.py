#---------------------------------------------------------------
# QUICK SORT V2 -- Hoare partition
#---------------------------------------------------------------
#
# Scope: the OTHER classic partition scheme. See quick_sort.py for
#        the idea of quick sort and the Lomuto partition.
#
# Lomuto (quick_sort.py) sweeps one pointer left to right. Hoare's
# original scheme uses TWO pointers that start at the ends and walk
# towards each other, swapping whenever both are stuck on a value
# that belongs on the other side:
#
#     [7, 2, 4, 9, 1, 8]      pivot = 4 (the middle value)
#      i              j
#      i stops at 7 (not < pivot)     j walks left to 1 (not > pivot)
#     [1, 2, 4, 9, 7, 8]      swap them
#            i  j
#      i walks right to 4             j walks left to 4
#            ij               the pointers met -> stop, return j = 2
#
#     [1, 2, 4] | [9, 7, 8]   everything left of the split <= everything right
#
# TWO DIFFERENCES FROM LOMUTO, both of which trip people up:
#   1) the returned index is a SPLIT POINT, not the pivot's final
#      position. So the recursion is (lo, p) and (p + 1, hi) --
#      p is INCLUDED in the left half.
#   2) the pivot must not be the LAST element, or (lo, p) can be the
#      whole range again and the recursion never ends. Using the first
#      or the middle element is safe.
#
# Why bother: Hoare does about a third as many swaps as Lomuto, and
# it copes with many equal values, where Lomuto goes quadratic.
#
# Time  : Best/Avg O(N log N), Worst O(N^2)
# Space : O(log N) average (recursion)
#
# References:
#   - https://leetcode.com/explore/learn/card/recursion-ii/470/divide-and-conquer/2870/
#   - https://en.wikipedia.org/wiki/Quicksort#Hoare_partition_scheme


def quicksort(lst):
    """Sort lst in place (ascending) and return it."""
    qsort(lst, 0, len(lst) - 1)
    return lst


def qsort(lst, lo, hi):
    """Sort lst[lo..hi] in place."""
    if lo < hi:
        p = partition(lst, lo, hi)
        qsort(lst, lo, p)            # p is INCLUDED: it is a split point, not the pivot
        qsort(lst, p + 1, hi)


def partition(lst, lo, hi):
    """Hoare partition of lst[lo..hi].

    Returns j such that every value in lst[lo..j] is <= every value in
    lst[j+1..hi], with lo <= j < hi.
    """
    # the middle element: never the last one (see the header), and a
    # good pivot on input that is already sorted
    pivot = lst[lo + (hi - lo) // 2]
    i = lo - 1
    j = hi + 1
    while True:
        # move i right to the first value that belongs on the right side
        i += 1
        while lst[i] < pivot:
            i += 1
        # move j left to the first value that belongs on the left side
        j -= 1
        while lst[j] > pivot:
            j -= 1
        if i >= j:                   # the pointers met or crossed: done
            return j
        lst[i], lst[j] = lst[j], lst[i]


if __name__ == "__main__":
    assert quicksort([5, 2, 9, 1, 5, 6]) == [1, 2, 5, 5, 6, 9]
    assert quicksort([]) == [] and quicksort([7]) == [7]
    assert quicksort([2, 1]) == [1, 2]

    # what partition promises: a split point, both sides non-empty
    values = [7, 2, 4, 9, 1, 8]
    split = partition(values, 0, len(values) - 1)
    assert split == 2 and values == [1, 2, 4, 9, 7, 8]      # the header's trace
    assert max(values[:split + 1]) <= min(values[split + 1:])

    # sorted, reversed and all-equal input are all ordinary cases here
    ordered = list(range(5000))
    assert quicksort(list(ordered)) == ordered
    assert quicksort(ordered[::-1]) == ordered
    assert quicksort([4] * 5000) == [4] * 5000

    # agree with the built-in sort on random input
    import random

    random.seed(1)
    for _ in range(300):
        data = [random.randint(-9, 9) for _ in range(random.randint(0, 30))]
        assert quicksort(list(data)) == sorted(data)

    print("Success.")
