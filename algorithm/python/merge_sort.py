#---------------------------------------------------------------
# MERGE SORT
#---------------------------------------------------------------
#
# Scope: the idea, the merge step, and the two common ways to write
#        the recursion. See merge_sort_topdown.py for a step-by-step
#        trace of the recursion and merge_sort_bottomup.py for the
#        version with no recursion at all.
#
# Divide and conquer:
#   1) split the list in half
#   2) sort each half (by doing the same thing again)
#   3) MERGE the two sorted halves into one sorted list
#
#     [5, 2, 4, 1]
#       /        \
#    [5, 2]     [4, 1]            split until one element is left
#    /   \      /   \
#  [5]   [2]  [4]   [1]           a single element is already sorted
#    \   /      \   /
#    [2, 5]     [1, 4]            merge on the way back up
#       \        /
#     [1, 2, 4, 5]
#
# THE MERGE STEP is the whole algorithm. Two sorted lists, one pointer
# into each; repeatedly take the smaller front value:
#
#     left = [2, 5]   right = [1, 4]
#             i                j          1 < 2  -> take 1
#             i                   j       2 < 4  -> take 2
#                i                j       4 < 5  -> take 4
#                i                  (right is empty) -> take the rest: 5
#
# All the work happens AFTER the recursion (quick sort is the mirror
# image: it partitions first, then recurses).
#
# STABLE -- but only if a tie takes from the LEFT list (`<=` below).
# Writing `<` instead still sorts correctly and silently loses
# stability, which is the most common merge sort bug.
#
# Time  : O(N log N) in EVERY case: log N levels, O(N) merging per level
# Space : O(N) for the merged copies, + O(log N) recursion stack
#
# Merge sort vs quick sort: merge sort is stable and has no bad input,
# but needs O(N) extra memory; quick sort is in place and usually
# faster, but is unstable and O(N^2) in the worst case.
#
# Used by: LC 912 Sort an Array, LC 148 Sort List, LC 23 Merge k
#          Sorted Lists, LC 315 / LC 493 (counting inversions).
#
# References:
#   - https://www.geeksforgeeks.org/merge-sort/


def merge(left, right):
    """Merge two SORTED lists into one new sorted list."""
    merged = []
    i = 0                            # next unused position in left
    j = 0                            # next unused position in right
    while i < len(left) and j < len(right):
        if left[i] <= right[j]:      # `<=`: on a tie the LEFT value goes first -> stable
            merged.append(left[i])
            i += 1
        else:
            merged.append(right[j])
            j += 1
    # one list ran out; whatever is left in the other is already sorted
    merged.extend(left[i:])
    merged.extend(right[j:])
    return merged


# V0 : returns a new sorted list -- the clearest form
def merge_sort_functional(collection):
    """Return a new sorted list; the input is left untouched."""
    if len(collection) <= 1:         # base case: nothing to sort
        return list(collection)
    mid = len(collection) // 2
    left = merge_sort_functional(collection[:mid])
    right = merge_sort_functional(collection[mid:])
    return merge(left, right)


# V1 : sorts the list it was given, writing the merge straight back
# into it. Still O(N) extra space (the two halves are copies) -- merge
# sort on an array cannot avoid that.
def merge_sort(arr):
    """Sort `arr` itself (ascending) and return it."""
    if len(arr) <= 1:
        return arr

    mid = len(arr) // 2
    left = arr[:mid]                 # copies of the two halves
    right = arr[mid:]
    merge_sort(left)
    merge_sort(right)

    # merge the sorted halves back into arr, position by position
    i = 0                            # next unused position in left
    j = 0                            # next unused position in right
    k = 0                            # next position to fill in arr
    while i < len(left) and j < len(right):
        if left[i] <= right[j]:
            arr[k] = left[i]
            i += 1
        else:
            arr[k] = right[j]
            j += 1
        k += 1
    while i < len(left):             # copy whatever is left over
        arr[k] = left[i]
        i += 1
        k += 1
    while j < len(right):
        arr[k] = right[j]
        j += 1
        k += 1
    return arr


if __name__ == "__main__":
    # the merge step on its own
    assert merge([2, 5], [1, 4]) == [1, 2, 4, 5]
    assert merge([], [1, 2]) == [1, 2] and merge([1, 2], []) == [1, 2]
    assert merge([1, 2], [3, 4]) == [1, 2, 3, 4]         # no interleaving at all

    assert merge_sort([5, 2, 4, 1]) == [1, 2, 4, 5]
    assert merge_sort([12, 11, 13, 5, 6, 7]) == [5, 6, 7, 11, 12, 13]
    assert merge_sort_functional([0, 5, 3, 2, 2]) == [0, 2, 2, 3, 5]
    for sort in (merge_sort, merge_sort_functional):
        assert sort([]) == [] and sort([7]) == [7]

    original = [3, 1, 2]
    assert merge_sort_functional(original) == [1, 2, 3] and original == [3, 1, 2]
    assert merge_sort(original) is original and original == [1, 2, 3]

    # stable: 1.0 and 1 are EQUAL, so they must keep their original order.
    # With `<` in the merge this prints [0, 1, 1.0] -- sorted, but not stable.
    assert repr(merge_sort([1.0, 0, 1])) == "[0, 1.0, 1]"
    assert repr(merge_sort_functional([1.0, 0, 1])) == "[0, 1.0, 1]"
    assert repr(merge([1.0], [1])) == "[1.0, 1]"

    # agree with the built-in sort (also stable) on random input
    import random

    random.seed(1)
    for _ in range(200):
        data = [random.randint(-9, 9) for _ in range(random.randint(0, 30))]
        assert merge_sort(list(data)) == sorted(data)
        assert merge_sort_functional(data) == sorted(data)

    print("Success.")
