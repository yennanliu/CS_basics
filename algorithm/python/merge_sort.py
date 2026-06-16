#---------------------------------------------------------------
# MERGE SORT
#---------------------------------------------------------------
#
# Divide and conquer: split the list in half, sort each half, then
# merge the two sorted halves. Stable.
#
# Time  : O(N log N) (all cases)
# Space : O(N)
#
# References:
#   - https://www.geeksforgeeks.org/merge-sort/


# V0 : in place (mutates and returns arr)
def merge_sort(arr):
    if len(arr) > 1:
        mid = len(arr) // 2
        left = arr[:mid]
        right = arr[mid:]

        merge_sort(left)
        merge_sort(right)

        i = j = k = 0
        while i < len(left) and j < len(right):
            if left[i] < right[j]:
                arr[k] = left[i]
                i += 1
            else:
                arr[k] = right[j]
                j += 1
            k += 1
        while i < len(left):
            arr[k] = left[i]
            i += 1
            k += 1
        while j < len(right):
            arr[k] = right[j]
            j += 1
            k += 1
    return arr


# V1 : pure / functional (returns a new sorted list)
def merge_sort_functional(collection):
    if len(collection) <= 1:
        return collection
    mid = len(collection) // 2
    left = merge_sort_functional(collection[:mid])
    right = merge_sort_functional(collection[mid:])
    return _merge(left, right)


def _merge(left, right):
    merged = []
    i = j = 0
    while i < len(left) and j < len(right):
        if left[i] < right[j]:
            merged.append(left[i])
            i += 1
        else:
            merged.append(right[j])
            j += 1
    merged.extend(left[i:])
    merged.extend(right[j:])
    return merged


if __name__ == "__main__":
    assert merge_sort([12, 11, 13, 5, 6, 7]) == [5, 6, 7, 11, 12, 13]
    assert merge_sort_functional([0, 5, 3, 2, 2]) == [0, 2, 2, 3, 5]
    print("Success.")
