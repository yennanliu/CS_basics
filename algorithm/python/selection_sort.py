#---------------------------------------------------------------
# SELECTION SORT
#---------------------------------------------------------------
#
# Repeatedly select the minimum of the unsorted part and swap it into
# the next position of the sorted part.
#
# Time  : O(N^2) (all cases)
# Space : O(1) in place
#
# References:
#   - https://github.com/yennanliu/CS_basics/blob/master/doc/pic/select_sort_1.jpg


# V0 : in place
def selection_sort(nums):
    for i in range(len(nums) - 1):
        min_idx = i
        for j in range(i + 1, len(nums)):
            if nums[j] < nums[min_idx]:
                min_idx = j
        if min_idx != i:
            nums[i], nums[min_idx] = nums[min_idx], nums[i]
    return nums


# V1 : extra space (build a new sorted list, leaves input untouched)
def selection_sort_extra_space(nums):
    nums = list(nums)
    sorted_list = []
    while nums:
        min_idx = nums.index(min(nums))
        sorted_list.append(nums.pop(min_idx))
    return sorted_list


if __name__ == "__main__":
    assert selection_sort([9, 1, 2, 3, 0]) == [0, 1, 2, 3, 9]
    assert selection_sort_extra_space([9, 1, 2, 3, 0]) == [0, 1, 2, 3, 9]
    print("Success.")
