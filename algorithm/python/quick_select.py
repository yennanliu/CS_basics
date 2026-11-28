#---------------------------------------------------------------
# QUICK SELECT
#---------------------------------------------------------------
#
# Scope: find the k-th smallest value WITHOUT sorting everything.
#        See quick_sort.py for the partition step this is built on.
#
# One partition splits the range into three blocks around a pivot:
#
#     [ smaller than pivot ][ equal to pivot ][ larger than pivot ]
#       indexes lo..lt-1      lt..gt            gt+1..hi
#
# The middle block is in its FINAL sorted position. Quick sort would
# now recurse into BOTH outer blocks. Quick select only needs the one
# that contains index k, and throws the rest away:
#
#     lt <= k <= gt   -> the pivot is the answer
#     k < lt          -> look only in the left block
#     k > gt          -> look only in the right block
#
#     nums = [3, 2, 1, 5, 6, 4],  want index k = 4 (the 5th smallest)
#       pivot 4 -> [3, 2, 1 | 4 | 5, 6]   block is index 3;   4 > 3 -> go right
#       pivot 6 -> [5 | 6]                block is index 5;   4 < 5 -> go left
#       one element left, at index 4 -> 5
#
# Each round keeps about half: N + N/2 + N/4 + ... = 2N work in total.
#
# WHY THREE BLOCKS, NOT TWO
#   A two-way partition (smaller | pivot | everything else) is enough
#   when the values are distinct. With many EQUAL values it is not: on
#   an all-equal list nothing is "smaller", so each round removes just
#   one element and the total is N + (N-1) + ... = O(N^2) -- on every
#   run, whatever pivot is picked. Giving the equal values a block of
#   their own ends that case in a single round.
#
# Time  : Avg O(N); Worst O(N^2), which now needs an unlucky pivot
#         round after round (a random pivot makes that vanishingly
#         unlikely) -- duplicates alone can no longer cause it
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
    """3-way partition of nums[lo..hi] around a random pivot.

    Returns (lt, gt): afterwards nums[lo..lt-1] < pivot,
    nums[lt..gt] == pivot, and nums[gt+1..hi] > pivot.

        [ < pivot | == pivot | not looked at yet | > pivot ]
          lo..lt-1   lt..i-1       i..gt            gt+1..hi
    """
    pivot = nums[random.randint(lo, hi)]
    lt = lo
    i = lo
    gt = hi
    while i <= gt:
        if nums[i] < pivot:
            nums[lt], nums[i] = nums[i], nums[lt]
            lt += 1
            i += 1
        elif nums[i] > pivot:
            nums[gt], nums[i] = nums[i], nums[gt]
            gt -= 1                  # do NOT advance i: the value swapped in is unseen
        else:
            i += 1
    return lt, gt


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
        lt, gt = partition(nums, lo, hi)
        if k < lt:
            hi = lt - 1              # the answer is among the smaller values
        elif k > gt:
            lo = gt + 1              # the answer is among the larger values
        else:
            return nums[k]           # index k falls inside the == pivot block


# V1 : LC 215 -- k-th LARGEST, k counted from 1
def kth_largest(nums, k):
    """The k-th largest value (k = 1 is the maximum). Leaves nums untouched."""
    # the k-th largest of N values is the one at sorted index N - k
    return quick_select(list(nums), len(nums) - k)


# V2 : the k smallest values. After quick_select puts the right value
# at index k - 1, everything before it is already no larger than it
# (each partition only ever moves smaller values to the left).
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

    # what partition promises: three blocks, the middle one in final position
    random.seed(3)
    blocks = [5, 1, 5, 9, 5, 2]
    lt, gt = partition(blocks, 0, len(blocks) - 1)
    pivot = blocks[lt]
    assert all(value < pivot for value in blocks[:lt])
    assert all(value == pivot for value in blocks[lt:gt + 1])
    assert all(value > pivot for value in blocks[gt + 1:])

    # all-equal input ends in ONE round. (A two-way partition would take
    # 200000 rounds here -- about 20 billion comparisons.)
    same = [7] * 200000
    assert partition(same, 0, len(same) - 1) == (0, len(same) - 1)
    assert quick_select(same, 0) == quick_select(same, len(same) - 1) == 7
    assert quick_select([2, 1] * 50000, 50000) == 2      # only two distinct values

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
