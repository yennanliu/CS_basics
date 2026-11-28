#---------------------------------------------------------------
# BUBBLE SORT
#---------------------------------------------------------------
#
# Walk the list comparing NEIGHBOURS and swap any pair that is out of
# order. After one full pass the largest value has "bubbled" to the
# end; repeat on the part that is still unsorted.
#
#     [5, 1, 4, 2]     pass 1: compare neighbours left to right
#      5>1 swap  ->  [1, 5, 4, 2]
#         5>4 swap ->  [1, 4, 5, 2]
#            5>2 swap -> [1, 4, 2, | 5]     5 is in its final place
#
#     pass 2:  [1, 4, 2 | 5]  ->  [1, 2, | 4, 5]
#     pass 3:  [1, 2 | 4, 5]  ->  no swap -> already sorted, stop
#
# It only ever swaps ADJACENT elements that are strictly out of order,
# so equal values never pass each other: the sort is STABLE.
#
# Time  : Best O(N) (already sorted, with the early exit of V1),
#         Avg/Worst O(N^2)
# Space : O(1), in place
#
# In practice: never the right choice (insertion sort does the same job
# with fewer writes). It is here because it is the simplest sort to
# reason about, and "one pass fixes one element" is a pattern worth
# seeing once.


# V0 : basic -- always makes every pass
def bubble_sort(nums):
    """Sort `nums` in place (ascending) and return it."""
    n = len(nums)
    for done in range(n - 1):
        # the last `done` elements are already in their final place
        for j in range(n - 1 - done):
            if nums[j] > nums[j + 1]:            # strictly greater: keeps it stable
                nums[j], nums[j + 1] = nums[j + 1], nums[j]
    return nums


# V1 : early exit -- a pass with no swap proves the list is sorted
def bubble_sort_optimized(nums):
    """Same as bubble_sort, but O(N) on input that is already sorted."""
    n = len(nums)
    for done in range(n - 1):
        swapped = False
        for j in range(n - 1 - done):
            if nums[j] > nums[j + 1]:
                nums[j], nums[j + 1] = nums[j + 1], nums[j]
                swapped = True
        if not swapped:                          # nothing moved: every pair is in order
            break
    return nums


def count_passes(nums):
    """How many passes bubble_sort_optimized makes (for the demo below)."""
    nums = list(nums)
    n = len(nums)
    passes = 0
    for done in range(n - 1):
        passes += 1
        swapped = False
        for j in range(n - 1 - done):
            if nums[j] > nums[j + 1]:
                nums[j], nums[j + 1] = nums[j + 1], nums[j]
                swapped = True
        if not swapped:
            break
    return passes


if __name__ == "__main__":
    assert bubble_sort([5, 1, 4, 2]) == [1, 2, 4, 5]
    assert bubble_sort([9, 1, 2, 3, 0]) == [0, 1, 2, 3, 9]
    assert bubble_sort_optimized([-2, -5, -45]) == [-45, -5, -2]
    assert bubble_sort([]) == [] and bubble_sort([7]) == [7]

    # the early exit: sorted input needs ONE pass, reversed input needs them all
    assert count_passes([1, 2, 3, 4, 5]) == 1
    assert count_passes([5, 4, 3, 2, 1]) == 4

    # stable: 1.0 and 1 are EQUAL, so they must keep their original order
    # (1.0 first). repr() is what lets us tell the two apart.
    assert repr(bubble_sort([1.0, 0, 1])) == "[0, 1.0, 1]"
    assert repr(bubble_sort([1, 0, 1.0])) == "[0, 1, 1.0]"

    # agree with the built-in sort on random input
    import random

    random.seed(1)
    for _ in range(200):
        data = [random.randint(-9, 9) for _ in range(random.randint(0, 12))]
        assert bubble_sort(list(data)) == sorted(data)
        assert bubble_sort_optimized(list(data)) == sorted(data)

    print("Success.")
