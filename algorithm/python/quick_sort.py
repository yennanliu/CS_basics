#---------------------------------------------------------------
# QUICK SORT
#---------------------------------------------------------------
#
# Divide and conquer: pick a pivot, partition the array into
# (< pivot) and (> pivot), then recurse on each side.
#
# Time  : Best/Avg O(N log N), Worst O(N^2) (bad pivots)
# Space : O(log N) average (recursion)
#
# References:
#   - https://rust-algo.club/sorting/quicksort/index.html
#   - https://www.baeldung.com/cs/quicksort-time-complexity-worst-case


# V0 : simple, extra space (easiest to read)
def quick_sort(arr):
    if len(arr) < 2:
        return arr
    pivot = arr[0]
    smaller = [x for x in arr[1:] if x <= pivot]
    bigger = [x for x in arr[1:] if x > pivot]
    return quick_sort(smaller) + [pivot] + quick_sort(bigger)


# V1 : in place, Lomuto partition (pivot = last element)
def quick_sort_inplace(arr, lo=0, hi=None):
    if hi is None:
        hi = len(arr) - 1
    if lo < hi:
        p = _partition(arr, lo, hi)
        quick_sort_inplace(arr, lo, p - 1)
        quick_sort_inplace(arr, p + 1, hi)
    return arr


def _partition(arr, lo, hi):
    pivot = arr[hi]
    i = lo
    for j in range(lo, hi):
        if arr[j] < pivot:
            arr[i], arr[j] = arr[j], arr[i]
            i += 1
    arr[i], arr[hi] = arr[hi], arr[i]
    return i


# V2 : in place, 3-way partition (efficient when there are many duplicates)
def quick_sort_3way(arr, left=0, right=None):
    if right is None:
        right = len(arr) - 1
    if right <= left:
        return arr
    a = i = left
    b = right
    pivot = arr[left]
    while i <= b:
        if arr[i] < pivot:
            arr[a], arr[i] = arr[i], arr[a]
            a += 1
            i += 1
        elif arr[i] > pivot:
            arr[b], arr[i] = arr[i], arr[b]
            b -= 1
        else:
            i += 1
    quick_sort_3way(arr, left, a - 1)
    quick_sort_3way(arr, b + 1, right)
    return arr


if __name__ == "__main__":
    assert quick_sort([0, 5, 3, 2, 2]) == [0, 2, 2, 3, 5]
    assert quick_sort_inplace([0, 5, 3, 1, 2]) == [0, 1, 2, 3, 5]
    assert quick_sort_3way([-5, -2, 1, -2, 0, 1]) == [-5, -2, -2, 0, 1, 1]
    print("Success.")
