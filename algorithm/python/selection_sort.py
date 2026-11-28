#---------------------------------------------------------------
# SELECTION SORT
#---------------------------------------------------------------
#
# Find the smallest value in the unsorted part and swap it to the
# front of that part. The sorted region grows by one each round.
#
#     sorted | unsorted
#            | 4  2  5  1     smallest is 1 -> swap with 4
#     1      | 2  5  4        smallest is 2 -> already in place
#     1  2   | 5  4           smallest is 4 -> swap with 5
#     1  2  4  5
#
# It always scans the whole unsorted part, so it is O(N^2) even on
# sorted input -- there is no early exit to add. What it is good at is
# WRITING: at most N - 1 swaps, the fewest of any simple sort. That
# matters only when a write is far more expensive than a comparison.
#
# NOT stable: the long-distance swap can jump a value over an equal
# one. In [2a, 2b, 1] the first round swaps 2a with 1 -> [1, 2b, 2a].
#
# Time  : O(N^2) in every case
# Space : O(1), in place
#
# References:
#   - https://github.com/yennanliu/CS_basics/blob/master/doc/pic/select_sort_1.jpg


# V0 : in place
def selection_sort(nums):
    """Sort `nums` in place (ascending) and return it."""
    for i in range(len(nums) - 1):
        # find the index of the smallest value in nums[i..]
        min_idx = i
        for j in range(i + 1, len(nums)):
            if nums[j] < nums[min_idx]:
                min_idx = j
        # put it at position i; nums[0..i] is now sorted and final
        if min_idx != i:
            nums[i], nums[min_idx] = nums[min_idx], nums[i]
    return nums


# V1 : extra space -- the same idea without the swap: keep pulling the
# minimum out into a new list. Leaves the input untouched.
def selection_sort_extra_space(nums):
    """Return a new sorted list."""
    remaining = list(nums)
    sorted_list = []
    while remaining:
        min_idx = remaining.index(min(remaining))    # O(N) scan, like the loop above
        sorted_list.append(remaining.pop(min_idx))
    return sorted_list


def count_swaps(nums):
    """How many swaps selection_sort makes (for the demo below)."""
    nums = list(nums)
    swaps = 0
    for i in range(len(nums) - 1):
        min_idx = i
        for j in range(i + 1, len(nums)):
            if nums[j] < nums[min_idx]:
                min_idx = j
        if min_idx != i:
            nums[i], nums[min_idx] = nums[min_idx], nums[i]
            swaps += 1
    return swaps


if __name__ == "__main__":
    assert selection_sort([4, 2, 5, 1]) == [1, 2, 4, 5]
    assert selection_sort([9, 1, 2, 3, 0]) == [0, 1, 2, 3, 9]
    assert selection_sort([]) == [] and selection_sort([7]) == [7]

    original = [9, 1, 2, 3, 0]
    assert selection_sort_extra_space(original) == [0, 1, 2, 3, 9]
    assert original == [9, 1, 2, 3, 0]               # the input was not touched

    # never more than N - 1 swaps, however scrambled the input
    assert count_swaps([4, 2, 5, 1]) == 2
    assert count_swaps([5, 4, 3, 2, 1]) == 2         # reversed: just 2 swaps
    assert count_swaps([1, 2, 3, 4, 5]) == 0

    # NOT stable: 2.0 and 2 are equal, 2.0 starts first, and ends up second
    assert repr(selection_sort([2.0, 2, 1])) == "[1, 2, 2.0]"

    # agree with the built-in sort on random input
    import random

    random.seed(1)
    for _ in range(200):
        data = [random.randint(-9, 9) for _ in range(random.randint(0, 12))]
        assert selection_sort(list(data)) == sorted(data)
        assert selection_sort_extra_space(data) == sorted(data)
        assert count_swaps(data) <= max(0, len(data) - 1)

    print("Success.")
