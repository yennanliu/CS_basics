#---------------------------------------------------------------
#  BUBBLE SORT
#---------------------------------------------------------------
#
# Repeatedly step through the list, swapping adjacent out-of-order
# elements so the largest "bubbles" to the end on each pass.
#
# Time  : Best O(N) (already sorted, with early exit), Avg/Worst O(N^2)
# Space : O(1)


# V0 : basic
def bubble_sort(nums):
    n = len(nums)
    for i in range(n - 1):
        # the last i elements are already in place, so skip them
        for j in range(n - 1 - i):
            if nums[j] > nums[j + 1]:
                nums[j], nums[j + 1] = nums[j + 1], nums[j]
    return nums


# V1 : optimized with early exit (stop once a pass makes no swap)
def bubble_sort_optimized(nums):
    n = len(nums)
    for i in range(n - 1):
        swapped = False
        for j in range(n - 1 - i):
            if nums[j] > nums[j + 1]:
                nums[j], nums[j + 1] = nums[j + 1], nums[j]
                swapped = True
        if not swapped:          # already sorted -> stop early
            break
    return nums


if __name__ == "__main__":
    assert bubble_sort([9, 1, 2, 3, 0]) == [0, 1, 2, 3, 9]
    assert bubble_sort_optimized([-2, -5, -45]) == [-45, -5, -2]
    print("Success.")
