#---------------------------------------------------------------
# MERGE SORT - TOP DOWN (recursive)
#---------------------------------------------------------------
#
# Scope: the recursive version, with a trace of what the recursion
#        actually does. See merge_sort.py for the idea and the merge
#        step, merge_sort_bottomup.py for the loop-only version.
#
# "Top down" = start from the whole list and split downwards:
#
#     merge_sort([5, 2, 9, 1])
#       merge_sort([5, 2])
#         merge_sort([5])          -> [5]        base case
#         merge_sort([2])          -> [2]        base case
#         merge([5], [2])          -> [2, 5]
#       merge_sort([9, 1])
#         merge_sort([9])          -> [9]
#         merge_sort([1])          -> [1]
#         merge([9], [1])          -> [1, 9]
#       merge([2, 5], [1, 9])      -> [1, 2, 5, 9]
#
# Read it as a recipe with one leap of faith: ASSUME merge_sort
# correctly sorts anything shorter than the current list. Then
# "sort the two halves, merge them" is obviously right -- and the
# base case (0 or 1 element) is what makes the assumption true.
#
# The recursion is log2(N) levels deep (the list halves each time),
# so even a million elements nest only 20 calls.
#
# Time  : O(N log N)
# Space : O(N) for the copies, + O(log N) call stack
#
# References:
#   - https://leetcode.com/explore/learn/card/recursion-ii/470/divide-and-conquer/2868/
#   - https://rust-algo.club/sorting/mergesort/


def merge_sort(nums):
    """Return a new sorted list."""
    # base case: an empty or single-element list is already sorted
    if len(nums) <= 1:
        return list(nums)
    mid = len(nums) // 2
    left = merge_sort(nums[:mid])    # sort the first half
    right = merge_sort(nums[mid:])   # sort the second half
    return merge(left, right)        # combine two sorted halves


def merge(left, right):
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
    # append whatever remains in either list
    res.extend(left[i:])
    res.extend(right[j:])
    return res


def merge_sort_traced(nums, depth=0, log=None):
    """merge_sort, recording each call -- this is how the header was drawn."""
    if log is None:
        log = []
    indent = "  " * depth
    if len(nums) <= 1:
        log.append("{}merge_sort({}) -> base case".format(indent, nums))
        return list(nums), log
    log.append("{}merge_sort({})".format(indent, nums))
    mid = len(nums) // 2
    left, _ = merge_sort_traced(nums[:mid], depth + 1, log)
    right, _ = merge_sort_traced(nums[mid:], depth + 1, log)
    result = merge(left, right)
    log.append("{}  merge({}, {}) -> {}".format(indent, left, right, result))
    return result, log


if __name__ == "__main__":
    assert merge_sort([5, 2, 9, 1]) == [1, 2, 5, 9]
    assert merge_sort([5, 2, 9, 1, 5, 6]) == [1, 2, 5, 5, 6, 9]
    assert merge_sort([]) == [] and merge_sort([7]) == [7]
    assert merge_sort([2, 1, 3]) == [1, 2, 3]            # odd length: halves differ

    # the recursion, as drawn in the header
    result, calls = merge_sort_traced([5, 2, 9, 1])
    assert result == [1, 2, 5, 9]
    assert calls[0] == "merge_sort([5, 2, 9, 1])"
    assert calls[1] == "  merge_sort([5, 2])"
    assert calls[-1] == "  merge([2, 5], [1, 9]) -> [1, 2, 5, 9]"

    # stable: 1.0 and 1 are equal and keep their original order
    assert repr(merge_sort([1.0, 0, 1])) == "[0, 1.0, 1]"

    # agree with the built-in sort on random input
    import random

    random.seed(1)
    for _ in range(200):
        data = [random.randint(-9, 9) for _ in range(random.randint(0, 30))]
        assert merge_sort(data) == sorted(data)

    print("\n".join(calls))
    print("Success.")
