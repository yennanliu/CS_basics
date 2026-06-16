#---------------------------------------------------------------
# PANCAKE SORT
#---------------------------------------------------------------
#
# Sort using only "flips" (reverse the prefix arr[0..k]). Each pass
# moves the current maximum to its final position with two flips:
#   1) flip the max to the front, 2) flip it to the back of the
#   unsorted part.
#
# Time  : O(N^2)
# Space : O(N)
#
# References:
#   - LC 969. Pancake Sorting
#   - https://github.com/TheAlgorithms/Python/blob/master/sorts/pancake_sort.py


# V0 : LC 969 - return the sequence of flip sizes (1-based)
class Solution:
    def pancakeSort(self, arr):
        res = []
        cur = len(arr)
        while cur > 1:
            max_idx = arr.index(max(arr[:cur]))
            res += [max_idx + 1, cur]                 # flip sizes are 1-based
            # flip the max to the front, then flip it to position cur - 1
            arr = arr[:max_idx + 1][::-1] + arr[max_idx + 1:]
            arr = arr[:cur][::-1] + arr[cur:]
            cur -= 1
        return res


# V1 : return the sorted array (leaves the input untouched)
def pancake_sort(arr):
    arr = list(arr)
    cur = len(arr)
    while cur > 1:
        max_idx = arr.index(max(arr[:cur]))
        arr = arr[max_idx::-1] + arr[max_idx + 1:]    # flip 0..max_idx
        arr = arr[cur - 1::-1] + arr[cur:]            # flip 0..cur-1
        cur -= 1
    return arr


if __name__ == "__main__":
    assert pancake_sort([3, 2, 4, 1]) == [1, 2, 3, 4]
    assert pancake_sort([]) == []
    print("flip sizes:", Solution().pancakeSort([3, 2, 4, 1]))
    print("Success.")
