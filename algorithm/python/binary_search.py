#---------------------------------------------------------------
#  BINARY SEARCH
#---------------------------------------------------------------
#
# Search a value in a SORTED array in O(log N) time by repeatedly
# halving the search range.
#
# Time  : Best O(1), Avg/Worst O(log N)
# Space : iterative O(1), recursive O(log N) (call stack)
#
# References:
#   - http://kuanghy.github.io/2016/06/14/python-bisect
#   - https://www.geeksforgeeks.org/complexity-analysis-of-binary-search/


# V0 : iterative
def binary_search(array, target):
    lo, hi = 0, len(array) - 1
    while lo <= hi:
        # use lo + (hi - lo) // 2 (not (lo + hi) // 2) to avoid overflow
        # in languages with fixed-width integers
        mid = lo + (hi - lo) // 2
        if array[mid] == target:
            return mid
        elif array[mid] < target:
            lo = mid + 1
        else:
            hi = mid - 1
    return None


# V1 : recursive
def binary_search_recursive(array, target, lo, hi):
    if lo > hi:                      # base case: not found
        return None
    mid = lo + (hi - lo) // 2
    if array[mid] == target:
        return mid
    elif array[mid] < target:
        return binary_search_recursive(array, target, mid + 1, hi)
    else:
        return binary_search_recursive(array, target, lo, mid - 1)


if __name__ == "__main__":
    arr = [0, 5, 7, 10, 15]
    assert binary_search(arr, 7) == 2
    assert binary_search(arr, 6) is None
    assert binary_search_recursive(arr, 15, 0, len(arr) - 1) == 4
    print("Success.")
