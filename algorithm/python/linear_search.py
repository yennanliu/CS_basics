#---------------------------------------------------------------
# LINEAR SEARCH
#---------------------------------------------------------------
#
# Check every element, front to back, until the target turns up.
#
#     target = 10
#
#     [ 0,  5,  7, 10, 15 ]
#       ^   ^   ^   ^
#       no  no  no  yes -> index 3
#
# It is the only search that needs NOTHING from the data: no sorting,
# no hashing, no index. That is exactly when to use it:
#
#     the data is ...            use
#     -------------------------  ---------------------------------
#     unsorted, searched once    linear search          O(N)
#     sorted                     binary search          O(log N)
#     searched many times        build a set / dict     O(1) each
#     tiny (a dozen items)       linear search -- nothing beats a
#                                straight scan at that size
#
# Time  : Best O(1) (first element), Avg/Worst O(N)
# Space : O(1)
#
# References:
#   - https://github.com/yennanliu/Python/blob/master/searches/linear_search.py


# V0 : index of the first match
def linear_search(sequence, target):
    """Return the index of the FIRST `target` in sequence, or None if absent."""
    for index, item in enumerate(sequence):
        if item == target:
            return index             # stop at the first hit: later copies are ignored
    return None


# V1 : every match -- the scan cannot stop early, so it is always O(N)
def linear_search_all(sequence, target):
    """Return the indexes of EVERY `target` in sequence (empty if absent)."""
    indexes = []
    for index, item in enumerate(sequence):
        if item == target:
            indexes.append(index)
    return indexes


# V2 : search by a condition instead of a value -- "first negative
# number", "first word longer than 5 letters". A hash set cannot do
# this; a scan can.
def find_first(sequence, condition):
    """Return the first item for which condition(item) is true, or None."""
    for item in sequence:
        if condition(item):
            return item
    return None


def is_negative(number):
    return number < 0


if __name__ == "__main__":
    nums = [0, 5, 7, 10, 15]
    assert linear_search(nums, 0) == 0           # best case: first element
    assert linear_search(nums, 15) == 4          # worst case: last element
    assert linear_search(nums, 6) is None        # absent: scanned everything
    assert linear_search([], 1) is None

    # works on unsorted data, and on anything that can be compared with ==
    assert linear_search([9, 3, 7, 3], 3) == 1   # the FIRST 3
    assert linear_search(["b", "a", "c"], "c") == 2

    assert linear_search_all([9, 3, 7, 3], 3) == [1, 3]
    assert linear_search_all([9, 3, 7, 3], 8) == []

    assert find_first([4, 2, -7, 1, -3], is_negative) == -7
    assert find_first([4, 2], is_negative) is None

    print("Success.")
