#---------------------------------------------------------------
# QUICK SORT
#---------------------------------------------------------------
#
# Scope: the idea, and the three ways to write it. See
#        quick_sort_v2.py for the Hoare partition scheme and
#        quick_select.py for "k-th smallest" using the same partition.
#
# Divide and conquer:
#   1) pick one value as the PIVOT
#   2) PARTITION: move everything smaller to its left and everything
#      larger to its right -- the pivot is now in its FINAL position
#   3) recurse on the left part and on the right part
#
#     [3, 6, 1, 8, 2, 5]             pivot = 5 (the last element)
#      \_________/  ^
#     [3, 1, 2]  5  [8, 6]           5 is home; neither side is sorted yet
#     [1, 2, 3]  5  [6, 8]           recurse on each side
#
# All the real work happens in the partition step, BEFORE the
# recursion (merge sort is the mirror image: it recurses first and
# does the work, the merge, afterwards).
#
# WHY THE PIVOT MATTERS
#   A pivot that lands in the middle halves the problem: log N levels
#   of O(N) work = O(N log N). A pivot that is always the smallest or
#   largest value peels off ONE element per level: N levels = O(N^2).
#   "Always pick the last element" hits that worst case on input that
#   is already sorted -- the most common input there is. Picking the
#   pivot at RANDOM (V1) makes the bad case vanishingly unlikely on
#   any input.
#
# Time  : Best/Avg O(N log N), Worst O(N^2) (bad pivots every time)
# Space : O(log N) average for the recursion stack, O(N) worst;
#         V0 also allocates O(N) new lists per level
#
# NOT stable: partitioning swaps values across long distances.
#
# References:
#   - https://rust-algo.club/sorting/quicksort/index.html
#   - https://www.baeldung.com/cs/quicksort-time-complexity-worst-case


import random


# V0 : new lists -- the easiest version to read and to explain.
# Not in place: every level builds new lists, so it is O(N) extra space.
def quick_sort(arr):
    """Return a new sorted list."""
    if len(arr) < 2:                 # 0 or 1 element is already sorted
        return arr

    pivot = arr[0]
    smaller = []                     # everything <= pivot (except the pivot itself)
    bigger = []                      # everything >  pivot
    for value in arr[1:]:
        if value <= pivot:
            smaller.append(value)
        else:
            bigger.append(value)

    return quick_sort(smaller) + [pivot] + quick_sort(bigger)


# V1 : in place, Lomuto partition, random pivot -- the one to know
def quick_sort_inplace(arr, lo=0, hi=None):
    """Sort arr[lo..hi] in place and return arr."""
    if hi is None:
        hi = len(arr) - 1
    if lo < hi:                      # at least two elements left
        p = _partition(arr, lo, hi)  # arr[p] is now in its final position
        quick_sort_inplace(arr, lo, p - 1)
        quick_sort_inplace(arr, p + 1, hi)
    return arr


def _partition(arr, lo, hi):
    """Lomuto partition of arr[lo..hi]; return the pivot's final index.

    `i` is the boundary: everything left of i is smaller than the pivot.

        [3, 6, 1, 8, 2 | 5]     pivot = 5        i=0
         3 < 5: keep, i=1
            6: skip
               1 < 5: swap with arr[1] -> [3, 1, 6, 8, 2 | 5]   i=2
                  8: skip
                     2 < 5: swap with arr[2] -> [3, 1, 2, 8, 6 | 5]   i=3
        finally swap the pivot into slot i    -> [3, 1, 2, 5, 6, 8]
    """
    # choose the pivot at random and park it at the end, so sorted
    # input is no longer the worst case
    pivot_index = random.randint(lo, hi)
    arr[pivot_index], arr[hi] = arr[hi], arr[pivot_index]
    pivot = arr[hi]

    i = lo
    for j in range(lo, hi):
        if arr[j] < pivot:
            arr[i], arr[j] = arr[j], arr[i]
            i += 1
    arr[i], arr[hi] = arr[hi], arr[i]    # the pivot goes between the two parts
    return i


# V2 : in place, 3-way partition (Dutch national flag) -- for input
# with MANY DUPLICATES.
#
# The 2-way partition above puts every copy of the pivot on one side,
# so a list of all-equal values degrades to O(N^2). Splitting into
# three parts sends every copy of the pivot home at once:
#
#     [ < pivot | == pivot | not looked at yet | > pivot ]
#       left..lt-1  lt..i-1      i..gt             gt+1..right
def quick_sort_3way(arr, left=0, right=None):
    """Sort arr[left..right] in place and return arr."""
    if right is None:
        right = len(arr) - 1
    if right <= left:
        return arr

    pivot = arr[random.randint(left, right)]
    lt = left                        # arr[left..lt-1]  <  pivot
    i = left                         # arr[lt..i-1]     == pivot
    gt = right                       # arr[gt+1..right] >  pivot
    while i <= gt:
        if arr[i] < pivot:
            arr[lt], arr[i] = arr[i], arr[lt]
            lt += 1
            i += 1
        elif arr[i] > pivot:
            arr[gt], arr[i] = arr[i], arr[gt]
            gt -= 1                  # do NOT advance i: the value swapped in is unseen
        else:
            i += 1

    quick_sort_3way(arr, left, lt - 1)   # the == pivot block is already final
    quick_sort_3way(arr, gt + 1, right)
    return arr


if __name__ == "__main__":
    assert quick_sort([3, 6, 1, 8, 2, 5]) == [1, 2, 3, 5, 6, 8]
    assert quick_sort([0, 5, 3, 2, 2]) == [0, 2, 2, 3, 5]
    assert quick_sort_inplace([0, 5, 3, 1, 2]) == [0, 1, 2, 3, 5]
    assert quick_sort_3way([-5, -2, 1, -2, 0, 1]) == [-5, -2, -2, 0, 1, 1]
    for sort in (quick_sort, quick_sort_inplace, quick_sort_3way):
        assert sort([]) == [] and sort([7]) == [7]

    # sorted input: with a random pivot this is an ordinary case. (With a
    # fixed "last element" pivot, 5000 sorted values would need 5000
    # nested calls and hit Python's recursion limit.)
    ordered = list(range(5000))
    assert quick_sort_inplace(list(ordered)) == ordered
    assert quick_sort_inplace(ordered[::-1]) == ordered

    # all-equal input: the 3-way partition finishes in ONE pass
    assert quick_sort_3way([4] * 5000) == [4] * 5000

    # agree with the built-in sort on random input
    random.seed(1)
    for _ in range(200):
        data = [random.randint(-9, 9) for _ in range(random.randint(0, 30))]
        assert quick_sort(list(data)) == sorted(data)
        assert quick_sort_inplace(list(data)) == sorted(data)
        assert quick_sort_3way(list(data)) == sorted(data)

    print("Success.")
