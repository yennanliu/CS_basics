#---------------------------------------------------------------
# BINARY SEARCH
#---------------------------------------------------------------
#
# Find a value in a SORTED array by repeatedly halving the range that
# could still contain it.
#
#     target = 10
#
#     index    0   1   2   3   4   5   6
#     value    0   5   7  10  15  20  30
#              lo          mid         hi      10 == 10 -> found at 3
#
#     target = 6
#     [ 0   5   7  10  15  20  30 ]    mid=3 (10)  6 < 10 -> go left
#     [ 0   5   7 ]                    mid=1 (5)   6 > 5  -> go right
#             [ 7 ]                    mid=2 (7)   6 < 7  -> go left
#             (empty: lo > hi)                     not found
#
# Every step throws away HALF of what is left, so N elements take at
# most log2(N) + 1 steps: 1,000,000 elements -> 20 comparisons.
#
# THE THREE THINGS PEOPLE GET WRONG
#   1) the loop condition: `lo <= hi` when hi is the LAST valid index
#      (a one-element range, lo == hi, still has to be checked)
#   2) the update: `mid + 1` / `mid - 1`, never `mid` -- otherwise the
#      range can stop shrinking and the loop never ends
#   3) what to return when the value is absent or duplicated: that is
#      what lower_bound / upper_bound below are for
#
# Time  : Best O(1), Avg/Worst O(log N)
# Space : iterative O(1), recursive O(log N) (call stack)
#
# See also: doc/cheatsheet/binary_search.md for the LeetCode patterns,
#           patience_sorting.py for a lower bound doing real work.
#
# References:
#   - https://docs.python.org/3/library/bisect.html
#   - https://www.geeksforgeeks.org/complexity-analysis-of-binary-search/


# V0 : iterative -- the one to write in an interview
def binary_search(array, target):
    """Return an index of `target` in the sorted `array`, or None if absent."""
    lo = 0
    hi = len(array) - 1              # hi is the LAST index still in play
    while lo <= hi:
        # `lo + (hi - lo) // 2` is the overflow-safe spelling of
        # `(lo + hi) // 2`. Python integers never overflow, but Java / C++
        # ints do, so this is the habit worth keeping.
        mid = lo + (hi - lo) // 2
        if array[mid] == target:
            return mid
        if array[mid] < target:
            lo = mid + 1             # target can only be to the RIGHT of mid
        else:
            hi = mid - 1             # target can only be to the LEFT of mid
    return None                      # the range emptied out: not there


# V1 : recursive -- same halving, the range is passed down instead of looped
def binary_search_recursive(array, target, lo=0, hi=None):
    """Return an index of `target` in array[lo..hi], or None if absent."""
    if hi is None:
        hi = len(array) - 1
    if lo > hi:                      # base case: empty range
        return None
    mid = lo + (hi - lo) // 2
    if array[mid] == target:
        return mid
    if array[mid] < target:
        return binary_search_recursive(array, target, mid + 1, hi)
    return binary_search_recursive(array, target, lo, mid - 1)


# V2 : lower bound -- the FIRST index whose value is >= target
#
# This is the version most real problems need. It answers three
# questions at once:
#   - where is the first copy of `target`?       (when duplicates exist)
#   - where would `target` be inserted?          (when it is absent)
#   - how many values are smaller than `target`? (the index itself)
#
# The range is half-open here: hi is ONE PAST the last candidate, so
# the loop is `lo < hi` and the answer may be len(array).
def lower_bound(array, target):
    """First index i with array[i] >= target (len(array) if there is none)."""
    lo = 0
    hi = len(array)
    while lo < hi:
        mid = lo + (hi - lo) // 2
        if array[mid] < target:
            lo = mid + 1             # mid is too small: the answer is right of it
        else:
            hi = mid                 # mid might BE the answer: keep it in range
    return lo


# V3 : upper bound -- the first index whose value is > target
def upper_bound(array, target):
    """First index i with array[i] > target (len(array) if there is none)."""
    lo = 0
    hi = len(array)
    while lo < hi:
        mid = lo + (hi - lo) // 2
        if array[mid] <= target:     # the only change from lower_bound: <=
            lo = mid + 1
        else:
            hi = mid
    return lo


def count_occurrences(array, target):
    """How many times `target` appears: the gap between the two bounds."""
    return upper_bound(array, target) - lower_bound(array, target)


if __name__ == "__main__":
    arr = [0, 5, 7, 10, 15, 20, 30]
    assert binary_search(arr, 10) == 3
    assert binary_search(arr, 0) == 0            # first element
    assert binary_search(arr, 30) == 6           # last element
    assert binary_search(arr, 6) is None         # absent, inside the range
    assert binary_search(arr, 99) is None        # absent, past the end
    assert binary_search([], 1) is None
    assert binary_search([4], 4) == 0            # the one-element range

    assert binary_search_recursive(arr, 15) == 4
    assert binary_search_recursive(arr, 6) is None

    # duplicates: binary_search may land on any copy; the bounds are exact
    dup = [1, 2, 2, 2, 3, 5]
    assert dup[binary_search(dup, 2)] == 2
    assert lower_bound(dup, 2) == 1              # first 2
    assert upper_bound(dup, 2) == 4              # just past the last 2
    assert count_occurrences(dup, 2) == 3
    assert count_occurrences(dup, 4) == 0

    # an absent value: both bounds give its insertion point
    assert lower_bound(dup, 4) == upper_bound(dup, 4) == 5
    assert lower_bound(dup, 0) == 0 and lower_bound(dup, 9) == len(dup)

    # same answers as the standard library, on every value around the data
    import bisect
    import random

    random.seed(1)
    for _ in range(200):
        data = sorted(random.randint(0, 20) for _ in range(random.randint(0, 15)))
        for value in range(-1, 22):
            assert lower_bound(data, value) == bisect.bisect_left(data, value)
            assert upper_bound(data, value) == bisect.bisect_right(data, value)
            found = binary_search(data, value)
            assert (found is not None) == (value in data)
            assert found is None or data[found] == value
            assert binary_search_recursive(data, value) == found

    print("Success.")
