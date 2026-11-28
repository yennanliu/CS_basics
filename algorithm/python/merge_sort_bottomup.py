#---------------------------------------------------------------
# MERGE SORT - BOTTOM UP (iterative)
#---------------------------------------------------------------
#
# Scope: merge sort with NO recursion. See merge_sort.py for the idea
#        and the merge step, merge_sort_topdown.py for the recursive
#        version.
#
# Top-down recursion splits the list down to single elements and then
# merges on the way back. Bottom-up skips the splitting: a list is
# ALREADY a row of sorted runs of length 1. Merge neighbouring runs
# pairwise, and the runs double in length each pass:
#
#     [5, 2, 9, 1, 6, 3, 8]
#
#     width 1:  [5][2] [9][1] [6][3] [8]
#            -> [2, 5] [1, 9] [3, 6] [8]         (8 has no partner yet)
#     width 2:  [2, 5][1, 9]   [3, 6][8]
#            -> [1, 2, 5, 9]   [3, 6, 8]
#     width 4:  [1, 2, 5, 9][3, 6, 8]
#            -> [1, 2, 3, 5, 6, 8, 9]
#
# For a pair starting at `lo` with run length `width`:
#     left  run = nums[lo        : lo + width]
#     right run = nums[lo + width : lo + 2 * width]
# and both ends are clamped to n, because the last run may be short
# or missing (the `min(..., n)` below).
#
# Why bother: no call stack, so no recursion limit -- this is the form
# to use for linked lists (LC 148 asks for O(1) extra space) and for
# sorting data too large for memory, one pass over the file per width.
#
# Time  : O(N log N) -- log N passes, each merging N elements
# Space : O(N) for the merged copies, O(1) call stack
#
# References:
#   - https://leetcode.com/problems/sort-an-array/discuss/568255/Python-Merge-Sort
#   - https://rust-algo.club/sorting/mergesort/


def merge_sort(nums):
    """Sort `nums` itself (ascending) and return it."""
    n = len(nums)
    width = 1                        # length of the sorted runs so far
    while width < n:
        # merge runs pairwise: [lo, mid) with [mid, hi)
        for lo in range(0, n, 2 * width):
            mid = min(lo + width, n)             # the left run may be the last, short one
            hi = min(lo + 2 * width, n)          # the right run may be short or empty
            nums[lo:hi] = _merge(nums[lo:mid], nums[mid:hi])
        width *= 2                   # every run is now twice as long
    return nums


def _merge(left, right):
    """Merge two SORTED lists into one new sorted list."""
    res = []
    i = 0
    j = 0
    while i < len(left) and j < len(right):
        if left[i] <= right[j]:      # `<=` keeps equal values in order (stable)
            res.append(left[i])
            i += 1
        else:
            res.append(right[j])
            j += 1
    res.extend(left[i:])
    res.extend(right[j:])
    return res


def passes(nums):
    """The list after each pass (width 1, 2, 4, ...) -- for the demo below."""
    nums = list(nums)
    n = len(nums)
    snapshots = []
    width = 1
    while width < n:
        for lo in range(0, n, 2 * width):
            mid = min(lo + width, n)
            hi = min(lo + 2 * width, n)
            nums[lo:hi] = _merge(nums[lo:mid], nums[mid:hi])
        snapshots.append(list(nums))
        width *= 2
    return snapshots


if __name__ == "__main__":
    assert merge_sort([5, 2, 9, 1, 5, 6]) == [1, 2, 5, 5, 6, 9]
    assert merge_sort([]) == [] and merge_sort([7]) == [7]

    # the three passes from the header (7 elements: the last run is short)
    assert passes([5, 2, 9, 1, 6, 3, 8]) == [
        [2, 5, 1, 9, 3, 6, 8],       # width 1 -> sorted runs of 2
        [1, 2, 5, 9, 3, 6, 8],       # width 2 -> sorted runs of 4
        [1, 2, 3, 5, 6, 8, 9],       # width 4 -> one run
    ]

    # stable: 1.0 and 1 are equal and keep their original order
    assert repr(merge_sort([1.0, 0, 1])) == "[0, 1.0, 1]"

    # no recursion: a long list is no deeper than a short one
    ordered = list(range(20000))
    assert merge_sort(ordered[::-1]) == ordered

    # every length from 0 to 40, so each "short last run" shape is hit
    import random

    random.seed(1)
    for length in range(41):
        data = [random.randint(-9, 9) for _ in range(length)]
        assert merge_sort(list(data)) == sorted(data)

    print("Success.")
