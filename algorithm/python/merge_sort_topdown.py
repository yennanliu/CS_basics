#---------------------------------------------------------------
# MERGE SORT - TOP DOWN
#---------------------------------------------------------------
#
# Recursively split the list down to single elements, then merge back
# up. Returns a new sorted list.
#
# Time  : O(N log N)
# Space : O(N)
#
# References:
#   - https://leetcode.com/explore/learn/card/recursion-ii/470/divide-and-conquer/2868/
#   - https://rust-algo.club/sorting/mergesort/


def merge_sort(nums):
    # base case: empty or single-element list is already sorted
    if len(nums) <= 1:
        return nums
    mid = len(nums) // 2
    left = merge_sort(nums[:mid])
    right = merge_sort(nums[mid:])
    return merge(left, right)


def merge(left, right):
    res = []
    i = j = 0
    while i < len(left) and j < len(right):
        if left[i] < right[j]:
            res.append(left[i])
            i += 1
        else:
            res.append(right[j])
            j += 1
    # append whatever remains in either list
    res.extend(left[i:])
    res.extend(right[j:])
    return res


if __name__ == "__main__":
    assert merge_sort([5, 2, 9, 1, 5, 6]) == [1, 2, 5, 5, 6, 9]
    print("Success.")
