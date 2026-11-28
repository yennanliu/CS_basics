#---------------------------------------------------------------
# INSERTION SORT
#---------------------------------------------------------------
#
# Grow a sorted region at the front of the list, one element at a
# time -- the way you sort a hand of cards. Take the next card (the
# `key`), slide every larger card one place right, and drop the key
# into the gap.
#
#     sorted | unsorted
#     [5]    | 2  4  1      key = 2:  5 slides right
#     [2, 5] | 4  1         key = 4:  5 slides right, 2 stays
#     [2, 4, 5] | 1         key = 1:  5, 4, 2 all slide right
#     [1, 2, 4, 5]
#
# The inner loop stops as soon as it meets something <= key. On input
# that is ALREADY nearly sorted it barely runs, which is the reason
# this is the quadratic sort that real libraries keep around: Timsort
# (Python's sort) and introsort use it for short runs.
#
# Equal values are never moved past each other (`key < arr[j]`, not
# `<=`), so the sort is STABLE.
#
# Time  : Best O(N) (already sorted), Avg/Worst O(N^2)
# Space : O(1), in place
#
# References:
#   - https://rust-algo.club/sorting/insertion_sort/index.html
#   - https://github.com/yennanliu/CS_basics/blob/master/doc/pic/insert_sort_1.jpg


# V0
def insertion_sort(arr):
    """Sort `arr` in place (ascending) and return it."""
    for i in range(1, len(arr)):
        key = arr[i]                 # the next value to place; arr[0..i-1] is sorted
        j = i - 1
        # slide everything larger than key one slot to the right
        while j >= 0 and key < arr[j]:
            arr[j + 1] = arr[j]
            j -= 1
        arr[j + 1] = key             # j stopped just LEFT of the gap
    return arr


def count_shifts(arr):
    """How many element moves insertion sort makes (for the demo below)."""
    arr = list(arr)
    shifts = 0
    for i in range(1, len(arr)):
        key = arr[i]
        j = i - 1
        while j >= 0 and key < arr[j]:
            arr[j + 1] = arr[j]
            j -= 1
            shifts += 1
        arr[j + 1] = key
    return shifts


if __name__ == "__main__":
    assert insertion_sort([5, 2, 4, 1]) == [1, 2, 4, 5]
    assert insertion_sort([12, 11, 13, 5, 6]) == [5, 6, 11, 12, 13]
    assert insertion_sort([]) == [] and insertion_sort([7]) == [7]

    # the cost IS the number of shifts, and that depends on the input order
    assert count_shifts([1, 2, 3, 4, 5]) == 0        # sorted: best case, O(N)
    assert count_shifts([1, 2, 4, 3, 5]) == 1        # nearly sorted: almost free
    assert count_shifts([5, 4, 3, 2, 1]) == 10       # reversed: N(N-1)/2, O(N^2)

    # stable: 1.0 and 1 are EQUAL, so they keep their original order
    assert repr(insertion_sort([1.0, 0, 1])) == "[0, 1.0, 1]"
    assert repr(insertion_sort([1, 0, 1.0])) == "[0, 1, 1.0]"

    # agree with the built-in sort on random input
    import random

    random.seed(1)
    for _ in range(200):
        data = [random.randint(-9, 9) for _ in range(random.randint(0, 12))]
        assert insertion_sort(list(data)) == sorted(data)

    print("Success.")
