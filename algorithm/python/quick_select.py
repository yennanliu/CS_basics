#---------------------------------------------------------------
# QUICK SELECT
#---------------------------------------------------------------
#
# Scope: find the k-th smallest value WITHOUT sorting everything.
#        See quick_sort.py for the partition step this is built on.
#
# After one partition the pivot is in its FINAL sorted position p:
#
#     [ smaller than pivot ] pivot [ larger than pivot ]
#       indexes lo..p-1        p     indexes p+1..hi
#
# Quick sort now recurses into BOTH sides. Quick select only needs the
# side that contains index k, and throws the other one away:
#
#     k == p   -> the pivot is the answer
#     k <  p   -> look only in the left part
#     k >  p   -> look only in the right part
#
#     nums = [3, 2, 1, 5, 6, 4],  want index k = 4 (the 5th smallest)
#       partition -> [3, 2, 1, 4 | 6, 5]   pivot 4 at p = 3;  4 > 3 -> go right
#       partition -> [5, 6]                pivot 6 at p = 5;  4 < 5 -> go left
#       one element left, index 4 -> 5
#
# Each round keeps about half: N + N/2 + N/4 + ... = 2N work in total.
#
# Time  : Avg O(N), Worst O(N^2) (bad pivot every time; the random
#         pivot makes that vanishingly unlikely)
# Space : O(1) -- a loop, no recursion. It REORDERS the list it is given.
#
#     approach            time          when
#     ------------------  ------------  -------------------------------
#     sort, then index    O(N log N)    you need the sorted order anyway
#     heap of size k      O(N log k)    k is small, or data is a stream
#     quick select        O(N) average  one-off k-th element query
#
# Used by: LC 215 Kth Largest Element in an Array, LC 973 K Closest
#          Points to Origin, LC 347 Top K Frequent Elements.


import random


def partition(nums, lo, hi):
    """Lomuto partition of nums[lo..hi] around a random pivot.

    Returns the pivot's final index: everything left of it is smaller,
    everything right of it is greater or equal.
    """
    pivot_index = random.randint(lo, hi)
    nums[pivot_index], nums[hi] = nums[hi], nums[pivot_index]
    pivot = nums[hi]

    boundary = lo                    # nums[lo..boundary-1] < pivot
    for j in range(lo, hi):
        if nums[j] < pivot:
            nums[boundary], nums[j] = nums[j], nums[boundary]
            boundary += 1
    nums[boundary], nums[hi] = nums[hi], nums[boundary]
    return boundary


# V0 : k-th smallest, k counted from 0 (k = 0 is the minimum)
def quick_select(nums, k):
    """Return the value that would be at index k if nums were sorted.

    Reorders `nums` in place.
    """
    if not 0 <= k < len(nums):
        raise IndexError("k out of range: {}".format(k))

    lo = 0
    hi = len(nums) - 1
    while True:
        p = partition(nums, lo, hi)
        if p == k:
            return nums[p]           # the pivot landed exactly on index k
        if k < p:
            hi = p - 1               # the answer is in the left part
        else:
            lo = p + 1               # the answer is in the right part


# V1 : LC 215 -- k-th LARGEST, k counted from 1
def kth_largest(nums, k):
    """The k-th largest value (k = 1 is the maximum). Leaves nums untouched."""
    # the k-th largest of N values is the one at sorted index N - k
    return quick_select(list(nums), len(nums) - k)


# V2 : the k smallest values. After quick_select puts the right value
# at index k - 1, everything before it is already no larger than it.
def k_smallest(nums, k):
    """The k smallest values, in no particular order. Leaves nums untouched."""
    if k <= 0:
        return []
    if k >= len(nums):
        return list(nums)
    copy = list(nums)
    quick_select(copy, k - 1)
    return copy[:k]


if __name__ == "__main__":
    nums = [3, 2, 1, 5, 6, 4]
    assert quick_select(list(nums), 0) == 1          # the minimum
    assert quick_select(list(nums), 4) == 5
    assert quick_select(list(nums), 5) == 6          # the maximum
    assert quick_select([7], 0) == 7

    # LC 215's examples
    assert kth_largest([3, 2, 1, 5, 6, 4], 2) == 5
    assert kth_largest([3, 2, 3, 1, 2, 4, 5, 5, 6], 4) == 4

    assert sorted(k_smallest([9, 5, 6, 2, 3, 8, 1], 3)) == [1, 2, 3]
    assert k_smallest([4, 2], 0) == [] and sorted(k_smallest([4, 2], 9)) == [2, 4]

    try:
        quick_select([1, 2, 3], 3)
        raise AssertionError("expected IndexError")
    except IndexError:
        pass

    # every k, on random input with duplicates, agrees with sorting
    random.seed(1)
    for _ in range(200):
        data = [random.randint(-9, 9) for _ in range(random.randint(1, 25))]
        expected = sorted(data)
        for k in range(len(data)):
            assert quick_select(list(data), k) == expected[k]
        assert sorted(k_smallest(data, 3)) == expected[:3]

    print("Success.")
