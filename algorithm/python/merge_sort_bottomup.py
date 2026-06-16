#---------------------------------------------------------------
# MERGE SORT - BOTTOM UP (iterative)
#---------------------------------------------------------------
#
# Iterative merge sort: instead of recursing, merge runs of width 1,
# then 2, then 4, ... doubling each pass until the whole array is
# merged. No recursion stack.
#
# Time  : O(N log N)
# Space : O(N)
#
# References:
#   - https://leetcode.com/problems/sort-an-array/discuss/568255/Python-Merge-Sort
#   - https://rust-algo.club/sorting/mergesort/


def merge_sort(nums):
    n = len(nums)
    width = 1
    while width < n:
        # merge adjacent runs of length `width`
        for lo in range(0, n, 2 * width):
            mid = min(lo + width, n)
            hi = min(lo + 2 * width, n)
            nums[lo:hi] = _merge(nums[lo:mid], nums[mid:hi])
        width *= 2
    return nums


def _merge(left, right):
    res = []
    i = j = 0
    while i < len(left) and j < len(right):
        if left[i] < right[j]:
            res.append(left[i])
            i += 1
        else:
            res.append(right[j])
            j += 1
    res.extend(left[i:])
    res.extend(right[j:])
    return res


if __name__ == "__main__":
    assert merge_sort([5, 2, 9, 1, 5, 6]) == [1, 2, 5, 5, 6, 9]
    assert merge_sort([]) == []
    print("Success.")
