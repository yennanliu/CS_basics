#---------------------------------------------------------------
# QUICK SORT V2 (separate partition helper)
#---------------------------------------------------------------
#
# Same algorithm as quick_sort.py, written as quicksort -> qsort ->
# partition helpers (Lomuto scheme, pivot = last element). In place.
#
# Time  : Best/Avg O(N log N), Worst O(N^2)
# Space : O(log N) average (recursion)
#
# References:
#   - https://leetcode.com/explore/learn/card/recursion-ii/470/divide-and-conquer/2870/


def quicksort(lst):
    """Sort lst in place (ascending) and return it."""
    qsort(lst, 0, len(lst) - 1)
    return lst


def qsort(lst, lo, hi):
    if lo < hi:
        p = partition(lst, lo, hi)
        qsort(lst, lo, p - 1)
        qsort(lst, p + 1, hi)


def partition(lst, lo, hi):
    """Lomuto partition: pick lst[hi] as pivot, return its final index."""
    pivot = lst[hi]
    i = lo
    for j in range(lo, hi):
        if lst[j] < pivot:
            lst[i], lst[j] = lst[j], lst[i]
            i += 1
    lst[i], lst[hi] = lst[hi], lst[i]
    return i


if __name__ == "__main__":
    assert quicksort([5, 2, 9, 1, 5, 6]) == [1, 2, 5, 5, 6, 9]
    print("Success.")
