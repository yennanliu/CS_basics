#---------------------------------------------------------------
# PANCAKE SORT
#---------------------------------------------------------------
#
# Sort a stack of pancakes when the ONLY move allowed is to slide a
# spatula under some pancake and flip everything above it -- that is,
# reverse a PREFIX of the list.
#
#     flip(arr, k) reverses the first k elements
#
# Each round sends the largest unsorted value to its final place with
# two flips:
#
#     [3, 2, 4, 1]      the largest unsorted value is 4, at index 2
#     flip 3  ->  [4, 2, 3, 1]      1) bring it to the FRONT
#     flip 4  ->  [1, 3, 2, 4]      2) flip the whole unsorted part: it lands at the BACK
#
#     [1, 3, 2 | 4]     now 3, at index 1
#     flip 2  ->  [3, 1, 2 | 4]
#     flip 3  ->  [2, 1, 3 | 4]
#
#     [2, 1 | 3, 4]     now 2, already at the front
#     flip 2  ->  [1, 2 | 3, 4]     sorted
#
# It is selection sort with a restricted move: "swap into place"
# becomes "two flips".
#
# Time  : O(N^2) -- N rounds, each one finding a maximum (O(N)) and
#         flipping (O(N))
# Space : O(1) extra for the in-place version (V1)
# Flips : at most 2 per round, so at most 2N in total
#
# References:
#   - LC 969. Pancake Sorting
#   - https://github.com/TheAlgorithms/Python/blob/master/sorts/pancake_sort.py


def flip(arr, k):
    """Reverse the first k elements of arr, in place."""
    left = 0
    right = k - 1
    while left < right:
        arr[left], arr[right] = arr[right], arr[left]
        left += 1
        right -= 1


def index_of_max(arr, size):
    """Index of the largest value among the first `size` elements."""
    best = 0
    for i in range(1, size):
        if arr[i] > arr[best]:
            best = i
    return best


# V0 : LC 969 -- return the flip sizes that sort the array
class Solution:
    def pancakeSort(self, arr):
        """Return a list of k values; flipping by each in turn sorts arr.

        Works on a copy, so the caller's list is left alone.
        """
        arr = list(arr)
        flips = []
        size = len(arr)              # arr[size..] is already sorted and final
        while size > 1:
            max_idx = index_of_max(arr, size)
            if max_idx != size - 1:              # not already in place
                if max_idx != 0:
                    flip(arr, max_idx + 1)       # 1) bring the max to the front
                    flips.append(max_idx + 1)
                flip(arr, size)                  # 2) send it to the back
                flips.append(size)
            size -= 1
        return flips


# V1 : return the sorted list
def pancake_sort(arr):
    """Return a new sorted list, built using prefix flips only."""
    arr = list(arr)
    size = len(arr)
    while size > 1:
        max_idx = index_of_max(arr, size)
        flip(arr, max_idx + 1)       # the max is now at index 0
        flip(arr, size)              # ... and now at index size - 1
        size -= 1
    return arr


def apply_flips(arr, flips):
    """Replay a list of flip sizes on a copy of arr."""
    arr = list(arr)
    for k in flips:
        flip(arr, k)
    return arr


if __name__ == "__main__":
    stack = [1, 2, 3, 4, 5]
    flip(stack, 3)
    assert stack == [3, 2, 1, 4, 5]              # only the prefix moved

    assert pancake_sort([3, 2, 4, 1]) == [1, 2, 3, 4]
    assert pancake_sort([]) == [] and pancake_sort([7]) == [7]

    # LC 969: the flips from the header, and replaying them sorts the input
    flips = Solution().pancakeSort([3, 2, 4, 1])
    assert flips == [3, 4, 2, 3, 2]
    assert apply_flips([3, 2, 4, 1], flips) == [1, 2, 3, 4]
    assert Solution().pancakeSort([1, 2, 3]) == []       # already sorted: no flips

    # any flip sequence it returns sorts the input, within the 2N budget
    import random

    random.seed(1)
    for _ in range(200):
        data = [random.randint(-9, 9) for _ in range(random.randint(0, 15))]
        assert pancake_sort(data) == sorted(data)
        flips = Solution().pancakeSort(data)
        assert apply_flips(data, flips) == sorted(data)
        assert len(flips) <= 2 * len(data)

    print("Success.")
