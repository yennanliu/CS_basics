#---------------------------------------------------------------
# INSERTION SORT
#---------------------------------------------------------------
#
# Build the sorted array one element at a time: take each element and
# insert it into its correct position within the already-sorted left
# part. Stable sort; very fast on nearly-sorted input.
#
# Time  : Best O(N), Avg/Worst O(N^2)
# Space : O(1)
#
# References:
#   - https://rust-algo.club/sorting/insertion_sort/index.html
#   - https://github.com/yennanliu/CS_basics/blob/master/doc/pic/insert_sort_1.jpg


# V0
def insertion_sort(arr):
    for i in range(1, len(arr)):
        key = arr[i]
        j = i - 1
        # shift elements greater than key one position to the right
        while j >= 0 and key < arr[j]:
            arr[j + 1] = arr[j]
            j -= 1
        arr[j + 1] = key
    return arr


if __name__ == "__main__":
    assert insertion_sort([12, 11, 13, 5, 6]) == [5, 6, 11, 12, 13]
    print("Success.")
